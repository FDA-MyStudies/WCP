<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page import="com.fdahpStudyDesigner.util.SessionObject"%>

<!-- Start left Content here -->
         <!-- ============================================================== -->        
        <div class="left-content">
            <div class="left-content-container">
                <ul class="menuNav">
                    <li class="first active">
                    	1.  Basic Information 
	                    <c:if test="${studyBo.studySequenceBo.basicInfo}">
	                    	<span class="sprites-icons-2 tick pull-right mt-xs" ></span>
	                    </c:if>
                    </li>
                    <li class="second">
                    	2.  Settings and Admins
                    	<c:if test="${studyBo.studySequenceBo.settingAdmins}">
	                    	<span class="sprites-icons-2 tick pull-right mt-xs" ></span>
	                    </c:if>
                    </li>
                    <li class="third">
                    	3.  Overview
                    	<c:if test="${studyBo.studySequenceBo.overView}">
	                    	<span class="sprites-icons-2 tick pull-right mt-xs" ></span>
	                    </c:if>
                    </li>
                    <li class="fourth">
                    	4.  Eligibility
                    	<c:if test="${studyBo.studySequenceBo.eligibility}">
	                    	<span class="sprites-icons-2 tick pull-right mt-xs" ></span>
	                    </c:if>
                    </li>
                    <li class="fifth">
                    	5.  Consent
                    	 <c:if test="${studyBo.studySequenceBo.consentEduInfo}">
	                    	<span class="sprites-icons-2 tick pull-right mt-xs" ></span>
	                    </c:if>
                    </li>
                    <li class="sub fifthConsent"><span class="dot"></span> Consent / Edu. Info</li>
                    <li class="sub fifthComre"><span class="dot"></span> Comprehension Test</li>
                    <li class="sub fifthConsentReview"><span class="dot"></span> Review and E-consent</li>
                    <li class="sixth">
                    	6.  Study Exercises
                    	<%-- <c:if test="${studyBo.studySequenceBo.comprehensionTest}">
	                    	<span class="sprites-icons-2 tick pull-right mt-xs" ></span>
	                    </c:if> --%>
                    </li>
                    <li class="seventh">
                    	7.  Study Dashboard
                    	<%-- <c:if test="${studyBo.studySequenceBo.eConsent}">
	                    	<span class="sprites-icons-2 tick pull-right mt-xs" ></span>
	                    </c:if> --%>
                    </li>
                    <li class="eigth">
                    	8.  Miscellaneous
                    	<%-- <c:if test="${studyBo.studySequenceBo.studyExcQuestionnaries}">
	                    	<span class="sprites-icons-2 tick pull-right mt-xs" ></span>
	                    </c:if> --%>
                    </li>
                    <li class="nine">
                    	9.  Checklist
                    	<%-- <c:if test="${studyBo.studySequenceBo.studyExcActiveTask}">
	                    	<span class="sprites-icons-2 tick pull-right mt-xs" ></span>
	                    </c:if> --%>
                    </li>
                    <li class="ten">
                    	10.  Actions
                    	<%-- <c:if test="${studyBo.studySequenceBo.basicInfo}">
	                    	<span class="sprites-icons-2 tick pull-right mt-xs" ></span>
	                    </c:if> --%>
                    </li>                 
                </ul>
            </div>
        </div>
        
        <!-- End left Content here -->
<script type="text/javascript">
$(document).ready(function(){
   $('#createStudyId').show();
   // Fancy Scroll Bar
   $(".left-content").niceScroll({cursorcolor:"#95a2ab",cursorborder:"1px solid #95a2ab"});
   $(".right-content-body").niceScroll({cursorcolor:"#d5dee3",cursorborder:"1px solid #d5dee3"});
   $("#myNavbar li.studyClass").addClass('active');
   
   var a = document.createElement('a');
   $('.first').click(function() {
		a.href = "/fdahpStudyDesigner/adminStudies/viewBasicInfo.do";
		document.body.appendChild(a).click();
	});
   
   <c:if test="${not empty studyBo.id}">
	   $('.second').click(function() {
			a.href = "/fdahpStudyDesigner/adminStudies/viewSettingAndAdmins.do";
			document.body.appendChild(a).click();
		});
	   $('.fourth').click(function() {
			a.href = "/fdahpStudyDesigner/adminStudies/viewStudyEligibilty.do";
			document.body.appendChild(a).click();
		});
   </c:if>
});
</script>
