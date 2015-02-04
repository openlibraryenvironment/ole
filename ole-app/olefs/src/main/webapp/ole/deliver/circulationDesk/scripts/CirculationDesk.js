jq(document).ready(function(){
    jq(".defaultClass").live("click",function(){
        jq(".defaultClass").removeAttr("checked");
        jq("#"+jq(this).attr('id')).attr("checked","true");
        var allowedId = jq(this).attr('id');
        allowedId = allowedId.replace("defaultLocation","allowedLocation");
        jq("#"+allowedId).removeAttr("checked");
    });

    jq(".allowedClass").live("click",function(){
        var allowedId = jq(this).attr('id');
        var newAllowedId = allowedId.replace("allowedLocation","defaultLocation");
        if(jq("#"+newAllowedId).is(':checked')){
            jq("#"+allowedId).removeAttr("checked");
        }
    });

});