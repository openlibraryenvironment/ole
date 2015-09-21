jq(document).ready(function(){
    showRequestErrorMessage();
    draggable();
    refreshPageUser();
    jq(document).keypress(function(e) {
            if(e.which == 13) {
                if(e.target.id == "pl_req_pat_control"){
                    if(jq( "#pl_req_pat_control").val()!=""){
                        jq(this).blur();
                        jq('#fetchPatronAddressButton').focus().click();
                    }else{
                        jq("#pl_req_pat_control").focus();
                        return false;
                    }
                }
                if(e.target.id == "itemIdSearch_control"){
                    if(jq( "#itemIdSearch_control").val()!=""){
                        jq(this).blur();
                        jq('#searchButton').focus().click();
                    }else{
                        jq("#itemIdSearch_control").focus();
                        return false;
                    }
                }
                else if(e.target.id == "_control"){
                    if(jq( "#TransitRequest-itemId_control").val()!=""){
                        jq(this).blur();
                        jq('#searchButtonForTransitRequest').focus().click();
                    }else{
                        jq("#TransitRequest-itemId_control").focus();
                        return false;
                    }

                }
                else if(e.target.id == "_control"){
                    if(jq( "#CopyRequest-itemId_control").val()!=""){
                        jq(this).blur();
                        jq('#searchButtonForCopyRequest').focus().click();
                    }else{
                        jq("#CopyRequest-itemId_control").focus();
                        return false;
                    }

                }
                else if(e.target.id == "_control"){
                    if(jq( "#PageRequest-itemId_control").val()!=""){
                        jq(this).blur();
                        jq('#searchButtonForPageRequest').focus().click();
                    }else{
                        jq("#PageRequest-itemId_control").focus();
                        return false;
                    }
                }
                else if(e.target.id == "_control"){
                    if(jq( "#OnholdRequest-itemId_control").val()!=""){
                        jq(this).blur();
                        jq('#searchButtonForOnholdRequest').focus().click();
                    }else{
                        jq("#OnholdRequest-itemId_control").focus();
                        return false;
                    }
                }
                else if(e.target.id == "_control"){
                    if(jq( "#RecallRequest-itemId_control").val()!=""){
                        jq(this).blur();
                        jq('#searchButtonForRecallRequest').focus().click();
                    }else{
                        jq("#RecallRequest-itemId_control").focus();
                        return false;
                    }
                }
                else if(e.target.id == "selectRequest-MaintenanceViews-borrowerBarcodes_control"){
                    if(jq( "#selectRequest-MaintenanceViews-borrowerBarcodes_control").val()!=""){
                        jq(this).blur();
                        jq('#searchButtonSelectRequest').focus().click();
                    }else{
                        jq("#selectRequest-MaintenanceViews-borrowerBarcodes_control").focus();
                        return false;
                    }
                }
                else if(e.target.id == "selectRequest-MaintenanceView-borrowerBarcodes_control"){
                    if(jq( "#selectRequest-MaintenanceView-borrowerBarcodes_control").val()!=""){
                        jq(this).blur();
                        jq('#searchButtonSelectRequests').focus().click();
                    }else{
                        jq("#selectRequest-MaintenanceView-borrowerBarcodes_control").focus();
                        return false;
                    }
                }
                else if(e.target.id == "selectRequestBorrower-MaintenanceView-borrowerBarcodes_control"){
                    if(jq( "#selectRequestBorrower-MaintenanceView-borrowerBarcodes_control").val()!=""){
                        jq(this).blur();
                        jq('#searchButtonSelectRequestBorrower').focus().click();
                    }else{
                        jq("#selectRequestBorrower-MaintenanceView-borrowerBarcodes_control").focus();
                        return false;
                    }
                }
                else if(e.target.id == "selectRequestOperator-MaintenanceView-borrowerBarcodes_control"){
                    if(jq( "#selectRequestOperator-MaintenanceView-borrowerBarcodes_control").val()!=""){
                        jq(this).blur();
                        jq('#searchButtonSelectRequestOperator').focus().click();
                    }else{
                        jq("#selectRequestOperator-MaintenanceView-borrowerBarcodes_control").focus();
                        return false;
                    }
                }else if(e.target.id == "selectReque-proxyBorrowerBarcodes_control"){
                    if(jq( "#selectReque-proxyBorrowerBarcodes_control").val()!=""){
                        jq(this).blur();
                        jq('#searchButtonSelectRequests').focus().click();
                    }else{
                        jq("#selectReque-proxyBorrowerBarcodes_control").focus();
                        return false;
                    }
                }
                return false;
            }

        }


    );


    jq('#placeRequest_deliver_control').live("change",function(){
        jq('select#placeRequest-pickupLocation_control option[value=""]').attr("selected",true);
    });

    jq('#placeRequest-pickupLocation_control').live("change",function(){
        jq('#placeRequest_deliver_control').removeAttr('checked');
    });


    jq('#pageRequestTyp_control_0').live("click",function(){

        jq('#pageRequestTyp_control_0').attr("checked","true");
        jq('#copyRequestTyp_control_0').removeAttr('checked');
        jq('#recallRequestTyp_control_0').removeAttr('checked');
        jq('#holdRequestTyp_control_0').removeAttr('checked');
        jq('#asrRequestTyp_control_0').removeAttr('checked');
    });
    jq('#copyRequestTyp_control_0').live("click",function(){

        jq('#copyRequestTyp_control_0').attr("checked","true");
        jq('#pageRequestTyp_control_0').removeAttr('checked');
        jq('#recallRequestTyp_control_0').removeAttr('checked');
        jq('#holdRequestTyp_control_0').removeAttr('checked');
        jq('#asrRequestTyp_control_0').removeAttr('checked');
    });
    jq('#recallRequestTyp_control_0').live("click",function(){

        jq('#recallRequestTyp_control_0').attr("checked","true");
        jq('#pageRequestTyp_control_0').removeAttr('checked');
        jq('#copyRequestTyp_control_0').removeAttr('checked');
        jq('#holdRequestTyp_control_0').removeAttr('checked');
        jq('#asrRequestTyp_control_0').removeAttr('checked');
    });

    jq('#holdRequestTyp_control_0').live("click",function(){

        jq('#holdRequestTyp_control_0').attr("checked","true");
        jq('#pageRequestTyp_control_0').removeAttr('checked');
        jq('#copyRequestTyp_control_0').removeAttr('checked');
        jq('#recallRequestTyp_control_0').removeAttr('checked');
        jq('#asrRequestTyp_control_0').removeAttr('checked');
    });

    jq('#asrRequestTyp_control_0').live("click",function(){

        jq('#asrRequestTyp_control_0').attr("checked","true");
        jq('#pageRequestTyp_control_0').removeAttr('checked');
        jq('#copyRequestTyp_control_0').removeAttr('checked');
        jq('#holdRequestTyp_control_0').removeAttr('checked');
        jq('#recallRequestTyp_control_0').removeAttr('checked');
    });

    jq('#titleLevelRequest_control_0').live("click",function(){
        jq('#titleLevelRequest_control_0').attr("checked","true");
        jq('#itemLevelRequest_control_0').removeAttr('checked');
        jq('#item_level_request_button').focus().click();
    });

    jq('#itemLevelRequest_control_0').live("click",function(){
        jq('#itemLevelRequest_control_0').attr("checked","true");
        if(jq('#titleLevelRequest_control_0').val()=="true"){
        jq('#titleLevelRequest_control_0').removeAttr('checked');
        }
        jq('#item_level_request_Clear_button').focus().click();
    });



});

