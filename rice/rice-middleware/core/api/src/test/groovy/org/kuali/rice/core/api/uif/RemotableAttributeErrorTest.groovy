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
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertEquals
import org.kuali.rice.core.test.JAXBAssert

class RemotableAttributeErrorTest {
    private static final String XML =
    """<attributeError xmlns="http://rice.kuali.org/core/v2_0">
         <attributeName>a_name</attributeName>
         <errors>
           <error>error0</error>
           <error>error1</error>
         </errors>
       </attributeError>""";

    @Test
    void testHappyPath() {
        RemotableAttributeError o = create().build();
        assertNotNull(o);
    }

    @Test
    void testMessage() {
        RemotableAttributeError.Builder o = create();
        assertEquals("error0, error1", o.build().getMessage())
    }

    @Test(expected=IllegalArgumentException.class)
    void testEmptyName() {
        RemotableAttributeError.Builder o = RemotableAttributeError.Builder.create((String) null, "error0");
        assertNotNull(o.build().attributeName);
    }

    @Test(expected=IllegalStateException.class)
    void testNoErrors() {
        RemotableAttributeError.Builder o = RemotableAttributeError.Builder.create("a_name");
        assertNotNull(o.build());
    }

    @Test(expected=IllegalStateException.class)
    void testBlankError() {
        RemotableAttributeError.Builder o = RemotableAttributeError.Builder.create("a_name", " ");
        assertNotNull(o.build());
    }

    @Test
    void testJAXB() {
        RemotableAttributeError o = create().build();
        JAXBAssert.assertEqualXmlMarshalUnmarshal(o, XML, RemotableAttributeError.class);
    }

    private RemotableAttributeError.Builder create() {
        return RemotableAttributeError.Builder.create("a_name", "error0", "error1");
    }
}
