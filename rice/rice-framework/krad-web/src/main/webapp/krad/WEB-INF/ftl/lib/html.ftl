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
<#macro html view>

<!DOCTYPE HTML>
<html lang="en">
<head>

    <meta charset="UTF-8">

    <title>
        <@spring.message "app.title"/>
        <#if view.headerText?has_content>
        :: ${view.headerText}
        </#if>
    </title>

    <#list view.theme.cssFiles as cssFile>
        <#if cssFile?starts_with('http')>
            <link href="${cssFile}" rel="stylesheet" type="text/css"/>
        <#else>
            <link href="${request.contextPath}/${cssFile}" rel="stylesheet" type="text/css"/>
        </#if>
    </#list>

    <#list view.additionalCssFiles as cssFile>
        <#if cssFile?starts_with('http')>
            <link href="${cssFile}" rel="stylesheet" type="text/css"/>
        <#else>
            <link href="${request.contextPath}/${cssFile}" rel="stylesheet" type="text/css"/>
        </#if>
    </#list>


</head>

<body>
    <#nested/>

    <#list view.theme.scriptFiles as javascriptFile>
        <#if javascriptFile?starts_with('http')>
            <script type="text/javascript" src="${javascriptFile}"></script>
        <#else>
            <script type="text/javascript"
                    src="${request.contextPath}/${javascriptFile}"></script>
        </#if>
    </#list>

    <#list view.additionalScriptFiles as scriptFile>
        <#if scriptFile?starts_with('http')>
            <script type="text/javascript" src="${scriptFile}"></script>
        <#else>
            <script type="text/javascript"
                                        src="${request.contextPath}/${scriptFile}"></script>
        </#if>
    </#list>

    <!-- preload script (server variables) -->
    <#if view.preLoadScript?has_content>
        <script type="text/javascript">
            ${view.preLoadScript}
        </script>
    </#if>

    <!-- custom script for the view -->
    <#if view.onLoadScript?has_content>
        <script type="text/javascript">
            jQuery(document).ready(function () {
            ${view.onLoadScript}
            })
        </script>
    </#if>
</body>

</html>

</#macro>