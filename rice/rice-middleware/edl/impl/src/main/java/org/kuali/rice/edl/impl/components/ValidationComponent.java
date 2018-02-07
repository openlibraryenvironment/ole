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
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.edl.impl.EDLContext;
import org.kuali.rice.edl.impl.EDLModelComponent;
import org.kuali.rice.edl.impl.EDLXmlUtils;
import org.kuali.rice.edl.impl.RequestParser;
import org.kuali.rice.edl.impl.service.EdlServiceLocator;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.rule.xmlrouting.XPathHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * Executes validations that are defined on the EDL Definitions.  These validation exist in a form
 * similiar to the following:
 *
 * <validations>
 *   <validation type="xpath">
 *     <expression>wf:field('grade') = 'other' and not(wf:empty(wf:field('otherGrade'))</expression>
 *     <message>Other Grade is required when grade is marked as 'other'</message>
 *   </validation>
 * </validations>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ValidationComponent extends SimpleWorkflowEDLConfigComponent implements EDLModelComponent  {

	private static final String XPATH_TYPE = "xpath";
	private EDLContext edlContext;

	public void updateDOM(Document dom, Element configElement, EDLContext edlContext) {
		if (edlContext.getUserAction().isValidatableAction()) {
			try {
				Document edlDef = EdlServiceLocator.getEDocLiteService().getDefinitionXml(edlContext.getEdocLiteAssociation());
				List<EDLValidation> validations = parseValidations(edlDef);
				if (!validations.isEmpty()) {
					XPath xpath = XPathHelper.newXPath(dom);
					for (EDLValidation validation : validations) {
						executeValidation(xpath, dom, validation, edlContext);
					}
				}
			} catch (Exception e) {
				if (e instanceof RuntimeException) {
					throw (RuntimeException)e;
				}
				throw new WorkflowRuntimeException("Failed to execute EDL validations.", e);
			}
		}
	}

	protected List<EDLValidation> parseValidations(Document document) throws Exception {
		List<EDLValidation> validations = new ArrayList<EDLValidation>();
		XPath xpath = XPathHelper.newXPath(document);
		NodeList validationNodes = (NodeList)xpath.evaluate("/edl/validations/validation", document, XPathConstants.NODESET);
		for (int index = 0; index < validationNodes.getLength(); index++) {
			Element validationElem = (Element)validationNodes.item(index);
			EDLValidation validation = new EDLValidation();
			String type = validationElem.getAttribute("type");
			String key = validationElem.getAttribute("key");
			String expression = EDLXmlUtils.getChildElementTextValue(validationElem, "expression");
			String message = EDLXmlUtils.getChildElementTextValue(validationElem, "message");
			if (StringUtils.isBlank(type)) {
				throw new WorkflowRuntimeException("An improperly configured validation was found with an empty type.");
			}
			if (StringUtils.isBlank(expression)) {
				throw new WorkflowRuntimeException("An improperly configured validation was found with an empty expression.");
			}
			if (StringUtils.isBlank(message)) {
				throw new WorkflowRuntimeException("An improperly configured validation was found with an empty message.");
			}
			validation.setType(type);
			validation.setKey(key);
			validation.setExpression(expression);
			validation.setMessage(message);
			validations.add(validation);
		}
		return validations;
	}

	protected void executeValidation(XPath xpath, Document dom, EDLValidation validation, EDLContext edlContext) throws Exception {
		// TODO: in the future, allow this to be pluggable, hardcode for now
		if (XPATH_TYPE.equals(validation.getType())) {
			Boolean result = (Boolean)xpath.evaluate(validation.getExpression(), dom, XPathConstants.BOOLEAN);
			// if validation returns false, we'll flag the error
			if (!result) {
				String key = validation.getKey();
				if (!StringUtils.isEmpty(key)) {
					Map<String, String> fieldErrors = (Map<String, String>)edlContext.getRequestParser().getAttribute(RequestParser.GLOBAL_FIELD_ERRORS_KEY);
					fieldErrors.put(key, validation.getMessage());

					// set invalid attribute to true on corresponding field
					//TODO remove - handled this in the widgets
//					Element edlElement = EDLXmlUtils.getEDLContent(dom, false);
//					Element edlSubElement = EDLXmlUtils.getOrCreateChildElement(edlElement, "data", true);
//					NodeList versionNodes = edlSubElement.getChildNodes();
//					for (int i = 0; i < versionNodes.getLength(); i++) {
//						Element version = (Element) versionNodes.item(i);
//						String current = version.getAttribute("current");
//						if (current == "true") {
//							NodeList fieldNodes = version.getChildNodes();
//							for (int j = 0; j < fieldNodes.getLength(); j++) {
//								Element field = (Element) fieldNodes.item(j);
//								String fieldName = field.getAttribute("name");
//								if(fieldName.equals(key)) {
//									field.setAttribute("invalid", "true");
//									break;
//								}
//							}
//						}
//					}

				} else {
					List globalErrors = (List)edlContext.getRequestParser().getAttribute(RequestParser.GLOBAL_ERRORS_KEY);
					globalErrors.add(validation.getMessage());
				}
				edlContext.setInError(true);
			}
		} else {
			throw new WorkflowRuntimeException("Illegal validation type specified.  Only 'xpath' is currently supported.");
		}
	}


}
