jq(document).ready(function () {
    jq("#EditorExistingRecordTreeNavigation").html(jq("#EditorHdnTreeData_control").val());
    jq('#WorkBibDataFieldSection table tr td').eq(0).css('width', '95%');
    jq('#DublinElementSection table tr td').eq(0).css('width', '80%');
    //jq('#callNumberHoldingsBrowseLinkHidden').hide();

    if (jq('#hiddenBibId_control').val() == "" || jq('#hiddenBibId_control').val() == null) {
        jq("#bibLevelActionSection").hide();
    }

    jq("input#OleMissingItem-missingPiecesCount_control").live('keydown',function(event) {
        if ( event.keyCode == 46 || event.keyCode == 8 || event.keyCode == 9 || event.keyCode == 27 || event.keyCode == 13 ||
            (event.keyCode == 65 && event.ctrlKey === true) ||
            (event.keyCode >= 35 && event.keyCode <= 39)) {
            return;
        }
        else {
            if (event.shiftKey || (event.keyCode < 48 || event.keyCode > 57) && (event.keyCode < 96 || event.keyCode > 105 )) {
                event.preventDefault();
            }
        }
    });

    jq("input#oleItemNumberOfPieces_control").live('keydown',function(event) {
        if ( event.keyCode == 46 || event.keyCode == 8 || event.keyCode == 9 || event.keyCode == 27 || event.keyCode == 13 ||
            (event.keyCode == 65 && event.ctrlKey === true) ||
            (event.keyCode >= 35 && event.keyCode <= 39)) {
            return;
        }
        else {
            if (event.shiftKey || (event.keyCode < 48 || event.keyCode > 57) && (event.keyCode < 96 || event.keyCode > 105 )) {
                event.preventDefault();
            }
        }
    });

    window.focus = function(a){

        try{
            a=document.getElementById(a);
            if(null!=a&&jQuery(a).is(":text,textarea,:password")&&a.value&&0!=a.value.length)if(a.createTextRange)
            {
                var g=a.createTextRange();g.moveStart("character",a.value.length);g.collapse();g.select()
            }else
            {
                if(a.selectionStart||void 0!=a.selectionStart&&"0"==a.selectionStart)
                    g=a.value.length,
                    a.selectionStart=g,
                    a.selectionEnd=g,
                    a.focus()
            }else
                null!=a&&a.focus()
        }
        catch(err) {
           /* alert("error");*/
        }
    }

    function disableEnterKey(evt) {
        var browser = navigator.userAgent;
        if(browser.indexOf("MSIE8.0")!=-1){

            if (evt.keyCode != null) {

                return false;
            }
        } else{
            if (evt.keyCode==13) {

                return false;
            }
        }
    }
    jq("input.uif-textControl").live("keypress", function(e){

        if(e.keyCode==13){
            return false;
        }
    })
    jq("input#OleMissingItem-missingPieceEffectiveDate_control").live('keydown',function(event) {
        if ( event.keyCode == 46 || event.keyCode == 8 || event.keyCode == 9 || event.keyCode == 27 || event.keyCode == 13 ||
            (event.keyCode == 65 && event.ctrlKey === true) ||
            (event.keyCode >= 35 && event.keyCode <= 39)) {
            return;
        }
        else {
            if (event.shiftKey || (event.keyCode < 48 || event.keyCode > 57) && (event.keyCode < 96 || event.keyCode > 105 )) {
                event.preventDefault();
            }
        }
    });
    //document.onkeypress = disableEnterKey;

    jq("#submitEditor_Header").click(function() {
        if ("bibliographic" == jq("#hdnDocType_control").val()) {
            jq("#hiddenBibFlag_control").val(false);
        } else if ("holdings" == jq("#hdnDocType_control").val()) {
            jq("#hiddenHoldingFlag_control").val(false);
        } else if ("item" == jq("#hdnDocType_control").val()) {
            jq("#hiddenItemFlag_control").val(false);
        } else if ("eHoldings" == jq("#hdnDocType_control").val()) {
            jq("#hiddenEHoldingsFlag_control").val(false);
        }
    });

    jq("input.uif-textControl").live("keypress", function (event) {
        if (!ctrlEnter) {
            var keycode = (event.keyCode ? event.keyCode : event.which);
            if (keycode == '13') {
                if ("eHoldings" == jq("#hdnDocType_control").val()) {
                    jq("#EInstanceSave").click();
                } else {
                    jq("#submitEditor").click();
                }
            }
        }
        ctrlEnter = false;
    });

    jq("#OleHoldingLocation_control").focus();
    jq("#OleHoldingLocationLevelName_control").focus();
    jq("#OleEHoldingLocation_control").focus();
    jq(document).live("keypress", function (event) {
        if (!ctrlEnter) {
            var keycode = (event.keyCode ? event.keyCode : event.which);
            if (keycode == '13') {
                if ("eHoldings" == jq("#hdnDocType_control").val()) {
                    jq("#EInstanceSave").click();
                } else {
                    jq("#submitEditor").click();
                }
            }
        }
        ctrlEnter = false;
    });

    jq("#OleHoldingCallNumber_control").live('blur',function() {
        if(jq("#OleHoldingCallNumber_control").val().length < 1){
            jq("#OleHoldingShelvingOrder_control").val("");
            jq("#OleHoldingShelvingScheme_control").val(1);
        }
    });
    jq("#OleItemCallNumber_control").live('blur',function() {
        if(jq("#OleItemCallNumber_control").val().length < 1){
            jq("#OleItemShelvingOrder_control").val("");
            jq("#OleItemShelvingScheme_control").val(1);
        }
    });

    jq(document).live("keyup", function (event) {
        var id = event.currentTarget.activeElement.getAttribute("id");
        if (id != null && id != undefined) {
            if (id.substring(0, 21) == "dataField_tag_id_line") {
                var idRow = id;
                idRow = idRow.replace("dataField_tag_id_line", "");
                idRow = idRow.replace("_control", "").trim();
                var value = jq("#" + id).val();
                if (value.length == 3) {
                    jq("#dataField_ind1_id_line" + idRow + "_control").focus();
                }
            } else if (id.substring(0, 22) == "dataField_ind1_id_line") {
                var idRow = id;
                idRow = idRow.replace("dataField_ind1_id_line", "");
                idRow = idRow.replace("_control", "").trim();
                var value = jq("#" + id).val();
                if (value.length == 1) {
                    jq("#dataField_ind2_id_line" + idRow + "_control").focus();
                }
            } else if (id.substring(0, 22) == "dataField_ind2_id_line") {
                var idRow = id;
                idRow = idRow.replace("dataField_ind2_id_line", "");
                idRow = idRow.replace("_control", "").trim();
                var value = jq("#" + id).val();
                if (value.length == 1) {
                    jq("#dataField_value_id_line" + idRow + "_control").focus();
                    jq("#dataField_value_not_260_id_line" + idRow + "_control").focus();
                    jq("#dataField_260_a_id_line" + idRow + "_control").focus();
                }
            }
        }
    });
    keyboardShortcuts();
});

function globalEditExpandAll(){
    if (jq("#OleLocationInformationSection_disclosureContent").attr('data-open') == 'false') {
        jq("#OleLocationInformationSection_toggle").focus().click();
    }
    if (jq("#OleCallNumberInformation_disclosureContent").attr('data-open') == 'false') {
        jq("#OleCallNumberInformation_toggle").focus().click();
    }
    if (jq("#OleExtentOfOwnershipSection_disclosureContent").attr('data-open') == 'false') {
        jq("#OleExtentOfOwnershipSection_toggle").focus().click();
    }
    if (jq("#OleExtendedInformation_disclosureContent").attr('data-open') == 'false') {
        jq("#OleExtendedInformation_toggle").focus().click();
    }
    if (jq("#OleHoldingNotes_disclosureContent").attr('data-open') == 'false') {
        jq("#OleHoldingNotes_toggle").focus().click();
    }
}

function globalEditCollapseAll(){
    if (jq("#OleLocationInformationSection_disclosureContent").attr('data-open') == 'true') {
        jq("#OleLocationInformationSection_toggle").focus().click();
    }
    if (jq("#OleCallNumberInformation_disclosureContent").attr('data-open') == 'true') {
        jq("#OleCallNumberInformation_toggle").focus().click();
    }
    if (jq("#OleExtentOfOwnershipSection_disclosureContent").attr('data-open') == 'true') {
        jq("#OleExtentOfOwnershipSection_toggle").focus().click();
    }
    if (jq("#OleExtendedInformation_disclosureContent").attr('data-open') == 'true') {
        jq("#OleExtendedInformation_toggle").focus().click();
    }
    if (jq("#OleHoldingNotes_disclosureContent").attr('data-open') == 'true') {
        jq("#OleHoldingNotes_toggle").focus().click();
    }
}

function expandAll() {

    if (jq("#LeaderStructureFields_disclosureContent").attr('data-open') == 'false') {
        jq("#LeaderStructureFields_toggle").focus().click();
    }
    if (jq("#Control_Field_disclosureContent").attr('data-open') == 'false') {
        jq("#WorkBibDataFieldDisclosureSection_toggle").focus().click();
    }
    if (jq("#WorkBibDataFieldDisclosureSection_disclosureContent").attr('data-open') == 'false') {
        jq("#WorkBibDataFieldDisclosureSection_toggle").focus().click();
    }
}

function collapseAll() {
    if (jq("#LeaderStructureFields_disclosureContent").attr('data-open') == 'true') {
        jq("#LeaderStructureFields_toggle").focus().click();
    }
    if (jq("#Control_Field_disclosureContent").attr('data-open') == 'true') {
        jq("#Control_Field_toggle").focus().click();
    }
    if (jq("#WorkBibDataFieldDisclosureSection_disclosureContent").attr('data-open') == 'true') {
        jq("#WorkBibDataFieldDisclosureSection_toggle").focus().click();
    }
}

function expandAllEHoldings(){
    if (jq("#OleEHoldingsOverviewSection_disclosureContent").attr('data-open') == 'false') {
        jq("#OleEHoldingsOverviewSection_toggle").focus().click();
    }
    if (jq("#OleEHoldingsAccessInfoSection_disclosureContent").attr('data-open') == 'false') {
        jq("#OleEHoldingsAccessInfoSection_toggle").focus().click();
    }
    if (jq("#OleEInstanceLocationInformationSection_disclosureContent").attr('data-open') == 'false') {
        jq("#OleEInstanceLocationInformationSection_toggle").focus().click();
    }
    if (jq("#OleEInstanceCallNumberInformation_disclosureContent").attr('data-open') == 'false') {
        jq("#OleEInstanceCallNumberInformation_toggle").focus().click();
    }
    if (jq("#OleExtentOfOwnershipDetails_disclosureContent").attr('data-open') == 'false') {
        jq("#OleExtentOfOwnershipDetails_toggle").focus().click();
    }
    if (jq("#OleEHoldingsRelatedERS_disclosureContent").attr('data-open') == 'false') {
        jq("#OleEHoldingsRelatedERS_toggle").focus().click();
    }
    if (jq("#OleEHoldingsAcquisitionSection_disclosureContent").attr('data-open') == 'false') {
        jq("#OleEHoldingsAcquisitionSection_toggle").focus().click();
    }
    if (jq("#OLEEHoldings-Subscription_disclosureContent").attr('data-open') == 'false') {
        jq("#OLEEHoldings-Subscription_toggle").focus().click();
    }
    if (jq("#OleEInstanceDonorInformation-ListOfDonors_disclosureContent").attr('data-open') == 'false') {
        jq("#OleEInstanceDonorInformation-ListOfDonors_toggle").focus().click();
    }
    if (jq("#OleEHoldingsLicenseSection_disclosureContent").attr('data-open') == 'false') {
        jq("#OleEHoldingsLicenseSection_toggle").focus().click();
    }
    if (jq("#OLEEResourceLicenses-E-Instance_disclosureContent").attr('data-open') == 'false') {
        jq("#OLEEResourceLicenses-E-Instance_toggle").focus().click();
    }
    if (jq("#OleEInstanceHoldingNotes_disclosureContent").attr('data-open') == 'false') {
        jq("#OleEInstanceHoldingNotes_toggle").focus().click();
    }

}

