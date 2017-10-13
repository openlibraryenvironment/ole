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
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.validator.ValidationTrace;

import java.util.ArrayList;
import java.util.List;

/**
 * A single Relationship definition in the DataDictionary, which contains information concerning which primitive
 * attributes of this
 * class can be used to retrieve an instance of some related Object instance
 *
 * The relationship element defines how primitive attributes of this
 * class can be used to retrieve an instance of some related Object instance
 * DD: See RelationshipDefinition.java.
 *
 * JSTL: relationship is a Map which is accessed using a key which is the
 * objectAttributeName of a relationship.  The map contains a single entry
 * with a key of "primitiveAttributes" and value which is an attributesMap ExportMap.
 *
 * The attributesMap ExportMap contains the following keys:
 * 0   (for first primitiveAttribute)
 * 1   (for second primitiveAttribute)
 * etc.
 * The corresponding value for each entry is an primitiveAttribute ExportMap
 * which contains the following keys:
 * "sourceName"
 * "targetName"
 */
@BeanTag(name = "relationshipDefinition-bean")
public class RelationshipDefinition extends DataDictionaryDefinitionBase {
    private static final long serialVersionUID = 2946722646095412576L;

    protected String objectAttributeName; //Same as parentAttributeName of DataObjectRelationship
    protected Class<?> sourceClass; //parentClass

    /**
     * For 1:1 relationships, this class represents the type of the reference class.  For 1:n references, this class
     * represents the type of the element
     * of the collection
     */
    protected Class<?> targetClass; //relatedClass

    protected List<PrimitiveAttributeDefinition> primitiveAttributes = new ArrayList<PrimitiveAttributeDefinition>();
    //parentToChildReferences
    protected List<SupportAttributeDefinition> supportAttributes = new ArrayList<SupportAttributeDefinition>();
    //parentToChildReferences

    public RelationshipDefinition() {}

    @BeanTagAttribute(name = "objectAttributeName")
    public String getObjectAttributeName() {
        return objectAttributeName;
    }

    @BeanTagAttribute(name = "sourceClass")
    public Class<?> getSourceClass() {
        return sourceClass;
    }

    /**
     * Returns the {@link #targetClass}
     */
    @BeanTagAttribute(name = "targetClass")
    public Class<?> getTargetClass() {
        if (targetClass == null) {
            Class propertyClass = DataDictionary.getAttributeClass(sourceClass, objectAttributeName);
            if (propertyClass == null) {
                throw new AttributeValidationException("cannot get valid class for property '"
                        + objectAttributeName
                        + "' as an attribute of '"
                        + sourceClass
                        + "'");
            }

            targetClass = propertyClass;
        }
        return targetClass;
    }

    /**
     * Sets the {@link #targetClass}
     *
     * @param targetClass
     */
    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    /**
     * Name of the business object property on the containing business object that is linked
     * by the contained PrimitiveAttributeDefinition objects.
     */
    public void setObjectAttributeName(String objectAttributeName) {
        if (StringUtils.isBlank(objectAttributeName)) {
            throw new IllegalArgumentException("invalid (blank) objectAttributeName");
        }

        this.objectAttributeName = objectAttributeName;
    }

    @BeanTagAttribute(name = "primitiveAttributes", type = BeanTagAttribute.AttributeType.LISTBEAN)
    public List<PrimitiveAttributeDefinition> getPrimitiveAttributes() {
        return primitiveAttributes;
    }

    @BeanTagAttribute(name = "supportAttributes", type = BeanTagAttribute.AttributeType.LISTBEAN)
    public List<SupportAttributeDefinition> getSupportAttributes() {
        return supportAttributes;
    }

    public boolean hasIdentifier() {
        for (SupportAttributeDefinition supportAttributeDefinition : supportAttributes) {
            if (supportAttributeDefinition.isIdentifier()) {
                return true;
            }
        }
        return false;
    }

    public SupportAttributeDefinition getIdentifier() {
        for (SupportAttributeDefinition supportAttributeDefinition : supportAttributes) {
            if (supportAttributeDefinition.isIdentifier()) {
                return supportAttributeDefinition;
            }
        }
        return null;
    }

