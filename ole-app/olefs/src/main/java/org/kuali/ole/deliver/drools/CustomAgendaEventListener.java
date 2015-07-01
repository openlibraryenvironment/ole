package org.kuali.ole.deliver.drools;

import org.kie.api.event.rule.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by pvsubrah on 3/18/15.
 */
public class CustomAgendaEventListener implements AgendaEventListener {
    private List<String> rulesFired = new LinkedList<String>();

    @Override
    public void matchCreated(MatchCreatedEvent matchCreatedEvent) {

    }

    @Override
    public void matchCancelled(MatchCancelledEvent matchCancelledEvent) {

    }

    @Override
    public void beforeMatchFired(BeforeMatchFiredEvent beforeMatchFiredEvent) {

    }

    @Override
    public void afterMatchFired(AfterMatchFiredEvent afterMatchFiredEvent) {
        rulesFired.add(afterMatchFiredEvent.getMatch().getRule().getName());
    }

    @Override
    public void agendaGroupPopped(AgendaGroupPoppedEvent agendaGroupPoppedEvent) {

    }

    @Override
    public void agendaGroupPushed(AgendaGroupPushedEvent agendaGroupPushedEvent) {

    }

    @Override
    public void beforeRuleFlowGroupActivated(RuleFlowGroupActivatedEvent ruleFlowGroupActivatedEvent) {

    }

    @Override
    public void afterRuleFlowGroupActivated(RuleFlowGroupActivatedEvent ruleFlowGroupActivatedEvent) {

    }

    @Override
    public void beforeRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent ruleFlowGroupDeactivatedEvent) {

    }

    @Override
    public void afterRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent ruleFlowGroupDeactivatedEvent) {

    }

    public List<String> getRulesFired() {
        return this.rulesFired;
    }

}
