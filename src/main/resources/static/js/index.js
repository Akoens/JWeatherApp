mapboxgl.accessToken = 'pk.eyJ1IjoibnVvbG9uIiwiYSI6ImNrNjZkdmx2ZDAzbGQza3Bqd2JlbWdra2wifQ.tIEIuYnogcurldkxvowetA'; // Nick's Access Token
var map = new mapboxgl.Map({
    container: 'map', // container id
    style: '/json/mapstyle.json', // stylesheet location
    center: [31.55, 31.1], // starting position
    zoom: 7
});
