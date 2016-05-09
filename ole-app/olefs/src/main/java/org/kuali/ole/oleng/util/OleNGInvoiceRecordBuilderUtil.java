package org.kuali.ole.oleng.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.util.BusinessObjectServiceHelperUtil;
import org.kuali.ole.pojo.OleInvoiceRecord;
import org.kuali.ole.pojo.edi.*;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.OleInvoiceNote;
import org.kuali.ole.vnd.businessobject.OleCurrencyType;
import org.kuali.ole.vnd.businessobject.VendorAlias;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.LookupService;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.*;

/**
 * Created by SheikS on 2/26/2016.
 */
public class OleNGInvoiceRecordBuilderUtil extends BusinessObjectServiceHelperUtil {
    private static final Logger LOG = Logger.getLogger(OleNGInvoiceRecordBuilderUtil.class);

    private static OleNGInvoiceRecordBuilderUtil oleNGInvoiceRecordBuilderUtil = null;
    private LookupService lookupService;
    private BatchDateTimeUtil batchDateTimeUtil;

    public static OleNGInvoiceRecordBuilderUtil getInstance() {
        if (null == oleNGInvoiceRecordBuilderUtil) {
            oleNGInvoiceRecordBuilderUtil = new OleNGInvoiceRecordBuilderUtil();
        }
        return oleNGInvoiceRecordBuilderUtil;
    }

    private OleNGInvoiceRecordBuilderUtil() {
    }

    public LookupService getLookupService() {
        if (null == lookupService) {
            lookupService = KRADServiceLocatorWeb.getLookupService();
        }
        return lookupService;
    }

