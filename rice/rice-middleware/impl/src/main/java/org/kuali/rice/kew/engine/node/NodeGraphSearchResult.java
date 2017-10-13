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

import java.util.ArrayList;
import java.util.List;

/**
 * The result of a node graph search.  Indentifies the node instance which was found in the search and also the
 * path followed to find the node.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class NodeGraphSearchResult {
	
	private RouteNodeInstance resultNodeInstance;
	private List path = new ArrayList();
	
	public NodeGraphSearchResult(RouteNodeInstance resultNodeInstance, List path) {
		this.resultNodeInstance = resultNodeInstance;
		this.path = path;
	}

	public RouteNodeInstance getResultNodeInstance() {
		return resultNodeInstance;
	}

	public void setResultNodeInstance(RouteNodeInstance resultNodeInstance) {
		this.resultNodeInstance = resultNodeInstance;
	}

	public List getPath() {
		return path;
	}

	public void setPath(List path) {
		this.path = path;
	}
	
	

}
