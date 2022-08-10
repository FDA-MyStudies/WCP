<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page import="com.fdahpstudydesigner.util.SessionObject" %>

<!-- Start left Content here -->
<!-- ============================================================== -->
<div class="col-sm-2 col-lc p-none">
    <div class="left-content-container">
        <ul class="menuNav">
            <li>
                <%--                <c:choose>--%>
                <%--                    <c:when test="${studyBo.multiLanguageFlag eq true && currLanguage != null && currLanguage != '' && currLanguage != 'en'}">--%>
                <%--                        <div class="tit_wrapper" data-toggle="tooltip" data-placement="top"--%>
                <%--                             title="${fn:escapeXml(not empty studyLanguageBO.name?studyLanguageBO.name:'Study Name')}">--%>
                <%--                                ${fn:escapeXml(not empty studyLanguageBO.name?studyLanguageBO.name:'Study Name')}--%>
                <%--                        </div>--%>
                <%--                    </c:when>--%>
                <%--                    <c:otherwise>--%>
                <%--                        <div class="tit_wrapper" data-toggle="tooltip" data-placement="top"--%>
                <%--                             title="${fn:escapeXml(not empty studyBo.name?studyBo.name:'Create Study')}">--%>
                <%--                                ${fn:escapeXml(not empty studyBo.name?studyBo.name:'Create Study')}--%>
                <%--                        </div>--%>
                <%--                    </c:otherwise>--%>
                <%--                </c:choose>--%>
                <div class="tit_wrapper" data-toggle="tooltip" data-placement="top"
                     title="${fn:escapeXml(not empty studyBo.name?studyBo.name:'Create Study')}">
                    ${fn:escapeXml(not empty studyBo.name?studyBo.name:'Create Study')}
                </div>
                <input type="hidden" id="mlBasicInfo" value="${sequenceLangBO.basicInfo}"/>
                <input type="hidden" id="mlCompre" value="${sequenceLangBO.comprehensionTest}"/>
                <input type="hidden" id="mlConsentEduInfo"
                       value="${sequenceLangBO.consentEduInfo}"/>
                <input type="hidden" id="mlConsent" value="${sequenceLangBO.eConsent}"/>
                <input type="hidden" id="mlEligibility" value="${sequenceLangBO.eligibility}"/>
                <input type="hidden" id="mlOverview" value="${sequenceLangBO.overView}"/>
                <input type="hidden" id="mlSettings" value="${sequenceLangBO.settingAdmins}"/>
                <input type="hidden" id="mlActiveTask"
                       value="${sequenceLangBO.studyExcActiveTask}"/>
                <input type="hidden" id="mlQuestionnaire"
                       value="${sequenceLangBO.studyExcQuestionnaries}"/>
                <input type="hidden" id="mlParticipantProp"
                       value="${sequenceLangBO.participantProperties}"/>
                <input type="hidden" id="mlResources"
                       value="${sequenceLangBO.miscellaneousResources}"/>
                <input type="hidden" id="mlNotification"
                       value="${sequenceLangBO.miscellaneousNotification}"/>
                <%--    for english language--%>
                <input id="basicInfo" type="hidden" value="${studyBo.studySequenceBo.basicInfo}">
                <input id="settings" type="hidden" value="${studyBo.studySequenceBo.settingAdmins}">
                <input id="overview" type="hidden" value="${studyBo.studySequenceBo.overView}">
                <input id="eligibility" type="hidden"
                       value="${studyBo.studySequenceBo.eligibility}">
                <input id="consentEduInfo" type="hidden"
                       value="${studyBo.studySequenceBo.consentEduInfo}">
                <input id="comprehensionTest" type="hidden"
                       value="${studyBo.studySequenceBo.comprehensionTest}">
                <input id="eConsent" type="hidden" value="${studyBo.studySequenceBo.eConsent}">
                <input id="participantProperties" type="hidden"
                       value="${studyBo.studySequenceBo.participantProperties}">
                <input id="studyExcQuestionnaries" type="hidden"
                       value="${studyBo.studySequenceBo.studyExcQuestionnaries}">
                <input id="studyExcActiveTask" type="hidden"
                       value="${studyBo.studySequenceBo.studyExcActiveTask}">
                <input id="resources" type="hidden"
                       value="${studyBo.studySequenceBo.miscellaneousResources}">
                <input id="notificationMenu" type="hidden"
                       value="${studyBo.studySequenceBo.miscellaneousNotification}">

                <div class="mb-lg ${empty studyBo.status?'hide':''}">
					<span
                            class="study_status
	                <c:if test="${studyBo.status eq 'Pre-launch' || studyBo.status eq 'Pre-launch(Published)'}">
	                    pre-launch_txt
	                </c:if>
	                <c:if test="${studyBo.status eq 'Active'}">
	                    active_txt
	                </c:if>
	                <c:if test="${studyBo.status eq 'Paused'}">
	                    paused_txt
	                </c:if>
	                <c:if test="${studyBo.status eq 'Deactivated'}">
	                    deactivated_txt
	                </c:if>
	                ">${studyBo.status}</span>
                    <%-- 	                <span class="version">${not empty studyBo.studyVersionBo?studyBo.studyVersionBo.studyLVersion:''}</span> --%>
                    <c:set var="isLive">${_S}isLive</c:set>
                    <span class="version">${not empty  sessionScope[isLive]?studyBo.studyVersionBo.studyLVersion:''}</span>
                </div>
            </li>
            <li class="first active">Basic Information <c:if
                    test="${studyBo.studySequenceBo.basicInfo}">
                <span class="sprites-icons-2 tick pull-right mt-xs"></span>
            </c:if>
            </li>
            <li class="second commonCls">Settings and Admins <c:if
                    test="${studyBo.studySequenceBo.settingAdmins}">
                <span class="sprites-icons-2 tick pull-right mt-xs"></span>
            </c:if>
            </li>
            <li class="third commonCls">Overview <c:if
                    test="${studyBo.studySequenceBo.overView}">
                <span class="sprites-icons-2 tick pull-right mt-xs"></span>
            </c:if>
            </li>
            <li class="fourth commonCls">Eligibility <c:if
                    test="${studyBo.studySequenceBo.eligibility}">
                <span class="sprites-icons-2 tick pull-right mt-xs"></span>
            </c:if>
            </li>
            <li class="fifth commonCls">Informed Consent <c:if
                    test="${studyBo.studySequenceBo.consentEduInfo && studyBo.studySequenceBo.comprehensionTest && studyBo.studySequenceBo.eConsent}">
                <span class="sprites-icons-2 tick pull-right mt-xs"></span>
            </c:if>
            </li>
            <li class="sub fifthConsent commonCls">Consent Sections <c:if
                    test="${studyBo.studySequenceBo.consentEduInfo}">
                <span class="sprites-icons-2 tick pull-right mt-xs"></span>
            </c:if>
            </li>
            <li class="sub fifthComre commonCls">Comprehension Test <c:if
                    test="${studyBo.studySequenceBo.comprehensionTest}">
                <span class="sprites-icons-2 tick pull-right mt-xs"></span>
            </c:if>
            </li>
            <li class="sub fifthConsentReview commonCls">Review and
                E-consent Steps <c:if test="${studyBo.studySequenceBo.eConsent}">
                    <span class="sprites-icons-2 tick pull-right mt-xs"></span>
                </c:if>
            </li>
            <li class="sixth commonCls">PARTICIPANT PROPERTIES <c:if
                    test="${studyBo.studySequenceBo.participantProperties}">
                <span class="sprites-icons-2 tick pull-right mt-xs"></span>
            </c:if>
            </li>
            <li class="seventh commonCls" style="padding-left:18px;">Study Activities
                <c:if
                    test="${studyBo.studySequenceBo.studyExcQuestionnaries and studyBo.studySequenceBo.studyExcActiveTask}">
                    <span class="sprites-icons-2 tick pull-right mt-xs"></span>
                </c:if>
            </li>
            <li class="sub seventhQuestionnaires commonCls">Questionnaires <c:if
                    test="${studyBo.studySequenceBo.studyExcQuestionnaries}">
                <span class="sprites-icons-2 tick pull-right mt-xs"></span>
            </c:if>
            </li>
            <li class="sub seventhTask commonCls " style="padding-left:25px;">Active Tasks <c:if
                    test="${studyBo.studySequenceBo.studyExcActiveTask}">
                <span class="sprites-icons-2 tick pull-right mt-xs"></span>
            </c:if>
            </li>
            <%--  <li class="seventh commonCls">
                         Study Dashboard
                        <c:if test="${studyBo.studySequenceBo.eConsent}">
                            <span class="sprites-icons-2 tick pull-right mt-xs" ></span>
                        </c:if>
                    </li> --%>
            <%-- <li class="eight commonCls">
                         Miscellaneous
                        <c:if test="${studyBo.studySequenceBo.miscellaneousResources && studyBo.studySequenceBo.miscellaneousNotification}">
                            <span class="sprites-icons-2 tick pull-right mt-xs" ></span>
                        </c:if>
                    </li> --%>

            <li class=" eighthResources commonCls">Resources <c:if
                    test="${studyBo.studySequenceBo.miscellaneousResources}">
                <span class="sprites-icons-2 tick pull-right mt-xs"></span>
            </c:if>
            </li>

            <li
                    class=" eigthNotification commonCls <c:set var="isLive">${_S}isLive</c:set>${not empty  sessionScope[isLive]?'cursor-none':''}">
                Notifications
                <c:if test="${studyBo.studySequenceBo.miscellaneousNotification}">
                    <span class="sprites-icons-2 tick pull-right mt-xs"></span>
                </c:if>
            </li>
            <%-- <li class="eighthResources commonCls">
                        Resources
                    <c:if test="${studyBo.studySequenceBo.miscellaneousResources}">
                            <span class="sprites-icons-2 tick pull-right mt-xs" ></span>
                    </c:if>
                    </li>
                    <li class="eigthNotification commonCls cursor-none-without-event">
                        Notifications
                        <c:if test="${studyBo.studySequenceBo.miscellaneousNotification}">
                            <span class="sprites-icons-2 tick pull-right mt-xs" ></span>
                        </c:if>
                    </li> --%>
            <li
                    class="nine commonCls <c:set var="isLive">${_S}isLive</c:set>${not empty  sessionScope[isLive]?'cursor-none':''}">
                Checklist <c:if test="${studyBo.studySequenceBo.checkList}">
                <span class="sprites-icons-2 tick pull-right mt-xs"></span>
            </c:if>
            </li>
            <li
                    class="tenth commonCls <c:set var="isLive">${_S}isLive</c:set>${not empty  sessionScope[isLive]?'cursor-none':''}">
                Actions
            </li>
        </ul>
    </div>
