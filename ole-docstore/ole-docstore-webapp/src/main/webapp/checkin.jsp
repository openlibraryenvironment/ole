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
<html>
<head>
</head>
<%
String pageTitle="Check-in document";
%>
<body topmargin="0" leftmargin="0" marginheight="0" marginwidth="0">

<%@ include file="oleHeader.jsp" %>
	<table align="center" border="0" width="994px" height="85%" cellpadding="0" cellspacing="0">
	<tr height="98%" valign="top">
		<td>
		     <form action="checkin" method="POST" enctype="multipart/form-data">
			  <table>
			   <tr>
			    <td>&nbsp;</td>
			   </tr>
			    <tr>
			        <td>
			        File UUID to be checked in:
			        </td>
			        <td>
			            <input type="text" name="uuid"/>
			        </td>
			    </tr>
			        <td></td>
			        <td>
			            <input type="submit" value="submit"/>
			        </td>
			    </tr>
			  </table>
			  </form>
		</td>
	</tr>

</table>

	<br/>
</div> <!-- close <div id="iframe_portlet_container_div"> from newHeader.jsp -->
<%@ include file="oleFooter.jsp" %>
</body>
</html>
