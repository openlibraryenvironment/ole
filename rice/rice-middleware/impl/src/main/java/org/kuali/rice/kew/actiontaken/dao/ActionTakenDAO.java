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
package org.kuali.rice.kew.actiontaken.dao;

import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.api.action.ActionType;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;


/**
 * Data Access Object for {@link ActionTakenValue}s.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface ActionTakenDAO {

    public ActionTakenValue load(String id);

    public void saveActionTaken(ActionTakenValue actionTaken);

    public void deleteActionTaken(ActionTakenValue actionTaken);

    public ActionTakenValue findByActionTakenId(String actionTakenId);

    public Collection<ActionTakenValue> findByDocumentId(String documentId);

    public Collection<ActionTakenValue> findByDocIdAndAction(String docId, String action);

    public List<ActionTakenValue> findByDocumentIdWorkflowId(String documentId, String workflowId);

    public List findByDocumentIdIgnoreCurrentInd(String documentId);

    public void deleteByDocumentId(String documentId);

    public boolean hasUserTakenAction(String workflowId, String documentId);

    public Timestamp getLastActionTakenDate(String documentId, ActionType actionType);

}
