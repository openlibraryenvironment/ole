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
<#macro uif_agendaTree widget componentId>

    <#-- KRAD doesn't support hidden input fields at present.  This is a workaround for it. -->
    <#if KualiForm.viewTypeName != 'MAINTENANCE'>
    <input type="hidden" name="dataObject.selectedAgendaItemId" value="${KualiForm.dataObject.selectedAgendaItemId!}"
           class="selectedAgendaItemId"/>
    </#if>

    <#--
    Invokes JS method to implement a tree plug-in.  see agendaTree.js
    -->
    <@krad.script value="initAgendaTree('${componentId}');"/>

</#macro>





