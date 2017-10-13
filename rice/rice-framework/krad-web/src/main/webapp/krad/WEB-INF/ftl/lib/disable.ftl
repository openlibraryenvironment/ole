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
<#macro disable control type>

    <#if control.disabledConditionJs?has_content && control.disabledConditionControlNames?has_content>
        <#list control.disabledConditionControlNames as cName>
            <@krad.script value="var condition = function(){return (${control.disabledConditionJs});};
                  setupDisabledCheck('${cName?js_string}', '${control.id}', '${type}', condition,
                  ${control.evaluateDisabledOnKeyUp?string});"/>
        </#list>
    </#if>

    <#if control.disabledWhenChangedPropertyNames?has_content>
        <#list control.disabledWhenChangedPropertyNames as cName>
            <@krad.script value="
                  setupDisabledCheck('${cName?js_string}', '${control.id}', '${type}', function(){return true;},
                  ${control.evaluateDisabledOnKeyUp?string});"/>
        </#list>
    </#if>

    <#if control.enabledWhenChangedPropertyNames?has_content>
        <#list control.enabledWhenChangedPropertyNames as cName>
            <@krad.script value="
                  setupDisabledCheck('${cName?js_string}', '${control.id}', '${type}', function(){return false;},
                  ${control.evaluateDisabledOnKeyUp?string});"/>
        </#list>
    </#if>

</#macro>