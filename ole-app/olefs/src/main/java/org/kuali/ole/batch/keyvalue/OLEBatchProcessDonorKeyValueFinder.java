package org.kuali.ole.batch.keyvalue;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileConstantsBo;
import org.kuali.ole.select.bo.OLEDonor;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;
import org.kuali.rice.krad.uif.view.ViewModel;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by rajeshbabuk on 21/7/14.
 */
public class OLEBatchProcessDonorKeyValueFinder extends UifKeyValuesFinderBase {
    @Override
    public List<KeyValue> getKeyValues(ViewModel model) {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) model;
        OLEBatchProcessProfileConstantsBo oleBatchProcessProfileConstantsBo = null;
        if (!CollectionUtils.isEmpty(maintenanceForm.getNewCollectionLines())) {
            oleBatchProcessProfileConstantsBo = (OLEBatchProcessProfileConstantsBo) maintenanceForm.getNewCollectionLines().get("document.newMaintainableObject.dataObject.oleBatchProcessProfileConstantsList");
        }
        if (oleBatchProcessProfileConstantsBo != null) {
            String dataType = oleBatchProcessProfileConstantsBo.getDataType();
            if (StringUtils.isNotBlank(dataType)) {
                String attributeName = oleBatchProcessProfileConstantsBo.getAttributeName();
                String oldAttributeName = oleBatchProcessProfileConstantsBo.getOldAttributeName();
                if (!StringUtils.isBlank(attributeName)) {
                    if (oldAttributeName == null || !attributeName.equalsIgnoreCase(oldAttributeName)) {
                        oleBatchProcessProfileConstantsBo.setOldAttributeName(attributeName);
                        if (dataType.equalsIgnoreCase(OLEConstants.OLEBatchProcess.BATCH_PROCESS_PROFILE_DATATYPE_ITEM) || dataType.equalsIgnoreCase(OLEConstants.OLEBatchProcess.BATCH_PROCESS_PROFILE_DATATYPE_EHOLDINGS)) {
                            List<OLEDonor> donorInfoList = (List<OLEDonor>) KRADServiceLocator.getBusinessObjectService().findAll(OLEDonor.class);
                            if (attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_DONOR_CODE)) {
                                if (!CollectionUtils.isEmpty(donorInfoList)) {
                                    Set<KeyValue> donorCodes = new HashSet<>();
                                    for (OLEDonor oleDonor : donorInfoList) {
                                        donorCodes.add(new ConcreteKeyValue(oleDonor.getDonorCode(), oleDonor.getDonorCode()));
                                    }
                                    keyValues.addAll(donorCodes);
                                }
                            } else if (attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_DONOR_PUBLIC_DISPLAY)) {
                                if (!CollectionUtils.isEmpty(donorInfoList)) {
                                    Set<KeyValue> donorPublicDisplays = new HashSet<>();
                                    for (OLEDonor oleDonor : donorInfoList) {
                                        donorPublicDisplays.add(new ConcreteKeyValue(oleDonor.getDonorPublicDisplay(), oleDonor.getDonorPublicDisplay()));
                                    }
                                    keyValues.addAll(donorPublicDisplays);
                                }
                            } else if (attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_DONOR_NOTE)) {
                                if (!CollectionUtils.isEmpty(donorInfoList)) {
                                    Set<KeyValue> donorNotes = new HashSet<>();
                                    for (OLEDonor oleDonor : donorInfoList) {
                                        donorNotes.add(new ConcreteKeyValue(oleDonor.getDonorNote(), oleDonor.getDonorNote()));
                                    }
                                    keyValues.addAll(donorNotes);
                                }
                            }
                        }
                    }
                }
            }
        }
        return keyValues;
    }
}
