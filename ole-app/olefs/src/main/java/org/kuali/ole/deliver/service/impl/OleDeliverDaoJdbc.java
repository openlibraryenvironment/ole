package org.kuali.ole.deliver.service.impl;

import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.bo.OLECheckedOutItem;
import org.kuali.ole.bo.OLEHold;
import org.kuali.ole.bo.OLEItemFine;
import org.kuali.rice.core.framework.persistence.jdbc.dao.PlatformAwareDaoBaseJdbc;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by maheswarang on 10/20/14.
 */
public class OleDeliverDaoJdbc extends PlatformAwareDaoBaseJdbc {
    private static final Logger LOG = Logger.getLogger(OleDeliverDaoJdbc.class);
    private BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
    private Map<String, OleCirculationDesk> oleCirculationDeskMap;
    private Map<String, OleDeliverRequestType> oleDeliverRequestTypeMap;

    public Map<String, OleCirculationDesk> getOleCirculationDeskMap() {
        return oleCirculationDeskMap;
    }

    public void setOleCirculationDeskMap(Map<String, OleCirculationDesk> oleCirculationDeskMap) {
        this.oleCirculationDeskMap = oleCirculationDeskMap;
    }

    public Map<String, OleDeliverRequestType> getOleDeliverRequestTypeMap() {
        return oleDeliverRequestTypeMap;
    }

    public void setOleDeliverRequestTypeMap(Map<String, OleDeliverRequestType> oleDeliverRequestTypeMap) {
        this.oleDeliverRequestTypeMap = oleDeliverRequestTypeMap;
    }

    /**
     * @return
     */
    public Map<String, OleCirculationDesk> getAvailableCirculationDesks() {
        if (this.oleCirculationDeskMap == null) {
            Map<String, OleCirculationDesk> circulationDeskMap = new HashMap<String, OleCirculationDesk>();
            List<OleCirculationDesk> oleCirculationDeskList = (List<OleCirculationDesk>) businessObjectService.findAll(OleCirculationDesk.class);
            if (oleCirculationDeskList != null && oleCirculationDeskList.size() > 0) {
                for (OleCirculationDesk oleCirculationDesk : oleCirculationDeskList) {
                    circulationDeskMap.put(oleCirculationDesk.getCirculationDeskId(), oleCirculationDesk);

                }
            }
            this.setOleCirculationDeskMap(circulationDeskMap);
        }
        return oleCirculationDeskMap;
    }

    public Map<String, OleDeliverRequestType> getAvailableRequestTypes() {
        if (this.oleDeliverRequestTypeMap == null) {
            Map<String, OleDeliverRequestType> requestTypeMap = new HashMap<String, OleDeliverRequestType>();
            List<OleDeliverRequestType> oleDeliverRequestTypeList = (List<OleDeliverRequestType>) businessObjectService.findAll(OleDeliverRequestType.class);
            if (oleDeliverRequestTypeList != null && oleDeliverRequestTypeList.size() > 0) {
                for (OleDeliverRequestType oleDeliverRequestType : oleDeliverRequestTypeList) {
                    requestTypeMap.put(oleDeliverRequestType.getRequestTypeId(), oleDeliverRequestType);
                }
            }
            this.setOleDeliverRequestTypeMap(requestTypeMap);
        }
        return oleDeliverRequestTypeMap;
    }


