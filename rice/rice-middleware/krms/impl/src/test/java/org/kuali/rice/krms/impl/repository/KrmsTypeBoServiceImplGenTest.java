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
import org.kuali.rice.krms.api.repository.KrmsTypeGenTest;
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition;

import static org.mockito.Mockito.mock;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public final class KrmsTypeBoServiceImplGenTest {

    org.kuali.rice.krms.impl.repository.KrmsTypeBoServiceImpl krmsTypeBoServiceImpl;
    KrmsTypeDefinition krmsType;

    KrmsTypeDefinition getKrmsType() {
        return krmsType;
    }

    KrmsTypeDefinition getKrmsTypeDefinition() {
        return getKrmsType();
    }

    public void setKrmsTypeBoServiceImpl(org.kuali.rice.krms.impl.repository.KrmsTypeBoServiceImpl impl) {
        this.krmsTypeBoServiceImpl = impl;
    }

    public static org.kuali.rice.krms.impl.repository.KrmsTypeBoServiceImplGenTest create(org.kuali.rice.krms.impl.repository.KrmsTypeBoServiceImpl impl) {
        org.kuali.rice.krms.impl.repository.KrmsTypeBoServiceImplGenTest test = new org.kuali.rice.krms.impl.repository.KrmsTypeBoServiceImplGenTest();
        test.setKrmsTypeBoServiceImpl(impl);
        return test;
    }

    @Before
    public void setUp() {
        krmsTypeBoServiceImpl = new KrmsTypeBoServiceImpl();
        KrmsAttributeDefinitionService mockAttributeService = mock(KrmsAttributeDefinitionService.class);
//        krmsTypeBoServiceImpl.setAttributeDefinitionService(mockAttributeService);
        krmsTypeBoServiceImpl.setBusinessObjectService(mock(BusinessObjectService.class));
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_getTypeById_null_fail() {
        krmsTypeBoServiceImpl.getTypeById(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_getTypeByName_null_fail() {
        krmsTypeBoServiceImpl.getTypeByName(null, null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_findAllTypesByNamespace_null_fail() {
        krmsTypeBoServiceImpl.findAllTypesByNamespace(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_findAllAgendaTypesByContextId_null_fail() {
        krmsTypeBoServiceImpl.findAllAgendaTypesByContextId(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_getAgendaTypeByAgendaTypeIdAndContextId_null_fail() {
        krmsTypeBoServiceImpl.getAgendaTypeByAgendaTypeIdAndContextId(null, null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_findAllRuleTypesByContextId_null_fail() {
        krmsTypeBoServiceImpl.findAllRuleTypesByContextId(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_getRuleTypeByRuleTypeIdAndContextId_null_fail() {
        krmsTypeBoServiceImpl.getRuleTypeByRuleTypeIdAndContextId(null, null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_findAllActionTypesByContextId_null_fail() {
        krmsTypeBoServiceImpl.findAllActionTypesByContextId(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_getActionTypeByActionTypeIdAndContextId_null_fail() {
        krmsTypeBoServiceImpl.getActionTypeByActionTypeIdAndContextId(null, null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_getAttributeDefinitionById_null_fail() {
        krmsTypeBoServiceImpl.getAttributeDefinitionById(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_getAttributeDefinitionByName_null_fail() {
        krmsTypeBoServiceImpl.getAttributeDefinitionByName(null, null);
    }

    @Test
    public void test_createKrmsType() {
        KrmsTypeDefinition def = KrmsTypeGenTest.buildFullKrmsTypeDefinition();

        krmsType = krmsTypeBoServiceImpl.getTypeByName(def.getNamespace(), def.getName());

        if (krmsType == null) {
            krmsType = krmsTypeBoServiceImpl.createKrmsType(def);
        }
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_createKrmsType_null_fail() {
        krmsTypeBoServiceImpl.createKrmsType(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_updateKrmsType_null_fail() {
        krmsTypeBoServiceImpl.updateKrmsType(null);
    }

}
