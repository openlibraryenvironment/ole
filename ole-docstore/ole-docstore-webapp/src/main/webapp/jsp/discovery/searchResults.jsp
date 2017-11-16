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
<%@ page language="java" contentType="text/html; charset=UTF-8"
         import="org.kuali.ole.docstore.discovery.*" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page errorPage="errorPage.jsp" %>
<%@ page import="org.kuali.ole.docstore.discovery.web.struts.form.DiscoveryForm" %>
<%@ page import="org.kuali.rice.core.api.config.property.ConfigContext" %>
<jsp:useBean id="discoveryForm" class="org.kuali.ole.docstore.discovery.web.struts.form.DiscoveryForm" scope="session"/>

<%
    long startTime = System.currentTimeMillis();
%>

<html>
<head>

    <script type="text/javascript" src="./script/jquery-1.4.3.min.js"></script>
    <script type="text/javascript" src="./script/jquery-ui-1.8.16.custom/js/jquery-ui-1.8.16.custom.min.js"></script>
    <link type="text/css" rel="Stylesheet"
          href="./script/jquery-ui-1.8.16.custom/css/smoothness/jquery-ui-1.8.16.custom.css"/>
    <script type="text/javascript"
            src="./script/multi-open-accordion/jquery.multi-open-accordion-1.5.3.min.js"></script>
    <script type="text/javascript" src="./script/jquery-ui-spinner/ui.spinner.js"></script>
    <link type="text/css" href="./script/jquery-ui-spinner/ui.spinner.css" rel="stylesheet" media="screen"/>
    <script type="text/javascript" src="./script/multi-expand/expand.js"></script>
    <link type="text/css" href="./script/multi-expand/css/multi-expand.css" rel="stylesheet"/>
    <link rel="stylesheet" href="./css/discovery.css"/>
    <script type="text/javascript" src="./script/blackbird.js"></script>
    <link type="text/css" rel="Stylesheet" href="./css/blackbird.css"/>
    <script type="text/javascript" src="./script/SoftXpath.js"></script>
    <!--
        <script type="text/javascript" src="./script/multi-open-accordion/jquery-ui-1.8.13.custom.min.js"></script>
        <link type="text/css" href="./script/multi-open-accordion/css/jquery-ui-1.8.9.custom/jquery-ui-1.8.9.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="./script/jPaginate/jquery.paginate.js"></script>
        <link type="text/css" href="./script/jPaginate/css/jPaginate.css" rel="stylesheet" media="screen"/>
         <link rel="stylesheet" type="text/css" href="./script/loader/css/jquery-ui.css">
        <script type="text/javascript" src="./script/loader/jquery.loader.js"></script>
        <link rel="stylesheet" type="text/css" href="./script/loader/css/jquery.loader.css">
    -->
</head>
<body>
<textarea id="searchQuery" rows="5" cols="80" style="display:none">
    <c:out value="${discoveryForm.searchQuery}"/>
</textarea>
<textarea id="resultsXmlLength" rows="5" cols="80" style="display:none">
    <%=discoveryForm.getSearchResult().length()%>
</textarea>
<textarea id="resultsXml" rows="5" cols="80" style="display:none"><c:out
        value="${discoveryForm.searchResult}"/></textarea>

<textarea id="sortedResultsXml" rows="50" cols="80" style="display:none"></textarea>


<div id="searchResultsBusyDiv" align="left"
     style="height:99.5%;width:99%;position:absolute;z-index:1;opacity:0.5;background-color:lightgray;color:blue;">
    <b> Loading search results. Please wait...</b>
    <img src="images/busyImage.gif">
</div>


<script>

var sortedResultsXml;
var sortedResultsXPathObj =  new SoftXpath();
var sortedInstanceResultsXPathObj = new SoftXpath();
var tempXPathObj = new SoftXpath();

function submitSearch(searchType) {
    var form = document.forms["discoveryForm"];
    if (searchType != null) {
        document.getElementById("searchType").value = searchType;
    }
    form.submit();
}
function getSearchResultXMLContent(uuid) {
    // Build the docstore url for the xml file content corresponding to the search result.
    var requestUri = "<%=request.getRequestURI()%>";
    var reqUrl = "<%=request.getRequestURL()%>";
    var docFormat=uuid.substr(37,uuid.length);
    uuid=uuid.substring(0,36);
    //var url = reqUrl.replace(requestUri, "") + "/oledocstore/document";
    //var finalurl = url + "?uuid=" + uuid+"&docFormat="+docFormat;
    <%
        String docstoreUrl = ConfigContext.getCurrentContextConfig().getProperty("ole.docstore.url.base");
    %>
    var docstoreUrl = "<%=docstoreUrl%>";
    var finalurl = docstoreUrl + "/document" +"?uuid=" + uuid;
    window.open(finalurl);
}
function editXMLContent(uuid,docType, docFormat, docIndex) {
    var uuidValue = uuid;
    <%
        String url = ConfigContext.getCurrentContextConfig().getProperty("ole.fs.url.base");
    %>
    var url = "<%=url%>";
    url = /*url + "/portal.do?channelTitle=Editor&channelUrl=" +*/ url + "/ole-kr-krad/editorcontroller?viewId=EditorView&methodToCall=load";
    var reqParams = "&docCategory=work&docType=" + docType + "&docFormat=" + docFormat + "&editable=true&docId=" + uuid;
    url = url + reqParams;
    if (docType == 'bibliographic') {
        url = url;
    }
    else if (docFormat == 'oleml') {

        var bibNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1) + "]/arr[@name='bibIdentifier'] /str");
        if (docType == 'instance') {
            url = url + "&bibId=" + bibNodes[0].text;
        }
        else {
            var instanceNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1) + "]/arr[@name='instanceIdentifier']/str");
            url = url + "&bibId=" + bibNodes[0].text + "&instanceId=" + instanceNodes[0].text;
        }
    }
    window.open(url);
}

function getLabelForInstanceOrHoldings() {
    var instanceLink = "";
    var location = "";
    var callNumber = "";
    var callNumberPrefix = "";
    var eResId = "";
    var locationToDisp = xPathObj.selectNodes("//result/doc[1]/arr[@name='Location_display']/str");
    var callNumberPrefixToDisp = xPathObj.selectNodes("//result/doc[1]/arr[@name='CallNumberPrefix_display']/str");
    var callNumberToDisp = xPathObj.selectNodes("//result/doc[1]/arr[@name='CallNumber_display']/str");
    var copyNumberToDisp = xPathObj.selectNodes("//result/doc[1]/arr[@name='CopyNumber_display']/str");
    var eResIdToDisp = xPathObj.selectNodes("//result/doc[1]/arr[@name='EResource_name_display']/str");
    var copyNumber = "";
    if ((locationToDisp != null || locationToDisp != "") && locationToDisp.length > 0) {
        location = locationToDisp[0].text;
    }
    if ((callNumberToDisp != null || callNumberToDisp != "") && callNumberToDisp.length > 0) {
        callNumber = callNumberToDisp[0].text;
    }
    if ((callNumberPrefixToDisp != null || callNumberPrefixToDisp != "") && callNumberPrefixToDisp.length > 0) {
        callNumberPrefix = callNumberPrefixToDisp[0].text;
    }
    if ((copyNumberToDisp != null || copyNumberToDisp != "") && copyNumberToDisp.length > 0) {
        copyNumber = copyNumberToDisp[0].text;
    }
    if(eResIdToDisp != null && eResIdToDisp != "") {
        eResId = eResIdToDisp[0].text;
    }

    instanceLink = addData(instanceLink, location);
    instanceLink = addData(instanceLink, callNumber);
    instanceLink = addData(instanceLink, callNumberPrefix);
    instanceLink = addData(instanceLink, copyNumber);
    instanceLink = addData(instanceLink,eResId);

    return instanceLink;
}

