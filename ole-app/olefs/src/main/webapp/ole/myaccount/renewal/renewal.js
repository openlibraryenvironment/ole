

jq(document).ready(function(){
    //jq("#Patron-barcode").focus();
    jq("#OleMyAccRequestView-RequestRecords_group > span").html("");
    jq("#MyAccountTabSection_div").width(jq("form").width());

    jq('#RenewalPatron-barcode_control').live("keypress", function(e) {

        if(e.which == 13) {
            if(jq( "#RenewalPatron-barcode_control").val()!=""){
            jq(this).blur();

            jq('#MyAccountSecondarySearchPatron').focus().click();
            }else{

                jq("#RenewalPatron-barcode_control").focus();
                return false;
            }
        }
    });

    jq("#OlePatronView_tab").live("click",function(){
        jq("#ShowReturnMyAccount_control").val("false")
    });

    jq("#RenewalViewPage_tab").live("click",function(){
        jq("#ShowReturnMyAccount_control").val("true");
        jq("#MyAccountTabSection_tabs").tabs("select","#RenewalViewPage_tab");
    });

    if(jq("#RenewalPatron-ExistingLoanItemListSection-HorizontalBoxSection_div").length > 0) {
        jq("#RenewalPatron-ExistingLoanItemListSection-HorizontalBoxSection_div th.col1").children().append("<input type='checkbox' value='false' class='selectAllLoanedCB' name='selectLoanedAll' id='selectLoanedAll' style='float:left;margin-left:0.5em'>");
    }

    jq(".selectAllLoanedCB").live("click",function(){
        if(jq(".selectAllLoanedCB").is(":checked")){
            jq(".loanedItemCBClass").attr("checked","true");
        }else{
            jq(".loanedItemCBClass").removeAttr("checked");
        }
    });

    if(jq("#ShowReturnMyAccount_control").val()=="true"){
        jq("#MyAccountTabSection_tabs").tabs("select","#RenewalViewPage_tab");
    }

   /* jq('#RenewalPatron-item_control').live("keypress", function(e) {

        if(e.which == 13) {
            if(jq("#RenewalPatron-item_control").val()!=""){
            jq(this).blur();
            jq('#RenewalSecondarySearchItem').focus().click();
            }else{

                jq('#RenewalPatron-item_control').focus();
                return false;
            }
        }
    });*/
    jq(document).keypress(function(e) {
        if(e.which == 13) {
            if(e.target.id == "RenewalPatron-item_control"){
                if(jq("#RenewalPatron-item_control").val()!=""){
                    jq(this).blur();
                    jq('#RenewalSecondarySearchItem').focus().click();
                }else{

                    jq('#RenewalPatron-item_control').focus();
                    return false;
                }
            }
            return false;
        }
    });
});

function renewTab(){
    jq("#MyAccountTabSection_tabs").tabs("select","#RenewalViewPage_tab");
}




