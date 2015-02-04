package org.kuali.ole.docstore.common.util;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.kuali.rice.core.api.config.property.ConfigContext;


/**
 * Created with IntelliJ IDEA.
 * User: jayabharathreddy
 * Date: 6/4/14
 * Time: 3:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataSource {
    private static DataSource datasource;
    private BasicDataSource ds;

    private DataSource() throws IOException, SQLException, PropertyVetoException {

        String dbVendor = ConfigContext.getCurrentContextConfig().getProperty("db.vendor");
        String connectionUrl = ConfigContext.getCurrentContextConfig().getProperty("jdbc.url");
        String userName = ConfigContext.getCurrentContextConfig().getProperty("jdbc.username");
        String passWord = ConfigContext.getCurrentContextConfig().getProperty("jdbc.password");
        String driverName = ConfigContext.getCurrentContextConfig().getProperty("jdbc.driver");

        ds = new BasicDataSource();
        ds.setDriverClassName(driverName);
        ds.setUsername(userName);
        ds.setPassword(passWord);
        ds.setUrl(connectionUrl);

        // the settings below are optional -- dbcp can work with defaults
        ds.setMinIdle(5);
        ds.setMaxIdle(20);
        ds.setMaxOpenPreparedStatements(180);
        ds.setMinEvictableIdleTimeMillis(180000000);
        ds.setTimeBetweenEvictionRunsMillis(180000000);
        ds.setNumTestsPerEvictionRun(3);
        ds.setTestOnBorrow(true);
        ds.setTestWhileIdle(true);
        if(dbVendor.equalsIgnoreCase("oracle")){
            ds.setValidationQuery("SELECT 1 from dual");
        }else if(dbVendor.equalsIgnoreCase("mysql")){
            ds.setValidationQuery("SELECT 1");
        }
        ds.setDefaultAutoCommit(false);
    }

    public static DataSource getInstance() throws IOException, SQLException, PropertyVetoException {
        if (datasource == null) {
            datasource = new DataSource();
            return datasource;
        } else {
            return datasource;
        }
    }

    public Connection getConnection() throws SQLException {
        return this.ds.getConnection();
    }


}
