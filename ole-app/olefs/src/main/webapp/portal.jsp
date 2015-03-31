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
<%@ include file="/jsp/sys/kfsTldHeader.jsp" %>
<%@ page import="org.kuali.rice.core.api.config.property.ConfigContext" %>
<%@ page import="org.kuali.rice.krad.util.GlobalVariables" %>

<%
    String gotoUrl = null;
    String selectedTab = null;
    if (request.getQueryString() != null && request.getQueryString().indexOf("channelUrl") >= 0) {
        gotoUrl = request.getQueryString().substring(request.getQueryString().indexOf("channelUrl") + 11, request.getQueryString().length());
    } else if (request.getParameter("channelUrl") != null && request.getParameter("channelUrl").length() > 0) {
        gotoUrl = request.getParameter("channelUrl");
    }

    if (request.getParameter("selectedTab") != null && request.getParameter("selectedTab").length() > 0) {
        session.setAttribute("selectedTab", request.getParameter("selectedTab"));
    }
    request.setAttribute("gotoUrl", gotoUrl);


    String url = ConfigContext.getCurrentContextConfig().getProperty("ole.fs.portal.url");
    String riceUrl = ConfigContext.getCurrentContextConfig().getProperty("ole.fs.portal.url");
    String docStoreUrl = ConfigContext.getCurrentContextConfig().getProperty("ole.fs.docstore.services.tab.url");
    String discoveryUrl = ConfigContext.getCurrentContextConfig().getProperty("ole.fs.discovery.url");

    String feedbackUrl = ConfigContext.getCurrentContextConfig().getProperty("ole.provide.feedback.link");
    String feedbackUrlText = ConfigContext.getCurrentContextConfig().getProperty("ole.provide.feedback.link.text");
    String reportsUrl = ConfigContext.getCurrentContextConfig().getProperty("ole.reports.url");
    String user = "";
    if (GlobalVariables.getUserSession() != null) {
        user = GlobalVariables.getUserSession().getPrincipalName();
    }

    request.setAttribute("url", url);
    request.setAttribute("riceUrl", riceUrl);
    request.setAttribute("docStoreUrl", docStoreUrl);
    request.setAttribute("discoveryUrl", discoveryUrl);
    request.setAttribute("user", user);
    request.setAttribute("feedbackUrl", feedbackUrl);
    request.setAttribute("feedbackUrlText", feedbackUrlText);
    request.setAttribute("reportsUrl", reportsUrl);

%>

<portal:olePortalHeader/>
<portal:olePortalTabs selectedTab="${sessionScope.selectedTab}"/>
<portal:olePortalBody selectedTab="${sessionScope.selectedTab}"
                      channelTitle="${param.channelTitle}" channelUrl="${gotoUrl}"
                      environment="${url}" riceUrl="${riceUrl}"/>
<portal:portalBottom/>
