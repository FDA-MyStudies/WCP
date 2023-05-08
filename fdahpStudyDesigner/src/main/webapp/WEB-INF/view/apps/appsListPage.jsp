<%--
  Created by IntelliJ IDEA.
  User: anoop
  Date: 17-06-2022
  Time: 23:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<head>
    <meta charset="UTF-8">
    <style nonce="${nonce}">
      body {
        height: 100vh !important;
      }
    </style>
</head>
<style nonce="${nonce}">
  #myModal .modal-dialog, #learnMyModal .modal-dialog .flr_modal {
    position: relative !important;
    right: 5px !important;
    margin-top: 6% !important;
  }
</style>
<div
        class="col-xs-12 col-sm-12 col-md-12 col-lg-12 grayeef2f5-bg p-none">
    <div>
        <!-- widgets section-->
        <div class="col-sm-12 col-md-12 col-lg-12 p-none mb-md">
            <div class="black-lg-f mt-none">Manage Force Update
                <span>
                    <span class="filled-tooltip"
                          data-toggle="tooltip"
                          data-placement="top"
                          title="Updates the participant mobile app from their current app version to the latest available android and iOS app version.">
                    </span>
                </span>
            </div>
        </div>
    </div>
    <div class="clearfix"></div>
</div>
<div class="col-xs-12 col-sm-12 col-md-12 col-md-12 p-none">
    <div class="white-bg">
        <div>
            <table id="app_list" role="grid" class="table wid100 tbl_rightalign tbl">
                <thead>
                <tr>
                    <th>App Id</th>
                    <th>App Name</th>
                    <th>Android Version</th>
                    <th>Update Type</th>
                    <th>iOS Version</th>
                    <th>Update Type</th>
                    <th>Action</th>

                </tr>
                </thead>
                <tbody>
                <c:forEach items="${appList}" var="app" varStatus="loop">
                    <tr>
                        <td>${app.appId}</td>
                        <td>${app.appName}</td>
                        <td>${app.androidVersion}</td>
                        <td>
                            <select id="androidUpdateType_${loop.index}"
                                    class="selectpicker updateType" name="androidForceUpgrade">
                                <c:forEach items="${updateTypes}" var="updateType">
                                    <option value="${updateType.key}" ${app.androidForceUpgrade eq updateType.value ? 'selected' : ''}>${updateType.key}</option>
                                </c:forEach>
                            </select>
                        </td>
                        <td>${app.iosVersion}</td>
                        <td>

                            <select id="iosUpdateType_${loop.index}" class="selectpicker updateType"
                                    name="iosForceUpgrade">
                                <c:forEach items="${updateTypes}" var="updateType">
                                    <option value="${updateType.key}" ${app.iosForceUpgrade eq updateType.value ? 'selected' : ''}>${updateType.key}</option>
                                </c:forEach>
                            </select>
                        </td>
                        <td>
                            <button class="btn btn-primary blue-btn update" id='${loop.index}'>
                                Update
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

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


<script type="text/javascript" nonce="${nonce}">
  var idleTime = 0;
  $(document).ready(function () {
    $('#rowId').parent().removeClass('#white-bg');

    $('.appClass').addClass('active');

    $('[data-toggle="tooltip"]').tooltip();

    $('.addOrEditUser').on('click', function () {
      $('#userId').val($(this).attr('userId'));
      $('#checkRefreshFlag').val('Y');
      $('#addOrEditUserForm').submit();
    });

    $('.viewUser').on('click', function () {
      $('#usrId').val($(this).attr('userId'));
      $('#checkViewRefreshFlag').val('Y');
      $('#viewUserForm').submit();
    });

    //app_list page Datatable
    table = $('#app_list').DataTable({
      "paging": true,
      "bAutoWidth": false,
      "aoColumns": [
        {sWidth: '15%'},
        {sWidth: '25%'},
        {sWidth: '12%'},
        {sWidth: '12%'},
        {sWidth: '12%'},
        {sWidth: '12%'},
        {sWidth: '12%'}
      ],
      "columnDefs": [{width: 50, orderable: false}],
      "order": [[0, "desc"]],
      "info": false,
      "lengthChange": false,
      "searching": false,
      "pageLength": 10
    });

    $('.c__search').on('keyup', function () {
      table.search($(this).val().replace(/(["])/g, "\ $1")).draw();
    });

    $('#filterRole').on('change', function () {
      var selected = $(this).find("option:selected").val();
      table.column(2).search(selected).draw();
    });

    $('.update').on('click', function () {
      $('#alertMsg').hide();
      let id = $(this).attr('id');
      let rowData = $('#app_list').DataTable().row(id).data();
      forceUpgradeApp(rowData, id);
    });

    parentInterval();

    function parentInterval() {
      let autoSaveInterval = setInterval(function () {
        idleTime += 1;
        if (idleTime > 3) { // 5 minutes
          clearInterval(autoSaveInterval);
          // keepAlive();
          timeOutFunction();
        }
      }, 225000);
    }

    $(this).mousemove(function (e) {
      idleTime = 0;
    });
    $(this).keypress(function (e) {
      idleTime = 0;
    });

    var timeOutInterval;

    function timeOutFunction() {
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

  function forceUpgradeApp(rowData, index) {
    console.log(rowData);
    console.log(rowData.length);
    console.log(index);
    if (rowData.length !== 7) {
      showErrMsg("Columns Mismatch");
      return false;
    }

    let androidUpdateType = $('#androidUpdateType_' + index).val();
    let iosUpdateType = $('#iosUpdateType_' + index).val();

    if (androidUpdateType === '' && iosUpdateType === '') {
      showErrMsg("Please select Update Type.");
      return false;
    }

    if (androidUpdateType !== '' && rowData[2] === '') {
      showErrMsg("Cannot update as application does not exist for android.");
      return false;
    }

    if (iosUpdateType !== '' && rowData[4] === '') {
      showErrMsg("Cannot update as application does not exist for iOS.");
      return false;
    }

    $.ajax({
      url: '/fdahpStudyDesigner/adminApps/forceUpgradeApp.do',
      type: "POST",
      datatype: "json",
      data: {
        appId: rowData[0],
        androidUpdateType: androidUpdateType,
        iosUpdateType: iosUpdateType,
        "${_csrf.parameterName}": "${_csrf.token}",
      },
      success: function test(data) {
        if (data.message === 'SUCCESS') {
          showSucMsg("App details updated successfully.");
        } else {
          showErrMsg("Error while updating app details.");
        }
      }
    });
  }
</script>
