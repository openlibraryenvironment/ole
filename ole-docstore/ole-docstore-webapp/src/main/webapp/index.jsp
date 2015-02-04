<%--
   - Copyright 2011 The Kuali Foundation.
   -
   - Licensed under the Educational Community License, Version 2.0 (the "License");
   - you may not use this file except in compliance with the License.
   - You may obtain a copy of the License at
   -
   - http://www.opensource.org/licenses/ecl2.php
   -
   - Unless required by applicable law or agreed to in writing, software
   - distributed under the License is distributed on an "AS IS" BASIS,
   - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   - See the License for the specific language governing permissions and
   - limitations under the License.
--%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>

<jsp:useBean id="repositoryBrowser" class="org.kuali.ole.RepositoryBrowser" scope="session"/>

<html>
<head>
    <script type="text/javascript" src="script/jquery-ui-1.8.16.custom/js/jquery-1.6.2.min.js"></script>
    <script type="text/javascript" src="script/jquery-ui-1.8.16.custom/js/jquery-ui-1.8.16.custom.min.js"></script>
    <script>

        function hideOnLoad() {
            $("#checkOutResult").hide();
            $("#checkInResult").hide();
            $("#ingestResult").hide();
            $("#bagItResult").hide();
            $("#dumpResult").hide();

        <%
     if( "services".equals(request.getParameter("tab"))) { %>
            $("#service").show();
            $("#refreshSummary").hide();
            $("#sections").hide();
        <%} else{ %>
            $("#service").hide();

        <%} %>

        }
        function hide() {
            $("#service").show();
            $("#refreshSummary").hide();
            $("#sections").hide();
        }
        function show() {
            $("#service").hide();
            $("#refreshSummary").show();
            $("#sections").show();
        }

        $(document).ready(function() {
            $("a#responseLink").click(function () {
                var href = $(this).attr("href");
                var arrHref = href.split("|");
                window.open(arrHref[0], "PopUp", arrHref[1])
                return false;
            });
            $("a#userGuideLink").click(function () {
                var href = $(this).attr("href");
                var arrHref = href.split("|");
                window.open(arrHref[0], "PopUp", arrHref[1])
                return false;
            });
        });


    </script>
    <link type="text/css" rel="Stylesheet"
          href="script/jquery-ui-1.8.16.custom/css/smoothness/jquery-ui-1.8.16.custom.css"/>
</head>
<%
    String pageTitle = "";
%>
<body topmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onload="hideOnLoad()">

<%@ include file="oleHeader.jsp" %>

<script>
    $(function() {
        $("#sections").tabs({
                                ajaxOptions: {
                                    error: function(xhr, status, index, anchor) {
                                        $(anchor.hash)
                                                .html("Couldn't load this tab. We'll try to fix this as soon as possible. "
                                                              + "If this wouldn't be a demo.");
                                    }
                                }
                            });
    });
</script>
<div id="busyDiv" align="left"
     style="height:99.5%;width:99%;position:absolute;z-index:1;opacity:0.5;background-color:lightgray;color:blue;">
    <b> Loading. Please wait...</b>
    <img src="images/busyImage.gif">
