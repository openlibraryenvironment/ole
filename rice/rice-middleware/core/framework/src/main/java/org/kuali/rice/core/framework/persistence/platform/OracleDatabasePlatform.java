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
package org.kuali.rice.core.framework.persistence.platform;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.accesslayer.LookupException;
import org.apache.ojb.broker.query.Criteria;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.RiceConstants;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * DatabasePlatform implementation that generates Oracle-compliant SQL
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class OracleDatabasePlatform extends ANSISqlDatabasePlatform {

	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OracleDatabasePlatform.class);
	private static final long DEFAULT_TIMEOUT_SECONDS = 60 * 60; // default to 1 hour
	public static final long WAIT_FOREVER = -1;
	
	private static final Pattern APOS_PAT = Pattern.compile("'");
	    
    public void applyLimit(Integer limit, Criteria criteria) {
        if (limit != null) {
            criteria.addSql("rownum <= " + limit.intValue());
        }
    }  
    
    public String getStrToDateFunction() {
        return "TO_DATE";
    }
    
    public String getCurTimeFunction() {
        return "sysdate";
    }
    
    public String getDateFormatString(String dateFormatString) {
        return "'" + dateFormatString + "'";
    }
    
    @SuppressWarnings("unchecked")
    public Long getNextValSQL(String sequenceName,  EntityManager entityManager) {
        List resultList = entityManager.createNativeQuery("select " + sequenceName + ".nextval from dual").getResultList();
        return new Long(((BigDecimal)resultList.get(0)).longValue());
//        return new Long(((BigDecimal) entityManager.createNativeQuery("select " + sequenceName + ".nextval from dual").getSingleResult()).longValue());
    }
    
	public Long getNextValSQL(String sequenceName,	PersistenceBroker persistenceBroker) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			Connection connection = persistenceBroker.serviceConnectionManager().getConnection();
			statement = connection.prepareStatement("select " + sequenceName + ".nextval from dual");
			resultSet = statement.executeQuery();

			if (!resultSet.next()) {
				throw new RuntimeException("Error retrieving next option id for action list from sequence.");
			}
			return new Long(resultSet.getLong(1));
		} catch (SQLException e) {
			throw new RuntimeException("Error retrieving next option id for action list from sequence.", e);
		} catch (LookupException e) {
			throw new RuntimeException("Error retrieving next option id for action list from sequence.", e);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
				}
			}
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
				}
			}
		}
	}

    public String getLockRouteHeaderQuerySQL(String documentId, boolean wait) {
    	long timeoutSeconds = getTimeoutSeconds();
    	String waitClause = "";
    	if (!wait) {
    		waitClause = " NOWAIT";
    	} else if (wait && timeoutSeconds > 0) {
    		waitClause = " WAIT " + timeoutSeconds;
    	}
        return "SELECT DOC_HDR_ID FROM KREW_DOC_HDR_T WHERE DOC_HDR_ID=? FOR UPDATE" + waitClause;
    }

    public String toString() {
        return "[OracleDatabasePlatform]";
    }

    protected long getTimeoutSeconds() {
    	String timeoutValue = ConfigContext.getCurrentContextConfig().getDocumentLockTimeout();
    	if (timeoutValue != null) {
    		try {
    			return Long.parseLong(timeoutValue);
    		} catch (NumberFormatException e) {
    			LOG.warn("Failed to parse document lock timeout as it was not a valid number: " + timeoutValue);
    		}
    	}
    	return DEFAULT_TIMEOUT_SECONDS;
    }
    
    /**
     * @see org.kuali.rice.ken.DatabasePlatform.Platform#getSelectForUpdateSuffix(long)
     */
    public String getSelectForUpdateSuffix(long waitMillis) {
        String sql = "for update";
        if (WAIT_FOREVER == waitMillis) {
            // do nothing
            LOG.warn("Selecting for update and waiting forever...");
        } else if (RiceConstants.NO_WAIT == waitMillis) {
            sql += " nowait";
        } else {
            // Oracle only supports wait time in seconds...
            long seconds = waitMillis / 1000;
            if (seconds == 0) seconds = 1;
            sql += " wait " + seconds;
        }
        return sql;
    }
    
    /**
     * Performs Oracle-specific escaping of String parameters.
     * 
     * @see DatabasePlatform#escapeString(java.lang.String)
     */
    public String escapeString(String sqlString) {
    	return (sqlString != null) ? APOS_PAT.matcher(sqlString).replaceAll("''") : null;
    }

    /**
     * Converts date-only values into JDBC d{...} date literals, but converts date-and-time values into timestamp
     * strings that are then converted into date values via the strToDateFunction.
     * 
     * @see ANSISqlDatabasePlatform#getDateSQL(java.lang.String, java.lang.String)
     */
	@Override
	public String getDateSQL(String date, String time) {
		String d = date.replace('/', '-');
        if (time == null) {
            return new StringBuilder("{d '").append(d).append("'}").toString();
        } else {
            return new StringBuilder(getStrToDateFunction()).append("('").append(d).append(" ").append(
            		time).append("', 'YYYY-MM-DD HH24:MI:SS')").toString(); 
        }
	}
}
