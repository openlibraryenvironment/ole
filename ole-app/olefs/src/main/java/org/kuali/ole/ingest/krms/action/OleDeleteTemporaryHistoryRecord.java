package org.kuali.ole.ingest.krms.action;

import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.framework.engine.Action;
import org.kuali.rice.krms.impl.type.ActionTypeServiceBase;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 12/27/12
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleDeleteTemporaryHistoryRecord extends ActionTypeServiceBase {
    @Override
    public Action loadAction(ActionDefinition actionDefinition) {
        String parameter= actionDefinition.getAttributes().get(OLEConstants.DELETE_TEMP_HISTORY_REC);
        return  new OleTemporaryHistoryRecord(parameter);
    }
    public class OleTemporaryHistoryRecord implements Action {
        private String numberOfDays;


        public OleTemporaryHistoryRecord(String numberOfDays) {
            this.numberOfDays = numberOfDays;
        }

        @Override
        public void execute(ExecutionEnvironment environment) {
            DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
            Date checkInDate = (Date) dataCarrierService.getData(OLEConstants.DATE_CHECK_IN);
            String type[]=null;
            if(numberOfDays != null && numberOfDays.trim().length()>0){
                type =  numberOfDays.split("-");
                int value =  Integer.parseInt(type[0].toString());
                if(value <= determineDifferenceInDays(checkInDate)){
                    environment.getEngineResults().setAttribute(OLEConstants.DELETE,true);
                }
            }
        }

        @Override
        public void executeSimulation(ExecutionEnvironment environment) {
            execute(environment);
        }
        private  int determineDifferenceInDays(Date checkInDate) {
            int result = 0;
            if(checkInDate!=null){
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(checkInDate);
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(new Date());
                long diffInMillis = calendar2.getTimeInMillis() - calendar1.getTimeInMillis();
                result =(int) (diffInMillis / (24* 1000 * 60 * 60));
            }
            return result;
        }
    }
}
