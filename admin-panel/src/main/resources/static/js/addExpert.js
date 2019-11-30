(function() {
	'use strict';
	
	window.addEventListener('load', function() {
	// Fetch all the forms we want to apply custom Bootstrap validation styles to
	var forms = document.getElementsByClassName('needs-validation');
	// Loop over them and prevent submission
	var validation = Array.prototype.filter.call(forms, function(form) {
		form.addEventListener('submit', function(event) {
			let counterCheckbox = 0;
			$("#categoriesGroup").children("div").children("input").each(function() {
				if( $(this).length > 0 && $(this)[0].checked ) {
					counterCheckbox++;
				}
			})
			
			let notEnoughChecked = false;
			
			if(counterCheckbox < 1) {
				$("#categoriesNotEnoughError").toggle(true);
				$("#categoryTitle").addClass("text-danger");
				notEnoughChecked = true;
			}
			
			let tooManyChecked = false;
			if(counterCheckbox > 4) {
				tooManyChecked = true;
			}
			
			if (form.checkValidity() === false || notEnoughChecked || tooManyChecked) {
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

	let counter = 0;
	$("#categoriesGroup").children("div").children("input").each(function() {
		if( $(this).length > 0 && $(this)[0].checked ) {
			counter++;
		}
	})
	
	if(counter < 1 && $("#main").hasClass("was-validated")) {
		$("#categoriesNotEnoughError").toggle(true);
		$("#categoryTitle").addClass("text-danger");
	}

	if(counter > 4 && $("#main").hasClass("was-validated")) {
		$("#categoriesError").show();
		$("#categoryTitle").addClass("text-danger");
	}
	
	$("input[type=checkbox]").on('change', function() {
		let counter1 = 0;
		$("#categoriesGroup").children("div").children("input").each(function() {
			if( $(this).length > 0 && $(this)[0].checked ) {
				counter1++;
			}
		})
		
		if(counter1 > 0) {
			$("#categoriesNotEnoughError").toggle(false);
			$("#categoryTitle").removeClass("text-danger");
		} else {
			$("#categoriesNotEnoughError").toggle(true);
			$("#categoryTitle").addClass("text-danger");
		}

		if(counter1 > 4) {
			$("#categoriesError").toggle(true);
			$("#categoryTitle").addClass("text-danger");
		} else {
			$("#categoriesError").toggle(false);
			if(counter1 > 0) {
				$("#categoryTitle").removeClass("text-danger");
			}
		}
	});

});