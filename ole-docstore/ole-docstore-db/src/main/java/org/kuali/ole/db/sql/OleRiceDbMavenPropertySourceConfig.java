/**
 * Copyright 2010-2013 The Kuali Foundation
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
package org.kuali.ole.db.sql;

import org.kuali.common.jdbc.JdbcProjectContext;
import org.kuali.common.util.ProjectContext;
import org.kuali.common.util.property.ProjectProperties;
import org.kuali.common.util.spring.ConfigUtils;
import org.kuali.common.util.spring.MavenPropertySourceConfig;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * This lets properties defined in the pom override properties defined elsewhere. System/environment properties still override everything.
 */
@Configuration
public class OleRiceDbMavenPropertySourceConfig extends MavenPropertySourceConfig {

    @Override
    protected List<ProjectProperties> getOtherProjectProperties() {
        ProjectContext jdbc = new JdbcProjectContext();
        ProjectContext oleRiceDb = new OleRiceDbProjectContext();
        return ConfigUtils.getProjectProperties(jdbc, oleRiceDb);
    }

}
