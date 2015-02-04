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
         import="org.kuali.ole.docstore.discovery.*" pageEncoding="UTF-8"%>
<%@ page import="org.kuali.ole.docstore.discovery.web.struts.form.DiscoveryForm" %>
<jsp:useBean id="discoveryForm" class="org.kuali.ole.docstore.discovery.web.struts.form.DiscoveryForm" scope="session"/>

<%@ include file="/kr/WEB-INF/jsp/tldHeader.jsp"%>
<%
    long startTime = System.currentTimeMillis();
%>
<script type="text/javascript" src="script/jquery/jquery-1.4.3.min.js"></script>
<script type="text/javascript" src="script/jquery-ui-1.8.16.custom/js/jquery-ui-1.8.16.custom.min.js"></script>
<link type="text/css" rel="Stylesheet" href="script/jquery-ui-1.8.16.custom/css/smoothness/jquery-ui-1.8.16.custom.css" />
<script type="text/javascript" src="script/multi-open-accordion/jquery.multi-open-accordion-1.5.3.min.js"></script>
<script type="text/javascript" src="script/jquery-ui-spinner/ui.spinner.js"></script>
<link type="text/css" href="script/jquery-ui-spinner/ui.spinner.css" rel="stylesheet" media="screen"/>
<script type="text/javascript" src="script/multi-expand/expand.js"></script>
<link type="text/css" href="script/multi-expand/css/multi-expand.css" rel="stylesheet" />
<link type="text/css" rel="stylesheet" href="css/discovery.css" />
<script type="text/javascript" src="scripts/ole/blackbird.js"></script>
<link type="text/css" rel="Stylesheet" href="css/blackbird.css" />
<script type="text/javascript" src="script/SoftXpath.js"></script>

<div id="headerarea1" class="headerarea1">
    <h1> Document Store Discovery</h1>
</div>
<textarea id="searchQuery" rows="5" cols="80" style="display:none">
    <c:out value="${discoveryForm.searchQuery}" />
</textarea>
<textarea id="resultsXmlLength" rows="5" cols="80" style="display:none">
    <%=discoveryForm.getSearchResult().length()%>
</textarea>
<textarea id="resultsXml" rows="5" cols="80" style="display:none"><c:out value="${discoveryForm.searchResult}" /></textarea>
<div id="searchResultsBusyDiv" align="left" style="height:99.5%;width:99%;position:absolute;z-index:1;opacity:0.5;background-color:lightgray;color:blue;">
    <b>	Loading search results. Please wait...</b>
    <img src="images-portal/busyImage.gif">
