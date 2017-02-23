jq(document).ready(function(){

    jq("#create_noticeType_control").live("change",function() {

        submitForm('refresh', null, null, null, null);
    });

    jq("#create_noticeType_comp1_control").live("change",function() {

        submitForm('refresh', null, null, null, null);
    });

});