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
 * Show growl with message, title and theme passed in
 *
 * @param message message of this jGrowl
 * @param title title of this jGrowl, can be empty string for none
 * @param theme class to append to jGrowl classes, can be empty string for none
 */
function showGrowl(message, title, theme) {
    var context = getContext();
    if (theme) {
        context.jGrowl(message, { header:title, theme:theme});
    }
    else {
        context.jGrowl(message, { header:title});
    }
}

/**
 * Set default growl options for this view
 *
 * @param options
 */
function setGrowlDefaults(options) {
    var context = getContext();
    context.jGrowl.defaults = context.extend(context.jGrowl.defaults, options);
}

/**
 * Invoked to initialize defaults for refresh or navigation blocking
 *
 * @param options - default options for the block ui plugin
 * @param blockingType - type of blocking the options should apply to
 */
function setBlockUIDefaults(options, blockingType) {
    var opts = {};

    if (!options || !blockingType) {
        return;
    }

    if (blockingType == "navigation") {
        opts.navigationOptions = options;
    } else if (blockingType == "refresh") {
        opts.refreshOptions = options;
    }

    var context = getContext();
    context.blockUI.defaults = context.extend(context.blockUI.defaults, opts);
}

/**
 * Uses jQuery blockUI plug-in to show a loading notification
 *
 * <p>
 * The loading functionality is used to block an area of the screen and to display a message while
 * a request is being processed. If elementToBlock is given (known as a refresh block), the blocking will occur
 * only on the area covered by that element. Else the entire window will be blocked (known as navigation blocking).
 * </p>
 *
 * <p>
 * Depending on whether the blocking is for a refresh or navigation, separate blockUI defaults will be used. These
 * defaults are initialized through the View object or script
 * </p>
 *
 * @param loadingMessage - (optional) message to display while blocking, defaults to 'Loading...'
 * @param elementToBlock - (optional) jQuery object representing an element that should be blocked, defaults to
 * entire window if not given
 * @param replaceElement - (optional) boolean that indicates to replace the element contents with the loading
 * notification instead of an overlay, defaults to false
 * @param options - (optional) adding plug-in options for blockUI
 */
function showLoading(loadingMessage, elementToBlock, replaceElement, options) {
    var context = getContext();

    if (elementToBlock && elementToBlock.length) {
        var blockingOptions = context.blockUI.defaults.refreshOptions || {};
        var loadingContent = refreshImage;
    }
    else {
        var blockingOptions = context.blockUI.defaults.navigationOptions || {};
        var loadingContent = navigationImage;
    }

    if (!loadingMessage) {
        loadingMessage = getMessage(kradVariables.MESSAGE_LOADING);
    }

    loadingContent = loadingContent.attr("alt", loadingMessage).get(0).outerHTML + " " + loadingMessage;

    if (elementToBlock && elementToBlock.length) {
        if (replaceElement) {
            elementToBlock.html(loadingContent);
        }
        else {
            blockingOptions = jQuery.extend(blockingOptions, {message:loadingContent});
            elementToBlock.block(blockingOptions);
        }
    }
    else {
        loadingContent = '<h1>' + loadingContent + '</h1>';
        blockingOptions = jQuery.extend(blockingOptions, {message:loadingContent});
        context.blockUI(blockingOptions);
    }
}

/**
 * Invoked to remove a loading/blocking indicator
 *
 * @param elementToBlock - (optional) the element the blocking is on, if not given it is assumed the entire
 * window is being blocked
 */
function hideLoading(elementToBlock) {
    if (nonEmpty(elementToBlock)) {
        elementToBlock.unblock();
    }
    else {
        var context = getContext();
        context.unblockUI();
    }
}

/**
 * Adds the icon that indicates the contents of a field have changed from the compared value (for instance the new side
 * on maintenance documents) to the field markers span
 *
 * @param fieldId - id for the field the icon should be added to
 */
function showChangeIcon(fieldId) {
    var fieldMarkerSpan = jQuery("#" + fieldId + "_markers");
    var fieldIcon = jQuery("#" + fieldId + "_changeIcon");

    if (fieldMarkerSpan.length > 0 && fieldIcon.length == 0) {
        fieldMarkerSpan.append("<img id='" + fieldId + "_changeIcon' alt='" + getMessage(kradVariables.MESSAGE_CHANGE) + "' src='" + getConfigParam(kradVariables.IMAGE_LOCATION) + "asterisk_orange.png'>");
    }
}

/**
 * Add icon to a group header that indicates the data for the group has changed
 *
 * @param headerFieldId - id for the header field the icon should be added to
 */
function showChangeIconOnHeader(headerFieldId) {
    showChangeIconOnGroupHeader(headerFieldId, "_div");
}

/**
 * Add icon to a group header that indicates the data for the group has changed
 *
 * @param headerFieldId - id for the header field the icon should be added to
 */
function showChangeIconOnDisclosure(headerFieldId) {
    showChangeIconOnGroupHeader(headerFieldId, "_toggle");
}

/**
 * Add icon to a group header element (disclosure/header) that indicates the data for the group has changed
 *
 * @param fieldId - id for the header field the icon should be added to
 */
function showChangeIconOnGroupHeader(fieldId, idSuffix) {
    var targetElement = jQuery("#" + fieldId + idSuffix).find("[class~=uif-headerText-span]");
    var headerIcon = jQuery("#" + fieldId + "_changeIcon");

    if (targetElement.length > 0 && headerIcon.length == 0) {
        targetElement.append("<img id='" + fieldId + "_changeIcon' class='" + kradVariables.CHANGED_HEADER_ICON_CLASS + "' alt='" + getMessage(kradVariables.MESSAGE_CHANGE) + "' src='" + getConfigParam(kradVariables.IMAGE_LOCATION) + "asterisk_orange.png'>");
    }
}

// Applies the watermark to the input with the id specified
function createWatermark(id, watermark) {
    jQuery("#" + id).watermark(watermark);
}

/**
 * If the content is an incident report view returns true, otherwise returns false
 *
 * @param content - response contents
 * @returns {Boolean} true if there was an incident, false otherwise
 */
function checkForIncidentReport(content) {
    var viewId = jQuery("#viewId", content);
    if (viewId.length && viewId.val() === kradVariables.INCIDENT_REPORT_VIEW_CLASS) {
        return true;
    }
    else {
        return false;
    }
}

/**
 * Called when client side validation is performed (for an action) and are errors
 * are present to display a notification to the user
 *
 * @param message - (optional) message for notification, default to generic message if not given
 */
function showClientSideErrorNotification(message) {
    if (!message) {
        message = getMessage(kradVariables.MESSAGE_FORM_CONTAINS_ERRORS);
    }

    showGrowl(message, getMessage(kradVariables.MESSAGE_ERROR), 'errorGrowl');
}

