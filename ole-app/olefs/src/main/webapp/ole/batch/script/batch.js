/**
 * Created with IntelliJ IDEA.
 * User: adityas
 * Date: 7/27/13
 * Time: 8:39 PM
 * To change this template use File | Settings | File Templates.
 */

jq(window).load(function () {

    var profileType = sessionStorage.getItem("profile");
    var profileName = sessionStorage.getItem("profileName")

   if(profileType != null){
       jq("#BatchProcessDefinition-batchProcessProfileName_processName_control").val(profileName);
       jq("#BatchProcessDefinition-batchProcessProfileName_processType_control").val(profileType);
       jq('#storeProfileId').focus().click();

   }

});

function sessionStorageData() {
    jq("#BatchProcessDefinition-batchProcessType_control").live("change", function () {
        /*alert("value changed");*/
        jq("#BatchProcessDefinition-batchProcessProfileName_orderImport_control").val("");
        jq("#BatchProcessDefinition-batchProcessProfileName_invoiceImport_control").val("");
        jq("#BatchProcessDefinition-batchProcessProfileName_batchDelete_control").val("");
        jq("#BatchProcessDefinition-batchProcessProfileName_batchExport_control").val("");
        jq("#BatchProcessDefinition-batchProcessProfileName_patronImport_control").val("");
        jq("#BatchProcessDefinition-batchProcessProfileName_bibImport_control").val("");
        jq("#BatchProcessDefinition-batchProcessProfileName_locationImport_control").val("");
        jq("#BatchProcessDefinition-batchProcessProfileName_claimReport_control").val("");
        jq("#BatchProcessDefinition-batchProcessProfileName_serialRecordImport_control").val("");
        jq("#BatchProcessDefinition-batchProcessProfileName_fundRecordImport_control").val("");
        jq('#hiddenButtonRefresh').focus().click();

    })
    jq("#BatchProcessDefinition-batchProcessProfileName_orderImport_control").live("blur", function () {
        jq('#hiddenProfileId').focus().click();
        sessionStorage.setItem("profile", "Order Record Import")
        sessionStorage.setItem("profileName", jq("#BatchProcessDefinition-batchProcessProfileName_orderImport_control").val());
    });


    jq("#BatchProcessDefinition-batchProcessProfileName_invoiceImport_control").live("blur", function () {
        sessionStorage.setItem("profile", "Invoice Import");
        sessionStorage.setItem("profileName", jq("#BatchProcessDefinition-batchProcessProfileName_invoiceImport_control").val());
    });


    jq("#BatchProcessDefinition-batchProcessProfileName_batchDelete_control").live("blur", function () {
        sessionStorage.setItem("profile", "Batch Delete");
        sessionStorage.setItem("profileName", jq("#BatchProcessDefinition-batchProcessProfileName_batchDelete_control").val());

    });


    jq("#BatchProcessDefinition-batchProcessProfileName_batchExport_control").live("blur", function () {
        jq('#hiddenProfileId').focus().click();
        sessionStorage.setItem("profile", "Batch Export");
        sessionStorage.setItem("profileName", jq("#BatchProcessDefinition-batchProcessProfileName_batchExport_control").val());

    });


    jq("#BatchProcessDefinition-batchProcessProfileName_patronImport_control").live("blur", function () {
        sessionStorage.setItem("profile", "Patron Import");
        sessionStorage.setItem("profileName", jq("#BatchProcessDefinition-batchProcessProfileName_patronImport_control").val());

    });


    jq("#BatchProcessDefinition-batchProcessProfileName_bibImport_control").live("blur", function () {
        sessionStorage.setItem("profile", "Bib Import")
        sessionStorage.setItem("profileName", jq("#BatchProcessDefinition-batchProcessProfileName_bibImport_control").val());

    });


    jq("#BatchProcessDefinition-batchProcessProfileName_locationImport_control").live("blur", function () {
        sessionStorage.setItem("profile", "Location Import");
        sessionStorage.setItem("profileName", jq("#BatchProcessDefinition-batchProcessProfileName_locationImport_control").val());

    });

    jq("#BatchProcessDefinition-batchProcessProfileName_claimReport_control").live("blur", function () {
        sessionStorage.setItem("profile", "Claim Report");
        sessionStorage.setItem("profileName", jq("#BatchProcessDefinition-batchProcessProfileName_claimReport_control").val());

    });

    jq("#BatchProcessDefinition-batchProcessProfileName_serialRecordImport_control").live("blur", function () {
        sessionStorage.setItem("profile", "Serial Record Import");
        sessionStorage.setItem("profileName", jq("#BatchProcessDefinition-batchProcessProfileName_serialRecordImport_control").val());

    });

    jq("#BatchProcessDefinition-batchProcessProfileName_fundRecordImport_control").live("blur", function () {
        sessionStorage.setItem("profile", "Fund Record Import");
        sessionStorage.setItem("profileName", jq("#BatchProcessDefinition-batchProcessProfileName_fundRecordImport_control").val());

    });
}
jq(document).ready(function(){

    if(jq("#mainSection-MaintenanceView-exportScope_control").val() != "filter") {
        jq("#OLEBatchProcessProfileBo-MaintenanceView-filterCriteriaSection").hide();
    }

    refreshPageUser();
    refreshBeanId();
    sessionStorageData();
    jq("#filterCriteria_filterFieldName_select_add_control").live("change",function() {
        //jq('#invoiceVendorBtn').click();
        if(jq("#filterCriteria_filterFieldName_select_add_control").val()!=''){
            jq("#filterCriteria_filterFieldName_text_add_control").attr("value","");
            jq("#filterCriteria_filterFieldName_text_add_control").attr('readonly','readonly');
        }
        else if(jq("#filterCriteria_filterFieldName_select_add_control").val()==''){
            jq("#filterCriteria_filterFieldName_text_add_control").removeAttr('readonly');
        }
    });

    jq("#profileConstants_attributeName_add_control").live("change",function() {
        //jq('#invoiceVendorBtn').click();
        if(jq("#profileConstants_attributeName_add_control").val()!=''){
            jq("#profileConstants_attributeName1_add_control").attr("value","");
            jq("#profileConstants_attributeName1_add_control").attr('readonly','readonly');
        }
        else if(jq("#profileConstants_attributeName_add_control").val()==''){
            jq("#profileConstants_attributeName1_add_control").removeAttr('readonly');
        }
    });

    jq("#constant-attributeNames-StatisticalCode_add_control").live("change",function() {
        //jq('#invoiceVendorBtn').click();
        if(jq("#constant-attributeNames-StatisticalCode_add_control").val()!=''){
            jq("#profileConstants_attributeValue_add_control").attr("value","");
            jq("#profileConstants_attributeValue_add_control").attr('readonly','readonly');
        }
        else if(jq("#constant-attributeNames-StatisticalCode_add_control").val()==''){
            jq("#profileConstants_attributeValue_add_control").removeAttr('readonly');
        }
    });
    jq("#constant-attributeNames-callNumberType_add_control").live("change",function() {
        //jq('#invoiceVendorBtn').click();
        if(jq("#constant-attributeNames-callNumberType_add_control").val()!=''){
            jq("#profileConstants_attributeValue_add_control").attr("value","");
            jq("#profileConstants_attributeValue_add_control").attr('readonly','readonly');
        }
        else if(jq("#constant-attributeNames-callNumberType_add_control").val()==''){
            jq("#profileConstants_attributeValue_add_control").removeAttr('readonly');
        }
    });
    jq("#constant-attributeNames-ItemType_add_control").live("change",function() {
        //jq('#invoiceVendorBtn').click();
        if(jq("#constant-attributeNames-ItemType_add_control").val()!=''){
            jq("#profileConstants_attributeValue_add_control").attr("value","");
            jq("#profileConstants_attributeValue_add_control").attr('readonly','readonly');
        }
        else if(jq("#constant-attributeNames-ItemType_add_control").val()==''){
            jq("#profileConstants_attributeValue_add_control").removeAttr('readonly');
        }
    });
    jq("#constant-attributeNames-ItemStatus_add_control").live("change",function() {
        //jq('#invoiceVendorBtn').click();
        if(jq("#constant-attributeNames-ItemStatus_add_control").val()!=''){
            jq("#profileConstants_attributeValue_add_control").attr("value","");
            jq("#profileConstants_attributeValue_add_control").attr('readonly','readonly');
        }
        else if(jq("#constant-attributeNames-ItemType_add_control").val()==''){
            jq("#profileConstants_attributeValue_add_control").removeAttr('readonly');
        }
    });
    jq("#constant-attributeNames-AccessStatus_add_control").live("change",function() {
        //jq('#invoiceVendorBtn').click();
        if(jq("#constant-attributeNames-AccessStatus_add_control").val()!=''){
            jq("#profileConstants_attributeValue_add_control").attr("value","");
            jq("#profileConstants_attributeValue_add_control").attr('readonly','readonly');
        }
        else if(jq("#constant-attributeNames-AccessStatus_add_control").val()==''){
            jq("#profileConstants_attributeValue_add_control").removeAttr('readonly');
        }
    });
    jq("#constant-attributeNames-LocationLevel1_add_control").live("change",function() {
        //jq('#invoiceVendorBtn').click();
        if(jq("#constant-attributeNames-LocationLevel1_add_control").val()!=''){
            jq("#profileConstants_attributeValue_add_control").attr("value","");
            jq("#profileConstants_attributeValue_add_control").attr('readonly','readonly');
        }
        else if(jq("#constant-attributeNames-LocationLevel1_add_control").val()==''){
            jq("#profileConstants_attributeValue_add_control").removeAttr('readonly');
        }
    });
    jq("#constant-attributeNames-LocationLevel2_add_control").live("change",function() {
        //jq('#invoiceVendorBtn').click();
        if(jq("#constant-attributeNames-LocationLevel2_add_control").val()!=''){
            jq("#profileConstants_attributeValue_add_control").attr("value","");
            jq("#profileConstants_attributeValue_add_control").attr('readonly','readonly');
        }
        else if(jq("#constant-attributeNames-LocationLevel2_add_control").val()==''){
            jq("#profileConstants_attributeValue_add_control").removeAttr('readonly');
        }
    });
    jq("#constant-attributeNames-LocationLevel3_add_control").live("change",function() {
        //jq('#invoiceVendorBtn').click();
        if(jq("#constant-attributeNames-LocationLevel3_add_control").val()!=''){
            jq("#profileConstants_attributeValue_add_control").attr("value","");
            jq("#profileConstants_attributeValue_add_control").attr('readonly','readonly');
        }
        else if(jq("#constant-attributeNames-LocationLevel3_add_control").val()==''){
            jq("#profileConstants_attributeValue_add_control").removeAttr('readonly');
        }
    });
    jq("#constant-attributeNames-LocationLevel4_add_control").live("change",function() {
        //jq('#invoiceVendorBtn').click();
        if(jq("#constant-attributeNames-LocationLevel4_add_control").val()!=''){
            jq("#profileConstants_attributeValue_add_control").attr("value","");
            jq("#profileConstants_attributeValue_add_control").attr('readonly','readonly');
        }
        else if(jq("#constant-attributeNames-LocationLevel4_add_control").val()==''){
            jq("#profileConstants_attributeValue_add_control").removeAttr('readonly');
        }
    });
    jq("#constant-attributeNames-LocationLevel5_add_control").live("change",function() {
        //jq('#invoiceVendorBtn').click();
        if(jq("#constant-attributeNames-LocationLevel5_add_control").val()!=''){
            jq("#profileConstants_attributeValue_add_control").attr("value","");
            jq("#profileConstants_attributeValue_add_control").attr('readonly','readonly');
        }
        else if(jq("#constant-attributeNames-LocationLevel5_add_control").val()==''){
            jq("#profileConstants_attributeValue_add_control").removeAttr('readonly');
        }
    });
    jq("#constant-attributeNames-Donor_add_control").live("change",function() {
        if(jq("#constant-attributeNames-Donor_add_control").val()!=''){
            jq("#profileConstants_attributeValue_add_control").attr("value","");
            jq("#profileConstants_attributeValue_add_control").attr('readonly','readonly');
        }
        else if(jq("#constant-attributeNames-Donor_add_control").val()==''){
            jq("#profileConstants_attributeValue_add_control").removeAttr('readonly');
        }
    });

    jq("#constant-attributeNames-remaining_add_control").live("change",function() {
        //jq('#invoiceVendorBtn').click();
        if(jq("#constant-attributeNames-remaining_add_control").val()!=''){
            jq("#profileConstants_attributeValue_add_control").attr("value","");
            jq("#profileConstants_attributeValue_add_control").attr('readonly','readonly');
        }
        else if(jq("#constant-attributeNames-remaining_add_control").val()==''){
            jq("#profileConstants_attributeValue_add_control").removeAttr('readonly');
        }
    });

    jq("#constant-attributeNames-orderImport_add_control").live("change",function() {
        //jq('#invoiceVendorBtn').click();
        if(jq("#constant-attributeNames-orderImport_add_control").val()!=''){
            jq("#profileConstants_attributeValue_add_control").attr("value","");
            jq("#profileConstants_attributeValue_add_control").attr('readonly','readonly');
        }
        else if(jq("#constant-attributeNames-orderImport_add_control").val()==''){
            jq("#profileConstants_attributeValue_add_control").removeAttr('readonly');
        }
    });

    /* For Source Field*/
    jq("#holding-sourcefield_line0_add_control").live("change",function() {
        //jq('#invoiceVendorBtn').click();
        if(jq("#holding-sourcefield_line0_add_control").val()!=''){
            jq("#dataMappingOptions_sourceField1_line0_add_control").attr("value","");
            jq("#dataMappingOptions_sourceField1_line0_add_control").attr('readonly','readonly');
        }
        else if(jq("#holding-sourcefield_line0_add_control").val()==''){
            jq("#dataMappingOptions_sourceField1_line0_add_control").removeAttr('readonly');
        }
    });

    jq("#item-sourcefield_line0_add_control").live("change",function() {
        //jq('#invoiceVendorBtn').click();
        if(jq("#item-sourcefield_line0_add_control").val()!=''){
            jq("#dataMappingOptions_sourceField1_line0_add_control").attr("value","");
            jq("#dataMappingOptions_sourceField1_line0_add_control").attr('readonly','readonly');
        }
        else if(jq("#item-sourcefield_line0_add_control").val()==''){
            jq("#dataMappingOptions_sourceField1_line0_add_control").removeAttr('readonly');
        }

    });

    jq("#einstance-sourcefield_line0_add_control").live("change",function() {
        //jq('#invoiceVendorBtn').click();
        if(jq("#einstance-sourcefield_line0_add_control").val()!=''){
            jq("#dataMappingOptions_sourceField1_line0_add_control").attr("value","");
            jq("#dataMappingOptions_sourceField1_line0_add_control").attr('readonly','readonly');
        }
        else if(jq("#einstance-sourcefield_line0_add_control").val()==''){
            jq("#dataMappingOptions_sourceField1_line0_add_control").removeAttr('readonly');
        }
    });
    /*Source field End*/

    /* For Destination Field*/
    jq("#bibImportHolding_destinationField_line0_add_control").live("change",function() {
        //jq('#invoiceVendorBtn').click();
        if(jq("#bibImportHolding_destinationField_line0_add_control").val()!=''){
            jq("#dataMappingOptions_destinationField1_line0_add_control").attr("value","");
            jq("#dataMappingOptions_destinationField1_line0_add_control").attr('readonly','readonly');
        }
        else if(jq("#bibImportHolding_destinationField_line0_add_control").val()==''){
            jq("#dataMappingOptions_destinationField1_line0_add_control").removeAttr('readonly');
        }
    });

    jq("#batchExportHolding_destinationField_line0_add_control").live("change",function() {
        //jq('#invoiceVendorBtn').click();
        if(jq("#batchExportHolding_destinationField_line0_add_control").val()!=''){
            jq("#dataMappingOptions_destinationField1_line0_add_control").attr("value","");
            jq("#dataMappingOptions_destinationField1_line0_add_control").attr('readonly','readonly');
        }
        else if(jq("#batchExportHolding_destinationField_line0_add_control").val()==''){
            jq("#dataMappingOptions_destinationField1_line0_add_control").removeAttr('readonly');
        }
    });

    jq("#item_destinationField_line0_add_control").live("change",function() {
        //jq('#invoiceVendorBtn').click();
        if(jq("#item_destinationField_line0_add_control").val()!=''){
            jq("#dataMappingOptions_destinationField1_line0_add_control").attr("value","");
            jq("#dataMappingOptions_destinationField1_line0_add_control").attr('readonly','readonly');
        }
        else if(jq("#item_destinationField_line0_add_control").val()==''){
            jq("#dataMappingOptions_destinationField1_line0_add_control").removeAttr('readonly');
        }
    });

    jq("#einstance_destinationField_line0_add_control").live("change",function() {
        //jq('#invoiceVendorBtn').click();
        if(jq("#einstance_destinationField_line0_add_control").val()!=''){
            jq("#dataMappingOptions_destinationField1_line0_add_control").attr("value","");
            jq("#dataMappingOptions_destinationField1_line0_add_control").attr('readonly','readonly');
        }
        else if(jq("#einstance_destinationField_line0_add_control").val()==''){
            jq("#dataMappingOptions_destinationField1_line0_add_control").removeAttr('readonly');
        }
    });
    /*Destination field End*/
    if (jq("#hdnScheduleFlag_control").val() != "true") {
        jq('#RunNow_Schedule span >  #RunNow_Schedule_control_0').attr("checked", "true");
    }
    selectRunNowOrSchedule();
});



