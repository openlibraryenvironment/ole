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
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.krms.api.repository.agenda.AgendaDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaTreeDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaTreeEntryDefinitionContract;
import org.kuali.rice.krms.api.repository.agenda.AgendaTreeRuleEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;


/**
 *   RuleManagementAgendaTreeDefinitionTest is to test the methods of ruleManagementServiceImpl relating to AgendaTreeDefinitions
 *
 *   Each test focuses on one of the methods.
 */
public class RuleManagementAgendaTreeDefinitionTest  extends RuleManagementBaseTest {
    @Override
    @Before
    public void setClassDiscriminator() {
        // set a unique discriminator for test objects of this class
        CLASS_DISCRIMINATOR = "RMATDT";
    }

    /**
     *  Test testGetAgendaTree()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .getAgendaTree(AgendaDefinition) method
     */
    @Test
    public void testGetAgendaTree() {
        /* *************************
            This is the tree which should be generated from a demo complex Agenda
           *************************
                AgendaTreeDefinition agendaTreeDefinition
                    agendaId = t0.agenda_Id
                    entries = ArrayList size = 2
                    [0] = AgendaTreeRuleEntry
                        agendaItemId = t0.agendaItem_0_Id
                        ruleId = t0.rule_0_Id
                        ifTrue = AgendaTreeDefinition
                            agendaId = t0.agenda_Id
                            entries = ArrayList size = 2
                                [0] = AgendaTreeRuleEntry
                                    agendaItemId = t0.agendaItem_1_Id
                                    ruleId = t0.rule_1_Id
                                    ifTrue = null
                                    ifFalse = null
                                [1] = AgendaTreeRuleEntry
                                    agendaItemId = t0.agendaItem_5_Id
                                    ruleId = t0.rule_5_Id
                                    ifTrue = null
                                    ifFalse = null
                        ifFalse = AgendaTreeDefinition
                            agendaId = t0.agenda_Id
                            entries = ArrayList size = 2
                                [0] = AgendaTreeRuleEntry
                                    agendaItemId = t0.agendaItem_2_Id
                                    ruleId = t0.rule_2_Id
                                    ifTrue = null
                                    ifFalse = AgendaTreeDefinition
                                        agendaId = t0.agenda_Id
                                        entries = ArrayList  size = 1
                                            [0] = AgendaTreeRuleEntry
                                                agendaItemId = t0.agendaItem_4_Id
                                                ruleId = t0.rule_4_Id
                                                ifTrue = null
                                                ifFalse = null
                                [1] = AgendaTreeRuleEntry
                                    agendaItemId = t0.agendaItem_6_Id
                                    ruleId = t0.rule_6_Id
                                    ifTrue = null
                                    ifFalse = null
                    [1] = AgendaTreeRuleEntry
                        agendaItemId = t0.agendaItem_3_Id
                        ruleId = t0.rule_3_Id
                        ifTrue = null
                        ifFalse = null
         */

        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t0 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t0");
        // create the Agenda to test with
        AgendaDefinition.Builder agendaBuilder = buildComplexAgenda(t0);

        // Get the AgendaTreeDefinition and drill down a branch to one of the lowest levels for information
        AgendaTreeDefinition agendaTreeDefinition = ruleManagementService.getAgendaTree(agendaBuilder.getId());
        assertNotNull("Should have returned a AgendaTreeDefinition", agendaTreeDefinition);

        List<AgendaTreeEntryDefinitionContract> agendaTreeRuleEntrys = agendaTreeDefinition.getEntries();
        assertEquals("First level of tree should of had 2 entries",2,agendaTreeRuleEntrys.size());

        // drill down a branch to test returned AgendaTreeDefinition
        AgendaTreeRuleEntry firstLevelFirstEntry = (AgendaTreeRuleEntry) agendaTreeRuleEntrys.get(0);
        AgendaTreeRuleEntry firstLevelSecondEntry = (AgendaTreeRuleEntry) agendaTreeRuleEntrys.get(1);
        AgendaTreeDefinition ifTrueEntry = firstLevelFirstEntry.getIfTrue();
        agendaTreeRuleEntrys = ifTrueEntry.getEntries();
        assertEquals("IfTrue level of first entry of tree should of had 2 entries",2,agendaTreeRuleEntrys.size());
        AgendaTreeRuleEntry agendaTreeRuleEntry = (AgendaTreeRuleEntry) agendaTreeRuleEntrys.get(1);
        assertEquals("Incorrect AgendaItemId found", t0.agendaItem_5_Id, agendaTreeRuleEntry.getAgendaItemId());

        // drill down to another location
        AgendaTreeDefinition ifFalseEntry = firstLevelFirstEntry.getIfFalse();
        agendaTreeRuleEntrys = ifFalseEntry.getEntries();
        assertEquals("IfFalse level of first entry of tree should of had 2 entries",2,agendaTreeRuleEntrys.size());
        agendaTreeRuleEntry = (AgendaTreeRuleEntry) agendaTreeRuleEntrys.get(0);
        ifFalseEntry = agendaTreeRuleEntry.getIfFalse();
        agendaTreeRuleEntrys = ifFalseEntry.getEntries();
        agendaTreeRuleEntry = (AgendaTreeRuleEntry) agendaTreeRuleEntrys.get(0);
        assertEquals("Incorrect AgendaItemId found", t0.agendaItem_4_Id, agendaTreeRuleEntry.getAgendaItemId());

        // Test call with blank parameter
        try {
            agendaTreeDefinition = ruleManagementService.getAgendaTree(" ");
            fail("Should have thrown RiceIllegalArgumentException: agenda id is null or blank");
        } catch (RiceIllegalArgumentException e) {
            // throws RiceIllegalArgumentException: agenda id is null or blank
        }

        // Test call with null parameter
        try {
            agendaTreeDefinition = ruleManagementService.getAgendaTree(null);
            fail("Should have thrown RiceIllegalArgumentException: agenda id is null or blank");
        } catch (RiceIllegalArgumentException e) {
            // throws RiceIllegalArgumentException: agenda id is null or blank
        }

        assertNull("Should have return null", ruleManagementService.getAgendaTree("badValueId"));
    }

    /**
     *  Test testGetAgendaTrees()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .getAgendaTrees(List<String> agendaIds</String>) method
     */
    @Test
    public void testGetAgendaTrees() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t1 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t1");

        // create the Agenda to test with
        AgendaDefinition.Builder agendaBuilder = buildComplexAgenda(t1);

        // get a second set of object names and create a second agenda to test with
        RuleManagementBaseTestObjectNames t2 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t2");
        buildComplexAgenda(t2);

        // create  a list of the agenda ids created
        List<String> agendaIds = new ArrayList<String>();
        agendaIds.add(t1.agenda_Id);
        agendaIds.add(t2.agenda_Id);


        List<AgendaTreeDefinition> agendaTreeDefinitions = ruleManagementService.getAgendaTrees( agendaIds);
        assertEquals("Two agendaTree definitions should have been return",2,agendaTreeDefinitions.size());
        for (AgendaTreeDefinition agendaTreeDefinition: agendaTreeDefinitions ) {
            if (!agendaIds.contains(agendaTreeDefinition.getAgendaId())) {
                fail("Invalid AgendaTreeDefinition returned");
            }
        }

        assertEquals("No AgendaTreeDefinitions should have been returned", 0, ruleManagementService.getAgendaTrees(
                null).size());

        agendaIds = Arrays.asList("badValueId");
        assertEquals("No AgendaTreeDefinitions should have been returned",0,
                ruleManagementService.getAgendaTrees( agendaIds).size());
    }
}
