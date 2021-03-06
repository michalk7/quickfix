
/*$(document).ready(function() {
	$('.picker').datepicker({
        format: "yyyy-mm-dd",
        todayBtn: "linked",
        autoclose: true,
        todayHighlight: true,
        language: 'pl'
	});
});
*/
//document.addEventListener('DOMContentLoaded', function() {
//	$.getJSON('/calendar/api/getList', function(data) {
//		
//		console.log(data);
//		
//		var calendarEl = document.getElementById('calendar');
//
//	    var calendar = new FullCalendar.Calendar(calendarEl, {
//	      plugins: [ 'dayGrid', 'timeGrid', 'list' ],
//	      header: {
//	        left: 'prev,next today',
//	        center: 'title',
//	        right: 'dayGridMonth,timeGridWeek,timeGridDay,listWeek'
//	      },
//	      locale: 'pl',
//	      //defaultDate: '2019-11-07',
//	      navLinks: true, // can click day/week names to navigate views
//	      editable: false,
//	      eventLimit: true, // allow "more" link when too many events
//	      events: data,
//	      eventClick: function(info) {
//	    	  console.log(info.event);
//	    	  Swal.fire({
//	    		  title: info.event.extendedProps.problemTitle,
//	    		  text: info.event.extendedProps.problemDescription,
//	    		  icon: 'info',
//	    		  confirmButtonText: 'Ok'
//	    		});
//	      }
////	      },
////	      eventRender: function(info) {
////	    	  console.log(info.event);
////	    	  Swal.fire({
////	    		  title: info.event.extendedProps.problemTitle,
////	    		  text: info.event.extendedProps.problemDescription,
////	    		  icon: 'info',
////	    		  confirmButtonText: 'Ok'
////	    		});
////	      }
//	    });
//
//	    calendar.render();
//		
//	});
//    
//});

//var calendar;

