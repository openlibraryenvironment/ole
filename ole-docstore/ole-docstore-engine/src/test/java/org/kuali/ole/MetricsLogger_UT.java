/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole;

import org.junit.Test;
import org.kuali.ole.logger.MetricsLogger;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: peris
 * Date: 8/5/11
 * Time: 1:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class MetricsLogger_UT
        extends BaseTestCase {
    @Test
    public void testRecordTiminigs() throws Exception {
        MetricsLogger metricsLogger = new MetricsLogger(this.getClass().getName());
        metricsLogger.startRecording();
        Thread.sleep(10);
        metricsLogger.endRecording();
        metricsLogger.printTimes("Time taken for the unit test");
    }

    @Test
    public void testMetricsLoggerWithInvalidClass() throws Exception {
        MetricsLogger metricsLogger = new MetricsLogger("abc");
        assertNotNull(metricsLogger);
    }
}
