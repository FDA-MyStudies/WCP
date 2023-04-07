<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:useBean id="date" class="java.util.Date"/>
<head>
	<meta charset="UTF-8">
</head>
<style nonce="${nonce}">
	.langSpecific {
		position: relative;
	}

	.langSpecific > button::before {
		content: '';
		display: block;
		background-image: url("../images/global_icon.png");
		width: 16px;
		height: 14px;
		position: absolute;
		top: 9px;
		left: 9px;
		background-repeat: no-repeat;
	}

	.langSpecific > button {
		padding-left: 30px;
	}

	#autoSavedMessage {
		width: 257px;
	}

	#myModal .modal-dialog, #learnMyModal .modal-dialog .flr_modal {
		position: relative !important;
		right: -14px !important;
		margin-top: 6% !important;
	}

	#timeOutModal .modal-dialog, #learnMyModal .modal-dialog .flr_modal {
		position: relative !important;
		right: -14px !important;
		margin-top: 6% !important;
	}

	.flr_modal {
		float: right !important;
	}

	.grey_txt {
		color: grey;
		font-size: 15px;
		font-weight: 500;
	}

	.blue_text {
		color: #007CBA !important;
		font-size: 15px;
		font-weight: 500;
	}

	.timerPos {
		position: relative;
		top: -2px;
		right: 2px !important;
	}

	.bold_txt {
		font-weight: 900 !important;
		color: #007cba !important;
		font-size: 15px;
	}
</style>
<c:set var="tz" value="America/Los_Angeles"/>

