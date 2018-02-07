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
import org.kuali.rice.krms.api.repository.language.NaturalLanguageTemplaterContract;
import org.kuali.rice.krms.api.repository.language.NaturalLanguageUsage;
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition;
import org.kuali.rice.krms.test.AbstractBoTest;
import org.kuali.rice.test.BaselineTestCase;

import java.util.Iterator;
import java.util.Map;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.ROLLBACK_CLEAR_DB)
public final class NaturalLanguageTemplateIntegrationGenTest extends AbstractBoTest {

    NaturalLanguageTemplateBoServiceImpl naturalLanguageTemplateBoServiceImpl;
    NaturalLanguageTemplate naturalLanguageTemplate;
    KrmsAttributeDefinitionService krmsAttributeDefinitionService;
    KrmsTypeBoServiceImpl krmsTypeBoServiceImpl;
    NaturalLanguageUsageBoServiceImpl naturalLanguageUsageBoServiceImpl; // TODO gen

    NaturalLanguageTemplate getNaturalLanguageTemplate() {
        return naturalLanguageTemplate;
    }

    /**
     * Note lower case u, do not override superclasses setUp
     *
     */
    @Before
    public void setup() {
        naturalLanguageTemplateBoServiceImpl = new NaturalLanguageTemplateBoServiceImpl();
        naturalLanguageTemplateBoServiceImpl.setNaturalLanguageTemplater(newStringReplaceTemplater()); // TODO gen?
        krmsAttributeDefinitionService = KrmsRepositoryServiceLocator.getKrmsAttributeDefinitionService();
        naturalLanguageTemplateBoServiceImpl.setAttributeDefinitionService(krmsAttributeDefinitionService);
        naturalLanguageTemplateBoServiceImpl.setBusinessObjectService(getBoService());
        krmsTypeBoServiceImpl = new KrmsTypeBoServiceImpl();
        krmsTypeBoServiceImpl.setBusinessObjectService(getBoService());
        naturalLanguageUsageBoServiceImpl = new NaturalLanguageUsageBoServiceImpl();
        naturalLanguageUsageBoServiceImpl.setBusinessObjectService(getBoService());
    }

