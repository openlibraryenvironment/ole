/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.krad.service;

import org.junit.Test;
import org.kuali.rice.krad.rules.rule.event.ApproveDocumentEvent;
import org.kuali.rice.krad.test.KRADTestCase;

import static org.junit.Assert.assertTrue;

/**
 * KualiRuleServiceTest tests {@link KualiRuleService}
 * <p>
 * Testing applyRules( event ) has proven to be too strongly dependent on an actual document type, and business rule, and XML
 * document which binds them together, to be really useful. Instead, we'll test each of the applyRules( rule, event ) methods which
 * are called by applyRules( event ), since they do the actual work involved in applying a rule.</p>
 */
public class KualiRuleServiceTest extends KRADTestCase {
    private static boolean entriesAdded = false;
    private static KualiRuleService kualiRuleService;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        if (!entriesAdded) {
            entriesAdded = true;
            kualiRuleService = KRADServiceLocatorWeb.getKualiRuleService();
        }
    }

    @Test public void testApplyRules_approveDocument_nullEvent() {
        boolean failedAsExpected = false;

        ApproveDocumentEvent event = null;
        try {
            kualiRuleService.applyRules(event);
        }
        catch (IllegalArgumentException e) {
            failedAsExpected = true;
        }

        assertTrue(failedAsExpected);
    }

    @Test public void testApplyRules_approveDocument_nullDocument() {
        boolean failedAsExpected = false;

        try {
            ApproveDocumentEvent event = new ApproveDocumentEvent(null);

            kualiRuleService.applyRules(event);
        }
        catch (RuntimeException e) {
            failedAsExpected = true;
        }

        assertTrue(failedAsExpected);
    }

