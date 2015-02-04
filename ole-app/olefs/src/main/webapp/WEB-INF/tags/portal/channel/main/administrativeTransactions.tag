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

<channel:portalChannelTop channelTitle="Administrative Transactions" />
<div class="body">
<c:if test="${ConfigProperties.module.capital.asset.enabled == 'true'}">
  <strong>Capital Asset Builder</strong>
  <ul class="chan">
	 <li><portal:portalLink displayTitle="true" title="Capital Asset Builder AP Transactions" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.cab.businessobject.PurchasingAccountsPayableProcessingReport&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
  	 <li><portal:portalLink displayTitle="true" title="Capital Asset Builder GL Transactions" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.cab.businessobject.GeneralLedgerEntry&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
  </ul>

  <strong>Capital Asset Management</strong>
  <ul class="chan">
     <li><portal:portalLink displayTitle="true" title="Asset Manual Payment" url="camsAssetPayment.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_MPAY" /></li>
  	 <li><portal:portalLink displayTitle="true" title="Barcode Inventory Process" url="uploadBarcodeInventoryFile.do?methodToCall=start&batchUpload.batchInputTypeName=assetBarcodeInventoryInputFileType" /></li>			 
  </ul>
</c:if>

	<strong>Financial Processing</strong><br />
    <ul class="chan">
        <li><portal:portalLink displayTitle="true" title="Cash Management" url="financialCashManagement.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_CMD" /></li>
		<li><portal:portalLink displayTitle="true" title="General Ledger Correction Process" url="generalLedgerCorrection.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_GLCP" /></li>									
		<li><portal:portalLink displayTitle="true" title="Journal Voucher" url="financialJournalVoucher.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_JV" /></li>
		<li><portal:portalLink displayTitle="true" title="Non-Check Disbursement" url="financialNonCheckDisbursement.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_ND" /></li>
		<li><portal:portalLink displayTitle="true" title="Service Billing" url="financialServiceBilling.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_SB" /></li>
    </ul>
	<strong>System</strong>
	<ul class="chan">
		<li><portal:portalLink displayTitle="true" title="Electronic Payment Claim" url="electronicFundTransfer.do?methodToCall=start" /></li>
	</ul>	   
</div>
<channel:portalChannelBottom />
