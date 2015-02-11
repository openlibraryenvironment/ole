<%@ page import="java.util.Date" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="keywords" content="jquery,ui,easy,easyui,web">
<meta name="description" content="easyui help you build your web page easily!">

<%--<link rel="stylesheet" type="text/css" href="css/easyui.css">
<link rel="stylesheet" type="text/css" href="css/icon.css">
<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.8.16.custom.min.js"></script>
<link type="text/css" rel="Stylesheet"
      href="css/jquery-ui-1.8.16.custom.css"/>--%>
<script type="text/javascript" src="plugins/jquery/jquery-1.8.3.js"></script>


<style type="text/css">
    .divSection {
        width: 600px;
        margin: 10px auto;
        padding: 10px;
        border: 7px solid #72B372;
        border-radius: 10px;
        font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
        color: #444;
        background-color: #F0F0F0;
        box-shadow: 0 0 20px 0 #000000;
    }
    .divSection h3 {
        margin: 0 15px 20px;
        border-bottom: 2px solid #72B372;
        padding: 5px 10px 5px 0;
        font-size: 1.1em;
    }

</style>

<%--header start--%>
<link href="./css/olePortal.css" rel="stylesheet" type="text/css"/>

<script language="javascript">
    if (top.location != self.location) {
        top.location = self.location;
    }

</script>
<script>

    function startOLESocketServer(action) {
        /*$("#portNo").attr("disabled", true);
        $("#serverUrl").attr("disabled", true);*/
        $("#start").attr("disabled", true);
        document.getElementById("socketServerStatus").value = "";
        $.post("./oleSip2",
                {action:"start"},
                function (data) {
                    document.getElementById("socketServerStatus").value = data.toString();
                    if(data != 'Socket server started Successfully' &&
                            data != 'Already request is send by someone else to start server. Please wait for few seconds and refresh the page' &&
                            data != 'Socket server started Successfully'){
                        /*$("#portNo").attr("disabled", false);
                        $("#serverUrl").attr("disabled", false);*/
                        $("#start").attr("disabled", false);
                    }
                }, 'html');
    }
    function stopOLESocketServer(action) {

        document.getElementById("socketServerStatus").value = "";
        $.ajaxSetup({async:false});
        $.post("./oleSip2",
                {action:"stop"},
                function (data) {
                    document.getElementById("socketServerStatus").value = data.toString();
                    if(data == 'Server Status: Socket Server Stopped'){
                        /*$("#portNo").attr("disabled", false);
                        $("#serverUrl").attr("disabled", false);*/
                        $("#start").attr("disabled", false);
                    }
                }, 'html');
    }

    function showOLESocketServerStatus(action) {

        document.getElementById("socketServerStatus").value = "";
        $.ajaxSetup({async:false});
        $.post("./oleSip2",
                {action:"status"},
                function (data) {
                    document.getElementById("socketServerStatus").value = data.toString();
                    if(data.toString() != 'Server Status: Socket Server is Running'){
                        /*$("#portNo").attr("disabled", false);
                        $("#serverUrl").attr("disabled", false);*/
                        $("#start").attr("disabled", false);
                    }
                }, 'html');
    }


</script>

<script>
    $(document).ready(function() {
        $.get("./oleSip2",
                {action:"test"},
                function (data) {
                    document.getElementById("socketServerStatus").value = data.toString();
                    if(data == 'Server Status: Socket Server is Running'){
                       /* $("#portNo").attr("disabled", true);
                        $("#serverUrl").attr("disabled", true);*/
                        $("#start").attr("disabled", true);
                    }else if(data == 'Already request is send by someone else to start server. Please wait for few seconds and refresh the page'){
                        /* $("#portNo").attr("disabled", true);
                         $("#serverUrl").attr("disabled", true);*/
                        $("#start").attr("disabled", true);
                    }
                }, 'html');
    });
</script>

<div id="header" title="Kuali Open Library Environment">
    <h1 class="kfs"></h1>
</div>

<div id="build"><%=new Date()%>
</div>
<div id="tabs" class="tabposition">
    <ul>
        <li class="red"><a class="red" href="." title="Main Menu" onclick="show()">OLE Socket Server</a></li>


    </ul>
</div>

<div class="header2">
    <div class="header2-left-focus">
        <div class="breadcrumb-focus">

        </div>
    </div>
</div>
<div id="iframe_portlet_container_div">
    <br/>


    <%-- header end--%>

    <div id="sections">

        <div id="section-reindex">
            <form name="oleSip2" method="POST">
                <div class="divSection">
                    <center><h3>OLE Socket Server page for SIP2</h3></center>
                    <table width="650">
                        <%--<tr>
                            <td>
                                Port No :
                            </td>
                            <td>
                                <input type="text" name="portNo" id="portNo"
                                       style="height: 25px; width: 200px; font-style: oblique; font-weight: bold;"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                Application Url :
                            </td>
                            <td>
                                <input type="text" name="serverUrl" id="serverUrl"
                                       style="height: 25px; width: 200px; font-style: oblique; font-weight: bold;"/>
                            </td>
                        </tr>--%>
                        <tr>
                            <td><input type="button" name="start" value="Start" id="start"
                                                   onclick="startOLESocketServer(start);"
                                                   style="height: 25px; width: 100px; font-style: oblique; font-weight: bold;"/>
                            </td><td><input type="button" name="stop" value="Stop" id="stop"
                                                        onclick="stopOLESocketServer(stop);"
                                                        style="height: 25px; width: 100px; font-style: oblique; font-weight: bold;"/>
                        </td>
                            <td><input type="button" name="showStatus" value="Show Status" id="showStatus"
                                                   onclick="showOLESocketServerStatus(showStatus);"
                                                   style="height: 25px; width: 100px; font-style: oblique; font-weight: bold;"/>
                            </td>
                        </tr>
                    </table>
                    <table>
                        <tr>
                            <td>
                                <textarea id="socketServerStatus" rows="10" cols="75" readonly="true"></textarea>
                            </td>
                        </tr>
                        <tr>

                            <td>
                                <center><input id="clearResult" type="button" value="Clear"
                                       onclick='document.getElementById("socketServerStatus").value=""'/></center>
                            </td>
                        </tr>
                    </table>

                    <br><br>

                    <div id="reindexTableDisplay">
                        <table id="reindexTable"></table>
                    </div>
                    <br><br>
                </div>
            </form>

        </div>
    </div>
    <br/><br/><br/><br/>
</div>
