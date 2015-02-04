/*
 * Copyright 2008 The Kuali Foundation
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
package org.kuali.ole.coa.document;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.coa.businessobject.Account;
import org.kuali.ole.coa.businessobject.IndirectCostRecoveryRateDetail;
import org.kuali.ole.coa.service.AccountService;
import org.kuali.ole.gl.GeneralLedgerConstants;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.FinancialSystemMaintainable;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.util.ObjectUtils;

public class IndirectCostRecoveryRateMaintainableImpl extends FinancialSystemMaintainable {
    private static final Logger LOG = Logger.getLogger(IndirectCostRecoveryRateMaintainableImpl.class);  

    private Integer indirectCostRecoveryRateNextEntryNumber;

    /**
     * Hook for quantity and setting asset numbers.
     * @see org.kuali.rice.kns.maintenance.KualiMaintainableImpl#addNewLineToCollection(java.lang.String)
     */
    @Override
    public void addNewLineToCollection(String collectionName) {

        // create handle for the addline section of the doc
        IndirectCostRecoveryRateDetail addLine = (IndirectCostRecoveryRateDetail) newCollectionLines.get(collectionName);
        List<IndirectCostRecoveryRateDetail> maintCollection = (List<IndirectCostRecoveryRateDetail>) ObjectUtils.getPropertyValue(getBusinessObject(), collectionName);

        if(StringUtils.isBlank(addLine.getSubAccountNumber()) || StringUtils.containsOnly(addLine.getSubAccountNumber(), "-")) {
            addLine.setSubAccountNumber(OLEConstants.getDashSubAccountNumber());
        }
        if(StringUtils.isBlank(addLine.getFinancialSubObjectCode()) || StringUtils.containsOnly(addLine.getFinancialSubObjectCode(), "-")) {
            addLine.setFinancialSubObjectCode(OLEConstants.getDashFinancialSubObjectCode());
        }

        Integer icrEntryNumberMax = 0;
        for(IndirectCostRecoveryRateDetail item : maintCollection) {
            if(icrEntryNumberMax < item.getAwardIndrCostRcvyEntryNbr()) {
                icrEntryNumberMax = item.getAwardIndrCostRcvyEntryNbr();
            }
        }

        // addLine.setActive(true); // TODO remove after active indicator fixes
        addLine.setNewCollectionRecord(true);
        addLine.setAwardIndrCostRcvyEntryNbr(icrEntryNumberMax + 1);
        maintCollection.add(addLine);
        initNewCollectionLine(collectionName);
    }

    /**
     * @see org.kuali.ole.sys.document.FinancialSystemMaintainable#populateChartOfAccountsCodeFields()
     * 
     * Special treatment is needed to populate the chart code from the account number field in IndirectCostRecoveryRateDetails, 
     * as the potential reference account doesn't exist in the collection due to wild cards, which also needs special handling.  
     */
    @Override
    protected void populateChartOfAccountsCodeFields() {
        // calling super method in case there're reference accounts/collections other than ICR rates
        super.populateChartOfAccountsCodeFields();
        
        PersistableBusinessObject bo = getBusinessObject();        
        AccountService acctService = SpringContext.getBean(AccountService.class);    
        PersistableBusinessObject newAccount = getNewCollectionLine(OLEPropertyConstants.INDIRECT_COST_RECOVERY_RATE_DETAILS);
        String accountNumber = (String)ObjectUtils.getPropertyValue(newAccount, OLEPropertyConstants.ACCOUNT_NUMBER);
        String coaCode = null;
        
        // if accountNumber is wild card, populate chart code with the same wild card
        if (GeneralLedgerConstants.PosterService.SYMBOL_USE_EXPENDITURE_ENTRY.equals(accountNumber) || 
            GeneralLedgerConstants.PosterService.SYMBOL_USE_ICR_FROM_ACCOUNT.equals(accountNumber)) {
            coaCode = accountNumber;
        }
        // otherwise do the normal account lookup
        else {
            Account account = acctService.getUniqueAccountForAccountNumber(accountNumber);            
            if (ObjectUtils.isNotNull(account)) {
                coaCode = account.getChartOfAccountsCode();
            }
        }
         
        // populate chart code field
        try {
            ObjectUtils.setObjectProperty(newAccount, OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, coaCode); 
        }
        catch (Exception e) {
            LOG.error("Error in setting property value for " + OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE);
        } 
    }    
    
}
