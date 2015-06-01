var lookupSrc;
var datePickerSrc;
jq(window).load(function() {
    if(sessionStorage.getItem("lookupSrc") == null){
        lookupSrc=jq("input[title='Search Payment Method']").attr("src");
        sessionStorage.setItem("lookupSrc", lookupSrc);
    }
    if(sessionStorage.getItem("datePickerSrc") == null){
        datePickerSrc=jq("img[id='document.newMaintainableObject.add.vendorContracts.vendorContractBeginningDate_datepicker']").attr('src');
        sessionStorage.setItem("datePickerSrc",datePickerSrc);
    }
    if(jq("input[id='document.newMaintainableObject.gokbId']").val().length != 0){
        jq("input[id='document.newMaintainableObject.gokbId']").attr("disabled", true);
        jq("input[id='document.newMaintainableObject.vendorName']").attr("disabled", true);
        jq("input[id='document.newMaintainableObject.add.vendorAliases.vendorAliasName']").attr("disabled", true);
        jq("input[id='document.newMaintainableObject.add.vendorAliases.vendorAliasType.aliasType']").attr("disabled", true);
    }else{
        jq("input[id='document.newMaintainableObject.gokbId']").attr("disabled", false);
        jq("input[id='document.newMaintainableObject.vendorName']").attr("disabled", false);
        jq("input[id='document.newMaintainableObject.add.vendorAliases.vendorAliasName']").attr("disabled", false);
        jq("input[id='document.newMaintainableObject.add.vendorAliases.vendorAliasType.aliasType']").attr("disabled", false);
    }
    var linked = jq("input[id='document.newMaintainableObject.linkedToEHoldings']").val();
    if(linked =="Yes"){
        jq("input[id='document.newMaintainableObject.gokbId']").attr("disabled", true);
        jq("input[title='Search GOKb ID']").attr("disabled", true);
    }else{
        jq("input[id='document.newMaintainableObject.gokbId']").attr("disabled", false);
        jq("input[title='Search GOKb ID']").attr("disabled", false);
    }
});

jq(document).ready(function(){
    if(sessionStorage.getItem("lookupSrc") == null){
        lookupSrc=jq("input[title='Search Payment Method']").attr("src");
        sessionStorage.setItem("lookupSrc", lookupSrc);
    }
    if(sessionStorage.getItem("datePickerSrc") == null){
        datePickerSrc=jq("img[id='document.newMaintainableObject.add.vendorContracts.vendorContractBeginningDate_datepicker']").attr('src');
        sessionStorage.setItem("datePickerSrc",datePickerSrc);
    }
    nonBillingVendor();
    jq("input[name='document.newMaintainableObject.nonBillable']").live("change",function(){
        nonBillingVendor();
    });
});



