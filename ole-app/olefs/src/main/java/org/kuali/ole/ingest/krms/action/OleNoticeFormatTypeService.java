package org.kuali.ole.ingest.krms.action;

import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.service.OLEDeliverNoticeHelperService;
import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.framework.engine.Action;
import org.kuali.rice.krms.impl.type.ActionTypeServiceBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vivekb on 9/16/14.
 */
public class OleNoticeFormatTypeService extends ActionTypeServiceBase {

    private OLEDeliverNoticeHelperService oleDeliverNoticeHelperService;
    @Override
    public Action loadAction(ActionDefinition actionDefinition) {
        String notice= actionDefinition.getAttributes().get("notice");
        String noticeType= actionDefinition.getAttributes().get("noticeType");


        return  new OleNoticeFormat(notice,noticeType);
    }
    /**
     * OleNoticeFormat is the action class
     */
    public class OleNoticeFormat implements Action {
        private String notice;
        private String noticeType;

        public OleNoticeFormat(String notice, String noticeType) {
            this.notice = notice;
            this.noticeType = noticeType;
        }

        /**
         * This method takes the initial request when creating the BibAction.
         *
         * @param executionEnvironment
         */
        @Override
        public void execute(ExecutionEnvironment executionEnvironment) {
            List<OLEDeliverNotice> deliverNotices = (List<OLEDeliverNotice>) executionEnvironment.getEngineResults().getAttribute("deliverNotices");
            Map noticeMap =  new HashMap();
            String[] notices = notice!=null? notice.split("[|]") : new String[0];
            String[] noticeTypes = noticeType!=null ? noticeType.split("[|]") : new String[0];
            if(notices.length == noticeTypes.length){
                for(int i =0 ;i<notices.length ; i++){
                    noticeMap.put(notices[i],noticeTypes[i]);
                }
            }
            if(deliverNotices!=null){
                for(OLEDeliverNotice deliverNotice : deliverNotices){
                    if(noticeMap.get(deliverNotice.getNoticeType())!=null){
                        deliverNotice.setNoticeSendType((String)noticeMap.get(deliverNotice.getNoticeType()));
                    }
                }
            }
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
