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
package edu.samplu.admin.test;

import org.junit.Test;

/**
 * JUnit implementation of XMLIngesterSTJUnitBase that navigates through the UI to the page under test.  In the future
 * the idea is to generate this class using the test methods from XMLIngesterAbstractSmokeTestBase and following the simple pattern of
 * <pre>super.testMethodNav(this);</pre>
 *
 * <pre>mvn -f rice-middleware/sampleapp/pom.xml -Pstests failsafe:integration-test -Dremote.public.url=env7.rice.kuali.org -Dit.test=XMLIngesterSTJUnitNavGen -DXMLIngester.groupId=2008 -DXMLIngester.userIncludeDTSinPrefix=false -DXMLIngester.userCntBegin=0 -DXMLIngester.userCnt=600  -DXMLIngester.userPrefix=loadtester -Dremote.driver.dontTearDown=y</pre>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class XMLIngesterSTJUnitNavGen extends XMLIngesterSTJUnitBase {

    /**
     * @link XMLIngesterAbstractSmokeTestBase#testIngestionNav
     * @throws Exception
     */
    @Test
    public void testXMLIngesterSuccessfulFileUpload() throws Exception {
        testIngestionNav(this);
    }
}
