package app.model;

import java.util.Date;

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
}
