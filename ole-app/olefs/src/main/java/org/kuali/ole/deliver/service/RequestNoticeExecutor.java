package org.kuali.ole.deliver.service;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OleCirculationHistory;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by maheswarang on 4/13/16.
 */
public class RequestNoticeExecutor  {

  private List<OleDeliverRequestBo> oleDeliverRequestBoList;
  private BusinessObjectService businessObjectService;

    public RequestNoticeExecutor(List<OleDeliverRequestBo> oleDeliverRequestBoList) {
        this.oleDeliverRequestBoList = oleDeliverRequestBoList;
    }

    public BusinessObjectService getBusinessObjectService() {
       if(businessObjectService == null){
           businessObjectService = KRADServiceLocator.getBusinessObjectService();
       }
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }


    public void generateNotice() {
        List<OLEDeliverNotice> oleDeliverNotices = new ArrayList<OLEDeliverNotice>();
        if(oleDeliverRequestBoList.size()>0){
        for(OleDeliverRequestBo oleDeliverRequestBo : oleDeliverRequestBoList){
        oleDeliverNotices.add(generateRequestExpirationNotice(oleDeliverRequestBo));
        oleDeliverNotices.add(generateOnHoldNotice(oleDeliverRequestBo));
        oleDeliverNotices.add(generateOnHoldExpirationNotice(oleDeliverRequestBo));
        }
            getBusinessObjectService().save(oleDeliverNotices);
    }

  }

    public OLEDeliverNotice generateOnHoldNotice(OleDeliverRequestBo oleDeliverRequestBo){
      OLEDeliverNotice oleDeliverNotice = new OLEDeliverNotice();
        oleDeliverNotice.setRequestId(oleDeliverRequestBo.getRequestId());
        oleDeliverNotice.setPatronId(oleDeliverRequestBo.getBorrowerId());
        oleDeliverNotice.setNoticeSendType(OLEConstants.EMAIL);
        oleDeliverNotice.setNoticeType(OLEConstants.ONHOLD_NOTICE);
        oleDeliverNotice.setNoticeContentConfigName("On Hold Notice");
        oleDeliverNotice.setItemBarcode(oleDeliverRequestBo.getItemId());
        return oleDeliverNotice;
    }

    public OLEDeliverNotice generateOnHoldExpirationNotice(OleDeliverRequestBo oleDeliverRequestBo){
        OLEDeliverNotice oleDeliverNotice = new OLEDeliverNotice();
        oleDeliverNotice.setRequestId(oleDeliverRequestBo.getRequestId());
        oleDeliverNotice.setPatronId(oleDeliverRequestBo.getBorrowerId());
        oleDeliverNotice.setNoticeSendType(OLEConstants.EMAIL);
        oleDeliverNotice.setNoticeType(OLEConstants.ONHOLD_EXPIRATION_NOTICE);
        oleDeliverNotice.setNoticeContentConfigName("On Hold Expiration Notice");
        oleDeliverNotice.setItemBarcode(oleDeliverRequestBo.getItemId());
        return oleDeliverNotice;
    }

    public OLEDeliverNotice generateRequestExpirationNotice(OleDeliverRequestBo oleDeliverRequestBo){
        OLEDeliverNotice oleDeliverNotice = new OLEDeliverNotice();
        oleDeliverNotice.setRequestId(oleDeliverRequestBo.getRequestId());
        oleDeliverNotice.setPatronId(oleDeliverRequestBo.getBorrowerId());
        oleDeliverNotice.setNoticeSendType(OLEConstants.EMAIL);
        oleDeliverNotice.setNoticeType(OLEConstants.REQUEST_EXPIRATION_NOTICE);
        oleDeliverNotice.setNoticeContentConfigName("Request Expiration Notice");
        oleDeliverNotice.setItemBarcode(oleDeliverRequestBo.getItemId());
        if(oleDeliverRequestBo.getRequestExpiryDate()!=null){
        oleDeliverNotice.setNoticeToBeSendDate(new Timestamp(oleDeliverRequestBo.getRequestExpiryDate().getTime()));
        }
        return oleDeliverNotice;
    }


}
