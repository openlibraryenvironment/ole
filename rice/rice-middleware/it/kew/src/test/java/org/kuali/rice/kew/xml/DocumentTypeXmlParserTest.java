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
package org.kuali.rice.kew.xml;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kuali.rice.core.api.util.xml.XmlException;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.action.ActionType;
import org.kuali.rice.kew.doctype.ApplicationDocumentStatus;
import org.kuali.rice.kew.doctype.ApplicationDocumentStatusCategory;
import org.kuali.rice.kew.doctype.DocumentTypeAttributeBo;
import org.kuali.rice.kew.doctype.DocumentTypePolicy;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.engine.node.ProcessDefinitionBo;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.krad.exception.GroupNotFoundException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class DocumentTypeXmlParserTest extends KEWTestCase {

    private static String applicationStatusDocumentTypeTemplate;

    @BeforeClass
    public static void beforeClass() throws Exception {
        applicationStatusDocumentTypeTemplate =
                IOUtils.toString(DocumentTypeXmlParserTest.class.getResourceAsStream("BadKEWAppDocStatusTemplate.xml"));
    }


    private boolean validate(String docName) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(true);
        dbf.setNamespaceAware( true );
        dbf.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", XMLConstants.W3C_XML_SCHEMA_NS_URI);
        DocumentBuilder db = dbf.newDocumentBuilder();
        db.setEntityResolver(new org.kuali.rice.core.impl.impex.xml.ClassLoaderEntityResolver());
        db.setErrorHandler(new DefaultHandler() {
            @Override
            public void error(SAXParseException e) throws SAXException {
                this.fatalError(e);
            }
            @Override
            public void fatalError(SAXParseException e) throws SAXException {
                super.fatalError(e);
            }
        });
        try {
            db.parse(getClass().getResourceAsStream(docName + ".xml"));
            return true;
        } catch (SAXException se) {
            log.error("Error validating " + docName + ".xml", se);
            return false;
        }
    }

    private List<DocumentType> testDoc(String docName, Class expectedException) throws Exception {
        return testDoc(docName, true, expectedException);
    }
    
    private List<DocumentType> testDoc(String docName, boolean valid, Class expectedException) throws Exception {
        assertEquals(valid, validate(docName));

        DocumentTypeXmlParser parser = new DocumentTypeXmlParser();
        try {
            List<DocumentType> docTypes = parser.parseDocumentTypes(getClass().getResourceAsStream(docName + ".xml"));
            if (expectedException != null) {
                fail(docName + " successfully loaded");
            }
            return docTypes;
        } catch (Exception e) {
            if (expectedException == null || !(expectedException.isAssignableFrom(e.getClass()))) {
                throw e;
            } else {
                log.error(docName + " exception: " + e);
                return new ArrayList();
            }
        }
    }


    /**
     * This method tests that the new document type with overwrite mode set to true will insert a 
     * new document type.
     */
    @Test public void testLoadOverwriteDocumentType() throws Exception {
        testDoc("OverwriteDocumentType", null);
        assertNotNull("Document type should exist after ingestion", KEWServiceLocator.getDocumentTypeService().findByName("DocumentTypeXmlParserTestDoc_OverwriteDocumentType"));
    }

    @Test public void testLoadDocWithVariousActivationTypes() throws Exception {
        testDoc("ValidActivationTypes", null);
    }

    @Test public void testLoadDocWithInvalidActivationType() throws Exception {
        testDoc("BadActivationType", false, IllegalArgumentException.class);
    }

    @Test public void testLoadDocWithValidPolicyNames() throws Exception {
        testDoc("ValidPolicyNames", null);
    }
    
    @Test public void testLoadDocWithValidRuleSelector() throws Exception {
        testDoc("ValidRuleSelector", null);
    }

    @Test public void testLoadDocWithDuplicatePolicyName() throws Exception {
        testDoc("DuplicatePolicyName", XmlException.class);
    }

    @Test public void testLoadDocWithBadPolicyName() throws Exception {
        testDoc("BadPolicyName", false, IllegalArgumentException.class);
    }

    @Test public void testLoadDocWithBadNextNode() throws Exception {
        testDoc("BadNextNode", XmlException.class);
    }

    @Test public void testLoadDocWithNoDocHandler() throws Exception {
        testDoc("NoDocHandler", null);
        DocumentType documentType = KEWServiceLocator.getDocumentTypeService().findByName("DocumentTypeXmlParserTestDoc1");
        assertTrue("Doc type unresolved doc handler should be empty.", StringUtils.isBlank(documentType.getUnresolvedDocHandlerUrl()));
        assertTrue("Doc type doc handler should be empty.", StringUtils.isBlank(documentType.getUnresolvedDocHandlerUrl()));
    }

    @Test public void testLoadDocWithBadExceptionWG() throws Exception {
        testDoc("BadExceptionWorkgroup", false, GroupNotFoundException.class);
    }

    @Test public void testLoadDocWithBadSuperUserWG() throws Exception {
        testDoc("BadSuperUserWorkgroup", false, GroupNotFoundException.class);
    }

    @Test public void testLoadDocWithBadBlanketApproveWG() throws Exception {
        testDoc("BadBlanketApproveWorkgroup", false, GroupNotFoundException.class);
    }

    @Test public void testLoadDocWithBadRuleTemplate() throws Exception {
        testDoc("BadRuleTemplate", XmlException.class);
    }

    @Test public void testLoadDocWithInvalidParent() throws Exception {
        testDoc("InvalidParent", XmlException.class);
    }
    
    @Test public void testLoadDocWithOrphanedNodes() throws Exception {
    	testDoc("OrphanedNodes", XmlException.class);
    }
    
    @Test public void testBlanketApprovePolicy() throws Exception {
    	testDoc("BlanketApprovePolicy", null);
    	
    	// on BlanketApprovePolicy1 anyone can blanket approve
    	WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("pzhang"), "BlanketApprovePolicy1");
    	document.saveDocumentData();
    	assertTrue(isActionCodeValidForDocument(document, KewApiConstants.ACTION_TAKEN_BLANKET_APPROVE_CD));
    	document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
    	assertFalse(isActionCodeValidForDocument(document, KewApiConstants.ACTION_TAKEN_BLANKET_APPROVE_CD));

    	// on BlanketApprovePolicy2 no-one can blanket approve
    	document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("pzhang"), "BlanketApprovePolicy2");
    	document.saveDocumentData();
    	assertFalse(isActionCodeValidForDocument(document, KewApiConstants.ACTION_TAKEN_BLANKET_APPROVE_CD));
    	document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
    	assertFalse(isActionCodeValidForDocument(document, KewApiConstants.ACTION_TAKEN_BLANKET_APPROVE_CD));
    	
    	// on BlanketApprovePolicy3 no-one can blanket approve
    	document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), "BlanketApprovePolicy3");
    	document.saveDocumentData();
    	assertFalse(isActionCodeValidForDocument(document, KewApiConstants.ACTION_TAKEN_BLANKET_APPROVE_CD));
    	document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
    	assertFalse(isActionCodeValidForDocument(document, KewApiConstants.ACTION_TAKEN_BLANKET_APPROVE_CD));
    	
    	// on BlanketApprovePolicy4 TestWorkgroup can blanket approve
    	/*document = WorkflowDocumentFactory.createDocument(new NetworkIdVO("ewestfal"), "BlanketApprovePolicy4");
    	document.saveDocumentData();
    	assertFalse(isActionCodeValidForDocument(document, KewApiConstants.ACTION_TAKEN_BLANKET_APPROVE_CD));*/
    	
    	// on Blanket ApprovePolicy 5, BlanketApprovePolicy is not allowed since no elements are defined on any document types in the hierarchy
    	document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("pzhang"), "BlanketApprovePolicy5");
    	document.saveDocumentData();
    	assertFalse(isActionCodeValidForDocument(document, KewApiConstants.ACTION_TAKEN_BLANKET_APPROVE_CD));
    	document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
    	assertFalse(isActionCodeValidForDocument(document, KewApiConstants.ACTION_TAKEN_BLANKET_APPROVE_CD));
    	
