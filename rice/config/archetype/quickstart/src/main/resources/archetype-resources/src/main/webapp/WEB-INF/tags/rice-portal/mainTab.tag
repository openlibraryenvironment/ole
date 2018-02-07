<%--
  ~ Copyright 2006-2012 The Kuali Foundation
  ~
  ~ Licensed under the Educational Community License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~ http://www.opensource.org/licenses/ecl2.php
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>

<%--
Kuali Rice ArcheType Help

This file is overrding a tag file from the rice portal in order to include
     custom application specific portal content.

To see other files that can be overriden, look at the org.kuali.rice:rice-web module.
--%>

<%@ include file="/rice-portal/jsp/sys/riceTldHeader.jsp"%>

<td class="content" valign="top">
  <mainChannel:customApplication />
</td>
<td class="content" valign="top">
<mainChannel:workflow />
<mainChannel:rules />
</td>
<td class="content" valign="top">
<mainChannel:notification />
</td>