    /**
     * This method is used to get the loan information to the response object
     *
     * @param oleLoanDocuments
     * @return
     */
    public List<OLECheckedOutItem> getCheckedOutItemsList(List<OleLoanDocument> oleLoanDocuments) {
        LOG.info("inside the getCheckedOutItemsList for processing ");
        SimpleDateFormat fmt = new SimpleDateFormat(OLEConstants.OleDeliverRequest.DATE_FORMAT);
        List<OLECheckedOutItem> oleCheckedOutItemList = new ArrayList<OLECheckedOutItem>();

        if (oleLoanDocuments != null && oleLoanDocuments.size() > 0) {
            OLECheckedOutItem oleCheckedOutItem = null;
            for (OleLoanDocument oleLoanDocument : oleLoanDocuments) {
                oleCheckedOutItem = new OLECheckedOutItem();
                try {
                    oleCheckedOutItem.setAcquiredFine("");
                    oleCheckedOutItem.setDateRecalled("");
                    try {
                        if (oleLoanDocument.getLoanDueDate() != null) {
                            oleCheckedOutItem.setDueDate(oleLoanDocument.getLoanDueDate().toString());
                            if ((fmt.format(oleLoanDocument.getLoanDueDate())).compareTo(fmt.format(new Date(System.currentTimeMillis()))) > 0) {
                                oleCheckedOutItem.setOverDue(false);
                            } else {
                                oleCheckedOutItem.setOverDue(true);
                            }
                        } else {
                            oleCheckedOutItem.setDueDate((new Timestamp(new Date(2025, 1, 1).getTime()).toString()));
                        }
                    } catch (Exception e) {
                        LOG.info("Exception occured while setting the due date " + oleLoanDocument.getLoanDueDate() + "Reason : " + e.getMessage());
                        LOG.error(e, e);
                    }
                    if (oleLoanDocument.getRenewalLoanDueDate() != null) {
                        oleCheckedOutItem.setDateRenewed(oleLoanDocument.getRenewalLoanDueDate().toString());
                    } else {
                        oleCheckedOutItem.setDateRenewed("");
                    }
                    if (null != oleLoanDocument.getCreateDate()) {
                        oleCheckedOutItem.setLoanDate(new Timestamp(oleLoanDocument.getCreateDate().getTime()).toString());
                    }
                    oleCheckedOutItem.setItemId(oleLoanDocument.getItemId());
                    if (oleLoanDocument.getNoOfOverdueNoticesSentForBorrower() != null) {
                        oleCheckedOutItem.setNumberOfOverdueSent(oleLoanDocument.getNoOfOverdueNoticesSentForBorrower());
                    } else {
                        oleCheckedOutItem.setNumberOfOverdueSent("1");
                    }

                    oleCheckedOutItem = setBibItemInformation(oleCheckedOutItem, oleLoanDocument.getItemId());
                    oleCheckedOutItemList.add(oleCheckedOutItem);
                } catch (Exception e) {
                    LOG.info("Exception occured while processing the loan document .Patron Barcode " + oleLoanDocument.getPatronBarcode() + "Item Barcode : " + oleLoanDocument.getItemId() + "Reason : " + e.getMessage());
                    LOG.error(e, e);
                }
            }
        }

        return oleCheckedOutItemList;
    }

    public String getParameter(String name) {
        ParameterKey parameterKey = ParameterKey.create(OLEConstants.APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, name);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        if (parameter == null) {
            parameterKey = ParameterKey.create(OLEConstants.APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, name);
            parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        }
        return parameter != null ? parameter.getValue() : null;
    }

