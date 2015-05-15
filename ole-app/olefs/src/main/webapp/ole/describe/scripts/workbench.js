/**
 * Created with IntelliJ IDEA.
 * User: meenau
 * Date: 7/25/13
 * Time: 4:24 PM
 * To change this template use File | Settings | File Templates.
 */
//for bib
function viewWorkbenchBibView(docFormat, docId, bibId) {
    window.open("editorcontroller?viewId=EditorView&methodToCall=load&docCategory=work&docType=bibliographic&docFormat=" + docFormat + "&docId=" + docId + "&bibId=" + bibId + "&editable=false");
    return false;

}

function viewWorkbenchBibEdit(docFormat, docId, bibId) {
    window.open("editorcontroller?viewId=EditorView&methodToCall=load&docCategory=work&docType=bibliographic&docFormat=" + docFormat + "&docId=" + docId + "&bibId=" + bibId + "&editable=true&fromSearch=true");
    return false;
}

function viewWorkbenchBibOverlay(docId, bibId) {
    window.open("importBibController?viewId=ImportBibView&methodToCall=overLay&docCategory=work&docType=bibliographic&docFormat=marc&docId=" + docId + "&bibId=" + bibId);
    return false;
}

function viewWorkbenchBibCreateInstance(bibId) {
    window.open("editorcontroller?viewId=EditorView&methodToCall=load&bibId=" + bibId + "&docCategory=work&docType=holdings&docFormat=oleml&editable=true");
    return false;
}

//for holdings
function viewWorkbenchHoldingsView(docId, bibId, instanceId) {
    window.open("editorcontroller?viewId=EditorView&methodToCall=load&docCategory=work&docType=holdings&docFormat=oleml&docId=" + docId + "&bibId=" + bibId + "&instanceId=" + instanceId + "&editable=false");
    return false;

}

function viewWorkbenchHoldingsEdit(docId, bibId, instanceId) {
    window.open("editorcontroller?viewId=EditorView&methodToCall=load&docCategory=work&docType=holdings&docFormat=oleml&docId=" + docId + "&bibId=" + bibId + "&instanceId=" + instanceId + "&editable=true&fromSearch=true");
    return false;
}

function viewWorkbenchHoldingsShowBib(docId, instanceId) {
    window.open("editorcontroller?viewId=ShowBibView&methodToCall=showBibs&docId=" + docId + "&instanceId=" + instanceId + "&docCategory=work&docType=holdings&docFormat=oleml&editable=true");
    return false;
}

function viewBoundWithRelation(holdingsId) {
    window.open("editorcontroller?viewId=ShowBibView&methodToCall=showBibs&holdingsId=" + holdingsId + "&docCategory=work&docType=holdings&docFormat=oleml&editable=true");
    return false;
}
function viewSeriesHoldingRelation(holdingsId) {
    window.open("analyticsController?viewId=AnalyticsSummaryView&methodToCall=showAnalyticsSummary&holdingsId=" + holdingsId + "&docType=holdings&");

    return false;
}
//for item

function viewWorkbenchItemView(docId, bibId, instanceId) {
    window.open("editorcontroller?viewId=EditorView&methodToCall=load&docCategory=work&docType=item&docFormat=oleml&docId=" + docId + "&bibId=" + bibId + "&instanceId=" + instanceId + "&editable=false");
    return false;

}

function viewWorkbenchItemEdit(docId, bibId, instanceId) {
    window.open("editorcontroller?viewId=EditorView&methodToCall=load&docCategory=work&docType=item&docFormat=oleml&docId=" + docId + "&bibId=" + bibId + "&instanceId=" + instanceId + "&editable=true&fromSearch=true");
    return false;

}

function viewWorkbenchItemShowBib(instanceId) {
    window.open("ole-kr-krad/editorcontroller?viewId=ShowBibView&methodToCall=showBibs&instanceId=" + instanceId + "&docCategory=work&docType=item&docFormat=oleml&editable=true");
    return false;
}

function viewAnalyticItemRelation(itemIdentifier) {
    window.open("analyticsController?viewId=AnalyticsSummaryView&methodToCall=showAnalyticsSummary&itemId=" + itemIdentifier + "&docType=item&");
}

//for EHoldings

function viewWorkbenchEHoldingsView(docId, bibId, holdingsId) {
    window.open("editorcontroller?viewId=EditorView&methodToCall=load&docCategory=work&docType=eHoldings&docFormat=oleml&docId=" + docId + "&editable=false&bibId=" + bibId + "&holdingsId=" + holdingsId);
    return false;

}

