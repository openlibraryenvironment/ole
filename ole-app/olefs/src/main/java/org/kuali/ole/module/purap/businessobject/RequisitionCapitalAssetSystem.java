/*
 * Copyright 2008 The Kuali Foundation
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


import org.kuali.ole.integration.purap.CapitalAssetLocation;
import org.kuali.ole.integration.purap.CapitalAssetSystem;
import org.kuali.ole.integration.purap.ItemCapitalAsset;

/**
 * @author Kuali Nervous System Team (kualidev@oncourse.iu.edu)
 */

public class RequisitionCapitalAssetSystem extends PurchasingCapitalAssetSystemBase {

    protected Integer purapDocumentIdentifier;

    /**
     * Default constructor.
     */
    public RequisitionCapitalAssetSystem() {
        super();
    }

    public RequisitionCapitalAssetSystem(CapitalAssetSystem originalSystem) {
        super();
        if (originalSystem != null) {
            this.setCapitalAssetSystemDescription(originalSystem.getCapitalAssetSystemDescription());
            this.setCapitalAssetNotReceivedCurrentFiscalYearIndicator(originalSystem.isCapitalAssetNotReceivedCurrentFiscalYearIndicator());
            this.setCapitalAssetTypeCode(originalSystem.getCapitalAssetTypeCode());
            this.setCapitalAssetManufacturerName(originalSystem.getCapitalAssetManufacturerName());
            this.setCapitalAssetModelDescription(originalSystem.getCapitalAssetModelDescription());
            this.setCapitalAssetNoteText(originalSystem.getCapitalAssetNoteText());
            populatePurchaseOrderItemCapitalAssets(originalSystem);
            populateCapitalAssetLocations(originalSystem);
            this.setCapitalAssetCountAssetNumber(originalSystem.getCapitalAssetCountAssetNumber());
        }
    }

    private void populatePurchaseOrderItemCapitalAssets(CapitalAssetSystem originalSystem) {
        for (ItemCapitalAsset reqAsset : originalSystem.getItemCapitalAssets()) {
            PurchaseOrderItemCapitalAsset poAsset = new PurchaseOrderItemCapitalAsset(reqAsset.getCapitalAssetNumber());
            this.getItemCapitalAssets().add(poAsset);
        }
    }

    private void populateCapitalAssetLocations(CapitalAssetSystem originalSystem) {
        for (CapitalAssetLocation reqLocation : originalSystem.getCapitalAssetLocations()) {
            PurchaseOrderCapitalAssetLocation poLocation = new PurchaseOrderCapitalAssetLocation();
            poLocation.setItemQuantity(reqLocation.getItemQuantity());
            poLocation.setCampusCode(reqLocation.getCampusCode());
            poLocation.setOffCampusIndicator(reqLocation.isOffCampusIndicator());
            poLocation.setBuildingCode(reqLocation.getBuildingCode());
            poLocation.setBuildingRoomNumber(reqLocation.getBuildingRoomNumber());
            poLocation.setCapitalAssetLine1Address(reqLocation.getCapitalAssetLine1Address());
            poLocation.setCapitalAssetCityName(reqLocation.getCapitalAssetCityName());
            poLocation.setCapitalAssetStateCode(reqLocation.getCapitalAssetStateCode());
            poLocation.setCapitalAssetPostalCode(reqLocation.getCapitalAssetPostalCode());
            poLocation.setCapitalAssetCountryCode(reqLocation.getCapitalAssetCountryCode());
            this.getCapitalAssetLocations().add(poLocation);
        }
    }

    @Override
    public Class getCapitalAssetLocationClass() {
        return RequisitionCapitalAssetLocation.class;
    }

    @Override
    public Class getItemCapitalAssetClass() {
        return RequisitionItemCapitalAsset.class;
    }

    public Integer getPurapDocumentIdentifier() {
        return purapDocumentIdentifier;
    }

    public void setPurapDocumentIdentifier(Integer purapDocumentIdentifier) {
        this.purapDocumentIdentifier = purapDocumentIdentifier;
    }

}
