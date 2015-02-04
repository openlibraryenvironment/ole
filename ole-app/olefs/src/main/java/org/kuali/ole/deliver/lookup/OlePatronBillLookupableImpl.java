package org.kuali.ole.deliver.lookup;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.bo.PatronBillPayment;
import org.kuali.ole.service.OlePatronHelperService;
import org.kuali.ole.service.OlePatronHelperServiceImpl;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.kim.impl.identity.email.EntityEmailBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameBo;
import org.kuali.rice.krad.bo.ExternalizableBusinessObject;
import org.kuali.rice.krad.lookup.LookupUtils;
import org.kuali.rice.krad.lookup.LookupableImpl;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.view.LookupView;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADUtils;
import org.kuali.rice.krad.util.UrlFactory;
import org.kuali.rice.krad.web.form.LookupForm;

import java.util.*;

/**
 * OlePatronLookupableImpl makes validation  and populate the search criteria and return the search results
 */
public class OlePatronBillLookupableImpl extends LookupableImpl {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OlePatronBillLookupableImpl.class);
    OlePatronHelperService olePatronHelperService = new OlePatronHelperServiceImpl();
    private String firstName;
    private String middleName;
    private String lastName;


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

        String maintenanceMapping = "patronBillMaintenance";

        return UrlFactory.parameterizeUrl(maintenanceMapping, props);
    }

    /**
     * This method will populate the search criteria and return the search results
     *
     * @param form
     * @param searchCriteria
     * @param bounded
     * @return displayList(Collection)
     */
    @Override
    public Collection<?> performSearch(LookupForm form, Map<String, String> searchCriteria, boolean bounded) {
        LOG.debug("Inside performSearch()");
        List<PatronBillPayment> patronBillPayments = new ArrayList<PatronBillPayment>();
        List<PatronBillPayment> billPayments = new ArrayList<PatronBillPayment>();
        List<PatronBillPayment> finalResult = new ArrayList<PatronBillPayment>();
        List<String> olePatronIdList = new ArrayList<String>();
        Map<String, String> searchEntityMap = new HashMap<String, String>();
        LookupUtils.preprocessDateFields(searchCriteria);
        firstName = searchCriteria.get(OLEConstants.OlePatron.PATRON_FIRST_NAME);
        lastName = searchCriteria.get(OLEConstants.OlePatron.PATRON_LAST_NAME);
        searchCriteria.remove("firstName");
        searchCriteria.remove("lastName");
        if (StringUtils.isNotEmpty(firstName)) {
            searchEntityMap.put("firstName", firstName);
        }
        if (StringUtils.isNotEmpty(lastName)) {
            searchEntityMap.put("lastName", lastName);
        }
        Integer searchResultsLimit = null;
        try {
            //By firstName,lastName,middleName
            if (searchEntityMap.size() > 0) {
                try {
                    List<?> searchResults;
                    Map<String, String> searchEntityCriteria = new HashMap<String, String>();
                    for (Map.Entry<String, String> entry : searchEntityMap.entrySet()) {
                        int counter=0;
                        for( int i=0; i<entry.getValue().length(); i++ ) {
                            if( entry.getValue().charAt(i) == '*' ) {
                                counter++;
                            }
                        }
                        if (counter==0 || (counter!=entry.getValue().length())) {
                            searchEntityCriteria.put(entry.getKey(), entry.getValue());
                        }
                    }
                    if (searchEntityCriteria.size() > 0) {
                        Class entityNameBoClass = Class.forName("org.kuali.rice.kim.impl.identity.name.EntityNameBo");
                        if (LookupUtils.hasExternalBusinessObjectProperty(entityNameBoClass, searchEntityCriteria)) {
                            Map<String, String> eboSearchCriteria = adjustCriteriaForNestedEBOs(searchEntityCriteria, bounded);
                            searchResults = (List<EntityNameBo>) getLookupService().findCollectionBySearchUnbounded(entityNameBoClass, eboSearchCriteria);
                        } else {
                            searchResults = (List<EntityNameBo>) getLookupService().findCollectionBySearchUnbounded(entityNameBoClass, searchEntityCriteria);
                        }
                    } else {
                        searchResults = (List<EntityNameBo>) KRADServiceLocator.getBusinessObjectService().findAll(EntityNameBo.class);
                    }
                    Iterator iterator = searchResults.iterator();
                    while (iterator.hasNext()) {
                        EntityNameBo entityNameBo = (EntityNameBo) iterator.next();
                        if (!olePatronIdList.contains(entityNameBo.getEntityId()))
                            olePatronIdList.add(entityNameBo.getEntityId());
                    }
                    if (searchCriteria.size() > 0) {
                        for (String patronId : olePatronIdList) {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("patronId", patronId);
                            PatronBillPayment patronBillPayment = (PatronBillPayment) KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(PatronBillPayment.class, map);
                            if (patronBillPayment != null) {
                                patronBillPayments.add(patronBillPayment);
                            }
                        }
                    }
                    Class entityPatronBoClass = Class.forName("org.kuali.ole.deliver.bo.PatronBillPayment");
                    if (bounded) {
                        searchResultsLimit = LookupUtils.getSearchResultsLimit(entityPatronBoClass, form);
                    }
                    int count=0;
                    if (searchResultsLimit != null) {
                        iterator = patronBillPayments.iterator();
                        while (iterator.hasNext()) {
                            PatronBillPayment billPayment = (PatronBillPayment) iterator.next();
                            if (count < searchResultsLimit.intValue()) {
                                billPayments.add(billPayment);
                                count++;
                            } else {
                                break;
                            }
                        }
                    } else {
                        billPayments.addAll(patronBillPayments);
                    }
                    String resultsPropertyName = "LookupResultMessages";
                    GlobalVariables.getMessageMap().clearErrorMessages();
                    GlobalVariables.getMessageMap().removeAllInfoMessagesForProperty(resultsPropertyName);
                    GlobalVariables.getMessageMap().removeAllWarningMessagesForProperty(resultsPropertyName);
                    if (billPayments.size() == 0) {
                        GlobalVariables.getMessageMap().putInfoForSectionId(resultsPropertyName,
                                RiceKeyConstants.INFO_LOOKUP_RESULTS_NONE_FOUND);
                    } else if (billPayments.size() == 1) {
                        GlobalVariables.getMessageMap().putInfoForSectionId(resultsPropertyName,
                                RiceKeyConstants.INFO_LOOKUP_RESULTS_DISPLAY_ONE);
                    } else if (billPayments.size() > 1) {
                        Boolean resultsExceedsLimit = bounded && searchResultsLimit != null && billPayments.size() > 0 && billPayments.size() > searchResultsLimit ? true : false;
                        if (resultsExceedsLimit) {
                            GlobalVariables.getMessageMap().putInfoForSectionId(resultsPropertyName,
                                    RiceKeyConstants.INFO_LOOKUP_RESULTS_EXCEEDS_LIMIT, billPayments.size() + "",
                                    searchResultsLimit.toString());
                        } else {

                            GlobalVariables.getMessageMap().putInfoForSectionId(resultsPropertyName,
                                    RiceKeyConstants.INFO_LOOKUP_RESULTS_DISPLAY_ALL, billPayments.size() + "");
                        }
                    }
                patronBillPayments.clear();
                patronBillPayments.addAll(billPayments);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Error trying to perform search", e);
                } catch (InstantiationException e1) {
                    throw new RuntimeException("Error trying to perform search", e1);
                }
            } else {
                patronBillPayments =(List<PatronBillPayment>) super.performSearch(form, searchCriteria, bounded);
            }

        } catch (Exception e) {
            LOG.error("Exception Occurred In Searching of Patrons------>Patron Bill Lookup");
        }
        Iterator iterator=patronBillPayments.iterator();
        while (iterator.hasNext()){
            PatronBillPayment patronBillPayment=(PatronBillPayment)iterator.next();
            Map<String, String> map = new HashMap<String, String>();
            map.put("entityId", patronBillPayment.getPatronId());
            EntityNameBo entityNameBo=(EntityNameBo)KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(EntityNameBo.class,map);
            if(entityNameBo!=null){
                patronBillPayment.setFirstName(entityNameBo.getFirstName());
                patronBillPayment.setLastName(entityNameBo.getLastName());
            }
            finalResult.add(patronBillPayment);
        }

        return finalResult;
    }

    public boolean isWildCardMatches(String word, String wildCardString) {
        LOG.debug("Applying WildCard Search");
        boolean isSuccess = true;
        if (wildCardString.contains("*")) {
            if (wildCardString.equalsIgnoreCase("*")) {
                isSuccess = true;
            } else {
                wildCardString = wildCardString.replace('*', ',');
                String[] wCardString = wildCardString.split(",");
                if (wCardString != null && wCardString.length > 0) {
                    for (String str : wCardString) {
                        if (word.toLowerCase().contains(str.toLowerCase())) {
                            isSuccess = isSuccess && true;
                        } else {
                            isSuccess = isSuccess && false;
                        }
                    }
                } else {
                    isSuccess = false;
                    if (word.equalsIgnoreCase(wildCardString)) {
                        isSuccess = true;
                    }
                }
            }
            return isSuccess;
        } else {
            if (wildCardString.equalsIgnoreCase(word)) {
                return true;
            } else {
                return false;
            }
        }
    }

}