function collapseAllEHoldings(){
    if (jq("#OleEHoldingsOverviewSection_disclosureContent").attr('data-open') == 'true') {
        jq("#OleEHoldingsOverviewSection_toggle").focus().click();
    }
    if (jq("#OleEHoldingsAccessInfoSection_disclosureContent").attr('data-open') == 'true') {
        jq("#OleEHoldingsAccessInfoSection_toggle").focus().click();
    }
    if (jq("#OleEInstanceLocationInformationSection_disclosureContent").attr('data-open') == 'true') {
        jq("#OleEInstanceLocationInformationSection_toggle").focus().click();
    }
    if (jq("#OleEInstanceCallNumberInformation_disclosureContent").attr('data-open') == 'true') {
        jq("#OleEInstanceCallNumberInformation_toggle").focus().click();
    }
    if (jq("#OleExtentOfOwnershipDetails_disclosureContent").attr('data-open') == 'true') {
        jq("#OleExtentOfOwnershipDetails_toggle").focus().click();
    }
    if (jq("#OleEHoldingsRelatedERS_disclosureContent").attr('data-open') == 'true') {
        jq("#OleEHoldingsRelatedERS_toggle").focus().click();
    }
    if (jq("#OleEHoldingsAcquisitionSection_disclosureContent").attr('data-open') == 'true') {
        jq("#OleEHoldingsAcquisitionSection_toggle").focus().click();
    }
    if (jq("#OLEEHoldings-Subscription_disclosureContent").attr('data-open') == 'true') {
        jq("#OLEEHoldings-Subscription_toggle").focus().click();
    }
    if (jq("#OleEInstanceDonorInformation-ListOfDonors_disclosureContent").attr('data-open') == 'true') {
        jq("#OleEInstanceDonorInformation-ListOfDonors_toggle").focus().click();
    }
    if (jq("#OleEHoldingsLicenseSection_disclosureContent").attr('data-open') == 'true') {
        jq("#OleEHoldingsLicenseSection_toggle").focus().click();
    }
    if (jq("#OLEEResourceLicenses-E-Instance_disclosureContent").attr('data-open') == 'true') {
        jq("#OLEEResourceLicenses-E-Instance_toggle").focus().click();
    }
    if (jq("#OleEInstanceHoldingNotes_disclosureContent").attr('data-open') == 'true') {
        jq("#OleEInstanceHoldingNotes_toggle").focus().click();
    }
}


function expandAllHoldings(){
    if (jq("#OleLocationInformationSection_disclosureContent").attr('data-open') == 'false') {
        jq("#OleLocationInformationSection_toggle").focus().click();
    }
    if (jq("#OleCallNumberInformation_disclosureContent").attr('data-open') == 'false') {
        jq("#OleCallNumberInformation_toggle").focus().click();
    }
    if (jq("#OleExtentOfOwnershipSection_disclosureContent").attr('data-open') == 'false') {
        jq("#OleExtentOfOwnershipSection_toggle").focus().click();
    }
    if (jq("#OleExtendedInformation_disclosureContent").attr('data-open') == 'false') {
        jq("#OleExtendedInformation_toggle").focus().click();
    }
    if (jq("#OleHoldingNotes_disclosureContent").attr('data-open') == 'false') {
        jq("#OleHoldingNotes_toggle").focus().click();
    }

}

function collapseAllHoldings(){
    if (jq("#OleLocationInformationSection_disclosureContent").attr('data-open') == 'true') {
        jq("#OleLocationInformationSection_toggle").focus().click();
    }
    if (jq("#OleCallNumberInformation_disclosureContent").attr('data-open') == 'true') {
        jq("#OleCallNumberInformation_toggle").focus().click();
    }
    if (jq("#OleExtentOfOwnershipSection_disclosureContent").attr('data-open') == 'true') {
        jq("#OleExtentOfOwnershipSection_toggle").focus().click();
    }
    if (jq("#OleExtendedInformation_disclosureContent").attr('data-open') == 'true') {
        jq("#OleExtendedInformation_toggle").focus().click();
    }
    if (jq("#OleHoldingNotes_disclosureContent").attr('data-open') == 'true') {
        jq("#OleHoldingNotes_toggle").focus().click();
    }
}



function expandAllItem(){

    if (jq("#OleHoldingsLocationAndCallNumberInformation_disclosureContent").attr('data-open') == 'false') {
        jq("#OleHoldingsLocationAndCallNumberInformation_toggle").focus().click();
    }
    if (jq("#OleItemsLocationAndCallNumberInformation_disclosureContent").attr('data-open') == 'false') {
        jq("#OleItemsLocationAndCallNumberInformation_toggle").focus().click();
    }
    if (jq("#OleItemInformation_disclosureContent").attr('data-open') == 'false') {
        jq("#OleItemInformation_toggle").focus().click();
    }
    if (jq("#OleAcquisitionInformation_disclosureContent").attr('data-open') == 'false') {
        jq("#OleAcquisitionInformation_toggle").focus().click();
    }
    if (jq("#OleDonorInformation-ListOfDonors_disclosureContent").attr('data-open') == 'false') {
        jq("#OleDonorInformation-ListOfDonors_toggle").focus().click();
    }
    if (jq("#OleItemExtendedInformation_disclosureContent").attr('data-open') == 'false') {
        jq("#OleItemExtendedInformation_toggle").focus().click();
    }
    if (jq("#OleCirculationInformation_disclosureContent").attr('data-open') == 'false') {
        jq("#OleCirculationInformation_toggle").focus().click();
    }
    if (jq("#itemClaimsReturnedRecords_disclosureContent").attr('data-open') == 'false') {
        jq("#itemClaimsReturnedRecords_toggle").focus().click();
    }
    if (jq("#itemDamagedRecords_disclosureContent").attr('data-open') == 'false') {
        jq("#itemDamagedRecords_toggle").focus().click();
    }
    if (jq("#MissingPieceItemHistory_disclosureContent").attr('data-open') == 'false') {
        jq("#MissingPieceItemHistory_toggle").focus().click();
    }
    if (jq("#OLEReturnHistoryRecords_disclosureContent").attr('data-open') == 'false') {
        jq("#OLEReturnHistoryRecords_toggle").focus().click();
    }
    if (jq("#OLEPatronFlaggedItemHistoryView-PatronRecords_disclosureContent").attr('data-open') == 'false') {
        jq("#OLEPatronFlaggedItemHistoryView-PatronRecords_toggle").focus().click();
    }
}

function collapseAllItem(){

    if (jq("#OleHoldingsLocationAndCallNumberInformation_disclosureContent").attr('data-open') == 'true') {
        jq("#OleHoldingsLocationAndCallNumberInformation_toggle").focus().click();
    }
    if (jq("#OleItemsLocationAndCallNumberInformation_disclosureContent").attr('data-open') == 'true') {
        jq("#OleItemsLocationAndCallNumberInformation_toggle").focus().click();
    }
    if (jq("#OleItemInformation_disclosureContent").attr('data-open') == 'true') {
        jq("#OleItemInformation_toggle").focus().click();
    }
    if (jq("#OleAcquisitionInformation_disclosureContent").attr('data-open') == 'true') {
        jq("#OleAcquisitionInformation_toggle").focus().click();
    }
    if (jq("#OleDonorInformation-ListOfDonors_disclosureContent").attr('data-open') == 'true') {
        jq("#OleDonorInformation-ListOfDonors_toggle").focus().click();
    }
    if (jq("#OleItemExtendedInformation_disclosureContent").attr('data-open') == 'true') {
        jq("#OleItemExtendedInformation_toggle").focus().click();
    }
    if (jq("#OleCirculationInformation_disclosureContent").attr('data-open') == 'true') {
        jq("#OleCirculationInformation_toggle").focus().click();
    }
    if (jq("#itemClaimsReturnedRecords_disclosureContent").attr('data-open') == 'true') {
        jq("#itemClaimsReturnedRecords_toggle").focus().click();
    }
    if (jq("#itemDamagedRecords_disclosureContent").attr('data-open') == 'true') {
        jq("#itemDamagedRecords_toggle").focus().click();
    }
    if (jq("#MissingPieceItemHistory_disclosureContent").attr('data-open') == 'true') {
        jq("#MissingPieceItemHistory_toggle").focus().click();
    }
    if (jq("#OLEReturnHistoryRecords_disclosureContent").attr('data-open') == 'true') {
        jq("#OLEReturnHistoryRecords_toggle").focus().click();
    }
    if (jq("#OLEPatronFlaggedItemHistoryView-PatronRecords_disclosureContent").attr('data-open') == 'true') {
        jq("#OLEPatronFlaggedItemHistoryView-PatronRecords_toggle").focus().click();
    }
}

function wrapEnterText(){
    if ("bibliographic" == (jq("#hdnDocType_control").val())) {
        jq(".uif-textAreaControl").attr("style","width: 650px;font-size: 13px;height: 18px;");

        for(var i=0;i<jq("textarea").length;i++) {
            var numRows = jq("#dataField_value_id_line"+i+"_control").val().length/80;
            jq("#dataField_value_id_line"+i+"_control").height((parseInt(numRows)+parseInt(1))*18);

        }
        jq(".uif-textAreaControl").keypress(function (e) {

            for(var i=0;i<jq("textarea").length;i++) {
                var numRows = jq("#dataField_value_id_line"+i+"_control").val().length/80;
                jq("#dataField_value_id_line"+i+"_control").height((parseInt(numRows)+parseInt(1))*18);
            }
            var currentContentLength = jq("#"+this.id).val().length;
            var height = jq("#"+this.id).height();
            var cols = jq("#"+this.id).attr("cols");
            //alert(currentContentLength+"  "+cols);
            if (currentContentLength % cols == 0) {
                // alert("adding ---> " +currentContentLength +"  "+cols);
                var sum =  parseInt(80)+parseInt(jq("#"+this.id).attr("cols"));
                if(currentContentLength!=0){
                    jq("#"+this.id).height(height + 18);
                }
                jq("#"+this.id).attr("cols", sum);

            }
            if(currentContentLength % (cols -80) == 0 ){
                // alert("deleting --> "+currentContentLength +"  "+cols +"  "+height);
                var diff =  parseInt(jq("#"+this.id).attr("cols")) - parseInt(80);
                if(currentContentLength != 0) {
                    jq("#"+this.id).height(height - 18);
                }
                jq("#"+this.id).attr("cols", diff);
            }
        });
    }

}




