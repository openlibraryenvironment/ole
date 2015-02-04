package org.kuali.ole.service;

import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.FeeType;
import org.kuali.ole.deliver.bo.PatronBillPayment;
import org.kuali.ole.deliver.bo.OlePatronDocument;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bala.km
 * Date: 7/2/12
 * Time: 1:14 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OleCirculationPolicyService {

    public boolean isValidBarcode(String barcode, String pattern);
    public Date getPatronMembershipExpireDate(String patronBarcode);
    public int getNoOfItemsLoaned(String patronBarcode, boolean renewalFlag);
    public Timestamp calculateLoanDueDate(String loanPeriod);
    public  List<FeeType>  getPatronBillPayment(String patronId);
    public HashMap getNumberOfOverdueItemsCheckedOut(String patronId);
    public List<Integer> getNumberOfOverdueDays(String patronId);
    public int getNumberOfClaimsReturned(String patronBarcode);
    public Integer getHoursDiff(Date dateOne, Date dateTwo);
    public List<OlePatronDocument> isProxyPatron(String patronId) throws Exception;
    public boolean isAddressVerified(String patronId) throws Exception;
    public HashMap getRecalledOverdueItemsCheckedOut(List<OleLoanDocument> oleLoanDocuments);
    public List<OlePatronDocument> isProxyPatron(OlePatronDocument olePatronDocument) throws Exception;
    public boolean isAddressVerified(OlePatronDocument olePatronDocument,String patronId) throws Exception;
    public HashMap getLoanedKeyMap(String patronId,boolean renewalFlag);
}
