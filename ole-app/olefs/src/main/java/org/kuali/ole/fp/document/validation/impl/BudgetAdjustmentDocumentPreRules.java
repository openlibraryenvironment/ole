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
package org.kuali.ole.fp.document.validation.impl;

import java.util.ArrayList;
import java.util.List;

import org.kuali.ole.fp.document.BudgetAdjustmentDocument;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.businessobject.AccountingLine;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kns.rules.PromptBeforeValidationBase;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.util.ObjectUtils;

/**
 * Checks warnings and prompt conditions for ba document.
 */
public class BudgetAdjustmentDocumentPreRules extends PromptBeforeValidationBase {
    protected ConfigurationService kualiConfiguration;


    /**
     * Execute pre-rules for BudgetAdjustmentDocument
     *
     * @document document with pre-rules being applied
     * @return true if pre-rules fire without problem
     * @see org.kuali.rice.kns.rules.PromptBeforeValidationBase#doRules(org.kuali.rice.kns.document.MaintenanceDocument)
     */
    @Override
    public boolean doPrompts(Document document) {
        boolean preRulesOK = true;

//        BudgetAdjustmentDocument budgetDocument = (BudgetAdjustmentDocument) document;
//        preRulesOK = askLaborBenefitsGeneration(budgetDocument);

        return preRulesOK;
    }

    /**
     * TODO: remove this method once baseline accounting lines has been removed
     */
    protected List deepCopyAccountingLinesList(List originals) {
        if (originals == null) {
            return null;
        }
        List copiedLines = new ArrayList();
        for (int i = 0; i < originals.size(); i++) {
            copiedLines.add(ObjectUtils.deepCopy((AccountingLine) originals.get(i)));
        }
        return copiedLines;
    }

    /**
     * Based on the routing status of the document, determines if labor benefits can be generated on the document
     * @param budgetAdjustmentDocument the budget adjustment document that labor benefits would be generated on
     * @return true if labor benefits can be generated, false otherwise
     */
    protected boolean canGenerateLaborBenefitsByRouteStatus(BudgetAdjustmentDocument budgetAdjustmentDocument) {
        final WorkflowDocument workflowDocument = budgetAdjustmentDocument.getDocumentHeader().getWorkflowDocument();
        if (workflowDocument.isInitiated() || workflowDocument.isSaved())
         {
            return true; // we're pre-route; we can add labor benefits
        }
        if (workflowDocument.isEnroute() && workflowDocument.getCurrentRouteNodeInstances().contains(OLEConstants.RouteLevelNames.ACCOUNT))
         {
            return true; // we're fiscal officers approving; we can add labor benefits
        }
        return false; // we're someone else and we're plum out of luck
    }
}

