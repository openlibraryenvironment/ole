jq(window).load(function() {

    if(jq("#closeBtnShowFlag_control").val()=="false"){
        jq("#CallNumberParentWindowClose_button").hide();
    }
    if(jq("#closeBtnShowFlag_control").val()=="true"){
        jq("#CallNumberClose_button").hide();
    }

    jq("#CallNumberBrowse-docType-Section_control_0").attr('checked', true);

    if(jq("#docType_control").val()=="holdings"){
        jq("#CallNumberBrowse-docType-Section_control_0").attr('checked', true);
    }
    else if(jq("#docType_control").val()=="item"){
        jq("#CallNumberBrowse-docType-Section_control_1").attr('checked', true);
    }

    jq(".dataTables_length select").live("change",function(){
        jq("#pageSize_control").val(jq(".dataTables_length select").val());
        if(jq("#docType_control").val()=="bibliographic"){
            submitForm('rowsBrowse', null, null, true, function () {
                common();
            });
        }
        else {
            jq('#CallNumberBrowseSearchRows_button').focus().click();
        }
    })

    jq(".paginate_enabled_previous").live("click",function(){
        if(jq("#docType_control").val()=="bibliographic"){
            submitForm('previous', null, null, true, function () {
                common();
            });
        }
        else {
            jq('#CallNumberBrowsePrevious_button').focus().click();
        }
    })

    jq(".paginate_enabled_next").live("click",function(){
        if(jq("#docType_control").val()=="bibliographic"){
            submitForm('next', null, null, true, function () {
                common();
            });
        }
        else {
            jq('#CallNumberBrowseNext_button').focus().click();
        }
    })

    jq(".dataTables_filter").hide();
    selectOrUnselect();

});

function browse(){
    jq("#hiddenSearchFields-browse_control").val(false);
    links = [];
    deLinks = [];
    common();
    return false;
}

function common(){

    jq(".dataTables_filter").hide();
   // alert(jq("#closeBtnShowFlag_control").val());
    if(jq("#closeBtnShowFlag_control").val()=="false"){
        jq("#CallNumberParentWindowClose_button").hide();
    }
    if(jq("#closeBtnShowFlag_control").val()=="true"){
        jq("#CallNumberClose_button").hide();
    }
    jq(".dataTables_length select").val(jq("#pageSize_control").val());
    selectCheckbox(jq("#DocumentAndSearchSelectionType_DocType_control").val());

        if(jq(".dataTables_info").text()!=""){
            jq(".dataTables_info").text(jq("#pageShowEntries_control").val());
        }
        if(jq("#nextFlag_control").val()=="true"){
            jq(".paginate_disabled_next").removeClass("paginate_disabled_next").addClass("paginate_enabled_next");
        }
        if(jq("#previousFlag_control").val()=="true"){
            jq(".paginate_disabled_previous").removeClass("paginate_disabled_previous").addClass("paginate_enabled_previous");
        }
        if(jq("#docType_control").val()=="holdings"){
            jq("#CallNumberBrowse-docType-Section_control_0").attr('checked', true);
        }
        else if(jq("#docType_control").val()=="item") {
           jq("#CallNumberBrowse-docType-Section_control_1").attr('checked', true);
        }

}

function clear(){
    //alert(jq("#closeBtnShowFlag_control").val());
    if(jq("#closeBtnShowFlag_control").val()=="false"){
        jq("#CallNumberParentWindowClose_button").hide();
    }
    if(jq("#closeBtnShowFlag_control").val()=="true"){
        jq("#CallNumberClose_button").hide();
    }
    return false;
}

function openSelectAll(doctype) {
    var checkedcount = 0;
    if (doctype == 'holdings') {
        var length = jq("#CallNumber-BrowseResults-Holdings table tbody tr").length;
        for (var count = 0; count < length; count++) {
            if (jq("#CallNumber-BrowseResults-Holdings table tbody tr:eq(" + count + ") td div input").is(':checked')) {
                window.open(jq("#CallNumber-BrowseResults-Holdings table tbody tr:eq(" + count + ") td:eq(1) div a").attr("href") + "&pageId=WorkHoldingsViewPage");
                checkedcount += 1;
            }
        }
    }
    else if (doctype == 'item') {
        var length = jq("#CallNumber-BrowseResults-Item tbody tr").length;
        for (var count = 0; count < length; count++) {
            if (jq("#CallNumber-BrowseResults-Item tbody tr:eq(" + count + ") td div input").is(':checked')) {
                window.open(jq("#CallNumber-BrowseResults-Item tbody tr:eq(" + count + ") td:eq(1) div a").attr("href") + "&pageId=WorkItemViewPage");
                checkedcount += 1;
            }
        }
    }
    else if (doctype == 'bibliographic') {
        var length = jq("#Title-Browse-BibSearchResults_disclosureContent table tbody tr").length;
        for (var count = 0; count < length; count++) {
            if (jq("#Title-Browse-BibSearchResults_disclosureContent table tbody tr:eq(" + count + ") td div input").is(':checked')) {
                window.open(jq("#Title-Browse-BibSearchResults_disclosureContent table tbody tr:eq(" + count + ") td:eq(1) div a").attr("href") + "&pageId=WorkBibEditorViewPage");
                checkedcount += 1;
            }
        }
    }
}


