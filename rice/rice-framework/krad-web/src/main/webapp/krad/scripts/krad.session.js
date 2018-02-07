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
/*
* Collection of functions related to session handling
*
* @author Kuali Rice Team (rice.collab@kuali.org)
*/

/**
 * Invoked after a timeout is received for an Ajax request
 *
 * @param redirectUrl url returned from the server to redirect to
 */
function handleAjaxSessionTimeout(redirectUrl) {
    redirect(redirectUrl);
}

/**
 * Redirects the user to the system logout URL
 */
function logoutUser() {
    var logoutUrl = getConfigParam(kradVariables.KRAD_URL) + "/listener?methodToCall=logout";

    redirect(logoutUrl);
}

/**
 * Initializes timers objects for giving the session timeout warning dialog and the session timeout
 * dialog
 *
 * <p>
 * Note this will be invoked if enableTimeoutWarning is set to true in the View configuration
 * </p>
 *
 * <p>
 * This broadcasts a message through local storage so the timers for views opened in other windows
 * will reset as well. In addition, we setup a lister for the storage event so this window can receive
 * notifications from other windows about any updates or session dialog responses
 * </p>
 *
 * @param warningInterval number of milliseconds until timeout warning should be given
 * @param timeoutInterval number of milliseconds until timeout dialog should be shown
 */
function initializeSessionTimers(warningInterval, timeoutInterval) {
    sessionWarningTimer = new KradTimer(showSessionTimeoutWarning, warningInterval);
    sessionTimeoutTimer = new KradTimer(showSessionTimeout, timeoutInterval);

    broadcastSessionUpdate();

    window.addEventListener("storage", checkForSessionUpdate);
}

/**
 * Shows the session timeout warning dialog, invoked by the session warning timer
 */
function showSessionTimeoutWarning() {
    showLightboxComponent(kradVariables.SESSION_TIMEOUT_WARNING_DIALOG);
}

/**
 * Shows the session timeout dialog, invoked by the session timer
 */
function showSessionTimeout() {
    showLightboxComponent(kradVariables.SESSION_TIMEOUT_DIALOG);
}

/**
 * Resets the timers for showing the session timeout warning dialog and timeout dialog
 *
 * <p>
 * Invoked automatically to reset the timers when an Ajax call is successfully made, see ajaxSetup in
 * krad.initialize.js
 * </p>
 *
 * <p>
 * This broadcasts a message through local storage so the timers for views opened in other windows
 * will reset as well
 * </p>
 */
function resetSessionTimers() {
    if (sessionWarningTimer) {
        sessionWarningTimer.reset();
    }

    if (sessionTimeoutTimer) {
        sessionTimeoutTimer.reset();
    }

    broadcastSessionUpdate();
}

/**
 * Handles the users selection to the session timeout warning dialog
 *
 * <p>
 * User may select to continue work, in which case we will send a server call to reset the session. Or
 * the user may select to logout, in which case we redirect the user to the logout URL.
 * </p>
 *
 * <p>
 * A broadcast message is also sent so if the dialog is present in other windows, the users response will
 * be picked up there as well
 * </p>
 *
 * @param event instance of the dialog response event, contains the value for the response input
 */
function handleTimeoutWarningResponse(event) {
    closeLightbox();

    if (event.value == 'continue') {
        var response = invokeServerListener(kradVariables.KEEP_SESSION_ALIVE_METHOD_TO_CALL);
        if (response.status == kradVariables.SUCCESS_RESPONSE) {
            resetSessionTimers();
        }

        broadcastSessionDialogResponse('continue');
    }
    else if (event.value == 'logout') {
        broadcastSessionDialogResponse('logout');

        logoutUser();
    }
}

/**
 * Handlers the users selection to the session timeout dialog
 *
 * <p>
 * The users only option is the okay response, in which case we just close the lightbox and broadcast the
 * response to other windows
 * </p>
 *
 * @param event instance of the dialog response event, contains the value for the response input
 */
function handleTimeoutResponse(event) {
    closeLightbox();

    broadcastSessionDialogResponse('okay');
}

/**
 * Handler for the storage event that checks for message broadcast for session events
 *
 * <p>
 * There are two types of messages sent through this event. One is the session update event. This gets
 * broadcast when another window has been refreshed (thus the session on the server has been refreshed and we
 * want to reset the timer for this window). The other message is broadcast when the user responds to a session
 * dialog in another window. In this case we want to close any session dialog that might be open in this window
 * and take the corresponding action
 * </p>
 *
 * @param event instance of a storage event where the key is the message type being sent
 */
function checkForSessionUpdate(event) {
    // if another page was refreshed reset the session timers for this page
    if ((event.key == 'sessionUpdate') && (event.newValue == true)) {
        resetSessionTimers();
    }

    // if the user responded to a session dialog in another page, we need to reset the timers (in the case of continue)
    // and close the session dialogs for this page if any are displaying
    if (event.key == 'sessionDialogResponse') {
        if ((activeDialogId == kradVariables.SESSION_TIMEOUT_WARNING_DIALOG)
                || (activeDialogId == kradVariables.SESSION_TIMEOUT_DIALOG)) {
            closeLightbox();
        }

        if (event.newValue == 'continue') {
            resetSessionTimers();
        }
        else if (event.newValue == 'logout') {
            logoutUser();
        }
    }
}

/**
 * Broadcasts a session update message by updating local storage
 */
function broadcastSessionUpdate() {
    if (localStorage) {
        localStorage.setItem('sessionUpdate', true);
        localStorage.removeItem('sessionUpdate');
    }
}

/**
 * Broadcasts a session dialog response message by updating local storage
 *
 * @param response the value for the response input the user selected
 */
function broadcastSessionDialogResponse(response) {
    if (localStorage) {
        localStorage.setItem('sessionDialogResponse', response);
        localStorage.removeItem('sessionDialogResponse');
    }
}

/**
 * Basic timer object that has a start, stop, and reset method
 *
 * @param action function that should be invoked when the timer completes
 * @param interval number of milliseconds before action should be triggered
 * @constructor
 */
function KradTimer(action, interval) {
    this.action = action;
    this.interval = interval;

    this.start();
}

KradTimer.prototype = {
    // amount of time to wait
    interval : 0,

    // the action (function) to invoke when time has elapsed
    action : null,

    // internal timer object
    _timerId : null,

    start : function() {
        this._timerId = window.setTimeout(this.action, this.interval);
    },

    reset : function() {
        this.stop();
        this.start();
    },

    stop : function() {
        window.clearTimeout(this._timerId);
    }
}