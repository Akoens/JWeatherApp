package app.net;

import app.model.WeatherData;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class WeatherClient {

    private Socket socket;
    private Thread thread;
    private ArrayList<WeatherDataListener> listeners;

    public WeatherClient(Socket socket) {
        this.socket = socket;
        thread = new Thread(this::run);
        listeners = new ArrayList<>();
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

    private void raiseOnWeatherData(WeatherData data) {
        for (WeatherDataListener listener : listeners) {
            listener.onWeatherData(data);
        }
    }

    private void run() { //TODO::Add sanitization.
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null && isAlive()) {
                System.out.println(line);
            }
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
