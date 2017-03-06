package org.kuali.ole.select.service.impl;

import org.kuali.ole.docstore.common.util.DataSource;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.select.businessobject.OleRequisitionItem;
import org.kuali.ole.select.businessobject.OlePaymentRequestItem;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.businessobject.OleInvoiceItem;
import org.kuali.ole.select.businessobject.OleCopy;
import org.kuali.ole.select.businessobject.OleLineItemReceivingDoc;
import org.kuali.ole.select.businessobject.OleCreditMemoItem;
import org.kuali.ole.select.businessobject.OleLineItemCorrectionReceivingDoc;
import org.kuali.ole.select.bo.OLESerialReceivingDocument;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibHoldingsRecord;

import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.select.service.DocumentUUIDUpdateService;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sureshss on 21/2/17.
 */
public class DocumentUUIDUpdateServiceImpl implements DocumentUUIDUpdateService {
    private BusinessObjectService businessObjectService;
    private Connection connection = null;
    PreparedStatement poUpdatePreparedStatement = null;
    PreparedStatement reqsUpdatePreparedStatement = null;
    PreparedStatement rcvUpdatePreparedStatement = null;
    PreparedStatement invoiceUpdatePreparedStatement = null;
    PreparedStatement preqUdatePreparedStatement = null;
    PreparedStatement crdtMemoUdatePreparedStatement = null;
    PreparedStatement rcvCorUdatePreparedStatement = null;
    PreparedStatement serRcvngUdatePreparedStatement = null;
    PreparedStatement holdingsUdatePreparedStatement = null;
    PreparedStatement itemUpdatePreparedStatement = null;
    PreparedStatement bibHoldingsUdatePreparedStatement = null;
    PreparedStatement copyHoldingsUdatePreparedStatement = null;



    private static final Logger LOG = LoggerFactory.getLogger(DocumentUUIDUpdateServiceImpl.class);

    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    private Connection getDatabaseConnection() throws SQLException {
    if(connection==null||connection.isClosed())
    {
        connection = getConnection();
    }
    return  connection;
}

    private Connection getConnection() throws SQLException {
        DataSource dataSource = null;
        try {
            dataSource = DataSource.getInstance();
        } catch (IOException e) {
            LOG.error("IOException : " + e);
        } catch (SQLException e) {
            LOG.error("SQLException : " + e);
        } catch (PropertyVetoException e) {
            LOG.error("PropertyVetoException : " + e);
        }
        return dataSource.getConnection();
    }

    public void closeConnections() throws SQLException {
        if (poUpdatePreparedStatement != null) {
            poUpdatePreparedStatement.close();
        }
        if (reqsUpdatePreparedStatement != null) {
            reqsUpdatePreparedStatement.close();
        }
        if (rcvUpdatePreparedStatement != null) {
            rcvUpdatePreparedStatement.close();
        }
        if (invoiceUpdatePreparedStatement != null) {
            invoiceUpdatePreparedStatement.close();
        }
        if (preqUdatePreparedStatement != null) {
            preqUdatePreparedStatement.close();
        }
        if (crdtMemoUdatePreparedStatement != null) {
            crdtMemoUdatePreparedStatement.close();
        }
        if (rcvCorUdatePreparedStatement != null) {
            rcvCorUdatePreparedStatement.close();
        }
        if (serRcvngUdatePreparedStatement != null) {
            serRcvngUdatePreparedStatement.close();
        }
        if (holdingsUdatePreparedStatement != null) {
            holdingsUdatePreparedStatement.close();
        }
        if (bibHoldingsUdatePreparedStatement != null) {
            bibHoldingsUdatePreparedStatement.close();
        }
        if (copyHoldingsUdatePreparedStatement != null) {
            copyHoldingsUdatePreparedStatement.close();
        }
        if (itemUpdatePreparedStatement != null) {
            itemUpdatePreparedStatement.close();
        }



        if (connection != null) {
            connection.close();
        }
    }



