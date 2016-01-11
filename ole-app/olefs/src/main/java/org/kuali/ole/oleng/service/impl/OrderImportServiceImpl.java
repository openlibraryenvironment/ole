package org.kuali.ole.oleng.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.batch.profile.model.BatchProfileDataMapping;
import org.kuali.ole.oleng.resolvers.*;
import org.kuali.ole.oleng.service.OrderImportService;
import org.kuali.ole.pojo.OleTxRecord;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 1/6/2016.
 */
public class OrderImportServiceImpl implements OrderImportService {

    private List<TxValueResolver> valueResolvers;

    @Override
    public OleTxRecord processDataMapping(OleTxRecord oleTxRecord, BatchProcessProfile batchProcessProfile) {
        List<BatchProfileDataMapping> batchProfileDataMappingList = batchProcessProfile.getBatchProfileDataMappingList();
        if(CollectionUtils.isNotEmpty(batchProfileDataMappingList)) {
            for (Iterator<BatchProfileDataMapping> iterator = batchProfileDataMappingList.iterator(); iterator.hasNext(); ) {
                BatchProfileDataMapping batchProfileDataMapping = iterator.next();

                String destinationField = batchProfileDataMapping.getField();
                String destinationValue = batchProfileDataMapping.getConstant();
                for (Iterator<TxValueResolver> valueResolverIterator = getValueResolvers().iterator(); valueResolverIterator.hasNext(); ) {
                    TxValueResolver txValueResolver = valueResolverIterator.next();
                    if (txValueResolver.isInterested(destinationField)) {
                        txValueResolver.setAttributeValue(oleTxRecord, destinationValue);
                    }
                }
            }
        }
        return oleTxRecord;
    }

    public List<TxValueResolver> getValueResolvers() {
        if (null == valueResolvers) {
            valueResolvers = new ArrayList<>();
            valueResolvers.add(new AccountNumberValueResolver());
            valueResolvers.add(new AssignToUserValueResolver());
            valueResolvers.add(new BuildingCodeValueResolver());
            valueResolvers.add(new CaptionValueResolver());
            valueResolvers.add(new ChartCodeValueResolver());
            valueResolvers.add(new ItemChartCodeValueResolver());
            valueResolvers.add(new QuantityValueResolver());
            valueResolvers.add(new ContractManagerValueResolver());
            valueResolvers.add(new CostSourceValueResolver());
            valueResolvers.add(new DefaultLocationValueResolver());
            valueResolvers.add(new DeliveryCampusCodeValueResolver());
            valueResolvers.add(new DiscountValueResolver());
            valueResolvers.add(new DonorCodeValueResolver());
            valueResolvers.add(new FundingSourceValueResolver());
            valueResolvers.add(new ItemNumPartsValueResolver());
            valueResolvers.add(new ItemPriceSourceValueResolver());
            valueResolvers.add(new ItemListPriceValueResolver());
            valueResolvers.add(new MethodOfPOTransmissionValueResolver());
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
            valueResolvers.add(new RequisitionSourceValueResolver());
            valueResolvers.add(new RequestorNoteValueResolver());
            valueResolvers.add(new RouteToRequestorValueResolver());
            valueResolvers.add(new SelectorNoteValueResolver());
            valueResolvers.add(new SingleCopyNumberValueResolver());
            valueResolvers.add(new TaxIndicatorValueResolver());
            valueResolvers.add(new VendorChoiceValueResolver());
            valueResolvers.add(new VendorNumberValueResolver());
            valueResolvers.add(new VendorInstructionsNoteValueResolver());
            valueResolvers.add(new DeliveryBuildingRoomNumberValueResolver());
        }
        return valueResolvers;
    }
}
