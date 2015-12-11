/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.select.batch.service;

import org.kuali.ole.module.purap.businessobject.PurchaseOrderType;
import org.kuali.ole.module.purap.document.RequisitionDocument;
import org.kuali.ole.select.bo.OLEEResourceOrderRecord;
import org.kuali.ole.select.document.OleRequisitionDocument;

import java.util.List;

public interface RequisitionCreateDocumentService {

    /**
     * Creates requisition documents and routes from the records loaded into the transaction table.
     *
     * @return True if the routing was successful, false otherwise.
     */
    public String saveRequisitionDocuments(RequisitionDocument reqDocument);

    public OleRequisitionDocument updateParamaterValue(OleRequisitionDocument requisitionDocument,List<PurchaseOrderType> purchaseOrderTypeDocumentList,OLEEResourceOrderRecord oleEResourceOrderRecord);

}
