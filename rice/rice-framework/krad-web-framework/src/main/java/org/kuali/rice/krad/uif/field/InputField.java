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

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.uif.DataType;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.core.api.util.type.TypeUtils;
import org.kuali.rice.krad.datadictionary.AttributeDefinition;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.parse.BeanTags;
import org.kuali.rice.krad.datadictionary.state.StateMapping;
import org.kuali.rice.krad.datadictionary.validation.capability.CaseConstrainable;
import org.kuali.rice.krad.datadictionary.validation.capability.MustOccurConstrainable;
import org.kuali.rice.krad.datadictionary.validation.capability.PrerequisiteConstrainable;
import org.kuali.rice.krad.datadictionary.validation.capability.SimpleConstrainable;
import org.kuali.rice.krad.datadictionary.validation.capability.ValidCharactersConstrainable;
import org.kuali.rice.krad.datadictionary.validation.constraint.CaseConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.MustOccurConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.PrerequisiteConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.SimpleConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.ValidCharactersConstraint;
import org.kuali.rice.krad.datadictionary.validator.ValidationTrace;
import org.kuali.rice.krad.datadictionary.validator.Validator;
import org.kuali.rice.krad.keyvalues.KeyValuesFinder;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.control.Control;
import org.kuali.rice.krad.uif.control.MultiValueControlBase;
import org.kuali.rice.krad.uif.control.TextAreaControl;
import org.kuali.rice.krad.uif.control.TextControl;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinder;
import org.kuali.rice.krad.uif.element.Label;
import org.kuali.rice.krad.uif.element.Message;
import org.kuali.rice.krad.uif.element.ValidationMessages;
import org.kuali.rice.krad.uif.util.ClientValidationUtils;
import org.kuali.rice.krad.uif.util.CloneUtils;
import org.kuali.rice.krad.uif.util.ComponentFactory;
import org.kuali.rice.krad.uif.util.ComponentUtils;
import org.kuali.rice.krad.uif.util.ConstraintStateUtils;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.view.ViewModel;
import org.kuali.rice.krad.uif.widget.QuickFinder;
import org.kuali.rice.krad.uif.widget.Suggest;
import org.kuali.rice.krad.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Field that encapsulates data input/output captured by an attribute within the
 * application
 *
 * <p>
 * The {@code InputField} provides the majority of the data input/output
 * for the screen. Through these fields the model can be displayed and updated.
 * For data input, the field contains a {@link Control} instance will
 * render an HTML control element(s). The input field also contains a
 * {@link Label}, summary, and widgets such as a quickfinder (for
 * looking up values) and inquiry (for getting more information on the value).
 * {@code InputField} instances can have associated messages (errors)
 * due to invalid input or business rule failures. Security can also be
 * configured to restrict who may view the fields valnue.
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTags({@BeanTag(name = "inputField-bean", parent = "Uif-InputField"),
        @BeanTag(name = "inputField-labelTop-bean", parent = "Uif-InputField-LabelTop"),
        @BeanTag(name = "inputField-labelRight-bean", parent = "Uif-InputField-LabelRight"),
        @BeanTag(name = "checkboxInputField-bean", parent = "Uif-CheckboxInputField"),
        @BeanTag(name = "dialogResponse-bean", parent = "Uif-DialogResponse"),
        @BeanTag(name = "dialogExplanation-bean", parent = "Uif-DialogExplanation"),
        @BeanTag(name = "documentNumber-bean", parent = "Uif-DocumentNumber"),
        @BeanTag(name = "documentStatus-bean", parent = "Uif-DocumentStatus"),
        @BeanTag(name = "documentInitiatorNetworkId-bean", parent = "Uif-DocumentInitiatorNetworkId"),
        @BeanTag(name = "documentCreateDate-bean", parent = "Uif-DocumentCreateDate"),
        @BeanTag(name = "documentTemplateNumber-bean", parent = "Uif-DocumentTemplateNumber"),
        @BeanTag(name = "documentDescription-bean", parent = "Uif-DocumentDescription"),
        @BeanTag(name = "documentExplaination-bean", parent = "Uif-DocumentExplaination"),
        @BeanTag(name = "organizationDocumentNumber-bean", parent = "Uif-OrganizationDocumentNumber"),
        @BeanTag(name = "selectCollectionItemField-bean", parent = "Uif-SelectCollectionItemField")})
public class InputField extends DataField implements SimpleConstrainable, CaseConstrainable, PrerequisiteConstrainable, MustOccurConstrainable, ValidCharactersConstrainable {
    private static final long serialVersionUID = -3703656713706343840L;

    // constraint variables
    private String customValidatorClass;
    private ValidCharactersConstraint validCharactersConstraint;
    private CaseConstraint caseConstraint;
    private List<PrerequisiteConstraint> dependencyConstraints;
    private List<MustOccurConstraint> mustOccurConstraints;
    private SimpleConstraint simpleConstraint;
    private DataType dataType;

    // display props
    private Control control;
    private KeyValuesFinder optionsFinder;

    private boolean uppercaseValue;
    private boolean disableNativeAutocomplete;

    private ValidationMessages validationMessages;

    // messages
    private String constraintText;
    private String instructionalText;

    private Message constraintMessage;
    private Message instructionalMessage;

    private AttributeQuery attributeQuery;

    // widgets
    private boolean enableAutoDirectInquiry;

    private QuickFinder quickfinder;
    private boolean enableAutoQuickfinder;

    private Suggest suggest;

    private boolean widgetInputOnly;

    public InputField() {
        super();

        simpleConstraint = new SimpleConstraint();

        enableAutoDirectInquiry = true;
        enableAutoQuickfinder = true;
    }

