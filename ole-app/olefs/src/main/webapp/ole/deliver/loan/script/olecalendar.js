/**
 * Created with IntelliJ IDEA.
 * User: dileepp
 * Date: 9/25/13
 * Time: 6:40 PM
 * To change this template use File | Settings | File Templates.
 */


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

function endDate(){
    if(jq("#messageId_control").val()=="This Calendar End-Date cannot be edited"){
        displayDialogWindow("div#EndDatePopUp" );
    } else {
        if(jq("#HdnEndDateYesFlag_control").val() == "false" && jq("#HdnEndDateNoFlag_control").val() == "false") {
            jq("#RouteCalendarBtn").focus().click();
        }
        else{
            displayDialogWindow("div#CalenderEndDate" );
        }
    }
}

function cancelOperation(){
    if(jq("#HdnCancelOperation_control").val()=="true") {
        jq("#HdnCancelOperation_control").val("false");
        jq('#mask').fadeOut(300);
    }


}

function cancelOperationEndDate() {
    if(jq("#HdnCancelOperationEndDate_control").val()=="true") {
        jq("#HdnCancelOperationEndDate_control").val("false");
        jq('#mask').fadeOut(300);
    }
}
