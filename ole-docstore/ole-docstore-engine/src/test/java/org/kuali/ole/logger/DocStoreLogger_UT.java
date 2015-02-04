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
package org.kuali.ole.logger;

import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.BaseTestCase;

/**
 * Created by IntelliJ IDEA.
 * User: peris
 * Date: 8/6/11
 * Time: 10:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class DocStoreLogger_UT
        extends BaseTestCase {

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testLog() throws Exception {
        DocStoreLogger docStoreLogger = new DocStoreLogger(DocStoreLogger_UT.class.getName());
        docStoreLogger.log("info message");
    }

    @Test
    public void testLogWithException() throws Exception {
        DocStoreLogger docStoreLogger = new DocStoreLogger(DocStoreLogger_UT.class.getName());
        docStoreLogger.log("error message", new Exception("mock error exception"));

    }

    @Test
    public void testDebug() throws Exception {
        DocStoreLogger docStoreLogger = new DocStoreLogger(DocStoreLogger_UT.class.getName());
        docStoreLogger.logDebugMsg("debug");
    }
}
