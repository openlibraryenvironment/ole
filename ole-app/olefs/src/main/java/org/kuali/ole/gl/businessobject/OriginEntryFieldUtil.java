/*
 * Copyright 2007-2009 The Kuali Foundation
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
package org.kuali.ole.gl.businessobject;

import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.businessobject.BusinessObjectStringParserFieldUtils;
import org.kuali.rice.krad.bo.BusinessObject;

/**
 * This class has utility methods for parsing OriginEntries from Strings
 */
public class OriginEntryFieldUtil extends BusinessObjectStringParserFieldUtils {

    /**
     * Returns the class to parse into - OriginEntryFull
     * @see org.kuali.ole.sys.businessobject.BusinessObjectStringParserFieldUtils#getBusinessObjectClass()
     */
    @Override
    public Class<? extends BusinessObject> getBusinessObjectClass() {
        return OriginEntryFull.class;
    }

    /**
     * Returns the fields to be parsed from a String, in order, to form an OriginEntryFull
     * @see org.kuali.ole.sys.businessobject.BusinessObjectStringParserFieldUtils#getOrderedProperties()
     */
    @Override
    public String[] getOrderedProperties() {
        return new String[] {
                OLEPropertyConstants.UNIVERSITY_FISCAL_YEAR,
                OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE,
                OLEPropertyConstants.ACCOUNT_NUMBER,
                OLEPropertyConstants.SUB_ACCOUNT_NUMBER,
                OLEPropertyConstants.FINANCIAL_OBJECT_CODE,
                OLEPropertyConstants.FINANCIAL_SUB_OBJECT_CODE,
                OLEPropertyConstants.FINANCIAL_BALANCE_TYPE_CODE,
                OLEPropertyConstants.FINANCIAL_OBJECT_TYPE_CODE,
                OLEPropertyConstants.UNIVERSITY_FISCAL_PERIOD_CODE,
                OLEPropertyConstants.FINANCIAL_DOCUMENT_TYPE_CODE,
                OLEPropertyConstants.FINANCIAL_SYSTEM_ORIGINATION_CODE,
                OLEPropertyConstants.DOCUMENT_NUMBER,
                OLEPropertyConstants.TRANSACTION_ENTRY_SEQUENCE_NUMBER,
                OLEPropertyConstants.TRANSACTION_LEDGER_ENTRY_DESC,
                OLEPropertyConstants.TRANSACTION_LEDGER_ENTRY_AMOUNT,
                OLEPropertyConstants.TRANSACTION_DEBIT_CREDIT_CODE,
                OLEPropertyConstants.TRANSACTION_DATE,
                OLEPropertyConstants.ORGANIZATION_DOCUMENT_NUMBER,
                OLEPropertyConstants.PROJECT_CODE,
                OLEPropertyConstants.ORGANIZATION_REFERENCE_ID,
                OLEPropertyConstants.REFERENCE_FIN_DOCUMENT_TYPE_CODE,
                OLEPropertyConstants.FIN_SYSTEM_REF_ORIGINATION_CODE,
                OLEPropertyConstants.FINANCIAL_DOCUMENT_REFERENCE_NBR,
                OLEPropertyConstants.FINANCIAL_DOCUMENT_REVERSAL_DATE,
                OLEPropertyConstants.TRANSACTION_ENCUMBRANCE_UPDT_CD
            };
    }
}
