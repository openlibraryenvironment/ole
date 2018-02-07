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
package org.kuali.rice.core.framework.persistence.jpa.criteria;

import javax.persistence.EntityManager;
import javax.persistence.Query;


/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class QueryByCriteria {

    private EntityManager entityManager;
    private Criteria criteria;
    private QueryByCriteriaType type;
    
    public enum QueryByCriteriaType {SELECT, UPDATE, DELETE}
    
    public QueryByCriteria(EntityManager entityManager, Criteria criteria) {
        this(entityManager, criteria, QueryByCriteriaType.SELECT);
    }

    public QueryByCriteria(EntityManager entityManager, Criteria criteria, QueryByCriteriaType type) {
        this.entityManager = entityManager;
        this.criteria = criteria;
        this.type = type;
    }

    public Query toQuery() {
        Query query = entityManager.createQuery(criteria.toQuery(type));
        if (criteria.getSearchLimit() != null) {
            query.setMaxResults(criteria.getSearchLimit());        	
        }
        criteria.prepareParameters(query);
        return query;
    }
    
    public Query toCountQuery() {
        Query query = entityManager.createQuery(criteria.toCountQuery());
        if (criteria.getSearchLimit() != null) {
            query.setMaxResults(criteria.getSearchLimit());        	
        }
        criteria.prepareParameters(query);
        return query;
    }

}
