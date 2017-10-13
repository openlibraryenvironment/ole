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
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition;
import org.kuali.rice.krms.test.AbstractBoTest;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public final class KrmsTypeIntegrationGenTest extends AbstractBoTest {


    KrmsTypeBoServiceImpl krmsTypeBoServiceImpl;
    KrmsAttributeDefinitionService krmsAttributeDefinitionService;
    KrmsTypeDefinition krmsType; // TODO gen

    /**
     * Note lower case u, do not override superclasses setUp
     *
     */
    @Before
    public void setup() {
        krmsTypeBoServiceImpl = new KrmsTypeBoServiceImpl();
        krmsAttributeDefinitionService = KrmsRepositoryServiceLocator.getKrmsAttributeDefinitionService();
//        krmsTypeBoServiceImpl.setAttributeDefinitionService(krmsAttributeDefinitionService);
        krmsTypeBoServiceImpl.setBusinessObjectService(getBoService());
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_getTypeById_null_fail() {
        (KrmsTypeBoServiceImplGenTest.create(krmsTypeBoServiceImpl)).test_getTypeById_null_fail();
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_getTypeByName_null_fail() {
        (KrmsTypeBoServiceImplGenTest.create(krmsTypeBoServiceImpl)).test_getTypeByName_null_fail();
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_findAllTypesByNamespace_null_fail() {
        (KrmsTypeBoServiceImplGenTest.create(krmsTypeBoServiceImpl)).test_findAllTypesByNamespace_null_fail();
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_findAllAgendaTypesByContextId_null_fail() {
        (KrmsTypeBoServiceImplGenTest.create(krmsTypeBoServiceImpl)).test_findAllAgendaTypesByContextId_null_fail();
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_getAgendaTypeByAgendaTypeIdAndContextId_null_fail() {
        (KrmsTypeBoServiceImplGenTest.create(krmsTypeBoServiceImpl)).test_getAgendaTypeByAgendaTypeIdAndContextId_null_fail();
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_findAllRuleTypesByContextId_null_fail() {
        (KrmsTypeBoServiceImplGenTest.create(krmsTypeBoServiceImpl)).test_findAllRuleTypesByContextId_null_fail();
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_getRuleTypeByRuleTypeIdAndContextId_null_fail() {
        (KrmsTypeBoServiceImplGenTest.create(krmsTypeBoServiceImpl)).test_getRuleTypeByRuleTypeIdAndContextId_null_fail();
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_findAllActionTypesByContextId_null_fail() {
        (KrmsTypeBoServiceImplGenTest.create(krmsTypeBoServiceImpl)).test_findAllActionTypesByContextId_null_fail();
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_getActionTypeByActionTypeIdAndContextId_null_fail() {
        (KrmsTypeBoServiceImplGenTest.create(krmsTypeBoServiceImpl)).test_getActionTypeByActionTypeIdAndContextId_null_fail();
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_getAttributeDefinitionById_null_fail() {
        (KrmsTypeBoServiceImplGenTest.create(krmsTypeBoServiceImpl)).test_getAttributeDefinitionById_null_fail();
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_getAttributeDefinitionByName_null_fail() {
        (KrmsTypeBoServiceImplGenTest.create(krmsTypeBoServiceImpl)).test_getAttributeDefinitionByName_null_fail();
    }

    @Test
    public void test_createKrmsType() {
        KrmsTypeBoServiceImplGenTest test = KrmsTypeBoServiceImplGenTest.create(krmsTypeBoServiceImpl);
        test.test_createKrmsType();
        krmsType  = test.getKrmsTypeDefinition(); // TODO gen
        assert(krmsType != null);
        assert(krmsType.getId() != null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_createKrmsType_null_fail() {
        (KrmsTypeBoServiceImplGenTest.create(krmsTypeBoServiceImpl)).test_createKrmsType_null_fail();
    }

    @Test
    public void test_updateKrmsType() {
        KrmsTypeBoServiceImplGenTest test = KrmsTypeBoServiceImplGenTest.create(krmsTypeBoServiceImpl);
        test.test_createKrmsType();
        KrmsTypeDefinition def = test.getKrmsTypeDefinition();
        String id = def.getId();
        assert(!"UpdateTest".equals(def.getName()));
        KrmsTypeBo bo = krmsTypeBoServiceImpl.from(def);
        bo.setName("UpdateTest");
        krmsTypeBoServiceImpl.updateKrmsType(KrmsTypeDefinition.Builder.create(bo).build());
        KrmsTypeDefinition def2 = krmsTypeBoServiceImpl.getTypeById(id);
        assert("UpdateTest".equals(def2.getName()));
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_updateKrmsType_null_fail() {
        (KrmsTypeBoServiceImplGenTest.create(krmsTypeBoServiceImpl)).test_updateKrmsType_null_fail();
    }

    public KrmsTypeDefinition getKrmsType() { // TODO gen
        return krmsType;
    }
}