package org.kuali.ole.gobi.resolvers;

import org.kuali.ole.gobi.datobjects.PurchaseOrder;
import org.kuali.ole.oleng.resolvers.orderimport.*;

import java.util.ArrayList;
import java.util.List;

public abstract class LocalDataMappingValueResolver extends TxValueResolver {
    List<TxValueResolver> valueResolvers;

    protected String sourceFieldName;
    protected String destFieldName;
    protected PurchaseOrder purchaseOrder;

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }


    public List<TxValueResolver> getTxValueResolverList() {
        if (null == valueResolvers) {
            valueResolvers = new ArrayList<>();
            valueResolvers.add(new AssignToUserValueResolver());
            valueResolvers.add(new CaptionValueResolver());
            valueResolvers.add(new ContractManagerValueResolver());
            valueResolvers.add(new CostSourceValueResolver());
            valueResolvers.add(new DefaultLocationValueResolver());
            valueResolvers.add(new DeliveryCampusCodeValueResolver());
            valueResolvers.add(new DiscountValueResolver());
            valueResolvers.add(new DonorCodeValueResolver());
            valueResolvers.add(new FundingSourceValueResolver());
            valueResolvers.add(new ItemNumPartsValueResolver());
            valueResolvers.add(new ItemListPriceValueResolver());
            valueResolvers.add(new MethodOfPOTransmissionValueResolver());
            valueResolvers.add(new MiscellaneousNoteValueResolver());
            valueResolvers.add(new ObjectCodeValueResolver());
            valueResolvers.add(new OrderTypeValueResolver());
            valueResolvers.add(new OrgCodeCodeValueResolver());
            valueResolvers.add(new PercentValueResolver());
            valueResolvers.add(new POConfirmationValueResolver());
            valueResolvers.add(new PreqPosstiveApprovalReqValueResolver());
            valueResolvers.add(new ReceivingRequiredValueResolver());
            valueResolvers.add(new ReceiptNoteValueResolver());
            valueResolvers.add(new RecurringPaymentTypeValueResolver());
            valueResolvers.add(new RecurringPaymentBeginDateValueResolver());
            valueResolvers.add(new RecurringPaymentEndDateTypeValueResolver());
            valueResolvers.add(new RequestSourceValueResolver());
            valueResolvers.add(new RequestorNoteValueResolver());
            valueResolvers.add(new RouteToRequestorValueResolver());
            valueResolvers.add(new SingleCopyNumberValueResolver());
            valueResolvers.add(new TaxIndicatorValueResolver());
            valueResolvers.add(new VendorChoiceValueResolver());
            valueResolvers.add(new VendorNumberValueResolver());
            valueResolvers.add(new VendorInstructionsNoteValueResolver());
            valueResolvers.add(new SpecialProcessingInstructionNoteValueResolver());
            valueResolvers.add(new SelectorNoteValueResolver());
        }
        return valueResolvers;
    }

    public void setTxValueResolverList(List<TxValueResolver> txValuResolverList) {
        this.valueResolvers = txValuResolverList;
    }

    public String getDestFieldName() {
        return destFieldName;
    }

    public void setDestFieldName(String destFieldName) {
        this.destFieldName = destFieldName;
    }

    public String getSourceFieldName() {
        return sourceFieldName;
    }

    public void setSourceFieldName(String sourceFieldName) {
        this.sourceFieldName = sourceFieldName;
    }
}
