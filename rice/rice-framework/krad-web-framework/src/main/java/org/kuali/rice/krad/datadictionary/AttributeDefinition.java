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
package org.kuali.rice.krad.datadictionary;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.uif.DataType;
import org.kuali.rice.core.api.util.ClassLoaderUtils;
import org.kuali.rice.core.web.format.Formatter;
import org.kuali.rice.krad.datadictionary.control.ControlDefinition;
import org.kuali.rice.krad.datadictionary.exception.AttributeValidationException;
import org.kuali.rice.krad.datadictionary.exception.ClassValidationException;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.validation.ValidationPattern;
import org.kuali.rice.krad.datadictionary.validation.capability.CaseConstrainable;
import org.kuali.rice.krad.datadictionary.validation.capability.Formatable;
import org.kuali.rice.krad.datadictionary.validation.capability.HierarchicallyConstrainable;
import org.kuali.rice.krad.datadictionary.validation.capability.MustOccurConstrainable;
import org.kuali.rice.krad.datadictionary.validation.capability.PrerequisiteConstrainable;
import org.kuali.rice.krad.datadictionary.validation.capability.ValidCharactersConstrainable;
import org.kuali.rice.krad.datadictionary.validation.constraint.CaseConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.LookupConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.MustOccurConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.PrerequisiteConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.ValidCharactersConstraint;
import org.kuali.rice.krad.datadictionary.validator.ValidationTrace;
import org.kuali.rice.krad.keyvalues.KeyValuesFinder;
import org.kuali.rice.krad.uif.control.Control;
import org.kuali.rice.krad.util.ObjectUtils;

import java.beans.PropertyEditor;
import java.util.List;

