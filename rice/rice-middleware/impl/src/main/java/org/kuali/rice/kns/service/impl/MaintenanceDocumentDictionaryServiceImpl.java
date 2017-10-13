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
package org.kuali.rice.kns.service.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.doctype.DocumentType;
import org.kuali.rice.kns.datadictionary.MaintainableCollectionDefinition;
import org.kuali.rice.kns.datadictionary.MaintainableFieldDefinition;
import org.kuali.rice.kns.datadictionary.MaintainableItemDefinition;
import org.kuali.rice.kns.datadictionary.MaintainableSectionDefinition;
import org.kuali.rice.kns.datadictionary.MaintenanceDocumentEntry;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.kns.maintenance.Maintainable;
import org.kuali.rice.kns.maintenance.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.kns.service.MaintenanceDocumentDictionaryService;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.datadictionary.DataDictionary;
import org.kuali.rice.krad.rules.rule.BusinessRule;
import org.kuali.rice.krad.service.DataDictionaryService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.krad.valuefinder.ValueFinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * This class is the service implementation for the MaintenanceDocumentDictionary structure. Defines the API for the interacting
 * with Document-related entries in the data dictionary. This is the default implementation, that is delivered with Kuali.
 */
@Deprecated
public class MaintenanceDocumentDictionaryServiceImpl implements MaintenanceDocumentDictionaryService {
    protected static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(MaintenanceDocumentDictionaryServiceImpl.class);

    private DataDictionaryService dataDictionaryService;

    /**
     * Gets the workflow document type for the given documentTypeName
     * 
     * @param documentTypeName
     * @return
     */
    protected DocumentType getDocumentType(String documentTypeName) {
        return KewApiServiceLocator.getDocumentTypeService().getDocumentTypeByName(documentTypeName);
    }

    /**
     * @see org.kuali.rice.kns.service.MaintenanceDocumentDictionaryService#getMaintenanceLabel(java.lang.String)
     */
    public String getMaintenanceLabel(String docTypeName) {
        String label = null;

        DocumentType docType = getDocumentType(docTypeName);
        if (docType != null) {
            label = docType.getLabel();
        }

        return label;
    }

    /**
     * @see org.kuali.rice.kns.service.MaintenanceDocumentDictionaryService#getMaintenanceDescription(java.lang.String)
     */
    public String getMaintenanceDescription(String docTypeName) {
        String description = null;

        DocumentType docType = getDocumentType(docTypeName);
        if (docType != null) {
            description = docType.getDescription();
        }

        return description;
    }

    /**
     * @see org.kuali.rice.kns.service.MaintenanceDocumentDictionaryService#getMaintainableClass(java.lang.String)
     */
    @Deprecated
    public Class getMaintainableClass(String docTypeName) {
        Class maintainableClass = null;

        MaintenanceDocumentEntry entry = getMaintenanceDocumentEntry(docTypeName);
        if (entry != null) {
            LOG.debug("suppling a generic Rule to insure basic validation");
            maintainableClass = entry.getMaintainableClass();
        }

        return maintainableClass;
    }

    /**
     * @see org.kuali.rice.kns.service.MaintenanceDocumentDictionaryService#getDataObjectClass(java.lang.String)
     */
    public Class getDataObjectClass(String docTypeName) {
        Class dataObjectClass = null;

        MaintenanceDocumentEntry entry = getMaintenanceDocumentEntry(docTypeName);
        if (entry != null) {
            dataObjectClass = entry.getDataObjectClass();
        }

        return dataObjectClass;
    }

    /**
     * @see org.kuali.rice.kns.service.MaintenanceDocumentDictionaryService#getDocumentTypeName(java.lang.Class)
     */
    public String getDocumentTypeName(Class businessObjectClass) {
        String documentTypeName = null;

        MaintenanceDocumentEntry entry = getMaintenanceDocumentEntry(businessObjectClass);
        if (entry != null) {
            documentTypeName = entry.getDocumentTypeName();
        }

        return documentTypeName;
    }

