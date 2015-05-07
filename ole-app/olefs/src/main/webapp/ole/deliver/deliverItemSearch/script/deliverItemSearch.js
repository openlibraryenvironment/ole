jq(document).ready(function(){
    if(jq("#itemLocation_line0").val() == undefined && jq("#location_line0").val() == undefined ){
        jq("#OLEDeliverItemSearchResult-AdditionalCopies span").css("color", "#808080");
    }

    if(jq("#itemLocation_line0").val() == undefined && jq("#location_line0").val() == undefined ) {
        jq("#OLEDeliverItemSearchResult-ItemFlags span").css("color", "#808080");
    }

    if(jq("#noteType_line0").val() == undefined){
        jq("#OLEDeliverItemSearchResult-Notes span").css("color", "#808080");
    }

    if(jq("#queuePositionItemPlaceRequest_line0").val() == undefined) {
        jq("#OLEDeliverItemSearchResult-Request span").css("color", "#808080");
    }

    if(jq("#firstNameFinePlaceRequest_line0").val() == undefined) {
        jq("#OLEDeliverItemSearchResult-OutstandingFines span").css("color", "#808080");
    }
})

function collapseAll(){

    if(jq( "#OLEDeliverItemSearchResult-Borrower_disclosureContent").attr('data-open') == 'true'){

        jq( "#OLEDeliverItemSearchResult-Borrower_toggle").focus().click();
    }

    if(jq( "#OLEDeliverItemSearchResult-AdditionalCopies_disclosureContent").attr('data-open') == 'true'){

        jq( "#OLEDeliverItemSearchResult-AdditionalCopies_toggle").focus().click();
    }


    if(jq( "#OLEDeliverItemSearchResult-Request_disclosureContent").attr('data-open') == 'true'){

        jq( "#OLEDeliverItemSearchResult-Request_toggle").focus().click();
    }


    if(jq( "#OLEDeliverItemSearchResult-ItemFlags_disclosureContent").attr('data-open') == 'true'){

        jq( "#OLEDeliverItemSearchResult-ItemFlags_toggle").focus().click();
    }


    if(jq( "#OLEDeliverItemSearchResult-Notes_disclosureContent").attr('data-open') == 'true'){
        jq( "#OLEDeliverItemSearchResult-Notes_toggle").focus().click();
    }


    if(jq( "#OLEDeliverItemSearchResult-OutstandingFines_disclosureContent").attr('data-open') == 'true'){
        jq( "#OLEDeliverItemSearchResult-OutstandingFines_toggle").focus().click();
    }


    if(jq( "#OLEDeliverItemSearchResult-IDNumbers_disclosureContent").attr('data-open') == 'true'){
        jq( "#OLEDeliverItemSearchResult-IDNumbers_toggle").focus().click();
    }

    if(jq( "#OLEDeliverItemSearchResult-ItemHistory_disclosureContent").attr('data-open') == 'true'){
        jq( "#OLEDeliverItemSearchResult-ItemHistory_toggle").focus().click();
    }



}



function expandAll(){

    if(jq( "#OLEDeliverItemSearchResult-Borrower_disclosureContent").attr('data-open') == 'false'){

        jq( "#OLEDeliverItemSearchResult-Borrower_toggle").focus().click();
    }

    if(jq( "#OLEDeliverItemSearchResult-AdditionalCopies_disclosureContent").attr('data-open') == 'false'){

        jq( "#OLEDeliverItemSearchResult-AdditionalCopies_toggle").focus().click();
    }


    if(jq( "#OLEDeliverItemSearchResult-Request_disclosureContent").attr('data-open') == 'false'){

        jq( "#OLEDeliverItemSearchResult-Request_toggle").focus().click();
    }


    if(jq( "#OLEDeliverItemSearchResult-ItemFlags_disclosureContent").attr('data-open') == 'false'){

        jq( "#OLEDeliverItemSearchResult-ItemFlags_toggle").focus().click();
    }


    if(jq( "#OLEDeliverItemSearchResult-Notes_disclosureContent").attr('data-open') == 'false'){
        jq( "#OLEDeliverItemSearchResult-Notes_toggle").focus().click();
    }


    if(jq( "#OLEDeliverItemSearchResult-OutstandingFines_disclosureContent").attr('data-open') == 'false'){
        jq( "#OLEDeliverItemSearchResult-OutstandingFines_toggle").focus().click();
    }


    if(jq( "#OLEDeliverItemSearchResult-IDNumbers_disclosureContent").attr('data-open') == 'false'){
        jq( "#OLEDeliverItemSearchResult-IDNumbers_toggle").focus().click();
    }

    if(jq( "#OLEDeliverItemSearchResult-ItemHistory_disclosureContent").attr('data-open') == 'false'){
        jq( "#OLEDeliverItemSearchResult-ItemHistory_toggle").focus().click();
    }



}


function disableLink(){
    if(jq("#itemLocation_line0").val() == undefined && jq("#location_line0").val() == undefined ){
        jq("#OLEDeliverItemSearchResult-AdditionalCopies span").css("color", "#808080");
    }

    if(jq("#itemLocation_line0").val() == undefined && jq("#location_line0").val() == undefined ) {
        jq("#OLEDeliverItemSearchResult-ItemFlags span").css("color", "#808080");
    }

    if(jq("#noteType_line0").val() == undefined){
        jq("#OLEDeliverItemSearchResult-Notes span").css("color", "#808080");
    }

    if(jq("#queuePositionItemPlaceRequest_line0").val() == undefined) {
        jq("#OLEDeliverItemSearchResult-Request span").css("color", "#808080");
    }

    if(jq("#firstNameFinePlaceRequest_line0").val() == undefined) {
        jq("#OLEDeliverItemSearchResult-OutstandingFines span").css("color", "#808080");
    }
}