//    private static final String NONE = "none";
//    private static final String ROUTE_DOCUMENT = "routeDoc";
//    private static final String APPROVE_DOCUMENT = "approveDoc";
//    private static final String SAVE_DOCUMENT = "saveDoc";
//    private static final String ADD_LINE = "addLine";
//    private static final String DELETE_LINE = "deleteLine";
//    private static final String UPDATE_LINE = "updateLine";
//    private static final String REVIEW_LINE = "reviewLine";
//
//    @Test public void testApplyRules_approveDocument_validRule_validDocument() {
//        RuleMockDocument d = new RuleMockDocument();
//        ApproveDocumentEvent event = new ApproveDocumentEvent(d);
//
//
//        assertFalse(d.isProcessed());
//        assertEquals(NONE, d.getEventType());
//
//        boolean success = kualiRuleService.applyRules(event);
//        assertTrue(success);
//
//        assertTrue(d.isProcessed());
//        assertEquals(ROUTE_DOCUMENT, d.getPrevEventType());
//        assertEquals(APPROVE_DOCUMENT, d.getEventType());
//    }
//
//    @Test public void testApplyRules_approveDocument_validRule_validTransactionalDocument_noAccountingLines() {
//        RuleMockDocument t = new RuleMockDocument();
//        ApproveDocumentEvent event = new ApproveDocumentEvent(t);
//
//
//        assertFalse(t.isProcessed());
//        assertEquals(NONE, t.getEventType());
//
//        boolean success = kualiRuleService.applyRules(event);
//        assertTrue(success);
//
//        assertTrue(t.isProcessed());
//        assertEquals(ROUTE_DOCUMENT, t.getPrevEventType());
//        assertEquals(APPROVE_DOCUMENT, t.getEventType());
//    }
//
//    @Test public void testApplyRules_routeDocument_validRule_validDocument() {
//        RuleMockDocument d = new RuleMockDocument();
//        RouteDocumentEvent event = new RouteDocumentEvent(d);
//
//
//        assertFalse(d.isProcessed());
//        assertEquals(NONE, d.getEventType());
//
//        boolean success = kualiRuleService.applyRules(event);
//        assertTrue(success);
//
//        assertTrue(d.isProcessed());
//        assertEquals(SAVE_DOCUMENT, d.getPrevEventType());
//        assertEquals(ROUTE_DOCUMENT, d.getEventType());
//    }
//
//    @Test public void testApplyRules_routeDocument_validRule_validTransactionalDocument_noAccountingLines() {
//        RuleMockDocument t = new RuleMockDocument();
//        RouteDocumentEvent event = new RouteDocumentEvent(t);
//
//
//        assertFalse(t.isProcessed());
//        assertEquals(NONE, t.getEventType());
//
//        boolean success = kualiRuleService.applyRules(event);
//        assertTrue(success);
//
//        assertTrue(t.isProcessed());
//        assertEquals(SAVE_DOCUMENT, t.getPrevEventType());
//        assertEquals(ROUTE_DOCUMENT, t.getEventType());
//    }
//
//    @Test public void testApplyRules_routeDocument_validRule_validTransactionalDocument_someAccountingLines() throws Exception {
//        RuleMockDocument t = new RuleMockDocument();
//        t.addSourceAccountingLine(buildSourceLine("1"));
//
//        RouteDocumentEvent event = new RouteDocumentEvent(t);
//
//
//        assertFalse(t.isProcessed());
//        assertEquals(NONE, t.getEventType());
//
//        boolean success = kualiRuleService.applyRules(event);
//        assertTrue(success);
//
//        assertTrue(t.isProcessed());
//        assertEquals(SAVE_DOCUMENT, t.getPrevEventType());
//        assertEquals(ROUTE_DOCUMENT, t.getEventType());
//    }
//
//    @Test public void testApplyRules_saveDocument_validRule_validDocument() {
//        RuleMockDocument d = new RuleMockDocument();
//        SaveDocumentEvent event = new SaveDocumentEvent(d);
//
//
//        assertFalse(d.isProcessed());
//        assertEquals(NONE, d.getEventType());
//
//        boolean success = kualiRuleService.applyRules(event);
//        assertTrue(success);
//
//        assertTrue(d.isProcessed());
//        assertEquals(NONE, d.getPrevEventType());
//        assertEquals(SAVE_DOCUMENT, d.getEventType());
//    }
//
//    @Test public void testApplyRules_saveDocument_validRule_validTransactionalDocument_noAccountingLines() {
//        RuleMockDocument t = new RuleMockDocument();
//        SaveDocumentEvent event = new SaveDocumentEvent(t);
//
//
//        assertFalse(t.isProcessed());
//        assertEquals(NONE, t.getEventType());
//
//        boolean success = kualiRuleService.applyRules(event);
//        assertTrue(success);
//
//        assertTrue(t.isProcessed());
//        assertEquals(NONE, t.getPrevEventType());
//        assertEquals(SAVE_DOCUMENT, t.getEventType());
//    }
//
//    @Test public void testApplyRules_saveDocument_validRule_validTransactionalDocument_someAccountingLines() throws Exception {
//        RuleMockDocument t = new RuleMockDocument();
//        t.addSourceAccountingLine(buildSourceLine("1"));
//
//        SaveDocumentEvent event = new SaveDocumentEvent(t);
//
//
//        assertFalse(t.isProcessed());
//        assertEquals(NONE, t.getEventType());
//
//        boolean success = kualiRuleService.applyRules(event);
//        assertTrue(success);
//
//        assertTrue(t.isProcessed());
//        assertEquals(ADD_LINE, t.getPrevEventType());
//        assertEquals(SAVE_DOCUMENT, t.getEventType());
//    }
//
//    @Test public void testApplyRules_addAccountingLine_validRule_nullDocument() {
//        boolean failedAsExpected = false;
//
//        AddAccountingLineEvent event = new AddAccountingLineEvent("test", null, null);
//        try {
//            kualiRuleService.applyRules(event);
//        }
//        catch (IllegalArgumentException e) {
//            failedAsExpected = true;
//        }
//
//        assertTrue(failedAsExpected);
//    }
//
//    @Test public void testApplyRules_addAccountingLine_validRule_validTransactionalDocument_nullAccountingLine() {
//        boolean failedAsExpected = false;
//
//        AddAccountingLineEvent event = new AddAccountingLineEvent("test", new RuleMockDocument(), null);
//        try {
//            kualiRuleService.applyRules(event);
//        }
//        catch (IllegalArgumentException e) {
//            failedAsExpected = true;
//        }
//
//        assertTrue(failedAsExpected);
//    }
//
//    @Test public void testApplyRules_addAccountingLine_validRule_validTransactionalDocument_validAccountingLine() throws Exception {
//        RuleMockDocument t = new RuleMockDocument();
//        SourceAccountingLine s = buildSourceLine("1");
//        t.addSourceAccountingLine(s);
//
//        AddAccountingLineEvent event = new AddAccountingLineEvent("test", t, s);
//
//        assertFalse(t.isProcessed());
//
//        boolean success = kualiRuleService.applyRules(event);
//        assertTrue(success);
//
//        assertTrue(t.isProcessed());
//        assertEquals(NONE, t.getPrevEventType());
//        assertEquals(ADD_LINE, t.getEventType());
//    }
//
//    @Test public void testApplyRules_deleteAccountingLine_validRule_nullDocument() {
//        boolean failedAsExpected = false;
//
//        DeleteAccountingLineEvent event = new DeleteAccountingLineEvent("test", null, null, false);
//        try {
//            kualiRuleService.applyRules(event);
//        }
//        catch (IllegalArgumentException e) {
//            failedAsExpected = true;
//        }
//
//        assertTrue(failedAsExpected);
//    }
//
//    @Test public void testApplyRules_deleteAccountingLine_validRule_validTransactionalDocument_nullAccountingLine() {
//        boolean failedAsExpected = false;
//
//        DeleteAccountingLineEvent event = new DeleteAccountingLineEvent("test", new RuleMockDocument(), null, false);
//        try {
//            kualiRuleService.applyRules(event);
//        }
//        catch (IllegalArgumentException e) {
//            failedAsExpected = true;
//        }
//
//        assertTrue(failedAsExpected);
//    }
//
//    @Test public void testApplyRules_deleteAccountingLine_validRule_validTransactionalDocument_validAccountingLine() throws Exception {
//        RuleMockDocument t = new RuleMockDocument();
//        SourceAccountingLine s = buildSourceLine("1");
//        t.addSourceAccountingLine(s);
//
//        DeleteAccountingLineEvent event = new DeleteAccountingLineEvent("test", t, s, false);
//
//        assertFalse(t.isProcessed());
//
//        boolean success = kualiRuleService.applyRules(event);
//        assertTrue(success);
//
//        assertTrue(t.isProcessed());
//        assertEquals(NONE, t.getPrevEventType());
//        assertEquals(DELETE_LINE, t.getEventType());
//    }
//
//
//    @Test public void testApplyRules_updateAccountingLine_validRule_validTransactionalDocument_nullAccountingLine() {
//        boolean failedAsExpected = false;
//
//        UpdateAccountingLineEvent event = new UpdateAccountingLineEvent("test", new RuleMockDocument(), null, null);
//        try {
//            kualiRuleService.applyRules(event);
//        }
//        catch (IllegalArgumentException e) {
//            failedAsExpected = true;
//        }
//
//        assertTrue(failedAsExpected);
//    }
//
//    @Test public void testApplyRules_updateAccountingLine_validRule_validTransactionalDocument_validAccountingLine_nullUpdatedAccountingLine() throws Exception {
//        boolean failedAsExpected = false;
//
//        RuleMockDocument t = new RuleMockDocument();
//        SourceAccountingLine s = buildSourceLine("1");
//        t.addSourceAccountingLine(s);
//
//        UpdateAccountingLineEvent event = new UpdateAccountingLineEvent("test", t, s, null);
//
//        try {
//            kualiRuleService.applyRules(event);
//        }
//        catch (IllegalArgumentException e) {
//            failedAsExpected = true;
//        }
//
//        assertTrue(failedAsExpected);
//    }
//
//    @Test public void testApplyRules_updateAccountingLine_validRule_validTransactionalDocument_validAccountingLines() throws Exception {
//        RuleMockDocument t = new RuleMockDocument();
//        SourceAccountingLine s = buildSourceLine("1");
//        s.setOrganizationReferenceId("A");
//        t.addSourceAccountingLine(s);
//
//        SourceAccountingLine updatedS = (SourceAccountingLine) ObjectUtils.deepCopy(t.getSourceAccountingLine(0));
//        updatedS.setOrganizationReferenceId("B");
//
//        UpdateAccountingLineEvent event = new UpdateAccountingLineEvent("test", t, s, updatedS);
//
//        assertFalse(t.isProcessed());
//
//        boolean success = kualiRuleService.applyRules(event);
//        assertTrue(success);
//
//        assertTrue(t.isProcessed());
//        assertEquals(NONE, t.getPrevEventType());
//        assertEquals(UPDATE_LINE, t.getEventType());
//    }
//
//    @Test public void testApplyRules_reviewAccountingLine_validRule_validTransactionalDocument_nullAccountingLine() {
//        boolean failedAsExpected = false;
//
//        ReviewAccountingLineEvent event = new ReviewAccountingLineEvent("test", new RuleMockDocument(), null);
//        try {
//            kualiRuleService.applyRules(event);
//        }
//        catch (IllegalArgumentException e) {
//            failedAsExpected = true;
//        }
//
//        assertTrue(failedAsExpected);
//    }
//
//    @Test public void testApplyRules_reviewAccountingLine_validRule_validTransactionalDocument_validAccountingLine() throws Exception {
//        RuleMockDocument t = new RuleMockDocument();
//        SourceAccountingLine s = buildSourceLine("1");
//        t.addSourceAccountingLine(s);
//
//        ReviewAccountingLineEvent event = new ReviewAccountingLineEvent("test", t, s);
//
//        assertFalse(t.isProcessed());
//
//        boolean success = kualiRuleService.applyRules(event);
//        assertTrue(success);
//
//        assertTrue(t.isProcessed());
//        assertEquals(NONE, t.getPrevEventType());
//        assertEquals(REVIEW_LINE, t.getEventType());
//    }
//
//    // TODO: commenting thse out for now, need to analyze and see if it still makes sense with new notes"copied from document " +
//    // sourceDocumentHeaderId);
//    @Test public void testApplyRules_addDocumentNote_validRule_nullDocument_nullDocumentNote() {
//        // boolean failedAsExpected = false;
//        //
//        // AddDocumentNoteEvent event = new AddDocumentNoteEvent(null, null);
//        // try {
//        // kualiRuleService.applyRules(event);
//        // }
//        // catch (IllegalArgumentException e) {
//        // failedAsExpected = true;
//        // }
//        //
//        // assertTrue(failedAsExpected);
//    }
//
//    @Test public void testApplyRules_addDocumentNote_validRule_validDocument_nullDocumentNote() {
//        // boolean failedAsExpected = false;
//        //
//        // MockTransactionalDocument d = new MockTransactionalDocument();
//        // DocumentNote n = null;
//        //
//        // AddDocumentNoteEvent event = new AddDocumentNoteEvent(d, n);
//        //
//        // try {
//        // kualiRuleService.applyRules(event);
//        // }
//        // catch (IllegalArgumentException e) {
//        // failedAsExpected = true;
//        // }
//        //
//        // assertTrue(failedAsExpected);
//    }
//
//    @Test public void testApplyRules_addDocumentNote_validRule_validDocument_validDocumentNote() {
//        // MockTransactionalDocument d = new MockTransactionalDocument();
//        // DocumentNote n = new DocumentNote();
//        // n.setFinancialDocumentNoteText("Test");
//        // d.getDocumentHeader().getNotes().add(n);
//        //
//        // AddDocumentNoteEvent event = new AddDocumentNoteEvent(d, n);
//        //
//        // assertFalse(d.isProcessed());
//        // assertTrue(kualiRuleService.applyRules(event));
//        // assertTrue(d.isProcessed());
//        // assertEquals(NONE, d.getPrevEventType());
//        // assertEquals(ADD_DOC_NOTE, d.getEventType());
//    }
//
//    private SourceAccountingLine buildSourceLine(String documentHeaderId) throws InstantiationException, IllegalAccessException {
//        SourceAccountingLine sourceLine = LINE.createSourceAccountingLine();
//        return sourceLine;
//
//    }
}
