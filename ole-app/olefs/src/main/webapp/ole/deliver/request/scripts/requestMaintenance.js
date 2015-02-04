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
