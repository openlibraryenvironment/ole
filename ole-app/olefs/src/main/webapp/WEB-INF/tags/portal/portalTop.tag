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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Kuali Portal Index</title>
    <c:forEach items="${fn:split(ConfigProperties.olePortal.css.files, ',')}" var="cssFile">
        <link href="${cssFile}" rel="stylesheet" type="text/css" />
    </c:forEach>
    <c:forEach items="${fn:split(ConfigProperties.portal.javascript.files, ',')}" var="javascriptFile">
        <script language="JavaScript" type="text/javascript" src="${ConfigProperties.application.url}/${javascriptFile}"></script>
    </c:forEach>
    <script language="javascript" >
        if (top.location != self.location) {
            top.location = self.location;
        }
    </script>
    <c:set var="feedbackUrl" value="${ConfigProperties.ole.provide.feedback.link}"/>
    <c:set var="feedbackText" value="${ConfigProperties.ole.provide.feedback.link.text}"/>

</head>
<body topmargin="0" leftmargin="0" marginheight="0" marginwidth="0">

<div id="header" title="Open Library Environment">
    <h1 class="kfs"></h1>Open Library Environment
</div>
<div id="feedback">
    <a class="portal_link" href="${feedbackUrl}" target="_blank" title="${feedbackText}">${feedbackText}</a>
</div>

<div id="build"> ${ConfigProperties.header.text} ${ConfigProperties.version} (${ConfigProperties.datasource.ojb.platform})</div>
    