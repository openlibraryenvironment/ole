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

import org.kuali.rice.edl.impl.EDLXmlUtils;
import org.kuali.rice.edl.impl.RequestParser;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * Matches university ID param to UserService to validate universityId.  Returns error message if 
 * universityId does NOT match.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class UniversityIdWorkflowEDLConfigComponent extends SimpleWorkflowEDLConfigComponent {
  
	private boolean required;

	@Override
	public Element getReplacementConfigElement(Element element) {
		Element replacementEl = (Element)element.cloneNode(true);
		Element type = (Element)((NodeList)replacementEl.getElementsByTagName(EDLXmlUtils.TYPE_E)).item(0);
		type.setTextContent("text");
		
		//find the validation element if required is true set a boolean and determin if blanks
		//are allowed based on that
		Element validation = (Element)((NodeList)replacementEl.getElementsByTagName(EDLXmlUtils.VALIDATION_E)).item(0);
		if (validation != null && validation.getAttribute("required").equals("true")) {
			required = true;
		}
		return replacementEl;
	}

	@Override
	public String getErrorMessage(Element originalConfigElement, RequestParser requestParser, MatchingParam param) {
		
		/*
         * <documentContent>
         *   <applicationContent>
         *     <data edlName="Test2">
         *       <version current="true" date="Thu Sep 14 14:44:43 EDT 2006" version="0">
         *         <field name="networkId">
         *           <value>jitrue</value>
         *         </field>
         *         <field name="universityId">
         *           <value>0000394389</value>
         *         </field>
         *       </version>
         *     </data>
         *   </applicationContent>
         * </documentContent>
		 */

		if (param.getParamValue().length() == 0 && required == true) {
			//empty and required so send error
			return ("University ID is a required field");
		} else if (param.getParamValue().length() == 0 && required == false) { 
			//empty but not required then just return 
			return null;			
		} else {
			//not blank validate as normal whether required or not
			String employeeId = param.getParamValue();
			
			Principal principal = KimApiServiceLocator.getIdentityService().getPrincipal(employeeId);
			if (principal == null) {
				return ("The value " + employeeId + " is an invalid University ID");
			}

			
		}
		return null;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}


}

