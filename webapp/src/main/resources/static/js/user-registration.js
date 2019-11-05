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
	$("#inputPostCode").on('input', function() {
		$("#inputPostCode").parent().children("div.invalid-feedback").empty();
		if($("#inputPostCode").val().length > 0) {
			$("#inputPostCode").parent().children("div.invalid-feedback").append("<p>Niepoprawny kod pocztowy</p>");
		} else {
			$("#inputPostCode").parent().children("div.invalid-feedback").append("<p>Wymagane</p>");
		}
	});

	$("#inputPhoneNumber").on('input', function() {
		$("#inputPhoneNumber").parent().children("div.invalid-feedback").empty();
		if($("#inputPhoneNumber").val().length > 0) {
			$("#inputPhoneNumber").parent().children("div.invalid-feedback").append("<p>Niepoprawny numer telefonu</p>");
		} else {
			$("#inputPhoneNumber").parent().children("div.invalid-feedback").append("<p>Wymagane</p>");
		}
	});

	$("#inputEmail").on('input', function() {
		$("#inputEmail").parent().children("div.invalid-feedback").empty();
		if($("#inputEmail").val().length > 0) {
			$("#inputEmail").parent().children("div.invalid-feedback").append("<p>Niepoprawny email</p>");
		} else {
			$("#inputEmail").parent().children("div.invalid-feedback").append("<p>Wymagane</p>");
		}
	});

});