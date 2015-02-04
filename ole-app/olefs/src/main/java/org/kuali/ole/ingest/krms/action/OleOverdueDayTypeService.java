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
 * Time: 3:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleOverdueDayTypeService extends ActionTypeServiceBase {

    /**
     *      This method invokes appropriate Bib action based on action definition's name
     * @param actionDefinition
     * @return Action
     */
    @Override
    public Action loadAction(ActionDefinition actionDefinition) {
       String parameter= actionDefinition.getAttributes().get(OLEConstants.OVERDUE_DAY);
       return new OleOverDueDayAction(parameter);
    }

    public class OleOverDueDayAction implements Action{
        private int numberOfDays;

        public OleOverDueDayAction(String numberOfDays) {
            this.numberOfDays = new Integer(numberOfDays);
        }

        @Override
        public void execute(ExecutionEnvironment environment) {
            List<String> errors = new ArrayList<String>();
            DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
            List<String> errorMessage = (List<String>)  environment.getEngineResults().getAttribute(OLEConstants.ERROR_ACTION);
            if(errorMessage!=null){
                errors.addAll(errorMessage);
            }
            List<Integer> listOfOverDueDays = (List<Integer>) dataCarrierService.getData(OLEConstants.LIST_OVERDUE_DAYS);
            for(Integer overDueDays : listOfOverDueDays){
                if(overDueDays>=numberOfDays){
                    errors.add(OLEConstants.OVERDUE_DAY_LIMIT_ERROR);
                 //   dataCarrierService.addData(OLEConstants.ERROR_ACTION,errors);
                    environment.getEngineResults().setAttribute(OLEConstants.ERROR_ACTION,errors);
                    break;
                }
            }
        }

        @Override
        public void executeSimulation(ExecutionEnvironment environment) {
            execute(environment);
        }
    }
}
