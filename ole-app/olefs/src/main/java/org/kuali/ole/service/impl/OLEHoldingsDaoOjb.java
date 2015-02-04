package org.kuali.ole.service.impl;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryFactory;
import org.kuali.ole.select.bo.OLEHoldingsRecord;
import org.kuali.ole.service.OLEHoldingsDao;
import org.kuali.rice.core.framework.persistence.ojb.dao.PlatformAwareDaoBaseOjb;

import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 4/11/14
 * Time: 12:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEHoldingsDaoOjb extends PlatformAwareDaoBaseOjb implements OLEHoldingsDao {

    @Override
    public List<OLEHoldingsRecord> getHoldingsByCollection(Collection bibIds) {
        Criteria criteria = new Criteria();
        criteria.addColumnIn("BIB_ID",bibIds);
        List<OLEHoldingsRecord> holdingsRecords = (List<OLEHoldingsRecord>) getPersistenceBrokerTemplate().getCollectionByQuery(QueryFactory.newQuery(OLEHoldingsRecord.class, criteria));
        return holdingsRecords;
    }
}
