package org.kuali.ole.ingest.krms.action;

import org.apache.log4j.Logger;
import org.kuali.common.util.Str;
import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.deliver.service.OLEDeliverNoticeHelperService;
import org.kuali.ole.deliver.service.impl.OLEDeliverNoticeHelperServiceImpl;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.framework.engine.Action;
import org.kuali.rice.krms.impl.type.ActionTypeServiceBase;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 02/05/13
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleRecallDueDateTypeService extends ActionTypeServiceBase {
    private static final Logger LOG = Logger.getLogger(OleRecallDueDateTypeService.class);
    private OLEDeliverNoticeHelperService oleDeliverNoticeHelperService;
    @Override
    public Action loadAction(ActionDefinition actionDefinition) {
        String minimumLoanPeriod= actionDefinition.getAttributes().get(OLEConstants.MINIMUM_LOAN_PERIOD);
        String recallLoanPeriod= actionDefinition.getAttributes().get(OLEConstants.RECALL_LOAN_PERIOD);
        String courtesyNoticeInterval = actionDefinition.getAttributes().get("courtesyNoticeInterval");
         String numberOfOverdueToBeSent = actionDefinition.getAttributes().get("numberOfOverdueToBeSent");
         String intervalToGenerateOverdueNotice = actionDefinition.getAttributes().get("intervalToGenerateOverdueNotice");
         String replacementBill = actionDefinition.getAttributes().get("replacementBill");
        return  new OleRecallDueDate(minimumLoanPeriod,recallLoanPeriod,courtesyNoticeInterval,numberOfOverdueToBeSent,intervalToGenerateOverdueNotice,replacementBill);
    }
    public class OleRecallDueDate implements Action {
        private String minimumLoanPeriod;
        private String recallLoanPeriod;
        private String courtesyNoticeInterval;
        private String numberOfOverdueToBeSent;
        private String intervalToGenerateOverdueNotice;
        private String replacementBill;



        public OleRecallDueDate(String minimumLoanPeriod, String recallLoanPeriod,String courtesyNoticeInterval,String numberOfOverdueToBeSent,String intervalToGenerateOverdueNotice,String replacementBill) {
            this.minimumLoanPeriod = minimumLoanPeriod;
            this.recallLoanPeriod = recallLoanPeriod;
            this.courtesyNoticeInterval=courtesyNoticeInterval;
            this.numberOfOverdueToBeSent=numberOfOverdueToBeSent;
            this.intervalToGenerateOverdueNotice=intervalToGenerateOverdueNotice;
            this.replacementBill=replacementBill;

        }

        @Override
        public void execute(ExecutionEnvironment environment) {
            Timestamp recallDueDate = null;
            LoanProcessor loanProcessor = new LoanProcessor();
            DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
            Calendar calendar = Calendar.getInstance();
            Date loanedDate = (Date) dataCarrierService.getData(OLEConstants.LOANED_DATE);
            if (loanedDate != null && minimumLoanPeriod != null && recallLoanPeriod != null) {
                Timestamp minimumLoanPeriodDate = calculateLoanDueDate(calendar, minimumLoanPeriod);
                calendar = Calendar.getInstance();
                Timestamp recallLoanPeriodDate = calculateLoanDueDate(calendar, recallLoanPeriod);
                /**
                 * If
                 * minimumLoanPeriod > = recallLoanPeriod
                 *
                 * Then
                 *
                 * RecallDueDate = CurrentDate + recallLoanPeriod
                 */
                if (minimumLoanPeriodDate.compareTo(recallLoanPeriodDate) <= 0) {
                    calendar = Calendar.getInstance();
                    recallDueDate = calculateLoanDueDate(calendar, recallLoanPeriod);
                } else {
                    /*
                     * If
                     * CurrentDate - LoanedDate >= minimumLoanPeriod - recallLoanPeriod ==>
                     * CurrentDate + recallLoanPeriod >= minimumLoanPeriod + LoanedDate
                     *
                     * Then
                     *
                     * RecallDueDate = CurrentDate + recallLoanPeriod
                     *
                     * Else
                     *
                     * RecallDueDate = CurrentDate + [minimumLoanPeriod - (CurrentDate - LoanedDate)]
                     *               = CurrentDate + minimumLoanPeriod - CurrentDate + LoanedDate
                     *               = minimumLoanPeriod + LoanedDate
                     */

                    // CurrentDate + recallLoanPeriod
                    calendar = Calendar.getInstance();
                    Timestamp recallLoanPlusCurrentDate = calculateLoanDueDate(calendar, recallLoanPeriod);

                    // minimumLoanPeriod + LoanedDate
                    calendar.setTime(loanedDate);
                    Timestamp minimumLoanPlusLoanedDate = calculateLoanDueDate(calendar, minimumLoanPeriod);

                    if (recallLoanPlusCurrentDate.compareTo(minimumLoanPlusLoanedDate) >= 0) {
                        recallDueDate = recallLoanPlusCurrentDate;
                    } else {
                        recallDueDate = minimumLoanPlusLoanedDate;
                    }
                    String defaultCloseTime = loanProcessor.getParameter(OLEParameterConstants.DEF_CLOSE_TIME);
                    recallDueDate = Timestamp.valueOf(new SimpleDateFormat(OLEConstants.CHECK_IN_DATE_TIME_FORMAT).
                            format(recallDueDate).concat(" ").concat(defaultCloseTime));
                }
                environment.getEngineResults().setAttribute(OLEConstants.RECALL_DUE_DATE, recallDueDate);

                List<OLEDeliverNotice> deliverNotices = (List<OLEDeliverNotice>) environment.getEngineResults().getAttribute("deliverNotices");
                if(deliverNotices==null){
                    deliverNotices = new ArrayList<>();
                }


                getOleDeliverNoticeHelperService().generateDeliverNoticesUsingKRMSValues(deliverNotices,recallDueDate,"CourtesyNotice",null,
                        numberOfOverdueToBeSent,courtesyNoticeInterval,null);

                getOleDeliverNoticeHelperService().generateDeliverNoticesUsingKRMSValues(deliverNotices,recallDueDate,"OverdueNotice",null,
                        numberOfOverdueToBeSent,courtesyNoticeInterval,replacementBill);

                environment.getEngineResults().setAttribute("deliverNotices",deliverNotices);

                LOG.info("minimumLoanPeriod---------->" + minimumLoanPeriod);
                LOG.info("recallLoanPeriod---------->" + recallLoanPeriod);
            }
        }

        @Override
        public void executeSimulation(ExecutionEnvironment environment) {
            execute(environment);
        }
        private Timestamp calculateLoanDueDate(Calendar calendar,String loanPeriod) {
            String loanPeriodType[]=null;
            Timestamp dueDate = null;
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
    }
    public OLEDeliverNoticeHelperService getOleDeliverNoticeHelperService() {
        if(oleDeliverNoticeHelperService ==null){
            oleDeliverNoticeHelperService = SpringContext.getBean(OLEDeliverNoticeHelperServiceImpl.class);
        }
        return oleDeliverNoticeHelperService;
    }

    public void setOleDeliverNoticeHelperService(OLEDeliverNoticeHelperService oleDeliverNoticeHelperService) {
        this.oleDeliverNoticeHelperService = oleDeliverNoticeHelperService;
    }
}
