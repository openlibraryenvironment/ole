var currencyType = null;
var currencySymbol = jq("#hdnCurrencyFormatFlag_control").val();
function displayDialogWindow(divID){
    jq(divID).fadeIn(300);
    var popMargTop = (jq(divID).height() + 42) / 2;
    var popMargLeft = (jq(divID).width() + 62) / 2;
    jq(divID).css({
        'margin-top' : -popMargTop,
        'margin-left' : -popMargLeft
    });
    jq(divID).draggable();
    jq('body').append('<div id="mask"></div>');
    jq('#mask').fadeIn(300);
}

function approve(){
    if(jq("#hdnduplicateFlag_control").val() == 'true' || jq('#hdnduplicateFlag span').text().trim() =='true') {
        jq("#hdnBlanketApproveValidationFlag_control").val(false);
        displayDialogWindow("div#OLEInvoice-DuplicationPopUp");
    }
    else if (jq("#hdnAmountExceeds_control").val() == 'true' || jq('#hdnAmountExceeds span').text().trim() =='true'){
        displayDialogWindow("div#MessagePopupSectionForInvoiceAmountExceedsThreshold");
    }
    else if(jq("#hdnBlanketApproveValidationFlag_control").val() == 'true') {
        displayDialogWindow("div#MessagePopupSectionForInvoiceAmountValidation");
    }
}

function closeDuplicationMessage() {
    jq("div#OLEInvoice-DuplicationPopUp").fadeOut(300);
    jq('#mask').fadeOut(300);
    if (jq("#hdnAmountExceeds_control").val() == 'true' || jq('#hdnAmountExceeds span').text().trim() =='true'){
        displayDialogWindow("div#MessagePopupSectionForInvoiceAmountExceedsThreshold");
    }
    if(jq("#hdnSfcFlag_control").val() == 'true') {
        displayDialogWindow("div#MessagePopupSectionForInvoice");
    }
    else if(jq("#hdnBlanketApproveValidationFlag_control").val() == 'true') {
        displayDialogWindow("div#MessagePopupSectionForInvoiceAmountValidation");
    }
    else  if (jq("#hdnsuccessFlag_control").val() == 'true') {
        displayDialogWindow("div#OLEInvoice-ConfirmationPopUp");
    }
    else {
        validateInvoiceSubscriptionApprove();
    }

    }

function closeDuplicationApprovalMessage() {
    jq("div#OLEInvoice-DuplicationApprovePopUp").fadeOut(300);
    jq('#mask').fadeOut(300);
    if(jq("#hdnSfcFlagForBlankApp_control").val() == 'true') {
        displayDialogWindow("div#MessagePopupSectionForInvoiceBlankApp");
    }
    else if(jq("#hdnAmountExceedsForBlanketApprove_control").val() == 'true'){
        displayDialogWindow("div#MessagePopupSectionForInvoiceAmountExceedsForBlanketApprove");
    }
    else  if (jq("#hdnBlanketApproveSuccessFlag_control").val() == 'true') {
        displayDialogWindow("div#OLEInvoice-ConfirmationPopUp");
    }
    else {
        validateInvoiceSubscriptionBlanketApprove();
    }
}

jq("#invoice-vendorHeaderIdentifier_control").live("change",function() {
    //jq('#invoiceVendorBtn').click();
    jq("#unsaved_control").val("true");
});
function revertCurrencyType(){

    if(currencyType != null){
        jq("#invoice-currencyType_control").val(currencyType);
    }
}


jq("#invoice-currencyType_control").live("change", function() {
    jq('#invoiceCurrencyTypeBtn').click();
});


/*function MessageInvoiceCurrencyPopup() {
    if(jq("#hiddenCurrencyOverrideBtn_control").val() == "true") {
        displayDialogWindow("div#MessagePopUpSectionForCurrencyOverride");
    }
}*/
/*jq("#invoice-invoiceNumber").live("focusout",function() {
    jq('#invoiceNumberBtn').click();
});*/

