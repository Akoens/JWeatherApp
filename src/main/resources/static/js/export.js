function prepareCSV() {
	var xhr = new XMLHttpRequest();
	xhr.onreadystatechange = function() {
		console.log(this);
		if (this.readyState == 4 && this.status == 200) {
			console.log("DONE");
			let dlEAEnable = document.getElementById("eurasia-download-enabled");
			let dlEADisabled = document.getElementById("eurasia-download-disabled");

			let dlAFEnable = document.getElementById("africa-download-enabled");
			let dlAFDisabled = document.getElementById("africa-download-disabled");

			dlEAEnable.style.display = "block";
			dlEADisabled.style.display = "none";
			dlAFEnable.style.display = "block";
			dlAFDisabled.style.display = "none";

			let dlButton = document.getElementById("dl-button");
			dlButton.setAttribute("disabled", true);
		}
	};
	xhr.open("GET", "/api/export/", true);
	xhr.send();
}