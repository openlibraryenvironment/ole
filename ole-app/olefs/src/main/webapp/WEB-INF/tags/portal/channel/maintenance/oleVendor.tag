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

<channel:portalChannelTop channelTitle="Vendor" />
<div class="body">
    
        <portal:olePortalLink green="true" displayTitle="true" title="Address Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.vnd.businessobject.AddressType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
        <portal:olePortalLink green="true" displayTitle="true" title="Campus" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.vnd.businessobject.CampusParameter&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
       <!--  <portal:olePortalLink displayTitle="true" title="Alias Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.vnd.businessobject.AliasType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
        <portal:olePortalLink displayTitle="true" title="Campus Parameter" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.vnd.businessobject.CampusParameter&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>  -->
        <portal:olePortalLink green="true" displayTitle="true" title="Commodity Code" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.vnd.businessobject.CommodityCode&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
        <portal:olePortalLink green="true" displayTitle="true" title="Contact Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.vnd.businessobject.ContactType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
        <portal:olePortalLink green="true" displayTitle="true" title="Contract Manager" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.vnd.businessobject.ContractManager&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
        <portal:olePortalLink green="true" displayTitle="true" title="Cost Source" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.vnd.businessobject.PurchaseOrderCostSource&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
        <!-- <portal:olePortalLink displayTitle="true" title="Currency Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.vnd.businessobject.OleCurrencyType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
        <portal:olePortalLink displayTitle="true" title="Exchange Rate" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.vnd.businessobject.OleExchangeRate&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/> -->
        <portal:olePortalLink green="true" displayTitle="true" title="Ownership Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.vnd.businessobject.OwnershipType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
        <portal:olePortalLink green="true" displayTitle="true" title="Ownership Type Category" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.vnd.businessobject.OwnershipCategory&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
        <portal:olePortalLink green="true" displayTitle="true" title="Payment Terms Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.vnd.businessobject.PaymentTermType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
        <portal:olePortalLink green="true" displayTitle="true" title="Phone Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.vnd.businessobject.PhoneType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
        <portal:olePortalLink green="true" displayTitle="true" title="Shipping Payment Terms" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.vnd.businessobject.ShippingPaymentTerms&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
        <portal:olePortalLink green="true" displayTitle="true" title="Shipping Special Conditions" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.vnd.businessobject.ShippingSpecialCondition&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
        <portal:olePortalLink green="true" displayTitle="true" title="Shipping Title" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.vnd.businessobject.ShippingTitle&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
        <portal:olePortalLink green="true" displayTitle="true" title="Supplier Diversity" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.vnd.businessobject.SupplierDiversity&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
        <portal:olePortalLink green="true" displayTitle="true" title="Vendor Inactive Reason" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.vnd.businessobject.VendorInactiveReason&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>     
        <portal:olePortalLink green="true" displayTitle="true" title="Vendor Search Alias Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.vnd.businessobject.AliasType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
        <portal:olePortalLink green="true" displayTitle="true" title="Vendor Transmission Format" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.vnd.businessobject.OleVendorTransmissionFormat&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
        <portal:olePortalLink green="true" displayTitle="true" title="Vendor Transmission Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.vnd.businessobject.OleVendorTransmissionType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
        <portal:olePortalLink green="true" displayTitle="true" title="Vendor Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.vnd.businessobject.VendorType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/><br/>
    
</div>
<channel:portalChannelBottom />

