package org.kuali.ole.deliver.service;

import org.kuali.ole.deliver.batch.OleNoticeBo;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.service.NoticeMailContentFormatter;

/**
 * Created by Palanivelrajanb on 11/13/2015.
 */
public class MissingPieceEmailContentFormatter extends NoticeMailContentFormatter {
    @Override
    protected void processCustomNoticeInfo(OleLoanDocument oleLoanDocument, OleNoticeBo oleNoticeBo) {
        oleNoticeBo.setOleCirculationDesk(oleLoanDocument.getOleCirculationDesk());
    }
}
