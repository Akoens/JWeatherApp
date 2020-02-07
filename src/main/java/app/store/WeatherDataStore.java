package app.store;

import app.model.AfricanData;
import app.model.EurasianData;
import app.model.Station;
import app.model.Stations;
import app.util.FileUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class WeatherDataStore {

    public static final String DATA_DATE_FORMAT = "dd-MM-yyyy";
    public static final String DATA_PREFIX_FORMAT = "HH:mm";
    private static final String DATA_EXT = "wd";
    private static final String ROOT_PATH = System.getProperty("user.dir");
    private static final String DATABASE_FOLDER = "\\database";
    private static final String EURASIAN_DATABASE_FOLDER = "\\eurasia";
    private static final String AFRICAN_DATABASE_FOLDER = "\\africa";
    private static final String EXPORT_DATABASE_FOLDER = "\\export";
    private static final int MAX_DATA_AGE_MONTHS = 1;

    private SimpleDateFormat dateFormat;
    private SimpleDateFormat prefixFormat;
    private HashMap<Integer, AfricanData> africanDataCache;
    private HashMap<Integer, Station> stations;

    public WeatherDataStore() {
        dateFormat = new SimpleDateFormat(DATA_DATE_FORMAT);
        prefixFormat = new SimpleDateFormat(DATA_PREFIX_FORMAT);
        africanDataCache = new HashMap<>();
        stations = new HashMap<>();
        for (Station station : loadStations().getStations()) {
            this.stations.put(station.getStationID(), station);
        }
    }

    public void touchDirectories() {
        touchDir(ROOT_PATH + DATABASE_FOLDER);
        touchDir(getEurasianPath());
        touchDir(getAfricanPath());
        touchDir(getExportPath());
    }

    private File touchDir(String path) {
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdir();
        return dir;
    }

    private File touchFile(String path) {
        try {

            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Stations loadStations() {
        try {
            JAXBContext jaxb = JAXBContext.newInstance(Stations.class);
            Unmarshaller unmarshaller = jaxb.createUnmarshaller();
            return (Stations) unmarshaller.unmarshal(getClass().getClassLoader().getResourceAsStream("data/stations.xml"));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AfricanData getLatestAfricanEntry(int stationID) {
        if (africanDataCache.containsKey(stationID))
            return africanDataCache.get(stationID);

        File dir = new File(getAfricanPath(stationID));
        if (!dir.exists())
            return null;

        File file = new File(getAfricanPath(stationID) + "\\" + getWDFilePath(new Date()));
        if (!file.exists())
            return null;

        try {
            String lastLine = FileUtil.tail(file);

            if (lastLine == null)
                return null;

            return AfricanData.fromFileLine(
                    stationID,
                    FileUtil.parseDateFromWB(file),
                    lastLine
            );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AfricanData[] getPastHourAfricanData(int stationID) {
        File dir = new File(getAfricanPath(stationID));
        if (!dir.exists())
            return null;

        File file = new File(getAfricanPath(stationID) + "\\" + getWDFilePath(new Date()));
        if (!file.exists())
            return null;

        String data = FileUtil.tail2(file, 60);
        if (data == null)
            return null;

        String[] lines = data.split("\n");
        ArrayList<AfricanData> dataList = new ArrayList<>();
            for (String line : lines) {
                try {
                    dataList.add(
                            AfricanData.fromFileLine(
                                    stationID,
                                    FileUtil.parseDateFromWB(file),
                                    line
                            )
                    );
                } catch (Exception ignored) {
                }
            }
        return dataList.toArray(new AfricanData[]{});
    }

    public EurasianData[] getEurasianDataByDay(int stationID, Date date) {
        File dir = new File(getEurasianPath(stationID));
        if (!dir.exists())
            return null;

        File file = new File(getEurasianPath(stationID) + "\\" + getWDFilePath(date));
        if (!file.exists())
            return null;

        ArrayList<String> lines = new ArrayList<>();
        try {
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while((line = reader.readLine()) != null)
                lines.add(line);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        ArrayList<EurasianData> dataList = new ArrayList<>();
        try {
            for (String line : lines) {
                dataList.add(
                        EurasianData.fromFileLine(
                                stationID,
                                FileUtil.parseDateFromWB(file),
                                line
                        )
                );
            }
            return dataList.toArray(new EurasianData[]{});
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean parseCSV() {
        try {
            parseEurasianCSV();
            parseAfricanCSV();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void parseEurasianCSV() throws IOException {
        File dir = new File(getEurasianPath());
        if (!dir.exists())
            return;

        if (!dir.isDirectory())
            return;

        File csv = new File(getExportPath() + "\\eurasia.csv");
        if (csv.exists())
            csv.delete();

        csv.createNewFile();
        FileWriter writer = new FileWriter(csv, true);
        writer.write(
                "\"station_id\"," +
                        "\"date\"," +
                        "\"temperature\"," +
                        "\"dew_point\"," +
                        "\"station_air_pressure\"," +
                        "\"sea_level_air_pressure\"," +
                        "\"visibility\"," +
                        "\"wind_speed\"," +
                        "\"downfall\"," +
                        "\"snowfall\"," +
                        "\"events\"," +
                        "\"cloud_coverage\"\n"
        );

        File[] files = dir.listFiles();
        for (File file : files)
            if (file.isDirectory())
                for (File dataFile : file.listFiles()) {
                    try {
                        int stationID = Integer.parseInt(file.getName());
                        String[] lines = parseEurasianCSVLines(stationID, dataFile);
                        if (lines != null)
                            for (String line : lines)
                                writer.write(line);
                    } catch (NumberFormatException ignored) {
                    }
                }

        writer.close();
    }

    private String[] parseEurasianCSVLines(int stationID, File file) {
        if (file == null)
            return null;

        if (!file.exists())
            return null;

        ArrayList<String> lines = new ArrayList<>();
        try {
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while((line = reader.readLine()) != null) {
                EurasianData eurasianData = EurasianData.fromFileLine(stationID, FileUtil.parseDateFromWB(file), line);
                lines.add(
                        String.format(
                            "%d,%s,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%o,%.2f\n",
                                stationID,
                                eurasianData.getDate(),
                                eurasianData.getTemperature(),
                                eurasianData.getDewPoint(),
                                eurasianData.getStationAirPressure(),
                                eurasianData.getSeaLevelAirPressure(),
                                eurasianData.getVisibility(),
                                eurasianData.getWindSpeed(),
                                eurasianData.getDownfall(),
                                eurasianData.getSnowfall(),
                                eurasianData.getEvents(),
                                eurasianData.getCloudCoverage()

                        )
                );
            }
        } catch (NullPointerException | IOException | ParseException e) {
            e.printStackTrace();
            return null;
        }
        return lines.toArray(new String[]{});
    }

    private void parseAfricanCSV() throws IOException {
        File dir = new File(getAfricanPath());
        if (!dir.exists())
            return;

        if (!dir.isDirectory())
            return;

        File csv = new File(getExportPath() + "\\africa.csv");
        if (csv.exists())
            csv.delete();

        csv.createNewFile();
        FileWriter writer = new FileWriter(csv, true);
        writer.write(
                "\"station_id\"," +
                        "\"date\"," +
                        "\"heat_index\"\n"
        );

        File[] files = dir.listFiles();
        for (File file : files)
            if (file.isDirectory())
                for (File dataFile : file.listFiles()) {
                    try {
                        int stationID = Integer.parseInt(file.getName());
                        String[] lines = parseAfricanCSVLines(stationID, dataFile);
                        if (lines != null)
                            for (String line : lines)
                                writer.write(line);
                    } catch (NumberFormatException ignored) {
                    }
                }

        writer.close();
    }

    private String[] parseAfricanCSVLines(int stationID, File file) {
        if (file == null)
            return null;

        if (!file.exists())
            return null;

        ArrayList<String> lines = new ArrayList<>();
        try {
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while((line = reader.readLine()) != null) {
                AfricanData africanData = AfricanData.fromFileLine(stationID, FileUtil.parseDateFromWB(file), line);
                lines.add(
                        String.format(
                                "%d,%s,%.2f\n",
                                stationID,
                                africanData.getDate(),
                                africanData.getHeatIndex()

                        )
                );
            }
        } catch (NullPointerException | IOException | ParseException e) {
            e.printStackTrace();
            return null;
        }
        return lines.toArray(new String[]{});
    }

    public void saveEurasianData(EurasianData data) {
        String path = getEurasianPath(data.getStationID());
        touchDir(path);
        path += "\\" + getWDFilePath(data.getDate());
        File dataFile = touchFile(path);
        if (dataFile == null) {
            System.err.println("ERROR: Bad path " + path);
            return;
        }

        try {
            FileWriter writer = new FileWriter(dataFile, true);
            writer.write(prefixFormat.format(data.getDate()) + "," + data.getStoreString() + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveAfricanData(AfricanData data) {
        String path = getAfricanPath(data.getStationID());
        touchDir(path);
        path += "\\" + getWDFilePath(data.getDate());
        File dataFile = touchFile(path);
        if (dataFile == null) {
            System.err.println("ERROR: Bad path " + path);
            return;
        }

        try {
            africanDataCache.put(data.getStationID(), data);
            FileWriter writer = new FileWriter(dataFile, true);
            writer.write(prefixFormat.format(data.getDate()) + "," + data.getStoreString() + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cleanup() {
        System.out.println("STATUS: Cleaning " + MAX_DATA_AGE_MONTHS + " month(s) old data");
        int cleaned = cleanDir(new File(getEurasianPath()));
        cleaned += cleanDir(new File(getAfricanPath()));
        System.out.println("STATUS: " + cleaned + " item(s) removed");
    }

    private int cleanDir(File dir) {
        if (dir == null)
            return -1;

        if (!dir.exists())
            return -1;

        if (!dir.isDirectory())
            return -1;

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -MAX_DATA_AGE_MONTHS);
        String dateString;
        int cleaned = 0;
        File[] files = dir.listFiles();
        for (File file : files)
            if (file.isDirectory()) {
                cleaned += cleanDir(file);
            } else if ((dateString = FileUtil.parseDateFromWB(file)) != null) {
                try {
                    if (dateFormat.parse(dateString).compareTo(calendar.getTime()) < 0) {
                        System.out.println("STATUS: Deleting " + dir.getName() + "/" + file.getName());
                        file.delete();
                        cleaned++;
                    }
                } catch (ParseException ignored){}
            }
        return cleaned;
    }

    public String getAfricanCSV() throws IOException {
        return new String(Files.readAllBytes(Paths.get(getExportPath() + "\\africa.csv")), StandardCharsets.UTF_8);
    }

    public String getEurasianCSV() throws IOException {
        return new String(Files.readAllBytes(Paths.get(getExportPath() + "\\eurasia.csv")), StandardCharsets.UTF_8);
    }

    public HashMap<Integer, Station> getStations() {
        return stations;
    }

    private String getEurasianPath() {
        return ROOT_PATH + DATABASE_FOLDER + EURASIAN_DATABASE_FOLDER;
    }

    private String getEurasianPath(int stationID) {
        return getEurasianPath() + "\\" + stationID;
    }

    private String getAfricanPath() {
        return ROOT_PATH + DATABASE_FOLDER + AFRICAN_DATABASE_FOLDER;
    }

    private String getAfricanPath(int stationID) {
        return getAfricanPath() + "\\" + stationID;
    }

    private String getWDFilePath(Date date) {
        return dateFormat.format(date) + "." + DATA_EXT;
    }

    private String getExportPath() {
        return ROOT_PATH + DATABASE_FOLDER + EXPORT_DATABASE_FOLDER;
    }

}
