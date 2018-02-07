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
/** Navigation */

/**
 * Setup the breadcrumbs for this view by replacing the old breadcrumbs with the newest from the page
 *
 * @param displayBreadcrumbsWhenOne display the breadcrumbs when there is only one when true, otherwise do not
 */
function setupBreadcrumbs(displayBreadcrumbsWhenOne) {
    var breadcrumbsWrapper = jQuery("div#Uif-BreadcrumbWrapper");

    if (!breadcrumbsWrapper.length) {
        return;
    }

    //clear the old breadcrumbs
    breadcrumbsWrapper.empty();
    breadcrumbsWrapper.show();

    //find the new ones
    var breadcrumbList = jQuery("div#Uif-BreadcrumbUpdate > ol").detach();
    var items = breadcrumbList.find("> li");

    //dont display if display when one is false and there is only one item
    if ((!displayBreadcrumbsWhenOne && items.length == 1) || items.length == 0) {
        breadcrumbsWrapper.hide();
        return;
    }

    //set up sibling breadcrumb handler
    jQuery(breadcrumbList).on("click", ".uif-breadcrumbSiblingLink", function () {
        var content = jQuery(this).parent().find("div.uif-breadcrumbSiblingContent");
        var breadcrumb = jQuery(this).parent().find("[data-role='breadcrumb']");
        var siblingLink = this;

        if (content.length && !content.is(":visible") && breadcrumb.length && !jQuery(siblingLink).data("close")) {
            content.attr("style", "");
            content.position({
                my: "left top",
                at: "left bottom+5",
                of: breadcrumb
            });
            content.show();

            jQuery(document).on("mouseup.bc-sibling", function (e) {
                var container = jQuery("div.uif-breadcrumbSiblingContent:visible");

                //if not in the breadcrumb sibling content, close and remove this handler
                if (container.has(e.target).length === 0) {
                    container.hide();
                    jQuery(document).off("mouseup.bc-sibling");
                }

                //if the target clicked is the siblingLink, mark it with a close flag (so click handler does not
                //reopen - processed after the mouseup)
                if (e.target == siblingLink) {
                    jQuery(siblingLink).data("close", true);
                }
            });

        }

        //remove the close flag
        if (jQuery(siblingLink).data("close")) {
            jQuery(siblingLink).data("close", false);
        }
    });

    //if the last item has a link, make it a span
    var lastLink = items.last().find("> a[data-role='breadcrumb']");
    if (lastLink.length) {
        lastLink.replaceWith(function () {
            return jQuery("<span data-role='breadcrumb'>" + jQuery(this).html() + "</span>");
        });
    }

    //append to the wrapper
    jQuery("div#Uif-BreadcrumbWrapper").append(breadcrumbList);
}

function setupLocationSelect(controlId) {
    var control = jQuery("select#" + controlId);
    if (control.length) {
        //navigate if the value changes
        control.on("change", function () {
            var selectedOption = jQuery(this).find("option:selected");
            if (!selectedOption.length) {
                return;
            }

            var location = selectedOption.data("location");
            if (!location) {
                return;
            }

            window.location.href = location;
        });

        //navigate when the same option is clicked
        control.find("option").on("mouseup", function () {
            var selectedOption = jQuery(this);
            if (!selectedOption && selectedOption.is("selected")) {
                return;
            }

            var location = selectedOption.data("location");
            if (!location) {
                return;
            }

            window.location.href = location;
        });

        //navigate on enter event
        control.on("keyup", function (e) {
            if (e.keyCode == 13) {
                var selectedOption = jQuery(this).find("option:selected");
                if (!selectedOption.length) {
                    return;
                }

                var location = selectedOption.data("location");
                if (!location) {
                    return;
                }

                window.location.href = location;
            }
        });
    }
}

/**
 * Renders a navigation group for the list with the given id. Helper methods are
 * called based on the type to implement a certain style of navigation.
 *
 * @param listId -
 *          unique id for the unordered list
 * @param navigationType -
 *          the navigation style to render
 */
function createNavigation(listId, navigationType, options) {
    if (navigationType == "VERTICAL_MENU") {
        createVerticalMenu(listId, options);
    }
    else if (navigationType == "TAB_MENU") {
        createTabMenu(listId, options);
    }
}

function createTabMenu(listId, options) {
    jQuery(document).ready(function () {
        jQuery("#" + listId).tabMenu(options);
    });
}

/**
 * Uses jQuery menu plug-in to build a menu for the list with the given id
 *
 * @param listId -
 *          unique id for the unordered list
 */
function createVerticalMenu(listId, options) {
    jQuery(document).ready(function () {
        jQuery("#" + listId).navMenu(options);
    });
}

/** Widgets */

/**
 * Sets ups a text popout button and window for this particular field that will be generated
 * when that button is clicked
 *
 * @param id - id of the control
 * @param label - label to be used in popout
 * @param summary - summary to be used in popout
 * @param constraint - constraint to be used in popout
 * @param imageUrl - the url for the popout icon
 */
function setupTextPopout(id, label, summary, constraint, imageUrl) {
    var options = {label: label, summary: summary, constraint: constraint};
    jQuery("#" + id).initPopoutText(options, imageUrl);
}

/**
 * Uses jQuery fancybox to open a lightbox for a link's content. The second
 * argument is a Map of options that are available for the FancyBox. See
 * <link>http://fancybox.net/api</link> for documentation on these options.
 * The third argument should only be true for inquiries and lookups.  When this
 * argument is true additional URL parameters are added for the bread crumbs history.
 *
 * @param linkId -
 *          id for the link that the fancybox should be linked to
 * @param options -
 *          map of option settings (option name/value pairs) for the plugin
 * @parm isAddAppParms -
 *          true if application parameters should be added to the link, false otherwise
 */
function createLightBoxLink(linkId, options, addAppParms) {
    jQuery(function () {
        var renderedInLightBox = isCalledWithinLightbox();

        // first time content is brought up in lightbox we don't want to continue history
        var flow = 'start';
        if (renderedInLightBox) {
            flow = jQuery("#flowKey").val();
        }

        var link = jQuery("#" + linkId);
        // Check if this is called within a light box
        if (!renderedInLightBox) {
            // If this is not the top frame, then create the lightbox
            // on the top frame to put overlay over whole window
            link.click(function (e) {
                e.preventDefault();

                options['href'] = link.attr('href');
                getContext().fancybox(options);
            });
        } else {
            link.attr('target', '_self');
        }

        if (addAppParms) {
            // Set the renderedInLightBox = true param
            if (link.attr('href').indexOf('&renderedInLightBox=true') == -1) {
                var href = link.attr('href');

                link.attr('href', href + '&renderedInLightBox=true&flow=' + flow);
            }
        }
    });
}

