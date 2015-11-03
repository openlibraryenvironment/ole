
jq("#create_orderNo_add_control").live('keydown',function(event) {
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
jq(document).ready(function() {
    if(jq("#selectRequest-MaintenanceViews-selector_control").val() == "Role") {
        jq("#selectRequest-MaintenanceViews-personName_add_control").val("");
        jq("#selectRequest-MaintenanceViews-personName_add").hide();
        jq("#selectRequest-MaintenanceViews-roleName_add").show();
    } else if(jq("#selectRequest-MaintenanceViews-selector_control").val() == "Person") {
        jq("#selectRequest-MaintenanceViews-roleName_add_control").val("");
        jq("#selectRequest-MaintenanceViews-roleName_add").hide();
        jq("#selectRequest-MaintenanceViews-personName_add").show();
    }
    jq("#selectRequest-MaintenanceViews-selector_control").change(function() {
        if(jq("#selectRequest-MaintenanceViews-selector_control").val() == "Role") {
            jq("#selectRequest-MaintenanceViews-personName_add_control").val("");
            jq("#selectRequest-MaintenanceViews-personName_add").hide();
            jq("#selectRequest-MaintenanceViews-roleName_add").show();
        } else if(jq("#selectRequest-MaintenanceViews-selector_control").val() == "Person") {
            jq("#selectRequest-MaintenanceViews-roleName_add_control").val("");
            jq("#selectRequest-MaintenanceViews-roleName_add").hide();
            jq("#selectRequest-MaintenanceViews-personName_add").show();
        }
    });




    if(jq("#selectRequest-MaintenanceViews-notifier_control").val() == "Role") {
        jq("#selectRequest-MaintenanceViews-recipientUserName_control").val("");
        jq("#selectRequest-MaintenanceViews-recipientUserName").hide();
        jq("#create_mailId_control").val("");
        jq("#create_mailId").hide();
        jq("#selectRequest-MaintenanceViews-recipientRoleName").show();
    } else if(jq("#selectRequest-MaintenanceViews-notifier_control").val() == "Person") {
        jq("#selectRequest-MaintenanceViews-recipientRoleName_control").val("");
        jq("#selectRequest-MaintenanceViews-recipientRoleName").hide();
        jq("#selectRequest-MaintenanceViews-recipientUserName").show();
        jq("#create_mailId_control").val("");
        jq("#create_mailId").hide();
    } else if(jq("#selectRequest-MaintenanceViews-notifier_control").val() == "mail") {
        jq("#selectRequest-MaintenanceViews-recipientRoleName_control").val("");
        jq("#selectRequest-MaintenanceViews-recipientRoleName").hide();
        jq("#selectRequest-MaintenanceViews-recipientUserName_control").val("");
        jq("#selectRequest-MaintenanceViews-recipientUserName").hide();
        jq("#create_mailId").show();
    }



    jq("#selectRequest-MaintenanceViews-notifier_control").change(function() {
        if(jq("#selectRequest-MaintenanceViews-notifier_control").val() == "Role") {
            jq("#selectRequest-MaintenanceViews-recipientUserName_control").val("");
            jq("#selectRequest-MaintenanceViews-recipientUserName").hide();
            jq("#create_mailId_control").val("");
            jq("#create_mailId").hide();
            jq("#selectRequest-MaintenanceViews-recipientRoleName").show();
        } else if(jq("#selectRequest-MaintenanceViews-notifier_control").val() == "Person") {
            jq("#selectRequest-MaintenanceViews-recipientRoleName_control").val("");
            jq("#selectRequest-MaintenanceViews-recipientRoleName").hide();
            jq("#selectRequest-MaintenanceViews-recipientUserName").show();
            jq("#create_mailId_control").val("");
            jq("#create_mailId").hide();
        } else if(jq("#selectRequest-MaintenanceViews-notifier_control").val() == "mail") {
            jq("#selectRequest-MaintenanceViews-recipientRoleName_control").val("");
            jq("#selectRequest-MaintenanceViews-recipientRoleName").hide();
            jq("#selectRequest-MaintenanceViews-recipientUserName_control").val("");
            jq("#selectRequest-MaintenanceViews-recipientUserName").hide();
            jq("#create_mailId").show();
        }
    });





});

function setSelectorValue() {
    if(jq("#selectRequest-MaintenanceViews-selector_control").val() == "Role") {
        jq("#selectRequest-MaintenanceViews-personName_add_control").val("");
        jq("#selectRequest-MaintenanceViews-personName_add").hide();
        jq("#selectRequest-MaintenanceViews-roleName_add").show();
    } else if(jq("#selectRequest-MaintenanceViews-selector_control").val() == "Person") {
        jq("#selectRequest-MaintenanceViews-roleName_add_control").val("");
        jq("#selectRequest-MaintenanceViews-roleName_add").hide();
        jq("#selectRequest-MaintenanceViews-personName_add").show();
    }
}
