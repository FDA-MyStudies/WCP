<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 p-none mt-md mb-md">
         <!-- widgets section-->
         <div class="col-sm-12 col-md-12 col-lg-12 p-none">
            <div class="black-lg-f">
              <span class="mr-xs"><a href="javascript:void(0)" class="backOrCancelBtnOfNotification">
              <img src="/fdahpStudyDesigner/images/icons/back-b.png"/></a></span> 
              <c:if test="${notificationBO.actionPage eq 'addOrCopy' || notificationBO eq null}">Create Notification</c:if>
              <c:if test="${notificationBO.actionPage eq 'edit'}">Edit Notification</c:if>
              <c:if test="${notificationBO.actionPage eq 'view'}">View Notification</c:if>
              <c:if test="${notificationBO.actionPage eq 'resend'}">Resend Notification</c:if>
            </div>
         </div>         
</div> 
<form:form action="/fdahpStudyDesigner/adminNotificationEdit/saveOrUpdateNotification.do?${_csrf.parameterName}=${_csrf.token}" 
     data-toggle="validator" role="form" id="appNotificationFormId"  method="post" autocomplete="off">       
     <input type="hidden" name="buttonType" id="buttonType">
     <!-- <input type="hidden" name="currentDateTime" id="currentDateTime"> -->
     <input type="hidden" name="notificationId" value="${notificationBO.notificationId}">
 
	<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 p-none">
	    <div class="col-md-12 p-none">
	        <div class="box-space white-bg">
	            
	            <!-- form- input-->
	            <div class="pl-none">
	                <div class="gray-xs-f mb-xs">Notification Text (250 characters max) <span class="requiredStar">*</span></div>
	                 <div class="form-group">
	                    <textarea class="form-control" maxlength="250" rows="5" id="notificationText" name="notificationText" required
	                    >${notificationBO.notificationText}</textarea>
	                    <div class="help-block with-errors red-txt"></div>
	                </div>
	            </div>
	            
	            <div class="mt-xlg mb-lg">
	             <div class="form-group">
		                <span class="radio radio-info radio-inline p-45">
		                    <input type="radio" id="inlineRadio1" value="notNowDateTime" name="currentDateTime" 
		                    <c:if test="${notificationBO.notificationScheduleType eq 'notNowDateTime'}">checked</c:if>
		                    <c:if test="${notificationBO.actionPage eq 'addOrCopy'}">checked</c:if>>
		                    <label for="inlineRadio1">Schedule a date/time</label>
		                </span>
		                <span class="radio radio-inline">
		                    <input type="radio" id="inlineRadio2" value="nowDateTime" name="currentDateTime"
		                    <c:if test="${notificationBO.notificationScheduleType eq 'nowDateTime'}">checked</c:if>>
		                    <label for="inlineRadio2">Send it Now</label>
		                </span>
	                	<div class="help-block with-errors red-txt"></div>
	                	<c:if test="${notificationBO.notificationSentDateTime ne null}">
	                		<div class="lastSendDateTime">Last Sent on ${notificationBO.notificationSentDate} at ${notificationBO.notificationSentTime}</div>
	                	</c:if>
	                	<div class="clearfix"></div>
	                </div>
	            </div>
	            
	            
	            <div class="add_notify_option mandatoryForAppNotification">
	                <div class="gray-xs-f mb-xs">Select Date <span class="requiredStar">*</span></div>
	                 <div class="form-group date">
	                     <input id='datetimepicker' type="text" class="form-control calendar datepicker resetVal" 
	                     name="scheduleDate" value="${notificationBO.scheduleDate}" oldValue="${notificationBO.scheduleDate}" 
	                     placeholder="MM/DD/YYYY"  disabled/>                    
	                     <div class="help-block with-errors red-txt"></div>
	                </div>
	            </div>
	           
	            <div class="add_notify_option mandatoryForAppNotification">
	                <div class="gray-xs-f mb-xs">Time <c:if test="${notificationBO.actionPage ne 'view'}">
	                	</c:if><span class="requiredStar">*</span></div>
	                 <div class="form-group">
	                     <input id="timepicker1" class="form-control clock timepicker resetVal" name="scheduleTime" 
	                     value="${notificationBO.scheduleTime}" oldValue="${notificationBO.scheduleTime}" data-provide="timepicker" 
	                     data-minute-step="5" data-modal-backdrop="true" type="text" data-format="h:mm a" placeholder="00:00"  disabled/>
	                     <div class="help-block with-errors red-txt"></div>
	                </div>
	            </div>
	            
	            
	        </div>
	    </div>
	</div>
	<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 p-none">
	   <div class="white-bg box-space t-bor text-right">
	       <div class="dis-line text-right ml-md">
	       
	         <div class="dis-line form-group mb-none mr-sm">
	             <button type="button" class="btn btn-default gray-btn backOrCancelBtnOfNotification">Cancel</button>
	         </div>
	         <c:if test="${empty notificationBO || notificationBO.actionPage eq 'addOrCopy'}">  
		         <div class="dis-line form-group mb-none mr-sm">
		             <button type="button" class="btn btn-primary blue-btn addNotification">Save</button>
		         </div>
	         </c:if>
	          <c:if test="${not empty notificationBO && not notificationBO.notificationSent && notificationBO.actionPage eq 'edit' && notificationBO.notificationSentDateTime eq null}">  
		         <div class="dis-line form-group mb-none mr-sm">
		         	 <button type="button" class="btn btn-primary blue-btn" id="deleteNotification">Delete</button>
		         </div>
	         </c:if>
	         <c:if test="${not empty notificationBO && not notificationBO.notificationSent && notificationBO.actionPage eq 'edit'}">  
		         <div class="dis-line form-group mb-none mr-sm">
		             <button type="submit" class="btn btn-primary blue-btn updateNotification">Update</button>
		         </div>
	         </c:if>
	         <c:if test="${not empty notificationBO && notificationBO.notificationSent && notificationBO.actionPage eq 'resend'}">  
		         <div class="dis-line form-group mb-none mr-sm">
		             <button type="button" class="btn btn-primary blue-btn resendNotification">Resend</button>
		         </div>
	         </c:if>
	      </div>       
	    </div>
	</div>  
