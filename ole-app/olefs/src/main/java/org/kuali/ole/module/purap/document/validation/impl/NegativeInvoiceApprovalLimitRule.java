/*
 * Copyright 2010 The Kuali Foundation.
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
package org.kuali.ole.module.purap.document.validation.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.module.purap.PurapKeyConstants;
import org.kuali.ole.module.purap.businessobject.NegativeInvoiceApprovalLimit;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.kns.maintenance.rules.MaintenanceDocumentRuleBase;

/**
 * Business rules for the NegativeInvoiceApprovalLimit maintenance document
 */
public class NegativeInvoiceApprovalLimitRule extends MaintenanceDocumentRuleBase {

    /**
     * @see org.kuali.rice.kns.maintenance.rules.MaintenanceDocumentRuleBase#processCustomApproveDocumentBusinessRules(org.kuali.rice.kns.document.MaintenanceDocument)
     */
    @Override
    protected boolean processCustomApproveDocumentBusinessRules(MaintenanceDocument document) {
        boolean result = super.processCustomApproveDocumentBusinessRules(document);
        final NegativeInvoiceApprovalLimit limit = (NegativeInvoiceApprovalLimit) getNewBo();
        result &= checkExclusiveOrganizationCodeAndAccountNumber(limit);
        return result;
    }

    /**
     * @see org.kuali.rice.kns.maintenance.rules.MaintenanceDocumentRuleBase#processCustomRouteDocumentBusinessRules(org.kuali.rice.kns.document.MaintenanceDocument)
     */
    @Override
    protected boolean processCustomRouteDocumentBusinessRules(MaintenanceDocument document) {
        boolean result = super.processCustomRouteDocumentBusinessRules(document);
        final NegativeInvoiceApprovalLimit limit = (NegativeInvoiceApprovalLimit) getNewBo();
        result &= checkExclusiveOrganizationCodeAndAccountNumber(limit);
        return result;
    }

    /**
     * Checks that organization code and account number aren't both specified on a new NegativeInvoiceApprovalLimit
     *
     * @param limit the NegativeInvoiceApprovalLimit to check
     * @return true if the rule passed, false otherwise (with error message added)
     */
    protected boolean checkExclusiveOrganizationCodeAndAccountNumber(NegativeInvoiceApprovalLimit limit) {
        if (!StringUtils.isBlank(limit.getOrganizationCode()) && !StringUtils.isBlank(limit.getAccountNumber())) {
            putFieldError(OLEPropertyConstants.ORGANIZATION_CODE, PurapKeyConstants.ERROR_NEGATIVE_PAYMENT_REQUEST_APPROVAL_LIMIT_ORG_AND_ACCOUNT_EXCLUSIVE, new String[]{});
            putFieldError(OLEPropertyConstants.ACCOUNT_NUMBER, PurapKeyConstants.ERROR_NEGATIVE_PAYMENT_REQUEST_APPROVAL_LIMIT_ORG_AND_ACCOUNT_EXCLUSIVE, new String[]{});
            return false;
        }
        return true;
    }

}
