package org.kuali.ole.ingest.krms.action;

import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.framework.engine.Action;
import org.kuali.rice.krms.impl.type.ActionTypeServiceBase;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 8/22/12
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleMaxNumberItemTypeService extends ActionTypeServiceBase {
    @Override
    public Action loadAction(ActionDefinition actionDefinition) {
        String itemType= actionDefinition.getAttributes().get("itemType");
        String maxNumber= actionDefinition.getAttributes().get("maxNumber");
        String errorMessage= actionDefinition.getAttributes().get("errorMessage");

        return  new MaxNumberItemType(itemType,maxNumber,errorMessage);
    }
    /**
     * MaxNumberItemType is the action class
     */
    public class MaxNumberItemType implements Action {
        private String itemType;
        private String maxNumber;
        private String errorMessage;

        public MaxNumberItemType(String itemType, String maxNumber, String errorMessage) {
            this.itemType = itemType;
            this.maxNumber = maxNumber;
            this.errorMessage = errorMessage;
        }

        /**
         * This method takes the initial request when creating the BibAction.
         *
         * @param executionEnvironment
         */
        @Override
        public void execute(ExecutionEnvironment executionEnvironment) {
            DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
            HashMap<String,Integer> itemTypeMap  = (HashMap<String,Integer>) dataCarrierService.getData("itemTypeMap");
            if(itemTypeMap!=null && itemTypeMap.size()>0){
                Integer itemCount = itemTypeMap.get(itemType);
                Integer maxCount= maxNumber!=null && isNumber(maxNumber) ? Integer.parseInt(maxNumber) : null;
                if(itemCount!=null && maxCount!=null && itemCount.compareTo(maxCount)>0){
                    OleErrorActionTypeService oleErrorActionTypeService = new OleErrorActionTypeService();
                    OleErrorActionTypeService.OleErrorAction oleErrorAction = oleErrorActionTypeService.new OleErrorAction(errorMessage);
                    oleErrorAction.execute(executionEnvironment);
                }
            }
        }
        /**
         * This method simulate the executionEnvironment.
         *
         * @param executionEnvironment
         */

        @Override
        public void executeSimulation(ExecutionEnvironment executionEnvironment) {
            execute(executionEnvironment);
        }

        public  boolean isNumber(String number) {
            for (int i = 0; i < number.length(); i++) {
                if (!Character.isDigit(number.charAt(i)))
                    return false;
            }
            return true;
        }
    }
}
