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

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.kew.export.KewExportDataSet;
import org.kuali.rice.kew.rule.bo.RuleAttribute;
import org.kuali.rice.kew.rule.bo.RuleTemplateAttributeBo;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.test.BaselineTestCase;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.NONE)
public class RuleAttributeXmlExporterTest extends XmlExporterTestCase {
    
    /**
     * This will test some rule attributes with routing and searching config.
     */
	@Test public void testExport() throws Exception {
        loadXmlFile("RuleAttributeExportConfig.xml");
        assertExport();
    }
        
    protected void assertExport() throws Exception {
        // export all existing rule attributes
        List oldRuleAttributes = KEWServiceLocator.getRuleAttributeService().findAll();
        KewExportDataSet dataSet = new KewExportDataSet();
        dataSet.getRuleAttributes().addAll(oldRuleAttributes);
        byte[] xmlBytes = CoreApiServiceLocator.getXmlExporterService().export(dataSet.createExportDataSet());
        assertTrue("XML should be non empty.", xmlBytes != null && xmlBytes.length > 0);
        
        // import the exported xml
        loadXmlStream(new BufferedInputStream(new ByteArrayInputStream(xmlBytes)));
        
        List newRuleAttributes = KEWServiceLocator.getRuleAttributeService().findAll();
        assertEquals("Should have same number of old and new RuleAttributes.", oldRuleAttributes.size(), newRuleAttributes.size());
        for (Iterator iterator = oldRuleAttributes.iterator(); iterator.hasNext();) {
            RuleAttribute oldRuleAttribute = (RuleAttribute) iterator.next();
            boolean foundAttribute = false;
            for (Iterator iterator2 = newRuleAttributes.iterator(); iterator2.hasNext();) {
                RuleAttribute newRuleAttribute = (RuleAttribute) iterator2.next();
                if (oldRuleAttribute.getName().equals(newRuleAttribute.getName())) {
                    assertRuleAttributeExport(oldRuleAttribute, newRuleAttribute);
                    foundAttribute = true;
                }
            }
            assertTrue("Could not locate the new rule attribute for name " + oldRuleAttribute.getName(), foundAttribute);
        }
    }
    
    private void assertRuleAttributeExport(RuleAttribute oldRuleAttribute, RuleAttribute newRuleAttribute) {
        // ids should be the same because we don't version rule attributes, but thier version number should be different
        assertEquals("Ids should be the same.", oldRuleAttribute.getId(), newRuleAttribute.getId());
        assertFalse("Version numbers should be different.", oldRuleAttribute.getVersionNumber().equals(newRuleAttribute.getVersionNumber()));
        assertEquals(oldRuleAttribute.getDescription(), newRuleAttribute.getDescription());
        assertEquals(oldRuleAttribute.getName(), newRuleAttribute.getName());
        assertEquals(oldRuleAttribute.getLabel(), newRuleAttribute.getLabel());
        assertEquals(oldRuleAttribute.getType(), newRuleAttribute.getType());
        assertEquals(StringUtils.deleteWhitespace(oldRuleAttribute.getXmlConfigData()), StringUtils.deleteWhitespace(newRuleAttribute.getXmlConfigData()));
        assertRuleTemplateAttributes(oldRuleAttribute.getRuleTemplateAttributes(), newRuleAttribute.getRuleTemplateAttributes());
    }
    
    private void assertRuleTemplateAttributes(List oldRuleTemplateAttributes, List newRuleTemplateAttributes) {
        assertEquals(oldRuleTemplateAttributes.size(), newRuleTemplateAttributes.size());
        for (Iterator iterator = oldRuleTemplateAttributes.iterator(); iterator.hasNext();) {
            RuleTemplateAttributeBo oldAttribute = (RuleTemplateAttributeBo) iterator.next();
            boolean foundAttribute = false;
            for (Iterator iterator2 = oldRuleTemplateAttributes.iterator(); iterator2.hasNext();) {
                RuleTemplateAttributeBo newAttribute = (RuleTemplateAttributeBo) iterator2.next();
                if (oldAttribute.getRuleAttribute().getName().equals(newAttribute.getRuleAttribute().getName())) {
                    assertEquals(oldAttribute.getRequired(), newAttribute.getRequired());
                    foundAttribute = true;
                }
            }
            assertTrue("Could not locate new attribute.", foundAttribute);
        }
    }
        
}
