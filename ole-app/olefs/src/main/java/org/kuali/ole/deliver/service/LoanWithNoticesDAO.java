package org.kuali.ole.deliver.service;

import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.framework.persistence.jdbc.dao.PlatformAwareDaoBaseJdbc;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.*;

/**
 * Created by pvsubrah on 3/31/15.
 */
public class LoanWithNoticesDAO extends PlatformAwareDaoBaseJdbc {

    private DateTimeService dateTimeService;

    public List<String> getLoanIdsForOverudeNotices(String noticeToSendDate,String noticeType) {
        String date = null;
        String query = null;
        String dbVendor = getProperty("db.vendor");
        if (dbVendor.equals("mysql")) {
            StringBuffer stringBuffer = new StringBuffer();
            String formattedDateForMySQL = null == noticeToSendDate ? "curdate()" : stringBuffer
                    .append("str_to_date(")
                    .append(formatDateForMySQL(noticeToSendDate))
                    .append(")")
                    .toString();
            query = "select loan_tran_id from ole_dlvr_loan_t where loan_tran_id in (select loan_id from " +
                    "ole_dlvr_loan_notice_t where ntc_typ='"+noticeType+"' and ntc_to_snd_dt < " +
                    formattedDateForMySQL + ")";
        } else {
            StringBuffer stringBuffer = new StringBuffer();
            String formatDateForOracle = null == noticeToSendDate ? "sysdate" : stringBuffer.append("'")
            .append(formatDateForOracle
                    (noticeToSendDate))
                    .append("'")
                    .toString();
            query = "select loan_tran_id from ole_dlvr_loan_t where loan_tran_id in (select loan_id from ole_dlvr_loan_notice_t where ntc_typ='"+noticeType+"' and ntc_to_snd_dt < " + formatDateForOracle + ")";
        }
        List loanIds = new ArrayList();
        List<Map<String, Object>> queryForList = executeQuery(query);
        for (Iterator<Map<String, Object>> iterator = queryForList.iterator(); iterator.hasNext(); ) {
            Map<String, Object> next = iterator.next();
            Object loan_id = next.get("loan_tran_id");
            String loanId = loan_id.toString();
            loanIds.add(loanId);
        }
        return loanIds;
    }

    protected List<Map<String, Object>> executeQuery(String query) {
        return getSimpleJdbcTemplate().queryForList(query);
    }

    protected String getProperty(String property) {
        return ConfigContext.getCurrentContextConfig().getProperty(property);
    }

    private String formatDateForOracle(String overdueNoticeToDate) {
        String forOracle = DateFormatHelper.getInstance().generateDateStringsForOracle(overdueNoticeToDate);
        return forOracle;
    }

    private String formatDateForMySQL(String overdueNoticeToDate) {
        String forMySQL = DateFormatHelper.getInstance().generateDateStringsForMySQL(overdueNoticeToDate);
        return forMySQL;
    }


    public void setDateTimeService(DateTimeService dateTimeService) {
        this.dateTimeService = dateTimeService;
    }

    public DateTimeService getDateTimeService() {
        return dateTimeService;
    }
}
