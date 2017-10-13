/**
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
package org.kuali.rice.krad.uif.container;

import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.parse.BeanTags;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.control.MultiValueControl;
import org.kuali.rice.krad.uif.field.InputField;
import org.kuali.rice.krad.uif.field.MessageField;
import org.kuali.rice.krad.uif.util.ScriptUtils;
import org.kuali.rice.krad.uif.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Special type of <code>Group</code> that presents a the content for a modal dialog
 *
 * <p>
 * This type of group will be hidden when the main view is displayed. It will be used as
 * content inside the LightBox widget when the modal dialog is displayed.
 * For convenience, this group contains a standard set of components for commonly used modal dialogs
 * <ul>
 * <li>a prompt to display in the lightbox</li>
 * <li>an optional explanation <code>InputField</code> for holding the user's textual response</li>
 * <li>a set of response options for the user to choose from</li>
 * </ul>
 *
 * <p>
 * The DialogGroup may also serve as a base class for more complex dialogs.
 * The default settings for this DialogGroup is to display a prompt message
 * with two buttons labeled OK and Cancel.
 * The optional explanation <code>TextAreaControl</code> is hidden by default.
 * </p>
 *
 * <p>
 * The prompt text, number of user options and their corresponding label are configurable.
 * The <code>InputField</code> for the explanation is <code>TextAreaControl</code> by default.
 * It may be configured to other types of InputFields.
 * The Component for ResponseInputField is a <code>HorizontalCheckboxGroup</code> by default.
 * JQuery styling is then used to style the checkboxes as buttons. The ResponseInputField may
 * be configured to other <code>InputField</code> types.
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTags({@BeanTag(name = "dialogGroup-bean", parent = "Uif-DialogGroup"),
        @BeanTag(name = "sensitiveData-dialogGroup-bean", parent = "Uif-SensitiveData-DialogGroup"),
        @BeanTag(name = "ok-cancel-dialogGroup-bean", parent = "Uif-OK-Cancel-DialogGroup"),
        @BeanTag(name = "yes-no-dialogGroup-bean", parent = "Uif-Yes-No-DialogGroup"),
        @BeanTag(name = "true-false-dialogGroup-bean", parent = "Uif-True-False-DialogGroup"),
        @BeanTag(name = "checkbox-dialogGroup-bean", parent = "Uif-Checkbox-DialogGroup"),
        @BeanTag(name = "radioButton-dialogGroup-bean", parent = "Uif-RadioButton-DialogGroup")})
public class DialogGroup extends Group {
    private static final long serialVersionUID = 1L;

    private String promptText;
    private List<KeyValue> availableResponses;

    private MessageField prompt;
    private InputField explanation;
    private InputField responseInputField;

    private boolean reverseButtonOrder;
    private boolean displayExplanation;

    private String onDialogResponseScript;
    private String onShowDialogScript;

    public DialogGroup() {
        super();
    }

    /**
     * The following actions are performed in this phase:
     *
     * <ul>
     * <li>Move custom dialogGroup properties prompt, explanation, and responseInputField into items collection if they
     * are not already present</li>
     * </ul>
     *
     * @see org.kuali.rice.krad.uif.component.ComponentBase#performInitialization(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object)
     */
    @Override
    public void performInitialization(View view, Object model) {
        super.performInitialization(view, model);

        // move dialogGroup custom properties into the items property.
        // where they will be rendered by group.jsp
        List<Component> newItems = new ArrayList<Component>();
        List<? extends Component> items = getItems();

        // do not add the custom properties if they are already present
        if (!(items.contains(prompt))) {
            view.assignComponentIds(prompt);
            newItems.add(prompt);
        }

        if (!(items.contains(explanation))) {
            view.assignComponentIds(explanation);
            newItems.add(explanation);
        }

        newItems.addAll(getItems());

        if (!(items.contains(responseInputField))) {
            view.assignComponentIds(responseInputField);
            newItems.add(responseInputField);
        }

        this.setItems(newItems);
    }

    /**
     * The following actions are performed in this phase:
     *
     * <p>
     * <ul>
     * <li>set the promptText in the message</li>
     * <li>sets whether to render explanation field</li>
     * <li>set the options for the checkbox control to the availableResponses KeyValue property of
     * this dialogGroup</li>
     * <li>orders response buttons</li>
     * </ul>
     * </p>
     *
     * @see org.kuali.rice.krad.uif.component.ComponentBase#performApplyModel(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object, org.kuali.rice.krad.uif.component.Component)
     */
    @Override
    public void performApplyModel(View view, Object model, Component parent) {
        super.performApplyModel(view, model, parent);

        // set the messageTest to the promptText
        prompt.setMessageText(promptText);

        // hide or show explanation
        explanation.setRender(displayExplanation);

        // add options to checkbox
        if (responseInputField.getControl() != null && responseInputField.getControl() instanceof MultiValueControl) {
            MultiValueControl multiValueControl = (MultiValueControl) responseInputField.getControl();

            if (reverseButtonOrder) {
                // reverse the button order (without changing original list)
                List<KeyValue> buttonList = new ArrayList<KeyValue>(availableResponses);
                Collections.reverse(buttonList);
                multiValueControl.setOptions(buttonList);
            } else {
                multiValueControl.setOptions(availableResponses);
            }
        }
    }

    /**
     * The following actions are performed in this phase:
     *
     * <p>
     * <ul>
     * <li>handle render via ajax configuration</li>
     * <li>adds script to the response input field for trigger the 'response' event</li>
     * </ul>
     * </p>
     *
     * @param view view instance that should be finalized for rendering
     * @param model top level object containing the data
     * @param parent parent component
     */
    @Override
    public void performFinalize(View view, Object model, Component parent) {
        super.performFinalize(view, model, parent);

        if (responseInputField != null) {
            String responseInputSelector = "#" + responseInputField.getId() + " [name='" +
                    responseInputField.getBindingInfo().getBindingPath() + "']";

            String onChangeScript = "var value = coerceValue(\"" + responseInputField.getBindingInfo().getBindingPath()
                    + "\");";
            onChangeScript += "jQuery('#" + getId() + "').trigger({type:'" + UifConstants.JsEvents.DIALOG_RESPONSE
                    + "',value:value});";

            String onChangeHandler = "jQuery(\"" + responseInputSelector + "\").change(function(e){" + onChangeScript
                    + "});";

            String onReadyScript = ScriptUtils.appendScript(getOnDocumentReadyScript(), onChangeHandler);
            setOnDocumentReadyScript(onReadyScript);
        }
    }

    /**
     * Override to add the handler script for the dialog response and show dialog events
     *
     * @see org.kuali.rice.krad.uif.component.Component#getEventHandlerScript()
     */
    @Override
    public String getEventHandlerScript() {
        String handlerScript = super.getEventHandlerScript();

        handlerScript += ScriptUtils.buildEventHandlerScript(getId(), UifConstants.JsEvents.DIALOG_RESPONSE,
                getOnDialogResponseScript());

        handlerScript += ScriptUtils.buildEventHandlerScript(getId(), UifConstants.JsEvents.SHOW_DIALOG,
                getOnShowDialogScript());

        return handlerScript;
    }

    /**
     * Returns the text to be displayed as the prompt or main message in this simple dialog
     *
     * @return String containing the prompt text
     */

    @BeanTagAttribute(name = "promptText")
    public String getPromptText() {
        return promptText;
    }

    /**
     * Sets the text String to display as the main message in this dialog
     *
     * @param promptText the String to be displayed as the main message
     */
    public void setPromptText(String promptText) {
        this.promptText = promptText;
    }

    /**
     * Retrieves the Message element for this dialog
     *
     * @return Message the text element containing the message string
     */
    @BeanTagAttribute(name = "prompt", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public MessageField getPrompt() {
        return prompt;
    }

    /**
     * Sets the prompt Message for this dialog
     *
     * @param prompt The Message element for this dialog
     */
    public void setPrompt(MessageField prompt) {
        this.prompt = prompt;
    }

    /**
     * Retrieves the explanation InputField used to gather user input text from the dialog
     *
     * <p>
     * By default, the control for this input is configured as a TextAreaControl. It may be configured for
     * other types of input fields.
     * </p>
     *
     * @return InputField component
     */
    @BeanTagAttribute(name = "explanation", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public InputField getExplanation() {
        return explanation;
    }

    /**
     * Sets the InputField for gathering user text input
     *
     * @param explanation InputField
     */
    public void setExplanation(InputField explanation) {
        this.explanation = explanation;
    }

    /**
     * determines if the explanation InputField is to be displayed in this dialog
     *
     * <p>
     * False by default.
     * </p>
     *
     * @return true if this user input is to be rendered, false if not
     */
    @BeanTagAttribute(name = "displayExplanation")
    public boolean isDisplayExplanation() {
        return displayExplanation;
    }

    /**
     * Sets whether to display the Explanation InputField on this dialog
     *
     * @param displayExplanation true if explanation control is to be displayed, false if not
     */
    public void setDisplayExplanation(boolean displayExplanation) {
        this.displayExplanation = displayExplanation;
    }

    /**
     * Gets the choices provided for user response.
     *
     * <p>
     * A List of KeyValue pairs for each of the choices provided on this dialog.
     * </p>
     *
     * @return the List of response actions to provide the user
     */
    @BeanTagAttribute(name = "availableResponses", type = BeanTagAttribute.AttributeType.LISTBEAN)
    public List<KeyValue> getAvailableResponses() {
        return availableResponses;
    }

    /**
     * Sets the list of user responses to provide on this dialog
     *
     * @param availableResponses a List of KeyValue pairs representing the user response choices
     */
    public void setAvailableResponses(List<KeyValue> availableResponses) {
        this.availableResponses = availableResponses;
    }

    /**
     * Retrieves the InputField containing the choices displayed in this dialog
     *
     * <p>
     * By default, this InputField is configured to be a HorizontalCheckboxControl.
     * Styling is then used to make the checkboxes appear to be buttons.
     * The values of the availableResponses List are used as labels for the "buttons".
     * </p>
     *
     * @return InputField component within this dialog
     */
    @BeanTagAttribute(name = "responseInputField", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public InputField getResponseInputField() {
        return responseInputField;
    }

    /**
     * Sets the type of InputField used to display the user choices in this dialog
     *
     * @param responseInputField a component used to display the response choices
     */
    public void setResponseInputField(InputField responseInputField) {
        this.responseInputField = responseInputField;
    }

    /**
     * Determines the positioning order of the choices displayed on this dialog
     *
     * <p>
     * Some page designers like the positive choice on the left and the negative choice on the right.
     * Others, prefer just the opposite. This allows the order to easily be switched.
     * </p>
     *
     * @return true if choices left to right
     *         false if choices right to left
     */
    @BeanTagAttribute(name = "reverseButtonOrder")
    public boolean isReverseButtonOrder() {
        return reverseButtonOrder;
    }

    /**
     * Sets the display order of the choices displayed on this dialog
     *
     * <p>
     * By default, the choices are displayed left to right
     * </p>
     *
     * @param reverseButtonOrder true if buttons displayed left to right, false if right to left
     */
    public void setReverseButtonOrder(boolean reverseButtonOrder) {
        this.reverseButtonOrder = reverseButtonOrder;
    }

    /**
     * Script that will be invoked when the response event is thrown
     *
     * <p>
     * The dialog group will throw a custom event type 'dialogresponse.uif' when a change occurs for the response
     * input field (for example one of the response options is selected). Script given here will bind to that
     * event as a handler
     * </p>
     *
     * @return javascript that will execute for the response event
     */
    @BeanTagAttribute(name = "onDialogResponseScript")
    public String getOnDialogResponseScript() {
        return onDialogResponseScript;
    }

    /**
     * Setter for the 'dialogresponse.uif' event handler code
     *
     * @param onDialogResponseScript
     */
    public void setOnDialogResponseScript(String onDialogResponseScript) {
        this.onDialogResponseScript = onDialogResponseScript;
    }

    /**
     * Script that will get invoked when the dialog group is shown
     *
     * <p>
     * Initially a dialog group will either be hidden in the DOM or not present at all (if retrieved via Ajax).
     * When the dialog is triggered and shown, the 'showdialog.uif' event will be thrown and this script will
     * be executed
     * </p>
     *
     * @return JavaScript code to execute when the dialog is shown
     */
    @BeanTagAttribute(name = "onShowDialogScript")
    public String getOnShowDialogScript() {
        return onShowDialogScript;
    }

    /**
     * Setter for the 'showdialog.uif' event handler code
     *
     * @param onShowDialogScript
     */
    public void setOnShowDialogScript(String onShowDialogScript) {
        this.onShowDialogScript = onShowDialogScript;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        DialogGroup dialogGroupCopy = (DialogGroup) component;

        if (this.availableResponses != null) {
            dialogGroupCopy.setAvailableResponses(new ArrayList<KeyValue>(this.availableResponses));
        }

        dialogGroupCopy.setDisplayExplanation(this.displayExplanation);
        dialogGroupCopy.setOnDialogResponseScript(this.onDialogResponseScript);
        dialogGroupCopy.setOnShowDialogScript(this.onShowDialogScript);

        if (this.prompt != null) {
            dialogGroupCopy.setPrompt((MessageField)this.prompt.copy());
        }

        dialogGroupCopy.setPromptText(this.promptText);
        dialogGroupCopy.setReverseButtonOrder(this.reverseButtonOrder);

        if (this.explanation != null) {
            dialogGroupCopy.setExplanation((InputField) this.explanation.copy());
        }

        if (this.responseInputField != null) {
            dialogGroupCopy.setResponseInputField((InputField) this.responseInputField.copy());
        }
    }
}
