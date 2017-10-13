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
package org.kuali.rice.kew.actionrequest.dao.impl;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actionrequest.dao.ActionRequestDAO;
import org.kuali.rice.kew.api.action.ActionRequestStatus;
import org.kuali.rice.kew.api.action.RecipientType;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.group.Group;

/**
 * This is a description of what this class does - sgibson don't forget to fill this in.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ActionRequestDAOJpaImpl implements ActionRequestDAO {
    
    @PersistenceContext(name = "kew-unit")
    private EntityManager entityManager;

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

	public void delete(String actionRequestId) {
        ActionRequestValue actionRequestValue = (ActionRequestValue) entityManager.find(ActionRequestValue.class, actionRequestId);
        entityManager.remove(actionRequestValue);
    }

    public void deleteByDocumentId(String documentId) {
        // FIXME should be jpa bulk update?
        Query query = entityManager.createNamedQuery("ActionRequestValue.FindByDocumentId");
        query.setParameter("documentId", documentId);
        List<ActionRequestValue> actionRequestValues = (List<ActionRequestValue>) query.getSingleResult();
        for(ActionRequestValue arv : actionRequestValues) {
            entityManager.remove(arv);
        }
    }

    public boolean doesDocumentHaveUserRequest(String principalId, String documentId) {
        Query query = entityManager.createNamedQuery("ActionRequestValue.GetUserRequestCount");
        query.setParameter("principalId", principalId);
        query.setParameter("documentId", documentId);
        query.setParameter("recipientTypeCd", RecipientType.PRINCIPAL.getCode());
        query.setParameter("currentIndicator", Boolean.TRUE);
        
        return ((Long)query.getSingleResult()) > 0;
    }

    public List<?> findActivatedByGroup(Group group) {
        
        Query query = entityManager.createNamedQuery("ActionRequestValue.FindActivatedByGroup");
        query.setParameter("groupId", group.getId());
        query.setParameter("currentIndicator", Boolean.TRUE);
        query.setParameter("status", ActionRequestStatus.ACTIVATED.getCode());
        
        return query.getResultList();
    }

    public List<ActionRequestValue> findAllByDocId(String documentId) {
        Query query = entityManager.createNamedQuery("ActionRequestValue.FindAllByDocId");
        query.setParameter("documentId", documentId);
        query.setParameter("currentIndicator", Boolean.TRUE);
        
        return query.getResultList();
    }

    public List<ActionRequestValue> findAllPendingByDocId(String documentId) {
        Query query = entityManager.createNamedQuery("ActionRequestValue.FindAllPendingByDocId");
        query.setParameter("documentId", documentId);
        query.setParameter("currentIndicator", Boolean.TRUE);
        query.setParameter("actionRequestStatus1", ActionRequestStatus.INITIALIZED.getCode());
        query.setParameter("actionRequestStatus2", ActionRequestStatus.ACTIVATED.getCode());
        
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<ActionRequestValue> findAllRootByDocId(String documentId) {
        Query query = entityManager.createNamedQuery("ActionRequestValue.FindAllRootByDocId");
        query.setParameter("documentId", documentId);
        query.setParameter("currentIndicator", Boolean.TRUE);
        
        return (List<ActionRequestValue>) query.getResultList();
    }

    public List<ActionRequestValue> findByDocumentIdIgnoreCurrentInd(String documentId) {
        Query query = entityManager.createNamedQuery("ActionRequestValue.FindByDocumentId");
        query.setParameter("documentId", documentId);
        
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<ActionRequestValue> findByStatusAndDocId(String statusCd, String documentId) {
        Query query = entityManager.createNamedQuery("ActionRequestValue.FindByStatusAndDocId");
        query.setParameter("documentId", documentId);
        query.setParameter("status", statusCd);
        query.setParameter("currentIndicator", Boolean.TRUE);
        
        return (List<ActionRequestValue>)query.getResultList();
    }

    public List<ActionRequestValue> findPendingByActionRequestedAndDocId(String actionRequestedCd, String documentId) {
        Query query = entityManager.createNamedQuery("ActionRequestValue.FindPendingByActionRequestedAndDocId");
        query.setParameter("documentId", documentId);
        query.setParameter("currentIndicator", Boolean.TRUE);
        query.setParameter("actionRequested", actionRequestedCd);
        query.setParameter("actionRequestStatus1", ActionRequestStatus.INITIALIZED.getCode());
        query.setParameter("actionRequestStatus2", ActionRequestStatus.ACTIVATED.getCode());
        
        return query.getResultList();
    }

    public List<ActionRequestValue> findPendingByDocIdAtOrBelowRouteLevel(String documentId, Integer routeLevel) {
        Query query = entityManager.createNamedQuery("ActionRequestValue.FindPendingByDocIdAtOrBelowRouteLevel");
        query.setParameter("documentId", documentId);
        query.setParameter("currentIndicator", Boolean.TRUE);
        query.setParameter("routeLevel", routeLevel);
        query.setParameter("status", ActionRequestStatus.DONE.getCode());
        
        return query.getResultList();
    }

    public List<ActionRequestValue> findPendingByResponsibilityIds(Collection responsibilityIds) {
        if (responsibilityIds == null || responsibilityIds.size() == 0)
            return Collections.emptyList();

        Query query = entityManager.createNamedQuery("ActionRequestValue.FindPendingByDocIdAtOrBelowRouteLevel");
        query.setParameter("responsibilityIds", responsibilityIds);
        
        return query.getResultList();
    }

    public List<ActionRequestValue> findPendingRootRequestsByDocIdAtOrBelowRouteLevel(String documentId, Integer routeLevel) {
        Query query = entityManager.createNamedQuery("ActionRequestValue.FindPendingRootRequestsByDocIdAtOrBelowRouteLevel");
        query.setParameter("documentId", documentId);
        query.setParameter("currentIndicator", Boolean.TRUE);
        query.setParameter("status", ActionRequestStatus.DONE.getCode());
        query.setParameter("routeLevel", routeLevel);
        
        return query.getResultList();
    }

    public List<ActionRequestValue> findPendingRootRequestsByDocIdAtRouteLevel(String documentId, Integer routeLevel) {
        Query query = entityManager.createNamedQuery("ActionRequestValue.FindPendingRootRequestsByDocIdAtRouteLevel");
        query.setParameter("documentId", documentId);
        query.setParameter("currentIndicator", Boolean.TRUE);
        query.setParameter("status", ActionRequestStatus.DONE.getCode());
        query.setParameter("routeLevel", routeLevel);
        
        return query.getResultList();
    }

    public List<ActionRequestValue> findPendingRootRequestsByDocIdAtRouteNode(String documentId, String nodeInstanceId) {
        Query query = entityManager.createNamedQuery("ActionRequestValue.FindPendingRootRequestsByDocIdAtRouteNode");
        query.setParameter("documentId", documentId);
        query.setParameter("currentIndicator", Boolean.TRUE);
        query.setParameter("routeNodeInstanceId", nodeInstanceId);
        query.setParameter("actionRequestStatus1", ActionRequestStatus.INITIALIZED.getCode());
        query.setParameter("actionRequestStatus2", ActionRequestStatus.ACTIVATED.getCode());
        
        return query.getResultList();
    }

    public List<ActionRequestValue> findPendingRootRequestsByDocumentType(String documentTypeId) {
        Query query = entityManager.createNamedQuery("ActionRequestValue.FindPendingRootRequestsByDocumentType");
        query.setParameter("documentTypeId", documentTypeId);
        query.setParameter("currentIndicator", Boolean.TRUE);
        query.setParameter("actionRequestStatus1", ActionRequestStatus.INITIALIZED.getCode());
        query.setParameter("actionRequestStatus2", ActionRequestStatus.ACTIVATED.getCode());
        
        return query.getResultList();
    }

    public List<ActionRequestValue> findRootRequestsByDocIdAtRouteNode(String documentId, String nodeInstanceId) {
        Query query = entityManager.createNamedQuery("ActionRequestValue.FindRootRequestsByDocIdAtRouteNode");
        query.setParameter("documentId", documentId);
        query.setParameter("currentIndicator", Boolean.TRUE);
        query.setParameter("routeNodeInstanceId", nodeInstanceId);
        
        return query.getResultList();
    }

    public ActionRequestValue getActionRequestByActionRequestId(String actionRequestId) {
        return entityManager.find(ActionRequestValue.class, actionRequestId);
    }

    @SuppressWarnings("unchecked")
    public List<String> getRequestGroupIds(String documentId) {
        Query query = entityManager.createNamedQuery("ActionRequestValue.GetRequestGroupIds");
        query.setParameter("documentId", documentId);
        query.setParameter("currentIndicator", Boolean.TRUE);
        query.setParameter("recipientTypeCd", RecipientType.GROUP.getCode());
        
        return query.getResultList();
    }

    public void saveActionRequest(ActionRequestValue actionRequest) {
        if ( actionRequest.getAnnotation() != null && actionRequest.getAnnotation().length() > 2000 ) {
        	actionRequest.setAnnotation( StringUtils.abbreviate(actionRequest.getAnnotation(), 2000) );
        }
    	if(actionRequest.getActionRequestId() == null) {
        	loadDefaultValues(actionRequest);
        	entityManager.persist(actionRequest);
        }else{
            OrmUtils.merge(entityManager, actionRequest);
        }
    }
    private void loadDefaultValues(ActionRequestValue actionRequest) {
        checkNull(actionRequest.getActionRequested(), "action requested");
        checkNull(actionRequest.getResponsibilityId(), "responsibility ID");
        checkNull(actionRequest.getRouteLevel(), "route level");
        checkNull(actionRequest.getDocVersion(), "doc version");
        if (actionRequest.getForceAction() == null) {
            actionRequest.setForceAction(Boolean.FALSE);
        }
        if (actionRequest.getStatus() == null) {
            actionRequest.setStatus(ActionRequestStatus.INITIALIZED.getCode());
        }
        if (actionRequest.getPriority() == null) {
            actionRequest.setPriority(KewApiConstants.ACTION_REQUEST_DEFAULT_PRIORITY);
        }
        if (actionRequest.getCurrentIndicator() == null) {
            actionRequest.setCurrentIndicator(true);
        }
        actionRequest.setCreateDate(new Timestamp(System.currentTimeMillis()));
    }
    //TODO Runtime might not be the right thing to do here...
    private void checkNull(Serializable value, String valueName) throws RuntimeException {
        if (value == null) {
            throw new RuntimeException("Null value for " + valueName);
        }
    }
    
	public List<ActionRequestValue> findActivatedByGroup(String groupId) {
		Query query = entityManager.createNamedQuery("ActionRequestValue.FindByStatusAndGroupId");
        query.setParameter("status", ActionRequestStatus.ACTIVATED.getCode());
        query.setParameter("currentIndicator", Boolean.TRUE);
        query.setParameter("groupId", groupId);
        
        return query.getResultList();
	}

    @Override
    public ActionRequestValue getRoleActionRequestByActionTakenId(String actionTakenId) {
        throw new UnsupportedOperationException("not yet implemented");
    }
}