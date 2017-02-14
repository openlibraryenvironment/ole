package org.kuali.ole.deliver.batch;

import org.apache.log4j.Logger;
import org.kuali.ole.deliver.service.OleDeliverRequestDocumentHelperServiceImpl;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 11/7/12
 * Time: 3:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleOverDueNotice {

    private static final Logger LOG = Logger.getLogger(OleOverDueNotice.class);
    private OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService;
    private OleShelvingLagTime oleShelvingLagTime;

    public OleShelvingLagTime getOleShelvingLagTime() {
        if (oleShelvingLagTime == null) {
            oleShelvingLagTime = new OleShelvingLagTime();
        }
        return oleShelvingLagTime;
    }

    public void setOleShelvingLagTime(OleShelvingLagTime oleShelvingLagTime) {
        this.oleShelvingLagTime = oleShelvingLagTime;
    }

    public OleDeliverRequestDocumentHelperServiceImpl getOleDeliverRequestDocumentHelperService() {
        if (oleDeliverRequestDocumentHelperService == null) {
            oleDeliverRequestDocumentHelperService = new OleDeliverRequestDocumentHelperServiceImpl();
        }
        return oleDeliverRequestDocumentHelperService;
    }

    public void setOleDeliverRequestDocumentHelperService(OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService) {
        this.oleDeliverRequestDocumentHelperService = oleDeliverRequestDocumentHelperService;
    }

    public void generateNotices() {
        LOG.debug("Start of scheduled job to execute generateNotices.");
        try {
            oleDeliverRequestDocumentHelperService = getOleDeliverRequestDocumentHelperService();
            oleDeliverRequestDocumentHelperService.generateNotices();
        } catch (Exception ex) {
            LOG.error("Exception occurred while performing generateNotices", ex);
        }

    }

    public void generateHoldExpirationNotice() {
        LOG.debug("Start of scheduled job to execute generateHoldExpirationNotice.");
        try {
            oleDeliverRequestDocumentHelperService = getOleDeliverRequestDocumentHelperService();
            oleDeliverRequestDocumentHelperService.generateHoldExpirationNotice();

        } catch (Exception ex) {
            LOG.error("Exception occurred while performing generateHoldExpirationNotice", ex);
        }

    }

    public void generateHoldCourtesyNotice() {
        LOG.debug("Start of scheduled job to execute generateHoldCourtesyNotice.");
        try {
            oleDeliverRequestDocumentHelperService = getOleDeliverRequestDocumentHelperService();
            oleDeliverRequestDocumentHelperService.generateOnHoldCourtesyNotice();

        } catch (Exception ex) {
            LOG.error("Exception occurred while performing generateHoldCourtesyNotice", ex);
        }

    }

    public void deleteTemporaryHistoryRecord() {
        LOG.debug("Start of scheduled job to execute deleteTemporaryHistoryRecord.");
        try {
            oleDeliverRequestDocumentHelperService = getOleDeliverRequestDocumentHelperService();
            oleDeliverRequestDocumentHelperService.deleteTemporaryHistoryRecord();

        } catch (Exception ex) {
            LOG.error("Exception occurred while performing deleteTemporaryHistoryRecord", ex);
        }

    }

    public void generateRequestExpirationNotice() {
        LOG.debug("Start of scheduled job to execute generateRequestExpirationNotice.");
        try {
            oleDeliverRequestDocumentHelperService = getOleDeliverRequestDocumentHelperService();
            oleDeliverRequestDocumentHelperService.generateRequestExpirationNotice();

        } catch (Exception ex) {
            LOG.error("Exception occurred while performing generateRequestExpirationNotice", ex);
        }

    }

    public void deletingExpiredRequests() {
        LOG.debug("Start of scheduled job to execute deletingExpiredRequests.");
        try {
            oleDeliverRequestDocumentHelperService = getOleDeliverRequestDocumentHelperService();
            oleDeliverRequestDocumentHelperService.deletingExpiredRequests();
        } catch (Exception ex) {
            LOG.error("Exception occurred while performing deletingExpiredRequests", ex);
        }

    }

    public void generateOnHoldNotice() {
        LOG.debug("Start of scheduled job to execute generateOnHoldNotice.");
        try {
            oleDeliverRequestDocumentHelperService = getOleDeliverRequestDocumentHelperService();
            oleDeliverRequestDocumentHelperService.generateOnHoldNotice();

        } catch (Exception ex) {
            LOG.error("Exception occurred while performing generateOnHoldNotice", ex);
        }

    }

    public void updateStatusIntoAvailableAfterReShelving() {
        LOG.debug("Start of scheduled job to execute updateStatusIntoAvailableAfterReShelving.");
        try {
            oleShelvingLagTime = getOleShelvingLagTime();
            oleShelvingLagTime.updateStatusIntoAvailableAfterReShelving();

        } catch (Exception ex) {
            LOG.error("Exception occurred while performing updateStatusIntoAvailableAfterReShelving", ex);
        }

    }

    public void generateCourtesyNotices(){
        LOG.debug("Start of scheduled job to execute generateCourtesyNotices.");
        try {
            oleDeliverRequestDocumentHelperService = getOleDeliverRequestDocumentHelperService();
            oleDeliverRequestDocumentHelperService.generateCourtesyNotice();

        } catch (Exception ex) {
            LOG.error("Exception occurred while performing generateCourtesyNotices", ex);
        }
    }


    public void generateOverdueNotices(){
        LOG.debug("Start of scheduled job to execute generateOverdueNotices.");
        try {
            oleDeliverRequestDocumentHelperService = getOleDeliverRequestDocumentHelperService();
            oleDeliverRequestDocumentHelperService.generateOverdueNotice();

        } catch (Exception ex) {
            LOG.error("Exception occurred while performing generateOverdueNotices", ex);
        }
    }

    public void generateLostNotices(){
        LOG.debug("Start of scheduled job to execute generateLostNotices.");
        try {
            oleDeliverRequestDocumentHelperService = getOleDeliverRequestDocumentHelperService();
            oleDeliverRequestDocumentHelperService.generateLostNotice();

        } catch (Exception ex) {
            LOG.error("Exception occurred while performing generateLostNotices", ex);
        }
    }

    public void deleteLoanNoticeHistory() {
        LOG.debug("Start of scheduled job to execute deleteLoanNoticeHistory.");
        try {
            oleDeliverRequestDocumentHelperService = getOleDeliverRequestDocumentHelperService();
            oleDeliverRequestDocumentHelperService.deleteLoanNoticeHistoryRecord();

        } catch (Exception ex) {
            LOG.error("Exception occurred while performing deleteLoanNoticeHistory", ex);
        }

    }

    public void deleteRenewalHistory() {
        LOG.debug("Start of scheduled job to execute deleteRenewalHistory.");
        try {
            oleDeliverRequestDocumentHelperService = getOleDeliverRequestDocumentHelperService();
            oleDeliverRequestDocumentHelperService.deleteRenewalHistoryRecord();

        } catch (Exception ex) {
            LOG.error("Exception occurred while performing deleteRenewalHistory", ex);
        }

    }

    public void deleteReturnHistory() {
        LOG.debug("Start of scheduled job to execute deleteReturnHistory.");
        try {
            oleDeliverRequestDocumentHelperService = getOleDeliverRequestDocumentHelperService();
            oleDeliverRequestDocumentHelperService.deleteReturnHistoryRecord();

        } catch (Exception ex) {
            LOG.error("Exception occurred while performing deleteReturnHistory", ex);
        }

    }

    public void deleteRequestHistory() {
        LOG.debug("Start of scheduled job to execute deleteRequestHistory.");
        try {
            oleDeliverRequestDocumentHelperService = getOleDeliverRequestDocumentHelperService();
            oleDeliverRequestDocumentHelperService.deleteRequestHistoryRecord();

        } catch (Exception ex) {
            LOG.error("Exception occurred while performing deleteReturnHistory", ex);
        }

    }

}
