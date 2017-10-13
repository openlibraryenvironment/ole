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
package org.kuali.rice.krad.datadictionary;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * DataDictionaryBuilderTest tests the DataDictionaryBuilder
 *
 *
 */

public class DataDictionaryBuilderTest {
    protected final Logger LOG = Logger.getLogger(getClass());

    static final String PACKAGE_CORE_BO = "org/kuali/rice/krad/bo/datadictionary/";

	static final String PACKAGE_CORE_DOCUMENT = "org/kuali/rice/krad/document/datadictionary/";

	static final String PACKAGE_KFS = "org/kuali/kfs/datadictionary/";

	static final String PACKAGE_CHART = "org/kuali/module/chart/datadictionary/";

	static final String PACKAGE_CG = "org/kuali/module/cg/datadictionary/";

	static final String PACKAGE_KRA_BUDGET = "org/kuali/module/kra/budget/datadictionary/";

	static final String PACKAGE_KRA_ROUTINGFORM = "org/kuali/module/kra/routingform/datadictionary/";

	static final String TESTPACKAGE_INVALID = "org/kuali/rice/krad/datadictionary/test/invalid/";

	DataDictionary dd = null;

	@Before
	public void setUp() throws Exception {

		dd = new DataDictionary();
	}

	@After
	public void tearDown() throws Exception {
		dd = null;
	}

	@Test
	public final void testDataDictionaryBuilder_source_invalid() throws Exception {
		boolean failedAsExpected = false;

		try {
			dd.addConfigFileLocation("KR-SAP", null);
		} catch (DataDictionaryException e) {
			failedAsExpected = true;
		}

		assertTrue(failedAsExpected);
	}

	@Test
	public final void testDataDictionaryBuilder_source_unknownFile() throws Exception {
		String INPUT_FILE = TESTPACKAGE_INVALID + "foo.xml";

		boolean failedAsExpected = false;

		try {
			dd.addConfigFileLocation("KR-SAP", INPUT_FILE);
		} catch (DataDictionaryException e) {
			failedAsExpected = true;
		}

		assertTrue(failedAsExpected);
	}

	@Test
	public final void testDataDictionaryBuilder_source_unknownPackage() throws Exception {
		String UNKNOWN_PACKAGE = TESTPACKAGE_INVALID + "foo/";

		boolean failedAsExpected = false;

		try {
			dd.addConfigFileLocation("KR-SAP", UNKNOWN_PACKAGE);
		} catch (DataDictionaryException e) {
			failedAsExpected = true;
		}

		assertTrue(failedAsExpected);
	}

	@Test
	public final void testDataDictionaryBuilder_invalidXml() throws Exception {
		String INPUT_FILE = TESTPACKAGE_INVALID + "InvalidXml.xml";

		boolean failedAsExpected = false;

		try {
			dd.addConfigFileLocation("KR-SAP", INPUT_FILE);
			dd.parseDataDictionaryConfigurationFiles( false );
		} catch (DataDictionaryException e) {
			failedAsExpected = true;
		} catch (Exception e) {
            LOG.error("Error loading DD files", e);
		    fail("Data Dictionary file load failed but with wrong exception type '" + e.getClass().getName() + "'");
		}

		assertTrue(failedAsExpected);
	}

	@Test
	public final void testDataDictionaryBuilder_getInvalidDictionary() throws Exception {
		String INPUT_FILE = TESTPACKAGE_INVALID + "InvalidXml.xml";

		boolean failedAsExpected = false;

		try {
			dd.addConfigFileLocation("KR-SAP", INPUT_FILE);
			dd.parseDataDictionaryConfigurationFiles( false );
		} catch (DataDictionaryException e) {
			failedAsExpected = true;
        } catch (Exception e) {
            LOG.error("Error loading DD files", e);
            fail("Data Dictionary file load failed but with wrong exception type '" + e.getClass().getName() + "'");
		}

		assertTrue(failedAsExpected);
	}

}
