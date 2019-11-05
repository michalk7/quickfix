$(document).ready(function() {
		
	let newUrlEnd = "";
	
	let urlSearchParam = new URLSearchParams(window.location.href);
	if(urlSearchParam.has("categoriesGroup")) {
		newUrlEnd += "&categoriesGroup=" + urlSearchParam.get("categoriesGroup");
		
		$("option[value=" + urlSearchParam.get("categoriesGroup") + "]").prop("selected", true);
	}
	if(urlSearchParam.has("onlyMyEstate")) {
		newUrlEnd += "&onlyMyEstate=" + urlSearchParam.get("onlyMyEstate");
		
		$("#onlyMyEstate").prop('checked', true);
	}
	if(urlSearchParam.has("showAll")) {
		newUrlEnd += "&showAll=" + urlSearchParam.get("showAll");
		
		$("#showAll").prop('checked', true);
	}
	
	$('a[href^="/search"]').each(function() {
		const oldUrl = $(this).attr("href");
		
		let newUrl = oldUrl + newUrlEnd;
		$(this).attr("href", newUrl);
		
	});
});