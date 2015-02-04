package org.kuali.ole.deliver.lookup;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.alert.bo.AlertDocument;
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
 * Created by arunag on 12/30/14.
 */
public class AlertDocumentLookupableImpl extends LookupableImpl {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleDeliverRequestLookupableImpl.class);

    @Override
    protected String getActionUrlHref(LookupForm lookupForm, Object dataObject, String methodToCall,
                                      List<String> pkNames) {
        LOG.debug("Inside getActionUrlHref()");

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

        props.put(UifParameters.DATA_OBJECT_CLASS_NAME, AlertDocument.class.getName());
        props.put(UifParameters.VIEW_TYPE_NAME, UifConstants.ViewType.MAINTENANCE.name());

        String maintenanceMapping = OLEConstants.ALERT_DOC_MAINTENANCE_ACTION_LINK;

        return UrlFactory.parameterizeUrl(maintenanceMapping, props);
    }

}
