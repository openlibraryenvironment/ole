package org.kuali.ole.deliver.notice.noticeFormatters;

import org.kuali.ole.deliver.batch.OleNoticeBo;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OlePatronDocument;

public class CancelRequestEmailContentFormatter extends RequestEmailContentFormatter {


    @Override
    protected void processCustomNoticeInfo(OleDeliverRequestBo oleDeliverRequestBo, OleNoticeBo oleNoticeBo) {

    }

    @Override
    protected OlePatronDocument getOlePatron(OleDeliverRequestBo oleDeliverRequestBo) {
        return oleDeliverRequestBo.getOlePatron();
    }
}
