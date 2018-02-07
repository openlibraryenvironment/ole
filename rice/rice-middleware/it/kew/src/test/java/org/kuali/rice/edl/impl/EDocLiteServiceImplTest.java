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

import org.junit.Test;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.impex.xml.XmlIngestionException;
import org.kuali.rice.coreservice.api.style.Style;
import org.kuali.rice.coreservice.api.style.StyleService;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.edl.impl.bo.EDocLiteAssociation;
import org.kuali.rice.edl.impl.bo.EDocLiteDefinition;
import org.kuali.rice.edl.impl.service.EDocLiteService;
import org.kuali.rice.edl.impl.service.EdlServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.test.TestUtilities;
import org.kuali.rice.test.BaselineTestCase;

import javax.xml.transform.Templates;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests EDocLiteServiceImpl
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.NONE)
public class EDocLiteServiceImplTest extends KEWTestCase {

	@Test public void testLoadXML() throws FileNotFoundException {
        loadXmlFile("EDocLiteContent.xml");
        loadXmlFile("edlstyle.xml");

        EDocLiteService edls = EdlServiceLocator.getEDocLiteService();
        StyleService styleService = CoreServiceApiServiceLocator.getStyleService();
        //edls.loadXml(new FileInputStream("conf/examples/xml/EDocLiteContent.xml"));
        assertTrue("Definition not found", edls.getEDocLiteDefinitions().contains("profile"));
        Style defaultStyle = styleService.getStyle("Default");
        assertNotNull("Style not found", defaultStyle);
        assertEquals(1, edls.getEDocLiteAssociations().size());
        EDocLiteDefinition def = edls.getEDocLiteDefinition("profile");
        assertNotNull("'profile' definition not found", def);
        assertEquals("profile", def.getName());
        assertNotNull(def.getActiveInd());
        assertTrue(def.getActiveInd().booleanValue());
        Style style = styleService.getStyle("Default");
        assertNotNull("'Default' style not found", style);
        assertEquals("Default", style.getName());
        assertTrue(style.isActive());
        assertNotNull(style.getXmlContent());
    }

    @Test public void testLoadBadDefinition() throws FileNotFoundException {
        EDocLiteService edls = EdlServiceLocator.getEDocLiteService();
        try {
            edls.loadXml(TestUtilities.loadResource(getClass(), "BadDefinition.xml"), null);
            fail("BadDefinition was successfully parsed.");
        } catch (XmlIngestionException re) {
            // should probably use type system to detect type of error, not just message string...
            // maybe we need general parsing or "semantic" validation exception
            assertTrue("Wrong exception occurred", re.getMessage().contains("EDocLite definition contains references to non-existent attributes"));
        }
    }

    @Test public void testStoreDefinition() {
        EDocLiteService edls = EdlServiceLocator.getEDocLiteService();
        String defXml = "<edl></edl>";
        try {
            edls.saveEDocLiteDefinition(new ByteArrayInputStream(defXml.getBytes()));
            fail("Storing edl with no name succeeded");
        } catch (XmlIngestionException wsee) {
            // expected due to lack of name
        }
        defXml = "<edl name=\"test\"></edl>";
        edls.saveEDocLiteDefinition(new ByteArrayInputStream(defXml.getBytes()));
        EDocLiteDefinition def = edls.getEDocLiteDefinition("test");
        assertNotNull(def);
        assertEquals("test", def.getName());
    }