    /**
     * This method is used the item and bib information to the check out response object
     *
     * @param oleCheckedOutItem
     * @param itemBarcode
     * @return
     */
    public OLECheckedOutItem setBibItemInformation(OLECheckedOutItem oleCheckedOutItem, String itemBarcode) {
        LOG.info("Inside setBibItemInformation method for setting the item information to the loan document for the barcode : " + itemBarcode);
        try {
            if (itemBarcode != null) {
                String query = getParameter("NCIP_BIB_ITEM_INFORMATION");
                List<Map<String, Object>> itemData = null;
                LOG.info(query);
                try {
                    itemData = getSimpleJdbcTemplate().queryForList(query, itemBarcode);
                } catch (Exception e) {
                    LOG.info("Exception occured while executing the query ...... " + query);
                }
                if (itemData != null && itemData.size() > 0) {
                    for (Map<String, Object> dataMap : itemData) {
                        if (dataMap.get("ITEM_CALL_NUMBER") != null) {
                            oleCheckedOutItem.setCallNumber(dataMap.get("ITEM_CALL_NUMBER").toString());
                        } else if (dataMap.get("HOLDINGS_CALL_NUMBER") != null) {
                            oleCheckedOutItem.setCallNumber(dataMap.get("HOLDINGS_CALL_NUMBER").toString());
                        }


                        if (dataMap.get("ITEM_COPY_NUMBER") != null) {
                            oleCheckedOutItem.setCopyNumber(dataMap.get("ITEM_COPY_NUMBER").toString());
                        } else if (dataMap.get("HOLDINGS_COPY_NUMBER") != null) {
                            oleCheckedOutItem.setCopyNumber(dataMap.get("HOLDINGS_COPY_NUMBER").toString());
                        }


                        if (dataMap.get("ITEM_LOCATION") != null) {
                            oleCheckedOutItem.setLocation(dataMap.get("ITEM_LOCATION").toString());
                        } else if (dataMap.get("HOLDINGS_LOCATION") != null) {
                            oleCheckedOutItem.setLocation(dataMap.get("HOLDINGS_LOCATION").toString());
                        }

                        if (dataMap.get("TITLE") != null) {
                            oleCheckedOutItem.setTitle(dataMap.get("TITLE").toString());
                        }
                        if (dataMap.get("AUTHOR") != null) {
                            oleCheckedOutItem.setAuthor(dataMap.get("AUTHOR").toString());
                        }

                        if (dataMap.get("ITEM_VOLUME_NUMBER") != null) {
                            oleCheckedOutItem.setVolumeNumber(dataMap.get("ITEM_VOLUME_NUMBER").toString());
                        }

                        if (dataMap.get("ITEM_TYPE_ID") != null) {
                            oleCheckedOutItem.setItemType(dataMap.get("ITEM_TYPE_ID").toString());
                        }

                    }
                }
            }
        } catch (Exception e) {
            LOG.info("Exception occured while setting the item info to the loan documents " + e.getMessage());
            LOG.error(e, e);
        }

        return oleCheckedOutItem;


    }