function viewWorkbenchEHoldingsEdit(docId, bibId, holdingsId) {
    window.open("editorcontroller?viewId=EditorView&methodToCall=load&docCategory=work&docType=eHoldings&docFormat=oleml&docId=" + docId + "&editable=true&fromSearch=true&bibId=" + bibId + "&holdingsId=" + holdingsId);
    return false;

}

jq("#SearchPanel-docType-Section").live("change", function () {
    jq("div#DescribeWorkBenchViewPage").each(function () {
        if (jq(this).find("div.uif-validationMessages")) {
            jq(this).find("div.uif-validationMessages").remove();
        }
    });
    jq('#hdn_refresh_button').focus().click();
    submitForm('start', null, null, null, null);
});


jq("#GlobalEditView-Type-Section").live("change", function () {
    jq("div#DescribeWorkBenchViewPage").each(function () {
        if (jq(this).find("div.uif-validationMessages")) {
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
    jq('#hdnRows_control').val(jq(".dataTables_length select").val());
    if (sessionStorage.getItem("sortOrder") == null) {
        sessionStorage.setItem("sortOrder", "asc");
        jq('#hiddenSearchFields_h3').val("Title_sort");
        jq('#hiddenSearchFields_h2').val("asc");
    }


});


jq(window).load(function () {
    jq("#SearchPanel-docType-Section_control_0").attr('checked', true);
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

function searching() {
    jq("#hdnSortFlag_control").val("false");

    var date = new Date();
    var endTime = date.getMilliseconds();
    var timeTaken = (endTime - localStorage.startTime) / 1000;
    jq("#totalTime_control").text(" " + timeTaken + " ");
    selectCheckbox(jq("#DocumentAndSearchSelectionType_DocType_control").val());
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
            var pageId = "&pageId=WorkBibEditorViewPage";
            if (doctype == "bibliographic") {
                docFormat = "marc";
            }
            for (var i = 0; i < ids.length; i++) {
                var id = ids[i].split("#");
                var link = href = "editorcontroller?viewId=EditorView&methodToCall=load&docCategory=work&docType=" + doctype + "&docFormat=" + docFormat + "&docId=" + id[0] + "&bibId=" + id[1] + "&editable=true";
                window.open(link);
            }
        });
    } else {
        var checkedcount = 0;
        if (doctype == 'bibliographic') {
            var length = jq("#BibSearchResults tbody tr").length;
            for (var count = 0; count < length; count++) {
                if (jq("#BibSearchResults tbody tr:eq(" + count + ") td div input").is(':checked')) {
                    window.open(jq("#BibSearchResults tbody tr:eq(" + count + ") td:eq(1) div a").attr("href") + "&pageId=WorkBibEditorViewPage");
                    checkedcount += 1;
                }
            }

            length = jq("#Title-Browse-BibSearchResults tbody tr").length;
            for (var count = 0; count < length; count++) {
                if (jq("#Title-Browse-BibSearchResults tbody tr:eq(" + count + ") td div input").is(':checked')) {
                    window.open(jq("#Title-Browse-BibSearchResults tbody tr:eq(" + count + ") td:eq(1) div a").attr("href") + "&pageId=WorkBibEditorViewPage");
                    checkedcount += 1;
                }
            }
        }
        else if (doctype == 'holdings') {
            var length = jq("#HoldingsSearchResults tbody tr").length;
            for (var count = 0; count < length; count++) {
                if (jq("#HoldingsSearchResults tbody tr:eq(" + count + ") td div input").is(':checked')) {
                    window.open(jq("#HoldingsSearchResults tbody tr:eq(" + count + ") td:eq(2) div a").attr("href") + "&pageId=WorkHoldingsViewPage");
                    checkedcount += 1;
                }
            }

            var length = jq("#CallNumberSectionResults tbody tr").length;
            for (var count = 0; count < length; count++) {
                if (jq("#CallNumberSectionResults tbody tr:eq(" + count + ") td div input").is(':checked')) {
                    window.open(jq("#CallNumberSectionResults tbody tr:eq(" + count + ") td:eq(2) div a").attr("href") + "&pageId=WorkHoldingsViewPage");
                    checkedcount += 1;
                }
            }
        }
        else if (doctype == 'item') {
            var length = jq("#ItemSearchResults tbody tr").length;
            for (var count = 0; count < length; count++) {
                if (jq("#ItemSearchResults tbody tr:eq(" + count + ") td div input").is(':checked')) {
                    window.open(jq("#ItemSearchResults tbody tr:eq(" + count + ") td:eq(2) div a").attr("href") + "&pageId=WorkItemViewPage");
                    checkedcount += 1;
                }
            }


            var length = jq("#CallNumberSectionResults tbody tr").length;
            for (var count = 0; count < length; count++) {
                if (jq("#CallNumberSectionResults tbody tr:eq(" + count + ") td div input").is(':checked')) {
                    window.open(jq("#CallNumberSectionResults tbody tr:eq(" + count + ") td:eq(2) div a").attr("href") + "&pageId=WorkHoldingsViewPage");
                    checkedcount += 1;
                }
            }
        }
        else if (doctype == 'eHoldings') {
            var length = jq("#EHoldingsSearchResults tbody tr").length;
            for (var count = 0; count < length; count++) {
                if (jq("#EHoldingsSearchResults tbody tr:eq(" + count + ") td div input").is(':checked')) {
                    window.open(jq("#EHoldingsSearchResults tbody tr:eq(" + count + ") td:eq(1) div a").attr("href") + "&pageId=WorkEInstanceViewPage");
                    checkedcount += 1;
                }

            }
        }
    }


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
    jq('#hiddenSearchFields_h3').val("Title_sort");
    jq('#hiddenSearchFields_h2').val("asc");
    var date = new Date();
    jq("#hiddenSearchFields_h9").val("false")
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
    var sortOrder = "";
    if (sessionStorage.getItem("sortOrder") == "asc") {
        sessionStorage.setItem("sortOrder", "desc");
    } else if (sessionStorage.getItem("sortOrder") == "desc") {
        sessionStorage.setItem("sortOrder", "asc");
    }
    sortOrder = sessionStorage.getItem("sortOrder");
    if (field == 'title') {
        sortField = "Title_sort";
    } else if (field == 'author') {
        sortField = "Author_sort";
    } else if (field == 'publicationDate') {
        sortField = "PublicationDate_sort";
    } else if (field == 'journal') {
        sortField = "JournalTitle_sort";
    } else if (field == 'publisher') {
        sortField = "Publisher_sort";
    } else if (field == 'local') {
        sortField = "LocalId_search";
    }

    jq('#hiddenSearchFields_h3').val(sortField);
    jq('#hiddenSearchFields_h2').val(sortOrder);
    submitForm('search', null, null, true, function () {
        searching();
    });

}

function itemSortBy(field) {
    var sortField = "";

    if (sessionStorage.getItem("sortOrder") == "asc") {
        sessionStorage.setItem("sortOrder", "desc");
    } else if (sessionStorage.getItem("sortOrder") == "desc") {
        sessionStorage.setItem("sortOrder", "asc");
    }
    var sortOrder = sessionStorage.getItem("sortOrder");
    if (field == 'title') {
        sortField = "Title_sort";
    } else if (field == 'location') {
        sortField = "Location_sort";
    } else if (field == 'callNumber') {
        sortField = "CallNumber_sort";
    } else if (field == 'barcode') {
        sortField = "ItemBarcode_sort";
    } else if (field == 'itemStatus') {
        sortField = "ItemStatus_sort";
    } else if (field == 'copyNumber') {
        sortField = "CopyNumber_sort";
    } else if (field == 'enumeration') {
        sortField = "Enumeration_sort";
    } else if (field == 'chronology') {
        sortField = "Chronology_sort";
    } else if (field == 'local') {
        sortField = "LocalId_search";
    }
    jq('#hiddenSearchFields_h3').val(sortField);
    jq('#hiddenSearchFields_h2').val(sortOrder);
    submitForm('search', null, null, true, function () {
        searching();
    });

}


function holdingsSortBy(field) {
    var sortField = "";

    if (sessionStorage.getItem("sortOrder") == "asc") {
        sessionStorage.setItem("sortOrder", "desc");
    } else if (sessionStorage.getItem("sortOrder") == "desc") {
        sessionStorage.setItem("sortOrder", "asc");
    }
    var sortOrder = sessionStorage.getItem("sortOrder");
    if (field == 'title') {
        sortField = "Title_sort";
    } else if (field == 'location') {
        sortField = "Location_sort";
    } else if (field == 'callNumber') {
        sortField = "CallNumber_sort";
    } else if (field == 'local') {
        sortField = "LocalId_search";
    }
    jq('#hiddenSearchFields_h3').val(sortField);
    jq('#hiddenSearchFields_h2').val(sortOrder);
    submitForm('search', null, null, true, function () {
        searching();
    });

}

