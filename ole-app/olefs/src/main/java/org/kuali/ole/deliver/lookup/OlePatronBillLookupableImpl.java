package org.kuali.ole.deliver.lookup;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ojb.broker.query.Criteria;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.FeeType;
import org.kuali.ole.deliver.bo.OLEPatronEntityViewBo;
import org.kuali.ole.deliver.bo.PatronBillPayment;
import org.kuali.ole.deliver.service.OleLoanDocumentDaoOjb;
import org.kuali.ole.service.OlePatronHelperService;
import org.kuali.ole.service.OlePatronHelperServiceImpl;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.kim.impl.identity.name.EntityNameBo;
import org.kuali.rice.krad.lookup.LookupUtils;
import org.kuali.rice.krad.lookup.LookupableImpl;
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
    private OleLoanDocumentDaoOjb oleLoanDocumentDaoOjb;

    public OleLoanDocumentDaoOjb getOleLoanDocumentDaoOjb() {
        if(oleLoanDocumentDaoOjb == null){
            oleLoanDocumentDaoOjb = (OleLoanDocumentDaoOjb) SpringContext.getBean("oleLoanDao");
        }
        return oleLoanDocumentDaoOjb;
    }

    public void setOleLoanDocumentDaoOjb(OleLoanDocumentDaoOjb oleLoanDocumentDaoOjb) {
        this.oleLoanDocumentDaoOjb = oleLoanDocumentDaoOjb;
    }

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
        List<PatronBillPayment> patronBillPayments = new ArrayList<>();
        List<PatronBillPayment> finalResult = new ArrayList<>();
        LookupUtils.preprocessDateFields(searchCriteria);

        Map<String, String> searchPatronCriteria = processSearchPatronCriteria(searchCriteria);

        Criteria searchFeeTypeCriteria = processSearchFeeTypeCriteria(searchCriteria);

        List<?> searchResults;
        boolean patronFlag = searchPatronCriteria.size() > 0 && !(searchPatronCriteria.size() == 1 && searchPatronCriteria.containsKey(OLEConstants.PTRN_ID));
        boolean feeFlag = !searchFeeTypeCriteria.isEmpty();
        if (patronFlag || feeFlag) {
            Iterator iterator;

            Set<String> olePatronIdList = new HashSet<>();
            Set<String> billNumberList = new HashSet<>();
            if (feeFlag) {
                List<FeeType> feeTypes = getOleLoanDocumentDaoOjb().getFeeTypes(searchFeeTypeCriteria);
                if (CollectionUtils.isNotEmpty(feeTypes)){
                    for (FeeType feeType : feeTypes){
                        String patronId = feeType != null && feeType.getPatronBillPayment() != null ? feeType.getPatronBillPayment().getPatronId() : null;
                        if (StringUtils.isNotBlank(patronId)) {
                            olePatronIdList.add(patronId);
                        }
                        String billNumber = feeType != null && feeType.getPatronBillPayment() != null ? feeType.getPatronBillPayment().getBillNumber() : null;
                        if (StringUtils.isNotBlank(billNumber)){
                            billNumberList.add(billNumber);
                        }
                    }
                }
            }

            if (!searchPatronCriteria.isEmpty() && !(feeFlag && CollectionUtils.isEmpty(olePatronIdList))) {
                Set<String> patronIdList = new HashSet<>();
                Integer searchResultsLimit = LookupUtils.getSearchResultsLimit(getDataObjectClass(), form);
                searchResults = (List<?>) getLookupService().findCollectionBySearchHelper(OLEPatronEntityViewBo.class,
                        searchPatronCriteria, !bounded, searchResultsLimit);
                iterator = searchResults.iterator();
                while (iterator.hasNext()) {
                    OLEPatronEntityViewBo olePatronEntityViewBo = (OLEPatronEntityViewBo) iterator.next();
                    String patronId = olePatronEntityViewBo.getPatronId();
                    if (StringUtils.isNotBlank(patronId)) {
                        if (feeFlag) {
                            if (olePatronIdList.contains(patronId)) {
                                patronIdList.add(patronId);
                            }
                        } else {
                            patronIdList.add(patronId);
                        }
                    }
                }
                olePatronIdList = patronIdList;
            }

            if (CollectionUtils.isNotEmpty(olePatronIdList)) {
                Map map = new HashMap<>();
                map.put(OLEConstants.PTRN_ID, olePatronIdList);
                if (CollectionUtils.isNotEmpty(billNumberList)){
                    map.put(OLEConstants.OlePatron.BILL_PAYMENT_ID, billNumberList);
                }
                patronBillPayments = (List<PatronBillPayment>) getBusinessObjectService().findMatching(PatronBillPayment.class, map);
            }
            patronBillPayments = processBillPayments(form, bounded, patronBillPayments);
        } else {
            patronBillPayments = (List<PatronBillPayment>) super.performSearch(form, searchCriteria, bounded);
        }

        List<PatronBillPayment> sortedPatronBillPayments = new ArrayList<>();
        Iterator iterator = patronBillPayments.iterator();
        while (iterator.hasNext()){
            PatronBillPayment patronBillPayment=(PatronBillPayment)iterator.next();
            Map<String, String> map = new HashMap<>();
            map.put(OLEConstants.ENTITY_ID, patronBillPayment.getPatronId());
            EntityNameBo entityNameBo=getBusinessObjectService().findByPrimaryKey(EntityNameBo.class,map);
            if(entityNameBo!=null){
                patronBillPayment.setFirstName(entityNameBo.getFirstName());
                patronBillPayment.setLastName(entityNameBo.getLastName());
            }
            finalResult.add(patronBillPayment);
        }
        sortedPatronBillPayments.addAll(finalResult);
        Collections.sort(sortedPatronBillPayments, new Comparator<PatronBillPayment>() {
            @Override
            public int compare(PatronBillPayment patronBillPayment1, PatronBillPayment patronBillPayment2) {
                return Integer.parseInt(patronBillPayment1.getBillNumber()) > Integer.parseInt(patronBillPayment2.getBillNumber()) ? 1 : -1;
            }
        });
        return sortedPatronBillPayments;
    }

    private Criteria processSearchFeeTypeCriteria(Map<String, String> searchCriteria) {
        String feeTypeId = searchCriteria.get(OLEConstants.OleFeeType.FEE_TYPE_ID);
        String itemBarcode = searchCriteria.get(OLEConstants.ITEM_BARCODE);
        String paymentStatusId = searchCriteria.get(OLEConstants.PAY_STATUS_ID);
        String fineAmountFrom = searchCriteria.get(OLEConstants.FINE_AMOUNT_FROM);
        String fineAmountTo = searchCriteria.get(OLEConstants.FINE_AMOUNT_TO);

        searchCriteria.remove(OLEConstants.OleFeeType.FEE_TYPE_ID);
        searchCriteria.remove(OLEConstants.ITEM_BARCODE);
        searchCriteria.remove(OLEConstants.PAY_STATUS_ID);
        searchCriteria.remove(OLEConstants.FINE_AMOUNT);

        Criteria criteria = new Criteria();
        if (StringUtils.isNotBlank(feeTypeId)) {
            criteria.addEqualTo(OLEConstants.FEE_TYPE_FIELD, feeTypeId);
        }
        if (StringUtils.isNotBlank(itemBarcode)) {
            itemBarcode = itemBarcode.replace("*", "%");
            criteria.addLike(OLEConstants.ITEM_BARCODE, itemBarcode);
        }
        if (StringUtils.isNotBlank(paymentStatusId)) {
            criteria.addEqualTo(OLEConstants.PAY_STATUS, paymentStatusId);
        }
        if (StringUtils.isNotBlank(fineAmountFrom)){
            criteria.addGreaterOrEqualThan("patronBillPayment.unPaidBalance", fineAmountFrom);
        }
        if (StringUtils.isNotBlank(fineAmountTo)){
            criteria.addLessOrEqualThan("patronBillPayment.unPaidBalance", fineAmountTo);
        }
        return criteria;
    }

    private Map<String, String> processSearchPatronCriteria(Map<String, String> searchCriteria) {
        Map<String, String> searchPatronMap = new HashMap<>();
        String patronBarcode = searchCriteria.get(OLEConstants.PATRON_BAR);
        String firstName = searchCriteria.get(OLEConstants.OlePatron.PATRON_FIRST_NAME);
        String middleName = searchCriteria.get(OLEConstants.OlePatron.PATRON_MIDDLE_NAME);
        String lastName = searchCriteria.get(OLEConstants.OlePatron.PATRON_LAST_NAME);
        String patronTypeId = searchCriteria.get(OLEConstants.PTRN_TYPE_ID);
        String patronId = searchCriteria.get(OLEConstants.PTRN_ID);

        searchCriteria.remove(OLEConstants.PATRON_BAR);
        searchCriteria.remove(OLEConstants.OlePatron.PATRON_FIRST_NAME);
        searchCriteria.remove(OLEConstants.OlePatron.PATRON_MIDDLE_NAME);
        searchCriteria.remove(OLEConstants.OlePatron.PATRON_LAST_NAME);
        searchCriteria.remove(OLEConstants.PTRN_TYPE_ID);

        if (StringUtils.isNotBlank(patronBarcode)) {
            searchPatronMap.put(OLEConstants.PATRON_BAR, patronBarcode);
        }
        if (StringUtils.isNotBlank(firstName)) {
            searchPatronMap.put(OLEConstants.OlePatron.PATRON_FIRST_NAME, firstName);
        }
        if (StringUtils.isNotBlank(middleName)) {
            searchPatronMap.put(OLEConstants.OlePatron.PATRON_MIDDLE_NAME, middleName);
        }
        if (StringUtils.isNotBlank(lastName)) {
            searchPatronMap.put(OLEConstants.OlePatron.PATRON_LAST_NAME, lastName);
        }
        if (StringUtils.isNotBlank(patronTypeId)) {
            searchPatronMap.put(OLEConstants.PTRN_TYPE_ID, patronTypeId);
        }
        if (StringUtils.isNotBlank(patronId)) {
            searchPatronMap.put(OLEConstants.PTRN_ID, patronId);
        }
        Map<String, String> searchPatronCriteria = new HashMap<>();
        if (searchPatronMap.size() > 0) {
            for (Map.Entry<String, String> entry : searchPatronMap.entrySet()) {
                int counter = 0;
                for (int i = 0; i < entry.getValue().length(); i++) {
                    if (entry.getValue().charAt(i) == '*') {
                        counter++;
                    }
                }
                if (counter == 0 || (counter != entry.getValue().length())) {
                    searchPatronCriteria.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return searchPatronCriteria;
    }

    private List<PatronBillPayment> processBillPayments(LookupForm form, boolean bounded, List<PatronBillPayment> patronBillPayments) {
        List<PatronBillPayment> billPayments = new ArrayList<>();
        try {
            Class entityPatronBoClass = Class.forName("org.kuali.ole.deliver.bo.PatronBillPayment");
            Integer searchResultsLimit = null;
            if (bounded) {
                searchResultsLimit = LookupUtils.getSearchResultsLimit(entityPatronBoClass, form);
            }
            int count = 0;
            if (searchResultsLimit != null) {
                Iterator iterator = patronBillPayments.iterator();
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
                Boolean resultsExceedsLimit = bounded && searchResultsLimit != null && patronBillPayments.size() > 0 && patronBillPayments.size() > searchResultsLimit ? true : false;
                if (resultsExceedsLimit) {
                    GlobalVariables.getMessageMap().putInfoForSectionId(resultsPropertyName,
                            RiceKeyConstants.INFO_LOOKUP_RESULTS_EXCEEDS_LIMIT, patronBillPayments.size() + "",
                            searchResultsLimit.toString());
                } else {

                    GlobalVariables.getMessageMap().putInfoForSectionId(resultsPropertyName,
                            RiceKeyConstants.INFO_LOOKUP_RESULTS_DISPLAY_ALL, billPayments.size() + "");
                }
            }
        } catch (Exception e) {
            LOG.error("Exception Occurred In Searching of Patrons------>Patron Bill Lookup");
        }
        return billPayments;
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
