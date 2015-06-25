package org.kuali.ole.select.document.service.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.coa.businessobject.Account;
import org.kuali.ole.coa.businessobject.Chart;
import org.kuali.ole.coa.businessobject.ObjectCode;
import org.kuali.ole.coa.service.AccountService;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.docstore.engine.service.storage.DocstoreRDBMSStorageService;
import org.kuali.ole.module.purap.businessobject.PurApAccountingLine;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.select.document.service.OlePurchaseOrderService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.vnd.businessobject.VendorAddress;
import org.kuali.ole.vnd.businessobject.VendorDetail;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.bo.AdHocRoutePerson;
import org.kuali.rice.krad.exception.ValidationException;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.servlet.ServletException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * Created by pvsubrah on 6/10/15.
 */
public class BulkPOChangeExecutor implements Callable {
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

    private final List<Map> poListToProcess;
    private final String pobaDocNumber;
    private OlePurchaseOrderService olePurchaseOrderService;
    private StringBuilder message = new StringBuilder();
    private BusinessObjectService businessObjectService;
    private UserSession userSession;
    boolean validationResult = true;
    private PlatformTransactionManager transactionManager;

    public BulkPOChangeExecutor(String pobaDocNumber, List<Map> poListToProcess) {
        this.pobaDocNumber = pobaDocNumber;
        this.poListToProcess = poListToProcess;
    }

    @Override
    public Object call() throws Exception {
        long startTime = System.currentTimeMillis();
        Map<String, String> resultMap = new HashMap<>();
        GlobalVariables.setUserSession(new UserSession(userSession.getPrincipalName()));
        OlePurchaseOrderDocument olePurchaseOrderDocument = null;
        String result = null;
        String poId = null;
        for (Iterator<Map> iterator = poListToProcess.iterator(); iterator.hasNext(); ) {
            Map<String, String> map = iterator.next();
            poId = map.get("purapDocumentIdentifier");
            if (olePurchaseOrderDocument == null) {
                olePurchaseOrderDocument = getPODocumentByPOId(poId);
            }
            validationResult &= validatePOFields(map);
            if (validationResult) {
                updatePurchaseOrderDocument(olePurchaseOrderDocument, map);
            } else {
                message.append("POA Creation Failed for PO Num: + ").append(poId);
            }

        }
        if (validationResult) {
            result = getOlePurchaseOrderService().createPurchaseOrderAmendmentDocument
                    (olePurchaseOrderDocument,
                            pobaDocNumber);
            if (StringUtils.isNotBlank(result)) {
                message.append(result);
            } else {
                message.append("Successfully created POA document for PO# : ").append(olePurchaseOrderDocument.getPurapDocumentIdentifier()).append(" ");
            }

        }
        resultMap.put(poId, message.toString());
        long endTime = System.currentTimeMillis();
        System.out.println("Time taken to create POA for PO doc: " + poId + "is " + (endTime-startTime) + " " +
                "milliseconds");
        return resultMap;
    }


    private OlePurchaseOrderDocument getPODocumentByPOId(String poId) {
        Map criteriaMap = new HashMap();
        criteriaMap.put("purapDocumentIdentifier", poId);
        criteriaMap.put("purchaseOrderCurrentIndicator", true);
        OlePurchaseOrderDocument olePurchaseOrderDocument = getBusinessObjectService().findByPrimaryKey(OlePurchaseOrderDocument.class, criteriaMap);
        return olePurchaseOrderDocument;
    }

    public boolean validatePOFields(Map map) {
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
        return result;
    }

    public void updatePurchaseOrderDocument(OlePurchaseOrderDocument olePurchaseOrderDocument, Map map) {
        try {
            updateDocument(olePurchaseOrderDocument, map);
            updateVendor(olePurchaseOrderDocument, map);
            int lineNumber = Integer.parseInt(map.get(OLEConstants.OLEPurchaseOrderBulkAmendment.ITM_LIN_NO).toString());
            updateItem(olePurchaseOrderDocument, lineNumber, map);
            updateAccount(olePurchaseOrderDocument, lineNumber, map);
        } catch (UnexpectedRollbackException re) {
            message.append(re.getMessage());
            message.append("\n");
        } catch (ValidationException ve) {
            message.append(ve.getMessage());
            message.append("\n");
        } catch (Exception ex) {
            message.append("Failed to create POA document for PO document number : " + olePurchaseOrderDocument.getDocumentNumber());
            message.append("\n");

        }
        //sendFyiForAccount(olePurchaseOrderDocument);
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
                // String principalName = account.getAccount().getAccountFiscalOfficerUser().getPrincipalName();
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


    private BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = SpringContext.getBean(BusinessObjectService.class);
        }
        return businessObjectService;
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