    /**
     * The following initialization is performed:
     *
     * <ul>
     * <li>Initializes instructional and constraint message fields if necessary</li>
     * </ul>
     *
     * @see org.kuali.rice.krad.uif.component.ComponentBase#performInitialization(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object)
     */
    @Override
    public void performInitialization(View view, Object model) {
        super.performInitialization(view, model);

        if ((StringUtils.isNotBlank(constraintText) || (getPropertyExpression("constraintText") != null)) && (
                constraintMessage
                        == null)) {
            constraintMessage = ComponentFactory.getConstraintMessage();
            view.assignComponentIds(constraintMessage);
        }

        if ((StringUtils.isNotBlank(instructionalText) || (getPropertyExpression("instructionalText") != null)) && (
                instructionalMessage
                        == null)) {
            instructionalMessage = ComponentFactory.getInstructionalMessage();
            view.assignComponentIds(instructionalMessage);
        }

    }

    /**
     * @see Component#performApplyModel(org.kuali.rice.krad.uif.view.View, Object, org.kuali.rice.krad.uif.component.Component)
     */
    @Override
    public void performApplyModel(View view, Object model, Component parent) {
        super.performApplyModel(view, model, parent);

        // Done in apply model so we have the message text for additional rich message processing in Message
        // Sets message
        if (StringUtils.isNotBlank(instructionalText) && StringUtils.isBlank(instructionalMessage.getMessageText())) {
            instructionalMessage.setMessageText(instructionalText);
        }

        // Sets constraints
        if (StringUtils.isNotBlank(constraintText) && StringUtils.isBlank(constraintMessage.getMessageText())) {
            constraintMessage.setMessageText(constraintText);
        }

        // invoke options finder if options not configured on the control
        List<KeyValue> fieldOptions = new ArrayList<KeyValue>();

        // use options directly configured on the control first
        if ((control != null) && control instanceof MultiValueControlBase) {
            MultiValueControlBase multiValueControl = (MultiValueControlBase) control;
            if ((multiValueControl.getOptions() != null) && !multiValueControl.getOptions().isEmpty()) {
                fieldOptions = multiValueControl.getOptions();
            }
        }

        // set multiLineReadOnlyDisplay to true to preserve text formatting
        if (control instanceof TextAreaControl) {
            setMultiLineReadOnlyDisplay(true);
        }

        // if options not configured on the control, invoke configured options finder
        if (fieldOptions.isEmpty() && (optionsFinder != null)) {
            if (optionsFinder instanceof UifKeyValuesFinder) {
                fieldOptions = ((UifKeyValuesFinder) optionsFinder).getKeyValues((ViewModel) model, this);

                // check if blank option should be added
                if (((UifKeyValuesFinder) optionsFinder).isAddBlankOption()) {
                    fieldOptions.add(0, new ConcreteKeyValue("", ""));
                }
            } else {
                fieldOptions = optionsFinder.getKeyValues();
            }

            if ((control != null) && control instanceof MultiValueControlBase) {
                ((MultiValueControlBase) control).setOptions(fieldOptions);
            }
        }

        if (this.enableAutoDirectInquiry && (getInquiry() == null) && !isReadOnly()) {
            buildAutomaticInquiry(view, model, true);
        }

        if (this.enableAutoQuickfinder && (getQuickfinder() == null)) {
            buildAutomaticQuickfinder(view, model);
        }

        // if read only do key/value translation if necessary (if alternative and additional properties not set)
        if (isReadOnly()
                && !fieldOptions.isEmpty()
                && StringUtils.isBlank(getReadOnlyDisplayReplacement())
                && StringUtils.isBlank(getReadOnlyDisplaySuffix())
                && StringUtils.isBlank(getReadOnlyDisplayReplacementPropertyName())
                && StringUtils.isBlank(getReadOnlyDisplaySuffixPropertyName())) {

            Object fieldValue = ObjectPropertyUtils.getPropertyValue(model, getBindingInfo().getBindingPath());

            // TODO: can we translate Collections? (possibly combining output with delimiter
            if ((fieldValue != null) && (TypeUtils.isSimpleType(fieldValue.getClass()))) {
                for (KeyValue keyValue : fieldOptions) {
                    if (StringUtils.equals(fieldValue.toString(), keyValue.getKey())) {
                        setReadOnlyDisplayReplacement(keyValue.getValue());
                        break;
                    }
                }
            }
        }
    }

    /**
     * The following actions are performed:
     *
     * <ul>
     * <li>Set the ids for the various attribute components</li>
     * <li>Sets up the client side validation for constraints on this field. In
     * addition, it sets up the messages applied to this field</li>
     * <li>Disable native autocomplete with the suggest widget is configured</li>
     * </ul>
     *
     * @see org.kuali.rice.krad.uif.component.ComponentBase#performFinalize(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object, org.kuali.rice.krad.uif.component.Component)
     */
    @Override
    public void performFinalize(View view, Object model, Component parent) {
        super.performFinalize(view, model, parent);

        setupIds();
        this.addDataAttribute(UifConstants.DataAttributes.ROLE, UifConstants.RoleTypes.INPUT_FIELD);

        // if read only or the control is null no input can be given so no need to setup validation
        if (isReadOnly() || getControl() == null) {
            return;
        }

        if (uppercaseValue) {
            Object currentPropertyValue = ObjectPropertyUtils.getPropertyValue(model, getBindingInfo().getBindingPath());
            if (currentPropertyValue instanceof String) {
                String uppercasedValue = ((String) currentPropertyValue).toUpperCase();
                ObjectPropertyUtils.setPropertyValue(model, getBindingInfo().getBindingPath(), uppercasedValue);
            }
        }

        // browser's native autocomplete causes issues with the suggest plugin
        if ((suggest != null) && suggest.isSuggestConfigured()) {
           setDisableNativeAutocomplete(true);
        }

        // adjust paths on PrerequisiteConstraint property names
        adjustPrerequisiteConstraintBinding(dependencyConstraints);

        // adjust paths on MustOccurConstraints property names
        adjustMustOccurConstraintBinding(mustOccurConstraints);

        // adjust paths on CaseConstraint property names
        if (caseConstraint != null) {
            String propertyName = getBindingInfo().getPropertyAdjustedBindingPath(caseConstraint.getPropertyName());
            caseConstraint.setPropertyName(propertyName);
        }

        setupFieldQuery();

        // special requiredness indicator handling, if this was previously not required reset its required
        // message to be ** for indicating required in the next state
        String path = view.getStateObjectBindingPath();
        Object stateObject;

        if (StringUtils.isNotBlank(path)) {
            stateObject = ObjectPropertyUtils.getPropertyValue(model, path);
        } else {
            stateObject = model;
        }
        StateMapping stateMapping = view.getStateMapping();

        if (stateMapping != null) {
            String validationState = ConstraintStateUtils.getClientViewValidationState(model, view);
            SimpleConstraint appliedSimpleConstraint = ConstraintStateUtils.getApplicableConstraint(
                    this.getSimpleConstraint(), validationState, stateMapping);

            if (appliedSimpleConstraint != null
                    && appliedSimpleConstraint.getRequired() != null
                    && appliedSimpleConstraint.getRequired()) {
                SimpleConstraint prevConstraint = ConstraintStateUtils.getApplicableConstraint(
                        this.getSimpleConstraint(), stateMapping.getCurrentState(stateObject), stateMapping);
                if (prevConstraint == null || prevConstraint.getRequired() == null || !prevConstraint.getRequired()) {
                    this.getFieldLabel().getRequiredMessage().setMessageText("**");
                }
            }
        }

        ClientValidationUtils.processAndApplyConstraints(this, view, model);
    }

