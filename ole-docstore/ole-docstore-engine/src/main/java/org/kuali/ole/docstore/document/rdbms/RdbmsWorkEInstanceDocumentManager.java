package org.kuali.ole.docstore.document.rdbms;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.OleDocStoreException;
import org.kuali.ole.docstore.model.rdbms.bo.*;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Content;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.ingest.ResponseDocument;
import org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.*;
import org.kuali.ole.docstore.model.xstream.work.oleml.WorkEInstanceOlemlRecordProcessor;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 7/22/13
 * Time: 3:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class RdbmsWorkEInstanceDocumentManager extends RdbmsAbstarctDocumentManager {

    private static RdbmsWorkEInstanceDocumentManager ourInstanceRdbms = null;
    private static final Logger LOG = LoggerFactory.getLogger(RdbmsWorkEInstanceDocumentManager.class);
    private BusinessObjectService businessObjectService;

    public static RdbmsWorkEInstanceDocumentManager getInstance() {
        if (null == ourInstanceRdbms) {
            ourInstanceRdbms = new RdbmsWorkEInstanceDocumentManager();
        }
        return ourInstanceRdbms;
    }

    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }


    @Override
    public void deleteDocs(RequestDocument requestDocument, Object object) {
        ResponseDocument responseDocument = new ResponseDocument();
        BusinessObjectService businessObjectService = (BusinessObjectService) object;
        EInstanceRecord eInstanceRecord = new EInstanceRecord();
        EHoldingsRecord eHoldingsRecord = new EHoldingsRecord();
        String eInstanceId = DocumentUniqueIDPrefix.getDocumentId(requestDocument.getUuid());
        Map itemMap = new HashMap();
        itemMap.put("eInstanceIdentifier", eInstanceId);
        eInstanceRecord.seteInstanceIdentifier(eInstanceId);
        eHoldingsRecord.seteHoldingsIdentifier(eInstanceId);
        deleteEHoldingNChildRecords(eHoldingsRecord);
        getBusinessObjectService().delete(eInstanceRecord);
        buildResponseDocument(requestDocument, eInstanceRecord, responseDocument);
    }

    @Override
    public ResponseDocument checkoutContent(RequestDocument requestDocument, Object object) {
        ResponseDocument respDoc = new ResponseDocument();
        Map eInstanceMap = new HashMap();
        eInstanceMap.put("eInstanceIdentifier", DocumentUniqueIDPrefix.getDocumentId(requestDocument.getUuid()));
        EInstanceRecord eInstanceRecord = getBusinessObjectService().findByPrimaryKey(EInstanceRecord.class, eInstanceMap);
        String content = buildEInstanceContent(eInstanceRecord);
        Content contentObj = new Content();
        contentObj.setContent(content);
        respDoc.setUuid(requestDocument.getUuid());
        respDoc.setCategory(requestDocument.getCategory());
        respDoc.setType(requestDocument.getType());
        respDoc.setFormat(requestDocument.getFormat());
        respDoc.setContent(contentObj);
        return respDoc;
    }

    @Override
    public void checkInContent(RequestDocument requestDocument, Object object, ResponseDocument responseDocument) throws OleDocStoreException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Node storeDocument(RequestDocument requestDocument, Object object, ResponseDocument responseDocument) throws OleDocStoreException {
        businessObjectService = (BusinessObjectService) object;
        String bibUUid = null;
        WorkEInstanceOlemlRecordProcessor workEInstanceOlemlRecordProcessor = new WorkEInstanceOlemlRecordProcessor();
        InstanceCollection instanceCollection = workEInstanceOlemlRecordProcessor.fromXML(requestDocument.getContent().getContent());
        EInstance eInstance = instanceCollection.getEInstance().get(0);
        EHoldings eHoldings = eInstance.getEHoldings();

        for (EInstance inst : instanceCollection.getEInstance()) {
            List<String> resIdList = new ArrayList<String>();
            resIdList.addAll(inst.getResourceIdentifier());
            List<String> resIdList1 = new ArrayList<String>();
            resIdList1.addAll(resIdList);
            for (String resId : inst.getResourceIdentifier()) {
                Map bibMap = new HashMap();
                bibMap.put("bibId", DocumentUniqueIDPrefix.getDocumentId(resId));
                List<BibRecord> bibRecords = (List<BibRecord>) getBusinessObjectService().findMatching(BibRecord.class, bibMap);
                if (bibRecords != null && bibRecords.size() == 0) {
                    resIdList1.remove(resId);
                }
            }
            inst.getResourceIdentifier().clear();
            inst.getResourceIdentifier().addAll(resIdList1);
        }
        if (eInstance.getResourceIdentifier() != null && eInstance.getResourceIdentifier().size() > 0) {
            bibUUid = DocumentUniqueIDPrefix.getDocumentId(eInstance.getResourceIdentifier().get(0));
        }
        String eInstancePrefix = DocumentUniqueIDPrefix.getPrefix(requestDocument.getCategory(), requestDocument.getType(), requestDocument.getFormat());
        EInstanceRecord eInstanceRecord = saveEInstanceRecord(eInstance, bibUUid, eInstancePrefix);

        String eHoldingsPrefix = DocumentUniqueIDPrefix.getPrefix(requestDocument.getCategory(), DocType.EHOLDINGS.getCode(), requestDocument.getFormat());
        EHoldingsRecord eHoldingsRecord = saveEHoldingsRecord(eHoldings, eInstanceRecord.geteInstanceIdentifier(), eHoldingsPrefix);
        eInstance.setInstanceIdentifier(DocumentUniqueIDPrefix.getPrefixedId(eInstanceRecord.getUniqueIdPrefix(), eInstanceRecord.geteInstanceIdentifier()));
        eHoldings.setHoldingsIdentifier(DocumentUniqueIDPrefix.getPrefixedId(eHoldingsRecord.getUniqueIdPrefix(), eHoldingsRecord.geteHoldingsIdentifier()));

        requestDocument.getContent().setContentObject(instanceCollection);
        requestDocument.getContent().setContent(workEInstanceOlemlRecordProcessor.toXML(instanceCollection));
        requestDocument.setUuid(DocumentUniqueIDPrefix.getPrefixedId(eInstanceRecord.getUniqueIdPrefix(), eInstanceRecord.geteInstanceIdentifier()));
        buildResponseDocument(responseDocument, eInstanceRecord, eHoldingsRecord);
        return null;
    }

    @Override
    public ResponseDocument deleteVerify(RequestDocument requestDocument, Object object) throws Exception {
        ResponseDocument responseDocument = new ResponseDocument();
        responseDocument.setStatus("success");
        return responseDocument;
    }

    private void buildResponseDocument(ResponseDocument responseDocument, EInstanceRecord eInstanceRecord, EHoldingsRecord eHoldingsRecord) {
        responseDocument.setUuid(DocumentUniqueIDPrefix.getPrefixedId(eInstanceRecord.getUniqueIdPrefix(), eInstanceRecord.geteInstanceIdentifier()));
        responseDocument.setCategory(DocCategory.WORK.getCode());
        responseDocument.setType(DocType.EINSTANCE.getCode());
        responseDocument.setFormat(DocFormat.OLEML.getCode());
        responseDocument.setStatus("Success");

        List<ResponseDocument> responseDocuments = new ArrayList<ResponseDocument>();

        ResponseDocument holdingResponseDocument = new ResponseDocument();
        holdingResponseDocument.setCategory(DocCategory.WORK.getCode());
        holdingResponseDocument.setType(DocType.EHOLDINGS.getCode());
        holdingResponseDocument.setFormat(DocFormat.OLEML.getCode());
        // holdingResponseDocument.setUuid(DocumentUniqueIDPrefix.getPrefixedId(eHoldingsRecord.getUniqueIdPrefix(), eHoldingsRecord.geteHoldingsIdentifier()));
        holdingResponseDocument.setUuid(DocumentUniqueIDPrefix.getPrefixedId(eHoldingsRecord.getUniqueIdPrefix(), eHoldingsRecord.geteHoldingsIdentifier()));
        responseDocuments.add(holdingResponseDocument);

        responseDocument.setLinkedDocuments(responseDocuments);
    }

    public ResponseDocument buildResponseDocument(RequestDocument requestDocument, EInstanceRecord eInstanceRecord, ResponseDocument responseDocument) {
        responseDocument.setId(eInstanceRecord.geteInstanceIdentifier());
        responseDocument.setCategory(requestDocument.getCategory());
        responseDocument.setType(requestDocument.getType());
        responseDocument.setFormat(requestDocument.getFormat());
        responseDocument.setUuid(DocumentUniqueIDPrefix.getPrefixedId(eInstanceRecord.getUniqueIdPrefix(),
                eInstanceRecord.geteInstanceIdentifier()));
        responseDocument.setId(eInstanceRecord.geteInstanceIdentifier());
        return responseDocument;
    }

    private EHoldingsRecord saveEHoldingsRecord(EHoldings eHoldings, String eInstanceId, String eHoldingsPrefix) {

        StringBuffer locationLevel = new StringBuffer("");

        EHoldingsRecord eHoldingsRecord = new EHoldingsRecord();
        eHoldingsRecord.seteInstanceIdentifier(eInstanceId);
        eHoldingsRecord.setUniqueIdPrefix(eHoldingsPrefix);

        if (eHoldings.getLocation() != null) {
            eHoldingsRecord.setLocation(getLocation(eHoldings.getLocation(), locationLevel));
            eHoldingsRecord.setLocationLevel(locationLevel.toString());
        }

        if (eHoldings.getAccessInformation() != null) {
            eHoldingsRecord.setAccessLocation(eHoldings.getAccessInformation().getAccessLocation());
            eHoldingsRecord.setAccessPassword(eHoldings.getAccessInformation().getAccessPassword());
            eHoldingsRecord.setAccessUsername(eHoldings.getAccessInformation().getAccessUsername());
            eHoldingsRecord.setAuthenticationType(eHoldings.getAccessInformation().getAuthenticationType());
            eHoldingsRecord.setNumberSimultaneousUsers(eHoldings.getAccessInformation().getNumberOfSimultaneousUser());
            eHoldingsRecord.setProxiedResource(eHoldings.getAccessInformation().getProxiedResource());
        }
        if (eHoldings.getCallNumber() != null) {
            CallNumber callNumber = eHoldings.getCallNumber();
            eHoldingsRecord.setCallNumberPrefix(callNumber.getPrefix());
            eHoldingsRecord.setCallNumber(callNumber.getNumber());
            if (callNumber.getShelvingOrder() != null) {
                eHoldingsRecord.setShelvingOrder(callNumber.getShelvingOrder().getFullValue());
            }
            CallNumberTypeRecord callNumberTypeRecord = saveCallNumberTypeRecord(callNumber.getCallNumberType());
            eHoldingsRecord.setCallNumberTypeId(callNumberTypeRecord == null ? null : callNumberTypeRecord.getCallNumberTypeId());

        }
        eHoldingsRecord.setAccessStatus(eHoldings.getAccessStatus());
        eHoldingsRecord.setDonorNote(eHoldings.getDonorNote());
        eHoldingsRecord.setDonorPublicDisplayNote(eHoldings.getDonorPublicDisplay());
        if (eHoldings.getLink() != null) {
            eHoldingsRecord.setLink(eHoldings.getLink().getUrl() != null ? eHoldings.getLink().getUrl() : "");
            eHoldingsRecord.setLinkText(eHoldings.getLink().getText() != null ? eHoldings.getLink().getText() : "");
        }
        eHoldingsRecord.setOrderFormat(eHoldings.getOrderFormat() != null ? eHoldings.getOrderFormat() : "");
        eHoldingsRecord.setOrderType(eHoldings.getOrderType() != null ? eHoldings.getOrderType() : "");
        eHoldingsRecord.setImprint(eHoldings.getImprint() != null ? eHoldings.getImprint() : "");

        //eHoldingsRecord.setStatusDate(eHoldings.getStatusDate().toGregorianCalendar() != null ? eHoldings.getStatusDate(): "");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        Timestamp statusDate = null;
        try {
            if (eHoldings.getStatusDate() != null) {
                statusDate = new Timestamp(sdf.parse(eHoldings.getStatusDate()).getTime());

            }
        } catch (Exception e) {
            LOG.info("Exception :", e);
        }
        eHoldingsRecord.setStatusDate(statusDate);

        eHoldingsRecord.seteResourceId(eHoldings.getEResourceId());
        eHoldingsRecord.seteResourceTitle(eHoldings.getEResourceTitle());
        eHoldingsRecord.setPublisher(eHoldings.getPublisher());
        eHoldingsRecord.setLocalPersistentLink(eHoldings.getLocalPersistentLink());
        eHoldingsRecord.setVendor(eHoldings.getVendor());
        eHoldingsRecord.setPurchaseOrderId(eHoldings.getPurchaseOrderId());

        eHoldingsRecord.setiLLAllowed(eHoldings.isInterLibraryLoanAllowed());
        eHoldingsRecord.setStaffOnly(eHoldings.isStaffOnlyFlag());
        eHoldingsRecord.setStatisticalSearchCode(eHoldings.getStatisticalSearchingCode());
        eHoldingsRecord.setSubscriptionStatus(eHoldings.getSubscriptionStatus());
        if (eHoldings.getPlatform() != null) {
            eHoldingsRecord.setPlatform(eHoldings.getPlatform().getPlatformName());
            eHoldingsRecord.setAdminURL(eHoldings.getPlatform().getAdminUrl());
            eHoldingsRecord.setAdminPassword(eHoldings.getPlatform().getAdminPassword());
            eHoldingsRecord.setAdminUsername(eHoldings.getPlatform().getAdminUserName());
        }

        if (eHoldings.getInvoice() != null) {
            eHoldingsRecord.setPaymentStatus(eHoldings.getInvoice().getPaymentStatus());
            eHoldingsRecord.setFundCode(eHoldings.getInvoice().getFundCode());
        }

        getBusinessObjectService().save(eHoldingsRecord);

        if (eHoldings.getExtentOfOwnership() != null) {
            if (eHoldings.getExtentOfOwnership().getCoverages() != null) {
                eHoldingsRecord.seteInstanceCoverageRecordList(saveCoverageRecord(eHoldings.getExtentOfOwnership().getCoverages(), eHoldingsRecord.geteHoldingsIdentifier()));
            }
            if (eHoldings.getExtentOfOwnership().getPerpetualAccesses() != null) {
                eHoldingsRecord.seteInstancePerpetualAccessRecordList(savePerpetualAccessRecord(eHoldings.getExtentOfOwnership().getPerpetualAccesses(), eHoldingsRecord.geteHoldingsIdentifier()));
            }
        }
        if (eHoldings.getNote() != null && eHoldings.getNote().size() > 0) {
            eHoldingsRecord.seteHoldingsNoteList(saveHoldingsNotes(eHoldings.getNote(), eHoldingsRecord.geteHoldingsIdentifier()));
        }
        getBusinessObjectService().save(eHoldingsRecord);

        return eHoldingsRecord;
    }

    //Coverages
    private List<EInstanceCoverageRecord> saveCoverageRecord(Coverages coverages, String eHoldingsIdentifier) {

        if (eHoldingsIdentifier != null) {
            Map parentCriteria1 = new HashMap();
            parentCriteria1.put("eHoldingsIdentifier", eHoldingsIdentifier);
            getBusinessObjectService().deleteMatching(EInstanceCoverageRecord.class, parentCriteria1);
        }

        List<EInstanceCoverageRecord> coverageList = new ArrayList<>();
        for (Coverage coverage : coverages.getCoverage()) {
            EInstanceCoverageRecord eInstanceCoverageRecord = new EInstanceCoverageRecord();
            eInstanceCoverageRecord.setEHoldingsIdentifier(eHoldingsIdentifier);
            if(coverage.getCoverageStartDate() != null) {
                eInstanceCoverageRecord.setCoverageStartDate(getTimeStampFromString(coverage.getCoverageStartDate()));
            }
            eInstanceCoverageRecord.setCoverageStartVolume(coverage.getCoverageStartVolume());
            eInstanceCoverageRecord.setCoverageStartIssue(coverage.getCoverageStartIssue());
            if(coverage.getCoverageEndDate() != null) {
                eInstanceCoverageRecord.setCoverageEndDate(getTimeStampFromString(coverage.getCoverageEndDate()));
            }
            eInstanceCoverageRecord.setCoverageEndVolume(coverage.getCoverageEndVolume());
            eInstanceCoverageRecord.setCoverageEndIssue(coverage.getCoverageEndIssue());
            coverageList.add(eInstanceCoverageRecord);
        }
        return coverageList;
    }

    //Perpetual Access
    private List<EInstancePerpetualAccessRecord> savePerpetualAccessRecord(PerpetualAccesses perpetualAccesses, String eHoldingsIdentifier) {

        if (eHoldingsIdentifier != null) {
            Map parentCriteria1 = new HashMap();
            parentCriteria1.put("eHoldingsIdentifier", eHoldingsIdentifier);
            getBusinessObjectService().deleteMatching(EInstancePerpetualAccessRecord.class, parentCriteria1);
        }

        List<EInstancePerpetualAccessRecord> perpetualAccessList = new ArrayList<>();
        for (PerpetualAccess perpetualAccess : perpetualAccesses.getPerpetualAccess()) {
            EInstancePerpetualAccessRecord eInstancePerpetualAccess = new EInstancePerpetualAccessRecord();
            eInstancePerpetualAccess.setEHoldingsIdentifier(eHoldingsIdentifier);
            if(perpetualAccess.getPerpetualAccessStartDate() != null) {
                eInstancePerpetualAccess.setPerpetualAccessStartDate(getTimeStampFromString(perpetualAccess.getPerpetualAccessStartDate()));
            }
            eInstancePerpetualAccess.setPerpetualAccessStartVolume(perpetualAccess.getPerpetualAccessStartVolume());
            eInstancePerpetualAccess.setPerpetualAccessStartIssue(perpetualAccess.getPerpetualAccessStartIssue());
            if(perpetualAccess.getPerpetualAccessEndDate() != null) {
                eInstancePerpetualAccess.setPerpetualAccessEndDate(getTimeStampFromString(perpetualAccess.getPerpetualAccessEndDate()));
            }
            eInstancePerpetualAccess.setPerpetualAccessEndVolume(perpetualAccess.getPerpetualAccessEndVolume());
            eInstancePerpetualAccess.setPerpetualAccessEndIssue(perpetualAccess.getPerpetualAccessEndIssue());
            perpetualAccessList.add(eInstancePerpetualAccess);
        }
        return perpetualAccessList;
    }

    //Notes
    private List<EHoldingsNote> saveHoldingsNotes(List<Note> notes, String eHoldingsIdentifier) {

        if (eHoldingsIdentifier != null) {
            Map parentCriteria1 = new HashMap();
            parentCriteria1.put("eHoldingsIdentifier", eHoldingsIdentifier);
            getBusinessObjectService().deleteMatching(EHoldingsNote.class, parentCriteria1);
        }

        List<EHoldingsNote> noteList = new ArrayList<>();
        for (Note note : notes) {
            EHoldingsNote eHoldingsNote = new EHoldingsNote();
            EHoldingsNoteType eHoldingsNoteType = new EHoldingsNoteType();
            eHoldingsNote.seteHoldingsIdentifier(eHoldingsIdentifier);
            eHoldingsNote.seteHoldingsNoteText(note.getValue());
            noteList.add(eHoldingsNote);
        }
        return noteList;
    }

    private EInstanceRecord saveEInstanceRecord(EInstance eInstance, String bibId, String eInstancePrefix) {
        EInstanceRecord eInstanceRecord = new EInstanceRecord();
        eInstanceRecord.setUniqueIdPrefix(eInstancePrefix);
        eInstanceRecord.setBibId(bibId);
        eInstanceRecord = getBusinessObjectService().save(eInstanceRecord);
        return eInstanceRecord;
    }

    private String buildEInstanceContent(EInstanceRecord eInstanceRecord) {
        EInstance eInstance = new EInstance();
        eInstance.getResourceIdentifier().add(DocumentUniqueIDPrefix.getPrefixedId(DocumentUniqueIDPrefix.PREFIX_WORK_BIB_MARC, eInstanceRecord.getBibId()));
        InstanceCollection eInstanceCollection = new InstanceCollection();
        eInstanceRecord.seteHoldingsRecord(eInstanceRecord.geteHoldingsRecord());
        eInstance.setInstanceIdentifier(DocumentUniqueIDPrefix.getPrefixedId(eInstanceRecord.getUniqueIdPrefix(), eInstanceRecord.geteInstanceIdentifier()));
        EHoldingsRecord holdingsRecord = eInstanceRecord.geteHoldingsRecord();
        EHoldings oleHoldings = buildEHoldingsContent(holdingsRecord);
        eInstance.setEHoldings(oleHoldings);
        eInstanceCollection.getEInstance().add(eInstance);
        String content = new WorkEInstanceOlemlRecordProcessor().toXML(eInstanceCollection);
        return content;
    }

    protected EHoldings buildEHoldingsContent(EHoldingsRecord eHoldingsRecord) {
        EHoldings oleHoldings = new EHoldings();
        ExtentOfOwnership extentOfOwnership = new ExtentOfOwnership();

        oleHoldings.setHoldingsIdentifier(DocumentUniqueIDPrefix.getPrefixedId(eHoldingsRecord.getUniqueIdPrefix(), eHoldingsRecord.geteHoldingsIdentifier()));
        oleHoldings.setAccessStatus(eHoldingsRecord.getAccessStatus());

        oleHoldings.setEResourceId(eHoldingsRecord.geteResourceId());
        oleHoldings.setEResourceTitle(eHoldingsRecord.geteResourceTitle());

        AccessInformation accessInformation = new AccessInformation();
        accessInformation.setAccessLocation(eHoldingsRecord.getAccessLocation());
        accessInformation.setAccessPassword(eHoldingsRecord.getAccessPassword());
        accessInformation.setAccessUsername(eHoldingsRecord.getAccessUsername());
        accessInformation.setAuthenticationType(eHoldingsRecord.getAuthenticationType());
        accessInformation.setNumberOfSimultaneousUser(eHoldingsRecord.getNumberSimultaneousUsers());
        accessInformation.setProxiedResource(eHoldingsRecord.getProxiedResource());
        oleHoldings.setAccessInformation(accessInformation);

        Link link = new Link();
        link.setUrl(eHoldingsRecord.getLink());
        link.setText(eHoldingsRecord.getLinkText());
        oleHoldings.setLink(link);

        oleHoldings.setDonorNote(eHoldingsRecord.getDonorNote());
        oleHoldings.setDonorPublicDisplay(eHoldingsRecord.getDonorPublicDisplayNote());

        if (eHoldingsRecord.getStatusDate() != null) {
            String statusDate = new SimpleDateFormat("dd-MM-yyyy").format(eHoldingsRecord.getStatusDate());
            oleHoldings.setStatusDate(statusDate);
        }


        oleHoldings.setStaffOnlyFlag(Boolean.valueOf(eHoldingsRecord.getStaffOnly()));
        oleHoldings.setStatisticalSearchingCode(eHoldingsRecord.getStatisticalSearchCode());
        oleHoldings.setSubscriptionStatus(eHoldingsRecord.getSubscriptionStatus());
        oleHoldings.setPublisher(eHoldingsRecord.getPublisher());

        Platform platform = new Platform();
        platform.setAdminPassword(eHoldingsRecord.getAdminPassword());
        platform.setAdminUrl(eHoldingsRecord.getAdminURL());
        platform.setAdminUserName(eHoldingsRecord.getAdminUsername());
        platform.setPlatformName(eHoldingsRecord.getPlatform());
        oleHoldings.setPlatform(platform);

        oleHoldings.setImprint(eHoldingsRecord.getImprint());
        Invoice invoice = new Invoice();
        invoice.setCurrentFYCost(eHoldingsRecord.getCurrentFYCost());
        invoice.setFundCode(eHoldingsRecord.getFundCode());
        invoice.setPaymentStatus(eHoldingsRecord.getPaymentStatus());
        oleHoldings.setInvoice(invoice);

        oleHoldings.setStaffOnlyFlag(Boolean.valueOf(eHoldingsRecord.getStaffOnly()));
        oleHoldings.setInterLibraryLoanAllowed(Boolean.valueOf(eHoldingsRecord.getiLLAllowed()));
        oleHoldings.setStatisticalSearchingCode(eHoldingsRecord.getStatisticalSearchCode());


        oleHoldings.setPurchaseOrderId(eHoldingsRecord.getPurchaseOrderId());
        oleHoldings.setLocalPersistentLink(eHoldingsRecord.getLocalPersistentLink());

        if (eHoldingsRecord.getLocation() != null) {
            Location location = getLocationDetails(eHoldingsRecord.getLocation(), eHoldingsRecord.getLocationLevel());
            oleHoldings.setLocation(location);
        }
        CallNumber callNumber = new CallNumber();
        if (eHoldingsRecord.getCallNumberTypeRecord() != null) {
            callNumber.setNumber(eHoldingsRecord.getCallNumber());
            CallNumberType callNumberType = new CallNumberType();
            callNumberType.setCodeValue(eHoldingsRecord.getCallNumberTypeRecord().getCode());
            callNumber.setCallNumberType(callNumberType);
            ShelvingOrder shelvingOrder = new ShelvingOrder();
            shelvingOrder.setFullValue(eHoldingsRecord.getShelvingOrder());
            callNumber.setShelvingOrder(shelvingOrder);
            callNumber.setPrefix(eHoldingsRecord.getCallNumberPrefix());
            oleHoldings.setCallNumber(callNumber);
        }
        if (eHoldingsRecord.geteInstanceCoverageRecordList() != null && eHoldingsRecord.geteInstanceCoverageRecordList().size() > 0) {
            Coverages coverages = new Coverages();
            for (EInstanceCoverageRecord holdingCoverage : eHoldingsRecord.geteInstanceCoverageRecordList()) {
                Coverage coverage = new Coverage();
                if (holdingCoverage != null) {
                    if (holdingCoverage.getCoverageStartDate() != null) {
                        coverage.setCoverageStartDate(new SimpleDateFormat("MM/dd/yyyy").format(holdingCoverage.getCoverageStartDate()));
                    }
                    if (holdingCoverage.getCoverageEndDate() != null) {
                        coverage.setCoverageEndDate(new SimpleDateFormat("MM/dd/yyyy").format(holdingCoverage.getCoverageEndDate()));
                    }
                    coverage.setCoverageStartVolume(holdingCoverage.getCoverageStartVolume());
                    coverage.setCoverageStartIssue(holdingCoverage.getCoverageStartIssue());
                    coverage.setCoverageEndVolume(holdingCoverage.getCoverageEndVolume());
                    coverage.setCoverageEndIssue(holdingCoverage.getCoverageEndIssue());
                    coverages.getCoverage().add(coverage);
                }
            }
            extentOfOwnership.setCoverages(coverages);
        }
        if (eHoldingsRecord.geteInstancePerpetualAccessRecordList() != null && eHoldingsRecord.geteInstancePerpetualAccessRecordList().size() > 0) {
            PerpetualAccesses perpetualAccesses = new PerpetualAccesses();
            for (EInstancePerpetualAccessRecord holdingPerpetualAccess : eHoldingsRecord.geteInstancePerpetualAccessRecordList()) {
                PerpetualAccess perpetualAccess = new PerpetualAccess();
                if (holdingPerpetualAccess != null) {
                    if (holdingPerpetualAccess.getPerpetualAccessStartDate() != null) {
                        perpetualAccess.setPerpetualAccessStartDate(new SimpleDateFormat("MM/dd/yyyy").format(holdingPerpetualAccess.getPerpetualAccessStartDate()));
                    }
                    if (holdingPerpetualAccess.getPerpetualAccessEndDate() != null) {
                        perpetualAccess.setPerpetualAccessEndDate(new SimpleDateFormat("MM/dd/yyyy").format(holdingPerpetualAccess.getPerpetualAccessEndDate()));
                    }
                    perpetualAccess.setPerpetualAccessStartVolume(holdingPerpetualAccess.getPerpetualAccessStartVolume());
                    perpetualAccess.setPerpetualAccessStartIssue(holdingPerpetualAccess.getPerpetualAccessStartIssue());
                    perpetualAccess.setPerpetualAccessEndVolume(holdingPerpetualAccess.getPerpetualAccessEndVolume());
                    perpetualAccess.setPerpetualAccessEndIssue(holdingPerpetualAccess.getPerpetualAccessEndIssue());
                    perpetualAccesses.getPerpetualAccess().add(perpetualAccess);
                }
            }
            extentOfOwnership.setPerpetualAccesses(perpetualAccesses);
        }
        oleHoldings.setExtentOfOwnership(extentOfOwnership);
        if (eHoldingsRecord.geteHoldingsNoteList() != null && eHoldingsRecord.geteHoldingsNoteList().size() > 0) {
            List<EHoldingsNote> holdingsNoteRecords = eHoldingsRecord.geteHoldingsNoteList();
            if (holdingsNoteRecords != null && holdingsNoteRecords.size() > 0) {
                List<Note> notes = new ArrayList<Note>();
                for (EHoldingsNote holdingsNoteRecord : holdingsNoteRecords) {
                    Note note = new Note();
                    if (holdingsNoteRecord.geteHoldingsNoteType() != null) {
                        note.setType(holdingsNoteRecord.geteHoldingsNoteType().geteHoldingsNoteTypeName());
                    }
                    note.setValue(holdingsNoteRecord.geteHoldingsNoteText());
                    oleHoldings.getNote().add(note);
                }

            }
        }
        return oleHoldings;
    }

    private Location getLocationDetails(String locationName, String locationLevelName) {
        Location location = new Location();
        //LocationLevel locationLevel = new LocationLevel();
        LocationLevel locationLevel = createLocationLevel(locationName, locationLevelName);
        location.setLocationLevel(locationLevel);
        return location;
    }

    public LocationLevel createLocationLevel(String locationName, String locationLevelName) {
        LocationLevel locationLevel = null;
        if (StringUtils.isNotEmpty(locationName) && StringUtils.isNotEmpty(locationLevelName)) {
            String[] locations = locationName.split("/");
            String[] locationLevels = locationLevelName.split("/");
            String locName = "";
            String levelName = "";
            if (locations.length > 0) {
                locName = locations[0];
                levelName = locationLevels[0];
                if (locationName.contains("/")) {
                    locationName = locationName.replaceFirst(locations[0] + "/", "");
                } else {
                    locationName = locationName.replace(locations[0], "");
                }

                if (locationLevelName.contains("/")) {
                    locationLevelName = locationLevelName.replaceFirst(locationLevels[0] + "/", "");
                } else {
                    locationLevelName = locationLevelName.replace(locationLevels[0], "");
                }
                if (locName != null && locations.length != 0) {
                    locationLevel = new LocationLevel();
                    locationLevel.setLevel(levelName);
                    locationLevel.setName(locName);
                    locationLevel.setLocationLevel(createLocationLevel(locationName, locationLevelName));
                }
            }
        }
        return locationLevel;
    }

    protected void saveEHoldingsRecord(EHoldings eHoldings, EHoldingsRecord eHoldingsRecord) {

        StringBuffer locationLevel = new StringBuffer("");

        eHoldingsRecord.setLocation(getLocation(eHoldings.getLocation(), locationLevel));
        eHoldingsRecord.setLocationLevel(locationLevel.toString());

        eHoldingsRecord.setAccessStatus(eHoldings.getAccessStatus());
        eHoldingsRecord.setDonorNote(eHoldings.getDonorNote());
        eHoldingsRecord.setDonorPublicDisplayNote(eHoldings.getDonorPublicDisplay());

        if (eHoldings.getAccessInformation() != null) {
            eHoldingsRecord.setAccessLocation(eHoldings.getAccessInformation().getAccessLocation());
            eHoldingsRecord.setAccessPassword(eHoldings.getAccessInformation().getAccessPassword());
            eHoldingsRecord.setAccessUsername(eHoldings.getAccessInformation().getAccessUsername());
            eHoldingsRecord.setAuthenticationType(eHoldings.getAccessInformation().getAuthenticationType());
            eHoldingsRecord.setNumberSimultaneousUsers(eHoldings.getAccessInformation().getNumberOfSimultaneousUser());
            eHoldingsRecord.setProxiedResource(eHoldings.getAccessInformation().getProxiedResource());
        }

        eHoldingsRecord.setOrderFormat(eHoldings.getOrderFormat() != null ? eHoldings.getOrderFormat() : "");
        eHoldingsRecord.setOrderType(eHoldings.getOrderType() != null ? eHoldings.getOrderType() : "");
        eHoldingsRecord.setImprint(eHoldings.getImprint() != null ? eHoldings.getImprint() : "");
        //eHoldingsRecord.setStatusDate(eHoldings.getStatusDate() != null ? eHoldings.getStatusDate() : "");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        Timestamp statusDate = null;
        try {

            if (eHoldings.getStatusDate() != null) {
                statusDate = new Timestamp(sdf.parse(eHoldings.getStatusDate()).getTime());

            }
        } catch (Exception e) {
            LOG.info("Exception :", e);
        }
        eHoldingsRecord.setStatusDate(statusDate);
        eHoldingsRecord.seteResourceId(eHoldings.getEResourceId());
        eHoldingsRecord.seteResourceTitle(eHoldings.getEResourceTitle());
        eHoldingsRecord.setPublisher(eHoldings.getPublisher());
        eHoldingsRecord.setLocalPersistentLink(eHoldings.getLocalPersistentLink());

        if (eHoldings.getPlatform() != null) {
            eHoldingsRecord.setPlatform(eHoldings.getPlatform().getPlatformName());
            eHoldingsRecord.setAdminURL(eHoldings.getPlatform().getAdminUrl());
            eHoldingsRecord.setAdminPassword(eHoldings.getPlatform().getAdminPassword());
            eHoldingsRecord.setAdminUsername(eHoldings.getPlatform().getAdminUserName());
        }

        if (eHoldings.getCallNumber() != null) {
            CallNumber callNumber = eHoldings.getCallNumber();
            eHoldingsRecord.setCallNumberPrefix(callNumber.getPrefix());
            eHoldingsRecord.setCallNumber(callNumber.getNumber());
            if (callNumber.getShelvingOrder() != null) {
                eHoldingsRecord.setShelvingOrder(callNumber.getShelvingOrder().getFullValue());
            }
            CallNumberTypeRecord callNumberTypeRecord = saveCallNumberTypeRecord(callNumber.getCallNumberType());
            eHoldingsRecord.setCallNumberTypeId(callNumberTypeRecord == null ? null : callNumberTypeRecord.getCallNumberTypeId());

        }
        if (eHoldings.getExtentOfOwnership() != null) {
            if (eHoldings.getExtentOfOwnership().getCoverages() != null) {
                eHoldingsRecord.seteInstanceCoverageRecordList(saveCoverageRecord(eHoldings.getExtentOfOwnership().getCoverages(), eHoldingsRecord.geteHoldingsIdentifier()));
            }
            if (eHoldings.getExtentOfOwnership().getPerpetualAccesses() != null) {
                eHoldingsRecord.seteInstancePerpetualAccessRecordList(savePerpetualAccessRecord(eHoldings.getExtentOfOwnership().getPerpetualAccesses(), eHoldingsRecord.geteHoldingsIdentifier()));
            }
        }
        if (eHoldings.getNote() != null && eHoldings.getNote().size() > 0) {
            eHoldingsRecord.seteHoldingsNoteList(saveHoldingsNotes(eHoldings.getNote(), eHoldingsRecord.geteHoldingsIdentifier()));
        }
        if (eHoldings.getLink() != null) {
            eHoldingsRecord.setLink(eHoldings.getLink().getUrl());
            eHoldingsRecord.setLinkText(eHoldings.getLink().getText());
        }
        eHoldingsRecord.setiLLAllowed(eHoldings.isInterLibraryLoanAllowed());
        eHoldingsRecord.setStaffOnly(eHoldings.isStaffOnlyFlag());
        eHoldingsRecord.setStatisticalSearchCode(eHoldings.getStatisticalSearchingCode());
        eHoldingsRecord.setSubscriptionStatus(eHoldings.getSubscriptionStatus());
        getBusinessObjectService().save(eHoldingsRecord);
    }

    protected CallNumberTypeRecord saveCallNumberTypeRecord(CallNumberType scheme) {

        Map callMap = new HashMap();
        callMap.put("code", scheme.getCodeValue());
        List<CallNumberTypeRecord> callNumberTypeRecords = (List<CallNumberTypeRecord>) getBusinessObjectService().findMatching(CallNumberTypeRecord.class, callMap);
        if (callNumberTypeRecords.size() == 0) {
            if (scheme.getCodeValue() != null && !"".equals(scheme.getCodeValue())) {
                CallNumberTypeRecord callNumberTypeRecord = new CallNumberTypeRecord();
                callNumberTypeRecord.setCode(scheme.getCodeValue());
                callNumberTypeRecord.setName(scheme.getFullValue());
                getBusinessObjectService().save(callNumberTypeRecord);
                return callNumberTypeRecord;
            } else
                return null;
        }
        return callNumberTypeRecords.get(0);
    }

    protected String getLocation(Location location, StringBuffer locationLevel) {
        StringBuffer locationName = new StringBuffer("");
        //StringBuffer locationLevel = new StringBuffer("");
        if (location != null && location.getLocationLevel() != null) {
            locationName = locationName.append(location.getLocationLevel().getName());
            locationLevel = locationLevel.append(location.getLocationLevel().getLevel());

            if (location.getLocationLevel().getLocationLevel() != null) {
                locationName = locationName.append("/").append(location.getLocationLevel().getLocationLevel().getName());
                locationLevel = locationLevel.append("/").append(location.getLocationLevel().getLocationLevel().getLevel());

                if (location.getLocationLevel().getLocationLevel().getLocationLevel() != null) {
                    locationName = locationName.append("/").append(location.getLocationLevel().getLocationLevel().getLocationLevel().getName());
                    locationLevel = locationLevel.append("/").append(location.getLocationLevel().getLocationLevel().getLocationLevel().getLevel());

                    if (location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel() != null) {
                        locationName = locationName.append("/").append(location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getName());
                        locationLevel = locationLevel.append("/").append(location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getLevel());

                        if (location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel() != null) {
                            locationName = locationName.append("/").append(location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getName());
                            locationLevel = locationLevel.append("/").append(location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getLevel());
                        }
                    }
                }
            }
        }
        return locationName.toString();
    }

    protected void deleteEHoldingNChildRecords(EHoldingsRecord eHoldingsRecord) {
        String eHoldingsId = eHoldingsRecord.geteHoldingsIdentifier();

        Map eHoldingsMap = new HashMap();
        eHoldingsMap.put("eHoldingsIdentifier", eHoldingsId);

        getBusinessObjectService().deleteMatching(EInstanceCoverageRecord.class, eHoldingsMap);
        getBusinessObjectService().deleteMatching(EInstancePerpetualAccessRecord.class, eHoldingsMap);
        getBusinessObjectService().deleteMatching(EHoldingsNote.class, eHoldingsMap);
        getBusinessObjectService().delete(eHoldingsRecord);
    }

    public void addResourceId(RequestDocument linkedRequestDocument, ResponseDocument respDoc) {
        String eInstanceContent = linkedRequestDocument.getContent().getContent();
        InstanceCollection instanceCollection = new WorkEInstanceOlemlRecordProcessor().fromXML(eInstanceContent);
        EInstance eInstance = instanceCollection.getEInstance().get(0);
        eInstance.getResourceIdentifier().clear();
        eInstance.getResourceIdentifier().add(DocumentUniqueIDPrefix.getPrefixedId(DocumentUniqueIDPrefix.PREFIX_WORK_BIB_MARC, respDoc.getUuid()));
        linkedRequestDocument.getContent().setContent(new WorkEInstanceOlemlRecordProcessor().toXML(instanceCollection));
    }

}