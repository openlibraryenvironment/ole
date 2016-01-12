package org.kuali.ole.oleng.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.batch.profile.model.BatchProfileDataMapping;
import org.kuali.ole.oleng.resolvers.*;
import org.kuali.ole.oleng.service.OrderImportService;
import org.kuali.ole.pojo.OleBibRecord;
import org.kuali.ole.pojo.OleTxRecord;
import org.kuali.ole.utility.MarcRecordUtil;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.marc4j.marc.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 1/6/2016.
 */
public class OrderImportServiceImpl implements OrderImportService {

    private List<TxValueResolver> valueResolvers;
    private MarcRecordUtil marcRecordUtil;
    private BusinessObjectService businessObjectService;

    @Override
    public OleTxRecord processDataMapping(String bibId, OleTxRecord oleTxRecord, BatchProcessProfile batchProcessProfile) {
        List<BatchProfileDataMapping> batchProfileDataMappingList = batchProcessProfile.getBatchProfileDataMappingList();

        if (CollectionUtils.isNotEmpty(batchProfileDataMappingList)) {

            BibRecord bibRecord = getBusinessObjectService().findBySinglePrimaryKey(BibRecord.class, DocumentUniqueIDPrefix.getDocumentId(bibId));
            List<Record> records = getMarcRecordUtil().convertMarcXmlContentToMarcRecord(bibRecord.getContent());
            Record marcRecord = records.get(0);

            for (Iterator<BatchProfileDataMapping> iterator = batchProfileDataMappingList.iterator(); iterator.hasNext(); ) {
                BatchProfileDataMapping batchProfileDataMapping = iterator.next();

                String destinationField = batchProfileDataMapping.getField();
                for (Iterator<TxValueResolver> valueResolverIterator = getValueResolvers().iterator(); valueResolverIterator.hasNext(); ) {
                    TxValueResolver txValueResolver = valueResolverIterator.next();
                    if (txValueResolver.isInterested(destinationField)) {
                        String destinationValue = getDestinationValue(marcRecord, batchProfileDataMapping);
                        txValueResolver.setAttributeValue(oleTxRecord, destinationValue);
                    }
                }
            }
        }
        return oleTxRecord;
    }

    private String getDestinationValue(Record marcRecord, BatchProfileDataMapping batchProfileDataMapping) {
        String destValue = null;
        if (batchProfileDataMapping.getDataType().equalsIgnoreCase("bib marc")) {
            String dataField = batchProfileDataMapping.getDataField();
            String subField = batchProfileDataMapping.getSubField();

            if (getMarcRecordUtil().isControlField(dataField)) {
                destValue = getMarcRecordUtil().getControlFieldValue(marcRecord, dataField);
            } else {
                destValue = getMarcRecordUtil().getDataFieldValue(marcRecord, dataField, subField);
            }

        } else if (batchProfileDataMapping.getDataType().equalsIgnoreCase("constant")) {
            destValue = batchProfileDataMapping.getConstant();
        }

        return destValue;
    }

    public MarcRecordUtil getMarcRecordUtil() {
        if (null == marcRecordUtil) {
            marcRecordUtil = new MarcRecordUtil();
        }
        return marcRecordUtil;
    }

    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
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
            valueResolvers.add(new VendorItemIdentifierValueResolver());
            valueResolvers.add(new VendorCustomerNumberValueResolver());
            valueResolvers.add(new VendorInstructionsNoteValueResolver());
            valueResolvers.add(new DeliveryBuildingRoomNumberValueResolver());
        }
        return valueResolvers;
    }
}
