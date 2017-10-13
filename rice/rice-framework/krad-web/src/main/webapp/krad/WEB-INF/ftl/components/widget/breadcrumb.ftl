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
<#macro uif_breadcrumb element breadcrumbsWidget>

    <#local id=""/>

    <#if element.id?has_content>
        <#local id="id=\"${element.id}\""/>
    </#if>

    <#if element.render && element.label?has_content && element.label != "&nbsp;">
    <li>
        <#if element.renderAsLink>
            <a ${id} data-role="breadcrumb" href="${element.url.href}" ${krad.attrBuild(element)}>${element.label}</a>
        <#else>
            <span data-role="breadcrumb" ${id} ${krad.attrBuild(element)}>${element.label}</span>
        </#if>

        <#if element.siblingBreadcrumbComponent?has_content && element.siblingBreadcrumbComponent.render>
            <a class="uif-breadcrumbSiblingLink">&#9660;</a>
            <div class="uif-breadcrumbSiblingContent" style="display: none;">
                <@krad.template component=element.siblingBreadcrumbComponent/>
            </div>
        </#if>
    </li>
    </#if>

</#macro>