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
package org.kuali.ole.select.document.web.struts;

import org.kuali.ole.module.purap.businessobject.CorrectionReceivingItem;
import org.kuali.ole.module.purap.document.web.struts.CorrectionReceivingForm;
import org.kuali.ole.select.businessobject.OleCorrectionReceivingItem;


public class OleCorrectionReceivingForm extends CorrectionReceivingForm {

    protected OleCorrectionReceivingItem newOleCorrectionReceivingItemLine;

    public OleCorrectionReceivingForm() {
        super();
        this.setNewCorrectionReceivingItemLine(setupNewCorrectionReceivingItemLine());
        newCorrectionReceivingItemLine.setItemTypeCode("ITEM");
    }

    public OleCorrectionReceivingItem setupNewOleCorrectionReceivingItemLine() {
        return new OleCorrectionReceivingItem();
    }

    public void setNewOleCorrectionReceivingItemLine(OleCorrectionReceivingItem newOleCorrectionReceivingItemLine) {
        this.newOleCorrectionReceivingItemLine = newOleCorrectionReceivingItemLine;
    }
    
/*    private Integer itemFormat;
    private KualiDecimal itemCorrectedPartsReceived;
    private KualiDecimal itemCorrectedPartsReturned;
    private KualiDecimal itemCorrectedPartsDamaged;
    
    public Integer getItemFormat() {
        return itemFormat;
    }
    public void setItemFormat(Integer itemFormat) {
        this.itemFormat = itemFormat;
    }
    public KualiDecimal getItemCorrectedPartsReceived() {
        return itemCorrectedPartsReceived;
    }
    public void setItemCorrectedPartsReceived(KualiDecimal itemCorrectedPartsReceived) {
        this.itemCorrectedPartsReceived = itemCorrectedPartsReceived;
    }
    public KualiDecimal getItemCorrectedPartsReturned() {
        return itemCorrectedPartsReturned;
    }
    public void setItemCorrectedPartsReturned(KualiDecimal itemCorrectedPartsReturned) {
        this.itemCorrectedPartsReturned = itemCorrectedPartsReturned;
    }
    public KualiDecimal getItemCorrectedPartsDamaged() {
        return itemCorrectedPartsDamaged;
    }
    public void setItemCorrectedPartsDamaged(KualiDecimal itemCorrectedPartsDamaged) {
        this.itemCorrectedPartsDamaged = itemCorrectedPartsDamaged;
    }*/

    /**
     * This method is overriden to return new Ole Correction Receiving Item Line.
     *
     * @return CorrectionReceivingItem
     */
    @Override
    public CorrectionReceivingItem setupNewCorrectionReceivingItemLine() {
        return new OleCorrectionReceivingItem();
    }
}
