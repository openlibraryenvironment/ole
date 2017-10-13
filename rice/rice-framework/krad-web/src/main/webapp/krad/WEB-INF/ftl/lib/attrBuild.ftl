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
<#-- Can be called by templates that are building HTML tags to build the standard attributes
such as class and style. This tag checks whether the component actually
has a value for these settings before building up the attribute. This makes the outputted html
cleaner and actually prevents problems from having any empty attribute value in some cases (like
for style. The attribute strings can be referenced by the calling template through the exported
variables -->

<#function attrBuild component>
	<#-- NOTICE: By KULRICE-10353 this method is duplicated, but not replaced, by
			org.kuali.rice.krad.uif.freemarker.FreeMarkerInlineRenderUtils.renderAttrBuild().
			When updating this template, also update that method. -->

    <#if component.styleClassesAsString?has_content>
        <#local styleClass="class=\"${component.styleClassesAsString}\""/>
    </#if>

    <#if component.style?has_content>
        <#local style="style=\"${component.style}\""/>
    </#if>

    <#if component.title?has_content>
        <#local title="title=\"${component.title}\""/>
    </#if>

    <#return "${styleClass!} ${style!} ${title!}">

</#function>