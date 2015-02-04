package org.kuali.ole.select.lookup;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OleCheckListBo;
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
 * OleCheckListLookupableImpl is the view helper service class for Check List Maintenance Document
 */
public class OleCheckListLookupableImpl extends LookupableImpl {
    private String returnLocation = "";

    /**
     * Gets the returnLocation attribute.
     *
     * @return Returns the returnLocation
     */
    public String getReturnLocation() {
        return returnLocation;
    }

    /**
     * Sets the returnLocation attribute value.
     *
     * @param returnLocation The returnLocation to set.
     */
    public void setReturnLocation(String returnLocation) {
        this.returnLocation = returnLocation;
    }

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
            returnLocation = lookupForm.getReturnLocation();
        }

        props.put(UifParameters.DATA_OBJECT_CLASS_NAME, lookupForm.getDataObjectClassName());
        props.put(UifParameters.VIEW_TYPE_NAME, UifConstants.ViewType.MAINTENANCE.name());

        String maintenanceMapping = OLEConstants.OleCheckList.CHECK_LIST_MAINTENANCE_ACTION_LINK;
        return UrlFactory.parameterizeUrl(maintenanceMapping, props);
    }

    /**
     * This method returns URL for downloading the checklist template.
     *
     * @param object
     * @return url
     */
    public String performDownload(Object object) {
        OleCheckListBo oleCheckListBo = (OleCheckListBo) object;
        String url = OLEConstants.OleCheckList.CHECK_LIST_LINK + getReturnLocation() + OLEConstants.OleCheckList.METHOD_TO_CALL
                + oleCheckListBo.getOleCheckListId();

        return url;
    }

    /**
     * This method returns checklist name, to be displayed in the result field.
     *
     * @param object
     * @return checkListName
     */
    public String getCheckListName(Object object) {
        String checkListName = "";
        OleCheckListBo oleCheckListBo = (OleCheckListBo) object;
        checkListName = oleCheckListBo.getName();
        return checkListName;
    }

    @Override
    protected List<?> getSearchResults(LookupForm form, Map<String, String> searchCriteria, boolean unbounded) {
        List<?> searchResults;
        List<OleCheckListBo> finalSearchResult = new ArrayList<OleCheckListBo>();
        List<OleCheckListBo> oleCheckListBo = new ArrayList<OleCheckListBo>();


        oleCheckListBo = (List<OleCheckListBo>) super.getSearchResults(form, searchCriteria, unbounded);
        finalSearchResult.addAll(oleCheckListBo);

        searchResults = finalSearchResult;

        if (searchResults.size() == 0) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.NO_RECORD_FOUND);
        }
        sortSearchResults(form, searchResults);
        return searchResults;
    }
}
