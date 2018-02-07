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

import org.junit.Test;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.kew.export.KewExportDataSet;
import org.kuali.rice.kew.rule.RuleTemplateOptionBo;
import org.kuali.rice.kew.rule.bo.RuleTemplateAttributeBo;
import org.kuali.rice.kew.rule.bo.RuleTemplateBo;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.test.BaselineTestCase;
import org.kuali.rice.test.ClearDatabaseLifecycle;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.NONE)
public class RuleTemplateXmlExporterTest extends XmlExporterTestCase {

    @Test public void testExport() throws Exception {
        loadXmlFile("RuleTemplateExportConfig.xml");
        assertExport();
    }

    protected void assertExport() throws Exception {
        // export all existing rule templates and their dependencies (rule attributes)
        List oldRuleTemplates = KEWServiceLocator.getRuleTemplateService().findAll();
        KewExportDataSet dataSet = new KewExportDataSet();
        dataSet.getRuleTemplates().addAll(oldRuleTemplates);
        dataSet.getRuleAttributes().addAll(KEWServiceLocator.getRuleAttributeService().findAll());
        byte[] xmlBytes = CoreApiServiceLocator.getXmlExporterService().export(dataSet.createExportDataSet());
        assertTrue("XML should be non empty.", xmlBytes != null && xmlBytes.length > 0);

        // now clear the tables
        new ClearDatabaseLifecycle(getPerTestTablesToClear(), getPerTestTablesNotToClear()).start();

        // import the exported xml
        loadXmlStream(new BufferedInputStream(new ByteArrayInputStream(xmlBytes)));

        List newRuleTemplates = KEWServiceLocator.getRuleTemplateService().findAll();
        assertEquals("Should have same number of old and new RuleTemplates.", oldRuleTemplates.size(), newRuleTemplates.size());
        for (Iterator iterator = oldRuleTemplates.iterator(); iterator.hasNext();) {
            RuleTemplateBo oldRuleTemplate = (RuleTemplateBo) iterator.next();
            boolean foundTemplate = false;
            for (Iterator iterator2 = newRuleTemplates.iterator(); iterator2.hasNext();) {
                RuleTemplateBo newRuleTemplate = (RuleTemplateBo) iterator2.next();
                if (oldRuleTemplate.getName().equals(newRuleTemplate.getName())) {
                    assertRuleTemplateExport(oldRuleTemplate, newRuleTemplate);
                    foundTemplate = true;
                }
            }
            assertTrue("Could not locate the new rule template for name " + oldRuleTemplate.getName(), foundTemplate);
        }
    }

    private void assertRuleTemplateExport(RuleTemplateBo oldRuleTemplate, RuleTemplateBo newRuleTemplate) {
        assertFalse("Ids should be different.", oldRuleTemplate.getId().equals(newRuleTemplate.getId()));
        assertEquals(oldRuleTemplate.getDescription(), newRuleTemplate.getDescription());
        assertEquals(oldRuleTemplate.getName(), newRuleTemplate.getName());
        if (oldRuleTemplate.getDelegationTemplate() != null) {
            assertRuleTemplateExport(oldRuleTemplate.getDelegationTemplate(), newRuleTemplate.getDelegationTemplate());
        } else {
            assertNull(newRuleTemplate.getDelegationTemplate());
        }
        assertAttributes(oldRuleTemplate.getRuleTemplateAttributes(), newRuleTemplate.getRuleTemplateAttributes(), "attribute");
        assertAttributes(oldRuleTemplate.getActiveRuleTemplateAttributes(), newRuleTemplate.getActiveRuleTemplateAttributes(), "active attribute");
        assertOptions(oldRuleTemplate.getRuleTemplateOptions(), newRuleTemplate.getRuleTemplateOptions());
    }

    private void assertAttributes(List oldAttributes, List newAttributes, String errorMessageAttributeLabel) {
        assertEquals(oldAttributes.size(), newAttributes.size());
        for (Iterator iterator = oldAttributes.iterator(); iterator.hasNext();) {
            RuleTemplateAttributeBo oldAttribute = (RuleTemplateAttributeBo) iterator.next();
            boolean foundAttribute = false;
            for (Iterator iterator2 = newAttributes.iterator(); iterator2.hasNext();) {
                RuleTemplateAttributeBo newAttribute = (RuleTemplateAttributeBo) iterator2.next();
                if (oldAttribute.getRuleAttribute().getName().equals(newAttribute.getRuleAttribute().getName())) {
                    assertEquals(oldAttribute.getRequired(), newAttribute.getRequired());
                    foundAttribute = true;
                }
            }
            assertTrue("Could not locate " + errorMessageAttributeLabel + " with name '" + oldAttribute.getRuleAttribute().getName() + "' in new attributes list.", foundAttribute);
        }
    }

    private void assertOptions(List oldTemplateOptions, List newTemplateOptions) {
        assertEquals(oldTemplateOptions.size(), newTemplateOptions.size());
        for (Iterator iterator = oldTemplateOptions.iterator(); iterator.hasNext();) {
            RuleTemplateOptionBo oldOption = (RuleTemplateOptionBo) iterator.next();
            boolean foundOption = false;
            for (Iterator iterator2 = newTemplateOptions.iterator(); iterator2.hasNext();) {
                RuleTemplateOptionBo newOption = (RuleTemplateOptionBo) iterator2.next();
                if (oldOption.getCode().equals(newOption.getCode())) {
                    assertEquals(oldOption.getValue(), newOption.getValue());
                    foundOption = true;
                }
            }
            assertTrue("Could not locate rule template option.", foundOption);
        }
    }

}
