jq(document).ready(function(){
    jq("#hiddenSelectedTab_control").hide();
    jq("#EResourceChangesDashBoard-TabSection_tabs").tabs({
        activate: function (event, ui) {
            var active = jq('#EResourceChangesDashBoard-TabSection_tabs').tabs('option', 'active');
            if (active == new String("0")) {
                jq("#hiddenSelectedTab_control").val("To-Do");
            } else {
                jq("#hiddenSelectedTab_control").val("Change-Log");
            }
            submitForm('selectTab', null, null, null, null);
        }
    });

});
