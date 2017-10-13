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

import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.test.TestUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class EDLGlobalConfigFactoryTest extends KEWTestCase {

	/**
	 * Test a positive case global parsing..
	 * 
	 * @throws Exception
	 */
	@Test public void testEDLGlobalConfigParsing() throws Exception {

        EDLGlobalConfig edlGlobalConfig = EDLGlobalConfigFactory.createEDLGlobalConfig("classpath:org/kuali/rice/kew/edl/TestEDLConfig.xml");
		Map preProcessors = edlGlobalConfig.getPreProcessors();
		Map postProcessors = edlGlobalConfig.getPostProcessors();
		Map stateComps = edlGlobalConfig.getStateComponents();

        InputStream fakeyEDL = TestUtilities.loadResource(this.getClass(), "FakeyEDL.xml");
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fakeyEDL);
		
		Object configProcessorInst = null;
		NodeList edlDefinitionNodes = doc.getElementsByTagName("fieldDef");
		for (int i = 0; i < edlDefinitionNodes.getLength(); i++) {
			Node definitionNode = edlDefinitionNodes.item(i);
			Class configClass = (Class)edlGlobalConfig.getConfigProcessor(definitionNode, null);
			if (configClass != null) {
				configProcessorInst = configClass.newInstance();
			}
			
		}
		
		assertTrue("should be 1 preProcessor", preProcessors.size() == 1);
		assertTrue("should be 1 postProcessor", postProcessors.size() == 1);
		assertTrue("should be 1 stateComps", stateComps.size() == 1);
		assertTrue("Object made from config proces, arg1", configProcessorInst instanceof TestConfigProcessor);
		
	}
	

}
