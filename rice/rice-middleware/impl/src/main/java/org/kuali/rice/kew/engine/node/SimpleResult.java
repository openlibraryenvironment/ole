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

/**
 * The result of the execution of a {@link SimpleNode}.  Simply wraps a boolean as
 * to whether or not the node has completed.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SimpleResult implements ProcessResult {

	private boolean complete = false;
	
	public SimpleResult(boolean complete) {
		this.complete = complete;
	}
	
	public boolean isComplete() {
		return complete;
	}
	
	protected void setComplete(boolean complete) {
		this.complete = complete;
	}
	
}
