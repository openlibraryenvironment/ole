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
    Standard HTML Iframe element

 -->

<#macro uif_iframe element>

    <#local attributes=""/>

    <#if element.width?has_content>
        <#local attributes='${attributes} width="${element.width}"'/>
    </#if>

    <#if element.height?has_content>
        <#local attributes='${attributes} height="${element.height}"'/>
    </#if>

    <#if element.frameborder?has_content>
        <#local attributes='${attributes} frameborder="${element.frameborder}"'/>
    </#if>

    <#if element.title?has_content>
        <#local attributes='${attributes} title="${element.title}"'/>
    </#if>

    <iframe id="${element.id}" src="${element.source}" ${attributes} ${element.simpleDataAttributes}>
    </iframe>

</#macro>