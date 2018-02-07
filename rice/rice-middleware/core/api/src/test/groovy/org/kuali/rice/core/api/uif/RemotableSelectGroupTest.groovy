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
import org.kuali.rice.core.test.JAXBAssert;

class RemotableSelectGroupTest {
            private static final String XML =
        """<selectGroup xmlns="http://rice.kuali.org/core/v2_0">
            <keyLabels>
		        <entry key="foo">bar</entry>
	        </keyLabels>
            <label>first_label</label>
          </selectGroup>""";

    @Test
    void testHappyPath() {
        RemotableSelectGroup o = RemotableSelectGroup.Builder.create(["foo":"bar"], "my_label").build();
        assertNotNull(o);
    }

    @Test(expected=IllegalArgumentException.class)
    void testbadLabel() {
        RemotableSelectGroup.Builder o = create();
        o.label = null
    }

    @Test(expected=IllegalArgumentException.class)
    void testEmptyKeyLabels() {
        RemotableSelectGroup o = RemotableSelectGroup.Builder.create([:], "foo").build()
    }

    @Test(expected=IllegalArgumentException.class)
    void testEmptyLabel() {
        RemotableSelectGroup o = RemotableSelectGroup.Builder.create(["foo":"bar"], " ").build()
    }

    @Test(expected=IllegalArgumentException.class)
    void testNullKeyLabels() {
        RemotableSelectGroup.Builder o = RemotableSelectGroup.Builder.create(null, "foo")
    }

    @Test(expected=IllegalArgumentException.class)
    void testNullLabel() {
        RemotableSelectGroup o = RemotableSelectGroup.Builder.create(["foo":"bar"], null).build()
    }

    @Test
	void testJAXB() {
		RemotableSelectGroup o = create().build();
		JAXBAssert.assertEqualXmlMarshalUnmarshal(o, XML, RemotableSelectGroup.class);
	}

    private RemotableSelectGroup.Builder create() {
		RemotableSelectGroup.Builder o = RemotableSelectGroup.Builder.create(["foo":"bar"], "first_label");
        return o
	}
}
