package org.kuali.ole.select.executor;

import org.kuali.ole.OLEConstants;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.framework.support.krms.RulesEngineExecutor;
import org.kuali.rice.krms.api.engine.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: juliyamonicas
 * Date: 8/20/13
 * Time: 1:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEEResourceRulesEngineExecutor implements RulesEngineExecutor {
    private static final String CONTEXT_NAMESPACE_CODE = OLEConstants.OLE_NAMESPACE;
    private static final String CONTEXT_NAME = "EResource Approval";
    private static final String EVENT_NAME = "EResource Approval";

    @Override
    public EngineResults execute(RouteContext routeContext, Engine engine) {

        ExecutionOptions executionOptions = new ExecutionOptions();
        executionOptions.setFlag(ExecutionFlag.LOG_EXECUTION, true);
        Map<String, String> contextQualifiers = new HashMap<String, String>();
        contextQualifiers.put("namespaceCode", CONTEXT_NAMESPACE_CODE);
        contextQualifiers.put("name", "OLE-CONTEXT");
        Facts.Builder factsBuilder = Facts.Builder.create();
        SelectionCriteria sectionCriteria = SelectionCriteria.createCriteria(null, contextQualifiers,
                getAgendaContext(CONTEXT_NAME));
        //return engine.execute(sectionCriteria, Facts.EMPTY_FACTS, null);
        factsBuilder.addFact("eresourceApproval", "true");
        return engine.execute(sectionCriteria, factsBuilder.build(), executionOptions);
    }

    /**
     * This method returns AgendaContext using agendaName..
     *
     * @param agendaName
     * @return Map
     */
    protected Map<String, String> getAgendaContext(String agendaName) {
        Map<String, String> selector = new HashMap<String, String>();
        selector.put(OLEConstants.NAME, agendaName);
        return selector;
    }
}
