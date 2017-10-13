/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.kew.server;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.engine.RouteHelper;
import org.kuali.rice.kew.engine.node.SimpleSplitNode;
import org.kuali.rice.kew.engine.node.SplitResult;

/**
 * This is a description of what this class does - delyea don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@Ignore
public class TestSplitNode extends SimpleSplitNode {

	private static boolean leftBranch = true;
	private static boolean rightBranch = true;
	
	@Override
	public SplitResult process(RouteContext routeContext,
			RouteHelper routeHelper) throws Exception {
		return new SplitResult(getBranchNames());
	}
	
	public List<String> getBranchNames() {
		List<String> branchNames = new ArrayList<String>();
		if (isLeftBranch()) {
			branchNames.add("Left");
		}
		if (isRightBranch()) {
			branchNames.add("Right");
		}
		return branchNames;
	}
	public static void setLeftBranch(boolean leftBranch) {
		TestSplitNode.leftBranch = leftBranch;
	}
	public static boolean isLeftBranch() {
		return TestSplitNode.leftBranch;
	}
	public static void setRightBranch(boolean rightBranch) {
		TestSplitNode.rightBranch = rightBranch;
	}		
	public static boolean isRightBranch() {
		return TestSplitNode.rightBranch;
	}  	
}
