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
package org.kuali.rice.krad.devtools.datadictionary;


import no.geosoft.cc.io.FileListener;
import no.geosoft.cc.io.FileMonitor;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.krad.datadictionary.DataDictionary;
import org.kuali.rice.krad.util.KRADConstants;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Extends the DataDictionary to add reloading of changed dictionary files
 * without a restart of the web container
 *
 * <p>
 * To use modify the "dataDictionaryService" spring definition
 * (KRADSpringBeans.xml) and change the constructor arg bean class from
 * "org.kuali.rice.krad.datadictionary.DataDictionary" to
 * "ReloadingDataDictionary"
 * </p>
 *
 * <p>
 * NOTE: For Development Purposes Only!
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ReloadingDataDictionary extends DataDictionary implements FileListener, URLMonitor.URLContentChangedListener, ApplicationContextAware {
    private static final Log LOG = LogFactory.getLog(DataDictionary.class);

    private static final String CLASS_DIR_CONFIG_PARM = "reload.data.dictionary.classes.dir";
    private static final String SOURCE_DIR_CONFIG_PARM = "reload.data.dictionary.source.dir";
    private static final String INTERVAL_CONFIG_PARM = "reload.data.dictionary.interval";

    private Map<String, String> fileToNamespaceMapping;
    private Map<String, String> urlToNamespaceMapping;
    
    private FileMonitor dictionaryFileMonitor;
    private URLMonitor dictionaryUrlMonitor;

    public ReloadingDataDictionary() {
        super();
    }

    /**
     * After dictionary has been loaded, determine the source files and add them
     * to the monitor
     *
     * @see org.kuali.rice.krad.datadictionary.DataDictionary#parseDataDictionaryConfigurationFiles(boolean)
     */
    @Override
    public void parseDataDictionaryConfigurationFiles(boolean allowConcurrentValidation) {
        ConfigurationService configurationService = CoreApiServiceLocator.getKualiConfigurationService();

        // class directory part of the path that should be replaced
        String classesDir = configurationService.getPropertyValueAsString(CLASS_DIR_CONFIG_PARM);

        // source directory where dictionary files are found
        String sourceDir = configurationService.getPropertyValueAsString(SOURCE_DIR_CONFIG_PARM);

        // interval to poll for changes in milliseconds
        int reloadInterval = Integer.parseInt(configurationService.getPropertyValueAsString(INTERVAL_CONFIG_PARM));

        dictionaryFileMonitor = new FileMonitor(reloadInterval);
        dictionaryFileMonitor.addListener(this);

        dictionaryUrlMonitor = new URLMonitor(reloadInterval);
        dictionaryUrlMonitor.addListener(this);

        super.parseDataDictionaryConfigurationFiles(allowConcurrentValidation);

        // need to hold mappings of file/url to namespace so we can correctly add beans to the associated
        // namespace when reloading the resource
        fileToNamespaceMapping = new HashMap<String, String>();
        urlToNamespaceMapping = new HashMap<String, String>();

        // add listener for each dictionary file
        for (Map.Entry<String, List<String>> moduleDictionary : moduleDictionaryFiles.entrySet()) {
            String namespace = moduleDictionary.getKey();
            List<String> configLocations = moduleDictionary.getValue();

            for (String configLocation : configLocations) {
                Resource classFileResource = getFileResource(configLocation);

                try {
                    if (classFileResource.getURI().toString().startsWith("jar:")) {
                        LOG.trace("Monitoring dictionary file at URI: " + classFileResource.getURI().toString());

                        dictionaryUrlMonitor.addURI(classFileResource.getURL());
                        urlToNamespaceMapping.put(classFileResource.getURL().toString(), namespace);
                    } else {
                        String filePathClassesDir = classFileResource.getFile().getAbsolutePath();
                        String sourceFilePath = StringUtils.replace(filePathClassesDir, classesDir, sourceDir);

                        File dictionaryFile = new File(filePathClassesDir);
                        if (dictionaryFile.exists()) {
                            LOG.trace("Monitoring dictionary file: " + dictionaryFile.getName());

                            dictionaryFileMonitor.addFile(dictionaryFile);
                            fileToNamespaceMapping.put(dictionaryFile.getAbsolutePath(), namespace);
                        }
                    }
                } catch (Exception e) {
                    LOG.info("Exception in picking up dictionary file for monitoring:  " + e.getMessage(), e);
                }
            }
        }
    }

    /**
     * Call back when a dictionary file is changed. Calls the spring bean reader
     * to reload the file (which will override beans as necessary and destroy
     * singletons) and runs the indexer
     *
     * @see no.geosoft.cc.io.FileListener#fileChanged(java.io.File)
     */
    public void fileChanged(File file) {
        LOG.info("reloading dictionary configuration for " + file.getName());
        try {
            List<String> beforeReloadBeanNames = Arrays.asList(ddBeans.getBeanDefinitionNames());
            
            Resource resource = new FileSystemResource(file);
            xmlReader.loadBeanDefinitions(resource);
            
            List<String> afterReloadBeanNames = Arrays.asList(ddBeans.getBeanDefinitionNames());
            
            List<String> addedBeanNames = ListUtils.removeAll(afterReloadBeanNames, beforeReloadBeanNames);
            String namespace = KRADConstants.DEFAULT_NAMESPACE;
            if (fileToNamespaceMapping.containsKey(file.getAbsolutePath())) {
                namespace = fileToNamespaceMapping.get(file.getAbsolutePath());
            }

            ddIndex.addBeanNamesToNamespace(namespace, addedBeanNames);

            performDictionaryPostProcessing(true);
        } catch (Exception e) {
            LOG.info("Exception in dictionary hot deploy: " + e.getMessage(), e);
        }
    }

    public void urlContentChanged(final URL url) {
        LOG.info("reloading dictionary configuration for " + url.toString());
        try {
            InputStream urlStream = url.openStream();
            InputStreamResource resource = new InputStreamResource(urlStream);

            List<String> beforeReloadBeanNames = Arrays.asList(ddBeans.getBeanDefinitionNames());
            
            int originalValidationMode = xmlReader.getValidationMode();
            xmlReader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_XSD);
            xmlReader.loadBeanDefinitions(resource);
            xmlReader.setValidationMode(originalValidationMode);
            
            List<String> afterReloadBeanNames = Arrays.asList(ddBeans.getBeanDefinitionNames());
            
            List<String> addedBeanNames = ListUtils.removeAll(afterReloadBeanNames, beforeReloadBeanNames);
            String namespace = KRADConstants.DEFAULT_NAMESPACE;
            if (urlToNamespaceMapping.containsKey(url.toString())) {
                namespace = urlToNamespaceMapping.get(url.toString());
            }

            ddIndex.addBeanNamesToNamespace(namespace, addedBeanNames);

            performDictionaryPostProcessing(true);
        } catch (Exception e) {
            LOG.info("Exception in dictionary hot deploy: " + e.getMessage(), e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // register a context close handler
        if (applicationContext instanceof ConfigurableApplicationContext) {
            ConfigurableApplicationContext context = (ConfigurableApplicationContext) applicationContext;
            context.addApplicationListener(new ApplicationListener<ContextClosedEvent>() {
                @Override
                public void onApplicationEvent(ContextClosedEvent e) {
                    LOG.info("Context '" + e.getApplicationContext().getDisplayName() +
                            "' closed, shutting down URLMonitor scheduler");
                    dictionaryUrlMonitor.shutdownScheduler();
                }
            });
        }
    }
}
