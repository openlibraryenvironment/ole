function validateCheckBoxes() {
    jq('input:checkbox').click(function () {
        jq('input:checkbox').not(this).removeAttr('checked');
    });
}

jq(function () {
    jq("#ImportFromExternalDataSource_tab").click(function () {
        jq("#showExternalDS_attribute").val("true")
    });

    jq("#ImportFromLocal_tab").click(function () {
        jq("#showExternalDS_attribute").val("false")
    });

    if (jq("#showExternalDS_attribute").val() == "true") {
        jq("#SearchBibTabSection_tabs").tabs("select", "#ImportFromExternalDataSource_tab");
    }
});

jq(document).ready(function () {
    jq("#OLESearchPanel-docType-Section_control_0").attr ('checked',true);
});


function load() {
    jq("#preference_Name_control").change(function () {
        if (jq("#load_button").is(':hidden')) {
            jq("#load_button").show();
            jq("#UserPreferences-Settings").hide();
            jq("#UserPreferences-CallNumberMapping").hide();
        }
    });

    if (jq("#preference_Name_control").length > 0) {
        if (jq("#preference_Name_control").get(0).selectedIndex >= 1) {
            jq("#load_button").hide();
            jq("#UserPreferences-Settings").show();
            jq("#UserPreferences-CallNumberMapping").show();
        }
        else if (jq("#preference_Name_control").get(0).selectedIndex == 0) {
            jq("#UserPreferences-Settings").hide();
            jq("#UserPreferences-CallNumberMapping").hide();
        }
    }
}
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

function browse() {
    jq(".dataTables_wrapper").attr("style", "width:1200px;");
    displayDialogWindow("div#MessagePopupSectionForLinkToEResource");
    return true;
}

jq("#OLESearchPanel-docType-Section").live("change", function () {
    jq("div#OLEDescribeWorkBenchViewPage").each(function () {
        if (jq(this).find("div.uif-validationMessages")) {
            jq(this).find("div.uif-validationMessages").remove();
        }
    });
    jq('#req_hdn_refresh_button').focus().click();
});

jq("#OLEERSSearchPanel-docType-Section").live("change", function () {
    jq("div#OLEEResourceBibViewPage").each(function () {
        if (jq(this).find("div.uif-validationMessages")) {
            jq(this).find("div.uif-validationMessages").remove();
        }
    });
    jq('#ers_hdn_refresh_button').focus().click();
});

jq(window).load(function () {
    jq(".dataTables_length select").live("change", function () {
        var rows = jq(".dataTables_length select").val();
        jq("#req_hdnRows_control").val(rows);
        jq("#req_hdn_search_button").focus().click();
        jq("#ers_hdnRows_control").val(rows);
        jq("#ers_hdn_search_button").focus().click();
    });
    jq(".paginate_enabled_next").live("click", function () {
        jq('#req_hdn_NextFlag_button').focus().click();
        jq('#ers_hdn_NextFlag_button').focus().click();
    });
    jq(".paginate_enabled_previous").live("click", function () {
        jq('#req_hdn_PreviousFlag_button').focus().click();
        jq('#ers_hdn_PreviousFlag_button').focus().click();
    });
    jq("#search_button").live("click", function () {
        jq('#req_hdn_search_button').focus().click();
    });
    jq("#oleERSSearch_button").live("click", function () {
        jq('#ers_hdn_search_button').focus().click();
    });

    jq(".sorting").live("click", function () {
        localStorage.indexOfTableSorting = jq(this).index();
        localStorage.classValueToReplace = "sorting_asc";
        commonShowEntries();
    });

    jq(".sorting_asc").live("click", function () {
        localStorage.indexOfTableSorting = jq(this).index();
        localStorage.classValueToReplace = "sorting_desc";
        commonShowEntries();
    });

    jq(".sorting_desc").live("click", function () {
        localStorage.indexOfTableSorting = jq(this).index();
        localStorage.classValueToReplace = "sorting";
        commonShowEntries();
    });
});

