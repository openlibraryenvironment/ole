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
 * Sets up a new request configured from the given action component and submits
 * the request
 *
 * @param component - dom element the event occurred on (the action)
 */
function actionInvokeHandler(component) {
    var action = jQuery(component);

    var kradRequest = new KradRequest(action);
    kradRequest.send();
}

/**
 * Convenience method for submitting the form via Ajax
 *
 * <p>
 * For full options using the KradRequest function directly
 * </p>
 *
 * @param methodToCall - the controller method to be called
 * @param additionalData - any additional data that needs to be passed to the server
 */
function ajaxSubmitForm(methodToCall, additionalData) {
    var kradRequest = new KradRequest();

    kradRequest.methodToCall = methodToCall;
    kradRequest.additionalData = additionalData;

    kradRequest.send();
}

/**
 * Convenience method for submitting the form via standard browser submit
 *
 * <p>
 * For full options using the KradRequest function directly
 * </p>
 *
 * @param methodToCall - the controller method to be called
 * @param additionalData - any additional data that needs to be passed to the server
 */
function nonAjaxSubmitForm(methodToCall, additionalData) {
    var kradRequest = new KradRequest();

    kradRequest.methodToCall = methodToCall;
    kradRequest.additionalData = additionalData;
    kradRequest.ajaxSubmit = false;

    kradRequest.send();
}

/**
 * Convenience method for submitting the form with option for ajax or non-ajax submit
 *
 * <p>
 * For full options using the KradRequest function directly
 * </p>
 *
 * @param methodToCall - the controller method to be called
 * @param additionalData - any additional data that needs to be passed to the server
 * @param validate - indicates whethere client side validation should be performed before the submit
 * @param ajaxSubmit - whether the submit should be via ajax or standard browser submit
 * @param successCallback - method to invoke after a successful request, only applies to ajax calls
 */
function submitForm(methodToCall, additionalData, validate, ajaxSubmit, successCallback) {
    var kradRequest = new KradRequest();

    kradRequest.methodToCall = methodToCall;
    kradRequest.additionalData = additionalData;
    kradRequest.validate = validate;
    kradRequest.ajaxSubmit = ajaxSubmit;
    kradRequest.successCallback = successCallback;

    kradRequest.send();
}

/**
 * Within a multi-page view changes the currently loaded page to the page identified
 * by the given id
 *
 * @param pageId id for the page to navigate to
 */
function navigateToPage(pageId) {
    ajaxSubmitForm(kradVariables.NAVIGATE_METHOD_TO_CALL, {"actionParameters[navigateToPageId]": pageId});
}

/**
 * Convenience method for redirecting to a URL
 *
 * @param url to redirect to
 */
function redirect(url) {
    window.location = url;
}

/**
 * Default handler for dialog responses
 *
 * <p>
 * Simply closes the dialog and makes server call to handle the response
 * </p>
 */
function submitDialogResponse() {
    closeLightbox();

    ajaxSubmitForm(kradVariables.RETURN_FROM_LIGHTBOX_METHOD_TO_CALL);
}

/**
 * Runs client side validation on the entire form and returns the result (an alert is also given
 * if errors are encountered)
 */
function validateForm() {
    clientErrorStorage = new Object();
    var summaryTextExistence = new Object();
    var validForm = true;

    jQuery.watermark.hideAll();
    pauseTooltipDisplay = true;

    if (validateClient) {
        // Turn on this flag to avoid prematurely writing out messages which will cause performance issues if MANY
        // fields have validation errors simultaneously (first we are only checking for errors, not checking and
        // writing simultaneously like normal)
        clientErrorExistsCheck = true;

        // Temporarily turn off this flag to avoid traversing unneeded logic (all messages will be shown at the end)
        messageSummariesShown = false;

        // Validate the whole form
        validForm = jq("#kualiForm").valid();

        // Handle field message bubbling manually, but do not write messages out yet
        jQuery("div[data-role='InputField']").each(function () {
            var id = jQuery(this).attr('id');
            var field = jQuery("#" + id);
            var data = getValidationData(field);
            var parent = field.data("parent");
            handleMessagesAtGroup(parent, id, data, true);
        });

        // Toggle the flag back to default
        clientErrorExistsCheck = false;

        // Message summaries are going to be shown
        messageSummariesShown = true;

        // Finally, write the result of the validation messages
        writeMessagesForPage();
    }

    if (!validForm) {
        validForm = false;

        //ensure all non-visible controls are visible to the user
        jQuery(".error:not(:visible)").each(function () {
            cascadeOpen(jQuery(this));
        });

        jumpToTop();
        showClientSideErrorNotification();
        jQuery(".uif-pageValidationMessages li.uif-errorMessageItem:first > a").focus();
    }

    jq.watermark.showAll();
    pauseTooltipDisplay = false;

    return validForm;
}

