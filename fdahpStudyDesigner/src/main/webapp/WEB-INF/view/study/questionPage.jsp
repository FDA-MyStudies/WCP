<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script type="text/javascript">
function isNumber(evt) {
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
    		return false;
    }
    return true;
}
/* function isOnlyNumber(elem) {
	var re = /^-?\d*\.?\d{0,6}$/; 
	var text = $(elem).val();
	console.log("text:"+text);
    if (text.match(re) !== null) {
       return true;
    }
    return false;
} */
function isOnlyNumber(evt) {
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
       console.log("charCode:"+charCode);
       if(charCode != 45){
        	return false;
        }
        
    }
    return true;
}
</script>
<style>
.tooltip {
  width: 175px;
}
</style>
<!-- Start right Content here -->
<div class="col-sm-10 col-rc white-bg p-none">
   <!--  Start top tab section-->
   <div class="right-content-head">
      <div class="text-right">
         <div class="black-md-f dis-line pull-left line34">
            <span class="mr-sm cur-pointer" onclick="goToBackPage(this);"><img src="../images/icons/back-b.png"/></span>
            <c:if test="${actionTypeForFormStep == 'edit'}">Edit Question Step</c:if>
         	<c:if test="${actionTypeForFormStep == 'view'}">View Question Step ${not empty isLive?'<span class="eye-inc ml-sm vertical-align-text-top"></span>':''}</c:if>
         	<c:if test="${actionTypeForFormStep == 'add'}">Add Question Step</c:if>
         </div>
         <div class="dis-line form-group mb-none mr-sm">
            <button type="button" class="btn btn-default gray-btn" onclick="goToBackPage(this);">Cancel</button>
         </div>
         <c:if test="${actionTypeForFormStep ne 'view'}">
	         <div class="dis-line form-group mb-none mr-sm">
	            <button type="button" class="btn btn-default gray-btn" onclick="saveQuestionStepQuestionnaire(this);">Save</button>
	         </div>
	         <div class="dis-line form-group mb-none">
	            <button type="button" class="btn btn-primary blue-btn" id="doneId">Done</button>
	         </div>
         </c:if>
      </div>
   </div>
   <!--  End  top tab section-->
   <!--  Start body tab section -->
   <form:form action="/fdahpStudyDesigner/adminStudies/saveOrUpdateFromQuestion.do?${_csrf.parameterName}=${_csrf.token}" name="questionStepId" id="questionStepId" method="post" data-toggle="validator" role="form" enctype="multipart/form-data">
   <div class="right-content-body pt-none pl-none pr-none">
      <ul class="nav nav-tabs review-tabs gray-bg">
         <li class="questionLevel active"><a data-toggle="tab" href="#qla">Question-level Attributes</a></li>
         <li class="responseLevel"><a data-toggle="tab" href="#rla">Response-level Attributes</a></li>
      </ul>
      <div class="tab-content pl-xlg pr-xlg">
         <!-- Step-level Attributes--> 
         <input type="hidden" name="stepType" id="stepType" value="Question">
         <input type="hidden" id="type" name="type" value="complete" />
         <input type="hidden" name="id" id="questionId" value="${questionsBo.id}">
         <input type="hidden" id="fromId" name="fromId" value="${formId}" />
         <input type="hidden" name="questionnairesId" id="questionnairesId" value="${questionnaireBo.id}">
         <!---  Form-level Attributes ---> 
         <div id="qla" class="tab-pane fade active in mt-xlg">
            <div class="col-md-6 pl-none">
                  <div class="gray-xs-f mb-xs">Question Short Title or Key  (1 to 15 characters) <span class="requiredStar">*</span><span class="ml-xs sprites_v3 filled-tooltip"  data-toggle="tooltip" title="This must be a human-readable question identifier and unique across all steps of the questionnaire and across all questions belonging to various form steps.In other words, no two questions should have the same short title - whether it belongs to a question step or a form step."></span></div>
                  <div class="form-group mb-none">
                     <input type="text" class="form-control" name="shortTitle" id="shortTitle" value="${fn:escapeXml(
                     questionsBo.shortTitle)}" required maxlength="15"/>
                     <div class="help-block with-errors red-txt"></div>
                     <input  type="hidden"  id="preShortTitleId" value="${fn:escapeXml(
                     questionsBo.shortTitle)}"/>
                  </div>
            </div>
            <div class="col-md-10 p-none">
               <div class="gray-xs-f mb-xs">Text of the question (1 to 250 characters)<span class="requiredStar">*</span><span class="ml-xs sprites_v3 filled-tooltip"  data-toggle="tooltip" title="The question you wish to ask the participant."></span></div>
               <div class="form-group">
                  <input type="text" class="form-control" name="question" id="questionTextId" placeholder="Type the question you wish to ask the participant" value="${fn:escapeXml(
                  questionsBo.question)}" required maxlength="250"/>
                  <div class="help-block with-errors red-txt"></div>
               </div>
            </div>
            <div class="col-md-10 p-none">
               <div class="gray-xs-f mb-xs">Description of the question (1 to 500 characters)</div>
               <div class="form-group">
                  <textarea class="form-control" rows="4" name="description" id="descriptionId" placeholder="Enter a line that describes your question, if needed" maxlength="500">${questionsBo.description}</textarea>
                  <div class="help-block with-errors red-txt"></div>
               </div>
            </div>
            <div class="clearfix"></div>
            <div>
               <div class="gray-xs-f mb-xs">Is this a Skippable Question?</div>
               <div>
                  <span class="radio radio-info radio-inline p-45">
                     <input type="radio" id="skiappableYes" value="Yes" name="skippable"  ${empty questionsBo.skippable  || questionsBo.skippable =='Yes' ? 'checked':''}>
                     <label for="skiappableYes">Yes</label>
                  </span>
                  <span class="radio radio-inline">
                     <input type="radio" id="skiappableNo" value="No" name="skippable" ${questionsBo.skippable=='No' ?'checked':''}>
                     <label for="skiappableNo">No</label>
                  </span>
             </div>
            </div>
            <div class="mt-md">
               <div class="gray-xs-f">Response Type <span class="requiredStar">*</span></div>
               <div class="gray-xs-f mb-xs"><small>The type of interface needed to capture the response</small></div>
               <div class="clearfix"></div>
               <div class="col-md-4 col-lg-3 p-none">
                  <div class="form-group">
                     <select id="responseTypeId" class="selectpicker" name="responseType" required value="${questionsBo.responseType}">
                      <option value=''>Select</option>
                      <c:forEach items="${questionResponseTypeMasterInfoList}" var="questionResponseTypeMasterInfo">
                      	<option value="${questionResponseTypeMasterInfo.id}" ${questionsBo.responseType eq questionResponseTypeMasterInfo.id ? 'selected' : ''}>${questionResponseTypeMasterInfo.responseType}</option>
                      </c:forEach>
                     </select>
                     <div class="help-block with-errors red-txt"></div>
                  </div>
               </div>
            </div>
            <div class="clearfix"></div>
            <div class="row">
               <div class="col-md-6 pl-none">
                  <div class="gray-xs-f mb-xs">Description of response type </div>
                  <div id="responseTypeDescrption">
                     - NA -
                  </div>
               </div>
               <div class="col-md-6">
                  <div class="gray-xs-f mb-xs">Data Type</div>
                  <div id="responseTypeDataType">- NA - </div>
               </div>
            </div>
            <div class="mt-lg mb-lg" id="useAnchorDateContainerId" style="display: none">
               <c:choose>
               	<c:when test="questionsBo.useAnchorDate">
               		<span class="checkbox checkbox-inline">
		               <input type="checkbox" id="useAnchorDateId" name="useAnchorDate" value="true" ${questionsBo.useAnchorDate ? 'checked':''} >
		               <label for="useAnchorDateId"> Use Anchor Date </label>
		            </span>
               	</c:when>
               	<c:otherwise>
               		<span class="tool-tip" data-toggle="tooltip" data-html="true" data-placement="top" <c:if test="${questionnaireBo.frequency ne 'One time' || isAnchorDate}"> title="This field is disabled for one of the following reasons:<br/>1. Your questionnaire is scheduled for a frequency other than 'one-time'<br/>2. There is already another question in the study that has been marked for anchor date<br/>Please make changes accordingly and try again." </c:if> >
		               <span class="checkbox checkbox-inline">
		               <input type="checkbox" id="useAnchorDateId" name="useAnchorDate" value="true" ${questionsBo.useAnchorDate ? 'checked':''} <c:if test="${questionnaireBo.frequency ne 'One time' || isAnchorDate}"> disabled </c:if> >
		               <label for="useAnchorDateId"> Use Anchor Date </label>
		               </span>
	               </span>
               	</c:otherwise>
               </c:choose>
            </div>
            <div class="clearfix"></div>
            <c:if test="${questionnaireBo.frequency ne 'One time'}">
            <div class="mt-lg mb-lg" id="addLineChartContainerId" style="display: none">
               <span class="checkbox checkbox-inline">
               <input type="checkbox" id="addLineChart" name="addLineChart" value="Yes" ${questionsBo.addLineChart eq 'Yes' ? 'checked':''}>
               <label for="addLineChart"> Add response data to line chart on app dashboard </label>
               </span>
            </div>
            <div class="clearfix"></div>
            <div id="chartContainer" style="display: none">
            <div class="col-md-6 p-none">
               <div class="gray-xs-f mb-xs">Time range for the chart <span class="requiredStar">*</span></div>
               <div class="form-group">
                  <select class="selectpicker elaborateClass chartrequireClass" id="lineChartTimeRangeId" name="lineChartTimeRange" value="${questionsBo.lineChartTimeRange}">
                       <option value="" selected disabled>Select</option>
	                   <c:forEach items="${timeRangeList}" var="timeRangeAttr">
	                        <option value="${timeRangeAttr}" ${questionsBo.lineChartTimeRange eq timeRangeAttr ? 'selected':''}>${timeRangeAttr}</option>
	                   </c:forEach>
                  </select>
                  <div class="help-block with-errors red-txt"></div>
               </div>
            </div>
            <div class="clearfix"></div>
            <div>
               <div class="gray-xs-f mb-xs">Allow rollback of chart? <span class="sprites_icon info" data-toggle="tooltip" title="If you select Yes, the chart will be allowed for rollback until the date of enrollment into the study."></span></div>
               <div>
                  <span class="radio radio-info radio-inline p-45">
                  <input type="radio" id="allowRollbackChartYes" value="Yes" name="allowRollbackChart" ${questionsBo.allowRollbackChart eq 'Yes' ? 'checked': ''}>
                  <label for="allowRollbackChartYes">Yes</label>
                  </span>
                  <span class="radio radio-inline">
                  <input type="radio" id="allowRollbackChartNo" value="No" name="allowRollbackChart" ${questionsBo.allowRollbackChart eq 'No' ? 'checked': ''}>
                  <label for="allowRollbackChartNo">No</label>
                  </span>
                  <div class="help-block with-errors red-txt"></div>
               </div>
            </div>
            <div class="clearfix"></div>
            <div class="col-md-4 col-lg-3 p-none">
               <div class="gray-xs-f mb-xs">Title for the chart (1 to 30 characters)<span class="requiredStar">*</span></div>
               <div class="form-group">
                  <input type="text" class="form-control chartrequireClass" name="chartTitle" id="chartTitleId" value="${fn:escapeXml(
                  questionsBo.chartTitle)}" maxlength="30">
                  <div class="help-block with-errors red-txt"></div>
               </div>
            </div>
            </div>
            </c:if>
            <div class="clearfix"></div>
            <div class="bor-dashed mt-sm mb-md" id="borderdashId" style="display:none"></div>
            <div class="clearfix"></div>
            <div class="mb-lg" id="useStasticDataContainerId" style="display: none">
               <span class="checkbox checkbox-inline">
               <input type="checkbox" id= "useStasticData" value="Yes" name="useStasticData" ${questionsBo.useStasticData eq 'Yes' ? 'checked':''}>
               <label for="useStasticData"> Use response data for statistic on dashboard</label>
               </span>
            </div>
            <div class="clearfix"></div>
            <div id="statContainer" style="display: none">
            <div class="col-md-6 col-lg-4 p-none">
               <div class="gray-xs-f mb-xs">Short identifier name (1 to 20 characters)<span class="requiredStar">*</span></div>
               <div class="form-group">
                  <input type="text" class="form-control requireClass" name="statShortName" id="statShortNameId" value="${fn:escapeXml(
                  questionsBo.statShortName)}" maxlength="20">
               	  <div class="help-block with-errors red-txt"></div>
               </div>
            </div>
            <div class="clearfix"></div>
            <div class="col-md-10 p-none">
               <div class="gray-xs-f mb-xs">Display name for the Stat (e.g. Total Hours of Activity Over 6 Months) (1 to 50 characters)<span class="requiredStar">*</span></div>
               <div class="form-group">
                  <input type="text" class="form-control requireClass" name="statDisplayName" id="statDisplayNameId" value="${fn:escapeXml(
                  questionsBo.statDisplayName)}" maxlength="50">
                  <div class="help-block with-errors red-txt"></div>
               </div>
            </div>
            <div class="clearfix"></div>
            <div class="col-md-6 col-lg-4 p-none">
               <div class="gray-xs-f mb-xs">Display Units (e.g. hours) (1 to 15 characters)<span class="requiredStar">*</span></div>
               <div class="form-group">
                  <input type="text" class="form-control requireClass" name="statDisplayUnits" id="statDisplayUnitsId" value="${fn:escapeXml(
                  questionsBo.statDisplayUnits)}" maxlength="15">
                  <div class="help-block with-errors red-txt"></div>
               </div>
            </div>
            <div class="clearfix"></div>
            <div class="col-md-4 col-lg-3 p-none">
               <div class="gray-xs-f mb-xs">Stat Type for image upload <span class="requiredStar">*</span></div>
               <div class="form-group">
                  <select class="selectpicker elaborateClass requireClass" id="statTypeId" title="Select" name="statType">
			         <option value="" selected disabled>Select</option>
			         <c:forEach items="${statisticImageList}" var="statisticImage">
			            <option value="${statisticImage.statisticImageId}" ${questionsBo.statType eq statisticImage.statisticImageId ? 'selected':''}>${statisticImage.value}</option>
			         </c:forEach>
			      </select> 
			      <div class="help-block with-errors red-txt"></div>
               </div>
            </div>
            <div class="clearfix"></div>
            <div class="col-md-10 p-none">
               <div class="gray-xs-f mb-xs">Formula for to be applied <span class="requiredStar">*</span></div>
               <div class="form-group">
                  <select class="selectpicker elaborateClass requireClass" id="statFormula" title="Select" name="statFormula">
			         <option value="" selected disabled>Select</option>
			         <c:forEach items="${activetaskFormulaList}" var="activetaskFormula">
			            <option value="${activetaskFormula.activetaskFormulaId}" ${questionsBo.statFormula eq activetaskFormula.activetaskFormulaId ? 'selected':''}>${activetaskFormula.value}</option>
			         </c:forEach>
			      </select>
			      <div class="help-block with-errors red-txt"></div>
               </div>
            </div>
            <div class="clearfix"></div>
            <div class="col-md-10 p-none">
               <div class="gray-xs-f mb-xs">Time ranges options available to the mobile app user</div>
               <div class="clearfix"></div>
            </div>
            <div class="clearfix"></div>
            <div>
               <div>
                  <span class="mr-lg"><span class="mr-sm"><img src="../images/icons/tick.png"/></span><span>Current Day</span></span>
                  <span class="mr-lg"><span class="mr-sm"><img src="../images/icons/tick.png"/></span><span>Current Week</span></span>
                  <span class="mr-lg"><span class="mr-sm"><img src="../images/icons/tick.png"/></span><span>Current Month</span></span>
                  <span class="txt-gray">(Rollback option provided for these three options)</span>
               </div>
               <!-- <div class="mt-sm">
                  <span class="mr-lg"><span class="mr-sm"><img src="../images/icons/tick.png"/></span><span>Custom Start and End Date</span></span>
               </div> -->
            </div>
		</div>
         </div>
         <!---  Form-level Attributes ---> 
         <div id="rla" class="tab-pane fade mt-xlg">
            <div class="col-md-4 col-lg-4 p-none">
               <div class="gray-xs-f mb-xs">Response Type </div>
               <div class="gray-xs-f mb-xs"><small>The type of interface needed to capture the response</small></div>
               <div class="form-group">
                  <input type="text" class="form-control" id="rlaResonseType" disabled>
               </div>
            </div>
            <div class="clearfix"></div>
            <div class="row">
               <div class="col-md-6 pl-none">
                  <div class="gray-xs-f mb-xs">Description of response type</div>
                  <div id="rlaResonseTypeDescription">
                     - NA -
                  </div>
               </div>
               <div class="col-md-6">
                  <div class="gray-xs-f mb-xs">Data Type</div>
                  <div id="rlaResonseDataType"> - NA -</div>
               </div>
            </div>
            <div class="clearfix"></div>
            <input type="hidden" class="form-control" name="questionReponseTypeBo.responseTypeId" id="questionResponseTypeId" value="${questionsBo.questionReponseTypeBo.responseTypeId}">
            <input type="hidden" class="form-control" name="questionReponseTypeBo.questionsResponseTypeId" id="responseQuestionId" value="${questionsBo.questionReponseTypeBo.questionsResponseTypeId}">
            <input type="hidden" class="form-control" name="questionReponseTypeBo.placeholder" id="placeholderTextId" />
            <input type="hidden" class="form-control" name="questionReponseTypeBo.step" id="stepValueId" />
            <div id="responseTypeDivId">
            <div id="scaleType" style="display: none">
            	<div class="mt-lg">
	               <div class="gray-xs-f mb-xs">Scale Type <span class="requiredStar">*</span></div>
	               <div>
	                  <span class="radio radio-info radio-inline p-45">
	                  <input type="radio" class="ScaleRequired" id="vertical" value="true" name="questionReponseTypeBo.vertical"  ${questionsBo.questionReponseTypeBo.vertical ? 'checked':''} >
	                  <label for="vertical">Vertical</label>
	                  </span>
	                  <span class="radio radio-inline">
	                  <input type="radio" class="ScaleRequired" id="horizontal" value="false" name="questionReponseTypeBo.vertical" ${empty questionsBo.questionReponseTypeBo.vertical || !questionsBo.questionReponseTypeBo.vertical ? 'checked':''} >
	                  <label for="horizontal">Horizontal</label>
	                  </span>
	                  <div class="help-block with-errors red-txt"></div>
	               </div>
	            </div>
            </div>
            <div id="Scale" style="display: none">
            <div class="clearfix"></div>
            <div class="row">
               <div class="col-md-6 pl-none">
                  <div class="col-md-8 col-lg-8 p-none">
                     <div class="gray-xs-f mb-xs">Minimum Value <span class="requiredStar">*</span> <span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" title="Enter an integer number in the range (Min, 10000)."></span></div>
                     <div class="form-group">
                        <input type="text" class="form-control ScaleRequired"  name="questionReponseTypeBo.minValue" id="scaleMinValueId" value="${fn:escapeXml(
                        questionsBo.questionReponseTypeBo.minValue)}" onkeypress="return isOnlyNumber(event)">
                        <div class="help-block with-errors red-txt"></div>
                     </div>
                  </div>
               </div>
               <div class="col-md-6">
                  <div class="col-md-8 col-lg-8 p-none">
                     <div class="gray-xs-f mb-xs">Maximum Value <span class="requiredStar">*</span> <span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" title="Enter an integer number in the range (Min+1, 10000)."></span></div>
                     <div class="form-group">
                        <input type="text" class="form-control ScaleRequired" name="questionReponseTypeBo.maxValue" id="scaleMaxValueId" value="${fn:escapeXml(
                        questionsBo.questionReponseTypeBo.maxValue)}" onkeypress="return isOnlyNumber(event)">
                        <div class="help-block with-errors red-txt"></div>
                     </div>
                  </div>
               </div>
            </div>
            <div class="clearfix"></div>
            <div class="row mt-sm">
               <div class="col-md-6  pl-none">
                  <div class="col-md-8 col-lg-8 p-none">
                     <div class="gray-xs-f mb-xs">Default value (slider position) <span class="requiredStar">*</span> <span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" title="Enter an integer between the minimum and maximum."></span></div>
                     <div class="form-group">
                        <input type="text" class="form-control ScaleRequired" name="questionReponseTypeBo.defaultValue" id="scaleDefaultValueId" value="${fn:escapeXml(
                        questionsBo.questionReponseTypeBo.defaultValue)}" onkeypress="return isOnlyNumber(event)">
                        <div class="help-block with-errors red-txt"></div>
                     </div>
                  </div>
               </div>
               <div class="col-md-6">
               <div class="col-md-4 col-lg-4 p-none mb-lg">
	               <div class="gray-xs-f mb-xs">Number of Steps  <span class="requiredStar">*</span> <span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" title="Specify the number of steps to divide the scale into."></span></div>
	               <div class="form-group">
	                  <input type="text" class="form-control ScaleRequired"  id="scaleStepId" value="${questionsBo.questionReponseTypeBo.step}" onkeypress="return isNumber(event)" maxlength="2">
	                  <div class="help-block with-errors red-txt"></div>
	               </div>
	           </div>
	           </div>
            </div>
            <div class="clearfix"></div>
            <div class="row">
            	<div class="col-md-6 pl-none">
                  <div class="col-md-8 col-lg-8 p-none">
                  	<div class="gray-xs-f mb-xs">Description for minimum value (1 to 20 characters)</div>
	                <div class="form-group">
	                  <input type="text" class="form-control" name="questionReponseTypeBo.minDescription" id="scaleMinDescriptionId" value="${fn:escapeXml(
	                  questionsBo.questionReponseTypeBo.minDescription)}" placeholder="Type the question you wish to ask the participant" maxlength="20"/>
	                  <div class="help-block with-errors red-txt"></div>
	                </div>
                  </div>
                </div>
            	<div class="col-md-6">
                  <div class="col-md-9 col-lg-9 p-none">
                  	<div class="gray-xs-f mb-xs">Description for maximum value (1 to 20 characters)</div>
	                <div class="form-group">
	                  <input type="text" class="form-control" name="questionReponseTypeBo.maxDescription" id="scaleMaxDescriptionId" value="${fn:escapeXml(
	                  questionsBo.questionReponseTypeBo.maxDescription)}" placeholder="Type the question you wish to ask the participant" maxlength="20" />
	                  <div class="help-block with-errors red-txt"></div>
	                </div>
                  </div>
                </div>
            </div>
            </div>
            <div id="ContinuousScale" style="display: none">
            <div class="clearfix"></div>
            <div class="row">
               <div class="col-md-6 pl-none">
                  <div class="col-md-8 col-lg-8 p-none">
                     <div class="gray-xs-f mb-xs">Minimum Value <span class="requiredStar">*</span> <span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" title="Enter an integer number in the range (Min, 10000)."></span></div>
                     <div class="form-group">
                        <input type="text" class="form-control ContinuousScaleRequired"  name="questionReponseTypeBo.minValue" id="continuesScaleMinValueId" value="${questionsBo.questionReponseTypeBo.minValue}" onkeypress="return isOnlyNumber(event)">
                        <div class="help-block with-errors red-txt"></div>
                     </div>
                  </div>
               </div>
               <div class="col-md-6">
                  <div class="col-md-8 col-lg-8 p-none">
                     <div class="gray-xs-f mb-xs">Maximum Value <span class="requiredStar">*</span> <span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" title="Enter an integer number in the range (Min+1, 10000)."></span></div>
                     <div class="form-group">
                        <input type="text" class="form-control ContinuousScaleRequired" name="questionReponseTypeBo.maxValue" id="continuesScaleMaxValueId" value="${questionsBo.questionReponseTypeBo.maxValue}" onkeypress="return isOnlyNumber(event)">
                        <div class="help-block with-errors red-txt"></div>
                     </div>
                  </div>
               </div>
            </div>
            <div class="clearfix"></div>
            <div class="row mt-sm">
               <div class="col-md-6  pl-none">
                  <div class="col-md-8 col-lg-8 p-none">
                     <div class="gray-xs-f mb-xs">Default value (slider position) <span class="requiredStar">*</span> <span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" title="Enter an integer between the minimum and maximum."></span></div>
                     <div class="form-group">
                        <input type="text" class="form-control ContinuousScaleRequired" name="questionReponseTypeBo.defaultValue" id="continuesScaleDefaultValueId" value="${questionsBo.questionReponseTypeBo.defaultValue}" onkeypress="return isOnlyNumber(event)">
                        <div class="help-block with-errors red-txt"></div>
                     </div>
                  </div>
               </div>
               <div class="col-md-6">
               <div class="col-md-4 col-lg-4 p-none mb-lg">
	               <div class="gray-xs-f mb-xs">Max Fraction Digits  <span class="requiredStar">*</span> <span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" title="Specify the number of steps to divide the scale into."></span></div>
	               <div class="form-group">
	                  <input type="text" class="form-control ContinuousScaleRequired"  name="questionReponseTypeBo.maxFractionDigits" id="continuesScaleFractionDigitsId" value="${questionsBo.questionReponseTypeBo.maxFractionDigits}" onkeypress="return isNumber(event)" maxlength="2">
	                  <div class="help-block with-errors red-txt"></div>
	               </div>
	           </div>
	           </div>
            </div>
            <div class="clearfix"></div>
            <div class="row">
            	<div class="col-md-6 pl-none">
                  <div class="col-md-8 col-lg-8 p-none">
                  	<div class="gray-xs-f mb-xs">Description for minimum value (1 to 20 characters)</div>
	                <div class="form-group">
	                  <input type="text" class="form-control" name="questionReponseTypeBo.minDescription" id="continuesScaleMinDescriptionId" value="${fn:escapeXml(questionsBo.questionReponseTypeBo.minDescription)}" placeholder="Type the question you wish to ask the participant" maxlength="20"/>
	                  <div class="help-block with-errors red-txt"></div>
	                </div>
                  </div>
                </div>
            	<div class="col-md-6">
                  <div class="col-md-9 col-lg-9 p-none">
                  	<div class="gray-xs-f mb-xs">Description for maximum value (1 to 20 characters)</div>
	                <div class="form-group">
	                  <input type="text" class="form-control" name="questionReponseTypeBo.maxDescription" id="continuesScaleMaxDescriptionId" value="${fn:escapeXml(questionsBo.questionReponseTypeBo.maxDescription)}" placeholder="Type the question you wish to ask the participant" maxlength="20" />
	                  <div class="help-block with-errors red-txt"></div>
	                </div>
                  </div>
                </div>
            </div>
            </div>
            <div id="Location" style="display: none">
            	<div class="mt-lg">
	               <div class="gray-xs-f mb-xs">Use Current Location <span class="requiredStar">*</span> <span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" title="Choose Yes if you wish to mark the user's current location on the map used to provide the response."></span></div>
	               <div>
	                  <span class="radio radio-info radio-inline p-45">
	                  <input type="radio" class="LocationRequired" id="useCurrentLocationYes" value="true" name="questionReponseTypeBo.useCurrentLocation"  ${empty questionsBo.questionReponseTypeBo.useCurrentLocation || questionsBo.questionReponseTypeBo.useCurrentLocation eq true ? 'checked':''} >
	                  <label for="useCurrentLocationYes">Yes</label>
	                  </span>
	                  <span class="radio radio-inline">
	                  <input type="radio" class="LocationRequired" id="useCurrentLocationNo" value="false" name="questionReponseTypeBo.useCurrentLocation" ${questionsBo.questionReponseTypeBo.useCurrentLocation eq false ? 'checked':''} >
	                  <label for="useCurrentLocationNo"">No</label>
	                  </span>
	                  <div class="help-block with-errors red-txt"></div>
	               </div>
	            </div>
            </div>
            <div id="Email" style="display: none">
	            <div class="row mt-sm">
	               <div class="col-md-6 pl-none">
	                  <div class="col-md-12 col-lg-12 p-none">
	                     <div class="gray-xs-f mb-xs">Placeholder Text (1 to 40 characters) <span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" title="Enter an input hint to the user"></span></div>
	                     <div class="form-group">
	                        <input type="text" class="form-control" placeholder="1-40 characters"  id="placeholderId" value="${fn:escapeXml(questionsBo.questionReponseTypeBo.placeholder)}" maxlength="40">
	                     </div>
	                  </div>
	               </div>
	            </div>
            </div>
           <div id="Text" style="display: none">
           		<div class="mt-lg">
	               <div class="gray-xs-f mb-xs">Allow Multiple Lines? <span class="requiredStar">*</span> <span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" title="Choose Yes if you need the user to enter large text in a text area."></span></div>
	               <div>
	                  <span class="radio radio-info radio-inline p-45">
	                  <input type="radio" class="TextRequired" id="multipleLinesYes" value="true" name="questionReponseTypeBo.multipleLines"  ${questionsBo.questionReponseTypeBo.multipleLines ? 'checked':''} >
	                  <label for="multipleLinesYes">Yes</label>
	                  </span>
	                  <span class="radio radio-inline">
	                  <input type="radio" class="TextRequired" id="multipleLinesNo" value="false" name="questionReponseTypeBo.multipleLines" ${empty questionsBo.questionReponseTypeBo.multipleLines || !questionsBo.questionReponseTypeBo.multipleLines ? 'checked':''} >
	                  <label for="multipleLinesNo">No</label>
	                  </span>
	                  <div class="help-block with-errors red-txt"></div>
	               </div>
	            </div>
           		<div class="clearfix"></div>
	            <div class="row">
	               <div class="col-md-6 pl-none">
	                  <div class="col-md-8 col-lg-8 p-none">
	                     <div class="gray-xs-f mb-xs">Placeholder (1 to 40 characters) <span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" title="Enter an input hint to the user"></span></div>
	                     <div class="form-group">
	                        <input type="text" class="form-control"  placeholder="1-50 characters"  id="textPlaceholderId" value="${fn:escapeXml(
	                        questionsBo.questionReponseTypeBo.placeholder)}" maxlength="50">
	                     </div>
	                  </div>
	               </div>
	               <div class="col-md-4">
	                  <div class="col-md-4 col-lg-4 p-none">
	                     <div class="gray-xs-f mb-xs">Max Length  <span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" title="Enter an integer for the maximum length of text allowed. If left empty, there will be no max limit applied."></span></div>
	                     <div class="form-group">
	                        <input type="text" class="form-control" name="questionReponseTypeBo.maxLength" id="textmaxLengthId" value="${fn:escapeXml(
	                        questionsBo.questionReponseTypeBo.maxLength)}" onkeypress="return isNumber(event)">
	                     </div>
	                  </div>
	               </div>
	            </div>
           </div>
           <div id="Height" style="display: none">
           		<div class="mt-lg">
	               <div class="gray-xs-f mb-xs">Measurement System <span class="requiredStar">*</span> <span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" title="Select a suitable measurement system for height"></span></div>
	               <div>
	                  <span class="radio radio-info radio-inline p-45">
	                  <input type="radio" class="HeightRequired" id="measurementSystemLocal" value="Local" name="questionReponseTypeBo.measurementSystem"  ${questionsBo.questionReponseTypeBo.measurementSystem eq 'Local'? 'checked':''} >
	                  <label for="measurementSystemLocal">Local</label>
	                  </span>
	                  <span class="radio radio-inline">
	                  <input type="radio" class="HeightRequired" id="measurementSystemMetric" value="Metric" name="questionReponseTypeBo.measurementSystem" ${questionsBo.questionReponseTypeBo.measurementSystem eq 'Metric' ? 'checked':''} >
	                  <label for="measurementSystemMetric">Metric</label>
	                  </span>
	                  <span class="radio radio-inline">
	                  <input type="radio" class="HeightRequired" id="measurementSystemUS" value="US" name="questionReponseTypeBo.measurementSystem" ${empty questionsBo.questionReponseTypeBo.measurementSystem || questionsBo.questionReponseTypeBo.multipleLines eq 'US' ? 'checked':''} >
	                  <label for="measurementSystemUS">US</label>
	                  </span>
	                  <div class="help-block with-errors red-txt"></div>
	               </div>
	            </div>
           		<div class="clearfix"></div>
	            <div class="row mt-sm">
	               <div class="col-md-6 pl-none">
	                  <div class="col-md-12 col-lg-12 p-none">
	                     <div class="gray-xs-f mb-xs">Placeholder Text (1 to 20 characters) <span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" title="Enter an input hint to the user"></span></div>
	                     <div class="form-group">
	                        <input type="text" class="form-control" placeholder="1-20 characters"  id="heightPlaceholderId" value="${fn:escapeXml(
	                        questionsBo.questionReponseTypeBo.placeholder)}" maxlength="20">
	                     </div>
	                  </div>
	               </div>
	            </div>
           </div>
           <div id="Timeinterval" style="display: none;">
	           <div class="row mt-sm">
	           	<div class="col-md-2 pl-none">
	               <div class="gray-xs-f mb-xs">Step value  <span class="requiredStar">*</span> <span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" title=" The step in the interval, in minutes. The value of this parameter must be between 1 and 30."></span></div>
	               <div class="form-group">
	                  <input type="text" class="form-control TimeintervalRequired wid90"  id="timeIntervalStepId" value="${questionsBo.questionReponseTypeBo.step}" onkeypress="return isNumber(event)" maxlength="2">
	                  <span class="dis-inline mt-sm ml-sm">Min</span>
	                  <div class="help-block with-errors red-txt"></div>
	               </div>
	            </div>
	         </div>
          </div>
          <div id="Numeric" style="display: none;">
          	<div class="mt-lg">
	               <div class="gray-xs-f mb-xs">Style <span class="requiredStar">*</span> <span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" title="Choose the kind of numeric input needed"></span></div>
	               <div class="form-group">
	                  <span class="radio radio-info radio-inline p-45">
	                  <input type="radio" class="NumericRequired" id="styleDecimal" value="Decimal" name="questionReponseTypeBo.style"  ${questionsBo.questionReponseTypeBo.style eq 'Decimal' ? 'checked':''} >
	                  <label for="styleDecimal">Decimal</label>
	                  </span>
	                  <span class="radio radio-inline">
	                  <input type="radio" class="NumericRequired" id="styleInteger" value="Integer" name="questionReponseTypeBo.style" ${questionsBo.questionReponseTypeBo.style eq 'Integer' ? 'checked':''} >
	                  <label for="styleInteger">Integer</label>
	                  </span>
	                  <div class="help-block with-errors red-txt"></div>
	               </div>
	        </div>
           	<div class="clearfix"></div>
          	<div class="row">
	               <div class="col-md-6 pl-none">
	                  <div class="col-md-8 col-lg-8 p-none">
	                     <div class="gray-xs-f mb-xs">Units (1 to 15 characters)  <span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" title="Enter the applicable units for the numeric input"></span></div>
	                     <div class="form-group">
	                        <input type="text" class="form-control"  name="questionReponseTypeBo.unit" id="numericUnitId" value="${fn:escapeXml(questionsBo.questionReponseTypeBo.unit)}" maxlength="15">
	                     </div>
	                  </div>
	               </div>
	               <div class="col-md-6">
	                  <div class="col-md-8 col-lg-8 p-none">
	                     <div class="gray-xs-f mb-xs">Placeholder Text (1 to 30 characters)  <span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" title="Provide an input hint to the user"></span></div>
	                     <div class="form-group">
	                        <input type="text" class="form-control"  id="numericPlaceholderId" value="${fn:escapeXml(questionsBo.questionReponseTypeBo.placeholder)}" maxlength="30">
	                     </div>
	                  </div>
	               </div>
	        </div>
          </div>
          <div id="Date" style="display: none;">
          	<div class="mt-lg">
	               <div class="gray-xs-f mb-xs">Style <span class="requiredStar">*</span> <span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" title="Choose the kind of numeric input needed"></span></div>
	               <div class="form-group">
	                  <span class="radio radio-info radio-inline p-45">
	                  <input type="radio" class="DateRequired" id="date" value="Date" name="questionReponseTypeBo.style"  ${questionsBo.questionReponseTypeBo.style eq 'Date' ? 'checked':''} >
	                  <label for="date">Date</label>
	                  </span>
	                  <span class="radio radio-inline">
	                  <input type="radio" class="DateRequired" id="dateTime" value="Date-Time" name="questionReponseTypeBo.style" ${questionsBo.questionReponseTypeBo.style eq 'Date-Time' ? 'checked':''} >
	                  <label for="dateTime">Date-Time</label>
	                  </span>
	                  <div class="help-block with-errors red-txt"></div>
	               </div>
	        </div>
           	<div class="clearfix"></div>
          	<div class="row">
	               <div class="col-md-6 pl-none">
	                  <div class="col-md-8 col-lg-8 p-none">
	                     <div class="gray-xs-f mb-xs">Minimum Date  <span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" title="Enter minimum date allowed."></span></div>
	                     <div class="form-group">
	                        <input type="text" class="form-control"  name="questionReponseTypeBo.minDate" id="minDateId" value="${questionsBo.questionReponseTypeBo.minDate}" >
	                        <div class="help-block with-errors red-txt"></div>
	                     </div>
	                  </div>
	               </div>
	       </div>
	       <div class="row">
	               <div class="col-md-6  pl-none">
	                  <div class="col-md-8 col-lg-8 p-none">
	                     <div class="gray-xs-f mb-xs">Maximum Date <span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" title="Enter maximum date allowed"></span></div>
	                     <div class="form-group">
	                        <input type="text" class="form-control"  name="questionReponseTypeBo.maxDate"id="maxDateId" value="${questionsBo.questionReponseTypeBo.maxDate}" >
	                        <div class="help-block with-errors red-txt"></div>
	                     </div>
	                  </div>
	               </div>
	        </div>
	        <div class="row">
	               <div class="col-md-6  pl-none">
	                  <div class="col-md-8 col-lg-8 p-none">
	                     <div class="gray-xs-f mb-xs">Default Date <span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" title="Enter default date to be shown as selected"></span></div>
	                     <div class="form-group">
	                        <input type="text" class="form-control"  name="questionReponseTypeBo.defaultDate" id="defaultDate" value="${questionsBo.questionReponseTypeBo.defaultDate}">
	                        <div class="help-block with-errors red-txt"></div>
	                     </div>
	                  </div>
	               </div>
	        </div>
          </div>
          <div id="Boolean" style="display: none;">
          	<div class="clearfix"></div>
          	<div class="mt-lg"><div class="gray-choice-f mb-xs">Choices <span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" title="If there is branching applied to your questionnaire, you can  define destination steps for the Yes and No choices"></span></div></div>
          	<div class="row mt-xs" id="0">
          		<input type="hidden" class="form-control" id="responseSubTypeValueId0" name="questionResponseSubTypeList[0].responseSubTypeValueId" value="${fn:escapeXml(questionsBo.questionResponseSubTypeList[0].responseSubTypeValueId)}">
	          	<div class="col-md-3 pl-none">
				   <div class="gray-xs-f mb-xs">Display Text <span class="requiredStar">*</span> </div>
				   <div class="form-group">
				      <input type="text" class="form-control" id="dispalyText0" name="questionResponseSubTypeList[0].text" value="Yes" readonly="readonly">
				      <div class="help-block with-errors red-txt"></div>
				   </div>
				</div>
				<div class="col-md-3 pl-none">
				   <div class="gray-xs-f mb-xs">Value <span class="requiredStar">*</span> </div>
				   <div class="form-group">
				      <input type="text" class="form-control" id="displayValue0" value="True" name="questionResponseSubTypeList[0].value" readonly="readonly">
				      <div class="help-block with-errors red-txt" ></div>
				   </div>
				</div>
			</div>
			
			<div class="row" id="1">
	          	<div class="col-md-3 pl-none" >
	          	<input type="hidden" class="form-control" id="responseSubTypeValueId1" name="questionResponseSubTypeList[1].responseSubTypeValueId" value="${fn:escapeXml(questionsBo.questionResponseSubTypeList[1].responseSubTypeValueId)}">
				   <div class="form-group">
				      <input type="text" class="form-control" id="dispalyText1" name="questionResponseSubTypeList[1].text" value="No" readonly="readonly">
				      <div class="help-block with-errors red-txt" ></div>
				   </div>
				</div>
				<div class="col-md-3 pl-none">
				   <div class="form-group">
				      <input type="text" class="form-control" id="displayValue1" value="False" name="questionResponseSubTypeList[1].value" readonly="readonly">
				      <div class="help-block with-errors red-txt"></div>
				   </div>
				</div>
			</div>
          </div>
          <div id="ValuePicker" style="display: none;">
           <div class="mt-lg"><div class="gray-choice-f mb-xs">Values for the picker<span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" title="Enter values in the order they must appear in the picker. Each row needs a display text and an associated value that gets captured if that choice is picked by the user."></span></div></div>
           <div class="row mt-sm" id="0">
          	<div class="col-md-3 pl-none">
			   <div class="gray-xs-f mb-xs">Display Text (1 to 15 characters)<span class="requiredStar">*</span></div>
			</div>
			<div class="col-md-4 pl-none">
			   <div class="gray-xs-f mb-xs">Value (1 to 50 characters)<span class="requiredStar">*</span></div>
			</div>
			<div class="clearfix"></div>
			<div class="ValuePickerContainer">
			<c:choose>
			  <c:when test="${questionsBo.responseType eq 4 && fn:length(questionsBo.questionResponseSubTypeList) gt 1}">
			  	<c:forEach items="${questionsBo.questionResponseSubTypeList}" var="questionResponseSubType" varStatus="subtype">
			  		<div class="value-picker row form-group" id="${subtype.index}">
			  		<input type="hidden" class="form-control" id="valPickSubTypeValueId${subtype.index}" name="questionResponseSubTypeList[${subtype.index}].responseSubTypeValueId" value="${questionResponseSubType.responseSubTypeValueId}">
						<div class="col-md-3 pl-none">
						   <div class="form-group">
						      <input type="text" class="form-control ValuePickerRequired" name="questionResponseSubTypeList[${subtype.index}].text" id="displayValPickText${subtype.index}" value="${fn:escapeXml(questionResponseSubType.text)}" maxlength="15">
						      <div class="help-block with-errors red-txt"></div>
						   </div>
						</div>
						<div class="col-md-4 pl-none">
						   <div class="form-group">
						      <input type="text" class="form-control ValuePickerRequired" name="questionResponseSubTypeList[${subtype.index}].value" id="displayValPickValue${subtype.index}" value="${fn:escapeXml(questionResponseSubType.value)}" maxlength="50">
						      <div class="help-block with-errors red-txt"></div>
						   </div>
						</div>
						<div class="col-md-2 pl-none mt-md">
						   <span class="addBtnDis addbtn mr-sm align-span-center" onclick='addValuePicker();'>+</span>
				           <span class="delete vertical-align-middle remBtnDis hide pl-md align-span-center" onclick='removeValuePicker(this);'></span>
						</div>
					</div>
			  	</c:forEach>
			  </c:when>
			  <c:otherwise>
			  	<div class="value-picker row form-group" id="0">
					<div class="col-md-3 pl-none">
					   <div class="form-group">
					      <input type="text" class="form-control ValuePickerRequired" name="questionResponseSubTypeList[0].text" id="displayValPickText0" value="${fn:escapeXml(questionsBo.questionResponseSubTypeList[0].text)}" maxlength="15">
					      <div class="help-block with-errors red-txt"></div>
					   </div>
					</div>
					<div class="col-md-4 pl-none">
					   <div class="form-group">
					      <input type="text" class="form-control ValuePickerRequired" name="questionResponseSubTypeList[0].value" id="displayValPickValue0" value="${fn:escapeXml(questionsBo.questionResponseSubTypeList[0].value)}" maxlength="50">
					      <div class="help-block with-errors red-txt"></div>
					   </div>
					</div>
					<div class="col-md-2 pl-none mt-md">
					   <span class="addBtnDis addbtn mr-sm align-span-center" onclick='addValuePicker();'>+</span>
			           <span class="delete vertical-align-middle remBtnDis hide pl-md align-span-center" onclick='removeValuePicker(this);'></span>
					</div>
				</div>
			   <div class="value-picker row form-group" id="1">
					<div class="col-md-3 pl-none">
					   <div class="form-group">
					      <input type="text" class="form-control ValuePickerRequired" name="questionResponseSubTypeList[1].text" id="displayValPickText1" value="${fn:escapeXml(questionsBo.questionResponseSubTypeList[1].text)}" maxlength="15">
					      <div class="help-block with-errors red-txt"></div>
					   </div>
					</div>
					<div class="col-md-4 pl-none">
					   <div class="form-group">
					      <input type="text" class="form-control ValuePickerRequired" name="questionResponseSubTypeList[1].value" id="displayValPickValue1" value="${fn:escapeXml(questionsBo.questionResponseSubTypeList[1].value)}" maxlength="50">
					      <div class="help-block with-errors red-txt"></div>
					   </div>
					</div>
					<div class="col-md-2 pl-none mt-md">
					<span class="addBtnDis addbtn mr-sm align-span-center" onclick='addValuePicker();'>+</span>
			        <span class="delete vertical-align-middle remBtnDis hide pl-md align-span-center" onclick='removeValuePicker(this);'></span>
					</div>
			   </div> 
			  </c:otherwise>
			</c:choose>
          	</div>
          </div>
         <div>
         </div>
         </div>
         <div id="TextScale" style="display: none;">
            <div class="clearfix"></div>
            <div class="gray-choice-f mb-xs">Text Choices<span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" title="Enter text choices in the order you want them to appear on the slider. You can enter a text that will be displayed for each slider position, and an associated  value to be captured if that position is selected by the user.  You can also select a destination step for each choice, if you have branching enabled for the questionnaire. "></span></div>
            <div class="row">
				   <div class="col-md-3 pl-none">
				      <div class="gray-xs-f mb-xs">Display Text (1 to 15 characters)<span class="requiredStar">*</span> </div>
				   </div>
				   <div class="col-md-4 pl-none">
				      <div class="gray-xs-f mb-xs">Value (1 to 50 characters)<span class="requiredStar">*</span></div>
				   </div>
				</div>
			<div class="TextScaleContainer">
				<c:choose>
					<c:when test="${questionsBo.responseType eq 3 && fn:length(questionsBo.questionResponseSubTypeList) gt 1}">
						<c:forEach items="${questionsBo.questionResponseSubTypeList}" var="questionResponseSubType" varStatus="subtype">
							<div class="text-scale row" id="${subtype.index}">
							<input type="hidden" class="form-control" id="textScaleSubTypeValueId${subtype.index}" name="questionResponseSubTypeList[${subtype.index}].responseSubTypeValueId" value="${questionResponseSubType.responseSubTypeValueId}">
							   <div class="col-md-3 pl-none">
							      <div class="form-group">
							         <input type="text" class="form-control TextScaleRequired" name="questionResponseSubTypeList[${subtype.index}].text" id="displayTextSclText${subtype.index}" value="${fn:escapeXml(questionResponseSubType.text)}" maxlength="15">
							         <div class="help-block with-errors red-txt"></div>
							      </div>
							   </div>
							   <div class="col-md-4 pl-none">
							      <div class="form-group">
							         <input type="text" class="form-control TextScaleRequired" name="questionResponseSubTypeList[${subtype.index}].value" id="displayTextSclValue${subtype.index}" value="${fn:escapeXml(questionResponseSubType.value)}" maxlength="50">
							         <div class="help-block with-errors red-txt"></div>
							      </div>
							   </div>
							   <div class="col-md-2 pl-none mt-md">
								<span class="addBtnDis addbtn mr-sm align-span-center" onclick='addTextScale();'>+</span>
						        <span class="delete vertical-align-middle remBtnDis hide pl-md align-span-center" onclick='removeTextScale(this);'></span>
							   </div>
							</div>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<div class="text-scale row" id="0">
						   <div class="col-md-3 pl-none">
						      <div class="form-group">
						         <input type="text" class="form-control TextScaleRequired" name="questionResponseSubTypeList[0].text" id="displayTextSclText0" value="${fn:escapeXml(questionsBo.questionResponseSubTypeList[0].text)}" maxlength="15">
						         <div class="help-block with-errors red-txt"></div>
						      </div>
						   </div>
						   <div class="col-md-4 pl-none">
						      <div class="form-group">
						         <input type="text" class="form-control TextScaleRequired" name="questionResponseSubTypeList[0].value" id="displayTextSclValue0" value="${fn:escapeXml(questionsBo.questionResponseSubTypeList[0].value)}" maxlength="50">
						         <div class="help-block with-errors red-txt"></div>
						      </div>
						   </div>
						   <div class="col-md-2 pl-none mt-md">
							<span class="addBtnDis addbtn mr-sm align-span-center" onclick='addTextScale();'>+</span>
					        <span class="delete vertical-align-middle remBtnDis hide pl-md align-span-center" onclick='removeTextScale(this);'></span>
							</div>
						</div>
		            	<div class="text-scale row" id="1">
						   <div class="col-md-3 pl-none">
						      <div class="form-group">
						         <input type="text" class="form-control TextScaleRequired" name="questionResponseSubTypeList[1].text" id="displayTextSclText1" value="${fn:escapeXml(questionsBo.questionResponseSubTypeList[1].text)}" maxlength="15">
						         <div class="help-block with-errors red-txt"></div>
						      </div>
						   </div>
						   <div class="col-md-4 pl-none">
						      <div class="form-group">
						         <input type="text" class="form-control TextScaleRequired" name="questionResponseSubTypeList[1].value" id="displayTextSclValue1" value="${fn:escapeXml(questionsBo.questionResponseSubTypeList[1].value)}" maxlength="50">
						         <div class="help-block with-errors red-txt"></div>
						      </div>
						   </div>
						   <div class="col-md-2 pl-none mt-md">
							<span class="addBtnDis addbtn mr-sm align-span-center" onclick='addTextScale();'>+</span>
					        <span class="delete vertical-align-middle remBtnDis hide pl-md align-span-center" onclick='removeTextScale(this);'></span>
							</div>
						</div>
					</c:otherwise>
				</c:choose>
            </div>
            <div class="clearfix"></div>
            <div class="row mt-sm">
                <div class="col-md-6 pl-none">
                   <div class="col-md-8 col-lg-8 p-none">
                        <div class="gray-xs-f mb-xs">Default slider position  <span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" title="Enter an integer number to indicate the desired default slider position. For example, if you have 6 choices, 5 will indicate the 5th choice."></span></div>
                        <div class="form-group">
                           <input type="text" class="form-control" id="textScalePositionId"  value="${questionsBo.questionReponseTypeBo.step}" onkeypress="return isNumber(event)">
                           <div class="help-block with-errors red-txt"></div>
                        </div>
                        </div>
                   </div>                          
               </div>           
         </div>  
         <div id="TextChoice" style="display: none;">
          <div class="mt-lg">
              <div class="gray-xs-f mb-xs">Selection Style <span class="requiredStar">*</span></div>
              <div class="form-group">
                  <span class="radio radio-info radio-inline p-45">
                  <input type="radio" class="TextChoiceRequired" id="singleSelect" value="Single" name="questionReponseTypeBo.selectionStyle"  ${empty questionsBo.questionReponseTypeBo.selectionStyle || questionsBo.questionReponseTypeBo.selectionStyle eq 'Single' ? 'checked':''} onchange="getSelectionStyle(this);">
                  <label for="singleSelect">Single Select</label>
                  </span>
                  <span class="radio radio-inline">
                  <input type="radio" class="TextChoiceRequired" id="multipleSelect" value="Multiple" name="questionReponseTypeBo.selectionStyle" ${questionsBo.questionReponseTypeBo.selectionStyle eq 'Multiple' ? 'checked':''} onchange="getSelectionStyle(this);">
                  <label for="multipleSelect">Multiple Select</label>
                  </span>
                  <div class="help-block with-errors red-txt"></div>
               </div>
          </div>
         <div class="clearfix"></div>
		 <div class="gray-choice-f mb-xs">Text Choices<span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" title="Enter text choices in the order you want them to appear. You can enter a display text, an associated  value to be captured if that choice is selected and mark the choice as exclusive, meaning once it is selected, all other options get deselected and vice-versa. You can also select a destination step for each choice that is exclusive, if you have branching enabled for the questionnaire. "></span></div>
		 <div class="row">
		   <div class="col-md-4 pl-none">
		      <div class="gray-xs-f mb-xs">Display Text (1 to 15 characters)<span class="requiredStar">*</span> </div>
		   </div>
		   <div class="col-md-4 pl-none">
		      <div class="gray-xs-f mb-xs">Value (1 to 50 characters)<span class="requiredStar">*</span></div>
		   </div>
		   <div class="col-md-2 pl-none">
		      <div class="gray-xs-f mb-xs">Mark as exclusive ? <span class="requiredStar">*</span></div>
		   </div>
		   
		 </div>
         <div class="TextChoiceContainer">
         	<c:choose>
				<c:when test="${questionsBo.responseType eq 6 && fn:length(questionsBo.questionResponseSubTypeList) gt 1}">
					<c:forEach items="${questionsBo.questionResponseSubTypeList}" var="questionResponseSubType" varStatus="subtype">
						<div class="col-md-12 p-none text-choice row" id="${subtype.index}">
						<input type="hidden" class="form-control" id="textChoiceSubTypeValueId${subtype.index}" name="questionResponseSubTypeList[${subtype.index}].responseSubTypeValueId" value="${questionResponseSubType.responseSubTypeValueId}">
						   <div class="col-md-4 pl-none">
						      <div class="form-group">
						         <input type="text" class="form-control TextChoiceRequired" name="questionResponseSubTypeList[${subtype.index}].text" id="displayTextChoiceText${subtype.index}" value="${fn:escapeXml(questionResponseSubType.text)}" maxlength="15">
						         <div class="help-block with-errors red-txt"></div>
						      </div>
						   </div>
						   <div class="col-md-4 pl-none">
						      <div class="form-group">
						         <input type="text" class="form-control TextChoiceRequired" name="questionResponseSubTypeList[${subtype.index}].value" id="displayTextChoiceValue${subtype.index}" value="${fn:escapeXml(questionResponseSubType.value)}" maxlength="50">
						         <div class="help-block with-errors red-txt"></div>
						      </div>
						   </div>
						   <div class="col-md-2 pl-none">
						      <div class="form-group">
						          <select name="questionResponseSubTypeList[${subtype.index}].exclusive" id="exclusiveId${subtype.index}" index="${subtype.index}" title="select" data-error="Please choose one option" class="selectpicker <c:if test="${questionsBo.questionReponseTypeBo.selectionStyle eq 'Multiple'}">TextChoiceRequired</c:if> textChoiceExclusive" <c:if test="${empty questionsBo.questionReponseTypeBo.selectionStyle || questionsBo.questionReponseTypeBo.selectionStyle eq 'Single'}">disabled</c:if> >
						              <option value="Yes" ${questionResponseSubType.exclusive eq 'Yes' ? 'selected' :''}>Yes</option>
						              <option value="No" ${questionResponseSubType.exclusive eq 'No' ? 'selected' :''}>No</option>
						          </select>
						         <div class="help-block with-errors red-txt"></div>
						      </div>
						   </div>
						   
						   <div class="col-md-2 pl-none mt-md">
						      <span class="addBtnDis addbtn mr-sm align-span-center" onclick='addTextChoice();'>+</span>
						      <span class="delete vertical-align-middle remBtnDis hide pl-md align-span-center" onclick='removeTextChoice(this);'></span>
						   </div>
						</div>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<div class="col-md-12 p-none text-choice row" id="0">
					   <div class="col-md-4 pl-none">
					      <div class="form-group">
					         <input type="text" class="form-control TextChoiceRequired" name="questionResponseSubTypeList[0].text" id="displayTextChoiceText0" value="${fn:escapeXml(questionsBo.questionResponseSubTypeList[0].text)}" maxlength="15">
					         <div class="help-block with-errors red-txt"></div>
					      </div>
					   </div>
					   <div class="col-md-4 pl-none">
					      <div class="form-group">
					         <input type="text" class="form-control TextChoiceRequired" name="questionResponseSubTypeList[0].value" id="displayTextChoiceValue0" value="${fn:escapeXml(questionsBo.questionResponseSubTypeList[0].value)}" maxlength="50">
					         <div class="help-block with-errors red-txt"></div>
					      </div>
					   </div>
					   <div class="col-md-2 pl-none">
					      <div class="form-group">
					          <select name="questionResponseSubTypeList[0].exclusive" id="exclusiveId0" index="0" title="select" data-error="Please choose one option" class="selectpicker <c:if test="${questionsBo.questionReponseTypeBo.selectionStyle eq 'Multiple'}">TextChoiceRequired</c:if> textChoiceExclusive" <c:if test="${ empty questionsBo.questionReponseTypeBo.selectionStyle || questionsBo.questionReponseTypeBo.selectionStyle eq 'Single'}">disabled</c:if> >
					              <option value="Yes" ${questionsBo.questionResponseSubTypeList[0].exclusive eq 'Yes' ? 'selected' :''}>Yes</option>
					              <option value="No" ${questionsBo.questionResponseSubTypeList[0].exclusive eq 'No' ? 'selected' :''}>No</option>
					          </select>
					         <div class="help-block with-errors red-txt"></div>
					      </div>
					   </div>
					   
					   <div class="col-md-2 pl-none mt-md">
					      <span class="addBtnDis addbtn mr-sm align-span-center" onclick='addTextChoice();'>+</span>
					      <span class="delete vertical-align-middle remBtnDis hide pl-md align-span-center" onclick='removeTextChoice(this);'></span>
					   </div>
					</div>
					<div class="col-md-12 p-none text-choice row" id="1">
					   <div class="col-md-4 pl-none">
					      <div class="form-group">
					         <input type="text" class="form-control TextChoiceRequired" name="questionResponseSubTypeList[1].text" id="displayTextChoiceText1" value="${fn:escapeXml(questionsBo.questionResponseSubTypeList[1].text)}" maxlength="15">
					         <div class="help-block with-errors red-txt"></div>
					      </div>
					   </div>
					   <div class="col-md-4 pl-none">
					      <div class="form-group">
					         <input type="text" class="form-control TextChoiceRequired" name="questionResponseSubTypeList[1].value" id="displayTextChoiceValue1" value="${fn:escapeXml(questionsBo.questionResponseSubTypeList[1].value)}" maxlength="50">
					         <div class="help-block with-errors red-txt"></div>
					      </div>
					   </div>
					   <div class="col-md-2 pl-none">
					      <div class="form-group">
					          <select name="questionResponseSubTypeList[1].exclusive" id="exclusiveId1" index="1" title="select" data-error="Please choose one option" class="selectpicker <c:if test="${questionsBo.questionReponseTypeBo.selectionStyle eq 'Multiple'}">TextChoiceRequired</c:if> textChoiceExclusive" <c:if test="${empty questionsBo.questionReponseTypeBo.selectionStyle || questionsBo.questionReponseTypeBo.selectionStyle eq 'Single'}">disabled</c:if> >
					              <option value="Yes" ${questionsBo.questionResponseSubTypeList[1].exclusive eq 'Yes' ? 'selected' :''}>Yes</option>
					              <option value="No" ${questionsBo.questionResponseSubTypeList[1].exclusive eq 'No' ? 'selected' :''}>No</option>
					          </select>
					         <div class="help-block with-errors red-txt"></div>
					      </div>
					   </div>
					   
					   <div class="col-md-2 pl-none mt-md">
					      <span class="addBtnDis addbtn mr-sm align-span-center" onclick='addTextChoice();'>+</span>
					      <span class="delete vertical-align-middle remBtnDis hide pl-md align-span-center" onclick='removeTextChoice(this);'></span>
					   </div>
					</div>
				</c:otherwise>
			</c:choose>
         </div>
         </div>
         <div id="ImageChoice" style="display: none;">
         	<div class="mt-lg"><div class="gray-choice-f mb-xs">Image Choices<span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" title="Fill in the different image choices you wish to provide. Upload images for display and selected states and enter display text and value to be captured for each choice. Also, if you have branching enabled for your questionnaire, you can define destination steps for each choice."></span></div></div>
         	<div class="mt-sm row">
			   <div>
			      <div class="col-md-2 pl-none col-smthumb-2">
			         <div class="gray-xs-f mb-xs">Image <span class="requiredStar">*</span><span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" data-html="true" title="JPEG / PNG <br> Recommended Size: <br>Min: 90x90 Pixels<br>Max: 120x120 Pixels<br>(Maintain aspect ratio for the selected size of the image)"></span> </div>
			      </div>
			      <div class="col-md-2 pl-none col-smthumb-2">
			         <div class="gray-xs-f mb-xs">Selected Image <span class="requiredStar">*</span><span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" data-html="true" title="JPEG / PNG <br> Recommended Size: <br>Min: 90x90 Pixels<br>Max: 120x120 Pixels<br>(Maintain aspect ratio for the selected size of the image)"></span> </div>
			      </div>
			      <div class="col-md-3 pl-none">
			         <div class="gray-xs-f mb-xs">Display Text <span class="requiredStar">*</span><span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" data-html="true" title="1 to 15 characters"></span></div>
			      </div>
			      <div class="col-md-3 col-lg-3 pl-none">
			         <div class="gray-xs-f mb-xs">Value <span class="requiredStar">*</span><span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" data-html="true" title="1 to 50 characters"></span></div>
			      </div>
			      
			      <div class="col-md-2 pl-none">
			         <div class="gray-xs-f mb-xs">&nbsp;</div>
			      </div>
			   </div>
			</div>
			<div class="ImageChoiceContainer">
				<c:choose>
				<c:when test="${questionsBo.responseType eq 5 && fn:length(questionsBo.questionResponseSubTypeList) gt 1}">
					<c:forEach items="${questionsBo.questionResponseSubTypeList}" var="questionResponseSubType" varStatus="subtype">
						<div class="image-choice row" id="${subtype.index}">
						   <input type="hidden" class="form-control" id="imageChoiceSubTypeValueId${subtype.index}" name="questionResponseSubTypeList[${subtype.index}].responseSubTypeValueId" value="${questionResponseSubType.responseSubTypeValueId}">
						   <div class="col-md-2 pl-none col-smthumb-2">
						      <div class="form-group">
						         <div class="sm-thumb-btn">
						            <div class="thumb-img">
						            <img src="<spring:eval expression="@propertyConfigurer.getProperty('fda.imgDisplaydPath')" />questionnaire/${fn:escapeXml(questionResponseSubType.image)}" onerror="this.src='/fdahpStudyDesigner/images/icons/sm-thumb.jpg';" class="imageChoiceWidth"/>
						            </div>
						            <div class="textLabelimagePathId${subtype.index}">Change</div>
						         </div>
						         <input class="dis-none upload-image <c:if test="${empty questionResponseSubType.image}">ImageChoiceRequired</c:if>" data-imageId='${subtype.index}' name="questionResponseSubTypeList[${subtype.index}].imageFile" id="imageFileId${subtype.index}" type="file"  accept=".png, .jpg, .jpeg" onchange="readURL(this);" value="<spring:eval expression="@propertyConfigurer.getProperty('fda.imgDisplaydPath')" />questionnaire/${fn:escapeXml(questionResponseSubType.image)}">
						         <input type="hidden" name="questionResponseSubTypeList[${subtype.index}].image" id="imagePathId${subtype.index}" value="${questionResponseSubType.image}">
						         <div class="help-block with-errors red-txt"></div>
						      </div>
						   </div>
						   <div class="col-md-2 pl-none col-smthumb-2">
						      <div class="form-group">
						         <div class="sm-thumb-btn">
						            <div class="thumb-img">
						            <img src="<spring:eval expression="@propertyConfigurer.getProperty('fda.imgDisplaydPath')" />questionnaire/${fn:escapeXml(questionResponseSubType.selectedImage)}" onerror="this.src='/fdahpStudyDesigner/images/icons/sm-thumb.jpg';" class="imageChoiceWidth"/>
						            </div>
						            <div class="textLabelselectImagePathId${subtype.index}">Change</div>
						         </div>
						         <input class="dis-none upload-image <c:if test="${empty questionResponseSubType.selectedImage}">ImageChoiceRequired</c:if>" data-imageId='${subtype.index}' name="questionResponseSubTypeList[${subtype.index}].selectImageFile" id="selectImageFileId${subtype.index}" type="file"  accept=".png, .jpg, .jpeg" onchange="readURL(this);">
						         <input type="hidden" name="questionResponseSubTypeList[${subtype.index}].selectedImage" id="selectImagePathId${subtype.index}" value="${questionResponseSubType.selectedImage}">
						         <div class="help-block with-errors red-txt"></div>
						      </div>
						   </div>
						   <div class="col-md-3 pl-none">
						      <div class="form-group">
						         <input type="text" class="form-control ImageChoiceRequired" name="questionResponseSubTypeList[${subtype.index}].text" id="displayImageChoiceText${subtype.index}" value="${fn:escapeXml(questionResponseSubType.text)}" maxlength="15">
						         <div class="help-block with-errors red-txt"></div>
						      </div>
						   </div>
						   <div class="col-md-3 col-lg-3 pl-none">
						      <div class="form-group">
						         <input type="text" class="form-control ImageChoiceRequired" name="questionResponseSubTypeList[${subtype.index}].value" id="displayImageChoiceValue${subtype.index}" value="${fn:escapeXml(questionResponseSubType.value)}"maxlength="50">
						         <div class="help-block with-errors red-txt"></div>
						      </div>
						   </div>
						   
						   <div class="col-md-2 pl-none mt-sm">
						      <span class="addBtnDis addbtn mr-sm align-span-center" onclick='addImageChoice();'>+</span>
							  <span class="delete vertical-align-middle remBtnDis hide pl-md align-span-center" onclick='removeImageChoice(this);'></span>
						   </div>
						</div>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<div class="image-choice row" id="0">
					   <div class="col-md-2 pl-none col-smthumb-2">
					      <div class="form-group">
					         <div class="sm-thumb-btn">
					            <div class="thumb-img">
					            <img src="<spring:eval expression="@propertyConfigurer.getProperty('fda.imgDisplaydPath')" />questionnaire/${fn:escapeXml(questionsBo.questionResponseSubTypeList[0].image)}" onerror="this.src='/fdahpStudyDesigner/images/icons/sm-thumb.jpg';" class="imageChoiceWidth"/>
					            </div>
					            <c:if test="${empty questionsBo.questionResponseSubTypeList[0].image}"><div class="textLabelimagePathId0">Upload</div></c:if>
					            <c:if test="${not empty questionsBo.questionResponseSubTypeList[0].image}"><div class="textLabelimagePathId0">Change</div></c:if>
					         </div>
					         <input class="dis-none upload-image <c:if test="${empty questionsBo.questionResponseSubTypeList[0].image}">ImageChoiceRequired</c:if>" data-imageId='0' name="questionResponseSubTypeList[0].imageFile" id="imageFileId0" type="file"  accept=".png, .jpg, .jpeg" onchange="readURL(this);">
					         <input type="hidden" name="questionResponseSubTypeList[0].image" id="imagePathId0" value="${questionsBo.questionResponseSubTypeList[0].image}">
					         <div class="help-block with-errors red-txt"></div>
					      </div>
					   </div>
					   <div class="col-md-2 pl-none col-smthumb-2">
					      <div class="form-group">
					         <div class="sm-thumb-btn">
					            <div class="thumb-img">
					            <img src="<spring:eval expression="@propertyConfigurer.getProperty('fda.imgDisplaydPath')" />questionnaire/${fn:escapeXml(questionsBo.questionResponseSubTypeList[0].selectedImage)}" onerror="this.src='/fdahpStudyDesigner/images/icons/sm-thumb.jpg';" class="imageChoiceWidth"/>
					            </div>
					            <c:if test="${empty questionsBo.questionResponseSubTypeList[0].selectedImage}"><div class="textLabelselectImagePathId0">Upload</div></c:if>
					            <c:if test="${not empty questionsBo.questionResponseSubTypeList[0].selectedImage}"><div class="textLabelselectImagePathId0">Change</div></c:if>
					         </div>
					         <input class="dis-none upload-image <c:if test="${empty questionsBo.questionResponseSubTypeList[0].selectedImage}">ImageChoiceRequired</c:if>" data-imageId='0' name="questionResponseSubTypeList[0].selectImageFile" id="selectImageFileId0" type="file"  accept=".png, .jpg, .jpeg" onchange="readURL(this);">
					         <input type="hidden" name="questionResponseSubTypeList[0].selectedImage" id="selectImagePathId0" value="${questionsBo.questionResponseSubTypeList[0].selectedImage}">
					         <div class="help-block with-errors red-txt"></div>
					      </div>
					   </div>
					   <div class="col-md-3 pl-none">
					      <div class="form-group">
					         <input type="text" class="form-control ImageChoiceRequired" name="questionResponseSubTypeList[0].text" id="displayImageChoiceText0" value="${fn:escapeXml(questionsBo.questionResponseSubTypeList[0].text)}" maxlength="15">
					         <div class="help-block with-errors red-txt"></div>
					      </div>
					   </div>
					   <div class="col-md-3 col-lg-3 pl-none">
					      <div class="form-group">
					         <input type="text" class="form-control ImageChoiceRequired" name="questionResponseSubTypeList[0].value" id="displayImageChoiceValue0" value="${fn:escapeXml(questionsBo.questionResponseSubTypeList[0].value)}" maxlength="50">
					         <div class="help-block with-errors red-txt"></div>
					      </div>
					   </div>
					   
					   <div class="col-md-2 pl-none mt-sm">
					      <span class="addBtnDis addbtn mr-sm align-span-center" onclick='addImageChoice();'>+</span>
						  <span class="delete vertical-align-middle remBtnDis hide pl-md align-span-center" onclick='removeImageChoice(this);'></span>
					   </div>
					</div>
					<div class="image-choice row" id="1">
					   <div class="col-md-2 pl-none col-smthumb-2">
					      <div class="form-group">
					         <div class="sm-thumb-btn">
					            <div class="thumb-img">
					             <img src="<spring:eval expression="@propertyConfigurer.getProperty('fda.imgDisplaydPath')" />questionnaire/${fn:escapeXml(questionsBo.questionResponseSubTypeList[1].image)}" onerror="this.src='/fdahpStudyDesigner/images/icons/sm-thumb.jpg';" class="imageChoiceWidth"/>
					            </div>
					            <c:if test="${empty questionsBo.questionResponseSubTypeList[1].image}"><div class="textLabelimagePathId1">Upload</div></c:if>
					            <c:if test="${not empty questionsBo.questionResponseSubTypeList[1].image}"><div class="textLabelimagePathId1">Change</div></c:if>
					         </div>
					          <input  class="dis-none upload-image <c:if test="${empty questionsBo.questionResponseSubTypeList[1].image}">ImageChoiceRequired</c:if>" type="file"   data-imageId='1' accept=".png, .jpg, .jpeg" name="questionResponseSubTypeList[1].imageFile" id="imageFileId1" onchange="readURL(this);">
					          <input type="hidden" name="questionResponseSubTypeList[1].image" id="imagePathId1" value="${questionsBo.questionResponseSubTypeList[1].image}">
					          <div class="help-block with-errors red-txt"></div>
					      </div>
					   </div>
					   <div class="col-md-2 pl-none col-smthumb-2">
					      <div class="form-group">
					         <div class="sm-thumb-btn">
					            <div class="thumb-img">
					            <img src="<spring:eval expression="@propertyConfigurer.getProperty('fda.imgDisplaydPath')" />questionnaire/${fn:escapeXml(questionsBo.questionResponseSubTypeList[1].selectedImage)}" onerror="this.src='/fdahpStudyDesigner/images/icons/sm-thumb.jpg';" class="imageChoiceWidth"/>
					            </div>
					            <c:if test="${empty questionsBo.questionResponseSubTypeList[1].selectedImage}"><div class="textLabelselectImagePathId1">Upload</div></c:if>
					            <c:if test="${not empty questionsBo.questionResponseSubTypeList[1].selectedImage}"><div class="textLabelselectImagePathId1">Change</div></c:if>
					         </div>
					          <input  class="dis-none upload-image <c:if test="${empty questionsBo.questionResponseSubTypeList[1].selectedImage}">ImageChoiceRequired</c:if>" type="file"  data-imageId='1' accept=".png, .jpg, .jpeg" name="questionResponseSubTypeList[1].selectImageFile" id="selectImageFileId1" onchange="readURL(this);">
					          <input type="hidden" name="questionResponseSubTypeList[1].selectedImage" id="selectImagePathId1" value="${questionsBo.questionResponseSubTypeList[1].selectedImage}">
					          <div class="help-block with-errors red-txt"></div>
					      </div>
					   </div>
					   <div class="col-md-3 pl-none">
					      <div class="form-group">
					         <input type="text" class="form-control ImageChoiceRequired" name="questionResponseSubTypeList[1].text" id="displayImageChoiceText1" value="${fn:escapeXml(questionsBo.questionResponseSubTypeList[1].text)}" maxlength="15">
					          <div class="help-block with-errors red-txt"></div>
					      </div>
					   </div>
					   <div class="col-md-3 col-lg-3 pl-none">
					      <div class="form-group">
					          <input type="text" class="form-control ImageChoiceRequired" name="questionResponseSubTypeList[1].value" id="displayImageChoiceValue1" value="${fn:escapeXml(questionsBo.questionResponseSubTypeList[1].value)}" maxlength="50">
					          <div class="help-block with-errors red-txt"></div>
					      </div>
					   </div>
					   
					   <div class="col-md-2 pl-none mt-sm">
					      <span class="addBtnDis addbtn mr-sm align-span-center" onclick='addImageChoice();'>+</span>
						  <span class="delete vertical-align-middle remBtnDis hide pl-md align-span-center" onclick='removeImageChoice(this);'></span>
					   </div>
					</div> 
				</c:otherwise>
				</c:choose>
			</div>
          </div>
          </div>
         </div>
      </div>
   </div>
   </form:form>