function edit() {
    jq('#hdnEditable_control').val("true");
    jq('#hdnFromSearch_control').val(true);
    submitForm('load', null, null, null, null);
}

function addInstance() {
    jq('#hdnDocCat_control').val("work");
    jq('#hdnDocType_control').val("holdings");
    jq('#hdnDocFormat_control').val("oleml");
    jq('#hiddenDocId_control').val("");
    var bibId = jq("#hiddenBibId_control").val();
    viewHoldingsEdit("",bibId,null,true);
}
function addEInstance() {
    jq('#hdnDocCat_control').val("work");
    jq('#hdnDocType_control').val("eHoldings");
    jq('#hdnDocFormat_control').val("oleml");
    jq('#hiddenDocId_control').val("");
    var bibId = jq("#hiddenBibId_control").val();
    viewEHoldingsEdit("",bibId,null,true);
}

function deleteBib() {
    jq('#hdnDocCat_control').val("work");
    jq('#hdnDocType_control').val("bibliographic");
    jq('#hdnDocFormat_control').val("marc");
    jq('#hiddenDocId_control').val(jq("#hiddenBibId_control").val());
    submitForm('deleteVerify', null, null, null, null);
}

function methodToCall(methodToCall) {

    jq("#hiddenBibFlag_control").val(false);
    jq("#hiddenHoldingFlag_control").val(false);
    jq("#hiddenItemFlag_control").val(false);
    jq("#hiddenEHoldingsFlag_control").val(false);
    localStorage.onchangeRecordEvent = false;
    submitForm(methodToCall, null, null, null, null);
}

var newInstances = 0;
var id;
var canAdd;
var canDelete;
var canDeleteEInstance;

jq(document).ready(function () {
//    jq('#rightClick').click(function(e){
//        //alert("left click");
//        jq('#hdnLoadInstance').focus().click();
//    });

    jq( "#EInstanceDelete" ).click(function() {
        jq("#Editor_Footer").hide();
    });

    jq( "#InstanceDelete" ).click(function() {
        jq("#Editor_Footer").hide();
    });

    jq( "#eInstanceDelete" ).click(function() {
        jq("#Editor_Footer").hide();
    });

    jq(function () {
        jq("#holdingsItemTree_tree").removeClass();
        jq("#holdingsItemTree_tree").jstree({

            "themes": {
                "theme": "krms",
                "icons": false,
                "dots": true
            },
            "core": {
                //"html_titles" : true
            },
            "cookies": {
                //"save_selected": true
            },
            "plugins": [ "themes", "html_data", "ui", "crrm", "contextmenu", "cookies"],
            "contextmenu": {
                "show_at_node": true,
                "items": function (node) {
                    if (checkHoldingsOrItemNode(node) == "HOLDINGS") {

                        canAdd = jq("#canAdd_control").val();
                        canDelete = jq("#canDelete_control").val();

                        if (canAdd == 'true' && canDelete == 'true') {

                            return {
                                "Delete": {
                                    "label": "   Delete Holdings",
                                    "icon": "../krad/images/cancel.png",
                                    "separator_after": true,
                                    "action": function (obj) {

                                        jq("#holdingsItemTree_tree").jstree("deselect_node", jQuery("#holdingsItemTree_tree").jstree("get_selected"));
                                        jq('#hdnDocCat_control').val("work");
                                        jq('#hdnDocType_control').val("holdings");
                                        jq('#hdnDocFormat_control').val("oleml");
                                        jq('#hiddenDocId_control').val(node.attr("class").split(' ')[0]);

                                        submitForm('deleteVerify', null, null, true, function () {
                                            jq("#Editor_Footer").hide();
                                            jq("#holdingsItemTree_tree").jstree('select_node', obj);
                                        });
                                    }
                                },
                                "Create": {
                                    "label": "   Add Item",
                                    "icon": "../krms/images/add.png",
                                    "action": function (obj) {

                                        jq("#holdingsItemTree_tree").jstree("deselect_node", jQuery("#holdingsItemTree_tree").jstree("get_selected"));
                                        jq('#hdnDocCat_control').val("work");
                                        jq('#hdnDocType_control').val("item");
                                        jq("#holdingsItemTree_tree").jstree('select_node', null);
                                        jq('#hdnDocFormat_control').val("oleml");
                                        if (node.attr("class").split(' ').length > 2) {
                                            jq('#hiddenHoldingsId_control').val(node.attr("class").split(' ')[0]);
                                        }
                                        else {
                                            jq('#hiddenHoldingsId_control').val("");
                                        }
                                        jq('#hiddenDocId_control').val("");
                                        viewItemEdit( "",jq('#hiddenBibId_control').val(),jq('#hiddenHoldingsId_control').val(),jq('#hdnEditable_control').val());
                                    }
                                }
                            };
                        }

                        if (canAdd == 'true') {

                            return {
                                "Create": {
                                    "label": "   Add Item",
                                    "icon": "../krms/images/add.png",
                                    "action": function (obj) {

                                        jq("#holdingsItemTree_tree").jstree("deselect_node", jQuery("#holdingsItemTree_tree").jstree("get_selected"));
                                        jq('#hdnDocCat_control').val("work");
                                        jq('#hdnDocType_control').val("item");
                                        jq("#holdingsItemTree_tree").jstree('select_node', null);
                                        jq('#hdnDocFormat_control').val("oleml");
                                        jq('#hiddenHoldingsId_control').val(node.attr("class").split(' ')[0]);
                                        jq('#hiddenDocId_control').val("");
                                        viewItemEdit( "",jq('#hiddenBibId_control').val(),jq('#hiddenHoldingsId_control').val(),jq('#hdnEditable_control').val());
                                    }
                                }
                            };
                        }
                        if (canDelete == 'true') {

                            return {
                                "Delete": {
                                    "label": "   Delete Holdings",
                                    "icon": "../krad/images/cancel.png",
                                    "separator_after": true,
                                    "action": function (obj) {

                                        jq("#holdingsItemTree_tree").jstree("deselect_node", jQuery("#holdingsItemTree_tree").jstree("get_selected"));
                                        jq('#hdnDocCat_control').val("work");
                                        jq('#hdnDocType_control').val("holdings");
                                        jq('#hdnDocFormat_control').val("oleml");
                                        jq('#hiddenDocId_control').val(node.attr("class").split(' ')[0]);

                                        submitForm('deleteVerify', null, null, true, function () {
                                            jq("#Editor_Footer").hide();
                                            jq("#holdingsItemTree_tree").jstree('select_node', obj);
                                        });
                                    }
                                }
                            };
                        }

                    }
                    if (checkHoldingsOrItemNode(node) == "ITEM") {

                        canDelete = jq("#canDelete_control").val();


                        if (canDelete == 'true') {
                            return {
                                "Delete": {
                                    "label": "   Delete Item",
                                    "icon": "../krad/images/cancel.png",
                                    "action": function (obj) {
                                        jq("#holdingsItemTree_tree").jstree("deselect_node", jQuery("#holdingsItemTree_tree").jstree("get_selected"));
                                        jq('#hdnDocCat_control').val("work");
                                        jq('#hdnDocType_control').val("item");
                                        jq('#hdnDocFormat_control').val("oleml");
                                        jq('#hiddenDocId_control').val(node.attr("class").split(' ')[0]);

                                        submitForm('deleteVerify', null, null, true, function () {
                                            jq("#Editor_Footer").hide();
                                            jq("#holdingsItemTree_tree").jstree('select_node', obj);
                                        });

                                    }
                                }
                            };
                        }


                    }
                    if (checkHoldingsOrItemNode(node) == "eHoldings") {

                        canDeleteEInstance = jq("#canDeleteEInstance_control").val();


                        if (canDeleteEInstance == 'true') {
                            return {
                                "Delete": {
                                    "label": "   Delete E-Holdings",
                                    "icon": "../krad/images/cancel.png",
                                    "action": function (obj) {
                                        jq("#holdingsItemTree_tree").jstree("deselect_node", jQuery("#holdingsItemTree_tree").jstree("get_selected"));
                                        jq('#hdnDocCat_control').val("work");
                                        jq('#hdnDocType_control').val("eHoldings");
                                        jq('#hdnDocFormat_control').val("oleml");
                                        jq('#hiddenDocId_control').val(node.attr("class").split(' ')[0]);
                                        submitForm('deleteVerify', null, null, true, function () {  //Modified for jira OLE-6862
                                            jq("#Editor_Footer").hide();
                                            jq("#holdingsItemTree_tree").jstree('select_node', obj);
                                        });
                                        //submitForm('delete', null, null, true);
                                        /*submitForm('delete', null, null, true, function () {
                                            jq('#showEditorFooter').val(true);
                                            jq('#hdnDocCat_control').val("work");
                                            jq('#hdnDocType_control').val("bibliographic");
                                            var isDublinRecord = jq('#hiddenFromDublin_control').val();
                                            if(isDublinRecord == "true") {
                                                jq('#hdnDocFormat_control').val("dublinunq");
                                            }   else {
                                                jq('#hdnDocFormat_control').val("marc");
                                            }
                                            jq('#hiddenDocId_control').val(jq("#hiddenBibId_control").val());
                                            submitForm("load", null, null, null);
                                        });*/
                                    }
                                }
                            };
                        }

                    }

                }
            }
        }).delegate("a", "click", function (e) {
                if (jq(this).attr('class').split(' ')[0] == 'boundWithbibs') {
                    window.open(jq(this).attr("href"));
                } else if (jq(this).attr('class').split(' ')[0] == 'analytics') {
                    window.open(jq(this).attr("href"));
                }
                else {
                    var selectedNode = jq.jstree._focused().get_selected();
                    if (checkHoldingsOrItemNode(selectedNode) == "HOLDINGS") {

                        var id = selectedNode.attr("class").split(' ')[0];
                        if (id.indexOf('w') == 0) {
                           viewHoldingsEdit(id,jq('#hiddenBibId_control').val(),id,jq('#hdnEditable_control').val());
                        }
                    }
                    if (checkHoldingsOrItemNode(selectedNode) == "ITEM") {


                        if (selectedNode.attr("class").split(' ').length > 2) {
                            var id = selectedNode.attr("class").split(' ')[0];
                            var holdingsId = (selectedNode.parent()).parent().attr("class").split(' ')[0];
                            if (jq("#Control_Field_008_control").val() != null && jq("#Control_Field_008_control").val() != "") {
                                viewItemEdit(id,jq('#hiddenBibId_control').val(),holdingsId,jq('#hdnEditable_control').val());
                            } else if (jq('#OleHoldingLocation_h0').val() == 'item') {
                                viewItemEdit(id,jq('#hiddenBibId_control').val(),holdingsId,jq('#hdnEditable_control').val());
                            } else if(jq("#hdnEditable_control").val()== "false") {
                                viewItemEdit(id,jq('#hiddenBibId_control').val(),holdingsId,jq('#hdnEditable_control').val());
                            }
                            else {
                                if(localStorage.onchangeRecordEvent == "true" && localStorage.closeWindowEvent == "true" && jq("#hiddenItemFlag_control").val() == "true"){
                                    localStorage.onchangeRecordEvent = false;
                                    localStorage.closeWindowEvent= false;
                                    jq("#hiddenItemFlag_control").val(false);
                                }
                                viewItemEditSelf(id,jq('#hiddenBibId_control').val(),holdingsId,jq('#hdnEditable_control').val());
                            }

                        }
                        else {
                            submitForm('load', null, null, true, function () {
                                jq('#hiddenDocId_control').val("");
                                jq('#hiddenHoldingsId_control').val((selectedNode.parent()).parent().attr("class").split(' ')[0]);

                            });
                        }
                    }
                    if (checkHoldingsOrItemNode(selectedNode) == "eHoldings") {

                        var id = selectedNode.attr("class").split(' ')[0];
                        viewEHoldingsEdit(id,jq('#hiddenBibId_control').val(),id,jq('#hdnEditable_control').val());
                    }
                    var current =jq('#hiddenDocId_control').val();
                    jq('#holdingsItemTree_tree').jstree('deselect_all');
                    jq('#holdingsItemTree_tree').jstree('select_node', '.'+current);
                }
            });
    });


    function checkHoldingsOrItemNode(node) {
        if (node.parent().get(0).tagName == 'UL') {
            if ((node.parent()).parent().get(0).tagName == 'DIV') {
                if (node.attr("class").split(' ')[1] == 'eHoldings') {
                    return "eHoldings";
                }
                return "HOLDINGS";
            }
            else {
                return "ITEM";
            }
        }
    }

});

