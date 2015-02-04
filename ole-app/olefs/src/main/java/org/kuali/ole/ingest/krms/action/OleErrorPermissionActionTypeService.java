package org.kuali.ole.ingest.krms.action;

import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.framework.engine.Action;
import org.kuali.rice.krms.impl.type.ActionTypeServiceBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 8/22/12
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleErrorPermissionActionTypeService extends ActionTypeServiceBase {
    @Override
    public Action loadAction(ActionDefinition actionDefinition) {
        String errorMessage= actionDefinition.getAttributes().get(OLEConstants.ERROR_ACTION);
        String roleName= actionDefinition.getAttributes().get(OLEConstants.PERMISSION_NAME);
        return  new OleErrorPermissionAction(errorMessage,roleName);
    }
    public class OleErrorPermissionAction implements Action {
        private String errorMessage;
        private String permissionName;


        public OleErrorPermissionAction(String errorMessage,String permissionName) {
            this.errorMessage = errorMessage;
            this.permissionName = permissionName;
        }

        @Override
        public void execute(ExecutionEnvironment environment) {
            HashMap<String,String> errorsAndPermission = new HashMap<>();
        //    DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
            HashMap<String,String> errorMessages = (HashMap<String,String>)  environment.getEngineResults().getAttribute(OLEConstants.ERRORS_AND_PERMISSION);
            if(errorMessages!=null){
                errorsAndPermission.putAll(errorMessages);
            }
            errorsAndPermission.put(errorMessage, permissionName);
          //  dataCarrierService.addData(OLEConstants.ERRORS_AND_PERMISSION , errorsAndPermission);
            environment.getEngineResults().setAttribute(OLEConstants.ERRORS_AND_PERMISSION , errorsAndPermission);
        }

        @Override
        public void executeSimulation(ExecutionEnvironment environment) {
            execute(environment);
        }
    }
}