    @Test public void testStoreAssociation() {
        EDocLiteService edls = EdlServiceLocator.getEDocLiteService();
        String assocXml = "<association></association>";
        try {
            edls.saveEDocLiteAssociation(new ByteArrayInputStream(assocXml.getBytes()));
            fail("Storing association with no docType succeeded");
        } catch (XmlIngestionException wsee) {
            // expected due to lack of doctype
        }
        assocXml = "<association><docType></docType></association>";
        try {
            edls.saveEDocLiteAssociation(new ByteArrayInputStream(assocXml.getBytes()));
            fail("Storing association with empty docType succeeded");
        } catch (XmlIngestionException wsee) {
            // expected due to emtpy doctype value
        }
        assocXml = "<association><docType>foobar</docType></association>";
        edls.saveEDocLiteAssociation(new ByteArrayInputStream(assocXml.getBytes()));
        EDocLiteAssociation assoc = edls.getEDocLiteAssociation("foobar");
        assertNull("Inactive Association was found", assoc);

        assocXml = "<association><docType>foobar</docType><active>true</active></association>";
        edls.saveEDocLiteAssociation(new ByteArrayInputStream(assocXml.getBytes()));
        assoc = edls.getEDocLiteAssociation("foobar");
        assertNotNull("Association was not found", assoc);
        assertEquals("foobar", assoc.getEdlName());
        assertNull(assoc.getDefinition());
        assertNull(assoc.getStyle());

        List<EDocLiteAssociation> assocs = edls.getEDocLiteAssociations();
        assertEquals(1, assocs.size());
        assoc = assocs.get(0);
        assertEquals("foobar", assoc.getEdlName());
        assertNull(assoc.getDefinition());
        assertNull(assoc.getStyle());
        assertNotNull(assoc.getActiveInd());
        assertTrue(assoc.getActiveInd().booleanValue());

        assocXml = "<association><style>style name</style><definition>definition name</definition><docType>foobar</docType><active>true</active></association>";
        edls.saveEDocLiteAssociation(new ByteArrayInputStream(assocXml.getBytes()));
        assoc = edls.getEDocLiteAssociation("foobar");
        assertNotNull("Association was not found", assoc);
        assertEquals("foobar", assoc.getEdlName());
        assertEquals("definition name", assoc.getDefinition());
        assertEquals("style name", assoc.getStyle());

        assocs = edls.getEDocLiteAssociations();
        assertEquals(1, assocs.size());
        assoc = (EDocLiteAssociation) assocs.get(0);
        assertNotNull("Association was not found", assoc);
        assertEquals("foobar", assoc.getEdlName());
        assertEquals("definition name", assoc.getDefinition());
        assertEquals("style name", assoc.getStyle());
        assertNotNull(assoc.getActiveInd());
        assertTrue(assoc.getActiveInd().booleanValue());
    }

    /**
     * Tests the caching of "styles" in EDocLiteServiceImpl.
     *
     * The style cache is really a cache of java.xml.transform.Templates objects which represent
     * the "compiled" stylesheets.
     */
    @Test public void testStyleCaching() throws Exception {
    	ConfigContext.getCurrentContextConfig().putProperty(Config.EDL_CONFIG_LOCATION, "classpath:org/kuali/rice/kew/edl/TestEDLConfig.xml");

    	loadXmlFile("EDocLiteContent.xml");
        loadXmlFile("edlstyle.xml");
        loadXmlFile("widgets.xml");

        // try to grab the templates out of the cache, it shouldn't be cached yet
//        Templates cachedTemplates = new EDocLiteServiceImpl().fetchTemplatesFromCache("Default");
//        assertNull("The default style template should not be cached yet.", cachedTemplates);

        // fetch the Templates object from the service
        EDocLiteAssociation association = EdlServiceLocator.getEDocLiteService().getEDocLiteAssociation("EDocLiteDocType");
        assertNull("We should be using the Default style.", association.getStyle());
        Templates templates = EdlServiceLocator.getEDocLiteService().getStyleAsTranslet(association.getStyle());
        assertNotNull("Templates should not be null.", templates);

        // the Templates should now be cached
//        cachedTemplates = new EDocLiteServiceImpl().fetchTemplatesFromCache("Default");
//        assertNotNull("Templates should now be cached.", cachedTemplates);

//        // the cached Templates should be the same as the Templates we fetched from the service
//        assertEquals("Templates should be the same.", templates, cachedTemplates);

        // now re-import the style and the templates should no longer be cached
        loadXmlFile("edlstyle.xml");
//        cachedTemplates = new EDocLiteServiceImpl().fetchTemplatesFromCache("Default");
//        assertNull("After re-import, the Default style Templates should no longer be cached.", cachedTemplates);

        // re-fetch the templates from the service and verify they are in the cache
        Templates newTemplates = EdlServiceLocator.getEDocLiteService().getStyleAsTranslet(association.getStyle());
        assertNotNull("Templates should not be null.", templates);
//        cachedTemplates = new EDocLiteServiceImpl().fetchTemplatesFromCache("Default");
//        assertNotNull("Templates should now be cached.", cachedTemplates);

        // lastly, check that the newly cached templates are not the same as the original templates
        assertFalse("Old Templates should be different from new Templates.", templates.equals(newTemplates));

    }
}
