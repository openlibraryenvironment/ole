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
package org.kuali.rice.krad.uif.service.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.LookupService;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.component.MethodInvokerConfig;
import org.kuali.rice.krad.uif.field.AttributeQuery;
import org.kuali.rice.krad.uif.field.AttributeQueryResult;
import org.kuali.rice.krad.uif.field.InputField;
import org.kuali.rice.krad.uif.service.AttributeQueryService;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.widget.LocationSuggest;
import org.kuali.rice.krad.uif.widget.Suggest;
import org.kuali.rice.krad.util.BeanPropertyComparator;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of <code>AttributeQueryService</code> that prepares the attribute queries and
 * delegates to the <code>LookupService</code>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class AttributeQueryServiceImpl implements AttributeQueryService {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(
            AttributeQueryServiceImpl.class);

    private LookupService lookupService;
    private ConfigurationService configurationService;

    /**
     * @see org.kuali.rice.krad.uif.service.AttributeQueryService#performFieldSuggestQuery(
     *org.kuali.rice.krad.uif.view.View, java.lang.String, java.lang.String, java.util.Map<java.lang.String,
     * java.lang.String>)
     */
    @Override
    public AttributeQueryResult performFieldSuggestQuery(View view, String fieldId, String fieldTerm,
            Map<String, String> queryParameters) {
        AttributeQueryResult queryResult = new AttributeQueryResult();

        // retrieve attribute field from view index
        InputField inputField = (InputField) view.getViewIndex().getComponentById(fieldId);
        if (inputField == null) {
            throw new RuntimeException("Unable to find attribute field instance for id: " + fieldId);
        }

        Suggest fieldSuggest = inputField.getSuggest();
        AttributeQuery suggestQuery = fieldSuggest.getSuggestQuery();

        // add term as a like criteria
        Map<String, String> additionalCriteria = new HashMap<String, String>();
        additionalCriteria.put(fieldSuggest.getValuePropertyName(), fieldTerm + "*");

        // execute suggest query
        Collection<?> results = null;
        if (suggestQuery.hasConfiguredMethod()) {
            Object queryMethodResult = executeAttributeQueryMethod(view, suggestQuery, queryParameters, true,
                    fieldTerm);
            if ((queryMethodResult != null) && (queryMethodResult instanceof Collection<?>)) {
                results = (Collection<?>) queryMethodResult;
            }
        } else {
            results = executeAttributeQueryCriteria(suggestQuery, queryParameters, additionalCriteria);
        }

        // build list of suggest data from result records
        if (results != null) {
            if (fieldSuggest.isReturnFullQueryObject()) {
                queryResult.setResultData((List<Object>) results);
            } else {
                retrievePropertiesOnResults(queryResult, results, fieldSuggest);
            }
        }

        return queryResult;
    }

    /**
     * Instead of returning the full object this method fills in queryResult with data that contain the properties
     * of each result object, as configured through the fieldSuggest, from the set of results.
     *
     * @param queryResult the queryResult to fill in
     * @param results the set of original results
     * @param fieldSuggest the Suggest widget
     */
    protected void retrievePropertiesOnResults(AttributeQueryResult queryResult, Collection<?> results,
            Suggest fieldSuggest) {
        List<Object> suggestData = new ArrayList<Object>();
        for (Object result : results) {
            if (result == null) {
                continue;
            }

            Map<String, String> propMap = new HashMap<String, String>();

            // if result is type string, use as both value and label
            if (result instanceof String) {
                propMap.put(UifParameters.VALUE, (String) result);
                propMap.put(UifParameters.LABEL, (String) result);
            }

            // value prop
            Object suggestFieldValue = null;
            if (StringUtils.isNotBlank(fieldSuggest.getValuePropertyName())) {
                suggestFieldValue = ObjectPropertyUtils.getPropertyValue(result, fieldSuggest.getValuePropertyName());
            } else if (ObjectPropertyUtils.isReadableProperty(result, UifParameters.VALUE)) {
                suggestFieldValue = ObjectPropertyUtils.getPropertyValue(result, UifParameters.VALUE);
            }

            if (suggestFieldValue != null) {
                propMap.put(UifParameters.VALUE, suggestFieldValue.toString());
            }

            // label prop
            Object suggestFieldLabel = null;
            if (StringUtils.isNotBlank(fieldSuggest.getLabelPropertyName())) {
                suggestFieldLabel = ObjectPropertyUtils.getPropertyValue(result, fieldSuggest.getLabelPropertyName());
            } else if (ObjectPropertyUtils.isReadableProperty(result, UifParameters.LABEL)) {
                suggestFieldLabel = ObjectPropertyUtils.getPropertyValue(result, UifParameters.LABEL);
            }

            if (suggestFieldLabel != null) {
                propMap.put(UifParameters.LABEL, suggestFieldLabel.toString());
            }

            // location suggest specific properties
            if (fieldSuggest instanceof LocationSuggest) {
                handleLocationSuggestProperties((LocationSuggest) fieldSuggest, result, propMap);
            }

            // additional properties
            handleAdditionalSuggestProperties(fieldSuggest, result, propMap);

            // only add if there was a property to send back
            if (!propMap.isEmpty()) {
                //TODO: need to apply formatter for field or have method in object property utils
                suggestData.add(propMap);
            }
        }

        queryResult.setResultData(suggestData);
    }

    /**
     * Handle the custom additionalProperties set back for a suggestion query.  These will be added to the propMap.
     *
     * @param fieldSuggest the suggest
     * @param result the result to pull properties from
     * @param propMap the propMap to add properties to
     */
    private void handleAdditionalSuggestProperties(Suggest fieldSuggest, Object result, Map<String, String> propMap) {
        if (fieldSuggest.getAdditionalPropertiesToReturn() != null) {
            //add properties for each valid property name
            for (String propName : fieldSuggest.getAdditionalPropertiesToReturn()) {
                Object propValue = null;

                if (StringUtils.isNotBlank(propName) && ObjectPropertyUtils.isReadableProperty(result, propName)) {
                    propValue = ObjectPropertyUtils.getPropertyValue(result, propName);
                }

                if (propValue != null) {
                    propMap.put(propName, propValue.toString());
                }
            }
        }
    }

    /**
     * Handle the LocationSuggest specific properties and add them to the map.
     *
     * @param fieldSuggest the suggest
     * @param result the result to pull properties from
     * @param propMap the propMap to add properties to
     */
    private void handleLocationSuggestProperties(LocationSuggest fieldSuggest, Object result,
            Map<String, String> propMap) {

        // href property
        Object suggestHrefValue = null;
        if (StringUtils.isNotBlank(fieldSuggest.getHrefPropertyName()) && ObjectPropertyUtils.isReadableProperty(result,
                fieldSuggest.getHrefPropertyName())) {
            suggestHrefValue = ObjectPropertyUtils.getPropertyValue(result, fieldSuggest.getHrefPropertyName());
        }

        // add if found
        if (suggestHrefValue != null) {
            propMap.put(fieldSuggest.getHrefPropertyName(), suggestHrefValue.toString());
        }

        // url addition/appendage property
        Object addUrlValue = null;
        if (StringUtils.isNotBlank(fieldSuggest.getAdditionalUrlPathPropertyName()) && ObjectPropertyUtils
                .isReadableProperty(result, fieldSuggest.getAdditionalUrlPathPropertyName())) {
            addUrlValue = ObjectPropertyUtils.getPropertyValue(result, fieldSuggest.getAdditionalUrlPathPropertyName());
        }

        // add if found
        if (addUrlValue != null) {
            propMap.put(fieldSuggest.getAdditionalUrlPathPropertyName(), addUrlValue.toString());
        }

        if (fieldSuggest.getRequestParameterPropertyNames() == null) {
            return;
        }

        // add properties for each valid requestParameter property name
        for (String key : fieldSuggest.getRequestParameterPropertyNames().keySet()) {
            String prop = fieldSuggest.getRequestParameterPropertyNames().get(key);
            Object propValue = null;

            if (StringUtils.isNotBlank(prop) && ObjectPropertyUtils.isReadableProperty(result, prop)) {
                propValue = ObjectPropertyUtils.getPropertyValue(result, prop);
            }

            if (propValue != null) {
                propMap.put(prop, propValue.toString());
            }
        }

    }

    /**
     * @see org.kuali.rice.krad.uif.service.AttributeQueryService#performFieldQuery(org.kuali.rice.krad.uif.view.View,
     * java.lang.String, java.util.Map<java.lang.String,java.lang.String>)
     */
    @Override
    public AttributeQueryResult performFieldQuery(View view, String fieldId, Map<String, String> queryParameters) {
        AttributeQueryResult queryResult = new AttributeQueryResult();

        // retrieve attribute field from view index
        InputField inputField = (InputField) view.getViewIndex().getComponentById(fieldId);
        if (inputField == null) {
            throw new RuntimeException("Unable to find attribute field instance for id: " + fieldId);
        }

        AttributeQuery fieldQuery = inputField.getAttributeQuery();
        if (fieldQuery == null) {
            throw new RuntimeException("Field query not defined for field instance with id: " + fieldId);
        }

        // execute query and get result
        Object resultObject = null;
        if (fieldQuery.hasConfiguredMethod()) {
            Object queryMethodResult = executeAttributeQueryMethod(view, fieldQuery, queryParameters, false, null);
            if (queryMethodResult != null) {
                // if method returned the result then no further processing needed
                if (queryMethodResult instanceof AttributeQueryResult) {
                    return (AttributeQueryResult) queryMethodResult;
                }

                // if method returned collection, take first record
                if (queryMethodResult instanceof Collection<?>) {
                    Collection<?> methodResultCollection = (Collection<?>) queryMethodResult;
                    if (!methodResultCollection.isEmpty()) {
                        resultObject = methodResultCollection.iterator().next();
                    }
                } else {
                    resultObject = queryMethodResult;
                }
            }
        } else {
            // execute field query as object lookup
            Collection<?> results = executeAttributeQueryCriteria(fieldQuery, queryParameters, null);

            if ((results != null) && !results.isEmpty()) {
                // expect only one returned row for field query
                if (results.size() > 1) {
                    //finding too many results in a not found message (not specific enough)
                    resultObject = null;
                } else {
                    resultObject = results.iterator().next();
                }
            }
        }

        if (resultObject != null) {
            // build result field data map
            Map<String, String> resultFieldData = new HashMap<String, String>();
            for (String fromField : fieldQuery.getReturnFieldMapping().keySet()) {
                String returnField = fieldQuery.getReturnFieldMapping().get(fromField);

                String fieldValueStr = "";
                Object fieldValue = ObjectPropertyUtils.getPropertyValue(resultObject, fromField);
                if (fieldValue != null) {
                    fieldValueStr = fieldValue.toString();
                }
                resultFieldData.put(returnField, fieldValueStr);
            }
            queryResult.setResultFieldData(resultFieldData);

            fieldQuery.setReturnMessageText("");
        } else {
            // add data not found message
            if (fieldQuery.isRenderNotFoundMessage()) {
                String messageTemplate = getConfigurationService().getPropertyValueAsString(
                        UifConstants.MessageKeys.QUERY_DATA_NOT_FOUND);
                String message = MessageFormat.format(messageTemplate, inputField.getLabel());
                fieldQuery.setReturnMessageText(message.toLowerCase());
            }
        }

        // set message and message style classes on query result
        queryResult.setResultMessage(fieldQuery.getReturnMessageText());
        queryResult.setResultMessageStyleClasses(fieldQuery.getReturnMessageStyleClasses());

        return queryResult;
    }

    /**
     * Prepares the method configured on the attribute query then performs the method invocation
     *
     * @param view view instance the field is contained within
     * @param attributeQuery attribute query instance to execute
     * @param queryParameters map of query parameters that provide values for the method arguments
     * @param isSuggestQuery indicates whether the query is for forming suggest options
     * @param queryTerm if being called for a suggest, the term for the query field
     * @return type depends on method being invoked, could be AttributeQueryResult in which
     * case the method has prepared the return result, or an Object that needs to be parsed for the result
     */
    protected Object executeAttributeQueryMethod(View view, AttributeQuery attributeQuery,
            Map<String, String> queryParameters, boolean isSuggestQuery, String queryTerm) {
        String queryMethodToCall = attributeQuery.getQueryMethodToCall();
        MethodInvokerConfig queryMethodInvoker = attributeQuery.getQueryMethodInvokerConfig();

        if (queryMethodInvoker == null) {
            queryMethodInvoker = new MethodInvokerConfig();
        }

        // if method not set on invoker, use queryMethodToCall, note staticMethod could be set(don't know since
        // there is not a getter), if so it will override the target method in prepare
        if (StringUtils.isBlank(queryMethodInvoker.getTargetMethod())) {
            queryMethodInvoker.setTargetMethod(queryMethodToCall);
        }

        // if target class or object not set, use view helper service
        if ((queryMethodInvoker.getTargetClass() == null) && (queryMethodInvoker.getTargetObject() == null)) {
            queryMethodInvoker.setTargetObject(view.getViewHelperService());
        }

        // setup query method arguments
        List<Object> arguments = new ArrayList<Object>();
        if ((attributeQuery.getQueryMethodArgumentFieldList() != null) && (!attributeQuery
                .getQueryMethodArgumentFieldList().isEmpty())) {
            // retrieve argument types for conversion and verify method arguments
            int numQueryMethodArguments = attributeQuery.getQueryMethodArgumentFieldList().size();
            if (isSuggestQuery) {
                numQueryMethodArguments += 1;
            }

            Class[] argumentTypes = queryMethodInvoker.getArgumentTypes();
            if ((argumentTypes == null) || (argumentTypes.length != numQueryMethodArguments)) {
                throw new RuntimeException(
                        "Query method argument field list size does not match found number of method arguments");
            }

            for (int i = 0; i < attributeQuery.getQueryMethodArgumentFieldList().size(); i++) {
                String methodArgumentFromField = attributeQuery.getQueryMethodArgumentFieldList().get(i);
                if (queryParameters.containsKey(methodArgumentFromField)) {
                    arguments.add(queryParameters.get(methodArgumentFromField));
                } else {
                    arguments.add(null);
                }
            }
        }

        if (isSuggestQuery) {
            arguments.add(queryTerm);
        }

        queryMethodInvoker.setArguments(arguments.toArray());

        try {
            queryMethodInvoker.prepare();

            return queryMethodInvoker.invoke();
        } catch (Exception e) {
            throw new RuntimeException("Unable to invoke query method: " + queryMethodInvoker.getTargetMethod(), e);
        }
    }

    /**
     * Prepares a query using the configured data object, parameters, and criteria, then executes
     * the query and returns the result Collection
     *
     * @param attributeQuery attribute query instance to perform query for
     * @param queryParameters map of parameters that will be used in the query criteria
     * @param additionalCriteria map of additional name/value pairs to add to the critiera
     * @return results of query
     */
    protected Collection<?> executeAttributeQueryCriteria(AttributeQuery attributeQuery,
            Map<String, String> queryParameters, Map<String, String> additionalCriteria) {
        Collection<?> results = null;

        // build criteria for query
        Map<String, String> queryCriteria = new HashMap<String, String>();
        for (String fieldName : attributeQuery.getQueryFieldMapping().values()) {
            if (queryParameters.containsKey(fieldName) && StringUtils.isNotBlank(queryParameters.get(fieldName))) {
                queryCriteria.put(fieldName, queryParameters.get(fieldName));
            }
        }

        // add any static criteria
        for (String fieldName : attributeQuery.getAdditionalCriteria().keySet()) {
            queryCriteria.put(fieldName, attributeQuery.getAdditionalCriteria().get(fieldName));
        }

        // add additional criteria
        if (additionalCriteria != null) {
            queryCriteria.putAll(additionalCriteria);
        }

        Class<?> queryClass = null;
        try {
            queryClass = Class.forName(attributeQuery.getDataObjectClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                    "Invalid data object class given for suggest query: " + attributeQuery.getDataObjectClassName(), e);
        }

        // run query
        results = getLookupService().findCollectionBySearchUnbounded(queryClass, queryCriteria);

        // sort results
        if (!attributeQuery.getSortPropertyNames().isEmpty() && (results != null) && (results.size() > 1)) {
            Collections.sort((List<?>) results, new BeanPropertyComparator(attributeQuery.getSortPropertyNames()));
        }

        return results;
    }

    /**
     * Gets the lookup service
     *
     * @return lookup service
     */
    protected LookupService getLookupService() {
        if (lookupService == null) {
            lookupService = KRADServiceLocatorWeb.getLookupService();
        }

        return lookupService;
    }

    /**
     * Sets the lookup service
     *
     * @param lookupService
     */
    public void setLookupService(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    /**
     * Gets the configuration service
     *
     * @return configuration service
     */
    protected ConfigurationService getConfigurationService() {
        if (configurationService == null) {
            configurationService = CoreApiServiceLocator.getKualiConfigurationService();
        }

        return configurationService;
    }

    /**
     * Sets the configuration service
     *
     * @param configurationService
     */
    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }
}
