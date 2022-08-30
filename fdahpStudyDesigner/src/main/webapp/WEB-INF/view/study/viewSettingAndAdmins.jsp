<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- ============================================================== -->
<!-- Start right Content here -->
<!-- ============================================================== -->
<head>
    <meta charset="UTF-8">
    <style>
      table.dataTable thead th:last-child {
        width: 100px !important;
      }

      .text-normal > button > .filter-option{
        text-transform: inherit !important;
      }

      .langSpecific{
        position: relative;
      }

      .langSpecific > button::before{
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

      .langSpecific > button{
        padding-left: 30px;
      }
      #myAutoModal .modal-dialog, #learnMyModal .modal-dialog .flr_modal{
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
       .close{
       background-image: none;
       }
 /*      
.checkbox label.addadmin_check::before {
    margin-left: 0px !important;
    margin-top: -13px !important;
    }
.checkbox label.addadmin_check::after {
    margin-left: 0px !important;
    margin-top: -13px !important;
    }
*/

.checkbox label {
    display: inline !important;
}
.checkbox label::before { top: 1px !important;} 

.checkbox label::after {
    display: inline !important;
    top: 1px !important;
    }
    
.close {
    margin-right: 5px;
}

.radio input[type="radio"] {
    opacity: 0 !important;
}

.checkbox input[type="checkbox"]:disabled+label {
    opacity: 0.99 !important;
}
    </style>
</head>
<div class="col-sm-10 col-rc white-bg p-none" id="settingId">
<form:form action="/fdahpStudyDesigner/sessionOut.do" id="backToLoginPage" name="backToLoginPage" method="post"></form:form>
    <form:form
            action="/fdahpStudyDesigner/adminStudies/removeSelectedLanguage.do?_S=${param._S}"
            id="removeLangFormId">
        <input type="hidden" id="deletedLanguage" name="deletedLanguage"/>
        <input type="hidden" id="newSelLanguages" name="newLanguages">
        <input type="hidden" name="studyId" value="${studyBo.id}">
        <input type="hidden" id="mlFlag" name="mlFlag"/>
    </form:form>
    <form:form
            action="/fdahpStudyDesigner/adminStudies/saveOrUpdateSettingAndAdmins.do?_S=${param._S}"
            data-toggle="validator" role="form" id="settingfoFormId" method="post"
            autocomplete="off">
        <input type="hidden" name="buttonText" id="buttonText">
        <input type="hidden" id="settingsstudyId" name="id"
               value="${studyBo.id}">

        <input type="hidden" id="userIds" name="userIds">
        <input type="hidden" id="newLanguages" name="newLanguages">
        <input type="hidden" id="deletedLanguages" name="deletedLanguages">
        <input type="hidden" id="currentLanguage" name="currentLanguage" value="${currLanguage}">
        <input type="hidden" id="mlName" value="${studyLanguageBO.name}"/>
        <input type="hidden" id="customStudyName" value="${fn:escapeXml(studyBo.name)}"/>
        <input type="hidden" id="alertText" value="${studyLanguageBO.allowRejoinText}">
        <input type="hidden" id="allowRejoinText" value="${studyBo.allowRejoinText}">
        <input type="hidden" id="permissions" name="permissions">
        <input type="hidden" id="projectLead" name="projectLead">
         <input type="hidden" id="isAutoSaved" value="${isAutoSaved}" name="isAutoSaved"/>
        <select id="langDeletableMap" style="display: none">
            <c:forEach items="${langDeletableMap}" var="langEntry">
                <option id='lang_${langEntry.key}' value="${langEntry.value}"></option>
            </c:forEach>
        </select>
        <!-- Start top tab section-->
        <div class="right-content-head">
            <div class="text-right">
                <div class="black-md-f text-uppercase dis-line pull-left line34">
                    SETTINGS AND ADMINS
                    <c:set var="isLive">${_S}isLive</c:set>
                        ${not empty  sessionScope[isLive]?'<span class="eye-inc ml-sm vertical-align-text-top"></span>':''}</div>

                <c:if test="${studyBo.multiLanguageFlag eq true}">
                    <div class="dis-line form-group mb-none mr-sm" style="width: 150px;">
                        <select
                                class="selectpicker aq-select aq-select-form studyLanguage langSpecific"
                                id="studyLanguage" name="studyLanguage" title="Select">
                            <option value="en" ${((currLanguage eq null) or (currLanguage eq '') or (currLanguage eq 'en')) ?'selected':''}>
                                English
                            </option>
                            <c:forEach items="${selectedLanguages}" var="language">
                                <option value="${language.key}" ${currLanguage eq language.key ?'selected':''}>${language.value}</option>
                            </c:forEach>
                        </select>
                    </div>
                </c:if>

                <div class="dis-line form-group mb-none mr-sm">
                    <button type="button" class="btn btn-default gray-btn cancelBut"
                            id="cancelId">Cancel
                    </button>
                </div>
                <c:if
                        test="${(empty permission) && (sessionObject.role ne 'Org-level Admin')}">
                    <div class="dis-line form-group mb-none mr-sm">
                        <button type="button" class="btn btn-default gray-btn" id="saveId">Save
                        </button>
                    </div>

                    <div class="dis-line form-group mb-none">
                        <button type="button" class="btn btn-primary blue-btn"
                                id="completedId">Mark as Completed
                        </button>
                    </div>
                </c:if>
            </div>
        </div>
        <!-- End top tab section-->


        <!-- Start body tab section -->
        <div class="right-content-body col-xs-12">
            <!-- Start Section-->
            <div class="col-md-12 p-none">
                <div class="gray-xs-f mb-sm">
                    Platform(s) Supported<span class="requiredStar"> *</span> <span
                        class="sprites_v3 filled-tooltip" id="infoIconId"></span>
                </div>
                <div class="form-group">
					<span class="checkbox checkbox-inline p-45 pl-2"> <input
                            class="platformClass" type="checkbox" id="inlineCheckbox1"
                            name="platform" value="I"
                            <c:if test="${fn:contains(studyBo.platform,'I')}">checked</c:if>
                            <c:if test="${not empty studyBo.liveStudyBo && fn:contains(studyBo.liveStudyBo.platform,'I')}">disabled</c:if>
                            data-error="Please check these box if you want to proceed."
                            required> <label for="inlineCheckbox1"> iOS </label>
					</span> <span class="checkbox checkbox-inline"> <input
                        type="checkbox" class="platformClass" id="inlineCheckbox2"
                        name="platform" value="A"
                        <c:if test="${fn:contains(studyBo.platform,'A')}">checked</c:if>
                        <c:if test="${not empty studyBo.liveStudyBo && fn:contains(studyBo.liveStudyBo.platform,'A')}">disabled</c:if>
                        data-error="Please check these box if you want to proceed."
                        required> <label for="inlineCheckbox2"> Android </label>
					</span>
                    <div class="help-block with-errors red-txt"></div>
                </div>
            </div>
            <!-- End Section-->

            <!-- Start Section-->
            <div class="col-md-12 p-none">
                <div class="gray-xs-f mb-sm">
                    Enable multi-language support for this study?
                    <span>
            		    <span data-toggle="tooltip" data-placement="top"
                              title="Select this option to enable multiple languages for this study other than English."
                              class="filled-tooltip"></span>
                    </span>
                </div>

                <div class="form-group">
            		<span class="radio radio-info radio-inline p-45 pl-2">
						<input type="radio" id="mlYes" value="Yes" name="multiLanguageFlag"
                               <c:if test="${studyBo.multiLanguageFlag eq true}">checked</c:if>
                        />
						<label for="mlYes">Yes</label>
            		</span>
                    <span class="radio radio-inline">
						<input type="radio" id="mlNo" value="No" name="multiLanguageFlag"
                               <c:if test="${studyBo.multiLanguageFlag eq false or studyBo.multiLanguageFlag eq null}">checked</c:if>
                        />
            			<label for="mlNo">No</label>
            		</span>
                    <div class="help-block with-errors red-txt"></div>
                </div>
            </div>
            <!-- End Section-->

            <div id="langSelect" style="display: none">
                <div class="mt-md study-list mb-md addHide" >
                    <select
                            class="selectpicker col-md-6 pl-none pr-none aq-select aq-select-form text-normal"
                            title="- Select and add languages -" id="multiple">
                        <c:forEach items="${supportedLanguages}" var="lang">
                            <option class="langOption" value="${lang.key}"
                                    id="${lang.key}">${lang.value}</option>
                        </c:forEach>
                        <c:if test="${supportedLanguages.size() eq 0}">
                            <option style="text-align: center; color: #000000" disabled>- All items
                                are already selected -
                            </option>
                        </c:if>
                    </select>
                    <span class="study-addbtn changeView" id="addLangBtn">+</span>
                </div>
                <!-- Selected Language items -->
                <div class="study-selected mt-md mb-md" id="selectedLanguages">
                    <c:forEach items="${selectedLanguages}" var="stdLang">
                        <input type="hidden" class="stdCls" id="${stdLang.key}"
                               value="${stdLang.key}">
                        <span id="span-${stdLang.key}">${stdLang.value}
                            <span
                                id="innerSpan-${stdLang.key}" class="ablue removeLang changeView"
                                onclick="removeLang(this.id, '${stdLang.value}', '')"> X&nbsp;&nbsp;
                            </span>
                        </span>
                    </c:forEach>
                </div>
            </div>
           


            <!-- Start Section-->
            <div class="col-md-12 p-none">
                <div class="gray-xs-f mb-sm">
                    Allow participants to enroll?<span class="requiredStar"> *</span>
                </div>

                <div class="form-group">
					<span class="radio radio-info radio-inline p-45 pl-2"> <input
                            type="radio" id="inlineRadio1" value="Yes"
                            name="enrollingParticipants"
                            <c:if test="${studyBo.enrollingParticipants eq 'Yes'}">checked</c:if>
                            required> <label for="inlineRadio1">Yes</label>
					</span> <span class="radio radio-inline"> <input type="radio"
                                                                     id="inlineRadio2" value="No"
                                                                     name="enrollingParticipants"
                                                                     <c:if test="${studyBo.enrollingParticipants eq null}">checked</c:if>
                                                                     <c:if test="${studyBo.enrollingParticipants eq 'No'}">checked</c:if>
                                                                     required> <label
                        for="inlineRadio2">No</label>
					</span>
                    <div class="help-block with-errors red-txt"></div>
                </div>
            </div>
            <!-- End Section-->

            <!-- Start Section-->
            <div class="col-md-12 p-none">
                <div class="gray-xs-f mb-sm">
                    Use Enrollment Date as Anchor Date in study activity scheduling?<span
                        class="requiredStar"> *</span><span><span
                        data-toggle="tooltip" data-placement="top"
                        title="Select this option to distribute a questionnaire, active task or resource, N number of days after participant enrollment. N is configured in the schedule settings of that study activity or resource."
                        class="filled-tooltip"></span></span>
                </div>

                <div class="form-group">
					<span class="radio radio-info radio-inline p-45 pl-2"> <input
                            type="radio" id="inlineRadio11" value="Yes"
                            name="enrollmentdateAsAnchordate"
                            <c:if test="${studyBo.enrollmentdateAsAnchordate}">checked</c:if>
                            required> <label for="inlineRadio11">Yes</label>
					</span> <span class="radio radio-inline"> <input type="radio"
                                                                     id="inlineRadio22" value="No"
                                                                     name="enrollmentdateAsAnchordate"
                    ${isAnchorForEnrollmentLive?'disabled':''}
                                                                     <c:if test="${studyBo.enrollmentdateAsAnchordate eq false}">checked</c:if>
                                                                     required> <label
                        for="inlineRadio22">No</label>
					</span>
                    <div class="help-block with-errors red-txt"></div>
                </div>
            </div>
            <!-- End Section-->

            <!-- Start Section-->
            <div class="col-md-12 p-none">
                <div class="gray-xs-f mb-sm">
                    Retain participant data when they leave a study? <span
                        class="requiredStar">*</span>
                </div>

                <div class="form-group">
					<span class="radio radio-info radio-inline p-45 pl-2"> <input
                            type="radio" id="inlineRadio3" value="Yes"
                            name="retainParticipant"
                            <c:if test="${studyBo.retainParticipant eq 'Yes'}">checked</c:if>
                            required> <label for="inlineRadio3">Yes</label>
					</span> <span class="radio radio-inline p-45"> <input type="radio"
                                                                          id="inlineRadio4"
                                                                          value="No"
                                                                          name="retainParticipant"
                                                                          <c:if test="${studyBo.retainParticipant eq 'No'}">checked</c:if>
                                                                          required> <label
                        for="inlineRadio4">No</label>
					</span> <span class="radio radio-inline"> <input type="radio"
                                                                     id="inlineRadio5" value="All"
                                                                     name="retainParticipant"
                                                                     <c:if test="${studyBo.retainParticipant eq 'All'}">checked</c:if>
                                                                     required> <label
                        for="inlineRadio5">Allow
							participant to choose to have their data retained or deleted</label>
					</span>
                    <div class="help-block with-errors red-txt"></div>
                </div>
            </div>
            <!-- End Section-->

            <!-- Start Section-->
            <div class="col-md-12 p-none">
                <div class="gray-xs-f mb-sm">
                    Allow users to rejoin a Study once they leave it? <span
                        class="requiredStar">*</span>
                </div>

                <div class="form-group">
					<span class="radio radio-info radio-inline p-45 pl-2"> <input
                            type="radio" class="rejoin_radio" id="inlineRadio6" value="Yes"
                            name="allowRejoin"
                            <c:if test="${studyBo.allowRejoin eq null}">checked</c:if>
                            <c:if test="${studyBo.allowRejoin eq 'Yes'}">checked</c:if>
                            required> <label for="inlineRadio6">Yes</label>
					</span> <span class="radio radio-inline"> <input type="radio"
                                                                     class="rejoin_radio"
                                                                     id="inlineRadio7" value="No"
                                                                     name="allowRejoin"
                                                                     <c:if test="${studyBo.allowRejoin eq 'No'}">checked</c:if>
                                                                     required> <label
                        for="inlineRadio7">No</label>
					</span>
                    <div class="help-block with-errors red-txt"></div>
                </div>

                <div class="gray-xs-f ">
                    Alert text for participants attempting to leave a study <span><span
                        data-toggle="tooltip" data-placement="top"
                        title="Enter a message that should be shown to participants when they attempt to leave the study indicating whether or not they have the option to re-join the study."
                        class="filled-tooltip"></span></span>
                </div>

                <div class="col-md-7 p-none mt-sm rejointextclassYes"
                     style="display: none;">
                    <div class="form-group m-none elaborateClass">
						<textarea class="form-control" maxlength="250" rows="5"
                                  id="rejoin_comment_yes"
                                  data-error="Please enter plain text of up to 250 characters max."
                                  placeholder="Please enter text that the user should see when they leave a study to let them know whether they can or cannot Rejoin the study">${fn:escapeXml(studyBo.allowRejoinText)}</textarea>
                        <div>
                            <small>(250 characters max)</small>
                        </div>
                        <div class="help-block with-errors red-txt"></div>
                    </div>
                </div>
                <div class="col-md-7 p-none mt-sm rejointextclassNo"
                     style="display: none;">
                    <div class="form-group m-none elaborateClass">
						<textarea class="form-control langSpecific" maxlength="250" rows="5"
                                  id="rejoin_comment_no"
                                  data-error="Please enter plain text of up to 250 characters max."
                                  placeholder="Please enter text that the user should see when they leave a study to let them know whether they can or cannot Rejoin the study">${fn:escapeXml(studyBo.allowRejoinText)}</textarea>
                        <div>
                            <small>(250 characters max)</small>
                        </div>
                        <div class="help-block with-errors red-txt"></div>
                    </div>
                </div>
            </div>
            <!-- End Section-->

            <!-- Start Section-->
            <c:if test="${fn:contains(permissions,7)}">
                <div>
                    <div class="black-md-f text-uppercase line34">MANAGE LIST OF
                        ADMINS ASSIGNED TO THE STUDY
                    </div>
                    <c:if test="${empty permission && fn:contains(permissions,5)}">
                        <div class="dis-line form-group mb-none">
                            <button type="button" id="addAdminButton"
                                    class="btn btn-primary blue-btn mb-sm mt-xs"
                                    onclick="addAdmin();">+ Add Admin
                            </button>
                        </div>
                    </c:if>

                    <table id="studyAdminsTable" class="display bor-none"
                           cellspacing="0" width="85%">
                        <thead>
                        <tr>
                            <th>&nbsp;&nbsp;&nbsp;Admins</th>
                            <th class="text-center">View</th>
                            <th class="text-center">View & Edit</th>
                            <th class="text-center">Project Lead</th>
                            <th></th>

                        </tr>
                        </thead>
                        <tbody id="studyAdminId">
                        <c:forEach items="${studyPermissionList}" var="perm">
                            <tr id="studyAdminRowId${perm.userId}" class="studyAdminRowCls"
                                studyUserId="${perm.userId}">
                                <td><span class="dis-ellipsis"
                                          title="${fn:escapeXml(perm.userFullName)}">${perm.userFullName}</span>
                                </td>
                                <td><span class="radio radio-info  radio-inline">
											<input type="radio" id="inlineRadio1${perm.userId}"
                                                   class="radcls" value="0"
                                                   name="view${perm.userId}"
                                                   <c:if test="${not perm.viewPermission}">checked</c:if>>
											<label for="inlineRadio1${perm.userId}"></label>
									</span></td>
                                <td align="center"><span
                                        class="radio radio-info radio-inline"> <input
                                        type="radio" id="inlineRadio2${perm.userId}" class="radcls"
                                        value="1" name="view${perm.userId}"
                                        <c:if test="${perm.viewPermission}">checked</c:if>> <label
                                        for="inlineRadio2${perm.userId}"></label>
									</span></td>
                                <td align="center"><span
                                        class="radio radio-info radio-inline"> <input
                                        type="radio" id="inlineRadio3${perm.userId}"
                                        class="radcls leadCls" value="" name="projectLead"
                                        <c:if test="${perm.projectLead eq 1}">checked</c:if>>
											<label for="inlineRadio3${perm.userId}"></label>
									</span></td>
                                <td align="center"><span
                                        class="sprites_icon copy delete <c:if test="${not empty permission || !fn:contains(permissions,5)}"> cursor-none </c:if>"
                                        onclick="removeUser(${perm.userId})" data-toggle="tooltip"
                                        data-placement="top" title="Delete"></span></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <!-- </div> -->
                </div>
            </c:if>
            <!-- End Section-->


        </div>
        <!-- End body tab section -->

    </form:form>

    <div class="modal fade" id="myAutoModal" role="dialog">
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

<c:if test="${empty permission && fn:contains(permissions,5)}">
    <div class="col-sm-10 col-rc white-bg p-none" id="adminsId">
        <div class="right-content-head">
            <div class="text-right">
                <div class="black-md-f dis-line pull-left line34">
					<span class="pr-sm"><a href="javascript:void(0)"
                                           onclick="cancelAddAdmin();"><img
                            src="/fdahpStudyDesigner/images/icons/back-b.png"/></a></span>Add Admins
                </div>

                <div class="dis-line form-group mb-none mr-sm">
                    <button type="button" class="btn btn-default gray-btn"
                            onclick="cancelAddAdmin();">Cancel
                    </button>
                </div>
                <div class="dis-line form-group mb-none mr-sm">
                    <button type="button" class="btn btn-primary blue-btn"
                            id="addAdminsToStudyId" onclick="addAdminsToStudy()">Add
                    </button>
                </div>
            </div>
        </div>
        <div class="right-content-body col-xs-12">
            <!-- <div class="right-content-body pt-none pb-none"> -->
            <div>
                <table id="userListTable" class="display bor-none tbl_rightalign"
                       cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th></th>
                        <th>USERS<span class="sort"></span></th>
                        <th>E-MAIL ADDRESS</th>
                        <th style="width: 100px !important">ROLE</th>

                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${userList}" var="user">
                        <tr id="user${user.userId}" class="checkCount">
                            <td><span
                                    class="checkbox checkbox-inline">
										<input type="checkbox" class="addAdminCheckbox"
                                               id="inlineCheckboxNew${user.userId}" name="case"
                                               value="${fn:escapeXml(user.userFullName)}"
                                               userId="${user.userId}"> <label class="addadmin_check"
                                    for="inlineCheckboxNew${user.userId}"></label>
								</span></td>
                            <td><span class="dis-ellipsis"
                                      title="${fn:escapeXml(user.userFullName)}">${user.userFullName}</span>
                            </td>
                            <td><span class="dis-ellipsis"
                                      title="${fn:escapeXml(user.userEmail)}">${user.userEmail}</span>
                            </td>
                            <td>${user.roleName}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <!-- </div> -->
            </div>
        </div>
    </div>
</c:if>
<!-- Modal -->
<div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog modal-lg">
        <!-- Modal content-->
        <div class="modal-content">

            <div class="cust-hdr pt-lg">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h5 class="modal-title pl-lg">
                    <b>Platform and Feature Support</b>
                </h5>
            </div>
            <div class="modal-body pt-xs pb-lg pl-xlg pr-xlg">
                <div>
                    <div>
                        <ul class="no-disc">
                            <li><strong>1. Platform Support: </strong><br/>
                                <ul class="no-disc">
                                    <li>Note that once the study is Launched, platform support
                                        cannot be revoked. However, adding support for a platform
                                        not
                                        previously selected will still be possible.
                                    </li>
                                </ul>
                            </li>
                            <li>&nbsp;</li>
                            <li><strong>2. Feature Support on iOS and Android:</strong><br/>

                                <ul class="no-disc">
                                    <li>Given below is a list of features currently NOT
                                        available for Android as compared to iOS. Please note the
                                        same
                                        in your creation of study questionnaires and active tasks.
                                    </li>
                                    <li>i. Activetasks: Activetask with type Tower Of Hanoi,
                                        Spatial Span Memory
                                    </li>
                                </ul>
                            </li>

                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
var idleTime = 0;
  $(document).ready(function () {
	  $('#loader').hide();
    let currLang = $('#studyLanguage').val();
    if (currLang !== undefined && currLang !== null && currLang !== '' && currLang !== 'en') {
      $('#currentLanguage').val(currLang);
      refreshAndFetchLanguageData(currLang);
    }

    let disableMLFlag = false;
    $('#langDeletableMap option').each(function() {
      let id = this.id.split('_')[1];
      $('#innerSpan-'+id).addClass('cursor-none');
      disableMLFlag = true;
    })
    if (disableMLFlag===true){
      if ($('#mlYes').prop('checked')===true) {
        $('#mlNo').prop('disabled', true);
      }
    }

    $(document).on('click', '.removeLangNew', function () {
      removeLang(this.id, 'new', '');
    })

    <c:if test="${empty permission && fn:contains(permissions,5)}">

    <c:if test="${user eq 'logout_login_user'}">
    bootbox.alert({
      closeButton: false,
      message: 'Your user account details have been updated. Please sign in again to continue using the portal.',
      callback: function (result) {
        var a = document.createElement('a');
        a.href = "/fdahpStudyDesigner/sessionOut.do";
        document.body.appendChild(a).click();
      }
    });
    </c:if>

   $('[data-toggle="tooltip"]').tooltip(); 

    $('#adminsId').hide();

    $('.studyAdminRowCls').each(function () {
      var userId = $(this).attr('studyUserId');
      $('#user' + userId).removeClass('checkCount').hide();
    });

    $('#userListTable').DataTable({
      "columnDefs": [
        {"width": "100px", "targets": 3}
      ],
      "paging": false,
      "emptyTable": "No data available",
      "info": false,
      "lengthChange": true,
      "searching": false,
    });

    $('.addAdminCheckbox').on('click', function () {
      var count = 0;
      $('[name=case]:checked').each(function () {
        count++;
      });
      if (count > 0) {
        $('#addAdminsToStudyId').prop('disabled', false);
      } else {
        $('#addAdminsToStudyId').prop('disabled', true);
      }
    });
    </c:if>

    table = $('#studyAdminsTable').DataTable({
      "paging": false,
      "aoColumns": [
        {"width": '40%', "bSortable": false},
        {"width": '10%', "bSortable": false},
        {"width": '10%', "bSortable": false},
        {"width": '10%', "bSortable": false},
        {"width": '10%', "bSortable": false}
      ],
      "info": false,
      "lengthChange": true,
      "searching": false,
    });

    $(".menuNav li.active").removeClass('active');
    $(".menuNav li.second").addClass('active');
    checkRadioRequired();
    $(".rejoin_radio").click(function () {
      checkRadioRequired();
    })
    <c:if test="${(not empty permission) || (sessionObject.role eq 'Org-level Admin')}">
    $('#settingfoFormId input,textarea,select').prop('disabled', true);
    $('#settingfoFormId').find('.elaborateClass').addClass('linkDis');
    $('#studyLanguage').removeAttr('disabled');
    </c:if>

    <c:if test="${!fn:contains(permissions,5)}">
    $('.radcls').prop('disabled', true);
    </c:if>

    $("#completedId").on('click', function (e) {
      var rowCount = 0;
      if (isFromValid("#settingfoFormId")) {
        rowCount = $('.leadCls').length;
        if (rowCount != 0) {
          if ($("#studyAdminsTable .leadCls:checked").length > 0) {
            $('#completedId').prop('disabled', true);
            platformTypeValidation('completed');
          } else {
            bootbox.alert({
              closeButton: false,
              message: 'Please select one of the admin as a project lead',
            });
          }
        } else {
          $('#completedId').prop('disabled', true);
          platformTypeValidation('completed');
        }
      }
    });

    $("#saveId").click(function () {
    	saveSettingAndAdminsPage('manual');
    });

    var allowRejoin = '${studyBo.allowRejoin}';
    if (allowRejoin != "") {
      if (allowRejoin == 'Yes') {
        $('.rejointextclassYes').show();
        $('#rejoin_comment_no').empty();
        ;
        $('.rejointextclassNo').hide();
      } else {
        $('.rejointextclassNo').show();
        $('.rejointextclassYes').hide();
        $('#rejoin_comment_yes').empty();
        ;
      }
    }
    $("[data-toggle=tooltip]").tooltip();
    $("#infoIconId").hover(function () {
      $('#myModal').modal('show');
    });
    
    setInterval(function () {
        idleTime += 1;
        if (idleTime > 3) {
                <c:if test="${permission ne 'view'}">
                saveSettingAndAdminsPage('auto');
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
          if ($('#timeOutModal').hasClass('show')) {
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
          $('#myAutoModal').modal('show');
          let i = 1;
          let j = 14;
          let lastSavedInterval = setInterval(function () {
              if ((i === 15) || (j === 0)) {
              $('#autoSavedMessage').html('<div class="blue_text">Last saved was ' + i + ' minutes ago</div><div class="grey_txt"><span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in <span class="bold_txt"> ' + j +' minutes</span></div>').css("fontSize", "15px");
                  if ($('#myAutoModal').hasClass('show')) {
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

  function checkRadioRequired() {
    var rejoinRadioVal = $('input[name=allowRejoin]:checked').val();
    if (rejoinRadioVal == 'Yes') {
      $('.rejointextclassYes').show();
      $('#rejoin_comment_yes').attr("required", "required");
      $('#rejoin_comment_no').removeAttr("required");
      $('.rejointextclassNo').hide();
    } else {
      $('.rejointextclassNo').show();
      $('#rejoin_comment_no').attr("required", "required");
      $('#rejoin_comment_yes').removeAttr("required");
      $('.rejointextclassYes').hide();
    }
  }

  function setAllowRejoinText() {
    var allowRejoin = $('input[name=allowRejoin]:checked').val();
    if (allowRejoin) {
      if (allowRejoin == 'Yes') {
        $('#rejoin_comment_yes').attr("name", "allowRejoinText");
        $('#rejoin_comment_no').removeAttr("name", "allowRejoinText");
      } else {
        $('#rejoin_comment_no').attr("name", "allowRejoinText");
        $('#rejoin_comment_yes').removeAttr("name", "allowRejoinText");
      }
    }
  }
  
  function saveSettingAndAdminsPage(mode){
	  platformTypeValidation('save', mode);
  }

  function platformTypeValidation(buttonText, mode) {
    var platformNames = '';
    $("input:checkbox[name=platform]:checked").each(function () {
      platformNames = platformNames + $(this).val();
    });
    var liveStudy = "${studyBo.liveStudyBo}";
    if (liveStudy) {
      var platform = "${studyBo.liveStudyBo.platform}";
      if (platform.includes('A')) {
        platformNames = '';
      }
    }
    if (platformNames != '' && platformNames.includes('A')) {
      $('.actBut').prop('disabled', true);
      $("body").addClass("loading");
      $.ajax({
        url: "/fdahpStudyDesigner/adminStudies/studyPlatformValidationforActiveTask.do?_S=${param._S}",
        type: "POST",
        datatype: "json",
        data: {
          studyId: $('#settingsstudyId').val(),
          "${_csrf.parameterName}": "${_csrf.token}",
        },
        success: function platformValid(data, status) {
          var message = data.message;
          var errorMessage = data.errorMessage;
          $("body").removeClass("loading");
          if (message == "SUCCESS") {
            $('#completedId').removeAttr('disabled');
            bootbox.alert(errorMessage);
          } else {
            if (mode === 'auto') {
                $("#isAutoSaved").val('true');
            }
            else{
            $("#isAutoSaved").val('false');
            }
            submitButton(buttonText);
          }
        },
        error: function status(data, status) {
          $("body").removeClass("loading");
        },
        complete: function () {
          $('.actBut').removeAttr('disabled');
        },
        global: false
      });
    } else {
       if (mode === 'auto') {
           $("#isAutoSaved").val('true');
       }
        else{
        $("#isAutoSaved").val('false');
        }
      submitButton(buttonText);
    }
  }

  function submitButton(buttonText) {
    setAllowRejoinText();
    admins() //Pradyumn
    var isAnchorForEnrollmentDraft = '${isAnchorForEnrollmentDraft}';
    if ($('#mlYes').prop('checked') === true && $('#selectedLanguages').children().length === 0) {
      bootbox.alert({
        closeButton: false,
        message: 'Please select atleast one language for enabling multi language support for this study.',
      });
      $('#completedId').removeAttr('disabled');
      return false;
    }
    if (buttonText === 'save') {
      $('#settingfoFormId').validator('destroy');
      $("#inlineCheckbox1,#inlineCheckbox2").prop('disabled', false);
      $("#buttonText").val('save');
      $("#settingfoFormId").submit();
    } else {
      var retainParticipant = $('input[name=retainParticipant]:checked').val();
      var enrollmentdateAsAnchordate = $('input[name=enrollmentdateAsAnchordate]:checked').val();
      if (retainParticipant) {
        if (retainParticipant == 'All')
          retainParticipant = 'Participant Choice';
        bootbox.confirm({
          closeButton: false,
          message: 'You have selected "' + retainParticipant
              + '" for the retention of participant response data when they leave a study.'
              + ' Your Consent content must be worded to convey the same.'
              + ' Click OK to proceed with completing this section or Cancel if you wish to make changes.',
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
// 			        	$("#inlineCheckbox1,#inlineCheckbox2").prop('disabled', false);
// 			        	$("#buttonText").val('completed');
// 	                    $("#settingfoFormId").submit();
              //phase2a anchor
              showWarningForAnchor(isAnchorForEnrollmentDraft, enrollmentdateAsAnchordate);
              //phase 2a anchor
            } else {
              $('#completedId').removeAttr('disabled');
            }
          }
        });
      } else {
        $("#inlineCheckbox1,#inlineCheckbox2").prop('disabled', false);
        $("#buttonText").val('completed');
        $("#settingfoFormId").submit();
      }
    }
  }

  function admins() {
    var userIds = "";
    var permissions = "";
    var projectLead = "";
    $('.studyAdminRowCls').each(function () {
      var userId = $(this).attr('studyUserId');
      if (userIds == "") {
        userIds = userId;
      } else {
        userIds += "," + userId;
      }
      var permission = $(this).find('input[type=radio]:checked').val();
      if (permissions == "") {
        permissions = permission;
      } else {
        permissions += "," + permission;
      }
      if ($(this).find('#inlineRadio3' + userId).prop('checked')) {
        projectLead = userId;
      }
    });
    $('#userIds').val(userIds);
    $('#permissions').val(permissions);
    $('#projectLead').val(projectLead);
  }

  // Adding selected study items
  var newSelectedLang = '';
  $(".study-addbtn").click(function () {
    let selLang = $("#langSelect").find('option:selected').val();
    if (selLang === '') {
      return false;
    }
    newSelectedLang += selLang + ',';
    $('#newLanguages').val(newSelectedLang);
    $(".study-list .bootstrap-select .dropdown-menu ul.dropdown-menu li.selected").hide();
    $(".study-list .bootstrap-select .dropdown-menu ul.dropdown-menu li").each(function () {
      if ($(this).text() === "- All items are already selected -") {
        $(this).hide();
      }
    });

    $('#multiple :selected').each(function (i, sel) {
      let selVal = $(sel).val();
      if (selVal !== undefined && selVal != null && selVal !== '') {
        let selTxt = DOMPurify.sanitize($(sel).text());
        let newDiv = "<input type='hidden' class='stdCls' id=" + "'" + selVal + "' value='" + selVal
            + "'>"
            + "<span id='span-" + selVal + "'>" + selTxt
            + "<span id='innerSpan-" + selVal + "' class='ablue removeLangNew changeView'"
            + "> X&nbsp;&nbsp;</span></span>";
        $('.study-selected').append(newDiv);
      }
    });

    $("#multiple").selectpicker('val', '');
    let tot_items = $(".study-list .bootstrap-select .dropdown-menu ul.dropdown-menu li").length;
    let count = $(".study-list .bootstrap-select .dropdown-menu ul.dropdown-menu li[style]").length;
    if (count === tot_items) {
      $(".study-list .bootstrap-select .dropdown-menu ul.dropdown-menu").empty().append(
          $("<li> </li>").attr("class", "text-center").text("- All items are already selected -"));
    }

  });

  if ($('#mlYes').prop('checked')===true) {
    $("#langSelect").show();
  } else {
    $("#langSelect").hide();
  }

  $('input[name="multiLanguageFlag"]').change(function (e) {
    if (this.value === 'No') {
      let message = '';
      let languages = '';
      let allLang = $('#selectedLanguages').find('span.removeLang');
      let size = allLang.length;
      allLang.each(function (index){
        let langName = $(this).parent().text().split(' ')[0];
        if (message==='') {
          message=langName;
        } else {
          if (index === size-1) {
            message += (' and '+langName);
          } else {
            message += (', '+langName);
          }
        }
        languages === '' ? languages=this.id.split('-')[1] : languages += (','+this.id.split('-')[1]);
      })
      if (languages!=='') {
        removeLang('', message, languages);
      }
      $("#langSelect").slideUp('slow');
    } else {
      $("#langSelect").slideDown('slow');
    }
  });

  <c:if test="${empty permission && fn:contains(permissions,5)}">

  function addAdmin() {
    var userListTableRowCount = $('.checkCount').length;
    if (userListTableRowCount == 0) {
      bootbox.alert({
        closeButton: false,
        message: 'There are currently no other admin users available to add to this study.',
      });
    } else {
      $('#settingId').hide();
      $('#adminsId').show();
      $('#addAdminsToStudyId').prop('disabled', true);
    }
  }

  function cancelAddAdmin() {
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
          $('#settingId').show();
          $('#adminsId').hide();
          $('[name=case]:checked').each(function () {
            $(this).prop('checked', false);
          });
        }
      }
    });
  }

  function addAdminsToStudy() {
    $('#addAdminsToStudyId').attr('disabled', true);
    $('[name=case]:checked').each(function () {
      var name = escapeXml($(this).val());
      var userId = parseInt($(this).attr('userId'));
      $('#user' + userId).removeClass('checkCount').hide();
      $('#settingId').show();
      $(this).prop('checked', false);
      $('#adminsId').hide();
      var domStr = '';
      domStr = domStr + '<tr id="studyAdminRowId' + userId
          + '" role="row" class="studyAdminRowCls" studyUserId="' + userId + '">';
      domStr = domStr + '<td><span class="dis-ellipsis" title="' + DOMPurify.sanitize(name) + '">'
          + DOMPurify.sanitize(name) + '</span></td>';
      domStr = domStr + '<td><span class="radio radio-info radio-inline">' +
          '<input type="radio" id="inlineRadio1' + userId + '" value="0" name="view' + userId
          + '" checked>' +
          '<label for="inlineRadio1' + userId + '"></label>' +
          '</span></td>';
      domStr = domStr + '<td align="center"><span class="radio radio-info radio-inline">' +
          '<input type="radio" id="inlineRadio2' + userId + '" value="1" name="view' + userId + '">'
          +
          '<label for="inlineRadio2' + userId + '"></label>' +
          '</span></td>';
      domStr = domStr + '<td align="center"><span class="radio radio-info radio-inline">' +
          '<input type="radio" id="inlineRadio3' + userId + '" class="leadCls" name="projectLead">'
          +
          '<label for="inlineRadio3' + userId + '"></label>' +
          '</span></td>';
      domStr = domStr
          + '<td align="center"><span class="sprites_icon copy delete" onclick="removeUser('
          + userId + ')" data-toggle="tooltip" data-placement="top" title="Delete"></span></td>';
      domStr = domStr + '</tr>';
      $('#studyAdminId').append(domStr);
      
      $('.dataTables_empty').remove();
    });
    $('#addAdminsToStudyId').attr('disabled', false);
  }

  function removeUser(userId) {
    var userId = userId;
    var count = 0;
    $('.studyAdminRowCls').each(function () {
      count++;
    });
    if (count == 1) {
      $('[data-toggle="tooltip"]').tooltip('dispose');
   
      table.clear().draw();
     
    }
    $('[data-toggle="tooltip"]').tooltip('dispose');
    $('#studyAdminRowId' + userId).remove();

    $('[data-toggle="tooltip"]').tooltip('dispose');
    
    $('#user' + userId).addClass('checkCount').show();
  }

  function escapeXml(unsafe) {
    return unsafe.replace(/[<>&'"]/g, function (c) {
      switch (c) {
        case '<':
          return '&lt;';
        case '>':
          return '&gt;';
        case '&':
          return '&amp;';
        case '\'':
          return '&apos;';
        case '"':
          return '&quot;';
      }
    });
  }

  </c:if>

  function showWarningForAnchor(isAnchorForEnrollmentDraft, enrollmentdateAsAnchordate) {
    if (isAnchorForEnrollmentDraft == 'true' && enrollmentdateAsAnchordate == 'No') {
      var text = "You have chosen not to use Enrollment Date as an Anchor Date.You will need to revise the schedules of Target Activities or Resources,if any, that were set based on the Enrollment Date.Buttons: OK, Cancel.";
      bootbox.confirm({
        closeButton: false,
        message: text,
        buttons: {
          'cancel': {
            label: 'Cancel',
          },
          'confirm': {
            label: 'OK',
          },
        },
        callback: function (valid) {
          if (valid) {
            console.log(1);
            $("#inlineCheckbox1,#inlineCheckbox2").prop('disabled', false);
            $("#buttonText").val('completed');
            $("#settingfoFormId").submit();
          } else {
            console.log(2);
            $('#completedId').removeAttr('disabled');
          }
        }
      });
    } else {
      $("#inlineCheckbox1,#inlineCheckbox2").prop('disabled', false);
      $("#buttonText").val('completed');
      $("#settingfoFormId").submit();
    }
  }

  var removedLanguages = '';

  function removeLang(langObject, fullName, allLang) {
    if (fullName === 'new') {
      let text = $('#' + langObject).parent().text();
      if (text !== undefined) {
        fullName = text.split(' ')[0];
      }
    }
    let message = 'This will remove all study content created in ' + fullName;
    bootbox
    .confirm({
      closeButton: false,
      message: message,
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
          if (langObject==='') {
            $('#mlFlag').val('Y');
            $('#deletedLanguage').val(allLang);
          } else {
            let targetStr = langObject.split('-')[1];
            $('#deletedLanguage').val(targetStr);
          }
          $('#newSelLanguages').val(newSelectedLang)
          $('#removeLangFormId').submit();
        } else {
          if (langObject==='') {
            $('#mlYes').prop('checked', true).change();
          }
        }
      }
    });
  }

  $('#studyLanguage').on('change', function () {
    let currLang = $('#studyLanguage').val();
    $('#currentLanguage').val(currLang);
    refreshAndFetchLanguageData(currLang);
  })

  function refreshAndFetchLanguageData(language) {
    let allowRejoin = '${studyBo.allowRejoin}';
    $.ajax({
      url: '/fdahpStudyDesigner/adminStudies/viewSettingAndAdmins.do?_S=${param._S}',
      type: "GET",
      data: {
        language: language
      },
      success: function (data) { // html data
        if (data !== null) {
          let htmlData = document.createElement('html');
          htmlData.innerHTML = data;
          if (language !== 'en') {
            $('.tit_wrapper').text($('#mlName', htmlData).val());
            updateCompletionTicks(htmlData);
            $('select, input[type!=hidden]').each(function () {
              if (!$(this).hasClass('langSpecific')) {
                $(this).addClass('ml-disabled').attr('disabled', true);
                if (this.nodeName.toLowerCase() === 'select') {
                  let id = this.id;
                  if (id !== undefined && id !== '') {
                    $('[data-id=' + id + ']').css('background-color', '#eee');
                    $('[data-id=' + id + ']').css('opacity', '1');
                  }
                }
              }
            });
            if (allowRejoin !== '') {
              if (allowRejoin === 'Yes') {
                $('#rejoin_comment_yes').val($('input#alertText', htmlData).val().trim());
                $('#rejoin_comment_no').empty();
              } else {
                $('#rejoin_comment_no').val($('input#alertText', htmlData).val().trim());
                $('#rejoin_comment_yes').empty();
              }
            }
            $('#addAdminButton').attr('disabled', true);
            $('#selectedLanguages').css('pointer-events', 'none');
            $('#addLangBtn').css('pointer-events', 'none');
            $('.sprites_icon').css('pointer-events', 'none');

            $('[data-id="multiple"]').css('background-color', '#eee').css('opacity', '1').addClass(
                'cursor-none');
          } else {
            updateCompletionTicksForEnglish();
            $('.tit_wrapper').text($('#customStudyName', htmlData).val());
            $('select, input[type!=hidden]').each(function () {
              if (!$(this).hasClass('langSpecific')) {
                $(this).removeClass('ml-disabled').attr('disabled', false);
                if (this.nodeName.toLowerCase() === 'select') {
                  let id = this.id;
                  if (id !== undefined && id !== '') {
                    $('[data-id=' + id + ']').removeProp('background-color');
                    $('[data-id=' + id + ']').removeProp('opacity');
                  }
                }
              }
            });
            if (allowRejoin !== '') {
              if (allowRejoin === 'Yes') {
                $('#rejoin_comment_yes').val($('input#allowRejoinText', htmlData).val().trim());
                $('#rejoin_comment_no').empty();
              } else {
                $('#rejoin_comment_no').val($('input#allowRejoinText', htmlData).val().trim());
                $('#rejoin_comment_yes').empty();
              }
            }
            $('#addAdminButton').attr('disabled', false);
            $('#selectedLanguages').removeAttr('style');
            $('#addLangBtn').removeAttr('style');
            $('.sprites_icon').removeAttr('style');

            $('[data-id="multiple"]').removeAttr('style').removeClass('cursor-none');

            let disableMLFlag = false;
            $('#langDeletableMap option', htmlData).each(function() {
              let id = this.id.split('_')[1];
              $('#innerSpan-'+id).addClass('cursor-none');
              disableMLFlag = true;
            })
            if (disableMLFlag===true){
              if ($('#mlYes').prop('checked')===true) {
                $('#mlNo').prop('disabled', true);
              }
            }

              <c:if test="${not empty studyBo.liveStudyBo && fn:contains(studyBo.liveStudyBo.platform,'I')}">
              $('input[name="platform"]').prop('disabled', true);
              </c:if>
            
            <c:if test="${permission == 'view'}">
            $('#settingfoFormId input,textarea').prop('disabled', true);
            </c:if>
          }
        }
      }
    });
  }
</script>