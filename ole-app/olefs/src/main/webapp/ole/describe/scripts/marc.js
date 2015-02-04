var positionChangeStr = "false";
var controlField006Id = 0;
var controlField007Id = 0;
var positionStr = "false";
var change008;
var data008 = false;
var controlIndex006 = null;
var controlIndex007 = null;
var controlIndex008 = null;
var dateEnterOnfile = "######";
var errorFlag;
var dataFieldsRowsCount = 0;
var updatedDataFieldsRowsCount = 0;
var field006RowsCount = 0;
var updated006FieldsRowsCount = 0;
var field007RowsCount = 0;
var updated007FieldsRowsCount = 0;
var dataFieldTag = "";
var dataFieldInd1 = "";
var dataFieldInd2 = "";
var dataFieldValue = "";
var field006Value = "";
var field007Value = "";
var extOwnerAddOrRemoveCount = 0;
var updatedExtOwnerAddOrRemoveCount = 0;
var extOfInfoRowsCount = 0;
var updatedExtOfInfoRowsCount = 0;
var holdingsNoteRowsCount = 0;
var updatedHoldingsNoteRowsCount = 0;
var itemExtInfoRowCount = 0;
var updatedItemExtInfoRowCount = 0;
var itemDonorRowCount = 0;
var updatedItemDonorRowCount = 0;
var coverageSectionRowCount = 0;
var updatedCoverageSectionRowCount = 0;
var perpetualAccessSectionRowCount = 0;
var updatedPerpetualAccessSectionRowCount = 0;
var eHoldingDonorRowCount = 0;
var updatedEHoldingDonorRowCount = 0;
var eHoldingNotesRowCount = 0;
var updatedEHoldingNotesRowCount = 0;

var unsaved = false;
/*jq("#hiddenBibFlag_control").val(false);
jq("#hiddenHoldingFlag_control").val(false);
jq("#hiddenItemFlag_control").val(false);
jq("#hiddenEHoldingsFlag_control").val(false);*/



/*

function addOrRemoveDataField(){

    updatedDataFieldsRowsCount = jq("div#WorkBibDataFieldSection_disclosureContent table tbody tr").length;
    if(dataFieldsRowsCount != updatedDataFieldsRowsCount) {
        localStorage.onchangeRecordEvent = true;
        localStorage.closeWindowEvent= true;
        jq("#hiddenBibFlag_control").val(true);
    } else {
//        alert(" DATA FIELD ELSE");
         for(var count = 0; count < dataFieldsRowsCount; count++){
             if(dataFieldTag != jq("input#dataField_tag_id_line"+count+"_control").val() ||
             dataFieldInd1 != jq("input#dataField_ind1_id_line"+count+"_control").val() ||
             dataFieldInd2 != jq("input#dataField_ind2_id_line"+count+"_control").val() ||
             dataFieldValue != jq("textarea#dataField_value_id_line"+count+"_control").val()){
                 localStorage.onchangeRecordEvent = true;
                 localStorage.closeWindowEvent= true;
                 jq("#hiddenBibFlag_control").val(true);
             }  else {

                 localStorage.onchangeRecordEvent = false;
                 localStorage.closeWindowEvent= false;
                 jq("#hiddenBibFlag_control").val(false);
             }
         }
    }
}
function addOrRemove006(){
    updated006FieldsRowsCount = jq("div#Control_Field_006_Collection_disclosureContent table tbody tr").length;
    if(field006RowsCount != updated006FieldsRowsCount) {
        localStorage.onchangeRecordEvent = true;
        localStorage.closeWindowEvent= true;
        jq("#hiddenBibFlag_control").val(true);
    } else {
//        alert("006 ELSE");
        for(var count = 0; count < field006RowsCount; count++){
            if(field006Value != jq("input#Control_Field_006_line"+count+"_control").val()){
                localStorage.onchangeRecordEvent = true;
                localStorage.closeWindowEvent= true;
                jq("#hiddenBibFlag_control").val(true);
            } else {
                localStorage.onchangeRecordEvent = false;
                localStorage.closeWindowEvent= false;
                jq("#hiddenBibFlag_control").val(false);
            }
        }
    }
}

function addOrRemove007(){
    updated007FieldsRowsCount = jq("div#Control_Field_007_Collection_disclosureContent table tbody tr").length;
    if(field007RowsCount != updated007FieldsRowsCount) {
        localStorage.onchangeRecordEvent = true;
        localStorage.closeWindowEvent= true;
        jq("#hiddenBibFlag_control").val(true);
    } else {
//        alert("007 ELSE");
        for(var count = 0; count < field007RowsCount; count++){
            if(field007Value != jq("input#Control_Field_007_line"+count+"_control").val()){
                localStorage.onchangeRecordEvent = true;
                localStorage.closeWindowEvent= true;
                jq("#hiddenBibFlag_control").val(true);
            } else {
                localStorage.onchangeRecordEvent = false;
                localStorage.closeWindowEvent= false;
                jq("#hiddenBibFlag_control").val(false);
            }
        }
    }
}
*/

