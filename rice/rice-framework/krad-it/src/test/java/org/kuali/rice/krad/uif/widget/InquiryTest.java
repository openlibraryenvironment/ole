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
package org.kuali.rice.krad.uif.widget;

import org.junit.Test;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.test.document.BOContainingPerson;
import org.kuali.rice.krad.uif.element.Action;
import org.kuali.rice.krad.uif.element.Link;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.test.KRADTestCase;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link Inquiry}
 */
public class InquiryTest extends KRADTestCase{
    @Test
    /**
     * tests that the inquiry link for a related class is built ok
     */
    public void testBuildInquiryUrl_relatedClass() {
        Inquiry inquiry = new Inquiry();
        inquiry.setParentReadOnly(true);
        inquiry.setInquiryLink(new Link());
        inquiry.setDirectInquiryAction(new Action());
        String baseInquiryUrl = CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString(
                KRADConstants.APPLICATION_URL_KEY);
        inquiry.setBaseInquiryUrl(baseInquiryUrl);
        Map<String, String> params = new HashMap<String, String>();
        params.put("principalId", "principalId");
        BOContainingPerson dataObject = new BOContainingPerson();
        dataObject.setPrincipalId("en");
        inquiry.buildInquiryLink(dataObject, "principalId", Person.class, params);

        assertNotNull("InquiryLink should not be null", inquiry.getInquiryLink());
        assertNotNull("InquiryLink's href should not be null", inquiry.getInquiryLink().getHref());
        String expectedHref = baseInquiryUrl +
                "/kr-krad/inquiry?principalId=en&methodToCall=start&dataObjectClassName=org.kuali.rice.kim.impl.identity.PersonImpl";
        assertEquals("InquiryLink's href is not the expected value", expectedHref, inquiry.getInquiryLink().getHref());
    }
}