    public static NaturalLanguageTemplaterContract newStringReplaceTemplater() {
        return new NaturalLanguageTemplaterContract() {
            @Override
            public String translate(NaturalLanguageTemplate naturalLanguageTemplate, Map<String,Object>variables) {
                String template = naturalLanguageTemplate.getTemplate();
                Map<String, String> attributes = naturalLanguageTemplate.getAttributes();
                Iterator<String> keys = attributes.keySet().iterator();
                while (keys.hasNext()) {
                    String key = keys.next();
                    template.replace(key, attributes.get(key));
                }
                return template;
            }
        };
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_getNaturalLanguageTemplatesByAttributes_null_fail() {
        (NaturalLanguageTemplateBoServiceImplGenTest.create(naturalLanguageTemplateBoServiceImpl, krmsAttributeDefinitionService)).test_getNaturalLanguageTemplatesByAttributes_null_fail();
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_getNaturalLanguageTemplatesByLanguageCode_null_fail() {
        (NaturalLanguageTemplateBoServiceImplGenTest.create(naturalLanguageTemplateBoServiceImpl, krmsAttributeDefinitionService)).test_getNaturalLanguageTemplatesByLanguageCode_null_fail();
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_getNaturalLanguageTemplatesByNaturalLanguageUsage_null_fail() {
        (NaturalLanguageTemplateBoServiceImplGenTest.create(naturalLanguageTemplateBoServiceImpl, krmsAttributeDefinitionService)).test_getNaturalLanguageTemplatesByNaturalLanguageUsage_null_fail();
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_getNaturalLanguageTemplatesByType_null_fail() {
        (NaturalLanguageTemplateBoServiceImplGenTest.create(naturalLanguageTemplateBoServiceImpl, krmsAttributeDefinitionService)).test_getNaturalLanguageTemplatesByType_null_fail();
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_getNaturalLanguageTemplatesByTemplate_null_fail() {
        (NaturalLanguageTemplateBoServiceImplGenTest.create(naturalLanguageTemplateBoServiceImpl, krmsAttributeDefinitionService)).test_getNaturalLanguageTemplatesByTemplate_null_fail();
    }

    @Test
    public void test_from_null_yields_null() {
        (NaturalLanguageTemplateBoServiceImplGenTest.create(naturalLanguageTemplateBoServiceImpl, krmsAttributeDefinitionService)).test_from_null_yields_null();
    }

    @Test
    public void test_to() {
        (NaturalLanguageTemplateBoServiceImplGenTest.create(naturalLanguageTemplateBoServiceImpl, krmsAttributeDefinitionService)).test_to();
    }

    @Test
    public void test_from() { // TODO gen
        (NaturalLanguageTemplateBoServiceImplGenTest.create(naturalLanguageTemplateBoServiceImpl, krmsAttributeDefinitionService)).test_from();
    }

    public void test_createNaturalLanguageTemplate() {
        KrmsTypeIntegrationGenTest krmsTypeTest = new KrmsTypeIntegrationGenTest();
        krmsTypeTest.setup(); // Note lowercase u
        krmsTypeTest.test_createKrmsType();
        KrmsTypeDefinition krmsType = krmsTypeTest.getKrmsType();
        NaturalLanguageUsageIntegrationGenTest naturalLanguageUsageTest = new NaturalLanguageUsageIntegrationGenTest();
        naturalLanguageUsageTest.setup(); // Note lowercase u
        naturalLanguageUsageTest.test_createNaturalLanguageUsage();
        NaturalLanguageUsage naturalLanguageUsage = naturalLanguageUsageTest.getNaturalLanguageUsage();
        NaturalLanguageTemplateBoServiceImplGenTest test = NaturalLanguageTemplateBoServiceImplGenTest.create(naturalLanguageTemplateBoServiceImpl, krmsAttributeDefinitionService);
        test.createNaturalLanguageTemplate(naturalLanguageUsage, krmsType); // TODO gen order of params
        naturalLanguageTemplate = test.getNaturalLanguageTemplate();
        assert(naturalLanguageTemplate != null);
        assert(naturalLanguageTemplate.getId() != null);
        assert(naturalLanguageTemplate.getAttributes() != null && !naturalLanguageTemplate.getAttributes().isEmpty());
    }

    @Test // TODO gen use multiple catches
    public void test_createNaturalLanguageTemplate_fail_existing() {
        test_createNaturalLanguageTemplate();
        try {
            test_createNaturalLanguageTemplate();
        } catch (IllegalStateException ise) {
            return; // local
        } catch (Exception e) {
            return; // ci database specific Exception so catch Exception to avoid depending on driver classes
        }
        assert(false); // exception should be thrown
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_createNaturalLanguageTemplate_null_fail() {
        (NaturalLanguageTemplateBoServiceImplGenTest.create(naturalLanguageTemplateBoServiceImpl, krmsAttributeDefinitionService)).test_createNaturalLanguageTemplate_null_fail();
    }

    public void test_getNaturalLanguageTemplate() {
        test_createNaturalLanguageTemplate();
        NaturalLanguageTemplate def = getNaturalLanguageTemplate();
        NaturalLanguageTemplate def2 = naturalLanguageTemplateBoServiceImpl.getNaturalLanguageTemplate(def.getId());
        assert(def2 != null);
        assert(def2.equals(def));
    }

    public void test_updateNaturalLanguageTemplate() {
        test_createNaturalLanguageTemplate();
        NaturalLanguageTemplate def = getNaturalLanguageTemplate();
        String id = def.getId();
        assert(!"UpdateTest".equals(def.getTemplate()));
        NaturalLanguageTemplateBo bo = naturalLanguageTemplateBoServiceImpl.from(def);
        bo.setTemplate("UpdateTest");
        naturalLanguageTemplateBoServiceImpl.updateNaturalLanguageTemplate(NaturalLanguageTemplate.Builder.create(bo).build());
        NaturalLanguageTemplate def2 = naturalLanguageTemplateBoServiceImpl.getNaturalLanguageTemplate(id);
        assert("UpdateTest".equals(def2.getTemplate()));
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_updateNaturalLanguageTemplate_null_fail() {
        (NaturalLanguageTemplateBoServiceImplGenTest.create(naturalLanguageTemplateBoServiceImpl, krmsAttributeDefinitionService)).test_updateNaturalLanguageTemplate_null_fail();
    }

    public void test_deleteNaturalLanguageTemplate() {
        test_createNaturalLanguageTemplate();
        NaturalLanguageTemplate def = getNaturalLanguageTemplate();
        String id = def.getId();
        naturalLanguageTemplateBoServiceImpl.deleteNaturalLanguageTemplate(id);
        NaturalLanguageTemplate def2 = naturalLanguageTemplateBoServiceImpl.getNaturalLanguageTemplate(id);
        assert(def2 == null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_deleteNaturalLanguageTemplate_null_fail() {
        (NaturalLanguageTemplateBoServiceImplGenTest.create(naturalLanguageTemplateBoServiceImpl, krmsAttributeDefinitionService)).test_deleteNaturalLanguageTemplate_null_fail();
    }
}