function Test(){
    jq('#canDelete_control').val(true);
    alert(jq('#EditorView h1 span').text()!=null?jq('#EditorView h1 span').text().toUpperCase():null);
    jq(document).ready();
}


function browse(docType) {
    var url = getApplicationPath() + "callnumberBrowseController?viewId=CallNumberBrowseView&methodToCall=browse&docType=" + docType + "&closeBtnShowFlag=true";

    if ("item" == (docType)) {
        var location = jq('#OleItemLocationLevelName_control').val();
        var itemClassificationScheme = jq('#OleItemShelvingScheme_control').val();
        if (itemClassificationScheme == ' ') {
            itemClassificationScheme = "' '";
        }
        url = url + "&classificationScheme=" + itemClassificationScheme + "&location=" + location + "&callNumberBrowseText=" + jq('#OleItemCallNumber_control').val();
        jq('#OleItemCallNumber_control').attr('autocomplete', 'off');
    }
    else {
        var location = jq('#OleHoldingLocation_control').val();
        var holdingsClassificationScheme = jq('#OleHoldingShelvingScheme_control').val();
        if (holdingsClassificationScheme == ' ') {
            holdingsClassificationScheme = "' '";
        }
        url = url + "&classificationScheme=" + holdingsClassificationScheme + "&location=" + location + "&callNumberBrowseText=" + jq('#OleHoldingCallNumber_control').val();
    }
    window.open(url);
}

function itemClickableLink(){
    var link ;
    link = jq('#oleItemAccessInformationURI_control').val();
    if(link == null || link ==""){
    }else{
        if(validateUrl(link)==true){
            window.open(link);
        }
    }
}


function getApplicationPath() {
    var loc = window.location;
    var loc1 = loc.toString();
    return loc1.substring(0, loc1.lastIndexOf('/') + 1);
}

function commonWidthForField() {
    jq('#WorkBibDataFieldSection table tr td').eq(0).css('width', '95%');
    jq('#DublinElementSection table tr td').eq(0).css('width', '80%');
    wrapEnterText();
}
function createSerialReceiving() {
    window.open("serialReceiving?viewId=OLESerialReceivingView&methodToCall=docHandler&command=initiate&bibId=" + jq("#hiddenBibId_control").val() + "&instanceId=" + jq("#hiddenHoldingsId_control").val());
}

function showSerialReceiving(){
    window.open("serialReceiving?viewId=OLESerialReceivingView&methodToCall=docHandler&docId=" + jq("#serialReceivingDocId_control").val() + "&command=displayDocSearchView&bibId="+jq("#hiddenBibId_control").val() + "&instanceId=" +jq("#hiddenHoldingsId_control").val());
}

function getViewId() {
    var pathname = window.location.href;
    if (!(pathname.indexOf("viewId")!=-1)) {
        var pathname = document.location;
    }

    var parameters = pathname.split("&")

    if (parameters[0].indexOf("viewId")!=-1) {
        var id = parameters[0].split("=")
        var val = id[1];
        return val;

    }
}
jq(document).ready(function () {
    jq("#oleItemStatus_control").change(function () {
        if (jq('#oleItemStatus_control').val() != "") {
            jq('#LocalItem_h0').val("true")
        }
    });
});

function printSlip(){
    window.open("editorcontroller?viewId=WorkInstanceEditorView&methodToCall=printCallSlip&formKey="+jq("#editorFormKey_control").val());
}


function bibClickableLink_edit(id) {
    var field ="dataField_value_id_line";
    var control = "_control";
    var field = '#'.concat(field).concat(id).concat(control);
    var link = jq(field).val();
    var outputValue=makeUrlClickable(link);
    var htmlText="<div id=\"hiddenDialog\" title=\"Test Link Dialog\"> <span>"+outputValue+"</span> </div>";
    jq(field).prepend(jq(htmlText));
    jq(function() {
        jq( "#hiddenDialog" ).dialog();
    });
}

function eInstanceClickableLink(id) {
    var field ="OleEinstance-linkURL-editable_line";
    var control = "_control";
    var field = '#'.concat(field).concat(id).concat(control);
    var link = jq(field).val();
    var outputValue=makeUrlClickable(link);
    var htmlText="<div id=\"hiddenDialog\" title=\"EHolding Link Dialog\"> <span>"+outputValue+"</span> </div>";
    jq(field).prepend(jq(htmlText));
    jq(function() {
        jq( "#hiddenDialog" ).dialog({
            position:['middle',400]
        });
    });
}

function holdingsClickableLink(id) {
    var field ="OleAccessInformationField_line";
    var control = "_control";
    var field = '#'.concat(field).concat(id).concat(control);
    var link = jq(field).val();
    var outputValue=makeUrlClickable(link);
    var htmlText="<div id=\"hiddenDialog\" title=\"Holding Link Dialog\"> <span>"+outputValue+"</span> </div>";
    jq(field).prepend(jq(htmlText));
    jq(function() {
        jq( "#hiddenDialog" ).dialog({
            position:['middle',700]
        });
    });
}


function eInstancelocalPersistentClickableLink() {
    var field ="OleEinstance_localPersistentLink_id";
    var control = "_control";
    var field = '#'.concat(field).concat(control);
    var link = jq(field).val();
    var outputValue=makeUrlClickable(link);
    var htmlText="<div id=\"hiddenDialog\" title=\"EHolding Link Dialog\"> <span>"+outputValue+"</span> </div>";
    jq(field).prepend(jq(htmlText));
    jq(function() {
        jq( "#hiddenDialog" ).dialog({
            position:['middle',500]
        });
    });
}

function eInstanceAdminClickableLink() {
    var field ="OleEinstance-adminUrl";
    var control = "_control";
    var field = '#'.concat(field).concat(control);
    var link = jq(field).val();
    var outputValue=makeUrlClickable(link);
    var htmlText="<div id=\"hiddenDialog\" title=\"EHolding Link Dialog\"> <span>"+outputValue+"</span> </div>";
    jq(field).prepend(jq(htmlText));
    jq(function() {
        jq( "#hiddenDialog" ).dialog({
            position:['middle',500]
        });
    });
}

function bibClickableLink_view(id) {
    var field ="dataField_value_id_readOnly_line";
    var control = "_control";
    var field = '#'.concat(field).concat(id).concat(control);
    var link = jq(field).text();
    var outputValue=makeUrlClickable(link);
    var htmlText="<div id=\"hiddenDialog\" title=\"Test Link Dialog\"> <span>"+outputValue+"</span> </div>";
    jq(field).prepend(jq(htmlText));
    jq(function() {
        jq( "#hiddenDialog" ).dialog();
    });
}

function validateUrl(url){
    var protocolArray = new Array();
    protocolArray[0] = "http://";
    protocolArray[1] = "https://";
    protocolArray[2] = "ftp://";
    protocolArray[3] = "mailto:";
    var urlFound=false;
    for(var arrCount=0;arrCount<protocolArray.length;arrCount++){
        if(url.indexOf(protocolArray[arrCount]) == 0){
            urlFound=true;
            break;
        }
    }
    return urlFound;
}

