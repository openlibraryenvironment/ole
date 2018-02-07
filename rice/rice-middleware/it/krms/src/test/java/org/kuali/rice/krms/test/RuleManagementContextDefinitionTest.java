/*
 * Copyright 2006-2013 The Kuali Foundation
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

package org.kuali.rice.krms.test;

import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.krad.criteria.CriteriaLookupDaoProxy;
import org.kuali.rice.krad.criteria.CriteriaLookupServiceImpl;
import org.kuali.rice.krms.api.repository.context.ContextDefinition;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.kuali.rice.core.api.criteria.PredicateFactory.in;

/**
 *   RuleManagementContextDefinitionTest is to test the methods of ruleManagementServiceImpl relating to ContextDefinitions
 *
 *   Each test focuses on one of the methods.
 */
public class RuleManagementContextDefinitionTest  extends RuleManagementBaseTest {
    @Override
    @Before
    public void setClassDiscriminator() {
        // set a unique discriminator for test objects of this class
        CLASS_DISCRIMINATOR = "RMCDT";
    }


    /**
     *  Test testCreateContext()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl
     *      .createContext(ContextDefinition contextDefinition) method
     */
    @Test
    public void testCreateContext() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t0 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t0");

        // create a context
        ContextDefinition.Builder contextDefinitionBuilder = ContextDefinition.Builder.create(t0.namespaceName, t0.context0_Name);
        contextDefinitionBuilder.setId(t0.context0_Id);
        ruleManagementService.createContext(contextDefinitionBuilder.build());

        // try to create context that already exists
        try {
            ruleManagementService.createContext(contextDefinitionBuilder.build());
            fail("Should have thrown IllegalStateException: the context to create already exists");
        } catch (IllegalStateException e) {
            // throws IllegalStateException: the context to create already exists
        }

        // verify created context
        ContextDefinition context = ruleManagementService.getContext(t0.context0_Id);
        assertEquals("Unexpected namespace on created context",t0.namespaceName,context.getNamespace());
        assertEquals("Unexpected name on created context",t0.context0_Name,context.getName());
        assertEquals("Unexpected context id on returned context",t0.context0_Id,context.getId());

        // build context with null Namespace
        try {
            contextDefinitionBuilder = ContextDefinition.Builder.create(null, t0.context1_Name);
            fail("Should have thrown IllegalArgumentException: namespace is blank");
        } catch (IllegalArgumentException e) {
            // throws IllegalArgumentException: namespace is blank
        }

