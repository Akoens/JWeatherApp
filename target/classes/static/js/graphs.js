function init() {
    loop();
}

function update_averages() {
    update_statics();
    update_graphs();
}

function update_statics() {
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() { 
        if (xhr.readyState == 4 && xhr.status == 200) {
            let d = new Date();
            for (timeEl of document.getElementsByClassName("currentTime")) 
                timeEl.innerHTML = ""+ d.getHours() + ":" + d.getMinutes();

            let r = this.response;
            document.getElementById("avgTempToday").innerHTML = r.avgTempToday == null ? "0" : r.avgTempToday;
            document.getElementById("avgTempMonth").innerHTML = r.avgTempMonth == null ? "0" : r.avgTempMonth;
            document.getElementById("avgLuxToday").innerHTML = r.avgLuxToday == null ? "0" : r.avgLuxToday;
            document.getElementById("avgLuxMonth").innerHTML = r.avgLuxMonth == null ? "0" : r.avgLuxMonth;
            document.getElementById("avgLengthToday").innerHTML = r.avgLengthToday == null ? "0" : r.avgLengthToday;
            document.getElementById("avgLengthMonth").innerHTML = r.avgLengthMonth == null ? "0" : r.avgLengthMonth;
        }
    }
    xhr.responseType = "json"
    xhr.open("GET", "/api/data?mode=graphs"); // true for asynchronous 
    xhr.send();
}

function update_graphs() {
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() { 
        if (xhr.readyState == 4 && xhr.status == 200) {
            update_temperature_graph(this.response);
            update_lux_graph(this.response);
            update_length_graph(this.response);
        }
    }
    xhr.responseType = "json"
    xhr.open("GET", "/api/data?mode=graphs_hours"); // true for asynchronous 
    xhr.send();
}

function loop() {
    update_averages();
    setTimeout(loop, 5000);
}

function update_temperature_graph(json){
    let labelSet = []
    let dataSet = []

    for (row of json.Temperatures) {
        let label = row[0].split(" ")[1];
        let h = label.split(":")[0];
        let m = label.split(":")[1];
        labelSet.push(h + ":" + m);
    }

    for (row of json.Temperatures)
        dataSet.push(row[1]);

    let chartTC = document.getElementById('chartTC').getContext('2d');
    let linechartTC = new Chart(chartTC, {
        type: 'line',
        data: {
            labels: labelSet,
            datasets: [{
                label: 'Last 24 hours',
                data: dataSet,
                backgroundColor: "rgba(205, 151, 187, 0.6)", //old
                borderColor: "rgba(205, 151, 187, 1)", //old
                pointBackgroundColor: "rgba(205, 151, 187, 1)", //old
                radius: 6,
                hoverRadius: 10,
                hitRadius: 30,
            }]
        },
        options: {
            animation: false,
            responsive: true,
            tooltips: {
                display: true,
                backgroundColor: "#708090",
                bodyFontSize: 25,
                callbacks: {
                    label: function (tooltipItems, data) {
                        multistringtext = ['Time: '+tooltipItems.xLabel];
                        multistringtext.push('Temp: '+tooltipItems.yLabel + ' °C')
                        return multistringtext;

                    },
                    title: function () {

                    }

                }
            },
            scales: {
                yAxes: [{
                    gridLines: {
                        display: true,
                        lineWidth: 3,
                        zeroLineWidth: 5
                    },
                    scaleLabel: {
                        display: true,
                        labelString: 'Temperature in Celcius (°C)',
                        fontSize: 30,
                        fontStyle: 'Bold',
                    },
                    ticks: {
                        fontSize: 20
                    }
                }],
                xAxes: [{
                    gridLines: {
                        display: true,
                        lineWidth: 3,
                        zeroLineWidth: 5
                    },
                    scaleLabel: {
                        display: true,
                        labelString: 'Time in hours',
                        fontSize: 30,
                        fontStyle: 'Bold',
                    },
                    ticks: {
                      fontSize: 20
                    }
                }]
            },
            title: {
                display: true,
                text: 'Temperature',
                fontSize: 35,
            },
            legend: {
                display: true,
                position: 'top',
                labels: {
                    fontColor: 'Teal',
                    fontSize: 17,
                    fontStyle: 'Bold',
                }
            }
        },

    });
}

