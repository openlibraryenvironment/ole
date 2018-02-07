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
package org.kuali.rice.kew.engine.node;

import java.util.Collection;

/**
 * The criteria defining parameters to a search through a document's node graph.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class NodeGraphSearchCriteria {

	// search directions
	public static final int SEARCH_DIRECTION_FORWARD = 1;
	public static final int SEARCH_DIRECTION_BACKWARD = 2;
	public static final int SEARCH_DIRECTION_BOTH = 3;
	
	private int searchDirection = SEARCH_DIRECTION_FORWARD;
	private Collection startingNodeInstances;
	private NodeMatcher matcher;
	
	public NodeGraphSearchCriteria(int searchDirection, Collection startingNodeInstances, NodeMatcher matcher) {
		if (startingNodeInstances == null || startingNodeInstances.isEmpty()) {
			throw new IllegalArgumentException("Starting node instances were empty.  At least one starting node instance must be specified in order to perform a search.");
		}
		this.searchDirection = searchDirection;
		this.startingNodeInstances = startingNodeInstances;
		this.matcher = matcher;
	}
	
	public NodeGraphSearchCriteria(int searchDirection, Collection startingNodeInstances, String nodeName) {
		this(searchDirection, startingNodeInstances, new NodeNameMatcher(nodeName));
	}

	public NodeMatcher getMatcher() {
		return matcher;
	}

	public Collection getStartingNodeInstances() {
		return startingNodeInstances;
	}
	
	public int getSearchDirection() {
		return searchDirection;
	}
	
}
