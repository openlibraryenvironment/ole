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
import org.kuali.rice.krms.api.repository.NaturalLanguageUsageGenTest;
import org.kuali.rice.krms.api.repository.language.NaturalLanguageUsage;

import static org.mockito.Mockito.mock;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public final class NaturalLanguageUsageBoServiceImplGenTest {

    NaturalLanguageUsageBoServiceImpl naturalLanguageUsageBoServiceImpl;
    NaturalLanguageUsage naturalLanguageUsage;

    NaturalLanguageUsage getNaturalLanguageUsage() {
        return naturalLanguageUsage;
    }

    public void setNaturalLanguageUsageBoServiceImpl(NaturalLanguageUsageBoServiceImpl impl) {
        this.naturalLanguageUsageBoServiceImpl = impl;
    }

    public static org.kuali.rice.krms.impl.repository.NaturalLanguageUsageBoServiceImplGenTest create(NaturalLanguageUsageBoServiceImpl impl) {
        org.kuali.rice.krms.impl.repository.NaturalLanguageUsageBoServiceImplGenTest test = new org.kuali.rice.krms.impl.repository.NaturalLanguageUsageBoServiceImplGenTest();
        test.setNaturalLanguageUsageBoServiceImpl(impl);
        return test;
    }

    @Before
    public void setUp() {
        naturalLanguageUsageBoServiceImpl = new NaturalLanguageUsageBoServiceImpl();
        naturalLanguageUsageBoServiceImpl.setBusinessObjectService(mock(BusinessObjectService.class));
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_findNaturalLanguageUsagesByName_null_fail() {
        naturalLanguageUsageBoServiceImpl.findNaturalLanguageUsagesByName(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_findNaturalLanguageUsagesByDescription_null_fail() {
        naturalLanguageUsageBoServiceImpl.findNaturalLanguageUsagesByDescription(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_findNaturalLanguageUsagesByNamespace_null_fail() {
        naturalLanguageUsageBoServiceImpl.findNaturalLanguageUsagesByNamespace(null);
    }

    @Test
    public void test_from_null_yields_null() {
        assert(naturalLanguageUsageBoServiceImpl.from(null) == null);
    }

    @Test
    public void test_from() {
        NaturalLanguageUsage def = NaturalLanguageUsageGenTest.buildFullNaturalLanguageUsage();
        NaturalLanguageUsageBo naturalLanguageUsageBo = naturalLanguageUsageBoServiceImpl.from(def);
        assert(naturalLanguageUsageBo.getName().equals(def.getName()));
        assert(naturalLanguageUsageBo.getNamespace().equals(def.getNamespace()));
        assert(naturalLanguageUsageBo.getId().equals(def.getId()));
    }

    @Test
    public void test_to() {
        NaturalLanguageUsage def = NaturalLanguageUsageGenTest.buildFullNaturalLanguageUsage();
        NaturalLanguageUsageBo naturalLanguageUsageBo = naturalLanguageUsageBoServiceImpl.from(def);
        NaturalLanguageUsage def2 = NaturalLanguageUsageBo.to(naturalLanguageUsageBo);
        assert(def.equals(def2));
    }

    @Test
    public void test_createNaturalLanguageUsage() {
        NaturalLanguageUsage def = NaturalLanguageUsageGenTest.buildFullNaturalLanguageUsage();

        naturalLanguageUsage = naturalLanguageUsageBoServiceImpl.getNaturalLanguageUsageByName(def.getNamespace(), def.getName());

        if (naturalLanguageUsage == null) {
            naturalLanguageUsage = naturalLanguageUsageBoServiceImpl.createNaturalLanguageUsage(def);
        }
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_createNaturalLanguageUsage_null_fail() {
        naturalLanguageUsageBoServiceImpl.createNaturalLanguageUsage(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_updateNaturalLanguageUsage_null_fail() {
        naturalLanguageUsageBoServiceImpl.updateNaturalLanguageUsage(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_deleteNaturalLanguageUsage_null_fail() {
        naturalLanguageUsageBoServiceImpl.deleteNaturalLanguageUsage(null);
    }

//    void createNaturalLanguageUsage() { // TODO gen what to do when no FKs?
//        // TODO change the Object type of the parameters
//        NaturalLanguageUsage def = NaturalLanguageUsageGenTest.buildFullFKNaturalLanguageUsage();
//        naturalLanguageUsage = naturalLanguageUsageBoServiceImpl.createNaturalLanguageUsage(def);
//    }

}
