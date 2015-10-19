package org.kuali.ole.loaders.deliver.service.impl;

import com.sun.jersey.api.core.HttpContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.constant.OLEPatronConstant;
import org.kuali.ole.deliver.service.OLEDeliverService;
import org.kuali.ole.ingest.pojo.*;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;
import org.kuali.ole.loaders.common.constants.OLELoaderConstants;
import org.kuali.ole.loaders.common.service.OLELoaderRestClient;
import org.kuali.ole.loaders.common.service.OLELoaderService;
import org.kuali.ole.loaders.common.service.impl.OLELoaderServiceImpl;
import org.kuali.ole.loaders.deliver.service.OLEPatronLoaderHelperService;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.kim.impl.identity.address.EntityAddressBo;
import org.kuali.rice.kim.impl.identity.affiliation.EntityAffiliationBo;
import org.kuali.rice.kim.impl.identity.email.EntityEmailBo;
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentBo;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.*;

/**
 * Created by sheiksalahudeenm on 27/3/15.
 */
public class OLEPatronLoaderHelperServiceImpl implements OLEPatronLoaderHelperService {

    private static final Logger LOG = Logger.getLogger(OLEPatronLoaderHelperServiceImpl.class);
    private BusinessObjectService businessObjectService;
    private OLELoaderService oleLoaderService;