<div class="col-sm-10 col-rc white-bg p-none">
	<form:form
			action="/fdahpStudyDesigner/adminStudies/saveOrUpdateStudyNotification.do?${_csrf.parameterName}=${_csrf.token}&_S=${param._S}"
			data-toggle="validator" role="form" id="studyNotificationFormId"
			method="post" autocomplete="off">
		<input type="hidden" name="buttonType" id="buttonType">
		<!-- <input type="hidden" name="currentDateTime" id="currentDateTime"> -->
		<input type="hidden" name="notificationId"
			   value="${notificationBO.notificationId}">
		<input type="hidden" name="actionPage"
			   value="${notificationBO.actionPage}">
		<input type="hidden" name="appId" value="${appId}">
		<input type="hidden" name="language" value="${currLanguage}">
		<input type="hidden" id="mlName" value="${studyLanguageBO.name}"/>
		<input type="hidden" id="customStudyName" value="${fn:escapeXml(studyBo.name)}"/>
		<input type="hidden" id="mlNotificationText"
			   value="${fn:escapeXml(notificationLangBO.notificationText)}"/>
		<input type="hidden" id="isAutoSaved" value="${isAutoSaved}" name="isAutoSaved"/>
		<div class="right-content-head">
			<div class="text-right">
				<div class="black-md-f dis-line pull-left line34">
					<span class="pr-xs"> <a href="javascript:void(0)"
											class="goToNotificationListForm"
											id="goToNotificationListForm"><img
							src="/fdahpStudyDesigner/images/icons/back-b.png"/></a>
					</span>
					<c:if test="${notificationBO.actionPage eq 'edit'}">Edit Notification</c:if>
					<c:if test="${notificationBO.actionPage eq 'addOrCopy'}">Add Notification</c:if>
					<c:if test="${notificationBO.actionPage eq 'view'}">View Notification</c:if>
					<c:if test="${notificationBO.actionPage eq 'resend'}">Resend Notification</c:if>
				</div>

				<c:if test="${studyBo.multiLanguageFlag eq true and notificationBO.actionPage != 'addOrCopy'}">
					<div class="dis-line form-group mb-none mr-sm wid-150">
						<select
								class="selectpicker aq-select aq-select-form studyLanguage langSpecific"
								id="studyLanguage" name="studyLanguage" title="Select">
							<option value="en" ${((currLanguage eq null) or (currLanguage eq '') or  (currLanguage eq 'undefined') or (currLanguage eq 'en')) ?'selected':''}>
								English
							</option>
							<c:forEach items="${languageList}" var="language">
								<option value="${language.key}"
									${currLanguage eq language.key ?'selected':''}>${language.value}</option>
							</c:forEach>
						</select>
					</div>
				</c:if>

				<c:if test="${studyBo.multiLanguageFlag eq true and notificationBO.actionPage == 'addOrCopy'}">
					<div class="dis-line form-group mb-none mr-sm wid-150">
                    <span class="tool-tip" id="markAsTooltipId" data-toggle="tooltip"
						  data-placement="bottom"
						  title="Language selection is available in edit screen only">
						<select class="selectpicker aq-select aq-select-form studyLanguage langSpecific"
								title="Select" disabled>
                        <option selected>English</option>
                    </select>
					</span>
					</div>
				</c:if>

				<div class="dis-line form-group mb-none">
					<button type="button"
							class="btn btn-default gray-btn goToNotificationListForm"
							id="goToStudyListPage">Cancel
					</button>
				</div>
				<div class="dis-line form-group mb-none">
					<button type="button"
							class="btn btn-primary gray-btn deleteNotificationButtonHide ml-sm"
							id="deleteStudyNotification">Delete
					</button>
				</div>
				<div class="dis-line form-group mb-none">
					<button type="button"
							class="btn btn-default gray-btn studyNotificationButtonHide ml-sm mr-sm"
							id="saveStudyId">Save
					</button>
				</div>
				<div class="dis-line form-group mb-none">
					<button type="button"
							class="btn btn-primary blue-btn studyNotificationButtonHide mr-sm"
							id="doneStudyId">Done
					</button>
				</div>
				<div class="dis-line form-group mb-none">
					<button type="button"
							class="btn btn-primary blue-btn resendBuuttonAsDone mr-sm"
							id="resendStudyId">Done
					</button>
				</div>
			</div>
		</div>
		<!-- End top tab section-->
		<!-- Start body tab section -->
		<div class="right-content-body">
			<!-- form- input-->
			<c:if
					test="${notificationBO.notificationSent && notificationBO.actionPage eq 'edit' && not empty notificationHistoryNoDateTime}">
				<div>
					<span>This notification has already been sent out to users
						and cannot be edited. To resend this notification, use the Resend
						action and choose a time for firing the notification.</span>
				</div>
			</c:if>

			<div class="pl-none mt-none">
				<div class="gray-xs-f mb-xs">
					Notification Text (250 characters max) <span class="requiredStar">*</span>
				</div>
				<div class="form-group">
					<textarea autofocus="autofocus" class="form-control"
							  maxlength="250" rows="5" id="notificationText"
							  name="notificationText"
							  required>${notificationBO.notificationText}</textarea>
					<div class="help-block with-errors red-txt"></div>
				</div>
			</div>

			<div class="mt-lg mb-none">
				<div class="form-group hideOnHover">
					<span class="radio radio-info radio-inline p-45 pl-1"> <input
							type="radio" id="inlineRadio1" value="notImmediate"
							name="currentDateTime"
							<c:if test="${notificationBO.notificationScheduleType eq 'notImmediate'}">checked</c:if>
							<c:if test="${notificationBO.actionPage eq 'addOrCopy'}">checked</c:if>>
						<label for="inlineRadio1">Schedule a date / time</label>
					</span> <span class="radio radio-inline"> <input type="radio"
																	 id="inlineRadio2"
																	 value="immediate"
																	 name="currentDateTime"
																	 <c:if test="${notificationBO.notificationScheduleType eq 'immediate'}">checked</c:if>
																	 <c:if test="${studyBo.status ne 'Active'}">disabled</c:if>>
						<label for="inlineRadio2" data-toggle="tooltip"
							   data-placement="top"
							   title="This option will be available once the study is launched.">Send
							Immediately</label>
					</span>
					<div class="help-block with-errors red-txt"></div>
					<c:if test="${not empty notificationHistoryNoDateTime}">
						<c:forEach items="${notificationHistoryNoDateTime}"
								   var="notificationHistory">
							<span class="lastSendDateTime">${notificationHistory.notificationSentdtTime}</span>
							<br>
							<br>
						</c:forEach>
					</c:if>
					<div class="clearfix"></div>
				</div>
			</div>

			<div class="add_notify_option mandatoryForStudyNotification">
				<div class="gray-xs-f mb-xs">
					Select Date <span class="requiredStar">*</span>
				</div>
				<div class="form-group date">
					<input id='datetimepicker' type="text"
						   class="form-control calendar datepicker resetVal" name="scheduleDate"
						   value="${notificationBO.scheduleDate}"
						   oldValue="${notificationBO.scheduleDate}" placeholder="MM/DD/YYYY"
						   disabled/>
					<div class="help-block with-errors red-txt"></div>
				</div>
			</div>

			<div class="add_notify_option mandatoryForStudyNotification">
				<div class="gray-xs-f mb-xs">
					Time <span class="requiredStar">*</span>
				</div>
				<div class="form-group">
					<input id="timepicker1"
						   class="form-control clock timepicker resetVal" id="scheduleTime"
						   name="scheduleTime" value="${notificationBO.scheduleTime}"
						   oldValue="${notificationBO.scheduleTime}" placeholder="00:00"
						   disabled/>
					<div class="help-block with-errors red-txt"></div>
				</div>
			</div>

		</div>
	</form:form>
	<div class="modal fade" id="myModal" role="dialog">
		<div class="modal-dialog modal-sm flr_modal">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-body">
					<div id="autoSavedMessage" class="text-right">
						<div class="blue_text">Last saved now</div>
						<div class="grey_txt"><span class="timerPos"><img
								src="../images/timer2.png"/></span>Your session expires in <span
								class="bold_txt">15 minutes</span></div>
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
					<div id="timeOutMessage" class="text-right blue_text"><span
							class="timerPos"><img src="../images/timer2.png"/></span>Your session
						expires in 15 minutes
					</div>
				</div>
			</div>
		</div>
	</div>
	<!--  End body tab section -->
