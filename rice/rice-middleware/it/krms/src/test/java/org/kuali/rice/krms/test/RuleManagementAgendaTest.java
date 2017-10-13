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

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.krms.api.repository.agenda.AgendaDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaItemDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaTreeDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaTreeEntryDefinitionContract;
import org.kuali.rice.krms.api.repository.context.ContextDefinition;
import org.kuali.rice.krms.api.repository.rule.RuleDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition;
import org.kuali.rice.krms.impl.repository.RuleManagementServiceImpl;
import org.springmodules.orm.ojb.OjbOperationException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import static org.junit.Assert.*;
import static org.kuali.rice.core.api.criteria.PredicateFactory.equal;
import static org.kuali.rice.core.api.criteria.PredicateFactory.in;

/**
 *   RuleManagementAgendaTest is to test the methods of ruleManagementServiceImpl relating to krms Agendas
 *
 *   Each test focuses on one of the methods.
 */
public class RuleManagementAgendaTest extends RuleManagementBaseTest {
    @Override
    @Before
    public void setClassDiscriminator() {
        // set a unique discriminator for test objects of this class
        CLASS_DISCRIMINATOR = "RMAT";
    }

    /**
     *  Test testCreateAgenda()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .createAgenda(AgendaDefinition) method
     */
    @Test
    public void testCreateAgenda() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t0 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t0");

        String ruleId = buildTestRuleDefinition(t0.namespaceName, t0.object0).getId();
        String agendaId = createTestAgenda(t0.object0, /* createAttributes */ true).getId();
        buildTestAgendaItemDefinition(t0.agendaItem_Id, agendaId, ruleId);
        AgendaDefinition agendaDefinition = ruleManagementService.getAgenda(agendaId);

        assertTrue("Created agenda is not active", agendaDefinition.isActive());

        assertEquals("Expected Context not found",t0.contextId,agendaDefinition.getContextId());
        assertEquals("Expected AgendaId not found",t0.agenda_Id,agendaDefinition.getId());

