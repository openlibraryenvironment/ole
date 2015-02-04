package org.kuali.ole.pojo.edi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/2/12
 * Time: 3:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class LineItemOrder {
    private List<LineItem> lineItem = new ArrayList<LineItem>();
    private List<ProductFunction> productFunction = new ArrayList<ProductFunction>();
    private List<ItemDescription> itemDescriptionList = new ArrayList<ItemDescription>();
    private List<QuantityInformation> quantityInformation = new ArrayList<QuantityInformation>();
    private List<PriceInformation> priceInformation = new ArrayList<PriceInformation>();
    private List<BuyerReferenceInformation> buyerReferenceInformation = new ArrayList<BuyerReferenceInformation>();
    private List<SupplierReferenceInformation> supplierReferenceInformation = new ArrayList<SupplierReferenceInformation>();
    private List<FundNumberReference> fundNumberReference = new ArrayList<FundNumberReference>();
    private List<FreeTextNotes> note = new ArrayList<FreeTextNotes>();
    private List<TransportStageQualifier> transportStageQualifier = new ArrayList<TransportStageQualifier>();
    private List<DateTimeDetail> dateTimeDetail = new ArrayList<>();
    private List<MonetaryDetail> monetaryDetail = new ArrayList<>();
    private List<LineItemAllowanceOrCharge> lineItemAllowanceOrCharge = new ArrayList<>();
    private List<AllowanceMonetaryDetail> allowanceMonetaryDetail = new ArrayList<>();


    public List<LineItemAllowanceOrCharge> getLineItemAllowanceOrCharge() {
        return lineItemAllowanceOrCharge;
    }

    public void setLineItemAllowanceOrCharge(List<LineItemAllowanceOrCharge> lineItemAllowanceOrCharge) {
        lineItemAllowanceOrCharge = lineItemAllowanceOrCharge;
    }

    public List<AllowanceMonetaryDetail> getAllowanceMonetaryDetail() {
        return allowanceMonetaryDetail;
    }

    public void setAllowanceMonetaryDetail(List<MonetaryDetail> allowanceMonetaryDetail) {
        allowanceMonetaryDetail = allowanceMonetaryDetail;
    }

    public List<FreeTextNotes> getNote() {
        return note;
    }

    public void setNote(List<FreeTextNotes> note) {
        this.note = note;
    }

    public List<ProductFunction> getProductFunction() {
        return productFunction;
    }

    public void setProductFunction(List<ProductFunction> productFunction) {
        this.productFunction = productFunction;
    }

    public List<QuantityInformation> getQuantityInformation() {
        return quantityInformation;
    }

    public void setQuantityInformation(List<QuantityInformation> quantityInformation) {
        this.quantityInformation = quantityInformation;
    }

    public List<PriceInformation> getPriceInformation() {
        return priceInformation;
    }

    public void setPriceInformation(List<PriceInformation> priceInformation) {
        this.priceInformation = priceInformation;
    }

    public List<BuyerReferenceInformation> getBuyerReferenceInformation() {
        return buyerReferenceInformation;
    }

    public void setBuyerReferenceInformation(List<BuyerReferenceInformation> buyerReferenceInformation) {
        this.buyerReferenceInformation = buyerReferenceInformation;
    }

    public List<SupplierReferenceInformation> getSupplierReferenceInformation() {
        return supplierReferenceInformation;
    }

    public void setSupplierReferenceInformation(List<SupplierReferenceInformation> supplierReferenceInformation) {
        this.supplierReferenceInformation = supplierReferenceInformation;
    }

    public List<FundNumberReference> getFundNumberReference() {
        return fundNumberReference;
    }

    public void setFundNumberReference(List<FundNumberReference> fundNumberReference) {
        this.fundNumberReference = fundNumberReference;
    }

    public List<TransportStageQualifier> getTransportStageQualifier() {
        return transportStageQualifier;
    }

    public void setTransportStageQualifier(List<TransportStageQualifier> transportStageQualifier) {
        this.transportStageQualifier = transportStageQualifier;
    }

    public List<DateTimeDetail> getDateTimeDetail() {
        return dateTimeDetail;
    }

    public void setDateTimeDetail(List<DateTimeDetail> dateTimeDetail) {
        this.dateTimeDetail = dateTimeDetail;
    }

    public List<MonetaryDetail> getMonetaryDetail() {
        return monetaryDetail;
    }

    public void setMonetaryDetail(List<MonetaryDetail> monetaryDetail) {
        this.monetaryDetail = monetaryDetail;
    }

    public void addLineItemAllowanceOrCharge(LineItemAllowanceOrCharge lineItemAllowanceOrCharge){
        if (!this.lineItemAllowanceOrCharge.contains(lineItemAllowanceOrCharge)) {
             this.lineItemAllowanceOrCharge.add(lineItemAllowanceOrCharge);
        }
    }

    public void addLineItem(AllowanceMonetaryDetail allowanceMonetaryDetail) {
        if (!this.allowanceMonetaryDetail.contains(allowanceMonetaryDetail)) {
            this.allowanceMonetaryDetail.add(allowanceMonetaryDetail);
        }
    }

    public void addLineItem(LineItem lineItem) {
        if (!this.lineItem.contains(lineItem)) {
            this.lineItem.add(lineItem);
        }
    }

    public void addNote(FreeTextNotes note) {
        if (!this.note.contains(note)) {
            this.note.add(note);
        }
    }

    public void addProductFunction(ProductFunction productFunction) {
        if (!this.productFunction.contains(productFunction)) {
            this.productFunction.add(productFunction);
        }
    }

    public void addItemDescription(ItemDescription itemDescription) {
        if (!this.itemDescriptionList.contains(itemDescription)) {
            this.itemDescriptionList.add(itemDescription);
        }
    }

    public void addQuantityInformation(QuantityInformation quantityInformation) {
        if (!this.quantityInformation.contains(quantityInformation)) {
            this.quantityInformation.add(quantityInformation);
        }
    }

    public void addPriceInformation(PriceInformation priceInformation) {
        if (!this.priceInformation.contains(priceInformation)) {
            this.priceInformation.add(priceInformation);
        }
    }

    public void addBuyerReferenceInformation(BuyerReferenceInformation buyerReferenceInformation) {
        if (!this.buyerReferenceInformation.contains(buyerReferenceInformation)) {
            this.buyerReferenceInformation.add(buyerReferenceInformation);
        }
    }

    public void addSupplierReferenceInformation(SupplierReferenceInformation supplierReferenceInformation) {
        if (!this.supplierReferenceInformation.contains(supplierReferenceInformation)) {
            this.supplierReferenceInformation.add(supplierReferenceInformation);
        }
    }

    public void addFundNumberReference(FundNumberReference fundNumberReference) {
        if (!this.fundNumberReference.contains(fundNumberReference)) {
            this.fundNumberReference.add(fundNumberReference);
        }
    }

    public void addTransportStageQualifier(TransportStageQualifier transportStageQualifier) {
        if (!this.transportStageQualifier.contains(transportStageQualifier)) {
            this.transportStageQualifier.add(transportStageQualifier);
        }
    }

    public void addDateTimeDetail(DateTimeDetail dateTimeDetail) {
        if (!this.dateTimeDetail.contains(dateTimeDetail)) {
            this.dateTimeDetail.add(dateTimeDetail);
        }
    }


    public void addMonetaryDetail(MonetaryDetail monetaryDetail) {
        if (!this.monetaryDetail.contains(monetaryDetail)) {
            this.monetaryDetail.add(monetaryDetail);
        }
    }

    public List<LineItem> getLineItem() {
        return lineItem;
    }

    public void setLineItem(List<LineItem> lineItem) {
        this.lineItem = lineItem;
    }

    public List<ItemDescription> getItemDescriptionList() {
        return itemDescriptionList;
    }

    public void setItemDescriptionList(List<ItemDescription> itemDescriptionList) {
        this.itemDescriptionList = itemDescriptionList;
    }


}