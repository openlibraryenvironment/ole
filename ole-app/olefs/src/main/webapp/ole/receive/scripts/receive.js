jq(document).ready(function(){
    var unsaved = false;
    if(jq("#serialHiddenFields_h4").val()!=jq("#instanceIdField_control").val()){
        unsaved = true;
    }
    jq(":input").live("keypress",function(){
        unsaved = true;
    });
    function unloadPage(){
        if(unsaved){
            return confirm( getMessage(kradVariables.MESSAGE_KEY_DIRTY_FIELDS));
        }
    }
    jq('form').bind('submit', function() { unsaved = false; });
    window.onbeforeunload = unloadPage;
    jq(".selectPOCheck").live("click",function(){
        jq( "#serialHiddenFields_h9").val(false);
        jq(".selectPOCheck").removeAttr("checked");
        jq("#"+jq(this).attr('id')).attr("checked","true");
    });
    if(jq("#serialHiddenFields_h8").val()=="S"){
        jq("#enumLevel1_control").focus();
    }
    if(jq( "#serialHiddenFields_h9").val()=="true") {

        displayRelatedPOs();
    }else{
        urgentNote();
    }
    jq("#recRecTypeField_control").live("change",function(){
        jq('#refreshReceivingRecordTypeBtn').click();
    });

   /* jq("#OLESerialReceiving-MainReceiptHistory_disclosureContent table thead tr th").live("click", function(){
        jq("#OLESerialReceiving-MainReceiptHistory_disclosureContent table thead tr th:eq(1)").removeAttr("class");
    });

    jq("#OLESerialReceiving-SupplementReceiptHistory_disclosureContent table thead tr th").live("click", function(){
        jq("#OLESerialReceiving-SupplementReceiptHistory_disclosureContent table thead tr th:eq(1)").removeAttr("class");
    });

    jq("#OLESerialReceiving-IndexReceiptHistory_disclosureContent table thead tr th").live("click", function(){
        jq("#OLESerialReceiving-IndexReceiptHistory_disclosureContent table thead tr th:eq(1)").removeAttr("class");
    });*/
    jq(".enumClass").live("click",function() {
        var id = jq(this).attr("id");
        id=id.replace(/[^\d.]/g,"");
        if(jq(this).hasClass("enumSupplementClass")) {
            jq("#onChangeEnumSupplement_line"+id).focus().click();
        }else if(jq(this).hasClass("enumMainClass")) {
            jq("#onChangeEnumMain_line"+id).focus().click();
        }else if(jq(this).hasClass("enumIndexClass")) {
            jq("#onChangeEnumIndex_line"+id).focus().click();
        }
    });
    jq(".chronClass").live("click",function() {
        var id = jq(this).attr("id");
        id=id.replace(/[^\d.]/g,"");
        if(jq(this).hasClass("chronSupplementClass")) {
            jq("#onChangeChronSupplement_line"+id).focus().click();
        }else if(jq(this).hasClass("chronMainClass")) {
            jq("#onChangeChronMain_line"+id).focus().click();
        }else if(jq(this).hasClass("chronIndexClass")) {
            jq("#onChangeChronIndex_line"+id).focus().click();
        }
    });
});

/*jq(window).load(function(){
    jq("#OLESerialReceiving-MainReceiptHistory_disclosureContent table thead tr th:eq(1)").removeAttr("class").unbind( "click" );
    jq("#OLESerialReceiving-SupplementReceiptHistory_disclosureContent table thead tr th:eq(1)").removeAttr("class").unbind( "click" );
    jq("#OLESerialReceiving-IndexReceiptHistory_disclosureContent table thead tr th:eq(1)").removeAttr("class").unbind( "click" );

})*/

function linkPO(){
    if(jq( "#serialHiddenFields_h9").val()=="false") {
        jq( "div#OLESerialReceiving-OLESerialRelatedPODocument-HorizontalBoxSection" ).fadeOut(300);
        jq('#mask').fadeOut(300)
        jq('#Link-PO-Button').click();
    }else{
        jq('#Link-PO-ErrMsg').click();
    }

}

function noLinkPO(){
    jq("div#OLESerialReceiving-OLESerialRelatedPODocument-HorizontalBoxSection").fadeOut(300);
    jq('#mask').fadeOut(300)
    jq('#No-Link-PO-Button').click();
}

function displayRelatedPOs(){
    displayDialogWindow("div#OLESerialReceiving-OLESerialRelatedPODocument-HorizontalBoxSection");
}
function urgentNote(){
    if(jq("#serialHiddenFields_h5").val()=="true" && jq("#urgentNoteField_control").val()!=""){
        displayDialogWindow("div#UrgentNote-HorizontalBoxSection");
      //  jq('#mask').fadeOut(300);
    }
}

