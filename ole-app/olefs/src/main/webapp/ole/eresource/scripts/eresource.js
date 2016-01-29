var unsaved = false;

function displayDialogWindow(divID){

    jq(divID).fadeIn(300);
    var popMargTop = (jq(divID).height() + 24) / 2;
    var popMargLeft = (jq(divID).width() + 24) / 2;
    jq(divID).css({
        'margin-top' : -popMargTop,
        'margin-left' : -popMargLeft
    });
    jq(divID).draggable();
    jq('body').append('<div id="mask"></div>');
    jq('#mask').fadeIn(300);
}

function instance() {
    displayDialogWindow("div#OLEEResourceRecordView-InstanceSelectionPopUp");
    //jq("#selectInstance input[type='image']").hide();
}

function instanceLink(docNumber,eResourceId) {
    if(jq("#selectInstanceId input[type='radio']:checked").val() == "linkExistingInstance") {
        window.open("olesearchcontroller?viewId=OLEEResourceBibView&methodToCall=start&tokenId="+docNumber+"&eResourceId="+eResourceId+"&eInstance=linkExistingInstance");
        jq("#OLEEResourceRecordView-SelectFlag").val('true');
        closeLinkInstance();
    }
    else if(jq("#selectInstanceId input[type='radio']:checked").val() == "createNewInstance") {

        window.open("olesearchcontroller?viewId=OLEEResourceBibView&link=create&methodToCall=start&tokenId="+docNumber+"&eResourceId="+eResourceId);
        jq("#OLEEResourceRecordView-SelectFlag").val('true');
        closeLinkInstance();
    }
    else {

        window.open("editorcontroller?viewId=EditorView&methodToCall=load&docCategory=work&docType=bibliographic&docFormat=marc&editable=true&tokenId="+docNumber+"&eResourceId="+eResourceId);
        jq("#OLEEResourceRecordView-createNewInstanceFlag").val('true');
        closeLinkInstance();
    }
}

function closeLinkInstance() {
    jq("div#OLEEResourceRecordView-InstanceSelectionPopUp").fadeOut(300);
    jq('#mask').fadeOut(300);
}

function saveERSDocument() {
    jq("#ERSLicense-Save").focus().click();
}

function saveERSAccessDocument() {
    setSpaceField();
    if(jq("#accessMessageField span").text() == undefined || jq("#accessMessageField span").text() == null || jq("#accessMessageField span").text() == ''){
        jq("#ERSAccess-Save").focus().click();
    }
}

function removeInstance() {
    jq("#SaveInstance-button").focus().click();
}

jq(document).ready(function () {

    jq("#OLEEResourceRecordView-Menu a").click(function(){
        if(this.id=='E-Resource-Instance-Navigation'){
           /* window.setTimeout(function () {
                jq("#instanceDetails").removeClass("DTTT_button DTTT_button_text xls");
                jq("#instanceDetails").removeClass("DTTT_button DTTT_button_text csv");
                jq("#instanceDetails").tableExport();
            }, 3000)*/
        }
    });

    jq("#publisherSuggest_control").blur(function(){
        jq("#refreshPublisher").focus().click();
    })

    jq("#vendorSuggest_control").blur(function(){
        jq("#refreshVendor").focus().click();
    })

    jq("#focusOutControl").attr('disabled','disabled');

    jq("input:text").live("click", function () {
        if (jq(this).attr("id") == undefined) {
            if (jq(this).parent().parent().attr("class") == "dataTables_filter") {
                jq(this).attr("id", "dataTextSearchBox");
            }
        }
    })
    jq("#cancellation_decision_date_control").live("keypress",function (e)
    {
        if(e.keyCode==8){

        }else{
            e.preventDefault();
        }

    });

    jq("#cancellation_effective_date_control").live("keypress",function (e)
    {
        if(e.keyCode==8){

        }else{
            e.preventDefault();
        }

    });

    jq("#current_subscription_startDate_control").live("keypress",function (e)
    {
        if(e.keyCode==8){

        }else{
            e.preventDefault();
        }

    });

    jq("#current_subscription_endDate_control").live("keypress",function (e)
    {
        if(e.keyCode==8){

        }else{
            e.preventDefault();
        }

    });
    jq("#initial_subscription_startDate_control").live("keypress",function (e)
    {
        if(e.keyCode==8){

        }else{
            e.preventDefault();

        }

    });



    unsaved = false;
    jq("#OLEEResourceRecordView").live("keypress",function(){
        unsaved = true;
    });
    jq("#OLEEResourceRecordView").live("change",function(){
        unsaved = true;
    });
    jq(":input").live("change keypress",function(){
        unsaved = true;
    });
    jq("#requestor_line0_fieldset").live("click",function(){
        unsaved = true;
    });
    jq("#selector_line0_fieldset").live("click",function(){
        unsaved = true;
    });
    jq("#u474_fieldset").live("click",function(){
        unsaved = true;
    });
    function unloadPage(){
        if(unsaved){
            var message  = "This page is asking you to confirm that you want to leave - data you have entered may not be saved.";
            return message;
        }
    }
    jq('form').bind('submit', function() {
        unsaved = false;
    });
    jq("#pageSizeHoldings").live("change", function () {
        var pageSize = jq("#pageSizeHoldings").val();
        retrieveComponent('OLEEResourceRecordView-InstanceDetailSection', "newSize", null, {
            "pageSize":pageSize
        }, true);

    })
    window.onbeforeunload = unloadPage;
    setRecipientSelectorValue();
});

