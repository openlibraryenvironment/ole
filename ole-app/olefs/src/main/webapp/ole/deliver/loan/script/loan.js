//hiddenReturnField_h4 mapped to returnCheck
jq(function() {
    jq("#ReturnViewPage_tab").live("click",function(){
        jq("#hiddenReturnField_h4").val("true")
    });
//hiddenReturnField_h4 mapped to returnCheck
    jq("#LoanViewPage_tab").live("click",function(){
        jq("#hiddenReturnField_h4").val("false")
    });
//hiddenReturnField_h4 mapped to returnCheck
    if(jq("#hiddenReturnField_h4").val()=="true"){
        jq("#DeliverTabSection_tabs").tabs("select","#ReturnViewPage_tab");
    }

});

function getProxyName(firstName,lastName){
    jq("#headerMsgId span label").html("The current patron is a proxy borrower for \""+firstName+" "+lastName+"\". Please select the correct one for this transaction and then click OK.");
}

function displayCopyMessage() {
    if (jq("#copyCheck input[type='radio']:checked").val() == "false") {
        jq("#check-in_msg").children('span').eq(0).html("Verify whether copy request has been fulfilled </br> <b>Keep the item in the copy section for processing the copy request</b>");
    }
    else{
        jq("#check-in_msg").children('span').eq(0).html("Verify whether copy request has been fulfilled </br>");
    }

}

function getCurrentProxyName(){
    jq("#headerMsgId span label").html("The current patron is a proxy borrower. Please select the correct one for this transaction and click OK." );
}

