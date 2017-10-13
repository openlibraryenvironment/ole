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

import com.google.common.base.Function;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.reflect.ObjectDefinition;
import org.kuali.rice.core.api.search.Range;
import org.kuali.rice.core.api.search.SearchExpressionUtils;
import org.kuali.rice.core.api.uif.AttributeLookupSettings;
import org.kuali.rice.core.api.uif.DataType;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.kuali.rice.core.api.util.ClassLoaderUtils;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.core.framework.persistence.jdbc.sql.SQLUtils;
import org.kuali.rice.core.framework.resourceloader.ObjectDefinitionResolver;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.krad.util.GlobalVariables;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

/**
 * Defines various utilities for internal use in the reference implementation of the document search functionality.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentSearchInternalUtils {

    private static final Logger LOG = Logger.getLogger(DocumentSearchInternalUtils.class);

    private static final boolean CASE_SENSITIVE_DEFAULT = false;

    private static final String STRING_ATTRIBUTE_TABLE_NAME = "KREW_DOC_HDR_EXT_T";
    private static final String DATE_TIME_ATTRIBUTE_TABLE_NAME = "KREW_DOC_HDR_EXT_DT_T";
    private static final String DECIMAL_ATTRIBUTE_TABLE_NAME = "KREW_DOC_HDR_EXT_FLT_T";
    private static final String INTEGER_ATTRIBUTE_TABLE_NAME = "KREW_DOC_HDR_EXT_LONG_T";

    private static final List<SearchableAttributeConfiguration> CONFIGURATIONS =
            new ArrayList<SearchableAttributeConfiguration>();
    public static final List<Class<? extends SearchableAttributeValue>> SEARCHABLE_ATTRIBUTE_BASE_CLASS_LIST =
            new ArrayList<Class<? extends SearchableAttributeValue>>();

    static {
        SEARCHABLE_ATTRIBUTE_BASE_CLASS_LIST.add(SearchableAttributeStringValue.class);
        SEARCHABLE_ATTRIBUTE_BASE_CLASS_LIST.add(SearchableAttributeFloatValue.class);
        SEARCHABLE_ATTRIBUTE_BASE_CLASS_LIST.add(SearchableAttributeLongValue.class);
        SEARCHABLE_ATTRIBUTE_BASE_CLASS_LIST.add(SearchableAttributeDateTimeValue.class);
    }

    static {

        CONFIGURATIONS.add(new SearchableAttributeConfiguration(
                STRING_ATTRIBUTE_TABLE_NAME,
                EnumSet.of(DataType.BOOLEAN, DataType.STRING, DataType.MARKUP),
                String.class));

        CONFIGURATIONS.add(new SearchableAttributeConfiguration(
                DATE_TIME_ATTRIBUTE_TABLE_NAME,
                EnumSet.of(DataType.DATE, DataType.TRUNCATED_DATE, DataType.DATETIME),
                Timestamp.class));

        CONFIGURATIONS.add(new SearchableAttributeConfiguration(
                DECIMAL_ATTRIBUTE_TABLE_NAME,
                EnumSet.of(DataType.FLOAT, DataType.DOUBLE, DataType.CURRENCY),
                Float.TYPE));

        CONFIGURATIONS.add(new SearchableAttributeConfiguration(
                INTEGER_ATTRIBUTE_TABLE_NAME,
                EnumSet.of(DataType.INTEGER, DataType.LONG),
                Long.TYPE));

    }

    // initialize-on-demand holder class idiom - see Effective Java item #71
    /**
     * KULRICE-6704 - cached ObjectMapper for improved performance
     *
     */
    private static ObjectMapper getObjectMapper() { return ObjectMapperHolder.objectMapper; }

    private static class ObjectMapperHolder {
        static final ObjectMapper objectMapper = initializeObjectMapper();

        private static ObjectMapper initializeObjectMapper() {
            ObjectMapper jsonMapper = new ObjectMapper();
            jsonMapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
            return jsonMapper;
        }
    }
    
    public static boolean isLookupCaseSensitive(RemotableAttributeField remotableAttributeField) {
        if (remotableAttributeField == null) {
            throw new IllegalArgumentException("remotableAttributeField was null");
        }
        AttributeLookupSettings lookupSettings = remotableAttributeField.getAttributeLookupSettings();
        if (lookupSettings != null) {
            if (lookupSettings.isCaseSensitive() != null) {
                return lookupSettings.isCaseSensitive().booleanValue();
            }
        }
        return CASE_SENSITIVE_DEFAULT;
    }

    public static String getAttributeTableName(RemotableAttributeField attributeField) {
        return getConfigurationForField(attributeField).getTableName();
    }

    public static Class<?> getDataTypeClass(RemotableAttributeField attributeField) {
        return getConfigurationForField(attributeField).getDataTypeClass();
    }

    private static SearchableAttributeConfiguration getConfigurationForField(RemotableAttributeField attributeField) {
        for (SearchableAttributeConfiguration configuration : CONFIGURATIONS) {
            DataType dataType = attributeField.getDataType();
            if (dataType == null) {
                dataType = DataType.STRING;
            }
            if (configuration.getSupportedDataTypes().contains(dataType))  {
                return configuration;
            }
        }
        throw new IllegalArgumentException("Failed to determine proper searchable attribute configuration for given data type of '" + attributeField.getDataType() + "'");
    }

    public static List<SearchableAttributeValue> getSearchableAttributeValueObjectTypes() {
        List<SearchableAttributeValue> searchableAttributeValueClasses = new ArrayList<SearchableAttributeValue>();
        for (Class<? extends SearchableAttributeValue> searchAttributeValueClass : SEARCHABLE_ATTRIBUTE_BASE_CLASS_LIST) {
            ObjectDefinition objDef = new ObjectDefinition(searchAttributeValueClass);
            SearchableAttributeValue attributeValue = (SearchableAttributeValue) ObjectDefinitionResolver.createObject(
                    objDef, ClassLoaderUtils.getDefaultClassLoader(), false);
            searchableAttributeValueClasses.add(attributeValue);
        }
        return searchableAttributeValueClasses;
    }

    public static SearchableAttributeValue getSearchableAttributeValueByDataTypeString(String dataType) {
        SearchableAttributeValue returnableValue = null;
        if (StringUtils.isBlank(dataType)) {
            return returnableValue;
        }
        for (SearchableAttributeValue attValue : getSearchableAttributeValueObjectTypes())
        {
            if (dataType.equalsIgnoreCase(attValue.getAttributeDataType()))
            {
                if (returnableValue != null)
                {
                    String errorMsg = "Found two SearchableAttributeValue objects with same data type string ('" + dataType + "' while ignoring case):  " + returnableValue.getClass().getName() + " and " + attValue.getClass().getName();
                    LOG.error("getSearchableAttributeValueByDataTypeString() " + errorMsg);
                    throw new RuntimeException(errorMsg);
                }
                LOG.debug("getSearchableAttributeValueByDataTypeString() SearchableAttributeValue class name is " + attValue.getClass().getName() + "... ojbConcreteClassName is " + attValue.getOjbConcreteClass());
                ObjectDefinition objDef = new ObjectDefinition(attValue.getClass());
                returnableValue = (SearchableAttributeValue) ObjectDefinitionResolver.createObject(objDef, ClassLoaderUtils.getDefaultClassLoader(), false);
            }
        }
        return returnableValue;
    }

    public static String getDisplayValueWithDateOnly(DateTime value) {
        return getDisplayValueWithDateOnly(new Timestamp(value.getMillis()));
    }

    public static String getDisplayValueWithDateOnly(Timestamp value) {
        return RiceConstants.getDefaultDateFormat().format(new Date(value.getTime()));
    }

    public static DateTime getLowerDateTimeBound(String dateRange) throws ParseException {
        Range range = SearchExpressionUtils.parseRange(dateRange);
        if (range == null) {
            throw new IllegalArgumentException("Failed to parse date range from given string: " + dateRange);
        }
        if (range.getLowerBoundValue() != null) {
            java.util.Date lowerRangeDate = null;
            try{
                lowerRangeDate = CoreApiServiceLocator.getDateTimeService().convertToDate(range.getLowerBoundValue());
            }catch(ParseException pe){
                GlobalVariables.getMessageMap().putError("dateFrom", RiceKeyConstants.ERROR_CUSTOM, pe.getMessage());
            }
            MutableDateTime dateTime = new MutableDateTime(lowerRangeDate);
            dateTime.setMillisOfDay(0);
            return dateTime.toDateTime();
        }
        return null;
    }

    public static DateTime getUpperDateTimeBound(String dateRange) throws ParseException {
        Range range = SearchExpressionUtils.parseRange(dateRange);
        if (range == null) {
            throw new IllegalArgumentException("Failed to parse date range from given string: " + dateRange);
        }
        if (range.getUpperBoundValue() != null) {
            java.util.Date upperRangeDate = null;
            try{
                upperRangeDate = CoreApiServiceLocator.getDateTimeService().convertToDate(range.getUpperBoundValue());
            }catch(ParseException pe){
                GlobalVariables.getMessageMap().putError("dateCreated", RiceKeyConstants.ERROR_CUSTOM, pe.getMessage());
            }
            MutableDateTime dateTime = new MutableDateTime(upperRangeDate);
            // set it to the last millisecond of the day
            dateTime.setMillisOfDay((24 * 60 * 60 * 1000) - 1);
            return dateTime.toDateTime();
        }
        return null;
    }

    public static class SearchableAttributeConfiguration {

        private final String tableName;
        private final EnumSet<DataType> supportedDataTypes;
        private final Class<?> dataTypeClass;

        public SearchableAttributeConfiguration(String tableName,
                EnumSet<DataType> supportedDataTypes,
                Class<?> dataTypeClass) {
            this.tableName = tableName;
            this.supportedDataTypes = supportedDataTypes;
            this.dataTypeClass = dataTypeClass;
        }

        public String getTableName() {
            return tableName;
        }

        public EnumSet<DataType> getSupportedDataTypes() {
            return supportedDataTypes;
        }

        public Class<?> getDataTypeClass() {
            return dataTypeClass;
        }

    }

    /**
     * Unmarshals a DocumentSearchCriteria from JSON string
     * @param string the JSON
     * @return unmarshalled DocumentSearchCriteria
     * @throws IOException
     */
    public static DocumentSearchCriteria unmarshalDocumentSearchCriteria(String string) throws IOException {
        DocumentSearchCriteria.Builder builder = getObjectMapper().readValue(string, DocumentSearchCriteria.Builder.class);
        // fix up the Joda DateTimes
        builder.normalizeDateTimes();
        // build() it
        return builder.build();
    }

    /**
     * Marshals a DocumentSearchCriteria to JSON string
     * @param criteria the criteria
     * @return a JSON string
     * @throws IOException
     */
    public static String marshalDocumentSearchCriteria(DocumentSearchCriteria criteria) throws IOException {
        // Jackson XC support not included by Rice, so no auto-magic JAXB-compatibility
        // AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
        // // make deserializer use JAXB annotations (only)
        // mapper.getDeserializationConfig().setAnnotationIntrospector(introspector);
        // // make serializer use JAXB annotations (only)
        // mapper.getSerializationConfig().setAnnotationIntrospector(introspector);
        return getObjectMapper().writeValueAsString(criteria);
    }

    public static List<RemotableAttributeError> validateSearchFieldValues(String fieldName, SearchableAttributeValue attributeValue, List<String> searchValues, String errorMessagePrefix, List<String> resultingValues, Function<String, Collection<RemotableAttributeError>> customValidator) {
        List<RemotableAttributeError> errors = new ArrayList<RemotableAttributeError>();
        // nothing to validate
        if (CollectionUtils.isEmpty(searchValues)) {
            return errors;
        }
        for (String searchValue: searchValues) {
            errors.addAll(validateSearchFieldValue(fieldName, attributeValue, searchValue, errorMessagePrefix, resultingValues, customValidator));
        }
        return Collections.unmodifiableList(errors);
    }

    /**
     * Validates a single DocumentSearchCriteria searchable attribute field value (of the list of possibly multiple values)
     * @param attributeValue the searchable attribute value type
     * @param enteredValue the incoming DSC field value
     * @param fieldName the name of the searchable attribute field/key
     * @param errorMessagePrefix error message prefix
     * @param resultingValues optional list of accumulated parsed values
     * @param customValidator custom value validator to invoke on default validation success
     * @return (possibly empty) list of validation error
     */
    public static List<RemotableAttributeError> validateSearchFieldValue(String fieldName, SearchableAttributeValue attributeValue, String enteredValue, String errorMessagePrefix, List<String> resultingValues, Function<String, Collection<RemotableAttributeError>> customValidator) {
        List<RemotableAttributeError> errors = new ArrayList<RemotableAttributeError>();
        if (enteredValue == null) {
            return errors;
        }
        // TODO: this also parses compound expressions and therefore produces a list of strings
        //       how does this relate to DocumentSearchInternalUtils.parseRange... which should consume which?
        List<String> parsedValues = SQLUtils.getCleanedSearchableValues(enteredValue, attributeValue.getAttributeDataType());
        for (String value: parsedValues) {
            errors.addAll(validateParsedSearchFieldValue(fieldName, attributeValue, value, errorMessagePrefix, resultingValues, customValidator));
        }
        return errors;
    }

    /**
     * Validates a single terminal value from a single search field (list of values); calls a custom validator if default validation passes and
     * custom validator is given
     * @param attributeValue the searchable value type
     * @param parsedValue the parsed value to validate
     * @param fieldName the field name for error message
     * @param errorMessagePrefix the prefix for error message
     * @param resultingValues parsed value is appended to this list if present (non-null)
     * @return immutable collection of errors (possibly empty)
     */
    public static Collection<RemotableAttributeError> validateParsedSearchFieldValue(String fieldName, SearchableAttributeValue attributeValue, String parsedValue, String errorMessagePrefix, List<String> resultingValues, Function<String, Collection<RemotableAttributeError>> customValidator) {
        Collection<RemotableAttributeError> errors = new ArrayList<RemotableAttributeError>(1);
        String value = parsedValue;
        if (attributeValue.allowsWildcards()) { // TODO: how should this work in relation to criteria expressions?? clean above removes *
            value = value.replaceAll(KewApiConstants.SearchableAttributeConstants.SEARCH_WILDCARD_CHARACTER_REGEX_ESCAPED, "");
        }

        if (resultingValues != null) {
            resultingValues.add(value);
        }

        if (!attributeValue.isPassesDefaultValidation(value)) {
            errorMessagePrefix = (StringUtils.isNotBlank(errorMessagePrefix)) ? errorMessagePrefix : "Field";
            String errorMsg = errorMessagePrefix + " with value '" + value + "' does not conform to standard validation for field type.";
            LOG.debug("validateSimpleSearchFieldValue: " + errorMsg + " :: field type '" + attributeValue.getAttributeDataType() + "'");
            errors.add(RemotableAttributeError.Builder.create(fieldName, errorMsg).build());
        } else if (customValidator != null) {
            errors.addAll(customValidator.apply(value));
        }

        return Collections.unmodifiableCollection(errors);
    }

    /**
     * Converts a searchable attribute field data type into a UI data type
     * @param dataTypeValue the {@link SearchableAttributeValue} data type
     * @return the corresponding {@link DataType}
     */
    public static DataType convertValueToDataType(String dataTypeValue) {
        if (StringUtils.isBlank(dataTypeValue)) {
            return DataType.STRING;
        } else if (KewApiConstants.SearchableAttributeConstants.DATA_TYPE_STRING.equals(dataTypeValue)) {
            return DataType.STRING;
        } else if (KewApiConstants.SearchableAttributeConstants.DATA_TYPE_DATE.equals(dataTypeValue)) {
            return DataType.DATE;
        } else if (KewApiConstants.SearchableAttributeConstants.DATA_TYPE_LONG.equals(dataTypeValue)) {
            return DataType.LONG;
        } else if (KewApiConstants.SearchableAttributeConstants.DATA_TYPE_FLOAT.equals(dataTypeValue)) {
            return DataType.FLOAT;
        }
        throw new IllegalArgumentException("Invalid dataTypeValue was given: " + dataTypeValue);
    }

}