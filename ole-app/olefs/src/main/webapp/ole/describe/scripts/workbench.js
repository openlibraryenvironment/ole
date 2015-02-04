/**
 * Created with IntelliJ IDEA.
 * User: meenau
 * Date: 7/25/13
 * Time: 4:24 PM
 * To change this template use File | Settings | File Templates.
 */
//for bib
function viewWorkbenchBibView(docFormat,docId,bibId){
    window.open("editorcontroller?viewId=EditorView&methodToCall=load&docCategory=work&docType=bibliographic&docFormat="+docFormat+"&docId="+docId+"&bibId="+bibId+"&editable=false");
    return false;

}

function viewWorkbenchBibEdit(docFormat,docId,bibId){
    window.open("editorcontroller?viewId=EditorView&methodToCall=load&docCategory=work&docType=bibliographic&docFormat="+docFormat+"&docId="+docId+"&bibId="+bibId+"&editable=true&fromSearch=true");
    return false;
}

function viewWorkbenchBibOverlay(docId,bibId){
    window.open("importBibController?viewId=ImportBibView&methodToCall=overLay&docCategory=work&docType=bibliographic&docFormat=marc&docId="+docId+"&bibId="+bibId);
    return false;
}

function viewWorkbenchBibCreateInstance(bibId){
    window.open("editorcontroller?viewId=EditorView&methodToCall=load&bibId="+bibId+"&docCategory=work&docType=holdings&docFormat=oleml&editable=true");
    return false;
}

//for holdings
function viewWorkbenchHoldingsView(docId,bibId,instanceId){
    window.open("editorcontroller?viewId=EditorView&methodToCall=load&docCategory=work&docType=holdings&docFormat=oleml&docId="+docId+"&bibId="+bibId+"&instanceId="+instanceId+"&editable=false");
    return false;

}

function viewWorkbenchHoldingsEdit(docId,bibId,instanceId){
    window.open("editorcontroller?viewId=EditorView&methodToCall=load&docCategory=work&docType=holdings&docFormat=oleml&docId="+docId+"&bibId="+bibId+"&instanceId="+instanceId+"&editable=true&fromSearch=true");
    return false;
}

function viewWorkbenchHoldingsShowBib(docId,instanceId){
    window.open("editorcontroller?viewId=ShowBibView&methodToCall=showBibs&docId="+docId+"&instanceId="+instanceId+"&docCategory=work&docType=holdings&docFormat=oleml&editable=true");
    return false;
}

function viewBoundWithRelation(holdingsId) {
    window.open("editorcontroller?viewId=ShowBibView&methodToCall=showBibs&holdingsId="+holdingsId+"&docCategory=work&docType=holdings&docFormat=oleml&editable=true");
    return false;
}
function viewSeriesHoldingRelation(holdingsId){
    window.open("analyticsController?viewId=AnalyticsSummaryView&methodToCall=showAnalyticsSummary&holdingsId="+holdingsId+"&docType=holdings&");

    return false;
}
//for item

function viewWorkbenchItemView(docId,bibId,instanceId){
    window.open("editorcontroller?viewId=EditorView&methodToCall=load&docCategory=work&docType=item&docFormat=oleml&docId="+docId+"&bibId="+bibId+"&instanceId="+instanceId+"&editable=false");
    return false;

}

function viewWorkbenchItemEdit(docId,bibId,instanceId){
    window.open("editorcontroller?viewId=EditorView&methodToCall=load&docCategory=work&docType=item&docFormat=oleml&docId="+docId+"&bibId="+bibId+"&instanceId="+instanceId+"&editable=true&fromSearch=true");
    return false;

}

function viewWorkbenchItemShowBib(instanceId){
    window.open("ole-kr-krad/editorcontroller?viewId=ShowBibView&methodToCall=showBibs&instanceId="+instanceId+"&docCategory=work&docType=item&docFormat=oleml&editable=true");
    return false;
}

function viewAnalyticItemRelation(itemIdentifier){
    window.open("analyticsController?viewId=AnalyticsSummaryView&methodToCall=showAnalyticsSummary&itemId="+itemIdentifier+"&docType=item&");
}

//for EHoldings

function viewWorkbenchEHoldingsView(docId,bibId,holdingsId){
    window.open("editorcontroller?viewId=EditorView&methodToCall=load&docCategory=work&docType=eHoldings&docFormat=oleml&docId="+docId+"&editable=false&bibId="+bibId+"&holdingsId="+holdingsId);
    return false;

}

