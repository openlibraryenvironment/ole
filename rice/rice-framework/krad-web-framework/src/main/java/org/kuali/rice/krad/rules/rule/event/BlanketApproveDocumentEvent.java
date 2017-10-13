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

/**
 * This class represents the blanketApprove event that is part of an eDoc in Kuali. This could be triggered when a user presses the
 * blanketApprove button for a given document enroute or it could happen when another piece of code calls the blanketApprove method
 * in the document service.
 *
 *
 */
public final class BlanketApproveDocumentEvent extends ApproveDocumentEvent {
    /**
     * Constructs an BlanketApproveDocumentEvent with the specified errorPathPrefix and document
     *
     * @param errorPathPrefix
     * @param document
     */
    public BlanketApproveDocumentEvent(String errorPathPrefix, Document document) {
        super("blanketApprove", errorPathPrefix, document);
    }

    /**
     * Constructs a BlanketApproveDocumentEvent with the given document
     *
     * @param document
     */
    public BlanketApproveDocumentEvent(Document document) {
        super("blanketApprove", "", document);
    }
}
