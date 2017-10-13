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
package org.kuali.rice.test;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Before;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.lifecycle.BaseLifecycle;
import org.kuali.rice.core.api.lifecycle.Lifecycle;
import org.kuali.rice.core.framework.resourceloader.SpringResourceLoader;
import org.kuali.rice.core.impl.config.property.JAXBConfigImpl;
import org.kuali.rice.test.data.PerSuiteUnitTestData;
import org.kuali.rice.test.lifecycles.PerSuiteDataLoaderLifecycle;
import org.springframework.beans.factory.BeanCreationNotAllowedException;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.xml.namespace.QName;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;


/**
 * Useful superclass for all Rice test cases. Handles setup of test utilities and a test environment. Configures the
 * Spring test environment providing a template method for custom context files in test mode. Also provides a template method
 * for running custom transactional setUp. Tear down handles automatic tear down of objects created inside the test
 * environment.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @since 0.9
 */
public abstract class RiceTestCase extends BaseRiceTestCase {

    protected static final Logger LOG = Logger.getLogger(RiceTestCase.class);

    private static final String ALT_LOG4J_CONFIG_LOCATION_PROP = "alt.log4j.config.location";
    private static final String DEFAULT_LOG4J_CONFIG = "classpath:rice-testharness-default-log4j.properties";
    protected static final String DEFAULT_TEST_HARNESS_SPRING_BEANS = "classpath:TestHarnessSpringBeans.xml";
    protected static boolean SUITE_LIFE_CYCLES_RAN = false;
    protected static boolean SUITE_LIFE_CYCLES_FAILED = false;
    protected static String failedSuiteTestName;

    protected List<Lifecycle> perTestLifeCycles = new LinkedList<Lifecycle>();

    protected List<Lifecycle> suiteLifeCycles = new LinkedList<Lifecycle>();

    private static Set<String> perSuiteDataLoaderLifecycleNamesRun = new HashSet<String>();

    private List<String> reports = new ArrayList<String>();

    private SpringResourceLoader testHarnessSpringResourceLoader;
    private boolean clearTables = true;

    @Override
	@Before
    public void setUp() throws Exception {
        try {
            configureLogging();
            logBeforeRun();

            final long initTime = System.currentTimeMillis();

            setUpInternal();

            report("Time to start all Lifecycles: " + (System.currentTimeMillis() - initTime));
        } catch (Throwable e) {
            e.printStackTrace();
            tearDown();
            throw new RuntimeException(e);
        }
    }

    /**
     * Internal setUp() implementation which is invoked by the main setUp() and wrapped with exception handling
     *
     * <p>Subclasses should override this method if they want to
     * add set up steps that should occur in the standard set up process, wrapped by
     * exception handling.</p>
     */
    protected void setUpInternal() throws Exception {
        assertNotNull(getModuleName());
        setModuleName(getModuleName());
        setBaseDirSystemProperty(getModuleName());

        this.perTestLifeCycles = getPerTestLifecycles();
        this.suiteLifeCycles = getSuiteLifecycles();

        if (SUITE_LIFE_CYCLES_FAILED) {
        	fail("Suite Lifecycles startup failed on test " + failedSuiteTestName + "!!!  Please see logs for details.");
        }
        if (!SUITE_LIFE_CYCLES_RAN) {
	        try {
    	        startLifecycles(this.suiteLifeCycles);
        	    SUITE_LIFE_CYCLES_RAN = true;
        	} catch (Throwable e) {
        		e.printStackTrace();
                SUITE_LIFE_CYCLES_RAN = false;
                SUITE_LIFE_CYCLES_FAILED = true;
                failedSuiteTestName = getFullTestName();
                tearDown();
                stopLifecycles(this.suiteLifeCycles);
                throw new RuntimeException(e);
            }
        }

        startSuiteDataLoaderLifecycles();

        startLifecycles(this.perTestLifeCycles);

    }

    /**
     * This block is walking up the class hierarchy of the current unit test looking for PerSuiteUnitTestData annotations. If it finds one,
     * it will run it once, then add it to a set so that it does not get run again. This is needed so that multiple 
     * tests can extend from the same suite and so that there can be multiple suites throughout the test source branch.
     * 
     * @throws Exception if a PerSuiteDataLoaderLifecycle is unable to be started
     */
    protected void startSuiteDataLoaderLifecycles() throws Exception {
        List<Class> classes = TestUtilities.getHierarchyClassesToHandle(getClass(), new Class[] { PerSuiteUnitTestData.class }, perSuiteDataLoaderLifecycleNamesRun);
        for (Class c: classes) {
            new PerSuiteDataLoaderLifecycle(c).start();
            perSuiteDataLoaderLifecycleNamesRun.add(c.getName());
        }
    }

