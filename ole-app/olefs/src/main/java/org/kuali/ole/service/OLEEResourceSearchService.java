package org.kuali.ole.service;

import org.kuali.ole.coa.businessobject.OLECretePOAccountingLine;
import org.kuali.ole.describe.form.WorkEInstanceOlemlForm;
import org.kuali.ole.docstore.common.document.Holdings;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.module.purap.businessobject.RequisitionItem;
import org.kuali.ole.module.purap.document.RequisitionDocument;
import org.kuali.ole.select.bo.*;
import org.kuali.ole.select.businessobject.OleRequisitionItem;
import org.kuali.ole.select.document.OLEEResourceEventLog;
import org.kuali.ole.select.document.OLEEResourceRecordDocument;
import org.kuali.ole.select.document.OLEPlatformRecordDocument;
import org.kuali.ole.select.document.OleRequisitionDocument;
import org.kuali.ole.select.form.OLEEResourceRecordForm;
import org.kuali.ole.vnd.businessobject.VendorDetail;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 7/10/13
 * Time: 12:13 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OLEEResourceSearchService {

    public List<OLEEResourceRecordDocument> statusNotNull(List<OLEEResourceRecordDocument> eresourceList, List<String> status);

    public List<OLEEResourceRecordDocument> performSearch(List<OLESearchCondition> oleSearchConditionsList)throws Exception;

    public List<OLEEResourceRecordDocument> findMatching(Map<String, List<String>> map,DocumentSearchCriteria.Builder docSearchCriteria);

    public void getEResourcesFields(String eResourceId, OleHoldings OleHoldings, WorkEInstanceOlemlForm eInstanceOlemlForm);

    public void getEResourcesLicenseFields(String eResourceId, WorkEInstanceOlemlForm eInstanceOlemlForm);

    public OLEEResourceRecordDocument getNewOleERSDoc(OLEEResourceRecordDocument oleERSDoc);

   // public List<WorkBibDocument> getWorkBibDocuments(List<String> instanceIdsList, String docType);

    public String getParameter(String parameterName);

    public String getParameter(String parameterName, String componentName);

    public void getDefaultCovergeDate(OLEEResourceRecordDocument oleERSDoc);

    public void getDefaultPerpetualAccessDate(OLEEResourceRecordDocument oleERSDoc);

    public OLEEResourceRecordDocument saveDefaultCoverageDate(OLEEResourceRecordDocument oleeResourceRecordDocument);

    public OLEEResourceRecordDocument saveDefaultPerpetualAccessDate(OLEEResourceRecordDocument oleeResourceRecordDocument);

    public void getNewInstance(OLEEResourceRecordDocument oleERSDoc, String documentNumber) throws Exception;

    public void getNewInstance(OLEEResourceRecordDocument oleERSDoc, String documentNumber, Holdings holdings) throws Exception;

    public void getAccessLocationFromEInstance(OleHoldings OleHoldings, WorkEInstanceOlemlForm workEInstanceOlemlForm);

    public void getDefaultCovDatesToPopup(OLEEResourceRecordDocument oleeResourceRecordDocument, String defaultCov);

    public void getDefaultPerAccDatesToPopup(OLEEResourceRecordDocument oleeResourceRecordDocument, String defaultPerpetualAcc);

    public boolean validateEResourceDocument(OLEEResourceRecordDocument oleeResourceRecordDocument);

    public void saveEResourceInstanceToDocstore(OLEEResourceRecordDocument oleeResourceRecordDocument)throws Exception;

    public boolean validateCoverageStartDates(OLEEResourceRecordDocument oleeResourceRecordDocument, OLEEResourceRecordForm oleERSForm);

    public boolean validateCoverageEndDates(OLEEResourceRecordDocument oleeResourceRecordDocument, OLEEResourceRecordForm oleERSForm);

    public boolean validatePerpetualAccessStartDates(OLEEResourceRecordDocument oleeResourceRecordDocument, OLEEResourceRecordForm oleERSForm);

    public boolean validatePerpetualAccessEndDates(OLEEResourceRecordDocument oleeResourceRecordDocument, OLEEResourceRecordForm oleERSForm);

    public boolean validateDates(OleHoldings eHoldings);

    public void getAcquisitionInfoFromPOAndInvoice(String holdingsId, WorkEInstanceOlemlForm workEInstanceOlemlForm);

    public void getInvoiceForERS(OLEEResourceRecordDocument oleERSDoc);

    public void getPoForERS(OLEEResourceRecordDocument oleERSDoc);

    public void removeDuplicateEresDocumentsFromList(List<OLEEResourceRecordDocument> eresourceDocumentList);

    public List<OLEEResourceRecordDocument> filterEResRecBystatusDate(Date beginDate, Date endDate,List<OLEEResourceRecordDocument> eresourceList);

    public List<RequisitionItem> generateItemList(OLEEResourceOrderRecord oleEResourceOrderRecord, OleRequisitionDocument requisitionDocument) throws Exception;

    public List<RequisitionItem> generateMultipleItemsForOneRequisition(List<OLEEResourceOrderRecord> oleEResourceOrderRecordList, OleRequisitionDocument requisitionDocument) throws Exception;

    public List<OLEEResourceOrderRecord> fetchOleOrderRecordList(List<OLECreatePO> posToCreateForHoldings, String linkToOrderOption, String location) throws Exception;

    public String validateAccountngLinesVendorAndPrice(OLECreatePO createPO);

    public void getBannerMessage(OLEEResourceRecordDocument oleEResourceRecordDocument);

    public List<OLECreatePO> getInstances(OLEEResourceRecordDocument oleeResourceRecordDocument, String purposeId);

    public List<OLECreatePO> getEresources(OLEEResourceRecordDocument oleeResourceRecordDocument, String purposeId);

    public String validateAccountingLines(String errorMessage, OLECretePOAccountingLine accountingLine);

    public RequisitionDocument setDocumentValues(OleRequisitionDocument requisitionDocument, OLEEResourceOrderRecord oleEResourceOrderRecord) throws Exception;

    public void setItemDescription(OLEEResourceOrderRecord oleEResourceOrderRecord, OleRequisitionItem item) throws Exception;

    public boolean validateAccountingLines(OLEEResourceAccountingLine accountingLine, String sectionId);

    public void createVendor(String organizationName, Integer gokbOrganizationId, String variantName);

    public void updateVendor(VendorDetail vendorDetail,String organizationName);

    public OLEPlatformRecordDocument createPlatform(String platformName,Integer gokbPlatformId,String softwarePlatform, String platformStatus,Integer platformProviderId);

    public void updatePlatform(OLEPlatformRecordDocument olePlatformRecordDocument, String platformName, String platformStatus, String softwarePlatform,Integer platformProviderId);

    public void updatePublisher(List<OLEGOKbTIPP> oleGoKbTIPPList, OLEEResourceRecordDocument oleeResourceRecordDocument);

    public void updatePlatformProvider(OLEEResourceRecordDocument oleeResourceRecordDocument);

    public void storeEventAttachments(MultipartFile attachmentFile) throws IOException;

    public void processEventAttachments(List<OLEEResourceEventLog> oleEResourceEventLogs);

    public boolean addAttachmentFile(OLEEResourceEventLog oleEResourceEventLog, String sectionId);

    public void downloadAttachment(HttpServletResponse response, String eventLogId, String fileName, byte[] attachmentContent, String attachmentMimeType) throws Exception;

    public void removeEResourcesFields(String eResourceId, OleHoldings eHoldings, WorkEInstanceOlemlForm olemlForm);

    public OLEEResourceRecordDocument populateInstanceAndEInstance(OLEEResourceRecordDocument oleeResourceRecordDocument);



}
