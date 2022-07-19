<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<head>
  <meta charset="UTF-8">
  <style>
  #autoSavedMessage{
  width:257px;
  }

  #myModal .modal-dialog, #learnMyModal .modal-dialog .flr_modal{
  position:relative !important;
  right:-14px !important;
  margin-top:6% !important;
  }

   #timeOutModal .modal-dialog, #learnMyModal .modal-dialog .flr_modal{
     position:relative !important;
     right:-14px !important;
     margin-top:6% !important;
     }

  .flr_modal{
  float:right !important;
  }

  .grey_txt{
  color:grey;
  font-size:15px;
  font-weight:500;
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

  .bold_txt{
  font-weight:900 !important;
  color:#007cba !important;
  font-size:15px;
   }
  </style>
</head>
  <!-- ============================================================== -->
         <!-- Start right Content here -->
         <!-- ============================================================== --> 
        <div class="col-sm-10 col-rc white-bg p-none">
        <form:form action="/fdahpStudyDesigner/sessionOut.do" id="backToLoginPage" name="backToLoginPage" method="post"></form:form>
        <form:form action="/fdahpStudyDesigner/adminStudies/saveOrDoneChecklist.do?${_csrf.parameterName}=${_csrf.token}&_S=${param._S}" id="checklistForm" role="form" method="post" autocomplete="off" enctype="multipart/form-data">    
            <input type="hidden" name="checklistId" value="${checklist.checklistId}">
            <input type="hidden" id="actionBut" name="actionBut">
            <input type="hidden" name="language" id="studyLanguage" value="${currLanguage}" />
            <input type="hidden" id="isAutoSaved" value="${isAutoSaved}" name="isAutoSaved"/>
            <!--  Start top tab section-->
            <div class="right-content-head">        
                <div class="text-right">
                    <div class="black-md-f dis-line pull-left line34"><span class="pr-sm">Checklist</span></div>
                    <div class="dis-line form-group mb-none mr-sm">
                         <button type="button" class="btn btn-default gray-btn cancelBut">Cancel</button>
                     </div>
                     <c:if test="${empty permission}">
	                     <div class="dis-line form-group mb-none mr-sm">
	                         <button type="button" class="btn btn-default gray-btn" id="saveChecklistId">Save</button>
	                     </div>
	
	                     <div class="dis-line form-group mb-none">
	                         <button type="button" class="btn btn-primary blue-btn" id="doneChecklistId">Done</button>
	                     </div>
                     </c:if>
                 </div>
            </div>
            <div class="new-checkbox right-content-body">
            <div>
                 <span>This checklist is meant to serve as a reminder for tasks to be completed before you launch or go-live with a study. Mark tasks as completed as and when you finish them.</span>
            </div>
            <div class="checkbox__container pt-lg">
                <div class="checkbox checkbox-inline p-45 pb-md">
                      <div>
	                       <input type="checkbox" id="inlineCheckbox1" class="class" name="checkbox1" <c:if test="${checklist.checkbox1}">checked</c:if> required>
	                       <label for="inlineCheckbox1"> IRB Review Completed </label>
                      </div>
                      <div class="pl-13">
                      	<span class="gray-xs-f mb-xs">Final IRB approval letter received for all participating institutions</span>
                      </div>
                </div>
                <div class="checkbox checkbox-inline p-45 pb-md">
                       <div>
                            <input type="checkbox" id="inlineCheckbox2" class="class" name="checkbox2" <c:if test="${checklist.checkbox2}">checked</c:if> required>
                            <label for="inlineCheckbox2"> Participant Cohort Identified </label>
                       </div>
                       <div class=" pl-13">
                       		<span class="gray-xs-f mb-xs">(If using pre-screened participant group)Participant cohort defined and identified for the study.</span>
                       </div>
               </div>
                <div class="checkbox checkbox-inline p-45 pb-md">
	                <div>
	                     <input type="checkbox" id="inlineCheckbox3" class="class" name="checkbox3" <c:if test="${checklist.checkbox3}">checked</c:if> required>
	                     <label for="inlineCheckbox3"> Platform Support and Participant Device Usage Verified </label>
	                </div>
                     <div class=" pl-13">
                     	<span class="gray-xs-f mb-xs">(If using pre-screened participant group) Verified participants have smartphones corresponding to at least one of the platforms chosen for the study (iOS, Android).</span>
              		</div> 
              </div>
              <div class="checkbox checkbox-inline p-45 pb-md">
              	<div>
                   <input type="checkbox" id="inlineCheckbox4" class="class" name="checkbox4" <c:if test="${checklist.checkbox4}">checked</c:if> required>
                   <label for="inlineCheckbox4"> Eligibility - Tokens Generated  </label>
                </div>
                 <div class=" pl-13">
                 	<span class="gray-xs-f mb-xs">(If using token validation method of eligibility), tokens generated on the Labkey portal </span>
           		 </div>
             </div>
             <div class="checkbox checkbox-inline p-45 pb-md">
              	<div>
                   <input type="checkbox" id="inlineCheckbox5" class="class" name="checkbox5" <c:if test="${checklist.checkbox5}">checked</c:if> required>
                   <label for="inlineCheckbox5"> Eligibility - Token Distribution </label>
               </div>
               <div class=" pl-13"> 
                  <span class="gray-xs-f mb-xs">(If using token validation method of eligibility), tokens distributed to pre-screened participants </span>
             	</div>
             </div>
            
              <div class="checkbox checkbox-inline p-45 pb-md">
              	<div> 
                   <input type="checkbox" id="inlineCheckbox6" class="class" name="checkbox6" <c:if test="${checklist.checkbox6}">checked</c:if> required>
                   	<label for="inlineCheckbox6"> App Install and Study Start Instructions Provided  </label>
                </div>
                <div class=" pl-13"> 
                   	<span class="gray-xs-f mb-xs">Instructions provided to participants on downloading and installing the app, intimation about study start date. </span>
             	</div>
             </div>
             <div class="checkbox checkbox-inline p-45 pb-md">
            	 <div> 
                  	<input type="checkbox" id="inlineCheckbox7" class="class" name="checkbox7" <c:if test="${checklist.checkbox7}">checked</c:if> required>
                  	<label for="inlineCheckbox7"> Appearance of full consent document - Reviewed and Confirmed </label>
              	</div>
              	<div class=" pl-13"> 
              		<span class="gray-xs-f mb-xs">Full consent document reviewed. Confirmed it matches the IRB reviewed consent</span>
              	</div>
            </div>
            <div class="checkbox checkbox-inline p-45 pb-md">
            	<div> 
                 	<input type="checkbox" id="inlineCheckbox8" class="class" name="checkbox8" <c:if test="${checklist.checkbox8}">checked</c:if> required>
                 	<label for="inlineCheckbox8"> Questionnaires Added </label>
               </div>
               <div class=" pl-13"> 
               		<span class="gray-xs-f mb-xs">At a minimum, baseline questionnaires have been filled out and scheduled for desired time period and frequency.</span>
           		</div>
           </div>
           <div class="checkbox checkbox-inline p-45 pb-md">
           		<div> 
                	<input type="checkbox" id="inlineCheckbox9" class="class" name="checkbox9" <c:if test="${checklist.checkbox9}">checked</c:if> required>
                	<label for="inlineCheckbox9"> Active Tasks Added </label>
                </div>
                <div class=" pl-13"> 
                	<span class="gray-xs-f mb-xs">If applicable to the study, active task added and scheduled for desired time period and frequency. </span>
          		</div>
          </div>
          <div class="checkbox checkbox-inline p-45 pb-md">
          		<div>
              	 	<input type="checkbox" id="inlineCheckbox10" class="class" name="checkbox10" <c:if test="${checklist.checkbox10}">checked</c:if> required>
              	 	<label for="inlineCheckbox10"> Resources Added </label>
               	</div>
               	<div class=" pl-13">
               		<span class="gray-xs-f mb-xs">Baseline study resources added and reviewed for content and period of visibility.</span>
         		</div>
         </div>
         <div class="checkbox checkbox-inline p-45 pb-md">
         	<div>
              	<input type="checkbox" id="inlineCheckbox11" class="class" name="checkbox11" <c:if test="${checklist.checkbox11}">checked</c:if> required>
              	<label for="inlineCheckbox11"> Standalone Study App - Tested and Verified  </label>
            </div>
            <div class=" pl-13">
              	<span class="gray-xs-f mb-xs">(If study created as a Standalone one), all functionality and UI has been tested and verified on the standalone test app provided by BTC.</span>
        	</div>
        </div>
         <div class="checkbox checkbox-inline p-45 pb-md">
         	<div>
              	<input type="checkbox" id="inlineCheckbox12" class="class" name="checkbox12" <c:if test="${checklist.checkbox12}">checked</c:if> required>
              	<label for="inlineCheckbox12"> Standalone Study App - Approved and Live on App Store/Play Store</label>
            </div>
            <div class=" pl-13">
            	<span class="gray-xs-f mb-xs">(If study created as a Standalone one), standalone app has been submitted to and approved by App Store/Play Store and live for participants to download from there.</span>
        	</div>
        </div>
      </div>
   </div>
            <!--  End body tab section -->
 </form:form>

    <div class="modal fade" id="myModal" role="dialog">
        <div class="modal-dialog modal-sm flr_modal">
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-body">
                  <div id="autoSavedMessage" class="text-right">
                    <div class="blue_text">Last saved now</div>
                    <div class="grey_txt"><span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in <span class="bold_txt">15 minutes</span></div>
                    </div>
                  </div>
                </div>
            </div>
        </div>

              <div class="modal fade" id="timeOutModal" role="dialog">
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
        <!-- End right Content here -->

<script type="text/javascript">
var idleTime = 0;
$(document).ready(function(){
	    $(".menuNav li").removeClass('active');
	    $(".nine").addClass('active'); 

	    <c:if test="${not empty permission}">
	    	$('.class').prop('disabled',true);
	    </c:if>
		
		$('#saveChecklistId').click(function() {
			autoSaveCheckListPage('manual');
		});

  let currLang = $('#studyLanguage').val();
  if (currLang !== undefined && currLang !== null && currLang !== '' && currLang !== 'en') {
    refreshAndFetchLanguageData(currLang);
  }
		
		$("#doneChecklistId").on('click', function(){
				 bootbox.confirm({
						closeButton: false,
						message : 'Are you sure you have no more updates to be made in this section? Clicking Done will mark this section as Complete.',	
					    buttons: {
					        'cancel': {
					            label: 'Cancel',
					        },
					        'confirm': {
					            label: 'OK',
					        },
					    },
					    callback: function(result) {
					        if (result) {
					        	$('#actionBut').val('done');
						 		$('#checklistForm').submit();
					        }
					    }
				    });
		});
		setInterval(function () {
                idleTime += 1;
                if (idleTime > 3) {
                        <c:if test="${permission ne 'view'}">
                        autoSaveCheckListPage('auto');
                         </c:if>
                        <c:if test="${permission eq 'view'}">
                            timeOutFunction();
                        </c:if>
                }
            }, 226000); // 5 minutes

            $(this).mousemove(function (e) {
                idleTime = 0;
            });
            $(this).keypress(function (e) {
                idleTime = 0;
            });

        function timeOutFunction() {
         $('#timeOutModal').modal('show');
          let i = 14;
          let timeOutInterval = setInterval(function () {
           if (i === 0) {
            $('#timeOutMessage').html('<span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in ' + i +' minutes');
             if ($('#timeOutModal').hasClass('in')) {
               $('#backToLoginPage').submit();
            }
             clearInterval(timeOutInterval);
               } else {
                 if (i === 1) {
               $('#timeOutMessage').html('<span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in 1 minute');
                 } else {
                 $('#timeOutMessage').html('<span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in ' + i +' minutes');
                }
                 idleTime = 0;
                 i-=1;
                  }
                  }, 60000);
                  }
    // pop message after 15 minutes
    if ($('#isAutoSaved').val() === 'true') {
        $('#myModal').modal('show');
        let i = 1;
        let j =14;
        let lastSavedInterval = setInterval(function () {
            if ((i === 15) || (j === 0))  {
                     $('#autoSavedMessage').html('<div class="blue_text">Last saved was ' + i + ' minutes ago</div><div class="grey_txt"><span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in <span class="bold_txt"> ' + j +' minutes</span></div>').css("fontSize", "15px");
                if ($('#myModal').hasClass('in')) {
                    $('#backToLoginPage').submit();
                }
                clearInterval(lastSavedInterval);
            } else {
                if ((i === 1) || (j === 14)) {
                     $('#autoSavedMessage').html('<div class="blue_text">Last saved was 1 minute ago</div><div class="grey_txt"><span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in <span class="bold_txt"> 14 minutes</span></div>').css("fontSize", "15px");
                }
                else if ((i === 14) || (j === 1)) {
                $('#autoSavedMessage').html('<div class="blue_text">Last saved was 14 minutes ago</div><div class="grey_txt"><span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in <span class="bold_txt"> 1 minute</span></div>')
                }
                else {
                     $('#autoSavedMessage').html('<div class="blue_text">Last saved was ' + i + ' minutes ago</div><div class="grey_txt"><span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in <span class="bold_txt"> ' + j +' minutes</span></div>').css("fontSize", "15px");
                }
                idleTime = 0;
                i+=1;
                j-=1;
            }
        }, 60000);
    }
});
function autoSaveCheckListPage(mode){
 $('#actionBut').val('save');
 $('#loader').show();
 if (mode === 'auto') {
  $("#isAutoSaved").val('true');
   }
   else{
   $("#isAutoSaved").val('false');
   }
	$('#checklistForm').submit();
}
function refreshAndFetchLanguageData(language) {
  $.ajax({
    url: '/fdahpStudyDesigner/adminStudies/getChecklist.do?_S=${param._S}',
    type: "GET",
    data: {
      language: language,
    },
    success: function (data) {
      let htmlData = document.createElement('html');
      htmlData.innerHTML = data;
      if (language !== 'en') {
        updateCompletionTicks(htmlData);
      }
    }
  })
}

</script>
