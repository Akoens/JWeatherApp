package app.net;

import app.model.AfricanData;
import app.model.EurasianData;

public interface WeatherDataListener {

    void onEurasianData(EurasianData data);
    void onAfricanData(AfricanData data);

}
