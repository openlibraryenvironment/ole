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
package org.kuali.rice.kew.mail;

import org.junit.Ignore;
import org.junit.Test;
import org.kuali.rice.core.api.mail.EmailContent;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.kew.actionitem.ActionItemActionListExtension;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.action.ActionItem;
import org.kuali.rice.kew.mail.service.impl.ActionListEmailServiceImpl;
import org.kuali.rice.kew.mail.service.impl.StyleableEmailContentServiceImpl;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Tests email content generation
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Ignore
public class EmailMessageTest extends KEWTestCase {
    private ActionListEmailServiceImpl actionListEmailService = new ActionListEmailServiceImpl();
    private StyleableEmailContentServiceImpl styleableContentService = new StyleableEmailContentServiceImpl();

    @Override
    public void setUp() throws Exception {
        super.setUp();
        actionListEmailService.setDeploymentEnvironment("dev");
       // hardCodedEmailContentService.setDeploymentEnvironment("dev");
        styleableContentService.setDeploymentEnvironment("dev");
        styleableContentService.setStyleService(CoreServiceApiServiceLocator.getStyleService());
    }

    @Override
    protected void loadTestData() throws Exception {
        loadXmlFile("EmailMessageDocType.xml");
    }

    private int generateDocs(String[] docTypes, Person user) throws Exception {
        String nid = getPrincipalNameForId(user.getPrincipalName());

        for (String docType: docTypes) {
            WorkflowDocument document = WorkflowDocumentFactory.createDocument(nid, docType);
            document.setTitle("a title");
            document.route("");
            document = WorkflowDocumentFactory.createDocument(nid, docType);
            document.setTitle("a title");
            document.route("");
            document = WorkflowDocumentFactory.createDocument(nid, docType);
            document.setTitle("a title");
            document.route("");
            document = WorkflowDocumentFactory.createDocument(nid, docType);
            document.setTitle("a title");
            document.route("");
            document = WorkflowDocumentFactory.createDocument(nid, docType);
            document.setTitle("a title");
            document.route("");
        }

        return 5 * docTypes.length;
    }

    /**
     * tests custom stylesheet
     * @throws Exception
     */
    @Test
    public void testGenerateRemindersCustomStyleSheet() throws Exception {
        loadXmlFile("customEmailStyleData.xml");
        assertNotNull(CoreServiceApiServiceLocator.getStyleService().getStyle("kew.email.style"));

        Person user = KimApiServiceLocator.getPersonService().getPersonByPrincipalName("arh14");
        int count = generateDocs(new String[] { "PingDocument", "PingDocumentWithEmailAttrib" }, user);

        Collection<ActionItem> actionItems = org.kuali.rice.kew.actionitem.ActionItem.to(new ArrayList<org.kuali.rice.kew.actionitem.ActionItem>(KEWServiceLocator.getActionListService().getActionList(user.getPrincipalId(), null)));
        assertEquals("user should have " + count + " items in his action list.", count, actionItems.size());

        EmailContent content = styleableContentService.generateImmediateReminder(user, actionItems.iterator().next(), KEWServiceLocator.getDocumentTypeService().findByName(actionItems.iterator().next().getDocName()));
        assertTrue("Unexpected subject", content.getSubject().startsWith("CUSTOM:"));
        assertTrue("Unexpected body", content.getBody().startsWith("CUSTOM:"));

        content = styleableContentService.generateDailyReminder(user, actionItems);
        assertTrue("Unexpected subject", content.getSubject().startsWith("CUSTOM:"));
        assertTrue("Unexpected body", content.getBody().startsWith("CUSTOM:"));

        content = styleableContentService.generateWeeklyReminder(user, actionItems);
        assertTrue("Unexpected subject", content.getSubject().startsWith("CUSTOM:"));
        assertTrue("Unexpected body", content.getBody().startsWith("CUSTOM:"));
    }

    /**
     * tests custom stylesheet
     * @throws Exception
     */
    @Test
    public void testGenerateRemindersDocCustomStyleSheet() throws Exception {
        // we need to make sure that the immediate email message is customized on a per-doc basis
        // so we need to easily distinguish from the global style and the custom style
        // an easy way to do that is use two styles that have introduced obvious and blatent
        // distinguishing marker...so we just reuse the global custom email style here
        loadXmlFile("customEmailStyleData.xml");
        loadXmlFile("docCustomEmailStyleData.xml");
        assertNotNull(CoreServiceApiServiceLocator.getStyleService().getStyle("kew.email.style"));
        assertNotNull(CoreServiceApiServiceLocator.getStyleService().getStyle("doc.custom.email.style"));

        Person user = KimApiServiceLocator.getPersonService().getPersonByPrincipalName("arh14");
        int count = generateDocs(new String[] { "PingDocumentCustomStyle" }, user);

        Collection<ActionItem> actionItems = org.kuali.rice.kew.actionitem.ActionItem.to(new ArrayList<org.kuali.rice.kew.actionitem.ActionItem>(KEWServiceLocator.getActionListService().getActionList(user.getPrincipalId(), null)));
        assertEquals("user should have " + count + " items in his action list.", count, actionItems.size());

        EmailContent content = styleableContentService.generateImmediateReminder(user, actionItems.iterator().next(), KEWServiceLocator.getDocumentTypeService().findByName(actionItems.iterator().next().getDocName()));
        // immediate email reminder should have used the doc type email style and NOT the global style
        assertFalse("Unexpected subject", content.getSubject().startsWith("CUSTOM:"));
        assertFalse("Unexpected body", content.getBody().startsWith("CUSTOM:"));
        assertTrue("Unexpected subject", content.getSubject().startsWith("DOCTYPE CUSTOM:"));
        assertTrue("Unexpected body", content.getBody().startsWith("DOCTYPE CUSTOM:"));


        // daily and weekly are unchanged since they are not document type specific
        content = styleableContentService.generateDailyReminder(user, actionItems);
        assertTrue("Unexpected subject", content.getSubject().startsWith("CUSTOM:"));
        assertTrue("Unexpected body", content.getBody().startsWith("CUSTOM:"));

        content = styleableContentService.generateWeeklyReminder(user, actionItems);
        assertTrue("Unexpected subject", content.getSubject().startsWith("CUSTOM:"));
        assertTrue("Unexpected body", content.getBody().startsWith("CUSTOM:"));
    }

    /**
     * tests loading a custom stylesheet that has entities that causes XPath to get confused down the ingestion pipeline...
     * @throws Exception
     */
    @Test
    public void testBadCustomStyleSheet() throws Exception {
    	try {
    		loadXmlFile("badCustomEmailStyleData.xml");
    		fail("Loading of badCustomEmailStyleData.xml should have failed!");
    	} catch (Exception e) {}
        // this doesn't get loaded
        assertNull(CoreServiceApiServiceLocator.getStyleService().getStyle("bad.kew.email.style"));
    }
}