//   	 on Blanket ApprovePolicy 6, BlanketApprovePolicy is not allowed since no elements are defined on any document types in the hierarchy
    	document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("pzhang"), "BlanketApprovePolicy6");
    	document.saveDocumentData();
    	assertFalse(isActionCodeValidForDocument(document, KewApiConstants.ACTION_TAKEN_BLANKET_APPROVE_CD));
    	document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
    	assertFalse(isActionCodeValidForDocument(document, KewApiConstants.ACTION_TAKEN_BLANKET_APPROVE_CD));
    	
//   	 on Blanket ApprovePolicy 7, BlanketApprovePolicy is not allowed since no elements are defined on any document types in the hierarchy
    	document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("pzhang"), "BlanketApprovePolicy7");
    	document.saveDocumentData();
    	assertTrue(isActionCodeValidForDocument(document, KewApiConstants.ACTION_TAKEN_BLANKET_APPROVE_CD));
    	document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
    	assertTrue(isActionCodeValidForDocument(document, KewApiConstants.ACTION_TAKEN_BLANKET_APPROVE_CD));
    	
//   	 on BlanketApprovePolicy_Override_NONE, BlanketApprovePolicy is not allowed since no elements are defined on any document types in the hierarchy
    	document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("pzhang"), "BlanketApprovePolicy_Override_NONE");
    	document.saveDocumentData();
    	assertFalse(isActionCodeValidForDocument(document, KewApiConstants.ACTION_TAKEN_BLANKET_APPROVE_CD));
    	document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
    	assertFalse(isActionCodeValidForDocument(document, KewApiConstants.ACTION_TAKEN_BLANKET_APPROVE_CD));
    	
