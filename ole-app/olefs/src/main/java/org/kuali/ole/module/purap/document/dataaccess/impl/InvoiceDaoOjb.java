/*
 * Copyright 2007 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl2.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.module.purap.document.dataaccess.impl;

import org.apache.log4j.Logger;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.broker.query.ReportQueryByCriteria;
import org.kuali.ole.module.purap.PurapPropertyConstants;
import org.kuali.ole.module.purap.document.InvoiceDocument;
import org.kuali.ole.module.purap.document.dataaccess.InvoiceDao;
import org.kuali.ole.module.purap.util.VendorGroupingHelper;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.framework.persistence.ojb.dao.PlatformAwareDaoBaseOjb;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * OJB Implementation of InvoiceDao.
 */
@Transactional
public class InvoiceDaoOjb extends PlatformAwareDaoBaseOjb implements InvoiceDao {
    private static Logger LOG = Logger.getLogger(InvoiceDaoOjb.class);

    /**
     * The special payments query should be this: select * from pur.ap_pmt_rqst_t where pmt_rqst_stat_cd in ('AUTO', 'DPTA') and
     * prcs_cmp_cd = ? and pmt_extrt_ts is NULL and pmt_hld_ind = 'N' and ( ( ( pmt_spcl_handlg_instrc_ln1_txt is not NULL or
     * pmt_spcl_handlg_instrc_ln2_txt is not NULL or pmt_spcl_handlg_instrc_ln3_txt is not NULL or pmt_att_ind = 'Y') and trunc
     * (pmt_rqst_pay_dt) <= trunc (sysdate)) or IMD_PMT_IND = 'Y')})
     *
     * @see org.kuali.ole.module.purap.document.dataaccess.InvoiceDao#getInvoicesToExtract(boolean, String, Date)
     */
    public List<InvoiceDocument> getInvoicesToExtract(boolean onlySpecialPayments, String chartCode, Date onOrBeforeInvoicePayDate) {
        LOG.debug("getInvoicesToExtract() started");

        Criteria criteria = new Criteria();
        if (chartCode != null) {
            criteria.addEqualTo("processingCampusCode", chartCode);
        }
        //criteria.addIn(PurapPropertyConstants.STATUS_CODE, Arrays.asList(InvoiceStatuses.STATUSES_ALLOWED_FOR_EXTRACTION));
        criteria.addIsNull("extractedTimestamp");
        criteria.addEqualTo("holdIndicator", Boolean.FALSE);

        if (onlySpecialPayments) {
            Criteria a = new Criteria();

            Criteria c1 = new Criteria();
            c1.addNotNull("specialHandlingInstructionLine1Text");
            Criteria c2 = new Criteria();
            c2.addNotNull("specialHandlingInstructionLine2Text");
            Criteria c3 = new Criteria();
            c3.addNotNull("specialHandlingInstructionLine3Text");
            Criteria c4 = new Criteria();
            c4.addEqualTo("paymentAttachmentIndicator", Boolean.TRUE);

            c1.addOrCriteria(c2);
            c1.addOrCriteria(c3);
            c1.addOrCriteria(c4);

            a.addAndCriteria(c1);
            a.addLessOrEqualThan("invoicePayDate", onOrBeforeInvoicePayDate);

            Criteria c5 = new Criteria();
            c5.addEqualTo("immediatePaymentIndicator", Boolean.TRUE);
            c5.addOrCriteria(a);

            criteria.addAndCriteria(a);
        } else {
            Criteria c1 = new Criteria();
            c1.addLessOrEqualThan("invoicePayDate", onOrBeforeInvoicePayDate);

            Criteria c2 = new Criteria();
            c2.addEqualTo("immediatePaymentIndicator", Boolean.TRUE);

            c1.addOrCriteria(c2);
            criteria.addAndCriteria(c1);
        }

        return (List<InvoiceDocument>) getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(InvoiceDocument.class, criteria));
    }

    /**
     * @see org.kuali.ole.module.purap.document.dataaccess.InvoiceDao#getImmediateInvoicesToExtract(String)
     */
    public List<InvoiceDocument> getImmediateInvoicesToExtract(String chartCode) {
        LOG.debug("getImmediateInvoicesToExtract() started");

        Criteria criteria = new Criteria();
        if (chartCode != null) {
            criteria.addEqualTo("processingCampusCode", chartCode);
        }

        //criteria.addIn(PurapPropertyConstants.STATUS_CODE, Arrays.asList(InvoiceStatuses.STATUSES_ALLOWED_FOR_EXTRACTION));
        criteria.addIsNull("extractedTimestamp");
        criteria.addEqualTo("immediatePaymentIndicator", Boolean.TRUE);

        return (List<InvoiceDocument>) getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(InvoiceDocument.class, criteria));
    }

    /**
     * @see org.kuali.ole.module.purap.document.dataaccess.InvoiceDao#getInvoicesToExtract(String, Integer, Integer, Integer, Integer, Date)
     */
    @Deprecated
    public List<InvoiceDocument> getInvoicesToExtract(String campusCode, Integer invoiceIdentifier, Integer purchaseOrderIdentifier, Integer vendorHeaderGeneratedIdentifier, Integer vendorDetailAssignedIdentifier, Date currentSqlDateMidnight) {
        LOG.debug("getInvoicesToExtract() started");

        Criteria criteria = new Criteria();
        criteria.addEqualTo("processingCampusCode", campusCode);
        //criteria.addIn(PurapPropertyConstants.STATUS_CODE, statuses);
        criteria.addIsNull("extractedTimestamp");
        criteria.addEqualTo("holdIndicator", Boolean.FALSE);

        Criteria c1 = new Criteria();
        c1.addLessOrEqualThan("invoicePayDate", currentSqlDateMidnight);

        Criteria c2 = new Criteria();
        c2.addEqualTo("immediatePaymentIndicator", Boolean.TRUE);

        c1.addOrCriteria(c2);
        criteria.addAndCriteria(c1);

        if (invoiceIdentifier != null) {
            criteria.addEqualTo("purapDocumentIdentifier", invoiceIdentifier);
        }
        if (purchaseOrderIdentifier != null) {
            criteria.addEqualTo("purchaseOrderIdentifier", purchaseOrderIdentifier);
        }
        criteria.addEqualTo("vendorHeaderGeneratedIdentifier", vendorHeaderGeneratedIdentifier);
        criteria.addEqualTo("vendorDetailAssignedIdentifier", vendorDetailAssignedIdentifier);

        return (List<InvoiceDocument>) getPersistenceBrokerTemplate().getIteratorByQuery(new QueryByCriteria(InvoiceDocument.class, criteria));
    }

    /**
     * @see org.kuali.ole.module.purap.document.dataaccess.InvoiceDao#getInvoicesToExtractForVendor(String,
     *      org.kuali.ole.module.purap.util.VendorGroupingHelper, Date)
     */
    public Collection<InvoiceDocument> getInvoicesToExtractForVendor(String campusCode, VendorGroupingHelper vendor, Date onOrBeforeInvoicePayDate) {
        LOG.debug("getInvoicesToExtract() started");

        Criteria criteria = new Criteria();
        criteria.addEqualTo("processingCampusCode", campusCode);
        //criteria.addIn(PurapPropertyConstants.STATUS_CODE, statuses);
        criteria.addIsNull("extractedTimestamp");
        criteria.addEqualTo("holdIndicator", Boolean.FALSE);

        Criteria c1 = new Criteria();
        c1.addLessOrEqualThan("invoicePayDate", onOrBeforeInvoicePayDate);

        Criteria c2 = new Criteria();
        c2.addEqualTo("immediatePaymentIndicator", Boolean.TRUE);

        c1.addOrCriteria(c2);
        criteria.addAndCriteria(c1);

        criteria.addEqualTo("vendorHeaderGeneratedIdentifier", vendor.getVendorHeaderGeneratedIdentifier());
        criteria.addEqualTo("vendorDetailAssignedIdentifier", vendor.getVendorDetailAssignedIdentifier());
        criteria.addEqualTo("vendorCountryCode", vendor.getVendorCountry());
        criteria.addLike("vendorPostalCode", vendor.getVendorPostalCode() + "%");

        return getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(InvoiceDocument.class, criteria));
    }

    /**
     * @see org.kuali.ole.module.purap.document.dataaccess.InvoiceDao#getEligibleForAutoApproval(Date)
     */
    public List<String> getEligibleForAutoApproval(Date todayAtMidnight) {

        Criteria criteria = new Criteria();
        criteria.addLessOrEqualThan(PurapPropertyConstants.PAYMENT_REQUEST_PAY_DATE, todayAtMidnight);
        criteria.addNotEqualTo("holdIndicator", "Y");
        criteria.addNotEqualTo("invoiceCancelIndicator", "Y");

        List<String> returnList = getDocumentNumbersOfInvoiceByCriteria(criteria, false);

        return returnList;
    }


    /**
     * @see org.kuali.ole.module.purap.document.dataaccess.InvoiceDao#getDocumentNumberByInvoiceId(Integer)
     */
    public String getDocumentNumberByInvoiceId(Integer id) {
        Criteria criteria = new Criteria();
        criteria.addEqualTo(PurapPropertyConstants.PURAP_DOC_ID, id);
        return getDocumentNumberOfInvoiceByCriteria(criteria);
    }

    public OleInvoiceDocument getDocumentByInvoiceId(Integer id) {
        Criteria criteria = new Criteria();
        criteria.addEqualTo(PurapPropertyConstants.PURAP_DOC_ID, id);
        return getInvoiceDocumentByCriteria(criteria);
    }

    /**
     * @see org.kuali.ole.module.purap.document.dataaccess.InvoiceDao#getDocumentNumbersByPurchaseOrderId(Integer)
     */
    public List<String> getDocumentNumbersByPurchaseOrderId(Integer poPurApId) {
        List<String> returnList = new ArrayList<String>();
        Criteria criteria = new Criteria();
        criteria.addEqualTo(PurapPropertyConstants.PURCHASE_ORDER_IDENTIFIER, poPurApId);
        returnList = getDocumentNumbersOfInvoiceByCriteria(criteria, false);

        return returnList;
    }

    /**
     * Retrieves a document number for a invoice by user defined criteria.
     *
     * @param criteria - list of criteria to use in the retrieve
     * @return - document number
     */
    protected String getDocumentNumberOfInvoiceByCriteria(Criteria criteria) {
        LOG.debug("getDocumentNumberOfInvoiceByCriteria() started");
        List<String> returnList = getDocumentNumbersOfInvoiceByCriteria(criteria, false);

        if (returnList.isEmpty()) {
            return null;
        }

        if (returnList.size() > 1) {
            // the list should have held only a single doc id of data but it holds 2 or more
            String errorMsg = "Expected single document number for given criteria but multiple (at least 2) were returned";
            LOG.error(errorMsg);
            throw new RuntimeException();

        } else {
            return (returnList.get(0));
        }
    }

    protected OleInvoiceDocument getInvoiceDocumentByCriteria(Criteria criteria) {
        LOG.debug("getDocumentNumberOfInvoiceByCriteria() started");
        List<OleInvoiceDocument> returnList = getInvoiceDocumentByCriteria(criteria, false);

        if (returnList.isEmpty()) {
            return null;
        }

        if (returnList.size() > 1) {
            // the list should have held only a single doc id of data but it holds 2 or more
            String errorMsg = "Expected single document number for given criteria but multiple (at least 2) were returned";
            LOG.error(errorMsg);
            throw new RuntimeException();

        } else {
            return (returnList.get(0));
        }
    }

    protected List<OleInvoiceDocument> getInvoiceDocumentByCriteria(Criteria criteria, boolean orderByAscending) {
        LOG.debug("getDocumentNumberOfInvoiceByCriteria() started");
        ReportQueryByCriteria rqbc = new ReportQueryByCriteria(OleInvoiceDocument.class, criteria);
            rqbc.addOrderByDescending(OLEPropertyConstants.DOCUMENT_NUMBER);

        List<OleInvoiceDocument> prDocs = (List<OleInvoiceDocument>) getPersistenceBrokerTemplate().getCollectionByQuery(rqbc);

        return prDocs;
    }

    /**
     * Retrieves a document number for a invoice by user defined criteria and sorts the values ascending if orderByAscending
     * parameter is true, descending otherwise.
     *
     * @param criteria         - list of criteria to use in the retrieve
     * @param orderByAscending - boolean to sort results ascending if true, descending otherwise
     * @return - Iterator of document numbers
     */
    protected List<String> getDocumentNumbersOfInvoiceByCriteria(Criteria criteria, boolean orderByAscending) {
        LOG.debug("getDocumentNumberOfInvoiceByCriteria() started");
        ReportQueryByCriteria rqbc = new ReportQueryByCriteria(InvoiceDocument.class, criteria);
        if (orderByAscending) {
            rqbc.addOrderByAscending(OLEPropertyConstants.DOCUMENT_NUMBER);
        } else {
            rqbc.addOrderByDescending(OLEPropertyConstants.DOCUMENT_NUMBER);
        }

        List<String> returnList = new ArrayList<String>();

        List<InvoiceDocument> prDocs = (List<InvoiceDocument>) getPersistenceBrokerTemplate().getCollectionByQuery(rqbc);
        for (InvoiceDocument prDoc : prDocs) {
            returnList.add(prDoc.getDocumentNumber());
        }

        return returnList;
    }

    /**
     * Retrieves a list of invoices by user defined criteria.
     *
     * @param qbc - query with criteria
     * @return - list of invoices
     */
    protected List<InvoiceDocument> getInvoicesByQueryByCriteria(QueryByCriteria qbc) {
        LOG.debug("getInvoicesByQueryByCriteria() started");
        return (List<InvoiceDocument>) getPersistenceBrokerTemplate().getCollectionByQuery(qbc);
    }

    /**
     * Retrieves a list of invoices with the given vendor id and invoice number.
     *
     * @param vendorHeaderGeneratedId - header id of the vendor id
     * @param vendorDetailAssignedId  - detail id of the vendor id
     * @param invoiceNumber           - invoice number as entered by AP
     * @return - List of invoices.
     */
    public List<InvoiceDocument> getActiveInvoicesByVendorNumberInvoiceNumber(Integer vendorHeaderGeneratedId, Integer vendorDetailAssignedId, String invoiceNumber) {
        LOG.debug("getActiveInvoicesByVendorNumberInvoiceNumber() started");
        Criteria criteria = new Criteria();
        criteria.addEqualTo("vendorHeaderGeneratedIdentifier", vendorHeaderGeneratedId);
        criteria.addEqualTo("vendorDetailAssignedIdentifier", vendorDetailAssignedId);
        criteria.addEqualTo("invoiceNumber", invoiceNumber);
        QueryByCriteria qbc = new QueryByCriteria(InvoiceDocument.class, criteria);
        return this.getInvoicesByQueryByCriteria(qbc);
    }

    /**
     * Retrieves a list of invoices with the given vendor id and invoice number.
     *
     * @param vendorHeaderGeneratedId - header id of the vendor id
     * @param vendorDetailAssignedId  - detail id of the vendor id
     * @return - List of invoices.
     */
    public List<InvoiceDocument> getActiveInvoicesByVendorNumber(Integer vendorHeaderGeneratedId, Integer vendorDetailAssignedId) {
        LOG.debug("getActiveInvoicesByVendorNumber started");
        Criteria criteria = new Criteria();
        criteria.addEqualTo("vendorHeaderGeneratedIdentifier", vendorHeaderGeneratedId);
        criteria.addEqualTo("vendorDetailAssignedIdentifier", vendorDetailAssignedId);
        QueryByCriteria qbc = new QueryByCriteria(InvoiceDocument.class, criteria);
        return this.getInvoicesByQueryByCriteria(qbc);
    }


    /**
     * @see org.kuali.ole.module.purap.document.dataaccess.InvoiceDao#getActiveInvoicesByPOIdInvoiceAmountInvoiceDate(Integer,
     *      org.kuali.rice.core.api.util.type.KualiDecimal, java.sql.Date)
     */
    public List<InvoiceDocument> getActiveInvoicesByPOIdInvoiceAmountInvoiceDate(Integer poId, KualiDecimal vendorInvoiceAmount, Date invoiceDate) {
        LOG.debug("getActiveInvoicesByVendorNumberInvoiceNumber() started");
        Criteria criteria = new Criteria();
        criteria.addEqualTo("purchaseOrderIdentifier", poId);
        criteria.addEqualTo("vendorInvoiceAmount", vendorInvoiceAmount);
        criteria.addEqualTo("invoiceDate", invoiceDate);
        QueryByCriteria qbc = new QueryByCriteria(InvoiceDocument.class, criteria);
        return this.getInvoicesByQueryByCriteria(qbc);
    }

    public List<String> getActiveInvoiceDocumentNumbersForPurchaseOrder(Integer purchaseOrderId) {
        LOG.debug("getActiveInvoicesByVendorNumberInvoiceNumber() started");

        List<String> returnList = new ArrayList<String>();
        Criteria criteria = new Criteria();

        criteria.addEqualTo(PurapPropertyConstants.PURCHASE_ORDER_IDENTIFIER, purchaseOrderId);
        returnList = getDocumentNumbersOfInvoiceByCriteria(criteria, false);

        return returnList;
    }

    public List<OleInvoiceDocument> getInvoiceInReceivingStatus() {
        Criteria criteria = new Criteria();
        criteria.addNotEqualTo("holdIndicator", "Y");
        criteria.addNotEqualTo("invoiceCancelIndicator", "Y");

     //   List<OleInvoiceDocument> returnList = new ArrayList<String>();
        List<OleInvoiceDocument> returnList = getInvoiceDocumentByCriteria(criteria, false);

        return returnList;

    }
}

