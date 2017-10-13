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
package org.kuali.rice.core.framework.config.module;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.config.ConfigurationException;
import org.kuali.rice.core.api.config.CoreConfigHelper;
import org.kuali.rice.core.api.config.module.Configurer;
import org.kuali.rice.core.api.config.module.RunMode;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.lifecycle.BaseCompositeLifecycle;
import org.kuali.rice.core.api.lifecycle.Lifecycle;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.resourceloader.ResourceLoader;
import org.kuali.rice.core.api.resourceloader.ResourceLoaderContainer;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.framework.resourceloader.BaseResourceLoader;
import org.kuali.rice.core.framework.resourceloader.RiceResourceLoaderFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

// FIXME: this class must be put in an API module somehow
public class ModuleConfigurer extends BaseCompositeLifecycle implements Configurer, InitializingBean, DisposableBean, ServletContextAware {
    /**
     * Config key under which the list of registered ModuleConfigurers is stored
     */
    private static final String MODULE_CONFIGURERS_CONFIG_KEY = "ModuleConfigurers";

    /**
     * @return list of registered ModuleConfigurers
     */
    public static Collection<ModuleConfigurer> getCurrentContextConfigurers() {
        return (Collection<ModuleConfigurer>) ConfigContext.getCurrentContextConfig().getObject(MODULE_CONFIGURERS_CONFIG_KEY);
    }

    protected final Logger LOG = Logger.getLogger(getClass());

    private List<RunMode> validRunModes = new ArrayList<RunMode>();

	private Properties properties = new Properties();
	private String moduleName;
	private ServletContext servletContext;
	
	public ModuleConfigurer() {
	}
	
	public ModuleConfigurer(String moduleName) {
		this.moduleName = moduleName;
	}
	
	@Override
	public final void start() throws Exception {
		super.start();
		doAdditionalModuleStartLogic();
	}
	
	protected void doAdditionalModuleStartLogic() throws Exception {
		// subclass can override if needed
	}
	
	@Override
	public final void afterPropertiesSet() throws Exception {
		validateConfigurerState();
		addToConfig();
		initializeResourceLoaders();
		start();
	}
	
	@Override
	public final void stop() throws Exception {
		try {
			doAdditionalModuleStopLogic();
		} finally {
			super.stop();
		}
	}
	
	protected void doAdditionalModuleStopLogic() throws Exception {
		// subclass can override if needed
	}
	
	@Override
	public final void destroy() throws Exception {
        stop();
        //FIXME: attempting to see if this fixes the ksb tests...
        GlobalResourceLoader.stop();
	}
	
	@Override
	public List<Lifecycle> loadLifecycles() throws Exception {
		return Collections.emptyList();
		//override in subclasses
	}
	
	public RunMode getRunMode() {
		String propertyName = getModuleName().toLowerCase() + ".mode";
		String runMode = ConfigContext.getCurrentContextConfig().getProperty(propertyName);
		if (StringUtils.isBlank(runMode)) {
			throw new ConfigurationException("Failed to determine run mode for module '" + getModuleName() + "'.  Please be sure to set configuration parameter '" + propertyName + "'");
		}
		return RunMode.valueOf(runMode.toUpperCase());
	}

    /**
     * Indicates whether or not this module has a web interface module.  Default implementation returns false, but
     * subclasses can override to return true if they have a web module that can be loaded.
     *
     * @return true if this module has a web interface, false otherwise
     */
    public boolean hasWebInterface() {
		return false;
	}
	
	/**
	 * This base implementation returns true when the module has a web interface and the
	 * runMode is "local".
	 * 
	 * <p>Subclasses can override this method if there are different requirements for inclusion
	 * of the web UI for the module.</p>
     *
     * @return true if the web interface for this module should be rendered, false otherwise
	 */
	public boolean shouldRenderWebInterface() {
		return hasWebInterface() && getRunMode().equals( RunMode.LOCAL );
	}
		
	public boolean isExposeServicesOnBus() {
		return Boolean.valueOf(ConfigContext.getCurrentContextConfig().getProperty("rice." + getModuleName().toLowerCase() + ".expose.services.on.bus")).booleanValue();
	}
		
	@Override
	public List<String> getPrimarySpringFiles() {
		return Collections.singletonList(getDefaultSpringBeansPath(getDefaultConfigPackagePath()));
	}

    public final WebModuleConfiguration getWebModuleConfiguration() {
        if (hasWebInterface()) {
            return loadWebModule();
        }
        return null;
    }

    /**
     * Subclasses can override the default implementation of this method if they want to provide a custom implementation
     * for loading the web module configuration.  This method will be called from #getWebModuleConfiguration when
     * #hasWebInterface return true.
     *
     * @return the loaded web module configuration, this method should *never* return null.
     */
    protected WebModuleConfiguration loadWebModule() {
        return new WebModuleConfiguration(this.moduleName);
    }

