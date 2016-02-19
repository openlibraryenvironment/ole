package org.kuali.ole.gobi.dao;

import org.kuali.rice.core.framework.persistence.jdbc.dao.PlatformAwareDaoBaseJdbc;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GobiDAO extends PlatformAwareDaoBaseJdbc {
    public String getDocumentStatus(String reqId) {

        StringBuilder query = new StringBuilder();
        query.append("select doc_hdr_stat_cd from krew_doc_hdr_t where doc_hdr_id in (select fdoc_nbr from pur_reqs_t where REQS_ID = '")
                .append(reqId)
                .append("')");

        List<Map<String, Object>> maps = executeQuery(query.toString());
        for (Iterator<Map<String, Object>> iterator = maps.iterator(); iterator.hasNext(); ) {
            Map<String, Object> objectMap = iterator.next();
            String documentStatus = (String) objectMap.get("doc_hdr_stat_cd");
            return documentStatus;

        }

        return null;
    }

    public String getPOId(String reqId) {

        StringBuilder query = new StringBuilder();
        query.append("select PO_ID from PUR_PO_T where REQS_ID = '")
                .append(reqId)
                .append("'");

        List<Map<String, Object>> maps = executeQuery(query.toString());
        for (Iterator<Map<String, Object>> iterator = maps.iterator(); iterator.hasNext(); ) {
            Map<String, Object> objectMap = iterator.next();
            BigDecimal poId = (BigDecimal) objectMap.get("PO_ID");
            return poId.toString();

        }

        return null;
    }

    protected List<Map<String, Object>> executeQuery(String query) {
        return getSimpleJdbcTemplate().queryForList(query);
    }

}
