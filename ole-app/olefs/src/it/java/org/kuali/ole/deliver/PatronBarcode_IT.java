package org.kuali.ole.deliver;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.kuali.ole.KFSTestCaseBase;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.controller.PatronBarcodeUpdateHandler;
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
public class PatronBarcode_IT extends KFSTestCaseBase {

    @Test
    public void updatePatronBarcode() {
        Map<String, String> patronBarcodeMap = new HashMap<String, String>();
        String barcode = "";
        patronBarcodeMap.put("olePatronId", "10000");
        OlePatronDocument olePatronDocument = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OlePatronDocument.class, patronBarcodeMap);
        assertNotNull(olePatronDocument);

        barcode = olePatronDocument.getBarcode();

        OleDeliverRequestBo oleDeliverRequestBos = new OleDeliverRequestBo();
        oleDeliverRequestBos.setItemId("10");
        oleDeliverRequestBos.setBorrowerBarcode(olePatronDocument.getBarcode());
        oleDeliverRequestBos.setBorrowerId(olePatronDocument.getOlePatronId());
        oleDeliverRequestBos.setRequestId("ole");
        oleDeliverRequestBos.setItemUuid("wio-10");
        OleDeliverRequestBo deliverRequestBo = KRADServiceLocator.getBusinessObjectService().save(oleDeliverRequestBos);

        olePatronDocument.setBarcode("102");
        OlePatronDocument olePatronDocuments = KRADServiceLocator.getBusinessObjectService().save(olePatronDocument);

        PatronBarcodeUpdateHandler patronBarcodeUpdateHandler = new PatronBarcodeUpdateHandler();
        patronBarcodeUpdateHandler.updatePatronBarcode(olePatronDocument, barcode);

        patronBarcodeMap.clear();
        patronBarcodeMap.put("borrowerId", olePatronDocument.getOlePatronId());
        List<OleDeliverRequestBo> deliverRequestBoList = (List<OleDeliverRequestBo>) KRADServiceLocator.getBusinessObjectService().findMatching(OleDeliverRequestBo.class, patronBarcodeMap);
        assertTrue(CollectionUtils.isNotEmpty(deliverRequestBoList));
        for (Iterator<OleDeliverRequestBo> iterator = deliverRequestBoList.iterator(); iterator.hasNext(); ) {
            OleDeliverRequestBo oleDeliverRequestBo = iterator.next();
            assertEquals(olePatronDocument.getBarcode(), oleDeliverRequestBo.getBorrowerBarcode());
            assertNotEquals(barcode, oleDeliverRequestBo.getBorrowerBarcode());
        }


    }
}
