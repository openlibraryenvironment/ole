package org.kuali.ole.ingest.krms.action;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleItemAvailableStatus;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.framework.engine.Action;
import org.kuali.rice.krms.impl.type.ActionTypeServiceBase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 8/24/12
 * Time: 1:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleUpdateItemStatusTypeService extends ActionTypeServiceBase {

    @Override
    public Action loadAction(ActionDefinition actionDefinition) {
        String parameter= actionDefinition.getAttributes().get(OLEConstants.UPDATE_ITEM_STATUS);
        return  new OleUpdateItemStatus(parameter);
    }

    public class OleUpdateItemStatus implements Action {
        private String itemStatus;
        private BusinessObjectService businessObjectService;

        public OleUpdateItemStatus(String itemStatus) {
            this.itemStatus = itemStatus;
            this.businessObjectService = getBusinessObjectService();
        }

        @Override
        public void execute(ExecutionEnvironment environment) {
            Map criteriaMap = new HashMap();
            criteriaMap.put(OLEConstants.ITEM_STATUS_CODE, itemStatus);
            OleItemAvailableStatus itemAvailableStatus = businessObjectService.findByPrimaryKey(OleItemAvailableStatus.class,criteriaMap);
            if(itemAvailableStatus!=null){
                environment.getEngineResults().setAttribute(OLEConstants.ITEM_STATUS,itemStatus);
            }
        }

        @Override
        public void executeSimulation(ExecutionEnvironment environment) {
            execute(environment);
        }

        /**
         *  Gets the businessObjectService attribute.
         * @return  Returns the businessObjectService
         */
        public BusinessObjectService getBusinessObjectService() {
            if (null == businessObjectService) {
                businessObjectService = KRADServiceLocator.getBusinessObjectService();
            }
            return businessObjectService;
        }
    }
}
