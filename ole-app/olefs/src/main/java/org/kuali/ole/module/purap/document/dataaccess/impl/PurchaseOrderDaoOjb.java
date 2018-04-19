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

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.broker.query.QueryFactory;
import org.apache.ojb.broker.query.ReportQueryByCriteria;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.service.DateFormatHelper;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapPropertyConstants;
import org.kuali.ole.module.purap.businessobject.AutoClosePurchaseOrderView;
import org.kuali.ole.module.purap.businessobject.PurchaseOrderItem;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.document.dataaccess.PurchaseOrderDao;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.framework.persistence.ojb.dao.PlatformAwareDaoBaseOjb;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.kew.actionitem.ActionItem;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * OJB implementation of PurchaseOrderDao.
 */
@Transactional
public class PurchaseOrderDaoOjb extends PlatformAwareDaoBaseOjb implements PurchaseOrderDao {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(PurchaseOrderDaoOjb.class);
    protected ParameterService parameterService;

    public Integer getPurchaseOrderIdForCurrentPurchaseOrderByRelatedDocId(Integer accountsPayablePurchasingDocumentLinkIdentifier) {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("accountsPayablePurchasingDocumentLinkIdentifier", accountsPayablePurchasingDocumentLinkIdentifier);
        criteria.addEqualTo(PurapPropertyConstants.PURCHASE_ORDER_CURRENT_INDICATOR, "Y");

        Collection<PurchaseOrderDocument> poList = getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(PurchaseOrderDocument.class, criteria));
        for (PurchaseOrderDocument purchaseOrderDocument : poList) {
            //should be only one
            return purchaseOrderDocument.getPurapDocumentIdentifier();
        }

