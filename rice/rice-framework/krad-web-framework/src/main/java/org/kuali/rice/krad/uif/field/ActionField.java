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
package org.kuali.rice.krad.uif.field;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.parse.BeanTags;
import org.kuali.rice.krad.datadictionary.validator.ErrorReport;
import org.kuali.rice.krad.datadictionary.validator.ValidationTrace;
import org.kuali.rice.krad.datadictionary.validator.Validator;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.component.ComponentSecurity;
import org.kuali.rice.krad.uif.element.Action;
import org.kuali.rice.krad.uif.element.Image;
import org.kuali.rice.krad.uif.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Field that encloses an @{link org.kuali.rice.krad.uif.element.Action} element
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTags({@BeanTag(name = "actionField-bean", parent = "Uif-ActionField"),
        @BeanTag(name = "actionLinkField-bean", parent = "Uif-ActionLinkField")})
public class ActionField extends FieldBase {
    private static final long serialVersionUID = -8495752159848603102L;

    private Action action;

    public ActionField() {
        action = new Action();
    }

    /**
     * PerformFinalize override - calls super, corrects the field's Label for attribute to point to this field's
     * content
     *
     * @param view the view
     * @param model the model
     * @param parent the parent component
     */
    @Override
    public void performFinalize(View view, Object model, Component parent) {
        super.performFinalize(view, model, parent);

        //determine what id to use for the for attribute of the label, if present
        if (this.getFieldLabel() != null && this.getAction() != null && StringUtils.isNotBlank(
                this.getAction().getId())) {
            this.getFieldLabel().setLabelForComponentId(this.getAction().getId());
        }
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#getComponentsForLifecycle()
     */
    @Override
    public List<Component> getComponentsForLifecycle() {
        List<Component> components = super.getComponentsForLifecycle();

        components.add(action);

        return components;
    }

    /**
     * Nested action component
     *
     * @return Action instance
     */
    @BeanTagAttribute(name = "action", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Action getAction() {
        return action;
    }

    /**
     * Setter for the nested action component
     *
     * @param action
     */
    public void setAction(Action action) {
        this.action = action;
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#getMethodToCall()
     */
    @BeanTagAttribute(name = "methodToCall")
    public String getMethodToCall() {
        return action.getMethodToCall();
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#setMethodToCall(java.lang.String)
     */
    public void setMethodToCall(String methodToCall) {
        action.setMethodToCall(methodToCall);
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#getActionLabel()
     */
    @BeanTagAttribute(name = "actionLabel")
    public String getActionLabel() {
        return action.getActionLabel();
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#setActionLabel(java.lang.String)
     */
    public void setActionLabel(String actionLabel) {
        action.setActionLabel(actionLabel);
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#getActionImage()
     */
    @BeanTagAttribute(name = "actionImage", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Image getActionImage() {
        return action.getActionImage();
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#setActionImage(org.kuali.rice.krad.uif.element.Image)
     */
    public void setActionImage(Image actionImage) {
        action.setActionImage(actionImage);
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#getNavigateToPageId()
     */
    @BeanTagAttribute(name = "navigateToPageId")
    public String getNavigateToPageId() {
        return action.getNavigateToPageId();
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#setNavigateToPageId(java.lang.String)
     */
    public void setNavigateToPageId(String navigateToPageId) {
        action.setNavigateToPageId(navigateToPageId);
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#getActionEvent()
     */
    @BeanTagAttribute(name = "actionEvent")
    public String getActionEvent() {
        return action.getActionEvent();
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#setActionEvent(java.lang.String)
     */
    public void setActionEvent(String actionEvent) {
        action.setActionEvent(actionEvent);
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#getActionParameters()
     */
    @BeanTagAttribute(name = "actionParameters", type = BeanTagAttribute.AttributeType.MAPVALUE)
    public Map<String, String> getActionParameters() {
        return action.getActionParameters();
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#setActionParameters(java.util.Map<java.lang.String,java.lang.String>)
     */
    public void setActionParameters(Map<String, String> actionParameters) {
        action.setActionParameters(actionParameters);
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#getAdditionalSubmitData()
     */
    @BeanTagAttribute(name = "additionalSubmitData", type = BeanTagAttribute.AttributeType.MAPVALUE)
    public Map<String, String> getAdditionalSubmitData() {
        return action.getAdditionalSubmitData();
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#setAdditionalSubmitData(java.util.Map<java.lang.String,java.lang.String>)
     */
    public void setAdditionalSubmitData(Map<String, String> additionalSubmitData) {
        action.setAdditionalSubmitData(additionalSubmitData);
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#addActionParameter(java.lang.String, java.lang.String)
     */
    public void addActionParameter(String parameterName, String parameterValue) {
        action.addActionParameter(parameterName, parameterValue);
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#getActionParameter(java.lang.String)
     */
    public String getActionParameter(String parameterName) {
        return action.getActionParameter(parameterName);
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#getJumpToIdAfterSubmit()
     */
    @BeanTagAttribute(name = "jumpToIdAfterSubmit")
    public String getJumpToIdAfterSubmit() {
        return action.getJumpToIdAfterSubmit();
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#setJumpToIdAfterSubmit(java.lang.String)
     */

    public void setJumpToIdAfterSubmit(String jumpToIdAfterSubmit) {
        action.setJumpToIdAfterSubmit(jumpToIdAfterSubmit);
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#getJumpToNameAfterSubmit()
     */
    @BeanTagAttribute(name = "jumpToNameAfterSubmit")
    public String getJumpToNameAfterSubmit() {
        return action.getJumpToNameAfterSubmit();
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#setJumpToNameAfterSubmit(java.lang.String)
     */
    public void setJumpToNameAfterSubmit(String jumpToNameAfterSubmit) {
        action.setJumpToNameAfterSubmit(jumpToNameAfterSubmit);
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#getFocusOnIdAfterSubmit()
     */
    @BeanTagAttribute(name = "focusOnIdAfterSubmit")
    public String getFocusOnIdAfterSubmit() {
        return action.getFocusOnIdAfterSubmit();
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#setFocusOnIdAfterSubmit(java.lang.String)
     */
    public void setFocusOnIdAfterSubmit(String focusOnAfterSubmit) {
        action.setFocusOnIdAfterSubmit(focusOnAfterSubmit);
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#isPerformClientSideValidation()
     */
    @BeanTagAttribute(name = "performClientSideValidation")
    public boolean isPerformClientSideValidation() {
        return action.isPerformClientSideValidation();
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#setPerformClientSideValidation(boolean)
     */
    public void setPerformClientSideValidation(boolean clientSideValidate) {
        action.setPerformClientSideValidation(clientSideValidate);
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#getActionScript()
     */
    @BeanTagAttribute(name = "actionScript")
    public String getActionScript() {
        return action.getActionScript();
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#setActionScript(java.lang.String)
     */
    public void setActionScript(String actionScript) {
        action.setActionScript(actionScript);
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#isPerformDirtyValidation()
     */
    @BeanTagAttribute(name = "performDirtyValidation")
    public boolean isPerformDirtyValidation() {
        return action.isPerformDirtyValidation();
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#setPerformDirtyValidation(boolean)
     */
    public void setPerformDirtyValidation(boolean blockValidateDirty) {
        action.setPerformDirtyValidation(blockValidateDirty);
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#isDisabled()
     */
    @BeanTagAttribute(name = "disabled")
    public boolean isDisabled() {
        return action.isDisabled();
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#setDisabled(boolean)
     */
    public void setDisabled(boolean disabled) {
        action.setDisabled(disabled);
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#getDisabledReason()
     */
    @BeanTagAttribute(name = "disabledReason")
    public String getDisabledReason() {
        return action.getDisabledReason();
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#setDisabledReason(java.lang.String)
     */
    public void setDisabledReason(String disabledReason) {
        action.setDisabledReason(disabledReason);
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#getActionImagePlacement()
     */
    @BeanTagAttribute(name = "actionImagePlacement")
    public String getActionImagePlacement() {
        return action.getActionImagePlacement();
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#setActionImagePlacement(java.lang.String)
     */
    public void setActionImagePlacement(String actionImageLocation) {
        action.setActionImagePlacement(actionImageLocation);
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#getPreSubmitCall()
     */
    @BeanTagAttribute(name = "preSubmitCall")
    public String getPreSubmitCall() {
        return action.getPreSubmitCall();
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#setPreSubmitCall(java.lang.String)
     */
    public void setPreSubmitCall(String preSubmitCall) {
        action.setPreSubmitCall(preSubmitCall);
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#isAjaxSubmit()
     */
    @BeanTagAttribute(name = "ajaxSubmit")
    public boolean isAjaxSubmit() {
        return action.isAjaxSubmit();
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#setAjaxSubmit(boolean)
     */
    public void setAjaxSubmit(boolean ajaxSubmit) {
        action.setAjaxSubmit(ajaxSubmit);
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#getSuccessCallback()
     */
    @BeanTagAttribute(name = "successCallback")
    public String getSuccessCallback() {
        return action.getSuccessCallback();
    }

    /**
     * @param successCallback
     * @see org.kuali.rice.krad.uif.element.Action#setSuccessCallback(java.lang.String)
     */
    public void setSuccessCallback(String successCallback) {
        action.setSuccessCallback(successCallback);
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#getErrorCallback()
     */
    @BeanTagAttribute(name = "errorCallback")
    public String getErrorCallback() {
        return action.getErrorCallback();
    }

    /**
     * @param errorCallback
     * @see org.kuali.rice.krad.uif.element.Action#setErrorCallback(java.lang.String)
     */
    public void setErrorCallback(String errorCallback) {
        action.setErrorCallback(errorCallback);
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#getRefreshId()
     */
    @BeanTagAttribute(name = "refreshId")
    public String getRefreshId() {
        return action.getRefreshId();
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#setRefreshId(java.lang.String)
     */
    public void setRefreshId(String refreshId) {
        action.setRefreshId(refreshId);
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#isDisableBlocking()
     */
    @BeanTagAttribute(name = "disableBlocking")
    public boolean isDisableBlocking() {
        return action.isDisableBlocking();
    }

    /**
     * @see org.kuali.rice.krad.uif.element.Action#setDisableBlocking(boolean)
     */
    public void setDisableBlocking(boolean disableBlocking) {
        action.setDisableBlocking(disableBlocking);
    }

    /**
     * @see org.kuali.rice.krad.uif.component.Component#completeValidation
     */
    @Override
    public void completeValidation(ValidationTrace tracer) {
        ArrayList<ErrorReport> reports = new ArrayList<ErrorReport>();
        tracer.addBean(this);

        // Checks that the action is set
        if (getAction() == null) {
            if (Validator.checkExpressions(this, "action")) {
                String currentValues[] = {"action =" + getAction()};
                tracer.createWarning("Action should not be null", currentValues);
            }
        }

        // checks that the label is set
        if (getLabel() == null) {
            if (Validator.checkExpressions(this, "label")) {
                String currentValues[] = {"label =" + getLabel(), "action =" + getAction()};
                tracer.createWarning("Label is null, action should be used instead", currentValues);
            }
        }

        super.completeValidation(tracer.getCopy());
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        ActionField actionFieldCopy = (ActionField) component;

        if (this.action != null) {
            actionFieldCopy.setAction((Action)this.action.copy());
        }
    }
}
