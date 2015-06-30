package org.kuali.ole.deliver.notice.executors;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.notice.noticeFormatters.RequestEmailContentFormatter;
import org.kuali.ole.deliver.notice.noticeFormatters.RequestExpirationEmailContentFormatter;

import java.util.List;

/**
 * Created by maheswarang on 6/23/15.
 */
public class RequestExpirationNoticesExecutor extends RequestNoticesExecutor{


    public RequestExpirationNoticesExecutor(List<OleDeliverRequestBo> oleDeliverRequestBos) {
       super(oleDeliverRequestBos);
    }

    @Override
    public RequestEmailContentFormatter getRequestEmailContentFormatter() {
        if(requestEmailContentFormatter == null){
        requestEmailContentFormatter = new RequestExpirationEmailContentFormatter();
        }
        return requestEmailContentFormatter;
    }

    @Override
    protected void postProcess() {

    }


    @Override
    public String getTitle() {
        String title = getParameterResolverInstance().getParameter(OLEConstants.APPL_ID, OLEConstants
                .DLVR_NMSPC, OLEConstants.DLVR_CMPNT,
                OLEParameterConstants.EXP_REQ_TITLE);
        return title;
    }

    @Override
    public String getBody() {
        String body = getParameterResolverInstance().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants
                .DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEParameterConstants.EXP_REQ_BODY);
        return body;
    }




}
