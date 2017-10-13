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
package org.kuali.rice.kew.config;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.config.module.RunMode;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.lifecycle.Lifecycle;
import org.kuali.rice.core.api.resourceloader.ResourceLoader;
import org.kuali.rice.core.framework.config.module.ModuleConfigurer;
import org.kuali.rice.core.framework.config.module.WebModuleConfiguration;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.lifecycle.StandaloneLifeCycle;
import org.kuali.rice.kew.plugin.PluginRegistry;
import org.kuali.rice.kew.plugin.PluginRegistryFactory;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


/**
 * Configures the KEW Rice module.  KEW module initiation proceeds as follows:
 *
 * <ol>
 *   <li>Parse and load configuration for:</li>
 *     <ul>
 *       <li>Client Protocol</li>
 *       <li>Database</li>
 *	   </ul>
 *   </li>
 *   <li>Configure and startup KEW for "Thin Client" mode OR</li>
 *   <li>Configure and startup KEW for "Embedded Mode"</li>
 * </ol>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class KEWConfigurer extends ModuleConfigurer {

	public static final String KEW_DATASOURCE_OBJ = "org.kuali.workflow.datasource";

	private DataSource dataSource;

    public KEWConfigurer() {
        super(KewApiConstants.Namespaces.MODULE_NAME);
        setValidRunModes(Arrays.asList(RunMode.THIN, RunMode.REMOTE, RunMode.EMBEDDED, RunMode.LOCAL));
    }

	@Override
	public List<String> getPrimarySpringFiles() {
        List<String> springFileLocations = new ArrayList<String>();
        if (RunMode.THIN == getRunMode()) {
            springFileLocations.add(getDefaultConfigPackagePath() + "KewThinSpringBeans.xml");
        } else if (RunMode.REMOTE == getRunMode()) {
            springFileLocations.add(getDefaultConfigPackagePath() + "KewRemoteSpringBeans.xml");
        } else if (RunMode.EMBEDDED == getRunMode()) {
            springFileLocations.add(getDefaultConfigPackagePath() + "KewEmbeddedSpringBeans.xml");
        } else if (RunMode.LOCAL == getRunMode()) {
            springFileLocations.add(getDefaultConfigPackagePath() + "KewLocalSpringBeans.xml");
        }
		return springFileLocations;
	}
	
	@Override
	public List<Lifecycle> loadLifecycles() throws Exception {
		
		List<Lifecycle> lifecycles = new LinkedList<Lifecycle>();
        if ( getRunMode().equals( RunMode.LOCAL ) ) { // local or embedded
			lifecycles.add(createStandaloneLifeCycle());
		}
		return lifecycles;
	}

	/**
	 * TODO Because a lot of our lifecycles live behind the embedded plugin and the KEWConfigurer does not, this is a simple
	 * measure to load these without having to deal with the removal of the embedded plugin right away.
     * @return Life Cycle
     * @throws Exception if life cycle not created
     */
	private Lifecycle createStandaloneLifeCycle() throws Exception {
		return new StandaloneLifeCycle();
	}

	@Override
	public void addAdditonalToConfig() {
		configureDataSource();
	}

	private void configureDataSource() {
		if (getDataSource() != null) {
			ConfigContext.getCurrentContextConfig().putObject(KEW_DATASOURCE_OBJ, getDataSource());
		}
	}

	@Override
	public Collection<ResourceLoader> getResourceLoadersToRegister() throws Exception {
        List<ResourceLoader> resourceLoaders = new ArrayList<ResourceLoader>();
		String pluginRegistryEnabled = ConfigContext.getCurrentContextConfig().getProperty("plugin.registry.enabled");
		if (!StringUtils.isBlank(pluginRegistryEnabled) && Boolean.valueOf(pluginRegistryEnabled).booleanValue()) {
    		// create the plugin registry
			PluginRegistry registry = new PluginRegistryFactory().createPluginRegistry();
            registry.start();
            resourceLoaders.add(registry);
		}
        return resourceLoaders;
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
        configuration.setWebSpringFiles(Arrays.asList(getDefaultConfigPackagePath() + "KewWebSpringBeans.xml"));
        return configuration;
    }
}
