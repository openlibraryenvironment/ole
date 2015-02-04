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
import static org.kuali.ole.sys.OLEPropertyConstants.FINANCIAL_OBJECT_CODE;
import static org.kuali.ole.sys.OLEPropertyConstants.FINANCIAL_SUB_OBJECT_CODE;
import static org.kuali.ole.sys.OLEPropertyConstants.ORGANIZATION_REFERENCE_ID;
import static org.kuali.ole.sys.OLEPropertyConstants.PROJECT_CODE;
import static org.kuali.ole.sys.OLEPropertyConstants.REFERENCE_NUMBER;
import static org.kuali.ole.sys.OLEPropertyConstants.SUB_ACCOUNT_NUMBER;

import org.kuali.ole.sys.businessobject.AccountingLineParserBase;


/**
 * This class represents a <code>PreEncumbranceDocument</code> accounting line parser.
 * 
 * @see org.kuali.ole.fp.document.PreEncumbranceDocument
 */
public class PreEncumbranceDocumentAccountingLineParser extends AccountingLineParserBase {
    protected static final String[] PE_SOURCE_FORMAT = { CHART_OF_ACCOUNTS_CODE, ACCOUNT_NUMBER, SUB_ACCOUNT_NUMBER, FINANCIAL_OBJECT_CODE, FINANCIAL_SUB_OBJECT_CODE, PROJECT_CODE, ORGANIZATION_REFERENCE_ID, AMOUNT };
    protected static final String[] PE_TARGET_FORMAT = { CHART_OF_ACCOUNTS_CODE, ACCOUNT_NUMBER, SUB_ACCOUNT_NUMBER, FINANCIAL_OBJECT_CODE, FINANCIAL_SUB_OBJECT_CODE, PROJECT_CODE, ORGANIZATION_REFERENCE_ID, REFERENCE_NUMBER, AMOUNT };

    /**
     * @see org.kuali.rice.krad.bo.AccountingLineParserBase#getSourceAccountingLineFormat()
     */
    @Override
    public String[] getSourceAccountingLineFormat() {
        return removeChartFromFormatIfNeeded(PE_SOURCE_FORMAT);
    }

    /**
     * @see org.kuali.rice.krad.bo.AccountingLineParserBase#getTargetAccountingLineFormat()
     */
    @Override
    public String[] getTargetAccountingLineFormat() {
        return removeChartFromFormatIfNeeded(PE_TARGET_FORMAT);
    }

}
