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
import org.kuali.rice.core.api.util.xml.XmlJotter;
import org.kuali.rice.edl.impl.bo.EDocLiteAssociation;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.transform.Templates;
import java.util.Iterator;
import java.util.Map;


/**
 * Responsible for notifying components associated with a particular EDL definition.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class EDLController {
	
	private static final Logger LOG = Logger.getLogger(EDLController.class);

	private EDocLiteAssociation edocLiteAssociation;
	private Templates style;
	private Map configProcessors;
	private Map preProcessors;
	private Map postProcessors;
	private Map stateComponents;
	private EDLGlobalConfig edlGlobalConfig;
	private Document defaultDOM;
	private EDLContext edlContext;

	public Document notifyComponents() {
		
		try {
			updateDOMWithProcessors(defaultDOM, preProcessors);
			updateDOMWithProcessors(defaultDOM, stateComponents);
			updateDOMWithProcessors(defaultDOM, configProcessors);
			updateDOMWithProcessors(defaultDOM, stateComponents);
			updateDOMWithProcessors(defaultDOM, postProcessors);	
		} catch (Exception e) {
			throw new WorkflowRuntimeException(e);
		}
		
		
		return defaultDOM;
	}
	
	private void updateDOMWithProcessors(Document dom, Map processors) throws Exception {
		
		for (Iterator iter = processors.entrySet().iterator(); iter.hasNext();) {
			Map.Entry processorEntry = (Map.Entry) iter.next();
			Element configElement = (Element)processorEntry.getKey();
			EDLModelComponent eldModelComp = (EDLModelComponent)((Class)processorEntry.getValue()).newInstance();
			eldModelComp.updateDOM(dom, configElement, edlContext);
			if (LOG.isDebugEnabled()) {
				LOG.debug("Just completed notification to component " + eldModelComp + " doc content looks like. " + XmlJotter.jotNode(dom));
			}
			
		}
	}

	public Map getConfigProcessors() {
		return configProcessors;
	}
	public void setConfigProcessors(Map configProcessors) {
		this.configProcessors = configProcessors;
	}
	public EDLGlobalConfig getEdlGlobalConfig() {
		return edlGlobalConfig;
	}
	public void setEdlGlobalConfig(EDLGlobalConfig edlConfig) {
		this.edlGlobalConfig = edlConfig;
	}
	public Templates getStyle() {
		return style;
	}
	public void setStyle(Templates style) {
		this.style = style;
	}
	public EDocLiteAssociation getEdocLiteAssociation() {
		return edocLiteAssociation;
	}
	public void setEdocLiteAssociation(EDocLiteAssociation edocLiteAssociation) {
		this.edocLiteAssociation = edocLiteAssociation;
	}
	public Document getDefaultDOM() {
		return defaultDOM;
	}
	public void setDefaultDOM(Document defaultDOM) {
		this.defaultDOM = defaultDOM;
	}
	public EDLContext getEdlContext() {
		return edlContext;
	}
	public void setEdlContext(EDLContext edlContext) {
		this.edlContext = edlContext;
	}

	public Map getPostProcessors() {
		return postProcessors;
	}

	public void setPostProcessors(Map postProcessors) {
		this.postProcessors = postProcessors;
	}

	public Map getPreProcessors() {
		return preProcessors;
	}

	public void setPreProcessors(Map preProcessors) {
		this.preProcessors = preProcessors;
	}

	public Map getStateComponents() {
		return stateComponents;
	}

	public void setStateComponents(Map stateComponents) {
		this.stateComponents = stateComponents;
	}
}
