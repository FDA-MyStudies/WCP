<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<head>
    <meta charset="UTF-8">
</head>
<style>
  .ml-dropdown-disabled {
    background-color: #eee !important;
    opacity: 1;
    cursor: not-allowed;
    pointer-events: none;
  }
  
  .globe{
    	position: relative;
  	  }

  	  .globe > button::before{
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

  	  .globe > button{
        padding-left: 30px;
  	  }
</style>
<!-- ============================================================== -->
<!-- Start right Content here -->
<!-- ============================================================== -->
<div class="col-sm-10 col-rc white-bg p-none">

    <!--  Start top tab section-->
    <div class="right-content-head">
        <%--		multilanguage data--%>
        <input type="hidden" id="mlDisplayText" value="${activeTaskLangBO.displayName}">
        <input type="hidden" id="mlInstructions" value="${activeTaskLangBO.instruction}">
        <input type="hidden" id="mlChartTitle" value="${activeTaskLangBO.chartTitle}">
        <input type="hidden" id="mlDisplayStat" value="${activeTaskLangBO.statName}">
        <input type="hidden" id="mlDisplayUnitStat" value="${activeTaskLangBO.displayUnitStat}">
        <input type="hidden" id="mlChartTitle2" value="${activeTaskLangBO.chartTitle2}">
        <input type="hidden" id="mlDisplayStat2" value="${activeTaskLangBO.statName2}">
        <input type="hidden" id="mlDisplayUnitStat2" value="${activeTaskLangBO.displayUnitStat2}">
        <input type="hidden" id="mlChartTitle3" value="${activeTaskLangBO.chartTitle3}">
        <input type="hidden" id="mlDisplayStat3" value="${activeTaskLangBO.statName3}">
        <input type="hidden" id="mlDisplayUnitStat3" value="${activeTaskLangBO.displayUnitStat3}">
        <input type="hidden" id="mlName" value="${studyLanguageBO.name}"/>
        <input type="hidden" id="customStudyName" value="${fn:escapeXml(studyBo.name)}"/>

        <%--		 english data--%>
        <input type="hidden" id="enDisplayText" value="${fn:escapeXml(activeTaskBo.displayName)}">
        <input type="hidden" id="enInstruction" value="${activeTaskBo.instruction}">
        <c:if test="${activeTaskBo.taskTypeId eq 1}">
            <input type="hidden" id="enFetalTitleChart"
                   value="${activeTaskBo.taskAttributeValueBos[2].titleChat}">
            <input type="hidden" id="enFetalDisplayStat"
                   value="${activeTaskBo.taskAttributeValueBos[2].displayNameStat}">
            <input type="hidden" id="enFetalDisplayUnitStat"
                   value="${activeTaskBo.taskAttributeValueBos[2].displayUnitStat}">
        </c:if>
        <c:if test="${activeTaskBo.taskTypeId eq 2}">
            <input type="hidden" id="enTowerTitleChart"
                   value="${activeTaskBo.taskAttributeValueBos[1].titleChat}">
            <input type="hidden" id="enTowerDisplayStat"
                   value="${activeTaskBo.taskAttributeValueBos[1].displayNameStat}">
            <input type="hidden" id="enTowerDisplayUnitStat"
                   value="${activeTaskBo.taskAttributeValueBos[1].displayUnitStat}">
        </c:if>
        <c:if test="${activeTaskBo.taskTypeId eq 3}">
            <input type="hidden" id="enSpatialTitleChart1"
                   value="${activeTaskBo.taskAttributeValueBos[7].titleChat}">
            <input type="hidden" id="enSpatialDisplayStat1"
                   value="${activeTaskBo.taskAttributeValueBos[7].displayNameStat}">
            <input type="hidden" id="enSpatialDisplayUnitStat1"
                   value="${activeTaskBo.taskAttributeValueBos[7].displayUnitStat}">
            <input type="hidden" id="enSpatialTitleChart2"
                   value="${activeTaskBo.taskAttributeValueBos[8].titleChat}">
            <input type="hidden" id="enSpatialDisplayStat2"
                   value="${activeTaskBo.taskAttributeValueBos[8].displayNameStat}">
            <input type="hidden" id="enSpatialDisplayUnitStat2"
                   value="${activeTaskBo.taskAttributeValueBos[8].displayUnitStat}">
            <input type="hidden" id="enSpatialTitleChart3"
                   value="${activeTaskBo.taskAttributeValueBos[9].titleChat}">
            <input type="hidden" id="enSpatialDisplayStat3"
                   value="${activeTaskBo.taskAttributeValueBos[9].displayNameStat}">
            <input type="hidden" id="enSpatialDisplayUnitStat3"
                   value="${activeTaskBo.taskAttributeValueBos[9].displayUnitStat}">
        </c:if>


        <div class="text-right">
            <div class="black-md-f text-uppercase dis-line pull-left line34">
				<span class="pr-sm cur-pointer" onclick="goToBackPage(this);">
					<img src="../images/icons/back-b.png" class="pr-md"/>
				</span>
                <c:if test="${actionPage eq 'add'}"> Add Active Task</c:if>
                <c:if test="${actionPage eq 'addEdit'}">Edit Active Task</c:if>
                <c:if test="${actionPage eq 'view'}">View Active Task <c:set
                        var="isLive">${_S}isLive</c:set>${not empty  sessionScope[isLive]?'<span class="eye-inc ml-sm vertical-align-text-top"></span> ':''} ${not empty  sessionScope[isLive]?activeTaskBo.activeTaskVersion:''}
                </c:if>
            </div>

            <c:if test="${studyBo.multiLanguageFlag eq true and actionPage != 'add'}">
                <div class="dis-line form-group mb-none mr-sm" style="width: 150px;">
                    <select
                            class="selectpicker aq-select aq-select-form studyLanguage globe lang-specific"
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

            <c:if test="${studyBo.multiLanguageFlag eq true and actionPage == 'add'}">
                <div class="dis-line form-group mb-none mr-sm" style="width: 150px;">
                    <span class="tool-tip" id="markAsTooltipId" data-toggle="tooltip"
                          data-placement="bottom"
                          title="Language selection is available in edit screen only">
						<select class="selectpicker aq-select aq-select-form studyLanguage globe lang-specific"
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

            <div class="dis-line form-group mb-none mr-sm">
                <button type="button" class="btn btn-default gray-btn actBut"
                        id="saveId">Save
                </button>
            </div>

            <div class="dis-line form-group mb-none">
                <button type="button" class="btn btn-primary blue-btn actBut"
                        id="doneId">Done
                </button>
            </div>
        </div>
    </div>
    <!--  End  top tab section-->

    <!--  Start body tab section -->
    <div class="right-content-body pt-none pl-none pr-none">

        <ul class="nav nav-tabs review-tabs gray-bg" id="tabsId">
            <li class="contentClass active"><a data-toggle="tab"
                                               href="#content">Content</a></li>
            <li class="scheduleTaskClass linkDis" disabled><a
                    data-toggle="tab" href="#schedule">Schedule</a></li>
        </ul>
        <div class="tab-content pl-xlg pr-xlg">
            <!-- Content-->
            <div id="content" class="tab-pane fade in active mt-xlg">
                <div class="mt-md blue-md-f text-uppercase">Select Active Task</div>
                <div class="gray-xs-f mt-md mb-sm">Choose from a list of
                    pre-defined active tasks
                </div>
                <div class="col-md-4 p-none">
                    <select class="selectpicker targetOption" id="targetOptionId"
                            taskId="${activeTaskBo.id}" title="Select">
                        <c:forEach items="${activeTaskListBos}" var="activeTaskTypeInfo">
                            <option value="${activeTaskTypeInfo.activeTaskListId}"
                                ${activeTaskBo.taskTypeId eq activeTaskTypeInfo.activeTaskListId ?'selected':''}>${activeTaskTypeInfo.taskName}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="clearfix"></div>
                <div class="mt-sm black-xs-f italic-txt activeText"></div>

                <div class="changeContent"></div>
            </div>
            <!-- End Content-->
            <!---  Schedule --->
            <div id="schedule" class="tab-pane fade mt-xlg"></div>
        </div>
    </div>
    <!--  End body tab section -->
</div>
<!-- End right Content here -->
<script>
  $(document)
  .ready(
      function () {
        // Fancy Scroll Bar
        var changeTabSchedule = true;

        $(".menuNav li.active").removeClass('active');
        $(".seventhTask").addClass('active');
        actionPageView();

        var typeOfActiveTask = '${activeTaskBo.taskTypeId}';
        var activeTaskInfoId = '${activeTaskBo.id}';
        if (!activeTaskInfoId) {
          activeTaskInfoId = '${activeTaskInfoId}';
        }
        var actionType = '${actionPage}';

        var selectedTask = $('.targetOption').find(
            "option:selected").text();

        if (activeTaskInfoId) {
          $('.targetOption').prop('disabled', true);
          $('.targetOption').addClass('linkDis');
          $('.activeText')
          .empty()
          .append(
              'This task records fetal activity for a given duration of time,')
          .append($("<br>"))
          .append(
              'in terms of the number of times the woman experiences kicks.');
          $('.scheduleTaskClass').prop('disabled', false);
          $('.scheduleTaskClass').removeClass('linkDis');
        } else {
          $('.actBut').hide();
        }
        if (typeOfActiveTask && activeTaskInfoId) {
          loadSelectedATask(typeOfActiveTask, activeTaskInfoId, actionType);
        }
        $(".schedule").click(function () {
          $(".all").addClass("dis-none");
          var schedule_opts = $(this).val();
          $("." + schedule_opts).removeClass("dis-none");
        });
        $("#targetOptionId")
        .change(
            function () {
              console.log($(this).val());
              var taskId = $(this).val();
              if (taskId == 1) {
                $('.activeText')
                .empty()
                .append(
                    'This task records fetal activity for a given duration of time,')
                .append($("<br>"))
                .append(
                    'in terms of the number of times the woman experiences kicks.');
              } else if (taskId == 2) {
                $('.activeText')
                .text(
                    "This task measures a person's problem-solving skills.");
              } else if (taskId == 3) {
                $('.activeText')
                .text(
                    "The task collects data that can be used to assess visuospatial memory and executive function.");
              }
              var typeOfActiveTask = $(this)
              .val();
              var activeTaskInfoId = $(this)
              .attr('taskId');
              $('.changeContent').empty();
              $(document).find('#saveId,#doneId')
              .unbind();
              loadSelectedATask(typeOfActiveTask,
                  activeTaskInfoId,
                  actionType);
              $('.actBut').show();
              $('.scheduleTaskClass').prop(
                  'disabled', false);
              $('.scheduleTaskClass')
              .removeClass('linkDis');
            });
        if (activeTaskInfoId || selectedTask) {
          loadActiveSchedule(changeTabSchedule);
        }

        function loadSelectedATask(typeOfActiveTask,
            activeTaskInfoId, actionType) {
          let lang = ($('#studyLanguage').val()!==undefined)?$('#studyLanguage').val():'';
          $(".changeContent").load(
              "/fdahpStudyDesigner/adminStudies/navigateContentActiveTask.do?${_csrf.parameterName}=${_csrf.token}&_S=${param._S}&language="
              + lang, {
                noncache: new Date().getTime(),
                typeOfActiveTask: typeOfActiveTask,
                activeTaskInfoId: activeTaskInfoId,
                actionType: actionType
              },
              function () {
                $(this)
                .parents('form')
                .attr(
                    'action',
                    '/fdahpStudyDesigner/adminStudies/saveOrUpdateActiveTaskContent.do?_S=${param._S}');
                resetValidation($(this)
                .parents('form'));
                var dt = new Date();
                $('#inputClockId')
                .datetimepicker(
                    {
                      format: 'HH:mm',
                      minDate: new Date(
                          dt
                          .getFullYear(),
                          dt
                          .getMonth(),
                          dt
                          .getDate(),
                          00,
                          00),
                      maxDate: new Date(
                          dt
                          .getFullYear(),
                          dt
                          .getMonth(),
                          dt
                          .getDate(),
                          23,
                          59)
                    });
                actionPageView();
                var currentPage = '${currentPage}';
                $('#currentPageId').val(
                    currentPage);
              });

        }

        function loadActiveSchedule(changeTabSchedule) {
          if (changeTabSchedule) {
            $("#schedule")
            .load(
                "/fdahpStudyDesigner/adminStudies/viewScheduledActiveTask.do?${_csrf.parameterName}=${_csrf.token}&_S=${param._S}",
                {
                  noncache: new Date()
                  .getTime(),
                  activeTaskId: activeTaskInfoId
                }, function () {
                  resetValidation($('form'));
                  actionPageView();
                });
            changeTabSchedule = false;
          } else {
            resetValidation($('form'));
          }
        }

        $('#tabsId a').click(function (e) {
          e.preventDefault();
          $(this).tab('show');
        });

        // store the currently selected tab in the hash value
        $("ul.nav-tabs > li > a")
        .on(
            "shown.bs.tab",
            function (e) {
              var id = $(e.target).attr("href")
              .substr(1);
              window.location.hash = id;
              $('#currentPageId').val(id);
            });

        //alert()
        // on load of the page: switch to the currently selected tab
        var hash = window.location.hash;
        $('#tabsId a[href="' + hash + '"]').tab('show');
        window.addEventListener("popstate", function (e) {
          var activeTab = $('[href="' + window.location.hash
              + '"]');
          if (activeTab.length) {
            activeTab.tab('show');
          } else {
            $('.nav-tabs a:first').tab('show');
          }
        });
      });

  function goToBackPage(item) {
    //window.history.back();
    <c:if test="${actionPage ne 'view'}">
    $(item).prop('disabled', true);
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
          let lang = ($('#studyLanguage').val()!==undefined)?$('#studyLanguage').val():'';
          a.href = "/fdahpStudyDesigner/adminStudies/viewStudyActiveTasks.do?_S=${param._S}&language="
              + lang;
          document.body.appendChild(a).click();
        } else {
          $(item).prop('disabled', false);
        }
      }
    });
    </c:if>
    <c:if test="${actionPage eq 'view'}">
    var a = document.createElement('a');
    let lang = ($('#studyLanguage').val()!==undefined)?$('#studyLanguage').val():'';
    a.href = "/fdahpStudyDesigner/adminStudies/viewStudyActiveTasks.do?_S=${param._S}&language="
        + lang;
    document.body.appendChild(a).click();
    </c:if>
  }

  function actionPageView() {
    <c:if test="${actionPage eq 'view'}">
    $(document).find('input,textarea,select').prop('disabled', true);
    $(document).find('form.elaborateClass').addClass('linkDis');
    $(document).find('.actBut, .addBtnDis, .remBtnDis').remove();
    $('#studyLanguage').attr('disabled', false);
    </c:if>
  }

  $('#studyLanguage').on('change', function () {
    let currLang = $('#studyLanguage').val();
    $('#currentLanguage').val(currLang);
    refreshAndFetchLanguageData($('#studyLanguage').val());
  })

  function refreshAndFetchLanguageData(language) {
    $.ajax({
      url: '/fdahpStudyDesigner/adminStudies/viewActiveTask.do?_S=${param._S}',
      type: "GET",
      data: {
        language: language,
      },
      success: function (data) {
        let htmlData = document.createElement('html');
        htmlData.innerHTML = data;
        if (language !== 'en') {
          updateCompletionTicks(htmlData);
          $('.tit_wrapper').text($('#mlName', htmlData).val());
          $('.remBtnDis, .addBtnDis').addClass('cursor-none');
          $('select, input[type!=hidden]').each(function () {
            if (!$(this).hasClass('lang-specific')) {
              $(this).addClass('ml-dropdown-disabled');
              if (this.nodeName.toLowerCase() === 'select') {
                let id = this.id;
                if (id !== undefined && id !== '') {
                  $('[data-id=' + id + ']').addClass('ml-dropdown-disabled');
                }
              }
            }
          });
          $('#displayName').val($('#mlDisplayText', htmlData).val());
          $('#comment').val($('#mlInstructions', htmlData).val());
          let targetOption = $('#targetOptionId').val();
          if (targetOption === '3') {
            if ($('#Score_spatial_chart_id').prop('checked') === true)
              $('#lineChartId').val($('#mlChartTitle', htmlData).val());
            if ($('#Score_spatial_stat_id').prop('checked') === true){
            	$('#displayStat4').val($('#mlDisplayStat', htmlData).val());
            	 $('#displayUnitStat4').val($('#mlDisplayUnitStat', htmlData).val());
            }
            if ($('#Number_of_Games_spatial_chart_id').prop('checked') === true)
              $('#lineChartId1').val($('#mlChartTitle2', htmlData).val());
            if ($('#Number_of_Games_spatial_stat_id').prop('checked') === true){
            	 $('#displayStat5').val($('#mlDisplayStat2', htmlData).val());
            	 $('#displayUnitStat5').val($('#mlDisplayUnitStat2', htmlData).val());
            }
            if ($('#Number_of_Failures_spatial_chart_id').prop('checked') === true)
              $('#lineChartId2').val($('#mlChartTitle3', htmlData).val());
            if ($('#Number_of_Failures_spatial_stat_id').prop('checked') === true){
            	$('#displayStat6').val($('#mlDisplayStat3', htmlData).val());
            	$('#displayUnitStat6').val($('#mlDisplayUnitStat3', htmlData).val());
            }
          } else {
            let id1 = '';
            let id2 = '';
            if (targetOption === '2') {
              id1 = '[name="taskAttributeValueBos[1].addToLineChart"]';
              id2 = '[name="taskAttributeValueBos[1].useForStatistic"]';
            } else {
              id1 = '#number_of_kicks_recorded_fetal_chart_id';
              id2 = '#number_of_kicks_recorded_fetal_stat_id';
            }
            if ($(id1).prop('checked') === true)
              $('#lineChartId').val($('#mlChartTitle', htmlData).val());
            if ($(id2).prop('checked') === true){
              $('#displayStat').val($('#mlDisplayStat', htmlData).val());
              $('#displayUnitStat').val($('#mlDisplayUnitStat', htmlData).val());
            }
          }
        } else {
          updateCompletionTicksForEnglish();
          $('.tit_wrapper').text($('#customStudyName', htmlData).val());
          $('.remBtnDis, .addBtnDis').removeClass('cursor-none');
          $('select, input[type!=hidden]').each(function () {
            if (!$(this).hasClass('lang-specific')) {
              $(this).removeClass('ml-dropdown-disabled');
              if (this.nodeName.toLowerCase() === 'select') {
                let id = this.id;
                if (id !== undefined && id !== '') {
                  $('[data-id=' + id + ']').removeClass('ml-dropdown-disabled');
                }
              }
            }
          });

          $('#displayName').val($('#enDisplayText', htmlData).val());
          $('#comment').val($('#enInstruction', htmlData).val());
          let targetOption = $('#targetOptionId').val();
          if (targetOption === '3') {
            if ($('#Score_spatial_chart_id').prop('checked') === true)
              $('#lineChartId').val($('#enSpatialTitleChart1', htmlData).val());
            if ($('#Score_spatial_stat_id').prop('checked') === true){
            	$('#displayStat4').val($('#enSpatialDisplayStat1', htmlData).val());
            	 $('#displayUnitStat4').val($('#enSpatialDisplayUnitStat1', htmlData).val());
            }
            if ($('#Number_of_Games_spatial_chart_id').prop('checked') === true)
              $('#lineChartId1').val($('#enSpatialTitleChart2', htmlData).val());
            if ($('#Number_of_Games_spatial_stat_id').prop('checked') === true){
            	 $('#displayStat5').val($('#enSpatialDisplayStat2', htmlData).val());
            	 $('#displayUnitStat5').val($('#enSpatialDisplayUnitStat2', htmlData).val());
            }
            if ($('#Number_of_Failures_spatial_chart_id').prop('checked') === true)
              $('#lineChartId2').val($('#enSpatialTitleChart3', htmlData).val());
            if ($('#Number_of_Failures_spatial_stat_id').prop('checked') === true){
            	 $('#displayStat6').val($('#enSpatialDisplayStat3', htmlData).val());
            	 $('#displayUnitStat6').val($('#enSpatialDisplayUnitStat3', htmlData).val());
            }
          } else {
            let id1 = '';
            let id2 = '';
            let id3 = '';
            let id4 = '';
            if (targetOption === '2') {
              id1 = '[name="taskAttributeValueBos[1].addToLineChart"]';
              id2 = '[name="taskAttributeValueBos[1].useForStatistic"]';
              id3 = '#enTowerTitleChart';
              id4 = '#enTowerDisplayStat';
            } else {
              id1 = '#number_of_kicks_recorded_fetal_chart_id';
              id2 = '#number_of_kicks_recorded_fetal_stat_id';
              id3 = '#enFetalTitleChart';
              id4 = '#enFetalDisplayStat';
            }
            if ($(id1).prop('checked') === true)
              $('#lineChartId').val($(id3, htmlData).val());
            if ($(id2).prop('checked') === true){
              $('#displayStat').val($(id4, htmlData).val());
              $('#displayUnitStat').val($(id4, htmlData).val());
            }
          }
          <c:if test="${actionPage eq 'view'}">
          $(document).find('input,textarea').prop('disabled', true);
          </c:if>
        }
      }
    })
  }


</script>