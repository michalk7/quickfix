$(document).ready(function() {
	
	const swalWithBootstrapButtons = Swal.mixin({
		  customClass: {
		    confirmButton: 'btn btn-success',
		    cancelButton: 'btn btn-danger mr-4'
		  },
		  buttonsStyling: false
		});
	
	$(".delete-street-button").each(function() {
		
		$(this).click(function() {
			let deleteUrl = $(this).attr("data-href");
			
			 swalWithBootstrapButtons.fire({
 				  title: 'Czy na pewno chcesz usunąć tą ulice?',
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
 									text: 'Ulica została usunięta. W ciągu 5 sekund nastapi odświeżenie strony.',
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
	//delete button end
	
	
	
	
	
});