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
package org.kuali.rice.kew.api.document.search;

import java.util.List;

/**
 * Defines the contract for results returned from a document search.  A document search returns multiple results, each
 * one representing a document and it's document attributes.  The results additional include information about the
 * criteria that was used to execute the search, as well as whether or not it was modified after it was submitted for
 * execution of the document search.
 *
 * <p>Additionally, results from the document search might be filtered for a particular principal for security purposes.
 * In these cases, the document search results include information on the number of results that were filtered out
 * because the principal executing the search did not have permission to view them.</p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface DocumentSearchResultsContract {

    /**
     * Returns the unmodifiable list of search results.  Each of these result objects represents a document returned
     * from the search.
     *
     * @return an unmodifiable list of search results, will never be null but may be null
     */
    List<? extends DocumentSearchResultContract> getSearchResults();

    /**
     * Returns the criteria that was used to execute the search.  This may not be the same criteria that was submitted
     * to the document search api since it is possible for criteria to be modified by backend processing of the
     * submitted criteria.  See {@link #isCriteriaModified()} for more information.
     *
     * @return the criteria used to execute this search, will never be null
     */
    DocumentSearchCriteriaContract getCriteria();

    /**
     * Returns true if the criteria on this search result was modified from the original criteria submitted by the
     * executor of the document search.  This may happen in cases where the document search implementation modifies the
     * given criteria.  This may be possible through document search customization hooks, or may happen as part of a
     * process of "defaulting" certain portions of the criteria.
     *
     * @return a boolean indicating whether or not the criteria was modified from it's original form prior to search
     * execution
     */
    boolean isCriteriaModified();

    /**
     * Returns true if the results of the search returned more rows then the document search framework is allowed to
     * return back to the caller of the api.  The implementation of the document search is permitted to cap the number
     * of results returned and a result cap can also be specified on the criteria itself.
     *
     * @return true if there are more results available for the requested search then can be included in the list of
     * results
     */
    boolean isOverThreshold();

    /**
     * Return the number of results that matched the criteria but are not included on this results instance because they
     * principal executing the document search did not have permissions to view them.
     *
     * @return the number of results that were filtered for security reasons
     */
    int getNumberOfSecurityFilteredResults();

}
