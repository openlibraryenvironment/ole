/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.kim.test

import org.junit.Before
import org.kuali.rice.kim.bo.ui.KimDocumentBoActivatableEditableBase
import org.kuali.rice.test.persistence.PersistenceTestHelper

/**
 * Tests persisting Entity objects in order to verify ORM mappings
 */
abstract class BoPersistenceTest extends KIMTestCase {

    @Delegate PersistenceTestHelper helper
    protected factory = new EntityFactory()

    @Before
    void init() {
        helper = new PersistenceTestHelper("kimDataSource")
    }

    protected def docno_field(KimDocumentBoActivatableEditableBase bo) {
        [ FDOC_NBR: bo.documentNumber ]
    }
    protected def kimdoc_fields(KimDocumentBoActivatableEditableBase bo) {
        basic_fields(bo) + active_field(bo) + default_field(bo) + edit_field(bo) + docno_field(bo)
    }
}