function handleLightboxOpen(link, options, addAppParms, event) {
    event.preventDefault();
    var renderedInLightBox = isCalledWithinLightbox();

    // first time content is brought up in lightbox we don't want to continue history
    var flow = 'start';
    if (renderedInLightBox) {
        flow = jQuery("#flowKey").val();
    }

    if (addAppParms) {
        // Set the renderedInLightBox = true param
        if (link.attr('href').indexOf('&renderedInLightBox=true') == -1) {
            var href = link.attr('href');

            //set lightbox flag and continue flow
            link.attr('href', href + '&renderedInLightBox=true&flow=' + flow);
        }
    }

    // Check if this is called within a light box
    if (!renderedInLightBox) {
        // If this is not the top frame, then create the lightbox
        // on the top frame to put overlay over whole window
        options['href'] = link.attr('href');
        getContext().fancybox(options);
    } else {
        window.location = link.attr('href');
    }
}

/**
 * Submits the form based on the quickfinder action identified by the given id and display the result content in
 * a lightbox using the jQuery fancybox. If we are not currently in a lightbox, we will request a redirect URL
 * for the lightbox contents. Otherwise, the internal iframe of the lightbox will be redirected.
 *
 * <p>
 * See <link>http://fancybox.net/api</link> for documentation on plugin options
 * </p>
 *
 * @param componentId -
 *          id for the action component that the fancybox should be linked to
 * @param options -
 *          map of option settings (option name/value pairs) for the fancybox plugin
 * @param lookupReturnByScript - boolean that indicates whether the lookup should return through script
 *        or via a server post
 */
function createLightBoxPost(componentId, options, lookupReturnByScript) {
    jQuery(function () {
        // get data that should be submitted when the action is selected
        var data = {};

        var submitData = jQuery("#" + componentId).data(kradVariables.SUBMIT_DATA);
        jQuery.extend(data, submitData);

        // Check if this is not called within a lightbox
        var renderedInLightBox = isCalledWithinLightbox();
        if (!renderedInLightBox) {
            jQuery("#" + componentId).click(function (e) {
                // Prevent the default submit
                e.preventDefault();

                data['jumpToId'] = componentId;
                data['ajaxRequest'] = 'true';
                data['actionParameters[renderedInLightBox]'] = 'true';
                data['actionParameters[flowKey]'] = 'start';
                data['actionParameters[returnByScript]'] = '' + lookupReturnByScript;

                // If this is the top frame, the page is not displayed in the iframeprotlet
                // set the return target
                if (top == self) {
                    data['actionParameters[returnTarget]'] = '_parent';
                } else {
                    data['actionParameters[returnTarget]'] = 'iframeportlet';
                }

                var jsonViewState = getSerializedViewState();
                if (jsonViewState) {
                    jQuery.extend(data, {clientViewState: jsonViewState});
                }

                // if refreshing the view on return from lookup need to clear dirty fields else
                // a warning is given
                if (!lookupReturnByScript) {
                    dirtyFormState.skipDirtyChecks = true;
                }

                // Do the Ajax submit on the kualiForm form
                jQuery("#kualiForm").ajaxSubmit({
                    data: data,
                    success: function (data) {
                        // Perform cleanup when lightbox is closed
                        // TODO: this stomps on the post form (clear out) so need to another
                        // way to clear forms when the lightbox performs a post back
                        // options['beforeClose'] = cleanupClosedLightboxForms;

                        // get the lookup redirect URL from the response
                        var lookupUrl = jQuery(data).text();

                        // Add the returned URL to the FancyBox href setting
                        options['href'] = lookupUrl.replace(/&amp;/g, '&');

                        // Open the light box
                        getContext().fancybox(options);
                    }
                });
            });
        } else {
            // add parameters for lightbox and do standard submit
            jQuery("#" + componentId).click(function (e) {
                // Prevent the default submit
                e.preventDefault();

                data['actionParameters[renderedInLightBox]'] = 'true';
                data['actionParameters[returnTarget]'] = '_self';
                data['actionParameters[flowKey]'] = jQuery("#flowKey").val();

                nonAjaxSubmitForm(data['methodToCall'], data);
            });
        }
    });
}

/**
 * Check if the code is inside a lightbox
 *
 * @return true if called within a lightbox, false otherwise
 */
function isCalledWithinLightbox() {
    if (jQuery('#renderedInLightBox').val() == undefined) {
        return false;
    }

    return jQuery('#renderedInLightBox').val().toUpperCase() == 'TRUE' ||
            jQuery('#renderedInLightBox').val().toUpperCase() == 'YES';
    // reverting for KULRICE-8346
//    try {
//        // For security reasons the browsers will not allow cross server scripts and
//        // throw an exception instead.
//        // Note that bad browsers (e.g. google chrome) will not catch the exception
//        if (jQuery("#fancybox-frame", parent.document).length) {
//            return true;
//        }
//    }
//    catch (e) {
//        // ignoring error
//    }
//
//    return false;
}

/*
 * Reload page with lookup result URL
 */
function returnLookupResultReload(href, target) {
    if (parent.jQuery('iframe[id*=easyXDM_]').length > 0) {
        // portal and content on same domain
        top.jQuery('iframe[id*=easyXDM_]').contents().find('#' + kradVariables.PORTAL_IFRAME_ID).attr('src', href);
    } else if (parent.parent.jQuery('#' + kradVariables.PORTAL_IFRAME_ID).length > 0) {
        // portal and content on different domain
        parent.parent.jQuery('#' + kradVariables.PORTAL_IFRAME_ID).attr('src', href)
    } else {
        window.open(href, target);
    }
}

/*
 * Function that returns lookup results by script
 */
function returnLookupResultByScript(fieldName, value) {
    var returnField;
    if (parent.jQuery('iframe[id*=easyXDM_]').length > 0) {
        // portal and content on same domain
        returnField = top.jQuery('iframe[id*=easyXDM_]').contents().find('#' + kradVariables.PORTAL_IFRAME_ID).contents().find('[name="' + escapeName(fieldName) + '"]');
    } else if (parent.parent.jQuery('#' + kradVariables.PORTAL_IFRAME_ID).length > 0) {
        // portal and content on different domain
        returnField = parent.parent.jQuery('#' + kradVariables.PORTAL_IFRAME_ID).contents().find('[name="' + escapeName(fieldName) + '"]');
    } else {
        returnField = top.jq('[name="' + escapeName(fieldName) + '"]');
    }

    if (!returnField.length) {
        return;
    }

    returnField.val(value);
    returnField.focus();
    returnField.blur();
    returnField.focus();

    // trigger change event
    returnField.change();
}

/*
 * Function that sets the return target when returning multiple lookup results
 */
function setMultiValueReturnTarget() {
    if (parent.jQuery('iframe[id*=easyXDM_]').length > 0) {
        // portal and content on same domain
        top.jQuery('iframe[id*=easyXDM_]').contents().find('#' + kradVariables.PORTAL_IFRAME_ID).contents().find('#' + kradVariables.KUALI_FORM).attr('target', kradVariables.PORTAL_IFRAME_ID);
    } else if (parent.parent.jQuery('#' + kradVariables.PORTAL_IFRAME_ID).length > 0) {
        // portal and content on different domain
        parent.jQuery('#' + kradVariables.KUALI_FORM).attr('target', kradVariables.PORTAL_IFRAME_ID);
    } else if (parent != null) {
        top.jQuery('#' + kradVariables.KUALI_FORM).attr('target', parent.name);
    } else {
        top.jQuery('#' + kradVariables.KUALI_FORM).attr('target', '_parent');
    }
}

