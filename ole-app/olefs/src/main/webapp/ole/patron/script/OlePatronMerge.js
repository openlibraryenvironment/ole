jq(document).ready(function(){
    jq(".survivorCheck").live("click",function(){
        jq(".survivorCheck").removeAttr("checked");
        jq("#"+jq(this).attr('id')).attr("checked","true");
        var duplicateRecordId = jq(this).attr('id');
        duplicateRecordId = duplicateRecordId.replace("checkSurvivor","checkDuplicatePatron");
        jq("#"+duplicateRecordId).removeAttr("checked");
    });

    jq(".duplicateCheck").live("click",function(){
        var duplicateRecordId = jq(this).attr('id');
        var newDuplicateRecordId = duplicateRecordId.replace("checkDuplicatePatron","checkSurvivor");
        if(jq("#"+newDuplicateRecordId).is(':checked')){
            jq("#"+duplicateRecordId).removeAttr("checked");
        }
    });

});