/**
 * Calls the updateComponent method on the controller with component id passed in, this id is
 * the component id with any/all suffixes on it not the dictionary id
 *
 * <p>
 * Retrieves the component with the matching id from the server and replaces a matching
 * _refreshWrapper marker span with the same id with the result.  In addition, if the result contains a label
 * and a displayWith marker span has a matching id, that span will be replaced with the label content
 * and removed from the component.  This allows for label and component content separation on fields
 * </p>
 *
 * @param id - id for the component to retrieve
 * @param methodToCall - name of the method that should be invoked for the refresh call (if custom method is needed)
 * @param successCallback - (optional) additional callback function to be executed after the component is retrieved
 * @param additionalData - (optional) additional data to be submitted with the request
 * @param disableBlocking - (optional) turns off blocking and loading messaging
 */
function retrieveComponent(id, methodToCall, successCallback, additionalData, disableBlocking) {
    var refreshComp = jQuery("#" + id);

    // if a call is made from refreshComponentUsingTimer() and the component does not exist on the page or is hidden
    // then get the handle of the refreshTimer and clear the timer. Also remove it from the refreshTimerComponentMap
    if (refreshComp === undefined || refreshComp.filter(':visible').length === 0) {
        var refreshHandle = refreshTimerComponentMap[id];
        if (!(refreshHandle === undefined)) {
            clearInterval(refreshHandle);
            delete refreshTimerComponentMap[id];

            return;
        }
    }

    if (!methodToCall) {
        methodToCall = kradVariables.REFRESH_METHOD_TO_CALL;
    }

    var kradRequest = new KradRequest();

    kradRequest.methodToCall = methodToCall;
    kradRequest.ajaxReturnType = kradVariables.RETURN_TYPE_UPDATE_COMPONENT;
    kradRequest.successCallback = successCallback;
    kradRequest.additionalData = additionalData;
    kradRequest.refreshId = id;

    if (disableBlocking) {
        kradRequest.disableBlocking = disableBlocking;
    }

    kradRequest.send();
}

/**
 * Performs client side validation against the controls present in a collection add line
 *
 * @param collectionGroupId - id for the collection whose add line should be validated
 * @param addViaLightbox - (optional) flag to indicate if add controls are in a lightbox
 */
function validateAddLine(collectionGroupId, addViaLightbox) {
    var collectionGroup = jQuery("#" + collectionGroupId);
    var addControls = collectionGroup.data(kradVariables.ADD_CONTROLS);

    if (addViaLightbox) {
        collectionGroup = jQuery("#kualiLightboxForm");
    }

    var controlsToValidate = jQuery(addControls, collectionGroup);

    var valid = validateLineFields(controlsToValidate);
    if (!valid) {
        if (!addViaLightbox) {
            showClientSideErrorNotification();
        }

        return false;
    }

    return true;
}

/**
 * Performs client side validation against the controls present in a collection line
 *
 * @param collectionName - name (binding path) for the collection
 * @param lineIndex - zero based index for the collection line
 */
function validateLine(collectionName, lineIndex) {
    var controlsToValidate = jQuery("[name^='" + collectionName + "[" + lineIndex + "]']");

    var valid = validateLineFields(controlsToValidate);
    if (!valid) {
        showClientSideErrorNotification();

        return false;
    }

    return true;
}

/**
 * Performs client side validation on the list of given controls and returns whether the controls
 * are valid
 *
 * @param controlsToValidate - list of controls (jQuery wrapping objects) that should be validated
 */
