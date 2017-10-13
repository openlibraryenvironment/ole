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
 * Holds configuration for making a server request (ajax and non-ajax) and
 * performs the request action
 *
 * @param action - (optional) reference to the action that triggers the request. If given request
 * attributes will be pulled from the action data
 */
function KradRequest(action) {
    if (action) {
        if (action.data("ajaxsubmit") !== undefined) {
            this.ajaxSubmit = action.data("ajaxsubmit");
        }

        this.additionalData = action.data(kradVariables.SUBMIT_DATA);

        this.methodToCall = this.additionalData['methodToCall'];

        if (action.data("successcallback") !== undefined) {
            this.successCallback = action.data("successcallback");
        }

        if (action.data("errorcallback") !== undefined) {
            this.errorCallback = action.data("errorcallback");
        }

        if (action.data("presubmitcall") !== undefined) {
            this.preSubmitCall = action.data("presubmitcall");
        }

        if (action.data("validate") !== undefined) {
            this.validate = action.data("validate");
        }

        if (action.data("loadingmessage") !== undefined) {
            this.loadingMessage = action.data("loadingmessage");
        }

        if (action.data("disableblocking") !== undefined) {
            this.disableBlocking = action.data("disableblocking")
        }

        if (action.data("ajaxreturntype") !== undefined) {
            this.ajaxReturnType = action.data("ajaxreturntype");
        }

        if (action.data("refreshid") !== undefined) {
            this.refreshId = action.data("refreshid");
        }

        if (action.data("dirtyonaction") !== undefined) {
            this.dirtyOnAction = action.data("dirtyonaction");
        }

        if (action.data("cleardirtyonaction") !== undefined) {
            this.clearDirtyOnAction = action.data("cleardirtyonaction");
        }
    }
}

