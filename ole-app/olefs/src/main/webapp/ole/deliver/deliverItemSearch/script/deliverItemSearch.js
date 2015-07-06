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


    jq(".dataTables_length select").live("change", function () {
        var rows = jq(".dataTables_length select").val();
        jq("#hiddenSearchFieldsItem_h0").val(rows); //hiddenSearchFields_h0 is mapped to pageSize
        submitForm('search', null, null, true, function () {

        });
    });

    sessionStorage.setItem("sortOrder", "asc");
    sessionStorage.setItem("field", "title");


    jq(document).keypress(function(e) {
        if(e.which == 13) {
            jq('#itemSearch_search_button').click();
        }
    });

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

function oleItemRowDetails(lineId, collectionId, show) {
    retrieveComponent(collectionId, "refresh", null, {
        "selectRowDetails" : lineId,
        "showSelectedRowDetails" : show
    }, true);
}


function oleItemSearchPager(linkElement, collectionId) {
    var link = jQuery(linkElement);
    if (link.parent().is(kradVariables.ACTIVE_CLASS)) return;
    var pageNumber = link.data(kradVariables.PAGE_NUMBER_DATA);
    if (pageNumber == 'next')
        submitForm('nextSearch', null, null, true, function () {

        });

    else if (pageNumber == 'prev')
        submitForm('previousSearch', null, null, true, function () {

        });
    else if (pageNumber == 'first') {
        jQuery('#hiddenSearchFieldsItem_control').val('1');
        submitForm('pageNumberSearch', null, null, true, function () {

        });
    } else if (pageNumber == 'last')
        submitForm('lastPageSearch', null, null, true, function () {

        });
    else {
        jQuery('#hiddenSearchFieldsItem_control').val(pageNumber);
        submitForm('pageNumberSearch', null, null, true, function () {

        });
    }

}


function sortBy(field) {
    var sortField = "";
    setFieldAndSort(field);
    var sortOrder = sessionStorage.getItem("sortOrder");
    if (field == 'title') {
        sortField = "Title_sort";
    } else if (field == 'author') {
        sortField = "Author_sort "+sortOrder+ ",Title_sort";
    } else if (field == 'pubYear') {
        sortField = "PublicationDate_sort "+sortOrder+ ",Title_sort";
    } else if (field == 'ItemLocation') {
        sortField = "Location_sort "+sortOrder+ ",Title_sort";
    } else if (field == 'callNumber') {
        sortField = "CallNumber_sort "+sortOrder+ ",Title_sort";
    } else if (field == 'status') {
        sortField = "ItemStatus_sort "+sortOrder+ ",Title_sort";
    }

    jq('#hiddenSearchFieldsItem_h2').val(sortField);
    jq('#hiddenSearchFieldsItem_h1').val(sortOrder);
    submitForm('search', null, null, true, function () {
    });

}


function setFieldAndSort(field) {
    if (sessionStorage.getItem("field") == field) {
        if (sessionStorage.getItem("sortOrder") == "asc") {
            sessionStorage.setItem("sortOrder", "desc");
        } else if (sessionStorage.getItem("sortOrder") == "desc") {
            sessionStorage.setItem("sortOrder", "asc");
        }
    } else {
        sessionStorage.setItem("field", field)
        sessionStorage.setItem("sortOrder", "asc");
    }
}



