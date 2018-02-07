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
<#macro demo_kradappheader element>

<div class="demo-appHeader">
    <div>
        <div class="demo-headerFooterContainer">
            <a href="${ConfigProperties['krad.url']}/kradsampleapp?viewId=KradSampleAppHome" class="demo-brand">
                <div class="demo-brandLogo">
                <#--Image placeholder-->
                </div>
            </a>

            <div>
                <ul class="demo-appNavigation">
                    <li><a id="Demo-HomeLink"
                           href="${ConfigProperties['krad.url']}/kradsampleapp?viewId=KradSampleAppHome">Home</a>
                    </li>
                    <li><a id="Demo-LibraryLink"
                           href="${ConfigProperties['krad.url']}/kradsampleapp?viewId=ComponentLibraryHome">Library</a>
                    </li>
                    <li><a id="Demo-DemoLink"
                           href="${ConfigProperties['krad.url']}/kradsampleapp?viewId=KradSampleAppDemo">Demo</a></li>
                    <li><a id="Demo-DownloadLink" href="http://kuali.org/download/rice">Download</a></li>
                </ul>
            </div>
        </div>
    </div>
</div>

</#macro>
