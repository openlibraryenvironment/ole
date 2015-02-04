package org.kuali.ole.ingest.krms.action;

import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.framework.engine.Action;
import org.kuali.rice.krms.impl.type.ActionTypeServiceBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 8/22/12
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleErrorActionTypeService extends ActionTypeServiceBase {
    @Override
    public Action loadAction(ActionDefinition actionDefinition) {
        String parameter= actionDefinition.getAttributes().get(OLEConstants.ERROR_ACTION);
        return  new OleErrorAction(parameter);
    }
    public class OleErrorAction implements Action {
        private String errorAction;


        public OleErrorAction(String errorAction) {
            this.errorAction = errorAction;
        }

        @Override
        public void execute(ExecutionEnvironment environment) {
            List<String> errors = new ArrayList<String>();
         //   DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
            List<String> errorMessage = (List<String>)  environment.getEngineResults().getAttribute(OLEConstants.ERROR_ACTION);
            if(errorMessage!=null){
                errors.addAll(errorMessage);
            }
            errors.add(errorAction);
           // dataCarrierService.addData(OLEConstants.ERROR_ACTION,errors);
            environment.getEngineResults().setAttribute(OLEConstants.ERROR_ACTION,errors);
        }

        @Override
        public void executeSimulation(ExecutionEnvironment environment) {
            execute(environment);
        }
    }
}
