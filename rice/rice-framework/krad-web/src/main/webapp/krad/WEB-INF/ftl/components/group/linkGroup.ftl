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
<#macro uif_linkGroup group>

    <@krad.groupWrap group=group>

        <#if !group.items?? || (group.items?size lt 1)>
            ${group.emptyLinkGroupString}
        <#else>
            <#if group.groupBeginDelimiter?has_content>
                ${group.groupBeginDelimiter}
            </#if>

            <#list group.items as item>
                <@krad.template component=item/>
                <#if group.linkSeparator?has_content && item_has_next>
                    ${group.linkSeparator}
                </#if>
            </#list>

            <#if group.groupEndDelimiter?has_content>
                ${group.groupEndDelimiter}
            </#if>
        </#if>

    </@krad.groupWrap>

</#macro>

