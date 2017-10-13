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
package org.kuali.rice.kns.workflow.attribute;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.document.DocumentWithContent;
import org.kuali.rice.kew.api.document.attribute.DocumentAttribute;
import org.kuali.rice.kew.api.document.attribute.DocumentAttributeFactory;
import org.kuali.rice.kew.api.document.attribute.DocumentAttributeString;
import org.kuali.rice.kew.api.document.attribute.WorkflowAttributeDefinition;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.api.extension.ExtensionDefinition;
import org.kuali.rice.kew.framework.document.attribute.SearchableAttribute;
import org.kuali.rice.kns.datadictionary.MaintenanceDocumentEntry;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.kns.lookup.LookupUtils;
import org.kuali.rice.kns.maintenance.KualiGlobalMaintainableImpl;
import org.kuali.rice.kns.maintenance.Maintainable;
import org.kuali.rice.kns.service.DictionaryValidationService;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.kns.util.FieldUtils;
import org.kuali.rice.kns.web.ui.Field;
import org.kuali.rice.kns.web.ui.Row;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.bo.DocumentHeader;
import org.kuali.rice.krad.bo.GlobalBusinessObject;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.datadictionary.BusinessObjectEntry;
import org.kuali.rice.krad.datadictionary.DocumentEntry;
import org.kuali.rice.krad.datadictionary.SearchingAttribute;
import org.kuali.rice.krad.datadictionary.SearchingTypeDefinition;
import org.kuali.rice.krad.datadictionary.WorkflowAttributes;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.util.ErrorMessage;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADPropertyConstants;
import org.kuali.rice.krad.util.MessageMap;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.kns.service.WorkflowAttributePropertyResolutionService;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DataDictionarySearchableAttribute implements SearchableAttribute {

    private static final long serialVersionUID = 173059488280366451L;
	private static final Logger LOG = Logger.getLogger(DataDictionarySearchableAttribute.class);
    public static final String DATA_TYPE_BOOLEAN = "boolean";

    @Override
    public String generateSearchContent(ExtensionDefinition extensionDefinition,
            String documentTypeName,
            WorkflowAttributeDefinition attributeDefinition) {
        return "";
    }

    @Override
    public List<DocumentAttribute> extractDocumentAttributes(ExtensionDefinition extensionDefinition,
            DocumentWithContent documentWithContent) {
        List<DocumentAttribute> attributes = new ArrayList<DocumentAttribute>();

        String docId = documentWithContent.getDocument().getDocumentId();

        DocumentService docService = KRADServiceLocatorWeb.getDocumentService();
        Document doc = null;
        try  {
            doc = docService.getByDocumentHeaderIdSessionless(docId);
        } catch (WorkflowException we) {
        	LOG.error( "Unable to retrieve document " + docId + " in getSearchStorageValues()", we);
        }

        String attributeValue = "";
        if ( doc != null ) {
        	if ( doc.getDocumentHeader() != null ) {
                attributeValue = doc.getDocumentHeader().getDocumentDescription();
        	} else {
        		attributeValue = "null document header";
        	}
        } else {
    		attributeValue = "null document";
        }
        DocumentAttributeString attribute = DocumentAttributeFactory.createStringAttribute("documentDescription", attributeValue);
        attributes.add(attribute);

        attributeValue = "";
        if ( doc != null ) {
        	if ( doc.getDocumentHeader() != null ) {
                attributeValue = doc.getDocumentHeader().getOrganizationDocumentNumber();
        	} else {
        		attributeValue = "null document header";
        	}
        } else {
    		attributeValue = "null document";
        }
        attribute = DocumentAttributeFactory.createStringAttribute("organizationDocumentNumber", attributeValue);
        attributes.add(attribute);

        if ( doc != null && doc instanceof MaintenanceDocument) {
            final Class<? extends BusinessObject> businessObjectClass = getBusinessObjectClass(
                    documentWithContent.getDocument().getDocumentTypeName());
            if (businessObjectClass != null) {
                if (GlobalBusinessObject.class.isAssignableFrom(businessObjectClass)) {
                    final GlobalBusinessObject globalBO = retrieveGlobalBusinessObject(docId, businessObjectClass);

                    if (globalBO != null) {
                        attributes.addAll(findAllDocumentAttributesForGlobalBusinessObject(globalBO));
                    }
                } else {
                    attributes.addAll(parsePrimaryKeyValuesFromDocument(businessObjectClass, (MaintenanceDocument)doc));
                }

            }
        }
        if ( doc != null ) {
            DocumentEntry docEntry = KRADServiceLocatorWeb.getDataDictionaryService().getDataDictionary().getDocumentEntry(
                    documentWithContent.getDocument().getDocumentTypeName());
            if ( docEntry != null ) {
		        WorkflowAttributes workflowAttributes = docEntry.getWorkflowAttributes();
		        WorkflowAttributePropertyResolutionService waprs = KNSServiceLocator
                        .getWorkflowAttributePropertyResolutionService();
		        attributes.addAll(waprs.resolveSearchableAttributeValues(doc, workflowAttributes));
            } else {
            	LOG.error("Unable to find DD document entry for document type: " + documentWithContent.getDocument()
                        .getDocumentTypeName());
            }
        }
        return attributes;
    }

    @Override
    public List<RemotableAttributeField> getSearchFields(ExtensionDefinition extensionDefinition, String documentTypeName) {
        List<Row> searchRows = getSearchingRows(documentTypeName);
        return FieldUtils.convertRowsToAttributeFields(searchRows);
    }

    /**
     * Produces legacy KNS rows to use for search attributes.  This method was left intact to help ease conversion
     * until KNS is replaced with KRAD.
     */
    protected List<Row> getSearchingRows(String documentTypeName) {

        List<Row> docSearchRows = new ArrayList<Row>();

        Class boClass = DocumentHeader.class;

        Field descriptionField = FieldUtils.getPropertyField(boClass, "documentDescription", true);
        descriptionField.setFieldDataType(KewApiConstants.SearchableAttributeConstants.DATA_TYPE_STRING);

        Field orgDocNumberField = FieldUtils.getPropertyField(boClass, "organizationDocumentNumber", true);
        orgDocNumberField.setFieldDataType(KewApiConstants.SearchableAttributeConstants.DATA_TYPE_STRING);

        List<Field> fieldList = new ArrayList<Field>();
        fieldList.add(descriptionField);
        docSearchRows.add(new Row(fieldList));

        fieldList = new ArrayList<Field>();
        fieldList.add(orgDocNumberField);
        docSearchRows.add(new Row(fieldList));


        DocumentEntry entry =
                KRADServiceLocatorWeb.getDataDictionaryService().getDataDictionary().getDocumentEntry(documentTypeName);
        if (entry  == null)
            return docSearchRows;
        if (entry instanceof MaintenanceDocumentEntry) {
            Class<? extends BusinessObject> businessObjectClass = getBusinessObjectClass(documentTypeName);
            Class<? extends Maintainable> maintainableClass = getMaintainableClass(documentTypeName);

            KualiGlobalMaintainableImpl globalMaintainable = null;
            try {
                globalMaintainable = (KualiGlobalMaintainableImpl)maintainableClass.newInstance();
                businessObjectClass = globalMaintainable.getPrimaryEditedBusinessObjectClass();
            } catch (Exception ie) {
                //was not a globalMaintainable.
            }

            if (businessObjectClass != null)
                docSearchRows.addAll(createFieldRowsForBusinessObject(businessObjectClass));
        }

        WorkflowAttributes workflowAttributes = entry.getWorkflowAttributes();
        if (workflowAttributes != null)
            docSearchRows.addAll(createFieldRowsForWorkflowAttributes(workflowAttributes));

        return docSearchRows;
    }

    @Override
    public List<RemotableAttributeError> validateDocumentAttributeCriteria(ExtensionDefinition extensionDefinition,
            DocumentSearchCriteria documentSearchCriteria) {
        List<RemotableAttributeError> validationErrors = new ArrayList<RemotableAttributeError>();
        DictionaryValidationService validationService = KNSServiceLocator.getKNSDictionaryValidationService();

        // validate the document attribute values
        Map<String, List<String>> documentAttributeValues = documentSearchCriteria.getDocumentAttributeValues();
        for (String key : documentAttributeValues.keySet()) {
            List<String> values = documentAttributeValues.get(key);
            if (CollectionUtils.isNotEmpty(values)) {
                for (String value : values) {
                    if (StringUtils.isNotBlank(value)) {
                        validationService.validateAttributeFormat(documentSearchCriteria.getDocumentTypeName(), key, value, key);
                    }
                }
            }
        }

        retrieveValidationErrorsFromGlobalVariables(validationErrors);

        return validationErrors;
    }

    /**
     * Retrieves validation errors from GlobalVariables MessageMap and appends to the given list of RemotableAttributeError
     * @param validationErrors list to append validation errors
     */
    protected void retrieveValidationErrorsFromGlobalVariables(List<RemotableAttributeError> validationErrors) {
        // can we use KualiConfigurationService?  It seemed to be used elsewhere...
        ConfigurationService configurationService = CoreApiServiceLocator.getKualiConfigurationService();

        if(GlobalVariables.getMessageMap().hasErrors()){
            MessageMap deepCopy = (MessageMap)ObjectUtils.deepCopy(GlobalVariables.getMessageMap());
            for (String errorKey : deepCopy.getErrorMessages().keySet()) {
                List<ErrorMessage> errorMessages = deepCopy.getErrorMessages().get(errorKey);
                if (CollectionUtils.isNotEmpty(errorMessages)) {
                    List<String> errors = new ArrayList<String>();
                    for (ErrorMessage errorMessage : errorMessages) {
                        // need to materialize the message from it's parameters so we can send it back to the framework
                        String error = MessageFormat.format(configurationService.getPropertyValueAsString(errorMessage.getErrorKey()), errorMessage.getMessageParameters());
                        errors.add(error);
                    }
                    RemotableAttributeError remotableAttributeError = RemotableAttributeError.Builder.create(errorKey, errors).build();
                    validationErrors.add(remotableAttributeError);
                }
            }
            // we should now strip the error messages from the map because they have moved to validationErrors
            GlobalVariables.getMessageMap().clearErrorMessages();
        }
    }

    protected List<Row> createFieldRowsForWorkflowAttributes(WorkflowAttributes attrs) {
        List<Row> searchFields = new ArrayList<Row>();

        List<SearchingTypeDefinition> searchingTypeDefinitions = attrs.getSearchingTypeDefinitions();
        final WorkflowAttributePropertyResolutionService propertyResolutionService = KNSServiceLocator
                .getWorkflowAttributePropertyResolutionService();
        for (SearchingTypeDefinition definition: searchingTypeDefinitions) {
            SearchingAttribute attr = definition.getSearchingAttribute();

            final String attributeName = attr.getAttributeName();
            final String businessObjectClassName = attr.getBusinessObjectClassName();
            Class boClass = null;
            BusinessObject businessObject  = null;
            try {
                boClass = Class.forName(businessObjectClassName);
                businessObject = (BusinessObject)boClass.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            Field searchField = FieldUtils.getPropertyField(boClass, attributeName, false);
            // prepend all document attribute field names with "documentAttribute."
            //searchField.setPropertyName(KewApiConstants.DOCUMENT_ATTRIBUTE_FIELD_PREFIX + searchField.getPropertyName());
            searchField.setColumnVisible(attr.isShowAttributeInResultSet());

            //TODO this is a workaround to hide the Field from the search criteria.
            //This should be removed once hiding the entire Row is working
            if (!attr.isShowAttributeInSearchCriteria()){
                searchField.setFieldType(Field.HIDDEN);
            }
            String fieldDataType = propertyResolutionService.determineFieldDataType(boClass, attributeName);
            if (fieldDataType.equals(DataDictionarySearchableAttribute.DATA_TYPE_BOOLEAN)) {
                fieldDataType = KewApiConstants.SearchableAttributeConstants.DATA_TYPE_STRING;
            }

            // Allow inline range searching on dates and numbers
            if (fieldDataType.equals(KewApiConstants.SearchableAttributeConstants.DATA_TYPE_FLOAT) ||
                fieldDataType.equals(KewApiConstants.SearchableAttributeConstants.DATA_TYPE_LONG) ||
                fieldDataType.equals(KewApiConstants.SearchableAttributeConstants.DATA_TYPE_DATE)) {

                searchField.setAllowInlineRange(true);
            }
            searchField.setFieldDataType(fieldDataType);
            List displayedFieldNames = new ArrayList();
            displayedFieldNames.add(attributeName);
            LookupUtils.setFieldQuickfinder(businessObject, attributeName, searchField, displayedFieldNames);

            List<Field> fieldList = new ArrayList<Field>();
            fieldList.add(searchField);

            Row row = new Row(fieldList);
            if (!attr.isShowAttributeInSearchCriteria()) {
                row.setHidden(true);
            }
            searchFields.add(row);
        }

        return searchFields;
    }

    protected List<DocumentAttribute> parsePrimaryKeyValuesFromDocument(Class<? extends BusinessObject> businessObjectClass, MaintenanceDocument document) {
        List<DocumentAttribute> values = new ArrayList<DocumentAttribute>();

        final List primaryKeyNames = KNSServiceLocator.getBusinessObjectMetaDataService().listPrimaryKeyFieldNames(businessObjectClass);

        for (Object primaryKeyNameAsObj : primaryKeyNames) {
            final String primaryKeyName = (String)primaryKeyNameAsObj;
            final DocumentAttribute searchableValue = parseSearchableAttributeValueForPrimaryKey(primaryKeyName, businessObjectClass, document);
            if (searchableValue != null) {
                values.add(searchableValue);
            }
        }
        return values;
    }

    /**
     * Creates a searchable attribute value for the given property name out of the document XML
     * @param propertyName the name of the property to return
     * @param businessObjectClass the class of the business object maintained
     * @param document the document XML
     * @return a generated SearchableAttributeValue, or null if a value could not be created
     */
    protected DocumentAttribute parseSearchableAttributeValueForPrimaryKey(String propertyName, Class<? extends BusinessObject> businessObjectClass, MaintenanceDocument document) {

        Maintainable maintainable  = document.getNewMaintainableObject();
        PersistableBusinessObject bo = maintainable.getBusinessObject();

        final Object propertyValue = ObjectUtils.getPropertyValue(bo, propertyName);
        if (propertyValue == null) return null;

        final WorkflowAttributePropertyResolutionService propertyResolutionService = KNSServiceLocator
                .getWorkflowAttributePropertyResolutionService();
        DocumentAttribute value = propertyResolutionService.buildSearchableAttribute(businessObjectClass, propertyName, propertyValue);
        return value;
    }

    /**
     * Returns the class of the object being maintained by the given maintenance document type name
     * @param documentTypeName the name of the document type to look up the maintained business object for
     * @return the class of the maintained business object
     */
    protected Class<? extends BusinessObject> getBusinessObjectClass(String documentTypeName) {
        MaintenanceDocumentEntry entry = retrieveMaintenanceDocumentEntry(documentTypeName);
        return (entry == null ? null : (Class<? extends BusinessObject>) entry.getDataObjectClass());
    }

    /**
     * Returns the maintainable of the object being maintained by the given maintenance document type name
     * @param documentTypeName the name of the document type to look up the maintained business object for
     * @return the Maintainable of the maintained business object
     */
    protected Class<? extends Maintainable> getMaintainableClass(String documentTypeName) {
        MaintenanceDocumentEntry entry = retrieveMaintenanceDocumentEntry(documentTypeName);
        return (entry == null ? null : entry.getMaintainableClass());
    }


    /**
     * Retrieves the maintenance document entry for the given document type name
     * @param documentTypeName the document type name to look up the data dictionary document entry for
     * @return the corresponding data dictionary entry for a maintenance document
     */
    protected MaintenanceDocumentEntry retrieveMaintenanceDocumentEntry(String documentTypeName) {
        return (MaintenanceDocumentEntry) KRADServiceLocatorWeb.getDataDictionaryService().getDataDictionary().getDocumentEntry(documentTypeName);
    }

    protected GlobalBusinessObject retrieveGlobalBusinessObject(String documentNumber, Class<? extends BusinessObject> businessObjectClass) {
        GlobalBusinessObject globalBO = null;

        Map pkMap = new LinkedHashMap();
        pkMap.put(KRADPropertyConstants.DOCUMENT_NUMBER, documentNumber);

        List returnedBOs = (List) KRADServiceLocator.getBusinessObjectService().findMatching(businessObjectClass, pkMap);
        if (returnedBOs.size() > 0) {
            globalBO = (GlobalBusinessObject)returnedBOs.get(0);
        }

        return globalBO;
    }

    protected List<DocumentAttribute> findAllDocumentAttributesForGlobalBusinessObject(GlobalBusinessObject globalBO) {
        List<DocumentAttribute> searchValues = new ArrayList<DocumentAttribute>();

        for (PersistableBusinessObject bo : globalBO.generateGlobalChangesToPersist()) {
            DocumentAttribute value = generateSearchableAttributeFromChange(bo);
            if (value != null) {
                searchValues.add(value);
            }
        }

        return searchValues;
    }

    protected DocumentAttribute generateSearchableAttributeFromChange(PersistableBusinessObject changeToPersist) {
        List<String> primaryKeyNames = KNSServiceLocator.getBusinessObjectMetaDataService().listPrimaryKeyFieldNames(changeToPersist.getClass());

        for (Object primaryKeyNameAsObject : primaryKeyNames) {
            String primaryKeyName = (String)primaryKeyNameAsObject;
            Object value = ObjectUtils.getPropertyValue(changeToPersist, primaryKeyName);

            if (value != null) {
                final WorkflowAttributePropertyResolutionService propertyResolutionService = KNSServiceLocator
                        .getWorkflowAttributePropertyResolutionService();
                DocumentAttribute saValue = propertyResolutionService.buildSearchableAttribute(changeToPersist.getClass(), primaryKeyName, value);
                return saValue;
            }
        }
        return null;
    }

    /**
     * Creates a list of search fields, one for each primary key of the maintained business object
     * @param businessObjectClass the class of the maintained business object
     * @return a List of KEW search fields
     */
    protected List<Row> createFieldRowsForBusinessObject(Class<? extends BusinessObject> businessObjectClass) {
        List<Row> searchFields = new ArrayList<Row>();

        final List primaryKeyNamesAsObjects = KNSServiceLocator.getBusinessObjectMetaDataService().listPrimaryKeyFieldNames(businessObjectClass);
        final BusinessObjectEntry boEntry = KRADServiceLocatorWeb.getDataDictionaryService().getDataDictionary().getBusinessObjectEntry(businessObjectClass.getName());
        final WorkflowAttributePropertyResolutionService propertyResolutionService = KNSServiceLocator
                .getWorkflowAttributePropertyResolutionService();
        for (Object primaryKeyNameAsObject : primaryKeyNamesAsObjects) {

            String attributeName =  (String)primaryKeyNameAsObject;
            BusinessObject businessObject = null;
            try {
                businessObject = businessObjectClass.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            Field searchField = FieldUtils.getPropertyField(businessObjectClass, attributeName, false);
            String dataType = propertyResolutionService.determineFieldDataType(businessObjectClass, attributeName);
            searchField.setFieldDataType(dataType);
            List<Field> fieldList = new ArrayList<Field>();

            List displayedFieldNames = new ArrayList();
            displayedFieldNames.add(attributeName);
            LookupUtils.setFieldQuickfinder(businessObject, attributeName, searchField, displayedFieldNames);

            fieldList.add(searchField);
            searchFields.add(new Row(fieldList));
        }

        return searchFields;
    }

}