document.addEventListener('DOMContentLoaded', function() {
	var calendarEl = document.getElementById('calendar');
	
	if(window.innerWidth < 1256) {
		$("#legend").removeClass("w-25");
	}
	
	function mobileCheck() {
        if (window.innerWidth >= 768 ) {
            return false;
        } else {
            return true;
        }
    };

    var calendar = new FullCalendar.Calendar(calendarEl, {
      plugins: [ 'dayGrid', 'timeGrid', 'list' ],
      defaultView: mobileCheck() ? 'timeGridDay' : 'timeGridWeek',
      header: {
        left: 'prev,next today',
        center: 'title',
        right: ''
      },
      footer: {
    	  left: 'dayGridMonth,timeGridWeek,timeGridDay,listWeek',
    	  center: '',
    	  right: ''
      },
      windowResize: function(view) {
          if (window.innerWidth >= 768 ) {
              calendar.changeView('timeGridWeek');
          } else {
              calendar.changeView('timeGridDay');
          }
      },
      locale: 'pl',
      allDaySlot: false,
      navLinks: true, // can click day/week names to navigate views
      editable: false,
      eventLimit: true, // allow "more" link when too many events
      events: {
          url: '/calendar/api/getList',
          failure: function() {
            document.getElementById('script-warning').style.display = 'block'
          }
        },
        loading: function(bool) {
          document.getElementById('loading').style.display =
            bool ? 'block' : 'none';
      },
      eventClick: function(info) {
    	  const detailsSwal = Swal.mixin({
    		  title: info.event.extendedProps.problemTitle,
    		  icon: 'info',
    		  showConfirmButton: false,
    		  showCloseButton: true,
    	  });
    	  
    	  
    	  
    	  if($('#notConfirmedCount').length != 0) {
    		  
    		  
    		  if(info.event.start < moment() || info.event.extendedProps.confirmed) {
    			  detailsSwal.fire({
        			  html: info.event.extendedProps.problemDescription + '<br><br>' 
        			  		+ '<h4>Kontakt:</h4>' + info.event.extendedProps.firstName + ' ' + info.event.extendedProps.lastName
        			  		+ '<br>' + 'tel. ' + info.event.extendedProps.phoneNumber,
        			  showConfirmButton: true
        		  });
    		  } else {
    			  
    			  let buttons = $('<div>')
	  	  			.append('<div class="swal2-content" style="display: block;">'
	  	  					+ info.event.extendedProps.problemDescription + '<br><br>' 
	  	  					+ '<h4>Kontakt:</h4>' + info.event.extendedProps.firstName + ' ' + info.event.extendedProps.lastName
	  	  					+ '<br>' + 'tel. ' + info.event.extendedProps.phoneNumber
	  	  					+'</div>')
	  	  			.append("<br>")
	  	  			.append('<button class="btn btn-danger mr-4" id="rejectButton">Odrzu??</button>')
	  	  			.append('<button class="btn btn-success" id="confirmButton">Potwierd??</button>');
	  	  
	  			  detailsSwal.fire({
	  				  html: buttons,
	  				  onOpen: function(dObj) {
	  					  
	  					  const swalWithBootstrapButtons = Swal.mixin({
	  			    		  customClass: {
	  			    		    confirmButton: 'btn btn-success',
	  			    		    cancelButton: 'btn btn-danger mr-4'
	  			    		  },
	  			    		  buttonsStyling: false
	  			    		});
	  					  
	  					  $('#confirmButton').on('click', function() {
	  						  detailsSwal.close(); 
	  						  
	  						  
	  						  let dataToSend = {
	  							  confirmed: true
		  		  			  };
		  		  			  
		  		  			  let token = $("meta[name='_csrf']").attr("content");
		  		  			  let header = $("meta[name='_csrf_header']").attr("content");
		  		  			  
		  		  			  $.ajax({
		  		  				  type: 'PUT',
		  		  				  url: '/calendar/api/setConfirmation/' + info.event.id + '/',
		  		  				  headers: {
		  		  					  [header]: token
		  		  				  },
		  		  				  data: JSON.stringify(dataToSend),
		  		  				  beforeSend: function() {
		  		  					  swalWithBootstrapButtons.fire({
		  		          				  title: 'Zapisywanie, prosz?? czeka??...',
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
		  		  						  
		  		  						  refreshCalendarData();
			  		  					  
			  		  					  swalWithBootstrapButtons.fire({
			  		  	    				  title: 'Gotowe!',
			  		  	    				  icon: 'success',
			  		  	    				  text: 'Wydarzenie zosta??o potwierdzone.',
			  		  	    				  confirmButtonText: 'OK'
			  		  	    			  });
		  		  						  
		  		  					  } else {
		  		  						  swalWithBootstrapButtons.fire({
		  		  							  title: 'B????d',
		  		  							  html: 'Zmiany nie zosta??y zapisane<br><br>' + response.message,
		  		  							  icon: 'error'
		  		  						  });
		  		  					  }
		  		  					  
		  		  				  },
		  		  				  contentType: "application/json; charset=utf-8",
		  		  				  error: function() {
		  		  					  swalWithBootstrapButtons.fire(
		  		  		    			  		'B????d',
		  		  		    			  		'Zmiany nie zosta??y zapisane',
		  		  		    			  		'error');
		  		  				  },
		  		  				  dataType: "json"
		  		  			  });
	  						  
	  					  });
	  					  
	  					  $('#rejectButton').click(function() {
	  						  
	  						  //tu da?? usuwanie
	  						  
	  						  detailsSwal.close();
	  						  
	  						  swalWithBootstrapButtons.fire({
		  	    				  title: 'Czy na pewno chcesz odwo??a?? to wydarzenie?',
		  	    				  icon: 'question',
		  	    				  text: 'Po odwo??aniu wydarzenie zostanie usuni??te.',
		  	    				  confirmButtonText: 'Odwo??aj',
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
				  		  				  url: '/calendar/api/' + info.event.id + '/',
				  		  				  headers: {
				  		  					  [header]: token
				  		  				  },
				  		  				  beforeSend: function() {
				  		  					  swalWithBootstrapButtons.fire({
				  		          				  title: 'Usuwanie, prosz?? czeka??...',
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
				  		  						  refreshCalendarData();
					  		  					  
					  		  					  swalWithBootstrapButtons.fire({
					  		  	    				  title: 'Gotowe!',
					  		  	    				  icon: 'success',
					  		  	    				  text: 'Wydarzenie zosta??o usuni??te.',
					  		  	    				  confirmButtonText: 'OK'
					  		  	    			  });
				  		  					  } else {
				  		  						  swalWithBootstrapButtons.fire({
				  		  							  title: 'B????d',
				  		  							  html: 'Zmiany nie zosta??y zapisane<br><br>' + response.message,
				  		  							  icon: 'error'
				  		  						  });
				  		  					  }
				  		  					  
				  		  					  
				  		  				  },
				  		  				  contentType: "application/json; charset=utf-8",
				  		  				  error: function() {
				  		  					  swalWithBootstrapButtons.fire(
				  		  		    			  		'B????d',
				  		  		    			  		'Zmiany nie zosta??y wprowadzone',
				  		  		    			  		'error');
				  		  				  },
				  		  				  dataType: "json"
				  		  			  });
		  	    					  
		  	    				  }
		  	    			  });
	  						  
	  						  
	  						  
	  						  
	  					  });
	  					  
	  					  
	  				  }
	  				});
    			  
    		  } //koniec ifa na daty
    		  
    		  
    		  
    	  } else { //if jeste??my userem
    		  
    		  detailsSwal.fire({
    			  html: info.event.extendedProps.problemDescription + '<br><br>' 
			  			+ '<h4>Kontakt:</h4>' + info.event.extendedProps.firstName + ' ' + info.event.extendedProps.lastName
			  			+ '<br>' + 'tel. ' +  info.event.extendedProps.phoneNumber,
    			  showConfirmButton: true
    		  });
    		  
    	  }
    	  
    	  
      }
    });
    
    $('.picker').datepicker({
        format: "yyyy-mm-dd",
        todayBtn: "linked",
        autoclose: true,
        todayHighlight: true,
        language: 'pl'
	}).on('changeDate', function(selected) {
		calendar.gotoDate(selected.date);
		calendar.changeView('timeGridDay');
	});
    
    function refreshCalendarData() {
    	calendar.refetchEvents();
    	if($('#notConfirmedCount').length != 0) {
    		$.getJSON('/calendar/api/getNotConfirmedCount', function(json) {
        		if(!json.error) {
        			$("#notConfirmedCount").text("Liczba niepotwierdzonych zdarze??: " + json.count);
        		}
        			
        	});
    	}
    }

    setInterval(refreshCalendarData, 60000);
    
    calendar.render();
});