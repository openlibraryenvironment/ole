
function displayHoldingsRecordAuditInfo() {
    jq("#DisplayHoldingsHistorySection_div").dialog({
        modal: true,
        height: 'auto',
        width: '600px',
        draggable: true,
        resizeable: true,
        closeOnEscape: true,
        position: [500,200]
    });
}

function displayItemRecordAuditInfo() {
    jq("#DisplayItemHistorySection_div").dialog({
        modal: true,
        height: 'auto',
        width: '600px',
        draggable: true,
        resizeable: true,
        closeOnEscape: true,
        position: [500,200]
    });
}

jq(document).ready(function(){
    jq("#ExistingRecordTreeNavigation_div").html(jq("#hdnTreeData_attribute").val());

});

var newItems = 0;
var id;
jq(document).ready(function(){
    //Left-Pane mouse click event
    /*
     jq('#ExistingRecordTreeNavigation_div a').mousedown(function() {
     var instanceIndex = jq(this).attr("id").split("_")[0];
     var uuid = jq(this).attr("id").split("_")[1];
     var index = jq(this).attr("id").split("_")[2];
     var docType = jq(this).attr("id").split("_")[3];
     jq('#instanceIndex').val(instanceIndex);
     jq('#hdnUUID').val(uuid);
     jq('#hdnIndex1').val(index);
     jq('#selectedDocType').val(docType);
     jq('#hdnButton').focus().click();
     jq(".newItem").addClass('newItem');
     });
     */

    jq('#rightClick').bind('contextmenu',function(e){
        var cmenu = jq(this).next();
        jq('<div class="overlay"></div>').css({left : '0px', top : '0px',position: 'absolute', width: '100%', height: '100%', zIndex: '100' }).click(function() {
            jq(this).remove();
            jq(cmenu.hide());
        }).bind('contextmenu' , function(){return false;}).appendTo(document.body);
        jq(this).next().css({ left: e.pageX, top: e.pageY, zIndex: '101' }).show();

        oncontextmenu= " return false";
    });

    /*jq('.itemIdentifierClass').bind('contextmenu',function(e){
        var cmenu = jq(this).next();
        jq('<div class="overlay"></div>').css({left : '0px', top : '0px',position: 'absolute', width: '100%', height: '100%', zIndex: '100' }).click(function() {
            jq(this).remove();
            jq(cmenu.hide());
        }).bind('contextmenu' , function(){return false;}).appendTo(document.body);
        jq(this).next().css({ left: e.pageX, top: e.pageY, zIndex: '101' }).show();

        oncontextmenu= " return false";
    });*/

    jq('.vmenu .add_li').live('click',function() {
        if( jq(this).children().size() == 1 ) {
            // alert(jq(this).children().text());
            newItems++;
            var exists = (jq('#ExistingRecordTreeNavigation_div ul li :contains("New Item")').length);
            if (newItems > 1 && exists > 0) {
                //alert('Cannot create additional new item. Unsaved item exists.');
            } else {
                var holdingId = jq(this).attr("id");
                jq()
                var i = jq('#hdnIndex1').val()+1;
                /*jq("#ExistingRecordTreeNavigation_div ul li:first").
                 append("<ul><li><a id='new_item' class='newItem'>New Item</a></li></ul>");*/
                jq(".holdingClass").after("<ul><li><a id='new_item' class='newItem'>New Item</a></li></ul>"); //Revert this back to original
                jq(".current").removeClass('current');
                jq(".newItem").addClass('current');
                jq('#hdnAddItemButton').focus().click();
            }

            jq('.vmenu').hide();
            jq('.overlay').hide();
        }
    });

    //delete instance
    jq('.vmenu .delete_li').live('click',function() {
        if( jq(this).children().size() == 1 ) {
            // alert(jq(this).children().text());
            newItems--;
            var exists = (jq('#ExistingRecordTreeNavigation_div ul li :contains("Delete Instance")').length);
            if (newItems < 1 && exists < 0) {
                //alert('Cannot delete instance. Unsaved item exists.');
            } else {
                var holdingId = jq(this).attr("id");
                jq()
                var i = jq('#hdnIndex1').val()-1;
                /*jq("#ExistingRecordTreeNavigation_div ul li:first").
                 append("<ul><li><a id='new_item' class='newItem'>New Item</a></li></ul>");*/
                jq(".holdingClass").after("<ul><li><a id='delete_instance' class='deleteInstance'>Delete Instance</a></li></ul>"); //Revert this back to original
                jq(".current").removeClass('current');
                jq(".deleteInstance").addClass('current');
                jq('#hdnDeleteInstanceButton').focus().click();
            }

            jq('.vmenu').hide();
            jq('.overlay').hide();
        }
    });

    //delete item
    jq('#itmMenu .delete_itm').live('click',function() {
        //alert(" in delete item ");
       // alert("jq(this).children().size() --------> " + jq(this).children().size())
        //if( jq(this).children().size() == 1 ) {
            //alert(" in 1st if " + jq(this).children().text());
        //alert(" in delete item function " + id);
        jq('#deleteItemId_attribute').val(id);
       // alert(" after assigning id ");
            newItems--;
            var exists = (jq('#ExistingRecordTreeNavigation_div ul li :contains("Delete Item")').length);
            if (newItems < 1 && exists < 0) {
                alert('Cannot delete item. Unsaved item exists.');
            } else {
                //alert(" in else condition ");
                var holdingId = jq(this).attr("id");
                jq()
                var i = jq('#hdnIndex1').val()-1;
                /*jq("#ExistingRecordTreeNavigation_div ul li:first").
                 append("<ul><li><a id='new_item' class='newItem'>New Item</a></li></ul>");*/
                jq(".holdingClass").after("<ul><li><a id='delete_item' class='deleteItem'>Delete Item</a></li></ul>"); //Revert this back to original
                jq(".current").removeClass('current');
                jq(".deleteItem").addClass('current');
                jq('#hdnDeleteItemButton').focus().click();
            }

            jq('.vmenu').hide();
            jq('.overlay').hide();
        //}
    });


    jq('.vmenu .inner_li span').live('click',function() {
        //alert(jq(this).text());
        jq('.vmenu').hide();
        jq('.overlay').hide();
    });


    jq(".add_li ,delete_li, .sec_li, .inner_li span").hover(function () {
            jq(this).css({backgroundColor : '#E0EDFE' , cursor : 'pointer'});
            if ( jq(this).children().size() >0 )
                jq(this).find('.inner_li').show();
            jq(this).css({cursor : 'default'});
        },
        function () {
            jq(this).css('background-color' , '#fff' );
            jq(this).find('.inner_li').hide();
        });





    /*jq('#ExistingRecordTreeNavigation_div a').mousedown(function() {
        var uuid = jq(this).attr("id").split("_")[0];
        var index = jq(this).attr("id").split("_")[1];
        //alert(index);
        jq('#hdnUUID').val(uuid);
        jq('#hdnIndex1').val(index);
        //alert("Idx val-->" + jq('#hdnIndex1').val());
        jq('#hdnButton').focus().click();
    });*/

    jq("#navigation").treeview({
        persist: "location",
        collapsed: false,
        unique: true
    });

    //Add New Item to Instance Record
    jq(".holdingClass").bind('click', function(){
        /*
         newItems++;
         var exists = (jq('#ExistingRecordTreeNavigation_div ul li :contains("New Item")').length);
         if (newItems > 1 && exists > 0) {
         alert('Cannot create additional new item. Unsaved item exists.');
         } else {
         var holdingId = jq(this).attr("id");
         jq()
         var i = jq('#hdnIndex1').val()+1;
         */
        /*jq("#ExistingRecordTreeNavigation_div ul li:first").
         append("<ul><li><a id='new_item' class='newItem'>New Item</a></li></ul>");*//*

         jq(".holdingClass").after("<ul><li><a id='new_item' class='newItem'>New Item</a></li></ul>"); //Revert this back to original
         jq(".current").removeClass('current');
         jq(".newItem").addClass('current');
         jq('#hdnAddItemButton').focus().click();
         }
         */
    });

    //Navigating to New Item data if 'New Item' selected in Left-Pane
    jq(".newItem").live("click", function(){
        jq(".current").removeClass('current');
        jq(".newItem").addClass('current');
        jq('#getNewItemData').focus().click();
    });


    /*jq('#rightclickarea').bind('contextmenu',function(e){
     var cmenu = jq(this).next();
     jq('<div class="overlay"></div>').css({left : '0px', top : '0px',position: 'absolute', width: '100%', height: '100%', zIndex: '100' }).click(function() {
     jq(this).remove();
     cmenu.hide();
     }).bind('contextmenu' , function(){return false;}).appendTo(document.body);
     jq(this).next().css({ left: e.pageX, top: e.pageY, zIndex: '101' }).show();

     return false;
     });

     jq('.vmenu .first_li').live('click',function() {
     if( jq(this).children().size() == 1 ) {
     alert(jq(this).children().text());
     jq('.vmenu').hide();
     jq('.overlay').hide();
     }
     });

     jq('.vmenu .inner_li span').live('click',function() {
     alert(jq(this).text());
     jq('.vmenu').hide();
     jq('.overlay').hide();
     });


     jq(".first_li , .sec_li, .inner_li span").hover(function () {
     jq(this).css({backgroundColor : '#E0EDFE' , cursor : 'pointer'});
     if ( jq(this).children().size() >0 )
     jq(this).find('.inner_li').show();
     jq(this).css({cursor : 'default'});
     },
     function () {
     jq(this).css('background-color' , '#fff' );
     jq(this).find('.inner_li').hide();
     });*/

    jq('#rightClick').bind('contextmenu',function(e){
        //alert("right click");
        var cmenu = jq(this).next();
        //alert("hai" + cmenu);
        jq('<div class="overlay"></div>').css({left : '0px', top : '0px',position: 'absolute', width: '100%', height: '100%', zIndex: '100' }).click(function() {
            jq(this).remove();
            //jq(cmenu.hide());
            jq(cmenu).hide();
        }).bind('contextmenu' , function(){return false;}).appendTo(document.body);
        //alert("1111111");
        jq(this).next().css({ left: e.pageX, top: e.pageY, zIndex: '101' }).show();

        return false;
    });

    /*jq('.itemIdentifierClass').bind('contextmenu',function(e){
        //alert("right click");
        var cmenu = jq(this).next();
        //alert("hai" + cmenu);
        jq('<div class="overlay"></div>').css({left : '0px', top : '0px',position: 'absolute', width: '100%', height: '100%', zIndex: '100' }).click(function() {
            jq(this).remove();
            //jq(cmenu.hide());
            jq(cmenu).hide();
        }).bind('contextmenu' , function(){return false;}).appendTo(document.body);
        //alert("1111111");
        jq(this).next().css({ left: e.pageX, top: e.pageY, zIndex: '101' }).show();

        return false;
    });*/

    jq('#rightClick').click(function(e){
        //alert("left click");
        jq('#hdnLoadInstance').focus().click();

    });

    jq('.vmenu .add_li').live('click',function() {
        if( jq(this).children().size() == 1 ) {
            jq('.vmenu').hide();
            jq('.overlay').hide();
        }
    });

    jq('.vmenu .delete_li').live('click',function() {
        if( jq(this).children().size() == 1 ) {
            jq('.vmenu').hide();
            jq('.overlay').hide();
        }
    });

    jq('.itmMenu .delete_itm').live('click',function() {
        if( jq(this).children().size() == 1 ) {
            jq('.vmenu').hide();
            jq('.overlay').hide();
        }
    });

    jq(".itemIdentifierClass").click(function(){
        //alert(jq(this).attr("id"));
        //jq('#hdnButton').focus().click();
        var uuid = jq(this).attr("id").split("_")[0];
        var index = jq(this).attr("id").split("_")[1];
        //alert(index);
        jq('#hdnUUID').val(uuid);
        jq('#hdnIndex1').val(index);
        //alert("Idx val-->" + jq('#hdnIndex1').val());
        jq('#hdnButton').focus().click();
    });

    jq(".itemIdentifierClass").bind("contextmenu", function(e) {
        //alert(jq(this).attr("id"));
        id = jq(this).attr("id");
        //alert(" id in right click event----------->  " + id);
        jq('#itmMenu').css({
            top: e.pageY+'px',
            left: e.pageX+'px'
        }).show();
        //jq('#hdnDeleteItemButton').focus().click();
        return false;
    });
    jq('#itmMenu').click(function() {
        //alert("1");
        jq('#itmMenu').hide();
    });
    jq(document).click(function() {
        //alert("2");
        jq('#itmMenu').hide();
    });




    /*jq(".first_li").hover(function () {
     jq(this).css({backgroundColor : '#E0EDFE' , cursor : 'pointer'});
     if ( jq(this).children().size() >0 )
     jq(this).find('.inner_li').show();
     jq(this).css({cursor : 'default'});
     },
     function () {
     jq(this).css('background-color' , '#fff' );
     jq(this).find('.inner_li').hide();
     });*/
});


//Removing newly added ITEMS on CANCEL
/*function cancelNewItem(){
 if (jq('#ExistingRecordTreeNavigation_div ul li:contains("New Item")').length > 0) {
 jq(".newItem").addClass('newItemWarn');
 var answer = confirm ("An unsaved item exists are you sure you want to exit?");
 if (answer == false){
 // Nothing to do any actions.. Stay on same page.
 } else {
 //jq("#ExistingRecordTreeNavigation_div ul li:last").remove();
 jq('#ExistingRecordTreeNavigation_div ul li a:contains("New Item")').remove();
 }
 }
 }*/




