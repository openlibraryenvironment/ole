package org.kuali.ole.select.document.service.impl;

import com.google.common.base.CharMatcher;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.coa.businessobject.Account;
import org.kuali.ole.coa.businessobject.Chart;
import org.kuali.ole.coa.businessobject.ObjectCode;
import org.kuali.ole.coa.service.AccountService;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.module.purap.businessobject.PurApAccountingLine;

import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.select.document.OLEPurchaseOrderBatchDocument;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.select.document.service.OLEPurchaseOrderBatchService;
import org.kuali.ole.select.document.service.OlePurchaseOrderService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.vnd.businessobject.VendorAddress;
import org.kuali.ole.vnd.businessobject.VendorDetail;
import org.kuali.rice.core.api.config.property.ConfigContext;

import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.bo.AdHocRoutePerson;
import org.kuali.rice.krad.exception.ValidationException;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: gopalp
 * Date: 5/18/15
 * Time: 12:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEPurchaseOrderBatchServiceImpl implements OLEPurchaseOrderBatchService {

    protected static final Logger LOG = Logger.getLogger(OLEPurchaseOrderBatchServiceImpl.class);
    private final String FILE_HEADER = "documentNumber,documentDescription,purapDocumentIdentifier," +
            "vendorHeaderGeneratedIdentifier,vendorDetailAssignedIdentifier,assignedUserPrincipalName," +
            "recurringPaymentTypeCode,poBeginDate,poEndDate,deliveryCampusCode,deliveryBuildingCode," +
            "deliveryBuildingLine1Address,deliveryBuildingLine2Address,deliveryBuildingRoomNumber,deliveryStateCode," +
            "deliveryPostalCode,deliveryCountryCode,deliveryToEmailAddress,deliveryToName,deliveryToPhoneNumber," +
            "deliveryBuildingCode,deliveryInstructionText,itemLineNumber,itemCopyNumber,format,category," +
            "itemPriceSource,requestSource,itemQuantity,itemNoOfParts,itemListPrice,itemPublicViewIndicator," +
            "doNotClaim,itemRouteToRequestorIndicator,itemLocation,vendorItemPoNumber,accountingLineIndex," +
            "chartOfAccountsCode,accountNumber,financialObjectCode,accountLinePercent";

    List<Chart> charts = new ArrayList<>();
    List<Account> accounts = new ArrayList<>();
    List<ObjectCode> objects = new ArrayList<>();
    List<VendorDetail> vendorDetails = new ArrayList<VendorDetail>();
    List<OleCategory> categories = new ArrayList<OleCategory>();
    List<OleFormatType> formatTypes = new ArrayList<OleFormatType>();
    List<OleItemPriceSource> itemPriceSources = new ArrayList<OleItemPriceSource>();
    List<OleRequestSourceType> requestSourceTypes = new ArrayList<OleRequestSourceType>();
    List<OleLocation> locations = new ArrayList<OleLocation>();
    List<String> chartList = new ArrayList<>();
    List<String> accountList = new ArrayList<>();
    List<String> objectCodeList = new ArrayList<>();
    List<String> vendorDetailList = new ArrayList<>();
    List<String> categoryList = new ArrayList<>();
    List<String> formatList = new ArrayList<>();
    List<String> requestSourceList = new ArrayList<>();
    List<String> priceSourceList = new ArrayList<>();
    List<String> locationList = new ArrayList<>();
    StringBuffer message;
    public Integer poNumber = 0;
    private FileWriter fileWriter = null;
    private OlePurchaseOrderService olePurchaseOrderService;
    private OlePurchaseOrderDocument purchaseOrderDocument = null;
    private String docNumber = "";

    private BusinessObjectService businessObjectService;
    private String fileSeparator = System.getProperty("file.separator");

    public void readFile(OLEPurchaseOrderBatchDocument olePurchaseOrderBatchDocument, UserSession userSession, File file) {
        GlobalVariables.setUserSession(userSession);
        charts = (List<Chart>) getBusinessObjectService().findAll(Chart.class);
        accounts = (List<Account>) getBusinessObjectService().findAll(Account.class);
        objects = (List<ObjectCode>) getBusinessObjectService().findAll(ObjectCode.class);
        vendorDetails = (List<VendorDetail>) getBusinessObjectService().findAll(VendorDetail.class);
        categories = (List<OleCategory>) getBusinessObjectService().findAll(OleCategory.class);
        formatTypes = (List<OleFormatType>) getBusinessObjectService().findAll(OleFormatType.class);
        itemPriceSources = (List<OleItemPriceSource>) getBusinessObjectService().findAll(OleItemPriceSource.class);
        requestSourceTypes = (List<OleRequestSourceType>) getBusinessObjectService().findAll(OleRequestSourceType.class);
        locations = (List<OleLocation>) getBusinessObjectService().findAll(OleLocation.class);
        for (Chart chart : (List<Chart>) charts) {
            chartList.add(chart.getChartOfAccountsCode());
        }
        for (Account account : (List<Account>) accounts) {
            accountList.add(account.getAccountNumber() + account.getChartOfAccountsCode());
        }
        for (ObjectCode objectCode : (List<ObjectCode>) objects) {
            objectCodeList.add(objectCode.getFinancialObjectCode() + objectCode.getChartOfAccountsCode());
        }
        for (VendorDetail vendorDetail : vendorDetails) {
            vendorDetailList.add(vendorDetail.getVendorHeaderGeneratedIdentifier().toString());
        }
        for (OleCategory category : categories) {
            categoryList.add(category.getCategory());
        }
        for (OleFormatType formatType : formatTypes) {
            formatList.add(formatType.getFormatTypeName());
        }
        for (OleItemPriceSource itemPriceSource : itemPriceSources) {
            priceSourceList.add(itemPriceSource.getItemPriceSource());
        }
        for (OleRequestSourceType requestSourceType : requestSourceTypes) {
            requestSourceList.add(requestSourceType.getRequestSourceType());
        }
        for (OleLocation location : locations) {
            locationList.add(location.getLocationCode());
        }
        try {
            message = new StringBuffer();


            List<Map> list = readCSVAndPreparePOs(file);

            message.append("\n");
            message.append("Starting Purchase Order Bulk Amendment Document :" + olePurchaseOrderBatchDocument.getDocumentNumber());
            message.append("\n");
            message.append("\n");
            OlePurchaseOrderDocument purchaseOrderDocument = null;
            String poId = "";
            for (int index = 0; index < list.size(); index++) {
                if (index == 0) {
                    poId = list.get(index).get(OLEConstants.OLEPurchaseOrderBulkAmendment.PUR_AP_ID).toString();
                }
                if (list.get(index).get(OLEConstants.OLEPurchaseOrderBulkAmendment.PUR_AP_ID).toString().equals(poId)) {
                    poId = list.get(index).get(OLEConstants.OLEPurchaseOrderBulkAmendment.PUR_AP_ID).toString();
                    if (validatePOFields(list.get(index))) {
                        message.append("Started creating POA document for PO# : " + list.get(index).get(OLEConstants.OLEPurchaseOrderBulkAmendment.PUR_AP_ID));
                        message.append("\n");
                        purchaseOrderDocument = createPurchaseOrderAmendmentDocument(list.get(index), olePurchaseOrderBatchDocument.getDocumentNumber());
                    } else {
                        message.append("Creation of POA Document failed for PO# " + list.get(index).get(OLEConstants.OLEPurchaseOrderBulkAmendment.PUR_AP_ID) + " and Item Line Number :" + list.get(index).get(OLEConstants.OLEPurchaseOrderBulkAmendment.ITM_LIN_NO));
                        message.append("\n");
                        message.append("\n");
                    }
                } else if (purchaseOrderDocument != null) {

                    try {
                        if (!(poNumber.equals(purchaseOrderDocument.getPurapDocumentIdentifier()))) {
                            String errorMessage = getOlePurchaseOrderService().createPurchaseOrderAmendmentDocument(purchaseOrderDocument, olePurchaseOrderBatchDocument.getDocumentNumber());
                            if (StringUtils.isNotEmpty(errorMessage)) {
                                message.append(errorMessage);
                                message.append("Failed to create POA document for PO# : " + purchaseOrderDocument.getPurapDocumentIdentifier());
                                message.append("\n");
                            } else {
                                message.append("Successfully created POA document for PO# : " + purchaseOrderDocument.getPurapDocumentIdentifier());
                                message.append("\n");
                            }
                        }
                    } catch (Exception e) {
                        message.append("Creation of POA Document failed for PO Doc# " + purchaseOrderDocument.getDocumentNumber());
                        message.append("\n");
                    }
                    purchaseOrderDocument = null;
                    poId = list.get(index).get(OLEConstants.OLEPurchaseOrderBulkAmendment.PUR_AP_ID).toString();
                    if (list.get(index).get(OLEConstants.OLEPurchaseOrderBulkAmendment.PUR_AP_ID).toString().equals(poId)) {
                        if (validatePOFields(list.get(index))) {
                            message.append("Started creating POA document for PO# : " + list.get(index).get(OLEConstants.OLEPurchaseOrderBulkAmendment.PUR_AP_ID));
                            message.append("\n");
                            purchaseOrderDocument = createPurchaseOrderAmendmentDocument(list.get(index), olePurchaseOrderBatchDocument.getDocumentNumber());

                        } else {
                            message.append("Creation of POA Document failed for PO# " + list.get(index).get(OLEConstants.OLEPurchaseOrderBulkAmendment.PUR_AP_ID) + " and Item Line Number :" + list.get(index).get(OLEConstants.OLEPurchaseOrderBulkAmendment.ITM_LIN_NO));
                            message.append("\n");
                            message.append("\n");
                        }
                    }


                } else {
                    if (purchaseOrderDocument == null) {
                        poId = list.get(index).get(OLEConstants.OLEPurchaseOrderBulkAmendment.PUR_AP_ID).toString();
                        if (list.get(index).get(OLEConstants.OLEPurchaseOrderBulkAmendment.PUR_AP_ID).toString().equals(poId)) {
                            if (validatePOFields(list.get(index))) {
                                message.append("Started creating POA document for PO# : " + list.get(index).get(OLEConstants.OLEPurchaseOrderBulkAmendment.PUR_AP_ID));
                                message.append("\n");
                                purchaseOrderDocument = createPurchaseOrderAmendmentDocument(list.get(index), olePurchaseOrderBatchDocument.getDocumentNumber());

                            } else {
                                message.append("Creation of POA Document failed for PO# " + list.get(index).get(OLEConstants.OLEPurchaseOrderBulkAmendment.PUR_AP_ID) + " and Item Line Number :" + list.get(index).get(OLEConstants.OLEPurchaseOrderBulkAmendment.ITM_LIN_NO));
                                message.append("\n");
                                message.append("\n");
                            }
                        }

                    }
                }
            }
            if (purchaseOrderDocument != null) {
                try {
                    if (!(poNumber.equals(purchaseOrderDocument.getPurapDocumentIdentifier()))) {
                        String errorMessage = getOlePurchaseOrderService().createPurchaseOrderAmendmentDocument(purchaseOrderDocument, olePurchaseOrderBatchDocument.getDocumentNumber());
                        if (StringUtils.isNotEmpty(errorMessage)) {
                            message.append(errorMessage);
                            message.append("\n");
                            message.append("Failed to create POA document for PO# : " + purchaseOrderDocument.getPurapDocumentIdentifier());
                            message.append("\n");
                        } else {
                            message.append("Successfully created POA document for PO# : " + purchaseOrderDocument.getPurapDocumentIdentifier());
                            message.append("\n");
                        }
                    }
                } catch (Exception e) {
                    LOG.info("Error Occurred  " + e.getMessage());
                }
            }
            writeFile();
        } catch (Exception e) {
            message.append("Error Occurred while creating POA Document");
            message.append("\n");
        }

    }

    public List<Map> readCSVAndPreparePOs(File file)
            throws IOException {

        List<Map> list = new ArrayList<>();

        String fileName;
        if (file != null) {
            File csvData = file;
            CSVParser parser = CSVParser.parse(csvData, Charset.defaultCharset(), CSVFormat.EXCEL);
            Iterator<CSVRecord> iterator = parser.getRecords().iterator();
            CSVRecord headerData = iterator.next();
            while (iterator.hasNext()) {
                CSVRecord csvRecord = iterator.next();
                Map map = new HashMap();
                for (int x = 0; x < headerData.size(); x++) {
                    map.put(headerData.get(x), csvRecord.get(x));
                }
                list.add(map);
            }
        }
        return list;
    }

    public OlePurchaseOrderDocument createPurchaseOrderAmendmentDocument(Map map, String documentNumber) {
        String purapDocumentIdentifier = (String) map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.PUR_AP_ID);
        if (purapDocumentIdentifier != null) {
            if (!docNumber.equals(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.DOC_NO))) {
                Map poMap = new HashMap();
                poMap.put(OLEConstants.OLEPurchaseOrderBulkAmendment.PUR_AP_ID, purapDocumentIdentifier);
                purchaseOrderDocument = getPODocumentByPrimaryKey(poMap);
                docNumber = map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.DOC_NO).toString();
            }

            try {
                purchaseOrderDocument = updateDocument(purchaseOrderDocument, map);
                purchaseOrderDocument = updateVendor(purchaseOrderDocument, map);
                int lineNumber = Integer.parseInt(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ITM_LIN_NO).toString());
                purchaseOrderDocument = updateItem(purchaseOrderDocument, lineNumber, map);
                purchaseOrderDocument = updateAccount(purchaseOrderDocument, lineNumber, map);
            } catch (UnexpectedRollbackException re) {
                message.append(re.getMessage());
                message.append("\n");
            } catch (ValidationException ve) {
                message.append(ve.getMessage());
                message.append("\n");
            } catch (Exception ex) {
                message.append("Failed to create POA document for PO document number : " + purchaseOrderDocument.getDocumentNumber());
                message.append("\n");

            }
        }
