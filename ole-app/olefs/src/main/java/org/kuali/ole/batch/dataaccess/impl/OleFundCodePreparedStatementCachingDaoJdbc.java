/*
 * Copyright 2009 The Kuali Foundation
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
package org.kuali.ole.batch.dataaccess.impl;

import org.kuali.ole.batch.dataaccess.OleFundCodePreparedStatementCachingDao;
import org.kuali.ole.coa.businessobject.OleFundCode;
import org.kuali.ole.coa.businessobject.OleFundCodeAccountingLine;
import org.kuali.ole.docstore.common.util.DataSource;
import org.kuali.ole.sys.batch.dataaccess.impl.AbstractFundCodePreparedStatementCachingDaoJdbc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class OleFundCodePreparedStatementCachingDaoJdbc extends AbstractFundCodePreparedStatementCachingDaoJdbc implements OleFundCodePreparedStatementCachingDao {
    static final Map<String, String> sql = new HashMap<String, String>();
    private DataSource dataSource;
    private Connection connection = null;
    private PreparedStatement fundCodePreparedStatement = null;
    private PreparedStatement fundCodeAccountingLinesPreparedStatement = null;
    private static final Logger LOG = LoggerFactory.getLogger(AbstractFundCodePreparedStatementCachingDaoJdbc.class);
    static {
        sql.put(RETRIEVE_PREFIX + OleFundCode.class, "select FUND_CD_ID, CD, ROW_ACT_IND, OBJ_ID, VER_NBR from OLE_FUND_CD_T where FUND_CD_ID=?");
        sql.put(RETRIEVE_PREFIX + OleFundCode.class, "select FUND_CD_ID, CD, ROW_ACT_IND, OBJ_ID, VER_NBR from OLE_FUND_CD_T where OBJ_ID=?");
        sql.put(RETRIEVE_PREFIX + OleFundCodeAccountingLine.class, "select FUND_CD_ACCTG_LN_ID,FUND_CD_ID, CHART_CD, ACCT_NUM, SUB_ACCT_NUM, OBJECT_CD,SUB_OBJECT_CD,PROJECT_CD,ORG_REF_ID,PERCENTAGE,OBJ_ID,VER_NBR from OLE_FUND_CD_ACCTG_LN_T where FUND_CD_ID=? and FUND_CD_ACCTG_LN_ID=?");
        sql.put("insertFundCode", "insert into OLE_FUND_CD_T (FUND_CD_ID, CD, ROW_ACT_IND, OBJ_ID, VER_NBR) values (?,?,?,?,?)");
        sql.put("insertFundCodeAccountingLines" , "insert into OLE_FUND_CD_ACCTG_LN_T (FUND_CD_ACCTG_LN_ID,FUND_CD_ID, CHART_CD, ACCT_NUM, SUB_ACCT_NUM, OBJECT_CD,SUB_OBJECT_CD,PROJECT_CD,ORG_REF_ID,PERCENTAGE,OBJ_ID,VER_NBR) values (?,?,?,?,?,?,?,?,?,?,?,?)");

    }

    public void initialize() {
        preparedStatementCache = new HashMap<String, PreparedStatement>();

        try {
            connection = getConnection();
            for (String statementKey : getSql().keySet()) {
                preparedStatementCache.put(statementKey,connection.prepareStatement(getSql().get(statementKey)));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Caught exception preparing statements in CachingDaoJdbc initialize method", e);
        }
    }

    public void destroy() {
        try {
            for (PreparedStatement preparedStatement : preparedStatementCache.values()) {
                preparedStatement.close();
            }
            preparedStatementCache = null;
            if (connection != null) {
                connection.close();
            }
            if (fundCodePreparedStatement != null) {
                fundCodePreparedStatement.close();
                fundCodePreparedStatement = null;
            }
            if (fundCodeAccountingLinesPreparedStatement != null) {
                fundCodeAccountingLinesPreparedStatement.close();
                fundCodeAccountingLinesPreparedStatement = null;
            }
            connection = null;

        } catch (SQLException e) {
            throw new RuntimeException("Caught exception closing statements in CachingDaoJdbc destroy method", e);
        }
    }


    @Override
    protected Map<String, String> getSql() {
        return sql;
    }


    public DataSource getDataSource() {
        try {
            if(dataSource == null) {
            dataSource = DataSource.getInstance();
            }
        } catch (IOException e) {
            LOG.error("IOException : " + e);
        } catch (SQLException e) {
            LOG.error("SQLException : " + e);
        } catch (PropertyVetoException e) {
            LOG.error("PropertyVetoException : " + e);
        }

        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;

    }

    private Connection getConnection() throws SQLException {
       dataSource = this.getDataSource();
       return dataSource.getConnection();
    }

    public OleFundCode getOleFundCode(final OleFundCode oleFundCode) {
        return new RetrievingJdbcWrapper<OleFundCode>() {
            @Override
            protected void populateStatement(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, oleFundCode.getFundCodeId());
              }

            @Override
            protected OleFundCode extractResult(ResultSet resultSet) throws SQLException {
                OleFundCode oleFundCode = new OleFundCode();
                oleFundCode.setFundCodeId(resultSet.getString(1));
                return oleFundCode;
            }
        }.get(OleFundCode.class);
    }

    public OleFundCode getOleFundCodeByObjectId(final OleFundCode oleFundCode) {
        return new RetrievingJdbcWrapper<OleFundCode>() {
            @Override
            protected void populateStatement(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, oleFundCode.getObjectId());
            }

            @Override
            protected OleFundCode extractResult(ResultSet resultSet) throws SQLException {
                OleFundCode oleFundCode = new OleFundCode();
                oleFundCode.setFundCodeId(resultSet.getString(1));
                return oleFundCode;
            }
        }.get(OleFundCode.class);
    }


    public OleFundCodeAccountingLine getOleFundCodeAccountingLine(final OleFundCodeAccountingLine oleFundCodeAccountingLine) {
        return new RetrievingJdbcWrapper<OleFundCodeAccountingLine>() {
            @Override
            protected void populateStatement(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, oleFundCodeAccountingLine.getFundCodeId());
                preparedStatement.setString(2, oleFundCodeAccountingLine.getFundCodeAccountingLineId());
            }

            @Override
            protected OleFundCodeAccountingLine extractResult(ResultSet resultSet) throws SQLException {
                OleFundCodeAccountingLine oleFundCodeAccountingLine = new OleFundCodeAccountingLine();
                oleFundCodeAccountingLine.setFundCodeId(resultSet.getString(2));
                return oleFundCodeAccountingLine;
            }
        }.get(OleFundCodeAccountingLine.class);
    }


    public void insertOleFundCode(final OleFundCode oleFundCode) throws SQLException{

        fundCodePreparedStatement = connection.prepareStatement(getSql().get("insertFundCode"));
        fundCodePreparedStatement.setString(1, oleFundCode.getFundCodeId());
        fundCodePreparedStatement.setString(2, oleFundCode.getFundCode());
        if(oleFundCode.isActive() == Boolean.TRUE) {
            fundCodePreparedStatement.setString(3, "Y");
        }
        else if(oleFundCode.isActive() == Boolean.FALSE) {
            fundCodePreparedStatement.setString(3, "N");
        }
        fundCodePreparedStatement.setString(4, oleFundCode.getObjectId());
        fundCodePreparedStatement.setLong(5, oleFundCode.getVersionNumber());
        fundCodePreparedStatement.executeUpdate();
        connection.commit();
    }


    public void insertOleFundCodeAccountingLine(final OleFundCodeAccountingLine oleFundCodeAccountingLine) throws SQLException{
        fundCodeAccountingLinesPreparedStatement = connection.prepareStatement(getSql().get("insertFundCodeAccountingLines"));
        fundCodeAccountingLinesPreparedStatement.setString(1, oleFundCodeAccountingLine.getFundCodeAccountingLineId());
        fundCodeAccountingLinesPreparedStatement.setString(2, oleFundCodeAccountingLine.getFundCodeId());
        fundCodeAccountingLinesPreparedStatement.setString(3, oleFundCodeAccountingLine.getChartCode());
        fundCodeAccountingLinesPreparedStatement.setString(4, oleFundCodeAccountingLine.getAccountNumber());
        fundCodeAccountingLinesPreparedStatement.setString(5, oleFundCodeAccountingLine.getSubAccount());
        fundCodeAccountingLinesPreparedStatement.setString(6, oleFundCodeAccountingLine.getObjectCode());
        fundCodeAccountingLinesPreparedStatement.setString(7, oleFundCodeAccountingLine.getSubObject());
        fundCodeAccountingLinesPreparedStatement.setString(8, oleFundCodeAccountingLine.getProject());
        fundCodeAccountingLinesPreparedStatement.setString(9, oleFundCodeAccountingLine.getOrgRefId());
        fundCodeAccountingLinesPreparedStatement.setBigDecimal(10, oleFundCodeAccountingLine.getPercentage());
        fundCodeAccountingLinesPreparedStatement.setString(11, oleFundCodeAccountingLine.getObjectId());
        fundCodeAccountingLinesPreparedStatement.setLong(12, oleFundCodeAccountingLine.getVersionNumber());
        fundCodeAccountingLinesPreparedStatement.executeUpdate();
        connection.commit();
    }

}