function viewWorkbenchEHoldingsEdit(docId,bibId,holdingsId){
    window.open("editorcontroller?viewId=EditorView&methodToCall=load&docCategory=work&docType=eHoldings&docFormat=oleml&docId="+docId+"&editable=true&fromSearch=true&bibId="+bibId+"&holdingsId="+holdingsId);
    return false;

}

jq("#SearchPanel-docType-Section").live("change",function() {
    jq("div#DescribeWorkBenchViewPage").each(function(){
        if(jq(this).find("div.uif-validationMessages")){
            jq(this).find("div.uif-validationMessages").remove();
        }
    });
    jq('#hdn_refresh_button').focus().click();
    jq('#hidden_refresh_button').focus().click();
});


jq("#GlobalEditView-Type-Section").live("change",function() {
    jq("div#DescribeWorkBenchViewPage").each(function(){
        if(jq(this).find("div.uif-validationMessages")){
            jq(this).find("div.uif-validationMessages").remove();
        }
    });
    jq('#GlobalEditView_hidden_button').focus().click();
});

function displayDialogWindow(divID) {
    jq(divID).fadeIn(300);
    var popMargTop = (jq(divID).height() + 24) / 2;
    var popMargLeft = (jq(divID).width() + 24) / 2;
    jq(divID).css({
        'margin-top': -popMargTop,
        'margin-left': -popMargLeft
    });
    jq(divID).draggable();
    jq('body').append('<div id="mask"></div>');
    jq('#mask').fadeIn(300);
}

function refresh(){
    window.setTimeout(function(){
        jq('#hdn_refreshLeft_button').focus().click();
        jq('#hdn_refreshRight_button').focus().click();
    },0);
    searching();
}

jq(document).ready(function () {
    // jq("#SearchConditions_SearchScope_id_line0_control_0").attr("checked","true");
    jq('#hdnStart_control').val(0);
    jq('#hdnRows_control').val(jq(".dataTables_length select").val());

    searchShowEntries();

    jq(document).click(function(e){
        if (jq(e.target).is('#BoundwithTree2TabSection_tabs,#BoundwithTree2TabSection_tabs *,#BoundwithTreeSection1_tabs,#BoundwithTreeSection1_tabs *,#TransferSection1_tabs *,#TransferSection2_tabs *')) {
            window.setTimeout(function () {
                searchShowEntries();
            }, 800)
        }

    });
});


jq(window).load(function () {
    jq("#SearchPanel-docType-Section_control_0").attr ('checked',true);
    jq(".dataTables_length select").live("change", function () {
        localStorage.localAscSort = true;
        localStorage.localDescSort = true;
        var rows = jq(".dataTables_length select").val();
        jq("#hdnRows_control").val(rows);
        jq("#hdnSortFlag_control").val("true");
        //jq("#hdnSortOrder_control").val("");
        // jq("#hdnSortField_control").val("");
        jq("#hdn_search_button").focus().click();
        jq("#hiddenRows_control").val(rows);
        jq("#hiddenSortFlag_control").val("true");
        jq("#hidden_search_button").focus().click();
    });
    jq(".paginate_enabled_next").live("click", function () {
        localStorage.localAscSort = true;
        localStorage.localDescSort = true;
        jq('#hdn_NextFlag_button').focus().click();
        jq('#hidden_NextFlag_button').focus().click();
    });
    jq(".paginate_enabled_previous").live("click", function () {
        localStorage.localAscSort = true;
        localStorage.localDescSort = true;
        jq('#hdn_PreviousFlag_button').focus().click();
        jq('#hidden_PreviousFlag_button').focus().click();
    });
    jq("#search_button").live("click",function(){
        localStorage.localAscSort = true;
        localStorage.localDescSort = true;
        jq('#hdn_search_button').focus().click();
    });

//    jq("#SearchButton").on("click",function(){
//        localStorage.localAscSort = true;
//        localStorage.localDescSort = true;
//        jq('#hidden_search_button').focus().click();
//    });

    jq(".sorting").live("click",function(){
        commonShowEntries();
    });

    jq(".sorting_asc").live("click",function(){
        if(localStorage.localAscSort == "true") {
            if(jq(this).attr("aria-label") != null){
                var sortAttr= jq(this).attr("aria-label").split(":");
                jq("#hdnSortOrder_control").val("asc");
                jq("#hdnSortField_control").val(sortAttr[0]);
                jq("#hiddenSortOrder_control").val("asc");
                jq("#hiddenSortField_control").val(sortAttr[0]);
            }
            localStorage.indexOfTableSorting = jq(this).index();
            localStorage.classValueToReplace = "sorting_asc";
            jq("#hdnSortFlag_control").val("true");
            jq('#hdn_search_button').focus().click();
            jq("#hiddenSortFlag_control").val("true");
            jq('#hidden_search_button').focus().click();
        }
        else {
            localStorage.localAscSort = true;
            localStorage.localDescSort = true;
            commonShowEntries();
        }
    });

    jq(".sorting_desc").live("click",function(){
        if(localStorage.localDescSort == "true") {
            if(jq(this).attr("aria-label") != null){
                var sortAttr= jq(this).attr("aria-label").split(":");
                jq("#hdnSortOrder_control").val("desc");
                jq("#hdnSortField_control").val(sortAttr[0]);
                jq("#hiddenSortOrder_control").val("desc");
                jq("#hiddenSortField_control").val(sortAttr[0]);
            }
            localStorage.indexOfTableSorting = jq(this).index();
            localStorage.classValueToReplace = "sorting_desc";
            jq("#hdnSortFlag_control").val("true");
            jq('#hdn_search_button').focus().click();
            jq("#hiddenSortFlag_control").val("true");
            jq('#hidden_search_button').focus().click();

        }
        else {
            localStorage.localAscSort = true;
            localStorage.localDescSort = true;
            commonShowEntries();
        }
    });
});

