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
import org.kuali.rice.krms.api.repository.ReferenceObjectBindingGenTest;
import org.kuali.rice.krms.api.repository.reference.ReferenceObjectBinding;
import static org.mockito.Mockito.*;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public final class ReferenceObjectBindingBoServiceImplGenTest {

    ReferenceObjectBindingBoServiceImpl referenceObjectBindingBoServiceImpl;
    ReferenceObjectBinding referenceObjectBinding;

    ReferenceObjectBinding getReferenceObjectBinding() {
        return referenceObjectBinding;
    }

    public void setReferenceObjectBindingBoServiceImpl(ReferenceObjectBindingBoServiceImpl impl) {
        this.referenceObjectBindingBoServiceImpl = impl;
    }

    public static org.kuali.rice.krms.impl.repository.ReferenceObjectBindingBoServiceImplGenTest create(ReferenceObjectBindingBoServiceImpl impl) {
        org.kuali.rice.krms.impl.repository.ReferenceObjectBindingBoServiceImplGenTest test = new org.kuali.rice.krms.impl.repository.ReferenceObjectBindingBoServiceImplGenTest();
        test.setReferenceObjectBindingBoServiceImpl(impl);
        return test;
    }

    @Before
    public void setUp() {
        referenceObjectBindingBoServiceImpl = new ReferenceObjectBindingBoServiceImpl();
        referenceObjectBindingBoServiceImpl.setBusinessObjectService(mock(BusinessObjectService.class));// TODO Import static org.mockito.Mockito.*;
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_findReferenceObjectBindingsByCollectionName_null_fail() {
        referenceObjectBindingBoServiceImpl.findReferenceObjectBindingsByCollectionName(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_findReferenceObjectBindingsByKrmsDiscriminatorType_null_fail() {
        referenceObjectBindingBoServiceImpl.findReferenceObjectBindingsByKrmsDiscriminatorType(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_findReferenceObjectBindingsByKrmsObject_null_fail() {
        referenceObjectBindingBoServiceImpl.findReferenceObjectBindingsByKrmsObject(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_findReferenceObjectBindingsByNamespace_null_fail() {
        referenceObjectBindingBoServiceImpl.findReferenceObjectBindingsByNamespace(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_findReferenceObjectBindingsByReferenceDiscriminatorType_null_fail() {
        referenceObjectBindingBoServiceImpl.findReferenceObjectBindingsByReferenceDiscriminatorType(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_findReferenceObjectBindingsByReferenceObject_null_fail() {
        referenceObjectBindingBoServiceImpl.findReferenceObjectBindingsByReferenceObject(null);
    }

    @Test
    public void test_from_null_yields_null() {
        assert(referenceObjectBindingBoServiceImpl.from(null) == null);
    }

    @Test
    public void test_from() {
        ReferenceObjectBinding def = ReferenceObjectBindingGenTest.buildFullReferenceObjectBinding();
        ReferenceObjectBindingBo referenceObjectBindingBo = referenceObjectBindingBoServiceImpl.from(def);
        assert(referenceObjectBindingBo.getKrmsDiscriminatorType().equals(def.getKrmsDiscriminatorType()));
        assert(referenceObjectBindingBo.getKrmsObjectId().equals(def.getKrmsObjectId()));
        assert(referenceObjectBindingBo.getNamespace().equals(def.getNamespace()));
        assert(referenceObjectBindingBo.getReferenceDiscriminatorType().equals(def.getReferenceDiscriminatorType()));
        assert(referenceObjectBindingBo.getReferenceObjectId().equals(def.getReferenceObjectId()));
        assert(referenceObjectBindingBo.getId().equals(def.getId()));
    }

    @Test
    public void test_to() {
        ReferenceObjectBinding def = ReferenceObjectBindingGenTest.buildFullReferenceObjectBinding();
        ReferenceObjectBindingBo referenceObjectBindingBo = referenceObjectBindingBoServiceImpl.from(def);
        ReferenceObjectBinding def2 = ReferenceObjectBindingBo.to(referenceObjectBindingBo);
        assert(def.equals(def2));
    }

    @Test
    public void test_createReferenceObjectBinding() {
        ReferenceObjectBinding def = ReferenceObjectBindingGenTest.buildFullReferenceObjectBinding();
        referenceObjectBinding = referenceObjectBindingBoServiceImpl.createReferenceObjectBinding(def);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_createReferenceObjectBinding_null_fail() {
        referenceObjectBindingBoServiceImpl.createReferenceObjectBinding(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_updateReferenceObjectBinding_null_fail() {
        referenceObjectBindingBoServiceImpl.updateReferenceObjectBinding(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_deleteReferenceObjectBinding_null_fail() {
        referenceObjectBindingBoServiceImpl.deleteReferenceObjectBinding(null);
    }

//    void create() { // TODO gen
//        ReferenceObjectBinding def = ReferenceObjectBindingGenTest.buildFullFKReferenceObjectBinding(params);
//    }

}
