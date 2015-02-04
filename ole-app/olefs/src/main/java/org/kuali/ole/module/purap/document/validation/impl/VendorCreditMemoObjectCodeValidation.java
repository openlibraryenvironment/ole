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
package org.kuali.ole.module.purap.document.validation.impl;

import org.kuali.ole.coa.businessobject.ObjectCode;
import org.kuali.ole.module.purap.PurapRuleConstants;
import org.kuali.ole.module.purap.businessobject.PurApAccountingLine;
import org.kuali.ole.module.purap.document.VendorCreditMemoDocument;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.businessobject.SourceAccountingLine;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.validation.GenericValidation;
import org.kuali.ole.sys.document.validation.event.AddAccountingLineEvent;
import org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent;
import org.kuali.rice.core.api.parameter.ParameterEvaluator;
import org.kuali.rice.core.api.parameter.ParameterEvaluatorService;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;

public class VendorCreditMemoObjectCodeValidation extends GenericValidation {

    private ParameterService parameterService;

    public boolean validate(AttributedDocumentEvent event) {
        boolean valid = true;
        PurApAccountingLine account = (PurApAccountingLine) ((AddAccountingLineEvent) event).getAccountingLine();
        ObjectCode objectCode = account.getObjectCode();

        ParameterEvaluator parameterEvaluator = /*REFACTORME*/SpringContext.getBean(ParameterEvaluatorService.class).getParameterEvaluator(VendorCreditMemoDocument.class, PurapRuleConstants.VALID_OBJECT_LEVELS_BY_OBJECT_TYPE_PARM_NM, PurapRuleConstants.INVALID_OBJECT_LEVELS_BY_OBJECT_TYPE_PARM_NM, objectCode.getFinancialObjectTypeCode(), objectCode.getFinancialObjectLevelCode());
        return parameterEvaluator.evaluateAndAddError(SourceAccountingLine.class, "objectCode.financialObjectLevelCode", OLEPropertyConstants.FINANCIAL_OBJECT_CODE);
    }

    public ParameterService getParameterService() {
        return parameterService;
    }

    public void setParameterService(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

}