function makeUrlClickable(content){
    content = content.replace('&nbsp;', ' ');
    var resultValue="";
    var clickableUrl="";
    var count=0;
    var length=content.length;
    var protocolArray = new Array();
    protocolArray[0] = "http://";
    protocolArray[1] = "https://";
    protocolArray[2] = "ftp://";
    protocolArray[3] = "mailto:";
    while(count<length){
        var textToScan=content.substring(count);
        var urlFound=false;
        var spaceIndex=-1;
        var url="";
        for(var arrCount=0;arrCount<protocolArray.length;arrCount++){
            if(textToScan.indexOf(protocolArray[arrCount]) == 0){
                urlFound=true;
                spaceIndex = textToScan.indexOf(' ');
                if(spaceIndex>0){
                    url=textToScan.substring(0,spaceIndex)
                }else{
                    url=textToScan.substring(0);
                }
                break;
            }
        }
        if(urlFound==true){
            clickableUrl="<a  href='" + url + "' target='_blank'>" + url + "</a>";
            count=count+url.length;
            resultValue=resultValue+clickableUrl;
        }else{
            resultValue=resultValue+content.charAt(count);
            count=count+1;
        }
    }
    return resultValue;

}
jq(window).load(function () {
//    localStorage.closeWindowEvent= false;

    jq("#OleEinstance-purchaseOrderId_control").html(jq("#OleEinstance-purchaseOrderId_control").text());
    jq("#holdingsItemTree_tree a").each( function(){
        var id = jq(this).attr("class");
        if(id.indexOf("who")!=-1){
            if(id.indexOf("eHoldings")!=-1) {
                jq(this).attr("title","eHoldings" + jq(this).find('span').html());
            }
            else {
                jq(this).attr("title","holdings" + jq(this).find('span').html());
            }
        }
        if(id.indexOf("wio")!=-1){
            jq(this).attr("title", "item" + jq(this).find('span').html());
        }
        jq(this).css('width' ,'75%');
        var current =jq('#hiddenDocId_control').val();
        jq('#holdingsItemTree_tree').jstree('deselect_all');
        if(current !=""){
            jq('#holdingsItemTree_tree').jstree('select_node', '.'+current);
        }

    });

    if(jq("#hiddenGlobalEditFlag_control").val() == "true") {
        //alert(jq('#hdnDocType_control').val());
        if(jq('#hdnDocType_control').val() == "holdings") {

            if(jq("#hiddenHoldingsLocationEditFlag_control").val() == "false"){
                jq("#OleHoldingLocation_control").attr("disabled",true);
            }
            if(jq("#hiddenHoldingsCallNumberEditFlag_control").val() == "false"){
                jq("#OleHoldingCallNumber_control").attr("disabled",true);
                jq("#callNumberHoldingsBrowseLink").attr("disabled",true);
            }
            if(jq("#hiddenHoldingsShelvingOrderEditFlag_control").val() == "false"){
                jq("#OleHoldingShelvingOrder_control").attr("disabled",true);
            }
            if(jq("#hiddenHoldingsCallNumberTypeEditFlag_control").val() == "false"){
                jq("#OleHoldingShelvingScheme_control").attr("disabled",true);
            }
            if(jq("#hiddenHoldingsCallNumberPrefixEditFlag_control").val() == "false"){
                jq("#OleHoldingCallNumberPrefix_control").attr("disabled",true);
            }
            if(jq("#hiddenHoldingsCopyNumberEditFlag_control").val() == "false"){
                jq("#OleHoldingCopyNumber_control").attr("disabled",true);
            }
            if(jq("#hiddenHoldingsExtentOwnerShipEditFlag_control").val() == "false"){
                jq("#extentTextualHoldingsType_line0_control").attr("disabled",true);
                jq("#extentTextualHoldings_line0_control").attr("disabled",true);
                jq("#extentSubHoldingNoteTypeField_line0_line0_control").attr("disabled",true);
                jq("#extentSubHoldingNoteDescField_line0_line0_control").attr("disabled",true);
                jq("#oleEowHoldingNotes_addTagButton_line0_line0").attr("disabled",true);
                jq("#oleEowHoldingNotes_removeTagButton_line0_line0").attr("disabled",true);
                jq("#extentOfOwnership_addTagButton_line0").attr("disabled",true);
                jq("#extentOfOwnership_removeTagButton_line0").attr("disabled",true);
            }
            if(jq("#hiddenHoldingsExtendedInfoEditFlag_control").val() == "false"){
                jq("#OleReceiptStatusField_control").attr("disabled",true);
                jq("#OleAccessInformationField_line0_control").attr("disabled",true);
                jq("#oleAccessInformation_addTagButton_line0").attr("disabled",true);
                jq("#oleAccessInformation_removeTagButton_line0").attr("disabled",true);
                jq("#holdingsClickableLink_line0").remove();

            }
            if(jq("#hiddenHoldingsNoteEditFlag_control").val() == "false"){
                jq("#OleHoldingNoteTypeField_line0_control").attr("disabled",true);
                jq("#OleHoldingNoteDescField_line0_control").attr("disabled",true);
                jq("#OleHoldingNotes_addTagButton_line0").attr("disabled",true);
                jq("#OleHoldingNotes_removeTagButton_line0").attr("disabled",true);
            }

            if(jq("#hiddenHoldingsReceiptStatusEditFlag_control").val() == "false"){
                jq("#OleReceiptStatusField_control").attr("disabled",true);
            }

            if(jq("#hiddenGlobalAccessInformationEditFlag_control").val() == "false"){
                jq("#OleAccessInformationField_line0_control").attr("disabled",true);
                jq("#oleAccessInformation_addTagButton_line0").attr("disabled",true);
                jq("#oleAccessInformation_removeTagButton_line0").attr("disabled",true);
                jq("#holdingsClickableLink_line0").remove();
            }

        }
        else if(jq('#hdnDocType_control').val() == "item") {

            if(jq("#hiddenItemLocationEditFlag_control").val() == "false"){
                jq("#OleItemLocationLevelName_control").attr("disabled",true);
            }

            if(jq("#hiddenItemCallNumberPrefixEditFlag_control").val() == "false"){
                jq("#OleItemCallNumberPrefix_control").attr("disabled",true);
            }

            if(jq("#hiddenItemCallNumberEditFlag_control").val() == "false"){
                jq("#OleItemCallNumber_control").attr("disabled",true);
                jq("#callNumberItemBrowseLink").attr("disabled",true);
            }

            if(jq("#hiddenItemShelvingOrderEditFlag_control").val() == "false"){
                jq("#OleItemShelvingOrder_control").attr("disabled",true);
            }

            if(jq("#hiddenItemCallNumberTypeEditFlag_control").val() == "false"){
                jq("#OleItemShelvingScheme_control").attr("disabled",true);
            }




            if(jq("#hiddenItemEnumerationEditFlag_control").val() == "false"){
                jq("#oleItemEnumeration_control").attr("disabled",true);
            }

            if(jq("#hiddenItemBarcodeEditFlag_control").val() == "false"){
                jq("#oleItemAccessInformationBarcode_control").attr("disabled",true);
            }

            if(jq("#hiddenItemChronologyEditFlag_control").val() == "false"){
                jq("#oleItemChronology_control").attr("disabled",true);
            }

            if(jq("#hiddenItemBarcodeARSLEditFlag_control").val() == "false"){
                jq("#oleItemBarcodeARSL_control").attr("disabled",true);
            }

            if(jq("#hiddenItemCopyNumberEditFlag_control").val() == "false"){
                jq("#oleItemCopyNumber_control").attr("disabled",true);
            }


            if(jq("#hiddenItemFormerIdentifiersEditFlag_control").val() == "false"){
                jq("#oleItemFormerIdentifier_control").attr("disabled",true);
            }

            if(jq("#hiddenItemAccessInfoEditFlag_control").val() == "false"){
                jq("#oleItemAccessInformationURI_control").attr("disabled",true);
                jq("#itemClickableLink").remove();
            }

            if(jq("#hiddenItemStatisticalSearchingCodesEditFlag_control").val() == "false"){
                jq("#oleItemStatisticalSearchingCodes_control").attr("disabled",true);
            }

            if(jq("#hiddenItemTypeEditFlag_control").val() == "false"){
                jq("#oleItemItemType_control").attr("disabled",true);
            }

            if(jq("#hiddenItemTempItemTypeEditFlag_control").val() == "false"){
                jq("#oleItemTemporaryItemType_control").attr("disabled",true);
            }

            if(jq("#hiddenItemNumberOfPiecesEditFlag_control").val() == "false"){
                jq("#oleItemNumberOfPieces_control").attr("disabled",true);
            }

            if(jq("#hiddenItemAcqInfoEditFlag_control").val() == "false"){
                jq("#OleAcquisitionInformation").remove();
            }


            if(jq("#hiddenItemPOLineItemIDEditFlag_control").val() == "false"){
                jq("#oleItemPoID_control").attr("disabled",true);
            }

            if(jq("#hiddenItemVendorLineItemIDEditFlag_control").val() == "false"){
                jq("#oleItemVendorLineItemID_control").attr("disabled",true);
            }

            if(jq("#hiddenItemFundEditFlag_control").val() == "false"){
                jq("#oleItemFund_control").attr("disabled",true);
            }

            if(jq("#hiddenItemPriceEditFlag_control").val() == "false"){
                jq("#oleItemPrice_control").attr("disabled",true);
            }


            if(jq("#hiddenItemDonorCodeEditFlag_control").val() == "false"){
                jq("#oleItemDonorCode_add_control").attr("disabled",true);
                jq("#item_donor-add_add").attr("disabled",true);
            }

            if(jq("#hiddenItemDonorPublicDisplayEditFlag_control").val() == "false"){
                jq("#oleItemDonorPublicDisplay_add_control").attr("disabled",true);
            }

            if(jq("#hiddenItemDonorNoteEditFlag_control").val() == "false"){
                jq("#oleItemDonorNote_add_control").attr("disabled",true);
            }


            if(jq("#hiddenItemStatusEditFlag_control").val() == "false"){
                jq("#oleItemStatus_control").attr("disabled",true);
            }

            if(jq("#hiddenItemStatusDateEditFlag_control").val() == "false"){
                jq("#oleItemStatusEffectiveDate_control").attr("disabled",true);
            }

            if(jq("#hiddenItemCheckinNoteEditFlag_control").val() == "false"){
                jq("#oleItemCheckinNote_control").attr("disabled",true);
            }

            if(jq("#hiddenItemFastAddEditFlag_control").val() == "false"){
                jq("#oleItemFastAdd_control").attr("disabled",true);
            }
/*
            if(jq("#hiddenItemDueDatetimeEditFlag_control").val() == "false"){
                jq("#oleDueDatetime_control").attr("disabled",true);
                // jq(".ui-datepicker-trigger").remove();
            }*/

            if(jq("#hiddenItemCurrentBorrowerEditFlag_control").val() == "false"){
                jq("#oleCurrentBorrower_control").attr("disabled",true);
            }

            if(jq("#hiddenItemProxyBorrowerEditFlag_control").val() == "false"){
                jq("#oleProxyBorrower_control").attr("disabled",true);
            }



            if(jq("#hiddenItemClaimsReturnEditFlag_control").val() == "false"){
                jq("#oleClaimsReturnFlag_control").attr("disabled",true);
            }

            if(jq("#hiddenItemClaimsReturnedCreateDateEditFlag_control").val() == "false"){
                jq("#oleClaimsReturnCreateDate_control").attr("disabled",true);
            }

            if(jq("#hiddenItemClaimsReturnedNoteEditFlag_control").val() == "false"){
                jq("#oleClaimsReturnNote_control").attr("disabled",true);
            }

            if(jq("#hiddenItemDamagedStatusEditFlag_control").val() == "false"){
                jq("#oleItemDamagedSection_control").attr("disabled",true);
            }

            if(jq("#hiddenItemDamagedItemNoteEditFlag_control").val() == "false"){
                jq("#OleItemDamaged-Note-Horizontal-Section_control").attr("disabled",true);
            }

            if(jq("#hiddenItemMissingPieceEditFlag_control").val() == "false"){
                jq("#OleMissingItem-missingPieceFlag_control").attr("disabled",true);
            }

            if(jq("#hiddenItemNoOfmissingpiecescountEditFlag_control").val() == "false"){
                jq("#OleMissingItem-missingPiecesCount_control").attr("disabled",true);
            }

            if(jq("#hiddenItemMissingPiecesEffectiveDateEditFlag_control").val() == "false"){
                jq("#OleMissingItem-missingPieceEffectiveDate_control").attr("disabled",true);
            }

            if(jq("#hiddenItemMissingPieceNoteEditFlag_control").val() == "false"){
                jq("#OleMissingItem-Note-Section_control").attr("disabled",true);
            }



            if(jq("#hiddenItemExtndInfoEditFlag_control").val() == "false"){
                jq("#OleItemNoteTypeField_line0_control").attr("disabled",true);
                jq("#OleItemNoteDescField_line0_control").attr("disabled",true);
                jq("#OleItemNote_addTagButton_line0").attr("disabled",true);
                jq("#OleItemNote_removeTagButton_line0").attr("disabled",true);


            }

            if(jq("#hiddenItemExtndInfoEditFlag_control").val() == "false"){
                jq("#OleItemExtendedInformation").attr("disabled",true);
            }

        }
        else if(jq('#hdnDocType_control').val() == "eHoldings") {

            if(jq("#hiddenEHoldingsAccessStatusEditFlag_control").val() == "false"){
                jq("#OleEinstance-accessStatus_control").attr("disabled",true);
            }

            if(jq("#hiddenEHoldingsPlatformEditFlag_control").val() == "false"){
                jq("#OleEinstance-platformName_control").attr("disabled",true);
            }

            if(jq("#hiddenEHoldingsStatusDateEditFlag_control").val() == "false"){
                jq("#OleEinstance-statusDate_control").attr("disabled",true);
            }

            if(jq("#hiddenEHoldingsPublisherEditFlag_control").val() == "false"){
                jq("#OleEinstance-publisher_control").attr("disabled",true);
            }

            if(jq("#hiddenEHoldingsStaffOnlyEditFlag_control").val() == "false"){
                // jq("#OleEinstance-staffOnlyFlagForHoldings_control").attr("disabled",true);
            }

            if(jq("#hiddenEHoldingsImprintEditFlag_control").val() == "false"){
                jq("#OleEinstance-imprint_control").attr("disabled",true);
            }


            if(jq("#hiddenEHoldingsStatisticalCodeEditFlag_control").val() == "false"){
                jq("#OleEinstance-statisticalSearchingCodeValue_control").attr("disabled",true);
            }

            if(jq("#hiddenEHoldingsISSNEditFlag_control").val() == "false"){
                jq("#OleEinstance-ISSN_control").attr("disabled",true);
            }

            if(jq("#hiddenEHoldingsLocationEditFlag_control").val() == "false"){
                jq("#OleEHoldingLocation_control").attr("disabled",true);
            }

            if(jq("#hiddenEHoldingsCallNumberPrefixEditFlag_control").val() == "false"){
                jq("#OleEHoldingCallNumberPrefix_control").attr("disabled",true);
            }

            if(jq("#hiddenEHoldingsCallNumberEditFlag_control").val() == "false"){
                jq("#OleEHoldingCallNumber_control").attr("disabled",true);
                jq("#OleEHoldingCallNumberHoldingsBrowseLink").attr("disabled",true);
            }

            if(jq("#hiddenEHoldingsShelvingOrderEditFlag_control").val() == "false"){
                jq("#OleEHoldingShelvingOrder_control").attr("disabled",true);
            }



            if(jq("#hiddenEHoldingsCallNumberTypeEditFlag_control").val() == "false"){
                jq("#OleEHoldingShelvingScheme_control").attr("disabled",true);
            }
            if(jq("#hiddenEHoldingsCoverageExtentOfOwnerShipEditFlag_control").val() == "false"){
                jq("#coverageStartDate_line0_control").attr("disabled",true);
                jq("#coverageStartVolume_line0_control").attr("disabled",true);
                jq("#coverageStartIssue_line0_control").attr("disabled",true);
                jq("#coverageEndDate_line0_control").attr("disabled",true);
                jq("#coverageEndVolume_line0_control").attr("disabled",true);
                jq("#coverageEndIssue_line0_control").attr("disabled",true);
                jq("#OleExtentOfOwnershipDetails-CoverageSection_addTagButton_id_line0").attr("disabled",true);
                jq("#OleExtentOfOwnershipDetails-CoverageSection_removeTagButton_id_line0").attr("disabled",true);
            }
            if(jq("#hiddenEHoldingsPerpetualAccessEditFlag_control").val() == "false"){
                jq("#perpetualStartDate_line0_control").attr("disabled",true);
                jq("#perpetualAccessStartVolume_line0_control").attr("disabled",true);
                jq("#perpetualAccessStartIssue_line0_control").attr("disabled",true);
                jq("#perpetualAccessEndDate_line0_control").attr("disabled",true);
                jq("#perpetualAccessEndVolume_line0_control").attr("disabled",true);
                jq("#perpetualAccessEndIssue_line0_control").attr("disabled",true);
                jq("#OleExtentOfOwnershipDetails-PerpetualAccessSection_addTagButton_id_line0").attr("disabled",true);
                jq("#OleExtentOfOwnershipDetails-PerpetualAccessSection_removeTagButton_id_line0").attr("disabled",true);
            }
            // TODO have to do  Acquisition Information
            if(jq("#hiddenEHoldingsSubscriptionEditFlag_control").val() == "false"){
                jq("#OleEinstance-subscriptionStatus_control").attr("disabled",true);
            }
            /*if(jq("#hiddenEHoldingsAccessInformationEditFlag_control").val() == "false"){
             jq("#OleEHoldingsAccessInfoSection").remove();
             }*/

            if(jq("#hiddenEHoldingsLinkEditFlag_control").val() == "false"){
                jq("#OleEinstance-linkURL_control").attr("disabled",true);
            }
            if(jq("#hiddenEHoldingsSimultaneousEditFlag_control").val() == "false"){
                jq("#OleEinstance-numberOfSimultaneousUser_control").attr("disabled",true);
            }
            if(jq("#hiddenEHoldingsPersistentLinkEditFlag_control").val() == "false"){
                jq("#OleEinstance-localPersistentLink_control").attr("disabled",true);
            }
            if(jq("#hiddenEHoldingsAccessLocationEditFlag_control").val() == "false"){
                jq("#OleEinstance-accessLocation_control").attr("disabled",true);
            }
            if(jq("#hiddenEHoldingsLinkTextEditFlag_control").val() == "false"){
                jq("#OleEinstance-linkText_control").attr("disabled",true);
            }
            if(jq("#hiddenEHoldingsAdminUserNameEditFlag_control").val() == "false"){
                jq("#OleEinstance-adminUserName_control").attr("disabled",true);
            }
            if(jq("#hiddenEHoldingsAccessUserNameEditFlag_control").val() == "false"){
                jq("#OleEinstance-accessUsername_control").attr("disabled",true);
            }
            if(jq("#hiddenEHoldingsAdminPasswordEditFlag_control").val() == "false"){
                jq("#OleEinstance-adminPassword_control").attr("disabled",true);
            }
            if(jq("#hiddenEHoldingsAccessPasswordEditFlag_control").val() == "false"){
                jq("#OleEinstance-accessPassword_control").attr("disabled",true);
            }
            if(jq("#hiddenEHoldingsAdminUrlEditFlag_control").val() == "false"){
                jq("#OleEinstance-adminUrl_control").attr("disabled",true);
            }
            if(jq("#hiddenEHoldingsAuthenticationEditFlag_control").val() == "false"){
                jq("#OleEinstance-authenticationType_control").attr("disabled",true);
            }
            if(jq("#hiddenEHoldingsProxiedEditFlag_control").val() == "false"){
                jq("#OleEinstance-proxiedResource_control").attr("disabled",true);
            }
            if(jq("#hiddenEHoldingsIllEditFlag_control").val() == "false"){
                jq("#OleEinstance-interLibraryLoanAllowed_control").attr("disabled",true);
            }


            if(jq("#hiddenEHoldingsRelationShipsEditFlag_control").val() == "false"){
                jq("#OleEHoldingsRelatedERS").remove();
            }
            /*
             if(jq("#hiddenEHoldingsAcquisitionInformationEditFlag_control").val() == "false"){
             jq("#OleEHoldingsAcquisitionSection").remove();
             }


             if(jq("#hiddenEHoldingsAccessInformationEditFlag_control").val() == "false"){
             jq("#OleEHoldingsAccessInfoSection").remove();
             }*/

            if(jq("#hiddenEHoldingsLicenseDetailsEditFlag_control").val() == "false"){
                jq("#OleEHoldingsLicenseSection").remove();
            }

            if(jq("#hiddenEHoldingsEHoldingsNoteEditFlag_control").val() == "false"){
                jq("#OleEHoldingNoteTypeField_line0_control").attr("disabled",true);
                jq("#OleEHoldingNoteDescField_line0_control").attr("disabled",true);
                jq("#OleEInstanceHoldingNotes-addTagButton_id_line0").attr("disabled",true);
                jq("#OleEInstanceHoldingNotes_removeTagButton_id_line0").attr("disabled",true);
            }


            if(jq("#hiddenEHoldingsDonorCodeEditFlag_control").val() == "false"){
                jq("#oleEInstanceDonorCode_add_control").attr("disabled",true);
            }

            if(jq("#hiddenEHoldingsDonorPublicDisplayEditFlag_control").val() == "false"){
                jq("#oleEInstanceDonorPublicDisplay_add_control").attr("disabled",true);
            }

            if(jq("#hiddenEHoldingsDonorNoteEditFlag_control").val() == "false"){
                jq("#oleEInstanceDonorNote_add_control").attr("disabled",true);
            }
            // TODO holdings_donor-add for add button


        }


    }
    jq('#buildDocTree_tree').jstree('open_all');
    wrapEnterText();
    wrapEnterTextForEInstanceLink();
});