</div>

<!-- End left Content here -->
<script type="text/javascript">
  $(document).ready(function () {
    $("#rowId").addClass('lc-gray-bg');
    $('#createStudyId').show();
    // Fancy Scroll Bar
    // $(".left-content").niceScroll({cursorcolor:"#95a2ab",cursorborder:"1px solid #95a2ab"});
    // $(".right-content-body").niceScroll({cursorcolor:"#d5dee3",cursorborder:"1px solid #d5dee3"});
    $("#myNavbar li.studyClass").addClass('active');
    $('[data-toggle="tooltip"]').tooltip();

    $('.cancelBut').click(function () {
      <c:if test="${empty permission}">
      $('.cancelBut').prop('disabled', true);
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
            var a = document.createElement('a');
            a.href = "/fdahpStudyDesigner/adminStudies/studyList.do";
            document.body.appendChild(a).click();
          } else {
            $('.cancelBut').prop('disabled', false);
          }
        }
      });
      </c:if>
      <c:if test="${not empty permission}">
      var a = document.createElement('a');
      a.href = "/fdahpStudyDesigner/adminStudies/studyList.do";
      document.body.appendChild(a).click();
      </c:if>
    });

    var a = document.createElement('a');
    $('.first').click(function () {
      let lang = ($('#studyLanguage').val()!==undefined)?$('#studyLanguage').val():'';
      a.href = "/fdahpStudyDesigner/adminStudies/viewBasicInfo.do?_S=${param._S}&language=" + lang;
      document.body.appendChild(a).click();
    });
    <c:if test="${not empty studyBo.studySequenceBo && studyBo.studySequenceBo.basicInfo}">
    $('.second').click(function () {
      let lang = ($('#studyLanguage').val()!==undefined)?$('#studyLanguage').val():'';
      a.href = "/fdahpStudyDesigner/adminStudies/viewSettingAndAdmins.do?_S=${param._S}&language="
          + lang;
      document.body.appendChild(a).click();
    });
    <c:if test="${studyBo.studySequenceBo.settingAdmins}">
    $('.third').click(function () {
      let lang = ($('#studyLanguage').val()!==undefined)?$('#studyLanguage').val():'';
      a.href = "/fdahpStudyDesigner/adminStudies/overviewStudyPages.do?_S=${param._S}&language="
          + lang;
      document.body.appendChild(a).click();
    });
    $('.fourth').click(function () {
      let lang = ($('#studyLanguage').val()!==undefined)?$('#studyLanguage').val():'';
      a.href = "/fdahpStudyDesigner/adminStudies/viewStudyEligibilty.do?_S=${param._S}&language="
          + lang;
      document.body.appendChild(a).click();
    });
    $('.fifth').click(function () {
      let lang = ($('#studyLanguage').val()!==undefined)?$('#studyLanguage').val():'';
      a.href = "/fdahpStudyDesigner/adminStudies/consentListPage.do?_S=${param._S}&language=" + lang
      document.body.appendChild(a).click();
    });
    $('.fifthConsent').click(function () {
      let lang = ($('#studyLanguage').val()!==undefined)?$('#studyLanguage').val():'';
      a.href = "/fdahpStudyDesigner/adminStudies/consentListPage.do?_S=${param._S}&language=" + lang
      document.body.appendChild(a).click();
    });
    $('.fifthComre').click(function () {
      let lang = ($('#studyLanguage').val()!==undefined)?$('#studyLanguage').val():'';
      a.href = "/fdahpStudyDesigner/adminStudies/comprehensionQuestionList.do?_S=${param._S}&language="
          + lang;
      document.body.appendChild(a).click();
    });
    $('.fifthConsentReview').click(function () {
      let lang = ($('#studyLanguage').val()!==undefined)?$('#studyLanguage').val():'';
      a.href = "/fdahpStudyDesigner/adminStudies/consentReview.do?_S=${param._S}&language=" + lang;
      document.body.appendChild(a).click();
    });
    $('.sixth').click(function () {
      let lang = ($('#studyLanguage').val()!==undefined)?$('#studyLanguage').val():'';
      a.href = "/fdahpStudyDesigner/adminStudies/participantPropertiesPage.do?_S=${param._S}&language="
          + lang;
      document.body.appendChild(a).click();
    });
    $('.seventh , .seventhQuestionnaires').click(function () {
      let lang = ($('#studyLanguage').val()!==undefined)?$('#studyLanguage').val():'';
      a.href = "/fdahpStudyDesigner/adminStudies/viewStudyQuestionnaires.do?_S=${param._S}&language="
          + lang;
      document.body.appendChild(a).click();

    });
    $('.seventhTask').click(function () {
      let lang = ($('#studyLanguage').val()!==undefined)?$('#studyLanguage').val():'';
      a.href = "/fdahpStudyDesigner/adminStudies/viewStudyActiveTasks.do?_S=${param._S}&language="
          + lang;
      document.body.appendChild(a).click();
    });

    $('.eight').click(function () {
      a.href = "/fdahpStudyDesigner/adminStudies/getResourceList.do?_S=${param._S}";
      document.body.appendChild(a).click();
    });
    $('.eighthResources').click(function () {
      let lang = ($('#studyLanguage').val()!==undefined)?$('#studyLanguage').val():'';
      $('.eighthResources').addClass('cursor-none');
      a.href = "/fdahpStudyDesigner/adminStudies/getResourceList.do?_S=${param._S}&language=" + lang;
      document.body.appendChild(a).click();
    });
    $('.eigthNotification').click(function () {
      $('.eigthNotification').addClass('cursor-none');
      let lang = ($('#studyLanguage').val()!==undefined)?$('#studyLanguage').val():'';
      a.href = "/fdahpStudyDesigner/adminStudies/viewStudyNotificationList.do?_S=${param._S}&language="
          + lang;
      document.body.appendChild(a).click();
    });
    $('.nine').click(function () {
      $('.nine').addClass('cursor-none');
      let lang = ($('#studyLanguage').val()!==undefined)?$('#studyLanguage').val():'';
      a.href = "/fdahpStudyDesigner/adminStudies/getChecklist.do?_S=${param._S}&language="
          + lang;
      document.body.appendChild(a).click();
    });
    $('.tenth').click(function () {
      $('.tenth').addClass('cursor-none');
      let lang = ($('#studyLanguage').val()!==undefined)?$('#studyLanguage').val():'';
      a.href = "/fdahpStudyDesigner/adminStudies/actionList.do?_S=${param._S}&language="
          + lang;
      document.body.appendChild(a).click();
    });
    </c:if>
    </c:if>
    <c:if test="${(empty studyBo.studySequenceBo) || not studyBo.studySequenceBo.basicInfo}">
    $('.commonCls').addClass('cursor-none-without-event');
    </c:if>
    <c:if test="${studyBo.studySequenceBo.basicInfo && not studyBo.studySequenceBo.settingAdmins}">
    $('.commonCls').not('.second').addClass('cursor-none-without-event');
    </c:if>
    $(window).on('load resize', function () {

      rtime1 = new Date();
      if (timeout1 === false) {
        timeout1 = true;
        setTimeout(resizeend1, delta1);
      }

    });
  });
  //Internet Explorer 6-11
  var isIE = /*@cc_on!@*/false || !!document.documentMode;

  // Edge 20+
  var isEdge = !isIE && !!window.StyleMedia;
  if (isIE || isEdge) {
    $(window).on('load resize', function () {

      rtime1 = new Date();
      if (timeout1 === false) {
        timeout1 = true;
        setTimeout(resizeend1, delta1);
      }

    });
  }
  var rtime1;
  var timeout1 = false;
  var delta1 = 200;

  function resizeend1() {
    if (new Date() - rtime1 < delta1) {
      setTimeout(resizeend1, delta1);
    } else {
      timeout1 = false;
      slideUpStudyMenu();
    }
  }

  function slideUpStudyMenu() {
    $(".slideUp.active").ScrollTo();
  }

  function updateCompletionTicks(htmlData) {
    updateTicksByClassName(htmlData, '#mlBasicInfo', '.first');
    updateTicksByClassName(htmlData, '#mlSettings', '.second');
    updateTicksByClassName(htmlData, '#mlOverview', '.third');
    updateTicksByClassName(htmlData, '#mlEligibility', '.fourth');
    updateTicksByClassName(htmlData, '#mlConsent', '.fifthConsentReview');
    updateTicksByClassName(htmlData, '#mlCompre', '.fifthComre');
    updateTicksByClassName(htmlData, '#mlConsentEduInfo', '.fifthConsent');
    updateTicksByClassName(htmlData, '#mlParticipantProp', '.sixth');
    updateTicksByClassName(htmlData, '#mlQuestionnaire', '.seventhQuestionnaires');
    updateTicksByClassName(htmlData, '#mlActiveTask', '.seventhTask');
    updateTicksByClassName(htmlData, '#mlResources', '.eighthResources');
    updateTicksByClassName(htmlData, '#mlNotification', '.eigthNotification');
    if ($('#mlConsent', htmlData).val() === 'true' && $('#mlCompre', htmlData).val() === 'true'
        && $('#mlConsentEduInfo', htmlData).val() === 'true') {
      let fifth = $('.fifth');
      if (fifth.find('span').length === 0) {
        fifth.append('<span class="sprites-icons-2 tick pull-right mt-xs"></span>');
      }
    } else $('.fifth').find('span').remove();

    if ($('#mlQuestionnaire', htmlData).val() === 'true' && $('#mlActiveTask', htmlData).val()
        === 'true') {
      let seventh = $('.seventh');
      if (seventh.find('span').length === 0) {
        seventh.append('<span class="sprites-icons-2 tick pull-right mt-xs"></span>');
      }
    } else $('.seventh').find('span').remove();
  }

  function updateTicksByClassName(htmlData, id, className) {
    if ($(id, htmlData).val() === 'true') {
      if ($(className).find('span').length === 0) {
        $(className).append('<span class="sprites-icons-2 tick pull-right mt-xs"></span>');
      }
    } else
      $(className).find('span').remove();
  }

  function updateCompletionTicksForEnglish() {
    updateEnglishTicksByClassName('#basicInfo', '.first');
    updateEnglishTicksByClassName('#settings', '.second');
    updateEnglishTicksByClassName('#overview', '.third');
    updateEnglishTicksByClassName('#eligibility', '.fourth');
    updateEnglishTicksByClassName('#consentEduInfo', '.fifthConsent');
    updateEnglishTicksByClassName('#comprehensionTest', '.fifthComre');
    updateEnglishTicksByClassName('#eConsent', '.fifthConsentReview');
    updateEnglishTicksByClassName('#participantProperties', '.sixth');
    updateEnglishTicksByClassName('#studyExcQuestionnaries', '.seventhQuestionnaires');
    updateEnglishTicksByClassName('#studyExcActiveTask', '.seventhTask');
    updateEnglishTicksByClassName('#resources', '.eighthResources');
    updateEnglishTicksByClassName('#notificationMenu', '.eigthNotification');

    if ($('#consentEduInfo').val() === 'true' && $('#comprehensionTest').val() === 'true'
        && $('#eConsent').val() === 'true') {
      let fifth = $('.fifth');
      if (fifth.find('span').length === 0) {
        fifth.append('<span class="sprites-icons-2 tick pull-right mt-xs"></span>');
      }
    } else $('.fifth').find('span').remove();

    if ($('#studyExcQuestionnaries').val() === 'true' && $('#studyExcActiveTask').val()
        === 'true') {
      let seventh = $('.seventh');
      if (seventh.find('span').length === 0) {
        seventh.append('<span class="sprites-icons-2 tick pull-right mt-xs"></span>');
      }
    } else {
      $('.seventh').find('span').remove();
    }
  }

  function updateEnglishTicksByClassName(id, className) {
    if ($(id).val() === 'true') {
      if ($(className).find('span').length === 0) {
        $(className).append('<span class="sprites-icons-2 tick pull-right mt-xs"></span>');
      }
    } else {
      $(className).find('span').remove();
    }
  }
</script>
