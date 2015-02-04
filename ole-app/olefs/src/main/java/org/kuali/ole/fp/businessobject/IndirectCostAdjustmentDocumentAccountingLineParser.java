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

package org.kuali.ole.fp.businessobject;

import static org.kuali.ole.sys.OLEPropertyConstants.ACCOUNT_NUMBER;
import static org.kuali.ole.sys.OLEPropertyConstants.AMOUNT;
import static org.kuali.ole.sys.OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE;
import static org.kuali.ole.sys.OLEPropertyConstants.FINANCIAL_SUB_OBJECT_CODE;
import static org.kuali.ole.sys.OLEPropertyConstants.ORGANIZATION_REFERENCE_ID;
import static org.kuali.ole.sys.OLEPropertyConstants.PROJECT_CODE;
import static org.kuali.ole.sys.OLEPropertyConstants.SUB_ACCOUNT_NUMBER;

import java.util.Map;

import org.kuali.ole.fp.document.IndirectCostAdjustmentDocument;
import org.kuali.ole.fp.document.validation.impl.IndirectCostAdjustmentDocumentRuleConstants;
import org.kuali.ole.sys.businessobject.AccountingLineParserBase;
import org.kuali.ole.sys.businessobject.SourceAccountingLine;
import org.kuali.ole.sys.businessobject.TargetAccountingLine;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;

/**
 * This class represents an <code>IndirectCostAdjustmentDocument</code> accounting line parser.
 * 
 * @see org.kuali.ole.fp.document.IndirectCostAdjustmentDocument
 */
public class IndirectCostAdjustmentDocumentAccountingLineParser extends AccountingLineParserBase {
    protected static final String[] ICA_FORMAT = { CHART_OF_ACCOUNTS_CODE, ACCOUNT_NUMBER, SUB_ACCOUNT_NUMBER, FINANCIAL_SUB_OBJECT_CODE, PROJECT_CODE, ORGANIZATION_REFERENCE_ID, AMOUNT };

    /**
     * @see org.kuali.rice.krad.bo.AccountingLineParserBase#getSourceAccountingLineFormat()
     */
    @Override
    public String[] getSourceAccountingLineFormat() {
        return removeChartFromFormatIfNeeded(ICA_FORMAT);
    }

    /**
     * @see org.kuali.rice.krad.bo.AccountingLineParserBase#getTargetAccountingLineFormat()
     */
    @Override
    public String[] getTargetAccountingLineFormat() {
        return removeChartFromFormatIfNeeded(ICA_FORMAT);
    }

    /**
     * @see org.kuali.rice.krad.bo.AccountingLineParserBase#performCustomSourceAccountingLinePopulation(java.util.Map,
     *      org.kuali.rice.krad.bo.SourceAccountingLine, java.lang.String)
     */
    @Override
    protected void performCustomSourceAccountingLinePopulation(Map<String, String> attributeValueMap, SourceAccountingLine sourceAccountingLine, String accountingLineAsString) {
        super.performCustomSourceAccountingLinePopulation(attributeValueMap, sourceAccountingLine, accountingLineAsString);
        String financialObjectCode = SpringContext.getBean(ParameterService.class).getParameterValueAsString(IndirectCostAdjustmentDocument.class, IndirectCostAdjustmentDocumentRuleConstants.GRANT_OBJECT_CODE);
        sourceAccountingLine.setFinancialObjectCode(financialObjectCode);
    }

    /**
     * @see org.kuali.rice.krad.bo.AccountingLineParserBase#performCustomTargetAccountingLinePopulation(java.util.Map,
     *      org.kuali.rice.krad.bo.TargetAccountingLine, java.lang.String)
     */
    @Override
    protected void performCustomTargetAccountingLinePopulation(Map<String, String> attributeValueMap, TargetAccountingLine targetAccountingLine, String accountingLineAsString) {
        super.performCustomTargetAccountingLinePopulation(attributeValueMap, targetAccountingLine, accountingLineAsString);
        String financialObjectCode = SpringContext.getBean(ParameterService.class).getParameterValueAsString(IndirectCostAdjustmentDocument.class, IndirectCostAdjustmentDocumentRuleConstants.RECEIPT_OBJECT_CODE);
        targetAccountingLine.setFinancialObjectCode(financialObjectCode);
    }

}
