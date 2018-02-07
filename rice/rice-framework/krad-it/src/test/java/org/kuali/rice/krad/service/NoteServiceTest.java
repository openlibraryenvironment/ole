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
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.krad.bo.Note;
import org.kuali.rice.krad.util.NoteType;
import org.kuali.rice.krad.test.KRADTestCase;

import static org.junit.Assert.fail;

/**
 * NoteServiceTest is used to test the {@link NoteService} implementation
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class NoteServiceTest extends KRADTestCase {

    /**
     * tests saving notes
     * 
     * @throws Exception
     */
    @Test public void testNoteSave_LargePersonId() throws Exception {
        Note note = new Note();
        note.setAuthorUniversalIdentifier("superLongNameUsersFromWorkflow");
        note.setNotePostedTimestamp(CoreApiServiceLocator.getDateTimeService().getCurrentTimestamp());
        note.setNoteText("i like notes");
        note.setRemoteObjectIdentifier("1209348109834u");
        note.setNoteTypeCode(NoteType.BUSINESS_OBJECT.getCode());
        try {
            KRADServiceLocator.getNoteService().save(note);
        } catch (Exception e) {
            fail("Saving a note should not fail");
        }
    }
    
}

