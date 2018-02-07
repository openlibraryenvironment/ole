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
package org.kuali.rice.kew.server;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.kuali.rice.core.api.delegation.DelegationType;
import org.kuali.rice.kew.actionitem.ActionItem;
import org.kuali.rice.kew.api.document.DocumentContentUpdate;
import org.kuali.rice.kew.api.document.attribute.WorkflowAttributeDefinition;
import org.kuali.rice.kew.dto.DTOConverter;
import org.kuali.rice.kew.rule.TestRuleAttribute;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;

import java.sql.Timestamp;
import java.util.Date;

import static org.junit.Assert.*;

public class DTOConverterTest extends KEWTestCase {

    private static final String DOCUMENT_CONTENT = KewApiConstants.DOCUMENT_CONTENT_ELEMENT;
    private static final String ATTRIBUTE_CONTENT = KewApiConstants.ATTRIBUTE_CONTENT_ELEMENT;
    private static final String SEARCHABLE_CONTENT = KewApiConstants.SEARCHABLE_CONTENT_ELEMENT;
    private static final String APPLICATION_CONTENT = KewApiConstants.APPLICATION_CONTENT_ELEMENT;

    /**
     * Tests the conversion of a String into a DocumentContentVO object which should split the
     * String into it's 3 distinct components.
     */
/*    @Test public void testConvertDocumentContent() throws Exception {

        // test null content
        String attributeContent = null;
        String searchableContent = null;
        String applicationContent = null;
        String xmlContent = constructContent(attributeContent, searchableContent, applicationContent);
        DocumentContentDTO contentVO = DTOConverter.convertDocumentContent(xmlContent, "-1234");
        assertFalse("Content cannot be empty.", org.apache.commons.lang.StringUtils.isEmpty(contentVO.getFullContent()));
        assertEquals("Attribute content is invalid.", "", contentVO.getAttributeContent());
        assertEquals("Searchable content is invalid.", "", contentVO.getSearchableContent());
        assertEquals("Application content is invalid.", "", contentVO.getApplicationContent());
        assertEquals("Should have fake document id.", "-1234", contentVO.getDocumentId());

        // test empty content
        attributeContent = "";
        searchableContent = "";
        applicationContent = "";
        contentVO = DTOConverter.convertDocumentContent(constructContent(attributeContent, searchableContent, applicationContent), null);
        assertContent(contentVO, attributeContent, searchableContent, applicationContent);

        // test fancy dancy content
        attributeContent = "<iEnjoyFlexContent><id>1234</id></iEnjoyFlexContent>";
        searchableContent = "<thisIdBeWarrenG>Warren G</thisIdBeWarrenG><whatsMyName>Snoop</whatsMyName>";
        applicationContent = "<thisIsTotallyRad><theCoolestContentInTheWorld qualify=\"iSaidSo\">it's marvelous!</theCoolestContentInTheWorld></thisIsTotallyRad>";
        contentVO = DTOConverter.convertDocumentContent(constructContent(attributeContent, searchableContent, applicationContent), null);
        assertContent(contentVO, attributeContent, searchableContent, applicationContent);

        attributeContent = "invalid<xml, I can't believe you would do such a thing<<<";
        try {
            contentVO = DTOConverter.convertDocumentContent(constructContent(attributeContent, searchableContent, applicationContent), null);
            fail("Parsing bad xml should have thrown an XmlException.");
        } catch (InvalidDocumentContentException e) {
            log.info("Expected XmlException was thrown.");
            // if we got the exception we are good to go
        }

        // test an older style document
        String appSpecificXml = "<iAmAnOldSchoolApp><myDocContent type=\"custom\">is totally app specific</myDocContent><howIroll>old school, that's how I roll</howIroll></iAmAnOldSchoolApp>";
        contentVO = DTOConverter.convertDocumentContent(appSpecificXml, null);
        assertContent(contentVO, "", "", appSpecificXml);

        // test the old school (Workflow 1.6) flex document XML
        String fleXml = "<flexdoc><meinAttribute>nein</meinAttribute></flexdoc>";
        contentVO = DTOConverter.convertDocumentContent(fleXml, null);
        assertFalse("Content cannot be empty.", org.apache.commons.lang.StringUtils.isEmpty(contentVO.getFullContent()));
        assertEquals("Attribute content is invalid.", StringUtils.deleteWhitespace(fleXml), StringUtils.deleteWhitespace(contentVO.getAttributeContent()));
        assertEquals("Searchable content is invalid.", "", StringUtils.deleteWhitespace(contentVO.getSearchableContent()));
        assertEquals("Application content is invalid.", "", StringUtils.deleteWhitespace(contentVO.getApplicationContent()));
    }*/

