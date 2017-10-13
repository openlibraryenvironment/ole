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
    Standard HTML Image element

 -->

<#macro uif_image element>

    <#if element.height?has_content>
        <#local height='height="${element.height}"'/>
    </#if>

    <#-- render caption header above -->
    <#if element.captionHeader?? && element.captionHeader.headerText?has_content
         && element.captionHeaderPlacementAboveImage>
        <@krad.template component=element.captionHeader/>
    </#if>

    <img id="${element.id}" src="${element.source!}" alt="${element.altText!}"
         ${height!} ${krad.attrBuild(element)} ${element.simpleDataAttributes!}/>

    <#-- render caption header above -->
    <#if element.captionHeader?? && element.captionHeader.headerText?has_content
         && !element.captionHeaderPlacementAboveImage>
        <@krad.template component=element.captionHeader/>
    </#if>

    <#-- render cutline text -->
    <#if element.cutlineMessage?? && element.cutlineMessage.messageText?has_content>
        <@krad.template component=element.cutlineMessage/>
    </#if>

</#macro>
