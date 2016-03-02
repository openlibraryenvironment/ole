<%@ page import="javax.swing.text.Document" %>
<%@ page import="org.kuali.ole.docstore.process.ProcessParameters" %>
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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="keywords" content="jquery,ui,easy,easyui,web">
<meta name="description" content="easyui help you build your web page easily!">
<link rel="stylesheet" type="text/css" href="script/jquery-easyui-1.3/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="script/jquery-easyui-1.3/themes/icon.css">
<script type="text/javascript" src="script/jquery-easyui-1.3/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="script/jquery-easyui-1.3/jquery.easyui.min.js"></script>
<%--  <script type="text/javascript" src="script/jquery-ui-1.8.16.custom/js/jquery-1.6.2.min.js"></script>--%>
<script type="text/javascript" src="script/jquery-ui-1.8.16.custom/js/jquery-ui-1.8.16.custom.min.js"></script>
<link type="text/css" rel="Stylesheet"
      href="script/jquery-ui-1.8.16.custom/css/smoothness/jquery-ui-1.8.16.custom.css"/>
<%
    String pageTitle = "";
    String docstoreUploadDir = ProcessParameters.getBulkUploadDir();
%>



<%@ include file="oleHeader.jsp" %>

<script>
    function showReindexResult(action) {
        document.getElementById("reindexResult").value = "";
        $.post("./rebuildIndex",
                {action:"start", batchSize:$("#batchSize").val(), startIndex:$("#startIndex").val(), endIndex:$("#endIndex").val(),updateDate:$("#updateDate").val()+" "+$("#time").val()},
                function (data) {
                    document.getElementById("reindexResult").value = data.toString();
                }, 'html');
    }
    function showReindexStatus(action) {

        document.getElementById("reindexResult").value = "";
        $.ajaxSetup({async:false});
        $.post("./rebuildIndex",
                {action:"status"},
                function (data) {
                    document.getElementById("reindexResult").value = data.toString();
                }, 'html');
    }

    function showStorageStatus(action) {

        document.getElementById("storeResult").value = "";
        $.ajaxSetup({async:false});
        $.post("./rebuildIndex",
                {action:"bibStatus"},
                function (data) {
                    document.getElementById("storeResult").value = data.toString();
                }, 'html');
    }

    function showStoreBibInfoResult() {
        document.getElementById("storeResult").value = "";
        $.post("./rebuildIndex",
                {action:"store", batchSize:$("#batchSize").val()},
                function (data) {
                    document.getElementById("storeResult").value = data.toString();
                }, 'html');
    }

    function generateShelfkeys() {
        document.getElementById("storeResult").value = "";
        $.post("./rebuildIndex",
                {action:"shelfKey"},
                function (data) {
                    document.getElementById("storeResult").value = data.toString();
                }, 'html');
    }

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

    $(function () {
        $('#updateDate').datepicker({ dateFormat: 'yy-mm-dd' });
        $('#updateDate').datepicker();
    });
</script>


