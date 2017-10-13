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
package org.kuali.rice.core.api.criteria;


import java.util.List;

/**
 * Contains a collection of results from a query.  This interface exists as a
 * utility for services that want to implement it to represents results from
 * criteria-based (or other) queries.
 *
 * <p>
 * Also may  provide information related to the count of rows returned
 * by the query as well as whether or not the query returned all available results.
 * </p>
 *
 * <p>
 * All the information in this interface is populated depending on the information
 * requested via the {@link QueryByCriteria}.
 * </p>
 *
 * @param <T> the type of the objects contained within the results list
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface QueryResults<T> {

    /**
	 * Return the list of results that are contained within.  This list can
	 * be empty but it should never be null.
	 *
	 * @return the list of results
	 */
	List<T> getResults();

	/**
	 * Gets the total number of records that match the executed query.  Note
	 * that this number will not necessarily match the number of results
	 * returned by {@link #getResults()} as the query may cut off the number
	 * of records returned by the actual query request.  In these cases, the
	 * total row count represents the total number of results which would be
	 * returned by the query if there was no cap on the results returned (i.e.
	 * the equivalent of the result of a "count" query in SQL).
	 * 
	 * <p>
     * The total row count is optional depending on whether or not the
	 * client requested the total row count when invoking the query.  The client
     * requests this information by setting the {@link CountFlag#INCLUDE} or
     * {@link CountFlag#ONLY} on the {@link QueryByCriteria}.  It's also
     * possible that the query is unable to produce a total row count depending
	 * on the back-end implementation, in which cases this value will also not
	 * be available.
     * </p>
     *
     * <p>
     * Will never be less than 0.
     * <p>
	 * 
	 * @return the total number of rows, or null if the total row count was not
	 * requested by the query or could not be determined 
	 */
	Integer getTotalRowCount();

	/**
	 * Indicates if there are more results available for the query immediately
	 * following the last result that was returned.  In this case, the records
	 * returned in {@link #getResults()} will not include the complete result
	 * set for the query.  This could be because the query only requested a
	 * certain number of rows, or that the query couldn't return the number
	 * of rows that were requested.
	 * 
	 * <p>
     * It is intended that this value be used to facilitate paging or
	 * reporting in the client in cases where that is desired.
     * </p>
     *
     * <p>
     * This information will only be available if the client sets a limit on the
     * results returned.  This limit is set with the maxResults field on the
     * {@link QueryByCriteria}.
     * </p>
	 * 
	 * @return true if there are more results available for the query, false otherwise
	 */
	boolean isMoreResultsAvailable();
}