</div>
<div id="sections">
    <ul>
        <li><a href="#section-summary">Summary</a></li>
        <li><a href="#section-restService">Rest</a></li>
        <%--<li><a href="#section-IngestStringContent">Ingest String Content</a></li>--%>
       <%-- <li><a href="getUUIDInclude.jsp">List of Bibs</a></li>--%>
       <%-- <li><a href="#section-CheckIn">Check-In</a></li>
        <li><a href="#section-CheckOut">Check-out</a></li>
        <li><a href="delete.jsp"> Delete</a></li>
        <li><a href="#bagIt-Requests">BagIt Requests</a></li>--%>
    </ul>
    <div id="section-summary">
        <h2>Summary of Document Categories, Types and Formats
            <a id="userGuideLink"
               href=" https://wiki.kuali.org/display/OLE/User+Guide+-+OLE+Document+Store#UserGuide-OLEDocumentStore-2.1Summary|toolbar=0,menubar=0,width=1000,height=1000,scrollbars=1">
                <img align="right" src="images/user-guide.jpg" title="User Guide"/> </a>
        </h2>
        <br>
        <input type="button" name="refreshSummary" value="Refresh Summary" id="refreshSummary" onclick="loadSummary()">
        <br><br>

        <div id="summaryDiv">

        </div>
        <script>
            $(document).ready(function() {
                $("#busyDiv").hide();
            });
            function loadSummary() {
                $("#busyDiv").show();
                $.ajax({
                           url: "summary.jsp",
                           cache: false,
                           success: function(html) {
                               $("#summaryDiv").html(html);
                               $("#busyDiv").hide();
                           }
                       });

            }
        </script>
    </div>

    <div id="section-restService">
        <h2>BibIds</h2>
        <textarea rows="5" cols="80" id="bibIds" name="bibIds"  style="overflow: scroll;">
        </textarea>
        <h2>Result</h2>
        <textarea rows="15" cols="80" id="result" readonly="true" style="overflow: scroll;">
        </textarea>

        <h2>Time</h2>
        <textarea rows="2" cols="50" id="timeTaken" readonly="true" style="overflow: scroll;">
        </textarea>

        <input type="button" value="show bib tree" onclick="showBibTree()"/>
        <input type="button" value="show holdings tree" onclick="showInstanceTree()"/>
    </div>
   <%-- <div id="section-IngestStringContent">
        <a id="userGuideLink"
           href=" https://wiki.kuali.org/display/OLE/User+Guide+-+OLE+Document+Store#UserGuide-OLEDocumentStore-2.3Ingest|toolbar=0,menubar=0,width=1000,height=1000,scrollbars=1">
            <img align="right" src="images/user-guide.jpg" title="User Guide"/> </a>
        <table>
            <tr>
                <td>
                    <h3>Create Bib Tree(s) with REST API</h3>
                    <h2>POST /documents/bibs/tree HTTP/1.1 </h2>
                    <br>
                    <form name="addDocForm" method="POST">
                        <input type="hidden" name="docAction" value="ingestContent">
                         Request body:<br>
                        <textarea rows="15" cols="60" id="stringContent" name="stringContent"></textarea>
                        <br>
                        <br>
                        <input type="button" value="Submit" onclick="showIngest(stringContent)"/>
                        <input type="button" value="Clear" onclick='document.getElementById("stringContent").value=""' />
                    </form>
                </td>
                <td style="padding-left: 10;">
                    <h2>Sample Request Body</h2>
                    <br>
                    <input type="hidden" name="docAction" value="ingestContent">
                    <br>
                    <textarea rows="15" cols="80" id="sampleIngestRequestXML" readonly="true" style="overflow: scroll;">
                    </textarea>
                    <br>
                    <br>

                </td>
            </tr>
            <tr><td colspan="2" id="ingestResult">
                <h2>Response</h2>
                <textarea rows="25" cols="100" readonly="true" id="showIngestResult"></textarea>
                <br/>
                    <input type="button" value="Clear" onclick='document.getElementById("showIngestResult").value=""' />
                 </td>
            </tr>
        </table>
    </div>--%>

    <%--<div id="section-CheckIn">
        <a id="userGuideLink"
           href=" https://wiki.kuali.org/display/OLE/User+Guide+-+OLE+Document+Store#UserGuide-OLEDocumentStore-2.5Checkin|toolbar=0,menubar=0,width=1000,height=1000,scrollbars=1">
            <img align="right" src="images/user-guide.jpg" title="User Guide"/> </a>
        <table>
            <tr>
                <td>
                    <h2>Check-in a document</h2>
                    <br>
                    <br>

                    <form name="checkInForm" method="POST">
                        <input type="hidden" name="docAction" id="checkIndocAction" value="checkIn">
                        <!--
                        File UUID to be checked in:<input type="text" name="uuid" value="" size="50">
                        <br>
                        -->
                        Request xml with the document to be checked-in (updated):<br>
                        <textarea rows="15" cols="60" id="checkinStringContent" name="stringContent"></textarea>
                        <br>
                        <br>
                        <input type="button" value="Submit" onclick="showCheckIn(stringContent,docAction)">
                        <input type="button" value="Clear" onclick='document.getElementById("checkinStringContent").value = ""'/>
                    </form>
                </td>

                <td style="padding-left: 10;">
                    <h2> Sample data for Check-in </h2>
                    <br>
                    <input type="hidden" name="docAction" value="checkIn"> <br>
                    <textarea rows="15" cols="80" id="sampleCheckInRequestXML" readonly="true"
                              style="overflow: scroll;">
                    </textarea>
                    <br>
                    <br>
                </td>
            </tr>
            <tr><td colspan="2" id="checkInResult">
                <h2>Result</h2>
                <textarea rows="15" cols="60" readonly="true" id="showCheckInResult" name= "showCheckInResult"></textarea>
                <br/>
                <input type="button" value="Clear" onclick='document.getElementById("showCheckInResult").value = ""'/>
            </td></tr>


        </table>

    </div>
    <div id="section-CheckOut">
        <a id="userGuideLink"
           href=" https://wiki.kuali.org/display/OLE/User+Guide+-+OLE+Document+Store#UserGuide-OLEDocumentStore-2.6Checkout|toolbar=0,menubar=0,width=1000,height=1000,scrollbars=1">
            <img align="right" src="images/user-guide.jpg" title="User Guide"/> </a>
        <h2>Check-out a document</h2>
        <br>
        <br>
        DocType :  Bibliographic
        <br>
        <form name="checkOutForm" method="POST">
            <input type="hidden" name="docAction" value="checkOut">
            <input type="hidden" name="docCategory" value="work">

            <input type="hidden" name="docType" value="bibliographic">

            <input type="hidden" name="docFormat" value="marc">

            UUID of the document to be checked out:

            <input type="text" name="uuid" id="uuid" value="" size="50">
            <br>
            <br>
            <input type="button" value="Submit" onclick="showCheckOut(uuid,docAction,docCategory,docType,docFormat)">
            <br><br>
        </form>
        <div id="checkOutResult">
        <h2>Result</h2>
            <textarea rows="25" cols="80" id="showCheckOutResult" readonly="true"></textarea>
            <br/>
            <input type="button" value="Clear" onclick='document.getElementById("showCheckOutResult").value = ""' />
        </div>
    </div>--%>
 <%--
 <div id="section-DeleteUUID">
        <h2>Delete a document by UUID</h2>
        <br>
        <br>
        <form name="deleteUUID" method="POST" action="./document">
            <input type="hidden" name="docAction" value="deleteUUID">
            UUID of the document to be deleted:
            <br>
            <input type="text" name="uuid" value="" size="50">
            <br>
            <br>
            <input type="submit" value="Submit">
        </form>
    </div>
    --%>
   <%-- <div id="section-IngestFile">
        <!--
        <h2>Ingest a file (Upload)</h2>
        <form name="addDocForm" method="POST" action="./document" enctype="multipart/form-data" >
            <input type="hidden" name="docAction" value="ingestFile">
            Doc Category:
            <select name="docCategory">
                <option value="bibliographic">Bibliographic</option>
                <option value="authority">Authorities</option>
            </select>
            <br>
            Doc Type:
            <select name="docType">
                <option value="marc">MARC</option>
                <option value="dublin_core">DUBLIN</option>
                <option value="eac">EAC</option>
            </select>

            <br>
            Select the file containing the Document:<input type="file" name="xmlData" size="50">
            <br>
            <input type="submit" value="Submit" >
        </form>
        -->
    </div>
    <div id="section-IngestFile">
        <!--
        <h2>Link two documents</h2>
        <form name="linkForm" method="POST" action="./document" >
            <input type="hidden" name="docAction" value="link">
            File UUID:<input type="text" name="uuid1" value="" size="50"> <br>
            File UUID:<input type="text" name="uuid2" value="" size="50"> <br>
            <input type="submit" value="Submit" >
        </form>
        -->
    </div>--%>
	<%--<div id="bagIt-Requests">
		<form action="multiPartBagRequestClientServlet" method="post">
			<label>BagIt Requests Directory Path: </label> 
			<input type="text" name="requestFolderPath" value="/opt/docstore/bagIt"> <br><br><br> 
			<input type="submit" value="Execute Requests">
		</form>
	</div>--%>
   <%-- <div id="bagIt-Requests">
        <a id="userGuideLink"
           href="https://wiki.kuali.org/display/OLE/User+Guide+-+OLE+Document+Store#UserGuide-OLEDocumentStore-2.10IngestBinarydata(BagItRequests)|toolbar=0,menubar=0,width=1000,height=1000,scrollbars=1">
            <img align="right" src="images/user-guide.jpg" title="User Guide"/> </a>

        <form name="bagItForm" method="POST">
            BagIt Requests Directory Path:
            <input type="text" name="requestFolderPath" value="/opt/docstore/bagIt" size="60">
            <br>
            <br>
            <input type="button" value="Submit" onclick="showBagIt(requestFolderPath)">
            <br><br>
        </form>
        <div id="bagItResult">
            <h2>Result</h2>
            <textarea rows="25" cols="80" id="showBagItResult" readonly="true"></textarea>
            <br/>
            <input type="button" value="Clear" onclick='document.getElementById("showBagItResult").value=""'/>
        </div>
    </div>--%>

