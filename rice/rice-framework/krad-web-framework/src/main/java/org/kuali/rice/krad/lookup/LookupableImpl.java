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
package org.kuali.rice.krad.lookup;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.encryption.EncryptionService;
import org.kuali.rice.core.api.search.SearchOperator;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.core.api.util.type.TypeUtils;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.bo.ExternalizableBusinessObject;
import org.kuali.rice.krad.datadictionary.BusinessObjectEntry;
import org.kuali.rice.krad.datadictionary.RelationshipDefinition;
import org.kuali.rice.krad.service.DataObjectAuthorizationService;
import org.kuali.rice.krad.service.DataObjectMetaDataService;
import org.kuali.rice.krad.service.DocumentDictionaryService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.LookupService;
import org.kuali.rice.krad.service.ModuleService;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.UifPropertyPaths;
import org.kuali.rice.krad.uif.control.Control;
import org.kuali.rice.krad.uif.control.HiddenControl;
import org.kuali.rice.krad.uif.control.ValueConfiguredControl;
import org.kuali.rice.krad.uif.element.Action;
import org.kuali.rice.krad.uif.field.InputField;
import org.kuali.rice.krad.uif.field.LookupInputField;
import org.kuali.rice.krad.uif.service.impl.ViewHelperServiceImpl;
import org.kuali.rice.krad.uif.util.ComponentUtils;
import org.kuali.rice.krad.uif.util.LookupInquiryUtils;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.uif.view.LookupView;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.util.BeanPropertyComparator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADUtils;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.krad.util.UrlFactory;
import org.kuali.rice.krad.web.form.LookupForm;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Default implementation of <code>Lookupable</code>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class LookupableImpl extends ViewHelperServiceImpl implements Lookupable {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(LookupableImpl.class);

    private Class<?> dataObjectClass;

    private transient ConfigurationService configurationService;
    private transient DataObjectAuthorizationService dataObjectAuthorizationService;
    private transient DataObjectMetaDataService dataObjectMetaDataService;
    private transient DocumentDictionaryService documentDictionaryService;
    private transient LookupService lookupService;
    private transient EncryptionService encryptionService;

    /**
     * Initialization of Lookupable requires that the business object class be set for the
     * {@link #initializeDataFieldFromDataDictionary(org.kuali.rice.krad.uif.view.View,
     * org.kuali.rice.krad.uif.field.DataField)} method
     *
     * @see org.kuali.rice.krad.uif.service.impl.ViewHelperServiceImpl#performInitialization(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object)
     */
    @Override
    public void performInitialization(View view, Object model) {
        if (!LookupView.class.isAssignableFrom(view.getClass())) {
            throw new IllegalArgumentException(
                    "View class '" + view.getClass() + " is not assignable from the '" + LookupView.class + "'");
        }

        LookupView lookupView = (LookupView) view;
        setDataObjectClass(lookupView.getDataObjectClassName());

        super.performInitialization(view, model);
    }

    /**
     * @see org.kuali.rice.krad.lookup.Lookupable#initSuppressAction(org.kuali.rice.krad.web.form.LookupForm)
     */
    @Override
    public void initSuppressAction(LookupForm lookupForm) {
        LookupViewAuthorizerBase lookupAuthorizer = (LookupViewAuthorizerBase) lookupForm.getView().getAuthorizer();
        Person user = GlobalVariables.getUserSession().getPerson();
        ((LookupView) lookupForm.getView()).setSuppressActions(!lookupAuthorizer.canInitiateDocument(lookupForm, user));
    }

    /**
     * @see org.kuali.rice.krad.lookup.Lookupable#performSearch
     */
    @Override
    public Collection<?> performSearch(LookupForm form, Map<String, String> searchCriteria, boolean bounded) {
        Collection<?> displayList;

        // TODO: force uppercase will be done in binding at some point
        displayList = getSearchResults(form, LookupUtils.forceUppercase(getDataObjectClass(), searchCriteria),
                !bounded);

        // TODO delyea - is this the best way to set that the entire set has a returnable row?
        for (Object object : displayList) {
            if (isResultReturnable(object)) {
                form.setAtLeastOneRowReturnable(true);
            }
        }

        return displayList;
    }

    /**
     * Get the search results of the lookup
     *
     * @param form lookup form instance containing the lookup data
     * @param searchCriteria map of criteria currently set
     * @param unbounded indicates whether the complete result should be returned.  When set to false the result is
     * limited (if necessary) to the max search result limit configured.
     * @return the list of result objects, possibly bounded
     */
    protected List<?> getSearchResults(LookupForm form, Map<String, String> searchCriteria, boolean unbounded) {
        Collection<?> searchResults;

        // removed blank search values and decrypt any encrypted search values
        Map<String, String> nonBlankSearchCriteria = processSearchCriteria(form, searchCriteria);

        // return empty search results (none found) when the search doesn't have any nonBlankSearchCriteria although
        // a filtered search criteria is specified
        if (nonBlankSearchCriteria == null) {
            return new ArrayList<Object>();
        }

        // if this class is an EBO, just call the module service to get the results
        if (ExternalizableBusinessObject.class.isAssignableFrom(getDataObjectClass())) {
            return getSearchResultsForEBO(nonBlankSearchCriteria, unbounded);
        }

        // if any of the properties refer to an embedded EBO, call the EBO
        // lookups first and apply to the local lookup
        try {
            Integer searchResultsLimit = null;

            if (!unbounded) {
                searchResultsLimit = LookupUtils.getSearchResultsLimit(getDataObjectClass(), form);
            }

            if (LookupUtils.hasExternalBusinessObjectProperty(getDataObjectClass(), nonBlankSearchCriteria)) {
                Map<String, String> eboSearchCriteria = adjustCriteriaForNestedEBOs(nonBlankSearchCriteria, unbounded);

                if (LOG.isDebugEnabled()) {
                    LOG.debug("Passing these results into the lookup service: " + eboSearchCriteria);
                }

                // add those results as criteria run the normal search (but with the EBO criteria added)
                searchResults = getLookupService().findCollectionBySearchHelper(getDataObjectClass(), eboSearchCriteria,
                        unbounded, searchResultsLimit);
                generateLookupResultsMessages(form, eboSearchCriteria, searchResults, unbounded);
            } else {
                searchResults = getLookupService().findCollectionBySearchHelper(getDataObjectClass(),
                        nonBlankSearchCriteria, unbounded, searchResultsLimit);
                generateLookupResultsMessages(form, nonBlankSearchCriteria, searchResults, unbounded);
            }

        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error trying to perform search", e);
        } catch (InstantiationException e1) {
            throw new RuntimeException("Error trying to perform search", e1);
        }

        if (searchResults == null) {
            searchResults = new ArrayList<Object>();
        } else {
            sortSearchResults(form, (List<?>) searchResults);
        }

        return (List<?>) searchResults;
    }

    /**
     * Convenience method for setting an error message on the lookup results section
     *
     * @param form
     * @param messageToDisplay
     */
    public void generateErrorMessageForResults(LookupForm form, String messageToDisplay) {
        GlobalVariables.getMessageMap().putErrorForSectionId("LookupResultMessages", messageToDisplay);
    }

    /**
     * Helper function to render lookup results messages
     *
     * @param form
     * @param searchCriteria
     * @param searchResult
     * @param unbounded
     */
    protected void generateLookupResultsMessages(LookupForm form, Map<String, String> searchCriteria,
            Collection<?> searchResult, boolean unbounded) {
        String resultsPropertyName = "LookupResultMessages";
        List<String> pkLabels = new ArrayList<String>();

        Boolean usingPrimaryKey = getLookupService().allPrimaryKeyValuesPresentAndNotWildcard(getDataObjectClass(),
                (Map<String, String>) searchCriteria);

        Integer searchResultsLimit = LookupUtils.getSearchResultsLimit(getDataObjectClass(), form);
        Long searchResultsSize = Long.valueOf(0);

        if (searchResult instanceof CollectionIncomplete
                && ((CollectionIncomplete) searchResult).getActualSizeIfTruncated() > 0) {
            searchResultsSize = ((CollectionIncomplete) searchResult).getActualSizeIfTruncated();
        } else if (searchResult != null) {
            searchResultsSize = Long.valueOf(searchResult.size());
        }

        Boolean resultsExceedsLimit = !unbounded
                && searchResultsLimit != null
                && searchResultsSize > 0
                && searchResultsSize > searchResultsLimit ? true : false;

        if (usingPrimaryKey) {
            List<String> pkNames = getDataObjectMetaDataService().listPrimaryKeyFieldNames(getDataObjectClass());
            for (String pkName : pkNames) {
                pkLabels.add(getDataDictionaryService().getAttributeLabel(getDataObjectClass(), pkName));
            }

            GlobalVariables.getMessageMap().putInfoForSectionId(resultsPropertyName,
                    RiceKeyConstants.INFO_LOOKUP_RESULTS_USING_PRIMARY_KEY, StringUtils.join(pkLabels, ","));
        }

        if (searchResultsSize == 0) {
            GlobalVariables.getMessageMap().putInfoForSectionId(resultsPropertyName,
                    RiceKeyConstants.INFO_LOOKUP_RESULTS_NONE_FOUND);
        } else if (searchResultsSize == 1) {
            GlobalVariables.getMessageMap().putInfoForSectionId(resultsPropertyName,
                    RiceKeyConstants.INFO_LOOKUP_RESULTS_DISPLAY_ONE);
        } else if (searchResultsSize > 1) {
            if (resultsExceedsLimit) {
                GlobalVariables.getMessageMap().putInfoForSectionId(resultsPropertyName,
                        RiceKeyConstants.INFO_LOOKUP_RESULTS_EXCEEDS_LIMIT, searchResultsSize.toString(),
                        searchResultsLimit.toString());
            } else {
                GlobalVariables.getMessageMap().putInfoForSectionId(resultsPropertyName,
                        RiceKeyConstants.INFO_LOOKUP_RESULTS_DISPLAY_ALL, searchResultsSize.toString());
            }
        }
    }

    /**
     * Sorts the given list of search results based on the lookup view's configured sort attributes
     *
     * <p>
     * First if the posted view exists we grab the sort attributes from it. This will take into account expressions
     * that might have been configured on the sort attributes. If the posted view does not exist (because we did a
     * search from a get request or form session storage is off), we get the sort attributes from the view that we
     * will be rendered (and was initialized before controller call). However, expressions will not be evaluated yet,
     * thus if expressions were configured we don't know the results and can not sort the list
     * </p>
     *
     * @param form - lookup form instance containing view information
     * @param searchResults - list of search results to sort
     * @TODO: revisit this when we have a solution for the posted view problem
     */
    protected void sortSearchResults(LookupForm form, List<?> searchResults) {
        List<String> defaultSortColumns = null;
        boolean defaultSortAscending = true;
        // first choice is to get default sort columns off posted view, since that will include the full
        // lifecycle and expression evaluations
        if (form.getPostedView() != null) {
            defaultSortColumns = ((LookupView) form.getPostedView()).getDefaultSortAttributeNames();
            defaultSortAscending = ((LookupView) form.getPostedView()).isDefaultSortAscending();
        }
        // now try view being built, if default sort attributes have any expression (entry is null) we can't use them
        else if (form.getView() != null) {
            defaultSortColumns = ((LookupView) form.getView()).getDefaultSortAttributeNames();
            defaultSortAscending = ((LookupView) form.getView()).isDefaultSortAscending();
            boolean hasExpression = false;
            if (defaultSortColumns != null) {
                for (String sortColumn : defaultSortColumns) {
                    if (sortColumn == null) {
                        hasExpression = true;
                    }
                }
            }

            if (hasExpression) {
                defaultSortColumns = null;
            }
        }

        if ((defaultSortColumns != null) && (!defaultSortColumns.isEmpty())) {
            BeanPropertyComparator comparator = new BeanPropertyComparator(defaultSortColumns, true);
            if (defaultSortAscending) {
                Collections.sort(searchResults, comparator);
            } else {
                Collections.sort(searchResults, Collections.reverseOrder(comparator));
            }
        }
    }

    /**
     * Process the search criteria to be used with the lookup
     *
     * <p>
     * Processing entails primarily of the removal of filtered and unused/blank search criteria.  Encrypted field
     * values are decrypted in this process as well.
     * </p>
     *
     * @param lookupForm lookup form instance containing the lookup data
     * @param searchCriteria map of criteria currently set
     * @return map with the non blank search criteria
     */
    protected Map<String, String> processSearchCriteria(LookupForm lookupForm, Map<String, String> searchCriteria) {
        Map<String, InputField> criteriaFields = new HashMap<String, InputField>();
        if (lookupForm.getPostedView() != null) {
            criteriaFields = getCriteriaFieldsForValidation((LookupView) lookupForm.getPostedView(), lookupForm);
        }

        Map<String, String> filteredSearchCriteria = new HashMap<String, String>(searchCriteria);
        for (String fieldName : searchCriteria.keySet()) {
            InputField inputField = criteriaFields.get(fieldName);
            if ((inputField == null) || !(inputField instanceof LookupInputField)) {
                continue;
            }

            filteredSearchCriteria = ((LookupInputField) inputField).filterSearchCriteria(filteredSearchCriteria);
            if (filteredSearchCriteria == null) {
                return null;
            }
        }

        Map<String, String> nonBlankSearchCriteria = new HashMap<String, String>();
        for (String fieldName : filteredSearchCriteria.keySet()) {
            String fieldValue = filteredSearchCriteria.get(fieldName);

            // don't add hidden criteria
            InputField inputField = criteriaFields.get(fieldName);
            if ((inputField != null) && (inputField.getControl() instanceof HiddenControl)) {
                continue;
            }

            // only add criteria if non blank
            if (StringUtils.isNotBlank(fieldValue)) {
                if (fieldValue.endsWith(EncryptionService.ENCRYPTION_POST_PREFIX)) {
                    String encryptedValue = StringUtils.removeEnd(fieldValue, EncryptionService.ENCRYPTION_POST_PREFIX);
                    try {
                        if (CoreApiServiceLocator.getEncryptionService().isEnabled()) {
                            fieldValue = getEncryptionService().decrypt(encryptedValue);
                        }
                    } catch (GeneralSecurityException e) {
                        LOG.error("Error decrypting value for business object class " + getDataObjectClass() +
                                " attribute " + fieldName, e);
                        throw new RuntimeException(
                                "Error decrypting value for business object class " + getDataObjectClass() +
                                        " attribute " + fieldName, e);
                    }
                }

                nonBlankSearchCriteria.put(fieldName, fieldValue);
            }
        }

        return nonBlankSearchCriteria;
    }

    /**
     * Get the search results of an {@linkExternalizableBusinessObject}
     *
     * @param searchCriteria map of criteria currently set
     * @param unbounded indicates whether the complete result should be returned.  When set to false the result is
     * limited (if necessary) to the max search result limit configured.
     * @return list of result objects, possibly bounded
     */
    protected List<?> getSearchResultsForEBO(Map<String, String> searchCriteria, boolean unbounded) {
        ModuleService eboModuleService = KRADServiceLocatorWeb.getKualiModuleService().getResponsibleModuleService(
                getDataObjectClass());
        BusinessObjectEntry ddEntry = eboModuleService.getExternalizableBusinessObjectDictionaryEntry(
                getDataObjectClass());

        Map<String, String> filteredFieldValues = new HashMap<String, String>();
        for (String fieldName : searchCriteria.keySet()) {
            if (ddEntry.getAttributeNames().contains(fieldName)) {
                filteredFieldValues.put(fieldName, searchCriteria.get(fieldName));
            }
        }

        List<?> searchResults = eboModuleService.getExternalizableBusinessObjectsListForLookup(
                (Class<? extends ExternalizableBusinessObject>) getDataObjectClass(), (Map) filteredFieldValues,
                unbounded);

        return searchResults;
    }

    /**
     * @param searchCriteria map of criteria currently set
     * @param unbounded indicates whether the complete result should be returned.  When set to false the result is
     * limited (if necessary) to the max search result limit configured.
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    protected Map<String, String> adjustCriteriaForNestedEBOs(Map<String, String> searchCriteria,
            boolean unbounded) throws InstantiationException, IllegalAccessException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("has EBO reference: " + getDataObjectClass());
            LOG.debug("properties: " + searchCriteria);
        }

        // remove the EBO criteria
        Map<String, String> nonEboFieldValues = LookupUtils.removeExternalizableBusinessObjectFieldValues(
                getDataObjectClass(), searchCriteria);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Non EBO properties removed: " + nonEboFieldValues);
        }

        // get the list of EBO properties attached to this object
        List<String> eboPropertyNames = LookupUtils.getExternalizableBusinessObjectProperties(getDataObjectClass(),
                searchCriteria);
        if (LOG.isDebugEnabled()) {
            LOG.debug("EBO properties: " + eboPropertyNames);
        }

        // loop over those properties
        for (String eboPropertyName : eboPropertyNames) {
            // extract the properties as known to the EBO
            Map<String, String> eboFieldValues = LookupUtils.getExternalizableBusinessObjectFieldValues(eboPropertyName,
                    searchCriteria);
            if (LOG.isDebugEnabled()) {
                LOG.debug("EBO properties for master EBO property: " + eboPropertyName);
                LOG.debug("properties: " + eboFieldValues);
            }

            // run search against attached EBO's module service
            ModuleService eboModuleService = KRADServiceLocatorWeb.getKualiModuleService().getResponsibleModuleService(
                    LookupUtils.getExternalizableBusinessObjectClass(getDataObjectClass(), eboPropertyName));

            // KULRICE-4401 made eboResults an empty list and only filled if
            // service is found.
            List<?> eboResults = Collections.emptyList();
            if (eboModuleService != null) {
                eboResults = eboModuleService.getExternalizableBusinessObjectsListForLookup(
                        LookupUtils.getExternalizableBusinessObjectClass(getDataObjectClass(), eboPropertyName),
                        (Map) eboFieldValues, unbounded);
            } else {
                LOG.debug("EBO ModuleService is null: " + eboPropertyName);
            }
            // get the mapping/relationship between the EBO object and it's
            // parent object
            // use that to adjust the searchCriteria

            // get the parent property type
            Class<?> eboParentClass;
            String eboParentPropertyName;
            if (ObjectUtils.isNestedAttribute(eboPropertyName)) {
                eboParentPropertyName = StringUtils.substringBeforeLast(eboPropertyName, ".");
                try {
                    eboParentClass = PropertyUtils.getPropertyType(getDataObjectClass().newInstance(),
                            eboParentPropertyName);
                } catch (Exception ex) {
                    throw new RuntimeException(
                            "Unable to create an instance of the business object class: " + getDataObjectClass()
                                    .getName(), ex);
                }
            } else {
                eboParentClass = getDataObjectClass();
                eboParentPropertyName = null;
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("determined EBO parent class/property name: " + eboParentClass + "/" + eboParentPropertyName);
            }

            // look that up in the DD (BOMDS)
            // find the appropriate relationship
            // CHECK THIS: what if eboPropertyName is a nested attribute -
            // need to strip off the eboParentPropertyName if not null
            RelationshipDefinition rd = getDataObjectMetaDataService().getDictionaryRelationship(eboParentClass,
                    eboPropertyName);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Obtained RelationshipDefinition for " + eboPropertyName);
                LOG.debug(rd);
            }

            // copy the needed properties (primary only) to the field values KULRICE-4446 do
            // so only if the relationship definition exists
            // NOTE: this will work only for single-field PK unless the ORM
            // layer is directly involved
            // (can't make (field1,field2) in ( (v1,v2),(v3,v4) ) style
            // queries in the lookup framework
            if (ObjectUtils.isNotNull(rd)) {
                if (rd.getPrimitiveAttributes().size() > 1) {
                    throw new RuntimeException(
                            "EBO Links don't work for relationships with multiple-field primary keys.");
                }
                String boProperty = rd.getPrimitiveAttributes().get(0).getSourceName();
                String eboProperty = rd.getPrimitiveAttributes().get(0).getTargetName();
                StringBuffer boPropertyValue = new StringBuffer();

                // loop over the results, making a string that the lookup
                // DAO will convert into an
                // SQL "IN" clause
                for (Object ebo : eboResults) {
                    if (boPropertyValue.length() != 0) {
                        boPropertyValue.append(SearchOperator.OR.op());
                    }
                    try {
                        boPropertyValue.append(PropertyUtils.getProperty(ebo, eboProperty).toString());
                    } catch (Exception ex) {
                        LOG.warn("Unable to get value for " + eboProperty + " on " + ebo);
                    }
                }

                if (eboParentPropertyName == null) {
                    // non-nested property containing the EBO
                    nonEboFieldValues.put(boProperty, boPropertyValue.toString());
                } else {
                    // property nested within the main searched-for BO that
                    // contains the EBO
                    nonEboFieldValues.put(eboParentPropertyName + "." + boProperty, boPropertyValue.toString());
                }
            }
        }

        return nonEboFieldValues;
    }

    /**
     * @see org.kuali.rice.krad.lookup.Lookupable#performClear
     */
    @Override
    public Map<String, String> performClear(LookupForm form, Map<String, String> searchCriteria) {
        Map<String, InputField> criteriaFieldMap = new HashMap<String, InputField>();
        if (form.getPostedView() == null) {
            criteriaFieldMap = getCriteriaFieldsForValidation((LookupView) form.getPostedView(), form);
        }

        List<String> readOnlyFieldsList = form.getReadOnlyFieldsList();

        Map<String, String> clearedSearchCriteria = new HashMap<String, String>();
        for (Map.Entry<String, String> searchKeyValue : searchCriteria.entrySet()) {
            String searchPropertyName = searchKeyValue.getKey();

            InputField inputField = criteriaFieldMap.get(searchPropertyName);

            if (readOnlyFieldsList != null && readOnlyFieldsList.contains(searchPropertyName)) {
                clearedSearchCriteria.put(searchPropertyName, searchKeyValue.getValue());
            } else if (inputField != null) {
                // TODO: check secure fields
                //                                if (field.isSecure()) {
                //                    field.setSecure(false);
                //                    field.setDisplayMaskValue(null);
                //                    field.setEncryptedValue(null);
                //                }

                // TODO: need formatting on default value and make sure it works when control converts
                // from checkbox to radio
                clearedSearchCriteria.put(searchPropertyName, inputField.getDefaultValue());
            } else {
                clearedSearchCriteria.put(searchPropertyName, "");
            }
        }

        return clearedSearchCriteria;
    }

    /**
     * @see org.kuali.rice.krad.lookup.Lookupable#validateSearchParameters
     */
    @Override
    public boolean validateSearchParameters(LookupForm form, Map<String, String> searchCriteria) {
        boolean valid = true;

        // if postedView is null then we are executing the search from get request, in which case we
        // can't validate the criteria
        if (form.getPostedView() == null) {
            return valid;
        }

        Map<String, InputField> criteriaFields = getCriteriaFieldsForValidation((LookupView) form.getPostedView(),
                form);

        // build list of hidden properties configured with criteria fields
        List<String> hiddenCriteria = new ArrayList<String>();
        for (InputField field : criteriaFields.values()) {
            if (field.getAdditionalHiddenPropertyNames() != null) {
                hiddenCriteria.addAll(field.getAdditionalHiddenPropertyNames());
            }
        }

        // validate required
        // TODO: this will be done by the uif validation service at some point
        for (Map.Entry<String, String> searchKeyValue : searchCriteria.entrySet()) {
            String searchPropertyName = searchKeyValue.getKey();
            String searchPropertyValue = searchKeyValue.getValue();

            InputField inputField = criteriaFields.get(searchPropertyName);

            String adjustedSearchPropertyPath = UifPropertyPaths.LOOKUP_CRITERIA + "[" + searchPropertyName + "]";
            if (inputField == null && hiddenCriteria.contains(adjustedSearchPropertyPath)) {
                return valid;
            }

            // verify the property sent is a valid to search on
            if ((inputField == null) && !searchPropertyName.contains(
                    KRADConstants.LOOKUP_RANGE_LOWER_BOUND_PROPERTY_PREFIX)) {
                throw new RuntimeException("Invalid search field sent for property name: " + searchPropertyName);
            }

            if (inputField != null) {
                if (StringUtils.isBlank(searchPropertyValue) && inputField.getRequired()) {
                    GlobalVariables.getMessageMap().putError(inputField.getPropertyName(),
                            RiceKeyConstants.ERROR_REQUIRED, inputField.getLabel());
                }

                validateSearchParameterWildcardAndOperators(inputField, searchPropertyValue);
            }
        }
        if (GlobalVariables.getMessageMap().hasErrors()) {
            valid = false;
        }

        return valid;
    }

    /**
     * Returns the criteria fields in a map keyed by the field property name.
     *
     * @param lookupView
     * @param form lookup form instance containing the lookup data
     * @return map of criteria fields
     */
    protected Map<String, InputField> getCriteriaFieldsForValidation(LookupView lookupView, LookupForm form) {
        Map<String, InputField> criteriaFieldMap = new HashMap<String, InputField>();

        if (lookupView.getCriteriaFields() == null) {
            return criteriaFieldMap;
        }

        // TODO; need hooks for code generated components and also this doesn't have lifecycle which
        // could change fields
        List<InputField> fields = ComponentUtils.getComponentsOfTypeDeep(lookupView.getCriteriaFields(),
                InputField.class);
        for (InputField field : fields) {
            criteriaFieldMap.put(field.getPropertyName(), field);
        }

        return criteriaFieldMap;
    }

    /**
     * Validates that any wildcards contained within the search value are valid wilcards and allowed for the
     * property type for which the field is searching
     *
     * @param inputField - attribute field instance for the field that is being searched
     * @param searchPropertyValue - value given for field to search for
     */
    protected void validateSearchParameterWildcardAndOperators(InputField inputField, String searchPropertyValue) {
        if (StringUtils.isBlank(searchPropertyValue)) {
            return;
        }

        // make sure a wildcard/operator is in the value
        boolean found = false;
        for (SearchOperator op : SearchOperator.QUERY_CHARACTERS) {
            String queryCharacter = op.op();

            if (searchPropertyValue.contains(queryCharacter)) {
                found = true;
            }
        }

        if (!found) {
            return;
        }

        String attributeLabel = inputField.getLabel();
        if ((LookupInputField.class.isAssignableFrom(inputField.getClass())) && (((LookupInputField) inputField)
                .isDisableWildcardsAndOperators())) {
            Object dataObjectExample = null;
            try {
                dataObjectExample = getDataObjectClass().newInstance();
            } catch (Exception e) {
                LOG.error("Exception caught instantiating " + getDataObjectClass().getName(), e);
                throw new RuntimeException("Cannot instantiate " + getDataObjectClass().getName(), e);
            }

            Class<?> propertyType = ObjectPropertyUtils.getPropertyType(getDataObjectClass(),
                    inputField.getPropertyName());
            if (TypeUtils.isIntegralClass(propertyType) || TypeUtils.isDecimalClass(propertyType) ||
                    TypeUtils.isTemporalClass(propertyType)) {
                GlobalVariables.getMessageMap().putError(inputField.getPropertyName(),
                        RiceKeyConstants.ERROR_WILDCARDS_AND_OPERATORS_NOT_ALLOWED_ON_FIELD, attributeLabel);
            }

            if (TypeUtils.isStringClass(propertyType)) {
                GlobalVariables.getMessageMap().putInfo(inputField.getPropertyName(),
                        RiceKeyConstants.INFO_WILDCARDS_AND_OPERATORS_TREATED_LITERALLY, attributeLabel);
            }
        } else {
            if (getDataObjectAuthorizationService().attributeValueNeedsToBeEncryptedOnFormsAndLinks(
                    getDataObjectClass(), inputField.getPropertyName())) {
                if (!searchPropertyValue.endsWith(EncryptionService.ENCRYPTION_POST_PREFIX)) {
                    // encrypted values usually come from the DB, so we don't
                    // need to filter for wildcards
                    // wildcards are not allowed on restricted fields, because
                    // they are typically encrypted, and wildcard searches cannot be performed without
                    // decrypting every row, which is currently not supported by KRAD

                    GlobalVariables.getMessageMap().putError(inputField.getPropertyName(),
                            RiceKeyConstants.ERROR_SECURE_FIELD, attributeLabel);
                }
            }
        }
    }

    /**
     * @see org.kuali.rice.krad.lookup.Lookupable#getReturnUrlForResults
     */
    public void getReturnUrlForResults(Action returnLink, Object model) {
        LookupForm lookupForm = (LookupForm) model;
        
        Map<String, Object> returnLinkContext = returnLink.getContext();
        LookupView lookupView = returnLinkContext == null ? null : (LookupView) returnLinkContext
                .get(UifConstants.ContextVariableNames.VIEW);
        Object dataObject = returnLinkContext == null ? null : returnLinkContext
                .get(UifConstants.ContextVariableNames.LINE);

        // don't render return link if the object is null or if the row is not returnable
        if ((dataObject == null) || (!isResultReturnable(dataObject))) {
            returnLink.setRender(false);
            return;
        }

        // build return link href (href may contain single quotes)
        String href = getReturnUrl(lookupView, lookupForm, dataObject);
        if (StringUtils.isBlank(href)) {
            returnLink.setRender(false);
            return;
        }

        // build return link label and title
        String linkLabel = getConfigurationService().getPropertyValueAsString(
                KRADConstants.Lookup.TITLE_RETURN_URL_PREPENDTEXT_PROPERTY);
        returnLink.setActionLabel(linkLabel);

        List<String> returnKeys = getReturnKeys(lookupView, lookupForm, dataObject);
        List<String> secureReturnKeys = lookupView.getAdditionalSecurePropertyNames();
        Map<String, String> returnKeyValues = KRADUtils.getPropertyKeyValuesFromDataObject(returnKeys, secureReturnKeys, dataObject);

        String title = LookupInquiryUtils.getLinkTitleText(linkLabel, getDataObjectClass(), returnKeyValues);
        returnLink.setTitle(title);

        // Add the return target if it is set
        String returnTarget = lookupView.getReturnTarget();
        if (returnTarget != null) {
            returnLink.setActionScript("window.open(\"" + href + "\", '" + returnTarget + "');");

            //  Add the close script if lookup is in a light box
            if (!returnTarget.equals("_self")) {
                // Add the return script if the returnByScript flag is set
                if (lookupView.isReturnByScript()) {
                    Properties props = getReturnUrlParameters(lookupView, lookupForm, dataObject);

                    StringBuilder script = new StringBuilder("e.preventDefault();");
                    for (String returnField : lookupForm.getFieldConversions().values()) {
                        if (props.containsKey(returnField)) {
                            Object value = props.get(returnField);
                            script = script.append(
                                    "returnLookupResultByScript(\"" + returnField + "\", '" + value + "');");
                        }
                    }
                    returnLink.setActionScript(script.append("closeLightbox();").toString());
                } else {
                    // Close the light box if return target is not _self or _parent
                    returnLink.setActionScript("e.preventDefault();closeLightbox();showLoading();" +
                            "returnLookupResultReload(\"" + href + "\", '" + returnTarget + "');");
                }
            }
        } else {
            // If no return target is set return in same frame
            // This is to insure that non light box lookups return correctly
            returnLink.setActionScript("window.open(\"" + href + "\", '_self');");
        }
    }

    /**
     * Builds the URL for returning the given data object result row
     *
     * <p>
     * Note return URL will only be built if a return location is specified on the <code>LookupForm</code>
     * </p>
     *
     * @param lookupView - lookup view instance containing lookup configuration
     * @param lookupForm - lookup form instance containing the data
     * @param dataObject - data object instance for the current line and for which the return URL is being built
     * @return String return URL or blank if URL cannot be built
     */
    protected String getReturnUrl(LookupView lookupView, LookupForm lookupForm, Object dataObject) {
        Properties props = getReturnUrlParameters(lookupView, lookupForm, dataObject);

        String href = "";
        if (StringUtils.isNotBlank(lookupForm.getReturnLocation())) {
            href = UrlFactory.parameterizeUrl(lookupForm.getReturnLocation(), props);
        }

        return href;
    }

    /**
     * Builds up a <code>Properties</code> object that will be used to provide the request parameters for the
     * return URL link
     *
     * @param lookupView - lookup view instance containing lookup configuration
     * @param lookupForm - lookup form instance containing the data
     * @param dataObject - data object instance for the current line and for which the return URL is being built
     * @return Properties instance containing request parameters for return URL
     */
    protected Properties getReturnUrlParameters(LookupView lookupView, LookupForm lookupForm, Object dataObject) {
        Properties props = new Properties();
        props.put(KRADConstants.DISPATCH_REQUEST_PARAMETER, KRADConstants.RETURN_METHOD_TO_CALL);

        if (StringUtils.isNotBlank(lookupForm.getReturnFormKey())) {
            props.put(UifParameters.FORM_KEY, lookupForm.getReturnFormKey());
        }

        props.put(KRADConstants.REFRESH_CALLER, lookupView.getId());
        props.put(KRADConstants.REFRESH_DATA_OBJECT_CLASS, getDataObjectClass().getName());

        if (StringUtils.isNotBlank(lookupForm.getDocNum())) {
            props.put(UifParameters.DOC_NUM, lookupForm.getDocNum());
        }

        if (StringUtils.isNotBlank(lookupForm.getReferencesToRefresh())) {
            props.put(KRADConstants.REFERENCES_TO_REFRESH, lookupForm.getReferencesToRefresh());
        }

        List<String> returnKeys = getReturnKeys(lookupView, lookupForm, dataObject);
        List<String> secureReturnKeys = lookupView.getAdditionalSecurePropertyNames();
        Map<String, String> returnKeyValues = KRADUtils.getPropertyKeyValuesFromDataObject(returnKeys, secureReturnKeys, dataObject);

        for (String returnKey : returnKeyValues.keySet()) {
            String returnValue = returnKeyValues.get(returnKey);
            if (lookupForm.getFieldConversions().containsKey(returnKey)) {
                returnKey = lookupForm.getFieldConversions().get(returnKey);
            }

            props.put(returnKey, returnValue);
        }
        // props.put(UifParameters.AJAX_REQUEST,"false");
        return props;
    }

    /**
     * <p>Returns the configured return key property names or if not configured defaults to the primary keys
     * for the data object class
     * </p>
     *
     * @param lookupView - lookup view instance containing lookup configuration
     * @param lookupForm - lookup form instance containing the data
     * @param dataObject - data object instance
     * @return List<String> property names which should be passed back on the return URL
     */
    protected List<String> getReturnKeys(LookupView lookupView, LookupForm lookupForm, Object dataObject) {
        List<String> returnKeys;
        if (lookupForm.getFieldConversions() != null && !lookupForm.getFieldConversions().isEmpty()) {
            returnKeys = new ArrayList<String>(lookupForm.getFieldConversions().keySet());
        } else {
            returnKeys = getDataObjectMetaDataService().listPrimaryKeyFieldNames(getDataObjectClass());
        }

        return returnKeys;
    }

    /**
     * @see org.kuali.rice.krad.lookup.Lookupable#getMaintenanceActionLink
     */
    public void getMaintenanceActionLink(Action actionLink, Object model, String maintenanceMethodToCall) {
        LookupForm lookupForm = (LookupForm) model;
        Map<String, Object> actionLinkContext = actionLink.getContext();
        Object dataObject = actionLinkContext == null ? null : actionLinkContext
                .get(UifConstants.ContextVariableNames.LINE);

        List<String> pkNames = getDataObjectMetaDataService().listPrimaryKeyFieldNames(getDataObjectClass());

        // build maintenance link href
        String href = getActionUrlHref(lookupForm, dataObject, maintenanceMethodToCall, pkNames);
        if (StringUtils.isBlank(href)) {
            actionLink.setRender(false);
            return;
        }
        // TODO: need to handle returning anchor
        actionLink.setActionScript("window.open('" + href + "', '_self');");

        // build action title
        String prependTitleText = actionLink.getActionLabel() + " " +
                getDataDictionaryService().getDataDictionary().getDataObjectEntry(getDataObjectClass().getName())
                        .getObjectLabel() + " " +
                getConfigurationService().getPropertyValueAsString(
                        KRADConstants.Lookup.TITLE_ACTION_URL_PREPENDTEXT_PROPERTY);

        Map<String, String> primaryKeyValues = KRADUtils.getPropertyKeyValuesFromDataObject(pkNames, dataObject);
        String title = LookupInquiryUtils.getLinkTitleText(prependTitleText, getDataObjectClass(), primaryKeyValues);
        actionLink.setTitle(title);
        lookupForm.setAtLeastOneRowHasActions(true);
    }

    /**
     * Generates a URL to perform a maintenance action on the given result data object
     *
     * <p>
     * Will build a URL containing keys of the data object to invoke the given maintenance action method
     * within the maintenance controller
     * </p>
     *
     * @param dataObject - data object instance for the line to build the maintenance action link for
     * @param methodToCall - method name on the maintenance controller that should be invoked
     * @param pkNames - list of primary key field names for the data object whose key/value pairs will be added to
     * the maintenance link
     * @return String URL link for the maintenance action
     */
    protected String getActionUrlHref(LookupForm lookupForm, Object dataObject, String methodToCall,
            List<String> pkNames) {
        LookupView lookupView = (LookupView) lookupForm.getView();

        Properties props = new Properties();
        props.put(KRADConstants.DISPATCH_REQUEST_PARAMETER, methodToCall);

        Map<String, String> primaryKeyValues = KRADUtils.getPropertyKeyValuesFromDataObject(pkNames, dataObject);
        for (String primaryKey : primaryKeyValues.keySet()) {
            String primaryKeyValue = primaryKeyValues.get(primaryKey);

            props.put(primaryKey, primaryKeyValue);
        }

        if (StringUtils.isNotBlank(lookupForm.getReturnLocation())) {
            props.put(KRADConstants.RETURN_LOCATION_PARAMETER, lookupForm.getReturnLocation());
        }

        props.put(UifParameters.DATA_OBJECT_CLASS_NAME, lookupForm.getDataObjectClassName());
        props.put(UifParameters.VIEW_TYPE_NAME, UifConstants.ViewType.MAINTENANCE.name());

        String maintenanceMapping = KRADConstants.Maintenance.REQUEST_MAPPING_MAINTENANCE;
        if (lookupView != null && StringUtils.isNotBlank(lookupView.getMaintenanceUrlMapping())) {
            maintenanceMapping = lookupView.getMaintenanceUrlMapping();
        }

        return UrlFactory.parameterizeUrl(maintenanceMapping, props);
    }

    /**
     * Sets the value for the attribute field control to contain the field conversion values for the line
     *
     * @see org.kuali.rice.krad.lookup.LookupableImpl#setMultiValueLookupSelect
     */
    @Override
    public void setMultiValueLookupSelect(InputField selectField, Object model) {
        LookupForm lookupForm = (LookupForm) model;
        Map<String, Object> selectFieldContext = selectField.getContext();
        Object lineDataObject = selectFieldContext == null ? null : selectFieldContext
                .get(UifConstants.ContextVariableNames.LINE);
        if (lineDataObject == null) {
            throw new RuntimeException("Unable to get data object for line from component: " + selectField.getId());
        }

        Control selectControl = ((InputField) selectField).getControl();
        if ((selectControl != null) && (selectControl instanceof ValueConfiguredControl)) {
            String lineIdentifier = "";

            // get value for each field conversion from line and add to lineIdentifier
            Map<String, String> fieldConversions = lookupForm.getFieldConversions();
            List<String> fromFieldNames = new ArrayList<String>(fieldConversions.keySet());
            Collections.sort(fromFieldNames);
            for (String fromFieldName : fromFieldNames) {
                Object fromFieldValue = ObjectPropertyUtils.getPropertyValue(lineDataObject, fromFieldName);
                if (fromFieldValue != null) {
                    lineIdentifier += fromFieldValue;
                }
                lineIdentifier += ":";
            }
            lineIdentifier = StringUtils.removeEnd(lineIdentifier, ":");

            ((ValueConfiguredControl) selectControl).setValue(lineIdentifier);
        }
    }

    /**
     * Determines if given data object has associated maintenance document that allows new or copy
     * maintenance
     * actions
     *
     * @return boolean true if the maintenance new or copy action is allowed for the data object instance, false
     *         otherwise
     */
    public boolean allowsMaintenanceNewOrCopyAction() {
        boolean allowsNewOrCopy = false;

        String maintDocTypeName = getMaintenanceDocumentTypeName();
        if (StringUtils.isNotBlank(maintDocTypeName)) {
            allowsNewOrCopy = getDataObjectAuthorizationService().canCreate(getDataObjectClass(),
                    GlobalVariables.getUserSession().getPerson(), maintDocTypeName);
        }

        return allowsNewOrCopy;
    }

    /**
     * Determines if given data object has associated maintenance document that allows edit maintenance
     * actions
     *
     * @return boolean true if the maintenance edit action is allowed for the data object instance, false otherwise
     */
    public boolean allowsMaintenanceEditAction(Object dataObject) {
        boolean allowsEdit = false;

        String maintDocTypeName = getMaintenanceDocumentTypeName();
        if (StringUtils.isNotBlank(maintDocTypeName)) {
            allowsEdit = getDataObjectAuthorizationService().canMaintain(dataObject,
                    GlobalVariables.getUserSession().getPerson(), maintDocTypeName);
        }

        return allowsEdit;
    }

    /**
     * Determines if given data object has associated maintenance document that allows delete maintenance
     * actions.
     *
     * @return boolean true if the maintenance delete action is allowed for the data object instance, false otherwise
     */
    public boolean allowsMaintenanceDeleteAction(Object dataObject) {
        boolean allowsMaintain = false;
        boolean allowsDelete = false;

        String maintDocTypeName = getMaintenanceDocumentTypeName();
        if (StringUtils.isNotBlank(maintDocTypeName)) {
            allowsMaintain = getDataObjectAuthorizationService().canMaintain(dataObject,
                    GlobalVariables.getUserSession().getPerson(), maintDocTypeName);
        }

        allowsDelete = getDocumentDictionaryService().getAllowsRecordDeletion(getDataObjectClass());

        return allowsDelete && allowsMaintain;
    }

    /**
     * Returns the maintenance document type associated with the business object class or null if one does not exist.
     *
     * @return String representing the maintenance document type name
     */
    protected String getMaintenanceDocumentTypeName() {
        DocumentDictionaryService dd = getDocumentDictionaryService();
        String maintDocTypeName = dd.getMaintenanceDocumentTypeName(getDataObjectClass());

        return maintDocTypeName;
    }

    /**
     * Determines whether a given data object that's returned as one of the lookup's results is considered returnable,
     * which means that for single-value lookups, a "return value" link may be rendered, and for multiple
     * value lookups, a checkbox is rendered.
     *
     * Note that this can be part of an authorization mechanism, but not the complete authorization mechanism.  The
     * component that invoked the lookup/ lookup caller (e.g. document, nesting lookup, etc.) needs to check
     * that the object that was passed to it was returnable as well because there are ways around this method
     * (e.g. crafting a custom return URL).
     *
     * @param dataObject - an object from the search result set
     * @return true if the row is returnable and false if it is not
     */
    protected boolean isResultReturnable(Object dataObject) {
        return true;
    }

    /**
     * @see org.kuali.rice.krad.lookup.Lookupable#setDataObjectClass
     */
    @Override
    public void setDataObjectClass(Class<?> dataObjectClass) {
        this.dataObjectClass = dataObjectClass;
    }

    /**
     * @see org.kuali.rice.krad.lookup.Lookupable#getDataObjectClass
     */
    @Override
    public Class<?> getDataObjectClass() {
        return this.dataObjectClass;
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    protected DataObjectAuthorizationService getDataObjectAuthorizationService() {
        if (dataObjectAuthorizationService == null) {
            this.dataObjectAuthorizationService = KRADServiceLocatorWeb.getDataObjectAuthorizationService();
        }
        return dataObjectAuthorizationService;
    }

    public void setDataObjectAuthorizationService(DataObjectAuthorizationService dataObjectAuthorizationService) {
        this.dataObjectAuthorizationService = dataObjectAuthorizationService;
    }

    protected DataObjectMetaDataService getDataObjectMetaDataService() {
        if (dataObjectMetaDataService == null) {
            this.dataObjectMetaDataService = KRADServiceLocatorWeb.getDataObjectMetaDataService();
        }
        return dataObjectMetaDataService;
    }

    public void setDataObjectMetaDataService(DataObjectMetaDataService dataObjectMetaDataService) {
        this.dataObjectMetaDataService = dataObjectMetaDataService;
    }

    public DocumentDictionaryService getDocumentDictionaryService() {
        if (documentDictionaryService == null) {
            documentDictionaryService = KRADServiceLocatorWeb.getDocumentDictionaryService();
        }
        return documentDictionaryService;
    }

    public void setDocumentDictionaryService(DocumentDictionaryService documentDictionaryService) {
        this.documentDictionaryService = documentDictionaryService;
    }

    protected LookupService getLookupService() {
        if (lookupService == null) {
            this.lookupService = KRADServiceLocatorWeb.getLookupService();
        }
        return lookupService;
    }

    public void setLookupService(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    protected EncryptionService getEncryptionService() {
        if (encryptionService == null) {
            this.encryptionService = CoreApiServiceLocator.getEncryptionService();
        }
        return encryptionService;
    }

    public void setEncryptionService(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }
}
