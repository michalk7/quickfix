$(document).ready(function() {
	
	const swalWithBootstrapButtons = Swal.mixin({
		  customClass: {
		    confirmButton: 'btn btn-success',
		    cancelButton: 'btn btn-danger mr-4'
		  },
		  buttonsStyling: false
		});
	
	
	$('.verify-button').each(function() {
		$(this).click(function() {
			let verifyUrl = $(this).attr("data-href");
			
			let valueToSend = true;
			
			let dataToSend = {
				verified: valueToSend
			};
			
			swalWithBootstrapButtons.fire({
				  title: 'Czy na pewno chcesz zweryfikować konto tego użytkownika?',
				  icon: 'question',
				  confirmButtonText: 'Zweryfikuj',
				  showCancelButton: true,
				  showCloseButton: true,
				  reverseButtons: true,
				  cancelButtonText: 'Anuluj'
			  }).then((result) => {
				  if(result.value) {
					  
					  let token = $("meta[name='_csrf']").attr("content");
					  let header = $("meta[name='_csrf_header']").attr("content");
					  
					  $.ajax({
						 type: 'PUT',
						 url: verifyUrl,
						 headers: {
							 [header]: token
						 },
						 data: JSON.stringify(dataToSend),
						 beforeSend: function() {
							swalWithBootstrapButtons.fire({
		          				  title: 'Zapisywanie zmian, proszę czekać...',
		                  		  confirmButtonText: 'Zapisywanie',
		                  		  showCancelButton: false,
		                  		  showLoaderOnConfirm: true,
		                  		  onOpen: () => {
		                  			  swalWithBootstrapButtons.showLoading();
		                  		  },
		                  		  allowOutsideClick: false,
		                  		  allowEscapeKey: false
		                  		});
						 },
						 success: function(response) {
							 swalWithBootstrapButtons.close();
							 
							 if(!response.error) {
								 swalWithBootstrapButtons.fire({
									title: 'Gotowe!',
									icon: 'success',
									text: 'Konto użytkownika zostało zweryfikowane. W ciągu 5 sekund nastapi odświeżenie strony.',
									timer: 5000,
									allowOutsideClick: false,
									allowEscapeKey: false
								 }).then((resultT) => {
									 window.location.reload(true);
								 });
							 } else {
								 swalWithBootstrapButtons.fire({
									 title: 'Błąd',
									 html: 'Zmiany nie zostały zapisane<br><br>' + response.message,
		  							 icon: 'error'
								 });
							 }
							 
						 },
						 contentType: "application/json; charset=utf-8",
		  				 error: function() {
		  					 swalWithBootstrapButtons.fire(
		  		    		  		'Błąd',
		  		    		  		'Zmiany nie zostały wprowadzone',
		  		    		  		'error');
		  				 },
		  				 dataType: "json"
					  });
				  }
			  });
			
			
		});
	});
	
	
	
});