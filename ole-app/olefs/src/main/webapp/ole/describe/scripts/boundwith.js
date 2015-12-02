function showTabs(methodToCall){
    window.setTimeout(function(){
        retrieveComponent('BoundwithSelectionSection', methodToCall, callSuccess, null);
    }, 2000);
    window.setTimeout(function(){
        jq('#rightTreeRefreshButton').focus().click();
        jq('#leftTreeRefreshButton').focus().click();

    }, 2000);

}
function showDeleteTabs(methodToCall){
    window.setTimeout(function(){
        retrieveComponent('BoundwithSelectionSection', methodToCall, callSuccess, null);
    },800);
}
function callSuccess(){
    jq("#BoundwithTreeSection1_tabs").tabs("select", "#BoundwithResultsSection_tab");
}
