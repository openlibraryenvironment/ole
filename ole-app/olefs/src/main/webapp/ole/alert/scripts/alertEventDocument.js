/**
 * Created by maheswarang on 12/26/14.
 */
jq(document).ready(function(){

jq("alert_field_name_add_control").change(function(){
alert("hi")
    //alert( "fieldtype" );
    var value= jq("alert_field_name_add_control option:selected").test();
    alert(value)
    if(value == "boolean"){
        //alert( "boolean" );
        jq('#fieldValue_add_control').attr('maxlength','1');
    }
    else{
        jq('#fieldValue_add_control').attr('maxlength','100');
    }
});
});