    /**
     * Directly validate simple fields, call completeValidation on Definition fields.
     *
     * @see org.kuali.rice.krad.datadictionary.DataDictionaryEntry#completeValidation()
     */
    public void completeValidation(Class rootBusinessObjectClass, Class otherBusinessObjectClass) {
        String propertyName = objectAttributeName;
        if (!DataDictionary.isPropertyOf(rootBusinessObjectClass, propertyName)) {
            throw new AttributeValidationException("property '"
                    + propertyName
                    + "' is not an attribute of class '"
                    + rootBusinessObjectClass
                    + "' ("
                    + ""
                    + ")");
        }

        getTargetClass(); // performs validation when this is called the first time

        for (PrimitiveAttributeDefinition primitiveAttributeDefinition : primitiveAttributes) {
            primitiveAttributeDefinition.completeValidation(rootBusinessObjectClass, targetClass);
        }
        for (SupportAttributeDefinition supportAttributeDefinition : supportAttributes) {
            supportAttributeDefinition.completeValidation(rootBusinessObjectClass, targetClass);
        }
    }

    /**
     * Directly validate simple fields
     *
     * @see org.kuali.rice.krad.datadictionary.DataDictionaryEntry#completeValidation(org.kuali.rice.krad.datadictionary.validator.ValidationTrace)
     */
    public void completeValidation(Class rootBusinessObjectClass, Class otherBusinessObjectClass,
            ValidationTrace tracer) {
        tracer.addBean(this.getClass().getSimpleName(), "Attribute: " + getObjectAttributeName());
        try {
            if (!DataDictionary.isPropertyOf(rootBusinessObjectClass, getObjectAttributeName())) {
                String currentValues[] =
                        {"property = " + getObjectAttributeName(), "Class =" + rootBusinessObjectClass};
                tracer.createError("Property is not an attribute of the class", currentValues);
            }
        } catch (RuntimeException ex) {
            String currentValues[] = {"attribute = " + getObjectAttributeName(), "Exception = " + ex.getMessage()};
            tracer.createError("Unable to validate attribute", currentValues);
        }

        if (targetClass == null) {
            Class propertyClass = DataDictionary.getAttributeClass(sourceClass, objectAttributeName);
            if (propertyClass == null) {
                String currentValues[] =
                        {"property = " + getObjectAttributeName(), "sourceClass = " + getSourceClass()};
                tracer.createError("Cannot get valid class for property", currentValues);
            } else {
                targetClass = propertyClass;
            }
        }

        for (PrimitiveAttributeDefinition primitiveAttributeDefinition : primitiveAttributes) {
            primitiveAttributeDefinition.completeValidation(rootBusinessObjectClass, targetClass, tracer.getCopy());
        }
        for (SupportAttributeDefinition supportAttributeDefinition : supportAttributes) {
            supportAttributeDefinition.completeValidation(rootBusinessObjectClass, targetClass, tracer.getCopy());
        }
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "RelationshipDefinition for relationship " + getObjectAttributeName();
    }

    /**
     * The primitiveAttribute element identifies one pair of
     * corresponding fields in the primary business object and
     * the related business object.
     *
     * JSTL: primitiveAttribute is a Map which is accessed by the
     * sequential key of "0", "1", etc.  Each entry contains the following
     * keys:
     * sourceName (String)
     * targetName (String)
     * The value corresponding to the sourceName key is the attribute name defined
     * for the primary business object.
     * The value corresponding to the targetName key is the attribute name for
     * the object being referenced by objectAttributeName.
     */
    public void setPrimitiveAttributes(List<PrimitiveAttributeDefinition> primitiveAttributes) {
        this.primitiveAttributes = primitiveAttributes;
    }

    /**
     * Support attributes define additional attributes that can be used to generate
     * lookup field conversions and lookup parameters.
     *
     * Field conversions and lookup parameters are normally generated using foreign key relationships
     * defined within OJB and the DD.  Because Person objects are linked in a special way (i.e. they may
     * come from an external data source and not from the DB, such as LDAP), it is often necessary to define
     * extra fields that are related to each other, sort of like a supplemental foreign key.
     *
     * sourceName is the name of the POJO property of the business object
     * targetName is the name of attribute that corresponds to the sourceName in the looked up BO
     * identifier when true, only the field marked as an identifier will be passed in as a lookup parameter
     * at most one supportAttribute for each relationship should be defined as identifier="true"
     */
    public void setSupportAttributes(List<SupportAttributeDefinition> supportAttributes) {
        this.supportAttributes = supportAttributes;
    }

    /**
     * @param sourceClass the sourceClass to set
     */
    public void setSourceClass(Class<?> sourceClass) {
        this.sourceClass = sourceClass;
    }
}

