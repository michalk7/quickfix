/**
 * 
 */

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
      plugins: [ 'dayGrid', 'timeGrid', 'list', 'interaction' ],
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
      eventColor: '#d3d3d3',
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
      selectable: true,
      selectHelper: true,
      selectOverlap: false,
      selectMirror: true,
      select: function(arg) {
    	  
    	  if(calendar.view.type != 'timeGridWeek' && calendar.view.type != 'timeGridDay') {
              calendar.unselect();
              return;
          }
    	  
    	  const swalWithBootstrapButtons = Swal.mixin({
    		  customClass: {
    		    confirmButton: 'btn btn-success',
    		    cancelButton: 'btn btn-danger mr-4'
    		  },
    		  buttonsStyling: false
    		});
    	  
    	  if(arg.start < moment()) {
    		  swalWithBootstrapButtons.fire(
    			  		'Błąd',
    			  		'Nie można zarezerwować terminu przeszłego',
    			  		'error');
      		  calendar.unselect()
    	  } else if(arg.end.getDate() != arg.start.getDate()){
    		  swalWithBootstrapButtons.fire(
  			  		'Błąd',
  			  		'Nie można wybrać więcej niż 1 dnia',
  			  		'error');
    		  calendar.unselect()
    	  } else if(arg.allDay) {
    		  swalWithBootstrapButtons.fire(
    			  		'Błąd',
    			  		'Nie można wybrać całego dnia',
    			  		'error');
    		  calendar.unselect()
    	  } else {
        	  
        	  swalWithBootstrapButtons.mixin({
        		  confirmButtonText: 'Dalej &rarr;',
        		  cancelButtonText: 'Anuluj',
        		  showCancelButton: true,
        		  progressSteps: ['1', '2'],
        		  reverseButtons: true
        	  }).queue([
        		  {
        			  title: 'Nazwa problemu',
        			  input: 'text',
        			  text: 'Nadaj nazwę problemu/usługi np. cieknąca rura',
        			  inputValidator: (value) => {
        				  if (!value) {
        					  return 'Pole nie może być puste!'
        				  }
        			  }
        		  },
        		  {
        			  title: 'Opis problemu',
        			  input: 'textarea',
        			  text: 'Opisz co się stało lub czego potrzebujesz od fachowca',
        			  inputValidator: (value) => {
        				  if (!value) {
        					  return 'Pole nie może być puste!'
        				  }
        			  }
        		  }
        	  ]).then((result) => {
        		  
        		  if(result.value) {
        			  let eventToSend = {
        					  problemTitle: result.value[0],
        					  problemDescription: result.value[1],
        					  startDate: moment(arg.start).format(),
            				  endDate: moment(arg.end).format()
        			  };
        			  
        			  let token = $("meta[name='_csrf']").attr("content");
        			  let header = $("meta[name='_csrf_header']").attr("content");
        			  
        			  $.ajax({
        				  type: 'POST',
        				  url: '/calendar/api/saveEvent/' + $('#expertIdHidden').val() + '/',
        				  headers: {
        					  [header]: token
        				  },
        				  data: JSON.stringify(eventToSend),
        				  beforeSend: function() {
        					  swalWithBootstrapButtons.fire({
                				  title: 'Zapisywanie, proszę czekać...',
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
        					  
        					  console.log(response);
        					  
        					  if(!response.error) {
        						  
        						  calendar.addEvent({
            	    				  title: 'Niepotwierdzone',
            	    				  start: arg.start,
            	    				  end: arg.end,
            	    				  allDay: arg.allDay,
            	    				  color: '#ede007',
            	    				  textColor: 'black'
            	    			  });
            					  
            					  calendar.unselect();
            					  
            					  swalWithBootstrapButtons.fire({
            	    				  title: 'Gotowe!',
            	    				  icon: 'success',
            	    				  text: 'Wydarzenie zostało zapisane. W ciągu 5 sekund nastąpi przekierowanie do twojego kalendarza.',
            	    				  timer: 5000,
            	    				  allowOutsideClick: false,
                            		  allowEscapeKey: false
            	    			  }).then((resultT) => {
            	    				  window.location.replace(window.location.origin + '/calendar');
            	    			  });
        						  
        					  } else {
        						  swalWithBootstrapButtons.fire({
          		    			  		title: 'Błąd',
          		    			  		html: 'Zmiany nie zostały zapisane<br><br>' + response.message,
          		    			  		icon: 'error'
          		    			  	});
          					 
        						  calendar.unselect();
        					  }
        					  
        				  },
        				  contentType: "application/json; charset=utf-8",
        				  error: function() {
        					  swalWithBootstrapButtons.close();
        					  swalWithBootstrapButtons.fire(
        		    			  		'Błąd',
        		    			  		'Zmiany nie zostały zapisane',
        		    			  		'error');
        					 
        					  calendar.unselect();
        				  },
        				  dataType: "json"
        			  });
        		  } else if(result.dismiss === Swal.DismissReason.cancel) {
        			  swalWithBootstrapButtons.fire(
        			  		'Anulowano',
        			  		'Zmiany nie zostały zapisane',
        			  		'error');
        			  
        			  calendar.unselect();
        		  }
        	  });
    	  }
      },
      editable: false,
      eventLimit: true, // allow "more" link when too many events
      events: {
          url: '/calendar/api/getExpertCalendar/' + $('#expertIdHidden').val() + '/',
          failure: function() {
            document.getElementById('script-warning').style.display = 'block'
          }
        },
        loading: function(bool) {
          document.getElementById('loading').style.display =
            bool ? 'block' : 'none';
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
    }

    setInterval(refreshCalendarData, 60000);
    
    calendar.render();
});