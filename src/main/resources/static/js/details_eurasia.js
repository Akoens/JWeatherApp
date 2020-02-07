Date.prototype.addHours = function(h) {
  this.setTime(this.getTime() + (h*60*60*1000));
  return this;
}

var tableEl = document.getElementById("table-content");
let id = parseInt(document.getElementById("stationID").value);
function appendData(date, temperature, dewpoint, stationairpressure, sealevelairpressure, visibility, windspeed, downfall, snowfall, events, cloudcoverage, winddirection) {
    var el = document.createElement("tr");
    var tdDate = document.createElement("td");
    var tdTemperature = document.createElement("td");
    var tdDewPoint = document.createElement("td");
    var tdStationAirPressure = document.createElement("td");
    var tdSeaLevelAirPressure = document.createElement("td");
    var tdVisibility = document.createElement("td");
    var tdWindSpeed = document.createElement("td");
    var tdDownfall = document.createElement("td");
    var tdSnowfall = document.createElement("td");
    var tdEvents = document.createElement("td");
    var tdCloudCoverage = document.createElement("td");
    var tdWindDirection = document.createElement("td");

    tdDate.innerHTML = ("0" + date.getHours()).slice(-2) + ":" + ("0" + date.getMinutes()).slice(-2);
    tdTemperature.innerHTML = temperature;
    tdDewPoint.innerHTML = dewpoint;
    tdStationAirPressure.innerHTML = stationairpressure;
    tdSeaLevelAirPressure.innerHTML = sealevelairpressure;
    tdVisibility.innerHTML = visibility;
    tdWindSpeed.innerHTML = windspeed;
    tdDownfall.innerHTML = downfall;
    tdSnowfall.innerHTML = snowfall;
    tdEvents.innerHTML = events;
    tdCloudCoverage.innerHTML = cloudcoverage;
    tdWindDirection.innerHTML = winddirection;

    el.appendChild(tdDate);
    el.appendChild(tdTemperature);
    el.appendChild(tdDewPoint);
    el.appendChild(tdStationAirPressure);
    el.appendChild(tdSeaLevelAirPressure);
    el.appendChild(tdVisibility);
    el.appendChild(tdWindSpeed);
    el.appendChild(tdDownfall);
    el.appendChild(tdSnowfall);
    el.appendChild(tdEvents);
    el.appendChild(tdCloudCoverage);
    el.appendChild(tdWindDirection);
    tableEl.prepend(el);
}

var xhr = new XMLHttpRequest();
xhr.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
        for (var d of this.response.data) {
            console.log(d);
            appendData(
                new Date(Date.parse(d.date.replace("CET", ""))).addHours(2),
                d.temperature,
                d.dew_point,
                d.station_air_pressure,
                d.sea_level_air_pressure,
                d.visibility,
                d.wind_speed,
                d.downfall,
                d.snowfall,
                d.events,
                d.cloud_coverage,
                d.wind_direction
            );
        }
    }
};
xhr.responseType = 'json';
xhr.open("GET", "/api/station/eurasia/" + id + "/historical", true);
xhr.send();