function exportFilter(){
    if(jq("#mainSection-MaintenanceView-exportScope_control").val() == "filter") {
        jq("#OLEBatchProcessProfileBo-MaintenanceView-filterCriteriaSection").show();
    } else {
        jq("#OLEBatchProcessProfileBo-MaintenanceView-filterCriteriaSection").hide();
    }
}
function openJobDetails() {
    var processId = jq("#processId_hidden_control").val();
    if( jq("#hdn_control").val()=="true"){
        jq("#hdn_control").val(false);
        displayDialogWindow("div#MessagePopupSectionForInvoiceImport-HorizontalBoxSection");
        return false;
    }
    if ((jq("#hdnJobDetails_control").val()=="true") && processId !== null && processId !== undefined && processId !== ''){
        window.open("oleBatchProcessJobController?viewId=OLEBatchProcessJobDetailsView&methodToCall=singleJobDetailView&command=initiate&documentClass=org.kuali.ole.batch.bo.OLEBatchProcessJobDetailsBo&batchProcessId=" + processId);
    }else if(jq("#hdnJobDetails_control").val()=="true"){
        window.open("oleBatchProcessJobController?viewId=OLEBatchProcessJobDetailsView&methodToCall=jobDocHandler&command=initiate&documentClass=org.kuali.ole.batch.bo.OLEBatchProcessJobDetailsBo");
    }
}

