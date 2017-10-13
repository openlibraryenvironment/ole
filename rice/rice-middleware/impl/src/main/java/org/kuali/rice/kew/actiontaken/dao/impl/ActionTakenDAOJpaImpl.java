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
package org.kuali.rice.kew.actiontaken.dao.impl;

import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.core.framework.persistence.jpa.criteria.Criteria;
import org.kuali.rice.core.framework.persistence.jpa.criteria.QueryByCriteria;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.actiontaken.dao.ActionTakenDAO;
import org.kuali.rice.kew.api.action.ActionType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;


/**
 * OJB implementation of the {@link ActionTakenDAO}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ActionTakenDAOJpaImpl implements ActionTakenDAO {

	@PersistenceContext(unitName="kew-unit")
	private EntityManager entityManager;

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ActionTakenDAOJpaImpl.class);

    public ActionTakenValue load(String id) {
        LOG.debug("Loading Action Taken for the given id " + id);
        return entityManager.find(ActionTakenValue.class, id);
    }

    public void deleteActionTaken(ActionTakenValue actionTaken) {
        LOG.debug("deleting ActionTaken " + actionTaken.getActionTakenId());
        entityManager.remove(entityManager.find(ActionTakenValue.class, actionTaken.getActionTakenId()));
    }

    public ActionTakenValue findByActionTakenId(String actionTakenId) {
        LOG.debug("finding Action Taken by actionTakenId " + actionTakenId);
        Criteria crit = new Criteria(ActionTakenValue.class.getName());
        crit.eq("actionTakenId", actionTakenId);
        crit.eq("currentIndicator", Boolean.TRUE);
        return (ActionTakenValue) new QueryByCriteria(entityManager, crit).toQuery().getSingleResult();
    }

    public Collection<ActionTakenValue> findByDocIdAndAction(String documentId, String action) {
        LOG.debug("finding Action Taken by documentId " + documentId + " and action " + action);
        Criteria crit = new Criteria(ActionTakenValue.class.getName());
        crit.eq("documentId", documentId);
        crit.eq("actionTaken", action);
        crit.eq("currentIndicator", Boolean.TRUE);
        return (Collection<ActionTakenValue>) new QueryByCriteria(entityManager, crit).toQuery().getResultList();
    }

    public Collection<ActionTakenValue> findByDocumentId(String documentId) {
        LOG.debug("finding Action Takens by documentId " + documentId);
        Criteria crit = new Criteria(ActionTakenValue.class.getName());
        crit.eq("documentId", documentId);
        crit.eq("currentIndicator", Boolean.TRUE);
        crit.orderBy("actionDate", true);
        return (Collection<ActionTakenValue>) new QueryByCriteria(entityManager, crit).toQuery().getResultList();
    }

    public List<ActionTakenValue> findByDocumentIdWorkflowId(String documentId, String workflowId) {
        LOG.debug("finding Action Takens by documentId " + documentId + " and workflowId" + workflowId);
        Criteria crit = new Criteria(ActionTakenValue.class.getName());
        crit.eq("documentId", documentId);
        crit.eq("principalId", workflowId);
        crit.eq("currentIndicator", Boolean.TRUE);
        return (List<ActionTakenValue>) new QueryByCriteria(entityManager, crit).toQuery().getResultList();
    }

    public List findByDocumentIdIgnoreCurrentInd(String documentId) {
        LOG.debug("finding ActionsTaken ignoring currentInd by documentId:" + documentId);
        Criteria crit = new Criteria(ActionTakenValue.class.getName());
        crit.eq("documentId", documentId);
        return (List) new QueryByCriteria(entityManager, crit);
    }

    public void saveActionTaken(ActionTakenValue actionTaken) {
        LOG.debug("saving ActionTaken");
        checkNull(actionTaken.getDocumentId(), "Document ID");
        checkNull(actionTaken.getActionTaken(), "action taken code");
        checkNull(actionTaken.getDocVersion(), "doc version");
        checkNull(actionTaken.getPrincipalId(), "principal ID");

        if (actionTaken.getActionDate() == null) {
            actionTaken.setActionDate(new Timestamp(System.currentTimeMillis()));
        }
        if (actionTaken.getCurrentIndicator() == null) {
            actionTaken.setCurrentIndicator(Boolean.TRUE);
        }
        LOG.debug("saving ActionTaken: routeHeader " + actionTaken.getDocumentId() +
                ", actionTaken " + actionTaken.getActionTaken() + ", principalId " + actionTaken.getPrincipalId());

        if(actionTaken.getActionTakenId()==null){
        	entityManager.persist(actionTaken);
        }else{
        	OrmUtils.merge(entityManager, actionTaken);
        }
    }

    //TODO perhaps runtime isn't the best here, maybe a dao runtime exception
    private void checkNull(Serializable value, String valueName) throws RuntimeException {
        if (value == null) {
            throw new RuntimeException("Null value for " + valueName);
        }
    }

    public void deleteByDocumentId(String documentId){
	    Criteria crit = new Criteria(ActionRequestValue.class.getName());
	    crit.eq("documentId", documentId);
	    ActionRequestValue actionRequestValue = (ActionRequestValue) new QueryByCriteria(entityManager, crit).toQuery().getSingleResult();
	    entityManager.remove(actionRequestValue);
    }

    public boolean hasUserTakenAction(String workflowId, String documentId) {
    	Criteria crit = new Criteria(ActionTakenValue.class.getName());
	    crit.eq("documentId", documentId);
	    crit.eq("principalId", workflowId);
	    crit.eq("currentIndicator", Boolean.TRUE);
	    long count = (Long) new QueryByCriteria(entityManager, crit).toCountQuery().getSingleResult();
        return count > 0;
    }

    @Override
    public Timestamp getLastActionTakenDate(String documentId, ActionType actionType) {
        // TODO - fix this!

        throw new UnsupportedOperationException("The JPA version of this method still needs to be implemented!");

    }

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
