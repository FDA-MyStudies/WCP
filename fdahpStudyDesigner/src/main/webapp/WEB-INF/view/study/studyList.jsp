<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<head>
    <meta charset="UTF-8">
</head>
<style>
  #studies_list_wrapper {
    width: 100%;
  }

  table thead {
    background: #fff;
  }

</style>
<div style="display:contents;">
    <table id="studies_list" class="table wid100 tbl">
        <thead>
        <tr>
            <th style="display: none;"><span class="sort"></span></th>
            <th style="display: none;">Live Study ID <span class="sort"></span></th>
            <th>Study ID <span class="sort"></span></th>
            <th>Study name <span class="sort"></span></th>
            <th>Study Category <span class="sort"></span></th>
            <th>FDA PROJECT LEAD <span class="sort"></span></th>
            <th>Research Sponsor <span class="sort"></span></th>
            <th>Status <span class="sort"></span></th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${studyBos}" var="study">
            <tr>
                <td style="display: none;">${study.createdOn}</td>
                <td style="display: none;">${study.liveStudyId}</td>
                <td>${study.customStudyId}</td>
                <td>
                    <div class="studylist-txtoverflow"
                         title="${fn:escapeXml(study.name)}">${study.name}</div>
                </td>
                <td>${study.category}</td>
                <td>
                    <div class="createdFirstname">${study.projectLeadName}</div>
                </td>
                <td>${study.researchSponsor}</td>
                <td>${study.status}</td>
                <td>
                    <!-- <span class="sprites_icon preview-g mr-lg"></span> -->
                    <span class="sprites_icon preview-g mr-lg viewStudyClass" isLive=""
                          studyId="${study.id}" permission="view" data-toggle="tooltip"
                          data-placement="top" title="View"></span>
                    <span class="${(not empty study.liveStudyId)?((study.flag)?'edit-inc-draft mr-md':'edit-inc mr-md'):'edit-inc-draft mr-md'}
                        addEditStudyClass 
                    <c:choose>
						<c:when test="${not study.viewPermission}">
								cursor-none
						</c:when>
						<c:when test="${not empty study.status && (study.status eq 'Deactivated')}">
							  cursor-none
						</c:when>
					</c:choose>" data-toggle="tooltip" data-placement="top"
                          title="${(not empty study.liveStudyId)?((study.flag)?'Draft Version':'Edit'):'Draft Version'}"
                          studyId="${study.id}"></span>
                    <c:if test="${not empty study.liveStudyId}">
                        <span class="eye-inc viewStudyClass " isLive="Yes"
                              studyId="${study.liveStudyId}" permission="view" data-toggle="tooltip"
                              data-placement="top" title="Last Published Version"></span>
                        <!-- Copy study functionality start -->
                        <%-- 					<c:if test="${fn:contains(sessionObject.userPermissions,'ROLE_CREATE_MANAGE_STUDIES')}"> --%>
                        <%-- 					<span class="sprites_icon copy copyStudyClass" customStudyId="${study.customStudyId}" data-toggle="tooltip" data-placement="top" title="Copy"></span> --%>
                        <%-- 					</c:if> --%>
                        <!-- Copy study functionality end-->
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<form:form action="/fdahpStudyDesigner/adminStudies/viewBasicInfo.do" id="addEditStudyForm"
           name="addEditStudyForm" method="post">
    <input type="hidden" id="studyId" name="studyId">
</form:form>

<div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog modal-sm flr_modal">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-body">
                <div id="timeOutMessage" class="text-right blue_text"><span class="timerPos"><img
                        src="../images/timer2.png"/></span>Your session expires in 15 minutes
                </div>
            </div>
        </div>
    </div>
