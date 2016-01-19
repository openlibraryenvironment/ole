package org.kuali.ole.deliver.service;

import org.kuali.ole.deliver.bo.OLEDeliverNoticeHistory;

/**
 * Created by maheswarang on 1/18/16.
 */
public interface OlePatronLoanNoticeService {

    public boolean sendMail(String toMailAddress,String mailContent);

    public OLEDeliverNoticeHistory cloneOleDeliverNoticeHistory(OLEDeliverNoticeHistory oleDeliverNoticeHistory);
}
