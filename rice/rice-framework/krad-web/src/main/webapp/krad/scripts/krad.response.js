/*
 * Copyright 2005-2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * Processes a response that has been returned from an Ajax call
 *
 * @param contents - the response contents (or body)
 */
function KradResponse(contents) {
    this.responseContents = contents;
}

KradResponse.prototype = {
    // full response contents
    responseContents: null,

    // maps return types to handler function names
    handlerMapping: {"update-page": "updatePageHandler", "update-component": "updateComponentHandler",
        "update-view": "updateViewHandler", "redirect": "redirectHandler",
        "display-lightbox": "displayLightBoxHandler", "update-dialog":"updateDialogHandler"},

    // invoked to process the response contents by invoking necessary handlers
    processResponse: function () {
        var responseFn = this;

        // iterate over returned contents divs and invoke handler
        jQuery(this.responseContents).children().each(function () {
            var div = jQuery(this);

            // get the return type sent by the server
            var returnType = div.data("returntype");

            // find the handler function from the mapping
            var functionName = responseFn.handlerMapping[returnType];
            var handlerFunc = responseFn[functionName];

            // invoke the handler function
            if (handlerFunc) {
                handlerFunc(div, div.data());
            }

            hideEmptyCells();
        });
    },

    // finds the page content in the returned content and updates the page, then processes breadcrumbs and hidden
    // scripts. While processing, the page contents are hidden
    updatePageHandler: function (content, dataAttr) {
        var page = jQuery("#page_update", content);

        // TODO: should this be hiding page or pageInLayout?
        page.hide();

        // give a selector that will avoid the temporary iframe used to hold ajax responses by the jquery form plugin
        var pageInLayout = "#" + kradVariables.VIEW_CONTENT_HEADER_CLASS + " > #" +
                kradVariables.PAGE_CONTENT_WRAPPER;
        hideBubblePopups(pageInLayout);

        // update page contents from response
        jQuery(pageInLayout).empty().append(page.find(">*"));

        pageValidatorReady = false;
        runHiddenScripts(jQuery(pageInLayout).attr("id"), false, true);

        jQuery(pageInLayout).show();
    },


    // finds the dialog content in the returned content and updates the view
    updateDialogHandler: function (content, dataAttr) {
        var id = dataAttr.updatecomponentid;
        var component = jQuery("#" + id + "_update", content);

        // remove old stuff
        if (jQuery("#" + id + "_errors").length) {
            jQuery("#" + id + "_errors").remove();
        }

        jQuery("input[data-for='" + id + "']").each(function () {
            jQuery(this).remove();
        });

        // replace component
        if (jQuery("#" + id).length) {
            jQuery("#" + id).replaceWith(component.html());
        }

        runHiddenScripts(id);
    },


    // retrieves the component with the matching id from the server and replaces a matching
    // _refreshWrapper marker span with the same id with the result.  In addition, if the result contains a label
    // and a displayWith marker span has a matching id, that span will be replaced with the label content
    // and removed from the component.  This allows for label and component content separation on fields
    updateComponentHandler: function (content, dataAttr) {
        var id = dataAttr.updatecomponentid;
        var elementToBlock = jQuery("#" + id);

        hideBubblePopups(elementToBlock);

        var component = jQuery("#" + id + "_update", content);

        var displayWithId = id;

        // special label handling, if any
        var theLabel = jQuery("#" + displayWithId + "_label_span", component);
        if (jQuery(".displayWith-" + displayWithId).length && theLabel.length) {
            theLabel.addClass("displayWith-" + displayWithId);
            jQuery("span.displayWith-" + displayWithId).replaceWith(theLabel);
            component.remove("#" + displayWithId + "_label_span");
        }

        // remove old stuff
        if (jQuery("#" + id + "_errors").length) {
            jQuery("#" + id + "_errors").remove();
        }

        jQuery("input[data-for='" + id + "']").each(function () {
            jQuery(this).remove();
        });

        // replace component
        if (jQuery("#" + id).length) {
            jQuery("#" + id).replaceWith(component.html());
        }

        if (jQuery("#" + id).parent().is("td")) {
            jQuery("#" + id).parent().show();
        }

        // lightbox specific processing
        if (jQuery('#renderedInLightBox').val() == 'true') {
            jQuery("#" + id).css('display', 'none');
        }

        var newComponent = jQuery("#" + id);

        var displayWithLabel = jQuery(".displayWith-" + displayWithId);
        displayWithLabel.show();
        if (displayWithLabel.parent().is("td") || displayWithLabel.parent().is("th")) {
            displayWithLabel.parent().show();
        }

        // assume this content is open if being refreshed
        var open = newComponent.attr("data-open");
        if (open != undefined && open == "false"){
            newComponent.attr("data-open", "true");
            newComponent.show();
        }

        // runs scripts on the span or div with id
        runHiddenScripts(id);

        // Only for table layout collections. Keeps collection on same page.
        var currentPage = retrieveFromSession(id + ":currentPageRichTable");
        if (currentPage != null) {
            openDataTablePage(id, currentPage);
        }

        elementToBlock.unblock({onUnblock: function () {
            jQuery(component).find("#" + id).addClass(kradVariables.PROGRESSIVE_DISCLOSURE_HIGHLIGHT_CLASS);
            newComponent.animate({backgroundColor:"transparent"}, 6000);
            jQuery(component).find("#" + id).animate({backgroundColor:"transparent"}, 6000);
            }
        });
    },

    // performs a redirect to the URL found in the returned contents
    redirectHandler: function (content, dataAttr) {
        // get url contents between div
        var redirectUrl = jQuery(content).text().trim();

        // don't check dirty state on a simple refresh (old url starts with the new one's url text)
        if (window.location.href.indexOf(redirectUrl) === 0) {
            dirtyFormState.skipDirtyChecks = true;
        }

        // redirect
        window.location.href = redirectUrl;
    },

    // replaces the view with the given content and run the hidden scripts
    updateViewHandler: function (content, dataAttr) {
        jQuery('#' + kradVariables.APP_ID).replaceWith(content);

        runHiddenScriptsAgain();
    },

    // displays the response contents in a lightbox
    displayLightBoxHandler: function (content, dataAttr) {
        showLightboxContent(content);
    }
}