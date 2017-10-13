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
package org.kuali.rice.kew.actionitem.dao.impl;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.rice.core.api.delegation.DelegationType;
import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.core.framework.persistence.jpa.criteria.Criteria;
import org.kuali.rice.core.framework.persistence.jpa.criteria.QueryByCriteria;
import org.kuali.rice.kew.actionitem.ActionItem;
import org.kuali.rice.kew.actionitem.OutboxItemActionListExtension;
import org.kuali.rice.kew.actionitem.dao.ActionItemDAO;
import org.kuali.rice.kew.actionrequest.KimGroupRecipient;
import org.kuali.rice.kew.actionrequest.Recipient;
import org.kuali.rice.kew.util.WebFriendlyRecipient;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * OJB implementation of {@link ActionItemDAO}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ActionItemDAOJpaImpl implements ActionItemDAO {
	@PersistenceContext(unitName="kew-unit")
	private EntityManager entityManager;

    public ActionItem findByActionItemId(String actionItemId) {
    	return entityManager.find(ActionItem.class, actionItemId);
    }

    public void deleteActionItems(Long actionRequestId) {
        Criteria crit = new Criteria(ActionItem.class.getName());
        crit.eq("actionRequestId", actionRequestId);
        for(Object actionItem:new QueryByCriteria(entityManager,crit).toQuery().getResultList()){
            if( ! (actionItem instanceof OutboxItemActionListExtension)) {
                entityManager.remove(actionItem);
            }
        }

    }

    public void deleteActionItem(ActionItem actionItem) {
    	entityManager.remove(findByActionItemId(actionItem.getId()));
    }

    public void deleteByDocumentIdWorkflowUserId(String documentId, String workflowUserId) {
        Criteria crit = new Criteria(ActionItem.class.getName());
        crit.eq("documentId", documentId);
        crit.eq("principalId", workflowUserId);
        for(Object actionItem: new QueryByCriteria(entityManager,crit).toQuery().getResultList()){
            if( ! (actionItem instanceof OutboxItemActionListExtension)) {
                entityManager.remove(actionItem);
            }
        }
    }

    public void deleteByDocumentId(String documentId) {
        Criteria crit = new Criteria(ActionItem.class.getName());
        crit.eq("documentId", documentId);
        for(Object actionItem: new QueryByCriteria(entityManager,crit).toQuery().getResultList()){
            if( ! (actionItem instanceof OutboxItemActionListExtension)) {
                entityManager.remove(actionItem);
            }
        }
    }

    /**
	 * This method replaces findByWorkflowUser
	 *
	 * @see org.kuali.rice.kew.actionitem.dao.ActionItemDAO#findByPrincipalId(java.lang.String)
	 */
	public Collection<ActionItem> findByPrincipalId(String principalId) {
		Criteria crit = new Criteria(ActionItem.class.getName());
        crit.eq("principalId", principalId);
        crit.orderBy("documentId", true);
        List<ActionItem> results = new QueryByCriteria(entityManager, crit).toQuery().getResultList();
        
        return removeOutBoxItems(results);
	}

    public Collection<ActionItem> findByWorkflowUserDocumentId(String workflowId, String documentId) {
        Criteria crit = new Criteria(ActionItem.class.getName());
        crit.eq("principalId", workflowId);
        crit.eq("documentId", documentId);
        List<ActionItem> results = new QueryByCriteria(entityManager, crit).toQuery().getResultList();
        
        return removeOutBoxItems(results);
    }

    public Collection<ActionItem> findByDocumentTypeName(String documentTypeName) {
        Criteria crit = new Criteria(ActionItem.class.getName());
        crit.eq("docName", documentTypeName);
        List<ActionItem> results = new QueryByCriteria(entityManager, crit).toQuery().getResultList();

        return removeOutBoxItems(results);
    }

    public Collection<OutboxItemActionListExtension> getOutboxItemsByDocumentType(String documentTypeName) {
        Criteria crit = new Criteria(ActionItem.class.getName());
        crit.eq("docName", documentTypeName);
        List<ActionItem> results = new QueryByCriteria(entityManager, crit).toQuery().getResultList();
        List<OutboxItemActionListExtension> finalResults = new ArrayList<OutboxItemActionListExtension>();
        for (ActionItem actionItem : results) {
            if (actionItem instanceof OutboxItemActionListExtension) {
                finalResults.add((OutboxItemActionListExtension)actionItem);
            }
        }
        return finalResults;
    }
    
    public Collection<ActionItem> findByDocumentId(String documentId) {
        Criteria crit = new Criteria(ActionItem.class.getName());
        crit.eq("documentId", documentId);
        List<ActionItem> results = new QueryByCriteria(entityManager, crit).toQuery().getResultList();
        
        return removeOutBoxItems(results);
    }

    public Collection<ActionItem> findByActionRequestId(String actionRequestId) {
        Criteria crit = new Criteria(ActionItem.class.getName());
        crit.eq("actionRequestId", actionRequestId);
        List<ActionItem> results = new QueryByCriteria(entityManager, crit).toQuery().getResultList();
        
        return removeOutBoxItems(results);
    }

    public void saveActionItem(ActionItem actionItem) {
    	if (actionItem.getDateAssigned() == null) {
            actionItem.setDateAssigned(new Timestamp(new Date().getTime()));
        }
    	
    	if(actionItem.getId()==null){
        	entityManager.persist(actionItem);
    	}else{
    		OrmUtils.merge(entityManager, actionItem);
    	}
    }

    public Collection<Recipient> findSecondaryDelegators(String principalId) {
        Criteria notNullWorkflowCriteria = new Criteria(ActionItem.class.getName());
        notNullWorkflowCriteria.notNull("delegatorPrincipalId");
        Criteria notNullWorkgroupCriteria = new Criteria(ActionItem.class.getName());
        notNullWorkgroupCriteria.notNull("delegatorGroupId");
        Criteria orCriteria = new Criteria(ActionItem.class.getName());
        orCriteria.or(notNullWorkflowCriteria);
        orCriteria.or(notNullWorkgroupCriteria);
        Criteria criteria = new Criteria(ActionItem.class.getName());
        criteria.eq("principalId", principalId);
        criteria.eq("delegationType", DelegationType.SECONDARY.getCode());
        criteria.and(orCriteria);

        Map<Object, Recipient> delegators = new HashMap<Object, Recipient>();

        for(Object actionItem:new QueryByCriteria(entityManager, criteria).toQuery().getResultList()){
        	final String delegatorPrincipalId = ((ActionItem)actionItem).getDelegatorPrincipalId();
        	String delegatorGroupId = ((ActionItem)actionItem).getDelegatorGroupId();

        	if (delegatorPrincipalId != null && !delegators.containsKey(delegatorPrincipalId)) {
                delegators.put(delegatorPrincipalId,new WebFriendlyRecipient(KimApiServiceLocator.getPersonService().getPerson(delegatorPrincipalId)));
            }else if (delegatorGroupId != null) {
                if (!delegators.containsKey(delegatorGroupId)) {
                    delegators.put(delegatorGroupId, new KimGroupRecipient(KimApiServiceLocator.getGroupService().getGroup(
                            delegatorGroupId)));
                }
            }
        }
         return delegators.values();
    }

    public Collection<Recipient> findPrimaryDelegationRecipients(String principalId) {
    	List<String> workgroupIds = KimApiServiceLocator.getGroupService().getGroupIdsByPrincipalId(principalId);
        Criteria orCriteria = new Criteria(ActionItem.class.getName());
        Criteria delegatorPrincipalIdCriteria = new Criteria(ActionItem.class.getName());
        delegatorPrincipalIdCriteria.eq("delegatorPrincipalId", principalId);
        if (CollectionUtils.isNotEmpty(workgroupIds)) {
            Criteria delegatorWorkgroupCriteria = new Criteria(ActionItem.class.getName());
            delegatorWorkgroupCriteria.in("delegatorGroupId", new ArrayList<String>(workgroupIds));
            orCriteria.or(delegatorWorkgroupCriteria);
            orCriteria.or(delegatorPrincipalIdCriteria);
        }
        else {
            orCriteria.and(delegatorPrincipalIdCriteria);
        }
        Criteria criteria = new Criteria(ActionItem.class.getName());
        criteria.eq("delegationType", DelegationType.PRIMARY.getCode());
        criteria.and(orCriteria);

        Map<Object, Recipient> delegators = new HashMap<Object, Recipient>();
        for(Object actionItem:new QueryByCriteria(entityManager, criteria).toQuery().getResultList()){
        	String princlId = ((ActionItem)actionItem).getPrincipalId();
            if (princlId != null && !delegators.containsKey(princlId)) {
                delegators.put(princlId, new WebFriendlyRecipient(KimApiServiceLocator.getPersonService().getPerson(princlId)));
            }
        }

        return delegators.values();
    }
    
    private List<ActionItem> removeOutBoxItems(List<ActionItem> results) {
        Iterator<ActionItem> iterator = results.iterator();
        while(iterator.hasNext()) {
            if(iterator.next() instanceof OutboxItemActionListExtension) {
                iterator.remove();
            }
        }
        
        return results;
    }

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


}
