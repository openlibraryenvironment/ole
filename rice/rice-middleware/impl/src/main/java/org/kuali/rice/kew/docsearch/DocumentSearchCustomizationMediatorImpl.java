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
package org.kuali.rice.kew.docsearch;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.api.document.search.DocumentSearchResults;
import org.kuali.rice.kew.framework.document.search.AttributeFields;
import org.kuali.rice.kew.framework.document.search.DocumentSearchCriteriaConfiguration;
import org.kuali.rice.kew.framework.document.search.DocumentSearchResultSetConfiguration;
import org.kuali.rice.kew.framework.document.search.DocumentSearchResultValues;
import org.kuali.rice.kew.doctype.DocumentTypeAttributeBo;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.framework.KewFrameworkServiceLocator;
import org.kuali.rice.kew.framework.document.search.DocumentSearchCustomization;
import org.kuali.rice.kew.framework.document.search.DocumentSearchCustomizationHandlerService;
import org.kuali.rice.kew.rule.bo.RuleAttribute;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Reference implementation of {@code DocumentSearchCustomizationMediator}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentSearchCustomizationMediatorImpl implements DocumentSearchCustomizationMediator {

    @Override
    public DocumentSearchCriteriaConfiguration getDocumentSearchCriteriaConfiguration(DocumentType documentType) {

        List<DocumentTypeAttributeBo> searchableAttributes = documentType.getSearchableAttributes();

        // This first map is used to partition our attributes by application id.  It maps an application id to the
        // list of searchable attribute names that are associated with that application id.  Note that 'null' is a
        // valid key in this map for those attributes that have no application id.
        LinkedHashMap<String, List<String>> applicationIdToAttributeNameMap = new LinkedHashMap<String, List<String>>();

        // This second map is used to map the searchable attribute name to the List of RemotableAttributeFields
        // that are returned by invocations of it's getSearchFields method.  This is a LinkedHashMap because it
        // preserves the order of the keys as they are entered.  This allows us to return attribute fields in the
        // proper order as defined by the order of searchable attributes on the doc type, despite the partitioning
        // of our attributes by application id.
        LinkedHashMap<String, AttributeFields> orderedSearchFieldMap = new LinkedHashMap<String, AttributeFields>();
        LinkedHashMap<String, AttributeFields> orderedResultSetFieldMap = new LinkedHashMap<String, AttributeFields>();

        for (DocumentTypeAttributeBo searchableAttribute : searchableAttributes) {
            RuleAttribute ruleAttribute = searchableAttribute.getRuleAttribute();
            String attributeName = ruleAttribute.getName();
            String applicationId = ruleAttribute.getApplicationId();
            if (!applicationIdToAttributeNameMap.containsKey(applicationId)) {
                applicationIdToAttributeNameMap.put(applicationId, new ArrayList<String>());
            }
            applicationIdToAttributeNameMap.get(applicationId).add(attributeName);
            // reserve a spot in the field map
            orderedSearchFieldMap.put(attributeName, null);
        }

        for (String applicationId : applicationIdToAttributeNameMap.keySet()) {
            DocumentSearchCustomizationHandlerService documentSearchCustomizationService = loadCustomizationService(
                    applicationId);
            List<String> searchableAttributeNames = applicationIdToAttributeNameMap.get(applicationId);
            DocumentSearchCriteriaConfiguration documentSearchConfiguration = documentSearchCustomizationService.getDocumentSearchConfiguration(
                    documentType.getName(), searchableAttributeNames);
            mergeAttributeFields(documentSearchConfiguration.getSearchAttributeFields(), orderedSearchFieldMap);
        }

        DocumentSearchCriteriaConfiguration.Builder configBuilder = DocumentSearchCriteriaConfiguration.Builder.create();
        configBuilder.setSearchAttributeFields(flattenOrderedFieldMap(orderedSearchFieldMap));
        return configBuilder.build();
    }

    @Override
    public List<RemotableAttributeError> validateLookupFieldParameters(DocumentType documentType,
            DocumentSearchCriteria documentSearchCriteria) {

        List<DocumentTypeAttributeBo> searchableAttributes = documentType.getSearchableAttributes();
        LinkedHashMap<String, List<String>> applicationIdToAttributeNameMap = new LinkedHashMap<String, List<String>>();

        for (DocumentTypeAttributeBo searchableAttribute : searchableAttributes) {
            RuleAttribute ruleAttribute = searchableAttribute.getRuleAttribute();
            String attributeName = ruleAttribute.getName();
            String applicationId = ruleAttribute.getApplicationId();
            if (!applicationIdToAttributeNameMap.containsKey(applicationId)) {
                applicationIdToAttributeNameMap.put(applicationId, new ArrayList<String>());
            }
            applicationIdToAttributeNameMap.get(applicationId).add(attributeName);
        }

        List<RemotableAttributeError> errors = new ArrayList<RemotableAttributeError>();
        for (String applicationId : applicationIdToAttributeNameMap.keySet()) {
            DocumentSearchCustomizationHandlerService documentSearchCustomizationService = loadCustomizationService(applicationId);
            List<String> searchableAttributeNames = applicationIdToAttributeNameMap.get(applicationId);
            List<RemotableAttributeError> searchErrors = documentSearchCustomizationService.validateCriteria(
                    documentSearchCriteria, searchableAttributeNames);
            if (!CollectionUtils.isEmpty(searchErrors)) {
                errors.addAll(searchErrors);
            }
        }

        return errors;
    }

    @Override
    public DocumentSearchCriteria customizeCriteria(DocumentType documentType, DocumentSearchCriteria documentSearchCriteria) {
        DocumentTypeAttributeBo customizerAttribute = documentType.getCustomizerAttribute();
        if (customizerAttribute != null) {
            DocumentSearchCustomizationHandlerService service = loadCustomizationService(customizerAttribute.getRuleAttribute().getApplicationId());
            if (service.getEnabledCustomizations(documentType.getName(), customizerAttribute.getRuleAttribute().getName()).contains(
                    DocumentSearchCustomization.CRITERIA)) {
                DocumentSearchCriteria customizedCriteria = service.customizeCriteria(documentSearchCriteria, customizerAttribute.getRuleAttribute().getName());
                if (customizedCriteria != null) {
                    return customizedCriteria;
                }
            }
        }
        return null;
    }

    @Override
    public DocumentSearchCriteria customizeClearCriteria(DocumentType documentType, DocumentSearchCriteria documentSearchCriteria) {
        DocumentTypeAttributeBo customizerAttribute = documentType.getCustomizerAttribute();
        if (customizerAttribute != null) {
            DocumentSearchCustomizationHandlerService service = loadCustomizationService(customizerAttribute.getRuleAttribute().getApplicationId());
            if (service.getEnabledCustomizations(documentType.getName(), customizerAttribute.getRuleAttribute().getName()).contains(
                    DocumentSearchCustomization.CLEAR_CRITERIA)) {
                DocumentSearchCriteria customizedCriteria = service.customizeClearCriteria(documentSearchCriteria, customizerAttribute.getRuleAttribute().getName());
                if (customizedCriteria != null) {
                    return customizedCriteria;
                }
            }
        }
        return null;
    }

    @Override
    public DocumentSearchResultValues customizeResults(DocumentType documentType,
            DocumentSearchCriteria documentSearchCriteria, DocumentSearchResults results) {
        DocumentTypeAttributeBo customizerAttribute = documentType.getCustomizerAttribute();
        if (customizerAttribute != null) {
            DocumentSearchCustomizationHandlerService service = loadCustomizationService(customizerAttribute.getRuleAttribute().getApplicationId());
            if (service.getEnabledCustomizations(documentType.getName(), customizerAttribute.getRuleAttribute().getName()).contains(
                    DocumentSearchCustomization.RESULTS)) {
                DocumentSearchResultValues customizedResults = service.customizeResults(documentSearchCriteria, results.getSearchResults(), customizerAttribute.getRuleAttribute().getName());
                if (customizedResults != null) {
                    return customizedResults;
                }
            }
        }
        return null;
    }

    @Override
    public DocumentSearchResultSetConfiguration customizeResultSetConfiguration(DocumentType documentType,
            DocumentSearchCriteria documentSearchCriteria) {
        DocumentTypeAttributeBo customizerAttribute = documentType.getCustomizerAttribute();
        if (customizerAttribute != null) {
            DocumentSearchCustomizationHandlerService service = loadCustomizationService(customizerAttribute.getRuleAttribute().getApplicationId());
            if (service.getEnabledCustomizations(documentType.getName(), customizerAttribute.getRuleAttribute().getName()).contains(
                    DocumentSearchCustomization.RESULT_SET_FIELDS)) {
                DocumentSearchResultSetConfiguration resultSetConfiguration = service.customizeResultSetConfiguration(
                        documentSearchCriteria, customizerAttribute.getRuleAttribute().getName());
                if (resultSetConfiguration != null) {
                    return resultSetConfiguration;
                }
            }
        }
        return null;
    }

    protected DocumentSearchCustomizationHandlerService loadCustomizationService(String applicationId) {
        DocumentSearchCustomizationHandlerService service = KewFrameworkServiceLocator.getDocumentSearchCustomizationHandlerService(
                applicationId);
        if (service == null) {
            throw new WorkflowRuntimeException("Failed to locate DocumentSearchCustomizationService for applicationId: " + applicationId);
        }
        return service;
    }

    protected void mergeAttributeFields(List<AttributeFields> attributeFieldsList, LinkedHashMap<String, AttributeFields> orderedFieldMap) {
        if (attributeFieldsList == null) {
            return;
        }
        for (AttributeFields attributeFields : attributeFieldsList) {
            orderedFieldMap.put(attributeFields.getAttributeName(), attributeFields);
        }
    }

    protected List<AttributeFields> flattenOrderedFieldMap(LinkedHashMap<String, AttributeFields> orderedFieldMap) {
        return new ArrayList<AttributeFields>(orderedFieldMap.values());
    }

}
