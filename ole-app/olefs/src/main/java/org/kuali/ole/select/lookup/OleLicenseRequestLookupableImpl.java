package org.kuali.ole.select.lookup;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OleLicenseRequestBo;
import org.kuali.ole.service.OleLicenseRequestService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.lookup.LookupableImpl;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.view.LookupView;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADUtils;
import org.kuali.rice.krad.util.UrlFactory;
import org.kuali.rice.krad.web.form.LookupForm;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * OleLicenseRequestLookupableImpl is the view helper service class for Ole License Request Document
 */
public class OleLicenseRequestLookupableImpl extends LookupableImpl {

    private OleLicenseRequestService oleLicenseRequestService;

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
     * This method returns action URL as oleLicenseRequest.
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

        String maintenanceMapping = "oleLicenseRequest";

        return UrlFactory.parameterizeUrl(maintenanceMapping, props);
    }

    /**
     * This method performs the search for license request based on the search criterias
     *
     * @param form
     * @param criteria
     * @param bounded
     * @return
     */
    @Override
    public Collection<?> performSearch(LookupForm form, Map<String, String> criteria, boolean bounded) {
        returnLocation = form.getReturnLocation();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String createdDateFrom = criteria.get(OLEConstants.OleLicenseRequest.CREATED_FROM_DATE);
        String createdDateTo = criteria.get(OLEConstants.OleLicenseRequest.CREATED_TO_DATE);
        String lastModifiedDateFrom = criteria.get(OLEConstants.OleLicenseRequest.LAST_MOD_FROM_DATE);
        String lastModifiedDateTo = criteria.get(OLEConstants.OleLicenseRequest.LAST_MOD_TO_DATE);
        /*String bibliographicTitle = criteria.get(OLEConstants.OleLicenseRequest.BIB_TITLE);
        String licenseRequestStatusCode = criteria.get(OLEConstants.OleLicenseRequest.STATUS_CODE);
        String assignee = criteria.get(OLEConstants.OleLicenseRequest.ASSIGNEE);
        String locationId = criteria.get(OLEConstants.OleLicenseRequest.LOCATION_ID);
        String licenseRequestTypeId = criteria.get(OLEConstants.OleLicenseRequest.LICENSE_REQUEST_TYPE_ID) ;*/

        if (!createdDateFrom.equalsIgnoreCase("") && !createdDateTo.equalsIgnoreCase("")) {
            if (createdDateFrom.compareTo(createdDateTo) > 0) {
                GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.OleLicenseRequest.ERROR_DATE_FROM_EXCEEDS_DATE_TO);
                return Collections.emptyList();
            }
        }
        if (!lastModifiedDateFrom.equalsIgnoreCase("") && !lastModifiedDateTo.equalsIgnoreCase("")) {
            if (lastModifiedDateFrom.compareTo(lastModifiedDateTo) > 0) {
                GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.OleLicenseRequest.ERROR_DATE_FROM_EXCEEDS_DATE_TO);
                return Collections.emptyList();
            }
        }/*else if (bibliographicTitle.isEmpty() && licenseRequestStatusCode.isEmpty() && assignee.isEmpty() &&
                locationId.isEmpty() && licenseRequestTypeId.isEmpty() && createdDateFrom.equalsIgnoreCase("") && createdDateTo.equalsIgnoreCase("")) {
                createdDateFrom = dateFormat.format(new Date());
                criteria.put(OLEConstants.OleLicenseRequest.CREATED_FROM_DATE,createdDateFrom);
            }*/
        Collection<?> displayList = null;
        try {
            displayList = getOleLicenseRequestservice().findLicenseRequestByCriteria(criteria);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        if (displayList.size() == 0) {
            GlobalVariables.getMessageMap().putInfo(OLEConstants.OleLicenseRequest.ERROR_NOT_FOUND, OLEConstants.OleLicenseRequest.ERROR_NOT_FOUND);
        }
        return displayList;

    }


    /**
     * This method returns the url for viewing the Requisition Document
     *
     * @param object
     * @return url
     */
    public String viewParticularDocument(Object object) {
        OleLicenseRequestBo oleLicenseRequestBo = (OleLicenseRequestBo) object;
        String url = "";
        if (returnLocation != null & !returnLocation.isEmpty())
            url = returnLocation.substring(0, returnLocation.indexOf("portal.do"));
        return url + "kew/DocHandler.do?command=displayDocSearchView&docId=" + oleLicenseRequestBo.getDocumentNumber();
    }

    /**
     * This method returns the OleLicenseRequestService object
     *
     * @return
     */
    public OleLicenseRequestService getOleLicenseRequestservice() {
        if (oleLicenseRequestService == null) {
            oleLicenseRequestService = GlobalResourceLoader.getService("oleLicenseRequestService");
        }
        return oleLicenseRequestService;
    }

}
