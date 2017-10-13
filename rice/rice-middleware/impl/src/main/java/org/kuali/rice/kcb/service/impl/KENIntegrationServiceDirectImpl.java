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
package org.kuali.rice.kcb.service.impl;

import java.util.Collection;

import javax.sql.DataSource;

import org.kuali.rice.kcb.service.KENIntegrationService;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Implements KEN integration by querying the database tables directly.  This avoids
 * a runtime dependence on KEN. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class KENIntegrationServiceDirectImpl implements KENIntegrationService {
    private DataSource datasource;

    /**
     * Sets the DataSource to query against
     * @param ds the DataSource to query against
     */
    @Required
    public void setDataSource(DataSource ds) {
        this.datasource = ds;
    }

    /**
     * @see org.kuali.rice.kcb.service.KENIntegrationService#getAllChannelNames()
     */
    public Collection<String> getAllChannelNames() {
        return new JdbcTemplate(datasource).queryForList("select distinct NM from KREN_CHNL_T", String.class);
    }
}
