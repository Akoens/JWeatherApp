let count = document.getElementById('count');
let dataset = [12, 19, 3, 5, 2, 3, 3];
let labelset = ['T-60', 'T-50', 'T-40', 'T-30', 'T-20', 'T-10', 'T'];
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
                    beginAtZero: true
                }
            }]
        }
    },
});

setInterval(function() {
    let number = Math.floor(Math.random() * 40) + 1;
    dataset.push(number);
    dataset.shift();
    dailyChart.update();
    let element = document.createElement('LI');
    let node = document.createTextNode(number);
    element.appendChild(node);
    list.appendChild(element);
}, 5000);


mapboxgl.accessToken = 'pk.eyJ1IjoibnVvbG9uIiwiYSI6ImNrNjZkdmx2ZDAzbGQza3Bqd2JlbWdra2wifQ.tIEIuYnogcurldkxvowetA'; // Nick's Access Token
var map = new mapboxgl.Map({
    container: 'map', // container id
    style: '/json/mapstyle.json', // stylesheet location
    center: [31.55, 31.1], // starting position
    zoom: 7
});
