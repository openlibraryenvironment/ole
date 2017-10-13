/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.test.data;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.test.SQLDataLoader;

/**
 * Utilities for unit test data annotations. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public final class UnitTestDataUtils {

	private UnitTestDataUtils() {
		throw new UnsupportedOperationException("do not call");
	}
	
    public static void executeDataLoader(UnitTestData[] data) throws Exception {
        for (UnitTestData d: data) {
            executeDataLoader(d);
        }
    }

    public static void executeDataLoader(UnitTestData data) throws Exception {
        SQLDataLoader sqlDataLoader;
        for (UnitTestData.Type type : data.order()) {
            switch (type) {
                case SQL_FILES : 
                    for (UnitTestFile file : data.sqlFiles()) {
                        sqlDataLoader = new SQLDataLoader(file.filename(), file.delimiter());
                        sqlDataLoader.runSql();
                    }
                    break;
                case SQL_STATEMENTS : 
                    for (UnitTestSql statement : data.sqlStatements()) {
                        sqlDataLoader = new SQLDataLoader(statement.value());
                        sqlDataLoader.runSql();
                    }
                    break;
                default: break;
            }
        }
        
        if (!StringUtils.isEmpty(data.filename())) {
            if (!StringUtils.isEmpty(data.value()))
                throw new RuntimeException("UnitTestDataArtifact may not specify both SQL file and content");
            sqlDataLoader = new SQLDataLoader(data.filename(), data.delimiter());
            sqlDataLoader.runSql();
        } else if (!StringUtils.isEmpty(data.value())) {
            sqlDataLoader = new SQLDataLoader(data.value());
            sqlDataLoader.runSql();
        }
    }
}
