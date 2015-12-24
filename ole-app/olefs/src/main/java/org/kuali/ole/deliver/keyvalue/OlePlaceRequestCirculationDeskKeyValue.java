package org.kuali.ole.deliver.keyvalue;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.Agenda;
import org.kie.api.runtime.rule.AgendaGroup;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OleCirculationDeskLocation;
import org.kuali.ole.deliver.drools.CustomAgendaFilter;
import org.kuali.ole.deliver.drools.DroolsKieEngine;
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
import java.util.Iterator;
import java.util.List;

/**
 * Created by maheswarang on 2/6/15.
 */
public class OlePlaceRequestCirculationDeskKeyValue extends UifKeyValuesFinderBase {

    private BusinessObjectService businessObjectService;


    public void fireRules(List<Object> facts, String[] expectedRules, String agendaGroup) {
        KieSession session = DroolsKieEngine.getInstance().getSession();
        for (Iterator<Object> iterator = facts.iterator(); iterator.hasNext(); ) {
            Object fact = iterator.next();
            session.insert(fact);
        }

        if (null != expectedRules && expectedRules.length > 0) {
            session.fireAllRules(new CustomAgendaFilter(expectedRules));
        } else {
            Agenda agenda = session.getAgenda();
            AgendaGroup group = agenda.getAgendaGroup(agendaGroup);
            group.setFocus();
            session.fireAllRules();
        }
        session.dispose();
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
        List<Object> facts = new ArrayList<>();
        List<KeyValue> options = new ArrayList<KeyValue>();
        OLEPlaceRequestForm olePlaceRequestForm = (OLEPlaceRequestForm) viewModel;
        facts.add(olePlaceRequestForm);
        facts.add(olePlaceRequestForm.getOlePatronDocument());
        fireRules(facts, null, "pickup-location");
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
        return options;
    }


}
