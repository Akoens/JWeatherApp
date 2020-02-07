package app.model;

import app.store.WeatherDataStore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AfricanData {

    private int stationID;
    private Date date;
    private double heatIndex;

    public AfricanData(int stationID, Date date, double heatIndex) {
        this.stationID = stationID;
        this.date = date;
        this.heatIndex = heatIndex;
    }

    public int getStationID() {
        return stationID;
    }

    public Date getDate() {
        return date;
    }

    public double getHeatIndex() {
        return heatIndex;
    }

    public String getStoreString() {
        return String.format("%.2f", getHeatIndex());
    }

    public static AfricanData fromFileLine(int stationID, String fileName, String line) throws ParseException {
        String[] data = line.split(",");
        if (data.length != 2)
            return null;

        SimpleDateFormat dateFormat = new SimpleDateFormat(WeatherDataStore.DATA_DATE_FORMAT + " " + WeatherDataStore.DATA_PREFIX_FORMAT);
        return new AfricanData(stationID, dateFormat.parse(fileName + " " + data[0]), Double.parseDouble(data[1]));
    }

    public String toJSON() {
        return "{" +
                    "\"stationID\":" + stationID + "," +
                    "\"date\":\"" + date + "\"," +
                    String.format(Locale.US, "\"heatIndex\":%.2f", heatIndex) +
                '}';
    }

    @Override
    public String toString() {
        return "AfricanData{" +
                "stationID=" + stationID +
                ", date=" + date +
                ", heatIndex=" + heatIndex +
                '}';
    }
}
