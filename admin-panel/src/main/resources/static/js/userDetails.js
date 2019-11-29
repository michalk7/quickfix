function listCategories(categories) {
	if(categories == null || categories.length == 0) {
		return '';
	} else {
		let result = '<h4>Kategorie:</h4>';
		for(const category of categories) {
			result += category + '<br>';
		}
		return result;
	}
}

$(document).ready(function() {
	
	const detailsSwal = Swal.mixin({
		title: 'Informacje szczegółowe',
		icon: 'info',
		showConfirmButton: false,
		showCloseButton: true
	});
	
	const swalWithBootstrapButtons = Swal.mixin({
		  customClass: {
		    confirmButton: 'btn btn-success',
		    cancelButton: 'btn btn-danger mr-4'
		  },
		  buttonsStyling: false
		});
	
	$(".details-button").each(function() {
		$(this).click(function() {
			let detailsUrl = $(this).attr("data-href");
			
			$.ajax({
				type: 'GET',
				url: detailsUrl,
				beforeSend: function() {
					swalWithBootstrapButtons.fire({
						title: 'Pobieranie danych, proszę czekać...',
                  		  confirmButtonText: 'Pobieranie danych',
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
					
					console.log(response);
					
					let swalContent = $('<div>')
	  	  			.append('<div class="swal2-content" style="display: block;">'
	  	  					+ '<h4>Adres:</h4>'
	  	  					+ response.city + ' ' + (response.district ? response.district : '') 
	  	  					+ '<br>'
	  	  					+ response.street + ' ' + response.houseNumber + (response.apartmentNumber ? '/' + response.apartmentNumber : '')
	  	  					+ '<br>'
	  	  					+ response.postCode + ' ' + response.postCity
	  	  					+ '<br><br>' 
	  	  					+ '<h4>Kontakt:</h4>'
	  	  					+ 'tel. ' + response.phoneNumber
	  	  					+ '<br><br>' 
	  	  					+ listCategories(response.categories)
	  	  					+'</div>')
	  	  			.append("<br>")
					
					detailsSwal.fire({
						html: swalContent,
						showConfirmButton: true
					});
				},
				error: function() {
	  				swalWithBootstrapButtons.fire(
	  		    			  	'Błąd',
	  		    			  	'Nie udało się pobrać danych',
	  		    			  	'error');
	  			}
			});
		});
	});
	
	
	$(".delete-button").each(function() {
		
		$(this).click(function() {
			let deleteUrl = $(this).attr("data-href");
			
			 swalWithBootstrapButtons.fire({
 				  title: 'Czy na pewno chcesz usunąć tego użytkownika?',
 				  icon: 'question',
 				  text: 'Akcja ta jest nieodwracalna.',
 				  confirmButtonText: 'Usuń',
 				  showCancelButton: true,
 				  showCloseButton: true,
 				  reverseButtons: true,
 				  cancelButtonText: 'Anuluj'
 			  }).then((result) => {
 				  if(result.value) {
 					  
 					  let token = $("meta[name='_csrf']").attr("content");
 					  let header = $("meta[name='_csrf_header']").attr("content");
 					  
 					  $.ajax({
 						 type: 'DELETE',
 						 url: deleteUrl,
 						 headers: {
 							 [header]: token
 						 },
 						 beforeSend: function() {
 							swalWithBootstrapButtons.fire({
		          				  title: 'Usuwanie, proszę czekać...',
		                  		  confirmButtonText: 'Usuwanie',
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
 									text: 'Użytkownik został usunięty. W ciągu 5 sekund nastapi odświeżenie strony.',
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