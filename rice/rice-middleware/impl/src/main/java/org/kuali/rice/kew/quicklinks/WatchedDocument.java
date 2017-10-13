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
package org.kuali.rice.kew.quicklinks;

import org.kuali.rice.kew.api.KewApiConstants;

/**
 * Represents a document that is being watched from the Quick Links.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class WatchedDocument {
    private String documentHeaderId;
    private String documentStatusCode;
    private String documentTitle;
    public WatchedDocument(String documentHeaderId, String documentStatusCode, String documentTitle) {
        this.documentHeaderId = documentHeaderId;
        this.documentStatusCode = documentStatusCode;
        this.documentTitle = documentTitle;
    }

    /**
     *
     * Used by DocumentRouteHeaderValue.QuickLinks.FindWatchedDocumentsByInitiatorWorkflowId named query
     *
     * @param documentHeaderId
     * @param documentStatusShortCode
     * @param documentTitle
     */
    public WatchedDocument(Long documentHeaderId, String documentStatusShortCode, String documentTitle) {
        this(documentHeaderId.toString(), KewApiConstants.DOCUMENT_STATUSES.get(documentStatusShortCode), documentTitle);
    }

    public String getDocumentHeaderId() {
        return documentHeaderId;
    }
    public void setDocumentHeaderId(String documentHeaderId) {
        this.documentHeaderId = documentHeaderId;
    }
    public String getDocumentStatusCode() {
        return documentStatusCode;
    }
    public void setDocumentStatusCode(String documentStatusCode) {
        this.documentStatusCode = documentStatusCode;
    }
    public String getDocumentTitle() {
        return documentTitle;
    }
    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }
}
