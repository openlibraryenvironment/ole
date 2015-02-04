jq(document).ready(function(){
    window.alert = function() {};
    jq("div#DetailedView-BillSection .dataTables_filter").live("keypress",function(e){
        jq(this).find("input").attr("id","DetailedView-BillSection _disclosureContent_Search_d");
    })

    jq("div#BillSection .dataTables_filter").live("keypress",function(e){
        jq(this).find("input").attr("id","BillSection_disclosureContent_Search_d");
    })
    jq('div#DetailedView-BillSection table').dataTable().fnDestroy();
    jq('div#BillView-Section table').dataTable().fnDestroy();
    var billDetailedtable = jq('div#DetailedView-BillSection table').dataTable({"aoColumnDefs":[
        {"bSortable" : false, "sType" : "string", "aTargets": [0]} ,
        {"sType" : "string", "sSortDataType" : "dom-checkbox", "aTargets": [1]} ,
    {"sType" : "numeric", "sSortDataType" : "dom-text", "aTargets": [2]} ,
    {"sType" : "date", "sSortDataType" : "dom-text", "aTargets": [3]} ,
    {"sType" : "string", "sSortDataType" : "dom-text", "aTargets": [4]} ,
    {"sType" : "string", "sSortDataType" : "dom-text", "aTargets": [5]} ,
    {"sType" : "string", "sSortDataType" : "dom-text", "aTargets": [6]} ,
    {"sType" : "kuali_currency", "sSortDataType" : "dom-text", "aTargets": [7]} ,
    {"sType" : "kuali_currency", "sSortDataType" : "dom-text", "aTargets": [8]} ,
    {"sType" : "kuali_currency", "sSortDataType" : "dom-text", "aTargets": [9]} ,
    {"sType" : "string", "sSortDataType" : "dom-text", "aTargets": [10]} ,
    {"sType" : "string", "sSortDataType" : "dom-text", "aTargets": [11]}
    ]});
    var billTable = jq('div#BillView-Section table').dataTable({"aoColumnDefs":[ {"bSortable" : false, "sType" : "string", "aTargets": [0]} , {"sType" : "string", "sSortDataType" : "dom-checkbox", "aTargets": [0]} ,
        {"sType" : "numeric", "sSortDataType" : "dom-text", "aTargets": [1]} ,
        {"sType" : "kuali_date", "sSortDataType" : "dom-text", "aTargets": [2]} ,
        {"sType" : "kuali_currency", "sSortDataType" : "dom-text", "aTargets": [3]} ,
        {"sType" : "kuali_currency", "sSortDataType" : "dom-text", "aTargets": [4]} ,
        {"sType" : "kuali_currency", "sSortDataType" : "dom-text", "aTargets": [5]} ,
        {"sType" : "string", "sSortDataType" : "dom-text", "aTargets": [6]} ,
        {"sType" : "string", "sSortDataType" : "dom-text", "aTargets": [7]} ]});
    prepareIndividualColumnSearch('DetailedView-BillSection',[0,1]);
    prepareIndividualColumnSearch('BillView-Section',[0]);
    jq(":input").live("keyup",function(){
        if(jq(this).hasClass("search_init_dataTable")){
            if (jq(this).attr("tableId") == 'BillView-Section') {
                billTable.fnFilter(this.value, jq(this).attr("index"), false, false);
            }
            if (jq(this).attr("tableId") == 'DetailedView-BillSection') {
                billDetailedtable.fnFilter(this.value, jq(this).attr("index"), false, false);

            }
        }
    });
    jq('a').live('click',function(){
        prepareInnerTransactionTableSortable();
    })

});

function prepareIndividualColumnSearch(id,indexToDisable){
    var divElement=jq('div#'+id+'');
    var tableElement=jq(divElement).find("table").find("tfoot").find("tr");
    var i=0;
    jq(tableElement).each(function () {
        if (i == 0) {
            jq(this).find('th').each(function () {
                var display = true;
                var indexToCompare=jq(this).index();
                jq.each(indexToDisable,function( index, value ) {
                    if(indexToCompare==value){
                        display=false;
                    }
                });
                if (display) {
                    var title = jq('div#'+id+' table thead th').eq(jq(this).index()).text();
                    jq(this).html('<input type="text" size="10" tableId="'+id+'" index="' + jq(this).index() + '" id="search_dataTable_' + jq(this).index() + '" class="search_init_dataTable" placeholder="Search ' + title + '"/>');
                }
            })

        }
        i++;

    })
    var tableElement=jq(divElement).find("table").find("tbody").find("tr");
    jq(tableElement).each(function () {
        jq(this).find('table').each(function () {
            jq(this).removeAttr("style");
            jq(this).css('{width:1326px}');
        })
    })

}

function prepareInnerTransactionTableSortable(){
    var lineNumber=0;
    jq('div').each(function() {
        var divId=jq(this).attr("id");
        if(divId!=undefined){
            if(divId==("billView_itemLevelTransaction_line"+lineNumber)){
                var id=jq("#billView_itemLevelTransaction_line"+lineNumber+"_disclosureContent"+" table").attr("id");
                if(id!=undefined){
                    jq("table#"+id).dataTable().fnDestroy();
                    jq("table#"+id).css({'width':"1325px"});
                    jq("table#"+id).dataTable({"aoColumnDefs":[
                        {"sType" : "numeric", "sSortDataType" : "dom-text", "aTargets": [0]} ,
                        {"sType" : "date", "sSortDataType" : "dom-text", "aTargets": [1]} ,
                        {"sType" : "string", "sSortDataType" : "dom-text", "aTargets": [2]} ,
                        {"sType" : "kuali_currency", "sSortDataType" : "dom-text", "aTargets": [3]} ,
                        {"sType" : "string", "sSortDataType" : "dom-text", "aTargets": [4]} ,
                        {"sType" : "string", "sSortDataType" : "dom-text", "aTargets": [5]} ,
                        {"sType" : "string", "sSortDataType" : "dom-text", "aTargets": [6]}]})

                }
                lineNumber++;
            }
        }
    })
}
