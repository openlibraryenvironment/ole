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
<%@ page language="java" import="org.kuali.ole.OleDocStoreData" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>

<jsp:useBean id="repositoryBrowser" class="org.kuali.ole.RepositoryBrowser" scope="session"/>

<html>
<head>
</head>
<%
    String pageTitle = "Summary of Document Categories and Formats";
%>
<body topmargin="0" leftmargin="0" marginheight="0" marginwidth="0">

<%@ include file="oleHeader.jsp" %>

<table border="0" width="70%" height="50%" align="center" cellspacing="0" cellpadding="0">
    <tr valign="top" bgcolor="#FFFFFF">
        <th align="left"><%out.println("Category"); %></th>
        <th align="left"><%out.println("Type"); %></th>
        <th align="left"><%out.println("Format"); %></th>
        <th align="left"><%out.println("File count"); %></th>
    </tr>
    <%
        List<OleDocStoreData> filesCount = repositoryBrowser.getFilesCount();
        for (Iterator<OleDocStoreData> iterator = filesCount.iterator(); iterator.hasNext(); ) {
	        OleDocStoreData oleDocStoreData = iterator.next(); %>
		    <tr valign="top" bgcolor="#FFFFFF">
		        <td class="content" valign="top"><% out.println(oleDocStoreData.getCategory()); %></td>
		        <td class="content" valign="top"></td>
		        <td class="content" valign="top"></td>
		    </tr>
		
		    <%Map<String, Map<String, Long>> typeFormatMapWithNodeCount = oleDocStoreData.getTypeFormatMapWithNodeCount();
		    %>
		    <%
	        Set<String> typeKeys = typeFormatMapWithNodeCount.keySet();
	        for (Iterator<String> stringIterator = typeKeys.iterator(); stringIterator.hasNext(); ) {
	            String type = stringIterator.next();
	    		%>
	    		<%
	            Map<String, Long> formatMap = typeFormatMapWithNodeCount.get(type);
	            Set<String> formats = formatMap.keySet();
	            for (Iterator<String> iterator1 = formats.iterator(); iterator1.hasNext(); ) {
	                String format = iterator1.next();
	     		%>   
			        <tr valign="top" bgcolor="#FFFFFF">
				        <td class="content" valign="top"></td>
				        <td class="content" valign="top"><%out.println(type);%></td>
				        <td class="content" valign="top"><%out.println(format);%></td>    
				        <td class="content" valign="top"><%out.println(formatMap.get(format));%></td>        
			        </tr> 	
	    		<% } 
	          } 
       }%>   
   
</table>

</div> <!-- close <div id="iframe_portlet_container_div"> from newHeader.jsp -->
<%@ include file="oleFooter.jsp" %>
</body>
</html>
