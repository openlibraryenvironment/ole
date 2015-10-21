package org.kuali.ole.ingest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.pojo.OleInvoiceRecord;
import org.kuali.ole.pojo.edi.*;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.OleInvoiceNote;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.vnd.businessobject.OleCurrencyType;
import org.kuali.ole.vnd.businessobject.VendorAlias;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.LookupService;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: palanivel
 * Date: 7/27/13
 * Time: 6:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleInvoiceRecordBuilder {
    private static final Logger LOG = Logger.getLogger(OleInvoiceRecordBuilder.class);
    private static OleInvoiceRecordBuilder oleInvoiceRecordBuilder;
    protected BusinessObjectService businessObjectService;


    /**
     * default constructor of OleInvoiceRecordBuilder.
     */
    private OleInvoiceRecordBuilder(){

    }

    /**
     *  Gets the instance of OleInvoiceRecordBuilder.
     *  If OleInvoiceRecordBuilder is null it returns new instance else it returns existing instance.
     * @return
     */
    public static OleInvoiceRecordBuilder getInstance() {
        if (null == oleInvoiceRecordBuilder) {
            oleInvoiceRecordBuilder = new OleInvoiceRecordBuilder();
        }
        return oleInvoiceRecordBuilder;
    }

    protected BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            businessObjectService = SpringContext.getBean(BusinessObjectService.class);
        }
        return businessObjectService;
    }

    private LookupService getLookupService() {
        return KRADServiceLocatorWeb.getLookupService();
    }


    public OleInvoiceRecord build(LineItemOrder lineItemOrder, INVOrder invOrder) throws Exception {

        OleInvoiceRecord oleInvoiceRecord = new OleInvoiceRecord();
        String listPrice = getListPrice(lineItemOrder);
        oleInvoiceRecord.setListPrice(listPrice!=null?listPrice:getPrice(lineItemOrder));
        oleInvoiceRecord.setUnitPrice(populateUnitPrice(lineItemOrder));
        oleInvoiceRecord.setQuantity(getQuantity(lineItemOrder));
        oleInvoiceRecord.setVendorItemIdentifier(getVendorItemIdentifier(lineItemOrder)!=null?getVendorItemIdentifier(lineItemOrder):getVendorIdentifierByPIA(lineItemOrder));
        oleInvoiceRecord.setBfnNumber(getBFNNumber(lineItemOrder));
        oleInvoiceRecord.setVendorNumber(getVendorNumber(invOrder,oleInvoiceRecord));
        if(StringUtils.isBlank(oleInvoiceRecord.getVendorNumber())){
           oleInvoiceRecord.setVendorAlias(getVendorAlias(invOrder));
           oleInvoiceRecord.setVendorNumber(getVendorNumberFromVendorAlias(oleInvoiceRecord,invOrder));
        }
        oleInvoiceRecord.setItemChartCode(OLEConstants.OleInvoiceImport.ITM_CHART_CODE);
        oleInvoiceRecord.setInvoiceNumber(getInvoiceNumber(invOrder));
        oleInvoiceRecord.setInvoiceDate(getInvoiceDate(invOrder));
        oleInvoiceRecord.setVendorInvoiceAmount(getVendorInvoiceAmount(invOrder));           // If vendor is foreign vendor, foreign vendor invoice amount is populated and vendor invoice amount is null.
        // oleInvoiceRecord.setForeignVendorInvoiceAmount(getForeignInvoiceAmount(invOrder));   // If vendor is foreign vendor, foreign vendor invoice amount is populated and vendor invoice amount is null.
        // oleInvoiceRecord.setAdditionalCharge(getForeignInvoiceAmount(invOrder));
        oleInvoiceRecord.setISBN(getISBN(lineItemOrder) == null? getLineItemISBN(lineItemOrder):getISBN(lineItemOrder));
        oleInvoiceRecord.setISSN(getISSN(lineItemOrder));
        //oleInvoiceRecord.setItemType(OLEConstants.OleInvoiceImport.QTY_TYP);
        oleInvoiceRecord.setBillToCustomerID(populateBillToCustomerId(invOrder));
        oleInvoiceRecord.setItemDescription(getItemDescription(lineItemOrder));
        oleInvoiceRecord.setItemNote(getItemNote(lineItemOrder));
        oleInvoiceRecord.setNumberOfCopiesOrdered(oleInvoiceRecord.getQuantity());
        //oleInvoiceRecord.setNumberOfParts(OLEConstants.OleInvoiceImport.NO_PARTS);
        oleInvoiceRecord.setSubscriptionPeriodFrom(getSubscriptionDateFrom(lineItemOrder));
        oleInvoiceRecord.setSubscriptionPeriodTo(getSubscriptionDateTo(lineItemOrder));
        oleInvoiceRecord.setSummaryAmount(getSummaryCharge(invOrder));
        oleInvoiceRecord.setAdditionalChargeCode(getAdditionChargeCode(invOrder));             // Additional charge code from header Level.
        oleInvoiceRecord.setAdditionalCharge(getAdditionCharge(invOrder));                     // Monetary amount for additional charge from header level.
        oleInvoiceRecord.setLineItemAdditionalChargeCode(populateLineItemChargeCode(lineItemOrder));    // Additional charge code from line item Level.
        oleInvoiceRecord.setLineItemAdditionalCharge(populateLineItemAdditionalCharge(lineItemOrder));  // Monetary amount for additional charge from line item level.
        oleInvoiceRecord.setPurchaseOrderNumber(getPurchaseOrderNumber(lineItemOrder));
        setCurrencyDetails(invOrder,oleInvoiceRecord);
        return  oleInvoiceRecord;
    }

    private String getVendorNumberFromVendorAlias(OleInvoiceRecord oleInvoiceRecord,INVOrder invOrder) throws Exception{
        if(StringUtils.isNotBlank(oleInvoiceRecord.getVendorAlias())){
            Map vendorAliasMap = new HashMap();
            vendorAliasMap.put(org.kuali.ole.sys.OLEConstants.VENDOR_ALIAS_NAME, oleInvoiceRecord.getVendorAlias());
            List<VendorAlias> vendorAliasList = (List<VendorAlias>) getLookupService().findCollectionBySearchHelper(VendorAlias.class, vendorAliasMap, true);
            if (vendorAliasList != null && vendorAliasList.size() > 0) {
                return vendorAliasList.get(0).getVendorHeaderGeneratedIdentifier() + "-" + vendorAliasList.get(0).getVendorDetailAssignedIdentifier();

            }else{
                if(oleInvoiceRecord.getVendorItemIdentifier() == null)
                    throw new Exception("The vendor alias in Edifact file doesn't match in database for invoice number:: "+getInvoiceNumber(invOrder)+" and invoice date:: "+getInvoiceDate(invOrder));
            }
        }
        return null;
    }

    private void setCurrencyDetails(INVOrder invOrder,OleInvoiceRecord oleInvoiceRecord){
        if(invOrder.getMessage() != null && invOrder.getMessage().getCurrencyDetails() != null && invOrder.getMessage().getCurrencyDetails().getCurrencyDetailsSupplierInformation() != null){
            if(!StringUtils.isBlank(invOrder.getMessage().getCurrencyDetails().getCurrencyDetailsSupplierInformation().getCurrencyType())){
                Map<String,String> currencyTypeMap = new HashMap<>();
                currencyTypeMap.put(OLEConstants.CURR_ALPHA_CD, invOrder.getMessage().getCurrencyDetails().getCurrencyDetailsSupplierInformation().getCurrencyType());
                List<OleCurrencyType> currencyTypeList = (List) getBusinessObjectService().findMatching(OleCurrencyType.class, currencyTypeMap);
                if(currencyTypeList != null && currencyTypeList.size() >0){
                    oleInvoiceRecord.setCurrencyTypeId(currencyTypeList.get(0).getCurrencyTypeId().toString());
                    oleInvoiceRecord.setCurrencyType(currencyTypeList.get(0).getCurrencyType());
                    if (!oleInvoiceRecord.getCurrencyType().equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                        oleInvoiceRecord.setForeignListPrice(oleInvoiceRecord.getListPrice());
                    }
                }
            }
        }
    }

    private String populateBillToCustomerId(INVOrder invOrder){
        if(invOrder.getMessage() != null && invOrder.getMessage().getPartyQualifier() != null && invOrder.getMessage().getPartyQualifier().size() > 0){
            for(int i=0;i<invOrder.getMessage().getPartyQualifier().size();i++){
                if(invOrder.getMessage().getPartyQualifier().get(i).getPartyCode()!=null && invOrder.getMessage().getPartyQualifier().get(i).getPartyCode().equalsIgnoreCase("BY")){
                    return invOrder.getMessage().getPartyQualifier().get(i).getPartyInformation().getCodeIdentification();
                } else if(invOrder.getMessage() != null && invOrder.getMessage().getBuyerAdditionalPartyIdentifier() != null && invOrder.getMessage().getBuyerAdditionalPartyIdentifier().getBuyerIdentifier() != null &&
                        invOrder.getMessage().getBuyerAdditionalPartyIdentifier().getBuyerIdentifier().getBuyerReferenceQualifier().equalsIgnoreCase("API")){
                    return invOrder.getMessage().getBuyerAdditionalPartyIdentifier().getBuyerIdentifier().getBuyerReferenceNumber();
                }
            }
        }
        return null;
    }

    private Integer getPurchaseOrderNumber(LineItemOrder lineItemOrder)throws Exception{
        if(lineItemOrder.getSupplierReferenceInformation() != null && lineItemOrder.getSupplierReferenceInformation().size() > 0){
            for(int i=0;i<lineItemOrder.getSupplierReferenceInformation().size();i++){
                if(lineItemOrder.getSupplierReferenceInformation().get(i).getSupplierLineItemReference() != null &&
                        lineItemOrder.getSupplierReferenceInformation().get(i).getSupplierLineItemReference().size() >0){
                    for(int j=0;j<lineItemOrder.getSupplierReferenceInformation().get(i).getSupplierLineItemReference().size();j++){
                        if(lineItemOrder.getSupplierReferenceInformation().get(i).getSupplierLineItemReference().get(j).getSuppliersOrderLine() != null &&
                                (lineItemOrder.getSupplierReferenceInformation().get(i).getSupplierLineItemReference().get(j).getSuppliersOrderLine().equals("ON")||lineItemOrder.getSupplierReferenceInformation().get(i).getSupplierLineItemReference().get(j).getSuppliersOrderLine().equals("LI"))){
                                try{
                                    if (lineItemOrder.getSupplierReferenceInformation().get(i).getSupplierLineItemReference().get(j).getVendorReferenceNumber() != null &&
                                            isNumeric(lineItemOrder.getSupplierReferenceInformation().get(i).getSupplierLineItemReference().get(j).getVendorReferenceNumber())) {
                                        return Integer.parseInt(lineItemOrder.getSupplierReferenceInformation().get(i).getSupplierLineItemReference().get(j).getVendorReferenceNumber());
                                    }
                                    else {
                                        return 0;
                                    }


                                }catch(Exception e){
                                    LOG.error("Purchase Order Number should be a number but invoice file has :"+lineItemOrder.getSupplierReferenceInformation().get(i).getSupplierLineItemReference().get(j).getVendorReferenceNumber());
                                      throw new Exception("Purchase Order Number should be a number but invoice file has :"+lineItemOrder.getSupplierReferenceInformation().get(i).getSupplierLineItemReference().get(j).getVendorReferenceNumber(),e);
                                }
                        }
                    }

                }
            }

        }
        /*if(invOrder.getMessage().getPurchaseOrderQualifier() != null){
            if(invOrder.getMessage().getPurchaseOrderQualifier().getPurchaseOrderReference() != null){
                return Integer.parseInt(invOrder.getMessage().getPurchaseOrderQualifier().getPurchaseOrderReference().getPurchaseOrderNumber());
            }
        }*/
        return null;
    }

    private String populateUnitPrice(LineItemOrder lineItemOrder){
        if(lineItemOrder.getMonetaryDetail() != null){
            if(lineItemOrder.getMonetaryDetail().get(0).getMonetaryLineItemInformation() != null){
                return lineItemOrder.getMonetaryDetail().get(0).getMonetaryLineItemInformation().get(0).getAmount();
            }
        }
        return null;
    }

    private String populateLineItemChargeCode(LineItemOrder lineItemOrder){
        if(lineItemOrder.getLineItemAllowanceOrCharge() != null){
            if(lineItemOrder.getLineItemAllowanceOrCharge().get(0).getLineItemSpecialServiceIdentification()!= null){
                return lineItemOrder.getLineItemAllowanceOrCharge().get(0).getLineItemSpecialServiceIdentification().get(0).getSpecialServiceCode();
            }
        }
        return null;
    }

    private String populateLineItemAdditionalCharge(LineItemOrder lineItemOrder){
        if(lineItemOrder.getAllowanceMonetaryDetail()!= null){
            if(lineItemOrder.getAllowanceMonetaryDetail().get(0).getAllowanceMonetaryLineItemInformation() != null){
                return "0";//lineItemOrder.getAllowanceMonetaryDetail().get(0).getAllowanceMonetaryLineItemInformation().get(0).getAmount();
            }
        }
        return null;
    }


    private String getAdditionCharge(INVOrder invOrder) {
        if(invOrder.getMessage().getMonetary() != null){
            if(invOrder.getMessage().getMonetary().getMonetaryInformation() != null){
                return invOrder.getMessage().getMonetary().getMonetaryInformation().getAmount();
            }
        }
        return null;
    }


    private String getAdditionChargeCode(INVOrder invOrder) {
        if(invOrder.getMessage().getAllowanceOrCharge() != null){
            if(invOrder.getMessage().getAllowanceOrCharge().getSpecialServiceIdentification() != null){
                return invOrder.getMessage().getAllowanceOrCharge().getSpecialServiceIdentification().getSpecialServiceCode();
            }
        }
        return null;
    }


    /**
     *  This method returns ListPrice from the List of PriceInformation got from lineItemOrder.
     *  If there are no PriceInformation then it return null.
     * @param lineItemOrder
     * @return  Price
     */
    private String getListPrice(LineItemOrder lineItemOrder) {

        List<MonetaryDetail> monetaryDetails = lineItemOrder.getMonetaryDetail();
        if(monetaryDetails != null && monetaryDetails.size() > 0){
            if(monetaryDetails.get(0).getMonetaryLineItemInformation()!= null && monetaryDetails.get(0).getMonetaryLineItemInformation().size()>0){
                if(monetaryDetails.get(0).getMonetaryLineItemInformation().get(0).getAmountType() != null && monetaryDetails.get(0).getMonetaryLineItemInformation().get(0).getAmountType().contains("2")){
                    return monetaryDetails.get(0).getMonetaryLineItemInformation().get(0).getAmount();
                }
            }
        }
        return null;
    }

    private String getPrice(LineItemOrder lineItemOrder){
        List<PriceInformation> priceInformation = lineItemOrder.getPriceInformation();
        if (priceInformation !=null && priceInformation.size() > 0) {
            List<ItemPrice> itemPrice = priceInformation.get(0).getItemPrice();
            if (itemPrice != null && itemPrice.size() > 0) {
                String priceCode = itemPrice.get(0).getGrossPrice();
                if(priceCode != null && (priceCode.equalsIgnoreCase("AAB") || priceCode.equalsIgnoreCase("CAL"))){
                    return itemPrice.get(0).getPrice();
                }
            }
        }
       return null;
    }


    /**
     * This method returns the Quantity from the list of QuantityInformation got from lineItemOrder.
     * If there are no QuantityInformation then it return null.
     * @param lineItemOrder
     * @return Quantity
     */
    private String getQuantity(LineItemOrder lineItemOrder) {
        List<QuantityInformation> quantityInformation = lineItemOrder.getQuantityInformation();
        if (quantityInformation != null && quantityInformation.size() > 0) {
            List<Qunatity> qunatity = quantityInformation.get(0).getQunatity();
            if (qunatity.size() > 0) {
                return qunatity.get(0).getQuantity();
            }
        }
        return null;
    }

    private String getVendorIdentifierByPIA(LineItemOrder lineItemOrder) {
        List<ProductFunction> productFunction = lineItemOrder.getProductFunction();
        if(productFunction != null && productFunction.size()>0){
            for(int i=0;i<productFunction.size();i++){
                if(productFunction.get(i).getProductArticleNumber() != null && productFunction.get(i).getProductArticleNumber().size()>0){
                    if(productFunction.get(i).getProductArticleNumber().get(0).getProductItemNumberType().equalsIgnoreCase("SA")){
                        return productFunction.get(i).getProductArticleNumber().get(0).getProductIsbn();
                    }
                }
            }
        }
        return null;
    }

    public String getVendorItemIdentifier(LineItemOrder lineItemOrder) {
        List<SupplierReferenceInformation> supplierReferenceInformationList = lineItemOrder.getSupplierReferenceInformation();
        if (supplierReferenceInformationList != null && supplierReferenceInformationList.size() > 0) {
            for(int i=0;i<supplierReferenceInformationList.size();i++){
                SupplierReferenceInformation supplierReferenceInformation = supplierReferenceInformationList.get(i);
                List<SupplierLineItemReference> supplierLineItemReferenceList = supplierReferenceInformation.getSupplierLineItemReference();
                if (supplierLineItemReferenceList != null && supplierLineItemReferenceList.size() > 0) {
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
        if (supplierReferenceInformationList != null && supplierReferenceInformationList.size() > 0) {
            for(int i=0;i<supplierReferenceInformationList.size();i++){
                SupplierReferenceInformation supplierReferenceInformation = supplierReferenceInformationList.get(i);
                List<SupplierLineItemReference> supplierLineItemReferenceList = supplierReferenceInformation.getSupplierLineItemReference();
                if (supplierLineItemReferenceList != null && supplierLineItemReferenceList.size() > 0) {
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

    private String getVendorAlias(INVOrder invOrder) throws Exception{
       if(invOrder.getMessage().getSupplierAdditionalPartyIdentifier() != null && invOrder.getMessage().getSupplierAdditionalPartyIdentifier().getSupplierIdentifier() != null && StringUtils.isNotEmpty(invOrder.getMessage().getSupplierAdditionalPartyIdentifier().getSupplierIdentifier().getReferenceQualifier()) && invOrder.getMessage().getSupplierAdditionalPartyIdentifier().getSupplierIdentifier().getReferenceQualifier().equalsIgnoreCase("API")){
          return invOrder.getMessage().getSupplierAdditionalPartyIdentifier().getSupplierIdentifier().getReferenceNumber();
       }
       return null;
    }

    /**
     *  This method gets the vendor Number from invoice Order.
     * @param invOrder
     * @return
     */
    private String getVendorNumber(INVOrder invOrder,OleInvoiceRecord oleInvoiceRecord) throws Exception{
        try{
            for(int i=0;i<invOrder.getMessage().getPartyQualifier().size();i++){
                if(invOrder.getMessage().getPartyQualifier().get(i).getPartyCode()!=null && (invOrder.getMessage().getPartyQualifier().get(i).getPartyCode().equalsIgnoreCase("SR") || invOrder.getMessage().getPartyQualifier().get(i).getPartyCode().equalsIgnoreCase("SU"))){
                    if(StringUtils.isNotEmpty(invOrder.getMessage().getPartyQualifier().get(i).getPartyInformation().getCodeIdentification())){
                      return invOrder.getMessage().getPartyQualifier().get(i).getPartyInformation().getCodeIdentification();
                    }
                }
            }
        }catch (Exception e){
            if(oleInvoiceRecord.getVendorItemIdentifier() == null)
                throw new Exception("Edifact file has no vendor number for invoice number:: "+getInvoiceNumber(invOrder)+" and invoice date:: "+getInvoiceDate(invOrder),e);
        }
        return null;
    }


    /**
     *  This method gets the invoice Number from invoice Order.
     * @param invOrder
     * @return
     */
    private String getInvoiceNumber(INVOrder invOrder)throws Exception{
        try{
            return invOrder.getMessage().getMessageBeginning().getMessageBeginningInterchangeControlReference();
        }catch (Exception e){
            throw new Exception("Edifact file has no invoice number.",e);
        }
    }

    /**
     *  This method gets the invoice date from invoice Order.
     * @param invOrder
     * @return
     */
    private String getInvoiceDate(INVOrder invOrder) throws Exception{
        try{
            return invOrder.getMessage().getMessageCreationInformation().getMessageCreationInfoDetails().getMessageCreationInfoDate();
        }catch (Exception e){
            throw new Exception("Edifact file has no invoice date.",e);
        }
    }


    private String getForeignInvoiceAmount(INVOrder invOrder){
        // return invOrder.getMessage().getMonetary().getMonetaryInformation().getAmount();
        return null;
    }

    private String getVendorInvoiceAmount(INVOrder invOrder){
        if(invOrder.getSummary() != null && invOrder.getSummary().getMonetarySummary() != null && invOrder.getSummary().getMonetarySummary().size() > 0){
            for(int i=0;i<invOrder.getSummary().getMonetarySummary().size();i++){
                MonetarySummary monetarySummary = (MonetarySummary)invOrder.getSummary().getMonetarySummary().get(i);
                if(monetarySummary.getMonetarySummaryInformation()!=null && monetarySummary.getMonetarySummaryInformation().size()>0){
                    for(int j=0;j<monetarySummary.getMonetarySummaryInformation().size();j++){
                        if(monetarySummary.getMonetarySummaryInformation().get(0).getAmountType().equalsIgnoreCase("86")){
                            return monetarySummary.getMonetarySummaryInformation().get(0).getAmount();
                        }
                    }
                }
            }
        }
        return null;
    }

    private String getLineItemISBN(LineItemOrder lineItemOrder){
        if(lineItemOrder.getLineItem() != null && lineItemOrder.getLineItem().get(0)!= null){
            if(lineItemOrder.getLineItem().get(0).getLineItemArticleNumber() != null && lineItemOrder.getLineItem().get(0).getLineItemArticleNumber().get(0)!=null){
                return lineItemOrder.getLineItem().get(0).getLineItemArticleNumber().get(0).getLineItemIsbn();
            }
        }
        return null;
    }

    private String getISBN(LineItemOrder lineItemOrder){
        if(lineItemOrder.getProductFunction()!= null && lineItemOrder.getProductFunction().size() > 0){
            if(lineItemOrder.getProductFunction().get(0).getProductArticleNumber() != null && lineItemOrder.getProductFunction().get(0).getProductArticleNumber().size() > 0){
                if(lineItemOrder.getProductFunction().get(0).getProductArticleNumber().get(0).getProductItemNumberType() != null){
                    return lineItemOrder.getProductFunction().get(0).getProductArticleNumber().get(0).getProductIsbn();
                }
                if(lineItemOrder.getProductFunction().get(0).getProductArticleNumber().get(0).getProductItemNumberType() == null &&
                        lineItemOrder.getProductFunction().get(0).getProductArticleNumber().get(0).getProductIsbn() != null){
                    return lineItemOrder.getProductFunction().get(0).getProductArticleNumber().get(0).getProductIsbn();
                }
            }
        }
        return null;
    }

    private String getISSN(LineItemOrder lineItemOrder){
        if (lineItemOrder.getProductFunction()!= null && lineItemOrder.getProductFunction().size() > 0){
            if(lineItemOrder.getProductFunction().get(0).getSupplierArticleNumber() != null && lineItemOrder.getProductFunction().get(0).getSupplierArticleNumber().size() > 0){
                return lineItemOrder.getProductFunction().get(0).getSupplierArticleNumber().get(0).getIsbn();
            }
        }
        return null;
    }


    private String getItemDescription(LineItemOrder lineItemOrder){
        String description = "";
        if(lineItemOrder.getItemDescriptionList() != null && lineItemOrder.getItemDescriptionList().size() > 0){
            for(int i=0;i<lineItemOrder.getItemDescriptionList().size();i++) {
                if(lineItemOrder.getItemDescriptionList().get(i).getItemCharacteristicCode().equals("050")) {
                    if (StringUtils.isNotBlank(description)) {
                        description = description + lineItemOrder.getItemDescriptionList().get(i).getData();
                    } else {
                        description = lineItemOrder.getItemDescriptionList().get(i).getData();
                    }
                }
            }
            return description;
        }
        return null;
    }

    private List getItemNote(LineItemOrder lineItemOrder){
        List itemNoteList = new ArrayList();

        if(lineItemOrder.getItemDescriptionList() != null && lineItemOrder.getItemDescriptionList().size() > 0){
            for(int itemNote=0;itemNote<lineItemOrder.getItemDescriptionList().size();itemNote++){
                if(lineItemOrder.getItemDescriptionList().get(itemNote).getItemCharacteristicCode() != null &&
                        lineItemOrder.getItemDescriptionList().get(itemNote).getItemCharacteristicCode().contains("08")){
                    OleInvoiceNote oleInvoiceNote = new OleInvoiceNote();
                    oleInvoiceNote.setNote(lineItemOrder.getItemDescriptionList().get(itemNote).getData());
                    itemNoteList.add(oleInvoiceNote);
                }
            }
        }
        if(lineItemOrder.getAllowanceMonetaryDetail()!= null){
            if(lineItemOrder.getAllowanceMonetaryDetail().get(0).getAllowanceMonetaryLineItemInformation() != null){
                OleInvoiceNote oleInvoiceNote = new OleInvoiceNote();
                oleInvoiceNote.setNote("The service charge for this item is $" + lineItemOrder.getAllowanceMonetaryDetail().get(0).getAllowanceMonetaryLineItemInformation().get(0).getAmount());
                itemNoteList.add(oleInvoiceNote);
            }
        }
        return itemNoteList;
    }

    private String getSubscriptionDateFrom(LineItemOrder lineItemOrder){
        if(lineItemOrder.getDateTimeDetail() != null && lineItemOrder.getDateTimeDetail().size() > 0){
            if(lineItemOrder.getDateTimeDetail().get(0).getDateTimeInformationList() != null && lineItemOrder.getDateTimeDetail().get(0).getDateTimeInformationList().size() > 0){
                if(lineItemOrder.getDateTimeDetail().get(0).getDateTimeInformationList().get(0).getPeriod().length() == 8) {
                    return lineItemOrder.getDateTimeDetail().get(0).getDateTimeInformationList().get(0).getPeriod();
                }else{
                    return lineItemOrder.getDateTimeDetail().get(0).getDateTimeInformationList().get(0).getPeriod()+01;
                }
            }
        }
        return null;
    }

    private int getLastDayOfMonth(String date){
        SimpleDateFormat dateFromRawFile = new SimpleDateFormat(org.kuali.ole.OLEConstants.DATE_FORMAT);
        Date dt = null;
        try {
            dt = dateFromRawFile.parse(date);
        } catch (ParseException e) {
            LOG.error("Unable to parse Subscription End Date");
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        Date lastDayOfMonth = calendar.getTime();
        int day = lastDayOfMonth.getDate();
        return day;
    }

    private String getSubscriptionDateTo(LineItemOrder lineItemOrder){
        if(lineItemOrder.getDateTimeDetail()!= null && lineItemOrder.getDateTimeDetail().size() > 1){
            if(lineItemOrder.getDateTimeDetail().get(1).getDateTimeInformationList() != null && lineItemOrder.getDateTimeDetail().get(1).getDateTimeInformationList().size() > 0){
               if(lineItemOrder.getDateTimeDetail().get(1).getDateTimeInformationList().get(0).getPeriod().length() == 8) {
                   return lineItemOrder.getDateTimeDetail().get(1).getDateTimeInformationList().get(0).getPeriod();
               }else{
                   String subscriptionDate = lineItemOrder.getDateTimeDetail().get(1).getDateTimeInformationList().get(0).getPeriod()+"01";
                   int day = getLastDayOfMonth(subscriptionDate);
                   return lineItemOrder.getDateTimeDetail().get(1).getDateTimeInformationList().get(0).getPeriod()+day;
               }
            }
        }
        return null;
    }

    private String getSummaryCharge(INVOrder invOrder){
        if(invOrder.getSummary()!= null && invOrder.getSummary().getMonetarySummary() != null &&
                invOrder.getSummary().getMonetarySummary().size()> 0){
            if(invOrder.getSummary().getMonetarySummary().get(invOrder.getSummary().getMonetarySummary().size()-1).getMonetarySummaryInformation() != null && invOrder.getSummary().getMonetarySummary().get(invOrder.getSummary().getMonetarySummary().size()-1).getMonetarySummaryInformation().size() > 0){
                invOrder.getSummary().getMonetarySummary().get(invOrder.getSummary().getMonetarySummary().size()-1).getMonetarySummaryInformation().get(0).getAmount();
            }
        }
        return null;
    }

    private static boolean isNumeric(String str) {
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();
    }

}