</div>
<script>
    function submitSearch(searchType) {
        var form = document.forms["discoveryForm"];
        if (searchType != null) {
            document.getElementById("searchType").value = searchType;
        }
        form.submit();
    }
    function getSearchResultXMLContent(uuid) {
        // Build the docstore url for the xml file content corresponding to the search result.
        var docFormat=uuid.substr(37,uuid.length);
        uuid=uuid.substring(0,36);
        var url = '<c:out value="${discoveryForm.docStoreURL}"/>';//"http://localhost:9080/documentstore-webapp/document"; //?uuid=5b45986a-ea8f-425e-896c-5a3b8ce4caab";
        var finalurl = url + "?uuid=" + uuid+"&docFormat="+docFormat;
        window.open(finalurl);
    }
    function editXMLContent(uuid) {
        var uuidValue=uuid;
        var url = '<c:url value="${discoveryForm.bibEditorURL}"/>';
        url = url + "&docType=marc&action=edit&uuid=" + uuid+"&__login_user=admin";
        window.open(url);
    }
    function getLinksHtml(docIndex, uuid, docType, docFormat) {

        var srHtmlText1 = [];
        var i = 0;
        var k = 1;

        var getButtonhtml = '<input type="button" name="Get" value=" View " onClick="getSearchResultXMLContent(\''  + uuid +'' + '-'+docFormat +'\')"> &nbsp;';
        var getEditButtonhtml='<input type="button" name="Edit" value="Edit  XML" onClick="editXMLContent(\''+uuid+'\')">';
        if (docFormat == 'marc'  )
            srHtmlText1[i++] = getButtonhtml+getEditButtonhtml;
        else
            srHtmlText1[i++] = getButtonhtml;
        srHtmlText1[i++] = "&nbsp;&nbsp;&nbsp;";
        var bib = "";
        var bibNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='bibIdentifier'] /str");
        if ((bibNodes != "" || bibNodes != null) && bibNodes.length > 0) {
            for (var j = 0; j < bibNodes.length; j++) {
                bib = bibNodes[j].text;

                srHtmlText1[i++] = "<a href='discovery.do?searchType=linksearch&linkValue=" + bib
                        + "'><b>Bib-" + parseInt(j + 1) + "</b></a>";
                srHtmlText1[i++] = "&nbsp;&nbsp;";
                if (k % 8 == 0) {
                    srHtmlText1[i++] = "<br/>";
                    for (var a = 0; a < 22; a++) {
                        srHtmlText1[i++] = "&nbsp;";
                    }
                }
                k++;
            }
        }
        if ((docType == "bibliographic") ) {
            var instances = "";
            var instanceNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                    + "]/arr[@name='instanceIdentifier']/str");
            if ((instanceNodes != "" || instanceNodes != null) && instanceNodes.length > 0) {
                for (var j = 0; j < instanceNodes.length; j++) {
                    instances = instanceNodes[j].text;
                    srHtmlText1[i++] = "<a href='discovery.do?searchType=linksearch&linkValue=" + instances
                            + "'><b>Instance-" + parseInt(j + 1) + "</b></a>";
                    srHtmlText1[i++] = "&nbsp;&nbsp;";
                    if (k % 8 == 0) {
                        srHtmlText1[i++] = "<br/>";
                        for (var a = 0; a < 22; a++) {
                            srHtmlText1[i++] = "&nbsp;";
                        }
                    }
                    k++;
                }
            }
        }
        else if ((docType == "holding") || (docType == "item")) {

            var instances = "";
            var instanceNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                    + "]/arr[@name='instanceIdentifier']/str");
            if ((instanceNodes != "" || instanceNodes != null) && instanceNodes.length > 0) {
                for (var j = 0; j < instanceNodes.length; j++) {
                    instances = instanceNodes[j].text;

                    srHtmlText1[i++] = "<a href='discovery.do?searchType=linksearch&linkValue=" + instances
                            + "'><b>Instance</b></a>";
                    srHtmlText1[i++] = "&nbsp;&nbsp;";
                    if (k % 8 == 0) {
                        srHtmlText1[i++] = "<br/>";
                        for (var a = 0; a < 22; a++) {
                            srHtmlText1[i++] = "&nbsp;";
                        }
                    }
                    k++;
                }

            }
        }

        var holdings = "";
        var holdingNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='holdingsIdentifier']/str");
        if ((holdingNodes != "" || holdingNodes != null) && holdingNodes.length > 0) {

            for (var j = 0; j < holdingNodes.length; j++) {
                holdings = holdingNodes[j].text;

                srHtmlText1[i++] = "<a href='discovery.do?searchType=linksearch&linkValue=" + holdings
                        + "'><b>Holdings</b></a>";
                srHtmlText1[i++] = "&nbsp;&nbsp;";
                if (k % 8 == 0) {
                    srHtmlText1[i++] = "<br/>";
                    for (var a = 0; a < 22; a++) {
                        srHtmlText1[i++] = "&nbsp;";
                    }
                }
                k++;
            }
        }
        var items = "";
        var itemNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='itemIdentifier']/str");
        if ((itemNodes != "" || itemNodes != null) && itemNodes.length > 0) {
            for (var j = 0; j < itemNodes.length; j++) {
                items = itemNodes[j].text;
                srHtmlText1[i++] = "<a href='discovery.do?searchType=linksearch&linkValue=" + items + "'><b>Item-"
                        + parseInt(j + 1) + "</b></a>";
                srHtmlText1[i++] = "&nbsp;&nbsp;";
                if (k % 8 == 0) {
                    srHtmlText1[i++] = "<br/>";
                    for (var a = 0; a < 22; a++) {
                        srHtmlText1[i++] = "&nbsp;";
                    }
                }
                k++;
            }
        }
        return srHtmlText1;
    }
</script>
<table>
    <tr>
        <td valign="top">
            <html:form action="discovery.do" method="post">
                <div align="left" class="backButton">
                    <input type="hidden" name="searchType" id="searchType" value="">
                    <input type="button" name="Back" id="Back" value="Revise Search" onclick="submitSearch('backToSearch')">
                    <input type="button" name="newSearch" value="New Search" onclick="submitSearch('newSearch')">
                    <input type="hidden" name="resultPageSize" id="resultPageSize" value="${discoveryForm.resultPageSize}">
                    <input type="hidden" name="resultFromIndex" id="resultFromIndex">
                    <input type="hidden" name="resultPageIndex" id="resultPageIndex" >
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
                            <input type="text" id="pageSpinner" size="9" value="" style="color:#f6931f; font-weight:bold;"/>
                            <input type="button" id="gotoPage" value="Go to page" onclick="goToPage()"/>
                        </p>
                        <div id="paginationDiv" class="paginationDiv" title="Slide or click on the bar to select a page. Or click and use arrow keys.">
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
                    <td colspan="2"  width="100%">
                        <div id="demo2" class="demo">
                        </div>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<script type="text/javascript">
var resultPageSize = "${discoveryForm.resultPageSize}";
var resultPageIndex = "${discoveryForm.resultPageIndex}";
var searchTerms = '${discoveryForm.searchTerms}';
var sortByTerms = "${discoveryForm.sortByTerms}";
var searchCategory = "${discoveryForm.docCategory}";
var searchType =  "${discoveryForm.docType}";
var searchFormat =  "${discoveryForm.docFormat}";
var searchTime =  "${discoveryForm.searchTime}";
var languagesInfoXml=null;
function startLoadBusy(){
    $("#searchResultsBusyDiv").show();
    $("#searchResultsBusyDiv").css("z-index","1");
}
function stopLoadBusy(){
    $("#searchResultsBusyDiv").hide();
    $("#searchResultsBusyDiv").css("z-index","-1");
}
startLoadBusy();
var startTime=new Date();
log.debug("startTime........"+startTime);
log.debug("startTime"+startTime);
var resultsXml = "";
var resultsXPathObj = new SoftXpath();
function displayResults() {
    resultsXml = document.getElementById("resultsXml").value;
    if (resultsXml.length <= 0){
        var srHtmlText = [];
        var k=0;
        $("#pageTag").hide();
        $("#limits").hide();
        $("#sortRecords").hide();
        $("#searchCrieteria").append("<b> DocCategory - </b>"+searchCategory+"<b> DocType - </b>"+searchType+"<b> DocFormat - </b>"+searchFormat+"</br>");
        srHtmlText[k++]="<b>No search results found.</b><br/><br/>";
        $("#demo2").html(srHtmlText.join(''));
    }
    else{
        resultsXPathObj.registerNamespace('','');
        if(!resultsXPathObj.loadXML(resultsXml)){
            alert('Loading Results xml failed!!!');
        }
        processSearchResultXml();
    }
}
function displayResults1() {
    $.get( "sampleSearchResult.xml",
            function( data ) {
                alert("data displayResults1"+data)
                resultsXml = data;
                $("p#status").text( "Loaded." );
                processSearchResultXml();
            } );
}