function validateLineFields(controlsToValidate) {
    var valid = true;

    // skip completely if client validation is off
    if (!validateClient) {
        return valid;
    }

    jQuery.watermark.hideAll();

    // Turn on this flag to avoid prematurely writing out messages which will cause performance issues if MANY
    // fields have validation errors simultaneously (first we are only checking for errors, not checking and
    // writing simultaneously like normal)
    clientErrorExistsCheck = true;

    // Temporarily turn off this flag to avoid traversing unneeded logic (all messages will be shown at the end)
    var tempMessagesSummariesShown = messageSummariesShown;
    messageSummariesShown = false;

    controlsToValidate.each(function () {
        var control = jQuery(this);
        var fieldId = jQuery(this).closest("div[data-role='InputField']").attr("id");
        var field = jQuery("#" + fieldId);
        var parent = field.data("parent");
        var validValue = true;

        // remove ignoreValid because there are issues with the plugin if it stays on
        control.removeClass("ignoreValid");

        haltValidationMessaging = true;

        if (!control.prop("disabled") && !control.hasClass("uif-readOnlyContent")) {
            control.valid();
            if (control.hasClass("error")) {
                validValue = false;
            }
        }

        var data = getValidationData(field);
        handleMessagesAtGroup(parent, fieldId, data, true);

        haltValidationMessaging = false;

        //details visibility check
        if (control.not(":visible") && !validValue) {
            cascadeOpen(control);
        }

        if (!validValue) {
            valid = false;
        }

        control.addClass("ignoreValid");
    });

    // Toggle the flag back to default
    clientErrorExistsCheck = false;

    // Message summaries are going to be shown
    messageSummariesShown = tempMessagesSummariesShown;

    if (messageSummariesShown) {
        // Finally, write the result of the validation messages
        writeMessagesForPage();
    }

    jQuery.watermark.showAll();

    return valid;
}

/**
 * Retrieves a page for the collection by id specified, the linkElement supplied must have "num" data to retrieve
 * the page; this method refreshes the collection with new page showing
 *
 * @param linkElement the link clicked with "num" data specifying the page to retrieve
 * @param collectionId the collection by id to retrieve the new page from
 */
function retrieveCollectionPage(linkElement, collectionId) {
    var link = jQuery(linkElement);
    var parentLI = link.parent();

    // Skip processing if the link supplied is disabled or active
    if (parentLI.is(kradVariables.DISABLED_CLASS) || parentLI.is(kradVariables.ACTIVE_CLASS)) {
        return;
    }

    var pageNumber = jQuery(linkElement).data(kradVariables.PAGE_NUMBER_DATA);
    retrieveComponent(collectionId, kradVariables.RETRIEVE_COLLECTION_PAGE_METHOD_TO_CALL,
            null, {pageNumber: pageNumber}, true);
}

/**
 * Ensures that the componentObject is visible by "opening" mechanisms that may be hiding it such as
 * row details or group disclosure.  Used to make invalid fields visible on validation.
 *
 * @param componentObject the object to check for visibility of and "open" parent containing elements to make
 * it visible
 */
function cascadeOpen(componentObject) {
    if (componentObject.not(":visible")) {
        var detailsDivs = componentObject.parents("div[data-role='details']");
        detailsDivs.each(function () {
            jQuery(this).parent().find("> a").click();
        });

        var disclosureDivs = componentObject.parents("div[data-role='disclosureContent']");
        disclosureDivs.each(function () {
            if (!jQuery(this).data("open")) {
                jQuery(this).parent().find("a[data-linkfor='" + jQuery(this).attr("id") + "']").click();
            }
        });
    }
}

/** Progressive Disclosure */

/**
 * Same as setupRefreshCheck except the condition will always be true (always refresh when
 * value changed on control)
 *
 * @param controlName - value for the name attribute for the control the event should be generated for
 * @param refreshId - id for the component that should be refreshed when change occurs
 * @param methodToCall - name of the method that should be invoked for the refresh call (if custom method is needed)
 */
function setupOnChangeRefresh(controlName, refreshId, methodToCall) {
    setupRefreshCheck(controlName, refreshId, function () {
        return true;
    }, methodToCall);
}

