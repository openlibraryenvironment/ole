package org.kuali.ole.deliver.notice.executors;

import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
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
        return null;
    }

    @Override
    protected void postProcess() {

    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getBody() {
        return null;
    }
}
