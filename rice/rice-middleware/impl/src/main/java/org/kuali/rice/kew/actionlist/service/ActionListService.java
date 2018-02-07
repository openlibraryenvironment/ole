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
package org.kuali.rice.kew.actionlist.service;

import java.util.Collection;
import java.util.List;

import org.kuali.rice.kew.actionitem.ActionItem;
import org.kuali.rice.kew.actionitem.ActionItemActionListExtension;
import org.kuali.rice.kew.actionitem.OutboxItemActionListExtension;
import org.kuali.rice.kew.actionlist.ActionListFilter;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actionrequest.Recipient;




/**
 * Main service for doing action list data access work
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface ActionListService {
    public ActionItem createActionItemForActionRequest(ActionRequestValue actionRequest);

    public Collection<ActionItemActionListExtension> getActionList(String principalId, ActionListFilter filter);

    public Collection<ActionItemActionListExtension> getActionListForSingleDocument(String documentId);

    /**
     * Returns a list of recipients <i>which secondary-delegate to</i> the target principalId
     * @param principalId the target principalId/delegate
     * @return a list of recipients <i>which secondary-delegate to</i> the target principalId
     */
    public Collection<Recipient> findUserSecondaryDelegators(String principalId);

    /**
     * Retruns a list of recipients <i>which are primary-delegated to by</i> the source principalId
     * @param principalId the source principalId to query for primary delegates
     * @return a list of recipients <i>which are primary-delegated to by</i> the source principalId
     */
    public Collection<Recipient> findUserPrimaryDelegations(String principalId);

    public void saveActionItem(ActionItem actionItem);

    public void deleteActionItem(ActionItem actionItem);

    public void deleteActionItem(ActionItem actionItem, boolean forceIntoOutbox);

    public void deleteByDocumentId(String documentId);

    public Collection<ActionItem> findByPrincipalId(String principalId);

    public Collection<ActionItem> findByWorkflowUserDocumentId(String workflowUserId, String documentId);

    public Collection<ActionItem> findByDocumentId(String documentId);

    public Collection<ActionItem> findByDocumentTypeName(String documentTypeName);

    public void updateActionItemsForTitleChange(String documentId, String newTitle);

    public void validateActionItem(ActionItem actionItem);

    public ActionItem findByActionItemId(String actionItemId);

    /**
     * Retrieves the number of Action List items in the given user's primary Action List (does not include secondary delegations)
     */
    public int getCount(String principalId);

    /**
     * Retrieves the max action item Id  and the total number of action items for the given user's primary Action List
     * (does not include secondary delegations)
     * @param principalId
     */
    public List<Object> getMaxActionItemDateAssignedAndCountForUser(String principalId);

    public Collection<ActionItem> findByActionRequestId(String actionRequestId);

    /**
     *
     * Retrieves {@link OutboxItemActionListExtension} items for the given user
     *
     * @param principalId
     * @param filter
     * @return
     */
    public Collection<OutboxItemActionListExtension> getOutbox(String principalId, ActionListFilter filter);
    public Collection<OutboxItemActionListExtension> getOutboxItemsByDocumentType(String documentTypeName);
    public void removeOutboxItems(String principalId, List<String> outboxItems);
    public void saveOutboxItem(ActionItem actionItem);
    public void saveOutboxItem(ActionItem actionItem, boolean forceIntoOutbox);
}