        // build context with null Name
        try {
            contextDefinitionBuilder = ContextDefinition.Builder.create(t0.namespaceName, null);
            fail("Should have thrown IllegalArgumentException: name is blank");
        } catch (IllegalArgumentException e) {
            // throws IllegalArgumentException: name is blank
        }
    }


    /**
     *  Test testFindCreateContext()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl
     *      .findCreateContext(ContextDefinition contextDefinition) method
     */
    @Test
    public void testFindCreateContext() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t1 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t1");

        // findCreateContext a context which does not already exist
        ContextDefinition.Builder contextDefinitionBuilder = ContextDefinition.Builder.create(t1.namespaceName, t1.context0_Name);
        contextDefinitionBuilder.setId(t1.context0_Id);
        ContextDefinition context = ruleManagementService.findCreateContext(contextDefinitionBuilder.build());

        // verify created context
        assertEquals("Unexpected namespace on created context",t1.namespaceName,context.getNamespace());
        assertEquals("Unexpected name on created context",t1.context0_Name,context.getName());
        assertEquals("Unexpected context id on returned context",t1.context0_Id,context.getId());

        // try to findCreate context that already exists
        contextDefinitionBuilder = ContextDefinition.Builder.create(t1.namespaceName, t1.context0_Name);
        context = ruleManagementService.findCreateContext(contextDefinitionBuilder.build());

        // re-verify created context - id should be from original create
        assertEquals("Unexpected context id on returned context",t1.context0_Id,context.getId());
        assertEquals("Unexpected namespace on created context",t1.namespaceName,context.getNamespace());
        assertEquals("Unexpected name on created context",t1.context0_Name,context.getName());

        // test findCreate with null ContextDefinition
        try {
            ruleManagementService.findCreateContext(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            // throws NullPointerException
        }
    }


    /**
     *  Test testUpdateContext()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl
     *      .updateContext(ContextDefinition contextDefinition) method
     */
    @Test
    public void testUpdateContext() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t2 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t2");

        // build test Context
        ContextDefinition context = buildTestContext(t2.object0);
        // make sure it is cached to test update CacheEvict
        context = ruleManagementService.getContext(context.getId());

        // verify created context
        assertEquals("Unexpected namespace on created context",t2.namespaceName,context.getNamespace());
        assertEquals("Unexpected name on created context",t2.context0_Name,context.getName());
        assertEquals("Unexpected context id on returned context",t2.context0_Id,context.getId());
        assertNull("Context Description not yet set, should have been null",context.getDescription());
        assertEquals("Unexpected context active state",true,context.isActive());

        // update the context's namespace, name, description and set inactive
        ContextDefinition.Builder contextBuilder = ContextDefinition.Builder.create(context);
        contextBuilder.setNamespace(t2.namespaceName + "Changed");
        contextBuilder.setName(t2.context0_Name + "Changed");
        contextBuilder.setDescription(t2.context0_Descr + "Changed");
        contextBuilder.setActive(false);
        ruleManagementService.updateContext(contextBuilder.build());

        context = ruleManagementService.getContext(t2.context0_Id);
        // verify updated context
        assertEquals("Unexpected namespace on created context",t2.namespaceName + "Changed",context.getNamespace());
        assertEquals("Unexpected name on created context",t2.context0_Name + "Changed",context.getName());
        assertEquals("Unexpected context id on returned context",t2.context0_Id,context.getId());
        assertEquals("Unexpected context description on returned context",t2.context0_Descr + "Changed",context.getDescription());
        assertEquals("Unexpected contex active state",false,context.isActive());

        // try update on null Content
        try {
            ruleManagementService.updateContext(null);
            fail("Should have thrown IllegalArgumentException: context is null");
        } catch (IllegalArgumentException e) {
            // throws IllegalArgumentException: context is null
        }
    }


    /**
     *  Test testDeleteContext()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl
     *      .deleteContext(String contextId) method
     */
    @Test
    public void testDeleteContext() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t3 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t3");

        // build test Context
        ContextDefinition context = buildTestContext(t3.object0);
        // proof text context exists
        context = ruleManagementService.getContext(t3.context0_Id);
        assertEquals("Unexpected contex name returned ",t3.context0_Name,context.getName());

        // delete Context
        try {
            ruleManagementService.deleteContext(t3.context0_Id);
            fail("Should have thrown RiceIllegalArgumentException: not implemented yet");
        } catch (RiceIllegalArgumentException e) {
            // throws RiceIllegalArgumentException: not implemented yet
        }

        // proof text context deleted  (uncomment when implemented
        // context = ruleManagementServiceImpl.getContext(t3.context0_Id);
        // assertEquals("Unexpected contex name returned ",t3.context0_Name,context.getName());
    }


    /**
     *  Test testGetContext()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl
     *      .getContext(String contextId) method
     */
    @Test
    public void testGetContext() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t4 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t4");

        // build test Context
        buildTestContext(t4.object0);

        // read context
        ContextDefinition context = ruleManagementService.getContext(t4.context0_Id);

        //proof context was read
        assertEquals("Unexpected contex name returned ",t4.context0_Name,context.getName());

        assertNull("Should be null", ruleManagementService.getContext(null));
        assertNull("Should be null", ruleManagementService.getContext("   "));
        assertNull("Should be null", ruleManagementService.getContext("badValue"));
    }


    /**
     *  Test testGetContextByNameAndNamespace()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl
     *      .getContextByNameAndNamespace(String name, String namespace) method
     */
    @Test
    public void testGetContextByNameAndNamespace() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t5 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t5");

        // build test Context
        buildTestContext(t5.object0);

        // read context  ByNameAndNamespace
        ContextDefinition context = ruleManagementService.getContextByNameAndNamespace(t5.context0_Name,
                t5.namespaceName);

        assertEquals("Unexpected namespace on created context",t5.namespaceName,context.getNamespace());
        assertEquals("Unexpected name on created context",t5.context0_Name,context.getName());

        // test call with null name
        try {
            ruleManagementService.getContextByNameAndNamespace(null,t5.namespaceName);
            fail("Should have thrown IllegalArgumentException: name is null or blank");
        } catch (IllegalArgumentException e) {
            // throws IllegalArgumentException: name is null or blank
        }

        // test call with null namespace
        try {
            ruleManagementService.getContextByNameAndNamespace(null,t5.namespaceName);
            fail("Should have thrown IllegalArgumentException: namespace is null or blank");
        } catch (IllegalArgumentException e) {
            // throws IllegalArgumentException: namespace is null or blank
        }

        // test call with blank name
        try {
            ruleManagementService.getContextByNameAndNamespace("  ",t5.namespaceName);
            fail("Should have thrown IllegalArgumentException: name is null or blank");
        } catch (IllegalArgumentException e) {
            // throws IllegalArgumentException: name is null or blank
        }

        // test call with null namespace
        try {
            ruleManagementService.getContextByNameAndNamespace(t5.context0_Name,"  ");
            fail("Should have thrown IllegalArgumentException: namespace is null or blank");
        } catch (IllegalArgumentException e) {
            // throws IllegalArgumentException: namespace is null or blank
        }

        // test get with values for non-existent name and namespace
        assertNull("Should be null", ruleManagementService.getContextByNameAndNamespace("badValue", t5.namespaceName));
        assertNull("Should be null", ruleManagementService.getContextByNameAndNamespace(t5.context0_Name, "badValue"));
    }


    /**
     *  Test testFindContextIds()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl
     *      .findContextIds(QueryByCriteria queryByCriteria) method
     */
    @Test
    public void testFindContextIds() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t6 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t6");

        // build four test Contexts
        buildTestContext(t6.object0);
        buildTestContext(t6.object1);
        buildTestContext(t6.object2);
        buildTestContext(t6.object3);
        List<String> contextIds = Arrays.asList(t6.context0_Id,t6.context1_Id,t6.context2_Id,t6.context3_Id);

        QueryByCriteria.Builder builder = QueryByCriteria.Builder.create();

        builder.setPredicates(in("id", contextIds.toArray(new String[]{})));

        List<String> foundIds = ruleManagementService.findContextIds(builder.build());
        assertEquals("Should of found 4 contexts",4,foundIds.size());

        for (String contactId : contextIds) {
            assertTrue("Should have only these ids",foundIds.contains(contactId));
        }

        try {
            ruleManagementService.findContextIds(null);
            fail("Should have thrown IllegalArgumentException: criteria is null");
        } catch (IllegalArgumentException e) {
            // throws IllegalArgumentException: criteria is null
        }
    }
}
