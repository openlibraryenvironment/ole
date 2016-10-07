package org.kuali.ole.gobi.valueFinder;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.gobi.bo.GobiSubAccountDeliveryAddressDocument;
import org.kuali.ole.sys.businessobject.Room;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;
import org.kuali.rice.krad.uif.view.ViewModel;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;

import java.util.*;

public class RoomNumberValueFinder extends UifKeyValuesFinderBase {
    @Override
    public List<KeyValue> getKeyValues(ViewModel model) {
        List<KeyValue> options = new ArrayList<KeyValue>();
        List<ConcreteKeyValue> concreteKeyValues = new ArrayList<ConcreteKeyValue>();

        MaintenanceDocumentForm maintenanceDocumentForm = (MaintenanceDocumentForm) model;
        MaintenanceDocument document = maintenanceDocumentForm.getDocument();
        GobiSubAccountDeliveryAddressDocument dataObject = (GobiSubAccountDeliveryAddressDocument) document.getNewMaintainableObject().getDataObject();
        String buildingCode = dataObject.getBuildingCode();
        if (StringUtils.isNotBlank(buildingCode)) {
            Map<String, String> parameterMap = new HashMap<>();
            parameterMap.put("buildingCode", buildingCode);
            List<Room> matching = (List<Room>) KRADServiceLocator.getBusinessObjectService().findMatching(Room.class, parameterMap);
            if (CollectionUtils.isNotEmpty(matching)) {
                for (Iterator<Room> iterator = matching.iterator(); iterator.hasNext(); ) {
                    Room room = iterator.next();
                    concreteKeyValues.add(new ConcreteKeyValue(room.getBuildingRoomNumber(), room.getBuildingRoomNumber()));
                }
            }
        }
        Collections.sort(concreteKeyValues);
        options.addAll(concreteKeyValues);
        return options;
    }
}
