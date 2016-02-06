package org.kuali.ole.deliver.keyvalue;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OleCirculationDeskDetail;
import org.kuali.ole.deliver.bo.OleCirculationDeskLocation;
import org.kuali.ole.deliver.controller.drools.RuleExecutor;
import org.kuali.ole.deliver.form.OLEPlaceRequestForm;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.deliver.util.DroolsResponse;
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
    private RuleExecutor ruleExecutor;

    public boolean isAddBlankOption() {
        return false;
    }

    public BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            this.businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public RuleExecutor getRuleExecutor() {
        if (ruleExecutor == null) {
            this.ruleExecutor = new RuleExecutor();
        }
        return ruleExecutor;
    }

    public void setRuleExecutor(RuleExecutor ruleExecutor) {
        this.ruleExecutor = ruleExecutor;
    }

    @Override
    public List<KeyValue> getKeyValues(ViewModel viewModel) {
        List<Object> facts = new ArrayList<>();
        List<KeyValue> options = new ArrayList<KeyValue>();
        OLEPlaceRequestForm olePlaceRequestForm = (OLEPlaceRequestForm) viewModel;
        OleCirculationDesk defaultPickupLocation = null;
        defaultPickupLocation = getDefaultPickUpLocation();
        if(defaultPickupLocation == null){
            defaultPickupLocation = getOperatorDefaultPickupLocation();
        }
        DroolsResponse droolsResponse = new DroolsResponse();
        facts.add(olePlaceRequestForm);
        facts.add(olePlaceRequestForm.getOlePatronDocument());
        facts.add(droolsResponse);

        getRuleExecutor().fireRules(facts, null, "pickup-location");
        if(droolsResponse.isRuleMatched()){
            List<String> pickUpLocationList = new ArrayList<>();
            if (StringUtils.isNotBlank(olePlaceRequestForm.getPickUpLocation())) {
                String[] pickUpLocations = olePlaceRequestForm.getPickUpLocation().split(",");
                for (String location : pickUpLocations) {
                    pickUpLocationList.add(location);
                }
            }



            if (CollectionUtils.isNotEmpty(pickUpLocationList)) {
                List<OleCirculationDesk> oleCirculationDeskLocations = (List<OleCirculationDesk>) getBusinessObjectService().findAll(OleCirculationDesk.class);
                //TODO: Get drop-down values dynamically by parsing DocumentConfig.xml file
                if (oleCirculationDeskLocations != null && oleCirculationDeskLocations.size() > 0) {
                    for (OleCirculationDesk oleCirculationDeskLocation : oleCirculationDeskLocations) {
                        if (pickUpLocationList.contains(oleCirculationDeskLocation.getCirculationDeskCode())) {
                            if(defaultPickupLocation!=null){
                                if(!defaultPickupLocation.getCirculationDeskId().equals(oleCirculationDeskLocation.getCirculationDeskId())){
                                    options.add(new ConcreteKeyValue(oleCirculationDeskLocation.getCirculationDeskId(), oleCirculationDeskLocation.getCirculationDeskCode()));
                                }
                            }else{
                                options.add(new ConcreteKeyValue(oleCirculationDeskLocation.getCirculationDeskId(), oleCirculationDeskLocation.getCirculationDeskCode()));
                            }
                        }
                    }
                }
            }
        }else{
            Map<String,String> criteriaMap = new HashMap<String,String>();
            criteriaMap.put("circulationPickUpDeskLocation",olePlaceRequestForm.getItemLocation());
            criteriaMap.put("oleCirculationDesk.pickUpLocation","true");
            List<OleCirculationDeskLocation> pickUpDesks  = (List<OleCirculationDeskLocation>)getBusinessObjectService().findMatching(OleCirculationDeskLocation.class,criteriaMap);
            if(pickUpDesks.size()>0){
                for(OleCirculationDeskLocation pickUDesk : pickUpDesks){
                    if(defaultPickupLocation!=null){
                        if(!defaultPickupLocation.getCirculationDeskId().equals(pickUDesk.getCirculationDeskId())){
                            options.add(new ConcreteKeyValue(pickUDesk.getCirculationDeskId(),pickUDesk.getOleCirculationDesk().getCirculationDeskCode()));
                        }
                    }else{
                        options.add(new ConcreteKeyValue(pickUDesk.getCirculationDeskId(),pickUDesk.getOleCirculationDesk().getCirculationDeskCode()));
                    }
                }
            }
        }


        if(defaultPickupLocation!=null){
            options.add(0,new ConcreteKeyValue(defaultPickupLocation.getCirculationDeskId(),defaultPickupLocation.getCirculationDeskCode()));
        }else{
            options.add(0,new ConcreteKeyValue("",""));
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
            if(oleCirculationDeskDetail.getOleCirculationDesk()!=null&& oleCirculationDeskDetail.getOleCirculationDesk().getDefaultPickupLocation() !=null && oleCirculationDeskDetail.getOleCirculationDesk().getDefaultPickupLocation().isPickUpLocation()){
                oleCirculationDesk = oleCirculationDeskDetail.getOleCirculationDesk().getDefaultPickupLocation();
            }

        }
        return oleCirculationDesk;
    }
}
