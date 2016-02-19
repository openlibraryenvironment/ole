/**
 * Created by maheswarang on 2/15/16.
 */
jq(".dataTables_length").live("change",function(){
    var rows = jq(".dataTables_length select").val();
    jq('#pageSize').val(rows);
 jq('#notice_search_button').focus().click();
})
