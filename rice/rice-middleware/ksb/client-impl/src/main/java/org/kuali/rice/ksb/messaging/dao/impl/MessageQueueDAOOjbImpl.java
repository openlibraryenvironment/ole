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
package org.kuali.rice.ksb.messaging.dao.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.kuali.rice.core.api.config.CoreConfigHelper;
import org.kuali.rice.core.api.util.RiceUtilities;
import org.kuali.rice.ksb.messaging.PersistedMessageBO;
import org.kuali.rice.ksb.messaging.PersistedMessagePayload;
import org.kuali.rice.ksb.messaging.dao.MessageQueueDAO;
import org.kuali.rice.ksb.util.KSBConstants;
import org.springmodules.orm.ojb.support.PersistenceBrokerDaoSupport;

import javax.xml.namespace.QName;
import java.util.List;
import java.util.Map;


public class MessageQueueDAOOjbImpl extends PersistenceBrokerDaoSupport implements MessageQueueDAO {

	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(MessageQueueDAOOjbImpl.class);

	public void remove(PersistedMessageBO routeQueue) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Removing message " + routeQueue);
		}
		Criteria crit = new Criteria();
		crit.addEqualTo("routeQueueId", routeQueue.getRouteQueueId());
		getPersistenceBrokerTemplate().deleteByQuery(new QueryByCriteria(PersistedMessageBO.class, crit));

		crit = new Criteria();
		crit.addEqualTo("routeQueueId", routeQueue.getPayload().getRouteQueueId());
		getPersistenceBrokerTemplate().deleteByQuery(new QueryByCriteria(PersistedMessagePayload.class, crit));
	}

	public void save(PersistedMessageBO routeQueue) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Persisting message " + routeQueue);
		}
		getPersistenceBrokerTemplate().store(routeQueue);
		routeQueue.getPayload().setRouteQueueId(routeQueue.getRouteQueueId());
		getPersistenceBrokerTemplate().store(routeQueue.getPayload());
	}

	@SuppressWarnings("unchecked")
	public List<PersistedMessageBO> findAll() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Returning all persisted messages");
		}
	return (List<PersistedMessageBO>) getPersistenceBrokerTemplate().getCollectionByQuery(
		new QueryByCriteria(PersistedMessageBO.class));
	}

	@SuppressWarnings("unchecked")
	public List<PersistedMessageBO> findAll(int maxRows) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Finding next " + maxRows + " messages");
		}
		QueryByCriteria query = new QueryByCriteria(PersistedMessageBO.class);
		query.setStartAtIndex(0);
		query.setEndAtIndex(maxRows);
		return (List<PersistedMessageBO>) getPersistenceBrokerTemplate().getCollectionByQuery(query);
	}

	@SuppressWarnings("unchecked")
    public List<PersistedMessageBO> findByValues(Map<String, String> criteriaValues, int maxRows) {
		Criteria crit = new Criteria();
		String value = null;
		for (String key : criteriaValues.keySet()) {
			value = criteriaValues.get(key);
			if (StringUtils.isBlank(key) && StringUtils.isBlank(value)) {
		throw new IllegalArgumentException("Either the key or value was blank in criteriaValues (" + key + "="
			+ value + ")");
			}

			// auto-wildcard the statement
	    if (!key.equals("routeQueueId")) {
			if (value.contains("*")) {
				value = value.replace("*", "%");
			} else {
				value = value.concat("%");
			}
	    }
		if (!StringUtils.containsOnly(value, "%")) {
			crit.addLike(key, value);
		}
	}
	QueryByCriteria query = new QueryByCriteria(PersistedMessageBO.class, crit);
	query.setFetchSize(maxRows);
	query.setStartAtIndex(0);
	query.setEndAtIndex(maxRows);
	return (List<PersistedMessageBO>) getPersistenceBrokerTemplate().getCollectionByQuery(query);
    }

	public PersistedMessageBO findByRouteQueueId(Long routeQueueId) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("routeQueueId", routeQueueId);
	return (PersistedMessageBO) getPersistenceBrokerTemplate().getObjectByQuery(
		new QueryByCriteria(PersistedMessageBO.class, criteria));
	}

    public PersistedMessagePayload findByPersistedMessageByRouteQueueId(Long routeQueueId) {
	Criteria criteria = new Criteria();
	criteria.addEqualTo("routeQueueId", routeQueueId);
	return (PersistedMessagePayload) getPersistenceBrokerTemplate().getObjectByQuery(
		new QueryByCriteria(PersistedMessagePayload.class, criteria));
	}

	@SuppressWarnings("unchecked")
	public List<PersistedMessageBO> getNextDocuments(Integer maxDocuments) {
		Criteria crit = new Criteria();
		String applicationId = CoreConfigHelper.getApplicationId();
		crit.addEqualTo("applicationId", applicationId);
		crit.addNotEqualTo("queueStatus", KSBConstants.ROUTE_QUEUE_EXCEPTION);
		crit.addEqualTo("ipNumber", RiceUtilities.getIpNumber());

		QueryByCriteria query = new QueryByCriteria(PersistedMessageBO.class, crit);
		query.addOrderByAscending("queuePriority");
		query.addOrderByAscending("routeQueueId");
		query.addOrderByAscending("queueDate");
		if (maxDocuments != null)
			query.setEndAtIndex(maxDocuments.intValue());
		return (List) getPersistenceBrokerTemplate().getCollectionByQuery(query);
	}

	@SuppressWarnings("unchecked")
	public List<PersistedMessageBO> findByServiceName(QName serviceName, String methodName) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Finding messages for service name " + serviceName);
		}
		Criteria crit = new Criteria();
		crit.addEqualTo("serviceName", serviceName.toString());
		crit.addEqualTo("methodName", methodName);
	return (List<PersistedMessageBO>) getPersistenceBrokerTemplate().getCollectionByQuery(
		new QueryByCriteria(PersistedMessageBO.class, crit));
	}

}
