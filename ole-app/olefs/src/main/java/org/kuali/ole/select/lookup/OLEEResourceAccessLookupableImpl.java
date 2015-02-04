package org.kuali.ole.select.lookup;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OLEEResourceAccessActivation;
import org.kuali.rice.krad.lookup.LookupableImpl;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.view.LookupView;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADUtils;
import org.kuali.rice.krad.util.UrlFactory;
import org.kuali.rice.krad.web.form.LookupForm;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/11/12
 * Time: 4:01 PM
 * To change this template use File | Settings | File Templates.
 */

public class OLEEResourceAccessLookupableImpl extends LookupableImpl {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OLEEResourceAccessLookupableImpl.class);


    @Override
    protected String getActionUrlHref(LookupForm lookupForm, Object dataObject, String methodToCall,
                                      List<String> pkNames) {
        LOG.debug("Inside getActionUrlHref()");
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

        props.put(UifParameters.DATA_OBJECT_CLASS_NAME, OLEEResourceAccessActivation.class.getName());
        props.put(UifParameters.VIEW_TYPE_NAME, UifConstants.ViewType.MAINTENANCE.name());

        String maintenanceMapping = OLEConstants.ERES_ACCESS_MAINTENANCE_ACTION_LINK;

        return UrlFactory.parameterizeUrl(maintenanceMapping, props);
    }
}


