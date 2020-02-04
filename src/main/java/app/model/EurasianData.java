package app.model;

import java.util.Date;

public class EurasianData {

    private int stationID;
    private Date date;
    private float temperature;
    private float dewPoint;
    private float stationAirPressure;
    private float seaLevelAirPressure;
    private float visibility;
    private float windSpeed;
    private float downfall;
    private float snowfall;
    private byte events;
    private float cloudCoverage;
    private int windDirection;


    public EurasianData(int stationID, Date date, float temperature, float dewPoint, float stationAirPressure, float seaLevelAirPressure, float visibility, float windSpeed, float downfall, float snowfall, byte events, float cloudCoverage, int windDirection) {
        this.stationID = stationID;
        this.date = date;
        this.temperature = temperature;
        this.dewPoint = dewPoint;
        this.stationAirPressure = stationAirPressure;
        this.seaLevelAirPressure = seaLevelAirPressure;
        this.visibility = visibility;
        this.windSpeed = windSpeed;
        this.downfall = downfall;
        this.snowfall = snowfall;
        this.events = events;
        this.cloudCoverage = cloudCoverage;
        this.windDirection = windDirection;
    }

    public int getStationID() {
        return stationID;
    }

    public float getTemperature() {
        return temperature;
    }

    public Date getDate() {
        return date;
    }

    public float getDewPoint() {
        return dewPoint;
    }

    public float getStationAirPressure() {
        return stationAirPressure;
    }

    public float getSeaLevelAirPressure() {
        return seaLevelAirPressure;
    }

    public float getVisibility() {
        return visibility;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public float getDownfall() {
        return downfall;
    }

    public float getSnowfall() {
        return snowfall;
    }

    public byte getEvents() {
        return events;
    }

    public float getCloudCoverage() {
        return cloudCoverage;
    }

    public int getWindDirection() {
        return windDirection;
    }

    public String getStoreString() {
        return "" + getTemperature() +
                "," + getDewPoint() +
                "," + getStationAirPressure() +
                "," + getSeaLevelAirPressure() +
                "," + getVisibility() +
                "," + getWindSpeed() +
                "," + getDownfall() +
                "," + getSnowfall() +
                "," + getEvents() +
                "," + getCloudCoverage() +
                "," + getWindDirection();
    }

}
