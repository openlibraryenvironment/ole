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
package org.kuali.ole.select.document.service;

import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.document.service.PurchaseOrderService;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;

import java.io.ByteArrayOutputStream;

/**
 * Defines methods that must be implemented by classes providing a PurchaseOrderService.
 */
public interface OlePurchaseOrderService extends PurchaseOrderService {

    public void setStatusCompletePurchaseOrderAmendment(PurchaseOrderDocument poa);

    public void purchaseOrderFirstTransmitViaPrinting(String documentNumber, ByteArrayOutputStream baosPDF);

    public String createPurchaseOrderAmendmentDocument(OlePurchaseOrderDocument olePurchaseOrderDocument,String docNumber);

}