function getLanguageName(languageCode){
    var languageName=null;

    if(languageCode.length == 3){
        var filterQuery = '//lang[@code=\''+languageCode+'\']';
        log.debug("filterQuery "+filterQuery);
        var languageNodes = languageXPathObj.selectNodes(filterQuery);
        if ((languageNodes != null) && (languageNodes.length > 0)) {
            languageName=languageNodes[0].text;
        }
    }
    else{
        languageName=languageCode;
    }

    if(languageName==null){
        languageName=languageCode;
    }

    return languageName;
}
function isFacetSelected(termsList,facetValue){

    var flag=false;
    for(y=0;y<termsList.length;y++){
        if(termsList[y]==facetValue){
            flag= true;
            break;
        }
    }
    return flag;

}
var languageXPathObj = new SoftXpath();
languageXPathObj.registerNamespace('','');
if(!languageXPathObj.load("languages2.xml")){
    alert('Loading Languages xml failed!!!');
}
var childWindowId;
function getFacetsContent(fieldName){
    var moreFacet = name;
    var url = "discovery.do?action=moreFacets&fieldName="+fieldName;
    document.getElementById("selectedFacetName").value = fieldName;
    childWindowId = window.open(
            url,
            'childWindowId',
            'height=400,width=600,toolbar=1,location=1,menubar=1,status=0,scrollbars=1,resizable=1,replace=1');
    setTimeout("SendToChild('" + fieldName + "')",1000);
}
function SendToChild(fieldName){

    childWindowId.document.moreFacets.fieldName.value = fieldName;
    childWindowId.document.moreFacets.resultsXml.value = resultsXml;
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
    var numfound = 0;
    nodes = resultsXPathObj.selectNodes("//result/@numFound");
    if(nodes.length > 0){
        numfound = nodes[0].text;
    }
    //var resultFromIndex = parseInt(resultsXPathObj.selectNodes("//result/@start")[0].text)+1;
    var resultFromIndex = "";
    var resultIndex = resultsXPathObj.selectNodes("//result/@start");
    if(resultIndex != null && resultIndex.length > 0){
        resultFromIndex = parseInt(resultIndex[0].text)+1;
    }
    var terms = "";
    var termsNodes = resultsXPathObj.selectNodes("//lst[@name='responseHeader']//str[@name='terms']");
    if((termsNodes != null || termsNodes != "") && termsNodes.length > 0){
        terms = termsNodes[0].text;
    }
    var termsList="";
    if(terms.length>0){
        termsList=terms.split("|");
    }
    var count = 0;
    var maxCount = 5;
    var counter = 0;
    facetHtmlText[i++]="&nbsp;<p class='sample'><b><i>Limit Your Search...</i></b></p>";
    var facetFieldNodeNameList = resultsXPathObj.selectNodes("//lst[@name='facet_fields']/lst/@name");
    for (var docFacet = 0; docFacet < facetFieldNodeNameList.length; docFacet++) {
        var facetName = facetFieldNodeNameList[docFacet].text;
        var facetFieldNameXPath = "//lst[@name='facet_fields']/lst[@name='" + facetName + "']/int/@name";
        var facetFieldNamesList = resultsXPathObj.selectNodes(facetFieldNameXPath);
        count=resultsXPathObj.selectNodes("//lst[@name='facet_fields']/lst[@name='" + facetName + "']/int").length;
        counter=0;
        fieldNameTemp=facetName.substring(0,facetName.indexOf("_"));
        if(fieldNameTemp == "PublicationDate"){
            fieldNameTemp = "Publication Date";
        }
        facetHtmlText[i++] = "<h3><a href=\"#\">" + fieldNameTemp + "</a></h3><div><ul>";
        for (var j = 0; j < count; j++) {
            var facetFieldName = "";
            var facetFieldNameNodes = resultsXPathObj.selectNodes("//lst[@name='facet_fields']/lst[@name='" + facetName + "']/int[" + (j+1) + "]/@name");
            if( (facetFieldNameNodes != null || facetFieldNameNodes != "")&&(facetFieldNameNodes.length > 0)){
                facetFieldName = facetFieldNameNodes[0].text;
            }
            var facetFieldValue = resultsXPathObj.selectNodes("//lst[@name='facet_fields']/lst[@name='" + facetName + "']/int[@name='" + facetFieldName + "']");
            var values = "";
            if( (facetFieldValue !=null || facetFieldValue != "") && facetFieldValue.length > 0) {
                values = facetFieldValue[0].text;
            }
            counter++;
            if(counter <= maxCount){
                if((facetFieldName==null||facetFieldName==""||facetFieldName=="N/A")||(getLanguageName(facetFieldName)==null||getLanguageName(facetFieldName)=="") ){
                    counter--;
                }
                else {
                    if(isFacetSelected(termsList,facetFieldName)){
                        if(fieldNameTemp=="Publication Date"){
                            facetHtmlText[i++] = "<li>" + getDisplayName(facetFieldName) + "&nbsp;(" + values + ")</li>";
                        }
                        else {
                            facetHtmlText[i++] = "<li>" + getDisplayName(getLanguageName(facetFieldName)) + "&nbsp;(" + values
                                    + ")</li>";
                        }
                    }
                    else {
                        if(fieldNameTemp=="Publication Date"){
                            facetHtmlText[i++] = "<li><a href='discovery.do?searchType=facetSearch&facetValue="
                                    + encodeURIComponent(facetFieldName) + "&facetFieldValue=" + facetName + "'>"
                                    + getDisplayName(facetFieldName) + "&nbsp;(" + values + ")</a></li>";
                        }
                        else {
                            //facetHtmlText[i++] = "<li><a href='discovery.do?searchType=facetSearch&facetValue="+facetFieldName+"&facetFieldValue="+facetName+"'>" + getLanguageName(facetFieldName) + "&nbsp;(" + values + ")</a></li>";
                            var facetFieldName1 =facetFieldName;
                            facetFieldName1 = facetFieldName1.replace(/\\/g,"\\\\");
                            facetFieldName1 = facetFieldName1.replace(/\'/g,"\\\'");
                            facetFieldName1 = facetFieldName1.replace(/\"/g,"\\\"");
                            var temp = "<li><a href='discovery.do?searchType=facetSearch&facetValue=" + encodeURIComponent(facetFieldName1) + "&facetFieldValue=" + facetName + "'>" + getDisplayName(getLanguageName(facetFieldName)) + "&nbsp;(" + values + ")</a></li>";
                            temp = temp.replace(/%5C'/g,"%5C%27");
                            facetHtmlText[i++] = temp;
                        }
                    }
                }
            }
        }
        facetHtmlText[i++] = "</ul>";
        if(count > maxCount){
            log.debug("more...");
            facetHtmlText[i++] = '<div align="right"><a href="javascript:getFacetsContent(\''+facetName+'\')" class="moreLink">more...</a></div>';
        }
        facetHtmlText[i++] = "</div>";
    }
    $("#searchFacetsDiv").html(facetHtmlText.join(''));
    $("#searchFacetsDiv").trigger("setAccordion");
    var srHtmlText1 = [];
    var j = 0;
    var count=resultFromIndex;
    var resultToIndex = parseInt(resultFromIndex)+parseInt(resultPageSize)-1;
    if(numfound < parseInt(resultToIndex)){
        resultToIndex = numfound;
    }
    termsList=terms.split("|");
    if(searchTerms==null||searchTerms==""){
        $("#searchCrieteria").append("<b> DocCategory - </b>"+searchCategory+"<b> DocType - </b>"+searchType+"<b> DocFormat - </b>"+searchFormat);
    }
    else{
        $("#searchCrieteria").append("<b>DocCategory - </b>"+searchCategory+"<b> DocType - </b>"+searchType+"<b> DocFormat - </b>"+searchFormat+"<br/>");
        $("#searchCrieteria").append("&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;&nbsp;<b>SearchTerms-</b>"+searchTerms );
    }
    if(numfound==0){
        srHtmlText1[i++]="<b>No search results found.</b><br/><br/>";
        $("#pageTag").hide();
    }
    for(y=0;y<termsList.length;y++){
        if(termsList[y]==null||termsList[y]==""){
            $("#limits").append("(No limits applied)");
        }
        else{
            //$("#limits").append(getLanguageName(termsList[y])+"<a href='discovery.do?searchType=facetDelete&facetValue="+termsList[y]+"'><b><img border='0' title='Remove' width='15' height='15' alt='(X)' src='images-portal/closeFacets.png'></b></a>"+'&nbsp;');
            var temp = getLanguageName(termsList[y]);
            temp = temp.replace(/\\\\/g, "\\");
            temp = temp.replace(/\\\"/g, "\"");
            temp = temp.replace(/\\\'/g, "\'");
            var temp1 = "";
            temp1 = encodeURIComponent(termsList[y]);
            temp1 = temp1.replace(/%5C'/g, "%5C%27");
            $("#limits").append((getDisplayName(temp)) + "<a href='discovery.do?searchType=facetDelete&facetValue=" + temp1
                    + "'><b><img border='0' title='Remove' width='15' height='15' alt='(X)' src='images-portal/closeFacets.png'></b></a>"
                    + '&nbsp;');
        }
        if(y<termsList.length-1)
            srHtmlText1[i++]="&nbsp;";
    }
    if (numfound > 0) {
        $("#pageList").append("<b>"+resultFromIndex+"-"+resultToIndex +" of "+ numfound + " results</b>");
    }
    srHtmlText1[i++] = "";
    var resultDocs = resultsXPathObj.selectNodes("//result/doc");
    for (var docIndex = 0; docIndex < resultDocs.length; docIndex++) {
        uuid = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/str[@name='id']")[0].text;
        var docType = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/str[@name='DocType']")[0].text;
        var docFormat = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/str[@name='DocFormat']")[0].text;
        var resultDoc = resultDocs[i];
        var title = "";
        //title= getMarcTitleValue(resultsXPathObj, docIndex);
        if(title == null || title == "") {
            var titleNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='Title_display']/str");
            if( (titleNodes !=null || titleNodes != "") && titleNodes.length > 0) {
                title = titleNodes[0].text;
                title = title.replace(/</g,"&lt;");

            }
        }
        var id = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/str[@name='id']")[0].text;

        var publisher = "";
        //publisher = getMarcPublisherValue(resultsXPathObj, docIndex);
        if(publisher == null || publisher == "") {
            var publisherNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='Publisher_display']/str");
            if( (publisherNodes !=null || publisherNodes != "") && publisherNodes.length > 0) {
                publisher = publisherNodes[0].text;
                publisher = publisher.replace(/</g,"&lt;");

            }
        }
        var publicationDate="";
        var publicationDateNodes=resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='PublicationDate_display']/str");
        if( (publicationDateNodes !=null || publicationDateNodes != "") && publicationDateNodes.length > 0) {
            publicationDate = publicationDateNodes[0].text;
            publicationDate = publicationDate.replace(/</g,"&lt;");
        }
        var author = "";
        //author = getMarcAuthorValue(resultsXPathObj, docIndex);
        if(author == null || author == "") {
            var authorNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='Author_display']/str");
            if((authorNodes != null || authorNodes != "") && authorNodes.length > 0) {
                author = authorNodes[0].text;
                author = author.replace(/</g,"&lt;") ;
            }
        }

        var subject = "";
        //subject = getMarcSubjectValue(resultsXPathObj, docIndex);
        if(subject == null || subject == "") {
            var subjectNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='Subject_display']/str");
            if( (subjectNodes !=null || subjectNodes !="") && subjectNodes.length > 0) {
                subject = subjectNodes[0].text;
                subject = subject.replace(/</g,"&lt;");
            }
        }

        var description = "";
        //description = getMarcDescriptionValue(resultsXPathObj, docIndex);
        if(description == null || description == "") {
            var descriptionNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='Description_display']/str");
            if( (descriptionNodes !=null || descriptionNodes != "") && descriptionNodes.length > 0) {
                description = descriptionNodes[0].text;
                description = description.replace(/</g,"&lt;");
            }
        }
        var location = "";
        //location = getMarcLocationValue(resultsXPathObj, docIndex);
        if(location == null || location == "") {
            var locationNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='Location_display']/str");
            if( (locationNodes !=null || locationNodes != "") && locationNodes.length >0) {
                location = locationNodes[0].text;
                location = location.replace(/</g,"&lt;");
            }
        }
        var isbn = "";
        var isbnNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='ISBN_display']/str");
        if( (isbnNodes !=null || isbnNodes != "") && isbnNodes.length > 0 ) {
            isbn = isbnNodes[0].text;
            isbn = isbn.replace(/</g,"&lt;");
        }
        var issn = "";
        var issnNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='ISSN_display']/str");
        if( (issnNodes != null || issnNodes != "") && issnNodes.length > 0){
            issn = issnNodes[0].text;
            issn = issn.replace(/</g,"&lt;");
        }
        var format = "";
        var formatNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='Format_display']/str");
        if((formatNodes!=null || formatNodes != "")&& formatNodes.length > 0){
            format = formatNodes[0].text;
            format = format.replace(/</g,"&lt;");
        }

        //OLEML fields start here...
        //holdings
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
                + "]/arr[@name='CallNumberType_display']/str");
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
        var copyNumberNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='CopyNumber_display']/str");
        if ((copyNumberNodes != null || copyNumberNodes != "") && copyNumberNodes.length > 0) {
            copyNumber = copyNumberNodes[0].text;
        }

        var volumeNumber = "";
        var volumeNumberNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex + 1)
                + "]/arr[@name='VolumeNumber_display']/str");
        if ((volumeNumberNodes != null || volumeNumberNodes != "") && volumeNumberNodes.length > 0) {
            volumeNumber = volumeNumberNodes[0].text;
        }
        //instance

        var instanceSource="";
        var  instanceSourceNodes=resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='Source_display']/str");
        if((instanceSourceNodes!=null || instanceSourceNodes != "")&& instanceSourceNodes.length > 0){
            instanceSource = instanceSourceNodes[0].text;
        }

        //patron

        var recordNumber="";
        var  recordNumberNodes=resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='RecordNumber_display']/str");
        if((recordNumberNodes!=null || recordNumberNodes != "")&& recordNumberNodes.length > 0){
            recordNumber = recordNumberNodes[0].text;
        }

        var beginDate="";
        var  beginDateNodes=resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='BeginDate_display']/str");
        if((beginDateNodes!=null || beginDateNodes != "")&& beginDateNodes.length > 0){
            beginDate = beginDateNodes[0].text;
        }

        var name="";
        var  nameNodes=resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='Name_display']/str");
        if((nameNodes!=null || nameNodes != "")&& nameNodes.length > 0){
            name = nameNodes[0].text;
        }

        var barrowerType="";
        var  barrowerTypeNodes=resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='BorrowerType_display']/str");
        if((barrowerTypeNodes!=null || barrowerTypeNodes != "")&& barrowerTypeNodes.length > 0){
            barrowerType = barrowerTypeNodes[0].text;
        }

        var barCodeNumber="";
        var  barCodeNumberNodes=resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='BarCodeNumber_display']/str");
        if((barCodeNumberNodes!=null || barCodeNumberNodes != "")&& barCodeNumberNodes.length > 0){
            barCodeNumber = barCodeNumberNodes[0].text;
        }

        var barCodeStatus="";
        var  barCodeStatusNodes=resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='BarCodeStatus_display']/str");
        if((barCodeStatusNodes!=null || barCodeStatusNodes != "")&& barCodeStatusNodes.length > 0){
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

        //ends here..

        if(docFormat=='marc' || docFormat=='dublinunq' || docFormat=='dublin')
        {
            srHtmlText1[i++] = '<h3 class="expand">'+count+".&nbsp;" + getHighlightedText(id, title, 'Title_search') + '</h3>';
        }
        if(docFormat=='oleml')
        {
            if( docType=='patron')
            {
                srHtmlText1[i++] = '<h3 class="expand">'+count+".&nbsp;" + getHighlightedText(id, id, 'id') + '</h3>';
            }
            else
            {
                srHtmlText1[i++] = '<h3 class="expand">'+count+".&nbsp;" + getHighlightedText(id, id, 'id') + '</h3>';
            }
        }
        else if(docFormat == 'pdf' || docFormat == 'xslt' || docFormat == 'doc') {
            srHtmlText1[i++] = '<h3 class="expand">' + count + ".&nbsp;" + getHighlightedText(id, id, 'id')
                    + '</h3>';
        }
        srHtmlText1[i++] = '<div class="collapse"><p>';
        srHtmlText1[i++] = "<div class='newDiv'>";
        srHtmlText1[i++] = "<dl>";

        if(docType=='bibliographic')
        {
            srHtmlText1[i++] = "<dt>Author:</dt><dd>" + getHighlightedText(id, author, 'Author_search')+"</dd>";
            if(author == ""|| author == null){
                srHtmlText1[i++] ="<br/>";
            }
            if(docFormat=='dublin' || docFormat=='dublinunq')
            {
                srHtmlText1[i++] = "<dt>Publisher:</dt><dd>" + getHighlightedText(id, publisher, 'Publisher_search')+' '+publicationDate+"</dd>";
                if(publisher == ""|| publisher == null){
                    srHtmlText1[i++] ="<br/>";
                }
            }
            if(docFormat=='marc' || docFormat=='all' )
            {
                srHtmlText1[i++] = "<dt>Publisher:</dt><dd>" + getHighlightedText(id, publisher, 'Publisher_search')+"</dd>";
                if(publisher == ""|| publisher == null){
                    srHtmlText1[i++] ="<br/>";
                }

            }
            if(docFormat!='dublin') {
                srHtmlText1[i++] = "<dt>Description:</dt><dd>" + getHighlightedText(id, description, 'Description_search')+"</dd>";
                if(description == "" || description == null){
                    srHtmlText1[i++] ="<br/>";
                }
            }
            srHtmlText1[i++] = "<dt>Subject:</dt><dd>" + getHighlightedText(id, subject, 'Subject_search')+"</dd>";
            if(subject == "" || subject == null){
                srHtmlText1[i++] ="<br/>";
            }
            srHtmlText1[i++] = "<dt>Location:</dt><dd>" + getHighlightedText(id, location, 'Location_search')+"</dd>";
            if( location =="" || location == null){
                srHtmlText1[i++] ="<br/>";
            }
            if(isbn != null && isbn != '') {
                srHtmlText1[i++] = "<dt>ISBN: </dt><dd>" + getHighlightedText(id, isbn, 'ISBN_search') +"</dd>";
                if(isbn == "" || isbn ==null){
                    srHtmlText1[i++] ="<br/>";
                }
            }
            if(issn != null && issn != '') {
                srHtmlText1[i++] = "<dt>ISSN: </dt><dd>" + getHighlightedText(id, issn, 'ISSN_search')+"</dd>";
                if(issn == ""|| issn == null) {
                    srHtmlText1[i++] ="<br/>";
                }
            }
            srHtmlText1[i++] = "<dt>Format:</dt><dd>" + getHighlightedText(id, format, 'Format_search')+"</dd>";
            if(format == "" || format == null){
                srHtmlText1[i++] ="<br/>";
            }

        }
        else if (docType == 'holdings') {
            srHtmlText1[i++] = "<dt>Holding URI:</dt><dd>" + getHighlightedText(id, uri, 'Uri_search')
                    + "</dd>";
            if (uri == "" || uri == null) {
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

            srHtmlText1[i++] = "<dt>Call Number:</dt><dd>" + getHighlightedText(id, callNumber,
                    'CallNumber_search')
                    + "</dd>";
            if (callNumber == "" || callNumber == null) {
                srHtmlText1[i++] = "<br/>";
            }

            srHtmlText1[i++] = "<dt>Call Number Type:</dt><dd>" + getHighlightedText(id, callNumberType,
                    'CallNumberType_search')
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

            srHtmlText1[i++] = "<dt>Classification Part:</dt><dd>" + getHighlightedText(id, classificationPart,
                    'ClassificationPart_search') + "</dd>";
            if (classificationPart == "" || classificationPart == null) {
                srHtmlText1[i++] = "<br/>";
            }

        }

        else if (docType == 'item') {
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
            srHtmlText1[i++] = "<dt>Vendorline id:</dt><dd>" + getHighlightedText(id, vendorLineItemIdentifier, 'VendorLineItemIdentifier_search')
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
            srHtmlText1[i++] = "<dt>Volume Number:</dt><dd>" + getHighlightedText(id, volumeNumber,
                    'VolumeNumber_search') + "</dd>";
            if (volumeNumber == "" || volumeNumber == null) {
                srHtmlText1[i++] = "<br/>";
            }
        }

        else if(docType=='instance')
        {
            srHtmlText1[i++] = "<dt>Source:</dt><dd>" + getHighlightedText(id, instanceSource, 'Source_search')+"</dd>";
            if(instanceSource == "" || instanceSource == null){
                srHtmlText1[i++] ="<br/>";
            }
        }
        else if(docType=='patron')

        {
            srHtmlText1[i++] = "<dt>Record Number:</dt><dd>" + getHighlightedText(id, recordNumber, 'RecordNumber_search')+"</dd>";
            if(recordNumber == "" || recordNumber == null){
                srHtmlText1[i++] ="<br/>";
            }
            srHtmlText1[i++] = "<dt>Begin Date:</dt><dd>" + getHighlightedText(id, beginDate, 'BeginDate_search')+"</dd>";
            if(beginDate == "" || beginDate == null){
                srHtmlText1[i++] ="<br/>";
            }
            srHtmlText1[i++] = "<dt>Name:</dt><dd>" + getHighlightedText(id, name, 'name_search')+"</dd>";
            if(name == "" || name == null){
                srHtmlText1[i++] ="<br/>";
            }
            srHtmlText1[i++] = "<dt>Barrower Type:</dt><dd>" + getHighlightedText(id, barrowerType, 'BarrowerType_search')+"</dd>";
            if(barrowerType == "" || barrowerType  == null){
                srHtmlText1[i++] ="<br/>";
            }
            srHtmlText1[i++] = "<dt>Barcode Number:</dt><dd>" + getHighlightedText(id, barCodeNumber, 'BarCodeNumber_search')+"</dd>";
            if(barCodeNumber == "" || barCodeNumber == null){
                srHtmlText1[i++] ="<br/>";
            }
            srHtmlText1[i++] = "<dt>Barcode Status:</dt><dd>" + getHighlightedText(id, barCodeStatus, 'BarCodeNumber_search')+"</dd>";
            if(barCodeStatus == "" || barCodeStatus == null){
                srHtmlText1[i++] ="<br/>";
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
        srHtmlText1[i++] = "<dt>Doc Type:</dt><dd>" + docType+"</dd>";
        srHtmlText1[i++] = "<dt>Doc Format:</dt><dd>" + docFormat+"</dd>";
        srHtmlText1[i++] = "</dl></div>";
        srHtmlText1[i++] = "<div>";
        var linksHtml = getLinksHtml(docIndex,uuid,docType,docFormat);
        for(var k = 0;k<linksHtml.length;k++){
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
    var titleNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='245a']/str");
    if( (titleNodes !=null || titleNodes != "") && titleNodes.length > 0) {
        title = titleNodes[0].text;
    }
    titleNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='245b']/str");
    if( (titleNodes !=null || titleNodes != "") && titleNodes.length > 0) {
        title = title + " " + titleNodes[0].text;
    }
    return title;
}

function getMarcAuthorValue(resultsXPathObj, docIndex) {

    var author = "";
    var authorNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='100a']/str");
    if((authorNodes != null || authorNodes != "") && authorNodes.length > 0) {
        author = authorNodes[0].text;
    }
    if (author == "") {
        authorNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='110a']/str");
        if((authorNodes != null || authorNodes != "") && authorNodes.length > 0) {
            author = authorNodes[0].text;
        }
    }
    if (author == "") {
        authorNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='111a']/str");
        if((authorNodes != null || authorNodes != "") && authorNodes.length > 0) {
            author = authorNodes[0].text;
        }
    }
    if (author == "") {
        authorNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='700a']/str");
        if((authorNodes != null || authorNodes != "") && authorNodes.length > 0) {
            author = authorNodes[0].text;
        }
    }
    if (author == "") {
        authorNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='710a']/str");
        if((authorNodes != null || authorNodes != "") && authorNodes.length > 0) {
            author = authorNodes[0].text;
        }
    }
    if (author == "") {
        authorNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='800a']/str");
        if((authorNodes != null || authorNodes != "") && authorNodes.length > 0) {
            author = authorNodes[0].text;
        }
    }
    if (author == "") {
        authorNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='810a']/str");
        if((authorNodes != null || authorNodes != "") && authorNodes.length > 0) {
            author = authorNodes[0].text;
        }
    }
    if (author == "") {
        authorNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='811a']/str");
        if((authorNodes != null || authorNodes != "") && authorNodes.length > 0) {
            author = authorNodes[0].text;
        }
    }
    if (author == "") {
        authorNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='400a']/str");
        if((authorNodes != null || authorNodes != "") && authorNodes.length > 0) {
            author = authorNodes[0].text;
        }
    }
    if (author == "") {
        authorNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='410a']/str");
        if((authorNodes != null || authorNodes != "") && authorNodes.length > 0) {
            author = authorNodes[0].text;
        }
    }
    if (author == "") {
        authorNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='411a']/str");
        if((authorNodes != null || authorNodes != "") && authorNodes.length > 0) {
            author = authorNodes[0].text;
        }
    }
    return author;
}

