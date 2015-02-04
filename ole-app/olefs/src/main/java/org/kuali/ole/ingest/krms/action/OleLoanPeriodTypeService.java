package org.kuali.ole.ingest.krms.action;

import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.bo.OleFixedDateTimeSpan;
import org.kuali.ole.deliver.bo.OleFixedDueDate;
import org.kuali.ole.deliver.calendar.service.OleCalendarService;
import org.kuali.ole.deliver.calendar.service.impl.OleCalendarServiceImpl;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.framework.engine.Action;
import org.kuali.rice.krms.impl.repository.RuleBo;
import org.kuali.rice.krms.impl.type.ActionTypeServiceBase;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 8/22/12
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleLoanPeriodTypeService extends ActionTypeServiceBase {
    private  DataCarrierService dataCarrierService ;

    public DataCarrierService getDataCarrierService() {
        if(dataCarrierService == null){
            dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
        }
        return dataCarrierService;
    }

    public void setDataCarrierService(DataCarrierService dataCarrierService) {
        this.dataCarrierService = dataCarrierService;
    }

    @Override
    public Action loadAction(ActionDefinition actionDefinition) {
        String parameter= actionDefinition.getAttributes().get(OLEConstants.LOAN_PERIOD);
        String ruleId  = actionDefinition.getRuleId();
        return  new OleLoanPeriod(parameter,ruleId);
    }

    private BusinessObjectService businessObjectService;

    /**
     *  Gets the businessObjectService attribute.
     * @return  Returns the businessObjectService
     */
    private BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }


    public class OleLoanPeriod implements Action {
        private String loanPeriod;
        private String ruleId;


        public OleLoanPeriod(String loanPeriod,String ruleId) {
            this.loanPeriod = loanPeriod;
            this.ruleId = ruleId;
        }

        @Override
        public void execute(ExecutionEnvironment environment) {
            LoanProcessor loanProcessor = new LoanProcessor();
            RuleBo ruleBo = getRuleBo(ruleId);
            String ruleName = ruleBo!=null?ruleBo.getName():"";
            environment.getEngineResults().setAttribute(OLEConstants.CIRCULATION_POLICY_SET_ID,ruleName);
            if(loanPeriod!=null && loanPeriod.equalsIgnoreCase(OLEConstants.FIXED_DUE_DATE)){
                java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
                List<OleFixedDueDate> oleFixedDueDates =  getFixedDueDateBasedOnPolicySet(ruleName);
                if(oleFixedDueDates!=null && oleFixedDueDates.size()>0){
                    for(OleFixedDueDate oleFixedDueDate : oleFixedDueDates){
                        List<OleFixedDateTimeSpan> oleFixedDateTimeSpans = oleFixedDueDate.getOleFixedDateTimeSpanList();
                        for(OleFixedDateTimeSpan oleFixedDateTimeSpan : oleFixedDateTimeSpans){
                            if(dateComparison(date,oleFixedDateTimeSpan.getFromDueDate(),oleFixedDateTimeSpan.getToDueDate())){
                                String defaultCloseTime = loanProcessor.getParameter(OLEParameterConstants.DEF_CLOSE_TIME);
                                Timestamp timestamp = Timestamp.valueOf(new SimpleDateFormat(OLEConstants.CHECK_IN_DATE_TIME_FORMAT).
                                        format(oleFixedDateTimeSpan.getFixedDueDate()).concat(" ").concat(defaultCloseTime));
                                environment.getEngineResults().setAttribute(OLEConstants.DUE_DATE,timestamp);
                                break;
                            }
                        }
                    }
                }
            } else{
             //   loanPeriod = renewalCheck(loanPeriod);
                OleCalendarService oleCalendarService = new OleCalendarServiceImpl();
                String groupId = (String) getDataCarrierService().getData(OLEConstants.GROUP_ID);
                String[] periodType=null;
                if(loanPeriod != null && !loanPeriod.equalsIgnoreCase("")){
                    periodType = loanPeriod.split("-");
                }
                   String dayOrHours =periodType!=null && periodType.length > 1 ? periodType[1] : null;
                Timestamp dueDate = null;
                if(dayOrHours != null && dayOrHours.equalsIgnoreCase("H")){
                 dueDate = loanPeriod!=null ? oleCalendarService.calculateDueDateHrs(groupId,loanPeriod,new Timestamp(System.currentTimeMillis())) : null;     //changed
                } else if(dayOrHours != null && dayOrHours.equalsIgnoreCase("D")){
                    String sysFlag = loanProcessor.getParameter(OLEConstants.CALENDER_FLAG);
                    if(sysFlag.equalsIgnoreCase("true")){
                        dueDate = loanPeriod!=null ? oleCalendarService.calculateDueDateHrs(groupId, loanPeriod, new Timestamp(System.currentTimeMillis())) : null;     //changed
                    } else {
                        dueDate = loanPeriod!=null ?oleCalendarService.calculateDueDate(groupId,loanPeriod,new Timestamp(System.currentTimeMillis())) : null;     //changed
                    }
                }

                if(dueDate==null){
                    environment.getEngineResults().setAttribute(OLEConstants.DUE_DATE,null);
                }else{
                    environment.getEngineResults().setAttribute(OLEConstants.DUE_DATE,dueDate);
                }
                getDataCarrierService().addData(OLEConstants.DUE_DATE,dueDate);
            }
        }

        private String renewalCheck(String loanPeriod) {
            Integer hoursDiff =(Integer) getDataCarrierService().getData(OLEConstants.HOURS_DIFF);
            if(hoursDiff!=null && hoursDiff>0){
                String loanPeriodType[]=null;
                if(loanPeriod != null && loanPeriod.trim().length()>0){
                    loanPeriodType =  loanPeriod.split("-");
                    int loanPeriodValue =  Integer.parseInt(loanPeriodType[0].toString());
                    String loanPeriodTypeValue =  loanPeriodType[1].toString();
                    if(loanPeriodTypeValue.equalsIgnoreCase("H")){
                        loanPeriodValue = loanPeriodValue + hoursDiff;
                    } else if(loanPeriodTypeValue.equalsIgnoreCase("D")) {
                        loanPeriodValue = loanPeriodValue + (hoursDiff/24);
                    } else  {
                        loanPeriodValue = loanPeriodValue + hoursDiff;
                    }
                    loanPeriod = loanPeriodValue+"-"+loanPeriodTypeValue;
                }
            }
            return loanPeriod ;
        }

        @Override
        public void executeSimulation(ExecutionEnvironment environment) {
            execute(environment);
        }
        private Timestamp calculateLoanDueDate(String loanPeriod) {
            String loanPeriodType[]=null;
            Timestamp dueDate = null;
            Calendar calendar = Calendar.getInstance();
            if(loanPeriod != null && loanPeriod.trim().length()>0){
                loanPeriodType =  loanPeriod.split("-");
                int loanPeriodValue =  Integer.parseInt(loanPeriodType[0].toString());
                String loanPeriodTypeValue =  loanPeriodType[1].toString();
                if(loanPeriodTypeValue.equalsIgnoreCase("M")){
                    calendar.add(Calendar.MINUTE, loanPeriodValue);
                } else if(loanPeriodTypeValue.equalsIgnoreCase("H")) {
                    calendar.add(Calendar.HOUR, loanPeriodValue);
                } else if(loanPeriodTypeValue.equalsIgnoreCase("W")) {
                    calendar.add(Calendar.WEEK_OF_MONTH, loanPeriodValue);
                } else {
                    calendar.add(Calendar.DATE, loanPeriodValue);
                }
                dueDate =  new Timestamp(calendar.getTime().getTime());
            }
            return dueDate;
        }
        private List<OleFixedDueDate> getFixedDueDateBasedOnPolicySet(String ruleName){
            Map<String,String> policySet = new HashMap<String, String>();
            policySet.put("circulationPolicySetId",ruleName);
            List<OleFixedDueDate> oleFixedDueDates =  (List<OleFixedDueDate>)KRADServiceLocator.getBusinessObjectService().findMatching(OleFixedDueDate.class,policySet);
            return oleFixedDueDates;
        }
        private RuleBo getRuleBo(String ruleId){
            Map<String,String> ruleMap = new HashMap<String, String>();
            ruleMap.put("id",ruleId);
            RuleBo ruleBo = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(RuleBo.class,ruleMap);
            return ruleBo;
        }
        private boolean dateComparison(Date date1,Date date2,Date date3){
            if(date1!=null && date2!=null && date3!=null){
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                String dateString1 = dateFormat.format(date1);
                String dateString2 = dateFormat.format(date2);
                String dateString3 = dateFormat.format(date3);
                if((date1.after(date2) && date1.before(date3)) || dateString1.equals(dateString2) || dateString1.equals(dateString3)){
                       return true;
                }
            }
            return false;
        }
    }
}
