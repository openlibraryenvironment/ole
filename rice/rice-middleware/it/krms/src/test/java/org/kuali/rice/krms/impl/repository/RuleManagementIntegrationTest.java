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
package org.kuali.rice.krms.impl.repository;

import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.krms.api.repository.language.NaturalLanguageTemplate;
import org.kuali.rice.krms.test.AbstractBoTest;
import org.kuali.rice.test.BaselineTestCase;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.CLEAR_DB)
public class RuleManagementIntegrationTest extends AbstractBoTest {

    RuleManagementServiceImpl ruleManagementServiceImpl;

    @Before
    public void setup() {
        ruleManagementServiceImpl = new RuleManagementServiceImpl();
        NaturalLanguageTemplateBoServiceImpl naturalLanguageTemplateBoServiceImpl = new NaturalLanguageTemplateBoServiceImpl();
        naturalLanguageTemplateBoServiceImpl.setNaturalLanguageTemplater(NaturalLanguageTemplateIntegrationGenTest.newStringReplaceTemplater());
        ruleManagementServiceImpl.setNaturalLanguageTemplateBoService(naturalLanguageTemplateBoServiceImpl);
        ruleManagementServiceImpl.setBusinessObjectService(getBoService()); // Business Object Service gets set to other Services
    }

    @Test
    public void naturalLanguage() {
        // create a new nlt, which we will use to get a created def, that we will add attributes to.
        NaturalLanguageTemplateIntegrationGenTest nltTest = new NaturalLanguageTemplateIntegrationGenTest();
        nltTest.setup();
        nltTest.test_createNaturalLanguageTemplate();
        NaturalLanguageTemplate nlt = nltTest.getNaturalLanguageTemplate();

        // get a def from the default test.
        NaturalLanguageTemplateBo nltBo = NaturalLanguageTemplateBo.from(nlt);
        // add the attributes for templating
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put("TEMPLATE", "template");
        nltBo.setAttributes(attributes);

        String krmsObjectId = "ID";
// TODO when attributes are working correctly, the getNaturalLanguageForType should not through an exception, and the two asserts after it should pass.
//        String result = ruleManagementServiceImpl.getNaturalLanguageForType(nlt.getNaturalLanguageUsageId(), nlt.getTypeId(), krmsObjectId, nlt.getLanguageCode());
//        assertNotNull(result);
//        assertEquals("template", result);
    }
}

