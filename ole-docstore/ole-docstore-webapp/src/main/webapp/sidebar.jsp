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
<%@ page import="org.kuali.ole.docstore.discovery.*"%>
<head>
<link rel="stylesheet" type="text/css" href="./css/ole.css" />
<script src="./script/validation.js" language="JavaScript"></script>
<script src="./script/ole.js" language="JavaScript"></script>

	<script type="text/javascript" src="./script/jquery.js"></script>
	<script type="text/javascript" src="./script/jquery.autocomplete.js"></script>  
    <link type="text/css" rel="Stylesheet" href="./css/jquery.autocomplete.css" />
 
    <script type="text/javascript" src="./script/blackbird.js"></script>
    <link type="text/css" rel="Stylesheet" href="./css/blackbird.css" />

 
	<style>
		input {
			font-size: 120%;
		}
	</style>
	<br>
	
	
	<div id="header" ><jsp:include page="header.jsp" /></div>

	
<%
	//Session attribute to hold search criteria
	final String SESSION_ATTRIBUTE_NAME = "searchcriteria"; 

	SearchCriteria sc = (SearchCriteria) session
			.getAttribute(SESSION_ATTRIBUTE_NAME);

	String tropicalNameEntry = ""; 
	String modifyingAgency = "";
	String name = "";
	String fullerFormName = "";
	String dateAssociatedWithName = "";
	String title = "";
	String remainderOfTitle = "";
	String stmtOfResponsibility = "";
	String placeOfPublication = "";
	String nameOfPublisher = "";
	String dateOfPublication = "";
	String extent = "";
	String dimentions = "";
	String generalNote = "";
	String tropcialNameEntry = "";
	String generalSubdivision = "";
	String personalName = "";
	String corporatename = "";
	String subordinateUnit = "";
	//String rangeOperator = "";
	//price-range-fields Name change
	String minPrice="";
	String maxPrice = "";
	String mainEntryPersonalNameComposite="";
	String titleStatement="";
	String dateMin="";
	String dateMax="";
	//ISBN,ISSN fileds in the session
	String ISBN="";
	String ISSN="";

	if (sc != null) {
		String searchType = sc.getSearchType();

		if (searchType.equals("advanced")) {

			modifyingAgency = sc.getModifyingAgency();
			mainEntryPersonalNameComposite=sc.getMainEntryPersonalNameCompositeForDisplay();
			name = sc.getName();
			fullerFormName = sc.getFullerFormName();
			dateAssociatedWithName = sc.getDateAssociatedWithName();
			titleStatement=sc.getTitleStatementCompositeForDisplay();
			title = sc.getTitle();
			remainderOfTitle = sc.getRemainderOfTitle();
			stmtOfResponsibility = sc.getStmtOfResponsibility();
			nameOfPublisher = sc.getNameOfPublisher();
			dateOfPublication = sc.getDateOfPublication();
			placeOfPublication = sc.getPlaceOfPublication();
			extent = sc.getExtent();
			dimentions = sc.getDimentions();
			generalNote = sc.getGeneralNote();
			tropicalNameEntry = sc.getTropcialNameEntry();
			generalSubdivision = sc.getGeneralSubdivision();
			personalName = sc.getPersonalName();
			corporatename = sc.getCorporatename();
			subordinateUnit = sc.getSubordinateUnit();
			//rangeOperator = sc.getRangeOperator();
			//price range fileds-name-change 
			 maxPrice = sc.getRangeInput();
			 minPrice=sc.getPrice();
			 dateMin=sc.getDateMin();
			 dateMax=sc.getDateMax();
			 ISBN=sc.getISBN();
			 ISSN=sc.getISSN();
		}
	}
%>

