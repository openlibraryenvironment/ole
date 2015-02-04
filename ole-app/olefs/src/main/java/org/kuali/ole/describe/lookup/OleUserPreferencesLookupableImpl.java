package org.kuali.ole.describe.lookup;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.ImportBibUserPreferences;
import org.kuali.rice.krad.lookup.LookupableImpl;
import org.kuali.rice.krad.service.KRADServiceLocator;
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
 * User: PJ7789
 * Date: 27/12/12
 * Time: 6:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleUserPreferencesLookupableImpl
        extends LookupableImpl {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(
            OleUserPreferencesLookupableImpl.class);

    /**
     * This method is to override the maintenance mapping
     *
     * @param lookupForm
     * @param dataObject
     * @param methodToCall
     * @param pkNames
     * @return mapping Url
     */
    @Override
    protected String getActionUrlHref(LookupForm lookupForm, Object dataObject, String methodToCall,
                                      List<String> pkNames) {
        LOG.debug("Inside getActionUrlHref()");
        Properties props = new Properties();
        props.put(KRADConstants.DISPATCH_REQUEST_PARAMETER, methodToCall);
        LOG.info("Method to call = " + methodToCall);
        Map<String, String> primaryKeyValues = KRADUtils.getPropertyKeyValuesFromDataObject(pkNames, dataObject);
        for (String primaryKey : primaryKeyValues.keySet()) {
            String primaryKeyValue = primaryKeyValues.get(primaryKey);

            props.put(primaryKey, primaryKeyValue);
        }

        if (StringUtils.isNotBlank(lookupForm.getReturnLocation())) {
            props.put(KRADConstants.RETURN_LOCATION_PARAMETER, lookupForm.getReturnLocation());
        }

        props.put(UifParameters.DATA_OBJECT_CLASS_NAME, ImportBibUserPreferences.class.getName());
        props.put(UifParameters.VIEW_TYPE_NAME, UifConstants.ViewType.MAINTENANCE.name());

        String maintenanceMapping = OLEConstants.OleUserPreferences.USER_PREF_MAINTENANCE_ACTION_LINK;

        return UrlFactory.parameterizeUrl(maintenanceMapping, props);
    }

    @Override
    public Collection<?> performSearch(LookupForm form, Map<String, String> searchCriteria, boolean bounded) {
        LOG.debug("Inside performSearch()");
        Map<String, String> criteriaMap = new HashMap<String, String>();
        List<ImportBibUserPreferences> importBibUserPreferencesList = new ArrayList<ImportBibUserPreferences>();

        String shelvingScheme = ((String) searchCriteria.get("shelvingScheme"));
        if (shelvingScheme != null && !shelvingScheme.equals("")) {
            criteriaMap.put("shelvingScheme", shelvingScheme);
            LOG.info("shelvingScheme = " + shelvingScheme);
        }
        String importStatus = ((String) searchCriteria.get("importStatus"));
        if (importStatus != null && !importStatus.equals("")) {
            criteriaMap.put("importStatus", importStatus);
            LOG.info("importStatus = " + importStatus);
        }

        String importType = ((String) searchCriteria.get("importType"));
        if (importType != null && !importType.equals("")) {
            criteriaMap.put("importType", importType);
            LOG.info("importType = " + importType);
        }

        String removalTags = ((String) searchCriteria.get("removalTags"));
        if (removalTags != null && !removalTags.equals("")) {
            criteriaMap.put("removalTags", removalTags);
            LOG.info("removalTags = " + removalTags);
        }

        String protectedTags = ((String) searchCriteria.get("protectedTags"));
        if (protectedTags != null && !protectedTags.equals("")) {
            criteriaMap.put("protectedTags", protectedTags);
            LOG.info("protectedTags = " + protectedTags);
        }

        String permLocation = ((String) searchCriteria.get("permLocation"));
        if (permLocation != null && !permLocation.equals("")) {
            criteriaMap.put("permLocation", permLocation);
            LOG.info("permLocation = " + permLocation);
        }

        String callNumberSource1 = ((String) searchCriteria.get("callNumberSource1"));
        if (callNumberSource1 != null && !callNumberSource1.equals("")) {
            criteriaMap.put("callNumberSource1", callNumberSource1);
            LOG.info("callNumberSource1 = " + callNumberSource1);
        }

        String callNumberSource2 = ((String) searchCriteria.get("callNumberSource2"));
        if (callNumberSource2 != null && !callNumberSource2.equals("")) {
            criteriaMap.put("callNumberSource2", callNumberSource2);
            LOG.info("callNumberSource2 = " + callNumberSource2);
        }

        String callNumberSource3 = ((String) searchCriteria.get("callNumberSource3"));
        if (callNumberSource3 != null && !callNumberSource3.equals("")) {
            criteriaMap.put("callNumberSource3", callNumberSource3);
            LOG.info("callNumberSource3 = " + callNumberSource3);
        }

        String prefName = ((String) searchCriteria.get("prefName"));
        if (prefName != null && !prefName.equals("")) {
            criteriaMap.put("prefName", prefName);
            LOG.info("prefName = " + prefName);
        }

        String tempLocation = ((String) searchCriteria.get("tempLocation"));
        if (tempLocation != null && !tempLocation.equals("")) {
            criteriaMap.put("tempLocation", tempLocation);
            LOG.info("tempLocation = " + tempLocation);
        }
        importBibUserPreferencesList = (List<ImportBibUserPreferences>) KRADServiceLocator.getBusinessObjectService().findMatching(ImportBibUserPreferences.class, criteriaMap);
        return importBibUserPreferencesList;
    }
}