<%--    <div id="section-docstoreDump">
        <a id="userGuideLink"
           href="https://wiki.kuali.org/display/OLE/User+Guide+-+OLE+Document+Store#UserGuide-OLEDocumentStore-2.10IngestBinarydata(BagItRequests)|toolbar=0,menubar=0,width=1000,height=1000,scrollbars=1">
            <img align="right" src="images/user-guide.jpg" title="User Guide"/> </a>
        <table>
            <tr>
                <td>
                    <h2>Generate Docstore Dump</h2>
                    <br>
                    <br>

                    <form name="addDocForm" method="POST">
                        <input type="hidden" name="docAction" value="docstoreDBDump">
                        Export Profile xml : <br>
                        <textarea rows="15" cols="60" id="requestContent" name="requestContent"></textarea>
                        <br>
                        <br>
                        <input type="button" value="Submit" onclick="showDocstoreDump(requestContent,docAction)"/>
                        <input type="button" value="Clear" onclick='document.getElementById("requestContent").value=""' />
                    </form>
                </td>
                <td style="padding-left: 10;">
                    <h2>Sample data for Ingest</h2>
                    <br>
                    <input type="hidden" name="docAction" value="docstoreDBDump">
                    <br>
                    <textarea rows="15" cols="80" id="sampleExportProfileXML" readonly="true" style="overflow: scroll;">
                    </textarea>
                    <br>
                    <br>

                </td>
            </tr>
            <tr><td colspan="2" id="dumpResult">
                <h2>Result</h2>
                <textarea rows="25" cols="100" readonly="true" id="showDumpResult"></textarea>
                <br/>
                <input type="button" value="Clear" onclick='document.getElementById("showDumpResult").value=""' />
            </td>
            </tr>
        </table>
    </div>--%>
  </div>

