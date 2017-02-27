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
    showPatronUserNoteBlockDialogBox();
    showPatronBlockDialogBox();
    showPatronUserNoteDialogBox();
    if(jq("#showReinstateDialogBox_control").val()=='true'){
        reinstateDialog();
    }
    disableEnableBarcode();
    openPatronMessagePopup();
    jq("#OlePatronDocument-validation-Message-Section").hide();
    jq("#OlePatronDocument-validation-Message-Section-reinstate").hide();

    unsaved = false;
    jq("#deleteButton").live("click",function(){
        unsaved = true;
    });



    window.onbeforeunload = unloadPage(unsaved);

});


function unloadPage(unsaved){
    if(unsaved){
        jq("#confirmationMessage").text('Patron record for '+jq("#lastName_control").val()+" "+jq("#firstName_control").val()+' is about to be permanently deleted, would you like to continue?');
        result = showLightboxComponent("deleteConfirmationMessage");//window.prompt(message,"");
    }
}

function expandAll(){

    if(jq("#note_disclosureContent").attr('data-open') == 'false'){
        jq("#note_toggle").focus().click();
    }
    if(jq("#OlePatronDocument-OverviewSection_disclosureContent").attr('data-open') == 'false'){
        jq("#OlePatronDocument-OverviewSection_toggle").focus().click();
    }
    if(jq("#OlePatronDocument-ContactsSection_disclosureContent").attr('data-open') == 'false'){
        jq("#OlePatronDocument-ContactsSection_toggle").focus().click();
    }
    if((jq("#OlePatronDocument-Affiliation-Wrapper h3 span a").attr('data-submit_data'))!=undefined && JSON.parse(jq("#OlePatronDocument-Affiliation-Wrapper h3 span a").attr('data-submit_data'))['filterAffiliation'] == 'false'){
        if(jq("#OlePatronDocument-Affiliation-Wrapper h3 span a").hasClass('enableColor')) {
            jq("#OlePatronDocument-Affiliation-Wrapper h3 span a").focus().click();
        }
    }
    if((jq("#OlePatronDocument-LibraryPoliciesSection-Wrapper h3 span a").attr('data-submit_data'))!=undefined && JSON.parse(jq("#OlePatronDocument-LibraryPoliciesSection-Wrapper h3 span a").attr('data-submit_data'))['filterLibraryPolicies'] == 'false'){
        if(jq("#OlePatronDocument-LibraryPoliciesSection-Wrapper h3 span a").hasClass('enableColor')) {
            jq("#OlePatronDocument-LibraryPoliciesSection-Wrapper h3 span a").focus().click();
        }
    }
    if((jq("#OlePatronDocument-PatronLoanedRecords-Wrapper h3 span a").attr('data-submit_data'))!=undefined && JSON.parse(jq("#OlePatronDocument-PatronLoanedRecords-Wrapper h3 span a").attr('data-submit_data'))['filterLoanedRecords'] == 'false'){
        if(jq("#OlePatronDocument-PatronLoanedRecords-Wrapper h3 span a").hasClass('enableColor')) {
            jq("#OlePatronDocument-PatronLoanedRecords-Wrapper h3 span a").focus().click();
        }
    }
    if((jq("#OlePatronDocument-PatronRequestedRecords-Wrapper h3 span a").attr('data-submit_data'))!=undefined && JSON.parse(jq("#OlePatronDocument-PatronRequestedRecords-Wrapper h3 span a").attr('data-submit_data'))['filterRequestedRecords'] == 'false'){
        if(jq("#OlePatronDocument-PatronRequestedRecords-Wrapper h3 span a").hasClass('enableColor')) {
            jq("#OlePatronDocument-PatronRequestedRecords-Wrapper h3 span a").focus().click();
        }
    }
    if((jq("#OlePatronDocument-TemporaryCirculationHistoryRecords-Wrapper h3 span a").attr('data-submit_data'))!=undefined && JSON.parse(jq("#OlePatronDocument-TemporaryCirculationHistoryRecords-Wrapper h3 span a").attr('data-submit_data'))['filterTemporaryCirculationHistoryRecords'] == 'false'){
        if(jq("#OlePatronDocument-TemporaryCirculationHistoryRecords-Wrapper h3 span a").hasClass('enableColor')) {
            jq("#OlePatronDocument-TemporaryCirculationHistoryRecords-Wrapper h3 span a").focus().click();
        }
    }
    if((jq("#OlePatronDocument-NotesSection-Wrapper h3 span a").attr('data-submit_data'))!=undefined && JSON.parse(jq("#OlePatronDocument-NotesSection-Wrapper h3 span a").attr('data-submit_data'))['filterNotesSection'] == 'false'){
        if(jq("#OlePatronDocument-NotesSection-Wrapper h3 span a").hasClass('enableColor')) {
            jq("#OlePatronDocument-NotesSection-Wrapper h3 span a").focus().click();
        }
    }
    if((jq("#OlePatronDocument-ProxySection-Wrapper h3 span a").attr('data-submit_data'))!=undefined && JSON.parse(jq("#OlePatronDocument-ProxySection-Wrapper h3 span a").attr('data-submit_data'))['filterProxySection'] == 'false'){
        if(jq("#OlePatronDocument-ProxySection-Wrapper h3 span a").hasClass('enableColor')) {
            jq("#OlePatronDocument-ProxySection-Wrapper h3 span a").focus().click();
        }
    }
    if((jq("#OlePatronDocument-ProxyForSection-Wrapper h3 span a").attr('data-submit_data'))!=undefined && JSON.parse(jq("#OlePatronDocument-ProxyForSection-Wrapper h3 span a").attr('data-submit_data'))['filterProxyForSection'] == 'false'){
        if(jq("#OlePatronDocument-ProxyForSection-Wrapper h3 span a").hasClass('enableColor')) {
            jq("#OlePatronDocument-ProxyForSection-Wrapper h3 span a").focus().click();
        }
    }
    if((jq("#OlePatronDocument-PatronLocalIdSection-Wrapper h3 span a").attr('data-submit_data'))!=undefined && JSON.parse(jq("#OlePatronDocument-PatronLocalIdSection-Wrapper h3 span a").attr('data-submit_data'))['filterPatronLocalIdSection'] == 'false'){
        if(jq("#OlePatronDocument-PatronLocalIdSection-Wrapper h3 span a").hasClass('enableColor')) {
            jq("#OlePatronDocument-PatronLocalIdSection-Wrapper h3 span a").focus().click();
        }
    }
    if(jq("#OlePatronDocument-InvalidOrLostBarcodeSection-Wrapper h3 span a").attr('data-submit_data')!=undefined && JSON.parse(jq("#OlePatronDocument-InvalidOrLostBarcodeSection-Wrapper h3 span a").attr('data-submit_data'))['filterInvalidOrLostBarcodeSection'] == 'false'){
        if(jq("#OlePatronDocument-InvalidOrLostBarcodeSection-Wrapper h3 span a").hasClass('enableColor')) {
            jq("#OlePatronDocument-InvalidOrLostBarcodeSection-Wrapper h3 span a").focus().click();
        }
    }
    if(jq("#adhoc_disclosureContent").attr('data-open') == 'false'){
        jq("#adhoc_toggle").focus().click();
    }
    if(jq("#routesection_disclosureContent").attr('data-open') == 'false'){
        jq("#routesection_toggle").focus().click();
    }
}