jq(window).load(function() {
    if(jq("#hiddenRequestTypePopulated_control").val() == "true") {
        if(jq("#selectRequestBorrower-MaintenanceView-requestTypeIds_control").val() !== null && jq("#selectRequestBorrower-MaintenanceView-requestTypeIds_control").val() !== undefined && jq("#selectRequestBorrower-MaintenanceView-requestTypeIds_control").val() !== '') {
            submitForm('refreshPageView', null, null, null, null);
        } else if(jq("#selectRequest-MaintenanceView-requestTypeIds_control").val() !== null && jq("#selectRequest-MaintenanceView-requestTypeIds_control").val() !== undefined && jq("#selectRequest-MaintenanceView-requestTypeIds_control").val() != '') {
            submitForm('refreshPageView', null, null, null, null);
        } else if(jq("#selectRequestOperator-MaintenanceView-requestTypeIds_control").val() !== null && jq("#selectRequestOperator-MaintenanceView-requestTypeIds_control").val() !== undefined && jq("#selectRequestOperator-MaintenanceView-requestTypeIds_control").val() != '') {
            submitForm('refreshPageView', null, null, null, null);
        }
    }
});


/*

function displayDialogWindow(divID){
    jq(divID).fadeIn(300);
    jq(divID).fadeIn(300);
    var popMargTop = (jq(divID).height() + 24) / 2;
    var popMargLeft = (jq(divID).width() + 24) / 2;
    var left=(jq(document).width()/2)-(jq(divID).width()/2)
    var top='300px';

    if(jq(divID).height()>300){
        top='250px';
    }
    if(jq(divID).height()>500){
        top='210px';
    }
    if(jq(divID).height()<300){
        top='100px';
    }
    jq(divID).css({
        */