<center>
<table id="sidebar" border="0" style="font-size: 14px" width="994px" height="75%">
	
	
	

		
	
		<tr>
	<tr valign="center">
		<td>
		<center>Advanced Search<center>
		<form name="asearch" method="post" action="./results.jsp">
		<table style="font-size: 12" border="0" width="994px" 
			>
			<!-- 
						the row below is commented as the content does not distinguish between bride and groom names
						If this issue is resolved the UI can be modified to support searching by bride and groom names
					 -->
			<!--
					<tr>
						<td align="left">
							<input type="radio" name="who" value="bride" checked> Bride
						</td>
						<td>
							<input type="radio" name="who" value="groom"> Groom
						</td>
					</tr>
					 -->

			
			</br>
               
			
                  
                 <tr>
            <TH width="10%">&nbsp;</th>
			<TH width="" align="left" colspan="2">
			Cataloging Resource
			</TH>
			</tr>   
			
		
			<tr>
                 <td width="10%">&nbsp;</td>			     
				<td width="" align="left">&nbsp;&nbsp;&nbsp;&nbsp;Modifying Agency</td>
				<td width="" align="left"><input size="50" id="modifyingAgency" name="modifyingAgency" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=modifyingAgency%>" /></td>
			</tr>		
            <tr>
            <TH width="10%">&nbsp;</th>
			<TH width="" align="left" colspan="3">
			Main Entry Personal Name
			</TH>
			</tr>
		
			<tr>
			<td width="10%">&nbsp;</td>			     
				<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Main Entry Personal Name:</td>
				<td align="left"><input size="50" name="mainEntryPersonalNameComposite" id="mainEntryPersonalNameComposite"
					type="text" onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=mainEntryPersonalNameComposite%>" /></td>
			</tr>

			<tr>
			<td width="10%">&nbsp;</td>
		     
				<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Name:</td>
				<td><input size="50" id="mainEntryPersonalName" name="name" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=name%>" /></td>
			</tr>

			<tr>
			<td width="10%">&nbsp;</td>
		     
				<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Fuller Form of Name:</td>
				<td><input align="left" size="50" name="fullerFormName"
					type="text" onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=fullerFormName%>" /></td>
			</tr>
			<tr>
			<td width="10%">&nbsp;</td>
	     
				<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Dates Associated With Name:</td>
				<td><input size="50" name="datesAssociatedWithName" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=dateAssociatedWithName%>" /></td>
			</tr>
            <tr>
            <TH width="10%">&nbsp;</th>
			<TH width="40%" align="left" colspan="2">
			Title Statement
			</TH>
			</tr>
			
			<tr>
			
			<td width="10%">&nbsp;</td>
				<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Title Statement:</td>
				<td><input size="50" name="titleStatementComposite" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=titleStatement%>" /></td>
			</tr>
			<tr>
			<td width="10%">&nbsp;</td>
				<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Title:</td>
				<td><input size="50" id="title" name="title" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=title%>" autocomplete="off"/>
					</td>
			</tr>
			<tr>
			<td width="10%">&nbsp;</td>
				<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Remainder Of Title:</td>
				<td><input size="50" name="remainderOfTitle" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=remainderOfTitle%>" /></td>
			</tr>
			<tr>
			<td width="10%">&nbsp;</td>
				<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Statement Of Responsibility:</td>
				<td><input size="50" name="stmtOfResponsibility" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=stmtOfResponsibility%>" /></td>
			</tr>
            <tr>
            <TH width="10%">&nbsp;</th>
			<TH width="40%" align="left" colspan="2">
			Publication,Distribution etc.,
			</TH>
			</tr>

			<tr>
			<td width="10%">&nbsp;</td>
				<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Place Of Publication:</td>
				<td><input size="50" id="placeOfPublication" name="placeOfPublication" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=placeOfPublication%>" /></td>
			</tr>
			<tr>
			<td width="10%">&nbsp;</td>
				<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Name Of Publisher:</td>
				<td><input size="50" name="nameOfPublisher" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=nameOfPublisher%>" /></td>
			</tr>
			<tr>
			<td width="10%">&nbsp;</td>
				<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Date Of Publication:</td>
				<td><input size="50" name="dateOfPublication" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=dateOfPublication%>" /></td>
			</tr>
            <tr>
            <TH width="10%">&nbsp;</th>
			<TH width="40%" align="left" colspan="2">
			Physical Distribution
			</TH>
			</tr>
			<tr>
			<td width="10%">&nbsp;</td>
				<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Extent:</td>
				<td><input size="50" name="extent" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=extent%>" /></td>
			</tr>
			<tr>
			<td width="10%">&nbsp;</td>
				<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Dimentions:</td>
				<td><input size="50" name="dimentions" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=dimentions%>" /></td>
			</tr>
            <tr>
            <TH width="10%">&nbsp;</th>
			<TH width="40%" align="left" colspan="2">
			General Note
			</TH>
			</tr>
			<tr>
			<td width="10%">&nbsp;</td>
				<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;General Note:</td>
				<td><input size="50" name="generalNote" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=generalNote%>" /></td>
			</tr>
            <tr>
            <TH width="10%">&nbsp;</th>
			<TH width="40%" align="left" colspan="2">
			Subject Added entry-Tropical Term
			</TH>
			</tr>
			<tr>
			<td width="10%">&nbsp;</td>
				<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Tropcial Name Entry:</td>
				<td><input size="50" name="tropcialNameEntry" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=tropicalNameEntry%>" /></td>
			</tr>
			<tr>
			<td width="10%">&nbsp;</td>
				<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;GeneralSubdivision:</td>
				<td><input size="50" name="generalSubdivision" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=generalSubdivision%>" /></td>
			</tr>
            <tr>
            <TH width="10%">&nbsp;</th>
			<TH width="40%" align="left" colspan="2">
			Added entry-Personal Name
			</TH>
			</tr>
			<tr>
			<td width="10%">&nbsp;</td>
				<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;PersonalName:</td>
				<td><input size="50" name="personalName" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=personalName%>" /></td>
			</tr>
            <tr>
            <TH width="10%">&nbsp;</th>
			<TH width="40%" align="left" colspan="2">
			Added entry-Corporate Name
			</TH>
			</tr>
			<tr>
			<td width="10%">&nbsp;</td>
				<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Corporate Name:</td>
				<td><input size="50" name="corporatename" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=corporatename%>" /></td>
			</tr>
			<tr>
			<td width="10%">&nbsp;</td>
				<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Subordinate Unit:</td>
				<td><input size="50" name="subordinateUnit" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=subordinateUnit%>" /></td>
			</tr>
            <tr>
            <TH width="10%">&nbsp;</th>
			<TH width="40%" align="left" colspan="2">
			Others
			</TH>
			</tr>
			<td width="10%">&nbsp;</td>
				<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Price:</td>
				<td><input size=20 name="minPrice" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=minPrice%>" />
					&nbsp;&nbsp;
