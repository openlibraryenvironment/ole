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
<#macro uif_breadcrumbs widget page>

    <#local options=page.breadcrumbOptions/>

<ol ${krad.attrBuild(widget)} role="navigation">

<#-- process homewardPath breadcrumbs -->
    <#if options.renderHomewardPathBreadcrumbs && options.homewardPathBreadcrumbs?has_content>
    <#list options.homewardPathBreadcrumbs as crumb>
        <@krad.template component=crumb breadcrumbsWidget=widget/>
    </#list>
</#if>

    <#-- process parent locations (if set) -->
    <#if !widget.usePathBasedBreadcrumbs && KualiForm.view.parentLocation?has_content &&
KualiForm.view.parentLocation.resolvedBreadcrumbItems?has_content && options.renderParentLocations>
    <#list KualiForm.view.parentLocation.resolvedBreadcrumbItems as crumb>
        <@krad.template component=crumb breadcrumbsWidget=widget/>
    </#list>
</#if>

    <#-- process path based breadcrumbs (if set)-->
    <#if widget.usePathBasedBreadcrumbs && KualiForm.view.pathBasedBreadcrumbs?has_content>
    <#list KualiForm.view.pathBasedBreadcrumbs as crumb>
        <@krad.template component=crumb breadcrumbsWidget=widget/>
    </#list>
</#if>

    <#if options.breadcrumbOverrides?has_content>
<#-- process only the breadcrumb overrides (if set) -->
    <#list options.breadcrumbOverrides as crumb>
        <@krad.template component=crumb breadcrumbsWidget=widget/>
    </#list>
<#else>

<#-- preView Breadcrumbs -->
    <#if options.renderPreViewBreadcrumbs && options.preViewBreadcrumbs?has_content>
        <#list options.preViewBreadcrumbs as crumb>
            <@krad.template component=crumb breadcrumbsWidget=widget/>
        </#list>
    </#if>

<#-- View Breadcrumb -->
    <#if options.renderViewBreadcrumb && KualiForm.view.breadcrumbItem?has_content>
        <@krad.template component=KualiForm.view.breadcrumbItem breadcrumbsWidget=widget/>
    </#if>

<#-- prePage Breadcrumbs -->
    <#if options.renderPrePageBreadcrumbs && options.prePageBreadcrumbs?has_content>
        <#list options.prePageBreadcrumbs as crumb>
            <@krad.template component=crumb breadcrumbsWidget=widget/>
        </#list>
    </#if>

<#-- Page Breadcrumb -->
    <#if page.breadcrumbItem?has_content>
        <@krad.template component=page.breadcrumbItem breadcrumbsWidget=widget/>
    </#if>
</#if>

</ol>

    <@krad.script value="setupBreadcrumbs(${widget.displayBreadcrumbsWhenOne?string});" />

</#macro>



