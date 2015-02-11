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

<%--
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
    $(document).ready(function () {
        $("#reindexResult").hide();
        $("#bulkIngestResult").hide();
        $("#clearResult").hide();
        $("#clear").hide();
        $("a#userGuideLink").click(function () {
            var href = $(this).attr("href");
            var arrHref = href.split("|");
            window.open(arrHref[0], "PopUp", arrHref[1])
            return false;
        });
		showProcessParams();

    });

    function onChangeCategory() {
        var doccategoryId = $("#documentCategory").val();
        populateReIndexDocTypeDropdown($("#documentType"), configInfoXml, doccategoryId);
    }

    function onChangeBulkIngestCategory() {
        var doccategoryId = $("#bulkIngestDocCategory").val();
        populateBulkIngestDocTypeDropdown($("#bulkIngestDocType"), configInfoXml, doccategoryId);
    }

    function onChangeDocType() {
        var doccategoryId = $("#documentCategory").val();
        var doctypeId = $("#documentType").val();
        populateFormatDropdown($("#documentFormat"), configInfoXml, doccategoryId, doctypeId);
    }

    function onChangeBulkIngestDocType() {
        var doccategoryId = $("#bulkIngestDocCategory").val();
        var doctypeId = $("#bulkIngestDocType").val();
        populateBulkIngestFormatDropdown($("#bulkIngestDocFormat"), configInfoXml, doccategoryId, doctypeId);
    }

    function populateDocTypeDropdown(select, data, doccategoryId) {
        select.html('');
        var nodePath = JQUERY4U.UTIL.formatVarString("documentCategory[id='{1}'] documentType", doccategoryId);
        $(data).find(nodePath).each(function () {
            select.append($('<option></option>').val($(this).attr('id')).html($(this).attr('name')));
        });
        $("#documentType").change(function () {
            var doctypeId = $(this).val();
            populateFormatDropdown($("#documentFormat"), configInfoXml, doccategoryId, doctypeId);
        });
        onChangeDocType();
    }

    function populateReIndexDocTypeDropdown(select, data, doccategoryId) {
        select.html('');
        var nodePath = JQUERY4U.UTIL.formatVarString("documentCategory[id='{1}'] documentType", doccategoryId);
        $(data).find(nodePath).each(function () {
            if($(this).attr('id') != "holdings" && $(this).attr('id') != "item" && $(this).attr('id') != "license" && $(this).attr('id') != "eholdings")
                select.append($('<option></option>').val($(this).attr('id')).html($(this).attr('name')));
        });
        $("#documentType").change(function () {
            var doctypeId = $(this).val();
            populateFormatDropdown($("#documentFormat"), configInfoXml, doccategoryId, doctypeId);
        });
        onChangeDocType();
    }


    function populateBulkIngestDocTypeDropdown(select, data, doccategoryId) {
        select.html('');
        var nodePath = JQUERY4U.UTIL.formatVarString("documentCategory[id='{1}'] documentType", doccategoryId);
        $(data).find(nodePath).each(function () {
        	if($(this).attr('id') != "holdings" && $(this).attr('id') != "item" && $(this).attr('id') != "license" && $(this).attr('id') != "instance")
            	select.append($('<option></option>').val($(this).attr('id')).html($(this).attr('name')));
        });
        $("#bulkIngestDocType").change(function () {
            var doctypeId = $(this).val();
            populateBulkIngestFormatDropdown($("#bulkIngestDocFormat"), configInfoXml, doccategoryId, doctypeId);
        });
        onChangeBulkIngestDocType();
    }

    function populateFormatDropdown(select, data, doccategoryId, doctypeId) {
        select.html('');
        var nodePath = JQUERY4U.UTIL.formatVarString("documentCategory[id='{1}'] documentType[id='{2}'] documentFormat",
                                                     doccategoryId, doctypeId);
        $(data).find(nodePath).each(function () {
            	select.append($('<option></option>').val($(this).attr('id')).html($(this).attr('name')));

        });
    }

    function populateBulkIngestFormatDropdown(select, data, doccategoryId, doctypeId) {
        select.html('');
        var nodePath = JQUERY4U.UTIL.formatVarString("documentCategory[id='{1}'] documentType[id='{2}'] documentFormat",
                                                     doccategoryId, doctypeId);
        $(data).find(nodePath).each(function () {
        	if($(this).attr('id') != "all")
            	select.append($('<option></option>').val($(this).attr('id')).html($(this).attr('name')));

        });
    }

    var JQUERY4U = {};
    JQUERY4U.UTIL = {
        formatVarString:function () {
            var args = [].slice.call(arguments);
            if (this.toString() != '[object Object]') {
                args.unshift(this.toString());
            }
            var pattern = new RegExp('{([1-' + args.length + '])}', 'g');
            return String(args[0]).replace(pattern, function (match, index) {
                return args[index];
            });
        }
    };

    function showProcessParams(){
    	$("#bulkIngestResult").hide();
        $("#clear").hide();
    	if( "DocStore Request" == document.getElementById("bulkIngestDataFormat").value){
          $("#bulkIngestTableDisplay").show();
          $("#bulkIngestStatistics").show();
          $("#bulkIngestStop").show();
          $("#bulkIngestClear").show();
	      $("#bulkIngestDocCategory").hide();
	      $("#bulkIngestDocCategoryLabel").hide();
	      $("#bulkIngestDocType").hide();
	      $("#bulkIngestDocTypeLabel").hide();
	      $("#bulkIngestDocFormat").hide();
	      $("#bulkIngestDocFormatLabel").hide();
	      $("#bulkIngestFolder").hide();
	      $("#bulkIngestFolderLabel").hide();
          $("#bulkIngestFolderLabel1").show();
          $("#bulkIngestFolder1").show();
          if ($("#bulkIngestFolder1").val() == null || $("#bulkIngestFolder1").val() == "")
                $("#bulkIngestFolder1").val("<%= docstoreUploadDir %>");

		}else{
          $("#bulkIngestTableDisplay").hide();
          $("#bulkIngestStatistics").hide();
          $("#bulkIngestStop").hide();
          $("#bulkIngestClear").hide();
	      $("#bulkIngestDocCategory").show();
	      $("#bulkIngestDocCategoryLabel").show();
	      $("#bulkIngestDocType").show();
	      $("#bulkIngestDocTypeLabel").show();
	      $("#bulkIngestDocFormat").show();
	      $("#bulkIngestDocFormatLabel").show();
	      $("#bulkIngestFolder").show();
	      $("#bulkIngestFolderLabel").show();
	      if($("#bulkIngestFolder").val()==null || $("#bulkIngestFolder").val()=="")
              $("#bulkIngestFolder").val("<%= docstoreUploadDir %>" + "/" + $("#bulkIngestDocCategory").val() + "/"
                                                 + $("#bulkIngestDocType").val() + "/" + $("#bulkIngestDocFormat").val()
                                                 + "/");
          $("#bulkIngestFolderLabel1").hide();
          $("#bulkIngestFolder1").hide();
		}
    }

    function showReindexStatus(action) {
           $("#reindexTableDisplay").show();
           $("#reindexResult").hide();
           $("#clearResult").hide();
           reindexTable();
       }


    function showReindexResult(action) {
        $("#reindexTable").hide();
        $("#reindexTableDisplay").hide();
        if (action.value == "Clear Status") {
            $("#reindexResult").hide();
            $("#clearResult").hide();
        }
        else {
            $("#reindexResult").show();
            $("#clearResult").show();
        }
        document.getElementById("reindexResult").value = "";
        $.ajaxSetup({async:false});
        $.post("./rebuildIndex",
               {docCategory:$("#documentCategory").val(), docType:$("#documentType").val(), docFormat:$("#documentFormat").val(), action:action.value},
               function (data) {
                   document.getElementById("reindexResult").value = data.toString();
               }, 'html');
    }

    var configInfoXml = null;

    $(document).ready(function () {
        $.ajaxSetup({async:false});
        $.post("./getDocumentConfigInfo", { doccategoryID:'all' }, function (data) {
            configInfoXml = data;
        });
        onChangeCategory();
        onChangeBulkIngestCategory();
        $("#documentCategory").change(function () {
            var doccategoryId = $("#documentCategory").val();
            populateDocTypeDropdown($("#documentType"), configInfoXml, doccategoryId);
        });

        $("#bulkIngestDocCategory").change(function () {
            var doccategoryId1 = $("#bulkIngestDocCategory").val();
            populateBulkIngestDocTypeDropdown($("#bulkIngestDocType"), configInfoXml, doccategoryId1);
        });
    });


    function showBulkIngest(action) {
        $("#bulkIngestTableDisplay").hide();
        if (action.value == "Clear Status") {
            $("#bulkIngestResult").hide();
            $("#clear").hide();
        }
        else {
            $("#bulkIngestResult").show();
            $("#clear").show();
        }
        document.getElementById("bulkIngestResult").value = "";
        if ($("#bulkIngestFolder1").val() != "") {
        $.ajaxSetup({async:false});
            $.post("./bulkIngest", {action:action.value, bulkIngestDataFormat:$("#bulkIngestDataFormat")
                    .val(), bulkIngestDocCategory:$("#bulkIngestDocCategory")
                    .val(), bulkIngestDocType:$("#bulkIngestDocType")
                    .val(), bulkIngestDocFormat:$("#bulkIngestDocFormat").val(), bulkIngestFolder:$("#bulkIngestFolder")
                    .val(),bulkIngestFolder1:$("#bulkIngestFolder1").val()}, function (data) {
                   document.getElementById("bulkIngestResult").value = data.toString();
               }, 'html');
    }
        else {
            document.getElementById("bulkIngestResult").value = "Please give the source folder for bulk ingest.";
        }

    }


    function showBulkIngestStatus(action) {
        $("#bulkIngestTableDisplay").show();
        $("#bulkIngestResult").hide();
        $("#clear").hide();
        bulkIngestTable();
    }

    function bulkIngestTable() {
        $('#bulkIngestTable').datagrid({
                                           url:'bulkIngest?action=statistics',
                                           title:'Status',
                                           fitColumns:false,
                                           nowrap:false,
                                           rownumbers:false,
                                           height:600,
                                           width:1300,
                                           columns:[
                                               [
                                                   {field:'fileName', title:'File Name', width:120 },
                                                   {field:'status', title:'Bulk Ingest Status', width:120},
                                                   {field:'batchStartTime', title:'Batch Start Time'},
                                                   {field:'timeToConvertStringToReqObj', title:'Time To Convert String To ReqObj'},
                                                   {field:'timeToCreateNodesInJcr', title:'Time To Create Nodes In Jcr'},
                                                   {field:'timeToSaveJcrSession', title:'Time To Save Jcr Session'},
                                                   {field:'ingestingTime', title:'Ingesting Time'},
                                                   {field:'timeToConvertXmlToPojo', title:'Time To Convert Xml To Pojo'},
                                                   {field:'timeToConvertToSolrInputDocs', title:'Time To Convert To SolrInputDocs'},
                                                   {field:'timeToIndexSolrInputDocs', title:'Time To Index SolrInputDocs' },
                                                   {field:'timeToSolrCommit', title:'Time To Solr Commit'},
                                                   {field:'indexingTime', title:'Indexing Time'},
                                                   {field:'ingestNIndexTotalTime', title:'Ingest and Index Total Time'},
                                                   {field:'batchTime', title:'Batch Time' },
                                                   {field:'batchEndTime', title:'Batch End Time'}
                                               ]
                                           ]
                                       });
    }

      function reindexTable() {
          $('#reindexTable').datagrid({
                                          url:'rebuildIndex?action=status',
                                          title:'Status',
                                          fitColumns:false,
                                          nowrap:false,
                                          rownumbers:true,
                                          height:400,
                                          width: 1300,
                                          columns:[
                                              [
                                                  {field:'category', title:'Doc Category', width:120},
                                                  {field:'type', title:'Doc Type', width:120},
                                                  {field:'format', title:'Doc Format', width:120},
                                                  {field:'typeStatus', title:'Reindex Status', width:120},
                                                  {field:'batchLoadTime', title:'Batch Data Load Time', width:180},
                                                  {field:'batchStartTime', title:'Batch Index Start Time', width:180},
                                                  {field:'batchEndTime', title:'Batch Index End Time', width:180},
                                                  {field:'batchIndexTime', title:'Batch Index Time', width:140},
                                                  {field:'recordsProcessed', title:'Batch Size', width:130},
                                                  {field:'batchTotalTime', title:'Batch Total Time', width:140},
                                                  {field:'status', title:'Batch Status', width:120}

                                              ]
                                          ]
                                      });
      }


   /* function linking() {
        $.post("./linkBibNInstance", function (data) {
                   document.getElementById("resultLinking").value = data.toString();
               }, 'html');
    }*/

