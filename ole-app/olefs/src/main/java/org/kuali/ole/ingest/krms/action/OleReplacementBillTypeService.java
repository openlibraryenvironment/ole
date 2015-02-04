package org.kuali.ole.ingest.krms.action;

import org.kuali.ole.OLEConstants;
import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.framework.engine.Action;
import org.kuali.rice.krms.impl.type.ActionTypeServiceBase;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 8/22/12
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleReplacementBillTypeService extends ActionTypeServiceBase {
    @Override
    public Action loadAction(ActionDefinition actionDefinition) {
        String parameter= actionDefinition.getAttributes().get(OLEConstants.REPLACEMENT_BILL);
        return  new OleReplacementBill(parameter);
    }
    public class OleReplacementBill implements Action {
        private BigDecimal replacementBill;


        public OleReplacementBill(String replacementBill) {
            this.replacementBill = new BigDecimal(replacementBill);
        }

        @Override
        public void execute(ExecutionEnvironment environment) {
           environment.getEngineResults().setAttribute(OLEConstants.REPLACEMENT_BILL,replacementBill);
        }

        @Override
        public void executeSimulation(ExecutionEnvironment environment) {
            execute(environment);
        }
    }
}
