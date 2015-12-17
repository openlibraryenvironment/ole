package org.kuali.ole.docstore.common.util;

import org.apache.commons.lang.StringUtils;

import org.kuali.ole.utility.callnumber.CallNumberFactory;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jayabharathreddy on 12/16/15.
 */
public class CallNumberMigration {

    private static final Logger LOG = LoggerFactory.getLogger(CallNumberMigration.class);

    private final static String dbVendor = ConfigContext.getCurrentContextConfig().getProperty("db.vendor");
    private static Map<String, String> callNumberType = new HashMap<>();
    PreparedStatement holdingsCallNumberPreparedStatement = null;
    PreparedStatement itemCallNumberPreparedStatement = null;
    private Connection connection = null;
    private Connection updateConnection = null;
    private Connection itemConnection = null;
    private Statement holdingsCallNumberStatement = null;
    private ResultSet holdingsCallNumberResultSet = null;
    private Statement itemCallNumberStatement = null;
    private ResultSet itemCallNumberResultSet = null;


    private String holdingsCallNumberQuery = "SELECT HOLDINGS_ID,CALL_NUMBER_TYPE_ID,CALL_NUMBER  FROM OLE_DS_HOLDINGS_T Where CALL_NUMBER  !='null' ORDER BY HOLDINGS_ID";
    private String itemCallNumberQuery = "SELECT ITEM_ID,CALL_NUMBER_TYPE_ID,CALL_NUMBER  FROM OLE_DS_ITEM_T Where CALL_NUMBER  !='null' ORDER BY ITEM_ID";


    public void init() throws SQLException {
        if (itemConnection == null || itemConnection.isClosed()) {
            itemConnection = getConnection();
        }
        if (updateConnection == null || updateConnection.isClosed()) {
            updateConnection = getConnection();
        }
        if (connection == null || connection.isClosed()) {
            connection = getConnection();
        }
        fetchCallNumberType();

    }

    public void getItemDetails() throws SQLException {

        itemCallNumberStatement = itemConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        if (dbVendor.equalsIgnoreCase("oracle")) {
            itemCallNumberStatement.setFetchSize(1);
        } else if (dbVendor.equalsIgnoreCase("mysql")) {
            itemCallNumberStatement.setFetchSize(Integer.MIN_VALUE);
        }

        itemCallNumberResultSet = itemCallNumberStatement.executeQuery(itemCallNumberQuery);

    }

    public void getHoldingsDetails() throws SQLException {
        holdingsCallNumberStatement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        if (dbVendor.equalsIgnoreCase("oracle")) {
            holdingsCallNumberStatement.setFetchSize(1);
        } else if (dbVendor.equalsIgnoreCase("mysql")) {
            holdingsCallNumberStatement.setFetchSize(Integer.MIN_VALUE);
        }
        holdingsCallNumberResultSet = holdingsCallNumberStatement.executeQuery(holdingsCallNumberQuery);

    }


    public void calculateAndUpdateHoldingsShelvingOrder() throws Exception {
        String callNumberTypeId = null;
        String callNumber = null;
        int holdingsId = 0;
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int count = 0;
        while (holdingsCallNumberResultSet.next()) {
            count++;
            String holdingsQuery = "UPDATE OLE_DS_HOLDINGS_T SET SHELVING_ORDER=?  WHERE HOLDINGS_ID=?";
            holdingsCallNumberPreparedStatement = updateConnection.prepareStatement(holdingsQuery);
            callNumberTypeId = holdingsCallNumberResultSet.getString("CALL_NUMBER_TYPE_ID");
            callNumber = holdingsCallNumberResultSet.getString("CALL_NUMBER");
            holdingsId = holdingsCallNumberResultSet.getInt("HOLDINGS_ID");
            if (StringUtils.isNotEmpty(callNumberTypeId) && StringUtils.isNotEmpty(callNumber)) {
                String callNumberCode = callNumberType.get(callNumberTypeId);
                holdingsCallNumberPreparedStatement.setString(1, getShelfKey(callNumber, callNumberCode));
                holdingsCallNumberPreparedStatement.setInt(2, holdingsId);
                try {
                    holdingsCallNumberPreparedStatement.executeUpdate();
                } catch (Exception e1) {
                    LOG.error("Exception while updating into OLE_DS_HOLDINGS_T, Holdings Id = " + holdingsId + " callNumber = " + callNumber + " : ", e1);

                }
            }
        }
        updateConnection.commit();
        stopWatch.stop();
        LOG.debug("Total Time taken " + count + " - for Holdings ::" + stopWatch.getTotalTimeMillis());
    }

