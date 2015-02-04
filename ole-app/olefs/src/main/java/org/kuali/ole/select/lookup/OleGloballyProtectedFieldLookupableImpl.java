package org.kuali.ole.select.lookup;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.rice.krad.lookup.LookupableImpl;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.view.LookupView;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADUtils;
import org.kuali.rice.krad.util.UrlFactory;
import org.kuali.rice.krad.web.form.LookupForm;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: JuliyaMonica.S
 * Date: 10/26/12
 * Time: 7:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleGloballyProtectedFieldLookupableImpl extends LookupableImpl {

    /**
     * This method returns  URL for checklist maintenance action.
     *
     * @param lookupForm
     * @param dataObject
     * @param methodToCall
     * @param pkNames
     * @return String
     */
    @Override
    protected String getActionUrlHref(LookupForm lookupForm, Object dataObject, String methodToCall,
                                      List<String> pkNames) {
        LookupView lookupView = (LookupView) lookupForm.getView();

        Properties props = new Properties();
        props.put(KRADConstants.DISPATCH_REQUEST_PARAMETER, methodToCall);

        Map<String, String> primaryKeyValues = KRADUtils.getPropertyKeyValuesFromDataObject(pkNames, dataObject);
        for (String primaryKey : primaryKeyValues.keySet()) {
            String primaryKeyValue = primaryKeyValues.get(primaryKey);

            props.put(primaryKey, primaryKeyValue);
        }

        if (StringUtils.isNotBlank(lookupForm.getReturnLocation())) {
            props.put(KRADConstants.RETURN_LOCATION_PARAMETER, lookupForm.getReturnLocation());
        }

        props.put(UifParameters.DATA_OBJECT_CLASS_NAME, lookupForm.getDataObjectClassName());
        props.put(UifParameters.VIEW_TYPE_NAME, UifConstants.ViewType.MAINTENANCE.name());

        String maintenanceMapping = OLEConstants.OleGloballyProtectedField.GLOBALLY_PROTECTED_ACTION_LINK;
        return UrlFactory.parameterizeUrl(maintenanceMapping, props);
    }
}
