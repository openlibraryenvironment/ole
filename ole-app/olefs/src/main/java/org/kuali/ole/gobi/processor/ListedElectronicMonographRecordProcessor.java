package org.kuali.ole.gobi.processor;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.gobi.GobiRequest;
import org.kuali.ole.gobi.datobjects.CollectionType;
import org.kuali.ole.gobi.datobjects.PurchaseOrder;
import org.kuali.ole.gobi.service.impl.ListedElectronicMonographGobiOrderRecordServiceImpl;
import org.kuali.ole.gobi.service.impl.OleGobiOrderRecordServiceImpl;
import org.kuali.ole.pojo.OleOrderRecord;

import java.util.Iterator;
import java.util.List;

/**
 * Created by pvsubrah on 9/22/15.
 */
public class ListedElectronicMonographRecordProcessor extends GobiAPIProcessor {

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
    protected OleGobiOrderRecordServiceImpl getOleOrderRecordService() {
        return new ListedElectronicMonographGobiOrderRecordServiceImpl();
    }

    @Override
    protected void linkToOrderOption() {
        List<OleOrderRecord> oleOrderRecordList = getOleOrderRecordList();
        for (Iterator<OleOrderRecord> iterator = oleOrderRecordList.iterator(); iterator.hasNext(); ) {
            OleOrderRecord oleOrderRecord = iterator.next();
            oleOrderRecord.setLinkToOrderOption(OLEConstants.ORDER_RECORD_IMPORT_MARC_ONLY_ELECTRONIC);
        }
    }
}