    /**
     * Tests the conversion of a DocumentContentVO object into an XML String.  Includes generating content
     * for any attributes which are on the DocumentContentVO object.
     *
     * TODO there is some crossover between this test and the DocumentContentTest, do we really need both of them???
     */
    @Test public void testBuildUpdatedDocumentContent() throws Exception {
        String startContent = "<"+DOCUMENT_CONTENT+">";
        String endContent = "</"+DOCUMENT_CONTENT+">";

        /*
         * 	// test no content, this should return null which indicates an unchanged document content VO
         * //RouteHeaderVO routeHeaderVO = new RouteHeaderVO();
         */

        // test no content, this should return empty document content
        DocumentContentUpdate contentUpdate = DocumentContentUpdate.Builder.create().build();
        //routeHeaderVO.setDocumentContent(contentVO);
        String content = DTOConverter.buildUpdatedDocumentContent(KewApiConstants.DEFAULT_DOCUMENT_CONTENT, contentUpdate,
                null);
        assertEquals("Invalid content conversion.", StringUtils.deleteWhitespace(KewApiConstants.DEFAULT_DOCUMENT_CONTENT), StringUtils.deleteWhitespace(content));

        // test simple case, no attributes
        String attributeContent = "<attribute1><id value=\"3\"/></attribute1>";
        String searchableContent = "<searchable1><data>hello</data></searchable1>";
        DocumentContentUpdate.Builder contentUpdateBuilder = DocumentContentUpdate.Builder.create();
        contentUpdateBuilder.setAttributeContent(constructContent(ATTRIBUTE_CONTENT, attributeContent));
        contentUpdateBuilder.setSearchableContent(constructContent(SEARCHABLE_CONTENT, searchableContent));
        content = DTOConverter.buildUpdatedDocumentContent(KewApiConstants.DEFAULT_DOCUMENT_CONTENT, contentUpdateBuilder.build(), null);
        String fullContent = startContent+"\n"+constructContent(ATTRIBUTE_CONTENT, attributeContent)+"\n"+constructContent(SEARCHABLE_CONTENT, searchableContent)+"\n"+endContent;
        assertEquals("Invalid content conversion.", StringUtils.deleteWhitespace(fullContent), StringUtils.deleteWhitespace(content));

        // now, add an attribute
        String testAttributeContent = new TestRuleAttribute().getDocContent();
        WorkflowAttributeDefinition attributeDefinition = WorkflowAttributeDefinition.Builder.create("TestRuleAttribute").build();
        contentUpdateBuilder.getAttributeDefinitions().add(attributeDefinition);
        content = DTOConverter.buildUpdatedDocumentContent(KewApiConstants.DEFAULT_DOCUMENT_CONTENT, contentUpdateBuilder.build(), null);
        fullContent = startContent+
            constructContent(ATTRIBUTE_CONTENT, attributeContent+testAttributeContent)+
            constructContent(SEARCHABLE_CONTENT, searchableContent)+
            endContent;
        assertEquals("Invalid content conversion.", StringUtils.deleteWhitespace(fullContent), StringUtils.deleteWhitespace(content));
    }

    private String constructContent(String type, String content) {
        if (org.apache.commons.lang.StringUtils.isEmpty(content)) {
            return "";
        }
        return "<"+type+">"+content+"</"+type+">";
    }

    private String constructContent(String attributeContent, String searchableContent, String applicationContent) {
        return "<"+DOCUMENT_CONTENT+">"+
            constructContent(ATTRIBUTE_CONTENT, attributeContent)+
            constructContent(SEARCHABLE_CONTENT, searchableContent)+
            constructContent(APPLICATION_CONTENT, applicationContent)+
            "</"+DOCUMENT_CONTENT+">";
    }

    /*private void assertContent(DocumentContentDTO contentVO, String attributeContent, String searchableContent, String applicationContent) {
        if (org.apache.commons.lang.StringUtils.isEmpty(attributeContent)) {
        	attributeContent = "";
        } else {
            attributeContent = "<"+ATTRIBUTE_CONTENT+">"+attributeContent+"</"+ATTRIBUTE_CONTENT+">";
        }
        if (org.apache.commons.lang.StringUtils.isEmpty(searchableContent)) {
        	searchableContent = "";
        } else {
            searchableContent = "<"+SEARCHABLE_CONTENT+">"+searchableContent+"</"+SEARCHABLE_CONTENT+">";
        }
        assertFalse("Content cannot be empty.", org.apache.commons.lang.StringUtils.isEmpty(contentVO.getFullContent()));
        assertEquals("Attribute content is invalid.", StringUtils.deleteWhitespace(attributeContent), StringUtils.deleteWhitespace(contentVO.getAttributeContent()));
        assertEquals("Searchable content is invalid.", StringUtils.deleteWhitespace(searchableContent), StringUtils.deleteWhitespace(contentVO.getSearchableContent()));
        assertEquals("Application content is invalid.", StringUtils.deleteWhitespace(applicationContent), StringUtils.deleteWhitespace(contentVO.getApplicationContent()));
        assertEquals("Incorrect number of attribute definitions.", 0, contentVO.getAttributeDefinitions().length);
        assertEquals("Incorrect number of searchable attribute definitions.", 0, contentVO.getSearchableDefinitions().length);
    }*/

