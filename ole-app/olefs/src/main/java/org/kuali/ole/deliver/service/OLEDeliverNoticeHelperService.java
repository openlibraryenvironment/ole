package org.kuali.ole.deliver.service;

import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.bo.OleLoanDocument;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by vivekb on 9/16/14.
 */
public interface OLEDeliverNoticeHelperService {

    public List<OLEDeliverNotice> getDeliverNotices(String loanId);

    public List<OLEDeliverNotice> updateDeliverNotices(String loanId);

    public void deleteDeliverNotices(String loanId);

    public void generateDeliverNoticesUsingKRMSValues(List<OLEDeliverNotice> deliverNotices, Timestamp dueDate,
                                                      String noticeType, String noticeFormat,
                                                      String numberOfOverdueToBeSent, String intervalToGenerateNotice,
                                                      String replacementBill);

    public void generateDeliverNotices(String patronId, String itemId, String deskLocation, String borrowerType,
                                       String itemType, String itemStatus, String claimsReturned, String replacementBill,
                                       String itemShelving, String itemCollection, String itemLibrary, String itemCampus,
                                       String itemInstitution, Date itemDueDate, String loanId) throws Exception;

    public void generateDeliverNotices(OleLoanDocument oleLoanDocument) throws Exception;

    public void updateDeliverNoticeForUnprocessedLoans()throws Exception;
}
