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

import org.kuali.rice.core.api.util.RiceUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Builds a EDLGlobalConfig.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class EDLGlobalConfigFactory {

	private static final String CONFIG_PROCESSOR_XPATH_XPRSN = "xpathExp";
	private static final String CONFIG_PROCESSOR_CLASS_NAME = "className";


	public static EDLGlobalConfig createEDLGlobalConfig(String edlConfigLocation) throws Exception {
		EDLGlobalConfig edlConfig = new EDLGlobalConfig();
		InputStream configStream = RiceUtilities.getResourceAsStream(edlConfigLocation);
		Document configXml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(configStream);

		edlConfig.setPreProcessors(createProcessorMap("//preProcessors/preProcessor", configXml));
		edlConfig.setPostProcessors(createProcessorMap("//postProcessors/postProcessor", configXml));
		edlConfig.setStateComponents(createProcessorMap("//stateComponents/stateComponent", configXml));
		edlConfig.setConfigProcessors(createConfigProcessorMap("//configProcessors/configProcessor", configXml));

		return edlConfig;
	}

	private static Map createProcessorMap(String xpathExpression, Document doc) throws Exception {
		Map processors = new LinkedHashMap();//preserve parsing order
		XPath xpath = XPathFactory.newInstance().newXPath();
		NodeList globalProcessorDeclarations = (NodeList)xpath.evaluate(xpathExpression, doc, XPathConstants.NODESET);
		for (int i = 0; i < globalProcessorDeclarations.getLength(); i++) {
			Element globalProcessorDeclaraion = (Element)globalProcessorDeclarations.item(i);
			processors.put(globalProcessorDeclaraion, Class.forName(globalProcessorDeclaraion.getFirstChild().getNodeValue()));
		}
		return processors;
	}

	private static Map createConfigProcessorMap(String xpathExpression, Document doc) throws Exception {
		Map configProcessors = new LinkedHashMap();//preserve parsing order
		XPath xpath = XPathFactory.newInstance().newXPath();
		NodeList globalConfigProcessorDeclarations = (NodeList)xpath.evaluate(xpathExpression, doc, XPathConstants.NODESET);
		for (int i = 0; i < globalConfigProcessorDeclarations.getLength(); i++) {
			Element globalProcessorDeclaraion = (Element)globalConfigProcessorDeclarations.item(i);
			String xpathEx = (String)xpath.evaluate(CONFIG_PROCESSOR_XPATH_XPRSN, globalProcessorDeclaraion, XPathConstants.STRING);
			String className = (String)xpath.evaluate(CONFIG_PROCESSOR_CLASS_NAME, globalProcessorDeclaraion, XPathConstants.STRING);
			configProcessors.put(xpathEx, Class.forName(className));
		}
		return configProcessors;
	}
}
