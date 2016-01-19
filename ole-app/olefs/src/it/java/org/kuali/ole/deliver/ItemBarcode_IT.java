package org.kuali.ole.deliver;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.kuali.ole.KFSTestCaseBase;
import org.kuali.ole.deliver.bo.*;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by hemalathas on 1/18/16.
 */
public class ItemBarcode_IT extends KFSTestCaseBase {

    @Test
    public void updateItemBarcode(){
        Map<String,String> itemBarcodeMap = new HashMap<String,String>();
        itemBarcodeMap.put("loanId","76");
        OleLoanDocument oleLoanDocument = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OleLoanDocument.class,itemBarcodeMap);
        if(oleLoanDocument != null){
            oleLoanDocument.setItemId("906");
        }
        OleLoanDocument loanDocument = KRADServiceLocator.getBusinessObjectService().save(oleLoanDocument);
        assertNotNull(loanDocument);
        assertEquals(loanDocument.getItemId() , "906");


        itemBarcodeMap.clear();
        itemBarcodeMap.put("oleCirculationHistoryId","8");
        OleCirculationHistory oleCirculationHistory = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OleCirculationHistory.class,itemBarcodeMap);
        if(oleCirculationHistory != null){
                oleCirculationHistory.setItemId("1000");
        }
        OleCirculationHistory circulationHistory =  KRADServiceLocator.getBusinessObjectService().save(oleCirculationHistory);
        assertNotNull(circulationHistory);
        assertEquals(circulationHistory.getItemId() , "1000");


        itemBarcodeMap.clear();
        itemBarcodeMap.put("temporaryCirculationHistoryId","31");
        OleTemporaryCirculationHistory oleTemporaryCirculationHistory = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OleTemporaryCirculationHistory.class,itemBarcodeMap);
        if(oleTemporaryCirculationHistory != null) {
               oleTemporaryCirculationHistory.setItemId("10001");
        }
        OleTemporaryCirculationHistory tempCirculationHistory =  KRADServiceLocator.getBusinessObjectService().save(oleTemporaryCirculationHistory);
        assertNotNull(tempCirculationHistory);
        assertEquals(tempCirculationHistory.getItemId(),"10001");

        itemBarcodeMap.clear();
        itemBarcodeMap.put("requestId","70");
        OleDeliverRequestBo oleDeliverRequestBo= KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OleDeliverRequestBo.class,itemBarcodeMap);
        if(oleDeliverRequestBo != null) {
              oleDeliverRequestBo.setItemId("10002");
         }
         OleDeliverRequestBo deliverRequest =  KRADServiceLocator.getBusinessObjectService().save(oleDeliverRequestBo);
        assertNotNull(deliverRequest);
        assertEquals(deliverRequest.getItemId(), "10002");


         itemBarcodeMap.clear();
         itemBarcodeMap.put("requestHistoryId","22");
         OleDeliverRequestHistoryRecord oleDeliverRequestHistoryRecord= KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OleDeliverRequestHistoryRecord.class,itemBarcodeMap);
         if(oleDeliverRequestHistoryRecord != null) {
                oleDeliverRequestHistoryRecord.setItemId("10003");
         }
         OleDeliverRequestHistoryRecord deliverRequestHistoryRecord = KRADServiceLocator.getBusinessObjectService().save(oleDeliverRequestHistoryRecord);
         assertNotNull(deliverRequestHistoryRecord);
         assertEquals(deliverRequestHistoryRecord.getItemId(), "10003");


         itemBarcodeMap.clear();
         itemBarcodeMap.put("id","23");
         OLEReturnHistoryRecord oleReturnHistory = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OLEReturnHistoryRecord.class,itemBarcodeMap);
         if(oleReturnHistory != null) {
                oleReturnHistory.setItemBarcode("10004");
         }
         OLEReturnHistoryRecord returnHistory =  KRADServiceLocator.getBusinessObjectService().save(oleReturnHistory);
          assertNotNull(returnHistory);
          assertEquals(returnHistory.getItemBarcode(), "10004");


          itemBarcodeMap.clear();
          itemBarcodeMap.put("oleRenewalHistoryId","1");
          OleRenewalHistory oleRenewalHistory = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OleRenewalHistory.class,itemBarcodeMap);
          if(oleRenewalHistory != null) {
                 oleRenewalHistory.setItemBarcode("10005");
          }
          OleRenewalHistory renewalHistory =  KRADServiceLocator.getBusinessObjectService().save(oleRenewalHistory);
           assertNotNull(renewalHistory);
           assertEquals(renewalHistory.getItemBarcode(), "10005");




























    }
}