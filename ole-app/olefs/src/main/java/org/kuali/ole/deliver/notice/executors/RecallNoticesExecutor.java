package org.kuali.ole.deliver.notice.executors;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.notice.noticeFormatters.RecallRequestEmailContentFormatter;
import org.kuali.ole.deliver.notice.noticeFormatters.RequestEmailContentFormatter;

import java.util.List;

/**
 * Created by maheswarang on 6/26/15.
 */
public class RecallNoticesExecutor extends RequestNoticesExecutor {
    public RecallNoticesExecutor(List<OleDeliverRequestBo> oleDeliverRequestBos) {
        super(oleDeliverRequestBos);
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
    protected void postProcess() {

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