//BIB

function bibAddOrRemove(){
    updatedDataFieldsRowsCount = jq("div#WorkBibDataFieldSection_disclosureContent table tbody tr").length;
    updated006FieldsRowsCount = jq("div#Control_Field_006_Collection_disclosureContent table tbody tr").length;
    updated007FieldsRowsCount = jq("div#Control_Field_007_Collection_disclosureContent table tbody tr").length;
    if(dataFieldsRowsCount != updatedDataFieldsRowsCount || field006RowsCount != updated006FieldsRowsCount || field007RowsCount != updated007FieldsRowsCount) {
        localStorage.onchangeRecordEvent = true;
        localStorage.closeWindowEvent= true;
        jq("#hiddenBibFlag_control").val(true);
    }
}

//HOLDINGS

function holdingsAddOrRemove(){
    updatedExtOwnerAddOrRemoveCount = jq("#OleExtentOfOwnershipSection_disclosureContent table tbody tr").length;
    updatedExtOfInfoRowsCount = jq("div#OleExtendedInformation_disclosureContent table tbody tr").length;
    updatedHoldingsNoteRowsCount = jq("div#OleHoldingNotes_disclosureContent table tbody tr").length;
    if(extOwnerAddOrRemoveCount != updatedExtOwnerAddOrRemoveCount || extOfInfoRowsCount != updatedExtOfInfoRowsCount || holdingsNoteRowsCount != updatedHoldingsNoteRowsCount) {
        localStorage.onchangeRecordEvent = true;
        localStorage.closeWindowEvent= true;
        jq("#hiddenHoldingFlag_control").val(true);
    }
}

/*
function addOrRemoveExtOfInfo(){
    updatedExtOfInfoRowsCount = jq("div#OleExtendedInformation_disclosureContent table tbody tr").length;
    if(extOfInfoRowsCount != updatedExtOfInfoRowsCount) {
        localStorage.onchangeRecordEvent = true;
        localStorage.closeWindowEvent= true;
        jq("#hiddenHoldingFlag_control").val(true);
    }
}

function addOrRemoveHoldingsNote(){
    updatedHoldingsNoteRowsCount = jq("div#OleHoldingNotes_disclosureContent table tbody tr").length;
    if(holdingsNoteRowsCount != updatedHoldingsNoteRowsCount) {
        localStorage.onchangeRecordEvent = true;
        localStorage.closeWindowEvent= true;
        jq("#hiddenHoldingFlag_control").val(true);
    }
}*/

//ITEM
function itemAddOrRemove(){
    updatedItemExtInfoRowCount = jq("div#OleNotes table tbody tr").length;
    updatedItemDonorRowCount = jq("div#OleDonorInformation-ListOfDonors table tbody tr ").length;
    if(itemExtInfoRowCount != updatedItemExtInfoRowCount || itemDonorRowCount != updatedItemDonorRowCount) {
        localStorage.onchangeRecordEvent = true;
        localStorage.closeWindowEvent= true;
        jq("#hiddenItemFlag_control").val(true);
    }
}

/*
function addOrRemoveDonor(){
    updatedItemDonorRowCount = jq("div#OleDonorInformation table tbody tr th table tbody tr").length;
    if(itemDonorRowCount != updatedItemDonorRowCount) {
        localStorage.onchangeRecordEvent = true;
        localStorage.closeWindowEvent= true;
        jq("#hiddenItemFlag_control").val(true);
    }

}
*/