</script>

<div id="sections">
    <ul>
        &lt;%&ndash;<li><a href="#section-bulkIngest">Bulk Ingest</a></li>&ndash;%&gt;
        <li><a href="#section-reindex">Reindex</a></li>
      &lt;%&ndash;  <li><a href="#section-linking">Linking</a></li>&ndash;%&gt;
    </ul>
    <div id="section-bulkIngest">
        <form name="bulkIngest" method="POST">
            <a id="userGuideLink"
               href=" https://wiki.kuali.org/display/OLE/User+Guide+-+OLE+Document+Store#UserGuide-OLEDocumentStore-2.8BulkIngest%28Adminfunction%29|toolbar=0,menubar=0,width=1000,height=1000,scrollbars=1">
                <img align="right" src="images/user-guide.jpg" title="User Guide"/> </a>
			<table width="800">
				<tr>
					<td colspan="2"><h3>Bulk Ingest Process</h3></td>
				</tr>
				<tr>
					<td>Data Format:</td>
					<td><select name="bulkIngestDataFormat" id="bulkIngestDataFormat" class="category" style="width: 250px" onchange="showProcessParams()">
							<option value="DocStore Request" selected="selected">DocStore Request</option>
							<option value="Standard Doc Format">Standard Doc Format</option>
					</select></td>
				</tr>
                <tr>
                    <td><label id="bulkIngestFolderLabel1">Folder:</label>&nbsp;</td>
                    <td><input type="text" name="bulkIngestFolder1" id="bulkIngestFolder1" class="category"
                               style="width: 500px"/></td>
				</tr>
				<tr>
					<td> <label id="bulkIngestDocCategoryLabel">Category:</label>&nbsp;</td>
					<td><select name="bulkIngestDocCategory" id="bulkIngestDocCategory" class="category" style="width: 250px" onclick="populateFields()">
							<option value="work">Work</option>
					</select></td>
				</tr>
				<tr>
					<td> <label id="bulkIngestDocTypeLabel">Type:</label>&nbsp;</td>
					<td><select name="bulkIngestDocType" id="bulkIngestDocType" class="category" style="width: 250px" ></select></td>
				</tr>
				<tr>
					<td> <label id="bulkIngestDocFormatLabel">Format:</label>&nbsp;</td>
					<td><select name="bulkIngestDocFormat" id="bulkIngestDocFormat" class="category" style="width: 250px"></select></td>
				</tr>
				<tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td><label id="bulkIngestFolderLabel">Folder:</label>&nbsp;</td>
                    <td><input type="text" name="bulkIngestFolder" id="bulkIngestFolder" class="category"
                               style="width: 500px"/></td>
                </tr>
				<tr>
					<td colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td width="100">
						<input type="button" name="start" value="Start" onclick="showBulkIngest(start);" style="height: 25px; width: 200px; font-style: oblique; font-weight: bold;" /></td>
                    <td width="100"><input type="button" name="stop" id="bulkIngestStop" value="Stop"
                                                              onclick="showBulkIngest(stop);"
                                                              style="height: 25px; width: 200px; font-style: oblique; font-weight: bold;"/></td>
                    <td width="100"><input type="button" name="statistics" id="bulkIngestStatistics" value="Show Status"
                                           onclick="showBulkIngestStatus(statistics);"
                                           style="height: 25px; width: 200px; font-style: oblique; font-weight: bold;"/></td>
                    <td width="100"><input type="button" name="clearStatistics" id="bulkIngestClear" value="Clear Status"
                                           onclick="showBulkIngest(clearStatistics);"
                                           style="height: 25px; width: 200px; font-style: oblique; font-weight: bold;"/></td>
				</tr>
                </table>
                <table>
				<tr>
					<td colspan="2"><textarea id="bulkIngestResult" rows="20" cols="100" readonly="true"></textarea></td>
				</tr>
				<tr>
					<td colspan="2"><input id="clear" type="button" value="Clear" onclick='document.getElementById("bulkIngestResult").value=""' /></td>
				</tr>
			</table>
            <div id="bulkIngestTableDisplay">
                <table id="bulkIngestTable"></table>
            </div>
		</form>
    </div>
    <div id="section-reindex">
        <form name="rebuildIndex" method="POST">
            <a id="userGuideLink"
               href=" https://wiki.kuali.org/display/OLE/User+Guide+-+OLE+Document+Store#UserGuide-OLEDocumentStore-2.9Rebuildindexes%28Adminfunction%29|toolbar=0,menubar=0,width=1000,height=1000,scrollbars=1">
                <img align="right" src="images/user-guide.jpg" title="User Guide"/> </a>
            <table width="850">
                <tr>
                    <td><h3>Rebuild Indexes Process</h3></td>
                </tr>
                <tr>
                    <td>
                        Category:
                    </td>
                    <td>
                        <select name="documentCategory" id="documentCategory" class="category" style="width: 200px">
                            <option value="work">Work</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>
                        Type:
                    </td>
                    <td>
                        <select name="documentType" id="documentType"  class="category" style="width: 200px">
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>
                        Format:
                    </td>
                    <td>
                        <select name="documentFormat" id="documentFormat" class="category" style="width: 200px">
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td width="125"><input type="button" name="start" value="Start" id="start"
                                           onclick="showReindexResult(start);"
                                           style="height: 25px; width: 200px; font-style: oblique; font-weight: bold;"/>
                    </td>
                    <td width="125"><input type="button" name="stop" value="Stop" id="stop"
                                           onclick="showReindexResult(stop);"
                                           style="height: 25px; width: 200px; font-style: oblique; font-weight: bold;"/>
                    </td>
                    <td width="125"><input type="button" name="showStatus" value="Show Status" id="showStatus"
                                           onclick="showReindexStatus(showStatus);"
                                           style="height: 25px; width: 200px; font-style: oblique; font-weight: bold;"/>
                    </td>
                    <td width="125"><input type="button" name="clearStatus" value="Clear Status" id="clearStatus"
                                           onclick="showReindexResult(clearStatus);"
                                           style="height: 25px; width: 200px; font-style: oblique; font-weight: bold;"/>
                    </td>
                </tr>
            </table>
            <table>
                <tr>
                    <td>
                        <textarea id="reindexResult" rows="20" cols="130" readonly="true"></textarea>
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
   &lt;%&ndash; <div id="section-linking">
        <table>
            <tr>
                <td colspan="2"><h3> Link Bib and Instance </h3></td>
            </tr>
            <tr>
                <td>
                    <input type="button" value="LinkBibNInstance" id="linking" onclick="linking()">
                </td>
            </tr>
            <tr>
                <td>
                    <textarea id="resultLinking" rows="10"  cols="100" readonly="true"></textarea>
                </td>
            </tr>
            <tr>
                <td>
                    <input type="button" value="clear" onclick='document.getElementById("resultLinking").value=""'>
                </td>
            </tr>
        </table>
    </div>&ndash;%&gt;
</div>

&lt;%&ndash;<%@ include file="oleFooter.jsp" %>&ndash;%&gt;

--%>



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

    </ul>


    <div id="section-reindex">
        <form name="rebuildIndex" method="POST">
            <a id="userGuideLink"
               href=" https://wiki.kuali.org/display/OLE/User+Guide+-+OLE+Document+Store#UserGuide-OLEDocumentStore-2.9Rebuildindexes%28Adminfunction%29|toolbar=0,menubar=0,width=1000,height=1000,scrollbars=1">
                <img align="right" src="images/user-guide.jpg" title="User Guide"/> </a>
            <table>
                <tr>
                    <td><h3>Rebuild Solr Indexes</h3></td>
                </tr>
                <tr>
                    <td>
                        Index all, bibs, holdings and items
                    </td>
                    <td>
                 <!--       <select Name="indexType" id="indexType">
                            <option value="ALL">All</option>
                            <option value="Bib">Bib</option>
                            <option value="Holdings">Holdings</option>
                            <option value="Item">Item</option>
                        </select> -->
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
                        <textarea id="reindexResult" rows="20" cols="130" readonly="true"></textarea>
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
</div>