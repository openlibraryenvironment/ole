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
<%@ page import="org.kuali.ole.docstore.discovery.service.DiscoveryServiceImpl"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    import="org.kuali.ole.docstore.discovery.*" pageEncoding="ISO-8859-1"%>

<%@ include file="/kr/WEB-INF/jsp/tldHeader.jsp"%>
	<script type="text/javascript" src="script/jquery/jquery-1.4.2.js"></script>
    <script type="text/javascript" src="script/jquery-ui-1.8.16.custom/js/jquery-ui-1.8.16.custom.min.js"></script>
	<link type="text/css" rel="Stylesheet" href="./script/jquery-ui-1.8.16.custom/css/smoothness/jquery-ui-1.8.16.custom.css" />	
    <link type="text/css" rel="stylesheet" href="css/discovery.css" />
	<script type="text/javascript" src="scripts/ole/blackbird.js"></script>
    <link type="text/css" rel="Stylesheet" href="css/blackbird.css" />

	<script type="text/javascript" src="script/SoftXpath.js"></script>

<div id="headerarea1" class="headerarea1">
	<h1> Document Store Discovery</h1>	
</div>
<div id="busyDiv" align="center">
	<b> Loading search results. Please wait...</b>
	<img src="images-portal/busyImage.gif">
</div>
<div id=searchScreenDiv>

<html:form action="discovery.do" method="post">
<textarea id="searchParamsXml" rows="5" cols="80" style="display:none">
<c:out value="${discoveryForm.searchParamsXml}" />
</textarea>

<fieldset>
<legend class="advanced">Advanced Search</legend>


<table>
	<tr>
		<td class="doc">Document Category</td>
		<td><select id="documentCategory" name="documentCategory" class="category" >
			<option value="work">Work</option>
			<option value="auth">Authority</option>
			<option value="licenses">Licenses</option>
		<%--	<option value="security">Security</option>--%>    <%--skip sccurity from solr--%>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td class="doc">Sort By</td>
		<td ><select id="sortingorder" name="sortingorder" >
			<option value="titleasc">Title (A-Z)</option>
			<option value="titledesc">Title (Z-A)</option>
			<option value="authorasc">Author (A-Z)</option>
			<option value="authordesc">Author (Z-A)</option>
			<option value="pubdatedesc">Pub date (new-old)</option>
			<option value="pubdateasc">Pub date (old-new)</option>
			<option value="relevance">Relevance</option>
		</select></td>
	</tr>
	<tr>
		<td class="doc">Document Type</td>
		<td><select id="documentType" name="documentType" class="category" >
		</select></td>

	</tr>
	<tr>
		<td class="doc">Document Format</td>
		<td><select id="documentFormat" name="documentFormat" class="category" >
		</select></td>
	</tr>

</table>
<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input id="searchText" name="st" size="50" type="text" />
<select id="searchScope" name="searchScope">
	<option value="AND">All of these</option>
	<option value="OR">Any of these</option>
	<option value="phrase">As a phrase</option>
 </select>
&nbsp;&nbsp;<label for="in">in </label>
&nbsp;&nbsp;Search Field: <select id="searchField" name="searchField">
						   </select>
<br>
<fieldset id="advancedSearch" style="border:none; padding:0">

&nbsp;&nbsp;
<input type="radio" name="operator0" id="operator0" value="AND" checked="checked"> AND
<input type="radio" name="operator0" id="operator0" value="OR"> OR
<input type="radio" name="operator0" id="operator0" value="NOT"> NOT
<br>

&nbsp;&nbsp;<input id="searchText" name="st1" size="50" type="text" />
<select id="searchScope" name="searchScope1">
	<option value="AND">All of these</option>
	<option value="OR">Any of these</option>
	<option value="phrase">As a phrase</option>
 </select>
&nbsp;&nbsp;<label for="in">in </label>
&nbsp;&nbsp;Search Field: <select id="searchField1" name="searchField1">
						   </select>