/**
 * A single attribute definition in the DataDictionary, which contains
 * information relating to the display, validation, and general maintenance of a
 * specific attribute of an entry.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "attributeDefinition-bean")
public class AttributeDefinition extends AttributeDefinitionBase implements CaseConstrainable, PrerequisiteConstrainable, Formatable, HierarchicallyConstrainable, MustOccurConstrainable, ValidCharactersConstrainable {
    private static final long serialVersionUID = -2490613377818442742L;

    protected Boolean forceUppercase = Boolean.FALSE;

    protected DataType dataType;

    protected Boolean unique;

    //These are deprecated DO NOT USE with new KRAD implementations
    @Deprecated
    protected ValidationPattern validationPattern;

    protected ControlDefinition control;

    // TODO: rename to control once ControlDefinition is removed
    protected Control controlField;

    protected String formatterClass;
    protected PropertyEditor propertyEditor;

    protected AttributeSecurity attributeSecurity;

    protected Boolean dynamic;

    // KRAD constraints
    protected String customValidatorClass;
    protected ValidCharactersConstraint validCharactersConstraint;
    protected CaseConstraint caseConstraint;
    protected List<PrerequisiteConstraint> dependencyConstraints;
    protected List<MustOccurConstraint> mustOccurConstraints;
    // if the user wants to match against two searches, that search must be defined as well
    protected LookupConstraint lookupDefinition;
    protected String lookupContextPath;

    //TODO: This may not be required since we now use ComplexAttributeDefinition
    protected String childEntryName;

    private KeyValuesFinder optionsFinder;

    protected String alternateDisplayAttributeName;
    protected String additionalDisplayAttributeName;

    public AttributeDefinition() {
        super();
    }

    /**
     * Setter for force upper case
     *
     * @param forceUppercase
     */
    public void setForceUppercase(Boolean forceUppercase) {
        this.forceUppercase = forceUppercase;
    }

    /**
     * Indicates whether user entry should be converted to upper case
     *
     * <p>
     * If set all user input will be changed to uppercase. Values from the database will also be forced to display
     * as upper case and thus be persisted as upper case.
     * </p>
     *
     * @return boolean true if force upper case is set
     */
    @BeanTagAttribute(name = "forceUppercase")
    public Boolean getForceUppercase() {
        return this.forceUppercase;
    }

    /**
     * @see org.kuali.rice.krad.datadictionary.validation.constraint.LengthConstraint#getMaxLength()
     */
    @BeanTagAttribute(name = "maxLength")
    public Integer getMaxLength() {
        return this.getSimpleConstraint().getMaxLength();
    }

    /**
     * Setter for maximum length
     *
     * @param maxLength
     */
    public void setMaxLength(Integer maxLength) {
        this.getSimpleConstraint().setMaxLength(maxLength);
    }

    /**
     * @see org.kuali.rice.krad.datadictionary.validation.constraint.RangeConstraint#getExclusiveMin()
     */
    @BeanTagAttribute(name = "exclusiveMin")
    public String getExclusiveMin() {
        return this.getSimpleConstraint().getExclusiveMin();
    }

    /**
     * Setter for minimum value
     *
     * @param exclusiveMin - minimum allowed value
     */
    public void setExclusiveMin(String exclusiveMin) {
        this.getSimpleConstraint().setExclusiveMin(exclusiveMin);
    }

    /**
     * @see org.kuali.rice.krad.datadictionary.validation.constraint.RangeConstraint#getInclusiveMax()
     */
    @BeanTagAttribute(name = "inclusiveMax")
    public String getInclusiveMax() {
        return this.getSimpleConstraint().getInclusiveMax();
    }

    /**
     * Setter for maximum value
     *
     * @param inclusiveMax - max allowed value
     */
    public void setInclusiveMax(String inclusiveMax) {
        this.getSimpleConstraint().setInclusiveMax(inclusiveMax);
    }

    /**
     * The validationPattern element defines the allowable character-level or
     * field-level values for an attribute.
     *
     * JSTL: validationPattern is a Map which is accessed using a key of
     * "validationPattern". Each entry may contain some of the keys listed
     * below. The keys that may be present for a given attribute are dependent
     * upon the type of validationPattern.
     *
     * maxLength (String) exactLength type allowWhitespace allowUnderscore
     * allowPeriod validChars precision scale allowNegative
     *
     * The allowable keys (in addition to type) for each type are: Type****
     * ***Keys*** alphanumeric exactLength maxLength allowWhitespace
     * allowUnderscore allowPeriod
     *
     * alpha exactLength maxLength allowWhitespace
     *
     * anyCharacter exactLength maxLength allowWhitespace
     *
     * charset validChars
     *
     * numeric exactLength maxLength
     *
     * fixedPoint allowNegative precision scale
     *
     * floatingPoint allowNegative
     *
     * date n/a emailAddress n/a javaClass n/a month n/a phoneNumber n/a
     * timestamp n/a year n/a zipcode n/a
     *
     * Note: maxLength and exactLength are mutually exclusive. If one is
     * entered, the other may not be entered.
     *
     * Note: See ApplicationResources.properties for exact regex patterns. e.g.
     * validationPatternRegex.date for regex used in date validation.
     */
    public void setValidationPattern(ValidationPattern validationPattern) {
        this.validationPattern = validationPattern;
    }

    /**
     * Indicates whether a validation pattern has been set
     * @return boolean
     */
    public boolean hasValidationPattern() {
        return (validationPattern != null);
    }

    /**
     * Defines the allowable character-level or
     * field-level values for an attribute
     *
     * <p>
     * ValidationPattern is a Map which is accessed using a key of "validationPattern". Each entry may contain
     * some of the keys listed below. The keys that may be present for a given attribute are dependent
     * upon the type of validationPattern.
     *
     * maxLength (String) exactLength type allowWhitespace allowUnderscore
     * allowPeriod validChars precision scale allowNegative
     *
     * The allowable keys (in addition to type) for each type are: Type****
     * ***Keys*** alphanumeric exactLength maxLength allowWhitespace
     * allowUnderscore allowPeriod
     *
     * alpha exactLength maxLength allowWhitespace
     *
     * anyCharacter exactLength maxLength allowWhitespace
     *
     * charset validChars
     *
     * numeric exactLength maxLength
     *
     * fixedPoint allowNegative precision scale
     *
     * floatingPoint allowNegative
     *
     * date n/a emailAddress n/a javaClass n/a month n/a phoneNumber n/a
     * timestamp n/a year n/a zipcode n/a
     *
     * Note: maxLength and exactLength are mutually exclusive. If one is
     * entered, the other may not be entered.
     *
     * Note: See ApplicationResources.properties for exact regex patterns. e.g.
     * validationPatternRegex.date for regex used in date validation.
     * </p>
     *
     * @return ValidationPattern
     */
    public ValidationPattern getValidationPattern() {
        return this.validationPattern;
    }

    /**
     * @return control
     */
    @BeanTagAttribute(name = "oldControl", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public ControlDefinition getControl() {
        return control;
    }

    /**
     * The control element defines the manner in which an attribute is displayed
     * and the manner in which the attribute value is entered.
     *
     * JSTL: control is a Map representing an HTML control. It is accessed using
     * a key of "control". The table below shows the types of entries associated
     * with each type of control.
     *
     * * Control Type** **Key** **Value** checkbox checkbox boolean String
     *
     * hidden hidden boolean String
     *
     * radio radio boolean String valuesFinder valuesFinder class name
     * dataObjectClass String keyAttribute String labelAttribute String
     * includeKeyInLabel boolean String
     *
     * select select boolean String valuesFinder valuesFinder class name
     * dataObjectClass String keyAttribute String labelAttribute String
     * includeBlankRow boolean String includeKeyInLabel boolean String
     *
     * apcSelect apcSelect boolean String paramNamespace String
     * parameterDetailType String parameterName String
     *
     * text text boolean String size String
     *
     * textarea textarea boolean String rows cols
     *
     * currency currency boolean String size String formattedMaxLength String
     *
     * kualiUser kualiUser boolean String universalIdAttributeName String
     * userIdAttributeName String personNameAttributeName String
     *
     * lookupHidden lookupHidden boolean String
     *
     * lookupReadonly lookupReadonly boolean String
     *
     * @param control
     * @throws IllegalArgumentException if the given control is null
     */
    public void setControl(ControlDefinition control) {
        if (control == null) {
            throw new IllegalArgumentException("invalid (null) control");
        }
        this.control = control;
    }

    public boolean hasFormatterClass() {
        return (formatterClass != null);
    }

    @Override
    @BeanTagAttribute(name = "formatterClass")
    public String getFormatterClass() {
        return formatterClass;
    }

    /**
     * The formatterClass element is used when custom formatting is required for
     * display of the field value. This field specifies the name of the java
     * class to be used for the formatting. About 15 different classes are
     * available including BooleanFormatter, CurrencyFormatter, DateFormatter,
     * etc.
     */
    public void setFormatterClass(String formatterClass) {
        if (formatterClass == null) {
            throw new IllegalArgumentException("invalid (null) formatterClass");
        }
        this.formatterClass = formatterClass;
    }

    /**
     * Performs formatting of the field value for display and then converting the value back to its
     * expected type from a string
     *
     * <p>
     * Note property editors exist and are already registered for the basic Java types and the
     * common Kuali types such as [@link KualiDecimal}. Registration with this property is only
     * needed for custom property editors
     * </p>
     *
     * @return PropertyEditor property editor instance to use for this field
     */
    @BeanTagAttribute(name = "propertyEditor", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public PropertyEditor getPropertyEditor() {
        return propertyEditor;
    }

    /**
     * Setter for the custom property editor to use for the field
     *
     * @param propertyEditor
     */
    public void setPropertyEditor(PropertyEditor propertyEditor) {
        this.propertyEditor = propertyEditor;
    }

    /**
     * Convenience setter for configuring a property editor by class
     *
     * @param propertyEditorClass
     */
    public void setPropertyEditorClass(Class<? extends PropertyEditor> propertyEditorClass) {
        this.propertyEditor = ObjectUtils.newInstance(propertyEditorClass);
    }

    /**
     * Directly validate simple fields, call completeValidation on Definition
     * fields.
     *
     * @see org.kuali.rice.krad.datadictionary.DataDictionaryEntry#completeValidation()
     */
    @Override
    @Deprecated
    public void completeValidation(Class<?> rootObjectClass, Class<?> otherObjectClass) {
        try {
            if (!DataDictionary.isPropertyOf(rootObjectClass, getName())) {
                throw new AttributeValidationException("property '"
                        + getName()
                        + "' is not a property of class '"
                        + rootObjectClass.getName()
                        + "' ("
                        + ""
                        + ")");
            }

            //TODO currently requiring a control or controlField, but this should not be case (AttrField should probably do the check)
            if (getControl() == null && getControlField() == null) {
                throw new AttributeValidationException("property '"
                        + getName()
                        + "' in class '"
                        + rootObjectClass.getName()
                        + " does not have a control defined");
            }

            if (getControl() != null) {
                getControl().completeValidation(rootObjectClass, otherObjectClass);
            }

            if (attributeSecurity != null) {
                attributeSecurity.completeValidation(rootObjectClass, otherObjectClass);
            }

            if (validationPattern != null) {
                validationPattern.completeValidation();
            }

            if (formatterClass != null) {
                try {
                    Class formatterClassObject = ClassUtils.getClass(ClassLoaderUtils.getDefaultClassLoader(),
                            getFormatterClass());
                    if (!Formatter.class.isAssignableFrom(formatterClassObject)) {
                        throw new ClassValidationException("formatterClass is not a valid instance of "
                                + Formatter.class.getName()
                                + " instead was: "
                                + formatterClassObject.getName());
                    }
                } catch (ClassNotFoundException e) {
                    throw new ClassValidationException("formatterClass could not be found: " + getFormatterClass(), e);
                }
            }
        } catch (RuntimeException ex) {
            Logger.getLogger(getClass()).error(
                    "Unable to validate attribute " + rootObjectClass + "." + getName() + ": " + ex.getMessage(), ex);
            throw ex;
        }
    }

    /**
     * Directly validate simple fields, call completeValidation on Definition
     * fields.
     *
     * @see org.kuali.rice.krad.datadictionary.DataDictionaryEntry#completeValidation(org.kuali.rice.krad.datadictionary.validator.ValidationTrace)
     */
    public void completeValidation(Class rootObjectClass, Class otherObjectClass, ValidationTrace tracer) {
        tracer.addBean(this.getClass().getSimpleName(), "Attribute: " + getName());
        try {
            if (!DataDictionary.isPropertyOf(rootObjectClass, getName())) {
                String currentValues[] = {"property = " + getName(), "class = " + rootObjectClass.getName()};
                tracer.createError("Property is not found in class", currentValues);
            }

            //TODO currently requiring a control or controlField, but this should not be case (AttrField should probably do the check)
            if (getControl() == null && getControlField() == null) {
                String currentValues[] = {"property = " + getName(), "class = " + rootObjectClass.getName()};
                tracer.createError("Property does not have a control defined in the class", currentValues);
            }

            if (getControl() != null) {
                //getControl().completeValidation(rootObjectClass, otherObjectClass, tracer.getCopy());
            }

            if (attributeSecurity != null) {
                attributeSecurity.completeValidation(rootObjectClass, otherObjectClass, tracer.getCopy());
            }

            if (validationPattern != null) {
                // validationPattern.completeValidation(tracer.getCopy());
            }

            if (formatterClass != null) {
                try {
                    Class formatterClassObject = ClassUtils.getClass(ClassLoaderUtils.getDefaultClassLoader(),
                            getFormatterClass());
                    if (!Formatter.class.isAssignableFrom(formatterClassObject)) {
                        String currentValues[] = {"formatterClassObject = " + formatterClassObject.getName()};
                        tracer.createError("FormatterClass is not a valid instance", currentValues);
                    }
                } catch (ClassNotFoundException e) {
                    String currentValues[] = {"class = " + getFormatterClass()};
                    tracer.createError("FormatterClass could not be found", currentValues);
                }
            }
        } catch (RuntimeException ex) {
            String currentValues[] =
                    {"attribute = " + rootObjectClass + "." + getName(), "Exception = " + ex.getMessage()};
            tracer.createError("Unable to validate attribute", currentValues);
        }
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "AttributeDefinition for attribute " + getName();
    }

    /**
     * @return the attributeSecurity
     */
    @BeanTagAttribute(name = "attributeSecurity", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public AttributeSecurity getAttributeSecurity() {
        return this.attributeSecurity;
    }

    /**
     * @param attributeSecurity the attributeSecurity to set
     */
    public void setAttributeSecurity(AttributeSecurity attributeSecurity) {
        this.attributeSecurity = attributeSecurity;
    }

    public boolean hasAttributeSecurity() {
        return (attributeSecurity != null);
    }

    /**
     * This overridden method ...
     *
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtils.isEmpty(name)) {
            throw new RuntimeException("blank name for bean: " + id);
        }
    }

    /**
     * @return the unique
     */
    public Boolean getUnique() {
        return this.unique;
    }

    /**
     * @param unique the unique to set
     */
    public void setUnique(Boolean unique) {
        this.unique = unique;
    }

    /**
     * Default {@code Control} to use when the attribute is to be rendered
     * for the UI. Used by the UIF when a control is not defined for an
     * {@code InputField}
     *
     * @return Control instance
     */
    @BeanTagAttribute(name = "control", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Control getControlField() {
        return this.controlField;
    }

    /**
     * Setter for the default control
     *
     * @param controlField
     */
    public void setControlField(Control controlField) {
        this.controlField = controlField;
    }

    /**
     * @see org.kuali.rice.krad.datadictionary.validation.constraint.LengthConstraint#getMinLength()
     */
    @BeanTagAttribute(name = "minLength")
    public Integer getMinLength() {
        return this.getSimpleConstraint().getMinLength();
    }

    /**
     * Setter for minumum length
     *
     * @param minLength
     */
    public void setMinLength(Integer minLength) {
        this.getSimpleConstraint().setMinLength(minLength);
    }

    /**
     * @return the dataType
     */
    @BeanTagAttribute(name = "dataType", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public DataType getDataType() {
        return simpleConstraint.getDataType();
    }

    /**
     * @param dataType the dataType to set
     */
    public void setDataType(DataType dataType) {
        simpleConstraint.setDataType(dataType);
    }

    public void setDataType(String dataType) {
        simpleConstraint.setDataType(DataType.valueOf(dataType));
    }

    /**
     * @return the customValidatorClass
     */
    @BeanTagAttribute(name = "customValidatorClass")
    public String getCustomValidatorClass() {
        return this.customValidatorClass;
    }

    /**
     * @param customValidatorClass the customValidatorClass to set
     */
    public void setCustomValidatorClass(String customValidatorClass) {
        this.customValidatorClass = customValidatorClass;
    }

    /**
     * @return the validChars
     */
    @Override
    @BeanTagAttribute(name = "validChractersConstraint", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public ValidCharactersConstraint getValidCharactersConstraint() {
        return this.validCharactersConstraint;
    }

    /**
     * @param validCharactersConstraint the validChars to set
     */
    public void setValidCharactersConstraint(ValidCharactersConstraint validCharactersConstraint) {
        this.validCharactersConstraint = validCharactersConstraint;
    }

    /**
     * @return the caseConstraint
     */
    @Override
    @BeanTagAttribute(name = "caseConstraint", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public CaseConstraint getCaseConstraint() {
        return this.caseConstraint;
    }

    /**
     * @param caseConstraint the caseConstraint to set
     */
    public void setCaseConstraint(CaseConstraint caseConstraint) {
        this.caseConstraint = caseConstraint;
    }

    /**
     * @return the requireConstraint
     */
    @Override
    @BeanTagAttribute(name = "prerequisteConstraint", type = BeanTagAttribute.AttributeType.LISTBEAN)
    public List<PrerequisiteConstraint> getPrerequisiteConstraints() {
        return this.dependencyConstraints;
    }

    /**
     * @param dependencyConstraints the requireConstraint to set
     */
    public void setPrerequisiteConstraints(List<PrerequisiteConstraint> dependencyConstraints) {
        this.dependencyConstraints = dependencyConstraints;
    }

    /**
     * @return the occursConstraint
     */
    @Override
    @BeanTagAttribute(name = "mustOccurConstraints", type = BeanTagAttribute.AttributeType.LISTBEAN)
    public List<MustOccurConstraint> getMustOccurConstraints() {
        return this.mustOccurConstraints;
    }

    /**
     * @param mustOccurConstraints the occursConstraint to set
     */
    public void setMustOccurConstraints(List<MustOccurConstraint> mustOccurConstraints) {
        this.mustOccurConstraints = mustOccurConstraints;
    }

    /**
     * @return the lookupDefinition
     */
    public LookupConstraint getLookupDefinition() {
        return this.lookupDefinition;
    }

    /**
     * @param lookupDefinition the lookupDefinition to set
     */
    public void setLookupDefinition(LookupConstraint lookupDefinition) {
        this.lookupDefinition = lookupDefinition;
    }

    /**
     * @return the lookupContextPath
     */
    public String getLookupContextPath() {
        return this.lookupContextPath;
    }

    /**
     * @param lookupContextPath the lookupContextPath to set
     */
    public void setLookupContextPath(String lookupContextPath) {
        this.lookupContextPath = lookupContextPath;
    }

    /**
     * @return the childEntryName
     */
    @BeanTagAttribute(name = "childEntryName")
    public String getChildEntryName() {
        return this.childEntryName;
    }

    /**
     * @param childEntryName the childEntryName to set
     */
    public void setChildEntryName(String childEntryName) {
        this.childEntryName = childEntryName;
    }

    /**
     * Instance of {@code KeyValluesFinder} that should be invoked to
     * provide a List of values the field can have. Generally used to provide
     * the options for a multi-value control or to validate the submitted field
     * value
     *
     * @return KeyValuesFinder instance
     */
    @BeanTagAttribute(name = "optionFinder", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
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
     * Setter that takes in the class name for the options finder and creates a
     * new instance to use as the finder for the attribute field
     *
     * @param optionsFinderClass
     */
    public void setOptionsFinderClass(Class<? extends KeyValuesFinder> optionsFinderClass) {
        this.optionsFinder = ObjectUtils.newInstance(optionsFinderClass);
    }

    public void setAdditionalDisplayAttributeName(String additionalDisplayAttributeName) {
        this.additionalDisplayAttributeName = additionalDisplayAttributeName;
    }

    @BeanTagAttribute(name = "additionalDisplayAttributeName")
    public String getAdditionalDisplayAttributeName() {
        return this.additionalDisplayAttributeName;
    }

    public void setAlternateDisplayAttributeName(String alternateDisplayAttributeName) {
        this.alternateDisplayAttributeName = alternateDisplayAttributeName;
    }

    @BeanTagAttribute(name = "alternateDisplayAttributeName")
    public String getAlternateDisplayAttributeName() {
        return this.alternateDisplayAttributeName;
    }

    /**
     * Gets dependency constraints for this AttributeDefinition.  Same as getPrerequisiteConstraints.
     *
     * @return dependency constraints
     */
    public List<PrerequisiteConstraint> getDependencyConstraints() {
        return dependencyConstraints;
    }

    /**
     * Sets dependency constraints for this AttributeDefinition.  Same as setPrerequisiteConstraints.
     *
     * @param dependencyConstraints dependency constraints
     */
    public void setDependencyConstraints(List<PrerequisiteConstraint> dependencyConstraints) {
        this.dependencyConstraints = dependencyConstraints;
    }

}