function update_lux_graph(json){
    let labelSet = []
    let dataSet = []

    for (row of json.Lux) {
        let label = row[0].split(" ")[1];
        let h = label.split(":")[0];
        let m = label.split(":")[1];
        labelSet.push(h + ":" + m);
    }

    for (row of json.Lux)
        dataSet.push(row[1]);

    let chartLC = document.getElementById('chartLC').getContext('2d');
    let linechartLC = new Chart(chartLC, {
        type: 'line',
        data: {
            labels: labelSet,
            datasets: [{
                label: 'Last 24 hours',
                data: dataSet,
                backgroundColor: "rgba(151, 205, 187, 0.6)", //old
                borderColor: "rgba(151, 205, 187, 1)",
                pointBackgroundColor: "rgba(151, 205, 187, 1)",

                radius: 6,
                hoverRadius: 10,
                hitRadius: 30,
            }]
        },
        options: {
            animation: false,
            tooltips: {
                display: true,
                backgroundColor: "#708090",
                bodyFontSize: 25,
                callbacks: {
                    label: function (tooltipItems, data) {
                        multistringtext = ['Time: '+tooltipItems.xLabel];
                        multistringtext.push('Lux: '+tooltipItems.yLabel + ' lx')
                        return multistringtext;

                    },
                    title: function () {

                    }

                }
            },
            scales: {
                yAxes: [{
                    gridLines: {
                        display: true,
                        lineWidth: 3,
                        zeroLineWidth: 5
                    },
                    scaleLabel: {
                        display: true,
                        labelString: 'Illuminance in Lux (lx)',
                        fontSize: 30,
                        fontStyle: 'Bold',
                    },
                    ticks: {
                        fontSize: 20
                    }
                }],
                xAxes: [{
                    gridLines: {
                        display: true,
                        lineWidth: 3,
                        zeroLineWidth: 5
                    },
                    scaleLabel: {
                        display: true,
                        labelString: 'Time in hours',
                        fontSize: 30,
                        fontStyle: 'Bold',
                    },
                    ticks: {
                        fontSize: 20
                    }
                }]
            },
            title: {
                display: true,
                text: 'Light',
                fontSize: 35,
            },
            legend: {
                display: true,
                position: 'top',
                labels: {
                    fontColor: 'Teal',
                    fontSize: 17,
                    fontStyle: 'Bold',
                }
            }
        },

    });
}

function update_length_graph(json) {
    let labelSet = []
    let dataSet = []

    for (row of json.Lengths) {
        let label = row[0].split(" ")[1];
        let h = label.split(":")[0];
        let m = label.split(":")[1];
        labelSet.push(h + ":" + m);
    }

    for (row of json.Lengths)
        dataSet.push(row[1]);

   let chartRD = document.getElementById('chartRD').getContext('2d');
    let linechartRD = new Chart(chartRD, {
        type: 'line',
        data: {
            labels: labelSet,
            datasets: [{
                label: 'Last 24 hours',
                data: dataSet,
                backgroundColor: "rgba(151, 187, 205, 0.6)", //old
                borderColor: "rgba(151, 187, 205, 1)", //old
                pointBackgroundColor: "rgba(151, 187, 205, 1)", //old
                radius: 6,
                hoverRadius: 10,
                hitRadius: 30,
            }]
        },
        options: {
            animation: false,
            tooltips: {
                display: true,
                backgroundColor: "#708090",
                bodyFontSize: 25,
                callbacks: {
                    label: function (tooltipItems, data) {
                        multistringtext = ['Time: '+tooltipItems.xLabel];
                        multistringtext.push('distance: '+tooltipItems.yLabel + ' cm')
                        return multistringtext;

                    },
                    title: function () {

                    }

                }
            },
            scales: {
                yAxes: [{
                    gridLines: {
                        display: true,
                        lineWidth: 3,
                        zeroLineWidth: 5
                    },
                    scaleLabel: {
                        display: true,
                        labelString: 'Roll-down length in centimeters (cm)',
                        fontSize: 30,
                        fontStyle: 'Bold',
                    },
                    ticks: {
                        fontSize: 20
                    }
                }],
                xAxes: [{
                    gridLines: {
                        display: true,
                        lineWidth: 3,
                        zeroLineWidth: 5
                    },
                    scaleLabel: {
                        display: true,
                        labelString: 'Time in hours',
                        fontSize: 30,
                        fontStyle: 'Bold',
                    },
                    ticks: {
                        fontSize: 20
                    }
                }]
            },
            title: {
                display: true,
                text: 'Distance',
                fontSize: 35,
            },
            legend: {
                display: true,
                position: 'top',
                labels: {
                    fontColor: 'Teal',
                    fontSize: 17,
                    fontStyle: 'Bold',
                }
            }
        },

    });
}

document.addEventListener('DOMContentLoaded', function() {
    init();
});