function getLinksHtml(docIndex, uuid, docType, docFormat) {
    var getButtonhtml = '<input type="button" name="Get" value=" View " onClick="getSearchResultXMLContent(\''
            + uuid +'\')"> &nbsp;';
    var getEditButtonhtml='<input type="button" name="Edit" value="Edit" onClick="editXMLContent(\''+uuid+'\',\''+docType+'\',\''+docFormat+'\',\''+docIndex+'\')">';
    //if (docFormat == 'marc'  )

    var srHtmlText1 = [];
    var i = 0;
    var k = 1;
    srHtmlText1[i++] = getButtonhtml;
    srHtmlText1[i++] = "&nbsp;&nbsp;&nbsp;";

    srHtmlText1[i++] = getEditButtonhtml;
    srHtmlText1[i++] = "&nbsp;&nbsp;&nbsp;";

    var bib = "";
    var linkedBibCount = "";
    var instanceId = "";
    var bibNodes = "";

    var bibNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
            + "]/arr[@name='bibIdentifier'] /str");
    //var bibTitle = "";
    if ((bibNodes != "" || bibNodes != null) && bibNodes.length > 0) {
        for (var j = 0; j < bibNodes.length; j++) {
            bib = bibNodes[j].text;
            if ((docType == 'holdings') || (docType == 'item')) {
                var instanceNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                        + "]/arr[@name='instanceIdentifier']/str");

                bibNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                        + "]/arr[@name='bibIdentifier']/str");

                instanceId = instanceNodes[0].text;
            }
            else if (docType == 'instance') {
                instanceId = resultsXPathObj.selectNodes("//result/doc/str[@name='id']")[0].text;
                bibNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                        + "]/arr[@name='bibIdentifier']/str");

                getSortedItemInfo(instanceId);

            }
            if (bibNodes.length > 1) {
                //linkedBibCount = (bibNodes.length - 1) + " more Bib(s) ";
                linkedBibCount = "Bound with Bibs("+(bibNodes.length)+")&nbsp;&nbsp;";
            }
            else if (bibNodes.length == 1) {
                //  linkedBibCount = "No more Bib(s) ";
            }

            var url = "<%=url%>";
            url = /*url + "/portal.do?channelTitle=Editor&channelUrl=" +*/ url
                    + "/ole-kr-krad/editorcontroller?viewId=ShowBibView&amp;methodToCall=showBibs&amp;instanceId="
                    + instanceId + "&amp;docCategory=work&amp;docType=" + docType
                    + "&amp;docFormat=oleml&amp;editable=true";

            var showBibLink = "<a href=" + url + ">" + "<b>" + linkedBibCount + "</b>" + "</a>";
        }
        if (docType != 'bibliographic') {
            srHtmlText1[i++] = showBibLink;
        }
    }

    if ((docType == "bibliographic")) {
        var instances = "";


        getSortedInstanceInfo(bib);

        var instanceNodes = sortedInstanceResultsXPathObj.selectNodes("//result/doc");
        if ((instanceNodes != "" || instanceNodes != null) && instanceNodes.length > 0) {
            for (var j = 0; j < instanceNodes.length; j++) {
                instances = sortedInstanceResultsXPathObj.selectNodes("//result/doc[" + (j+1) + "]/arr[@name='instanceIdentifier']/str");
                var instanceId = instances[0].text;
                getHoldingsIdentifierInfo(instanceId);
                var instanceLink = "";
                var idNodes = xPathObj.selectNodes("//result/doc[1]/arr[@name='holdingsIdentifier']/str");
                if ((idNodes != null || idNodes != "") && idNodes.length > 0) {
                    var id = idNodes[0].text;
                    getInfo(id);
                }

                instanceLink = getLabelForInstanceOrHoldings();

                if(!instanceLink.length > 0){
                    instanceLink = "Instance";
                }

                srHtmlText1[i++] = "<a href='discovery.do?searchType=linksearch&linkValue=" + instanceId
                        + "'><b> " + instanceLink + " </b></a>";
                srHtmlText1[i++] = "&nbsp;&nbsp;";
                //commented for docstore search holdings,item,einstance allignment
//                if (k % 8 == 0) {
//                    //srHtmlText1[i++] = "<br/>";
//                    for (var a = 0; a < 22; a++) {
//                        srHtmlText1[i++] = "&nbsp;";
//                    }
//                }
                k++;
            }
        }

    }else if((docType == "holdings") || (docType == "item") || (docType == "eHoldings") ) {
        var instances = "";
        var instanceNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='instanceIdentifier']/str");
        if ((instanceNodes != "" || instanceNodes != null) && instanceNodes.length > 0) {
            for (var j = 0; j < instanceNodes.length; j++) {
                instances = instanceNodes[j].text;
                if(docType!="item") {
                    getSortedItemInfo(instances);
                }
                getHoldingsIdentifierInfo(instances);
                var idNodes = xPathObj.selectNodes("//result/doc[1]/arr[@name='holdingsIdentifier']/str");
                if ((idNodes != null || idNodes != "") && idNodes.length > 0) {
                    var id = idNodes[0].text;
                    getInfo(id);
                }
                var instanceLink = "";

                instanceLink = getLabelForInstanceOrHoldings();
                if(!instanceLink.length > 0){
                    instanceLink = "Instance";
                }

                srHtmlText1[i++] = "<a href='discovery.do?searchType=linksearch&linkValue=" + instances
                        + "'><b> " + instanceLink + " </b></a>";

                srHtmlText1[i++] = "&nbsp;&nbsp;";
                //commented for docstore search holdings,item,einstance allignment
//                if (k % 8 == 0) {
//                    srHtmlText1[i++] = "<br/>";
//                    for (var a = 0; a < 22; a++) {
//                        srHtmlText1[i++] = "&nbsp;";
//                    }
//                }
                k++;
            }
        }
    }

    var holdings = "";
    var holdingNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
            + "]/arr[@name='holdingsIdentifier']/str");
    var holdings_CallNumber ="";
    var holdings_CallNumberPrefix = "";
    var holdings_Location = "";
    if ((holdingNodes != "" || holdingNodes != null) && holdingNodes.length > 0) {
        for (var j = 0; j < holdingNodes.length; j++) {
            holdings = holdingNodes[j].text;
            getInfo(holdings);
            var holdingsLink = "";
            staffOnlyFlag = "";
            staffOnlyFlagToDisp = xPathObj.selectNodes("//result/doc[1]/str[@name='staffOnlyFlag']");

            if ((staffOnlyFlagToDisp != null || staffOnlyFlagToDisp != "") && staffOnlyFlagToDisp.length > 0) {
                staffOnlyFlag = staffOnlyFlagToDisp[0].text ;
            }

            holdingsLink = getLabelForInstanceOrHoldings();

            if(!holdingsLink.length > 0){
                holdingsLink = "Holdings";
            }
            if(staffOnlyFlag=='true') {
                srHtmlText1[i++] = "<i><a style='color: red;' href='discovery.do?searchType=linksearch&linkValue=" + holdings
                        + "'><b> " +    "<font color='red'>" + holdingsLink + "</font></b></a></i>";
            }
            else{
                srHtmlText1[i++] = "<a href='discovery.do?searchType=linksearch&linkValue=" + holdings
                        + "'><b> " + holdingsLink + "</font> </b></a>";
            }
            srHtmlText1[i++] = "&nbsp;&nbsp;";
            //commented for docstore search holdings,item,einstance allignment
//            if (k % 8 == 0) {
//                srHtmlText1[i++] = "<br/>";
//                for (var a = 0; a < 22; a++) {
//                    srHtmlText1[i++] = "&nbsp;";
//                }
//            }
            k++;
        }
    }


    var items = "";
    var itemObject;
    var itemId="";
    var itemNodes = sortedResultsXPathObj.selectNodes("//result/doc");
    if ((itemNodes != "" || itemNodes != null) && itemNodes.length > 0) {
        for (var j = 0; j < itemNodes.length; j++) {
            itemObject = sortedResultsXPathObj.selectNodes("//result/doc[" + (j+1) + "]/str[@name='id']");
            itemId = itemObject[0].text
            var itemLink = "";
            var enumeration = "";
            var chronology = "";
            var copyNumber = "";
            var staffOnlyFlag;
            var locationToDisp = sortedResultsXPathObj.selectNodes("//result/doc[" + (j+1) + "]/arr[@name='Location_display']/str");
            var callNumberPrefixToDisp = sortedResultsXPathObj.selectNodes("//result/doc[" + (j+1) + "]/arr[@name='CallNumberPrefix_display']/str");
            var callNumberToDisp = sortedResultsXPathObj.selectNodes("//result/doc[" + (j+1) + "]/arr[@name='CallNumber_display']/str");
            var staffOnlyFlagToDisp =   sortedResultsXPathObj.selectNodes("//result/doc[" + (j+1) + "]/str[@name='staffOnlyFlag']");

            if ((staffOnlyFlagToDisp != null || staffOnlyFlagToDisp != "") && staffOnlyFlagToDisp.length > 0) {
                staffOnlyFlag = staffOnlyFlagToDisp[0].text
            }

            if ((locationToDisp != null || locationToDisp != "") && locationToDisp.length > 0) {
                var item_Location = locationToDisp[0].text;
            }
            if ((callNumberToDisp != null || callNumberToDisp != "") && callNumberToDisp.length > 0) {
                var item_CallNumber = callNumberToDisp[0].text;
            }
            if ((callNumberPrefixToDisp != null || callNumberPrefixToDisp != "") && callNumberPrefixToDisp.length > 0) {
                var item_CallNumberPrefix = callNumberPrefixToDisp[0].text;
            }
            var enumerationToDisp = sortedResultsXPathObj.selectNodes("//result/doc[" + (j+1) + "]/str[@name='Enumeration_display']");
            var chronologyToDisp = sortedResultsXPathObj.selectNodes("//result/doc[" + (j+1) + "]/str[@name='Chronology_display']");
            var copyNumberToDisp = sortedResultsXPathObj.selectNodes("//result/doc[" + (j+1)+ "]/str[@name='CopyNumber_display']");
            if ((enumerationToDisp != null || enumerationToDisp != "") && enumerationToDisp.length > 0) {
                enumeration = enumerationToDisp[0].text;
            }
            if ((chronologyToDisp != null || chronologyToDisp != "") && chronologyToDisp.length > 0) {
                chronology = chronologyToDisp[0].text;
            }
            if ((copyNumberToDisp != null || copyNumberToDisp != "") && copyNumberToDisp.length > 0) {
                copyNumber = copyNumberToDisp[0].text;
            }
            if(item_Location != null){
                if((holdings_Location != "" || holdings_Location != null) && (holdings_Location != item_Location)){
                    itemLink = item_Location;
                }
                if(item_CallNumberPrefix != null){
                    if((holdings_CallNumberPrefix != "" || holdings_CallNumberPrefix != null) && (holdings_CallNumberPrefix != item_CallNumberPrefix)){
                        itemLink = addData(itemLink,item_CallNumberPrefix);
                    }
                }
                if(item_CallNumber != null){
                    if((holdings_CallNumber != "" || holdings_CallNumber != null) && (holdings_CallNumber != item_CallNumber)){
                        itemLink = addData(itemLink,item_CallNumber);
                    }
                }
            }else if(item_CallNumberPrefix != null){
                if((holdings_CallNumberPrefix != "" || holdings_CallNumberPrefix != null) && (holdings_CallNumberPrefix != item_CallNumberPrefix)){
                    itemLink = addData(itemLink,item_CallNumberPrefix);
                }
                if(item_CallNumber != null){
                    if((holdings_CallNumber != "" || holdings_CallNumber != null) && (holdings_CallNumber != item_CallNumber)){
                        itemLink = addData(itemLink,item_CallNumber);
                    }
                }
            }else{
                if(item_CallNumber != null){
                    if((holdings_CallNumber != "" || holdings_CallNumber != null) && (holdings_CallNumber != item_CallNumber)){
                        itemLink = addData(itemLink,item_CallNumber);
                    }
                }
            }
            if((enumeration != "") || (chronology != "") || (copyNumber != "")){
                if(itemLink != ""){
                    itemLink = itemLink + "-";
                }
            }
            if(enumeration != null){
                if(chronology != null){
                    itemLink = itemLink + addData(enumeration,chronology);
                    if(copyNumber != null){
                        itemLink = addData(itemLink,copyNumber);
                    }
                }else if(copyNumber != null){
                    itemLink = itemLink + addData(itemLink,copyNumber);
                }
                else{
                    itemLink = itemLink + enumeration;
                }
            }else if(chronology != null){
                if(copyNumber != null){
                    itemLink = itemLink + addData(chronology,copyNumber);
                }else{
                    itemLink = itemLink + chronology;
                }
            }else{
                if(copyNumber != null){
                    itemLink = itemLink + copyNumber;
                }
            }
            if(!itemLink.length > 0){
                itemLink = "Item";
            }

            if(staffOnlyFlag=='true') {
                srHtmlText1[i++] = "<a href='discovery.do?searchType=linksearch&linkValue=" + itemId + "'><b> "
                        + "<i><font color='red'>" +itemLink + "</font></i> </b></a>";
            }
            else{
                srHtmlText1[i++] = "<a href='discovery.do?searchType=linksearch&linkValue=" + itemId + "'><b> "
                        + itemLink + " </b></a>";
            }

            srHtmlText1[i++] = "&nbsp;&nbsp;";
            //commented for docstore search holdings,item,einstance allignment
//            if (k % 8 == 0) {
//                srHtmlText1[i++] = "<br/>";
//                for (var a = 0; a < 22; a++) {
//                    srHtmlText1[i++] = "&nbsp;";
//                }
//            }
            staffOnlyFlag="false";
            k++;
        }
    }
    return srHtmlText1;
}


function getSortedInstanceInfo(bibId) {
    $.ajax({
        async: false,
        url: "./bib/select?q=(bibIdentifier:" +bibId+")AND(DocType:holdings OR DocType:eHoldings)&sort=Location_sort asc,ShelvingOrder_sort asc,CallNumber_sort asc&rows=1000",
        dataType: "text",
        success: function(data) {
            if (sortedInstanceResultsXPathObj.loadXML(data) ) {
            }
            else {
                alert(" not loaded");
            }
        }
    });
}

function getSortedItemInfo(id) {
    $.ajax({
        url: "./bib/select?q=(instanceIdentifier:" +id+")AND(DocType:item)&sort=Enumeration_sort asc,Chronology_display asc,CopyNumber_sort asc&rows=1000",
        dataType: "text",
        success: function(data) {
            if (sortedResultsXPathObj.loadXML(data)) {
                // perform required functions
            }
            else {
                alert(" not loaded");
            }
        }
    });
}
function getInfo(id){
    $.ajaxSetup({async:false});
    $.post("./bib/select?q=id:"+id+"&fl=Title_display,Location_display,CallNumber_display,CallNumberPrefix_display,Enumeration_display,Chronology_display,CopyNumber_display,staffOnlyFlag,EResource_name_display",
            function(data) {
                if (!xPathObj.loadXML(data)) {
                    alert('Loading Results xml failed!!!');
                }
            }, 'html'
    );
}


function addData(label,data) {
    if (data != null && data.length > 0) {
        if (label.length > 0) {
            label = label + "-";
        }
        label = label + data;
    }
    return label;
}

function getHoldingsIdentifierInfo(id){
    $.ajaxSetup({async:false});
    $.post("./bib/select?q=id:"+id+"&fl=holdingsIdentifier",
            function(data) {
                if (!xPathObj.loadXML(data)) {
                    alert('Loading Results xml failed!!!');
                }
            }, 'html'
    );
}

function getBibIdentifierInfo(id){
    $.ajaxSetup({async:false});
    $.post("./bib/select?q=id:"+id+"&fl=bibIdentifier",
            function(data) {
                if (!xPathObj.loadXML(data)) {
                    alert('Loading Results xml failed!!!');
                }
            }, 'html'
    );
}

</script>

