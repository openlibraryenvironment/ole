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
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%--<link rel="STYLESHEET" type="text/css" href="codebase/dhtmlxtree.css">--%>
<title>Repository Content</title>
</head>
<body >
<%
String pageTitle="Tree structure of documents";
%>
<body topmargin="0" leftmargin="0" marginheight="0" marginwidth="0">

<%@ include file="oleHeader.jsp" %>


<table>
    <tr>
        <td>
            <div id="treeboxbox_tree" style="width:500px; height:400px;background-color:#f5f5f5;border :1px solid Silver;overflow:auto;"></div>
        </td>
        <td rowspan="2" style="padding-left:25" valign="top">
            <div id="logarea" style="height:400px;width:500px; padding:3px;border :1px solid Silver; overflow:auto;"></div>
        </td>
    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
</table>
<!-- 
    <li>Selected node ID will be passed to function specified as argument for setDefaultAction(funcObj)</li>
    <li>Dropped node ID and new parent node ID  will be passed to function specified as argument for setDragFunction(funcObj)</li>
    <li>node ID will be passed to the function specified as argument for setOpenAction(aFunc)</li>
    <li>node ID will be passed to the function specified as argument for setDblClickAction(aFunc)</li>
 -->    
<br>
<script>

// ***** Tree Events - START *****
function doLog(str) {
    var log = document.getElementById("logarea");
    log.innerHTML = log.innerHTML + str + "<br/>";
    log.scrollTop = log.scrollHeight;
}
function showDocVersion(docId) {
	if (docId.indexOf('file') == 0) {
	    var log = document.getElementById("logarea");
	    var infoText = tree.getItemText(docId) + " versions:<br/>";
	    infoText = infoText + docVersions[docId];
	    log.innerHTML = infoText;
	    log.scrollTop = log.scrollHeight;
	}
}
function tonclick(id) {
    //doLog("Item " + tree.getItemText(id) + " was selected.");
    showDocVersion(id);
};
function tondblclick(id) {
    //doLog("Item " + tree.getItemText(id) + " was doubleclicked.");
    showDocVersion(id);
};
function tondrag(id, id2) {
    return confirm("Do you want to move node " + tree.getItemText(id) + " to item " + tree.getItemText(id2) + "?");
};
function tonopen(id, mode) {
	//var result = confirm("Do you want to " + (mode > 0 ? "close": "open") + " node " + tree.getItemText(id) + "?"); 
    //return result;
    return true;
};
function toncheck(id, state) {
    doLog("Item " + tree.getItemText(id) + " was " + ((state) ? "checked": "unchecked"));
};
//***** Tree Events - END *****

// ***** Tree definition - START *****
tree = new dhtmlXTreeObject("treeboxbox_tree", "100%", "100%", "0");
tree.setSkin('dhx_skyblue');
tree.setImagePath("codebase/imgs/csh_bluefolders/");


tree.enableCheckBoxes(1);
//tree.enableDragAndDrop(1);
tree.setOnOpenHandler(tonopen);
tree.attachEvent("onOpenEnd", function(nodeId, event) {
    //doLog("An id of open item is " + nodeId);
});
tree.setOnClickHandler(tonclick);
tree.setOnCheckHandler(toncheck);
//tree.setOnDblClickHandler(tondblclick);
//tree.setDragHandler(tondrag);


//tree.loadXML("xml/test.xml");
tree.loadXML("xmldata.jsp");

//***** Tree definition - END *****


</script>

<br><br>

</div> <!-- close <div id="iframe_portlet_container_div"> from newHeader.jsp -->
<%@ include file="oleFooter.jsp" %>
</body>
</html>
