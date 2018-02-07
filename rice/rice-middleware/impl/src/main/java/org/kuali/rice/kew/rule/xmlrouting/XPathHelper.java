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
package org.kuali.rice.kew.rule.xmlrouting;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFunctionResolver;

import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.w3c.dom.Node;


/**
 * Provides utilities for obtaining XPath instances which are "good-to-go" with access to the Workflow
 * namespace and custom XPath functions.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class XPathHelper {

	/**
	 * Creates a new XPath instance and initializes it with the WorkflowNamespaceContext and the
	 * WorkflowFunctionResolver.
	 */
	public static XPath newXPath() {
		XPath xPath = XPathFactory.newInstance().newXPath();
		xPath.setNamespaceContext(new WorkflowNamespaceContext());
		WorkflowFunctionResolver resolver = new WorkflowFunctionResolver();
		xPath.setXPathFunctionResolver(resolver); 
		resolver.setXpath(xPath);
		return xPath;
	}

	/**
	 * Creates a new XPath instances and initializes it with the WorkflowNamespaceContext and the
	 * WorkflowFunctionResolver.  Also sets the root node on the WorkflowFunctionResolver to 
	 * the given Node.  This is required for some of the functions in the function resolver
	 * to perform properly.
	 */
	public static XPath newXPath(Node rootNode) {
		XPath xPath = newXPath();
		WorkflowFunctionResolver resolver = extractFunctionResolver(xPath);
		resolver.setRootNode(rootNode);
		return xPath;
	}
	
	/**
	 * A utility to extract the WorkflowFunctionResolver from the given XPath instances.  If the XPath instance
	 * does not contain a WorkflowFunctionResolver, then this method will throw a WorkflowRuntimeException.
	 * 
	 * @throws WorkflowRuntimeException if the given XPath instance does not contain a WorklflowFunctionResolver
	 */
	public static WorkflowFunctionResolver extractFunctionResolver(XPath xPath) {
		XPathFunctionResolver resolver = xPath.getXPathFunctionResolver();
		if (!hasWorkflowFunctionResolver(xPath)) {
			throw new WorkflowRuntimeException("The XPathFunctionResolver on the given XPath instance is not an instance of WorkflowFunctionResolver, was: " + resolver);
		}
		return (WorkflowFunctionResolver)resolver;
	}
	
	/**
	 * Returns true if the given XPath instance has a WorkflowFunctionResolver, false otherwise.
	 */
	public static boolean hasWorkflowFunctionResolver(XPath xPath) {
		return xPath.getXPathFunctionResolver() instanceof WorkflowFunctionResolver;
	}
	
}
