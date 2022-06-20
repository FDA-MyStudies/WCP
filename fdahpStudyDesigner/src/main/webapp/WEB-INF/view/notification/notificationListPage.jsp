<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<head>
  <meta charset="UTF-8">
  <style>
<!--
.sorting, .sorting_asc, .sorting_desc {
    background : none !important;
}
-->

#myModal .modal-dialog, #learnMyModal .modal-dialog .flr_modal{
      position:relative !important;
      right:-14px !important;
      margin-top:6% !important;
      }

      .flr_modal{
      float:right !important;
      }

      .blue_text{
      color:#007CBA !important;
      font-size:15px;
      font-weight:500;
      }

      .timerPos{
      position:relative;
      top:-2px;
      right:2px !important;
      }

      #timeOutMessage{
      width:257px;
      }
</style>
</head>
	
<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 p-none mb-md">
     <div>
         <!-- widgets section-->        
         <div class="col-sm-12 col-md-12 col-lg-12 p-none">
            <div class="black-lg-f">
                Manage Gateway Notifications
            </div>          
            <div class="dis-line pull-right ml-md">
             	<c:if test="${fn:contains(sessionObject.userPermissions,'ROLE_MANAGE_APP_WIDE_NOTIFICATION_EDIT')}">
	                 <div class="form-group mb-none mt-xs">
	                     <button type="button" class="btn btn-primary blue-btn notificationDetailsToEdit" actionType="add">
	                      Create Notification</button>
	                 </div>
                 </c:if>
             </div>
         </div>         
    </div>
    <div  class="clearfix"></div>
</div>

<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 p-none"> 
    <div class="white-bg">
        <div>
            <table id="app_Wide_Notification_list" class="table table-right tbl">
            <thead>
              <tr>
                <th>TITLE</th>
                <th class="linkDis">Status</th>
                <th class="linkDis">ACTIONS</th>  
              </tr>
            </thead>
            <tbody>
            <c:forEach items="${notificationList}" var="notification" varStatus="status">
              <tr>
                <td><div class="dis-ellipsis lg-ellipsis" title="${fn:escapeXml(notification.notificationText)}">${fn:escapeXml(notification.notificationText)}</div></td>                
                <td>${notification.checkNotificationSendingStatus}</td>
                <td>
                	<span class="sprites_icon preview-g mr-lg notificationDetailsToView" actionType="view" notificationId="${notification.notificationId}" data-toggle="tooltip" data-placement="top" title="View"></span>
                    <c:if test="${fn:contains(sessionObject.userPermissions,'ROLE_MANAGE_APP_WIDE_NOTIFICATION_EDIT')}">
                    	<c:if test="${notification.notificationSent}">
	                    	<span class="sprites-icons-2 send mr-lg notificationDetailsToEdit" actionType="resend" notificationId="${notification.notificationId}" data-toggle="tooltip" data-placement="top" title="Resend"></span>
	                    </c:if>
	                    <c:if test="${not notification.notificationSent}">
	                    	<span class="sprites_icon edit-g mr-lg notificationDetailsToEdit" actionType="edit" notificationId="${notification.notificationId}" data-toggle="tooltip" data-placement="top" title="Edit"></span>
	                    </c:if>
	                    <span class="sprites_icon copy notificationDetailsToEdit" actionType="add" notificationText="${fn:escapeXml(notification.notificationText)}" data-toggle="tooltip" data-placement="top" title="Copy"></span>                    
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

<form:form
             action="/fdahpStudyDesigner/sessionOut.do"
              id="backToLoginPage"
              name="backToLoginPage"
              method="post">
</form:form>

<div class="modal fade" id="myModal" role="dialog">
        <div class="modal-dialog modal-sm flr_modal">
            <!-- Modal content-->
            <div class="modal-content">
                    <div class="modal-body">
                    <div id="timeOutMessage" class="text-right blue_text"><span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in  15 minutes</div>
                    </div>
                </div>
            </div>
        </div>
    </div>

<script>
   var idleTime = 0;
	//notificationTable();
	$(document).ready(function(){
		$('#rowId').parent().removeClass('white-bg');
		
		$('#notification').addClass('active');
		
		$('[data-toggle="tooltip"]').tooltip();  
		 
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
		
		$('#app_Wide_Notification_list').DataTable( {
		    "paging":   true,
		    "order": [],
		    "columnDefs": [ { orderable: false, orderable: false, targets: [0] } ],
		    "info" : false, 
		    "lengthChange": false, 
		    "searching": false, 
		    "pageLength": 15,
		});

		setInterval(function () {
              idleTime += 1;
               if (idleTime > 3) { // 5 minutes
               timeOutFunction();
                }
                }, 75000);

                $(this).mousemove(function (e) {
                  idleTime = 0;
                });
                $(this).keypress(function (e) {
                 idleTime = 0;
                 });

                 function timeOutFunction() {
                 $('#myModal').modal('show');
                  let i = 14;
                  let timeOutInterval = setInterval(function () {
                  if (i === 0) {
                  $('#timeOutMessage').html('<span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in ' + i +' minutes');
                  if ($('#myModal').hasClass('in')) {
                  $('#backToLoginPage').submit();
                    }
                    clearInterval(timeOutInterval);
                     } else {
                     if (i === 14) {
                    $('#timeOutMessage').html('<span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in 14 minutes');
                      } else {
                      $('#timeOutMessage').html('<span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in ' + i +' minutes');
                        }
                        i-=1;
                         }
                       }, 15000);
                     }
});
</script>
