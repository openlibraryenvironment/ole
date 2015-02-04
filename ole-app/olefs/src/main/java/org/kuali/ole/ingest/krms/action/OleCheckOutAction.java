package org.kuali.ole.ingest.krms.action;

import org.kuali.ole.OLEConstants;
import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.framework.engine.Action;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 8/24/12
 * Time: 1:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleCheckOutAction implements Action {

    private String parameter;


    public OleCheckOutAction(String parameter) {
        this.parameter = parameter;
    }

    @Override
    public void execute(ExecutionEnvironment environment) {
         environment.getEngineResults().setAttribute(OLEConstants.CHECKOUT,true);
    }

    @Override
    public void executeSimulation(ExecutionEnvironment environment) {
        execute(environment);
    }
}
