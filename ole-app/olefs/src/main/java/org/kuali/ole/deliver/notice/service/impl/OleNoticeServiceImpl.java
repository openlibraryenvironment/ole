package org.kuali.ole.deliver.notice.service.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.calendar.service.DateUtil;
import org.kuali.ole.deliver.notice.bo.OleNoticeContentConfigurationBo;
import org.kuali.ole.deliver.notice.service.OleNoticeService;
import org.kuali.ole.deliver.notice.util.NoticeUtil;
import org.kuali.ole.deliver.service.OleLoanDocumentDaoOjb;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maheswarang on 7/1/15.
 */
public class OleNoticeServiceImpl implements OleNoticeService {

    private BusinessObjectService businessObjectService;
    private NoticeUtil noticeUtil;


    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public NoticeUtil getNoticeUtil() {
    if(noticeUtil == null){
        noticeUtil = new NoticeUtil();
    }
        return noticeUtil;
    }


    public void setNoticeUtil(NoticeUtil noticeUtil) {
        this.noticeUtil = noticeUtil;
    }

    @Override
    public OLEDeliverNotice createNotice(OleDeliverRequestBo oleDeliverRequestBo,String noticeType,Timestamp noticeToBeSendDate) {
        OLEDeliverNotice oleDeliverNotice = new OLEDeliverNotice();
        oleDeliverNotice.setNoticeSendType(OLEConstants.EMAIL);
        oleDeliverNotice.setPatronId(oleDeliverRequestBo.getBorrowerId());
        if(noticeToBeSendDate!=null){
        oleDeliverNotice.setNoticeToBeSendDate(noticeToBeSendDate);
        }
        if (noticeType.equalsIgnoreCase(OLEConstants.RECALL_NOTICE)){
            oleDeliverNotice.setNoticeContentConfigName(oleDeliverRequestBo.getRecallNoticeContentConfigName());
            oleDeliverNotice.setLoanId(oleDeliverRequestBo.getLoanTransactionRecordNumber());
        }
        else if (noticeType.equalsIgnoreCase(OLEConstants.REQUEST_EXPIRATION_NOTICE)){
            oleDeliverNotice.setNoticeContentConfigName(oleDeliverRequestBo.getRequestExpirationNoticeContentConfigName());
        }
        else if (noticeType.equalsIgnoreCase(OLEConstants.ONHOLD_EXPIRATION_NOTICE)){
            oleDeliverNotice.setNoticeSendType(getNoticeUtil().getParameter(OLEConstants.HOLD_COUR_NOT_TYP));
            oleDeliverNotice.setNoticeContentConfigName(oleDeliverRequestBo.getOnHoldExpirationNoticeContentConfigName());
        }
        else if (noticeType.equalsIgnoreCase(OLEConstants.ONHOLD_NOTICE)){
            oleDeliverNotice.setNoticeContentConfigName(oleDeliverRequestBo.getOnHoldNoticeContentConfigName());
        }
        else if (noticeType.equalsIgnoreCase(OLEConstants.ONHOLD_COURTESY_NOTICE)){
            oleDeliverNotice.setNoticeContentConfigName(oleDeliverRequestBo.getOnHoldCourtesyNoticeContentConfigName());
        }
        oleDeliverNotice.setItemBarcode(oleDeliverRequestBo.getItemId());
        oleDeliverNotice.setNoticeType(noticeType);
        return oleDeliverNotice;
    }

