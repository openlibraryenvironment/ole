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

<channel:portalChannelTop channelTitle="Financial Processing" />
<div class="body">

    
		<portal:olePortalLink grey="true" displayTitle="false" title="Cash Drawer" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.fp.businessobject.CashDrawer&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" hiddenTitle="true"/><br/>
 		<portal:olePortalLink grey="true" displayTitle="false" title="Credit Card Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.fp.businessobject.CreditCardType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" hiddenTitle="true"/><br/>
		<portal:olePortalLink grey="true" displayTitle="false" title="Credit Card Vendor" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.fp.businessobject.CreditCardVendor&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" hiddenTitle="true" /><br/>
        <portal:olePortalLink grey="true" displayTitle="false" title="Disbursement Voucher Documentation Location" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.fp.businessobject.DisbursementVoucherDocumentationLocation&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" hiddenTitle="true"/><br/>
        <portal:olePortalLink yellow="true" displayTitle="true" title="Disbursement Voucher Payment Reason" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.fp.businessobject.PaymentReasonCode&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>        
        <portal:olePortalLink grey="true" displayTitle="false" title="Disbursement Voucher Tax Income Class" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.fp.businessobject.TaxIncomeClassCode&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" hiddenTitle="true"/><br/>
        <portal:olePortalLink grey="true" displayTitle="false" title="Disbursement Voucher Travel Expense Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.fp.businessobject.TravelExpenseTypeCode&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" hiddenTitle="true"/><br/>
        <portal:olePortalLink grey="true" displayTitle="false" title="Disbursement Voucher Travel Mileage Rate" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.fp.businessobject.TravelMileageRate&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" hiddenTitle="true"/><br/>
        <portal:olePortalLink grey="true" displayTitle="false" title="Disbursement Voucher Travel Per Diem" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.fp.businessobject.TravelPerDiem&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" hiddenTitle="true"/><br/>
        <portal:olePortalLink grey="true" displayTitle="false" title="Disbursement Voucher Wire Charge" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.fp.businessobject.WireCharge&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" hiddenTitle="true"/><br/>        
		<portal:olePortalLink yellow="true" displayTitle="true" title="Fiscal Year Function Control" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.fp.businessobject.FiscalYearFunctionControl&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>				
		<portal:olePortalLink yellow="true" displayTitle="true" title="Function Control Code" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.fp.businessobject.FunctionControlCode&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>				
        <portal:olePortalLink grey="true" displayTitle="false" title="Non-Resident Alien Tax Percent" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.fp.businessobject.NonResidentAlienTaxPercent&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" hiddenTitle="true"/><br/><br/>
   
</div>
<channel:portalChannelBottom />
                
