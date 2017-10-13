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

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.edl.impl.EDLContext;
import org.kuali.rice.edl.impl.EDLModelComponent;
import org.kuali.rice.kew.rule.xmlrouting.XPathHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



/**
 * Looks at a field definition for a select field and attempts to analyze and resolve any valuesGroups based on document data.
 * The result of this is to take a fieldDef that has a valuesGroup and either remove it if it does not match or replace the
 * <valuesGroup> element with a series of <values> elements that include the values contained with the values group.
 * 
 * <p>This allows for <select> fields to be dependent upon one another. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SelectControlEDLComponent implements EDLModelComponent {

	public void updateDOM(Document dom, Element currentDefinitionElement, EDLContext edlContext) {
	    Element currentVersion = VersioningPreprocessor.findCurrentVersion(dom);
	    XPath xPath = XPathHelper.newXPath(dom);
	    try {
		NodeList selectFieldDefs = (NodeList)xPath.evaluate("//fieldDef[display/type = 'select' and display/valuesGroup] | //fieldDef[display/type = 'select_refresh' and display/valuesGroup]", dom, XPathConstants.NODESET);
		for (int fIndex = 0; fIndex < selectFieldDefs.getLength(); fIndex++) {
		    Element fieldDef = (Element)selectFieldDefs.item(fIndex);
		NodeList valuesGroups = (NodeList)xPath.evaluate("./display/valuesGroup", fieldDef, XPathConstants.NODESET);
		for (int index = 0; index < valuesGroups.getLength(); index++) {
		    Element valuesGroupElem = (Element)valuesGroups.item(index);
		    NodeList dependsOnFields = (NodeList)xPath.evaluate("./dependsOn/field", valuesGroupElem, XPathConstants.NODESET);
		    String fieldEvalExpression = "";
		    for (int dIndex = 0; dIndex < dependsOnFields.getLength(); dIndex++) {
			if (!StringUtils.isBlank(fieldEvalExpression)) {
			    fieldEvalExpression += " and ";
			}
			Element fieldElem = (Element)dependsOnFields.item(dIndex);
			String name = fieldElem.getAttribute("name");
			String value = fieldElem.getTextContent();
			fieldEvalExpression += "./field[@name='" + name + "']/value = '" + value + "'";
		    }
		    if ((Boolean)xPath.evaluate(fieldEvalExpression, currentVersion, XPathConstants.BOOLEAN)) {
			includeValuesGroup(valuesGroupElem);
		    } else {
			// remove the valuesGroup as it did not match
			valuesGroupElem.getParentNode().removeChild(valuesGroupElem);
		    }
		}
		}
	    } catch (XPathExpressionException e) {
		throw new RuntimeException("Failed to evaluate xpath expression.", e);
	    }
	}
	
	protected void includeValuesGroup(Element valuesGroupElem) {
	    Element valuesGroupParent = (Element)valuesGroupElem.getParentNode();
	    NodeList valuesGroupChildren = valuesGroupElem.getChildNodes();
	    
	    for (int index = 0; index < valuesGroupChildren.getLength(); index++) {
		Node item = valuesGroupChildren.item(index);
		if (Node.ELEMENT_NODE == item.getNodeType() && item.getNodeName().equals("values")) {
		    valuesGroupParent.insertBefore(item, valuesGroupElem);
		}
	    }
	    valuesGroupParent.removeChild(valuesGroupElem);
	}	
	
}
