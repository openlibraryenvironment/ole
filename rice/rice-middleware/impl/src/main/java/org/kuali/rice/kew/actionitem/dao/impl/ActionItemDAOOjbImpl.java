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
import org.apache.log4j.Logger;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.broker.query.QueryFactory;
import org.apache.ojb.broker.query.ReportQueryByCriteria;
import org.kuali.rice.core.api.delegation.DelegationType;
import org.kuali.rice.kew.actionitem.ActionItem;
import org.kuali.rice.kew.actionitem.OutboxItemActionListExtension;
import org.kuali.rice.kew.actionitem.dao.ActionItemDAO;
import org.kuali.rice.kew.actionrequest.KimGroupRecipient;
import org.kuali.rice.kew.actionrequest.Recipient;
import org.kuali.rice.kew.util.WebFriendlyRecipient;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.springmodules.orm.ojb.support.PersistenceBrokerDaoSupport;

import java.sql.Timestamp;
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
public class ActionItemDAOOjbImpl extends PersistenceBrokerDaoSupport implements ActionItemDAO {
	
 
	private static final Logger LOG = Logger.getLogger(ActionItemDAOOjbImpl.class);

	
    public ActionItem findByActionItemId(String actionItemId) {
        Criteria crit = new Criteria();
        crit.addEqualTo("id", actionItemId);
        return (ActionItem) this.getPersistenceBrokerTemplate().getObjectByQuery(new QueryByCriteria(ActionItem.class, crit));
    }

    public void deleteActionItems(Long actionRequestId) {
        Criteria crit = new Criteria();
        crit.addEqualTo("actionRequestId", actionRequestId);
        this.getPersistenceBrokerTemplate().deleteByQuery(new QueryByCriteria(ActionItem.class, crit));
    }

    public void deleteActionItem(ActionItem actionItem) {
        this.getPersistenceBrokerTemplate().delete(actionItem);
    }

    public void deleteByDocumentIdWorkflowUserId(String documentId, String workflowUserId) {
        Criteria crit = new Criteria();
        crit.addEqualTo("documentId", documentId);
        crit.addEqualTo("principalId", workflowUserId);
        this.getPersistenceBrokerTemplate().deleteByQuery(new QueryByCriteria(ActionItem.class, crit));
    }

    public void deleteByDocumentId(String documentId) {
        Criteria crit = new Criteria();
        crit.addEqualTo("documentId", documentId);
        this.getPersistenceBrokerTemplate().deleteByQuery(new QueryByCriteria(ActionItem.class, crit));
    }