/**
 * Sets up the conditional refresh mechanism in js by adding a change handler to the control
 * which may satisfy the conditional refresh condition passed in.  When the condition is satisfied,
 * refresh the necessary content specified by id by making a server call to retrieve a new instance
 * of that component
 *
 * @param controlName - value for the name attribute for the control the event should be generated for
 * @param refreshId - id for the component that should be refreshed when condition occurs
 * @param condition - function which returns true to refresh, false otherwise
 * @param methodToCall - name of the method that should be invoked for the refresh call (if custom method is needed)
 */
function setupRefreshCheck(controlName, refreshId, condition, methodToCall) {
    jQuery("[name='" + escapeName(controlName) + "']").live('change', function () {
        // visible check because a component must logically be visible to refresh
        var refreshComp = jQuery("#" + refreshId);
        if (refreshComp.length) {
            if (condition()) {
                retrieveComponent(refreshId, methodToCall);
            }
        }
    });
}

/**
 * Setup disabled check handlers that will evaluate a passed in condition and will disable/enable the component
 * based on the result (true to disable, false to enable).  controlName represents the field to be evaluated and
 * disableCompId represents the component by id to be disabled/enabled as a result.
 *
 * @param controlName name of the control to put a handler on
 * @param disableCompId id of the component to disable/enable
 * @param disableCompType type of the component being disabled/enabled
 * @param condition function that if returns true disables the component, and if returns false enables the component
 * @param onKeyUp true if evaluating on keyUp, only applies to textarea/text inputs
 */
function setupDisabledCheck(controlName, disableCompId, disableCompType, condition, onKeyUp) {
    var theControl = jQuery("[name='" + escapeName(controlName) + "']");
    var eventType = 'change';

    if (onKeyUp && (theControl.is("textarea") || theControl.is("input[type='text'], input[type='password']"))) {
        eventType = 'keyup';
    }

    if (disableCompType == "radioGroup" || disableCompType == "checkboxGroup") {
        theControl.on(eventType, function () {
            if (condition()) {
                jQuery("input[id^='" + disableCompId + "']").prop("disabled", true);
            }
            else {
                jQuery("input[id^='" + disableCompId + "']").prop("disabled", false);
            }
        });
    }
    else {
        theControl.on(eventType, function () {
            var disableControl = jQuery("#" + disableCompId);
            if (condition()) {
                disableControl.prop("disabled", true);
                disableControl.addClass("disabled");
                if (disableCompType === "actionLink" || disableCompType === "action") {
                    disableControl.attr("tabIndex", "-1");
                }
            }
            else {
                disableControl.prop("disabled", false);
                disableControl.removeClass("disabled");
                if (disableCompType === "actionLink" || disableCompType === "action") {
                    disableControl.attr("tabIndex", "0");
                }
            }
        });
    }
}

/**
 * Sets up the progressive disclosure mechanism in js by adding a change handler to the control
 * which may satisfy the progressive disclosure condition passed in.  When the condition is satisfied,
 * show the necessary content, otherwise hide it.  If the content has not yet been rendered then a server
 * call is made to retrieve the content to be shown.  If alwaysRetrieve is true, the component
 * is always retrieved from the server when disclosed.
 * Do not add check if the component is part of the "old" values on a maintanance document (endswith _c0).
 *
 * @param controlName
 * @param disclosureId
 * @param condition - function which returns true to disclose, false otherwise
 * @param methodToCall - name of the method that should be invoked for the retrieve call (if custom method is needed)
 */
