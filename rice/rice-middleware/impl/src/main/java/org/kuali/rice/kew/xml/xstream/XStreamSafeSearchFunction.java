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
package org.kuali.rice.kew.xml.xstream;

import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;

import org.w3c.dom.Node;

/**
 * An XPathFunction which will run XStream safe XPath queries.
 * 
 * @see XStreamSafeEvaluator
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class XStreamSafeSearchFunction implements XPathFunction {

	private final Node rootNode;
	private XPath xpath;
	private static XStreamSafeEvaluator evaluator = new XStreamSafeEvaluator();
	
	public XStreamSafeSearchFunction(Node rootNode, XPath xpath) {
		this.rootNode = rootNode;
		this.xpath = xpath;
	}
	
	public Object evaluate(List parameters) throws XPathFunctionException {
		String xPathExpression = getXPathExpressionParameter(parameters);
		evaluator.setXpath(xpath);
		//Node rootSearchNode = getRootSearchNodeParameter(parameters);
		try {
			return evaluator.evaluate(xPathExpression, rootNode);
		} catch (XPathExpressionException e) {
			throw new XPathFunctionException(e);
		}
	}
	
	private String getXPathExpressionParameter(List parameters) throws XPathFunctionException {
		if (parameters.size() < 1) {
			throw new XPathFunctionException("First parameter must be an XPath expression.");
		}
		if (!(parameters.get(0) instanceof String)) {
			throw new XPathFunctionException("First parameter must be an XPath expression String");
		}
		return (String)parameters.get(0);
	}

	public XPath getXpath() {
		return xpath;
	}

	public void setXpath(XPath xpath) {
		this.xpath = xpath;
	}
	
	/*private Node getRootSearchNodeParameter(List parameters) throws XPathFunctionException {
		if (parameters.size() < 2) {
			throw new XPathFunctionException("Second parameter should be root node and is required");
		}
		System.out.println(parameters.get(1));
		if (!(parameters.get(1) instanceof Node)) {
			throw new XPathFunctionException("Second parameter should be an instance of Node (try using the root() XPath function).");
		}
		return (Node)parameters.get(1);
	}*/

}
