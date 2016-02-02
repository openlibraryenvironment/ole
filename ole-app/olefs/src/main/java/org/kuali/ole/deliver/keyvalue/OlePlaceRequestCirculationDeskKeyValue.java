package org.kuali.ole.deliver.keyvalue;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OleCirculationDeskDetail;
import org.kuali.ole.deliver.bo.OleCirculationDeskLocation;
import org.kuali.ole.deliver.form.OLEPlaceRequestForm;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.describe.form.BoundwithForm;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;
import org.kuali.rice.krad.uif.view.ViewModel;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maheswarang on 2/6/15.
 */
public class OlePlaceRequestCirculationDeskKeyValue extends UifKeyValuesFinderBase {

    private BusinessObjectService businessObjectService;

    @Override
    public boolean isAddBlankOption() {
        return false;
    }

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
        OleCirculationDesk defaultPickUpLocation = getDefaultPickUpLocation();
         if(defaultPickUpLocation == null){
             defaultPickUpLocation = getOperatorDefaultPickupLocation();
        }
        List<KeyValue> options = new ArrayList<KeyValue>();
        //TODO: Get drop-down values dynamically by parsing DocumentConfig.xml file
        if(defaultPickUpLocation!=null){
            options.add(new ConcreteKeyValue(defaultPickUpLocation.getCirculationDeskId(),defaultPickUpLocation.getCirculationDeskCode()));
        }else{
            options.add(new ConcreteKeyValue("",""));
        }
          if(oleCirculationDeskLocations!=null && oleCirculationDeskLocations.size()>0){
          for(OleCirculationDeskLocation oleCirculationDeskLocation : oleCirculationDeskLocations){
              if(defaultPickUpLocation!=null){
              if(!oleCirculationDeskLocation.getCirculationDeskId().equals(defaultPickUpLocation.getCirculationDeskId())){
               options.add(new ConcreteKeyValue(oleCirculationDeskLocation.getCirculationDeskId(),oleCirculationDeskLocation.getOleCirculationDesk().getCirculationDeskCode()));
              }
              }else{
                options.add(new ConcreteKeyValue(oleCirculationDeskLocation.getCirculationDeskId(),oleCirculationDeskLocation.getOleCirculationDesk().getCirculationDeskCode()));
              }
              }
          }
        return options;
    }

    private OleCirculationDesk getDefaultPickUpLocation() {
        String parameterValue = ParameterValueResolver.getInstance().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.DEFAULT_PICK_UP_LOCATION);
        if (StringUtils.isNotBlank(parameterValue)) {
            Map<String, String> pickUpLocMap = new HashMap<>();
            pickUpLocMap.put(OLEConstants.PICKUP_LOCATION, "true");
            pickUpLocMap.put(OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_CD, parameterValue);
            List<OleCirculationDesk> oleCirculationDesks = (List<OleCirculationDesk>) KRADServiceLocator.getBusinessObjectService().findMatching(OleCirculationDesk.class, pickUpLocMap);
            if (CollectionUtils.isNotEmpty(oleCirculationDesks)) {
                return oleCirculationDesks.get(0);
            }
        }
        return null;
    }


    private OleCirculationDesk getOperatorDefaultPickupLocation(){
        OleCirculationDesk oleCirculationDesk = null;
        Map<String,String> criteriaMap = new HashMap<String,String>();
        criteriaMap.put("operatorId", GlobalVariables.getUserSession().getPrincipalId());
        criteriaMap.put("defaultLocation","Y");
        List<OleCirculationDeskDetail> oleCirculationDeskDetailList = (List<OleCirculationDeskDetail>)getBusinessObjectService().findMatching(OleCirculationDeskDetail.class,criteriaMap);
        if(oleCirculationDeskDetailList.size()>0){
          OleCirculationDeskDetail  oleCirculationDeskDetail = oleCirculationDeskDetailList.get(0);
          if(oleCirculationDeskDetail.getOleCirculationDesk().getDefaultPickupLocation().isPickUpLocation()){
            oleCirculationDesk = oleCirculationDeskDetail.getOleCirculationDesk().getDefaultPickupLocation();
          }

        }
        return oleCirculationDesk;
    }
}
