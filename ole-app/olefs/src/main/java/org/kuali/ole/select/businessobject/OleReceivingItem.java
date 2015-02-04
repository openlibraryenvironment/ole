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
package org.kuali.ole.select.businessobject;

import org.kuali.ole.module.purap.businessobject.PurapEnterableItem;
import org.kuali.ole.module.purap.businessobject.ReceivingItem;
import org.kuali.rice.core.api.util.type.KualiDecimal;

/**
 * Interface for Ole Receiving Items
 */

public interface OleReceivingItem extends ReceivingItem, PurapEnterableItem {

    public KualiDecimal getItemReceivedTotalParts();

    public void setItemReceivedTotalParts(KualiDecimal itemReceivedTotalParts);

    public KualiDecimal getItemReturnedTotalParts();

    public void setItemReturnedTotalParts(KualiDecimal itemReturnedTotalParts);

    public KualiDecimal getItemDamagedTotalParts();

    public void setItemDamagedTotalParts(KualiDecimal itemDamagedTotalParts);

    public KualiDecimal getItemOriginalReceivedTotalParts();

    public void setItemOriginalReceivedTotalParts(KualiDecimal itemOriginalReceivedTotalParts);

    public KualiDecimal getItemOriginalReturnedTotalParts();

    public void setItemOriginalReturnedTotalParts(KualiDecimal itemOriginalReturnedTotalParts);

    public KualiDecimal getItemOriginalDamagedTotalParts();

    public void setItemOriginalDamagedTotalParts(KualiDecimal itemOriginalDamagedTotalParts);
}