    /**
     * @see org.kuali.rice.kns.service.MaintenanceDocumentDictionaryService#getMaintainableSections(java.lang.String)
     */
    @Deprecated
    public List getMaintainableSections(String docTypeName) {
        List sections = null;

        MaintenanceDocumentEntry entry = getMaintenanceDocumentEntry(docTypeName);
        if (entry != null) {
            sections = entry.getMaintainableSections();
        }

        return sections;
    }


    /**
     * @see org.kuali.rice.kns.service.MaintenanceDocumentDictionaryService#getDefaultExistenceChecks(java.lang.Class)
     */
    public Collection getDefaultExistenceChecks(Class businessObjectClass) {
        return getDefaultExistenceChecks(getDocumentTypeName(businessObjectClass));
    }

    /**
     * @see org.kuali.rice.kns.service.MaintenanceDocumentDictionaryService#getDefaultExistenceChecks(java.lang.String)
     */
    public Collection getDefaultExistenceChecks(String docTypeName) {

        Collection defaultExistenceChecks = null;

        MaintenanceDocumentEntry entry = getMaintenanceDocumentEntry(docTypeName);
        if (entry != null) {
            defaultExistenceChecks = entry.getDefaultExistenceChecks();
        }

        return defaultExistenceChecks;
    }

    /**
     * @see org.kuali.rice.kns.service.MaintenanceDocumentDictionaryService#getLockingKeys(java.lang.String)
     */
    public List getLockingKeys(String docTypeName) {
        List lockingKeys = null;

        MaintenanceDocumentEntry entry = getMaintenanceDocumentEntry(docTypeName);
        if (entry != null) {
            lockingKeys = entry.getLockingKeyFieldNames();
        }

        return lockingKeys;
    }

    /**
     * @param dataDictionaryService
     */
    public void setDataDictionaryService(DataDictionaryService dataDictionaryService) {
        this.dataDictionaryService = dataDictionaryService;
    }

    /**
     * @return
     */
    public DataDictionary getDataDictionary() {
        return this.dataDictionaryService.getDataDictionary();
    }

    /**
     * @param docTypeName
     * @return
     */
    public MaintenanceDocumentEntry getMaintenanceDocumentEntry(String docTypeName) {
        if (StringUtils.isBlank(docTypeName)) {
            throw new IllegalArgumentException("invalid (blank) docTypeName");
        }

        MaintenanceDocumentEntry entry = (MaintenanceDocumentEntry)getDataDictionary().getDocumentEntry(docTypeName);
        return entry;
    }

    private MaintenanceDocumentEntry getMaintenanceDocumentEntry(Class businessObjectClass) {
        if (businessObjectClass == null) {
            throw new IllegalArgumentException("invalid (blank) dataObjectClass");
        }

        // Treat KRAD documents as non existing (KULRICE-9909)
        org.kuali.rice.krad.datadictionary.MaintenanceDocumentEntry
                entry = getDataDictionary().getMaintenanceDocumentEntryForBusinessObjectClass(businessObjectClass);
        return (entry instanceof MaintenanceDocumentEntry) ? (MaintenanceDocumentEntry) entry : null;
    }

    /**
     * @see org.kuali.rice.kns.service.MaintenanceDocumentDictionaryService#getFieldDefaultValue(java.lang.Class, java.lang.String)
     */
    public String getFieldDefaultValue(Class boClass, String fieldName) {

        // input parameter validation
        if (boClass == null) {
            throw new IllegalArgumentException("The boClass parameter value specified was " + "null.  A valid class representing the boClass must " + "be specified.");
        }

        // call the twin
        return getFieldDefaultValue(getDocumentTypeName(boClass), fieldName);
    }

