package org.kuali.ole.ingest.krms.action;

import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.framework.engine.Action;
import org.kuali.rice.krms.impl.type.ActionTypeServiceBase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 8/22/12
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleMaxFineTypeService extends ActionTypeServiceBase {
    @Override
    public Action loadAction(ActionDefinition actionDefinition) {
        String parameter= actionDefinition.getAttributes().get(OLEConstants.MAX_FINE);
        return  new OleMaxFine(parameter);
    }
    public class OleMaxFine implements Action {
        private BigDecimal maxFine;


        public OleMaxFine(String maxFine) {
            this.maxFine = new BigDecimal(maxFine);
        }

        @Override
        public void execute(ExecutionEnvironment environment) {
            BigDecimal  fineRate = (BigDecimal)environment.getEngineResults().getAttribute(OLEConstants.FINE_RATE);
             if(fineRate.compareTo(maxFine)>0){
                environment.getEngineResults().setAttribute(OLEConstants.MAX_FINE,maxFine);
             }
        }

        @Override
        public void executeSimulation(ExecutionEnvironment environment) {
            execute(environment);
        }
    }
}
