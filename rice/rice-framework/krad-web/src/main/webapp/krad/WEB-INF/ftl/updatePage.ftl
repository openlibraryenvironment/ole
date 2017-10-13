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
<html>
    <#-- now render the updated component (or page) wrapped in an update div -->
    <div id="page_update">
        <#list view.viewTemplates as viewTemplate>
            <#include "${viewTemplate}" parse=true/>
        </#list>

            <#-- rerun view pre-load script to get new state variables for component -->
            <@krad.script value="${view.preLoadScript!}" component=Component/>

            <@krad.template componentUpdate=true component=Component/>

            <@krad.script value="${KualiForm.lightboxScript!}" component=Component/>

            <#-- show added growls -->
            <@krad.script value="${KualiForm.growlScript!}" component=Component/>

            <#-- set focus and perform jump to -->
            <@krad.script value="jQuery(document).on(kradVariables.PAGE_LOAD_EVENT, function(){
                    performFocusAndJumpTo(${view.currentPage.autoFocus?string}, true, true, '${KualiForm.focusId!}',
                        '${KualiForm.jumpToId!}', '${KualiForm.jumpToName!}');
                    dirtyFormState.setDirty(${KualiForm.dirtyForm?string});
                 });" component=Component/>
    </div>
</html>