        return null;
    }

    public List<PurchaseOrderDocument> getPurchaseOrderIdByRelatedDocId(Integer accountsPayablePurchasingDocumentLinkIdentifier) {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("accountsPayablePurchasingDocumentLinkIdentifier", accountsPayablePurchasingDocumentLinkIdentifier);

        List<PurchaseOrderDocument> poList = (List<PurchaseOrderDocument>) getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(PurchaseOrderDocument.class, criteria));
       if(poList.size() > 0) {
           return poList;
       }

        return null;
    }


    public List<PurchaseOrderDocument> getCurrentPurchaseOrderListByRelatedDocId(Integer accountsPayablePurchasingDocumentLinkIdentifier) {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("accountsPayablePurchasingDocumentLinkIdentifier", accountsPayablePurchasingDocumentLinkIdentifier);
        criteria.addEqualTo(PurapPropertyConstants.PURCHASE_ORDER_CURRENT_INDICATOR, "Y");

        List<PurchaseOrderDocument> poList = (List<PurchaseOrderDocument>) getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(PurchaseOrderDocument.class, criteria));
        if(poList.size() > 0) {
            return poList;
        }
        return null;
    }

    public PurchaseOrderDocument getCurrentPurchaseOrder(Integer id) {
        Criteria criteria = new Criteria();
        criteria.addEqualTo(PurapPropertyConstants.PURAP_DOC_ID, id);
        criteria.addEqualTo(PurapPropertyConstants.PURCHASE_ORDER_CURRENT_INDICATOR, "Y");

        return (PurchaseOrderDocument) getPersistenceBrokerTemplate().getObjectByQuery(new QueryByCriteria(PurchaseOrderDocument.class, criteria));
    }


    /**
     * @see org.kuali.ole.module.purap.document.dataaccess.PurchaseOrderDao#getDocumentNumberForPurchaseOrderId(java.lang.Integer)
     */
    public String getDocumentNumberForPurchaseOrderId(Integer id) {
        Criteria criteria = new Criteria();
        criteria.addEqualTo(PurapPropertyConstants.PURAP_DOC_ID, id);

        return getDocumentNumberUsingPurchaseOrderCriteria(criteria);
    }

    /**
     * @see org.kuali.ole.module.purap.document.dataaccess.PurchaseOrderDao#getDocumentNumberForCurrentPurchaseOrder(java.lang.Integer)
     */
    public String getDocumentNumberForCurrentPurchaseOrder(Integer id) {
        Criteria criteria = new Criteria();
        criteria.addEqualTo(PurapPropertyConstants.PURAP_DOC_ID, id);
        criteria.addEqualTo(PurapPropertyConstants.PURCHASE_ORDER_CURRENT_INDICATOR, "Y");

        return getDocumentNumberUsingPurchaseOrderCriteria(criteria);
    }

    /**
     * @see org.kuali.ole.module.purap.document.dataaccess.PurchaseOrderDao#getOldestPurchaseOrderDocumentNumber(java.lang.Integer)
     */
    public String getOldestPurchaseOrderDocumentNumber(Integer id) {
        Criteria criteria = new Criteria();
        criteria.addEqualTo(PurapPropertyConstants.PURAP_DOC_ID, id);
        ReportQueryByCriteria rqbc = QueryFactory.newReportQuery(PurchaseOrderDocument.class, criteria);
        rqbc.setAttributes(new String[]{OLEPropertyConstants.DOCUMENT_NUMBER});
        //the documents need to be sorted in descending order because we want the 
        //the oldest document number to get the oldest purchase order
        //because the notes remoteobjectid is set to the object id of the oldest
        //purchase order document.
        //OLEMI-8394
        rqbc.addOrderByDescending(OLEPropertyConstants.DOCUMENT_NUMBER);

        String oldestDocumentNumber = null;
        java.util.Iterator iter = getPersistenceBrokerTemplate().getReportQueryIteratorByQuery(rqbc);
        while (iter.hasNext()) {
            final Object[] results = (Object[]) iter.next();
            oldestDocumentNumber = (String) results[0];
        }

        return oldestDocumentNumber;
    }

    /**
     * Retrieves the document number of the purchase order returned by the passed in criteria.
     *
     * @param criteria - list of criteria to use in the retrieve
     * @return Document number string if a valid purchase order is found, null if no purchase order is found
     */
    protected String getDocumentNumberUsingPurchaseOrderCriteria(Criteria criteria) {
        List<String> returnList = getDocumentNumbersUsingPurchaseOrderCriteria(criteria);

        if (returnList.isEmpty()) {
            return null;
        }

        if (returnList.size() > 1) {
            // the list should have held only a single doc id of data but it holds 2 or more
            String errorMsg = "Expected single document number for given criteria but multiple (at least 2) were returned";
            LOG.error(errorMsg);
            throw new RuntimeException();

        } else {
            // at this part of the code, we know there's no more elements in iterator
            return returnList.get(0);
        }
    }

    /**
     * Retrieves a list of document numbers of the purchase order returned by the passed in criteria.
     *
     * @param criteria - list of criteria to use in the retrieve
     * @return Iterator of document numbers
     */
    protected List<String> getDocumentNumbersUsingPurchaseOrderCriteria(Criteria criteria) {
        ReportQueryByCriteria rqbc = new ReportQueryByCriteria(PurchaseOrderDocument.class, criteria);
        List<String> returnList = new ArrayList<String>();

        rqbc.addOrderByAscending(OLEPropertyConstants.DOCUMENT_NUMBER);

        List<PurchaseOrderDocument> poDocs = (List<PurchaseOrderDocument>) getPersistenceBrokerTemplate().getCollectionByQuery(rqbc);

        for (PurchaseOrderDocument poDoc : poDocs) {
            returnList.add(poDoc.getDocumentNumber());
        }

        return returnList;
    }

    /**
     * @see org.kuali.ole.module.purap.document.dataaccess.PurchaseOrderDao#itemExistsOnPurchaseOrder(java.lang.Integer, java.lang.String)
     */
    public boolean itemExistsOnPurchaseOrder(Integer poItemLineNumber, String docNumber) {
        boolean existsInPo = false;

        Criteria criteria = new Criteria();
        criteria.addEqualTo("documentNumber", docNumber);
        criteria.addEqualTo("itemLineNumber", poItemLineNumber);

        ReportQueryByCriteria rqbc = new ReportQueryByCriteria(PurchaseOrderItem.class, criteria);
        //  rqbc.setAttributes(new String[] { OLEPropertyConstants.DOCUMENT_NUMBER });
        rqbc.addOrderByAscending(OLEPropertyConstants.DOCUMENT_NUMBER);

        List<PurchaseOrderItem> poItems = (List<PurchaseOrderItem>) getPersistenceBrokerTemplate().getCollectionByQuery(rqbc);
        if (!poItems.isEmpty()) {
            existsInPo = true;
        }

        return existsInPo;
    }

    /**
     * @see org.kuali.ole.module.purap.document.dataaccess.PurchaseOrderDao#getAllOpenPurchaseOrders(java.util.List)
     */
    public List<AutoClosePurchaseOrderView> getAllOpenPurchaseOrders(List<String> excludedVendorChoiceCodes) {
        LOG.debug("getAllOpenPurchaseOrders() started");
        Criteria criteria = new Criteria();
        criteria.addIsNull(PurapPropertyConstants.RECURRING_PAYMENT_TYPE_CODE);
        Boolean encumb_check = parameterService.getParameterValueAsBoolean(PurapConstants.PURAP_NAMESPACE, "All", OLEConstants.ZERO_ENCUMBRANCE_CHECK, Boolean.FALSE);
        if(encumb_check) {
            criteria.addEqualTo(PurapPropertyConstants.TOTAL_ENCUMBRANCE, new KualiDecimal(0));
        }
        criteria.addEqualTo(PurapPropertyConstants.PURCHASE_ORDER_CURRENT_INDICATOR, true);
        for (String excludeCode : excludedVendorChoiceCodes) {
            criteria.addNotEqualTo(PurapPropertyConstants.VENDOR_CHOICE_CODE, excludeCode);
        }
        QueryByCriteria qbc = new QueryByCriteria(AutoClosePurchaseOrderView.class, criteria);
        if (LOG.isDebugEnabled()) {
            LOG.debug("getAllOpenPurchaseOrders() Query criteria is " + criteria.toString());
        }
        List<AutoClosePurchaseOrderView> l = (List<AutoClosePurchaseOrderView>) getPersistenceBrokerTemplate().getCollectionByQuery(qbc);
        LOG.debug("getAllOpenPurchaseOrders() ended.");
        return l;
    }

    /**
     * @see org.kuali.ole.module.purap.document.dataaccess.PurchaseOrderDao#getAllOpenPurchaseOrders(java.util.List,java.sql.Date,java.sql.Date,java.util.Collection)
     */
    public List<AutoClosePurchaseOrderView> getAllOpenPurchaseOrders(List<String> excludedVendorChoiceCodes, java.sql.Date poCloseFromDate,Date poCloseToDate, Collection<String> orderTypeString) {
        LOG.debug("getAllOpenPurchaseOrders() started");
        Criteria criteria = new Criteria();
        criteria.addIsNull(PurapPropertyConstants.RECURRING_PAYMENT_TYPE_CODE);
        Boolean encumb_check = getParameterService().getParameterValueAsBoolean(PurapConstants.PURAP_NAMESPACE, "All", OLEConstants.ZERO_ENCUMBRANCE_CHECK, Boolean.FALSE);
        if(encumb_check) {
            criteria.addEqualTo(PurapPropertyConstants.TOTAL_ENCUMBRANCE, new KualiDecimal(0));
        }
        criteria.addEqualTo(PurapPropertyConstants.PURCHASE_ORDER_CURRENT_INDICATOR, true);
        criteria.addEqualTo(PurapPropertyConstants.APP_DOC_STAT, PurapConstants.PurchaseOrderStatuses.APPDOC_OPEN);
        criteria.addGreaterOrEqualThan(PurapPropertyConstants.TOTAL_AMOUNT, new KualiDecimal(0));
        criteria.addGreaterThan(PurapPropertyConstants.INVOICED_QTY, new KualiDecimal(0));
        if(orderTypeString != null) {
            criteria.addIn(PurapPropertyConstants.PO_TYPE, orderTypeString);
        }
        String dbVendor = getProperty("db.vendor");
        if (dbVendor.equals("mysql")) {
            if (poCloseFromDate != null) {
                criteria.addGreaterOrEqualThan(PurapPropertyConstants.PO_CREATE_DATE, poCloseFromDate);
            }
            if (poCloseToDate != null) {
                criteria.addLessOrEqualThan(PurapPropertyConstants.PO_CREATE_DATE, poCloseToDate);
            }
        }
        else {
            if (poCloseFromDate != null) {
                //java.util.Date  ss1 = new java.util.Date(poCloseFromDate.toString());
                SimpleDateFormat formatter=new SimpleDateFormat("MM/dd/yyyy");
                String date = formatter.format(poCloseFromDate);
                criteria.addGreaterOrEqualThan(PurapPropertyConstants.PO_CREATE_DATE, formatDateForOracle(date));
              //  criteria.addGreaterOrEqualThan(PurapPropertyConstants.PO_CREATE_DATE, poCloseFromDate);
            }
            if (poCloseToDate != null) {
                //java.util.Date  ss1=new java.util.Date(poCloseToDate.toString());
                SimpleDateFormat formatter=new SimpleDateFormat("MM/dd/yyyy");
                String date = formatter.format(poCloseToDate);
                criteria.addLessOrEqualThan( PurapPropertyConstants.PO_CREATE_DATE ,formatDateForOracle(date));
              //  criteria.addLessOrEqualThan(PurapPropertyConstants.PO_CREATE_DATE, poCloseToDate);
            }
        }
        for (String excludeCode : excludedVendorChoiceCodes) {
            criteria.addNotEqualTo(PurapPropertyConstants.VENDOR_CHOICE_CODE, excludeCode);
        }
        QueryByCriteria qbc = new QueryByCriteria(AutoClosePurchaseOrderView.class, criteria);
        if (LOG.isDebugEnabled()) {
            LOG.debug("getAllOpenPurchaseOrders() Query criteria is " + criteria.toString());
        }
        System.out.println("query criteria  >>>>" + qbc);
        List<AutoClosePurchaseOrderView> l = (List<AutoClosePurchaseOrderView>) getPersistenceBrokerTemplate().getCollectionByQuery(qbc);
        LOG.debug("getAllOpenPurchaseOrders() ended.");
        return l;
    }

    /**
     * @see org.kuali.ole.module.purap.document.dataaccess.PurchaseOrderDao#getAutoCloseRecurringPurchaseOrders(java.util.List)
     */
    public List<AutoClosePurchaseOrderView> getAutoCloseRecurringPurchaseOrders(List<String> excludedVendorChoiceCodes) {
        LOG.debug("getAutoCloseRecurringPurchaseOrders() started.");
        Criteria criteria = new Criteria();
        criteria.addNotNull(PurapPropertyConstants.RECURRING_PAYMENT_TYPE_CODE);
        //PURCHASE_ORDER_STATUS_CODE does not exist in tables anymore but it is on workflowdocument.
        //the checking for open status is done in PurchaseOrderServiceImpl class - autoCloseRecurringOrders method.
        for (String excludeCode : excludedVendorChoiceCodes) {
            criteria.addNotEqualTo(PurapPropertyConstants.VENDOR_CHOICE_CODE, excludeCode);
        }
        QueryByCriteria qbc = new QueryByCriteria(AutoClosePurchaseOrderView.class, criteria);
        if (LOG.isDebugEnabled()) {
            LOG.debug("getAutoCloseRecurringPurchaseOrders() Query criteria is " + criteria.toString());
        }
        List<AutoClosePurchaseOrderView> l = (List<AutoClosePurchaseOrderView>) getPersistenceBrokerTemplate().getCollectionByQuery(qbc);
        //we need to include in this list only those whose workflowdocument appDocStatus = APPDOC_OPEN

        LOG.debug("getAutoCloseRecurringPurchaseOrders() ended.");

        return l;
    }

    public List<PurchaseOrderDocument> getPendingPurchaseOrdersForFaxing() {
        LOG.debug("Getting pending purchase orders for faxing");
        Criteria criteria = new Criteria();
        QueryByCriteria qbc = new QueryByCriteria(PurchaseOrderDocument.class, criteria);
        List<PurchaseOrderDocument> l = (List<PurchaseOrderDocument>) getPersistenceBrokerTemplate().getCollectionByQuery(qbc);

        return l;
    }

    protected String getProperty(String property) {
        return ConfigContext.getCurrentContextConfig().getProperty(property);
    }

    private String formatDateForOracle(String overdueNoticeToDate) {
        String forOracle = DateFormatHelper.getInstance().generateDateStringsForOracle(overdueNoticeToDate);
        return forOracle;
    }

    public void deleteFromWorkflowItem(String documentNumber) {

        Criteria criteria = new Criteria();
        criteria.addEqualTo(PurapPropertyConstants.DOC_HDR_ID, documentNumber);
        getPersistenceBrokerTemplate().deleteByQuery(new QueryByCriteria(ActionItem.class, criteria));
        getPersistenceBrokerTemplate().clearCache();
    }

    public void setParameterService(ParameterService parameterService)
    {
        this.parameterService = parameterService;
    }

    public ParameterService getParameterService() {
        if (null == parameterService){
            parameterService = (ParameterService) SpringContext.getBean("parameterService");
        }
        return parameterService;
    }
}
