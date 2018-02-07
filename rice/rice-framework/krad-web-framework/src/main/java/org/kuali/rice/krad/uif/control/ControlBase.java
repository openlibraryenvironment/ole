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
package org.kuali.rice.krad.uif.control;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.validator.ValidationTrace;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.element.ContentElementBase;
import org.kuali.rice.krad.uif.view.ExpressionEvaluator;
import org.kuali.rice.krad.uif.util.ExpressionUtils;
import org.kuali.rice.krad.uif.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all <code>Control</code> implementations
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @see org.kuali.rice.krad.uif.control.Control
 */
@BeanTag(name = "controlBase-bean", parent = "Uif-ControlBase")
public abstract class ControlBase extends ContentElementBase implements Control {
    private static final long serialVersionUID = -7898244978136312663L;

    private int tabIndex;

    private boolean disabled;
    private String disabledExpression;
    private String disabledReason;
    private boolean evaluateDisabledOnKeyUp;

    private String disabledConditionJs;
    private List<String> disabledConditionControlNames;

    private List<String> disabledWhenChangedPropertyNames;
    private List<String> enabledWhenChangedPropertyNames;

    public ControlBase() {
        super();

        disabled = false;
        disabledWhenChangedPropertyNames = new ArrayList<String>();
        enabledWhenChangedPropertyNames = new ArrayList<String>();
    }

    /**
     * Sets the disabledExpression, if any, evaluates it and sets the disabled property
     *
     * @param view view instance to which the component belongs
     * @param model top level object containing the data (could be the form or a
     * top level business object, dto)
     * @param parent
     */
    public void performApplyModel(View view, Object model, Component parent) {
        super.performApplyModel(view, model, parent);

        disabledExpression = this.getPropertyExpression("disabled");
        if(disabledExpression != null){
            ExpressionEvaluator expressionEvaluator =
                    view.getViewHelperService().getExpressionEvaluator();

            disabledExpression = expressionEvaluator.replaceBindingPrefixes(view, this,
                    disabledExpression);
            disabled = (Boolean) expressionEvaluator.evaluateExpression(this.getContext(), disabledExpression);
        }
    }

    /**
     * Parses the disabled expressions, if any, to equivalent javascript and evaluates the disable/enable when
     * changed property names.
     *
     * @param view view instance that should be finalized for rendering
     * @param model top level object containing the data
     * @param parent parent component
     */
    public void performFinalize(View view, Object model, Component parent) {
        super.performApplyModel(view, model, parent);

        ExpressionEvaluator expressionEvaluator =
                view.getViewHelperService().getExpressionEvaluator();

        if (StringUtils.isNotEmpty(disabledExpression) && !disabledExpression.equalsIgnoreCase("true")
                && !disabledExpression.equalsIgnoreCase("false")) {
            disabledConditionControlNames = new ArrayList<String>();
            disabledConditionJs = ExpressionUtils.parseExpression(disabledExpression,
                    disabledConditionControlNames);
        }

        List<String> adjustedDisablePropertyNames = new ArrayList<String>();
        for (String propertyName : disabledWhenChangedPropertyNames) {
            adjustedDisablePropertyNames.add(expressionEvaluator.replaceBindingPrefixes(view, this,
                    propertyName));
        }
        disabledWhenChangedPropertyNames = adjustedDisablePropertyNames;

        List<String> adjustedEnablePropertyNames = new ArrayList<String>();
        for (String propertyName : enabledWhenChangedPropertyNames) {
            adjustedEnablePropertyNames.add(expressionEvaluator.replaceBindingPrefixes(view, this,
                    propertyName));
        }
        enabledWhenChangedPropertyNames = adjustedEnablePropertyNames;

        // add control role
        this.addDataAttribute(UifConstants.DataAttributes.ROLE, UifConstants.RoleTypes.CONTROL);
    }

    /**
     * @see org.kuali.rice.krad.uif.component.Component#getComponentTypeName()
     */
    @Override
    public final String getComponentTypeName() {
        return "control";
    }

    /**
     * @see org.kuali.rice.krad.uif.control.Control#getTabIndex()
     */
    @BeanTagAttribute(name="tabIndex")
    public int getTabIndex() {
        return this.tabIndex;
    }

    /**
     * @see org.kuali.rice.krad.uif.control.Control#setTabIndex(int)
     */
    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    /**
     * @see org.kuali.rice.krad.uif.control.Control#isDisabled()
     */
    @BeanTagAttribute(name="disabled")
    public boolean isDisabled() {
        return disabled;
    }

    /**
     * @see org.kuali.rice.krad.uif.control.Control#setDisabled(boolean)
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * @see org.kuali.rice.krad.uif.control.Control#getDisabledReason()
     */
    @BeanTagAttribute(name="disabledReason")
    public String getDisabledReason() {
        return disabledReason;
    }

