$(document).ready(function() {
	$(".nav li").removeClass("active");
    $('a[href="' + this.location.pathname + '"]').parent().addClass('active');
});