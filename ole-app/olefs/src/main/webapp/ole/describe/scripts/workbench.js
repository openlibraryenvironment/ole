/**
 * Created with IntelliJ IDEA.
 * User: meenau
 * Date: 7/25/13
 * Time: 4:24 PM
 * To change this template use File | Settings | File Templates.
 */
//for bib
var links = [];
var deLinks = [];
function viewWorkbenchBibView(docFormat, docId, bibId) {
    window.open("editorcontroller?viewId=EditorView&methodToCall=load&docCategory=work&docType=bibliographic&docFormat=" + docFormat + "&docId=" + docId + "&bibId=" + bibId + "&editable=false");
    return false;

}

function viewWorkbenchBibEdit(docFormat, docId, bibId) {
    window.open("editorcontroller?viewId=EditorView&methodToCall=load&docCategory=work&docType=bibliographic&docFormat=" + docFormat + "&docId=" + docId + "&bibId=" + bibId + "&editable=true&fromSearch=true");
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

function viewWorkbenchHoldingsShowBib(docId, instanceId) {
    window.open(getApplicationBanner()+"portal.do?channelTitle=Marc Editor&channelUrl="+getApplicationPath()+"editorcontroller?viewId=ShowBibView&methodToCall=showBibs&docId=" + docId + "&instanceId=" + instanceId + "&docCategory=work&docType=holdings&docFormat=oleml&editable=true");
    return false;
}

function viewBoundWithRelation(holdingsId) {
    window.open(getApplicationBanner()+"portal.do?channelTitle=Marc Editor&channelUrl="+getApplicationPath()+"editorcontroller?viewId=ShowBibView&methodToCall=showBibs&holdingsId="+holdingsId+"&docCategory=work&docType=holdings&docFormat=oleml&editable=true");
    return false;
}
function viewSeriesHoldingRelation(holdingsId){
    window.open(getApplicationBanner()+"portal.do?channelTitle=Marc Editor&channelUrl="+getApplicationPath()+"analyticsController?viewId=AnalyticsSummaryView&methodToCall=showAnalyticsSummary&holdingsId="+holdingsId+"&docType=holdings&");

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
    window.open(getApplicationBanner()+"portal.do?channelTitle=Marc Editor&channelUrl="+getApplicationPath()+"editorcontroller?viewId=ShowBibView&methodToCall=showBibs&instanceId="+instanceId+"&docCategory=work&docType=item&docFormat=oleml&editable=true");
    return false;
}

function viewAnalyticItemRelation(itemIdentifier){
    window.open(getApplicationBanner()+"portal.do?channelTitle=Marc Editor&channelUrl="+getApplicationPath()+"analyticsController?viewId=AnalyticsSummaryView&methodToCall=showAnalyticsSummary&itemId="+itemIdentifier+"&docType=item&");
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
    submitForm('start', null, null,null,null);
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

function refresh() {
    window.setTimeout(function () {
        jq('#hdn_refreshLeft_button').focus().click();
        jq('#hdn_refreshRight_button').focus().click();
    }, 0);
    searching();
}

jq(document).ready(function () {
    // jq("#SearchConditions_SearchScope_id_line0_control_0").attr("checked","true");
    jq('#hdnStart_control').val(0);
    jq('#hdnRows_control').val(jq(".dataTables_length select").val());
    if (sessionStorage.getItem("sortOrder") == null) {
        sessionStorage.setItem("sortOrder", "asc");
        sessionStorage.setItem("field", "title");
        jq('#hiddenSearchFields_h3').val("Title_sort");
        jq('#hiddenSearchFields_h2').val("asc");
    }
    
    jq(document).click(function(e){
        if (jq(e.target).is('#BoundwithTree2TabSection_tabs,#BoundwithTree2TabSection_tabs *,#BoundwithTreeSection1_tabs,#BoundwithTreeSection1_tabs *,#TransferSection1_tabs *,#TransferSection2_tabs *')) {

        }

    });

    jq(document).keypress(function (event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        var type = jq("select[name='searchType']").val();
        if (keycode == '13' && type == "search") {
            submitForm('search', null, null, null, null);
        } else if (keycode == '13' && type == "browse") {
            submitForm('browse', null, null, true, function () {
                browse()
            });
        }
    });
});


jq(window).load(function () {
    jq("#SearchButton").focus();
    jq("#SearchPanel-docType-Section_control_0").attr ('checked',true);
    jq(".dataTables_length select").live("change", function () {
        localStorage.localAscSort = true;
        localStorage.localDescSort = true;
        var rows = jq(".dataTables_length select").val();
        jq("#hdnRows_control").val(rows);
        jq("#hdnSortFlag_control").val("true");
        jq("#hdn_search_button").focus().click();
        jq("#hiddenSearchFields_h1").val(rows); //hiddenSearchFields_h1 is mapped to pageSize
        jq("#hiddenSearchFields_h4").val("true"); //hiddenSearchFields_h4 is mapped to sortFlag
        submitForm('search', null, null, true, function () {
            searching();
        });
    });
    jq("#search_button").live("click", function () {
        localStorage.localAscSort = true;
        localStorage.localDescSort = true;
        jq('#hdn_search_button').focus().click();
    });

});

function submitSearching() {
    localStorage.classValueToReplace = "";
    localStorage.localAscSort = true;
    localStorage.localDescSort = true;
    jq("#hdnSortFlag_control").val("false");
    jq("#hdnSortOrder_control").val("");
    jq("#hdnSortField_control").val("");
    searching();
}

function getUrl(line) {
    var url = jq("#BibSearchResults tbody tr:eq(" + line + ") td:eq(1) div a").attr("href") + "&pageId=WorkBibEditorViewPage";
    if (jq("#DocumentAndSearchSelectionType_SearchType_control").val()  == "search" ||  jq("#global_searchType_control").val() == "search" || jq("#AnalyticsDocumentAndSearchSelectionType_DocType_control").val()!=null) {
        var doctype = jq("#DocumentAndSearchSelectionType_DocType_control").val();
        if(doctype==undefined){
            doctype = jq("#global_docType_control").val();
        }
        if(doctype==undefined){
            doctype = jq("#AnalyticsDocumentAndSearchSelectionType_DocType_control").val();
        }
        if (doctype == 'holdings') {
            url = href = jq("#HoldingsSearchResults tbody tr:eq(" + line + ") td:eq(2) div a").attr("href") + "&pageId=WorkHoldingsViewPage";
        }
        else if (doctype == 'item') {
            url = href = jq("#ItemSearchResults tbody tr:eq(" + line + ") td:eq(2) div a").attr("href") + "&pageId=WorkItemViewPage";
        }
        else if (doctype == 'eHoldings') {
            url = href = jq("#EHoldingsSearchResults tbody tr:eq(" + line + ") td:eq(2) div a").attr("href") + "&pageId=WorkEInstanceViewPage";
        }

    } else {
        url = jq("#Title-Browse-BibSearchResults tbody tr:eq(" + line + ") td:eq(1) div a").attr("href") + "&pageId=WorkBibEditorViewPage";
        if (jq("#CallNumberBrowse-docType-Section_control_0").is(':checked') || jq("#CallNumberBrowse-docType-Section_control_1").is(':checked')) {
            url = jq("#CallNumberSectionResults tbody tr:eq(" + line + ") td:eq(1) div a").attr("href");
        }

    }
    return url;
}

function getPageId() {
    var pageId = "&pageId=WorkBibEditorViewPage";
    if (jq("#DocumentAndSearchSelectionType_SearchType_control").val() == "search") {
        var doctype = jq("#DocumentAndSearchSelectionType_DocType_control").val();
        if (doctype == 'holdings') {
            pageId = "&pageId=WorkHoldingsViewPage";
        }
        else if (doctype == 'item') {
            pageId = "&pageId=WorkItemViewPage";
        }
        else if (doctype == 'eHoldings') {
            pageId = "&pageId=WorkEInstanceViewPage";
        }

    } else {
        pageId = "&pageId=WorkBibEditorViewPage";
        if (jq("#CallNumberBrowse-docType-Section_control_0").is(':checked')) {
            pageId = "&pageId=WorkHoldingsViewPage";
        } else if (jq("#CallNumberBrowse-docType-Section_control_1").is(':checked')) {
            pageId = "&pageId=WorkItemViewPage";
        }
    }
    return pageId;
}

function selectOrUnselectLink(id, url) {
     var length = links.length + 1;
    if (jq("#" + id).is(':checked')) {
        if (length > 0) {
            var isAvailable = false;
            for (i = 1; i < length; i++) {
                if (links[i] == url) {
                    isAvailable = true;
                    break;
                }
            }
            if (!isAvailable) {
                links[length] = url;
            }
        } else {
            links[length] = url;
        }
    } else {
        for (i = 1; i < links.length; i++) {
            if (links[i] == url) {
                delete links[i];
                break;
            }
        }

    }
}


function selectOrUnselectDeLink(id, url) {
    var length = deLinks.length + 1;
    if (jq("#" + id).is(':checked')) {
        for (i = 1; i < length; i++) {
            if (deLinks[i] == url) {
                delete deLinks[i];
                break;
            }
        }
    } else {
        if (length > 0) {
            var isAvailable = false;
            for (i = 1; i < length; i++) {
                if (deLinks[i] == url) {
                    isAvailable = true;
                    break;
                }
            }
            if (!isAvailable) {
                deLinks[length] = url;
            }
        } else {
            deLinks[length] = url;
        }

    }
}


function selectOrUnselect() {
    jq('.sorting_1').live("click", function (event) {
        var id = event.target.id;
        if (id != "") {
            var line = id.split("line")[1].split("_")[0];
            var url = getUrl(line);
            if (jq("#hiddenSearchFields_h9").val() == "true" || jq("#hiddenSearchFields-browse_control").val() == "true") {
                selectOrUnselectDeLink(id, url)
            } else {
                selectOrUnselectLink(id, url);
            }
        }
    });
}

function searching() {
    jq("#hdnSortFlag_control").val("false");
    var date = new Date();
    var endTime = date.getMilliseconds();
    var timeTaken = (endTime - localStorage.startTime) / 1000;
    jq("#totalTime_control").text(" " + timeTaken + " ");
    selectCheckbox(jq("#DocumentAndSearchSelectionType_DocType_control").val());
    selectOrUnselect();
}


function globalEditInstance(docType) {
    window.open("editorcontroller?viewId=EditorView&methodToCall=load&docCategory=work&docType=" + docType + "&docFormat=oleml&editable=true&fromSearch=true&pageId=WorkHoldingsViewPage");
}

function selectCheckbox(doctype) {
    //hiddenSearchFields_h9 is mapped to selectAllRecords
    if (jq("#hiddenSearchFields_h9").val() == "true") {
        if (doctype == 'bibliographic') {
            var length = jq("#BibSearchResults tbody tr").length;
            for (var count = 0; count < length; count++) {
                jq("#BibSearchResults tbody tr:eq(" + count + ") td div input").prop("checked", true);
            }
        }
        else if (doctype == 'holdings') {
            var length = jq("#HoldingsSearchResults tbody tr").length;
            for (var count = 0; count < length; count++) {
                jq("#HoldingsSearchResults tbody tr:eq(" + count + ") td div input").prop("checked", true);
            }
        }
        else if (doctype == 'item') {
            var length = jq("#ItemSearchResults tbody tr").length;
            for (var count = 0; count < length; count++) {
                jq("#ItemSearchResults tbody tr:eq(" + count + ") td div input").prop("checked", true);
            }
        }
        else if (doctype == 'eHoldings') {
            var length = jq("#EHoldingsSearchResults tbody tr").length;
            for (var count = 0; count < length; count++) {
                jq("#EHoldingsSearchResults tbody tr:eq(" + count + ") td div input").prop("checked", true);
            }
        }
    } else if (jq('#hiddenSearchFields-browse_control').val() == "true") {
        var length = jq("#Title-Browse-BibSearchResults tbody tr").length;
        for (var count = 0; count < length; count++) {
            jq("#Title-Browse-BibSearchResults tbody tr:eq(" + count + ") td div input").prop("checked", true);
        }

        var length = jq("#CallNumberSectionResults tbody tr").length;
        for (var count = 0; count < length; count++) {
            jq("#CallNumberSectionResults tbody tr:eq(" + count + ") td div input").prop("checked", true);
        }
    } else {

        if (doctype == 'bibliographic') {
            var length = jq("#BibSearchResults tbody tr").length;
            for (var count = 0; count < length; count++) {
                for (var line = 0; line < links.length; line++) {
                    if (links[line] != null) {
                        if (links[line] == getUrl(count)) {
                            jq("#BibSearchResults tbody tr:eq(" + count + ") td div input").prop("checked", true);
                        }
                    }
                }

            }
        }
        else if (doctype == 'holdings') {
            var length = jq("#HoldingsSearchResults tbody tr").length;
            for (var count = 0; count < length; count++) {
                for (var line = 0; line < links.length; line++) {
                    if (links[line] != null) {
                        if (links[line] == getUrl(count)) {
                            jq("#HoldingsSearchResults tbody tr:eq(" + count + ") td div input").prop("checked", true);
                        }
                    }
                }
            }
        }
        else if (doctype == 'item') {
            var length = jq("#ItemSearchResults tbody tr").length;
            for (var count = 0; count < length; count++) {
                for (var line = 0; line < links.length; line++) {
                    if (links[line] != null) {
                        if (links[line] == getUrl(count)) {
                            jq("#ItemSearchResults tbody tr:eq(" + count + ") td div input").prop("checked", true);
                        }
                    }
                }
            }
        }
        else if (doctype == 'eHoldings') {
            var length = jq("#EHoldingsSearchResults tbody tr").length;
            for (var count = 0; count < length; count++) {
                for (var line = 0; line < links.length; line++) {
                    if (links[line] != null) {
                        if (links[line] == getUrl(count)) {
                            jq("#EHoldingsSearchResults tbody tr:eq(" + count + ") td div input").prop("checked", true);
                        }
                    }
                }
            }
        }

    }
}

function openSelectAll(doctype) {
    //hiddenSearchFields_h9 is mapped to selectAllRecords
    if (jq("#hiddenSearchFields_h9").val() == "true" || jq("#hiddenSearchFields-browse_control").val() == "true") {
        submitForm('openAll', null, null, true, function () {
            var selectedRecords = jq("#hiddenSearchFields_h10").val();  //hiddenSearchFields_h10 is mapped to idsToBeOpened
            if (selectedRecords == undefined) {
                selectedRecords = jq("#hiddenSearchFields-browse_h0").val();
            }
            var ids = selectedRecords.split(",");
            var docFormat = "oleml";
            var pageId = getPageId();
            if (doctype == "bibliographic") {
                docFormat = "marc";
            }
            for (var i = 0; i < ids.length; i++) {
                var id = ids[i].split("#");
                var link = href = getApplicationBanner()+"portal.do?channelTitle=Marc Editor&channelUrl="+getApplicationPath()+"editorcontroller?viewId=EditorView&methodToCall=load&docCategory=work&docType=" + doctype + "&docFormat=" + docFormat + "&docId=" + id[0] + "&bibId=" + id[1] + "&editable=true" + pageId;
                if (doctype == "bibliographic") {
                    link = getApplicationBanner()+"portal.do?channelTitle=Marc Editor&channelUrl="+getApplicationPath()+"editorcontroller?viewId=EditorView&methodToCall=load&docCategory=work&docType=" + doctype + "&docFormat=" + docFormat + "&docId=" + id[0] + "&bibId=&editable=true" + pageId;
                } else if (doctype == "item") {
                    link = href = getApplicationBanner()+"portal.do?channelTitle=Marc Editor&channelUrl="+getApplicationPath()+"editorcontroller?viewId=EditorView&methodToCall=load&docCategory=work&docType=" + doctype + "&docFormat=" + docFormat + "&docId=" + id[0] + "&bibId=" + id[1] + "&instanceId=" + id[2] + "&editable=true" + pageId;
                }
                if (deLinks.length > 0) {
                    var isAvailable = true;
                    for (j = 1; j < deLinks.length; j++) {
                        if (link == deLinks[j]) {
                            isAvailable = false;
                            break;
                        }
                    }
                    if (isAvailable) {
                        window.open(link);
                    }
                } else {
                    window.open(link);
                }

            }
        });
    } else {

        for (i = 0; i < links.length; i++)
            if (links[i] != null) {
                var url = getApplicationBanner()+"portal.do?channelTitle=Marc Editor&channelUrl="+getApplicationPath()+links[i];
                window.open(url);
            }
    }
}

function getApplicationPath() {
    var loc = window.location;
    var loc1 = loc.toString();
    return loc1.substring(0, loc1.lastIndexOf('/') + 1);
}

function getApplicationBanner() {
    var loc = window.location;
    var loc1 = loc.toString();
    loc1 =loc1.replace("/ole-kr-krad","");
    return loc1.substring(0, loc1.lastIndexOf('/')+1);
}

//hiddenSearchFields_h9 is mapped to selectAllRecords
function selectAll(doctype) {
    jq("#hiddenSearchFields_h9").val(true);
    jq("#hiddenSearchFields-browse_control").val(true);
    selectCheckbox(doctype);

}
function highLightNewSearch(id) {
    var JSONObject = JSON.stringify(id.extraData);//.split("actionParameters[selectedLineIndex]");
    var obj = jQuery.parseJSON(JSONObject);

    for (key in obj) {
        if (key == "actionParameters[selectedLineIndex]")
            id = parseInt(obj[key]) + 1;
    }
    jq('#' + 'items_line' + id).css({"background-color": "#FFFFD6", "border-color": "#000000"});
    jq('#' + 'SearchConditions_SearchText_id_line' + id + '_control').focus();
    if (jq('#' + 'SearchConditions_SearchText_id_line' + id + '_control').val() == null &&
        jq('#' + 'SearchConditions_DocField_id_line' + id + '_control').val() == null) {
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
    jq("hiddenSearchFields-browse_control").val("false");
    links = [];
    deLinks = [];
    displayDialogWindow("div#MessagePopupSectionForLinkToEResource");
    return true;
}
function openHelpWindow(url) {
    if (jq("input.uif-textControl").is(":focus")) {
        jq('#SearchButton').click();

    } else {
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
    jq('#hiddenSearchFields_control').val(1);
    sessionStorage.setItem("sortOrder", "asc");
    sessionStorage.setItem("field", "title");
    jq('#hiddenSearchFields_h3').val("Title_sort");
    jq('#hiddenSearchFields_h2').val("asc");
    var date = new Date();
    jq("#hiddenSearchFields_h9").val("false");
    links = [];
    deLinks = [];
    localStorage.startTime = date.getMilliseconds();
    submitForm('search', null, null, true, function () {
        searching();
    });
}
//hiddenSearchFields is mapped to pageNumber
function oleSearchPager(linkElement, collectionId) {
    var link = jQuery(linkElement);
    if (link.parent().is(kradVariables.ACTIVE_CLASS)) return;
    var pageNumber = link.data(kradVariables.PAGE_NUMBER_DATA);
    if (pageNumber == 'next')
        submitForm('nextSearch', null, null, true, function () {
            searching();
        });

    else if (pageNumber == 'prev')
        submitForm('previousSearch', null, null, true, function () {
            searching();
        });
    else if (pageNumber == 'first') {
        jQuery('#hiddenSearchFields_control').val('1');
        submitForm('pageNumberSearch', null, null, true, function () {
            searching();
        });
    } else if (pageNumber == 'last')
        submitForm('lastPageSearch', null, null, true, function () {
            searching();
        });
    else {
        jQuery('#hiddenSearchFields_control').val(pageNumber);
        submitForm('pageNumberSearch', null, null, true, function () {
            searching();
        });
    }

}

//hiddenSearchFields_h9 is mapped to selectAllRecords
function bibSortBy(field) {
    var sortField = "";
    setFieldAndSort(field);
    var sortOrder = sessionStorage.getItem("sortOrder");
    if (field == 'title') {
        sortField = "Title_sort";
    } else if (field == 'author') {
        sortField = "Author_sort "+sortOrder+ ",Title_sort";
    } else if (field == 'publicationDate') {
        sortField = "PublicationDate_sort "+sortOrder+ ",Title_sort";
    } else if (field == 'journal') {
        sortField = "JournalTitle_sort "+sortOrder+ ",Title_sort";
    } else if (field == 'publisher') {
        sortField = "Publisher_sort "+sortOrder+ ",Title_sort";
    } else if (field == 'local') {
        sortField = "LocalId_search "+sortOrder+ ",Title_sort";
    }

    jq('#hiddenSearchFields_h3').val(sortField);
    jq('#hiddenSearchFields_h2').val(sortOrder);
    submitForm('search', null, null, true, function () {
        searching();
    });

}

function itemSortBy(field) {
    var sortField = "";
    setFieldAndSort(field);
    var sortOrder = sessionStorage.getItem("sortOrder");
    if (field == 'title') {
        sortField = "Title_sort";
    } else if (field == 'location') {
        sortField = "Location_sort " +sortOrder+ ",Title_sort";
    } else if (field == 'callNumber') {
        sortField = "CallNumber_sort " +sortOrder+ ",Title_sort";
    } else if (field == 'barcode') {
        sortField = "ItemBarcode_sort ";
    } else if (field == 'itemStatus') {
        sortField = "ItemStatus_sort " +sortOrder+ ",Title_sort";
    } else if (field == 'copyNumber') {
        sortField = "CopyNumber_sort " +sortOrder+ ",Title_sort";
    } else if (field == 'enumeration') {
        sortField = "Enumeration_sort ";
    } else if (field == 'chronology') {
        sortField = "Chronology_sort " +sortOrder+ ",Title_sort";
    } else if (field == 'local') {
        sortField = "LocalId_search " +sortOrder+ ",Title_sort";
    } else if (field == 'shelvingOrder') {
        sortField = "ShelvingOrder_sort " +sortOrder+ ",Title_sort";
    }

    jq('#hiddenSearchFields_h3').val(sortField);
    jq('#hiddenSearchFields_h2').val(sortOrder);
    submitForm('search', null, null, true, function () {
        searching();
    });

}

function holdingsSortBy(field) {
    var sortField = "";
    setFieldAndSort(field);
    var sortOrder = sessionStorage.getItem("sortOrder");
    if (field == 'title') {
        sortField = "Title_sort";
    } else if (field == 'location') {
        sortField = "Location_sort " +sortOrder+ ",Title_sort";
    } else if (field == 'callNumber') {
        sortField = "CallNumber_sort " +sortOrder+ ",Title_sort";
    } else if (field == 'local') {
        sortField = "LocalId_search " +sortOrder+ ",Title_sort";
    } else if (field == 'shelvingOrder') {
        sortField = "ShelvingOrder_sort " +sortOrder+ ",Title_sort";
    }
    jq('#hiddenSearchFields_h3').val(sortField);
    jq('#hiddenSearchFields_h2').val(sortOrder);
    submitForm('search', null, null, true, function () {
        searching();
    });

}

function setFieldAndSort(field) {
    if (sessionStorage.getItem("field") == field) {
        if (sessionStorage.getItem("sortOrder") == "asc") {
            sessionStorage.setItem("sortOrder", "desc");
        } else if (sessionStorage.getItem("sortOrder") == "desc") {
            sessionStorage.setItem("sortOrder", "asc");
        }
    } else {
        sessionStorage.setItem("field", field)
        sessionStorage.setItem("sortOrder", "asc");
    }
}

function changeDocType(index){
    jq('#searchIndex_control').val(index);
    submitForm('changeDocType', null, null, null);
}





