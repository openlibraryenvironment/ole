package org.kuali.ole.deliver.notice.executors;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.notice.bo.OleNoticeContentConfigurationBo;
import org.kuali.ole.deliver.notice.noticeFormatters.RecallRequestEmailContentFormatter;
import org.kuali.ole.deliver.notice.noticeFormatters.RequestEmailContentFormatter;
import org.kuali.ole.deliver.notice.service.impl.OleNoticeServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maheswarang on 6/26/15.
 */
public class RecallNoticesExecutor extends RequestNoticesExecutor {


    public RecallNoticesExecutor(Map requestMap) {
        super(requestMap);
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
        super.deleteNotices(filteredDeliverNotices);
    }

    @Override
    public void setOleNoticeContentConfigurationBo() {
        List<OleNoticeContentConfigurationBo> oleNoticeContentConfigurationBoList = null;
        Map<String,String> noticeConfigurationMap = new HashMap<String,String>();
        noticeConfigurationMap.put("noticeType",OLEConstants.RECALL_NOTICE);
        noticeConfigurationMap.put("noticeName", noticeContentConfigName);
        oleNoticeContentConfigurationBoList= (List<OleNoticeContentConfigurationBo>)getBusinessObjectService().findMatching(OleNoticeContentConfigurationBo.class,noticeConfigurationMap);
        if(CollectionUtils.isNotEmpty(oleNoticeContentConfigurationBoList)){
            oleNoticeContentConfigurationBo = oleNoticeContentConfigurationBoList.get(0);
        }else{
            oleNoticeContentConfigurationBo = new OleNoticeContentConfigurationBo();
            oleNoticeContentConfigurationBo.setNoticeType(getType());
            oleNoticeContentConfigurationBo.setNoticeTitle(getTitle());
            oleNoticeContentConfigurationBo.setNoticeBody(getBody());
            oleNoticeContentConfigurationBo.setNoticeFooterBody("");
            oleNoticeContentConfigurationBo.setNoticeSubjectLine(new OleNoticeServiceImpl().getNoticeSubjectForNoticeType(getType()));
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

    @Override
    public String getType() {
        return OLEConstants.RECALL_NOTICE;
    }
}
