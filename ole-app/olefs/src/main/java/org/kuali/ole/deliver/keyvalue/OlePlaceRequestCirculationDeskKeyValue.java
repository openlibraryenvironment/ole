package org.kuali.ole.deliver.keyvalue;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OleCirculationDeskLocation;
import org.kuali.ole.deliver.form.OLEPlaceRequestForm;
import org.kuali.ole.describe.form.BoundwithForm;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;
import org.kuali.rice.krad.uif.view.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by maheswarang on 2/6/15.
 */
public class OlePlaceRequestCirculationDeskKeyValue extends UifKeyValuesFinderBase {

    private BusinessObjectService businessObjectService;

    public BusinessObjectService getBusinessObjectService() {
        if(businessObjectService == null){
            this.businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    @Override
    public List<KeyValue> getKeyValues(ViewModel viewModel) {
        OLEPlaceRequestForm olePlaceRequestForm = (OLEPlaceRequestForm) viewModel;
        HashMap<String,String> circulationDeskMap = new HashMap<String,String>();
        circulationDeskMap.put("circulationPickUpDeskLocation",olePlaceRequestForm.getItemLocation());
        List<OleCirculationDeskLocation> oleCirculationDeskLocations = (List<OleCirculationDeskLocation>)getBusinessObjectService().findMatching(OleCirculationDeskLocation.class,circulationDeskMap);
        List<KeyValue> options = new ArrayList<KeyValue>();
        //TODO: Get drop-down values dynamically by parsing DocumentConfig.xml file
          if(oleCirculationDeskLocations!=null && oleCirculationDeskLocations.size()>0){

          for(OleCirculationDeskLocation oleCirculationDeskLocation : oleCirculationDeskLocations){
            options.add(new ConcreteKeyValue(oleCirculationDeskLocation.getCirculationDeskId(),oleCirculationDeskLocation.getOleCirculationDesk().getCirculationDeskCode()));
          }
          }
        return options;
    }
}
