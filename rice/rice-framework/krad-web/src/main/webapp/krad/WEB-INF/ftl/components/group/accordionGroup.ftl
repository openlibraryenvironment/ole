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
<#macro uif_accordionGroup group params...>

    <@krad.groupWrap group=group>

        <#-- render items in list -->
        <ul id="${group.id}_accordList">
            <#list group.items as item>
                <li class="uif-accordionTab" data-tabfor="${item.id}">
                    <a href="#${item.id}_accordTitle">${item.header.headerText}</a>
                    <@krad.template component=item/>
                </li>
            </#list>
        </ul>

        <#-- render accordion widget -->
        <@krad.template component=group.accordionWidget parent=group/>

    </@krad.groupWrap>

</#macro>