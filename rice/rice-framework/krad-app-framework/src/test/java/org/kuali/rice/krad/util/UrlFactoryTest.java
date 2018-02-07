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
package org.kuali.rice.krad.util;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertTrue;

/**
 * UrlFactoryTest tests the UrlFactory methods
 */
public class UrlFactoryTest {

    /**
     * Test that what is returned from url factory matches the url we expect.
     */
    @Test public void testFactoryMatch() throws Exception {
        String basePath = "http://localhost:8080/";
        String actionPath = "kr/lookup.do";
        String testUrl = basePath + actionPath + "?" + KRADConstants.DISPATCH_REQUEST_PARAMETER + "=start" + "&" + KRADConstants.DOC_FORM_KEY + "=903" + KRADConstants.LOOKUPABLE_IMPL_ATTRIBUTE_NAME + "=accountLookupableImpl" + KRADConstants.RETURN_LOCATION_PARAMETER + "=" + basePath + "ib.do";
        testUrl = UrlFactory.encode(testUrl);

        // construct lookup url
        Properties parameters = new Properties();
        parameters.put(KRADConstants.DISPATCH_REQUEST_PARAMETER, "start");
        parameters.put(KRADConstants.DOC_FORM_KEY, "903");
        parameters.put(KRADConstants.LOOKUPABLE_IMPL_ATTRIBUTE_NAME, "accountLookupableImpl");
        parameters.put(KRADConstants.RETURN_LOCATION_PARAMETER, basePath + "ib.do");

        String returnedUrl = UrlFactory.parameterizeUrl(basePath + actionPath, parameters);

        assertTrue("Returned url is empty", StringUtils.isNotBlank(returnedUrl));
        assertTrue("Returned url has incorrect base", returnedUrl.startsWith(basePath + actionPath + "?"));
        assertTrue("Returned url does not have correct # of &", StringUtils.countMatches(returnedUrl, "&") == 3);
        assertTrue("Returned url missing parameter 1", StringUtils.contains(returnedUrl, KRADConstants.DISPATCH_REQUEST_PARAMETER + "=start"));
        assertTrue("Returned url missing parameter 2", StringUtils.contains(returnedUrl, KRADConstants.DOC_FORM_KEY + "=903"));
        assertTrue("Returned url missing parameter 3", StringUtils.contains(returnedUrl, KRADConstants.LOOKUPABLE_IMPL_ATTRIBUTE_NAME + "=accountLookupableImpl"));
        // assertTrue("Returned url missing parameter 4",StringUtils.contains(returnedUrl,
        // UrlFactory.encode(KRADConstants.RETURN_LOCATION_PARAMETER + "=" + basePath + "ib.do")));
    }
}
