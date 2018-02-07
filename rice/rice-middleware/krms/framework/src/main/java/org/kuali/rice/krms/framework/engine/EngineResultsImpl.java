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
package org.kuali.rice.krms.framework.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kuali.rice.krms.api.engine.EngineResults;
import org.kuali.rice.krms.api.engine.ResultEvent;

/**
 * An implementation of {@link EngineResults} using List<{@link ResultEvent}> for results and Map<String, Object> for
 * attributes
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class EngineResultsImpl implements EngineResults {
	
	private List<ResultEvent> results = new ArrayList<ResultEvent>();
	private Map<String, Object> attributes = new HashMap<String, Object>();
	
	@Override
	public void addResult(ResultEvent result) {
		results.add(result);
	}

    /**
     * Return a shallow copy of the list of ResultEvents.
     * @return a shallow copy of all the ResultEvents
     */
	@Override
	public List<ResultEvent> getAllResults() {		
		return new ArrayList<ResultEvent>(results); // shallow copy should be defensive enough
	}

    /**
     * Returns null, unimplemented.
     * @param index of the ResultEvent to return
     * @return null
     *
     * @deprecated use {@link #getAllResults()} instead, this method will be removed in a future version
     */
	@Override
    @Deprecated
	public ResultEvent getResultEvent(int index) {
		return getAllResults().get(index);
	}

	@Override
	public List<ResultEvent> getResultsOfType(String type) {
		// TODO Auto-generated method stub
		ArrayList<ResultEvent> newList = new ArrayList<ResultEvent>();
		if (type == null) return newList;
		for (int i=0; i<results.size(); i++){
			if (type.equalsIgnoreCase(results.get(i).getType())){
				newList.add(results.get(i));
			}
		}
		return newList;
	}
	
	/**
	 * @see org.kuali.rice.krms.api.engine.EngineResults#getAttribute(java.lang.String)
	 */
	@Override
	public Object getAttribute(String key) {
	    return attributes.get(key);
	}
	
	/**
	 * @see org.kuali.rice.krms.api.engine.EngineResults#setAttribute(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setAttribute(String key, Object attr) {
	    attributes.put(key, attr);
	}
	
}
