/*
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
package edu.samplu.common;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import java.lang.reflect.Method;

/**
 * JUnit Test Runner to run test SmokeTests.  Enables bookmark mode for test methods
 * ending in Bookmark and navigation mode for test methods ending in Nav.
 */
public class SmokeTestRunner extends BlockJUnit4ClassRunner {

    /**
     * SmokeTestRunner constructor
     * @param type
     * @throws InitializationError
     */
    public SmokeTestRunner(Class<?> type) throws InitializationError {
        super(type);
    }

    @Override
    protected Statement methodInvoker(FrameworkMethod method, Object test) {
        Method testMethod = method.getMethod();
        final String testClass = test.getClass().toString();
        if (testMethod.getName().endsWith("Bookmark") ||
           (testClass.endsWith("WDIT")) ||
           (testClass.endsWith("BkMrkGen"))) {
            ((SmokeTestBase) test).enableBookmarkMode();
        } else if (testMethod.getName().endsWith("Nav") ||
                  (testClass.endsWith("NavIT"))) {
            ((SmokeTestBase) test).enableNavigationMode();
        }
        return super.methodInvoker(method, test);
    }
}