<table>
    <tr>
        <td valign="top">
            <html:form action="discovery.do" method="post">
                <div align="left" class="backButton">
                    <input type="hidden" name="searchType" id="searchType" value="">
                    <input type="button" name="Back" id="Back" value="Revise Search"
                           onclick="submitSearch('backToSearch')">
                    <input type="button" name="newSearch" value="New Search" onclick="submitSearch('newSearch')">
                    <input type="hidden" name="resultPageSize" id="resultPageSize"
                           value="${discoveryForm.resultPageSize}">
                    <input type="hidden" name="resultFromIndex" id="resultFromIndex">
                    <input type="hidden" name="resultPageIndex" id="resultPageIndex">
                    <input type="hidden" name="sortByTerms" id="sortByTerms" value="${discoveryForm.sortByTerms}">
                    <input type="hidden" name="action" id="action">
                </div>
            </html:form>
            <input type="hidden" name="selectedFacetName" id="selectedFacetName" value=""/>

            <div id="searchFacetsDiv" class="searchFacetsDiv">
            </div>
            <div id="timeDiv" class="sample">
                Time taken:<br>
                Search: <span id="searchTime"></span><br>
                JSP: <span id="jspTime"></span><br>
                Display: <span id="displayTime"></span><br>
            </div>
        </td>
        <td valign="top" width="100%">
            <table width="100%">
                <tr>
                    <td valign="top" colspan="2" width="100%">
                        <div class="searchCrieteriaDiv">
                            <label for="searchCrieteria">Your Search:</label>
                            <span id="searchCrieteria"></span><br/>
                            <label id="limits" for="limits">Limited To:</label>
                            <span id="limits"></span>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="1">
                        <p id="pageTag" class="pageTag">
                            <label for="pageSpinner">Selected page:</label>
                            <input type="text" id="pageSpinner" size="9" value=""
                                   style="color:#f6931f; font-weight:bold;"/>
                            <input type="button" id="gotoPage" value="Go to page" onclick="goToPage()"/>
                        </p>

                        <div id="paginationDiv" class="paginationDiv"
                             title="Slide or click on the bar to select a page. Or click and use arrow keys.">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td valign="middle" width="30%">
                        <div id="pageList" class="pageList">
                        </div>
                    </td>
                    <td valign="middle" width="70%">
                        <div id="sortRecords" align="right" class="sortDiv">
                            <b>Sort By&nbsp;</b>
                            <select name="sortingorder" id="sortingorder" class="sortingOrder">
                                <option value="titleasc">Title (A-Z)</option>
                                <option value="titledesc">Title (Z-A)</option>
                                <option value="authorasc">Author (A-Z)</option>
                                <option value="authordesc">Author (Z-A)</option>
                                <option value="pubdatedesc">Pub date (new-old)</option>
                                <option value="pubdateasc">Pub date (old-new)</option>
                                <option value="relevance">Relevance</option>
                            </select>
                            <select id="recordsperpage" name="rows" class="rows">
                                <option value="25">Show 25</option>
                                <option value="50">Show 50</option>
                                <option value="100">Show 100</option>
                            </select>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" width="100%">
                        <div id="demo2" class="demo">
                        </div>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>

</body>
<script type="text/javascript">
var resultPageSize = "${discoveryForm.resultPageSize}";
var resultPageIndex = "${discoveryForm.resultPageIndex}";
var searchTerms = '${discoveryForm.searchTerms}';
var sortByTerms = "${discoveryForm.sortByTerms}";
var searchCategory = "${discoveryForm.docCategory}";
var searchType = "${discoveryForm.docType}";
var searchFormat = "${discoveryForm.docFormat}";
var searchTime =  "${discoveryForm.searchTime}";
var languagesInfoXml = null;
function startLoadBusy() {
    $("#searchResultsBusyDiv").show();
    $("#searchResultsBusyDiv").css("z-index", "1");
}
function stopLoadBusy() {
    $("#searchResultsBusyDiv").hide();
    $("#searchResultsBusyDiv").css("z-index", "-1");
}
startLoadBusy();
var startTime = new Date();
log.debug("startTime" + startTime);
log.debug("startTime" + startTime);
var resultsXml = "";
var resultsXPathObj = new SoftXpath();
var xPathObj = new SoftXpath();
function displayResults() {

    resultsXml = document.getElementById("resultsXml").value;
    if (resultsXml.length <= 0) {
        var srHtmlText = [];
        var k = 0;
        $("#pageTag").hide();
        $("#limits").hide();
        $("#sortRecords").hide();
        $("#searchCrieteria").append("<b> DocCategory - </b>" + searchCategory + "<b> DocType - </b>" + searchType
                + "<b> DocFormat - </b>" + searchFormat + "</br>");
        srHtmlText[k++] = "<b>No search results found.</b><br/><br/>";
        $("#demo2").html(srHtmlText.join(''));
    }
    else {
        resultsXPathObj.registerNamespace('', '');
        if (!resultsXPathObj.loadXML(resultsXml)) {
            alert('Loading Results xml failed!!!');
        }
        processSearchResultXml();
    }
}
function displayResults1() {
    $.get("sampleSearchResult.xml", function(data) {
        resultsXml = data;
        $("p#status").text("Loaded.");
        processSearchResultXml();
    });
}

function getLanguageName(languageCode) {
    var languageName = null;

    languageName = languageCode;
    return languageName;
}

function isFacetSelected(termsList, facetValue) {

    var flag = false;
    for (y = 0; y < termsList.length; y++) {
        if (termsList[y] == facetValue) {
            flag = true;
            break;
        }
    }
    return flag;

}

var childWindowId;
function getFacetsContent(fieldName) {
    var moreFacet = name;
    var url = "discovery.do?action=moreFacets&fieldName=" + fieldName;
    document.getElementById("selectedFacetName").value = fieldName;
    childWindowId = window.open(url, 'childWindowId',
            'height=400,width=600,toolbar=1,location=1,menubar=1,status=1,scrollbars=1,resizable=1,replace=1');
    setTimeout("SendToChild('" + fieldName + "')", 1000);
}

function SendToChild(fieldName) {

    //childWindowId.document.moreFacets.fieldName.value = fieldName;
    //childWindowId.document.moreFacets.resultsXml.value = resultsXml;
}

function parseXpathString(facetFieldName) {
    var ret = "";
    if (facetFieldName.indexOf("'") != -1) {
        var fieldName = facetFieldName.split("\'");
        for (var i = 0; i < fieldName.length; i++) {
            if (ret != "") {
                ret += ",\"'\",";
            }
            fieldName[i] = fieldName[i].replace(/"/g, "\",'\"',\"");
            ret += "\"" + fieldName[i] + "\"";
        }
        ret = "concat(" + ret + ")";
    } else {
        ret = "'" + facetFieldName + "'";
    }
    return ret;
}

function getDisplayName(facetFieldName) {
    var facetDisplay;
    var index = 0;
    index = facetFieldName.indexOf(" /r/n!@#$");
    if (index > 0) {
        facetDisplay = facetFieldName.substring(index + 9);
    }
    else {
        facetDisplay = facetFieldName.replace(" /r/n!@#$", " ");
    }
    return facetDisplay;
}

