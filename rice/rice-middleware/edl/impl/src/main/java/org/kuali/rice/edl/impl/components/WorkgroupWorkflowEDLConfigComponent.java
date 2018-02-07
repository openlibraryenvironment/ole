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
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.util.Utilities;
import org.kuali.rice.kew.workgroup.GroupId;
import org.kuali.rice.kew.workgroup.GroupNameId;
import org.kuali.rice.kim.api.group.Group;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Looks up workgroup param to validate workgroup exists and is active.  Returns error message if workgroup is does not exist or is not active.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class WorkgroupWorkflowEDLConfigComponent extends SimpleWorkflowEDLConfigComponent {
	
	private boolean required = false;
	
	protected GroupId resolveId(String id) {
		String groupName = Utilities.parseGroupName(id);
    	String namespace = Utilities.parseGroupNamespaceCode(id);
		return new GroupNameId(namespace, groupName);
	}
	
	@Override
	public Element getReplacementConfigElement(Element element) {
		Element replacementEl = (Element)element.cloneNode(true);
		Element type = (Element)((NodeList)replacementEl.getElementsByTagName(EDLXmlUtils.TYPE_E)).item(0);
		type.setTextContent("text");
		
		//find the validation element if required is true set a boolean and determine if blanks
		//are allowed based on that
		Element validation = (Element)((NodeList)replacementEl.getElementsByTagName(EDLXmlUtils.VALIDATION_E)).item(0);
		if (validation != null && validation.getAttribute("required").equals("true")) {
			required = true;
		}
		return replacementEl;
	}
	
	@Override
	public String getErrorMessage(Element originalConfigElement, RequestParser requestParser, MatchingParam param) {
		
		if (param.getParamValue().length() == 0 && required == true) {
			//empty and required so send error
			return ("Workgroup is a required field");
		} else if (param.getParamValue().length() == 0 && required == false) { 
			//empty but not required then just return 
			return null;
		}
		String wrkgrpName = param.getParamValue();
		// TODO add support for namespace here!
		Group group = KEWServiceLocator.getIdentityHelperService().getGroup(resolveId(wrkgrpName));
		if (group == null) {
		    return ("Group " + wrkgrpName + " not found.");
		}
		if (!group.isActive()) {
		    return ("Group " + wrkgrpName + " is not an active group.");
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
