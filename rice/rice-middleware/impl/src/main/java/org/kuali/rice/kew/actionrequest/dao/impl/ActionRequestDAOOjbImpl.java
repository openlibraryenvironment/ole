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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.broker.query.QueryFactory;
import org.apache.ojb.broker.query.ReportQueryByCriteria;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actionrequest.dao.ActionRequestDAO;
import org.kuali.rice.kew.api.action.ActionRequestStatus;
import org.kuali.rice.kew.api.action.RecipientType;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.api.KewApiConstants;
import org.springmodules.orm.ojb.support.PersistenceBrokerDaoSupport;


/**
 * OJB implementation of the {@link ActionRequestDAO}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ActionRequestDAOOjbImpl extends PersistenceBrokerDaoSupport implements ActionRequestDAO {

    public ActionRequestValue getActionRequestByActionRequestId(String actionRequestId) {
        Criteria crit = new Criteria();
        crit.addEqualTo("actionRequestId", actionRequestId);
        return (ActionRequestValue) this.getPersistenceBrokerTemplate().getObjectByQuery(new QueryByCriteria(ActionRequestValue.class, crit));
    }

    public void saveActionRequest(ActionRequestValue actionRequest) {
        if (actionRequest.getActionRequestId() == null) {
            loadDefaultValues(actionRequest);
        }
        if ( actionRequest.getAnnotation() != null && actionRequest.getAnnotation().length() > 2000 ) {
        	actionRequest.setAnnotation( StringUtils.abbreviate(actionRequest.getAnnotation(), 2000) );
        }
        this.getPersistenceBrokerTemplate().store(actionRequest);
    }

    public List<ActionRequestValue> findPendingByResponsibilityIds(Collection responsibilityIds) {
        if (responsibilityIds == null || responsibilityIds.size() == 0) return Collections.emptyList();
        Criteria crit = new Criteria();
        Criteria statusCriteria = new Criteria();
        Criteria activatedCriteria = new Criteria();
        activatedCriteria.addEqualTo("status", ActionRequestStatus.ACTIVATED.getCode());

        Criteria initializedCriteria = new Criteria();
        initializedCriteria.addEqualTo("status", ActionRequestStatus.INITIALIZED.getCode());

        statusCriteria.addOrCriteria(activatedCriteria);
        statusCriteria.addOrCriteria(initializedCriteria);
        crit.addAndCriteria(statusCriteria);
        crit.addIn("responsibilityId", responsibilityIds);
        return new ArrayList<ActionRequestValue>(this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(ActionRequestValue.class, crit)));
    }

    public List<ActionRequestValue> findPendingByActionRequestedAndDocId(String actionRequestedCd, String documentId) {
        Criteria crit = new Criteria();
        crit.addEqualTo("actionRequested", actionRequestedCd);
        crit.addEqualTo("documentId", documentId);
        crit.addEqualTo("currentIndicator", Boolean.TRUE);
        crit.addAndCriteria(getPendingCriteria());
        return new ArrayList<ActionRequestValue>(this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(ActionRequestValue.class, crit)));
    }

    @SuppressWarnings("unchecked")
    public List<ActionRequestValue> findByStatusAndDocId(String statusCd, String documentId) {
        Criteria crit = new Criteria();
        crit.addEqualTo("status", statusCd);
        crit.addEqualTo("documentId", documentId);
        crit.addEqualTo("currentIndicator", true);

        return (List<ActionRequestValue>) this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(ActionRequestValue.class, crit));
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
    private void checkNull(Object value, String valueName) throws RuntimeException {
        if (value == null) {
            throw new RuntimeException("Null value for " + valueName);
        }
    }

    public List<ActionRequestValue> findPendingRootRequestsByDocIdAtRouteLevel(String documentId, Integer routeLevel) {
        Criteria crit = new Criteria();
        crit.addEqualTo("routeLevel", routeLevel);
        crit.addNotEqualTo("status", ActionRequestStatus.DONE.getCode());
        crit.addEqualTo("documentId", documentId);
        crit.addEqualTo("currentIndicator", Boolean.TRUE);
        crit.addIsNull("parentActionRequest");
        return new ArrayList<ActionRequestValue>(this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(ActionRequestValue.class, crit)));
    }

    public List<ActionRequestValue> findPendingByDocIdAtOrBelowRouteLevel(String documentId, Integer routeLevel) {
        Criteria crit = new Criteria();
        crit.addLessOrEqualThan("routeLevel", routeLevel);
        crit.addNotEqualTo("status", ActionRequestStatus.DONE.getCode());
        crit.addEqualTo("documentId", documentId);
        crit.addEqualTo("currentIndicator", true);
        return new ArrayList<ActionRequestValue>(this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(ActionRequestValue.class, crit)));
    }

    public List<ActionRequestValue> findPendingRootRequestsByDocIdAtOrBelowRouteLevel(String documentId, Integer routeLevel) {
        Criteria crit = new Criteria();
        crit.addLessOrEqualThan("routeLevel", routeLevel);
        crit.addNotEqualTo("status", ActionRequestStatus.DONE.getCode());
        crit.addEqualTo("documentId", documentId);
        crit.addEqualTo("currentIndicator", true);
        crit.addIsNull("parentActionRequest");
        return new ArrayList<ActionRequestValue>(this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(ActionRequestValue.class, crit)));
    }

    public void delete(String actionRequestId) {
    	Criteria crit = new Criteria();
    	crit.addEqualTo("actionRequestId", actionRequestId);
    	this.getPersistenceBrokerTemplate().deleteByQuery(new QueryByCriteria(ActionRequestValue.class, crit));
    }

    public List<ActionRequestValue> findAllPendingByDocId(String documentId) {
        Criteria initializedStatCriteria = new Criteria();
        initializedStatCriteria.addEqualTo("status", ActionRequestStatus.INITIALIZED.getCode());

        Criteria activatedStatCriteria = new Criteria();
        activatedStatCriteria.addEqualTo("status", ActionRequestStatus.ACTIVATED.getCode());

        Criteria statusCriteria = new Criteria();
        statusCriteria.addOrCriteria(initializedStatCriteria);
        statusCriteria.addOrCriteria(activatedStatCriteria);

        Criteria crit = new Criteria();
        crit.addEqualTo("documentId", documentId);
        crit.addEqualTo("currentIndicator", true);
        crit.addAndCriteria(statusCriteria);

        return new ArrayList<ActionRequestValue>(this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(ActionRequestValue.class, crit)));
    }

    public List<ActionRequestValue> findAllByDocId(String documentId) {
        Criteria crit = new Criteria();
        crit.addEqualTo("documentId", documentId);
        crit.addEqualTo("currentIndicator", true);
        return new ArrayList<ActionRequestValue>(this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(ActionRequestValue.class, crit)));
    }

    public List<ActionRequestValue> findAllRootByDocId(String documentId) {
        Criteria crit = new Criteria();
        crit.addEqualTo("documentId", documentId);
        crit.addEqualTo("currentIndicator", true);
        crit.addIsNull("parentActionRequest");
        return new ArrayList<ActionRequestValue>(this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(ActionRequestValue.class, crit)));
    }

    public List<ActionRequestValue> findByDocumentIdIgnoreCurrentInd(String documentId) {
        Criteria crit = new Criteria();
        crit.addEqualTo("documentId", documentId);
        return new ArrayList<ActionRequestValue>(this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(ActionRequestValue.class, crit)));
    }

    
    private Criteria getPendingCriteria() {
        Criteria pendingCriteria = new Criteria();
        Criteria activatedCriteria = new Criteria();
        activatedCriteria.addEqualTo("status", ActionRequestStatus.ACTIVATED.getCode());
        Criteria initializedCriteria = new Criteria();
        initializedCriteria.addEqualTo("status", ActionRequestStatus.INITIALIZED.getCode());
        pendingCriteria.addOrCriteria(activatedCriteria);
        pendingCriteria.addOrCriteria(initializedCriteria);
        return pendingCriteria;
    }

    public  void deleteByDocumentId(String documentId){
        Criteria crit = new Criteria();
        crit.addEqualTo("documentId", documentId);
        this.getPersistenceBrokerTemplate().deleteByQuery(new QueryByCriteria(ActionRequestValue.class, crit));
    }

    public List<ActionRequestValue> findPendingRootRequestsByDocumentType(String documentTypeId) {
    	Criteria routeHeaderCrit = new Criteria();
    	routeHeaderCrit.addEqualTo("documentTypeId", documentTypeId);
    	Criteria crit = new Criteria();
    	crit.addIn("documentId", new ReportQueryByCriteria(DocumentRouteHeaderValue.class, new String[] {"documentId"}, routeHeaderCrit));
        crit.addAndCriteria(getPendingCriteria());
        crit.addEqualTo("currentIndicator", Boolean.TRUE);
        crit.addIsNull("parentActionRequest");
        return new ArrayList<ActionRequestValue>(this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(ActionRequestValue.class, crit)));
    }

    public List<ActionRequestValue> findPendingRootRequestsByDocIdAtRouteNode(String documentId, String nodeInstanceId) {
    	Criteria crit = new Criteria();
        crit.addEqualTo("documentId", documentId);
        crit.addAndCriteria(getPendingCriteria());
        crit.addEqualTo("currentIndicator", Boolean.TRUE);
        crit.addIsNull("parentActionRequest");
        crit.addEqualTo("nodeInstance.routeNodeInstanceId", nodeInstanceId);
        return new ArrayList<ActionRequestValue>(this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(ActionRequestValue.class, crit)));
    }

    public List<ActionRequestValue> findRootRequestsByDocIdAtRouteNode(String documentId, String nodeInstanceId) {
        Criteria crit = new Criteria();
        crit.addEqualTo("documentId", documentId);
        crit.addEqualTo("currentIndicator", Boolean.TRUE);
        crit.addIsNull("parentActionRequest");
        crit.addEqualTo("nodeInstance.routeNodeInstanceId", nodeInstanceId);
        return new ArrayList<ActionRequestValue>(this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(ActionRequestValue.class, crit)));
    }

    public boolean doesDocumentHaveUserRequest(String principalId, String documentId) {
    	Criteria crit = new Criteria();
    	crit.addEqualTo("documentId", documentId);
    	crit.addEqualTo("recipientTypeCd", RecipientType.PRINCIPAL.getCode());
    	crit.addEqualTo("principalId", principalId);
    	crit.addEqualTo("currentIndicator", Boolean.TRUE);
    	int count = getPersistenceBrokerTemplate().getCount(new QueryByCriteria(ActionRequestValue.class, crit));
    	return count > 0;
    }

    public List<String> getRequestGroupIds(String documentId) {
    	Criteria crit = new Criteria();
    	crit.addEqualTo("documentId", documentId);
    	crit.addEqualTo("recipientTypeCd", RecipientType.GROUP.getCode());
    	crit.addEqualTo("currentIndicator", Boolean.TRUE);

    	ReportQueryByCriteria query = QueryFactory.newReportQuery(ActionRequestValue.class, crit);
    	query.setAttributes(new String[] { "groupId" });

    	List<String> groupIds = new ArrayList<String>();
    	Iterator iter = getPersistenceBrokerTemplate().getReportQueryIteratorByQuery(query);
    	while (iter.hasNext()) {
			Object[] row = (Object[]) iter.next();
			String id = (String)row[0];
			groupIds.add(id);
		}
    	return groupIds;
    }

	/**
	 * @see org.kuali.rice.kew.actionrequest.dao.ActionRequestDAO#findActivatedByGroup(String)
	 */
	public List<ActionRequestValue> findActivatedByGroup(String groupId) {
        Criteria statusCriteria = new Criteria();
        statusCriteria.addEqualTo("status", ActionRequestStatus.ACTIVATED.getCode());
        Criteria crit = new Criteria();
        crit.addEqualTo("groupId", groupId);
        crit.addEqualTo("currentIndicator", true);
        crit.addAndCriteria(statusCriteria);

        return new ArrayList<ActionRequestValue>(this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(ActionRequestValue.class, crit)));
	}

    @Override
    public ActionRequestValue getRoleActionRequestByActionTakenId(String actionTakenId) {
        Criteria crit = new Criteria();
        crit.addEqualTo("actionTakenId", actionTakenId);
        crit.addEqualTo("currentIndicator", true);
        crit.addEqualTo("recipientTypeCd", RecipientType.ROLE.getCode());
        crit.addIsNull("parentActionRequest");
        return (ActionRequestValue) this.getPersistenceBrokerTemplate().getObjectByQuery(new QueryByCriteria(ActionRequestValue.class, crit));
    }
}
