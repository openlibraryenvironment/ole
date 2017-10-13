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
package org.kuali.rice.krad.test;

/**
 * Provides centralized storage of constants that occur throughout the tests
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class KRADTestConstants {
    
    public static final String TEST_NAMESPACE_CODE = "TEST";

    public static final class TestConstants {
        private static final String HOST = "localhost";
        private static final String PORT = "8080";
        public static final String BASE_PATH = "http://" + HOST + ":" + PORT + "/";
        public static final String MESSAGE =
                "JUNIT test entry. If this exist after the tests are not cleaning up correctly. Created by class";

        private TestConstants() {
            throw new UnsupportedOperationException("do not call");
        }
    }

    private KRADTestConstants() {
        throw new UnsupportedOperationException("do not call");
    }
}
