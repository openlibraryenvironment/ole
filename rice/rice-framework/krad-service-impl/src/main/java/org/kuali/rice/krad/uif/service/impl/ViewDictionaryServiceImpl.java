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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.krad.datadictionary.DataDictionary;
import org.kuali.rice.krad.inquiry.Inquirable;
import org.kuali.rice.krad.service.DataDictionaryService;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.UifPropertyPaths;
import org.kuali.rice.krad.uif.util.ViewModelUtils;
import org.kuali.rice.krad.uif.view.LookupView;
import org.kuali.rice.krad.uif.service.ViewDictionaryService;
import org.kuali.rice.krad.uif.UifConstants.ViewType;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.view.ViewSessionPolicy;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.krad.web.form.LookupForm;
import org.springframework.beans.PropertyValues;
import org.springframework.util.Assert;

/**
 * Implementation of <code>ViewDictionaryService</code>
 *
 * <p>
 * Pulls view entries from the data dictionary to implement the various query
 * methods
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ViewDictionaryServiceImpl implements ViewDictionaryService {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(
            ViewDictionaryServiceImpl.class);

    private DataDictionaryService dataDictionaryService;

    /**
     * @see org.kuali.rice.krad.uif.service.ViewDictionaryService#getInquirable(java.lang.Class,
     *      java.lang.String)
     */
    public Inquirable getInquirable(Class<?> dataObjectClass, String viewName) {
        Inquirable inquirable = null;

        if (StringUtils.isBlank(viewName)) {
            viewName = UifConstants.DEFAULT_VIEW_NAME;
        }

        Map<String, String> indexKey = new HashMap<String, String>();
        indexKey.put(UifParameters.VIEW_NAME, viewName);
        indexKey.put(UifParameters.DATA_OBJECT_CLASS_NAME, dataObjectClass.getName());

        // get view properties
        PropertyValues propertyValues = getDataDictionary().getViewPropertiesByType(ViewType.INQUIRY, indexKey);

        String viewHelperServiceClassName = ViewModelUtils.getStringValFromPVs(propertyValues,
                UifPropertyPaths.VIEW_HELPER_SERVICE_CLASS);
        if (StringUtils.isNotBlank(viewHelperServiceClassName)) {
            try {
                inquirable = (Inquirable) ObjectUtils.newInstance(Class.forName(viewHelperServiceClassName));
            } catch (ClassNotFoundException e) {
                throw new RiceRuntimeException(
                        "Unable to find class for inquirable classname: " + viewHelperServiceClassName, e);
            }
        }

        return inquirable;
    }

    /**
     * @see org.kuali.rice.krad.uif.service.ViewDictionaryService#isInquirable(java.lang.Class)
     */
    public boolean isInquirable(Class<?> dataObjectClass) {
        Map<String, String> indexKey = new HashMap<String, String>();
        indexKey.put(UifParameters.VIEW_NAME, UifConstants.DEFAULT_VIEW_NAME);
        indexKey.put(UifParameters.DATA_OBJECT_CLASS_NAME, dataObjectClass.getName());

        boolean isInquirable = getDataDictionary().viewByTypeExist(ViewType.INQUIRY, indexKey);

        return isInquirable;
    }

    /**
     * @see org.kuali.rice.krad.uif.service.ViewDictionaryService#isLookupable(java.lang.Class)
     */
    public boolean isLookupable(Class<?> dataObjectClass) {
        Map<String, String> indexKey = new HashMap<String, String>();
        indexKey.put(UifParameters.VIEW_NAME, UifConstants.DEFAULT_VIEW_NAME);
        indexKey.put(UifParameters.DATA_OBJECT_CLASS_NAME, dataObjectClass.getName());

        boolean isLookupable = getDataDictionary().viewByTypeExist(ViewType.LOOKUP, indexKey);

        return isLookupable;
    }

    /**
     * @see org.kuali.rice.krad.uif.service.ViewDictionaryService#isMaintainable(java.lang.Class)
     */
    public boolean isMaintainable(Class<?> dataObjectClass) {
        Map<String, String> indexKey = new HashMap<String, String>();
        indexKey.put(UifParameters.VIEW_NAME, UifConstants.DEFAULT_VIEW_NAME);
        indexKey.put(UifParameters.DATA_OBJECT_CLASS_NAME, dataObjectClass.getName());

        boolean isMaintainable = getDataDictionary().viewByTypeExist(ViewType.MAINTENANCE, indexKey);

        return isMaintainable;
    }

    /**
     * @see org.kuali.rice.krad.uif.service.ViewDictionaryService#getResultSetLimitForLookup(java.lang.Class,
     *      org.kuali.rice.krad.web.form.LookupForm)
     *
     *      If the form is null, only the dataObjectClass will be used to find the LookupView and corresponding
     *      results set limit.  Since the viewID is not known, the default view will be used.
     */
    @Override
    public Integer getResultSetLimitForLookup(Class<?> dataObjectClass, LookupForm lookupForm) {
        LookupView lookupView = null;
        boolean multipleValueSelectSpecifiedOnURL = false;

        if (ObjectUtils.isNotNull(lookupForm)) {
            if (lookupForm.isMultipleValuesSelect()) {
                multipleValueSelectSpecifiedOnURL = true;
            }

            if (!multipleValueSelectSpecifiedOnURL && lookupForm.getViewRequestParameters().containsKey(
                    UifParameters.MULTIPLE_VALUES_SELECT)) {
                String multiValueSelect = lookupForm.getViewRequestParameters().get(
                        UifParameters.MULTIPLE_VALUES_SELECT);
                if (multiValueSelect.equalsIgnoreCase("true")) {
                    multipleValueSelectSpecifiedOnURL = true;
                }
            }
            lookupView = (LookupView) lookupForm.getView();
        } else {
            Map<String, String> indexKey = new HashMap<String, String>();
            indexKey.put(UifParameters.VIEW_NAME, UifConstants.DEFAULT_VIEW_NAME);
            indexKey.put(UifParameters.DATA_OBJECT_CLASS_NAME, dataObjectClass.getName());
            lookupView = (LookupView) getDataDictionary().getViewByTypeIndex(ViewType.LOOKUP, indexKey);
        }

        if (lookupView != null) {
            if (lookupView.isMultipleValuesSelect() || multipleValueSelectSpecifiedOnURL) {
                return lookupView.getMultipleValuesSelectResultSetLimit();
            } else {
                return lookupView.getResultSetLimit();
            }
        }
        return null;
    }

    /**
     * @see org.kuali.rice.krad.uif.service.ViewDictionaryService#getViewSessionPolicy(java.lang.String)
     */
    public ViewSessionPolicy getViewSessionPolicy(String viewId) {
        Assert.hasLength(viewId, "view id is required for retrieving the view session policy");

        ViewSessionPolicy viewSessionPolicy = null;

        View view = getDataDictionary().getImmutableViewById(viewId);
        if (view != null) {
            viewSessionPolicy = view.getSessionPolicy();
        }

        return viewSessionPolicy;
    }

    /**
     * @see org.kuali.rice.krad.uif.service.ViewDictionaryService#isSessionStorageEnabled(java.lang.String)
     */
    public boolean isSessionStorageEnabled(String viewId) {
        Assert.hasLength(viewId, "view id is required for retrieving session indicator");

        boolean sessionStorageEnabled = false;

        View view = getDataDictionary().getImmutableViewById(viewId);
        if (view != null) {
            sessionStorageEnabled = view.isPersistFormToSession();
        }

        return sessionStorageEnabled;
    }

    protected DataDictionary getDataDictionary() {
        return getDataDictionaryService().getDataDictionary();
    }

    protected DataDictionaryService getDataDictionaryService() {
        return this.dataDictionaryService;
    }

    public void setDataDictionaryService(DataDictionaryService dataDictionaryService) {
        this.dataDictionaryService = dataDictionaryService;
    }
}