/**
 * Opens the inquiry window
 * Is called from the onclick event on the direct inquiry
 * The parameters is added by dynamically getting the values
 * for the fields in the parameter maps and then added to the url string
 *
 * @param url -
 *          the base url to use to call the inquiry
 * @param paramMap -
 *          array of field parameters for the inquiry
 * @param showLightBox -
 *          flag to indicate if it must be shown in a lightbox
 * @param lightBoxOptions -
 *          map of option settings (option name/value pairs) for the lightbox plugin
 */
function showDirectInquiry(url, paramMap, showLightBox, lightBoxOptions) {
    var parameterPairs = paramMap.split(",");
    var queryString = "";

    for (i in parameterPairs) {
        var parameters = parameterPairs[i].split(":");
        var value = checkDirectInquiryValueValid(jQuery('[name="' + escapeName(parameters[0]) + '"]').val());
        if (!value) {
            alert(getMessage(kradVariables.MESSAGE_PLEASE_ENTER_VALUE));
            return false;
        } else {
            queryString = queryString + "&" + parameters[1] + "=" + value;
        }
    }

    if (showLightBox) {
        // Check if this is called within a light box
        if (!getContext().find('.fancybox-inner', parent.document).length) {
            // Perform cleanup when lightbox is closed
            lightBoxOptions['beforeClose'] = cleanupClosedLightboxForms;

            queryString = queryString + "&flow=start&renderedInLightBox=true";
            lightBoxOptions['href'] = url + queryString;
            getContext().fancybox(lightBoxOptions);
        } else {
            // If this is already in a lightbox just open in current lightbox
            queryString = queryString + "&flow=" + jQuery("#flowKey").val() + "&renderedInLightBox=true";
            window.open(url + queryString, "_self");
        }
    } else {
        queryString = queryString;
        window.open(url + queryString, "_blank", "width=640, height=600, scrollbars=yes");
    }
}

/**
 * Removes wildcards and check for empty values
 *
 * @param value - value without wildcards or false if empty
 */
function checkDirectInquiryValueValid(value) {
    value = value.replace(/\*/g, '');
    if (value == "") {
        return false;
    }
    return value;
}

/**
 * Cleanup form data from server when lightbox window is closed
 */
function cleanupClosedLightboxForms() {
    if (jQuery('#formKey').length) {
        // get the formKey of the lightbox (fancybox)
        var context = getContext();
        var formKey = context('iframe.fancybox-iframe').contents().find('input#formKey').val();

        clearServerSideForm(formKey);
    }
}

/**
 * Uses jQuery DatePicker to render a calendar that can be used to select date
 * values for the field with the given control id. The second argument is a Map
 * of options that are available for the DatePicker. See
 * <link>http://jqueryui.com/demos/datepicker/#option-showOptions</link> for
 * documentation on these options
 *
 * @param controlId -
 *          id for the control that the date picker should populate
 * @param options -
 *          map of option settings (option name/value pairs) for the plugin
 */
function createDatePicker(controlId, options) {
    var fieldId = jQuery("#" + controlId).closest("div[data-role='InputField']").attr("id");
    jQuery(function () {
        var datePickerControl = jQuery("#" + controlId);
        datePickerControl.datepicker(options);
        datePickerControl.datepicker('option', 'onClose',
                function () {
                    getValidationData(jQuery("#" + fieldId)).messagingEnabled = true;
                    jQuery(this).trigger("focusout");
                    jQuery(this).trigger("focus");
                });
        datePickerControl.datepicker('option', 'beforeShow',
                function () {
                    getValidationData(jQuery("#" + fieldId)).messagingEnabled = false;
                });

        //KULRICE-7310 can't change only month or year with picker (jquery limitation)
        datePickerControl.datepicker('option', 'onChangeMonthYear',
                function (y, m, i) {
                    var d = i.selectedDay;
                    jQuery(this).datepicker('setDate', new Date(y, m - 1, d));
                });

        //KULRICE-7261 fix date format passed back.  jquery expecting mm-dd-yy
        if (options.dateFormat == "mm-dd-yy" && datePickerControl[0].getAttribute("value").indexOf("/") != -1) {
            datePickerControl.datepicker('setDate', new Date(datePickerControl[0].getAttribute("value")));
        }
    });

    // in order to compensate for jQuery's "Today" functionality (which does not actually return the date to the input box), alter the functionality
    jQuery.datepicker._gotoToday = function (id) {
        var target = jQuery(id);
        var inst = this._getInst(target[0]);
        if (this._get(inst, 'gotoCurrent') && inst.currentDay) {
            inst.selectedDay = inst.currentDay;
            inst.drawMonth = inst.selectedMonth = inst.currentMonth;
            inst.drawYear = inst.selectedYear = inst.currentYear;
        }
        else {
            var date = new Date();
            inst.selectedDay = date.getDate();
            inst.drawMonth = inst.selectedMonth = date.getMonth();
            inst.drawYear = inst.selectedYear = date.getFullYear();
        }
        this._notifyChange(inst);
        this._adjustDate(target);

        // The following two lines are additions to the original jQuery code
        this._setDateDatepicker(target, new Date());
        this._selectDate(id, this._getDateDatepicker(target));
    }
}

/**
 * Sets up the script necessary to toggle a group as a disclosure
 *
 * @param groupId -
 *          id for the group to be toggled
 * @param headerId -
 *          id for the group's header in which the toggle link and image will be
 *          inserted
 * @param widgetId - id for the accordion widget, used for updating state
 * @param defaultOpen -
 *          indicates whether the group should be initially open or close
 * @param collapseImgSrc -
 *          path to the image that should be displayed for collapsing the group
 * @param expandImgSrc -
 *          path to the image that should be displayed for expanding the group
 * @param animationSpeed -
 *          speed at which the group should be expanded or collapsed
 * @param renderImage -
 *          boolean that indicates whether the expanded or collapsed image should be rendered
 * @param ajaxRetrieval -
  *          boolean that indicates whether the disclosure group should be retrieved when open
 */
