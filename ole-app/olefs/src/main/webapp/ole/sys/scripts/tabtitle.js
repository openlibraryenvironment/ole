/**
 * Created by premkb on 1/22/15.
 */

var jq = jQuery.noConflict();
jq(window).load(function(){
    if(document.title.indexOf("LookUp")<0 && document.title.indexOf("Lookup")<0 && (document.title.indexOf("Item Search")<0 || document.title.indexOf("Deliver Item Search")>0)){
        if(jq("h1").hasClass("uif-viewHeader")){
            title = jq("span:first").text();
            parent.document.title=title;
        }else if(jq("div").hasClass("uif-headerText")){
            title = jq("span:first").text();
            parent.document.title=title;
        }else if(jq("div").hasClass("headerarea")){
            title = jq("h1:first").text();
            //Added for Payment File Batch Upload screen
            if(title===null || title.length===0 || title.trim().length===0){
                title=jq("h2:first").text();
            }
            if(!isKNSTransaction(title)){//To display particular record inquired or edited in tabtitle and only for kns maintenance screen
                if(jq("div").hasClass("tab-container")){
                    title = title+' ('+jq("span:first").text().trim()+')';
                }
            }
            parent.document.title=title;
        }else if(jq("div").hasClass("headerarea-small")){
            title = jq("h1:first").text();
            parent.document.title=title;
        }else if(jq("span").hasClass("uif-headerText-span")){
            title = jq("span:first").text();
            //Added for Single Record Import screen
            if(title=='Import Bib Step-4 Bibliographic Editor'){
                title = 'Single Record Import';
            }
            parent.document.title=title;
        }
    }
});

/**
 * To avoid the display of () in transaction document which is used in maintenance document to display the particular inquired or edited object
 * @param documentName
 * @returns {boolean}
 */
function isKNSTransaction(documentName){
    var transactionDoc = ["Requisition","PurchaseOrder","FundLookup","VendorCreditMemo","BudgetAdjustment","TransferOfFunds","LineItemReceiving",
        "AcquisitionsSearch","DisbursementVoucher","DistributionOfIncomeAndExpense","GeneralErrorCorrection","GeneralLedgerCorrectionProcess","Pre-Encumbrance",
        "YearEndBudgetAdjustment","YearEndDistributionOfIncomeAndExpense","YearEndGeneralErrorCorrection"];
    documentName=documentName.replace(/\s+/g,'').trim();
    var index = jq.inArray(documentName.trim(),transactionDoc);
    if(index==-1){
        return false;
    }else{
        return true;
    }
}



