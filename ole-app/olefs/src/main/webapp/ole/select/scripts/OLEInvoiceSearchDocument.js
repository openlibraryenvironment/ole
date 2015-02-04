/**
 * Created with IntelliJ IDEA.
 * User: aurojyotit
 * Date: 9/18/13
 * Time: 5:23 PM
 * To change this template use File | Settings | File Templates.
 */
jq(document).ready(function(){
    jq(document).keypress(function(e) {
        if(e.which == 13) {
            e.preventDefault();
            return false;
        }
    })
});

