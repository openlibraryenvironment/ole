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
package org.kuali.rice.core.api.uif

import org.junit.Test
import static org.junit.Assert.*
import org.kuali.rice.core.test.JAXBAssert

class RemotableTextInputTest {
           	private static final String XML =
        """<textInput xmlns="http://rice.kuali.org/core/v2_0">
            <size>2</size>
            <watermark>foo</watermark>
          </textInput>""";

    @Test
    void testHappyPath() {
        RemotableTextInput o = RemotableTextInput.Builder.create().build();
        assertNotNull(o);
    }

    @Test
    void testHappyPath2() {
        RemotableTextInput.Builder o = create();
        o.size = 2
        o.watermark = "foo"

        assertNotNull(o.build());
    }

    @Test
    void testOptional() {
        RemotableTextInput.Builder o = create();
        o.size = null
        o.watermark = null

        assertNotNull(o.build());
    }

    @Test(expected=IllegalArgumentException.class)
    void testbadSize() {
        RemotableTextInput.Builder o = create();
        o.size = 0

        assertNotNull(o.build());
    }

    @Test
	void testJAXB() {
		RemotableTextInput o = create().build();
		JAXBAssert.assertEqualXmlMarshalUnmarshal(o, XML, RemotableTextInput.class);
	}

    private RemotableTextInput.Builder create() {
		RemotableTextInput.Builder o = RemotableTextInput.Builder.create();
        o.size = 2
        o.watermark = "foo"
        return o
	}
}