jq(".prorateByQuantityClass").live("click",function(){
    jq(".prorateByDollarClass").removeAttr("checked");
    jq(".noProrateClass").removeAttr("checked");
    jq(".prorateByManualClass").removeAttr("checked");
    jq("#"+jq(this).attr('id')).attr("checked","true");
    jq('#proratedSurchargeBtn').click();

});

jq(".prorateByDollarClass").live("click",function(){
    jq(".prorateByQuantityClass").removeAttr("checked");
    jq(".noProrateClass").removeAttr("checked");
    jq(".prorateByManualClass").removeAttr("checked");
    jq("#"+jq(this).attr('id')).attr("checked","true");
    jq('#proratedSurchargeBtn').click();

});

jq(".prorateByManualClass").live("click",function(){
    jq(".prorateByQuantityClass").removeAttr("checked");
    jq(".prorateByDollarClass").removeAttr("checked");
    jq(".noProrateClass").removeAttr("checked");
    jq("#"+jq(this).attr('id')).attr("checked","true");
    jq('#proratedSurchargeBtn').click();
});

jq(".noProrateClass").live("click",function(){
    jq(".prorateByQuantityClass").removeAttr("checked");
    jq(".prorateByDollarClass").removeAttr("checked");
    jq(".prorateByManualClass").removeAttr("checked");
    jq("#"+jq(this).attr('id')).attr("checked","true");
    jq('#proratedSurchargeBtn').click();

});

function selectNoProrate(){
    jq(".noProrateClass").removeAttr("checked");
    jq(".prorateByQuantityClass").removeAttr("checked");
    jq(".prorateByDollarClass").removeAttr("checked");
    jq("#myAccount_noProrate_control").attr("checked","true");
    if(jq(".prorateByManualClass").is(':checked')) {

        jq('#proratedSurchargeBtn').click();
    }
    jq(".prorateByManualClass").removeAttr("checked");
}

function save() {
    if(jq("#hdnduplicateSaveFlag_control").val() == 'true') {
        displayDialogWindow("div#OLEInvoice-DuplicationSavePopUp");
    }
    removeDollarSymbol();
    removeDollarSymbolsForProcessItem();
}

function closeInvoiceVendorSavePopUp(){
    jq("div#OLEInvoice-DuplicationSavePopUp").fadeOut(300);
    jq("#mask").fadeOut(300);
}


function route(){
    if((jq("#hdnduplicateRouteFlag_control").val() == 'true' || jq('#hdn span').text().trim() =='true')) {
        jq("#unsaved_control").val(false);
        jq("#hdnValidationFlag_control").val(false);
        displayDialogWindow("div#OLEInvoice-DuplicationRoutePopUp");
    }
    else if(jq("#hdnSfcFlag_control").val() == 'true') {
        displayDialogWindow("div#MessagePopupSectionForInvoice");
    }
    else if(jq("#hdnValidationFlag_control").val() == 'true') {
        jq("#unsaved_control").val(false);
        displayDialogWindow("div#MessagePopupSectionForInvoiceValidation");
        jq('#mask').fadeOut(300);
    }
    else  if (jq("#hdnsuccessFlag_control").val() == 'true') {
        displayDialogWindow("div#OLEInvoice-ConfirmationPopUp");
    }
    else{
        validateInvoiceSubscriptionApprove();
    }
}