    /**
     * This method is used to set the hold response object with the request object list
     *
     * @param oleDeliverRequestBoList
     * @return
     * @throws Exception
     */
    public List<OLEHold> getHoldRecordsList(List<OleDeliverRequestBo> oleDeliverRequestBoList) throws Exception {
        List<OLEHold> oleHoldList = new ArrayList<OLEHold>();
        LOG.info("Inside the getHoldRecordsList ");
        String[] availableDates;
        try {
            if (oleDeliverRequestBoList != null && oleDeliverRequestBoList.size() > 0) {
                for (OleDeliverRequestBo oleDeliverRequestBo : oleDeliverRequestBoList) {
                    OLEHold oleHold = new OLEHold();
                    oleHold.setItemId(oleDeliverRequestBo.getItemId());
                    if (oleDeliverRequestBo.getRequestTypeId() != null && !oleDeliverRequestBo.getRequestTypeId().isEmpty()) {
                        if (oleDeliverRequestBo.getRequestTypeId().equals("1") || oleDeliverRequestBo.getRequestTypeId().equals("2")) {
                            oleHold.setRecallStatus(OLEConstants.YES);
                        } else {
                            oleHold.setRecallStatus(OLEConstants.NO);
                        }
                    }
                    if (oleDeliverRequestBo.getRequestExpiryDate() != null) {
                        oleHold.setRequestExpiryDate(oleDeliverRequestBo.getRequestExpiryDate().toString());
                    }
                    if (oleDeliverRequestBo.getHoldExpirationDate() != null) {
                        oleHold.setHoldExpiryDate(oleDeliverRequestBo.getHoldExpirationDate().toString());
                    }
                    if (oleDeliverRequestBo.getCreateDate() != null) {
                        oleHold.setCreateDate(oleDeliverRequestBo.getCreateDate().toString());
                    }
                    if (oleDeliverRequestBo.getBorrowerQueuePosition() != null) {
                        oleHold.setPriority(oleDeliverRequestBo.getBorrowerQueuePosition().toString());
                    }

                    if (oleDeliverRequestBo.getRecallDueDate() != null) {
                        oleHold.setDateRecalled(oleDeliverRequestBo.getRecallDueDate().toString());
                    }
                    if (oleDeliverRequestBo.getRecallDueDate() != null) {
                        oleHold.setDateRecalled(oleDeliverRequestBo.getRecallDueDate().toString());
                    }
                    if (getAvailableRequestTypes() != null && getAvailableRequestTypes().size() > 0) {
                        if (getAvailableRequestTypes().get(oleDeliverRequestBo.getRequestTypeId()) != null) {
                            oleHold.setRequestType(getAvailableRequestTypes().get(oleDeliverRequestBo.getRequestTypeId()).getRequestTypeCode());
                        }
                    }
                    Map<String, String> loanMap = new HashMap<String, String>();
                    loanMap.put(OLEConstants.OleDeliverRequest.ITEM_ID, oleDeliverRequestBo.getItemId());
                    List<OleLoanDocument> oleLoanDocumentList = (List<OleLoanDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OleLoanDocument.class, loanMap);
                    if (oleLoanDocumentList.size() > 0) {
                        if (oleLoanDocumentList.get(0).getLoanDueDate() != null) {
                            availableDates = oleLoanDocumentList.get(0).getLoanDueDate().toString().split(" ");
                            if (availableDates != null && availableDates.length > 0) {
                                oleHold.setAvailableDate(availableDates[0]);
                            } else {
                                oleHold.setAvailableDate(oleLoanDocumentList.get(0).getLoanDueDate().toString());
                            }
                            if (oleDeliverRequestBo.getPickUpLocationId() != null) {
                                if (getAvailableCirculationDesks().size() > 0 && getAvailableCirculationDesks().get(oleDeliverRequestBo.getPickUpLocationId()) != null) {
                                    oleHold.setDateAvailableExpires(addDate(new java.sql.Date(oleLoanDocumentList.get(0).getLoanDueDate().getTime()), Integer.parseInt(getAvailableCirculationDesks().get(oleDeliverRequestBo.getPickUpLocationId()).getOnHoldDays())).toString());
                                    oleHold.setPickupLocation(getAvailableCirculationDesks().get(oleDeliverRequestBo.getPickUpLocationId()).getCirculationDeskCode());
                                }
                            }
                        } else {
                            oleHold.setAvailableDate(OLEConstants.INDEFINITE);
                            oleHold.setDateAvailableExpires(OLEConstants.INDEFINITE);
                        }
                    }
                    if (oleDeliverRequestBo.getRequestTypeId().equals("2") || oleDeliverRequestBo.getRequestTypeId().equals("4") || oleDeliverRequestBo.getRequestTypeId().equals("6")) {
                        oleHold.setReserve(true);
                    } else {
                        oleHold.setReserve(false);
                    }
                    oleHold = setBibItemInformation(oleHold, oleDeliverRequestBo.getItemId());
                    oleHoldList.add(oleHold);
                }
            }
        } catch (Exception e) {
            LOG.info("Exception occured while setting the bib and item info to the holds . Reason" + e.getMessage());
            LOG.error(e, e);
        }

        return oleHoldList;
    }


    /**
     * This method is used to set the the bib and item informations to the response hold object
     *
     * @param oleHold
     * @param itemBarcode
     * @return
     */
    public OLEHold setBibItemInformation(OLEHold oleHold, String itemBarcode) {
        LOG.info("Inside setBibItemInformation for the hold records for the item Barcode :" + itemBarcode);
        try {
            if (itemBarcode != null) {
                String query = getParameter("NCIP_BIB_ITEM_INFORMATION_HOLD");
                List<Map<String, Object>> itemData = null;
                try {

                    itemData = getSimpleJdbcTemplate().queryForList(query, itemBarcode);
                } catch (Exception e) {
                    LOG.info("Exception occured while executing the query ........" + query);
                }
                if (itemData != null && itemData.size() > 0) {
                    for (Map<String, Object> dateMap : itemData) {
                        if (dateMap.get("ITEM_CALL_NUMBER") != null) {
                            oleHold.setCallNumber(dateMap.get("ITEM_CALL_NUMBER").toString());
                        } else if (dateMap.get("HOLDINGS_CALL_NUMBER") != null) {
                            oleHold.setCallNumber(dateMap.get("HOLDINGS_CALL_NUMBER").toString());
                        }

                        if (dateMap.get("ITEM_COPY_NUMBER") != null) {
                            oleHold.setCopyNumber(dateMap.get("ITEM_COPY_NUMBER").toString());
                        } else if (dateMap.get("HOLDINGS_COPY_NUMBER") != null) {
                            oleHold.setCopyNumber(dateMap.get("HOLDINGS_COPY_NUMBER").toString());
                        }

                        if (dateMap.get("TITLE") != null) {
                            oleHold.setTitle(dateMap.get("TITLE").toString());
                        }
                        if (dateMap.get("AUTHOR") != null) {
                            oleHold.setAuthor(dateMap.get("AUTHOR").toString());
                        }

                        if (dateMap.get("ITEM_VOLUME_NUMBER") != null) {
                            oleHold.setVolumeNumber(dateMap.get("ITEM_VOLUME_NUMBER").toString());
                        }

                        if (dateMap.get("ITEM_TYPE_ID") != null) {
                            oleHold.setItemType(dateMap.get("ITEM_TYPE_ID").toString());
                        }
                        if (dateMap.get("ITEM_STATUS_ID") != null) {
                            oleHold.setAvailableStatus(dateMap.get("ITEM_STATUS_ID").toString());
                        }

                    }
                }
            }
        } catch (Exception e) {
            LOG.info("Exception occured while setting the setting the item information to the hold records " + e.getMessage());
            LOG.error(e, e);
        }
        return oleHold;

    }