    public Collection<ActionItem> findByWorkflowUserDocumentId(String workflowId, String documentId) {
        Criteria crit = new Criteria();
        crit.addEqualTo("principalId", workflowId);
        crit.addEqualTo("documentId", documentId);
        return this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(ActionItem.class, crit));
    }

    public Collection<ActionItem> findByDocumentId(String documentId) {
        Criteria crit = new Criteria();
        crit.addEqualTo("documentId", documentId);
        return this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(ActionItem.class, crit));
    }

    public Collection<ActionItem> findByActionRequestId(String actionRequestId) {
        Criteria crit = new Criteria();
        crit.addEqualTo("actionRequestId", actionRequestId);
        return this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(ActionItem.class, crit));
    }

    public Collection<ActionItem> findByDocumentTypeName(String documentTypeName) {
        return getItemsByDocumentType(ActionItem.class, documentTypeName);
    }

    public Collection<OutboxItemActionListExtension> getOutboxItemsByDocumentType(String documentTypeName) {
        return getItemsByDocumentType(OutboxItemActionListExtension.class, documentTypeName);
    }

    private <T extends ActionItem> Collection<T> getItemsByDocumentType(Class<T> objectClass, String documentTypeName) {
        Criteria crit = new Criteria();
        crit.addEqualTo("docName", documentTypeName);
        return this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(objectClass, crit));
    }

    public void saveActionItem(ActionItem actionItem) {
        if (actionItem.getDateAssigned() == null) {
            actionItem.setDateAssigned(new Timestamp(new Date().getTime()));
        }
        this.getPersistenceBrokerTemplate().store(actionItem);
    }

    public Collection<Recipient> findSecondaryDelegators(String principalId) {
        Criteria notNullWorkflowCriteria = new Criteria();
        notNullWorkflowCriteria.addNotNull("delegatorPrincipalId");
        Criteria notNullWorkgroupCriteria = new Criteria();
        notNullWorkgroupCriteria.addNotNull("delegatorGroupId");
        Criteria orCriteria = new Criteria();
        orCriteria.addOrCriteria(notNullWorkflowCriteria);
        orCriteria.addOrCriteria(notNullWorkgroupCriteria);
        Criteria criteria = new Criteria();
        criteria.addEqualTo("principalId", principalId);
        criteria.addEqualTo("delegationType", DelegationType.SECONDARY.getCode());
        criteria.addAndCriteria(orCriteria);
        ReportQueryByCriteria query = QueryFactory.newReportQuery(ActionItem.class, criteria);

        query.setAttributes(new String[]{"delegatorPrincipalId", "delegatorGroupId"});
        Map<Object, Recipient> delegators = new HashMap<Object, Recipient>();
        Iterator iterator = this.getPersistenceBrokerTemplate().getReportQueryIteratorByQuery(query);
        while (iterator.hasNext()) {
            Object[] ids = (Object[]) iterator.next();
            if (ids[0] != null && !delegators.containsKey((String) ids[0])) {
            	WebFriendlyRecipient rec = new WebFriendlyRecipient(KimApiServiceLocator.getPersonService().getPerson((String) ids[0]));
                delegators.put((String) ids[0], rec);
            } else if (ids[1] != null) {
                String workgroupId = ids[1].toString();
                if (!delegators.containsKey(workgroupId)) {
                    delegators.put(workgroupId, new KimGroupRecipient(KimApiServiceLocator.getGroupService().getGroup(workgroupId)));
                }
            }
        }
        return delegators.values();
    }

    public Collection<Recipient> findPrimaryDelegationRecipients(String principalId) {
    	List<String> workgroupIds = KimApiServiceLocator.getGroupService().getGroupIdsByPrincipalId(principalId);
        Criteria orCriteria = new Criteria();
        Criteria delegatorPrincipalIdCriteria = new Criteria();
        delegatorPrincipalIdCriteria.addEqualTo("delegatorPrincipalId", principalId);
        if (CollectionUtils.isNotEmpty(workgroupIds)) {
            Criteria delegatorWorkgroupCriteria = new Criteria();
            delegatorWorkgroupCriteria.addIn("delegatorGroupId", workgroupIds);
            orCriteria.addOrCriteria(delegatorWorkgroupCriteria);
            orCriteria.addOrCriteria(delegatorPrincipalIdCriteria);
        }
        else {
            orCriteria.addAndCriteria(delegatorPrincipalIdCriteria);
        }
        Criteria criteria = new Criteria();
        criteria.addEqualTo("delegationType", DelegationType.PRIMARY.getCode());
        criteria.addAndCriteria(orCriteria);
        ReportQueryByCriteria query = QueryFactory.newReportQuery(ActionItem.class, criteria, true);

        query.setAttributes(new String[]{"principalId"});
        Map<Object, Recipient> delegators = new HashMap<Object, Recipient>();
        Iterator iterator = this.getPersistenceBrokerTemplate().getReportQueryIteratorByQuery(query);
        while (iterator.hasNext()) {
            Object[] ids = (Object[]) iterator.next();
            if (ids[0] != null && !delegators.containsKey((String) ids[0])) {
               
            	Person person = KimApiServiceLocator.getPersonService().getPerson((String) ids[0]);
            	if (person != null) {
            		WebFriendlyRecipient rec = new WebFriendlyRecipient(person);
            	    delegators.put((String) ids[0],rec);
            	}  else {
                    LOG.warn("The name for " + (String) ids[0] + " was not added to the primary delegate drop down list because the delegate does not exist.");
                }

            }
        }
        return delegators.values();
    }

	/**
	 * This overridden method replaced findByWorkfowUser
	 *
	 * @see org.kuali.rice.kew.actionitem.dao.ActionItemDAO#findByPrincipalId(java.lang.String)
	 */
	public Collection<ActionItem> findByPrincipalId(String principalId) {
		 Criteria crit = new Criteria();
	     crit.addEqualTo("principalId", principalId);
	     QueryByCriteria query = new QueryByCriteria(ActionItem.class, crit);
	     query.addOrderByAscending("documentId");
	     return this.getPersistenceBrokerTemplate().getCollectionByQuery(query);
	}

}
