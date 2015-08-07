package org.kuali.ole.deliver.notice.executors;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.notice.bo.OleNoticeContentConfigurationBo;
import org.kuali.ole.deliver.notice.bo.OleNoticeFieldLabelMapping;
import org.kuali.ole.deliver.notice.noticeFormatters.RecallRequestEmailContentFormatter;
import org.kuali.ole.deliver.notice.noticeFormatters.RequestEmailContentFormatter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maheswarang on 6/26/15.
 */
public class RecallNoticesExecutor extends RequestNoticesExecutor {


    public RecallNoticesExecutor(List<OLEDeliverNotice> deliverNotices) {
        super(deliverNotices);
    }

    @Override
    public RequestEmailContentFormatter getRequestEmailContentFormatter() {
        if(requestEmailContentFormatter==null){
        return new RecallRequestEmailContentFormatter();
        }else {
            return requestEmailContentFormatter;
        }
    }

    @Override
    public boolean isValidRequestToSendNotice(OleDeliverRequestBo oleDeliverRequestBo) {
        return false;
    }

    @Override
    protected void postProcess() {

    }

    @Override
    public void populateFieldLabelMapping() {
        List<OleNoticeContentConfigurationBo> oleNoticeContentConfigurationBoList = null;
        Map<String,String> noticeConfigurationMap = new HashMap<String,String>();
        noticeConfigurationMap.put("noticeType",OLEConstants.RECALL_NOTICE);
        oleNoticeContentConfigurationBoList= (List<OleNoticeContentConfigurationBo>)getBusinessObjectService().findMatching(OleNoticeContentConfigurationBo.class,noticeConfigurationMap);
        if(oleNoticeContentConfigurationBoList!=null && oleNoticeContentConfigurationBoList.size()>0){
            if(oleNoticeContentConfigurationBoList.get(0)!=null){
                fieldLabelMap.put("noticeTitle",oleNoticeContentConfigurationBoList.get(0).getNoticeTitle());
                fieldLabelMap.put("noticeBody",oleNoticeContentConfigurationBoList.get(0).getNoticeBody());
                fieldLabelMap.put("noticeSubjectLine",oleNoticeContentConfigurationBoList.get(0).getNoticeSubjectLine());
                if(oleNoticeContentConfigurationBoList.get(0).getOleNoticeFieldLabelMappings()!=null && oleNoticeContentConfigurationBoList.get(0).getOleNoticeFieldLabelMappings().size()>0){
                for(OleNoticeFieldLabelMapping oleNoticeFieldLabelMapping : oleNoticeContentConfigurationBoList.get(0).getOleNoticeFieldLabelMappings()){
                    fieldLabelMap.put(oleNoticeFieldLabelMapping.getFieldName(),oleNoticeFieldLabelMapping.getFieldLabel());
                }
                }
            }
        }else{
            fieldLabelMap.put("noticeTitle",getTitle());
            fieldLabelMap.put("noticeBody",getBody());
        }
    }



    @Override
    public String getTitle() {
        String title = getParameterResolverInstance().getParameter(OLEConstants.APPL_ID, OLEConstants
                .DLVR_NMSPC, OLEConstants.DLVR_CMPNT,
                OLEParameterConstants.RECALL_TITLE);
        return title;
    }

    @Override
    public String getBody() {
        String body = getParameterResolverInstance().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants
                .DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEParameterConstants.RECALL_BODY);
        return body;
    }

}
