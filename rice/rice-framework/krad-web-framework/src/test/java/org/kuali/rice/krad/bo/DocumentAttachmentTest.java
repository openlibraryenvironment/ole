/**
 * Copyright 2005-2014 The Kuali Foundation
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
package org.kuali.rice.krad.bo;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * tests DocumentAttachment getters and setters
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class DocumentAttachmentTest {

	DocumentAttachment dummyDocumentAttachment;

	@Before
	public void setUp() throws Exception {
		dummyDocumentAttachment = new DocumentAttachment();
	}

	@After
	public void tearDown() throws Exception {
		dummyDocumentAttachment = null;
	}

	@Test
    /**
     * tests documentNumber getter and setter
     */
	public void testDocumentNumber(){
		dummyDocumentAttachment.setDocumentNumber("c122");
		assertEquals("Testing DocumentNumber in DocumnetAttchment", "c122", dummyDocumentAttachment.getDocumentNumber());
	}
}
