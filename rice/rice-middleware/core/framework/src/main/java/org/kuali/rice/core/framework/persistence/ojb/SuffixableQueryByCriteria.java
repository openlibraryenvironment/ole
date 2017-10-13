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
package org.kuali.rice.core.framework.persistence.ojb;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;

/**
 * QueryByCriteria subclass that introduces a suffix that the SqlGenerator should
 * append onto the end of the generated sql statement.
 * This is a hack to introduce select-for-update functionality into OJB so the same ORM/Criteria abstractions
 * can be retained for select-for-update queries.  Select for update appears to have been added in the OJB
 * source repository, so maybe a forthcoming release will include this functionality and these kludges can be
 * removed.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SuffixableQueryByCriteria extends QueryByCriteria {
    protected String suffix;

    public SuffixableQueryByCriteria(Class targetClass, Criteria criteria, boolean distinct) {
        super(targetClass, criteria, distinct);
    }

    public SuffixableQueryByCriteria(Class targetClass, Criteria whereCriteria, Criteria havingCriteria, boolean distinct) {
        super(targetClass, whereCriteria, havingCriteria, distinct);
    }

    public SuffixableQueryByCriteria(Class targetClass, Criteria whereCriteria, Criteria havingCriteria) {
        super(targetClass, whereCriteria, havingCriteria);
    }

    public SuffixableQueryByCriteria(Class targetClass, Criteria criteria) {
        super(targetClass, criteria);
    }

    public SuffixableQueryByCriteria(Class classToSearchFrom) {
        super(classToSearchFrom);
    }

    public SuffixableQueryByCriteria(Object anObject, boolean distinct) {
        super(anObject, distinct);
    }

    public SuffixableQueryByCriteria(Object anObject) {
        super(anObject);
    }

    public void setQuerySuffix(String suffix) {
        this.suffix = suffix;
    }
    
    public String getQuerySuffix() {
        return suffix;
    }
}
