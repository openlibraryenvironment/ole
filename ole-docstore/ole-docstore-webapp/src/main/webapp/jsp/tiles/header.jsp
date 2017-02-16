<%@ page import="org.kuali.rice.core.api.config.property.ConfigContext" %>
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
	<link href="./css/portal-discovery.css" rel="stylesheet" type="text/css" />
<!--
	<script language="JavaScript" type="text/javascript" src="http://dev.oleproject.org/ole-dev/kr/scripts/my_common.js"></script>
-->

<script language="javascript" >
if (top.location != self.location) {
	top.location = self.location;
}
</script>

 <div id="header" title="Kuali Open Library Environment">
    <h1 class="kfs"></h1>
  </div>
  <div id="feedback">
  	<a class="portal_link" href="#"
  	title="Provide Feedback">Provide Feedback</a>
  </div>
    <%
        String url = ConfigContext.getCurrentContextConfig().getProperty("ole.fs.url.base");
    %>
  <div id="build">(rev: 749) 2011-06-02_05-00-46 (Oracle9i)</div>
<div id="tabs" class="tabposition">
	<ul>
		<li class="red"><a class="red" href="./discovery"	title="Main Menu" id="discovery" onclick="show()">Document Store Discovery</a></li>
<!-- 		<li class="red"><a class="red" href="./?tab=services"	title="Main Menu" id="services" onclick="hide()">Services</a></li> -->
        <li class="red"><a class="red" href="./discovery.do" title="Main Menu" onclick="hide()">Search</a></li>
<!--
        <li class="red"><a class="red" href="restfulAPI.jsp" title="RESTful API">RESTful API</a></li>
-->
        <li class="red"><a class="red" href="<%=url%>"  title="Main Menu" >OLE</a></li>
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
<div id="login-info">   <strong>You are not logged in.</strong>   </div>
-->
<div id="iframe_portlet_container_div">
	<br/>