</fieldset>
    <br>
    <fieldset id="advancedSearch" style="border:none; padding:0">

        &nbsp;&nbsp;
        <input type="radio" name="operator1" id="operator1" value="AND"> AND
        <input type="radio" name="operator1" id="operator1" value="OR"> OR
        <input type="radio" name="operator1" id="operator1" value="NOT"> NOT
        <br>

        &nbsp;&nbsp;<input id="searchText" name="st2" size="50" type="text"/>
        <select id="searchScope" name="searchScope2">
            <option value="AND">All of these</option>
            <option value="OR">Any of these</option>
            <option value="phrase">As a phrase</option>
        </select>
        &nbsp;&nbsp;<label for="in">in </label>
        &nbsp;&nbsp;Search Field: <select id="searchField2" name="searchField2">
    </select>
    </fieldset>
    <br>
    <fieldset id="advancedSearch" style="border:none; padding:0">

        &nbsp;&nbsp;
        <input type="radio" name="operator2" id="operator2" value="AND"> AND
        <input type="radio" name="operator2" id="operator2" value="OR"> OR
        <input type="radio" name="operator2" id="operator2" value="NOT"> NOT
        <br>

        &nbsp;&nbsp;<input id="searchText" name="st3" size="50" type="text"/>
        <select id="searchScope" name="searchScope3">
            <option value="AND">All of these</option>
            <option value="OR">Any of these</option>
            <option value="phrase">As a phrase</option>
        </select>
        &nbsp;&nbsp;<label for="in">in </label>
        &nbsp;&nbsp;Search Field: <select id="searchField3" name="searchField3">
    </select>
    </fieldset>
    <br>
    <fieldset id="advancedSearch" style="border:none; padding:0">

        &nbsp;&nbsp;
        <input type="radio" name="operator3" id="operator3" value="AND"> AND
        <input type="radio" name="operator3" id="operator3" value="OR"> OR
        <input type="radio" name="operator3" id="operator3" value="NOT"> NOT
        <br>

        &nbsp;&nbsp;<input id="searchText" name="st4" size="50" type="text"/>
        <select id="searchScope" name="searchScope4">
            <option value="AND">All of these</option>
            <option value="OR">Any of these</option>
            <option value="phrase">As a phrase</option>
        </select>
        &nbsp;&nbsp;<label for="in">in </label>
        &nbsp;&nbsp;Search Field: <select id="searchField4" name="searchField4">
    </select>
    </fieldset>

</fieldset>
<br \>
<br \>

<input type="hidden" name="searchType" id="searchType" value="">
<!-- <input type="hidden" name="sortField" value="Author_display">-->
<input type="hidden" name="sortByTerms" id="sortByTerms" value="${discoveryForm.sortByTerms}">
<!-- <input type="hidden" name="sortOrder" value="asc"> -->
<input type="hidden" name="docCat" value="bib">
<p style="margin : 0em 20em; ">
	<input type="button" name="Search" style="background:url(images-portal/buttonsmall_search.gif) 0 0 no-repeat;border:none; width:70px; height:100px; cursor:pointer;" onclick="submitSearch('advancedSearch')">
	<input type="button" name="Clear" style="background:url(images-portal/buttonsmall_clear.gif) 0 0 no-repeat;border:none; width:58px; height:100px; cursor:pointer;" onclick="submitSearch('newSearch')">
	<input type="button" name="Cancel" style="background:url(images-portal/buttonsmall_cancel.gif) 0 0 no-repeat;border:none; width:70px; height:100px; cursor:pointer;" onclick="submitSearch('cancel')">
</p>

</html:form>
</div>
</body>


<script type="text/javascript">
var sortByTerms = "${discoveryForm.sortByTerms}";

