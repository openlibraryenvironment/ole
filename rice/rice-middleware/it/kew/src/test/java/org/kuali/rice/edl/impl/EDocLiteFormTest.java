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
package org.kuali.rice.edl.impl;

/*import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;*/
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.rice.test.BaseRiceTestCase;

/**
 * Tests the behavior of EDocLiteForm in the presence of various types of input
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class EDocLiteFormTest extends BaseRiceTestCase {
    /**
     * Tests how EDocLiteForm handles parameters with multiple values
     */
	@Ignore("This test needs to be implemented!")
	@Test public void testMultipleValuesPlainPOST() throws Exception {
//        MockHttpServletRequest req = new MockHttpServletRequest();
//        req.setMethod("post");
//        req.addParameter("multiple_value", "value0");
//        req.addParameter("multiple_value", "value1");
//        req.addParameter("single_value", "value0");
//        String[] values = req.getParameterValuesAsString("multiple_value");
//        assertNotNull(values);
//        assertEquals(2, values.length);
//        values = req.getParameterValuesAsString("single_value");
//        assertNotNull(values);
//        assertEquals(1, values.length);
//
//        values = (String[]) req.getParameterMap().get("multiple_value");
//        assertNotNull(values);
//        assertEquals(2, values.length);
//        values = (String[]) req.getParameterMap().get("single_value");
//        assertNotNull(values);
//        assertEquals(1, values.length);
//
//        EDocLiteForm form = new EDocLiteForm(req);
//        values = (String[]) form.getParameterMap().get("multiple_value");
//        assertNotNull(values);
//        assertEquals(2, values.length);
//        values = (String[]) form.getParameterMap().get("single_value");
//        assertNotNull(values);
//        assertEquals(1, values.length);
//
//        req.addParameter("action", "testAction");
//        form = new EDocLiteForm(req);
//        assertEquals("testAction", form.getAction());
//
//        req.addParameter("action", "testAction2");
//        form = new EDocLiteForm(req);
//        assertEquals("testAction", form.getAction());
    }

    /* this requires Jakarta Commons HttpClient to compile and
       Commons Codec, IO, and Collections as well to run */
    /*
    public void testMultipleValuesMultipart() throws IOException {
        Part[] parts = {
            new StringPart("multiple_value", "value0"),
            new StringPart("multiple_value", "value1"),
            new StringPart("single_value", "value0")
        };
        MultipartRequestEntity mp = new MultipartRequestEntity(parts, new PostMethod().getParams());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        mp.writeRequest(baos);

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setMethod("post");
        req.setContentType(mp.getContentType());
        req.setContent(baos.toByteArray());

        EDocLiteForm form = new EDocLiteForm(req);
        Object o = form.getParameterMap().get("multiple_value");
        assertNotNull(o);
        log.info(o.toString());
        assertTrue("multiple_value parameter value is not a String[]: " + o.getClass(), o instanceof String[]);

        String[] values = (String[]) o;
        assertEquals(2, values.length);
        o = form.getParameterMap().get("single_value");
        assertNotNull(o);
        assertTrue("single_value parameter value is not a String[]: " + o.getClass(), o instanceof String[]);
        values = (String[]) o;
        assertNotNull(values);
        assertEquals(1, values.length);
        assertEquals("testAction", form.getAction());
    }*/
}
