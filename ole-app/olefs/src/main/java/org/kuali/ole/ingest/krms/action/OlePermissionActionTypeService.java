package org.kuali.ole.ingest.krms.action;

import org.kuali.ole.OLEConstants;
import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.framework.engine.Action;
import org.kuali.rice.krms.impl.type.ActionTypeServiceBase;

/**
 * Created with IntelliJ IDEA.
 * User: meenau
 * Date: 7/7/13
 * Time: 5:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class OlePermissionActionTypeService extends ActionTypeServiceBase {
    @Override
    public Action loadAction(ActionDefinition actionDefinition) {
        String parameter= actionDefinition.getAttributes().get(OLEConstants.PERMISSION_ACTION);
        return  new OlePermissionAction(parameter);
    }
    public class OlePermissionAction implements Action {
        private String permissionAction;


        public OlePermissionAction(String permissionAction) {
            this.permissionAction = permissionAction;
        }

        @Override
        public void execute(ExecutionEnvironment environment) {
            environment.getEngineResults().setAttribute(OLEConstants.PERMISSION_ACTION,permissionAction);
        }

        @Override
        public void executeSimulation(ExecutionEnvironment environment) {
            execute(environment);
        }
    }
}
