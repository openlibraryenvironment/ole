jq(".sorting_desc").live("click", function () {
    successMethod();
});

jq(".sorting_asc").live("click", function () {
    successMethod();
});

jq(".sorting").live("click", function () {
    successMethod();
});

jq('#u100006').hide();
jq('#u100007').hide();
jq('#u100009').hide();


jq("#item_search_lookup").live("click", function () {
    if (jq('#pageDisplay_control').val() == "") {
        jq('#pageDisplay_control').val("search");
    }
    if (jq("#pagesz_control").val() == "") {
        jq("#pagesz_control").val("10");
    }


});

jq(".paginate_enabled_previous").live("click", function () {
    localStorage.localAscSort = true;
    localStorage.localDescSort = true;
    jq('#pageDisplay_control').val("previous");


    jq('#item_search_lookup').focus().click();

});

jq(".paginate_enabled_next").live("click", function () {
    localStorage.localAscSort = true;
    localStorage.localDescSort = true;
    jq('#pageDisplay_control').val("next");
    jq('#item_search_lookup').focus().click();
});


jq(".dataTables_length select").live("change", function () {
    jq("#pagesz_control").val(jq(".dataTables_length select").val());
    jq('#item_search_lookup').focus().click();

})


function successMethod() {
    jq('#u100006').hide();
    jq('#u100007').hide();
    jq('#u100009').hide();


    jq('#pageDisplay_control').val("search");
    jq(".dataTables_info").text(jq("#totalRec_control").val());
    if (jq("#nxtflg_control").val() == "true") {
        jq(".paginate_disabled_next").removeClass("paginate_disabled_next").addClass("paginate_enabled_next");
    }
    if (jq("#prvflg_control").val() == "true") {
        jq(".paginate_disabled_previous").removeClass("paginate_disabled_previous").addClass("paginate_enabled_previous");
    }


}