    /**
     * This method is used to set the fine information to the fine response object
     *
     * @param olePatronBillDocumentList
     * @return
     * @throws Exception
     */
    public List<OLEItemFine> getFineItemLists(List<PatronBillPayment> olePatronBillDocumentList) throws Exception {
        List<OLEItemFine> oleItemFineList = new ArrayList<OLEItemFine>();
        //Fee
        try {
            for (PatronBillPayment olePatronBillPayment : olePatronBillDocumentList) {
                //to do
                List<FeeType> feeTypeList = olePatronBillPayment.getFeeType();
                for (FeeType feeType : feeTypeList) {
                    OLEItemFine oleItemFine = new OLEItemFine();
                    oleItemFine.setAmount((feeType.getFeeAmount() != null ? feeType.getFeeAmount().bigDecimalValue() : OLEConstants.BIGDECIMAL_DEF_VALUE));
                    oleItemFine.setBalance((feeType.getBalFeeAmount() != null ? feeType.getBalFeeAmount().bigDecimalValue() : OLEConstants.BIGDECIMAL_DEF_VALUE));
                    oleItemFine.setBillDate(feeType.getBillDate().toString());
                    int noOfPayment = feeType.getItemLevelBillPaymentList().size();
                    oleItemFine.setNoOfPayments(new Integer(noOfPayment).toString());
                    if (feeType.getOleFeeType() != null) {
                        oleItemFine.setReason(feeType.getOleFeeType().getFeeTypeName());
                        oleItemFine.setFeeType(feeType.getOleFeeType().getFeeTypeCode());
                    } else {
                        oleItemFine.setReason(feeType.getFeeType());
                        oleItemFine.setFeeType(feeType.getFeeType());
                    }
                    oleItemFine.setDateCharged(feeType.getBillDate().toString());
                    oleItemFine = setBibItemInformation(oleItemFine, feeType.getItemBarcode());
                    oleItemFineList.add(oleItemFine);
                }
            }
        } catch (Exception e) {
            LOG.info("Exception occured while setting the item information to the fine records Reason : " + e.getMessage());
            LOG.error(e, e);
        }

        return oleItemFineList;
    }