function claimNote(){
    if(jq("#serialHiddenFields_h6").val()=="true" || jq("#serialHiddenFields_h7").val()=="true"){
        displayDialogWindow("div#ClaimNote-HorizontalBoxSection");
        jq('#mask').fadeOut(300);
    }
}
function specialIssue(){
    if(jq("#serialHiddenFields_control").val()=='true'){
        displayDialogWindow("div#SpecialIssue-HorizontalBoxSection");
       // jq('#mask').fadeOut(300);
    }
}

function closeUrgentNoteDialog(){
    jq( "#serialHiddenFields_h5").val(false);
    jq( "div#UrgentNote-HorizontalBoxSection" ).fadeOut(300);
    jq('#mask').fadeOut(300);
    jq("#receiveAckBtnForUrgNote").focus().click();
}


function closeClaimNoteDialog(){
    jq( "#serialHiddenFields_h6").val(false);
    jq( "div#ClaimNote-HorizontalBoxSection" ).fadeOut(300);
    jq('#mask').fadeOut(300);
}

function treatmentNote(){
    if(jq("#serialHiddenFields_h0").val()=='true'){
        displayDialogWindow("div#TreatmentInstructionNote-HorizontalBoxSection");
       // jq('#mask').fadeOut(300);
    }else{
        createItemLink();
    }
  //  jq("#OLESerialReceiving-MainReceiptHistory_disclosureContent table thead tr th:eq(1)").removeAttr("class").unbind( "click" );

}

function closeSpecialIssueDialog(){
    jq( "#serialHiddenFields_control").val(false);
    jq( "div#SpecialIssue-HorizontalBoxSection" ).fadeOut(300);
    jq('#mask').fadeOut(300);
}

