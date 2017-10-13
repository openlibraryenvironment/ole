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
 * The DirtyFormState constructor sets up the beforeUnload event that checks for dirtyness when the user tries to
 * leave the page through a browser based method (back, url, refresh, close, anchor not in krad scope)
 *
 * @constructor
 */
function DirtyFormState() {
    var dirtyFormStateObject = this;
    // make sure form doesn't have any unsaved data when user clicks on any other portal links,
    // closes browser or presses fwd/back browser button
    jQuery(window).bind('beforeunload', function (event) {
        // methodToCall check is needed to skip form posts
        var methodToCall = jQuery("[name='methodToCall']").val();
        if (!methodToCall) {
            var dirty = dirtyFormStateObject.checkDirty(event, false);

            // prompt does not come through in checkDirty since we are unloaded, so we
            // need to return the question
            if (dirty) {
                return getMessage(kradVariables.MESSAGE_KEY_DIRTY_FIELDS);
            }
        }
    });
}

/**
 * DirtyFormState keeps track of dirty state information on the page by detecting field changes and blocking user
 * navigation with an alert when the field is considered dirty (changed).  The object also provides the necessary
 * methods for accessing and changing this state.
 *
 * @type {{dirtyFieldCount: number, dirtyFormInput: *, skipDirtyChecks: boolean, isDirty: Function, setDirty: Function, reset: Function, resetDirtyFieldCount: Function, incrementDirtyFieldCount: Function, decrementDirtyFieldCount: Function, checkDirty: Function, dirtyHandlerSetup: Function, isControlDirty: Function}}
 */
DirtyFormState.prototype = {
    /**
     * The dirtyFieldCount which represents the total fields dirty on the form
     */
    dirtyFieldCount: 0,
    /**
     * jQuery selection of dirtyForm field
     */
    dirtyFormInput: jQuery("input[name='dirtyForm']"),
    /**
     * Set this to true to prevent dirty blocking until a View reload or until it is set back to false
     */
    skipDirtyChecks: false,
    /**
     * Returns true if the current form is considered dirty, false otherwise.  The form is diry if the dirtyForm field
     * is set to "true" or the dirtyFieldCount > 0.
     *
     * @return {boolean} true if the form is dirty, false otherwise
     */
    isDirty: function () {
        return this.dirtyFormInput.val() == "true" || this.dirtyFieldCount > 0;
    },
    /**
     * Set the dirty flag (normally set by the server) - this is the dirtyForm field on the form
     *
     * @param isDirty true if setting to dirty, false otherwise
     */
    setDirty: function (isDirty) {
        if (isDirty) {
            this.dirtyFormInput.val("true");
        }
        else {
            this.dirtyFormInput.val("false");
        }
    },
    /**
     * Reset both the dirty flag (normally set by the server) and the dirty field count
     */
    reset: function () {
        this.resetDirtyFieldCount();
        this.setDirty(false);
    },
    /**
     * Reset the dirty field count to 0 (amount of fields considered dirty on the page)
     */
    resetDirtyFieldCount: function () {
        this.dirtyFieldCount = 0;
    },
    /**
     * Increment the dirty field count (amount of fields considered dirty on the page)
     */
    incrementDirtyFieldCount: function () {
        this.dirtyFieldCount++;
    },
    /**
     * Decrement the dirty field count (amount of fields considered dirty on the page)
     */
    decrementDirtyFieldCount: function () {
        this.dirtyFieldCount--;
    },
    /**
     * Validate dirty fields on the form
     *
     * <p>Whenever the user clicks on the action field which navigates away from the page,
     * form dirtyness is checked. It checks for any input elements which has "dirty" class. If present,
     * it pops a message to the user to confirm whether they want to stay on the page or want to navigate.
     * </p>
     *
     * @param event the event which triggered the action
     * @param showAlert (optional) if true show the alert now.  This flag is used to not show the alert for unload because
     *  most browsers will stop this from occuring anyways and it eliminates duplicate alert dialogs to click through.
     * @returns true if the form has dirty fields, false if not
     */
    checkDirty: function (event, showAlert) {
        var validateDirty = jQuery("#validateDirty").val();

        if (!this.skipDirtyChecks && validateDirty == "true" && this.isDirty()) {

            //immediate return if skipping alert
            if (showAlert != undefined && !showAlert) {
                return true;
            }

            var dirtyMessage = getMessage(kradVariables.MESSAGE_KEY_DIRTY_FIELDS);

            var answer = confirm(dirtyMessage);

            if (answer == false) {
                event.preventDefault();
                event.stopImmediatePropagation();

                // change the current nav button class to 'current' if user doesn't wants to leave the page
                var ul = jQuery("#" + event.target.id).closest("ul");
                if (ul.length > 0) {
                    var pageId = jQuery("[name='view.currentPageId']").val();
                    if (ul.hasClass(kradVariables.TAB_MENU_CLASS)) {
                        jQuery("#" + ul.attr("id")).selectTab({selectPage: pageId});
                    }
                    else {
                        jQuery("#" + ul.attr("id")).selectMenuItem({selectPage: pageId});
                    }
                }

                return true;
            }
        }

        return false;

    },
    /**
     * Sets up the global dirty field handler which will mark dirtyness and keep track of a dirtyFieldCount on
     * InputField controls
     */
    dirtyHandlerSetup: function () {
        var dirtyFormStateObject = this;

        jQuery(document).on("change", "div[data-role='InputField'] input, div[data-role='InputField'] select, "
                + "div[data-role='InputField'] textarea", function (e) {
            var fieldDirty = true;

            //check if original value
            if (e && e.target) {
                fieldDirty = dirtyFormStateObject.isControlDirty(e.target);
            }

            //does it already have a dirty class?
            var hadDirtyClass = jQuery(this).hasClass(kradVariables.DIRTY_CLASS);

            //if already has dirty class and not dirty remove and decrement dirty count, or opposite for other scenario
            if (hadDirtyClass && !fieldDirty) {
                jQuery(this).removeClass(kradVariables.DIRTY_CLASS);
                dirtyFormStateObject.decrementDirtyFieldCount();
            }
            else if (!hadDirtyClass && fieldDirty) {
                jQuery(this).addClass(kradVariables.DIRTY_CLASS);
                dirtyFormStateObject.incrementDirtyFieldCount();
            }
        });
    },
    /**
     * Determines if the control passed in is considered dirty (does not match its original value)
     *
     * @param control the control to check for dirtyness
     * @return {boolean} true if dirty (assumed true if checks cannot prove otherwise), false if not dirty
     */
    isControlDirty: function (control) {
        //assume dirty
        var fieldDirty = true;

        //basic input
        if (control.defaultValue != undefined && control.value == control.defaultValue) {
            fieldDirty = false;
        }
        //checkbox or radio
        else if ((control.type == "checkbox" || control.type == "radio")
                && control.defaultChecked != undefined && control.checked == control.defaultChecked) {
            fieldDirty = false;
        }
        //select
        else if (control.options != undefined) {
            fieldDirty = false;
            var hasDefaultSelected = false;
            var options = jQuery(control).find("option");
            jQuery(options).each(function () {
                //if these differ the field is dirty
                if (this.defaultSelected != this.selected) {
                    fieldDirty = true;
                }

                //check to see if any option was a default selection
                hasDefaultSelected = this.defaultSelected || hasDefaultSelected;
            });

            //special case when no default value was selected when the control was rendered
            if (!hasDefaultSelected &&
                    ((control.multiple && control.selectedIndex == -1) ||
                            (!control.multiple && control.selectedIndex == 0))) {
                fieldDirty = false;
            }

        }

        return fieldDirty;

    }
}