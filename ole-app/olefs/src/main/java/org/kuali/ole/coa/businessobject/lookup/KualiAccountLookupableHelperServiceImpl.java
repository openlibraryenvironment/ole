/*
 * Copyright 2006 The Kuali Foundation
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
package org.kuali.ole.coa.businessobject.lookup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kuali.ole.coa.businessobject.Account;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.IdentityManagementService;
import org.kuali.rice.kns.lookup.HtmlData;
import org.kuali.rice.kns.lookup.HtmlData.AnchorHtmlData;
import org.kuali.rice.kns.lookup.KualiLookupableHelperServiceImpl;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;

/**
 * This class overrids the base getActionUrls method
 */
public class KualiAccountLookupableHelperServiceImpl extends KualiLookupableHelperServiceImpl {

    ThreadLocal<Person> currentUser = new ThreadLocal<Person>();

    /**
     * If the account is not closed or the user is an Administrator the "edit" link is added The "copy" link is added for Accounts
     *
     * @returns links to edit and copy maintenance action for the current maintenance record.
     * @see org.kuali.rice.kns.lookup.AbstractLookupableHelperServiceImpl#getCustomActionUrls(org.kuali.rice.kns.bo.BusinessObject,
     *      java.util.List)
     */
    @Override
    public List<HtmlData> getCustomActionUrls(BusinessObject businessObject, List pkNames) {
        StringBuffer actions = new StringBuffer();
        Account theAccount = (Account) businessObject;
        List<HtmlData> anchorHtmlDataList = new ArrayList<HtmlData>();
        Person user = GlobalVariables.getUserSession().getPerson();
        AnchorHtmlData urlDataCopy = getUrlData(businessObject, KRADConstants.MAINTENANCE_COPY_METHOD_TO_CALL, pkNames);

        if (theAccount.isActive()) {
            anchorHtmlDataList.add(getUrlData(businessObject, KRADConstants.MAINTENANCE_EDIT_METHOD_TO_CALL, pkNames));
        }
        else {
            String principalId = user.getPrincipalId();
            String namespaceCode = OLEConstants.PermissionNames.EDIT_INACTIVE_ACCOUNT.namespace;
            String permissionName = OLEConstants.PermissionNames.EDIT_INACTIVE_ACCOUNT.name;

            IdentityManagementService identityManagementService = SpringContext.getBean(IdentityManagementService.class);
            boolean isAuthorized = identityManagementService.hasPermission(principalId, namespaceCode, permissionName);

            if (isAuthorized) {
                anchorHtmlDataList.add(getUrlData(businessObject, KRADConstants.MAINTENANCE_EDIT_METHOD_TO_CALL, pkNames));
            }
            else {
                urlDataCopy.setPrependDisplayText("&nbsp;&nbsp;&nbsp;&nbsp;");
            }
        }
        anchorHtmlDataList.add(urlDataCopy);

        return anchorHtmlDataList;
    }

