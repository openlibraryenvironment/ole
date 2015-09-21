package org.kuali.ole.deliver.drools;

import org.drools.core.common.InternalAgendaGroup;
import org.drools.core.reteoo.RuleTerminalNodeLeftTuple;
import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.Match;

/**
 * Created by pvsubrah on 7/23/15.
 */
public class CustomAgendaFilterForAgendaGroup implements AgendaFilter {

    private String agendaGroupToMatch;

    public CustomAgendaFilterForAgendaGroup(String agendaGroup) {
        this.agendaGroupToMatch = agendaGroup;
    }


    @Override
    public boolean accept(Match match) {
        InternalAgendaGroup agendaGroup = ((RuleTerminalNodeLeftTuple) match).getAgendaGroup();
        return this.agendaGroupToMatch.equals(agendaGroup.getName());
    }
}