function setupProgressiveCheck(controlName, disclosureId, baseId, condition, alwaysRetrieve, methodToCall) {
    if (!baseId.match("\_c0$")) {
        jQuery("[name='" + escapeName(controlName) + "']").live('change', function () {
            var refreshDisclosure = jQuery("#" + disclosureId);
            if (refreshDisclosure.length) {
                var displayWithId = disclosureId;

                if (condition()) {
                    if (refreshDisclosure.data("role") == "placeholder" || alwaysRetrieve) {
                        retrieveComponent(disclosureId, methodToCall);
                    }
                    else {
                        refreshDisclosure.addClass(kradVariables.PROGRESSIVE_DISCLOSURE_HIGHLIGHT_CLASS);
                        refreshDisclosure.show();

                        if (refreshDisclosure.parent().is("td")) {
                            refreshDisclosure.parent().show();
                        }

                        refreshDisclosure.animate({backgroundColor: "transparent"}, 6000);

                        //re-enable validation on now shown inputs
                        hiddenInputValidationToggle(disclosureId);

                        var displayWithLabel = jQuery(".displayWith-" + displayWithId);
                        displayWithLabel.show();
                        if (displayWithLabel.parent().is("td") || displayWithLabel.parent().is("th")) {
                            displayWithLabel.parent().show();
                        }
                    }
                }
                else {
                    refreshDisclosure.hide();

                    // ignore validation on hidden inputs
                    hiddenInputValidationToggle(disclosureId);

                    var displayWithLabel = jQuery(".displayWith-" + displayWithId);
                    displayWithLabel.hide();
                    if (displayWithLabel.parent().is("td") || displayWithLabel.parent().is("th")) {
                        displayWithLabel.parent().hide();
                    }
                }

                hideEmptyCells();
            }
        });
    }
}

/**
 * Disables client side validation on any inputs within the element(by id) passed in , if
 * that element is hidden.  Otherwise, it turns input validation back on if the element and
 * its children are visible
 *
 * @param id - id for the component for which the input hiddens should be processed
 */
function hiddenInputValidationToggle(id) {
    var element = jQuery("#" + id);
    if (element.length) {
        if (element.css("display") === "none") {
            jQuery(":input:hidden", element).each(function () {
                storeOriginalDisabledProperty(jQuery(this));
                jQuery(this).addClass("ignoreValid");
                //disable hidden inputs to prevent from being submitted
                jQuery(this).prop("disabled", true);
            });
        }
        else {
            jQuery(":input:visible", element).each(function () {
                storeOriginalDisabledProperty(jQuery(this));
                jQuery(this).removeClass("ignoreValid");
                //return to original disabled property value
                jQuery(this).prop("disabled", jQuery(this).data('original-disabled'));
            });
        }
    }
}

/**
 * Stores the original value of the disabled property of the element into jquery data.
 * This ensures that the correct value is set after toggling in hiddenInputValidation().
 *
 * @param element - jQuery element to examine and set the original-disabled data.
 */
function storeOriginalDisabledProperty(element) {
    //capture original disabled property value
    if (element.data('original-disabled') === undefined) {
        element.data("original-disabled", element.prop("disabled"));
    }
}

/**
 * Refreshes a component by calling retrieveComponent() at the given time interval
 *
 * @param componentId - id of the component to be refreshed
 * @param methodToCall - controller method to call on refresh
 * @param timeInterval -  interval in seconds at which the component should be refreshed
 */
function refreshComponentUsingTimer(componentId, methodToCall, timeInterval) {
    var refreshTimer = refreshTimerComponentMap[componentId];

    // if a timer already exists for the component then clear it and remove it from the map
    // this is done so that the time interval between executions remains the same.
    if (refreshTimer != null) {
        clearInterval(refreshTimer);
        delete refreshTimerComponentMap[componentId];
    }

    //set a new timer on the component
    refreshTimerComponentMap[componentId] = setInterval(function () {
        retrieveComponent(componentId, methodToCall);
    }, timeInterval * 1000);
}

/**
 *  Open a hidden section in a jquery bubblepopup and freeze it until the user
 *  clicks outside of the popup, or on the optional close button.
 *
 * @param e event (required)
 * @param contentId id of hidden section with content (required)
 * @param popupOptions map of bubblepopup options (optional)
 * @param closeButton when true, a small close button is rendered in the top-right corner of the popup (optional)
 **/