function processSearchResultXml() {
    var facetHtmlText = [];
    var i = 0;
    var value;
    var fieldName;
    var fieldNameTemp;
    var facetFieldWithRange;
    var facetValueToQueryWithRange;
    var facetFieldWithRangeTemp;
    var publicationDateFieldName;
    var facetsRangeCount;
    //var numfound =$(resultsXml).find("result").attr("numFound");
    var numfound = 0;
    nodes = resultsXPathObj.selectNodes("//result/@numFound");
    if (nodes.length > 0) {
        numfound = nodes[0].text;
    }
    //var resultFromIndex = parseInt($(resultsXml).find("result").attr("start"))+ 1;
    var resultFromIndex = "";
    var resultIndex = resultsXPathObj.selectNodes("//result/@start");
    if (resultIndex != null && resultIndex.length > 0) {
        resultFromIndex = parseInt(resultIndex[0].text) + 1;
    }
    //var terms =$(resultsXml).find("lst[name='responseHeader'] str[name='terms']").text();
    var terms = "";
    var termsNodes = resultsXPathObj.selectNodes("//lst[@name='responseHeader']//str[@name='terms']");
    if ((termsNodes != null || termsNodes != "") && termsNodes.length > 0) {
        terms = termsNodes[0].text;
    }
    var termsList = "";
    var uuid = '';
    if (terms.length > 0) {
        termsList = terms.split("|");
    }
    var count = 0;
    var maxCount = 5;
    var counter = 0;
    facetHtmlText[i++] = "&nbsp;<p class='sample'><b><i>Limit Your Search... </i></b></p>";
    //    var facetDocFormat=resultsXPathObj.selectNodes("//result[@name='response']//doc//str[@name='DocFormat']")[0].text;
    //resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/str[@name='DocFormat']")[0].text

    var facetFieldNodeNameList = resultsXPathObj.selectNodes("//lst[@name='facet_fields']/lst/@name");
    for (var docFacet = 0; docFacet < facetFieldNodeNameList.length; docFacet++) {
        var facetName = facetFieldNodeNameList[docFacet].text;

        var facetFieldNameXPath = "//lst[@name='facet_fields']/lst[@name='" + facetName + "']/int/@name";
        var facetFieldNamesList = resultsXPathObj.selectNodes(facetFieldNameXPath);
        count = resultsXPathObj.selectNodes("//lst[@name='facet_fields']/lst[@name='" + facetName + "']/int").length;
        counter = 0;
        fieldNameTemp = facetName.substring(0, facetName.indexOf("_"));
        if (fieldNameTemp == "PublicationDate") {
            fieldNameTemp = "Publication Date";
        }
        facetHtmlText[i++] = "<h3><a href=\"#\">" + fieldNameTemp + "</a></h3><div><ul>";
        for (var j = 0; j < count; j++) {
            var facetFieldName = "";
            var facetFieldNameNodes = resultsXPathObj.selectNodes("//lst[@name='facet_fields']/lst[@name='" + facetName
                    + "']/int[" + (j + 1) + "]/@name");
            if ((facetFieldNameNodes != null || facetFieldNameNodes != "") && (facetFieldNameNodes.length > 0)) {
                facetFieldName = facetFieldNameNodes[0].text;
            }
            var facetFieldValue = resultsXPathObj.selectNodes("//lst[@name='facet_fields']/lst[@name='" + facetName
                    + "']/int[@name=" + parseXpathString(facetFieldName) + "]");
            var values = "";
            if ((facetFieldValue != null || facetFieldValue != "") && facetFieldValue.length > 0) {
                values = facetFieldValue[0].text;
            }
            counter++;
            if (counter <= maxCount) {
                if ((facetFieldName == null || facetFieldName == "" || facetFieldName == "N/A")
                        || (getLanguageName(facetFieldName) == null || getLanguageName(facetFieldName) == "")) {
                    counter--;
                }
                else {
                    if (isFacetSelected(termsList, facetFieldName)) {
                        if (fieldNameTemp == "Publication Date") {
                            facetHtmlText[i++] = "<li>" + getDisplayName(facetFieldName) + "&nbsp;(" + values + ")</li>";
                        }
                        else {
                            facetHtmlText[i++] = "<li>" + getDisplayName(getLanguageName(facetFieldName)) + "&nbsp;(" + values
                                    + ")</li>";
                        }
                    }
                    else {
                        if (fieldNameTemp == "Publication Date") {
                            facetHtmlText[i++] = "<li><a href='discovery.do?searchType=facetSearch&facetValue="
                                    + encodeURIComponent(facetFieldName) + "&facetFieldValue=" + facetName + "'>"
                                    + getDisplayName(facetFieldName) + "&nbsp;(" + values + ")</a></li>";
                        }
                        else {
                            /*facetHtmlText[i++] = "<li><a href='discovery.do?searchType=facetSearch&facetValue="
                             + facetFieldName + "&facetFieldValue=" + facetName + "'>"
                             + getLanguageName(facetFieldName) + "&nbsp;(" + values
                             + ")</a></li>";*/

                            var facetFieldName1 =facetFieldName;
                            facetFieldName1 = facetFieldName1.replace(/\\/g,"\\\\");
                            facetFieldName1 = facetFieldName1.replace(/\'/g,"\\\'");
                            facetFieldName1 = facetFieldName1.replace(/\"/g,"\\\"");
                            facetFieldName1 = facetFieldName1.replace(/</g,"&lt;");
                            var temp = "<li><a href='discovery.do?searchType=facetSearch&facetValue=" + encodeURIComponent(facetFieldName1) + "&facetFieldValue=" + facetName + "'>" + getDisplayName(getLanguageName(facetFieldName)) + "&nbsp;(" + values + ")</a></li>";
                            temp = temp.replace(/%5C'/g,"%5C%27");
                            facetHtmlText[i++] = temp;
                        }
                    }
                }
            }
        }
        facetHtmlText[i++] = "</ul>";
        if (count > maxCount) {
            log.debug("more...");
            facetHtmlText[i++] = '<div align="right"><a href="javascript:getFacetsContent(\'' + facetName
                    + '\')" class="moreLink">more...</a></div>';
        }
        facetHtmlText[i++] = "</div>";
    }
    $("#searchFacetsDiv").html(facetHtmlText.join(''));
    $("#searchFacetsDiv").trigger("setAccordion");

    var srHtmlText1 = [];
    var j = 0;

    var count = resultFromIndex;
    var resultToIndex = parseInt(resultFromIndex) + parseInt(resultPageSize) - 1;
    if (numfound < parseInt(resultToIndex)) {
        resultToIndex = numfound;
    }
    termsList = terms.split("|");
    if (searchTerms == null || searchTerms == "") {
        $("#searchCrieteria").append("<b> DocCategory - </b>" + searchCategory + "<b> DocType - </b>" + searchType
                + "<b> DocFormat - </b>" + searchFormat);
    }
    else {
        $("#searchCrieteria").append("<b>DocCategory - </b>" + searchCategory + "<b> DocType - </b>" + searchType
                + "<b> DocFormat - </b>" + searchFormat + "<br/>");
        $("#searchCrieteria").append("&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;&nbsp;<b>SearchTerms-</b>"
                + searchTerms);
    }
    if (numfound == 0) {
        srHtmlText1[i++] = "<b>No search results found.</b><br/><br/>";
        $("#pageTag").hide();
    }
    for (y = 0; y < termsList.length; y++) {
        if (termsList[y] == null || termsList[y] == "") {
            $("#limits").append("(No limits applied)");
        }
        else {
            /*$("#limits").append(getLanguageName(termsList[y])
             + "<a href='discovery.do?searchType=facetDelete&facetValue=" + termsList[y]
             + "'><b><img border='0' title='Remove' width='15' height='15' alt='(X)' src='images/closeFacets.png'></b></a>"
             + '&nbsp;');*/
            var temp = getLanguageName(termsList[y]);
            temp = temp.replace(/\\\\/g, "\\");
            temp = temp.replace(/\\\"/g, "\"");
            temp = temp.replace(/\\\'/g, "\'");
            var temp1 = "";
            temp1 = encodeURIComponent(termsList[y]);
            temp1 = temp1.replace(/%5C'/g, "%5C%27");
            $("#limits").append((getDisplayName(temp)) + "<a href='discovery.do?searchType=facetDelete&facetValue=" + temp1
                    + "'><b><img border='0' title='Remove' width='15' height='15' alt='(X)' src='images/closeFacets.png'></b></a>"
                    + '&nbsp;');
        }
        if (y < termsList.length - 1)
            srHtmlText1[i++] = "&nbsp;";
    }
    if (numfound > 0) {
        $("#pageList").append("<b>" + resultFromIndex + "-" + resultToIndex + " of " + numfound + " results</b>");
    }
    srHtmlText1[i++] = "";

    var resultDocs = resultsXPathObj.selectNodes("//result/doc");
    for (var docIndex = 0; docIndex < resultDocs.length; docIndex++) {
        uuid = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1) + "]/str[@name='id']")[0].text;
        var docType = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1) + "]/str[@name='DocType']")[0].text;
        var docFormat = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1) + "]/str[@name='DocFormat']")[0]
                .text;
        var resultDoc = resultDocs[i];
        var title = "";;
        if (title == null || title == "") {
            var titleNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                    + "]/arr[@name='Title_display']/str");

            if ((titleNodes != null || titleNodes != "") && titleNodes.length > 0) {
                title = titleNodes[0].text;
                title = title.replace(/</g,"&lt;")
            }
        }
        var id = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1) + "]/str[@name='id']")[0].text;

        var publisher = "";
        //publisher = getMarcPublisherValue(resultsXPathObj, docIndex);
        if (publisher == null || publisher == "") {
            var publisherNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                    + "]/arr[@name='Publisher_display']/str");
            if ((publisherNodes != null || publisherNodes != "") && publisherNodes.length > 0) {
                publisher = publisherNodes[0].text;
                publisher = publisher.replace(/</g,"&lt;");
            }
        }
        var publicationDate = "";
        var publicationDateNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='PublicationDate_display']/str");
        if ((publicationDateNodes != null || publicationDateNodes != "") && publicationDateNodes.length > 0) {
            publicationDate = publicationDateNodes[0].text;
            publicationDate = publicationDate.replace(/</g,"&lt;");
        }

        var localId = "";
        var localIdNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) +
                "]/str[@name='LocalId_display']");
        if((localIdNodes != null || localIdNodes != "") && localIdNodes.length > 0) {
            localId = localIdNodes[0].text;
            localId = localId.replace(/</g,"&lt;") ;
        }

        var author = "";
        // author = getMarcAuthorValue(resultsXPathObj, docIndex);
        if (author == null || author == "") {
            var authorNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                    + "]/arr[@name='Author_display']/str");
            if ((authorNodes != null || authorNodes != "") && authorNodes.length > 0) {
                author = authorNodes[0].text;
                author = author.replace(/</g,"&lt;") ;

            }
        }

        var subject = "";
        //  subject = getMarcSubjectValue(resultsXPathObj, docIndex);
        if (subject == null || subject == "") {
            var subjectNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                    + "]/arr[@name='Subject_display']/str");
            if ((subjectNodes != null || subjectNodes != "") && subjectNodes.length > 0) {
                subject = subjectNodes[0].text;
                subject = subject.replace(/</g,"&lt;");
            }
        }

        var description = "";
        //description = getMarcDescriptionValue(resultsXPathObj, docIndex);
        if (description == null || description == "") {
            var descriptionNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                    + "]/arr[@name='Description_display']/str");
            if ((descriptionNodes != null || descriptionNodes != "") && descriptionNodes.length > 0) {
                description = descriptionNodes[0].text;
                description = description.replace(/</g,"&lt;");

            }
        }

        var location = "";
        //   location = getMarcLocationValue(resultsXPathObj, docIndex);
        if (location == null || location == "") {
            var locationNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                    + "]/arr[@name='Location_display']/str");
            if ((locationNodes != null || locationNodes != "") && locationNodes.length > 0) {
                location = locationNodes[0].text;
                location = location.replace(/</g,"&lt;");
            }
        }

        var isbn = "";
        var isbnNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1) + "]/arr[@name='ISBN_display']/str");
        if ((isbnNodes != null || isbnNodes != "") && isbnNodes.length > 0) {
            isbn = isbnNodes[0].text;
            isbn = isbn.replace(/</g,"&lt;");
        }

        var issn = "";
        var issnNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1) + "]/arr[@name='ISSN_display']/str");
        if ((issnNodes != null || issnNodes != "") && issnNodes.length > 0) {
            issn = issnNodes[0].text;
            issn = issn.replace(/</g,"&lt;");
        }

        var format = "";
        var formatNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='Format_display']/str");
        if ((formatNodes != null || formatNodes != "") && formatNodes.length > 0) {
            format = formatNodes[0].text;
            format = format.replace(/</g,"&lt;");
        }

        //OLEML fields start here...
        //holdings

        var locationLevel = "";
        var locationLevelNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='Location_display']/str");
        if ((locationLevelNodes != null || locationLevelNodes != "") && locationLevelNodes.length > 0) {
            locationLevel = locationLevelNodes[0].text;
        }

        var staffOnlyFlag = "";
        var staffOnlyFlagNode = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/str[@name='staffOnlyFlag']");
        if ((staffOnlyFlagNode != null || staffOnlyFlagNode != "") && staffOnlyFlagNode.length > 0) {
            staffOnlyFlag = staffOnlyFlagNode[0].text;

        }

        var uri = "";
        var uriNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='Uri_display']/str");
        if ((uriNodes != null || uriNodes != "") && uriNodes.length > 0) {
            uri = uriNodes[0].text;
        }

        var holdingsNote = "";
        var holdingsNoteNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='HoldingsNote_display']/str");
        if ((holdingsNoteNodes != null || holdingsNoteNodes != "") && holdingsNoteNodes.length > 0) {
            holdingsNote = holdingsNoteNodes[0].text;
        }

        var receiptsStatus = "";
        var receiptsStatusNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='ReceiptStatus_display']/str");
        if ((receiptsStatusNodes != null || receiptsStatusNodes != "") && receiptsStatusNodes.length > 0) {
            receiptsStatus = receiptsStatusNodes[0].text;
        }

        var copyNumber = "";
        var copyNumbersNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='CopyNumber_display']/str");
        if ((copyNumbersNodes != null || copyNumbersNodes != "") && copyNumbersNodes.length > 0) {
            copyNumber = copyNumbersNodes[0].text;
        }

        var callNumber = "";
        var callNumberNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='CallNumber_display']/str");
        if ((callNumberNodes != null || callNumberNodes != "") && callNumberNodes.length > 0) {
            callNumber = callNumberNodes[0].text;
        }

        var callNumberPrefix = "";
        var callNumberPrefixNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='CallNumberPrefix_display']/str");
        if ((callNumberPrefixNodes != null || callNumberPrefixNodes != "") && callNumberPrefixNodes.length > 0) {
            callNumberPrefix = callNumberPrefixNodes[0].text;
        }

        var callNumberType = "";
        var callNumberTypeNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='ShelvingSchemeCode_display']/str");
        if ((callNumberTypeNodes != null || callNumberTypeNodes != "") && callNumberTypeNodes.length > 0) {
            callNumberType = callNumberTypeNodes[0].text;
        }

        var classificationPart = "";
        var classificationPartNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='ClassificationPart_display']/str");
        if ((classificationPartNodes != null || classificationPartNodes != "") && classificationPartNodes.length > 0) {
            classificationPart = classificationPartNodes[0].text;
        }

        //Item
        var itemBarcode = "";
        var itemBarcodeNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='ItemBarcode_display']/str");
        if ((itemBarcodeNodes != null || itemBarcodeNodes != "") && itemBarcodeNodes.length > 0) {
            itemBarcode = itemBarcodeNodes[0].text;
        }

        var itemType = "";
        var itemTypeNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='ItemTypeFullValue_display']/str");
        if ((itemTypeNodes != null || itemTypeNodes != "") && itemTypeNodes.length > 0) {
            itemType = itemTypeNodes[0].text;
        }

        var vendorLineItemIdentifier = "";
        var vendorLineItemIdentifierNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='VendorLineItemIdentifier_display']/str");
        if ((vendorLineItemIdentifierNodes != null || vendorLineItemIdentifierNodes != "") && vendorLineItemIdentifierNodes.length > 0) {
            vendorLineItemIdentifier = vendorLineItemIdentifierNodes[0].text;
        }

        var shelvingScheme = "";
        var shelvingSchemeNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='ShelvingSchemeValue_display']/str");
        if ((shelvingSchemeNodes != null || shelvingSchemeNodes != "") && shelvingSchemeNodes.length > 0) {
            shelvingScheme = shelvingSchemeNodes[0].text;
        }

        var shelvingOrder = "";
        var shelvingOrderNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='ShelvingOrder_display']/str");
        if ((shelvingOrderNodes != null || shelvingOrderNodes != "") && shelvingOrderNodes.length > 0) {
            shelvingOrder = shelvingOrderNodes[0].text;
        }

        var purchaseOrderLineItemIdentifier = "";
        var purchaseOrderLineItemIdentifierNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='PurchaseOrderLineItemIdentifier_display']/str");
        if ((purchaseOrderLineItemIdentifierNodes != null || purchaseOrderLineItemIdentifierNodes != "") && purchaseOrderLineItemIdentifierNodes.length > 0) {
            purchaseOrderLineItemIdentifier = purchaseOrderLineItemIdentifierNodes[0].text;
        }

        var copyNumber = "";
        var copyNumberNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) +
                "]/str[@name='CopyNumber_display']");
        if((copyNumberNodes != null || copyNumberNodes != "") && copyNumberNodes.length > 0) {
            copyNumber = copyNumberNodes[0].text;
            copyNumber = copyNumber.replace(/</g,"&lt;") ;
        }
        //alert(copyNumber);

        var enumeration = "";
        var enumerationNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) +
                "]/str[@name='Enumeration_display']");
        if ((enumerationNodes != null || enumerationNodes != "") && enumerationNodes.length > 0) {
            enumeration = enumerationNodes[0].text;
            enumeration = enumeration.replace(/</g,"&lt;") ;
        }

        var chronology = "";
        var chronologyNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) +
                "]/str[@name='Chronology_display']");
        if ((chronologyNodes != null || chronologyNodes != "") && chronologyNodes.length > 0) {
            chronology = chronologyNodes[0].text;
            chronology = chronology.replace(/</g,"&lt;") ;
        }


        var volumeNumber = "";
        var volumeNumberNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='VolumeNumber_display']/str");
        if ((volumeNumberNodes != null || volumeNumberNodes != "") && volumeNumberNodes.length > 0) {
            volumeNumber = volumeNumberNodes[0].text;
        }
        //instance

        var instanceSource = "";
        var instanceSourceNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='Source_display']/str");
        if ((instanceSourceNodes != null || instanceSourceNodes != "") && instanceSourceNodes.length > 0) {
            instanceSource = instanceSourceNodes[0].text;
        }
        //fields for einstance
        var accessStatus = "";
        var accessStatusNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='AccessStatus_display']/str");
        if ((accessStatusNodes != null || accessStatusNodes != "") && accessStatusNodes.length > 0) {
            accessStatus = accessStatusNodes[0].text;
        }

        var platForm = "";
        var platFormNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='Platform_display']/str");
        if ((platFormNodes != null || platFormNodes != "") && platFormNodes.length > 0) {
            platForm = platFormNodes[0].text;
        }

        var imprint = "";
        var imprintNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='Imprint_display']/str");
        if ((imprintNodes != null || imprintNodes != "") && imprintNodes.length > 0) {
            imprint = imprintNodes[0].text;
        }

        var statisticalCode = "";
        var statisticalCodeNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='StatisticalSearchingCodeValue_display']/str");
        if ((statisticalCodeNodes != null || statisticalCodeNodes != "") && statisticalCodeNodes.length > 0) {
            statisticalCode = statisticalCodeNodes[0].text;
        }

        var eResName = "";
        var eResNameNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='EResource_name_display']/str");
        if ((eResNameNodes != null || eResNameNodes != "") && eResNameNodes.length > 0) {
            eResName = eResNameNodes[0].text;
        }

        //patron

        var recordNumber = "";
        var recordNumberNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='RecordNumber_display']/str");
        if ((recordNumberNodes != null || recordNumberNodes != "") && recordNumberNodes.length > 0) {
            recordNumber = recordNumberNodes[0].text;
        }

        var beginDate = "";
        var beginDateNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='BeginDate_display']/str");
        if ((beginDateNodes != null || beginDateNodes != "") && beginDateNodes.length > 0) {
            beginDate = beginDateNodes[0].text;
        }

        var name = "";
        var nameNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1) + "]/arr[@name='Name_display']/str");
        if ((nameNodes != null || nameNodes != "") && nameNodes.length > 0) {
            name = nameNodes[0].text;
        }

        var barrowerType = "";
        var barrowerTypeNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='BorrowerType_display']/str");
        if ((barrowerTypeNodes != null || barrowerTypeNodes != "") && barrowerTypeNodes.length > 0) {
            barrowerType = barrowerTypeNodes[0].text;
        }

        var barCodeNumber = "";
        var barCodeNumberNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='BarCodeNumber_display']/str");
        if ((barCodeNumberNodes != null || barCodeNumberNodes != "") && barCodeNumberNodes.length > 0) {
            barCodeNumber = barCodeNumberNodes[0].text;
        }

        var barCodeStatus = "";
        var barCodeStatusNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='BarCodeStatus_display']/str");
        if ((barCodeStatusNodes != null || barCodeStatusNodes != "") && barCodeStatusNodes.length > 0) {
            barCodeStatus = barCodeStatusNodes[0].text;
        }

        //ends here...
        //license fields starts here..

        var contractNumber="";
        var contractNumberNodes=resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='ContractNumber_display']/str");
        if ((contractNumberNodes != null || contractNumberNodes != "") && contractNumberNodes.length > 0) {
            contractNumber = contractNumberNodes[0].text;
        }
        var licensee="";
        var licenseeNodes=resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='Licensee_display']/str");
        if ((licenseeNodes != null || licenseeNodes != "") && licenseeNodes.length > 0) {
            licensee = licenseeNodes[0].text;
        }

        var licensor="";
        var licensorNodes=resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='Licensor_display']/str");
        if ((licensorNodes != null || licensorNodes != "") && licensorNodes.length > 0) {
            licensor = licensorNodes[0].text;
        }
        var status="";
        var statusNodes=resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='Status_display']/str");
        if ((statusNodes != null || statusNodes != "") && statusNodes.length > 0) {
            status = statusNodes[0].text;
        }

        var type="";
        var typeNodes=resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='Type_display']/str");
        if ((typeNodes != null || typeNodes != "") && typeNodes.length > 0) {
            type = typeNodes[0].text;
        }
        var method="";
        var methodNodes=resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='Method_display']/str");
        if ((methodNodes != null || methodNodes != "") && methodNodes.length > 0) {
            method = methodNodes[0].text;
        }

        var filename="";
        var filenameNodes=resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='FileName_display']/str");
        if ((filenameNodes != null || filenameNodes != "") && filenameNodes.length > 0) {
            filename = filenameNodes[0].text;
        }

        var dateUploaded="";
        var dateUploadedNodes=resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='DateUploaded_display']/str");
        if ((dateUploadedNodes != null || dateUploadedNodes != "") && dateUploadedNodes.length > 0) {
            dateUploaded = dateUploadedNodes[0].text;
        }
        var owner="";
        var ownerNodes=resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='Owner_display']/str");
        if ((ownerNodes != null || ownerNodes != "") && ownerNodes.length > 0) {
            owner = ownerNodes[0].text;
        }

        var itemStatus = "";
        var itemStatusNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='ItemStatus_display']/str");
        if ((itemStatusNodes != null || itemStatusNodes != "") && itemStatusNodes.length > 0) {
            itemStatus = itemStatusNodes[0].text;
        }
        //ends here..

        if (docFormat == 'marc' || docFormat == 'dublinunq' || docFormat == 'dublin' || docFormat == 'onixpl') {
            if(staffOnlyFlag=='true')  {
                srHtmlText1[i++] = '<h3 class="expand"><i><font color="red">' + count + ".&nbsp;"+ getHighlightedText(id, title, 'Title_search')
                        + '</font></i></h3>';
            }else{
                srHtmlText1[i++] = '<h3 class="expand">' + count + ".&nbsp;" + getHighlightedText(id, title, 'Title_search')
                        + '</h3>';

            }

        }
        if (docFormat == 'oleml') {
            if (docType == 'patron') {
                srHtmlText1[i++] = '<h3 class="expand">' + count + ".&nbsp;" + getHighlightedText(id, locationLevel, 'Location_search')
                        + '</h3>';
            }else if(docType == 'item'){
                if(staffOnlyFlag=='true')  {
                    srHtmlText1[i++] = '<h3 class="expand"><i><font color="red">' + count + ".&nbsp;"+ getHighlightedText(id, locationLevel, 'Location_search')
                            + '</font></i></h3>';
                }else{
                    srHtmlText1[i++] = '<h3 class="expand">' + count + ".&nbsp;" + getHighlightedText(id, locationLevel, 'Location_search')
                            + '</h3>';
                }
            }else if(docType == 'holdings'){

                if(staffOnlyFlag=='true')  {
                    srHtmlText1[i++] = '<h3 class="expand"><i><font color="red">' + count + ".&nbsp;";
                }
                else{
                    srHtmlText1[i++] = '<h3 class="expand">' + count + ".&nbsp;";
                }

                if(locationLevel != null) {
                    srHtmlText1[i++] = getHighlightedText(id, locationLevel, 'Location_search');
                    if((callNumberPrefix !=null || callNumberPrefix !="") && callNumberPrefix.length>0) {
                        srHtmlText1[i++] =  "-" +  getHighlightedText(id, callNumberPrefix, 'CallNumberPrefix_search') ;
                        if((callNumber != null  || callNumber !="") && callNumber.length>0) {
                            srHtmlText1[i++] = "-" +   getHighlightedText(id, callNumber, 'CallNumber_search');
                        }
                    }else{
                        if((callNumber != null  || callNumber !="") && callNumber.length>0) {
                            srHtmlText1[i++] = "-" + getHighlightedText(id, callNumber, 'CallNumber_search');
                        }
                    }
                    if((copyNumber !=null || copyNumber !="") && copyNumber.length>0) {
                        srHtmlText1[i++] =  "-"  + getHighlightedText(id, copyNumber, 'CopyNumber_search') + '</font></i></h3>';

                    }
                }else{
                    if((callNumberPrefix !=null || callNumberPrefix !="") && callNumberPrefix.length>0) {
                        srHtmlText1[i++] =  "-" + getHighlightedText(id, callNumberPrefix, 'CallNumberPrefix_search') ;
                        if((callNumber != null  || callNumber !="") && callNumber.length>0) {
                            srHtmlText1[i++] = "-" +   getHighlightedText(id, callNumber, 'CallNumber_search');
                        }
                    }else{
                        if((callNumber != null  || callNumber !="") && callNumber.length>0) {
                            srHtmlText1[i++] =  "-"+getHighlightedText(id, callNumber, 'CallNumber_search');
                        }
                    }
                    if((copyNumber !=null || copyNumber !="") && copyNumber.length>0) {
                        srHtmlText1[i++] = "-"  + getHighlightedText(id, copyNumber, 'CopyNumber_search');
                    }
                }

                if(staffOnlyFlag=='true')  {
                    srHtmlText1[i++] = '</font></i></h3>';
                }
                else{
                    srHtmlText1[i++] = '</h3>';
                }

            }else if(docType == 'instance') {
                getHoldingsIdentifierInfo(id);
                var id;
                var idNodes = xPathObj.selectNodes("//result/doc[1]/arr[@name='holdingsIdentifier']/str");
                if ((idNodes != null || idNodes != "") && idNodes.length > 0) {
                    id = idNodes[0].text;
                    getInfo(id);
                }


                srHtmlText1[i++] = '<h3 class="expand">' + count + ".&nbsp;";
                srHtmlText1[i++] = getLabelForInstanceOrHoldings();
                srHtmlText1[i++] = '</h3>';
            }
            else if(docType == "eHoldings") {
                if(staffOnlyFlag=='true')  {
                    srHtmlText1[i++] = '<h3 class="expand"><i><font color="red">' + count + ".&nbsp;";
                }
                else{
                    srHtmlText1[i++] = '<h3 class="expand">' + count + ".&nbsp;";
                }
                var eHoldingsTitle = "";
                eHoldingsTitle = addData(eHoldingsTitle, locationLevel);
                eHoldingsTitle = addData(eHoldingsTitle, callNumber);
                eHoldingsTitle = addData(eHoldingsTitle, callNumberPrefix);
                eHoldingsTitle = addData(eHoldingsTitle, copyNumber);
                eHoldingsTitle = addData(eHoldingsTitle,eResName);
                srHtmlText1[i++] = eHoldingsTitle;
                if(staffOnlyFlag=='true')  {
                    srHtmlText1[i++] = '</font></i></h3>';
                }
                else{
                    srHtmlText1[i++] = '</h3>';
                }
            }
            else {
                if(staffOnlyFlag=='true')  {

                    srHtmlText1[i++] = '<h3 class="expand"><i><font color="red">' + count + ".&nbsp;"+ getHighlightedText(id, id, 'id')
                            + '</font></i></h3>';
                }else{

                    srHtmlText1[i++] = '<h3 class="expand">' + count + ".&nbsp;" + getHighlightedText(id, id, 'id')
                            + '</h3>';
                }
            }
        }


        else if(docFormat == 'pdf' || docFormat == 'xslt' || docFormat == 'doc') {
            srHtmlText1[i++] = '<h3 class="expand">' + count + ".&nbsp;" + getHighlightedText(id, id, 'id')
                    + '</h3>';
        }

        srHtmlText1[i++] = '<div class="collapse"><p>';
        srHtmlText1[i++] = "<div class='newDiv'>";
        srHtmlText1[i++] = "<dl>";

        if (docType == 'bibliographic') {

            srHtmlText1[i++] = "<dt>Local Identifier:</dt><dd>" + getHighlightedText(id, localId, 'LocalId_display') + "</dd>"; + "</dd>";
            if (localId == "" || localId == null) {
                srHtmlText1[i++] = "<br/>";
            }

            srHtmlText1[i++] = "<dt>Author:</dt><dd>" + getHighlightedText(id, author, 'Author_search') + "</dd>";
            if (author == "" || author == null) {
                srHtmlText1[i++] = "<br/>";
            }
            if (docFormat == 'dublin' || docFormat == 'dublinunq') {
                srHtmlText1[i++] = "<dt>Publisher:</dt><dd>" + getHighlightedText(id, publisher, 'Publisher_search')
                        + ' ' + publicationDate + "</dd>";
                if (publisher == "" || publisher == null) {
                    srHtmlText1[i++] = "<br/>";
                }
            }
            if (docFormat == 'marc' || docFormat == 'all') {
                srHtmlText1[i++] = "<dt>Publisher:</dt><dd>" + getHighlightedText(id, publisher, 'Publisher_search')
                        + "</dd>";
                if (publisher == "" || publisher == null) {
                    srHtmlText1[i++] = "<br/>";
                }

            }
            if (docFormat != 'dublin') {
                srHtmlText1[i++] = "<dt>Description:</dt><dd>" + getHighlightedText(id, description,
                        'Description_search') + "</dd>";
                if (description == "" || description == null) {
                    srHtmlText1[i++] = "<br/>";
                }
            }
            srHtmlText1[i++] = "<dt>Subject:</dt><dd>" + getHighlightedText(id, subject, 'Subject_search') + "</dd>";
            if (subject == "" || subject == null) {
                srHtmlText1[i++] = "<br/>";
            }
            srHtmlText1[i++] = "<dt>Location:</dt><dd>" + getHighlightedText(id, location, 'Location_search') + "</dd>";
            if (location == "" || location == null) {
                srHtmlText1[i++] = "<br/>";
            }
            if (isbn != null && isbn != '') {
                srHtmlText1[i++] = "<dt>ISBN: </dt><dd>" + getHighlightedText(id, isbn, 'ISBN_search') + "</dd>";
                if (isbn == "" || isbn == null) {
                    srHtmlText1[i++] = "<br/>";
                }
            }
            if (issn != null && issn != '') {
                srHtmlText1[i++] = "<dt>ISSN: </dt><dd>" + getHighlightedText(id, issn, 'ISSN_search') + "</dd>";
                if (issn == "" || issn == null) {
                    srHtmlText1[i++] = "<br/>";
                }
            }
            srHtmlText1[i++] = "<dt>Format:</dt><dd>" + getHighlightedText(id, format, 'Format_search') + "</dd>";
            if (format == "" || format == null) {
                srHtmlText1[i++] = "<br/>";
            }

        } else if (docType == 'holdings') {

            getBibIdentifierInfo(id);
            var idNodes = xPathObj.selectNodes("//result/doc[1]/arr[@name='bibIdentifier']/str");
            if((idNodes != "" || idNodes != null) && idNodes.length > 0 )  {
                var bibId = idNodes[0].text;
                getInfo(bibId);
                var titleToDisp = xPathObj.selectNodes("//result/doc[1]/arr[@name='Title_display']/str");
                var title;
                if(titleToDisp != null && titleToDisp.length > 0) {
                    title= titleToDisp[0].text;
                }
                srHtmlText1[i++] = "<dt>Title:</dt><dd>" + "<a href='discovery.do?searchType=linksearch&linkValue=" + bibId + "'><b> "
                        + title + " </b></a>";
                if (title == "" || title == null) {
                    srHtmlText1[i++] = "<br/>";
                }
            }

            srHtmlText1[i++] = "<dt>Local Identifier:</dt><dd>" + getHighlightedText(id, localId, 'LocalId_display') + "</dd>";
            if (localId == "" || localId == null) {
                srHtmlText1[i++] = "<br/>";
            }
            srHtmlText1[i++] = "<dt>Location:</dt><dd>" + getHighlightedText(id, locationLevel,
                    'Location_search')
                    + "</dd>";
            if (locationLevel == "" || locationLevel == null) {
                srHtmlText1[i++] = "<br/>";
            }

            srHtmlText1[i++] = "<dt>Call Number Type:</dt><dd>" + getHighlightedText(id, callNumberType,
                    'ShelvingSchemeCode_search')
                    + "</dd>";
            if (callNumberType == "" || callNumberType == null) {
                srHtmlText1[i++] = "<br/>";
            }

            srHtmlText1[i++] = "<dt>Call Number Prefix:</dt><dd>" + getHighlightedText(id, callNumberPrefix,
                    'CallNumberPrefix_search')
                    + "</dd>";
            if (callNumberPrefix == "" || callNumberPrefix == null) {
                srHtmlText1[i++] = "<br/>";
            }

            srHtmlText1[i++] = "<dt>Call Number:</dt><dd>" + getHighlightedText(id, callNumber,
                    'CallNumber_search')
                    + "</dd>";
            if (callNumber == "" || callNumber == null) {
                srHtmlText1[i++] = "<br/>";
            }

            srHtmlText1[i++] = "<dt>Holding Note:</dt><dd>" + getHighlightedText(id, holdingsNote,
                    'HoldingsNote_search') + "</dd>";
            if (holdingsNote == "" || holdingsNote == null) {
                srHtmlText1[i++] = "<br/>";
            }

            srHtmlText1[i++] = "<dt>Receipt Status:</dt><dd>" + getHighlightedText(id, receiptsStatus,
                    'ReceiptStatus_search') + "</dd>";
            if (receiptsStatus == "" || receiptsStatus == null) {
                srHtmlText1[i++] = "<br/>";
            }


            srHtmlText1[i++] = "<dt>CopyNumber:</dt><dd>" + getHighlightedText(id, copyNumber,
                    'CopyNumber_search') + "</dd>";
            if (copyNumber == "" || copyNumber == null) {
                srHtmlText1[i++] = "<br/>";
            }
            /*srHtmlText1[i++] = "<dt>Holding URI:</dt><dd>" + getHighlightedText(id, uri, 'Uri_search')
             + "</dd>";
             if (uri == "" || uri == null) {
             srHtmlText1[i++] = "<br/>";
             }
             srHtmlText1[i++] = "<dt>Classification Part:</dt><dd>" + getHighlightedText(id, classificationPart,
             'ClassificationPart_search') + "</dd>";
             if (classificationPart == "" || classificationPart == null) {
             srHtmlText1[i++] = "<br/>";
             }*/
        }
        else if (docType == 'item') {

            getBibIdentifierInfo(id);
            var idNodes = xPathObj.selectNodes("//result/doc[1]/arr[@name='bibIdentifier']/str");
            if((idNodes != "" || idNodes != null) && idNodes.length > 0 )  {
                var bibId = idNodes[0].text;
                getInfo(bibId);
                var titleToDisp = xPathObj.selectNodes("//result/doc[1]/arr[@name='Title_display']/str");
                var title;
                if(titleToDisp != null && titleToDisp.length > 0) {
                    title= titleToDisp[0].text;
                }
                srHtmlText1[i++] = "<dt>Title:</dt><dd>" + "<a href='discovery.do?searchType=linksearch&linkValue=" + bibId + "'><b> "
                        + title + " </b></a>";
                if (title == "" || title == null) {
                    srHtmlText1[i++] = "<br/>";
                }
            }
            srHtmlText1[i++] = "<dt>Local Identifier:</dt><dd>" + getHighlightedText(id, localId, 'LocalId_display') + "</dd>";
            if (localId == "" || localId == null) {
                srHtmlText1[i++] = "<br/>";
            }
            srHtmlText1[i++] = "<dt>Location:</dt><dd>" + getHighlightedText(id, locationLevel,
                    'Location_search')
                    + "</dd>";
            if (locationLevel == "" || locationLevel == null) {
                srHtmlText1[i++] = "<br/>";
            }

            srHtmlText1[i++] = "<dt>Call Number Type:</dt><dd>" + getHighlightedText(id, callNumberType,
                    'ShelvingSchemeCode_search')
                    + "</dd>";
            if (callNumberType == "" || callNumberType == null) {
                srHtmlText1[i++] = "<br/>";
            }

            srHtmlText1[i++] = "<dt>Call Number Prefix:</dt><dd>" + getHighlightedText(id, callNumberPrefix,
                    'CallNumberPrefix_search')
                    + "</dd>";
            if (callNumberPrefix == "" || callNumberPrefix == null) {
                srHtmlText1[i++] = "<br/>";
            }

            srHtmlText1[i++] = "<dt>Call Number:</dt><dd>" + getHighlightedText(id, callNumber,
                    'CallNumber_search')
                    + "</dd>";
            if (callNumber == "" || callNumber == null) {
                srHtmlText1[i++] = "<br/>";
            }

            srHtmlText1[i++] = "<dt>Barcode:</dt><dd>" + getHighlightedText(id, itemBarcode, 'ItemBarcode_search')
                    + "</dd>";
            if (itemBarcode == "" || itemBarcode == null) {
                srHtmlText1[i++] = "<br/>";
            }

            srHtmlText1[i++] = "<dt>Item Type:</dt><dd>" + getHighlightedText(id, itemType, 'ItemTypeFullValue_search')
                    + "</dd>";
            if (itemType == "" || itemType == null) {
                srHtmlText1[i++] = "<br/>";
            }
            srHtmlText1[i++] = "<dt>Copy Number:</dt><dd>" + getHighlightedText(id, copyNumber, 'CopyNumber_search')
                    + "</dd>";
            if (copyNumber == "" || copyNumber == null) {
                srHtmlText1[i++] = "<br/>";
            }
            srHtmlText1[i++] = "<dt>Enumeration :</dt><dd>" + getHighlightedText(id, enumeration, 'Enumeration_search') + "</dd>";
            if (enumeration == "" || enumeration == null) {
                srHtmlText1[i++] = "<br/>";
            }

            srHtmlText1[i++] = "<dt>Chronology :</dt><dd>" + getHighlightedText(id, chronology, 'Chronology_search') + "</dd>";
            if (chronology == "" || chronology == null) {
                srHtmlText1[i++] = "<br/>";
            }


            srHtmlText1[i++] = "<dt>Item Status :</dt><dd>" + getHighlightedText(id, itemStatus, 'ItemStatus_search') + "</dd>";
            if (itemStatus == "" || itemStatus == null) {
                srHtmlText1[i++] = "<br/>";
            }

            /*srHtmlText1[i++] = "<dt>Vendorline id:</dt><dd>" + getHighlightedText(id, vendorLineItemIdentifier, 'VendorLineItemIdentifier_search')
             + "</dd>";
             if (vendorLineItemIdentifier == "" || vendorLineItemIdentifier == null) {
             srHtmlText1[i++] = "<br/>";
             }
             srHtmlText1[i++] = "<dt>Shelving Scheme:</dt><dd>" + getHighlightedText(id, shelvingScheme,
             'ShelvingSchemeValue_search') + "</dd>";
             if (shelvingScheme == "" || shelvingScheme == null) {
             srHtmlText1[i++] = "<br/>";
             }
             srHtmlText1[i++] = "<dt>Shelving Order:</dt><dd>" + getHighlightedText(id, shelvingOrder,
             'ShelvingOrder_search') + "</dd>";
             if (shelvingOrder == "" || shelvingOrder == null) {
             srHtmlText1[i++] = "<br/>";
             }
             srHtmlText1[i++] = "<dt>Purchaseorder id:</dt><dd>" + getHighlightedText(id, purchaseOrderLineItemIdentifier,
             'PurchaseOrderLineItemIdentifier_search') + "</dd>";
             if (purchaseOrderLineItemIdentifier == "" || purchaseOrderLineItemIdentifier == null) {
             srHtmlText1[i++] = "<br/>";
             }
             srHtmlText1[i++] = "<dt>Copy Number:</dt><dd>" + getHighlightedText(id, copyNumber, 'CopyNumber_search')
             + "</dd>";
             if (copyNumber == "" || copyNumber == null) {
             srHtmlText1[i++] = "<br/>";
             }
             srHtmlText1[i++] = "<dt>Copy Number:</dt><dd>" + getHighlightedText(id, enumeration, 'Enumeration_search') + "</dd>";
             if (enumeration == "" || enumeration == null) {
             srHtmlText1[i++] = "<br/>";
             }

             srHtmlText1[i++] = "<dt>Volume Number:</dt><dd>" + getHighlightedText(id, volumeNumber,
             'VolumeNumber_search') + "</dd>";
             if (volumeNumber == "" || volumeNumber == null) {
             srHtmlText1[i++] = "<br/>";
             }*/
        }

        else if (docType == 'instance') {

            getBibIdentifierInfo(id);
            var idNodes = xPathObj.selectNodes("//result/doc[1]/arr[@name='bibIdentifier']/str");
            if((idNodes != "" || idNodes != null) && idNodes.length > 0 )  {
                var bibId = idNodes[0].text;
                getInfo(bibId);
                var titleToDisp = xPathObj.selectNodes("//result/doc[1]/arr[@name='Title_display']/str");
                var title;
                if(titleToDisp != null && titleToDisp.length > 0) {
                    title= titleToDisp[0].text;
                }
                srHtmlText1[i++] = "<dt>Title:</dt><dd>" + "<a href='discovery.do?searchType=linksearch&linkValue=" + bibId + "'><b> "
                        + title + " </b></a>";
                if (title == "" || title == null) {
                    srHtmlText1[i++] = "<br/>";
                }
            }

            srHtmlText1[i++] = "<dt>Local Identifier:</dt><dd>" + getHighlightedText(id, localId, 'LocalId_display') + "</dd>";
            if (localId == "" || localId == null) {
                srHtmlText1[i++] = "<br/>";
            }
            srHtmlText1[i++] = "<dt>Source:</dt><dd>" + getHighlightedText(id, instanceSource, 'Source_search')
                    + "</dd>";
            if (instanceSource == "" || instanceSource == null) {
                srHtmlText1[i++] = "<br/>";
            }
        } else if (docType == 'patron') {
            srHtmlText1[i++] = "<dt>Record Number:</dt><dd>" + getHighlightedText(id, recordNumber,
                    'RecordNumber_search') + "</dd>";
            if (recordNumber == "" || recordNumber == null) {
                srHtmlText1[i++] = "<br/>";
            }
            srHtmlText1[i++] = "<dt>Begin Date:</dt><dd>" + getHighlightedText(id, beginDate, 'BeginDate_search')
                    + "</dd>";
            if (beginDate == "" || beginDate == null) {
                srHtmlText1[i++] = "<br/>";
            }
            srHtmlText1[i++] = "<dt>Name:</dt><dd>" + getHighlightedText(id, name, 'name_search') + "</dd>";
            if (name == "" || name == null) {
                srHtmlText1[i++] = "<br/>";
            }
            srHtmlText1[i++] = "<dt>Barrower Type:</dt><dd>" + getHighlightedText(id, barrowerType,
                    'BarrowerType_search') + "</dd>";
            if (barrowerType == "" || barrowerType == null) {
                srHtmlText1[i++] = "<br/>";
            }
            srHtmlText1[i++] = "<dt>Barcode Number:</dt><dd>" + getHighlightedText(id, barCodeNumber,
                    'BarCodeNumber_search') + "</dd>";
            if (barCodeNumber == "" || barCodeNumber == null) {
                srHtmlText1[i++] = "<br/>";
            }
            srHtmlText1[i++] = "<dt>Barcode Status:</dt><dd>" + getHighlightedText(id, barCodeStatus,
                    'BarCodeNumber_search') + "</dd>";
            if (barCodeStatus == "" || barCodeStatus == null) {
                srHtmlText1[i++] = "<br/>";
            }
        }
        else if(docType=='license')
        {
            if(docFormat=='onixpl')
            {
                srHtmlText1[i++] = "<dt>Contract Number:</dt><dd>" + getHighlightedText(id, contractNumber,
                        'ContractNumber_search') + "</dd>";
                if (contractNumber == "" || contractNumber == null) {
                    srHtmlText1[i++] = "<br/>";
                }

                srHtmlText1[i++] = "<dt>Licensee:</dt><dd>" + getHighlightedText(id, licensee,
                        'Licensee_search') + "</dd>";
                if (licensee == "" || licensee == null) {
                    srHtmlText1[i++] = "<br/>";
                }

                srHtmlText1[i++] = "<dt>Licensor:</dt><dd>" + getHighlightedText(id, licensor, 'Licensor_search') + "</dd>";
                if (licensor == "" || licensor == null) {
                    srHtmlText1[i++] = "<br/>";
                }

                srHtmlText1[i++] = "<dt>Type:</dt><dd>" + getHighlightedText(id, type,'Type_search')+"</dd>";
                if (type == "" || type == null) {
                    srHtmlText1[i++] = "<br/>";
                }
            }
            else
            {
                srHtmlText1[i++] = "<dt>Name:</dt><dd>" + getHighlightedText(id, name,'Name_search') + "</dd>";
                if (name == "" || name == null) {
                    srHtmlText1[i++] = "<br/>";
                }

                srHtmlText1[i++] = "<dt>File Name:</dt><dd>" + getHighlightedText(id, filename,
                        'FileName_search') + "</dd>";
                if (filename == "" || filename == null) {
                    srHtmlText1[i++] = "<br/>";
                }

                srHtmlText1[i++] = "<dt>Date Uploaded:</dt><dd>" + getHighlightedText(id, dateUploaded, 'DateUploaded_search') + "</dd>";
                if (dateUploaded == "" || dateUploaded == null) {
                    srHtmlText1[i++] = "<br/>";
                }

                srHtmlText1[i++] = "<dt>Owner:</dt><dd>" + getHighlightedText(id, owner,'Owner_search')+"</dd>";
                if (owner == "" || owner == null) {
                    srHtmlText1[i++] = "<br/>";
                }
            }
        }
        else if (docType == "eHoldings") {

            getBibIdentifierInfo(id);
            var idNodes = xPathObj.selectNodes("//result/doc[1]/arr[@name='bibIdentifier']/str");
            if((idNodes != "" || idNodes != null) && idNodes.length > 0 )  {
                var bibId = idNodes[0].text;
                getInfo(bibId);
                var titleToDisp = xPathObj.selectNodes("//result/doc[1]/arr[@name='Title_display']/str");
                var title;
                if(titleToDisp != null && titleToDisp.length > 0) {
                    title= titleToDisp[0].text;
                }
                srHtmlText1[i++] = "<dt>Title:</dt><dd>" + "<a href='discovery.do?searchType=linksearch&linkValue=" + bibId + "'><b> "
                        + title + " </b></a>";
                if (title == "" || title == null) {
                    srHtmlText1[i++] = "<br/>";
                }
                var isbnToDisp = xPathObj.selectNodes("//result/doc[1]/arr[@name='ISBN_display']/str");
                var isbn;
                if(isbnToDisp != null && isbnToDisp.length > 0) {
                    isbn = isbnToDisp[0].text;
                }
                srHtmlText1[i++] = "<dt>ISBN:</dt><dd>"  + isbn+ " </b></a>";
                if (isbn == "" || isbn == null) {
                    srHtmlText1[i++] = "<br/>";
                }

                var publisherToDisp = xPathObj.selectNodes("//result/doc[1]/arr[@name='Publisher_display']/str");
                var publisher;
                if(publisherToDisp != null && publisherToDisp.length > 0) {
                    publisher = publisherToDisp[0].text;
                }
                srHtmlText1[i++] = "<dt>Publisher:</dt><dd>"  + publisher+ " </b></a>";
                if (publisher == "" || publisher == null) {
                    srHtmlText1[i++] = "<br/>";
                }

            }

            srHtmlText1[i++] = "<dt>Local Identifier:</dt><dd>" + getHighlightedText(id, localId, 'LocalId_display') + "</dd>"; + "</dd>";
            if (localId == "" || localId == null) {
                srHtmlText1[i++] = "<br/>";
            }

            srHtmlText1[i++] = "<dt>Access status:</dt><dd>" + getHighlightedText(id, accessStatus ,'AccessStatus_search') + "</dd>";
            if (accessStatus == "" || accessStatus == null) {
                srHtmlText1[i++] = "<br/>";
            }

            srHtmlText1[i++] = "<dt>Platform:</dt><dd>" + getHighlightedText(id, platForm,
                    'PlatForm_search') + "</dd>";
            if (platForm == "" || platForm == null) {
                srHtmlText1[i++] = "<br/>";
            }

            srHtmlText1[i++] = "<dt>Imprint:</dt><dd>" + getHighlightedText(id, imprint, 'Imprint_search') + "</dd>";
            if (imprint == "" || imprint == null) {
                srHtmlText1[i++] = "<br/>";
            }

            srHtmlText1[i++] = "<dt>Statistical code:</dt><dd>" + getHighlightedText(id, statisticalCode, 'StatisticalSearchingCodeValue_search') + "</dd>";
            if (statisticalCode == "" || statisticalCode == null) {
                srHtmlText1[i++] = "<br/>";
            }

        }
