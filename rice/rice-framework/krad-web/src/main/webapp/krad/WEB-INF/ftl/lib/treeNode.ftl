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
<#macro treeNode node>

    <li id="${node.data.id}" class="${node.nodeType!}">
        <a href="#" class="${node.nodeType!}">
            <@krad.template component=node.nodeLabel/>
        </a>

        <@krad.template component=node.data/>

        <#if node.children?? && (node.children?size gt 0)>
            <ul>
                <#list node.children as childNode>
                    <@krad.treeNode node=childNode/>
                </#list>
            </ul>
        </#if>
    </li>

</#macro>