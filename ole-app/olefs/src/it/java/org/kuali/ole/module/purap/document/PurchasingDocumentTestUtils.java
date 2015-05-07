/*
 * Copyright 2007 The Kuali Foundation
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
package org.kuali.ole.module.purap.document;

import java.util.List;

import org.kuali.ole.KFSTestCaseBase;
import org.kuali.ole.KualiTestBase;
import org.kuali.ole.OLETestCaseBase;
import org.kuali.ole.module.purap.businessobject.PurchasingItem;
import org.kuali.ole.module.purap.document.PurchasingDocument;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class PurchasingDocumentTestUtils extends KFSTestCaseBase {

    public static void testAddItem(PurchasingDocument document, List<PurchasingItem> items, int expectedItemTotal) throws Exception {
        assertTrue("no items found", items.size() > 0);

        assertEquals(0, document.getItems().size());

        for (PurchasingItem item : items) {
            document.addItem(item);
        }

        assertEquals("item count mismatch", expectedItemTotal, document.getItems().size());
    }

}