function defaultCoverage() {
    displayDialogWindow("div#OLEEResourceRecordView-InstanceDetailPopUp");
    coverageDates();
}

function defaultPerpetualAccess() {
    displayDialogWindow("div#OLEEResourceRecordView-InstanceDetailPopUp");
    perpetualAccessDates();
}

function saveCoverageOrPerpetualAccess() {
    closeInstanceDate();
    if(jq("#defaultCovStartDateErrorMessage span").text().length == 0
        && jq("#defaultCovEndDateErrorMessage span").text().length == 0
        && jq("#defaultPerAccStartDateErrorMessage span").text().length == 0
        && jq("#defaultPerAccEndDateErrorMessage span").text().length == 0) {
        jq("#hdnRefreshDefaultDate").focus().click();
    }
}

function closeInstanceDate() {
    if(jq("#defaultCovStartDateErrorMessage span").text().length == 0
        && jq("#defaultCovEndDateErrorMessage span").text().length == 0
        && jq("#defaultPerAccStartDateErrorMessage span").text().length == 0
        && jq("#defaultPerAccEndDateErrorMessage span").text().length == 0) {
        jq("#OLEEResourceRecordView-coverageFlag").val("false");
        jq("#OLEEResourceRecordView-perpetualAccessFlag").val("false");
        jq("div#OLEEResourceRecordView-InstanceDetailPopUp").fadeOut(300);
        jq("#mask").fadeOut(300);
    } else {
        jq("#OLEEResourceRecordView-coverageFlag").val("true");
        jq("#OLEEResourceRecordView-perpetualAccessFlag").val("true");
        coverageDates();
        perpetualAccessDates();
        displayDialogWindow("div#OLEEResourceRecordView-InstanceDetailPopUp");
    }
}

function coverageDates() {
    if (jq("#default-Coverage-Date_control").val() != null) {
        var startDate = jq("#OLEEResourceRecordView-CovStartDate_control").val();
        jq("#default-Coverage-Date_control").val(null);
        jq("#default-Coverage-Date_control").val(startDate.trim());
        startDate = jq("#OLEEResourceRecordView-CovStartDate_control").val(null);
    }
    if (jq("#default-coverageEndDate_control").val() != null) {
        var endDate = jq("#OLEEResourceRecordView-CovEndDate_control").val();
        jq("#default-coverageEndDate_control").val(null);
        jq("#default-coverageEndDate_control").val(endDate.trim());
        endDate = jq("#OLEEResourceRecordView-CovEndDate_control").val(null);
    }
}

function perpetualAccessDates() {
    if (jq("#defaultPerpetual-Date_control").val() != null) {
        var startDate = jq("#OLEEResourceRecordView-PerAccStartDate_control").val();
        jq("#defaultPerpetual-Date_control").val(null);
        jq("#defaultPerpetual-Date_control").val(startDate.trim());
        startDate = jq("#OLEEResourceRecordView-PerAccStartDate_control").val(null);
    }
    if (jq("#default-perpetualAccessEndDate_control").val() != null) {
        var endDate = jq("#OLEEResourceRecordView-PerAccEndDate_control").val();
        jq("#default-perpetualAccessEndDate_control").val(null);
        jq("#default-perpetualAccessEndDate_control").val(endDate.trim());
        endDate = jq("#OLEEResourceRecordView-PerAccEndDate_control").val(null);
    }
}

