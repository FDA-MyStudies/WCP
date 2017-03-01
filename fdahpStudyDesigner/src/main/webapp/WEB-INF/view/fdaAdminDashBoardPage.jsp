<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page import="com.fdahpStudyDesigner.util.SessionObject"%>
<!DOCTYPE html>
<html class="overflow-hidden">
	<head>
        
    <!-- Basic -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
            
    <title>FDA</title>	
    
    <meta name="description" content="">
    <meta name="keywords" content="">
    <meta name="author" content="">

    <!-- Favicon -->
    <link rel="shortcut icon" href="#" type="image/x-icon" />
    <link rel="apple-touch-icon" href="#">
        
    <!-- Mobile Metas -->
    <meta name="viewport" content="width=device-width, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
        
    <!-- Web Fonts  -->
   <link href="https://fonts.googleapis.com/css?family=Roboto:300,400" rel="stylesheet">
        
    <!-- Vendor CSS -->
    <link rel="stylesheet" href="/fdahpStudyDesigner/vendor/boostrap/bootstrap.min.css">
    <link rel="stylesheet" href="/fdahpStudyDesigner/vendor/datatable/css/dataTables.bootstrap.min.css">
    <link rel="stylesheet" href="/fdahpStudyDesigner/vendor/dragula/dragula.min.css">
    <link rel="stylesheet" href="/fdahpStudyDesigner/vendor/magnific-popup/magnific-popup.css">        
    <link rel="stylesheet" href="/fdahpStudyDesigner/vendor/animation/animate.css">    
        
    <!-- Theme Responsive CSS -->
    <link rel="stylesheet" href="/fdahpStudyDesigner/css/sprites.css">   
    <link rel="stylesheet" href="/fdahpStudyDesigner/css/layout.css">   
        
    <!-- Theme CSS -->
    <link rel="stylesheet" href="/fdahpStudyDesigner/css/theme.css">
    <link rel="stylesheet" href="/fdahpStudyDesigner/css/style.css">
        
    <!-- Head Libs -->
    <script src="/fdahpStudyDesigner/vendor/modernizr/modernizr.js"></script>
        
