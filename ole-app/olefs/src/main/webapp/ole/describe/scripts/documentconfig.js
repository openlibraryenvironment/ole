jq("#DocFormat-docType-Section").live("change", function () {
    jq("div#DocFormat-searchParams-Section_disclosureContent").each(function () {
        if (jq(this).find("div.uif-validationMessages")) {
            jq(this).find("div.uif-validationMessages").remove();
        }
    });
    jq('#hdn_refresh_buttonDocConfig').focus().click();
});

jq("#DocFieldDocType-docType-Section_control").live("change", function () {
    jq("div#DocFieldDocType-docType-Section_disclosureContent").each(function () {
        if (jq(this).find("div.uif-validationMessages")) {
            jq(this).find("div.uif-validationMessages").remove();
        }
    });
    jq('#hdn_refresh_buttonDocFormat').focus().click();
    jq('#hdn_refresh_buttonDocField').focus().click();
});


jq("#DocFieldDocFormat-docType-Section").live("change", function () {
    jq("div#DocFieldDocFormat-docType-Section_disclosureContent").each(function () {
        if (jq(this).find("div.uif-validationMessages")) {
            jq(this).find("div.uif-validationMessages").remove();
        }
    });
    jq('#hdn_refresh_buttonDocField').focus().click();
});

function refreshDocFieldSection() {
    jq('#hdn_refresh_buttonDocFieldDocType').focus().click();
    jq('#hdn_refresh_buttonDocDocTypeMessage').focus().click();
}

location.href = "javascript:(" + function () {
    window.onbeforeunload = null;
    window.onunload = null;
} + ")()";
