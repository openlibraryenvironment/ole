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
package org.kuali.rice.kew.test;

import org.kuali.rice.core.api.lifecycle.BaseLifecycle;
import org.kuali.rice.core.api.lifecycle.Lifecycle;
import org.kuali.rice.core.framework.resourceloader.SpringResourceLoader;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.batch.KEWXmlDataLoader;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.services.KimImplServiceLocator;
import org.kuali.rice.test.BaselineTestCase;
import org.kuali.rice.test.ClearDatabaseLifecycle;
import org.kuali.rice.test.SQLDataLoader;
import org.kuali.rice.test.lifecycles.KEWXmlDataLoaderLifecycle;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.xml.namespace.QName;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Useful superclass for all KEW test cases. Handles setup of test utilities and
 * a test environment. Configures the Spring test environment providing a
 * template method for custom context files in test mode. Also provides a
 * template method for running custom transactional setUp. Tear down handles
 * automatic tear down of objects created inside the test environment.
 */
@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.ROLLBACK_CLEAR_DB)
public abstract class KEWTestCase extends BaselineTestCase {

    private static final String SQL_FILE = "classpath:org/kuali/rice/kew/test/DefaultSuiteTestData.sql";
	private static final String XML_FILE = "classpath:org/kuali/rice/kew/test/DefaultSuiteTestData.xml";
    private static final String KRAD_MODULE_NAME = "kew";

    public KEWTestCase() {
		super(KRAD_MODULE_NAME);
	}

    @Override
	protected List<Lifecycle> getSuiteLifecycles() {
		List<Lifecycle> suiteLifecycles = super.getSuiteLifecycles();
		suiteLifecycles.add(new KEWXmlDataLoaderLifecycle(XML_FILE));

		return suiteLifecycles;
	}

	@Override
	protected void loadSuiteTestData() throws Exception {
		super.loadSuiteTestData();
        new SQLDataLoader(SQL_FILE, ";").runSql();
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
				"classpath:org/kuali/rice/kew/test/DefaultPerTestData.sql", ";")
				.runSql();

		KEWXmlDataLoader.loadXmlClassLoaderResource(KEWTestCase.class,
				"DefaultPerTestData.xml");
	}

	@Override
	protected Lifecycle getLoadApplicationLifecycle() {
        SpringResourceLoader springResourceLoader = new SpringResourceLoader(new QName("KEWTestResourceLoader"), "classpath:org/kuali/rice/kew/config/TestKEWSpringBeans.xml", null);
    	springResourceLoader.setParentSpringResourceLoader(getTestHarnessSpringResourceLoader());
    	return springResourceLoader;
	}

    /**
	 * Default implementation does nothing. Subclasses should override this
	 * method if they want to perform setup work inside of a database
	 * transaction.
	 */
	protected void setUpAfterDataLoad() throws Exception {
		// override
	}

	protected void loadTestData() throws Exception {
		// override this to load your own test data
	}

	protected TransactionTemplate getTransactionTemplate() {
		return TestUtilities.getTransactionTemplate();
	}

   /**
	 * Initiates loading of per-test data
	 */
	@Override
	protected void loadPerTestData() throws Exception {
        final long t1 = System.currentTimeMillis();
        loadDefaultTestData();

		final long t2 = System.currentTimeMillis();
		loadTestData();

		final long t3 = System.currentTimeMillis();
		report("Time to load test-specific test data: " + (t3 - t2));

		setUpAfterDataLoad();

		final long t4 = System.currentTimeMillis();
		report("Time to run test-specific setup: " + (t4 - t3));
	}

	/**
	 * Override the standard per-test lifecycles to prepend
	 * ClearDatabaseLifecycle and ClearCacheLifecycle
	 *
	 * @see org.kuali.rice.test.RiceTestCase#getPerTestLifecycles()
	 */
	@Override
	protected List<Lifecycle> getPerTestLifecycles() {
		List<Lifecycle> lifecycles = new ArrayList<Lifecycle>();
		lifecycles.add(new ClearDatabaseLifecycle(getPerTestTablesToClear(),
				getPerTestTablesNotToClear()));
		lifecycles.add(new ClearCacheLifecycle());
		lifecycles.addAll(super.getPerTestLifecycles());
		return lifecycles;
	}

	/**
	 * Flushes the KEW cache(s)
	 */
	public class ClearCacheLifecycle extends BaseLifecycle {
		@Override
		public void stop() throws Exception {
            clearCacheManagers(KimImplServiceLocator.getLocalCacheManager(), KEWServiceLocator.getLocalCacheManager());
			super.stop();
		}
	}

    protected void clearCacheManagers(CacheManager... cacheManagers) {
        for (CacheManager cacheManager : cacheManagers) {
            for (String cacheName : cacheManager.getCacheNames()) {
                LOG.info("Clearing cache: " + cacheName);
                cacheManager.getCache(cacheName).clear();
            }
        }
    }

	/**
	 * Returns the List of tables that should be cleared on every test run.
	 */
	protected List<String> getPerTestTablesToClear() {
		List<String> tablesToClear = new ArrayList<String>();
		tablesToClear.add("KREW_.*");
		tablesToClear.add("KRSB_.*");
		tablesToClear.add("KREN_.*");
        tablesToClear.add("KRMS_.*");
		return tablesToClear;
	}

	protected List<String> getPerTestTablesNotToClear() {
		return new ArrayList<String>();
	}

	protected void loadXmlFile(String fileName) {
		try {
			KEWXmlDataLoader.loadXmlClassLoaderResource(getClass(), fileName);
		} catch (Exception e) {
			throw new WorkflowRuntimeException(e);
		}
	}

	protected void loadXmlFile(Class clazz, String fileName) {
		try {
			KEWXmlDataLoader.loadXmlClassLoaderResource(clazz, fileName);
		} catch (Exception e) {
			throw new WorkflowRuntimeException(e);
		}
	}

	protected void loadXmlFileFromFileSystem(String fileName) {
		try {
			KEWXmlDataLoader.loadXmlFile(fileName);
		} catch (Exception e) {
			throw new WorkflowRuntimeException(e);
		}
	}

	protected void loadXmlStream(InputStream xmlStream) {
		try {
			KEWXmlDataLoader.loadXmlStream(xmlStream);
		} catch (Exception e) {
			throw new WorkflowRuntimeException(e);
		}
	}

	protected String getPrincipalIdForName(String principalName) {
		return KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName(principalName).getPrincipalId();
	}

	protected String getPrincipalNameForId(String principalId) {
		return KimApiServiceLocator.getIdentityService().getPrincipal(principalId).getPrincipalName();
	}

	protected String getGroupIdForName(String namespace, String groupName) {
		return KimApiServiceLocator.getGroupService().getGroupByNamespaceCodeAndName(namespace, groupName).getId();
	}

    protected String getRoleIdForName(String namespace, String roleName) {
        Role role = KimApiServiceLocator.getRoleService().getRoleByNamespaceCodeAndName(namespace, roleName);
        return (role == null) ? null : role.getId();
    }
}
