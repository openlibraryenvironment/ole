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
            jq('#Hidden_BrowseSearchRows_button').focus().click();
        }
        else {
            jq('#CallNumberBrowseSearchRows_button').focus().click();
        }
    })

    jq(".paginate_enabled_previous").live("click",function(){
        if(jq("#docType_control").val()=="bibliographic"){
            jq('#Hidden_BrowsePrevious_button').focus().click();
        }
        else {
            jq('#CallNumberBrowsePrevious_button').focus().click();
        }
    })

    jq(".paginate_enabled_next").live("click",function(){
        if(jq("#docType_control").val()=="bibliographic"){
            jq('#Hidden_BrowseNext_button').focus().click();
        }
        else {
            jq('#CallNumberBrowseNext_button').focus().click();
        }
    })

    jq(".sorting_asc").live("click",function(){
        if(jq(".dataTables_info").text()!=""){
            jq(".dataTables_info").text(jq("#pageShowEntries_control").val());
        }
        if(jq("#nextFlag_control").val()=="true"){
            jq(".paginate_disabled_next").removeClass("paginate_disabled_next").addClass("paginate_enabled_next");
        }
        if(jq("#previousFlag_control").val()=="true"){
            jq(".paginate_disabled_previous").removeClass("paginate_disabled_previous").addClass("paginate_enabled_previous");
        }
    })

    jq(".sorting_desc").live("click",function(){
        if(jq(".dataTables_info").text()!=""){
            jq(".dataTables_info").text(jq("#pageShowEntries_control").val());
        }
        if(jq("#nextFlag_control").val()=="true"){
            jq(".paginate_disabled_next").removeClass("paginate_disabled_next").addClass("paginate_enabled_next");
        }
        if(jq("#previousFlag_control").val()=="true"){
            jq(".paginate_disabled_previous").removeClass("paginate_disabled_previous").addClass("paginate_enabled_previous");
        }
    })

    jq(".dataTables_filter").hide();

    /*if(jq(".dataTables_info").text()!=""){
        jq(".dataTables_info").text(jq("#pageShowEntries_control").val());
    }


    if(jq("#nextFlag_control").val()=="true"){
        alert("next");
        jq(".paginate_disabled_next").removeClass("paginate_disabled_next").addClass("paginate_enabled_next");
    }
    if(jq("#previousFlag_control").val()=="true"){
        alert("previous");
        jq(".paginate_disabled_previous").removeClass("paginate_disabled_previous").addClass("paginate_enabled_previous");
    }
*/
});

function browse(){
    common();
    jq("#CallNumberBrowseSearchResultsPanel_toggle").click(function(){
        if(jq("#CallNumberBrowseSearchResultsPanel_disclosureContent").css("display")=="block"){
            window.setTimeout(function () {
                common();
            }, 500)
        }

    });

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
    window.setTimeout(function(){
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
    }, 500);
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
