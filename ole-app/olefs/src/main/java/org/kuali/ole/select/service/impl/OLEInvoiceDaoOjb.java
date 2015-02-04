package org.kuali.ole.select.service.impl;

import org.apache.log4j.Logger;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryFactory;
import org.apache.ojb.broker.query.ReportQueryByCriteria;
import org.kuali.ole.select.businessobject.OleInvoiceItem;
import org.kuali.ole.sys.util.TransactionalServiceUtils;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.framework.persistence.ojb.dao.PlatformAwareDaoBaseOjb;

import java.math.BigDecimal;
import java.util.Iterator;

/**
 * Created by vivekb on 16/7/14.
 */
public class OLEInvoiceDaoOjb extends PlatformAwareDaoBaseOjb {
    private static final Logger LOG = Logger.getLogger(OLEInvoiceDaoOjb.class);

    public BigDecimal getInvoiceTotal(Integer purapDocumentIdentifier,String itemTypeCode){
        BigDecimal debitResult = BigDecimal.ZERO;
        BigDecimal creditResult = BigDecimal.ZERO;
        Criteria criteria = new Criteria();
        criteria.addEqualTo("purapDocumentIdentifier", purapDocumentIdentifier);
        criteria.addEqualTo("debitItem","Y");
        if(itemTypeCode!=null){
            criteria.addEqualTo("itemTypeCode",itemTypeCode);
        }
        ReportQueryByCriteria query = QueryFactory.newReportQuery(OleInvoiceItem.class, criteria);
        query.setAttributes(new String[] { "sum(ITM_EXTND_PRC)" });
        Iterator results=  getPersistenceBrokerTemplate().getReportQueryIteratorByQuery(query);
        if (results.hasNext()) {
            debitResult = (BigDecimal) ((Object[]) TransactionalServiceUtils.retrieveFirstAndExhaustIterator(results))[0];
            LOG.info("debitResult" + debitResult);
            if(debitResult==null){
                debitResult = BigDecimal.ZERO;
            }
        }
        criteria = new Criteria();
        criteria.addEqualTo("purapDocumentIdentifier", purapDocumentIdentifier);
        criteria.addEqualTo("debitItem","N");
        if(itemTypeCode!=null){
            criteria.addEqualTo("itemTypeCode",itemTypeCode);
        }
        query = QueryFactory.newReportQuery(OleInvoiceItem.class, criteria);
        query.setAttributes(new String[] { "sum(ITM_EXTND_PRC)" });
        results=  getPersistenceBrokerTemplate().getReportQueryIteratorByQuery(query);
        if (results.hasNext()) {
            creditResult = (BigDecimal) ((Object[]) TransactionalServiceUtils.retrieveFirstAndExhaustIterator(results))[0];
            LOG.info("creditResult" + creditResult);
            if(creditResult==null){
                creditResult = BigDecimal.ZERO;
            }
        }
        return debitResult.subtract(creditResult).setScale(KualiDecimal.SCALE, KualiDecimal.ROUND_BEHAVIOR);
    }

    public BigDecimal getForeignInvoiceTotal(Integer purapDocumentIdentifier,String itemTypeCode){
        BigDecimal debitResult = BigDecimal.ZERO;
        BigDecimal creditResult = BigDecimal.ZERO;
        Criteria criteria = new Criteria();
        criteria.addEqualTo("purapDocumentIdentifier", purapDocumentIdentifier);
        criteria.addEqualTo("debitItem","Y");
        if(itemTypeCode!=null){
            criteria.addEqualTo("itemTypeCode",itemTypeCode);
        }
        ReportQueryByCriteria query = QueryFactory.newReportQuery(OleInvoiceItem.class, criteria);
        query.setAttributes(new String[] { "sum(ITM_INV_QTY * OLE_FOR_UNT_CST)" } );

        Iterator results=  getPersistenceBrokerTemplate().getReportQueryIteratorByQuery(query);
        if (results.hasNext()) {
            debitResult = (BigDecimal) ((Object[]) TransactionalServiceUtils.retrieveFirstAndExhaustIterator(results))[0];
            LOG.info("debitResult for foreign currency" + debitResult);
            if(debitResult==null){
                debitResult = BigDecimal.ZERO;
            }
        }

        criteria = new Criteria();
        criteria.addEqualTo("purapDocumentIdentifier", purapDocumentIdentifier);
        criteria.addEqualTo("debitItem","N");
        if(itemTypeCode!=null){
            criteria.addEqualTo("itemTypeCode",itemTypeCode);
        }
        query = QueryFactory.newReportQuery(OleInvoiceItem.class, criteria);
        query.setAttributes(new String[] { "sum(ITM_INV_QTY * OLE_FOR_UNT_CST)" });
        results=  getPersistenceBrokerTemplate().getReportQueryIteratorByQuery(query);
        if (results.hasNext()) {
            creditResult = (BigDecimal) ((Object[]) TransactionalServiceUtils.retrieveFirstAndExhaustIterator(results))[0];
            LOG.info("creditResult for foreign currency" + creditResult);
            if(creditResult==null){
                creditResult = BigDecimal.ZERO;
            }
        }

        return debitResult.subtract(creditResult).setScale(KualiDecimal.SCALE, KualiDecimal.ROUND_BEHAVIOR);
    }
}