//        srHtmlText1[i++] = "<dt>Doc Type:</dt><dd>" + docType + "</dd>";
//        srHtmlText1[i++] = "<dt>Doc Format:</dt><dd>" + docFormat + "</dd>";
        srHtmlText1[i++] = "</dl></div>";
        srHtmlText1[i++] = "<div>";
        var linksHtml = getLinksHtml(docIndex, uuid, docType, docFormat);
        for (var k = 0; k < linksHtml.length; k++) {
            srHtmlText1[i++] = linksHtml[k];
        }
        srHtmlText1[i++] = "</div>";
        count++;
        srHtmlText1[i++] = "</p></div>";
    }
    $("#demo2").html(srHtmlText1.join(''));

}

function getMarcTitleValue(resultsXPathObj, docIndex) {
    var title = "";
    var titleNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1) + "]/arr[@name='245a']/str");
    if ((titleNodes != null || titleNodes != "") && titleNodes.length > 0) {
        title = titleNodes[0].text;
    }
    titleNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1) + "]/arr[@name='245b']/str");
    if ((titleNodes != null || titleNodes != "") && titleNodes.length > 0) {
        title = title + " " + titleNodes[0].text;
    }
    return title;
}

function getMarcAuthorValue(resultsXPathObj, docIndex) {

    var author = "";
    var authorNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1) + "]/arr[@name='100a']/str");
    if ((authorNodes != null || authorNodes != "") && authorNodes.length > 0) {
        author = authorNodes[0].text;
    }
    if (author == "") {
        authorNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1) + "]/arr[@name='110a']/str");
        if ((authorNodes != null || authorNodes != "") && authorNodes.length > 0) {
            author = authorNodes[0].text;
        }
    }
    if (author == "") {
        authorNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1) + "]/arr[@name='111a']/str");
        if ((authorNodes != null || authorNodes != "") && authorNodes.length > 0) {
            author = authorNodes[0].text;
        }
    }
    if (author == "") {
        authorNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1) + "]/arr[@name='700a']/str");
        if ((authorNodes != null || authorNodes != "") && authorNodes.length > 0) {
            author = authorNodes[0].text;
        }
    }
    if (author == "") {
        authorNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1) + "]/arr[@name='710a']/str");
        if ((authorNodes != null || authorNodes != "") && authorNodes.length > 0) {
            author = authorNodes[0].text;
        }
    }
    if (author == "") {
        authorNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1) + "]/arr[@name='800a']/str");
        if ((authorNodes != null || authorNodes != "") && authorNodes.length > 0) {
            author = authorNodes[0].text;
        }
    }
    if (author == "") {
        authorNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1) + "]/arr[@name='810a']/str");
        if ((authorNodes != null || authorNodes != "") && authorNodes.length > 0) {
            author = authorNodes[0].text;
        }
    }
    if (author == "") {
        authorNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1) + "]/arr[@name='811a']/str");
        if ((authorNodes != null || authorNodes != "") && authorNodes.length > 0) {
            author = authorNodes[0].text;
        }
    }
    if (author == "") {
        authorNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1) + "]/arr[@name='400a']/str");
        if ((authorNodes != null || authorNodes != "") && authorNodes.length > 0) {
            author = authorNodes[0].text;
        }
    }
    if (author == "") {
        authorNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1) + "]/arr[@name='410a']/str");
        if ((authorNodes != null || authorNodes != "") && authorNodes.length > 0) {
            author = authorNodes[0].text;
        }
    }
    if (author == "") {
        authorNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1) + "]/arr[@name='411a']/str");
        if ((authorNodes != null || authorNodes != "") && authorNodes.length > 0) {
            author = authorNodes[0].text;
        }
    }
    return author;
}

