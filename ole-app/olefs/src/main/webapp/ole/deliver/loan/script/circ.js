function openLightboxOnLoad(dialogId) {
    showLightboxComponent(dialogId, {closeBtn:false});
    jQuery('.uif-dialogButtons').button();
}

function toggleCurrentLoanSection(){
    jq( "#currentLoanListSection-HorizontalBoxSection" ).click(function() {
        jq( "#currentLoanList-HorizontalBoxSection" ).toggle();
    });
}

function toggleExistingLoanSection(){
    jq( "#existingLoanItemListSection-HorizontalBoxSection" ).click(function() {
        jq( "#existingLoanItemList-HorizontalBoxSection" ).toggle();
    });
}

function openLightboxOnLoadWithOverrideParameters(dialogId,overrideParameters) {
    showLightboxComponent(dialogId, overrideParameters);
    jQuery('.uif-dialogButtons').button();
    if(dialogId == 'checkinMissingPieceDialog'){
        document.getElementById('checkinMissingPieceDialog').style='height:180px;overflow-y:scroll';
    }
}

function openLightboxUrl(url) {
    showLightboxUrl(url, {closeBtn:false});
    jQuery('.uif-dialogButtons').button();
}

function printSlip(content){
    var printWindow = window.open('', '', 'scrollbars=yes, height=600,width=600');
    printWindow.document.write('<html><head>');
    printWindow.document.write('</head><body >');
    printWindow.document.write(content);
    printWindow.document.write('</body></html>');
    printWindow.document.close();
    printWindow.print();
}


function alterDueDate(){
    var alterDueDateObjects = [];
    var divLength= jq('#alterDueDateDialog tbody tr').length;
    for(var count=0;count<divLength;count++){
        var itemBarcode=  jq('#alterDueDateDialog tbody tr div#alterDueDate-Barcode_line'+count+' a.uif-link').text();
        var dueDate=  jq('#alterDueDate-dueDate_line'+count+'_control').val();
        var dueTime=  jq('#alterDueDate-time_line'+count+'_control').val();
        var alterDueDateObject = {};
        alterDueDateObject['itemBarcode'] = itemBarcode;
        alterDueDateObject['date'] = dueDate;
        alterDueDateObject['dateTime'] = dueTime;
        alterDueDateObjects.push(alterDueDateObject);
    }
    jq.fancybox.close();
    submitForm('alterDueDate',{alterDueDateObjectValues:JSON.stringify(alterDueDateObjects)},null,true,function(){
        jq('#checkoutItem_control').focus();
    });
}

jq("#pageSizeProxy").live("change", function () {
    var pageSize = jq("#pageSizeProxy").val();
      retrieveComponent('proxyForPatronList-HorizontalBoxSection', "newSizeCheckoutDialog", null, {
        "pageSize":pageSize
    }, true);

})

jq("#existingLoanItemTable_length").live("change", function () {
    var pageSize = jq("#existingLoanItemTable_length").val();
    retrieveComponent('existingLoanItemListSection-HorizontalBoxSection', "newSize", function(){
        destroyDataTableForExistingLoanAndCreateNewDataTable();
    } , {
        "pageSize":pageSize
    }, true);

})

jq(".patronCheckBoxListClass").live("click", function () {
    jq('input:checkbox').click(function () {
        jq('input:checkbox').not(this).removeAttr('checked');
    });
})



function oleCircPager(linkElement, collectionId) {
    var link = jQuery(linkElement);
    if (link.parent().is(kradVariables.ACTIVE_CLASS)) return;
    retrieveComponent(collectionId, "newPage", function(){
        destroyDataTableForExistingLoanAndCreateNewDataTable();
    }, {
        "pageNumber": link.data(kradVariables.PAGE_NUMBER_DATA)
    }, true);
}

function proxyListCheckoutDialog(linkElement, collectionId) {
    var link = jQuery(linkElement);
    if (link.parent().is(kradVariables.ACTIVE_CLASS)) return;
    retrieveComponent(collectionId, "newPageCheckoutDialog", null, {
        "pageNumber": link.data(kradVariables.PAGE_NUMBER_DATA)
    }, true);
}