<table border="1" cellspacing="1" cellpadding="1" width="100%" id="service">
    <tr>
        <th>Service Name</th>
        <th>Description</th>
        <th>URL</th>
        <th>Method</th>
        <th>Parameters</th>
        <th>Response Code</th>
        <th>Response</th>


    </tr>
    <tr>
        <td>Ingest string content</td>
        <td>Ingest and index the given documents along with any linking information</td>
        <td>http://localhost:8080/oledocstore/document</td>
        <td>Post</td>
        <td>docAction=ingestContent<br>stringContent=[<a
                href="./xml/ingestRequestDocument.xml|toolbar=0,menubar=0,width=500,height=500,scrollbars=1"
                id="responseLink">request</a>]
        </td>
        <td>200 OK</td>
        <td>[<a href="./xml/ingestResponseDocument.xml|toolbar=0,menubar=0,width=500,height=500,scrollbars=1"
                id="responseLink">response</a>]
        </td>
    </tr>
    <tr>
        <td>Check-in</td>
        <td>Check-in the given document</td>
        <td>http://localhost:8080/oledocstore/document</td>
        <td>Post</td>
        <td>docAction=checkIn<br>stringContent=[<a
                href="./xml/checkInRequest.xml|toolbar=0,menubar=0,width=500,height=500,scrollbars=1" id="responseLink">request]
        </td>
        <td>200 OK</td>
        <td>[<a href="./xml/checkInResponse.xml|toolbar=0,menubar=0,width=500,height=500,scrollbars=1" id="responseLink">response</a>]
        </td>
    </tr>
    <tr>
        <td>Check-out</td>
        <td>Check-out a document using UUID</td>
        <td>http://localhost:8080/oledocstore/document</td>
        <td>Post</td>
        <td>docAction=checkOut<br>uuid=[uuid]</td>
        <td>200 OK</td>
        <td>[<a href="./xml/checkOutResponse.xml|toolbar=0,menubar=0,width=500,height=500,scrollbars=1"
                id="responseLink">response</a>]
        </td>
    </tr>
    <tr>
        <td>Batch delete with links</td>
        <td>Delete the documents with given uuids along with the linked documents</td>
        <td>http://localhost:8080/oledocstore/document</td>
        <td>Post</td>
        <td>docAction=batchDeleteWithLinks<br>category=[category]<br>type=[type]<br>format=[format]<br>UUIDs=[UUID],[UUID]<br>stringContent=[<a
                href="./xml/batchDeleteRequest.xml|toolbar=0,menubar=0,width=500,height=500,scrollbars=1"
                id="responseLink">request</a>]
        </td>
        <td>200 OK</td>
        <td>[<a href="./xml/batchDeleteResponse.xml|toolbar=0,menubar=0,width=500,height=500,scrollbars=1"
                id="responseLink">response</a>]
        </td>
    </tr>
</table>