function viewHoldingsEdit(docId,bibId,instanceId,editable){
    window.open(jq('#channelUrl_control').val() + "editorcontroller?viewId=EditorView&methodToCall=load&docCategory=work&docType=holdings&docFormat=oleml&docId="+docId+"&bibId="+bibId+"&instanceId="+instanceId+"&editable="+editable+"&fromSearch=true");
    return false;
}

function viewItemEdit(docId,bibId,instanceId,editable){
    window.open(jq('#channelUrl_control').val() + "editorcontroller?viewId=EditorView&methodToCall=load&docCategory=work&docType=item&docFormat=oleml&docId="+docId+"&bibId="+bibId+"&instanceId="+instanceId+"&editable="+editable+"&fromSearch=true");
    return false;

}

function viewEHoldingsEdit(docId,bibId,holdingsId,editable){
    window.open(jq('#channelUrl_control').val() + "editorcontroller?viewId=EditorView&methodToCall=load&docCategory=work&docType=eHoldings&docFormat=oleml&docId="+docId+"&editable="+editable+"&fromSearch=true&bibId="+bibId+"&holdingsId="+holdingsId);
    return false;

}

function viewItemEditSelf(docId,bibId,instanceId,editable){
    window.open(jq('#channelUrl_control').val() + "editorcontroller?viewId=EditorView&methodToCall=load&docCategory=work&docType=item&docFormat=oleml&docId="+docId+"&bibId="+bibId+"&instanceId="+instanceId+"&editable="+editable+"&fromSearch=true","_self");
    return false;

}

function closeEditorWindow(){

    //alert(localStorage.onchangeRecordEvent);

    if(localStorage.onchangeRecordEvent=="true") {
        loadingPage();
    }
    else {
        localStorage.onchangeRecordEvent="false";
        window.close();
        methodToCall('returnToHub');

        /*var url = document.URL;
         alert(url);*/
    }
}