function getMarcSubjectValue(resultsXPathObj, docIndex) {

    var subject = "";
    var subjectNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='600a']/str");
    if((subjectNodes != null || subjectNodes != "") && subjectNodes.length > 0) {
        subject = subjectNodes[0].text;
    }
    if (subject == "") {
        subjectNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='610a']/str");
        if((subjectNodes != null || subjectNodes != "") && subjectNodes.length > 0) {
            subject = subjectNodes[0].text;
        }
    }
    if (subject == "") {
        subjectNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='611a']/str");
        if((subjectNodes != null || subjectNodes != "") && subjectNodes.length > 0) {
            subject = subjectNodes[0].text;
        }
    }
    if (subject == "") {
        subjectNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='630a']/str");
        if((subjectNodes != null || subjectNodes != "") && subjectNodes.length > 0) {
            subject = subjectNodes[0].text;
        }
    }
    if (subject == "") {
        subjectNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='650a']/str");
        if((subjectNodes != null || subjectNodes != "") && subjectNodes.length > 0) {
            subject = subjectNodes[0].text;
        }
    }
    if (subject == "") {
        subjectNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='651a']/str");
        if((subjectNodes != null || subjectNodes != "") && subjectNodes.length > 0) {
            subject = subjectNodes[0].text;
        }
    }
    if (subject == "") {
        subjectNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='653a']/str");
        if((subjectNodes != null || subjectNodes != "") && subjectNodes.length > 0) {
            subject = subjectNodes[0].text;
        }
    }

    return subject;
}

