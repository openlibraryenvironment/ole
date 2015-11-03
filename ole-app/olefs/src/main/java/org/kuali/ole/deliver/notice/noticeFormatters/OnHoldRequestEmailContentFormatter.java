package org.kuali.ole.deliver.notice.noticeFormatters;

import org.kuali.ole.deliver.batch.OleNoticeBo;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OlePatronDocument;

import java.util.Map;

/**
 * Created by maheswarang on 6/25/15.
 */
public class OnHoldRequestEmailContentFormatter extends RequestEmailContentFormatter {

    @Override
    protected void processCustomNoticeInfo(OleDeliverRequestBo oleDeliverRequestBo, OleNoticeBo oleNoticeBo) {

    }

    @Override
    protected OlePatronDocument getOlePatron(OleDeliverRequestBo oleDeliverRequestBo) {
        return oleDeliverRequestBo.getOlePatron();
    }
}
