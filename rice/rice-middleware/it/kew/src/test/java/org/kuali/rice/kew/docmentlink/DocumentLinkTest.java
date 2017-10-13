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
package org.kuali.rice.kew.docmentlink;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.document.DocumentLink;
import org.kuali.rice.kew.api.document.WorkflowDocumentService;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.test.BaselineTestCase;

/**
 * This is a description of what this class does - g1zhang don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (kuali-rice@googlegroups.com)
 *
 */
@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.NONE)
public class DocumentLinkTest extends KEWTestCase {

	private static final Logger LOG = Logger.getLogger(DocumentLinkTest.class);
	
	private WorkflowDocumentService service;

	@Override
	protected void setUpAfterDataLoad() throws Exception {
		super.setUpAfterDataLoad();
		this.service = KewApiServiceLocator.getWorkflowDocumentService();
	}

	@Test public void testAddLinkBTW2DocsSucess() throws Exception {

		// Test add link
		DocumentLink testDocLink1 = DocumentLink.Builder.create("5000", "6000").build();
		
		
		testDocLink1 = service.addDocumentLink(testDocLink1);
		assertNotNull(testDocLink1.getId());
		assertEquals("5000", testDocLink1.getOriginatingDocumentId());
		assertEquals("6000", testDocLink1.getDestinationDocumentId());

		// ensure that a corresponding link was created with the other document
		List<DocumentLink> outgoingLinks = service.getOutgoingDocumentLinks("6000");
		assertEquals(1, outgoingLinks.size());
		DocumentLink testDocLink2 = outgoingLinks.get(0);
		
		assertEquals(testDocLink1.getOriginatingDocumentId(), testDocLink2.getDestinationDocumentId());
		assertEquals(testDocLink2.getOriginatingDocumentId(), testDocLink1.getDestinationDocumentId());

	}

	@Test public void testAddDuplicatedLinkBTW2DocsFailure() throws Exception {

		DocumentLink testDocLink = DocumentLink.Builder.create("5000", "6000").build();

		DocumentLink testDocLinkAdded = service.addDocumentLink(testDocLink);
		assertNotNull(testDocLinkAdded);
		assertNotNull(testDocLinkAdded.getId());

		List<DocumentLink> links1 = service.getOutgoingDocumentLinks("5000");
		assertEquals(1, links1.size());

		DocumentLink testDocLinkAdded2 = service.addDocumentLink(testDocLink);
		assertNotNull(testDocLinkAdded);
		assertNotNull(testDocLinkAdded.getId());
		assertEquals(testDocLinkAdded2, testDocLinkAdded);

		List<DocumentLink> links2 = service.getOutgoingDocumentLinks("5000");
		assertEquals(1, links2.size());

		assertEquals(links1.size(), links2.size());

	}

	@Test public void testAddIncomplelteLinkBTW2DocsFailure() throws Exception {

		try {
			DocumentLink.Builder.create(null, null);
			fail();
		} catch (IllegalArgumentException e) {}

		try{
			DocumentLink.Builder.create("6000", null);
			fail();
		} catch (IllegalArgumentException e){
			assertTrue(e.getMessage().contains("was null or blank"));
		}

	}

	@Test public void testGetLinkBTW2DocsSucess() throws Exception {

		DocumentLink testDocLink = DocumentLink.Builder.create("5000", "6000").build();

		DocumentLink link1 = service.addDocumentLink(testDocLink);

		
		link1 = service.getDocumentLink(link1.getId());

		assertNotNull(link1);
		assertEquals(testDocLink.getOriginatingDocumentId(), link1.getOriginatingDocumentId());
		assertEquals(testDocLink.getDestinationDocumentId(), link1.getDestinationDocumentId());

	}

	@Test public void testGetLinkBTW2DocsFailure() throws Exception {


		DocumentLink testDocLink = DocumentLink.Builder.create("5000", "6000").build();

		service.addDocumentLink(testDocLink);

		List<DocumentLink> links = service.getOutgoingDocumentLinks("5001");

		assertTrue(links.isEmpty());

	}

	@Test public void testGetAllLinksFromOrgnDocSucess() throws Exception {

		DocumentLink link1 = DocumentLink.Builder.create("5000", "6000").build();
		service.addDocumentLink(link1);
		
		DocumentLink link2 = DocumentLink.Builder.create("5009", "6009").build();
		service.addDocumentLink(link2);
		
		DocumentLink link3 = DocumentLink.Builder.create("5000", "6003").build();
		service.addDocumentLink(link3);
		
		DocumentLink link4 = DocumentLink.Builder.create("5000", "6004").build();
		service.addDocumentLink(link4);
		
		List<DocumentLink> links = service.getOutgoingDocumentLinks("5000");
		assertEquals(3, links.size());

	}

	@Test public void testGetAllLinksFromOrgnDocFailure()throws Exception {

		DocumentLink link1 = DocumentLink.Builder.create("5000", "6000").build();
		service.addDocumentLink(link1);

		DocumentLink link2 = DocumentLink.Builder.create("5009", "6009").build();
		service.addDocumentLink(link2);
		
		DocumentLink link3 = DocumentLink.Builder.create("5000", "6003").build();
		service.addDocumentLink(link3);

		List<DocumentLink> links = service.getOutgoingDocumentLinks("8000");

		assertEquals(0, links.size());

	}

	@Test public void testRemoveLinkBTW2DocsSucess() throws Exception{

		DocumentLink link1 = DocumentLink.Builder.create("5000", "6000").build();
		link1 = service.addDocumentLink(link1);

		List<DocumentLink> links1 = service.getOutgoingDocumentLinks("5000");

		assertEquals(1, links1.size());

		List<DocumentLink> links2 = service.getOutgoingDocumentLinks("6000");

		assertEquals(1, links2.size());

		DocumentLink deletedLink = service.deleteDocumentLink(link1.getId());
		assertNotNull(deletedLink);

		List<DocumentLink> links3 = service.getOutgoingDocumentLinks("5000");

		assertEquals(0, links3.size());
	}

	@Test public void testRemoveAllLinksFromOrgnDocSucess() throws Exception {

		DocumentLink link1 = DocumentLink.Builder.create("5000", "6000").build();
		link1 = service.addDocumentLink(link1);

		DocumentLink link2 = DocumentLink.Builder.create("5000", "6002").build();
		link2 = service.addDocumentLink(link2);

		List<DocumentLink> links01 = service.getOutgoingDocumentLinks("5000");
		List<DocumentLink> links02 = service.getOutgoingDocumentLinks("6000");
		List<DocumentLink> links03 = service.getOutgoingDocumentLinks("6002");

		assertEquals(2, links01.size());
		assertEquals(1, links02.size());
		assertEquals(1, links03.size());


		List<DocumentLink> deletedDocuments = service.deleteDocumentLinksByDocumentId("5000");
		assertEquals(2, deletedDocuments.size());

		links01 = service.getOutgoingDocumentLinks("5000");
		links02 = service.getOutgoingDocumentLinks("6000");
		links03 = service.getOutgoingDocumentLinks("6002");

		assertEquals(0, links01.size());
		assertEquals(0, links02.size());
		assertEquals(0, links03.size());

	}
	
	@Test public void testDocLinktoItself() throws Exception {
		try{			
			DocumentLink.Builder.create("5000", "5000");
			fail();
		} catch(IllegalArgumentException e){
			assertTrue(e.getMessage().contains("cannot link a document with itself"));
		}
	}

}
