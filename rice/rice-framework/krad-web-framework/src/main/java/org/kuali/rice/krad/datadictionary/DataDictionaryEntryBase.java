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
import org.kuali.rice.krad.datadictionary.exception.DuplicateEntryException;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.state.StateMapping;
import org.kuali.rice.krad.datadictionary.validator.ValidationTrace;
import org.kuali.rice.krad.exception.ValidationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Contains common properties and methods for data dictionary entries
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
abstract public class DataDictionaryEntryBase extends DictionaryBeanBase implements DataDictionaryEntry, Serializable, InitializingBean {
    protected List<AttributeDefinition> attributes;
    protected List<ComplexAttributeDefinition> complexAttributes;
    protected List<CollectionDefinition> collections;
    protected List<RelationshipDefinition> relationships;
    protected Map<String, AttributeDefinition> attributeMap;
    protected Map<String, ComplexAttributeDefinition> complexAttributeMap;
    protected Map<String, CollectionDefinition> collectionMap;
    protected Map<String, RelationshipDefinition> relationshipMap;

    protected StateMapping stateMapping;

    public DataDictionaryEntryBase() {
        this.attributes = new ArrayList<AttributeDefinition>();
        this.complexAttributes = new ArrayList<ComplexAttributeDefinition>();
        this.collections = new ArrayList<CollectionDefinition>();
        this.relationships = new ArrayList<RelationshipDefinition>();
        this.attributeMap = new LinkedHashMap<String, AttributeDefinition>();
        this.complexAttributeMap = new LinkedHashMap<String, ComplexAttributeDefinition>();
        this.collectionMap = new LinkedHashMap<String, CollectionDefinition>();
        this.relationshipMap = new LinkedHashMap<String, RelationshipDefinition>();
    }

    /* Returns the given entry class (bo class or document class) */
    public abstract Class<?> getEntryClass();

    /**
     * @param attributeName
     * @return AttributeDefinition with the given name, or null if none with that name exists
     */
    public AttributeDefinition getAttributeDefinition(String attributeName) {
        if (StringUtils.isBlank(attributeName)) {
            throw new IllegalArgumentException("invalid (blank) attributeName");
        }
        return attributeMap.get(attributeName);
    }

    /**
     * @return a Map containing all AttributeDefinitions associated with this BusinessObjectEntry, indexed by
     *         attributeName
     */
    @BeanTagAttribute(name = "attributes", type = BeanTagAttribute.AttributeType.LISTBEAN)
    public List<AttributeDefinition> getAttributes() {
        return this.attributes;
    }

    /**
     * @return the complexAttributes
     */
    public List<ComplexAttributeDefinition> getComplexAttributes() {
        return this.complexAttributes;
    }

    /**
     * @param complexAttributes the complexAttributes to set
     */
    public void setComplexAttributes(List<ComplexAttributeDefinition> complexAttributes) {
        complexAttributeMap.clear();
        for (ComplexAttributeDefinition complexAttribute : complexAttributes) {
            if (complexAttribute == null) {
                throw new IllegalArgumentException("invalid (null) complexAttributeDefinition");
            }
            String complexAttributeName = complexAttribute.getName();
            if (StringUtils.isBlank(complexAttributeName)) {
                throw new ValidationException("invalid (blank) collectionName");
            }

            if (complexAttributeMap.containsKey(complexAttribute)) {
                throw new DuplicateEntryException("complex attribute '"
                        + complexAttribute
                        + "' already defined as an complex attribute for class '"
                        + getEntryClass().getName()
                        + "'");
            } else if (collectionMap.containsKey(complexAttributeName)) {
                throw new DuplicateEntryException("complex attribute '"
                        + complexAttributeName
                        + "' already defined as a Collection for class '"
                        + getEntryClass().getName()
                        + "'");
            } else if (attributeMap.containsKey(complexAttributeName)) {
                throw new DuplicateEntryException("complex attribute '"
                        + complexAttributeName
                        + "' already defined as an Attribute for class '"
                        + getEntryClass().getName()
                        + "'");
            }

            complexAttributeMap.put(complexAttributeName, complexAttribute);

        }

        this.complexAttributes = complexAttributes;
    }

