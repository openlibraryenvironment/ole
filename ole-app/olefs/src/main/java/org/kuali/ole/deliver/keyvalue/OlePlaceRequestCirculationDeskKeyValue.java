package org.kuali.ole.deliver.keyvalue;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OleCirculationDeskLocation;
import org.kuali.ole.deliver.controller.drools.RuleExecutor;
import org.kuali.ole.deliver.form.OLEPlaceRequestForm;
import org.kuali.ole.deliver.util.DroolsResponse;
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
import java.util.Map;

/**
 * Created by maheswarang on 2/6/15.
 */
public class OlePlaceRequestCirculationDeskKeyValue extends UifKeyValuesFinderBase {

    private BusinessObjectService businessObjectService;
    private RuleExecutor ruleExecutor;

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
                    options.add(new ConcreteKeyValue(oleCirculationDeskLocation.getCirculationDeskId(), oleCirculationDeskLocation.getCirculationDeskCode()));
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
                    options.add(new ConcreteKeyValue(pickUDesk.getCirculationDeskId(),pickUDesk.getOleCirculationDesk().getCirculationDeskCode()));
                }
            }
         }
        return options;
    }


}
