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
package org.kuali.rice.edl.impl.config;

import org.kuali.rice.core.api.config.module.RunMode;
import org.kuali.rice.core.framework.config.module.ModuleConfigurer;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Configures the EDocLite module. 
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class EDLConfigurer extends ModuleConfigurer {

	//public static final String EDL_DATASOURCE_OBJ = "edl.datasource";
    public static final String EDL_LOCAL_SPRING_FILE = "classpath:org/kuali/rice/edl/impl/config/EDLLocalSpringBeans.xml";
	private DataSource dataSource;

    public EDLConfigurer() {
        super("edl");
        setValidRunModes(Arrays.asList(RunMode.LOCAL));
    }

	@Override
	public List<String> getPrimarySpringFiles() {
		final List<String> springFileLocations = new ArrayList<String>();
		if (RunMode.LOCAL.equals(getRunMode())) {
			springFileLocations.add(EDL_LOCAL_SPRING_FILE);
		}

		return springFileLocations;
	}
	
    /*private List<String> getEmbeddedSpringFileLocation(){
    	final List<String> springFileLocations = new ArrayList<String>();
    	springFileLocations.add("classpath:org/kuali/rice/edl/impl/config/EDLSpringBeans.xml");
      	springFileLocations.add("classpath:org/kuali/rice/edl/impl/config/EDLOJBSpringBeans.xml");
    	return springFileLocations;
    }*/

	/*@Override
	public void addAdditonalToConfig() {
		configureDataSource();
	}*/

	/*private void configureDataSource() {
		if (getDataSource() != null) {
			ConfigContext.getCurrentContextConfig().putObject(KEW_DATASOURCE_OBJ, getDataSource());
		}
	}*/

	/*
	@Override
	public Collection<ResourceLoader> getResourceLoadersToRegister() throws Exception {
		// create the plugin registry
		PluginRegistry registry = null;
		String pluginRegistryEnabled = ConfigContext.getCurrentContextConfig().getProperty("plugin.registry.enabled");
		if (!StringUtils.isBlank(pluginRegistryEnabled) && Boolean.valueOf(pluginRegistryEnabled).booleanValue()) {
			registry = new PluginRegistryFactory().createPluginRegistry();
		}

		final Collection<ResourceLoader> rls = new ArrayList<ResourceLoader>();
		for (ResourceLoader rl : RiceResourceLoaderFactory.getSpringResourceLoaders()) {
			CoreResourceLoader coreResourceLoader =
				new CoreResourceLoader(rl, registry);
			coreResourceLoader.start();

			//wait until core resource loader is started to attach to GRL;  this is so startup
			//code can depend on other things hooked into GRL without incomplete KEW resources
			//messing things up.

			GlobalResourceLoader.addResourceLoader(coreResourceLoader);

			// now start the plugin registry if there is one
			if (registry != null) {
				registry.start();
				// the registry resourceloader is now being handled by the CoreResourceLoader
				//GlobalResourceLoader.addResourceLoader(registry);
			}
			rls.add(coreResourceLoader);
		}

		return rls;
	}

	private ClientProtocol getClientProtocol() {
		return ClientProtocol.valueOf(ConfigContext.getCurrentContextConfig().getProperty("client.protocol"));
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	*/
}
