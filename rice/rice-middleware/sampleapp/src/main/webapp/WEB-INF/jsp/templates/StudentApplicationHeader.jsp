<%--

    Copyright 2005-2014 The Kuali Foundation

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
<%@ include file="/krad/WEB-INF/jsp/tldHeader.jsp" %>

<tiles:useAttribute name="element" classname="org.kuali.rice.krad.uif.element.Header"/>

<krad:div component="${element}">

  <h1>
    Learning
    <span class="white-text">Plan</span>
  </h1>

</krad:div>

<krad:template component="${KualiForm.view.breadcrumbs}"/>

<div id="ks-user-profile" class="fl-container-flex20 fl-col gutter-left">
  <span>Hi, Malik | <a href="_bottom">Logout</a></span>
  <img width="25px" height="25px" alt="user profile pic" src="${ConfigProperties['krad.externalizable.images.url']}ks/user_profile_pic.png">
</div>