function continueImport(){
    jq("#hdnfla_control").val(true);
    jq('#continue_invoice_import').focus().click();
}

function refreshPageUser(){
    jq("#batchExportDataTypeField_line0_add_control").live("change",function() {
        jq('#hiddenButtonRefresh').focus().click();
    });
    jq("#bibImportDataType-destinationfield_line0_add_control").live("change",function() {
        jq('#hiddenButtonRefresh').focus().click();
    });
    jq("#batchOrderImportDataType-destinationfield_line0_add_control").live("change",function() {
        jq('#hiddenButtonRefresh').focus().click();
    });
    jq("#batchInvoiceImportDataType-destinationfield_line0_add_control").live("change",function() {
        jq('#hiddenButtonRefresh').focus().click();
    });
    /*jq("#batchExportDataType-destinationfield_line0_add_control").live("change",function() {
        jq('#hiddenButtonRefresh').focus().click();
    });*/
    jq("#profileConstants_dataType_add_control").live("change",function() {
        jq('#profileConstants_attributeValue_add_control').val('');
        jq('#constant-datatype-bibmarc_add_control').val('');
        jq('#constant-datatype-holdings_add_control').val('');
        jq('#constant-datatype-item_add_control').val('');
        jq('#constant-datatype-eHoldings_add_control').val('');
        jq('#hiddenButtonRefresh').focus().click();
    });
    jq("#constant-datatype-orderImport_add_control").live("change",function() {
        jq('#orderImport_oldAttributeName_add_control').val(null);
        jq('#profileConstants_attributeValue_add_control').val('');
        jq('#hiddenButtonRefresh').focus().click();
    });
    jq("#constant-datatype-bibmarc_add_control").live("change",function() {
        jq('#profileConstants_attributeValue_add_control').val('');
        jq('#hiddenButtonRefresh').focus().click();
    });
    jq("#constant-datatype-holdings_add_control").live("change",function() {
        jq('#profileConstants_attributeValue_add_control').val('');
        jq('#hiddenButtonRefresh').focus().click();
    });
    jq("#constant-datatype-item_add_control").live("change",function() {
        jq('#profileConstants_attributeValue_add_control').val('');
        jq('#hiddenButtonRefresh').focus().click();
    });
    jq("#constant-datatype-eHoldings_add_control").live("change",function() {
        jq('#profileConstants_attributeValue_add_control').val('');
        jq('#hiddenButtonRefresh').focus().click();
    });
    jq("#constant-datatype-orderImport_add_control").live("change",function() {
        jq('#hiddenButtonRefresh').focus().click();
    });
    jq("#constant-datatype-invoiceImport_add_control").live("change",function() {
        jq('#orderImport_oldAttributeName_add_control').val(null);
        jq('#profileConstants_attributeValue_add_control').val('');
        if(jq('#constant-datatype-invoiceImport_add_control').val() == 'exchangeRate'){
            jq('#hiddenButtonExchangeRate').focus().click();
        }
        jq('#hiddenButtonRefresh').focus().click();
    });
    jq("#constant-datatype-remaining_add_control").live("change",function() {
        jq('#profileConstants_attributeValue_add_control').val('');
        jq('#hiddenButtonRefresh').focus().click();
    });
    jq("#constant-datatype-ediExport_add_control").live("change",function() {
        jq('#hiddenButtonRefresh').focus().click();
    });
    jq("#orderImport_dataType_add_control").live("change",function() {
        jq('#hiddenButtonRefresh').focus().click();
    });
    jq("#invoiceImport_dataType_add_control").live("change",function() {
        jq('#hiddenButtonRefresh').focus().click();
    });
    jq("#ediExport_dataType_add_control").live("change",function() {
        jq('#hiddenButtonRefresh').focus().click();
    });
}

