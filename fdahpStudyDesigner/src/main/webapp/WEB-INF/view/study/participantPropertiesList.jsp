<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<head>
    <meta charset="UTF-8">
    <style>
      .table > thead:last-child > tr:last-child > th, .table > body:last-child > tr:last-child > td {
        text-align: center;
      }

      .no-border {
        border: none !important;
        margin: 0px !important;
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

<div class="col-sm-10 col-rc white-bg p-none">

    <!--  Start top tab section-->
    <div class="right-content-head">
        <div class="text-right">
            <div class="black-md-f text-uppercase dis-line pull-left line34">Participant
                Properties
            </div>

            <c:if test="${studyBo.multiLanguageFlag eq true}">
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

            <div class="dis-line form-group mb-none">
                <button type="button" class="btn btn-default gray-btn cancelBut">Cancel</button>
            </div>

            <c:if test="${empty permission}">
                <div class="dis-line form-group mb-none ml-sm">
					<span class="tool-tip" id="markAsTooltipId" data-toggle="tooltip"
                          data-placement="bottom"
                            <c:if test="${!markAsComplete}"> title="${activityMsg}" </c:if>>
						<button type="button" class="btn btn-primary blue-btn"
                                id="markAsCompleteBtnId" onclick="markAsCompleted();"
                                <c:if test="${!markAsComplete}"> disabled </c:if>>Mark
							as Completed</button>
					</span>
                </div>
            </c:if>
        </div>
    </div>
    <!--  End  top tab section-->

    <!--  Start body tab section -->
    <div class="right-content-body">
        <div class="table-responsive">
            <table id="participantProperties_list"
                   class="display bor-none dragtbl dataTable no-footer" style="width:100% !important">
                <thead>
                <tr>
                    <th>SHORT TITLE</th>
                    <th>DATA TYPE</th>
                    <th><c:if test="${empty permission}">
                        <div class="dis-line form-group mb-none">
                            <button type="button" id="addButton" class="btn btn-primary blue-btn"
                                    onclick="addParticipantProperties();">+ Add Property
                            </button>
                        </div>
                    </c:if></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${participantPropertiesList}"
                           var="participantProperty">
                    <tr>
                        <td>${participantProperty.shortTitle}</td>
                        <td>${participantProperty.dataType}</td>
                        <td><span class="sprites_icon preview-g mr-lg"
                                  data-toggle="tooltip" data-placement="top" title="View"
                                  onclick="viewQuestionnaires(${participantProperty.id});"></span>
                            <span
                                    class="${participantProperty.completed?'edit-inc':'edit-inc-draft mr-md'} mr-lg
								<c:if test="${not empty permission}"> cursor-none </c:if> 
								<c:if test="${not participantProperty.status}"> cursor-none </c:if>"
                                    data-toggle="tooltip" data-placement="top" title="Edit"
                                    onclick="editQuestionnaires(${participantProperty.id});"></span>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    <!--  End body tab section -->
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
<!-- End right Content here -->

<form:form
        action="/fdahpStudyDesigner/adminStudies/addParticipantProperties.do?_S=${param._S}"
        name="participantPropertiesForm" id="participantPropertiesForm"
        method="post">
    <input type="hidden" name="participantPropertiesId"
           id="participantPropertiesId" value="">
    <input type="hidden" id="mlName" value="${studyLanguageBO.name}"/>
    <input type="hidden" id="customStudyName" value="${fn:escapeXml(studyBo.name)}"/>
    <input type="hidden" name="actionType" value="add">
</form:form>

<form:form
        action="/fdahpStudyDesigner/adminStudies/editParticipantProperties.do?_S=${param._S}"
        name="editParticipantProperties" id="editParticipantProperties"
        method="post">
    <input type="hidden" name="actionType" id="actionType" value="">
    <input type="hidden" name="${csrf.parameterName}" value="${csrf.token}">
    <input type="hidden" name="participantPropertyId"
           id="participantPropertyId" value="">
    <input type="hidden" name="studyId" id="studyId" value="${studyId}"/>
    <input type="hidden" id="currentLanguage" name="language" value="${currLanguage}">
</form:form>

<script>
var idleTime = 0;
  $(document).ready(function () {
    $('[data-toggle="tooltip"]').tooltip();

    $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
      var a = $(".col-lc").height();
      var b = $(".col-rc").height();
      if (a > b) {
        $(".col-rc").css("height", a);
      } else {
        $(".col-rc").css("height", "auto");
      }
    });

    let currLang = $('#studyLanguage').val();
    if (currLang !== undefined && currLang !== null && currLang !== '' && currLang !== 'en') {
      $('#currentLanguage').val(currLang);
      refreshAndFetchLanguageData(currLang);
    }

    $(".menuNav li.active").removeClass('active');
    $(".menuNav li.sixth").addClass('active');

    $('#participantProperties_list').DataTable({
      "paging": true,
      "abColumns": [
        {"bSortable": true},
        {"bSortable": true},
        {"bSortable": true}
      ],
      "order": [],
      "info": false,
      "lengthChange": false,
      "searching": false,
      "pageLength": 10,
      "fnRowCallback": function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
        // $('td:eq(0)', nRow).addClass("dis-none");
      }
    });
    if (document.getElementById("markAsCompleteBtnId") != null && document.getElementById(
        "markAsCompleteBtnId").disabled) {
      $('[data-toggle="tooltip"]').tooltip();
    }
  });