function collapseAll(){
    if(jq("#note_disclosureContent").attr('data-open') == 'true'){
        jq("#note_toggle").focus().click();
    }
    if(jq("#OlePatronDocument-OverviewSection_disclosureContent").attr('data-open') == 'true'){
        jq("#OlePatronDocument-OverviewSection_toggle").focus().click();
    }
    if(jq("#OlePatronDocument-ContactsSection_disclosureContent").attr('data-open') == 'true'){
        jq("#OlePatronDocument-ContactsSection_toggle").focus().click();
    }
    if((jq("#OlePatronDocument-Affiliation-Wrapper h3 span a").attr('data-submit_data'))!=undefined && JSON.parse(jq("#OlePatronDocument-Affiliation-Wrapper h3 span a").attr('data-submit_data'))['filterAffiliation'] == 'true'){
        if(jq("#OlePatronDocument-Affiliation-Wrapper h3 span a").hasClass('enableColor')) {
            jq("#OlePatronDocument-Affiliation-Wrapper h3 span a").focus().click();
        }
    }
    if((jq("#OlePatronDocument-LibraryPoliciesSection-Wrapper h3 span a").attr('data-submit_data'))!=undefined && JSON.parse(jq("#OlePatronDocument-LibraryPoliciesSection-Wrapper h3 span a").attr('data-submit_data'))['filterLibraryPolicies'] == 'true'){
        if(jq("#OlePatronDocument-LibraryPoliciesSection-Wrapper h3 span a").hasClass('enableColor')) {
            jq("#OlePatronDocument-LibraryPoliciesSection-Wrapper h3 span a").focus().click();
        }
    }
    if((jq("#OlePatronDocument-PatronLoanedRecords-Wrapper h3 span a").attr('data-submit_data'))!=undefined && JSON.parse(jq("#OlePatronDocument-PatronLoanedRecords-Wrapper h3 span a").attr('data-submit_data'))['filterLoanedRecords'] == 'true'){
        if(jq("#OlePatronDocument-PatronLoanedRecords-Wrapper h3 span a").hasClass('enableColor')) {
            jq("#OlePatronDocument-PatronLoanedRecords-Wrapper h3 span a").focus().click();
        }
    }
    if((jq("#OlePatronDocument-PatronRequestedRecords-Wrapper h3 span a").attr('data-submit_data'))!=undefined && JSON.parse(jq("#OlePatronDocument-PatronRequestedRecords-Wrapper h3 span a").attr('data-submit_data'))['filterRequestedRecords'] == 'true'){
        if(jq("#OlePatronDocument-PatronRequestedRecords-Wrapper h3 span a").hasClass('enableColor')) {
            jq("#OlePatronDocument-PatronRequestedRecords-Wrapper h3 span a").focus().click();
        }
    }
    if((jq("#OlePatronDocument-TemporaryCirculationHistoryRecords-Wrapper h3 span a").attr('data-submit_data'))!=undefined && JSON.parse(jq("#OlePatronDocument-TemporaryCirculationHistoryRecords-Wrapper h3 span a").attr('data-submit_data'))['filterTemporaryCirculationHistoryRecords'] == 'true'){
        if(jq("#OlePatronDocument-TemporaryCirculationHistoryRecords-Wrapper h3 span a").hasClass('enableColor')) {
            jq("#OlePatronDocument-TemporaryCirculationHistoryRecords-Wrapper h3 span a").focus().click();
        }
    }
    if((jq("#OlePatronDocument-NotesSection-Wrapper h3 span a").attr('data-submit_data'))!=undefined && JSON.parse(jq("#OlePatronDocument-NotesSection-Wrapper h3 span a").attr('data-submit_data'))['filterNotesSection'] == 'true'){
        if(jq("#OlePatronDocument-NotesSection-Wrapper h3 span a").hasClass('enableColor')) {
            jq("#OlePatronDocument-NotesSection-Wrapper h3 span a").focus().click();
        }
    }
    if((jq("#OlePatronDocument-ProxySection-Wrapper h3 span a").attr('data-submit_data'))!=undefined && JSON.parse(jq("#OlePatronDocument-ProxySection-Wrapper h3 span a").attr('data-submit_data'))['filterProxySection'] == 'true'){
        if(jq("#OlePatronDocument-ProxySection-Wrapper h3 span a").hasClass('enableColor')) {
            jq("#OlePatronDocument-ProxySection-Wrapper h3 span a").focus().click();
        }
    }
    if((jq("#OlePatronDocument-ProxyForSection-Wrapper h3 span a").attr('data-submit_data'))!=undefined && JSON.parse(jq("#OlePatronDocument-ProxyForSection-Wrapper h3 span a").attr('data-submit_data'))['filterProxyForSection'] == 'true'){
        if(jq("#OlePatronDocument-ProxyForSection-Wrapper h3 span a").hasClass('enableColor')) {
            jq("#OlePatronDocument-ProxyForSection-Wrapper h3 span a").focus().click();
        }
    }
    if((jq("#OlePatronDocument-PatronLocalIdSection-Wrapper h3 span a").attr('data-submit_data'))!=undefined && JSON.parse(jq("#OlePatronDocument-PatronLocalIdSection-Wrapper h3 span a").attr('data-submit_data'))['filterPatronLocalIdSection'] == 'true'){
        if(jq("#OlePatronDocument-PatronLocalIdSection-Wrapper h3 span a").hasClass('enableColor')) {
            jq("#OlePatronDocument-PatronLocalIdSection-Wrapper h3 span a").focus().click();
        }
    }
    if(jq("#OlePatronDocument-InvalidOrLostBarcodeSection-Wrapper h3 span a").attr('data-submit_data')!=undefined && JSON.parse(jq("#OlePatronDocument-InvalidOrLostBarcodeSection-Wrapper h3 span a").attr('data-submit_data'))['filterInvalidOrLostBarcodeSection'] == 'true'){
        if(jq("#OlePatronDocument-InvalidOrLostBarcodeSection-Wrapper h3 span a").hasClass('enableColor')) {
            jq("#OlePatronDocument-InvalidOrLostBarcodeSection-Wrapper h3 span a").focus().click();
        }
    }
    if(jq("#adhoc_disclosureContent").attr('data-open') == 'true'){
        jq("#adhoc_toggle").focus().click();
    }
    if(jq("#routesection_disclosureContent").attr('data-open') == 'true'){
        jq("#routesection_toggle").focus().click();
    }
}

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