function refreshBeanId(){
    jq("#bibAddOverlaySectionMatch-bibOverlayOrAddOrNone_control_1").live("change",function() {
        jq('#hiddenButtonForBibOverlay').focus().click();
    });
    jq("#bibAddOverlaySectionMatch-bibOverlayOrAddOrNone_control_2").live("change",function() {
        jq('#hiddenButtonForBibOverlay').focus().click();
    });
    jq("#bibAddOverlaySectionMatch-bibOverlayOrAddOrNone_control_0").live("change",function() {
        jq('#hiddenButtonForBibOverlay').focus().click();
    });
    jq("#bibStatus-noChangeOrSet_control_1").live("change",function() {
        jq('#hiddenButtonForSetStatus').focus().click();
    });
    jq("#bibStatus-noChangeOrSet_control_0").live("change",function() {
        jq('#hiddenButtonForSetStatus').focus().click();
    });
    jq("#bibStaffonly-noChangeOrSet_control_1").live("change",function() {
        jq('#hiddenButtonForSetStaffOnly').focus().click();
    });
    jq("#bibStaffonly-noChangeOrSet_control_0").live("change",function() {
        jq('#hiddenButtonForSetStaffOnly').focus().click();
    });
    jq("#changesToImport-dontChange001_control_1").live("change",function() {
        jq('#hiddenButtonForChangeToImport').focus().click();
    });
    jq("#changesToImport-dontChange001_control_0").live("change",function() {
        jq('#hiddenButtonForChangeToImport').focus().click();
    });
    jq("#changesToImport-ChangeTo035_control_1").live("change",function() {
        jq("#changesToImport-valueToPrepend_control").removeAttr('readonly');
        jq('#hiddenButtonForChangeToImport').focus().click();
    });
    jq("#changesToImport-ChangeTo035_control_0").live("change",function() {
        jq("#changesToImport-valueToPrepend_control").attr("value","");
        jq("#changesToImport-valueToPrepend_control").attr('readonly','readonly');
        jq('#hiddenButtonForChangeToImport').focus().click();
    });
}

