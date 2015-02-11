package org.kuali.ole.select.document.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.module.purap.document.service.OlePurapService;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.OleCopy;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.businessobject.OleRequisitionItem;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.select.document.OleRequisitionDocument;
import org.kuali.ole.select.document.service.OleCopyHelperService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.ole.docstore.common.document.BibTree;

import org.kuali.ole.docstore.common.util.DataSource;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: aurojyotit
 * Date: 12/12/14
 * Time: 12:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLECopyUpdateImpl {
    protected static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OLECopyUpdateImpl.class);
    String directory = ConfigContext.getCurrentContextConfig().getProperty(org.kuali.ole.OLEConstants.STAGING_DIRECTORY) + File.separator + "olecopies";
    String saveOleCopyFileName = "req_po_nocopies_process_info.txt";
    String infoOleCopyFileName = "req_po_nocopies_info.txt";

    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (null == docstoreClientLocator) {
            return SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }


    public List<OlePurchaseOrderDocument> getOlePurchaseOrderDocuments() {
        List result = new ArrayList();
        List<OlePurchaseOrderDocument> olePurchaseOrderDocuments = (List<OlePurchaseOrderDocument>) KRADServiceLocator.getBusinessObjectService().findAll(OlePurchaseOrderDocument.class);
        if (CollectionUtils.isNotEmpty(olePurchaseOrderDocuments)) {
            result.addAll(olePurchaseOrderDocuments);
        }
        return result;
    }


    public void processUpdateOleCopies() {
        StringBuffer buffer = new StringBuffer();
        buffer = new StringBuffer();
        List<OlePurchaseOrderDocument> olePurchaseOrderDocuments = getOlePurchaseOrderDocuments();
        if (CollectionUtils.isNotEmpty(olePurchaseOrderDocuments)) {
            for (OlePurchaseOrderDocument olePurchaseOrderDocument : olePurchaseOrderDocuments) {
                olePurchaseOrderDocument.processAfterRetrieve();
                Map map = new HashMap();
                map.put("purapDocumentIdentifier", olePurchaseOrderDocument.getRequisitionIdentifier().toString());
                List<OleRequisitionDocument> oleRequisitionDocuments = (List<OleRequisitionDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OleRequisitionDocument.class, map);
                if (CollectionUtils.isNotEmpty(oleRequisitionDocuments)) {
                    OleRequisitionDocument oleRequisitionDocument = oleRequisitionDocuments.get(0);
                    if (LOG.isInfoEnabled()) {
                        LOG.info("#REQ DOC NO :" + oleRequisitionDocument.getDocumentNumber() + "  ######  " + "PO DOC NO :" + olePurchaseOrderDocument.getDocumentNumber());
                        buffer.append("#REQ DOC NO :" + oleRequisitionDocument.getDocumentNumber() + "  ######  " + "PO DOC NO :" + olePurchaseOrderDocument.getDocumentNumber());
                        buffer.append("\n");
                    }
                    if (CollectionUtils.isNotEmpty(olePurchaseOrderDocument.getItems())) {
                        try {
                            for (OlePurchaseOrderItem olePurchaseOrderItem : (List<OlePurchaseOrderItem>) olePurchaseOrderDocument.getItems()) {
                                if (CollectionUtils.isEmpty(olePurchaseOrderItem.getCopyList())) {
                                    if (olePurchaseOrderItem.getItemLineNumber() != null) {
                                        prepareAndSaveOleCopy(oleRequisitionDocument, olePurchaseOrderDocument, olePurchaseOrderItem, buffer);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            LOG.info("###########  processUpdateOleCopies of OLECopyUpdateImpl ###########" + e);
                        }
                    }
                }
            }
        }
        try {
            this.writeStatusToFile(directory, saveOleCopyFileName, buffer.toString());
        } catch (Exception e) {
            LOG.error("while writing the file error occurred" + e);
        }
    }

    public void checkPoAndReqHaveOleCopies() {
        StringBuffer buffer = new StringBuffer();
        buffer = new StringBuffer();
        if (LOG.isInfoEnabled()) {
            LOG.info("###### Start of method execution for checking olecopies in Requisition and PurchaseOrder  #####");
            buffer.append("###### Start of method execution for checking olecopies in Requisition and PurchaseOrder  #####");
            buffer.append("\n");
        }
        List<OlePurchaseOrderDocument> olePurchaseOrderDocuments = getOlePurchaseOrderDocuments();
        if (CollectionUtils.isNotEmpty(olePurchaseOrderDocuments)) {
            for (OlePurchaseOrderDocument olePurchaseOrderDocument : olePurchaseOrderDocuments) {
                olePurchaseOrderDocument.processAfterRetrieve();
                Map map = new HashMap();
                map.put("purapDocumentIdentifier", olePurchaseOrderDocument.getRequisitionIdentifier().toString());
                List<OleRequisitionDocument> oleRequisitionDocuments = (List<OleRequisitionDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OleRequisitionDocument.class, map);
                if (CollectionUtils.isNotEmpty(oleRequisitionDocuments)) {
                    OleRequisitionDocument oleRequisitionDocument = oleRequisitionDocuments.get(0);
                    if (CollectionUtils.isNotEmpty(olePurchaseOrderDocument.getItems())) {
                        try {
                            for (OlePurchaseOrderItem olePurchaseOrderItem : (List<OlePurchaseOrderItem>) olePurchaseOrderDocument.getItems()) {
                                if (CollectionUtils.isEmpty(olePurchaseOrderItem.getCopyList())) {
                                    if (olePurchaseOrderItem.getItemLineNumber() != null) {
                                        if (LOG.isInfoEnabled()) {
                                            LOG.info("    #REQ DOC NO :" + oleRequisitionDocument.getDocumentNumber() + "  #PO ITEM ID :" + olePurchaseOrderItem.getItemIdentifier() + "  ######  " + "PO DOC NO :" + olePurchaseOrderDocument.getDocumentNumber());
                                            buffer.append("    #REQ DOC NO :" + oleRequisitionDocument.getDocumentNumber() + "  #PO ITEM ID :" + olePurchaseOrderItem.getItemIdentifier() + "  ######  " + "PO DOC NO :" + olePurchaseOrderDocument.getDocumentNumber());
                                            buffer.append("\n");
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            LOG.info("###########  processUpdateOleCopies of OLECopyUpdateImpl ###########" + e);
                        }
                    }
                }
            }
        }
        if (LOG.isInfoEnabled()) {
            LOG.info("###### End of method execution for checking olecopies in Requisition and PurchaseOrder  #####");
            buffer.append("###### End of method execution for checking olecopies in Requisition and PurchaseOrder  #####");
            buffer.append("\n");
        }
        try {
            this.writeStatusToFile(directory, infoOleCopyFileName, buffer.toString());
        } catch (Exception e) {
            LOG.error("while writing the file error occurred" + e);
        }
    }

    public void saveOleCopies(List<OleCopy> oleCopies, StringBuffer buffer) {
        try {
            if (LOG.isInfoEnabled()) {
                LOG.info("################Inside saveOleCopies method#################");
                buffer.append("################Inside saveOleCopies method#################");
                buffer.append("\n");
            }
            KRADServiceLocator.getBusinessObjectService().save(oleCopies);
            if (LOG.isInfoEnabled()) {
                for (OleCopy oleCopy : oleCopies) {
                    LOG.info("#OleCopy --> ID: " + oleCopy.getCopyId() + " REQ ITEM ID :  " + oleCopy.getReqItemId() + " PO ITEM ID : " + oleCopy.getPoItemId() + " PO DOC NUM : " + oleCopy.getPoDocNum() + " HOLDINGS ID : " + oleCopy.getInstanceId() + " ITEM ID : " + oleCopy.getItemUUID() + " BIB ID : " + oleCopy.getBibId());
                    buffer.append("#OleCopy --> ID: " + oleCopy.getCopyId() + " REQ ITEM ID :  " + oleCopy.getReqItemId() + " PO ITEM ID : " + oleCopy.getPoItemId() + " PO DOC NUM : " + oleCopy.getPoDocNum() + " HOLDINGS ID : " + oleCopy.getInstanceId() + " ITEM ID : " + oleCopy.getItemUUID() + " BIB ID : " + oleCopy.getBibId());
                    buffer.append("\n");
                }
                if (LOG.isInfoEnabled()) {
                    LOG.info("################Copies record saved successfully#################");
                }
                buffer.append("################Copies record saved successfully#################");
                buffer.append("\n");
            }
        } catch (Exception e) {
            LOG.error("Error occured : while updating the copies for requisition document" + e.getMessage() + " " + e);
            buffer.append("Error occured : while updating the copies for requisition document" + e.getMessage() + " " + e);
            buffer.append("\n");
        }
    }

    public void prepareAndSaveOleCopy(OleRequisitionDocument oleRequisitionDocument, OlePurchaseOrderDocument olePurchaseOrderDocument, OlePurchaseOrderItem olePurchaseOrderItem, StringBuffer buffer) {
        if (CollectionUtils.isNotEmpty(oleRequisitionDocument.getItems())) {
            List<OleRequisitionItem> oleRequisitionItems = (List<OleRequisitionItem>) oleRequisitionDocument.getItems();
            if (CollectionUtils.isNotEmpty(oleRequisitionItems)) {
                for (OleRequisitionItem item : oleRequisitionItems) {
                    try {
                        if (item.getItemLineNumber() != null && olePurchaseOrderItem.getItemLineNumber() != null && item.getItemLineNumber().equals(olePurchaseOrderItem.getItemLineNumber())) {
                            try {
                                if (null != item.getItemType() && item.getItemType().isQuantityBasedGeneralLedgerIndicator()) {
                                    if (item.getItemQuantity() != null && item.getItemNoOfParts() != null && !item.getItemQuantity().isGreaterThan(new KualiDecimal(1))
                                            && !item.getItemNoOfParts().isGreaterThan(new KualiInteger(1))) {
                                        OleCopy oleCopy = new OleCopy();
                                        oleCopy.setLocation(item.getItemLocation());
                                        oleCopy.setBibId(item.getItemTitleId());
                                        if (StringUtils.isNotBlank(item.getLinkToOrderOption()) && (item.getLinkToOrderOption().equals(OLEConstants.NB_PRINT) || item.getLinkToOrderOption().equals(OLEConstants.EB_PRINT))) {
                                            oleCopy.setCopyNumber(item.getSingleCopyNumber() != null && !item.getSingleCopyNumber().isEmpty() ? item.getSingleCopyNumber() : null);
                                        }
                                        oleCopy.setReceiptStatus(OLEConstants.OleLineItemReceiving.NOT_RECEIVED_STATUS);
                                        oleCopy.setReqDocNum(olePurchaseOrderDocument.getPurapDocumentIdentifier());
                                        oleCopy.setReqItemId(item.getItemIdentifier());
                                        oleCopy.setPoDocNum(olePurchaseOrderDocument.getDocumentNumber());
                                        oleCopy.setPoItemId(olePurchaseOrderItem.getItemIdentifier());
                                        List<OleCopy> copyList = new ArrayList<>();
                                        copyList.add(oleCopy);
                                        item.setCopyList(copyList);
                                        Map map = new HashMap();
                                        map.put("purchaseOrderItemLineId", olePurchaseOrderDocument.getPurapDocumentIdentifier());
                                        List<ItemRecord> itemRecords = (List<ItemRecord>) KRADServiceLocator.getBusinessObjectService().findMatching(ItemRecord.class, map);
                                        if (CollectionUtils.isNotEmpty(itemRecords)) {
                                            oleCopy.setItemUUID(DocumentUniqueIDPrefix.PREFIX_WORK_ITEM_OLEML + "-" + itemRecords.get(0).getItemId());
                                            oleCopy.setInstanceId(DocumentUniqueIDPrefix.PREFIX_WORK_HOLDINGS_OLEML + "-" + itemRecords.get(0).getHoldingsId());
                                        }
                                        saveOleCopies(item.getCopyList(), buffer);
                                    } else {
                                        //oleRequisitionDocument.populateCopiesSection(item);
                                        KualiInteger noOfParts = new KualiInteger((item.getItemNoOfParts().intValue()));
                                        KualiInteger noOfCopies = new KualiInteger(item.getItemQuantity().intValue());
                                        int copyNumberMax = noOfCopies.intValue();
                                        KualiInteger noOfItems = new KualiInteger(1);
                                        if (noOfParts != null && noOfItems != null) {
                                            if (noOfItems.isZero() && noOfCopies.isZero()) {
                                                noOfItems = new KualiInteger(1);
                                            } else {
                                                noOfItems = noOfCopies.multiply(noOfParts);
                                            }
                                        }
                                        int enumNum = noOfParts.intValue();
                                        String enumString = "";
                                        for (int i = 1; i <= enumNum; i++) {
                                            enumString = enumString + i;
                                            int index = i;
                                            if (index++ != enumNum) {
                                                enumString = enumString + ",";
                                            }
                                        }
                                        List<OleCopy> oleCopies = item.getCopyList();
                                        for (int itemNo = 1, copyNumber = 1, partNumber = 1; itemNo <= noOfItems.intValue(); itemNo++, copyNumber++) {
                                            OleCopy oleCopy = new OleCopy();
                                            oleCopy.setLocation(item.getLocationCopies());
                                            //volNum = volChar.size()>enumCount ? volChar.get(enumCount):"";
                                            oleCopy.setEnumeration(item.getCaption() + " ");
                                            oleCopy.setReceiptStatus(OLEConstants.OleLineItemReceiving.NOT_RECEIVED_STATUS);
                                            oleCopy.setLocation(item.getItemLocation());
                                            oleCopy.setBibId(item.getItemTitleId());
                                            if (StringUtils.isNotBlank(item.getLinkToOrderOption()) && (item.getLinkToOrderOption().equals(OLEConstants.NB_PRINT) || item.getLinkToOrderOption().equals(OLEConstants.EB_PRINT))) {
                                                oleCopy.setCopyNumber(item.getSingleCopyNumber() != null && !item.getSingleCopyNumber().isEmpty() ? item.getSingleCopyNumber() : null);
                                            }
                                            oleCopy.setReceiptStatus(OLEConstants.OleLineItemReceiving.NOT_RECEIVED_STATUS);
                                            oleCopy.setReqDocNum(olePurchaseOrderDocument.getPurapDocumentIdentifier());
                                            oleCopy.setReqItemId(item.getItemIdentifier());
                                            oleCopy.setPoDocNum(olePurchaseOrderDocument.getDocumentNumber());
                                            oleCopy.setPoItemId(olePurchaseOrderItem.getItemIdentifier());
                                            oleCopy.setEnumeration(enumString + " " + copyNumber);
                                            oleCopy.setCopyNumber(copyNumber + "");
                                            oleCopy.setPartNumber(partNumber + "");
                                            oleCopies.add(oleCopy);
                                            if (copyNumber == copyNumberMax) {
                                                copyNumber = 0;
                                                partNumber++;
                                            }

                                        }
                                        Map map = new HashMap();
                                        map.put("purchaseOrderItemLineId", olePurchaseOrderDocument.getPurapDocumentIdentifier());
                                        List<ItemRecord> itemRecords = (List<ItemRecord>) KRADServiceLocator.getBusinessObjectService().findMatching(ItemRecord.class, map);
                                        if (CollectionUtils.isNotEmpty(itemRecords)) {
                                            if (itemRecords.size() == oleCopies.size()) {
                                                int index = 0;
                                                for (OleCopy oleCopy : oleCopies) {
                                                    oleCopy.setItemUUID(DocumentUniqueIDPrefix.PREFIX_WORK_ITEM_OLEML + "-" + itemRecords.get(index).getItemId());
                                                    oleCopy.setInstanceId(DocumentUniqueIDPrefix.PREFIX_WORK_HOLDINGS_OLEML + "-" + itemRecords.get(index).getHoldingsId());
                                                    index++;
                                                }
                                                saveOleCopies(oleCopies, buffer);
                                            } else {
                                                LOG.error("In Purchase order different purchase orderItem shouldn't use same bib id ");
                                                buffer.append("In Purchase order different purchase orderItem shouldn't use same bib id ");
                                                buffer.append("\n");
                                            }
                                        }

                                    }
                                }
                            } catch (Exception e) {
                                LOG.error(e);
                                buffer.append("Error occurred while prepareAndSaveOleCopy " + e.getMessage());
                                buffer.append("\n");
                            }
                        }
                    } catch (Exception e) {
                        if (LOG.isInfoEnabled()) {
                            LOG.info("###########Exception while preparing  copy record  for save###########");
                        }
                        LOG.error(e);
                        buffer.append("###########Exception while preparing  copy record  for save###########");
                        buffer.append("\n");
                        buffer.append(e.getStackTrace());
                        buffer.append("\n");
                    }
                }
            }
        }


    }

    public void writeStatusToFile(String directoryPath, String fileName, String content) {
        try {
            String fileSeparator = File.separator;
            Date date = new Date();
            File file = new File(directory);
            if (file.exists() && file.isDirectory()) {
                file = new File(directory + File.separator + fileName);
                file.createNewFile();

            } else {
                file.mkdir();
                File newFile = file = new File(directory + File.separator + fileName);
                if (!newFile.isDirectory() && !newFile.exists()) {
                    newFile.createNewFile();
                }
            }
            FileWriter fw = new FileWriter(directoryPath + fileSeparator + fileName, true);
            fw.write("\n");
            fw.write("*************************************************************************");
            fw.write("\n");
            fw.write("<!-----------------   " + date.toString() + "  ------------------!>");
            fw.write("\n");
            fw.write(content);
            fw.write("\n");
            fw.write("*************************************************************************");
            fw.write("\n");
            fw.close();
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }

}
