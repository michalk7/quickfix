(function() {
	'use strict';
	
	// var constraints = {
	// 	userName: {
	// 		presence: true,
	// 		length: {
	// 			minimum: 4,
	// 			maximum: 20,
	// 			message: "Od 4 do 20 znaków."
	// 		},
	// 		message: "Wymagane!"
	// 	}
	// }
	
	// var form = document.querySelector("form#main");
	// form.addEventListener("submit", function(event) {
	// 	event.preventDefault();
	// 	handleFormSubmit(form);
	// });
	
	// var inputs = document.querySelectorAll("input, textarea, select");
	// for( let i  = 0; i < inputs.length; i++) {
	// 	inputs.item(i).addEventListener("change", function(event) {
	// 		let errors = validate(form, constraints) || {};
	// 		showErrorsForInput(this, errors[this.name])
	// 	});
	// }

	// function handleFormSubmit(form, input) {
	// 	let errors = validate(form, constraints);
	// 	showErrors(form, errors || {});
	// 	if(!errors) {
	// 		showSuccess();
	// 	}
	// }

	// function showErrors(form, errors) {
	// 	_.each(form.querySelectorAll("input[name], select[name]"), function(input) {
	// 		showErrorsForInput(input, errors && errors[input.name]);
	// 	});
	// }

	// function showErrorsForInput(input, errors) {
	// 	let formGroup = closestParent(input.parentNode, "form-label-group"),
	// 		feedback = formGroup.querySelector(".invalid-feedback");
	// 	if(errors) {
			
	// 	}
	// }

	// function closestParent(child, className) {
	// 	if (!child || child == document) {
	// 	  return null;
	// 	}
	// 	if (child.classList.contains(className)) {
	// 	  return child;
	// 	} else {
	// 	  return closestParent(child.parentNode, className);
	// 	}
	//   }

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

	let counter = 0;
	$("#categoriesGroup").children("div").children("input").each(function() {
		if( $(this).length > 0 && $(this)[0].checked ) {
			counter++;
		}
	})

	if(counter > 4) {
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

		if(counter1 > 4) {
			$("#categoriesError").toggle(true);
			$("#categoryTitle").addClass("text-danger");
		} else {
			$("#categoriesError").toggle(false);
			$("#categoryTitle").removeClass("text-danger");
		}
	});

});