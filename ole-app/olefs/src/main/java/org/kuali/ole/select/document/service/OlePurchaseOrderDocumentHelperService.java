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
import org.kuali.ole.module.purap.document.RequisitionDocument;
import org.kuali.ole.select.businessobject.BibInfoBean;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.businessobject.OleRequisitionItem;
import org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent;

import java.util.List;

public interface OlePurchaseOrderDocumentHelperService {

    public void populatePurchaseOrderFromRequisition(PurchaseOrderDocument purchaseOrderDocument, RequisitionDocument requisitionDocument);

    public void prepareForSave(PurchaseOrderDocument purchaseOrderDocument, KualiDocumentEvent event);

    public void processAfterRetrieve(PurchaseOrderDocument purchaseOrderDocument);

    public void setBibInfoBean(BibInfoBean bibInfoBean, OlePurchaseOrderItem singleItem);

    public void setBibInfoBean(BibInfoBean bibInfoBean, OleRequisitionItem singleItem);

    public boolean getIsFinalReqs(PurchaseOrderDocument purchaseOrderDocument);

    public boolean getIsSplitPO(PurchaseOrderDocument purchaseOrderDocument);

    public boolean getIsReOpenPO(PurchaseOrderDocument purchaseOrderDocument);

    public String getBibeditorCreateURL();

    public String getBibeditorEditURL();

    public String getDublinEditorEditURL();

    public String getBibSearchURL();

    public String getBibeditorViewURL();

    public String getDublinEditorViewURL();

    public String getInstanceEditorURL();

    public String getMarcXMLFileDirLocation() throws Exception;

    public List getItemsActiveOnly(PurchaseOrderDocument purchaseOrderDocument);

    public List getItemsActiveOnlySetupAlternateAmount(PurchaseOrderDocument purchaseOrderDocument);

    public boolean getAdditionalChargesExist(PurchaseOrderDocument purchaseOrderDocument);
}
