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
name="addGroupFormId" id="addGroupFormId" method="post">

<input type="hidden" id="actionType" name="actionType"
                           value="${fn:escapeXml(actionType)}">
                    <input type="hidden" id="buttonText" name="buttonText"
                           value="">
        			<input type="hidden" value="${groupsBean.action}" id="action" name="action"> 
				<input type="hidden" value="" id="buttonText" value="${id}" name="buttonText">
                           
                    <input type="hidden" id="isAutoSaved" value="${isAutoSaved}" name="isAutoSaved"/>

          <div class="col-sm-10 col-rc white-bg p-none">
            <!--  Start top tab section-->
            <!-- <div class="right-content-head"><div class="text-right"><div class="black-md-f dis-line pull-left line34"><span class="mr-sm"><a href="#"><img src="images/icons/back-b.png"/></a></span> Group
                    </div></div></div> -->
            <div class="right-content-head">
              <div class="text-right">
           <div class="black-md-f dis-line pull-left line34">
                <span class="pr-sm cur-pointer" onclick="goToBackPage(this);">
                <img src="../images/icons/back-b.png" class="pr-md"/></span>
                Group-Level Attributes
            </div>
                <div class="dis-line form-group mb-none mr-sm" style="width: 150px;">
                  <span class="tool-tip" id="markAsTooltipId" data-toggle="tooltip" data-placement="bottom" title="Language selection is available in edit screen only">
                    <select class="selectpicker aq-select aq-select-form studyLanguage langSpecific" title="Select" disabled>
                      <option selected>English</option>
                    </select>
                  </span>
                </div>
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
                      <input  type="text" custAttType="cust" type="text" class="form-control" placeholder="Enter group ID"  name ="groupId" id="groupId" value="${fn:escapeXml(groupsBo.groupId)}" required>

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
                      <input type="text" custAttType="cust" type="text" class="form-control" placeholder="Enter group name" name ="groupName" id="groupName" value="${fn:escapeXml(groupsBo.groupName)}" required>
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
                             <input type="hidden" id="defaultVisibility" name="groupDefaultVisibility" value="${groupsBo.groupDefaultVisibility}"/>
                             <label class="switch bg-transparent mt-xs">
                                 <input type="checkbox" class="switch-input"
                                        id="groupDefaultVisibility"
                                 <c:if test="${empty groupsBo.groupDefaultVisibility || groupsBo.groupDefaultVisibility eq 'true'}"> checked</c:if>>
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

                                              <div class="col-md-5"></div>
                                          </div>
                                                 <div class="row">
                                                         <div class="col-md-4"></div>

                                                         <div class="col-md-3"></div>
                                                         </div>
                                                                             <br>
                                                                             <div class="row">
                                                                              <div class="col-md-4"></div>
                                                                               <div class="col-md-5">
                                 <select name="destinationTrueAsGroup" id="destinationTrueAsGroup"
                                         data-error="Please choose one option" class="selectpicker text-normal" required title="-select-">
                                     <c:forEach items="${destinationStepList}" var="destinationStep">
                                         <option value="${destinationStep.stepId}"
                                             ${groupsBo.destinationTrueAsGroup eq destinationStep.stepId ? 'selected' :''}>
                                             Step ${destinationStep.sequenceNo} :${destinationStep.stepShortTitle}
                                         </option>
                                     </c:forEach>
                                     <c:forEach items="${groupsList}" var="group" varStatus="status">
                                           <option value="${group.groupId}" id="selectGroup${group.groupId}">Group ${status.index + 1} :  ${group.groupName}&nbsp;</option>
                                     </c:forEach>
                                     <option value="0"
                                         ${groupsBo.destinationTrueAsGroup eq 0 ? 'selected' :''}>
                                         Completion Step</option>
                                 </select>
                             </div>
                             <div class="col-md-3"></div>
                         </div>
                         </div>
                         <br>

                         <div id="formulaContainer${status.index}">
                             <c:choose>
                                 <c:when test="${groupsBo.preLoadLogicBeans.size() gt 0}">
                                     <c:forEach items="${groupsBo.preLoadLogicBeans}" var="preLoadLogicBean" varStatus="status">
                                         <div id="form-div${status.index}"
                                              <c:if test="${status.index gt 0}">style="height: 200px; margin-top:20px"</c:if>
                                              <c:if test="${status.index eq 0}">style="height: 150px;"</c:if> class="form-div">
                                             <c:if test="${status.index gt 0}">
                                                 <div class="form-group">
            												<span class="radio radio-info radio-inline p-45 pl-2">
            													<input type="radio" id="andRadio${status.index}" value="&&" class="con-radio con-op-and" name="preLoadLogicBeans[${status.index}].conditionOperator"
                                                                       <c:if test="${preLoadLogicBean.conditionOperator eq '&&'}">checked </c:if> />
            													<label for="andRadio${status.index}">AND</label>
            												</span>
                                                     <span class="radio radio-inline">
            													<input type="radio" id="orRadio${status.index}" value="||" class="con-radio con-op-or" name="preLoadLogicBeans[${status.index}].conditionOperator"
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
                                                             <span class="delete vertical-align-middle remBtnDis hide pl-md align-span-center" data-id="form-div${status.index}" onclick="removeFormulaContainer(this)"></span>
                                                         </c:if>
                                                     </div>
                                                 </div>
                                                 <div style="height: 100px; border:1px solid #bfdceb;">
                                                     <div class="row">
                                                         <div class="col-md-3 gray-xs-f mb-xs" style="padding-top: 18px;">Define Functions</div>
                                                         <div class="col-md-3 gray-xs-f mb-xs" style="padding-top: 18px;">Define Inputs</div>
                                                         <div class="col-md-6"></div>
                                                     </div>
                                                     <div class="row data-div">
                                                         <div class="col-md-1" style="padding-top: 7px">Operator</div>
                                                         <div class="col-md-2">
                                                             <select class="selectpicker operator text-normal" required
                                                                     id="operator${status.index}" name="preLoadLogicBeans[${status.index}].operator" title="-select-">
                                                                 <c:forEach items="${operators}" var="operator">
                                                                     <option value="${operator}" ${preLoadLogicBean.operator eq operator ?'selected':''}>${operator}</option>
                                                                 </c:forEach>
                                                             </select>
                                                         </div>

                                                         <div class="col-md-1" style="padding-top: 7px">Value&nbsp;&nbsp;&nbsp;= </div>
                                                         <div class="col-md-3">
                                                             <input type="hidden" value="${preLoadLogicBean.id}" class="id" name="preLoadLogicBeans[${status.index}].id" >
                                                             <input type="text" required class="form-control value" value="${preLoadLogicBean.inputValue}" id="value${status.index}" name="preLoadLogicBeans[${status.index}].inputValue" placeholder="Enter">
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
                                                 <div class="col-md-3 gray-xs-f mb-xs" style="padding-top: 18px;">Define Functions</div>
                                                 <div class="col-md-3 gray-xs-f mb-xs" style="padding-top: 18px;">Define Inputs</div>
                                                 <div class="col-md-6"></div>
                                             </div>
                                             <div class="row data-div">
                                                 <div class="col-md-1" style="padding-top: 7px">Operator</div>
                                                 <div class="col-md-2">
                                                     <select required class="selectpicker operator text-normal"
                                                             id="operator0" name="preLoadLogicBeans[0].operator" title="-select-">
                                                         <c:forEach items="${operators}" var="operator">
                                                             <option value="${operator}">${operator}</option>
                                                         </c:forEach>
                                                     </select>
                                                 </div>

                                                 <div class="col-md-1" style="padding-top: 7px">Value&nbsp;&nbsp;&nbsp;= </div>
                                                 <div class="col-md-3">
                                                     <input type="hidden" id="id${status.index}">
                                                     <input type="text" required class="form-control value" id="value0" name="preLoadLogicBeans[0].inputValue" placeholder="Enter">
                                                 </div>
                                             </div>
                                         </div>
                                     </div>
                                     <br>
                                 </c:otherwise>
                             </c:choose>
                         </div>
                         <button type="button" id="addFormula" style="margin-top:10px" class="btn btn-primary blue-btn">Add Formula</button>
                     </div>
                 </div>
	</div>


        <!-- End right Content here -->
