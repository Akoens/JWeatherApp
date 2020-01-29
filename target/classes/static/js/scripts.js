


let count = document.getElementById('count');
let dataset = [12, 19, 3, 5, 2, 3, 3];
let labelset = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];
let linecolor = "#EF1A24";
let list = document.getElementById('history-list');
var ctx = document.getElementById('dailyChart').getContext('2d');
var dailyChart = new Chart(ctx, {
    type: 'line',
    data: {
        labels: labelset,
        datasets: [{
            label: 'Temperature',
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
    labelset.push(labelset[0]);
    labelset.shift();
    dailyChart.update();
    let element = document.createElement('LI');
    let node = document.createTextNode(number);
    element.appendChild(node);
    list.appendChild(element);
}, 5000);