function blanketApprove(){
    if(jq("#hdnduplicateApproveFlag_control").val() == 'true' || jq('#hdnduplicateApproveFlag span').text().trim() =='true') {
        jq("#unsaved_control").val(false);
        displayDialogWindow("div#OLEInvoice-DuplicationApprovePopUp");
    }
    else if(jq("#hdnSfcFlagForBlankApp_control").val() == 'true') {
        displayDialogWindow("div#MessagePopupSectionForInvoiceBlankApp");
    }
    else if(jq("#hdnAmountExceedsForBlanketApprove_control").val() == 'true') {
        jq("#unsaved_control").val(false);
        displayDialogWindow("div#MessagePopupSectionForInvoiceAmountExceedsForBlanketApprove");
    }
    else if(jq("#hdnBlanketApproveValidationFlag_control").val() == 'true') {
        jq("#unsaved_control").val(false);
        displayDialogWindow("div#MessagePopupSectionForBlanketApproveInvoiceValidation");
        jq('#mask').fadeOut(300);
    }else  if (jq("#hdnBlanketApproveSuccessFlag_control").val() == 'true') {
        displayDialogWindow("div#OLEInvoice-ConfirmationPopUp");
    }
    else{
        validateInvoiceSubscriptionBlanketApprove();
    }

}

function closeInvoicePopUp(){
    jq("div#MessagePopupSectionForInvoice").fadeOut(300);
    jq("#mask").fadeOut(300);
    validateInvoiceSubscriptionApprove();
}

function closeInvoiceApprovePopUp(){
    jq("div#MessagePopupSectionForInvoiceBlankApp").fadeOut(300);
    jq("#mask").fadeOut(300);
    validateInvoiceSubscriptionBlanketApprove();
}
function refreshCurrentItems(){
    jq('#hiddenButtonForCurrentItems').focus().click();
}

function closeInvoiceValidationPopUp(){
    jq("div#MessagePopupSectionForInvoiceValidation").fadeOut(300);
    jq("#mask").fadeOut(300);
    validateInvoiceNumber();
}

function closeInvoiceBlanketApproveValidationPopUp(){
    jq("#unsaved_control").val(false);
    jq("div#MessagePopupSectionForBlanketApproveInvoiceValidation").fadeOut(300);
    jq("#mask").fadeOut(300);
}

function closeInvoiceBlanketApproveSubscriptionDateValidationPopUp(){
    jq("div#MessagePopupSectionForBlanketApproveSubscriptionDateValidation").fadeOut(300);
    jq("#mask").fadeOut(300);
    validateInvoiceAmountBlanketApprove();
}

function closeInvoiceSubscriptionDateValidationPopUp(){
    jq("div#MessagePopupSectionForSubscriptionDateValidation").fadeOut(300);
    jq("#mask").fadeOut(300);
    validateInvoiceAmount();
}

function closeInvoiceAmountExceedsApprovalPopUp(){
    jq("div#MessagePopupSectionForInvoiceAmountExceedsThreshold").fadeOut(300);
    jq("#mask").fadeOut(300);
    if(jq("#hdnSfcFlag_control").val() == 'true') {
        displayDialogWindow("div#MessagePopupSectionForInvoice");
    }
    else  if (jq("#hdnsuccessFlag_control").val() == 'true') {
        displayDialogWindow("div#OLEInvoice-ConfirmationPopUp");
    }
    else {
        validateInvoiceSubscriptionApprove();
    }
    }

function closeInvoiceAmountExceedsBlankApprovalPopUp(){
    jq("div#MessagePopupSectionForInvoiceAmountExceedsForBlanketApprove").fadeOut(300);
    jq("#mask").fadeOut(300);
    if(jq("#hdnSfcFlagForBlankApp_control").val() == 'true') {
        displayDialogWindow("div#MessagePopupSectionForInvoiceBlankApp");
    }
    else if(jq("#hdnBlanketApproveValidationFlag_control").val() == 'true') {
        jq("#unsaved_control").val(false);
        displayDialogWindow("div#MessagePopupSectionForBlanketApproveInvoiceValidation");
        jq('#mask').fadeOut(300);
    }
    else{
        validateInvoiceSubscriptionBlanketApprove();
    }
}

function onChangePriceScript() {
    jq("#updatePriceBtn").focus().click();
   /* writeHiddenToForm('methodToCall', 'updatePrice');
    jQuery.fancybox.close();
    jQuery('#kualiForm').submit();*/
}

