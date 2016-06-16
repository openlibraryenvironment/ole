/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 6/19/13
 * Time: 4:04 PM
 * To change this template use File | Settings | File Templates.
 */
jq(document).ready(function(){
    jq(document).keypress(function(e) {
        if(e.which == 13) {
            jq("#searchReqBtn").focus().click();
            return false;
        }
    });


});
jq("#selectAll").live("click",function(){
    jq("input:checkbox").attr('checked',true);

});

jq("#deSelectAll").live("click",function(){
    jq("input:checkbox").attr('checked',false);

});



