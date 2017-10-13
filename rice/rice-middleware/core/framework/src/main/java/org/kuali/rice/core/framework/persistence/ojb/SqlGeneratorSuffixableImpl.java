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

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.ojb.broker.accesslayer.sql.SelectStatement;
import org.apache.ojb.broker.accesslayer.sql.SqlGeneratorDefaultImpl;
import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.platforms.Platform;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.util.logging.Logger;
import org.apache.ojb.broker.util.logging.LoggerFactory;

/**
 * SqlGeneratorDefaultImpl subclass that replaced the vanilla SqlSelectStatement implementation
 * with a new {@link SuffixedSqlSelectStatement} that is {@link SuffixableQueryByCriteria} - aware.
 * This class needs to be specified as the SqlGenerator implementation in the OJB properties, to replace
 * the SqlGeneratorDefaultImpl.
 * This is a hack to introduce select-for-update functionality into OJB so the same ORM/Criteria abstractions
 * can be retained for select-for-update queries.  Select for update appears to have been added in the OJB
 * source repository, so maybe a forthcoming release will include this functionality and these kludges can be
 * removed.
 * @see SuffixedSqlSelectStatement
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SqlGeneratorSuffixableImpl extends SqlGeneratorDefaultImpl {
    private Logger logger = LoggerFactory.getLogger(SqlGeneratorSuffixableImpl.class);

    public SqlGeneratorSuffixableImpl(Platform platform) {
        super(platform);
    }

    @Override
    public SelectStatement getPreparedSelectStatement(Query query, ClassDescriptor cld) {
        SelectStatement sql = new SuffixedSqlSelectStatement(getPlatform(), cld, query, logger);
        if (logger.isDebugEnabled()) {
            boolean masochisticSqlLogging = true;
            if ( masochisticSqlLogging ) {
            	logger.debug("SQL: " + sql.getStatement() + "\n" + query.getCriteria() + "\nFor platform: " + getPlatform().getClass().toString() + "\n" + ExceptionUtils.getStackTrace(new Throwable()));
            } else {
                logger.debug("SQL: " + sql.getStatement() );
            }
        }
        return sql;
    }
}