function makeSeparateLink(content){
    var responseString="";
    var linkArray = content.split(',');
    for(var count=0;count<linkArray.length;count++){
        url = linkArray[count];
        responseString =  "   " +responseString  + "<a  href='" +  url + "' target='_blank'> " + url + "</a>";
    }
    return responseString;
}

function displayLink(successCallback) {
    if(successCallback == 'true' || successCallback == true){
        unsaved = false;
    }
    jq("#OLEEResourceRecordView-InstanceDetails_disclosureContent tbody tr").each(function(i, tr) {
        var value = jq("#eHoldings_url", tr).text();
        var returnValue = makeSeparateLink(value);
        jq("#eHoldings_url",tr).html(returnValue);
    });
}

function displayPopup() {
    displayDialogWindow("div#OLEEResourceRecordView-GokbPopUp");
}

function closeDisplayPopUp() {
    jq("div#OLEEResourceRecordView-GokbPopUp").fadeOut(300);
    jq('#mask').fadeOut(300);
}

function showLightBox(index, type) {
    jq('#hiddenIndex_control').val(index);

    if(type == 'instance') {
        showLightboxComponent('confirmationForInstance');
    }
    if(type == 'eResource') {
        showLightboxComponent('confirmationForEResource');
    }

}

function displayChildEresourcePopup() {
    displayDialogWindow("div#OLEEResourceRecordView-CreateChildE-ResourcePopUp");
}

function next(){
    jq("div#OLEEResourceRecordView-CreateChildE-ResourcePopUp").fadeOut(300);
    jq('#mask').fadeOut(300);
    window.open(jq('#OLEEResourceRecordView-Url_control').val());
}

function closeChildEresourcePopup() {
    jq("div#OLEEResourceRecordView-CreateChildE-ResourcePopUp").fadeOut(300);
    jq('#mask').fadeOut(300);
}

function displayRemoveRelationShipPopup(relationShipType){
    if(relationShipType == "parent"){
        displayDialogWindow("div#OLEEResourceRecordView-RemoveRelationShipParentPopUp");
    } else{
        displayDialogWindow("div#OLEEResourceRecordView-RemoveRelationShipPopUp");
    }
}

function closeRemoveRelationShipPopup(){
    jq("div#OLEEResourceRecordView-RemoveRelationShipPopUp").fadeOut(300);
    jq('#mask').fadeOut(300);
    jq("div#OLEEResourceRecordView-RemoveRelationShipParentPopUp").fadeOut(300);
    jq('#mask').fadeOut(300);
}

function closeDeleteRelatedInstances(){
    //jq(".fancybox-close").click();
    jq("div#OLEEResourceRecordView-DeleteRelatedInstancesPopUp").fadeOut(300);
    jq('#mask').fadeOut(300);
}



function deleteRelatedInstances(){
    jq("div#OLEEResourceRecordView-DeleteRelatedInstancesPopUp").fadeOut(300);
    jq('#mask').fadeOut(300);
    showLightboxComponent("OLEEResourceRecordView-DeleteRelatedInstancesPermanentPopUp");
}

function closeDeleteInstance(){
    jq("div#OLEEResourceRecordView-DeleteRelatedInstancesInfoPopUp").fadeOut(300);
    jq('#mask').fadeOut(300);
}

function eResourceSearch(){
    _initAndOpenLightbox({type: 'iframe', href: jq("#searchUrl_control").val(), height: 'auto', width: '90%', autoSize: true},
        undefined);
}

function validLinkToERes() {
    if (jq.find("div#ERes_Message") != "") {
        displayDialogWindow("div#MessagePopupSectionForLinkToERes");
    } else {
        parent.jq.fancybox.close();
    }
}

function closeMessagePopupSectionForLinkToERes() {
    jq("#search_button").click();
    jq("div#MessagePopupSectionForLinkToERes").fadeOut(300);
    jq('#mask').fadeOut(300);
}

function closeFancyBox(){
    parent.jq.fancybox.close();
}



    function copyToClipBoard(element) {
        jq("#OLEEResourceRecordView-PriceIncreaseAnalysis-EmailText_control").select();
        document.execCommand("copy");
        jq("#OLEEResourceRecordView-PriceIncreaseAnalysis-EmailText_control").append("<span> message copied</span>");
    }


function showTippPopup(packageName,packageId){
    jq("#OLEEResourceRecordView-PackageId_control").val(packageId);
    jq("#OLEEResourceRecordView-Package_control").val(packageName);
    displayDialogWindow('div#tiipResults');
}

