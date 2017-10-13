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

<tiles:useAttribute name="group" classname="org.kuali.rice.krad.uif.container.Group"/>

<krad:div component="${group}">

  <!-- first cell -->
  <div class="fl-container-flex15 fl-col padding-left padding-right border-right">
    <h4 class="ks-no-border">My Learning Plans</h4>
    <a href="_bottom">Primary</a>

    <p>NOTE: You can hav as many learning plans as you like, but one must always be primary. Some programs will require
      advisor approval on your primary plan.</p>
  </div>
  <!-- end first cell -->
  <!-- 2nd cell -->
  <div class="fl-container-flex15 fl-col padding-right  padding-left border-right">
    <h4 class="ks-no-border">Find Courses + Programs</h4>
    <ul class="ks-icon-list">
      <li class="ks-folder"><a href="_bottom">Saved Courses</a></li>
      <li class="ks-search"><a href="_bottom">Advanced Search</a></li>
      <li class="ks-bullets"><a href="_bottom">Explore Program Requirements</a></li>
    </ul>
  </div>
  <!-- end 2nd cell -->
  <!-- 3rd cell -->
  <div class="fl-container-flex15 fl-col padding-right padding-left border-right">
    <h4 class="ks-no-border">Schedule and Registration</h4>
    <ul class="ks-icon-list">
      <li class="ks-time-sensitive"><a href="_bottom">Schedule Builder</a></li>
      <li class="ks-edit"><a href="_bottom">Register for Classes</a></li>
    </ul>

  </div>
  <!-- end 3rd cell -->
  <!-- 4th cell -->
  <div class="fl-container-flex15 fl-col padding-right padding-left border-right">
    <h4 class="ks-no-border">Privacy + Sharing</h4>
    <ul class="ks-icon-list">
      <li class="ks-sharing"><a href="_bottom">Sharing</a></li>
      <li class="ks-forward"><a href="_bottom">Send to Advisor</a></li>
      <li class="ks-print"><a href="_bottom">Print / Download</a></li>
    </ul>

  </div>
  <!-- end 4th cell -->
  <!-- 5th cell -->
  <div class="fl-container-flex15 fl-col padding-left">
    <h4 class="ks-no-border">Help</h4>
    <ul class="ks-list-style-none">
      <li><a href="_bottom">Video Tutorial</a></li>
      <li><a href="_bottom">Schedul Builder</a></li>
      <li><a href="_bottom">1-page tutorial: Learning Plan</a></li>
      <li><a href="_bottom">1-page tutorial: Program Exploration</a></li>
      <li><a href="_bottom">FAQ</a></li>
      <li><a href="_bottom">Explore Program Requirements Forums</a></li>
    </ul>

  </div>
  <!-- end 5th cell -->
  <!-- float clear -->
  <div class="clearfix"></div>

</krad:div>