    public OlePurchaseOrderService getOlePurchaseOrderService() {
        if (olePurchaseOrderService == null) {
            olePurchaseOrderService = (OlePurchaseOrderService) SpringContext.getService("olePurchaseOrderService");
        }
        return olePurchaseOrderService;
    }

    public void setCharts(List<Chart> charts) {
        this.charts = charts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public void setObjects(List<ObjectCode> objects) {
        this.objects = objects;
    }

    public void setVendorDetails(List<VendorDetail> vendorDetails) {
        this.vendorDetails = vendorDetails;
    }

    public void setCategories(List<OleCategory> categories) {
        this.categories = categories;
    }

    public void setFormatTypes(List<OleFormatType> formatTypes) {
        this.formatTypes = formatTypes;
    }

    public void setItemPriceSources(List<OleItemPriceSource> itemPriceSources) {
        this.itemPriceSources = itemPriceSources;
    }

    public void setRequestSourceTypes(List<OleRequestSourceType> requestSourceTypes) {
        this.requestSourceTypes = requestSourceTypes;
    }

    public void setLocations(List<OleLocation> locations) {
        this.locations = locations;
    }

    public void setChartList(List<String> chartList) {
        this.chartList = chartList;
    }

    public void setAccountList(List<String> accountList) {
        this.accountList = accountList;
    }

    public void setObjectCodeList(List<String> objectCodeList) {
        this.objectCodeList = objectCodeList;
    }

    public void setVendorDetailList(List<String> vendorDetailList) {
        this.vendorDetailList = vendorDetailList;
    }

    public void setCategoryList(List<String> categoryList) {
        this.categoryList = categoryList;
    }

    public void setFormatList(List<String> formatList) {
        this.formatList = formatList;
    }

    public void setRequestSourceList(List<String> requestSourceList) {
        this.requestSourceList = requestSourceList;
    }

    public void setPriceSourceList(List<String> priceSourceList) {
        this.priceSourceList = priceSourceList;
    }

    public void setLocationList(List<String> locationList) {
        this.locationList = locationList;
    }

    public void setOlePurchaseOrderService(OlePurchaseOrderService olePurchaseOrderService) {
        this.olePurchaseOrderService = olePurchaseOrderService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public StringBuilder getMessage() {
        return message;
    }

    public List<Chart> getCharts() {
        return charts;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public List<ObjectCode> getObjects() {
        return objects;
    }

    public List<VendorDetail> getVendorDetails() {
        return vendorDetails;
    }

    public List<OleCategory> getCategories() {
        return categories;
    }

    public List<OleFormatType> getFormatTypes() {
        return formatTypes;
    }

    public List<OleItemPriceSource> getItemPriceSources() {
        return itemPriceSources;
    }

    public List<OleRequestSourceType> getRequestSourceTypes() {
        return requestSourceTypes;
    }

    public List<OleLocation> getLocations() {
        return locations;
    }

    public List<String> getChartList() {
        return chartList;
    }

    public List<String> getAccountList() {
        return accountList;
    }

    public List<String> getObjectCodeList() {
        return objectCodeList;
    }

    public List<String> getVendorDetailList() {
        return vendorDetailList;
    }

    public List<String> getCategoryList() {
        return categoryList;
    }

    public List<String> getFormatList() {
        return formatList;
    }

    public List<String> getRequestSourceList() {
        return requestSourceList;
    }

    public List<String> getPriceSourceList() {
        return priceSourceList;
    }

    public List<String> getLocationList() {
        return locationList;
    }

    public List<Map> getPoListToProcess() {
        return poListToProcess;
    }

    public String getPobaDocNumber() {
        return pobaDocNumber;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public PlatformTransactionManager getTransactionManager() {
        if (transactionManager == null) {
            transactionManager = GlobalResourceLoader.getService("transactionManager");
        }
        return this.transactionManager;
    }
}
