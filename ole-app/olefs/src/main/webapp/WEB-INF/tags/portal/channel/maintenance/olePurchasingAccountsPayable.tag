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

<channel:portalChannelTop channelTitle="Purchasing/Accounts Payable" />
<div class="body">
	
		
			<portal:olePortalLink green="true" displayTitle="true" title="Billing Address"
				url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.BillingAddress&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
		<br/>
		<!-- 
            <portal:olePortalLink green="true" displayTitle="true" title="Capital Asset System State"
                url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.CapitalAssetSystemState&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
        <br/>
        
            <portal:olePortalLink green="true" displayTitle="true" title="Capital Asset System Type"
                url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.CapitalAssetSystemType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
        <br/>	 -->	
        
            <portal:olePortalLink green="true" displayTitle="true" title="Carrier"
                url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.Carrier&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
        <br/>
        <portal:olePortalLink green="true" displayTitle="true" title="Category" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleCategory&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/><br/>
		
			<portal:olePortalLink green="true" displayTitle="true"
				title="Delivery Required Date Reason"
				url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.DeliveryRequiredDateReason&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
		<br/>
		
			<portal:olePortalLink green="true" displayTitle="true" title="Electronic Invoice Item Mapping"
				url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.ElectronicInvoiceItemMapping&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
		<br/>
		<portal:olePortalLink green="true" displayTitle="true" title="Exception Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleExceptionType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
		<portal:olePortalLink green="true" displayTitle="true" title="Format" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleFormatType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/><br/>
		
		
			<portal:olePortalLink green="true" displayTitle="true" title="Funding Source"
				url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.FundingSource&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
		<br/>
		<portal:olePortalLink green="true" displayTitle="true" title="Invoice Sub Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleInvoiceSubType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/><br/>
		<portal:olePortalLink green="true" displayTitle="true" title="Invoice Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleInvoiceType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
		<portal:olePortalLink green="true" displayTitle="true" title="Item Price Source" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleItemPriceSource&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/><br/>
		  
		
			<portal:olePortalLink green="true" displayTitle="true" title="Item Reason Added"
				url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.ItemReasonAdded&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
		<br/>
		
			<portal:olePortalLink green="true" displayTitle="true" title="Item Type"
				url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.ItemType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
		<br/>
		
		
			<portal:olePortalLink green="true" displayTitle="true"
				title="Method of PO Transmission"
				url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.PurchaseOrderTransmissionMethod&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
		<br/>		
		
			<portal:olePortalLink green="true" displayTitle="true"
				title="Negative Payment Request Approval Limit"
				url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.NegativePaymentRequestApprovalLimit&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
		<br/>
		<portal:olePortalLink green="true" displayTitle="true" title="Note Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleNoteType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
		<!-- 
			<portal:olePortalLink green="true" displayTitle="true"
				title="Order Item Status"
				url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.OrderItemStatus&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
		<br/> -->	
		
			<portal:olePortalLink green="true" displayTitle="true" title="Organization Parameter"
				url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.OrganizationParameter&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
		<br/>
		
		
		
   			 <portal:olePortalLink green="true" displayTitle="true"
               title="Order Type"
               url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.PurchaseOrderType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
        <br/>
        <portal:olePortalLink green="true" displayTitle="true" title="Payment Method" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OlePaymentMethod&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
		
			<portal:olePortalLink green="true" displayTitle="true"
				title="Payment Request Auto Approve Exclusions"
				url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.AutoApproveExclude&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
		<br/>
		
			<portal:olePortalLink green="true" displayTitle="true"
				title="Purchase Order Contract Language"
				url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.PurchaseOrderContractLanguage&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
		<br/>
		
			<portal:olePortalLink green="true" displayTitle="true"
				title="Purchase Order Item Status"
				url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.OrderItemStatus&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
		<br/> 
		
			<portal:olePortalLink green="true" displayTitle="true"
				title="Purchase Order Quote Language"
				url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.PurchaseOrderQuoteLanguage&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
		<br/>
        
        	<portal:olePortalLink green="true" displayTitle="true"
        		title="Purchase Order Quote List"
        		url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.PurchaseOrderQuoteList&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
		<br/>
		
			<portal:olePortalLink green="true" displayTitle="true"
				title="Purchase Order Quote Status"
				url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.PurchaseOrderQuoteStatus&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
		<br/>		
        
			<portal:olePortalLink green="true" displayTitle="true"
				title="Purchase Order Vendor Choice"
				url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.PurchaseOrderVendorChoice&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
		<br/>
		
			<portal:olePortalLink green="true" displayTitle="true"
				title="Receiving Address"
				url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.ReceivingAddress&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
		<br/>
        
            <portal:olePortalLink green="true" displayTitle="true" title="Receiving Threshold"
                url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.ReceivingThreshold&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
        <br/>
		
			<portal:olePortalLink green="true" displayTitle="true"
				title="Recurring Payment Frequency"
				url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.RecurringPaymentFrequency&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
		<br/>
		
			<portal:olePortalLink green="true" displayTitle="true" title="Recurring Payment Type"
				url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.RecurringPaymentType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
		<br/>
		<portal:olePortalLink green="true" displayTitle="true" title="Request Source Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleRequestSourceType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/><br/>
		
			<portal:olePortalLink green="true" displayTitle="true" title="Requisition Source"
				url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.RequisitionSource&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
		<br/>
		
            <portal:olePortalLink green="true" displayTitle="true" title="Sensitive Data"
                url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.SensitiveData&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
        <br/>		
		
			<portal:olePortalLink green="true" displayTitle="true" title="Vendor Stipulation"
				url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.VendorStipulation&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
		<br/>
		<portal:olePortalLink green="true" displayTitle="true"
				title="Licensing Requirement"
				url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleLicensingRequirement&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
		<br/>
		<br/>
	
</div>
<channel:portalChannelBottom />
