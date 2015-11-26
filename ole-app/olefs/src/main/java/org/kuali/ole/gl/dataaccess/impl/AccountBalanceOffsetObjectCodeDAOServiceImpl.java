package org.kuali.ole.gl.dataaccess.impl;

import org.kuali.ole.gl.dataaccess.AccountBalanceOffsetObjectCodeDAOService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.rice.core.framework.persistence.jdbc.dao.PlatformAwareDaoBaseJdbc;

import java.util.List;
import java.util.Map;

/**
 * Created by premkb on 11/20/15.
 */
public class AccountBalanceOffsetObjectCodeDAOServiceImpl extends PlatformAwareDaoBaseJdbc implements AccountBalanceOffsetObjectCodeDAOService {

    public List<Map<String, Object>> getBalanceTable(Map fieldValues){
        String fiscalYear=fieldValues.get(OLEConstants.FISCAL_YEAR).toString();
        String accountNumber=fieldValues.get(OLEConstants.ACCOUNT_NUMBER).toString();
        String chartCode=fieldValues.get(OLEConstants.CHART_CODE).toString();
        String query = "SELECT UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,ACLN_ANNL_BAL_AMT FROM GL_BALANCE_T WHERE FIN_OBJECT_CD IN (SELECT FIN_OBJECT_CD FROM CA_OBJECT_CODE_T WHERE UNIV_FISCAL_YR='"+fiscalYear+"' AND FIN_COA_CD='"+chartCode+"' AND ACCOUNT_NBR='"+accountNumber+"' AND FIN_OBJ_TYP_CD='FB')";
        return getSimpleJdbcTemplate().queryForList(query);
    }

}