<div id="sections">
    <ul>
        <li><a href="#section-reindex">Reindex</a></li>
        <li><a href="#section-bibInfo">Initialize Bib Info</a></li>
        <li><a href="#section-callNumberMigration">Generate Shelving Keys</a></li>

    </ul>


    <div id="section-reindex">


        <div >
            <form name="reindexFromFile" method="POST"  action="rebuildIndex?action=fromFile" enctype="multipart/form-data">
                <table>
                    <tr>
                        <td><h3>Rebuild Solr Indexes</h3></td>
                    </tr>
                    <tr>
                        <td>Bib IDs from File :</td>
                        <td><input type="file" name="file"/></td>
                        <td><input type="submit" value="upload and reindex" /></td>
                    </tr>
                </table>

            </form>
        </div>
        <form name="rebuildIndex" method="POST">
            <a id="userGuideLink"
               href=" https://wiki.kuali.org/display/OLE/User+Guide+-+OLE+Document+Store#UserGuide-OLEDocumentStore-2.9Rebuildindexes%28Adminfunction%29|toolbar=0,menubar=0,width=1000,height=1000,scrollbars=1">
                <img align="right" src="images/user-guide.jpg" title="User Guide"/> </a>
            <table>
                <tr>
                    <td>
                        Index all, bibs, holdings and items
                    </td>
                    <td>
                    </td>
                </tr>
                <tr>
                    <td>
                        Batch Size :
                    </td>
                    <td>
                        <input type="text" name="batchSize" value="5000" id="batchSize"
                               style="height: 25px; width: 200px; font-style: oblique; font-weight: bold;"/>
                    </td>
                    <td>
                        Start BibId :
                    </td>
                    <td>
                        <input type="text" name="startIndex" value="0" id="startIndex"
                               style="height: 25px; width: 200px; font-style: oblique; font-weight: bold;"/>
                    </td>
                    <td>
                        End BibId :
                    </td>
                    <td>
                        <input type="text" name="endIndex" value="0" id="endIndex"
                               style="height: 25px; width: 200px; font-style: oblique; font-weight: bold;"/>
                    </td>
                </tr>
                <tr>
                    <td>Date:</td>
                    <td><input type="text" name="updateDate" id="updateDate" /></td>
                    <td>Time</td>
                    <td><input type="text" name="time" id="time" placeholder="HH:MM:SS"/></td>
                </tr>
                <tr>
                    <td width="125"><input type="button" name="start" value="Start" id="start"
                                           onclick="showReindexResult(start);"
                                           style="height: 25px; width: 200px; font-style: oblique; font-weight: bold;"/>
                    </td>
                    <td width="125"><input type="button" name="showStatus" value="Show Status" id="showStatus"
                                           onclick="showReindexStatus(showStatus);"
                                           style="height: 25px; width: 200px; font-style: oblique; font-weight: bold;"/>
                    </td>
                </tr>
            </table>
            <table>
                <tr>
                    <td>
                        <textarea id="reindexResult" rows="20" cols="130" readonly="true"> <%=request.getAttribute("reindexResult")%></textarea>
                    </td>
                </tr>
                <tr>
                    <td>
                        <input id="clearResult" type="button" value="Clear"
                               onclick='document.getElementById("reindexResult").value=""'/>
                    </td>
                </tr>
            </table>

            <br><br>

            <div id="reindexTableDisplay">
                <table id="reindexTable"></table>
            </div>
            <br><br>
        </form>

    </div>


    <div id="section-bibInfo">
        <form name="rebuildIndex" method="POST">
            <table width="850">
                <tr>
                    <td><h3>Initialize Bib Info</h3></td>
                </tr>
                <tr>
                    <td>
                        Batch Size :
                    </td>
                    <td>
                        <input type="text" name="batchSize" value="5000" id="batchSize"
                               style="height: 25px; width: 200px; font-style: oblique; font-weight: bold;"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <input id="storeBibInfo" type="button" value="Initialize Bib Info"
                               onclick='showStoreBibInfoResult();'/>
                    </td>
                    <td width="125"><input type="button" name="showStatus" value="Show Status" id="showStatus1"
                                           onclick="showStorageStatus(showStatus1);"
                                           style="height: 25px; width: 200px; font-style: oblique; font-weight: bold;"/>
                    </td>

                </tr>
            </table>
            <table>
                <tr>
                    <td>
                        <textarea id="storeResult" rows="20" cols="130" readonly="true"></textarea>
                    </td>
                </tr>
                <tr>
                    <td>
                        <input id="clearResult1" type="button" value="Clear"
                               onclick='document.getElementById("storeResult").value=""'/>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div id="section-callNumberMigration">
        <form name="callNumberMigration" method="POST">
            <table width="850">
                <tr>
                    <td><h3>Shelving key Generation</h3></td>
                </tr>

                <tr>
                    <td>
                        <input id="shelvingkeyGeneration" type="button" value="Generate Shelving key"
                               onclick='generateShelfkeys();'/>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>