function getMarcPublisherValue(resultsXPathObj, docIndex) {

    var publisher = "";
    var publisherNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='260b']/str");
    if( (publisherNodes !=null || publisherNodes != "") && publisherNodes.length > 0) {
        publisher = publisherNodes[0].text;
    }

    return publisher;

}

function getMarcDescriptionValue(resultsXPathObj, docIndex) {

    var description = "";
    var descriptionNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='505a']/str");
    if( (descriptionNodes !=null || descriptionNodes != "") && descriptionNodes.length > 0) {
        description = descriptionNodes[0].text;
    }

    return description;
}

function getMarcLocationValue(resultsXPathObj, docIndex) {

    var location = "";
    var locationNodes = resultsXPathObj.selectNodes("//result/doc[" + (docIndex+1) + "]/arr[@name='856u']/str");
    if( (locationNodes !=null || locationNodes != "") && locationNodes.length >0) {
        location = locationNodes[0].text;
    }

    return location;
}
function getHighlightedText(id, displayFieldText, searchFieldName)
{
    var resultText=displayFieldText;
    var spanText="<span style=\"background-color:yellow\">";
    var filterKey="//lst[@name='highlighting']/lst[@name='"+ id+"']/arr[@name='"+ searchFieldName+"']/str";
    var highlightedFieldText = "";
    var highlightedNodes = resultsXPathObj.selectNodes(filterKey);
    if((highlightedNodes != null) && (highlightedNodes.length > 0)){
        highlightedFieldText = resultsXPathObj.selectNodes(filterKey)[0].text;
    }
    var indexText=highlightedFieldText.indexOf(spanText);
    if(indexText >= 0) {
        indexText=indexText+spanText.length;
        var highlightedWord=highlightedFieldText.substring(indexText,highlightedFieldText.indexOf("</span>"));
        var highlighText=spanText+highlightedWord+"</span>";
        resultText = resultText.replace(highlightedWord, highlighText);
    }
    return resultText;
}

