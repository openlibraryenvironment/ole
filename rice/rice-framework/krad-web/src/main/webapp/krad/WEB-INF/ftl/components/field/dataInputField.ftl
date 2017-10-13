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
<#macro uif_dataInputField field>

    <#local readOnly=field.readOnly || !field.inputAllowed/>

    <@krad.div component=field>

        <@krad.fieldLbl field=field>

            <#if field.renderFieldset>
                <fieldset data-type="InputSet" aria-labelledby="${field.id}_label" id="${field.id}_fieldset">
                    <legend style="display: none">${field.label!}</legend>
            </#if>

            <#local quickfinderInputOnly=(field.widgetInputOnly!false) && ((field.quickfinder.dataObjectClassName)!"")?has_content />

            <#-- render field value (if read-only/quickfinder-input-only) or control (if edit) -->
            <#if readOnly || quickfinderInputOnly>

                <#local readOnlyDisplay>
                    <#if field.forcedValue?has_content>
                        ${field.forcedValue}
                    <#else>
                        <#-- display replacement display value if set -->
                        <#if field.readOnlyDisplayReplacement?has_content>
                             ${field.readOnlyDisplayReplacement}
                        <#else>
                            <#-- display actual field value -->
                            <@spring.bind path="KualiForm.${field.bindingInfo.bindingPath}"/>

                            <#-- check escape flag -->
                            <#if field.escapeHtmlInPropertyValue>
                                ${(spring.status.value?default(""))?html}
                            <#else>
                                ${(spring.status.value?default(""))}
                            </#if>

                            <#-- add display suffix value if set -->
                            <#if field.readOnlyDisplaySuffix?has_content>
                                 *-* ${field.readOnlyDisplaySuffix}
                            </#if>
                        </#if>
                    </#if>
                </#local>

                <#if field.multiLineReadOnlyDisplay>
                    <#local readOnlyDisplay="<pre>${readOnlyDisplay?trim?replace(' ','&nbsp;')}</pre>"/>
                </#if>

                <span id="${field.id}_control" class="uif-readOnlyContent">
                    <#-- render inquiry if enabled -->
                    <#if field.inquiry?has_content && field.inquiry.render>
                        <@krad.template component=field.inquiry componentId="${field.id}" body="${readOnlyDisplay}"
                          readOnly=field.readOnly/>
                    <#else>
                        ${readOnlyDisplay}
                    </#if>
                </span>

            <#else>

                <#-- render field instructional text -->
                <@krad.template component=field.instructionalMessage/>

                <#-- render control for input -->
                <@krad.template component=field.control field=field/>

            </#if>

            <#-- render field quickfinder -->
            <#if field.inputAllowed>
                <@krad.template component=field.quickfinder componentId="${field.id}"/>
            </#if>

            <#-- render field direct inquiry if field is editable and inquiry is enabled-->
            <#if !readOnly && (field.inquiry.render)!false>
                <@krad.template component=field.inquiry componentId="${field.id}" readOnly=field.readOnly/>
            </#if>

            <#-- render field help -->
            <@krad.template component=field.help/>

            <#if field.renderFieldset>
                </fieldset>
            </#if>

        </@krad.fieldLbl>

        <!-- placeholder for dynamic field markers -->
        <span id="${field.id}_markers"></span>

        <#if !readOnly>
            <#-- render error container for field -->
            <@krad.template component=field.validationMessages/>

            <#-- render field constraint -->
            <@krad.template component=field.constraintMessage/>
        </#if>

        <#-- render span and values for informational properties -->
        <span id="${field.id}_info_message"></span>

        <#if field.propertyNamesForAdditionalDisplay??>
            <#list field.propertyNamesForAdditionalDisplay as infoPropertyPath>
                <span id="${field.id}_info_${krad.cleanPath(infoPropertyPath)}" class="uif-informationalMessage">
                    <@spring.bind path="KualiForm.${infoPropertyPath}"/>
                     ${spring.status.value?default("")}
                </span>
            </#list>
        </#if>

        <#-- render field suggest if field is editable -->
        <#if !readOnly>
            <@krad.template component=field.suggest parent=field/>
        </#if>

        <#-- render hidden fields -->
        <#-- TODO: always render hiddens if configured? -->
        <#list field.additionalHiddenPropertyNames as hiddenPropertyName>
            <@spring.formHiddenInput id="${field.id}_h${hiddenPropertyName_index}"
            path="KualiForm.${hiddenPropertyName}"/>
        </#list>

        <#-- transform all text on attribute field to uppercase -->
        <#if !readOnly && field.control?? && field.uppercaseValue>
            <@krad.script value="uppercaseValue('${field.control.id}');"/>
        </#if>

    </@krad.div>

</#macro>


