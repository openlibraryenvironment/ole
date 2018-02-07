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
    Element that creates a table element and then invokes datatables to complete
    the table based on configured source data
 -->

<#macro uif_dataTable element>

    <#if element.styleClassesAsString?has_content>
        <#local styleClass="class=\"${element.styleClassesAsString}\""/>
    </#if>

    <#if element.style?has_content>
        <#local style="style=\"${element.style}\""/>
    </#if>

    <table id="${element.id}" ${style!} ${styleClass!}>
    </table>

    <@krad.script value="createTable('${element.id}', ${element.richTable.templateOptionsJSString}); "/>

</#macro>