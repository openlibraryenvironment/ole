package org.kuali.ole.deliver.lookup;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OleLookupableImpl;
import org.kuali.ole.deliver.bo.*;
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

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * OlePatronLookupableImpl makes validation  and populate the search criteria and return the search results
 */
public class OlePatronLookupableImpl extends OleLookupableImpl {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OlePatronLookupableImpl.class);
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
    //@Override
  /*  public Collection<?> performSearch(LookupForm form, Map<String, String> searchCriteria, boolean bounded) {
        LOG.debug("Inside performSearch()");
        List<OlePatronDocument> olePatronDocuments=new ArrayList<OlePatronDocument>();
        List<OlePatronDocument> finalResult=new ArrayList<OlePatronDocument>();
        List<String> olePatronIdList=new ArrayList<String>();
        List<String> olePatronPatronIdList=new ArrayList<String>();
        List<String> olePatronCommonIdList=new ArrayList<String>();
        List<String> olePatronEntityNameList=new ArrayList<String>();
        List<String> olePatronEntityPhoneList=new ArrayList<String>();
        List<String> olePatronEntityEmailList=new ArrayList<String>();
        Map<String,String> searchEntityMap=new HashMap<String,String>();
        Map<String,String> tempMap=new HashMap<String,String>();
        Map<String,String> searchEntityPhoneMap=new HashMap<String,String>();
        Map<String,String> searchEntityEmailMap=new HashMap<String,String>();
        LookupUtils.preprocessDateFields(searchCriteria);
        String firstName = searchCriteria.get(OLEConstants.OlePatron.PATRON_FIRST_NAME);
        String middleName = searchCriteria.get("middleName");
        String lastName = searchCriteria.get(OLEConstants.OlePatron.PATRON_LAST_NAME);
        String email = searchCriteria.get("emailAddress");
        String phoneNumber = searchCriteria.get("phoneNumber");
        if(StringUtils.isNotEmpty(searchCriteria.get("barcode"))){
            Map<String, String> map = new HashMap<String, String>();
            map.put(OLEConstants.OlePatron.PATRON_LOST_BARCODE_FLD, searchCriteria.get("barcode"));
            List<OlePatronLostBarcode> olePatronLostBarcodes = (List<OlePatronLostBarcode>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronLostBarcode.class, map);
            if(olePatronLostBarcodes!=null && olePatronLostBarcodes.size()>0){
                  for(Map.Entry<String,String> entry:searchCriteria.entrySet()){
                      if(entry.getKey().equalsIgnoreCase("barcode")){
                          String patronId=olePatronLostBarcodes.get(0).getOlePatronId();
                          Map patronMap = new HashMap();
                          patronMap.put(OLEConstants.OleDeliverRequest.PATRON_ID, patronId);
                          List<OlePatronDocument> olePatronDocumentList = (List<OlePatronDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronDocument.class, patronMap);
                          if (olePatronDocumentList != null && olePatronDocumentList.size() > 0) {
                              entry.setValue(olePatronDocumentList.get(0).getBarcode());
                          }
                      }
                  }
            }
            searchCriteria.remove("activeIndicator");
        }
        searchCriteria.remove("emailAddress");
        searchCriteria.remove("firstName");
        searchCriteria.remove("middleName");
        searchCriteria.remove("lastName");
        searchCriteria.remove("phoneNumber");


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

        Collection<?> displayList;

        // TODO: force uppercase will be done in binding at some point
        displayList = getSearchResults(form, LookupUtils.forceUppercase(getDataObjectClass(), searchCriteria),
                !bounded);

        try {
            //By firstName,lastName,middleName
            if (searchEntityMap.size() > 0) {
                try {
                    Map<String,String> searchEntityCriteria=new HashMap<String,String>();
                    for(Map.Entry<String,String> entry:searchEntityMap.entrySet()){
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
                    if (searchEntityCriteria.size()>0) {
                        Class entityNameBoClass = Class.forName("org.kuali.ole.deliver.bo.OLEPatronEntityViewBo");
                        if (LookupUtils.hasExternalBusinessObjectProperty(entityNameBoClass, searchEntityCriteria)) {
                            Map<String, String> eboSearchCriteria = adjustCriteriaForNestedEBOs(searchEntityCriteria, bounded);
                            searchResults = (List<EntityNameBo>) getLookupService().findCollectionBySearchUnbounded(entityNameBoClass, eboSearchCriteria);
                        } else {
                            searchResults = (List<EntityNameBo>) getLookupService().findCollectionBySearchUnbounded(entityNameBoClass, searchEntityCriteria);
                        }
                    } else {
                        searchResults=(List<EntityNameBo>)KRADServiceLocator.getBusinessObjectService().findAll(EntityNameBo.class);
                    }
                    Iterator iterator = searchResults.iterator();
                    while (iterator.hasNext()) {
                        EntityNameBo entityNameBo=(EntityNameBo)iterator.next();
                        if(!olePatronEntityNameList.contains(entityNameBo.getEntityId()))
                            olePatronEntityNameList.add(entityNameBo.getEntityId());
                        if(!olePatronIdList.contains(entityNameBo.getEntityId()))
                            olePatronIdList.add(entityNameBo.getEntityId());
                    }

                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Error trying to perform search", e);
                } catch (InstantiationException e1) {
                    throw new RuntimeException("Error trying to perform search", e1);
                }

            }

            // TODO delyea - is this the best way to set that the entire set has a returnable row?
            for (Object object : displayList) {
                if(object instanceof OlePatronDocument){
                    OlePatronDocument patronBo = (OlePatronDocument) object;
                    OLEPatronEntityViewBo olePatronEntityViewBo = patronBo.getOlePatronEntityViewBo();
                    patronBo.setFirstName(olePatronEntityViewBo.getFirstName());
                    patronBo.setMiddleName(olePatronEntityViewBo.getMiddleName());
                    patronBo.setLastName(olePatronEntityViewBo.getLastName());
                    patronBo.setEmailAddress(olePatronEntityViewBo.getEmailAddress());
                    patronBo.setPhoneNumber(olePatronEntityViewBo.getPhoneNumber());
                    patronBo.setCreateBillUrl(getPatronBillUrl(patronBo.getOlePatronId(),patronBo.getFirstName(),patronBo.getLastName()));
                    if(olePatronEntityViewBo.getBillCount()>0){
                        patronBo.setPatronBillFileName("Patron Bill");
                        patronBo.setViewBillUrl("patronbill?viewId=BillView&amp;methodToCall=start&amp;patronId=" + patronBo.getOlePatronId());
                    }
                }
                if (isResultReturnable(object)) {
                    form.setAtLeastOneRowReturnable(true);
                }
            }
        }
            catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
       *//* for (int i = 0; i < finalResult.size(); i++) {
            String patronId = ((OlePatronDocument) finalResult.get(i)).getOlePatronId();
            Map patronIdAvailable = new HashMap();
            patronIdAvailable.put("patronId", patronId);
            List<PatronBillPayment> patronBillPaymentList = (List<PatronBillPayment>) KRADServiceLocator.getBusinessObjectService().findMatching(PatronBillPayment.class, patronIdAvailable);
            if (patronBillPaymentList != null && !patronBillPaymentList.isEmpty()) {
                ((OlePatronDocument) finalResult.get(i)).setPatronBillFlag(true);
            } else {
                ((OlePatronDocument) finalResult.get(i)).setPatronBillFlag(false);
            }
        }*//*
        finalResult = (List<OlePatronDocument>) displayList;
        searchResults=finalResult;
        return finalResult;
    }*/

    @Override
    public Collection<?> performSearch(LookupForm form, Map<String, String> searchCriteria, boolean bounded) {
        LOG.debug("Inside performSearch()");
        List<OlePatronDocument> finalResult=new ArrayList<OlePatronDocument>();
        LookupUtils.preprocessDateFields(searchCriteria);
        String borrowerType = searchCriteria.get(OLEConstants.OlePatron.BORROWER_TYPE);
        String barcode = searchCriteria.get(OLEConstants.OlePatron.BARCODE);
        String firstName = searchCriteria.get(OLEConstants.OlePatron.PATRON_FIRST_NAME);
        String middleName = searchCriteria.get(OLEConstants.OlePatron.PATRON_MIDDLE_NAME);
        String lastName = searchCriteria.get(OLEConstants.OlePatron.PATRON_LAST_NAME);
        String email = searchCriteria.get(OLEConstants.OlePatron.PATRON_EMAIL_ADDRESS);
        String phoneNumber = searchCriteria.get(OLEConstants.OlePatron.PATRON_PHONE_NUMNER);
        if(StringUtils.isNotEmpty(barcode)){
            Map<String, String> map = new HashMap<String, String>();
            map.put(OLEConstants.OlePatron.PATRON_LOST_BARCODE_FLD, barcode);
            List<OlePatronLostBarcode> olePatronLostBarcodes = (List<OlePatronLostBarcode>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronLostBarcode.class, map);
            if(olePatronLostBarcodes!=null && olePatronLostBarcodes.size()>0){
                for(Map.Entry<String,String> entry:searchCriteria.entrySet()){
                    if(entry.getKey().equalsIgnoreCase(OLEConstants.OlePatron.BARCODE)){
                        String patronId=olePatronLostBarcodes.get(0).getOlePatronId();
                        Map patronMap = new HashMap();
                        patronMap.put(OLEConstants.OleDeliverRequest.PATRON_ID, patronId);
                        List<OlePatronDocument> olePatronDocumentList = (List<OlePatronDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronDocument.class, patronMap);
                        if (olePatronDocumentList != null && olePatronDocumentList.size() > 0) {
                            entry.setValue(olePatronDocumentList.get(0).getBarcode());
                        }
                    }
                }
            }
            /*searchCriteria.remove(OLEConstants.OlePatron.PATRON_ACTIVE_IND);*/
        }
        searchCriteria.remove(OLEConstants.OlePatron.PATRON_EMAIL_ADDRESS);
        searchCriteria.remove(OLEConstants.OlePatron.PATRON_FIRST_NAME);
        searchCriteria.remove(OLEConstants.OlePatron.PATRON_MIDDLE_NAME);
        searchCriteria.remove(OLEConstants.OlePatron.PATRON_LAST_NAME);
        searchCriteria.remove(OLEConstants.OlePatron.PATRON_PHONE_NUMNER);


        if(StringUtils.isNotEmpty(firstName)){
            searchCriteria.put(OLEConstants.OLEPatronEntityViewBo.PATRON_FIRST_NAME,firstName);
        }
        if(StringUtils.isNotEmpty(borrowerType)){
            searchCriteria.remove(OLEConstants.OlePatron.BORROWER_TYPE);
            searchCriteria.put(OLEConstants.OLEPatronEntityViewBo.PATRON_TYPE_ID,borrowerType);
        }
        if(StringUtils.isNotEmpty(lastName)){
            searchCriteria.put(OLEConstants.OLEPatronEntityViewBo.PATRON_LAST_NAME,lastName);
        }
        if(StringUtils.isNotEmpty(middleName)){
            searchCriteria.put(OLEConstants.OLEPatronEntityViewBo.PATRON_MIDDLE_NAME,middleName);
        }
        if(StringUtils.isNotEmpty(phoneNumber)){
            searchCriteria.put(OLEConstants.OLEPatronEntityViewBo.PATRON_PHONE_NUMBER,phoneNumber);
        }
        if(StringUtils.isNotEmpty(email)){
            searchCriteria.put(OLEConstants.OLEPatronEntityViewBo.PATRON_EMAIL_ADDRESS,email);
        }

        if (StringUtils.isNotBlank(phoneNumber)) {
            if (!validatePhoneNumber(phoneNumber)) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.INVALID_PHONE_NUMBER_FORMAT);
                return new ArrayList<Object>();
            }
            phoneNumber = buildPhoneNumber(phoneNumber);
        }

        Collection<?> displayList;

        // TODO: force uppercase will be done in binding at some point
        displayList = getSearchResults(form, LookupUtils.forceUppercase(getDataObjectClass(), searchCriteria),
                !bounded);

        try {
                // TODO delyea - is this the best way to set that the entire set has a returnable row?
                for (Object object : displayList) {
                    if(object instanceof OlePatronDocument){
                        OlePatronDocument patronBo = (OlePatronDocument) object;
                        OLEPatronEntityViewBo olePatronEntityViewBo = patronBo.getOlePatronEntityViewBo();
                        patronBo.setFirstName(olePatronEntityViewBo.getFirstName());
                        patronBo.setMiddleName(olePatronEntityViewBo.getMiddleName());
                        patronBo.setLastName(olePatronEntityViewBo.getLastName());
                        patronBo.setEmailAddress(olePatronEntityViewBo.getEmailAddress());
                        patronBo.setPhoneNumber(olePatronEntityViewBo.getPhoneNumber());
                        patronBo.setCreateBillUrl(getPatronBillUrl(patronBo.getOlePatronId(),patronBo.getFirstName(),patronBo.getLastName()));
                        if(olePatronEntityViewBo.getBillCount()>0){
                            patronBo.setPatronBillFileName(OLEConstants.OlePatron.PATRON_BILL);
                            patronBo.setViewBillUrl(OLEConstants.OlePatron.PATRON_VIEW_BILL_URL + patronBo.getOlePatronId());
                        }
                    }
                    if (isResultReturnable(object)) {
                        form.setAtLeastOneRowReturnable(true);
                    }
                }
                finalResult = (List<OlePatronDocument>)displayList;
        }
        catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
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
        Map<String, String> primaryKeyValues = super.getPropertyKeyValuesFromDataObject(pkNames, dataObject);
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
     * This method will return url for view link in patron record.
     *
     * @return Url
     */

    public String getUrl() {
        if (searchResults != null && searchResults.size() > 0) {
            List<OlePatronDocument> olePatronDocuments = (List<OlePatronDocument>) searchResults;
            int pos = 0;
            OlePatronDocument document = (OlePatronDocument) searchResults.get(0);
            if (!document.isStartingIndexExecuted()) {
                document.setPointing(true);
                document.setStartingIndexExecuted(true);
            }
            OlePatronDocument patronDocument = null;
            for (OlePatronDocument olePatronDocument : olePatronDocuments) {
                if (olePatronDocument.isPointing()) {
                    if (olePatronDocument.isPatronBillFlag()) {
                        patronDocument = olePatronDocument;
                        break;
                    } else {
                        break;
                    }
                }
                pos++;
            }
            if (patronDocument != null) {
                return "patronbill?viewId=BillView&amp;methodToCall=start&amp;patronId=" + patronDocument.getOlePatronId();
            } else {
                return null;
            }

        }
        return null;
    }

    /**
     * This method will return view bill  link name for create bill.
     *
     * @return link name
     */
    public String getFileName() {
        if (searchResults != null && searchResults.size() > 0) {
            List<OlePatronDocument> olePatronDocuments = (List<OlePatronDocument>) searchResults;
            int pos = 0;
            OlePatronDocument patronDocument = null;
            for (OlePatronDocument olePatronDocument : olePatronDocuments) {
                if (olePatronDocument.isPointing()) {
                    olePatronDocument.setPointing(false);
                    if (searchResults.size() - 1 > pos) {
                        OlePatronDocument nextOlePatronDocument = (OlePatronDocument) searchResults.get(pos + 1);
                        nextOlePatronDocument.setPointing(true);
                    }
                    if (olePatronDocument.isPatronBillFlag()) {
                        patronDocument = olePatronDocument;
                        break;
                    } else {
                        break;
                    }

                }
                pos++;
            }
            if (patronDocument != null) {
                return "patronbill";
            } else {
                return null;
            }
        }
        return null;

    }

    /**
     * This method will return the url for create bill link in patron record.
     *
     * @return link url
     */

    public String getPatronBillUrl(String patronId, String firstName, String lastName) {
        String url =  OLEConstants.OlePatron.PATRON_CREATE_BILL_URL+ patronId + "&firstName=" + firstName + "&lastName=" + lastName;
        return url;
    }

    public boolean isWildCardMatches(String word, String wildCardString) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Applying WildCard Search");
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
        if (LOG.isDebugEnabled()) {
            LOG.debug("Validating the Phone Number  Format - ##########, (###)###-#### , ###-###-#### , ### ###-#### , ### ### ####");
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