//jQuery initialization code:
$(function(){
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
    var numfound = 0;
    var nodes = resultsXPathObj.selectNodes("//result/@numFound");
    if(nodes.length > 0){
        numfound = nodes[0].text;
    }
    var resultFromIndex = parseInt(resultsXPathObj.selectNodes("//result/@start")[0].text)+1;
    var i = parseInt(numfound)/parseInt(resultPageSize);
    var maxPage = Math.ceil(i);
    if(numfound > parseInt(resultPageSize)){
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
    else{
        $('#pageTag').hide();
    }
});
$(document).ready(function(){
    $('#recordsperpage').val(resultPageSize);
    $("#recordsperpage").change(function(){
        startLoadBusy();
        var select_index = $("#recordsperpage").val();
        document.getElementById("resultPageSize").value = select_index;
        document.getElementById("resultFromIndex").value = parseInt(resultsXPathObj.selectNodes("//result/@start")[0].text);
        document.getElementById("resultPageIndex").value =resultPageIndex;
        document.getElementById("action").value = "setPageSize";
        document.forms["discoveryForm"].submit();
    });
    $('#sortingorder').val(sortByTerms);
    $("#sortingorder").change(function()
    {
        startLoadBusy();
        var sort_value = $("#sortingorder").val();
        document.getElementById("sortByTerms").value = sort_value;
        var select_index = $("#recordsperpage").val();
        document.getElementById("resultPageSize").value = select_index;
        document.getElementById("resultFromIndex").value = parseInt(resultsXPathObj.selectNodes("//result/@start")[0].text);
        document.getElementById("resultPageIndex").value =resultPageIndex;
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
    var EndTime=new Date();
    log.debug("EndTime"+EndTime);
    var TimeDiff = EndTime - startTime;
    log.debug("TimeDiff........"+TimeDiff);

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



































