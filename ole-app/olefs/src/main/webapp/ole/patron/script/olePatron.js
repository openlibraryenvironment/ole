/**
 * Created with IntelliJ IDEA.
 * User: gayathria
 * Date: 9/5/13
 * Time: 6:26 PM
 * To change this template use File | Settings | File Templates.
 */



jq(document).ready(function(){
    jq("#OlePatronDocument-OverviewSection_errors").css("visibility","hidden");
      jq( "#mydiv" ).css( "color", "green" )

      disableContactLink();
   // disableLink();


    showBarcodeUpdateDialogBox();
    if(jq("#showReinstateDialogBox_control").val()=='true'){
        reinstateDialog();
    }
    disableEnableBarcode();
    openPatronMessagePopup();
    jq("#OlePatronDocument-validation-Message-Section").hide();
    jq("#OlePatronDocument-validation-Message-Section-reinstate").hide();

})


function showDetails(){
    jq("#OlePatronDocument-Address_detLink_line0").focus().click();
}

function reinstate() {
    if (jq("#OlePatronDocument-reinstate-reason-note_control").val().length==0) {
        jq("#OlePatronDocument-validation-Message-Section-reinstate").show();
    } else {
        jq("#showReinstateDialogBox_control").val("true");
        jq("div#OlePatronDocument-reinstateBarcode-section").fadeOut(300);
        jq('#mask').fadeOut(300);
        jq("#reinstateBarcodeBtn").focus().click();
    }
}

function confirm() {
    if (jq("#OlePatronDocument-reason-note_control").val().length==0) {
        jq("#OlePatronDocument-validation-Message-Section").show();
    } else {
        jq("div#OlePatronDocument-Barcode-Update-Section").fadeOut(300);
        jq('#mask').fadeOut(300);
        jq("#updateBarcodeBtn").focus().click();
    }
}

function closeUpdateBarcodeDialog(){
    jq("div#OlePatronDocument-Barcode-Update-Section").fadeOut(300);
    jq('#mask').fadeOut(300);
    jq("#showUpdateBarcodeDialogBox_control").val("false");
    jq("#overviewId_edit input#barcode_control").attr("disabled", "disabled");
    jq("#overviewId input#barcode_control").attr("disabled", "disabled");
}

function closeReinstateDialog(){
    jq("div#OlePatronDocument-reinstateBarcode-section").fadeOut(300);
    jq('#mask').fadeOut(300);
    jq("#showReinstateDialogBox_control").val("false");
    jq("button#Refresh-OlePatronDocument-InvalidOrLostBarcodeSection").focus().click();
    jq("#OlePatronDocument-reinstate-reason-note_control").removeClass( "required" )
}

function showBarcodeUpdateDialogBox(){
    if(jq("#showUpdateBarcodeDialogBox_control").val()=='true'){
        jq("#OlePatronDocument-validation-Message-Section").hide();
        displayDialogWindow("div#OlePatronDocument-Barcode-Update-Section");
    }
}

function reinstateDialog(){
    if(jq("#showReinstateDialogBox_control").val()=='false'){
        submitForm('reinstateBarcode', null, null, null, null);
    }
    if(jq("#showReinstateDialogBox_control").val()=='true'){
        jq("#OlePatronDocument-validation-Message-Section-reinstate").hide();
        jq("#showReinstateDialogBox_control").val(false);
        displayDialogWindow("div#OlePatronDocument-reinstateBarcode-section");
    }

}

jq(window).load(function () {
    if(jq("div#OlePatronDocument-LookupView")){
        jq("div#OlePatronDocument-LookupView  #barcode_control").focus();
    }

   // disableLink()
});

