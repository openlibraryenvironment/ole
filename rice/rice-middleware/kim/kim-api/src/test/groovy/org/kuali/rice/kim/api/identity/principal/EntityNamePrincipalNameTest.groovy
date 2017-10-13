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
package org.kuali.rice.kim.api.identity.principal

import org.junit.Test
import org.kuali.rice.kim.api.test.JAXBAssert
import org.junit.Assert
import org.kuali.rice.kim.api.identity.name.EntityName
import org.kuali.rice.kim.api.identity.name.EntityNameTest

class EntityNamePrincipalNameTest {
    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_code_whitespace() {
        EntityNamePrincipalName entityNamePrincipalName = null;
        EntityNamePrincipalName.Builder builder = EntityNamePrincipalName.Builder.create(entityNamePrincipalName);
    }

    @Test
    void happy_path() {
        EntityName.Builder builder = new EntityName.Builder();
        EntityNamePrincipalName.Builder.create("ABC", builder);
    }
}
