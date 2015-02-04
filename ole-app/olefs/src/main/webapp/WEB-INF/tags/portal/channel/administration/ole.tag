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

<channel:portalChannelTop channelTitle="Kuali OLE Modules (Rice-2.0)"/>
<div class="body">

    <portal:portalLink displayTitle="true" title="Krms Builder"
                       url="${ConfigProperties.application.url}/ole-kr-krad/krmsbuildercontroller?viewId=KrmsBuilderView&methodToCall=start"/><br/>

    <%--<portal:portalLink displayTitle="true" title="Profile Builder" green="true"
                       url="${ConfigProperties.application.url}/ole-kr-krad/profilebuildercontroller?viewId=ProfileBuilderView&methodToCall=start"/><br/> --%>

    <portal:portalLink displayTitle="true" title="Staff Upload"
                       url="${ConfigProperties.application.url}/ole-kr-krad/staffuploadcontroller?viewId=StaffUploadView&methodToCall=start"/>
    <br/>

    <%--<portal:portalLink displayTitle="true" title="Marc Editor" green="true"
                       url="${ConfigProperties.application.url}/ole-kr-krad/marceditorcomponents?viewId=MarcEditorView&methodToCall=start"/>
    <br/>

    <portal:portalLink displayTitle="true" title="Dublin Editor" green="true"
                       url="${ConfigProperties.application.url}/ole-kr-krad/dublincomponents?viewId=DublinEditorView&methodToCall=start"/>
    <br/>--%>

   <%-- <portal:portalLink displayTitle="true" title="Instance Editor" green="true"
                       url="${ConfigProperties.application.url}/ole-kr-krad/instanceeditor?viewId=InstanceEditorView&methodToCall=start"/>
    <br/>--%>
    <portal:portalLink displayTitle="true" title="People Flow"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.rice.kew.impl.peopleflow.PeopleFlowBo&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/><br/>




</div>
<channel:portalChannelBottom/>