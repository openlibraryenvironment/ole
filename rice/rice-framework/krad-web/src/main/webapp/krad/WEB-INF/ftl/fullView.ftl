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
<@krad.html view=view>

    <@krad.script value="${KualiForm.growlScript!}"/>

<div id="Uif-Application" style="display:none;" class="uif-application">

    <!-- APPLICATION HEADER -->
    <#if view.applicationHeader?has_content>
        <#assign stickyDataAttribute=""/>
        <#if view.stickyApplicationHeader>
            <#assign stickyDataAttribute="data-sticky='true'"/>
        </#if>

        <div id="Uif-ApplicationHeader-Wrapper" ${stickyDataAttribute}>
            <@krad.template component=view.applicationHeader/>

            <!-- Backdoor info (here to inherit stickyness with the header, if set) -->
            <@krad.backdoor/>
        </div>
    <#else>
        <!-- Backdoor info -->
        <@krad.backdoor/>
    </#if>

    <@krad.form render=view.renderForm postUrl="${view.formPostUrl!KualiForm.formPostUrl}"
    onSubmitScript="${view.onSubmitScript!}" disableNativeAutocomplete=view.disableNativeAutocomplete>

        <#if view.renderForm>
        <#-- write out view, page id as hidden so the view can be reconstructed if necessary -->
            <@spring.formHiddenInput id="viewId" path="KualiForm.viewId"/>

        <#-- all forms will be stored in session, this is the conversation key -->
            <@spring.formHiddenInput id="formKey" path="KualiForm.formKey"/>

        <#-- original form key requested, may differ from actual form key-->
            <@spring.formHiddenInput id="requestedFormKey" path="KualiForm.requestedFormKey"/>

        <#-- tracks the session, used to determine timeouts -->
            <@spring.formHiddenInput id="sessionId" path="KualiForm.sessionId"/>

        <#-- flow key to maintain a history flow -->
            <@spring.formHiddenInput id="flowKey" path="KualiForm.flowKey"/>

        <#-- based on the view setting, form elements will be checked for dirtyness -->
            <@spring.formHiddenInput id="validateDirty" path="KualiForm.view.applyDirtyCheck"/>

        <#-- based on the view setting, form elements will be checked for dirtyness -->
            <@spring.formHiddenInput id="dirtyForm" path="KualiForm.dirtyForm"/>

        <#-- indicator which is set to true when content is being rendered inside a lightbox -->
            <@spring.formHiddenInput id="renderedInLightBox" path="KualiForm.renderedInLightBox"/>

        <#-- indicator for single page view, used to drive script page handling logic -->
            <@spring.formHiddenInput id="singlePageView" path="KualiForm.view.singlePageView"/>

        <#-- indicator for disabling browser caching of the view -->
            <@spring.formHiddenInput id="disableBrowserCache" path="KualiForm.view.disableBrowserCache"/>
        </#if>

        <@krad.template component=view/>
    </@krad.form>

    <@krad.script value="${KualiForm.lightboxScript!}"/>

<#-- set focus and perform jump to -->
    <#if KualiForm.view.currentPage?has_content>
        <@krad.script value="jQuery(document).on(kradVariables.PAGE_LOAD_EVENT, function(){
                    performFocusAndJumpTo(${KualiForm.view.currentPage.autoFocus?string}, true, true, '${KualiForm.focusId!}',
                        '${KualiForm.jumpToId!}', '${KualiForm.jumpToName!}');
                });" component=Component/>
    </#if>

</div>

<!-- APPLICATION FOOTER -->
    <#if view.applicationFooter?has_content>
        <#assign stickyFooterDataAttribute=""/>
        <#if view.stickyApplicationFooter>
            <#assign stickyFooterDataAttribute="data-sticky_footer='true'"/>
        </#if>

    <div id="Uif-ApplicationFooter-Wrapper" ${stickyFooterDataAttribute}>
        <@krad.template component=view.applicationFooter/>
    </div>
    </#if>

</@krad.html>