function closeLightBoxComponentTipp(id) {
    jq(id).fadeOut(300);
    jq('#mask').fadeOut(300);

}

function showPlatform(packageName, packageId){
    jq("#OLEEResourceRecordView-Package_control").val(packageName);
    jq("#OLEEResourceRecordView-PackageId_control").val(packageId);
    jq("#OLEEResourceRecordView-Platform_control").val("Platform Search Result");
//    retrieveComponent('platFormSection','getPlatforms', null, null);
    submitForm('getPlatforms', null, null, null)
}

function showEResourceNotDeletedPopUp()
{
    /*if (jq("#hdn_accessStatus_control").val() == 'Active') {
     showLightboxComponent('OLEEResourceMainTab_EResourceNotDeletedPopup');
     }
     else if (jq("#hdn_accessStatus_control").val() == 'Inactive') {
     }*/
}


/*function showInstancesNotDeletedPopUp(instancesWithPOs)
 {

 alert(instancesWithPOs)

 if(instancesWithPOs > 0){
 showLightboxComponent('OLEEResourceRecordView-DeleteRelatedInstancesInfoPopUp');
 jq("#OLEEResourceRecordView-DeleteRelatedInstancesPermanentPopUp").hide();

 }
 else if (instancesWithPOs == 0)
 {
 jq("#OLEEResourceRecordView-DeleteRelatedInstancesPermanentPopUp").hide();
 }
 }*/

function refresh(){
    submitForm('refreshPOSection', null, null, null, null);
}

function cancel(){
    window.close();
}

function selectEventOrProblem() {
    jq("input:radio").live("change",function(){
        if(jq(this).attr("name")=="newCollectionLines['document.oleERSEventLogs'].logTypeId"){
            if(jq(this).attr("value")==1){
                jq("#EventLogSection_eResourceproblemType_add_control").attr('disabled', 'true');
                jq("#EventLogSection_eResourceeventType_add_control").removeAttr('disabled');

                jq("#EventLogSection_eResourceeventStatus_add_control").attr('disabled', 'true');
                jq("#EventLogSection_eResourceeventResolvedDate_add_control").attr('disabled', 'true');
                jq("#EventLogSection_eResourceeventResolvedDate_add_control").datepicker("disable");
                jq("#EventLogSection_eResourceeventResolution_add_control").attr('disabled', 'true');
            }
            if(jq(this).attr("value")==2){
                jq("#EventLogSection_eResourceproblemType_add_control").removeAttr('disabled');
                jq("#EventLogSection_eResourceeventType_add_control").attr('disabled', 'true');
                jq("#EventLogSection_eResourceeventStatus_add_control").removeAttr('disabled');
                jq("#EventLogSection_eResourceeventResolvedDate_add_control").removeAttr('disabled');
                jq("#EventLogSection_eResourceeventResolvedDate_add_control").datepicker("enable");
                jq("#EventLogSection_eResourceeventResolution_add_control").removeAttr('disabled');
                enableOrDisableDateResolved();
            }
        }
    })
}





function selectFilterEventOrProblem() {
    jq("input:radio").live("change",function(){
        if(jq(this).attr("name")=="newCollectionLines['document.filterEventLogs'].logTypeId"){
            if(jq(this).attr("value")==1){
                jq("#FilterEventLogSection_eResourceproblemType_add_control").attr('disabled', 'true');
                jq("#FilterEventLogSection_eResourceeventType_add_control").removeAttr('disabled');
                jq("#FilterEventLogSection_eResourceeventStatus_add_control").attr('disabled', 'true');
                jq("#FilterEventLogSection_eResourceeventResolvedDate_add_control").attr('disabled', 'true');
                jq("#FilterEventLogSection_eResourceeventResolvedDate_add_control").datepicker("disable");
                jq("#FilterEventLogSection_eResourceeventResolution_add_control").attr('disabled', 'true');
            }
            if(jq(this).attr("value")==2){
                jq("#FilterEventLogSection_eResourceproblemType_add_control").removeAttr('disabled');
                jq("#FilterEventLogSection_eResourceeventType_add_control").attr('disabled', 'true');
                jq("#FilterEventLogSection_eResourceeventStatus_add_control").removeAttr('disabled');
                jq("#FilterEventLogSection_eResourceeventResolvedDate_add_control").removeAttr('disabled');
                jq("#FilterEventLogSection_eResourceeventResolvedDate_add_control").datepicker("enable");
                jq("#FilterEventLogSection_eResourceeventResolution_add_control").removeAttr('disabled');
                enableOrDisableFilteredDateResolved();
            }
        }
    })
}