<!-- 					
					<select name="Roperator" style="width:50px">
							<option>>=</option>
							<option><=</option>

						</select>
						 -->
					to &nbsp;&nbsp;<input type="text" name="maxPrice" value="<%=maxPrice%>" />
					</td>
									
			</tr>
			<tr>
				<td width="10%">&nbsp;</td>
				<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Date (999-u):</td>
				<td><input size=20 name="dateMin" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=dateMin%>" />
									
					
						&nbsp;&nbsp;  to &nbsp;&nbsp;<input type="text" name="dateMax" value="<%=dateMax%>" size=20
					 />[MM/DD/YYYY]</td>
               
			</tr>
			<!-- ISBN,ISSN field definition -->
			<tr>
				<td width="10%">&nbsp;</td>
				<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;ISBN:</td>
				<td><input size=20 name="ISBN" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=ISBN%>" />
									
					
			</tr>
			 <tr>
			<td width="10%">&nbsp;</td>
				<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;ISSN:</td>
				<td><input size=20 name="ISSN" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=ISSN%>" />
					</td>
					</tr>

			<!--  
			
		<td colspan="2" align="center"><input type="button"
					class="buttonLong" name="Submit" value="Search"
					onclick="validateAdvancedSearch();" />&nbsp;</td>  
			</tr>		
					-->
			<tr>
			<td width="10%">&nbsp;</td>
				<td align="center"><input type="radio" name="operator"
					value="OR" checked> OR &nbsp; &nbsp; <input type="radio"
					name="operator" value="AND"> AND</td>
			</tr>
			<tr>
			<td width="10%">&nbsp;</td>
				<td align="center" colspan="1">&nbsp; 
				<a href="#" class="round"
					style="float: left" onclick="resetForm();"><ins><strong>Clear</strong></ins></a>
				&nbsp; &nbsp; &nbsp;&nbsp;&nbsp;&nbsp;
				<a href="#" class="round" style="float: right"
					onclick="validateAdvancedSearch();"><ins><strong>Search</strong></ins></a>
				</td>
				<td width="">&nbsp;</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
		</table>
		<input type="hidden" name="searchtype" value="advanced"></input></form>
		<input type="hidden" name="fieldtype" value=""></input></form>
		</fieldset>
		</td>
	</tr>
	<tr>
		<td>
		<p></p>
		</td>
	</tr>
	<script>
	function enableAutoComplete(textBoxId, docCategoryName, docTypeName, docFieldName) {
		log.debug("enableAutoComplete: textBoxId=" + textBoxId + ", docCategory=" + docCategoryName + ", docType=" + docTypeName + ", docFieldName=" + docFieldName);
		var textBox = document.getElementById(textBoxId);
		  $(textBox).autocomplete(
					docCategoryName + "/select/?wt=json&json.wrf=?", {
					dataType : "jsonp",
					width: 260,
					selectFirst: false,
					searchFieldName : docFieldName,
					extraParams : {
						rows : 10,
						fq : "DocType:" + docTypeName //,
						//qt : "artistAutoComplete"
					},
					minChars : 3,
					parse : function(data) {
						log.debug("resulting documents count:"
								+ data.response.docs.length);
						return $.map(data.response.docs, function(
								doc) {
							log.debug("doc.id:" + doc.id + " | doc." + docFieldName + ":" + eval("doc." + docFieldName));
							return {
								data : doc,
								value : doc.id.toString(),
								result : eval("doc." + docFieldName)
							}
						});
					},
					formatItem : function(doc) {
						//return formatForDisplay(doc);
						return eval("doc." + docFieldName)
					}
				});
		
	}	  
	
	   // var options=new Object();
	  //  options.extra
		//$("#modifyingAgency").autocomplete("Suggestions.jsp");
