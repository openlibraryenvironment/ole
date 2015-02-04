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
package org.kuali.ole.fixture;

import org.kuali.ole.sys.businessobject.GeneralLedgerPendingEntry;

public enum GeneralLedgerPendingEntryFixture {
    EXPECTED_EXPLICIT_SOURCE_PENDING_ENTRY_FOR_EXPENSE("UA", "1912201", "BEER", "D", "AC", "OLE_TF", "TE", "9900", false, "KUL"), 
    EXPECTED_EXPLICIT_TARGET_PENDING_ENTRY_FOR_EXPENSE(null, null, "BEER", "C", "AC", "OLE_TF", "TE", "9900", false, "KUL"), 
    EXPECTED_GEC_EXPLICIT_SOURCE_PENDING_ENTRY_FOR_EXPENSE(null, null, null, "C", "AC", "OLE_GEC", "EX", "1940", false, "KUL"), 
    EXPECTED_GEC_EXPLICIT_TARGET_PENDING_ENTRY_FOR_EXPENSE(null, null, null, "D", "AC", "OLE_GEC", "EX", "1940", false, "KUL"), 
    EXPECTED_GEC_EXPLICIT_SOURCE_PENDING_ENTRY(null, null, null, "C", "AC", "OLE_GEC", "AS", "8111", false, "KUL"), 
    EXPECTED_GEC_EXPLICIT_TARGET_PENDING_ENTRY(null, null, null, "D", "AC", "OLE_GEC", "AS", "8111", false, "KUL"), 
    EXPECTED_GEC_OFFSET_SOURCE_PENDING_ENTRY("BA", "6044900", null, "D", "AC", "OLE_GEC", "AS", "8000", true, "KUL"), 
    EXPECTED_GEC_OFFSET_TARGET_PENDING_ENTRY(null, null, null, "C", "AC", "OLE_GEC", "AS", "8000", true, "KUL"), 
    EXPECTED_JV_EXPLICIT_SOURCE_PENDING_ENTRY_FOR_EXPENSE(null, null, "BEER", "D", "AC", "OLE_JV", "EX", "9900", false, "KUL"), 
    EXPECTED_JV_EXPLICIT_TARGET_PENDING_ENTRY_FOR_EXPENSE(null, null, null, "D", "AC", "OLE_JV", "EX", "9900", false, null), 
    EXPECTED_JV_EXPLICIT_SOURCE_PENDING_ENTRY(null, null, null, "D", "AC", "OLE_JV", "AS", "9980", false, "KUL"), 
    EXPECTED_JV_EXPLICIT_TARGET_PENDING_ENTRY(null, null, null, "D", "AC", "OLE_JV", "TI", "9980", false, null), 
    EXPECTED_OFFSET_SOURCE_PENDING_ENTRY("UA", "1912201", "BEER", "C", "AC", "OLE_TF", "AS", "8000", true, "KUL"), 
    EXPECTED_OFFSET_TARGET_PENDING_ENTRY(null, null, "BEER", "D", "AC", "OLE_TF", "AS", "8000", true, "KUL"), 
    EXPECTED_EXPLICIT_SOURCE_PENDING_ENTRY(null, null, null, "D", "AC", "OLE_TF", "AS", "9980", false, null), 
    EXPECTED_EXPLICIT_TARGET_PENDING_ENTRY(null, null, null, "C", "AC", "OLE_TF", "AS", "9980", false, null), 
    EXPECTED_FLEXIBLE_EXPLICIT_SOURCE_PENDING_ENTRY_FOR_EXPENSE2("BL", "2231401", null, "C", "AC", "OLE_TF", "TE", "9900", false, "KUL"), 
    EXPECTED_FLEXIBLE_EXPLICIT_SOURCE_PENDING_ENTRY_FOR_EXPENSE("BL", "2231401", null, "D", "AC", "OLE_TF", "TE", "9900", false, "KUL"), 
    EXPECTED_FLEXIBLE_OFFSET_SOURCE_PENDING_ENTRY("UA", "1912201", null, "D", "AC", "OLE_TF", "AS", "8000", true, "KUL"), 
    EXPECTED_FLEXIBLE_OFFSET_SOURCE_PENDING_ENTRY_MISSING_OFFSET_DEFINITION("BL", "2231401", null, "C", "AC", "OLE_TF", "--", "----", true, "KUL"), 
    EXPECTED_AV_EXPLICIT_TARGET_PENDING_ENTRY_FOR_EXPENSE(null, null, "BEER", "D", null, "OLE_AVAD", "ES", "9900", false, "KUL"), 
    EXPECTED_AV_EXPLICIT_SOURCE_PENDING_ENTRY_FOR_EXPENSE(null, null, null, "D", "AC", "OLE_AVAD", "ES", "1940", false, "KUL"), 
    EXPECTED_AV_EXPLICIT_SOURCE_PENDING_ENTRY(null, null, null, "D", "AC", "OLE_AVAD", "AS", "8111", false, "KUL"), 
    EXPECTED_AV_EXPLICIT_TARGET_PENDING_ENTRY(null, null, null, "D", "AC", "OLE_AVAD", "AS", "8111", false, "KUL"), ;

    public final String chartOfAccountsCode;
    public final String accountNumber;
    public final String subAccountNumber;
    public final String transactionDebitCreditCode;
    public final String financialBalanceTypeCode;
    public final String financialDocumentTypeCode;
    public final String financialObjectTypeCode;
    public final String financialObjectCode;
    public final boolean transactionEntryOffsetIndicator;
    public final String projectCode;


    private GeneralLedgerPendingEntryFixture(String chartOfAccountsCode, String accountNumber, String subAccountNumber, String transactionDebitCreditCode, String financialBalanceTypeCode, String financialDocumentTypeCode, String financialObjectTypeCode, String financialObjectCode, boolean transactionEntryOffsetIndicator, String projectCode) {
        this.chartOfAccountsCode = chartOfAccountsCode;
        this.accountNumber = accountNumber;
        this.subAccountNumber = subAccountNumber;
        this.transactionDebitCreditCode = transactionDebitCreditCode;
        this.financialBalanceTypeCode = financialBalanceTypeCode;
        this.financialDocumentTypeCode = financialDocumentTypeCode;
        this.financialObjectTypeCode = financialObjectTypeCode;
        this.financialObjectCode = financialObjectCode;
        this.transactionEntryOffsetIndicator = transactionEntryOffsetIndicator;
        this.projectCode = projectCode;
    }


    public GeneralLedgerPendingEntry createGeneralLedgerPendingEntry() {
        GeneralLedgerPendingEntry glpe = new GeneralLedgerPendingEntry();
        glpe.setChartOfAccountsCode(this.chartOfAccountsCode);
        glpe.setAccountNumber(this.accountNumber);
        glpe.setSubAccountNumber(this.subAccountNumber);
        glpe.setTransactionDebitCreditCode(this.transactionDebitCreditCode);
        glpe.setFinancialBalanceTypeCode(this.financialBalanceTypeCode);
        glpe.setFinancialDocumentTypeCode(this.financialDocumentTypeCode);
        glpe.setFinancialObjectTypeCode(this.financialObjectTypeCode);
        glpe.setFinancialObjectCode(this.financialObjectCode);
        glpe.setTransactionEntryOffsetIndicator(transactionEntryOffsetIndicator);
        glpe.setProjectCode(this.projectCode);

        return glpe;
    }

}
