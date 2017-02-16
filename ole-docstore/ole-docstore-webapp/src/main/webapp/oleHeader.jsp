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

<%@ page import="org.kuali.rice.core.api.config.property.ConfigContext" %>

<link href="./css/portal.css" rel="stylesheet" type="text/css"/>
<!--
<script language="JavaScript" type="text/javascript" src="http://dev.oleproject.org/ole-dev/kr/scripts/my_common.js"></script>
-->

<script language="javascript">
    if (top.location != self.location) {
        top.location = self.location;
    }

</script>

<div id="header" title="Kuali Open Library Environment">
    <h1 class="kfs"></h1>
</div>
<%
    String feedbackText=ConfigContext.getCurrentContextConfig().getProperty("app.feedback.linkText");
    String feedbackUrl=ConfigContext.getCurrentContextConfig().getProperty("app.feedback.link");
%>
<div id="feedback">
    <a class="portal_link" href="<%=feedbackUrl%>" target="_blank"
       title="Provide Feedback"><%=feedbackText%></a>
</div>

<div id="build">${project.version} :: ${kuali.build.timestamp}</div>
<div id="tabs" class="tabposition">
    <ul>
        <li class="red"><a class="red" href="." title="Main Menu" onclick="show()">Document Store</a></li>



    </ul>
</div>

<div class="header2">
    <div class="header2-left-focus">
        <div class="breadcrumb-focus">
            <!--
              <a class="portal_link" href="#" title="Action List">
                       <img src="images/ole/icon-port-actionlist.gif" alt="action list" width="91" height="19" border="0"></a>
              <a class="portal_link" href="#" title="Document Search">
                    <img src="images/ole/icon-port-docsearch.gif" alt="doc search" width="96" height="19" border="0"></a>
            -->

        </div>
    </div>
</div>
<!--
<div id="login-info"> <strong>You are not logged in.</strong> </div>
-->
<div id="iframe_portlet_container_div">
    <br/>
        <%
if ((null != pageTitle) && (pageTitle.length() > 0)) {
%>
    <h1><font size="3"><%=pageTitle%>
    </font></h1>
    <br/>
    <br/>
        <%
}
%>
