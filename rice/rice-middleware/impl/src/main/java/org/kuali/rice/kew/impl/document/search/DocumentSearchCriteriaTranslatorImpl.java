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
package org.kuali.rice.kew.impl.document.search;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.kuali.rice.core.api.search.Range;
import org.kuali.rice.core.api.search.SearchExpressionUtils;
import org.kuali.rice.core.api.uif.AttributeLookupSettings;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.kuali.rice.kew.api.KEWPropertyConstants;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.api.document.DocumentStatusCategory;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteriaContract;
import org.kuali.rice.kew.api.document.search.RouteNodeLookupLogic;
import org.kuali.rice.kew.docsearch.DocumentSearchInternalUtils;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.framework.document.search.DocumentSearchCriteriaConfiguration;
import org.kuali.rice.kew.impl.document.ApplicationDocumentStatusUtils;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.krad.util.KRADConstants;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Reference implementation of {@code DocumentSearchCriteriaTranslator}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentSearchCriteriaTranslatorImpl implements DocumentSearchCriteriaTranslator {

    private static final Logger LOG = Logger.getLogger(DocumentSearchCriteriaTranslatorImpl.class);

    private static final String DOCUMENT_STATUSES = "documentStatuses";
    private static final String ROUTE_NODE_LOOKUP_LOGIC = "routeNodeLookupLogic";

    /**
     * Fields which translate directory from criteria strings to properties on the DocumentSearchCriteria.
     */
    private static final String[] DIRECT_TRANSLATE_FIELD_NAMES = {
            "documentId",
            "applicationDocumentId",
            "applicationDocumentStatus",
            "initiatorPrincipalName",
            "initiatorPrincipalId",
            "viewerPrincipalName",
            "viewerPrincipalId",
            "groupViewerId",
            "approverPrincipalName",
            "approverPrincipalId",
            "routeNodeName",
            "documentTypeName",
            "saveName",
            "title",
            "isAdvancedSearch"
    };
    private static final Set<String> DIRECT_TRANSLATE_FIELD_NAMES_SET =
            new HashSet<String>(Arrays.asList(DIRECT_TRANSLATE_FIELD_NAMES));

    private static final String[] DATE_RANGE_TRANSLATE_FIELD_NAMES = {
            "dateCreated",
            "dateLastModified",
            "dateApproved",
            "dateFinalized"
    };
    private static final Set<String> DATE_RANGE_TRANSLATE_FIELD_NAMES_SET =
            new HashSet<String>(Arrays.asList(DATE_RANGE_TRANSLATE_FIELD_NAMES));

    @Override
    public DocumentSearchCriteria translateFieldsToCriteria(Map<String, String> fieldValues) {

        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        List<String> documentAttributeFields = new ArrayList<String>();
        for (Map.Entry<String, String> field : fieldValues.entrySet()) {
            try {
                if (StringUtils.isNotBlank(field.getValue())) {
                    if (DIRECT_TRANSLATE_FIELD_NAMES_SET.contains(field.getKey())) {
                        PropertyUtils.setNestedProperty(criteria, field.getKey(), field.getValue());
                    } else if (DATE_RANGE_TRANSLATE_FIELD_NAMES_SET.contains(field.getKey())) {
                        applyDateRangeField(criteria, field.getKey(), field.getValue());
                    } else if (field.getKey().startsWith(KewApiConstants.DOCUMENT_ATTRIBUTE_FIELD_PREFIX)) {
                        documentAttributeFields.add(field.getKey());
                    }

                }
            } catch (Exception e) {
                throw new IllegalStateException("Failed to set document search criteria field: " + field.getKey(), e);
            }
        }

        if (!documentAttributeFields.isEmpty()) {
            translateDocumentAttributeFieldsToCriteria(fieldValues, documentAttributeFields, criteria);
        }

        String routeNodeLookupLogic = fieldValues.get(ROUTE_NODE_LOOKUP_LOGIC);
        if (StringUtils.isNotBlank(routeNodeLookupLogic)) {
            criteria.setRouteNodeLookupLogic(RouteNodeLookupLogic.valueOf(routeNodeLookupLogic));
        }

        String documentStatusesValue = fieldValues.get(KEWPropertyConstants.DOC_SEARCH_RESULT_PROPERTY_NAME_STATUS_CODE);
        if (StringUtils.isNotBlank(documentStatusesValue)) {
            String[] documentStatuses = documentStatusesValue.split(",");
            for (String documentStatus : documentStatuses) {
                if (documentStatus.startsWith("category:")) {
                    String categoryCode = StringUtils.remove(documentStatus, "category:");
                    criteria.getDocumentStatusCategories().add(DocumentStatusCategory.fromCode(categoryCode));
                } else {
                    criteria.getDocumentStatuses().add(DocumentStatus.fromCode(documentStatus));
                }
            }
        }

        LinkedHashMap<String, List<String>> applicationDocumentStatusGroupings =
                ApplicationDocumentStatusUtils.getApplicationDocumentStatusCategories(criteria.getDocumentTypeName());

        String applicationDocumentStatusesValue = fieldValues.get(KEWPropertyConstants.DOC_SEARCH_RESULT_PROPERTY_NAME_DOC_STATUS);
        if (StringUtils.isNotBlank(applicationDocumentStatusesValue)) {
            String[] applicationDocumentStatuses = applicationDocumentStatusesValue.split(",");
            for (String applicationDocumentStatus : applicationDocumentStatuses) {
                // KULRICE-7786: support for groups (categories) of application document statuses
                if (applicationDocumentStatus.startsWith("category:")) {
                    String categoryCode = StringUtils.remove(applicationDocumentStatus, "category:");
                    if (applicationDocumentStatusGroupings.containsKey(categoryCode)) {
                        criteria.getApplicationDocumentStatuses().addAll(applicationDocumentStatusGroupings.get(categoryCode));
                    }
                } else {
                    criteria.getApplicationDocumentStatuses().add(applicationDocumentStatus);
                }
            }
        }

        // blank the deprecated field out, it's not needed.
        criteria.setApplicationDocumentStatus(null);

        return criteria.build();
    }

    /**
     * Converts the DocumentSearchCriteria to a Map of values that can be applied to the Lookup form fields.
     * @param criteria the criteria to translate
     * @return a Map of values that can be applied to the Lookup form fields.
     */
    public Map<String, String[]> translateCriteriaToFields(DocumentSearchCriteria criteria) {
        Map<String, String[]> values = new HashMap<String, String[]>();

        for (String property: DIRECT_TRANSLATE_FIELD_NAMES) {
            convertCriteriaPropertyToField(criteria, property, values);
        }

        for (String property: DATE_RANGE_TRANSLATE_FIELD_NAMES) {
            convertCriteriaRangeField(criteria, property, values);
        }

        Map<String, List<String>> docAttrValues = criteria.getDocumentAttributeValues();
        if (!docAttrValues.isEmpty()) {
            Map<String, AttributeLookupSettings> attributeLookupSettingsMap = getAttributeLookupSettings(criteria);
            for (Map.Entry<String, List<String>> entry: docAttrValues.entrySet()) {
                AttributeLookupSettings lookupSettings = attributeLookupSettingsMap.get(entry.getKey());
                if (lookupSettings != null && lookupSettings.isRanged()) {
                    convertAttributeRangeField(entry.getKey(), entry.getValue(), values);
                } else {
                    values.put(KewApiConstants.DOCUMENT_ATTRIBUTE_FIELD_PREFIX + entry.getKey(), entry.getValue().toArray(new String[0]));
                }
            }
        }

        RouteNodeLookupLogic lookupLogic = criteria.getRouteNodeLookupLogic();
        if (lookupLogic != null) {
            values.put(ROUTE_NODE_LOOKUP_LOGIC, new String[]{lookupLogic.name()});
        }

        Collection<String> statuses = new ArrayList<String>();
        for (DocumentStatus status: criteria.getDocumentStatuses()) {
            statuses.add(status.getCode());
        }
        for (DocumentStatusCategory category: criteria.getDocumentStatusCategories()) {
            statuses.add("category:" + category.getCode());
        }
        values.put(KEWPropertyConstants.DOC_SEARCH_RESULT_PROPERTY_NAME_STATUS_CODE, statuses.toArray(new String[0]));

        return values;
    }

    /**
     * Convert a ranged document search attribute field into a form field.
     * This means:
     * 0) the attribute field has been identified as a ranged attribute
     * 1) we need to parse the attribute search expression to find upper and lower bounds
     * 2) set upper and lower bounds in distinct form fields
     * @param attrKey the attribute key
     * @param attrValues the attribute value
     */
    protected static void convertAttributeRangeField(String attrKey, List<String> attrValues, Map<String, String[]> values) {
        String value = "";
        if (attrValues != null && !attrValues.isEmpty()) {
            value = attrValues.get(0);
            // can ranged attributes be multi-valued?
            if (attrValues.size() > 1) {
                LOG.warn("Encountered multi-valued ranged document search attribute '" + attrKey + "': " + attrValues);
            }
        }
        Range range = SearchExpressionUtils.parseRange(value);
        String lower;
        String upper;
        if (range != null) {
            lower = range.getLowerBoundValue();
            upper = range.getUpperBoundValue();
        } else {
            lower = null;
            upper = value;
        }
        values.put(KewApiConstants.DOCUMENT_ATTRIBUTE_FIELD_PREFIX + KRADConstants.LOOKUP_RANGE_LOWER_BOUND_PROPERTY_PREFIX + attrKey, new String[] { lower });
        values.put(KewApiConstants.DOCUMENT_ATTRIBUTE_FIELD_PREFIX + attrKey, new String[] { upper });
    }

    /**
     * Convenience method for converting a set of doc search criteria range fields into form fields
     * @param criteria the dsc
     * @param property the abstract property name
     * @param values the form field values
     */
    protected static void convertCriteriaRangeField(DocumentSearchCriteria criteria, String property, Map<String, String[]> values) {
        convertCriteriaPropertyToField(criteria, property + "From", KRADConstants.LOOKUP_RANGE_LOWER_BOUND_PROPERTY_PREFIX + property, values);
        convertCriteriaPropertyToField(criteria, property + "To", property, values);
    }

    /**
     * Looks up a property on the criteria object and sets it as a key/value pair in the values Map
     * @param criteria the DocumentSearchCriteria
     * @param property the DocumentSearchCriteria property name and destination field name
     * @param values the map of values to update
     */
    protected static void convertCriteriaPropertyToField(DocumentSearchCriteria criteria, String property, Map<String, String[]> values) {
        convertCriteriaPropertyToField(criteria, property, property, values);
    }

    /**
     * Looks up a property on the criteria object and sets it as a key/value pair in the values Map
     * @param criteria the DocumentSearchCriteria
     * @param property the DocumentSearchCriteria property name
     * @param fieldName the destination field name
     * @param values the map of values to update
     */
    protected static void convertCriteriaPropertyToField(DocumentSearchCriteria criteria, String property, String fieldName, Map<String, String[]> values) {
        try {
            Object val = PropertyUtils.getProperty(criteria, property);
            if (val != null) {
                values.put(fieldName, new String[] { ObjectUtils.toString(val) });
            }
        } catch (NoSuchMethodException nsme) {
            LOG.error("Error reading property '" + property + "' of criteria", nsme);
        } catch (InvocationTargetException ite) {
            LOG.error("Error reading property '" + property + "' of criteria", ite);
        } catch (IllegalAccessException iae) {
            LOG.error("Error reading property '" + property + "' of criteria", iae);

        }
    }

    protected void applyDateRangeField(DocumentSearchCriteria.Builder criteria, String fieldName, String fieldValue) throws Exception {
        DateTime lowerDateTime = DocumentSearchInternalUtils.getLowerDateTimeBound(fieldValue);
        DateTime upperDateTime = DocumentSearchInternalUtils.getUpperDateTimeBound(fieldValue);
        if (lowerDateTime != null) {
            PropertyUtils.setNestedProperty(criteria, fieldName + "From", lowerDateTime);
        }
        if (upperDateTime != null) {
            PropertyUtils.setNestedProperty(criteria, fieldName + "To", upperDateTime);
        }
    }

    /**
     * Returns a map of attributelookupsettings for the custom search attributes of the document if specified in the criteria
     * @param criteria the doc search criteria
     * @return a map of attributelookupsettings for the custom search attributes of the document if specified in the criteria, empty otherwise
     */
    protected Map<String, AttributeLookupSettings> getAttributeLookupSettings(DocumentSearchCriteriaContract criteria) {
        String documentTypeName = criteria.getDocumentTypeName();
        Map<String, AttributeLookupSettings> attributeLookupSettingsMap = new HashMap<java.lang.String, AttributeLookupSettings>();

        if (StringUtils.isNotEmpty(documentTypeName)) {
            DocumentType documentType = KEWServiceLocator.getDocumentTypeService().findByNameCaseInsensitive(documentTypeName);
            if (documentType != null) {
                DocumentSearchCriteriaConfiguration configuration = KEWServiceLocator.getDocumentSearchCustomizationMediator().getDocumentSearchCriteriaConfiguration(
                        documentType);
                if (configuration != null) {
                    List<RemotableAttributeField> remotableAttributeFields = configuration.getFlattenedSearchAttributeFields();
                    for (RemotableAttributeField raf: remotableAttributeFields) {
                        attributeLookupSettingsMap.put(raf.getName(), raf.getAttributeLookupSettings());
                    }
                }
            } else {
                LOG.error("Searching against unknown document type '" + documentTypeName + "'; searchable attribute ranges will not work.");
            }
        }

        return attributeLookupSettingsMap;
    }

    protected String translateRangePropertyToExpression(Map<String, String> fieldValues, String property, String prefix, AttributeLookupSettings settings) {
        String lowerBoundValue = fieldValues.get(prefix + KRADConstants.LOOKUP_RANGE_LOWER_BOUND_PROPERTY_PREFIX + property);
        String upperBoundValue = fieldValues.get(prefix + property);

        Range range = new Range();
        // defaults for general lookup/search
        range.setLowerBoundInclusive(settings.isLowerBoundInclusive());
        range.setUpperBoundInclusive(settings.isUpperBoundInclusive());
        range.setLowerBoundValue(lowerBoundValue);
        range.setUpperBoundValue(upperBoundValue);

        String expr = range.toString();
        if (StringUtils.isEmpty(expr)) {
            expr = upperBoundValue;
        }
        return expr;
    }

    protected void translateDocumentAttributeFieldsToCriteria(Map<String, String> fieldValues, List<String> fields, DocumentSearchCriteria.Builder criteria) {
        Map<String, AttributeLookupSettings> attributeLookupSettingsMap = getAttributeLookupSettings(criteria);
        for (String field: fields) {
            String documentAttributeName = field.substring(KewApiConstants.DOCUMENT_ATTRIBUTE_FIELD_PREFIX.length());
            // omit the synthetic lower bound field is there is an upper bound field, don't set back into doc attrib values
            if (documentAttributeName.startsWith(KRADConstants.LOOKUP_RANGE_LOWER_BOUND_PROPERTY_PREFIX)) {
                String tempDocumentAttributeName = StringUtils.substringAfter(documentAttributeName,KRADConstants.LOOKUP_RANGE_LOWER_BOUND_PROPERTY_PREFIX) ;
                String tempField = fieldValues.get(KewApiConstants.DOCUMENT_ATTRIBUTE_FIELD_PREFIX + tempDocumentAttributeName);
                if (StringUtils.isEmpty(tempField)) {
                    documentAttributeName =  tempDocumentAttributeName;
                } else {
                    continue;
                }
            }
            String value = fieldValues.get(field);
            AttributeLookupSettings lookupSettings = attributeLookupSettingsMap.get(documentAttributeName);
            if (lookupSettings != null && lookupSettings.isRanged()) {
                value = translateRangePropertyToExpression(fieldValues, documentAttributeName, KewApiConstants.DOCUMENT_ATTRIBUTE_FIELD_PREFIX, lookupSettings);
            }
            applyDocumentAttribute(criteria, documentAttributeName, value);
        }
    }

    protected void applyDocumentAttribute(DocumentSearchCriteria.Builder criteria, String documentAttributeName, String attributeValue) {
        criteria.addDocumentAttributeValue(documentAttributeName, attributeValue);
    }

}