    /**
     * @param collectionName
     * @return CollectionDefinition with the given name, or null if none with that name exists
     */
    public CollectionDefinition getCollectionDefinition(String collectionName) {
        if (StringUtils.isBlank(collectionName)) {
            throw new IllegalArgumentException("invalid (blank) collectionName");
        }
        return collectionMap.get(collectionName);
    }

    /**
     * @return a Map containing all CollectionDefinitions associated with this BusinessObjectEntry, indexed by
     *         collectionName
     */
    @BeanTagAttribute(name = "collections", type = BeanTagAttribute.AttributeType.LISTBEAN)
    public List<CollectionDefinition> getCollections() {
        return this.collections;
    }

    /**
     * @param relationshipName
     * @return RelationshipDefinition with the given name, or null if none with that name exists
     */
    public RelationshipDefinition getRelationshipDefinition(String relationshipName) {
        if (StringUtils.isBlank(relationshipName)) {
            throw new IllegalArgumentException("invalid (blank) relationshipName");
        }
        return relationshipMap.get(relationshipName);
    }

    /**
     * @return a Map containing all RelationshipDefinitions associated with this BusinessObjectEntry, indexed by
     *         relationshipName
     */
    @BeanTagAttribute(name = "relationships", type = BeanTagAttribute.AttributeType.LISTBEAN)
    public List<RelationshipDefinition> getRelationships() {
        return this.relationships;
    }

    /**
     * Directly validate simple fields, call completeValidation on Definition fields.
     */
    public void completeValidation() {

        for (AttributeDefinition attributeDefinition : attributes) {
            attributeDefinition.completeValidation(getEntryClass(), null);
        }

        for (CollectionDefinition collectionDefinition : collections) {
            collectionDefinition.completeValidation(getEntryClass(), null);
        }

        for (RelationshipDefinition relationshipDefinition : relationships) {
            relationshipDefinition.completeValidation(getEntryClass(), null);
        }
    }

    /**
     * Directly validate simple fields, call completeValidation on Definition
     * fields.
     *
     * @see org.kuali.rice.krad.datadictionary.DataDictionaryEntry#completeValidation(org.kuali.rice.krad.datadictionary.validator.ValidationTrace)
     */
    public void completeValidation(ValidationTrace tracer) {
        for (AttributeDefinition definition : getAttributes()) {
            definition.completeValidation(getEntryClass(), null, tracer.getCopy());
        }
        for (CollectionDefinition definition : getCollections()) {
            definition.completeValidation(getEntryClass(), null, tracer.getCopy());
        }
        for (RelationshipDefinition definition : getRelationships()) {
            definition.completeValidation(getEntryClass(), null, tracer.getCopy());
        }
    }