    /**
     * maven will set this property and find resources from the config based on it. This makes eclipse testing work because
     * we have to put the basedir in our config files in order to find things when testing from maven
     */
    protected void setBaseDirSystemProperty(String moduleBaseDir) {
        if (System.getProperty("basedir") == null) {
        	final String userDir = System.getProperty("user.dir");
        	
            System.setProperty("basedir", userDir + ((userDir.endsWith(File.separator + "it" + File.separator + moduleBaseDir)) ? "" : File.separator + "it" + File.separator + moduleBaseDir));
        }
    }

    /**
     * the absolute path on the file system to the root folder of the maven module containing a child of this class
     * e.g. for krad: [rice-project-dir]/it/krad
     *
     * <p>
     * the user.dir property can be set on the CLI or IDE run configuration e.g. -Duser.dir=/some/dir
     * </p>
     * @return the value of a system property 'user.dir' if it exists, null if not
     */
    protected String getUserDir() {
        return System.getProperty("user.dir");
    }

    /**
     * Returns the basedir for the module under which the tests are currently executing.
     */
    protected String getBaseDir() {
        return System.getProperty("basedir");
    }

    protected void setModuleName(String moduleName) {
        if (System.getProperty("module.name") == null) {
            System.setProperty("module.name", moduleName);
        }
    }

    @Override
	@After
    public void tearDown() throws Exception {
    	// wait for outstanding threads to complete for 1 minute
    	ThreadMonitor.tearDown(60000);
        try {
            stopLifecycles(this.perTestLifeCycles);
        // Avoid failing test for creation of bean in destroy.
        } catch (BeanCreationNotAllowedException bcnae) {
            LOG.warn("BeanCreationNotAllowedException during stopLifecycles during tearDown " + bcnae.getMessage());
        }
        logAfterRun();
    }

    protected void logBeforeRun() {
        LOG.info("##############################################################");
        LOG.info("# Starting test " + getFullTestName() + "...");
        LOG.info("# " + dumpMemory());
        LOG.info("##############################################################");
    }

    protected void logAfterRun() {
        LOG.info("##############################################################");
        LOG.info("# ...finished test " + getFullTestName());
        LOG.info("# " + dumpMemory());
        for (final String report : this.reports) {
            LOG.info("# " + report);
        }
        LOG.info("##############################################################\n\n\n");
    }
    
    protected String getFullTestName() {
    	return getClass().getSimpleName() + "." + getName();
    }

    /**
     * configures logging using custom properties file if specified, or the default one.
     * Log4j also uses any file called log4.properties in the classpath
     *
     * <p>To configure a custom logging file, set a JVM system property on using -D. For example
     * -Dalt.log4j.config.location=file:/home/me/kuali/test/dev/log4j.properties
     * </p>
     *
     * <p>The above option can also be set in the run configuration for the unit test in the IDE.
     * To avoid log4j using files called log4j.properties that are defined in the classpath, add the following system property:
     * -Dlog4j.defaultInitOverride=true
     * </p>
     * @throws IOException
     */
	protected void configureLogging() throws IOException {
        ResourceLoader resourceLoader = new FileSystemResourceLoader();
        String altLog4jConfigLocation = System.getProperty(ALT_LOG4J_CONFIG_LOCATION_PROP);
        Resource log4jConfigResource = null;
        if (!StringUtils.isEmpty(altLog4jConfigLocation)) { 
            log4jConfigResource = resourceLoader.getResource(altLog4jConfigLocation);
        }
        if (log4jConfigResource == null || !log4jConfigResource.exists()) {
            System.out.println("Alternate Log4j config resource does not exist! " + altLog4jConfigLocation);
            System.out.println("Using default log4j configuration: " + DEFAULT_LOG4J_CONFIG);
            log4jConfigResource = resourceLoader.getResource(DEFAULT_LOG4J_CONFIG);
        } else {
            System.out.println("Using alternate log4j configuration at: " + altLog4jConfigLocation);
        }
        Properties p = new Properties();
        p.load(log4jConfigResource.getInputStream());
        PropertyConfigurator.configure(p);
    }

	/**
	 * Executes the start() method of each of the lifecycles in the given list.
	 */
    protected void startLifecycles(List<Lifecycle> lifecycles) throws Exception {
        for (Lifecycle lifecycle : lifecycles) {
                lifecycle.start();
        }
    }

    /**
     * Executes the stop() method of each of the lifecyles in the given list.  The
     * List of lifecycles is processed in reverse order.
     */
    protected void stopLifecycles(List<Lifecycle> lifecycles) throws Exception {
        int lifecyclesSize = lifecycles.size() - 1;
        for (int i = lifecyclesSize; i >= 0; i--) {
            try {
            	if (lifecycles.get(i) == null) {
            		LOG.warn("Attempted to stop a null lifecycle");
            	} else {
            		if (lifecycles.get(i).isStarted()) {
                        LOG.warn("Attempting to stop a lifecycle " + lifecycles.get(i).getClass());
            			lifecycles.get(i).stop();
            		}
            	}
            } catch (Exception e) {
                LOG.error("Failed to shutdown one of the lifecycles!", e);
            }
        }
    }