        assertEquals("Expected AgendaItemId not found",t0.agendaItem_0_Id,agendaDefinition.getFirstItemId());
        assertEquals("Expected Rule of AgendaItem not found",t0.rule_0_Id,
                ruleManagementService.getAgendaItem(agendaDefinition.getFirstItemId()).getRule().getId());
    }

    /**
     *  Test testGetAgendaByNameAndContextId()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .getAgendaByNameAndContextId(AgendaName, ContextId) method
     */
    @Test
    public void testGetAgendaByNameAndContextId() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t1 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t1");

        String ruleId = buildTestRuleDefinition(t1.namespaceName, t1.object0).getId();
        String agendaId = createTestAgenda(t1.object0).getId();
        buildTestAgendaItemDefinition(t1.agendaItem_Id, agendaId, ruleId);

        AgendaDefinition agendaDefinition = ruleManagementService.getAgendaByNameAndContextId(t1.agenda_Name,
                t1.contextId);

        assertEquals("Invalid agendaId name found", t1.agenda_Id, agendaDefinition.getId());
        assertEquals("Invalid contextId found",t1.contextId,agendaDefinition.getContextId());
        assertEquals("Invalid typeId found", t1.typeId,agendaDefinition.getTypeId());
        assertEquals("Incorrect agendaName found",t1.agenda_Name,agendaDefinition.getName());
        assertEquals("Invalid agendaFirstItemId found",t1.agendaItem_0_Id,agendaDefinition.getFirstItemId());

        agendaDefinition = ruleManagementService.getAgendaByNameAndContextId(t1.agenda_Name,"badContext");
        assertNull("Invalid Context, no agendas should have been found",agendaDefinition);

        agendaDefinition = ruleManagementService.getAgendaByNameAndContextId("badName",t1.contextId);
        assertNull("Invalid Name, no agendas should have been found",agendaDefinition);

        try {
            agendaDefinition = ruleManagementService.getAgendaByNameAndContextId(null,t1.contextId);
            fail("Null Name specified for search, should have thrown .RiceIllegalArgumentException: name is blank ");
        } catch (RiceIllegalArgumentException e) {
            // thrown .RiceIllegalArgumentException: name is blank
        }

        try {
            agendaDefinition = ruleManagementService.getAgendaByNameAndContextId(t1.agenda_Name,null);
            fail("Null Context specified for search, should have thrown .RiceIllegalArgumentException: contextId is blank");
        } catch (RiceIllegalArgumentException e) {
            // thrown .RiceIllegalArgumentException: contextId is blank
        }
    }

    /**
     *  Test testFindCreateAgenda()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .findCreateAgenda(AgendaDefinition) method
     */
    @Test
    public void testFindCreateAgenda() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t2 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t2");

        // create a context
        ContextDefinition.Builder contextDefinitionBuilder = ContextDefinition.Builder.create(t2.namespaceName,
                t2.contextName);
        contextDefinitionBuilder.setId(t2.contextId);
        ContextDefinition contextDefinition = contextDefinitionBuilder.build();
        contextDefinition = ruleManagementService.findCreateContext(contextDefinition);

        assertNull("Agenda should not have already existed", ruleManagementService.getAgenda(t2.agendaItem_0_Id));

        // create an agenda
        AgendaDefinition.Builder agendaBuilder = AgendaDefinition.Builder.create(
                t2.agenda_Id, t2.agenda_Name, null, t2.contextId);
        AgendaDefinition agenda = agendaBuilder.build();
        agenda = ruleManagementService.findCreateAgenda(agenda);

        assertNotNull("Agenda should have been created", ruleManagementService.getAgenda(t2.agenda_Id));

        // update an agenda using findCreateAgenda - invalid attempt
        // ( cannot change name or context as these are used to uniquely identify agenda for findCreateAgenda
        agendaBuilder = AgendaDefinition.Builder.create(t2.agenda_Id, "ChangedName", null, t2.contextId);
        agenda = agendaBuilder.build();
        try {
            agenda = ruleManagementService.findCreateAgenda(agenda);
            fail( "should have failed with OjbOperationException: OJB operation failed");
        } catch (OjbOperationException e) {
            // thrown OjbOperationException: OJB operation failed ...OptimisticLockException: Object has been modified by someone else
        }

        // create a new agendaItem to update the agenda with
        AgendaItemDefinition agendaItem = buildTestAgendaItemDefinition("AINew" + t2.action0, t2.agenda_Id, null);

        //  findCreateAgenda with changed agendaFirstItemId
        agendaBuilder = AgendaDefinition.Builder.create(t2.agenda_Id, t2.agenda_Name, t2.typeId, t2.contextId);
        agendaBuilder.setFirstItemId(agendaItem.getId());
        agenda = ruleManagementService.findCreateAgenda(agendaBuilder.build());

        assertEquals("Agenda should have been changed by findCreateAgenda","AINew" + t2.action0,
                ruleManagementService.getAgenda(t2.agenda_Id).getFirstItemId());
    }

    /**
     *  Test testGetAgenda()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .getAgenda(AgendaId) method
     */
    @Test
    public void testGetAgenda() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t3 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t3");
        AgendaDefinition agendaDefinition = createTestAgenda(t3.object0);

        assertEquals("Agenda not found", t3.agenda_Name, ruleManagementService.getAgenda(t3.agenda_Id).getName());

        // call getAgenda method with null
        try {
            ruleManagementService.getAgenda(null);
            fail("Should have thrown RiceIllegalArgumentException: agenda id is null or blank");
        } catch (RiceIllegalArgumentException e) {
            // throws RiceIllegalArgumentException: agenda id is null or blank
        }

        // call getAgenda method with blank
        try {
            ruleManagementService.getAgenda("  ");
            fail("Should have thrown RiceIllegalArgumentException: agenda id is null or blank");
        } catch (RiceIllegalArgumentException e) {
            // throws RiceIllegalArgumentException: agenda id is null or blank
        }

        // call get Agenda with bad AgendaId
        assertNull("Agenda should not have been found", ruleManagementService.getAgenda("badAgendaId"));
    }

    /**
     *  Test testGetAgendasByContext()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .getAgendasByContext(ContextId) method
     */
    @Test
    public void testGetAgendasByContext() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t4 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t4");
        createTestAgenda(t4.object0);

        // get a second set of object names for the creation of second agenda
        RuleManagementBaseTestObjectNames t5 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t5");
        createTestAgenda(t5.object0);

        // set second agendaContextId to same as first
        AgendaDefinition.Builder agendaBuilder = AgendaDefinition.Builder.create(ruleManagementService.getAgenda(t5.agenda_Id));
        agendaBuilder.setContextId(t4.contextId);
        ruleManagementService.updateAgenda(agendaBuilder.build());

        List<AgendaDefinition> agendas = ruleManagementService.getAgendasByContext(t4.contextId);
        assertEquals("Incorrect number of Agendas returned",2,agendas.size());

        List<String> agendaIds = Arrays.asList(t4.agenda_Id, t5.agenda_Id);

        // verify expected agendas returned & count the returned agendas
        int agendasFound = 0;
        for( AgendaDefinition agenda : agendas ) {
            if(agendaIds.contains(agenda.getId())) {
                agendasFound++;
            }
        }
        assertEquals("Incorrect results of getAgendasByContext",2,agendasFound);

        // call getAgendasByContext method with null
        try {
            ruleManagementService.getAgendasByContext(null);
            fail("Should have thrown RiceIllegalArgumentException: context ID is null or blank");
        } catch (RiceIllegalArgumentException e) {
            // throws RiceIllegalArgumentException: context ID is null or blank
        }

        // call getAgendasByContext method with blank ContextId
        try {
            ruleManagementService.getAgendasByContext("   ");
            fail("Should have thrown RiceIllegalArgumentException: context ID is null or blank");
        } catch (RiceIllegalArgumentException e) {
            // throws RiceIllegalArgumentException: context ID is null or blank
        }

        // call getAgendasByContext with bad ContextId
        assertEquals("No Agenda's should have been found",0,
                ruleManagementService.getAgendasByContext("badContextId").size());

    }

    /**
     *  Test testUpdateAgenda()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .updateAgenda(AgendaDefinition) method
     */
    @Test
    public void testUpdateAgenda() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t6 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t6");
        createTestAgenda(t6.object0, /* createAttributes */ true);
        // create krms type AGENDA
        KrmsTypeDefinition krmsType = createKrmsTypeDefinition(null, t6.namespaceName, "AGENDA", null);

        AgendaDefinition.Builder agendaBuilder = AgendaDefinition.Builder.create(ruleManagementService.getAgenda(t6.agenda_Id));

        // change attribute value
        String attrKey = agendaBuilder.getAttributes().entrySet().iterator().next().getKey();
        String newAttrValue = "newAttrVal" + t6.object0;
        agendaBuilder.setAttributes(Collections.singletonMap(attrKey, newAttrValue));

        agendaBuilder.setActive(false);
        ruleManagementService.updateAgenda(agendaBuilder.build());

        assertEquals("Updated agenda attribute not found",newAttrValue, ruleManagementService.getAgenda(t6.agenda_Id).getAttributes().get(attrKey));
        assertEquals("Agenda should have been changed to inActive",false,
                ruleManagementService.getAgenda(t6.agenda_Id).isActive());
    }

    /**
     *  Test testDeleteAgenda()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .deleteAgenda("AgendaId") method
     */
    @Test
    public void testDeleteAgenda() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t7 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t7");

        assertNull("Agenda should not yet exist", ruleManagementService.getAgenda(t7.agenda_Id));

        AgendaDefinition agendaDefinition = createTestAgenda(t7.object0, /* createAttributes */ true);
        assertNotNull("Agenda should exist", ruleManagementService.getAgenda(t7.agenda_Id));

        ruleManagementService.deleteAgenda(t7.agenda_Id);
        assertNull("Agenda should not exist after deletion", ruleManagementService.getAgenda(t7.agenda_Id));

        try {
            ruleManagementService.deleteAgenda("junkAgenda");
            fail("Should have failed with IllegalStateException: the Agenda to delete does not exists");
        } catch (IllegalStateException e) {
            // throws  IllegalStateException: the Agenda to delete does not exists: junkAgenda
        }

        try {
            ruleManagementService.deleteAgenda(null);
            fail("Should have failed with .RiceIllegalArgumentException: agendaId is null");
        } catch (RiceIllegalArgumentException e) {
            // throws .RiceIllegalArgumentException: agendaId is null
        }
    }

    /**
     *  Test testGetAgendasByType()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .getAgendasByType("NamespaceType") method
     */
    @Test
    public void testGetAgendasByType() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t8 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t8");
        createTestAgenda(t8.object0);

        // get a second set of object names for the creation of second agenda
        RuleManagementBaseTestObjectNames t9 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t9");
        createTestAgenda(t9.object0);

        // create krms type AGENDA5008
        KrmsTypeDefinition krmsType = createKrmsTypeDefinition(null, t8.namespaceName, t8.namespaceType, null);

        // set agendaType for both agendas
        AgendaDefinition.Builder agendaBuilder = AgendaDefinition.Builder.create(ruleManagementService.getAgenda(t8.agenda_Id));
        agendaBuilder.setTypeId(krmsType.getId());
        ruleManagementService.updateAgenda(agendaBuilder.build());
        agendaBuilder = AgendaDefinition.Builder.create(ruleManagementService.getAgenda(t9.agenda_Id));
        agendaBuilder.setTypeId(krmsType.getId());
        ruleManagementService.updateAgenda(agendaBuilder.build());

        List<AgendaDefinition> agendas = ruleManagementService.getAgendasByType(krmsType.getId());
        assertEquals("Incorrect number of Agendas returned",2,agendas.size());

        List<String> agendaIds = Arrays.asList(t8.agenda_Id, t9.agenda_Id);

        // verify expected agendas returned & count the returned agendas
        int agendasFound = 0;
        for( AgendaDefinition agenda : agendas ) {
            if(agendaIds.contains(agenda.getId())) {
                agendasFound++;
            }
        }

        assertEquals("Incorrect results of getAgendasByContext",2,agendasFound);
    }

    /**
     *  Test testGetAgendasByTypeAndContext()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .getAgendasByTypeAndContext("NamespaceType", "ContextId") method
     */
    @Test
    public void testGetAgendasByTypeAndContext() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t10 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t10");
        createTestAgenda(t10.object0);

        // get a second set of object names for the creation of second agenda
        RuleManagementBaseTestObjectNames t11 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t11");
        createTestAgenda(t11.object0);

        // create krms type AGENDA5010
        KrmsTypeDefinition krmsType = createKrmsTypeDefinition(null, t10.namespaceName, t10.namespaceType, null);

        // set agendaType for both agendas and contextId of 5011 to match 5010
        AgendaDefinition.Builder agendaBuilder = AgendaDefinition.Builder.create(ruleManagementService.getAgenda(t10.agenda_Id));
        agendaBuilder.setTypeId(krmsType.getId());
        ruleManagementService.updateAgenda(agendaBuilder.build());
        agendaBuilder = AgendaDefinition.Builder.create(ruleManagementService.getAgenda(t11.agenda_Id));
        agendaBuilder.setTypeId(krmsType.getId());
        agendaBuilder.setContextId(ruleManagementService.getAgenda(t10.agenda_Id).getContextId());
        ruleManagementService.updateAgenda(agendaBuilder.build());

        List<AgendaDefinition> agendas = ruleManagementService.getAgendasByTypeAndContext(krmsType.getId(),
                t10.contextId);
        assertEquals("Incorrect number of Agendas returned",2,agendas.size());

        List<String> agendaIds = Arrays.asList(t10.agenda_Id, t11.agenda_Id);

        // verify expected agendas returned & count the returned agendas
        int agendasFound = 0;
        for( AgendaDefinition agenda : agendas ) {
            if(agendaIds.contains(agenda.getId())) {
                agendasFound++;
            }
        }

        assertEquals("Incorrect results of getAgendasByTypeAndContext",2,agendasFound);
    }

    /**
     *  Test testFindAgendaIds()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .findAgendaIds(QueryByCriteria) method
     */
    @Test
    public void testFindAgendaIds() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t12 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t12");
        createTestAgenda(t12.object0);

        // get a second set of object names for the creation of second agenda
        RuleManagementBaseTestObjectNames t13 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t13");
        createTestAgenda(t13.object0);

        // get a third set of object names for the creation of thrid agenda
        RuleManagementBaseTestObjectNames t14 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t14");
        createTestAgenda(t14.object0);

        // create krms type t12.AGENDA
        KrmsTypeDefinition krmsType = createKrmsTypeDefinition(null, t12.namespaceName, t12.namespaceType, null);

        // set agendaType for all agendas to match / and contextId of 5013 to match 5012 but not match 5014
        AgendaDefinition.Builder agendaBuilder = AgendaDefinition.Builder.create(ruleManagementService.getAgenda(
                t12.agenda_Id));
        agendaBuilder.setTypeId(krmsType.getId());
        ruleManagementService.updateAgenda(agendaBuilder.build());
        agendaBuilder = AgendaDefinition.Builder.create(ruleManagementService.getAgenda(t13.agenda_Id));
        agendaBuilder.setTypeId(krmsType.getId());
        agendaBuilder.setContextId(ruleManagementService.getAgenda(t12.agenda_Id).getContextId());
        ruleManagementService.updateAgenda(agendaBuilder.build());
        agendaBuilder = AgendaDefinition.Builder.create(ruleManagementService.getAgenda(t14.agenda_Id));
        agendaBuilder.setTypeId(krmsType.getId());
        ruleManagementService.updateAgenda(agendaBuilder.build());
        // create list of agendas with same ContextId
        List<String> agendaNames =  new ArrayList<String>();
        agendaNames.add(t12.agenda_Name);
        agendaNames.add(t13.agenda_Name);
        agendaNames.add(t14.agenda_Name);

        QueryByCriteria.Builder builder = QueryByCriteria.Builder.create();
        // find active agendas with same agendaType
        builder.setPredicates(equal("active","Y"),equal("typeId", krmsType.getId()));
        List<String> agendaIds = ruleManagementService.findAgendaIds(builder.build());
        assertEquals("Wrong number of Agendas returned",3,agendaIds.size());

        // find agendas with the same Context
        builder.setPredicates(equal("contextId", t12.contextId));
        agendaIds = ruleManagementService.findAgendaIds(builder.build());
        assertEquals("Wrong number of Agendas returned",2,agendaIds.size());

        // find agendas in list of agendaNames
        builder.setPredicates(in("name", agendaNames.toArray(new String[]{})));
        agendaIds = ruleManagementService.findAgendaIds(builder.build());
        assertEquals("Wrong number of Agendas returned",3,agendaIds.size());

        // verify expected agendas returned & count the returned agendas
        int agendasFound = 0;
        for( String agendaId : agendaIds ) {
            if(agendaNames.contains(ruleManagementService.getAgenda(agendaId).getName())) {
                agendasFound++;
            }
        }

        assertEquals("Incorrect results of findAgendaIds",3,agendasFound);
    }

    /**
     * Tests whether the {@code AgendaDefinition} cache is being evicted properly by checking the status the dependent
     * objects before and after creating an {@code AgendaDefinition} (and consequently emptying the cache).
     *
     * <p>
     * The following object caches are affected:
     * {@code AgendaTreeDefinition}, {@code AgendaDefinition}, {@code AgendaItemDefinition}, {@code ContextDefinition}
     * </p>
     */
    @Test
    public void testAgendaCacheEvict() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t15 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t15");

        verifyEmptyAgenda(t15);

        RuleDefinition ruleDefinition = buildTestRuleDefinition(t15.namespaceName, t15.object0);
        AgendaDefinition agendaDefinition = createTestAgenda(t15.object0);
        buildTestAgendaItemDefinition(t15.agendaItem_Id, agendaDefinition.getId(), ruleDefinition.getId());

        verifyFullAgenda(t15);
    }

    /**
     * Builds a prototype AgendaDefinition for use in testing the isSame method in RuleManagementServiceImpl.  Note
     * that the values that are be set for FK properties have no actual relationships to other persisted entities.
     * @return the prototype AgendaDefinition
     */
    private AgendaDefinition getPrototypeAgendaDefinition() {
        // Create a prototype AgendaDefinition that we'll tweak to test equality
        AgendaDefinition.Builder protoBuilder = AgendaDefinition.Builder.create("123", "name", "typeId", "contextId");
        protoBuilder.setActive(true);
        protoBuilder.setAttributes(Collections.singletonMap("attrKey", "attrValue"));
        protoBuilder.setFirstItemId("234");
        protoBuilder.setVersionNumber(1l);
        AgendaDefinition prototype = protoBuilder.build();

        return prototype;
    }

    /**
     * Call the private method
     * {@link org.kuali.rice.krms.impl.repository.RuleManagementServiceImpl#isSame(org.kuali.rice.krms.api.repository.agenda.AgendaDefinition, org.kuali.rice.krms.api.repository.agenda.AgendaDefinition)}
     * for white box testing purposes
     *
     * @param ad1 first agenda definition to compare
     * @param ad2 second agenda definition to compare
     * @return whether these agenda definitions are considered the same
     * @throws Exception if there are reflection-related issues with accessing and calling the method
     */
    private boolean callIsSame(AgendaDefinition ad1, AgendaDefinition ad2) throws Exception {
        RuleManagementServiceImpl ruleManagementServiceImpl = new RuleManagementServiceImpl();

        Method equalityMethod = ruleManagementServiceImpl.getClass().getDeclaredMethod("isSame", AgendaDefinition.class,
                AgendaDefinition.class);
        equalityMethod.setAccessible(true);

        return (Boolean) equalityMethod.invoke(ruleManagementServiceImpl, ad1, ad2);
    }

    /**
     * White box test of RuleManagementServiceImpl agenda comparison logic, verifying that an identical copy is
     * considered equal
     */
    @Test
    public void testAgendaDefinitionComparisonLogic_nonNullEquality() throws Exception {
        RuleManagementServiceImpl ruleManagementServiceImpl = new RuleManagementServiceImpl();

        AgendaDefinition prototype = getPrototypeAgendaDefinition();

        // need to test boolean, Map, and String equality to cover the cases in RuleManagementServiceImpl.isSame

        AgendaDefinition identicalCopy = AgendaDefinition.Builder.create(prototype).build();

        assertTrue("isSame should return true for identical copy", callIsSame(prototype, identicalCopy));
        assertTrue("isSame should return true for identical copy", callIsSame(identicalCopy, prototype));
    }

    /**
     * White box test of RuleManagementServiceImpl agenda comparison logic, verifying that a copy with a mutated boolean
     * field is not considered equal
     */
    @Test
    public void testAgendaDefinitionComparisonLogic_booleanEquality() throws Exception {
        AgendaDefinition prototype = getPrototypeAgendaDefinition();

        // test in both directions that the different boolean field results in a false equality result
        AgendaDefinition.Builder protoBuilder = AgendaDefinition.Builder.create(prototype);
        protoBuilder.setActive(false);
        AgendaDefinition agendaDefinitionInactive = protoBuilder.build();

        assertFalse("isSame should return false for copy with mutated active flag",
                callIsSame(prototype, agendaDefinitionInactive));
        assertFalse("isSame should return false for copy with mutated active flag",
                callIsSame(agendaDefinitionInactive, prototype));
    }

    /**
     * White box test of RuleManagementServiceImpl agenda comparison logic, verifying that a copy with a mutated String
     * field is not considered equal
     */
    @Test
    public void testAgendaDefinitionComparisonLogic_stringEquality() throws Exception {
        AgendaDefinition prototype = getPrototypeAgendaDefinition();

        AgendaDefinition.Builder protoBuilder = AgendaDefinition.Builder.create(prototype);
        protoBuilder.setTypeId(null);
        AgendaDefinition agendaDefinitionNullType = protoBuilder.build();

        // test in both directions that the nulled out field results in a false equality result
        assertFalse("isSame should return false for copy with nulled out typeId",
                callIsSame(prototype, agendaDefinitionNullType));
        assertFalse("isSame should return false for copy with nulled out typeId",
                callIsSame(agendaDefinitionNullType, prototype));

        // test that two nulled out string fields are considered equal
        assertTrue("isSame should return true for two copies with nulled out typeIds",
                callIsSame(agendaDefinitionNullType, AgendaDefinition.Builder.create(agendaDefinitionNullType).build()));

        // test that unequal strings are recognized and we get a false equality result
        protoBuilder = AgendaDefinition.Builder.create(prototype);
        protoBuilder.setTypeId("dIepyt");
        AgendaDefinition agendaDefinitionDiffType = protoBuilder.build();

        assertFalse("isSame should return false for copy with mutated typeId",
                callIsSame(prototype, agendaDefinitionDiffType));
        assertFalse("isSame should return false for copy with mutated typeId",
                callIsSame(agendaDefinitionDiffType, prototype));
    }

    /**
     * White box test of RuleManagementServiceImpl agenda comparison logic, verifying that a copy with a mutated Map
     * field is not considered equal
     */
    @Test
    public void testAgendaDefinitionComparisonLogic_mapEquality() throws Exception {
        AgendaDefinition prototype = getPrototypeAgendaDefinition();

        // test that a null map is detected as non-equal to the prototype map
        AgendaDefinition.Builder protoBuilder = AgendaDefinition.Builder.create(prototype);
        protoBuilder.setAttributes(null);
        AgendaDefinition agendaDefinitionNullAttrs = protoBuilder.build();

        assertFalse("isSame should return false for copy with nulled attributes map",
                callIsSame(prototype, agendaDefinitionNullAttrs));
        assertFalse("isSame should return false for copy with nulled attributes map",
                callIsSame(agendaDefinitionNullAttrs, prototype));

        // test that two null maps are considered equal
        assertTrue("isSame should return true for two instances with nulled attributes map",
                callIsSame(agendaDefinitionNullAttrs, AgendaDefinition.Builder.create(agendaDefinitionNullAttrs).build()));

        // test that unequal maps result in a false equality result
        protoBuilder = AgendaDefinition.Builder.create(prototype);
        protoBuilder.setAttributes(Collections.<String,String>emptyMap());
        AgendaDefinition agendaDefinitionEmptyAttrs = protoBuilder.build();

        assertFalse("isSame should return false for copy with empty attributes map",
                callIsSame(prototype, agendaDefinitionEmptyAttrs));
        assertFalse("isSame should return false for copy with empty attributes map",
                callIsSame(agendaDefinitionEmptyAttrs, prototype));

        // test that different types of map but with equal key value pairs are considered equal
        protoBuilder = AgendaDefinition.Builder.create(prototype);
        protoBuilder.setAttributes(new TreeMap<String,String>());
        AgendaDefinition agendaDefinitionEmptyTreeMapAttrs = protoBuilder.build();

        assertTrue("isSame should return true for two empty attributes maps, even of different classes", callIsSame(
                agendaDefinitionEmptyAttrs, agendaDefinitionEmptyTreeMapAttrs));
        assertTrue("isSame should return true for two empty attributes maps, even of different classes", callIsSame(
                agendaDefinitionEmptyTreeMapAttrs, agendaDefinitionEmptyAttrs));
    }

    private void verifyEmptyAgenda(RuleManagementBaseTestObjectNames t) {
        AgendaDefinition agenda = ruleManagementService.getAgenda(t.agenda_Id);
        assertNull("Agenda is not null", agenda);

        AgendaItemDefinition agendaItem = ruleManagementService.getAgendaItem(t.agendaItem_Id);
        assertFalse("Agenda in AgendaItem found", agendaItem != null);

        boolean foundAgenda = false;
        ContextDefinition context = ruleManagementService.getContext(t.contextId);
        if (context != null) {
            for (AgendaDefinition contextAgenda : context.getAgendas()) {
                if (StringUtils.equals(t.agenda_Id, contextAgenda.getId())) {
                    foundAgenda = true;
                    break;
                }
            }
        }
        assertFalse("Agenda in Context found", foundAgenda);

        AgendaTreeDefinition agendaTree = ruleManagementService.getAgendaTree(t.agenda_Id);
        assertFalse("Agenda in AgendaTree found", agendaTree != null);
    }

    private void verifyFullAgenda(RuleManagementBaseTestObjectNames t) {
        AgendaDefinition agenda = ruleManagementService.getAgenda(t.agenda_Id);
        assertNotNull("Agenda is null", agenda);

        AgendaItemDefinition agendaItem = ruleManagementService.getAgendaItem(t.agendaItem_Id);
        assertTrue("Agenda in AgendaItem not found", agendaItem != null);
        assertTrue("Agenda in AgendaItem not found", StringUtils.equals(t.agenda_Id, agendaItem.getAgendaId()));

        boolean foundAgenda = false;
        ContextDefinition context = ruleManagementService.getContext(t.contextId);
        if (context != null) {
            for (AgendaDefinition contextAgenda : context.getAgendas()) {
                if (StringUtils.equals(t.agenda_Id, contextAgenda.getId())) {
                    foundAgenda = true;
                    break;
                }
            }
        }
        assertTrue("Agenda in Context not found", foundAgenda);

        foundAgenda = false;
        AgendaTreeDefinition agendaTree = ruleManagementService.getAgendaTree(t.agenda_Id);
        if (agendaTree != null) {
            for (AgendaTreeEntryDefinitionContract agendaTreeEntry : agendaTree.getEntries()) {
                if (StringUtils.equals(t.agendaItem_Id, agendaTreeEntry.getAgendaItemId())) {
                    foundAgenda = true;
                    break;
                }
            }
        }
        assertTrue("Agenda in AgendaTree not found", foundAgenda);
    }
}
