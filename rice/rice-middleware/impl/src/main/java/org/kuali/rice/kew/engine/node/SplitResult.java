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
 * The result of the processing of a {@link SplitNode}.  Contains a List of branch names that
 * the document's route path should split to.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SplitResult extends SimpleResult {

	private List<String> branchNames = new ArrayList<String>();
	
	public SplitResult(List<String> branchNames) {
		super(true);
		this.branchNames = branchNames;
	}

	public List<String> getBranchNames() {
		return branchNames;
	}

	protected void setBranchNames(List<String> branchNames) {
		this.branchNames = branchNames;
	}
	
	
	
}
