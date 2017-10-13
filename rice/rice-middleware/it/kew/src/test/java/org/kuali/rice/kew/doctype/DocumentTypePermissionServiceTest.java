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

import org.junit.Test;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.document.Document;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.doctype.service.DocumentTypePermissionService;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class DocumentTypePermissionServiceTest extends KEWTestCase {

	private DocumentTypePermissionService service;
	
	@Override
	public void setUpInternal() throws Exception {
		super.setUpInternal();
		service = KEWServiceLocator.getDocumentTypePermissionService();
	}

	@Test
	public void canBlanketApprove() throws Exception {
		DocumentType testDocType = KEWServiceLocator.getDocumentTypeService().findByName("TestDocumentType");
        Principal ewestfalPrincipal = KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName("ewestfal");
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(ewestfalPrincipal.getPrincipalId(), "TestDocumentType");
        document.route("routing test doc");
		assertNotNull(testDocType);
		assertTrue("ewestfal should be a blanket approver", service.canBlanketApprove(ewestfalPrincipal.getPrincipalId(), KEWServiceLocator.getRouteHeaderService().getRouteHeader(document.getDocumentId())));
		
		// TODO set up actual KIM permissions in DB and verify this permission works
	}
	
	@Test
	public void testCanInitiate() throws Exception {
		DocumentType testDocType = KEWServiceLocator.getDocumentTypeService().findByName("TestDocumentType");
		Principal ewestfalPrincipal = KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName("ewestfal");
		assertNotNull(testDocType);
		assertTrue("ewestfal should be allowed to initiate", service.canInitiate(ewestfalPrincipal.getPrincipalId(), testDocType));
    }
	
}
