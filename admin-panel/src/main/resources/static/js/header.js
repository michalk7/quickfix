$(document).ready(function() {
	$(".activeChanger").removeClass("active");
    $('a[href="' + this.location.pathname + '"]').addClass('active');
});