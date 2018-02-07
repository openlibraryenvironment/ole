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

import org.apache.log4j.Logger;

/**
 * Base module test case that allows overriding of the test harness spring beans
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class BaseModuleTestCase extends RiceInternalSuiteDataTestCase {
    protected final Logger LOG = Logger.getLogger(getClass());

    protected final String moduleName;

    /**
     * @param moduleName module name
     */
    public BaseModuleTestCase(String moduleName) {
        this.moduleName = moduleName;
    }

    /**
     * @see org.kuali.rice.test.RiceTestCase#getModuleName()
     */
    @Override
    protected String getModuleName() {
        return moduleName;
    }

}
