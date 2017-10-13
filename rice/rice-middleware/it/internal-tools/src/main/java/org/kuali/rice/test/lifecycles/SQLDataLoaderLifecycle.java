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
package org.kuali.rice.test.lifecycles;

import org.apache.log4j.Logger;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.lifecycle.BaseLifecycle;
import org.kuali.rice.test.SQLDataLoader;

/**
 * A lifecycle for loading SQL datasets.
 * This lifecycle will not be run (even if it is listed in the lifecycles list)
 * if the 'use.sqlDataLoaderLifecycle' configuration property is defined, and is
 * not 'true'.  If the property is omitted the lifecycle runs as normal.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SQLDataLoaderLifecycle extends BaseLifecycle {
    private static final Logger LOG = Logger.getLogger(SQLDataLoaderLifecycle.class);

    private SQLDataLoader sqlDataLoader;

    private String filename;

    private String delimiter;

    public SQLDataLoaderLifecycle(String filename, String delimiter) {
        this.filename = filename;
        this.delimiter = delimiter;
    }

    public void start() throws Exception {
        String useSqlDataLoaderLifecycle = ConfigContext.getCurrentContextConfig().getProperty("use.sqlDataLoaderLifecycle");
        if (useSqlDataLoaderLifecycle != null && !Boolean.valueOf(useSqlDataLoaderLifecycle)) {
            LOG.debug("Skipping SQLDataLoaderLifecycle due to property: use.sqlDataLoaderLifecycle=" + useSqlDataLoaderLifecycle);
            return;
        }

        sqlDataLoader = new SQLDataLoader(filename, delimiter);
        sqlDataLoader.runSql();
        super.start();
    }

    public void stop() throws Exception {
        // TODO: may way to do something with the dataLoader
        super.stop();
    }
}
