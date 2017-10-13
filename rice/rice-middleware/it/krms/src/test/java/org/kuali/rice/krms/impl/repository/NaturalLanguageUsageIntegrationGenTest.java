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

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.krms.api.repository.language.NaturalLanguageTemplate;
import org.kuali.rice.krms.api.repository.language.NaturalLanguageUsage;
import org.kuali.rice.krms.test.AbstractBoTest;

import java.util.List;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public final class NaturalLanguageUsageIntegrationGenTest extends AbstractBoTest{

    NaturalLanguageUsageBoServiceImpl naturalLanguageUsageBoServiceImpl;
    NaturalLanguageTemplateBoServiceImpl naturalLanguageTemplateBoServiceImpl;
    NaturalLanguageUsage naturalLanguageUsage;

    NaturalLanguageUsage getNaturalLanguageUsage() {
        return naturalLanguageUsage;
    }

    /**
     * Note lower case u, do not override superclasses setUp
     *
     */
    @Before
    public void setup() {
        naturalLanguageUsageBoServiceImpl = new NaturalLanguageUsageBoServiceImpl();
        naturalLanguageUsageBoServiceImpl.setBusinessObjectService(getBoService());

        // NOTE: this is not fully wired, we only need the BusinessObjectService for what we do with this impl
        naturalLanguageTemplateBoServiceImpl = new NaturalLanguageTemplateBoServiceImpl();
        naturalLanguageTemplateBoServiceImpl.setBusinessObjectService(getBoService());
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_findNaturalLanguageUsagesByName_null_fail() {
        (NaturalLanguageUsageBoServiceImplGenTest.create(naturalLanguageUsageBoServiceImpl)).test_findNaturalLanguageUsagesByName_null_fail();
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_findNaturalLanguageUsagesByDescription_null_fail() {
        (NaturalLanguageUsageBoServiceImplGenTest.create(naturalLanguageUsageBoServiceImpl)).test_findNaturalLanguageUsagesByDescription_null_fail();
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_findNaturalLanguageUsagesByNamespace_null_fail() {
        (NaturalLanguageUsageBoServiceImplGenTest.create(naturalLanguageUsageBoServiceImpl)).test_findNaturalLanguageUsagesByNamespace_null_fail();
    }

    @Test
    public void test_from_null_yields_null() {
        (NaturalLanguageUsageBoServiceImplGenTest.create(naturalLanguageUsageBoServiceImpl)).test_from_null_yields_null();
    }

    @Test
    public void test_to() {
        (NaturalLanguageUsageBoServiceImplGenTest.create(naturalLanguageUsageBoServiceImpl)).test_to();
    }

    @Test
    public void test_createNaturalLanguageUsage() {
        NaturalLanguageUsageBoServiceImplGenTest test = NaturalLanguageUsageBoServiceImplGenTest.create(naturalLanguageUsageBoServiceImpl);
        test.test_createNaturalLanguageUsage();
        naturalLanguageUsage = test.getNaturalLanguageUsage();
        assert(naturalLanguageUsage != null);
        assert(naturalLanguageUsage.getId() != null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_createNaturalLanguageUsage_null_fail() {
        (NaturalLanguageUsageBoServiceImplGenTest.create(naturalLanguageUsageBoServiceImpl)).test_createNaturalLanguageUsage_null_fail();
    }

    @Test
    public void test_getNaturalLanguageUsage() {
        test_createNaturalLanguageUsage();
        NaturalLanguageUsage def = getNaturalLanguageUsage();
        NaturalLanguageUsage def2 = naturalLanguageUsageBoServiceImpl.getNaturalLanguageUsage(def.getId());
        assert(def2 != null);
        assert(def2.equals(def));
    }

    @Test
    public void test_updateNaturalLanguageUsage() {
        test_createNaturalLanguageUsage();
        NaturalLanguageUsage def = getNaturalLanguageUsage();
        String id = def.getId();
        assert(!"UpdateTest".equals(def.getName()));
        NaturalLanguageUsageBo bo = naturalLanguageUsageBoServiceImpl.from(def);
        bo.setName("UpdateTest");
        naturalLanguageUsageBoServiceImpl.updateNaturalLanguageUsage(NaturalLanguageUsage.Builder.create(bo).build());
        NaturalLanguageUsage def2 = naturalLanguageUsageBoServiceImpl.getNaturalLanguageUsage(id);
        assert("UpdateTest".equals(def2.getName()));
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_updateNaturalLanguageUsage_null_fail() {
        (NaturalLanguageUsageBoServiceImplGenTest.create(naturalLanguageUsageBoServiceImpl)).test_updateNaturalLanguageUsage_null_fail();
    }

    @Test
    public void test_deleteNaturalLanguageUsage() {
        test_createNaturalLanguageUsage();
        NaturalLanguageUsage def = getNaturalLanguageUsage();
        String id = def.getId();

        // make sure there aren't any templates for this usage which will cause deleting the usage to fail
        List<NaturalLanguageTemplate> templates =
                naturalLanguageTemplateBoServiceImpl.findNaturalLanguageTemplatesByNaturalLanguageUsage(id);

        if (!CollectionUtils.isEmpty(templates)) {
            for (NaturalLanguageTemplate template : templates) {
                naturalLanguageTemplateBoServiceImpl.deleteNaturalLanguageTemplate(template.getId());
            }
        }

        naturalLanguageUsageBoServiceImpl.deleteNaturalLanguageUsage(id);
        NaturalLanguageUsage def2 = naturalLanguageUsageBoServiceImpl.getNaturalLanguageUsage(id);
        assert(def2 == null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_deleteNaturalLanguageUsage_null_fail() {
        (NaturalLanguageUsageBoServiceImplGenTest.create(naturalLanguageUsageBoServiceImpl)).test_deleteNaturalLanguageUsage_null_fail();
    }

}