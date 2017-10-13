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
Create pre tags containing the text to highlight adding the css class used by the plugin

-->
<#macro uif_syntaxHighlighter widget >

<div id="${widget.id}">
    <#if widget.header?has_content>
        <@krad.template component=widget.header/>
    </#if>
    <div class="uif-syntaxHighlighter">
        <#if widget.allowCopy>
            <a class="uif-copyPaste" id="${widget.id}_syntaxHighlightCopy"></a>
        </#if>
        <pre class="${widget.pluginCssClass}">
            <#if widget.sourceCode?has_content>
                    ${widget.sourceCode}
                </#if>
        </pre>
    </div>
    <#if widget.sourceCode?has_content>
        <div id="${widget.id}_syntaxOriginalText" style="display: none;">${widget.sourceCode}</div>
    </#if>
</div>

    <@krad.script value="prettyPrint();"/>
    <#if widget.allowCopy>
        <@krad.script value="createCopyToClipboard('${widget.id}', '${widget.id}_syntaxHighlightCopy',
            '${widget.id}_syntaxOriginalText', ${widget.showCopyConfirmation?string})" />
    </#if>
</#macro>


