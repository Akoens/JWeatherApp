package app.store;

import app.model.AfricanData;
import app.model.EurasianData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class WeatherDataStore {

    private static final String DATA_DATE_FORMAT = "dd-MM-yyyy";
    private static final String DATA_PREFIX_FORMAT = "HH:mm";
    private static final String DATA_EXT = "wd";
    private static final String ROOT_PATH = System.getProperty("user.dir");
    private static final String DATABASE_FOLDER = "\\database";
    private static final String EURASIAN_DATABASE_FOLDER = "\\eurasia";
    private static final String AFRICAN_DATABASE_FOLDER = "\\africa";

    private SimpleDateFormat dateFormat;
    private SimpleDateFormat prefixFormat;

    public WeatherDataStore() {
        dateFormat = new SimpleDateFormat(DATA_DATE_FORMAT);
        prefixFormat = new SimpleDateFormat(DATA_PREFIX_FORMAT);
        touchDir(ROOT_PATH + DATABASE_FOLDER);
        touchDir(ROOT_PATH + DATABASE_FOLDER + EURASIAN_DATABASE_FOLDER);
        touchDir(ROOT_PATH + DATABASE_FOLDER + AFRICAN_DATABASE_FOLDER);
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

    public void saveEurasianData(EurasianData data) {
        String path = ROOT_PATH + DATABASE_FOLDER + EURASIAN_DATABASE_FOLDER + "\\" + data.getStationID();
        touchDir(path);
        path += "\\" + dateFormat.format(data.getDate()) + "." + DATA_EXT;
        File dataFile = touchFile(path);
        if (dataFile == null) {
            System.err.println("ERROR: Bad path " + path);
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
        String path = ROOT_PATH + DATABASE_FOLDER + AFRICAN_DATABASE_FOLDER + "\\" + data.getStationID();
        touchDir(path);
        path += "\\" + dateFormat.format(data.getDate()) + "." + DATA_EXT;
        File dataFile = touchFile(path);
        if (dataFile == null) {
            System.err.println("ERROR: Bad path " + path);
        }

        try {
            FileWriter writer = new FileWriter(dataFile, true);
            writer.write(prefixFormat.format(data.getDate()) + "," + data.getStoreString() + "\r\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