function getMarcSubjectValue(resultsXPathObj, docIndex) {

    var subject = "";
    var subjectNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1) + "]/arr[@name='600a']/str");
    if ((subjectNodes != null || subjectNodes != "") && subjectNodes.length > 0) {
        subject = subjectNodes[0].text;
    }
    if (subject == "") {
        subjectNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1) + "]/arr[@name='610a']/str");
        if ((subjectNodes != null || subjectNodes != "") && subjectNodes.length > 0) {
            subject = subjectNodes[0].text;
        }
    }
    if (subject == "") {
        subjectNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1) + "]/arr[@name='611a']/str");
        if ((subjectNodes != null || subjectNodes != "") && subjectNodes.length > 0) {
            subject = subjectNodes[0].text;
        }
    }
    if (subject == "") {
        subjectNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1) + "]/arr[@name='630a']/str");
        if ((subjectNodes != null || subjectNodes != "") && subjectNodes.length > 0) {
            subject = subjectNodes[0].text;
        }
    }
    if (subject == "") {
        subjectNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1) + "]/arr[@name='650a']/str");
        if ((subjectNodes != null || subjectNodes != "") && subjectNodes.length > 0) {
            subject = subjectNodes[0].text;
        }
    }
    if (subject == "") {
        subjectNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1) + "]/arr[@name='651a']/str");
        if ((subjectNodes != null || subjectNodes != "") && subjectNodes.length > 0) {
            subject = subjectNodes[0].text;
        }
    }
    if (subject == "") {
        subjectNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1) + "]/arr[@name='653a']/str");
        if ((subjectNodes != null || subjectNodes != "") && subjectNodes.length > 0) {
            subject = subjectNodes[0].text;
        }
    }

    return subject;
}

