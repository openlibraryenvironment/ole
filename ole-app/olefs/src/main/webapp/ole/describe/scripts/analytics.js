/**
 * Created with IntelliJ IDEA.
 * User: rajeshbabuk
 * Date: 3/3/14
 * Time: 8:13 PM
 * To change this template use File | Settings | File Templates.
 */

jq(document).ready(function () {
    window.setTimeout(function () {
        jq("a.analytics ins.jstree-icon").removeClass("jstree-icon");
        jq("a.boundWithbibs ins.jstree-icon").removeClass("jstree-icon");
        jq("#SeriesTree_expandAll").focus().click();
        jq("#AnalyticTree_expandAll").focus().click();

        jq("#AnalyticTree a").each(function () {
            var id1 = jq(this).attr("class");

            if (id1.indexOf("wio") !=-1) {
                var id2 = jq(this).next().attr("id");
                if (id2 == null) {
                    id2 = jq(this).next().next().attr("id")
                }
                if (id2 != null) {
                    var id3 = jq("#" + id2 + " .uif-checkboxControl").attr("id");
                    jq("#" + id3).removeAttr("disabled");
                }

            }
        });

    }, 200);
});

function analyticsSummaryTree() {

    window.setTimeout(function () {
        jq("a.analytics ins.jstree-icon").removeClass("jstree-icon");
        jq("a.boundWithbibs ins.jstree-icon").removeClass("jstree-icon");
        jq("#SeriesTree_expandAll").focus().click();
        jq("#AnalyticTree_expandAll").focus().click();

        jq("#AnalyticTree a").each(function () {
            var id1 = jq(this).attr("class");

            if (id1.indexOf("wio") !=-1) {
                var id2 = jq(this).next().attr("id");
                if (id2 == null) {
                    id2 = jq(this).next().next().attr("id")
                }
                if (id2 != null) {
                    var id3 = jq("#" + id2 + " .uif-checkboxControl").attr("id");
                    jq("#" + id3).removeAttr("disabled");
                }

            }
        });

    }, 200);
}

function refresh() {
    jq('#rightTreeRefreshButtonForAnalytics').focus().click();
    jq('#leftTreeRefreshButtonForAnalytics').focus().click();
}

//REMOVE ANALYTICS FOLDER IN TREE

/*function analyticsResult(methodToCall) {
    retrieveComponent('AnalyticsSelectionSection', methodToCall, null, null);
    jq('#rightTreeRefreshButtonForAnalytics').focus().click();
    jq('#leftTreeRefreshButtonForAnalytics').focus().click();
}*/

function analyticsTree() {

    window.setTimeout(function () {
        jq("a.analytics ins.jstree-icon").removeClass("jstree-icon");
        jq("a.boundWithbibs ins.jstree-icon").removeClass("jstree-icon");
        jq("#SeriesTreeView_expandAll").focus().click();
        jq("#AnalyticTreeView_expandAll").focus().click();

        jq("#AnalyticsLeftTree_tree a").each(function () {

            var id1 = jq(this).attr("class");

            if (id1.indexOf("wio") !=-1) {
                var id2 = jq(this).next().attr("id");
                if (id2 == null) {
                    id2 = jq(this).next().next().attr("id")
                }
                if (id2 != null) {
                    var id3 = jq("#" + id2 + " .uif-checkboxControl").attr("id");
                    jq("#" + id3).attr("disabled","true");
                }
            }

        });

        jq("#AnalyticsRightTree a").each(function () {

            var id1 = jq(this).attr("class");

            if (id1.indexOf("who") !=-1) {
                var id2 = jq(this).next().attr("id");
                if (id2 == null) {
                    id2 = jq(this).next().next().attr("id");
                }
                if (id2 != null) {
                    var id3 = jq("#" + id2 + " .uif-checkboxControl").attr("id");
                    jq("#" + id3).attr("disabled","true");
                }
            }
        });

    }, 300);
}