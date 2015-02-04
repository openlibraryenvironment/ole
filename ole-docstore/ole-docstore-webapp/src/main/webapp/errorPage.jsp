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
<%@ page isErrorPage="true" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.io.StringWriter" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%
	Logger log = LoggerFactory.getLogger(this.getClass());
	log.error("Unexpected error in Web Application",exception);
%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="./css/okc.css" />
</head>
<body>
	<br/>
	<table id="error" align="center" border="0" width="60%" height="60%" cellpadding="0" cellspacing="0">
		<tr>
			<td>
				<br/>
				Unexpected error occurred, please try later.				
				<br/>
				If the error persists, please contact <a href="mailto:appsupport@kuali.org">support team.</a>
				<br/>
<%
out.println("<!--");
StringWriter sw = new StringWriter();
PrintWriter pw = new PrintWriter(sw);
exception.printStackTrace(pw);
out.print(sw);
sw.close();
pw.close();
out.println("-->");
%>
			</td>
		</tr>
	</table>
	<br/>
</body>
</html>
