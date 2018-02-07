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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.edl.impl.EDLContext;
import org.kuali.rice.edl.impl.EDLModelComponent;
import org.kuali.rice.edl.impl.RequestParser;
import org.kuali.rice.edl.impl.UserAction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class RefreshFromLookupComponent implements EDLModelComponent {
	
	public void updateDOM(Document dom, Element configElement, EDLContext edlContext) {
		String command = edlContext.getUserAction().getAction();
		if (UserAction.ACTION_REFRESH_FROM_LOOKUP.equals(command)) {
			RequestParser requestParser = edlContext.getRequestParser();
			
			Element currentVersion = VersioningPreprocessor.findCurrentVersion(dom);
			// First, create a list of all of the <field> tags that match the parameter names returned from the lookup
			List<Element> fieldsToDelete = new ArrayList<Element>();
			NodeList fieldNodes = currentVersion.getElementsByTagName("field");
			
			// get the list of input parameters returned from the lookup so we can clear empty ones as well
			List<String> requestParameterNames = requestParser.getParameterNames();
			
			for (int i = 0; i < fieldNodes.getLength(); i++) {
				Element fieldNode = (Element) fieldNodes.item(i);
				String fieldName = fieldNode.getAttribute("name");
				if (requestParameterNames.contains(fieldName)) {
					fieldsToDelete.add(fieldNode);
				}
			}
			
			// Second, delete those nodes; we will rely on normal population to recreate those nodes
			// if the nodes weren't deleted, EDL would continue to display the old value on the generated output
			for (Element fieldToDelete : fieldsToDelete) {
				currentVersion.removeChild(fieldToDelete);
			}
		}
	}
}
