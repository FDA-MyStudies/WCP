<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@page import="com.fdahpStudyDesigner.util.SessionObject"%>
<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 p-none mb-md">
     
         <!-- widgets section-->
         <div class="col-sm-12 col-md-12 col-lg-12 p-none">
            <div class="black-lg-f">
              My Account
            </div>
           <!--  <div class="dis-line pull-right ml-md line34">
                <a href="javascript:formSubmit();" class="blue-link text-weight-normal text-uppercase">
                <span>Log Out</span>
                <span class="ml-xs"><img src="/fdahpStudyDesigner/images/icons/logout.png"/></span></a>  
           </div> -->
         </div>   
    
    <div  class="clearfix"></div>
    <%-- <div id="displayMessage">
	    <div id="errMsg" class="text-center e-box p-none">${errMsg}</div>
	    <div id="sucMsg" class="text-center s-box p-none">${sucMsg}</div>
	</div> --%>
</div>
   <form:form action="/fdahpStudyDesigner/adminDashboard/updateUserDetails.do?${_csrf.parameterName}=${_csrf.token}" id="userDetailsForm" 
         			name="userDetailsForm" role="form" autocomplete="off" data-toggle="validator" method="post">
<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 p-none mb-lg">
     <div class="white-bg box-space">
         <%-- <input type="hidden" name="userId" value="${userBO.userId}"> --%>
         <div class="b-bor">
              <div class="ed-user-layout row">               
                    <div class="col-md-4 p-none">
                       <div class="gray-xs-f line34">First Name <small>(50 characters max)</small><span class="requiredStar"> *</span></div>
                    </div>
                    <div class="col-md-6 p-none">
                        <div class="form-group cursAllow">
                            <input type="text" class="form-control edit-field bor-trans resetVal linkDis" name="firstName" value="${fn:escapeXml(userBO.firstName)}" oldVal="${fn:escapeXml(userBO.firstName)}" 
                            maxlength="50" required readonly/>
                        	<div class="help-block with-errors red-txt"></div>
                        </div>
                    </div>                
             </div>
        </div>
        
        <div class="b-bor mt-md">
              <div class="ed-user-layout row">               
                    <div class="col-md-4 p-none">
                       <div class="gray-xs-f line34">Last Name <small>(50 characters max)</small><span class="requiredStar"> *</span></div>
                    </div>
                    <div class="col-md-6 p-none">
                        <div class="form-group cursAllow">
                            <input type="text" class="form-control edit-field bor-trans resetVal linkDis" name="lastName" value="${fn:escapeXml(userBO.lastName)}" oldVal="${fn:escapeXml(userBO.lastName)}" 
                            maxlength="50" required readonly/>
                        	<div class="help-block with-errors red-txt"></div>
                        </div>
                    </div>                
             </div>
        </div>
         
         <div class="b-bor mt-md">
              <div class="ed-user-layout row">               
                 <div class="col-md-4 p-none">
                    <div class="gray-xs-f line34">Email Address <small>(100 characters max)</small><span class="requiredStar"> *</span></div>
                 </div>
                 <div class="col-md-6 p-none">
                     <div class="form-group cursAllow" id="removeText">
                         <input type="text" class="form-control edit-field bor-trans validateUserEmail resetVal linkDis" id="userEmail" name="userEmail" value="${userBO.userEmail}" 
                         					oldVal="${userBO.userEmail}" maxlength="100" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$" data-pattern-error="Email address is invalid" required readonly />
                     	<div class="help-block with-errors red-txt"></div>
                     </div>
                 </div>                
             </div>
        </div>
         
         <div class="b-bor mt-md">
              <div class="ed-user-layout row">               
                 <div class="col-md-4 p-none">
                    <div class="gray-xs-f line34">Phone Number <small>(10 characters max)</small><span class="requiredStar"> *</span></div>
                 </div>
                 <div class="col-md-6 p-none">
                     <div class="form-group cursAllow">
                         <input type="text" class="form-control edit-field bor-trans phoneMask resetVal linkDis" name="phoneNumber" value="${userBO.phoneNumber}" 
                         		oldVal="${userBO.phoneNumber}" maxlength="12" data-minlength="12" required readonly/>
                     	<div class="help-block with-errors red-txt"></div>
                     </div>
                 </div>                
             </div>
        </div>
         
         <div class="b-bor mt-md">
              <div class="ed-user-layout row">               
                    <div class="col-md-4 p-none">
                       <div class="gray-xs-f line34">Role</div>
                    </div>
                    <div class="col-md-6 p-none ">
                        <div class="form-group cur-not-allowed">
                            <input type="text" class="form-control edit-field bor-trans linkDis" name="roleName" value="${userBO.roleName}" maxlength="50" readonly/>
                        	<div class="help-block with-errors red-txt"></div>
                        </div>
                    </div>                
             </div>
        </div>
         
         
         
         <div id="hideProfileButton" class="mt-xlg">
              <div class="text-right"> 
                   <div class="dis-line form-group mb-none">
                        <button id="editable" type="button" class="btn btn-primary blue-btn">Edit</button>
                        <button id="ed-cancel" type="button" class="btn btn-default gray-btn dis-none">Cancel</button>
                        <button id="ed-update" type="submit" class="btn btn-primary blue-btn dis-none">Update</button>
                    </div>
             </div>
         </div>
    </div>