//   	 on BlanketApprovePolicy_Override_ANY, BlanketApprovePolicy is not allowed since no elements are defined on any document types in the hierarchy
    	document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("pzhang"), "BlanketApprovePolicy_Override_ANY");
    	document.saveDocumentData();
    	assertTrue(isActionCodeValidForDocument(document, KewApiConstants.ACTION_TAKEN_BLANKET_APPROVE_CD));
    	document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
    	assertTrue(isActionCodeValidForDocument(document, KewApiConstants.ACTION_TAKEN_BLANKET_APPROVE_CD));

//  	 on BlanketApprovePolicy_Override_ANY, BlanketApprovePolicy is not allowed since no elements are defined on any document types in the hierarchy
    	document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("pzhang"), "BlanketApprovePolicy_NoOverride");
    	document.saveDocumentData();
    	assertFalse(isActionCodeValidForDocument(document, KewApiConstants.ACTION_TAKEN_BLANKET_APPROVE_CD));
    	document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
    	assertTrue(isActionCodeValidForDocument(document, KewApiConstants.ACTION_TAKEN_BLANKET_APPROVE_CD));
    }
    
    @Test public void testReportingWorkgroupName() throws Exception {
    	testDoc("ReportingWorkgroupName", null);
    	
    	DocumentType documentType1 = KEWServiceLocator.getDocumentTypeService().findByName("ReportingWorkgroupName1");
    	assertNotNull("Should have a reporting workgroup.", documentType1.getReportingWorkgroup());
    	assertEquals("Should be WorkflowAdmin reporting workgroup", "WorkflowAdmin", documentType1.getReportingWorkgroup().getName());
    		
    	DocumentType documentType2 = KEWServiceLocator.getDocumentTypeService().findByName("ReportingWorkgroupName2");
    	assertNull("Should not have a reporting workgroup.", documentType2.getReportingWorkgroup());
    }
    
    @Test public void testCurrentDocumentNotMaxVersionNumber() throws Exception {
        String fileNameToIngest = "VersionNumberCheck";
        String documentTypeName = "VersionCheckDocument";
        testDoc(fileNameToIngest, null);
        testDoc(fileNameToIngest, null);
        testDoc(fileNameToIngest, null);
        
        DocumentType originalCurrentDocType = KEWServiceLocator.getDocumentTypeService().findByName(documentTypeName);
        assertNotNull("Should have found document for doc type '" + documentTypeName + "'",originalCurrentDocType);
        assertNotNull("Doc Type should have previous doc type id",originalCurrentDocType.getPreviousVersionId());
        assertEquals("Doc Type should be current",Boolean.TRUE,originalCurrentDocType.getCurrentInd());
        DocumentType previousDocType1 = KEWServiceLocator.getDocumentTypeService().findById(originalCurrentDocType.getPreviousVersionId());
        assertNotNull("Should have found document for doc type '" + documentTypeName + "' and previous version " + originalCurrentDocType.getPreviousVersionId(),previousDocType1);
        assertNotNull("Doc Type should have previous doc type id",previousDocType1.getPreviousVersionId());
        DocumentType firstDocType = KEWServiceLocator.getDocumentTypeService().findById(previousDocType1.getPreviousVersionId());
        assertNotNull("Should have found document for doc type '" + documentTypeName + "' and previous version " + previousDocType1.getPreviousVersionId(),firstDocType);
        assertNull("Doc type retrieved should have been first doc type",firstDocType.getPreviousVersionId());
        
        // reset the current document to the previous one to replicate bug conditions
        originalCurrentDocType.setCurrentInd(Boolean.FALSE);
        KEWServiceLocator.getDocumentTypeService().save(originalCurrentDocType);
        firstDocType.setCurrentInd(Boolean.TRUE);
        KEWServiceLocator.getDocumentTypeService().save(firstDocType);
        DocumentType newCurrentDocType = KEWServiceLocator.getDocumentTypeService().findByName(documentTypeName);
        assertNotNull("Should have found document for doc type '" + documentTypeName + "'",newCurrentDocType);
        assertEquals("Version of new doc type should match that of first doc type", firstDocType.getVersion(), newCurrentDocType.getVersion());
        
        // ingest the doc type again and verify correct version number
        try {
            testDoc(fileNameToIngest, null);
        } catch (Exception e) {
            fail("File should have ingested correctly" + e.getLocalizedMessage());
        }
        
        DocumentType currentDocType = KEWServiceLocator.getDocumentTypeService().findByName(documentTypeName);
        assertNotNull("Should have found document for doc type '" + documentTypeName + "'",currentDocType);
        assertEquals("Doc Type should be current",Boolean.TRUE,currentDocType.getCurrentInd());
        assertNotNull("Doc Type should have previous doc type id",currentDocType.getPreviousVersionId());
        assertEquals("New current document should have version 1 greater than ", Integer.valueOf(originalCurrentDocType.getVersion().intValue() + 1), currentDocType.getVersion());
        previousDocType1 = KEWServiceLocator.getDocumentTypeService().findById(currentDocType.getPreviousVersionId());
        assertNotNull("Should have found document for doc type '" + documentTypeName + "' and previous version " + newCurrentDocType.getPreviousVersionId(),previousDocType1);
        assertFalse("Doc Type should be current",previousDocType1.getCurrentInd());
        assertNull("Doc type retrieved should not have previous doc type",previousDocType1.getPreviousVersionId());
    }
    
    @Test public void testLoadDocWithOrderedAttributes() throws Exception {
        List documentTypes = testDoc("ValidActivationTypes", null);
        assertEquals("Should only be one doc type parsed", 1, documentTypes.size());
        DocumentType docType = (DocumentType) documentTypes.get(0);
        for (int i = 0; i < docType.getDocumentTypeAttributes().size(); i++) {
            DocumentTypeAttributeBo attribute = docType.getDocumentTypeAttributes().get(i);
            assertEquals("Invalid Index Number", i+1, attribute.getOrderIndex());
        }
        
        DocumentType docTypeFresh = KEWServiceLocator.getDocumentTypeService().findByName("DocumentTypeXmlParserTestDoc_ValidActivationTypes");
        assertEquals("Should be 3 doc type attributes", 3, docTypeFresh.getDocumentTypeAttributes().size());
        int index = 0;
        DocumentTypeAttributeBo attribute = docTypeFresh.getDocumentTypeAttributes().get(index);
        assertEquals("Invalid Index Number", index+1, attribute.getOrderIndex());
        assertEquals("Invalid attribute name for order value " + index+1, "TestRuleAttribute2", attribute.getRuleAttribute().getName());
        
        index = 1;
        attribute = docTypeFresh.getDocumentTypeAttributes().get(index);
        assertEquals("Invalid Index Number", index+1, attribute.getOrderIndex());
        assertEquals("Invalid attribute name for order value " + index+1, "TestRuleAttribute3", attribute.getRuleAttribute().getName());

        index = 2;
        attribute = docTypeFresh.getDocumentTypeAttributes().get(index);
        assertEquals("Invalid Index Number", index+1, attribute.getOrderIndex());
        assertEquals("Invalid attribute name for order value " + index+1, "TestRuleAttribute", attribute.getRuleAttribute().getName());
    }
    
    @Test public void testLoadDocWithNoLabel() throws Exception {
    	List documentTypes = testDoc("DocTypeWithNoLabel", false, null);
    	assertEquals("Should have parsed 1 document type", 1, documentTypes.size());
    	
    	DocumentType documentType = (DocumentType)documentTypes.get(0);
    	assertEquals("Document type has incorrect name", "DocumentTypeXmlParserTestDoc_DocTypeWithNoLabel", documentType.getName());
    	assertEquals("Document type has incorrect label", KewApiConstants.DEFAULT_DOCUMENT_TYPE_LABEL, documentType.getLabel());
    	
    	// now test a DocumentType ingestion with no label for a DocumentType that has a previous version
    	// in this case we use TestDocumentType3 which should have been ingested from DefaultTestData.xml
    	DocumentType testDocType3 = KEWServiceLocator.getDocumentTypeService().findByName("TestDocumentType3");
    	assertNotNull("TestDocumentType3 should exist.", testDocType3);
    	// the current label for TestDocumentType3 should be TestDocumentType
    	String expectedLabel = "TestDocumentType";
    	assertEquals("Incorrect label", expectedLabel, testDocType3.getLabel());
    	
    	// now let's ingest a new version without the label, it should maintain the original label and not
    	// end up with a value of Undefined
    	documentTypes = testDoc("DocTypeWithNoLabelPreviousVersion", false, null);
    	assertEquals("Should have parsed 1 document type", 1, documentTypes.size());
    	testDocType3 = (DocumentType)documentTypes.get(0);
    	assertEquals("Document type has incorrect name", "TestDocumentType3", testDocType3.getName());
    	assertEquals("Document type has incorrect label", expectedLabel, testDocType3.getLabel());
    }

    @Test public void testLoadOverwriteModeDocumentType() throws Exception {
        String docTypeName = "LoadRoutePathOnlyAdjustsDocument";
        testDoc("RoutePathAdjustment1", null);
        DocumentType docType1 = KEWServiceLocator.getDocumentTypeService().findByName(docTypeName);
        assertNotNull("Document type should exist", docType1);
        assertEquals("The blanket approve workgroup name is incorrect", "TestWorkgroup", docType1.getBlanketApproveWorkgroup().getName());
        assertEquals("The blanket approve workgroup namespace is incorrect", "KR-WKFLW", docType1.getBlanketApproveWorkgroup().getNamespaceCode());
        assertEquals("The super user workgroup name is incorrect", "TestWorkgroup", docType1.getSuperUserWorkgroup().getName());
        assertEquals("The super user workgroup namespace is incorrect", "KR-WKFLW", docType1.getSuperUserWorkgroup().getNamespaceCode());
        List routeNodes = KEWServiceLocator.getRouteNodeService().getFlattenedNodes(docType1, true);
        assertEquals("Incorrect document route node count", 1, routeNodes.size());
        assertEquals("Expected Route Node Name is incorrect", "First", ((RouteNode)routeNodes.get(0)).getRouteNodeName());

        testDoc("RoutePathAdjustment2", null);
        DocumentType docType2 = KEWServiceLocator.getDocumentTypeService().findByName(docTypeName);
        assertNotNull("Document type should exist", docType1);
        assertEquals("The blanket approve workgroup name is incorrect", "WorkflowAdmin", docType2.getBlanketApproveWorkgroup().getName());
        assertEquals("The blanket approve workgroup namespace is incorrect", "KR-WKFLW", docType2.getBlanketApproveWorkgroup().getNamespaceCode());
        assertEquals("The super user workgroup name is incorrect", "TestWorkgroup", docType2.getSuperUserWorkgroup().getName());
        assertEquals("The super user workgroup namespace is incorrect", "KR-WKFLW", docType2.getSuperUserWorkgroup().getNamespaceCode());
        routeNodes = KEWServiceLocator.getRouteNodeService().getFlattenedNodes(docType2, true);
        assertEquals("Incorrect document route node count", 2, routeNodes.size());
        assertEquals("Expected Route Node Name is incorrect", "First", ((RouteNode)routeNodes.get(0)).getRouteNodeName());
        assertEquals("Expected Route Node Name is incorrect", "Second", ((RouteNode)routeNodes.get(1)).getRouteNodeName());
    }

    /**
     * Checks if a child document can be processed when it precedes its parent.
     * 
     * @throws Exception
     */
    @Test public void testLoadDocWithOneChildPrecedingParent() throws Exception {
    	List<?> docTypeList;
    	// Test a case where there is a single child document preceding its parent.
    	docTypeList = testDoc("ChildParentTestConfig1_Reordered", null);
    	assertEquals("There should be 5 document types.", 5, docTypeList.size());
    }

    /**
     * Checks if a child routing document can be processed when it precedes its parent.
     * 
     * @throws Exception
     */
    @Test public void testRouteDocWithOneChildPrecedingParent() throws Exception {
    	List<?> docTypeList;
    	this.loadXmlFile("ChildParentTestConfig1_Reordered.xml");
    	// Test a case where there is a single router child document preceding its parent.
    	docTypeList = testDoc("ChildParentTestConfig1_Routing", null);
    	assertEquals("There should be 5 document types.", 5, docTypeList.size());
    }

    /**
     * Checks if the child-parent resolution works with a larger inheritance tree.
     * 
     * @throws Exception
     */
    @Test public void testLoadDocWithLargerChildPrecedenceInheritanceTree() throws Exception {
    	List<?> docTypeList;
    	// Test a case where there are multiple inheritance tree layers to resolve.
    	docTypeList = testDoc("ChildParentTestConfig1_Reordered2", null);
    	assertEquals("There should be 10 document types.", 10, docTypeList.size());
    }

    /**
     * Checks if the child-parent resolution works with a larger inheritance tree and a mix of standard & routing documents.
     * 
     * @throws Exception
     */
    @Test public void testRouteDocWithLargerChildPrecedenceInheritanceTree() throws Exception {
    	List<?> docTypeList;
    	this.loadXmlFile("ChildParentTestConfig1_Routing2_Prep.xml");
    	// Test a case where there are multiple inheritance tree layers to resolve.
    	docTypeList = testDoc("ChildParentTestConfig1_Routing2", null);
    	assertEquals("There should be 10 document types.", 10, docTypeList.size());
    }

    private void tryLoadingBadDocument(String docTypeFileName, String failToFailMessage) {
        try {
            loadXmlFile(docTypeFileName);
            fail(failToFailMessage);
        } catch (WorkflowRuntimeException e) {
            // Good, that is what we expect
        }
    }

    /**
     * Tests a number of forms of invalid application document status XML to ensure that ingestion fails
     * @throws Exception
     */
    @Test public void testLoadBadDocWithAppDocStatus() throws Exception {
        loadDocWithBadAppDocStatus(
                "      <validApplicationStatuses>\n"
                + "        <status>bogus1</status>\n"
                + "        <category name=\"bogus1\">\n"
                + "          <status>Completed</status>\n"
                + "        </category>\n"
                + "      </validApplicationStatuses>",
                "duplicate category and status name should cause failure to ingest");

        loadDocWithBadAppDocStatus(
                "      <validApplicationStatuses>\n"
                + "        <category name=\"bogus1\">\n"
                + "          <status>Approved</status>\n"
                + "        </category>\n"
                + "        <category name=\"bogus1\">\n"
                + "          <status>Completed</status>\n"
                + "        </category>\n"
                + "      </validApplicationStatuses>",
                "duplicate category name should cause failure to ingest");


        loadDocWithBadAppDocStatus(
                "      <validApplicationStatuses>\n"
                + "        <category name=\"\">\n"
                + "          <status>Approved</status>\n"
                + "        </category>\n"
                + "      </validApplicationStatuses>",
                "empty category name should cause failure to ingest");

        loadDocWithBadAppDocStatus(
                "      <validApplicationStatuses>\n"
                + "        <category>\n"
                + "          <status>Approved</status>\n"
                + "        </category>\n"
                + "      </validApplicationStatuses>",
                "no category name should cause failure to ingest");

        loadDocWithBadAppDocStatus(
                "      <validApplicationStatuses>\n"
                + "        <status>bogus1</status>\n"
                + "        <category name=\"IN PROGRESS\">\n"
                + "        </category>\n"
                + "      </validApplicationStatuses>",
                "empty category should cause failure to ingest");

        loadDocWithBadAppDocStatus(
                "      <validApplicationStatuses>\n"
                + "        <status>bogus1</status>\n"
                + "        <status>bogus1</status>\n"
                + "      </validApplicationStatuses>",
                "duplicate status name should cause failure to ingest");

        loadDocWithBadAppDocStatus(
                "      <validApplicationStatuses>\n"
                + "        <status></status>\n"
                + "      </validApplicationStatuses>",
                "empty status content should cause failure to ingest");

        loadDocWithBadAppDocStatus(
                "      <validApplicationStatuses>\n"
                + "        <status/>\n"
                + "      </validApplicationStatuses>",
                "no status content should cause failure to ingest");

        loadDocWithBadAppDocStatus(
                "      <validApplicationStatuses>\n"
                + "        <status/>\n"
                + "      </validApplicationStatuses>",
                "no status content should cause failure to ingest");

    }

    /**
     * Helper method to test bad validAppStatuses content
     * @param validAppStatusesContent xml content for the validAppStatuses section
     * @param failureMessage the message to call junit's fail with
     */
    private void loadDocWithBadAppDocStatus(String validAppStatusesContent, String failureMessage) {
        try {
            String docTypeContent = applicationStatusDocumentTypeTemplate.replace("VALIDAPPSTATUSES", validAppStatusesContent);
            loadXmlStream(new ByteArrayInputStream(docTypeContent.getBytes()));
            fail(failureMessage);
        } catch (WorkflowRuntimeException e) {
            // good, this is what we want.
        }
    }

    @Test public void testLoadDocWithAppDocStatus() throws Exception {
        loadXmlFile("TestKEWAppDocStatus.xml");
        DocumentType documentType = KEWServiceLocator.getDocumentTypeService().findByName("TestAppDocStatusDoc2");
        
        // verify DocumentStatusPolicy = "APP"
        assertFalse("DocumentStatusPolicy should not be set to KEW, or BOTH", documentType.isKEWStatusInUse());
        assertTrue("DocumentStatusPolicy should be set to APP", documentType.isAppDocStatusInUse());
        
        // verify Valid Statuses defined
        assertTrue("6 Valid Application Statuses should be defined", documentType.getValidApplicationStatuses().size() == 6);
        Iterator<ApplicationDocumentStatus> iter = documentType.getValidApplicationStatuses().iterator();
        while (iter.hasNext()){
        	ApplicationDocumentStatus myStatus = iter.next();
        	String myStatusName = myStatus.getStatusName();
        	assertTrue("Valid Status value is incorrect: " + myStatusName,
        			("Approval In Progress".equalsIgnoreCase(myStatusName) ||
        			"Submitted".equalsIgnoreCase(myStatusName) ||
        			"Pending".equalsIgnoreCase(myStatusName) ||
        			"Completed".equalsIgnoreCase(myStatusName) ||
        			"Approved".equalsIgnoreCase(myStatusName) ||
        			"Rejected".equalsIgnoreCase(myStatusName) ));
        }
        
        //verify next_doc_status in RouteNode
        List procs = documentType.getProcesses();
        ProcessDefinitionBo myProc = (ProcessDefinitionBo) procs.get(0);
        RouteNode myNode = myProc.getInitialRouteNode();
        String nextDocStatus = myNode.getNextDocStatus();
        assertTrue("RouteNode nextDocStatus is Invalid", "Approval in Progress".equalsIgnoreCase(nextDocStatus));

        // Test that a document type with app doc status categories has the configured structure
        DocumentType documentType4 = KEWServiceLocator.getDocumentTypeService().findByName("TestAppDocStatusDoc4");
        List<ApplicationDocumentStatusCategory> categories = documentType4.getApplicationStatusCategories();
        assertTrue(2 == categories.size());
        assertTrue(9 == documentType4.getValidApplicationStatuses().size());
    }

    @Test public void testLoadDocWithInvalidDocumentStatusPolicy() throws Exception {
        testDoc("DocumentStatusPolicyInvalidStringValue", XmlException.class);
    }
    
    @Test public void testLoadDocWithBlankDocumentStatusPolicyStringValue() throws Exception {
        testDoc("DocumentStatusPolicyMissingStringValue", false, XmlException.class);
    }

    @Test public void testLoadDocWithDocTypePolicyXMLConfig() throws Exception {
        List<DocumentType> docTypes = testDoc("DocumentTypePolicyConfig", null);
        assertEquals(1, docTypes.size());
        DocumentType docType = docTypes.get(0);
        DocumentTypePolicy policy = docType.getRecallNotification();
        assertNotNull(policy);
        assertNotNull(policy.getPolicyStringValue());
        assertEquals("<config>"
                    + "<recipients xmlns:r=\"ns:workflow/Rule\" xsi:schemaLocation=\"ns:workflow/Rule resource:Rule\">"
                    + "<r:principalName>quickstart</r:principalName>"
                    + "<r:user>quickstart</r:user>"
                    + "<role name=\"foobar\" namespace=\"KEW\"/>"
                    + "</recipients>"
                    + "</config>", policy.getPolicyStringValue().replaceAll("\\n*", ""));
    }

    private boolean isActionCodeValidForDocument(WorkflowDocument document, String actionCode) {
        return document.isValidAction(ActionType.fromCode(actionCode));
    }

    
}
