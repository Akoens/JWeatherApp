package app.handler;

import app.model.AfricanData;
import app.model.EurasianData;
import app.net.WeatherClient;
import app.net.WeatherClientListener;
import app.net.WeatherDataListener;
import app.store.WeatherDataStore;

import java.util.Arrays;

public class WeatherDataHandler implements WeatherClientListener, WeatherDataListener {

    private WeatherDataStore weatherDataStore;

    public WeatherDataHandler(WeatherDataStore weatherDataStore) {
        this.weatherDataStore = weatherDataStore;
    }

    @Override
    public void onConnect(WeatherClient client) {
        client.addWeatherDataListener(this);
        client.listen();
    }

    @Override
    public void onEurasianData(EurasianData data) {
        weatherDataStore.saveEurasianData(data);
    }

    @Override
    public void onAfricanData(AfricanData data) {
        weatherDataStore.saveAfricanData(data);
    }
}
