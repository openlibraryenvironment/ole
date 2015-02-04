jq(document).ready(function(){

    jq(document).keypress(function(e) {
    if(e.which == 13) {
        if(e.target.id == "agreementTitle"){
            if(jq( "#agreementTitle").val()=="" ){
                jq(this).blur();
                jq('#u77').focus().click();
            }else{
                jq('#u77').focus().click();
                return false;
            }
        }
        if(e.target.id == "contractNumber"){
            if(jq( "#contractNumber").val()=="" ){
                jq(this).blur();
                jq('#u77').focus().click();
            }else{
                jq('#u77').focus().click();
                return false;
            }
        }
        if(e.target.id == "licensee"){
            if(jq( "#licensee").val()=="" ){
                jq(this).blur();
                jq('#u77').focus().click();
            }else{
                jq('#u77').focus().click();
                return false;
            }
        }
        if(e.target.id == "licensor"){
            if(jq( "#licensor").val()=="" ){
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
