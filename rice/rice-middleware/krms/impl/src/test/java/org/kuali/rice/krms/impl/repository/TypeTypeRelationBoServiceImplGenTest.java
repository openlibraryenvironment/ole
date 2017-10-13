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
import org.kuali.rice.krms.api.repository.TypeTypeRelationGenTest;
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition;
import org.kuali.rice.krms.api.repository.typerelation.TypeTypeRelation;
import static org.mockito.Mockito.*;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public final class TypeTypeRelationBoServiceImplGenTest {

    TypeTypeRelationBoServiceImpl typeTypeRelationBoServiceImpl;
    TypeTypeRelation typeTypeRelation;

    TypeTypeRelation getTypeTypeRelation() {
        return typeTypeRelation;
    }

    public void setTypeTypeRelationBoServiceImpl(TypeTypeRelationBoServiceImpl impl) {
        this.typeTypeRelationBoServiceImpl = impl;
    }

    public static org.kuali.rice.krms.impl.repository.TypeTypeRelationBoServiceImplGenTest create(TypeTypeRelationBoServiceImpl impl) {
        org.kuali.rice.krms.impl.repository.TypeTypeRelationBoServiceImplGenTest test = new org.kuali.rice.krms.impl.repository.TypeTypeRelationBoServiceImplGenTest();
        test.setTypeTypeRelationBoServiceImpl(impl);
        return test;
    }

    @Before
    public void setUp() {
        typeTypeRelationBoServiceImpl = new TypeTypeRelationBoServiceImpl();
        typeTypeRelationBoServiceImpl.setBusinessObjectService(mock(BusinessObjectService.class));
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_findTypeTypeRelationsByFromType_null_fail() {
        typeTypeRelationBoServiceImpl.findTypeTypeRelationsByFromType(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_findTypeTypeRelationsByToType_null_fail() {
        typeTypeRelationBoServiceImpl.findTypeTypeRelationsByToType(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_findTypeTypeRelationsByRelationshipType_null_fail() {
        typeTypeRelationBoServiceImpl.findTypeTypeRelationsByRelationshipType(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_findTypeTypeRelationsBySequenceNumber_null_fail() {
        typeTypeRelationBoServiceImpl.findTypeTypeRelationsBySequenceNumber(null);
    }

    @Test
    public void test_from_null_yields_null() {
        assert(typeTypeRelationBoServiceImpl.from(null) == null);
    }

    @Test
    public void test_from() {
        TypeTypeRelation def = TypeTypeRelationGenTest.buildFullTypeTypeRelation();
        TypeTypeRelationBo typeTypeRelationBo = typeTypeRelationBoServiceImpl.from(def);
        assert(typeTypeRelationBo.getFromTypeId().equals(def.getFromTypeId()));
        assert(typeTypeRelationBo.getToTypeId().equals(def.getToTypeId()));
        assert(typeTypeRelationBo.getId().equals(def.getId()));
    }

    @Test
    public void test_to() {
        TypeTypeRelation def = TypeTypeRelationGenTest.buildFullTypeTypeRelation();
        TypeTypeRelationBo typeTypeRelationBo = typeTypeRelationBoServiceImpl.from(def);
        TypeTypeRelation def2 = TypeTypeRelationBo.to(typeTypeRelationBo);
        assert(def.equals(def2));
    }

    @Test
    public void test_createTypeTypeRelation() {
        TypeTypeRelation def = TypeTypeRelationGenTest.buildFullTypeTypeRelation();
        typeTypeRelation = typeTypeRelationBoServiceImpl.createTypeTypeRelation(def);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_createTypeTypeRelation_null_fail() {
        typeTypeRelationBoServiceImpl.createTypeTypeRelation(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_updateTypeTypeRelation_null_fail() {
        typeTypeRelationBoServiceImpl.updateTypeTypeRelation(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_deleteTypeTypeRelation_null_fail() {
        typeTypeRelationBoServiceImpl.deleteTypeTypeRelation(null);
    }

    void createTypeTypeRelation(KrmsTypeDefinition fromType, KrmsTypeDefinition toType) {
        TypeTypeRelation def = TypeTypeRelationGenTest.buildFullFKTypeTypeRelation(fromType, toType);
        typeTypeRelation = typeTypeRelationBoServiceImpl.createTypeTypeRelation(def);
    }

}