</form:form>

<script>
 $(document).ready(function () {
     $(".menuNav li.active").removeClass('active');
     $(".seventhQuestionnaires").addClass('active');
  $("#saveId").click(function () {
  var questnId = $('#questionnaireId').val();
     var groupId = $('#groupId').val();
      var groupName = $('#groupName').val();
      var defaultVisibility = $('#groupDefaultVisibility').val();
      var destinationTrueAsGroup = $('#destinationTrueAsGroup').val();

     var id =  $('#id').val();
         if(groupId != '' && groupId != null && typeof groupId != 'undefined' && groupName != '' && groupName != null && typeof groupName != 'undefined'){
                 $("#action").val('false');

                 $('#id').val();
                 $('#groupId').val();
                 $("#groupName").val();
                 $("#groupDefaultVisibility").val();
                 $("#destinationTrueAsGroup").val();
                 $("#buttonText").val('save');
                 $("#isAutoSaved").val('true');
                 $('#addGroupFormId').submit();
                 showSucMsg("Content saved as draft.");
                 }
                 else
                 {
                 $("#alertMsg").removeClass('s-box').addClass('e-box').text(
                      "Please fill out this all the mandatory fields");
                  $('#alertMsg').show();
                 }
                 setTimeout(hideDisplayMessage, 4000);

               });
       });

         function goToBackPage(item) {
                 var a = document.createElement('a');
                     a.href = "/fdahpStudyDesigner/adminStudies/viewGroups.do?_S=${param._S}";
                     document.body.appendChild(a).click();
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
                 '<div id="form-div' + count + '" class="form-div" style="height: 200px; margin-top:20px">'+
                 '<div class="form-group">'+
                 '<span class="radio radio-info radio-inline p-45 pl-2">'+
                 '<input type="radio" id="andRadio' + count + '" value="&&" class="con-radio con-op-and" name="preLoadLogicBeans['+count+'].conditionOperator" checked/>'+
                 '<label for="andRadio' + count + '">AND</label>'+
                 '</span>'+
                 '<span class="radio radio-inline">'+
                 '<input type="radio" id="orRadio' + count + '" value="||" class="con-radio con-op-or" name="preLoadLogicBeans['+count+'].conditionOperator" />'+
                 '<label for="orRadio' + count + '">OR</label>'+
                 '</span>'+
                 '</div>'+
                 '<div style="height: 150px">'+
                 '<div class="row formula-box">'+
                 '<div class="col-md-2"><strong class="font-family: arial;">Formula</strong></div>'+
                 '<div class="col-md-10 text-right">'+
                 '<span class="delete vertical-align-middle remBtnDis hide pl-md align-span-center" data-id="form-div' + count + '" onclick="removeFormulaContainer(this)"></span>'+
                 '</div>'+
                 '</div>'+
                 '<div style="height: 100px; border:1px solid #bfdceb;">'+
                 '<div class="row">'+
                 '<div class="col-md-3 gray-xs-f mb-xs" style="padding-top: 18px;">Define Functions</div>'+
                 '<div class="col-md-3 gray-xs-f mb-xs" style="padding-top: 18px;">Define Inputs</div>'+
                 '<div class="col-md-6"></div>'+
                 '</div>'+
                 '<div class="row data-div">'+
                 '<div class="col-md-1" style="padding-top: 7px">Operator</div>'+
                 '<div class="col-md-2">'+
                 '<select required class="selectpicker operator text-normal" '+
                 'id="operator' + count + '" name="preLoadLogicBeans['+count+'].operator" title="-select-">'+
                 '<option> < </option>'+
                 '<option> > </option>'+
                 '<option> = </option>'+
                 '<option> != </option>'+
                 '<option> >= </option>'+
                 '<option> <= </option>'+
                 '</select>'+
                 '</div>'+
                 '<div class="col-md-1" style="padding-top: 7px">Value&nbsp;&nbsp;&nbsp;= </div>'+
                 '<div class="col-md-3">'+
                 '<input type="hidden" class="id"/>'+
                 '<input type="text" required class="form-control value" id="value' + count + '" name="preLoadLogicBeans['+count+'].inputValue" placeholder="Enter">'+
                 '</div>'+
                 '</div>'+
                 '</div>'+
                 '</div>'+
                 '</div>';
             formContainer.append(formula);
             $('.selectpicker').selectpicker();
         });

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
         }

         defaultVisibility.on('change', function () {
             let toggle = $(this);
             let logicDiv = $('#logicDiv');
             let addForm = $('#addFormula');
             if  (toggle.is(':checked')) {
                 logicDiv.find('div.bootstrap-select, input, select').each( function () {
                     $(this).addClass('ml-disabled');
                     if ($(this).is("input.con-radio")) {
                         $(this).attr('disabled', true);
                     }
                 });
                 $('#defaultVisibility').val('true');
                 addForm.attr('disabled', true);
             } else {
                 logicDiv.find('div.bootstrap-select, input, select').each( function () {
                     $(this).removeClass('ml-disabled');
                     if ($(this).is("input.con-radio")) {
                         $(this).attr('disabled', false);
                     }
                 });
                 toggle.attr('checked', false);
                 $('#defaultVisibility').val('false');
                 addForm.attr('disabled', false);
             }
         })

         function removeFormulaContainer(object) {
             let id = object.getAttribute('data-id');
             $('#'+id).remove();
         }
         
         $("#doneGroupId").click(function () {
        	  var questnId = $('#questionnaireId').val();
        	  var id = $('#id').val();
        	   var groupId = $('#groupId').val();
        	    var groupName = $('#groupName').val();
        	    var defaultVisibility = $('#groupDefaultVisibility').val();
                var destinationTrueAsGroup = $('#destinationTrueAsGroup').val();
        	    $('input.con-radio').each(function(e) {
                                  $(this).removeAttr('disabled');
                              })
        		if(isFromValid('#id')){
        	       if(groupId != '' && groupId != null && typeof groupId != 'undefined' && groupName != '' && groupName != null && typeof groupName != 'undefined'){
        	    	   $('#buttonText').val('done');
                       $("#action").val('true');
        			$('#doneGroupId').prop('disabled',true);
        	               $('#id').val();
        	               $('#groupId').val();
        	               $("#groupName").val();
        	               $("#groupDefaultVisibility").val();
                           $("#destinationTrueAsGroup").val();
        	               $('#addGroupFormId').submit();

        	               }
        	               else
        	               {
        	               $("#alertMsg").removeClass('s-box').addClass('e-box').text(
        	                    "Please fill out this all the mandatory fields");
        	                $('#alertMsg').show();
        	               }
        	               setTimeout(hideDisplayMessage, 4000);
        		}
        			else{
        	            		$('#doneStudyId').prop('disabled',false);
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
        	      if (existedKey != preGroupId) {
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
        	      if (existedKey != preGroupName) {
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




    </script>