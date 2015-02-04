/**
 * Created with IntelliJ IDEA.
 * User: aurojyotit
 * Date: 8/23/13
 * Time: 12:49 PM
 * To change this template use File | Settings | File Templates.
 */

jq(document).ready(function () {
    jq("input:text").live("click", function () {
        if (jq(this).attr("id") == undefined) {
            if (jq(this).parent().parent().attr("class") == "dataTables_filter") {
                jq(this).attr("id", "dataTextSearchBox");
            }
        }
    })
});

