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
import org.kuali.rice.core.test.JAXBAssert
import static org.junit.Assert.assertNotNull

class RemotableQuickFinderTest {
    
            private static final String XML1 =
        """<quickFinder xmlns="http://rice.kuali.org/core/v2_0">
            <baseLookupUrl>http://lookup.url/</baseLookupUrl>
            <dataObjectClass>FooBo</dataObjectClass>
          </quickFinder>""";

        private static final String XML2 =
        """<quickFinder xmlns="http://rice.kuali.org/core/v2_0">
            <baseLookupUrl>http://lookup.url/</baseLookupUrl>
            <dataObjectClass>FooBo</dataObjectClass>
            <lookupParameters>
                <entry key="foo">bar</entry>
            </lookupParameters>
            <fieldConversions>
                <entry key="baz">bing</entry>
            </fieldConversions>
          </quickFinder>""";

    @Test
    void testHappyPath() {
        RemotableQuickFinder o = RemotableQuickFinder.Builder.create("http://lookup.url/", "FooBo").build();
        assertNotNull(o);
    }

    @Test
    void testHappyPath2() {
        RemotableQuickFinder.Builder o = create2();
        assertNotNull(o.build());
    }

    @Test
    void testOptional() {
        RemotableQuickFinder.Builder o = create();
        o.fieldConversions = null
        o.lookupParameters = null
        assertNotNull(o.build());
    }

    @Test
    void testOptional2() {
        RemotableQuickFinder.Builder o = create();
        o.fieldConversions = [:]
        o.lookupParameters = [:]
        assertNotNull(o.build());
    }

    @Test(expected=IllegalArgumentException.class)
    void testNullDO() {
        RemotableQuickFinder o = RemotableQuickFinder.Builder.create("http://lookup.url/", null).build()
    }

    @Test(expected=IllegalArgumentException.class)
    void testBlankDO() {
        RemotableQuickFinder o = RemotableQuickFinder.Builder.create("http://lookup.url/", " ").build()
    }

    @Test
	void testJAXB1() {
		RemotableQuickFinder o = create().build();
		JAXBAssert.assertEqualXmlMarshalUnmarshal(o, XML1, RemotableQuickFinder.class);
	}

    @Test
	void testJAXB2() {
		RemotableQuickFinder o = create2().build();
		JAXBAssert.assertEqualXmlMarshalUnmarshal(o, XML2, RemotableQuickFinder.class);
	}

    private RemotableQuickFinder.Builder create() {
		RemotableQuickFinder.Builder o = RemotableQuickFinder.Builder.create("http://lookup.url/", "FooBo");
        return o
	}

    private RemotableQuickFinder.Builder create2() {
		RemotableQuickFinder.Builder o = RemotableQuickFinder.Builder.create("http://lookup.url/", "FooBo");
        o.fieldConversions = ["baz" : "bing"]
        o.lookupParameters = ["foo" : "bar"]
        return o
	}
}
