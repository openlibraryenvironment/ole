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
package org.kuali.rice.kew.doctype;

import mocks.MockPostProcessor;
import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.xml.XmlJotter;
import org.kuali.rice.edl.framework.workflow.EDocLitePostProcessor;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.doctype.service.DocumentTypeService;
import org.kuali.rice.kew.engine.node.NodeType;
import org.kuali.rice.kew.engine.node.ProcessDefinitionBo;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.export.KewExportDataSet;
import org.kuali.rice.kew.framework.postprocessor.PostProcessor;
import org.kuali.rice.kew.postprocessor.DefaultPostProcessor;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.test.TestUtilities;
import org.kuali.rice.kew.xml.export.DocumentTypeXmlExporter;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.test.BaselineTestCase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.NONE)
public class DocumentTypeTest extends KEWTestCase {
    private static final Logger LOG = Logger.getLogger(DocumentTypeTest.class);

    protected void loadTestData() throws Exception {
        ConfigContext.getCurrentContextConfig().putProperty("test.doctype.workgroup", "TestWorkgroup");
        loadXmlFile("DoctypeConfig.xml");
    }

    /**
     * TODO: Ignored currently, this method goes into an infinite loop in document type parsing (ick!) when invoked...not good.
     * 
     * KULRICE-3526
     */
    @Test public void testDuplicateNodeName() throws Exception {
        try {
            loadXmlFile("DocTypeConfig_loadDupliateNodes.xml");
            fail("loadXmlFile should have thrown routing exception");
        } catch (Exception e) {
        }
    }
    @Test public void testDuplicateNodeNameInRoutePath() throws Exception {
        loadXmlFile("DocTypeConfig_duplicateNodes.xml");
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("user1"), "TestDoubleNodeDocumentType");
        document.setTitle("");
        document.route("");
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
        assertTrue("rkirkend should have an approve request", document.isApprovalRequested());
        document.approve("");
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user2"), document.getDocumentId());
        assertTrue("user2 should have an approve request", document.isApprovalRequested());
        document.approve("");
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user3"), document.getDocumentId());
        assertTrue("user3 should have an approve request", document.isApprovalRequested());
        document.approve("");
    }
    @Test public void testNestedDuplicateNodeNameInRoutePath() throws Exception {
        loadXmlFile("DocTypeConfig_nestedNodes.xml");
        // Path 1
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("user1"), "TestDoubleNodeDocumentType");
        document.setTitle("");
        document.route("");
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
        assertTrue("rkirkend should have an approve request", document.isApprovalRequested());
        document.approve("");
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user2"), document.getDocumentId());
        assertTrue("user2 should have an approve request", document.isApprovalRequested());
        document.approve("");
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user3"), document.getDocumentId());
        assertTrue("user3 should have an approve request", document.isApprovalRequested());
        document.approve("");
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user4"), document.getDocumentId());
        assertTrue("user4 should have an approve request", document.isApprovalRequested());
        document.approve("");
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
        assertTrue("rkirkend should have an approve request", document.isApprovalRequested());
        document.approve("");
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user3"), document.getDocumentId());
        assertTrue("user3 should have an approve request", document.isApprovalRequested());
        document.approve("");
    }
    /**
     * Verify that enroute documents are not affected if you edit their document type.
     * @throws Exception
     */
    @Test public void testChangingDocumentTypeOnEnrouteDocument() throws Exception {
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("user1"), "DocumentType");
        document.setTitle("");
        document.route("");

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
        assertTrue("rkirkend should have an approve request", document.isApprovalRequested());

        org.kuali.rice.kew.api.doctype.DocumentTypeService docTypeService = KewApiServiceLocator.getDocumentTypeService();
        Integer version1 = docTypeService.getDocumentTypeByName(document.getDocumentTypeName()).getDocumentTypeVersion();

        //update the document type
        loadXmlFile("DoctypeSecondVersion.xml");

        //verify that we have a new documenttypeid and its a newer version
        Integer version2 = docTypeService.getDocumentTypeByName(document.getDocumentTypeName()).getDocumentTypeVersion();

        assertTrue("Version2 should be larger than verison1", version2.intValue() > version1.intValue());

        //the new version would take the document final
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
        assertTrue("rkirkend should have an approve request", document.isApprovalRequested());

        document.approve("");

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user2"), document.getDocumentId());

        Integer versionDocument = docTypeService.getDocumentTypeById(document.getDocument().getDocumentTypeId()).getDocumentTypeVersion();

        assertTrue("user2 should have an approve request", document.isApprovalRequested());
        //make sure our document still represents the accurate version
        assertEquals("Document has the wrong document type version", version1, versionDocument);
    }

    /**
     * this test will verify that finalapprover node policies work
     *
     * @throws Exception
     */
    @Test public void testFinalApproverRouting() throws Exception {

        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("user1"), "FinalApproverDocumentType");
        document.setTitle("");
        document.route("");
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
        try {
            document.approve("");
            fail("document should have thrown routing exception");
        } catch (Exception e) {
            //deal with single transaction issue in test.
        	TestUtilities.getExceptionThreader().join();
        	document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
            assertTrue("Document should be in exception routing", document.isException());
        }
    }
    
    /**
     * this test will verify that a document type with an empty route path will go directly
     * to "final" status
     *
     * @throws Exception
     */
    @Test public void testEmptyRoutePath() throws Exception {

        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("user1"), "EmptyRoutePathDocumentType");
        document.setTitle("");
        document.route("");
        assertTrue("Document should be in final state", document.isFinal());
    }

    /**
     * Tests that route nodes mark as mandatory send out approve requests
     * @throws Exception
     */
    @Test public void testMandatoryRoute() throws Exception {
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("user1"), "MandatoryRouteDocumentType");
        document.setTitle("");
        try {
            document.route("");
        } catch (Exception e) {
            //deal with single transaction issue in test.
        	TestUtilities.getExceptionThreader().join();
        	document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user1"), document.getDocumentId());
            assertTrue("Document should be in exception routing", document.isException());
        }
    }

    /**
     * Makes sure the DocumentTypeXmlParser is working.  Compare the parsed 'DocumentType' doctype to it's expected values.
     * This test does not include multiple processes.
     *
     * @throws Exception
     */
    @Test public void testDocumentTypeXmlParser() throws Exception {
        ConfigContext.getCurrentContextConfig().putProperty("test.base.url", "http://someurl/path");
        DocumentType parsedDocument = KEWServiceLocator.getDocumentTypeService().findByName("DocumentType");
        assertEquals("Wrong name", "DocumentType", parsedDocument.getName());
        assertEquals("Wrong description", "TestDocumentType", parsedDocument.getDescription());
        assertEquals("Wrong label", "TestDocumentType", parsedDocument.getLabel());
        assertEquals("Wrong postprocessor", "org.kuali.rice.kew.postprocessor.DefaultPostProcessor", parsedDocument.getPostProcessorName());
        assertEquals("Wrong su workgroup", "TestWorkgroup", parsedDocument.getSuperUserWorkgroup().getName());
        // roundabout way of testing to see if the exception workgroup has been processed properly
        DocumentTypeXmlExporter exporter = new DocumentTypeXmlExporter();
        KewExportDataSet dataSet = new KewExportDataSet();
        dataSet.getDocumentTypes().add(parsedDocument);
        String regex = "(?s).*<defaultExceptionGroupName namespace=\"" + KimConstants.KIM_GROUP_WORKFLOW_NAMESPACE_CODE + "\">TestWorkgroup</defaultExceptionGroupName>.*";
        LOG.warn("Using regex: " + regex);
        assertTrue(XmlJotter.jotNode(exporter.export(dataSet.createExportDataSet())).matches(regex));
        //assertNotNull(parsedDocument.getDefaultExceptionWorkgroup());
        //assertEquals("Wrong default exception workgroup", "TestWorkgroup", parsedDocument.getDefaultExceptionWorkgroup().getDisplayName());
        assertEquals("Wrong doc handler url", "http://someurl/path/_blank", parsedDocument.getResolvedDocumentHandlerUrl());
        assertEquals("Wrong unresolved doc handler url", "${test.base.url}/_blank", parsedDocument.getUnresolvedDocHandlerUrl());
        assertEquals("Wrong help def url", "/_help", parsedDocument.getHelpDefinitionUrl());
        assertEquals("Wrong unresolved help def url", "/_help", parsedDocument.getUnresolvedHelpDefinitionUrl());
        assertEquals("Wrong blanketApprover workgroup", "TestWorkgroup", parsedDocument.getBlanketApproveWorkgroup().getName());
        assertEquals("Wrong blanketApprove policy", null, parsedDocument.getBlanketApprovePolicy());
        assertEquals("Wrong DEFAULT_APPROVE policy value", Boolean.FALSE, parsedDocument.getDefaultApprovePolicy().getPolicyValue());
        assertEquals("Wrong LOOK_FUTURE", Boolean.TRUE, parsedDocument.getLookIntoFuturePolicy().getPolicyValue());

        List processes = parsedDocument.getProcesses();
        assertEquals("Should only be 1 process", 1, processes.size());

        //this is going against the intended structure and is very brittle

        ProcessDefinitionBo process = (ProcessDefinitionBo)processes.get(0);
        List flattenedNodeList = KEWServiceLocator.getRouteNodeService().getFlattenedNodes(process);
        assertEquals("Should be 6 total route nodes", 6, flattenedNodeList.size());

        RouteNode adHocNode = process.getInitialRouteNode();
        assertEquals("Wrong node name should be 'AdHoc'", "AdHoc",adHocNode.getRouteNodeName());
        assertTrue("Wrong node type", NodeType.START.isAssignableFrom(Class.forName(adHocNode.getNodeType())));
        assertEquals("Default Exception workgroup not propagated", "TestWorkgroup", adHocNode.getExceptionWorkgroup().getName());

        RouteNode split = (RouteNode)adHocNode.getNextNodes().get(0);
        assertEquals("Wrong node name", "Split", split.getRouteNodeName());
        assertTrue("Wrong node type", NodeType.SPLIT.isAssignableFrom(Class.forName(split.getNodeType())));
        assertEquals("Default Exception workgroup not propagated", "TestWorkgroup", split.getExceptionWorkgroup().getName());

        RouteNode ruleTemplate1 = (RouteNode)split.getNextNodes().get(0);
        assertEquals("Wrong node name", "RuleTemplate1", ruleTemplate1.getRouteNodeName());
        assertTrue("Wrong node type", NodeType.REQUESTS.isAssignableFrom(Class.forName(ruleTemplate1.getNodeType())));
        assertEquals("Wrong branch name", "B1", ruleTemplate1.getBranch().getName());
        assertEquals("Default Exception workgroup not propagated", "TestWorkgroup", ruleTemplate1.getExceptionWorkgroup().getName());

        RouteNode ruleTemplate2 = (RouteNode)split.getNextNodes().get(1);
        assertEquals("Wrong node name", "RuleTemplate2", ruleTemplate2.getRouteNodeName());
        assertTrue("Wrong node type", NodeType.REQUESTS.isAssignableFrom(Class.forName(ruleTemplate2.getNodeType())));
        assertEquals("Wrong branch name", "B2", ruleTemplate2.getBranch().getName());
        assertEquals("Default Exception workgroup not propagated", "TestWorkgroup", ruleTemplate2.getExceptionWorkgroup().getName());

        RouteNode join = (RouteNode)ruleTemplate2.getNextNodes().get(0);
        assertEquals("Wrong node name", "Join", join.getRouteNodeName());
        assertTrue("Wrong node type", NodeType.JOIN.isAssignableFrom(Class.forName(join.getNodeType())));
        assertEquals("Default Exception workgroup not propagated", "TestWorkgroup", join.getExceptionWorkgroup().getName());

        RouteNode ruleTemplate3 = (RouteNode)join.getNextNodes().get(0);
        assertEquals("Wrong node name", "RuleTemplate3", ruleTemplate3.getRouteNodeName());
        assertTrue("Wrong node type", NodeType.REQUESTS.isAssignableFrom(Class.forName(ruleTemplate3.getNodeType())));
        assertEquals("Default Exception workgroup not propagated", "TestWorkgroup", ruleTemplate3.getExceptionWorkgroup().getName());
    }

    //verifies the documenttype hierarchy is intact after multiple uploads
    @Test public void testHierarchyUpload() throws Exception {
    	super.loadXmlFile("ParentWithChildrenDocTypeConfiguration.xml");
    	DocumentType parent = KEWServiceLocator.getDocumentTypeService().findByName("UGSDocumentType");
    	boolean foundRemonstrance = false;
    	boolean foundNewCourse = false;
    	boolean foundDelete = false;
    	for (Iterator iter = parent.getChildrenDocTypes().iterator(); iter.hasNext();) {
			DocumentType childDocType = (DocumentType) iter.next();
			assertTrue("child documenttype should be current", childDocType.getCurrentInd().booleanValue());
			if(childDocType.getName().equals("CourseRemonstranceProcess")) {
				foundRemonstrance = true;
			} else if (childDocType.getName().equals("NewCourseRequest")) {
				foundNewCourse = true;
			} else if (childDocType.getName().equals("DeleteCourseRequest")) {
				foundDelete = true;
			}
		}

    	assertTrue("Didn't find CourseRemonstraneProcess", foundRemonstrance);
    	assertTrue("Didn't find NewCourseRequest", foundNewCourse);
    	assertTrue("Didn't find DeleteCourseRequest", foundDelete);

    	//reload and verify that the structure looks the same - the below is missing one of the children document types
    	//to verify that a partial upload of the hierarchy doesn't kill the entire hierarchy
    	super.loadXmlFile("ParentWithChildrenDocTypeConfiguration2.xml");

    	parent = KEWServiceLocator.getDocumentTypeService().findByName("UGSDocumentType");
    	foundRemonstrance = false;
    	foundNewCourse = false;
    	foundDelete = false;
    	int i = 0;
    	for (Iterator iter = parent.getChildrenDocTypes().iterator(); iter.hasNext(); i++) {
			DocumentType childDocType = (DocumentType) iter.next();
			assertTrue("child documenttype should be current", childDocType.getCurrentInd().booleanValue());
			if(childDocType.getName().equals("CourseRemonstranceProcess")) {
				foundRemonstrance = true;
			} else if (childDocType.getName().equals("NewCourseRequest")) {
				foundNewCourse = true;
			} else if (childDocType.getName().equals("DeleteCourseRequest")) {
				foundDelete = true;
			}
		}
    	assertTrue("Didn't find CourseRemonstranceProcess", foundRemonstrance);
    	assertTrue("Didn't find NewCourseRequest", foundNewCourse);
    	assertTrue("Didn't find DeleteCourseRequest", foundDelete);
    }

    //verifies documenttype hierarchy is intact after uploading a series of documenttypes and then
    //uploading a parent onto those document types
    @Test public void testHierarchyUpload2() throws Exception {
    	super.loadXmlFile("DocTypesWithoutParent.xml");
    	//Verify that the document types are there
    	DocumentType courseRemonstrance1 = KEWServiceLocator.getDocumentTypeService().findByName("CourseRemonstranceProcess");
    	DocumentType newCourseRequest1 = KEWServiceLocator.getDocumentTypeService().findByName("NewCourseRequest");
    	DocumentType deleteCourse1 = KEWServiceLocator.getDocumentTypeService().findByName("DeleteCourseRequest");

    	//upload the new config with the parent and verify we are getting new document types with new versions
    	super.loadXmlFile("ParentWithChildrenDocTypeConfiguration.xml");
    	DocumentType courseRemonstrance2 = null;
    	DocumentType newCourseRequest2 = null;
    	DocumentType deleteCourse2 = null;

    	DocumentType ugsDocumentType = KEWServiceLocator.getDocumentTypeService().findByName("UGSDocumentType");
    	for (Iterator iter = ugsDocumentType.getChildrenDocTypes().iterator(); iter.hasNext();) {
			DocumentType childDocType = (DocumentType) iter.next();
			if(childDocType.getName().equals("CourseRemonstranceProcess")) {
				courseRemonstrance2 = childDocType;
			} else if (childDocType.getName().equals("NewCourseRequest")) {
				newCourseRequest2 = childDocType;
			} else if (childDocType.getName().equals("DeleteCourseRequest")) {
				deleteCourse2 = childDocType;
			}
		}

    	assertNotNull(courseRemonstrance2);
    	assertNotNull(newCourseRequest2);
    	assertNotNull(deleteCourse2);

    	assertTrue("Version didn't get incremented", courseRemonstrance1.getVersion().intValue() < courseRemonstrance2.getVersion().intValue());
    	assertTrue("Version didn't increment", newCourseRequest1.getVersion().intValue() < newCourseRequest2.getVersion().intValue());
    	assertTrue("Version didn't increment", deleteCourse1.getVersion().intValue() < deleteCourse2.getVersion().intValue());
    }

    /**
     * Tests that the document type ingestion will not create a brand new
     * document when only label or description field changes.  Relates to
     * JIRA's EN-318 and KULOWF-147.
     *
     * @throws Exception
     */
    @Test public void testDocumentTypeIngestion() throws Exception {
        // first ingestion
        super.loadXmlFile("DocTypeIngestTestConfig1.xml");  // original document
        super.loadXmlFile("DocTypeIngestTestConfig2.xml");  // document with changed label and description fields

        DocumentType secondIngestDoc = KEWServiceLocator.getDocumentTypeService().findByName("IngestTestDocumentType");
        assertNotNull("Second ingested document has empty Previous Version ID after first ingest", secondIngestDoc.getPreviousVersionId());
        DocumentType firstIngestDoc = KEWServiceLocator.getDocumentTypeService().findById(secondIngestDoc.getPreviousVersionId());

        // the second ingested document should now be set to Current with the first ingested document should no longer be set to Current
        assertEquals("First ingested document is still set to Current after first ingest", Boolean.FALSE, firstIngestDoc.getCurrentInd());
        assertEquals("Second ingested document is not set to Current after first ingest", Boolean.TRUE, secondIngestDoc.getCurrentInd());

        // second ingestion
        super.loadXmlFile("DocTypeIngestTestConfig3.xml");  // document setting active to false

        firstIngestDoc = null;
        secondIngestDoc = null;
        DocumentType thirdIngestDoc = KEWServiceLocator.getDocumentTypeService().findByName("IngestTestDocumentType");
        assertNotNull("Third ingested document has empty Previous Version ID after second ingest", thirdIngestDoc.getPreviousVersionId());
        secondIngestDoc = KEWServiceLocator.getDocumentTypeService().findById(thirdIngestDoc.getPreviousVersionId());
        assertNotNull("Second ingested document has empty Previous Version ID after second ingest", secondIngestDoc.getPreviousVersionId());
        firstIngestDoc = KEWServiceLocator.getDocumentTypeService().findById(secondIngestDoc.getPreviousVersionId());

        // the third ingested document should now be set to Current and Inactive... all others should not be set to Current
        assertEquals("First ingested document is set to Current after second ingest", Boolean.FALSE, firstIngestDoc.getCurrentInd());
        assertEquals("Second ingested document is set to Current after second ingest", Boolean.FALSE, secondIngestDoc.getCurrentInd());
        assertEquals("Third ingested document is not set to Inactive after second ingest", Boolean.FALSE, thirdIngestDoc.getActive());
        assertEquals("Third ingested document is not set to Current after second ingest", Boolean.TRUE, thirdIngestDoc.getCurrentInd());

        // third ingestion
        super.loadXmlFile("DocTypeIngestTestConfig4.xml");  // document setting active to true

        firstIngestDoc = null;
        secondIngestDoc = null;
        thirdIngestDoc = null;
        DocumentType fourthIngestDoc = KEWServiceLocator.getDocumentTypeService().findByName("IngestTestDocumentType");
        assertNotNull("Fourth ingested document has empty Previous Version ID after third ingest", fourthIngestDoc.getPreviousVersionId());
        thirdIngestDoc = KEWServiceLocator.getDocumentTypeService().findById(fourthIngestDoc.getPreviousVersionId());
        assertNotNull("Third ingested document has empty Previous Version ID after third ingest", thirdIngestDoc.getPreviousVersionId());
        secondIngestDoc = KEWServiceLocator.getDocumentTypeService().findById(thirdIngestDoc.getPreviousVersionId());
        assertNotNull("Second ingested document has empty Previous Version ID after third ingest", secondIngestDoc.getPreviousVersionId());
        firstIngestDoc = KEWServiceLocator.getDocumentTypeService().findById(secondIngestDoc.getPreviousVersionId());

        // the fourth ingested document should now be set to Current and Active... all others should not be set to Current
        assertEquals("First ingested document is set to Current after third ingest", Boolean.FALSE, firstIngestDoc.getCurrentInd());
        assertEquals("Second ingested document is set to Current after third ingest", Boolean.FALSE, secondIngestDoc.getCurrentInd());
        assertEquals("Third ingested document is set to Current after third ingest", Boolean.FALSE, thirdIngestDoc.getCurrentInd());
        assertEquals("Fourth ingested document is not set to Active after third ingest", Boolean.TRUE, fourthIngestDoc.getActive());
        assertEquals("Fourth ingested document is not set to Current after third ingest", Boolean.TRUE, fourthIngestDoc.getCurrentInd());
    }

    @Test public void testDocumentTypeParentChildLinking() throws Exception {
    	super.loadXmlFile("ParentWithChildrenDocTypeConfiguration.xml");
    	verifyDocumentTypeLinking();

    	// scenario 1, update the parent document type and verify that all linking is correct
    	super.loadXmlFile("ParentWithChildrenDocTypeConfigurationUpdate1.xml");
    	verifyDocumentTypeLinking();

    	// scenario 2, update a child document type and verify that all linking is correct
    	super.loadXmlFile("ParentWithChildrenDocTypeConfigurationUpdate2.xml");
    	verifyDocumentTypeLinking();

    	// let's reimport from the beginning as well
    	super.loadXmlFile("ParentWithChildrenDocTypeConfiguration.xml");
    	verifyDocumentTypeLinking();

    	// scenario 3, try an xml file with child doctype listed first
    	super.loadXmlFile("ParentWithChildrenDocTypeConfigurationUpdate3.xml");
    	verifyDocumentTypeLinking();

    	// try loading each of these in parallel threads to verify caching can
    	// handle concurrency situations
    	int num = 7;
    	Thread[] threads = new Thread[num];
    	Callback[] callbacks = new Callback[num];
    	for (int i = 0; i < num; i++) {
    		callbacks[i] = new Callback();
    	}
    	threads[0] = new Thread(new LoadXml("ParentWithChildrenDocTypeConfiguration.xml", callbacks[0]));
    	threads[1] = new Thread(new LoadXml("DocTypeIngestTestConfig1.xml", callbacks[1]));
    	threads[2] = new Thread(new LoadXml("DocumentTypeAttributeFetchTest.xml", callbacks[2]));
    	threads[3] = new Thread(new LoadXml("ChildDocType1.xml", callbacks[3]));
    	threads[4] = new Thread(new LoadXml("ChildDocType2.xml", callbacks[4]));
    	threads[5] = new Thread(new LoadXml("ChildDocType3.xml", callbacks[5]));
    	threads[6] = new Thread(new LoadXml("ChildDocType4.xml", callbacks[6]));
    	for (Thread thread : threads) {
    		thread.start();
    	}
    	for (Thread thread : threads) {
    		thread.join(2*60*1000);
    	}
    	// What should have happened here was an optimistic lock being thrown from the
    	// document type XML import.  Currently, that code is catching and just logging
    	// those errors (not rethrowing), so there's no way for us to check that the
    	// optimistic lock was thrown. However, the verifyDocumentTypeLinking should pass
    	// because the update was never made, and we can check to make sure that
    	// at least one of the above documents failed to be ingested.
    	boolean atLeastOneFailure = false;
    	for (Callback callback : callbacks) {
    		if (!callback.isXmlLoaded()) {
    			atLeastOneFailure = true;
    		}
    	}
    	assertTrue("At least one of the XML files should have failed the ingestion process", atLeastOneFailure);
    	verifyDocumentTypeLinking();

    	// reload again for good measure
    	super.loadXmlFile("ParentWithChildrenDocTypeConfiguration.xml");
    	verifyDocumentTypeLinking();

    }
    
    @Test public void testSameFileChildParentIngestion() throws Exception {
        loadXmlFile("ChildParentTestConfig1.xml");
        verifyDocumentTypeLinking();
        loadXmlFile("ChildParentTestConfig2.xml");
        verifyDocumentTypeLinking();
    }

    @Test
    public void testPostProcessor() throws Exception {
    	loadXmlFile("DoctypePostProcessorConfig.xml");

    	DocumentType ppTestParent1 = KEWServiceLocator.getDocumentTypeService().findByName("PPTestParent1");
    	DocumentType ppTestParent2 = KEWServiceLocator.getDocumentTypeService().findByName("PPTestParent2");
    	DocumentType ppTestChild1 = KEWServiceLocator.getDocumentTypeService().findByName("PPTestChild1");
    	DocumentType ppTestChild2 = KEWServiceLocator.getDocumentTypeService().findByName("PPTestChild2");
    	DocumentType ppTestChild3 = KEWServiceLocator.getDocumentTypeService().findByName("PPTestChild3");

    	assertEquals("Incorrect PostProcessor", MockPostProcessor.class, ppTestParent1.getPostProcessor().getClass());
    	assertEquals("Incorrect PostProcessor", DefaultPostProcessor.class, ppTestParent2.getPostProcessor().getClass());
    	assertEquals("Incorrect PostProcessor", MockPostProcessor.class, ppTestChild1.getPostProcessor().getClass());
    	PostProcessor testChild2PP = ppTestChild2.getPostProcessor();
    	assertEquals("Incorrect PostProcessorRemote", EDocLitePostProcessor.class, testChild2PP.getClass());
    	assertEquals("Incorrect PostProcessor", DefaultPostProcessor.class, ppTestChild3.getPostProcessor().getClass());
    }

    /**
     * Tests to ensure that a given document type has its fields updated when the a second XML doc type with the same name is ingested.
     * 
     * NOTE: This unit test is still incomplete.
     * 
     * @throws Exception
     */
    @Test public void testUpdateOfDocTypeFields() throws Exception {
    	//Collection<DocumentTypePolicy> docPolicies = docType.getPolicies();
    	//List<DocumentTypeAttribute> docAttributes = docType.getDocumentTypeAttributes();
    	//List firstRouteNode = KEWServiceLocator.getRouteNodeService().getInitialNodeInstances(docType.getDocumentTypeId());
    	// The expected field values from the test XML files.
    	String[][] expectedValues = { {"TestWithMostParams1", "TestParent01", "A test of doc type parameters.", "TestWithMostParams1",
    			"mocks.MockPostProcessor", "KR-WKFLW:TestWorkgroup", null, "any", "KR-WKFLW:TestWorkgroup",
    			"KR-WKFLW:TestWorkgroup", "_blank", "_blank", "_blank", "_blank", "_blank", "TestCl1", "false", "a.doc.type.authorizer"},
    								{"TestWithMostParams1", "AnotherParent", "Another test of most parameters.",
    			"AntoherTestWithMostParams", "org.kuali.rice.kew.postprocessor.DefaultPostProcessor", "KR-WKFLW:WorkflowAdmin",
    			"KR-WKFLW:WorkflowAdmin", null, "KR-WKFLW:WorkflowAdmin", "KR-WKFLW:WorkflowAdmin", "_nothing", "_nothing",
    			"_nothing", "_nothing", "_nothing", "KEW", "true", "a.parent.authorizer"}
    	};
    	// Ingest each document type, and test the properties of each one.
    	for (int i = 0; i < expectedValues.length; i++) {
    		// Load the document type and store its data.
    	    String fileToLoad = "DocTypeWithMostParams" + (i+1) + ".xml"; 
    		loadXmlFile(fileToLoad);
    		DocumentType docType = KEWServiceLocator.getDocumentTypeService().findByName("TestWithMostParams1");
    		Group baWorkgroup = docType.getBlanketApproveWorkgroup();
    		Group rpWorkgroup = docType.getReportingWorkgroup();
    		Group deWorkgroup = getValidDefaultExceptionWorkgroup(docType);
        	String[] actualValues = {docType.getName(), docType.getParentDocType().getName(), docType.getDescription(),
        			docType.getLabel(), docType.getPostProcessorName(), constructGroupNameWithNamespace(docType.getSuperUserWorkgroupNoInheritence()),
        			constructGroupNameWithNamespace(baWorkgroup), docType.getBlanketApprovePolicy(),
        			constructGroupNameWithNamespace(rpWorkgroup), constructGroupNameWithNamespace(deWorkgroup),
        	    	docType.getUnresolvedDocHandlerUrl(), docType.getUnresolvedHelpDefinitionUrl(),
        	    	docType.getUnresolvedDocSearchHelpUrl(),
        	    	docType.getNotificationFromAddress(), docType.getCustomEmailStylesheet(),
        	    	docType.getApplicationId(), docType.getActive().toString(),
                    docType.getAuthorizer()
        	};
        	// Compare the expected field values with the actual ones.
    		for (int j = 0; j < expectedValues[i].length; j++) {
    			assertEquals("The document does not have the expected parameter value. (i=" + i + ",j=" + j + ")", expectedValues[i][j], actualValues[j]);
    		}
    	}
    }

    private Group getValidDefaultExceptionWorkgroup(DocumentType documentType) {
        List flattenedNodes = KEWServiceLocator.getRouteNodeService().getFlattenedNodes(documentType, false);
        assertTrue("Document Type '" + documentType.getName() + "' should have a default exception workgroup.", hasDefaultExceptionWorkgroup(flattenedNodes));
        return ((RouteNode)flattenedNodes.get(0)).getExceptionWorkgroup();
    }

    // this method is duplicated in the DocumentTypeXmlExporter class
    private boolean hasDefaultExceptionWorkgroup(List flattenedNodes) {
        boolean hasDefaultExceptionWorkgroup = true;
        String exceptionWorkgroupName = null;
        for (Iterator iterator = flattenedNodes.iterator(); iterator.hasNext();) {
            RouteNode node = (RouteNode) iterator.next();
            if (exceptionWorkgroupName == null) {
                exceptionWorkgroupName = node.getExceptionWorkgroupName();
            }
            if (exceptionWorkgroupName == null || !exceptionWorkgroupName.equals(node.getExceptionWorkgroupName())) {
                hasDefaultExceptionWorkgroup = false;
                break;
            }
        }
        return hasDefaultExceptionWorkgroup;
    }

    private String constructGroupNameWithNamespace(Group group) {
        if (ObjectUtils.isNull(group)) {
            return null;
        }
        return group.getNamespaceCode() + KewApiConstants.KIM_GROUP_NAMESPACE_NAME_DELIMITER_CHARACTER + group.getName();
    }

    protected void verifyDocumentTypeLinking() throws Exception {
    	DocumentTypeService service = KEWServiceLocator.getDocumentTypeService();
    	List rootDocs = service.findAllCurrentRootDocuments();
    	int numRoots = rootDocs.size();
    	List documentTypes = service.findAllCurrent();
    	List<DocumentType> leafs = new ArrayList<DocumentType>();
    	for (Iterator iterator = documentTypes.iterator(); iterator.hasNext();) {
			DocumentType documentType = (DocumentType) iterator.next();
			// check that all document types with parents are current
			if (ObjectUtils.isNotNull(documentType.getParentDocType())) {
			    assertEquals("Parent of document type '" + documentType.getName() + "' should be Current", Boolean.TRUE, documentType.getParentDocType().getCurrentInd());
			}
			List children = service.getChildDocumentTypes(documentType.getDocumentTypeId());
			if (children.isEmpty()) {
				leafs.add(documentType);
			} else {
	            // check that all child document types are current
			    for (Iterator iterator2 = children.iterator(); iterator2.hasNext();) {
                    DocumentType childDocType = (DocumentType) iterator2.next();
                    assertEquals("Any child document type should be Current", Boolean.TRUE, childDocType.getCurrentInd());
                }
			}
		}
    	Set<String> rootDocIds = new HashSet<String>();
    	// verify the hierarchy
    	for (DocumentType leaf : leafs) {
			verifyHierarchy(leaf, rootDocIds);
		}
    	for (DocumentType leaf : leafs) {
			verifyHierarchy(leaf, rootDocIds);
		}
    	// we should have the same number of roots as we did from the original roots query
    	assertEquals("Should have the same number of roots", numRoots, rootDocIds.size());
    }
    
    protected void verifyHierarchy(DocumentType docType, Set<String> rootDocIds) {
    	assertTrue("DocumentType " + docType.getName() + " should be current.", docType.getCurrentInd().booleanValue());
    	if (docType.getParentDocType() == null) {
    		rootDocIds.add(docType.getDocumentTypeId());
    	} else {
    		verifyHierarchy(docType.getParentDocType(), rootDocIds);
    	}
    }

    private class LoadXml implements Runnable {

    	private String xmlFile;
    	private Callback callback;

    	public LoadXml(String xmlFile, Callback callback) {
    		this.xmlFile = xmlFile;
    		this.callback = callback;
    	}

		public void run() {
			try {
				loadXmlFile(xmlFile);
			} catch (Throwable t) {
				callback.record(xmlFile, t);
			}
		}

    }

    private class Callback {
    	private String xmlFile;
    	private Throwable t;
    	public void record(String xmlFile, Throwable t) {
    		this.xmlFile = xmlFile;
    		this.t = t;
    	}
    	public boolean isXmlLoaded() {
    		if (t != null) {
    			t.printStackTrace();
   				//fail("Failed to load xml file " + xmlFile);
   				LOG.info("The XML file " + xmlFile + " failed to load, but this was expected.");
   				return false;
    		}
    		return true;
    	}
    }
}
