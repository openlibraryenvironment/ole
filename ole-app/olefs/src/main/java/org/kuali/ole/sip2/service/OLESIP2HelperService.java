package org.kuali.ole.sip2.service;

import org.kuali.ole.deliver.bo.FeeType;
import org.kuali.ole.deliver.bo.OleItemLevelBillPayment;
import org.kuali.ole.deliver.bo.OlePaymentStatus;
import org.kuali.ole.bo.OLEItemFine;
import org.kuali.rice.core.api.util.type.KualiDecimal;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by sheiksalahudeenm on 12/30/14.
 */
public interface OLESIP2HelperService {

    public OLENettyServer doActionForSocketServer(String action, StringBuffer responseString, OLENettyServer olesip2Server);

    public OLENettyServer startOLESip2Server(StringBuffer responseString, OLENettyServer olesip2Server);

    public void stopOLESip2Server(StringBuffer responseString, OLENettyServer olesip2Server);

    public void startOLESip2Server();

    public void stopOLESip2Server();

    public OlePaymentStatus getPaymentStatus(String paymentStatus);

    public KualiDecimal itemWiseFeePaid(List<FeeType> feeTypes, KualiDecimal paymentAmount, String paymentMethod, String fullyPaidStatus, String partiallyPaidStatus, String forgiveErrorNote, String transactionNumber, String transactionNote, String paidByUser, List<OleItemLevelBillPayment> currentSessionTransactions);

    public String feePaid(String patronBarcode, String feeTypeCode, String feeTypeId, String paymentMethod, KualiDecimal paymentAmount, String transactionNumber, String paidByUser);

    public BigDecimal calculateTotalFineBalance(List<OLEItemFine> oleItemFineList);
}
