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
package org.kuali.rice.kew.xml.export;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.kuali.rice.core.api.impex.ExportDataSet;
import org.kuali.rice.core.api.util.xml.XmlHelper;
import org.kuali.rice.core.api.util.xml.XmlRenderer;
import org.kuali.rice.core.framework.impex.xml.XmlExporter;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.export.KewExportDataSet;
import org.kuali.rice.kew.rule.bo.RuleAttribute;

import java.io.StringReader;
import java.util.Iterator;

import static org.kuali.rice.core.api.impex.xml.XmlConstants.*;

/**
 * Exports {@link org.kuali.rice.kew.rule.bo.RuleAttribute}s to XML.
 * 
 * @see org.kuali.rice.kew.rule.bo.RuleAttribute
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RuleAttributeXmlExporter implements XmlExporter {

    protected final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(getClass());
    
    private XmlRenderer renderer = new XmlRenderer(RULE_ATTRIBUTE_NAMESPACE);
    
	@Override
	public boolean supportPrettyPrint() {
		return true;
	}

    public Element export(ExportDataSet exportDataSet) {
    	KewExportDataSet dataSet = KewExportDataSet.fromExportDataSet(exportDataSet);
        if (!dataSet.getRuleAttributes().isEmpty()) {
            Element rootElement = renderer.renderElement(null, RULE_ATTRIBUTES);
            rootElement.setAttribute(SCHEMA_LOCATION_ATTR, RULE_ATTRIBUTE_SCHEMA_LOCATION, SCHEMA_NAMESPACE);
            for (Iterator iterator = dataSet.getRuleAttributes().iterator(); iterator.hasNext();) {
                RuleAttribute template = (RuleAttribute)iterator.next();
                exportRuleAttribute(rootElement, template);
            }
            return rootElement;
        }
        return null;
    }
    
    private void exportRuleAttribute(Element parent, RuleAttribute ruleAttribute) {
        Element attributeElement = renderer.renderElement(parent, RULE_ATTRIBUTE);
        renderer.renderTextElement(attributeElement, NAME, ruleAttribute.getName());
        renderer.renderTextElement(attributeElement, CLASS_NAME, ruleAttribute.getResourceDescriptor());
        renderer.renderTextElement(attributeElement, LABEL, ruleAttribute.getLabel());
        renderer.renderTextElement(attributeElement, DESCRIPTION, ruleAttribute.getDescription());
        renderer.renderTextElement(attributeElement, TYPE, ruleAttribute.getType());
        renderer.renderTextElement(attributeElement, APPLICATION_ID, ruleAttribute.getApplicationId());
        if (!org.apache.commons.lang.StringUtils.isEmpty(ruleAttribute.getXmlConfigData())) {
            try {
                Document configDoc = new SAXBuilder().build(new StringReader(ruleAttribute.getXmlConfigData()));
                XmlHelper.propagateNamespace(configDoc.getRootElement(), RULE_ATTRIBUTE_NAMESPACE);
                attributeElement.addContent(configDoc.getRootElement().detach());
            } catch (Exception e) {
            	LOG.error("Error parsing attribute XML configuration.", e);
                throw new WorkflowRuntimeException("Error parsing attribute XML configuration.");
            }
        }
    }
    
}