function closePatronUserNoteBlockDialog(){
    jq("div#OlePatronDocument-userBlock-note-Section").fadeOut(300);
    jq('#mask').fadeOut(300);
}

function closePatronBlockDialog(){
    jq("div#OlePatronDocument-Block-Section").fadeOut(300);
    jq('#mask').fadeOut(300);
}

function closePatronUserNoteDialog(){
    jq("div#OlePatronDocument-user-note-Section").fadeOut(300);
    jq('#mask').fadeOut(300);
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

function showPatronUserNoteBlockDialogBox(){
    if(jq("#showPatronUserNoteDialogBox_control").val()=='true' && jq("#showPatronBlockDialogBox_control").val()=='true' ){
        displayDialogWindow("div#OlePatronDocument-userBlock-note-Section");
    }
}

function showPatronBlockDialogBox(){
    if(jq("#showPatronUserNoteDialogBox_control").val()!='true' && jq("#showPatronBlockDialogBox_control").val()=='true' ){
        displayDialogWindow("div#OlePatronDocument-Block-Section");
    }
}

function showPatronUserNoteDialogBox(){
    if(jq("#showPatronUserNoteDialogBox_control").val()=='true' && jq("#showPatronBlockDialogBox_control").val()!='true' ){
        displayDialogWindow("div#OlePatronDocument-user-note-Section");
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

