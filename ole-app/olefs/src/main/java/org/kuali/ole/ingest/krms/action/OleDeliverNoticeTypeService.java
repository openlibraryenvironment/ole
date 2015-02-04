package org.kuali.ole.ingest.krms.action;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.service.OLEDeliverNoticeHelperService;
import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.framework.engine.Action;
import org.kuali.rice.krms.impl.type.ActionTypeServiceBase;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 9/15/14
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleDeliverNoticeTypeService extends ActionTypeServiceBase {

    private OLEDeliverNoticeHelperService oleDeliverNoticeHelperService;
    @Override
    public Action loadAction(ActionDefinition actionDefinition) {
        String noticeType= actionDefinition.getAttributes().get("noticeType");
        String numberOfOverdueToBeSent= actionDefinition.getAttributes().get("numberOfOverdueToBeSent");
        String intervalToGenerateNotice= actionDefinition.getAttributes().get("intervalToGenerateNotice");
        String replacementBill= actionDefinition.getAttributes().get("replacementBill");

        return  new OleDeliverNotice(noticeType,numberOfOverdueToBeSent,intervalToGenerateNotice,replacementBill);
    }
    /**
     * OleDeliverNotice is the action class
     */
    public class OleDeliverNotice implements Action {
        private String noticeType;
        private String numberOfOverdueToBeSent;
        private String intervalToGenerateNotice;
        private String replacementBill;


        public OleDeliverNotice(String noticeType, String numberOfOverdueToBeSent, String intervalToGenerateNotice, String replacementBill) {
            this.noticeType = noticeType;
            this.numberOfOverdueToBeSent = numberOfOverdueToBeSent;
            this.intervalToGenerateNotice = intervalToGenerateNotice;
            this.replacementBill = replacementBill;
        }

        /**
         * This method takes the initial request when creating the OleDeliverNotice.
         *
         * @param executionEnvironment
         */
        @Override public void execute(ExecutionEnvironment executionEnvironment) {
            List<OLEDeliverNotice> deliverNotices = (List<OLEDeliverNotice>) executionEnvironment.getEngineResults().getAttribute("deliverNotices");
            if(deliverNotices==null){
                deliverNotices = new ArrayList<>();
            }
            Timestamp dueDate = (Timestamp) executionEnvironment.getEngineResults().getAttribute(OLEConstants.ITEMS_DUE_DATE);
            getOleDeliverNoticeHelperService().generateDeliverNoticesUsingKRMSValues(deliverNotices,dueDate,noticeType,null,
                    numberOfOverdueToBeSent,intervalToGenerateNotice,replacementBill);
            executionEnvironment.getEngineResults().setAttribute("deliverNotices",deliverNotices);

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
    }

    public OLEDeliverNoticeHelperService getOleDeliverNoticeHelperService() {
        return oleDeliverNoticeHelperService;
    }

    public void setOleDeliverNoticeHelperService(OLEDeliverNoticeHelperService oleDeliverNoticeHelperService) {
        this.oleDeliverNoticeHelperService = oleDeliverNoticeHelperService;
    }

}