function displayDialogWindow(divID){
    jq(divID).fadeIn(300);
    jq(divID).fadeIn(300);
    var popMargTop = (jq(divID).height() + 24) / 2;
    var popMargLeft = (jq(divID).width() + 24) / 2;
    var left=(jq(document).width()/2)-(jq(divID).width()/2)
    var top='300px';

    if(jq(divID).height()>300){
        top='250px';
    }
    if(jq(divID).height()>500){
        top='210px';
    }
    jq(divID).css({
        /* 'margin-top' : '100px',
         'margin-left' : '100px',*/
        'top': top,
        'left':left+"px",
        'position':'fixed',
        'align':'center'
    })
    jq(divID).draggable();
    jq('body').append('<div id="mask"></div>');
    jq('#mask').fadeIn(300);
    jq('body').scrollTop(0);
    jq(window.parent).scrollTop(0);
}

function refreshClaimsReturnNote(){
    jq("#patronLoan_hdnBtn").focus().click();
}

function refreshMissingPieceNote(){
    jq("#patronLoan_hdnBtn").focus().click();
}

function refreshItemDamagedNote(){
    jq("#patronLoan_hdnBtn").focus().click();
}

function disableEnableBarcode(){
    if(jq("input#OlePatronDocument-barcodeEditable_control").val()=='false'){
        jq("#overviewId_edit input#barcode_edit_control").attr('readonly', true);
        jq("#overviewId input#barcode_control").attr('readonly', true);
    }

    if(jq("input#OlePatronDocument-barcodeEditable_control").val()=='true'){
        jq("#overviewId_edit input#barcode_edit_control").removeAttr("readonly");
        jq("#overviewId input#barcode_control").removeAttr("readonly");
    }
}

function openPatronMessagePopup(){

    if(jq("#OlePatronDocument-popupDialog_control").val()=='true'){
        displayDialogWindow("div#OlePatronDocument-messagePopupSection");
    }
}

function closePatronMessagePopup(){

    jq("div#OlePatronDocument-messagePopupSection").fadeOut(300);
    jq('#mask').fadeOut(300);
    jq("#OlePatronDocument-popupDialog_control").val("false");
}
function continueSubmit(){
    jq("div#OlePatronDocument-messagePopupSection").fadeOut(300);
    jq('#mask').fadeOut(300);
    submitForm('route', null, null, null, null);
}


jq("#OlePatronDocument-reason-note_control").live("keypress", function () {
    jq("#OlePatronDocument-validation-Message-Section").hide();

});

jq("#OlePatronDocument-reinstate-reason-note_control").live("keypress", function () {
    jq("#OlePatronDocument-validation-Message-Section-reinstate").hide();

});

function setLoanModified(obj){
    var id=obj.id;
    var lineNumber = id.replace( /^\D+/g,'');
    jq('input#patronLoan_isModified_line'+lineNumber).val('true');
}

function disableContactLink(){
    if(jq("#addressTypeCode_line0").val() == undefined){
        jq("#OlePatronDocument-Address  span").css("color", "#808080");
    }

    if(jq("#phoneTypeCode_line0").val() == undefined){
        jq("#OlePatronDocument-Phone  span").css("color", "#808080");
    }

    if(jq("#emailTypeCode_line0").val() == undefined){
        jq("#OlePatronDocument-Email  span").css("color", "#808080");
    }
}

function disableLink(){
    if(jq("#Show-OlePatronDocument-LoanedItems-TotalNoCount").val() == undefined && jq("#patronLoan_title_line0").val() == undefined ){
        jq("#OlePatronDocument-PatronLoanedRecords-Wrapper span a").css("color", "#808080");
    }

    if(jq("#Show-OlePatronDocument-RequestedItems-TotalCount").val() == undefined && jq("#patronRequest_requestId_line0_control").val() == undefined ){
        jq("#OlePatronDocument-PatronLoanedRecords-Wrapper span a").css("color", "#808080");
    }

    if(jq("#Show-OlePatronDocument-TemporaryCirculationHistoryRecords-Count").val() == undefined && jq("#patronTemp_circulationLocationCode_line0_control").val() == undefined ){
        jq("#OlePatronDocument-TemporaryCirculationHistoryRecords-Wrapper span a").css("color", "#808080");
    }
}

