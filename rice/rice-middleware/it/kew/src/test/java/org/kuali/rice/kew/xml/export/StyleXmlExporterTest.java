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
package org.kuali.rice.kew.xml.export;

import org.jdom.Document;
import org.junit.Test;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.coreservice.api.style.Style;
import org.kuali.rice.core.api.util.xml.XmlHelper;
import org.kuali.rice.core.api.util.xml.XmlJotter;
import org.kuali.rice.coreservice.impl.style.StyleBo;
import org.kuali.rice.coreservice.impl.style.StyleExportDataSet;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.test.BaselineTestCase;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;


/**
 * Tests exporting Styles
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.NONE)
public class StyleXmlExporterTest extends XmlExporterTestCase {

	public void testExportActionConfig() throws Exception {
		// action config has no help entries
    }

    public void testExportEngineConfig() throws Exception {
    	// engine config has no help entries
    }

    /**
     * This will test exporting some styles
     */
    @Test public void testExport() throws Exception {
        loadXmlFile("StyleExportConfig.xml");
        assertExport();
    }

    protected void assertExport() throws Exception {
        List<String> oldStyleNames = CoreServiceApiServiceLocator.getStyleService().getAllStyleNames();
        
        StyleExportDataSet dataSet = new StyleExportDataSet();
        for (String oldStyleName : oldStyleNames) {
        	Style oldStyle = CoreServiceApiServiceLocator.getStyleService().getStyle(oldStyleName);
        	assertNotNull(oldStyle);
        	dataSet.getStyles().add(StyleBo.from(oldStyle));
        }

        byte[] xmlBytes = CoreApiServiceLocator.getXmlExporterService().export(dataSet.createExportDataSet());
        assertTrue("XML should be non empty.", xmlBytes != null && xmlBytes.length > 0);
        // quick check to verify that not only is the XML non-empty, but that it might actually contain an attempt at an exported style
        // (otherwise the XML could not contain any styles, and the test would pass with a false positive even though the export never
        // exported anything)
        assertTrue("XML does not contain exported style", new String(xmlBytes).contains("<styles "));
        assertTrue("XML does not contain exported style", new String(xmlBytes).contains("<style name=\"an_arbitrary_style\">"));

        // import the exported xml
        loadXmlStream(new BufferedInputStream(new ByteArrayInputStream(xmlBytes)));

        List<String> newStyleNames = CoreServiceApiServiceLocator.getStyleService().getAllStyleNames();
        assertEquals("Should have same number of old and new Styles.", oldStyleNames.size(), newStyleNames.size());
        for (Iterator<String> iterator = oldStyleNames.iterator(); iterator.hasNext();) {
            String oldStyleName = iterator.next();
            Style oldStyleEntry = CoreServiceApiServiceLocator.getStyleService().getStyle(oldStyleName);
            assertNotNull(oldStyleEntry);
            boolean foundAttribute = false;
            for (String newStyleName : newStyleNames) {
                if (oldStyleEntry.getName().equals(newStyleName)) {
                	Style newStyleEntry = CoreServiceApiServiceLocator.getStyleService().getStyle(newStyleName);
                	assertNotNull(newStyleEntry);
                    // NOTE: xmlns="http://www.w3.org/1999/xhtml" must be set on elements that contain HTML; exporter will automatically append an empty
                    // attribute, which will result in trivially unmatching content
                    assertEquals(canonicalize(oldStyleEntry.getXmlContent()),
                                 canonicalize(newStyleEntry.getXmlContent()));
                    foundAttribute = true;
                }
            }
            assertTrue("Could not locate the new style for name " + oldStyleEntry.getName(), foundAttribute);
        }
    }

    private String canonicalize(String xml) throws Exception {
    	Document document = XmlHelper.buildJDocument(new StringReader(xml));
    	return XmlJotter.jotDocument(document);
    }
}
