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
import org.kuali.rice.krad.datadictionary.exception.AttributeValidationException;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.validation.capability.CollectionSizeConstrainable;
import org.kuali.rice.krad.datadictionary.validator.ValidationTrace;

/**
 * CollectionDefinition defines a single Collection attribute definition in the DataDictionary
 *
 * <p>It contains information relating to the display, validation,
 * and general maintenance of a specific Collection attribute of an entry. It helps to provide meaningful labels for
 * collections on a business or data object.
 * It can be used to define collections that are generated at runtime and marked using @{@code Transient} in the
 * containing
 * business or data object class.</p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "collectionDefinition-bean")
public class CollectionDefinition extends DataDictionaryDefinitionBase implements CollectionSizeConstrainable {
    private static final long serialVersionUID = -2644072136271281041L;

    protected String dataObjectClass;

    protected String name;

    protected String label;

    protected String shortLabel;

    protected String elementLabel;

    protected String summary;

    protected String description;

    protected Integer minOccurs;

    protected Integer maxOccurs;

    /**
     * default constructor
     */
    public CollectionDefinition() {
        //empty
    }

    /**
     * gets the name of the collection
     *
     * @return the collection name
     */
    public String getName() {
        return name;
    }

    /**
     * sets the name of the collection
     *
     * @param name - the collection name
     * @throws IllegalArgumentException if the name is blank
     */
    public void setName(String name) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("invalid (blank) name");
        }
        this.name = name;
    }

    /**
     * gets the label
     *
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * sets the label
     *
     * @param label - a descriptive string to use for a label
     */
    public void setLabel(String label) {
        if (StringUtils.isBlank(label)) {
            throw new IllegalArgumentException("invalid (blank) label");
        }
        this.label = label;
    }

    /**
     * gets the short label
     *
     * @return the shortLabel, or the label if no shortLabel has been set
     */
    public String getShortLabel() {
        return (shortLabel != null) ? shortLabel : label;
    }

    /**
     * sets the short label
     *
     * @param shortLabel - the short label
     * @throws IllegalArgumentException when {@code shortLabel} is blank
     */
    public void setShortLabel(String shortLabel) {
        if (StringUtils.isBlank(shortLabel)) {
            throw new IllegalArgumentException("invalid (blank) shortLabel");
        }
        this.shortLabel = shortLabel;
    }

    /**
     * Gets the elementLabel attribute
     *
     * @return the element Label
     */
    public String getElementLabel() {
        return elementLabel;
    }

    /**
     * gets the element label
     *
     * <p>The elementLabel defines the name to be used for a single object within the collection.
     * For example: "Address" may be the name
     * of one object within the "Addresses" collection.</p>
     */
    public void setElementLabel(String elementLabel) {
        this.elementLabel = elementLabel;
    }

    /**
     * gets the summary
     *
     * <p>summary element is used to provide a short description of the
     * attribute or collection. This is designed to be used for help purposes.</p>
     *
     * @return the summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * gets the summary
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * gets the description
     *
     * <p>The description element is used to provide a long description of the
     * attribute or collection.  This is designed to be used for help purposes.</p>
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * sets the description
     *
     * @param description - the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * gets the data object class
     *
     * <p>This is the Java class type of the object contained in this collection</p>
     *
     * @return the dataObjectClass
     */
    public String getDataObjectClass() {
        return this.dataObjectClass;
    }

    /**
     * sets the data object class
     *
     * @param dataObjectClass the dataObjectClass to set
     */
    public void setDataObjectClass(String dataObjectClass) {
        this.dataObjectClass = dataObjectClass;
    }

    /**
     * Directly validate simple fields, call completeValidation on Definition fields
     *
     * @see org.kuali.rice.krad.datadictionary.DataDictionaryEntry#completeValidation()
     */
    public void completeValidation(Class rootBusinessObjectClass, Class otherBusinessObjectClass) {
        if (!DataDictionary.isCollectionPropertyOf(rootBusinessObjectClass, name)) {
            throw new AttributeValidationException("property '"
                    + name
                    + "' is not a collection property of class '"
                    + rootBusinessObjectClass
                    + "' ("
                    + ""
                    + ")");
        }
    }

    /**
     * Directly validate simple fields, call completeValidation on Definition
     * fields.
     *
     * @see org.kuali.rice.krad.datadictionary.DataDictionaryEntry#completeValidation(org.kuali.rice.krad.datadictionary.validator.ValidationTrace)
     */
    public void completeValidation(Class rootBusinessObjectClass, Class otherBusinessObjectClass,
            ValidationTrace tracer) {
        tracer.addBean(this.getClass().getSimpleName(), "Attribute: " + getName());
        if (!DataDictionary.isCollectionPropertyOf(rootBusinessObjectClass, name)) {
            String currentValues[] = {"property = " + getName(), "Class =" + rootBusinessObjectClass};
            tracer.createError("Property is not collection property of the class", currentValues);
        }
    }

    /**
     * @return a descriptive string with the collection name
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CollectionDefinition for collection " + getName();
    }

    /**
     * @see org.kuali.rice.krad.datadictionary.validation.constraint.CollectionSizeConstraint#getMaximumNumberOfElements()
     */
    @Override
    public Integer getMaximumNumberOfElements() {
        return this.maxOccurs;
    }

    /**
     * @see org.kuali.rice.krad.datadictionary.validation.constraint.CollectionSizeConstraint#getMinimumNumberOfElements()
     */
    @Override
    public Integer getMinimumNumberOfElements() {
        return this.minOccurs;
    }

    /**
     * gets the minimum amount of items in this collection
     *
     * @return the minOccurs
     */
    public Integer getMinOccurs() {
        return this.minOccurs;
    }

    /**
     * gets the minimum amount of items in this collection
     *
     * @param minOccurs the minOccurs to set
     */
    public void setMinOccurs(Integer minOccurs) {
        this.minOccurs = minOccurs;
    }

    /**
     * gets maximum amount of items in this collection
     *
     * @return the maxOccurs
     */
    public Integer getMaxOccurs() {
        return this.maxOccurs;
    }

    /**
     * sets maximum amount of items in this collection
     *
     * @param maxOccurs the maxOccurs to set
     */
    public void setMaxOccurs(Integer maxOccurs) {
        this.maxOccurs = maxOccurs;
    }

}
