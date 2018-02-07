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

import static org.junit.Assert.*
import org.junit.Test
import org.kuali.rice.core.test.JAXBAssert;

class RemotablePasswordInputTest {
               	private static final String XML =
        """<passwordInput xmlns="http://rice.kuali.org/core/v2_0">
            <size>2</size>
          </passwordInput>""";

    @Test
    void testHappyPath() {
        RemotablePasswordInput o = RemotablePasswordInput.Builder.create().build();
        assertNotNull(o);
    }

    @Test
    void testHappyPath2() {
        RemotablePasswordInput.Builder o = create();
        o.size = 2

        assertNotNull(o.build());
    }

    @Test
    void testOptional() {
        RemotablePasswordInput.Builder o = create();
        o.size = null

        assertNotNull(o.build());
    }

    @Test(expected=IllegalArgumentException.class)
    void testbadSize() {
        RemotablePasswordInput.Builder o = create();
        o.size = 0

        assertNotNull(o.build());
    }

    @Test
	void testJAXB() {
		RemotablePasswordInput o = create().build();
		JAXBAssert.assertEqualXmlMarshalUnmarshal(o, XML, RemotablePasswordInput.class);
	}

    private RemotablePasswordInput.Builder create() {
		RemotablePasswordInput.Builder o = RemotablePasswordInput.Builder.create();
        o.size = 2
        return o
	}
}
