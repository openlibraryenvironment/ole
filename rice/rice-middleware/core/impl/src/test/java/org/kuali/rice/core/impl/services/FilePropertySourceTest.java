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
package org.kuali.rice.core.impl.services;

import org.junit.Test;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.impl.services.ConfigurationServiceImpl;

import static org.junit.Assert.assertTrue;

/**
 * This class tests the FilePropertySource methods.
 */
public class FilePropertySourceTest {

    @Test public void testLoadProperties_defaultFileName() {
        ConfigurationServiceImpl.FilePropertySource fps = new ConfigurationServiceImpl.FilePropertySource();

        boolean failedAsExpected = false;
        try {
            fps.loadProperties();
        }
        catch (IllegalStateException e) {
            failedAsExpected = true;
        }

        assertTrue(failedAsExpected);
    }

    @Test public void testLoadProperties_invalidFileName() {
        ConfigurationServiceImpl.FilePropertySource fps = new ConfigurationServiceImpl.FilePropertySource();
        fps.setFileName("      ");

        boolean failedAsExpected = false;
        try {
            fps.loadProperties();
        }
        catch (IllegalStateException e) {
            failedAsExpected = true;
        }

        assertTrue(failedAsExpected);
    }

    @Test public void testLoadProperties_unknownFileName() {
        ConfigurationServiceImpl.FilePropertySource fps = new ConfigurationServiceImpl.FilePropertySource();
        fps.setFileName("unknown");

        boolean failedAsExpected = false;
        try {
            fps.loadProperties();
        }
        catch (RiceRuntimeException e) {
            failedAsExpected = true;
        }

        assertTrue(failedAsExpected);
    }

    @Test public void testLoadProperties_knownFileName_noSuffix() {
        ConfigurationServiceImpl.FilePropertySource fps = new ConfigurationServiceImpl.FilePropertySource();
        fps.setFileName("configuration");

        boolean failedAsExpected = false;
        try {
            fps.loadProperties();
        }
        catch (RiceRuntimeException e) {
            failedAsExpected = true;
        }

        assertTrue(failedAsExpected);
    }
}