function submitSearch(searchType) {
	//$("#searchScreenDiv").loader('loader', true);
	var form = document.forms["discoveryForm"];
	if (searchType != null) {
		document.getElementById("searchType").value = searchType;
	}	
	if (searchType == "advancedSearch") {
		$("#busyDiv").show();
	}
	form.submit();
}

	$(document).ready(function(){
        //$("#searchScreenDiv").loader();
        $("#busyDiv").hide();
	});
	
		function onAddNewField(){
				var j = 0;
					while (true) {
						var name = "#searchField" + j ;
						var value = $(name).html();
						if (value == null){
						var fieldName = "#searchField" + (j-1) ;
						$(fieldName).append(fieldOptionsText);
						break;
						}
					
						if(operatorZeroValue!=""){
							onclickradio = operatorZeroValue;
						}
						if(onclickradio == ""){
							onclickradio ="AND";
						}
						var nodePath = JQUERY4U.UTIL.formatVarString('input:radio[id="{1}"]', "operator"+0);
						var valuePath = JQUERY4U.UTIL.formatVarString('[value="{1}"]', onclickradio);
						log.debug("nodePath"+nodePath);
						$(nodePath).filter(valuePath).attr('checked', true);
						j++;
					}
		}
		var configInfoXml = null;
		 var operatorZeroValue = "";
		var onclickradio = "";
		 
		function onChangeCategory()  {   
				var doccategoryId = $("#documentCategory").val();
				populateDocTypeDropdown($("#documentType"), configInfoXml, doccategoryId);
		}

		function onChangeDocType() {    
			var doccategoryId = $("#documentCategory").val();
			var doctypeId = $("#documentType").val();
	        populateFormatDropdown($("#documentFormat"), configInfoXml, doccategoryId, doctypeId);
	    }

		function onChangeDocFormat() {    
			var doccategoryId = $("#documentCategory").val();
			var doctypeId = $("#documentType").val();
			var docFormatId = $("#documentFormat").val();
            populateFieldDropdowns(configInfoXml, doccategoryId, doctypeId, docFormatId);
	    }
	    $(document).ready(function() {  
	    	$('#sortingorder').val(sortByTerms);	    	
	    	$('input:radio[id="operator0"]').click(function(event) {
	    		onclickradio = $('input:radio[id="operator0"]:checked').val();
	    	});
	    
	      	$.ajaxSetup({async:false});
	    	$.post("DocumentConfig.xml", { doccategoryID: 'all' }, function(data) {
			configInfoXml = data;
			
		     });
			onChangeCategory();
				$("#documentCategory").change(function()  {
					var doccategoryId = $("#documentCategory").val();
					populateDocTypeDropdown($("#documentType"), configInfoXml, doccategoryId);
				});
			
			 var searchParamsXml = document.getElementById("searchParamsXml").value;
				 var searchParamsObj = new SoftXpath();
				 searchParamsObj.registerNamespace('','');
				 if(!searchParamsObj.loadXML(searchParamsXml)){
				 }
				 log.debug("searchType"+searchParamsObj.selectNodes("//searchType")[0].text);
			 if(searchParamsObj.selectNodes("//searchType")[0].text == "backToSearch"){
				 setInitialValues(configInfoXml,searchParamsObj);
			 }
			 
	    });
	    function populateDocTypeDropdown(select, data, doccategoryId) {
	        select.html('');
			var nodePath = JQUERY4U.UTIL.formatVarString("documentCategory[id='{1}'] documentType", doccategoryId);
			$(data).find(nodePath).each(function() {
	            select.append($('<option></option>').val($(this).attr('id')).html($(this).attr('name')));
			});
			$("#documentType").change(function(){
				var doctypeId = $(this).val();
	                populateFormatDropdown($("#documentFormat"), configInfoXml, doccategoryId,doctypeId);
	        }); 
			onChangeDocType();
		}
		function populateFormatDropdown(select, data, doccategoryId,doctypeId) {
		    select.html('');
			var nodePath = JQUERY4U.UTIL.formatVarString("documentCategory[id='{1}'] documentType[id='{2}'] documentFormat", doccategoryId, doctypeId);
			$(data).find(nodePath).each(function() {
	            select.append($('<option></option>').val($(this).attr('id')).html($(this).attr('name')));
			});
			onChangeDocFormat();
			$("#documentFormat").change(function () {
				var  docFormatId = $(this).val();
					populateFieldDropdowns(configInfoXml, doccategoryId, doctypeId, docFormatId);
	        });
		}
		function getFieldOptionsText(data,  doccategoryId, doctypeId, docFormatId ) {
			var fieldOptionsText = [];
			var i = 0;
			var id ="";
			var nodePath = JQUERY4U.UTIL.formatVarString(" documentCategory[id='{1}'] documentType[id='{2}'] documentFormat[id='{3}'] field", doccategoryId, doctypeId ,docFormatId );
			fieldOptionsText[i++] = JQUERY4U.UTIL.formatVarString('<option value="{1}">{2}</option>', 'all', 'All');
			$(data).find(nodePath).each(function() {
					 if(docFormatId == "all"){
		                    fieldOptionsText[i++] = JQUERY4U.UTIL.formatVarString('<option value="{1}">{2}</option>',  $(this).attr('id'), $(this).attr('name'));
		             }else{
					 id = $(this).attr('id');
		                var arr = id.split("_");
		                if (arr[1] == "search")
		                    fieldOptionsText[i++] = JQUERY4U.UTIL.formatVarString('<option value="{1}">{2}</option>', id,
		                                                                          $(this).attr('name'));
		                 }
	                  });
			return fieldOptionsText.join('');
		}
		var fieldOptionsText = "" ;
		function populateFieldDropdowns( data,  doccategoryId, doctypeId, docFormatId ){
			 fieldOptionsText = getFieldOptionsText(data,  doccategoryId, doctypeId, docFormatId);
			$("#searchField").html('');
			$("#searchField").append(fieldOptionsText);
			$("#searchField0").html('');
			$("#searchField0").append(fieldOptionsText);
			
			$("#searchField").change(function () {    
				resetAutoComplete(this);
	        });
			$("#searchField0").change(function () {    
				resetAutoComplete(this);
	        });


			var j = 1;
				while (true) {
					var name = "#searchField" + j ;
					$(name).change(function () {
						resetAutoComplete(this);
			        });
					$(name).children().remove().end().append(fieldOptionsText);
					var value = $(name).html();
					if (value == null){
					break;
					}
					j++;
			}
				}

		function resetAutoComplete(selectBox) {
			var selectBoxId = selectBox.id.substr(11);
			var textBoxId = 'searchText';
			if ((null != selectBoxId) && (selectBoxId.length > 0)) {
				textBoxId = textBoxId + selectBoxId;
			}
			var docCategoryName = $("#documentCategory").val();
			var docTypeName = $("#documentType").val();
			log.debug('docTypeName selected=' + docTypeName);
			var docFormatName = $("#documentFormat").val();
			var docFieldName = selectBox.value;
		    var docSearchURL = "<%=DiscoveryServiceImpl.getDocSearchUrl()%>";
			enableAutoComplete(textBoxId, docCategoryName, docTypeName, docFormatName, docFieldName, docSearchURL);
		}

		function enableAutoComplete(textBoxId, docCategoryName, docTypeName, docFormatName, docFieldName, docSearchURL) {
			log.debug("enableAutoComplete: textBoxId=" + textBoxId + ", documentCategory=" + docCategoryName + ", documentType=" + docTypeName + ", docFormatName=" + docFormatName + ", docFieldName=" + docFieldName);
			var queryText = '';
			var displayFieldName = docFieldName.substring(0, docFieldName.indexOf("_")) + "_display";
			log.debug("displayFieldName=" + displayFieldName);
			if (docTypeName != '') {
				queryText = "(documentType:" + docTypeName + ")";
			}
			if (docFormatName != '') {
				if (queryText.length > 0) {
					queryText = queryText + " AND ";
				}
				queryText = queryText + "(documentFormat:" + docFormatName + ")";
			}
			log.debug("queryText=" + queryText);
			var textBox = document.getElementById(textBoxId);
			log.debug('textBoxId=' + textBoxId);
			log.debug('textBox=' + textBox);
		<!-- log.debug('url=' + docSearchURL + docCategoryName + "/select/?wt=json&json.wrf=?&"); -->
		  log.debug('url=' + docSearchURL +"/bib/select/?wt=json&json.wrf=?&");
			$(textBox).autocomplete({
						source: function( request, response ) {
							$.ajax({
								url: docSearchURL +"bib/select/?wt=json&json.wrf=?&",
								dataType: "jsonp",
								data: {
									rows: 20,
									q: docFieldName + ":" + request.term,
									sort: "score desc"
								},
								success: function( data ) {
									log.debug("resulting documents count:"
											+ data.response.docs.length);
									response( $.map(data.response.docs, function(doc) {
										var result = eval("doc." + docFieldName);
										log.debug("doc.id:" + doc.id + " | doc." + docFieldName + ":" + result);
										return {
											label: ""+eval("doc." + displayFieldName),
											value: eval("doc." + displayFieldName)
										}
									}));
								}
							});
						},
						minLength: 3,
						select: function( event, ui ) {
							//log( ui.item ?
							//	"Selected: " + ui.item.label :
							//	"Nothing selected, input was " + this.value);
						},
						open: function() {
							//$( this ).removeClass( "ui-corner-all" ).addClass( "ui-corner-top" );
						},
						close: function() {
							//$( this ).removeClass( "ui-corner-top" ).addClass( "ui-corner-all" );
						}
					});					  								
		}	
			
		
	var JQUERY4U = {};
	JQUERY4U.UTIL = {
	formatVarString: function()
	    {
	        var args = [].slice.call(arguments);
	        if(this.toString() != '[object Object]')
	        {
	            args.unshift(this.toString());
	        }
	        var pattern = new RegExp('{([1-' + args.length + '])}','g');
	        return String(args[0]).replace(pattern, function(match, index) { return args[index]; });
	    }
	}