function createDisclosure(groupId, headerId, widgetId, defaultOpen, collapseImgSrc, expandImgSrc, animationSpeed,
                          renderImage, ajaxRetrieval) {
    jQuery(document).ready(function () {
        var groupToggleLinkId = groupId + kradVariables.ID_SUFFIX.DISCLOSURE_TOGGLE;

        var expandImage = "";
        var collapseImage = "";
        if (renderImage && defaultOpen) {
            expandImage = "<img id='" + groupToggleLinkId + "_exp" + "' src='" + expandImgSrc + "' alt='" + getMessage(kradVariables.MESSAGE_EXPAND) + "' class='uif-disclosure-image'/>";
            collapseImage = "<img style='display:none;' id='" + groupToggleLinkId + "_col" + "' src='" + collapseImgSrc + "' alt='" + getMessage(kradVariables.MESSAGE_COLLAPSE) + "' class='uif-disclosure-image'/>";
        }
        else if (renderImage && !defaultOpen) {
            expandImage = "<img style='display:none;' id='" + groupToggleLinkId + "_exp" + "' src='" + expandImgSrc + "' alt='" + getMessage(kradVariables.MESSAGE_EXPAND) + "' class='uif-disclosure-image'/>";
            collapseImage = "<img id='" + groupToggleLinkId + "_col" + "' src='" + collapseImgSrc + "' alt='" + getMessage(kradVariables.MESSAGE_COLLAPSE) + "' class='uif-disclosure-image'/>";
        }

        var content = jQuery("#" + groupId + kradVariables.ID_SUFFIX.DISCLOSURE_CONTENT);

        // perform initial open/close and insert toggle link and image
        var headerText = jQuery("#" + headerId).find(".uif-headerText-span:first");
        if (defaultOpen) {
            content.show();

            content.attr(kradVariables.ATTRIBUTES.DATA_OPEN, true);

            headerText.prepend(expandImage);
            headerText.prepend(collapseImage);
        }
        else {
            content.hide();

            content.attr(kradVariables.ATTRIBUTES.DATA_OPEN, false);

            headerText.prepend(collapseImage);
            headerText.prepend(expandImage);
        }

        headerText.wrap("<a data-role=" + kradVariables.DATA_ROLES.DISCLOSURE_LINK + " data-linkfor='"
                + content.attr("id") + "' href='#' "
                + "id='" + groupToggleLinkId + "' "
                + "data-open='" + defaultOpen + "' "
                + "data-widgetid='" + widgetId + "' "
                + "data-speed='" + animationSpeed + "' "
                + "data-ajax='" + ajaxRetrieval + "'"
                + "></a>");
    });
}

/**
 * Expands all the disclosure divs on the page
 */
function expandDisclosures() {
    jQuery("a[data-role='" + kradVariables.DATA_ROLES.DISCLOSURE_LINK + "']").each(function () {
        var contentId = jQuery(this).attr("data-linkfor");
        if (jQuery("#" + contentId).attr(kradVariables.ATTRIBUTES.DATA_OPEN) == "false") {
            jQuery(this).click();
        }
    });
}

/**
 * Collapses all the disclosure divs on the page
 */
function collapseDisclosures() {
    jQuery("a[data-role='" + kradVariables.DATA_ROLES.DISCLOSURE_LINK + "']").each(function () {
        var contentId = jQuery(this).attr("data-linkfor");
        if (jQuery("#" + contentId).attr(kradVariables.ATTRIBUTES.DATA_OPEN) == "true") {
            jQuery(this).click();
        }
    });
}

/**
 * Uses jQuery DataTable plug-in to decorate a table with functionality like
 * sorting and page. The second argument is a Map of options that are available
 * for the plug-in. See <a href=http://www.datatables.net/usage/>datatables</a> for
 * documentation on these options
 *
 * @param tableId id for the table that should be decorated
 * @param additionalOptions map of additional or override option settings (option name/value pairs) for the plugin
 * @param groupingOptions (optional) if supplied, the collection will use rowGrouping with these options
 */
function createTable(tableId, additionalOptions, groupingOptions) {
    jQuery(document).ready(function () {
        var table = jQuery("#" + tableId);

        var detailsOpen = table.parent().data("detailsdefaultopen");
        table.data("open", detailsOpen);

        if (groupingOptions) {
            table.attr("data-groups", "true");
        }

        var options = {
            "bDestory": true,
            "bStateSave": true,
            "fnStateSave": function (oSettings, oData) {
                setComponentState(tableId, 'richTableState', oData);
            },
            "fnStateLoad": function (oSettings) {
                var oData = getComponentState(tableId, 'richTableState');

                return oData;
            }
        }

        options = jQuery.extend(options, additionalOptions);

        var exportOptions = {
            "sDownloadSource": additionalOptions.sDownloadSource,
            "oTableTools": {
                "aButtons": [
                    {
                        "sExtends": "text",
                        "sButtonText": "csv",
                        "fnClick": function (nButton, oConfig) {
                            window.location.href = additionalOptions.sDownloadSource + "&methodToCall=tableCsvRetrieval&formatType=csv";
                        },
                    },
                    {
                        "sExtends": "text",
                        "sButtonText": "xml",
                        "fnClick": function (nButton, oConfig) {
                            window.location.href = additionalOptions.sDownloadSource + "&methodToCall=tableXmlRetrieval&formatType=xml";
                        },
                    },
                    {
                        "sExtends": "text",
                        "sButtonText": "xls",
                        "fnClick": function (nButton, oConfig) {
                            window.location.href = additionalOptions.sDownloadSource + "&methodToCall=tableXlsRetrieval&formatType=xls";
                        },
                    }
                ]
            }
        }

        /// check that the export feature is turned on
        if (options.sDom && options.sDom.search("T") >= 0) {
            options = jQuery.extend(options, exportOptions)
        }

        var oTable = table.dataTable(options);

        //make sure scripts are run after table renders (must be done here for deferred rendering)
        runHiddenScripts(tableId, false, true);
        initBubblePopups();

        //insure scripts (if any) are run on each draw, fixes bug with scripts lost when paging after a refresh
        jQuery(oTable).on("dataTables.tableDraw", function () {
            runHiddenScripts(tableId, false, true);
            jQuery("div[data-role='InputField'][data-has_messages='true']", "#" + tableId).each(function () {
                var id = jQuery(this).attr('id');
                var validationData = getValidationData(jQuery("#" + id));

                if (validationData && validationData.hasOwnMessages) {
                    handleMessagesAtField(id);
                }
            });
        });

        //handle row details related functionality setup
        if (detailsOpen != undefined) {
            jQuery(oTable).on("dataTables.tableDraw", function () {
                if (table.data("open")) {
                    openAllDetails(tableId);
                }
                else {
                    closeAllDetails(tableId);
                }
            });

            if (detailsOpen) {
                openAllDetails(tableId, false);
            }
        }

        // allow table column size recalculation on window resize
        jQuery(window).bind('resize', function () {
            // passing false to avoid copious ajax requests during window resize
            oTable.fnAdjustColumnSizing(false);
        });

        if (groupingOptions) {
            oTable.rowGrouping(groupingOptions);
        }
    });
}

/**
 * Expands a data table row by finding the row that matches the actionComponent passed in, in the
 * dataTable which matches tableId.  If useImages is true, images will be swapped out when the
 * action is clicked.  The row will be closed if already open.
 *
 * @param actionComponent the actionComponent clicked
 * @param tableId the dataTable to expand the clicked row in
 * @param useImages if true, swap open/close images on click
 */
