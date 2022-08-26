<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<head>
<meta charset="UTF-8">
<style>
.btns__devider {
	display: inline-block;
	border-top: 1px solid #dddddd;
	padding-top: 30px;
	margin-top: 20px;
}

#spec-tooltip .tooltip-inner {
	min-width: 430px !important;
}

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

.bold_txt{
font-weight:900 !important;
color:#007cba !important;
font-size:15px;
 }

#timeOutMessage{
width:257px;
}
.close{
background:none;
}
</style>
</head>

<div class="col-sm-10  col-lg-9 col-rc white-bg p-none">

	<!--  Start top tab section-->
	<div class="right-content-head">
		<div class="text-right">
			<div class="black-md-f text-uppercase dis-line pull-left line34">ACTIONS</div>
			<div class="dis-line form-group mb-none mr-sm"></div>
			<div class="dis-line form-group mb-none"></div>
		</div>
	</div>
	<!--  End  top tab section-->
	<!--  Start body tab section -->
	<div class="right-content-body pt-none pl-xlg">
		<div class="gray-xs-f mb-xs mt-xlg mb-md" id="spec-tooltip">
			Select Publishing Mode<span class="requiredStar">*</span><span
				class="ml-xs sprites_v3 filled-tooltip" data-placement="top" data-toggle="tooltip"
				title="Use the Test Mode to verify your study content and behavior in a test app prior to actual launch/publishing in Live Mode.  Please note that the study cannot be reverted back to Test Mode once you switch to the Live Mode. The Study ID and App ID fields cannot be edited once the study is launched (or published as an upcoming study) in either Test Mode or Live Mode. However, after you switch from Test Mode to Live Mode, you will need to enter in a Study ID and an App ID that are different from what were used in Test mode, before you launch (or publish as upcoming study) the study in Live Mode."></span>
		</div>
		<div class="pb-lg ">
			<span class="radio radio-info radio-inline p-40 pl-1"> <input
				type="radio" id="testmode" class="typeofmode" value="testmode"
				name="modetype" checked
				<c:if test="${not empty permission}">disabled</c:if>
				<c:if test="${not studyPermissionBO.viewPermission}">disabled</c:if>>
				<label for="testmode">Test Mode</label>
			</span> <span class="tool-tip" data-toggle="tooltip" data-html="true"
				data-placement="top"> <span class="radio radio-inline p-40">
					<input type="radio" id="livemode" class="typeofmode"
					value="livemode" name="modetype"
					<c:if test="${not empty permission}">disabled</c:if>
					<c:if test="${not studyPermissionBO.viewPermission}">disabled</c:if>> 
					<label for="livemode">Live Mode</label>
			</span>
			</span>
		</div>
		<div>
			<c:if test="${studyBo.studyPreActiveFlag eq false}">
				<div class="form-group mr-sm" style="white-space: normal;">
					<button type="button" class="btn btn-primary blue-btn-action" style="white-space: nowrap;"
						id="publishId" onclick="validateStudyStatus(this);"
						<c:choose>
				             <c:when test="${not empty permission}">
				                disabled
				             </c:when>
				             <c:when test="${not empty studyBo.status && (studyBo.status eq 'Paused' || studyBo.status eq 'Pre-launch(Published)' || studyBo.status eq 'Active' || studyBo.status eq 'Deactivated')}">
				                    disabled
				             </c:when>
				            </c:choose>
						<c:if test="${not studyPermissionBO.viewPermission}">disabled</c:if>>Publish
						as Upcoming Study</button>
				</div>
			</c:if>
			<c:if test="${studyBo.studyPreActiveFlag eq true}">
				<div class="form-group mr-sm" style="white-space: normal;">
					<button type="button" class="btn btn-primary blue-btn-action"
						id="unpublishId" onclick="validateStudyStatus(this);"
						<c:choose>
				             <c:when test="${not empty permission}">
				                disabled
				             </c:when>
				             <c:when test="${not empty studyBo.status && (studyBo.status eq 'Paused' || studyBo.status eq 'Active' || studyBo.status eq 'Deactivated')}">
				                    disabled
				             </c:when>
				            </c:choose>
						<c:if test="${not studyPermissionBO.viewPermission}">disabled</c:if>>Unpublish</button>
				</div>
			</c:if>
			<div class="form-group mr-sm" style="white-space: normal;">
				<button type="button" class="btn btn-default gray-btn-action "
					id="lunchId" onclick="validateStudyStatus(this);"
					<c:choose>
				            <c:when test="${not empty permission}">
				                disabled
				             </c:when>
				             <c:when test="${not empty studyBo.status && (studyBo.status eq 'Active' || studyBo.status eq 'Paused' || studyBo.status eq 'Deactivated')}">
				                    disabled
				             </c:when>
				            </c:choose>
					<c:if test="${not studyPermissionBO.viewPermission}">disabled</c:if>>Launch
					Study</button>
			</div>

			<div class="form-group mr-sm" style="white-space: normal;">
				<button type="button" class="btn btn-default gray-btn-action"
					id="updatesId" onclick="validateStudyStatus(this);"
					<c:choose>
					            <c:when test="${not empty permission}">
					                disabled
					             </c:when>
					             <c:when test="${not empty studyBo.status && empty liveStudyBo && (studyBo.hasStudyDraft==0  || studyBo.status eq 'Pre-launch' || studyBo.status eq 'Pre-launch(Published)' || 
					             studyBo.status eq 'Paused' || studyBo.status eq 'Deactivated')}">
			                      disabled
			                    </c:when>
					             <c:when test="${not empty studyBo.status && not empty liveStudyBo && (studyBo.hasStudyDraft==0  || studyBo.status eq 'Pre-launch' || studyBo.status eq 'Pre-launch(Published)' || 
					             studyBo.status eq 'Paused' || studyBo.status eq 'Deactivated' || liveStudyBo.status eq 'Paused')}">
					                    disabled
					             </c:when>
					            </c:choose>
					<c:if test="${not studyPermissionBO.viewPermission}">disabled</c:if>>Publish
					Updates</button>
			</div>

			<div class="form-group mr-sm" style="white-space: normal;">
				<button type="button" class="btn btn-default gray-btn-action "
					id="pauseId" onclick="validateStudyStatus(this);"
					<c:choose>
			             <c:when test="${not empty permission}">
			                disabled
			             </c:when>
			             <c:when test="${empty liveStudyBo && not empty studyBo.status && (studyBo.status eq 'Pre-launch' || studyBo.status eq 'Pre-launch(Published)' || studyBo.status eq 'Paused'  || studyBo.status eq 'Deactivated')}">
			                    disabled
			             </c:when>
			             <c:when test="${not empty liveStudyBo && not empty liveStudyBo.status && (liveStudyBo.status eq 'Pre-launch' || liveStudyBo.status eq 'Pre-launch(Published)' || liveStudyBo.status eq 'Paused'  || liveStudyBo.status eq 'Deactivated')}">
			                    disabled
			             </c:when>
			           </c:choose>
					<c:if test="${not studyPermissionBO.viewPermission}">disabled</c:if>
					<%-- <c:if test="${empty liveStudyBo && studyMode eq 'testMode'}">disabled</c:if> --%>>Pause</button>
			</div>

			<div class="form-group mr-sm" style="white-space: normal;">
				<button type="button" class="btn btn-default gray-btn-action "
					id="resumeId" onclick="validateStudyStatus(this);"
					<c:choose>
				             <c:when test="${not empty permission}">
				                disabled
				             </c:when>
				             <c:when test="${empty liveStudyBo && not empty studyBo.status && (studyBo.status eq 'Pre-launch' || studyBo.status eq 'Pre-launch(Published)' || studyBo.status eq 'Active' || studyBo.status eq 'Deactivated')}">
				                    disabled
				             </c:when>
				             <c:when test="${not empty liveStudyBo && not empty liveStudyBo.status && (liveStudyBo.status eq 'Pre-launch' || liveStudyBo.status eq 'Pre-launch(Published)' || liveStudyBo.status eq 'Active'  || liveStudyBo.status eq 'Deactivated')}">
			                    disabled
			                 </c:when>
				            </c:choose>
					<c:if test="${not studyPermissionBO.viewPermission}">disabled</c:if>>Resume</button>
			</div>

			<div class="form-group mr-sm" style="white-space: normal;">
				<span class="btns__devider">
					<button type="button" class="btn btn-default red-btn-action "
						id="deactivateId" onclick="validateStudyStatus(this);"
						<c:choose>
			             <c:when test="${not empty permission}">
			                disabled
			             </c:when>
			             <c:when test="${not empty studyBo.status && (studyBo.status eq 'Pre-launch' || studyBo.status eq 'Pre-launch(Published)' || studyBo.status eq 'Deactivated')}">
			                    disabled
			             </c:when>
			            </c:choose>
						<c:if test="${not studyPermissionBO.viewPermission}">disabled</c:if>
						<%-- <c:if test="${empty liveStudyBo && studyMode eq 'testMode'}">disabled</c:if> --%>>Deactivate</button>
				</span>
			</div>
		</div>
	</div>
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
<form:form
	action="/fdahpStudyDesigner/adminStudies/updateStudyAction.do?_S=${param._S}"
	name="actionInfoForm" id="actionInfoForm" method="post">
	<input type="hidden" name="studyId" id="studyId" value="${studyBo.id}" />
	<input type="hidden" name="buttonText" id="buttonText" value="" />
	<input type="hidden" name="language" id="studyLanguage" value="${currLanguage}" />
