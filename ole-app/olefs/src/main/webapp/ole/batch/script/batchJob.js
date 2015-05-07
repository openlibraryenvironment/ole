/**
 * Created with IntelliJ IDEA.
 * User: rajeshbabuk
 * Date: 8/2/13
 * Time: 8:11 PM
 * To change this template use File | Settings | File Templates.
 */

/*function cancel(method,line){
 alert("cancel1");
 alert(jq("#jobId_control").val(line));
 jq("#jobId_control").val(line);
 alert("cancel2");
 submitForm(method, null, null, null, null);
 }*/

jq(document).ready(function(){
//    setInterval("location.reload()", 60000);
    jq(".dataTables_filter").css("display:none")
});


function stop(line){
    jq("#jobId_control").val(line);
    jq("#hdnStop_button").focus().click();
}

function refresh(line){
    jq("#jobId_control").val(line);
    jq("#BatchProcessJobDetail_Refresh_Hidden").focus().click();
}

function start(line){
    jq("#jobId_control").val(line);
    jq("#hdnStart_button").focus().click();
}

function remove(line){
    jq("#jobId_control").val(line);
    jq("#hdnRemove_button").focus().click();
}

function pause(line){
    jq("#jobId_control").val(line);
    jq("#hdnPause_button").focus().click();
}

function resume(line){
    jq("#jobId_control").val(line);
    jq("#hdnResume_button").focus().click();
}

function stopCallback(){
    //jq(document).trigger('load');
    window.location.reload();
}


