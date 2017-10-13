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

class RemotableRadioButtonGroupTest {
            private static final String XML =
        """<radioButtonGroup xmlns="http://rice.kuali.org/core/v2_0">
            <keyLabels>
		        <entry key="foo">bar</entry>
	        </keyLabels>
          </radioButtonGroup>""";

    @Test
    void testHappyPath() {
        RemotableRadioButtonGroup o = RemotableRadioButtonGroup.Builder.create(["foo":"bar"]).build();
        assertNotNull(o);
    }

    @Test
    void testHappyPath2() {
        RemotableRadioButtonGroup.Builder o = create();

        assertNotNull(o.build());
    }

    @Test(expected=IllegalArgumentException.class)
    void testEmptyKeyLabels() {
        RemotableRadioButtonGroup.Builder o = RemotableRadioButtonGroup.Builder.create([:])
    }

    @Test(expected=IllegalArgumentException.class)
    void testNullKeyLabels() {
        RemotableRadioButtonGroup.Builder o = RemotableRadioButtonGroup.Builder.create(null)
    }

    @Test
	void testJAXB() {
		RemotableRadioButtonGroup o = create().build();
		JAXBAssert.assertEqualXmlMarshalUnmarshal(o, XML, RemotableRadioButtonGroup.class);
	}

    private RemotableRadioButtonGroup.Builder create() {
		RemotableRadioButtonGroup.Builder o = RemotableRadioButtonGroup.Builder.create(["foo":"bar"]);
        return o
	}
}
