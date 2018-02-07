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
package org.kuali.rice.krms.test;

import org.junit.Before;
import org.kuali.rice.core.api.lifecycle.Lifecycle;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.framework.resourceloader.SpringResourceLoader;
import org.kuali.rice.core.impl.cache.DistributedCacheManagerDecorator;
import org.kuali.rice.krms.api.KrmsConstants;
import org.kuali.rice.test.BaselineTestCase;
import org.kuali.rice.test.BaselineTestCase.BaselineMode;
import org.kuali.rice.test.BaselineTestCase.Mode;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;

/**
 * This is test base that should be used for all KRMS integration tests.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BaselineMode(Mode.ROLLBACK_CLEAR_DB)
public abstract class KRMSTestCase extends BaselineTestCase {

	private static final String KRMS_MODULE_NAME = "krms";
	
	private SpringResourceLoader krmsTestResourceLoader;
	
	public KRMSTestCase() {
		super(KRMS_MODULE_NAME);
	}

	@Override
	protected List<Lifecycle> getSuiteLifecycles() {
		List<Lifecycle> suiteLifecycles = super.getSuiteLifecycles();
		return suiteLifecycles;
	}
	
	@Override
	protected void loadSuiteTestData() throws Exception {
		super.loadSuiteTestData();
	}
	
	
	
	@Override
	protected Lifecycle getLoadApplicationLifecycle() {
	    if (krmsTestResourceLoader == null) {
	        krmsTestResourceLoader = new SpringResourceLoader(new QName("KRMSTestHarnessApplicationResourceLoader"), "classpath:KRMSTestHarnessSpringBeans.xml", null);
	        krmsTestResourceLoader.setParentSpringResourceLoader(getTestHarnessSpringResourceLoader());
	        getTestHarnessSpringResourceLoader().addResourceLoader(krmsTestResourceLoader);
	    }
    	return krmsTestResourceLoader;
	}

	
	protected List<String> getPerTestTablesNotToClear() {
		List<String> tablesNotToClear = new ArrayList<String>();

        tablesNotToClear.addAll(super.getPerTestTablesNotToClear());
        tablesNotToClear.add("KRIM_.*");
		tablesNotToClear.add("KRNS_.*");
		tablesNotToClear.add("KREW_.*");
		tablesNotToClear.add("KREN_.*");
        tablesNotToClear.add("KRCR_.*");

		return tablesNotToClear;
	}

    protected List<String> getPerTestTablesToClear() {
        List<String> tablesToClear = new ArrayList<String>();

        tablesToClear.addAll(super.getPerTestTablesToClear());
        tablesToClear.add("KRMS_.*");

        return tablesToClear;
    }

    /**
     * @see org.kuali.rice.test.RiceTestCase#getModuleName()
     */
	@Override
	protected String getModuleName() {
		return KRMS_MODULE_NAME;
	}
}
