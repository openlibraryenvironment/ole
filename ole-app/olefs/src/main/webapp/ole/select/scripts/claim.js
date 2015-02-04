function showCancelDialog(){
    if(jq("#claimingCancelDialog_control").val()=='true'){
        displayDialogWindow("div#OLEClaimingView-cancelSection");

    }
}
function closeCancelDialog(){
    jq("div#OLEClaimingView-cancelSection").fadeOut(300);
    jq('#mask').fadeOut(300);
    jq("#claimingCancelDialog_control").val("false");
}
function redirect(){

    window.location ="../oleReceivingQueueSearch.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_QUEUESEARCH";
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


