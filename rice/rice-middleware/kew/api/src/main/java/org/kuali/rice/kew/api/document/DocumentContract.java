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
package org.kuali.rice.kew.api.document;

import java.util.Map;

import org.joda.time.DateTime;

/**
 * Provides read-only access to Document meta-data.
 */
public interface DocumentContract {

    /**
     * Retrieve the document id
     * @return the document id
     */
    String getDocumentId();

    /**
     * Retrieve the document status
     * @return the document status
     */
    DocumentStatus getStatus();

    /**
     * Retrieve the document creation date
     * @return the document creation date or null
     */
    DateTime getDateCreated();

    /**
     * Retrieve the document last-modified date
     * @return the document last-modified date or null
     */
    DateTime getDateLastModified();

    /**
     * Retrieve the document approval date
     * @return the document approval date or null
     */
    DateTime getDateApproved();

    /**
     * Retrieve the document finalization date
     * @return the document finalization date or null
     */
    DateTime getDateFinalized();

    /**
     * Retrieve the document title
     * @return the document title
     */
    String getTitle();

    /**
     * Retrieve the application document id.  The Application Document Id is used to record an application-relevant
     * id for the workflow document.
     * @return the application document id
     */
    String getApplicationDocumentId();

    /**
     * Retrieve the initiator principal id
     * @return the initiator principal id
     */
    String getInitiatorPrincipalId();

    /**
     * Retrieve the router principal id
     * @return the router principal id
     */
    String getRoutedByPrincipalId();

    /**
     * Retrieve the name of the type of this document
     * @return the name of the type of this document
     */
    String getDocumentTypeName();

    /**
     * Retrieve the id of the type of this document
     * @return the id of the type of this document
     */
    String getDocumentTypeId();

    /**
     * Retrieve the document handler url
     * @return the document handler url
     */
    String getDocumentHandlerUrl();

    /**
     * Retrieve the application document status. The Application Document Status is used
     * to track document/applicaiton specific statuses
     * @return the application document status
     */
    String getApplicationDocumentStatus();

    /**
     * Retrieve the last application document status transition date.  The Application Document Status date is
     * the date the application document status last transitioned.
     * @return the application document status date
     */
    DateTime getApplicationDocumentStatusDate();

    /**
     * Retrieve the currently defined internal workflow engine variables for the document
     * NOTE: use of workflow engine variables is an advanced technique requiring specific crafting of the
     * workflow document routing; these variables will not be useful for the majority of workflow use cases
     * @return the currently defined workflow engine variables for the document
     */
    Map<String, String> getVariables();
}
