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
import org.kuali.rice.edl.impl.EDLXmlUtils;
import org.kuali.rice.edl.impl.RequestParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * Handles setting the current page in the EDL XML.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class PageHandler implements EDLModelComponent {

	public static final String CURRENT_PAGE = "currentPage";
	public static final String PREVIOUS_PAGE = "previousPage";
	public static final String GOTO_PAGE = "edl.gotoPage";
	
	public void updateDOM(Document dom, Element configElement, EDLContext edlContext) {
	    String currentPage = edlContext.getRequestParser().getParameterValue("edl." + CURRENT_PAGE);
	    String previousPage = edlContext.getRequestParser().getParameterValue("edl." + PREVIOUS_PAGE);
	    String gotoPage = findGotoPage(edlContext.getRequestParser(), currentPage);
	    if (!StringUtils.isBlank(gotoPage)) {
		previousPage = currentPage;
		currentPage = gotoPage;
	    }
	    establishCurrentPage(dom, currentPage);
	    establishPreviousPage(dom, previousPage);
	}
	
	/**
	 * Find parameter that looks like gotoPage:<pageName>
	 */
	private String findGotoPage(RequestParser requestParser, String currentPage) {
	    List<String> parameterNames = requestParser.getParameterNames();
	    for (String parameterName : parameterNames) {
		if (parameterName.startsWith(GOTO_PAGE + ":")) {
		    return parameterName.split(":")[1];
		}
	    }
	    return null;
	}

	private void establishCurrentPage(Document dom, String currentPage) {
	    if (!StringUtils.isBlank(currentPage)) {
		Element currentPageElement = EDLXmlUtils.getOrCreateChildElement(dom.getDocumentElement(), CURRENT_PAGE, true);
		currentPageElement.appendChild(dom.createTextNode(currentPage));
	    }
	}
	

	private void establishPreviousPage(Document dom, String previousPage) {
	    if (!StringUtils.isBlank(previousPage)) {
		Element previousPageElement = EDLXmlUtils.getOrCreateChildElement(dom.getDocumentElement(), PREVIOUS_PAGE, true);
		previousPageElement.appendChild(dom.createTextNode(previousPage));
	    }
	}
	    

}
