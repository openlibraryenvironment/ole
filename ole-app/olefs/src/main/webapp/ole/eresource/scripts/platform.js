var unsaved = false;

jq(document).ready(function () {
    /*var gokbFlag = false;*/
    var bindButton = false;
    unsaved = false;

    /*jq("#gokbIdd_quickfinder_act").live("click", function () {
        gokbFlag = true;
    });*/

    jq("#OLEPlatformRecordView").live("keypress",function(){
        unsaved = true;
    });
    jq("#OLEPlatformRecordView").live("change",function(){
        unsaved = true;
    });
    jq(":input").live("change keypress",function(){
        unsaved = true;
    });
    jq(":button").live("click",function(){
        if (!bindButton) {
            unsaved = true;
        }
    });

    function unloadPage() {
        /*if (gokbFlag) {
            var message = "Linking this Platform record to a GOKb identifier will overwrite the following fields with GOKb-provided metadata: GOKb Identifier, Platform Name, Platform Provider Name, and Software. This data will no longer be editable. Select “OK” to proceed.";
            alert(message);
        }*/
        if(unsaved){
            var message  = "This page is asking you to confirm that you want to leave - data you have entered may not be saved.";
            return message;
        }
    }
    jq('form').bind('submit', function() {
        bindButton = true;
        unsaved = false;
        /*gokbFlag = false*/;
    });
    window.onbeforeunload = unloadPage;

    if(jq("#hdnGokbPlatformFlag_control").val() == 'true') {
        jq("#hdnGokbPlatformFlag_control").val("false");
        displayDialogWindow("div#popupToDisplayGOKBMessage");
    }
    jq("#platformProviderName_control").live("blur", function () {
        if(jq("#platformProviderName_control").val().length>0){
            jq('#hdnPlatformProviderBtn').focus().click();
        }
    });
});

function displayPlatformProviderMessagePopup(){
    if(jq("#hdnPlatformProviderFlag_control").val() == 'true') {
        displayDialogWindow("div#popupToDisplayPlatformProviderMessage");
        jq("#hdnPlatformProviderFlag_control").val("false");
    } else if(jq("#hdnsaveValidationFlag_control").val() == 'true') {
        displayDialogWindow("div#popupToDisplayLinkedRecords");
        jq("#hdnsaveValidationFlag_control").val("false");
    }
}

function closeDisplayLinkedRecordsPopup(){
    jq("div#popupToDisplayLinkedRecords").fadeOut(300);
    jq('#mask').fadeOut(300);
}

function closeGOKBMessagePopup(){
    jq("#gokbPlatformId_control").val(0);
    jq("#gokbPlatformId").val(0);
    if(jq("#hdnPlatformProviderFlag_control").val() == 'true') {
        jq('#hiddenButtonForUpdateVendorDetails').focus().click();
    }
    jq("div#popupToDisplayGOKBMessage").fadeOut(300);
    jq('#mask').fadeOut(300);
}

function closePlatformProviderMessagePopup(){
    jq("div#popupToDisplayPlatformProviderMessage").fadeOut(300);
    jq('#mask').fadeOut(300);
}

function save(){
    unsaved = false;
    if(jq("#hdnsaveValidationFlag_control").val() == 'true') {
        displayDialogWindow("div#popupToDisplayLinkedRecords");
    }
}

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

function closeEventPopup(){
    jq("div#OLEPlatformEventLogTab-AddEvent").fadeOut(300);
    jq('#mask').fadeOut(300);
}

function selectEventOrProblem() {
    jq("input:radio").live("change", function () {
            if (jq(this).attr("name") == "newCollectionLines['document.eventLogs'].logTypeId") {
                if (jq(this).attr("value") == 1) {
                    jq("#problemType_add_control").attr('disabled', 'true');
                    jq("#eventTypeId_add_control").removeAttr('disabled');
                    jq("#eventStatus_add_control").attr('disabled', 'true');
                    jq("#eventResolvedDate_add_control").attr('disabled', 'true');
                    jq("#eventResolvedDate_add_control").datepicker("disable");
                    jq("#input-group").attr('disabled', 'true');
                    jq("#eventResolution_add_control").attr('disabled', 'true');
                }
                if (jq(this).attr("value") == 2) {
                    jq("#problemType_add_control").removeAttr('disabled');
                    jq("#eventTypeId_add_control").attr('disabled', 'true');
                    jq("#eventStatus_add_control").removeAttr('disabled');
                    jq("#eventResolvedDate_add_control").removeAttr('disabled');
                    jq("#eventResolvedDate_add_control").datepicker("enable");
                    jq("#eventResolution_add_control").removeAttr('disabled');
                    enableOrDisableDateResolved();
                }
            }
        }
    )
}

function selectFilterEventOrProblem() {
    jq("input:radio").live("change", function () {
            if (jq(this).attr("name") == "newCollectionLines['document.filterEventLogs'].logTypeId") {
                if (jq(this).attr("value") == 1) {
                    jq("#filterProblemType_add_control").attr('disabled', 'true');
                    jq("#filterEventType_add_control").removeAttr('disabled');
                    jq("#filterEventStatus_add_control").attr('disabled', 'true');
                    jq("#filterEventResolvedDate_add_control").attr('disabled', 'true');
                    jq("#filterEventResolvedDate_add_control").datepicker("disable");
                    jq("#filterEventResolution_add_control").attr('disabled', 'true');
                }
                if (jq(this).attr("value") == 2) {
                    jq("#filterProblemType_add_control").removeAttr('disabled');
                    jq("#filterEventType_add_control").attr('disabled', 'true');
                    jq("#filterEventStatus_add_control").removeAttr('disabled');
                    jq("#filterEventResolvedDate_add_control").removeAttr('disabled');
                    jq("#filterEventResolvedDate_add_control").datepicker("enable");
                    jq("#filterEventResolution_add_control").removeAttr('disabled');
                    enableOrDisableFilteredDateResolved();
                }
            }
        }
    )
}

function enableOrDisableDateResolved() {
    if(jq('#eventStatus_add_control').val() === 'Resolved'){
        jq("#eventResolvedDate_add_control").removeAttr('disabled');
        jq("#eventResolvedDate_add_control").datepicker("enable");
    } else {
        jq("#eventResolvedDate_add_control").val('');
        jq("#eventResolvedDate_add_control").attr('disabled','true');
        jq("#eventResolvedDate_add_control").datepicker("disable");
    }
}
function enableOrDisableFilteredDateResolved() {
    if(jq('#filterEventStatus_add_control').val() === 'Resolved'){
        jq("#filterEventResolvedDate_add_control").removeAttr('disabled');
        jq("#filterEventResolvedDate_add_control").datepicker("enable");
    } else {
        jq("#filterEventResolvedDate_add_control").val('');
        jq("#filterEventResolvedDate_add_control").attr('disabled','true');
        jq("#filterEventResolvedDate_add_control").datepicker("disable");
    }
}