function onChangePOPriceScript() {
    submitForm('updatePOPrice',null,null,true,function(){
        removeDollarSymbolsForProcessItem();
    });
}

function validateInvoiceAmount(){
    if(jq("#hdnValidationFlag_control").val() == 'true') {
        displayDialogWindow("div#MessagePopupSectionForInvoiceValidation");
        /* jq('#mask').fadeOut(300);*/
    } else {
        successConfirmation();
    }
}

function validateInvoiceNumber(){
    if(jq("#hdnduplicateRouteFlag_control").val() == 'true' || jq('#hdnduplicateRouteFlag span').text().trim() =='true') {
        displayDialogWindow("div#OLEInvoice-DuplicationRoutePopUp");
        /*jq('#mask').fadeOut(300);*/
    }
    else{
        successConfirmation();
    }
}

function successConfirmation(){
    if (jq("#hdnsuccessFlag_control").val() == 'true') {
        displayDialogWindow("div#OLEInvoice-ConfirmationPopUp");
    }else{
        unsaved();
    }
}

function validateInvoiceNo(){
    if(jq("#hdnduplicateValidationFlag_control").val() == 'true') {
        displayDialogWindow("div#OLEInvoice-DuplicationValidationPopUp");
    }
}

function closevalidateInvoiceNo(){
    jq("div#OLEInvoice-DuplicationValidationPopUp").fadeOut(300);
    jq("#mask").fadeOut(300);
}

function closeInvoiceVendorRoutePopUp(){
    jq("div#OLEInvoice-DuplicationRoutePopUp").fadeOut(300);
    jq("#mask").fadeOut(300);
    if(jq("#hdnSfcFlag_control").val() == 'true') {
        displayDialogWindow("div#MessagePopupSectionForInvoice");
    }
    else if(jq("#hdnValidationFlag_control").val() == 'true') {
        jq("#unsaved_control").val(false);
        displayDialogWindow("div#MessagePopupSectionForInvoiceValidation");
        jq('#mask').fadeOut(300);
    }
    else  if (jq("#hdnsuccessFlag_control").val() == 'true') {
        displayDialogWindow("div#OLEInvoice-ConfirmationPopUp");
    }
    else {
        validateInvoiceSubscriptionApprove();
    }
}

function unsaved(){
    jq("#unsaved_control").val("false");
}

function validateInvoiceAmountBlanketApprove(){
	if(jq("#hdnBlanketApproveValidationFlag_control").val() == 'true') {
        displayDialogWindow("div#MessagePopupSectionForBlanketApproveInvoiceValidation");
        jq('#mask').fadeOut(300);
    }
    else{
        if(jq("#unsaved_control").val()=='false'){
            successConfirmationBlanketApprove();
        }

    }
}

function successConfirmationBlanketApprove(){
    if (jq("#hdnBlanketApproveSuccessFlag_control").val() == 'true') {
        displayDialogWindow("div#OLEInvoice-ConfirmationPopUp");
    }else{
        unsaved();
    }
}

function validateInvoiceSubscriptionBlanketApprove(){
    if(jq("#hdnBlanketApproveSubscriptionDateValidationFlag_control").val() == 'true') {
        displayDialogWindow("div#MessagePopupSectionForBlanketApproveSubscriptionDateValidation");
        jq('#mask').fadeOut(300);
    }
    else{
        validateInvoiceAmountBlanketApprove();
        }


}

function validateInvoiceSubscriptionApprove(){
    if(jq("#hdnSubscriptionDateValidationFlag_control").val() == 'true') {
        displayDialogWindow("div#MessagePopupSectionForSubscriptionDateValidation");
        jq('#mask').fadeOut(300);
    }
    else{
        validateInvoiceAmount();
        }


}