function closeTreatmentNoteDialog(){
    jq( "#serialHiddenFields_h0").val(false);
    jq( "div#TreatmentInstructionNote-HorizontalBoxSection" ).fadeOut(300);
    jq('#mask').fadeOut(300);
    jq("#receiveAckBtn").focus().click();
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

function searchSerialReceiving(){
    window.open("serialsReceivingRecordController?viewId=SerialsReceivingRecordSearchView&methodToCall=start","_self");
}
function createItemLink(){
    if(jq(".createItemCss:checked").length == 1 && jq("#serialHiddenFields_h11").val() == "true"){
        window.open("editorcontroller?viewId=EditorView&methodToCall=load&docCategory=work&docType=item&docFormat=oleml&docId="+jq("#serialHiddenFields_h3").val()+"&bibId="+jq("#serialHiddenFields_h10").val()+"&instanceId="+jq("#instanceIdField_control").val()+"&editable=true");
    }
}
function receive(line){
    jq("#serialHiddenFields_h1").val(line);
    jq("#hdnReceiveHistory_button").focus().click();
}
function claim(line){
    jq("#serialHiddenFields_h1").val(line);
    jq("#hdnClaimAgainNote_button").focus().click();
}
function cancelClaim(line){
    jq("#sserialHiddenFields_h1").val(line);
    jq("#hdnCancelClaim_button").focus().click();
}
function relink(){
    jq("#instanceIdField input[type='image']").focus().click();
}
function poRelink(){
    jq("#pIdField input[type='image']").focus().click();
}

function confirmReturnToSearch(){
    if(jq("#serialHiddenFields_h2").val()=='true'){
        displayDialogWindow("div#ConfirmReturnToSearch");
        jq('#mask').fadeOut(300);
    }
}
function closeConfirmReturnToSearchDialog(){
    jq( "#serialHiddenFields_h2").val(false);
    jq( "div#ConfirmReturnToSearch" ).fadeOut(300);
    jq('#mask').fadeOut(300);
}
function unReceive(line){
    jq("#serialHiddenFields_h1").val(line);
    jq("#hdnUnReceiveHistory_button").focus().click();
}
jq("#vendorIdField_control").live("change",function() {
    jq('#serialReceivingVendor_button').click();
});


function selectAllMainPublicDisplay(){
    jq(".publicDisplayMainClass").attr("checked","true");
}

function deSelectAllMainPublicDisplay(){
    jq(".publicDisplayMainClass").removeAttr("checked");
}

function selectAllSupplementPublicDisplay(){
    jq(".publicDisplaySupplementClass").attr("checked","true");
}

function deSelectAllSupplementPublicDisplay(){
    jq(".publicDisplaySupplementClass").removeAttr("checked");
}

function selectAllIndexPublicDisplay(){
    jq(".publicDisplayIndexClass").attr("checked","true");
}

function deSelectAllIndexPublicDisplay(){
    jq(".publicDisplayIndexClass").removeAttr("checked");
}

function lTrim(s)
{
    var pos=0;
    while(pos < s.length && s[pos] == ' ')
    {   pos++; }
    return s.substring(pos, s.length);
}

function onChangeEnumCaption1() {
    var enumCaption1 = jq("#enumCaption1_control").val();
    enumCaption1 = lTrim(enumCaption1);
    jq("#enumCaption1Length_control").val(enumCaption1.length);
}

function onChangeEnumCaption2() {
    var enumCaption2 = jq("#enumCaption2_control").val();
    enumCaption2 = lTrim(enumCaption2);
    jq("#enumCaption2Length_control").val(enumCaption2.length);
}

function onChangeEnumCaption3() {
    var enumCaption3 = jq("#enumCaption3_control").val();
    enumCaption3 = lTrim(enumCaption3);
    jq("#enumCaption3Length_control").val(enumCaption3.length);
}

function onChangeEnumCaption4() {
    var enumCaption4 = jq("#enumCaption4_control").val();
    enumCaption4 = lTrim(enumCaption4);
    jq("#enumCaption4Length_control").val(enumCaption4.length);
}

function onChangeEnumCaption5() {
    var enumCaption5 = jq("#enumCaption5_control").val();
    enumCaption5 = lTrim(enumCaption5);
    jq("#enumCaption5Length_control").val(enumCaption5.length);
}

function onChangeEnumCaption6() {
    var enumCaption6 = jq("#enumCaption6_control").val();
    enumCaption6 = lTrim(enumCaption6);
    jq("#enumCaption6Length_control").val(enumCaption6.length);
}

function onChangeChronCaption1() {
    var chronCaption1 = jq("#chronCaption1_control").val();
    chronCaption1 = lTrim(chronCaption1);
    jq("#chronCaption1Length_control").val(chronCaption1.length);
}

function onChangeChronCaption2() {
    var chronCaption2 = jq("#chronCaption2_control").val();
    chronCaption2 = lTrim(chronCaption2);
    jq("#chronCaption2Length_control").val(chronCaption2.length);
}

function onChangeChronCaption3() {
    var chronCaption3 = jq("#chronCaption3_control").val();
    chronCaption3 = lTrim(chronCaption3);
    jq("#chronCaption3Length_control").val(chronCaption3.length);
}

function onChangeChronCaption4() {
    var chronCaption4 = jq("#chronCaption4_control").val();
    chronCaption4 = lTrim(chronCaption4);
    jq("#chronCaption4Length_control").val(chronCaption4.length);
}

function onChangeEnumerationScript(line) {
    var spans = jq(jq(".enumClass")[0].getElementsByTagName("span"))
    if(spans.length<3){
        jq("#serialHiddenFields_h1").val(line);
        jq("#setEnumeration").focus().click();
}
}

function onChangeChronologyScript(line) {
    var spans = jq(jq(".chronClass")[0].getElementsByTagName("span"))
    if(spans.length<3){
        jq("#serialHiddenFields_h1").val(line);
        jq("#setChronology").focus().click();
}
}

function closeConfirmEnumerationChange(){
    jq( "div#enumerationSaveDialog" ).fadeOut(300);
    jq('#mask').fadeOut(300);
}

function closeConfirmChronologyChange(){
    jq( "div#chronologySaveDialog" ).fadeOut(300);
    jq('#mask').fadeOut(300);
}
function showChronDialog(){
    displayDialogWindow("div#chronologySaveDialog");
}

function showEnumDialog(){
    displayDialogWindow("div#enumerationSaveDialog");
}

function loadMainHistoryRecords(){
    if(jq("#serialHiddenFields_h12").val() == "false"){
        jq("#serialHiddenFields_h12").val('true');
        jq("#OLESerialReceiving-MainReceiptHistory_Btn").focus().click();
    }else{
        jq("#OLESerialReceiving-MainReceiptHistory_toggle").focus().click();
    }
}

function loadSupplementaryHistoryRecords(){
    if(jq("#serialHiddenFields_h13").val() == "false"){
        jq("#serialHiddenFields_h13").val('true');
        jq("#OLESerialReceiving-SupplementReceiptHistory_Btn").focus().click();
    }else{
        jq("#OLESerialReceiving-SupplementReceiptHistory_toggle").focus().click();
    }
}

function loadIndexHistoryRecords(){
    if(jq("#serialHiddenFields_h14").val() == "false"){
        jq("#serialHiddenFields_h14").val('true');
        jq("#OLESerialReceiving-IndexReceiptHistory_Btn").focus().click();
    }else{
        jq("#OLESerialReceiving-IndexReceiptHistory_toggle").focus().click();
    }
}