	public Properties getProperties() {
		return this.properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public List<String> getAdditionalSpringFiles() {
		final String files = ConfigContext.getCurrentContextConfig().getProperty("rice." + getModuleName() + ".additionalSpringFiles");
		return files == null ? Collections.<String>emptyList() : parseFileList(files);
	}
	
	private List<String> parseFileList(String files) {
		final List<String> parsedFiles = new ArrayList<String>();
		for (String file : Arrays.asList(files.split(","))) {
			final String trimmedFile = file.trim();
			if (!trimmedFile.isEmpty()) {
				parsedFiles.add(trimmedFile);	
			}
		}
		
		return parsedFiles;
	}
	
	public String getModuleName() {
		return this.moduleName;
	}

	protected void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
    
    /* helper methods for constructors */
    protected String getDefaultConfigPackagePath() {
    	return "classpath:org/kuali/rice/" + getModuleName().toLowerCase() + "/config/";
    }
    protected String getDefaultSpringBeansPath(String configPackagePath) {
        return configPackagePath + getModuleName().toUpperCase() + "SpringBeans.xml"; 
    }
    
	public List<RunMode> getValidRunModes() {
		return this.validRunModes;
	}

	protected void setValidRunModes(List<RunMode> validRunModes) {
		this.validRunModes = validRunModes;
	}
	
	@Override
	public final void validateConfigurerState() {
		if (StringUtils.isBlank(this.moduleName)) {
			throw new IllegalStateException("the module name for this module has not been set");
		}
		
		if (CollectionUtils.isEmpty(this.validRunModes)) {
			throw new IllegalStateException("the valid run modes for this module has not been set");
		}
		
		// ConfigContext must be initialized...
		if (!ConfigContext.isInitialized()) {
    		throw new ConfigurationException("ConfigContext has not yet been initialized, please initialize prior to using.");
    	}
		
		validateRunMode();
		
		doAdditonalConfigurerValidations();
	}
	
	private void validateRunMode() {
		if ( !validRunModes.contains( getRunMode() ) ) {
			throw new IllegalArgumentException( "Invalid run mode for the " + this.getClass().getSimpleName() + ": " + getRunMode() + " - Valid Values are: " + validRunModes );
		}
	}
	
	protected void doAdditonalConfigurerValidations() {
		//override in subclasses
	}
	
	
	/**
	 * This method does the following: 
	 * <ol>
	 *  <li>Places all module specific configurations into the root config</li>
	 *  <li>Adds any additional properties passed into the config into the root config</li>
	 *  <li>Adds any items a subclass wants to put into the config</li>
	 * </ol>
	 */
	@Override
	public final void addToConfig() {
		
		if (this.properties != null) {
			ConfigContext.getCurrentContextConfig().putProperties(this.properties);
		}
		
		registerConfigurerWithConfig();
		addAdditonalToConfig();
	}
	
	protected void addAdditonalToConfig() {
		//override in subclasses
	}
	
	/**
	 * This is a bit of a hack.... fix me
	 *
	 */
	private void registerConfigurerWithConfig() {
		@SuppressWarnings("unchecked")
		Collection<ModuleConfigurer> configurers = (Collection<ModuleConfigurer>) ConfigContext.getCurrentContextConfig().getObject(MODULE_CONFIGURERS_CONFIG_KEY);
		if (configurers == null) {
			configurers = new ArrayList<ModuleConfigurer>();
		}
		configurers.add(this);
		
		ConfigContext.getCurrentContextConfig().putObject(MODULE_CONFIGURERS_CONFIG_KEY, configurers);
	}
	
	@Override
	public final void initializeResourceLoaders() throws Exception {
		List<String> files = new ArrayList<String>();
		files.addAll(getPrimarySpringFiles());
		files.addAll(getAdditionalSpringFiles());
		
		ResourceLoader rootResourceLoader = GlobalResourceLoader.getResourceLoader();
		if (rootResourceLoader == null) {
			rootResourceLoader = createRootResourceLoader();
		}
		
		if (!files.isEmpty()) {
			ResourceLoader rl = RiceResourceLoaderFactory.createRootRiceResourceLoader(servletContext, files,
                    getModuleName());
			rl.start();
			GlobalResourceLoader.addResourceLoader(rl);
		}
		
		final Collection<ResourceLoader> rls = getResourceLoadersToRegister();
		
		for (ResourceLoader rl : rls) {
			GlobalResourceLoader.addResourceLoader(rl);
		}
	}
	
	protected Collection<ResourceLoader> getResourceLoadersToRegister() throws Exception {
		return Collections.emptyList();
		//override in subclasses
	}
	
	private ResourceLoader createRootResourceLoader() throws Exception {
		final ResourceLoaderContainer container = new ResourceLoaderContainer(new QName(CoreConfigHelper.getApplicationId(), RiceConstants.ROOT_RESOURCE_LOADER_CONTAINER_NAME));
		ResourceLoader rootResourceLoader = new BaseResourceLoader(new QName(CoreConfigHelper.getApplicationId(), RiceConstants.DEFAULT_ROOT_RESOURCE_LOADER_NAME));
		
		container.addResourceLoader(rootResourceLoader);
		GlobalResourceLoader.addResourceLoader(container);
		GlobalResourceLoader.start();
		
		return container;
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
}
