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
package org.kuali.rice.kew.framework.document.security;

import org.kuali.rice.kew.api.document.Document;

import java.io.Serializable;

/**
 * This is an attribute used to implement custom document security for document search and the route log.
 * SecurityAttributes are configured to be associated with the document type against which they should
 * be applied.  For each route log or row that is returned from a document search, this authorization
 * methods will be executed. 
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface DocumentSecurityAttribute extends Serializable {

    /**
     * Determines whether or not a principal is authorized to see information about a given document.
     *
     * @param principalId the principalId for which to check authorization
     * @param document the document for which to check security
     *
     * @return true if the principal is authorized to view the document, false otherwise
     */
    boolean isAuthorizedForDocument(String principalId, Document document);

}
