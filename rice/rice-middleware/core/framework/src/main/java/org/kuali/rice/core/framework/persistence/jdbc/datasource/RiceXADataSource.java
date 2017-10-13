/**
 * Copyright 2005-2014 The Kuali Foundation
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
package org.kuali.rice.core.framework.persistence.jdbc.datasource;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.PoolingConnection;
import org.apache.commons.pool.KeyedObjectPool;
import org.apache.commons.pool.KeyedObjectPoolFactory;
import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.apache.commons.pool.impl.GenericKeyedObjectPoolFactory;
import org.enhydra.jdbc.standard.StandardXADataSource;
import org.enhydra.jdbc.standard.StandardXAStatefulConnection;
import org.springframework.util.Assert;

import javax.sql.DataSource;

/**
 * This is a description of what this class does - wliang don't forget to fill this in. 
 * 
 * Portions of this code were copied from Apache DBCP 1.2.1
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class RiceXADataSource extends StandardXADataSource {
	private KeyedObjectPoolFactory _stmtPoolFactory;
	private int preparedStatementCacheSize;

	public RiceXADataSource() {
		// the built-in XAPool prepared statement caching is broken, and it was enabled by calling this method with a non-zero value.
		// so we set it to 0, which effectively disables the built-in caching
		super.setPreparedStmtCacheSize(0);
	}
	
	@Override
	public synchronized Connection getConnection(String arg0, String arg1)
			throws SQLException {
		Connection conn = super.getConnection(arg0, arg1);
		
		// wrap the connection with another connection that can pool prepared statements
		if (getPreparedStatementCacheSize() > 0) {
			conn = wrapConnection(conn);
		}
		return conn;
	}

	@Override
	public synchronized StandardXAStatefulConnection getFreeConnection() throws SQLException {
		StandardXAStatefulConnection conn = super.getFreeConnection();
		if (getPreparedStatementCacheSize() > 0) {
			if (conn != null && !(conn.con instanceof PreparedStatementCachingConnection)) {
				conn.con = wrapConnection(conn.con);
			}
		}
		return conn;
	}
	
	public int getPreparedStatementCacheSize() {
		return preparedStatementCacheSize;
	}

	public void setPreparedStatementCacheSize(int preparedStmtCacheSize) {
		this.preparedStatementCacheSize = preparedStmtCacheSize;
	}

	/**
	 * This method calls {@link #setPreparedStatementCacheSize(int)} instead of setting this property.
	 * The reason 2 properties exist is because preparedStmtCacheSize is used by XAPool to enable PreparedStatement caching, but
	 * its implementation seems to cause max cursors to be exceeded under oracle.  Therefore, this class defines a new property, preparedStatementCacheSize,
	 * that will instead be used to turn on caching
	 * 
	 * @see org.enhydra.jdbc.standard.StandardConnectionPoolDataSource#setPreparedStmtCacheSize(int)
	 */
	@Override
	public void setPreparedStmtCacheSize(int preparedStmtCacheSize) {
		// the built-in XAPool prepared statement caching is broken, and it was enabled by calling this method with a non-zero value.
		// so we override it not to call the super's method and instead set the size of the cache that's implemented in this class
		setPreparedStatementCacheSize(preparedStmtCacheSize);
	}
	
	protected KeyedObjectPoolFactory createStatementPoolFactory() {
		return new GenericKeyedObjectPoolFactory(null, 
                -1, // unlimited maxActive (per key)
                GenericKeyedObjectPool.WHEN_EXHAUSTED_GROW, 
                0, // maxWait
                1, // maxIdle (per key) 
                getPreparedStatementCacheSize()); 
	}
	
	protected PreparedStatementCachingConnection wrapConnection(Connection realConnection) {
		// can't initialize the following variable in the constructor because the prepared statement cache size won't be available
		if (_stmtPoolFactory == null) {
			_stmtPoolFactory = createStatementPoolFactory();
		}
		
        KeyedObjectPool stmtpool = _stmtPoolFactory.createPool();
        PreparedStatementCachingConnection wrappedConnection = new PreparedStatementCachingConnection(realConnection, stmtpool);
        
        stmtpool.setFactory(wrappedConnection);
        return wrappedConnection;
	}

	public <T> T  unwrap(Class<T> iface) throws SQLException {
		Assert.notNull(iface, "Interface argument must not be null");
		if (!DataSource.class.equals(iface)) {
			throw new SQLException("DataSource of type [" + getClass().getName() +
					"] can only be unwrapped as [javax.sql.DataSource], not as [" + iface.getName());
		}
		return (T) this;
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return DataSource.class.equals(iface);
	}

    /**
     * A subclass of Apache DBCP's PoolingConnection class.  This class generates a unique
     * toString value for each instance of this class, because XApool relies on such behavior
     *
     * @author Kuali Rice Team (rice.collab@kuali.org)
     *
     */
    private static class PreparedStatementCachingConnection extends PoolingConnection {
        private String stringRepresentation;

        public PreparedStatementCachingConnection(Connection conn, KeyedObjectPool preparedStatementCache) {
            super(conn, preparedStatementCache);
            stringRepresentation = null;
        }

        @Override
        public String toString() {
            // for some reason, XAPool uses the connection's toString as a unique identifier for the connection
            // this method should provide that unique identifier
            if (stringRepresentation == null) {
                stringRepresentation = "PreparedStatementCachingConnection: " + System.identityHashCode(this);
            }
            return stringRepresentation;
        }
    }
}
