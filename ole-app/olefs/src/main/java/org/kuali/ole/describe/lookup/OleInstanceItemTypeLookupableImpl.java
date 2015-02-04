package org.kuali.ole.describe.lookup;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleInstanceItemType;
import org.kuali.rice.krad.lookup.LookupableImpl;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.view.LookupView;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADUtils;
import org.kuali.rice.krad.util.UrlFactory;
import org.kuali.rice.krad.web.form.LookupForm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 1/24/13
 * Time: 2:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleInstanceItemTypeLookupableImpl extends LookupableImpl {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleInstanceItemTypeLookupableImpl.class);


    /**
     * Generates a URL to perform a maintenance action on the given result data object
     * <p/>
     * <p>
     * Will build a URL containing keys of the data object to invoke the given maintenance action method
     * within the maintenance controller
     * </p>
     *
     * @param dataObject   - data object instance for the line to build the maintenance action link for
     * @param methodToCall - method name on the maintenance controller that should be invoked
     * @param pkNames      - list of primary key field names for the data object whose key/value pairs will be added to
     *                     the maintenance link
     * @return String URL link for the maintenance action
     */
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

        String maintenanceMapping = "instanceItemTypeMaintenance";

        return UrlFactory.parameterizeUrl(maintenanceMapping, props);


    }

    @Override
    protected List<?> getSearchResults(LookupForm form, Map<String, String> searchCriteria, boolean unbounded) {
        List<?> searchResults;
        List<OleInstanceItemType> finalSearchResult = new ArrayList<OleInstanceItemType>();
        List<OleInstanceItemType> oleInstanceItemType = new ArrayList<OleInstanceItemType>();


        oleInstanceItemType = (List<OleInstanceItemType>) super.getSearchResults(form, searchCriteria, unbounded);
        finalSearchResult.addAll(oleInstanceItemType);

        searchResults = finalSearchResult;
        if (searchResults.size() == 0) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.NO_RECORD_FOUND);
        }
        sortSearchResults(form, searchResults);
        return searchResults;
    }

}