</div>
<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 p-none mb-md">
     <div class="white-bg box-space">
            <div class="row" id="hideChangePwd">
	            <div class="col-md-12 pl-none pr-none">              
	                    <div class="col-md-3 p-none">
	                       <div class="gray-xs-f line34">Password</div>
	                    </div>
	                    <div class="col-md-7 p-none">
	                     <span class="chngpassdot">........</span>
	                    </div>
	                    <div class="col-md-2 p-none dis-line form-group mb-none text-right">
	                     <button id="pwd-link" type="button" class="btn btn-default gray-btn cur-pointer disChangePassButton">Change Password</button>
	                      </div> 
	             </div> 
            </div> 
            <div class="row changepwd dis-none">
	             <div class="pl-none ">   
		              <div class="b-bor mt-md">
			              <div class="ed-user-layout row">            
			                <div class="col-md-6 p-none ">
			                    <div class="gray-xs-f line34">Old Password<span class="requiredStar"> *</span></div>
			                </div>
			                 <div class="col-md-6 p-none">
			                    <div class="form-group mb-none">
		                             <input type="password" class="input-field wow_input emptyField" maxlength="14" id="oldPassword" name="oldPassword" 
		                                  autocomplete="off" required tabindex="1"/>
		                             <div class="help-block with-errors red-txt"></div>
			                     </div>
			                 </div> 
			              </div>
		             </div> 
		             <div class="b-bor mt-md">
		              	<div class="ed-user-layout row">   
			                <div class="col-md-6 p-none">
			                   <div class="gray-xs-f line34">New Password<span class="requiredStar"> *</span></div>
			                </div>
			                <div class="col-md-6 p-none">
			                   <div class="form-group mb-none">
		                            <input type="password" class="input-field wow_input emptyField" id="password" maxlength="14"  data-minlength="8" 
		                            tabindex="2" name="password" data-error="Password is invalid"
		                             pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!&quot;#$%&amp;'()*+,-.:;&lt;=&gt;?@[\]^_`{|}~])[A-Za-z\d!&quot;#$%&amp;'()*+,-.:;&lt;=&gt;?@[\]^_`{|}~]{8,14}" autocomplete="off" required/>
		                            <div class="help-block with-errors red-txt"></div>
		                            <span class="arrowLeftSugg"></span>
			                    </div>
			                </div>
		                </div>
		              </div>
	                  <div class="b-bor mt-md">
		                  <div class="ed-user-layout row">   
		                      <div class="col-md-6 p-none">
		                       <div class="gray-xs-f line34">Confirm Password<span class="requiredStar"> *</span></div>
		                    </div>
		                    <div class="col-md-6 p-none">
		                       <div class="form-group mb-none">
	                                <input type="password" class="input-field wow_input emptyField" maxlength="14"  data-minlength="8" data-match-error="Whoops, these don't match" id="conpassword" data-match="#password" 
	                                     tabindex="3" autocomplete="off" required />
	                                <div class="help-block with-errors red-txt"></div>
	                                
		                        </div>
		                    </div> 
		                    </div>
	                    </div>
	                    <div class="text-right">  
		                     <div class="dis-line form-group mt-md mb-none">
	                             <button type="button" class="btn btn-default gray-btn mr-sm" id="cancelBtn" tabindex="4">Cancel</button>
	                             <button type="button" class="btn btn-primary blue-btn" id="updateBtn" tabindex="5">Update</button>
		                      </div>
	                    </div>
                   </div>                
             </div>
     </div>
</div> 
 </form:form>        
