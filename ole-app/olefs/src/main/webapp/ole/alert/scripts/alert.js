var alertSelector;
jq(document).ready(function () {
    alertSelector = jq("#alert_selector_add_control").val();
    setAlertSelectorValue();
});

function setAlertSelectorBasedOnIndex(id,editflag) {
    var selectorField = "alert_selector_line";
    var roleField = "alert_receivingRoleName_line";
    var groupField = "alert_receivingGroupName_line";
    var personField = "alert_receivingUserName_line";
    var control = "_control";
    var selectorField = '#'.concat(selectorField).concat(id).concat(control);
    var roleField = '#'.concat(roleField).concat(id);
    var groupField = '#'.concat(groupField).concat(id);
    var personField = '#'.concat(personField).concat(id);
    if(editflag === true) {
        jq(selectorField).val(alertSelector);
    }
    if(id >= 0) {
        setAlertSelectorValue();
        if(jq(selectorField).val() == 'Role') {
            jq(roleField).show();
            jq(groupField).hide();
            jq(personField).hide();
        } else if(jq(selectorField).val() == 'Group') {
            jq(groupField).show();
            jq(roleField).hide();
            jq(personField).hide();
        } else if(jq(selectorField).val() == 'Person') {
            jq(personField).show();
            jq(roleField).hide();
            jq(groupField).hide();
        }
    } else {
        setAlertSelectorValue();
    }
}

function setAlertSelectorValue() {
    jq("#alert_selector_add_control").val(alertSelector);
    if(jq("#alert_selector_add_control").val() == 'Role') {
        jq("#alert_receivingRoleName_add").show();
        jq("#alert_receivingGroupName_add").hide();
        jq("#alert_receivingUserName_add").hide();
    } else if(jq("#alert_selector_add_control").val() == 'Group') {
        jq("#alert_receivingGroupName_add").show();
        jq("#alert_receivingRoleName_add").hide();
        jq("#alert_receivingUserName_add").hide();
    } else if(jq("#alert_selector_add_control").val() == 'Person') {
        jq("#alert_receivingUserName_add").show();
        jq("#alert_receivingRoleName_add").hide();
        jq("#alert_receivingGroupName_add").hide();
    }
}

function changeSelectorValue(id){
    var selectorField = "alert_selector_line";
    var roleField = "alert_receivingRoleName_line";
    var groupField = "alert_receivingGroupName_line";
    var personField = "alert_receivingUserName_line";
    var control = "_control";
    var selectorField = '#'.concat(selectorField).concat(id).concat(control);
    var roleFieldControl = '#'.concat(roleField).concat(id).concat(control);
    var groupFieldControl = '#'.concat(groupField).concat(id).concat(control);
    var personFieldControl = '#'.concat(personField).concat(id).concat(control);
    var roleFieldId = '#'.concat(roleField).concat(id);
    var groupFieldId = '#'.concat(groupField).concat(id);
    var personFieldId = '#'.concat(personField).concat(id);
    if(id == -1) {
        if(jq("#alert_selector_add_control").val() == 'Role') {
            alertSelector = jq("#alert_selector_add_control").val();
            jq("#alert_receivingRoleName_add").show();
            jq("#alert_receivingGroupName_add_control").val("");
            jq("#alert_receivingGroupId_add_control").val("");
            jq("#alert_receivingGroupName_add").hide();
            jq("#alert_receivingUserName_add_control").val("");
            jq("#alert_receivingUserId_add_control").val("");
            jq("#alert_receivingUserName_add").hide();
        } else if(jq("#alert_selector_add_control").val() == 'Group') {
            alertSelector = jq("#alert_selector_add_control").val();
            jq("#alert_receivingGroupName_add").show();
            jq("#alert_receivingRoleName_add_control").val("");
            jq("#alert_receivingRoleId_add_control").val("");
            jq("#alert_receivingRoleName_add").hide();
            jq("#alert_receivingUserName_add_control").val("");
            jq("#alert_receivingUserId_add_control").val("");
            jq("#alert_receivingUserName_add").hide();
        } else if(jq("#alert_selector_add_control").val() == 'Person') {
            alertSelector = jq("#alert_selector_add_control").val();
            jq("#alert_receivingUserName_add").show();
            jq("#alert_receivingRoleName_add_control").val("");
            jq("#alert_receivingRoleId_add_control").val("");
            jq("#alert_receivingRoleName_add").hide();
            jq("#alert_receivingGroupName_add_control").val("");
            jq("#alert_receivingGroupId_add_control").val("");
            jq("#alert_receivingGroupName_add").hide();
        }
    } else {
        if(jq(selectorField).val() == 'Role') {
            alertSelector = jq(selectorField).val();
            jq(roleFieldId).show();
            jq(groupFieldControl).val("");
            jq(groupFieldId).hide();
            jq(personFieldId).hide();
        } else if(jq(selectorField).val() == 'Group') {
            alertSelector = jq(selectorField).val();
            jq(groupFieldId).show();
            jq(roleFieldControl).val("");
            jq(roleFieldId).hide();
            jq(personFieldId).hide();
        } else if(jq(selectorField).val() == 'Person') {
            alertSelector = jq(selectorField).val();
            jq(personFieldId).show();
            jq(roleFieldControl).val("");
            jq(roleFieldId).hide();
            jq(groupFieldControl).val("");
            jq(groupFieldId).hide();
        }
    }
}


