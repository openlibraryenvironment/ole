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
 * The result of the processing of a {@link DynamicNode}.  May contain a single node instance of
 * multiple node instances depending on whether or not the dynamic node has generated a split.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DynamicResult extends SimpleResult {

	private RouteNodeInstance nextNodeInstance;
	private List nextNodeInstances = new ArrayList();
	
	public DynamicResult(boolean complete, RouteNodeInstance nextNodeInstance) {
		super(complete);
		this.nextNodeInstance = nextNodeInstance;
	}

    public RouteNodeInstance getNextNodeInstance() {
        return nextNodeInstance;
    }
    
    public void setNextNodeInstance(RouteNodeInstance nextNodeInstance) {
        this.nextNodeInstance = nextNodeInstance;
    }
    
    public List getNextNodeInstances() {
        return nextNodeInstances;
    }
    public void setNextNodeInstances(List nextNodeInstances) {
        this.nextNodeInstances = nextNodeInstances;
    }
}
