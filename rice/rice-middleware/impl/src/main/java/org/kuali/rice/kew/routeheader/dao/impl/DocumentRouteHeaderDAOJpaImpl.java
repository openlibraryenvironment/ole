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
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.core.framework.persistence.jpa.criteria.Criteria;
import org.kuali.rice.core.framework.persistence.jpa.criteria.QueryByCriteria;
import org.kuali.rice.core.framework.persistence.platform.DatabasePlatform;
import org.kuali.rice.kew.actionitem.ActionItem;
import org.kuali.rice.kew.actionlist.service.ActionListService;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.action.ActionRequestStatus;
import org.kuali.rice.kew.docsearch.SearchableAttributeValue;
import org.kuali.rice.kew.api.exception.LockingException;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValueContent;
import org.kuali.rice.kew.routeheader.dao.DocumentRouteHeaderDAO;
import org.kuali.rice.kew.service.KEWServiceLocator;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class DocumentRouteHeaderDAOJpaImpl implements DocumentRouteHeaderDAO {

	@PersistenceContext(unitName="kew-unit")
	private EntityManager entityManager;
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DocumentRouteHeaderDAOJpaImpl.class);

    
    /**
	 * @return the entityManager
	 */
	public EntityManager getEntityManager() {
		return this.entityManager;
	}

	/**
	 * @param entityManager the entityManager to set
	 */
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void saveRouteHeader(DocumentRouteHeaderValue routeHeader) {   	
    	DocumentRouteHeaderValueContent documentContent = routeHeader.getDocumentContent();    	
//    	List<SearchableAttributeValue> searchableAttributes = routeHeader.getSearchableAttributeValues();
    	
    	if (routeHeader.getDocumentId() == null){
    		entityManager.persist(routeHeader);
    	} else {
    		OrmUtils.merge(entityManager, routeHeader);
    	}
        
        //Save document content (document content retrieved via a service call)
        documentContent.setDocumentId(routeHeader.getDocumentId());
        entityManager.merge(documentContent);
        
        /*
        //Save searchable attributes
        for (SearchableAttributeValue searchableAttributeValue:searchableAttributes){
        	searchableAttributeValue.setDocumentId(routeHeader.getDocumentId());
        	if (searchableAttributeValue.getSearchableAttributeValueId() == null){
        		entityManager.persist(searchableAttributeValue);
        	} else {
        		entityManager.merge(searchableAttributeValue);
        	}
        }
        */
    }

    public DocumentRouteHeaderValueContent getContent(String documentId) {
    	Query query = entityManager.createNamedQuery("DocumentRouteHeaderValueContent.FindByDocumentId");
    	query.setParameter("documentId", documentId);
        return (DocumentRouteHeaderValueContent)query.getSingleResult();
    }

    public void clearRouteHeaderSearchValues(String documentId) {
    	Collection<SearchableAttributeValue> searchableAttributeValues = findSearchableAttributeValues(documentId);
    	for (SearchableAttributeValue searchableAttributeValue:searchableAttributeValues){
    		entityManager.remove(searchableAttributeValue);
    	}
    }
   
    public Collection<SearchableAttributeValue> findSearchableAttributeValues(String documentId){
    	List<SearchableAttributeValue> searchableAttributeValues = new ArrayList<SearchableAttributeValue>();
    	
    	for (int i=1;i<=4; i++){
    		String namedQuery = "";
    		switch (i) {
				case 1: namedQuery = "SearchableAttributeFloatValue.FindByDocumentId"; break;
				case 2: namedQuery = "SearchableAttributeDateTimeValue.FindByDocumentId"; break;
				case 3: namedQuery = "SearchableAttributeLongValue.FindByDocumentId";break;
				case 4: namedQuery = "SearchableAttributeStringValue.FindByDocumentId"; break;
    		}
	    	Query query = entityManager.createNamedQuery(namedQuery);
	    	query.setParameter("documentId", documentId);   	
	    	searchableAttributeValues.addAll(query.getResultList());
    	}    	

    	return searchableAttributeValues;
    }

    public void lockRouteHeader(final String documentId, final boolean wait) {
    	String sql = getPlatform().getLockRouteHeaderQuerySQL(documentId, wait);
    	try{
	    	Query query = entityManager.createNativeQuery(sql);
	    	query.setParameter(1, documentId);
	    	query.getSingleResult();
    	} catch (Exception e){
    		//FIXME: Should this check for hibernate LockAcquisitionException
    		throw new LockingException("Could not aquire lock on document, documentId=" + documentId, e);
    	}
    }

    public DocumentRouteHeaderValue findRouteHeader(String documentId) {
    	return findRouteHeader(documentId, false);
    }

    public DocumentRouteHeaderValue findRouteHeader(String documentId, boolean clearCache) {
        Query query = entityManager.createNamedQuery("DocumentRouteHeaderValue.FindByDocumentId");
        query.setParameter("documentId", documentId);

        //TODO: What cache do we clear when using JPA
        if (clearCache) {
        	//this.getPersistenceBrokerTemplate().clearCache();
        }
        
	    DocumentRouteHeaderValue routeHeader = (DocumentRouteHeaderValue) query.getSingleResult(); 
//	    routeHeader.setSearchableAttributeValues(findSearchableAttributeValues(documentId));
	    return routeHeader;
    }

    public Collection<DocumentRouteHeaderValue> findRouteHeaders(Collection<String> documentIds) {
    	return findRouteHeaders(documentIds, false);
    }
    
    public Collection<DocumentRouteHeaderValue> findRouteHeaders(Collection<String> documentIds, boolean clearCache) {
    	if (documentIds == null || documentIds.isEmpty()) {
    		return null;
    	}
    	Criteria crit = new Criteria(DocumentRouteHeaderValue.class.getName());
    	crit.in("documentId", documentIds);
    	
    	//TODO: What cache do we clear when using JPA
        if (clearCache) {
        	//this.getPersistenceBrokerTemplate().clearCache();
        }
    	
    	return new QueryByCriteria(entityManager, crit).toQuery().getResultList();
    }
    
    public void deleteRouteHeader(DocumentRouteHeaderValue routeHeader) {
    	DocumentRouteHeaderValue attachedRouteHeader = findRouteHeader(routeHeader.getDocumentId());
    	entityManager.remove(attachedRouteHeader);
    }

    public String getNextDocumentId() {
    	Long nextDocumentId = getPlatform().getNextValSQL("KREW_DOC_HDR_S", entityManager);
        return nextDocumentId.toString();
    }
    
    protected DatabasePlatform getPlatform() {
    	return (DatabasePlatform) GlobalResourceLoader.getService(RiceConstants.DB_PLATFORM);
    }

    @Override
    public Collection<String> findPendingByResponsibilityIds(Set<String> responsibilityIds) {

        if (responsibilityIds.isEmpty()) {
            return new ArrayList();
        }

        String respIds = "('";
        int index = 0;
        for (String responsibilityId : responsibilityIds) {
            respIds += responsibilityId + (index == responsibilityIds.size()-1 ? "" : "','");
        }
        respIds += "')";

        String query = "SELECT DISTINCT(doc_hdr_id) FROM KREW_ACTN_RQST_T "+
        	"WHERE (STAT_CD='" +
        	ActionRequestStatus.INITIALIZED.getCode()+
        	"' OR STAT_CD='"+
        	ActionRequestStatus.ACTIVATED.getCode()+
        	"') AND RSP_ID IN "+respIds;

        LOG.debug("Query to find pending documents for requeue: " + query);
        
        List<String> idList = new ArrayList<String>();
        for (Object tempId : entityManager.createNativeQuery(query).getResultList()) {
        	idList.add(((String) tempId));
        }

        return idList; //(List<Long>)entityManager.createNativeQuery(query).getResultList();
    }

    public boolean hasSearchableAttributeValue(String documentId, String searchableAttributeKey, String searchableAttributeValue) {
    	return hasSearchableAttributeValue(documentId, searchableAttributeKey, searchableAttributeValue, "SearchableAttributeDateTimeValue.FindByKey")
    		|| hasSearchableAttributeValue(documentId, searchableAttributeKey, searchableAttributeValue, "SearchableAttributeStringValue.FindByKey")
    		|| hasSearchableAttributeValue(documentId, searchableAttributeKey, searchableAttributeValue, "SearchableAttributeLongValue.FindByKey")
    		|| hasSearchableAttributeValue(documentId, searchableAttributeKey, searchableAttributeValue, "SearchableAttributeFloatValue.FindByKey");
    }
    
    private boolean hasSearchableAttributeValue(String documentId, String searchableAttributeKey, String searchableAttributeValue, String namedQuery) {
    	Query query = entityManager.createNamedQuery(namedQuery);
        query.setParameter("documentId", documentId);
        query.setParameter("searchableAttributeKey", searchableAttributeKey);
        Collection results = query.getResultList();
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
    	
    	try {
            String sql = "SELECT DT.APPL_ID FROM KREW_DOC_TYP_T DT, KREW_DOC_HDR_T DH "+
            	"WHERE DH.DOC_TYP_ID=DT.DOC_TYP_ID AND "+
            	"DH.DOC_HDR_ID=?";
        	
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, documentId);
            
            applicationId = (String)query.getSingleResult();
    	} catch (EntityNotFoundException enfe) {
    		throw new WorkflowRuntimeException(enfe.getMessage());
		}
    	
    	return applicationId;
    }

    public String getDocumentStatus(String documentId) {
    	DocumentRouteHeaderValue document = findRouteHeader(documentId);

		return document.getDocRouteStatus();
    }
    
    public String getAppDocId(String documentId) {
    	Query query = entityManager.createNamedQuery("DocumentRouteHeaderValue.GetAppDocId");
        query.setParameter("documentId", documentId);
        return (String) query.getSingleResult(); 
 	 }

    public String getAppDocStatus(String documentId) {
        Query query = entityManager.createNamedQuery("DocumentRouteHeaderValue.GetAppDocStatus");
        query.setParameter("documentId", documentId);
        return (String) query.getSingleResult();
    }

    public void save(SearchableAttributeValue searchableAttributeValue) {   	
    	if (searchableAttributeValue.getSearchableAttributeValueId() == null){
    		entityManager.persist(searchableAttributeValue);
    	} else {
    		entityManager.merge(searchableAttributeValue);
    	}
    }

	public Collection findByDocTypeAndAppId(String documentTypeName,
			String appId) {
    	try {
            String sql = 
        	 	"SELECT DISTINCT " +
        		"    (docHdr.doc_hdr_id) " +
        		"FROM " +
        		"    KREW_DOC_HDR_T docHdr, " +
        		"    KREW_DOC_TYP_T docTyp " +
        		"WHERE " +
        		"    docHdr.APP_DOC_ID     = ? " +
        		"    AND docHdr.DOC_TYP_ID = docTyp.DOC_TYP_ID " +
        		"    AND docTyp.DOC_TYP_NM = ?";
        	
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, appId);
            query.setParameter(2, documentTypeName);
            Collection<Long> idCollection = new ArrayList<Long>();
            for (Object tempId : query.getResultList()) {
            	idCollection.add(((BigDecimal)tempId).longValueExact());
            }
            return idCollection;
    	} catch (EntityNotFoundException enfe) {
    		throw new WorkflowRuntimeException(enfe.getMessage());
		}
	}


}