/*
	  $("#modifyingAgency").autocomplete("Suggestions.jsp", {
         extraParams: {fieldId: 0}
       }); 
	
	  $("#title").autocomplete("Suggestions.jsp", {
		   extraParams: {fieldId: 1}
		}); 
		
	  $("#placeOfPublication").autocomplete("Suggestions.jsp", {
		   extraParams: {fieldId: 2}
		}); 
	  $("#mainEntryPersonalNameComposite").autocomplete("Suggestions.jsp", {
		   extraParams: {fieldId: 3}
		}); 
	  */
	  $().ready(function() {
		  $("#modifyingAgency").autocomplete("Suggestions.jsp", {
		         extraParams: {fieldId: 0}
		       }); 
		  $("#placeOfPublication").autocomplete("Suggestions.jsp", {
			   extraParams: {fieldId: 2}
			}); 
		  $("#mainEntryPersonalNameComposite").autocomplete("Suggestions.jsp", {
			   extraParams: {fieldId: 3}
			}); 
		});
<%
String docCategoryName = "";
String docTypeName = "";
String docFieldName = "";
String textBoxId = "";
String autoCompleteFields[] = {"dynaField1", "dynaField2"};
%>		
<% 
	for (int i=0; i < autoCompleteFields.length; i++) {
		
		if (autoCompleteFields[i].equals("dynaField1")) {
			docCategoryName = "bib";
			docTypeName = "marc";
			docFieldName = "Title";
			textBoxId = "title";
		}
		else if (autoCompleteFields[i].equals("dynaField2")) {
			docCategoryName = "bib";
			docTypeName = "marc";
			docFieldName = "MainEntryPersonalName";
			textBoxId = "mainEntryPersonalName";			
		}
%>
enableAutoComplete('<%=textBoxId%>', '<%=docCategoryName%>', '<%=docTypeName%>', '<%=docFieldName%>');			
<%
	}
%>


	</script>
	<!--
	<tr height="20%">
		<td>
		<table border="0" align="center" id="main" height="100%" width="100%"
			cellpadding="0" cellspacing="0">
			<tr>
				<td align="left"><img src="./images/bullet_main.gif"></img></td>
				<td align="left">Use Advanced Search to search by Name, Date
				<br/>and Page / Book #</td>
			</tr>
			<tr>
				<td align="left"><img src="./images/bullet_main.gif"></img></td>
				<td align="left">You can browse through the
				licenses/certificates by date and locate the right
				license/certificate</td>
			</tr>
		</table>
		</td>
	</tr>
	-->
</table>
</center>