</div>
<!-- End right Content here -->
<script type="text/javascript">
$(document).ready(function(){
	
	<c:if test="${actionTypeForFormStep == 'view'}">
		$('#questionStepId input,textarea ').prop('disabled', true);
		$('#questionStepId select').addClass('linkDis');
		$('.addBtnDis, .remBtnDis').addClass('dis-none');
	</c:if>
	
	$(".menuNav li.active").removeClass('active');
	$(".sixthQuestionnaires").addClass('active');
     $("#doneId").click(function(){
    	 var isValid = true;
    	 if(isFromValid("#questionStepId")){
    		 var resType = $("#rlaResonseType").val();
   		  var placeholderText ='';
   		  var stepText = "";
   		  if(resType == "Email"){
   				 placeholderText = $("#placeholderId").val();	
   		  }else if(resType == "Text"){
   				placeholderText = $("#textPlaceholderId").val(); 
   		  }else if(resType == "Height"){
   				placeholderText = $("#heightPlaceholderId").val();
   		  }else if(resType == "Numeric"){
   				placeholderText = $("#numericPlaceholderId").val(); 
   		  }else if(resType == "Time interval"){
   			  stepText = $("#timeIntervalStepId").val();
   		  }else if(resType == "Scale" || resType == "Continuous Scale"){
   			  stepText =  $("#scaleStepId").val();
   				var minValue =''
   				var maxValue = ''
	   			if(resType == "Continuous Scale"){
	   				 minValue = $("#continuesScaleMinDescriptionId").val();
	     			 maxValue = $("#continuesScaleMaxDescriptionId").val();
	   			}else{
	   				 minValue = $("#scaleMinDescriptionId").val();
	     			 maxValue = $("#scaleMaxDescriptionId").val();
	   			}
   			  if((minValue != '' && maxValue != '') || (minValue == '' && maxValue == '')){
   				isValid = true;
   			  }else{
   				  if(maxValue == ''){
   					if(resType == "Continuous Scale"){
   					 	$("#continuesScaleMaxDescriptionId").parent().addClass("has-danger").addClass("has-error");
                     	$("#continuesScaleMaxDescriptionId").parent().find(".help-block").empty();
                        $("#continuesScaleMaxDescriptionId").parent().find(".help-block").append("<ul class='list-unstyled'><li>Please fill out this field</li></ul>");
                    }else{
                    	$("#scaleMaxDescriptionId").parent().addClass("has-danger").addClass("has-error");
                        $("#scaleMaxDescriptionId").parent().find(".help-block").empty();
                        $("#scaleMaxDescriptionId").parent().find(".help-block").append("<ul class='list-unstyled'><li>Please fill out this field</li></ul>"); 
                    }
   				  }
     			  if(minValue == ''){
     				 if(resType == "Continuous Scale"){
       					 $("#continuesScaleMinDescriptionId").parent().addClass("has-danger").addClass("has-error");
                         $("#continuesScaleMinDescriptionId").parent().find(".help-block").empty();
                         $("#continuesScaleMinDescriptionId").parent().find(".help-block").append("<ul class='list-unstyled'><li>Please fill out this field</li></ul>");
                     }else{
                    	 $("#scaleMinDescriptionId").parent().addClass("has-danger").addClass("has-error");
                         $("#scaleMinDescriptionId").parent().find(".help-block").empty();
                         $("#scaleMinDescriptionId").parent().find(".help-block").append("<ul class='list-unstyled'><li>Please fill out this field</li></ul>");   
                     }  
     			  }
   				isValid = false;
   			  }
   			  
   		  }else if(resType == 'Text Scale'){
			  stepText =  $("#textScalePositionId").val();
		  }
   		 $("#placeholderTextId").val(placeholderText);
   		 $("#stepValueId").val(stepText);
   		if(resType != '' && resType != null && resType != 'undefined'){
	    	 $("#responseTypeId > option").each(function() {
	    		 var textVal = this.text.replace(/\s/g, '');
	    		 console.log("textVal:"+textVal);
   			 if(resType.replace(/\s/g, '') == textVal){
   			 }else{
   				 $("#"+textVal).empty();
   			 }    
   		 });
		 }
   		if(isValid){
   			document.questionStepId.submit();
   		}
     	 
		}else{
			var qlaCount = $('#qla').find('.has-error.has-danger').length;
			var rlaCount = $('#rla').find('.has-error.has-danger').length;
			if(parseInt(qlaCount) >= 1){
				 $('.questionLevel a').tab('show');
			}else if(parseInt(rlaCount) >= 1){
				 $('.responseLevel a').tab('show');
			}	
		} 
     });
     $(".responseLevel ").on('click',function(){
    	var reponseType = $("#responseTypeId").val();
    	if(reponseType != '' && reponseType !='' && typeof reponseType != 'undefined'){
    		$("#responseTypeDivId").show();
    	}else{
    		$("#responseTypeDivId").hide();
    	}
     });
     $("#continuesScaleMaxDescriptionId,#continuesScaleMinDescriptionId,#scaleMinDescriptionId,#scaleMaxDescriptionId").on("change",function(){
    	 $(this).validator('validate');
         $(this).parent().removeClass("has-danger").removeClass("has-error");
         $(this).parent().find(".help-block").html("");
     });
     $("#scaleMinValueId,#scaleMaxValueId").on("change",function(){
    	if($(this).val() != ''){
    		$("#scaleStepId").val('');
    		$("#scaleDefaultValueId").val('');
    	} 
     });
     $("#addLineChart").on('change',function(){
    	if($(this).is(":checked")){
    		$(this).val("Yes");
    		$("#chartContainer").show();
    		$(".chartrequireClass").attr('required',true);
    	} else{
    		$(this).val("No");
    		$("#chartContainer").hide();
    		$(".chartrequireClass").attr('required',false);
    	}
     });
    $("#useStasticData").on('change',function(){
    	if($(this).is(":checked")){
    		$(this).val("Yes");
    		$("#statContainer").show();
    		$(".requireClass").attr('required',true);
    	} else{
    		$(this).val("No");
    		$("#statContainer").hide();
    		$(".requireClass").attr('required',false);
    	}
    });
    $("#scaleMinValueId").blur(function(){
    	var value= $(this).val();
    	var maxValue = $("#scaleMaxValueId").val();
    	if(maxValue != ''){
    		if(parseInt(value) >= -10000 && parseInt(value) <= 10000){
    			if(parseInt(value) > parseInt(maxValue)){
            		$(this).val('');
           		    $(this).parent().addClass("has-danger").addClass("has-error");
                    $(this).parent().find(".help-block").empty();
                    $(this).parent().find(".help-block").append("<ul class='list-unstyled'><li>Please enter an integer number in the range (Min, 10000)</li></ul>");
            	}else{
            		$(this).validator('validate');
            		$(this).parent().removeClass("has-danger").removeClass("has-error");
                    $(this).parent().find(".help-block").html("");
            	}
    		}else{
        		$(this).val('');
       		    $(this).parent().addClass("has-danger").addClass("has-error");
                $(this).parent().find(".help-block").empty();
                $(this).parent().find(".help-block").append("<ul class='list-unstyled'><li>Please enter an integer number in the range (Min, 10000) </li></ul>");
        	}
    	}else{
    		if(parseInt(value) >= -10000 && parseInt(value) <= 10000){
        		$(this).validator('validate');
        		$(this).parent().removeClass("has-danger").removeClass("has-error");
                $(this).parent().find(".help-block").html("");
        	}else{
        		$(this).val('');
       		    $(this).parent().addClass("has-danger").addClass("has-error");
                $(this).parent().find(".help-block").empty();
                $(this).parent().find(".help-block").append("<ul class='list-unstyled'><li>Please enter an integer number in the range (Min, 10000) </li></ul>");
        	}
    	}
    });
    $("#scaleMaxValueId").blur(function(){
    	var value= $(this).val();
    	var minValue = $("#scaleMinValueId").val();
    	console.log("minValue:"+minValue+" "+Number(minValue)+1);
    	console.log("value:"+value);
    	if(minValue != ''){
    		if(parseInt(value) >= -10000 && parseInt(value) <= 10000){
    			if(parseInt(value) >= parseInt(minValue)+1 && parseInt(value) <= 10000){
        			console.log("iffff");
        			$(this).validator('validate');
            		$(this).parent().removeClass("has-danger").removeClass("has-error");
                    $(this).parent().find(".help-block").html("");
        		}else if(parseInt(value) < parseInt(minValue)+1){
        			console.log("else");
        			$(this).val('');
           		    $(this).parent().addClass("has-danger").addClass("has-error");
                    $(this).parent().find(".help-block").empty();
                    $(this).parent().find(".help-block").append("<ul class='list-unstyled'><li>Please enter an integer number in the range (Min+1, 10000)</li></ul>");
        		}
        	}else{
        		$(this).val('');
       		    $(this).parent().addClass("has-danger").addClass("has-error");
                $(this).parent().find(".help-block").empty();
                $(this).parent().find(".help-block").append("<ul class='list-unstyled'><li>Please enter an integer number in the range (Min+1, 10000) </li></ul>");
        	}
    	}else{
    		if(parseInt(value) >= -10000 && parseInt(value) <= 10000){
        		$(this).validator('validate');
        		$(this).parent().removeClass("has-danger").removeClass("has-error");
                $(this).parent().find(".help-block").html("");
        	}else{
        		$(this).val('');
       		    $(this).parent().addClass("has-danger").addClass("has-error");
                $(this).parent().find(".help-block").empty();
                $(this).parent().find(".help-block").append("<ul class='list-unstyled'><li>Please enter an integer number in the range (Min+1, 10000) </li></ul>");
        	}
    	}
    });
    $("#scaleStepId").blur(function(){
    	var value= $(this).val();
    	var minValue = $("#scaleMinValueId").val();
    	var maxValue = $("#scaleMaxValueId").val();
    	if(value != '' && parseInt(value) >= 1 && parseInt(value) <= 13){
    		if(minValue != '' && maxValue != ''){
    			var diff = parseInt(maxValue)-parseInt(minValue);
    			if((parseInt(diff)%parseInt(value)) == 0){
    				$(this).validator('validate');
    	    		$(this).parent().removeClass("has-danger").removeClass("has-error");
    	            $(this).parent().find(".help-block").html("");
    			}else{
    				 $(this).val('');
    	    		 $(this).parent().addClass("has-danger").addClass("has-error");
    	             $(this).parent().find(".help-block").empty();
    	             $(this).parent().find(".help-block").append("<ul class='list-unstyled'><li>Please enter an valid step count </li></ul>");
    			}
    		}
    	}else{
    	     $(this).val('');
    		 $(this).parent().addClass("has-danger").addClass("has-error");
             $(this).parent().find(".help-block").empty();
             $(this).parent().find(".help-block").append("<ul class='list-unstyled'><li>Please enter an integer between the 1 and 13 </li></ul>");
    	}
    });
    $("#timeIntervalStepId").blur(function(){
    	var value= $(this).val();
    	if(parseInt(value) >= 1 && parseInt(value) <= 30){
    		$(this).validator('validate');
    		$(this).parent().removeClass("has-danger").removeClass("has-error");
            $(this).parent().find(".help-block").html("");
    	}else{
    	     $(this).val('');
    		 $(this).parent().addClass("has-danger").addClass("has-error");
             $(this).parent().find(".help-block").empty();
             $(this).parent().find(".help-block").append("<ul class='list-unstyled'><li>Please enter an integer from 1 to 30 </li></ul>");
    	}
    });
    $("#textScalePositionId").blur(function(){
    	var count = $('.text-scale').length;
    	var value= $(this).val();
    	if(value >= 2 && value <= count){
    		$(this).validator('validate');
    		$(this).parent().removeClass("has-danger").removeClass("has-error");
            $(this).parent().find(".help-block").html("");
    	}else{
    	     $(this).val('');
    		 $(this).parent().addClass("has-danger").addClass("has-error");
             $(this).parent().find(".help-block").empty();
             $(this).parent().find(".help-block").append("<ul class='list-unstyled'><li>Please enter an integer between the 1 and Number of choices </li></ul>");
    	}
    });
    $("#scaleDefaultValueId").blur(function(){
    	var value= $(this).val();
    	var minValue = $("#scaleMinValueId").val();
		var maxValue = $("#scaleMaxValueId").val();
		if(parseInt(value) >= parseInt(minValue) && parseInt(value) <= parseInt(maxValue)){
			$(this).validator('validate');
    		$(this).parent().removeClass("has-danger").removeClass("has-error");
            $(this).parent().find(".help-block").html("");
		}else{
			 $(this).val('');
    		 $(this).parent().addClass("has-danger").addClass("has-error");
             $(this).parent().find(".help-block").empty();
             $(this).parent().find(".help-block").append("<ul class='list-unstyled'><li>Please enter an integer between the minimum and maximum  </li></ul>");
		}
    });
    $("#continuesScaleMinValueId").blur(function(){
    	var value= $(this).val();
    	var maxValue = $("#continuesScaleMaxValueId").val();
    	if(maxValue != ''){
    		if(parseInt(value) >= -10000 && parseInt(value) <= 10000){
    			if(parseInt(value) > parseInt(maxValue)){
            		$(this).val('');
           		    $(this).parent().addClass("has-danger").addClass("has-error");
                    $(this).parent().find(".help-block").empty();
                    $(this).parent().find(".help-block").append("<ul class='list-unstyled'><li>Please enter an integer number in the range (Min, 10000)</li></ul>");
            	}else{
            		$(this).validator('validate');
            		$(this).parent().removeClass("has-danger").removeClass("has-error");
                    $(this).parent().find(".help-block").html("");
            	}
        	}else{
        		$(this).val('');
       		    $(this).parent().addClass("has-danger").addClass("has-error");
                $(this).parent().find(".help-block").empty();
                $(this).parent().find(".help-block").append("<ul class='list-unstyled'><li>Please enter an integer number in the range (Min, 10000) </li></ul>");
        	}
    	}else{
    		if(parseInt(value) >= -10000 && parseInt(value) <= 10000){
        		$(this).validator('validate');
        		$(this).parent().removeClass("has-danger").removeClass("has-error");
                $(this).parent().find(".help-block").html("");
        	}else{
        		$(this).val('');
       		    $(this).parent().addClass("has-danger").addClass("has-error");
                $(this).parent().find(".help-block").empty();
                $(this).parent().find(".help-block").append("<ul class='list-unstyled'><li>Please enter an integer number in the range (Min, 10000) </li></ul>");
        	}
    	}
    });
    $("#continuesScaleMaxValueId").blur(function(){
    	var value= $(this).val();
    	var minValue = $("#continuesScaleMinValueId").val();
    	if(minValue != ''){
    		if(parseInt(value) >= -10000 && parseInt(value) <= 10000){
    			if(parseInt(value) >= parseInt(minValue)+1 && parseInt(value) <= 10000){
        			$(this).validator('validate');
            		$(this).parent().removeClass("has-danger").removeClass("has-error");
                    $(this).parent().find(".help-block").html("");
        		}else if(parseInt(value) < parseInt(minValue)+1){
        			$(this).val('');
           		    $(this).parent().addClass("has-danger").addClass("has-error");
                    $(this).parent().find(".help-block").empty();
                    $(this).parent().find(".help-block").append("<ul class='list-unstyled'><li>Please enter an integer number in the range (Min+1, 10000)</li></ul>");
        		}
        	}else{
        		$(this).val('');
       		    $(this).parent().addClass("has-danger").addClass("has-error");
                $(this).parent().find(".help-block").empty();
                $(this).parent().find(".help-block").append("<ul class='list-unstyled'><li>Please enter an integer number in the range (Min+1, 10000) </li></ul>");
        	}
    	}else{
    		if(parseInt(value) >= -10000 && parseInt(value) <= 10000){
        		$(this).validator('validate');
        		$(this).parent().removeClass("has-danger").removeClass("has-error");
                $(this).parent().find(".help-block").html("");
        	}else{
        		$(this).val('');
       		    $(this).parent().addClass("has-danger").addClass("has-error");
                $(this).parent().find(".help-block").empty();
                $(this).parent().find(".help-block").append("<ul class='list-unstyled'><li>Please enter an integer number in the range (Min+1, 10000) </li></ul>");
        	}
    	}
    });
    $("#continuesScaleFractionDigitsId").blur(function(){
    	var value= $(this).val();
    	if(parseInt(value) >= 1 && parseInt(value) <= 13){
    		$(this).validator('validate');
    		$(this).parent().removeClass("has-danger").removeClass("has-error");
            $(this).parent().find(".help-block").html("");
    	}else{
    	     $(this).val('');
    		 $(this).parent().addClass("has-danger").addClass("has-error");
             $(this).parent().find(".help-block").empty();
             $(this).parent().find(".help-block").append("<ul class='list-unstyled'><li>Please enter an integer between the 1 and 13 </li></ul>");
    	}
    });
    $("#continuesScaleDefaultValueId").blur(function(){
    	var value= $(this).val();
    	var minValue = $("#continuesScaleMinValueId").val();
		var maxValue = $("#continuesScaleMaxValueId").val();
		if(parseInt(value) >= parseInt(minValue) && parseInt(value) <= parseInt(maxValue)){
			$(this).validator('validate');
    		$(this).parent().removeClass("has-danger").removeClass("has-error");
            $(this).parent().find(".help-block").html("");
		}else{
			 $(this).val('');
    		 $(this).parent().addClass("has-danger").addClass("has-error");
             $(this).parent().find(".help-block").empty();
             $(this).parent().find(".help-block").append("<ul class='list-unstyled'><li>Please enter an integer between the minimum and maximum  </li></ul>");
		}
    });
    var responseTypeId= '${questionsBo.responseType}';
    if(responseTypeId != null && responseTypeId !='' && typeof responseTypeId != 'undefined'){
    	 getResponseType(responseTypeId);
    }
    $("#responseTypeId").on("change",function(){
    	var value= $(this).val();
    	console.log(value);
    	 getResponseType(value);
    });
    $('.DateRequired').on("change",function(){
    	var value= $(this).val();
    	setResponseDate(value);
    	
    });
    $("#minDateId").on('dp.change',function(){
    	var minDate = $("#minDateId").val();
        var maxDate = $('#maxDateId').val();
        if(minDate!='' && maxDate!='' && new Date(minDate) > new Date(maxDate)){
        	$('#minDateId').parent().addClass("has-danger").addClass("has-error");
       	    $('#minDateId').parent().find(".help-block").html('<ul class="list-unstyled"><li>Max Date and Time Should not be less than Min Date and Time</li></ul>');
       	    $('#minDateId').val('');
        }else{
        	$('#minDateId').parent().removeClass("has-danger").removeClass("has-error");
            $('#minDateId').parent().find(".help-block").html("");
            $("#maxDateId").parent().removeClass("has-danger").removeClass("has-error");
            $("#maxDateId").parent().find(".help-block").html("");
        }
    	
    });
    $("#maxDateId").on('dp.change',function(){
    	var minDate = $("#minDateId").val();
        var maxDate = $('#maxDateId').val();
        console.log("minDate:"+minDate);
        console.log("maxDate:"+maxDate);
        if(minDate!='' && maxDate!='' && new Date(minDate) > new Date(maxDate)){
        	$('#maxDateId').parent().addClass("has-danger").addClass("has-error");
       	    $('#maxDateId').parent().find(".help-block").html('<ul class="list-unstyled"><li>Max Date and Time Should not be less than Min Date and Time</li></ul>');
       	    $('#maxDateId').val('');
       	    console.log("ifffffffff");
        }else{
        	$('#maxDateId').parent().removeClass("has-danger").removeClass("has-error");
            $('#maxDateId').parent().find(".help-block").html("");
            $("#minDateId").parent().removeClass("has-danger").removeClass("has-error");
            $("#minDateId").parent().find(".help-block").html("");
        }
    });
    $("#defaultDate").on('dp.change',function(){
    	var minDate = $("#minDateId").val();
        var maxDate = $('#maxDateId').val();
        var defaultDate = $("#defaultDate").val();
        console.log("minDate:"+minDate);
        console.log("maxDate:"+maxDate);
        console.log("defaultDate:"+defaultDate);
        if(minDate!='' && maxDate!='' && defaultDate != ''){
        	if(new Date(defaultDate) > new Date(minDate) && new Date(defaultDate) < new Date(maxDate)){
        		$('#defaultDate').parent().removeClass("has-danger").removeClass("has-error");
                $('#defaultDate').parent().find(".help-block").html("");
                console.log("ifffff");
        	}else{
        		$('#defaultDate').parent().addClass("has-danger").addClass("has-error");
           	    $('#defaultDate').parent().find(".help-block").html('<ul class="list-unstyled"><li>Enter default date to be shown as selected as per availability of Min and Max</li></ul>');
           	    $('#defaultDate').val('');
           	     console.log("else");
        	}
        }
    });
    $("#shortTitle").blur(function(){
     	var shortTitle = $(this).val();
     	var questionnaireId = $("#questionnairesId").val();
     	var stepType="Question";
     	var thisAttr= this;
     	var existedKey = $("#preShortTitleId").val();
     	if(shortTitle != null && shortTitle !='' && typeof shortTitle!= 'undefined'){
     		if(existedKey !=shortTitle){
     			$.ajax({
                     url: "/fdahpStudyDesigner/adminStudies/validateQuestionKey.do",
                     type: "POST",
                     datatype: "json",
                     data: {
                     	shortTitle : shortTitle,
                     	questionnaireId : questionnaireId,
                     },
                     beforeSend: function(xhr, settings){
                         xhr.setRequestHeader("X-CSRF-TOKEN", "${_csrf.token}");
                     },
                     success:  function getResponse(data){
                         var message = data.message;
                         console.log(message);
                         if('SUCCESS' != message){
                             $(thisAttr).validator('validate');
                             $(thisAttr).parent().removeClass("has-danger").removeClass("has-error");
                             $(thisAttr).parent().find(".help-block").html("");
                         }else{
                             $(thisAttr).val('');
                             $(thisAttr).parent().addClass("has-danger").addClass("has-error");
                             $(thisAttr).parent().find(".help-block").empty();
                             $(thisAttr).parent().find(".help-block").append("<ul class='list-unstyled'><li>'" + shortTitle + "' already exists.</li></ul>");
                         }
                     },
                     global : false
               });
     		}
     	}
     });
    if($('.value-picker').length > 2){
		$('.ValuePickerContainer').find(".remBtnDis").removeClass("hide");
	}else{
		$('.ValuePickerContainer').find(".remBtnDis").addClass("hide");
	}
    if($('.text-scale').length > 2){
		$('.TextScaleContainer').find(".remBtnDis").removeClass("hide");
	}else{
		$('.TextScaleContainer').find(".remBtnDis").addClass("hide");
	}
    if($('.text-choice').length > 2){
		$('.TextChoiceContainer').find(".remBtnDis").removeClass("hide");
	}else{
		$('.TextChoiceContainer').find(".remBtnDis").addClass("hide");
	}
    if($('.image-choice').length > 2){
		$('.ImageChoiceContainer').find(".remBtnDis").removeClass("hide");
	}else{
		$('.ImageChoiceContainer').find(".remBtnDis").addClass("hide");
	}
    $('[data-toggle="tooltip"]').tooltip();
 // File Upload    
    $(".sm-thumb-btn").click(function(){
        $(this).next().click();
    });
    var _URL = window.URL || window.webkitURL;

    $(document).on('change', '.upload-image', function(e) {
        var file, img;
        var thisAttr = this;
        if ((file = this.files[0])) {
            img = new Image();
            img.onload = function() {
                var ht = this.height;
                var wds = this.width;
                if ((parseInt(ht) == parseInt(wds)) && (parseInt(ht) >= 90 && parseInt(ht) <= 120 ) && (parseInt(wds) >=90 && parseInt(wds) <= 120)) {
                    $(thisAttr).parent().find('.form-group').removeClass('has-error has-danger');
                    $(thisAttr).parent().find(".help-block").empty();
                    var id= $(thisAttr).next().attr("id");
                    $("#"+id).val('');
                    $('.textLabel'+id).text("Change");
                } else {
                    $(thisAttr).parent().find('img').attr("src","../images/icons/sm-thumb.jpg");
                    $(thisAttr).parent().find('.form-group').addClass('has-error has-danger');
                    $(thisAttr).parent().find(".help-block").empty().append('<ul class="list-unstyled"><li>Failed to upload. </li></ul>');
                    $(thisAttr).parent().parent().parent().find(".removeUrl").click();
                    var id= $(thisAttr).next().attr("id");
                    $("#"+id).val('');
                    $('.textLabel'+id).text("Upload");
                }
            };
            img.onerror = function() {
                $(thisAttr).parent().find('img').attr("src","../images/icons/sm-thumb.jpg");
                $(thisAttr).parent().find('.form-group').addClass('has-error has-danger');
                $(thisAttr).parent().find(".help-block").empty().append('<ul class="list-unstyled"><li>Failed to upload. </li></ul>');
                $(thisAttr).parent().parent().parent().find(".removeUrl").click();
            };
            img.src = _URL.createObjectURL(file);
        }
    });
});
//Displaying images from file upload 
function readURL(input) {
    
    if (input.files && input.files[0]) {
            var reader = new FileReader();            
             
            reader.onload = function (e) {
                 var a = input.getAttribute("id");
                 $("#" + a).prev().children().children()
                    .attr('src', e.target.result)
                    .width(32)
                    .height(32); 
                var  sr = $("#" + a).prev().children().children().attr('src');
            };

            reader.readAsDataURL(input.files[0]);
        }
}
function toJSDate( dateTime ) {
	if(dateTime != null && dateTime !='' && typeof dateTime != 'undefined'){
		var date = dateTime.split("/");
		return new Date(date[2], (date[0]-1), date[1]);
	}
}
function setResponseDate(type){
	console.log("type:"+type);
	if(type == 'Date-Time'){
		
		$("#minDateId").datetimepicker().data('DateTimePicker').format('MM/DD/YYYY HH:mm:ss');
	    $("#maxDateId").datetimepicker().data('DateTimePicker').format('MM/DD/YYYY HH:mm:ss');
	    $("#defaultDate").datetimepicker().data('DateTimePicker').format('MM/DD/YYYY HH:mm:ss');
	    
	}else{
		
		$("#minDateId").datetimepicker().data('DateTimePicker').format('MM/DD/YYYY');
	    $("#maxDateId").datetimepicker().data('DateTimePicker').format('MM/DD/YYYY');
	    $("#defaultDate").datetimepicker().data('DateTimePicker').format('MM/DD/YYYY');
	   
	}
}
function resetTheLineStatData(){
	 $("#chartContainer").find('input:text').val(''); 
	 $("#statContainer").find('input:text').val(''); 
	 $("#chartContainer").find('input:text').val(''); 
	 $("#statContainer").find('input:text').val(''); 
	 $("#addLineChart").prop("checked", false);
	 $("#useStasticData").prop("checked", false);
	 $("#chartContainer").hide();
     $("#statContainer").hide();
     $(".chartrequireClass").attr('required',false);
     $(".requireClass").attr('required',false);
	 var container = document.getElementById('chartContainer');
	 if(container != null){
		 var children = container.getElementsByTagName('select');
		 for (var i = 0; i < children.length; i++) {
		        children[i].selectedIndex = 0;
		 }
	 }
	  
	 var statcontainer = document.getElementById('statContainer');
	 if(statcontainer != null){
		 var statchildren = statcontainer.getElementsByTagName('select');
		 for (var i = 0; i < statchildren.length; i++) {
		        statchildren[i].selectedIndex = 0;
		 } 
	 }
	 $('.selectpicker').selectpicker('refresh');
}
function getResponseType(id){
	if(id != null && id !='' && typeof id != 'undefined'){
		var previousResponseType = '${questionsBo.responseType}';
		if(Number(id) != Number(previousResponseType)){
			 var responseType = $("#responseTypeId>option:selected").html();
			 resetTheLineStatData();
			 if(responseType != 'Boolean'){
				 $("#"+responseType.replace(/\s/g, '')).find('input:text').val(''); 
				 if(responseType == "Date"){
					 var datePicker = $("#"+responseType.replace(/\s/g, '')).find('input:text').data("DateTimePicker");
					 if(typeof datePicker != 'undefined'){
						 $("#minDateId").datetimepicker().data('DateTimePicker').clear();
						 $("#maxDateId").datetimepicker().data('DateTimePicker').clear();
						 $("#defaultDate").datetimepicker().data('DateTimePicker').clear();
					 }
				 }
			 }
			 if(responseType == 'Image Choice'){
				 $("#"+responseType.replace(/\s/g, '')).find('input:file').val(''); 
				 $("#"+responseType.replace(/\s/g, '')).find('img').attr("src","../images/icons/sm-thumb.jpg");
				 $("#"+responseType.replace(/\s/g, '')).find("input:hidden").each(function(){
					 $("#"+this.id).val('');
				 });
			 }
			 if(responseType == 'Text Scale' && responseType == 'Text Choice' && responseType == 'Boolean'){
				 	var container = document.getElementById(responseType.replace(/\s/g, ''));
				    var children = container.getElementsByTagName('select');
				    console.log("children.length:"+children.length);
				    for (var i = 0; i < children.length; i++) {
				        children[i].selectedIndex = 0;
				    }
				    $('.selectpicker').selectpicker('refresh');
			}
			$("#timeIntervalStepId").val(1);
			$("#scaleStepId").val(5);
			$("#textScalePositionId").val(2);
			if(responseType == 'Text Scale'){
	    		 $("#vertical").attr("checked",true);
	    	}
			if(responseType == 'Scale' || responseType == 'Continuous Scale'){
	    		 $("#horizontal").attr("checked",true);
	    	}
		    if(responseType == 'Numeric'){
		    	 $('input[name="questionReponseTypeBo.style"]').attr("checked",false);
		    	 $("#styleDecimal").attr("checked",true);
		    }
		    if(responseType == 'Date'){
		    	$('input[name="questionReponseTypeBo.style"]').attr("checked",false); 
		    	$("#date").attr("checked",true);
		    }
		 }
		<c:forEach items="${questionResponseTypeMasterInfoList}" var="questionResponseTypeMasterInfo">
		 var infoId = Number('${questionResponseTypeMasterInfo.id}'); 
		 var responseType = '${questionResponseTypeMasterInfo.responseType}';
		 /* var type='';
		 if(responseType == 'Continuous Scale'){
			 type = 'Scale';
		 }else{
			 type = responseType;
			 $("#"+type.replace(/\s/g, '')).hide();
		 } */
		 $("#"+responseType.replace(/\s/g, '')).hide();
		 if(responseType == 'Date'){
			 var style = '${questionReponseTypeBo.style}';
			 console.log("style:"+style);
			 setResponseDate(style);
		 }
		 /* if(responseType == 'Value Picker'){
			if($('.value-picker').length > 2){
				$('.ValuePickerContainer').find(".remBtnDis").removeClass("hide");
			}else{
				$('.ValuePickerContainer').find(".remBtnDis").addClass("hide");
			}
		 } */
		 $("."+responseType.replace(/\s/g, '')+"Required").attr("required",false);
		 if(id == infoId){
    		var description = '${questionResponseTypeMasterInfo.description}';
    		var dataType = "${questionResponseTypeMasterInfo.dataType}";
    		var dashboard = '${questionResponseTypeMasterInfo.dashBoardAllowed}';
    		$("#responseTypeDataType").text(dataType);
    		$("#responseTypeDescrption").text(description);
    		$("#rlaResonseType").val(responseType);
    		$("#rlaResonseDataType").text(dataType);
    		$("#rlaResonseTypeDescription").text(description);
    		$("#"+responseType.replace(/\s/g, '')).show();
    		$("."+responseType.replace(/\s/g, '')+"Required").attr("required",true);
    		if(dashboard == 'true'){
    			$("#useStasticDataContainerId").show();
        		$("#addLineChartContainerId").show();	
        		$("#borderdashId").show();
        		console.log("ifff");
        		 if($("#addLineChart").is(":checked")){
        			 $("#chartContainer").show();
        			 $(".chartrequireClass").attr('required',true);
        		 }
        		 if($("#useStasticData").is(":checked")){
        			 $("#statContainer").show();
        			 $(".requireClass").attr('required',true);
        		 }
    		}else{
    			$("#useStasticDataContainerId").hide();
        		$("#addLineChartContainerId").hide();
        		$("#borderdashId").hide();
    		}
    		if(responseType == 'Date'){
   			 	$("#useAnchorDateContainerId").show();
	   		}else{
	   			$("#useAnchorDateContainerId").hide();
	   		}
    		if(responseType == 'Scale' || responseType == 'Continuous Scale' || responseType == 'Text Scale'){
    			$("#scaleType").show();
	   		}else{
	   			$("#scaleType").hide();
	   		}
    	 }
    	</c:forEach>
	}else{
		$("#responseTypeDataType").text("- NA -");
		$("#responseTypeDescrption").text("- NA -");
		$("#rlaResonseType").val('');
		$("#rlaResonseDataType").text("- NA -");
		$("#rlaResonseTypeDescription").text("- NA -");
	}
}
function saveQuestionStepQuestionnaire(item,callback){
	
	var fromId = $("#fromId").val();
	
	var questionsBo = new Object();
	
	var short_title = $("#shortTitle").val();
	var questionText = $("#questionTextId").val();
	var descriptionText = $("#descriptionId").val();
	var responseType = $("#responseTypeId").val();
	var addLinceChart = $('input[name="addLineChart"]:checked').val();
	var lineChartTimeRange = $("#lineChartTimeRangeId").val();
	var allowRollbackChart = $('input[name="allowRollbackChart"]:checked').val();
	var chartTitle = $('#chartTitleId').val();
	var useStasticData = $('input[name="useStasticData"]:checked').val();
	var statShortName = $("#statShortNameId").val();
	var statDisplayName = $("#statDisplayNameId").val();
	var statDisplayUnits = $("#statDisplayUnitsId").val();
	var statType=$("#statType").val();
	var statFormula=$("#statFormula").val();
	var questionid = $("#questionId").val();
	var skippableText = $('input[name="skippable"]:checked').val();
	var anchor_date = $('input[name="questionsBo.useAnchorDate"]:checked').val();
	
	console.log("questionid:"+questionid);
	questionsBo.shortTitle=short_title;
	questionsBo.question=questionText;
	questionsBo.description=descriptionText;
	questionsBo.responseType=responseType;
	questionsBo.lineChartTimeRange=lineChartTimeRange;
	questionsBo.addLineChart=addLinceChart;
	questionsBo.allowRollbackChart=allowRollbackChart;
	questionsBo.chartTitle=chartTitle;
	questionsBo.useStasticData=useStasticData;
	questionsBo.statShortName=statShortName;
	questionsBo.statDisplayName=statDisplayName;
	questionsBo.statDisplayUnits=statDisplayUnits;
	questionsBo.statType=statType;
	questionsBo.statFormula=statFormula;
	questionsBo.type="save";
	questionsBo.fromId=fromId;
	questionsBo.id = questionid;
	questionsBo.skippable=skippableText;
	questionsBo.useAnchorDate=anchor_date;
	var questionReponseTypeBo = new  Object();
	var minValue='';
	var maxValue='';
	var defaultValue='';
	var maxdescription='';
	var mindescrption='';
	var step='';
	var resType = $("#rlaResonseType").val();
	var verticalText = '';
	
	var formData = new FormData();
	
	if(resType == "Scale"){
		minValue = $("#scaleMinValueId").val();
		maxValue = $("#scaleMaxValueId").val();
		defaultValue = $("#scaleDefaultValueId").val();
		mindescrption = $("#scaleMinDescriptionId").val();
		maxdescription = $("#scaleMaxDescriptionId").val();
		step = $("#scaleStepId").val();
		verticalText = $('input[name="questionReponseTypeBo.vertical"]:checked').val();	
		
		questionReponseTypeBo.vertical=verticalText;
		questionReponseTypeBo.minValue=minValue;
		questionReponseTypeBo.maxValue=maxValue;
		questionReponseTypeBo.defaultValue=defaultValue;
		questionReponseTypeBo.minDescription=mindescrption;
		questionReponseTypeBo.maxDescription=maxdescription;
		questionReponseTypeBo.step=step;
	}else if(resType == "Continuous Scale"){
		
		minValue = $("#continuesScaleMinValueId").val();
		maxValue = $("#continuesScaleMaxValueId").val();
		defaultValue = $("#continuesScaleDefaultValueId").val();
		mindescrption = $("#continuesScaleMinDescriptionId").val();
		maxdescription = $("#continuesScaleMaxDescriptionId").val();
		vertical = $('input[name="questionReponseTypeBo.vertical"]:checked').val();	
		var fractionDigits = $("#continuesScaleFractionDigitsId").val();
		
		questionReponseTypeBo.vertical=verticalText;
		questionReponseTypeBo.minValue=minValue;
		questionReponseTypeBo.maxValue=maxValue;
		questionReponseTypeBo.defaultValue=defaultValue;
		questionReponseTypeBo.minDescription=mindescrption;
		questionReponseTypeBo.maxDescription=maxdescription;
		questionReponseTypeBo.maxFractionDigits=fractionDigits;
		
	}else if(resType == "Location"){
		var usecurrentlocation = $('input[name="questionReponseTypeBo.useCurrentLocation"]:checked').val();	
		questionReponseTypeBo.useCurrentLocation=usecurrentlocation;
	}else if(resType == "Email"){
		var placeholderText = $("#placeholderId").val();	
		questionReponseTypeBo.placeholder=placeholderText;
	}else if(resType == "Text"){
		var max_length = $("#textmaxLengthId").val();
		var placeholderText = $("#textPlaceholderId").val(); 
	    var multiple_lines = $('input[name="questionReponseTypeBo.multipleLines"]:checked').val();	
			
	    questionReponseTypeBo.maxLength = max_length;
	    questionReponseTypeBo.placeholder = placeholderText;
	    questionReponseTypeBo.multipleLines = multiple_lines;
	}else if(resType == "Height"){
		var measurement_system = $('input[name="questionReponseTypeBo.measurementSystem"]:checked').val();
		var placeholder_text = $("#heightPlaceholderId").val();
		questionReponseTypeBo.measurementSystem = measurement_system;
		questionReponseTypeBo.placeholder = placeholder_text;
	}else if(resType == "Time interval"){
		 var stepValue = $("#timeIntervalStepId").val();
		 questionReponseTypeBo.step=stepValue;
	}else if(resType == "Numeric"){
		var styletext = $('input[name="questionReponseTypeBo.style"]:checked').val();
		var unitText = $("#numericUnitId").val();
		var palceholder_text = $("#numericPlaceholderId").val(); 
		questionReponseTypeBo.style = styletext;
		questionReponseTypeBo.placeholder = palceholder_text;
		questionReponseTypeBo.unit=unitText;
	}else if(resType == "Date"){
		
		var min_date = $("#minDateId").val(); 
		var max_date = $("#maxDateId").val(); 
		var default_date = $("#defaultDate").val(); 
		var style=$('input[name="questionReponseTypeBo.style"]:checked').val();
		questionReponseTypeBo.minDate = min_date;
		questionReponseTypeBo.maxDate = max_date;
		questionReponseTypeBo.defaultDate = default_date;
		questionReponseTypeBo.style=style;
	}else if(resType == "Boolean"){
		var questionSubResponseArray  = new Array();
		$('#Boolean .row').each(function(){
			var questionSubResponseType = new Object();
			var id = $(this).attr("id");
			var response_sub_type_id = $("#responseSubTypeValueId"+id).val();
			var diasplay_text = $("#dispalyText"+id).val();
			var diaplay_value = $("#displayValue"+id).val();
			
			
			questionSubResponseType.responseSubTypeValueId=response_sub_type_id;
			questionSubResponseType.text=diasplay_text;
			questionSubResponseType.value=diaplay_value;
			
			
			questionSubResponseArray.push(questionSubResponseType);
		});
		questionsBo.questionResponseSubTypeList = questionSubResponseArray;
		
		
	}else if(resType == "Value Picker"){
		var questionSubResponseArray  = new Array();
		$('.value-picker').each(function(){
			var questionSubResponseType = new Object();
			var id = $(this).attr("id");
			console.log("id:"+id);
			var response_sub_type_id = $("#valPickSubTypeValueId"+id).val();
			var diasplay_text = $("#displayValPickText"+id).val();
			var diaplay_value = $("#displayValPickValue"+id).val();
			
			questionSubResponseType.responseSubTypeValueId=response_sub_type_id;
			questionSubResponseType.text=diasplay_text;
			questionSubResponseType.value=diaplay_value;
			questionSubResponseArray.push(questionSubResponseType);
		});
		questionsBo.questionResponseSubTypeList = questionSubResponseArray;
	} else if(resType == "Text Scale"){
		var questionSubResponseArray  = new Array();
		$('.text-scale').each(function(){
			
			var questionSubResponseType = new Object();
			var id = $(this).attr("id");
			console.log("id:"+id);
			
			var response_sub_type_id = $("#textScaleSubTypeValueId"+id).val();
			var diasplay_text = $("#displayTextSclText"+id).val();
			var diaplay_value = $("#displayTextSclValue"+id).val();
			
			
			console.log("diasplay_text:"+diasplay_text);
			console.log("diaplay_value:"+diaplay_value);
			
			
			questionSubResponseType.responseSubTypeValueId=response_sub_type_id;
			questionSubResponseType.text=diasplay_text;
			questionSubResponseType.value=diaplay_value;
			
			questionSubResponseArray.push(questionSubResponseType);
			
		});
		questionsBo.questionResponseSubTypeList = questionSubResponseArray;
	}else if(resType == "Text Choice"){
		
		var questionSubResponseArray  = new Array();
		var selectionStyel = $('input[name="questionReponseTypeBo.selectionStyle"]:checked').val();
		questionReponseTypeBo.selectionStyle = selectionStyel;
		$('.text-choice').each(function(){
			var questionSubResponseType = new Object();
			var id = $(this).attr("id");
			console.log("id:"+id);
			
			var response_sub_type_id = $("#textChoiceSubTypeValueId"+id).val();
			var diasplay_text = $("#displayTextChoiceText"+id).val();
			var diaplay_value = $("#displayTextChoiceValue"+id).val();
			var exclusioveText = $("#exclusiveId"+id).val();
			
			questionSubResponseType.responseSubTypeValueId=response_sub_type_id;
			questionSubResponseType.text=diasplay_text;
			questionSubResponseType.value=diaplay_value;
			questionSubResponseType.exclusive=exclusioveText;
			questionSubResponseArray.push(questionSubResponseType);
			
		});
		questionsBo.questionResponseSubTypeList = questionSubResponseArray;
	}else if(resType == "Image Choice"){
		var questionSubResponseArray  = new Array();
		var i=0;
		$('.image-choice').each(function(){
			var questionSubResponseType = new Object();
			var id = $(this).attr("id");
			console.log("id:"+id);
			
			var response_sub_type_id = $("#imageChoiceSubTypeValueId"+id).val();
			var diasplay_text = $("#displayImageChoiceText"+id).val();
			var diaplay_value = $("#displayImageChoiceValue"+id).val();
			
			
			var imagePath = $("#imagePathId"+id).val();
			var selectedImagePath = $("#selectImagePathId"+id).val();
		    
			formData.append('imageFile[' + id + ']', document.getElementById("imageFileId"+id).files[0]);
		    formData.append('selectImageFile[' + id + ']', document.getElementById("selectImageFileId"+id).files[0]);
			
			questionSubResponseType.responseSubTypeValueId=response_sub_type_id;
			questionSubResponseType.text=diasplay_text;
			questionSubResponseType.value=diaplay_value;
			questionSubResponseType.imageId=id;
			questionSubResponseType.image=imagePath;
			questionSubResponseType.selectedImage=selectedImagePath;
			
			questionSubResponseArray.push(questionSubResponseType);
			
			i=i+1;
		});
		questionsBo.questionResponseSubTypeList = questionSubResponseArray;
	}
	
	var response_type_id = $("#questionResponseTypeId").val();
	var question_response_type_id = $("#responseQuestionId").val();
	
	questionReponseTypeBo.responseTypeId=response_type_id;
	questionReponseTypeBo.questionsResponseTypeId=question_response_type_id;
	
	
	questionsBo.questionReponseTypeBo=questionReponseTypeBo;
	if(fromId != null && fromId!= '' && typeof fromId !='undefined' && 
			questionText != null && questionText!= '' && typeof questionText !='undefined' &&
			short_title != null && short_title!= '' && typeof short_title !='undefined'){
		formData.append("questionInfo", JSON.stringify(questionsBo)); 
		
		var data = JSON.stringify(questionsBo);
		$.ajax({ 
	          url: "/fdahpStudyDesigner/adminStudies/saveQuestion.do",
	          type: "POST",
	          datatype: "json",
	          data: formData,
	          processData: false,
           	  contentType: false,
	          beforeSend: function(xhr, settings){
	              xhr.setRequestHeader("X-CSRF-TOKEN", "${_csrf.token}");
	          },
	          success:function(data){
	        	var jsonobject = eval(data);			                       
				var message = jsonobject.message;
				if(message == "SUCCESS"){
					
					$("#preShortTitleId").val(short_title);
					var questionId = jsonobject.questionId;
					var questionResponseId = jsonobject.questionResponseId;
					console.log("questionResponseId:"+questionResponseId);
					$("#questionId").val(questionId);
					$("#questionResponseTypeId").val(questionResponseId);
					$("#responseQuestionId").val(questionId);
					
					$("#alertMsg").removeClass('e-box').addClass('s-box').html("Content saved as draft.");
					$(item).prop('disabled', false);
					$('#alertMsg').show();
					if (callback)
						callback(true);
					if($('.sixthQuestionnaires').find('span').hasClass('sprites-icons-2 tick pull-right mt-xs')){
						$('.sixthQuestionnaires').find('span').removeClass('sprites-icons-2 tick pull-right mt-xs');
					}
				}else{
					$("#alertMsg").removeClass('s-box').addClass('e-box').html("Something went Wrong");
					$('#alertMsg').show();
					if (callback)
  						callback(false);
				}
				setTimeout(hideDisplayMessage, 4000);
	          },
	          error: function(xhr, status, error) {
    			  $(item).prop('disabled', false);
    			  $('#alertMsg').show();
    			  $("#alertMsg").removeClass('s-box').addClass('e-box').html("Something went Wrong");
    			  setTimeout(hideDisplayMessage, 4000);
    		  }
	   }); 
	}else{
		if(questionText == null || questionText == '' || typeof questionText =='undefined' ){
			$('#questionTextId').validator('destroy').validator();
			if(!$('#questionTextId')[0].checkValidity()) {
				$("#questionTextId").parent().addClass('has-error has-danger').find(".help-block").empty().append('<ul class="list-unstyled"><li>This is a required field.</li></ul>');
				$('.questionLevel a').tab('show');
			}
		}
		if(short_title == null || short_title== '' || typeof short_title =='undefined' ){
			$('#shortTitle').validator('destroy').validator();
			if(!$('#shortTitle')[0].checkValidity()) {
				$("#shortTitle").parent().addClass('has-error has-danger').find(".help-block").empty().append('<ul class="list-unstyled"><li>This is a required field.</li></ul>');
				$('.questionLevel a').tab('show');
			}
		}
	}
}
function goToBackPage(item){
	//window.history.back();
	$(item).prop('disabled', true);
	<c:if test="${actionTypeForFormStep ne 'view'}">
		bootbox.confirm({
				closeButton: false,
				message : 'You are about to leave the page and any unsaved changes will be lost. Are you sure you want to proceed?',	
			    buttons: {
			        'cancel': {
			            label: 'Cancel',
			        },
			        'confirm': {
			            label: 'OK',
			        },
			    },
			    callback: function(result) {
			        if (result) {
			        	var a = document.createElement('a');
			        	a.href = "/fdahpStudyDesigner/adminStudies/formStep.do";
			        	document.body.appendChild(a).click();
			        }else{
			        	$(item).prop('disabled', false);
			        }
			    }
		});
	</c:if>
	<c:if test="${actionTypeForFormStep eq 'view'}">
		var a = document.createElement('a');
		a.href = "/fdahpStudyDesigner/adminStudies/formStep.do";
		document.body.appendChild(a).click();
	</c:if>
}
function getSelectionStyle(item){
	var value= $(item).val();
	if(value == 'Single'){
		$('.textChoiceExclusive').attr("disabled",true);
		$('.textChoiceExclusive').attr("required",false);
		$('.textChoiceExclusive').val('');
		//$('.destionationYes').val('');
		//$('.destionationYes').attr("disabled",false);
		$('.selectpicker').selectpicker('refresh');
		$(".textChoiceExclusive").validator('validate');
	}else{
		$('.textChoiceExclusive').attr("disabled",false);
		$('.textChoiceExclusive').attr("required",true);
		$('.selectpicker').selectpicker('refresh');
	}
}
/* function setExclusiveData(item){
	var index = $(item).attr('index');
	var value = $(item).val();
	if(value == "Yes"){
		$("#destinationTextChoiceStepId"+index).attr("disabled",false);
		$('.selectpicker').selectpicker('refresh');
	}else{
		$("#destinationTextChoiceStepId"+index).val('');
		$("#destinationTextChoiceStepId"+index).attr("disabled",true);
		$('.selectpicker').selectpicker('refresh');
	}
	console.log("index:"+index);
	console.log("value:"+value);
} */
var count = $('.value-picker').length;
function addValuePicker(){
	count = count+1;
	var newValuePicker ="<div class='value-picker row form-group' id="+count+">"+
						"	<div class='col-md-3 pl-none'>"+
						"   <div class='form-group'>"+
						"      <input type='text' class='form-control' name='questionResponseSubTypeList["+count+"].text' id='displayValPickText"+count+"' required maxlength='15'>"+
						"      <div class='help-block with-errors red-txt'></div>"+
						"   </div>"+
						"</div>"+
						"<div class='col-md-4 pl-none'>"+
						"   <div class='form-group'>"+
						"      <input type='text' class='form-control' name='questionResponseSubTypeList["+count+"].value' id='displayValPickValue"+count+"' required maxlength='50'>"+
						"      <div class='help-block with-errors red-txt'></div>"+
						"   </div>"+
						"</div>"+
						"<div class='col-md-2 pl-none mt-md'>"+
						"   <span class='addBtnDis addbtn mr-sm align-span-center' onclick='addValuePicker();'>+</span>"+
					    "<span class='delete vertical-align-middle remBtnDis hide pl-md align-span-center' onclick='removeValuePicker(this);'></span>"+
						"</div>"+
					"</div>";	
	$(".value-picker:last").after(newValuePicker);
	$(".value-picker").parents("form").validator("destroy");
    $(".value-picker").parents("form").validator();
	if($('.value-picker').length > 2){
		$(".remBtnDis").removeClass("hide");
	}else{
		$(".remBtnDis").addClass("hide");
	}
}
function removeValuePicker(param){
	if($('.value-picker').length > 2){
		
		$(param).parents(".value-picker").remove();
	    $(".value-picker").parents("form").validator("destroy");
		$(".value-picker").parents("form").validator();
		if($('.value-picker').length > 2){
			$(".remBtnDis").removeClass("hide");
		}else{
			$(".remBtnDis").addClass("hide");
		}
	}
}
var scaleCount = $('.text-scale').length;
function addTextScale(){
	scaleCount = scaleCount+1;
	if($('.text-scale').length < 8){
	var newTextScale = "<div class='text-scale row' id="+scaleCount+">"+
					    "	<div class='col-md-3 pl-none'>"+
					    "    <div class='form-group'>"+
				        "      <input type='text' class='form-control TextScaleRequired' name='questionResponseSubTypeList["+scaleCount+"].text' id='displayTextSclText"+scaleCount+"'+  maxlength='15' required>"+
					    "      <div class='help-block with-errors red-txt'></div>"+
					    "   </div>"+
						"</div>"+
						" <div class='col-md-4 pl-none'>"+
						"    <div class='form-group'>"+
						"       <input type='text' class='form-control TextScaleRequired' class='form-control' name='questionResponseSubTypeList["+scaleCount+"].value' id='displayTextSclValue"+scaleCount+"' maxlength='50' required>"+
						"       <div class='help-block with-errors red-txt'></div>"+
						"    </div>"+
						" </div>";
						newTextScale+="<div class='col-md-2 pl-none mt-md'>"+
						"	<span class='addBtnDis addbtn mr-sm align-span-center' onclick='addTextScale();'>+</span>"+
						"  <span class='delete vertical-align-middle remBtnDis hide pl-md align-span-center' onclick='removeTextScale(this);'></span>"+
						"	</div>"+
						"</div>";
	$(".text-scale:last").after(newTextScale);
	$('.selectpicker').selectpicker('refresh');
	$(".text-scale").parents("form").validator("destroy");
    $(".text-scale").parents("form").validator();
	if($('.text-scale').length > 2){
		$(".remBtnDis").removeClass("hide");
	}else{
		$(".remBtnDis").addClass("hide");
	}
	}
}
function removeTextScale(param){
	if($('.text-scale').length > 2){
		$(param).parents(".text-scale").remove();
	    $(".text-scale").parents("form").validator("destroy");
		$(".text-scale").parents("form").validator();
		if($('.text-scale').length > 2){
			$(".remBtnDis").removeClass("hide");
		}else{
			$(".remBtnDis").addClass("hide");
		}
		$("#textScalePositionId").val($('.text-scale').length);
	}
}
var choiceCount = $('.text-scale').length;
function addTextChoice(){
	choiceCount = choiceCount+1;
	var selectionStyle = $('input[name="questionReponseTypeBo.selectionStyle"]:checked').val();
	var newTextChoice = "<div class='col-md-12 p-none text-choice row' id='"+choiceCount+"'>"+
						"	   <div class='col-md-4 pl-none'>"+
					    "<div class='form-group'>"+
					    "   <input type='text' class='form-control TextChoiceRequired' name='questionResponseSubTypeList["+choiceCount+"].text' id='displayTextSclText'  maxlength='15' required>"+
					    "   <div class='help-block with-errors red-txt'></div>"+
					    "</div>"+
					 	"  </div>"+
					 	"<div class='col-md-4 pl-none'>"+
					    "<div class='form-group'>"+
					    "   <input type='text' class='form-control TextChoiceRequired' name='questionResponseSubTypeList["+choiceCount+"].value' id='displayTextSclValue'  maxlength='50' required>"+
					    "   <div class='help-block with-errors red-txt'></div>"+
					    "</div>"+
					 	"</div>"+
					 	"<div class='col-md-2 pl-none'>"+
					    "<div class='form-group'>";
					    if(selectionStyle == 'Single'){
					    	newTextChoice += "<select name='questionResponseSubTypeList["+choiceCount+"].exclusive' id='exclusiveId"+choiceCount+"' index="+choiceCount+" title='select' data-error='Please choose one option' class='selectpicker TextChoiceRequired textChoiceExclusive' disabled >";
					    }else{
					    	newTextChoice += "<select name='questionResponseSubTypeList["+choiceCount+"].exclusive' id='exclusiveId"+choiceCount+"' index="+choiceCount+" title='select' data-error='Please choose one option' class='selectpicker TextChoiceRequired textChoiceExclusive' required >";
					    }
					    newTextChoice += "<option value='Yes'>Yes</option>"+
					    "        <option value='No'>No</option>"+
					    "    </select>"+
					    "   <div class='help-block with-errors red-txt'></div>"+
					    "</div>"+
					    "</div>";
					   newTextChoice += "<div class='col-md-2 pl-none mt-md'>"+
					    "<span class='addBtnDis addbtn mr-sm align-span-center' onclick='addTextChoice();'>+</span>"+
					    "<span class='delete vertical-align-middle remBtnDis hide pl-md align-span-center' onclick='removeTextChoice(this);'></span>"+
					 "</div>"+
					"</div> ";
	$(".text-choice:last").after(newTextChoice);
	$('.selectpicker').selectpicker('refresh');
	$(".text-choice").parents("form").validator("destroy");
	$(".text-choice").parents("form").validator();
	if($('.text-choice').length > 2){
		$(".remBtnDis").removeClass("hide");
	}else{
		$(".remBtnDis").addClass("hide");
	}
}
function removeTextChoice(param){
	if($('.text-choice').length > 2){
		$(param).parents(".text-choice").remove();
	    $(".text-choice").parents("form").validator("destroy");
		$(".text-choice").parents("form").validator();
		if($('.text-choice').length > 2){
			$(".remBtnDis").removeClass("hide");
		}else{
			$(".remBtnDis").addClass("hide");
		}
	}
}
var imageCount = $('.image-choice').length;
function addImageChoice(){
	imageCount = imageCount+1;
	var newImageChoice = "<div class='image-choice row' id='"+imageCount+"'>"+
						 "	   <div class='col-md-2 pl-none col-smthumb-2'>"+
						 "   <div class='form-group'>"+
						 "      <div class='sm-thumb-btn'>"+
						 "         <div class='thumb-img'><img src='../images/icons/sm-thumb.jpg'/></div>"+
						 "         <div class='textLabelimagePathId"+imageCount+"'>Upload</div>"+
						 "      </div>"+
						 "      <input class='dis-none ImageChoiceRequired upload-image' data-imageId='"+imageCount+"' name='questionResponseSubTypeList["+imageCount+"].imageFile' id='imageFileId"+imageCount+"' type='file'  accept='.png, .jpg, .jpeg' onchange='readURL(this);' required>"+
						 "		<input type='hidden' name='questionResponseSubTypeList["+imageCount+"].image' id='imagePathId"+imageCount+"' >"+
						 "      <div class='help-block with-errors red-txt'></div>"+
						 "   </div>"+
						 "</div>"+
						 "<div class='col-md-2 pl-none col-smthumb-2'>"+
						 "   <div class='form-group'>"+
						 "      <div class='sm-thumb-btn'>"+
						 "         <div class='thumb-img'><img src='../images/icons/sm-thumb.jpg'/></div>"+
						 "         <div class='textLabelselectImagePathId"+imageCount+"'>Upload</div>"+
						 "      </div>"+
						 "      <input class='dis-none ImageChoiceRequired upload-image' data-imageId='"+imageCount+"' name='questionResponseSubTypeList["+imageCount+"].selectImageFile' id='selectImageFileId"+imageCount+"' type='file'  accept='.png, .jpg, .jpeg' onchange='readURL(this);' required>"+
						 "		<input type='hidden' name='questionResponseSubTypeList["+imageCount+"].selectedImage' id='selectImagePathId"+imageCount+"'>"+
						 "      <div class='help-block with-errors red-txt'></div>"+
						 "   </div>"+
						 "</div>"+
						 "<div class='col-md-3 pl-none'>"+
						 "   <div class='form-group'>"+
						 "      <input type='text' class='form-control ImageChoiceRequired' name='questionResponseSubTypeList["+imageCount+"].text' id='displayImageChoiceText"+imageCount+"' required maxlength='15'>"+
						 "      <div class='help-block with-errors red-txt'></div>"+
						 "   </div>"+
						 "</div>"+
						 "<div class='col-md-3 col-lg-3 pl-none'>"+
						 "   <div class='form-group'>"+
						 "      <input type='text' class='form-control ImageChoiceRequired' name='questionResponseSubTypeList["+imageCount+"].value' id='displayImageChoiceValue"+imageCount+"' required maxlength='50'>"+
						 "      <div class='help-block with-errors red-txt'></div>"+
						 "   </div>"+
						 "</div>";
						 newImageChoice +="<div class='col-md-2 pl-none mt-sm'>"+
						 "   <span class='addBtnDis addbtn mr-sm align-span-center' onclick='addImageChoice();'>+</span>"+
						 "	  <span class='delete vertical-align-middle remBtnDis hide pl-md align-span-center' onclick='removeImageChoice(this);'></span>"+
						 "</div>"+
						"</div> ";
	$(".image-choice:last").after(newImageChoice);
	$('.selectpicker').selectpicker('refresh');
	$(".image-choice").parents("form").validator("destroy");
	$(".image-choice").parents("form").validator();
	$(".sm-thumb-btn").click(function(){
		   $(this).next().click();
    });
	if($('.image-choice').length > 2){
		$(".remBtnDis").removeClass("hide");
	}else{
		$(".remBtnDis").addClass("hide");
	}
}
function removeImageChoice(param){
	if($('.image-choice').length > 2){
		$(param).parents(".image-choice").remove();
	    $(".image-choice").parents("form").validator("destroy");
		$(".image-choice").parents("form").validator();
		if($('.image-choice').length > 2){
			$(".remBtnDis").removeClass("hide");
		}else{
			$(".remBtnDis").addClass("hide");
		}
	}
}
</script>