function showPreviousNext(){


    window.setTimeout(function () {


        if (jq("#moreFacetNext_control").val() == "true") {
            jq(".paginate_disabled_next").removeClass("paginate_disabled_next").addClass("paginate_enabled_next");
        }
        else {
            jq(".paginate_enabled_next").removeClass("paginate_enabled_next").addClass("paginate_disabled_next");
        }

        if (jq("#moreFacetPrevious_control").val() == "true") {
            jq(".paginate_disabled_previous").removeClass("paginate_disabled_previous").addClass("paginate_enabled_previous");
        }
        else {
            jq(".paginate_enabled_previous").removeClass("paginate_enabled_previous").addClass("paginate_disabled_previous");
        }
//        jq(".dataTables_info").text(jq("#facetPageShowEntries_control").val());
    }, 500)

}


jq(window).load(function () {
    jq(".paginate_enabled_next").live("click", function () {
        submitForm("nextFacet", null, null, false, null);

    });
    jq(".paginate_enabled_previous").live("click", function () {
        submitForm("previousFacet", null, null, false, null);
    });
    showPreviousNext();


});