//        sendFyiForAccount(purchaseOrderDocument);
        return purchaseOrderDocument;
    }


    public OlePurchaseOrderDocument updateItem(OlePurchaseOrderDocument purchaseOrderDocument, int itemLineNumber, Map map) throws Exception {
        for (OlePurchaseOrderItem olePurchaseOrderItem : purchaseOrderDocument.getItemsActiveOnly()) {
            if (olePurchaseOrderItem.getItemLineNumber() != null && olePurchaseOrderItem.getItemLineNumber() == itemLineNumber) {
                if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.VENDOR_ITEM_NO).equals("null")) {
                    olePurchaseOrderItem.setVendorItemPoNumber(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.VENDOR_ITEM_NO).toString());
                } else {
                    olePurchaseOrderItem.setVendorItemPoNumber("");
                }
                if (map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ITM_LOC) != null) {
                    olePurchaseOrderItem.setItemLocation(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ITM_LOC).toString());
                }
                if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ITM_LIST_PRC).equals("null")) {
                    olePurchaseOrderItem.setItemListPrice(new KualiDecimal(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ITM_LIST_PRC).toString()));
                }
                if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ITM_QTY).equals("null")) {
                    olePurchaseOrderItem.setItemQuantity(new KualiDecimal(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ITM_QTY).toString()));
                }
                if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ITM_NO_OF_PARTS).equals("null")) {
                    olePurchaseOrderItem.setItemNoOfParts(new KualiInteger(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ITM_NO_OF_PARTS).toString()));
                }
                if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ITEM_PUB_VIEW_IND).equals("null")) {
                    boolean indicator = Boolean.parseBoolean(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ITEM_PUB_VIEW_IND).toString());
                    olePurchaseOrderItem.setItemPublicViewIndicator(indicator);
                }
                if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.DO_NOT_CLAIM).equals("null")) {
                    boolean indicator = Boolean.parseBoolean(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.DO_NOT_CLAIM).toString());
                    olePurchaseOrderItem.setDoNotClaim(indicator);
                }
                if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ITEM_ROUTE_IND).equals("null")) {
                    boolean indicator = Boolean.parseBoolean(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ITEM_ROUTE_IND).toString());
                    olePurchaseOrderItem.setItemRouteToRequestorIndicator(indicator);
                }
                if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ITM_NO_OF_PARTS).equals("null")) {
                    olePurchaseOrderItem.setItemNoOfParts(new KualiInteger(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ITM_NO_OF_PARTS).toString()));
                }
                if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ITM_COPY_NO).equals("null")) {
                    olePurchaseOrderItem.setCopyNumber((map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ITM_COPY_NO).toString()));
                }
                if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.FORMAT_CD).equals("null")) {
                    if (getFormatId(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.FORMAT_CD).toString()) != null) {
                        olePurchaseOrderItem.setFormatTypeId(getFormatId(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.FORMAT_CD).toString()));
                    }
                }
                if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.CATEORY_CD).equals("null")) {
                    if (getCategoryId(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.CATEORY_CD).toString()) != null) {
                        olePurchaseOrderItem.setCategoryId(getCategoryId(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.CATEORY_CD).toString()));
                    }
                }
                if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ITM_PRICE_SOURCE).equals("null")) {
                    if (getItemPriceSourceId(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ITM_PRICE_SOURCE).toString()) != null) {
                        olePurchaseOrderItem.setItemPriceSourceId(getItemPriceSourceId(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ITM_PRICE_SOURCE).toString()));
                    }
                }
                if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.REQUEST_SOURCE).equals("null")) {
                    if (getRequestSourceId(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.REQUEST_SOURCE).toString()) != null) {
                        olePurchaseOrderItem.setRequestSourceTypeId(getRequestSourceId(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.REQUEST_SOURCE).toString()));
                    }
                }
            }
        }
        return purchaseOrderDocument;

    }

    public OlePurchaseOrderDocument updateAccount(OlePurchaseOrderDocument purchaseOrderDocument, int itemLineNumber, Map map) {
        for (OlePurchaseOrderItem olePurchaseOrderItem : purchaseOrderDocument.getItemsActiveOnly()) {
            if (olePurchaseOrderItem.getItemLineNumber() != null && olePurchaseOrderItem.getItemLineNumber() == itemLineNumber) {
                //for (PurApAccountingLine accountingLine : olePurchaseOrderItem.getSourceAccountingLines()) {
                if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ACC_LINE_INDEX).equals("null")) {
                    int index = Integer.parseInt(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ACC_LINE_INDEX).toString()) - 1;
                    PurApAccountingLine accountingLine = olePurchaseOrderItem.getSourceAccountingLines().get(index);
                    if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.CHART_CD).equals("null")) {
                        accountingLine.setChartOfAccountsCode(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.CHART_CD).toString());
                    }
                    if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ACC_NO).equals("null")) {
                        accountingLine.setAccountNumber(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ACC_NO).toString());
                    }
                    if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.OBJ_CD).equals("null")) {
                        accountingLine.setFinancialObjectCode(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.OBJ_CD).toString());
                    }
                    if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ACC_LIN_PERC).equals("null")) {
                        accountingLine.setAccountLinePercent(new BigDecimal(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ACC_LIN_PERC).toString()));
                    }

                }
            }
        }
        return purchaseOrderDocument;
    }

    public OlePurchaseOrderDocument updateDocument(OlePurchaseOrderDocument purchaseOrderDocument, Map map) {

        if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.DEL_ST_CD).equals("null")) {
            purchaseOrderDocument.setDeliveryStateCode(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.DEL_ST_CD).toString());
        }
        if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.DEL_ROOM_NO).equals("null")) {
            purchaseOrderDocument.setDeliveryBuildingRoomNumber(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.DEL_ROOM_NO).toString());
        }
        if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.DEL_LINE1_ADDR).equals("null")) {
            purchaseOrderDocument.setDeliveryBuildingLine1Address(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.DEL_LINE1_ADDR).toString());
        }
        if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.DEL_LINE2_ADDR).equals("null")) {
            purchaseOrderDocument.setDeliveryBuildingLine2Address(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.DEL_LINE2_ADDR).toString());
        }
        if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.DEL_CAMPUS_CD).equals("null")) {
            purchaseOrderDocument.setDeliveryCampusCode(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.DEL_CAMPUS_CD).toString());
        }
        if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.DEL_COUNTRY_CD).equals("null")) {
            purchaseOrderDocument.setDeliveryCountryCode(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.DEL_COUNTRY_CD).toString());
        }
        if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.DEL_POSTAL_CD).equals("null")) {
            purchaseOrderDocument.setDeliveryPostalCode(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.DEL_POSTAL_CD).toString());
        }
        if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.DEL_BUL_CD).equals("null")) {
            purchaseOrderDocument.setDeliveryBuildingCode(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.DEL_BUL_CD).toString());
        }
        if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.DEL_TO_EMAIL_ADDR).equals("null")) {
            purchaseOrderDocument.setDeliveryToEmailAddress(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.DEL_TO_EMAIL_ADDR).toString());
        }
        if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.DEL_TO_NAME).equals("null")) {
            purchaseOrderDocument.setDeliveryToName(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.DEL_TO_NAME).toString());
        }
        if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.DEL_TO_PHONE_NO).equals("null")) {
            purchaseOrderDocument.setDeliveryToPhoneNumber(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.DEL_TO_PHONE_NO).toString());
        }
        if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.DEL_INST_NT).equals("null")) {
            purchaseOrderDocument.setDeliveryInstructionText(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.DEL_INST_NT).toString());
        }

        if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.DOCUMENT_DESC).equals("null")) {
            purchaseOrderDocument.getDocumentHeader().setDocumentDescription(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.DOCUMENT_DESC).toString());
        }
        if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ASSIGN_TO_PRCPL_NM).equals("null")) {
            purchaseOrderDocument.setAssignedUserPrincipalName(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ASSIGN_TO_PRCPL_NM).toString());
        }
        if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.RECUR_PAY_TYP_CD).equals("null") && !map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.RECUR_PAY_TYP_CD).equals("")) {
            purchaseOrderDocument.setRecurringPaymentTypeCode(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.RECUR_PAY_TYP_CD).toString());
        } else {
            purchaseOrderDocument.setRecurringPaymentTypeCode(null);
        }
        if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.PO_BEGIN_DT).equals("null") && !map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.PO_BEGIN_DT).equals("")) {
            SimpleDateFormat format = new SimpleDateFormat(OLEConstants.OLEPurchaseOrderBulkAmendment.TIMESTAMP_FORMAT);
            purchaseOrderDocument.setPurchaseOrderBeginDate(java.sql.Date.valueOf(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.PO_BEGIN_DT).toString()));
        } else {
            purchaseOrderDocument.setPurchaseOrderBeginDate(null);
        }
        if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.PO_END_DATE).equals("null") && !map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.PO_END_DATE).equals("")) {
            SimpleDateFormat format = new SimpleDateFormat(OLEConstants.OLEPurchaseOrderBulkAmendment.TIMESTAMP_FORMAT);
            purchaseOrderDocument.setPurchaseOrderEndDate(java.sql.Date.valueOf(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.PO_END_DATE).toString()));
        } else {
            purchaseOrderDocument.setPurchaseOrderEndDate(null);
        }

        return purchaseOrderDocument;
    }

    public OlePurchaseOrderDocument updateVendor(OlePurchaseOrderDocument purchaseOrderDocument, Map map) {
        Map vendorMap = new HashMap();
        vendorMap.put(OLEConstants.OLEPurchaseOrderBulkAmendment.VENDOR_NUMBER, map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.VENDOR_NUMBER));
        vendorMap.put(OLEConstants.OLEPurchaseOrderBulkAmendment.VEN_DTL_ASSIGN_ID, map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.VEN_DTL_ASSIGN_ID));
        VendorDetail vendorDetail = getBusinessObjectService().findByPrimaryKey(VendorDetail.class, vendorMap);
        if (vendorDetail != null) {
            purchaseOrderDocument.setVendorName(vendorDetail.getVendorName());
            purchaseOrderDocument.setVendorHeaderGeneratedIdentifier(vendorDetail.getVendorHeaderGeneratedIdentifier());
            purchaseOrderDocument.setVendorDetailAssignedIdentifier(vendorDetail.getVendorDetailAssignedIdentifier());
            purchaseOrderDocument.setVendorAliasName("");
            purchaseOrderDocument.setVendorPaymentTermsCode(vendorDetail.getVendorPaymentTermsCode());
            purchaseOrderDocument.setVendorShippingPaymentTermsCode(vendorDetail.getVendorShippingPaymentTermsCode());
            for (VendorAddress vendorAddress : vendorDetail.getVendorAddresses()) {
                if (vendorAddress.isActive() && vendorAddress.isVendorDefaultAddressIndicator()) {
                    purchaseOrderDocument.setVendorCityName(vendorAddress.getVendorCityName());
                    purchaseOrderDocument.setVendorLine1Address(vendorAddress.getVendorLine1Address());
                    purchaseOrderDocument.setVendorLine2Address(vendorAddress.getVendorLine2Address());
                    purchaseOrderDocument.setVendorCountryCode(vendorAddress.getVendorCountryCode());
                    purchaseOrderDocument.setVendorStateCode(vendorAddress.getVendorStateCode());
                    purchaseOrderDocument.setVendorPostalCode(vendorAddress.getVendorZipCode());
                }
            }
        }
        return purchaseOrderDocument;
    }

    public void writeFile() {
        String fileName = OLEConstants.OLEPurchaseOrderBulkAmendment.FILE_NM;
        String filePath = getPOBACSVDirectory() + fileSeparator + OLEConstants.POBA_DIRECTORY + fileSeparator;
        try {
            FileWriter fileWriter = new FileWriter(filePath+fileName);
            fileWriter.append(message.toString());
            fileWriter.flush();
            fileWriter.close();
            LOG.info("POBA output file created successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> createFileForPOBA(OLEPurchaseOrderBatchDocument olePurchaseOrderBatchDocument) throws
            Exception {

        String fileDirectory = getPOBACSVDirectory();
        File fileName = new File(fileDirectory + fileSeparator + OLEConstants.POBA_DIRECTORY + fileSeparator + olePurchaseOrderBatchDocument.getDocIdIngestFile().getOriginalFilename());
        BufferedWriter documentOut = new BufferedWriter(new FileWriter(fileName));
        String documentFileContent = new String(olePurchaseOrderBatchDocument.getDocIdIngestFile().getBytes(),"UTF-8");
        documentOut.write(documentFileContent);
        documentOut.close();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
        String text = null;
        List<String> poIds = new ArrayList<String>();
        while ((text = br.readLine()) != null) {
            if (StringUtils.isNotEmpty(text)) {
                text = CharMatcher.ASCII.retainFrom(text);
                poIds.add(text);
            }
        }
        br.close();
        return poIds;
    }

    public boolean validatePOFields(Map map) {
        String poId = map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.PUR_AP_ID).toString();
        Map invMap = new HashMap();

        boolean result = true;
        if (!vendorDetailList.contains(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.VENDOR_NUMBER))) {
            message.append("Invalid Vendor Number : " + map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.VENDOR_NUMBER));
            message.append("\n");
            result = result & false;
        }
        if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.CATEORY_CD).equals("null") && !categoryList.contains(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.CATEORY_CD))) {
            message.append("Invalid Category Type : " + map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.CATEORY_CD));
            message.append("\n");
            result = result & false;
        }
        if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.FORMAT_CD).equals("null") && !formatList.contains(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.FORMAT_CD))) {
            message.append("Invalid Format Type : " + map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.FORMAT_CD));
            message.append("\n");
            result = result & false;
        }
        if (!map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ITM_PRICE_SOURCE).equals("null") && !priceSourceList.contains(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ITM_PRICE_SOURCE))) {
            message.append("Invalid Item Price Source Type : " + map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ITM_PRICE_SOURCE));
            message.append("\n");
            result = result & false;
        }
        if (map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.REQUEST_SOURCE).equals("null") && !requestSourceList.contains(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.REQUEST_SOURCE))) {
            message.append("Invalid Request Source Type : " + map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.REQUEST_SOURCE));
            message.append("\n");
            result = result & false;
        }
        if (map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ITM_LOC).equals("null") && !locationList.contains(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ITM_LOC))) {
            message.append("Invalid Location code : " + map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ITM_LOC));
            message.append("\n");
            result = result & false;
        }
        if (map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.CHART_CD) != null && !chartList.contains(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.CHART_CD))) {
            message.append("Invalid Chart code : " + map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.CHART_CD));
            message.append("\n");
            result = result & false;
        }
        String accountNumber = map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ACC_NO).toString() + map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.CHART_CD).toString();
        if (map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ACC_NO) != null && !accountList.contains(accountNumber)) {
            message.append("Invalid Account Number : " + map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ACC_NO));
            message.append("\n");
            result = result & false;
        }
        String objectCode = map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.OBJ_CD).toString() + map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.CHART_CD).toString();
        if (map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.OBJ_CD) != null && !objectCodeList.contains(objectCode)) {
            message.append("Invalid Financial Object Number : " + map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.OBJ_CD));
            message.append("\n");
            result = result & false;
        }
        if (!result) {
            poNumber = Integer.parseInt(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.PUR_AP_ID).toString());
        }
        return result;
    }

    private FileWriter getFileWriterForPOCSV() throws IOException {
        String fileDirectory = getPOBACSVDirectory();
        Date date = new Date();
        String fileCreationDate = new SimpleDateFormat(OLEConstants.OLEPurchaseOrderBulkAmendment.DATE_FORMAT).format(date).replace(":", "");

        String fileName = fileDirectory + fileSeparator + OLEConstants.POBA_DIRECTORY + fileSeparator + fileCreationDate + OLEConstants.OLEPurchaseOrderBulkAmendment.OUTPUT_FILE_NAME;
        fileWriter = new FileWriter(fileName);
        return fileWriter;
    }

    public void downloadCSV(List<String> poIds) {
        String NEW_LINE_SEPARATOR = "\n";
        try {
            fileWriter = getFileWriterForPOCSV();
            fileWriter.append(FILE_HEADER.toString());
            fileWriter.append(NEW_LINE_SEPARATOR);
            for (String poid : poIds) {
                Map poMap = new HashMap();
                poMap.put(OLEConstants.OLEPurchaseOrderBulkAmendment.PUR_AP_ID, poid);
                poMap.put(OLEConstants.OLEPurchaseOrderBulkAmendment.PUR_AP_CUR_IND, true);
                OlePurchaseOrderDocument purchaseOrderDocument = getPODocumentByPrimaryKey(poMap);

                for (Iterator iterator = purchaseOrderDocument.getItems().iterator(); iterator.hasNext(); ) {
                    OlePurchaseOrderItem item = (OlePurchaseOrderItem) iterator.next();
                    Integer accountingLineNumber = 1;
                    if (item.getItemLineNumber() != null) {
                        List<PurApAccountingLine> sourceAccountingLines = item.getSourceAccountingLines();
                        for (Iterator<PurApAccountingLine> purApAccountingLineIterator = sourceAccountingLines.iterator(); purApAccountingLineIterator.hasNext(); ) {
                            PurApAccountingLine accountingLine = purApAccountingLineIterator.next();
                            writeValue(purchaseOrderDocument.getDocumentNumber());
                            writeValue(purchaseOrderDocument.getDocumentHeader().getDocumentDescription());
                            writeValue(purchaseOrderDocument.getPurapDocumentIdentifier().toString());
                            writeValue(purchaseOrderDocument.getVendorHeaderGeneratedIdentifier().toString());
                            writeValue(purchaseOrderDocument.getVendorDetailAssignedIdentifier().toString());
                            writeValue(purchaseOrderDocument.getAssignedUserPrincipalName());
                            writeValue(purchaseOrderDocument.getRecurringPaymentTypeCode());
                            writeValue(purchaseOrderDocument.getPurchaseOrderBeginDate() != null ? purchaseOrderDocument.getPurchaseOrderBeginDate().toString() : null);
                            writeValue(purchaseOrderDocument.getPurchaseOrderEndDate() != null ? purchaseOrderDocument.getPurchaseOrderEndDate().toString() : null);
                            writeValue(purchaseOrderDocument.getDeliveryCampusCode());
                            writeValue(purchaseOrderDocument.getDeliveryBuildingCode());
                            writeValue(purchaseOrderDocument.getDeliveryBuildingLine1Address());
                            writeValue(purchaseOrderDocument.getDeliveryBuildingLine2Address());
                            writeValue(purchaseOrderDocument.getDeliveryBuildingRoomNumber());
                            writeValue(purchaseOrderDocument.getDeliveryStateCode());
                            writeValue(purchaseOrderDocument.getDeliveryPostalCode());
                            writeValue(purchaseOrderDocument.getDeliveryCountryCode());
                            writeValue(purchaseOrderDocument.getDeliveryToEmailAddress());
                            writeValue(purchaseOrderDocument.getDeliveryToName());
                            writeValue(purchaseOrderDocument.getDeliveryToPhoneNumber());
                            writeValue(purchaseOrderDocument.getDeliveryBuildingCode());
                            writeValue(purchaseOrderDocument.getDeliveryInstructionText());
                            writeValue(item.getItemLineNumber().toString());
                            writeValue(item.getCopyNumber());
                            writeValue(item.getFormatTypeName() != null ? item.getFormatTypeName().getFormatTypeName() : null);
                            writeValue(item.getCategory() != null ? item.getCategory().getCategory() : null);
                            writeValue(item.getItemPriceSource() != null ? item.getItemPriceSource().getItemPriceSource() : null);
                            writeValue(item.getOleRequestSourceType() != null ? item.getOleRequestSourceType().getRequestSourceType() : null);
                            writeValue(item.getItemQuantity().toString());
                            writeValue(item.getItemNoOfParts().toString());
                            writeValue(item.getItemListPrice().toString());
                            Boolean publicViewIndicator = item.isItemPublicViewIndicator();
                            writeValue(publicViewIndicator.toString());
                            Boolean doNotClaimIndicator = item.isDoNotClaim();
                            writeValue(doNotClaimIndicator.toString());
                            Boolean routeToRequestorIndicator = item.isItemRouteToRequestorIndicator();
                            writeValue(routeToRequestorIndicator.toString());
                            writeValue(item.getItemLocation());
                            writeValue(item.getVendorItemPoNumber());
                            writeValue(accountingLineNumber.toString());
                            writeValue(accountingLine.getChartOfAccountsCode());
                            writeValue(accountingLine.getAccountNumber());
                            writeValue(accountingLine.getFinancialObjectCode());
                            fileWriter.append("\"").append(accountingLine.getAccountLinePercent().toString()).append
                                    ("\"");
                            fileWriter.append(NEW_LINE_SEPARATOR);
                            ++accountingLineNumber;
                        }
                    }
                }
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    private void writeValue(String value) throws IOException {
        fileWriter.append("\"");
        fileWriter.append(value);
        fileWriter.append("\"");
        fileWriter.append(",");
    }

    private OlePurchaseOrderDocument getPODocumentByPrimaryKey(Map poMap) {
        return getBusinessObjectService().findByPrimaryKey(OlePurchaseOrderDocument.class, poMap);
    }

    private BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = SpringContext.getBean(BusinessObjectService.class);
        }
        return businessObjectService;
    }

    private String getPOBACSVDirectory() {
        return ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.STAGING_DIRECTORY);
    }

    public void createPOBADirectory() {
        String stagingDirectory = ConfigContext.getCurrentContextConfig().getProperty(org.kuali.ole.sys.OLEConstants.STAGING_DIRECTORY_KEY);
        String fileLocation = stagingDirectory + fileSeparator + org.kuali.ole.sys.OLEConstants.POBA_DIRECTORY;
        File fileLocationDir = new File(fileLocation);
        if (!(fileLocationDir.exists())) {
            fileLocationDir.mkdir();
        }
    }

    public OlePurchaseOrderService getOlePurchaseOrderService() {
        if (olePurchaseOrderService == null) {
            olePurchaseOrderService = (OlePurchaseOrderService) SpringContext.getService("olePurchaseOrderService");
        }
        return olePurchaseOrderService;
    }


    public void sendFyiForAccount(PurchaseOrderDocument po) {

        List<AdHocRoutePerson> fyiList = createFyiFiscalOfficerListForAmendGlEntries(po);
        String annotation = "Amendment to Purchase Order " + po.getPurapDocumentIdentifier() + "( Document id " + po.getDocumentNumber() + ")" +
                " in the batch process(POBA).";
        String responsibilityNote = " ";
        if (fyiList != null && fyiList.size() > 0) {
            for (AdHocRoutePerson adHocPerson : fyiList) {
                try {
                    po.appSpecificRouteDocumentToUser(
                            po.getDocumentHeader().getWorkflowDocument(),
                            adHocPerson.getPerson().getPrincipalId(),
                            annotation,
                            responsibilityNote);
                } catch (WorkflowException e) {
                    throw new RuntimeException("Error routing fyi for document with id " + po.getDocumentNumber(), e);
                }
            }
        }
    }

    protected List<AdHocRoutePerson> createFyiFiscalOfficerListForAmendGlEntries(PurchaseOrderDocument po) {

        List<AdHocRoutePerson> adHocRoutePersons = new ArrayList<AdHocRoutePerson>();
        Map fiscalOfficers = new HashMap();
        AdHocRoutePerson adHocRoutePerson = null;
        for (PurApAccountingLine account : (List<PurApAccountingLine>) po.getSourceAccountingLines()) {
            Account acct = SpringContext.getBean(AccountService.class).getByPrimaryId(account.getChartOfAccountsCode(),
                    account.getAccountNumber());
            if (acct != null) {
                String principalName = acct.getAccountFiscalOfficerUser().getPrincipalName();
                if (fiscalOfficers.containsKey(principalName) == false) {
                    fiscalOfficers.put(principalName, principalName);
                    adHocRoutePerson = new AdHocRoutePerson();
                    adHocRoutePerson.setId(principalName);
                    adHocRoutePerson.setActionRequested(KewApiConstants.ACTION_REQUEST_FYI_REQ);
                    adHocRoutePersons.add(adHocRoutePerson);
                }
            }
        }
        return adHocRoutePersons;
    }

    public Integer getCategoryId(String name) {
        Integer id = null;
        Map idMap = new HashMap();
        idMap.put("category", name);
        OleCategory category = getBusinessObjectService().findByPrimaryKey(OleCategory.class, idMap);
        if (category != null) {
            return category.getCategoryId().intValue();
        }
        return id;
    }

    public Integer getFormatId(String name) {
        Integer id = null;
        Map idMap = new HashMap();
        idMap.put("formatTypeName", name);
        OleFormatType formatType = getBusinessObjectService().findByPrimaryKey(OleFormatType.class, idMap);
        if (formatType != null) {
            return formatType.getFormatTypeId().intValue();
        }
        return id;
    }

    public Integer getItemPriceSourceId(String name) {
        Integer id = null;
        Map idMap = new HashMap();
        idMap.put("itemPriceSource", name);
        OleItemPriceSource itemPriceSource = getBusinessObjectService().findByPrimaryKey(OleItemPriceSource.class, idMap);
        if (itemPriceSource != null) {
            return itemPriceSource.getItemPriceSourceId().intValue();
        }
        return id;
    }

    public Integer getRequestSourceId(String name) {
        Integer id = null;
        Map idMap = new HashMap();
        idMap.put("requestSourceType", name);
        OleRequestSourceType requestSourceType = getBusinessObjectService().findByPrimaryKey(OleRequestSourceType.class, idMap);
        if (requestSourceType != null) {
            return requestSourceType.getRequestSourceTypeId().intValue();
        }
        return id;
    }


    public void setFileWriter(FileWriter fileWriter) {
        this.fileWriter = fileWriter;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }
}
