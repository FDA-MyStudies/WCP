/**
 * @author Vivek
 * 
 * @see Intercepts the ajax request.
 */
function ajaxSessionTimeout() {
	window.location.href = '/acuityLink/login.do?error=timeOut';
}

!function($) {
	$.ajaxSetup({
		statusCode : {
			901 : ajaxSessionTimeout
		}
	});
}(window.jQuery);