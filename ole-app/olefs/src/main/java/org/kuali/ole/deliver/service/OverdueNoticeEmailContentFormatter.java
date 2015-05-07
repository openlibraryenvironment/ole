package org.kuali.ole.deliver.service;

import org.kuali.ole.deliver.bo.OleLoanDocument;

/**
 * Created by pvsubrah on 4/8/15.
 */
public class OverdueNoticeEmailContentFormatter extends NoticeMailContentFormatter {


    @Override
    protected String generateCustomHTML(OleLoanDocument oleLoanDocument) {
        return null;
    }
}