function validateRealPatron(){
    if(!jq(".patronCheckBoxClass:checked").length == 0){
        jq('#realPatronBtn').focus().click();
    }
}
//hiddenLoanField_h11 mapped to fastAddItemIndicator
//hiddenLoanField_h12 mapped to fastAddUrl
function openFastAdd(){
    if(jq("#hiddenLoanField_h11").val()=='true'){
        _initAndOpenLightbox({type: 'iframe', href: jq("#hiddenLoanField_h12").val(), height: 'auto', width: '70%', autoSize: true},
            undefined);
        return false;
    }
}

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
    if(jq(divID).height()<300){
        top='100px';
    }
    if(divID == "div#RealPatronSection-HorizontalBoxSection"){
        top='75px';
    }
    if(divID == "div#PatronUserNote-HorizontalBoxSection"){
        top='75px';
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


var checkOutIdleTime = 0;
var checkInIdleTime = 0;
//hiddenReturnField_h4 mapped to returnCheck
jq(document).ready(function(){
     if(jq("#hiddenReturnField_h4").val()=='true') {
         jq("#ReturnItemView").append("<audio id='sound' preload='auto'><source src='../ole/deliver/loan/script/alert.wav'type='audio/wav'/></audio>");
         jq("#hiddenReturnField_h4").val("true");
     } else{
          jq("#PatronItemView").append("<audio id='sound' preload='auto'><source src='../ole/deliver/loan/script/alert.wav'type='audio/wav'/></audio>");
         jq("#hiddenReturnField_h4").val("false");
     }
    validationsForPop();

    jq(".fancybox-close").live("click" ,function(e) {
        jq('#Patron-item_control').select().focus();
    });

    //hiddenLoanField_h8 mapped to maxSessionTime
    var checkOutIdleInterval = setInterval(function(){
        var counterCheckOut = jq("#hiddenLoanField_h8").val();
        checkOutIdleTime = checkOutIdleTime + 1;
        if (parseInt(checkOutIdleTime) > parseInt(counterCheckOut)) {
            clearInterval(checkOutIdleInterval);
            jq('#SessionReset').focus().click();;
        }
    }, 60000); // 1 minute

    //Zero the idle timer on mouse movement.
    jq(this).mousemove(function (e) {
        checkOutIdleTime = 0;
        checkInIdleTime = 0;
    });
    //Zero the idle timer on key press.
    jq(this).keypress(function (e) {
        checkOutIdleTime = 0;
        checkInIdleTime = 0;
    });
//hiddenReturnField_h12 mapped to maxTimeForCheckInDate
    //hiddenReturnField_h4 mapped to returnCheck
    var checkInIdleInterval = setInterval(function(){
        var counterCheckIn = jq("#hiddenReturnField_h12").val();
        checkInIdleTime = checkInIdleTime + 1;
        if (parseInt(checkInIdleTime) > parseInt(counterCheckIn)) {
            clearInterval(checkInIdleInterval);
            jq("#hiddenReturnField_h4").val("true")
            jq("#endSessionButton").focus().click();
        }
    }, 60000); // 1 minute


/*
    var counter = jq("#maxSessionTime_control").val();

    timer = setInterval(function() {
        jq("#maxSessionTime_control").val(--counter);
        if (counter == 0) {
            clearInterval(timer);
            jq('#SessionReset').focus().click();
        };
    }, 60000);*/

    jq("#DeliverTabSection").width(jq("form").width());

    jq("#LoanCirculationDesk_control").live("change",function() {
        jq('#ChangeLocationButton').focus().click();
    });

    jq("#ReturnCirculationDesk_control").live("change",function() {
        jq('#ChangeReturnLocationButton').focus().click();
    });


    jq(".patronCheckBoxClass").live("click",function(){
        jq(".patronCheckBoxClass").removeAttr("checked");
        jq("#"+jq(this).attr('id')).attr("checked","true");
    });

    jq(document).keypress(function(e) {
        if(e.which == 13) {
            if(e.target.id == "Patron-barcode_control"){
                if(jq( "#Patron-barcode_control").val()!=""){
                    jq(this).blur();
                    jq('#SecondarySearchPatron').focus().click();
                }else{
                    jq('#Patron-barcode_control').select().focus();
                    return false;
                }
            }else if(e.target.id == "Patron-item_control"){
                if(jq( "#Patron-item_control").val()!=""){
                    jq(this).blur();
                    if(jq( "#Patron-barcode_control").val()!=""){
                        jq('#SecondarySearchItem').focus().click();
                    }else{
                        jq("#LoanMessageFieldSection").text("Please enter patron barcode.");
                        jq('#Patron-barcode_control').select().focus();
                        return false;
                    }
                }else{
                    jq( '#Patron-item_control').select().focus();
                    return false;
                }
            }
            return false;
        }
    });

    //hiddenLoanField_h21 mapped to itemFocus
    if(jq("#hiddenLoanField_h21").val()=="true"){
        jq( '#Patron-item_control').select().focus();
    }
    //hiddenLoanField_h22 mapped to patronFocus
    if(jq("#hiddenLoanField_h22").val()=="true"){
        jq('#Patron-barcode_control').select().focus();
    }


    jq('#BillPaymentOption_control').click(function(){
        if(jq("#BillPaymentOption_control").is(':checked')){
            jq("div#AlertBoxSectionForBill-HorizontalBoxSection").fadeOut(300);
        }else{
            jq("div#AlertBoxSectionForBill-HorizontalBoxSection").fadeIn(300);
        }
    });

    jq('#CheckInItem_control').live("keypress" ,function(e) {
        if(e.which == 13) {
            if(jq( "#CheckInItem_control").val()!=""){
                jq('#validateItem').trigger('click');
                jq("#DeliverTabSection_tabs").tabs("select","#ReturnViewPage_tab");
            }
        }
    });
    jq( "#CheckInItem_control").select().focus();


    if(jq("#RenewalDueDateCollection-HorizontalBoxSection").length > 0) {
        jq("#RenewalDueDateCollection-HorizontalBoxSection th.col1").children().append("<input type='checkbox' value='false' class='selectAllRenewCB' name='selectLoanedAll' id='selectLoanedAll' style='float:left;margin-left:0.5em'>");
    }


    if(jq("#Patron-ExistingLoanItemListSection-HorizontalBoxSection").length > 0) {
        jq("#Patron-ExistingLoanItemListSection-HorizontalBoxSection th.col1").children().append("<input type='checkbox' value='false' class='selectAllLoanedCB' name='selectLoanedAll' id='selectLoanedAll' style='float:left;margin-left:0.5em'>");
    }
//hiddenReturnField_h7 mapped to CurrentDate
    jq("#CheckInDate_control").live("change",function(){
        var diff = validateDate(jq("#hiddenReturnField_h7").val(), jq("#CheckInDate_control").val());
        if(diff==-1){
            // hiddenReturnField_h0 mapped to audioForPastDate
            //hiddenReturnField_h1 mapped to audioEnable
            if(jq("#hiddenReturnField_h0").val()=="true"){
                jq("#hiddenReturnField_h1").val("true");
            }
            playAudio();
            jq("div#AlertBoxSectionForDate-HorizontalBoxSection").fadeIn(300);
            jq("div#AlertBoxSectionForTime-HorizontalBoxSection").fadeOut(300);
        }else if(diff==1){
            jq("#CheckInDate_control").val('');
            jq("#CheckInDate_control").focus();
            jq("div#AlertBoxSectionForDate-HorizontalBoxSection").fadeOut(300);
        }else{
            jq("div#AlertBoxSectionForDate-HorizontalBoxSection").fadeOut(300);
            validateCheckInTime();
        }
    });

    jq('#CheckInTime_control').live("change",function(){
        validateCheckInTime();
    });

    // hiddenLoanField_h0 mapped to loanLoginMessage.
    if (jq("#hiddenLoanField_h0").val() == 'true') {
       jq(parent.document).find(".searchbox").focus();
    }
    //hiddenLoanField_h27 mapped to loanLoginName
    jq(parent.document).find("#search form:eq(0)").submit(function() {
        jq( "#hiddenLoanField_h27").val(jq(parent.document).find(".searchbox").val());
        jq("#loanLoginBtn").focus().click();
        return false;
    });
    jq(parent.document).find("#search form:eq(1)").submit(function() {
        jq("#loanLogOutBtn").focus().click();
        return false;
    });

    jq("input#ReturnView-missingPieceCount_control").live('keydown',function(event) {
            if ( event.keyCode == 46 || event.keyCode == 8 || event.keyCode == 9 || event.keyCode == 27 || event.keyCode == 13 ||
                (event.keyCode == 65 && event.ctrlKey === true) ||
                (event.keyCode >= 35 && event.keyCode <= 39)) {
                return;
            }
            else {
                if (event.shiftKey || (event.keyCode < 48 || event.keyCode > 57) && (event.keyCode < 96 || event.keyCode > 105 )) {
                    event.preventDefault();
                }
            }
    });
    jq("input#LoanView-missingPieceCount_control").live('keydown',function(event) {
        if ( event.keyCode == 46 || event.keyCode == 8 || event.keyCode == 9 || event.keyCode == 27 || event.keyCode == 13 ||
            (event.keyCode == 65 && event.ctrlKey === true) ||
            (event.keyCode >= 35 && event.keyCode <= 39)) {
            return;
        }
        else {
            if (event.shiftKey || (event.keyCode < 48 || event.keyCode > 57) && (event.keyCode < 96 || event.keyCode > 105 )) {
                event.preventDefault();
            }
        }
    });
    jq("input#MissingPiece-dialogMissingPieceCount-Section_control").live('keydown',function(event) {
        if ( event.keyCode == 46 || event.keyCode == 8 || event.keyCode == 9 || event.keyCode == 27 || event.keyCode == 13 ||
            (event.keyCode == 65 && event.ctrlKey === true) ||
            (event.keyCode >= 35 && event.keyCode <= 39)) {
            return;
        }
        else {
            if (event.shiftKey || (event.keyCode < 48 || event.keyCode > 57) && (event.keyCode < 96 || event.keyCode > 105 )) {
                event.preventDefault();
            }
        }
    });
    jq("input#MissingPiece-dialogItemNoOfPieces-Section_control").live('keydown',function(event) {
        if ( event.keyCode == 46 || event.keyCode == 8 || event.keyCode == 9 || event.keyCode == 27 || event.keyCode == 13 ||
            (event.keyCode == 65 && event.ctrlKey === true) ||
            (event.keyCode >= 35 && event.keyCode <= 39)) {
            return;
        }
        else {
            if (event.shiftKey || (event.keyCode < 48 || event.keyCode > 57) && (event.keyCode < 96 || event.keyCode > 105 )) {
                event.preventDefault();
            }
        }
    });
    jq("input#numberOfPieces_control").live('keydown',function(event) {
        if ( event.keyCode == 46 || event.keyCode == 8 || event.keyCode == 9 || event.keyCode == 27 || event.keyCode == 13 ||
            (event.keyCode == 65 && event.ctrlKey === true) ||
            (event.keyCode >= 35 && event.keyCode <= 39)) {
            return;
        }
        else {
            if (event.shiftKey || (event.keyCode < 48 || event.keyCode > 57) && (event.keyCode < 96 || event.keyCode > 105 )) {
                event.preventDefault();
            }
        }
    });
    setMissingPieceCount();
    displayMissingDamagedBox();

});

function selectAllLoanedItem(){
    jq(".loaningItemCBClass").attr("checked","true");
}

function deSelectAllLoanedItem(){
    jq(".loaningItemCBClass").removeAttr("checked");
}

function selectAllExistingLoanedItem(){
    jq(".loanedItemCBClass").attr("checked","true");

}

function deSelectAllExistingLoanedItem(){
    jq(".loanedItemCBClass").removeAttr("checked");
}

function selectAllRenewItem(){
    jq(".renewItemCBClass").attr("checked","true");
}

function deSelectAllRenewItem(){
    jq(".renewItemCBClass").removeAttr("checked");
}
//hiddenLoanField_h17 mapped to dueDateMap
function setDueDate(){
    jq( "#hiddenLoanField_h17").val(jq( "#popUpDate_control").val());
    jq('#hdnLoanBtn').focus().click();
    closeMessageBox();
}
//hiddenReturnField_h4 mapped to returnCheck
//hiddenLoanField_h17 mapped to dueDateMap
function returnLoan(){
    jq( "#hiddenLoanField_h17").val(jq( "#popUpDate").val());
    jq('#hdnReturnLoanBtn').focus().click();
    jq("div#MessagePopupSectionForLoan").fadeOut(300);
    jq('#mask').fadeOut(300);
    jq("#hiddenReturnField_h4").val("true");
}
//hiddenReturnField_h4 mapped to returnCheck
function doNotReturnLoan(){
    jq('#hdnNoReturnLoanBtn').focus().click();
    jq("div#MessagePopupSectionForLoan").fadeOut(300);
    jq('#mask').fadeOut(300);
    jq("#hiddenReturnField_h4").val("true");
}
function setAlterDueDateLoanList(){
    jq( "div#AlterDueDateSection-HorizontalBoxSection" ).fadeOut('fast');
}
//hiddenLoanField_h14 mapped to alterDueDateFlag
function alterDueDate(){
    if(jq("#hiddenLoanField_h14").val()=='true'){
        jq('body').scrollTop(0);
        jq(window.parent).scrollTop(0);
        displayDialogWindow("div#AlterDueDateSection-HorizontalBoxSection");
        return false;
    }
}


//hiddenLoanField_h25 mapped to claimsReturnFlag
function claimsReturn(){
    if(jq("#hiddenLoanField_h25").val()=='true'){
        jq('body').scrollTop(0);
        jq(window.parent).scrollTop(0);
        displayDialogWindow("div#ClaimsReturn-HorizontalBoxSection");
        jq('#mask').fadeOut(300);
    }
}
function setRenewalDueDateLoanList(){
    jq('#hdnrenewLoanDueDateBtn').focus().click();
    jq( "div#RenewalDueDateSection-HorizontalBoxSection" ).fadeOut('fast');
    jq('#mask').fadeOut(300);
}

function setPatronUserNoteDelete(){
    if(!jq(".patronNotesCheckBoxClass:checked").length == 0){
        jq('#hdnPatronDeleteBtn').focus().click();
        jq( "div#PatronUserNote-HorizontalBoxSection" ).fadeOut(300);
        jq('#mask').fadeOut(300);
    }else{
        jq('#message_patronUserNote').attr('style','display:inline');
    }
}

function saveFastAddItem(){
    jq('#addFastAddItemBtn').focus().click();
}


function setClaimsReturnList(){
    if (jq("#claimsNote_control").val().length > 0) {
        jq("#mapClaimsReturnNote_control").val(jq("#claimsNote").val());
        jq('#hdnClaimsReturnBtn').focus().click();
        jq("div#ClaimsReturn-HorizontalBoxSection").fadeOut(300);
        jq('#mask').fadeOut(300);
    } else {
        return false;
    }
}
//hiddenLoanField_h25 mapped to claimsReturnFlag
//hiddenLoanField_h26 mapped to removeClaimsReturnFlag
function removeClaimsReturnList(){
    jq( "#hiddenLoanField_h25").val(false);
    jq( "#hiddenLoanField_h26").val(true);
    jq('#hdnClaimsReturnBtn').focus().click();
    jq( "div#ClaimsReturn-HorizontalBoxSection" ).fadeOut(300);
    jq('#mask').fadeOut(300);
}
//hiddenLoanField_h25 mapped to claimsReturnFlag
function closeClaimsReturnDialog(){
    jq( "#hiddenLoanField_h25").val(false);
    jq( "div#ClaimsReturn-HorizontalBoxSection" ).fadeOut(300);
    jq('#mask').fadeOut(300);
    focusItem();
}
//hiddenLoanField_h14 mapped to alterDueDateFlag
function closeAlterDueDateDialog(){
   if(jq('#AlterDueDateInformationSection span').length==0){
        jq( "#hiddenLoanField_h14").val(false);
        jq('#hdnAlterLoanCloseBtn').focus().click();
        jq( "div#AlterDueDateSection-HorizontalBoxSection" ).fadeOut('fast');
        jq('#mask').fadeOut(300);
    }  else {
       jq('body').scrollTop(0);
       jq(window.parent).scrollTop(0);
        displayDialogWindow("div#AlterDueDateSection-HorizontalBoxSection");
    }
    focusItem();
}
//hiddenLoanField_h10 mapped to renewDueDateFlag
function closeRenewalDueDateDialog(){
    jq( "#hiddenLoanField_h10").val(false);
    jq('#hdnrenewLoanCloseBtn').focus().click();
    jq( "div#RenewalDueDateSection-HorizontalBoxSection" ).fadeOut('fast');
    jq('#mask').fadeOut(300);
}

function closeRenewOverrideDialog(){
    jq( "#showRenewDueDateDialog_attribute").val(false);
    jq('#hdnrenewCloseBtn').focus().click();
    jq( "div#RenewalDueDateSection-HorizontalBoxSection_div" ).fadeOut('fast');
}
//hiddenLoanField_h23 mapped to realPatronFlag
function closeRealPatronDialog(){
    jq( "#hiddenLoanField_h23").val(false);
    jq('#hdnRealPatronCloseBtn').focus().click();
    jq( "div#RealPatronSection-HorizontalBoxSection" ).fadeOut('fast');
    jq('#mask').fadeOut(300);
}
//hiddenLoanField_h23 mapped to realPatronFlag
function closeRealPatronUpdate(){
    jq( "#hiddenLoanField_h23").val(false);
    jq( "div#RealPatronSection-HorizontalBoxSection" ).fadeOut('fast');
    jq('#mask').fadeOut(300);
    focusItem();
}
//hiddenLoanField_h7 mapped to patronNoteFlag
function closePatronUserNoteDialog(){
    jq( "#hiddenLoanField_h7").val(false);
    jq( "div#PatronUserNote-HorizontalBoxSection" ).fadeOut(300);
    jq('#mask').fadeOut(300);
}
//hiddenLoanField_h24 mapped to changeLocationFlag
function closeCirculationLocationDialog(){
    jq( "#hiddenLoanField_h24").val(false);
    jq( "div#ConfirmCirculationLocationChange" ).fadeOut(300);
    jq('#mask').fadeOut(300);
}
function closeReturnCirculationLocationDialog(){
    jq( "#hiddenLoanField_h24").val(false);
    jq( "div#ConfirmReturnCirculationLocationChange" ).fadeOut(300);
    jq('#mask').fadeOut(300);
}
//hiddenLoanField_h4 mapped to newPrincipalId
function setUser(){
    if(jq( "#hiddenLoanField_h4").val() != null && jq( "#hiddenLoanField_h4").val() != ""){
       jq(parent.document).find("#login-info").children('strong').eq(1).html("  Impersonating User: "+jq( "#hiddenLoanField_h4").val());
    }
    if(jq("#newPrincipalName_control").val()!= null && jq("#newPrincipalName_control").val()!=""){
        jq('#hdlogInUser').focus().click();
        jq("#OverRideLogInSectionLink").fadeOut(300);
        jq('#mask').fadeOut(300);
    }
}

//hiddenLoanField_h5 mapped to oldPrincipalId
function refreshUserLogin(){
    if(jq( "#hiddenLoanField_h5").val() != null && jq( "#hiddenLoanField_h5").val() != ""){
       jq(parent.document).find("#login-info").children('strong').eq(1).html("  Impersonating User: "+jq( "#hiddenLoanField_h5").val());
    }
}


function setDescription(){
    jq('#hdnContinueBtn').focus().click();
    closeMessageBoxForReturn();
}
function continueBtn(){
    jq('#hdnLoanContinueBtn').focus().click();
    closeMessageBox();
}
//hiddenReturnField_h12 mapped for maxTimeForCheckInDate
function validateDate(currDate, checkInDate){
    var currentDate = new Date(currDate);
    var newCheckInDate = new Date(checkInDate);
    if(currentDate>newCheckInDate){
        var counter = jq("#hiddenReturnField_h12").val();
        timer = setInterval(function() {
          //  alert(counter);
           // jq("#mapMaxTimeForCheckInDate_control").val(--counter);
            if (counter == 0) {
                clearInterval(timer);
              //  jq("#CheckInDate").val(currDate);
              //  jq("#mapMaxTimeForCheckInDate_control").val(jq("#mapCheckInDateMaxTime_control").val());
              //  jq("div#AlertBoxSectionForDate-HorizontalBoxSection").fadeOut(300);
              //  jq("#endSessionButton").focus().click();
                return 0;
            };
        }, 1000);
        return -1;
    } else if(currentDate<newCheckInDate){
        return 1;
    } else{
        return 0;
    }
}
function validPatronItem(){
//hiddenLoanField_h6 mapped to overrideFlag
    if(jq("#hiddenLoanField_h6").val()=='true'){
        jq('body').scrollTop(0);
        jq(window.parent).scrollTop(0);
        displayDialogWindow("div#OverRideLogInSectionLink");
        return false;
    }

    // hiddenLoanField_h2 mapped to renewalFlag.
    //hiddenLoanField_h9 mapped to success
    if(jq("#hiddenLoanField_h9").val()=='false' && jq("#hiddenLoanField_h2").val()=='false'){
        playAudio();
        jq('body').scrollTop(0);
        jq(window.parent).scrollTop(0);
        displayDialogWindow("div#MessagePopupSection");
        jq('body').scrollTop(0);
        jq(window).scrollTop(0);
        if (jq("loanClaimsOption_control_0")) {
            jq("#loanClaimsOption_control_0").attr('checked', true);
        }
        return false;
    }

    // hiddenLoanField_h2 mapped to renewalFlag. && jq("#hiddenLoanField_h2").val()=='true'
    //hiddenLoanField_h9 mapped to  success
    if(jq("#hiddenLoanField_h9").val()=='false'){
        playAudio();
        jq('body').scrollTop(0);
        jq(window.parent).scrollTop(0);
        displayDialogWindow("div#MessagePopupSection");
        jq('body').scrollTop(0);
        jq(window).scrollTop(0);
        return false;
    }
    //hiddenLoanField_h25 mapped to claimsReturnFlag
    if(jq("#hiddenLoanField_h25").val()=='true'){
        jq('body').scrollTop(0);
        jq(window.parent).scrollTop(0);
        displayDialogWindow("div#ClaimsReturn-HorizontalBoxSection" );
        return false;
    }

    //hiddenLoanField_h23 mapped to realPatronFlag
    if(jq("#hiddenLoanField_h23").val()=='true'){
        jq('body').scrollTop(0);
        jq(window.parent).scrollTop(0);
        displayDialogWindow("div#RealPatronSection-HorizontalBoxSection");
        return false;
    }


    //hiddenLoanField_h7 mapped to patronNoteFlag
    if(jq("#hiddenLoanField_h7").val()=='true'){
        jq('body').scrollTop(0);
        jq(window.parent).scrollTop(0);
        displayDialogWindow( "div#PatronUserNote-HorizontalBoxSection" );
        return false;
    }

    displayOnHoldRequest();

    displayDueDateInfo();
    playAudio();
    focusItem();

}

function displayOnHoldRequest(){
    //hiddenLoanField_h35 mapped to onHoldRequestMessage
    if(jq("#hiddenLoanField_h35").val()!=null && jq("#hiddenLoanField_h35").val()!='') {
        jq('body').scrollTop(0);
        jq(window.parent).scrollTop(0);
        displayDialogWindow("div#PatronOnHoldItem-HorizontalBoxSection");
        return false;
    }
}

function closeOnHoldRequestDialog(){
    jq("div#PatronOnHoldItem-HorizontalBoxSection").fadeOut(300);
    jq('#mask').fadeOut(300);
}



function setRenewalDueDate(){
    if(jq( "#popUpDate_control").val() != null){
        jq( "#hiddenLoanField_h17").val(jq( "#popUpDate_control").val());
    }
    jq('#hdnRenewalBtn').focus().click();
}


//hiddenLoanField_h34 mapped to successMessage
function displayDueDateInfo(){
    if(jq("#hiddenLoanField_h34").val() !=null && jq("#hiddenLoanField_h34").val()!=""){
        jq('body').scrollTop(0);
        jq(window.parent).scrollTop(0);
        displayDialogWindow("div#DueDateInformationSection");
    }
}
//#hiddenReturnField_h3 mapped to returnSuccess
//#hiddenReturnField_h2 mapped to checkOut
function validateItem(){
    if(jq("#hiddenReturnField_h3").val()=='false' && jq("#hiddenReturnField_h2").val()=="true"){
        playAudio();
        jq('body').scrollTop(0);
        jq(window.parent).scrollTop(0);
        displayDialogWindow("div#MessagePopupSectionForLoan" );
        jq('body').scrollTop(0);
        jq(window.parent).scrollTop(0);
    }
    //#hiddenReturnField_h1 mapped to audioEnable
    //#hiddenReturnField_h3 mapped to returnSuccess
    //#hiddenReturnField_h5 mapped to numberOfPieces
    //#hiddenReturnField_h8 mapped to billAvailability
    //#hiddenReturnField_h10 mapped to checkInNoteExists
    //#hiddenReturnField_h11 mapped to damagedCheckIn
    if(jq("#hiddenReturnField_h3").val()=='false' && (jq("#hiddenReturnField_h5").val()=="true"||jq("#hiddenReturnField_h10").val()=="true"||jq("#hiddenReturnField_h8").val()=="true"||jq("#hiddenReturnField_h1").val()=="false" || jq("hiddenReturnField_h11").val()=="true")){
    //hiddenLoanField_h18 mapped to backGroundCheckIn    
        playAudio();
        if(jq("#hiddenLoanField_h18").val()=="true"){
            setMissingPieceCount();
            jq('body').scrollTop(0);
            jq(window.parent).scrollTop(0);
            displayDialogWindow("div#MessagePopupSectionForBackGroundReturn" );
            if (jq("input#OLELoanView-MissingPiece-RecordNote_control_0") != undefined) {
                jq("input#OLELoanView-MissingPiece-RecordNote_control_0").attr('checked', true);
            }
        }else{
            setMissingPieceCount();
            jq('body').scrollTop(0);
            jq(window.parent).scrollTop(0);
            displayDialogWindow("div#MessagePopupSectionForReturn" );
            jq('body').scrollTop(0);
            jq(window.parent).scrollTop(0);
            if (jq("returnClaimsOption_control_0")) {
                jq("#returnClaimsOption_control_0").attr('checked', true);
            }
            if (jq("input#OLEReturnView-MissingPiece-RecordNote_control_0") != undefined) {
                jq("input#OLEReturnView-MissingPiece-RecordNote_control_0").attr('checked', true);
            }
        }
    }
    dateValidation();
    validateCheckInTime();
    displayMissingDamagedBox();
    if(jq("#hiddenReturnField_h10").val()=="false"){//hiddenReturnField_h10 mapped to checkInNoteExists
        playAudio();
        printBill();
    }
}
function closeDueDateInfo(){
    jq("div#DueDateInformationSection").fadeOut(300);
    jq('#mask').fadeOut(300);

}
function dateValidation(){
    var diff = validateDate(jq("#hiddenReturnField_h7").val(), jq("#CheckInDate_control").val());//hiddenReturnField_h7 mapped to currentDate
    if(diff==-1){
        jq("div#AlertBoxSectionForDate-HorizontalBoxSection").fadeIn(300);
    }else if(diff==1){
        jq("#CheckInDate_control").val('');
        jq("#CheckInDate_control").focus();
        jq("div#AlertBoxSectionForDate-HorizontalBoxSection").fadeOut(300);
    }else{
        jq("div#AlertBoxSectionForDate-HorizontalBoxSection").fadeOut(300);
    }
}
function validationsForPop(){
    validPatronItem();
    validateItem();

}
//hiddenLoanField_h10 mapped to renewDueDateFlag
function validateRenew(){
    if(jq("#hiddenLoanField_h10").val()=='true'){
        jq('body').scrollTop(0);
        jq(window.parent).scrollTop(0);
        displayDialogWindow("div#RenewalDueDateSection-HorizontalBoxSection");
        jq('#mask').fadeOut(300);
        return false;
    }
    //hiddenLoanField_h3 mapped to overrideRenewItemFlag
    if(jq("hiddenLoanField_h3").val()=="true"){

    }
}
function doNotLoan(){
    jq('#hdnNoLoanBtn').select().focus();
    closeMessageBox();
}
function proceed(){
    if(jq( "#popUpDate_control").val() != null){
       jq( "#hiddenLoanField_h17").val(jq( "#popUpDate_control").val());
    }
    jq('#hdnProceedRenewBtn').focus().click();
    closeMessageBox();

}
function doNotProceed(){
    jq('#hdnNoProceedRenewBtn').focus().click();
    closeMessageBox();
}
//#hiddenReturnField_h4 mapped to returnCheck
function closeMessageBox(){
    jq("div#MessagePopupSection").fadeOut(300);
    jq('#mask').fadeOut(300);
    jq("#hiddenReturnField_h4").val("false");
    focusItem();
}

function closeOverrideDialog(){
    jq("div#OverRideLogInSectionLink").fadeOut(300);
    jq('#mask').fadeOut(300);
}

//hiddenLoanField_h19 mapped to missingPieceDialog
//hiddenLoanField_h20 mapped to damagedItemDialog
function focusItem(){
    jq("input#hiddenLoanField_h20").val('false');
    jq("input#hiddenLoanField_h19").val('false');
    jq('#Patron-item_control').select().focus();

}

function focusPatron(){
    jq('#Patron-barcode_control').select().focus();
}
//hiddenReturnField_h4 mapped to returnCheck
function closeMessageBoxForReturn(){
    jq("div#MessagePopupSectionForReturn").fadeOut(300);
    jq('#mask').fadeOut(300);
    validationsForPop();
    jq( "#CheckInItem_control").select().focus();
    jq("#hiddenReturnField_h4").val("true");
}
//hiddenLoanField_h24 mapped to changeLocationFlag
function changeLocation(){
    if(jq("#hiddenLoanField_h24").val()=='true'){
        jq('body').scrollTop(0);
        jq(window.parent).scrollTop(0);
        displayDialogWindow( "div#ConfirmCirculationLocationChange" );
    }
}
function sessionFadeOut(){
    jq('#mask').fadeOut(300);
}
function changeReturnLocation(){
    if(jq("#hiddenLoanField_h24").val()=='true'){
        jq('body').scrollTop(0);
        jq(window.parent).scrollTop(0);
        displayDialogWindow( "div#ConfirmReturnCirculationLocationChange" );
        dateValidation();
        validateCheckInTime();
    }
}

//hiddenReturnField_h9 mapped to oleFormKey
function printBill(){
    if(jq("#hiddenLoanField_control").val()=="true"){
        window.open("loancontroller?viewId=PatronItemView&methodToCall=printLoanBill&formKey="+jq("#hiddenReturnField_h9").val());
        jq("#hiddenLoanField_control").val("false");
    }
  //hiddenReturnField_h6 mapped to billAvailability
    //hiddenReturnField_h9 mapped to oleFormKey
    //hiddenLoanField_h18 mapped to backGroundCheckIn
    if(jq("#hiddenReturnField_h6").val()=="true" && jq("#hiddenLoanField_h18").val()=="false"){
        window.open("loancontroller?viewId=PatronItemView&methodToCall=printBill&formKey="+jq("#hiddenReturnField_h9").val());
        jq("#hiddenReturnField_h6").val("false");     
        setTimeout(function(){
            jq("button#refreshExport_button").focus().click();
        },300);

    }
}
//hiddenReturnField_h9 mapped to oleFormKey
//hiddenReturnField_h14 mapped to holdSlip
function printHoldSlips(){
    if(jq("#hiddenReturnField_h14").val()=="true"){
        window.open("loancontroller?viewId=PatronItemView&methodToCall=printHoldSlips&formKey="+jq("#hiddenReturnField_h9").val());
        jq("#hiddenReturnField_h14").val("false");
    }
}
//hiddenReturnField_h4 mapped to returnCheck
//#hiddenReturnField_h1 mapped to audioEnable

function playAudio(){
    if(jq("#hiddenReturnField_h4").val()=='true') {
        var audio = jq("#sound")[0];
        if(jq("#hiddenLoanField_h1").val()=="true"){
            audio.play();
            jq("#hiddenLoanField_h1").val("false");
        }
    }
    else{
        //hiddenLoanField_h1 is mapped to audioEnable.
        if(jq("#hiddenLoanField_h1").val()=="true"){
            var audio = jq("#sound")[0];
            audio.play();
            jq("#hiddenLoanField_h1").val("false");
        }
    }

}
function backGroundCheckOut(){
    printBill();
    playAudio();
}

function setCheckinItemFocus(){
    if(jq("#okCheckInNoteBtn").val() == undefined && jq("#returnBtn").val() == undefined && jq("#okClaimsBtn").val() == undefined && jq("#continueBtn").val() == undefined && jq("#OLEReturnView-DamagedItem-Loan").val() == undefined && jq("#returnLoanBtn").val() == undefined && jq("#closeBtn").val() == undefined){
        jq('#CheckInItem_control').select().focus();
    }else{
        jq("#CheckInItem_control").blur();
    }
}

function callFastAddClose(){
    if(jq("#loanItemFlag_control").val()=="false"){
        jq("#closeFastAddItemBtn").focus().click();
    }
}

function setItemBarcode(){
    if(jq("#FastAddMessageFieldSection > .uif-message").text()==""){
      parent.jq.fancybox.close();
      parent.jq("#FastAddItemBarcode").focus().click();
    }

}
//hiddenLoanField_h27 mapped to loanLoginName
//hiddenLoanField_h28 mapped to validLogin
function refreshLoanScreen(){
    if(jq( "#hiddenLoanField_h27").val() != null && jq( "#hiddenLoanField_h27").val() != "" && jq("#hiddenLoanField_h28").val()=="true"){
        if(jq(parent.document).find("#login-info").children('strong').eq(1).length>0){
            jq(parent.document).find("#login-info").children('strong').eq(1).html("  Impersonating User: "+jq( "#hiddenLoanField_h27").val());
        } else{
            jq(parent.document).find("#login-info").html(jq(parent.document).find("#login-info").html()+("<strong>  Impersonating User: "+jq( "#hiddenLoanField_h27").val()+"</strong>"));
        }
        jq(parent.document).find(".searchbox").val("");
      //  parent.location.reload();
    }
}

function closeMessageBoxForBackGroundReturn(){
    jq("div#MessagePopupSectionForBackGroundReturn").fadeOut(300);
    jq('#mask').fadeOut(300);
    validationsForPop();
}
//hiddenLoanField_h29 mapped to loginUser
function refreshScreen(){
    if(jq( "#hiddenLoanField_h29").val() != null && jq( "#hiddenLoanField_h29").val() != ""){
        if(jq(parent.document).find("#login-info").children('strong').eq(1).length>0){
            jq(parent.document).find("#login-info").children('strong').eq(1).html("  Impersonating User: "+jq( "#hiddenLoanField_h29").val());
        }
    }
}
//hiddenReturnField_15 mapped to itemDamagedStatus
//hiddenReturnField_1 mapped to skipDamagedCheckIn
function checkDamagedCheckIn(){
    if(jq("#hiddenReturnField_h15").val()=='true' && jq("#hiddenReturnField_h1").val()=='false'){
        jq('body').scrollTop(0);
        jq(window.parent).scrollTop(0);
        displayDialogWindow("div#OLEReturnView-DamagedItem-MessageBox");
        if (jq("input#OLEReturnView-DamagedItem-RecordNote_control_0") != undefined) {
            jq("input#OLEReturnView-DamagedItem-RecordNote_control_0").attr("checked", "true");
        }
    }
}

function closeDamagedCheckIn(){
    jq("div#OLEReturnView-DamagedItem-MessageBox").fadeOut(300);
    jq('#mask').fadeOut(300);
}
//hiddenReturnField_h1 mapped to skipDamagedCheckIn
function continueDamagedCheckIn(){
    jq("div#OLEReturnView-DamagedItem-MessageBox").fadeOut(300);
    jq('#mask').fadeOut(300);
    jq("input#hiddenReturnField_h1").val('true');
    jq('#validateItem').trigger('click');
}

function setMissingPieceCount(){
    if(jq("select#matchCheck_control option:selected").val()=='true'){
        jq("div#ReturnView-missingPieceCount").hide();
        jq("div#descText").hide();
        jq("div#OLEReturnView-MissingPiece-RecordNote").hide();
    }
    jq("select#matchCheck_control").live('change',function() {
        if(jq("select#matchCheck_control option:selected").val()=='false'){
            jq("div#ReturnView-missingPieceCount").show();
            jq("div#OLEReturnView-MissingPiece-RecordNote").show();
            jq("div#descText").show();
        } else {
            jq("div#ReturnView-missingPieceCount").hide();
            jq("div#descText").hide();
            jq("div#OLEReturnView-MissingPiece-RecordNote").hide();
        }
    })
    if(jq("select#matchCheck_loan_control option:selected").val()=='true'){
        jq("div#LoanView-missingPieceCount").hide();
        jq("div#descText_loan").hide();
        jq("div#OLELoanView-MissingPiece-RecordNote").hide();
    }
    jq("select#matchCheck_loan_control").live('change',function() {
        if(jq("select#matchCheck_loan_control option:selected").val()=='false'){
            jq("div#LoanView-missingPieceCount").show();
            jq("div#descText_loan").show();
            jq("div#OLELoanView-MissingPiece-RecordNote").show();
        } else {
            jq("div#LoanView-missingPieceCount").hide();
            jq("div#descText_loan").hide();
            jq("div#OLEReturnView-MissingPiece-RecordNote").hide();
        }
    })
}
//hiddenLoanField_h19 mapped to missingPieceDialog
function showMissingPieceDialog() {
    if (jq("input#hiddenLoanField_h19").val() == 'true') {
        jq('body').scrollTop(0);
        jq(window.parent).scrollTop(0);
        displayDialogWindow("div#MissingPiece-HorizontalBoxSection");
    } else{
        deactivateAllDialogs();
    }
}
//hiddenLoanField_h20 mapped to damagedItemDialog
function showDamagedItemDialog() {
    if (jq("input#hiddenLoanField_h20").val() == 'true') {
        jq('body').scrollTop(0);
        jq(window.parent).scrollTop(0);
        displayDialogWindow("div#DamagedItem-HorizontalBoxSection");
    }
}
//hiddenLoanField_h20 mapped to damagedItemDialog
function closeDamagedItemDialog() {
    jq("div#DamagedItem-HorizontalBoxSection").fadeOut(300);
    jq('#mask').fadeOut(300);
    jq("input#hiddenLoanField_h20").val('false');
}
//hiddenLoanField_h19 mapped to missingPieceDialog
function closeMissingPieceDialog() {
    jq("div#MissingPiece-HorizontalBoxSection").fadeOut(300);
    jq('#mask').fadeOut(300);
    jq("input#hiddenLoanField_h19").val('false');
}

function deactivateAllDialogs() {
    closeDamagedItemDialog();
    closeMissingPieceDialog();
}

function closeDamagedItemBox(){
    jq("div#OLELoanView-RecordNote-Damaged-MessageBox").fadeOut(300);
    jq('#mask').fadeOut(300);
}

function closeMissingPieceBox(){
    jq("div#OLELoanView-RecordNote-MissingPiece-MessageBox").fadeOut(300);
    jq('#mask').fadeOut(300);
}
//hiddenLoanField_h34 mapped to displayDamagedRecordNotePopup
function displayMissingDamagedBox(){
    if (jq("input#hiddenLoanField_h31").val() == 'true') {
        jq('body').scrollTop(0);
        jq(window.parent).scrollTop(0);
        displayDialogWindow("div#OLELoanView-RecordNote-Damaged-MessageBox");
        if (jq("input#OLELoanView-RecordNote-Damaged-MessageBoxOption_control_0") != undefined) {
            jq("input#OLELoanView-RecordNote-Damaged-MessageBoxOption_control_0").attr("checked", "true");
         }
    }
    //hiddenLoanField_h30 mapped to displayMissingPieceNotePopup
    else if (jq("input#hiddenLoanField_h30").val() == 'true') {
        jq('body').scrollTop(0);
        jq(window.parent).scrollTop(0);
        displayDialogWindow("div#OLELoanView-RecordNote-MissingPiece-MessageBox");
        if (jq("input#OLELoanView-RecordNote-MissingPiece-MessageBoxOption_control_0") != undefined) {
            jq("input#OLELoanView-RecordNote-MissingPiece-MessageBoxOption_control_0").attr("checked", "true");
        }
    }else {
        displayDueDateInfo();
    }
}

function ValidateTime(inputVal){
    var t = inputVal.split(':');
    var regxWithSeconds=/^\d\d:\d\d:\d\d$/;
    var regxWithOutSeconds=/^\d\d:\d\d$/;
    if(regxWithOutSeconds.test(inputVal)){
       return t[0] >= 0 && t[0] < 25 && t[1] >= 0 && t[1] < 60;
    } else if(regxWithSeconds.test(inputVal)){
        return t[0] >= 0 && t[0] < 25 && t[1] >= 0 && t[1] < 60 && t[2] >= 0 && t[2] < 60;
    } else {
         return false;
    }
}

function validateCheckInTime(){
    var inputVal = jq('#CheckInTime_control').val();
    var d=new Date();
    jq("div#AlertBoxSectionForInvalidTime-HorizontalBoxSection").fadeOut(300);
    if (!jq.trim(inputVal)=="") {
        if (!ValidateTime(inputVal)) {
            jq("div#AlertBoxSectionForInvalidTime-HorizontalBoxSection").fadeIn(300);
            jq("div#AlertBoxSectionForTime-HorizontalBoxSection").fadeOut(300);
        } else {
            var end = d.getHours() + ":" + d.getMinutes();
            var start = inputVal;
            s = start.split(':');
            e = end.split(':');
            min = e[1]-s[1];
            hour = e[0]-s[0];
            hour_carry = 0;
            if(min < 0){
                min += 60;
                var min_carry=hour*60;
                min_carry=min_carry-min;
                min = min_carry % 60 ;
                hour = Math.floor(min_carry/ 60);
            }
            diff = hour + ":" + min;
            if(hour>0 && hour<10){
                hour="0"+hour;
            }
            if(min>0 && min<10){
                min="0"+min;
            }
            var total_min=hour*60+min;
            if(total_min!=0){
                var diff = validateDate(jq("#hiddenReturnField_h7").val(), jq("#CheckInDate_control").val());//hiddenReturnField_h7 mapped for currentDate
                if (diff == 0) {
                    if (jq("#hiddenReturnField_h0").val() == "true") {
                        jq("#hiddenLoanField_h1").val("true");//#hiddenReturnField_h1 mapped to audioEnable
                    }
                    playAudio();
                    jq("div#AlertBoxSectionForTime-HorizontalBoxSection").fadeIn(300);
                }
            }
        }
    } else{
        jq("div#AlertBoxSectionForInvalidTime-HorizontalBoxSection").fadeOut(300);
        jq("div#AlertBoxSectionForTime-HorizontalBoxSection").fadeOut(300);
        var diff = validateDate(jq("#hiddenReturnField_h7").val(), jq("#CheckInDate_control").val());/*hiddenReturnField_h7 mapped to currentDate*/
        if(diff==0){
            jq("div#AlertBoxSectionForDate-HorizontalBoxSection").fadeOut(300);
        }
    }
}
function refreshLoanList(){
    jq('#OLEReturnView-refreshReturnLoanList').focus().click();

}
//hiddenReturnField_h7 mapped for currentDate
//#hiddenReturnField_h1 mapped to audioEnable
function refreshExport(){
    var diff = validateDate(jq("#hiddenReturnField_h7").val(), jq("#CheckInDate_control").val());
    if(diff==-1){
        // hiddenReturnField_h0 mapped to audioForPastDate
        if(jq("#hiddenReturnField_h0").val()=="true"){
            jq("#hiddenLoanField_h1").val("true");
        }
        playAudio();
        jq("div#AlertBoxSectionForDate-HorizontalBoxSection").fadeIn(300);
        jq("div#AlertBoxSectionForTime-HorizontalBoxSection").fadeOut(300);
    }else if(diff==1){
        jq("#CheckInDate_control").val('');
        jq("#CheckInDate_control").focus();
        jq("div#AlertBoxSectionForDate-HorizontalBoxSection").fadeOut(300);
    }else{
        jq("div#AlertBoxSectionForDate-HorizontalBoxSection").fadeOut(300);
        validateCheckInTime();
    }
}