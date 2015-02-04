/*
 * Copyright 2007 The Kuali Foundation
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

import java.util.ArrayList;
import java.util.List;

import org.kuali.ole.coa.businessobject.Account;
import org.kuali.ole.coa.businessobject.AccountGlobal;
import org.kuali.ole.coa.businessobject.AccountGlobalDetail;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.document.FinancialSystemGlobalMaintainable;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.maintenance.MaintenanceLock;

/**
 * This class overrides the base {@link KualiGlobalMaintainableImpl} to generate the specific maintenance locks for Global accounts
 */
public class AccountGlobalMaintainableImpl extends FinancialSystemGlobalMaintainable {

    /**
     * This creates the particular locking representation for this global document.
     * 
     * @see org.kuali.rice.kns.maintenance.Maintainable#generateMaintenanceLocks()
     */
    @Override
    public List<MaintenanceLock> generateMaintenanceLocks() {
        AccountGlobal accountGlobal = (AccountGlobal) getBusinessObject();
        List<MaintenanceLock> maintenanceLocks = new ArrayList();

        for (AccountGlobalDetail detail : accountGlobal.getAccountGlobalDetails()) {
            MaintenanceLock maintenanceLock = new MaintenanceLock();
            StringBuffer lockrep = new StringBuffer();

            lockrep.append(Account.class.getName() + OLEConstants.Maintenance.AFTER_CLASS_DELIM);
            lockrep.append("chartOfAccountsCode" + OLEConstants.Maintenance.AFTER_FIELDNAME_DELIM);
            lockrep.append(detail.getChartOfAccountsCode() + OLEConstants.Maintenance.AFTER_VALUE_DELIM);
            lockrep.append("accountNumber" + OLEConstants.Maintenance.AFTER_FIELDNAME_DELIM);
            lockrep.append(detail.getAccountNumber());

            maintenanceLock.setDocumentNumber(accountGlobal.getDocumentNumber());
            maintenanceLock.setLockingRepresentation(lockrep.toString());
            maintenanceLocks.add(maintenanceLock);
        }
        return maintenanceLocks;
    }

    @Override
    public Class<? extends PersistableBusinessObject> getPrimaryEditedBusinessObjectClass() {
        return Account.class;
    }
}
