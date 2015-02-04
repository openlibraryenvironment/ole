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
import org.kuali.ole.utility.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: peris
 * Date: 7/22/11
 * Time: 2:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class DateTimeUtil_UT
        extends BaseTestCase {
    private static Logger LOG = LoggerFactory.getLogger(DateTimeUtil_UT.class);

    @Test
    public void testFormatTime() throws Exception {
        long startTime = System.currentTimeMillis();
        Thread.sleep(20);
        long endTime = System.currentTimeMillis();
        String formattedTime = DateTimeUtil.formatTime(endTime - startTime);
        assertNotNull(formattedTime);
        LOG.info(formattedTime);
    }

    @Test
    public void testFormatTime1() throws Exception {
        long startTime = System.currentTimeMillis();
        Thread.sleep(10);
        long endTime = System.currentTimeMillis();

        String formattedTime = DateTimeUtil.formatTime(endTime, startTime);
        assertNotNull(formattedTime);
        LOG.info(formattedTime);
    }
}
