<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<head>
    <meta charset="UTF-8">
    <style>
      .tool-tip {
        display: inline-block;
      }

      .tool-tip [disabled] {
        pointer-events: none;
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
    </style>
</head>
<!-- ============================================================== -->
<!-- Start right Content here -->
<!-- ============================================================== -->
<div class="col-sm-10 col-rc white-bg p-none">
    <!--  Start top tab section-->
    <div class="right-content-head">
        <div class="text-right">
       <div class="black-md-f dis-line pull-left line34">
				<span class="pr-sm cur-pointer" onclick="goToBackPage(this);">
				<img src="../images/icons/back-b.png" class="pr-md"/></span>
                                    
            </div>
            <div class="black-md-f text-uppercase dis-line pull-left line34">GROUPS</div>
          <div class="dis-line form-group mb-none">

                         <!--   <button type="button" class="btn btn-primary blue-btn"
                                    id="addButton" onclick="addGroups();">+ Add Group
                            </button> -->

                            <button type="button"
                            							class="btn btn-primary blue-btn addOrEditGroups"
                            							style="margin-top: 12px;">+ Add Group</button>

                        </div>
        </div>
    </div>
    <!--  End  top tab section-->
   <!--  Start body tab section -->
    <div class="right-content-body pt-none">
        <div>
            <table id="groups_list" class="display bor-none dragtbl"
                   cellspacing="0" width="100%">
                <thead>
                <tr>
                    <th style="display: none;"></th>
                    <th>GROUP ID<span class="sort"></span></th>
                    <th>GROUP NAME<span class="sort"></span></th>
                    <th>ACTION</th>
                </tr>
                </thead>
                 <c:forEach items="${groupsList}" var="groupsList">
                 <tr id="row${groupsList.groupId}">
                 <td>${groupsList.groupId}</td>
                 <td class="wid50 title">${groupsList.groupName}</td>
                 <td style="width: 200px !important;">
                  <span class="sprites_icon edit-g addOrEditGroups"
                  id="${groupsList.id}" data-toggle="tooltip"
                  	data-placement="top" title="Edit" id="editIcon${groupsList.id}">
                  	</span>


                  <span
                                class="sprites_icon copy delete <c:if test="${not empty permission}"> cursor-none </c:if>"
                                data-toggle="tooltip" data-placement="top" title="Delete"
                                onclick="deleteGroups(${groupInfo.groupId});"></span></td>
                 </tr>
                 </c:forEach>
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
        action="/fdahpStudyDesigner/adminStudies/viewGroups.do?_S=${param._S}"
        name="groupsInfoForm" id="groupsInfoForm" method="post">
    <input type="hidden" name="groupId" id="groupId"
           value="">
    <input type="hidden" name="actionType" id="actionType">
    <input type="hidden" name="studyId" id="studyId" value="${studyId}"/>
    <input type="hidden" name="chkRefreshflag" value="y">
</form:form>

<form:form
        action="/fdahpStudyDesigner/adminStudies/addOrEditGroupsDetails.do?_S=${param._S}"
        name="addgroupsInfoForm" id="addgroupsInfoForm" method="post">
    <input type="hidden" name="groupId" id="groupId"
           value="${groupId}">
 <input type="hidden" name="id" id="id"
                      value="${id}">
    <input type="hidden" name="actionType" id="actionType">
    <input type="hidden" name="studyId" id="studyId" value="${studyId}"/>
    <input type="hidden" id="checkRefreshFlag" name="checkRefreshFlag">
    <input type="hidden" name="questionnaireId" id="questionnaireId" value="${questionnaireId}">
</form:form>


<form:form
             action="/fdahpStudyDesigner/sessionOut.do"
              id="backToLoginPage"
              name="backToLoginPage"
              method="post">
</form:form>

<script>
var idleTime = 0;
  $(document).ready(function () {
    $('[data-toggle="tooltip"]').tooltip();
    $(".menuNav li.active").removeClass('active');
    $(".seventhQuestionnaires").addClass('active');
    $('#studyLanguage').removeClass('linkDis');
	
    let currLang = $('#studyLanguage').val();
    if (currLang !== undefined && currLang !== null && currLang !== '' && currLang !== 'en') {
      $('#currentLanguage').val(currLang);
      refreshAndFetchLanguageData(currLang);
    }

    $('#groups_list').DataTable({
      "paging": true,
      "abColumns": [
        {"bSortable": true},
        {"bSortable": true},
        {"bSortable": true}
      ],
      "order": [[0, "desc"]],
      "info": false,
      "lengthChange": false,
      "searching": false,
      "pageLength": 10,
      "fnRowCallback": function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
        $('td:eq(0)', nRow).addClass("dis-none");
      }
    });
	
     
	
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
                         i-=1;
                          }
                        }, 60000);
                      }
  });

  
  
 
