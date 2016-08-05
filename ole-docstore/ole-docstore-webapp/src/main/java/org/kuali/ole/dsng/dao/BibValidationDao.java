package org.kuali.ole.dsng.dao;

import org.kuali.rice.core.framework.persistence.jdbc.dao.PlatformAwareDaoBaseJdbc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 * Created by jayabharathreddy on 6/22/16.
 */
public class BibValidationDao extends PlatformAwareDaoBaseJdbc {
    private static final Logger LOG = LoggerFactory.getLogger(BibValidationDao.class);
    public boolean isHoldingAttachedToPo(String holdingsId) {
        boolean isAttached = false;
        SqlRowSet totalCountSet = getJdbcTemplate().queryForRowSet("SELECT COUNT(INSTANCE_UUID) as total FROM OLE_COPY_T where INSTANCE_UUID='who-" + holdingsId+"'");
        while (totalCountSet.next()) {
            int count = totalCountSet.getInt("total");
            if (count > 0) {
                isAttached = true;
            }
        }
        return isAttached;
    }

    public boolean isItemAttachedToPO(String itemId) {
        boolean isAttached = false;
        SqlRowSet totalCountSet = getJdbcTemplate().queryForRowSet("SELECT COUNT(ITEM_UUID) as total FROM OLE_COPY_T where ITEM_UUID='wio-" + itemId+"'");
        while (totalCountSet.next()) {
            int count = totalCountSet.getInt("total");
            if (count > 0) {
                isAttached = true;
            }
        }
        return isAttached;
    }

    public boolean isBibAttachedToPo(String bibId) {
        boolean isAttached = false;
        SqlRowSet totalCountSet = getJdbcTemplate().queryForRowSet("SELECT COUNT(BIB_UUID) as total FROM OLE_COPY_T where BIB_UUID='wbm-" + bibId+"'");
        while (totalCountSet.next()) {
            int count = totalCountSet.getInt("total");
            if (count > 0) {
                isAttached = true;
            }
        }
        return isAttached;
    }
}