    public BusinessObjectService getBusinessObjectService() {
        if(businessObjectService == null){
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public OLELoaderService getOleLoaderService() {
        if(oleLoaderService == null){
            oleLoaderService = new OLELoaderServiceImpl();
        }
        return oleLoaderService;
    }

    public void setOleLoaderService(OLELoaderService oleLoaderService) {
        this.oleLoaderService = oleLoaderService;
    }

    @Override
    public OlePatronDocument getPatronById(String patronId) {
        Map patronMap = new HashMap();
        patronMap.put("olePatronId", patronId);
        return getBusinessObjectService().findByPrimaryKey(OlePatronDocument.class, patronMap);
    }

    @Override
    public OlePatronDocument getPatronByCode(String patronCode) {
        Map patronMap = new HashMap();
        patronMap.put("patronCode", patronCode);
        return getBusinessObjectService().findByPrimaryKey(OlePatronDocument.class, patronMap);
    }

    @Override
    public List<OlePatronDocument> getAllPatrons() {
        return (List<OlePatronDocument>) getBusinessObjectService().findAll(OlePatronDocument.class);
    }

    @Override
    public Object formPatronExportResponse(Object object, String patronContext, String uri, boolean addContext) {
        OlePatronDocument olePatron = (OlePatronDocument) object;
        JSONObject jsonObject = new JSONObject();
        try {
            if(addContext)
                jsonObject.put("@context",patronContext);
            jsonObject.put("@id", OLELoaderConstants.PATRON_URI + OLELoaderConstants.SLASH + olePatron.getOlePatronId());
            jsonObject.put("borrowerType",olePatron.getBorrowerTypeCode());
            jsonObject.put("barcode",olePatron.getBarcode());
            jsonObject.put("activationDate",olePatron.getActivationDate());
            jsonObject.put("expirationDate",olePatron.getExpirationDate());
            jsonObject.put("active",olePatron.isActiveIndicator());
            jsonObject.put("statisticalCategory",olePatron.getStatisticalCategory());
            JSONObject patronLevelPolicyJsonObject = new JSONObject();
            patronLevelPolicyJsonObject.put("receivesCourtesyNotice",olePatron.isCourtesyNotice());
            patronLevelPolicyJsonObject.put("deliveryPrivilege",olePatron.isDeliveryPrivilege());
            patronLevelPolicyJsonObject.put("generalBlock",olePatron.isGeneralBlock());
            patronLevelPolicyJsonObject.put("blockNotes",olePatron.getGeneralBlockNotes());
            patronLevelPolicyJsonObject.put("pagingPrivilege",olePatron.isPagingPrivilege());
            jsonObject.put("patronLevelPolicies",patronLevelPolicyJsonObject);
            jsonObject = formPatronName(jsonObject,olePatron);
            jsonObject = populateOlePatronPhone(jsonObject, olePatron);
            jsonObject = populateOlePatronEmail(jsonObject, olePatron);
            jsonObject = populateOlePatronAddress(jsonObject, olePatron);
            jsonObject = populateOlePatronAffiliations(jsonObject, olePatron);
            jsonObject = populateOlePatronNotes(jsonObject, olePatron);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public Object formAllPatronExportResponse(HttpContext context, List<OlePatronDocument> olePatronList, String patronContext, String uri) {
        JSONObject jsonResponseObject = new JSONObject();
        JSONArray paginationArray = new JSONArray();
        JSONObject paginationObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try{
            jsonResponseObject.put("@context",patronContext);
            int startIndex = 0;
            int maxResults = 50;
            boolean validStartIndex = true;
            if(context.getRequest().getQueryParameters().containsKey("start")){
                try{
                    String start = context.getRequest().getQueryParameters().getFirst("start");
                    startIndex = Integer.parseInt(start);
                    if(startIndex < 0)
                        startIndex =0;
                    if(startIndex > olePatronList.size()){
                        validStartIndex = false;
                    }
                }catch (Exception e){
                    LOG.info("Invalid Start Index : " + e.getMessage());
                    startIndex = 0;
                }
            }
            if(context.getRequest().getQueryParameters().containsKey("maxResults")){
                try{
                    String maxCount = context.getRequest().getQueryParameters().getFirst("maxResults");
                    maxResults = Integer.parseInt(maxCount);
                    if(maxResults < 0)
                        maxResults =50;
                }catch (Exception e){
                    LOG.info("Invalid Max Result count : " + e.getMessage());
                    maxResults = 50;
                }
            }
            int loopIterationEnd = 0;
            if(startIndex+maxResults > olePatronList.size())
                loopIterationEnd = olePatronList.size();
            else{
                loopIterationEnd = startIndex+maxResults;
            }

            if(validStartIndex){
                if(startIndex != 0){
                    paginationObject.put("rel","prev");
                    paginationObject.put("href",OLELoaderConstants.PATRON_URI + "?start="+((startIndex-1)-maxResults < 0 ? 0 : (startIndex-1)-maxResults)+"&maxResults="+maxResults);
                    paginationArray.put(paginationObject);
                }
                if(loopIterationEnd != olePatronList.size()){
                    paginationObject = new JSONObject();
                    paginationObject.put("rel","next");
                    paginationObject.put("href",OLELoaderConstants.PATRON_URI + "?start="+(loopIterationEnd+1)+"&maxResults="+maxResults);
                    paginationArray.put(paginationObject);
                }

                jsonResponseObject.put("links",paginationArray);
                for(int index = (startIndex == 0 ? 0 : startIndex-1) ; index < loopIterationEnd-1 ; index++){
                    OlePatronDocument olePatron = olePatronList.get(index);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("@id", OLELoaderConstants.PATRON_URI + OLELoaderConstants.SLASH + olePatron.getOlePatronId());
                        jsonObject.put("borrowerType",olePatron.getBorrowerTypeCode());
                        jsonObject.put("barcode",olePatron.getBarcode());
                        jsonObject.put("activationDate",olePatron.getActivationDate());
                        jsonObject.put("expirationDate",olePatron.getExpirationDate());
                        jsonObject.put("active",olePatron.isActiveIndicator());
                        jsonObject.put("statisticalCategory",olePatron.getStatisticalCategory());
                        JSONObject patronLevelPolicyJsonObject = new JSONObject();
                        patronLevelPolicyJsonObject.put("receivesCourtesyNotice",olePatron.isCourtesyNotice());
                        patronLevelPolicyJsonObject.put("deliveryPrivilege",olePatron.isDeliveryPrivilege());
                        patronLevelPolicyJsonObject.put("generalBlock",olePatron.isGeneralBlock());
                        patronLevelPolicyJsonObject.put("blockNotes",olePatron.getGeneralBlockNotes());
                        patronLevelPolicyJsonObject.put("pagingPrivilege",olePatron.isPagingPrivilege());
                        jsonObject.put("patronLevelPolicies",patronLevelPolicyJsonObject);
                        jsonObject = formPatronName(jsonObject,olePatron);
                        jsonObject = populateOlePatronPhone(jsonObject, olePatron);
                        jsonObject = populateOlePatronEmail(jsonObject, olePatron);
                        jsonObject = populateOlePatronAddress(jsonObject, olePatron);
                        jsonObject = populateOlePatronAffiliations(jsonObject, olePatron);
                        jsonObject = populateOlePatronNotes(jsonObject, olePatron);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);
                }
                jsonResponseObject.put("items",jsonArray);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonResponseObject;
    }

   /* @Override
    public OLELoaderResponseBo updateOlePatronDocument(OlePatronDocument olePatron, OLEPatronBo olePatronBo, HttpContext context) {
        if(StringUtils.isNotBlank(olePatronBo.getPatronName()))
            olePatron.setPatronName(olePatronBo.getPatronName());
        if(StringUtils.isNotBlank(olePatronBo.getPatronDescription()))
            olePatron.setPatronDescription(olePatronBo.getPatronDescription());
        olePatron.setActive(olePatronBo.isActive());
        try{
            olePatron = KRADServiceLocator.getBusinessObjectService().save(olePatron);
            String details = formOlePatronDocumentExportResponse(olePatron, OLELoaderConstants.OLELoaderContext.PATRON,
                    context.getRequest().getRequestUri().toASCIIString(), true).toString();
            return getOleLoaderService().generateResponse(
                    OLELoaderConstants.OLEloaderMessage.PATRON_SUCCESS,
                    OLELoaderConstants.OLEloaderStatus.PATRON_SUCCESS,details);
        }catch(Exception e){
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.PATRON_FAILED, OLELoaderConstants.OLEloaderStatus.PATRON_FAILED);
        }

    }*/

    private JSONObject formPatronName(JSONObject patronJsonObject,OlePatronDocument olePatronDocument){
        JSONObject nameJsonObject = new JSONObject();
        try{
            olePatronDocument = OLEDeliverService.populatePatronName(olePatronDocument);
            nameJsonObject.put("firstName", olePatronDocument.getFirstName());
            nameJsonObject.put("surName", olePatronDocument.getLastName());
            nameJsonObject.put("suffix", olePatronDocument.getNameSuffix());
            nameJsonObject.put("prefix", olePatronDocument.getNamePrefix());
            patronJsonObject.put("name",nameJsonObject);
        }catch (Exception e){
            LOG.error("Error while process name object");
        }
        return patronJsonObject;
    }

    private JSONObject populateOlePatronPhone(JSONObject patronJsonObject, OlePatronDocument olePatronDocument) {

        List<OlePhoneBo> olePhoneBoList = olePatronDocument.getOlePhones();
        EntityBo entity = olePatronDocument.getEntity();
        JSONArray phoneJsonArray = new JSONArray();
        try{
            if (!entity.getEntityTypeContactInfos().isEmpty()) {
                List<EntityPhoneBo> entityPhoneBoList = entity.getEntityTypeContactInfos().get(0).getPhoneNumbers();
                for(EntityPhoneBo entityPhoneBo : entityPhoneBoList){
                    JSONObject phoneJsonObject = new JSONObject();
                    phoneJsonObject.put("phoneType",entityPhoneBo.getPhoneTypeCode());
                    phoneJsonObject.put("phoneNumber", entityPhoneBo.getPhoneNumber());
                    phoneJsonObject.put("country", entityPhoneBo.getCountryCode());
                    phoneJsonObject.put("default", entityPhoneBo.isDefaultValue());
                    phoneJsonObject.put("extensionNumber", entityPhoneBo.getExtensionNumber());
                    phoneJsonObject.put("active", entityPhoneBo.isActive());
                    for (OlePhoneBo olePhone : olePhoneBoList) {
                        if (olePhone.getId().equalsIgnoreCase(entityPhoneBo.getId())) {
                            String phoneSourceId = olePhone.getPhoneSource();
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("oleAddressSourceId", phoneSourceId);
                            List<OleAddressSourceBo> phoneSourceList = (List<OleAddressSourceBo>) getBusinessObjectService().findMatching(OleAddressSourceBo.class, map);
                            if (CollectionUtils.isNotEmpty(phoneSourceList)) {
                                patronJsonObject.put("phoneSource", phoneSourceList.get(0).getOleAddressSourceCode());
                            }
                            break;
                        }
                    }
                    phoneJsonArray.put(phoneJsonObject);
                }
            }
            patronJsonObject.put("phone",phoneJsonArray);
        }catch(Exception e){
            LOG.error("Error while process phone object");
        }

        return patronJsonObject;
    }

    private JSONObject populateOlePatronEmail(JSONObject patronJsonObject, OlePatronDocument olePatronDocument) {

        List<OleEmailBo> oleEmailBoList = olePatronDocument.getOleEmails();
        EntityBo entity = olePatronDocument.getEntity();
        JSONArray emailJsonArray = new JSONArray();
        try{
            if (!entity.getEntityTypeContactInfos().isEmpty()) {
                List<EntityEmailBo> entityEmailList = entity.getEntityTypeContactInfos().get(0).getEmailAddresses();
                for(EntityEmailBo entityEmailBo : entityEmailList){
                    JSONObject emailJsonObject = new JSONObject();
                    emailJsonObject.put("emailAddressType",entityEmailBo.getEmailTypeCode());
                    emailJsonObject.put("emailAddress", entityEmailBo.getEmailAddress());
                    emailJsonObject.put("active", entityEmailBo.isActive());
                    emailJsonObject.put("default", entityEmailBo.isDefaultValue());
                    for(OleEmailBo oleEmail : oleEmailBoList) {
                        if(oleEmail.getId().equalsIgnoreCase(entityEmailBo.getId())) {
                            String emailSourceId = oleEmail.getEmailSource();
                            HashMap<String, String> map = new HashMap<>();
                            map.put("oleAddressSourceId", emailSourceId);
                            List<OleAddressSourceBo> emailSourceList = (List<OleAddressSourceBo>) getBusinessObjectService().findMatching(OleAddressSourceBo.class, map);
                            if(CollectionUtils.isNotEmpty(emailSourceList)) {
                                patronJsonObject.put("emailSource", emailSourceList.get(0).getOleAddressSourceCode());
                            }
                            break;
                        }
                    }
                    emailJsonArray.put(emailJsonObject);
                }
                patronJsonObject.put("email",emailJsonArray);
            }
        }catch(Exception e){
            LOG.error("Error while process email object");
        }
        return patronJsonObject;
    }

    private JSONObject populateOlePatronAddress(JSONObject patronJsonObject, OlePatronDocument olePatronDocument) {
        List<OleAddressBo> oleAddressBoList = olePatronDocument.getOleAddresses();
        EntityBo entity = olePatronDocument.getEntity();
        JSONArray addressJsonArray = new JSONArray();
        try{
            if (!entity.getEntityTypeContactInfos().isEmpty()) {
                List<EntityAddressBo> entityAddressList = entity.getEntityTypeContactInfos().get(0).getAddresses();
                for (EntityAddressBo entityAdd : entityAddressList) {
                    JSONObject addressJsonObject = new JSONObject();
                    addressJsonObject.put("line1",entityAdd.getLine1());
                    addressJsonObject.put("line2", entityAdd.getLine2());
                    addressJsonObject.put("line3", entityAdd.getLine3());
                    addressJsonObject.put("city", entityAdd.getCity());
                    addressJsonObject.put("active", entityAdd.isActive());
                    addressJsonObject.put("country", entityAdd.getCountryCode());
                    addressJsonObject.put("default", entityAdd.isDefaultValue());
                    addressJsonObject.put("postalCode", entityAdd.getPostalCode());
                    addressJsonObject.put("addressTypeCode",entityAdd.getAddressTypeCode());
                    addressJsonObject.put("stateProvinceCode", entityAdd.getStateProvinceCode());
                    for(OleAddressBo oleAddress : oleAddressBoList){
                        if(oleAddress.getId().equalsIgnoreCase(entityAdd.getId())) {
                            addressJsonObject.put("validFrom", oleAddress.getAddressValidFrom());
                            addressJsonObject.put("validTo", oleAddress.getAddressValidTo());
                            addressJsonObject.put("addressVerified",oleAddress.isAddressVerified());
                            String addressSourceId =oleAddress.getAddressSource();
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("oleAddressSourceId", addressSourceId);
                            List<OleAddressSourceBo> addressSourceList = (List<OleAddressSourceBo>) getBusinessObjectService().findMatching(OleAddressSourceBo.class, map);
                            if (CollectionUtils.isNotEmpty(addressSourceList)) {
                                addressJsonObject.put("addressSource",addressSourceList.get(0).getOleAddressSourceCode());
                            }
                            break;
                        }
                    }
                    addressJsonArray.put(addressJsonObject);
                }
                patronJsonObject.put("address",addressJsonArray);
            }
        }catch(Exception e){
            LOG.error("Error while process address object");
        }

        return patronJsonObject;
    }

    private JSONObject populateOlePatronAffiliations(JSONObject patronJsonObject, OlePatronDocument olePatronDocument) {
        EntityBo entity = olePatronDocument.getEntity();
        JSONArray affiliationJsonArray = new JSONArray();
        try{
            if (CollectionUtils.isNotEmpty(entity.getEmploymentInformation())) {
                for(EntityEmploymentBo entityEmploymentBo : entity.getEmploymentInformation()){
                    JSONObject affiliationJsonObject = new JSONObject();
                    JSONObject employmentJsonObject = new JSONObject();
                    employmentJsonObject.put("employeeTypeCode",entityEmploymentBo.getEmployeeTypeCode());
                    employmentJsonObject.put("primary", entityEmploymentBo.isPrimary());
                    employmentJsonObject.put("active", entityEmploymentBo.isActive());
                    employmentJsonObject.put("salaryAmount", entityEmploymentBo.getBaseSalaryAmount());
                    employmentJsonObject.put("employeeStatusCode", entityEmploymentBo.getEmployeeStatusCode());
                    employmentJsonObject.put("employeeId", entityEmploymentBo.getEmployeeId());
                    employmentJsonObject.put("primaryDepartmentCode", entityEmploymentBo.getPrimaryDepartmentCode());
                    EntityAffiliationBo entityAffiliationBo = entityEmploymentBo.getEntityAffiliation();
                    affiliationJsonObject.put("campusCode", entityAffiliationBo.getCampusCode());
                    affiliationJsonObject.put("active", entityAffiliationBo.isActive());
                    affiliationJsonObject.put("affiliationType", entityAffiliationBo.getAffiliationTypeCode());
                    affiliationJsonObject.put("employment", employmentJsonObject);
                    affiliationJsonArray.put(affiliationJsonObject);
                }
                patronJsonObject.put("affiliation",affiliationJsonArray);
            }
        }catch(Exception e){
            LOG.error("Error while process affiliation object");
        }
        return patronJsonObject;
    }

    private JSONObject populateOlePatronNotes(JSONObject patronJsonObject, OlePatronDocument olePatronDocument) {
        JSONArray noteJsonArray = new JSONArray();
        try{
            if(CollectionUtils.isNotEmpty(olePatronDocument.getNotes())){
                for(OlePatronNotes olePatronNotes : olePatronDocument.getNotes()){
                    JSONObject noteJsonObject = new JSONObject();
                    noteJsonObject.put("noteType", olePatronNotes.getOlePatronNoteType().getPatronNoteTypeCode());
                    noteJsonObject.put("note", olePatronNotes.getPatronNoteText());
                    noteJsonObject.put("active", olePatronNotes.isActive());
                    noteJsonArray.put(noteJsonObject);
                }
            }
            patronJsonObject.put("note",noteJsonArray);
        }catch(Exception e){
            LOG.error("Error while process note object");
        }
        return patronJsonObject;
    }

    @Override
    public List<OlePatron> formIngestOlePatrons(JSONArray patronJsonArray,Map<String,Integer> rejectedPatronBarcodeIndexMap, Map<String,Integer> selectedPatronBarcodeIndexMap) {
        List<OlePatron> olePatrons = new ArrayList<>();
        for (int index = 0; index < patronJsonArray.length(); index ++) {
            JSONObject jsonObject = null;
            OlePatron patron = new OlePatron();
            try {
                String barcode = "";
                jsonObject = (JSONObject)patronJsonArray.get(index);
                if(jsonObject == null){
                    rejectedPatronBarcodeIndexMap.put("InvalidPatron : " + index,index);
                    continue;
                }
                if(jsonObject.has("barcode")){
                    barcode = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"barcode");
                    if(StringUtils.isNotBlank(barcode)){
                        patron.setBarcode(barcode);
                    }else{
                        rejectedPatronBarcodeIndexMap.put("InvalidPatron : " + index,index);
                        continue;
                    }
                }else{
                    rejectedPatronBarcodeIndexMap.put("InvalidPatron : " + index, index);
                    continue;
                }
                if(jsonObject.has("@id")) {
                    String patronIdUrl = getOleLoaderService().getStringValueFromJsonObject(jsonObject, "@id");
                    if (StringUtils.isNotBlank(patronIdUrl)) {
                            Map<String, Object> restResponseMap = OLELoaderRestClient.jerseryClientGet(patronIdUrl);
                            if ((Integer) restResponseMap.get("status") != 200) {
                                String patronId = patronIdUrl.substring(patronIdUrl.indexOf("/api/patron/") + 12);
                                patron.setPatronID(patronId);
                            } else{
                                rejectedPatronBarcodeIndexMap.put("InvalidPatron : " + index, index);
                                continue;
                            }
                        }
                }
                if(jsonObject.has("activationDate")){
                    String activationDate = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"activationDate");
                    if(StringUtils.isNotBlank(activationDate)){
                        try{
                            Date date = OLELoaderConstants.DATE_FORMAT.parse(activationDate);
                            patron.setActivationDate(new java.sql.Date(date.getTime()));
                        }catch(Exception e){
                            e.printStackTrace();
                            rejectedPatronBarcodeIndexMap.put("InvalidPatron : " + index, index);
                            continue;
                        }
                    }
                }
                if(jsonObject.has("expirationDate")){
                    String expirationDate = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"expirationDate");
                    if(StringUtils.isNotBlank(expirationDate)){
                        try{
                            Date date = OLELoaderConstants.DATE_FORMAT.parse(expirationDate);
                            patron.setExpirationDate(new java.sql.Date(date.getTime()));
                        }catch(Exception e){
                            e.printStackTrace();
                            rejectedPatronBarcodeIndexMap.put("InvalidPatron : " + index, index);
                            continue;
                        }
                    }
                }

                if(jsonObject.has("active")){
                    try{
                        boolean active = Boolean.parseBoolean(getOleLoaderService().getStringValueFromJsonObject(jsonObject, "active"));
                        patron.setActive(active);
                    }catch(Exception e){
                        e.printStackTrace();
                        rejectedPatronBarcodeIndexMap.put("InvalidPatron : " + index, index);
                        continue;
                    }

                }

                if(jsonObject.has("borrowerType")){
                    String borrowerType = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"borrowerType");
                    if(StringUtils.isNotBlank(borrowerType)){
                        patron.setBorrowerType(borrowerType);
                    }
                }

                if(jsonObject.has("patronLevelPolicies")){
                    JSONObject patronLevelPolicies = getOleLoaderService().getJsonObjectValueFromJsonObject(jsonObject, "patronLevelPolicies");
                    OlePatronLevelPolicies olePatronLevelPolicies = new OlePatronLevelPolicies();
                    if(patronLevelPolicies.has("receivesCourtesyNotice")){
                        olePatronLevelPolicies.setReceivesCourtesyNotice(Boolean.parseBoolean(getOleLoaderService().getStringValueFromJsonObject(patronLevelPolicies, "receivesCourtesyNotice")));
                    }
                    if(patronLevelPolicies.has("deliveryPrivilege")){
                        olePatronLevelPolicies.setHasDeliveryPrivilege(Boolean.parseBoolean(getOleLoaderService().getStringValueFromJsonObject(patronLevelPolicies, "deliveryPrivilege")));
                    }
                    if(patronLevelPolicies.has("generalBlock")){
                        olePatronLevelPolicies.setGenerallyBlocked(Boolean.parseBoolean(getOleLoaderService().getStringValueFromJsonObject(patronLevelPolicies, "generalBlock")));
                    }
                    if(patronLevelPolicies.has("pagingPrivilege")){
                        olePatronLevelPolicies.setHasPagingPrivilege(Boolean.parseBoolean(getOleLoaderService().getStringValueFromJsonObject(patronLevelPolicies, "pagingPrivilege")));
                    }
                    if(patronLevelPolicies.has("generalBlockNotes")){
                        olePatronLevelPolicies.setGeneralBlockNotes(getOleLoaderService().getStringValueFromJsonObject(patronLevelPolicies, "generalBlockNotes"));
                    }
                    patron.setPatronLevelPolicies(olePatronLevelPolicies);
                }
                if(jsonObject.has("name")){
                    JSONObject nameJsonObject = getOleLoaderService().getJsonObjectValueFromJsonObject(jsonObject, "name");
                    OleNameTypes oleNameTypes = new OleNameTypes();
                    if(nameJsonObject.has("firstName")){
                        oleNameTypes.setFirst(getOleLoaderService().getStringValueFromJsonObject(nameJsonObject, "firstName"));
                    }
                    if(nameJsonObject.has("surName")){
                        oleNameTypes.setSurname(getOleLoaderService().getStringValueFromJsonObject(nameJsonObject, "surName"));
                    }
                    if(nameJsonObject.has("prefix")){
                        oleNameTypes.setTitle(getOleLoaderService().getStringValueFromJsonObject(nameJsonObject, "prefix"));
                    }
                    if(nameJsonObject.has("suffix")){
                        oleNameTypes.setSuffix(getOleLoaderService().getStringValueFromJsonObject(nameJsonObject, "suffix"));
                    }
                    patron.setName(oleNameTypes);
                }

                if(jsonObject.has("phone")){
                    JSONArray phoneJsonArray = getOleLoaderService().getJsonArrayValueFromJsonObject(jsonObject,"phone");
                    patron = populateIngestOlePatronPhone(phoneJsonArray, patron);
                }

                if(jsonObject.has("email")){
                    JSONArray emailJsonArray = getOleLoaderService().getJsonArrayValueFromJsonObject(jsonObject,"email");
                    patron = populateIngestOlePatronEmail(emailJsonArray, patron);
                }

                if(jsonObject.has("address")){
                    JSONArray addressJsonArray = getOleLoaderService().getJsonArrayValueFromJsonObject(jsonObject,"address");
                    patron = populateIngestOlePatronAddress(addressJsonArray, patron , olePatrons);
                }

                if(jsonObject.has("affiliation")){
                    JSONArray affiliationJsonArray = getOleLoaderService().getJsonArrayValueFromJsonObject(jsonObject,"affiliation");
                    patron = populateIngestOlePatronAffiliations(affiliationJsonArray, patron);
                }

                if(jsonObject.has("note")){
                    JSONArray noteJsonArray = getOleLoaderService().getJsonArrayValueFromJsonObject(jsonObject, "note");
                    patron = populateIngestOlePatronNotes(noteJsonArray, patron);
                }
                selectedPatronBarcodeIndexMap.put(barcode, index);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            olePatrons.add(patron);
        }
        return olePatrons;
    }

    public OlePatron populateIngestOlePatronEmail(JSONArray emailJsonArray, OlePatron olePatron) {
        List<OlePatronEmailAddress> olePatronEmailAddressList = new ArrayList<>();
        for (int index = 0; index < emailJsonArray.length(); index++) {
            JSONObject emailJsonObject = null;
            OlePatronEmailAddress olePatronEmailAddress = new OlePatronEmailAddress();
            try {
                emailJsonObject = (JSONObject) emailJsonArray.get(index);
                if(emailJsonObject.has("emailAddressType")){
                    olePatronEmailAddress.setEmailAddressType(getOleLoaderService().getStringValueFromJsonObject(emailJsonObject, "emailAddressType"));
                }
                if(emailJsonObject.has("emailSource")){
                    olePatronEmailAddress.setEmailSource(getOleLoaderService().getStringValueFromJsonObject(emailJsonObject, "emailSource"));
                }
                if(emailJsonObject.has("emailAddress")){
                    olePatronEmailAddress.setEmailAddress(getOleLoaderService().getStringValueFromJsonObject(emailJsonObject, "emailAddress"));
                }
                if(emailJsonObject.has("active")){
                    olePatronEmailAddress.setActive(Boolean.parseBoolean(getOleLoaderService().getStringValueFromJsonObject(emailJsonObject, "active")));
                }
                if(emailJsonObject.has("default")){
                    olePatronEmailAddress.setDefaults(Boolean.parseBoolean(getOleLoaderService().getStringValueFromJsonObject(emailJsonObject, "default")));
                }
                olePatronEmailAddressList.add(olePatronEmailAddress);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
            olePatron.setEmailAddresses(olePatronEmailAddressList);
            return olePatron;

    }

    public OlePatron populateIngestOlePatronName(JSONArray emailJsonArray, OlePatron olePatron) {
        /*OleNameTypes oleNameTypes = new OleNameTypes();
        oleNameTypes.setFirst(olePatronBo.getName().getFirstName());
        oleNameTypes.setSuffix(olePatronBo.getName().getSuffix());
        oleNameTypes.setSurname(olePatronBo.getName().getSurname());
        oleNameTypes.setTitle(olePatronBo.getName().getTitle());
        olePatron.setName(oleNameTypes);*/
        return olePatron;
    }

    public OlePatron populateIngestOlePatronAddress(JSONArray addressJsonArray, OlePatron olePatron, List<OlePatron> olePatrons) {
        List<OlePatronPostalAddress> olePatronPostalAddressList = new ArrayList<>();
        for (int index = 0; index < addressJsonArray.length(); index++) {
            JSONObject addressJsonObject = null;
            OlePatronPostalAddress olePatronPostalAddress = new OlePatronPostalAddress();
            try {
                addressJsonObject = (JSONObject) addressJsonArray.get(index);
                if(addressJsonObject.has("city")){
                    olePatronPostalAddress.setCity(getOleLoaderService().getStringValueFromJsonObject(addressJsonObject, "city"));
                }
                if(addressJsonObject.has("country")){
                    olePatronPostalAddress.setCountry(getOleLoaderService().getStringValueFromJsonObject(addressJsonObject, "country"));
                }
                if(addressJsonObject.has("postalCode")){
                    olePatronPostalAddress.setPostalCode(getOleLoaderService().getStringValueFromJsonObject(addressJsonObject, "postalCode"));
                }
                if(addressJsonObject.has("addressTypeCode")){
                    olePatronPostalAddress.setPostalAddressType(getOleLoaderService().getStringValueFromJsonObject(addressJsonObject, "addressTypeCode"));
                }
                if(addressJsonObject.has("stateProvinceCode")){
                    olePatronPostalAddress.setStateProvince(getOleLoaderService().getStringValueFromJsonObject(addressJsonObject, "stateProvinceCode"));
                }
                if(addressJsonObject.has("active")){
                    olePatronPostalAddress.setActive(Boolean.parseBoolean(getOleLoaderService().getStringValueFromJsonObject(addressJsonObject, "active")));
                }
                if(addressJsonObject.has("default")){
                    olePatronPostalAddress.setDefaults(Boolean.parseBoolean(getOleLoaderService().getStringValueFromJsonObject(addressJsonObject, "default")));
                }

                if(addressJsonObject.has("addressSource")){
                    olePatronPostalAddress.setAddressSource(getOleLoaderService().getStringValueFromJsonObject(addressJsonObject, "addressSource"));
                }

                List<OleAddressLine> oleAddressLineList = new ArrayList<>();
                for(int lineIndex = 1 ; lineIndex <= 3 ; lineIndex ++){
                    OleAddressLine oleAddressLine = new OleAddressLine();
                    if(addressJsonObject.has("line"+lineIndex)){
                        oleAddressLine.setAddressLine(getOleLoaderService().getStringValueFromJsonObject(addressJsonObject, "line" + lineIndex));
                    }
                    oleAddressLineList.add(oleAddressLine);
                }
                olePatronPostalAddress.setAddressLinesList(oleAddressLineList);

                if(addressJsonObject.has("validFrom")){
                    String validFrom = getOleLoaderService().getStringValueFromJsonObject(addressJsonObject,"validFrom");
                    if(StringUtils.isNotBlank(validFrom )){
                        try{
                            Date date = OLELoaderConstants.DATE_FORMAT.parse(validFrom);
                            olePatronPostalAddress.setAddressValidFrom(new java.sql.Date(date.getTime()));
                        }catch(Exception e){
                            e.printStackTrace();
                            olePatron.setErrorMessage(OLELoaderConstants.OLEloaderMessage.INVALID_DATE);
                            olePatrons.add(olePatron);
                            break;
                        }
                    }
                }

                if(addressJsonObject.has("validTo")){
                    String validTo = getOleLoaderService().getStringValueFromJsonObject(addressJsonObject,"validTo");
                    if(StringUtils.isNotBlank(validTo )){
                        try{
                            Date date = OLELoaderConstants.DATE_FORMAT.parse(validTo);
                            olePatronPostalAddress.setAddressValidTo(new java.sql.Date(date.getTime()));
                        }catch(Exception e){
                            e.printStackTrace();
                            olePatron.setErrorMessage(OLELoaderConstants.OLEloaderMessage.INVALID_DATE);
                            olePatrons.add(olePatron);
                            break;
                        }
                    }
                }
                if(addressJsonObject.has("addressVerified")){
                    try{
                        boolean addressVerified = Boolean.parseBoolean(getOleLoaderService().getStringValueFromJsonObject(addressJsonObject, "addressVerified"));
                        olePatronPostalAddress.setAddressVerified(addressVerified);
                    }catch(Exception e){
                        e.printStackTrace();
                        olePatron.setErrorMessage(OLELoaderConstants.OLEloaderMessage.INVALID_BOOLEAN);
                        olePatrons.add(olePatron);
                        break;
                    }

                }
                olePatronPostalAddressList.add(olePatronPostalAddress);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        olePatron.setPostalAddresses(olePatronPostalAddressList);
        return olePatron;
    }

    public OlePatron populateIngestOlePatronPhone(JSONArray phoneJsonArray, OlePatron olePatron) {
        List<OlePatronTelePhoneNumber> telephoneNumbers = new ArrayList<>();
        for (int index = 0; index < phoneJsonArray.length(); index++) {
            JSONObject phoneJsonObject = null;
            OlePatronTelePhoneNumber olePatronTelePhoneNumber = new OlePatronTelePhoneNumber();
            try {
                phoneJsonObject = (JSONObject) phoneJsonArray.get(index);
                if(phoneJsonObject.has("phoneType")){
                    olePatronTelePhoneNumber.setTelephoneNumberType(getOleLoaderService().getStringValueFromJsonObject(phoneJsonObject, "phoneType"));
                }
                if(phoneJsonObject.has("phoneNumber")){
                    olePatronTelePhoneNumber.setTelephoneNumber(getOleLoaderService().getStringValueFromJsonObject(phoneJsonObject, "phoneNumber"));
                }
                if(phoneJsonObject.has("country")){
                    olePatronTelePhoneNumber.setCountry(getOleLoaderService().getStringValueFromJsonObject(phoneJsonObject, "country"));
                }
                if(phoneJsonObject.has("extensionNumber")){
                    olePatronTelePhoneNumber.setExtension(getOleLoaderService().getStringValueFromJsonObject(phoneJsonObject, "extensionNumber"));
                }
                if(phoneJsonObject.has("active")){
                    olePatronTelePhoneNumber.setActive(Boolean.parseBoolean(getOleLoaderService().getStringValueFromJsonObject(phoneJsonObject, "active")));
                }
                if(phoneJsonObject.has("default")){
                    olePatronTelePhoneNumber.setDefaults(Boolean.parseBoolean(getOleLoaderService().getStringValueFromJsonObject(phoneJsonObject, "default")));
                }
                if(phoneJsonObject.has("phoneSource")){
                    olePatronTelePhoneNumber.setPhoneSource(getOleLoaderService().getStringValueFromJsonObject(phoneJsonObject, "phoneSource"));
                }
                telephoneNumbers.add(olePatronTelePhoneNumber);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        olePatron.setTelephoneNumbers(telephoneNumbers);
        return olePatron;
    }


    public OlePatron populateIngestOlePatronAffiliations(JSONArray affiliationJsonArray, OlePatron olePatron) {
        List<OlePatronAffiliations> affiliations = new ArrayList<>();
        for (int index = 0; index < affiliationJsonArray.length(); index++) {
            JSONObject affiliationJsonObject = null;
            OlePatronAffiliations olePatronAffiliations = new OlePatronAffiliations();
            try {
                affiliationJsonObject = (JSONObject) affiliationJsonArray.get(index);
                if(affiliationJsonObject.has("employment")){
                    JSONObject employmentJsonObject = getOleLoaderService().getJsonObjectValueFromJsonObject(affiliationJsonObject, "employment");
                    List<OlePatronEmployments> olePatronEmploymentsList = new ArrayList<>();
                    OlePatronEmployments olePatronEmployments = new OlePatronEmployments();
                    if(employmentJsonObject.has("employeeTypeCode")) {
                        olePatronEmployments.setEmployeeTypeCode(getOleLoaderService().getStringValueFromJsonObject(employmentJsonObject, "employeeTypeCode"));
                    }
                    if(employmentJsonObject.has("salaryAmount")){
                        try{
                            olePatronEmployments.setBaseSalaryAmount(new KualiDecimal(getOleLoaderService().getStringValueFromJsonObject(employmentJsonObject, "salaryAmount")));
                        }catch (Exception e){
                            olePatron.setErrorMessage(OLELoaderConstants.OLEloaderMessage.PATRON_INVALID_SALARY);
                            break;
                        }

                    }
                    if (employmentJsonObject.has("employeeStatusCode")) {
                        olePatronEmployments.setEmployeeStatusCode(getOleLoaderService().getStringValueFromJsonObject(employmentJsonObject, "employeeStatusCode"));
                    }
                    if (employmentJsonObject.has("employeeId")) {
                        olePatronEmployments.setEmployeeId(getOleLoaderService().getStringValueFromJsonObject(employmentJsonObject, "employeeId"));
                    }
                    if (employmentJsonObject.has("active")) {
                        olePatronEmployments.setActive(Boolean.parseBoolean(getOleLoaderService().getStringValueFromJsonObject(employmentJsonObject, "active")));
                    }
                    if (employmentJsonObject.has("primary")) {
                        olePatronEmployments.setPrimary(Boolean.parseBoolean(getOleLoaderService().getStringValueFromJsonObject(employmentJsonObject, "primary")));
                    }
                    if (employmentJsonObject.has("primaryDepartmentCode")) {
                        olePatronEmployments.setPrimaryDepartmentCode(getOleLoaderService().getStringValueFromJsonObject(employmentJsonObject, "primaryDepartmentCode"));
                    }
                    olePatronEmploymentsList.add(olePatronEmployments);
                    olePatronAffiliations.setEmployments(olePatronEmploymentsList);
                }
                if(affiliationJsonObject.has("campusCode")){
                    olePatronAffiliations.setCampusCode(getOleLoaderService().getStringValueFromJsonObject(affiliationJsonObject, "campusCode"));
                }
                if(affiliationJsonObject.has("affiliationType")){
                    olePatronAffiliations.setAffiliationType(getOleLoaderService().getStringValueFromJsonObject(affiliationJsonObject, "affiliationType"));
                }
                if(affiliationJsonObject.has("active")){
                    olePatronAffiliations.setActive(Boolean.parseBoolean(getOleLoaderService().getStringValueFromJsonObject(affiliationJsonObject, "active")));
                }
                affiliations.add(olePatronAffiliations);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        olePatron.setAffiliations(affiliations);
        return olePatron;
    }

    public OlePatron populateIngestOlePatronNotes(JSONArray noteJsonArray, OlePatron olePatron) {
        List<OlePatronNote> notes = new ArrayList<>();
        for (int index = 0; index < noteJsonArray.length(); index++) {
            JSONObject noteJsonObject = null;
            OlePatronNote olePatronNote = new OlePatronNote();
            try {
                noteJsonObject = (JSONObject) noteJsonArray.get(index);
                if(noteJsonObject.has("noteType")){
                    olePatronNote.setNoteType(getOleLoaderService().getStringValueFromJsonObject(noteJsonObject, "noteType"));
                }
                if(noteJsonObject.has("note")){
                    olePatronNote.setNote(getOleLoaderService().getStringValueFromJsonObject(noteJsonObject, "note"));
                }
                if(noteJsonObject.has("active")){
                    olePatronNote.setActive(Boolean.parseBoolean(getOleLoaderService().getStringValueFromJsonObject(noteJsonObject, "active")));
                }
                notes.add(olePatronNote);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        olePatron.setNotes(notes);
        return olePatron;
    }
}