    @Test public void testConvertActionItem() throws Exception {
        // get test data
        String testWorkgroupName = "TestWorkgroup";
        Group testWorkgroup = KimApiServiceLocator.getGroupService().getGroupByNamespaceCodeAndName(
                KimConstants.KIM_GROUP_WORKFLOW_NAMESPACE_CODE, testWorkgroupName);
        String testWorkgroupId = testWorkgroup.getId();
        assertTrue("Test workgroup '" + testWorkgroupName + "' should have at least one user", KimApiServiceLocator.getGroupService().getDirectMemberPrincipalIds(
                testWorkgroup.getId()).size() > 0);
        String workflowId = KimApiServiceLocator.getGroupService().getDirectMemberPrincipalIds(testWorkgroup.getId()).get(0);
        assertNotNull("User from workgroup should not be null", workflowId);
        String actionRequestCd = KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ;
        String actionRequestId = "4";
        String docName = "dummy";
        String roleName = "fakeRole";
        String documentId = "23";
        Timestamp dateAssigned = new Timestamp(new Date().getTime());
        String docHandlerUrl = "http://this.is.not.us";
        String docTypeLabel = "Label Me";
        String docTitle = "Title me";
        String responsibilityId = "35";
        DelegationType delegationType = DelegationType.PRIMARY;

        // create fake action item
        ActionItem actionItem = new ActionItem();
        actionItem.setActionRequestCd(actionRequestCd);
        actionItem.setActionRequestId(actionRequestId);
        actionItem.setDocName(docName);
        actionItem.setRoleName(roleName);
        actionItem.setPrincipalId(workflowId);
        actionItem.setDocumentId(documentId);
        actionItem.setDateAssigned(dateAssigned);
        actionItem.setDocHandlerURL(docHandlerUrl);
        actionItem.setDocLabel(docTypeLabel);
        actionItem.setDocTitle(docTitle);
        actionItem.setGroupId(testWorkgroupId);
        actionItem.setResponsibilityId(responsibilityId);
        actionItem.setDelegationType(delegationType);
        actionItem.setDelegatorPrincipalId(workflowId);
        actionItem.setDelegatorGroupId(testWorkgroupId);

        // convert to action item vo object and verify
        org.kuali.rice.kew.api.action.ActionItem actionItemVO = ActionItem.to(actionItem);
        assertEquals("Action Item VO object has incorrect value", actionRequestCd, actionItemVO.getActionRequestCd());
        assertEquals("Action Item VO object has incorrect value", actionRequestId, actionItemVO.getActionRequestId());
        assertEquals("Action Item VO object has incorrect value", docName, actionItemVO.getDocName());
        assertEquals("Action Item VO object has incorrect value", roleName, actionItemVO.getRoleName());
        assertEquals("Action Item VO object has incorrect value", workflowId, actionItemVO.getPrincipalId());
        assertEquals("Action Item VO object has incorrect value", documentId, actionItemVO.getDocumentId());
        assertEquals("Action Item VO object has incorrect value", new DateTime(dateAssigned.getTime()), actionItemVO.getDateTimeAssigned());
        assertEquals("Action Item VO object has incorrect value", docHandlerUrl, actionItemVO.getDocHandlerURL());
        assertEquals("Action Item VO object has incorrect value", docTypeLabel, actionItemVO.getDocLabel());
        assertEquals("Action Item VO object has incorrect value", docTitle, actionItemVO.getDocTitle());
        assertEquals("Action Item VO object has incorrect value", "" + testWorkgroupId, actionItemVO.getGroupId());
        assertEquals("Action Item VO object has incorrect value", responsibilityId, actionItemVO.getResponsibilityId());
        assertEquals("Action Item VO object has incorrect value", delegationType, actionItemVO.getDelegationType());
        assertEquals("Action Item VO object has incorrect value", workflowId, actionItemVO.getDelegatorPrincipalId());
        assertEquals("Action Item VO object has incorrect value", testWorkgroupId, actionItemVO.getDelegatorGroupId());
    }

    /*@Test public void testConvertActionRequest() throws Exception {
    	ActionRequestValue actionRequest = new ActionRequestValue();
    	ActionTakenValue actionTaken = new ActionTakenValue();
    	actionRequest.setActionTaken(actionTaken);
    	actionTaken.getActionRequests().add(actionRequest);
    	
    	ActionRequestDTO convertedActionRequest = DTOConverter.convertActionRequest(actionRequest);
    	assertNotNull("ActionTakenDTO object should be valid", convertedActionRequest);

    }

    @Test public void testConvertActionTaken() throws Exception {
    	ActionRequestValue actionRequest = new ActionRequestValue();
    	ActionTakenValue actionTaken = new ActionTakenValue();
    	actionRequest.setActionTaken(actionTaken);
    	actionTaken.getActionRequests().add(actionRequest);
    	
    	ActionTakenDTO convertedActionTaken = DTOConverter.convertActionTaken(actionTaken);
    	assertNotNull("ActionTakenDTO object should be valid", convertedActionTaken);
    	convertedActionTaken = DTOConverter.convertActionTakenWithActionRequests(actionTaken);
    	assertNotNull("ActionTakenDTO object should be valid", convertedActionTaken);
    }*/
}
