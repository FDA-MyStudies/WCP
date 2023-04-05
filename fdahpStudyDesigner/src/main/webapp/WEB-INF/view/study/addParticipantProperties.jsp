<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<head>
    <meta charset="UTF-8">
    <style>
      .checkBoxForm {

      }

      #infoModel {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        overflow: hidden;
        background: rgba(0, 0, 0, 0.5);
        z-index: 999999;
        display: none;
      }

      #infoModel > div {
        display: table;
        width: 500px;
        padding: 20px;
        background: #fff;
        color: #000;
        margin: 5% auto 0;
      }

      .not-allowed {
        cursor: not-allowed;
        pointer-events: none;
        opacity: 0.5;
      }

      #infoModel .infoOkBtn {
        color: #fff;
        background: #007cba;
        border-color: #007cba !important;
        padding: 4px 20px;
        border: none;
        border-radius: 4px;
        margin-top: 30px;
        float: right;
      }

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

      #timeOutModal .modal-dialog, #learnMyModal .modal-dialog .flr_modal {
        position: relative !important;
        right: -14px !important;
        margin-top: 6% !important;
      }

      #myModal .modal-dialog, #learnMyModal .modal-dialog .flr_modal {
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
</head>

<!-- Start right Content here -->
<div class="col-sm-10 col-rc white-bg p-none">
    <!--  Start top tab section-->
    <div class="right-content-head">
        <div class="text-right">
            <!-- <div class="black-md-f dis-line pull-left line34">
                <span class="mr-xs" onclick="goToBackPage(this);"><img
                        src="../images/icons/back-b.png" /></span> Add Property
            </div> -->

            <div class="black-md-f text-uppercase dis-line pull-left line34">
				<span class="pr-sm cur-pointer" onclick="goToBackPage(this);"><img
                        src="../images/icons/back-b.png" class="pr-md"/></span>
                <c:if test="${actionType eq 'add'}">Add Property</c:if>
                <c:if test="${actionType eq 'edit'}">Edit Property</c:if>
                <c:if test="${actionType eq 'view'}">View Property</c:if>
            </div>

            <c:if test="${studyBo.multiLanguageFlag eq true and actionType != 'add'}">
                <div class="dis-line form-group mb-none mr-sm" style="width: 150px;">
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

            <c:if test="${studyBo.multiLanguageFlag eq true and actionType == 'add'}">
                <div class="dis-line form-group mb-none mr-sm" style="width: 150px;">
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

            <div class="dis-line form-group mb-none mr-sm">
                <button type="button" class="btn btn-default gray-btn"
                        onclick="goToBackPage(this);">Cancel
                </button>
            </div>

            <c:if test="${actionType ne 'view'}">
                <div class="dis-line form-group mb-none mr-sm">
                    <button type="button" class="btn btn-default gray-btn" id="saveId">Save</button>
                </div>

                <div class="dis-line form-group mb-none">
                    <button type="button" class="btn btn-primary blue-btn" id="doneId">Done</button>
                </div>
            </c:if>
        </div>
    </div>
    <!--  End  top tab section-->

    <!--  Start body tab section -->
    <div class="right-content-body pt-none pl-none pr-none">


        <div class="tab-content pl-xlg pr-xlg">

            <!-- Step-level Attributes-->
            <div id="sla" class="tab-pane fade in active show mt-xlg">
                <form:form
                        action="/fdahpStudyDesigner/adminStudies/saveorUpdateParticipantProperties.do?_S=${param._S}"
                        name="participantPropertiesFormId" id="participantPropertiesFormId"
                        modelAttribute="participantProperties" method="post">
                    <input type="hidden" name="${csrf.parameterName}"
                           value="${csrf.token}">
                    <input type="hidden" id="mlName" value="${studyLanguageBO.name}"/>
                    <input type="hidden" id="customStudyName"
                           value="${fn:escapeXml(studyBo.name)}"/>
                    <form:hidden path="id"/>
                    <form:hidden path="anchorDateId"/>
                    <input type="hidden" id="actionType" name="actionType"
                           value="${fn:escapeXml(actionType)}">
                    <input type="hidden" id="actionButtonType" name="actionButtonType"
                           value="">
                    <input type="hidden" id="isAutoSaved" value="${isAutoSaved}"
                           name="isAutoSaved"/>
                    <input type="hidden" id="preShortTitleId"
                           value="${fn:escapeXml(participantProperties.shortTitle)}"/>
                    <input type="hidden" id="participantId"
                           value="${fn:escapeXml(participantProperties.id)}"/>

                    <form:hidden id="short-title-id" path="shortTitle"/>
                    <form:hidden id="prePropertyType" path="propertyType"/>
                    <form:hidden id="preUseAsAnchorDate" path="useAsAnchorDate"/>
                    <form:hidden id="preRefreshedValue" path="refreshedValue"/>

                    <div class="row">
                        <div class="form-group col-md-4 pl-none mt-lg">
                            <div class="gray-xs-f mb-xs">
                                Short Title or Key(1 to 50 characters)<span
                                    class="requiredStar">*</span>
                                <span class="ml-xs sprites_v3 filled-tooltip"
                                      data-toggle="tooltip"
                                      title="A human readable step identifier and must be unique for a participant property.Note that this field cannot be edited once the study is launched."></span>
                            </div>
                            <div class="form-group mb-none">
                                    <%-- <form:input autofocus="autofocus" type="text" custAttType="cust"
                                        class="form-control" name="shortTitle" id="shortTitleId"
                                        path="shortTitle" maxlength="50" required="required" /> --%>
                                <input autofocus="autofocus" type="text" custAttType="cust"
                                       <c:if test="${actionType eq 'edit' && participantProperties.live eq 1}">disabled</c:if>
                                       class="form-control" id="shortTitleId"
                                       value="${fn:escapeXml(participantProperties.shortTitle)}"
                                       maxlength="50"
                                       required="required"/>
                                <div class="help-block with-errors red-txt"></div>
                            </div>
                        </div>
                        <div class="clearfix"></div>
                    </div>
                    <div class="row">
                        <div class="col-md-4 pl-none mt-lg">
                            <div class="gray-xs-f mb-xs">
                                Property Name(1 to 300 characters)<span
                                    class="requiredStar">*</span>
                                <span class="ml-xs sprites_v3 filled-tooltip"
                                      data-toggle="tooltip"
                                      title="A descriptive name for the property."></span>
                            </div>
                            <div class="form-group mb-none">
                                <form:input type="text" class="form-control" name="propertyName"
                                            id="propertyName" path="propertyName"
                                            required="required"
                                            maxlength="300"/>
                                <div class="help-block with-errors red-txt"></div>
                            </div>
                        </div>
                        <div class="clearfix"></div>
                    </div>
                    <div>
                        <div class="gray-xs-f mb-xs mt-lg">
                            Property Type<span class="requiredStar">*</span> <span
                                class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip"
                                title="This determines whether the value of the property per participant will be made available to the system before the participant enrolls into the study using the mobile app. As a general guideline, pre-enrollment properties must be used for information that is generic or non-sensitive in nature. Note that this field cannot be edited once the study is launched."></span>
                        </div>
                        <div class="form-group">
							<span class="radio radio-info radio-inline p-45 pl-1 propTypeCls">
								<%-- <form:radiobutton class="enrollment-cls" id="inlineRadio1"
									value="PreEnrollment" name="typeOfProperty" path="propertyType"
									required="required" /> --%> <input type="radio"
                                                                       class="enrollment-cls"
                                                                       id="inlineRadio1"
                                                                       value="PreEnrollment"
                                                                       name="typeOfProperty"
                                ${participantProperties.propertyType=='PreEnrollment' ?'checked':''}
                                                                       <c:if test="${actionType eq 'edit' && participantProperties.live eq 1}">disabled</c:if>
                                                                       required="required"/> <label
                                    for="inlineRadio1">Pre-Enrollment</label>
							</span> <span class="radio radio-inline propTypeCls"> <%-- <form:radiobutton class="enrollment-cls" id="inlineRadio2"
									required="required" value="PostEnrollment"
									name="typeOfProperty" path="propertyType" /> --%> <input
                                type="radio" class="enrollment-cls" id="inlineRadio2"
                                required="required" value="PostEnrollment" name="typeOfProperty"
                            ${participantProperties.propertyType=='PostEnrollment' ?'checked':''}
                                <c:if test="${actionType eq 'edit' && participantProperties.live eq 1}">disabled</c:if> />
								<label for="inlineRadio2">Post-Enrollment</label>
							</span>
                            <div class="help-block with-errors red-txt"></div>
                        </div>
                    </div>
                    <div class="clearfix"></div>
                    <div class="row">
                        <div class="col-md-4 col-lg-3 p-none">
                            <div class="gray-xs-f mb-xs">
                                Data Type<span class="requiredStar">*</span> <span
                                    class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip"
                                    title="The type of data that is used to hold the value of Participant property. Note this field cannot be edited once the study is launched."></span>
                            </div>
                            <div
                                    class="form-group
							<c:if test="${actionType eq 'edit' && participantProperties.live eq 1}">cursor-none</c:if>">
                                <form:select id="dataType" class="selectpicker required-attr"
                                             title="Select" required="required" path="dataType">
                                    <form:options items="${dataType}"/>
                                </form:select>
                                <div class="help-block with-errors red-txt"></div>
                            </div>
                        </div>

                        <div class="col-md-4 col-lg-3 mt-xlg mb-lg useAsAnchorDate"
                             style="display: none;">
                                <%-- <form:checkbox id="inlineCheckbox1" value=""
                                    path="useAsAnchorDate" />
                                <label for="inlineCheckbox1"> Use as Anchor Date </label> --%>

                            <span class="checkbox checkbox-inline"> <input
                                    type="checkbox" id="inlineCheckbox1" value="" name="anchorDate"
                                ${participantProperties.useAsAnchorDate ?'checked':''}
                                    <c:if test="${actionType eq 'edit' && participantProperties.live eq 1}">disabled</c:if> />
								<label for="inlineCheckbox1"> Use as Anchor Date </label>
							</span>
                        </div>
                    </div>
                    <div class="clearfix"></div>
                    <div class="col-md-4 col-lg-3 p-none">
                        <div class="gray-xs-f mb-xs">
                            Data Source<span class="requiredStar">*</span> <span
                                class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip"
                                title="The source/mechanism used to get the value of the Participant Property per participant, into the system."></span>
                        </div>
                        <div class="form-group">
                            <form:select id="dataSource" class="selectpicker required-attr"
                                         title="Select" required="required" path="dataSource">
                                <form:options items="${dataSource}"/>
                            </form:select>
                            <div class="help-block with-errors red-txt"></div>
                        </div>
                    </div>
                    <div class="clearfix"></div>
                    <%-- <div>
                        <div class="gray-xs-f mb-xs">
                            Data to be populated at Enrollment?<span class="requiredStar">*</span>
                            <span class="ml-xs sprites_v3 filled-tooltip"
                                data-toggle="tooltip"
                                title="The Tooltip plugin is small pop-up box that appears when the user moves."></span>
                        </div>
                        <div class="form-group">
                            <span
                                class="radio radio-info radio-inline p-45
                            <c:if test="${actionType eq 'edit' && participantProperties.live eq 1}">cursor-none</c:if>">
                                <form:radiobutton id="inlineRadio3" value="true"
                                    class="required-attr" name="radioInline2"
                                    path="populatedAtEnrollment" required="required" /> <label
                                for="inlineRadio3">Yes</label>
                            </span> <span
                                class="radio radio-inline
                            <c:if test="${actionType eq 'edit' && participantProperties.live eq 1}">cursor-none</c:if>">
                                <form:radiobutton id="inlineRadio4" value="false"
                                    class="required-attr" name="radioInline2"
                                    path="populatedAtEnrollment" required="required" /> <label
                                for="inlineRadio4">No</label>
                            </span>
                            <div class="help-block with-errors red-txt"></div>
                        </div>
                    </div> --%>
                    <div class="clearfix"></div>

                    <div class="mt-lg mb-lg refresh-value" style="display: none;">
                            <%-- <form:checkbox id="inlineCheckbox2" path="refreshedValue" />
                            <label class="checkBoxForm" for="inlineCheckbox2"> Query
                                for Refreshed Value </label> --%>

                        <span class="checkbox checkbox-inline"> <input
                                type="checkbox" id="inlineCheckbox2"
                            ${participantProperties.refreshedValue ?'checked':''} /> <label
                                class="checkBoxForm" for="inlineCheckbox2"> Query for
								Refreshed Value </label>
						</span>
                    </div>
                    <div class="clearfix"></div>
                </form:form>
            </div>
            <div class="dis-line form-group mb-none mr-sm">
                <c:choose>
                    <c:when test="${participantProperties.live eq 1}">
                        <div class="form-group mr-sm" style="white-space: normal;">
                            <button type="button" class="btn btn-default red-btn-action"
                                    id="deactivateId"
                                    onclick="deactivateParticipantProperty(${participantProperties.id});">
                                Deactivate
                            </button>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="form-group mr-sm" style="white-space: normal;">
                            <button type="button"
                                    class="btn btn-default red-btn-action
							<c:if test="${empty participantProperties.id}">cursor-none</c:if>"
                                    id="deleteId"
                                    onclick="deleteParticipantProperty(${participantProperties.id});">
                                Delete
                            </button>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>
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
                <div id="timeOutMessage" class="text-right blue_text"><span class="timerPos"><img
                        src="../images/timer2.png"/></span>Your session expires in 15 minutes
                </div>
            </div>
        </div>
    </div>
