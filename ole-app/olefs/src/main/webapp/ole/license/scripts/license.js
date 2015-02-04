jq(document).ready(function(){

    jq(document).keypress(function(e) {
    if(e.which == 13) {
        if(e.target.id == "code"){
            if(jq( "#code").val()=="" ){
                jq(this).blur();
                jq('#u77').focus().click();
            }else{
                jq('#u77').focus().click();
                return false;
            }
        }
        if(e.target.id == "name"){
            if(jq( "#name").val()=="" ){
                jq(this).blur();
                jq('#u77').focus().click();
            }else{
                jq('#u77').focus().click();
                return false;
            }
        }
        if(e.target.id == "description"){
            if(jq( "#description").val()=="" ){
                jq(this).blur();
                jq('#u77').focus().click();
            }else{
                jq('#u77').focus().click();
                return false;
            }

        }
        return false;
    }
});
});
