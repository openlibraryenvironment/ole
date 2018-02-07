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
package org.kuali.rice.krad.service;

import java.sql.Timestamp;

import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.web.form.DocumentFormBase;

/**
 * Service API for persisting <code>Document</code> form content and
 * retrieving back
 *
 * @deprecated (Deprecated and removed from use in KRAD  (KULRICE-9149)
 *
 * <p>
 * Used as an extension to session support. If a session times out, the doucment contents
 * can be retrieved from the persistence storage and work resumed
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Deprecated
public interface SessionDocumentService {

    /**
     * Retrieves a document from the user session for the given document id
     */
    public WorkflowDocument getDocumentFromSession(UserSession userSession, String docId);

    /**
     * This method places a document into the user session.
     */
    public void addDocumentToUserSession(UserSession userSession, WorkflowDocument document);

     /**
     * Delete DocumentFormBase from session and database.
     *
     * @param documentNumber
     * @param docFormKey
     * @param userSession
     * @throws
     */
    public void purgeDocumentForm(String documentNumber, String docFormKey, UserSession userSession, String ipAddress);

	/**
     * Delete KualiDocumentFormBase from session and database.
     *
     * @param documentNumber
     * @throws
     */
    public void purgeAllSessionDocuments(Timestamp expirationDate);

    /**
     * This method stores a UifFormBase into session and database
     *
     * @param form
     * @param userSession
     * @param ipAddress
     */
    public void setDocumentForm(DocumentFormBase form, UserSession userSession, String ipAddress);

    /**
     * Returns DocumentFormBase object from the db
     *
     * @param documentNumber
     * @param docFormKey
     * @param userSession
     * @param ipAddress
     * @return
     */
    public DocumentFormBase getDocumentForm(String documentNumber, String docFormKey, UserSession userSession,
            String ipAddress);
}
