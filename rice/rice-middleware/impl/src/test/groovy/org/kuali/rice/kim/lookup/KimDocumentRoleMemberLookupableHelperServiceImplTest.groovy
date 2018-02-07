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
package org.kuali.rice.kim.lookup

import org.junit.Test
import static org.junit.Assert.*;

class KimDocumentRoleMemberLookupableHelperServiceImplTest {

    @Test
    public void testStripEnd() {
    	assertNull(KimDocumentRoleMemberLookupableHelperServiceImpl.stripEnd(null, null));
    	assertEquals("", KimDocumentRoleMemberLookupableHelperServiceImpl.stripEnd("", null));
    	assertEquals("", KimDocumentRoleMemberLookupableHelperServiceImpl.stripEnd("", ""));
    	assertEquals("b", KimDocumentRoleMemberLookupableHelperServiceImpl.stripEnd("b", ""));
    	assertEquals("", KimDocumentRoleMemberLookupableHelperServiceImpl.stripEnd("b", "b"));
    	assertEquals("b", KimDocumentRoleMemberLookupableHelperServiceImpl.stripEnd("b", "bb"));
    	assertEquals("wx", KimDocumentRoleMemberLookupableHelperServiceImpl.stripEnd("wxyz", "yz"));
    	assertEquals("wx", KimDocumentRoleMemberLookupableHelperServiceImpl.stripEnd("wxyz     ", "yz     "));
    	assertEquals("wxyz", KimDocumentRoleMemberLookupableHelperServiceImpl.stripEnd("wxyz", "abc"));
    }
}
