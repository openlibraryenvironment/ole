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

<channel:portalChannelTop channelTitle="Lookup and Maintenance" />
<div class="body">
	<c:if test="${ConfigProperties.module.capital.asset.enabled == 'true'}">
	   <%--<strong><strike>Capital Asset Builder</strike></strong><br />
	    
			<strike><portal:olePortalLink displayTitle="true" title="Pre-Asset Tagging" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.cab.businessobject.Pretag&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></strike><br/>
		
	    <strong><strike>Capital Asset Management</strike></strong><br />
	    
			<strike><portal:olePortalLink displayTitle="true" title="Asset" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.cam.businessobject.Asset&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></strike><br/>
			<strike><portal:olePortalLink displayTitle="true" title="Asset Fabrication" url="kr/maintenance.do?maintenanceAction=New&methodToCall=start&businessObjectClassName=org.kuali.ole.module.cam.businessobject.AssetFabrication" /></strike><br/>
			<strike><portal:olePortalLink displayTitle="true" title="Asset Global (Add)" url="kr/lookup.do?businessObjectClassName=org.kuali.ole.module.cam.businessobject.AssetAcquisitionType&conversionFields=acquisitionTypeCode:acquisitionTypeCode&returnLocation=portal.do&docFormKey=88888888" /></strike><br/>
			<strike><portal:olePortalLink displayTitle="true" title="Asset Location Global" url="kr/maintenance.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.cam.businessobject.AssetLocationGlobal" /></strike><br/>
			<strike><portal:olePortalLink displayTitle="true" title="Asset Payment" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.cam.businessobject.AssetPayment&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></strike><br/>
			<strike><portal:olePortalLink displayTitle="true" title="Asset Retirement Global" url="kr/lookup.do?businessObjectClassName=org.kuali.ole.module.cam.businessobject.AssetRetirementReason&conversionFields=retirementReasonCode:retirementReasonCode&returnLocation=portal.do&docFormKey=88888888" /></strike><br/>
		 --%> 
	</c:if>
    <strong>Chart of Accounts</strong><br /><br/>
    
	    <portal:olePortalLink green="true" displayTitle="true" title="Account" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.businessobject.Account&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
	    <portal:olePortalLink green="true" displayTitle="true" title="Account Global" url="kr/maintenance.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.businessobject.AccountGlobal" /><br/>	
	    <portal:olePortalLink yellow="true" displayTitle="true" title="Account Delegate" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.businessobject.AccountDelegate&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
	    <portal:olePortalLink yellow="true" displayTitle="true" title="Account Delegate Global" url="kr/maintenance.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.businessobject.AccountDelegateGlobal" /><br/>
	    <portal:olePortalLink yellow="true" displayTitle="true" title="Account Delegate Model" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.businessobject.AccountDelegateModel&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
	    <portal:olePortalLink yellow="true" displayTitle="true" title="Account Delegate Global From Model" url="kr/lookup.do?businessObjectClassName=org.kuali.ole.coa.businessobject.AccountDelegateModel&conversionFields=chartOfAccountsCode:modelChartOfAccountsCode,organizationCode:modelOrganizationCode,accountDelegateModelName:modelName&returnLocation=portal.do&docFormKey=88888888" /><br/>
	    <portal:olePortalLink green="true" displayTitle="true" title="Object Code" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.businessobject.ObjectCode&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
	    <portal:olePortalLink green="true" displayTitle="true" title="Object Code Global" url="kr/maintenance.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.businessobject.ObjectCodeGlobal" /><br/>
	    <portal:olePortalLink green="true" displayTitle="true" title="Organization" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.businessobject.Organization&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
	    <portal:olePortalLink yellow="true" displayTitle="true" title="Organization Review" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.identity.OrgReviewRole&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
	    <portal:olePortalLink yellow="true" displayTitle="true" title="Project Code" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.businessobject.ProjectCode&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
	    <portal:olePortalLink green="true" displayTitle="true" title="Sub-Account" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.businessobject.SubAccount&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
	    <portal:olePortalLink green="true" displayTitle="true" title="Sub-Object Code" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.businessobject.SubObjectCode&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
	    <portal:olePortalLink green="true" displayTitle="true" title="Sub-Object Code Global" url="kr/maintenance.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.businessobject.SubObjectCodeGlobal" /><br/><br/>
	
	<c:choose>
 	<c:when test="${ConfigProperties['module.external.kuali.coeus.enabled'] == 'true'}"> 
    </c:when>
    <c:when test="${ConfigProperties['module.contracts.and.grants.enabled'] == 'true'}">
	    <strong>Contracts & Grants</strong><br /><br/>
		
			<portal:olePortalLink grey="true" displayTitle="false" title="Award" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.cg.businessobject.Award&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" hiddenTitle="true"/><br/>
			<portal:olePortalLink grey="true" displayTitle="false" title="Proposal" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.cg.businessobject.Proposal&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" hiddenTitle="true"/><br/><br/>
		
    </c:when>
    </c:choose>
	<c:if test="${ConfigProperties.module.endowment.enabled == 'true'}">
    	<!-- <strong>Endowment</strong><br />
		
		    <portal:olePortalLink displayTitle="true" title="KEMID" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.endow.businessobject.KEMID&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
			<portal:olePortalLink displayTitle="true" title="Recurring Cash Transfer" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.endow.businessobject.EndowmentRecurringCashTransfer&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
			<portal:olePortalLink displayTitle="true" title="Security" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.endow.businessobject.Security&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
		    <portal:olePortalLink displayTitle="true" title="Tickler" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.endow.businessobject.Tickler&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
		 -->
	</c:if>		
   <%-- <strong><strike>Financial Processing</strike></strong><br />
	
	    <strike><portal:olePortalLink displayTitle="true" title="Disbursement Voucher Travel Company" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.fp.businessobject.TravelCompanyCode&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></strike><br/>
	  --%>
	<%-- <strong>Requestor(Patron) Lookup</strong><br/><br/>
	
	<portal:olePortalLink green="true" displayTitle="true" title="Requestor" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleRequestor&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/><br/> --%>
	
    <strong>Vendor</strong><br /><br/>
		
        <portal:olePortalLink green="true" displayTitle="true" title="Vendor" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.vnd.businessobject.VendorDetail&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
        <portal:olePortalLink grey="true" displayTitle="false" title="Vendor Contracts" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.vnd.businessobject.VendorContract&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" hiddenTitle="true"/><br/><br/>
	
    </div>
<channel:portalChannelBottom />
