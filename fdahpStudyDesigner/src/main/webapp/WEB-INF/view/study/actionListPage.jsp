<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="col-sm-10 col-rc white-bg p-none">
            
            <!--  Start top tab section-->
            <div class="right-content-head">        
                <div class="text-right">
                    <div class="black-md-f text-uppercase dis-line pull-left line34">ACTIONS</div>
                    <div class="dis-line form-group mb-none mr-sm">
                     </div>
					<div class="dis-line form-group mb-none">
					</div>
                 </div>
            </div>
            <!--  End  top tab section-->
            <!--  Start body tab section -->
            <div class="right-content-body">
               <div> 
	                <div class="form-group mr-sm" style="white-space: normal;width: 100px;">
	                         <button type="button" class="btn btn-primary blue-btn" id="publishId" onclick="validateStudyStatus(this);" <c:if test="${not empty studyBo.status && (studyBo.status eq 'Paused' || studyBo.status eq 'Active' || studyBo.status eq 'Launched' || studyBo.status eq 'Deactivated')}">disabled</c:if>>Publish as Upcoming Study</button>
	                </div>
	                     
	                <div class="form-group mr-sm" style="white-space: normal;width: 100px;">
	                         <button type="button" class="btn btn-default gray-btn " id="lunchId" onclick="validateStudyStatus(this);" <c:if test="${not empty studyBo.status && (studyBo.status eq 'Pre-launch' || studyBo.status eq 'Launched' || studyBo.status eq 'Paused' || studyBo.status eq 'Deactivated') }">disabled</c:if>>Launch Study</button>
	                </div> 
	                
<!-- 	                <div class="form-group mr-sm" style="white-space: normal;width: 100px;"> -->
<%-- 	                         <button type="button" class="btn btn-default gray-btn" id="updatesId" onclick="validateStudyStatus(this);" <c:if test="${not empty studyBo.status && studyBo.status ne 'Pre-launch' && studyBo.status ne 'Launched' && studyBo.status ne 'Paused' && studyBo.status ne 'Deactivated' }">disabled</c:if>>Publish Updates</button> --%>
<!-- 	                </div>   -->
	                
<!-- 			       <div class="form-group mr-sm" style="white-space: normal;width: 100px;"> -->
<%-- 			             <button id="addpage" type="button" class="btn btn-default gray-btn " id="pauseId" onclick="validateStudyStatus(this);" <c:if test="${not empty studyBo.status && studyBo.status ne 'Pre-launch' && studyBo.status ne 'Launched' && studyBo.status ne 'Paused' && studyBo.status ne 'Deactivated' }">disabled</c:if>>Pause</button> --%>
<!-- 			       </div> -->
			       
<!-- 			       <div class="form-group mr-sm" style="white-space: normal;width: 100px;"> -->
<%-- 			             <button id="addpage" type="button" class="btn btn-default gray-btn " id="resumeId" onclick="validateStudyStatus(this);" <c:if test="${not empty studyBo.status && studyBo.status ne 'Pre-launch' && studyBo.status ne 'Launched' && studyBo.status ne 'Paused' && studyBo.status ne 'Deactivated' }">disabled</c:if>>Resume</button> --%>
<!-- 			       </div> -->
			       
<!-- 			       <div class="form-group mr-sm" style="white-space: normal;width: 100px;"> -->
<%-- 			             <button id="addpage" type="button" class="btn btn-default gray-btn " id="deactivateId" onclick="validateStudyStatus(this);" <c:if test="${not empty studyBo.status && studyBo.status ne 'Pre-launch' && studyBo.status ne 'Launched' && studyBo.status ne 'Paused' && studyBo.status ne 'Active' }">disabled</c:if>>Deactivate</button> --%>
<!-- 			       </div> -->
            </div>
            </div>
</div>
<form:form action="/fdahpStudyDesigner/adminStudies/updateStudyAction.do" name="actionInfoForm" id="actionInfoForm" method="post">
<input type="hidden" name="studyId" id="studyId" value="${studyBo.id}" />
<input type="hidden" name="buttonText" id="buttonText" value="" />
</form:form>
<script type="text/javascript">
$(document).ready(function(){
	
});
function validateStudyStatus(obj){
	var buttonText = obj.id;
     if(buttonText){
    	 $.ajax({
             url: "/fdahpStudyDesigner/adminStudies/validateStudyAction.do",
             type: "POST",
             datatype: "json",
             data: {
            	 buttonText:buttonText,
                 "${_csrf.parameterName}":"${_csrf.token}",
             },
             success: function emailValid(data, status) {
                 var jsonobject = eval(data);
                 var message = jsonobject.message;
                 if (message == "SUCCESS") {
                	 if(buttonText == 'publishId'){
                		 $('#buttonText').val(buttonText);
                     	 $('#actionInfoForm').submit();
                	 }else if(buttonText == 'lunchId'){
                		 bootbox.confirm("Are you sure you want to updated without checking all checkbox and data retention?", function(result){ 
                 			if(result){
                 				$('#buttonText').val(buttonText);
                            	$('#actionInfoForm').submit();
                 			}
                 		});
                	 }
                 }else{
                	 showErrMsg(message); 
                 }
             },
             error:function status(data, status) {
             	$("body").removeClass("loading");
             },
             global:false,
             complete : function(){ $('.actBut').removeAttr('disabled'); }
         });
     } 

}
</script>