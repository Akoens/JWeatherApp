package app.store;

import app.model.AfricanData;
import app.model.EurasianData;
import app.util.FileUtil;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    private SimpleDateFormat dateFormat;
    private SimpleDateFormat prefixFormat;
    private HashMap<Integer, AfricanData> africanDataCache;

    public WeatherDataStore() {
        dateFormat = new SimpleDateFormat(DATA_DATE_FORMAT);
        prefixFormat = new SimpleDateFormat(DATA_PREFIX_FORMAT);
        africanDataCache = new HashMap<>();
    }

    public void touchDirectories() {
        touchDir(ROOT_PATH + DATABASE_FOLDER);
        touchDir(getEurasianPath());
        touchDir(getEurasianPath());
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
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String lastLine = FileUtil.tail(file);
            return AfricanData.fromFileLine(
                    stationID,
                    file.getName().substring(0, file.getName().lastIndexOf(".")),
                    lastLine
            );
        } catch (IOException | ParseException e) {
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

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            String lastLine = "";
            while ((line = reader.readLine()) != null)
                lastLine = line;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
            writer.write(prefixFormat.format(data.getDate()) + "," + data.getStoreString() + "\r\n");
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
            writer.write(prefixFormat.format(data.getDate()) + "," + data.getStoreString() + "\r\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