</div>
<script>
  var idleTime = 0;
  $(document).ready(function () {
    $('.studyClass').addClass('active');
    $('[data-toggle="tooltip"]').tooltip();

    $('.addEditStudyClass').on('click', function () {
      var form = document.createElement('form');
      form.method = 'post';
      var input = document.createElement('input');
      input.type = 'hidden';
      input.name = 'studyId';
      input.value = $(this).attr('studyId');
      form.appendChild(input);

      input = document.createElement('input');
      input.type = 'hidden';
      input.name = '${_csrf.parameterName}';
      input.value = '${_csrf.token}';
      form.appendChild(input);

      form.action = '/fdahpStudyDesigner/adminStudies/viewStudyDetails.do';
      document.body.appendChild(form);
      form.submit();
    });

    $('.viewStudyClass').on('click', function () {
      var form = document.createElement('form');
      form.method = 'post';
      var input = document.createElement('input');
      input.type = 'hidden';
      input.name = 'studyId';
      input.value = $(this).attr('studyId');
      form.appendChild(input);

      var input1 = document.createElement('input');
      input1.type = 'hidden';
      input1.name = 'permission';
      input1.value = $(this).attr('permission');
      form.appendChild(input1);

      var input2 = document.createElement('input');
      input2.type = 'hidden';
      input2.name = 'isLive';
      input2.value = $(this).attr('isLive');
      form.appendChild(input2);

      input = document.createElement('input');
      input.type = 'hidden';
      input.name = '${_csrf.parameterName}';
      input.value = '${_csrf.token}';
      form.appendChild(input);

      form.action = '/fdahpStudyDesigner/adminStudies/viewStudyDetails.do';
      document.body.appendChild(form);
      form.submit();
    });

    $('#studies_list').DataTable({
      "paging": true,
      "abColumns": [
        {"bSortable": true},
        {"bSortable": true},
        {"bSortable": true},
        {"bSortable": true},
        {"bSortable": true},
        {"bSortable": false}
      ],
      "columnDefs": [{orderable: false, targets: [8]}],
      "order": [[0, "desc"]],
      "info": false,
      "lengthChange": false,
      "searching": false,
      "pageLength": 10
    });

    parentInterval();

    function parentInterval() {
      let timeOutInterval = setInterval(function () {
        idleTime += 1;
        console.log('Inside first interval, idleTime : ' + idleTime);
        if (idleTime > 3) { // 5 minutes
          clearInterval(timeOutInterval);
          keepAlive();
          timeOutFunction();
        }
      }, 226000);
    }

    $(this).mousemove(function (e) {
      idleTime = 0;
    });
    $(this).keypress(function (e) {
      idleTime = 0;
    });

    var timeOutInterval;

    function timeOutFunction() {
      console.log('Starting timeout function.');
      $('#myModal').modal('show');
      let i = 14;
      timeOutInterval = setInterval(function () {
        if (i === 0) {
          $('#timeOutMessage').html(
              '<span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in '
              + i + ' minutes');
          if ($('#myModal').hasClass('show')) {
            var a = document.createElement('a');
            a.href = "/fdahpStudyDesigner/sessionOut.do";
            document.body.appendChild(a).click();
          }
          clearInterval(timeOutInterval);
        } else {
          if (i === 1) {
            $('#timeOutMessage').html(
                '<span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in 1 minute');
          } else {
            $('#timeOutMessage').html(
                '<span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in '
                + i + ' minutes');
          }
          idleTime = 0;
          i -= 1;
        }
      }, 60000);
    }

    $(document).click(function (e) {
      if ($(e.target).closest('#myModal').length) {
        clearInterval(timeOutInterval);
        $('#timeOutMessage').html(
            '<span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in 15 minutes');
        parentInterval();
      }
    });
  });

  $('.copyStudyClass').on('click', function () {
    var form = document.createElement('form');
    form.method = 'post';
    var input = document.createElement('input');
    input.type = 'hidden';
    input.name = 'customStudyId';
    input.value = $(this).attr('customStudyId');
    form.appendChild(input);

    input = document.createElement('input');
    input.type = 'hidden';
    input.name = '${_csrf.parameterName}';
    input.value = '${_csrf.token}';
    form.appendChild(input);

    form.action = '/fdahpStudyDesigner/adminStudies/crateNewStudy.do';
    document.body.appendChild(form);
    form.submit();
  });

  //datatable icon toggle
  $(".table thead tr th").click(function () {
    $(this).children().removeAttr('class')
    $(this).siblings().children().removeAttr('class').addClass('sort');
    if ($(this).attr('class') == 'sorting_asc') {
      $(this).children().addClass('asc');
      //alert('asc');
    } else if ($(this).attr('class') == 'sorting_desc') {
      $(this).children().addClass('desc');
      //alert('desc');
    } else {
      $(this).children().addClass('sort');
    }
  });
</script>