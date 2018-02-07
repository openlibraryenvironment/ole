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

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.core.api.util.type.TypeUtils;
import org.kuali.rice.core.framework.persistence.jdbc.sql.SQLUtils;
import org.kuali.rice.core.web.format.DateFormatter;
import org.kuali.rice.kns.datadictionary.MaintainableFieldDefinition;
import org.kuali.rice.kns.datadictionary.MaintainableItemDefinition;
import org.kuali.rice.kns.datadictionary.MaintainableSectionDefinition;
import org.kuali.rice.kns.datadictionary.MaintenanceDocumentEntry;
import org.kuali.rice.kns.service.DictionaryValidationService;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.kns.service.WorkflowAttributePropertyResolutionService;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.datadictionary.control.ControlDefinition;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Deprecated
public class DictionaryValidationServiceImpl extends org.kuali.rice.krad.service.impl.DictionaryValidationServiceImpl implements DictionaryValidationService {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(
            DictionaryValidationServiceImpl.class);

    protected WorkflowAttributePropertyResolutionService workflowAttributePropertyResolutionService;

    /**
     * @see org.kuali.rice.krad.service.DictionaryValidationService#validateDocumentAndUpdatableReferencesRecursively(org.kuali.rice.krad.document.Document, int, boolean, boolean)
     * @deprecated since 2.1
     */
    @Override
    @Deprecated
    public void validateDocumentAndUpdatableReferencesRecursively(Document document, int maxDepth,
            boolean validateRequired, boolean chompLastLetterSFromCollectionName) {
        // Use the KNS validation code here -- this overrides the behavior in the krad version which calls validate(...)
        validateBusinessObject(document, validateRequired);

        if (maxDepth > 0) {
            validateUpdatabableReferencesRecursively(document, maxDepth - 1, validateRequired,
                    chompLastLetterSFromCollectionName, newIdentitySet());
        }
    }

    /**
     * @see org.kuali.rice.kns.service.DictionaryValidationService#validateDocumentRecursively(org.kuali.rice.krad.document.Document, int)
     * @deprecated since 2.0
     */
    @Deprecated
    @Override
    public void validateDocumentRecursively(Document document, int depth) {
        // validate primitives of document
        validateDocument(document);

        // call method to recursively find business objects and validate
        validateBusinessObjectsFromDescriptors(document, PropertyUtils.getPropertyDescriptors(document.getClass()),
                depth);
    }

    /**
     * @see org.kuali.rice.kns.service.DictionaryValidationService#validateDocument(org.kuali.rice.krad.document.Document)
     * @param document - document to validate
     * @deprecated since 2.1.2
     */
    @Deprecated
    @Override
    public void validateDocument(Document document) {
        String documentEntryName = document.getDocumentHeader().getWorkflowDocument().getDocumentTypeName();

        validatePrimitivesFromDescriptors(documentEntryName, document, PropertyUtils.getPropertyDescriptors(document.getClass()), "", true);
    }

    @Override
    @Deprecated
    public void validateBusinessObject(BusinessObject businessObject) {
        validateBusinessObject(businessObject, true);
    }

    @Override
    @Deprecated
    public void validateBusinessObject(BusinessObject businessObject, boolean validateRequired) {
        if (ObjectUtils.isNull(businessObject)) {
            return;
        }
        try {
            // validate the primitive attributes of the bo
            validatePrimitivesFromDescriptors(businessObject.getClass().getName(), businessObject,
                    PropertyUtils.getPropertyDescriptors(businessObject.getClass()), "", validateRequired);
        } catch (RuntimeException e) {
            LOG.error(String.format("Exception while validating %s", businessObject.getClass().getName()), e);
            throw e;
        }
    }

