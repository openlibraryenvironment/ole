<%@ page import="org.kuali.rice.core.api.config.property.ConfigContext" %>
<%--
 Copyright 2005 The Kuali Foundation
 
 Licensed under the Educational Community License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.opensource.org/licenses/ecl2.php
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
--%>
<%@ include file="/jsp/sys/kfsTldHeader.jsp"%>
<%
   String gotoUrl = null;
   String selectedTab = null;
   if (request.getQueryString() != null && request.getQueryString().indexOf("channelUrl") >= 0) {
      gotoUrl = request.getQueryString().substring(request.getQueryString().indexOf("channelUrl")+11,request.getQueryString().length());
   } else if (request.getParameter("channelUrl") != null && request.getParameter("channelUrl").length() > 0) {
      gotoUrl = request.getParameter("channelUrl");
   }
   
   if (request.getParameter("selectedTab") != null && request.getParameter("selectedTab").length() > 0) {
       session.setAttribute("selectedTab", request.getParameter("selectedTab"));
   }
    String reportsUrl = ConfigContext.getCurrentContextConfig().getProperty("ole.reports.url");

    request.setAttribute("gotoUrl", gotoUrl);
    request.setAttribute("reportsUrl", reportsUrl);
%>
<c:choose>
	<c:when test="${ConfigProperties.environment == 'local'}">
		<c:set var="url" scope="request" value="http://localhost:8080/ole-local/portal.jsp" />
	</c:when>
	<c:when test="${ConfigProperties.environment == 'dev'}">
		<c:set var="url" scope="request" value="http://dev.oleproject.org/ole-dev/portal.jsp" />
	</c:when>
	<c:when test="${ConfigProperties.environment == 'tst'}">
		<c:set var="url" scope="request" value="http://tst.oleproject.org/ole-tst/portal.jsp" />
	</c:when>
	<c:when test="${ConfigProperties.environment == 'demo'}">
		<c:set var="url" scope="request" value="http://demo.oleproject.org/ole-demo/portal.jsp" />
	</c:when>
	<c:otherwise>
		<c:set var="url" scope="request" value="" />
	</c:otherwise>
</c:choose>

<portal:portalTop />
<portal:portalTabs selectedTab="${sessionScope.selectedTab}" />
<portal:portalBody selectedTab="${sessionScope.selectedTab}"
	channelTitle="${param.channelTitle}" channelUrl="${gotoUrl}" environment="${url}"/>
<portal:portalBottom />
