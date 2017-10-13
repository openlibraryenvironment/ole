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
package org.kuali.rice.krms.impl.repository;

import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krms.api.repository.NaturalLanguageTemplateGenTest;
import org.kuali.rice.krms.api.repository.language.NaturalLanguageTemplate;
import org.kuali.rice.krms.api.repository.language.NaturalLanguageUsage;
import org.kuali.rice.krms.api.repository.type.KrmsAttributeDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeRepositoryService;

import java.util.Map;

import static org.mockito.Mockito.mock;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public final class NaturalLanguageTemplateBoServiceImplGenTest {

    NaturalLanguageTemplateBoServiceImpl naturalLanguageTemplateBoServiceImpl;
    NaturalLanguageTemplate naturalLanguageTemplate;
    KrmsAttributeDefinitionService krmsAttributeDefinitionService;

    NaturalLanguageTemplate getNaturalLanguageTemplate() {
        return naturalLanguageTemplate;
    }

    public void setNaturalLanguageTemplateBoServiceImpl(NaturalLanguageTemplateBoServiceImpl impl) {
        this.naturalLanguageTemplateBoServiceImpl = impl;
    }

    public void setKrmsAttributeDefinitionService(KrmsAttributeDefinitionService impl) {
        krmsAttributeDefinitionService = impl;
    }

    public static org.kuali.rice.krms.impl.repository.NaturalLanguageTemplateBoServiceImplGenTest create(
            NaturalLanguageTemplateBoServiceImpl nlTemplateBoService,
            KrmsAttributeDefinitionService attributeDefService) {

        org.kuali.rice.krms.impl.repository.NaturalLanguageTemplateBoServiceImplGenTest test =
                new org.kuali.rice.krms.impl.repository.NaturalLanguageTemplateBoServiceImplGenTest();
        test.setKrmsAttributeDefinitionService(attributeDefService);
        test.setNaturalLanguageTemplateBoServiceImpl(nlTemplateBoService);
        return test;
    }

    @Before
    public void setUp() {
        naturalLanguageTemplateBoServiceImpl = new NaturalLanguageTemplateBoServiceImpl();
        KrmsAttributeDefinitionService mockAttributeService = mock(KrmsAttributeDefinitionService.class);
        NaturalLanguageTemplateBo.setAttributeDefinitionService(mockAttributeService);
        KrmsTypeRepositoryService mockTypeRepositoryService = mock(KrmsTypeRepositoryService.class);
        NaturalLanguageTemplateBo.setTypeRepositoryService(mockTypeRepositoryService);
        naturalLanguageTemplateBoServiceImpl.setAttributeDefinitionService(mockAttributeService);
        naturalLanguageTemplateBoServiceImpl.setBusinessObjectService(mock(BusinessObjectService.class));
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_getNaturalLanguageTemplatesByAttributes_null_fail() {
        naturalLanguageTemplateBoServiceImpl.findNaturalLanguageTemplatesByAttributes(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_getNaturalLanguageTemplatesByLanguageCode_null_fail() {
        naturalLanguageTemplateBoServiceImpl.findNaturalLanguageTemplatesByLanguageCode(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_getNaturalLanguageTemplatesByNaturalLanguageUsage_null_fail() {
        naturalLanguageTemplateBoServiceImpl.findNaturalLanguageTemplatesByNaturalLanguageUsage(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_getNaturalLanguageTemplatesByType_null_fail() {
        naturalLanguageTemplateBoServiceImpl.findNaturalLanguageTemplatesByType(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_getNaturalLanguageTemplatesByTemplate_null_fail() {
        naturalLanguageTemplateBoServiceImpl.findNaturalLanguageTemplatesByTemplate(null);
    }

    @Test
    public void test_from_null_yields_null() {
        assert(naturalLanguageTemplateBoServiceImpl.from(null) == null);
    }

    @Test
    public void test_from() {
        NaturalLanguageTemplate def = NaturalLanguageTemplateGenTest.buildFullNaturalLanguageTemplate();
        NaturalLanguageTemplateBo naturalLanguageTemplateBo = naturalLanguageTemplateBoServiceImpl.from(def);
        assert(naturalLanguageTemplateBo.getLanguageCode().equals(def.getLanguageCode()));
        assert(naturalLanguageTemplateBo.getNaturalLanguageUsageId().equals(def.getNaturalLanguageUsageId()));
        assert(naturalLanguageTemplateBo.getTypeId().equals(def.getTypeId()));
        assert(naturalLanguageTemplateBo.getTemplate().equals(def.getTemplate()));
        assert(naturalLanguageTemplateBo.getId().equals(def.getId()));
    }

    @Test
    public void test_to() {
        NaturalLanguageTemplate def = NaturalLanguageTemplateGenTest.buildFullNaturalLanguageTemplate();
        NaturalLanguageTemplateBo naturalLanguageTemplateBo = naturalLanguageTemplateBoServiceImpl.from(def);
        NaturalLanguageTemplate def2 = NaturalLanguageTemplateBo.to(naturalLanguageTemplateBo);
        assert(def.equals(def2));
    }

    @Test
    public void test_createNaturalLanguageTemplate() {
        NaturalLanguageTemplate def = NaturalLanguageTemplateGenTest.buildFullNaturalLanguageTemplate();
        naturalLanguageTemplate = naturalLanguageTemplateBoServiceImpl.createNaturalLanguageTemplate(def);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_createNaturalLanguageTemplate_null_fail() {
        naturalLanguageTemplateBoServiceImpl.createNaturalLanguageTemplate(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_updateNaturalLanguageTemplate_null_fail() {
        naturalLanguageTemplateBoServiceImpl.updateNaturalLanguageTemplate(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_deleteNaturalLanguageTemplate_null_fail() {
        naturalLanguageTemplateBoServiceImpl.deleteNaturalLanguageTemplate(null);
    }

    void createNaturalLanguageTemplate(NaturalLanguageUsage naturalLanguageUsage, KrmsTypeDefinition type) {
        NaturalLanguageTemplate def = NaturalLanguageTemplateGenTest.buildFullNaturalLanguageTemplate(naturalLanguageUsage, type);

        for (Map.Entry<String, String> attributeEntry : def.getAttributes().entrySet()) {
            // check for template attribute definition, create if not there
            KrmsAttributeDefinition attrDef = krmsAttributeDefinitionService.getAttributeDefinitionByNameAndNamespace(
                    attributeEntry.getKey(), type.getNamespace());

          // rebuild attributes in all cases until Constraint Error found and corrected
          //  if (null == attrDef) {
                KrmsAttributeDefinition.Builder attrDefBuilder =
                        KrmsAttributeDefinition.Builder.create(null, attributeEntry.getKey(), type.getNamespace());
                krmsAttributeDefinitionService.createAttributeDefinition(attrDefBuilder.build());
          //  }
        }

        naturalLanguageTemplate = naturalLanguageTemplateBoServiceImpl.createNaturalLanguageTemplate(def);
    }

}
