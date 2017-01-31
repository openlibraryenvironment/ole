package org.kuali.ole.deliver.notice.executors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.notice.bo.OleNoticeContentConfigurationBo;
import org.kuali.ole.deliver.notice.noticeFormatters.OnHoldRequestEmailContentFormatter;
import org.kuali.ole.deliver.notice.noticeFormatters.RequestEmailContentFormatter;
import org.kuali.ole.deliver.notice.service.impl.OleNoticeServiceImpl;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by maheswarang on 6/25/15.
 */
public class OnHoldNoticesExecutor extends RequestNoticesExecutor {


    public OnHoldNoticesExecutor(Map requestMap) {
        super(requestMap);
    }

    @Override
    public RequestEmailContentFormatter getRequestEmailContentFormatter() {
        if(requestEmailContentFormatter ==null){
            requestEmailContentFormatter =  new OnHoldRequestEmailContentFormatter();
        }
        return requestEmailContentFormatter;
    }

    @Override
    public boolean isValidRequestToSendNotice(OleDeliverRequestBo oleDeliverRequestBo) {
        String onHoldItemStatusParameter = getNoticeUtil().getParameter(OLEConstants.ON_HOLD_NOTICE_ITEM_STATUS);
        Map<String, String> itemStatuses = new HashMap<String, String>();
        if (onHoldItemStatusParameter != null && !onHoldItemStatusParameter.trim().isEmpty()) {
            String[] itemStatus = onHoldItemStatusParameter.split(";");
            itemStatuses = getNoticeUtil().getMap(itemStatus);
        }
        if(itemStatuses.containsKey(oleDeliverRequestBo.getItemStatus())){
            oleDeliverRequestBo.setOnHoldNoticeSentDate(new java.sql.Date(System.currentTimeMillis()));
            return true;
        }
        return false;
    }

    @Override
    protected void postProcess() {
      getBusinessObjectService().save(deliverRequestBos);
        super.deleteNotices(filteredDeliverNotices);
    }

    @Override
    public void setOleNoticeContentConfigurationBo() {
        List<OleNoticeContentConfigurationBo> oleNoticeContentConfigurationBoList = null;
        Map<String,String> noticeConfigurationMap = new HashMap<String,String>();
        noticeConfigurationMap.put("noticeType",OLEConstants.ONHOLD_NOTICE);
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
    public void deleteNotices(List<OLEDeliverNotice> oleDeliverNotices) {

    }

    @Override
    public String getTitle() {
        String title = getParameterResolverInstance().getParameter(OLEConstants.APPL_ID, OLEConstants
                .DLVR_NMSPC, OLEConstants.DLVR_CMPNT,
                OLEParameterConstants.ONHOLD_TITLE);
        return title;
    }

    @Override
    public String getBody() {
        String body = getParameterResolverInstance().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants
                .DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEParameterConstants.ONHOLD_BODY);
        return body;
    }

    @Override
    public String getType() {
        return OLEConstants.ONHOLD_NOTICE;
    }
}