</div>
<!-- Modal -->
<div id="infoModel">
    <div>
        <div class="tt">Please Note. You are trying to deactivate a
            Participant Property which is already in use. Please delete the
            Activities or Resources which uses this Participant Property as
            Anchor based scheduling and try again
        </div>
        <button class="infoOkBtn">OK</button>
    </div>

</div>

<form:form
        action="/fdahpStudyDesigner/adminStudies/deactivateParticipantProperty.do?_S=${param._S}"
        name="deactivateParticipantPropertyForm"
        id="deactivateParticipantPropertyFormId" method="post">
    <input type="hidden" name="participantPropertyId"
           id="participantPropertyId" value=""/>
</form:form>
<script>
  <c:if test="${actionType == 'view'}">
  $('#participantPropertiesFormId input[type="text"]').prop('disabled', true);
  $('#participantPropertiesFormId input[type="checkbox"]').prop('disabled',
      true);
  $('select').prop('disabled', true);
  $('#inlineRadio1,#inlineRadio2,#inlineRadio3,#inlineRadio4,#inlineRadio5')
  .prop('disabled', true);
  $('#deactivateId,#deleteId').prop('disabled', true);
  $('#studyLanguage').prop('disabled', false);
  </c:if>

  var dataVal = "";
  var propType = "";
  var idleTime = 0;
  $(document).ready(function () {
    $('#doneId').click(function (e) {
      $('.required-attr').prop('required', true);
      if (isFromValid("#participantPropertiesFormId")) {
        $("#actionButtonType").val('done');
        $("#short-title-id").val($("#shortTitleId").val());
        $("#prePropertyType").val($('input[name="typeOfProperty"]:checked').val());

        if ($('#inlineCheckbox1').is(':checked')) {
          $("#preUseAsAnchorDate").val(true);
        } else {
          $("#preUseAsAnchorDate").val(false);
        }

        if ($('#inlineCheckbox2').is(':checked')) {
          $("#preRefreshedValue").val(true);
        } else {
          $("#preRefreshedValue").val(false);
        }
        $('#participantPropertiesFormId').submit();
      }
    });
    $(".menuNav li.active").removeClass('active');
    $(".menuNav li.sixth").addClass('active');
    dataVal = $("#dataType").val();
    if (dataVal === 'date') {
      $(".useAsAnchorDate").show();
    } else {
      $(".useAsAnchorDate").hide();
      $("#inlineCheckbox1").prop('checked', false);
    }
    propType = $('.enrollment-cls:checked').val();

    let currLang = $('#studyLanguage').val();
    if (currLang !== undefined && currLang !== null && currLang !== '' && currLang !== 'en') {
      $('#currentLanguage').val(currLang);
      refreshAndFetchLanguageData(currLang);
    }

    if (propType === 'PostEnrollment' && dataVal === 'date') {
      if ($('#inlineCheckbox1').is(':checked')) {
        $('.refresh-value').show();
      } else {
        $('.refresh-value').hide();
        $("#inlineCheckbox2").prop('checked', false);
      }
    } else {
      $('.refresh-value').hide();
      $("#inlineCheckbox2").prop('checked', false);
    }
    if (${participantProperties.isUsedInQuestionnaire || participantProperties.isUsedInActiveTask || participantProperties.isUsedInResource}) {
      $('#deactivateId').mouseenter(function () {
        $("#infoModel").css("display", "block");
        $("#infoModel .tt").text(
            "Please Note. You are trying to deactivate a Participant Property which is already in use. Please delete the Activities or Resources which uses this Participant Property as Anchor based scheduling and try again");
        //$("#infoModel").removeClass("hide")
      });

      $('#deleteId').mouseenter(function () {
        $("#infoModel").css("display", "block");
        $("#infoModel .tt").text(
            "Please Note. You are trying to delete a Participant Property which is already in use. Please delete the Activities or Resources which uses this Participant Property as Anchor based scheduling and try again");
      });
    }
    /* if(


    ${actionType eq 'edit' && participantProperties.live eq 1}){
			$("#shortTitleId").prop('disabled', true);
			$("#inlineRadio1").prop('disabled', true);
			$("#inlineRadio2").prop('disabled', true);
			$("#inlineRadio3").prop('disabled', true);
			$("#inlineRadio4").prop('disabled', true);
			$("#inlineCheckbox1").prop('disabled', true);
		}  */
    /* if(


    ${actionType eq 'edit' && participantProperties.live eq 1}){
			$("#shortTitleId").addClass('not-allowed')
			$("#inlineRadio1").addClass('not-allowed')
			$("#inlineRadio2").addClass('not-allowed')
			$("#inlineRadio3").prop('readonly', true);
			$("#inlineRadio4").prop('readonly', true);
			$("#inlineCheckbox1").prop('readonly', true);
		} */

    if (${actionType eq 'edit' && participantProperties.live eq 1}) {
      $("#shortTitleId").attr("tabindex", "-1");
    }
  });

  $(".infoOkBtn").click(function () {
    $("#infoModel").css("display", "none");
  });

  $('#inlineCheckbox1').change(function () {
    propType = $('.enrollment-cls:checked').val();
    dataVal = $("#dataType").val();
    if (propType === 'PostEnrollment' && dataVal === 'date') {
      if (this.checked) {
        $('.refresh-value').show();
      } else {
        $('.refresh-value').hide();
        $("#inlineCheckbox2").prop('checked', false);
      }
    } else {
      $('.refresh-value').hide();
      $("#inlineCheckbox2").prop('checked', false);
    }
  });

  $('.propTypeCls').change(function () {
    propType = $('.enrollment-cls:checked').val();
    dataVal = $("#dataType").val();
    if (propType === 'PostEnrollment' && dataVal === 'date') {
      if ($('#inlineCheckbox1').is(':checked')) {
        $('.refresh-value').show();
      } else {
        $('.refresh-value').hide();
        $("#inlineCheckbox2").prop('checked', false);
      }
    } else {
      $('.refresh-value').hide();
      $("#inlineCheckbox2").prop('checked', false);
    }
  });

  $('#dataType').change(function () {
    propType = $('.enrollment-cls:checked').val();
    dataVal = $("#dataType").val();
    if (propType === 'PostEnrollment' && dataVal === 'date') {
      if ($('#inlineCheckbox1').is(':checked')) {
        $('.refresh-value').show();
      } else {
        $('.refresh-value').hide();
        $("#inlineCheckbox2").prop('checked', false);
      }
    } else {
      $('.refresh-value').hide();
      $("#inlineCheckbox2").prop('checked', false);
    }
  });

  $("#dataType").change(function () {
    dataVal = $("#dataType").val();
    if (dataVal === 'date') {
      $(".useAsAnchorDate").show();
    } else {
      $(".useAsAnchorDate").hide();
      $("#inlineCheckbox1").prop('checked', false);
    }
  });

  $("#shortTitleId").blur(function () {
    validateShortTitle('', function (val) {
    });
  });

  $("#saveId").click(function () {
    autoSaveParticipantPropertyPage('manual');
  });

  parentInterval();

  function parentInterval() {
    let timeOutInterval = setInterval(function () {
      idleTime += 1;
      if (idleTime > 3) { // 5 minutes
        <c:if test="${actionType ne 'view'}">
      autoSaveParticipantPropertyPage('auto');
      </c:if>
      <c:if test="${actionType eq 'view'}">
        clearInterval(timeOutInterval);
        // keepAlive();
        timeOutFunction();
      </c:if>
    }
    }, 228000); // 5 minutes
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

  function autoSaveParticipantPropertyPage(mode) {
    $('.required-attr').prop('required', false);
    if (isFromValid("#participantPropertiesFormId")) {
      $("#actionButtonType").val('save');
      $("#short-title-id").val($("#shortTitleId").val());
      $("#prePropertyType").val($('input[name="typeOfProperty"]:checked').val());

      if ($('#inlineCheckbox1').is(':checked')) {
        $("#preUseAsAnchorDate").val(true);
      } else {
        $("#preUseAsAnchorDate").val(false);
      }

      if ($('#inlineCheckbox2').is(':checked')) {
        $("#preRefreshedValue").val(true);
      } else {
        $("#preRefreshedValue").val(false);
      }
      if (mode === 'auto') {
        $("#isAutoSaved").val('true');
      } else {
        $("#isAutoSaved").val('false');
      }
      $('#participantPropertiesFormId').submit();
      showSucMsg("Content saved as draft.");
    }
  }

  function validateShortTitle(item, callback) {
    var shortTitle = $("#shortTitleId").val();
    var thisAttr = $("#shortTitleId");
    var existedKey = $("#preShortTitleId").val();
    if (shortTitle != null && shortTitle != ''
        && typeof shortTitle != 'undefined') {
      $(thisAttr).parent().removeClass("has-danger").removeClass(
          "has-error");
      $(thisAttr).parent().find(".help-block").empty();
      if (existedKey != shortTitle) {
        $
        .ajax({
          url: "/fdahpStudyDesigner/adminStudies/validateParticipantPropertyKey.do?_S=${param._S}",
          type: "POST",
          datatype: "json",
          data: {
            shortTitle: shortTitle
          },
          beforeSend: function (xhr, settings) {
            xhr.setRequestHeader("X-CSRF-TOKEN",
                "${_csrf.token}");
          },
          success: function getResponse(data) {
            var message = data.message;

            if ('SUCCESS' != message) {
              $(thisAttr).validator('validate');
              $(thisAttr).parent().removeClass(
                  "has-danger").removeClass(
                  "has-error");
              $(thisAttr).parent().find(".help-block")
              .empty();
              callback(true);
            } else {
              $(thisAttr).val('');
              $(thisAttr).parent().addClass("has-danger")
              .addClass("has-error");
              $(thisAttr).parent().find(".help-block")
              .empty();
              $(thisAttr)
              .parent()
              .find(".help-block")
              .append(
                  "<ul class='list-unstyled'><li>'"
                  + shortTitle
                  + "' has already been used in the past.</li></ul>");
              callback(false);
            }
          },
          global: false
        });
      } else {
        callback(true);
        $(thisAttr).parent().removeClass("has-danger").removeClass(
            "has-error");
        $(thisAttr).parent().find(".help-block").empty();
      }
    } else {
      callback(false);
    }
  }

  function goToBackPage(item) {
    $(item).prop('disabled', true);
    <c:if test="${actionType ne 'view'}">
    bootbox
    .confirm({
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
          var a = document.createElement('a');
          let lang = ($('#studyLanguage').val() !== undefined) ? $('#studyLanguage').val() : '';
          a.href = "/fdahpStudyDesigner/adminStudies/participantPropertiesPage.do?_S=${param._S}&language="
              + lang;
          document.body.appendChild(a).click();
        } else {
          $(item).prop('disabled', false);
        }
      }
    });
    </c:if>
    <c:if test="${actionType eq 'view'}">
    var a = document.createElement('a');
    a.href = "/fdahpStudyDesigner/adminStudies/participantPropertiesPage.do?_S=${param._S}";
    document.body.appendChild(a).click();
    </c:if>
  }

  function deactivateParticipantProperty(participantPropertyId) {
    bootbox.confirm("Are you sure you want to deactivate this Participant Property?",
        function (result) {
          if (result) {
            $("#participantPropertyId").val(participantPropertyId);
            $("#deactivateParticipantPropertyFormId").submit();
            showSucMsg('Participant Property successfully deactivated.');
          }
        });
  }

  function deleteParticipantProperty(participantPropertyId) {
    bootbox.confirm({
      message: "Are you sure you want to delete this Participant Property item? This item will no longer appear on the mobile app or admin portal. Response data already gathered against this item, if any, will still be available on the response database.",
      buttons: {
        confirm: {
          label: 'Yes',
        },
        cancel: {
          label: 'No',
        }
      },
      callback: function (result) {
        if (result) {
          if (participantPropertyId != null && participantPropertyId != ''
              && typeof participantPropertyId != 'undefined') {
            document.deactivateParticipantPropertyForm.action = "/fdahpStudyDesigner/adminStudies/deleteParticipantProperty.do?_S=${param._S}";
            $("#participantPropertyId").val(participantPropertyId);
            document.deactivateParticipantPropertyForm.submit();
          }
        }
      }
    });
  }

  $('#studyLanguage').on('change', function () {
    let currLang = $('#studyLanguage').val();
    $('#currentLanguage').val(currLang);
    refreshAndFetchLanguageData($('#studyLanguage').val());
  })

  function refreshAndFetchLanguageData(language) {
    $.ajax({
      url: '/fdahpStudyDesigner/adminStudies/editParticipantProperties.do?_S=${param._S}',
      type: "GET",
      data: {
        language: language,
        participantPropertyId: $('#participantId').val()
      },
      success: function (data) {
        let htmlData = document.createElement('html');
        htmlData.innerHTML = data;
        if ($('#actionType').val() === 'edit') {
          if (language !== 'en') {
            updateCompletionTicks(htmlData);
            $('.tit_wrapper').text($('#mlName', htmlData).val());
            $('#shortTitleId, #propertyName, #inlineCheckbox1, #inlineCheckbox2, [name="typeOfProperty"], #dataType, #dataSource, #deleteId').attr(
                'disabled', true);
            $('[data-id="dataSource"], [data-id="dataType"]').css(
                'background-color',
                '#eee').css('opacity', '1').addClass('cursor-none');
            $('#saveId, #doneId, #deactivateId').addClass('cursor-none');
          } else {
            updateCompletionTicksForEnglish();
            $('.tit_wrapper').text($('#customStudyName', htmlData).val());
            $('#shortTitleId, #propertyName, #inlineCheckbox1, #inlineCheckbox2, [name="typeOfProperty"], #dataType, #dataSource, #deleteId').attr(
                'disabled', false);
            $('[data-id="dataSource"], [data-id="dataType"], #saveId, #doneId, #deactivateId').removeAttr(
                'style').removeClass('cursor-none');
          }
        }
      }
    });
  }

  $('#propertyName').on('keyup', function () {
    $(this).parent().find(".help-block").empty();
    $('.form-control').parent().removeClass("has-danger").removeClass("has-error");
  });

</script>