    /**
     * @deprecated since 1.1
     */
    @Deprecated
    @Override
    public void validateBusinessObjectOnMaintenanceDocument(BusinessObject businessObject, String docTypeName) {
        MaintenanceDocumentEntry entry =
                KNSServiceLocator.getMaintenanceDocumentDictionaryService().getMaintenanceDocumentEntry(docTypeName);
        for (MaintainableSectionDefinition sectionDefinition : entry.getMaintainableSections()) {
            validateBusinessObjectOnMaintenanceDocumentHelper(businessObject, sectionDefinition.getMaintainableItems(),
                    "");
        }
    }

    protected void validateBusinessObjectOnMaintenanceDocumentHelper(BusinessObject businessObject,
            List<? extends MaintainableItemDefinition> itemDefinitions, String errorPrefix) {
        for (MaintainableItemDefinition itemDefinition : itemDefinitions) {
            if (itemDefinition instanceof MaintainableFieldDefinition) {
                if (getDataDictionaryService().isAttributeDefined(businessObject.getClass(),
                        itemDefinition.getName())) {
                    Object value = ObjectUtils.getPropertyValue(businessObject, itemDefinition.getName());
                    if (value != null && StringUtils.isNotBlank(value.toString())) {
                        Class propertyType = ObjectUtils.getPropertyType(businessObject, itemDefinition.getName(),
                                persistenceStructureService);
                        if (TypeUtils.isStringClass(propertyType) ||
                                TypeUtils.isIntegralClass(propertyType) ||
                                TypeUtils.isDecimalClass(propertyType) ||
                                TypeUtils.isTemporalClass(propertyType)) {
                            // check value format against dictionary
                            if (!TypeUtils.isTemporalClass(propertyType)) {
                                validateAttributeFormat(businessObject.getClass().getName(), itemDefinition.getName(),
                                        value.toString(), errorPrefix + itemDefinition.getName());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * iterates through property descriptors looking for primitives types, calls validate format and required check
     *
     * @param entryName
     * @param object
     * @param propertyDescriptors
     * @param errorPrefix
     */
    @Deprecated
    protected void validatePrimitivesFromDescriptors(String entryName, Object object,
            PropertyDescriptor[] propertyDescriptors, String errorPrefix, boolean validateRequired) {
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            validatePrimitiveFromDescriptor(entryName, object, propertyDescriptor, errorPrefix, validateRequired);
        }
    }

    /**
     * calls validate format and required check for the given propertyDescriptor
     *
     * @param entryName
     * @param object
     * @param propertyDescriptor
     * @param errorPrefix
     */
    @Override
    @Deprecated
    public void validatePrimitiveFromDescriptor(String entryName, Object object, PropertyDescriptor propertyDescriptor,
            String errorPrefix, boolean validateRequired) {
        // validate the primitive attributes if defined in the dictionary
        if (null != propertyDescriptor && getDataDictionaryService().isAttributeDefined(entryName,
                propertyDescriptor.getName())) {
            Object value = ObjectUtils.getPropertyValue(object, propertyDescriptor.getName());
            Class propertyType = propertyDescriptor.getPropertyType();

            if (TypeUtils.isStringClass(propertyType) ||
                    TypeUtils.isIntegralClass(propertyType) ||
                    TypeUtils.isDecimalClass(propertyType) ||
                    TypeUtils.isTemporalClass(propertyType)) {

                // check value format against dictionary
                if (value != null && StringUtils.isNotBlank(value.toString())) {
                    if (!TypeUtils.isTemporalClass(propertyType)) {
                        validateAttributeFormat(entryName, propertyDescriptor.getName(), value.toString(),
                                errorPrefix + propertyDescriptor.getName());
                    }
                } else if (validateRequired) {
                    validateAttributeRequired(entryName, propertyDescriptor.getName(), value, Boolean.FALSE,
                            errorPrefix + propertyDescriptor.getName());
                }
            }
        }
    }

    /**
     * @see org.kuali.rice.kns.service.DictionaryValidationService#validateAttributeFormat(String, String, String, String)
     *      objectClassName is the docTypeName
     * @deprecated since 1.1
     */
    @Override
    @Deprecated
    public void validateAttributeFormat(String objectClassName, String attributeName, String attributeInValue,
            String errorKey) {
        // Retrieve the field's data type, or set to the string data type if an exception occurs when retrieving the class or the DD entry.
        String attributeDataType = null;
        try {
            attributeDataType = getWorkflowAttributePropertyResolutionService().determineFieldDataType(
                    (Class<? extends BusinessObject>) Class.forName(
                            getDataDictionaryService().getDataDictionary().getDictionaryObjectEntry(objectClassName)
                                    .getFullClassName()), attributeName);
        } catch (ClassNotFoundException e) {
            attributeDataType = KRADConstants.DATA_TYPE_STRING;
        } catch (NullPointerException e) {
            attributeDataType = KRADConstants.DATA_TYPE_STRING;
        }

        validateAttributeFormat(objectClassName, attributeName, attributeInValue, attributeDataType, errorKey);
    }

    /**
     * The attributeDataType parameter should be one of the data types specified by the SearchableAttribute
     * interface; will default to DATA_TYPE_STRING if a data type other than the ones from SearchableAttribute
     * is specified.
     *
     * @deprecated since 1.1
     */
    @Override
    @Deprecated
    public void validateAttributeFormat(String objectClassName, String attributeName, String attributeInValue,
            String attributeDataType, String errorKey) {
        boolean checkDateBounds = false; // this is used so we can check date bounds
        Class<?> formatterClass = null;

        if (LOG.isDebugEnabled()) {
            LOG.debug("(bo, attributeName, attributeValue) = (" + objectClassName + "," + attributeName + "," +
                    attributeInValue + ")");
        }

        /*
        *  This will return a list of searchable attributes. so if the value is
        *  12/07/09 .. 12/08/09 it will return [12/07/09,12/08/09]
        */

        final List<String> attributeValues = SQLUtils.getCleanedSearchableValues(attributeInValue, attributeDataType);

        if (attributeValues == null || attributeValues.isEmpty()) {
            return;
        }

        for (String attributeValue : attributeValues) {

            // FIXME: JLR : Replacing this logic with KS-style validation is trickier, since KS validation requires a DataProvider object that can
            // look back and find other attribute values aside from the one we're working on.
            // Also - the date stuff below is implemented very differently.
            //validator.validateAttributeField(businessObject, fieldName);

            if (StringUtils.isNotBlank(attributeValue)) {
                Integer minLength = getDataDictionaryService().getAttributeMinLength(objectClassName, attributeName);
                if ((minLength != null) && (minLength.intValue() > attributeValue.length())) {
                    String errorLabel = getDataDictionaryService().getAttributeErrorLabel(objectClassName,
                            attributeName);
                    GlobalVariables.getMessageMap().putError(errorKey, RiceKeyConstants.ERROR_MIN_LENGTH,
                            new String[]{errorLabel, minLength.toString()});
                    return;
                }
                Integer maxLength = getDataDictionaryService().getAttributeMaxLength(objectClassName, attributeName);
                if ((maxLength != null) && (maxLength.intValue() < attributeValue.length())) {
                    String errorLabel = getDataDictionaryService().getAttributeErrorLabel(objectClassName,
                            attributeName);
                    GlobalVariables.getMessageMap().putError(errorKey, RiceKeyConstants.ERROR_MAX_LENGTH,
                            new String[]{errorLabel, maxLength.toString()});
                    return;
                }
                Pattern validationExpression = getDataDictionaryService().getAttributeValidatingExpression(
                        objectClassName, attributeName);
                if (validationExpression != null && !validationExpression.pattern().equals(".*")) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("(bo, attributeName, validationExpression) = (" + objectClassName + "," +
                                attributeName + "," + validationExpression + ")");
                    }

                    if (!validationExpression.matcher(attributeValue).matches()) {
                        // Retrieving formatter class
                        if (formatterClass == null) {
                            // this is just a cache check... all dates ranges get called twice
                            formatterClass = getDataDictionaryService().getAttributeFormatter(objectClassName,
                                    attributeName);
                        }

                        if (formatterClass != null) {
                            boolean valuesAreValid = true;
                            boolean isError = true;
                            String errorKeyPrefix = "";
                            try {

                                // this is a special case for date ranges in order to set the proper error message
                                if (DateFormatter.class.isAssignableFrom(formatterClass)) {
                                    String[] values = attributeInValue.split("\\.\\."); // is it a range
                                    if (values.length == 2 &&
                                            attributeValues.size() == 2) { // make sure it's not like a .. b | c
                                        checkDateBounds = true; // now we need to check that a <= b
                                        if (attributeValues.indexOf(attributeValue) ==
                                                0) { // only care about lower bound
                                            errorKeyPrefix = KRADConstants.LOOKUP_RANGE_LOWER_BOUND_PROPERTY_PREFIX;
                                        }
                                    }
                                }

                                Method validatorMethod = formatterClass.getDeclaredMethod(VALIDATE_METHOD,
                                        new Class<?>[]{String.class});
                                Object o = validatorMethod.invoke(formatterClass.newInstance(), attributeValue);
                                if (o instanceof Boolean) {
                                    isError = !((Boolean) o).booleanValue();
                                }
                                valuesAreValid &= !isError;
                            } catch (Exception e) {
                                if (LOG.isDebugEnabled()) {
                                    LOG.debug(e.getMessage(), e);
                                }
                                isError = true;
                                valuesAreValid = false;
                            }
                            if (isError) {
                                checkDateBounds = false; // it's already invalid, no need to check date bounds
                                String errorMessageKey =
                                        getDataDictionaryService().getAttributeValidatingErrorMessageKey(
                                                objectClassName, attributeName);
                                String[] errorMessageParameters =
                                        getDataDictionaryService().getAttributeValidatingErrorMessageParameters(
                                                objectClassName, attributeName);
                                GlobalVariables.getMessageMap().putError(errorKeyPrefix + errorKey, errorMessageKey,
                                        errorMessageParameters);
                            }
                        } else {
                            // if it fails the default validation and has no formatter class then it's still a std failure.
                            String errorMessageKey = getDataDictionaryService().getAttributeValidatingErrorMessageKey(
                                    objectClassName, attributeName);
                            String[] errorMessageParameters =
                                    getDataDictionaryService().getAttributeValidatingErrorMessageParameters(
                                            objectClassName, attributeName);
                            GlobalVariables.getMessageMap().putError(errorKey, errorMessageKey, errorMessageParameters);
                        }
                    }
                }
                /*BigDecimal*/
                String exclusiveMin = getDataDictionaryService().getAttributeExclusiveMin(objectClassName,
                        attributeName);
                if (exclusiveMin != null) {
                    try {
                        BigDecimal exclusiveMinBigDecimal = new BigDecimal(exclusiveMin);
                        if (exclusiveMinBigDecimal.compareTo(new BigDecimal(attributeValue)) >= 0) {
                            String errorLabel = getDataDictionaryService().getAttributeErrorLabel(objectClassName,
                                    attributeName);
                            GlobalVariables.getMessageMap().putError(errorKey, RiceKeyConstants.ERROR_EXCLUSIVE_MIN,
                                    // todo: Formatter for currency?
                                    new String[]{errorLabel, exclusiveMin.toString()});
                            return;
                        }
                    } catch (NumberFormatException e) {
                        // quash; this indicates that the DD contained a min for a non-numeric attribute
                    }
                }
                /*BigDecimal*/
                String inclusiveMax = getDataDictionaryService().getAttributeInclusiveMax(objectClassName,
                        attributeName);
                if (inclusiveMax != null) {
                    try {
                        BigDecimal inclusiveMaxBigDecimal = new BigDecimal(inclusiveMax);
                        if (inclusiveMaxBigDecimal.compareTo(new BigDecimal(attributeValue)) < 0) {
                            String errorLabel = getDataDictionaryService().getAttributeErrorLabel(objectClassName,
                                    attributeName);
                            GlobalVariables.getMessageMap().putError(errorKey, RiceKeyConstants.ERROR_INCLUSIVE_MAX,
                                    // todo: Formatter for currency?
                                    new String[]{errorLabel, inclusiveMax.toString()});
                            return;
                        }
                    } catch (NumberFormatException e) {
                        // quash; this indicates that the DD contained a max for a non-numeric attribute
                    }
                }
            }
        }

        if (checkDateBounds) {
            // this means that we only have 2 values and it's a date range.
            java.sql.Timestamp lVal = null;
            java.sql.Timestamp uVal = null;
            try {
                lVal = CoreApiServiceLocator.getDateTimeService().convertToSqlTimestamp(attributeValues.get(0));
                uVal = CoreApiServiceLocator.getDateTimeService().convertToSqlTimestamp(attributeValues.get(1));
            } catch (Exception ex) {
                // this shouldn't happen because the tests passed above.
                String errorMessageKey = getDataDictionaryService().getAttributeValidatingErrorMessageKey(
                        objectClassName, attributeName);
                String[] errorMessageParameters =
                        getDataDictionaryService().getAttributeValidatingErrorMessageParameters(objectClassName,
                                attributeName);
                GlobalVariables.getMessageMap().putError(
                        KRADConstants.LOOKUP_RANGE_LOWER_BOUND_PROPERTY_PREFIX + errorKey, errorMessageKey,
                        errorMessageParameters);
            }

            if (lVal != null && lVal.compareTo(uVal) > 0) { // check the bounds
                String errorMessageKey = getDataDictionaryService().getAttributeValidatingErrorMessageKey(
                        objectClassName, attributeName);
                String[] errorMessageParameters =
                        getDataDictionaryService().getAttributeValidatingErrorMessageParameters(objectClassName,
                                attributeName);
                GlobalVariables.getMessageMap().putError(
                        KRADConstants.LOOKUP_RANGE_LOWER_BOUND_PROPERTY_PREFIX + errorKey, errorMessageKey + ".range",
                        errorMessageParameters);
            }
        }
    }

    // FIXME: JLR - this is now redundant and should be using the same code as the required processing elsewhere, but the control definition stuff doesn't really fit
    // it doesn't seem to be used anywhere
    @Override
    @Deprecated
    public void validateAttributeRequired(String objectClassName, String attributeName, Object attributeValue,
            Boolean forMaintenance, String errorKey) {
        // check if field is a required field for the business object
        if (attributeValue == null || (attributeValue instanceof String && StringUtils.isBlank(
                (String) attributeValue))) {
            Boolean required = getDataDictionaryService().isAttributeRequired(objectClassName, attributeName);
            ControlDefinition controlDef = getDataDictionaryService().getAttributeControlDefinition(objectClassName,
                    attributeName);

            if (required != null && required.booleanValue() && !(controlDef != null && controlDef.isHidden())) {

                // get label of attribute for message
                String errorLabel = getDataDictionaryService().getAttributeErrorLabel(objectClassName, attributeName);
                GlobalVariables.getMessageMap().putError(errorKey, RiceKeyConstants.ERROR_REQUIRED, errorLabel);
            }
        }
    }

    /**
     * gets the locally saved instance of @{link WorkflowAttributePropertyResolutionService}
     *
     * <p>If the instance in this class has not been initialized, retrieve it using
     * {@link KNSServiceLocator#getWorkflowAttributePropertyResolutionService()} and save locally</p>
     *
     * @return the locally saved instance of {@code WorkflowAttributePropertyResolutionService}
     */
    protected WorkflowAttributePropertyResolutionService getWorkflowAttributePropertyResolutionService() {
        if (workflowAttributePropertyResolutionService == null) {
            workflowAttributePropertyResolutionService =
                    KNSServiceLocator.getWorkflowAttributePropertyResolutionService();
        }
        return workflowAttributePropertyResolutionService;
    }
}
