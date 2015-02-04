/*jq(document).ready(function () {
    jq(".defaultClass").click(function () {
        jq(".defaultClass").removeAttr("checked");
        jq("#" + jq(this).attr('id')).attr("checked", "true");

    });

});*/

function showTabs(methodToCall){
    window.setTimeout(function(){
        retrieveComponent('BoundwithSelectionSection', methodToCall, callSuccess, null);
    }, 2000);
    window.setTimeout(function(){
        jq('#rightTreeRefreshButton').focus().click();
        jq('#leftTreeRefreshButton').focus().click();

    }, 2000);
    searchShowEntries();
}

function showDeleteTabs(methodToCall){
    window.setTimeout(function(){
        retrieveComponent('BoundwithSelectionSection', methodToCall, callSuccess, null);
    },800);

    window.setTimeout(function(){
        commonShowEntries();
    },800);



}

function callSuccess(){
    jq("#BoundwithTreeSection1_tabs").tabs("select", "#BoundwithResultsSection_tab");
}
