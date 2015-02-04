/*
 * Copyright 2006-2008 The Kuali Foundation
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
package org.kuali.ole.module.purap.businessobject;

import org.kuali.ole.integration.purap.ItemCapitalAsset;
import org.kuali.ole.module.purap.document.PurchasingDocument;
import org.kuali.ole.module.purap.document.RequisitionDocument;
import org.kuali.rice.krad.util.ObjectUtils;

public class RequisitionCapitalAssetItem extends PurchasingCapitalAssetItemBase {

    private Integer purapDocumentIdentifier;

    public RequisitionCapitalAssetItem() {
        super();
        this.setPurchasingCapitalAssetSystem(new RequisitionCapitalAssetSystem());
    }

    public RequisitionCapitalAssetItem(PurchasingDocument pd) {
        super(pd);
        setPurapDocumentIdentifier(pd.getPurapDocumentIdentifier());
        this.setPurchasingCapitalAssetSystem(new RequisitionCapitalAssetSystem());
    }

    public RequisitionCapitalAssetItem(PurchasingCapitalAssetItem reqAssetItem, Integer itemIdentifier) {
        this.setItemIdentifier(itemIdentifier);
        this.setCapitalAssetTransactionTypeCode(reqAssetItem.getCapitalAssetTransactionTypeCode());
        if (ObjectUtils.isNotNull(reqAssetItem.getPurchasingCapitalAssetSystem())) {
            this.setPurchasingCapitalAssetSystem(new PurchaseOrderCapitalAssetSystem(reqAssetItem.getPurchasingCapitalAssetSystem()));
        }
    }

    public Integer getPurapDocumentIdentifier() {
        return purapDocumentIdentifier;
    }

    public void setPurapDocumentIdentifier(Integer purapDocumentIdentifier) {
        this.purapDocumentIdentifier = purapDocumentIdentifier;
    }

    @Override
    public void setPurchasingDocument(PurchasingDocument pd) {
        super.setPurchasingDocument(pd);

        RequisitionDocument req = (RequisitionDocument) pd;
        if (req != null) {
            setPurapDocumentIdentifier(req.getPurapDocumentIdentifier());
        }
    }

    @Override
    public ItemCapitalAsset setupNewPurchasingItemCapitalAssetLine() {
        ItemCapitalAsset asset = new RequisitionItemCapitalAsset();
        return asset;
    }

}
