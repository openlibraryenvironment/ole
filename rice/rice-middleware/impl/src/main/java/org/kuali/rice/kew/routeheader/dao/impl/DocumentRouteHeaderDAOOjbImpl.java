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
package org.kuali.rice.kew.routeheader.dao.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.ojb.broker.OptimisticLockException;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.accesslayer.LookupException;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.broker.query.QueryFactory;
import org.apache.ojb.broker.query.ReportQueryByCriteria;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.framework.persistence.platform.DatabasePlatform;
import org.kuali.rice.kew.actionitem.ActionItem;
import org.kuali.rice.kew.actionlist.service.ActionListService;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.action.ActionRequestStatus;
import org.kuali.rice.kew.api.exception.LockingException;
import org.kuali.rice.kew.docsearch.SearchableAttributeValue;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValueContent;
import org.kuali.rice.kew.routeheader.dao.DocumentRouteHeaderDAO;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.springframework.dao.CannotAcquireLockException;
import org.springmodules.orm.ojb.OjbFactoryUtils;
import org.springmodules.orm.ojb.PersistenceBrokerCallback;
import org.springmodules.orm.ojb.support.PersistenceBrokerDaoSupport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DocumentRouteHeaderDAOOjbImpl extends PersistenceBrokerDaoSupport implements DocumentRouteHeaderDAO {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DocumentRouteHeaderDAOOjbImpl.class);

    public void saveRouteHeader(DocumentRouteHeaderValue routeHeader) {
        if ( LOG.isDebugEnabled() ) {
            LOG.debug( "About to Save the route Header: " + routeHeader.getDocumentId() + " / version=" + routeHeader.getVersionNumber() );
            DocumentRouteHeaderValue currHeader = findRouteHeader(routeHeader.getDocumentId());
            if ( currHeader != null ) {
                LOG.debug( "Current Header Version: " + currHeader.getVersionNumber() );
//                for ( SearchableAttributeValue s : currHeader.get() ) {
//                    LOG.debug( "SA: " + s.getSearchableAttributeValueId() + " / version=" + s.get )
//                }
            } else {
                LOG.debug( "Current Header: null" );
            }
            LOG.debug( ExceptionUtils.getStackTrace(new Throwable()) );
        }
        try {
            getPersistenceBrokerTemplate().store(routeHeader);
            routeHeader.getDocumentContent().setDocumentId(routeHeader.getDocumentId());
            getPersistenceBrokerTemplate().store(routeHeader.getDocumentContent());
        } catch ( RuntimeException ex ) {
            if ( ex.getCause() instanceof OptimisticLockException ) {
                 LOG.error( "Optimistic Locking Exception saving document header or content. Offending object: " + ((OptimisticLockException)ex.getCause()).getSourceObject() 
                 + "; DocumentId = " + routeHeader.getDocumentId() + " ;  Version Number = " + routeHeader.getVersionNumber());
            }
            LOG.error( "Unable to save document header or content. Route Header: " + routeHeader, ex );
            throw ex;
        }
    }

    public DocumentRouteHeaderValueContent getContent(String documentId) {
    	Criteria crit = new Criteria();
        crit.addEqualTo("documentId", documentId);
        return (DocumentRouteHeaderValueContent)this.getPersistenceBrokerTemplate().getObjectByQuery(new QueryByCriteria(DocumentRouteHeaderValueContent.class, crit));
    }

    public void clearRouteHeaderSearchValues(String documentId) {
        Criteria crit = new Criteria();
        crit.addEqualTo("documentId", documentId);
        QueryByCriteria query = new QueryByCriteria(SearchableAttributeValue.class, crit);
        query.addOrderByAscending("searchableAttributeValueId");
        Collection<SearchableAttributeValue> results = this.getPersistenceBrokerTemplate().getCollectionByQuery(query);
        if (!results.isEmpty()) {
            for (SearchableAttributeValue srchAttrVal: results) {
                this.getPersistenceBrokerTemplate().delete(srchAttrVal);
            }
        }
    }

    public Collection<SearchableAttributeValue> findSearchableAttributeValues(String documentId) {
        Criteria crit = new Criteria();
        crit.addEqualTo("documentId", documentId);
        QueryByCriteria query = new QueryByCriteria(SearchableAttributeValue.class, crit);
        query.addOrderByAscending("searchableAttributeValueId");
        return this.getPersistenceBrokerTemplate().getCollectionByQuery(query);
    }

    public void lockRouteHeader(final String documentId, final boolean wait) {

        /*
         * String sql = (wait ? LOCK_SQL_WAIT : LOCK_SQL_NOWAIT); try { getJdbcTemplate().update(sql, new Object[] { documentId }); } catch (CannotAcquireLockException e) { throw new LockingException("Could not aquire lock on document, documentId=" + documentId, e); }
         */

    	this.getPersistenceBrokerTemplate().execute(new PersistenceBrokerCallback() {
            public Object doInPersistenceBroker(PersistenceBroker broker) {
                PreparedStatement statement = null;
                try {
                    Connection connection = broker.serviceConnectionManager().getConnection();
                    String sql = getPlatform().getLockRouteHeaderQuerySQL(documentId, wait);
                    statement = connection.prepareStatement(sql);
                    statement.setString(1, documentId);
                    statement.execute();
                    return null;
                } catch (SQLException e) {
                    throw new LockingException("Could not aquire lock on document, documentId=" + documentId, e);
                } catch (LookupException e) {
                    throw new LockingException("Could not aquire lock on document, documentId=" + documentId, e);
                } catch (CannotAcquireLockException e) {
                    throw new LockingException("Could not aquire lock on document, documentId=" + documentId, e);
                } finally {
                    if (statement != null) {
                        try {
                            statement.close();
                        } catch (SQLException e) {
                        }
                    }
                }
            }
        });

    }

    public DocumentRouteHeaderValue findRouteHeader(String documentId) {
    	return findRouteHeader(documentId, false);
    }

    public DocumentRouteHeaderValue findRouteHeader(String documentId, boolean clearCache) {
        Criteria crit = new Criteria();
        crit.addEqualTo("documentId", documentId);
        if (clearCache) {
        	this.getPersistenceBrokerTemplate().clearCache();
        }
        return (DocumentRouteHeaderValue) this.getPersistenceBrokerTemplate().getObjectByQuery(new QueryByCriteria(DocumentRouteHeaderValue.class, crit));
    }

    public Collection<DocumentRouteHeaderValue> findRouteHeaders(Collection<String> documentIds) {
    	return findRouteHeaders(documentIds, false);
    }
    
    public Collection<DocumentRouteHeaderValue> findRouteHeaders(Collection<String> documentIds, boolean clearCache) {
    	if (documentIds == null || documentIds.isEmpty()) {
    		return null;
    	}
    	Criteria crit = new Criteria();
    	crit.addIn("documentId", documentIds);
    	if (clearCache) {
        	this.getPersistenceBrokerTemplate().clearCache();
        }
    	return (Collection<DocumentRouteHeaderValue>) this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(DocumentRouteHeaderValue.class, crit));
    }

    public void deleteRouteHeader(DocumentRouteHeaderValue routeHeader) {
    	this.getPersistenceBrokerTemplate().delete(routeHeader);
    }

    public String getNextDocumentId() {
        return (String)this.getPersistenceBrokerTemplate().execute(new PersistenceBrokerCallback() {
            public Object doInPersistenceBroker(PersistenceBroker broker) {
            	return getPlatform().getNextValSQL("KREW_DOC_HDR_S", broker).toString();
                    }
        });
    }

    protected DatabasePlatform getPlatform() {
    	return (DatabasePlatform)GlobalResourceLoader.getService(RiceConstants.DB_PLATFORM);
    }

    public Collection<String> findPendingByResponsibilityIds(Set<String> responsibilityIds) {
        Collection<String> documentIds = new ArrayList();
        if (responsibilityIds.isEmpty()) {
            return documentIds;
        }
        PersistenceBroker broker = null;
        Connection conn = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            broker = getPersistenceBroker(false);
            conn = broker.serviceConnectionManager().getConnection();
            String respIds = "('";
            int index = 0;
            for (String responsibilityId : responsibilityIds) {
                respIds += responsibilityId + (index == responsibilityIds.size()-1 ? "" : "','");
                index++;
            }
            respIds += "')";
            String query = "SELECT DISTINCT(doc_hdr_id) FROM KREW_ACTN_RQST_T "+
            	"WHERE (STAT_CD='" +
            	ActionRequestStatus.INITIALIZED.getCode()+
            	"' OR STAT_CD='"+
            	ActionRequestStatus.ACTIVATED.getCode()+
            	"') AND RSP_ID IN "+respIds;
            LOG.debug("Query to find pending documents for requeue: " + query);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()) {
            	documentIds.add(rs.getString(1));
            }
        } catch (SQLException sqle) {
            LOG.error("SQLException: " + sqle.getMessage(), sqle);
            throw new WorkflowRuntimeException(sqle);
        } catch (LookupException le) {
            LOG.error("LookupException: " + le.getMessage(), le);
            throw new WorkflowRuntimeException(le);
        } finally {
        	if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    LOG.warn("Could not close result set.");
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    LOG.warn("Could not close statement.");
                }
            }
            try {
                if (broker != null) {
                    OjbFactoryUtils.releasePersistenceBroker(broker, this.getPersistenceBrokerTemplate().getPbKey());
                }
            } catch (Exception e) {
                LOG.error("Failed closing connection: " + e.getMessage(), e);
            }
        }
        return documentIds;
    }

    public boolean hasSearchableAttributeValue(String documentId, String searchableAttributeKey, String searchableAttributeValue) {
    	Criteria crit = new Criteria();
        crit.addEqualTo("documentId", documentId);
        crit.addEqualTo("searchableAttributeKey", searchableAttributeKey);
        Collection results = getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(SearchableAttributeValue.class, crit));
        if (!results.isEmpty()) {
            for (Iterator iterator = results.iterator(); iterator.hasNext();) {
                SearchableAttributeValue attribute = (SearchableAttributeValue) iterator.next();
                if (StringUtils.equals(attribute.getSearchableAttributeDisplayValue(), searchableAttributeValue)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getApplicationIdByDocumentId(String documentId) {
    	if (documentId == null) {
    		throw new IllegalArgumentException("Encountered a null document ID.");
    	}
    	String applicationId = null;
        PersistenceBroker broker = null;
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            broker = this.getPersistenceBroker(false);
            conn = broker.serviceConnectionManager().getConnection();
            String query = "SELECT DT.APPL_ID FROM KREW_DOC_TYP_T DT, KREW_DOC_HDR_T DH "+
            	"WHERE DH.DOC_TYP_ID=DT.DOC_TYP_ID AND "+
            	"DH.DOC_HDR_ID=?";
            statement = conn.prepareStatement(query);
            statement.setString(1, documentId);
            rs = statement.executeQuery();
            if (rs.next()) {
            	applicationId = rs.getString(1);
                if (rs.wasNull()) {
                	applicationId = null;
                }
            }
        } catch (SQLException sqle) {
            LOG.error("SQLException: " + sqle.getMessage(), sqle);
            throw new WorkflowRuntimeException(sqle);
        } catch (LookupException le) {
            LOG.error("LookupException: " + le.getMessage(), le);
            throw new WorkflowRuntimeException(le);
        } finally {
        	if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    LOG.warn("Could not close result set.");
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    LOG.warn("Could not close statement.");
                }
            }
            try {
                if (broker != null) {
                    OjbFactoryUtils.releasePersistenceBroker(broker, this.getPersistenceBrokerTemplate().getPbKey());
                }
            } catch (Exception e) {
                LOG.error("Failed closing connection: " + e.getMessage(), e);
            }
        }
        return applicationId;
    }

    public String getDocumentStatus(String documentId) {
	Criteria crit = new Criteria();
    	crit.addEqualTo("documentId", documentId);
    	ReportQueryByCriteria query = QueryFactory.newReportQuery(DocumentRouteHeaderValue.class, crit);
    	query.setAttributes(new String[] { "docRouteStatus" });
    	String status = null;
    	Iterator iter = getPersistenceBrokerTemplate().getReportQueryIteratorByQuery(query);
    	while (iter.hasNext()) {
    	    Object[] row = (Object[]) iter.next();
    	    status = (String)row[0];
    	}
    	return status;
    }
    
    public String getAppDocId(String documentId) {
 	 	Criteria crit = new Criteria();
 	 	crit.addEqualTo("documentId", documentId);
 	 	ReportQueryByCriteria query = QueryFactory.newReportQuery(DocumentRouteHeaderValue.class, crit);
 	 	query.setAttributes(new String[] { "appDocId" });
 	 	String appDocId = null;
 	 	Iterator iter = getPersistenceBrokerTemplate().getReportQueryIteratorByQuery(query);
 	 	while (iter.hasNext()) {
 	 		Object[] row = (Object[]) iter.next();
 	 		appDocId = (String)row[0];
 	 	}
 	 	return appDocId;
 	 }

    public String getAppDocStatus(String documentId) {
        Criteria crit = new Criteria();
        crit.addEqualTo("documentId", documentId);
        ReportQueryByCriteria query = QueryFactory.newReportQuery(DocumentRouteHeaderValue.class, crit);
        query.setAttributes(new String[] { "appDocStatus" });
        String appDocStatus = null;
        Iterator iter = getPersistenceBrokerTemplate().getReportQueryIteratorByQuery(query);
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            appDocStatus = (String)row[0];
        }
        return appDocStatus;
    }

    public void save(SearchableAttributeValue searchableAttributeValue) {
    	getPersistenceBrokerTemplate().store(searchableAttributeValue);
    }

	public Collection findByDocTypeAndAppId(String documentTypeName,
			String appId) {
        Collection documentIds = new ArrayList();

        PersistenceBroker broker = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            broker = getPersistenceBroker(false);
            conn = broker.serviceConnectionManager().getConnection();

            String query = 
            	 	"SELECT DISTINCT " +
            		"    (docHdr.doc_hdr_id) " +
            		"FROM " +
            		"    KREW_DOC_HDR_T docHdr, " +
            		"    KREW_DOC_TYP_T docTyp " +
            		"WHERE " +
            		"    docHdr.APP_DOC_ID     = ? " +
            		"    AND docHdr.DOC_TYP_ID = docTyp.DOC_TYP_ID " +
            		"    AND docTyp.DOC_TYP_NM = ?";
            
            LOG.debug("Query to find documents by app id: " + query);
            
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, appId);
            stmt.setString(2, documentTypeName);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
            	documentIds.add(new String(rs.getString(1)));
            }
            rs.close();
        } catch (SQLException sqle) {
            LOG.error("SQLException: " + sqle.getMessage(), sqle);
            throw new WorkflowRuntimeException(sqle);
        } catch (LookupException le) {
            LOG.error("LookupException: " + le.getMessage(), le);
            throw new WorkflowRuntimeException(le);
        } finally {
            try {
                if (broker != null) {
                    OjbFactoryUtils.releasePersistenceBroker(broker, this.getPersistenceBrokerTemplate().getPbKey());
                }
            } catch (Exception e) {
                LOG.error("Failed closing connection: " + e.getMessage(), e);
            }
        }
        return documentIds;
	}

}
