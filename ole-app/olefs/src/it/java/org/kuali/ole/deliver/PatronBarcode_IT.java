package org.kuali.ole.deliver;

import org.junit.Test;
import org.kuali.ole.KFSTestCaseBase;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OleRenewalHistory;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemClaimsReturnedRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemDamagedRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.MissingPieceItemRecord;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by hemalathas on 1/18/16.
 */
public class PatronBarcode_IT extends KFSTestCaseBase{

    @Test
    public void updatePatronBarcode() {
        Map<String,String> patronBarcodeMap = new HashMap<String,String>();

        patronBarcodeMap.put("requestId","70");
        OleDeliverRequestBo oleDeliverRequestBo= KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OleDeliverRequestBo.class,patronBarcodeMap);
        if(oleDeliverRequestBo != null) {
            oleDeliverRequestBo.setBorrowerBarcode("0007");
        }
        OleDeliverRequestBo deliverRequest =  KRADServiceLocator.getBusinessObjectService().save(oleDeliverRequestBo);
        assertNotNull(deliverRequest);
        assertEquals(deliverRequest.getBorrowerBarcode(),"0007");


        patronBarcodeMap.clear();
        patronBarcodeMap.put("oleRenewalHistoryId","1");
        OleRenewalHistory oleRenewalHistory = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OleRenewalHistory.class,patronBarcodeMap);
        if(oleRenewalHistory != null) {
            oleRenewalHistory.setPatronBarcode("0008");
        }
        OleRenewalHistory renewalHistory =  KRADServiceLocator.getBusinessObjectService().save(oleRenewalHistory);
        assertNotNull(renewalHistory);
        assertEquals(renewalHistory.getPatronBarcode(),"0008");


        patronBarcodeMap.clear();
        patronBarcodeMap.put("missingPieceItemId","2");
        MissingPieceItemRecord missingPieceItemRecord = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(MissingPieceItemRecord.class,patronBarcodeMap);
        if(missingPieceItemRecord != null) {
            missingPieceItemRecord.setPatronBarcode("0009");
        }
        MissingPieceItemRecord missingPieceItemRecord1 =  KRADServiceLocator.getBusinessObjectService().save(missingPieceItemRecord);
        assertNotNull(missingPieceItemRecord1);
        assertEquals(missingPieceItemRecord1.getPatronBarcode(),"0009");


        patronBarcodeMap.clear();
        patronBarcodeMap.put("claimsReturnedId","1");
        ItemClaimsReturnedRecord itemClaimsReturnedRecord = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(ItemClaimsReturnedRecord.class,patronBarcodeMap);
        if(itemClaimsReturnedRecord != null) {
            itemClaimsReturnedRecord.setClaimsReturnedPatronBarcode("00010");
        }
        ItemClaimsReturnedRecord claimsReturnedRecord =  KRADServiceLocator.getBusinessObjectService().save(itemClaimsReturnedRecord);
        assertNotNull(claimsReturnedRecord);
        assertEquals(claimsReturnedRecord.getClaimsReturnedPatronBarcode(),"00010");

        patronBarcodeMap.clear();
        patronBarcodeMap.put("itemDamagedId","3");
        ItemDamagedRecord itemDamagedRecord = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(ItemDamagedRecord.class,patronBarcodeMap);
        if(itemDamagedRecord != null) {
            itemDamagedRecord.setPatronBarcode("00011");
        }
        ItemDamagedRecord damagedRecord =  KRADServiceLocator.getBusinessObjectService().save(itemDamagedRecord);
        assertNotNull(damagedRecord);
        assertEquals(damagedRecord.getPatronBarcode(),"00011");
    }
}