    /**
     * Returns the List of Lifecycles to start when the unit test suite is started
     */
    protected List<Lifecycle> getSuiteLifecycles() {
        List<Lifecycle> lifecycles = new LinkedList<Lifecycle>();
        
        /**
         * Initializes Rice configuration from the test harness configuration file.
         */
        lifecycles.add(new BaseLifecycle() {
            @Override
			public void start() throws Exception {
                Config config = getTestHarnessConfig();
                ConfigContext.init(config);
                super.start();
            }
        });
        
        /**
         * Loads the TestHarnessSpringBeans.xml file which obtains connections to the DB for us
         */
        lifecycles.add(getTestHarnessSpringResourceLoader());
        
        /**
         * Establishes the TestHarnessServiceLocator so that it has a reference to the Spring context
         * created from TestHarnessSpringBeans.xml
         */
        lifecycles.add(new BaseLifecycle() {
            @Override
			public void start() throws Exception {
                TestHarnessServiceLocator.setContext(getTestHarnessSpringResourceLoader().getContext());
                super.start();
            }
        });
        
        /**
         * Clears the tables in the database.
         */
        if (clearTables) {
        	lifecycles.add(new ClearDatabaseLifecycle());
        }
        
        /**
         * Loads Suite Test Data
         */
        lifecycles.add(new BaseLifecycle() {
        	@Override
			public void start() throws Exception {
        		loadSuiteTestData();
        		super.start();
        	}
        });
        
        Lifecycle loadApplicationLifecycle = getLoadApplicationLifecycle();
        if (loadApplicationLifecycle != null) {
        	lifecycles.add(loadApplicationLifecycle);
        }
        return lifecycles;
    }
    
    /**
     * This should return a Lifecycle that can be used to load the application
     * being tested.  For example, this could start a Jetty Server which loads
     * the application, or load a Spring context to establish a set of services,
     * or any other application startup activities that the test depends upon.
     */
    protected Lifecycle getLoadApplicationLifecycle() {
    	// by default return null, do nothing
    	return null;
    }

    /**
     * @return Lifecycles run every test run
     */
    protected List<Lifecycle> getPerTestLifecycles() {
    	List<Lifecycle> lifecycles = new LinkedList<Lifecycle>();
        lifecycles.add(getPerTestDataLoaderLifecycle());
        lifecycles.add(new BaseLifecycle() {
            @Override
			public void start() throws Exception {
                loadPerTestData();
                super.start();
            }
        });
        return lifecycles;
    }
    
    /**
     * A method that can be overridden to load test data for the unit test Suite.
     */
    protected void loadSuiteTestData() throws Exception {
    	// do nothing by default, subclass can override
    }
    
    /**
     * A method that can be overridden to load test data on a test-by-test basis
     */
    protected void loadPerTestData() throws Exception {
    	// do nothing by default, subclass can override
    }

    protected void report(final String report) {
        this.reports.add(report);
    }

    protected String dumpMemory() {
        final long total = Runtime.getRuntime().totalMemory();
        final long free = Runtime.getRuntime().freeMemory();
        final long max = Runtime.getRuntime().maxMemory();
        return "[Memory] max: " + max + ", total: " + total + ", free: " + free;
    }

    public SpringResourceLoader getTestHarnessSpringResourceLoader() {
        if (testHarnessSpringResourceLoader == null) {
            testHarnessSpringResourceLoader = new SpringResourceLoader(new QName("TestHarnessSpringContext"), getTestHarnessSpringBeansLocation(), null);
        }
        return testHarnessSpringResourceLoader;
    }

    /**
     * Returns the location of the test harness spring beans context file.
     * Subclasses may override to specify a different location.
     * @return the location of the test harness spring beans context file.
     */
    protected List<String> getTestHarnessSpringBeansLocation() {
        return Collections.singletonList( DEFAULT_TEST_HARNESS_SPRING_BEANS );
    }

    protected Config getTestHarnessConfig() throws Exception {
        Config config = new JAXBConfigImpl(getConfigLocations(), System.getProperties());
        config.parseConfig();
        return config;
    }

    /**
     * Subclasses may override this method to customize the location(s) of the Rice configuration.
     * By default it is: classpath:META-INF/" + getModuleName().toLowerCase() + "-test-config.xml"
     * @return List of config locations to add to this tests config location.
     */
    protected List<String> getConfigLocations() {
        List<String> configLocations = new ArrayList<String>();
        configLocations.add(getRiceMasterDefaultConfigFile());
        configLocations.add(getModuleTestConfigLocation());
        return configLocations;
    }
    
    protected String getModuleTestConfigLocation() {
        return "classpath:META-INF/" + getModuleName().toLowerCase() + "-test-config.xml";
    }

    protected String getRiceMasterDefaultConfigFile() {
        return "classpath:META-INF/test-config-defaults.xml";
    }

    /**
     * same as the module directory in the project.
     * 
     * @return name of module that the tests located
     */
    protected abstract String getModuleName();

    protected void setClearTables(boolean clearTables) {
    	this.clearTables = clearTables;
    }
    
}
