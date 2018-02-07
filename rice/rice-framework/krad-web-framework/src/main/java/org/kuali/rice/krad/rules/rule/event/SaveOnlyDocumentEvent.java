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

import org.kuali.rice.krad.document.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the save event that is part of an eDoc in Kuali. This could be triggered when a user presses the save
 * button for a given document or it could happen when another piece of code calls the save method in the document service. This
 * event does not trigger sub-events for validation.
 *
 *
 */
public class SaveOnlyDocumentEvent extends SaveDocumentEvent {
    /**
     * Constructs a SaveOnlyDocumentEvent with the specified errorPathPrefix and document
     *
     * @param document
     * @param errorPathPrefix
     */
    public SaveOnlyDocumentEvent(String errorPathPrefix, Document document) {
        this("creating save event using no generated events for document " + getDocumentId(document), errorPathPrefix, document);
    }

    /**
     * Constructs a SaveDocumentEvent with the given document
     *
     * @param document
     */
    public SaveOnlyDocumentEvent(Document document) {
        this("", document);
    }

    /**
     * @see org.kuali.rice.krad.rules.rule.event.KualiDocumentEventBase#KualiDocumentEventBase(java.lang.String, java.lang.String, org.kuali.rice.krad.document.Document)
     */
    public SaveOnlyDocumentEvent(String description, String errorPathPrefix, Document document) {
	super(description, errorPathPrefix, document);
    }

    /**
     * This overridden method returns an empty list always
     *
     * @see org.kuali.rice.krad.rules.rule.event.SaveDocumentEvent#generateEvents()
     */
    @Override
    public List<KualiDocumentEvent> generateEvents() {
	return new ArrayList<KualiDocumentEvent>();
    }


}
