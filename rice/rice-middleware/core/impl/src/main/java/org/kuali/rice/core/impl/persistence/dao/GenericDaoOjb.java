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
package org.kuali.rice.core.impl.persistence.dao;

import org.apache.log4j.Logger;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.broker.query.QueryFactory;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.framework.persistence.dao.GenericDao;
import org.kuali.rice.core.framework.persistence.ojb.SuffixableQueryByCriteria;
import org.kuali.rice.core.framework.persistence.platform.DatabasePlatform;
import org.springframework.dao.DataAccessException;
import org.springmodules.orm.ojb.support.PersistenceBrokerDaoSupport;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class is the OJB implementation of the GenericDao interface. This
 * class was adapted from the Kuali Nervous System
 * (org.kuali.rice.krad.dao.impl.GenericDaoOjb).
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class GenericDaoOjb extends PersistenceBrokerDaoSupport implements GenericDao {
    private static final Logger LOG = Logger.getLogger(GenericDaoOjb.class);

    private boolean useSelectForUpdate = true;

    /**
     * @param selectForUpdate whether to use select for update to implement pessimistic locking (testing/debugging purposes only)
     */
    public void setUseSelectForUpdate(boolean useSelectForUpdate) {
        this.useSelectForUpdate = useSelectForUpdate;
    }

    /**
     * @see org.kuali.rice.core.framework.persistence.dao.GenericDao#findById(Class, Object)
     */
    public Object findById(Class clazz, Object id) {
        return getPersistenceBrokerTemplate().getObjectById(clazz, id);
    }

    /**
     * @see org.kuali.rice.core.framework.persistence.dao.GenericDao#findByPrimaryKey(java.lang.Class, java.util.Map)
     */
    public Object findByPrimaryKey(Class clazz, Map primaryKeys) {
        Criteria criteria = buildCriteria(primaryKeys);

        return getPersistenceBrokerTemplate().getObjectByQuery(QueryFactory.newQuery(clazz, criteria));
    }

    /**
     * @see org.kuali.rice.core.framework.persistence.dao.GenericDao#findByUniqueKey(java.lang.Class, java.util.Map)
     */
    public Object findByUniqueKey(Class clazz, Map uniqueKeys) {
        Criteria criteria = buildCriteria(uniqueKeys);

        return getPersistenceBrokerTemplate().getObjectByQuery(QueryFactory.newQuery(clazz, criteria));
    }

    /**
     * @see org.kuali.rice.core.framework.persistence.dao.GenericDao#findAll(java.lang.Class)
     */
    public Collection findAll(Class clazz) {
        return getPersistenceBrokerTemplate().getCollectionByQuery(QueryFactory.newQuery(clazz, (Criteria) null));
    }

    /**
     * @see org.kuali.rice.core.framework.persistence.dao.GenericDao#findAllOrderBy(java.lang.Class, java.lang.String, boolean)
     */
    public Collection findAllOrderBy(Class clazz, String sortField,
            boolean sortAscending) {
        QueryByCriteria queryByCriteria = new QueryByCriteria(clazz, (Criteria) null);

        if (sortAscending) {
            queryByCriteria.addOrderByAscending(sortField);
        } else {
            queryByCriteria.addOrderByDescending(sortField);
        }

        return getPersistenceBrokerTemplate().getCollectionByQuery(
                queryByCriteria);
    }

    /**
     * @see org.kuali.rice.core.framework.persistence.dao.GenericDao#findMatching(java.lang.Class, java.util.Map)
     */
    public Collection findMatching(Class clazz, Map fieldValues) {
        Criteria criteria = buildCriteria(fieldValues);

        return getPersistenceBrokerTemplate().getCollectionByQuery(QueryFactory.newQuery(clazz, criteria));
    }

    /**
     * @see org.kuali.rice.core.framework.persistence.dao.GenericDao#countMatching(java.lang.Class, java.util.Map)
     */
    public int countMatching(Class clazz, Map fieldValues) {
        Criteria criteria = buildCriteria(fieldValues);

        return getPersistenceBrokerTemplate().getCount(QueryFactory.newQuery(clazz, criteria));
    }

    /**
     * @see org.kuali.rice.core.framework.persistence.dao.GenericDao#countMatching(java.lang.Class, java.util.Map, java.util.Map)
     */
    public int countMatching(Class clazz, Map positiveFieldValues,
            Map negativeFieldValues) {
        Criteria criteria = buildCriteria(positiveFieldValues);
        Criteria negativeCriteria = buildNegativeCriteria(negativeFieldValues);
        criteria.addAndCriteria(negativeCriteria);
        return getPersistenceBrokerTemplate().getCount(QueryFactory.newQuery(clazz, criteria));
    }

    /**
     * @see org.kuali.rice.core.framework.persistence.dao.GenericDao#findMatchingOrderBy(java.lang.Class, java.util.Map, java.lang.String, boolean)
     */
    public Collection findMatchingOrderBy(Class clazz, Map fieldValues,
            String sortField, boolean sortAscending) {
        Criteria criteria = buildCriteria(fieldValues);
        QueryByCriteria queryByCriteria = new QueryByCriteria(clazz, criteria);

        if (sortAscending) {
            queryByCriteria.addOrderByAscending(sortField);
        } else {
            queryByCriteria.addOrderByDescending(sortField);
        }

        return getPersistenceBrokerTemplate().getCollectionByQuery(queryByCriteria);
    }

    /**
     * @see org.kuali.rice.core.framework.persistence.dao.GenericDao#save(java.lang.Object)
     */
    public void save(Object bo) throws DataAccessException {
        getPersistenceBrokerTemplate().store(bo);
    }

    /**
     * @see org.kuali.rice.core.framework.persistence.dao.GenericDao#save(java.util.List)
     */
    public void save(List businessObjects) throws DataAccessException {
        for (Iterator i = businessObjects.iterator(); i.hasNext();) {
            Object bo = i.next();
            getPersistenceBrokerTemplate().store(bo);
        }
    }

    /**
     * @see org.kuali.rice.core.framework.persistence.dao.GenericDao#delete(java.lang.Object)
     */
    public void delete(Object bo) {
        getPersistenceBrokerTemplate().delete(bo);
    }

    /**
     * @see org.kuali.rice.core.framework.persistence.dao.GenericDao#delete(java.util.List)
     */
    public void delete(List<Object> boList) {
        for (Object bo : boList) {
            getPersistenceBrokerTemplate().delete(bo);
        }
    }

    /**
     * @see org.kuali.rice.core.framework.persistence.dao.GenericDao#deleteMatching(java.lang.Class, java.util.Map)
     */
    public void deleteMatching(Class clazz, Map fieldValues) {
        Criteria criteria = buildCriteria(fieldValues);

        getPersistenceBrokerTemplate().deleteByQuery(QueryFactory.newQuery(clazz, criteria));

        // An ojb delete by query doesn't update the cache so we need to clear
        // the cache for everything to work property.
        // don't believe me? Read the source code to OJB
        getPersistenceBrokerTemplate().clearCache();
    }

    /**
     * @see org.kuali.rice.core.framework.persistence.dao.GenericDao#retrieve(java.lang.Object)
     */
    public Object retrieve(Object object) {
        return getPersistenceBrokerTemplate().getObjectByQuery(QueryFactory.newQueryByIdentity(object));
    }

    /**
     * @see org.kuali.rice.core.framework.persistence.dao.GenericDao#findMatchingByExample(java.lang.Object)
     */
    public Collection findMatchingByExample(Object object) {
        return getPersistenceBrokerTemplate().getCollectionByQuery(QueryFactory.newQueryByExample(object));
    }

    /**
     * @see org.kuali.rice.core.framework.persistence.dao.GenericDao#findMatching(java.lang.Class, org.apache.ojb.broker.query.Criteria)
     */
    public Collection findMatching(Class clazz, Criteria criteria) {
        return findMatching(clazz, criteria, false, RiceConstants.NO_WAIT);
        /*return getPersistenceBrokerTemplate().getCollectionByQuery(
        QueryFactory.newQuery(clazz, criteria));*/
    }

    /**
     * @see org.kuali.rice.core.framework.persistence.dao.GenericDao#findMatching(Class, Criteria, boolean)
     */
    public Collection findMatching(Class clazz, Criteria criteria, boolean selectForUpdate, long wait) {
        Query query;
        if (selectForUpdate && !useSelectForUpdate) {
            LOG.warn("Pessimistic locking was requested but select for update is disabled");
        }
        if (selectForUpdate && useSelectForUpdate) {
            SuffixableQueryByCriteria q = new SuffixableQueryByCriteria(clazz, criteria);
            // XXX: hax
            Config config = ConfigContext.getCurrentContextConfig();
            DatabasePlatform platform = null;
            try {
                platform = (DatabasePlatform) Class.forName(config.getProperty(Config.DATASOURCE_PLATFORM)).newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
            q.setQuerySuffix(" " + platform.getSelectForUpdateSuffix(wait));
            query = q;            
        } else {
            query = QueryFactory.newQuery(clazz, criteria);
        }
        return getPersistenceBrokerTemplate().getCollectionByQuery(query);

    }

    /**
     * This method will build out criteria in the key-value paradigm
     * (attribute-value).
     * 
     * @param fieldValues
     * @return
     */
    private Criteria buildCriteria(Map fieldValues) {
        Criteria criteria = new Criteria();
        for (Iterator i = fieldValues.entrySet().iterator(); i.hasNext();) {
            Map.Entry e = (Map.Entry) i.next();

            String key = (String) e.getKey();
            Object value = e.getValue();
            if (value instanceof Collection) {
                criteria.addIn(key, (Collection) value);
            } else {
                criteria.addEqualTo(key, value);
            }
        }

        return criteria;
    }

    /**
     * This method will build out criteria in the key-value paradigm
     * (attribute-value).
     * 
     * @param fieldValues
     * @return
     */
    private Criteria buildNegativeCriteria(Map negativeFieldValues) {
        Criteria criteria = new Criteria();
        for (Iterator i = negativeFieldValues.entrySet().iterator(); i.hasNext();) {
            Map.Entry e = (Map.Entry) i.next();

            String key = (String) e.getKey();
            Object value = e.getValue();
            if (value instanceof Collection) {
                criteria.addNotIn(key, (Collection) value);
            } else {
                criteria.addNotEqualTo(key, value);
            }
        }

        return criteria;
    }

    /**
     * This overridden method ...
     *
     * @see org.kuali.rice.core.framework.persistence.dao.GenericDao#findMatching(java.lang.Class, org.kuali.rice.core.framework.persistence.jpa.criteria.Criteria)
     */
    @Override
    public Collection findMatching(Class clazz,
            org.kuali.rice.core.framework.persistence.jpa.criteria.Criteria criteria) {
        // TODO g1zhang - THIS METHOD NEEDS JAVADOCS
        return null;
    }

    /**
     * This overridden method ...
     *
     * @see org.kuali.rice.core.framework.persistence.dao.GenericDao#findMatching(java.lang.Class, org.kuali.rice.core.framework.persistence.jpa.criteria.Criteria, boolean, long)
     */
    @Override
    public Collection findMatching(Class clazz,
            org.kuali.rice.core.framework.persistence.jpa.criteria.Criteria criteria,
            boolean selectForUpdate, long wait) {
        // TODO g1zhang - THIS METHOD NEEDS JAVADOCS
        return null;
    }


    /**
     * This overridden method ...
     *
     * @see org.kuali.rice.core.framework.persistence.dao.GenericDao#findMatching(java.lang.Class, java.util.Map, boolean, long)
     */
    @Override
    public Collection findMatching(Class clazz, Map criteria,
            boolean selectForUpdate, long wait) {

        LOG.info("*******************************calling GenericDaoOjb.findMatching(Class clazz, Map criteria, boolean selectForUpdate, long wait)");
        Criteria c = buildCriteria(criteria);
        return findMatching(clazz, c, selectForUpdate, wait);
    }
}
