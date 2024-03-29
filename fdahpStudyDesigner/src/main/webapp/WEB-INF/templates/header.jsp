<%@page import="com.fdahpstudydesigner.util.SessionObject"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:url value="/j_spring_security_logout" var="logoutUrl" />

<style nonce="${nonce}">
.navbar-inverse{
padding:0px;
}
/*.navbar-inverse::before{
display:table;
}*/
.container-fluid{
flex-wrap:initial !important;
}
/*.container-fluid::before{
display:table;
}*/
.container-fluid > div {
display:flex !important;
}
.navbar-toggle{
display:contents;
}
.navbar-nav {
/*display:contents;*/
display: -webkit-box;
}
.dropdown-toggle::after{
display:none;
}
.navbar-nav .dropdown-menu {
    position: absolute;

}
.navbar-collapse > div {
display:flex;
}
</style>
<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 p-none white-bg hd_con">
     <div class="md-container">
         
         <!-- Navigation Menu-->
         <nav class="navbar navbar-inverse">
          <div class="container-fluid p-none">
            <div class="navbar-header">
              <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>                        
              </button>
              <a class="navbar-brand pt-none pb-none" href="javascript:void(0)" id="landingScreen"><img src="/fdahpStudyDesigner/images/logo/logo-sm.png"/></a>
            </div>
            <div class="collapse navbar-collapse p-none" id="myNavbar">
            <div>
              <ul class="nav navbar-nav">
              <c:if test="${fn:contains(sessionObject.userPermissions,'ROLE_MANAGE_STUDIES')}">
                <li class="studyClass"><a href="javascript:void(0)" id="studySection" >Studies</a></li>
              </c:if>
                <!-- <li class="dropdown">
                  <a class="dropdown-toggle" data-toggle="dropdown" href="javascript:void(0)">repository <span><i class="fa fa-angle-down" aria-hidden="true"></i></span></a>
                  <ul class="dropdown-menu">
                    <li><a href="javascript:void(0)">Reference Tables</a></li>
                    <li><a href="javascript:void(0)">QA content</a></li>
                    <li><a href="javascript:void(0)">Resources</a></li>
                    <li><a href="javascript:void(0)">Gateway app level content</a></li>
                    <li><a href="javascript:void(0)">Legal Text</a></li>
                  </ul>
                </li> -->
                <c:if test="${fn:contains(sessionObject.userPermissions,'ROLE_MANAGE_APP_WIDE_NOTIFICATION_VIEW')}">
                <li id="notification" class=""><a href="javascript:void(0)" id="manageNotificationSection">Notifications</a></li>
                </c:if>
                <c:if test="${fn:contains(sessionObject.userPermissions,'ROLE_MANAGE_USERS_VIEW')}">
                <li id="users"><a href="javascript:void(0)" id="usersSection">Users</a></li>
                </c:if>
                <c:if test="${fn:contains(sessionObject.userPermissions,'ROLE_MANAGE_STUDIES')}">
                    <li class="appClass"><a href="javascript:void(0)" id="appSection" >App Force Update</a></li>
                </c:if>
              </ul>
              </div>


              
              

               
               

            </div>
            <div>
                          <ul class="nav navbar-nav navbar-right">
                          		        <li id="myAccount" class="dropdown ml-lg userLi">
                          		          <a class="dropdown-toggle blue-link" data-toggle="dropdown" href="javascript:void(0)">${sessionObject.firstName} ${sessionObject.lastName} &nbsp;<i class="fa fa-angle-down" aria-hidden="true"></i></a>
                          		          <ul class="dropdown-menu pb-none pt-none profileBox mt-3">

                          		            <li class="linkProf"><a href="javascript:void(0)" class="blue-link text-weight-normal text-uppercase" id="profileSection">My Account</a><hr align="left" width="100%"><a href="/fdahpStudyDesigner/sessionOut.do" class="blue-link text-weight-normal text-uppercase"><span>sign Out</span> <span class="ml-xs"><img src="/fdahpStudyDesigner/images/icons/logout.png"/></span></a></li>
                          		          </ul>
                          		          </li>
                                         </ul>
                          </div>
          </div>
        </nav>   
         
     </div>
 </div>
 
<form action="${logoutUrl}" method="post" id="logoutForm">
	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" id="logoutCsrf"/>
</form>
<%--<form:form action="/fdahpStudyDesigner/adminUsersView/getUserList.do" id="userListForm" name="userListForm" method="post">--%>
<%--</form:form>--%>
<form:form action="/fdahpStudyDesigner/adminNotificationView/viewNotificationList.do" id="manageNotificationForm" name="manageNotificationForm" method="post">
</form:form>
<form:form action="/fdahpStudyDesigner/adminDashboard/viewUserDetails.do" id="myAccountForm" name="myAccountForm" method="post">
</form:form>
<form:form action="/fdahpStudyDesigner/adminStudies/studyList.do" id="adminStudyDashForm" name="adminStudyDashForm" method="post">
</form:form>
<%--<form:form action="/fdahpStudyDesigner/adminDashboard/viewDashBoard.do" id="landingPageForm" name="landingPageForm" method="post">--%>
<%--</form:form>--%>
 <script type="text/javascript" nonce="${nonce}">
 $(document).ready(function(){
 	var a = document.createElement('a');
 	$('#usersSection').on('click',function(){
//  		$('#userListForm').submit();
		a.href = "/fdahpStudyDesigner/adminUsersView/getUserList.do?${_csrf.parameterName}=${_csrf.token}";
		document.body.appendChild(a).click();
 	});

    $('#appSection').on('click',function(){
        a.href = "/fdahpStudyDesigner/adminApps/getAppsList.do";
        document.body.appendChild(a).click();
    });
 	
 	$('#manageNotificationSection').on('click',function(){
//  		$('#manageNotificationForm').submit();
		a.href = "/fdahpStudyDesigner/adminNotificationView/viewNotificationList.do";
		document.body.appendChild(a).click();
 	});
 	
 	$('#profileSection').on('click',function(){
//  		$('#myAccountForm').submit();
		a.href = "/fdahpStudyDesigner/adminDashboard/viewUserDetails.do";
		document.body.appendChild(a).click();
 	});
 	
 	$('#studySection').on('click',function(){
//  		$('#adminStudyDashForm').submit();
		a.href = "/fdahpStudyDesigner/adminStudies/studyList.do";
		document.body.appendChild(a).click();
 	});
 	
 	$('#landingScreen').on('click',function(){
//  		$('#landingPageForm').submit();
      console.log('login called from header.jsp...');
		a.href = "/fdahpStudyDesigner/adminDashboard/viewDashBoard.do";
		document.body.appendChild(a).click();
 	});
 	
 });
	function formSubmit() {
			document.getElementById("logoutForm").submit();
// 		var a = document.createElement('a');
// 		a.href = "fdahpStudyDesigner/${logoutUrl}";
// 		document.body.appendChild(a).click();
	}
 </script>
 <!-- browser back button disable code here -->
  <script type="text/javascript" nonce="${nonce}">
  history.pushState(null, null, location.href);
  history.back();
  history.forward();
  window.onpopstate = function () { history.go(1); };
</script> 