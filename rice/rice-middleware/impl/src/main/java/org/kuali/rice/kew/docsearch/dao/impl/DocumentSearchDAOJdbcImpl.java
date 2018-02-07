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
package org.kuali.rice.kew.docsearch.dao.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.api.document.search.DocumentSearchResults;
import org.kuali.rice.kew.impl.document.search.DocumentSearchGenerator;
import org.kuali.rice.kew.docsearch.dao.DocumentSearchDAO;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.util.PerformanceLogger;
import org.kuali.rice.krad.util.KRADConstants;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Spring JdbcTemplate implementation of DocumentSearchDAO
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class DocumentSearchDAOJdbcImpl implements DocumentSearchDAO {

    public static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DocumentSearchDAOJdbcImpl.class);
    private static final int DEFAULT_FETCH_MORE_ITERATION_LIMIT = 10;
    
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = new TransactionAwareDataSourceProxy(dataSource);
    }

    @Override
    public DocumentSearchResults.Builder findDocuments(final DocumentSearchGenerator documentSearchGenerator, final DocumentSearchCriteria criteria, final boolean criteriaModified, final List<RemotableAttributeField> searchFields) {
        final int maxResultCap = getMaxResultCap(criteria);
        try {
            final JdbcTemplate template = new JdbcTemplate(dataSource);

            return template.execute(new ConnectionCallback<DocumentSearchResults.Builder>() {
                @Override
                public DocumentSearchResults.Builder doInConnection(final Connection con) throws SQLException {
                    final Statement statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    try {
                        final int fetchIterationLimit = getFetchMoreIterationLimit();
                        final int fetchLimit = fetchIterationLimit * maxResultCap;
                        statement.setFetchSize(maxResultCap + 1);
                        statement.setMaxRows(fetchLimit + 1);

                        PerformanceLogger perfLog = new PerformanceLogger();
                        String sql = documentSearchGenerator.generateSearchSql(criteria, searchFields);
                        perfLog.log("Time to generate search sql from documentSearchGenerator class: " + documentSearchGenerator
                                .getClass().getName(), true);
                        LOG.info("Executing document search with statement max rows: " + statement.getMaxRows());
                        LOG.info("Executing document search with statement fetch size: " + statement.getFetchSize());
                        perfLog = new PerformanceLogger();
                        final ResultSet rs = statement.executeQuery(sql);
                        try {
                            perfLog.log("Time to execute doc search database query.", true);
                            final Statement searchAttributeStatement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                            try {
                           		return documentSearchGenerator.processResultSet(criteria, criteriaModified, searchAttributeStatement, rs, maxResultCap, fetchLimit);
                            } finally {
                                try {
                                    searchAttributeStatement.close();
                                } catch (SQLException e) {
                                    LOG.warn("Could not close search attribute statement.");
                                }
                            }
                        } finally {
                            try {
                                rs.close();
                            } catch (SQLException e) {
                                LOG.warn("Could not close result set.");
                            }
                        }
                    } finally {
                        try {
                            statement.close();
                        } catch (SQLException e) {
                            LOG.warn("Could not close statement.");
                        }
                    }
                }
            });

        } catch (DataAccessException dae) {
            String errorMsg = "DataAccessException: " + dae.getMessage();
            LOG.error("getList() " + errorMsg, dae);
            throw new RuntimeException(errorMsg, dae);
        } catch (Exception e) {
            String errorMsg = "LookupException: " + e.getMessage();
            LOG.error("getList() " + errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }

    /**
     * Returns the maximum number of results that should be returned from the document search.
     *
     * @param criteria the criteria in which to check for a max results value
     * @return the maximum number of results that should be returned from a document search
     */
    public int getMaxResultCap(DocumentSearchCriteria criteria) {
        int systemLimit = KewApiConstants.DOCUMENT_LOOKUP_DEFAULT_RESULT_CAP;
        String resultCapValue = CoreFrameworkServiceLocator.getParameterService().getParameterValueAsString(KewApiConstants.KEW_NAMESPACE, KRADConstants.DetailTypes.DOCUMENT_SEARCH_DETAIL_TYPE, KewApiConstants.DOC_SEARCH_RESULT_CAP);
        if (StringUtils.isNotBlank(resultCapValue)) {
            try {
                int configuredLimit = Integer.parseInt(resultCapValue);
                if (configuredLimit <= 0) {
                    LOG.warn(KewApiConstants.DOC_SEARCH_RESULT_CAP + " was less than or equal to zero.  Please use a positive integer.");
                } else {
                    systemLimit = configuredLimit;
                }
            } catch (NumberFormatException e) {
                LOG.warn(KewApiConstants.DOC_SEARCH_RESULT_CAP + " is not a valid number.  Value was " + resultCapValue + ".  Using default: " + KewApiConstants.DOCUMENT_LOOKUP_DEFAULT_RESULT_CAP);
            }
        }
        int maxResults = systemLimit;
        if (criteria.getMaxResults() != null) {
            int criteriaLimit = criteria.getMaxResults().intValue();
            if (criteriaLimit > systemLimit) {
                LOG.warn("Result set cap of " + criteriaLimit + " is greater than system value of " + systemLimit);
            } else {
                if (criteriaLimit < 0) {
                    LOG.warn("Criteria results limit was less than zero.");
                    criteriaLimit = 0;
                }
                maxResults = criteriaLimit;
            }
        }
        return maxResults;
    }

    public int getFetchMoreIterationLimit() {
        int fetchMoreLimit = DEFAULT_FETCH_MORE_ITERATION_LIMIT;
        String fetchMoreLimitValue = CoreFrameworkServiceLocator.getParameterService().getParameterValueAsString(KewApiConstants.KEW_NAMESPACE, KRADConstants.DetailTypes.DOCUMENT_SEARCH_DETAIL_TYPE, KewApiConstants.DOC_SEARCH_FETCH_MORE_ITERATION_LIMIT);
        if (!StringUtils.isBlank(fetchMoreLimitValue)) {
            try {
                fetchMoreLimit = Integer.parseInt(fetchMoreLimitValue);
                if (fetchMoreLimit < 0) {
                    LOG.warn(KewApiConstants.DOC_SEARCH_FETCH_MORE_ITERATION_LIMIT + " was less than zero.  Please use a value greater than or equal to zero.");
                    fetchMoreLimit = DEFAULT_FETCH_MORE_ITERATION_LIMIT;
                }
            } catch (NumberFormatException e) {
                LOG.warn(KewApiConstants.DOC_SEARCH_FETCH_MORE_ITERATION_LIMIT + " is not a valid number.  Value was " + fetchMoreLimitValue);
            }
        }
        return fetchMoreLimit;
    }

}