</script>


<script>
  $(document).ready(function () {
    var idleTime = 0;
    //datatable icon toggle
    $("#user_list thead tr th").click(function () {
      $(this).children().removeAttr('class')
      $(this).siblings().children().removeAttr('class').addClass('sort');
      if ($(this).attr('class') == 'sorting_asc') {
        $(this).children().addClass('asc');
      } else if ($(this).attr('class') == 'sorting_desc') {
        $(this).children().addClass('desc');
      } else {
        $(this).children().addClass('sort');
      }
    });

    let timeOutInterval = setInterval(function () {
      idleTime += 1;
      if (idleTime > 3) { // 5 minutes
        clearInterval(timeOutInterval);
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
              var a = document.createElement('a');
              a.href = "/fdahpStudyDesigner/sessionOut.do";
              document.body.appendChild(a).click();
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

  function addParticipantProperties() {
    $("#participantPropertiesId").val('');
    $("#participantPropertiesForm").submit();
  }

  function viewQuestionnaires(participantPropertyId) {
    if (participantPropertyId != null && participantPropertyId != '' && typeof participantPropertyId
        != 'undefined') {
      $("#actionType").val('view');
      $("#participantPropertyId").val(participantPropertyId);
      $("#editParticipantProperties").submit();
    }
  }

  function editQuestionnaires(participantPropertyId) {
    if (participantPropertyId != null && participantPropertyId != '' && typeof participantPropertyId
        != 'undefined') {
      $("#actionType").val('edit');
      $("#participantPropertyId").val(participantPropertyId);
      $("#editParticipantProperties").submit();
    }
  }

  function markAsCompleted() {
    document.editParticipantProperties.action = "/fdahpStudyDesigner/adminStudies/participantPropertiesMarkAsCompleted.do?_S=${param._S}";
    document.editParticipantProperties.submit();
  }

  $('#studyLanguage').on('change', function () {
    let currLang = $('#studyLanguage').val();
    $('#currentLanguage').val(currLang);
    refreshAndFetchLanguageData($('#studyLanguage').val());
  })

  function refreshAndFetchLanguageData(language) {
    $.ajax({
      url: '/fdahpStudyDesigner/adminStudies/participantPropertiesPage.do?_S=${param._S}',
      type: "GET",
      data: {
        language: language
      },
      success: function (data) {
        let htmlData = document.createElement('html');
        htmlData.innerHTML = data;
        if (language !== 'en') {
          updateCompletionTicks(htmlData);
          $('.tit_wrapper').text($('#mlName', htmlData).val());
          $('#addButton').attr('disabled', true);
          $('.sorting, .sorting_asc, .sorting_desc').css('pointer-events', 'none');
        } else {
          updateCompletionTicksForEnglish();
          $('.tit_wrapper').text($('#customStudyName', htmlData).val());
          $('#addButton').attr('disabled', false);
          $('.sorting, .sorting_asc, .sorting_desc').removeAttr('style');
        }
      }
    });
  }
</script>