function deleteItemDonor() {
    for(var count=0; count<itemDonorRowCount-1; count++) {
        jq("#OleDonorInformation-ListOfDonors_del_line"+count).click(function() {
            localStorage.onchangeRecordEvent = true;
            localStorage.closeWindowEvent= true;
            jq("#hiddenItemFlag_control").val(true);
        });
    }
}

//Eholdings

function eHoldingsAddOrRemove(){
    updatedCoverageSectionRowCount = jq("div#OleExtentOfOwnershipDetails-CoverageSection table tbody tr").length;
    updatedPerpetualAccessSectionRowCount = jq("div#OleExtentOfOwnershipDetails-PerpetualAccessSection table tbody tr").length;
    updatedEHoldingDonorRowCount = jq("div#OleEInstanceDonorInformation-ListOfDonors table tbody tr").length;
    updatedEHoldingNotesRowCount = jq("div#OleEInstanceHoldingNotes_disclosureContent table tbody tr").length;
    if(coverageSectionRowCount != updatedCoverageSectionRowCount || perpetualAccessSectionRowCount != updatedPerpetualAccessSectionRowCount || eHoldingDonorRowCount != updatedEHoldingDonorRowCount || eHoldingNotesRowCount != updatedEHoldingNotesRowCount) {
        localStorage.onchangeRecordEvent = true;
        localStorage.closeWindowEvent= true;
        jq("#hiddenEHoldingsFlag_control").val(true);
    }

}

function deleteEHoldingsDonor() {
    for(var count=0; count<eHoldingDonorRowCount-1; count++) {
        jq("#OleEInstanceDonorInformation-ListOfDonors_del_line"+count).click(function() {
            localStorage.onchangeRecordEvent = true;
            localStorage.closeWindowEvent= true;
            jq("#hiddenEHoldingsFlag_control").val(true);
        });
    }
}

function confirmationOfSavingRecord() {

    // for item
    jq("#WorkItemViewPage").change(function(){
//    alert("item");
        localStorage.onchangeRecordEvent = true;
        localStorage.closeWindowEvent= true;
        jq("#hiddenItemFlag_control").val(true);
    });

    jq("#WorkItemViewPage").keypress(function(){
//        alert("item");
        localStorage.onchangeRecordEvent = true;
        localStorage.closeWindowEvent= true;
        jq("#hiddenItemFlag_control").val(true);
    });

    // holdings

    jq("#WorkHoldingsViewPage").change(function(){
        //alert("holdings");
        localStorage.onchangeRecordEvent = true;
        localStorage.closeWindowEvent= true;
        jq("#hiddenHoldingFlag_control").val(true);
    });

    jq("#WorkHoldingsViewPage").keypress(function(){
        //alert("holdings");
        localStorage.onchangeRecordEvent = true;
        localStorage.closeWindowEvent= true;
        jq("#hiddenHoldingFlag_control").val(true);
    });

    // bibliographic
    jq("#WorkBibEditorViewPage").change(function(){
//        alert("bib change");
        localStorage.onchangeRecordEvent = true;
        localStorage.closeWindowEvent= true;
        jq("#hiddenBibFlag_control").val(true);
    });

    jq("#WorkBibEditorViewPage").keypress(function(){
//        alert("bib keypress");
        localStorage.onchangeRecordEvent = true;
        localStorage.closeWindowEvent= true;
        jq("#hiddenBibFlag_control").val(true);
    });

    // eHoldings

    jq("#WorkEInstanceViewPage").change(function(){
       // alert("eholdings");
        localStorage.onchangeRecordEvent = true;
        localStorage.closeWindowEvent= true;
        jq("#hiddenEHoldingsFlag_control").val(true);
    });

    jq("#WorkEInstanceViewPage").keypress(function(){
        // alert("eholdings");
        localStorage.onchangeRecordEvent = true;
        localStorage.closeWindowEvent= true;
        jq("#hiddenEHoldingsFlag_control").val(true);
    });



}

