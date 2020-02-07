package app.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "STATION")
@XmlAccessorType(XmlAccessType.FIELD)
public class Station {

    @XmlElement(name = "ID")
    private int stationID;

    @XmlElement(name = "NAME")
    private String name;

    @XmlElement(name = "COUNTRY")
    private String country;

    @XmlElement(name = "CONTINENT")
    private String continent;

    public int getStationID() {
        return stationID;
    }

    public void setStationID(int stationID) {
        this.stationID = stationID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    @Override
    public String toString() {
        return "Station{" +
                "stationID=" + stationID +
                ", name=" + name +
                ", country=" + country +
                ", continent=" + continent +
                '}';
    }
}