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
			console.log(detailsUrl);
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
	})
	
});