function unloadPage(){
    var name =  jQuery.uaMatch(navigator.userAgent).browser;
    if(localStorage.closeWindowEvent== "true" && (jq("#hiddenBibFlag_control").val()== "true" || jq("#hiddenHoldingFlag_control").val() =="true" ||
        jq("#hiddenItemFlag_control").val()=="true" || jq("#hiddenEHoldingsFlag_control").val()=="true")) {
        //alert("true closew window event");
        if( (jq("#hiddenBibFlag_control").val()== "true" || jq("#hiddenHoldingFlag_control").val() =="true" ||
            jq("#hiddenItemFlag_control").val()=="true" || jq("#hiddenEHoldingsFlag_control").val()=="true")) {
            if("chrome"==name) {
                return 'This page is asking you to confirm that you want to leave - data you have entered may not be saved';
            }else{
                return "This page is asking you to confirm that you want to leave - data you have entered may not be saved";
                /* var confirm1 = confirm('Data you have entered may not be saved.If you want to Save and Close . Press OK ');
                 if(confirm1){
                 jq("#updatedDate_h0").val(true);
                 if(jq("#hiddenBibFlag_control").val()=="true") {
                 jq('#hiddenDocId_control').val(jq('#hiddenBibId_control').val());
                 jq('#hdnDocCat_control').val("work");
                 jq('#hdnDocType_control').val("bibliographic");
                 jq('#hdnDocFormat_control').val("marc");
                 jq("#hiddenBibFlag_control").val(false);
                 }
                 else if(jq("#hiddenHoldingFlag_control").val()=="true" || jq("#hiddenEHoldingsFlag_control").val()=="true"){
                 //alert("holdings");
                 jq('#hiddenDocId_control').val(jq('#hiddenHoldingsId_control').val());
                 jq('#hdnDocCat_control').val("work");
                 jq('#hdnDocType_control').val("holdings");
                 jq('#hdnDocFormat_control').val("oleml");
                 jq("#hiddenHoldingFlag_control").val(false);
                 jq("#hiddenEHoldingsFlag_control").val(false);
                 }
                 else if(jq("#hiddenItemFlag_control").val()=="true"){
                 jq('#hdnDocCat_control').val("work");
                 jq('#hdnDocType_control').val("item");
                 jq('#hdnDocFormat_control').val("oleml");
                 jq("#hiddenItemFlag_control").val(false);
                 }
                 submitForm('save', null, null, true, function () {
                 jq("#updatedDate_h0").val(false);
                 localStorage.onchangeRecordEvent = true;
                 localStorage.closeWindowEvent = false;
                 window.close();
                 });
                 window.onbeforeunload = null;
                 }*/
            }

        }
        window.onbeforeunload = null;
    }

}

window.onbeforeunload = unloadPage;

jq("#submitEditor").live("click",function(){
    // alert(localStorage.closeWindowEvent);
    localStorage.closeWindowEvent = "false";
})



jq(window).load(function () {
    var divLength= jq("#WorkBibDataFieldSection_disclosureContent tbody div").length;
    for(var count=0;count<divLength;count++){
        var value=  jq("#WorkBibDataFieldSection_disclosureContent tbody div#dataField_value_id_readOnly_line"+count+" span#dataField_value_id_readOnly_line"+count+"_control").text();
        var returnValue=makeUrlClickable(value);
        jq("#WorkBibDataFieldSection_disclosureContent tbody div#dataField_value_id_readOnly_line"+count+" span#dataField_value_id_readOnly_line"+count+"_control").html(returnValue);
    }
});

function eHoldingsDonorCodeDisable(index) {
    if (parseInt(jq("input[id='oleEInstanceDonorCode_line" + index + "_control']").val().trim().length) > 0) {
        jq("input[id='oleEInstanceDonorPublicDisplay_line" + index + "_control']").attr("disabled", "true");
        jq("input[id='oleEInstanceDonorNote_line" + index + "_control']").attr("disabled", "true");
    }
    else {
        jq("input[id='oleEInstanceDonorPublicDisplay_line" + index + "_control']").removeAttr("disabled");
        jq("input[id='oleEInstanceDonorNote_line" + index + "_control']").removeAttr("disabled");
    }
}

function validateEHoldingsDonorCode(index) {
    if (parseInt(jq("input[id='oleEInstanceDonorCode_line" + index + "_control']").val().trim().length) > 0) {
        submitForm('validateEHoldingsDonorCode',{index:JSON.stringify(index)},null,true,function(){
            jq("button[id='eHoldings_donor_add_line" + index + "']").focus();
        });
    }
}

function itemDonorCodeDisable(index) {
    if (parseInt(jq("input[id='oleItemDonorCode_line" + index + "_control']").val().trim().length) > 0) {
        jq("input[id='oleItemDonorPublicDisplay_line" + index + "_control']").attr("disabled", "true");
        jq("input[id='oleItemDonorNote_line" + index + "_control']").attr("disabled", "true");
    }
    else {
        jq("input[id='oleItemDonorPublicDisplay_line" + index + "_control']").removeAttr("disabled");
        jq("input[id='oleItemDonorNote_line" + index + "_control']").removeAttr("disabled");
    }
}

function validateItemDonorCode(index) {
    if (parseInt(jq("input[id='oleItemDonorCode_line" + index + "_control']").val().trim().length) > 0) {
        submitForm('validateItemDonorCode',{index:JSON.stringify(index)},null,true,function(){
            jq("button[id='item_donor-add_line" + index + "']").focus();
        });
    }
}

function getCoverageStartDate(index) {
    if(jq('#'+'coverageStartDateString_line'+index+'_control').val()!=""){
        jq('#'+'coverageStartDateFormat_line'+index+'_control').prop("readonly", true).next("img").hide();
    } else if(jq('#'+'coverageStartDateString_line'+index+'_control').val()==""){
        jq('#'+'coverageStartDateFormat_line'+index+'_control').prop("readonly", false).next("img").show();
    }

    if(jq('#'+'coverageStartDateFormat_line'+index+'_control').val()!=""){
        jq('#'+'coverageStartDateString_line'+index+'_control').attr("readonly", true);
    } else if(jq('#'+'coverageStartDateFormat_line'+index+'_control').val()==""){
        jq('#'+'coverageStartDateString_line'+index+'_control').attr("readonly", false);
    }
}

function getCoverageEndDate(index) {
    if(jq('#'+'coverageEndDateString_line'+index+'_control').val()!=""){
        jq('#'+'coverageEndDateFormat_line'+index+'_control').prop("readonly", true).next("img").hide();
    } else if(jq('#'+'coverageEndDateString_line'+index+'_control').val()==""){
        jq('#'+'coverageEndDateFormat_line'+index+'_control').prop("readonly", false).next("img").show();
    }

    if(jq('#'+'coverageEndDateFormat_line'+index+'_control').val()!=""){
        jq('#'+'coverageEndDateString_line'+index+'_control').attr("readonly", true);
    } else if(jq('#'+'coverageEndDateFormat_line'+index+'_control').val()==""){
        jq('#'+'coverageEndDateString_line'+index+'_control').attr("readonly", false);
    }
}

function getPerpetualAccessStartDate(index) {
    if(jq('#'+'perpetualAccessStartDateString_line'+index+'_control').val()!=""){
        jq('#'+'perpetualAccessStartDateFormat_line'+index+'_control').prop("readonly", true).next("img").hide();
    } else if(jq('#'+'perpetualAccessStartDateString_line'+index+'_control').val()==""){
        jq('#'+'perpetualAccessStartDateFormat_line'+index+'_control').prop("readonly", false).next("img").show();
    }

    if(jq('#'+'perpetualAccessStartDateFormat_line'+index+'_control').val()!=""){
        jq('#'+'perpetualAccessStartDateString_line'+index+'_control').attr("readonly", true);
    } else if(jq('#'+'perpetualAccessStartDateFormat_line'+index+'_control').val()==""){
        jq('#'+'perpetualAccessStartDateString_line'+index+'_control').attr("readonly", false);
    }
}

function getPerpetualAccessEndDate(index) {
    if(jq('#'+'perpetualAccessEndDateString_line'+index+'_control').val()!=""){
        jq('#'+'perpetualAccessEndDateFormat_line'+index+'_control').prop("readonly", true).next("img").hide();
    } else if(jq('#'+'perpetualAccessEndDateString_line'+index+'_control').val()==""){
        jq('#'+'perpetualAccessEndDateFormat_line'+index+'_control').prop("readonly", false).next("img").show();
    }

    if(jq('#'+'perpetualAccessEndDateFormat_line'+index+'_control').val()!=""){
        jq('#'+'perpetualAccessEndDateString_line'+index+'_control').attr("readonly", true);
    } else if(jq('#'+'perpetualAccessEndDateFormat_line'+index+'_control').val()==""){
        jq('#'+'perpetualAccessEndDateString_line'+index+'_control').attr("readonly", false);
    }
}


