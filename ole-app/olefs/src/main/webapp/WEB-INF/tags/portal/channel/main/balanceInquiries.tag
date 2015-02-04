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

<channel:portalChannelTop channelTitle="Balance Inquiries" />
<div class="body">
 	<strong>General Ledger</strong><br />
    <ul class="chan">
		<li><portal:portalLink displayTitle="true" title="Available Balances" url="${OLEConstants.GL_MODIFIED_INQUIRY_ACTION}?methodToCall=start&businessObjectClassName=org.kuali.ole.gl.businessobject.AccountBalance&docFormKey=88888888&returnLocation=portal.do&hideReturnLink=true" /></li>
	    <li><portal:portalLink displayTitle="true" title="Balances by Consolidation" url="${OLEConstants.GL_ACCOUNT_BALANCE_BY_CONSOLIDATION_LOOKUP_ACTION}?methodToCall=start&businessObjectClassName=org.kuali.ole.gl.businessobject.AccountBalanceByConsolidation&docFormKey=88888888&returnLocation=portal.do&hideReturnLink=true" /></li>
	   	<li><portal:portalLink displayTitle="true" title="Cash Balances" url="${OLEConstants.GL_MODIFIED_INQUIRY_ACTION}?methodToCall=start&businessObjectClassName=org.kuali.ole.gl.businessobject.CashBalance&docFormKey=88888888&returnLocation=portal.do&hideReturnLink=true" /></li>
	    <li><portal:portalLink displayTitle="true" title="General Ledger Balance" url="${OLEConstants.GL_BALANCE_INQUIRY_ACTION}?methodToCall=start&businessObjectClassName=org.kuali.ole.gl.businessobject.Balance&docFormKey=88888888&returnLocation=portal.do&hideReturnLink=true" /></li>
		<li><portal:portalLink displayTitle="true" title="General Ledger Entry" url="${OLEConstants.GL_MODIFIED_INQUIRY_ACTION}?methodToCall=start&businessObjectClassName=org.kuali.ole.gl.businessobject.Entry&docFormKey=88888888&returnLocation=portal.do&hideReturnLink=true" /></li>
		<li><portal:portalLink displayTitle="true" title="General Ledger Pending Entry" url="${OLEConstants.GL_MODIFIED_INQUIRY_ACTION}?methodToCall=start&businessObjectClassName=org.kuali.ole.sys.businessobject.GeneralLedgerPendingEntry&docFormKey=88888888&returnLocation=portal.do&hideReturnLink=true" /></li>
	   	<li><portal:portalLink displayTitle="true" title="Open Encumbrances" url="${OLEConstants.GL_MODIFIED_INQUIRY_ACTION}?methodToCall=start&businessObjectClassName=org.kuali.ole.gl.businessobject.Encumbrance&docFormKey=88888888&returnLocation=portal.do&hideReturnLink=true" /></li>
    </ul>
<c:if test="${ConfigProperties.module.labor.distribution.enabled == 'true'}">    
 	<strong>Labor Distribution</strong><br />
        <ul class="chan">
	<li><portal:portalLink displayTitle="true" title="Account Status (Base Funds)" url="${OLEConstants.GL_MODIFIED_INQUIRY_ACTION}?methodToCall=start&businessObjectClassName=org.kuali.ole.module.ld.businessobject.AccountStatusBaseFunds&docFormKey=88888888&returnLocation=portal.do&hideReturnLink=true" /></li>    
	<li><portal:portalLink displayTitle="true" title="Account Status (Current Funds)" url="${OLEConstants.GL_MODIFIED_INQUIRY_ACTION}?methodToCall=start&businessObjectClassName=org.kuali.ole.module.ld.businessobject.AccountStatusCurrentFunds&docFormKey=88888888&returnLocation=portal.do&hideReturnLink=true" /></li>     
	<li><portal:portalLink displayTitle="true" title="Calculated Salary Foundation" url="${OLEConstants.GL_MODIFIED_INQUIRY_ACTION}?methodToCall=start&businessObjectClassName=org.kuali.ole.module.ld.businessobject.LaborCalculatedSalaryFoundationTracker&docFormKey=88888888&returnLocation=portal.do&hideReturnLink=true" /></li>
	<li><portal:portalLink displayTitle="true" title="Employee Funding" url="${OLEConstants.GL_MODIFIED_INQUIRY_ACTION}?methodToCall=start&businessObjectClassName=org.kuali.ole.module.ld.businessobject.EmployeeFunding&docFormKey=88888888&returnLocation=portal.do&hideReturnLink=true" /></li>     
    <li><portal:portalLink displayTitle="true" title="July 1 Position Funding" url="${OLEConstants.GL_MODIFIED_INQUIRY_ACTION}?methodToCall=start&businessObjectClassName=org.kuali.ole.module.ld.businessobject.July1PositionFunding&docFormKey=88888888&returnLocation=portal.do&hideReturnLink=true" /></li>  
	<li><portal:portalLink displayTitle="true" title="Labor Ledger View" url="${OLEConstants.GL_BALANCE_INQUIRY_ACTION}?methodToCall=start&businessObjectClassName=org.kuali.ole.module.ld.businessobject.LedgerBalance&docFormKey=88888888&returnLocation=portal.do&hideReturnLink=true&financialBalanceTypeCode=AC" /></li>  
	<li><portal:portalLink displayTitle="true" title="Labor Ledger Pending Entry" url="${OLEConstants.GL_MODIFIED_INQUIRY_ACTION}?methodToCall=start&businessObjectClassName=org.kuali.ole.module.ld.businessobject.LaborLedgerPendingEntry&docFormKey=88888888&returnLocation=portal.do&hideReturnLink=true" /></li>  
	<li><portal:portalLink displayTitle="true" title="Position Inquiry" url="${OLEConstants.GL_MODIFIED_INQUIRY_ACTION}?methodToCall=start&businessObjectClassName=org.kuali.ole.module.ld.businessobject.PositionData&docFormKey=88888888&returnLocation=portal.do&hideReturnLink=true" /></li>  
    </ul>
</c:if>
<c:if test="${ConfigProperties.module.endowment.enabled == 'true'}">    
    <strong>Endowment</strong><br />
    <ul class="chan">
    <li><portal:portalLink displayTitle="true" title="Current KEMID Available Balances Lookup" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.endow.businessobject.KEMIDCurrentAvailableBalance&docFormKey=88888888&returnLocation=portal.do&hideReturnLink=true" /></li>    
	<li><portal:portalLink displayTitle="true" title="Current KEMID Balances Lookup" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.endow.businessobject.KEMIDCurrentBalance&docFormKey=88888888&returnLocation=portal.do&hideReturnLink=true" /></li> 
	<li><portal:portalLink displayTitle="true" title="KEMID Historical Balances Lookup" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.endow.businessobject.KEMIDHistoricalBalance&docFormKey=88888888&returnLocation=portal.do&hideReturnLink=true" /></li>       
    <li><portal:portalLink displayTitle="true" title="Transaction Archives" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.endow.businessobject.TransactionArchive&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
    </ul>
</c:if>
</div>
<channel:portalChannelBottom />
