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
package org.kuali.rice.edl.impl.xml.export;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.Namespace;
import org.kuali.rice.core.api.impex.ExportDataSet;
import org.kuali.rice.coreservice.api.style.Style;
import org.kuali.rice.coreservice.api.style.StyleService;
import org.kuali.rice.core.api.util.xml.XmlHelper;
import org.kuali.rice.core.api.util.xml.XmlRenderer;
import org.kuali.rice.core.framework.impex.xml.XmlExporter;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.edl.impl.bo.EDocLiteAssociation;
import org.kuali.rice.edl.impl.bo.EDocLiteDefinition;
import org.kuali.rice.edl.impl.service.EDocLiteService;
import org.kuali.rice.edl.impl.service.EdlServiceLocator;

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import static org.kuali.rice.core.api.impex.xml.XmlConstants.*;
/**
 * Exports EDocLite definitions to XML.
 *
 * @see EDocLiteDefinition
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class EDocLiteXmlExporter implements XmlExporter {

	private static final Logger LOG = Logger.getLogger(EDocLiteXmlExporter.class);

	private XmlRenderer renderer = new XmlRenderer(EDL_NAMESPACE);

	@Override
	public boolean supportPrettyPrint() {
		return false;
	}
	
	public Element export(ExportDataSet exportDataSet) {
		EdlExportDataSet dataSet = EdlExportDataSet.fromExportDataSet(exportDataSet);
		if (!dataSet.getEdocLites().isEmpty()) {
			Element rootElement = renderer.renderElement(null, EDL_EDOCLITE);
			rootElement.setAttribute(SCHEMA_LOCATION_ATTR, EDL_SCHEMA_LOCATION, SCHEMA_NAMESPACE);
			// create output in order of all edl followed by all stylesheets, followed by all associations, this is so multiple edoclite's can be ingested in a single xml file.
			List<EDocLiteAssociation> assocList = dataSet.getEdocLites();
			// loop thru same list 3 times.
			for (EDocLiteAssociation e : assocList) {
				exportEdlDefinitions(rootElement, e);
			}
			for (EDocLiteAssociation e : assocList) {
				exportStyles(rootElement, e);
			}
			for (EDocLiteAssociation e : assocList) {
				exportAssociations(rootElement, e);
			}
			return rootElement;
		}
		return null;
	}

	private void exportEdlDefinitions(Element parentEl, EDocLiteAssociation edl) {

		try {
			EDocLiteService edlService = EdlServiceLocator.getEDocLiteService();
			if (edl.getDefinition() != null) {  //this probably shouldn't be supported on the entry side...
				EDocLiteDefinition def = edlService.getEDocLiteDefinition(edl.getDefinition());
				if (def == null) {
					LOG.error("Attempted to export definition " + edl.getDefinition() + " which was not found");
					return;
				}
				Element defEl = XmlHelper.buildJDocument(new StringReader(def.getXmlContent())).getRootElement();
				setNamespace(defEl, EDL_NAMESPACE);
				parentEl.addContent(defEl.detach());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void exportStyles(Element parentEl, EDocLiteAssociation edl) {

		try {
			StyleService styleService = CoreServiceApiServiceLocator.getStyleService();

			if (edl.getStyle() != null) {//this probably shouldn't be supported on the entry side...
				Element styleWrapperEl = renderer.renderElement(parentEl, EDL_STYLE);
				renderer.renderAttribute(styleWrapperEl, "name", edl.getStyle());
				Style style = styleService.getStyle(edl.getStyle());
				if (style == null) {
					LOG.error("Attempted to export style " + edl.getStyle() + " which was not found");
					return;
				}
				Element styleEl = XmlHelper.buildJDocument(new StringReader(style.getXmlContent())).getRootElement();
				styleWrapperEl.addContent(styleEl.detach());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void exportAssociations(Element parentEl, EDocLiteAssociation edl) {
		try {
			Element associationEl = renderer.renderElement(parentEl, EDL_ASSOCIATION);
			renderer.renderTextElement(associationEl, EDL_DOC_TYPE, edl.getEdlName());
			if (edl.getDefinition() != null) {
				renderer.renderTextElement(associationEl, EDL_DEFINITION, edl.getDefinition());
			}
			if (edl.getStyle() != null) {
				renderer.renderTextElement(associationEl, EDL_STYLE, edl.getStyle());
			}

			renderer.renderTextElement(associationEl, EDL_ACTIVE, edl.getActiveInd().toString());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void setNamespace(Element element, Namespace namespace) {
		element.setNamespace(namespace);
		for (Iterator iter = element.getChildren().iterator(); iter.hasNext();) {
			setNamespace((Element)iter.next(), namespace);
		}
	}
}