</form:form>
<form:form
	action="/fdahpStudyDesigner/adminStudies/studyList.do?_S=${param._S}"
	name="studyListInfoForm" id="studyListInfoForm" method="post">
</form:form>
<form:form
             action="/fdahpStudyDesigner/sessionOut.do"
              id="backToLoginPage"
              name="backToLoginPage"
              method="post">
</form:form>
<script type="text/javascript">
var idleTime = 0;
	$(document).ready(function() {
		$(".menuNav li").removeClass('active');
		$(".tenth").addClass('active');
		$("#createStudyId").show();
		$('.tenth').removeClass('cursor-none');

		var studyMode = "${studyMode}";
		if(studyMode == "liveMode"){
			$("#livemode").prop("checked", true);
			$("#testmode").attr('disabled', true);
		}else{
			$("#testmode").prop("checked", true);
		}

		let currLang = $('#studyLanguage').val();
		if (currLang !== undefined && currLang !== null && currLang !== '' && currLang !== 'en') {
			refreshAndFetchLanguageData(currLang);
		}

		setInterval(function () {
              idleTime += 1;
               if (idleTime > 3) { // 5 minutes
               timeOutFunction();
                }
                }, 226000);

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
                  if ($('#myModal').hasClass('show')) {
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
	});
	$('input[type="radio"]').change(function() {
		var studyId = "${studyBo.id}";
		if ($(this).is(':checked') && $(this).val() == 'livemode') {
			bootbox.confirm({
			    message: "Are you sure you wish to switch to Live Mode from Test Mode? This action is NOT reversible.  You will need to enter a new Study ID and App ID before publishing the study in Live Mode. ",
			    buttons: {
			        confirm: {
			            label: 'Yes',
			        },
			        cancel: {
			            label: 'No',
			        }
			    },
			    callback: function (result) {
			    	if(result){
			    		$.ajax({
				    			url: "/fdahpStudyDesigner/adminStudies/switchToLiveMode.do?_S=${param._S}",
				    			type: "POST",
				    			datatype: "json",
				    			data:{
				    				studyId : studyId,
				    				"${_csrf.parameterName}":"${_csrf.token}",
				    			},
				    			success: function switchToLiveMode(data){
				    				var status = data.message;
				    				var studyMode = data.studyMode;
				    				if(status == "SUCCESS"){
				    					document.studyListInfoForm.action = "/fdahpStudyDesigner/adminStudies/viewBasicInfo.do?_S=${param._S}";
										document.studyListInfoForm.submit();
				    				}
				    			},
				    			error: function(xhr, status, error) {
				    			  $("#testmode").prop("checked", true);
				    			}
				    		});
					}else{
						$("#testmode").prop("checked", true);
					}
			    }
			});
		}
	});
	function validateStudyStatus(obj) {
		var buttonText = obj.id;
		var messageText = "";
		if (buttonText) {
			if (buttonText == 'unpublishId' || buttonText == 'pauseId'
					|| buttonText == 'deactivateId') {
				if (buttonText == 'unpublishId') {
					messageText = "You are attempting to Unpublish the study. Are you sure you wish to proceed?";
				} else if (buttonText == 'pauseId') {
					messageText = "You are attempting to Pause the study. Mobile app users can no longer participate in study activities until you resume the study again. However, they will still be able to view the study dashboard and study resources. Are you sure you wish to proceed?";
				} else if (buttonText == 'deactivateId') {
					messageText = "You are attempting to Deactivate a live study. Once deactivated, mobile app users will no longer be able to participate in the study. Also, deactivated studies can never be reactivated. Are you sure you wish to proceed?";
				}
				bootbox.confirm({
					closeButton : false,
					message : messageText,
					buttons : {
						'cancel' : {
							label : 'Cancel',
						},
						'confirm' : {
							label : 'OK',
						},
					},
					callback : function(result) {
						if (result) {
							updateStudyByAction(buttonText);
						}
					}
				});
			} else {
				$
						.ajax({
							url : "/fdahpStudyDesigner/adminStudies/validateStudyAction.do?_S=${param._S}",
							type : "POST",
							datatype : "json",
							data : {
								buttonText : buttonText,
								"${_csrf.parameterName}" : "${_csrf.token}",
							},
							success : function emailValid(data, status) {
								var message = data.message;
								var checkListMessage = data.checkListMessage;
								var checkFailureMessage = data.checkFailureMessage;
								var isRequiredSectionsCompleted=data.isRequiredSectionsCompleted;
								if (message == "SUCCESS") {
									if (checkListMessage == "Yes") {
										showBootBoxMessage(buttonText,
												messageText);
									} else {
										bootbox.confirm({
											closeButton : false,
											message : checkFailureMessage,
											buttons : {
												'cancel' : {
													label : 'Cancel',
												},
												'confirm' : {
													label : 'OK',
												},
											},
											callback : function(result) {
												if (result) {
													showBootBoxMessage(
															buttonText,
															messageText);
												}
											}
										})
									}
								} else {
									if (buttonText == 'publishId') {
										messageText = "To publish a study as an Upcoming study, the Basic Information, Settings, Overview and Consent sections for the language selected need to be marked as completed indicating you have finished adding all mandatory and sufficient content in those sections to give mobile app users a fair idea about the upcoming study. Please complete these sections and try again.";
									} else if (buttonText == 'lunchId') {
										messageText = "Launching to a study requires that all sections be marked as completed for the language selected indicating that you have finished adding all mandatory and intended content in the section. Please complete all the sections and try again.";
									} else if (buttonText == 'updatesId') {
										messageText = "Publish Updates to a study requires that all sections be marked as completed indicating that you have finished adding all mandatory and intended content in the section for the language selected. Please complete all the sections  for the language selected and try again.";
									}
									bootbox.confirm(message, function(result) {
										if(!isRequiredSectionsCompleted){
											bootbox.alert(messageText);
										}
									})

								}
							},
							error : function status(data, status) {
								$("body").removeClass("loading");
							},
							complete : function() {
								$('.actBut').removeAttr('disabled');
							}
						});
			}
		}

	}
	function showErrMsg1(message) {
		$("#alertMsg").removeClass('s-box').addClass('e-box').text(message);
		$('#alertMsg').show('10000');
		setTimeout(hideDisplayMessage, 10000);
	}
	function showBootBoxMessage(buttonText, messageText) {
		if (buttonText == 'resumeId') {
			messageText = "You are attempting to Resume a paused study. This will activate the study and allow mobile app users to resume participation in study activities with the latest study content.  Are you sure you wish to proceed?";
		} else if (buttonText == 'publishId') {
			messageText = "You are attempting to Publish the study. Are you sure you wish to proceed?";
		} else if (buttonText == 'lunchId') {
			messageText = "You are attempting to Launch the study. This will make the study available for mobile app users to explore and join. Are you sure you wish to proceed?";
		} else if (buttonText == 'updatesId') {
			messageText = "You are attempting to Publish Updates to the study. This will make all new updates available to mobile app users. Are you sure you wish to proceed?";
		}
		bootbox.confirm({
			closeButton : false,
			message : messageText,
			buttons : {
				'cancel' : {
					label : 'Cancel',
				},
				'confirm' : {
					label : 'OK',
				},
			},
			callback : function(result) {
				if (result) {
					updateStudyByAction(buttonText);
				}
			}
		})
	}

	function updateStudyByAction(buttonText) {
		if (buttonText) {
			var studyId = "${studyBo.id}";
			$
					.ajax({
						url : "/fdahpStudyDesigner/adminStudies/updateStudyAction.do?_S=${param._S}",
						type : "POST",
						datatype : "json",
						data : {
							buttonText : buttonText,
							studyId : studyId,
							"${_csrf.parameterName}" : "${_csrf.token}",
						},
						success : function updateAction(data, status) {
							var message = data.message;
							if (message == "SUCCESS") {
								if (buttonText == 'deactivateId'
										|| buttonText == 'lunchId'
										|| buttonText == 'updatesId') {
									$('#studyListInfoForm').submit();
								} else {
									document.studyListInfoForm.action = "/fdahpStudyDesigner/adminStudies/actionList.do?_S=${param._S}";
									document.studyListInfoForm.submit();
								}
							} else {
								$('#studyListInfoForm').submit();
							}
						},
						error : function status(data, status) {
							$("body").removeClass("loading");
						},
						complete : function() {
							$('.actBut').removeAttr('disabled');
						}
					});
		}
	}

	function refreshAndFetchLanguageData(language) {
		$.ajax({
			url: '/fdahpStudyDesigner/adminStudies/actionList.do?_S=${param._S}',
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