    /**
     * @see org.kuali.rice.krad.uif.control.Control#setDisabledReason(java.lang.String)
     */
    public void setDisabledReason(String disabledReason) {
        this.disabledReason = disabledReason;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.Component#completeValidation
     */
    @Override
    public void completeValidation(ValidationTrace tracer){
        tracer.addBean(this);

        super.completeValidation(tracer.getCopy());
    }


    /**
     * Evaluate the disable condition on controls which disable it on each key up event
     *
     * @return true if evaluate on key up, false otherwise
     */
    @BeanTagAttribute(name="evaluateDisabledOnKeyUp")
    public boolean isEvaluateDisabledOnKeyUp() {
        return evaluateDisabledOnKeyUp;
    }

    /**
     * Set evaluateDisableOnKeyUp
     *
     * @param evaluateDisabledOnKeyUp
     */
    public void setEvaluateDisabledOnKeyUp(boolean evaluateDisabledOnKeyUp) {
        this.evaluateDisabledOnKeyUp = evaluateDisabledOnKeyUp;
    }

    /**
     * Get the disable condition js derived from the springEL, cannot be set.
     *
     * @return the disableConditionJs javascript to be evaluated
     */
    public String getDisabledConditionJs() {
        return disabledConditionJs;
    }

    /**
     * Control names to add handlers to for disable functionality, cannot be set
     *
     * @return control names to add handlers to for disable
     */
    public List<String> getDisabledConditionControlNames() {
        return disabledConditionControlNames;
    }

    /**
     * Gets the property names of fields that when changed, will disable this component
     *
     * @return the property names to monitor for change to disable this component
     */
    @BeanTagAttribute(name="disabledWhenChangedPropertyNames",type= BeanTagAttribute.AttributeType.LISTVALUE)
    public List<String> getDisabledWhenChangedPropertyNames() {
        return disabledWhenChangedPropertyNames;
    }

    /**
     * Sets the property names of fields that when changed, will disable this component
     *
     * @param disabledWhenChangedPropertyNames
     */
    public void setDisabledWhenChangedPropertyNames(List<String> disabledWhenChangedPropertyNames) {
        this.disabledWhenChangedPropertyNames = disabledWhenChangedPropertyNames;
    }

    /**
     * Gets the property names of fields that when changed, will enable this component
     *
     * @return the property names to monitor for change to enable this component
     */
    @BeanTagAttribute(name="ensabledConditionControlNames",type= BeanTagAttribute.AttributeType.LISTVALUE)
    public List<String> getEnabledWhenChangedPropertyNames() {
        return enabledWhenChangedPropertyNames;
    }

    /**
     * Sets the property names of fields that when changed, will enable this component
     *
     * @param enabledWhenChangedPropertyNames
     */
    public void setEnabledWhenChangedPropertyNames(List<String> enabledWhenChangedPropertyNames) {
        this.enabledWhenChangedPropertyNames = enabledWhenChangedPropertyNames;
    }

    /**
     * Sets the disabled expression
     *
     * @param disabledExpression
     */
    protected void setDisabledExpression(String disabledExpression) {
        this.disabledExpression = disabledExpression;
    }

    /**
     * Sets the disabled condition javascript
     *
     * @param disabledConditionJs
     */
    protected void setDisabledConditionJs(String disabledConditionJs) {
        this.disabledConditionJs = disabledConditionJs;
    }

    /**
     * Sets the disabled condition control names
     *
     * @param disabledConditionControlNames
     */
    protected void setDisabledConditionControlNames(List<String> disabledConditionControlNames) {
        this.disabledConditionControlNames = disabledConditionControlNames;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        ControlBase controlBaseCopy = (ControlBase) component;
        controlBaseCopy.setTabIndex(this.tabIndex);
        controlBaseCopy.setDisabled(this.disabled);
        controlBaseCopy.setDisabledExpression(this.disabledExpression);
        controlBaseCopy.setDisabledReason(this.disabledReason);
        controlBaseCopy.setEvaluateDisabledOnKeyUp(this.evaluateDisabledOnKeyUp);
        controlBaseCopy.setDisabledConditionJs(this.disabledConditionJs);

        if (disabledConditionControlNames != null) {
            controlBaseCopy.setDisabledConditionControlNames(new ArrayList<String>(this.disabledConditionControlNames));
        }

        if (disabledWhenChangedPropertyNames != null) {
            controlBaseCopy.setDisabledWhenChangedPropertyNames(new ArrayList<String>(this.disabledWhenChangedPropertyNames));
        }

        if (enabledWhenChangedPropertyNames != null) {
            controlBaseCopy.setEnabledWhenChangedPropertyNames(new ArrayList<String>(this.enabledWhenChangedPropertyNames));
        }
    }
}
