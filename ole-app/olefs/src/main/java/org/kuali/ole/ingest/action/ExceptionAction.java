package org.kuali.ole.ingest.action;

import org.kuali.ole.OLEConstants;
import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.framework.engine.Action;

/**
 * ExceptionAction is used to execute an action when KRMS Rules throws an exeception.
 */
public class ExceptionAction implements Action {
    /**
     *     This method sets the  Exception creation flag in executionEnvironment as true.
     * @param executionEnvironment
     */
    @Override
    public void execute(ExecutionEnvironment executionEnvironment) {
        executionEnvironment.getEngineResults().setAttribute(OLEConstants.EXCEPTION_CREATION_FLAG, true);
    }

    /**
     *  This method simulates the executionEnvironment.
     * @param executionEnvironment
     */
    @Override
    public void executeSimulation(ExecutionEnvironment executionEnvironment) {
        execute(executionEnvironment);
    }
}