function rowDetailsActionHandler(actionComponent, tableId) {
    var oTable = getDataTableHandle(tableId);

    if (oTable != null) {
        var row = jQuery(actionComponent).parents('tr')[0];
        if (oTable.fnIsOpen(row)) {
            closeDetails(oTable, jQuery(row), actionComponent, true);
        }
        else {
            openDetails(oTable, jQuery(row), actionComponent, true);
        }
        jQuery(row).data("det-interact", true);
    }
}

/**
 * Open all row details in the table specified by id
 *
 * @param tableId id of the table to open all details for
 * @param animate [optional] if true, animate during opening the rows
 * @param forceOpen [optional] if true, force the each row details to open and reset interaction flag, otherwise if the
 * row has been interacted with skip (used to retain state)
 */
function openAllDetails(tableId, animate, forceOpen) {
    var oTable = getDataTableHandle(tableId);

    if (oTable != null) {
        var rows = jQuery(oTable).find('tr').not(".detailsRow");
        rows.each(function () {
            var row = jQuery(this);
            //Means the row is not open and the user has not interacted with it (or force if forceOpen is true)
            //This is done to retain row details "state" between table draws for rows the user may have interacted with
            if (!oTable.fnIsOpen(this) && (!row.data("det-interact") || forceOpen)) {
                var actionComponent = row.find("a[data-role='detailsLink']");

                openDetails(oTable, row, actionComponent, animate);

                //reset user interaction flag
                row.data("det-interact", false);
            }
        });
    }
}

/**
 * Open the row details. If the ajaxRetrieval option is set this will retrieve the detail content.
 *
 * @param oTable the dataTable object handle
 * @param row the row to open details for
 * @param actionComponent [optional] actionComponent used to invoke the action, required if using image swap
 * or ajaxRetrieval
 * @param animate if true, the open will have an animation effect
 */
function openDetails(oTable, row, actionComponent, animate) {
    var detailsGroup = row.find("div[data-role='details'], span[data-role='placeholder']").filter(":first");
    var ajaxRetrieval = jQuery(detailsGroup).is("span[data-role='placeholder']");
    var detailsId = jQuery(detailsGroup).attr("id");

    if (actionComponent && jQuery(actionComponent).data("swap") && jQuery(actionComponent).find("img").length) {
        jQuery(actionComponent).find("img").replaceWith(detailsCloseImage.clone());
    }

    var newRow = oTable.fnOpenCustom(row[0], detailsGroup, "uif-rowDetails");
    detailsGroup = jQuery(newRow).find("div[data-role='details'], span[data-role='placeholder']").filter(":first");

    detailsGroup.attr("data-open", "true");

    //make sure scripts are run on the now shown group
    runHiddenScripts(detailsGroup, true, true);

    //show the group
    detailsGroup.show();

    if (ajaxRetrieval) {
        var kradRequest = new KradRequest(jQuery(actionComponent));

        if (!kradRequest.methodToCall) {
            kradRequest.methodToCall = kradVariables.REFRESH_METHOD_TO_CALL;
        }

        kradRequest.ajaxReturnType = kradVariables.RETURN_TYPE_UPDATE_COMPONENT;
        kradRequest.refreshId = detailsId;

        kradRequest.send();
    }
}

/**
 * Close all row details in the table specified by id
 *
 * @param tableId id of the table to close all details for
 * @param animate [optional] if true, animate during closing the rows
 * @param forceOpen [optional] if true, force the each row details to close and reset interaction flag, otherwise if the
 * row has been interacted with skip (used to retain state)
 */
function closeAllDetails(tableId, animate, forceClose) {
    var oTable = getDataTableHandle(tableId);

    if (oTable != null) {
        var rows = jQuery(oTable).find('tr').not(".detailsRow");
        rows.each(function () {
            var row = jQuery(this);
            //Means the row is open and the user has not interacted with it (or force if forceClose is true)
            //This is done to retain row details "state" between table draws for rows the user may have interacted with
            if (oTable.fnIsOpen(this) && (!row.data("det-interact") || forceClose)) {
                var actionComponent = row.find("a[data-role='detailsLink']");

                closeDetails(oTable, row, actionComponent, animate);

                //reset user interaction flag
                row.data("det-interact", false);
            }
        });
    }
}

/**
 * Close the row details.
 *
 * @param oTable the dataTable object handle
 * @param row the row to close details for
 * @param actionComponent [optional] actionComponent used to invoke the action, required if using image swap
 * @param animate if true, the close will have an animation effect
 */
function closeDetails(oTable, row, actionComponent, animate) {
    var fieldGroupWrapper = row.find("> td > div[data-role='detailsFieldGroup']");
    var detailsContent = row.next().first().find("> td > div[data-role='details'], "
            + "> td > span[data-role='placeholder']").filter(":first");

    if (actionComponent && jQuery(actionComponent).data("swap") && jQuery(actionComponent).find("img").length) {
        jQuery(actionComponent).find("img").replaceWith(detailsOpenImage.clone());
    }

    detailsContent.attr("data-open", "false");

    detailsContent.hide();
    fieldGroupWrapper.append(detailsContent.detach());
    oTable.fnClose(row[0]);

}

/**
 * Open or close all rows for the table specified by the actionComponent's "tableid" data attribute
 *
 * @param actionComponent the calling action component
 */
function toggleRowDetails(actionComponent) {
    var action = jQuery(actionComponent);
    var tableId = action.data("tableid");
    var open = action.data("open");
    if (open) {
        closeAllDetails(tableId, true, true);
        action.data("open", false);
        jQuery("#" + tableId).data("open", false);
    }
    else {
        openAllDetails(tableId, true, true);
        action.data("open", true);
        jQuery("#" + tableId).data("open", true);
    }
}

/**
 * Select all checkboxes within the collection div that are marked with class 'kr-select-line' (used
 * for multi-value select collections)
 *
 * @param collectionId - id for the collection to select checkboxes for
 */
function selectAllLines(collectionId) {
    jQuery("#" + collectionId + " input:checkbox.kr-select-line").attr('checked', true);
    setMultivalueLookupReturnButton(jQuery("#" + collectionId + " input:checkbox.kr-select-line"));

}

/**
 * Deselects all checkboxes within the collection div that are marked with class 'kr-select-line' (used
 * for multi-value select collections)
 *
 * @param collectionId - id for the collection to deselect checkboxes for
 */
function deselectAllLines(collectionId) {
    jQuery("#" + collectionId + " input:checkbox.kr-select-line").attr('checked', false);
    setMultivalueLookupReturnButton(jQuery("#" + collectionId + " input:checkbox.kr-select-line"));
}

/**
 * Select all checkboxes within the datatable (all pages) that are marked with class 'kr-select-line' (used
 * for multi-value select collections)
 *
 * @param collectionId - id for the collection to select checkboxes for
 */
