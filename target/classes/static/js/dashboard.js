function init() {
	update_ports();
}

function update_port(port) {
	var xhr = new XMLHttpRequest();
	xhr.onreadystatechange = function() { 
		if (xhr.readyState == 4 && xhr.status == 200) {
			port.getElementsByClassName("status")[0].innerHTML = this.response.status;
			port.getElementsByClassName("mode")[0].innerHTML = this.response.mode == 1 ? "Autonomous" : (this.response.mode == 2 ? "Manual" : "Unkown");
			port.getElementsByClassName("temperature")[0].innerHTML = this.response.temperature;
			port.getElementsByClassName("lux")[0].innerHTML = this.response.lux;
			port.getElementsByClassName("distance")[0].innerHTML = this.response.distance;
		}
	}

	xhr.responseType = "json"
	xhr.open("GET", "/api/data?port=" + port.getAttribute("port") + "&mode=single"); // true for asynchronous 
	xhr.send();
}

function update_ports() {
	ports = document.getElementById("ports");
	for (a of ports.children) { 
		port = a.children[0];
		if (port.getAttribute("port") == null)
			continue;
		update_port(port);
	}
	setTimeout(update_ports, 5000);
}

document.addEventListener('DOMContentLoaded', function() {
	init();
});