</div>

<form:form
		action="/fdahpStudyDesigner/adminStudies/viewStudyNotificationList.do?_S=${param._S}"
		id="viewStudyNotificationListPage" name="viewStudyNotificationListPage"
		method="post">
	<input type="hidden" name="language" value="${currLanguage}">
</form:form>

<form:form
		action="/fdahpStudyDesigner/adminStudies/studyList.do?_S=${param._S}"
		name="studyListPage" id="studyListPage" method="post">
</form:form>

<form:form
		action="/fdahpStudyDesigner/adminStudies/deleteStudyNotification.do?_S=${param._S}"
		id="deleteStudyNotificationForm" name="deleteStudyNotificationForm"
		method="post">
	<input type="hidden" name="notificationId"
		   value="${notificationBO.notificationId}">
</form:form>
<script nonce="${nonce}">
	var idleTime = 0;
	$(document).ready(function () {
		var appId = '${appId}';
		$(".menuNav li").removeClass('active');
		$(".eigthNotification").addClass('active');
		$("#createStudyId").show();
		$('.eigthNotification').removeClass('cursor-none');

		let currLang = $('#studyLanguage').val();
		if (currLang !== undefined && currLang !== null && currLang !== '' && currLang !== 'en') {
			$('[name="language"]').val(currLang);
			refreshAndFetchLanguageData(currLang);
		}

		$('[data-toggle="tooltip"]').tooltip();

		<c:if test="${studyBo.status eq 'Active'}">
		$('[data-toggle="tooltip"]').tooltip('dispose');
		</c:if>

		<c:if test="${notificationBO.actionPage eq 'view'}">
		$('[data-toggle="tooltip"]').tooltip('dispose');
		</c:if>

		<c:if test="${notificationBO.actionPage eq 'view'}">
		$('#studyNotificationFormId input,textarea').prop('disabled', true);
		$('.studyNotificationButtonHide').addClass('dis-none');
		$('.deleteNotificationButtonHide').addClass('dis-none');
		if ($('#inlineRadio2').prop('checked')) {
			$('.add_notify_option').addClass('dis-none');
		}
		$('.resendBuuttonAsDone').addClass('dis-none');
		</c:if>

		<c:if test="${notificationBO.actionPage eq 'addOrCopy'}">
		$('.deleteNotificationButtonHide').addClass('dis-none');
		$('.resendBuuttonAsDone').addClass('dis-none');
		if ($('#inlineRadio1').prop('checked')) {
			$('#datetimepicker, #timepicker1').prop('disabled', false);
			$('#datetimepicker, #timepicker1').attr('required', 'required');
		}
		if ($('#inlineRadio2').prop('checked')) {
			$('.add_notify_option').addClass('dis-none');
		}
		</c:if>

		<c:if test="${not notificationBO.notificationSent && notificationBO.actionPage eq 'edit' && empty notificationHistoryNoDateTime}">
		if ($('#inlineRadio1').prop('checked')) {
			$('#datetimepicker, #timepicker1').prop('disabled', false);
			$('#datetimepicker, #timepicker1').attr('required', 'required');
		}
		if ($('#inlineRadio2').prop('checked')) {
			$('.add_notify_option').addClass('dis-none');
		}
		$('.resendBuuttonAsDone').addClass('dis-none');
		</c:if>

		<c:if test="${notificationBO.notificationSent && notificationBO.actionPage eq 'edit' && not empty notificationHistoryNoDateTime}">
		$('[data-toggle="tooltip"]').tooltip('dispose');
		$('#studyNotificationFormId input,textarea').prop('disabled', true);
		$('.deleteNotificationButtonHide').addClass('dis-none');
		$('.studyNotificationButtonHide').addClass('dis-none');
		$('.resendBuuttonAsDone').addClass('dis-none');
		</c:if>

		<c:if test="${not notificationBO.notificationSent && notificationBO.actionPage eq 'edit'}">
		$('.deleteNotificationButtonHide').removeClass('dis-none');
		$('.resendBuuttonAsDone').addClass('dis-none');
		if ($('#inlineRadio1').prop('checked')) {
			$('#datetimepicker, #timepicker1').prop('disabled', false);
			$('#datetimepicker, #timepicker1').attr('required', 'required');
		}
		if ($('#inlineRadio2').prop('checked')) {
			$('.add_notify_option').addClass('dis-none');
		}
		</c:if>

		<c:if test="${not notificationBO.notificationSent && notificationBO.actionPage eq 'edit' && not empty notificationHistoryNoDateTime}">
		$('.deleteNotificationButtonHide').addClass('dis-none');
		$('#studyNotificationFormId textarea').prop('disabled', true);
		</c:if>

		<c:if test="${not notificationBO.notificationSent && notificationBO.actionPage eq 'resend'}">
		$('#studyNotificationFormId input,textarea').prop('disabled', true);
		$('#studyNotificationFormId #inlineRadio2,#inlineRadio2').prop('disabled', true);
		$('.resendBuuttonAsDone').addClass('dis-none');
		$('.deleteNotificationButtonHide').addClass('dis-none');
		$('.studyNotificationButtonHide').addClass('dis-none');
		$('[data-toggle="tooltip"]').tooltip('dispose');
		$('#doneStudyId').addClass('dis-none');
		</c:if>

		<c:if test="${notificationBO.notificationSent && notificationBO.actionPage eq 'resend'}">
		$('#studyNotificationFormId #inlineRadio1').prop('disabled', false);
		<c:if test="${studyBo.status ne 'Active'}">
		$('#studyNotificationFormId #inlineRadio2').prop('disabled', true);
		</c:if>
		<c:if test="${studyBo.status eq 'Active'}">
		$('#studyNotificationFormId #inlineRadio2').prop('disabled', false);
		</c:if>

		$('#studyNotificationFormId textarea,#datetimepicker,#timepicker1,#inlineRadio1').prop(
				'disabled', false);
		$('#studyNotificationFormId textarea').prop('disabled', true);
		if ($('#inlineRadio1').prop('checked')) {
			$('#datetimepicker, #timepicker1').attr('required', 'required');
		}
		if ($('#inlineRadio2').prop('checked')) {
			$('.add_notify_option').addClass('dis-none');
		}
		$('#buttonType').val('resend');
		$('.resendBuuttonAsDone').removeClass('dis-none');
		$('#saveStudyId').addClass('dis-none');
		$('#doneStudyId').addClass('dis-none');
		$('.deleteNotificationButtonHide').addClass('dis-none');
		</c:if>

		$('.studyNotificationList').on('click', function () {
			$('.studyNotificationList').prop('disabled', true);
			$('#viewStudyNotificationListPage').submit();
		});

		$('#deleteStudyNotification').on('click', function () {
			bootbox.confirm("Are you sure you want to delete this notification?",
					function (result) {
						if (result) {
							$('#deleteStudyNotificationForm').submit();
						}
					});
		});

		$('.datepicker').datetimepicker({
			format: 'MM/DD/YYYY',
			ignoreReadonly: true,
			useCurrent: false
		}).on('dp.change change', function (e) {
			validateTime();
		});

		$('.timepicker').datetimepicker({
			format: 'h:mm a',
			minDate: 0
		}).on('dp.change change', function (e) {
			validateTime();
		});

		$(".datepicker").on("click", function (e) {
			$('.datepicker').data("DateTimePicker").minDate(serverDate());
		});

		$(".timepicker").on("click", function (e) {
			var dt = $('#datetimepicker').val();
			if (dt != '' && dt != moment(serverDate()).format("MM/DD/YYYY")) {
				$('.timepicker').data("DateTimePicker").minDate(false);
				$('.timepicker').parent().removeClass('has-error has-danger').find(
						'.help-block.with-errors').empty();
			} else {
				$('.timepicker').data("DateTimePicker").minDate(serverDateTime());
			}
		});

		$('#inlineRadio2').on('click', function () {
			$('#datetimepicker, #timepicker1').removeAttr('required');
			$("#datetimepicker, #timepicker1").parent().removeClass('has-error has-danger');
			$("#datetimepicker, #timepicker1").parent().find(".help-block").empty();
			$('.add_notify_option').addClass('dis-none');
			resetValidation('.mandatoryForStudyNotification');
		});

		$('#inlineRadio1').on('click', function () {
			$('#datetimepicker, #timepicker1').val('');
			$('#datetimepicker, #timepicker1').prop('disabled', false);
			$('.add_notify_option').removeClass('dis-none');
			$('#datetimepicker, #timepicker1').attr('required', 'required');
			$('#studyNotificationFormId').find('.resetVal').each(function () {
				$(this).val($(this).attr('oldValue'));
			});
			resetValidation('.mandatoryForStudyNotification');
		});

		$("#doneStudyId").on('click', function (e) {
			$('#inlineRadio1, #inlineRadio2').attr('required', 'required');
			$('#buttonType').val('done');
			if (isFromValid('#studyNotificationFormId')) {
				if ($('#inlineRadio2').prop('checked')) {
					bootbox.confirm("Are you sure you want to send this notification immediately?",
							function (result) {
								if (result) {
									$('#doneStudyId').prop('disabled', true);
									$('#studyNotificationFormId').submit();
								}
							});
				} else if ($('#inlineRadio1').prop('checked')) {
					if (validateTime()) {
						$('#doneStudyId').prop('disabled', true);
						//$('#appId').val(appId);
						$('#studyNotificationFormId').submit();
					}
				}
			} else {
				$('#doneStudyId').prop('disabled', false);
			}
		});

		$("#resendStudyId").on('click', function (e) {
			$('#inlineRadio1, #inlineRadio2').attr('required', 'required');
			$('#buttonType').val('resend');
			if (isFromValid('#studyNotificationFormId')) {
				$('#notificationText').prop('disabled', false);
				if ($('#inlineRadio2').prop('checked')) {
					bootbox.confirm(
							"Are you sure you want to resend this notification immediately?",
							function (result) {
								if (result) {
									$('#resendStudyId').prop('disabled', true);
									$('#studyNotificationFormId').submit();
								}
							});
				} else if ($('#inlineRadio1').prop('checked')) {
					if (validateTime()) {
						$('#resendStudyId').prop('disabled', true);
						$('#studyNotificationFormId').submit();
					}
				}
			} else {
				$('#resendStudyId').prop('disabled', false);
			}
		});

		$('#saveStudyId').click(function () {
			autoSaveNotificationPage('manual');
		});

		$('.goToNotificationListForm').on('click', function () {


			<c:if test="${notificationBO.actionPage eq 'edit' || notificationBO.actionPage eq 'addOrCopy' && not notificationBO.notificationSent}">
			bootbox.confirm({
				closeButton: false,
				message: 'You are about to leave the page and any unsaved changes will be lost. Are you sure you want to proceed?',
				buttons: {
					'cancel': {
						label: 'Cancel',
					},
					'confirm': {
						label: 'OK',
					},
				},
				callback: function (result) {
					if (result) {
						$('#viewStudyNotificationListPage').submit();
					}
				}
			});
			</c:if>
			<c:if test="${notificationBO.actionPage eq 'view' || notificationBO.actionPage eq 'edit' && notificationBO.notificationSent}">
			$('#viewStudyNotificationListPage').submit();
			</c:if>
			<c:if test="${notificationBO.actionPage eq 'resend' && not notificationBO.notificationSent}">
			$('#viewStudyNotificationListPage').submit();
			</c:if>
			<c:if test="${notificationBO.actionPage eq 'resend' && notificationBO.notificationSent}">
			bootbox.confirm({
				closeButton: false,
				message: 'You are about to leave the page and any unsaved changes will be lost. Are you sure you want to proceed?',
				buttons: {
					'cancel': {
						label: 'Cancel',
					},
					'confirm': {
						label: 'OK',
					},
				},
				callback: function (result) {
					if (result) {
						$('#viewStudyNotificationListPage').submit();
					}
				}
			});
			</c:if>
		});

		parentInterval();

		function parentInterval() {
			let timeOutInterval = setInterval(function () {
				idleTime += 1;
				if (idleTime > 3) {
					if ($('#notificationText').val() !== '') {
						<c:if test="${notificationBO.actionPage ne 'view'}">
					autoSaveNotificationPage('auto');
					</c:if>
					<c:if test="${notificationBO.actionPage eq 'view'}">
						clearInterval(timeOutInterval);
						// keepAlive();
						timeOutFunction();
					</c:if>
					}
				}
			}, 228000);
		}

		$(this).mousemove(function (e) {
			idleTime = 0;
		});
		$(this).keypress(function (e) {
			idleTime = 0;
		});

		var timeOutInterval;

		function timeOutFunction() {
			$('#timeOutModal').modal('show');
			let i = 14;
			timeOutInterval = setInterval(function () {
				if (i === 0) {
					$('#timeOutMessage').html(
							'<span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in '
							+ i + ' minutes');
					if ($('#timeOutModal').hasClass('show')) {
						var a = document.createElement('a');
						a.href = "/fdahpStudyDesigner/sessionOut.do";
						document.body.appendChild(a).click();
					}
					clearInterval(timeOutInterval);
				} else {
					if (i === 1) {
						$('#timeOutMessage').html(
								'<span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in 1 minute');
					} else {
						$('#timeOutMessage').html(
								'<span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in '
								+ i + ' minutes');
					}
					idleTime = 0;
					i -= 1;
				}
			}, 60000);
		}

		$(document).click(function (e) {
			if ($(e.target).closest('#timeOutModal').length) {
				clearInterval(timeOutInterval);
				$('#timeOutMessage').html(
						'<span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in 15 minutes');
				parentInterval();
			}
		});

		// pop message after 15 minutes
		if ($('#isAutoSaved').val() === 'true') {
			$('#myModal').modal('show');
			let i = 1;
			let j = 14;
			let lastSavedInterval = setInterval(function () {
				if ((i === 15) || (j === 0)) {
					$('#autoSavedMessage').html('<div class="blue_text">Last saved was ' + i
							+ ' minutes ago</div><div class="grey_txt"><span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in <span class="bold_txt"> '
							+ j + ' minutes</span></div>').css("fontSize", "15px");
					if ($('#myModal').hasClass('show')) {
						var a = document.createElement('a');
						a.href = "/fdahpStudyDesigner/sessionOut.do";
						document.body.appendChild(a).click();
					}
					clearInterval(lastSavedInterval);
				} else {
					if ((i === 1) || (j === 14)) {
						$('#autoSavedMessage').html(
								'<div class="blue_text">Last saved was 1 minute ago</div><div class="grey_txt"><span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in <span class="bold_txt"> 14 minutes</span></div>').css(
								"fontSize", "15px");
					} else if ((i === 14) || (j === 1)) {
						$('#autoSavedMessage').html(
								'<div class="blue_text">Last saved was 14 minutes ago</div><div class="grey_txt"><span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in <span class="bold_txt"> 1 minute</span></div>')
					} else {
						$('#autoSavedMessage').html('<div class="blue_text">Last saved was ' + i
								+ ' minutes ago</div><div class="grey_txt"><span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in <span class="bold_txt"> '
								+ j + ' minutes</span></div>').css("fontSize", "15px");
					}
					idleTime = 0;
					i += 1;
					j -= 1;
				}
			}, 60000);
		}
	});

	function autoSaveNotificationPage(mode) {
		$('#datetimepicker, #timepicker1').removeAttr('required', 'required');
		$('#buttonType').val('save');
		if (isFromValid('#studyNotificationFormId')) {
			if ($('#inlineRadio2').prop('checked')) {
				bootbox.confirm("Are you sure you want to send this notification immediately?",
						function (result) {
							if (result) {
								$('#saveStudyId').prop('disabled', true);
								$('#loader').show();
								if (mode === 'auto') {
									$("#isAutoSaved").val('true');
								} else {
									$("#isAutoSaved").val('false');
								}
								$('#studyNotificationFormId').submit();
								$('.eigthNotification').find('span').remove();
							}
						});
			} else if ($('#inlineRadio1').prop('checked')) {
				if (validateTime()) {
					$('#saveStudyId').prop('disabled', true);
					$('#loader').show();
					if (mode === 'auto') {
						$("#isAutoSaved").val('true');
					} else {
						$("#isAutoSaved").val('false');
					}
					$('#studyNotificationFormId').submit();
					$('.eigthNotification').find('span').remove();
				}
			}
		} else {
			$('#saveStudyId').prop('disabled', false);
		}
	}

	function validateTime() {
		var dt = $('#datetimepicker').val();
		var tm = $('#timepicker1').val();
		var valid = true;
		if (dt && tm) {
			dt = moment(dt, "MM/DD/YYYY").toDate();
			thisDate = moment($('.timepicker').val(), "h:mm a").toDate();
			dt.setHours(thisDate.getHours());
			dt.setMinutes(thisDate.getMinutes());
			$('.timepicker').parent().removeClass('has-error has-danger').find(
					'.help-block.with-errors').empty();
			if (dt < serverDateTime()) {
				$('.timepicker').parent().addClass('has-error has-danger').find(
						'.help-block.with-errors').empty().append(
						$("<ul><li> </li></ul>").attr("class", "list-unstyled").text(
								"Please select a time that has not already passed for the current date."));
				valid = false;
			}
		}
		return valid;
	}

	$('#studyLanguage').on('change', function () {
		let currLang = $('#studyLanguage').val();
		$('[name="language"]').val(currLang);
		refreshAndFetchLanguageData($('#studyLanguage').val());
	})

	function refreshAndFetchLanguageData(language) {
		$.ajax({
			url: '/fdahpStudyDesigner/adminStudies/getStudyNotification.do?_S=${param._S}',
			type: "GET",
			data: {
				language: language,
				notificationId: $('[name="notificationId"]').val(),
				notificationText: $('#notificationText').val(),
				actionType: $('[name="actionType"]').val(),
				chkRefreshflag: 'y',
				appId: $('[name="appId"]').val()
			},
			success: function (data) {
				let htmlData = document.createElement('html');
				htmlData.innerHTML = data;
				if (language !== 'en') {
					updateCompletionTicks(htmlData);
					$('.tit_wrapper').text($('#mlName', htmlData).val());
					$('[name="currentDateTime"]').prop('disabled', true);
					$('#deleteStudyNotification').addClass('cursor-none');
					if ($('#inlineRadio1').prop('checked') === true) {
						$('#datetimepicker').prop('disabled', true);
						$('#timepicker1').prop('disabled', true);
					}
					$('#notificationText').val($('#mlNotificationText', htmlData).val());
				} else {
					updateCompletionTicksForEnglish();
					$('.tit_wrapper').text($('#customStudyName', htmlData).val());
					$('[name="currentDateTime"], #deleteStudyNotification').prop('disabled', false);
					if ($('#inlineRadio1').prop('checked') === true) {
						$('#datetimepicker').prop('disabled', false);
						$('#timepicker1').prop('disabled', false);
					}
					$('#deleteStudyNotification').removeClass('cursor-none');
					$('#notificationText').val($('#notificationText', htmlData).val());
				}
			}
		});
	}
</script>