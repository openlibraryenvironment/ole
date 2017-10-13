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
<#macro uif_label element>

    <#local label="${element.labelText}"/>
    <#local colon=""/>

    <#if element.renderColon>
        <#local colon=":"/>
    </#if>

    <#if element.title?has_content>
        <#local title="title=\"${element.title}\""/>
    </#if>

    <@krad.span component=element>

    <#-- required message left -->
        <#if element.requiredMessagePlacement == 'LEFT'>
            <@krad.template component=element.requiredMessage/>
        </#if>

    <label id="${element.id}" for="${element.labelForComponentId!}" ${title!} ${element.simpleDataAttributes!}>
        <#if element.richLabelMessage?has_content>
            <@krad.template component=element.richLabelMessage/>${colon}
        <#else>
            ${label}${colon}
        </#if>
    </label>

    <#-- required message right -->
        <#if element.requiredMessagePlacement == 'RIGHT'>
            <@krad.template component=element.requiredMessage/>
        </#if>

    </@krad.span>

</#macro>