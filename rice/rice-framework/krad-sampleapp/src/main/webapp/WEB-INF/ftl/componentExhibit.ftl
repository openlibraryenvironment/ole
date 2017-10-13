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
<#macro uif_exhibit element>

    <@krad.template component=element.tabGroup/>

    <div id="demo-exhibitSource" style="display:none;">
        <#list element.demoSourceCode as sourceCode>
            <pre class="${element.sourceCodeViewer.pluginCssClass}"
                  data-index="${sourceCode_index}">${sourceCode}</pre>
        </#list>
    </div>

    <div id="demo-additionalExhibitSource1" style="display:none;">
        <#list element.additionalDemoSourceCode1 as sourceCode>
            <#if sourceCode?has_content>
                <pre class="${element.sourceCodeViewer.pluginCssClass}"
                      data-index="${sourceCode_index}">${sourceCode}</pre>
            </#if>
        </#list>
    </div>

    <div id="demo-additionalExhibitSource2" style="display:none;">
        <#list element.additionalDemoSourceCode2 as sourceCode>
            <#if sourceCode?has_content>
                <pre class="${element.sourceCodeViewer.pluginCssClass}"
                      data-index="${sourceCode_index}">${sourceCode}</pre>
            </#if>
        </#list>
    </div>

    <@krad.template component=element.sourceCodeViewer/>
    <@krad.template component=element.additionalSourceCodeViewer1/>
    <@krad.template component=element.additionalSourceCodeViewer2/>

    <@krad.script value="setupExhibitHandlers()" />
</#macro>
