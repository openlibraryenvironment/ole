package org.kuali.ole.deliver.controller.drools;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.Agenda;
import org.kie.api.runtime.rule.AgendaGroup;
import org.kuali.ole.deliver.drools.CustomAgendaFilter;
import org.kuali.ole.deliver.drools.DroolsKieEngine;

import java.util.Iterator;
import java.util.List;

/**
 * Created by hemalathas on 12/24/15.
 */
public class RuleExecutor {

    public void fireRules(List<Object> facts, String[] expectedRules, String agendaGroup) {
        KieSession session = DroolsKieEngine.getInstance().getSession();
        for (Iterator<Object> iterator = facts.iterator(); iterator.hasNext(); ) {
            Object fact = iterator.next();
            session.insert(fact);
        }

        if (null != expectedRules && expectedRules.length > 0) {
            session.fireAllRules(new CustomAgendaFilter(expectedRules));
        } else {
            Agenda agenda = session.getAgenda();
            AgendaGroup group = agenda.getAgendaGroup(agendaGroup);
            group.setFocus();
            session.fireAllRules();
        }
        session.dispose();
    }
}
