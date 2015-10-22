/**
 * Created by angelind on 10/6/15.
 */

var alertSelector;
jq(document).ready(function() {
    alertSelector = jq("#alert_selector_add_control").val();
    setAdHocSelector();
});

function setAdHocSelectorAfterAdd(editFlag) {
    if(editFlag === true) {
        jq("#adHocRecipientSelector_add_control").val(alertSelector);
    }
    setAdHocSelector();
}

function setAdHocSelector() {
    alertSelector = jq("#alert_selector_add_control").val();
    if(jq("#adHocRecipientSelector_add_control").val() == "Role") {
        jq("#adHocPrincipalName_add_control").val("");
        jq("#adHocGroupName_add_control").val("");
        jq("#adHocPrincipalName_add").hide();
        jq("#adHocGroupName_add").hide();
        jq("#adHocRoleName_add").show();
    } else if(jq("#adHocRecipientSelector_add_control").val() == "Group") {
        jq("#adHocPrincipalName_add_control").val("");
        jq("#adHocRoleName_add_control").val("");
        jq("#adHocPrincipalName_add").hide();
        jq("#adHocRoleName_add").hide();
        jq("#adHocGroupName_add").show();
    } else if(jq("#adHocRecipientSelector_add_control").val() == "Person") {
        jq("#adHocRoleName_add_control").val("");
        jq("#adHocGroupName_add_control").val("");
        jq("#adHocRoleName_add").hide();
        jq("#adHocGroupName_add").hide();
        jq("#adHocPrincipalName_add").show();
    }
}