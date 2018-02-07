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

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.kuali.rice.test.lifecycles.PerTestDataLoaderLifecycle;
import org.kuali.rice.test.runners.RiceUnitTestClassRunner;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A generic Rice Unit Test base class.
 * 
 * 1) Sets up a generic logger.
 * 2) Sets the name of the class being run to mimic jUnit 3 functionality.
 * 3) Stores the name of the method being run for use by subclasses (set by {@link RiceUnitTestClassRunner}
 * 4) Sets the PerTestDataLoaderLifecycle that will load sql for the currently running test.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @since 0.9
 */
@RunWith(RiceUnitTestClassRunner.class)
public abstract class BaseRiceTestCase implements MethodAware {

	protected final Logger log = Logger.getLogger(getClass());

	private static final Map<String, Level> changedLogLevels = new HashMap<String, Level>();
	
	private String name;
	private PerTestDataLoaderLifecycle perTestDataLoaderLifecycle;
	protected Method method;

	public BaseRiceTestCase() {
		super();
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
	@Before
	public void setUp() throws Exception {
	}
	
    @After
    public void tearDown() throws Exception {
        resetLogLevels();
    }
    
    /**
     * Changes the logging-level associated with the given loggerName to the
     * given level. The original logging-level is saved, and will be
     * automatically restored at the end of each test.
     * 
     * @param loggerName
     *            name of the logger whose level to change
     * @param newLevel
     *            the level to change to
     */
    protected void setLogLevel(String loggerName, Level newLevel) {
        Logger logger = Logger.getLogger(loggerName);

        if (!changedLogLevels.containsKey(loggerName)) {
            Level originalLevel = logger.getLevel();
            changedLogLevels.put(loggerName, originalLevel);
        }

        logger.setLevel(newLevel);
    }

    /**
     * Restores the logging-levels changed through calls to setLogLevel to their
     * original values.
     */
    protected void resetLogLevels() {
        for (Iterator i = changedLogLevels.entrySet().iterator(); i.hasNext();) {
            Map.Entry e = (Map.Entry) i.next();

            String loggerName = (String) e.getKey();
            Level originalLevel = (Level) e.getValue();

            Logger.getLogger(loggerName).setLevel(originalLevel);
        }
        changedLogLevels.clear();
    }
    
	/**
	 * @see org.kuali.rice.test.MethodAware#setTestMethod(java.lang.reflect.Method)
	 */
	public void setTestMethod(Method testMethod) {
        this.method = testMethod;

        perTestDataLoaderLifecycle = new PerTestDataLoaderLifecycle(method);
    }
	
    protected PerTestDataLoaderLifecycle getPerTestDataLoaderLifecycle() {
		return this.perTestDataLoaderLifecycle;
	}
}
