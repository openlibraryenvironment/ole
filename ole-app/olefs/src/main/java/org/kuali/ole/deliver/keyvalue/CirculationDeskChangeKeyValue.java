package org.kuali.ole.deliver.keyvalue;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OleCirculationDeskDetail;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/19/12
 * Time: 12:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class CirculationDeskChangeKeyValue extends KeyValuesBase {

    public static List<KeyValue> circulationDeskKeyValues = null;

    public static long timeLastRefreshed;

    public static int refreshInterval = 300;     // in seconds

    private PermissionService getPermissionService() {
        PermissionService service = KimApiServiceLocator.getPermissionService();
        return service;
    }

    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> options = new ArrayList<KeyValue>();
        Map<String, String> userMap = new HashMap<String, String>();
        userMap.put("operatorId", GlobalVariables.getUserSession().getPrincipalId());
        /*  List<String> myList = new CopyOnWriteArrayList<String>()<String>();*/
        Collection<OleCirculationDeskDetail> oleCirculationDeskDetails = KRADServiceLocator.getBusinessObjectService().findMatching(OleCirculationDeskDetail.class, userMap);

        for (OleCirculationDeskDetail oleCirculationDeskDetail : oleCirculationDeskDetails) {
            if (oleCirculationDeskDetail.isDefaultLocation() && oleCirculationDeskDetail.getOleCirculationDesk().isActive()) {
                options.add(new ConcreteKeyValue(oleCirculationDeskDetail.getOleCirculationDesk().getCirculationDeskId(), oleCirculationDeskDetail.getOleCirculationDesk().getCirculationDeskCode()));
            }
        }
        for (OleCirculationDeskDetail oleCirculationDeskDetail : oleCirculationDeskDetails) {
            if (!oleCirculationDeskDetail.isDefaultLocation() && oleCirculationDeskDetail.getOleCirculationDesk().isActive()) {
                options.add(new ConcreteKeyValue(oleCirculationDeskDetail.getOleCirculationDesk().getCirculationDeskId(), oleCirculationDeskDetail.getOleCirculationDesk().getCirculationDeskCode()));
            }
        }
        /*if (options.size() < 1) {
            if (getPermissionService().hasPermission(GlobalVariables.getUserSession().getPrincipalId(), OLEConstants.OlePatron.PATRON_NAMESPACE, OLEConstants.CAN_OVERRIDE_LOAN) || getPermissionService().hasPermission(GlobalVariables.getUserSession().getPrincipalId(), OLEConstants.OlePatron.PATRON_NAMESPACE, OLEConstants.CAN_LOAN)) {
                //GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_VALIDATIONS);
            } else {
                //GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.UNAUTHORIZED_LOAN_USER);
            }
        }*/
        return options;
    }

    public static List<KeyValue> initCirculationDeskDetails() {

        List<KeyValue> options = new ArrayList<KeyValue>();
        Collection<OleCirculationDesk> oleCirculationDesks = KRADServiceLocator.getBusinessObjectService().findAll(OleCirculationDesk.class);
        for (OleCirculationDesk oleCirculationDesk : oleCirculationDesks) {
            if (oleCirculationDesk.isActive()) {
                options.add(new ConcreteKeyValue(oleCirculationDesk.getCirculationDeskId(), oleCirculationDesk.getCirculationDeskCode()));
            }
        }
        return options;
    }


    public static List<String> retrieveCirculationDeskDetailsForSuggest(String locationVal) {
        List<KeyValue> locationKeyValues = retrieveLocationDetails();
        List<String> locationValues = new ArrayList<String>();
        for (KeyValue keyValue : locationKeyValues) {
            locationValues.add(keyValue.getValue());
        }

        Pattern pattern = Pattern.compile("[?$(){}\\[\\]\\^\\\\]");
        Matcher matcher = pattern.matcher(locationVal);
        if (matcher.matches()) {
            return new ArrayList<String>();
        }

        if (!locationVal.equalsIgnoreCase("*")) {
            locationValues = Lists.newArrayList(Collections2.filter(locationValues, Predicates.contains(Pattern.compile(locationVal, Pattern.CASE_INSENSITIVE))));
        }
        Collections.sort(locationValues);
        return locationValues;
    }

    private static List<KeyValue> retrieveLocationDetails() {
        long currentTime = System.currentTimeMillis() / 1000;
        if (circulationDeskKeyValues == null) {
            circulationDeskKeyValues = initCirculationDeskDetails();
            timeLastRefreshed = currentTime;
        } else {
            if (currentTime - timeLastRefreshed > refreshInterval) {
                circulationDeskKeyValues = initCirculationDeskDetails();
                timeLastRefreshed = currentTime;
            }
        }
        return circulationDeskKeyValues;
    }

}
