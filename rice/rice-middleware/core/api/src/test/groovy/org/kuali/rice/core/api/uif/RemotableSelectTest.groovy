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
import org.junit.Ignore;

class RemotableSelectTest {
        private static final String XML1 =
        """<select xmlns="http://rice.kuali.org/core/v2_0">
            <keyLabels>
		        <entry key="foo">bar</entry>
	        </keyLabels>
            <size>2</size>
            <multiple>false</multiple>
          </select>""";

        private static final String XML2 =
        """<select xmlns="http://rice.kuali.org/core/v2_0">
            <groups>
                <group>
                    <keyLabels>
                        <entry key="foo">bar</entry>
                    </keyLabels>
                    <label>first_label</label>
                </group>
                <group>
                    <keyLabels>
                        <entry key="baz">bin</entry>
                    </keyLabels>
                    <label>second_label</label>
                </group>
            </groups>
            <size>2</size>
            <multiple>false</multiple>
          </select>""";

    @Test
    void testHappyPath() {
        RemotableSelect o = RemotableSelect.Builder.create(["foo":"bar"]).build();
        assertNotNull(o);
    }

    @Test
    void testHappyPath2() {
        RemotableSelect.Builder o = create();
        o.size = 2

        assertNotNull(o.build());
    }

    @Test
    void testOptional() {
        RemotableSelect.Builder o = create();
        o.size = null

        assertNotNull(o.build());
    }

    @Test(expected=IllegalArgumentException.class)
    void testbadSize() {
        RemotableSelect.Builder o = create();
        o.size = 0

        assertNotNull(o.build());
    }

    @Test(expected=IllegalStateException.class)
    void testEmptyKeyLabelsAndGroups1() {
        RemotableSelect o = RemotableSelect.Builder.create([:]).build()
    }

    @Test(expected=IllegalStateException.class)
    void testEmptyKeyLabelsAndGroups2() {
        RemotableSelect o = RemotableSelect.Builder.create([]).build()
    }

    @Test(expected=IllegalArgumentException.class)
    void testNullKeyLabels() {
        RemotableSelect.Builder o = RemotableSelect.Builder.create(null)
    }

    @Test
	void testJAXB1() {
		RemotableSelect o = create().build();
		JAXBAssert.assertEqualXmlMarshalUnmarshal(o, XML1, RemotableSelect.class);
	}

    @Test
	void testJAXB2() {
		RemotableSelect o = create2().build();
		JAXBAssert.assertEqualXmlMarshalUnmarshal(o, XML2, RemotableSelect.class);
	}

    private RemotableSelect.Builder create() {
		RemotableSelect.Builder o = RemotableSelect.Builder.create(["foo":"bar"]);
        o.size = 2
        return o
	}

    private RemotableSelect.Builder create2() {
		RemotableSelect.Builder o = RemotableSelect.Builder.create([
                RemotableSelectGroup.Builder.create(["foo":"bar"], "first_label"), RemotableSelectGroup.Builder.create(["baz":"bin"], "second_label")]);
        o.size = 2
        return o
	}
}