    /**
     * The attributes element contains attribute
     * elements.  These define the specifications for business object fields.
     *
     * JSTL: attributes is a Map which is accessed by a key of "attributes".
     * This map contains entries with the following keys:
     * attributeName of first attribute
     * attributeName of second attribute
     * etc.
     *
     * The corresponding value for each entry is an attribute ExportMap.
     * By the time the JSTL export happens, all attributeReferences will be
     * indistinguishable from attributes.
     *
     * See AttributesMapBuilder.java
     *
     * The attribute element specifies the way in which a business object
     * field appears on a screen for data entry or display purposes.  These
     * specifications include the following:
     * The title and formatting of the field
     * Descriptive information about the field
     * The edits used at time of data-entry
     *
     * DD: See AttributeDefinition.java
     *
     * JSTL: attribute is a Map which is accessed using a key which is the attributeName
     * of an attribute.  Each entry contains the following keys:
     * name (String)
     * forceUppercase (boolean String)
     * label (String)
     * shortLabel (String, copied from label if not present)
     * maxLength (String)
     * exclusiveMin (bigdecimal String)
     * exclusiveMax (bigdecimal String)
     * validationPattern (Map, optional)
     * required (boolean String)
     * control (Map)
     * summary (String)
     * description (String)
     * formatterClass (String, optional)
     * fullClassName (String)
     * displayWorkgroup(String, optional)
     * displayMaskClass(String, optional)
     *
     * See AttributesMapBuilder.java
     * Note: exclusiveMax is mapped from the inclusiveMax element!
     * The validation logic seems to be assuming inclusiveMax.
     */
    public void setAttributes(List<AttributeDefinition> attributes) {
        attributeMap.clear();
        for (AttributeDefinition attribute : attributes) {
            if (attribute == null) {
                throw new IllegalArgumentException("invalid (null) attributeDefinition");
            }
            String attributeName = attribute.getName();
            if (StringUtils.isBlank(attributeName)) {
                throw new ValidationException("invalid (blank) attributeName");
            }

            if (attributeMap.containsKey(attributeName)) {
                throw new DuplicateEntryException("attribute '"
                        + attributeName
                        + "' already defined as an Attribute for class '"
                        + getEntryClass().getName()
                        + "'");
            } else if (collectionMap.containsKey(attributeName)) {
                throw new DuplicateEntryException("attribute '"
                        + attributeName
                        + "' already defined as a Collection for class '"
                        + getEntryClass().getName()
                        + "'");
            } else if (complexAttributeMap.containsKey(attributeName)) {
                throw new DuplicateEntryException("attribute '"
                        + attributeName
                        + "' already defined as an Complex Attribute for class '"
                        + getEntryClass().getName()
                        + "'");
            }
            attributeMap.put(attributeName, attribute);
        }
        this.attributes = attributes;
    }

    /**
     * The collections element contains collection elements.  These define
     * the lists of other business objects which are related to and
     * defined in the business objects.
     *
     * JSTL: collections is a Map which is accessed by a key of "collections".
     * This map contains entries with the following keys:
     * name of first collection
     * name of second collection
     * etc.
     * The corresponding value for each entry is a collection ExportMap.
     *
     * The collection element defines the name and description a
     * list of objects related to the business object.
     *
     * DD: See CollectionDefinition.java.
     *
     * JSTL: collection is a Map which is accessed using a key which is the
     * name of the collection.  Each entry contains the following keys:
     * name (String)
     * label (String)
     * shortLabel (String, copied from label if missing)
     * elementLabel (String, copied from contained class if missing)
     * summary (String)
     * description (String)
     *
     * See CollectionsMapBuilder.java.
     */
    public void setCollections(List<CollectionDefinition> collections) {
        collectionMap.clear();
        for (CollectionDefinition collection : collections) {
            if (collection == null) {
                throw new IllegalArgumentException("invalid (null) collectionDefinition");
            }
            String collectionName = collection.getName();
            if (StringUtils.isBlank(collectionName)) {
                throw new ValidationException("invalid (blank) collectionName");
            }

            if (collectionMap.containsKey(collectionName)) {
                throw new DuplicateEntryException("collection '"
                        + collectionName
                        + "' already defined for class '"
                        + getEntryClass().getName()
                        + "'");
            } else if (attributeMap.containsKey(collectionName)) {
                throw new DuplicateEntryException("collection '"
                        + collectionName
                        + "' already defined as an Attribute for class '"
                        + getEntryClass().getName()
                        + "'");
            } else if (complexAttributeMap.containsKey(collectionName)) {
                throw new DuplicateEntryException("collection '"
                        + collectionName
                        + "' already defined as Complex Attribute for class '"
                        + getEntryClass().getName()
                        + "'");
            }

            collectionMap.put(collectionName, collection);

        }
        this.collections = collections;
    }

