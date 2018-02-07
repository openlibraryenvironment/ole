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
import org.kuali.rice.core.api.criteria.PredicateFactory;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.krad.criteria.CriteriaLookupDaoProxy;
import org.kuali.rice.krad.criteria.CriteriaLookupServiceImpl;
import org.kuali.rice.krms.api.repository.reference.ReferenceObjectBinding;
import org.kuali.rice.krms.test.AbstractBoTest;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public final class ReferenceObjectBindingIntegrationGenTest extends AbstractBoTest {


    ReferenceObjectBindingBoServiceImpl referenceObjectBindingBoServiceImpl;
    ReferenceObjectBinding referenceObjectBinding;

    ReferenceObjectBinding getReferenceObjectBinding() {
        return referenceObjectBinding;
    }

    /**
     * Note lower case u, do not override superclasses setUp
     *
     */
    @Before
    public void setup() {
        referenceObjectBindingBoServiceImpl = new ReferenceObjectBindingBoServiceImpl();
        referenceObjectBindingBoServiceImpl.setBusinessObjectService(getBoService());
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_findReferenceObjectBindingsByCollectionName_null_fail() {
        (ReferenceObjectBindingBoServiceImplGenTest.create(referenceObjectBindingBoServiceImpl)).test_findReferenceObjectBindingsByCollectionName_null_fail();
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_findReferenceObjectBindingsByKrmsDiscriminatorType_null_fail() {
        (ReferenceObjectBindingBoServiceImplGenTest.create(referenceObjectBindingBoServiceImpl)).test_findReferenceObjectBindingsByKrmsDiscriminatorType_null_fail();
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_findReferenceObjectBindingsByKrmsObject_null_fail() {
        (ReferenceObjectBindingBoServiceImplGenTest.create(referenceObjectBindingBoServiceImpl)).test_findReferenceObjectBindingsByKrmsObject_null_fail();
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_findReferenceObjectBindingsByNamespace_null_fail() {
        (ReferenceObjectBindingBoServiceImplGenTest.create(referenceObjectBindingBoServiceImpl)).test_findReferenceObjectBindingsByNamespace_null_fail();
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_findReferenceObjectBindingsByReferenceDiscriminatorType_null_fail() {
        (ReferenceObjectBindingBoServiceImplGenTest.create(referenceObjectBindingBoServiceImpl)).test_findReferenceObjectBindingsByReferenceDiscriminatorType_null_fail();
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_findReferenceObjectBindingsByReferenceObject_null_fail() {
        (ReferenceObjectBindingBoServiceImplGenTest.create(referenceObjectBindingBoServiceImpl)).test_findReferenceObjectBindingsByReferenceObject_null_fail();
    }

    @Test
    public void test_from_null_yields_null() {
        (ReferenceObjectBindingBoServiceImplGenTest.create(referenceObjectBindingBoServiceImpl)).test_from_null_yields_null();
    }

    @Test
    public void test_to() {
        (ReferenceObjectBindingBoServiceImplGenTest.create(referenceObjectBindingBoServiceImpl)).test_to();
    }

    @Test
    public void test_createReferenceObjectBinding() {
        ReferenceObjectBindingBoServiceImplGenTest test = ReferenceObjectBindingBoServiceImplGenTest.create(referenceObjectBindingBoServiceImpl);
        test.test_createReferenceObjectBinding();
        referenceObjectBinding = test.getReferenceObjectBinding();
        assert(referenceObjectBinding != null);
        assert(referenceObjectBinding.getId() != null);
    }

    @Test(expected = java.lang.IllegalStateException.class)
    public void test_createReferenceObjectBinding_fail_existing() {
        test_createReferenceObjectBinding();
        test_createReferenceObjectBinding();
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_createReferenceObjectBinding_null_fail() {
        (ReferenceObjectBindingBoServiceImplGenTest.create(referenceObjectBindingBoServiceImpl)).test_createReferenceObjectBinding_null_fail();
    }

    @Test
    public void test_getReferenceObjectBinding() {
        test_createReferenceObjectBinding();
        ReferenceObjectBinding def = getReferenceObjectBinding();
        ReferenceObjectBinding def2 = referenceObjectBindingBoServiceImpl.getReferenceObjectBinding(def.getId());
        assert(def2 != null);
        assert(def2.equals(def));
    }

    @Test
    public void test_updateReferenceObjectBinding() {
        test_createReferenceObjectBinding();
        ReferenceObjectBinding def = getReferenceObjectBinding();
        String id = def.getId();
        assert(!"UpdateTest".equals(def.getCollectionName()));
        ReferenceObjectBindingBo bo = referenceObjectBindingBoServiceImpl.from(def);
        bo.setCollectionName("UpdateTest");
        referenceObjectBindingBoServiceImpl.updateReferenceObjectBinding(ReferenceObjectBinding.Builder.create(bo).build());
        ReferenceObjectBinding def2 = referenceObjectBindingBoServiceImpl.getReferenceObjectBinding(id);
        assert("UpdateTest".equals(def2.getCollectionName()));
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_updateReferenceObjectBinding_null_fail() {
        (ReferenceObjectBindingBoServiceImplGenTest.create(referenceObjectBindingBoServiceImpl)).test_updateReferenceObjectBinding_null_fail();
    }

    @Test
    public void test_deleteReferenceObjectBinding() {
        test_createReferenceObjectBinding();
        ReferenceObjectBinding def = getReferenceObjectBinding();
        String id = def.getId();
        referenceObjectBindingBoServiceImpl.deleteReferenceObjectBinding(id);
        ReferenceObjectBinding def2 = referenceObjectBindingBoServiceImpl.getReferenceObjectBinding(id);
        assert(def2 == null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_deleteReferenceObjectBinding_null_fail() {
        (ReferenceObjectBindingBoServiceImplGenTest.create(referenceObjectBindingBoServiceImpl)).test_deleteReferenceObjectBinding_null_fail();
    }

    @Test
    public void test_Find() {
        QueryByCriteria.Builder query = QueryByCriteria.Builder.create();
        query.setPredicates(PredicateFactory.equal("id", "ID"));
        CriteriaLookupServiceImpl criteria = new CriteriaLookupServiceImpl();
        criteria.setCriteriaLookupDao(new CriteriaLookupDaoProxy());
        referenceObjectBindingBoServiceImpl.setCriteriaLookupService(criteria);
        assert(referenceObjectBindingBoServiceImpl.findReferenceObjectBindingIds(query.build()).isEmpty());
        test_createReferenceObjectBinding();
        assert(!referenceObjectBindingBoServiceImpl.findReferenceObjectBindingIds(query.build()).isEmpty());
        assert("ID".equals(referenceObjectBindingBoServiceImpl.findReferenceObjectBindingIds(query.build()).get(0)));
    }
}