function nonBillingVendor(){
    var checked= jq("input[id='document.newMaintainableObject.nonBillable']").attr('checked');
    if(checked == "checked"){
        jq("select[id='document.newMaintainableObject.vendorHeader.vendorTypeCode']").attr('style','display:none');
        jq("input[title='Search Vendor Type']").attr('src','');
        jq("input[title='Search Vendor Type']").attr('alt','');
        jq("input[id='document.newMaintainableObject.vendorHeader.vendorForeignIndicator']").attr('style','display:none');
        jq("select[id='document.newMaintainableObject.vendorHeader.vendorForeignIndicator']").attr('style','display:none');
        jq("input[id='document.newMaintainableObject.vendorHeader.vendorTaxNumber']").attr('style','display:none');
        jq("input[id='document.newMaintainableObject.vendorHeader.vendorTaxTypeCodeFEIN']").attr('style','display:none');
        jq("input[id='document.newMaintainableObject.vendorHeader.vendorTaxTypeCodeSSN']").attr('style','display:none');
        jq("input[id='document.newMaintainableObject.vendorHeader.vendorTaxTypeCodeNONE']").attr('style','display:none');
        jq("select[id='document.newMaintainableObject.vendorHeader.vendorOwnershipCode']").attr('style','display:none');
        jq("input[title='Search Ownership Type']").attr('src','');
        jq("input[title='Search Ownership Type']").attr('alt','');
        jq("select[id='document.newMaintainableObject.vendorHeader.vendorOwnershipCategoryCode']").attr('style','display:none');
        jq("input[title='Search Ownership Type Category']").attr('src','');
        jq("input[title='Search Ownership Type Category']").attr('alt','');
        jq("select[id='document.newMaintainableObject.vendorHeader.vendorW9ReceivedIndicator']").attr('style','display:none');
        jq("select[id='document.newMaintainableObject.vendorHeader.vendorW8BenReceivedIndicator']").attr('style','display:none');
        jq("input[id='document.newMaintainableObject.vendorHeader.vendorFederalWithholdingTaxBeginningDate']").attr('style','display:none');
        jq("img[id='document.newMaintainableObject.vendorHeader.vendorFederalWithholdingTaxBeginningDate_datepicker']").attr('src','');
        jq("img[id='document.newMaintainableObject.vendorHeader.vendorFederalWithholdingTaxBeginningDate_datepicker']").attr('alt','');
        jq("input[id='document.newMaintainableObject.vendorHeader.vendorFederalWithholdingTaxEndDate']").attr('style','display:none');
        jq("img[id='document.newMaintainableObject.vendorHeader.vendorFederalWithholdingTaxEndDate_datepicker']").attr('src','');
        jq("img[id='document.newMaintainableObject.vendorHeader.vendorFederalWithholdingTaxEndDate_datepicker']").attr('alt','');
        jq("select[id='document.newMaintainableObject.vendorHeader.vendorDebarredIndicator']").attr('style','display:none');
        jq("select[id='document.newMaintainableObject.currencyTypeId']").attr('style','display:none');
        jq("input[title='Search Currency Type']").attr('src','');
        jq("input[title='Search Currency Type']").attr('alt','');
        jq("select[id='document.newMaintainableObject.vendorPaymentTermsCode']").attr('style','display:none');
        jq("input[title='Search Payment Terms']").attr('src','');
        jq("input[title='Search Payment Terms']").attr('alt','');
        jq("select[id='document.newMaintainableObject.paymentMethodId']").attr('style','display:none');
        jq("input[title='Search Payment Method']").attr('src','');
        jq("input[title='Search Payment Method']").attr('alt','');
        jq("select[id='document.newMaintainableObject.vendorPrepaymentIndicator']").attr('style','display:none');
        jq("select[id='document.newMaintainableObject.vendorCreditCardIndicator']").attr('style','display:none');
        jq("input[id='document.newMaintainableObject.vendorMinimumOrderAmount']").attr('style','display:none');
        jq("select[id='document.newMaintainableObject.vendorShippingTitleCode']").attr('style','display:none');
        jq("input[title='Search Shipping Title']").attr('src','');
        jq("input[title='Search Shipping Title']").attr('alt','');
        jq("select[id='document.newMaintainableObject.vendorShippingPaymentTermsCode']").attr('style','display:none');
        jq("input[title='Search Shipping Payment Terms']").attr('src','');
        jq("input[title='Search Shipping Payment Terms']").attr('alt','');
        jq("input[id='document.newMaintainableObject.vendorDunsNumber']").attr('style','display:none');
        jq("input[id='document.newMaintainableObject.vendorUrlAddress']").attr('style','display:none');
        jq("select[id='document.newMaintainableObject.vendorConfirmationIndicator']").attr('style','display:none');
        jq("select[id='document.newMaintainableObject.vendorRestrictedIndicator']").attr('style','display:none');
        jq("textarea[id='document.newMaintainableObject.vendorRestrictedReasonText']").attr('style','display:none');
        jq("input[id='document.newMaintainableObject.vendorRemitName']").attr('style','display:none');
        jq("select[id='document.newMaintainableObject.add.vendorTransmissionFormat.vendorTransmissionFormatId']").attr('style','display:none');
        jq("input[title='Search Transmission Format']").attr('src','');
        jq("input[title='Search Transmission Format']").attr('alt','');
        jq("input[id='document.newMaintainableObject.add.vendorTransmissionFormat.vendorPreferredTransmissionFormat']").attr('style','display:none');
        jq("select[id='document.newMaintainableObject.add.vendorTransmissionFormat.vendorTransmissionTypeId']").attr('style','display:none');
        jq("input[title='Search Transmission Type']").attr('src','');
        jq("input[title='Search Transmission Type']").attr('alt','');
        jq("input[id='document.newMaintainableObject.add.vendorTransmissionFormat.vendorEDIConnectionAddress']").attr('style','display:none');
        jq("input[id='document.newMaintainableObject.add.vendorTransmissionFormat.vendorEDIConnectionUserName']").attr('style','display:none');
        jq("input[id='document.newMaintainableObject.add.vendorTransmissionFormat.vendorEDIConnectionPassword']").attr('style','display:none');
        jq("input[id='document.newMaintainableObject.add.vendorTransmissionFormat.active']").attr('style','display:none');
        jq("select[id='document.newMaintainableObject.add.vendorHeader.vendorSupplierDiversities.vendorSupplierDiversityCode']").attr('style','display:none');
        jq("input[title='Search Supplier Diversity']").attr('src','');
        jq("input[title='Search Supplier Diversity']").attr('alt','');
        jq("input[id='document.newMaintainableObject.add.vendorHeader.vendorSupplierDiversities.active']").attr('style','display:none');
        jq("select[id='document.newMaintainableObject.add.vendorShippingSpecialConditions.vendorShippingSpecialConditionCode']").attr('style','display:none');
        jq("input[title='Search Shipping Special Condition']").attr('src','');
        jq("input[title='Search Shipping Special Condition']").attr('alt','');
        jq("input[id='document.newMaintainableObject.add.vendorShippingSpecialConditions.active']").attr('style','display:none');
        jq("input[id='document.newMaintainableObject.add.vendorCommodities.purchasingCommodityCode']").attr('style','display:none');
        jq("input[title='Search Commodity Code']").attr('src','');
        jq("input[title='Search Commodity Code']").attr('alt','');
        jq("input[id='document.newMaintainableObject.add.vendorCommodities.commodityDefaultIndicator']").attr('style','display:none');
        jq("input[id='document.newMaintainableObject.add.vendorContracts.vendorContractName']").attr('style','display:none');
        jq("input[id='document.newMaintainableObject.add.vendorContracts.vendorContractDescription']").attr('style','display:none');
        jq("select[id='document.newMaintainableObject.add.vendorContracts.vendorCampusCode']").attr('style','display:none');
        jq("input[title='Search Campus']").attr('src','');
        jq("input[title='Search Campus']").attr('alt','');
        jq("input[id='document.newMaintainableObject.add.vendorContracts.vendorContractBeginningDate']").attr('style','display:none');
        jq("img[id='document.newMaintainableObject.add.vendorContracts.vendorContractBeginningDate_datepicker']").attr('src','');
        jq("img[id='document.newMaintainableObject.add.vendorContracts.vendorContractBeginningDate_datepicker']").attr('alt','');
        jq("input[id='document.newMaintainableObject.add.vendorContracts.vendorContractEndDate']").attr('style','display:none');
        jq("img[id='document.newMaintainableObject.add.vendorContracts.vendorContractEndDate_datepicker']").attr('src','');
        jq("img[id='document.newMaintainableObject.add.vendorContracts.vendorContractEndDate_datepicker']").attr('alt','');
        jq("select[id='document.newMaintainableObject.add.vendorContracts.contractManagerCode']").attr('style','display:none');
        jq("input[title='Search Contract Manager']").attr('src','');
        jq("input[title='Search Contract Manager']").attr('alt','');
        jq("select[id='document.newMaintainableObject.add.vendorContracts.purchaseOrderCostSourceCode']").attr('style','display:none');
        jq("input[title='Search PO Cost Source']").attr('src','');
        jq("input[title='Search PO Cost Source']").attr('alt','');
        jq("select[id='document.newMaintainableObject.add.vendorContracts.vendorPaymentTermsCode']").attr('style','display:none');
        jq("select[id='document.newMaintainableObject.add.vendorContracts.vendorShippingPaymentTermsCode']").attr('style','display:none');
        jq("input[title='Search Shipping Terms']").attr('src','');
        jq("input[title='Search Shipping Terms']").attr('alt','');
        jq("select[id='document.newMaintainableObject.add.vendorContracts.vendorShippingTitleCode']").attr('style','display:none');
        jq("input[id='document.newMaintainableObject.add.vendorContracts.vendorContractExtensionDate']").attr('style','display:none');
        jq("img[id='document.newMaintainableObject.add.vendorContracts.vendorContractExtensionDate_datepicker']").attr('src','');
        jq("img[id='document.newMaintainableObject.add.vendorContracts.vendorContractExtensionDate_datepicker']").attr('alt','');
        jq("input[id='document.newMaintainableObject.add.vendorContracts.organizationAutomaticPurchaseOrderLimit']").attr('style','display:none');
        jq("input[id='document.newMaintainableObject.add.vendorContracts.active']").attr('style','display:none');

    }else{
        jq("select[id='document.newMaintainableObject.vendorHeader.vendorTypeCode']").attr('style','display:inline');
        jq("input[title='Search Vendor Type']").attr('src',sessionStorage.getItem("lookupSrc"));
        jq("input[title='Search Vendor Type']").attr('alt','Search Vendor Type');
        jq("input[id='document.newMaintainableObject.vendorHeader.vendorForeignIndicator']").attr('style','display:inline');
        jq("select[id='document.newMaintainableObject.vendorHeader.vendorForeignIndicator']").attr('style','display:inline');
        jq("input[id='document.newMaintainableObject.vendorHeader.vendorTaxNumber']").attr('style','display:inline');
        jq("input[id='document.newMaintainableObject.vendorHeader.vendorTaxTypeCodeFEIN']").attr('style','display:inline');
        jq("input[id='document.newMaintainableObject.vendorHeader.vendorTaxTypeCodeSSN']").attr('style','display:inline');
        jq("input[id='document.newMaintainableObject.vendorHeader.vendorTaxTypeCodeNONE']").attr('style','display:inline');
        jq("select[id='document.newMaintainableObject.vendorHeader.vendorOwnershipCode']").attr('style','display:inline');
        jq("input[title='Search Ownership Type']").attr('src',sessionStorage.getItem("lookupSrc"));
        jq("input[title='Search Ownership Type']").attr('alt','Search Ownership Type');
        jq("select[id='document.newMaintainableObject.vendorHeader.vendorOwnershipCategoryCode']").attr('style','display:inline');
        jq("input[title='Search Ownership Type Category']").attr('src',sessionStorage.getItem("lookupSrc"));
        jq("input[title='Search Ownership Type Category']").attr('alt','Search Ownership Type Category');
        jq("select[id='document.newMaintainableObject.vendorHeader.vendorW9ReceivedIndicator']").attr('style','display:inline');
        jq("select[id='document.newMaintainableObject.vendorHeader.vendorW8BenReceivedIndicator']").attr('style','display:inline');
        jq("input[id='document.newMaintainableObject.vendorHeader.vendorFederalWithholdingTaxBeginningDate']").attr('style','display:inline');
        jq("img[id='document.newMaintainableObject.vendorHeader.vendorFederalWithholdingTaxBeginningDate_datepicker']").attr('src',sessionStorage.getItem("datePickerSrc"));
        jq("img[id='document.newMaintainableObject.vendorHeader.vendorFederalWithholdingTaxBeginningDate_datepicker']").attr('alt','Date selector');
        jq("input[id='document.newMaintainableObject.vendorHeader.vendorFederalWithholdingTaxEndDate']").attr('style','display:inline');
        jq("img[id='document.newMaintainableObject.vendorHeader.vendorFederalWithholdingTaxEndDate_datepicker']").attr('src',sessionStorage.getItem("datePickerSrc"));
        jq("img[id='document.newMaintainableObject.vendorHeader.vendorFederalWithholdingTaxEndDate_datepicker']").attr('alt','Date selector');
        jq("select[id='document.newMaintainableObject.vendorHeader.vendorDebarredIndicator']").attr('style','display:inline');
        jq("select[id='document.newMaintainableObject.currencyTypeId']").attr('style','display:inline');
        jq("input[title='Search Currency Type']").attr('src',sessionStorage.getItem("lookupSrc"));
        jq("input[title='Search Currency Type']").attr('alt','Search Currency Type');
        jq("select[id='document.newMaintainableObject.vendorPaymentTermsCode']").attr('style','display:inline');
        jq("input[title='Search Payment Terms']").attr('src',sessionStorage.getItem("lookupSrc"));
        jq("input[title='Search Payment Terms']").attr('alt','Search Payment Terms');
        jq("select[id='document.newMaintainableObject.paymentMethodId']").attr('style','display:inline');
        jq("input[title='Search Payment Method']").attr('src',sessionStorage.getItem("lookupSrc"));
        jq("input[title='Search Payment Method']").attr('alt','Search Payment Method');
        jq("select[id='document.newMaintainableObject.vendorPrepaymentIndicator']").attr('style','display:inline');
        jq("select[id='document.newMaintainableObject.vendorCreditCardIndicator']").attr('style','display:inline');
        jq("input[id='document.newMaintainableObject.vendorMinimumOrderAmount']").attr('style','display:inline');
        jq("select[id='document.newMaintainableObject.vendorShippingTitleCode']").attr('style','display:inline');
        jq("input[title='Search Shipping Title']").attr('src',sessionStorage.getItem("lookupSrc"));
        jq("input[title='Search Shipping Title']").attr('alt','Search Shipping Title');
        jq("select[id='document.newMaintainableObject.vendorShippingPaymentTermsCode']").attr('style','display:inline');
        jq("input[title='Search Shipping Payment Terms']").attr('src',sessionStorage.getItem("lookupSrc"));
        jq("input[title='Search Shipping Payment Terms']").attr('alt','Search Shipping Payment Terms');
        jq("input[id='document.newMaintainableObject.vendorDunsNumber']").attr('style','display:inline');
        jq("input[id='document.newMaintainableObject.vendorUrlAddress']").attr('style','display:inline');
        jq("select[id='document.newMaintainableObject.vendorConfirmationIndicator']").attr('style','display:inline');
        jq("select[id='document.newMaintainableObject.vendorRestrictedIndicator']").attr('style','display:inline');
        jq("textarea[id='document.newMaintainableObject.vendorRestrictedReasonText']").attr('style','display:inline');
        jq("input[id='document.newMaintainableObject.vendorRemitName']").attr('style','display:inline');
        jq("select[id='document.newMaintainableObject.add.vendorTransmissionFormat.vendorTransmissionFormatId']").attr('style','display:inline');
        jq("input[title='Search Transmission Format']").attr('src',sessionStorage.getItem("lookupSrc"));
        jq("input[title='Search Transmission Format']").attr('alt','Search Transmission Format');
        jq("input[id='document.newMaintainableObject.add.vendorTransmissionFormat.vendorPreferredTransmissionFormat']").attr('style','display:inline');
        jq("select[id='document.newMaintainableObject.add.vendorTransmissionFormat.vendorTransmissionTypeId']").attr('style','display:inline');
        jq("input[title='Search Transmission Type']").attr('src',sessionStorage.getItem("lookupSrc"));
        jq("input[title='Search Transmission Type']").attr('alt','Search Transmission Type');
        jq("input[id='document.newMaintainableObject.add.vendorTransmissionFormat.vendorEDIConnectionAddress']").attr('style','display:inline');
        jq("input[id='document.newMaintainableObject.add.vendorTransmissionFormat.vendorEDIConnectionUserName']").attr('style','display:inline');
        jq("input[id='document.newMaintainableObject.add.vendorTransmissionFormat.vendorEDIConnectionPassword']").attr('style','display:inline');
        jq("input[id='document.newMaintainableObject.add.vendorTransmissionFormat.active']").attr('style','display:inline');
        jq("select[id='document.newMaintainableObject.add.vendorHeader.vendorSupplierDiversities.vendorSupplierDiversityCode']").attr('style','display:inline');
        jq("input[title='Search Supplier Diversity']").attr('src',sessionStorage.getItem("lookupSrc"));
        jq("input[title='Search Supplier Diversity']").attr('alt','Search Supplier Diversity');
        jq("input[id='document.newMaintainableObject.add.vendorHeader.vendorSupplierDiversities.active']").attr('style','display:inline');
        jq("select[id='document.newMaintainableObject.add.vendorShippingSpecialConditions.vendorShippingSpecialConditionCode']").attr('style','display:inline');
        jq("input[title='Search Shipping Special Condition']").attr('src',sessionStorage.getItem("lookupSrc"));
        jq("input[title='Search Shipping Special Condition']").attr('alt','Search Shipping Special Condition');
        jq("input[id='document.newMaintainableObject.add.vendorShippingSpecialConditions.active']").attr('style','display:inline');
        jq("input[id='document.newMaintainableObject.add.vendorCommodities.purchasingCommodityCode']").attr('style','display:inline');
        jq("input[title='Search Commodity Code']").attr('src',sessionStorage.getItem("lookupSrc"));
        jq("input[title='Search Commodity Code']").attr('alt','Search Shipping Special Condition');
        jq("input[id='document.newMaintainableObject.add.vendorCommodities.commodityDefaultIndicator']").attr('style','display:inline');
        jq("input[id='document.newMaintainableObject.add.vendorContracts.vendorContractName']").attr('style','display:inline');
        jq("input[id='document.newMaintainableObject.add.vendorContracts.vendorContractDescription']").attr('style','display:inline');
        jq("select[id='document.newMaintainableObject.add.vendorContracts.vendorCampusCode']").attr('style','display:inline');
        jq("input[title='Search Campus']").attr('src',sessionStorage.getItem("lookupSrc"));
        jq("input[title='Search Campus']").attr('alt','Search Campus');
        jq("input[id='document.newMaintainableObject.add.vendorContracts.vendorContractBeginningDate']").attr('style','display:inline');
        jq("img[id='document.newMaintainableObject.add.vendorContracts.vendorContractBeginningDate_datepicker']").attr('src',sessionStorage.getItem("datePickerSrc"));
        jq("img[id='document.newMaintainableObject.add.vendorContracts.vendorContractBeginningDate_datepicker']").attr('alt','Date selector');
        jq("input[id='document.newMaintainableObject.add.vendorContracts.vendorContractEndDate']").attr('style','display:inline');
        jq("img[id='document.newMaintainableObject.add.vendorContracts.vendorContractEndDate_datepicker']").attr('src',sessionStorage.getItem("datePickerSrc"));
        jq("img[id='document.newMaintainableObject.add.vendorContracts.vendorContractEndDate_datepicker']").attr('alt','Date selector');
        jq("select[id='document.newMaintainableObject.add.vendorContracts.contractManagerCode']").attr('style','display:inline');
        jq("input[title='Search Contract Manager']").attr('src',sessionStorage.getItem("lookupSrc"));
        jq("input[title='Search Contract Manager']").attr('alt','Search Contract Manager');
        jq("select[id='document.newMaintainableObject.add.vendorContracts.purchaseOrderCostSourceCode']").attr('style','display:inline');
        jq("input[title='Search PO Cost Source']").attr('src',sessionStorage.getItem("lookupSrc"));
        jq("input[title='Search PO Cost Source']").attr('alt','Search PO Cost Source');
        jq("select[id='document.newMaintainableObject.add.vendorContracts.vendorPaymentTermsCode']").attr('style','display:inline');
        jq("select[id='document.newMaintainableObject.add.vendorContracts.vendorShippingPaymentTermsCode']").attr('style','display:inline');
        jq("input[title='Search Shipping Terms']").attr('src',sessionStorage.getItem("lookupSrc"));
        jq("input[title='Search Shipping Terms']").attr('alt','Search Shipping Terms');
        jq("select[id='document.newMaintainableObject.add.vendorContracts.vendorShippingTitleCode']").attr('style','display:inline');
        jq("input[id='document.newMaintainableObject.add.vendorContracts.vendorContractExtensionDate']").attr('style','display:inline');
        jq("img[id='document.newMaintainableObject.add.vendorContracts.vendorContractExtensionDate_datepicker']").attr('src',sessionStorage.getItem("datePickerSrc"));
        jq("img[id='document.newMaintainableObject.add.vendorContracts.vendorContractExtensionDate_datepicker']").attr('alt','Date selector');
        jq("input[id='document.newMaintainableObject.add.vendorContracts.organizationAutomaticPurchaseOrderLimit']").attr('style','display:inline');
        jq("input[id='document.newMaintainableObject.add.vendorContracts.active']").attr('style','display:inline');

    }
}