<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 p-none mb-md">
     <div class="white-bg box-space">
         
         <div class="ed-user-layout row"> 
             <div class="blue-md-f text-uppercase mb-md">Assigned Permissions</div>
             
             <!-- Assigned Permissions List-->
             <div class="edit-user-list-widget mb-xs">
                 <span>Manage Users</span>
                 <span class="gray-xs-f pull-right">
	                 <c:if test="${!fn:contains(sessionObject.userPermissions,'ROLE_MANAGE_USERS_EDIT')}">View Only</c:if>
	                 <c:if test="${fn:contains(sessionObject.userPermissions,'ROLE_MANAGE_USERS_EDIT')}">View & Edit</c:if>
                 </span>
             </div>
             
             <div class="edit-user-list-widget mb-xs">
                 <span>Manage App-Wide Notifications</span>
                 <span class="gray-xs-f pull-right">
	                 <c:if test="${!fn:contains(sessionObject.userPermissions,'ROLE_MANAGE_APP_WIDE_NOTIFICATION_EDIT')}">View Only</c:if>
	                 <c:if test="${fn:contains(sessionObject.userPermissions,'ROLE_MANAGE_APP_WIDE_NOTIFICATION_EDIT')}">View & Edit</c:if>
                 </span>
             </div>
             
             <!-- Assigned Permissions List-->
            <%--  <div class="edit-user-list-widget mb-xs">
                 <span>Manage Studies</span>
                 <span class="gray-xs-f pull-right">
	                 <c:if test="${!fn:contains(sessionObject.userPermissions,'ROLE_CREATE_MANAGE_STUDIES')}">View Only</c:if>
	                 <c:if test="${fn:contains(sessionObject.userPermissions,'ROLE_CREATE_MANAGE_STUDIES')}">View & Edit</c:if>
                 </span>
             </div> --%>
             
              <!-- Assigned Permissions List-->
             
             <!-- Assigned Permissions List-->
             <div class="edit-user-list-widget">
                 <span>Manage Studies</span>
                 <span class="gray-xs-f pull-right">
	                 <c:if test="${!fn:contains(sessionObject.userPermissions,'ROLE_MANAGE_STUDIES')}">No</c:if>
	                 <c:if test="${fn:contains(sessionObject.userPermissions,'ROLE_MANAGE_STUDIES')}">Yes</c:if>
                 </span>
                 <c:if test="${fn:contains(sessionObject.userPermissions,'ROLE_MANAGE_STUDIES')}">
	                 <div class="mt-lg pl-md">
	                 	<c:if test="${fn:contains(sessionObject.userPermissions,'ROLE_CREATE_MANAGE_STUDIES')}">
		                    <div class="pb-md bor-dashed">
		                        <span class="dot">Adding a New Study</span> 
		                    </div>
	                    </c:if>
	                    <div class="pl-sm pt-md">
	                        <span class="gray-xs-f text-weight-semibold text-uppercase">Existing Studies</span>
	                     </div>
	                     <c:forEach items="${studyAndPermissionList}" var="studyAndPermission">
		                     <div class="pt-sm pb-sm pl-sm b-bor-dark">
		                            <span class="dot" id="${studyAndPermission.customStudyId}">${studyAndPermission.name}</span>
		                            <span class="gray-xs-f pull-right">
			                            <c:if test="${not studyAndPermission.viewPermission}">View Only</c:if>
			                            <c:if test="${studyAndPermission.viewPermission}">View & Edit</c:if>
		                            </span>
		                     </div>
	                    </c:forEach>
	                 </div>
                 </c:if>
             </div>
         </div>
    </div>
</div>
<input type="hidden" id="csrfDet" csrfParamName="${_csrf.parameterName}" csrfToken="${_csrf.token}" />
<%-- <c:url value="/j_spring_security_logout" var="logoutUrl" />
<form action="${logoutUrl}" method="post" id="logoutForm">
	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
</form> --%>