function submitSearching() {
    localStorage.classValueToReplace = "";
    searching();
}

function searching() {
    jq(".dataTables_wrapper").attr("style", "width:1250px;");
    searchShowEntries();
    jq("#OLESearchResultsPanel_toggle").click(function () {
        if (jq("#OLESearchResultsPanel_disclosureContent").css("display") == "block") {
            window.setTimeout(function () {
                searchShowEntries();
            }, 500)
        }
    });
    jq("#OLESearchPanel_toggle").click(function () {
        if (jq("#OLESearchPanel_disclosureContent").css("display") == "block") {
            window.setTimeout(function () {
                searchShowEntries();
            }, 500)
        }
    });
    jq("#OLEERSSearchResultsPanel_toggle").click(function () {
        if (jq("#OLEERSSearchResultsPanel_disclosureContent").css("display") == "block") {
            window.setTimeout(function () {
                searchShowEntries();
            }, 500)
        }
    });
    jq("#OLEERSSearchPanel_toggle").click(function () {
        if (jq("#OLEERSSearchPanel_disclosureContent").css("display") == "block") {
            window.setTimeout(function () {
                searchShowEntries();
            }, 500)
        }
    });
}

function searchShowEntries() {
    window.setTimeout(function () {
        if (jq(".dataTables_info").text() != "") {
            jq(".dataTables_info").text(jq("#req_workbenchPageShowEntries_control").val());
            jq(".dataTables_info").text(jq("#ers_workbenchPageShowEntries_control").val());
        }
        if (jq("#req_workbenchNextFlag_control").val() == "true") {
            jq(".paginate_disabled_next").removeClass("paginate_disabled_next").addClass("paginate_enabled_next");
        }
        if (jq("#req_workbenchPreviousFlag_control").val() == "true") {
            jq(".paginate_disabled_previous").removeClass("paginate_disabled_previous").addClass("paginate_enabled_previous");
        }
        if (jq("#ers_workbenchNextFlag_control").val() == "true") {
            jq(".paginate_disabled_next").removeClass("paginate_disabled_next").addClass("paginate_enabled_next");
        }
        if (jq("#ers_workbenchPreviousFlag_control").val() == "true") {
            jq(".paginate_disabled_previous").removeClass("paginate_disabled_previous").addClass("paginate_enabled_previous");
        }
        if (localStorage.classValueToReplace == "sorting") {
            jq("table#u216 thead tr th:eq(" + localStorage.indexOfTableSorting + ")").removeClass("sorting").addClass("sorting_desc");
            jq(".sorting_desc").click();

        } else if (localStorage.classValueToReplace == "sorting_desc") {
            jq("table#u216 thead tr th:eq(" + localStorage.indexOfTableSorting + ")").removeClass("sorting").addClass("sorting_asc");
            jq(".sorting_asc").click();
        }

    }, 500)
}

function commonShowEntries() {
    if (jq("#req_workbenchNextFlag_control").val() == "true") {
        jq(".paginate_disabled_next").removeClass("paginate_disabled_next").addClass("paginate_enabled_next");
    }
    if (jq("#req_workbenchPreviousFlag_control").val() == "true") {
        jq(".paginate_disabled_previous").removeClass("paginate_disabled_previous").addClass("paginate_enabled_previous");
    }
    if (jq(".dataTables_info").text() != "") {
        jq(".dataTables_info").text(jq("#req_workbenchPageShowEntries_control").val());
        jq(".dataTables_info").text(jq("#ers_workbenchPageShowEntries_control").val());
    }
    if (jq("#ers_workbenchNextFlag_control").val() == "true") {
        jq(".paginate_disabled_next").removeClass("paginate_disabled_next").addClass("paginate_enabled_next");
    }
    if (jq("#ers_workbenchPreviousFlag_control").val() == "true") {
        jq(".paginate_disabled_previous").removeClass("paginate_disabled_previous").addClass("paginate_enabled_previous");
    }
}