    /**
     * Creates a new {@link org.kuali.rice.krad.uif.widget.QuickFinder} and then invokes the lifecycle process for
     * the quickfinder to determine if a relationship was found, if so the quickfinder is assigned to the field
     *
     * @param view view instance being processed
     * @param model object containing the view data
     */
    protected void buildAutomaticQuickfinder(View view, Object model) {
        QuickFinder autoQuickfinder = ComponentFactory.getQuickFinder();

        view.getViewHelperService().spawnSubLifecyle(view, model, autoQuickfinder, this,
                UifConstants.ViewPhases.INITIALIZE, UifConstants.ViewPhases.APPLY_MODEL);

        // if render flag is true, that means the quickfinder was able to find a relationship
        if (autoQuickfinder.isRender()) {
            this.quickfinder = autoQuickfinder;
        }
    }

    /**
     * Overrides processReadOnlyListDisplay to handle MultiValueControls by creating the list of values from values
     * instead of the keys of the options selected (makes the list "human-readable").  Otherwise it just passes the
     * list ahead as normal if this InputField does not use a MultiValueControl.
     *
     * @param model the model
     * @param originalList originalList of values
     */
    @Override
    protected void processReadOnlyListDisplay(Object model, List<?> originalList) {
        //Special handling for option based fields
        if ((control != null) && control instanceof MultiValueControlBase) {
            List<String> newList = new ArrayList<String>();
            List<KeyValue> fieldOptions = ((MultiValueControlBase) control).getOptions();

            if (fieldOptions == null || fieldOptions.isEmpty()) {
                return;
            }

            for (Object fieldValue : originalList) {
                for (KeyValue keyValue : fieldOptions) {
                    if (fieldValue != null && StringUtils.equals(fieldValue.toString(), keyValue.getKey())) {
                        newList.add(keyValue.getValue());
                        break;
                    }
                }
            }
            this.setReadOnlyDisplayReplacement(super.generateReadOnlyListDisplayReplacement(newList));
        } else {
            this.setReadOnlyDisplayReplacement(super.generateReadOnlyListDisplayReplacement(originalList));
        }
    }

    /**
     * Overridding to check quickfinder when masked is being applied. If quickfinder is configured set the component
     * to widgetInputOnly, else set to readOnlh
     *
     * @see DataField#setAlternateAndAdditionalDisplayValue(org.kuali.rice.krad.uif.view.View, java.lang.Object)
     */
    @Override
    protected void setAlternateAndAdditionalDisplayValue(View view, Object model) {
        // if alternate or additional display values set don't override
        if (StringUtils.isNotBlank(getReadOnlyDisplayReplacement()) || StringUtils.isNotBlank(
                getReadOnlyDisplaySuffix())) {
            return;
        }

        if (isApplyMask()) {
            if ((this.quickfinder != null) && StringUtils.isNotBlank(this.quickfinder.getDataObjectClassName())) {
                setWidgetInputOnly(true);
            } else {
                setReadOnly(true);
            }
        }

        super.setAlternateAndAdditionalDisplayValue(view, model);
    }

    /**
     * Adjust paths on the must occur constrain bindings
     *
     * @param mustOccurConstraints
     */
    protected void adjustMustOccurConstraintBinding(List<MustOccurConstraint> mustOccurConstraints) {
        if (mustOccurConstraints != null) {
            for (MustOccurConstraint mustOccurConstraint : mustOccurConstraints) {
                adjustPrerequisiteConstraintBinding(mustOccurConstraint.getPrerequisiteConstraints());
                adjustMustOccurConstraintBinding(mustOccurConstraint.getMustOccurConstraints());
            }
        }
    }

    /**
     * Adjust paths on the prerequisite constraint bindings
     *
     * @param prerequisiteConstraints
     */
    protected void adjustPrerequisiteConstraintBinding(List<PrerequisiteConstraint> prerequisiteConstraints) {
        if (prerequisiteConstraints != null) {
            for (PrerequisiteConstraint prerequisiteConstraint : prerequisiteConstraints) {
                String propertyName = getBindingInfo().getPropertyAdjustedBindingPath(
                        prerequisiteConstraint.getPropertyName());
                prerequisiteConstraint.setPropertyName(propertyName);
            }
        }
    }

