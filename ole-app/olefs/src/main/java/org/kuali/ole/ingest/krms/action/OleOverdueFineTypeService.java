package org.kuali.ole.ingest.krms.action;

import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.calendar.service.OleCalendarService;
import org.kuali.ole.deliver.calendar.service.impl.OleCalendarServiceImpl;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.framework.engine.Action;
import org.kuali.rice.krms.impl.type.ActionTypeServiceBase;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 8/22/12
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleOverdueFineTypeService extends ActionTypeServiceBase {
    @Override
    public Action loadAction(ActionDefinition actionDefinition) {
        String parameter= actionDefinition.getAttributes().get(OLEConstants.OVER_DUE_FINE);
        return  new OleOverdueFine(parameter);
    }
    public class OleOverdueFine implements Action {
        private String overdueFine;

        public OleOverdueFine(String overdueFine) {
            this.overdueFine = overdueFine;
        }

        @Override
        public void execute(ExecutionEnvironment environment) {
            long begin = System.currentTimeMillis();
            DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
            Integer hoursDiff =(Integer) dataCarrierService.getData(OLEConstants.HOURS_DIFF);
            String groupId = (String) dataCarrierService.getData(OLEConstants.GROUP_ID);
            Boolean excludeTime = (Boolean) dataCarrierService.getData("EXCLUDE_TIME");
            Timestamp dueDate =(Timestamp)dataCarrierService.getData("DUEDATE");
            Timestamp checkInDate = dataCarrierService.getData("CHECKINDATE")!=null?(Timestamp)dataCarrierService.getData("CHECKINDATE"):new Timestamp(System.currentTimeMillis());
            OleCalendarService oleCalendarService = new OleCalendarServiceImpl();
            Float fineRate;
            if(dueDate.compareTo(checkInDate)==0 && excludeTime!= null && excludeTime){
                fineRate = 0.0f;
            }else {
                fineRate = oleCalendarService.calculateFine(groupId,dueDate,checkInDate,overdueFine);
            }
            //Integer fineRate = calculateFineRate(overdueFine,hoursDiff);
            BigDecimal decimalFineRate = new BigDecimal(fineRate);
            environment.getEngineResults().setAttribute(OLEConstants.FINE_RATE,decimalFineRate);
            long end = System.currentTimeMillis();
            long total = end - begin;
            System.out.println("Time taken Inside OleOverdueFineTypeService - execute"+total);
        }

        @Override
        public void executeSimulation(ExecutionEnvironment environment) {
            execute(environment);
        }
        private Integer calculateFineRate(String overdueFine,Integer hoursDiff) {
            String overdueFineType[]=null;
             int fineRate=0;
            if(overdueFine != null && overdueFine.trim().length()>0){
                overdueFineType =  overdueFine.split("/");
                int loanPeriodValue =  Integer.parseInt(overdueFineType[0].toString());
                String loanPeriodTypeValue =  overdueFineType[1].toString();
                if(loanPeriodTypeValue.equalsIgnoreCase("H")){
                    fineRate = loanPeriodValue*hoursDiff;
                } else if(loanPeriodTypeValue.equalsIgnoreCase("D")) {
                    loanPeriodValue = loanPeriodValue*24;
                    fineRate= loanPeriodValue*hoursDiff;
                } else {
                    fineRate = loanPeriodValue*hoursDiff;
                }

            }
            return fineRate;
        }
    }
}
