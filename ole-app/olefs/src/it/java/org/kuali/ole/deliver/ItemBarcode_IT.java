package org.kuali.ole.deliver;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.kuali.ole.KFSTestCaseBase;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.controller.ItemBarcodeUpdateHandler;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by hemalathas on 1/19/16.
 */
public class ItemBarcode_IT extends KFSTestCaseBase {

    @Test
    public void updateItemBarcode() {
        Map<String, String> itemBarcodeMap = new HashMap<String, String>();
        String barcode = "";
        itemBarcodeMap.put("itemId", "2");
        ItemRecord itemRecord = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(ItemRecord.class, itemBarcodeMap);
        assertNotNull(itemRecord);
        barcode = itemRecord.getBarCode();

        OleDeliverRequestBo oleDeliverRequestBos = new OleDeliverRequestBo();
        oleDeliverRequestBos.setItemId("10");
        oleDeliverRequestBos.setRequestId("ole");
        oleDeliverRequestBos.setItemUuid("wio-2");
        OleDeliverRequestBo deliverRequestBo = KRADServiceLocator.getBusinessObjectService().save(oleDeliverRequestBos);

        itemRecord.setBarCode("2000");
        ItemBarcodeUpdateHandler itemBarcodeUpdateHandler = new ItemBarcodeUpdateHandler();
        itemBarcodeUpdateHandler.updateItemBarcode(itemRecord, barcode);


        itemBarcodeMap.clear();
        itemBarcodeMap.put("itemUuid", "wio-2");
        List<OleDeliverRequestBo> deliverRequestBoList = (List<OleDeliverRequestBo>) KRADServiceLocator.getBusinessObjectService().findMatching(OleDeliverRequestBo.class, itemBarcodeMap);
        assertTrue(CollectionUtils.isNotEmpty(deliverRequestBoList));
        for (Iterator<OleDeliverRequestBo> iterator = deliverRequestBoList.iterator(); iterator.hasNext(); ) {
            OleDeliverRequestBo oleDeliverRequestBo = iterator.next();
            assertEquals(oleDeliverRequestBo.getItemId(), itemRecord.getBarCode());
            assertNotEquals(oleDeliverRequestBo.getItemId(), barcode);
        }
    }
}