function selectEvent(){
    jq("#EventLogSection_eResourcelogType_add_control_0").attr("checked", "true");
    jq("#EventLogSection_eResourceproblemType_add_control").hide()
    jq("#EventLogSection_eResourceproblemType_add_label").hide()
    jq("#EventLogSection_eResourceeventStatus_add_control").attr('disabled', 'true');
    jq("#EventLogSection_eResourceeventResolvedDate_add_control").attr('disabled', 'true');
    jq("#EventLogSection_eResourceeventResolvedDate_add_control").datepicker("enable");
    jq("#EventLogSection_eResourceeventResolution_add_control").attr('disabled', 'true');
}

function manageGokbPopup() {
    jq('body').scrollTop(0);
    jq(window.parent).scrollTop(0);
    displayDialogWindow("div#EResourceInstance_ManageGoKbPopup");
    return false;
}

function setSpaceField() {
    var navigationBarHeight = jq('#Uif-Navigation').height();
    navigationBarHeight = navigationBarHeight/2;
    jq('#eResRelatedEresSpaceField').height(navigationBarHeight);
    jq('#eResPOSpaceField').height(navigationBarHeight);
    jq('#eResInvoiceSpaceField').height(navigationBarHeight);
    jq('#eResLicenseSpaceField').height(navigationBarHeight);
    jq('#eResContactsSpaceField').height(navigationBarHeight);
    jq('#eResReviewDashSpaceField').height(navigationBarHeight);
    jq('#eResAdminDataSpaceField').height(navigationBarHeight);
    jq('#eResAccessSpaceField').height(navigationBarHeight);
}

function enableOrDisableDateResolved(){
    if(jq('#EventLogSection_eResourceeventStatus_add_control').val() === 'Resolved'){
        jq("#EventLogSection_eResourceeventResolvedDate_add_control").removeAttr('disabled');
        jq("#EventLogSection_eResourceeventResolvedDate_add_control").datepicker("enable");
    } else {
        jq("#EventLogSection_eResourceeventResolvedDate_add_control").val('');
        jq("#EventLogSection_eResourceeventResolvedDate_add_control").attr('disabled','true');
        jq("#EventLogSection_eResourceeventResolvedDate_add_control").datepicker("disable");
    }
}




function enableOrDisableFilteredDateResolved(){
    if(jq('#FilterEventLogSection_eResourceeventStatus_add_control').val() === 'Resolved'){
        jq("#FilterEventLogSection_eResourceeventResolvedDate_add_control").removeAttr('disabled');
        jq("#FilterEventLogSection_eResourceeventResolvedDate_add_control").datepicker("enable");
    } else {
        jq("#FilterEventLogSection_eResourceeventResolvedDate_add_control").val('');
        jq("#FilterEventLogSection_eResourceeventResolvedDate_add_control").attr('disabled','true');
        jq("#FilterEventLogSection_eResourceeventResolvedDate_add_control").datepicker("disable");
    }
}

function enableOrDisableDateResolvedEntry(index){
    if(jq("#EventLogSection_eResourceeventStatus_entry_line" + index + "_control").val() === 'Resolved'){
        jq("input[name='document.oleERSEventLogs[" + index + "].eventResolvedDate']").removeAttr('disabled');
        jq("input[name='document.oleERSEventLogs[" + index + "].eventResolvedDate']").datepicker("enable");
    } else {
        jq("input[name='document.oleERSEventLogs[" + index + "].eventResolvedDate']").val('');
        jq("input[name='document.oleERSEventLogs[" + index + "].eventResolvedDate']").attr('disabled','true');
        jq("input[name='document.oleERSEventLogs[" + index + "].eventResolvedDate']").datepicker("disable");
    }
}
function enableOrDisableFilteredDateResolvedEntry(index){
    if(jq("#FilterEventLogSection_eResourceeventStatus_entry_line" + index + "_control").val() === 'Resolved'){
        jq("input[name='document.filterEventLogs[" + index + "].eventResolvedDate']").removeAttr('disabled');
        jq("input[name='document.filterEventLogs[" + index + "].eventResolvedDate']").datepicker("enable");
    } else {
        jq("input[name='document.filterEventLogs[" + index + "].eventResolvedDate']").val('');
        jq("input[name='document.filterEventLogs[" + index + "].eventResolvedDate']").attr('disabled','true');
        jq("input[name='document.filterEventLogs[" + index + "].eventResolvedDate']").datepicker("disable");
    }
}

