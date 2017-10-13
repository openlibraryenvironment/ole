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

import org.apache.log4j.Logger;
import org.kuali.rice.core.api.util.xml.XmlHelper;
import org.kuali.rice.core.api.util.xml.XmlJotter;
import org.kuali.rice.edl.impl.bo.EDocLiteAssociation;
import org.kuali.rice.edl.impl.service.EDocLiteService;
import org.kuali.rice.edl.impl.service.EdlServiceLocator;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.krad.util.GlobalVariables;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Templates;
import javax.xml.xpath.XPathFactory;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Creates EDL controllers.  The parsed config is a definition name related to
 * a Map containing config element and their associated class.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public final class EDLControllerFactory {

    private EDLControllerFactory() {
        throw new UnsupportedOperationException("do not call");
    }

	private static final Logger LOG = Logger.getLogger(EDLControllerFactory.class);

	public static EDLController createEDLController(EDocLiteAssociation edlAssociation, EDLGlobalConfig edlGlobalConfig) {
        EDLController edlController = new EDLController();
		edlController.setEdocLiteAssociation(edlAssociation);
        edlController.setEdlContext(getPreEDLContext(edlController));

        try {
			edlController.setEdlGlobalConfig(edlGlobalConfig);
			edlController.setDefaultDOM(getDefaultDOM(edlAssociation));
			loadConfigProcessors(edlController, edlGlobalConfig);
			loadPreProcessors(edlController, edlGlobalConfig);
			loadPostProcessor(edlController, edlGlobalConfig);
			loadStateComponents(edlController, edlGlobalConfig);
			loadStyle(edlController);
			
		} catch (Exception e) {
            String edl = null;
            if (edlAssociation != null) {
                edl = edlAssociation.getEdlName();
            }
            String message = "Error creating controller for EDL" + (edl == null ? "" : ": " + edl);
            LOG.error(message, e);
			throw new WorkflowRuntimeException("Problems creating controller for EDL: " + edl, e);
		}

		return edlController;
	}

	public static EDLController createEDLController(EDocLiteAssociation edlAssociation, EDLGlobalConfig edlGlobalConfig, DocumentRouteHeaderValue document) {
		EDLController edlController = createEDLController(edlAssociation, edlGlobalConfig);
		try {
			Document defaultDom = edlController.getDefaultDOM();
			Document documentDom = XmlHelper.readXml(document.getDocContent());
			// get the data node and import it into our default DOM
			Element documentData = (Element) documentDom.getElementsByTagName(EDLXmlUtils.DATA_E).item(0);
			if (documentData != null) {
				Element defaultDomEDL = EDLXmlUtils.getEDLContent(defaultDom, false);
				Element defaultDomData = (Element) defaultDomEDL.getElementsByTagName(EDLXmlUtils.DATA_E).item(0);
				defaultDomEDL.replaceChild(defaultDom.importNode(documentData, true), defaultDomData);
			}
			if (LOG.isDebugEnabled()) {
				LOG.debug("Created default Node from document id " + document.getDocumentId() + " content " + XmlJotter.jotNode(defaultDom));
			}
		} catch (Exception e) {
			throw new WorkflowRuntimeException("Problems creating controller for EDL " + edlAssociation.getEdlName() + " document " + document.getDocumentId(), e);
		}
		return edlController;
	}

	private static synchronized void loadStyle(EDLController edlController) throws Exception {
		EDocLiteService edlService = getEDLService();
		final Templates styleSheet = edlService.getStyleAsTranslet(edlController.getEdocLiteAssociation().getStyle());
		edlController.setStyle(styleSheet);
	}
	
	private static synchronized void loadPreProcessors(EDLController edlController, EDLGlobalConfig edlGlobalConfig) {
		edlController.setPreProcessors(cloneConfigMap(edlGlobalConfig.getPreProcessors(), edlController.getDefaultDOM()));
	}
	
	private static synchronized void loadPostProcessor(EDLController edlController, EDLGlobalConfig edlGlobalConfig) {
		edlController.setPostProcessors(cloneConfigMap(edlGlobalConfig.getPostProcessors(), edlController.getDefaultDOM()));
	}
	
	private static synchronized void loadStateComponents(EDLController edlController, EDLGlobalConfig edlGlobalConfig) {
		edlController.setStateComponents(cloneConfigMap(edlGlobalConfig.getStateComponents(), edlController.getDefaultDOM()));
	}

	private static synchronized void loadConfigProcessors(final EDLController edlController, final EDLGlobalConfig edlGlobalConfig) throws Exception {
		EDocLiteAssociation edlAssociation = edlController.getEdocLiteAssociation();
        // these are classes mapped to the conf element from the edlconfig.
        Document document = getEDLService().getDefinitionXml(edlAssociation);
        Element definitionElement = (Element) document.getFirstChild();

        Map configProcessorMappings = new LinkedHashMap();
        edlController.setEdlGlobalConfig(edlGlobalConfig);
        NodeList edlDefinitionNodes = definitionElement.getChildNodes();
        for (int i = 0; i < edlDefinitionNodes.getLength(); i++) {
            Node definitionNode = edlDefinitionNodes.item(i);
            Class configProcessorClass = edlGlobalConfig.getConfigProcessor(definitionNode, edlController.getEdlContext());
            if (configProcessorClass != null) {
                configProcessorMappings.put(definitionNode, configProcessorClass);
            }
        }
        edlController.setConfigProcessors(cloneConfigMap(configProcessorMappings, edlController.getDefaultDOM()));
	}
	
	private static synchronized Map cloneConfigMap(Map configMap, Document defaultDom) {
		Map tempConfigProcessors = new LinkedHashMap();
		for (Iterator iter = configMap.entrySet().iterator(); iter.hasNext();) {
			Map.Entry configProcessorMapping = (Map.Entry) iter.next();
			tempConfigProcessors.put(defaultDom.importNode((Node)configProcessorMapping.getKey(), true), configProcessorMapping.getValue());
		}
		return tempConfigProcessors;
	}

	private static EDocLiteService getEDLService() {
		return EdlServiceLocator.getEDocLiteService();
	}

	private static Document getDefaultDOM(EDocLiteAssociation edlAssociation) throws Exception {
		Document dom = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		Element rootElement = dom.createElement("documentContent"); // this is a
		// throwback
		// to some
		// original
		// madness
		// to get EDL routing over a year ago we need to look into this being
		// eliminated.
		dom.appendChild(rootElement);
		Element edlContentElement = EDLXmlUtils.getEDLContent(dom, true);
		EDLXmlUtils.getDataFromEDLDocument(edlContentElement, true);
		
		// get the data element that was just created ***jitrue***
		Element edlData = EDLXmlUtils.getChildElement(edlContentElement, EDLXmlUtils.DATA_E);
		// set edlName attribute on data element of default DOM ***jitrue***
		edlData.setAttribute("edlName", edlAssociation.getEdlName());
		
		return dom;
	}

    public static EDLContext getPreEDLContext(EDLController edlController) {
        EDLContext edlContext = new EDLContext();
        edlContext.setEdocLiteAssociation(edlController.getEdocLiteAssociation());
        edlContext.setUserSession(GlobalVariables.getUserSession());
        edlContext.setXpath(XPathFactory.newInstance().newXPath());
        return edlContext;
    }
}
