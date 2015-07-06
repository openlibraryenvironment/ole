package org.kuali.ole.deliver.notice.executors;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.calendar.service.DateUtil;
import org.kuali.ole.deliver.notice.noticeFormatters.OnHoldRequestEmailContentFormatter;
import org.kuali.ole.deliver.notice.noticeFormatters.RequestEmailContentFormatter;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by maheswarang on 6/25/15.
 */
public class OnHoldNoticesExecutor extends RequestNoticesExecutor {


    public OnHoldNoticesExecutor(List<OLEDeliverNotice> deliverNotices) {
        super(deliverNotices);
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
            if(StringUtils.isEmpty(oleDeliverRequestBo.getOlePickUpLocation().getOnHoldDays())){
                oleDeliverRequestBo.setHoldExpirationDate(new java.sql.Date(System.currentTimeMillis()));
            }else{
                int noDays = Integer.parseInt(oleDeliverRequestBo.getOlePickUpLocation().getOnHoldDays());
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, noDays);
                Date date = calendar.getTime();
                oleDeliverRequestBo.setHoldExpirationDate(new java.sql.Date(date.getTime()));
            }
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




}
