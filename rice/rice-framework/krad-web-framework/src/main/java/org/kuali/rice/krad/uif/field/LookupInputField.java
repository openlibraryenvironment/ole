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
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.krad.datadictionary.AttributeDefinition;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.control.CheckboxControl;
import org.kuali.rice.krad.uif.control.Control;
import org.kuali.rice.krad.uif.control.FilterableLookupCriteriaControl;
import org.kuali.rice.krad.uif.control.MultiValueControl;
import org.kuali.rice.krad.uif.control.RadioGroupControl;
import org.kuali.rice.krad.uif.control.TextAreaControl;
import org.kuali.rice.krad.uif.element.Message;
import org.kuali.rice.krad.uif.util.ComponentFactory;
import org.kuali.rice.krad.uif.util.ComponentUtils;
import org.kuali.rice.krad.uif.util.KeyMessage;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADPropertyConstants;

import java.util.Map;

/**
 * Custom <code>InputField</code> for search fields within a lookup view
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "lookupCriteriaInputField-bean", parent = "Uif-LookupCriteriaInputField")
public class LookupInputField extends InputField {
    private static final long serialVersionUID = -8294275596836322699L;

    public static final String CHECKBOX_CONVERTED_RADIO_CONTROL = "Uif-CheckboxConvertedRadioControl";

    private boolean disableWildcardsAndOperators;
    private boolean addControlSelectAllOption;
    private boolean triggerOnChange;
    private boolean ranged;

    public LookupInputField() {
        super();

        disableWildcardsAndOperators = false;
        addControlSelectAllOption = false;
        setTriggerOnChange(false);
    }

    /**
     * The following actions are performed:
     *
     * <ul>
     * <li>Add all option if enabled and control is multi-value</li>
     * </ul>
     *
     * @see org.kuali.rice.krad.uif.component.ComponentBase#performFinalize(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object, org.kuali.rice.krad.uif.component.Component)
     */
    @Override
    public void performFinalize(View view, Object model, Component parent) {
        super.performFinalize(view, model, parent);

        // if enabled add option to select all values
        if (addControlSelectAllOption && (getControl() != null) && getControl() instanceof MultiValueControl) {
            String allOptionText = KRADServiceLocatorWeb.getMessageService().getMessageText(
                    UifConstants.MessageKeys.OPTION_ALL);

            MultiValueControl multiValueControl = (MultiValueControl) getControl();
            if (multiValueControl.getOptions() != null) {
                multiValueControl.getOptions().add(0, new ConcreteKeyValue("", allOptionText));
            }

            if (multiValueControl.getRichOptions() != null) {
                Message message = ComponentFactory.getMessage();

                view.assignComponentIds(message);
                message.setMessageText(allOptionText);
                message.setGenerateSpan(false);

                multiValueControl.getRichOptions().add(0, new KeyMessage("", allOptionText, message));
            }
        }
    }

    /**
     * Override of InputField copy to setup properties necessary to make the field usable for inputting
     * search criteria
     *
     * @param attributeDefinition AttributeDefinition instance the property values should be copied from
     * @see DataField#copyFromAttributeDefinition(org.kuali.rice.krad.uif.view.View,
     *      org.kuali.rice.krad.datadictionary.AttributeDefinition)
     */
    @Override
    public void copyFromAttributeDefinition(View view, AttributeDefinition attributeDefinition) {
        // label
        if (StringUtils.isEmpty(getLabel())) {
            setLabel(attributeDefinition.getLabel());
        }

        // short label
        if (StringUtils.isEmpty(getShortLabel())) {
            setShortLabel(attributeDefinition.getShortLabel());
        }

        // security
        if (getDataFieldSecurity().getAttributeSecurity() == null) {
            getDataFieldSecurity().setAttributeSecurity(attributeDefinition.getAttributeSecurity());
        }

        // options
        if (getOptionsFinder() == null) {
            setOptionsFinder(attributeDefinition.getOptionsFinder());
        }

        // TODO: what about formatter?

        // use control from dictionary if not specified and convert for searching
        if (getControl() == null) {
            Control control = convertControlToLookupControl(attributeDefinition);
            view.assignComponentIds(control);

            setControl(control);
        }

        // overwrite maxLength to allow for wildcards and ranges
        setMaxLength(100);

        // set default value for active field to true
        if (StringUtils.isEmpty(getDefaultValue())) {
            if ((StringUtils.equals(getPropertyName(), KRADPropertyConstants.ACTIVE))) {
                setDefaultValue(KRADConstants.YES_INDICATOR_VALUE);
            }
        }

        /*
           * TODO delyea: FieldUtils.createAndPopulateFieldsForLookup used to allow for a set of property names to be passed in via the URL
           * parameters of the lookup url to set fields as 'read only'
           */

    }

    /**
     * If control definition is defined on the given attribute definition, converts to an appropriate control for
     * searching (if necessary) and returns a copy for setting on the field
     *
     * @param attributeDefinition attribute definition instance to retrieve control from
     * @return Control instance or null if not found
     */
    protected static Control convertControlToLookupControl(AttributeDefinition attributeDefinition) {
        if (attributeDefinition.getControlField() == null) {
            return null;
        }

        Control newControl = null;

        // convert checkbox to radio with yes/no/both options
        if (CheckboxControl.class.isAssignableFrom(attributeDefinition.getControlField().getClass())) {
            newControl = getCheckboxConvertedRadioControl();
        }
        // text areas get converted to simple text inputs
        else if (TextAreaControl.class.isAssignableFrom(attributeDefinition.getControlField().getClass())) {
            newControl = ComponentFactory.getTextControl();
        } else {
            newControl = ComponentUtils.copy(attributeDefinition.getControlField(), "");
        }

        return newControl;
    }

    /**
     * @return the treatWildcardsAndOperatorsAsLiteral
     */
    @BeanTagAttribute(name = "disableWildcardsAndOperators")
    public boolean isDisableWildcardsAndOperators() {
        return this.disableWildcardsAndOperators;
    }

    /**
     * @param disableWildcardsAndOperators the treatWildcardsAndOperatorsAsLiteral to set
     */
    public void setDisableWildcardsAndOperators(boolean disableWildcardsAndOperators) {
        this.disableWildcardsAndOperators = disableWildcardsAndOperators;
    }

    /**
     * Indicates whether the option for all values (blank key, 'All' label) should be added to the lookup
     * field, note this is only supported for {@link org.kuali.rice.krad.uif.control.MultiValueControl} instance
     *
     * @return boolean true if all option should be added, false if not
     */
    @BeanTagAttribute(name = "addControlSelectAllOption")
    public boolean isAddControlSelectAllOption() {
        return addControlSelectAllOption;
    }

    /**
     * Setter for the add all option indicator
     *
     * @param addControlSelectAllOption
     */
    public void setAddControlSelectAllOption(boolean addControlSelectAllOption) {
        this.addControlSelectAllOption = addControlSelectAllOption;
    }

    /**
     * Indicates that the search must execute on changing of a value in the lookup input field
     *
     * @return boolean
     */
    public boolean isTriggerOnChange() {
        return triggerOnChange;
    }

    /**
     * Setter for the trigger search on change flag
     *
     * @param triggerOnChange
     */
    public void setTriggerOnChange(boolean triggerOnChange) {
        this.triggerOnChange = triggerOnChange;
    }

    /**
     * Indicates that a field must be rendered as a from and to value
     *
     * @return
     */
    public boolean isRanged() {
        return ranged;
    }

    /**
     * Setter for ranged flag to indicate this is a range field
     *
     * @param ranged
     */
    public void setRanged(boolean ranged) {
        this.ranged = ranged;
    }

    /**
     * Remove any search criteria that are not part of the database lookup
     *
     * @param searchCriteria the search criteria to be filtered
     * @return the filteredSearchCriteria
     */
    public Map<String, String> filterSearchCriteria(Map<String, String> searchCriteria) {
        if (getControl() instanceof FilterableLookupCriteriaControl) {
            return ((FilterableLookupCriteriaControl) getControl()).filterSearchCriteria(getPropertyName(),
                    searchCriteria);
        } else {
            return searchCriteria;
        }
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        LookupInputField lookupInputFieldCopy = (LookupInputField) component;
        lookupInputFieldCopy.setDisableWildcardsAndOperators(this.disableWildcardsAndOperators);
        lookupInputFieldCopy.setAddControlSelectAllOption(this.addControlSelectAllOption);
        lookupInputFieldCopy.setTriggerOnChange(this.triggerOnChange);
        lookupInputFieldCopy.setRanged(this.ranged);
    }

    /**
     * Retrieves a new radio group control instance for converted lookup criteria checkboxes from Spring
     * (initialized by the bean definition with the given id)
     *
     * @return RadioGroupControl
     */
    private static RadioGroupControl getCheckboxConvertedRadioControl() {
        return (RadioGroupControl) ComponentFactory.getNewComponentInstance(CHECKBOX_CONVERTED_RADIO_CONTROL);
    }
}