function openPopupContent(e, contentId, popupOptions, closeButton) {
    stopEvent(e);

    var popupTarget = jQuery((e.currentTarget) ? e.currentTarget : e.srcElement);
    if (popupTarget.IsBubblePopupOpen()) {
        return;
    }

    // in case a prior popup is still open
    if (gCurrentBubblePopupId) {
        _hideBubblePopup(jQuery("#" + gCurrentBubblePopupId));
    }
    jQuery(".uif-tooltip").HideAllBubblePopups(); // just in case, case

    gCurrentBubblePopupId = popupTarget.attr('id');
    var clickName = "click." + gCurrentBubblePopupId;
    var popupContent = jQuery("#" + contentId).detach().show();

    // add required class uif-tooltip to action and create popup
    if (!popupTarget.HasBubblePopup()) {
        popupTarget.addClass("uif-tooltip");
        //initBubblePopups();  // shotgun approach to CreateBubblePopup, versus...
        popupTarget.CreateBubblePopup(".uif-tooltip");

        if (closeButton) {
            var closeButton = jQuery('<div class="uif-popup-closebutton"/>');
            closeButton.on(clickName, function () {
                _hideBubblePopup(popupTarget)
            });
            popupContent.prepend(closeButton);
        }

        // Odd error work-around with 2 popup forms: pressing Enter in a text
        // field on the second popup causes a click event on the first button.
        // True.
        jQuery("input,select", popupContent).keypress(function (event) {
            if (event.keyCode == 10 || event.keyCode == 13) {
                stopEvent(event);
            }
        });
    }

    var clonedDefaultOptions = jQuery.extend({}, kradVariables.FORM_BUBBLEPOPUP_DEFAULT_OPTIONS);
    clonedDefaultOptions.themePath = getConfigParam(kradVariables.APPLICATION_URL) + this.BUBBLEPOPUP_THEME_PATH;

    jQuery.extend(clonedDefaultOptions, popupOptions);

    popupTarget.ShowBubblePopup(clonedDefaultOptions, true);
    popupTarget.FreezeBubblePopup();

    var popupId = popupTarget.GetBubblePopupID();
    jQuery("div#" + popupId + " td.jquerybubblepopup-innerHtml").append(popupContent);

    // close popup on any click outside current popup
    jQuery(document).on(clickName, function (e) {
        var docTarget = jQuery((e.target) ? e.target : e.srcElement);
        if (docTarget.parents("div.jquerybubblepopup").length === 0) {
            _hideBubblePopup(popupTarget);
        }
    });

    // Note: afterHidden property causes openPopupContentsWithErrors() to break
    function _hideBubblePopup(target) {
        target.HideBubblePopup();
        jQuery(document).off("click." + target.attr('id'));
        gCurrentBubblePopupId = "";
    }
}

/**
 *  Locate all bubblepopup content and see if any have a error displayed (via
 *  class "uif-hasError").  If so, locate the action which opens the content
 *  and submit the click event for that action.
 **/
function openPopupContentsWithErrors() {
    var hiddenScript, popupFormId;
    var bubblePopupContent = {};
    jQuery("div.uif-bubblepopup-content").each(function () {
        if (bubblePopupContent[this.id] == true) {
            // .detach() apparently creates duplicates in the DOM, and this code eliminates them
            return false;
        }
        bubblePopupContent[this.id] = true;

        if (jQuery('.uif-hasError', jQuery(this)).length > 0) {
            popupFormId = this.id;
            // find the action linked to this popup via the openPopupContent() function:
            jQuery('.uif-action').siblings('input[type="hidden"][data-role="script"]').each(function () {

                hiddenScript = jQuery(this).val();
                if ((hiddenScript.indexOf('openPopupContent') != -1)
                        && (hiddenScript.indexOf(popupFormId) != -1)) {
                    var saveSpeed = (typeof kradVariables.FORM_BUBBLEPOPUP_DEFAULT_OPTIONS['openingSpeed'] === "undefined") ?
                            -1 : kradVariables.FORM_BUBBLEPOPUP_DEFAULT_OPTIONS['openingSpeed'];

                    // open popup as fast as possible
                    kradVariables.FORM_BUBBLEPOPUP_DEFAULT_OPTIONS['openingSpeed'] = 1;
                    jQuery('#' + jQuery(this).attr('data-for')).click();
                    kradVariables.FORM_BUBBLEPOPUP_DEFAULT_OPTIONS['openingSpeed'] = saveSpeed;

                    // break from .each
                    return false;
                }
            });
        }
    });
}