function renewOverride(){

    if(!jq(".renewItemCBClass:checked").length == 0){
        var renewObjects = [];
        var divLength= jq('#renewOverrideDialog tbody tr').length;
        for(var count=0;count<divLength;count++){
            var itemBarcode=  jq('#renewOverrideDialog tbody tr div#circ_renewBarcodeRenewal_line'+count+' a.uif-link').text();
            var isChecked=  jq('#renewOverrideDialog tbody tr input#circ_checkIdRenew_line'+count+'_control').is(':checked');
            var dueDate=  jq('#circ_renewDueDateForNonCirculatingItem_Date_line'+count+'_control').val();

            if(isChecked === true){
                var renewObject = {};
                renewObject['itemBarcode'] = itemBarcode;
                renewObject['isChecked'] = isChecked;
                if(!(dueDate === null || dueDate === undefined)){
                    var dueTime=  jq('#circ_renewDueDateForNonCirculatingItem_Time_line'+count+'_control').val();
                    renewObject['date'] = dueDate;
                    renewObject['dateTime'] = dueTime;
                }
                renewObjects.push(renewObject);
            }

        }
        submitForm('overrideRenewItems',{renewObjectValues:JSON.stringify(renewObjects)},null,null,null);
    }else{
        jq('#circ_renew_message').attr('style','display:inline');
    }

}

jq("input#checkInMissingPiece-count_control").live('keydown',function(event) {
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

var screenIdleTime = 0;
jq(document).ready(function () {
    var screenIdleInterval = setInterval(function () {
        var screenTimeoutCount =jq("#maxSessionTime_control").val();
        screenIdleTime = screenIdleTime + 1;
        if (parseInt(screenIdleTime) > parseInt(screenTimeoutCount)) {
            clearInterval(screenIdleInterval);
            screenIdleTime = 0;
            submitForm('clearSession', null, null, null, null)
        }
    }, 60000);
    jq("input:text").live("click", function () {
        if (jq(this).attr("id") == undefined) {
            if (jq(this).parent().parent().attr("class") == "dataTables_filter") {
                jq(this).attr("id", "dataTextSearchBox");
            }
        }
    });
});

function setTimeoutInterval(interval){
    screenTimeoutCount = interval;
}

jq(window).load(function () {
    if (jq.trim(jq('#barcodeFieldSection_control').val()) === '') {
        jq("#barcodeFieldSection_control").focus();
    }
});

function validateCheckInDate() {
    var checkInDate = new Date(jq("#checkinCustomDueDate_control").val());
    jq('#alertBoxSectionForCheckinCustomDueDate').attr('style', 'display:none');
    if (!jq.trim(checkInDate) == "") {
        if (jq("#checkinCustomDueDate_control").valid()) {
            var currentDate = new Date();
            currentDate.setMilliseconds(0);
            currentDate.setSeconds(0);
            currentDate.setMinutes(0);
            currentDate.setHours(0);
            if (currentDate > checkInDate) {
                jq('#alertBoxSectionForCheckinCustomDueDate').attr('style', 'display:inline');
            } else if (currentDate < checkInDate) {
                jq("#checkinCustomDueDate_control").val('');
            }
        }
    }
    validateCheckInTime();
}

function validateCheckInTime() {
    var checkInTime = jq('#checkinCustomDueDateTime_control').val();
    jq("div#alertBoxSectionForCheckinCustomDueDateTime").attr('style', 'display:none');
    if (!jq.trim(checkInTime) == "") {
        if (jq("#checkinCustomDueDateTime_control").valid()) {
            var currentDate = new Date();
            var end = currentDate.getHours() + ":" + currentDate.getMinutes();
            var start = checkInTime;
            s = start.split(':');
            e = end.split(':');
            min = e[1] - s[1];
            hour = e[0] - s[0];
            if (min != 0 || hour != 0) {
                jq("div#alertBoxSectionForCheckinCustomDueDateTime").attr('style', 'display:inline');
            }
        }
    }
}

function populateDueDateForAlterDueDateDialog(jsonContentForAlterDueDateDialog){
    var divLength= jq('#alterDueDateDialog tbody tr').length;
    for(var count=0;count<divLength;count++){
        var itemBarcode=  jq('#alterDueDateDialog tbody tr div#alterDueDate-Barcode_line'+count+' a.uif-link').text();
        jQuery.each(jsonContentForAlterDueDateDialog ,	function(index, value){
            if(itemBarcode === value["itemBarcode"]){
                jq('#alterDueDate-dueDate_line'+count+'_control').val(value['date']);
                jq('#alterDueDate-time_line'+count+'_control').val(value['dateTime']);
            }
        });
    }
}

function enableDataTableForExistingLoanedItem(){
    var pageSize = jq("#existingLoanItemTable_length").val();
    jq('#existingLoanItemTable').DataTable( {
        "bLengthChange": false,
        "iDisplayLength" : pageSize
    } );
}

function destroyDataTableForExistingLoanAndCreateNewDataTable(){
    jq('#existingLoanItemTable').dataTable().fnDestroy();
    enableDataTableForExistingLoanedItem();
}

window.onload=enableDataTableForExistingLoanedItem