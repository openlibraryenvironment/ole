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
import java.util.Iterator;
import java.util.List;

import org.kuali.rice.edl.impl.EDLContext;
import org.kuali.rice.edl.impl.EDLModelComponent;
import org.kuali.rice.edl.impl.EDLXmlUtils;
import org.kuali.rice.edl.impl.RequestParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;



/**
 * Matches request params to fields defined in edl configs.  Places matched value on the dom for rendering.  Places 
 * currentDefinitionElement definition ( the element used to get the param from the request) element on the
 * dom for rendering.
 * 
 * This is the base EDL Config Component for dealing with defined elements in an edl definition.  Most 
 * custom edl element behavior can be achieved by subclassing this.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class SimpleWorkflowEDLConfigComponent implements EDLModelComponent {

	protected Element definitionElement;
	private EDLContext edlContext;

	public void updateDOM(Document dom, Element currentDefinitionElement, EDLContext edlContext) {

		RequestParser requestParser = edlContext.getRequestParser();
		this.edlContext = edlContext;
		
		
		this.definitionElement = currentDefinitionElement;
		Element configElementForDOM = getReplacementConfigElement(currentDefinitionElement);
		if (configElementForDOM == null) {
			configElementForDOM = currentDefinitionElement;
		}

		Element edlContentElement = EDLXmlUtils.getEDLContent(dom, false);
		edlContentElement.appendChild(configElementForDOM);
		
		Element currentVersion = VersioningPreprocessor.findCurrentVersion(dom);
		
		List matchingParams = getMatchingParams(configElementForDOM, requestParser, edlContext);
		for (Iterator iter = matchingParams.iterator(); iter.hasNext();) {
			MatchingParam matchingParam = (MatchingParam) iter.next();
			EDLXmlUtils.createFieldDataElement(currentVersion, matchingParam);
		}
	}
	
	public Element getReplacementConfigElement(Element element) {
		return null;
	}

	public List getMatchingParams(Element originalConfigElement, RequestParser requestParser, EDLContext edlContext) {
		List params = new ArrayList();
		String paramName = originalConfigElement.getAttribute("name");
		String[] paramValues = requestParser.getParameterValues(paramName);
		if (paramValues == null) {
			return params;
		}
		for (int i = 0; i < paramValues.length; i++) {
			String value = paramValues[i];
			MatchingParam matchingParam = new MatchingParam();
			matchingParam.setParamName(paramName);
			matchingParam.setParamValue(value);
			String errorMessage = getErrorMessage(originalConfigElement, requestParser, matchingParam);
			if (errorMessage != null) {
				matchingParam.setError(Boolean.TRUE);
				matchingParam.setErrorMessage(errorMessage);
				edlContext.setInError(true);
			}
			params.add(matchingParam);
		}
		return params;
	}

	public String getErrorMessage(Element originalConfigElement, RequestParser requestParser, MatchingParam matchingParam) {
		return null;
	}

	public Element getDefinitionElement() {
		return definitionElement;
	}

	public void setDefinitionElement(Element definitionElement) {
		this.definitionElement = definitionElement;
	}

	public EDLContext getEdlContext() {
		return edlContext;
	}

	public void setEdlContext(EDLContext edlContext) {
		this.edlContext = edlContext;
	}
}