function closeDocument(){
    if(jq("#unsaved_control").val()=='false' && jq("#hdnblankApproveFlag_control").val()=='true'){
        jq("#invoice_close_btn").focus().click();
        unsaved();
    }
    jq("#hdnblankApproveFlag_control").val(true);
}
function toggleCollapseInvoice() {
    if(jq("#OLEInvoiceView-vendor_disclosureContent").css("display")=="block"){
        jq("#OLEInvoiceView-vendor_toggle").focus().click();
    }
    if(jq("#OLEInvoiceView-invoiceInfo_disclosureContent").css("display")=="block"){
        jq("#OLEInvoiceView-invoiceInfo_toggle").focus().click();
    }
   /* if(jq("#OleInvoiceDocument-invoiceItems_disclosureContent").css("display")=="block"){
        jq("#OleInvoiceDocument-invoiceItems_toggle").focus().click();
    }*/
    if(jq("#OLEInvoice-ProcessItem-AdditionalCharges_disclosureContent").css("display")=="block"){
        jq("#OLEInvoice-ProcessItem-AdditionalCharges_toggle").focus().click();
    }
    if(jq("#OLEInvoiceView-accountSummary_disclosureContent").css("display")=="block"){
        jq("#OLEInvoiceView-accountSummary_toggle").focus().click();
    }
    if(jq("#Uif-Inv-DocumentNotesSection_disclosureContent").css("display")=="block"){
        jq("#Uif-Inv-DocumentNotesSection_toggle").focus().click();
    }
    if(jq("#Uif-Inv-DocumentAdHocRecipientsSection_disclosureContent").css("display")=="block"){
        jq("#Uif-Inv-DocumentAdHocRecipientsSection_toggle").focus().click();
    }
    if(jq("#Uif-Inv-DocumentRouteLogSection_disclosureContent").css("display")=="block"){
        jq("#Uif-Inv-DocumentRouteLogSection_toggle").focus().click();
    }
    if(jq("#Uif-Inv-DocumentOverviewSection_disclosureContent").css("display")=="block"){
        jq("#Uif-Inv-DocumentOverviewSection_toggle").focus().click();
    }
}


function toggleCollapseAllSections() {
    if(jq("#Uif-Inv-DocumentOverviewSection_disclosureContent").css("display")=="block" && jq( "#overviewFlag_control").val()=="false"){
        jq("#Uif-Inv-DocumentOverviewSection_toggle").focus().click();
    }
    if(jq("#OLEInvoiceView-vendor_disclosureContent").css("display")=="block" && jq( "#vendorInfoFlag_control").val()=="false"){
        jq("#OLEInvoiceView-vendor_toggle").focus().click();
    }
    if(jq("#OLEInvoiceView-invoiceInfo_disclosureContent").css("display")=="block" && jq( "#invoiceInfo_control").val()=="false"){
        jq("#OLEInvoiceView-invoiceInfo_toggle").focus().click();
    }
    if(jq("#OleInvoiceDocument-invoiceItems_disclosureContent").css("display")=="block" && jq( "#currentItemsFlag_control").val()=="false"){
        jq("#OleInvoiceDocument-invoiceItems_toggle").focus().click();
    }
    if(jq("#OLEInvoiceView-ProcessItem-AdditionalCharges_disclosureContent").css("display")=="block" && jq( "#additionalChargesFlag_control").val()=="false"){
        jq("#OLEInvoiceView-ProcessItem-AdditionalCharges_toggle").focus().click();
    }
    /* if(jq("#OLEInvoice-ProcessItem-AdditionalCharges_disclosureContent").css("display")=="block" && jq( "#vendorInfoFlag_control").val()=="false"){
     jq("#OLEInvoice-ProcessItem-AdditionalCharges_toggle").focus().click();
     }*/
    if(jq("#OLEInvoiceView-accountSummary_disclosureContent").css("display")=="block" && jq( "#accountSummaryFlag_control").val()=="false"){
        jq("#OLEInvoiceView-accountSummary_toggle").focus().click();
    }
    if(jq("#Uif-Inv-DocumentNotesSection_disclosureContent").css("display")=="block" && jq( "#notesAndAttachmentFlag_control").val()=="false"){
        jq("#Uif-Inv-DocumentNotesSection_toggle").focus().click();
    }
    if(jq("#Uif-Inv-DocumentAdHocRecipientsSection_disclosureContent").css("display")=="block" && jq( "#adHocRecipientsFlag_control").val()=="false"){
        jq("#Uif-Inv-DocumentAdHocRecipientsSection_toggle").focus().click();
    }
    if(jq("#Uif-Inv-DocumentRouteLogSection_disclosureContent").css("display")=="block" && jq( "#routeLogFlag_control").val()=="false"){
        jq("#Uif-Inv-DocumentRouteLogSection_toggle").focus().click();
    }
}

