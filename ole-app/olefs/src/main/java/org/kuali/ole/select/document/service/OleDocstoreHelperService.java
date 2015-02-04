/*
 * Copyright 2012 The Kuali Foundation.
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.select.document.service;

import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.module.purap.businessobject.PurApItem;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.select.businessobject.OleCopy;

public interface OleDocstoreHelperService {

    public String rollbackData(String bibiUUID);

    public void createOrUpdateDocStoreBasedOnLocation(PurchaseOrderDocument document,PurApItem item, String currentDocumentTypeName, String note);

    //public void updateItemNote(PurApItem item, String note);

    public void updateItemLocation(PurchaseOrderDocument document, PurApItem item);

    public OleHoldings setHoldingDetails(OleCopy copy) throws Exception;

    public boolean isValidLocation(String location);

}
