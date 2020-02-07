package app.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement(name = "STATIONS")
@XmlAccessorType(XmlAccessType.FIELD)
public class Stations {

    @XmlElement(name = "STATION")
    private ArrayList<Station> stations;

    public ArrayList<Station> getStations() {
        return stations;
    }

}
