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
package org.kuali.rice.krms.config;

import org.kuali.rice.core.api.config.module.RunMode;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.framework.config.module.ModuleConfigurer;
import org.kuali.rice.krms.api.KrmsConstants;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class handles the Spring based KRMS configuration that is part of the Rice Configurer that must 
 * exist in all Rice based systems and clients. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class KRMSConfigurer extends ModuleConfigurer {
    public static final String KRMS_DATASOURCE_OBJ = "krms.datasource";
    private static final String KRMS_SPRING_LOCAL_BEANS_PATH = "classpath:org/kuali/rice/krms/config/KRMSLocalSpringBeans.xml";
    private static final String KRMS_SPRING_REMOTE_BEANS_PATH = "classpath:org/kuali/rice/krms/config/KRMSRemoteSpringBeans.xml";
    private DataSource dataSource;

	public KRMSConfigurer() {
        super(KrmsConstants.Namespaces.MODULE_NAME);
        setValidRunModes(Arrays.asList(RunMode.REMOTE, RunMode.LOCAL));
    }

    @Override
    public void addAdditonalToConfig() {
        configureDataSource();
    }

    private void configureDataSource() {
        if (getDataSource() != null) {
            ConfigContext.getCurrentContextConfig().putObject(KRMS_DATASOURCE_OBJ, getDataSource());
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
	public List<String> getPrimarySpringFiles() {
        List<String> springFileLocations = new ArrayList<String>();
        if (RunMode.REMOTE == getRunMode()) {
            springFileLocations.add(KRMS_SPRING_REMOTE_BEANS_PATH);
        } else if (RunMode.LOCAL == getRunMode()) {
            springFileLocations.add(KRMS_SPRING_LOCAL_BEANS_PATH);
        }
		return springFileLocations;
	}
}
