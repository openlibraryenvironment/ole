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
package org.kuali.rice.edl.impl.components;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.edl.impl.EDLContext;
import org.kuali.rice.edl.impl.EDLModelComponent;
import org.kuali.rice.edl.impl.RequestParser;
import org.kuali.rice.edl.impl.UserAction;
import org.kuali.rice.krad.util.KRADConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * Handles establishing what action the user submitted.  It's important to normalize this because
 * the action could be submitted in the "userAction" request parameter, in the "command" request
 * parameter or not passed at all.
 * 
 * <p>This is primarily important in identifying whether the submission is the first-time "load"
 * of a document or an action being executed against an already loaded document.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class EstablishUserAction implements EDLModelComponent {
	
    public static final String USER_ACTION_PARAM = "userAction";
    public static final String COMMAND_PARAM = "command";
    
    public void updateDOM(Document dom, Element configElement, EDLContext edlContext) {
	RequestParser requestParser = edlContext.getRequestParser();

	List<String> params = requestParser.getParameterNames();
	
	/**
	 * IE does not process image buttons in the same way as other browsers. If you have 
	 *   <input name="userAction" value="performAction.unit_1" type=image src="images/searchicon.gif"/>
	 *   The actual request parameters that are sent are: userAction.x=9; userAction.y=22. The numbers
	 *   correspond to the location of the button on the screen. NO OTHER DATA IS SENT.  So in order 
	 *   for this to work for IE we need to change the name to include the value of userAction.
	 *   So we're now sending
	 *   <input name="userAction.performLookup.unit_1" value="This no longer matters" type=image src="images/searchicon.gif"/>
	 *   So we now need to parse out the userAction, the action, and the value. Which is what happens below.
	 *   The end result is a new parameter "userAction=performLookup.unit_1". 
	 */
	for(String param: params){
		if(param.startsWith("userAction.performLookup") && param.endsWith(".y")){			
			requestParser.setParameterValue(USER_ACTION_PARAM, param.substring("userAction.".length(), param.length()-2));
		}
	}
	String userAction = requestParser.getParameterValue(USER_ACTION_PARAM);

	if (StringUtils.isEmpty(userAction)) {
	    String command = requestParser.getParameterValue(COMMAND_PARAM);
	    if (!StringUtils.isEmpty(command)) {
		// from Workflow Quick Links, the "command" parameter will be passed with a value of "initiate"
		if (UserAction.ACTION_CREATE.equals(command)) {
		    userAction = UserAction.ACTION_CREATE;
		}
		// from Document Search/Action List a command parameter is passed but we want to load the document
		userAction = UserAction.ACTION_LOAD;
	    } else {
	    	String methodToCall = requestParser.getParameterValue(KRADConstants.DISPATCH_REQUEST_PARAMETER);
			if (StringUtils.equals(methodToCall, KRADConstants.RETURN_METHOD_TO_CALL)) {
				userAction = UserAction.ACTION_REFRESH_FROM_LOOKUP;
			}
			else {
				userAction = UserAction.ACTION_UNDEFINED;
		    }
	    }
	}
	edlContext.setUserAction(new UserAction(userAction));
    }
	
}
