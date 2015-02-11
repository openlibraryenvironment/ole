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

<channel:portalChannelTop channelTitle="Select & Acquire Admin" />
<div class="body">
    <strong>ORDER INFORMATION</strong><br/><br/>

    <portal:portalLink displayTitle="true" title="Bank" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.sys.businessobject.Bank&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
    <portal:portalLink displayTitle="true" title="Billing Address"
                          url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.BillingAddress&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
    <br/>
    <portal:portalLink displayTitle="true" title="Building" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.sys.businessobject.Building&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
    <portal:portalLink displayTitle="true"  title="Cancellation Reason"
                          url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.select.bo.OLECancellationReason&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true" title="Carrier"
                          url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.Carrier&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
    <br/>
    <portal:portalLink displayTitle="true" title="Currency Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.vnd.businessobject.OleCurrencyType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
    <portal:portalLink displayTitle="true"
                          title="Delivery Required Date Reason"
                          url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.DeliveryRequiredDateReason&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
    <br/>
    <portal:portalLink displayTitle="true"  title="Donor"
                          url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.select.bo.OLEDonor&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>

        <portal:portalLink displayTitle="true" title="Exception Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleExceptionType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
    <portal:olePortalLink yellow="true" displayTitle="true" title="Exchange Rates" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.vnd.businessobject.OleExchangeRate&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
    <portal:portalLink displayTitle="true" title="Invoice Sub Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleInvoiceSubType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/><br/>
    <portal:portalLink displayTitle="true" title="Invoice Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleInvoiceType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
    <portal:portalLink displayTitle="true" title="Item Category" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleCategory&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/><br/>
    <portal:portalLink displayTitle="true" title="Item Type"
                          url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.ItemType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
    <br/>
    <portal:portalLink displayTitle="true" title="Method of PO Transmission" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.PurchaseOrderTransmissionMethod&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
    <br/>
    <portal:portalLink displayTitle="true" title="Note Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleNoteType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
    <portal:portalLink displayTitle="true"
                          title="Order Type"
                          url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.PurchaseOrderType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
    <br/>

    <portal:portalLink displayTitle="true" title="Payment Method" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OlePaymentMethod&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
    <portal:portalLink displayTitle="true" title="Recurring Payment Type"
                          url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.RecurringPaymentType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
    <br/>
    <portal:portalLink displayTitle="true" title="Request Source Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleRequestSourceType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/><br/>
    <portal:portalLink displayTitle="true" title="Sensitive Data"
                          url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.SensitiveData&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
    <br/>
    <portal:portalLink displayTitle="true" title="Purchase Order Purpose" url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.select.bo.OlePurchaseOrderPurpose&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>

   <%-- <portal:portalLink displayTitle="true" title="Requisition Source"
                          url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.RequisitionSource&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
    <br/>--%>
    <br/>
    
    <strong>CENTRAL TYPES & CODES</strong> <br/><br/>
    
    <portal:portalLink displayTitle="true" title="Electronic Invoice Item Mapping"
                           url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.ElectronicInvoiceItemMapping&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
    <br/>
    <portal:portalLink displayTitle="true" title="Format" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleFormatType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/><br/>


    <portal:portalLink displayTitle="true" title="Funding Source"
                          url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.FundingSource&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
    <br/>

    <portal:portalLink displayTitle="true" title="Item Price Source" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleItemPriceSource&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/><br/>


    <portal:portalLink displayTitle="true" title="Item Reason Added"
                          url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.ItemReasonAdded&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
    <br/>
    <br/>
    

    <strong>${ConfigProperties.ORGANIZATIONINFORMATION}</strong><br/><br/>

    <portal:portalLink displayTitle="true"
                           title="Negative Payment Request Approval Limit"
                           url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.NegativePaymentRequestApprovalLimit&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
    <br/>
    <portal:portalLink displayTitle="true" title="${ConfigProperties.OrganizationAPOLimit}"
                          url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.OrganizationParameter&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
    <br/>
    <portal:portalLink displayTitle="true"
                          title="Payment Request Auto Approve Exclusions"
                          url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.AutoApproveExclude&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
    <br/>

    <portal:portalLink displayTitle="true"
                          title="Purchase Order Contract Language"
                          url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.PurchaseOrderContractLanguage&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
    <br/>

    <portal:portalLink displayTitle="true"
                          title="Purchase Order Item Status"
                          url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.OrderItemStatus&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
    <br/>

    <%--<portal:portalLink displayTitle="true"
                          title="Purchase Order Quote Language"
                          url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.PurchaseOrderQuoteLanguage&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
    <br/>

    <portal:portalLink displayTitle="true"
                          title="Purchase Order Quote List"
                          url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.PurchaseOrderQuoteList&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
    <br/>

    <portal:portalLink displayTitle="true"
                          title="Purchase Order Quote Status"
                          url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.PurchaseOrderQuoteStatus&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
    <br/>--%>

    <portal:portalLink displayTitle="true"
                          title="Purchase Order Vendor Choice"
                          url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.PurchaseOrderVendorChoice&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
    <br/>

    <portal:portalLink displayTitle="true"
                          title="Receiving Address"
                          url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.ReceivingAddress&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
    <br/>

    <portal:portalLink displayTitle="true" title="Receiving Threshold"
                          url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.ReceivingThreshold&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
    <br/>

    <portal:portalLink displayTitle="true"
                          title="Recurring Payment Frequency"
                          url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.RecurringPaymentFrequency&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
    <br/>
    <portal:portalLink displayTitle="true" title="Requisition Source"
                          url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.purap.businessobject.RequisitionSource&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />
    <br/>
    <portal:portalLink displayTitle="true" title="Room" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.sys.businessobject.Room&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/><br/>





    <strong>FINANCIAL TRANSACTION CONTROLS</strong><br/><br/>

    <portal:portalLink displayTitle="true" title="Disbursement Voucher Payment Reason" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.fp.businessobject.PaymentReasonCode&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
    <portal:portalLink displayTitle="true" title="Fiscal Year Function Control" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.fp.businessobject.FiscalYearFunctionControl&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
    <portal:portalLink displayTitle="true" title="Function Control Code" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.fp.businessobject.FunctionControlCode&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
    <portal:portalLink displayTitle="true" title="Offset Definition" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.businessobject.OffsetDefinition&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
    <portal:portalLink displayTitle="true" title="Organization Reversion" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.businessobject.OrganizationReversion&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
    <portal:portalLink displayTitle="true" title="Organization Reversion Category" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.businessobject.OrganizationReversionCategory&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
    <portal:portalLink displayTitle="true" title="Organization Reversion Global" url="kr/maintenance.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.businessobject.OrganizationReversionGlobal" /><br/>
    <portal:portalLink displayTitle="true" title="University Date" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.sys.businessobject.UniversityDate&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>

    


</div>
<channel:portalChannelBottom />




