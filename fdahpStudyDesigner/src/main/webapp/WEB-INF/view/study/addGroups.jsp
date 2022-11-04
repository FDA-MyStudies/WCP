<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<jsp:useBean id="date" class="java.util.Date" />
<c:set var="tz" value="America/Los_Angeles" />

<head>
<meta charset="UTF-8">
<style>
.modal-dialog {
left: -3px !important;
}
.col-rc {
width:1100px !important;
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

.ml-disabled {
        background-color: #eee !important;
        opacity: 1;
        cursor: not-allowed;
        pointer-events: none;
      }

#addGroupFormId{
display:contents !important;
}

.text-normal > button > .filter-option{
          text-transform: inherit !important;
      }

      .formula-box {
          height: 50px;
          border:1px solid #bfdceb;
          border-bottom:0;
          padding: 15px;
          color: #007cba;
      }

      .operator {
          width: 63% !important;
      }

      .dest-label {
          padding-left: 0 !important;
          padding-top: 7px;
      }


      .dest-row {
          margin-top: 12px;
      }

</style>

</head>

<!-- Start right Content here -->
<form:form
action="/fdahpStudyDesigner/adminStudies/addOrUpdateGroupsDetails.do?_S=${param._S}"
name="addGroupFormId" id="addGroupFormId" method="post" >
<input type="hidden" name="language" value="${currLanguage}">
<input type="hidden" id="actionType" name="actionType" value="${fn:escapeXml(actionType)}">
<input type="hidden" id="buttonText" name="buttonText" value="">
<input type="hidden" id="isPublished" name="isPublished" value="${groupsBo.isPublished}">
<input type="hidden" value="${groupsBean.action}" id="action" name="action">
<input type="hidden" value="" id="buttonText" value="${id}" name="buttonText">
<input type="hidden" id="isAutoSaved" value="${isAutoSaved}" name="isAutoSaved"/>

          <div class="col-sm-10 col-rc white-bg p-none">
            <!--  Start top tab section-->
            <div class="right-content-head">
              <div class="text-right">
           <div class="black-md-f dis-line pull-left line34">
                <span class="mr-xs cur-pointer" onclick="goToBackPage(this);">
                <img src="../images/icons/back-b.png"/></span>
                Group-Level Attributes
            </div>
                 <c:if test="${studyBo.multiLanguageFlag eq true and actionType != 'add'}">
                <div class="dis-line form-group mb-none mr-sm" style="width: 150px;">
                    <select
                            class="selectpicker aq-select aq-select-form studyLanguage langSpecific"
                            id="studyLanguage" name="studyLanguage" title="Select">
                        <option value="en" ${((currLanguage eq null) or (currLanguage eq '') or (currLanguage eq 'undefined') or (currLanguage eq 'en')) ?'selected':''}>
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
                  <button type="button" class="btn btn-default gray-btn" onclick="goToBackPage(this);">Cancel </button>
                </div>
                <div class="dis-line form-group mb-none mr-sm">
                  <button type="button" class="btn btn-default gray-btn" id="saveId">Save </button>
                </div>
                <div class="dis-line form-group mb-none">
                  <span class="tool-tip" data-toggle="tooltip" data-placement="bottom" id="helpNote"></span>
                  <button type="button" class="btn btn-primary blue-btn" id="doneGroupId" >Done</button>
                  </span>
                </div>
              </div>
            </div>
            <!--  End  top tab section-->
            <!--  Start body tab section -->
            <div class="right-content-body">
              <div>
                <div class="form-group"></div>
                <div class="row form-group">
                  <div class="col-md-6 pl-none">
                  <input type="hidden" name="id" id="id" value="${fn:escapeXml(groupsBo.id)}">
                    <div class="gray-xs-f mb-xs" >Group ID <span class="requiredStar">*</span> <span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" title="" data-original-title="The identification number of the group shall be mentioned in this text box."></span>
                    <div class="help-block with-errors red-txt"></div>
                    </div>
                    <div class="form-group">
                      <input  type="text" type="text" class="form-control req" placeholder="Enter group ID"  name ="groupId" id="groupId" value="${fn:escapeXml(groupsBo.groupId)}"
                      <c:if test="${groupsBo.isPublished eq 1}"> disabled </c:if> required
                      <c:if test="${currLanguage eq 'es'}"><c:out value="disabled='disabled'"/></c:if>>

                      <div class="help-block with-errors red-txt"></div>
                      <input type="hidden" id="preGroupId"
                           value="${fn:escapeXml(groupsBo.groupId)}"/>
                    </div>
                  </div>
                  <div class="col-md-6 pl-none">
                    <div class="gray-xs-f mb-xs">Group Name <span class="requiredStar">*</span> <span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" title="" data-original-title="The name of the group can be mentioned in this text box."></span>
                        <div class="help-block with-errors red-txt"></div>
                    </div>

                    <div class="form-group">
                      <input type="text"  type="text" class="form-control req" placeholder="Enter group name" name ="groupName" id="groupName" value="${fn:escapeXml(groupsBo.groupName)}" required
                      <c:if test="${currLanguage eq 'es'}"><c:out value="disabled='disabled'"/></c:if>>
                    <div class="help-block with-errors red-txt"></div>
                     <input type="hidden" id="preGroupName"
                           value="${fn:escapeXml(groupsBo.groupName)}"/>
                    </div>

				</div>
			</div>
		</div>
		<div>
                         <div class="gray-xs-f mb-xs">Group Default Visibility</div>
                         <div>
                             <label class="switch bg-transparent mt-xs">
                                 <input type="checkbox" class="switch-input"
                                        id="groupDefaultVisibility"
                                 <c:if test="${empty groupsBo.id || groupStepLists.size()<2}"><c:out value="disabled='disabled'"/></c:if>
                                 <c:if test="${empty groupsBo.defaultVisibility || groupsBo.defaultVisibility eq 'true'}"> checked</c:if>>
                                 <span class="switch-label bg-transparent" data-on="On" data-off="Off"></span>
                                 <span class="switch-handle"></span>
                             </label>
                         </div>
                     </div>

                     <div id="logicDiv">
                         <div class="row">
                             <div class="gray-xs-f mb-xs">Pre-Load Logic</div>
                         </div>
                         <div class="row">
                             <div class="col-md-3 dest-label">
                                 If True, Destination step =
                             </div>
                             <div class="col-md-1"></div>
                             <div class="col-md-5 parent-pll">
                                 <select name="destinationTrueAsGroup" id="destinationTrueAsGroup"
                                         data-error="Please choose one option" class="selectpicker text-normal req-pll"
                                         title="-select-">
                             <c:forEach items="${qTreeMap}"
								var="destinationStep">
								<c:if
									test="${destinationStep.value.stepType eq 'Instruction' || destinationStep.value.stepType eq 'Question'}">
									<option value="${destinationStep.key}"
										${groupsBo.destinationTrueAsGroup eq destinationStep.key ? 'selected' :''}>
										Step ${destinationStep.key} : ${destinationStep.value.title}</option>
								</c:if>
								<c:if test="${destinationStep.value.stepType eq 'Form'}">
									<c:forEach items="${destinationStep.value.fromMap}"
										var="subentry">
										<option value="${destinationStep.key}"
											${groupsBo.destinationTrueAsGroup eq destinationStep.key ? 'selected' :''}>
											Step ${destinationStep.key} : ${subentry.value.title}</option>
									</c:forEach>
								</c:if>
							</c:forEach>
							<c:forEach items="${groupsList}" var="group" varStatus="status">
                                <option value="${group.id}" id="selectGroup${group.id}"
                                    ${groupsBo.destinationTrueAsGroup eq group.id ? 'selected' :''}>
                                    Group ${status.index + 1} : ${group.groupName}&nbsp;
                                </option>
                            </c:forEach>
                            <option value="0"
                                ${groupsBo.destinationTrueAsGroup eq 0 ? 'selected' :''}>
                                Completion Step
                            </option>
                        </select>
                        <div class="help-block with-errors red-txt"></div>
                    </div>
                </div>
                <br>
            </div>
            <br>

                         <div id="formulaContainer${status.index}">
                             <c:choose>
                                 <c:when test="${preLoadLogicBoList.size() gt 0}">
                                     <c:forEach items="${preLoadLogicBoList}" var="preLoadLogicBean" varStatus="status">
                                         <div id="form-div${status.index}"
                                              <c:if test="${status.index gt 0}">style="height: 200px; margin-top:20px"</c:if>
                                              <c:if test="${status.index eq 0}">style="height: 150px;"</c:if>
                                              class="form-div <c:if test="${status.index gt 0}">deletable</c:if>">
                                             <c:if test="${status.index gt 0}">
                                                 <div class="form-group">
            												<span class="radio radio-info radio-inline p-45 pl-2">
            													<input type="radio" id="andRadio${status.index}" value="&&" class="con-radio con-op-and" name="preLoadLogicBeans[${status.index}].conditionOperator"
                                                                       <c:if test="${preLoadLogicBean.conditionOperator eq '&&'}">checked </c:if> />
            													<label for="andRadio${status.index}">AND</label>
            												</span>
                                        <span class="radio radio-inline">
                                            <input type="radio" id="orRadio${status.index}"
                                                   value="||" class="con-radio con-op-or"
                                                   name="preLoadLogicBeans[${status.index}].conditionOperator"
                                                   <c:if test="${preLoadLogicBean.conditionOperator eq '||'}">checked </c:if> />
                                            <label for="orRadio${status.index}">OR</label>
                                        </span>
                                    </div>
                                </c:if>
                                <div>
                                    <div class="row formula-box">
                                        <div class="col-md-2">
                                            <strong class="font-family: arial;">Formula</strong>
                                        </div>
                                        <div class="col-md-10 text-right">
                                            <c:if test="${status.index gt 0}">
                                                <span class="delete vertical-align-middle remBtnDis hide pl-md align-span-center"
                                                      data-id="form-div${status.index}"
                                                      onclick="removeFormulaContainer(this)"></span>
                                            </c:if>
                                        </div>
                                    </div>
                                    <div style="height: 100px; border:1px solid #bfdceb;">
                                        <div class="row">
                                            <div class="col-md-3 gray-xs-f mb-xs" style="padding-top: 18px;">Define
                                                Functions
                                            </div>
                                            <div class="col-md-3 gray-xs-f mb-xs" style="padding-top: 18px;">Define
                                                Inputs
                                            </div>
                                            <div class="col-md-6"></div>
                                        </div>
                                        <div class="row data-div">
                                            <div class="col-md-1" style="padding-top: 7px">Operator</div>
                                            <div class="col-md-2 parent-pll form-group">
                                                <select class="selectpicker operator text-normal req-pll"
                                                        id="operator${status.index}"
                                                        name="preLoadLogicBeans[${status.index}].operator"
                                                        title="-select-">
                                                   <%--  <c:if test="${currLanguage ne 'es'}"> --%>
                                                        <c:forEach items="${operators}" var="operator">
                                                            <option value="${operator}" ${preLoadLogicBean.operator eq operator ?'selected':''}>${operator}</option>
                                                        </c:forEach>
                                                    <%-- </c:if> --%>
                                                </select>
                                                <div class="help-block with-errors red-txt"></div>
                                            </div>

                                            <div class="col-md-1" style="padding-top: 7px">Value&nbsp;&nbsp;&nbsp;=
                                            </div>
                                            <div class="col-md-3 form-group">
                                                <input type="hidden" value="${preLoadLogicBean.id}" class="id"
                                                       name="preLoadLogicBeans[${status.index}].id">
                                                <input type="text" required class="form-control value req-pll"
                                                       value="${preLoadLogicBean.inputValue}" id="value${status.index}"
                                                       name="preLoadLogicBeans[${status.index}].inputValue"
                                                       placeholder="Enter">
                                                <div class="help-block with-errors red-txt"></div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <br>
                            </div>
                        </c:forEach>
                    </c:when>

                    <c:otherwise>
                        <div style="height: 136px" class="form-div">
                            <div class="row formula-box">
                                <div class="col-md-2">
                                    <strong class="font-family: arial;">Formula</strong>
                                </div>
                            </div>
                            <div style="height: 100px; border:1px solid #bfdceb;">
                                <div class="row">
                                    <div class="col-md-3 gray-xs-f mb-xs" style="padding-top: 18px;">Define Functions
                                    </div>
                                    <div class="col-md-3 gray-xs-f mb-xs" style="padding-top: 18px;">Define Inputs</div>
                                    <div class="col-md-6"></div>
                                </div>
                                <div class="row data-div">
                                    <div class="col-md-1" style="padding-top: 7px">Operator</div>
                                    <div class="col-md-2 parent-pll form-group">
                                        <select required class="selectpicker operator text-normal req-pll"
                                                id="operator0" name="preLoadLogicBeans[0].operator" title="-select-">
                                            <c:forEach items="${operators}" var="operator">
                                                <option value="${operator}">${operator}</option>
                                            </c:forEach>
                                        </select>
                                        <div class="help-block with-errors red-txt"></div>
                                    </div>

                                    <div class="col-md-1" style="padding-top: 7px">Value&nbsp;&nbsp;&nbsp;=</div>
                                    <div class="col-md-3 form-group">
                                        <input type="hidden" id="id${status.index}">
                                        <input type="text" required class="form-control value req-pll" id="value0"
                                               name="preLoadLogicBeans[0].inputValue" placeholder="Enter">
                                        <div class="help-block with-errors red-txt"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <br>
                    </c:otherwise>
                </c:choose>
            </div>
            <button type="button" id="addFormula" style="margin-top:10px" class="btn btn-primary blue-btn">Add Formula
            </button>
        </div>
    </div>


        <!-- End right Content here -->

        <div class="modal fade dominate" id="myModal" role="dialog" style="z-index: 1301 !important;">
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