function getMarcPublisherValue(resultsXPathObj, docIndex) {

    var publisher = "";
    var publisherNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1) + "]/arr[@name='260b']/str");
    if ((publisherNodes != null || publisherNodes != "") && publisherNodes.length > 0) {
        publisher = publisherNodes[0].text;
    }

    return publisher;
}

function getMarcDescriptionValue(resultsXPathObj, docIndex) {

    var description = "";
    var descriptionNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1) + "]/arr[@name='505a']/str");
    if ((descriptionNodes != null || descriptionNodes != "") && descriptionNodes.length > 0) {
        description = descriptionNodes[0].text;
    }
    return  description;
}

function getMarcLocationValue(resultsXPathObj, docIndex) {

    var location = "";
    var locationNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1) + "]/arr[@name='856u']/str");
    if ((locationNodes != null || locationNodes != "") && locationNodes.length > 0) {
        location = locationNodes[0].text;
    }

    return location;
}

function getHighlightedText(id, displayFieldText, searchFieldName) {

    var resultText = displayFieldText;
    var spanText = "<span style=\"background-color:yellow\">";
    //var id=$(docXml).find("str[name='id']").text();
    //var id = docXPathObj.selectNodes("//str[@name='id']").text;
    var filterKey = "//lst[@name='highlighting']/lst[@name='" + id + "']/arr[@name='" + searchFieldName + "']/str";
    //var highlightedFieldText=$(resultsXml).find(filterKey).text();
    var highlightedFieldText = "";
    var highlightedNodes = resultsXPathObj.selectNodes(filterKey);
    if ((highlightedNodes != null) && (highlightedNodes.length > 0)) {
        highlightedFieldText = resultsXPathObj.selectNodes(filterKey)[0].text;
    }
    var indexText = highlightedFieldText.indexOf(spanText);
    if (indexText >= 0) {
        indexText = indexText + spanText.length;
        var highlightedWord = highlightedFieldText.substring(indexText, highlightedFieldText.indexOf("</span>"));
        var highlighText = spanText + highlightedWord + "</span>";
        //resultText=fieldText.replace(highlightedWord, highlighText);
        resultText = resultText.replace(highlightedWord, highlighText);
    }
    return resultText;
}