function rescheduleJob(batchProcessId){
    window.open("oleBatchProcessDefinitionController?viewId=OLEBatchProcessDefinitionView&methodToCall=reschedule&batchProcessId="+batchProcessId+"&command=displayDocSearchView");
}

function removeScheduledJob(line){
    jq("#scheduleId_control").val(line);
    jq("#hdnRemove_job_button").focus().click();

}
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

function validateUploadFileSize(batchProcessType) {
    if (batchProcessType == "Bib Import") {
        if (jq("#ingestInputFile_control")[0].files[0] != undefined) {
            var uploadFileSize = jq("#ingestInputFile_control")[0].files[0].size;
            if (uploadFileSize > 100000000) {
                displayDialogWindow("div#MaximumPageSizeWindow-HorizontalBoxSection");
            } else {
                if (jq("#hdnRunNowFlag_control").val() == "true") {
                    jq('#hdn_runNow_button').focus().click();
                } else {
                    jq('#Hdn_BP-Uif-SubmitAction').focus().click();
                }
            }
        } else {
            if (jq("#hdnRunNowFlag_control").val() == "true") {
                jq('#hdn_runNow_button').focus().click();
            } else {
                jq('#Hdn_BP-Uif-SubmitAction').focus().click();
            }
        }
    } else {
        if (jq("#hdnRunNowFlag_control").val() == "true") {
            jq('#hdn_runNow_button').focus().click();
        } else {
            jq('#Hdn_BP-Uif-SubmitAction').focus().click();
        }
    }
}

