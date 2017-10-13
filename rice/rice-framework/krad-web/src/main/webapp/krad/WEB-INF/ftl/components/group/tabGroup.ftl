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
<#macro uif_tabGroup group>

    <@krad.groupWrap group=group>

        <div id="${group.id}_tabs">

            <#-- render items in list -->
            <ul id="${group.id}_tabList">
                <#list group.items as item>
                    <li data-tabfor="${item.id}">
                        <a href="#${item.id}_tab">${(item.header.headerText)}</a>
                    </li>
                </#list>
            </ul>

            <#list group.items as item>
                <div data-tabwrapperfor="${item.id}" data-type="TabWrapper" id="${item.id}_tab">
                    <@krad.template component=item/>
                </div>
            </#list>
        </div>

        <#-- render tabs widget -->
        <@krad.template component=group.tabsWidget parent=group/>

    </@krad.groupWrap>

</#macro>


