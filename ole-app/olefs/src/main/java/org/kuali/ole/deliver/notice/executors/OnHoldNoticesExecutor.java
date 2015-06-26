package org.kuali.ole.deliver.notice.executors;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.notice.noticeFormatters.OnHoldRequestEmailContentFormatter;
import org.kuali.ole.deliver.notice.noticeFormatters.RequestEmailContentFormatter;

import java.util.List;

/**
 * Created by maheswarang on 6/25/15.
 */
public class OnHoldNoticesExecutor extends RequestNoticesExecutor {

    public OnHoldNoticesExecutor(List<OleDeliverRequestBo> oleDeliverRequestBos) {
        super(oleDeliverRequestBos);
    }

    @Override
    public RequestEmailContentFormatter getRequestEmailContentFormatter() {
            return new OnHoldRequestEmailContentFormatter();
    }

    @Override
    protected void postProcess() {
      getBusinessObjectService().save(deliverRequestBos);
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
