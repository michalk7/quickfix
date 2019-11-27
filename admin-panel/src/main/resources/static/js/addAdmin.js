(function() {
	'use strict';

	window.addEventListener('load', function() {
	// Fetch all the forms we want to apply custom Bootstrap validation styles to
	var forms = document.getElementsByClassName('needs-validation');
	// Loop over them and prevent submission
	var validation = Array.prototype.filter.call(forms, function(form) {
		form.addEventListener('submit', function(event) {
			
			if (form.checkValidity() === false) {
				event.preventDefault();
				event.stopPropagation();
			}
			form.classList.add('was-validated');
		}, false);
	});
	}, false);
})();



$(document).ready(function() {

	$("#inputEmail").on('input', function() {
		$("#inputEmail").parent().children("div.invalid-feedback").empty();
		if($("#inputEmail").val().length > 0) {
			$("#inputEmail").parent().children("div.invalid-feedback").append("<p>Niepoprawny email</p>");
		} else {
			$("#inputEmail").parent().children("div.invalid-feedback").append("<p>Wymagane</p>");
		}
	});

});