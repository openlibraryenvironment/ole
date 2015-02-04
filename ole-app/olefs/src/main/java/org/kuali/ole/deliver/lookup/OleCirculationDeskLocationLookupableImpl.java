package org.kuali.ole.deliver.lookup;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
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
 * OleLicenseRequestLookupableImpl is the view helper service class for Ole License Request Document
 */
public class OleCirculationDeskLocationLookupableImpl extends LookupableImpl {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleCirculationDeskLocationLookupableImpl.class);

    /**
     * This method returns action URL as oleLicenseRequest.
     *
     * @param lookupForm
     * @param dataObject
     * @param methodToCall
     * @param pkNames
     * @return String
     */
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

        props.put(UifParameters.DATA_OBJECT_CLASS_NAME, OleCirculationDesk.class.getName());
        props.put(UifParameters.VIEW_TYPE_NAME, UifConstants.ViewType.MAINTENANCE.name());

        String maintenanceMapping = "oleCirculationDeskMaintenance";

        return UrlFactory.parameterizeUrl(maintenanceMapping, props);
    }

    @Override
    protected List<?> getSearchResults(LookupForm form, Map<String, String> searchCriteria, boolean unbounded) {

        List<?> searchResults;
        List<OleCirculationDesk> finalSearchResult = new ArrayList<OleCirculationDesk>();
        List<OleCirculationDesk> oleCirculationDesk = new ArrayList<OleCirculationDesk>();

        oleCirculationDesk = (List<OleCirculationDesk>) super.getSearchResults(form, searchCriteria, unbounded);
        finalSearchResult.addAll(oleCirculationDesk);

        searchResults = finalSearchResult;
        if (searchResults.size() == 0) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.NO_RECORD_FOUND);
        }
        sortSearchResults(form, searchResults);
        return searchResults;
    }


}
