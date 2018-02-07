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
Standard HTML Hidden Input

-->

<#macro uif_hidden control field>

    <#local attributes='class="${control.styleClassesAsString!}"
         ${control.simpleDataAttributes!}'/>

    <#if control.style?has_content>
        <#local attributes='${attributes} style="${control.style}"'/>
    </#if>

    <@spring.formHiddenInput id="${control.id}" path="KualiForm.${field.bindingInfo.bindingPath}"
            attributes="${attributes}"/>

</#macro>