    /**
     * The relationships element contains relationship elements.
     * These are used to map attribute names to fields in a reference object.
     *
     * JSTL: relationships is a Map which is accessed by a key of "relationships".
     * This map contains entries with the following keys:
     * objectAttributeName of first relationship
     * objectAttributeName of second relationship
     * etc.
     * The corresponding value for each entry is a relationship ExportMap.
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
     *
     * See RelationshipsMapBuilder.java.
     */
    public void setRelationships(List<RelationshipDefinition> relationships) {
        this.relationships = relationships;
    }

    public Set<String> getCollectionNames() {
        return collectionMap.keySet();
    }

    public Set<String> getAttributeNames() {
        return attributeMap.keySet();
    }

    public Set<String> getRelationshipNames() {
        return relationshipMap.keySet();
    }

    /**
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        if (relationships != null) {
            relationshipMap.clear();
            for (RelationshipDefinition relationship : relationships) {
                if (relationship == null) {
                    throw new IllegalArgumentException("invalid (null) relationshipDefinition");
                }
                String relationshipName = relationship.getObjectAttributeName();
                if (StringUtils.isBlank(relationshipName)) {
                    throw new ValidationException("invalid (blank) relationshipName");
                }
                relationship.setSourceClass(getEntryClass());
                relationshipMap.put(relationshipName, relationship);
            }
        }

        //Populate attributes with nested attribute definitions
        if (complexAttributes != null) {
            for (ComplexAttributeDefinition complexAttribute : complexAttributes) {
                addNestedAttributes(complexAttribute, complexAttribute.getName());
            }
        }
    }

    /**
     * recursively add complex attributes
     *
     * @param complexAttribute - the complex attribute to add recursively
     * @param attrPath - a string representation of specifically which attribute (at some depth) is being accessed
     */
    private void addNestedAttributes(ComplexAttributeDefinition complexAttribute, String attrPath) {
        DataDictionaryEntryBase dataDictionaryEntry = (DataDictionaryEntryBase) complexAttribute.getDataObjectEntry();

        //Add attributes for the complex attibutes
        for (AttributeDefinition attribute : dataDictionaryEntry.getAttributes()) {
            String nestedAttributeName = attrPath + "." + attribute.getName();
            AttributeDefinition nestedAttribute = copyAttributeDefinition(attribute);
            nestedAttribute.setName(nestedAttributeName);

            if (!attributeMap.containsKey(nestedAttributeName)) {
                this.attributes.add(nestedAttribute);
                this.attributeMap.put(nestedAttributeName, nestedAttribute);
            }
        }

        //Recursively add complex attributes
        List<ComplexAttributeDefinition> nestedComplexAttributes = dataDictionaryEntry.getComplexAttributes();
        if (nestedComplexAttributes != null) {
            for (ComplexAttributeDefinition nestedComplexAttribute : nestedComplexAttributes) {
                addNestedAttributes(nestedComplexAttribute, attrPath + "." + nestedComplexAttribute.getName());
            }
        }
    }

    /**
     * copy an attribute definition
     *
     * @param attrDefToCopy - the attribute to create a copy of
     * @return a copy of the attribute
     */
    private AttributeDefinition copyAttributeDefinition(AttributeDefinition attrDefToCopy) {
        AttributeDefinition attrDefCopy = new AttributeDefinition();

        try {
            BeanUtils.copyProperties(attrDefToCopy, attrDefCopy, new String[]{"formatterClass"});

            //BeanUtils doesn't copy properties w/o "get" read methods, manually copy those here
            attrDefCopy.setRequired(attrDefToCopy.isRequired());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return attrDefCopy;
    }

    /**
     * @see org.kuali.rice.krad.datadictionary.DataDictionaryEntry#getStateMapping()
     */
    @BeanTagAttribute(name = "stateMapping", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public StateMapping getStateMapping() {
        return stateMapping;
    }

    /**
     * @see DataDictionaryEntry#setStateMapping(org.kuali.rice.krad.datadictionary.state.StateMapping)
     */
    public void setStateMapping(StateMapping stateMapping) {
        this.stateMapping = stateMapping;
    }
}
