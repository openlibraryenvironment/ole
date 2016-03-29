package org.kuali.ole.gobi.processor;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.gobi.GobiRequest;
import org.kuali.ole.gobi.datobjects.CollectionType;
import org.kuali.ole.gobi.datobjects.PurchaseOrder;
import org.kuali.ole.gobi.service.impl.ListedElectronicSerialGobiOrderRecordServiceImpl;
import org.kuali.ole.gobi.service.impl.OleGobiOrderRecordServiceImpl;
import org.kuali.ole.pojo.OleOrderRecord;

import java.util.Iterator;
import java.util.List;

public class ListedElectronicSerialRecordProcessor extends GobiAPIProcessor {

    @Override
    public boolean isInterested(GobiRequest gobiRequest) {
        return null != gobiRequest.getPurchaseOrder().getOrder().getListedElectronicSerial();
    }


    @Override
    public String getMarcXMLContent(GobiRequest gobiRequest) {
        String collectionSerializedContent = null;
        PurchaseOrder.Order.ListedElectronicSerial listedElectronicSerial =
                gobiRequest.getPurchaseOrder().getOrder().getListedElectronicSerial();
        CollectionType collection = listedElectronicSerial.getCollection();
        if (null != collection) {
            collectionSerializedContent = collection.serialize(collection);
        }
        return collectionSerializedContent;
    }

    @Override
    protected OleGobiOrderRecordServiceImpl getOleOrderRecordService() {
        return new ListedElectronicSerialGobiOrderRecordServiceImpl();
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