/**
 * Sets the initial values for all fields, based on whether it is a new search or current search.
 */
 
function setInitialValues(configInfoXml,searchParamsObj) {
	var searchParamsXml = document.getElementById("searchParamsXml").value;
	 var searchParamsObj = new SoftXpath();
	 searchParamsObj.registerNamespace('','');
	 if(!searchParamsObj.loadXML(searchParamsXml)){
	 }
	var searchType = '';
	var docCategoryVal = '';
	var docTypeVal = '';
	var docFormat = '';
	var sortField = '';
	var sortOrder = '';

	$("#documentCategory").val(searchParamsObj.selectNodes("//documentCategory")[0].text);
	onChangeCategory();
	$("#documentType").val(searchParamsObj.selectNodes("//documentType")[0].text);
	onChangeDocType();
	$("#documentFormat").val(searchParamsObj.selectNodes("//documentFormat")[0].text);

	populateFieldDropdowns(configInfoXml, $("#documentCategory").val(), $("#documentType").val(), $("#documentFormat").val());
	// search fields and search field values
	$("#searchField").val(searchParamsObj.selectNodes("//searchCondition/@fieldName")[0].text);
	$("#searchText").val(searchParamsObj.selectNodes("//searchCondition/@fieldValue")[0].text);
	$("#searchScope").val(searchParamsObj.selectNodes("//searchCondition/@searchScope")[0].text);
	for (var i=0; i < searchConditionCount-1; i++) {
		var searchFieldName = "#searchField" + i;
		var searchTextName = "#searchText" + i;
		var operatorid = "operator" + i;
		var searchScope = "#searchScope" + i;		
		$(searchFieldName).val(searchParamsObj.selectNodes("//searchCondition/@fieldName")[i+1].text);
		$(searchTextName).val(searchParamsObj.selectNodes("//searchCondition/@fieldValue")[i+1].text);
		$(searchScope).val(searchParamsObj.selectNodes("//searchCondition/@searchScope")[i+1].text);
		var preset = searchParamsObj.selectNodes("//searchCondition/@operator")[i+1].text;
		var nodePath = JQUERY4U.UTIL.formatVarString('input:radio[id="{1}"]', operatorid);
		var valuePath = JQUERY4U.UTIL.formatVarString('[value="{1}"]', preset);
		$(nodePath).filter(valuePath).attr('checked', true);
	}
	operatorZeroValue = searchParamsObj.selectNodes("//searchCondition/@operator")[1].text;
	var nodePath = JQUERY4U.UTIL.formatVarString('input:radio[id="{1}"]', "operator"+0);
	var valuePath = JQUERY4U.UTIL.formatVarString('[value="{1}"]', operatorZeroValue);
	$(nodePath).filter(valuePath).attr('checked', true);
	
}	
	</script>


