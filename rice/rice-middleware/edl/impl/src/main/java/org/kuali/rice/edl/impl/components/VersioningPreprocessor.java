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

import org.kuali.rice.edl.impl.EDLContext;
import org.kuali.rice.edl.impl.EDLModelComponent;
import org.kuali.rice.edl.impl.EDLXmlUtils;
import org.kuali.rice.edl.impl.RequestParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * Versions the data element if necessary by checking 'currentVersion' param on request.  If this request is
 * a doc handler request this will configure the dom so the next request will cause the data to be incremented.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class VersioningPreprocessor implements EDLModelComponent {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(VersioningPreprocessor.class);

	public void updateDOM(Document dom, Element configElement, EDLContext edlContext) {

		RequestParser requestParser = edlContext.getRequestParser();

		boolean incrementVersion = edlContext.getUserAction().isIncrementVersionAction();
		boolean replaceVersion = edlContext.getUserAction().isReplaceVersionAction();

		Element edlContentElement = EDLXmlUtils.getEDLContent(dom, false);
		Element dataElement = EDLXmlUtils.getDataFromEDLDocument(edlContentElement, false);
		Element currentVersion = findCurrentVersion(dom);


		if (currentVersion == null) {
			Integer currentVersionCount = new Integer(0);
			currentVersion = EDLXmlUtils.getVersionFromData(dataElement, currentVersionCount);
		} else if (incrementVersion)  {
			currentVersion.getAttributeNode("current").setNodeValue("false");
			int currentVersionCount = new Integer(currentVersion.getAttribute("version")).intValue() + 1;
			EDLXmlUtils.getVersionFromData(dataElement, new Integer(currentVersionCount));
		} else if (replaceVersion) {
		    NodeList children = currentVersion.getChildNodes();
		    // important to store these in the last for removal later because we can't safely removeChild in the first for-loop below
		    List<Node> childrenToRemove = new ArrayList<Node>();
		    for (int index = 0; index < children.getLength(); index++) {
			childrenToRemove.add(children.item(index));
		    }
		    for (Node childToRemove : childrenToRemove) {
			currentVersion.removeChild(childToRemove);
		    }
		}
		requestParser.setAttribute("currentVersion", currentVersion.getAttribute("currentVersion"));
	}

	public static Element findCurrentVersion(Document dom) {
		Element edlContentElement = EDLXmlUtils.getEDLContent(dom, false);
		Element dataElement = EDLXmlUtils.getDataFromEDLDocument(edlContentElement, false);
		NodeList versionElements = dataElement.getElementsByTagName(EDLXmlUtils.VERSION_E);
		for (int i = 0; i < versionElements.getLength(); i++) {
			Element version = (Element) versionElements.item(i);
			Boolean currentVersion = new Boolean(version.getAttribute("current"));
			if (currentVersion.booleanValue()) {
				return version;
			}
		}
		return null;

	}

}
