package org.kuali.ole.deliver.notice.executors;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.notice.bo.OleNoticeContentConfigurationBo;
import org.kuali.ole.deliver.notice.noticeFormatters.RequestEmailContentFormatter;
import org.kuali.ole.deliver.notice.noticeFormatters.RequestExpirationEmailContentFormatter;
import org.kuali.ole.deliver.notice.service.impl.OleNoticeServiceImpl;
import org.kuali.ole.deliver.service.OleDeliverRequestDocumentHelperServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by govindarajank on 2/3/17.
 */
public class HoldCourtesyNoticeExecutor extends RequestNoticesExecutor {

    public HoldCourtesyNoticeExecutor(Map requestMap) {
        super(requestMap);
    }

    public OleDeliverRequestDocumentHelperServiceImpl getOleDeliverRequestHelperService(){
        return new OleDeliverRequestDocumentHelperServiceImpl();
    }

    @Override
    public RequestEmailContentFormatter getRequestEmailContentFormatter() {
        if(requestEmailContentFormatter == null){
            requestEmailContentFormatter =  new RequestExpirationEmailContentFormatter();
        }return requestEmailContentFormatter;
    }

    @Override
    public boolean isValidRequestToSendNotice(OleDeliverRequestBo oleDeliverRequestBo) {
        return true;
    }

    @Override
    protected void postProcess() {
        getBusinessObjectService().save(deliverRequestBos);
        super.deleteNotices(filteredDeliverNotices);
        //To do create the request history record and delete the record from the request table
    }

    @Override
    public void setOleNoticeContentConfigurationBo() {
        List<OleNoticeContentConfigurationBo> oleNoticeContentConfigurationBoList = null;
        Map<String,String> noticeConfigurationMap = new HashMap<String,String>();
        noticeConfigurationMap.put("noticeType", OLEConstants.ONHOLD_COURTESY_NOTICE);
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
                OLEParameterConstants
                        .ONHOLD_COURTESY_TITLE);
        return title;
    }

    @Override
    public String getBody() {
        String body = getParameterResolverInstance().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants
                .DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.OleDeliverRequest.ONHOLD_COURTESY_NOTICE_CONTENT);
        return body;
    }

    @Override
    public String getType() {
        return OLEConstants.ONHOLD_COURTESY_NOTICE;
    }
}
