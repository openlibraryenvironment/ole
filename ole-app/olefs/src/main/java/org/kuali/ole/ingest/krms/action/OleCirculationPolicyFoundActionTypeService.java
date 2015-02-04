package org.kuali.ole.ingest.krms.action;

import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.api.engine.Term;
import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.framework.engine.Action;
import org.kuali.rice.krms.impl.type.ActionTypeServiceBase;

import java.util.Date;
import java.util.Map;
/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 1/24/13
 * Time: 1:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleCirculationPolicyFoundActionTypeService extends ActionTypeServiceBase {

    @Override
    public Action loadAction(ActionDefinition actionDefinition) {
        String parameter= actionDefinition.getAttributes().get(OLEConstants.CIRC_POLICY_FOUND);
        return new OleCirculationPolicyFoundAction(parameter);
    }

    public class OleCirculationPolicyFoundAction implements Action{
        private String parameter;


        public OleCirculationPolicyFoundAction(String parameter) {
            this.parameter = parameter;
        }

        @Override
        public void execute(ExecutionEnvironment environment) {
            Boolean circulationPolicyFound = parameter!=null?parameter.equals(OLEConstants.TRUE):false;
            DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
            Map<Term, Object> facts = environment.getFacts();
            String patronId="";
            String itemId="";
            Date itemDueDate=null;
            for (Term term : facts.keySet()) {
                if(term.getName().equals(OLEConstants.PATRON_ID_POLICY)){
                    patronId = (String)facts.get(term);
                }
                if(term.getName().equals(OLEConstants.ITEM_ID_POLICY)){
                    itemId = (String)facts.get(term);
                }
                if(term.getName().equals(OLEConstants.ITEMS_DUE_DATE)){
                    itemDueDate = (Date)facts.get(term);
                }
            }
            dataCarrierService.addData(patronId+itemId,circulationPolicyFound);
            environment.getEngineResults().setAttribute(OLEConstants.ITEMS_DUE_DATE,itemDueDate);
        }

        @Override
        public void executeSimulation(ExecutionEnvironment environment) {
            execute(environment);
        }
    }
}
