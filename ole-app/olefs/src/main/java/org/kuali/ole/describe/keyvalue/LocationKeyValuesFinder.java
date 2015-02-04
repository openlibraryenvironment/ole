package org.kuali.ole.describe.keyvalue;

import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;
import org.kuali.rice.krad.uif.view.ViewModel;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;

import java.util.*;

/**
 * The LocationKeyValuesFinder class contains a method that returns the location name as key and location code as value
 * wrapped as a KeyValue object in a list.
 */
public class LocationKeyValuesFinder extends UifKeyValuesFinderBase {

    /**
     * This method returns the key-values in a List based on the model parameter that is passed.
     *
     * @param model
     * @return options
     */
    @Override
    public List<KeyValue> getKeyValues(ViewModel model) {
        MaintenanceDocumentForm testForm = (MaintenanceDocumentForm) model;
        List<KeyValue> options = new ArrayList<KeyValue>();
        if (testForm.getDocument().getNewMaintainableObject().getDataObject() instanceof OleDeliverRequestBo) {
            List<OleLocation> oleLocations = (List<OleLocation>) KRADServiceLocator.getBusinessObjectService().findAll(OleLocation.class);
            for (int i = 0; i < oleLocations.size(); i++) {
                options.add(new ConcreteKeyValue(oleLocations.get(i).getLevelId(), oleLocations.get(i).getLocationCode()));
            }
        } else if (testForm.getDocument().getNewMaintainableObject().getDataObject() instanceof OleCirculationDesk) {
            Map<String, String> locationMap = new HashMap<String, String>();
            locationMap.put("levelId", "5");
            List<OleLocation> shelvingLocations = (List<OleLocation>) KRADServiceLocator.getBusinessObjectService().findMatching(OleLocation.class, locationMap);
            for (int i = 0; i < shelvingLocations.size(); i++) {
                options.add(new ConcreteKeyValue(shelvingLocations.get(i).getLocationId(), shelvingLocations.get(i).getLocationCode()));
            }
        } else {
            String levelId = ((OleLocation) testForm.getDocument().getNewMaintainableObject().getDataObject()).getLevelId();
            Collection<OleLocation> oleLocations = KRADServiceLocator.getBusinessObjectService().findAll(OleLocation.class);
            for (OleLocation location : oleLocations) {
                if (location.getLevelId() != null && !location.getLevelId().equalsIgnoreCase("") && levelId != null && !levelId.equalsIgnoreCase("")) {
                    if (Integer.parseInt(location.getLevelId()) < Integer.parseInt(levelId)) {
                        options.add(new ConcreteKeyValue(location.getLocationId(), location.getLocationName() + " [" + location.getLocationCode() + "]"));
                    }
                }
            }
            return options;
        }
        return options;
    }

}