function clonePopUp(){
    if(jq("#hdnCloneFlag_control").val() == 'true' || jq('#hdnCloneFlag span').text().trim() =='true') {
        displayDialogWindow("div#OLEInvoice-ClonePopUp");
        jq("#mask").fadeOut(300);
    }
}

function addVendorAlias(){
    var id = jq("#invoice-vendorHeaderIdentifier_control").val();
    if(id.length > 0){
        var amount = jq("#invoice-invoiceNumber_control").val();
        if(amount.length > 0){
            jq("#invoice-invoiceDate_control").focus();
        } else{
            jq("#invoice-invoiceNumber_control").focus();
        }
    }else{
        jq("#invoice-vendorHeaderIdentifier_control").focus();
    }
}

jq(document).keypress(function(e) {
    if(e.which == 13) {
        if(e.target.id == "OleInvoice_POLookup_control"){
            if(jq( "#OleInvoice_POLookup_control").val()!=""){
                jq(this).blur();
                jq('#addProcessItem-button').focus().click();
                return false;
            }
            else{
                jq("#OleInvoice_POLookup_control").focus();
                return false;
            }
        }else{
            e.preventDefault();
            jq('#invoice-invoiceDate_control').focus();
        }
    }
});

jq(window).load(function () {
    if (jq("#processItemFlag_control").val() == "true") {
        if (jq("#OLEInvoiceView-processItems-Wrapper span input").attr('style') != null) {
            jq("#ProcessItemsLink").click();
        }
    }
    var id = jq("#invoice-vendorHeaderIdentifier_control").val();
    if(id.length > 0){
        var amount = jq("#invoice-invoiceNumber_control").val();
        if(amount.length > 0){
            jq("#invoice-invoiceDate_control").focus();
        } else{
            jq("#invoice-invoiceNumber_control").focus();
        }
    }else{
        jq("#invoice-vendorHeaderIdentifier_control").focus();
    }
});

jq(document).ready(function(){
/*    jq(":input").live("keypress",function(){
        jq("#unsaved_control").val("false");
    });*/
    jq("#invoice-invoiceNumber_control").live("blur",function() {
        if(jq("#invoice-invoiceNumber_control").val().length > 0){
            submitForm('validateInvoiceNumber', null, null, true, function () {
                validateInvoiceNo();
            });
        }
    });
    function unloadPage(){
        if(jq("#unsaved_control").val()=='true'){
            return confirm( getMessage(kradVariables.MESSAGE_KEY_DIRTY_FIELDS));
        }
    }
    jq(".uif-actionImage").live("click",function(){
       unsaved();
    });
    jq(".uif-dropdownControl").live("change",function() {
        jq("#unsaved_control").val("true");
    });
    window.onbeforeunload = unloadPage;
    saveCurrencyType();
    modifyAccountNumber();
    jq("OLEInvoiceView-ProcessItem").live("click",function(){
        removeDollarSymbol();
    });

});

