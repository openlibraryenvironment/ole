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
package org.kuali.rice.ken.test;

import org.kuali.rice.core.api.lifecycle.BaseLifecycle;
import org.kuali.rice.core.api.lifecycle.Lifecycle;
import org.kuali.rice.core.framework.resourceloader.RiceResourceLoaderFactory;
import org.kuali.rice.core.framework.resourceloader.SpringResourceLoader;
import org.kuali.rice.ken.core.SpringNotificationServiceLocator;
import org.kuali.rice.kew.batch.KEWXmlDataLoader;
import org.kuali.rice.test.BaselineTestCase;
import org.kuali.rice.test.BaselineTestCase.BaselineMode;
import org.kuali.rice.test.BaselineTestCase.Mode;
import org.kuali.rice.test.CompositeBeanFactory;
import org.kuali.rice.test.SQLDataLoader;
import org.kuali.rice.test.lifecycles.KEWXmlDataLoaderLifecycle;
import org.kuali.rice.test.lifecycles.SQLDataLoaderLifecycle;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.transaction.PlatformTransactionManager;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;


/**
 * Base test case for KEN that extends RiceTestCase
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BaselineMode(Mode.ROLLBACK_CLEAR_DB)
public abstract class KENTestCase extends BaselineTestCase {
    private static final String KEN_MODULE_NAME = "ken";
    private static final String TX_MGR_BEAN_NAME = "transactionManager";

    protected SpringNotificationServiceLocator services;
    protected PlatformTransactionManager transactionManager;

    public KENTestCase() {
        super(KEN_MODULE_NAME);
    }   
    
    
    
    @Override
	protected List<Lifecycle> getSuiteLifecycles() {
		List<Lifecycle> suiteLifecycles = super.getSuiteLifecycles();
		suiteLifecycles.add(new KEWXmlDataLoaderLifecycle("classpath:org/kuali/rice/ken/test/DefaultSuiteTestData.xml"));
		return suiteLifecycles;
	}
    
    @Override
	protected Lifecycle getLoadApplicationLifecycle() {
    	SpringResourceLoader springResourceLoader = new SpringResourceLoader(new QName("KENTestHarnessApplicationResourceLoader"), "classpath:KENTestHarnessSpringBeans.xml", null);
    	springResourceLoader.setParentSpringResourceLoader(getTestHarnessSpringResourceLoader());
    	return springResourceLoader;
	}

	@Override
    protected List<Lifecycle> getPerTestLifecycles() {
    	List<Lifecycle> lifecycles = super.getPerTestLifecycles();
    	lifecycles.add(new ClearCacheLifecycle());
    	lifecycles.addAll(getNotificationPerTestLifecycles());
    	return lifecycles;
    }
    
	protected List<Lifecycle> getNotificationPerTestLifecycles() {
        List<Lifecycle> lifecycles = new ArrayList<Lifecycle>();
        lifecycles.add(new BaseLifecycle() {
            @Override
            public void start() throws Exception {
                // get the composite Rice Spring context
                BeanFactory moduleContext = CompositeBeanFactory.createBeanFactory(
                        RiceResourceLoaderFactory.getSpringResourceLoaders());
                // This method sets up the Spring services so that they can be accessed by the tests.
                services = new SpringNotificationServiceLocator(moduleContext);
                // grab the module's transaction manager
                transactionManager = (PlatformTransactionManager) moduleContext.getBean(TX_MGR_BEAN_NAME, PlatformTransactionManager.class);
                super.start();
            }

        });

        // clear out the KEW cache
        lifecycles.add(new BaseLifecycle() {
            @Override
            public void start() throws Exception {
                super.start();

                LOG.info("Status of Ken scheduler on start: " + (services.getScheduler().isStarted() ? "started" : "stopped"));
                // stop quartz if a test failed to do so
                disableQuartzJobs();
            }
            public void stop() throws Exception {
                //KsbApiServiceLocator.getCacheAdministrator().flushAll();

                LOG.info("Status of Ken scheduler on stop: " + (services.getScheduler().isStarted() ? "started" : "stopped"));
                // stop quartz if a test failed to do so
                disableQuartzJobs();

                super.stop();
            }
        });

        // load the default SQL
        //lifecycles.add(new SQLDataLoaderLifecycle("classpath:org/kuali/rice/ken/test/DefaultPerTestData.sql", ";"));
        
        //lifecycles.add(new KEWXmlDataLoaderLifecycle("classpath:org/kuali/rice/ken/test/DefaultPerTestData.xml"));
        

        return lifecycles;

    }

    /**
	 * By default this loads the "default" data set from the DefaultTestData.sql
	 * and DefaultTestData.xml files. Subclasses can override this to change
	 * this behaviour
	 */
	protected void loadDefaultTestData() throws Exception {
		// at this point this is constants. loading these through xml import is
		// problematic because of cache notification
		// issues in certain low level constants.
		new SQLDataLoader(
				"classpath:org/kuali/rice/ken/test/DefaultPerTestData.sql", ";")
				.runSql();

		KEWXmlDataLoader.loadXmlClassLoaderResource(KENTestCase.class, "DefaultPerTestData.xml");
	}
    /**
	 * Returns the List of tables that should be cleared on every test run.
	 */
    @Override
	protected List<String> getPerTestTablesToClear() {
		List<String> tablesToClear = new ArrayList<String>();
		tablesToClear.add("KREW_.*");
		tablesToClear.add("KRSB_.*");
		tablesToClear.add("KREN_.*");
		return tablesToClear;
	}

    protected void setUpAfterDataLoad() throws Exception {
		// override this to load your own test data
	}

    /**
	 * Initiates loading of per-test data
	 */
	@Override
	protected void loadPerTestData() throws Exception {
        loadDefaultTestData();


		setUpAfterDataLoad();

		final long t4 = System.currentTimeMillis();
	}

    /**
     * Flushes the KEW cache(s)
     */
    public class ClearCacheLifecycle extends BaseLifecycle {
        @Override
        public void stop() throws Exception {
            //KsbApiServiceLocator.getCacheAdministrator().flushAll();
            //KimApiServiceLocator.getIdentityManagementService().flushAllCaches();
            //KimApiServiceLocator.getRoleService().flushRoleCaches();
            super.stop();
        }

    }
    /**
     * This method makes sure to disable the Quartz scheduler
     * @throws SchedulerException
     */
    protected void disableQuartzJobs() throws SchedulerException {
        // do this so that our quartz jobs don't go off - we don't care about
        // these in our unit tests
        Scheduler scheduler = services.getScheduler();
        scheduler.standby();
        //scheduler.shutdown();
    }

    /**
     * This method enables the Quartz scheduler
     * @throws SchedulerException
     */
    protected void enableQuartzJobs() throws SchedulerException {
        // do this so that our quartz jobs don't go off - we don't care about
        // these in our unit tests
        Scheduler scheduler = services.getScheduler();
        scheduler.start();
    }

}