<script>
    $.ajaxSetup({async:false});
    $.post("sampleCheckInRequest.xml", { doccategoryID: 'all' }, function(data) {
        $("#sampleCheckInRequestXML").html(data.toString());
    }, 'html');

    $.ajaxSetup({async:false});
    $.post("sampleExportProfile.xml", { doccategoryID: 'all' }, function(data) {
        $("#sampleExportProfileXML").html(data.toString());
    }, 'html');

    $.ajaxSetup({async:false});
    $.post("BibTrees1.xml", { doccategoryID: 'all' }, function(data) {
        $("#sampleIngestRequestXML").html(data.toString());
    }, 'html');
/*
    function showCheckOut(uuid,docAction){
        document.getElementById("showCheckOutResult").value = "";
        $.ajaxSetup({async:false});
        $.post("./document", {uuid:uuid.value,docAction:docAction.value},
                function(data) {
                    $("#checkOutResult").show();
                    document.getElementById("showCheckOutResult").value = data.toString();
                }, 'html'
        );
    }
*/
    function showCheckOut(uuid,docAction,docCategory,docType,docFormat){
        document.getElementById("showCheckOutResult").value = "";
        $.ajaxSetup({async:false});
        $.post("./document", {uuid:uuid.value,docAction:docAction.value,docCategory:docCategory.value,docType:docType.value,docFormat:docFormat.value },
                function(data) {
                    $("#checkOutResult").show();
                    document.getElementById("showCheckOutResult").value = data.toString();
                }, 'html'
        );
    }
    function showCheckIn(stringContent,docAction){
        document.getElementById("showCheckInResult").value = "";
        $.ajaxSetup({async:false});
        $.post("./document", {stringContent:stringContent.value,docAction:docAction.value},
                function(data) {
                    $("#checkInResult").show();
                    document.getElementById("showCheckInResult").value = data.toString();
                }, 'html'
        );
    }
    function showIngest(stringContent){
        document.getElementById("showIngestResult").value = "";
        $.ajaxSetup({async:false});
        $.post("./documentrest/bibs/trees", {stringContent:stringContent.value},
                function(data) {
                    $("#ingestResult").show();
                    document.getElementById("showIngestResult").value = data.toString();
                }, 'html'
        );
    }
    function showBibTree(){
        var date = new Date();
        var startTime = date.getTime();
        var bibIds = document.getElementById("bibIds").value;
        var bibIdss = bibIds.split(",");
        var result = "";
        for(var i=0 ; i < bibIdss.length ; i++ ) {
            $.ajaxSetup({async:false});
            $.get("./documentrest/bibs/doc/tree?"+ "bibId=" + bibIdss[i] ,
                    function(data) {
                        result = result + data.toString();
                    }, 'html'
            );
        }
        document.getElementById("result").value = result;
        var endDate = new Date();
        var endTime = endDate.getTime();
        var totalTime = endTime - startTime;
        document.getElementById("timeTaken").value = totalTime;

    }

    function showInstanceTree() {
        var date = new Date();
        var startTime = date.getTime();
        var bibIds = document.getElementById("bibIds").value;
        var result = "";
            $.ajaxSetup({async:false});
            window.open("http://192.168.40.18:9090/olefs/document?docAction=instanceDetails&format=xml&bibIds=" + bibIds);
//            $.post("http://192.168.40.18:9090/olefs/document?docAction=instanceDetails&format=xml&bibIds=" + bibIds ,
//                    function(data) {
//                        alert("sss")
//                        result = result + data.toString();
//                    }, 'html'
//            );
        document.getElementById("result").value = result;
        var endDate = new Date();
        var endTime = endDate.getTime();
        var totalTime = endTime - startTime;
        document.getElementById("timeTaken").value = totalTime;

    }

    function showBagIt(requestFolderPath){
        document.getElementById("showBagItResult").value = "";
            $.ajaxSetup({async:false});
            $.post("./multiPartBagRequestClientServlet", {requestFolderPath:requestFolderPath.value},
                    function(data) {
                        $("#bagItResult").show();
                        document.getElementById("showBagItResult").value = data.toString();
                    }, 'html'
            );
        }

    function showDocstoreDump(requestContent,docAction){
        document.getElementById("showDumpResult").value = "";
        $.ajaxSetup({async:false});
        $.post("./document", {requestContent:requestContent.value,docAction:docAction.value},
                function(data) {
                    $("#dumpResult").show();
                    document.getElementById("showDumpResult").value = data.toString();
                }, 'html'
        );
    }


</script>

<% String tabField = request.getParameter("tab");
    if (tabField != null) {%>
<input type="hidden" id="services" value="<%=tabField%>"/>
<% }%>

</div> <!-- close <div id="iframe_portlet_container_div"> from oleHeader.jsp -->
<%@ include file="oleFooter.jsp" %>
</body>
</html>