/* 'margin-top' : '100px',
         'margin-left' : '100px',*//*

        'top': top,
        'left':left+"px",
        'position':'fixed',
        'align':'center'
    })
    jq(divID).draggable();
    jq('body').append('<div id="mask"></div>');
    jq('#mask').fadeIn(300);
    jq('body').scrollTop(0);
    jq(window.parent).scrollTop(0);
}
*/


function draggable(){
    jq("#RequestSearchSection-HorizontalBoxSection > table > tbody").addClass("draggable");
    jq("#RequestSearchSection-HorizontalBoxSection > table > tbody").addClass("sortable");
    jq("#RequestSearchSection-HorizontalBoxSection > span").html("");
    jq( ".sortable" ).sortable({
        revert: false,
        update: function(ev, ui) {
            var rows = jq("#RequestSearchSection-HorizontalBoxSection > table > tbody > tr");
            rows.each(function(index) {
                jq(this).removeClass("odd");
                if(index%2!=0){
                    jq(this).addClass("odd");
                }
                jq("td:nth-child(3) input",this).val(index+1);
            });
        }
    });
    jq( ".draggable" ).draggable({
        revert: "invalid"
    });
    jq("#selectRequest-MaintenanceView-requestCreators").live("change",function() {

        jq('#hiddenSelectRequestButton').focus().click();
    });
    jq("#selectRequestProxy-MaintenanceView-requestCreators").live("change",function() {

        jq('#hiddenSelectRequestProxyButton').focus().click();
    });
    jq("#selectRequestBorrower-MaintenanceView-requestCreators").live("change",function() {

        jq('#hiddenSelectRequestBorrowerButton').focus().click();
    });
    jq("#selectRequestOperator-MaintenanceView-requestCreators").live("change",function() {

        jq('#hiddenSelectRequestOperatorButton').focus().click();
    });

    jq("#selectRequest-MaintenanceView-requestTypeId").live("change",function() {


        jq('#hiddenSelectRequestButton').focus().click();
    });
    jq("#selectRequest-MaintenanceView-requestTypeIds").live("change",function() {


        jq('#hiddenSelectRequestProxyButton').focus().click();
    });
    jq("#selectRequestOperator-MaintenanceView-requestTypeIds").live("change",function() {


        jq('#hiddenSelectRequestOperatorButton').focus().click();
    });
    jq("#selectRequestBorrower-MaintenanceView-requestTypeIds").live("change",function() {


        jq('#hiddenSelectRequestBorrowerButton').focus().click();
    });

}


function refreshPageUser(){





}
function printResponseString(){
    if(!(jq("div").hasClass("uif-validationMessages uif-groupValidationMessages uif-pageValidationMessages uif-pageValidationMessages-error"))){
        jq('#hiddenButtonForCreatePdf').focus().click();
    }
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


function showItemsDialog() {
     jq("div#OLEPlaceRequestView_MultipleItem_details").show();
     displayDialogWindow("div#OLEPlaceRequestView_MultipleItem_details");
}

function showRequestErrorMessage(){

    if(jq("#OLEPlaceRequestView_errorMessage").val() != null) {
        jq('body').scrollTop(0);
        jq(window.parent).scrollTop(0);
        displayDialogWindow("div#OLEPlaceRequestView_errorMessage");
        jq('body').scrollTop(0);
        jq(window).scrollTop(0);
    }
}



function cancelOverride(){
    jq( "div#OLEPlaceRequestView_errorMessage" ).fadeOut('fast');
    jq('#mask').fadeOut(300);
}

function closeItemsDialog(){
    jq("div#OLEPlaceRequestView_MultipleItem_details").fadeOut(300);
    jq('#mask').fadeOut(300);
}
function selectAllItem(){
    jq(".selectItemCBClass").attr("checked","true");
}

function deSelectAllItem(){
    jq(".selectItemCBClass").removeAttr("checked");
}

