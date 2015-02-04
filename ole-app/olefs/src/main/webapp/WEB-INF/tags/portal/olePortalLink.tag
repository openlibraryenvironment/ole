<%--
 Copyright 2005-2009 The Kuali Foundation
 
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
<%@ include file="/jsp/sys/kfsTldHeader.jsp"%><%--

 --%><%@ attribute name="url" required="true" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="displayTitle" required="false" %>
<%@ attribute name="prefix" required="false" %>
<%@ attribute name="hiddenTitle" required="false" %>
<%@ attribute name="green" required="false" %>
<%@ attribute name="yellow" required="false" %>
<%@ attribute name="grey" required="false" %>
<%@ attribute name="openNewWindow" required="false" %>
<%@ attribute name="rice2" required="false" %>
<%@ attribute name="riceUrl" required="false" type="java.lang.String"%>
<%@ attribute name="docStoreUrl" required="false" type="java.lang.String"%>
<%@ attribute name="discoveryUrl" required="false" type="java.lang.String"%>
<%@ attribute name="user" required="false" %>

<c:set var="backdoorPortalUrlAddition" value="" />
<c:set var="backdoorMainUrlAddition" value="" />

<c:if test="${green}" >
 	<img src="images-portal/green.png"/>
</c:if>
<c:if test="${yellow}" >
 	<img src="images-portal/yellow.png"/>
</c:if>
<c:if test="${grey}" >
 	<img src="images-portal/grey.png"/>
</c:if>
<c:if test="${rice2}" >
 	<c:set var="prefix" value="${riceUrl}/"/>
</c:if>
<c:if test="${docStoreUrl}" >
 	<c:set var="docStoreUrl" value="${docStoreUrl}/"/>
</c:if>
<c:if test="${discoveryUrl}" >
 	<c:set var="discoveryUrl" value="${discoveryUrl}/"/>
</c:if>
<c:if test="${user}" >
 	<c:set var="prefix" value="${user}/"/>
</c:if>
<c:if test="${UserSession.backdoorInUse}">
	<%-- Can't add this.  If on the main (portal) request, it assumes this was a
	 backdoor login request and appends an additional parameter which causes some forms to blow 
	<c:set var="backdoorPortalUrlAddition" value="&backdoorId=${UserSession.principalName}" />
	 --%>
	<c:choose>
		<c:when test="${fn:contains(url,'?')}">
			<c:set var="backdoorMainUrlAddition" value="&backdoorId=${UserSession.principalName}" />
		</c:when>
		<c:otherwise>
			<c:set var="backdoorMainUrlAddition" value="?backdoorId=${UserSession.principalName}" />
		</c:otherwise>
	</c:choose>
</c:if>


<c:if test="${openNewWindow}" >
	<c:set var="href" value="javascript:openNewWindow('${url}' );" />
</c:if>
<c:if test="${!openNewWindow}" >
	<c:set var="href" value="${prefix}portal.do?channelTitle=${title}&channelUrl=${url}" />
</c:if>

<c:if test="${displayTitle}" >
  <a class="portal_link" href="${href}"  title="${title}">${title}</a>
</c:if>
<c:if test="${! displayTitle}" >
  <a class="portal_link" href="${href}" title="${title}"><jsp:doBody/></a>
</c:if>

<%-- <c:if test="${hiddenTitle}" >
 	<img src="images-portal/green.png"/><font color="#B0B0B0" >${title}</font>
</c:if>--%>

<c:if test="${hiddenTitle}" >
 	<font color="#606060" >${title}</font>
</c:if>

<script language="javascript">
function openNewWindow(url) {
	 popupWin = window.open(url);
	 }
</script>