function closeMaximumPageSizeWindow() {
    jq("div#MaximumPageSizeWindow-HorizontalBoxSection").fadeOut(300);
    jq('#mask').fadeOut(300);
}

function selectRunNowOrSchedule() {
    if(jq('#RunNow_Schedule span >  #RunNow_Schedule_control_0').is(':checked')) {
        jq("#hdnRunNowFlag_control").val("true");
        jq("#hdnScheduleFlag_control").val("false");
    } else if(jq('#RunNow_Schedule span >  #RunNow_Schedule_control_1').is(':checked')){
        jq("#hdnScheduleFlag_control").val("true");
        jq("#hdnRescheduleFlag_control").val("false");
        jq("#hdnRunNowFlag_control").val("false");
    }
}

function refreshSchedule() {
    jq('#hdnSchedule').focus().click();
}

jq("#matchingAddOverlaySection-incomingHoldingNotMatched-addHolding").click(function(){
    if(jq("#matchingAddOverlaySection-incomingHoldingNotMatched-addHolding_control_0").val() == "holdingsNotMatched_addHoldings") {
//        jq("#matchingAddOverlaySection-incomingHoldingNotMatched-addItem_control").val(true);
        jq("#matchingAddOverlaySection-incomingHoldingNotMatched-addItem_control").prop('checked', true);
    }


})
jq("#matchingAddOverlaySection-incomingHoldingNotMatched").click(function(){
    if (jq("#matchingAddOverlaySection-incomingHoldingNotMatched_control_0").val() == "holdingsNotMatched_discardHoldings") {
        jq("#matchingAddOverlaySection-incomingHoldingNotMatched-addItem_control").prop('checked', false);
    }


})
jq("#matchingAddOverlaySection-incomingHoldingNotMatched-addItem").click(function(){
    if(jq("#matchingAddOverlaySection-incomingHoldingNotMatched_control_0").prop("checked")) {
        jq("#matchingAddOverlaySection-incomingHoldingNotMatched-addItem_control").prop('checked', false);
    }
});


jq("#matchingAddOverlaySection-incomingHoldingMatched-addHolding").click(function(){
    if(jq("#matchingAddOverlaySection-incomingHoldingMatched-addHolding_control_0").val() == "holdingsMatched_addHoldings") {
        jq("#matchingAddOverlaySection-incomingHoldingNotMatched-addItsItem_control").prop('checked', true);
    }


})

jq("#matchingAddOverlaySection-incomingHoldingMatched").click(function(){
    if (jq("#matchingAddOverlaySection-incomingHoldingMatched_control_0").val() == "processHoldingAndItem") {
        jq("#matchingAddOverlaySection-incomingHoldingNotMatched-addItsItem_control").prop('checked', false);
    }


})

jq("#matchingAddOverlaySection-incomingHoldingNotMatched-addItsItem").click(function(){
    if(jq("#matchingAddOverlaySection-incomingHoldingMatched_control_0").prop("checked")) {
        jq("#matchingAddOverlaySection-incomingHoldingNotMatched-addItsItem_control").prop('checked', false);
    }
});















