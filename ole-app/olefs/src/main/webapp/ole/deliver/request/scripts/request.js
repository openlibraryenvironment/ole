jq(document).ready(function(){
    draggable();
    refreshPageUser();
    jq(document).keypress(function(e) {
            if(e.which == 13) {
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


});






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