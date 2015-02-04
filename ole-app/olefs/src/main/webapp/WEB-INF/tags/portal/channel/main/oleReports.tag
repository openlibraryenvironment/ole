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

<channel:portalChannelTop channelTitle="Reports" />
<div class="body">
	<c:if test="${ConfigProperties.module.accounts.receivable.enabled == 'true'}">
		<strong>Accounts Receivable</strong><br/><br/>
	    	
		    <portal:olePortalLink grey="true" displayTitle="false" title="Billing Statement" url="arCustomerStatement.do?methodToCall=start" hiddenTitle="true"/><br/>
	   		<portal:olePortalLink grey="true" displayTitle="false" title="Customer Aging Report" url="arCustomerAgingReportLookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.ar.businessobject.CustomerAgingReportDetail" hiddenTitle="true"/><br/> 		
	   		<portal:olePortalLink grey="true" displayTitle="false" title="Customer Invoice" url="arCustomerInvoice.do?methodToCall=start" hiddenTitle="true"/><br/><br/>
		
	</c:if>
	<c:if test="${ConfigProperties.module.labor.distribution.enabled == 'true'}">
		<strong>Effort Certification</strong><br/>
	    	
			
				<portal:olePortalLink displayTitle="true" title="Duplicate Certifications Report"
					url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.ec.businessobject.DuplicateCertificationsReport&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
			<br/>
			
			
				<portal:olePortalLink displayTitle="true" title="Effort Certification Extract Build"
					url="${OLEConstants.GL_MODIFIED_INQUIRY_ACTION}?methodToCall=start&businessObjectClassName=org.kuali.ole.module.ec.businessobject.EffortCertificationDetailBuild&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
			<br/>
			
			
				<portal:olePortalLink displayTitle="true" title="Outstanding Certifications by Chart/Org/Report"
					url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.ec.businessobject.OutstandingCertificationsByOrganization&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
			<br/>
			
			
				<portal:olePortalLink displayTitle="true" title="Outstanding Certifications By Report"
					url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.ec.businessobject.OutstandingCertificationsByReport&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
			<br/>
		</ul>
	</c:if>
	<c:if test="${ConfigProperties.module.endowment.enabled == 'true'}">
		<strong>Endowments</strong><br/>
	    	
			<portal:olePortalLink displayTitle="true" title="Asset Statement" url="reportEndowAssetStatement.do?methodToCall=start"/><br/>
			<portal:olePortalLink displayTitle="true" title="Transaction Statement" url="reportEndowTransactionStatement.do?methodToCall=start"/><br/>
			<portal:olePortalLink displayTitle="true" title="Transaction Summary" url="reportEndowTransactionSummary.do?methodToCall=start"/><br/>		
			<portal:olePortalLink displayTitle="true" title="Trial Balance" url="reportEndowTrialBalance.do?methodToCall=start"/><br/>
		</ul>
	</c:if>
	<strong>System (PDF Samples Only)</strong><br/><br/>
    
	    <!-- <a class="portal_link" href="http://kuali.org/kfs/fis-standard-reports/kuali_acct_labor_trans.pdf" title="Account Labor Transactions" target="_BLANK">Account Labor Transactions</a><br/> -->
	    <img src="images-portal/yellow.png"/> <a class="portal_link" href="http://kuali.org/kfs/fis-standard-reports/accountStatus.pdf" title="Account Status" target="_BLANK">Account Status</a><br/>
	    <img src="images-portal/yellow.png"/> <a class="portal_link" href="http://kuali.org/kfs/fis-standard-reports/accountTransactions.pdf" title="Account Transactions" target="_BLANK">Account Transactions</a><br/>
	    <img src="images-portal/yellow.png"/> <a class="portal_link" href="http://kuali.org/kfs/fis-standard-reports/consolidatedAccountStatus.pdf" title="Consolidated Account Status" target="_BLANK">Consolidated Account Status</a><br/>
	    <img src="images-portal/yellow.png"/> <a class="portal_link" href="http://kuali.org/kfs/fis-standard-reports/consolidatedStatus.pdf" title="Consolidated Status" target="_BLANK">Consolidated Status</a><br/>
	    <img src="images-portal/yellow.png"/> <a class="portal_link" href="http://kuali.org/kfs/fis-standard-reports/kuali_rvrsn_carryfwd_sum.pdf" title="Reversion and Carry Forward Summary" target="_BLANK">Reversion and Carry Forward Summary</a><br/>
	    <img src="images-portal/yellow.png"/> <a class="portal_link" href="http://kuali.org/kfs/fis-standard-reports/trialBalance.pdf" title="Trial Balance" target="_BLANK">Trial Balance</a><br/>
	</ul>
</div>
<channel:portalChannelBottom />
						
						
						
						
						
						
