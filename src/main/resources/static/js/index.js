mapboxgl.accessToken = 'pk.eyJ1IjoibnVvbG9uIiwiYSI6ImNrNjZkdmx2ZDAzbGQza3Bqd2JlbWdra2wifQ.tIEIuYnogcurldkxvowetA'; // Nick's Access Token
var map = new mapboxgl.Map({
    container: 'map', // container id
    style: '/json/mapstyle.json', // stylesheet location
    center: [31.55, 31.1], // starting position
    zoom: 7
});

map.on('click', 'data', function(e) {
	window.location = "/details/" + e.features[0].properties.id;
});
 
// Change the cursor to a pointer when the mouse is over the places layer.
map.on('mouseenter', 'data', function() {
	map.getCanvas().style.cursor = 'pointer';
});
 
// Change it back to a pointer when it leaves.
map.on('mouseleave', 'data', function() {
	map.getCanvas().style.cursor = '';
});