</form:form>

<form:form action="/fdahpStudyDesigner/sessionOut.do"
           id="backToLoginPage" name="backToLoginPage"
           method="post" data-toggle="validator" role="form">
</form:form>

<script>
 var idleTime = 0;
 $(document).ready(function () {
     $(".menuNav li.active").removeClass('active');
     $(".seventhQuestionnaires").addClass('active');

       $("#saveId").click(function () {
        saveAddGroupsPage('manual');
               });
                    setInterval(function () {
                           idleTime += 1;
                           if (idleTime > 3) { // 5 minutes
                           <c:if test="${actionType ne 'view'}">
                               saveAddGroupsPage('auto');
                               </c:if>
                               <c:if test="${actionType eq 'view'}">
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
                                   $('#myModal').modal('show');
                                   let i = 1;
                                   let j = 14;
                                   let lastSavedInterval = setInterval(function () {
                                       if ((i === 15) || (j === 0)) {
                                            $('#autoSavedMessage').html('<div class="blue_text">Last saved was ' + i + ' minutes ago</div><div class="grey_txt"><span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in <span class="bold_txt"> ' + j +' minutes</span></div>');
                                           if ($('#myModal').hasClass('show')) {
                                               $('#backToLoginPage').submit();
                                           }
                                           clearInterval(lastSavedInterval);
                                       } else {
                                           if ((i === 1) || (j === 14)) {
                                           $('#autoSavedMessage').html('<div class="blue_text">Last saved was 1 minute ago</div><div class="grey_txt"><span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in <span class="bold_txt"> 14 minutes</span></div>');
                                           }
                                           else if ((i === 14) || (j === 1)) {
                                           $('#autoSavedMessage').html('<div class="blue_text">Last saved was 14 minutes ago</div><div class="grey_txt"><span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in <span class="bold_txt"> 1 minute</span></div>');
                                           }
                                           else {
                                           $('#autoSavedMessage').html('<div class="blue_text">Last saved was ' + i + ' minutes ago</div><div class="grey_txt"><span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in <span class="bold_txt"> ' + j +' minutes</span></div>');
                                           }
                                           idleTime = 0;
                                           i+=1;
                                           j-=1;
                                       }
                                   }, 60000);
                               }

       });


    function saveAddGroupsPage(mode) {
        let isValid = true;
        $('.req').each(function () {
            let parent = $(this).parent();
            if ($(this).val() === '') {
                isValid = false;
                parent.addClass('has-error has-danger').find(".help-block")
                    .empty()
                    .append($("<ul><li> </li></ul>")
                        .attr("class","list-unstyled")
                        .text("Please fill out this field.")).ScrollTo();
            } else {
                if (parent.hasClass('has-error has-danger')) {
                    parent.removeClass('has-error has-danger').find(".help-block").empty();
                }
            }
        });
        $("#action").val('false');
        $("#buttonText").val('save');
        $('#actionType').val('edit');
        if (mode === 'auto') {
            $("#isAutoSaved").val('true');
        } else {
            $("#isAutoSaved").val('false');
        }
        $('#addGroupFormId').submit();
    }

 let currLang = $('#studyLanguage').val();
 if (currLang !== undefined && currLang !== null && currLang !== '' && currLang !== 'en') {
   $('#currentLanguage').val(currLang);
   refreshAndFetchLanguageData(currLang);
 }

function goToBackPage(item) {
var actionPage = "${actionType}";
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
let lang = ($('#studyLanguage').val()!==undefined)?$('#studyLanguage').val():'';
a.href = "/fdahpStudyDesigner/adminStudies/viewGroups.do?_S=${param._S}&actionType=${actionType}&language="
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
let lang = ($('#studyLanguage').val()!==undefined)?$('#studyLanguage').val():'';
a.href = "/fdahpStudyDesigner/adminStudies/viewGroups.do?_S=${param._S}&language="
+ lang;
document.body.appendChild(a).click();
</c:if>
}
         $('#preLoadSurveyId').on('change', function () {
         	refreshSourceKeys();
         })

         $('#chkSelect').on('change', function(e) {
             if($(this).is(':checked')) {
                 $('#contents').show();
             } else {
                 $('#contents').hide();
             }
         });



         $("#groupId").blur(function () {
        	    validateGroupId('', function (val) {
        	    });
        	  });

         $("#groupName").blur(function () {
        	 validateGroupName('', function (val) {
        	    });
        	  });

    $('#addFormula').on('click', function () {
        let formContainer = $('#formulaContainer');
        let count = formContainer.find('div.formula-box').length;
        let formula =
            '<div id="form-div' + count + '" class="form-div deletable" style="height: 200px; margin-top:20px">' +
            '<div class="form-group">' +
            '<span class="radio radio-info radio-inline p-45 pl-2">' +
            '<input type="radio" id="andRadio' + count + '" value="&&" class="con-radio con-op-and" name="preLoadLogicBeans[' + count + '].conditionOperator" checked/>' +
            '<label for="andRadio' + count + '">AND</label>' +
            '</span>' +
            '<span class="radio radio-inline">' +
            '<input type="radio" id="orRadio' + count + '" value="||" class="con-radio con-op-or" name="preLoadLogicBeans[' + count + '].conditionOperator" />' +
            '<label for="orRadio' + count + '">OR</label>' +
            '</span>' +
            '</div>' +
            '<div style="height: 150px">' +
            '<div class="row formula-box">' +
            '<div class="col-md-2"><strong class="font-family: arial;">Formula</strong></div>' +
            '<div class="col-md-10 text-right">' +
            '<span class="delete vertical-align-middle remBtnDis hide pl-md align-span-center" data-id="form-div' + count + '" onclick="removeFormulaContainer(this)"></span>' +
            '</div>' +
            '</div>' +
            '<div style="height: 100px; border:1px solid #bfdceb;">' +
            '<div class="row">' +
            '<div class="col-md-3 gray-xs-f mb-xs" style="padding-top: 18px;">Define Functions</div>' +
            '<div class="col-md-3 gray-xs-f mb-xs" style="padding-top: 18px;">Define Inputs</div>' +
            '<div class="col-md-6"></div>' +
            '</div>' +
            '<div class="row data-div">' +
            '<div class="col-md-1" style="padding-top: 7px">Operator</div>' +
            '<div class="col-md-2 parent-pll form-group">' +
            '<select required class="selectpicker operator text-normal req-pll" ' +
            'id="operator' + count + '" name="preLoadLogicBeans[' + count + '].operator" title="-select-">' +
            '<option> < </option>' +
            '<option> > </option>' +
            '<option> = </option>' +
            '<option> != </option>' +
            '<option> >= </option>' +
            '<option> <= </option>' +
            '</select> <div class="help-block with-errors red-txt"></div>' +
            '</div>' +
            '<div class="col-md-1" style="padding-top: 7px">Value&nbsp;&nbsp;&nbsp;= </div>' +
            '<div class="col-md-3 form-group">' +
            '<input type="hidden" class="id"/>' +
            '<input type="text" required class="form-control value req-pll" id="value' + count + '" name="preLoadLogicBeans[' + count + '].inputValue" placeholder="Enter"> <div class="help-block with-errors red-txt"></div>' +
            '</div>' +
            '</div>' +
            '</div>' +
            '</div>' +
            '</div>';
        formContainer.append(formula);
        $('.selectpicker').selectpicker();
    });

         let defaultVisibility = $('#groupDefaultVisibility');
         if (defaultVisibility.is(':checked')) {
             $('.deletable').remove();
             $('#logicDiv').find('div.bootstrap-select, input, select').each( function () {
                 $(this).addClass('ml-disabled');
                 if ($(this).is("input.con-radio")) {
                     $(this).attr('disabled', true);
                 }
             });
             $('#defaultVisibility').val('true');
             $('#addFormula').attr('disabled', true);
             $('#value0').attr('disabled', true);
             $('#operator0').attr('disabled', true);
            //  $('#operator0').parent().addClass('ml-disabled');

         }

         defaultVisibility.on('change', function () {
             let toggle = $(this);
             let logicDiv = $('#logicDiv');
             let addForm = $('#addFormula');
             if  (toggle.is(':checked')) {
                 $('.deletable').remove();
                 logicDiv.find('div.bootstrap-select, input, select').each( function () {
                     $(this).addClass('ml-disabled');
                     if ($(this).is("input.con-radio")) {
                         $(this).attr('disabled', true);
                     }
                        $(this).attr('required', false);
                        $(this).removeClass('has-error has-danger').find(".help-block").empty();
                        $(this).parent().parent().removeClass('has-error has-danger').find(".help-block").empty();
                        $(this).parent().removeClass('has-error has-danger').find(".help-block").empty();
                 });
                 $('#defaultVisibility').val('true');
                 $('#destinationTrueAsGroup, #preLoadSurveyId, #value0, #operator0').val('').selectpicker('refresh');
                 $('#differentSurveyPreLoad').attr('checked', false).attr('disabled', true);
                 addForm.attr('disabled', true);
                 $('#value0').attr('disabled', true);
                 $('#operator0').attr('disabled', true);
                 $('#operator0').parent().addClass('disabled');
                 $('#operator0').siblings().addClass('disabled');
             } else {
                 logicDiv.find('div.bootstrap-select, input, select').each( function () {
                     $(this).removeClass('ml-disabled');
                     if ($(this).is("input.con-radio")) {
                         $(this).attr('disabled', false);
                     }
                     $(this).attr('required','required');
                     $(this).parent().removeClass('has-error has-danger').find(".help-block").empty();

                 });
                 toggle.attr('checked', false);
                 $('#defaultVisibility').val('false');
                 addForm.attr('disabled', false);
                 $('#value0').attr('disabled', false);
                 $('#operator0').attr('disabled', false);
                 $('#operator0').parent().removeClass('disabled');
                 $('#operator0').siblings().removeClass('disabled');
             }
         })

    function removeFormulaContainer(object) {
        let id = object.getAttribute('data-id');
        $('#' + id).remove();
    }

    $("#doneGroupId").click(function () {
        if (isFormValid()) {
            $('#buttonText').val('done');
            $("#action").val('true');
            $('#doneGroupId').prop('disabled', true);
            $('#addGroupFormId').submit();
        }
    });


         function validateGroupId(item, callback) {
        	    var groupId = $("#groupId").val();
        	    var thisAttr = $("#groupId");
        	    var existedKey = $("#preGroupId").val();
        	    if (groupId != null && groupId != ''
        	        && typeof groupId != 'undefined') {
        	      $(thisAttr).parent().removeClass("has-danger").removeClass(
        	          "has-error");
        	      $(thisAttr).parent().find(".help-block").empty();
        	      if (existedKey != groupId) {
        	        $
        	        .ajax({
        	          url: "/fdahpStudyDesigner/adminStudies/validateGroupIdKey.do?_S=${param._S}",
        	          type: "POST",
        	          datatype: "json",
        	          data: {
        	            groupId: groupId
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
        	                  + groupId
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

         function validateGroupName(item, callback) {
        	    var groupName = $("#groupName").val();
        	    var thisAttr = $("#groupName");
        	    var existedKey = $("#preGroupName").val();
        	    if (groupName != null && groupName != ''
        	        && typeof groupName != 'undefined') {
        	      $(thisAttr).parent().removeClass("has-danger").removeClass(
        	          "has-error");
        	      $(thisAttr).parent().find(".help-block").empty();
        	      if (existedKey != groupName) {
        	        $
        	        .ajax({
        	          url: "/fdahpStudyDesigner/adminStudies/validateGroupName.do?_S=${param._S}",
        	          type: "POST",
        	          datatype: "json",
        	          data: {
        	        	  groupName: groupName
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
        	                  + groupName
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

         $('#studyLanguage').on('change', function () {
              let currLang = $('#studyLanguage').val();
              $('#currentLanguage').val(currLang);
              refreshAndFetchLanguageData($('#studyLanguage').val());
            })

            function refreshAndFetchLanguageData(language) {
      		    $.ajax({
      		      url: '/fdahpStudyDesigner/adminStudies/addOrEditGroupsDetails.do?_S=${param._S}',
      		      type: "GET",
      		      data: {
      		        language: language
      		      },
      		      success: function (data) {
      		        let htmlData = document.createElement('html');
      		        htmlData.innerHTML = data;
      		        if ($('#actionType').val() === 'edit'){
      		        if (language !== 'en') {
      		          updateCompletionTicks(htmlData);
      		          $('.tit_wrapper').text($('#mlName', htmlData).val());
      		          $('#groupName').attr('disabled', true);
      		          $('#groupId').attr('disabled', true);
      		 	      $('#groupDefaultVisibility').attr('disabled', true);
                         $('#addFormula').attr('disabled', true);

                         $('#saveId').attr('disabled', true);
                         $('#doneGroupId').attr('disabled', true);
                         $('.radio').addClass('disabled');
                         $('.remBtnDis').addClass('disabled');


                         $('#logicDiv').find('div.bootstrap-select, input').each( function () {
					$(this).addClass('ml-disabled');
					if ($(this).is("input")) {
						$(this).attr('disabled', true);
					}
				});
                $('.data-div').find('div.bootstrap-select, input').each( function () {
					$(this).addClass('ml-disabled');
					if ($(this).is("input")) {
						$(this).attr('disabled', true);
					}
				});

      		          let mark=true;
      		          $('#groups_list option', htmlData).each(function (index, value) {
      		            let id = '#row' + value.getAttribute('id');
      		            $(id).find('td.title').text(value.getAttribute('value'));
      		          });
      		        } else

                    { // for english
      		          updateCompletionTicksForEnglish();
      		          $('.tit_wrapper').text($('#customStudyName', htmlData).val());
      		          $('#groupName').attr('disabled', false);
      		          $('#groupId').attr('disabled', false);
      		          $('#groupDefaultVisibility').attr('disabled', false);
      		          $('#studyProtocolId').prop('disabled', false);
                        $('#addFormula').attr('disabled', false);

                        $('#saveId').attr('disabled', false);
                         $('#doneGroupId').attr('disabled', false);

                         $('.radio').removeClass('disabled');
                         $('.remBtnDis').removeClass('disabled');

                        $('#logicDiv').find('div.bootstrap-select, input').each( function () {
					$(this).removeClass('ml-disabled');
					if ($(this).is("input")) {
						$(this).attr('disabled', false);
					}
				});
                $('.data-div').find('div.bootstrap-select, input').each( function () {
					$(this).removeClass('ml-disabled');
					if ($(this).is("input")) {
						$(this).attr('disabled', false);
					}
				});

      		          let mark=true;
      		          $('tbody tr', htmlData).each(function (index, value) {
      		            let id = '#'+value.getAttribute('id');
      		            $(id).find('td.title').text($(id, htmlData).find('td.title').text());
      		          });

      		          <c:if test="${not empty permission}">
      		          $('.delete').addClass('cursor-none');
      		          </c:if>

                        let defaultVisibility = $('#groupDefaultVisibility');
         if (defaultVisibility.is(':checked')) {
             $('#logicDiv').find('div.bootstrap-select, input, select').each( function () {
                 $(this).addClass('ml-disabled');
                 if ($(this).is("input.con-radio")) {
                     $(this).attr('disabled', true);
                 }
             });
             $('#defaultVisibility').val('true');
             $('#addFormula').attr('disabled', true);
             $('#value0').attr('disabled', true);
             $('#operator0').attr('disabled', true);
            //  $('#operator0').parent().addClass('ml-disabled');

         }

      		        }
      		        }
      		      }
      		    });
      		  }

    let formContainer = $('#formulaContainer');
    formContainer.on('change', function (e) {
        let element = e.target;
        let parent = $(element).parent();
        if ($(element).is('select')) {
            parent = $(element).closest('div.parent-pll');
        }
        if ($(element).val() === '') {
            parent.addClass('has-error has-danger').find(".help-block")
                .empty()
                .append($("<ul><li> </li></ul>")
                    .attr("class","list-unstyled")
                    .text("Please fill out this field."));
        } else {
            if (parent.hasClass('has-error has-danger')) {
                parent.removeClass('has-error has-danger').find(".help-block").empty();
            }
        }
    });

    formContainer.on('keypress', function (e) {
        let element = e.target;
        if ($(element).is('input')) {
            let parent = $(element).parent();
            if (parent.hasClass('has-error has-danger')) {
                parent.removeClass('has-error has-danger').find(".help-block").empty();
            }
        }
    });

 $('.req').on('keypress', function (e) {
     let element = e.target;
     if ($(element).is('input')) {
         let parent = $(element).parent();
         if (parent.hasClass('has-error has-danger')) {
             parent.removeClass('has-error has-danger').find(".help-block").empty();
         }
     }
 });

    function isFormValid() {
        let classnames;
        let valid = true;
        if ($('#groupDefaultVisibility').is(':checked')) {
            classnames = 'select.req, input.req';

        } else {
            classnames = 'select.req, input.req, select.req-pll, input.req-pll';
        }
        $(classnames).each(function () {
            let parent = $(this).parent();
            if ($(this).is('select')) {
                parent = $(this).closest('div.parent-pll');
            }
            if ($(this).val() === '') {
                valid = false;
                parent.addClass('has-error has-danger').find(".help-block")
                    .empty()
                    .append($("<ul><li> </li></ul>")
                        .attr("class","list-unstyled")
                        .text("Please fill out this field.")).ScrollTo();
            } else {
                if (parent.hasClass('has-error has-danger')) {
                    parent.removeClass('has-error has-danger').find(".help-block").empty();
                }
            }
        });
        return valid;
    }

</script>