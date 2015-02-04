package org.kuali.ole.deliver.inquiry;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEPropertyConstants;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.deliver.service.OleDeliverRequestDocumentHelperServiceImpl;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.kim.impl.identity.address.EntityAddressBo;
import org.kuali.rice.kim.impl.identity.affiliation.EntityAffiliationBo;
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentBo;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.bo.ExternalizableBusinessObject;
import org.kuali.rice.krad.datadictionary.exception.UnknownBusinessClassAttributeException;
import org.kuali.rice.krad.inquiry.InquirableImpl;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.ModuleService;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OlePatronInquirableImpl supports to get the data object and patron document.
 */
public class OlePatronInquirableImpl extends InquirableImpl {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OlePatronInquirableImpl.class);

    /**
     * This method will retrieve the patron document based on the olePatronId
     *
     * @param parameters
     * @return patronDocument(Object)
     */
    String baseUrl = ConfigContext.getCurrentContextConfig().getProperty(OLEPropertyConstants.OLE_URL_BASE);

    public Object retrieveDataObject(Map<String, String> parameters) {
        if (dataObjectClass == null) {
            LOG.error("Data object class must be set in inquirable before retrieving the object");
            throw new RuntimeException("Data object class must be set in inquirable before retrieving the object");
        }

        // build list of key values from the map parameters
        List<String> pkPropertyNames = getDataObjectMetaDataService().listPrimaryKeyFieldNames(dataObjectClass);

        // some classes might have alternate keys defined for retrieving
        List<List<String>> alternateKeyNames = this.getAlternateKeysForClass(dataObjectClass);

        // add pk set as beginning so it will be checked first for match
        alternateKeyNames.add(0, pkPropertyNames);

        List<String> dataObjectKeySet = retrieveKeySetFromMap(alternateKeyNames, parameters);
        if ((dataObjectKeySet == null) || dataObjectKeySet.isEmpty()) {
            LOG.warn("Matching key set not found in request for class: " + getDataObjectClass());

            return null;
        }
        String patronHome = parameters.get("flow");

        // found key set, now build map of key values pairs we can use to retrieve the object
        Map<String, Object> keyPropertyValues = new HashMap<String, Object>();
        for (String keyPropertyName : dataObjectKeySet) {
            String keyPropertyValue = parameters.get(keyPropertyName);

            // uppercase value if needed
            Boolean forceUppercase = Boolean.FALSE;
            try {
                forceUppercase = getDataDictionaryService().getAttributeForceUppercase(dataObjectClass,
                        keyPropertyName);
            } catch (UnknownBusinessClassAttributeException ex) {
                // swallowing exception because this check for ForceUppercase would
                // require a DD entry for the attribute, and we will just set force uppercase to false
                LOG.warn("Data object class "
                        + dataObjectClass
                        + " property "
                        + keyPropertyName
                        + " should probably have a DD definition.", ex);
            }

            if (forceUppercase.booleanValue() && (keyPropertyValue != null)) {
                keyPropertyValue = keyPropertyValue.toUpperCase();
            }

            // check security on key field
            if (getDataObjectAuthorizationService().attributeValueNeedsToBeEncryptedOnFormsAndLinks(dataObjectClass,
                    keyPropertyName)) {
                try {
                    keyPropertyValue = getEncryptionService().decrypt(keyPropertyValue);
                } catch (GeneralSecurityException e) {
                    LOG.error("Data object class "
                            + dataObjectClass
                            + " property "
                            + keyPropertyName
                            + " should have been encrypted, but there was a problem decrypting it.", e);
                    throw new RuntimeException("Data object class "
                            + dataObjectClass
                            + " property "
                            + keyPropertyName
                            + " should have been encrypted, but there was a problem decrypting it.", e);
                }
            }

            keyPropertyValues.put(keyPropertyName, keyPropertyValue);
        }

        // now retrieve the object based on the key set
        Object dataObject = null;

        ModuleService moduleService = KRADServiceLocatorWeb.getKualiModuleService().getResponsibleModuleService(
                getDataObjectClass());
        if (moduleService != null && moduleService.isExternalizable(getDataObjectClass())) {
            dataObject = moduleService.getExternalizableBusinessObject(getDataObjectClass().asSubclass(
                    ExternalizableBusinessObject.class), keyPropertyValues);
        } else if (BusinessObject.class.isAssignableFrom(getDataObjectClass())) {
            dataObject = getBusinessObjectService().findByPrimaryKey(getDataObjectClass().asSubclass(
                    BusinessObject.class), keyPropertyValues);
        }
        //OleEntityAddressBo entityAddressBo = new OleEntityAddressBo();
        EntityAddressBo entityAddress = new EntityAddressBo();
        List<OleEntityAddressBo> oleEntityAddressList = new ArrayList<OleEntityAddressBo>();
        OlePatronDocument patronDocument = (OlePatronDocument) dataObject;
        if (patronDocument != null) {
            EntityBo kimEnity = patronDocument.getEntity();
            if (kimEnity.getNames().size() > 0) {
                patronDocument.setName(kimEnity.getNames().get(0));
            }
            if(kimEnity.getEntityTypeContactInfos().size()>0){
                List<EntityAddressBo> entityAddressList = kimEnity.getEntityTypeContactInfos().get(0).getAddresses();
                for (EntityAddressBo entityAdd : entityAddressList) {
                    OleEntityAddressBo entityAddressBo = new OleEntityAddressBo();
                    entityAddressBo.setEntityAddressBo(entityAdd);
                    Map addMap = new HashMap();
                    //addMap.put(OLEConstants.OlePatron.OLE_ADDRESS_ID, entityAdd.getId());
                    addMap.put("id", entityAdd.getId());
                    OleAddressBo oleAddressBo = getBusinessObjectService().findByPrimaryKey(OleAddressBo.class, addMap);
                    entityAddressBo.setOleAddressBo(oleAddressBo);
                    oleEntityAddressList.add(entityAddressBo);
                }
                patronDocument.setOleEntityAddressBo(oleEntityAddressList);
                patronDocument.setPhones(kimEnity.getEntityTypeContactInfos().get(0).getPhoneNumbers());
                patronDocument.setEmails(kimEnity.getEntityTypeContactInfos().get(0).getEmailAddresses());
                patronDocument.setPatronAffiliations(getPatronAffiliationFromEntity(kimEnity.getAffiliations(), kimEnity.getEmploymentInformation()));
                if (patronHome != null) {
                    if (patronHome.equalsIgnoreCase("start"))
                        patronDocument.setPatronHomePage(false);
                    else
                        patronDocument.setPatronHomePage(true);
                }
            }



            List<OleProxyPatronDocument> oleProxyPatronDocuments = patronDocument.getOleProxyPatronDocuments();
            List<OleProxyPatronDocument> proxyPatronDocumentList = new ArrayList<OleProxyPatronDocument>();
            if (oleProxyPatronDocuments.size() > 0) {
                for (OleProxyPatronDocument oleProxyPatronDocument : oleProxyPatronDocuments) {
                    Map<String, String> proxyMap = new HashMap<String, String>();
                    proxyMap.put(OLEConstants.OlePatron.PATRON_ID, oleProxyPatronDocument.getProxyPatronId());
                    OlePatronDocument tempDocument = (OlePatronDocument) KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OlePatronDocument.class, proxyMap);
                    if (tempDocument != null && tempDocument.isActiveIndicator()) {
                        oleProxyPatronDocument.setProxyPatronBarcode(tempDocument.getBarcode());
                        oleProxyPatronDocument.setProxyPatronFirstName(tempDocument.getEntity().getNames().get(0).getFirstName());
                        oleProxyPatronDocument.setProxyPatronLastName(tempDocument.getEntity().getNames().get(0).getLastName());
                        proxyPatronDocumentList.add(oleProxyPatronDocument);
                    }
                }
                patronDocument.setOleProxyPatronDocuments(proxyPatronDocumentList);
            }
            OlePatronDocument olePatronDocument;
            OleProxyPatronDocument proxyPatronDocument = null;
            List<OleProxyPatronDocument> proxyPatronDocuments = new ArrayList<OleProxyPatronDocument>();
            Map proxyMap = new HashMap();
            proxyMap.put(OLEConstants.OlePatron.PROXY_PATRON_ID, patronDocument.getOlePatronId());
            List<OleProxyPatronDocument> oleProxyPatronDocumentList = (List<OleProxyPatronDocument>) getBusinessObjectService().findMatching(OleProxyPatronDocument.class, proxyMap);
            if (oleProxyPatronDocumentList.size() > 0) {
                proxyMap.remove(OLEConstants.OlePatron.PROXY_PATRON_ID);
                for (OleProxyPatronDocument oleProxyPatronDocument : oleProxyPatronDocumentList) {
                    proxyMap.put(OLEConstants.OlePatron.PATRON_ID, oleProxyPatronDocument.getOlePatronId());
                    List<OlePatronDocument> olePatronDocumentList = (List<OlePatronDocument>) getBusinessObjectService().findMatching(OlePatronDocument.class, proxyMap);
                    if (olePatronDocumentList.size() > 0) {
                        olePatronDocument = olePatronDocumentList.get(0);
                        proxyPatronDocument = new OleProxyPatronDocument();
                        if (olePatronDocument.isActiveIndicator()) {
                            proxyPatronDocument.setOlePatronId(olePatronDocument.getOlePatronId());
                            proxyPatronDocument.setProxyForPatronFirstName(olePatronDocument.getEntity().getNames().get(0).getFirstName());
                            proxyPatronDocument.setProxyForPatronLastName(olePatronDocument.getEntity().getNames().get(0).getLastName());
                            proxyPatronDocument.setProxyPatronActivationDate(oleProxyPatronDocument.getProxyPatronActivationDate());
                            proxyPatronDocument.setProxyPatronExpirationDate(oleProxyPatronDocument.getProxyPatronExpirationDate());
                            /*List<OleProxyPatronDocument> proxyPatronList = (List<OleProxyPatronDocument>) getBusinessObjectService().findMatching(OleProxyPatronDocument.class, proxyMap);
                            if (proxyPatronList.size() > 0) {
                                for (OleProxyPatronDocument proxyPatron : proxyPatronList) {
                                    if (proxyPatron.getOlePatronId().equals(olePatronDocument.getOlePatronId())) {
                                        proxyPatronDocument.setProxyPatronActivationDate(proxyPatron.getProxyPatronActivationDate());
                                        proxyPatronDocument.setProxyPatronExpirationDate(proxyPatron.getProxyPatronExpirationDate());
                                    }
                                }
                            }*/
                        }
                        proxyPatronDocuments.add(proxyPatronDocument);
                    }
                }
                patronDocument.setOleProxyPatronDocumentList(proxyPatronDocuments);
            }
            LoanProcessor loanProcessor = new LoanProcessor();
            OleDeliverRequestDocumentHelperServiceImpl requestService = new OleDeliverRequestDocumentHelperServiceImpl();
            List<OleDeliverRequestBo> oleDeliverRequestBoList = patronDocument.getOleDeliverRequestBos();
            if (oleDeliverRequestBoList.size() > 0) {
                for (int i = 0; i < oleDeliverRequestBoList.size(); i++) {
            /*        OleItemSearch oleItemSearch= requestService.getItemDetailsForPatron(oleDeliverRequestBoList.get(i).getItemUuid());
                    if(oleItemSearch!=null && oleItemSearch.getItemBarCode()!=null)
                    {
                    oleDeliverRequestBoList.get(i).setTitle(oleItemSearch.getTitle());
                    oleDeliverRequestBoList.get(i).setCallNumber(oleItemSearch.getCallNumber());
                    }*/
                    requestService.processItem(oleDeliverRequestBoList.get(i));
                }
            }

            try {
                patronDocument.setOleLoanDocuments(loanProcessor.getPatronLoanedItemBySolr(patronDocument.getOlePatronId()));
                patronDocument.setOleTemporaryCirculationHistoryRecords(loanProcessor.getPatronTemporaryCirculationHistoryRecords(patronDocument.getOlePatronId()));
                patronDocument.setOleDeliverRequestBos(loanProcessor.getPatronRequestRecords(patronDocument.getOlePatronId()));
            } catch (Exception e) {
                LOG.error("Cannot fetch the patron loaned items.", e);

            }
        }

        return patronDocument;
    }

    private List<OlePatronAffiliation> getPatronAffiliationFromEntity(List<EntityAffiliationBo> affiliations,
                                                                      List<EntityEmploymentBo> employeeDetails) {
        List<OlePatronAffiliation> patronAffiliations = new ArrayList<OlePatronAffiliation>();
        for (EntityAffiliationBo entityAffiliationBo : affiliations) {
            OlePatronAffiliation patronAffiliation = new OlePatronAffiliation(entityAffiliationBo);
            List<EntityEmploymentBo> employmentBos = new ArrayList<EntityEmploymentBo>();
            for (EntityEmploymentBo entityEmploymentBo : employeeDetails) {
                if (patronAffiliation.getEntityAffiliationId().equalsIgnoreCase(entityEmploymentBo.getEntityAffiliationId())) {
                    employmentBos.add(entityEmploymentBo);
                }
                patronAffiliation.setEmployments(employmentBos);
            }
            patronAffiliations.add(patronAffiliation);
        }
        return patronAffiliations;
    }

    public String getTempCircRecords(String olePatronId) {
        String url = baseUrl + "/portal.do?channelTitle=Patron&channelUrl=" + baseUrl + "/ole-kr-krad/temporaryCirculationRecord?viewId=OleTemporaryCirculationHistoryRecordView&amp;methodToCall=viewTempCircRecords&amp;patronId=" + olePatronId;
        return url;
    }

    public String getLoanedRecords(String olePatronId) {

        String url = baseUrl + "/portal.do?channelTitle=Patron&channelUrl=" + baseUrl + "/ole-kr-krad/patronLoanedRecord?viewId=OlePatronLoanedRecordView&amp;methodToCall=viewLoanedRecords&amp;patronId=" + olePatronId;
        return url;
    }

    public String getRequestedRecords(String olePatronId) {
        String url = baseUrl + "/portal.do?channelTitle=Patron&channelUrl=" + baseUrl + "/ole-kr-krad/patronRequestedRecord?viewId=OlePatronRequestedRecordView&amp;methodToCall=viewRequestedRecords&amp;patronId=" + olePatronId;
        return url;
    }

    public String getCountOfPendingRequests(String itemId){
        Map itemMap = new HashMap();
        itemMap.put(OLEConstants.OleDeliverRequest.ITEM_ID, itemId);
        List<OleDeliverRequestBo> oleDeliverRequestBoList = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, itemMap);
        if (oleDeliverRequestBoList!=null && oleDeliverRequestBoList.size()>0) {

            return "View all requests";
        }
        else
            return " ";
    }

    public String getHrefLink(String item,String instance,String bib){
       String inquiryUrl= ConfigContext.getCurrentContextConfig().getProperty(OLEPropertyConstants.OLE_URL_BASE)+
               "/ole-kr-krad/editorcontroller?viewId=EditorView&amp;methodToCall=load&amp;docCategory=work&amp;docType=item&amp;editable=false&amp;docFormat=oleml&amp;docId="+item+"&amp;instanceId="+instance+"&amp;bibId="+bib+"";
        return inquiryUrl;
    }

}