function selectAllPagesLines(collectionId) {
    // get a handle on the datatables plugin object for the results collection
    var oTable = getDataTableHandle(jQuery("#" + collectionId).find("table").attr('id'));
    jQuery('input:checkbox.kr-select-line', oTable.fnGetNodes()).prop('checked', true);
    setMultivalueLookupReturnButton(jQuery("#" + collectionId + " input:checkbox.kr-select-line"));
}

/**
 * Deselects all checkboxes within the datatable (all pages) that are marked with class 'kr-select-line' (used
 * for multi-value select collections)
 *
 * @param collectionId - id for the collection to deselect checkboxes for
 */
function deselectAllPagesLines(collectionId) {
    // get a handle on the datatables plugin object for the results collection
    var oTable = getDataTableHandle(jQuery("#" + collectionId).find("table").attr('id'));
    jQuery('input:checkbox.kr-select-line', oTable.fnGetNodes()).prop('checked', false);
    setMultivalueLookupReturnButton(jQuery("#" + collectionId + " input:checkbox.kr-select-line"));
}

/**
 * Uses jQuery jsTree plug-in to decorate a div with tree functionality. The
 * second argument is a Map of options that are available
 * for the plug-in. See <link>http://www.jstree.com/documentation/</link> for
 * documentation on these options
 *
 * @param divId -
 *          id for the div that should be decorated
 * @param options -
 *          map of option settings (option name/value pairs) for the plugin
 */
function createTree(divId, options) {
    jQuery(document).ready(function () {
        jQuery("#" + divId).jstree(options);
    });
}

/**
 * Adds a ZeroClipboard flash movie to the copy trigger element which will copy the content of the content element
 * on mousedown. This uses the ZeroClipboard plugin bundled with the Datatables plugin
 *
 * @param componentId - id of the parent component
 * @param copyTriggerId - id of the element that must trigger the copy action
 * @param contentElementId - id of the element that the value must be copied from
 * @param showCopyConfirmation [optional] if supplied and true, a dialog will be triggered displaying the copied value
 * after copy action
 */
function createCopyToClipboard(componentId, copyTriggerId, contentElementId, showCopyConfirmation) {

    // the ZeroClipboard flash movie can only be added to visible elements so must be added on document ready
    jQuery(document).ready(function () {

        // Do not add flash to hidden syntax highlighters as this causes exception
        if (jQuery("#" + componentId).is(':visible')) {

            // setup new client for this component
            ZeroClipboard.setMoviePath(getConfigParam(kradVariables.APPLICATION_URL)
                    + '/plugins/datatables/copy_cvs_xls_pdf.swf');
            var clip = new ZeroClipboard.Client();

            // copy text on mousedown
            clip.addEventListener('mousedown', function (client) {
                clip.setText(jQuery("#" + contentElementId).text());
            });

            // show dialog
            if (showCopyConfirmation) {
                clip.addEventListener('complete', function (client, text) {
                    alert('Copied to Clipboard :\n\n' + text);
                });
            }

            // the element needs to be visible when adding the flah movie to it
            // just reset the display css on the element after showing it
            jQuery('#' + copyTriggerId).show();
            clip.glue(jQuery('#' + copyTriggerId).get(0));
            jQuery('#' + copyTriggerId).css("display", "");
        }

    });
}

function createAccordion(id, options, active) {
    if (active == false) {
        active = "false";
    }

    options = options || {};
    options = jQuery.extend({
        active: active,
        heightStyle: "content",
        collapsible: true
    }, options);

    jQuery("#" + id + " > ul").accordion(options);
    //jQuery("#id > ul").accordion("option", "active", active);
}

/**
 * Create a tab group for the div given by the element id using the jQuery UI tab plugin
 * (http://api.jqueryui.com/tabs/)
 *
 * @param id id of the element the tabs should be created for
 * @param widgetId id for the tabs widget
 * @param options object of options for tabs plugin (see plugin documentation for valid options)
 * @param position position of tabs related to group content, options are TOP, BOTTOM, RIGHT, or LEFT
 */
function createTabs(id, widgetId, options, position) {
    var tabs = jQuery("#" + id + "_tabs").tabs(options);

    // when active tab changes we need to update the client side state
    tabs.on("tabsactivate", function (event, ui) {
        var activeTabId = ui.newPanel.attr('id');
        activeTabId = activeTabId.replace(/_tab$/, "");

        setComponentState(widgetId, 'activeTab', activeTabId);
    });

    if (position == "BOTTOM") {
        tabs.addClass("ui-tabs-bottom");
        jQuery(".ui-tabs-bottom .ui-tabs-nav, .ui-tabs-bottom .ui-tabs-nav > *")
                .removeClass("ui-corner-all ui-corner-top")
                .addClass("ui-corner-bottom");
        jQuery(".ui-tabs-bottom .ui-tabs-nav").appendTo(".ui-tabs-bottom");
    }
    else if (position == "RIGHT") {
        tabs.addClass('ui-tabs-vertical ui-tabs-vertical-right ui-helper-clearfix');
        tabs.find("li").removeClass('ui-corner-top').addClass('ui-corner-right');
    }
    else if (position == "LEFT") {
        tabs.addClass("ui-tabs-vertical ui-tabs-vertical-left ui-helper-clearfix");
        tabs.find("li").removeClass("ui-corner-top").addClass("ui-corner-left");
    }
}

/**
 * Uses jQuery UI Auto-complete widget to provide suggest options for the given field. See
 * <link>http://jqueryui.com/demos/autocomplete/</link> for documentation on this widget
 *
 * @param controlId id for the html control the autocomplete will be enabled for
 * @param options map of option settings (option name/value pairs) for the widget
 * @param queryFieldId id for the attribute field the control belongs to, used when making the
 * request to execute the associated attribute query
 * @param queryParameters map of parameters that should be sent along with the query. map key gives
 * @param localSource indicates whether the suggest options will be provided locally instead of by
 * a query
 * @param suggestOptions when localSource is set to true provides the suggest options
 * the name of the parameter to send, and the value gives the name of the field to pull the value from
 * @param labelProp the property name that holds the label for the plugin
 * @param valueProp the property name that holds the value for the plugin
 * @param returnCustomObj if true, the full object is expected as return value
 */
function createSuggest(controlId, options, queryFieldId, queryParameters, localSource, suggestOptions, labelProp, valueProp, returnCustomObj) {
    if (localSource) {
        options.source = suggestOptions;
    }
    else {

        options.source = function (request, response) {
            var successFunction = function (data) {
                response(data.resultData);
            };

            //special success logic for the object return case with label/value props specified
            if (returnCustomObj && (labelProp || valueProp)) {
                successFunction = function (data) {
                    var isObject = false;

                    if (data.resultData && data.resultData.length && data.resultData[0]) {
                        isObject = (data.resultData[0].constructor === Object);
                    }

                    if (data.resultData && data.resultData.length && isObject) {
                        //find and match props, and set them into each object so the autocomplete plugin can read them
                        jQuery.each(data.resultData, function (index, object) {
                            if (labelProp && object[labelProp]) {
                                object.label = object[labelProp];
                            }

                            if (valueProp && object[valueProp]) {
                                object.value = object[valueProp];
                            }
                        });
                        response(data.resultData);
                    }
                    else {
                        response(data.resultData);
                    }

                };
            }

            var queryData = {};

            queryData.methodToCall = 'performFieldSuggest';
            queryData.ajaxRequest = true;
            queryData.ajaxReturnType = 'update-none';
            queryData.formKey = jQuery("input#formKey").val();
            queryData.queryTerm = request.term;
            queryData.queryFieldId = queryFieldId;

            for (var parameter in queryParameters) {
                queryData['queryParameter.' + parameter] = coerceValue(queryParameters[parameter]);
            }

            jQuery.ajax({
                url: jQuery("form#kualiForm").attr("action"),
                dataType: "json",
                beforeSend: null,
                complete: null,
                error: null,
                data: queryData,
                success: successFunction
            });
        };
    }

    jQuery(document).ready(function () {
        jQuery("#" + controlId).autocomplete(options);
    });
}

