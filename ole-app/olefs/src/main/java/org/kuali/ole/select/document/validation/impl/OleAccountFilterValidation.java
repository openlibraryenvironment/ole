/*
 * Copyright 2012 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.select.document.validation.impl;

import org.kuali.ole.coa.businessobject.Account;
import org.kuali.ole.coa.businessobject.Chart;
import org.kuali.ole.coa.businessobject.ObjectCode;
import org.kuali.ole.coa.service.AccountService;
import org.kuali.ole.coa.service.ChartService;
import org.kuali.ole.coa.service.ObjectCodeService;
import org.kuali.ole.select.document.validation.event.OleAccountFilterEvent;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.document.validation.GenericValidation;
import org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.HashMap;
import java.util.Map;

public class OleAccountFilterValidation extends GenericValidation {

    private BusinessObjectService businessObjectService;
    private AccountService accountService;
    private ObjectCodeService objectCodeService;
    private ChartService chartService;

    public void setObjectCodeService(ObjectCodeService objectCodeService) {
        this.objectCodeService = objectCodeService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setChartService(ChartService chartService) {
        this.chartService = chartService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public boolean validateAccountNumber(String accountNumber) {
        boolean valid = true;
        Account account = accountService.getUniqueAccountForAccountNumber(accountNumber);
        if (account == null) {
            GlobalVariables.getMessageMap().putError(OLEConstants.OrderQueue.REQUISITIONS, OLEKeyConstants.ERROR_FILTER_VALIDATION,
                    OLEPropertyConstants.ACCOUNT_NUMBER);
            valid = false;
        }
        return valid;
    }

    public boolean validateObjectCode(String objectCode) {
        boolean valid = true;
        Map<String, String> objectMap = new HashMap<String, String>();
        objectMap.put("financialObjectCode", objectCode);
        Object objects = (Object) businessObjectService.findByPrimaryKey(ObjectCode.class, objectMap);

        if (objects == null) {
            GlobalVariables.getMessageMap().putError(OLEConstants.OrderQueue.REQUISITIONS, OLEKeyConstants.ERROR_FILTER_VALIDATION,
                    OLEConstants.OrderQueue.OBJECT_CODE);
            valid = false;
        }
        return valid;
    }


    public boolean validateChartCode(String chartCode) {
        boolean valid = true;
        Chart chart = chartService.getByPrimaryId(chartCode);
        if (chart == null) {
            GlobalVariables.getMessageMap().putError(OLEConstants.OrderQueue.REQUISITIONS, OLEKeyConstants.ERROR_FILTER_VALIDATION,
                    OLEConstants.OrderQueue.CHART_CODE);
            valid = false;
        }
        return valid;
    }

    // jira OLE-2363 Validation for chart code, account and object code for order holding queue.
    @Override
    public boolean validate(AttributedDocumentEvent event) {
        boolean valid = true;
        String chartOfAccountsCode = ((OleAccountFilterEvent) event).getChartOfAccountsCode();
        String accountNumber = ((OleAccountFilterEvent) event).getAccountNumber();
        String financialObjectCode = ((OleAccountFilterEvent) event).getObjectCode();
        if (accountNumber != null) {
            if (chartOfAccountsCode == null) {
                valid = false;
                GlobalVariables.getMessageMap().putError(OLEConstants.OrderQueue.REQUISITIONS, OLEKeyConstants.ERROR_CHART_FILTER_VALIDATION,
                        OLEConstants.OrderQueue.CHART_CODE);
            }
            if (valid) {
                valid &= validateAccountNumber(accountNumber.toUpperCase());
            }
        }
        if (financialObjectCode != null) {
            if (chartOfAccountsCode == null) {
                valid = false;
                GlobalVariables.getMessageMap().putError(OLEConstants.OrderQueue.REQUISITIONS, OLEKeyConstants.ERROR_CHART_FILTER_VALIDATION,
                        OLEConstants.OrderQueue.CHART_CODE);
            }
            if (valid) {
                valid &= validateObjectCode(financialObjectCode.toUpperCase());
            }
        }
        if (chartOfAccountsCode != null && financialObjectCode != null) {
            valid &= validateChartCode(chartOfAccountsCode.toUpperCase());
        }
        if (valid) {
            if (accountNumber != null && chartOfAccountsCode != null) {
                Account account = accountService.getByPrimaryId(chartOfAccountsCode.toUpperCase(),
                        accountNumber.toUpperCase());
                if (account == null) {
                    valid &= false;
                    GlobalVariables.getMessageMap().putError(OLEConstants.OrderQueue.REQUISITIONS, OLEKeyConstants.ERROR_ACCOUNT_INFORMATION_FILTER_VALIDATION,
                            OLEPropertyConstants.ACCOUNT_NUMBER);
                }
            }
            if (financialObjectCode != null && chartOfAccountsCode != null) {
                ObjectCode objectCode = objectCodeService.getByPrimaryIdForCurrentYear(chartOfAccountsCode.toUpperCase(),
                        financialObjectCode.toUpperCase());
                if (objectCode == null) {
                    GlobalVariables.getMessageMap().putError(OLEConstants.OrderQueue.REQUISITIONS, OLEKeyConstants.ERROR_ACCOUNT_INFORMATION_FILTER_VALIDATION,
                            OLEConstants.OrderQueue.OBJECT_CODE);
                    valid &= false;

                }
            }
        }
        return valid;
    }


}
