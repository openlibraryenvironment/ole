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
package org.kuali.rice.kim.config;

import org.kuali.rice.core.api.config.module.RunMode;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.framework.config.module.ModuleConfigurer;
import org.kuali.rice.core.framework.config.module.WebModuleConfiguration;
import org.kuali.rice.kim.api.KimApiConstants;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class handles the Spring based KIM configuration that is part of the Rice Configurer that must 
 * exist in all Rice based systems and clients. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class KIMConfigurer extends ModuleConfigurer {
    public static final String KIM_DATASOURCE_OBJ = "kim.datasource";
	private static final String KIM_UI_SPRING_BEANS_PATH = "classpath:org/kuali/rice/kim/impl/config/KimWebSpringBeans.xml";
    private DataSource dataSource;

    public KIMConfigurer() {
        super(KimApiConstants.Namespaces.MODULE_NAME);
        setValidRunModes(Arrays.asList(RunMode.THIN, RunMode.REMOTE, RunMode.EMBEDDED, RunMode.LOCAL));
    }

    @Override
    protected String getDefaultConfigPackagePath() {
        return "classpath:org/kuali/rice/kim/impl/config/";
    }

    @Override
	public List<String> getPrimarySpringFiles() {
        List<String> springFileLocations = new ArrayList<String>();
        if (RunMode.THIN == getRunMode()) {
            springFileLocations.add(getDefaultConfigPackagePath() + "KimThinSpringBeans.xml");
        } else if (RunMode.REMOTE == getRunMode()) {
            springFileLocations.add(getDefaultConfigPackagePath() + "KimRemoteSpringBeans.xml");
        } else if (RunMode.EMBEDDED == getRunMode()) {
            springFileLocations.add(getDefaultConfigPackagePath() + "KimEmbeddedSpringBeans.xml");
        } else if (RunMode.LOCAL == getRunMode()) {
            springFileLocations.add(getDefaultConfigPackagePath() + "KimLocalSpringBeans.xml");
        }
		return springFileLocations;
	}

    @Override
    public void addAdditonalToConfig() {
        configureDataSource();
    }

    private void configureDataSource() {
        if (getDataSource() != null) {
            ConfigContext.getCurrentContextConfig().putObject(KIM_DATASOURCE_OBJ, getDataSource());
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    @Override
    public boolean hasWebInterface() {
        return true;
    }

    @Override
    protected WebModuleConfiguration loadWebModule() {
        WebModuleConfiguration configuration = super.loadWebModule();
        configuration.setWebSpringFiles(Arrays.asList(KIM_UI_SPRING_BEANS_PATH));
        return configuration;
    }
}