</head>
<body class="white-bg">
     <form:form action="" name="studyListForm" id="studyListForm" method="post">
     </form:form>
     <c:url value="/j_spring_security_logout" var="logoutUrl" />
	<form action="${logoutUrl}" method="post" id="logoutForm">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	</form>
    <div class="lg-container">
        
        <!-- Login Left Section-->
        <div class="lg-space-left">
            <div class="lg-space-img">
                <img src="/fdahpStudyDesigner/images/logo/fda-logo-w.png"/>
            </div>
            <div class="lg-space-txt">
               Health Study <br>Management Portal
            </div>
             <div class="lg-space-cover">
                <img src="/fdahpStudyDesigner/images/icons/web.png"/>
            </div>
        </div>
        <!-- End Login Left Section-->
        <!-- <div>
          <a href="javascript:formSubmit();">Logout</a>
        </div> -->
        <!-- Login Right Section-->
        <div class="lg-space-right">
        	<div class="logout">
               <div class="dis-line pull-right ml-md line34">
                 <a href="javascript:formSubmit();" class="blue-link text-weight-normal text-uppercase"><span>Log Out</span> <span class="ml-xs"><img src="/fdahpStudyDesigner/images/icons/logout.png"/></span></a>  
               </div>
           	</div>
            <div class="lg-space-container wd">
                <div class="lg-space-center">
	                <div class="lg-space-title">
	                    <span>Welcome,</span><span>${sessionObject.firstName}</span>
	                </div>
	                <div class='lg-icons'> 
	                   <ul class="lg-icons-list"> 
	                    <li class="studyListId">
	                        <a class='studies-g' href='#'></a>
	                        <div class='studyList'>Manage Studies</div>
	                    </li>
	                    <li>
	                        <a class='repository-g' href='#'></a>
	                        <div>Manage Repository</div>
	                    </li> 
	                    <li class="notificationListId">
	                        <a class='notifications-g' href='#'></a>
	                        <div>Manage Notifications</div>
	                    </li> 
	                   <li class="userListId">
	                        <a class='user-g' href='#'></a>
	                        <div>Manage Users</div>
	                    </li> 
	                    <li class="myAccountId">
	                        <a class='account-g' href='#'></a>
	                        <div>My Account</div>
	                    </li>
	                 </ul> 
	                </div>
                </div>
               <div class="clearfix"></div>
               <div class="footer">
                    <span>Copyright � 2016 FDA</span><span><a href="#">Terms</a></span><span><a href="#">Privacy Policy</a></span>
              </div>
            </div>
            
             
            
             
        </div>
        <!-- End Login Right Section-->
        
    </div>
    
    
    <!-- Vendor -->
    <script src="/fdahpStudyDesigner/vendor/jquery/jquery-3.1.1.min.js"></script>
    <script src="/fdahpStudyDesigner/vendor/boostrap/bootstrap.min.js"></script>
    <script src="/fdahpStudyDesigner/vendor/animation/wow.min.js"></script>
    <script src="/fdahpStudyDesigner/vendor/datatable/js/jquery.dataTables.min.js"></script>
    <script src="/fdahpStudyDesigner/vendor/dragula/react-dragula.min.js"></script>
    <script src="/fdahpStudyDesigner/vendor/magnific-popup/jquery.magnific-popup.min.js"></script>    
    <script src="/fdahpStudyDesigner/vendor/slimscroll/jquery.slimscroll.min.js"></script>
    
    
    <!-- Theme Custom JS-->
    <script src="/fdahpStudyDesigner/js/theme.js"></script>
    <script src="/fdahpStudyDesigner/js/common.js"></script>
    
    <script>
      (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
      (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
      m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
      })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

      ga('create', 'UA-71064806-1', 'auto');
      ga('send', 'pageview');
	function formSubmit() {
		document.getElementById("logoutForm").submit();
	}
    $(document).ready(function(e) {
    	<c:if test="${not fn:contains(sessionObject.userPermissions,'ROLE_MANAGE_STUDIES')}">
    	 $(".studyListId").addClass('cursor-none');
    	 $(".studyListId").unbind();
    	</c:if>
    	<c:if test="${not fn:contains(sessionObject.userPermissions,'ROLE_MANAGE_USERS_VIEW')}"> 
    	 $(".userListId").addClass('cursor-none');
    	 $(".userListId").unbind();
    	</c:if>
    	 <c:if test="${not fn:contains(sessionObject.userPermissions,'ROLE_MANAGE_APP_WIDE_NOTIFICATION_VIEW')}">
    	 $(".notificationListId").addClass('cursor-none');
    	 $(".notificationListId").unbind();
    	 </c:if>
    	 
    	 
    	 $(".studyListId").click(function(){	
    		document.studyListForm.action="/fdahpStudyDesigner/adminStudies/studyList.do";
    		document.studyListForm.submit();
    	 });
    	 
    	 $(".userListId").click(function(){	
    		document.studyListForm.action="/fdahpStudyDesigner/adminUsersView/getUserList.do";
    		document.studyListForm.submit();
    	 });
    	 
    	 /* $(".notificationListId").click(function(){	
     		document.studyListForm.action="/fdahpStudyDesigner/adminNotificationView/viewNotificationList.do";
     		document.studyListForm.submit();
     	 }); */
    	 $(".myAccountId").click(function(){	
      		document.studyListForm.action="/fdahpStudyDesigner/adminDashboard/viewUserDetails.do";
      		document.studyListForm.submit();
      	 });
    });
    <c:if test="${param.action eq 'landing'}">
    /* function noBack() { 
  	  history.pushState(null, null, 'login.do');
  	   window.addEventListener('popstate', function(event) {
  	     history.pushState(null, null, 'login.do');
  	  }); 
    } */
    window.onload = function () {
      if (typeof history.pushState === "function") {
          history.pushState("jibberish", null, null);
          window.onpopstate = function () {
              history.pushState('newjibberish', null, null);
              // Handle the back (or forward) buttons here
              // Will NOT handle refresh, use onbeforeunload for this.
          };
      }
      else {
          var ignoreHashChange = true;
          window.onhashchange = function () {
              if (!ignoreHashChange) {
                  ignoreHashChange = true;
                  window.location.hash = Math.random();
                  // Detect and redirect change here
                  // Works in older FF and IE9
                  // * it does mess with your hash symbol (anchor?) pound sign
                  // delimiter on the end of the URL
              }
              else {
                  ignoreHashChange = false;   
              }
          };
      }
  }
  </c:if>
    </script>
</body>
</html>