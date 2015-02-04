package org.kuali.ole.service;

import java.sql.Connection;
import java.sql.ResultSet;

/**
 * Created with IntelliJ IDEA.
 * User: srinivasane
 * Date: 4/10/14
 * Time: 3:06 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DataBaseConnectionService {
       public ResultSet getResults(String query);
       public Connection getConnection();
}