function submitSearching(){
    localStorage.classValueToReplace = "";
    localStorage.localAscSort = true;
    localStorage.localDescSort = true;
    jq("#hdnSortFlag_control").val("false");
    jq("#hdnSortOrder_control").val("");
    jq("#hdnSortField_control").val("");
    searching();
}

function searching() {
    jq("#hdnSortFlag_control").val("false");
    searchShowEntries();
    window.setTimeout(function () {
        jq("#SearchResultsPanel_toggle").click(function(){
            if(jq("#SearchResultsPanel_disclosureContent").css("display")=="block"){
                    searchShowEntries();
            }
        });
        jq("#SearchPanel_toggle").click(function(){
            if(jq("#SearchPanel_disclosureContent").css("display")=="block"){
                    searchShowEntries();
            }

        });
        jq("#OLEERSSearchResultsPanel_toggle").click(function () {
            if (jq("#OLEERSSearchResultsPanel_disclosureContent").css("display") == "block") {
                    searchShowEntries();
            }
        });
        jq("#OLEERSSearchPanel_toggle").click(function () {
            if (jq("#OLEERSSearchPanel_disclosureContent").css("display") == "block") {
                    searchShowEntries();
            }
        });

        jq("#BoundwithSearchResultsPanel_toggle").click(function(){
            if(jq("#BoundwithSearchResultsPanel_disclosureContent").css("display")=="block"){
                    searchShowEntries();
            }
        });

        jq("#BoundwithSearchPanel_toggle").click(function(){
            if(jq("#BoundwithSearchPanel_disclosureContent").css("display")=="block"){
                    searchShowEntries();
            }
          });
    }, 500)
    var date =new Date();
    var endTime = date.getMilliseconds();
    var timeTaken = (endTime - localStorage.startTime)/1000;
    jq("#totalTime_control").text(" "+timeTaken+" ");

}


