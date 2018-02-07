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
package org.kuali.rice.kew.docsearch.service;

import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.api.document.search.DocumentSearchResults;
import org.kuali.rice.kew.impl.document.search.DocumentSearchGenerator;
import org.kuali.rice.kew.doctype.bo.DocumentType;

import java.util.List;


/**
 * Service for data access for document searches.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface DocumentSearchService {

    /**
     * This method performs a standard document search for the given criteria.
     *
     * @param principalId the id of the principal who is executing the search, this may be null to indicate the
     * search could be executed by an arbitrary user
     * @param criteria criteria to use to search documents
     * @return the results of the search, will never return null
     */
    DocumentSearchResults lookupDocuments(String principalId, DocumentSearchCriteria criteria);

    /**
     * Returns a saved search criteria, either explicitly named by the user, or saved automatically as a recent search
     * @param principalId the user principal id
     * @param key the user option key under which the criteria is saved
     * @return the DocumentSearchCriteria or null if not found
     */
    DocumentSearchCriteria getSavedSearchCriteria(String principalId, String key);

    /**
     * Returns an explicitly named saved search criteria
     * @param principalId the user principal id
     * @param savedSearchName the user-provided saved search name
     * @return the DocumentSearchCriteria or null if not found
     */
    DocumentSearchCriteria getNamedSearchCriteria(String principalId, String savedSearchName);

    /**
     * Clears all saved searches for the specified user (named and automatic)
     * @param principalId user principal id
     */
    void clearNamedSearches(String principalId);

    /**
     * Returns named saved searches for the specified user
     * @param principalId the user principal id
     * @return list of search key/label
     */
    List<KeyValue> getNamedSearches(String principalId);

    /**
     * Returns automatically saved recent searches for the specified user
     * @param principalId the user principal id
     * @return list of search key/label
     */
    List<KeyValue> getMostRecentSearches(String principalId);

    DocumentSearchCriteria clearCriteria(DocumentType documentType, DocumentSearchCriteria criteria);

    DocumentSearchGenerator getStandardDocumentSearchGenerator();

    void validateDocumentSearchCriteria(DocumentSearchGenerator docSearchGenerator, DocumentSearchCriteria.Builder criteria);

    /**
     * Returns the maximum number of results that should be returned from the document search.
     *
     * @param criteria the criteria in which to check for a max results value
     * @return the maximum number of results that should be returned from a document search
     */
    public int getMaxResultCap(DocumentSearchCriteria criteria);

    /**
     * Returns the number of results that should be returned from an additional fetch against
     * the document search.
     *
     * Default: {@link org.kuali.rice.kew.docsearch.dao.impl.DocumentSearchDAOJdbcImpl.DEFAULT_FETCH_MORE_ITERATION_LIMIT}
     * Override: {@link org.kuali.rice.kew.api.KewApiConstants#DOC_SEARCH_FETCH_MORE_ITERATION_LIMIT}
     * @return int
     */
    public int getFetchMoreIterationLimit();
}
