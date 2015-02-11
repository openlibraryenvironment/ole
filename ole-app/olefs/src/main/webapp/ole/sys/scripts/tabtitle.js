/**
 * Created by premkb on 1/22/15.
 */
var jq = jQuery.noConflict();
jq(window).load(function(){
    if(jq("h1").hasClass("uif-viewHeader")){
        title = jq("span:first").text();
        parent.document.title=title;
    }else if(jq("div").hasClass("uif-headerText")){
        title = jq("span:first").text();
        parent.document.title=title;
    }else if(jq("div").hasClass("headerarea")){
        title = jq("h1:first").text();
        parent.document.title=title;
    }else if(jq("div").hasClass("headerarea-small")){
        title = jq("h1:first").text();
        parent.document.title=title;
    }else if(jq("span").hasClass("uif-headerText-span")){
        title = jq("span:first").text();
        parent.document.title=title;
    }
});