//jQuery initialization code:
$(function() {
    // Setup accordion layout for facets.
    $("#searchFacetsDiv").live("setAccordion", function() {

        $('#searchFacetsDiv').multiOpenAccordion({
            active: [0, 1, 2, 3, 4],
            click: function(event, ui) {
                //console.log('clicked')
            },
            init: function(event, ui) {
                //console.log('whoooooha')
            },
            tabShown: function(event, ui) {
                //console.log('shown')
            },
            tabHidden: function(event, ui) {
                //console.log('hidden')
            }

        });
        $('#searchFacetsDiv').multiOpenAccordion("option", "active", [0, 1, 2, 3, 4, 5]);
    });
    $("#searchFacetsDiv").trigger("setAccordion");

    // Setup accordion layout for search results.
    $("#searchResultsDiv").live("setAccordion", function() {

        $('#searchResultsDiv').multiOpenAccordion({
            active: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
            click: function(event, ui) {
                //console.log('clicked')
            },
            init: function(event, ui) {
                //console.log('whoooooha')
            },
            tabShown: function(event, ui) {
                //console.log('shown')
            },
            tabHidden: function(event, ui) {
                //console.log('hidden')
            }

        });
        $('#searchResultsDiv').multiOpenAccordion("option", "active", [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]);
    });
    $("#searchResultsDiv").trigger("setAccordion");

    // Setup pagination for search results.
    //var numfound = parseInt($(resultsXml).find("result").attr("numFound"));
    var numfound = 0;
    var nodes = resultsXPathObj.selectNodes("//result/@numFound");
    if (nodes.length > 0) {
        numfound = nodes[0].text;
    }
    //var resultFromIndex = parseInt($(resultsXml).find("result").attr("start"))+ 1;
    var resultFromIndex = "";
    var resultIndex = resultsXPathObj.selectNodes("//result/@start");
    if (resultIndex != null && resultIndex.length > 0) {
        resultFromIndex = parseInt(resultIndex[0].text) + 1;
    }
    var i = parseInt(numfound) / parseInt(resultPageSize);
    var maxPage = Math.ceil(i);
    if (numfound > parseInt(resultPageSize)) {
        /*
         $("#paginationDiv1").paginate({
         count 		: maxPage,
         start 		: resultPageIndex,
         display     : 10,
         border					: true,
         border_color			: '#000',
         text_color  			: 'black',
         background_color    	: 'darkgray',
         border_hover_color		: '#ccc',
         text_hover_color  		: '#000',
         background_hover_color	: '#fff',
         images					: false,
         mouse					: 'press',
         onChange     			: function(page){
         startLoadBusy();
         document.getElementById("resultPageIndex").value = page;
         var select_index = $("#recordsperpage").val();
         document.getElementById("resultPageSize").value = select_index;
         document.getElementById("resultFromIndex").value = parseInt($(resultsXml).find("result").attr("start"));
         var sort_value = $("#sortingorder").val();
         document.getElementById("sortByTerms").value = sort_value;
         document.getElementById("action").value = "goToPage";
         document.forms["discoveryForm"].submit();

         }
         });
         */
        $("#paginationDiv").slider({
            //range: "min",
            value: resultPageIndex,
            min: 1,
            max: maxPage,
            slide: function(event, ui) {
                $("#selectedPage").val(ui.value);
                $("#pageSpinner").val(ui.value);
            }
        });
        $("#selectedPage").val($("#paginationDiv").slider("value"));

        $('#pageSpinner').spinner({min: 1, max: maxPage, step: 1, increment: 'fast', showOn: 'both'});
        $("#pageSpinner").val(resultPageIndex);
    }
    else {
        $('#pageTag').hide();
    }
});
$(document).ready(function() {
    $('#recordsperpage').val(resultPageSize);
    $("#recordsperpage").change(function() {
        startLoadBusy();
        var select_index = $("#recordsperpage").val();
        document.getElementById("resultPageSize").value = select_index;
        //document.getElementById("resultFromIndex").value = parseInt($(resultsXml).find("result").attr("start"));
        document.getElementById("resultFromIndex").value = parseInt(resultsXPathObj.selectNodes("//result/@start")[0]
                .text);
        document.getElementById("resultPageIndex").value = resultPageIndex;
        var sort_value = $("#sortingorder").val();
        document.getElementById("sortByTerms").value = sort_value;
        document.getElementById("action").value = "setPageSize";
        document.forms["discoveryForm"].submit();
    });
    $('#sortingorder').val(sortByTerms);
    $("#sortingorder").change(function() {
        startLoadBusy();
        var sort_value = $("#sortingorder").val();
        document.getElementById("sortByTerms").value = sort_value;
        var select_index = $("#recordsperpage").val();
        document.getElementById("resultPageSize").value = select_index;
        //document.getElementById("resultFromIndex").value = parseInt($(resultsXml).find("result").attr("start"));
        document.getElementById("resultFromIndex").value = parseInt(resultsXPathObj.selectNodes("//result/@start")[0]
                .text);
        document.getElementById("resultPageIndex").value = resultPageIndex;
        document.getElementById("action").value = "sorting";
        document.forms["discoveryForm"].submit();

    });
});

function goToPage() {
    startLoadBusy();
    document.getElementById("resultPageIndex").value = pageSpinner.value;
    var select_index = $("#recordsperpage").val();
    document.getElementById("resultPageSize").value = select_index;
    document.getElementById("resultFromIndex").value = parseInt(resultsXPathObj.selectNodes("//result/@start")[0].text);
    var sort_value = $("#sortingorder").val();
    document.getElementById("sortByTerms").value = sort_value;
    document.getElementById("action").value = "goToPage";
    document.forms["discoveryForm"].submit();
}

</script>
<script type="text/javascript">
    <!--//--><![CDATA[//><!--
    $(function() {
        $("#demo1 h3.expand").toggler();
        $("#demo2 h3.expand").toggler({initShow: "div.collapse:eq(0)"});

        $("#demo2").expandAll({
            trigger: "h3.expand",
            ref: "h3.expand",
            showMethod: "slideDown",
            hideMethod: "slideUp",
            oneSwitch : false,
            state:'shown'
        });
    });
    displayResults();
    stopLoadBusy();
    var EndTime = new Date();
    //    log.debug("EndTime" + EndTime);
    var TimeDiff = EndTime - startTime;
    log.debug("TimeDiff........" + TimeDiff);
    $("#searchTime").html(searchTime + " ms");
    <%
       long endTime = System.currentTimeMillis();
       String jspTime = String.valueOf(endTime - startTime);
     %>
    var jspTime = '<%=jspTime%>';
    $("#jspTime").html(jspTime + " ms");
    $("#displayTime").html(TimeDiff + " ms");

    //--><!]]>
</script>
</html>