</form:form>    
<form:form action="/fdahpStudyDesigner/adminNotificationView/viewNotificationList.do" id="notificationBackOrCancelBtnForm" name="notificationBackOrCancelBtnForm" method="post">
</form:form>
<form:form action="/fdahpStudyDesigner/adminNotificationEdit/deleteNotification.do" id="deleteNotificationForm" name="deleteNotificationForm" method="post">
	<input type="hidden" name="notificationId" value="${notificationBO.notificationId}">
</form:form>
<script>  
$(document).ready(function(){
	$('#rowId').parent().removeClass('white-bg');
	$("#notification").addClass("active");
	
	<c:if test="${not notificationBO.notificationSent && notificationBO.actionPage ne 'view'}">
		if($('#inlineRadio1').prop('checked')){
			$('#datetimepicker, #timepicker1').prop('disabled', false);
			$('#datetimepicker, #timepicker1').attr('required', 'required');
		}
		if($('#inlineRadio2').prop('checked')){
			$('.add_notify_option').addClass('dis-none');
		}
	</c:if>
	
	<c:if test="${notificationBO.notificationSent || notificationBO.actionPage eq 'view'}">
	    $('#appNotificationFormId input,textarea').prop('disabled', true);
	    if($('#inlineRadio2').prop('checked')){
			$('.add_notify_option').addClass('dis-none');
		}
	</c:if>
	
	<c:if test="${not notificationBO.notificationSent && notificationBO.actionPage eq 'resend'}">
    	$('#appNotificationFormId input,textarea').prop('disabled', true);
	</c:if>
	
	<c:if test="${notificationBO.actionPage eq 'addOrCopy'}">
			$('#inlineRadio1').prop('checked','checked');
	</c:if>
	
	<c:if test="${notificationBO.actionPage eq 'edit' && notificationBO.notificationSentDateTime ne null}">
		$('#appNotificationFormId textarea').prop('disabled', true);
	</c:if>
	
	<c:if test="${notificationBO.notificationSent && notificationBO.actionPage eq 'resend'}">
		$('#appNotificationFormId #inlineRadio1,#inlineRadio2').prop('disabled', false);
		$('#appNotificationFormId input,textarea').prop('disabled', false);
		$('#appNotificationFormId textarea').prop('readonly', true);
		if($('#inlineRadio1').prop('checked')){
			$('#datetimepicker, #timepicker1').attr('required', 'required');
		}
		if($('#inlineRadio2').prop('checked')){
			$('.add_notify_option').addClass('dis-none');
			$('#datetimepicker, #timepicker1').removeAttr('required');
		}
		$('#buttonType').val('resend');
	</c:if>
 
	$('#inlineRadio2').on('click',function(){
		 $('#datetimepicker, #timepicker1').removeAttr('required');
		 $("#datetimepicker, #timepicker1").parent().removeClass('has-error has-danger');
		 $("#datetimepicker, #timepicker1").parent().find(".help-block").text("");
		 $('.add_notify_option').addClass('dis-none');
		 resetValidation('.mandatoryForAppNotification');
		 $('.addNotification').prop('disabled',false);
	 });
	 
	 $('#inlineRadio1').on('click',function(){
		 $('#datetimepicker, #timepicker1').val('');
		 $('#datetimepicker, #timepicker1').prop('disabled', false);
		 $('.add_notify_option').removeClass('dis-none');
		 $('#datetimepicker, #timepicker1').attr('required', 'required');
		 $('#appNotificationFormId').find('.resetVal').each(function() {
					$(this).val($(this).attr('oldValue'));
		 });
		 resetValidation('.mandatoryForAppNotification');
	 });
	
	
	$('.backOrCancelBtnOfNotification').on('click',function(){
		$('.backOrCancelBtnOfNotification').prop('disabled', true);
		$('#notificationBackOrCancelBtnForm').submit();
	});
	
	$('.addNotification').on('click',function(){
		$('#buttonType').val('add');
		if(isFromValid('#appNotificationFormId')){
			$('.addNotification').prop('disabled',true);
			$('#appNotificationFormId').submit();
      	}else{
      		$('.addNotification').prop('disabled',false);
        }
		//$('.addNotification').prop('disabled', true);
		//$('#appNotificationFormId').submit();
	});
	
	$('.updateNotification').on('click',function(){
		$('#buttonType').val('update');
		if(isFromValid('#appNotificationFormId')){
			$('.updateNotification').prop('disabled',true);
			$('#appNotificationFormId').submit();
      	}else{
      		$('.updateNotification').prop('disabled',false);
        }
		//$('#appNotificationFormId').submit();
	});
	
	$('.resendNotification').on('click',function(){
		$('#buttonType').val('resend');
		if(isFromValid('#appNotificationFormId')){
			 if($('#inlineRadio2').prop('checked')){
	  			  bootbox.confirm("Are you sure you want to resend this notification now?", function(result){ 
	          	  		if(result){
	          	  		$('.updateNotification').prop('disabled',true);
	        			$('#appNotificationFormId').submit();
	          	  		}
	          	  	  });
					}
	  		  if($('#inlineRadio1').prop('checked')){
	  			$('.updateNotification').prop('disabled',true);
				$('#appNotificationFormId').submit();
	  		  }
      	}else{
      		$('.updateNotification').prop('disabled',false);
        }
	});
	
	$('#deleteNotification').on('click',function(){
  	  	bootbox.confirm("Are you sure want to delete notification!", function(result){ 
  		if(result){
  	    		$('#deleteNotificationForm').submit();
  		}
  	  });
  	});
	
	$('.datepicker').datetimepicker({
        format: 'MM/DD/YYYY',
//          minDate: new Date(),
        ignoreReadonly: true,
        useCurrent :false
    }); 
	
	 $(".datepicker").on("click", function (e) {
         $('.datepicker').data("DateTimePicker").minDate(new Date(new Date().getFullYear(),new Date().getMonth(), new Date().getDate()));
     });
	 
	 /* $('.deleteNotification').on('click',function(){
	  	    var notificationIdForDelete = $(this).attr('notificationIdForDelete');
	  	  	//var scheduledDate = $(this).attr('scheduledDate');
	  	  	//var scheduledTime = $(this).attr('scheduledTime');
	  	  	bootbox.confirm("Are you sure want to delete notification!", function(result){ 
	  		if(result){
	  	    	if(notificationIdForDelete != '' && notificationIdForDelete != null && typeof notificationIdForDelete != 'undefined'){
			  		$.ajax({
			  			url : "/fdahpStudyDesigner/adminNotificationEdit/deleteNotification.do",
			  			type : "POST",
			  			datatype: "json",
			  			data : {
			  				notificationIdForDelete : notificationIdForDelete,
			  				//scheduledDate : scheduledDate,
			  				//scheduledTime : scheduledTime,
			  		  		"${_csrf.parameterName}":"${_csrf.token}"
			  			},
			  			success:function(data){
			  			var jsonObj = eval(data);
								var message = jsonObj.message;
								if(message == 'SUCCESS'){
									alert("Success");
								} else {
									alert("Failed");
								}
			  			},
			  		});
	  	    	}
	  		}
	  	  });
	  	}); */
});
</script>