function refresh() {
	$.ajax({
		url: '/fdahpStudyDesigner/adminStudies/viewGroups.do?_S=${param._S}',
		type: "GET",
		data: {
			questionnaireId: questionnaireId
		},
		success: function (data) {
          let htmlData = document.createElement('html');
          htmlData.innerHTML = data;
			if (language !== 'en') {
              
				let mark =true;
              $('#groupsList option', htmlData).each(function (index, value) {
                let id = '#row' + value.getAttribute('id');
                $(id).find('td.title').text(value.getAttribute('value'));
                if (value.getAttribute('status')==="true") {
                  let edit = $(id).find('span.editIcon');
                  if (!edit.hasClass('edit-inc')) {
                    edit.addClass('edit-inc');
                  }
                  if (edit.hasClass('edit-inc-draft')) {
                    edit.removeClass('edit-inc-draft');
                  }
                }
                else {
                  mark =false;
                  let edit = $(id).find('span.editIcon');
                  if (!edit.hasClass('edit-inc-draft')) {
                    edit.addClass('edit-inc-draft');
                  }
                  if (edit.hasClass('edit-inc')) {
                    edit.removeClass('edit-inc');
                  }
                }
              });
              if (!mark) {
                $('#markAsCompleteBtnId').addClass('cursor-none').prop('disabled', true);
                $('#markAsTooltipId').attr('data-original-title', 'Please ensure individual list items on this page are marked Done before attempting to mark this section as Complete.')
              } else {
                $('#markAsCompleteBtnId').removeClass('cursor-none').prop('disabled', false);
                $('#markAsTooltipId').removeAttr('data-original-title');
              }
			} else {
              
              let mark=true;
              $('tbody tr', htmlData).each(function (index, value) {
                let id = '#'+value.getAttribute('id');
                $(id).find('td.title').text($(id, htmlData).find('td.title').text());
                if (value.getAttribute('status')==="true") {
                  let edit = $(id).find('span.editIcon');
                  if (!edit.hasClass('edit-inc')) {
                    edit.addClass('edit-inc');
                  }
                  if (edit.hasClass('edit-inc-draft')) {
                    edit.removeClass('edit-inc-draft');
                  }
                }
                else {
                  mark=false;
                  let edit = $(id).find('span.editIcon');
                  if (!edit.hasClass('edit-inc-draft')) {
                    edit.addClass('edit-inc-draft');
                  }
                  if (edit.hasClass('edit-inc')) {
                    edit.removeClass('edit-inc');
                  }
                }
              });
              if (!mark) {
                $('#markAsCompleteBtnId').addClass('cursor-none').prop('disabled', true);
                $('#markAsTooltipId').attr('data-original-title', 'Please ensure individual list items on this page are marked Done before attempting to mark this section as Complete.')
              } else {
                $('#markAsCompleteBtnId').removeClass('cursor-none').prop('disabled', false);
                $('#markAsTooltipId').removeAttr('data-original-title');
              }
			}
		}
	});
	  
	
}

    function goToBackPage(item) {
          var a = document.createElement('a'); 
              a.href = "/fdahpStudyDesigner/adminStudies/viewQuestionnaire.do?_S=${param._S}";
              document.body.appendChild(a).click();
           actionOn
        
      }

     /* function addGroups() {
              $('#groupId').val($(this).attr('groupId'));
              $('#checkRefreshFlag').val('Y');
              $("#addgroupsInfoForm").submit();
            } */

	$('.addOrEditGroups').on('click',function(){
			$('#id').val($(this).attr('id'));
			$('#checkRefreshFlag').val('Y');
			$('#addgroupsInfoForm').submit();
	});

</script>