    public boolean updateCopyRecord(String fromId, String toId) throws SQLException{
        String fromRecordType=fromId.substring(0,3).equals("wio") ? "ITEM" : fromId.substring(0,3).equals("who") ? "HOLDINGS" : fromId.substring(0,3).equals("wbm") ? "BIB" : null;
        String toRecordType=toId.substring(0,3).equals("wio") ? "ITEM" : toId.substring(0,3).equals("who") ? "HOLDINGS" : toId.substring(0,3).equals("wbm") ? "BIB" : null;
        Map fromParamMap = new HashMap();
        Map toParamMap = new HashMap();
        String sourceBibId = null;
        String sourceInstanceId = null;
        String sourceItemId = null;
        Integer reqsItmId = 0;
        Integer poItmId = 0;
        Integer rcvItmId = 0;
        Integer corItmId = 0;
        Integer itemLineNumber = 0;

        if(fromRecordType.equals("ITEM")) {
            fromParamMap.put("itemUUID",fromId);
        }
        else if(fromRecordType.equals("HOLDINGS")) {
            fromParamMap.put("instanceId",fromId);
        }

        if(toRecordType.equals("ITEM")) {
            toParamMap.put("itemUUID",toId);
        }
        else if(toRecordType.equals("HOLDINGS")) {
            toParamMap.put("instanceId",toId);
        }
        else if(toRecordType.equals("BIB")) {
            toParamMap.put("bibId",toId);
            toParamMap.put("instanceId",fromId);
        }

        List<OleCopy> oleSourceCopyList = (List<OleCopy>) getBusinessObjectService().findMatching(OleCopy.class, fromParamMap);
        List<OleCopy> oleDestCopyList = (List<OleCopy>) getBusinessObjectService().findMatching(OleCopy.class, toParamMap);
        String destBibId = null;
        String destInstanceId = null;
        if(oleSourceCopyList.size() > 0  && oleDestCopyList.size() > 0) {
            if (oleDestCopyList.size() > 0) {
                destBibId = oleDestCopyList.get(0).getBibId();
                destInstanceId = oleDestCopyList.get(0).getInstanceId();
            }
        }
        else  if(oleSourceCopyList.size() > 0  && oleDestCopyList.size() == 0) {
            if (toRecordType.equals("HOLDINGS")) {
                Map holdMap = new HashMap();
                holdMap.put("holdingsId", toId.substring(4));
                destInstanceId = toId;
                HoldingsRecord oleHoldingsRecord = (HoldingsRecord) getBusinessObjectService().findByPrimaryKey(HoldingsRecord.class, holdMap);
                destBibId = "wbm-"+oleHoldingsRecord.getBibId();
            }
            else if(toRecordType.equals("BIB")) {
                Map bibdMap = new HashMap();
                bibdMap.put("bibId", toId.substring(4));

                BibRecord oleBibRecord = (BibRecord) getBusinessObjectService().findByPrimaryKey(BibRecord.class, bibdMap);
                destBibId = "wbm-"+oleBibRecord.getBibId();
                destInstanceId=fromId;
            }
        }
        else  if(oleSourceCopyList.size() == 0  && oleDestCopyList.size() == 0) {
            if (toRecordType.equals("HOLDINGS")) {
                Map holdMap = new HashMap();
                holdMap.put("holdingsId", toId.substring(4));
                destInstanceId = toId;
                sourceItemId = fromId;
                HoldingsRecord oleHoldingsRecord = (HoldingsRecord) getBusinessObjectService().findByPrimaryKey(HoldingsRecord.class, holdMap);
                destBibId = "wbm-"+oleHoldingsRecord.getBibId();
            } else if (toRecordType.equals("BIB")) {
                Map bibdMap = new HashMap();
                bibdMap.put("bibId", toId.substring(4));
                BibRecord oleBibRecord = (BibRecord) getBusinessObjectService().findByPrimaryKey(BibRecord.class, bibdMap);
                destBibId = "wbm-"+oleBibRecord.getBibId();
                destInstanceId=fromId;
            }
            if (fromRecordType.equals("HOLDINGS")) {
                Map holdMap = new HashMap();
                holdMap.put("holdingsId", fromId.substring(4));
                sourceInstanceId = fromId;
                HoldingsRecord oleHoldingsRecord = (HoldingsRecord) getBusinessObjectService().findByPrimaryKey(HoldingsRecord.class, holdMap);
                sourceBibId = "wbm-"+oleHoldingsRecord.getBibId();
            } else if (fromRecordType.equals("ITEM")) {
                Map itemMap = new HashMap();
                itemMap.put("itemId", fromId.substring(4));
                sourceItemId = fromId;
                ItemRecord oleItemRecord = (ItemRecord) getBusinessObjectService().findByPrimaryKey(ItemRecord.class, itemMap);
                sourceInstanceId = oleItemRecord.getHoldingsId();
                Map holdMap = new HashMap();
                holdMap.put("holdingsId", sourceInstanceId);

                HoldingsRecord oleHoldingsRecord = (HoldingsRecord) getBusinessObjectService().findByPrimaryKey(HoldingsRecord.class, holdMap);
                sourceBibId = "wbm-"+oleHoldingsRecord.getBibId();

            }
        }
        else  if(oleSourceCopyList.size() == 0  && oleDestCopyList.size() > 0) {
            if (oleDestCopyList.size() > 0) {
                destBibId = oleDestCopyList.get(0).getBibId();
                destInstanceId = oleDestCopyList.get(0).getInstanceId();
            }
            if (fromRecordType.equals("HOLDINGS")) {
                Map holdMap = new HashMap();
                holdMap.put("holdingsId", fromId.substring(4));
                sourceInstanceId = fromId;
                HoldingsRecord oleHoldingsRecord = (HoldingsRecord) getBusinessObjectService().findByPrimaryKey(HoldingsRecord.class, holdMap);
                sourceBibId = "wbm-"+oleHoldingsRecord.getBibId();
            } else if (fromRecordType.equals("ITEM")) {
                Map itemMap = new HashMap();
                itemMap.put("itemId", fromId.substring(4));
                sourceItemId = fromId;
                ItemRecord oleItemRecord = (ItemRecord) getBusinessObjectService().findByPrimaryKey(ItemRecord.class, itemMap);
                sourceInstanceId = oleItemRecord.getHoldingsId();
                Map holdMap = new HashMap();
                holdMap.put("holdingsId", sourceInstanceId);

                HoldingsRecord oleHoldingsRecord = (HoldingsRecord) getBusinessObjectService().findByPrimaryKey(HoldingsRecord.class, holdMap);
                sourceBibId = "wbm-"+oleHoldingsRecord.getBibId();

            }


        }
        if (oleSourceCopyList.size() > 0) {
            for (OleCopy oleSourceCopy : oleSourceCopyList) {
                sourceBibId = oleSourceCopy.getBibId();
                poItmId = oleSourceCopy.getPoItemId();
                reqsItmId = oleSourceCopy.getReqItemId();
                rcvItmId = oleSourceCopy.getReceivingItemId();
                corItmId = oleSourceCopy.getCorrectionItemId();
                sourceInstanceId = oleSourceCopy.getInstanceId();
                sourceItemId = oleSourceCopy.getItemUUID();
                Map poMap = new HashMap();
                Map serMap = new HashMap();
                Map reqsMap = new HashMap();
                Map rcvMap = new HashMap();
                Map corMap = new HashMap();
                Map invMap = new HashMap();
                poMap.put("itemTitleId", sourceBibId);
                poMap.put("itemIdentifier", poItmId);

                reqsMap.put("itemTitleId", sourceBibId);
                reqsMap.put("itemIdentifier", reqsItmId);

                rcvMap.put("itemTitleId", sourceBibId);
                rcvMap.put("receivingLineItemIdentifier", rcvItmId);

                corMap.put("itemTitleId", sourceBibId);
                corMap.put("receivingLineItemIdentifier", corItmId);

                serMap.put("bibId", sourceBibId);
                serMap.put("instanceId", sourceInstanceId);


                List<OlePurchaseOrderItem> olePOItemList = (List<OlePurchaseOrderItem>) getBusinessObjectService().findMatching(OlePurchaseOrderItem.class, poMap);
                List<OleRequisitionItem> olereqsItemList = (List<OleRequisitionItem>) getBusinessObjectService().findMatching(OleRequisitionItem.class, reqsMap);
                List<OleLineItemReceivingDoc> oleLineItemReceivingDocList = (List<OleLineItemReceivingDoc>) getBusinessObjectService().findMatching(OleLineItemReceivingDoc.class, rcvMap);
                List<OleLineItemCorrectionReceivingDoc> oleRcvCorItemList = (List<OleLineItemCorrectionReceivingDoc>) getBusinessObjectService().findMatching(OleLineItemCorrectionReceivingDoc.class, corMap);
                List<OLESerialReceivingDocument> oleSerRcvngItemList = (List<OLESerialReceivingDocument>) getBusinessObjectService().findMatching(OLESerialReceivingDocument.class, serMap);

                if (olePOItemList.size() > 0) {
                    for (OlePurchaseOrderItem olePurchaseOrderItem : olePOItemList) {
                        itemLineNumber = olePurchaseOrderItem.getItemLineNumber();
                        String updatePOItemQuery = "UPDATE PUR_PO_ITM_T SET OLE_DOCUMENT_UUID=? WHERE OLE_DOCUMENT_UUID=? AND PO_ITM_ID=?";
                        poUpdatePreparedStatement = getDatabaseConnection().prepareStatement(updatePOItemQuery);
                        poUpdatePreparedStatement.setString(1, destBibId);
                        poUpdatePreparedStatement.setString(2, sourceBibId);
                        poUpdatePreparedStatement.setInt(3, poItmId);
                        try {
                            poUpdatePreparedStatement.executeUpdate();
                        } catch (Exception e1) {
                            connection.rollback();
                            LOG.error("Exception while update of OLE_DOCUMENT_UUID in table PUR_PO_ITM_T from " + sourceBibId + " to = " + destBibId + " : ", e1);
                            return Boolean.FALSE;
                        }

                        invMap.put("itemTitleId", sourceBibId);
                        invMap.put("itemLineNumber", itemLineNumber);

                        List<OleInvoiceItem> oleInvoiceItemList = (List<OleInvoiceItem>) getBusinessObjectService().findMatching(OleInvoiceItem.class, invMap);
                        List<OlePaymentRequestItem> olePaymentRequestItemList = (List<OlePaymentRequestItem>) getBusinessObjectService().findMatching(OlePaymentRequestItem.class, invMap);
                        List<OleCreditMemoItem> oleCrdtMemoRequestItemList = (List<OleCreditMemoItem>) getBusinessObjectService().findMatching(OleCreditMemoItem.class, invMap);


                        if (oleInvoiceItemList.size() > 0) {
                            String updateInvoiceItemQuery = "UPDATE OLE_AP_INV_PO_ITM_T SET OLE_DOCUMENT_UUID=? WHERE OLE_DOCUMENT_UUID=? AND ITM_LN_NBR=?";
                            invoiceUpdatePreparedStatement = getDatabaseConnection().prepareStatement(updateInvoiceItemQuery);
                            invoiceUpdatePreparedStatement.setString(1, destBibId);
                            invoiceUpdatePreparedStatement.setString(2, sourceBibId);
                            invoiceUpdatePreparedStatement.setInt(3, itemLineNumber);
                            try {
                                invoiceUpdatePreparedStatement.executeUpdate();
                            } catch (Exception e1) {
                                connection.rollback();
                                LOG.error("Exception while update of OLE_DOCUMENT_UUID in table PUR_PO_ITM_T from " + sourceBibId + " to = " + destBibId + " : ", e1);
                                return Boolean.FALSE;
                            }
                        }

                        if (olePaymentRequestItemList.size() > 0) {
                            String updatePreqItemQuery = "UPDATE AP_PMT_RQST_ITM_T SET OLE_DOCUMENT_UUID=? WHERE OLE_DOCUMENT_UUID=? AND ITM_LN_NBR=?";
                            preqUdatePreparedStatement = getDatabaseConnection().prepareStatement(updatePreqItemQuery);
                            preqUdatePreparedStatement.setString(1, destBibId);
                            preqUdatePreparedStatement.setString(2, sourceBibId);
                            preqUdatePreparedStatement.setInt(3, itemLineNumber);
                            try {
                                preqUdatePreparedStatement.executeUpdate();
                            } catch (Exception e1) {
                                connection.rollback();
                                LOG.error("Exception while update of OLE_DOCUMENT_UUID in table PUR_PO_ITM_T from " + sourceBibId + " to = " + destBibId + " : ", e1);
                                return Boolean.FALSE;
                            }
                        }

                        if (oleCrdtMemoRequestItemList.size() > 0) {
                            String updateCrdtMemoRequestItemQuery = "UPDATE AP_CRDT_MEMO_ITM_T SET OLE_DOCUMENT_UUID=? WHERE OLE_DOCUMENT_UUID=? AND ITM_LN_NBR=?";
                            crdtMemoUdatePreparedStatement = getDatabaseConnection().prepareStatement(updateCrdtMemoRequestItemQuery);
                            crdtMemoUdatePreparedStatement.setString(1, destBibId);
                            crdtMemoUdatePreparedStatement.setString(2, sourceBibId);
                            crdtMemoUdatePreparedStatement.setInt(3, itemLineNumber);
                            try {
                                crdtMemoUdatePreparedStatement.executeUpdate();
                            } catch (Exception e1) {
                                connection.rollback();
                                LOG.error("Exception while update of OLE_DOCUMENT_UUID in table PUR_PO_ITM_T from " + sourceBibId + " to = " + destBibId + " : ", e1);
                                return Boolean.FALSE;
                            }
                        }
                    }
                }
                if (olereqsItemList.size() > 0) {
                    String updateReqsItemQuery = "UPDATE PUR_REQS_ITM_T SET OLE_DOCUMENT_UUID=? WHERE OLE_DOCUMENT_UUID=? AND REQS_ITM_ID=?";
                    reqsUpdatePreparedStatement = getDatabaseConnection().prepareStatement(updateReqsItemQuery);
                    reqsUpdatePreparedStatement.setString(1, destBibId);
                    reqsUpdatePreparedStatement.setString(2, sourceBibId);
                    reqsUpdatePreparedStatement.setInt(3, reqsItmId);
                    try {
                        reqsUpdatePreparedStatement.executeUpdate();
                    } catch (Exception e1) {
                        connection.rollback();
                        LOG.error("Exception while update of OLE_DOCUMENT_UUID in table PUR_PO_ITM_T from " + sourceBibId + " to = " + destBibId + " : ", e1);
                        return Boolean.FALSE;
                    }
                }
                if (oleLineItemReceivingDocList.size() > 0) {
                    String updateRcvngItemQuery = "UPDATE OLE_PUR_RCVNG_LN_ITM_DOC_T SET OLE_DOCUMENT_UUID=? WHERE OLE_DOCUMENT_UUID=? AND RCVNG_LN_ITM_ID=?";
                    rcvUpdatePreparedStatement = getDatabaseConnection().prepareStatement(updateRcvngItemQuery);
                    rcvUpdatePreparedStatement.setString(1, destBibId);
                    rcvUpdatePreparedStatement.setString(2, sourceBibId);
                    rcvUpdatePreparedStatement.setInt(3, rcvItmId);
                    try {
                        rcvUpdatePreparedStatement.executeUpdate();
                    } catch (Exception e1) {
                        connection.rollback();
                        LOG.error("Exception while update of OLE_DOCUMENT_UUID in table PUR_PO_ITM_T from " + sourceBibId + " to = " + destBibId + " : ", e1);
                        return Boolean.FALSE;
                    }

                }

                if (oleRcvCorItemList.size() > 0) {
                    String updateRcvngCorQuery = "UPDATE OLE_PUR_RCVNG_COR_LN_ITM_DOC_T SET OLE_DOCUMENT_UUID=? WHERE OLE_DOCUMENT_UUID=? AND RCVNG_COR_ITM_ID=?";
                    rcvCorUdatePreparedStatement = getDatabaseConnection().prepareStatement(updateRcvngCorQuery);
                    rcvCorUdatePreparedStatement.setString(1, destBibId);
                    rcvCorUdatePreparedStatement.setString(2, sourceBibId);
                    rcvCorUdatePreparedStatement.setInt(3, corItmId);
                    try {
                        rcvCorUdatePreparedStatement.executeUpdate();
                    } catch (Exception e1) {
                        connection.rollback();
                        LOG.error("Exception while update of OLE_DOCUMENT_UUID in table OLE_PUR_RCVNG_COR_LN_ITM_DOC_T from " + sourceBibId + " to = " + destBibId + " : ", e1);
                        return Boolean.FALSE;
                    }

                }

                if (oleSerRcvngItemList.size() > 0) {
                    String updateSerRcvQuery = "UPDATE OLE_SER_RCV_REC SET BIB_ID=?,INSTANCE_ID=? WHERE BIB_ID=? AND INSTANCE_ID=?";
                    serRcvngUdatePreparedStatement = getDatabaseConnection().prepareStatement(updateSerRcvQuery);
                    serRcvngUdatePreparedStatement.setString(1, destBibId);
                    serRcvngUdatePreparedStatement.setString(2, destInstanceId);
                    serRcvngUdatePreparedStatement.setString(3, sourceBibId);
                    serRcvngUdatePreparedStatement.setString(3, sourceInstanceId);
                    try {
                        serRcvngUdatePreparedStatement.executeUpdate();
                    } catch (Exception e1) {
                        connection.rollback();
                        LOG.error("Exception while update of BIB_ID, INSTANCE_ID in table OLE_SER_RCV_REC from " + sourceBibId + " to = " + destBibId + " : ", e1);
                        return Boolean.FALSE;
                    }

                }
                if(oleSourceCopy.getItemUUID() != null) {
                    Map itemMap = new HashMap();
                    itemMap.put("itemId", sourceItemId.substring(4));

                    List<ItemRecord> oleItemRecordList = (List<ItemRecord>) getBusinessObjectService().findMatching(ItemRecord.class, itemMap);
                    //    String sourceHoldingsId = sourceInstanceId;

                    if (oleItemRecordList.size() > 0) {
                        String updateItemsQuery = "UPDATE OLE_DS_ITEM_T SET HOLDINGS_ID=? WHERE ITEM_ID=?";
                        itemUpdatePreparedStatement = getDatabaseConnection().prepareStatement(updateItemsQuery);
                        itemUpdatePreparedStatement.setString(1, destInstanceId.substring(4));
                        itemUpdatePreparedStatement.setString(2, sourceItemId.substring(4));
                        try {
                            itemUpdatePreparedStatement.executeUpdate();
                        } catch (Exception e1) {
                            connection.rollback();
                            LOG.error("Exception while update of BIB_ID in table OLE_DS_HOLDINGS_T from " + sourceBibId + " to = " + destBibId + " : ", e1);
                            return Boolean.FALSE;
                        }
                    }
                }

                Map holdMap = new HashMap();
                holdMap.put("holdingsId", oleSourceCopy.getInstanceId().substring(4));

                List<HoldingsRecord> oleHoldingsRecordList = (List<HoldingsRecord>) getBusinessObjectService().findMatching(HoldingsRecord.class, holdMap);
                String sourceHoldingsId = oleSourceCopy.getInstanceId();


                if (fromRecordType.equals("HOLDINGS")) {
                    Map copyHoldMap = new HashMap();
                    copyHoldMap.put("instanceId", oleSourceCopy.getInstanceId());
                    List<OleCopy> copyHoldingsList = (List<OleCopy>) getBusinessObjectService().findMatching(OleCopy.class, copyHoldMap);
                    if (copyHoldingsList.size() > 0) {
                        String updateCopyHoldingsQuery = "UPDATE OLE_COPY_T SET BIB_UUID=? WHERE INSTANCE_UUID=?";
                        copyHoldingsUdatePreparedStatement = getDatabaseConnection().prepareStatement(updateCopyHoldingsQuery);
                        copyHoldingsUdatePreparedStatement.setString(1, destBibId);
                        copyHoldingsUdatePreparedStatement.setString(2, sourceInstanceId);
                        try {
                            copyHoldingsUdatePreparedStatement.executeUpdate();
                        } catch (Exception e1) {
                            connection.rollback();
                            LOG.error("Exception while update of BIB_UUID in table OLE_COPY_T from " + sourceBibId + " to = " + destBibId + " : ", e1);
                            return Boolean.FALSE;
                        }
                    }

                    if (oleHoldingsRecordList.size() > 0) {
                        String updateHoldingsQuery = "UPDATE OLE_DS_HOLDINGS_T SET BIB_ID=? WHERE HOLDINGS_ID=?";
                        holdingsUdatePreparedStatement = getDatabaseConnection().prepareStatement(updateHoldingsQuery);
                        holdingsUdatePreparedStatement.setString(1, destBibId.substring(4));
                        holdingsUdatePreparedStatement.setString(2, sourceHoldingsId.substring(4));
                        try {
                            holdingsUdatePreparedStatement.executeUpdate();
                        } catch (Exception e1) {
                            connection.rollback();
                            LOG.error("Exception while update of BIB_ID in table OLE_DS_HOLDINGS_T from " + sourceBibId + " to = " + destBibId + " : ", e1);
                            return Boolean.FALSE;
                        }
                    }

                    List<BibHoldingsRecord> bibHoldingsRecordList = (List<BibHoldingsRecord>) getBusinessObjectService().findMatching(BibHoldingsRecord.class, holdMap);
                    if (bibHoldingsRecordList.size() > 0) {
                        String updatebibHoldingsQuery = "UPDATE OLE_DS_BIB_HOLDINGS_T SET BIB_ID=? WHERE HOLDINGS_ID=?";
                        bibHoldingsUdatePreparedStatement = getDatabaseConnection().prepareStatement(updatebibHoldingsQuery);
                        bibHoldingsUdatePreparedStatement.setString(1, destBibId.substring(4));
                        bibHoldingsUdatePreparedStatement.setString(2, sourceHoldingsId.substring(4));
                        try {
                            bibHoldingsUdatePreparedStatement.executeUpdate();
                        } catch (Exception e1) {
                            connection.rollback();
                            LOG.error("Exception while update of BIB_ID in table OLE_DS_BIB_HOLDINGS_T from " + sourceBibId + " to = " + destBibId + " : ", e1);
                            return Boolean.FALSE;
                        }
                    }

                }
                else {
                    Map copyMap = new HashMap();
                    copyMap.put("itemUUID", oleSourceCopy.getItemUUID());
                    List<OleCopy> copyRecordsList = (List<OleCopy>) getBusinessObjectService().findMatching(OleCopy.class, copyMap);
                    sourceItemId = oleSourceCopy.getItemUUID();
                    if (copyRecordsList.size() > 0) {
                        String updateCopyRecordQuery = "UPDATE OLE_COPY_T SET BIB_UUID=?, INSTANCE_UUID=? WHERE ITEM_UUID=?";
                        copyHoldingsUdatePreparedStatement = getDatabaseConnection().prepareStatement(updateCopyRecordQuery);
                        copyHoldingsUdatePreparedStatement.setString(1, destBibId);
                        copyHoldingsUdatePreparedStatement.setString(2, destInstanceId);
                        copyHoldingsUdatePreparedStatement.setString(3, sourceItemId);
                        try {
                            copyHoldingsUdatePreparedStatement.executeUpdate();
                        } catch (Exception e1) {
                            connection.rollback();
                            LOG.error("Exception while update of BIB_UUID, INSTANCE_UUID in table OLE_COPY_T for " + sourceItemId + " from  " + sourceBibId + " to = " + destBibId + " : ", e1);
                            return Boolean.FALSE;
                        }
                    }
                }

            }
            connection.commit();
            closeConnections();
            return Boolean.TRUE;
        }
       else {
            if(sourceItemId != null) {
                Map itemMap = new HashMap();
                itemMap.put("itemId", sourceItemId.substring(4));

                List<ItemRecord> oleItemRecordList = (List<ItemRecord>) getBusinessObjectService().findMatching(ItemRecord.class, itemMap);
                //    String sourceHoldingsId = sourceInstanceId;

                if (oleItemRecordList.size() > 0) {
                    String updateItemsQuery = "UPDATE OLE_DS_ITEM_T SET HOLDINGS_ID=? WHERE ITEM_ID=?";
                    itemUpdatePreparedStatement = getDatabaseConnection().prepareStatement(updateItemsQuery);
                    itemUpdatePreparedStatement.setString(1, destInstanceId.substring(4));
                    itemUpdatePreparedStatement.setString(2, sourceItemId.substring(4));
                    try {
                        itemUpdatePreparedStatement.executeUpdate();
                    } catch (Exception e1) {
                        connection.rollback();
                        LOG.error("Exception while update of BIB_ID in table OLE_DS_HOLDINGS_T from " + sourceBibId + " to = " + destBibId + " : ", e1);
                        return Boolean.FALSE;
                    }
                }
            }
            if (fromRecordType.equals("HOLDINGS")) {
                Map holdMap = new HashMap();
                holdMap.put("holdingsId", sourceInstanceId.substring(4));

                List<HoldingsRecord> oleHoldingsRecordList = (List<HoldingsRecord>) getBusinessObjectService().findMatching(HoldingsRecord.class, holdMap);
                String sourceHoldingsId = sourceInstanceId;

                if (oleHoldingsRecordList.size() > 0) {
                    String updateHoldingsQuery = "UPDATE OLE_DS_HOLDINGS_T SET BIB_ID=? WHERE HOLDINGS_ID=?";
                    holdingsUdatePreparedStatement = getDatabaseConnection().prepareStatement(updateHoldingsQuery);
                    holdingsUdatePreparedStatement.setString(1, destBibId.substring(4));
                    holdingsUdatePreparedStatement.setString(2, sourceInstanceId.substring(4));
                    try {
                        holdingsUdatePreparedStatement.executeUpdate();
                    } catch (Exception e1) {
                        connection.rollback();
                        LOG.error("Exception while update of BIB_ID in table OLE_DS_HOLDINGS_T from " + sourceBibId + " to = " + destBibId + " : ", e1);
                        return Boolean.FALSE;
                    }
                }
                List<BibHoldingsRecord> bibHoldingsRecordList = (List<BibHoldingsRecord>) getBusinessObjectService().findMatching(BibHoldingsRecord.class, holdMap);
                if (bibHoldingsRecordList.size() > 0) {
                    String updatebibHoldingsQuery = "UPDATE OLE_DS_BIB_HOLDINGS_T SET BIB_ID=? WHERE HOLDINGS_ID=?";
                    bibHoldingsUdatePreparedStatement = getDatabaseConnection().prepareStatement(updatebibHoldingsQuery);
                    bibHoldingsUdatePreparedStatement.setString(1, destBibId.substring(4));
                    bibHoldingsUdatePreparedStatement.setString(2, sourceHoldingsId.substring(4));
                    try {
                        bibHoldingsUdatePreparedStatement.executeUpdate();
                    } catch (Exception e1) {
                        connection.rollback();
                        LOG.error("Exception while update of BIB_ID in table OLE_DS_BIB_HOLDINGS_T from " + sourceBibId + " to = " + destBibId + " : ", e1);
                        return Boolean.FALSE;
                    }
                }
            }
            connection.commit();
            closeConnections();
            return Boolean.TRUE;
        }
    }
}
