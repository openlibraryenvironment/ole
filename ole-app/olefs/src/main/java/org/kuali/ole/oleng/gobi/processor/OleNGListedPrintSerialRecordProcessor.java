package org.kuali.ole.oleng.gobi.processor;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.gobi.GobiRequest;
import org.kuali.ole.gobi.datobjects.CollectionType;
import org.kuali.ole.gobi.datobjects.PurchaseOrder;
import org.kuali.ole.gobi.processor.GobiAPIProcessor;
import org.kuali.ole.gobi.service.impl.ListedPrintSerialGobiOrderRecordServiceImpl;
import org.kuali.ole.gobi.service.impl.OleGobiOrderRecordServiceImpl;
import org.kuali.ole.pojo.OleOrderRecord;

import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 8/3/2016.
 */
public class OleNGListedPrintSerialRecordProcessor extends OleNGGobiApiProcessor {

    @Override
    public boolean isInterested(GobiRequest gobiRequest) {
        return null != gobiRequest.getPurchaseOrder().getOrder().getListedPrintSerial();
    }


    @Override
    public String getMarcXMLContent(GobiRequest gobiRequest) {
        String collectionSerializedContent = null;
        PurchaseOrder.Order.ListedPrintSerial listedPrintSerial = gobiRequest.getPurchaseOrder().getOrder().getListedPrintSerial();
        CollectionType collection = listedPrintSerial.getCollection();
        if (null != collection) {
            collectionSerializedContent = collection.serialize(collection);
        }
        return collectionSerializedContent;
    }

    @Override
    protected OleGobiOrderRecordServiceImpl getOleOrderRecordService() {
        return new ListedPrintSerialGobiOrderRecordServiceImpl();
    }

    @Override
    protected void linkToOrderOption() {
        List<OleOrderRecord> oleOrderRecordList = getOleOrderRecordList();
        for (Iterator<OleOrderRecord> iterator = oleOrderRecordList.iterator(); iterator.hasNext(); ) {
            OleOrderRecord oleOrderRecord = iterator.next();
            oleOrderRecord.setLinkToOrderOption(OLEConstants.ORDER_RECORD_IMPORT_MARC_ONLY_PRINT);
        }
    }
}