    /**
     * Performs setup of the field attribute query and informational display properties. Paths
     * are adjusted to match the binding for the this field, and the necessary onblur script for
     * triggering the query client side is constructed
     */
    protected void setupFieldQuery() {
        if (getAttributeQuery() != null) {
            // adjust paths on query mappings
            getAttributeQuery().updateQueryFieldMapping(getBindingInfo());
            getAttributeQuery().updateReturnFieldMapping(getBindingInfo());
            getAttributeQuery().updateQueryMethodArgumentFieldList(getBindingInfo());

            // build onblur script for field query
            String script = "executeFieldQuery('" + getControl().getId() + "',";
            script += "'" + getId() + "'," + getAttributeQuery().getQueryFieldMappingJsString() + ",";
            script += getAttributeQuery().getQueryMethodArgumentFieldsJsString() + ",";
            script += getAttributeQuery().getReturnFieldMappingJsString() + ");";

            if (StringUtils.isNotBlank(getControl().getOnBlurScript())) {
                script = getControl().getOnBlurScript() + script;
            }
            getControl().setOnBlurScript(script);
        }
    }

    /**
     * Sets the ids on all components the input field uses so they will all
     * contain this input field's id in their ids. This is useful for jQuery
     * manipulation.
     */
    protected void setupIds() {
        // update ids so they all match the attribute

        setNestedComponentIdAndSuffix(getControl(), UifConstants.IdSuffixes.CONTROL);
        setNestedComponentIdAndSuffix(getValidationMessages(), UifConstants.IdSuffixes.ERRORS);
        setNestedComponentIdAndSuffix(getFieldLabel(), UifConstants.IdSuffixes.LABEL);
        setNestedComponentIdAndSuffix(getInstructionalMessage(), UifConstants.IdSuffixes.INSTRUCTIONAL);
        setNestedComponentIdAndSuffix(getConstraintMessage(), UifConstants.IdSuffixes.CONSTRAINT);
        setNestedComponentIdAndSuffix(getQuickfinder(), UifConstants.IdSuffixes.QUICK_FINDER);
        setNestedComponentIdAndSuffix(getSuggest(), UifConstants.IdSuffixes.SUGGEST);

        if (this.getControl() != null) {
            this.getControl().addDataAttribute(UifConstants.DataAttributes.CONTROL_FOR, this.getId());
        }
    }

