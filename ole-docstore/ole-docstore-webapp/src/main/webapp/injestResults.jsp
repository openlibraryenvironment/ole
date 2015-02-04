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
<%@ page language="java" import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>

<html>
<head>
</head>
<body >

<%
String pageTitle="Ingest documents";
%>
<body topmargin="0" leftmargin="0" marginheight="0" marginwidth="0">

<%@ include file="oleHeader.jsp" %>	

	<table align="center" border="0" width="994px" height="85%" cellpadding="0" cellspacing="0">
	<tr height="98%" valign="top">
		<td>
              <table>
		      <%
                    Map uuidMap = (Map)request.getAttribute("mapResults");
                    String filename = (String)request.getAttribute("fileName");

                    for (Iterator<String> iterator = uuidMap.keySet().iterator(); iterator.hasNext();) {
                        String uuid = (String)uuidMap.get(iterator.next());
                    %>
                     <tr>
                        <td>
                            <%
                      out.println("Successfully ingested " + filename + "with UUID: " + uuid);
                            %>

                        </td>
                     </tr>

                    <%
                    }

		      %>
		      </table>
		</td>
	</tr>

</table>

	<br/>
</div> <!-- close <div id="iframe_portlet_container_div"> from newHeader.jsp -->
<%@ include file="oleFooter.jsp" %>
</body>
</html>
