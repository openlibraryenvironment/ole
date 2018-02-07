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

import org.apache.ojb.broker.accesslayer.sql.SqlQueryStatement;
import org.apache.ojb.broker.accesslayer.sql.SqlSelectStatement;
import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.platforms.Platform;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.util.logging.Logger;

/**
 * A SqlSelectStatement sublclass that is aware of a special {@link SuffixableQueryByCriteria} Criteria
 * class and will append a suffix specified by that class of criteria to the generated SQL statement.
 * This is a hack to introduce select-for-update functionality into OJB so the same ORM/Criteria abstractions
 * can be retained for select-for-update queries.  Select for update appears to have been added in the OJB
 * source repository, so maybe a forthcoming release will include this functionality and these kludges can be
 * removed.
 * @see SqlGeneratorSuffixableImpl
 * @see SuffixableQueryByCriteria
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SuffixedSqlSelectStatement extends SqlSelectStatement {

    public SuffixedSqlSelectStatement(Platform pf, ClassDescriptor cld, Query query, Logger logger) {
        super(pf, cld, query, logger);
    }

    public SuffixedSqlSelectStatement(SqlQueryStatement parent, Platform pf, ClassDescriptor cld, Query query,
            Logger logger) {
        super(parent, pf, cld, query, logger);
    }

    @Override
    protected String buildStatement() {
        String stmt = super.buildStatement();
        Query query = this.getQuery();
        if (query instanceof SuffixableQueryByCriteria) {
            stmt += " " + ((SuffixableQueryByCriteria) query).getQuerySuffix();
        }
        return stmt;
    }
}