    /**
     * Overridden to changed the "closed" parameter to an "active" parameter
     *
     * Checks for the T-Restricted records (Temporary Restricted) and removes from the list of search Results
     *
     * @see org.kuali.rice.kns.lookup.KualiLookupableHelperServiceImpl#getSearchResults(java.util.Map)
     */
    @Override
    public List<? extends BusinessObject> getSearchResults(Map<String, String> parameters) {
        Map<String, String> activeMap = new HashMap<String, String>();
        List<Account> accountDetails = new ArrayList<Account>();

        boolean flag = false;
        if (parameters.containsKey(OLEPropertyConstants.CLOSED)) {
            final String closedValue = parameters.get(OLEPropertyConstants.CLOSED);

            if (closedValue != null && closedValue.length() != 0) {
                if ("Y1T".indexOf(closedValue) > -1) {
                    parameters.put(OLEPropertyConstants.ACTIVE, "N");
                }
                else if ("N0F".indexOf(closedValue) > -1) {
                    parameters.put(OLEPropertyConstants.ACTIVE, "Y");
               }
            }

            parameters.remove(OLEPropertyConstants.CLOSED);
        }

        // Modified for JIRA OLE-2519 starts
        final String restrictedValue = parameters.get(OleSelectConstant.RESTRICTED);
        boolean containsChartCode = false;
        if(!"".equals(parameters.get(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE))){
            containsChartCode=true;
        }
        if(parameters.containsKey(OleSelectConstant.RESTRICTED)){

            if(restrictedValue.equalsIgnoreCase("Y")){
                parameters.put(OLEPropertyConstants.ACCOUNT_RESTRICTED_STATUS_CODE, "T|R");
            }
            else if(restrictedValue.equalsIgnoreCase("N")){
                parameters.put(OLEPropertyConstants.ACCOUNT_RESTRICTED_STATUS_CODE, "U|N");
            }
        }
        parameters.remove(OleSelectConstant.RESTRICTED);

        String subFundGroupCodeValue = parameters.get(OleSelectConstant.SUBFUND_GROUP_CODE);
        String vendorNameValue = parameters.get(OleSelectConstant.VENDOR_NAME);
        String subFundGroupParameter = getParameterService()
                .getParameterValueAsString(Account.class, OleSelectConstant.SUB_FUND_GRP_CD);
        if (subFundGroupCodeValue.equalsIgnoreCase(subFundGroupParameter) && vendorNameValue.isEmpty()) {

            parameters.remove(OleSelectConstant.VENDOR_DEPOSIT_ACCOUNT);
            GlobalVariables.getMessageMap().putError(OleSelectConstant.VENDOR_NAME, OLEKeyConstants.ERROR_VENDOR_DEPOSIT_ACCOUNT);
            flag = true;

        }
        else if ((!subFundGroupCodeValue.equalsIgnoreCase(subFundGroupParameter) || (subFundGroupCodeValue
                .isEmpty())) && !vendorNameValue.isEmpty()) {

            parameters.remove(OleSelectConstant.VENDOR_DEPOSIT_ACCOUNT);
            GlobalVariables.getMessageMap().putError(OleSelectConstant.VENDOR_NAME, OLEKeyConstants.ERROR_SUBFUND_GROUP_CODE);
            flag = true;
        }
        if(!flag) {
            final String vendorDepositAccount = parameters.get(OleSelectConstant.VENDOR_DEPOSIT_ACCOUNT);
            if(vendorDepositAccount.equalsIgnoreCase("true")) {    //exclude vendor deposit account


                parameters.remove(OleSelectConstant.VENDOR_DEPOSIT_ACCOUNT);

                parameters.put(OleSelectConstant.VENDOR_NAME, "");
                accountDetails = (List<Account>) super.getSearchResults(parameters);
                for (int vendorKey = 0; vendorKey < accountDetails.size(); vendorKey++) {
                    accountDetails.get(vendorKey).setVendorHeaderGeneratedIdentifier(null);
                    accountDetails.get(vendorKey).setVendorDetailAssignedIdentifier(null);
                }

            }
            else {  //include vendor deposit account

                parameters.remove(OleSelectConstant.VENDOR_DEPOSIT_ACCOUNT);
                parameters.remove(OleSelectConstant.VENDOR_NAME);

                accountDetails = (List<Account>) super.getSearchResults(parameters);
        if(containsChartCode){
            if(restrictedValue.equalsIgnoreCase("N")) {
                for (int i=0; i< accountDetails.size(); i++) {
                    if (accountDetails.get(i).getAccountRestrictedStatusCode().equalsIgnoreCase(OleSelectConstant.ACCOUNT_TEMPORARY_RESTRICTED_CODE) || accountDetails.get(i).getAccountRestrictedStatusCode().equalsIgnoreCase(OleSelectConstant.ACCOUNT_RESTRICTED_CODE)) {
                        accountDetails.remove(i);
                    }
                }
            }
        }
            }
        }
        return accountDetails;
    }

}