/**
 * Create a locationSuggest which overrides the select method with one that will navigate the user based on url
 * settings
 *
 * @param baseUrl baseUrl of the urls being built
 * @param hrefProperty href url property name - if found use this always (do not build url)
 * @param addUrlProperty additional url appendage property name for built urls
 * @param requestParamNamesObj obj containing key/propertyName pairs for requestParameters on a built url
 * @param requestParameterString static request parameter string to append
 * @param controlId id for the html control the autocomplete will be enabled for
 * @param options map of option settings (option name/value pairs) for the widget
 * @param queryFieldId id for the attribute field the control belongs to, used when making the
 * request to execute the associated attribute query
 * @param queryParameters map of parameters that should be sent along with the query. map key gives
 * @param localSource indicates whether the suggest options will be provided locally instead of by
 * a query
 * @param suggestOptions when localSource is set to true provides the suggest options
 * the name of the parameter to send, and the value gives the name of the field to pull the value from
 * @param labelProp the property name that holds the label for the plugin
 * @param valueProp the property name that holds the value for the plugin
 * @param returnCustomObj if true, the full object is expected as return value
 */
function createLocationSuggest(baseUrl, hrefProperty, addUrlProperty, requestParamNamesObj, requestParameterString, controlId, options, queryFieldId, queryParameters, localSource, suggestOptions, labelProp, valueProp, returnCustomObj) {

    var originalFunction = undefined;
    if (options.select != undefined) {
        originalFunction = options.select;
    }

    options.select = function (event, object) {
        var originalFunctionResult = true;
        if (originalFunction) {
            originalFunctionResult = originalFunction();
        }

        if (object && object.item && hrefProperty && object.item[hrefProperty]) {
            window.location.href = object.item[hrefProperty];
        }
        else if (object && object.item && baseUrl) {
            var builtUrl = baseUrl;

            if (addUrlProperty && object.item[addUrlProperty]) {
                builtUrl = builtUrl + object.item[addUrlProperty];
            }

            var addParams = "";
            if (requestParamNamesObj) {
                jQuery.each(requestParamNamesObj, function (key, propName) {
                    if (object.item[propName]) {
                        addParams = addParams + "&" + key + "=" + object.item[propName];
                    }
                });
            }

            if (requestParameterString) {
                builtUrl = builtUrl + requestParameterString + addParams;
            }
            else if (addParams) {
                builtUrl = builtUrl + "?" + addParams.substr(1, addParams.length);
            }

            window.location.href = builtUrl;
        }

        return originalFunctionResult;
    };

    createSuggest(controlId, options, queryFieldId, queryParameters, localSource, suggestOptions,
            labelProp, valueProp, returnCustomObj);
}

/**
 * Creates the spinner widget for an input
 *
 * @param id - id for the control to apply the spinner to
 * @param options - options for the spinner
 */
function createSpinner(id, options) {
    jQuery("#" + id).spinner(options);
}

/**
 * Creates the tooltip widget for an component
 *
 * @param id - id for the component to apply the tooltip to
 * @param options - options for the tooltip
 */
function createTooltip(id, text, options, onMouseHoverFlag, onFocusFlag) {
    var elementInfo = getHoverElement(id);
    var element = elementInfo.element;

    options['innerHtml'] = text;
    options['manageMouseEvents'] = false;
    if (onFocusFlag) {
        // Add onfocus trigger
        jQuery("#" + id).focus(function () {
//            if (!jQuery("#" + id).IsBubblePopupOpen()) {
            // TODO : use data attribute to check if control
            if (!isControlWithMessages(id)) {
                jQuery("#" + id).SetBubblePopupOptions(options, true);
                jQuery("#" + id).SetBubblePopupInnerHtml(options.innerHTML, true);
                jQuery("#" + id).ShowBubblePopup();
            }
//            }
        });
        jQuery("#" + id).blur(function () {
            jQuery("#" + id).HideBubblePopup();
        });
    }
    if (onMouseHoverFlag) {
        // Add mouse hover trigger
        jQuery("#" + id).hover(function () {
            if (!jQuery("#" + id).IsBubblePopupOpen()) {
                if (!isControlWithMessages(id)) {
                    jQuery("#" + id).SetBubblePopupOptions(options, true);
                    jQuery("#" + id).SetBubblePopupInnerHtml(options.innerHTML, true);
                    jQuery("#" + id).ShowBubblePopup();
                }
            }
        }, function (event) {
            if (!onFocusFlag || !jQuery("#" + id).is(":focus")) {
                var result = mouseInTooltipCheck(event, id, element, this, elementInfo.type);
                if (result) {
                    mouseLeaveHideTooltip(id, jQuery("#" + id), element, elementInfo.type);
                }
            }
        });
    }
}

/**
 * Checks if the component is a control or contains a control that contains validation messages
 *
 * @param id the id of the field
 */
function isControlWithMessages(id) {
    // check if component is or contains a control
    if (jQuery("#" + id).is("[data-role='Control']")
            || (jQuery("#" + id).is("[data-role='InputField']") && jQuery("#" + id + "_control").is("[data-role='Control']"))) {
        return hasMessage(id)
    }
    return false;
}

/**
 * Checks if a field has any messages
 *
 * @param id
 */
function hasMessage(id) {
    var fieldId = getAttributeId(id);
    var messageData = getValidationData(jQuery("#" + fieldId));
    if (messageData && (messageData.serverErrors.length || (messageData.errors && messageData.errors.length)
            || messageData.serverWarnings.length || (messageData.warnings && messageData.warnings.length)
            || messageData.serverInfo.length || (messageData.info && messageData.info.length))) {
        return true;
    }
    return false;
}

/**
 * Workaround to prevent hiding the tooltip when the mouse actually may still be hovering over the field
 * correctly, checks to see if the mouseleave event was entering the tooltip and if so dont continue the
 * hide action, rather add a mouseleave handler that will only be invoked once for that segment, when this
 * is left the check occurs again, until the user has either left the tooltip or the field - then the tooltip
 * is hidden appropriately
 * @param event - mouseleave event
 * @param fieldId - id of the field this logic is being applied to
 * @param triggerElements - the elements that can trigger mouseover
 * @param callingElement - original element that invoked the mouseleave
 * @param type - type of the field
 */
