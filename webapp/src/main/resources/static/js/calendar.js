
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
    	  console.log(info.event);
    	  Swal.fire({
    		  title: info.event.extendedProps.problemTitle,
    		  text: info.event.extendedProps.problemDescription,
    		  icon: 'info',
    		  confirmButtonText: 'Ok'
    		});
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

    calendar.render();
});