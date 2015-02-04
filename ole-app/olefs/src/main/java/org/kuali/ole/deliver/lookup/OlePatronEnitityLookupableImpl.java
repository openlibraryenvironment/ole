package org.kuali.ole.deliver.lookup;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OleLookupableImpl;
import org.kuali.ole.deliver.bo.OLEPatronEntityViewBo;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.bo.PatronBillPayment;
import org.kuali.ole.service.OlePatronHelperService;
import org.kuali.ole.service.OlePatronHelperServiceImpl;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.kim.impl.identity.email.EntityEmailBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameBo;
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneBo;
import org.kuali.rice.krad.lookup.LookupUtils;
import org.kuali.rice.krad.lookup.LookupableImpl;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.field.InputField;
import org.kuali.rice.krad.uif.util.ComponentUtils;
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
public class OlePatronEnitityLookupableImpl extends OleLookupableImpl {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OlePatronEnitityLookupableImpl.class);
    List<?> searchResults;
    OlePatronHelperService olePatronHelperService = new OlePatronHelperServiceImpl();
    public static int count = 0;

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
        List<OLEPatronEntityViewBo> olePatronDocuments=new ArrayList<OLEPatronEntityViewBo>();
        List<OLEPatronEntityViewBo> finalResult=new ArrayList<OLEPatronEntityViewBo>();
        List<String> olePatronIdList=new ArrayList<String>();
        Map<String,String> searchEntityMap=new HashMap<String,String>();
        Map<String,String> searchEntityPhoneMap=new HashMap<String,String>();
        Map<String,String> searchEntityEmailMap=new HashMap<String,String>();
        LookupUtils.preprocessDateFields(searchCriteria);
        String firstName = searchCriteria.get(OLEConstants.OlePatron.PATRON_FIRST_NAME);
        String middleName = searchCriteria.get("middleName");
        String lastName = searchCriteria.get(OLEConstants.OlePatron.PATRON_LAST_NAME);
        String email = searchCriteria.get("emailAddress");
        String phoneNumber = searchCriteria.get("phoneNumber");
        String patronType = searchCriteria.get("patronType");
        if(StringUtils.isNotEmpty(searchCriteria.get("patronBarcode"))){
            searchCriteria.remove("active");
        }
        if(StringUtils.isNotEmpty(firstName)){
            searchEntityMap.put("firstName",firstName);
        }
        if(StringUtils.isNotEmpty(lastName)){
            searchEntityMap.put("lastName",lastName);
        }
        if(StringUtils.isNotEmpty(middleName)){
            searchEntityMap.put("middleName",middleName);
        }
        if(StringUtils.isNotEmpty(phoneNumber)){
            searchEntityPhoneMap.put("phoneNumber",phoneNumber);
        }
        if(StringUtils.isNotEmpty(email)){
            searchEntityEmailMap.put("emailAddress",email);
        }

        if (StringUtils.isNotBlank(phoneNumber)) {
            if (!validatePhoneNumber(phoneNumber)) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.INVALID_PHONE_NUMBER_FORMAT);
                return new ArrayList<Object>();
            }
            phoneNumber = buildPhoneNumber(phoneNumber);
        }
        //finalResult = (List<OLEPatronEntityViewBo>) super.performSearch(form,searchCriteria,true);
        Collection<?> displayList;

        // TODO: force uppercase will be done in binding at some point
        displayList = getSearchResults(form, LookupUtils.forceUppercase(getDataObjectClass(), searchCriteria),
                !bounded);

        // TODO delyea - is this the best way to set that the entire set has a returnable row?
        for (Object object : displayList) {
           /* if(object instanceof OLEPatronEntityViewBo){
                OLEPatronEntityViewBo patronEntityViewBo = (OLEPatronEntityViewBo) object;
                patronEntityViewBo.setCreateBillUrl(getPatronBillUrl(patronEntityViewBo.getPatronId(),patronEntityViewBo.getFirstName(),patronEntityViewBo.getLastName()));
                if(patronEntityViewBo.getBillCount()>0){
                    patronEntityViewBo.setPatronBillFileName("Patron Bill");
                    patronEntityViewBo.setViewBillUrl("patronbill?viewId=BillView&amp;methodToCall=start&amp;patronId=" + patronEntityViewBo.getPatronId());
                }
            }*/
            if (isResultReturnable(object)) {
                form.setAtLeastOneRowReturnable(true);
            }
        }

        finalResult = (List<OLEPatronEntityViewBo>) displayList;
        searchResults=finalResult;
        return finalResult;
    }



    /**
     * This method will validate the criteria fields
     *
     * @param lookupView
     * @param form
     * @return criteriaFieldMap(Map)
     */
    @Override
    protected Map<String, InputField> getCriteriaFieldsForValidation(LookupView lookupView, LookupForm form) {
        LOG.debug("Inside getCriteriaFieldsForValidation()");
        Map<String, InputField> criteriaFieldMap = new HashMap<String, InputField>();

        List<InputField> fields = ComponentUtils.getComponentsOfTypeDeep(lookupView.getCriteriaFields(),
                InputField.class);

        for (InputField field : fields) {
            criteriaFieldMap.put(field.getPropertyName(), field);
        }

        return criteriaFieldMap;
    }

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

        props.put(UifParameters.DATA_OBJECT_CLASS_NAME, OlePatronDocument.class.getName());
        props.put(UifParameters.VIEW_TYPE_NAME, UifConstants.ViewType.MAINTENANCE.name());

        String maintenanceMapping = OLEConstants.OlePatron.PATRON_MAINTENANCE_ACTION_LINK;

        return UrlFactory.parameterizeUrl(maintenanceMapping, props);
    }

    /**
     * This method will return the url for create bill link in patron record.
     *
     * @return link url
     */

    public String getPatronBillUrl(String patronId, String firstName, String lastName) {
        String url = "patronBillMaintenance?viewTypeName=MAINTENANCE&returnLocation=%2Fportal.do&methodToCall=start&dataObjectClassName=org.kuali.ole.deliver.bo.PatronBillPayment&patronId=" + patronId + "&firstName=" + firstName + "&lastName=" + lastName;
        return url;
    }

    public boolean isWildCardMatches(String word, String wildCardString) {
        if (LOG.isInfoEnabled()) {
            LOG.info("Applying WildCard Search");
        }
        boolean isSuccess = true;
        if (wildCardString != null && (!wildCardString.equalsIgnoreCase("")) && wildCardString.contains("*")) {
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

    private boolean validatePhoneNumber(String phoneNo) {
        if (LOG.isInfoEnabled()) {
            LOG.info("Validating the Phone Number  Format - ##########, (###)###-#### , ###-###-#### , ### ###-#### , ### ### ####");
        }
        if (phoneNo.matches("\\d{10}")) return true;
        else if (phoneNo.matches("\\d{3}[-]\\d{3}[-]\\d{4}")) return true;
        else if (phoneNo.matches("\\d{3}[\\s]\\d{3}[-]\\d{4}")) return true;
        else if (phoneNo.matches("\\d{3}[\\s]\\d{3}[\\s]\\d{4}")) return true;
        else if (phoneNo.matches("\\(\\d{3}\\)[\\s]\\d{3}[-]\\d{4}")) return true;
        else return false;

    }
    private String buildPhoneNumber(String phoneNumber){
        StringBuilder userPhoneNumber = new StringBuilder();
        for (int i = 0; i < phoneNumber.length(); i++) {
            if (Character.isDigit(phoneNumber.charAt(i))) {
                userPhoneNumber.append(phoneNumber.charAt(i));
            }
        }
        return userPhoneNumber.toString();
    }

}