    public void calculateAndUpdateItemShelvingOrder() throws Exception {
        String callNumberTypeId = null;
        String callNumber = null;
        int itemId = 0;
        int count = 0;
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        while (itemCallNumberResultSet.next()) {
            count++;
            String itemQuery = "UPDATE OLE_DS_ITEM_T SET SHELVING_ORDER=?  WHERE ITEM_ID=?";
            itemCallNumberPreparedStatement = updateConnection.prepareStatement(itemQuery);
            callNumberTypeId = itemCallNumberResultSet.getString("CALL_NUMBER_TYPE_ID");
            callNumber = itemCallNumberResultSet.getString("CALL_NUMBER");
            itemId = itemCallNumberResultSet.getInt("ITEM_ID");
            if (StringUtils.isNotEmpty(callNumberTypeId) && StringUtils.isNotEmpty(callNumber)) {
                String callNumberCode = callNumberType.get(callNumberTypeId);
                String shelfKey = getShelfKey(callNumber, callNumberCode);

                itemCallNumberPreparedStatement.setString(1, shelfKey);
                itemCallNumberPreparedStatement.setInt(2, itemId);
                try {
                    itemCallNumberPreparedStatement.executeUpdate();
                } catch (Exception e1) {
                    LOG.error("Exception while updating into OLE_DS_ITEM_T, Item Id = " + itemId + " callNumber = " + callNumber + " : ", e1);

                }
            }
        }
        updateConnection.commit();
        stopWatch.stop();
        LOG.debug("Total Time taken "+count+" - for Item ::" +stopWatch.getTotalTimeMillis());
    }


    private void fetchCallNumberType() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT SHVLG_SCHM_ID,SHVLG_SCHM_CD from OLE_CAT_SHVLG_SCHM_T");
        while (resultSet.next()) {
            callNumberType.put(resultSet.getString("SHVLG_SCHM_ID"), resultSet.getString("SHVLG_SCHM_CD"));
        }
        resultSet.close();
    }


    protected String getShelfKey(String callNumber, String codeValue) {
        String Shelfkey = null;
        if (StringUtils.isNotEmpty(callNumber) && StringUtils.isNotEmpty(codeValue)) {
            org.solrmarc.callnum.CallNumber callNumberObj = CallNumberFactory.getInstance().getCallNumber(codeValue);
            if (callNumberObj != null) {
                callNumberObj.parse(callNumber);
                Shelfkey = callNumberObj.getShelfKey();
            }
        }
        return Shelfkey;
    }


    private Connection getConnection() throws SQLException {
        DataSource dataSource = null;
        try {
            dataSource = DataSource.getInstance();
        } catch (IOException e) {
            LOG.error("IOException : " + e);
        } catch (SQLException e) {
            LOG.error("SQLException : " + e);
        } catch (PropertyVetoException e) {
            LOG.error("PropertyVetoException : " + e);
        }
        return dataSource.getConnection();
    }


    public void closeConnections() throws SQLException {
        if (holdingsCallNumberPreparedStatement != null) {
            holdingsCallNumberPreparedStatement.close();
        }

        if (itemCallNumberPreparedStatement != null) {
            itemCallNumberPreparedStatement.close();
        }
        if (connection != null) {
            connection.close();
        }
        if (itemConnection != null) {
            itemConnection.close();
        }

        if (updateConnection != null) {
            updateConnection.close();
        }
    }

}