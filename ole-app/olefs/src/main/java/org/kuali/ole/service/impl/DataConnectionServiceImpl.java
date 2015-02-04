package org.kuali.ole.service.impl;

import org.apache.log4j.Logger;
import org.kuali.ole.service.DataBaseConnectionService;
import org.kuali.rice.core.api.config.property.ConfigContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**

 */
public class DataConnectionServiceImpl implements DataBaseConnectionService {

    private static final Logger LOG = Logger.getLogger(DataConnectionServiceImpl.class);

    @Override
    public ResultSet getResults(String query) {
        ResultSet resultSet=null;
        try {
            PreparedStatement preparedStatement = null;
            Connection connection  = getConnection();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

           }catch (Exception ex){
              ex.printStackTrace();
           }
        return  resultSet;

    }
    public Connection getConnection() {
        Connection connection = null;
        try {
            String connectionUrl = ConfigContext.getCurrentContextConfig().getProperty("datasource.url");
            String userName = ConfigContext.getCurrentContextConfig().getProperty("datasource.username");
            String passWord = ConfigContext.getCurrentContextConfig().getProperty("datasource.password");
            String driverName = ConfigContext.getCurrentContextConfig().getProperty("jdbc.driver");
            Class.forName(driverName);
            connection = DriverManager.getConnection(connectionUrl, userName, passWord);
        } catch (Exception e) {

        }
        return connection;
    }
}
