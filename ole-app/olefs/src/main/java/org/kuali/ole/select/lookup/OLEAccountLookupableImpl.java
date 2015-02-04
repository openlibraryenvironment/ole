package org.kuali.ole.select.lookup;

import org.kuali.ole.coa.businessobject.Account;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.vnd.businessobject.VendorDetail;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.lookup.LookupableImpl;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.form.LookupForm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: anithaa
 * Date: 7/11/13
 * Time: 3:16 PM
 * To change this template use File | Settings | File Templates.
 */

public class OLEAccountLookupableImpl extends LookupableImpl {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OLEAccountLookupableImpl.class);
    protected static ParameterService parameterService;

    public ParameterService getParameterService() {
        if (parameterService == null) {
            parameterService = SpringContext.getBean(ParameterService.class);
        }
        return parameterService;
    }

    public List<? extends BusinessObject> getSearchResults(LookupForm form, Map<String, String> parameters, boolean unbounded) {
        Map<String, String> activeMap = new HashMap<String, String>();
        List<Account> accountDetails = new ArrayList<Account>();

        boolean flag = false;
        if (parameters.containsKey(OLEPropertyConstants.CLOSED)) {
            final String closedValue = parameters.get(OLEPropertyConstants.CLOSED);
            if (closedValue != null && closedValue.length() != 0) {
                if ("Y1T".indexOf(closedValue) > -1) {
                    parameters.put(OLEPropertyConstants.ACTIVE, "N");
                } else if ("N0F".indexOf(closedValue) > -1) {
                    parameters.put(OLEPropertyConstants.ACTIVE, "Y");
                }
            }

            parameters.remove(OLEPropertyConstants.CLOSED);
        }

        // Modified for JIRA OLE-2519 starts

        final String restrictedValue = parameters.get(OleSelectConstant.RESTRICTED);
        boolean containsChartCode = false;
        if (!"".equals(parameters.get(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE))) {
            containsChartCode = true;
        }
        if (parameters.containsKey(OleSelectConstant.RESTRICTED)) {

            if (restrictedValue.equalsIgnoreCase("Y")) {
                parameters.put(OLEPropertyConstants.ACCOUNT_RESTRICTED_STATUS_CODE, "T|R");
            } else if (restrictedValue.equalsIgnoreCase("N")) {
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

        } else if ((!subFundGroupCodeValue.equalsIgnoreCase(subFundGroupParameter) || (subFundGroupCodeValue
                .isEmpty())) && !vendorNameValue.isEmpty()) {

            parameters.remove(OleSelectConstant.VENDOR_DEPOSIT_ACCOUNT);
            GlobalVariables.getMessageMap().putError(OleSelectConstant.VENDOR_NAME, OLEKeyConstants.ERROR_SUBFUND_GROUP_CODE);
            flag = true;
        }
        if (!flag) {
            final String vendorDepositAccount = parameters.get(OleSelectConstant.VENDOR_DEPOSIT_ACCOUNT);
            if (vendorDepositAccount.equalsIgnoreCase("true")) {    //exclude vendor deposit account

                Map vendorMap = new HashMap();
                if (parameters.get("vendorName") != null) {
                    vendorMap.put("vendorName", parameters.get("vendorName"));
                }
                List<VendorDetail> vendor = (List<VendorDetail>) SpringContext.getBean(BusinessObjectService.class).findMatching(VendorDetail.class, vendorMap);
                if (vendor.size() > 0) {
                    Integer vendorHeaderGeneratedIdentifier = vendor.get(0).getVendorHeaderGeneratedIdentifier();
                    parameters.put("vendorHeaderGeneratedIdentifier", vendorHeaderGeneratedIdentifier.toString());
                }

                parameters.remove(OleSelectConstant.VENDOR_DEPOSIT_ACCOUNT);

                parameters.put(OleSelectConstant.VENDOR_NAME, "");
                accountDetails = (List<Account>) super.getSearchResults(form, parameters, unbounded);
                for (int vendorKey = 0; vendorKey < accountDetails.size(); vendorKey++) {
                    accountDetails.get(vendorKey).setVendorHeaderGeneratedIdentifier(null);
                    accountDetails.get(vendorKey).setVendorDetailAssignedIdentifier(null);
                }

            } else {  //include vendor deposit account

                parameters.remove(OleSelectConstant.VENDOR_DEPOSIT_ACCOUNT);
                parameters.remove(OleSelectConstant.VENDOR_NAME);
                accountDetails = (List<Account>) super.getSearchResults(form, parameters, unbounded);
                if (containsChartCode) {
                    if (restrictedValue.equalsIgnoreCase("N")) {
                        for (int i = 0; i < accountDetails.size(); i++) {
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


