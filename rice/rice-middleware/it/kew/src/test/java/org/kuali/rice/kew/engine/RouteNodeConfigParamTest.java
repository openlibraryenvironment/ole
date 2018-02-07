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
package org.kuali.rice.kew.engine;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.util.Utilities;
import org.xml.sax.SAXException;

/**
 * Tests the parsing of route node config params 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RouteNodeConfigParamTest extends KEWTestCase {

    private static final Logger LOG = Logger.getLogger(RouteNodeConfigParamTest.class);

    protected void loadTestData() throws Exception {
        loadXmlFile("RouteNodeConfigParams.xml");
    }

    /**
     * Asserts that specified route node definition has the specified config parameter
     */
    protected void assertHasConfigParam(RouteNode routeNodeDef, String key, String value) {
        Map<String, String> cfgMap = Utilities.getKeyValueCollectionAsMap(routeNodeDef.getConfigParams());
        try {
            XMLUnit.setIgnoreWhitespace(true);
            XMLAssert.assertXMLEqual(value, cfgMap.get(key));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test public void testRouteNodeConfigParams() {
        DocumentType docType = KEWServiceLocator.getDocumentTypeService().findByName("RouteNodeConfigParams");
        assertNotNull(docType);
        assertNotNull(docType.getDocumentTypeId());

        // adhoc node
        RouteNode routeNodeDef = KEWServiceLocator.getRouteNodeService().findRouteNodeByName(docType.getDocumentTypeId(), "AdHoc");
        assertNotNull(routeNodeDef);
        assertHasConfigParam(routeNodeDef, RouteNode.CONTENT_FRAGMENT_CFG_KEY, "<start name=\"AdHoc\"><activationType>P</activationType></start>");
        assertHasConfigParam(routeNodeDef, "activationType", "P");

        routeNodeDef = KEWServiceLocator.getRouteNodeService().findRouteNodeByName(docType.getDocumentTypeId(), "setStartedVar");
        assertNotNull(routeNodeDef);
        assertHasConfigParam(routeNodeDef, RouteNode.CONTENT_FRAGMENT_CFG_KEY, "<simple name=\"setStartedVar\"><type>org.kuali.rice.kew.engine.node.var.SetVarNode</type><name>started</name><value>startedVariableValue</value></simple>");
        assertHasConfigParam(routeNodeDef, "type", "org.kuali.rice.kew.engine.node.var.SetVarNode");
        assertHasConfigParam(routeNodeDef, "name", "started");
        assertHasConfigParam(routeNodeDef, "value", "startedVariableValue");

        routeNodeDef = KEWServiceLocator.getRouteNodeService().findRouteNodeByName(docType.getDocumentTypeId(), "setCopiedVar");
        assertNotNull(routeNodeDef);
        assertHasConfigParam(routeNodeDef, RouteNode.CONTENT_FRAGMENT_CFG_KEY, "<simple name=\"setCopiedVar\"><type>org.kuali.rice.kew.engine.node.var.SetVarNode</type><name>copiedVar</name><value>var:started</value></simple>");
        assertHasConfigParam(routeNodeDef, "type", "org.kuali.rice.kew.engine.node.var.SetVarNode");
        assertHasConfigParam(routeNodeDef, "name", "copiedVar");
        assertHasConfigParam(routeNodeDef, "value", "var:started");

        routeNodeDef = KEWServiceLocator.getRouteNodeService().findRouteNodeByName(docType.getDocumentTypeId(), "PreApprovalTestOne");
        assertNotNull(routeNodeDef);
        assertHasConfigParam(routeNodeDef, RouteNode.CONTENT_FRAGMENT_CFG_KEY, "<requests name=\"PreApprovalTestOne\"><activationType>S</activationType><ruleSelector>Named</ruleSelector><ruleName>TestRule1</ruleName></requests>");
        assertHasConfigParam(routeNodeDef, "activationType", "S");
        assertHasConfigParam(routeNodeDef, "ruleSelector", "Named");
        assertHasConfigParam(routeNodeDef, "ruleName", "TestRule1");
        
        routeNodeDef = KEWServiceLocator.getRouteNodeService().findRouteNodeByName(docType.getDocumentTypeId(), "setEndedVar");
        assertNotNull(routeNodeDef);
        assertHasConfigParam(routeNodeDef, RouteNode.CONTENT_FRAGMENT_CFG_KEY, "<simple name=\"setEndedVar\"><type>org.kuali.rice.kew.engine.node.var.SetVarNode</type><name>ended</name><value>endedVariableValue</value></simple>");
        assertHasConfigParam(routeNodeDef, "type", "org.kuali.rice.kew.engine.node.var.SetVarNode");
        assertHasConfigParam(routeNodeDef, "name", "ended");
        assertHasConfigParam(routeNodeDef, "value", "endedVariableValue");
        
        routeNodeDef = KEWServiceLocator.getRouteNodeService().findRouteNodeByName(docType.getDocumentTypeId(), "setGoogleVar");
        assertNotNull(routeNodeDef);
        assertHasConfigParam(routeNodeDef, RouteNode.CONTENT_FRAGMENT_CFG_KEY, "<simple name=\"setGoogleVar\"><type>org.kuali.rice.kew.engine.node.var.SetVarNode</type><name>google</name><value>url:http://google.com</value></simple>");
        assertHasConfigParam(routeNodeDef, "type", "org.kuali.rice.kew.engine.node.var.SetVarNode");
        assertHasConfigParam(routeNodeDef, "name", "google");
        assertHasConfigParam(routeNodeDef, "value", "url:http://google.com");

        routeNodeDef = KEWServiceLocator.getRouteNodeService().findRouteNodeByName(docType.getDocumentTypeId(), "setXPathVar");
        assertNotNull(routeNodeDef);
        assertHasConfigParam(routeNodeDef, RouteNode.CONTENT_FRAGMENT_CFG_KEY, "<simple name=\"setXPathVar\"><type>org.kuali.rice.kew.engine.node.var.SetVarNode</type><name>xpath</name><value>xpath:concat(local-name(//documentContent),$ended)</value></simple>");
        assertHasConfigParam(routeNodeDef, "type", "org.kuali.rice.kew.engine.node.var.SetVarNode");
        assertHasConfigParam(routeNodeDef, "name", "xpath");
        assertHasConfigParam(routeNodeDef, "value", "xpath:concat(local-name(//documentContent),$ended)");

        routeNodeDef = KEWServiceLocator.getRouteNodeService().findRouteNodeByName(docType.getDocumentTypeId(), "resetStartedVar");
        assertNotNull(routeNodeDef);
        assertHasConfigParam(routeNodeDef, RouteNode.CONTENT_FRAGMENT_CFG_KEY, "<simple name=\"resetStartedVar\"><type>org.kuali.rice.kew.engine.node.var.SetVarNode</type><name>started</name><value>aNewStartedVariableValue</value></simple>");
        assertHasConfigParam(routeNodeDef, "type", "org.kuali.rice.kew.engine.node.var.SetVarNode");
        assertHasConfigParam(routeNodeDef, "name", "started");
        assertHasConfigParam(routeNodeDef, "value", "aNewStartedVariableValue");

        routeNodeDef = KEWServiceLocator.getRouteNodeService().findRouteNodeByName(docType.getDocumentTypeId(), "logNode");
        assertNotNull(routeNodeDef);
        assertHasConfigParam(routeNodeDef, RouteNode.CONTENT_FRAGMENT_CFG_KEY, "<simple name=\"logNode\"><type>org.kuali.rice.kew.engine.node.LogNode</type><message>var:xpath</message></simple>");
        assertHasConfigParam(routeNodeDef, "type", "org.kuali.rice.kew.engine.node.LogNode");
        assertHasConfigParam(routeNodeDef, "message", "var:xpath");

        routeNodeDef = KEWServiceLocator.getRouteNodeService().findRouteNodeByName(docType.getDocumentTypeId(), "logNode2");
        assertNotNull(routeNodeDef);
        assertHasConfigParam(routeNodeDef, RouteNode.CONTENT_FRAGMENT_CFG_KEY, "<simple name=\"logNode2\"><type>org.kuali.rice.kew.engine.node.LogNode</type><level>ErRoR</level><log>Custom.Logger.Name</log><message>THAT'S ALL FOLKS</message></simple>");
        assertHasConfigParam(routeNodeDef, "type", "org.kuali.rice.kew.engine.node.LogNode");
        assertHasConfigParam(routeNodeDef, "level", "ErRoR");
        assertHasConfigParam(routeNodeDef, "message", "THAT'S ALL FOLKS");
    }   
}
