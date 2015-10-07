jq(document).ready(function(){

    jq("#create_noticeType_control").live("change",function() {

        submitForm('refresh', null, null, null, null);
    });

});