function wrapEnterTextForEInstanceLink() {
    if ("eHoldings" == (jq("#hdnDocType_control").val())) {
        jq(".uif-textAreaControl").attr("style", "width: 220px;font-size: 13px;height: 18px;");
        var name = jQuery.uaMatch(navigator.userAgent).browser;
        //var numRows = jq("#OleEinstance_localPersistentLink_id_control").val().length / 20;
        //jq("#OleEinstance_localPersistentLink_id_control").height((parseInt(numRows) + parseInt(1)) * 18);

        jq("textarea").each(function () {
            var id = jq(this).attr("id");
            var urlNumRows = jq(this).val().length / 20;
            var name = jQuery.uaMatch(navigator.userAgent).browser;
            if ("chrome" == name) {
                if (id.includes("OleEinstance-linkURL_line")) {
                    var height = (parseInt(urlNumRows)) * 18;
                    if (height > 18) {
                        jq(this).height((parseInt(urlNumRows)) * 18);
                    }
                }
                if (id.includes("OleEinstance-linkText_line")) {
                    var height = (parseInt(urlNumRows)) * 18;
                    if (height > 18) {
                        jq(this).height((parseInt(urlNumRows)) * 18);
                    }
                }
                if (id.includes("OleEinstance-localPersistentLink")) {
                    var height = (parseInt(urlNumRows)) * 18;
                    if (height > 18) {
                        jq(this).height((parseInt(urlNumRows)) * 18);
                    }
                }
                if (id.includes("OleEinstance-adminUrl")) {
                    var height = (parseInt(urlNumRows)) * 18;
                    if (height > 18) {
                        jq(this).height((parseInt(urlNumRows)) * 18);
                    }
                }
            } else {
                if (id.contains("OleEinstance-linkURL_line")) {
                    var height = (parseInt(urlNumRows)) * 18;
                    if (height > 18) {
                        jq(this).height((parseInt(urlNumRows)) * 18);
                    }
                }
                if (id.contains("OleEinstance-linkText_line")) {
                    var height = (parseInt(urlNumRows)) * 18;
                    if (height > 18) {
                        jq(this).height((parseInt(urlNumRows)) * 18);
                    }
                }
                if (id.contains("OleEinstance-localPersistentLink")) {
                    var height = (parseInt(urlNumRows)) * 18;
                    if (height > 18) {
                        jq(this).height((parseInt(urlNumRows)) * 18);
                    }
                }
                if (id.includes("OleEinstance-adminUrl")) {
                    var height = (parseInt(urlNumRows)) * 18;
                    if (height > 18) {
                        jq(this).height((parseInt(urlNumRows)) * 18);
                    }
                }
            }
        });

        jq(".uif-textAreaControl").on('keyup', function() {
            //Chorme
            var currentContentLength = jq("#" + this.id).val().length;
            var height = jq("#" + this.id).height();
            var cols = jq("#" + this.id).attr("cols");
            var flag = "false";
            if (currentContentLength % cols == 0) {
                var sum = parseInt(20) + parseInt(jq("#" + this.id).attr("cols"));
                if (currentContentLength != 0) {
                    jq("#" + this.id).height(height + 18);
                }
                jq("#" + this.id).attr("cols", sum);
            }

            if (currentContentLength % cols == 20) {
                var sum = parseInt(20) + parseInt(jq("#" + this.id).attr("cols"));
                if (currentContentLength != 0) {
                    jq("#" + this.id).height(height + 18);
                }
                jq("#" + this.id).attr("cols", sum);
                cols = jq("#" + this.id).attr("cols");
            }
            if (cols - currentContentLength > 40) {
                var diff = parseInt(jq("#" + this.id).attr("cols")) - parseInt(20);

                if (currentContentLength < 20) {
                    diff = 20;
                    jq("#" + this.id).height(18);
                } else {
                    if (currentContentLength != 0) {
                        height = jq("#" + this.id).height();
                        jq("#" + this.id).height(height - 18);
                    }
                }
            }
            jq("#" + this.id).attr("cols", diff);
        });

        jq(".uif-textAreaControl").on('keypress', function() {
            //Firefox
            var currentContentLength = jq("#" + this.id).val().length;
            var height = jq("#" + this.id).height();
            var cols = jq("#" + this.id).attr("cols");
            var flag = "false";
            if (currentContentLength % cols == 0) {
                var sum = parseInt(20) + parseInt(jq("#" + this.id).attr("cols"));
                if (currentContentLength != 0) {
                    jq("#" + this.id).height(height + 18);
                }
                jq("#" + this.id).attr("cols", sum);
            }

            if (currentContentLength % cols == 20) {
                var sum = parseInt(20) + parseInt(jq("#" + this.id).attr("cols"));
                if (currentContentLength != 0) {
                    jq("#" + this.id).height(height + 18);
                }
                jq("#" + this.id).attr("cols", sum);
                cols = jq("#" + this.id).attr("cols");
            }
            if (cols - currentContentLength > 40) {
                var diff = parseInt(jq("#" + this.id).attr("cols")) - parseInt(20);

                if (currentContentLength < 20) {
                    diff = 20;
                    jq("#" + this.id).height(18);
                } else {
                    if (currentContentLength != 0) {
                        height = jq("#" + this.id).height();
                        jq("#" + this.id).height(height - 18);
                    }
                }
            }
            jq("#" + this.id).attr("cols", diff);
        });
    }
}
function coverageDateStart(id){
    var JSONObject = JSON.stringify(id.extraData);//.split("actionParameters[selectedLineIndex]");
    var obj = jQuery.parseJSON(JSONObject);
    for (key in obj) {
        if (key == "actionParameters[selectedLineIndex]")
            id = parseInt(obj[key]) + 1;
    }
    var objId = id.split("_");
    var index = objId[1].substring(4, 5);
    getCoverageStartDate(index);
    getCoverageEndDate(index);

    jq('#hiddenCoverageDateStartField_control').val("true");
    if(jq('#hiddenCoverageDateStartField_control').val() == "false" && jq('#'+'coverageStartDateFormat_line'+index+'_control').val()==""
        &&jq('#'+'coverageStartDateString_line'+index+'_control').val()==""
        &&jq('#'+'coverageStartVolume_line'+index+'_control').val()==""
        &&jq('#'+'coverageStartIssue_line'+index+'_control').val()==""){
        jq('#hiddenCoverageDateStartField_control').val("false");
    }
}

function coverageDateEnd(id){
    var JSONObject = JSON.stringify(id.extraData);//.split("actionParameters[selectedLineIndex]");
    var obj = jQuery.parseJSON(JSONObject);
    for (key in obj) {
        if (key == "actionParameters[selectedLineIndex]")
            id = parseInt(obj[key]) + 1;
    }
    var objId = id.split("_");
    var index = objId[1].substring(4, 5);
    getCoverageStartDate(index);
    getCoverageEndDate(index);
    jq('#hiddenCoverageDateEndField_control').val("true");
    if(jq('#hiddenCoverageDateEndField_control').val() == "false" && jq('#'+'coverageEndDateString_line'+index+'_control').val()==""
        &&jq('#'+'coverageEndDateFormat_line'+index+'_control').val()==""
        &&jq('#'+'coverageEndVolume_line'+index+'_control').val()==""
        &&jq('#'+'coverageEndIssue_line'+index+'_control').val()==""){
        jq('#hiddenCoverageDateEndField_control').val("false");
    }
}

function perpetualDateStart(id) {
    var JSONObject = JSON.stringify(id.extraData);//.split("actionParameters[selectedLineIndex]");
    var obj = jQuery.parseJSON(JSONObject);
    for (key in obj) {
        if (key == "actionParameters[selectedLineIndex]")
            id = parseInt(obj[key]) + 1;
    }
    var objId = id.split("_");
    var index = objId[1].substring(4, 5);
    getPerpetualAccessStartDate(index);
    getPerpetualAccessEndDate(index);
    jq('#hiddenPerpetualDateStartField_control').val("true");
    if(jq('#hiddenPerpetualDateStartField_control').val() == "false" && jq('#'+'perpetualAccessStartDateFormat_line'+index+'_control').val()==""
        &&jq('#'+'perpetualAccessStartDateString_line'+index+'_control').val()==""
        &&jq('#'+'perpetualAccessStartVolume_line'+index+'_control').val()==""
        &&jq('#'+'perpetualAccessStartIssue_line'+index+'_control').val()==""){
        jq('#hiddenPerpetualDateStartField_control').val("false");
    }
}

function perpetualDateEnd(id) {
    var JSONObject = JSON.stringify(id.extraData);//.split("actionParameters[selectedLineIndex]");
    var obj = jQuery.parseJSON(JSONObject);
    for (key in obj) {
        if (key == "actionParameters[selectedLineIndex]")
            id = parseInt(obj[key]) + 1;
    }
    var objId = id.split("_");
    var index = objId[1].substring(4, 5);
    getPerpetualAccessStartDate(index);
    getPerpetualAccessEndDate(index);
    jq('#hiddenPerpetualDateEndField_control').val("true");
    if(jq('#hiddenPerpetualDateEndField_control').val() == "false" && jq('#'+'perpetualAccessEndDateString_line'+index+'_control').val()==""
        &&jq('#'+'perpetualAccessEndDateFormat_line'+index+'_control').val()==""
        &&jq('#'+'perpetualAccessEndVolume_line'+index+'_control').val()==""
        &&jq('#'+'perpetualAccessEndIssue_line'+index+'_control').val()==""){
        jq('#hiddenPerpetualDateEndField_control').val("false");
    }
}

function noOfUser(id){
    jq('#hiddenNoOfUser_control').val("true");
    if(jq('#hiddenNoOfUser_control').val() == "false" && jq('#OleEinstance-numberOfSimultaneousUser_control').val() == ""){
        jq('#hiddenNoOfUser_control').val("false");
    }
}

function authenticationType(id){
    jq('#hiddenAuthenticationType_control').val("true");
}

function accessLocation(id){
    jq('#hiddenAccessLocation_control').val("true");
}

function statisticalCode(id){
    jq('#hiddenStatisticalCode_control').val("true");
}

function focusOnTag(index) {
    jq("#dataField_tag_id_line" + (index + 1) + "_control").focus();

}

function printBib(){
    window.open("editorcontroller?viewId=EditorView&methodToCall=printBib&formKey="+jq("#editorFormKey_control").val());
}

function copyBib(methodToCall) {
    jq('#hiddenDocId_control').val('');
    submitForm(methodToCall, null, null, null, null);
}

var valueChange006 = false;
var valueChange007 = false;
var valueChange008 = false;

function valueChange(controlField) {
    if (controlField == '006') {
        valueChange006 = true;
    } else if (controlField == '007') {
        valueChange007 = true;
    } else if (controlField == '008') {
        valueChange008 = true;
    }
}

function validateAndSave() {
    if (valueChange006) {
        myConfirm("Control Field 006 value is modified. Please set the control field and submit.", null, function() { submitForm('save', null, null, null, null); }, "Please set 006 field");
    } else if (valueChange007) {
        myConfirm("Control Field 007 value is modified. Please set the control field and submit.", null, function() { submitForm('save', null, null, null, null); }, "Please set 007 field");
    } else if (valueChange008) {
        myConfirm("Control Field 008 value is modified. Please set the control field and submit.", null, function() { submitForm('save', null, null, null, null); }, "Please set 008 field");
    } else {
        submitForm('save', null, null, null, null);
    }
}

var isCtrl = false;
var isEnter = false;
var isD = false;
var isS = false;
var ctrlEnter = false;
var is3 = false;
var isF = false;

function keyboardShortcuts() {
    jq(document).live("keyup", function (e) {
        if (e.which == 17) {
            isCtrl = false;
        }
        if (e.which == 13) {
            isEnter = false;
        }
        if (e.which == 68 || e.which == 100) {
            isD = false;
        }
        if (e.which == 83 || e.which == 115) {
            isS = false;
        }
        if (e.which == 51) {
            is3 = false;
        }
        if (e.which == 70 || e.which == 102) {
            isF = false;
        }
    });

    jq(document).live("keydown", function (e) {
        if (e.which == 17) {
            isCtrl = true;
        }
        if (e.which == 13) {
            isEnter = true;
        }
        if (e.which == 68 || e.which == 100) {
            isD = true;
        }
        if (e.which == 83 || e.which == 115) {
            isS = true;
        }
        if (e.which == 51) {
            is3 = true;
        }
        if (e.which == 70 || e.which == 102) {
            isF = true;
        }
        if (isCtrl && isEnter) {
            ctrlEnter = true;
            var dataFieldRowCount = jq("div#WorkBibDataFieldDisclosureSection_disclosureContent table tbody tr").length;
            jq("#dataField_addTagButton_id_line" + (dataFieldRowCount - 1)).click();
        } else if (isCtrl && isD) {
            e.preventDefault();
            jq("#dataField_removeTagButton_id").click();
        } else if (isCtrl && isS) {
            e.preventDefault();
            submitForm('save', null, null, null, null);
        } else if (isCtrl && is3) {
            var dataFieldRowCount = jq("div#WorkBibDataFieldDisclosureSection_disclosureContent table tbody tr").length;
            jq("#hdnShortcutAddDataField_control").val(true);
            jq("#dataField_addTagButton_id_line" + (dataFieldRowCount - 1)).click();
            jq("#hdnShortcutAddDataField_control").val(false);
        } else if (isCtrl && isF) {
            //location.reload();
        }
    });
}
