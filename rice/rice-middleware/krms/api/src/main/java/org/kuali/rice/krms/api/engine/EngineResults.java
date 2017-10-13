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
package org.kuali.rice.krms.api.engine;

import java.util.List;

/**
 * Results of an {@link Engine}'s execution
 *
 * @see ResultEvent
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface EngineResults {

    /**
     * Return the ResultEvent for the given index
     * @param index of the ResultEvent to return
     * @return {@link ResultEvent} whose index was given
     *
     * @deprecated use {@link #getAllResults()} instead, this method will be removed in a future version
     */
    @Deprecated
	public ResultEvent getResultEvent(int index);

    /**
     * Return the list of ResultEvents
     * @return List<ResultEvent> all the results
     */
	public List<ResultEvent> getAllResults();

    /**
     * Return the ResultEvents of the given type
     * @param type of result events to return
     * @return List<ResultEvent> of the given type
     */
	public List<ResultEvent> getResultsOfType(String type);

    /**
     * Return the attribute of the given key
     * @param key to return the attribute of
     * @return Object that is the attribute for the given key
     */
	public Object getAttribute(String key);

    /**
     * Set the attribute of the given values
     * @param key to set the given attribute of
     * @param attribute to set as the given key's attribute
     */
	public void setAttribute(String key, Object attribute);

    /**
     * Add the given {@link ResultEvent}
     * @param result to add
     */
	public void addResult(ResultEvent result);
}