function saveModifiyEditorRecord(){

    jq("#updatedDate_h0").val(true);
    if(jq("#hiddenBibFlag_control").val()=="true") {
        jq('#hiddenDocId_control').val(jq('#hiddenBibId_control').val());
        jq('#hdnDocCat_control').val("work");
        jq('#hdnDocType_control').val("bibliographic");
        jq('#hdnDocFormat_control').val("marc");
        jq("#hiddenBibFlag_control").val(false);
    }
    else if(jq("#hiddenHoldingFlag_control").val()=="true"){
        //alert("holdings");
        jq('#hiddenDocId_control').val(jq('#hiddenHoldingsId_control').val());
        jq('#hdnDocCat_control').val("work");
        jq('#hdnDocType_control').val("holdings");
        jq('#hdnDocFormat_control').val("oleml");
        jq("#hiddenHoldingFlag_control").val(false);
        jq("#hiddenEHoldingsFlag_control").val(false);
    }
    else if(jq("#hiddenEHoldingsFlag_control").val()=="true"){
        //alert("eHoldings");
        jq('#hdnDocCat_control').val("work");
        jq('#hdnDocType_control').val("eHoldings");
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
        if(jq(".uif-errorMessageItem").attr("class")==null) {
            window.close();
            methodToCall('returnToHub');

            /*var url = document.URL;
            alert(url);
*/
//            localStorage.onchangeRecordEvent = false;
        }
        confirmationOfSavingRecord();
    });

}
function loadingPage() {

       // alert( jq("#hiddenBibFlag_control").val()+"   "+jq("#hiddenHoldingFlag_control").val()+"  "+jq("#hiddenItemFlag_control").val()+"  "+jq("#hiddenEHoldingsFlag_control").val());
       /* alert(jq('#hiddenBibId_control').val());
        alert(jq('#hiddenHoldingsId_control').val());*/
        if(jq(".uif-errorMessageItem").attr("class")!=null) {
            errorFlag = true;
    //            localStorage.onchangeRecordEvent = false;
        }
        if(errorFlag == true || jq("#hiddenBibFlag_control").val()== "true" || jq("#hiddenHoldingFlag_control").val() =="true" || jq("#hiddenItemFlag_control").val()=="true" || jq("#hiddenEHoldingsFlag_control").val()=="true") {
            myConfirm2('Click "Save and Close" to save the changes and close the window or click "Cancel" to close this error message', function () {
                    saveModifiyEditorRecord();
               }, function () {
                     localStorage.onchangeRecordEvent = true;
                },
                'Unsaved notification message'
            );
        }
    else {
           window.close();
           methodToCall('returnToHub');

           /*var url = document.URL;
            alert(url);*/
        }
}

jq(document).ready(function () {
    function disableEnterKey(evt) {
        if (evt.keyCode == 13) {
            return false;
        }
    }
    jq("input.uif-textControl").live("keypress", function(e){

        if(e.keyCode==13){
            return false;
        }
    })

    //document.onkeypress = disableEnterKey;
    leaderDropDownSetting();
    jq("#Control_Field_001_h1").val("Control_Field_006_line0_control")
    jq("#Control_Field_001_h4").val("Control_Field_007_line0_control")
});

