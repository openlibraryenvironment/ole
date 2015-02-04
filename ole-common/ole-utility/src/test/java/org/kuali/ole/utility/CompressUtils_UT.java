/*
 * Copyright 2012 The Kuali Foundation.
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
package org.kuali.ole.utility;

import static org.junit.Assert.fail;

import java.io.File;

import org.apache.commons.logging.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class CompressUtils_UT to test CompressUtils.
 *
 * @author Rajesh Chowdary K
 * @created May 24, 2012
 */
public class CompressUtils_UT {

    public static final Logger LOG = LoggerFactory.getLogger(CompressUtils_UT.class);

    private CompressUtils fileUtil = new CompressUtils();

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link org.kuali.ole.utility.CompressUtils#createZippedBagFile(java.io.File)}.
     *
     * @throws Exception
     */
    @Test
    public void testCreateZippedBagFile() throws Exception {
        try {
            File dir = new File(getClass().getResource("license").toURI());
            File bagFile = fileUtil.createZippedBagFile(dir);
            LOG.info("Bag File: " + bagFile);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Fialed in creating bag: ");
            throw e;
        }
    }

    /**
     * Test method for {@link org.kuali.ole.utility.CompressUtils#extractZippedBagFile(java.lang.String, java.lang.String)}.
     *
     * @throws Exception
     */
    @Test
    public void testExtractZippedBagFile() throws Exception {

        try {
            File dir = new File(getClass().getResource("license").toURI());
            File bagFile = fileUtil.createZippedBagFile(dir);
            LOG.info("Extracted to location :  " + fileUtil.extractZippedBagFile(bagFile.getAbsolutePath(), null).getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Fialed in extracting bag: ");
            throw e;
        }
    }

}
