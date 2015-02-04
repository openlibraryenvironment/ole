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
    import="org.kuali.ole.docstore.discovery.*" pageEncoding="ISO-8859-1"%>

<%@ include file="/kr/WEB-INF/jsp/tldHeader.jsp"%>


<script src="scripts/ole/validation.js" language="JavaScript"></script>
<script src="scripts/ole/ole.js" language="JavaScript"></script>

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


	<kul:page showDocumentInfo="false" htmlFormAction="docStore" renderMultipart="true"
	showTabButtons="false" docTitle="Document Store" 
	transactionalDocument="false" headerDispatch="true" headerTabActive="true"
	sessionDocument="false" headerMenuBar="" feedbackKey="true" defaultMethodToCall="start" >

	
<c:set var="BookAttributes" value="${DataDictionary.Book.attributes}" />
<kul:tabTop tabTitle="DocumentStore" defaultOpen="true" tabErrorKey="*">
			<div class="tab-container" align=center>				
				
<table id="sidebar" border="0" width="75%" height="100%" style="font-size: 10px"
	>
	<tr>
		<td>

		<div id="header"><jsp:include page="header.jsp" /></div>
	</tr>
	<tr>
	<tr valign="center">
		<td>
		<center>Advanced Search<center>
		<form name="asearch" method="post" action="./results.jsp">
		<table style="font-size: 12" border="0" width="994px" 
			>
				</br>                             
                 <tr>
                 <TH width="10%">&nbsp;</th>
			<TH width="" align="left" colspan="2">
			<kul:htmlAttributeHeaderCell literalLabel="Cataloging Resource" />
						</TH>
			</tr>   
			
		
			<tr>
                 <td width="10%">&nbsp;</td>			
                 <kul:htmlAttributeHeaderCell literalLabel="Cataloging Resource" />     
				<td width="" align="left">&nbsp;&nbsp;&nbsp;&nbsp;Modifying Agency</td>
				<td width="" align="left"><input size="50" name="modifyingAgency" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=modifyingAgency%>" /></td>
			</tr>		
            <tr>
            <TH width="10%">&nbsp;</th>
			<TH width="" align="left" colspan="3">
			<kul:htmlAttributeHeaderCell literalLabel="Cataloging Resource" />
			Main Entry Personal Name
			</TH>
			</tr>
		
			<tr>
			<td width="10%">&nbsp;</td>	
			<kul:htmlAttributeHeaderCell literalLabel="Cataloging Resource" />		     
				<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Main Entry Personal Name:</td>
				<td align="left"><input size="50" name="mainEntryPersonalNameComposite"
					type="text" onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=mainEntryPersonalNameComposite%>" /></td>
			</tr>

			<tr>
			<td width="10%">&nbsp;</td>
		     <kul:htmlAttributeHeaderCell literalLabel="Name" />
				<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Name:</td>
				<td><input size="50" name="name" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=name%>" /></td>
			</tr>

			<tr>
			<td width="10%">&nbsp;</td>
		     <kul:htmlAttributeHeaderCell literalLabel="Fuller Form of Name:" />
				<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Fuller Form of Name:</td>
				<td><input align="left" size="50" name="fullerFormName"
					type="text" onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=fullerFormName%>" /></td>
			</tr>
			<tr>
			<td width="10%">&nbsp;</td>
	     <kul:htmlAttributeHeaderCell literalLabel="dates Associated with Name:" />
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
			<kul:htmlAttributeHeaderCell literalLabel="Title Statement:" />
				<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Title Statement:</td>
				<td><input size="50" name="titleStatementComposite" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=titleStatement%>" /></td>
			</tr>
			<tr>
			<td width="10%">&nbsp;</td>
			<kul:htmlAttributeHeaderCell literalLabel="Title:" />
				<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Title:</td>
				<td><input size="50" name="title" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=title%>" /></td>
			</tr>
			<tr>
			<td width="10%">&nbsp;</td>
			<kul:htmlAttributeHeaderCell literalLabel="Remainder of Title:" />
				<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Remainder Of Title:</td>
				<td><input size="50" name="remainderOfTitle" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=remainderOfTitle%>" /></td>
			</tr>
			<tr>
			<td width="10%">&nbsp;</td>
			<kul:htmlAttributeHeaderCell literalLabel="Statement Of Responsibility:" />
				<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Statement Of Responsibility:</td>
				<td><input size="50" name="stmtOfResponsibility" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=stmtOfResponsibility%>" /></td>
			</tr>
            <tr>
            <TH width="10%">&nbsp;</th>
			<TH width="40%" align="left" colspan="2">
			
			<kul:htmlAttributeHeaderCell literalLabel="Publication,Distribution etc"/>
			
			</TH>
			</tr>

			<tr>
			<td width="10%">&nbsp;</td>
				<kul:htmlAttributeHeaderCell literalLabel="Place Of Publication"/>
				<td><input size="50" name="placeOfPublication" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=placeOfPublication%>" /></td>
			</tr>
			<tr>
			<td width="10%">&nbsp;</td>
				<kul:htmlAttributeHeaderCell literalLabel="Name Of Publisher"/>
				<td><input size="50" name="nameOfPublisher" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=nameOfPublisher%>" /></td>
			</tr>
			<tr>
			<td width="10%">&nbsp;</td>
				<kul:htmlAttributeHeaderCell literalLabel="Date Of Publication"/>
				<td><input size="50" name="dateOfPublication" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=dateOfPublication%>" /></td>
			</tr>
            <tr>
            <TH width="10%">&nbsp;</th>
			<TH width="40%" align="left" colspan="2">
			<kul:htmlAttributeHeaderCell literalLabel="Physical Distribution"/>
			</TH>
			</tr>
			<tr>
			<td width="10%">&nbsp;</td>
				<kul:htmlAttributeHeaderCell literalLabel="Extent"/>
				<td><input size="50" name="extent" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=extent%>" /></td>
			</tr>
			<tr>
			<td width="10%">&nbsp;</td>
				<kul:htmlAttributeHeaderCell literalLabel="Dimentions:"/>
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
				<kul:htmlAttributeHeaderCell literalLabel="General Note:"/>
				<td><input size="50" name="generalNote" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=generalNote%>" /></td>
			</tr>
            <tr>
            <TH width="10%">&nbsp;</th>
			<TH width="40%" align="left" colspan="2">
			<kul:htmlAttributeHeaderCell literalLabel="Subject Added entry-Tropical Term"/>
			</TH>
			</tr>
			<tr>
			<td width="10%">&nbsp;</td>
				<kul:htmlAttributeHeaderCell literalLabel="Tropcial Name Entry"/>
				<td><input size="50" name="tropcialNameEntry" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=tropicalNameEntry%>" /></td>
			</tr>
			<tr>
			<td width="10%">&nbsp;</td>
				<kul:htmlAttributeHeaderCell literalLabel="GeneralSubdivision:"/>
				<td><input size="50" name="generalSubdivision" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=generalSubdivision%>" /></td>
			</tr>
            <tr>
            <TH width="10%">&nbsp;</th>
			<TH width="40%" align="left" colspan="2">
			<kul:htmlAttributeHeaderCell literalLabel="Added entry-Personal Name"/>
			</TH>
			</tr>
			<tr>
			<td width="10%">&nbsp;</td>
				<kul:htmlAttributeHeaderCell literalLabel="PersonalName:"/>
				<td><input size="50" name="personalName" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=personalName%>" /></td>
			</tr>
            <tr>
            <TH width="10%">&nbsp;</th>
			<TH width="40%" align="left" colspan="2">
			<kul:htmlAttributeHeaderCell literalLabel="Added entry-Corporate Name"/>
			</TH>
			</tr>
			<tr>
			<td width="10%">&nbsp;</td>
				<kul:htmlAttributeHeaderCell literalLabel="Corporate Name:"/>
				<td><input size="50" name="corporatename" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=corporatename%>" /></td>
			</tr>
			<tr>
			<td width="10%">&nbsp;</td>
				<kul:htmlAttributeHeaderCell literalLabel="Subordinate Unit:"/>
				<td><input size="50" name="subordinateUnit" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=subordinateUnit%>" /></td>
			</tr>
            <tr>
            <TH width="10%">&nbsp;</th>
			<TH width="40%" align="left" colspan="2">
			<kul:htmlAttributeHeaderCell literalLabel="Others"/>
			</TH>
			</tr>
			<td width="10%">&nbsp;</td>
				<kul:htmlAttributeHeaderCell literalLabel="Price:"/>
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
				<kul:htmlAttributeHeaderCell literalLabel="Date (999-u):"/>
				<td><input size=20 name="dateMin" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=dateMin%>" />
									
					
						&nbsp;&nbsp;  to &nbsp;&nbsp;<input type="text" name="dateMax" value="<%=dateMax%>" size=20
					 />[MM/DD/YYYY]</td>
               
			</tr>
			<!-- ISBN,ISSN field definition -->
			<tr>
				<td width="10%">&nbsp;</td>
				<kul:htmlAttributeHeaderCell literalLabel="ISBN:"/>
				<td><input size=20 name="ISBN" type="text"
					onkeypress="return KeyPress('AdvancedSearch',event)"
					class="inputbox text" value="<%=ISBN%>" />
									
					
			</tr>
			 <tr>
			<td width="10%">&nbsp;</td>
				<kul:htmlAttributeHeaderCell literalLabel="ISSN"/>
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
		</fieldset>
		</td>
	</tr>
	<tr>
		<td>
		<p></p>
		</td>
	</tr>
	
</table>
</div>
	</kul:tabTop>
	<kul:panelFooter />
	<div id="globalbuttons" class="globalbuttons">
        <c:if test="${not readOnly}">	        
			<html:image src="${ConfigProperties.kr.externalizable.images.url}buttonsmall_submit.gif" property="methodToCall.search" title="Save" alt="Save"/>
	    	<html:image src="${ConfigProperties.kr.externalizable.images.url}buttonsmall_cancel.gif" styleClass="globalbuttons" property="methodToCall.start" title="Cancel" alt="Cancel"/>
        </c:if>		
    </div>

</kul:page>