jq(window).load(function () {
    //Bib's Data Field
    dataFieldsRowsCount = jq("div#WorkBibDataFieldSection_disclosureContent table tbody tr").length;
    for(var count = 0; count < dataFieldsRowsCount; count++){
        dataFieldTag = jq("input#dataField_tag_id_line"+count+"_control").val();
        dataFieldInd1 = jq("input#dataField_ind1_id_line"+count+"_control").val();
        dataFieldInd2 = jq("input#dataField_ind2_id_line"+count+"_control").val();
        dataFieldValue = jq("textarea#dataField_value_id_line"+count+"_control").val();
    }
    field006RowsCount = jq("div#Control_Field_006_Collection_disclosureContent table tbody tr").length;
    for(var count = 0; count < field006RowsCount; count++){
        field006Value = jq("input#Control_Field_006_line"+count+"_control").val();
    }
    field007RowsCount = jq("div#Control_Field_007_Collection_disclosureContent table tbody tr").length;
    for(var count = 0; count < field007RowsCount; count++){
        field007Value = jq("input#Control_Field_007_line"+count+"_control").val();
    }

    //HOlding's Extend of Owmership
    extOwnerAddOrRemoveCount = jq("#OleExtentOfOwnershipSection_disclosureContent table tbody tr").length;
    //HOlding's Extend of Info
    extOfInfoRowsCount = jq("div#OleExtendedInformation_disclosureContent table tbody tr").length;
    //HOlding's note
    holdingsNoteRowsCount = jq("div#OleHoldingNotes_disclosureContent table tbody tr").length;
    //Item's note
    itemExtInfoRowCount = jq("div#OleNotes table tbody tr").length;
    //Item's Donor
    itemDonorRowCount = jq("div#OleDonorInformation-ListOfDonors table tbody tr ").length;
    deleteItemDonor();

    //Eholding's Section
    coverageSectionRowCount = jq("div#OleExtentOfOwnershipDetails-CoverageSection table tbody tr").length;
    perpetualAccessSectionRowCount = jq("div#OleExtentOfOwnershipDetails-PerpetualAccessSection table tbody tr").length;
    eHoldingDonorRowCount = jq("div#OleEInstanceDonorInformation-ListOfDonors table tbody tr").length;
    eHoldingNotesRowCount = jq("div#OleEInstanceHoldingNotes_disclosureContent table tbody tr").length;
    deleteEHoldingsDonor();


    confirmationOfSavingRecord();
    var control006Val = jq("#Control_Field_006_line0_control").val();
    if (control006Val != "" && control006Val != null) {
        var controlField006Format = control006Val.substring(0, 1);
        jq("#Control_Field_001_h0").val(controlField006Format);
    }
    var control007Val = jq("#Control_Field_007_line0_control").val();
    if (control007Val != "" && control007Val != null) {
        var controlField007Format = control007Val.substring(0, 1);

        jq("#Control_Field_001_h3").val(controlField007Format);
    }
    if (jq("#Control_Field_008_control").val() != null && jq("#Control_Field_008_control").val() != "") {
        var numericDateEnterOnfile = jq("#Control_Field_008_control").val().substring(0, 6);
       if(!jq.isNumeric(numericDateEnterOnfile)) {
           dateEnterOnfile = jq("#Control_Field_008_h0").val();
       }else{
           dateEnterOnfile = numericDateEnterOnfile;
       }

    }

    if (jq("#updatedDate_h0").val() == "false") {
        myConfirm1('Record has  been updated by another user. Do you want to overwrite?', function () {
                jq("#updatedDate_h0").val("true");
                jq("#hdnCheckOverwriteFlag_control").val("overwrite");
                submitForm('save', null, null, true, function () {
                    jq("#updatedDate_h0").val(" ");
                    wrapEnterText();
                });
            }, function () {
                jq("#updatedDate_h0").val("false");
                submitForm('load', null, null, true, function () {
                    jq("#updatedDate_h0").val(" ");
                    wrapEnterText();
                });
            },
            'Update alert'
        );
    }

});

//for confirmation screen
function myConfirm(dialogText, okFunc, cancelFunc, dialogTitle) {
    jq('<div style="padding: 10px; max-width: 500px; word-wrap: break-word;">' + dialogText + '</div>').dialog({
        draggable: false,
        modal: true,
        resizable: false,
        width: 'auto',
        title: dialogTitle || 'Confirm',
        minHeight: 75,
        buttons: {
            OK: function () {
                if (typeof (okFunc) == 'function') {
                    setTimeout(okFunc, 50);
                }
                jq(this).dialog('destroy');
            },
            Cancel: function () {
                if (typeof (cancelFunc) == 'function') {
                    setTimeout(cancelFunc, 50);
                }
                jq(this).dialog('destroy');
            }
        }
    });
}

//for confirmation screen
function myConfirm1(dialogText, okFunc, cancelFunc, dialogTitle) {
    jq('<div style="padding: 10px; max-width: 500px; word-wrap: break-word;">' + dialogText + '</div>').dialog({
        draggable: false,
        modal: true,
        resizable: false,
        width: 'auto',
        title: dialogTitle || 'Confirm',
        minHeight: 75,
        buttons: {
            Overwrite: function () {
                if (typeof (okFunc) == 'function') {
                    setTimeout(okFunc, 50);
                }
                jq(this).dialog('destroy');
            },
            Discard: function () {
                if (typeof (cancelFunc) == 'function') {
                    setTimeout(cancelFunc, 50);
                }
                jq(this).dialog('destroy');
            }
        }
    });
}

