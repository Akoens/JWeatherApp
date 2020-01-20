document.addEventListener('DOMContentLoaded', function() {
	//Init modal
	var elems = document.querySelectorAll('.modal');
	var instances = M.Modal.init(elems, {
		startingTop: "20%",
		endingTop: "20%",
	});

	//Init sidenav
	var elems = document.querySelectorAll('.sidenav');
	var instances = M.Sidenav.init(elems, null);
});