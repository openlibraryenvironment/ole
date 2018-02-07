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
package org.kuali.rice.kew.actions;

import java.util.ArrayList;
import java.util.List;

import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.engine.RouteHelper;
import org.kuali.rice.kew.engine.node.SplitNode;
import org.kuali.rice.kew.engine.node.SplitResult;

/**
 * This is a test split class that always returns "False". 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class SimpleBooleanSplitNode implements SplitNode {
	 private static org.apache.commons.logging.Log LOG = org.apache.commons.logging.LogFactory.getLog(SimpleBooleanSplitNode.class);

	    /**
	     * This method will look up the document being routed, if it is an instance of ResearchDocumentBase
	     * it will call answerSplitNodeQuestion on it passing the name of the route node.  The default implementation (currently)
	     * throws an UnsupportedOperationException for any input. If one wishes to support the SplitNode for a given document, the
	     * method should be overridden and return boolean T/F based on which of the branches ( always names "True" and "False" ) 
	     * KEW should route to based upon the name of the split node.
	     * 
	     * @see org.kuali.rice.kew.engine.node.SimpleNode#process(org.kuali.rice.kew.engine.RouteContext, org.kuali.rice.kew.engine.RouteHelper)
	     */
	    
	    public SplitResult process(RouteContext context, RouteHelper helper ) throws Exception {
	       return this.booleanToSplitResult(false);
	    }
	    
	    
	    /**
	     * Converts a boolean value to SplitResult where the branch name is "True" or "False" based on the value of the given boolean
	     * @param b a boolean to convert to a SplitResult
	     * @return the converted SplitResult
	     */
	    protected SplitResult booleanToSplitResult(boolean b) {
	        List<String> branches = new ArrayList<String>();
	        final String branchName = b ? "True" : "False";
	        branches.add(branchName);
	        return new SplitResult(branches);
	    }

}
