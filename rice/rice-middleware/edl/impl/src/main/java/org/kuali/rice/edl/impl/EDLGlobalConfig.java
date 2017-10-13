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
package org.kuali.rice.edl.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * Store global EDL config information parsed from config file.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class EDLGlobalConfig {

	private Map preProcessors = new HashMap();
	private Map postProcessors = new HashMap();
	private Map stateComponents = new HashMap();
    private Map configProcessors = new HashMap();

    public void addPreProcessor(String preProcessorName, Element element) {
		try {
			preProcessors.put(Class.forName(preProcessorName), element);	
		} catch (ClassNotFoundException ce) {
			throw new WorkflowRuntimeException("Class " + preProcessorName + " not found.", ce);
		}
	}
	
	public void addPostProcessor(String postProcessorName, Element configElement) {
		try {
			postProcessors.put(Class.forName(postProcessorName), configElement);
		} catch (ClassNotFoundException ce) {
			throw new WorkflowRuntimeException("Class " + postProcessorName + " not found.", ce);
		}
	}
	
	public void addStateComponent(String stateComponentName, Element configElement) {
		try {
			stateComponents.put(Class.forName(stateComponentName), configElement);
		} catch (ClassNotFoundException ce) {
			throw new WorkflowRuntimeException("Class " + stateComponentName + " not found.", ce);
		}
	}
	
	public void addConfigProcessor(String xpathExpression, String configProcessorName) {
		Class configProcessor;
		try {
			configProcessor = Class.forName(configProcessorName);
		} catch (ClassNotFoundException ce) {
			throw new WorkflowRuntimeException("Class " + configProcessorName + " not found.", ce);
		}
		if (configProcessors.containsKey(configProcessor)) {
			throw new WorkflowRuntimeException("Config processor " + configProcessorName + " attempted to register an xpath expression twice.  " +
					"The expression being used is " + configProcessors.get(configProcessor));
		} else {
			configProcessors.put(configProcessor, xpathExpression);	
		}
	}
	
	public Map getPreProcessors() {
		return preProcessors;
	}
	
	public Map getPostProcessors() {
		return postProcessors;
	}
	
	public Map getStateComponents() {
		return stateComponents;
	}

	public Class getConfigProcessor(Node configElement, EDLContext edlContext) {
		if (configElement instanceof Element) {
            XPath xpath = null;
            if (edlContext != null) {
                xpath = edlContext.getXpath();
            } else {
                xpath = XPathFactory.newInstance().newXPath();
            }
			String xpathExpression = "";
			try {
				for (Iterator iter = configProcessors.entrySet().iterator(); iter.hasNext();) {
					Map.Entry configProcessor = (Map.Entry) iter.next();
					xpathExpression = (String) configProcessor.getKey();
					Boolean match = (Boolean) xpath.evaluate(xpathExpression, configElement, XPathConstants.BOOLEAN);
					if (match.booleanValue()) {
						return (Class) configProcessor.getValue();
					}
				}
				return null;
			} catch (XPathExpressionException e) {
				throw new WorkflowRuntimeException("Unable to evaluate xpath expression " + xpathExpression, e);
			} catch (Exception ie) {
				throw new WorkflowRuntimeException(ie);
			}
		}
		return null;
	}

	public Map getConfigProcessors() {
		return configProcessors;
	}

	public void setConfigProcessors(Map configProcessors) {
		this.configProcessors = configProcessors;
	}

	public void setPostProcessors(Map postProcessors) {
		this.postProcessors = postProcessors;
	}

	public void setPreProcessors(Map preProcessors) {
		this.preProcessors = preProcessors;
	}

	public void setStateComponents(Map stateComponents) {
		this.stateComponents = stateComponents;
	}
	
	
}