function mouseInTooltipCheck(event, fieldId, triggerElements, callingElement, type) {
    if (event.relatedTarget &&
            jQuery(event.relatedTarget).length &&
            jQuery(event.relatedTarget).attr("class") != null &&
            jQuery(event.relatedTarget).attr("class").indexOf("jquerybubblepopup") >= 0) {
        //this bind is only every invoked once, then unbound - return false to stop hide
        jQuery(event.relatedTarget).one("mouseleave", function (event) {
            mouseInTooltipCheck(event, fieldId, triggerElements, callingElement, type);
        });
        return false;
    }
    //If target moving into is not a triggerElement for this hover
    // and if the source of the event is not a trigger element
    else if (!jQuery(event.relatedTarget).is(triggerElements) && !jQuery(event.target).is(triggerElements)) {
        //hide the tooltip for the original element
        mouseLeaveHideTooltip(fieldId, callingElement, triggerElements, type, true);
        return true;
    }
    else {
        return true;
    }
}

/**
 * Method to hide the tooltip when the mouse leave event was successful for the field
 * @param id id of the field
 * @param currentElement the current element be iterated on
 * @param elements all elements within the hover set
 * @param type type of field
 */
function mouseLeaveHideTooltip(id, currentElement, elements, type, force) {
    var hide = true;
    var tooltipElement = jQuery(currentElement);

    if (type == "fieldset") {
        //hide only if mouseleave is on fieldset not its internal radios/checkboxes
        hide = force || jQuery(currentElement).is("fieldset");
        tooltipElement = elements.filter("label:first");
    }

    //hide only if hide flag is true and the tooltip is open
    if (hide && jQuery(tooltipElement).IsBubblePopupOpen()) {
        hideTooltip(id);
    }
}

/**
 * Hide the tooltip associated with the field by id
 * @param fieldId the id of the field
 */
function hideTooltip(fieldId) {
    var elementInfo = getTooltipElement(fieldId);
    var element = elementInfo.element;
    if (elementInfo.type == "fieldset") {
        //for checkbox/radio fieldsets we put the tooltip on the label of the first input
        element = jQuery(element).filter("label:first");
    }
    var data = getValidationData(jQuery("#" + fieldId));
    if (data && data.showTimer) {
        clearTimeout(data.showTimer);
    }
    var tooltipId = jQuery(element).GetBubblePopupID();
    if (tooltipId) {
        //this causes the tooltip to be IMMEDIATELY hidden, rather than wait for animation
        jQuery("#" + tooltipId).css("opacity", 0);
        jQuery("#" + tooltipId).hide();
    }
    jQuery(element).HideBubblePopup();

}

/**
 * Gets the hover elements for a field by id.  The hover elements are the elements which will cause the tooltip to
 * be shown, the element the tooltip is actually placed on is an item its hover elements.
 * @param fieldId the id of the field
 */
function getTooltipElement(fieldId) {
    var hasFieldset = jQuery("#" + fieldId).find("fieldset").length;
    var elementInfo = {};

    if (!hasFieldset) {
        //regular case
        elementInfo.element = jQuery("#" + fieldId);
        elementInfo.type = "";
        if (elementInfo.element.is("input:checkbox")) {
            elementInfo.themeMargins = {
                total: '13px',
                difference: '0px'
            };
        }
    }
    else if (hasFieldset && jQuery("#" + fieldId).find("fieldset > span > input").length) {
        //radio and checkbox fieldset case
        //get the fieldset, the inputs its associated with, and the associated labels as hover elements
        elementInfo.element = jQuery("#" + fieldId).find("fieldset, fieldset input, fieldset label");
        elementInfo.type = "fieldset";
        elementInfo.themeMargins = {
            total: '13px',
            difference: '2px'
        };
    }
    else {
        //not found or wrapping fieldset case
        elementInfo.element = [];
        elementInfo.type = "";
    }
    return elementInfo;
}

/**
 * Executes a query with ajax for the given field to retrieve additional information after
 * the field has been updated (on blur)
 *
 * @param controlId -
 *           id for the html control to pull current value from
 * @param queryFieldId -
 *          id for the attribute field the control belongs to, used when making the
 * request to execute the associated field query
 * @param queryParameters -
 *         map of parameters that should be sent along with the query. map key gives
 * the name of the parameter to send, and the value gives the name of the field to pull the value from
 * @param queryMethodArgs -
 *         list of parameters that should be sent along with the query, the list gives the
 * name of the field in the view to pull values from, and will be sent with the same name
 * as a query parameter on the request
 * @param returnFieldMapping -
 *        map of fields that should be returned (updated) from the query. map key gives
 * the name of the parameter to update, map value is the name of field to pull value from
 */
function executeFieldQuery(controlId, queryFieldId, queryParameters, queryMethodArgs, returnFieldMapping) {
    var queryData = {};

    queryData.methodToCall = 'performFieldQuery';
    queryData.ajaxRequest = true;
    queryData.ajaxReturnType = 'update-none';
    queryData.formKey = jQuery("input#formKey").val();
    queryData.queryFieldId = queryFieldId;

    for (var parameter in queryParameters) {
        queryData['queryParameter.' + queryParameters[parameter]] = coerceValue(parameter);
    }

    for (var parameter in queryMethodArgs) {
        queryData['queryParameter.' + queryMethodArgs[parameter]] = coerceValue(parameter);
    }

    jQuery.ajax({
        url: jQuery("form#kualiForm").attr("action"),
        dataType: "json",
        data: queryData,
        beforeSend: null,
        complete: null,
        error: null,
        success: function (data) {
            // write out return message (or blank)
            var returnMessageSpan = jQuery("#" + queryFieldId + "_info_message");
            if (returnMessageSpan.length > 0) {
                returnMessageSpan.html(data.resultMessage);
                if (data.resultMessageStyleClasses) {
                    returnMessageSpan.addClass(data.resultMessageStyleClasses);
                }
            }

            // write out informational field values, note if data does not exist
            // this will clear the field values
            for (var returnField in returnFieldMapping) {
                var fieldValue = data.resultFieldData[returnField];
                if (!fieldValue) {
                    fieldValue = "";
                }

                // check for regular fields
                var control = jQuery("[name='" + escapeName(returnField) + "']");
                if (control.length > 0) {
                    setValue(returnField, fieldValue);
                    control.change();
                }

                // check for info spans
                var returnFieldId = returnField.replace(/\./g, "_")
                        .replace(/\[/g, "-lbrak-")
                        .replace(/\]/g, "-rbrak-")
                        .replace(/\'/g, "-quot-");
                var infoFieldSpan = jQuery("#" + queryFieldId + "_info_" + returnFieldId);
                if (infoFieldSpan.length > 0) {
                    infoFieldSpan.html(fieldValue);
                }
            }
        }
    });
}
