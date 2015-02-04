<%--
 Copyright 2007 The Kuali Foundation
 
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
<%@ include file="/jsp/sys/kfsTldHeader.jsp"%>

<channel:portalChannelTop channelTitle="System Admin" />
<div class="body">
	<strong>LOCATIONS</strong><br/>
    <br/>
        <portal:portalLink displayTitle="true" title="Campus" url="${ConfigProperties.kr.url}/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.rice.location.impl.campus.CampusBo&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
  		<portal:portalLink displayTitle="true" title="County" url="${ConfigProperties.kr.url}/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.rice.location.impl.county.CountyBo&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /> <br/>
        <portal:portalLink displayTitle="true" title="Country" url="${ConfigProperties.kr.url}/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.rice.location.impl.country.CountryBo&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
        <portal:portalLink displayTitle="true" title="Postal Code" url="${ConfigProperties.kr.url}/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.rice.location.impl.postalcode.PostalCodeBo&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
        <portal:portalLink displayTitle="true" title="State" url="${ConfigProperties.kr.url}/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.rice.location.impl.state.StateBo&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
    <br/>
  <strong>REFERENCE</strong> <br/>
    <br/>
        <portal:portalLink displayTitle="true" title="Address Type" url="${ConfigProperties.kr.url}/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.rice.kim.impl.identity.address.EntityAddressTypeBo&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
        <portal:portalLink displayTitle="true" title="Affiliation Type" url="${ConfigProperties.kr.url}/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.rice.kim.impl.identity.affiliation.EntityAffiliationTypeBo&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
        <portal:portalLink displayTitle="true" title="Campus Type" url="${ConfigProperties.kr.url}/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.rice.location.impl.campus.CampusTypeBo&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
        <portal:portalLink displayTitle="true" title="Citizenship Status" url="${ConfigProperties.kr.url}/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.rice.kim.impl.identity.citizenship.EntityCitizenshipStatusBo&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
        <portal:portalLink displayTitle="true" title="Email Type" url="${ConfigProperties.kr.url}/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.rice.kim.impl.identity.email.EntityEmailTypeBo&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
    <br/>

    <strong>Employment Status</strong><br/>
    <br/>
        <%--<li><portal:portalLink displayTitle="true" title="Employment Status" url="${ConfigProperties.kr.url}/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.rice.kim.impl.identity.employment.EntityEmploymentStatusBo&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>--%>
        <portal:portalLink displayTitle="true" title="Employment Type" url="${ConfigProperties.kr.url}/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.rice.kim.impl.identity.employment.EntityEmploymentTypeBo&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
        <portal:portalLink displayTitle="true" title="Entity Type" url="${ConfigProperties.kr.url}/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.rice.kim.impl.identity.EntityTypeBo&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
        <portal:portalLink displayTitle="true" title="External Identifier Type" url="${ConfigProperties.kr.url}/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.rice.kim.impl.identity.external.EntityExternalIdentifierTypeBo&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
        <portal:portalLink displayTitle="true" title="Name Type" url="${ConfigProperties.kr.url}/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.rice.kim.impl.identity.name.EntityNameTypeBo&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
        <portal:portalLink displayTitle="true" title="Phone Type" url="${ConfigProperties.kr.url}/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.rice.kim.impl.identity.phone.EntityPhoneTypeBo&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>



</div>
<channel:portalChannelBottom />       