    /**
     * Defaults the properties of the {@code InputField} to the
     * corresponding properties of its {@code AttributeDefinition}
     * retrieved from the dictionary (if such an entry exists). If the field
     * already contains a value for a property, the definitions value is not
     * used.
     *
     * @param view view instance the field belongs to
     * @param attributeDefinition AttributeDefinition instance the property values should be
     * copied from
     */
    @Override
    public void copyFromAttributeDefinition(View view, AttributeDefinition attributeDefinition) {
        super.copyFromAttributeDefinition(view, attributeDefinition);

        // max length
        if (getMaxLength() == null) {
            setMaxLength(attributeDefinition.getMaxLength());
        }

        // min length
        if (getMinLength() == null) {
            setMinLength(attributeDefinition.getMinLength());
        }

        // valid characters
        if (getValidCharactersConstraint() == null) {
            setValidCharactersConstraint(attributeDefinition.getValidCharactersConstraint());
        }

        if (getCaseConstraint() == null) {
            setCaseConstraint(attributeDefinition.getCaseConstraint());
        }

        if (getDependencyConstraints() == null) {
            setDependencyConstraints(attributeDefinition.getPrerequisiteConstraints());
        }

        if (getMustOccurConstraints() == null) {
            setMustOccurConstraints(attributeDefinition.getMustOccurConstraints());
        }

        // required
        if (getRequired() == null) {
            setRequired(attributeDefinition.isRequired());

            //if still null, default to false
            if (getRequired() == null) {
                setRequired(Boolean.FALSE);
            }
        }

        if (getDataType() == null) {
            setDataType(attributeDefinition.getDataType());
            //Assume date if dataType is still null and using a DatePicker
            if (getDataType() == null
                    && control instanceof TextControl
                    && ((TextControl) control).getDatePicker() != null) {
                setDataType(DataType.DATE);
            }
        }

        // control
        if ((getControl() == null) && (attributeDefinition.getControlField() != null)) {
            Control control = attributeDefinition.getControlField();
            view.assignComponentIds(control);

            setControl(ComponentUtils.copy(control));
        }

        // constraint
        if (StringUtils.isEmpty(getConstraintText())) {
            setConstraintText(attributeDefinition.getConstraintText());

            if (constraintMessage == null) {
                constraintMessage = ComponentFactory.getConstraintMessage();
                view.assignComponentIds(constraintMessage);
            }
            getConstraintMessage().setMessageText(attributeDefinition.getConstraintText());
        }

        // options
        if (getOptionsFinder() == null) {
            setOptionsFinder(attributeDefinition.getOptionsFinder());
        }

        // copy over simple constraint information because we cannot directly use simpleConstraint from
        // attributeDefinition because the settings in InputField take precedence
        if (this.getSimpleConstraint().getConstraintStateOverrides() == null) {
            this.getSimpleConstraint().setConstraintStateOverrides(
                    attributeDefinition.getSimpleConstraint().getConstraintStateOverrides());
        }

        if (this.getSimpleConstraint().getStates().isEmpty()) {
            this.getSimpleConstraint().setStates(attributeDefinition.getSimpleConstraint().getStates());
        }

        if (this.getSimpleConstraint().getMessageKey() == null) {
            this.getSimpleConstraint().setMessageKey(attributeDefinition.getSimpleConstraint().getMessageKey());
        }

        if (this.getSimpleConstraint().getApplyClientSide() == null) {
            this.getSimpleConstraint().setApplyClientSide(
                    attributeDefinition.getSimpleConstraint().getApplyClientSide());
        }

        if (this.getSimpleConstraint().getValidationMessageParams() == null) {
            this.getSimpleConstraint().setValidationMessageParams(
                    attributeDefinition.getSimpleConstraint().getValidationMessageParams());
        }
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#getComponentsForLifecycle()
     */
    @Override
    public List<Component> getComponentsForLifecycle() {
        List<Component> components = super.getComponentsForLifecycle();

        components.add(instructionalMessage);
        components.add(constraintMessage);
        components.add(control);
        components.add(validationMessages);
        components.add(quickfinder);
        components.add(suggest);

        return components;
    }

    /**
     * @see DataField#isInputAllowed()
     */
    @Override
    public boolean isInputAllowed() {
        return true;
    }

    /**
     * {@code Control} instance that should be used to input data for the
     * field
     *
     * <p>
     * When the field is editable, the control will be rendered so the user can
     * input a value(s). Controls typically are part of a Form and render
     * standard HTML control elements such as text input, select, and checkbox
     * </p>
     *
     * @return Control instance
     */
    @BeanTagAttribute(name = "control", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Control getControl() {
        return this.control;
    }

    /**
     * Setter for the field's control
     *
     * @param control
     */
    public void setControl(Control control) {
        this.control = control;
    }

    /**
     * Field that contains the messages (errors) for the input field. The
     * {@code ValidationMessages} holds configuration on associated messages along
     * with information on rendering the messages in the user interface
     *
     * @return ValidationMessages instance
     */
    @BeanTagAttribute(name = "validationMessages", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public ValidationMessages getValidationMessages() {
        return this.validationMessages;
    }

    /**
     * Setter for the input field's errors field
     *
     * @param validationMessages
     */
    public void setValidationMessages(ValidationMessages validationMessages) {
        this.validationMessages = validationMessages;
    }

    /**
     * Instance of {@code KeyValuesFinder} that should be invoked to
     * provide a List of values the field can have. Generally used to provide
     * the options for a multi-value control or to validate the submitted field
     * value
     *
     * @return KeyValuesFinder instance
     */
    @BeanTagAttribute(name = "optionsFinder", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public KeyValuesFinder getOptionsFinder() {
        return this.optionsFinder;
    }

    /**
     * Setter for the field's KeyValuesFinder instance
     *
     * @param optionsFinder
     */
    public void setOptionsFinder(KeyValuesFinder optionsFinder) {
        this.optionsFinder = optionsFinder;
    }

    /**
     * Get the class of the optionsFinder being used by this InputField
     *
     * @return the class of the set optionsFinder, if not set or not applicable, returns null
     */
    @BeanTagAttribute(name = "optionsFinderClass")
    public Class<? extends KeyValuesFinder> getOptionsFinderClass(){
        if(this.optionsFinder != null){
            return this.optionsFinder.getClass();
        }
        else{
            return null;
        }
    }

    /**
     * Setter that takes in the class name for the options finder and creates a
     * new instance to use as the finder for the input field
     *
     * @param optionsFinderClass the options finder class to set
     */
    public void setOptionsFinderClass(Class<? extends KeyValuesFinder> optionsFinderClass) {
        this.optionsFinder = ObjectUtils.newInstance(optionsFinderClass);
    }

    /**
     * Indicates whether direct inquiries should be automatically set when a relationship for
     * the field's property is found
     *
     * <p>
     * Note this only applies when the {@link #getInquiry()} widget has not been configured (is null)
     * and is set to true by default
     * </p>
     *
     * @return true if auto direct inquiries are enabled, false if not
     */
    public boolean isEnableAutoDirectInquiry() {
        return enableAutoDirectInquiry;
    }

    /**
     * Setter for enabling automatic direct inquiries
     *
     * @param enableAutoDirectInquiry
     */
    public void setEnableAutoDirectInquiry(boolean enableAutoDirectInquiry) {
        this.enableAutoDirectInquiry = enableAutoDirectInquiry;
    }

    /**
     * Lookup finder widget for the field
     *
     * <p>
     * The quickfinder widget places a small icon next to the field that allows
     * the user to bring up a search screen for finding valid field values. The
     * {@code Widget} instance can be configured to point to a certain
     * {@code LookupView}, or the framework will attempt to associate the
     * field with a lookup based on its metadata (in particular its
     * relationships in the model)
     * </p>
     *
     * @return QuickFinder lookup widget
     */
    @BeanTagAttribute(name = "quickfinder", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public QuickFinder getQuickfinder() {
        return this.quickfinder;
    }

    /**
     * Setter for the lookup widget
     *
     * @param quickfinder the field lookup widget to set
     */
    public void setQuickfinder(QuickFinder quickfinder) {
        this.quickfinder = quickfinder;
    }

    /**
     * Indicates whether quickfinders should be automatically set when a relationship for the field's
     * property is found
     *
     * <p>
     * Note this only applies when the {@link #getQuickfinder()} widget has not been configured (is null)
     * and is set to true by default
     * </p>
     *
     * @return true if auto quickfinders are enabled, false if not
     */
    public boolean isEnableAutoQuickfinder() {
        return enableAutoQuickfinder;
    }

    /**
     * Setter for enabling automatic quickfinders
     *
     * @param enableAutoQuickfinder
     */
    public void setEnableAutoQuickfinder(boolean enableAutoQuickfinder) {
        this.enableAutoQuickfinder = enableAutoQuickfinder;
    }

    /**
     * Suggest box widget for the input field
     *
     * <p>
     * If enabled (by render flag), as the user inputs data into the
     * fields control a dynamic query is performed to provide the user
     * suggestions on values which they can then select
     * </p>
     *
     * <p>
     * Note the Suggest widget is only valid when using a standard TextControl
     * </p>
     *
     * @return Suggest instance
     */
    @BeanTagAttribute(name = "suggest", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Suggest getSuggest() {
        return suggest;
    }

    /**
     * Setter for the fields suggest widget
     *
     * @param suggest the field suggest widget to  set
     */
    public void setSuggest(Suggest suggest) {
        this.suggest = suggest;
    }

    /**
     * Indicates indicates whether the field can only be updated through a widget
     *
     * widgetInputOnly behaves similar to ReadOnly with the exception that the value of the input field
     * can be changed via the associated widget (e.g. spinner, date picker, quickfinder, etc).
     *
     * @return true if only widget input is allowed, false otherwise
     */
    @BeanTagAttribute(name = "widgetInputOnly")
    public boolean isWidgetInputOnly() {
        return this.widgetInputOnly;
    }

    /**
     * Setter for the widget input only indicator
     *
     * @param widgetInputOnly
     */
    public void setWidgetInputOnly(boolean widgetInputOnly) {
        this.widgetInputOnly = widgetInputOnly;
    }

    /**
     * Instructional text that display an explanation of the field usage
     *
     * <p>
     * Text explaining how to use the field, including things like what values should be selected
     * in certain cases and so on (instructions)
     * </p>
     *
     * @return instructional message
     */
    @BeanTagAttribute(name = "instructionalText")
    public String getInstructionalText() {
        return this.instructionalText;
    }

    /**
     * Setter for the instructional message
     *
     * @param instructionalText the instructional text to set
     */
    public void setInstructionalText(String instructionalText) {
        this.instructionalText = instructionalText;
    }

    /**
     * Message field that displays instructional text
     *
     * <p>
     * This message field can be configured to for adjusting how the instructional text will display. Generally
     * the styleClasses property will be of most interest
     * </p>
     *
     * @return instructional message field
     */
    @BeanTagAttribute(name = "instructionalMessage", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Message getInstructionalMessage() {
        return this.instructionalMessage;
    }

    /**
     * Setter for the instructional text message field
     *
     * <p>
     * Note this is the setter for the field that will render the instructional text. The actual text can be
     * set on the field but can also be set using {@link #setInstructionalText(String)}
     * </p>
     *
     * @param instructionalMessage the instructional message to set
     */
    public void setInstructionalMessage(Message instructionalMessage) {
        this.instructionalMessage = instructionalMessage;
    }

    /**
     * Text that display a restriction on the value a field can hold
     *
     * <p>
     * For example when the value must be a valid format (phone number, email), certain length, min/max value and
     * so on this text can be used to indicate the constraint to the user. Generally displays with the control so
     * it is visible when the user tabs to the field
     * </p>
     *
     * @return text to display for the constraint message
     */
    @BeanTagAttribute(name = "constraintText")
    public String getConstraintText() {
        return this.constraintText;
    }

    /**
     * Setter for the constraint message text
     *
     * @param constraintText the constraint text to set
     */
    public void setConstraintText(String constraintText) {
        this.constraintText = constraintText;
    }

    /**
     * Message field that displays constraint text
     *
     * <p>
     * This message field can be configured to for adjusting how the constrain text will display. Generally
     * the styleClasses property will be of most interest
     * </p>
     *
     * @return constraint message field
     */
    @BeanTagAttribute(name = "constraintMessage", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Message getConstraintMessage() {
        return this.constraintMessage;
    }

    /**
     * Setter for the constraint text message field
     *
     * <p>
     * Note this is the setter for the field that will render the constraint text. The actual text can be
     * set on the field but can also be set using {@link #setConstraintText(String)}
     * </p>
     *
     * @param constraintMessage the constrain message field to set
     */
    public void setConstraintMessage(Message constraintMessage) {
        this.constraintMessage = constraintMessage;
    }

    /**
     * The {@code ValidCharactersConstraint} that applies to this {@code InputField}
     *
     * @return the valid characters constraint for this input field
     */
    @Override
    @BeanTagAttribute(name = "validCharactersConstraint", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public ValidCharactersConstraint getValidCharactersConstraint() {
        return this.validCharactersConstraint;
    }

    /**
     * Setter for {@code validCharacterConstraint}
     *
     * @param validCharactersConstraint the {@code ValidCharactersConstraint} to set
     */
    public void setValidCharactersConstraint(ValidCharactersConstraint validCharactersConstraint) {
        this.validCharactersConstraint = validCharactersConstraint;
    }

    /**
     * The {@code CaseConstraint} that applies to this {@code InputField}
     *
     * @return the case constraint for this input field
     */
    @Override
    @BeanTagAttribute(name = "caseConstraint", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public CaseConstraint getCaseConstraint() {
        return this.caseConstraint;
    }

    /**
     * Setter for {@code caseConstraint}
     *
     * @param caseConstraint the {@code CaseConstraint} to set
     */
    public void setCaseConstraint(CaseConstraint caseConstraint) {
        this.caseConstraint = caseConstraint;
    }

    /**
     * List of {@code PrerequisiteConstraint} that apply to this {@code InputField}
     *
     * @return the dependency constraints for this input field
     */
    @BeanTagAttribute(name = "dependencyConstraints", type = BeanTagAttribute.AttributeType.LISTBEAN)
    public List<PrerequisiteConstraint> getDependencyConstraints() {
        return this.dependencyConstraints;
    }

    /**
     * Setter for {@code dependencyConstraints}
     *
     * @param dependencyConstraints list of {@code PrerequisiteConstraint} to set
     */
    public void setDependencyConstraints(List<PrerequisiteConstraint> dependencyConstraints) {
        this.dependencyConstraints = dependencyConstraints;
    }

    @Override
    public List<PrerequisiteConstraint> getPrerequisiteConstraints() {
        return dependencyConstraints;
    }

    /**
     * List of {@code MustOccurConstraint} that apply to this {@code InputField}
     *
     * @return the must occur constraints for this input field
     */
    @Override
    @BeanTagAttribute(name = "mustOccurConstraints", type = BeanTagAttribute.AttributeType.LISTBEAN)
    public List<MustOccurConstraint> getMustOccurConstraints() {
        return this.mustOccurConstraints;
    }

    /**
     * Setter for {@code mustOccurConstraints}
     *
     * @param mustOccurConstraints list of {@code MustOccurConstraint} to set
     */
    public void setMustOccurConstraints(List<MustOccurConstraint> mustOccurConstraints) {
        this.mustOccurConstraints = mustOccurConstraints;
    }

    /**
     * Simple constraints for the input field
     *
     * <p>
     * A simple constraint which store the values for constraints such as required,
     * min/max length, and min/max value.
     * </p>
     *
     * @return the simple constraint of the input field
     */
    @Override
    @BeanTagAttribute(name = "simpleConstraint", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public SimpleConstraint getSimpleConstraint() {
        return this.simpleConstraint;
    }

    /**
     * Setter for simple constraint
     *
     * <p>
     * When a simple constraint is set on this object ALL simple validation
     * constraints set directly will be overridden - recommended to use this or
     * the other gets/sets for defining simple constraints, not both.
     * </p>
     *
     * @param simpleConstraint the simple constraint to set
     */
    public void setSimpleConstraint(SimpleConstraint simpleConstraint) {
        this.simpleConstraint = simpleConstraint;
    }

    /**
     * This does not have to be set, represents the DataType constraint of this field.
     * This is only checked during server side validation.
     *
     * @param dataType the dataType to set
     */
    public void setDataType(DataType dataType) {
        this.simpleConstraint.setDataType(dataType);
    }

    public void setDataType(String dataType) {
        this.simpleConstraint.setDataType(DataType.valueOf(dataType));
    }

    /**
     * Gets the DataType of this InputField, note that DataType set to be date
     * when this field is using a date picker with a TextControl and has not otherwise been
     * explicitly set.
     *
     * @return DataType
     */
    @BeanTagAttribute(name = "dataType", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public DataType getDataType() {
        return this.simpleConstraint.getDataType();
    }

    /**
     * Maximum number of characters the input field value is allowed to have
     *
     * <p>
     * The maximum length determines the maximum allowable length of the value
     * for data entry editing purposes.  The maximum length is inclusive and can
     * be smaller or longer than the actual control size.  The constraint
     * is enforced on all data types (e.g. a numeric data type needs to meet the
     * maximum length constraint in which digits and symbols are counted).
     * </p>
     *
     * @return the maximum length of the input field
     */
    @BeanTagAttribute(name = "maxLength")
    public Integer getMaxLength() {
        return simpleConstraint.getMaxLength();
    }

    /**
     * Setter for input field max length
     *
     * @param maxLength the maximum length to set
     */
    public void setMaxLength(Integer maxLength) {
        simpleConstraint.setMaxLength(maxLength);
    }

    /**
     * Minimum number of characters the input field value needs to be
     *
     * <p>
     * The minimum length determines the minimum required length of the value for
     * data entry editing purposes.  The minimum length is inclusive. The constraint
     * is enforced on all data types (e.g. a numeric data type needs to meet the
     * minimum length requirement in which digits and symbols are counted).
     * </p>
     *
     * @return the minimum length of the input field
     */
    @BeanTagAttribute(name = "minLength")
    public Integer getMinLength() {
        return simpleConstraint.getMinLength();
    }

    /**
     * Setter for input field minimum length
     *
     * @param minLength the minLength to set
     */
    public void setMinLength(Integer minLength) {
        simpleConstraint.setMinLength(minLength);
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#getRequired()
     */
    @Override
    @BeanTagAttribute(name = "required")
    public Boolean getRequired() {
        return this.simpleConstraint.getRequired();
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#setRequired(java.lang.Boolean)
     */
    @Override
    public void setRequired(Boolean required) {
        this.simpleConstraint.setRequired(required);
    }

    /**
     * The exclusive minimum value for numeric or date field.
     *
     * <p>
     * The exclusiveMin element determines the minimum allowable value for data
     * entry editing purposes. This constrain is supported for numeric and
     * date fields and to be used in conjunction with the appropriate
     * {@link ValidCharactersConstraint}.
     *
     * For numeric constraint the value can be an integer or decimal such as -.001 or 99.
     * </p>
     *
     * @return the exclusive minimum numeric value of the input field
     */
    @BeanTagAttribute(name = "exclusiveMin")
    public String getExclusiveMin() {
        return simpleConstraint.getExclusiveMin();
    }

    /**
     * Setter for the field's exclusive minimum value
     *
     * @param exclusiveMin the minimum value to set
     */
    public void setExclusiveMin(String exclusiveMin) {
        simpleConstraint.setExclusiveMin(exclusiveMin);
    }

    /**
     * The inclusive maximum value for numeric or date field.
     *
     * <p>
     * The inclusiveMax element determines the maximum allowable value for data
     * entry editing purposes. This constrain is supported for numeric and
     * date fields and to be used in conjunction with the appropriate
     * {@link ValidCharactersConstraint}.
     *
     * For numeric constraint the value can be an integer or decimal such as -.001 or 99.
     * </p>
     *
     * @return the inclusive maximum numeric value of the input field
     */
    @BeanTagAttribute(name = "inclusiveMax")
    public String getInclusiveMax() {
        return simpleConstraint.getInclusiveMax();
    }

    /**
     * Setter for the field's inclusive maximum value
     *
     * @param inclusiveMax the maximum value to set
     */
    public void setInclusiveMax(String inclusiveMax) {
        simpleConstraint.setInclusiveMax(inclusiveMax);
    }

    /**
     * Attribute query instance configured for this field to dynamically pull information back for
     * updates other fields or providing messages
     *
     * <p>
     * If field attribute query is not null, associated event script will be generated to trigger the
     * query from the UI. This will invoke the {@code AttributeQueryService} to
     * execute the query and return an instance of {@code AttributeQueryResult} that is then
     * read by the script to update the UI. Typically used to update informational property values or
     * other field values
     * </p>
     *
     * @return AttributeQuery instance
     */
    @BeanTagAttribute(name = "attributeQuery", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public AttributeQuery getAttributeQuery() {
        return attributeQuery;
    }

    /**
     * Setter for this field's attribute query
     *
     * @param attributeQuery
     */
    public void setAttributeQuery(AttributeQuery attributeQuery) {
        this.attributeQuery = attributeQuery;
    }

    /**
     * Perform uppercase flag for this field to force input to uppercase.
     *
     * <p>
     * It this flag is set to true the 'text-transform' style on the field will be set to 'uppercase'
     * which will automatically change any text input into the field to uppercase.
     * </p>
     *
     * @return performUppercase flag
     */
    @BeanTagAttribute(name = "uppercaseValue")
    public boolean isUppercaseValue() {
        return uppercaseValue;
    }

    /**
     * Setter for this field's performUppercase flag
     *
     * @param uppercaseValue boolean flag
     */
    public void setUppercaseValue(boolean uppercaseValue) {
        this.uppercaseValue = uppercaseValue;
    }

    /**
     * Indicates whether the browser autocomplete functionality should be disabled for the
     * input field (adds autocomplete="off")
     *
     * <p>
     * The browser's native autocomplete functionality can cause issues with security fields and also fields
     * with the UIF suggest widget enabled
     * </p>
     *
     * @return true if the native autocomplete should be turned off for the input field, false if not
     */
    public boolean isDisableNativeAutocomplete() {
        return disableNativeAutocomplete;
    }

    /**
     * Setter to disable browser autocomplete for the input field
     *
     * @param disableNativeAutocomplete
     */
    public void setDisableNativeAutocomplete(boolean disableNativeAutocomplete) {
        this.disableNativeAutocomplete = disableNativeAutocomplete;
    }

    @Override
    public boolean isRenderFieldset() {
        return super.isRenderFieldset() || (quickfinder != null
                && quickfinder.isRender()
                && quickfinder.getQuickfinderAction() != null
                && quickfinder.getQuickfinderAction().isRender());
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);

        InputField inputFieldCopy = (InputField) component;

        inputFieldCopy.setCustomValidatorClass(this.customValidatorClass);
        inputFieldCopy.setValidCharactersConstraint(CloneUtils.deepClone(this.validCharactersConstraint));
        inputFieldCopy.setCaseConstraint(CloneUtils.deepClone(this.caseConstraint));

        if (dependencyConstraints != null) {
            List<PrerequisiteConstraint> dependencyConstraintsCopy = Lists.newArrayListWithExpectedSize(
                    dependencyConstraints.size());

            for (PrerequisiteConstraint dependencyConstraint : dependencyConstraints) {
                dependencyConstraintsCopy.add(CloneUtils.deepClone(dependencyConstraint));
            }

            inputFieldCopy.setDependencyConstraints(dependencyConstraintsCopy);
        }

        if (mustOccurConstraints != null) {
            List<MustOccurConstraint> mustOccurConstraintsCopy = Lists.newArrayListWithExpectedSize(
                    mustOccurConstraints.size());

            for (MustOccurConstraint mustOccurConstraint : mustOccurConstraints) {
                mustOccurConstraintsCopy.add(CloneUtils.deepClone(mustOccurConstraint));
            }

            inputFieldCopy.setMustOccurConstraints(mustOccurConstraintsCopy);
        }

        inputFieldCopy.setSimpleConstraint(CloneUtils.deepClone(this.simpleConstraint));
        inputFieldCopy.setDataType(this.dataType);

        // display props
        if (this.control != null) {
            inputFieldCopy.setControl((Control) this.control.copy());
        }

        inputFieldCopy.setOptionsFinder(this.optionsFinder);
        inputFieldCopy.setUppercaseValue(this.uppercaseValue);
        inputFieldCopy.setDisableNativeAutocomplete(this.disableNativeAutocomplete);

        if (this.validationMessages != null) {
            inputFieldCopy.setValidationMessages((ValidationMessages) this.validationMessages.copy());
        }

        // messages
        inputFieldCopy.setConstraintText(this.constraintText);
        inputFieldCopy.setInstructionalText(this.instructionalText);

        if (this.constraintMessage != null) {
            inputFieldCopy.setConstraintMessage((Message) this.constraintMessage.copy());
        }

        if (this.instructionalMessage != null) {
            inputFieldCopy.setInstructionalMessage((Message) this.instructionalMessage.copy());
        }

        if (this.attributeQuery != null) {
            inputFieldCopy.setAttributeQuery((AttributeQuery) this.attributeQuery.copy());
        }

        inputFieldCopy.setEnableAutoDirectInquiry(this.enableAutoDirectInquiry);

        // widgets
        if (this.quickfinder != null) {
            inputFieldCopy.setQuickfinder((QuickFinder) this.quickfinder.copy());
        }

        inputFieldCopy.setEnableAutoQuickfinder(this.enableAutoQuickfinder);

        if (this.suggest != null) {
            inputFieldCopy.setSuggest((Suggest) this.suggest.copy());
        }

        inputFieldCopy.setWidgetInputOnly(this.widgetInputOnly);
    }

    /**
     * @see org.kuali.rice.krad.uif.component.Component#completeValidation
     */
    @Override
    public void completeValidation(ValidationTrace tracer) {
        tracer.addBean(this);

        // Checks that the control is set
        if (getControl() == null) {
            if (Validator.checkExpressions(this, "control")) {
                String currentValues[] = {"control =" + getConstraintText()};
                tracer.createWarning("Control should be set", currentValues);
            }
        }

        super.completeValidation(tracer.getCopy());
    }

    public void setCustomValidatorClass(String customValidatorClass) {
        this.customValidatorClass = customValidatorClass;
    }
}
