package app.net;

import app.model.AfricanData;
import app.model.EurasianData;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class WeatherClient {

    private Socket socket;
    private Thread thread;
    private ArrayList<WeatherDataListener> listeners;
    private SimpleDateFormat dateFormat;

    public WeatherClient(Socket socket) {
        this.socket = socket;
        thread = new Thread(this::run);
        listeners = new ArrayList<>();
        dateFormat = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy");
    }

    public void listen() {
        thread.start();
    }

    public void close() throws IOException {
        thread.interrupt();
        socket.close();
    }

    public boolean isAlive() {
        return socket.isConnected() && !socket.isClosed();
    }

    public void addWeatherDataListener(WeatherDataListener listener) {
        listeners.add(listener);
    }

    private void raiseOnEurasianData(EurasianData data) {
        for (WeatherDataListener listener : listeners)
            listener.onEurasianData(data);
    }

    private void raiseOnAfricanData(AfricanData data) {
        for (WeatherDataListener listener : listeners)
            listener.onAfricanData(data);
    }

    private void run() { //TODO::Add sanitization.
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("STATUS: WeatherClient connected");
            String line;
            while ((line = reader.readLine()) != null && isAlive()) {
                String[] data = line.split(",");
                System.out.println(line);
                if (data.length < 4) {
                    handleAfricanData(data);
                } else {
                    handleEurasianData(data);
                }
            }
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleEurasianData(String[] data) {
        if (data.length != 13)
            return;

        try {
            EurasianData eurasianData = new EurasianData(
                    Integer.parseInt(data[0]), //stationID
                    dateFormat.parse(data[1]), //date
                    Float.parseFloat(data[2]), //temperature
                    Float.parseFloat(data[3]), //dewPoint
                    Float.parseFloat(data[4]), //stationAirPressure
                    Float.parseFloat(data[5]), //seaLevelAirPressure
                    Float.parseFloat(data[6]), //visibility
                    Float.parseFloat(data[7]), //windspeed
                    Float.parseFloat(data[8]), //downfall
                    Float.parseFloat(data[9]), //snowfall
                    Byte.parseByte(data[10]), //events
                    Float.parseFloat(data[11]), //cloudCoverage
                    Integer.parseInt(data[12]) //windDirection

            );
            raiseOnEurasianData(eurasianData);
        } catch (NumberFormatException | ParseException e) {
            System.err.println("ERROR: Bad data");
        }
    }

    private void handleAfricanData(String[] data) {
        if (data.length != 3)
            return;

        try {
            AfricanData africanData = new AfricanData(
                    Integer.parseInt(data[0]),
                    dateFormat.parse(data[1]),
                    Double.parseDouble(data[2])
            );
            raiseOnAfricanData(africanData);
        } catch (NumberFormatException | ParseException e) {
            System.err.println("ERROR: Bad data");
        }
    }

}
