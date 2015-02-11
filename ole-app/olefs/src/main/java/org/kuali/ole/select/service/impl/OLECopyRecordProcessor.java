package org.kuali.ole.select.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jfree.util.Log;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.common.search.SearchResult;
import org.kuali.ole.docstore.common.search.SearchResultField;
import org.kuali.ole.docstore.common.util.DataSource;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.select.bo.OLELinkPurapDonor;
import org.kuali.ole.select.businessobject.OleCopy;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.businessobject.OleRequisitionItem;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.select.document.OleRequisitionDocument;
import org.kuali.ole.select.document.service.OleDocstoreHelperService;
import org.kuali.ole.select.document.service.impl.OleDocstoreHelperServiceImpl;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.batch.AbstractStep;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


/**
 * Created by pvsubrah on 12/19/14.
 */
public class OLECopyRecordProcessor extends AbstractStep {
    protected static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OLECopyRecordProcessor.class);
    private static Connection connection = null;
    private DocstoreClientLocator docstoreClientLocator;
    private int batchSize;
    private OleDocstoreHelperServiceImpl oleDocstoreHelperService;

    public OleDocstoreHelperServiceImpl getOleDocstoreHelperService() {
        if(oleDocstoreHelperService==null){
            this.oleDocstoreHelperService = (OleDocstoreHelperServiceImpl) SpringContext.getBean(OleDocstoreHelperService.class);
        }
        return oleDocstoreHelperService;
    }

    public void setOleDocstoreHelperService(OleDocstoreHelperServiceImpl oleDocstoreHelperService) {
        this.oleDocstoreHelperService=oleDocstoreHelperService;
    }

    @Override
    public boolean execute(String jobName, Date jobRunDate) throws InterruptedException {
        Boolean jobSuccess;
        jobSuccess = createCopyRecords();
        return jobSuccess;
    }

    int copyCount = 0;
    public DocstoreClientLocator getDocstoreClientLocator() {
        if (null == docstoreClientLocator) {
            return SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    private static Connection getConnection() throws SQLException {
        DataSource dataSource = null;
        try {
            dataSource = DataSource.getInstance();
        } catch (IOException e) {

        } catch (SQLException e) {

        } catch (PropertyVetoException e) {

        }
        return dataSource.getConnection();
    }

    public List<String> getPurchaseOrderFDocNoList() {
        String schemaName = ConfigContext.getCurrentContextConfig().getProperty("jdbc.username");
        String sql = "select po.fdoc_nbr from " + schemaName + ".pur_po_t po left join " + schemaName + ".ole_copy_t cp on (po.fdoc_nbr = cp.fdoc_nbr) where cp.fdoc_nbr is null ";
        Set<String> result = new HashSet<>();
        try {
                connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                result.add(rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List searchResult = new ArrayList<>();
        searchResult.addAll(result);
        return searchResult;

    }

    public Boolean createCopyRecords() {
        Long b1 = System.currentTimeMillis();
        List<String> docList = getPurchaseOrderFDocNoList();
        //int batchSize = getBatchSize();
        int batchSize = 5000;
        int noOfIteration = docList.size() / batchSize;
        int noOfRecordRemaining = docList.size() % batchSize;
        if (noOfRecordRemaining < batchSize) {
            noOfIteration = noOfIteration + 1;
        }

        int startIndex = 1;
        int endIndex = batchSize;
        if (docList.size() < batchSize) {
            createCopies(docList);
        } else {
            for (int i = 1; i <= noOfIteration; i++) {
                if (i > 1) {
                    startIndex = endIndex + 1;
                    if (i == noOfIteration) {
                        endIndex = startIndex + noOfRecordRemaining - 1;
                    } else {
                        endIndex = startIndex + batchSize - 1;
                    }
                }
                createCopies(docList.subList(startIndex, endIndex));
                Log.info("Processing records with starting index: " + startIndex + " and ending index: " + endIndex);
            }
        }
        Long b2 = System.currentTimeMillis();
        LOG.info("Total time taken" + (b2 - b1));
        try {
           connection.close();
        } catch (Exception e){
            LOG.error("While closing the database connection error occurred "+e);
        }
        return Boolean.TRUE;

    }

    public void createCopies(List<String> docList) {
        for (String docNum : docList) {
            Map poMap = new HashMap();
            poMap.put("documentNumber", docNum);
            List<OlePurchaseOrderDocument> olePurchaseOrderDocuments = (List<OlePurchaseOrderDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePurchaseOrderDocument.class, poMap);
            HashMap<String, String> copyMap = new HashMap<String, String>();
            copyMap.put("poDocNum", docNum);
            List<OleCopy> copies = (List<OleCopy>) KRADServiceLocator.getBusinessObjectService().findMatching(OleCopy.class, copyMap);
            // checking the OlePurchaseOrderDocument  FDOC_NBR against the copy records
            if (CollectionUtils.isNotEmpty((olePurchaseOrderDocuments)) && CollectionUtils.isEmpty((copies))) {
                try {
                    OlePurchaseOrderDocument olePurchaseOrderDocument = olePurchaseOrderDocuments.get(0);
                    OleRequisitionDocument oleRequisitionDocument = getRequisitionDocument(olePurchaseOrderDocument.getRequisitionIdentifier());
                    LOG.info("#REQ DOC NO :" + ((oleRequisitionDocument != null) ? oleRequisitionDocument.getDocumentNumber() : "null") + "  ######  " + "PO DOC NO :" + olePurchaseOrderDocument.getDocumentNumber());
                    List<OlePurchaseOrderItem> olePurchaseOrderItems = (List<OlePurchaseOrderItem>) olePurchaseOrderDocument.getItems();
                    if (CollectionUtils.isNotEmpty(olePurchaseOrderItems)) {
                        for (OlePurchaseOrderItem item : olePurchaseOrderItems) {
                            if (item.getItemLineNumber() != null) {
                                if (null != item.getItemType() && item.getItemType().isQuantityBasedGeneralLedgerIndicator()) {
                                    if (item.getItemQuantity() != null && item.getItemNoOfParts() != null && !item.getItemQuantity().isGreaterThan(new KualiDecimal(1))
                                            && !item.getItemNoOfParts().isGreaterThan(new KualiInteger(1))) {
                                        List<OleCopy> copyList = new ArrayList<>();
                                        OleCopy oleCopy = new OleCopy();
                                        oleCopy.setLocation(item.getItemLocation());
                                        oleCopy.setBibId(item.getItemTitleId());
                                        if (StringUtils.isNotBlank(item.getLinkToOrderOption()) && (item.getLinkToOrderOption().equals(OLEConstants.NB_PRINT) || item.getLinkToOrderOption().equals(OLEConstants.EB_PRINT))) {
                                            oleCopy.setCopyNumber(item.getSingleCopyNumber() != null && !item.getSingleCopyNumber().isEmpty() ? item.getSingleCopyNumber() : null);
                                        }
                                        oleCopy.setReceiptStatus(OLEConstants.OleLineItemReceiving.NOT_RECEIVED_STATUS);
                                        oleCopy.setReqDocNum(olePurchaseOrderDocument.getRequisitionIdentifier());

                                        //oleCopy.setReqItemId(oleRequisitionItem.getItemIdentifier());
                                        if (oleRequisitionDocument != null) {
                                            oleCopy.setReqDocNum(oleRequisitionDocument.getPurapDocumentIdentifier());
                                            if (CollectionUtils.isNotEmpty(oleRequisitionDocument.getItems())) {
                                                OleRequisitionItem oleRequisitionItem = getOleRequisitionItemByLineNBR(oleRequisitionDocument.getItems(), item.getItemLineNumber());
                                                if (oleRequisitionItem != null) {
                                                    oleCopy.setReqItemId(oleRequisitionItem.getItemIdentifier());
                                                }

                                            }
                                        }
                                        oleCopy.setPoDocNum(olePurchaseOrderDocument.getDocumentNumber());
                                        oleCopy.setPoItemId(item.getItemIdentifier());
                                        copyList.add(oleCopy);
                                        Map itemMap = new HashMap();
                                        itemMap.clear();
                                        itemMap.put("purchaseOrderItemLineId", olePurchaseOrderDocument.getPurapDocumentIdentifier());
                                        List<ItemRecord> itemRecords = (List<ItemRecord>) KRADServiceLocator.getBusinessObjectService().findMatching(ItemRecord.class, itemMap);
                                        if (CollectionUtils.isNotEmpty(itemRecords)) {
                                            oleCopy.setItemUUID(DocumentUniqueIDPrefix.PREFIX_WORK_ITEM_OLEML + "-" + itemRecords.get(0).getItemId());
                                            oleCopy.setInstanceId(DocumentUniqueIDPrefix.PREFIX_WORK_HOLDINGS_OLEML + "-" + itemRecords.get(0).getHoldingsId());
                                            oleCopy.setLocation(getLocation(oleCopy.getItemUUID()));
                                        } else {
                                            Bib bib = new BibMarc();
                                            bib.setId(item.getItemTitleId());
                                            String poNumber = olePurchaseOrderDocument.getPurapDocumentIdentifier() != null ? olePurchaseOrderDocument.getPurapDocumentIdentifier().toString() : null;
                                            String poLineItemId = item.getItemIdentifier() != null ? item.getItemIdentifier().toString() : null;
                                            updateOleCopies(poNumber, copyList, item, null, poLineItemId,item.getItemTitleId());
                                        }
                                        saveOleCopies(copyList);
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
                                        int noOfPartMax=noOfParts.intValue();
                                        List<OleCopy> oleCopies = item.getCopyList();
                                        for (int itemNo = 1, partNumber = 1; itemNo <= noOfItems.intValue(); itemNo++,partNumber++) {
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
                                            if (oleRequisitionDocument != null) {
                                                oleCopy.setReqDocNum(oleRequisitionDocument.getPurapDocumentIdentifier());
                                                if (CollectionUtils.isNotEmpty(oleRequisitionDocument.getItems())) {
                                                    OleRequisitionItem oleRequisitionItem = getOleRequisitionItemByLineNBR(oleRequisitionDocument.getItems(), item.getItemLineNumber());
                                                    if (oleRequisitionItem != null) {
                                                        oleCopy.setReqItemId(oleRequisitionItem.getItemIdentifier());
                                                    }

                                                }
                                            }
                                            oleCopy.setPoDocNum(olePurchaseOrderDocument.getDocumentNumber());
                                            oleCopy.setPoItemId(item.getItemIdentifier());
                                            oleCopy.setEnumeration(String.valueOf(partNumber));
                                            oleCopy.setCopyNumber(String.valueOf(1));
                                            oleCopy.setPartNumber(String.valueOf(partNumber));
                                            oleCopies.add(oleCopy);
                                            if (partNumber == noOfPartMax) {
                                                partNumber=1;
                                            }

                                        }
                                        Map itemMap = new HashMap();
                                        itemMap.clear();
                                        itemMap.put("purchaseOrderItemLineId", olePurchaseOrderDocument.getPurapDocumentIdentifier());
                                        List<ItemRecord> itemRecords = (List<ItemRecord>) KRADServiceLocator.getBusinessObjectService().findMatching(ItemRecord.class, itemMap);
                                        if (CollectionUtils.isNotEmpty(itemRecords)) {
                                            // if item record in  doc-store side and  no of copy records matches its just updating copy record and going for save
                                            if (itemRecords.size() == oleCopies.size() || itemRecords.size()>oleCopies.size()) {
                                                int index = 0;
                                                for (OleCopy oleCopy : oleCopies) {
                                                    oleCopy.setItemUUID(DocumentUniqueIDPrefix.PREFIX_WORK_ITEM_OLEML + "-" + itemRecords.get(index).getItemId());
                                                    oleCopy.setInstanceId(DocumentUniqueIDPrefix.PREFIX_WORK_HOLDINGS_OLEML + "-" + itemRecords.get(index).getHoldingsId());
                                                    oleCopy.setLocation(getLocation(oleCopy.getItemUUID()));
                                                    index++;
                                                }
                                                saveOleCopies(oleCopies);
                                            } else {
                                                // if item record in  doc-store side and  no of copy records not  matches , first its going for updating copy record  which hav item record
                                                // then which doesn't have item record its going to create then saving the record
                                                int index = 0;
                                                for(OleCopy oleCopy:oleCopies){
                                                    if(itemRecords.size()<oleCopies.size() && index < itemRecords.size()){
                                                        ItemRecord itemRecord = index < itemRecords.size() ? itemRecords.get(index) : itemRecords.get(0);
                                                        oleCopy.setItemUUID(DocumentUniqueIDPrefix.PREFIX_WORK_ITEM_OLEML + "-" + itemRecord.getItemId());
                                                        oleCopy.setInstanceId(DocumentUniqueIDPrefix.PREFIX_WORK_HOLDINGS_OLEML + "-" + itemRecord.getHoldingsId());
                                                        oleCopy.setLocation(getLocation(oleCopy.getItemUUID()));
                                                    }
                                                    index++;
                                                }
                                                Bib bib = new BibMarc();
                                                bib.setId(item.getItemTitleId());
                                                String poNumber = olePurchaseOrderDocument.getPurapDocumentIdentifier() != null ? olePurchaseOrderDocument.getPurapDocumentIdentifier().toString() : null;
                                                String poLineItemId = item.getItemIdentifier() != null ? item.getItemIdentifier().toString() : null;
                                                updateOleCopies(poNumber,oleCopies,item,null,poLineItemId,item.getItemTitleId());
                                                saveOleCopies(oleCopies);
                                            }

                                        } else {
                                            // if item record not there at all it will create item record
                                            Bib bib = new BibMarc();
                                            bib.setId(item.getItemTitleId());
                                            String poNumber = olePurchaseOrderDocument.getPurapDocumentIdentifier() != null ? olePurchaseOrderDocument.getPurapDocumentIdentifier().toString() : null;
                                            String poLineItemId = item.getItemIdentifier() != null ? item.getItemIdentifier().toString() : null;
                                            updateOleCopies(poNumber,oleCopies,item,null,poLineItemId,item.getItemTitleId());
                                            saveOleCopies(oleCopies);
                                        }
                                    }

                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("while processing the olepurchase order record for creation of copy record" + e.getStackTrace());
                }
            }
        }
    }


    public OleRequisitionDocument getRequisitionDocument(Integer itemIdentifier) {
        if (itemIdentifier != null) {
            Map map = new HashMap();
            map.put("purapDocumentIdentifier", itemIdentifier);
            List<OleRequisitionDocument> oleRequisitionDocuments = (List<OleRequisitionDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OleRequisitionDocument.class, map);
            if (CollectionUtils.isNotEmpty(oleRequisitionDocuments)) {
                return oleRequisitionDocuments.get(0);
            }
        }
        return null;
    }

    public OleRequisitionItem getOleRequisitionItemByLineNBR(List<OleRequisitionItem> oleRequisitionItems, Integer lineNumber) {
        for (OleRequisitionItem item : oleRequisitionItems) {
            if (item.getItemLineNumber() != null && item.getItemLineNumber().equals(lineNumber)) {
                return item;
            }
        }

        return null;
    }

    public String getLocation(String itemUUID) {
        String location = "";
        String itemLocation = "";
        String holdingLoc = "";
        try {
            org.kuali.ole.docstore.common.document.Item item = new ItemOleml();
            org.kuali.ole.docstore.common.search.SearchParams search_Params = new org.kuali.ole.docstore.common.search.SearchParams();
            SearchResponse searchResponse = null;
            search_Params.getSearchConditions().add(search_Params.buildSearchCondition("phrase", search_Params.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), Item.ITEMIDENTIFIER, itemUUID), ""));
            search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), "Location_display"));
            search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(DocType.HOLDINGS.getCode(), "Location_display"));
            searchResponse = getDocstoreClientLocator().getDocstoreClient().search(search_Params);
            for (SearchResult searchResult : searchResponse.getSearchResults()) {
                for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                    String fieldName = searchResultField.getFieldName();
                    String fieldValue = searchResultField.getFieldValue() != null ? searchResultField.getFieldValue() : "";
                    if (StringUtils.isNotBlank(fieldName) && fieldName.equalsIgnoreCase("Location_display") && searchResultField.getDocType().equalsIgnoreCase(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode())) {
                        location = fieldValue;
                    } else if (searchResultField.getFieldName().equalsIgnoreCase("Location_display") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("holdings")) {
                        holdingLoc = searchResultField.getFieldValue();
                    } else {

                    }
                }
            }
        } catch (Exception ex) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, "Item Exists");
            LOG.error(org.kuali.ole.OLEConstants.ITEM_EXIST + ex);
        }
        if (StringUtils.isNotBlank(itemLocation)) {
            return itemLocation;
        } else {
            return holdingLoc;
        }
    }


    public void saveOleCopies(List<OleCopy> oleCopies) {
        try {

            KRADServiceLocator.getBusinessObjectService().save(oleCopies);

        } catch (Exception e) {
            LOG.error("Error occurred : while updating the copies for requisition document" + e.getMessage() + " " + e);
        }
    }


    public int getBatchSize() {
        ParameterKey batchSizeParameterKey = ParameterKey.create(org.kuali.ole.OLEConstants.APPL_ID_OLE, org.kuali.ole.OLEConstants.SYS_NMSPC, org.kuali.ole.OLEConstants.BATCH_CMPNT, "PO_RELINK_COPY_RECORD_BATCH_SIZE");
        Parameter batchSizeParameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(batchSizeParameterKey);
        String value = batchSizeParameter.getValue();
        return new Integer(value).intValue();
    }


    private void updateOleCopies(String poNumber, List<OleCopy> oleCopyList, OlePurchaseOrderItem singleItem,
                                 BibTree bibTree, String poLineItemId,String bibid) throws Exception {
        if (oleCopyList != null) {
            boolean holdingsExists = false;
            for (OleCopy oleCopy : oleCopyList) {
                if (oleCopy != null && oleCopy.getInstanceId() != null) {
                    holdingsExists = true;
                    Holdings holdings = getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(oleCopy.getInstanceId());
                    org.kuali.ole.docstore.common.document.content.instance.OleHoldings oleHoldings = new HoldingOlemlRecordProcessor().fromXML(holdings.getContent());
                    oleHoldings.setReceiptStatus(OLEConstants.OleLineItemReceiving.NOT_RECEIVED_STATUS);
                    oleHoldings.setLocation(getOleDocstoreHelperService().setHoldingDetails(oleCopy).getLocation());
                    holdings.setContent(new HoldingOlemlRecordProcessor().toXML(oleHoldings));
                    getDocstoreClientLocator().getDocstoreClient().updateHoldings(holdings);
                    if (oleCopy.getItemUUID() != null) {
                        org.kuali.ole.docstore.common.document.Item item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(oleCopy.getItemUUID());
                        org.kuali.ole.docstore.common.document.content.instance.Item itemContent = new ItemOlemlRecordProcessor().fromXML(item.getContent());
                        getOleDocstoreHelperService().setItemDetails(itemContent, oleCopy, singleItem, new ArrayList<OLELinkPurapDonor>(), poNumber);
                        item.setContent(new ItemOlemlRecordProcessor().toXML(itemContent));
                        item.setId(itemContent.getItemIdentifier());
                        getDocstoreClientLocator().getDocstoreClient().updateItem(item);
                    } else {
                        org.kuali.ole.docstore.common.document.content.instance.Item item = new org.kuali.ole.docstore.common.document.content.instance.Item();
                        getOleDocstoreHelperService().setItemDetails(item, oleCopy, singleItem, new ArrayList<OLELinkPurapDonor>(), poNumber);
                        org.kuali.ole.docstore.common.document.Item itemDocument = new org.kuali.ole.docstore.common.document.Item();
                        itemDocument.setContent(new ItemOlemlRecordProcessor().toXML(item));
                        itemDocument.setCategory(OLEConstants.ITEM_CATEGORY);
                        itemDocument.setType(OLEConstants.ITEM_TYPE);
                        itemDocument.setFormat(OLEConstants.ITEM_FORMAT);
                        if (org.apache.commons.lang3.StringUtils.isNotBlank(oleCopy.getInstanceId())) {
                            itemDocument.setHolding(holdings);
                            getDocstoreClientLocator().getDocstoreClient().createItem(itemDocument);
                        }
                        oleCopy.setItemUUID(itemDocument.getId());
                    }
                }
            }
            if (!holdingsExists) {
                createOleHoldingsTree(poNumber, poLineItemId, oleCopyList, singleItem.getItemTypeDescription(), singleItem.getItemStatus(), singleItem,bibid);
            }
        }
    }

    public void createOleHoldingsTree(String poNumber, String poLineItemId,List<OleCopy> oleCopyList,
                                      String itemTypeDescription,String itemStatusValue, OlePurchaseOrderItem singleItem,String bibid) throws Exception {
        OleCopy copy = oleCopyList.get(0);
        Holdings pHoldings = new PHoldings();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(copy.getInstanceId())) {
            pHoldings = getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(copy.getInstanceId());
        }
        List<org.kuali.ole.docstore.common.document.Item> itemList = new ArrayList<org.kuali.ole.docstore.common.document.Item>();
        org.kuali.ole.docstore.common.document.content.instance.OleHoldings oleHoldings = getOleDocstoreHelperService().setHoldingDetails(copy);
        oleHoldings.setHoldingsIdentifier(null);
        oleHoldings.setReceiptStatus(OLEConstants.OleLineItemReceiving.NOT_RECEIVED_STATUS);
        List<Integer> copyIdList = new ArrayList<>();
        for (OleCopy oleCopy : oleCopyList) {
            // item details
            org.kuali.ole.docstore.common.document.content.instance.Item item = getOleDocstoreHelperService().setItemDetails(oleCopy, itemTypeDescription);
            item.setPurchaseOrderLineItemIdentifier(poNumber);
            if (singleItem != null) {
                String vendorItemPoNumber = singleItem.getVendorItemPoNumber();
                item.setVendorLineItemIdentifier(vendorItemPoNumber);
                if (singleItem.getExtendedPrice() != null) {
                    item.setPrice(singleItem.getExtendedPrice().toString());
                }

            }
            org.kuali.ole.docstore.common.document.Item itemDocument = new org.kuali.ole.docstore.common.document.Item();
            itemDocument.setStaffOnly(true);
            itemDocument.setContent(new ItemOlemlRecordProcessor().toXML(item));
            itemDocument.setCategory(OLEConstants.ITEM_CATEGORY);
            itemDocument.setType(OLEConstants.ITEM_TYPE);
            itemDocument.setFormat(OLEConstants.ITEM_FORMAT);
            if (org.apache.commons.lang3.StringUtils.isNotBlank(copy.getInstanceId())) {
                itemDocument.setHolding(pHoldings);
                getDocstoreClientLocator().getDocstoreClient().createItem(itemDocument);
            } else {
                itemList.add(itemDocument);
            }
            saveOleCopies(oleCopyList);
            copyIdList.add(oleCopy.getCopyId());

        }
        HoldingsTree holdingsTree = new HoldingsTree();
        if (org.apache.commons.lang3.StringUtils.isBlank(copy.getInstanceId())) {
            holdingsTree.getItems().addAll(itemList);
            Holdings holdings = new PHoldings();
            holdings.setCategory(org.kuali.ole.docstore.common.document.content.enums.DocCategory.WORK.getCode());
            holdings.setType(org.kuali.ole.docstore.common.document.content.enums.DocType.HOLDINGS.getCode());
            holdings.setFormat(org.kuali.ole.docstore.common.document.content.enums.DocFormat.OLEML.getCode());
            holdings.setContent(new HoldingOlemlRecordProcessor().toXML(oleHoldings));
            Bib bib = getDocstoreClientLocator().getDocstoreClient().retrieveBib(bibid);
            holdings.setBib(bib);
            holdingsTree.setHoldings(holdings);
            getDocstoreClientLocator().getDocstoreClient().createHoldingsTree(holdingsTree);
        } else {
            holdingsTree = getDocstoreClientLocator().getDocstoreClient().retrieveHoldingsTree(copy.getInstanceId());
        }
        int i = 0;
        for (org.kuali.ole.docstore.common.document.Item item : holdingsTree.getItems()) {
            for (OleCopy oleCopy : oleCopyList) {
                if (copyIdList != null && copyIdList.size() > i && copyIdList.get(i).equals(oleCopy.getCopyId())) {
                    oleCopy.setInstanceId(holdingsTree.getHoldings().getId());
                    oleCopy.setItemUUID(item.getId());
                    this.copyCount++;
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Instance UUID" + holdingsTree.getHoldings().getId() + "****** Item UUID" + item.getId());
                    }
                }
            }
            i++;

        }
    }






}
