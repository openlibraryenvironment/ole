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
    Renders text in a span tag
 -->

<#macro uif_message element>
<#--if there is messageComponentStructure specified use that because this is a rich message-->
    <#if element.messageComponentStructure?has_content>
        <#if element.generateSpan>
            <@krad.div component=element>
                <#list element.messageComponentStructure as messageElement>
                    <@krad.template component=messageElement/>
                </#list>
            </@krad.div>
        <#else>
            <#list element.messageComponentStructure as messageElement>
                <@krad.template component=messageElement/>
            </#list>
        </#if>
    <#else>
    <#--generate wrapping span if true-->
        <#if element.generateSpan>
            <@krad.span component=element>${element.messageText!}</@krad.span>
        <#else>
        ${element.messageText!}
        </#if>
    </#if>
</#macro>