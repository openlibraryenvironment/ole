package org.kuali.ole.service.impl;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.alert.document.OleMaintenanceDocumentBase;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.License;
import org.kuali.ole.docstore.common.document.LicenseAttachment;
import org.kuali.ole.docstore.common.document.LicenseOnixpl;
import org.kuali.ole.docstore.common.document.Licenses;
import org.kuali.ole.docstore.discovery.service.QueryServiceImpl;
import org.kuali.ole.select.bo.*;
import org.kuali.ole.select.controller.LicenceRoutingRuleDelegationMaintainable;
import org.kuali.ole.select.document.OLEEResourceRecordDocument;
import org.kuali.ole.service.OLEEResourceSearchService;
import org.kuali.ole.service.OleLicenseRequestService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.utility.CompressUtils;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.bo.DocumentHeader;
import org.kuali.rice.krad.maintenance.MaintenanceDocumentBase;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * OleLicenseRequestServiceImpl builds the Xml for the Agreement Document ingest operation.
 */
public class OleLicenseRequestServiceImpl implements OleLicenseRequestService {

    private static final Logger LOG = Logger.getLogger(OleLicenseRequestServiceImpl.class);
    private static final String DOCSTORE_URL = "docstore.url";
    private static final String queryString = "DocType:bibliographic AND Title_search:";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private OLEEResourceSearchService oleEResourceSearchService = null;
    private DocstoreClientLocator docstoreClientLocator;
    private DocumentHeader documentHeader;

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (null == docstoreClientLocator) {
            return  SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }


    /**
     *  This method creates the multipart entity  based on zipfile and sent to the target location,
     *
     * @param target
     * @param zipFile
     * @return List<File>
     * @throws Exception
     */
//    public static List<File> postDataForLicense(String target, File zipFile) throws Exception {
//        CompressUtils compressUtils = new CompressUtils();
//        HttpPost httpPost = new HttpPost(target);
//        HttpClient httpclient = new DefaultHttpClient();
//        FileBody uploadFilePart = new FileBody(zipFile);
//        MultipartEntity reqEntity = new MultipartEntity();
//        reqEntity.addPart("upload-file", uploadFilePart);
//        httpPost.setEntity(reqEntity);
//
//        HttpResponse httpResponse = httpclient.execute(httpPost);
//
//        HttpEntity respEntity = httpResponse.getEntity();
//        InputStream outcome = respEntity.getContent();
//        File respFile = File.createTempFile("DocStore Ingest-", "-Response File.zip");
//        IOUtils.copy(outcome, new FileOutputStream(respFile));
//        List<File> resp = compressUtils.getAllFilesList(compressUtils.extractZippedBagFile(respFile.getAbsolutePath(), null));
//        return resp;
//    }
    /**
     *  This method creates the zipped Bag file which contains the requestXml and the File to be uploaded to the docStore and
     *                  process the response file to get the uuid.
     * @param oleAgreementDocs
     * @return List<OleAgreementDocumentMetadata>
     */
    public List<OleAgreementDocumentMetadata> processIngestAgreementDocuments(List<OleAgreementDocumentMetadata> oleAgreementDocs) {
        try {
            String filePath = getKualiConfigurationService().getPropertyValueAsString(
                    KRADConstants.ATTACHMENTS_PENDING_DIRECTORY_KEY) + OLEConstants.OleLicenseRequest.AGREEMENT_TMP_LOCATION;

            File file = new File(filePath);
            file.mkdirs();
            Licenses licenses = new Licenses();
            for (OleAgreementDocumentMetadata oleAgreementDoc : oleAgreementDocs) {
                LicenseAttachment licenseAttachment = new LicenseAttachment();
                licenseAttachment.setFilePath(filePath);
                licenseAttachment.setFileName(oleAgreementDoc.getAgreementFileName());
//                File agreementDoc = new File(filePath + File.separator + oleAgreementDoc.getAgreementFileName());
//                FileUtils.copyFileToDirectory(agreementDoc, file);
                String agreementFormat = oleAgreementDoc.getAgreementFileName().substring(oleAgreementDoc.getAgreementFileName().indexOf(".")+1,oleAgreementDoc.getAgreementFileName().length());
                if((agreementFormat.equals("pdf")) | (agreementFormat.equals("xslt"))) {
                    licenseAttachment.setFormat(agreementFormat);
                }
                else {
                    licenseAttachment.setFormat("doc");
                }
//                licenseAttachment.setByteContent(FileUtils.readFileToByteArray(agreementDoc));
                licenseAttachment.setFileName(oleAgreementDoc.getAgreementFileName());
                licenseAttachment.setDocumentTitle(oleAgreementDoc.getAgreementName());
                licenseAttachment.setDocumentMimeType(oleAgreementDoc.getAgreementMimeType());
                licenseAttachment.setAgreementNote(oleAgreementDoc.getAgreementNotes());
                licenseAttachment.setAgreementType(oleAgreementDoc.getAgreementType());
                licenses.getLicenses().add(licenseAttachment);
            }

            getDocstoreClientLocator().getDocstoreClient().createLicenses(licenses);
            Iterator agreementDocIterator = oleAgreementDocs.iterator();
            for (License license : licenses.getLicenses()) {
                OleAgreementDocumentMetadata agreementDocumentMetadata = (OleAgreementDocumentMetadata) agreementDocIterator.next();
                agreementDocumentMetadata.setAgreementUUID(license.getId());
                File agreementDoc = new File(filePath + File.separator + agreementDocumentMetadata.getAgreementFileName());
                agreementDoc.delete();
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        return oleAgreementDocs;
    }
    /**
     *  This method creates the zipped Bag file which contains the requestXml and the File to be updated to the docStore and
     *                  process the response file to get the uuid (after the checkin).
     * @param oleAgreementDocs
     * @return List<oleAgreementDocumentMetadata>
     */
    public List<OleAgreementDocumentMetadata> processCheckInAgreementDocuments(List<OleAgreementDocumentMetadata> oleAgreementDocs) {
        try {
//            String docstoreURL = ConfigContext.getCurrentContextConfig().getProperty(DOCSTORE_URL);
            CompressUtils compressUtils = new CompressUtils();
            File file = new File(getKualiConfigurationService().getPropertyValueAsString(
                    KRADConstants.ATTACHMENTS_PENDING_DIRECTORY_KEY)+ OLEConstants.OleLicenseRequest.AGREEMENT_LOCATION);
            file.mkdirs();
            Licenses licenses = buildCheckInAgreementRequestXml(oleAgreementDocs);
            getDocstoreClientLocator().getDocstoreClient().updateLicenses(licenses);
        }

        catch(Exception e) {
            e.printStackTrace();
        }
        return oleAgreementDocs;

    }
    /**
     *  This method builds the requestXml for the Agreement Document ingest operation.
     * @param agreementDocs
     * @return requestXml
     */

//    private String buildIngestAgreementRequestXml(List<OleAgreementDocumentMetadata> agreementDocs) {
//        Request requestObject = new Request();
//        RequestDocument requestDocument;
//        ArrayList<RequestDocument> requestDocuments = new ArrayList<RequestDocument>();
//        requestObject.setOperation("ingest");
//        Person user = GlobalVariables.getUserSession().getPerson();
//        requestObject.setUser(user.getPrincipalName());
//        for(OleAgreementDocumentMetadata agreementDoc : agreementDocs) {
//            requestDocument = new RequestDocument();
//            requestDocument.setId("1");
//            requestDocument.setCategory(OLEConstants.WORK_CATEGORY);
//            requestDocument.setType("license");
//            requestDocument.setUser(agreementDoc.getUploadedBy());
//            String agreementFormat = agreementDoc.getAgreementFileName().substring(agreementDoc.getAgreementFileName().indexOf(".")+1,agreementDoc.getAgreementFileName().length());
//            if((agreementFormat.equals("pdf")) | (agreementFormat.equals("xslt"))) {
//                requestDocument.setFormat(agreementFormat);
//            }
//            else {
//                requestDocument.setFormat("doc");
//            }
//
//            requestDocument.setDocumentName(agreementDoc.getAgreementFileName());
//            requestDocument.setDocumentTitle(agreementDoc.getAgreementName());
//            requestDocument.setDocumentMimeType(agreementDoc.getAgreementMimeType());
//            AdditionalAttributes additionalAttributes = new AdditionalAttributes();
//            additionalAttributes.setDateEntered(agreementDoc.getUploadedDate().toString());
//            additionalAttributes.setAttribute("label", agreementDoc.getAgreementName());
//            additionalAttributes.setAttribute("notes", agreementDoc.getAgreementNotes());
//            additionalAttributes.setAttribute("type",agreementDoc.getAgreementType());
//            requestDocument.setAdditionalAttributes(additionalAttributes);
//            requestDocuments.add(requestDocument);
//        }
//        requestObject.setRequestDocuments(requestDocuments);
//
//        RequestHandler requestHandler = new RequestHandler();
//        String requestXml = requestHandler.toXML(requestObject);
//        return requestXml;
//    }

    /**
     * This method builds the requestXml for the checkin operation of the agreement document.
     * @param agreementDocs
     * @return  requestXml
     */
    private Licenses buildCheckInAgreementRequestXml(List<OleAgreementDocumentMetadata> agreementDocs) {
        Licenses licenses = new Licenses();

        for(OleAgreementDocumentMetadata agreementDoc : agreementDocs) {
            License license = null;
            String agreementFormat = agreementDoc.getAgreementFileName().substring(agreementDoc.getAgreementFileName().indexOf(".")+1,agreementDoc.getAgreementFileName().length());
            if((agreementFormat.equals("pdf")) || (agreementFormat.equals("xslt")) || (agreementFormat.equals("doc"))) {
                LicenseAttachment licenseAttachment = new LicenseAttachment();
                licenseAttachment.setFormat(agreementFormat);
                licenseAttachment.setFileName(agreementDoc.getAgreementFileName());
                licenseAttachment.setDocumentTitle(agreementDoc.getAgreementName());
                licenseAttachment.setDocumentMimeType(agreementDoc.getAgreementMimeType());
                licenseAttachment.setAgreementType(agreementDoc.getAgreementType());
                license = licenseAttachment;

            }
            else {
                license = new LicenseOnixpl();
                license.setFormat("onixpl");
            }

            license.setId(agreementDoc.getAgreementUUID());
            license.setCategory(OLEConstants.WORK_CATEGORY);
            license.setType("license");
            license.setUpdatedBy(agreementDoc.getUploadedBy());
            licenses.getLicenses().add(license);
        }
        return licenses;
    }
    /**
     *  This method downloads the agreement document from the docStore based on uuid.
     * @param oleAgreementDocumentMetadata
     * @return File
     */
    public File downloadAgreementDocumentFromDocstore(OleAgreementDocumentMetadata oleAgreementDocumentMetadata) {
//        File resultFile = null;
//        String docstoreURL = ConfigContext.getCurrentContextConfig().getProperty(DOCSTORE_URL);
//        CompressUtils compressUtils = new CompressUtils();
//        File file = new File(getKualiConfigurationService().getPropertyValueAsString(
//                KRADConstants.ATTACHMENTS_PENDING_DIRECTORY_KEY)+ OLEConstants.OleAgreementDownloadConstants.AGREEMENT_CHECKOUT_LOCATION_ROOT);
//        file.mkdirs();
//        String requestXml = checkoutAgreementDocument(oleAgreementDocumentMetadata);
//        try {
//            File requestxml = new File(getKualiConfigurationService().getPropertyValueAsString(
//                    KRADConstants.ATTACHMENTS_PENDING_DIRECTORY_KEY)+ OLEConstants.OleAgreementDownloadConstants.AGREEMENT_CHECKOUT_LOCATION);
//            requestxml.createNewFile();
//            FileUtils.writeStringToFile(requestxml, requestXml);
//            File zipFile = compressUtils.createZippedBagFile(file);
//            requestxml.delete();
//            file.delete();
//            List<File> resp = postDataForLicense(docstoreURL, zipFile);
//            zipFile.delete();
//            for (File respFile : resp) {
//                if (respFile.getName().equalsIgnoreCase(oleAgreementDocumentMetadata.getAgreementFileName())) {
//                    return respFile;
//                }
//            }
//        }
//        catch(Exception e) {
//            LOG.error("Error while retriving form docstore");
//        }

        try {
            LOG.info("License id to retrieve : " + oleAgreementDocumentMetadata.getAgreementUUID());
            LicenseAttachment licenseAttachment = (LicenseAttachment) getDocstoreClientLocator().getDocstoreClient().retrieveLicense(oleAgreementDocumentMetadata.getAgreementUUID());

            File resultFile = new File(licenseAttachment.getFilePath() + File.separator + licenseAttachment.getFileName());
            return resultFile;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * This method builds the requestXml for the checkOut of agreementdocument from the docstore.
     * @param oleAgreementDocumentMetadata
     * @return  String
     */
//    private String checkoutAgreementDocument(OleAgreementDocumentMetadata oleAgreementDocumentMetadata) {
//        if (oleAgreementDocumentMetadata.getAgreementUUID() != null) {
//            Request request = new Request();
//
//            request.setUser(GlobalVariables.getUserSession().getPrincipalName());
//            request.setOperation("checkOut");
//            RequestDocument requestDocument = new RequestDocument();
//            requestDocument.setId("1");
//            requestDocument.setCategory(OLEConstants.WORK_CATEGORY);
//            requestDocument.setType("license");
//            String agreementFormat = oleAgreementDocumentMetadata.getAgreementFileName().substring(oleAgreementDocumentMetadata.getAgreementFileName().indexOf(".")+1,
//                    oleAgreementDocumentMetadata.getAgreementFileName().length());
//            requestDocument.setFormat(agreementFormat);
//            requestDocument.setUuid(oleAgreementDocumentMetadata.getAgreementUUID());
//            request.getRequestDocuments().add(requestDocument);
//            RequestHandler requestHandler = new RequestHandler();
//            String requestXml = requestHandler.toXML(request);
//
//            return requestXml;
//        }
//        else {
//            LOG.error("UUId doesnt exit");
//        }
//        return null;
//    }

    /**
     * This method returns the uuid by  ingesting the agreement content to the docstore
     * @param content
     * @return   uuid
     */
    public String ingestAgreementContent(String content) {
        License licenseOnixpl = new LicenseOnixpl();
        String uuid = "";
        licenseOnixpl.setCreatedBy(GlobalVariables.getUserSession().getPrincipalName());
        licenseOnixpl.setCategory(OLEConstants.WORK_CATEGORY);
        licenseOnixpl.setType("license");
        licenseOnixpl.setFormat("onixpl");
        licenseOnixpl.setContent(content);
        Licenses licenses = new Licenses();
        licenses.getLicenses().add(licenseOnixpl);
        try {
            getDocstoreClientLocator().getDocstoreClient().createLicenses(licenses);
        }
         catch (Exception e) {
             LOG.error("Exception while ingesting aggrement content", e);
         }
        return licenseOnixpl.getId();
    }

    /**
     * This method returns the agreement content for the given uniqueId
     * @param uuid
     * @return agreementContent
     */
    public String getAgreementContent(String uuid){
        String agreementContent = "";
        try {
            LOG.info("License content id to retrieve : " + uuid);
            License license = getDocstoreClientLocator().getDocstoreClient().retrieveLicense(uuid);
            agreementContent = license.getContent();
        }
        catch (Exception e) {
            LOG.error("Exception while getting agreement content", e);
        }

        return agreementContent;
    }

    /**
     * This method returns the License request documents based on the criterias map
     * @param licenseId
     * @return  licenseRequestList
     */
    public int getLicenseAttachments(String licenseId){


        BusinessObjectService service = KRADServiceLocator.getBusinessObjectService();
        Map<String,String> documentCriteria = new HashMap<String,String>();
        documentCriteria.put(OLEConstants.NAME, OLEConstants.OleLicenseRequest.LICENSE_REQUEST_DOC_TYPE);
        List<DocumentType> documentTypeList= (List<DocumentType>) service.findMatching(DocumentType.class,documentCriteria);
        List<OleAgreementDocumentMetadata> list = null;
        Map<String,String> searchCriteria = new HashMap<String,String>();
        for(int k=0;k<documentTypeList.size();k++){
            searchCriteria.put(OLEConstants.DOC_TYP_ID,documentTypeList.get(k).getDocumentTypeId());
            searchCriteria.put("documentId",licenseId);
            List<DocumentRouteHeaderValue> documentList= ( List<DocumentRouteHeaderValue>)  service.findMatching(DocumentRouteHeaderValue.class,searchCriteria);
            OleLicenseRequestBo licenseRequestBo ;
            for(int i=0;i<documentList.size();i++){
                //LicenceRoutingRuleDelegationMaintainable bo = (LicenceRoutingRuleDelegationMaintainable) getDataObjectFromXML(documentList.get(i).getDocContent());
                String documentNumber = documentList.get(i).getDocumentId();
                Map docIdMap = new HashMap();
                docIdMap.put("documentNumber",documentNumber);
                MaintenanceDocumentBase oleMaintenanceDocumentBase = service.findByPrimaryKey(MaintenanceDocumentBase.class,docIdMap);
                OleMaintenanceDocumentBase oleMaintenanceDocumentBase1 = new OleMaintenanceDocumentBase();
                licenseRequestBo = (OleLicenseRequestBo)oleMaintenanceDocumentBase1.getDataObjectFromXML("newMaintainableObject",oleMaintenanceDocumentBase.getXmlDocumentContents());
                list=licenseRequestBo.getAgreementDocumentMetadataList();

            }

        }
        return list.size();

    }
    public OLEEResourceSearchService getOleEResourceSearchService() {
        if (oleEResourceSearchService == null) {
            oleEResourceSearchService = GlobalResourceLoader.getService(OLEConstants.OLEEResourceRecord.ERESOURSE_SEARCH_SERVICE);
        }
        return oleEResourceSearchService;
    }

    public List<OleLicenseRequestBo> findLicenseRequestByCriteria( Map<String, String> criteria)throws Exception{
        boolean add =false ;
        BusinessObjectService service = KRADServiceLocator.getBusinessObjectService();
        List<OleLicenseRequestBo> licenseRequestList = new ArrayList<OleLicenseRequestBo>();
        //List<String> uuids = new ArrayList<String>();
        //boolean isTitlePresent = false;
        boolean isValidDate = false;
        String createdFromDate=criteria.get(OLEConstants.OleLicenseRequest.CREATED_FROM_DATE);
        String createdToDate=criteria.get(OLEConstants.OleLicenseRequest.CREATED_TO_DATE);
        String lastModifiedDateFrom = criteria.get(OLEConstants.OleLicenseRequest.LAST_MOD_FROM_DATE);
        String lastModifiedDateTo = criteria.get(OLEConstants.OleLicenseRequest.LAST_MOD_TO_DATE);
        String lastModifiedDateSearchType = criteria.get(OLEConstants.OleLicenseRequest.LAST_MOD_SEARCH_TYPE);
        try {
            //createdToDate=createdToDate.length()!=0?createdToDate:dateFormat.format(new Date());
            //Timestamp createdDateTo = new Timestamp(dateFormat.parse(createdToDate).getTime());
            //lastModifiedDateTo=lastModifiedDateTo.length()!=0?lastModifiedDateTo:dateFormat.format(new Date());
            //Timestamp lastModifiedToDate = new Timestamp(dateFormat.parse(lastModifiedDateTo).getTime());
            /*if(!("".equals(criteria.get(OLEConstants.OleLicenseRequest.BIB_TITLE)))) {
                isTitlePresent =  true;
                uuids = getUUIDs(criteria.get(OLEConstants.OleLicenseRequest.BIB_TITLE));
            }*/
            Map<String,String> documentCriteria = new HashMap<String,String>();
            documentCriteria.put(OLEConstants.NAME, OLEConstants.OleLicenseRequest.LICENSE_REQUEST_DOC_TYPE);
            List<DocumentType> documentTypeList= (List<DocumentType>) service.findMatching(DocumentType.class,documentCriteria);
            Map<String,String> searchCriteria = new HashMap<String,String>();
            for(int k=0;k<documentTypeList.size();k++){
                searchCriteria.put(OLEConstants.DOC_TYP_ID,documentTypeList.get(k).getDocumentTypeId());
                List<DocumentRouteHeaderValue> documentList= ( List<DocumentRouteHeaderValue>)  service.findMatching(DocumentRouteHeaderValue.class,searchCriteria);
                OleLicenseRequestBo licenseRequestBo ;
                for(int i=0;i<documentList.size();i++){
                    add = false;
                    //LicenceRoutingRuleDelegationMaintainable bo = (LicenceRoutingRuleDelegationMaintainable) getDataObjectFromXML(documentList.get(i).getDocContent());
                    //OleLicenseRequestBo bo =
                    Date createDateInBo=documentList.get(i).getCreateDate();
                    //String strCreateDateInBo=dateFormat.format(createDateInBo);
                    //String lastModifiedDateInBo = dateFormat.format(documentList.get(i).getDateModified());

                    String documentNumber = documentList.get(i).getDocumentId();
                    Map docIdMap = new HashMap();
                    docIdMap.put("documentNumber",documentNumber);
                    MaintenanceDocumentBase oleMaintenanceDocumentBase = service.findByPrimaryKey(MaintenanceDocumentBase.class,docIdMap);
                    OleMaintenanceDocumentBase oleMaintenanceDocumentBase1 = new OleMaintenanceDocumentBase();
                    licenseRequestBo = (OleLicenseRequestBo)oleMaintenanceDocumentBase1.getDataObjectFromXML("newMaintainableObject",oleMaintenanceDocumentBase.getXmlDocumentContents());
                    boolean isValidCreateDate = false;
                    boolean  isValidModifiedDate = false;
                    boolean  isDateBlank = false;
                    if (createdFromDate.isEmpty() && createdToDate.isEmpty() && lastModifiedDateFrom.isEmpty() && lastModifiedDateTo.isEmpty()){
                        isDateBlank=true;
                    }
                    isValidCreateDate = validateDate(createDateInBo,createdFromDate,createdToDate);
                    isValidModifiedDate = validateDate(documentList.get(i).getDateModified(),lastModifiedDateFrom,lastModifiedDateTo);
                    if(lastModifiedDateSearchType.equalsIgnoreCase("true")){
                        isValidDate =  isValidCreateDate && isValidModifiedDate;
                    }
                    else {
                        isValidDate = (isValidCreateDate && !(createdFromDate .isEmpty() && createdToDate.isEmpty()))||
                                (isValidModifiedDate && !(lastModifiedDateFrom.isEmpty() && lastModifiedDateTo.isEmpty()));
                    }
                    if(licenseRequestBo!=null && (isValidDate || isDateBlank)) {
                        //licenseRequestBo =  (OleLicenseRequestBo) bo.getDataObject();
                        Map<String, String> tempId = new HashMap<String, String>();
                        tempId.put(OLEConstants.DOC_NUM, licenseRequestBo.geteResourceDocNumber());
                        OLEEResourceRecordDocument oleeResourceRecordDocument = (OLEEResourceRecordDocument) KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OLEEResourceRecordDocument.class, tempId);
                        if (oleeResourceRecordDocument != null) {
                            licenseRequestBo.seteResourceName(oleeResourceRecordDocument.getTitle());
                        }
                        if (("".equals(criteria.get(OLEConstants.OleLicenseRequest.ASSIGNEE))) || (licenseRequestBo.getAssignee() != null && !licenseRequestBo.getAssignee().isEmpty()
                                && licenseRequestBo.getAssignee().equalsIgnoreCase(criteria.get(OLEConstants.OleLicenseRequest.ASSIGNEE)))) {
                            if (("".equals(criteria.get(OLEConstants.OleLicenseRequest.LOCATION_ID))) || (licenseRequestBo.getLocationId() != null && !licenseRequestBo.getLocationId().isEmpty()
                                    && licenseRequestBo.getLocationId().equalsIgnoreCase(criteria.get(OLEConstants.OleLicenseRequest.LOCATION_ID)))) {
                                if (("".equals(criteria.get(OLEConstants.OleLicenseRequest.STATUS_CODE))) || (licenseRequestBo.getLicenseRequestStatusCode() != null
                                        && !licenseRequestBo.getLicenseRequestStatusCode().isEmpty() && licenseRequestBo.getOleLicenseRequestStatus().getName().equalsIgnoreCase(criteria.get(OLEConstants.OleLicenseRequest.STATUS_CODE)))) {
                                    if (("".equals(criteria.get(OLEConstants.OleLicenseRequest.LICENSE_REQUEST_TYPE_ID))) || (licenseRequestBo.getLicenseRequestTypeId() != null
                                            && !licenseRequestBo.getLicenseRequestTypeId().isEmpty() && licenseRequestBo.getLicenseRequestTypeId().equalsIgnoreCase(criteria.get(OLEConstants.OleLicenseRequest.LICENSE_REQUEST_TYPE_ID)))) {
                                        if (licenseRequestBo.geteResourceName() != null && !licenseRequestBo.geteResourceName().isEmpty()) {
                                            DocumentSearchCriteria.Builder docSearchCriteria = DocumentSearchCriteria.Builder.create();
                                            docSearchCriteria.setDocumentTypeName(OLEConstants.OLEEResourceRecord.OLE_ERS_DOC);
                                            Map<String, List<String>> eResNameMap = new HashMap<>();
                                            List<String> eResNameList = new ArrayList<>();
                                            if (!"".equals(criteria.get(OLEConstants.OleLicenseRequest.E_RES_NAME)))
                                                eResNameList.add(criteria.get(OLEConstants.OleLicenseRequest.E_RES_NAME));
                                            eResNameMap.put(OLEConstants.OLEEResourceRecord.ERESOURCE_TITLE, eResNameList);
                                            List<OLEEResourceRecordDocument> oleeResourceRecordDocumentList = getOleEResourceSearchService().findMatching(eResNameMap, docSearchCriteria);
                                            for (OLEEResourceRecordDocument oleEResourceRecordDocument : oleeResourceRecordDocumentList) {
                                                if (oleEResourceRecordDocument.getTitle().equals(licenseRequestBo.geteResourceName())) {
                                                    add = true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (add) {
                            OleLicenseRequestBo oleLicenseRequestBo = getOleLicenseRequestBoWithDocNumb(licenseRequestBo);
                            if (oleLicenseRequestBo != null) {
                                List<OleLicenseRequestItemTitle> itemTitleList = oleLicenseRequestBo.getOleLicenseRequestItemTitles();
                                List<OleLicenseRequestItemTitle> newItemTitleList = new ArrayList<OleLicenseRequestItemTitle>();
                                if (itemTitleList != null && itemTitleList.size() > 0) {
                                    OleLicenseRequestBo newLicenseRequestBo;
                                    for (int j = 0; j < itemTitleList.size(); j++) {
                                        newLicenseRequestBo = (OleLicenseRequestBo) ObjectUtils.deepCopy(licenseRequestBo);
                                        newLicenseRequestBo.setOleLicenseRequestItemTitles(null);
                                        newItemTitleList.add(itemTitleList.get(j));
                                        newLicenseRequestBo.setOleLicenseRequestItemTitles(newItemTitleList);
                                        newLicenseRequestBo.setDocumentNumber(documentList.get(i).getDocumentContent().getDocumentId());
                                        newLicenseRequestBo.setCreatedDate(documentList.get(i).getCreateDate());
                                        newLicenseRequestBo.setCreatedDateFrom(documentList.get(i).getCreateDate());
                                    /*if(isTitlePresent) {*/
                                        /*if(uuids.size() > 0 && uuids.contains(itemTitleList.get(j).getItemUUID())) {*/
                                        //String bibliographicTitle= getDescription(itemTitleList.get(j).getItemUUID());
                                        //newLicenseRequestBo.setBibliographicTitle(bibliographicTitle);
                                        if (newLicenseRequestBo.getLocationId() != null && !newLicenseRequestBo.getLocationId().isEmpty()) {
                                            newLicenseRequestBo.setOleLicenseRequestLocation(getLicenseRequestLocation(newLicenseRequestBo.getLocationId()));
                                        }
                                        if (newLicenseRequestBo.getLicenseRequestTypeId() != null && !newLicenseRequestBo.getLicenseRequestTypeId().isEmpty()) {
                                            newLicenseRequestBo.setOleLicenseRequestType(getLicenseRequestType(newLicenseRequestBo.getLicenseRequestTypeId()));
                                        }
                                        licenseRequestList.add(newLicenseRequestBo);
                                        /*}*/
                                    /*}*/
                                    /*else {*/
                                        //String bibliographicTitle= getDescription(itemTitleList.get(j).getItemUUID());
                                        //newLicenseRequestBo.setBibliographicTitle(bibliographicTitle);
                                        /*if (newLicenseRequestBo.getLocationId() != null && !newLicenseRequestBo.getLocationId().isEmpty()) {
                                            newLicenseRequestBo.setOleLicenseRequestLocation(getLicenseRequestLocation(newLicenseRequestBo.getLocationId()));
                                        }
                                        if (newLicenseRequestBo.getLicenseRequestTypeId() != null && !newLicenseRequestBo.getLicenseRequestTypeId().isEmpty()) {
                                            newLicenseRequestBo.setOleLicenseRequestType(getLicenseRequestType(newLicenseRequestBo.getLicenseRequestTypeId()));
                                        }
                                        licenseRequestList.add(newLicenseRequestBo);*/
                                   /* }*/
                                    }
                                } else /*if (!isTitlePresent)*/ {
                                    licenseRequestBo.setDocumentNumber(documentList.get(i).getDocumentContent().getDocumentId());
                                    licenseRequestBo.setCreatedDate(documentList.get(i).getCreateDate());
                                    licenseRequestBo.setCreatedDateFrom(documentList.get(i).getCreateDate());
                                    licenseRequestList.add(licenseRequestBo);
                                }

                            }
                        }
                    }  }
            }
        }catch  (ParseException e) {
            e.printStackTrace();
        }
        return licenseRequestList;
    }

    private OleLicenseRequestLocation getLicenseRequestLocation(String id) {
        OleLicenseRequestLocation oleLicenseRequestLocation = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(OleLicenseRequestLocation.class,
                id);
        return oleLicenseRequestLocation;
    }

    private OleLicenseRequestType getLicenseRequestType (String licenseRequestTypeId) {
        OleLicenseRequestType oleLicenseRequestType = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(OleLicenseRequestType.class, licenseRequestTypeId);
        return oleLicenseRequestType;
    }


    public OleLicenseRequestBo getLicenseRequestFromDocumentContent(String documentContent) {
        OleLicenseRequestBo oleLicenseRequestBo = null;
        LicenceRoutingRuleDelegationMaintainable bo = (LicenceRoutingRuleDelegationMaintainable) getDataObjectFromXML(documentContent);
        oleLicenseRequestBo =  (OleLicenseRequestBo) bo.getDataObject();
        return oleLicenseRequestBo;
    }
    /**
     * This method returns the license request documentNum for the given requisition documet number
     * @param reqDocNum
     * @return  licenseRequestDocNum
     */
   /* public String getLicenseRequestByRequisitionDocNum(String reqDocNum) {
        BusinessObjectService service = KRADServiceLocator.getBusinessObjectService();
        Map<String,String> documentCriteria = new HashMap<String,String>();
        documentCriteria.put(OLEConstants.NAME,OLEConstants.OleLicenseRequest.LICENSE_REQUEST_DOC_TYPE);
        List<DocumentType> documentTypeList= (List<DocumentType>) service.findMatching(DocumentType.class,documentCriteria);
        Map<String,String> searchCriteria = new HashMap<String,String>();
        for(int i=0;i<documentTypeList.size();i++){
            searchCriteria.put(OLEConstants.DOC_TYP_ID,documentTypeList.get(i).getDocumentTypeId());
        }
        List<DocumentRouteHeaderValue> documentList= ( List<DocumentRouteHeaderValue>)  service.findMatching(DocumentRouteHeaderValue.class,searchCriteria);
        String licenseRequestDocNum = "";
        OleLicenseRequestBo licenseRequestBo ;
        for(int i=0;i<documentList.size();i++){
            LicenceRoutingRuleDelegationMaintainable bo = (LicenceRoutingRuleDelegationMaintainable) getDataObjectFromXML(documentList.get(i).getDocContent());

            if(bo!=null){
                licenseRequestBo =  (OleLicenseRequestBo) bo.getDataObject();
                if(("".equals(reqDocNum)) || (licenseRequestBo.getRequisitionDocNumber()!=null&&!licenseRequestBo.getRequisitionDocNumber().isEmpty()
                        && licenseRequestBo.getRequisitionDocNumber().equals(reqDocNum))){
                    licenseRequestDocNum = licenseRequestBo.getDocumentNumber();
                }
            }
        }
        return licenseRequestDocNum;
    }*/

    /**
     * This method converts the xml content into bussinessObjectBase class
     * @param xmlDocumentContents
     * @return   businessObject
     */
    private Object getDataObjectFromXML(String xmlDocumentContents) {
        if(xmlDocumentContents!=null || !"".equals(xmlDocumentContents) || !xmlDocumentContents.isEmpty()) {
            String maintXml = StringUtils.substringBetween(xmlDocumentContents, OLEConstants.OleLicenseRequest.START_TAG,
                    OLEConstants.OleLicenseRequest.END_TAG);
            Object businessObject =null;
            if(maintXml!=null){
                maintXml = maintXml.substring(2,maintXml.length());
                businessObject = KRADServiceLocator.getXmlObjectSerializerService().fromXml(maintXml);
            }
            return businessObject;
        }
        else{
            return null;
        }
    }




    /**
     * This method returns the bibliographic title for the given uuid
     * @param bibUuid
     * @return   itemDescription
     */
    private String getDescription(String bibUuid){
        String itemDescription=null;
        String title=null;
        String author=null;
        String publisher=null;
        String isbn=null;
        try{
            List<HashMap<String, Object>> bibDocumentList= QueryServiceImpl.getInstance().retriveResults("id:"+bibUuid);
            HashMap<String, Object> bibValues = bibDocumentList.get(0);

            ArrayList<String> titleList= (ArrayList<String>) bibValues.get("Title_display");
            ArrayList<String> authorList= (ArrayList<String>) bibValues.get("Author_display");
            ArrayList<String> publisherList= (ArrayList<String>) bibValues.get("Publisher_display");
            ArrayList<String> isbnList= (ArrayList<String>) bibValues.get("ISBN_display");
            if(titleList!=null){
                title=titleList.get(0);
            }
            if(authorList!=null){
                author=authorList.get(0);
            }
            if(publisherList!=null){
                publisher=publisherList.get(0);}
            if(isbnList!=null) {
                isbn=isbnList.get(0);
            }
            itemDescription  = (( title!=null && ! title.isEmpty()) ? title+"," : "");
            /*+
      (( author!=null&&!author.isEmpty())? author+"," : "") +
      ((publisher!=null&&!publisher.isEmpty()) ?   publisher+"," : "") +
      ((isbn!=null&&!isbn.isEmpty() ) ?  isbn+"," : "");*/
            if(itemDescription != null && !(itemDescription.equals(""))){
                itemDescription = itemDescription.lastIndexOf(",") < 0 ? itemDescription :
                        itemDescription.substring(0, itemDescription.lastIndexOf(","));
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return itemDescription;
    }


    /**
     * This method returns the uuids for the given bibliographic Title
     * @param title
     * @return   uuids
     */
    private List<String> getUUIDs(String title){
        StringEscapeUtils stringEscapeUtils = new StringEscapeUtils();
        List<String> uuids = new ArrayList<String>();
        try{
            List<HashMap<String, Object>> bibDocumentList= QueryServiceImpl.getInstance().retriveResults(queryString+ stringEscapeUtils.escapeXml(title));
            if(bibDocumentList.size() > 0) {
                Iterator listIterator = bibDocumentList.iterator();
                while(listIterator.hasNext()) {
                    Map results = (Map)listIterator.next();
                    uuids.add((String)results.get("uniqueId"));
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return uuids;
    }
    private ConfigurationService getKualiConfigurationService() {
        return GlobalResourceLoader.getService("kualiConfigurationService");
    }

    /**
     * This method will delete the Agreement Document Content from the docstore
     * @param metadata
     * * @return boolean
     */
    public boolean deleteAgreementDocument(OleAgreementDocumentMetadata metadata) {
        try{
            getDocstoreClientLocator().getDocstoreClient().deleteLicense(metadata.getAgreementUUID());
            return true;
        }
        catch (Exception e) {
            return false;
        }

    }

    /**
     * This method will generate the request xml for the delete operation.
     * @param metadata
     * @return  rollBackXml
     */
//    private String buildDeleteAgreementRequestXml(OleAgreementDocumentMetadata metadata){
//        ResponseHandler responseHandler = new ResponseHandler();
//        RequestHandler requestHandler = new RequestHandler();
//        Request request = new Request();
//        request.setUser("khuntley");
//        request.setOperation("delete");
//        RequestDocument requestDocument = new RequestDocument();
//        requestDocument.setId("1");
//        requestDocument.setCategory(OLEConstants.WORK_CATEGORY);
//        requestDocument.setType("license");
//        requestDocument.setUuid(metadata.getAgreementUUID());
//        String agreementFormat = metadata.getAgreementFileName().substring(metadata.getAgreementFileName().indexOf(".")+1
//                ,metadata.getAgreementFileName().length());
//        if((agreementFormat.equals("pdf")) | (agreementFormat.equals("xslt"))) {
//            requestDocument.setFormat(agreementFormat);
//        }
//        else {
//            requestDocument.setFormat("doc");
//        }
//        //requestDocument.setFormat(agreementFormat);
//        List<RequestDocument> requestDocuments = new ArrayList<RequestDocument>();
//        requestDocuments.add(requestDocument);
//        request.setRequestDocuments(requestDocuments);
//        String rollBackXml = requestHandler.toXML(request);
//        return rollBackXml;
//    }
    @Override
    public boolean validateDate (Date documentDate, String fromDate, String toDate)throws Exception{
        boolean isValidDate = false ;
        String dateToCompare = "";
        if(documentDate != null ) {
            dateToCompare=dateFormat.format(documentDate);
        }
        try {
            if (((toDate == null || toDate.isEmpty()) && fromDate != null && !fromDate.isEmpty())
                    && (dateToCompare.equals(fromDate) || (documentDate != null && documentDate.after(dateFormat.parse(fromDate))))) {
                isValidDate = true;
            }
            else if ( ((fromDate == null || fromDate.isEmpty()) && toDate != null && !toDate.isEmpty())
                    && (dateToCompare.equals(toDate) || (documentDate != null && documentDate.before(dateFormat.parse(toDate))))) {
                isValidDate = true;
            }
            else if (((fromDate == null || fromDate.isEmpty() ) && ((toDate == null) || toDate.isEmpty())) ||
                    ((fromDate != null && (dateToCompare.equals(fromDate) || (documentDate != null && documentDate.after(dateFormat.parse(fromDate))))) &&
                            (toDate != null && (dateToCompare.equals(toDate) || (documentDate != null && documentDate.before(dateFormat.parse(toDate))))))) {
                isValidDate = true;
            }
        }
        catch (Exception e) {
            LOG.error("Error while comparing the date" + e.getMessage());
            throw new RuntimeException(e);
        }
        return isValidDate;

    }
    public OleLicenseRequestBo getOleLicenseRequestBoWithDocNumb(OleLicenseRequestBo oleLicenseRequestBo){
        Map<String,String> map=new HashMap<>();
        map.put(OLEConstants.OLEEResourceRecord.ERESOURCE_DOC_NUMBER,oleLicenseRequestBo.geteResourceDocNumber());
        return KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OleLicenseRequestBo.class,map);
    }


}