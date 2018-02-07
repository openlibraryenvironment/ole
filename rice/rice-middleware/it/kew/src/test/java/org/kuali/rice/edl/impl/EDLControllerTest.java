/**
 * Copyright 2005-2013 The Kuali Foundation
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.edl.impl.service.EDocLiteService;
import org.kuali.rice.edl.impl.service.EdlServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.w3c.dom.Element;


public class EDLControllerTest extends KEWTestCase {

	protected void loadTestData() throws Exception {
		super.loadXmlFile("widgets.xml");
		super.loadXmlFile("edlstyle.xml");
		super.loadXmlFile("FakeyEDL.xml");
	}

	@Test public void testEDLControllerCreation() throws Exception {
		ConfigContext.getCurrentContextConfig().putProperty(Config.EDL_CONFIG_LOCATION, "classpath:org/kuali/rice/kew/edl/TestEDLConfig.xml");


		EDLController edlController = getEDLService().getEDLControllerUsingEdlName("FakeyEDL");
		edlController.setEdlContext(getEDLcontext());
		assertNotNull("There should be a default dom in the edlcontoller", edlController.getDefaultDOM());
		edlController.notifyComponents();

		assertTrue("PreProcess component should have been notified", TestPreProcessor.isContacted());
		assertTrue("PostProcessor component should have been notified", TestPostProcessor.isContacted());
		assertTrue("State component should have been notified", TestStateComponent.isContacted());
		assertTrue("ConfigProcess component should have been notified", TestConfigProcessor.isContacted());

		//make sure they all have the correct config element passed in
		Element preProcessorConfigElement = (Element) ((Map.Entry)edlController.getEdlGlobalConfig().getPreProcessors().entrySet().iterator().next()).getKey();
		assertEquals("PreProcessor config element is of the wrong class", "org.kuali.rice.edl.impl.TestPreProcessor", preProcessorConfigElement.getFirstChild().getNodeValue());

		Element postProcessorConfigElement = (Element) ((Map.Entry)edlController.getEdlGlobalConfig().getPostProcessors().entrySet().iterator().next()).getKey();
		assertEquals("PostProcessor config element is of the wrong class", "org.kuali.rice.edl.impl.TestPostProcessor", postProcessorConfigElement.getFirstChild().getNodeValue());

		Element stateConfigElement = (Element) ((Map.Entry)edlController.getEdlGlobalConfig().getStateComponents().entrySet().iterator().next()).getKey();
		assertEquals("State config element is of the wrong class", "org.kuali.rice.edl.impl.TestStateComponent", stateConfigElement.getFirstChild().getNodeValue());

		Element configProcessorConfigElement = (Element) ((Map.Entry)edlController.getConfigProcessors().entrySet().iterator().next()).getKey();
		assertEquals("Config processor element should be fielDef", "fieldDef", configProcessorConfigElement.getNodeName());

	}

	private EDLContext getEDLcontext() {
		EDLContext edlContext = new EDLContext();
		edlContext.setRequestParser(new RequestParser(new MockHttpServletRequest()));
		return edlContext;
	}

	private EDocLiteService getEDLService() {
		return EdlServiceLocator.getEDocLiteService();
	}

}
