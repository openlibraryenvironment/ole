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

import org.kuali.rice.core.test.JAXBAssert
import org.junit.Test
import org.junit.Assert

class RemotableTextExpandTest {

    	private static final String XML =
        """<textExpand xmlns="http://rice.kuali.org/core/v2_0">
          </textExpand>""";

    @Test
    void testHappyPath() {
        RemotableTextExpand o = create();
        Assert.assertNotNull(o);
    }

    @Test
	void testJAXB() {
		RemotableTextExpand o = create();
		JAXBAssert.assertEqualXmlMarshalUnmarshal(o, XML, RemotableTextExpand.class);
	}

    private RemotableTextExpand create() {
		return RemotableTextExpand.Builder.create().build();
	}
}
