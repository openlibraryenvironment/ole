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
package org.kuali.ole.fp.document;

import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.*;
import org.kuali.ole.fixture.AccountingLineFixture;
import org.kuali.ole.fixture.UserNameFixture;
import org.kuali.ole.sys.businessobject.SourceAccountingLine;
import org.kuali.ole.sys.businessobject.TargetAccountingLine;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.AccountingDocumentTestUtils;
import org.kuali.rice.kns.service.DataDictionaryService;
import org.kuali.rice.kns.service.TransactionalDocumentDictionaryService;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.ArrayList;
import java.util.List;

import static org.kuali.ole.fixture.AccountingLineFixture.LINE15;
import static org.kuali.ole.fixture.UserNameFixture.khuntley;
import static org.kuali.ole.sys.OLEConstants.GL_CREDIT_CODE;
import static org.kuali.ole.sys.OLEConstants.GL_DEBIT_CODE;
import static org.kuali.ole.sys.document.AccountingDocumentTestUtils.testGetNewDocument_byDocumentClass;

/**
 * This class is used to test NonCheckDisbursementDocumentTest.
 */

public class AuxiliaryVoucherDocumentTest extends KFSTestCaseBase {

    public static final Class<AuxiliaryVoucherDocument> DOCUMENT_CLASS = AuxiliaryVoucherDocument.class;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        changeCurrentUser(UserNameFixture.khuntley);
    }

    private Document getDocumentParameterFixture() throws Exception {
        // AV document has a restriction on accounting period cannot be more than 2 periods behind current
        return DocumentTestUtils.createDocument(SpringContext.getBean(DocumentService.class), AuxiliaryVoucherDocument.class);
    }

    private List<AccountingLineFixture> getSourceAccountingLineParametersFromFixtures() {
        List<AccountingLineFixture> list = new ArrayList<AccountingLineFixture>();
        list.add(LINE15);
        return list;
    }

    private AuxiliaryVoucherDocument buildDocument() throws Exception {
        // put accounting lines into document parameter for later
        AuxiliaryVoucherDocument document = (AuxiliaryVoucherDocument) getDocumentParameterFixture();

        // set accountinglines to document
        for (AccountingLineFixture sourceFixture : getSourceAccountingLineParametersFromFixtures()) {
            SourceAccountingLine line = sourceFixture.createAccountingLine(SourceAccountingLine.class, document.getDocumentNumber(), document.getPostingYear(), document.getNextSourceLineNumber());
            SourceAccountingLine balance = sourceFixture.createAccountingLine(SourceAccountingLine.class, document.getDocumentNumber(), document.getPostingYear(), document.getNextSourceLineNumber());
            balance.setDebitCreditCode(GL_DEBIT_CODE.equals(line.getDebitCreditCode()) ? GL_CREDIT_CODE : GL_DEBIT_CODE);
            document.addSourceAccountingLine(line);
            document.addSourceAccountingLine(balance);

        }

        return document;
    }


    private int getExpectedPrePeCount() {
        return 2;
    }


    @Test
    public final void testAddAccountingLine() throws Exception {
        GlobalVariables.getMessageMap().clearErrorMessages();
        List<SourceAccountingLine> sourceLines = generateSouceAccountingLines();
        List<TargetAccountingLine> targetLines = new ArrayList<TargetAccountingLine>();
        int expectedSourceTotal = sourceLines.size();
        int expectedTargetTotal = targetLines.size();
        AccountingDocumentTestUtils.testAddAccountingLine(DocumentTestUtils.createDocument(SpringContext.getBean(DocumentService.class), DOCUMENT_CLASS), sourceLines, targetLines, expectedSourceTotal, expectedTargetTotal);
    }

    @Test
    public final void testGetNewDocument() throws Exception {
        GlobalVariables.getMessageMap().clearErrorMessages();
        testGetNewDocument_byDocumentClass(DOCUMENT_CLASS, SpringContext.getBean(DocumentService.class));
    }

    @Test
    public final void testConvertIntoErrorCorrection_documentAlreadyCorrected() throws Exception {
        GlobalVariables.getMessageMap().clearErrorMessages();
        AccountingDocumentTestUtils.testConvertIntoErrorCorrection_documentAlreadyCorrected(buildDocument(), SpringContext.getBean(TransactionalDocumentDictionaryService.class));
    }

    @Test
    public final void testConvertIntoErrorCorrection_errorCorrectionDisallowed() throws Exception {
        GlobalVariables.getMessageMap().clearErrorMessages();
        AccountingDocumentTestUtils.testConvertIntoErrorCorrection_errorCorrectionDisallowed(buildDocument(), SpringContext.getBean(DataDictionaryService.class));
    }

    @Test
    public final void testRouteDocument() throws Exception {
        GlobalVariables.getMessageMap().clearErrorMessages();
        AccountingDocumentTestUtils.testRouteDocument(buildDocument(), SpringContext.getBean(DocumentService.class));
    }

    @Test
    public void testSaveDocument() throws Exception {
        GlobalVariables.getMessageMap().clearErrorMessages();
        AccountingDocumentTestUtils.testSaveDocument(buildDocument(), SpringContext.getBean(DocumentService.class));
    }

    @Test
    public void testConvertIntoCopy() throws Exception {
        GlobalVariables.getMessageMap().clearErrorMessages();
        AccountingDocumentTestUtils.testConvertIntoCopy(buildDocument(), SpringContext.getBean(DocumentService.class), getExpectedPrePeCount());
    }

    // test util mehtods
    private List<SourceAccountingLine> generateSouceAccountingLines() throws Exception {
        List<SourceAccountingLine> sourceLines = new ArrayList<SourceAccountingLine>();
        // set accountinglines to document
        for (AccountingLineFixture sourceFixture : getSourceAccountingLineParametersFromFixtures()) {
            sourceLines.add(sourceFixture.createSourceAccountingLine());
            sourceLines.add(sourceFixture.createAccountingLine(SourceAccountingLine.class, GL_DEBIT_CODE.equals(sourceFixture.debitCreditCode) ? GL_CREDIT_CODE : GL_DEBIT_CODE));
        }

        return sourceLines;
    }

}