    public void setLookupService(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    public OleInvoiceRecord build(LineItemOrder lineItemOrder, INVOrder invOrder) throws Exception {

        OleInvoiceRecord oleInvoiceRecord = new OleInvoiceRecord();
        String listPrice = getListPrice(lineItemOrder);
        oleInvoiceRecord.setListPrice(listPrice != null ? listPrice : getPrice(lineItemOrder));
        oleInvoiceRecord.setUnitPrice(populateUnitPrice(lineItemOrder));
        oleInvoiceRecord.setQuantity(getQuantity(lineItemOrder));
        oleInvoiceRecord.setVendorItemIdentifier(getVendorItemIdentifier(lineItemOrder) != null ? getVendorItemIdentifier(lineItemOrder) : getVendorIdentifierByPIA(lineItemOrder));
        oleInvoiceRecord.setBfnNumber(getBFNNumber(lineItemOrder));
        oleInvoiceRecord.setVendorNumber(getVendorNumber(invOrder, oleInvoiceRecord));
        if (StringUtils.isBlank(oleInvoiceRecord.getVendorNumber())) {
            oleInvoiceRecord.setVendorAlias(getVendorAlias(invOrder));
            oleInvoiceRecord.setVendorNumber(getVendorNumberFromVendorAlias(oleInvoiceRecord, invOrder));
        }
        oleInvoiceRecord.setItemChartCode(OLEConstants.OleInvoiceImport.ITM_CHART_CODE);
        oleInvoiceRecord.setInvoiceNumber(getInvoiceNumber(invOrder));
        oleInvoiceRecord.setInvoiceDate(getInvoiceDate(invOrder));
        oleInvoiceRecord.setVendorInvoiceAmount(getVendorInvoiceAmount(invOrder));
        oleInvoiceRecord.setISBN(getISBN(lineItemOrder) == null ? getLineItemISBN(lineItemOrder) : getISBN(lineItemOrder));
        oleInvoiceRecord.setISSN(getISSN(lineItemOrder));
        oleInvoiceRecord.setBillToCustomerID(populateBillToCustomerId(invOrder));
        oleInvoiceRecord.setItemDescription(getItemDescription(lineItemOrder));
        oleInvoiceRecord.setItemNote(getItemNote(lineItemOrder));
        oleInvoiceRecord.setNumberOfCopiesOrdered(oleInvoiceRecord.getQuantity());
        oleInvoiceRecord.setSubscriptionPeriodFrom(getSubscriptionDateFrom(lineItemOrder));
        oleInvoiceRecord.setSubscriptionPeriodTo(getSubscriptionDateTo(lineItemOrder));
        oleInvoiceRecord.setSubscriptionPeriod(getSubscriptionPeriod(lineItemOrder));
        oleInvoiceRecord.setSummaryAmount(getSummaryCharge(invOrder));
        oleInvoiceRecord.setAdditionalChargeCode(getAdditionChargeCode(invOrder));
        oleInvoiceRecord.setAdditionalCharge(getAdditionCharge(invOrder));
        oleInvoiceRecord.setLineItemAdditionalChargeCode(populateLineItemChargeCode(lineItemOrder));
        oleInvoiceRecord.setLineItemAdditionalCharge(populateLineItemAdditionalCharge(lineItemOrder));
        oleInvoiceRecord.setPurchaseOrderNumber(getPurchaseOrderNumber(lineItemOrder));
        oleInvoiceRecord.setLineItemTaxAmount(getTaxForLineItem(lineItemOrder));
        oleInvoiceRecord.setSummaryTaxAmount(getSummaryTax(invOrder));
        oleInvoiceRecord.setSummaryTaxableAmount(getSummaryTaxable(invOrder));
        setCurrencyDetails(invOrder, oleInvoiceRecord);
        return oleInvoiceRecord;
    }

    private String getSummaryTaxable(INVOrder invOrder) {
        String summaryTaxableAmount = "";
        Summary summary = invOrder.getSummary();
        if (summary != null && null != summary.getMonetaryTaxableSummary()) {
            MonetaryTaxableSummary monetaryTaxableSummary = summary.getMonetaryTaxableSummary();
            MonetaryTaxableSummaryDetails monetaryTaxableSummaryDetails = monetaryTaxableSummary.getMonetaryTaxableSummaryDetails();
            if (monetaryTaxableSummaryDetails != null && "125".equals(monetaryTaxableSummaryDetails.getAmountType())) {
                summaryTaxableAmount = monetaryTaxableSummaryDetails.getAmount();
            }
        }
        return summaryTaxableAmount;
    }

    private String getSummaryTax(INVOrder invOrder) {
        String summaryTaxAmount = "";
        Summary summary = invOrder.getSummary();
        if (summary != null && null != summary.getMonetaryTaxSummary()) {
            MonetaryTaxSummary monetaryTaxSummary = summary.getMonetaryTaxSummary();
            MonetaryTaxSummaryDetails monetaryTaxSummaryDetails = monetaryTaxSummary.getMonetaryTaxSummaryDetails();
            if (monetaryTaxSummaryDetails != null && "124".equals(monetaryTaxSummaryDetails.getAmountType())) {
                summaryTaxAmount = monetaryTaxSummaryDetails.getAmount();
            }
        }
        return summaryTaxAmount;
    }

    private String getTaxForLineItem(LineItemOrder lineItemOrder) {
        String taxAmount = "";
        TaxLineItemInfo taxLineItemInfo = lineItemOrder.getTaxLineItemInfo();
        if (taxLineItemInfo != null && null != lineItemOrder.getMonetaryTaxLineItem()) {
            MonetaryTaxLineItem monetaryTaxLineItem = lineItemOrder.getMonetaryTaxLineItem();
            MonetaryLineItemTaxDetail monetaryLineItemTaxDetail = monetaryTaxLineItem.getMonetaryLineItemTaxDetail();
            if (monetaryLineItemTaxDetail != null && "124".equals(monetaryLineItemTaxDetail.getAmountType())) {
                taxAmount = monetaryLineItemTaxDetail.getAmount();
            }
        }
        return taxAmount;
    }

    private String getVendorNumberFromVendorAlias(OleInvoiceRecord oleInvoiceRecord, INVOrder invOrder) throws Exception {
        if (StringUtils.isNotBlank(oleInvoiceRecord.getVendorAlias())) {
            Map vendorAliasMap = new HashMap();
            vendorAliasMap.put(org.kuali.ole.sys.OLEConstants.VENDOR_ALIAS_NAME, oleInvoiceRecord.getVendorAlias());
            List<VendorAlias> vendorAliasList = (List<VendorAlias>) getLookupService().findCollectionBySearchHelper(VendorAlias.class, vendorAliasMap, true);
            if (CollectionUtils.isNotEmpty(vendorAliasList)) {
                VendorAlias vendorAlias = vendorAliasList.get(0);
                return vendorAlias.getVendorHeaderGeneratedIdentifier() + "-" + vendorAlias.getVendorDetailAssignedIdentifier();

            } else {
                if (oleInvoiceRecord.getVendorItemIdentifier() == null)
                    throw new Exception("The vendor alias in Edifact file doesn't match in database for invoice number:: " + getInvoiceNumber(invOrder) + " and invoice date:: " + getInvoiceDate(invOrder));
            }
        }
        return null;
    }

    private void setCurrencyDetails(INVOrder invOrder, OleInvoiceRecord oleInvoiceRecord) {
        if (invOrder.getMessage() != null && invOrder.getMessage().getCurrencyDetails() != null && invOrder.getMessage().getCurrencyDetails().getCurrencyDetailsSupplierInformation() != null) {
            if (!StringUtils.isBlank(invOrder.getMessage().getCurrencyDetails().getCurrencyDetailsSupplierInformation().getCurrencyType())) {
                Map<String, String> currencyTypeMap = new HashMap<>();
                currencyTypeMap.put(OLEConstants.CURR_ALPHA_CD, invOrder.getMessage().getCurrencyDetails().getCurrencyDetailsSupplierInformation().getCurrencyType());
                List<OleCurrencyType> currencyTypeList = (List) getBusinessObjectService().findMatching(OleCurrencyType.class, currencyTypeMap);
                if (CollectionUtils.isNotEmpty(currencyTypeList)) {
                    OleCurrencyType oleCurrencyType = currencyTypeList.get(0);
                    oleInvoiceRecord.setCurrencyTypeId(oleCurrencyType.getCurrencyTypeId().toString());
                    oleInvoiceRecord.setCurrencyType(oleCurrencyType.getCurrencyType());
                    if (StringUtils.isNotBlank(oleInvoiceRecord.getCurrencyType()) && !oleInvoiceRecord.getCurrencyType().equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                        oleInvoiceRecord.setForeignListPrice(oleInvoiceRecord.getListPrice());
                    }
                }
            }
        }
    }

    private String populateBillToCustomerId(INVOrder invOrder) {
        if (invOrder.getMessage() != null && CollectionUtils.isNotEmpty(invOrder.getMessage().getPartyQualifier())) {
            for (int index = 0; index < invOrder.getMessage().getPartyQualifier().size(); index++) {
                if (invOrder.getMessage().getPartyQualifier().get(index).getPartyCode() != null && invOrder.getMessage().getPartyQualifier().get(index).getPartyCode().equalsIgnoreCase("BY")) {
                    return invOrder.getMessage().getPartyQualifier().get(index).getPartyInformation().getCodeIdentification();
                } else if (invOrder.getMessage() != null && invOrder.getMessage().getBuyerAdditionalPartyIdentifier() != null && invOrder.getMessage().getBuyerAdditionalPartyIdentifier().getBuyerIdentifier() != null &&
                        invOrder.getMessage().getBuyerAdditionalPartyIdentifier().getBuyerIdentifier().getBuyerReferenceQualifier().equalsIgnoreCase("API")) {
                    return invOrder.getMessage().getBuyerAdditionalPartyIdentifier().getBuyerIdentifier().getBuyerReferenceNumber();
                }
            }
        }
        return null;
    }

    private Integer getPurchaseOrderNumber(LineItemOrder lineItemOrder) throws Exception {
        if (CollectionUtils.isNotEmpty(lineItemOrder.getSupplierReferenceInformation())) {
            for (int index = 0; index < lineItemOrder.getSupplierReferenceInformation().size(); index++) {
                SupplierReferenceInformation supplierReferenceInformation = lineItemOrder.getSupplierReferenceInformation().get(index);
                if (supplierReferenceInformation.getSupplierLineItemReference() != null &&
                        supplierReferenceInformation.getSupplierLineItemReference().size() > 0) {
                    for (int innerIndex = 0; innerIndex < supplierReferenceInformation.getSupplierLineItemReference().size(); innerIndex++) {
                        SupplierLineItemReference supplierLineItemReference = supplierReferenceInformation.getSupplierLineItemReference().get(innerIndex);
                        if (supplierLineItemReference.getSuppliersOrderLine() != null &&
                                (supplierLineItemReference.getSuppliersOrderLine().equals("ON") ||
                                        supplierLineItemReference.getSuppliersOrderLine().equals("LI"))) {
                            try {
                                if (supplierLineItemReference.getVendorReferenceNumber() != null &&
                                        isNumeric(supplierLineItemReference.getVendorReferenceNumber())) {
                                    return Integer.parseInt(supplierLineItemReference.getVendorReferenceNumber());
                                } else {
                                    return 0;
                                }


                            } catch (Exception e) {
                                LOG.error("Purchase Order Number should be a number but invoice file has :" + supplierLineItemReference.getVendorReferenceNumber());
                                throw new Exception("Purchase Order Number should be a number but invoice file has :" + supplierLineItemReference.getVendorReferenceNumber(), e);
                            }
                        }
                    }

                }
            }

        }
        return null;
    }

    private String populateUnitPrice(LineItemOrder lineItemOrder) {
        if (CollectionUtils.isNotEmpty(lineItemOrder.getMonetaryDetail())) {
            MonetaryDetail monetaryDetail = lineItemOrder.getMonetaryDetail().get(0);
            if (CollectionUtils.isNotEmpty(monetaryDetail.getMonetaryLineItemInformation())) {
                return monetaryDetail.getMonetaryLineItemInformation().get(0).getAmount();
            }
        }
        return null;
    }

    private String populateLineItemChargeCode(LineItemOrder lineItemOrder) {
        if (CollectionUtils.isNotEmpty(lineItemOrder.getLineItemAllowanceOrCharge())) {
            LineItemAllowanceOrCharge lineItemAllowanceOrCharge = lineItemOrder.getLineItemAllowanceOrCharge().get(0);
            if (CollectionUtils.isNotEmpty(lineItemAllowanceOrCharge.getLineItemSpecialServiceIdentification())) {
                return lineItemAllowanceOrCharge.getLineItemSpecialServiceIdentification().get(0).getSpecialServiceCode();
            }
        }
        return null;
    }

    private String populateLineItemAdditionalCharge(LineItemOrder lineItemOrder) {
        if (CollectionUtils.isNotEmpty(lineItemOrder.getAllowanceMonetaryDetail())) {
            if (lineItemOrder.getAllowanceMonetaryDetail().get(0).getAllowanceMonetaryLineItemInformation() != null) {
                return "0";//lineItemOrder.getAllowanceMonetaryDetail().get(0).getAllowanceMonetaryLineItemInformation().get(0).getAmount();
            }
        }
        return null;
    }


    private String getAdditionCharge(INVOrder invOrder) {
        if (invOrder.getMessage().getMonetary() != null) {
            if (invOrder.getMessage().getMonetary().getMonetaryInformation() != null) {
                return invOrder.getMessage().getMonetary().getMonetaryInformation().getAmount();
            }
        }
        return null;
    }


    private String getAdditionChargeCode(INVOrder invOrder) {
        if (invOrder.getMessage().getAllowanceOrCharge() != null) {
            if (invOrder.getMessage().getAllowanceOrCharge().getSpecialServiceIdentification() != null) {
                return invOrder.getMessage().getAllowanceOrCharge().getSpecialServiceIdentification().getSpecialServiceCode();
            }
        }
        return null;
    }


    /**
     * This method returns ListPrice from the List of PriceInformation got from lineItemOrder.
     * If there are no PriceInformation then it return null.
     *
     * @param lineItemOrder
     * @return Price
     */
    private String getListPrice(LineItemOrder lineItemOrder) {

        List<MonetaryDetail> monetaryDetails = lineItemOrder.getMonetaryDetail();
        if (CollectionUtils.isNotEmpty((monetaryDetails))) {
            MonetaryDetail monetaryDetail = monetaryDetails.get(0);
            if (CollectionUtils.isNotEmpty(monetaryDetail.getMonetaryLineItemInformation())) {
                MonetaryLineItemInformation monetaryLineItemInformation = monetaryDetail.getMonetaryLineItemInformation().get(0);
                if (monetaryLineItemInformation.getAmountType() != null && monetaryLineItemInformation.getAmountType().contains("2")) {
                    return monetaryLineItemInformation.getAmount();
                }
            }
        }
        return null;
    }

    private String getPrice(LineItemOrder lineItemOrder) {
        List<PriceInformation> priceInformation = lineItemOrder.getPriceInformation();
        if (CollectionUtils.isNotEmpty(priceInformation)) {
            List<ItemPrice> itemPrices = priceInformation.get(0).getItemPrice();
            if (CollectionUtils.isNotEmpty(itemPrices)) {
                ItemPrice itemPrice = itemPrices.get(0);
                String priceCode = itemPrice.getGrossPrice();
                if (priceCode != null && (priceCode.equalsIgnoreCase("AAB") || priceCode.equalsIgnoreCase("CAL"))) {
                    return itemPrice.getPrice();
                }
            }
        }
        return null;
    }


    /**
     * This method returns the Quantity from the list of QuantityInformation got from lineItemOrder.
     * If there are no QuantityInformation then it return null.
     *
     * @param lineItemOrder
     * @return Quantity
     */
    private String getQuantity(LineItemOrder lineItemOrder) {
        List<QuantityInformation> quantityInformation = lineItemOrder.getQuantityInformation();
        if (CollectionUtils.isNotEmpty(quantityInformation)) {
            List<Qunatity> qunatities = quantityInformation.get(0).getQunatity();
            if (CollectionUtils.isNotEmpty(qunatities)) {
                return qunatities.get(0).getQuantity();
            }
        }
        return null;
    }

    private String getVendorIdentifierByPIA(LineItemOrder lineItemOrder) {
        List<ProductFunction> productFunctions = lineItemOrder.getProductFunction();
        if (CollectionUtils.isNotEmpty(productFunctions)) {
            for (int index = 0; index < productFunctions.size(); index++) {
                ProductFunction productFunction = productFunctions.get(index);
                if (CollectionUtils.isNotEmpty(productFunction.getProductArticleNumber())) {
                    ProductArticleNumber productArticleNumber = productFunction.getProductArticleNumber().get(0);
                    if (productArticleNumber.getProductItemNumberType().equalsIgnoreCase("SA")) {
                        return productArticleNumber.getProductIsbn();
                    }
                }
            }
        }
        return null;
    }

    public String getVendorItemIdentifier(LineItemOrder lineItemOrder) {
        List<SupplierReferenceInformation> supplierReferenceInformationList = lineItemOrder.getSupplierReferenceInformation();
        if (CollectionUtils.isNotEmpty(supplierReferenceInformationList)) {
            for (int index = 0; index < supplierReferenceInformationList.size(); index++) {
                SupplierReferenceInformation supplierReferenceInformation = supplierReferenceInformationList.get(index);
                List<SupplierLineItemReference> supplierLineItemReferenceList = supplierReferenceInformation.getSupplierLineItemReference();
                if (CollectionUtils.isNotEmpty(supplierLineItemReferenceList)) {
                    SupplierLineItemReference supplierLineItemReferenceRef = supplierLineItemReferenceList.get(0);
                    String supplierOrderLine = supplierLineItemReferenceRef.getSuppliersOrderLine();
                    String vendorItemReference = supplierLineItemReferenceRef.getVendorReferenceNumber();
                    if (supplierOrderLine.equals("SNA")) {
                        return vendorItemReference;
                    }
                }
            }
        }
        return null;
    }

    public String getBFNNumber(LineItemOrder lineItemOrder) {
        List<SupplierReferenceInformation> supplierReferenceInformationList = lineItemOrder.getSupplierReferenceInformation();
        if (CollectionUtils.isNotEmpty(supplierReferenceInformationList)) {
            for (int index = 0; index < supplierReferenceInformationList.size(); index++) {
                SupplierReferenceInformation supplierReferenceInformation = supplierReferenceInformationList.get(index);
                List<SupplierLineItemReference> supplierLineItemReferenceList = supplierReferenceInformation.getSupplierLineItemReference();
                if (CollectionUtils.isNotEmpty(supplierLineItemReferenceList)) {
                    SupplierLineItemReference supplierLineItemReferenceRef = supplierLineItemReferenceList.get(0);
                    String supplierOrderLine = supplierLineItemReferenceRef.getSuppliersOrderLine();
                    String vendorItemReference = supplierLineItemReferenceRef.getVendorReferenceNumber();
                    if (supplierOrderLine.equals("BFN")) {
                        return vendorItemReference;
                    }
                }
            }
        }
        return null;
    }

    private String getVendorAlias(INVOrder invOrder) throws Exception {
        if (null != invOrder.getMessage()) {
            SupplierAdditionalPartyIdentifier supplierAdditionalPartyIdentifier = invOrder.getMessage().getSupplierAdditionalPartyIdentifier();
            if (supplierAdditionalPartyIdentifier != null && supplierAdditionalPartyIdentifier.getSupplierIdentifier() != null
                    && StringUtils.isNotEmpty(supplierAdditionalPartyIdentifier.getSupplierIdentifier().getReferenceQualifier())
                    && supplierAdditionalPartyIdentifier.getSupplierIdentifier().getReferenceQualifier().equalsIgnoreCase("API")) {
                return supplierAdditionalPartyIdentifier.getSupplierIdentifier().getReferenceNumber();
            }
        }
        return null;
    }

    /**
     * This method gets the vendor Number from invoice Order.
     *
     * @param invOrder
     * @return
     */
    private String getVendorNumber(INVOrder invOrder, OleInvoiceRecord oleInvoiceRecord) throws Exception {
        try {
            if (null != invOrder.getMessage() && CollectionUtils.isNotEmpty(invOrder.getMessage().getPartyQualifier())) {
                for (int index = 0; index < invOrder.getMessage().getPartyQualifier().size(); index++) {
                    PartyQualifier partyQualifier = invOrder.getMessage().getPartyQualifier().get(index);
                    if (partyQualifier.getPartyCode() != null && (partyQualifier.getPartyCode().equalsIgnoreCase("SR") || partyQualifier.getPartyCode().equalsIgnoreCase("SU"))) {
                        if (StringUtils.isNotEmpty(partyQualifier.getPartyInformation().getCodeIdentification())) {
                            return partyQualifier.getPartyInformation().getCodeIdentification();
                        }
                    }
                }
            }
        } catch (Exception e) {
            if (oleInvoiceRecord.getVendorItemIdentifier() == null)
                throw new Exception("Edifact file has no vendor number for invoice number:: " + getInvoiceNumber(invOrder) + " and invoice date:: " + getInvoiceDate(invOrder), e);
        }
        return null;
    }


    /**
     * This method gets the invoice Number from invoice Order.
     *
     * @param invOrder
     * @return
     */
    private String getInvoiceNumber(INVOrder invOrder) throws Exception {
        try {
            return invOrder.getMessage().getMessageBeginning().getMessageBeginningInterchangeControlReference();
        } catch (Exception e) {
            throw new Exception("Edifact file has no invoice number.", e);
        }
    }

    /**
     * This method gets the invoice date from invoice Order.
     *
     * @param invOrder
     * @return
     */
    private String getInvoiceDate(INVOrder invOrder) throws Exception {
        try {
            return invOrder.getMessage().getMessageCreationInformation().getMessageCreationInfoDetails().getMessageCreationInfoDate();
        } catch (Exception e) {
            throw new Exception("Edifact file has no invoice date.", e);
        }
    }

    private String getVendorInvoiceAmount(INVOrder invOrder) {
        Summary summary = invOrder.getSummary();
        if (summary != null && CollectionUtils.isNotEmpty(summary.getMonetarySummary())) {
            for (int index = 0; index < summary.getMonetarySummary().size(); index++) {
                MonetarySummary monetarySummary = (MonetarySummary) summary.getMonetarySummary().get(index);
                if (CollectionUtils.isNotEmpty(monetarySummary.getMonetarySummaryInformation())) {
                    for (int innerIndex = 0; innerIndex < monetarySummary.getMonetarySummaryInformation().size(); innerIndex++) {
                        MonetarySummaryInformation monetarySummaryInformation = monetarySummary.getMonetarySummaryInformation().get(0);
                        if (monetarySummaryInformation.getAmountType().equalsIgnoreCase("86")) {
                            return monetarySummaryInformation.getAmount();
                        }
                    }
                }
            }
        }
        return null;
    }

    private String getLineItemISBN(LineItemOrder lineItemOrder) {
        List<LineItem> lineItems = lineItemOrder.getLineItem();
        if (CollectionUtils.isNotEmpty(lineItems)) {
            LineItem lineItem = lineItems.get(0);
            if (lineItem != null && CollectionUtils.isNotEmpty(lineItem.getLineItemArticleNumber()) && lineItem.getLineItemArticleNumber().get(0) != null) {
                return lineItem.getLineItemArticleNumber().get(0).getLineItemIsbn();
            }
        }
        return null;
    }

    private String getISBN(LineItemOrder lineItemOrder) {
        if (CollectionUtils.isNotEmpty(lineItemOrder.getProductFunction())) {
            ProductFunction productFunction = lineItemOrder.getProductFunction().get(0);
            if (CollectionUtils.isNotEmpty(productFunction.getProductArticleNumber())) {
                ProductArticleNumber productArticleNumber = productFunction.getProductArticleNumber().get(0);
                if (productArticleNumber.getProductItemNumberType() != null) {
                    return productArticleNumber.getProductIsbn();
                }
                if (productArticleNumber.getProductItemNumberType() == null &&
                        productArticleNumber.getProductIsbn() != null) {
                    return productArticleNumber.getProductIsbn();
                }
            }
        }
        return null;
    }

    private String getISSN(LineItemOrder lineItemOrder) {
        if (CollectionUtils.isNotEmpty(lineItemOrder.getProductFunction())) {
            ProductFunction productFunction = lineItemOrder.getProductFunction().get(0);
            if (CollectionUtils.isNotEmpty(productFunction.getSupplierArticleNumber())) {
                return productFunction.getSupplierArticleNumber().get(0).getIsbn();
            }
        }
        return null;
    }


    private String getItemDescription(LineItemOrder lineItemOrder) {
        String description = "";
        if (CollectionUtils.isNotEmpty(lineItemOrder.getItemDescriptionList())) {
            for (int index = 0; index < lineItemOrder.getItemDescriptionList().size(); index++) {
                ItemDescription itemDescription = lineItemOrder.getItemDescriptionList().get(index);
                String itemCharacteristicCode = itemDescription.getItemCharacteristicCode();
                if (StringUtils.isNotBlank(itemCharacteristicCode) && itemCharacteristicCode.equals("050") || itemCharacteristicCode.equals("JTI")) {
                    if (StringUtils.isNotBlank(description)) {
                        description = description + itemDescription.getData();
                    } else {
                        description = itemDescription.getData();
                    }
                }
            }
            return description;
        }
        return null;
    }

    private List getItemNote(LineItemOrder lineItemOrder) {
        List itemNoteList = new ArrayList();

        if (CollectionUtils.isNotEmpty(lineItemOrder.getItemDescriptionList())) {
            for (int index = 0; index < lineItemOrder.getItemDescriptionList().size(); index++) {
                ItemDescription itemDescription = lineItemOrder.getItemDescriptionList().get(index);
                if (itemDescription.getItemCharacteristicCode() != null &&
                        itemDescription.getItemCharacteristicCode().contains("08")) {
                    OleInvoiceNote oleInvoiceNote = new OleInvoiceNote();
                    oleInvoiceNote.setNote(itemDescription.getData());
                    itemNoteList.add(oleInvoiceNote);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(lineItemOrder.getAllowanceMonetaryDetail())) {
            AllowanceMonetaryDetail allowanceMonetaryDetail = lineItemOrder.getAllowanceMonetaryDetail().get(0);
            if (allowanceMonetaryDetail.getAllowanceMonetaryLineItemInformation() != null) {
                OleInvoiceNote oleInvoiceNote = new OleInvoiceNote();
                oleInvoiceNote.setNote("The service charge for this item is $" + allowanceMonetaryDetail.getAllowanceMonetaryLineItemInformation().get(0).getAmount());
                itemNoteList.add(oleInvoiceNote);
            }
        }
        return itemNoteList;
    }

    private String getSubscriptionDateFrom(LineItemOrder lineItemOrder) {
        String subScriptionDate = getSubScriptionDateFromDateTimeDetails(lineItemOrder.getDateTimeDetail(), 0);
        if(StringUtils.isBlank(subScriptionDate)) {
            List<ItemDescription> itemDescriptionList = lineItemOrder.getItemDescriptionList();
            subScriptionDate = getSubScriptionDateFromDescription(itemDescriptionList, "085");
        }
        if(StringUtils.isNotBlank(subScriptionDate)) {
            try {
                Date date = getBatchDateTimeUtil().convertToDate(subScriptionDate);
                if(subScriptionDate.length() == 6) {
                    date = processDate(date, "from");
                }
                return OleNGConstants.DATE_FORMAT_WITHOUT_TIME.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String getSubScriptionDateFromDateTimeDetails(List<DateTimeDetail> dateTimeDetails, int index) {
        if(CollectionUtils.isNotEmpty(dateTimeDetails)) {
            DateTimeDetail dateTimeDetail = dateTimeDetails.get(index);
            if (null != dateTimeDetail) {
                List<DateTimeInformation> dateTimeInformationList = dateTimeDetail.getDateTimeInformationList();
                if(CollectionUtils.isNotEmpty(dateTimeInformationList)) {
                    DateTimeInformation dateTimeInformation = dateTimeInformationList.get(0);
                    if(null != dateTimeInformation) {
                        return getDateString(dateTimeInformation.getPeriod());
                    }
                }
            }
        }
        return null;
    }

    private String getSubScriptionDateFromDescription(List<ItemDescription> itemDescriptionList, String code) {
        if(CollectionUtils.isNotEmpty(itemDescriptionList)) {
            for (Iterator<ItemDescription> iterator = itemDescriptionList.iterator(); iterator.hasNext(); ) {
                ItemDescription itemDescription = iterator.next();
                String itemCharacteristicCode = itemDescription.getItemCharacteristicCode();
                if(StringUtils.equals(itemCharacteristicCode, code)) {
                    return getDateString(itemDescription.getData());
                }
            }
        }
        return null;
    }

    private Date processDate(Date date, String type) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if(StringUtils.equals(type, "from")) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        } else if(StringUtils.equals(type, "to")) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        }
        return calendar.getTime();
    }

    private String getSubscriptionDateTo(LineItemOrder lineItemOrder) {
        String subScriptionDate = getSubScriptionDateFromDateTimeDetails(lineItemOrder.getDateTimeDetail(), 1);
        if(StringUtils.isBlank(subScriptionDate)) {
            List<ItemDescription> itemDescriptionList = lineItemOrder.getItemDescriptionList();
            subScriptionDate = getSubScriptionDateFromDescription(itemDescriptionList, "086");
        }
        if(StringUtils.isNotBlank(subScriptionDate)) {
            try {
                Date date = getBatchDateTimeUtil().convertToDate(subScriptionDate);
                if(subScriptionDate.length() == 6) {
                    date = processDate(date, "to");
                }
                return OleNGConstants.DATE_FORMAT_WITHOUT_TIME.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String getDateString(String data) {
        if(StringUtils.isNotBlank(data)) {
            String[] split = data.split(" ");
            if(null != split && split.length > 0) {
                return split[0].replaceAll(":::","");
            }
        }
        return null;
    }

    private String getSummaryCharge(INVOrder invOrder) {
        if (invOrder.getSummary() != null) {
            List<MonetarySummary> monetarySummary = invOrder.getSummary().getMonetarySummary();
            if (CollectionUtils.isNotEmpty(monetarySummary)) {
                List<MonetarySummaryInformation> monetarySummaryInformation = monetarySummary.get(monetarySummary.size() - 1).getMonetarySummaryInformation();
                if (CollectionUtils.isNotEmpty(monetarySummaryInformation)) {
                    monetarySummaryInformation.get(0).getAmount();
                }
            }
        }
        return null;
    }

    private boolean isNumeric(String str) {
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();
    }


    private String getSubscriptionPeriod(LineItemOrder lineItemOrder) {
        StringBuilder subscriptionPeriod = new StringBuilder();
        if (CollectionUtils.isNotEmpty(lineItemOrder.getItemDescriptionList())) {
            for (int index = 0; index < lineItemOrder.getItemDescriptionList().size(); index++) {
                ItemDescription itemDescription = lineItemOrder.getItemDescriptionList().get(index);
                if (itemDescription.getItemCharacteristicCode() != null &&
                        itemDescription.getItemCharacteristicCode().contains("08")) {
                    if(StringUtils.isNotBlank(subscriptionPeriod.toString())) {
                        subscriptionPeriod.append("-");
                    }
                    String data = itemDescription.getData();
                    data = data.replaceAll(":::","");
                    subscriptionPeriod.append(data);
                }
            }
        }
        return subscriptionPeriod.toString();
    }

    public BatchDateTimeUtil getBatchDateTimeUtil() {
        if(null == batchDateTimeUtil) {
            batchDateTimeUtil = new BatchDateTimeUtil();
        }
        return batchDateTimeUtil;
    }

    public void setBatchDateTimeUtil(BatchDateTimeUtil batchDateTimeUtil) {
        this.batchDateTimeUtil = batchDateTimeUtil;
    }
}