//for confirmation screen
function myConfirm2(dialogText, okFunc, cancelFunc, dialogTitle) {
    var href = ""
    jq('<div style="padding: 10px; max-width: 500px; word-wrap: break-word;">' + dialogText + '</div>').dialog({
        draggable: false,
        modal: true,
        resizable: false,
        width: 'auto',
        title: dialogTitle || 'Confirm',
        minHeight: 75,
        buttons: {
            "Save and Close": function () {
                if (typeof (okFunc) == 'function') {
                    setTimeout(okFunc, 50);
                }
                jq(this).dialog('destroy');
            },
            Cancel: function () {
                if (typeof (cancelFunc) == 'function') {
                    setTimeout(cancelFunc, 50);
                }
                jq(this).dialog('destroy');
            }
        }

    });

}


function positionChange() {
    positionChangeStr = "true";
}

//Leader Set
function leaderSet() {
    change008 = true;
    var text008 = jq("#Control_Field_008_control").val();
    if (positionChangeStr == "true") {

        myConfirm('Leader tag position 6 or 7 has changed.This will effect 008 data elements. Do you want change to Leader and 008?', function () {
                jq("#Control_Field_008_control").val("");
                controlField008setDepending06and07();
                change008 = true;
            }, function () {
                change008 = false;
                leaderDropDownSetting();
            },
            'Please review 008 field.'
        );
        positionChangeStr = "false"
        positionStr == "true";
    }
    if (change008) {
        var length = jq('#Length_control').val();
        var recStatus = jq("#RecordStatus_control").val();
        var type = jq("#Type_control").val();
        var level = jq("#Level_control").val();
        var control = jq("#Control_control").val();
        var coding = jq('#coding_control').val();
        var indicatorCount = jq('#indicatorCount_control').val();
        var subfieldCount = jq('#subfieldCount_control').val();
        var baseAddress = jq('#baseAddress_control').val();
        var elvlControl = jq("#Elvl_control").val();
        var descCatalogForm = jq("#DescCatalogForm_control").val();
        var multipartRecLevel = jq("#MultipartRecLevel_control").val();
        var lengthOfLengthOfField = jq("#LengthOfField_control").val();
        var lengthOfStartingCharacterPosition = jq("#StartingCharacterPos_control").val();
        var lengthOfImplementationDefined = jq("#ImplDefined_control").val();
        var undefined = jq("#undefinedLeader_control").val();

        var leaderText = length + recStatus + type + level + control + coding + indicatorCount + subfieldCount + baseAddress
            + elvlControl + descCatalogForm + multipartRecLevel + lengthOfLengthOfField
            + lengthOfStartingCharacterPosition + lengthOfImplementationDefined + undefined;

        jq("#LeaderTextInputField_control").val(leaderText);
    }
    positionChangeStr = "false";
}
function changed() {
    var controlField006Format = jq("#006Field_0_control").val();
    showControlFields006(controlField006Format);
}
function leaderReset() {
    leaderSet();
}
function leaderDropDownSetting() {
    var leaderText = jq("#LeaderTextInputField_control").val();
    if (leaderText != "" && leaderText != null) {
        jq("#Length_control").val(leaderText.substring(0, 5))
        jq("#RecordStatus_control").val(leaderText.substring(5, 6))
        jq("#Type_control").val(leaderText.substring(6, 7))
        jq("#Level_control").val(leaderText.substring(7, 8));
        jq("#Control_control").val(leaderText.substring(8, 9));
        jq("#coding_control").val(leaderText.substring(9, 10));
        jq('#indicatorCount_control').val(leaderText.substring(10, 11));
        jq('#subfieldCount_control').val(leaderText.substring(11, 12));
        jq('#baseAddress_control').val(leaderText.substring(12, 17));
        jq("#Elvl_control").val(leaderText.substring(17, 18));
        jq("#DescCatalogForm_control").val(leaderText.substring(18, 19));
        jq("#MultipartRecLevel_control").val(leaderText.substring(19, 20));
        jq("#LengthOfField_control").val(leaderText.substring(20, 21));
        jq("#StartingCharacterPos_control").val(leaderText.substring(21, 22));
        jq("#ImplDefined_control").val(leaderText.substring(22, 23));
        jq("#undefinedLeader_control").val(leaderText.substring(23));
        setControl008Position();
    }
}


