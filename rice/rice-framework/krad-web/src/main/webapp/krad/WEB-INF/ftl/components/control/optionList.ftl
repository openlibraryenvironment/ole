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
<#--
Control for outputting options or selected options (KeyValues) as readOnly.  Also supports navigation options.

-->
<#macro uif_optionList control field>

    <#local attributes='class="${control.styleClassesAsString!}"
            tabindex="${control.tabIndex!}"  ${control.simpleDataAttributes!}'/>

    <#if control.disabled>
        <#local attributes='${attributes} disabled="disabled"'/>
    </#if>

    <#if control.style?has_content>
        <#local attributes='${attributes} style="${control.style}"'/>
    </#if>

    <#local isSelected=false>

    <#if field.propertyName?has_content>
        <#local path="KualiForm.${field.bindingInfo.bindingPath}"/>
        <@spring.bind path/>
    </#if>

    <#if control.options?has_content>
    <ul id="${control.id}" ${attributes}>
        <#list control.options as option>
        <#--check for key match if backed by property name-->
            <#if field.propertyName?has_content && spring.status.actualValue?has_content
            && spring.status.actualValue?is_sequence>
                <#local isSelected=spring.contains(spring.status.actualValue?default([""]), option.key)>
            <#elseif field.propertyName?has_content && spring.status.actualValue?has_content>
                <#local isSelected=spring.status.actualValue?string == option.key>
            </#if>

        <#--selected class-->
            <#if isSelected>
                <#local selectedItemCssClass=" ${control.selectedItemCssClass}"/>
            <#else>
                <#local selectedItemCssClass=""/>
            </#if>

        <#--Build items-->
            <#if isSelected ||(!isSelected && !control.showOnlySelected)>
                <li class="${control.itemCssClass}${selectedItemCssClass}">
                    <#if option.location?has_content && option.location.href?has_content>
                        <a data-key="${option.key?html}"
                           href="${option.location.href}">${option.value?html}</a>
                    <#else>
                        <span data-key="${option.key?html}">${option.value?html}</span>
                    </#if>
                </li>
            </#if>
        </#list>
    </ul>
    </#if>

</#macro>