function removeLine(lineNumber){
    submitForm('removeInstance',{lineNumber:lineNumber}, null, true, function(){
        submitForm('loadEHoldings',null,null,null,null);
    });
}

function oleHoldingsPager(linkElement, collectionId) {
    var link = jQuery(linkElement);
    if (link.parent().is(kradVariables.ACTIVE_CLASS)) return;
    retrieveComponent(collectionId, "newPage", null, {
        "pageNumber": link.data(kradVariables.PAGE_NUMBER_DATA)
    }, true);
}

function setRecipientSelectorValue() {
    if(jq("#renewal_recipientSelector_control").val() == 'Role') {
        jq("#renewal_recipientRoleName").show();
        jq("#renewal_recipientGroupName_control").val("");
        jq("#renewal_recipientGroupId_control").val("");
        jq("#renewal_recipientGroupName").hide();
        jq("#renewal_recipientUserName_control").val("");
        jq("#renewal_recipientUserId_control").val("");
        jq("#renewal_recipientUserName").hide();
    } else if(jq("#renewal_recipientSelector_control").val() == 'Group') {
        jq("#renewal_recipientGroupName").show();
        jq("#renewal_recipientRoleName_control").val("");
        jq("#renewal_recipientRoleId_control").val("");
        jq("#renewal_recipientRoleName").hide();
        jq("#renewal_recipientUserName_control").val("");
        jq("#renewal_recipientUserId_control").val("");
        jq("#renewal_recipientUserName").hide();
    } else if(jq("#renewal_recipientSelector_control").val() == 'Person') {
        jq("#renewal_recipientUserName").show();
        jq("#renewal_recipientRoleName_control").val("");
        jq("#renewal_recipientRoleId_control").val("");
        jq("#renewal_recipientRoleName").hide();
        jq("#renewal_recipientGroupName_control").val("");
        jq("#renewal_recipientGroupId_control").val("");
        jq("#renewal_recipientGroupName").hide();
    }
}


function populateEmailText(){
   var emailText =  jq("#mailText_control").val();
    jq("#OLEEResourceRecordView-PriceIncreaseAnalysis-EmailText_control").val(emailText);
}


function clearPriceIncreaseAnalysisTab(){
    jq("#OLEEResourceRecordView-PriceIncreaseAnalysis-FiscalYearCost_control").val("");
    jq("#OLEEResourceRecordView-PriceIncreaseAnalysis-YearPriceQuote_control").val("");
}
function clearPriceIncreaseAnalysisInfoTab(){
jq("#OLEEResourceRecordView-PriceIncreaseAnalysis-CostIncrease_control").val("");
jq("#OLEEResourceRecordView-PriceIncreaseAnalysis-percentageIncrease_control").val("");
jq("#OLEEResourceRecordView-PriceIncreaseAnalysis-EmailText_control").val("");
}

function closePriceIncreaseWidget(){
    jq('.fancybox-close').click();
}

function openInstancePOSectionSection() {
    var index = jq('#hiddenIndex_control').val();
    index = parseInt(index)+1;
    var detailLinkId = '#InstancePOSection_detLink_line'+index;
    jq(detailLinkId).focus().click();
}

function openEresourcePODetailSection() {
    var index = jq('#hiddenIndex_control').val();
    index = parseInt(index)+1;
    var detailLinkId = '#EResourcePOSection_detLink_line'+index;
    jq(detailLinkId).focus().click();
}

function hideExportSection(){
    jq("#OLEEResourceRecordView-InstanceDetails-Export").hide();
}

/*jq(document).ajaxComplete(function(event) {
    console.log(event);
    var pageId = event.target.URL.toString();
    var id = pageId.split("pageId=");
    console.log(id[1]);
    if(id[1] == "OLEEResourceRecordView-E-ResourceInstanceTab" && event.target.readyState == 'complete'){
        showExport();
    }
});*/

function exportData(name){
    jq("#OLEEResourceRecordView-InstanceDetails-Export").show();
    var tableId = jq("#OLEEResourceRecordView-InstanceDetailSection-Export_disclosureContent table").attr("id");
    //alert(tableId);
    var id = 0;
    if(name == 'xls'){
        id = 2;
    }else if(name == 'xml'){
        id = 1;
    }
    jq('#ToolTables_'+ tableId + '_'+id).click();
    jq("#OLEEResourceRecordView-InstanceDetails-Export").hide();
}