jq("#invoice-paymentMethod_control").live("change",function() {
    jq('#hiddenButtonForChangeToPaymentMethod').focus().click();
});

/*function closeInvoiceCurrencyOverridePopUp(){
    jq("div#MessagePopUpSectionForCurrencyOverride").fadeOut(300);
    jq("#mask").fadeOut(300);
}*/

function updateExchangeRate() {
    jq("#updateExchangeRateBtn").focus().click();
}
jq(".uif-detailsAction").live("click",function(){
    var id =jq(this).attr("id");
    var index = id.substring(id.indexOf("_line"),id.length).replace("_line","");
    jq("#OLEInvoiceView-invoiceItem-relatedView_Btn_line"+index).focus().click();
});

jq("#Uif-Inv-DocumentRouteLogSection_toggle_col").live("click",function(){
    jq("#routeLogDisplayBtn").focus().click();
});

function oleInvoicePager(linkElement, collectionId) {
    var link = jQuery(linkElement);
    if (link.parent().is(kradVariables.ACTIVE_CLASS)) return;
    retrieveComponent(collectionId, "refresh", null, {
    	"pageNumber": link.data(kradVariables.PAGE_NUMBER_DATA)
    }, true);
}

function oleInvoiceRowDetails(lineId, collectionId, show) {
    retrieveComponent(collectionId, "refresh", null, {
    	"selectRowDetails" : lineId,
    	"showSelectedRowDetails" : show
    }, true);
}




function saveCurrencyType() {
    jq("#invoice-currencyType").live("change", function() {
        currencyType=jq("#invoice-currencyType_control").val();
    });
}

function clearVendorName() {
    jq("#invoice-vendorHeaderIdentifier_control").val("");
}

jq(window).load(function () {
    removeDollarSymbol();
});

function removeDollarSymbol() {
    for (var i = 0; i < 2000; i++) {
        var itemDiscount = jq("#OleInvoiceView-invoiceItems_line" + i + "_itemDiscount_control").val();
        if (itemDiscount != null) {
            itemDiscount = itemDiscount.replace(currencySymbol, "");
            jq("#OleInvoiceView-invoiceItems_line" + i + "_itemDiscount_control").val(itemDiscount);
        } else {
            break;
        }
    }
    }

function removeDollarSymbolsForProcessItem() {
    for(var itemSize = 0;itemSize<10;itemSize++) {
        if (document.getElementById('OLEInvoiceView-processItems-currentItems_line0_line' + itemSize + '_itemDiscount_control') != null) {
            var itemDiscount = jq("#OLEInvoiceView-processItems-currentItems_line0_line" + itemSize + "_itemDiscount_control").val();
            itemDiscount = itemDiscount.replace(currencySymbol, "");
            jq("#OLEInvoiceView-processItems-currentItems_line0_line" + itemSize + "_itemDiscount_control").val(itemDiscount);
        }
    }

}


function onChangeSequenceScript(id) {
    var indexValue = id;
    var sequenceObject={};
    sequenceObject['indexNo'] = id;
    submitForm('modifySequenceOrder', {sequenceObject:JSON.stringify(sequenceObject)}, null, null, null);

}

function modifyOffsetAccountNumber(obj) {
    var id = obj.id;
    var  addId = "#"+ id + "_control";
    var accountNumber = jq(addId).val();
    if (accountNumber != null) {
        accountNumber = accountNumber.toUpperCase();
        jq(addId).val(accountNumber);
    }
}

function modifyAccountNumber() {
    jq("#OLEInvoiceView-Item-accountingLines_line0_add_accountNumber_control").live("change", function () {
        var accountNumber = jq("#OLEInvoiceView-Item-accountingLines_line0_add_accountNumber_control").val()
        if (accountNumber != null) {
            accountNumber = accountNumber.toUpperCase();
            jq("#OLEInvoiceView-Item-accountingLines_line0_add_accountNumber_control").val(accountNumber);
        }
    });
}

