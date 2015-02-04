+
/**
 * Created with IntelliJ IDEA.
 * User: rajeshbabuk
 * Date: 11/21/13
 * Time: 7:13 PM
 * To change this template use File | Settings | File Templates.
 */
jq(".sorting_asc").live("click", function () {
   /* alert("inside asc"+jq("#pgn_flag_control").val());
    if (jq("#pgn_flag_control").val() == "true") {*/
        jq('#ascendingSort_button').focus().click();
  //  }

});

jq(".sorting_desc").live("click", function () {
   /* alert("inside des"+jq("#pgn_flag_control").val());
    if (jq("#pgn_flag_control").val() == "true") {*/
        jq('#descendingSort_button').focus().click();
   // }
});
jq(document).ready(function () {
    window.onbeforeunload = null;
});
jq(window).load(function () {
    jq(".paginate_enabled_next").live("click", function () {
        localStorage.localAscSort = true;
        localStorage.localDescSort = true;
        jq('#hdn_NextFlag_buttondd').focus().click();
        jq('#hidden_NextFlag_buttondd').focus().click();
    });
    jq(".paginate_enabled_previous").live("click", function () {
        localStorage.localAscSort = true;
        localStorage.localDescSort = true;
        jq('#hdn_PreviousFlag_buttondd').focus().click();
        jq('#hidden_PreviousFlag_buttondd').focus().click();
    });

    jq(".sorting").live("click", function () {
        if (jq("#pgn_flag_control").val() == "true") {
            pageEntriesDisplay();
        }
    });




});


function refreshPage() {
    window.open("serialsReceivingRecordController?viewId=SerialsReceivingRecordSearchView&methodToCall=start", "_self");
}

function pageEntriesDisplay() {
    if (jq("#pgn_flag_control").val() == "true") {
        jq("#pgn_flag_control").val("false");

        if (jq(".dataTables_info").text() != "") {
            jq(".dataTables_info").text(jq("#hdndata_i_control").val());
        }

        if (jq("#searchNextFlagdd_control").val() == "true") {
            jq(".paginate_disabled_next").removeClass("paginate_disabled_next").addClass("paginate_enabled_next");
        }
        if (jq("#searchPreviousFlagdd_control").val() == "true") {
            jq(".paginate_disabled_previous").removeClass("paginate_disabled_previous").addClass("paginate_enabled_previous");
        }
    }
}
function setSearchType(){
    jq("#src_flag_control").val("search");

}