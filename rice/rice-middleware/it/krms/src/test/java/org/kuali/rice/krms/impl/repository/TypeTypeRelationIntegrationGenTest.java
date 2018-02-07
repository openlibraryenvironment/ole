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
import org.kuali.rice.krms.api.repository.typerelation.RelationshipType;
import org.kuali.rice.krms.api.repository.typerelation.TypeTypeRelation;
import org.kuali.rice.krms.test.AbstractBoTest;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public final class TypeTypeRelationIntegrationGenTest extends AbstractBoTest{

    TypeTypeRelationBoServiceImpl typeTypeRelationBoServiceImpl;
    TypeTypeRelation typeTypeRelation;
    KrmsTypeBoServiceImpl krmsTypeBoServiceImpl;

    TypeTypeRelation getTypeTypeRelation() {
        return typeTypeRelation;
    }

    /**
     * Note lower case u, do not override superclasses setUp
     *
     */
    @Before
    public void setup() {
        typeTypeRelationBoServiceImpl = new TypeTypeRelationBoServiceImpl();
        typeTypeRelationBoServiceImpl.setBusinessObjectService(getBoService());
        krmsTypeBoServiceImpl = new KrmsTypeBoServiceImpl();
        krmsTypeBoServiceImpl.setBusinessObjectService(getBoService());
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_findTypeTypeRelationsByFromType_null_fail() {
        (TypeTypeRelationBoServiceImplGenTest.create(typeTypeRelationBoServiceImpl)).test_findTypeTypeRelationsByFromType_null_fail();
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_findTypeTypeRelationsByToType_null_fail() {
        (TypeTypeRelationBoServiceImplGenTest.create(typeTypeRelationBoServiceImpl)).test_findTypeTypeRelationsByToType_null_fail();
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_findTypeTypeRelationsByRelationshipType_null_fail() {
        (TypeTypeRelationBoServiceImplGenTest.create(typeTypeRelationBoServiceImpl)).test_findTypeTypeRelationsByRelationshipType_null_fail();
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_findTypeTypeRelationsBySequenceNumber_null_fail() {
        (TypeTypeRelationBoServiceImplGenTest.create(typeTypeRelationBoServiceImpl)).test_findTypeTypeRelationsBySequenceNumber_null_fail();
    }

    @Test
    public void test_from_null_yields_null() {
        (TypeTypeRelationBoServiceImplGenTest.create(typeTypeRelationBoServiceImpl)).test_from_null_yields_null();
    }

    @Test
    public void test_to() {
        (TypeTypeRelationBoServiceImplGenTest.create(typeTypeRelationBoServiceImpl)).test_to();
    }

    @Test
    public void test_createTypeTypeRelation() {
        KrmsTypeIntegrationGenTest krmsTypeTest = new KrmsTypeIntegrationGenTest();
        krmsTypeTest.setup(); // Note lowercase u
        krmsTypeTest.test_createKrmsType();
        KrmsTypeDefinition krmsType = krmsTypeTest.getKrmsType();
        TypeTypeRelationBoServiceImplGenTest test = TypeTypeRelationBoServiceImplGenTest.create(typeTypeRelationBoServiceImpl);
        test.createTypeTypeRelation(krmsType, krmsType); // TODO gen handle more than 1 of the same type
        typeTypeRelation = test.getTypeTypeRelation();
        assert(typeTypeRelation != null);
        assert(typeTypeRelation.getId() != null);
    }

    @Test(expected = java.lang.IllegalStateException.class)
    public void test_createTypeTypeRelation_fail_existing() {
        test_createTypeTypeRelation();
        test_createTypeTypeRelation();
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_createTypeTypeRelation_null_fail() {
        (TypeTypeRelationBoServiceImplGenTest.create(typeTypeRelationBoServiceImpl)).test_createTypeTypeRelation_null_fail();
    }

    @Test
    public void test_getTypeTypeRelation() {
        test_createTypeTypeRelation();
        TypeTypeRelation def = getTypeTypeRelation();
        TypeTypeRelation def2 = typeTypeRelationBoServiceImpl.getTypeTypeRelation(def.getId());
        assert(def2 != null);
        assert(def2.equals(def));
    }

    @Test
    public void test_updateTypeTypeRelation() {
        test_createTypeTypeRelation();
        TypeTypeRelation def = getTypeTypeRelation();
        String id = def.getId();
        assert(!RelationshipType.USAGE_ALLOWED.equals(def.getRelationshipType()));
        TypeTypeRelationBo bo = typeTypeRelationBoServiceImpl.from(def);
        bo.setRelationshipType(RelationshipType.USAGE_ALLOWED);
        typeTypeRelationBoServiceImpl.updateTypeTypeRelation(TypeTypeRelation.Builder.create(bo).build());
        TypeTypeRelation def2 = typeTypeRelationBoServiceImpl.getTypeTypeRelation(id);
        assert(RelationshipType.USAGE_ALLOWED.equals(def2.getRelationshipType()));
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_updateTypeTypeRelation_null_fail() {
        (TypeTypeRelationBoServiceImplGenTest.create(typeTypeRelationBoServiceImpl)).test_updateTypeTypeRelation_null_fail();
    }

    @Test
    public void test_deleteTypeTypeRelation() {
        test_createTypeTypeRelation();
        TypeTypeRelation def = getTypeTypeRelation();
        String id = def.getId();
        typeTypeRelationBoServiceImpl.deleteTypeTypeRelation(id);
        TypeTypeRelation def2 = typeTypeRelationBoServiceImpl.getTypeTypeRelation(id);
        assert(def2 == null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_deleteTypeTypeRelation_null_fail() {
        (TypeTypeRelationBoServiceImplGenTest.create(typeTypeRelationBoServiceImpl)).test_deleteTypeTypeRelation_null_fail();
    }

}
