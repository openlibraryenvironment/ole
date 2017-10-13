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
<#include "libInclude.ftl" parse=true/>

<#compress>

    <#if !KualiForm.ajaxRequest || (KualiForm.ajaxReturnType == "update-view")
        || (KualiForm.ajaxReturnType == "update-page")>
        <#global view=KualiForm.view!/>
    <#else>
        <#global view=KualiForm.postedView!/>
    </#if>

    <#if KualiForm.ajaxRequest && KualiForm.ajaxReturnType == "redirect">
        <div data-returntype="redirect">
            <#include "redirect.ftl" parse=true/>
        </div>
    <#elseif view?? && view.viewTemplates??>
        <#-- include all templates needed for the view -->
        <#list view.viewTemplates as viewTemplate>
            <#include "${viewTemplate}" parse=true/>
        </#list>
    </#if>

    <#if KualiForm.jsonRequest>
        <#include "${KualiForm.requestJsonTemplate}" parse=true/>
    <#elseif KualiForm.ajaxRequest>

        <#if KualiForm.ajaxReturnType == "update-view">
            <div data-returntype="update-view">
                <#include "fullView.ftl" parse=true/>
            </div>

        <#elseif KualiForm.ajaxReturnType == "update-component">
            <div data-returntype="update-component" data-updatecomponentid="${Component.id!}">
                <#include "updateComponent.ftl" parse=true/>
            </div>

        <#elseif KualiForm.ajaxReturnType == "update-page">
            <div data-returntype="update-page">
                <#include "updatePage.ftl" parse=true/>
            </div>

        <#elseif KualiForm.ajaxReturnType == "display-lightbox">
            <div data-returntype="display-lightbox">
                <#include "updateComponent.ftl" parse=true/>
            </div>

        <#elseif KualiForm.ajaxReturnType == "update-dialog">
            <div data-returntype="update-dialog" data-updatecomponentid="${Component.id!}">
                <#include "updateComponent.ftl" parse=true/>
            </div>
        </#if>

    <#else>
        <#include "fullView.ftl" parse=true/>
    </#if>

</#compress>