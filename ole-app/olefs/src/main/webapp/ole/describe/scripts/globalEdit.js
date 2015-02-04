

function viewGlobalEdit(docType){
    window.open("editorcontroller?viewId=EditorView&methodToCall=start&docType="+docType+"&editable=true&globalEditFlag=true&docCategory=work&docFormat=oleml");
    return false;
}

jq(window).load(function () {
    jq("#GlobalEditView-Type-Section_control_0").attr ('checked',true);
    jq("#GlobalEdit-SearchPanel-docType-Section_control_0").attr("checked", true);



    jq("#GlobalEditView-Type-Section_control_1").click(function(){
        window.setTimeout(function () {
            jq("#GlobalEditView-Import-DocType_control_0").attr ('checked',true);
            jq("#GlobalEditView-Import-FieldType_1_control_0").attr ('checked',true);
        }, 500)

    });


});

function editImport(){
    jq("#GlobalEdit-SearchPanel-docType-Section_control_0").attr ('checked',true);
}

function editDocType(){
    jq('#GlobalEditView-Import-FieldType_2').css({"display": ""});
    if(jq('#GlobalEditView-Import-DocType_control_2').is(':checked'))  {
        jq("#GlobalEditView-Import-FieldType_1").hide();
        jq("#GlobalEditView-Import-FieldType_2").show();
    }
    if(jq('#GlobalEditView-Import-DocType_control_0').is(':checked')||jq('#GlobalEditView-Import-DocType_control_1').is(':checked'))  {
        jq("#GlobalEditView-Import-FieldType_2").hide();
        jq("#GlobalEditView-Import-FieldType_1").show();
    }

}
