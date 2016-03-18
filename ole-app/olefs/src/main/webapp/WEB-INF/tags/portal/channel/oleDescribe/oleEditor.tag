<%--
 Copyright 2007-2009 The Kuali Foundation

 Licensed under the Educational Community License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.opensource.org/licenses/ecl2.php

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
--%>
<%@ include file="/rice-portal/jsp/sys/riceTldHeader.jsp" %>

<channel:portalChannelTop channelTitle="Create/Edit Titles, Holdings or Items"/>
<div class="body">


    <%--<portal:portalLink displayTitle="true" title="Call Number Browse"
                       url="${ConfigProperties.application.url}/ole-kr-krad/callnumberBrowseController?viewId=CallNumberBrowseView&methodToCall=start"/> <br/>
 --%>   <%--<portal:portalLink displayTitle="true" title="Describe Workbench"--%>
                       <%--url="${ConfigProperties.application.url}/ole-kr-krad/describeworkbenchcontroller?viewId=DescribeWorkBenchView&methodToCall=start"/> <br/>--%>
    <portal:portalLink displayTitle="true" title="Dublin Core Editor"
                       url="${ConfigProperties.application.url}/ole-kr-krad/editorcontroller?viewId=EditorView&methodToCall=load&docCategory=work&docType=bibliographic&docFormat=dublinunq&editable=true"/> <br/>
    <portal:portalLink displayTitle="true" title="Marc Editor"
                       url="${ConfigProperties.application.url}/ole-kr-krad/editorcontroller?viewId=EditorView&methodToCall=load&docCategory=work&docType=bibliographic&docFormat=marc&editable=true"/> <br/>
    <portal:portalLink displayTitle="true" title="Marc Editor Workform"
                           url="${ConfigProperties.application.url}/ole-kr-krad/editorcontroller?viewId=EditorWorkformView&methodToCall=load&docCategory=work&docType=bibliographic&docFormat=marc&editable=true"/> <br/>
    <portal:portalLink displayTitle="true" title="Global Edit of Holdings/Items/EHoldings"
                       url="${ConfigProperties.application.url}/ole-kr-krad/globaleditController?viewId=GlobalEditView&methodToCall=start"/> <br/>

        <portal:portalLink displayTitle="true" title="New Search Workbench"
                           url="${ConfigProperties.application.url}/oleNgSearch.jsp"/> <br/>












<%-- <portal:portalLink displayTitle="true" title="Bound-with's"
                       url="${ConfigProperties.application.url}/ole-kr-krad/boundwithController?viewId=BoundwithView&methodToCall=start"/> <br/>

    <portal:portalLink displayTitle="true" title="Import Bib"
                       url="${ConfigProperties.application.url}/ole-kr-krad/importBibController?viewId=ImportBibView&methodToCall=start"/> <br/>



    <portal:portalLink displayTitle="true" title="Transfer"
                       url="${ConfigProperties.application.url}/ole-kr-krad/transferController?viewId=TransferView&methodToCall=start"/> <br/>

--%>

</div>
<channel:portalChannelBottom/>