KradRequest.prototype = {
    // name of the controller method to be invoked
    methodToCall: "refresh",

    // additional data to send with the request (in addition to form data)
    additionalData: {},

    // indicates whether the request should be made with ajax or standard browser submit
    ajaxSubmit: true,

    // for ajax requests, specifies how the response should be handled
    ajaxReturnType: "update-page",

    // when the return type is update-component, indicates the id for the component that
    // should be updated
    refreshId: null,

    // indicates whether client side validation should be performed before making
    // the request (see ajaxReturnHandlers)
    validate: false,

    dirtyOnAction: false,

    clearDirtyOnAction: false,

    // when blocking is enabled will display this text with the blocking overlay
    loadingMessage: getMessage(kradVariables.MESSAGE_LOADING),

    // jQuery object that should be blocked while the request is sent, if empty
    // and return type is update-component, the component will be blocked, else the full window
    // will be blocked
    elementToBlock: null,

    // indicates whether blocking should be disabled for the request
    disableBlocking: false,

    // function or script that should be invoked before the request is sent
    // if the function returns false the request is not carried out
    // the function can optionally take the request object and modify any of the
    // request attributes (for example add additional data)
    // Note as well: the preSubmitCall can be given as a string or function object. When given as a string it may
    // optionally take the request by including the parameter 'this'. Other literal parameters may be passed as well
    // (literal on client, but useful for passing server side variables)
    preSubmitCall: null,

    // function or script that is invoked after a successful ajax request
    // the function may take the response contents as a parameter
    // Note as well: the successCallback can be given as a string or function object. When given as a string it may
    // optionally take the response contents by including the parameter 'responseContents'. Other literal parameters
    // may be passed as well (literal on client, but useful for passing server side variables)
    successCallback: null,

    // function or script that is invoked after an error is encountered from an ajax request
    // (including when an incident page is returned. The function may take the response contents as a parameter
    // Note as well: the successCallback can be given as a string or function object. When given as a string it may
    // optionally take the response contents by including the parameter 'responseContents'. Other literal parameters
    // may be passed as well (literal on client, but useful for passing server side variables)
    errorCallback: null,

    // called to make the request and handle the response
    send: function () {
        var data = {};

        // invoke validateForm if validate flag is true, if returns false do not continue
        if (this.validate && !validateForm()) {
            clearHiddens();

            return;
        }

        // invoke the preSubmitCall script, if it evaluates to false return
        if (this.preSubmitCall) {
            // expose a variable for preSubmitCode
            var kradRequest = this;
            if (typeof this.preSubmitCall == "string") {
                var preSubmitCode = "(function(){" + this.preSubmitCall + "})();";
                var preSubmitValid = eval(preSubmitCode);
            } else {
                var preSubmitValid = this.preSubmitCall(this);
            }

            if (!preSubmitValid) {
                clearHiddens();

                return;
            }
        }

        //reset dirty form state
        if (this.clearDirtyOnAction){
            dirtyFormState.reset();
        }

        //increase dirty field count when this flag is true
        if (this.dirtyOnAction){
            dirtyFormState.incrementDirtyFieldCount();
        }

        // check for non-ajax request
        if (!this.ajaxSubmit) {
            this._submit();

            return;
        }

        data.methodToCall = this.methodToCall;
        data.ajaxReturnType = this.ajaxReturnType;
        data.ajaxRequest = this.ajaxSubmit;

        if (this.refreshId) {
            data.updateComponentId = this.refreshId;
        }

        if (this.additionalData) {
            jQuery.extend(data, this.additionalData);
        }

        var jsonViewState = getSerializedViewState();
        if (jsonViewState) {
            jQuery.extend(data, {clientViewState: jsonViewState});
        }

        // check if called from a lightbox, if it is set the lightboxCompId
        var lightboxCompId = undefined;
        if (jQuery('#kualiLightboxForm').children(':first').length == 1) {
            lightboxCompId = jQuery('#kualiLightboxForm').children(':first').attr('id');
        }

        // create a reference to the request for ajax callbacks
        var request = this;

        var submitOptions = {
            data: data,
            success: function (response) {
                var responseContents = document.createElement('div');
                responseContents.innerHTML = response;

                // for lightbox copy data back into lightbox
                if (lightboxCompId !== undefined) {
                    jQuery('#' + lightboxCompId + "_dialogPlaceholder").empty();
                }

                // create a response object to process the response contents
                var kradResponse = new KradResponse(responseContents);
                kradResponse.processResponse();

                var hasError = checkForIncidentReport(response);
                if (!hasError) {
                    if (request.successCallback) {
                        if (typeof request.successCallback == "string") {
                            eval(request.successCallback);
                        } else {
                            request.successCallback(responseContents);
                        }
                    }
                } else if (request.errorCallback) {
                    if (typeof request.errorCallback == "string") {
                        eval(request.errorCallback);
                    } else {
                        request.errorCallback(responseContents);
                    }
                }

                clearHiddens();
            },
            error: function (jqXHR, textStatus) {
                if (request.errorCallback) {
                    if (typeof request.errorCallback == "string") {
                        eval(request.errorCallback);
                    } else {
                        request.errorCallback();
                    }
                }
                else {
                    alert("Request failed: " + textStatus);
                }
            }
        };

        this._setupBlocking(submitOptions);

        // for lightbox copy data back into form because its content exist outside it
        // TODO: do we need this here again? Already in the success callback
        if (lightboxCompId !== undefined) {
            var component = jQuery('#' + lightboxCompId).clone(true, true);

            jQuery('#' + lightboxCompId + "_dialogPlaceholder").append(component);
        }

        jQuery("#" + kradVariables.KUALI_FORM).ajaxSubmit(submitOptions);
    },

    // handles the request as standard form submit
    _submit: function () {
        // write out methodToCall as hidden
        writeHiddenToForm("methodToCall", this.methodToCall);

        // if additional data write out as hiddens
        for (key in this.additionalData) {
            writeHiddenToForm(key, this.additionalData[key]);
        }

        // start the loading indicator (will be removed on page load)
        if (!this.disableBlocking) {
            showLoading(this.loadingMessage);
        }

        var jsonViewState = getSerializedViewState();
        if (jsonViewState) {
            writeHiddenToForm("clientViewState", jsonViewState);
        }

        // check for file inputs and set encoding, this is handled for us with the ajax submits (using jqform)
        var fileInputs = jQuery('input[type=file]:enabled[value!=""]', '#kualiForm');

        var hasFileInputs = fileInputs.length > 0;
        if (hasFileInputs) {
            jQuery('#kualiForm').attr('enctype', 'multipart/form-data');
        }

        // submit
        jQuery('#kualiForm').submit();
    },

    // sets up the component or page blocking for an ajax request
    _setupBlocking: function (options) {
        // initialize element to block if necessary
        if (!this.elementToBlock && !this.disableBlocking &&
                (this.ajaxReturnType == kradVariables.RETURN_TYPE_UPDATE_COMPONENT) && this.refreshId) {
            this.elementToBlock = jQuery("#" + this.refreshId);
        }

        // create a reference to the request for ajax callbacks
        var request = this;

        // adding blocking configuration to ajax options
        var elementBlockingOptions = {
            beforeSend: function () {
                if (nonEmpty(request.elementToBlock) && (request.elementToBlock.is(":hidden, .uif-placeholder"))) {
                    var replaceElement = true;
                    request.elementToBlock.show();
                }

                if (!request.disableBlocking) {
                    showLoading(request.loadingMessage, request.elementToBlock, replaceElement);
                }
            },
            complete: function (jqXHR, textStatus) {
                // note that if you want to unblock simultaneous with showing the new retrieval
                // you must do so in the successCallback
                if (!request.disableBlocking) {
                    hideLoading(request.elementToBlock);
                }

                resetSessionTimers();
            },
            error: function () {
                if (nonEmpty(request.elementToBlock) && request.elementToBlock.hasClass("uif-placeholder")) {
                    request.elementToBlock.hide();
                }
                else if (!request.disableBlocking) {
                    hideLoading(request.elementToBlock);
                }
            },
            statusCode: {403: function (jqXHR, textStatus) {
                if (nonEmpty(request.elementToBlock) && request.elementToBlock.hasClass("uif-placeholder")) {
                    request.elementToBlock.hide();
                }
                else if (!request.disableBlocking) {
                    hideLoading(request.elementToBlock);
                }

                handleAjaxSessionTimeout(jqXHR.responseText);
            }}
        };

        jQuery.extend(options, elementBlockingOptions);
    }
}
