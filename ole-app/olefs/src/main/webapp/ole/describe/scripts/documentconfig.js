/*function getDocTypeAdd(id) {
    alert("DoctypeConfig");
    var JSONObject = JSON.stringify(id.extraData);//.split("actionParameters[selectedLineIndex]");
    var obj = jQuery.parseJSON(JSONObject);

    for (key in obj) {
        if (key == "actionParameters[selectedLineIndex]")
            controlField006Id = parseInt(obj[key]) + 1;
    }
    jq('#DocTypeName_line' + controlField006Id + '_control').css({"background-color": "#EFFFEF", "border-color": "#000000"});
    //jq("#Control_Field_001_h1").val('Control_Field_006_line' + controlField006Id + '_control');
    jq('#DocTypeName_line0_control').focus().click();
}*/

jq("#DocFormat-docType-Section").live("change",function() {
    jq("div#DocFormat-searchParams-Section_disclosureContent").each(function(){
        if(jq(this).find("div.uif-validationMessages")){
           jq(this).find("div.uif-validationMessages").remove();
        }
    });
    jq('#hdn_refresh_buttonDocConfig').focus().click();
});

jq("#DocFieldDocType-docType-Section_control").live("change",function() {
    jq("div#DocFieldDocType-docType-Section_disclosureContent").each(function(){
        if(jq(this).find("div.uif-validationMessages")){
            jq(this).find("div.uif-validationMessages").remove();
        }
    });
    jq('#hdn_refresh_buttonDocFormat').focus().click();
    jq('#hdn_refresh_buttonDocField').focus().click();
});


jq("#DocFieldDocFormat-docType-Section").live("change",function() {
    jq("div#DocFieldDocFormat-docType-Section_disclosureContent").each(function(){
        if(jq(this).find("div.uif-validationMessages")){
            jq(this).find("div.uif-validationMessages").remove();
        }
    });
    jq('#hdn_refresh_buttonDocField').focus().click();
});


/*jq(window).load(function () {
    jq("#DocFormat-docType-Section_control_0").attr ('checked',true);
    jq(".dataTables_length select").live("change", function () {
        localStorage.localAscSort = true;
        localStorage.localDescSort = true;
        var rows = jq(".dataTables_length select").val();
        jq("#hdnRows_control").val(rows);
        jq("#hdnSortFlag_control").val("true");
        //jq("#hdnSortOrder_control").val("");
        // jq("#hdnSortField_control").val("");
        jq("#hdn_search_button").focus().click();

    });

    jq("#search_button").live("click",function(){
        localStorage.localAscSort = true;
        localStorage.localDescSort = true;
        jq('#hdn_search_button').focus().click();
    });






});*/

/*
function getDocFormatAdd(id) {
    alert("DocformatConfig");
    var JSONObject = JSON.stringify(id.extraData);//.split("actionParameters[selectedLineIndex]");
    var obj = jQuery.parseJSON(JSONObject);

    for (key in obj) {
        if (key == "actionParameters[selectedLineIndex]")
            controlField006Id = parseInt(obj[key]) + 1;
    }
    jq('#DocFormatName_line' + controlField006Id + '_control').css({"background-color": "#EFFFEF", "border-color": "#000000"});
    //jq("#Control_Field_001_h1").val('Control_Field_006_line' + controlField006Id + '_control');
    jq('#DocFormatName_line0_control').focus().click();
}*/

function refreshDocFieldSection(){
        jq('#hdn_refresh_buttonDocFieldDocType').focus().click();
        jq('#hdn_refresh_buttonDocDocTypeMessage').focus().click();
}
