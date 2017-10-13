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
package org.kuali.rice.krad.criteria;

import org.kuali.rice.core.api.criteria.CriteriaLookupService;
import org.kuali.rice.core.api.criteria.GenericQueryResults;
import org.kuali.rice.core.api.criteria.LookupCustomizer;
import org.kuali.rice.core.api.criteria.QueryByCriteria;

public class CriteriaLookupServiceImpl implements CriteriaLookupService {
    CriteriaLookupDao criteriaLookupDao;


    public void setCriteriaLookupDao(CriteriaLookupDao criteriaLookupDao) {
        this.criteriaLookupDao = criteriaLookupDao;
    }

    @Override
    public <T> GenericQueryResults<T> lookup(Class<T> queryClass, QueryByCriteria criteria) {
        return criteriaLookupDao.lookup(queryClass, criteria);
    }

    @Override
    public <T> GenericQueryResults<T> lookup(Class<T> queryClass, QueryByCriteria criteria,
            LookupCustomizer<T> customizer) {
        return criteriaLookupDao.lookup(queryClass, criteria, customizer);
    }
}
