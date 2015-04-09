package org.kuali.ole.loaders.deliver.service.impl;

import com.sun.jersey.api.core.HttpContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.deliver.bo.OLEPatronEntityViewBo;
import org.kuali.ole.deliver.bo.OleAddressBo;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.bo.OlePatronNotes;
import org.kuali.ole.ingest.pojo.OlePatron;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;
import org.kuali.ole.loaders.common.constants.OLELoaderConstants;
import org.kuali.ole.loaders.common.service.OLELoaderService;
import org.kuali.ole.loaders.common.service.impl.OLELoaderServiceImpl;
import org.kuali.ole.loaders.deliver.service.OLEPatronLoaderHelperService;
import org.kuali.rice.kim.impl.identity.address.EntityAddressBo;
import org.kuali.rice.kim.impl.identity.affiliation.EntityAffiliationBo;
import org.kuali.rice.kim.impl.identity.email.EntityEmailBo;
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentBo;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            patronLevelPolicyJsonObject.put("courtesyNotice",olePatron.isCourtesyNotice());
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
                        patronLevelPolicyJsonObject.put("courtesyNotice",olePatron.isCourtesyNotice());
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
        OLEPatronEntityViewBo olePatronEntityViewBo = olePatronDocument.getOlePatronEntityViewBo();
        JSONObject nameJsonObject = new JSONObject();
        try{
            if(olePatronEntityViewBo != null) {
                nameJsonObject.put("firstName", olePatronEntityViewBo.getFirstName());
                nameJsonObject.put("surName", olePatronEntityViewBo.getLastName());
                nameJsonObject.put("suffix", olePatronEntityViewBo.getSuffix());
                nameJsonObject.put("prefix", olePatronEntityViewBo.getPrefix());
            }
            patronJsonObject.put("name",nameJsonObject);
        }catch (Exception e){
            LOG.error("Error while process name object");
        }
        return patronJsonObject;
    }

    private JSONObject populateOlePatronPhone(JSONObject patronJsonObject, OlePatronDocument olePatronDocument) {
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
        EntityBo entity = olePatronDocument.getEntity();
        JSONArray emailJsonArray = new JSONArray();
        try{
            if (!entity.getEntityTypeContactInfos().isEmpty()) {
                List<EntityEmailBo> entityEmailList = entity.getEntityTypeContactInfos().get(0).getEmailAddresses();
                for(EntityEmailBo entityEmailBo : entityEmailList){
                    JSONObject emailJsonObject = new JSONObject();
                    emailJsonObject.put("emailAddressType",entityEmailBo.getEntityTypeCode());
                    emailJsonObject.put("emailAddress", entityEmailBo.getEmailAddress());
                    emailJsonObject.put("active", entityEmailBo.isActive());
                    emailJsonObject.put("defaults", entityEmailBo.isDefaultValue());
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
                    addressJsonObject.put("defaults", entityAdd.isDefaultValue());
                    addressJsonObject.put("postalCode", entityAdd.getPostalCode());
                    addressJsonObject.put("addressTypeCode",entityAdd.getAddressTypeCode());
                    addressJsonObject.put("stateProvinceCode", entityAdd.getStateProvinceCode());
                    for(OleAddressBo oleAddress : oleAddressBoList){
                        if(oleAddress.getId().equalsIgnoreCase(entityAdd.getId())) {
                            addressJsonObject.put("validFrom", oleAddress.getAddressValidFrom());
                            addressJsonObject.put("validTo", oleAddress.getAddressValidTo());
                            addressJsonObject.put("addressVerified",oleAddress.isAddressVerified());
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
}
