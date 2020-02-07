Date.prototype.addHours = function(h) {
  this.setTime(this.getTime() + (h*60*60*1000));
  return this;
}

function minDiff(date1, date2) {
    var diff = date1 - date2;
    return Math.floor((diff/1000)/60);
}

var tableEl = document.getElementById("table-content");
function appendData(date, heatIndex) {
    var el = document.createElement("tr");
    var td1 = document.createElement("td");
    var td2 = document.createElement("td");
    td1.innerHTML = ("0" + date.getDate()).slice(-2) + "-" + ("0"+(date.getMonth()+1)).slice(-2) + "-" +
                date.getFullYear() + " " + ("0" + date.getHours()).slice(-2) + ":" + ("0" + date.getMinutes()).slice(-2);
    td2.innerHTML = heatIndex;
    el.appendChild(td1);
    el.appendChild(td2);
    tableEl.prepend(el);
    if (tableEl.children.length > 3)
    tableEl.lastElementChild.remove(); 
}

let count = document.getElementById('count');
let dataset = [];
let labelset = [];
for (var x=60; x>0; x--) {
    dataset.push(0);
    labelset.push(x);
}
let linecolor = "#EF1A24";
let list = document.getElementById('history-list');
var ctx = document.getElementById('dailyChart').getContext('2d');
var dailyChart = new Chart(ctx, {
    type: 'line',
    data: {
        labels: labelset,
        datasets: [{
            label: 'Heat Index Temperature in degrees °C',
            data: dataset,
            borderColor: linecolor,
            backgroundColor: "rgba(239,26,36,0.23)"
        }]
    },
    options: {
        responsive: false,
        scales: {
            yAxes: [{
                ticks: {
                    max: 60,
                    min: 0,
                    beginAtZero: true
                }
            }]
        }
    },
});

let id = parseInt(document.getElementById("stationID").value);
let currentTempEl = document.getElementById("current-temp");
let currentTime = new Date();
setInterval(function() {
    var timeDiff = minDiff(new Date(), currentTime);
    for (var x=0; x<timeDiff; x++) {
        dataset.push(0);
        dataset.shift();
        currentTime = new Date();
    }

    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var d = this.response;
            var date = new Date(Date.parse(d.date.replace("CET", ""))).addHours(2);
            var diff = minDiff(new Date(), date);
            if (diff > 59 || diff < 0)
                return;
            dataset[dataset.length - diff] = d.heatIndex;
            currentTempEl.innerHTML = d.heatIndex;
            if (diff < 2)
                appendData(date, d.heatIndex);
            dailyChart.update();
        }
    };
    xhr.responseType = 'json';
    xhr.open("GET", "/api/station/africa/" + id + "/live", true);
    xhr.send();
}, 5000);

var xhr = new XMLHttpRequest();
xhr.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
        var x = 0;
        for (d of this.response.data) {
            var date = new Date(Date.parse(d.date.replace("CET", ""))).addHours(2);
            var diff = minDiff(new Date(), date);
            if (diff > 59 || diff < 0)
                continue;
            dataset[dataset.length - diff] = d.heatIndex;
            currentTempEl.innerHTML = d.heatIndex;
            if (x < 3) {
                appendData(date, d.heatIndex);
            }
            x++;
        }
        dailyChart.update();
    }
};
xhr.responseType = 'json';
xhr.open("GET", "/api/station/africa/" + id + "/historical", true);
xhr.send();