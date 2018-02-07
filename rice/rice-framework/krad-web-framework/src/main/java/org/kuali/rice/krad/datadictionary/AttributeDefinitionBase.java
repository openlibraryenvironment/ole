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

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.validation.capability.ExistenceConstrainable;
import org.kuali.rice.krad.datadictionary.validation.capability.SimpleConstrainable;
import org.kuali.rice.krad.datadictionary.validation.constraint.SimpleConstraint;

/**
 * Common class for attribute definitions in the DataDictionary, which contains
 * information relating to the display, validation, and general maintenance of a
 * specific attribute of an entry. An attribute can be a simple or complex attribute
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class AttributeDefinitionBase extends DataDictionaryDefinitionBase implements ExistenceConstrainable,
        SimpleConstrainable {

    protected String name;

    protected String label;
    protected String shortLabel;
    protected String displayLabelAttribute;

    protected String constraintText;
    protected String summary;
    protected String description;

    protected SimpleConstraint simpleConstraint;

    public AttributeDefinitionBase() {
        super();
        simpleConstraint = new SimpleConstraint();
    }

    /**
     * Name of the attribute
     *
     * @return the name
     */
    @BeanTagAttribute(name="name")
    public String getName() {
        return name;
    }

    /**
     * Name of the attribute
     *
     * @param name
     */
    public void setName(String name) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("invalid (blank) name");
        }
        this.name = name;
    }

    /**
     * The label element is the field or collection name that will be shown on
     * inquiry and maintenance screens. This will be overridden by presence of
     * displayLabelAttribute element.
     *
     * @return the label
     */
    @BeanTagAttribute(name="label")
    public String getLabel() {
        return label;
    }

    /**
     * The label element is the field or collection name that will be shown on
     * inquiry and maintenance screens. This will be overridden by presence of
     * displayLabelAttribute element.
     */
    public void setLabel(String label) {
        if (StringUtils.isBlank(label)) {
            throw new IllegalArgumentException("invalid (blank) label");
        }
        this.label = label;
    }

    /**
     * @return the shortLabel, or the label if no shortLabel has been set
     */
    @BeanTagAttribute(name="shortLabel")
    public String getShortLabel() {
        return (shortLabel != null) ? shortLabel : getLabel();
    }

    /**
     * @return the shortLabel directly, without substituting in the label
     */
    protected String getDirectShortLabel() {
        return shortLabel;
    }

    /**
     * The shortLabel element is the field or collection name that will be used
     * in applications when a shorter name (than the label element) is required.
     * This will be overridden by presence of displayLabelAttribute element.
     */
    public void setShortLabel(String shortLabel) {
        if (StringUtils.isBlank(shortLabel)) {
            throw new IllegalArgumentException("invalid (blank) shortLabel");
        }
        this.shortLabel = shortLabel;
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
     * @return String text to display for the constraint message
     */
    @BeanTagAttribute(name="constraintText")
    public String getConstraintText() {
        return this.constraintText;
    }

    /**
     * Setter for the constraint message text
     *
     * @param constraintText
     */
    public void setConstraintText(String constraintText) {
        this.constraintText = constraintText;
    }

    /**
     * The summary element is used to provide a short description of the
     * attribute or collection. This is designed to be used for help purposes.
     *
     * @return the summary
     */
    @BeanTagAttribute(name="summary")
    public String getSummary() {
        return summary;
    }

    /**
     * The summary element is used to provide a short description of the
     * attribute or collection. This is designed to be used for help purposes.
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * The description element is used to provide a long description of the
     * attribute or collection. This is designed to be used for help purposes.
     *
     * @return the description
     */
    @BeanTagAttribute(name="description")
    public String getDescription() {
        return description;
    }

    /**
     * The description element is used to provide a long description of the
     * attribute or collection. This is designed to be used for help purposes.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayLabelAttribute() {
        return displayLabelAttribute;
    }

    /**
     * The displayLabelAttribute element is used to indicate that the label and
     * short label should be obtained from another attribute.
     *
     * The label element and short label element defined for this attribute will
     * be overridden. Instead, the label and short label values will be obtained
     * by referencing the corresponding values from the attribute indicated by
     * this element.
     */
    public void setDisplayLabelAttribute(String displayLabelAttribute) {
        this.displayLabelAttribute = displayLabelAttribute;
    }

    /**
     * Gets the SimpleConstraint which contains settings for required, min, max, minLength, and maxLength.
     *
     * @return SimpleConstraint object
     */
    public SimpleConstraint getSimpleConstraint() {
        return simpleConstraint;
    }

    /**
     * Sets the SimpleConstraint which contains settings for required, min, max, minLength, and maxLength.
     *
     * @param simpleConstraint
     */
    public void setSimpleConstraint(SimpleConstraint simpleConstraint) {
        this.simpleConstraint = simpleConstraint;
    }

    /**
     * Sets if this attribute is required
     *
     * @param required true when required, false otherwise
     */
    public void setRequired(Boolean required) {
        this.simpleConstraint.setRequired(required);
    }

    /**
     * The required element allows values of "true" or "false". A value of
     * "true" indicates that a value must be entered for this business object
     * when creating or editing a new business object.
     */
    @BeanTagAttribute(name="required")
    public Boolean isRequired() {
        return this.simpleConstraint.isRequired();
    }

}
