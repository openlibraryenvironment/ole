package org.kuali.ole.deliver.drools;

import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.Match;

import java.util.Arrays;
import java.util.List;

/**
 * Created by pvsubrah on 3/18/15.
 */
public class CustomAgendaFilter implements AgendaFilter {

    private List<String> rulesToAllow;

    public CustomAgendaFilter(String[] ruleNames) {
        this.rulesToAllow = Arrays.asList(ruleNames);
    }

    @Override
    public boolean accept(Match match) {
        if (this.rulesToAllow.contains(match.getRule().getName())) {
            return true;
        }
        return false;
    }
}
