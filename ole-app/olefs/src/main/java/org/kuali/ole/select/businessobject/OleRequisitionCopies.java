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

import org.kuali.ole.module.purap.businessobject.PurApItem;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

/**
 * OLE RequisitionNotes Base Business Object.
 */
public class OleRequisitionCopies extends PersistableBusinessObjectBase implements OleCopies {

    private Integer itemCopiesId;
    private Integer itemIdentifier;
    private KualiInteger parts;
    private KualiDecimal itemCopies;
    private String partEnumeration;
    private String locationCopies;
    private KualiInteger startingCopyNumber;
    //private String       instanceId;
    private String caption;
    private String volumeNumber;

    private PurApItem purapItem;
    private KualiInteger singleCopyNumber;

    /**
     * Constructs a OleRequisitionNotesBase.java.
     */
    public OleRequisitionCopies() {

    }

    public OleRequisitionCopies(KualiDecimal itemCopies, KualiInteger parts, String locationCopies,
                                KualiInteger startingCopyNumber) {
        this.itemCopies = itemCopies;
        this.parts = parts;
        this.locationCopies = locationCopies;
        this.startingCopyNumber = startingCopyNumber;
    }

    /**
     * get the RequisitionItem Copies Id
     *
     * @return itemCopiesId
     * @see org.kuali.ole.select.businessobject.OleCopies#getItemCopiesId()
     */
    @Override
    public Integer getItemCopiesId() {
        return itemCopiesId;
    }


    @Override
    public void setItemCopiesId(Integer itemCopiesId) {
        this.itemCopiesId = itemCopiesId;
    }


    @Override
    public Integer getItemIdentifier() {
        return itemIdentifier;
    }


    @Override
    public void setItemIdentifier(Integer itemIdentifier) {
        this.itemIdentifier = itemIdentifier;
    }


    @Override
    public String getPartEnumeration() {
        return partEnumeration;
    }


    @Override
    public void setPartEnumeration(String partEnumeration) {
        this.partEnumeration = partEnumeration;
    }

    @Override
    public String getLocationCopies() {
        return locationCopies;
    }

    @Override
    public void setLocationCopies(String locationCopies) {
        this.locationCopies = locationCopies;
    }


    /**
     * get the OleRequistionItem
     *
     * @return purapItem
     * @see org.kuali.ole.select.businessobject.OleCopies#getPurapItem()
     */

    public PurApItem getPurapItem() {
        return purapItem;
    }

    /**
     * set the OleRequistionItem
     *
     * @param purapItem
     * @see org.kuali.ole.select.businessobject.OleCopies#setReqItem(org.kuali.ole.module.purap.businessobject.PurApItem)
     */

    public void setPurapItem(PurApItem purapItem) {
        this.purapItem = purapItem;
    }

    @Override
    public KualiInteger getParts() {
        return parts;
    }

    @Override
    public void setParts(KualiInteger parts) {
        this.parts = parts;
    }

    @Override
    public KualiDecimal getItemCopies() {
        return itemCopies;
    }

    @Override
    public void setItemCopies(KualiDecimal itemCopies) {
        this.itemCopies = itemCopies;
    }

    @Override
    public KualiInteger getStartingCopyNumber() {
        return startingCopyNumber;
    }

    @Override
    public void setStartingCopyNumber(KualiInteger startingCopyNumber) {
        this.startingCopyNumber = startingCopyNumber;
    }

    @Override
    public void setSingleCopyNumber(KualiInteger singleCopyNumber){
        this.singleCopyNumber=singleCopyNumber;
    }

    public KualiInteger getSingleCopyNumber(){
        return singleCopyNumber;
    }
    /*@Override
    public String getInstanceId() {
        return instanceId;
    }

    @Override
    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }*/

    /**
     * @see org.kuali.rice.kns.bo.BusinessObjectBase#toStringMapper()
     */
    @SuppressWarnings("rawtypes")
    protected LinkedHashMap toStringMapper_RICE20_REFACTORME() {
        LinkedHashMap map = new LinkedHashMap();
        map.put("itemIdentifier", itemIdentifier);
        map.put("itemCopiesId", itemCopiesId);
        map.put("parts", parts);
        map.put("itemCopies", itemCopies);
        map.put("partEnumeration", partEnumeration);
        map.put("locationCopies", locationCopies);
        map.put("startingCopyNumber", startingCopyNumber);
        map.put("caption", caption);
        map.put("volumeNumber", volumeNumber);
        // map.put("instanceId", instanceId);
        return map;
    }


    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getVolumeNumber() {
        return volumeNumber;
    }

    public void setVolumeNumber(String volumeNumber) {
        this.volumeNumber = volumeNumber;
    }
}