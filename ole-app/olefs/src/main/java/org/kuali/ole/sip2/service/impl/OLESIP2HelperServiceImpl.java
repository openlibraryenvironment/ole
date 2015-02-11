package org.kuali.ole.sip2.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.service.OleLoanDocumentDaoOjb;
import org.kuali.ole.ncip.bo.OLEItemFine;
import org.kuali.ole.sip2.constants.OLESIP2Constants;
import org.kuali.ole.sip2.service.OLENettyServer;
import org.kuali.ole.sip2.service.OLESIP2HelperService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.web.format.CurrencyFormatter;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sheiksalahudeenm on 12/29/14.
 */
public class OLESIP2HelperServiceImpl implements OLESIP2HelperService {

    Logger LOG = Logger.getLogger(OLESIP2HelperServiceImpl.class);

    private BusinessObjectService businessObjectService;

    private BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }


    @Override
    public OLENettyServer doActionForSocketServer(String action, StringBuffer responseString, OLENettyServer olesip2Server) {
        if (action.equalsIgnoreCase("start")) {
            olesip2Server = startOLESip2Server(responseString, olesip2Server);
        } else if (action.equalsIgnoreCase("stop")) {
            LOG.info("******** Request for stop OLE Socket server ********");
            if (olesip2Server != null) {
                olesip2Server.stop();
                responseString.append(OLESIP2Constants.SERVER_STOPPED);
                LOG.info("******** " + OLESIP2Constants.SERVER_STOPPED + " ********");
            }
        } else if (action.equalsIgnoreCase("status")) {
            LOG.info("******** Request for getting status of OLE Socket server ********");
            if (olesip2Server != null) {
                if (olesip2Server.isStopped()) {
                    responseString.append(OLESIP2Constants.SERVER_DOWN);
                    LOG.info("******** " + OLESIP2Constants.SERVER_DOWN + " ********");
                } else {
                    responseString.append(OLESIP2Constants.SERVER_RUNNING);
                    LOG.info("******** " + OLESIP2Constants.SERVER_RUNNING + " ********");
                }
            } else {
                responseString.append(OLESIP2Constants.SERVER_DOWN);
                LOG.info("******** " + OLESIP2Constants.SERVER_DOWN + " ********");

            }
        }
        return olesip2Server;
    }

    @Override
    public OLENettyServer startOLESip2Server(StringBuffer responseString, OLENettyServer olesip2Server) {
        LOG.info("******** Request for starting OLE Socket server ********");
        String portNo = ConfigContext.getCurrentContextConfig().getProperty("sip2.port");
        String serverUrl = ConfigContext.getCurrentContextConfig().getProperty("sip2.url");
        //String circulationService=parameterMap.get("circulationService")[0];
        if (StringUtils.isNotBlank(portNo) && StringUtils.isNotBlank(serverUrl)) {

            olesip2Server = new OLENettyServer(Integer.parseInt(portNo), serverUrl, "/circulationsip?service=");

            try {
                Thread thread = new Thread(olesip2Server);
                thread.start();
                try {
                    Thread.sleep(5 * 1000);
                    if (olesip2Server.getMessage() != null) {
                        responseString.append(olesip2Server.getMessage());
                    } else {
                        responseString.append(OLESIP2Constants.STARTED_SUCCESSFULLY);
                        LOG.info("******** " + OLESIP2Constants.STARTED_SUCCESSFULLY + " ********");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    responseString.append(OLESIP2Constants.PROBLEM_WITH_SERVER + "\n");
                    responseString.append("Exception: \n" + e);
                }
            } catch (Exception e) {
                e.printStackTrace();
                responseString.append(OLESIP2Constants.PROBLEM_WITH_SERVER + "\n");
                responseString.append("Exception: \n" + e);
            }

            //}

        } else {
            responseString.append(OLESIP2Constants.FAIL_TO_LOAD + "\n");
            responseString.append(OLESIP2Constants.PARAMETER_MISSING);
            LOG.info("******** " + OLESIP2Constants.FAIL_TO_LOAD + " ********");
            LOG.info("******** " + OLESIP2Constants.PARAMETER_MISSING + " ********");
        }
        return olesip2Server;

    }


    @Override
    public void stopOLESip2Server(StringBuffer responseString, OLENettyServer olesip2Server) {
        LOG.info("******** Request for stop OLE Socket server ********");
        if (olesip2Server != null) {
            olesip2Server.stop();
            responseString.append(OLESIP2Constants.SERVER_STOPPED);
            LOG.info("******** " + OLESIP2Constants.SERVER_STOPPED + " ********");
        }

    }

    @Override
    public void startOLESip2Server() {
        boolean onLoadStartup = ConfigContext.getCurrentContextConfig().getBooleanProperty("sip2.startOnLoad");
        if (onLoadStartup) {
            StringBuffer responseString = new StringBuffer();
            OLESIP2Constants.olesip2Server = startOLESip2Server(responseString, OLESIP2Constants.olesip2Server);
            LOG.info(responseString.toString());
        }

    }

    @Override
    public void stopOLESip2Server() {
        StringBuffer responseString = new StringBuffer();
        stopOLESip2Server(responseString, OLESIP2Constants.olesip2Server);
        LOG.info(responseString.toString());

    }

    @Override
    public OlePaymentStatus getPaymentStatus(String paymentStatus) {
        LOG.debug("Inside the getPaymentStatus method");
        Map statusMap = new HashMap();
        statusMap.put("paymentStatusCode", paymentStatus);
        List<OlePaymentStatus> olePaymentStatusList = (List<OlePaymentStatus>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePaymentStatus.class, statusMap);
        return olePaymentStatusList != null && olePaymentStatusList.size() > 0 ? olePaymentStatusList.get(0) : null;
    }

    @Override
    public KualiDecimal itemWiseFeePaid(List<FeeType> feeTypes, KualiDecimal paymentAmount, String paymentMethod,
                                        String fullyPaidStatus, String partiallyPaidStatus, String forgiveErrorNote,
                                        String transactionNumber, String transactionNote, String paidByUser, List<OleItemLevelBillPayment> currentSessionTransactions) {
        KualiDecimal payAmt = paymentAmount;
        LOG.debug("Inside itemWiseFeePaid");
        String operatorId = OLESIP2Constants.OPERATOR_ID;
        KualiDecimal unPaidAmount;
        for (FeeType feeType : feeTypes) {
            if (((feeType.getPaymentStatusCode().equalsIgnoreCase(OLEConstants.PAY_OUTSTANDING) || feeType.getPaymentStatusCode().equalsIgnoreCase(OLEConstants.PAY_PARTIALLY))
                    && paymentAmount.compareTo(OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE) > 0)) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("billNumber", feeType.getBillNumber());
                PatronBillPayment patronBillPayment = getBusinessObjectService().findByPrimaryKey(PatronBillPayment.class, map);
                List<FeeType> patronFeeTypes = patronBillPayment.getFeeType();
                for (FeeType patronFeeType : patronFeeTypes) {
                    if (patronFeeType.getId().equalsIgnoreCase(feeType.getId())) {
                        if (paymentAmount.compareTo(patronFeeType.getBalFeeAmount()) >= 0) {
                            unPaidAmount = patronBillPayment.getUnPaidBalance().subtract(feeType.getBalFeeAmount());
                            paymentAmount = paymentAmount.subtract(patronFeeType.getBalFeeAmount());
                            OlePaymentStatus olePaymentStatus = getPaymentStatus(fullyPaidStatus);
                            patronFeeType.setOlePaymentStatus(olePaymentStatus);
                            patronFeeType.setPaymentStatus(olePaymentStatus.getPaymentStatusId());
                            List<OleItemLevelBillPayment> oleItemLevelBillPayments = patronFeeType.getItemLevelBillPaymentList() != null && patronFeeType.getItemLevelBillPaymentList().size() > 0
                                    ? patronFeeType.getItemLevelBillPaymentList() : new ArrayList<OleItemLevelBillPayment>();
                            OleItemLevelBillPayment oleItemLevelBillPayment = new OleItemLevelBillPayment();
                            oleItemLevelBillPayment.setPaymentDate(new Timestamp(System.currentTimeMillis()));
                            oleItemLevelBillPayment.setAmount(patronFeeType.getBalFeeAmount());
                            oleItemLevelBillPayment.setCreatedUser(operatorId);
                            oleItemLevelBillPayment.setTransactionNote(transactionNote);
                            oleItemLevelBillPayment.setTransactionNumber(transactionNumber);
                            oleItemLevelBillPayment.setPaymentMode(paymentMethod);
                            oleItemLevelBillPayments.add(oleItemLevelBillPayment);
                            currentSessionTransactions.add(oleItemLevelBillPayment);
                            patronFeeType.setBalFeeAmount(OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE);
                            patronFeeType.setItemLevelBillPaymentList(oleItemLevelBillPayments);
                            if (paymentMethod != null && paymentMethod.equalsIgnoreCase(OLEConstants.ERROR)) {
                                patronFeeType.setErrorNote(forgiveErrorNote);
                            }
                            if (paymentMethod != null && paymentMethod.equalsIgnoreCase(OLEConstants.FORGIVE)) {
                                patronFeeType.setForgiveNote(forgiveErrorNote);
                            }
                            feeType.setActiveItem(false);
                            patronBillPayment.setPaymentAmount(payAmt.subtract(paymentAmount));
                            payAmt = paymentAmount;
                        } else {
                            unPaidAmount = patronBillPayment.getUnPaidBalance().subtract(paymentAmount);
                            KualiDecimal updatedFeeAmount = patronFeeType.getBalFeeAmount().subtract(paymentAmount);
                            KualiDecimal transAmount = paymentAmount;
                            paymentAmount = paymentAmount.subtract(patronFeeType.getBalFeeAmount());
                            OlePaymentStatus olePaymentStatus = getPaymentStatus(partiallyPaidStatus);
                            patronFeeType.setOlePaymentStatus(olePaymentStatus);
                            patronFeeType.setPaymentStatus(olePaymentStatus.getPaymentStatusId());
                            List<OleItemLevelBillPayment> oleItemLevelBillPayments = patronFeeType.getItemLevelBillPaymentList() != null && patronFeeType.getItemLevelBillPaymentList().size() > 0
                                    ? patronFeeType.getItemLevelBillPaymentList() : new ArrayList<OleItemLevelBillPayment>();
                            OleItemLevelBillPayment oleItemLevelBillPayment = new OleItemLevelBillPayment();
                            oleItemLevelBillPayment.setPaymentDate(new Timestamp(System.currentTimeMillis()));
                            oleItemLevelBillPayment.setAmount(transAmount.compareTo(OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE) < 0 ? transAmount : transAmount);
                            oleItemLevelBillPayment.setCreatedUser(operatorId);
                            oleItemLevelBillPayment.setTransactionNote(transactionNote);
                            oleItemLevelBillPayment.setTransactionNumber(transactionNumber);
                            oleItemLevelBillPayment.setPaymentMode(paymentMethod);
                            oleItemLevelBillPayments.add(oleItemLevelBillPayment);
                            currentSessionTransactions.add(oleItemLevelBillPayment);
                            patronFeeType.setBalFeeAmount(updatedFeeAmount);
                            patronFeeType.setItemLevelBillPaymentList(oleItemLevelBillPayments);
                            if (paymentMethod != null && paymentMethod.equalsIgnoreCase(OLEConstants.ERROR)) {
                                patronFeeType.setErrorNote(forgiveErrorNote);
                            }
                            if (paymentMethod != null && paymentMethod.equalsIgnoreCase(OLEConstants.FORGIVE)) {
                                patronFeeType.setForgiveNote(forgiveErrorNote);
                            }
                            patronBillPayment.setPaymentAmount(payAmt);
                        }
                        //feeType = patronFeeType;
                        patronBillPayment.setUnPaidBalance(unPaidAmount);
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("unPaidAmount" + patronBillPayment.getUnPaidBalance());
                        }
                        patronBillPayment.setPaymentOperatorId(OLESIP2Constants.OPERATOR_ID);
                        patronBillPayment.setPayDate(new java.sql.Date((new java.util.Date()).getTime()));
                        patronBillPayment.setPaymentMethod(paymentMethod);
                        if (patronBillPayment.getPaymentMethod().equals(OLEConstants.FORGIVE) || patronBillPayment.getPaymentMethod().equals(OLEConstants.CANCEL) || patronBillPayment.getPaymentMethod().equals(OLEConstants.ERROR)) {
                            if (patronBillPayment.getPaymentMethod().equalsIgnoreCase(OLEConstants.FORGIVE)) {
                                patronBillPayment.setFreeTextNote(CurrencyFormatter.getSymbolForCurrencyPattern() + patronBillPayment.getPaymentAmount() + OLEConstants.FORGIVE_MESSAGE);
                            }
                            if (patronBillPayment.getPaymentMethod().equalsIgnoreCase(OLEConstants.CANCEL)) {
                                patronBillPayment.setFreeTextNote(OLEConstants.CANCEL_MESSAGE_AMT + " $" + patronBillPayment.getPaymentAmount());
                            }
                            if (patronBillPayment.getPaymentMethod().equalsIgnoreCase(OLEConstants.ERROR)) {
                                patronBillPayment.setFreeTextNote(CurrencyFormatter.getSymbolForCurrencyPattern() + patronBillPayment.getPaymentAmount() + OLEConstants.ERROR_MESSAGE);
                            }
                        } else {
                            patronBillPayment.setFreeTextNote(CurrencyFormatter.getSymbolForCurrencyPattern() + patronBillPayment.getPaymentAmount() + " paid through " + patronBillPayment.getPaymentMethod());
                            patronBillPayment.setNote(null);
                        }
                        getBusinessObjectService().save(patronBillPayment);
                    }
                }
            }
        }
        return paymentAmount;
    }

    @Override
    public String feePaid(String patronBarcode, String feeTypeCode, String feeTypeId, String paymentMethod, KualiDecimal paymentAmount, String transactionNumber, String paidByUser) {
        String responseString = "";
        List<FeeType> feeTypes = new ArrayList<FeeType>();
        String patronId = "";
        Map barMap = new HashMap();
        barMap.put(OLEConstants.OlePatron.BARCODE, patronBarcode);
        List<OlePatronDocument> patronDocumentList = (List<OlePatronDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronDocument.class, barMap);
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(patronDocumentList)) {
            patronId = patronDocumentList.get(0).getOlePatronId();
        }
        if (org.apache.commons.lang.StringUtils.isNotBlank(patronId)) {
            List<PatronBillPayment> patronBills = (List<PatronBillPayment>) ((OleLoanDocumentDaoOjb) SpringContext.getBean("oleLoanDao")).getPatronBills(patronId);
            String forgiveErrorNote = "";
            String transactionNote = "";
            for (PatronBillPayment olPatronBillPayment : patronBills) {
                for (FeeType feeType : olPatronBillPayment.getFeeType()) {
                    if (org.apache.commons.lang.StringUtils.isNotBlank(feeTypeId)) {
                        if (feeType.getBillNumber().equalsIgnoreCase(feeTypeId) && feeType.getOleFeeType().getFeeTypeCode().equalsIgnoreCase(feeTypeCode)) {
                            feeTypes.add(feeType);
                        }
                    } else {
                        if (feeType.getOleFeeType().getFeeTypeCode().equalsIgnoreCase(feeTypeCode)) {
                            feeTypes.add(feeType);
                        }
                    }
                }
            }
            if (feeTypes.size() == 1) {
                List<OleItemLevelBillPayment> oleItemLevelBillPaymentList = new ArrayList<OleItemLevelBillPayment>();
                for (FeeType feeType : feeTypes) {
                    if (org.apache.commons.collections.CollectionUtils.isNotEmpty(feeType.getItemLevelBillPaymentList())) {
                        oleItemLevelBillPaymentList.addAll(feeType.getItemLevelBillPaymentList());
                    }
                }
                KualiDecimal unPaidAmount = itemWiseFeePaid(feeTypes, paymentAmount, paymentMethod, OLEConstants.FULL_PAID, OLEConstants.PAR_PAID, forgiveErrorNote,
                        transactionNumber, transactionNote, paidByUser, oleItemLevelBillPaymentList);
                LOG.info("unPaidAmount : " + unPaidAmount);
                responseString = "Successfully Paid the Amount";
            } else {
                LOG.info("More than one fee is there");
                responseString = "Having more than one fee";
            }
        }
        return responseString;
    }

    @Override
    public BigDecimal calculateTotalFineBalance(List<OLEItemFine> oleItemFineList) {
        BigDecimal balanceAmount = new BigDecimal(0);
        if (CollectionUtils.isNotEmpty(oleItemFineList)) {
            for (OLEItemFine oleItemFine : oleItemFineList) {
                balanceAmount = balanceAmount.add(oleItemFine.getBalance());
            }
        }

        return balanceAmount;
    }

}
