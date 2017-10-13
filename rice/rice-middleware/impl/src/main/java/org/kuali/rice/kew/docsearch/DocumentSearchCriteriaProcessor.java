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
package org.kuali.rice.kew.docsearch;

import java.util.List;

import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kns.web.ui.Row;

/**
 * Used by the document search helper to produce rows to render for the document search screen.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface DocumentSearchCriteriaProcessor {

    /**
     * Constructs the list of rows and fields to display in the lookup criteria section of the document search screen.
     * The documentType parameter passed to this method will generally be supplied in cases where the document type name
     * was supplied to the lookup via a query parameter or when refreshing the criteria after making a change to it
     * (like the user doing a document type lookup to fill in the value and then returning it back to the document search).
     *
     * <p>This method should look at the given document type (if there is one) to determine if there are any custom
     * rows to render for that document type.  It should also look at the request for detailed and super user search
     * and adjust the rows accordingly for either of those conditions.</p>
     *
     * @param documentType supplies the document type in use on this search if one is specified, will be null otherwise
     * @param defaultRows the default set of rows that are supplied from the data dictionary for the document search
     * @param detailed indicates whether or not a detailed search has been requested
     * @param superSearch indicates whether or not a super user search has been requested
     *
     * @return the rows to render is the criteria section of the document search.
     */
	List<Row> getRows(DocumentType documentType, List<Row> defaultRows, boolean detailed, boolean superSearch);

}
