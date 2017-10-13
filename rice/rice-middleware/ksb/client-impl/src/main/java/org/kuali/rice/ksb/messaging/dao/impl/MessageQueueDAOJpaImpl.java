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

import org.kuali.rice.core.api.config.CoreConfigHelper;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.util.RiceUtilities;
import org.kuali.rice.core.framework.persistence.jpa.criteria.Criteria;
import org.kuali.rice.core.framework.persistence.jpa.criteria.QueryByCriteria;
import org.kuali.rice.ksb.messaging.PersistedMessageBO;
import org.kuali.rice.ksb.messaging.PersistedMessagePayload;
import org.kuali.rice.ksb.messaging.dao.MessageQueueDAO;
import org.kuali.rice.ksb.util.KSBConstants;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.namespace.QName;
import java.util.List;
import java.util.Map;


public class MessageQueueDAOJpaImpl implements MessageQueueDAO {
    
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(MessageQueueDAOJpaImpl.class);
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @SuppressWarnings("unchecked")
    public List<PersistedMessageBO> findAll() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Returning all persisted messages");
        }
        
        Query query = entityManager.createNamedQuery("PersistedMessageBO.FindAll");
        return (List<PersistedMessageBO>) query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<PersistedMessageBO> findAll(int maxRows) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finding next " + maxRows + " messages");
        }
        
        Query query = entityManager.createNamedQuery("PersistedMessageBO.FindAll");
        query.setMaxResults(maxRows);
        
        return (List<PersistedMessageBO>) query.getResultList();
    }

    
    public PersistedMessagePayload findByPersistedMessageByRouteQueueId(Long routeQueueId) {
        return (PersistedMessagePayload) entityManager.find(PersistedMessagePayload.class, routeQueueId);
    }

    
    public PersistedMessageBO findByRouteQueueId(Long routeQueueId) {
        return (PersistedMessageBO) entityManager.find(PersistedMessageBO.class, routeQueueId);
    }

    @SuppressWarnings("unchecked")
    public List<PersistedMessageBO> findByServiceName(QName serviceName, String methodName) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finding messages for service name " + serviceName);
        }
        
        Query query = entityManager.createNamedQuery("PersistedMessageBO.FindByServiceName");
        query.setParameter("serviceName", serviceName.toString());
        query.setParameter("methodName", methodName);
        
        return (List<PersistedMessageBO>) query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<PersistedMessageBO> findByValues(Map<String, String> criteriaValues, int maxRows) {
        Criteria criteria = new Criteria(PersistedMessageBO.class.getName());
        for(Map.Entry<String, String> entry : criteriaValues.entrySet()) {
            criteria.eq(entry.getKey(), entry.getValue());
        }
        
        QueryByCriteria query = new QueryByCriteria(entityManager, criteria);
        
        return query.toQuery().getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<PersistedMessageBO> getNextDocuments(Integer maxDocuments) {
        String applicationId = CoreConfigHelper.getApplicationId();
        
        Query query = entityManager.createNamedQuery("PersistedMessageBO.GetNextDocuments");
        query.setParameter("applicationId", applicationId);
        query.setParameter("queueStatus", KSBConstants.ROUTE_QUEUE_EXCEPTION);
        query.setParameter("ipNumber", RiceUtilities.getIpNumber());
        
        if (maxDocuments != null)
            query.setMaxResults(maxDocuments);
        
        return (List<PersistedMessageBO>) query.getResultList();
    }

    
    public void remove(PersistedMessageBO routeQueue) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Removing message " + routeQueue);
        }
        if (routeQueue.getRouteQueueId() == null) {
            throw new RiceRuntimeException("can't delete a PersistedMessageBO with no id");
        }
        
        routeQueue = entityManager.merge(routeQueue);
        entityManager.remove(routeQueue);

        if (routeQueue.getPayload() != null) {
            PersistedMessagePayload payload =  entityManager.merge(routeQueue.getPayload());
            entityManager.remove(payload);
        }
    }

    
    public void save(PersistedMessageBO routeQueue) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Persisting message " + routeQueue);
        }
        PersistedMessageBO jpaInstance = entityManager.merge(routeQueue);
        Long routeQueueId = jpaInstance.getRouteQueueId();
        Integer verNo = jpaInstance.getLockVerNbr();
        routeQueue.setRouteQueueId(routeQueueId);
        routeQueue.setLockVerNbr(verNo);

        if (routeQueue.getPayload() != null) {
            routeQueue.getPayload().setRouteQueueId(routeQueueId);
            entityManager.merge(routeQueue.getPayload());
        }
    }

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
