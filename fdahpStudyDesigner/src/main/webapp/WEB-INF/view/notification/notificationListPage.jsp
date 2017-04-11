<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

	
<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 p-none mb-md">
     <div>
         <!-- widgets section-->        
         <div class="col-sm-12 col-md-12 col-lg-12 p-none">
            <div class="black-lg-f">
                Manage Notifications
            </div>          
            <div class="dis-line pull-right ml-md">
             	<c:if test="${fn:contains(sessionObject.userPermissions,'ROLE_MANAGE_APP_WIDE_NOTIFICATION_EDIT')}">
	                 <div class="form-group mb-none mt-xs">
	                     <button type="button" class="btn btn-primary blue-btn notificationDetailsToEdit" actionType="add">
	                     <span class="mr-xs">+</span> Create Notification</button>
	                 </div>
                 </c:if>
             </div>
            
         </div>         
    </div>
    <div  class="clearfix"></div>
    <%-- <div id="displayMessage">
	    <div id="errMsg" class="text-center e-box p-none">${errMsg}</div>
	    <div id="sucMsg" class="text-center s-box p-none">${sucMsg}</div>
	</div> --%>
</div>

<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 p-none"> 
    <div class="white-bg">
        <div class="table-responsive">
            <table id="app_Wide_Notification_list" class="table table-right">
            <thead>
              <tr>
                <th>TITLE</th>
                <%-- <c:if test="${fn:contains(sessionObject.userPermissions,'ROLE_MANAGE_APP_WIDE_NOTIFICATION_EDIT')}"> --%>
                	<th>ACTIONS</th>  
                <%-- </c:if>  --%>             
              </tr>
            </thead>
            <tbody>
            <c:forEach items="${notificationList}" var="notification" varStatus="status">
              <tr>
                <td><div class="dis-ellipsis" title="${fn:escapeXml(notification.notificationText)}">${fn:escapeXml(notification.notificationText)}</div></td>                
                <td>
                    <span class="sprites_icon preview-g mr-lg notificationDetailsToView" actionType="view" notificationId="${notification.notificationId}"></span>
                    <c:if test="${fn:contains(sessionObject.userPermissions,'ROLE_MANAGE_APP_WIDE_NOTIFICATION_EDIT')}">
	                    <span class="sprites-icons-2 send mr-lg notificationDetailsToEdit" actionType="resend" notificationId="${notification.notificationId}"></span>
	                    <span class="sprites_icon edit-g mr-lg notificationDetailsToEdit" actionType="edit" notificationId="${notification.notificationId}"></span>
	                    <span class="sprites_icon copy notificationDetailsToEdit" actionType="add" notificationText="${notification.notificationText}"></span>                    
                    </c:if>
                  </td>        
               </tr> 
              </c:forEach>
			  </tbody>
          </table>
        </div>
  </div>
</div>
<form:form action="/fdahpStudyDesigner/adminNotificationEdit/getNotificationToEdit.do" id="getNotificationEditPage" name="getNotificationEditPage" method="post">
		<input type="hidden" id="notificationId" name="notificationId">
		<input type="hidden" id="notificationText" name="notificationText">
		<input type="hidden" id="actionType" name="actionType">
		<input type="hidden" name="chkRefreshflag" value="y">
</form:form>
<form:form action="/fdahpStudyDesigner/adminNotificationView/getNotificationToView.do" id="getNotificationViewPage" name="getNotificationViewPage" method="post">
		<input type="hidden" id="notificationIdToView" name="notificationId">
		<input type="hidden" id="actionTypeToView" name="actionType">
		<input type="hidden" name="chkRefreshflag" value="y">
</form:form>

<script>
	//notificationTable();
	$(document).ready(function(){
		$('#rowId').parent().removeClass('white-bg');
		
		$('#notification').addClass('active');
		/* $("#notification").addClass("active"); */
		
		/* var sucMsg = '${sucMsg}';
		var errMsg = '${errMsg}';
		if(sucMsg.length > 0){
			$("#sucMsg .msg").html(sucMsg);
	    	$("#sucMsg").show("fast");
	    	$("#errMsg").hide("fast");
	    	setTimeout(hideDisplayMessage, 4000);
		}
		if(errMsg.length > 0){
			$("#errMsg .msg").html(errMsg);
		   	$("#errMsg").show("fast");
		   	$("#sucMsg").hide("fast");
		   	setTimeout(hideDisplayMessage, 4000);
		}
		
		 $('#displayMessage').click(function(){
			$('#displayMessage').hide();
		});
		  */
		 
		$('.notificationDetailsToEdit').on('click',function(){
			$('.notificationDetailsToEdit').prop('disabled', true);
			$('#notificationId').val($(this).attr('notificationId'));
			$('#notificationText').val($(this).attr('notificationText'));
			$('#actionType').val($(this).attr('actionType'));
			$('#getNotificationEditPage').submit();
		});
		
		$('.notificationDetailsToView').on('click',function(){
			$('.notificationDetailsToView').prop('disabled', true);
			$('#notificationIdToView').val($(this).attr('notificationId'));
			$('#actionTypeToView').val($(this).attr('actionType'));
			$('#getNotificationViewPage').submit();
		});
		
		$('.resendNotification').on('click',function(){
	  	    var notificationIdToResend = $(this).attr('notificationIdToResend');
	  	    alert(notificationIdToResend);
	  	   /*  bootbox.confirm("Are you sure you want to delete this Notification?", function(result){
	  	    	alert("alert bootstrap"); */
		  	    /* if (result) { */
			  		$.ajax({
			  			url : "/fdahpStudyDesigner/adminNotificationEdit/resendNotification.do",
			  			type : "POST",
			  			datatype: "json",
			  			data : {
			  				notificationIdToResend : notificationIdToResend,
			  		  		"${_csrf.parameterName}":"${_csrf.token}"
			  			},
			  			success:function(data){
			  			var jsonObj = eval(data);
								var message = jsonObj.message;
								if(message == 'SUCCESS'){
									alert("Success");
									//$('#displayMessage').removeClass('aq-danger').addClass('aq-success');
									//$("#sucMsg .msg").html('Deleted successfully.');
									//$("#sucMsg").show();
									//$("#errMsg").hide();
								}  else {
									alert("Failed");
									/* $('#displayMessage').removeClass('aq-success').addClass('aq-danger');
									$("#errMsg .msg").html('Failed to delete. Please try again.');
									$("#errMsg").show();
									$("#sucMsg").hide(); */
								}
								/* setTimeout(hideDisplayMessage, 4000); */
			  			},
			  		});
		  	  	//}
	  	  	//});
	  	});
		/* $('#app_Wide_Notification_list').DataTable( {
		    "paging":   true,
		    "abColumns": [
		       { "bSortable": true }
		      ],  
		    "info" : false, 
		    "lengthChange": false, 
		    "searching": false, 
		    "pageLength": 15,
		}); */
		
		$('#app_Wide_Notification_list').DataTable( {
		    "paging":   true,
		    "order": [],
		    "columnDefs": [ { orderable: false, targets: [0] } ],
		    "info" : false, 
		    "lengthChange": false, 
		    "searching": false, 
		    "pageLength": 15,
		});
		
});
	
	/* function hideDisplayMessage(){
		$('#sucMsg').hide();
		$('#errMsg').hide();
	} */
</script>
