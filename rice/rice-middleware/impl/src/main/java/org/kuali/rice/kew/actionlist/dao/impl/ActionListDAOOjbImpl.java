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
package org.kuali.rice.kew.actionlist.dao.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.accesslayer.LookupException;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.kuali.rice.core.api.delegation.DelegationType;
import org.kuali.rice.kew.actionitem.ActionItem;
import org.kuali.rice.kew.actionitem.ActionItemActionListExtension;
import org.kuali.rice.kew.actionitem.OutboxItemActionListExtension;
import org.kuali.rice.kew.actionlist.ActionListFilter;
import org.kuali.rice.kew.actionlist.dao.ActionListDAO;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.springmodules.orm.ojb.PersistenceBrokerCallback;
import org.springmodules.orm.ojb.support.PersistenceBrokerDaoSupport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * OJB implementation of the {@link ActionListDAO}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ActionListDAOOjbImpl extends PersistenceBrokerDaoSupport implements ActionListDAO {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ActionListDAOOjbImpl.class);

    public Collection<ActionItemActionListExtension> getActionList(String principalId, ActionListFilter filter) {
        return getActionItemsInActionList(ActionItemActionListExtension.class, principalId, filter);
    }

    public Collection<ActionItemActionListExtension> getActionListForSingleDocument(String documentId) {
        LOG.debug("getting action list for document id " + documentId);
        Criteria crit = new Criteria();
        crit.addEqualTo("documentId", documentId);
        Collection<ActionItemActionListExtension> collection = this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(ActionItemActionListExtension.class, crit));
        LOG.debug("found " + collection.size() + " action items for document id " + documentId);
        return createActionListForRouteHeader(collection);
    }

    private Criteria setUpActionListCriteria(String principalId, ActionListFilter filter) {
        LOG.debug("setting up Action List criteria");
        Criteria crit = new Criteria();
        boolean filterOn = false;
        String filteredByItems = "";

        if (filter.getActionRequestCd() != null && !"".equals(filter.getActionRequestCd().trim()) && !filter.getActionRequestCd().equals(KewApiConstants.ALL_CODE)) {
            if (filter.isExcludeActionRequestCd()) {
                crit.addNotEqualTo("actionRequestCd", filter.getActionRequestCd());
            } else {
                crit.addEqualTo("actionRequestCd", filter.getActionRequestCd());
            }
            filteredByItems += filteredByItems.length() > 0 ? ", " : "";
            filteredByItems += "Action Requested";
        }

        if (filter.getCreateDateFrom() != null || filter.getCreateDateTo() != null) {
            if (filter.isExcludeCreateDate()) {
                if (filter.getCreateDateFrom() != null && filter.getCreateDateTo() != null) {
                    crit.addNotBetween("routeHeader.createDate", new Timestamp(beginningOfDay(filter.getCreateDateFrom()).getTime()), new Timestamp(endOfDay(filter.getCreateDateTo()).getTime()));
                } else if (filter.getCreateDateFrom() != null && filter.getCreateDateTo() == null) {
                    crit.addLessOrEqualThan("routeHeader.createDate", new Timestamp(beginningOfDay(filter.getCreateDateFrom()).getTime()));
                } else if (filter.getCreateDateFrom() == null && filter.getCreateDateTo() != null) {
                    crit.addGreaterOrEqualThan("routeHeader.createDate", new Timestamp(endOfDay(filter.getCreateDateTo()).getTime()));
                }
            } else {
                if (filter.getCreateDateFrom() != null && filter.getCreateDateTo() != null) {
                    crit.addBetween("routeHeader.createDate", new Timestamp(beginningOfDay(filter.getCreateDateFrom()).getTime()), new Timestamp(endOfDay(filter.getCreateDateTo()).getTime()));
                } else if (filter.getCreateDateFrom() != null && filter.getCreateDateTo() == null) {
                    crit.addGreaterOrEqualThan("routeHeader.createDate", new Timestamp(beginningOfDay(filter.getCreateDateFrom()).getTime()));
                } else if (filter.getCreateDateFrom() == null && filter.getCreateDateTo() != null) {
                    crit.addLessOrEqualThan("routeHeader.createDate", new Timestamp(endOfDay(filter.getCreateDateTo()).getTime()));
                }
            }
            filteredByItems += filteredByItems.length() > 0 ? ", " : "";
            filteredByItems += "Date Created";
        }

        if (filter.getDocRouteStatus() != null && !"".equals(filter.getDocRouteStatus().trim()) && !filter.getDocRouteStatus().equals(KewApiConstants.ALL_CODE)) {
            if (filter.isExcludeRouteStatus()) {
                crit.addNotEqualTo("routeHeader.docRouteStatus", filter.getDocRouteStatus());
            } else {
                crit.addEqualTo("routeHeader.docRouteStatus", filter.getDocRouteStatus());
            }
            filteredByItems += filteredByItems.length() > 0 ? ", " : "";
            filteredByItems += "Document Route Status";
        }

        if (filter.getDocumentTitle() != null && !"".equals(filter.getDocumentTitle().trim())) {
            String docTitle = filter.getDocumentTitle();
            if (docTitle.trim().endsWith("*")) {
                docTitle = docTitle.substring(0, docTitle.length() - 1);
            }

            if (filter.isExcludeDocumentTitle()) {
                crit.addNotLike("docTitle", "%" + docTitle + "%");
            } else {
                crit.addLike("docTitle", "%" + docTitle + "%");
            }
            filteredByItems += filteredByItems.length() > 0 ? ", " : "";
            filteredByItems += "Document Title";
        }

        if (filter.getDocumentType() != null && !"".equals(filter.getDocumentType().trim())) {
            if (filter.isExcludeDocumentType()) {
                crit.addNotLike("docName", "%" + filter.getDocumentType() + "%");
            } else {
                String documentTypeName = filter.getDocumentType();
                DocumentType documentType = KEWServiceLocator.getDocumentTypeService().findByName(documentTypeName);
                if (documentType == null) {
                    crit.addLike("docName", "%" + filter.getDocumentType() + "%");
                } else {
                    // search this document type plus it's children
                    Criteria docTypeCrit = new Criteria();
                    constructDocumentTypeCriteria(docTypeCrit, documentType);
                    crit.addAndCriteria(docTypeCrit);
                }
            }
            filteredByItems += filteredByItems.length() > 0 ? ", " : "";
            filteredByItems += "Document Type";
        }

        if (filter.getLastAssignedDateFrom() != null || filter.getLastAssignedDateTo() != null) {
            if (filter.isExcludeLastAssignedDate()) {
                if (filter.getLastAssignedDateFrom() != null && filter.getLastAssignedDateTo() != null) {
                    crit.addNotBetween("dateAssigned", new Timestamp(beginningOfDay(filter.getLastAssignedDateFrom()).getTime()), new Timestamp(endOfDay(filter.getLastAssignedDateTo()).getTime()));
                } else if (filter.getLastAssignedDateFrom() != null && filter.getLastAssignedDateTo() == null) {
                    crit.addLessOrEqualThan("dateAssigned", new Timestamp(beginningOfDay(filter.getLastAssignedDateFrom()).getTime()));
                } else if (filter.getLastAssignedDateFrom() == null && filter.getLastAssignedDateTo() != null) {
                    crit.addGreaterOrEqualThan("dateAssigned", new Timestamp(endOfDay(filter.getLastAssignedDateTo()).getTime()));
                }
            } else {
                if (filter.getLastAssignedDateFrom() != null && filter.getLastAssignedDateTo() != null) {
                    crit.addBetween("dateAssigned", new Timestamp(beginningOfDay(filter.getLastAssignedDateFrom()).getTime()), new Timestamp(endOfDay(filter.getLastAssignedDateTo()).getTime()));
                } else if (filter.getLastAssignedDateFrom() != null && filter.getLastAssignedDateTo() == null) {
                    crit.addGreaterOrEqualThan("dateAssigned", new Timestamp(beginningOfDay(filter.getLastAssignedDateFrom()).getTime()));
                } else if (filter.getLastAssignedDateFrom() == null && filter.getLastAssignedDateTo() != null) {
                    crit.addLessOrEqualThan("dateAssigned", new Timestamp(endOfDay(filter.getLastAssignedDateTo()).getTime()));
                }
            }
            filteredByItems += filteredByItems.length() > 0 ? ", " : "";
            filteredByItems += "Date Last Assigned";
        }

        filter.setGroupId(null);
        if (filter.getGroupIdString() != null && !"".equals(filter.getGroupIdString().trim()) && !filter.getGroupIdString().trim().equals(KewApiConstants.NO_FILTERING)) {

            filter.setGroupId(filter.getGroupIdString().trim());

            if (filter.isExcludeGroupId()) {
                Criteria critNotEqual = new Criteria();
                critNotEqual.addNotEqualTo("groupId", filter.getGroupId());
                Criteria critNull = new Criteria();
                critNull.addIsNull("groupId");
                critNotEqual.addOrCriteria(critNull);
                crit.addAndCriteria(critNotEqual);
            } else {
                crit.addEqualTo("groupId", filter.getGroupId());
            }
            filteredByItems += filteredByItems.length() > 0 ? ", " : "";
            filteredByItems += "Action Request Workgroup";
        }

        if (filteredByItems.length() > 0) {
            filterOn = true;
        }

        boolean addedDelegationCriteria = false;
        if (StringUtils.isBlank(filter.getDelegationType()) && StringUtils.isBlank(filter.getPrimaryDelegateId()) && StringUtils.isBlank(filter.getDelegatorId())) {
            crit.addEqualTo("principalId", principalId);
            addedDelegationCriteria = true;
        } else if ((StringUtils.isNotBlank(filter.getDelegationType()) && DelegationType.PRIMARY.getCode().equals(filter.getDelegationType()))
                || StringUtils.isNotBlank(filter.getPrimaryDelegateId())) {
            // using a primary delegation
            if ((StringUtils.isBlank(filter.getPrimaryDelegateId())) || (filter.getPrimaryDelegateId().trim().equals(KewApiConstants.ALL_CODE))) {
                // user wishes to see all primary delegations
                Criteria userCrit = new Criteria();
                Criteria groupCrit = new Criteria();
                Criteria orCrit = new Criteria();
                userCrit.addEqualTo("delegatorPrincipalId", principalId);
                List<String> delegatorGroupIds = KimApiServiceLocator.getGroupService().getGroupIdsByPrincipalId(
                        principalId);
                if (delegatorGroupIds != null && !delegatorGroupIds.isEmpty()) {
                    groupCrit.addIn("delegatorGroupId", delegatorGroupIds);
                }
                orCrit.addOrCriteria(userCrit);
                orCrit.addOrCriteria(groupCrit);
                crit.addAndCriteria(orCrit);
                crit.addEqualTo("delegationType", DelegationType.PRIMARY.getCode());
                filter.setDelegationType(DelegationType.PRIMARY.getCode());
                filter.setExcludeDelegationType(false);
                addToFilterDescription(filteredByItems, "Primary Delegator Id");
                addedDelegationCriteria = true;
                filterOn = true;
            } else if (!filter.getPrimaryDelegateId().trim().equals(KewApiConstants.PRIMARY_DELEGATION_DEFAULT)) {
                // user wishes to see primary delegation for a single user
                crit.addEqualTo("principalId", filter.getPrimaryDelegateId());
                Criteria userCrit = new Criteria();
                Criteria groupCrit = new Criteria();
                Criteria orCrit = new Criteria();
                userCrit.addEqualTo("delegatorPrincipalId", principalId);
                List<String> delegatorGroupIds = KimApiServiceLocator.getGroupService().getGroupIdsByPrincipalId(
                        principalId);
                if (delegatorGroupIds != null && !delegatorGroupIds.isEmpty()) {
                    groupCrit.addIn("delegatorGroupId", delegatorGroupIds);
                }
                orCrit.addOrCriteria(userCrit);
                orCrit.addOrCriteria(groupCrit);
                crit.addAndCriteria(orCrit);
                crit.addEqualTo("delegationType", DelegationType.PRIMARY.getCode());
                filter.setDelegationType(DelegationType.PRIMARY.getCode());
                filter.setExcludeDelegationType(false);
                addToFilterDescription(filteredByItems, "Primary Delegator Id");
                addedDelegationCriteria = true;
                filterOn = true;
            }
        }
        if (!addedDelegationCriteria && ( (StringUtils.isNotBlank(filter.getDelegationType()) && DelegationType.SECONDARY.getCode().equals(filter.getDelegationType()))
                || StringUtils.isNotBlank(filter.getDelegatorId()) )) {
            // using a secondary delegation
            crit.addEqualTo("principalId", principalId);
            if (StringUtils.isBlank(filter.getDelegatorId())) {
                filter.setDelegationType(DelegationType.SECONDARY.getCode());
                // if isExcludeDelegationType() we want to show the default action list which is set up later in this method
                if (!filter.isExcludeDelegationType()) {
                    crit.addEqualTo("delegationType", DelegationType.SECONDARY.getCode());
                    addToFilterDescription(filteredByItems, "Secondary Delegator Id");
                    addedDelegationCriteria = true;
                    filterOn = true;
                }
            } else if (filter.getDelegatorId().trim().equals(KewApiConstants.ALL_CODE)) {
                // user wishes to see all secondary delegations
                crit.addEqualTo("delegationType", DelegationType.SECONDARY.getCode());
                filter.setDelegationType(DelegationType.SECONDARY.getCode());
                filter.setExcludeDelegationType(false);
                addToFilterDescription(filteredByItems, "Secondary Delegator Id");
                addedDelegationCriteria = true;
                filterOn = true;
            } else if (!filter.getDelegatorId().trim().equals(
                    KewApiConstants.DELEGATION_DEFAULT)) {
                // user has specified an id to see for secondary delegation
                filter.setDelegationType(DelegationType.SECONDARY.getCode());
                filter.setExcludeDelegationType(false);
                Criteria userCrit = new Criteria();
                Criteria groupCrit = new Criteria();
                if (filter.isExcludeDelegatorId()) {
                    Criteria userNull = new Criteria();
                    userCrit.addNotEqualTo("delegatorPrincipalId", filter.getDelegatorId());
                    userNull.addIsNull("delegatorPrincipalId");
                    userCrit.addOrCriteria(userNull);
                    Criteria groupNull = new Criteria();
                    groupCrit.addNotEqualTo("delegatorGroupId", filter.getDelegatorId());
                    groupNull.addIsNull("delegatorGroupId");
                    groupCrit.addOrCriteria(groupNull);
                    crit.addAndCriteria(userCrit);
                    crit.addAndCriteria(groupCrit);
                } else {
                    Criteria orCrit = new Criteria();
                    userCrit.addEqualTo("delegatorPrincipalId", filter.getDelegatorId());
                    groupCrit.addEqualTo("delegatorGroupId", filter.getDelegatorId());
                    orCrit.addOrCriteria(userCrit);
                    orCrit.addOrCriteria(groupCrit);
                    crit.addAndCriteria(orCrit);
                }
                addToFilterDescription(filteredByItems, "Secondary Delegator Id");
                addedDelegationCriteria = true;
                filterOn = true;
            }
        }

        // if we haven't added delegation criteria then use the default criteria below
        if (!addedDelegationCriteria) {
            crit.addEqualTo("principalId", principalId);
            filter.setDelegationType(DelegationType.SECONDARY.getCode());
            filter.setExcludeDelegationType(true);
            Criteria critNotEqual = new Criteria();
            Criteria critNull = new Criteria();
            critNotEqual.addNotEqualTo("delegationType", DelegationType.SECONDARY.getCode());
            critNull.addIsNull("delegationType");
            critNotEqual.addOrCriteria(critNull);
            crit.addAndCriteria(critNotEqual);
        }

        if (! "".equals(filteredByItems)) {
            filteredByItems = "Filtered by " + filteredByItems;
        }
        filter.setFilterLegend(filteredByItems);
        filter.setFilterOn(filterOn);

        LOG.debug("returning from Action List criteria");
        return crit;
    }

    private void constructDocumentTypeCriteria(Criteria criteria, DocumentType documentType) {
        // search this document type plus it's children
        Criteria docTypeBaseCrit = new Criteria();
        docTypeBaseCrit.addEqualTo("docName", documentType.getName());
        criteria.addOrCriteria(docTypeBaseCrit);
        Collection children = documentType.getChildrenDocTypes();
        if (children != null) {
            for (Iterator iterator = children.iterator(); iterator.hasNext();) {
                DocumentType childDocumentType = (DocumentType) iterator.next();
                constructDocumentTypeCriteria(criteria, childDocumentType);
            }
        }
    }

    private void addToFilterDescription(String filterDescription, String labelToAdd) {
        filterDescription += filterDescription.length() > 0 ? ", " : "";
        filterDescription += labelToAdd;
    }

    private static final String ACTION_LIST_COUNT_QUERY = "select count(distinct(ai.doc_hdr_id)) from krew_actn_itm_t ai where ai.PRNCPL_ID = ? and (ai.dlgn_typ is null or ai.dlgn_typ = 'P')";

    public int getCount(final String workflowId) {
        return (Integer)getPersistenceBrokerTemplate().execute(new PersistenceBrokerCallback() {
            public Object doInPersistenceBroker(PersistenceBroker broker) {
                PreparedStatement statement = null;
                ResultSet resultSet = null;
                try {
                    Connection connection = broker.serviceConnectionManager().getConnection();
                    statement = connection.prepareStatement(ACTION_LIST_COUNT_QUERY);
                    statement.setString(1, workflowId);
                    resultSet = statement.executeQuery();
                    if (!resultSet.next()) {
                        throw new WorkflowRuntimeException("Error determining Action List Count.");
                    }
                    return resultSet.getInt(1);
                } catch (SQLException e) {
                    throw new WorkflowRuntimeException("Error determining Action List Count.", e);
                } catch (LookupException e) {
                    throw new WorkflowRuntimeException("Error determining Action List Count.", e);
                } finally {
                    if (statement != null) {
                        try {
                            statement.close();
                        } catch (SQLException e) {}
                    }
                    if (resultSet != null) {
                        try {
                            resultSet.close();
                        } catch (SQLException e) {}
                    }
                }
            }
        });
    }

    private static final String MAX_ACTION_ITEM_DATE_ASSIGNED_AND_ACTION_LIST_COUNT_AND_QUERY = "select max(ASND_DT) as max_date, count(distinct(doc_hdr_id)) as total_records"
            + "  from ("
            + "       select ASND_DT,doc_hdr_id  "
            + "         from KREW_ACTN_ITM_T   where    prncpl_id=? "
            + "         group by  ASND_DT,doc_hdr_id "
            + "       ) T";


    /**
     * Gets the max action item id and count doe a given user.
     *
     * @return A List with the first value being the maxActionItemId and the second value being the count
     */
    public List<Object> getMaxActionItemDateAssignedAndCountForUser(final String principalId){
        return (List<Object>)getPersistenceBrokerTemplate().execute(new PersistenceBrokerCallback() {
            public Object doInPersistenceBroker(PersistenceBroker broker) {
                PreparedStatement statement = null;
                ResultSet resultSet = null;
                List<Object> result = new ArrayList<Object>();
                try {
                    Connection connection = broker.serviceConnectionManager().getConnection();
                    statement = connection.prepareStatement(MAX_ACTION_ITEM_DATE_ASSIGNED_AND_ACTION_LIST_COUNT_AND_QUERY);
                    statement.setString(1, principalId);
                    resultSet = statement.executeQuery();
                    if (!resultSet.next()) {
                        throw new WorkflowRuntimeException("Error determining Action List Count and Max Action Item Id.");
                    }
                    else{
                        result.add(resultSet.getTimestamp(1));
                        result.add(resultSet.getInt(2));
                    }
                    return result;
                } catch (SQLException e) {
                    throw new WorkflowRuntimeException("Error determining Action List Count and Max Action Item Id.", e);
                } catch (LookupException e) {
                    throw new WorkflowRuntimeException("Error determining Action List Count and Max Action Item Id.", e);
                } finally {
                    if (statement != null) {
                        try {
                            statement.close();
                        } catch (SQLException e) {}
                    }
                    if (resultSet != null) {
                        try {
                            resultSet.close();
                        } catch (SQLException e) {}
                    }
                }
            }
        });
    }

    /**
     * Creates an Action List from the given collection of Action Items.  The Action List should
     * contain only one action item per document.  The action item chosen should be the most "critical"
     * or "important" one on the document.
     *
     * @return the Action List as a Collection of ActionItems
     */
    private <T extends ActionItemActionListExtension> Collection<T> createActionListForUser(Collection<T> actionItems) {
        Map<String, T> actionItemMap = new HashMap<String, T>();
        ActionListPriorityComparator comparator = new ActionListPriorityComparator();
        for (T potentialActionItem: actionItems) {
            T existingActionItem = actionItemMap.get(potentialActionItem.getDocumentId());
            if (existingActionItem == null || comparator.compare(potentialActionItem, existingActionItem) > 0) {
                actionItemMap.put(potentialActionItem.getDocumentId(), potentialActionItem);
            }
        }
        return actionItemMap.values();
    }

    /**
     * Creates an Action List from the given collection of Action Items.  The Action List should
     * contain only one action item per user.  The action item chosen should be the most "critical"
     * or "important" one on the document.
     *
     * @return the Action List as a Collection of ActionItems
     */
    private Collection<ActionItemActionListExtension> createActionListForRouteHeader(Collection<ActionItemActionListExtension> actionItems) {
        Map<String, ActionItemActionListExtension> actionItemMap = new HashMap<String, ActionItemActionListExtension>();
        ActionListPriorityComparator comparator = new ActionListPriorityComparator();
        for (ActionItemActionListExtension potentialActionItem: actionItems) {
            ActionItemActionListExtension existingActionItem = actionItemMap.get(potentialActionItem.getPrincipalId());
            if (existingActionItem == null || comparator.compare(potentialActionItem, existingActionItem) > 0) {
                actionItemMap.put(potentialActionItem.getPrincipalId(), potentialActionItem);
            }
        }
        return actionItemMap.values();
    }

    private <T extends ActionItemActionListExtension> Collection<T> getActionItemsInActionList(Class<T> objectsToRetrieve, String principalId, ActionListFilter filter) {
        LOG.debug("getting action list for user " + principalId);
        Criteria crit = null;
        if (filter == null) {
            crit = new Criteria();
            crit.addEqualTo("principalId", principalId);
        } else {
            crit = setUpActionListCriteria(principalId, filter);
        }
        LOG.debug("running query to get action list for criteria " + crit);
        Collection<T> collection = this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(objectsToRetrieve, crit));
        LOG.debug("found " + collection.size() + " action items for user " + principalId);
        return createActionListForUser(collection);
    }

    public Collection<OutboxItemActionListExtension> getOutbox(String principalId, ActionListFilter filter) {
        return getActionItemsInActionList(OutboxItemActionListExtension.class, principalId, filter);
    }

    /**
     *
     * Deletes all outbox items specified by the list of ids
     *
     * @see org.kuali.rice.kew.actionlist.dao.ActionListDAO#removeOutboxItems(java.lang.String, java.util.List)
     */
    public void removeOutboxItems(String principalId, List<String> outboxItems) {
        Criteria crit = new Criteria();
        crit.addIn("id", outboxItems);
        getPersistenceBrokerTemplate().deleteByQuery(new QueryByCriteria(OutboxItemActionListExtension.class, crit));
    }

    /**
     * Saves an outbox item
     *
     * @see org.kuali.rice.kew.actionlist.dao.ActionListDAO#saveOutboxItem(org.kuali.rice.kew.actionitem.OutboxItemActionListExtension)
     */
    public void saveOutboxItem(OutboxItemActionListExtension outboxItem) {
        this.getPersistenceBrokerTemplate().store(outboxItem);
    }

    /**
     * Gets the outbox item associated with the document id
     *
     * @see org.kuali.rice.kew.actionlist.dao.ActionListDAO#getOutboxByDocumentId(java.lang.String)
     */
    public OutboxItemActionListExtension getOutboxByDocumentId(String documentId) {
        Criteria crit = new Criteria();
        crit.addEqualTo("documentId", documentId);
        return (OutboxItemActionListExtension)getPersistenceBrokerTemplate().getObjectByQuery(new QueryByCriteria(OutboxItemActionListExtension.class, crit));
    }

    public OutboxItemActionListExtension getOutboxByDocumentIdUserId(String documentId, String userId) {
        Criteria crit = new Criteria();
        crit.addEqualTo("documentId", documentId);
        crit.addEqualTo("principalId", userId);
        return (OutboxItemActionListExtension)getPersistenceBrokerTemplate().getObjectByQuery(new QueryByCriteria(OutboxItemActionListExtension.class, crit));
    }

    private Date beginningOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    private Date endOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

}
