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
    Box Layout Manager:

      Places each component of the given list into a horizontal or vertical row.

      The amount of padding is configured by the 'padding'
      property of the layout manager. The padding is implemented by setting the margin of the wrapping
      span style. For vertical orientation, the span style is set to block.
 -->

<#macro uif_box items manager container>

    <#if manager.styleClassesAsString?has_content>
        <#local styleClass="class=\"${manager.styleClassesAsString}\""/>
    </#if>

    <#if manager.style?has_content>
        <#local style="style=\"${manager.style}\""/>
    </#if>

    <div id="${manager.id}_boxLayout" ${style!} ${styleClass!}>
        <#list items as item>
           <@krad.template component=item/>
       </#list>
    </div>

</#macro>