package org.kuali.ole.deliver.service;

import org.kuali.ole.deliver.bo.OleLoanDocument;

/**
 * Created by maheswarang on 4/8/15.
 */
public class CourtesyNoticeEmailContentFormatter extends NoticeMailContentFormatter {
    @Override
    protected String generateCustomHTML(OleLoanDocument oleLoanDocument) {
        return null;
    }
}
