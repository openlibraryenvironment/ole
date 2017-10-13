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
package org.kuali.rice.coreservice.impl.component;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.kuali.rice.core.framework.persistence.ojb.DataAccessUtils;
import org.springmodules.orm.ojb.support.PersistenceBrokerDaoSupport;

/**
 * JDBC-based implementation of the {@code ComponentSetDao}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ComponentSetDaoOjbImpl extends PersistenceBrokerDaoSupport implements ComponentSetDao {

    @Override
    public ComponentSetBo getComponentSet(String componentSetId) {
        Criteria criteria = new Criteria();
	    criteria.addEqualTo("componentSetId", componentSetId);
	    return (ComponentSetBo) getPersistenceBrokerTemplate().getObjectByQuery(new QueryByCriteria(ComponentSetBo.class, criteria));
    }

    @Override
    public boolean saveIgnoreLockingFailure(ComponentSetBo componentSetBo) {
        try {
            getPersistenceBrokerTemplate().store(componentSetBo);
        } catch (RuntimeException e) {
            if (DataAccessUtils.isOptimisticLockFailure(e)) {
                return false;
            }
            throw e;
        }
        return true;
    }
}
