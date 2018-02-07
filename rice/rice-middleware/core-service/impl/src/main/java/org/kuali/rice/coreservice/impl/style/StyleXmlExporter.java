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
package org.kuali.rice.coreservice.impl.style;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.kuali.rice.core.api.impex.ExportDataSet;
import org.kuali.rice.core.api.util.xml.XmlException;
import org.kuali.rice.core.api.util.xml.XmlHelper;
import org.kuali.rice.core.api.util.xml.XmlRenderer;
import org.kuali.rice.core.framework.impex.xml.XmlExporter;
import org.kuali.rice.coreservice.impl.style.StyleBo;

import java.io.StringReader;
import java.util.Iterator;

import static org.kuali.rice.core.api.impex.xml.XmlConstants.*;

/**
 * Exports Style definitions to XML.
 *
 * @see org.kuali.rice.edl.impl.service.StyleService
 * @see org.kuali.rice.kew.StyleXmlParserImpl.StyleXmlParser
 * @see EDocLiteStyle
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class StyleXmlExporter implements XmlExporter {
	private static final Logger LOG = Logger.getLogger(StyleXmlExporter.class);

	private XmlRenderer renderer = new XmlRenderer(STYLE_NAMESPACE);
	
	@Override
	public boolean supportPrettyPrint() {
		return false;
	}

	public Element export(ExportDataSet exportDataSet) {
		StyleExportDataSet dataSet = StyleExportDataSet.fromExportDataSet(exportDataSet);
		if (!dataSet.getStyles().isEmpty()) {
			Element rootElement = renderer.renderElement(null, STYLE_STYLES);
			rootElement.setAttribute(SCHEMA_LOCATION_ATTR, STYLE_SCHEMA_LOCATION, SCHEMA_NAMESPACE);
			for (Iterator<StyleBo> iter = dataSet.getStyles().iterator(); iter.hasNext();) {
				StyleBo edocLite = iter.next();
				exportStyle(rootElement, edocLite);
			}
			return rootElement;
		}
		return null;
	}

	private void exportStyle(Element parentEl, StyleBo style) {
        if (style == null) {
            LOG.error("Attempted to export style which was not found");
            return;
        }

        Element styleWrapperEl = renderer.renderElement(parentEl, STYLE_STYLE);
        renderer.renderAttribute(styleWrapperEl, "name", style.getName());

        try {
            Element styleEl = XmlHelper.buildJDocument(new StringReader(style.getXmlContent())).getRootElement();
            styleWrapperEl.addContent(styleEl.detach());
		} catch (XmlException e) {
			throw new RuntimeException("Error building JDom document for style", e);
		}
	}	
}
