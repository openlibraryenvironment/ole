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
package org.kuali.rice.krad.web.form;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.lookup.LookupUtils;
import org.kuali.rice.krad.lookup.Lookupable;
import org.kuali.rice.krad.uif.UifConstants.ViewType;
import org.kuali.rice.krad.uif.view.LookupView;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Form class for <code>LookupView</code> screens
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class LookupForm extends UifFormBase {
    private static final long serialVersionUID = -7323484966538685327L;

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(LookupForm.class);

    private String dataObjectClassName;
    private String docNum;
    private String referencesToRefresh;

    private boolean multipleValuesSelect;
    private String lookupCollectionName;

    private Map<String, String> lookupCriteria;
    private Map<String, String> fieldConversions;

    private Collection<?> lookupResults;

    private boolean atLeastOneRowReturnable;
    private boolean atLeastOneRowHasActions;

    private boolean redirectedLookup;

    public LookupForm() {
        super();

        setViewTypeName(ViewType.LOOKUP);
        atLeastOneRowReturnable = false;
        atLeastOneRowHasActions = false;
        multipleValuesSelect = false;
        redirectedLookup = false;

        lookupCriteria = new HashMap<String, String>();
        fieldConversions = new HashMap<String, String>();
    }

    /**
     * Picks out business object name from the request to get retrieve a
     * lookupable and set properties on the initial request
     */
    @Override
    public void postBind(HttpServletRequest request) {
        super.postBind(request);

        try {
            Lookupable lookupable = getLookupable();
            if (lookupable == null) {
                // assume lookupable will be set by controller or a redirect will happen
                return;
            }

            if (StringUtils.isBlank(getDataObjectClassName())) {
                setDataObjectClassName(((LookupView) getView()).getDataObjectClassName().getName());
            }
            Class<?> dataObjectClass = Class.forName(getDataObjectClassName());
            lookupable.setDataObjectClass(dataObjectClass);

            if (request.getMethod().equals("GET")) {
                // populate field conversions list
                if (request.getParameter(KRADConstants.CONVERSION_FIELDS_PARAMETER) != null) {
                    String conversionFields = request.getParameter(KRADConstants.CONVERSION_FIELDS_PARAMETER);
                    setFieldConversions(KRADUtils.convertStringParameterToMap(conversionFields));
                }

                // perform upper casing of lookup parameters
                Map<String, String> fieldValues = new HashMap<String, String>();
                if (getLookupCriteria() != null) {
                    for (Map.Entry<String, String> entry : getLookupCriteria().entrySet()) {
                        // check here to see if this field is a criteria element on the form
                        fieldValues.put(entry.getKey(), LookupUtils.forceUppercase(dataObjectClass, entry.getKey(),
                                entry.getValue()));
                    }
                }

                if (StringUtils.isNotBlank(getDocNum())) {
                    fieldValues.put(KRADConstants.DOC_NUM, getDocNum());
                }

                this.setLookupCriteria(fieldValues);
            }
        } catch (ClassNotFoundException e) {
            LOG.error("Object class " + getDataObjectClassName() + " not found");
            throw new RuntimeException("Object class " + getDataObjectClassName() + " not found", e);
        }
    }

    public Lookupable getLookupable() {
        if ((getView() != null) && (getView().getViewHelperService() != null) && Lookupable.class.isAssignableFrom(
                getView().getViewHelperService().getClass())) {
            return (Lookupable) getView().getViewHelperService();
        } else if ((getPostedView() != null) && (getPostedView().getViewHelperService() != null) && Lookupable.class
                .isAssignableFrom(getPostedView().getViewHelperService().getClass())) {
            return (Lookupable) getPostedView().getViewHelperService();
        }

        return null;
    }

    public String getDataObjectClassName() {
        return this.dataObjectClassName;
    }

    public void setDataObjectClassName(String dataObjectClassName) {
        this.dataObjectClassName = dataObjectClassName;
    }

    public String getDocNum() {
        return this.docNum;
    }

    public void setDocNum(String docNum) {
        this.docNum = docNum;
    }

    public String getReferencesToRefresh() {
        return referencesToRefresh;
    }

    public void setReferencesToRefresh(String referencesToRefresh) {
        this.referencesToRefresh = referencesToRefresh;
    }

    /**
     * Indicates whether multiple values select should be enabled for the lookup
     *
     * <p>
     * When set to true, the select field is enabled for the lookup results group that allows the user
     * to select one or more rows for returning
     * </p>
     *
     * @return boolean true if multiple values should be enabled, false otherwise
     */
    public boolean isMultipleValuesSelect() {
        return multipleValuesSelect;
    }

    /**
     * Setter for the multiple values select indicator
     *
     * @param multipleValuesSelect
     */
    public void setMultipleValuesSelect(boolean multipleValuesSelect) {
        this.multipleValuesSelect = multipleValuesSelect;
    }

    /**
     * For the case of multi-value lookup, indicates the collection that should be populated with
     * the return results
     *
     * @return String collection name (must be full binding path)
     */
    public String getLookupCollectionName() {
        return lookupCollectionName;
    }

    /**
     * Setter for the name of the collection that should be populated with lookup results
     *
     * @param lookupCollectionName
     */
    public void setLookupCollectionName(String lookupCollectionName) {
        this.lookupCollectionName = lookupCollectionName;
    }

    public Map<String, String> getLookupCriteria() {
        return this.lookupCriteria;
    }

    public void setLookupCriteria(Map<String, String> lookupCriteria) {
        this.lookupCriteria = lookupCriteria;
    }

    public Map<String, String> getFieldConversions() {
        return this.fieldConversions;
    }

    public void setFieldConversions(Map<String, String> fieldConversions) {
        this.fieldConversions = fieldConversions;
    }

    public Collection<?> getLookupResults() {
        return this.lookupResults;
    }

    public void setLookupResults(Collection<?> lookupResults) {
        this.lookupResults = lookupResults;
    }

    public boolean isAtLeastOneRowReturnable() {
        return atLeastOneRowReturnable;
    }

    public void setAtLeastOneRowReturnable(boolean atLeastOneRowReturnable) {
        this.atLeastOneRowReturnable = atLeastOneRowReturnable;
    }

    public boolean isAtLeastOneRowHasActions() {
        return atLeastOneRowHasActions;
    }

    public void setAtLeastOneRowHasActions(boolean atLeastOneRowHasActions) {
        this.atLeastOneRowHasActions = atLeastOneRowHasActions;
    }

    /**
     * Indicates whether the requested was redirected from the lookup framework due to an external object
     * request. This prevents the framework from performing another redirect check
     *
     * @return boolean true if request was a redirect, false if not
     */
    public boolean isRedirectedLookup() {
        return redirectedLookup;
    }

    /**
     * Setter for the redirected request indicator
     *
     * @param redirectedLookup
     */
    public void setRedirectedLookup(boolean redirectedLookup) {
        this.redirectedLookup = redirectedLookup;
    }
}