function searchShowEntries(){

    jq(".dataTables_length select").val(jq("#hiddenRows_control").val());
    window.setTimeout(function () {
        if (jq(".dataTables_info").text() != "") {
            jq(".dataTables_info").text(jq("#boundwithPageShowEntries_control").val());
            jq(".dataTables_info").text(jq("#workbenchPageShowEntries_control").val());
            jq(".dataTables_info").text(jq("#searchPageShowEntries_control").val());
        }

        if (jq("#boundwithNextFlag_control").val() == "true") {
            jq(".paginate_disabled_next").removeClass("paginate_disabled_next").addClass("paginate_enabled_next");
        }
        if (jq("#boundwithPreviousFlag_control").val() == "true") {
            jq(".paginate_disabled_previous").removeClass("paginate_disabled_previous").addClass("paginate_enabled_previous");
        }

        if (jq("#workbenchNextFlag_control").val() == "true") {
            jq(".paginate_disabled_next").removeClass("paginate_disabled_next").addClass("paginate_enabled_next");
        }
        if (jq("#workbenchPreviousFlag_control").val() == "true") {
            jq(".paginate_disabled_previous").removeClass("paginate_disabled_previous").addClass("paginate_enabled_previous");
        }

        if (jq("#searchNextFlag_control").val() == "true") {
            jq(".paginate_disabled_next").removeClass("paginate_disabled_next").addClass("paginate_enabled_next");
        }
        if (jq("#searchPreviousFlag_control").val() == "true") {
            jq(".paginate_disabled_previous").removeClass("paginate_disabled_previous").addClass("paginate_enabled_previous");
        }
        if (localStorage.classValueToReplace == "sorting_desc") {
            localStorage.classValueToReplace = "sorting_desc";
            jq("table#u216 thead tr th:eq(" + localStorage.indexOfTableSorting +")").removeClass("sorting").addClass("sorting_desc");
            jq("table#u353 thead tr th:eq(" + localStorage.indexOfTableSorting +")").removeClass("sorting").addClass("sorting_desc");
            jq("table#u496 thead tr th:eq(" + localStorage.indexOfTableSorting +")").removeClass("sorting").addClass("sorting_desc");
            jq("table#u659 thead tr th:eq(" + localStorage.indexOfTableSorting +")").removeClass("sorting").addClass("sorting_desc");
            localStorage.localAscSort = true;
            localStorage.localDescSort = true;
            commonShowEntries();
        }
        else  if (localStorage.classValueToReplace == "sorting_asc") {
            localStorage.localAscSort = true;
            localStorage.localDescSort = true;
            localStorage.classValueToReplace = "sorting_asc";
            jq("table#u216 thead tr th:eq("+  localStorage.indexOfTableSorting + ")").removeClass("sorting").addClass("sorting_asc");
            jq("table#u353 thead tr th:eq("+  localStorage.indexOfTableSorting + ")").removeClass("sorting").addClass("sorting_asc");
            jq("table#u496 thead tr th:eq("+  localStorage.indexOfTableSorting + ")").removeClass("sorting").addClass("sorting_asc");
            jq("table#u659 thead tr th:eq("+  localStorage.indexOfTableSorting + ")").removeClass("sorting").addClass("sorting_asc");
            jq(".sorting_desc").click();
        }
    }, 500)
}

function commonShowEntries(){

    if (jq(".dataTables_info").text() != "") {
        jq(".dataTables_info").text(jq("#boundwithPageShowEntries_control").val());
        jq(".dataTables_info").text(jq("#workbenchPageShowEntries_control").val());
        jq(".dataTables_info").text(jq("#searchPageShowEntries_control").val());
    }

    if (jq("#boundwithNextFlag_control").val() == "true") {
        jq(".paginate_disabled_next").removeClass("paginate_disabled_next").addClass("paginate_enabled_next");
    }
    if (jq("#boundwithPreviousFlag_control").val() == "true") {
        jq(".paginate_disabled_previous").removeClass("paginate_disabled_previous").addClass("paginate_enabled_previous");
    }

    if (jq("#workbenchNextFlag_control").val() == "true") {
        jq(".paginate_disabled_next").removeClass("paginate_disabled_next").addClass("paginate_enabled_next");
    }
    if (jq("#workbenchPreviousFlag_control").val() == "true") {
        jq(".paginate_disabled_previous").removeClass("paginate_disabled_previous").addClass("paginate_enabled_previous");
    }

    if (jq("#searchNextFlag_control").val() == "true") {
        jq(".paginate_disabled_next").removeClass("paginate_disabled_next").addClass("paginate_enabled_next");
    }
    if (jq("#searchPreviousFlag_control").val() == "true") {
        jq(".paginate_disabled_previous").removeClass("paginate_disabled_previous").addClass("paginate_enabled_previous");
    }

}

function globalEditInstance(docType){
    window.open("editorcontroller?viewId=EditorView&methodToCall=load&docCategory=work&docType=" +docType + "&docFormat=oleml&editable=true&fromSearch=true&pageId=WorkHoldingsViewPage");
}

