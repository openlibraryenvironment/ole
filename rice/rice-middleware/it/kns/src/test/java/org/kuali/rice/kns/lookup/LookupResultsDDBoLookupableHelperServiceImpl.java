/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.kns.lookup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.kuali.rice.kns.document.authorization.BusinessObjectRestrictions;
import org.kuali.rice.kns.lookup.HtmlData;
import org.kuali.rice.kns.lookup.LookupableHelperService;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.kns.web.struts.form.LookupForm;
import org.kuali.rice.kns.web.ui.Column;
import org.kuali.rice.kns.web.ui.Field;
import org.kuali.rice.kns.web.ui.Row;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.kns.service.BusinessObjectDictionaryService;
import org.kuali.rice.krad.service.DataDictionaryService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;

/**
 * Mock lookupable helper service for the LookupResultsService test
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class LookupResultsDDBoLookupableHelperServiceImpl implements LookupableHelperService {

	/**
	 * Just sends back whatever someValue was sent in - or "A" as some value if nothing else was out there
	 * @see org.kuali.rice.krad.lookup.AbstractLookupableHelperServiceImpl#getSearchResults(java.util.Map)
	 */
	public List<? extends BusinessObject> getSearchResults(Map<String, String> fieldValues) {
		final String valueToPopulate = (fieldValues.containsKey("someValue")) ? fieldValues.get("someValue") : "A";
		final LookupResultsDDBo result = new LookupResultsDDBo(valueToPopulate);
		List<LookupResultsDDBo> results = new ArrayList<LookupResultsDDBo>();
		results.add(result);
		return results;
	}

	/**
	 * Always return false
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#allowsMaintenanceNewOrCopyAction()
	 */
	public boolean allowsMaintenanceNewOrCopyAction() {
		return false;
	}

	/**
	 * Always return false
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#allowsNewOrCopyAction(java.lang.String)
	 */
	public boolean allowsNewOrCopyAction(String documentTypeName) {
		return false;
	}

	/**
	 * Don't do anything
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#applyFieldAuthorizationsFromNestedLookups(org.kuali.rice.krad.web.ui.Field)
	 */
	public void applyFieldAuthorizationsFromNestedLookups(Field field) {}

	/**
	 * Always returns false
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#checkForAdditionalFields(java.util.Map)
	 */
	public boolean checkForAdditionalFields(Map<String, String> fieldValues) {
		return false;
	}

	/**
	 * Always returns a blank String
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#getActionUrls(org.kuali.rice.krad.bo.BusinessObject, java.util.List, org.kuali.rice.krad.authorization.BusinessObjectRestrictions)
	 */
	public String getActionUrls(BusinessObject businessObject, List pkNames, BusinessObjectRestrictions businessObjectRestrictions) {
		return "";
	}

	/**
	 * Always returns blank String
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#getBackLocation()
	 */
	public String getBackLocation() {
		return "";
	}

	/**
	 * Always returns the class of LookupResultsDDBo
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#getBusinessObjectClass()
	 */
	public Class getBusinessObjectClass() {
		return LookupResultsDDBo.class;
	}

	/**
	 * Gets the class from the KRADServiceLocatorInternal
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#getBusinessObjectDictionaryService()
	 */
	public BusinessObjectDictionaryService getBusinessObjectDictionaryService() {
		return KNSServiceLocator.getBusinessObjectDictionaryService();
	}

	/**
	 * Always returns null
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#getColumns()
	 */
	public List<Column> getColumns() {
		return null;
	}

	/**
	 * Always returns null
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#getCustomActionUrls(org.kuali.rice.krad.bo.BusinessObject, java.util.List)
	 */
	public List<HtmlData> getCustomActionUrls(BusinessObject businessObject, List pkNames) {
		return null;
	}

	/**
	 * Returns DataDictionaryService from KRADServiceLocatorInternal
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#getDataDictionaryService()
	 */
	public DataDictionaryService getDataDictionaryService() {
		return KRADServiceLocatorWeb.getDataDictionaryService();
	}

	/**
	 * Always returns null
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#getDefaultSortColumns()
	 */
	public List getDefaultSortColumns() {
		return null;
	}

	/**
	 * Always returns an empty String
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#getDocFormKey()
	 */
	public String getDocFormKey() {
		return "";
	}

	/**
	 * Always returns empty String
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#getDocNum()
	 */
	public String getDocNum() {
		return "";
	}

	/**
	 * Always returns null
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#getExtraField()
	 */
	public Field getExtraField() {
		return null;
	}

	/**
	 * Always returns null
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#getInquiryUrl(org.kuali.rice.krad.bo.BusinessObject, java.lang.String)
	 */
	public HtmlData getInquiryUrl(BusinessObject businessObject, String propertyName) {
		return null;
	}

	/**
	 * Always returns null
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#getMaintenanceUrl(org.kuali.rice.krad.bo.BusinessObject, org.kuali.rice.krad.lookup.HtmlData, java.util.List, org.kuali.rice.krad.authorization.BusinessObjectRestrictions)
	 */
	public String getMaintenanceUrl(BusinessObject businessObject, HtmlData htmlData, List pkNames, BusinessObjectRestrictions businessObjectRestrictions) {
		return null;
	}

	/**
	 * Always returns null
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#getParameters()
	 */
	public Map getParameters() {
		return null;
	}

	/**
	 * Returns an incredibly sophisticated puzzle that would require the smartest genius on earth years to disentangle.  It only appears to return null
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#getPrimaryKeyFieldLabels()
	 */
	public String getPrimaryKeyFieldLabels() {
		return null;
	}

	/**
	 * Isn't this class exciting?
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#getReadOnlyFieldsList()
	 */
	public List<String> getReadOnlyFieldsList() {
		return null;
	}

	/**
	 * It does ever so much work
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#getReturnKeys()
	 */
	public List<String> getReturnKeys() {
		return null;
	}

	/**
	 * Returns null for everything
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#getReturnLocation()
	 */
	public String getReturnLocation() {
		return null;
	}

	/**
	 * Yeah, this too
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#getReturnUrl(org.kuali.rice.krad.bo.BusinessObject, org.kuali.rice.krad.web.struts.form.LookupForm, java.util.List, org.kuali.rice.krad.authorization.BusinessObjectRestrictions)
	 */
	public HtmlData getReturnUrl(BusinessObject businessObject, LookupForm lookupForm, List returnKeys, BusinessObjectRestrictions businessObjectRestrictions) {
		return null;
	}

	/**
	 * Why am I doing all of this?
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#getReturnUrl(org.kuali.rice.krad.bo.BusinessObject, java.util.Map, java.lang.String, java.util.List, org.kuali.rice.krad.authorization.BusinessObjectRestrictions)
	 */
	public HtmlData getReturnUrl(BusinessObject businessObject, Map fieldConversions, String lookupImpl, List returnKeys, BusinessObjectRestrictions businessObjectRestrictions) {
		return null;
	}

	/**
	 * Why not just extend AbstractLookupableHelperServiceImpl?
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#getRows()
	 */
	public List<Row> getRows() {
		return null;
	}

	/**
	 * Oh, trust me...
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#getSearchResultsUnbounded(java.util.Map)
	 */
	public List getSearchResultsUnbounded(Map<String, String> fieldValues) {
		return null;
	}

	/**
	 * There's a story there
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#getSupplementalMenuBar()
	 */
	public String getSupplementalMenuBar() {
		return null;
	}

	/**
	 * At any rate, my unit test works
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#getTitle()
	 */
	public String getTitle() {
		return null;
	}

	/**
	 * And I just have a lot of dead methods
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#isResultReturnable(org.kuali.rice.krad.bo.BusinessObject)
	 */
	public boolean isResultReturnable(BusinessObject object) {
		return false;
	}

	/**
	 * I'm not injecting dependencies
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#isSearchUsingOnlyPrimaryKeyValues()
	 */
	public boolean isSearchUsingOnlyPrimaryKeyValues() {
		return false;
	}

	/**
	 * This method found it hard pressed to do anything...
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#performClear(org.kuali.rice.krad.web.struts.form.LookupForm)
	 */
	public void performClear(LookupForm lookupForm) {}

	/**
	 * Always returns false
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#performCustomAction(boolean)
	 */
	public boolean performCustomAction(boolean ignoreErrors) {
		return false;
	}

	/**
	 * Always returns null
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#performLookup(org.kuali.rice.krad.web.struts.form.LookupForm, java.util.Collection, boolean)
	 */
	public Collection performLookup(LookupForm lookupForm, Collection resultTable, boolean bounded) {
		return null;
	}

	/**
	 * Ignores the passed in value
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#setBackLocation(java.lang.String)
	 */
	public void setBackLocation(String backLocation) {}

	/**
	 * Throws the passed in value away
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#setBusinessObjectClass(java.lang.Class)
	 */
	public void setBusinessObjectClass(Class businessObjectClass) {}

	/**
	 * Did you actually want this mock service to save this information?  I think not...
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#setDocFormKey(java.lang.String)
	 */
	public void setDocFormKey(String docFormKey) {}

	/**
	 * Does nothing
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#setDocNum(java.lang.String)
	 */
	public void setDocNum(String docNum) {}

	/**
	 * Doesn't do a thing
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#setFieldConversions(java.util.Map)
	 */
	public void setFieldConversions(Map fieldConversions) {}

	/**
	 * Doesn't set anything
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#setParameters(java.util.Map)
	 */
	public void setParameters(Map parameters) {}

	/**
	 * doesn't set anything
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#setReadOnlyFieldsList(java.util.List)
	 */
	public void setReadOnlyFieldsList(List<String> readOnlyFieldsList) {}

	/**
	 * Always returns true, so that James isn't completely bored
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#shouldDisplayHeaderNonMaintActions()
	 */
	public boolean shouldDisplayHeaderNonMaintActions() {
		return true;
	}

	/**
	 * Flips a coin to determine whether to return true or false
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#shouldDisplayLookupCriteria()
	 */
	public boolean shouldDisplayLookupCriteria() {
		java.util.Random r = new java.util.Random();
		double value = r.nextDouble();
		return (value < 0.5);
	}

	/**
	 * Everything's valid, trust us
	 *
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#validateSearchParameters(java.util.Map)
	 */
	public void validateSearchParameters(Map<String, String> fieldValues) {}

	/**
	 * @see org.kuali.rice.krad.lookup.LookupableHelperService#applyConditionalLogicForFieldDisplay()
	 */
	public void applyConditionalLogicForFieldDisplay() {

	}

}
