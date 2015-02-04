package org.kuali.ole.batch.service;

import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.batch.bo.*;
import org.kuali.ole.batch.form.OLEDeliverNoticeForm;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.rice.core.api.config.property.ConfigContext;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 8/7/13
 * Time: 8:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEDeliverNoticeService {
    private static final Logger LOG = Logger.getLogger(OLEDeliverNoticeService.class);
    private LoanProcessor loanProcessor = new LoanProcessor();

    public OLEDeliverNoticeForm populateOLEDeliverNoticeForm(OLEDeliverNoticeForm oleDeliverNoticeForm){

        List<File> expiredRequestList= new ArrayList<File>();
        List<File> courtesyNoticeList = new ArrayList<File>();
        List<File> overDueNoticeList = new ArrayList<File>();
        List<File> onHoldNoticeList = new ArrayList<File>();
        List<File> recallNoticeList=new ArrayList<File>();
        List<File> pickupNoticeList=new ArrayList<File>();
        List<File> onHoldCourtesyNoticeList = new ArrayList<File>();
        String pdfLocationSystemParam = loanProcessor.getParameter(OLEParameterConstants.PDF_LOCATION);
        if (pdfLocationSystemParam == null || pdfLocationSystemParam.trim().isEmpty()) {
            pdfLocationSystemParam = ConfigContext.getCurrentContextConfig().getProperty("staging.directory") + "/";
        } else{
            pdfLocationSystemParam = ConfigContext.getCurrentContextConfig().getProperty("homeDirectory")+ "/" + pdfLocationSystemParam +"/";
        }
        LOG.info("PDF LOCATION : " + pdfLocationSystemParam);
        File directory = new File(pdfLocationSystemParam);
        File[] fList = directory.listFiles();
        if (fList != null && fList.length > 0) {
            for (File file : fList) {
                if (file.isFile()) {
                    if (file.getName().contains(loanProcessor.getParameter(OLEParameterConstants.EXP_REQ_TITLE).replaceAll(" ", "_"))) {
                        expiredRequestList.add(file);
                    } else if (file.getName().contains(loanProcessor.getParameter(OLEParameterConstants.COURTESY_TITLE).replaceAll(" ", "_"))) {
                        courtesyNoticeList.add(file);
                    } else if (file.getName().contains(loanProcessor.getParameter(OLEParameterConstants.OVERDUE_TITLE).replaceAll(" ", "_"))) {
                        overDueNoticeList.add(file);
                    } else if (file.getName().contains(loanProcessor.getParameter(OLEParameterConstants.EXPIRED_TITLE).replaceAll(" ", "_"))) {
                        onHoldCourtesyNoticeList.add(file);
                    } else if (file.getName().contains(loanProcessor.getParameter(OLEParameterConstants.ONHOLD_TITLE).replaceAll(" ", "_"))) {
                        onHoldNoticeList.add(file);
                    } else if (file.getName().contains(loanProcessor.getParameter(OLEParameterConstants.RECALL_TITLE).replaceAll(" ", "_"))) {
                        recallNoticeList.add(file);
                    }else if (file.getName().contains(loanProcessor.getParameter(OLEParameterConstants.PICKUP_TITLE).replaceAll(" ", "_"))) {
                        pickupNoticeList.add(file);
                    }
                } /*else if (file.isDirectory()) {
                listf(file.getAbsolutePath());
            }*/
            }
        }
        oleDeliverNoticeForm.setOleRecallNoticeList(generateRecallNoticeList(recallNoticeList));
        oleDeliverNoticeForm.setOleCourtesyNoticeList(generateCourtesyNoticeList(courtesyNoticeList));
        oleDeliverNoticeForm.setOleExpiredRequestNoticeList(generateExpiredRequestNoticeList(expiredRequestList));
        oleDeliverNoticeForm.setOleOnHoldCourtesyNoticeList(generateOnHoldCourtesyNoticeList(onHoldCourtesyNoticeList));
        oleDeliverNoticeForm.setOleOnHoldNoticeList(generateOnHoldNoticeList(onHoldNoticeList));
        oleDeliverNoticeForm.setOleOverDueNoticeList(generateOverDueNoticeList(overDueNoticeList));
        oleDeliverNoticeForm.setOlePickupNoticeList(generatePickupNoticeList(pickupNoticeList));
        return oleDeliverNoticeForm;
    }

    public List<OLERecallNotice> generateRecallNoticeList(List<File> recallNoticeList) {
        List<OLERecallNotice> oleRecallNotices = new ArrayList<OLERecallNotice>();
        OLERecallNotice oleRecallNotice ;
        for(File file :recallNoticeList){
            oleRecallNotice= new OLERecallNotice();
            oleRecallNotice.setFileName(file.getName());
            oleRecallNotice.setFileLocation(file.getAbsolutePath());
            oleRecallNotices.add(oleRecallNotice);
        }
        LOG.info("No of Recall Notices : " +oleRecallNotices.size());
        return oleRecallNotices;

    }


    public List<OLEOnHoldNotice> generateOnHoldNoticeList(List<File> onHoldNoticeList) {
        List<OLEOnHoldNotice> oleOnHoldNoticeList = new ArrayList<OLEOnHoldNotice>();
        OLEOnHoldNotice oleOnHoldNotice ;
        for(File file :onHoldNoticeList){
            oleOnHoldNotice= new OLEOnHoldNotice();
            oleOnHoldNotice.setFileName(file.getName());
            oleOnHoldNotice.setFileLocation(file.getAbsolutePath());
            oleOnHoldNoticeList.add(oleOnHoldNotice);
        }
        LOG.info("No of OnHold Notices : " +oleOnHoldNoticeList.size());
        return oleOnHoldNoticeList;
    }


    public List<OLEOnHoldCourtesyNotice> generateOnHoldCourtesyNoticeList(List<File> onHoldCourtesyNoticeList) {
        List<OLEOnHoldCourtesyNotice> oleOnHoldCourtesyNoticeList = new ArrayList<OLEOnHoldCourtesyNotice>();
        OLEOnHoldCourtesyNotice oleOnHoldCourtesyNotice ;
        for(File file :onHoldCourtesyNoticeList){
            oleOnHoldCourtesyNotice= new OLEOnHoldCourtesyNotice();
            oleOnHoldCourtesyNotice.setFileName(file.getName());
            oleOnHoldCourtesyNotice.setFileLocation(file.getAbsolutePath());
            oleOnHoldCourtesyNoticeList.add(oleOnHoldCourtesyNotice);
        }
        LOG.info("No of OnHold Courtesy  Notices : " +oleOnHoldCourtesyNoticeList.size());
        return oleOnHoldCourtesyNoticeList;
    }

    public List<OLEOverDueNotice> generateOverDueNoticeList(List<File> overDueNoticeList) {
        List<OLEOverDueNotice> oleOverDueNoticeList = new ArrayList<OLEOverDueNotice>();
        OLEOverDueNotice oleOverDueNotice ;
        for(File file :overDueNoticeList){
            oleOverDueNotice= new OLEOverDueNotice();
            oleOverDueNotice.setFileName(file.getName());
            oleOverDueNotice.setFileLocation(file.getAbsolutePath());
            oleOverDueNoticeList.add(oleOverDueNotice);
        }
        LOG.info("No of OverDue Notices : " +oleOverDueNoticeList.size());
        return oleOverDueNoticeList;
    }

    public List<OLECourtesyNotice> generateCourtesyNoticeList(List<File> courtesyNoticeList) {
        List<OLECourtesyNotice> oleCourtesyNoticeList = new ArrayList<OLECourtesyNotice>();
        OLECourtesyNotice oleCourtesyNotice ;
        for(File file :courtesyNoticeList){
            oleCourtesyNotice= new OLECourtesyNotice();
            oleCourtesyNotice.setFileName(file.getName());
            oleCourtesyNotice.setFileLocation(file.getAbsolutePath());
            oleCourtesyNoticeList.add(oleCourtesyNotice);
        }
        LOG.info("No of Courtesy Notices : " +oleCourtesyNoticeList.size());
        return oleCourtesyNoticeList;
    }

    public List<OLEExpiredRequestNotice> generateExpiredRequestNoticeList(List<File> expiredRequestNoticeList) {
        List<OLEExpiredRequestNotice> oleExpiredRequestNoticeList = new ArrayList<OLEExpiredRequestNotice>();
        OLEExpiredRequestNotice oleExpiredRequestNotice ;
        for(File file :expiredRequestNoticeList){
            oleExpiredRequestNotice= new OLEExpiredRequestNotice();
            oleExpiredRequestNotice.setFileName(file.getName());
            oleExpiredRequestNotice.setFileLocation(file.getAbsolutePath());
            oleExpiredRequestNoticeList.add(oleExpiredRequestNotice);
        }
        LOG.info("No of Expired Request  Notices : " +oleExpiredRequestNoticeList.size());
        return oleExpiredRequestNoticeList;
    }
    public List<OLEPickupNotice> generatePickupNoticeList(List<File> pickupNoticeList) {
        List<OLEPickupNotice>  olePickupNotices = new ArrayList<OLEPickupNotice>();
        OLEPickupNotice olePickupNotice ;
        for(File file :pickupNoticeList){
            olePickupNotice= new OLEPickupNotice();
            olePickupNotice.setFileName(file.getName());
            olePickupNotice.setFileLocation(file.getAbsolutePath());
            olePickupNotices.add(olePickupNotice);
        }
        LOG.info("No of Expired Request  Notices : " +olePickupNotices.size());
        return olePickupNotices;
    }



}


