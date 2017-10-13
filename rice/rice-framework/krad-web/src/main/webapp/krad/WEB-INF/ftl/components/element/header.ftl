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
<#macro uif_header element>

    <#if element.headerStyleClassesAsString?has_content>
        <#local styleClass="class=\"${element.headerStyleClassesAsString}\""/>
    </#if>

    <#if element.headerTagStyle?has_content>
        <#local style="style=\"${element.headerTagStyle}\""/>
    </#if>

    <#if element.headerLevel?has_content>
        <#local headerOpenTag="<${element.headerLevel} ${style!} ${styleClass!}>"/>
        <#local headerCloseTag="</${element.headerLevel}>"/>
    </#if>

    <div class="clearfix uif-header-contentWrapper">

        <#-- upper group -->
        <@krad.template component=element.upperGroup/>

        <@krad.div component=element>

            <#if element.headerLevel?has_content && element.headerText?has_content && element.headerText != '&nbsp;'>

                ${headerOpenTag}
                <span class="uif-headerText-span">
                    <#-- rich message support -->
                    <#if element.richHeaderMessage?has_content>
                        <@krad.template component=element.richHeaderMessage/>
                    <#else>
                        ${element.headerText}
                    </#if>
                </span>

                <#if element.context?? && element.context['parent']?has_content>
                    <#local group=element.context['parent']/>
                    <@krad.template component=group.help/>
                </#if>

                ${headerCloseTag}

            </#if>

            <#-- right group -->
            <@krad.template component=element.rightGroup/>

        </@krad.div>

        <#-- lower group -->
        <@krad.template component=element.lowerGroup/>

    </div>

</#macro>