    /**
     * This method is used to set the title informations to the item response object
     *
     * @param oleItemFine
     * @param itemBarcode
     * @return
     */
    public OLEItemFine setBibItemInformation(OLEItemFine oleItemFine, String itemBarcode) {
        LOG.info("Inside the setBibItemInformation fro setting the bib information for the fine record . Item Barcode :  " + itemBarcode);
        try {
            if (itemBarcode != null) {
                List<Map<String, Object>> itemData = null;
                String query = getParameter("NCIP_BIB_ITEM_INFORMATION_FINE");
                itemData = getSimpleJdbcTemplate().queryForList(query, itemBarcode);

                if (itemData != null && itemData.size() > 0) {
                    for (Map<String, Object> dateMap : itemData) {

                        if (dateMap.get("TITLE") != null) {
                            oleItemFine.setTitle(dateMap.get("TITLE").toString());
                        }
                        if (dateMap.get("AUTHOR") != null) {
                            oleItemFine.setAuthor(dateMap.get("AUTHOR").toString());
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.info("Exception occured while setting the information to the Fine record for the item barcode : " + itemBarcode + "Reason : " + e.getMessage());
            LOG.error(e, e);

        }
        return oleItemFine;

    }


    private java.sql.Date addDate(java.sql.Date in, int daysToAdd) {
        if (in == null) {
            return null;
        }
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(in);
        cal.add(Calendar.DAY_OF_MONTH, daysToAdd);
        return new java.sql.Date(cal.getTime().getTime());
    }


    /**
     * This method is used to get the patron record based on the input patron barcode
     *
     * @param patronBarcode
     * @return
     */
    public OlePatronDocument getPatronDocument(String patronBarcode) {
        LOG.info("Inside the getPatronDocument for getting the patron informations for the patron barcode " + patronBarcode);

        List<Map<String, Object>> patronData = null;
        OlePatronDocument olePatronDocument = new OlePatronDocument();
        try {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("select OLE_PTRN_ID , GENERAL_BLOCK, PAGING_PRIVILEGE,COURTESY_NOTICE,DELIVERY_PRIVILEGE, BORR_TYP,ACTV_IND,ACTIVATION_DATE,EXPIRATION_DATE from OLE_PTRN_T where BARCODE='" + patronBarcode + "'");
            try {
                patronData = getSimpleJdbcTemplate().queryForList(stringBuffer.toString());

            } catch (Exception e) {
                LOG.info("Exception occured while executing the query ...." + stringBuffer.toString());
            }
            if (patronData != null && patronData.size() > 0) {
                for (Map<String, Object> dateMap : patronData) {

                    if (dateMap.get("OLE_PTRN_ID") != null) {
                        olePatronDocument.setOlePatronId(dateMap.get("OLE_PTRN_ID").toString());
                    }
                    if (dateMap.get("GENERAL_BLOCK") != null) {
                        if (dateMap.get("GENERAL_BLOCK").toString().equalsIgnoreCase("Y")) {
                            olePatronDocument.setGeneralBlock(true);
                        }
                    }

                    if (dateMap.get("PAGING_PRIVILEGE") != null) {

                        if (dateMap.get("PAGING_PRIVILEGE").toString().equalsIgnoreCase("Y")) {
                            olePatronDocument.setPagingPrivilege(true);
                        }

                    }

                    if (dateMap.get("COURTESY_NOTICE") != null) {
                        if (dateMap.get("COURTESY_NOTICE").toString().equalsIgnoreCase("Y")) {
                            olePatronDocument.setCourtesyNotice(true);
                        }
                    }


                    if (dateMap.get("DELIVERY_PRIVILEGE") != null) {
                        if (dateMap.get("DELIVERY_PRIVILEGE").toString().equalsIgnoreCase("Y")) {
                            olePatronDocument.setDeliveryPrivilege(true);
                        }
                    }

                    if (dateMap.get("ACTV_IND") != null) {
                        if (dateMap.get("ACTV_IND").toString().equalsIgnoreCase("Y")) {
                            olePatronDocument.setActiveIndicator(true);
                        }
                    }


                    if (dateMap.get("ACTIVATION_DATE") != null) {
                        olePatronDocument.setActivationDate((Timestamp) dateMap.get("ACTIVATION_DATE"));
                    }

                    if (dateMap.get("EXPIRATION_DATE") != null) {
                        olePatronDocument.setExpirationDate((Timestamp) dateMap.get("EXPIRATION_DATE"));
                    }


                    if (dateMap.get("BORR_TYP") != null) {
                        olePatronDocument.setBorrowerType(dateMap.get("BORR_TYP").toString());
                    }

                }
            }
        } catch (Exception e) {
            LOG.info("Exception occured while getting the patron information for the patron barcode : " + patronBarcode + "Reason :" + e.getMessage());
            LOG.error(e, e);
        }
        return olePatronDocument;

    }


}
