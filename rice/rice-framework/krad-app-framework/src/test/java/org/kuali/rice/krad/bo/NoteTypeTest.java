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
import static org.junit.Assert.assertTrue;


/**
 * tests NoteType getters and setters
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class NoteTypeTest{

	NoteType dummyNoteType;
	
    @Before
	public void setUp() throws Exception {
		dummyNoteType = new NoteType();
		
	}

    @After
	public void tearDown() throws Exception {
		dummyNoteType = null;
	}
	
	@Test
    /**
     * tests noteTypeCode getter and setter
     */
	public void testNoteTypeCode(){
		dummyNoteType.setNoteTypeCode("plain");
		assertEquals("Testing NoteTypeCode in NoteTypeTest","plain",dummyNoteType.getNoteTypeCode());
	}
	
	@Test
    /**
     * tests noteTypeDescription getter and setter
     */
	public void testNoteTypeDescription(){
		dummyNoteType.setNoteTypeDescription("This note is plain");
		assertEquals("Testing NoteTypeDescription in NoteTypeTest","This note is plain",dummyNoteType.getNoteTypeDescription());
	}
	
	@Test
    /**
     * tests noteTypeActiveIndicator getter and setter
     */
	public void testNoteTypeActiveIndicator(){
		dummyNoteType.setNoteTypeActiveIndicator(true);
		assertTrue("Testing setNoteTypeActiveIndicator in NoteTypeTest",dummyNoteType.isNoteTypeActiveIndicator());
	}
}
