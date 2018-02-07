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
package org.kuali.rice.krad.rules.rule.event;

import org.kuali.rice.krad.bo.Note;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.rules.rule.AddNoteRule;
import org.kuali.rice.krad.rules.rule.BusinessRule;

/**
 * This class represents the add note event that is part of an eDoc in Kuali. This is triggered when a user presses the add button
 * for a given note or it could happen when another piece of code calls the create note method in the document service.
 *
 *
 */
public final class AddNoteEvent extends KualiDocumentEventBase {
    private Note note;

    /**
     * Constructs an AddNoteEvent with the specified errorPathPrefix and document
     *
     * @param document
     * @param errorPathPrefix
     */
    public AddNoteEvent(String errorPathPrefix, Document document, Note note) {
        super("creating add note event for document " + KualiDocumentEventBase.getDocumentId(document), errorPathPrefix, document);
        this.note = note;
    }

    /**
     * Constructs an AddNoteEvent with the given document
     *
     * @param document
     */
    public AddNoteEvent(Document document, Note note) {
        this("", document, note);
    }

    /**
     * This method retrieves the note associated with this event.
     *
     * @return
     */
    public Note getNote() {
        return note;
    }

    @Override
    public void validate() {
        super.validate();
        if (getNote() == null) {
            throw new IllegalArgumentException("invalid (null) note");
        }
    }

    /**
     * @see org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent#getRuleInterfaceClass()
     */
    public Class<? extends BusinessRule> getRuleInterfaceClass() {
        return AddNoteRule.class;
    }

    /**
     * @see org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent#invokeRuleMethod(org.kuali.rice.krad.rules.rule.BusinessRule)
     */
    public boolean invokeRuleMethod(BusinessRule rule) {
        return ((AddNoteRule) rule).processAddNote(getDocument(), getNote());
    }
}