<script>
	  $(document).ready(function(){ 
	  $('#rowId').parent().removeClass('white-bg');
		  addPasswordPopup();
		  $("#myAccount").addClass("active");
		  
		  $("form").submit(function() {
	    		$(this).submit(function() {
	       	 		return false;
	    		});
	    		 	return true;
			});
		 
		  var button = $('#ed-update');
		  $('input').each(function () {
		      $(this).data('val', $(this).val());
		  });
		  $('input').bind('keyup change blur', function(){
		      var changed = false;
		      $('input').each(function () {
		          if($(this).val() != $(this).data('val')){
		              changed = true;
		          }
		      });
		      button.prop('disabled', !changed);
		  });
		  
		  /* Profile buttons starts */
		  
		// Edit & Update button toggling
		
          $("#editable").click(function(){
            $(".edit-field").prop('readonly', false).removeClass("bor-trans");
            $("#ed-cancel,#ed-update").removeClass("dis-none");
            $("input[type='password']").prop("required",false);
            $('.cursAllow input').removeClass("linkDis");
            $("#editable").addClass("dis-none");
            /* $("#hideChangePwd").addClass("dis-none"); */
           /*  $("#pwd-link").addClass("linkDis").parent().addClass('cur-not-allowed'); */
            $(".disChangePassButton").prop('disabled', true);
            $('#ed-update').addClass('disabled');
           	$('#ed-update').addClass('disClick');
          });
          
          //Cancel editing
          $("#ed-cancel").click(function(){
        	    $('#userDetailsForm').find('.resetVal').each(function() {
					$(this).val($(this).attr('oldVal'));
			    });
        	    resetValidation('#userDetailsForm');
        	    $('#userEmail').parent().find(".help-block").empty();
        	    /* $("#userDetailsForm .form-group").removeClass("has-danger").removeClass("has-error");
                $("#userDetailsForm .help-block ul li").remove(); */
	            $(".edit-field").prop('readonly', true).addClass("bor-trans");
	            $("#ed-cancel,#ed-update").addClass("dis-none");
	            $('.cursAllow input').addClass("linkDis");
	            $("#editable").removeClass("dis-none");
	            $(".disChangePassButton").prop('disabled', false);
	           /*  $("#hideChangePwd").removeClass("dis-none"); */
	            /* $("#pwd-link").removeClass("linkDis").parent().removeClass('cur-not-allowed'); */
          });
          
          
          
          /* Profile buttons ends */
          
          $("#pwd-link").click(function(){
        	  $("input[type='password']").prop("required",true);
        	  $("#editable").prop('disabled', true);
			  $("#hideChangePwd").addClass("dis-none");
			  $(".changepwd").removeClass("dis-none");
			  $("#updateBtn").prop('disabled', false);
			  $("#oldPassword").click();
		  });
		
		
		$("#cancelBtn").click(function(){
		  $("#hideChangePwd").removeClass("dis-none");
		  $(".changepwd").addClass("dis-none");
		  $("#editable").prop('disabled', false);
		  $(".emptyField").val("");
		  resetValidation('#userDetailsForm');
		});
		
		
		$("#updateBtn").click(function(){
		  	/* $("#hideChangePwd").removeClass("dis-none");
		  	$(".changepwd").addClass("dis-none");  */
		  	
		  	var oldPassword = $('#oldPassword').val();
			var newPassword = $('#password').val();
  	  		isFromValid("#userDetailsForm");
  	  		if($(".has-danger").length < 1){
  	  		var thisAttr= this;
				if(oldPassword != newPassword){
					$(".changepwd .help-block ul").remove();
					$("#updateBtn").prop('disabled', true);
					$.ajax({
						url : "/fdahpStudyDesigner/adminDashboard/changePassword.do",
						type : "POST",
						datatype : "json",
						data : {
							oldPassword : oldPassword,
							newPassword : newPassword,
							"${_csrf.parameterName}":"${_csrf.token}"
						},
						success : function getResponse(data, status) {
							var jsonObj = eval(data);
							var message = jsonObj.message;								
							if('SUCCESS' == message){
								showSucMsg('Password updated successfully.');
								/* $("#sucMsg").show();
								$("#errMsg").hide(); */
								$("#cancelBtn").click();
							} else {
								showErrMsg(message);
								$("input[type='password']").prop("required",true);
							}
							$(window).scrollTop(0);
							$("#updateBtn").prop('disabled', false);
							//setTimeout(hideDisplayMessage, 4000);
							$(".changepwd .emptyField").val("");
						},
					});
  	  		}else{
  	  		showErrMsg('New password should not be same as old Password.');
					$(window).scrollTop(0);
					$(".changepwd .emptyField").val("");
					//setTimeout(hideDisplayMessage, 4000);
					$("#updateBtn").prop('disabled', false);
				}
  	  	}else{
					
				}
		});
          
	      
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
			
			 $('#alertMsg').click(function(){
				$('#alertMsg').hide();
			}); */
			
	  });
	  
	  /* Password buttons ends */
	  /* function hideDisplayMessage(){
			$('#sucMsg').hide();
			$('#errMsg').hide();
		} */
	  
	 /*  function formSubmit() {
			document.getElementById("logoutForm").submit();
		} */
	  
	 var addPasswordPopup = function() {
		 $("#password").passwordValidator({
				// list of qualities to require
				require: ['length', 'lower', 'upper', 'digit','spacial'],
				// minimum length requirement
				length: 8
			});
		}
   </script>
