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
<%@ page language="java" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%
	 String contextPath = request.getContextPath();
%>

<html:html locale="true">

<head>
<title>OLE DocStore Discovery<%--<tiles:get name="title" />--%></title>
<LINK href="/css/discovery.css" type=text/css rel=stylesheet>
</head>

<body topmargin="0" leftmargin="0" marginheight="0" marginwidth="0">

<tiles:get name="header" />
<table class="bodyTable" width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td valign="top">
    	<tiles:get name="body" />
    </td>
  </tr>
</table>

</div> <!-- close <div id="iframe_portlet_container_div"> from header.jsp -->

<tiles:get name="footer" />
</body>
</html:html>