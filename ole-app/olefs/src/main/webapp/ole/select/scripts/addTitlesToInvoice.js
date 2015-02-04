

jq(document).ready(function(){
    showCancelDialog();
    showReceiveAndPayDialog();
});
function showCancelDialog(){
    if(jq("#showCancelDialog_control").val()=='true'){
        jq( "#showChangeLocationDialog_control").val(false);
        displayDialogWindow("div#OLEAddTitlesToInvoiceView-cancelSection");

    }
}
function closeCancelDialog(){
    jq("div#OLEAddTitlesToInvoiceView-cancelSection").fadeOut(300);
    jq('#mask').fadeOut(300);
    jq("#showCancelDialog_control").val("false");
}
function redirect(){

    window.location ="../oleReceivingQueueSearch.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_QUEUESEARCH";
    /*self.parent.location ="../oleReceivingQueueSearch.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_QUEUESEARCH";*/
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

function showReceiveAndPayDialog(){
    if(jq("input#continueReceiveAndPayDialog_control").val()=='true'){
        displayDialogWindow("div#OLEAddTitlesToInvoiceView-continueReceiveAndPayDialog");
        jq( "input#continueReceiveAndPayDialog_control").val('false');
    } else {
        jq("div#OLEAddTitlesToInvoiceView-continueReceiveAndPayDialog").fadeOut(300);
        jq('#mask').fadeOut(300);
        jq( "input#continueReceiveAndPayDialog_control").val('false');
    }
}
function closeReceiveAndPayDialog(){
    jq("div#OLEAddTitlesToInvoiceView-continueReceiveAndPayDialog").fadeOut(300);
    jq('#mask').fadeOut(300);
    jq( "input#continueReceiveAndPayDialog_control").val('false');
    jq( "input#skipValidation_control").val('false');
}


