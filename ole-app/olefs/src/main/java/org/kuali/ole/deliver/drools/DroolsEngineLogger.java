package org.kuali.ole.deliver.drools;

import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.runtime.KieSession;
import org.kie.internal.runtime.StatefulKnowledgeSession;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by pvsubrah on 3/24/15.
 */
public class DroolsEngineLogger {
    private static final Logger LOG = Logger.getLogger(DroolsEngineLogger.class);
    private static DroolsEngineLogger droolsEngineLogger;


    private DroolsEngineLogger() {

    }

    public static DroolsEngineLogger getInstance(){
        if(null == droolsEngineLogger){
            droolsEngineLogger = new DroolsEngineLogger();
        }
        return droolsEngineLogger;
    }

    public void logRulesFired(String info){

        StatefulKnowledgeSession session = DroolsEngine.getInstance().getSession();

        Collection<AgendaEventListener> agendaEventListeners = session
                .getAgendaEventListeners();
        CustomAgendaEventListener agendaEventListener = (CustomAgendaEventListener) (agendaEventListeners.toArray
                ()[0]);


        List<String> rulesFiredList = agendaEventListener.getRulesFired();
        if (rulesFiredList.size() == 0) {
            System.out.println("No rules fired");
        } else {
            System.out.println("Rules Fired for " + info);
        }
        for (Iterator<String> stringIterator = rulesFiredList.iterator(); stringIterator.hasNext(); ) {
            String rulesFired = stringIterator.next();
            Log.info(rulesFired);
            System.out.println(rulesFired);
        }
    }

    public void logRulesFired(String info, KieSession session){

        Collection<AgendaEventListener> agendaEventListeners = session
                .getAgendaEventListeners();
        CustomAgendaEventListener agendaEventListener = (CustomAgendaEventListener) (agendaEventListeners.toArray
                ()[0]);


        List<String> rulesFiredList = agendaEventListener.getRulesFired();
        if (rulesFiredList.size() == 0) {
            System.out.println("No rules fired");
        } else {
            System.out.println("Rules Fired for " + info);
        }
        for (Iterator<String> stringIterator = rulesFiredList.iterator(); stringIterator.hasNext(); ) {
            String rulesFired = stringIterator.next();
            Log.info(rulesFired);
            System.out.println(rulesFired);
        }
    }
}