    /**
     * @see org.kuali.rice.kns.service.MaintenanceDocumentDictionaryService#getFieldDefaultValue(java.lang.String, java.lang.String)
     */
    public String getFieldDefaultValue(String docTypeName, String fieldName) {

        // input parameter validation
        if (StringUtils.isBlank(docTypeName)) {
            throw new IllegalArgumentException("The docTypeName parameter value specified was  " + "blank, whitespace, or null.  A valid string representing the docTypeName must " + "be specified.");
        }
        if (StringUtils.isBlank(fieldName)) {
            throw new IllegalArgumentException("The fieldName parameter value specified was  " + "blank, whitespace, or null.  A valid string representing the fieldName must " + "be specified.");
        }

        // walk through the sections
        List sections = getMaintainableSections(docTypeName);
        for (Iterator sectionIterator = sections.iterator(); sectionIterator.hasNext();) {
            MaintainableSectionDefinition section = (MaintainableSectionDefinition) sectionIterator.next();

            // walk through the fields
            Collection fields = section.getMaintainableItems();
            String defaultValue = getFieldDefaultValue(fields, fieldName);
            // need to keep trying sections until a match is found
            if (defaultValue != null) {
                return defaultValue;
            }
        }
        return null;
    }

    private String getFieldDefaultValue(Collection maintainableFields, String fieldName) {
        for (Iterator iterator = maintainableFields.iterator(); iterator.hasNext();) {
            MaintainableItemDefinition item = (MaintainableItemDefinition) iterator.next();
            // only check fields...skip subcollections
            if (item instanceof MaintainableFieldDefinition) {

                MaintainableFieldDefinition field = (MaintainableFieldDefinition) item;

                // if the field name matches
                if (field.getName().endsWith(fieldName)) {

                    // preferentially take the raw default value
                    if (StringUtils.isNotBlank(field.getDefaultValue())) {
                        return field.getDefaultValue();
                    }

                    // take the valuefinder
                    else if (field.getDefaultValueFinderClass() != null) {

                        // attempt to get an instance of the defaultValueFinderClass
                        ValueFinder valueFinder = null;
                        try {
                            valueFinder = (ValueFinder) field.getDefaultValueFinderClass().newInstance();
                        }
                        catch (Exception e) {
                            LOG.info("Exception obtaining valueFinder for collection field default value", e);
                            valueFinder = null;
                        }

                        // get the value
                        if (valueFinder != null) {
                            return valueFinder.getValue();
                        }
                    }
                    // if we found the field, but no default anything, then we're done
                    else {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    /**
     * @see org.kuali.rice.kns.service.MaintenanceDocumentDictionaryService#getCollectionFieldDefaultValue(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public String getCollectionFieldDefaultValue(String docTypeName, String collectionName, String fieldName) {
        // input parameter validation
        if (StringUtils.isBlank(docTypeName)) {
            throw new IllegalArgumentException("The docTypeName parameter value specified was blank, whitespace, or null.  A valid string representing the docTypeName must be specified.");
        }
        if (StringUtils.isBlank(fieldName)) {
            throw new IllegalArgumentException("The fieldName parameter value specified was blank, whitespace, or null.  A valid string representing the fieldName must be specified.");
        }
        if (StringUtils.isBlank(collectionName)) {
            throw new IllegalArgumentException("The collectionName parameter value specified was null.  A valid string representing the collectionName must be specified.");
        }

        MaintainableCollectionDefinition coll = getMaintainableCollection(docTypeName, collectionName);
        if (coll != null) {
            Collection collectionFields = coll.getMaintainableFields();
            return getFieldDefaultValue(collectionFields, fieldName);
        }
        return null;
    }

    /**
     * @see org.kuali.rice.kns.service.MaintenanceDocumentDictionaryService#getAllowsCopy(MaintenanceDocument)
     */
    public Boolean getAllowsCopy(MaintenanceDocument document) {
        Boolean allowsCopy = Boolean.FALSE;
        if (document != null && document.getNewMaintainableObject() != null) {
            MaintenanceDocumentEntry entry = getMaintenanceDocumentEntry(document.getNewMaintainableObject().getBoClass());
            if (entry != null) {
                allowsCopy = Boolean.valueOf(entry.getAllowsCopy());
            }
        }

        return allowsCopy;
    }

    /**
     * @see org.kuali.rice.kns.service.MaintenanceDocumentDictionaryService#getAllowsNewOrCopy(java.lang.String)
     */
    public Boolean getAllowsNewOrCopy(String docTypeName) {
        Boolean allowsNewOrCopy = Boolean.FALSE;

        if (docTypeName != null) {
            MaintenanceDocumentEntry entry = getMaintenanceDocumentEntry(docTypeName);
            if (entry != null) {
                allowsNewOrCopy = Boolean.valueOf(entry.getAllowsNewOrCopy());
            }
        }

        return allowsNewOrCopy;
    }

    public MaintainableItemDefinition getMaintainableItem(String docTypeName, String itemName) {
        // input parameter validation
        if (StringUtils.isBlank(docTypeName)) {
            throw new IllegalArgumentException("The docTypeName parameter value specified was  " + "blank, whitespace, or null.  A valid string representing the docTypeName must " + "be specified.");
        }
        if (StringUtils.isBlank(itemName)) {
            throw new IllegalArgumentException("The itemName parameter value specified was  " + "blank, whitespace, or null.  A valid string representing the itemName must " + "be specified.");
        }

        // split name for subcollections
        String[] subItems = {};
        subItems = StringUtils.split(itemName, ".");


        // walk through the sections
        List sections = getMaintainableSections(docTypeName);
        for (Iterator sectionIterator = sections.iterator(); sectionIterator.hasNext();) {
            MaintainableSectionDefinition section = (MaintainableSectionDefinition) sectionIterator.next();

            // walk through the fields
            Collection fields = section.getMaintainableItems();
            for (Iterator fieldIterator = fields.iterator(); fieldIterator.hasNext();) {
                MaintainableItemDefinition item = (MaintainableItemDefinition) fieldIterator.next();

                if (item.getName().equals(itemName)) {
                    return item;
                }
                // if collection check to see if it has sub collections
                // for now this only allows 1 level (i.e. a.b) it should be expanded at some point
                if (item instanceof MaintainableCollectionDefinition) {
                    MaintainableCollectionDefinition col = (MaintainableCollectionDefinition) item;
                    if ((subItems.length > 1) && (StringUtils.equals(col.getName(), subItems[0]))) {
                        for (Iterator<MaintainableCollectionDefinition> colIterator = col.getMaintainableCollections().iterator(); colIterator.hasNext();) {
                            MaintainableCollectionDefinition subCol = (MaintainableCollectionDefinition) colIterator.next();
                            if (subCol.getName().equals(subItems[1])) {
                                return subCol;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public MaintainableFieldDefinition getMaintainableField(String docTypeName, String fieldName) {
        MaintainableItemDefinition item = getMaintainableItem(docTypeName, fieldName);
        if (item != null && item instanceof MaintainableFieldDefinition) {
            return (MaintainableFieldDefinition) item;
        }
        return null;
    }

    public MaintainableCollectionDefinition getMaintainableCollection(String docTypeName, String collectionName) {
        // strip brackets as they are not needed to get to collection class
        // Like the other subcollections changes this currently only supports one sub level
        if (StringUtils.contains(collectionName, "[")) {
            collectionName = StringUtils.substringBefore(collectionName, "[") + StringUtils.substringAfter(collectionName, "]");
        }
        MaintainableItemDefinition item = getMaintainableItem(docTypeName, collectionName);
        if (item != null && item instanceof MaintainableCollectionDefinition) {
            return (MaintainableCollectionDefinition) item;
        }
        return null;
    }

    public Class getCollectionBusinessObjectClass(String docTypeName, String collectionName) {
        MaintainableCollectionDefinition coll = getMaintainableCollection(docTypeName, collectionName);
        if (coll != null) {
            return coll.getBusinessObjectClass();
        }
        return null;
    }

    public List<MaintainableCollectionDefinition> getMaintainableCollections(String docTypeName) {
        ArrayList<MaintainableCollectionDefinition> collections = new ArrayList<MaintainableCollectionDefinition>();

        // walk through the sections
        List sections = getMaintainableSections(docTypeName);
        for (Iterator sectionIterator = sections.iterator(); sectionIterator.hasNext();) {
            MaintainableSectionDefinition section = (MaintainableSectionDefinition) sectionIterator.next();

            // walk through the fields
            Collection fields = section.getMaintainableItems();
            for (Iterator fieldIterator = fields.iterator(); fieldIterator.hasNext();) {
                MaintainableItemDefinition item = (MaintainableItemDefinition) fieldIterator.next();

                if (item instanceof MaintainableCollectionDefinition) {
                    collections.add((MaintainableCollectionDefinition) item);
                    // collections.addAll( getMaintainableCollections( (MaintainableCollectionDefinition)item ) );
                }
            }
        }

        return collections;
    }

    public List<MaintainableCollectionDefinition> getMaintainableCollections(MaintainableCollectionDefinition parentCollection) {
        ArrayList<MaintainableCollectionDefinition> collections = new ArrayList<MaintainableCollectionDefinition>();

        // walk through the sections
        Collection<MaintainableCollectionDefinition> colls = parentCollection.getMaintainableCollections();
        for (MaintainableCollectionDefinition coll : colls) {
            collections.add(coll);
            collections.addAll(getMaintainableCollections(coll));
        }

        return collections;
    }

    /**
     * @see org.kuali.rice.kns.service.MaintenanceDocumentDictionaryService#validateMaintenanceRequiredFields(org.kuali.rice.krad.maintenance.MaintenanceDocument)
     */
    public void validateMaintenanceRequiredFields(MaintenanceDocument document) {
        Maintainable newMaintainableObject = document.getNewMaintainableObject();
        if (newMaintainableObject == null) {
            LOG.error("New maintainable is null");
            throw new RuntimeException("New maintainable is null");
        }

        List<MaintainableSectionDefinition> maintainableSectionDefinitions = getMaintainableSections(getDocumentTypeName(newMaintainableObject.getBoClass()));
        for (MaintainableSectionDefinition maintainableSectionDefinition : maintainableSectionDefinitions) {
            for (MaintainableItemDefinition maintainableItemDefinition : maintainableSectionDefinition.getMaintainableItems()) {
                // validate fields
                if (maintainableItemDefinition instanceof MaintainableFieldDefinition) {
                    validateMaintainableFieldRequiredFields((MaintainableFieldDefinition) maintainableItemDefinition, newMaintainableObject.getBusinessObject(), maintainableItemDefinition.getName());
                }
                // validate collections
                else if (maintainableItemDefinition instanceof MaintainableCollectionDefinition) {
                    validateMaintainableCollectionsRequiredFields(newMaintainableObject.getBusinessObject(), (MaintainableCollectionDefinition) maintainableItemDefinition);
                }
            }
        }
    }

    /**
     * generates error message if a field is marked as required but is not filled in
     * 
     * @param maintainableFieldDefinition
     * @param businessObject
     * @param fieldName
     */
    private void validateMaintainableFieldRequiredFields(MaintainableFieldDefinition maintainableFieldDefinition, PersistableBusinessObject businessObject, String fieldName) {

        if (StringUtils.isBlank(fieldName)) {
            throw new IllegalArgumentException("invalid fieldName parameter.");
        }
        // if required check we have a value for this field
        if (maintainableFieldDefinition.isRequired() && !maintainableFieldDefinition.isUnconditionallyReadOnly() ) {
            try {
                Object obj = ObjectUtils.getNestedValue(businessObject, fieldName);

                if (obj == null || StringUtils.isBlank(obj.toString())) {
                    String attributeLabel = dataDictionaryService.getAttributeLabel(businessObject.getClass(), fieldName);
                    String shortLabel = dataDictionaryService.getAttributeShortLabel(businessObject.getClass(), fieldName);
                    GlobalVariables.getMessageMap().putError(fieldName, RiceKeyConstants.ERROR_REQUIRED, attributeLabel + " (" + shortLabel + ")" );
                } else if ( fieldName.endsWith(".principalName") ) {
                    // special handling to catch when the principalName is not really a valid user
                    // pull the Person object and test the entity ID.  If that's null, then this
                    // is just a shell user instance and does not represent a true user
                    // the main principalId property on the main object would be null at this point
                    // but it is also unconditionally read only and not tested - checking that would
                    // require checking the relationships and be more complex than we want to get here
                    String personProperty = ObjectUtils.getNestedAttributePrefix(fieldName); 
                    if ( StringUtils.isNotBlank(personProperty) ) {
                        if ( StringUtils.isBlank( (String)ObjectUtils.getNestedValue(businessObject, personProperty+".entityId") ) ) {
                            String attributeLabel = dataDictionaryService.getAttributeLabel(businessObject.getClass(), fieldName);
                            GlobalVariables.getMessageMap().putError(fieldName, RiceKeyConstants.ERROR_EXISTENCE, attributeLabel );
                        }
                    }
                }
            } catch( Exception ex ) {
                LOG.error( "unable to read property during doc required field checks", ex );
            }
        }
    }

    
    private MaintainableCollectionDefinition getCollectionDefinition( String docTypeName, String collectionName ) {
        String currentCollection = collectionName;
        String nestedCollections = "";
    	if (StringUtils.contains(collectionName, "[")) {
    		// strip off any array indexes
            currentCollection = StringUtils.substringBefore( collectionName, "[" );
            nestedCollections = StringUtils.substringAfter( collectionName, "." );
    	}
    	
        // loop over all sections to find this collection
        List<MaintainableSectionDefinition> maintainableSectionDefinitions = getMaintainableSections( docTypeName );
        for (MaintainableSectionDefinition maintainableSectionDefinition : maintainableSectionDefinitions) {
            for (MaintainableItemDefinition maintainableItemDefinition : maintainableSectionDefinition.getMaintainableItems()) {
                if (maintainableItemDefinition instanceof MaintainableCollectionDefinition && maintainableItemDefinition.getName().equals( currentCollection ) ) {
                    if ( StringUtils.isBlank( nestedCollections ) ) {
                        return (MaintainableCollectionDefinition) maintainableItemDefinition;
                    } 
                    
                    return getCollectionDefinition( (MaintainableCollectionDefinition)maintainableItemDefinition, nestedCollections );
                }
            }
        }
        
        return null;
    }

    private MaintainableCollectionDefinition getCollectionDefinition( MaintainableCollectionDefinition collectionDef, String collectionName ) {
        String currentCollection = collectionName;
        String nestedCollections = "";
    	if (StringUtils.contains(collectionName, "[")) {
    		// strip off any array indexes
            currentCollection = StringUtils.substringBefore( collectionName, "[" );
            nestedCollections = StringUtils.substringAfter( collectionName, "." );
    	}
        
        // loop over all nested collections
        for (MaintainableCollectionDefinition maintainableCollectionDefinition : collectionDef.getMaintainableCollections()) {
            if ( maintainableCollectionDefinition.getName().equals( currentCollection ) ) {
                if ( StringUtils.isBlank( nestedCollections ) ) {
                    return maintainableCollectionDefinition;
                } 
                return getCollectionDefinition( maintainableCollectionDefinition, nestedCollections );
            }
        }
        
        return null;
    }
    
    public void validateMaintainableCollectionsAddLineRequiredFields(MaintenanceDocument document, PersistableBusinessObject businessObject, String collectionName ) {
        MaintainableCollectionDefinition def = getCollectionDefinition( getDocumentTypeName(businessObject.getClass()), collectionName );
        if ( def != null ) {
            validateMaintainableCollectionsAddLineRequiredFields( document, businessObject, collectionName, def, 0);
        }
    }
    /**
     * calls code to generate error messages if maintainableFields within any collections or sub-collections are marked as required
     * 
     * @param document
     * @param businessObject
     * @param collectionName
     * @param maintainableCollectionDefinition
     * @param depth
     */
    private void validateMaintainableCollectionsAddLineRequiredFields(MaintenanceDocument document, PersistableBusinessObject businessObject, String collectionName, MaintainableCollectionDefinition maintainableCollectionDefinition, int depth) {
        if ( depth == 0 ) {
            GlobalVariables.getMessageMap().addToErrorPath("add");
        }
        // validate required fields on fields withing collection definition
        PersistableBusinessObject element = document.getNewMaintainableObject().getNewCollectionLine( collectionName );
        GlobalVariables.getMessageMap().addToErrorPath(collectionName);
        for (MaintainableFieldDefinition maintainableFieldDefinition : maintainableCollectionDefinition.getMaintainableFields()) {
            final String fieldName = maintainableFieldDefinition.getName();
            validateMaintainableFieldRequiredFields(maintainableFieldDefinition, element, fieldName);
            
        }

        GlobalVariables.getMessageMap().removeFromErrorPath(collectionName);
        if ( depth == 0 ) {
            GlobalVariables.getMessageMap().removeFromErrorPath("add");
        }
    }

    /**
     * calls code to generate error messages if maintainableFields within any collections or sub-collections are marked as required
     * 
     * @param businessObject
     * @param maintainableCollectionDefinition
     */
    private void validateMaintainableCollectionsRequiredFields(PersistableBusinessObject businessObject, MaintainableCollectionDefinition maintainableCollectionDefinition) {
        final String collectionName = maintainableCollectionDefinition.getName();

        // validate required fields on fields withing collection definition
        Collection<PersistableBusinessObject> collection = (Collection) ObjectUtils.getPropertyValue(businessObject, collectionName);
        if (collection != null && !collection.isEmpty()) {
            for (MaintainableFieldDefinition maintainableFieldDefinition : maintainableCollectionDefinition.getMaintainableFields()) {
                int pos = 0;
                final String fieldName = maintainableFieldDefinition.getName();
                for (PersistableBusinessObject element : collection) {
                    String parentName = collectionName + "[" + (pos++) + "]";
                    GlobalVariables.getMessageMap().addToErrorPath(parentName);
                    validateMaintainableFieldRequiredFields(maintainableFieldDefinition, element, fieldName);
                    GlobalVariables.getMessageMap().removeFromErrorPath(parentName);
                }
            }

            // recursivley validate required fields on subcollections
            GlobalVariables.getMessageMap().addToErrorPath(collectionName);
            for (MaintainableCollectionDefinition nestedMaintainableCollectionDefinition : maintainableCollectionDefinition.getMaintainableCollections()) {
                for (PersistableBusinessObject element : collection) {
                    validateMaintainableCollectionsRequiredFields(element, nestedMaintainableCollectionDefinition);
                }
            }
            GlobalVariables.getMessageMap().removeFromErrorPath(collectionName);
        }
    }
    
    /**
     * default implementation checks for duplicats based on keys of objects only
     * 
     * @see org.kuali.rice.kns.service.MaintenanceDocumentDictionaryService#validateMaintainableCollectionsForDuplicateEntries(org.kuali.rice.krad.maintenance.MaintenanceDocument)
     */
    public void validateMaintainableCollectionsForDuplicateEntries(MaintenanceDocument document) {
        Maintainable newMaintainableObject = document.getNewMaintainableObject();
        if (newMaintainableObject == null) {
            LOG.error("New maintainable is null");
            throw new RuntimeException("New maintainable is null");
        }

        List<MaintainableSectionDefinition> maintainableSectionDefinitions = getMaintainableSections(getDocumentTypeName(newMaintainableObject.getBoClass()));
        for (MaintainableSectionDefinition maintainableSectionDefinition : maintainableSectionDefinitions) {
            for (MaintainableItemDefinition maintainableItemDefinition : maintainableSectionDefinition.getMaintainableItems()) {
                // validate collections
                if (maintainableItemDefinition instanceof MaintainableCollectionDefinition) {
                    validateMaintainableCollectionsForDuplicateEntries(newMaintainableObject.getBusinessObject(), (MaintainableCollectionDefinition) maintainableItemDefinition);
                }
            }
        }
    }

    /**
     * recursivly checks collections for duplicate entries based on key valuse
     * 
     * @param businessObject
     * @param maintainableCollectionDefinition
     */
    private void validateMaintainableCollectionsForDuplicateEntries(PersistableBusinessObject businessObject, MaintainableCollectionDefinition maintainableCollectionDefinition) {
        final String collectionName = maintainableCollectionDefinition.getName();

        if (maintainableCollectionDefinition.dissallowDuplicateKey()) {
            final Class maintainableBusinessObjectClass = businessObject.getClass();
            // validate that no duplicates based on keys exist
            Collection<PersistableBusinessObject> collection = (Collection) ObjectUtils.getPropertyValue(businessObject, collectionName);
            if (collection != null && !collection.isEmpty()) {
                final String propertyName = maintainableCollectionDefinition.getAttributeToHighlightOnDuplicateKey();
                // get collection label for dd
                final String label = dataDictionaryService.getCollectionLabel(maintainableBusinessObjectClass, collectionName);
                final String shortLabel = dataDictionaryService.getCollectionShortLabel(maintainableBusinessObjectClass, collectionName);
                int pos = 0;
                for (PersistableBusinessObject element : collection) {
                    String pathToElement = collectionName + "[" + (pos++) + "]";
                    if (ObjectUtils.countObjectsWithIdentitcalKey(collection, element) > 1) {
                        GlobalVariables.getMessageMap().addToErrorPath(pathToElement);
                        GlobalVariables.getMessageMap().putError(propertyName, RiceKeyConstants.ERROR_DUPLICATE_ELEMENT, new String[] { label, shortLabel });
                        GlobalVariables.getMessageMap().removeFromErrorPath(pathToElement);
                    }
                }

                // recursivley check for duplicate entries on subcollections
                GlobalVariables.getMessageMap().addToErrorPath(collectionName);
                for (MaintainableCollectionDefinition nestedMaintainableCollectionDefinition : maintainableCollectionDefinition.getMaintainableCollections()) {
                    for (PersistableBusinessObject element : collection) {
                        validateMaintainableCollectionsForDuplicateEntries(element, nestedMaintainableCollectionDefinition);
                    }
                }
                GlobalVariables.getMessageMap().removeFromErrorPath(collectionName);

            }
        }
    }
       
	/**
	 * for issue KULRice 3072
	 * 
	 * @see org.kuali.rice.kns.service.MaintenanceDocumentDictionaryService#getgetPreserveLockingKeysOnCopy(java.lang.Class)
	 */
	public boolean getPreserveLockingKeysOnCopy(Class businessObjectClass) {

		boolean preserveLockingKeysOnCopy = false;

		MaintenanceDocumentEntry docEntry = getMaintenanceDocumentEntry(businessObjectClass);
		
		if (docEntry != null) {
			preserveLockingKeysOnCopy = docEntry.getPreserveLockingKeysOnCopy();
		}
		
		return preserveLockingKeysOnCopy;
	}

	/**
	 * for isue KULRice 3070
	 * 
	 * @see org.kuali.rice.kns.service.MaintenanceDocumentDictionaryService#getAllowsRecordDeletion(java.lang.Class)
	 */
	public Boolean getAllowsRecordDeletion(Class businessObjectClass) {
		
		Boolean allowsRecordDeletion = Boolean.FALSE;

		MaintenanceDocumentEntry docEntry = getMaintenanceDocumentEntry(businessObjectClass);
		
		if (docEntry != null) {
			allowsRecordDeletion = Boolean.valueOf(docEntry.getAllowsRecordDeletion());
		}
		
		return allowsRecordDeletion;
	}

	/**
	 *  for issue KULRice3070, see if need delete button
	 * 
	 * @see org.kuali.rice.kns.service.MaintenanceDocumentDictionaryService#getAllowsRecordDeletion(org.kuali.rice.krad.maintenance.MaintenanceDocument)
	 */
	public Boolean getAllowsRecordDeletion(MaintenanceDocument document) {
        return document != null ? this.getAllowsRecordDeletion(document.getNewMaintainableObject().getBoClass()) : Boolean.FALSE;
	}

	/**
	 * @see org.kuali.rice.kns.service.MaintenanceDocumentDictionaryService#translateCodes(java.lang.Class)
	 */
	public Boolean translateCodes(Class businessObjectClass) {
		boolean translateCodes = false;

		MaintenanceDocumentEntry docEntry = getMaintenanceDocumentEntry(businessObjectClass);

		if (docEntry != null) {
			translateCodes = docEntry.isTranslateCodes();
		}

		return translateCodes;
	}

}
