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

<channel:portalChannelTop channelTitle="Contracts & Grants" />
<div class="body">
	 

 	<c:choose>
 	<c:when test="${ConfigProperties['module.external.kuali.coeus.enabled'] == 'true'}"> 
 		<portal:olePortalLink grey="true"   title="CG Account Default" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.external.kc.businessobject.AccountAutoCreateDefaults&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" hiddenTitle="true"/><br/>
    </c:when>
    <c:otherwise>
		
			<portal:olePortalLink grey="true"   title="Agency"
				url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.cg.businessobject.Agency&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" hiddenTitle="true"/>
		<br/>
		
			<portal:olePortalLink grey="true"   title="Agency Type"
				url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.cg.businessobject.AgencyType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" hiddenTitle="true"/>
		<br/>
		
		
			<portal:olePortalLink grey="true"   title="Award Status"
				url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.cg.businessobject.AwardStatus&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" hiddenTitle="true"/>
		<br/>
				
		
			<portal:olePortalLink grey="true"   title="CFDA"
				url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.cg.businessobject.CFDA&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" hiddenTitle="true"/>
		<br/>
		
		
	
		
			<portal:olePortalLink grey="true"   title="Grant Description"
				url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.cg.businessobject.GrantDescription&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" hiddenTitle="true"/>
		<br/>
		
		
			<portal:olePortalLink grey="true"  
				title="Letter of Credit Fund Group"
				url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.cg.businessobject.LetterOfCreditFundGroup&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" hiddenTitle="true"/>
		<br/>
		
			<portal:olePortalLink grey="true"  
				title="Proposal/Award Close"
				url="cgClose.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_CLOS" hiddenTitle="true"/>
		<br/>
		
			<portal:olePortalLink grey="true"   title="Proposal Purpose"
				url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.cg.businessobject.ProposalPurpose&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" hiddenTitle="true"/>
		<br/>
		
			<portal:olePortalLink grey="true"   title="Proposal Status"
				url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.cg.businessobject.ProposalStatus&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" hiddenTitle="true"/>
		<br/>
		
			<portal:olePortalLink grey="true"   title="Proposal Type"
				url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.cg.businessobject.ProposalAwardType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" hiddenTitle="true"/>
		<br/>
		
		
			<portal:olePortalLink grey="true"   title="Research Risk Type"
				url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.cg.businessobject.ResearchRiskType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" hiddenTitle="true"/>
		<br/>
		
		
			<portal:olePortalLink grey="true"   title="Sub-Contractor"
				url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.cg.businessobject.SubContractor&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" hiddenTitle="true"/>
		<br/><br/>	
	</c:otherwise>
	</c:choose>
	  
</div>
<channel:portalChannelBottom />