function  openSelectAll(doctype) {
    var checkedcount=0;
    if (doctype == 'bibliographic'){
        var length = jq("#BibSearchResults tbody tr").length;
        for (var count = 0; count < length; count++) {
            if (jq("#BibSearchResults tbody tr:eq(" + count + ") td div input").is(':checked')) {
                window.open(jq("#BibSearchResults tbody tr:eq(" + count + ") td:eq(1) div a").attr("href") + "&pageId=WorkBibEditorViewPage");
                checkedcount+=1;
            }
        }
    }
    else if (doctype == 'holdings'){
        var length = jq("#HoldingsSearchResults tbody tr").length;
        for (var count = 0; count < length; count++) {
            if (jq("#HoldingsSearchResults tbody tr:eq(" + count + ") td div input").is(':checked')) {
                window.open(jq("#HoldingsSearchResults tbody tr:eq(" + count + ") td:eq(2) div a").attr("href") + "&pageId=WorkHoldingsViewPage");
                checkedcount+=1;
            }
        }
    }
    else if (doctype == 'item'){
        var length = jq("#ItemSearchResults tbody tr").length;
        for (var count = 0; count < length; count++) {
            if (jq("#ItemSearchResults tbody tr:eq(" + count + ") td div input").is(':checked')) {
                window.open(jq("#ItemSearchResults tbody tr:eq(" + count + ") td:eq(2) div a").attr("href") + "&pageId=WorkItemViewPage");
                checkedcount+=1;
            }
        }
    }
    else if (doctype == 'eHoldings'){
        var length = jq("#EHoldingsSearchResults tbody tr").length;
        for (var count = 0; count < length; count++) {
            if (jq("#EHoldingsSearchResults tbody tr:eq(" + count + ") td div input").is(':checked')) {
                window.open(jq("#EHoldingsSearchResults tbody tr:eq(" + count + ") td:eq(1) div a").attr("href") + "&pageId=WorkEInstanceViewPage");
                checkedcount+=1;
            }

        }
    }
    else {
        // alert("Undefined Doctype");
    }
    if(checkedcount == 0){
        alert("Please select atleast one record");
    }

}
function highLightNewSearch(id){
    var JSONObject = JSON.stringify(id.extraData);//.split("actionParameters[selectedLineIndex]");
    var obj = jQuery.parseJSON(JSONObject);

    for (key in obj) {
        if (key == "actionParameters[selectedLineIndex]")
            id = parseInt(obj[key]) + 1;
    }
    jq('#'+'items_line'+id).css({"background-color": "#FFFFD6", "border-color": "#000000"});
    jq('#'+'SearchConditions_SearchText_id_line'+id+'_control').focus();
    if(jq('#'+'SearchConditions_SearchText_id_line'+id+'_control').val()==null&&
        jq('#'+'SearchConditions_DocField_id_line'+id+'_control').val()==null){
        alert("Please enter values for the search condition.")
    }
}
function validateCheckBoxes() {
    jq('input:checkbox').click(function () {
        jq('input:checkbox').not(this).removeAttr('checked');
    });
}

function validLinkToOrder() {
    if (jq("div#success_message").text().trim() != "") {
        parent.window.close();
    }
    else if (jq.find("div#link_message") != "") {
        displayDialogWindow("div#MessagePopupSectionForLinkToOrder");
    }
}
function closeMessagePopUp() {
    jq('#mask').fadeOut(300);
    if (jq("div#success_message").text().trim() != "") {
        parent.window.close();
    }
}

function validLinkToEResource() {
    if (jq("#link_success_message").val() == "") {
        parent.window.close();
    }
}
function holdingsOReholdings() {
    displayDialogWindow("div#MessagePopupSectionForLinkToEResource");
}
function displayList() {
    if (jq("#ERRadio input[type='radio']:checked").val() == "Holdings") {
        jq('#ER1_button').focus().click();
    }
    else {
        jq('#ER2_button').focus().click();
    }
}
jq("#OLEERSSearchPanel-docType-Section").live("change", function () {
    jq("div#OLEEResourceBibViewPage").each(function () {
        if (jq(this).find("div.uif-validationMessages")) {
            jq(this).find("div.uif-validationMessages").remove();
        }
    });
    jq('#ers_hdn_refresh_button').focus().click();
});
function browse() {
    jq(".dataTables_wrapper").attr("style", "width:1200px;");
    displayDialogWindow("div#MessagePopupSectionForLinkToEResource");
    return true;
}
function openHelpWindow(url) {

    if(jq("input.uif-textControl").is(":focus")){
        jq('#SearchButton').click();

    }else{
        var windowWidth = screen.availWidth / 2;
        var windowHeight = screen.availHeight / 2;
        var windowPositionY = parseInt((screen.availWidth / 2) - (windowWidth / 2));
        var windowPositionX = parseInt((screen.availHeight / 2) - (windowHeight / 2));

        var windowUrl = url;
        var windowName = 'HelpWindow';
        var windowOptions = 'width=' + windowWidth + ',height=' + windowHeight + ',top=' + windowPositionX + ',left=' + windowPositionY + ',scrollbars=yes,resizable=yes';

        var myWindow = window.open('', windowName);
        myWindow.close();
        myWindow = window.open(windowUrl, windowName, windowOptions);
    }
}
function search() {
    var date =new Date();
    localStorage.startTime = date.getMilliseconds();
    jq("#hidden_search_button").focus().click();
}
