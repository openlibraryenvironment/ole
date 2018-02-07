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

import org.junit.Test;

import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.service.SequenceAccessorService;
import org.kuali.rice.krms.api.repository.agenda.AgendaItemDefinition;
import org.kuali.rice.krms.api.repository.context.ContextDefinition;
import org.kuali.rice.krms.api.repository.proposition.PropositionDefinition;
import org.kuali.rice.krms.api.repository.proposition.PropositionType;
import org.kuali.rice.krms.api.repository.rule.RuleDefinition;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class AgendaBoTest {

    public static final String TEST_PREFIX = "AgendaBoTest";
    @Test
    public void testCopy() {
        SequenceAccessorService mapSas = new IncrementalSas();
        AgendaBo.setSequenceAccessorService(mapSas);
        AgendaItemBo.setSequenceAccessorService(mapSas);
        RuleBo.setSequenceAccessorService(mapSas);
        PropositionBo.setSequenceAccessorService(mapSas);

        ContextBo contextBo = ContextBo.from(ContextDefinition.Builder.create(TEST_PREFIX + "ContextName", TEST_PREFIX + "Namespace").build());
        contextBo.setId(TEST_PREFIX + "ContextId");

//        AgendaBo agendaBo = AgendaBo.from(AgendaDefinition.Builder.create(TEST_PREFIX + "AgendaId00", TEST_PREFIX + "AgendaName", null, contextBo.getId()));
        AgendaBo agendaBo = new AgendaBo();
        agendaBo.setId(TEST_PREFIX + "AgendaId00");
        agendaBo.setName(TEST_PREFIX + "AgendaName");
        agendaBo.setContext(contextBo);

        RuleBo ruleBo00 = newTestRuleBo("00");
        RuleBo ruleBo01 = newTestRuleBo("01");
        RuleBo ruleBo02 = newTestRuleBo("02");
        RuleBo ruleBo03 = newTestRuleBo("03");
        RuleBo ruleBo04 = newTestRuleBo("04");
        RuleBo ruleBo05 = newTestRuleBo("05");
        RuleBo ruleBo06 = newTestRuleBo("06");

        List<AgendaItemBo> agendaItemBos = new LinkedList<AgendaItemBo>();
        AgendaItemBo agendaItemBo00 = newTestAgendaItemBo("00", ruleBo00, agendaBo.getId());
        agendaItemBos.add(agendaItemBo00);
        AgendaItemBo agendaItemBo01 = newTestAgendaItemBo("01", ruleBo01, agendaBo.getId());
        agendaItemBos.add(agendaItemBo01);
        AgendaItemBo agendaItemBo02 = newTestAgendaItemBo("02", ruleBo02, agendaBo.getId());
        agendaItemBos.add(agendaItemBo02);
        AgendaItemBo agendaItemBo03 = newTestAgendaItemBo("03", ruleBo03, agendaBo.getId());
        agendaItemBos.add(agendaItemBo03);
        AgendaItemBo agendaItemBo04 = newTestAgendaItemBo("04", ruleBo04, agendaBo.getId());
        agendaItemBos.add(agendaItemBo04);
        AgendaItemBo agendaItemBo05 = newTestAgendaItemBo("05", ruleBo05, agendaBo.getId());
        agendaItemBos.add(agendaItemBo05);
        AgendaItemBo agendaItemBo06 = newTestAgendaItemBo("06", ruleBo06, agendaBo.getId());
        agendaItemBos.add(agendaItemBo06);

        // My Fabulous Agenda
        agendaItemBo00.setWhenTrue(agendaItemBo01);
        agendaItemBo00.setWhenFalse(agendaItemBo04);
        agendaItemBo00.setAlways(agendaItemBo05);

        agendaItemBo01.setAlways(agendaItemBo02);

        agendaItemBo02.setWhenFalse(agendaItemBo06);
        agendaItemBo02.setAlways(agendaItemBo03);

        agendaBo.setFirstItemId(agendaItemBo00.getId());

        agendaBo.setItems(agendaItemBos);

        AgendaBo copiedAgenda = agendaBo.copyAgenda("NewAgendaCopy", "dts123");

        assertFalse(agendaItemBo00.getAgendaId() == null);
        assertTrue("agendaBo.getItems().size() of " + agendaBo.getItems().size() + " is not equal to copiedAgenda.getItems().size() of "+ copiedAgenda.getItems().size(), agendaBo.getItems().size() == copiedAgenda.getItems().size());

        // Assert that the agenda item ids are 0 - 6
        boolean[] ids = {false, false, false, false, false, false, false};
        assertTrue("agendaBo.getItems().size() of " + agendaBo.getItems().size() + " does not match ids.length" + ids.length, agendaBo.getItems().size() == ids.length);
        Iterator<AgendaItemBo> copiedItems = copiedAgenda.getItems().iterator();
        int index = 0;
        while (copiedItems.hasNext()) {
            try {
                index = Integer.parseInt(copiedItems.next().getId());
                ids[index] = true;
            } catch (IndexOutOfBoundsException e) {
                fail("copied agenda item id " +  index + " is higher than " + ids.length);
            }
        }
        for (int i = 0; i < ids.length; i++) {
            assertTrue("agenda item id verification of " + i + " is false", ids[i]);
        }

        // Assert agenda item version numbers are all the same always 0 in unit tests, but IT would have real values
        copiedItems = copiedAgenda.getItems().iterator();
        long lastVersion = -1L;
        long version = -1L;
        while (copiedItems.hasNext()) {
            AgendaItemBo agendaItemBo = copiedItems.next();
            if (agendaItemBo.getVersionNumber() != null) {  // if null throws nullpointer in unboxing
                version = agendaItemBo.getVersionNumber();
                if (lastVersion == -1L) {
                    lastVersion = version;
                } else {
                    assertTrue(lastVersion == version);
                    lastVersion = version;
                }
            }
        }
    }


    private RuleBo newTestRuleBo(String testId) {
        String ruleId = TEST_PREFIX + "RuleId" + testId;
        PropositionDefinition.Builder prop = PropositionDefinition.Builder.create(TEST_PREFIX + "PropositionId" + testId, PropositionType.SIMPLE.getCode(), ruleId, null, null);
        RuleDefinition.Builder builder = RuleDefinition.Builder.create(ruleId, ruleId + "Name", TEST_PREFIX + "Namespace", null, prop.getId());
        builder.setProposition(prop);
        return RuleBo.from(builder.build());
    }

    private AgendaItemDefinition.Builder newTestAgendaItem(String testId, RuleBo ruleBo, String agendaId) {
        return AgendaItemDefinition.Builder.create(TEST_PREFIX + "AgendaItemId" + testId, agendaId);
    }

    private AgendaItemBo newTestAgendaItemBo(String testId, RuleBo ruleBo, String agendaId) {
        return AgendaItemBo.from(newTestAgendaItem(testId, ruleBo, agendaId).build());
    }
}

class IncrementalSas implements SequenceAccessorService {
    static Map<String, Long> sasMap = new HashMap<String, Long>();

    @Override
    public Long getNextAvailableSequenceNumber(String sequenceName, Class<? extends BusinessObject> clazz) {
        return getNextAvailableSequenceNumber(sequenceName + clazz.getCanonicalName());
    }

    @Override
    public synchronized Long getNextAvailableSequenceNumber(String sequenceName) {
        if (sasMap.get(sequenceName) == null) {
            sasMap.put(sequenceName, 0L);
        }
        long l = sasMap.get(sequenceName);
        sasMap.put(sequenceName, l + 1);
        return l;
    }
}
