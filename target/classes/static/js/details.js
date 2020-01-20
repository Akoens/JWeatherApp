function init() {
	update_ports();
}

function update_port(port) {
	var xhr = new XMLHttpRequest();
	xhr.onreadystatechange = function() { 
		if (xhr.readyState == 4 && xhr.status == 200) {
			port.getElementsByClassName("status")[0].innerHTML = this.response.status;
			port.getElementsByClassName("temperature")[0].innerHTML = this.response.temperature;
			port.getElementsByClassName("lux")[0].innerHTML = this.response.lux;
			port.getElementsByClassName("distance")[0].innerHTML = this.response.distance;
		}
	}
	xhr.responseType = "json"
	xhr.open("GET", "/api/data?port=" + port.getAttribute("port") + "&mode=single");
	xhr.send();
}

function update_ports() {
	ports = document.getElementById("ports");
	for (port of ports.children) { 
		if (port.getAttribute("port") == null)
			continue;
		update_port(port);
	}
	setTimeout(update_ports, 5000);
}

function force_action(action) {
	port = document.getElementById("port");
	var xhr = new XMLHttpRequest();
	xhr.onreadystatechange = function() { 
		if (xhr.readyState == 4) {
			if (xhr.status == 200) {
				ports = document.getElementById("ports");
				for (port of ports.children) { 
					if (port.getAttribute("port") == null)
						continue;
					port.getElementsByClassName("auto")[0].checked = action != "open" && action != "close"
					port.getElementsByClassName("auto")[1].checked = action == "open" || action == "close"
				}
				M.toast({html: 'Command: ' + action + ' success!'})
			} else {
				M.toast({html: 'Command failed: ' + action})
			}
		}
	}
	xhr.open("GET", "/details/command?port=" + port.value + "&action=" + action);
	xhr.send();
}

document.addEventListener('DOMContentLoaded', function() {
	init();
});