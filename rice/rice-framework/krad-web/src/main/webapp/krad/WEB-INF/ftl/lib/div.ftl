<#--

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

-->
<#macro div component>
	<#-- NOTICE: By KULRICE-10353 this method is duplicated, but not replaced, by
			org.kuali.rice.krad.uif.freemarker.FreeMarkerInlineRenderUtils.renderOpenDiv() and
			org.kuali.rice.krad.uif.freemarker.FreeMarkerInlineRenderUtils.renderCloseDiv().
			When updating this template, also update those methods. -->

  <div id="${component.id!}" ${krad.attrBuild(component)} ${component.simpleDataAttributes}>
    <#nested/>
  </div>

</#macro>