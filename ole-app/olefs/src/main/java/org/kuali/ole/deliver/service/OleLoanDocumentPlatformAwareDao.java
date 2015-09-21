package org.kuali.ole.deliver.service;

import org.kuali.ole.deliver.util.BulkUpdateDataObject;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.framework.persistence.jdbc.dao.PlatformAwareDaoBaseJdbc;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by sheiksalahudeenm on 8/11/15.
 */
public class OleLoanDocumentPlatformAwareDao extends PlatformAwareDaoBaseJdbc {


    private Config currentContextConfig;

    public int[] updateLoanDocument(List<BulkUpdateDataObject> bulkUpdateDataObjects) {
        List<String> updateQueries = getUpdateQueriesForDate(bulkUpdateDataObjects);
        String[] sqls = updateQueries.toArray(new String[updateQueries.size()]);
        return getJdbcTemplate().batchUpdate(sqls);

    }

    public List<String> getUpdateQueriesForDate(List<BulkUpdateDataObject> bulkUpdateDataObjects) {
        List<String> updateQueries = new ArrayList<>();
        for (Iterator<BulkUpdateDataObject> iterator = bulkUpdateDataObjects.iterator(); iterator.hasNext(); ) {
            BulkUpdateDataObject bulkUpdateDataObject = iterator.next();
            Map setClauseMap = bulkUpdateDataObject.getSetClauseMap();
            String columnName = (String) setClauseMap.keySet().iterator().next();
            Map whereClauseMap = bulkUpdateDataObject.getWhereClauseMap();
            String whereCondition = (String) whereClauseMap.keySet().iterator().next();
            String query = "update ole_dlvr_loan_t set " + columnName + " = "+ getColValue(setClauseMap, columnName) +
                    " where " + whereCondition + " = '" + whereClauseMap.get(whereCondition) + "'";
            updateQueries.add(query);
        }
        return updateQueries;
    }

    private Object getColValue(Map setClauseMap, String columnName) {
        if(columnName.contains("CURR_DUE_DT_TIME")){
            Timestamp timestamp = (Timestamp) setClauseMap.get(columnName);
            if (null != timestamp) {
                String dbVendor = getProperty("db.vendor");
                if (dbVendor.equals("mysql")){
                    String generateDateStringsForMySQL = DateFormatHelper.getInstance().generateDateStringsForMySQL(timestamp);
                    return "str_to_date (" + generateDateStringsForMySQL + ")";
                } else if  (dbVendor.equals("oracle")){
                    String generateDateStringsForOracle = DateFormatHelper.getInstance().generateDateStringsForOracle(timestamp);
                    return "to_date (" + generateDateStringsForOracle + ")";
                }
            }else{
                return "NULL";
            }
        }
        return setClauseMap.get(columnName);
    }

    protected String getProperty(String property) {
        if (null == currentContextConfig) {
            currentContextConfig = ConfigContext.getCurrentContextConfig();
        }
        return currentContextConfig.getProperty(property);
    }

    public void setCurrentContextConfig(Config currentContextConfig) {
        this.currentContextConfig = currentContextConfig;
    }
}