    @Override
    public OleDeliverRequestBo processNoticeForRequest(OleDeliverRequestBo oleDeliverRequestBo) {
        List<OLEDeliverNotice> oleDeliverNotices = new ArrayList<OLEDeliverNotice>();
        if(oleDeliverRequestBo.getDeliverNotices() ==null || (oleDeliverRequestBo.getDeliverNotices()!=null && oleDeliverRequestBo.getDeliverNotices().size()==0)){
        OLEDeliverNotice requestExpirationDeliverNotice = createNotice(oleDeliverRequestBo,OLEConstants.REQUEST_EXPIRATION_NOTICE,new java.sql.Timestamp(oleDeliverRequestBo.getRequestExpiryDate().getTime()));
        oleDeliverNotices.add(requestExpirationDeliverNotice);
        OleLoanDocumentDaoOjb oleLoanDocumentDaoOjb = (OleLoanDocumentDaoOjb) SpringContext.getService("oleLoanDao");
        String requestTypeParameter = getNoticeUtil().getParameter(OLEConstants.ON_HOLD_NOTICE_REQUEST_TYPE);
        List<String> requestTypeIds = new ArrayList<String>();
        List<String> requestTypeCodes = new ArrayList<String>();
        if(requestTypeParameter!=null && !requestTypeParameter.trim().isEmpty()){
            String[] requestType = requestTypeParameter.split(";");
            requestTypeCodes = getNoticeUtil().getList(requestType);
            requestTypeIds = oleLoanDocumentDaoOjb.getRequestTypeIdsForHoldNotice(requestTypeCodes);
        }
        if(oleDeliverRequestBo.getRequestTypeId().equals("1") || oleDeliverRequestBo.getRequestTypeId().equals("2")){
        OLEDeliverNotice recallDeliverNotice = createNotice(oleDeliverRequestBo,OLEConstants.RECALL_NOTICE,new java.sql.Timestamp(System.currentTimeMillis()));
        oleDeliverNotices.add(recallDeliverNotice);
        }
        if(requestTypeIds.contains(oleDeliverRequestBo.getRequestTypeId())){
        OLEDeliverNotice onHoldDeliverNotice = createNotice(oleDeliverRequestBo,OLEConstants.ONHOLD_NOTICE,null);
        OLEDeliverNotice onHoldExpiryDeliverNotice = createNotice(oleDeliverRequestBo,OLEConstants.ONHOLD_EXPIRATION_NOTICE,null);
        OLEDeliverNotice onHoldCourtestDeliverNotice = createNotice(oleDeliverRequestBo,OLEConstants.ONHOLD_COURTESY_NOTICE,null);
        oleDeliverNotices.add(onHoldDeliverNotice);
        oleDeliverNotices.add(onHoldExpiryDeliverNotice);
        oleDeliverNotices.add(onHoldCourtestDeliverNotice);
        }
        oleDeliverRequestBo.setDeliverNotices(oleDeliverNotices);
        }
        return oleDeliverRequestBo;
    }

    @Override
    public void updateHoldNoticesDate(String requestId){
        List<OLEDeliverNotice> oleDeliverNoticeList = new ArrayList<OLEDeliverNotice>();
        Map<String,String> requestMap = new HashMap<String,String>();
        requestMap.put("requestId",requestId);
        requestMap.put("noticeType", OLEConstants.ONHOLD_NOTICE);
        List<OLEDeliverNotice> oleDeliverNotices = (List<OLEDeliverNotice>)getBusinessObjectService().findMatching(OLEDeliverNotice.class,requestMap);
        if(oleDeliverNotices!=null && oleDeliverNotices.size()>0){
            for(OLEDeliverNotice oleDeliverNotice : oleDeliverNotices){
                oleDeliverNotice.setNoticeToBeSendDate(new Timestamp(System.currentTimeMillis()));
                oleDeliverNoticeList.add(oleDeliverNotice);
                if(oleDeliverNotice.getOleDeliverRequestBo()!=null && oleDeliverNotice.getOleDeliverRequestBo().getDeliverNotices()!=null){
                    for(OLEDeliverNotice oleDeliverNotice1 : oleDeliverNotice.getOleDeliverRequestBo().getDeliverNotices()){
                        if(oleDeliverNotice1.getNoticeType().equals(OLEConstants.ONHOLD_EXPIRATION_NOTICE)){
                            if(oleDeliverNotice.getOleDeliverRequestBo().getOlePickUpLocation() !=null ){
                                if(StringUtils.isEmpty(oleDeliverNotice.getOleDeliverRequestBo().getOlePickUpLocation().getOnHoldDays())){
                                    oleDeliverNotice1.setNoticeToBeSendDate(new Timestamp(System.currentTimeMillis()));
                                }else{
                                    oleDeliverNotice1.setNoticeToBeSendDate(DateUtil.addDays(new Timestamp(System.currentTimeMillis()), Integer.parseInt(oleDeliverNotice.getOleDeliverRequestBo().getOlePickUpLocation().getOnHoldDays())));
                                }
                                oleDeliverNoticeList.add(oleDeliverNotice1);
                            }else if(oleDeliverNotice1.getNoticeType().equals(OLEConstants.ONHOLD_COURTESY_NOTICE)){
                                oleDeliverNotice1.setNoticeToBeSendDate(new Timestamp(System.currentTimeMillis()));
                                oleDeliverNoticeList.add(oleDeliverNotice1);
                            }
                        }
                    }
                }
            }
        }
        getBusinessObjectService().save(oleDeliverNoticeList);
    }

    @Override
    public String getNoticeSubjectForNoticeType(String noticeName){
        String noticeSubject=null;
        Map<String,String> noticeMap=new HashMap<>();
        noticeMap.put("noticeType",noticeName);
        List<OleNoticeContentConfigurationBo> noticeContentConfigurationBoList=(List<OleNoticeContentConfigurationBo>)getBusinessObjectService().findMatching(OleNoticeContentConfigurationBo.class,noticeMap);
        if(noticeContentConfigurationBoList.size()>0) {
            OleNoticeContentConfigurationBo noticeContentConfigurationBo = noticeContentConfigurationBoList.get(0);
            noticeSubject=(noticeContentConfigurationBo.getNoticeSubjectLine()!=null?noticeContentConfigurationBo.getNoticeSubjectLine():null);
        }
        return noticeSubject;
    }

}



