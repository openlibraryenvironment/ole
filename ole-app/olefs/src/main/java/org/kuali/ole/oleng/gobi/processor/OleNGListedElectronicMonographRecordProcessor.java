package org.kuali.ole.oleng.gobi.processor;

import org.kuali.ole.gobi.GobiRequest;
import org.kuali.ole.gobi.datobjects.CollectionType;
import org.kuali.ole.gobi.datobjects.PurchaseOrder;
import org.kuali.ole.oleng.gobi.service.impl.OleNgGobiOrderImportServiceImpl;
import org.kuali.ole.oleng.gobi.service.impl.OleNgListedElectronicMonographGobiOrderRecordServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Created by SheikS on 8/3/2016.
 */
@Service("oleNGListedElectronicMonographRecordProcessor")
public class OleNGListedElectronicMonographRecordProcessor  extends OleNGGobiApiProcessor {

    @Override
    public boolean isInterested(GobiRequest gobiRequest) {
        return null != gobiRequest.getPurchaseOrder().getOrder().getListedElectronicMonograph();
    }

    @Override
    public String getMarcXMLContent(GobiRequest gobiRequest) {
        String collectionSerializedContent = null;
        PurchaseOrder.Order.ListedElectronicMonograph listedElectronicMonograph =
                gobiRequest.getPurchaseOrder().getOrder().getListedElectronicMonograph();
        CollectionType collection = listedElectronicMonograph.getCollection();
        if (null != collection) {
            collectionSerializedContent = collection.serialize(collection);
        }
        return collectionSerializedContent;
    }

    @Override
    protected OleNgGobiOrderImportServiceImpl getOleOrderRecordService() {
        return new OleNgListedElectronicMonographGobiOrderRecordServiceImpl();
    }

    @Override
    protected void linkToOrderOption() {
       /* List<OleOrderRecord> oleOrderRecordList = getOleOrderRecordList();
        for (Iterator<OleOrderRecord> iterator = oleOrderRecordList.iterator(); iterator.hasNext(); ) {
            OleOrderRecord oleOrderRecord = iterator.next();
            oleOrderRecord.setLinkToOrderOption(OLEConstants.ORDER_RECORD_IMPORT_MARC_ONLY_ELECTRONIC);
        }*/
    }
}