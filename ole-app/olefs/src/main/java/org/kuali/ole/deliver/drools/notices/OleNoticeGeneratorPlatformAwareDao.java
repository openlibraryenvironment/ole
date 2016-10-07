package org.kuali.ole.deliver.drools.notices;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.deliver.service.DateFormatHelper;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.framework.persistence.jdbc.dao.PlatformAwareDaoBaseJdbc;

import java.util.List;
import java.util.Map;

/**
 * Created by pvsubrah on 11/3/15.
 */
public class OleNoticeGeneratorPlatformAwareDao extends PlatformAwareDaoBaseJdbc {

    public List<Map<String, Object>> getLoanRecordsFromDBByDates(String fromDate, String toDate) {

        List<Map<String, Object>> queryForList = null;

        String query;

        String dbVendor = getProperty("db.vendor");
        if (dbVendor.equalsIgnoreCase("mysql")) {
            String fromDateString;
            String toDateString;

            String formattedFromDateForMySQL = null;
            String formattedToDateForMySQL = null;

            if (StringUtils.isNotBlank(fromDate)) {
                fromDateString = DateFormatHelper.getInstance().generateDateStringsForMySQL(fromDate);
                StringBuffer stringBuffer = new StringBuffer();

                formattedFromDateForMySQL = stringBuffer.append("str_to_date(")
                        .append(fromDateString)
                        .append(")")
                        .toString();
            }

            if (StringUtils.isNotBlank(toDate)) {
                StringBuffer stringBuffer = new StringBuffer();
                toDateString = DateFormatHelper.getInstance().generateDateStringsForMySQL(toDate);

                formattedToDateForMySQL = stringBuffer.append("str_to_date(")
                        .append(toDateString)
                        .append(")")
                        .toString();
            }

            if (null != formattedFromDateForMySQL && null != formattedToDateForMySQL) {
                query = "select * from ole_dlvr_loan_t where crte_dt_time >= " + formattedFromDateForMySQL + " and crte_dt_time <= " +
                        formattedToDateForMySQL + " and LOAN_TRAN_ID not in (select loan_id from ole_dlvr_loan_notice_t)";

            } else {
                query = "select * from ole_dlvr_loan_t where curr_due_dt_time is not null and itm_id not in (select barcode from ole_ds_item_t where item_status_Id = '14') and LOAN_TRAN_ID not in (select loan_id from ole_dlvr_loan_notice_t)";
            }

            queryForList = executeQuery(query);
            return queryForList;


        } else if (dbVendor.equalsIgnoreCase("oracle")) {
            return queryForList;
        }

        return null;
    }

    protected List<Map<String, Object>> executeQuery(String query) {
        return getSimpleJdbcTemplate().queryForList(query);
    }

    protected String getProperty(String property) {
        return ConfigContext.getCurrentContextConfig().getProperty(property);
    }





}
