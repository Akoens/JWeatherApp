package app.model;

import app.store.WeatherDataStore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public static EurasianData fromFileLine(int stationID, String fileName, String line) throws ParseException {
        String[] data = line.split(",");
        if (data.length != 12)
            return null;

        SimpleDateFormat dateFormat = new SimpleDateFormat(WeatherDataStore.DATA_DATE_FORMAT + " " + WeatherDataStore.DATA_PREFIX_FORMAT);
        return new EurasianData(
                stationID, //stationID
                dateFormat.parse(fileName + " " + data[0]), //date
                Float.parseFloat(data[1]), //temperature
                Float.parseFloat(data[2]), //dewPoint
                Float.parseFloat(data[3]), //stationAirPressure
                Float.parseFloat(data[4]), //seaLevelAirPressure
                Float.parseFloat(data[5]), //visibility
                Float.parseFloat(data[6]), //windspeed
                Float.parseFloat(data[7]), //downfall
                Float.parseFloat(data[8]), //snowfall
                Byte.parseByte(data[9]), //events
                Float.parseFloat(data[10]), //cloudCoverage
                Integer.parseInt(data[11]) //windDirection

        );
    }

    public String getStoreString() {
        return String.format(
                "%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%d,%.2f,%d",
                getTemperature(),
                getDewPoint(),
                getStationAirPressure(),
                getSeaLevelAirPressure(),
                getVisibility(),
                getWindSpeed(),
                getDownfall(),
                getSnowfall(),
                getEvents(),
                getCloudCoverage(),
                getWindDirection()
        );

    }

}
