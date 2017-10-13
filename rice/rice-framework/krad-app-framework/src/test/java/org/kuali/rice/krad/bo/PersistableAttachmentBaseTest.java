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
 * tests PersistableAttachmentBase getters and setters
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class PersistableAttachmentBaseTest {

	PersistableAttachmentBase persistableAttachmentBase;
	
	@Before
	public void setUp() throws Exception {
		persistableAttachmentBase = new PersistableAttachmentBase();
	}

	@After
	public void tearDown() throws Exception {
	    persistableAttachmentBase = null;
	}
	
	@Test
    /**
     * tests attachmentContent getter and setter
     */
	public void testAttachmentContent(){
		byte[] dummyByte = "dummy string".getBytes(); 
		persistableAttachmentBase.setAttachmentContent(dummyByte);
		assertEquals("Testing AttachmentContent in PersistableAttachmentBase.",dummyByte,persistableAttachmentBase.getAttachmentContent());
	}
	
	@Test
    /**
     * tests fileName getter and setter
     */
	public void testFileName(){
		persistableAttachmentBase.setFileName("FileName");
		assertEquals("Testing FileName in PersistableAttachmentBase.","FileName",persistableAttachmentBase.getFileName());
	}
	
	@Test
    /**
     * tests contentType getter and setter
     */
	public void testContentType(){
		persistableAttachmentBase.setContentType("contentType");
		assertEquals("Testing FileName in PersistableAttachmentBase.","contentType",persistableAttachmentBase.getContentType());
	}
}
