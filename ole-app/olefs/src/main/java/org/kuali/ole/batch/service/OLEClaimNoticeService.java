package org.kuali.ole.batch.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.batch.bo.OLEBatchProcessJobDetailsBo;
import org.kuali.ole.batch.bo.OLEClaimNotice;
import org.kuali.ole.batch.form.OLEClaimNoticeForm;
import org.kuali.ole.deliver.batch.OleMailer;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.BibMarc;
import org.kuali.ole.docstore.discovery.service.QueryService;
import org.kuali.ole.docstore.discovery.service.QueryServiceImpl;
import org.kuali.ole.docstore.model.bo.WorkBibDocument;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.module.purap.businessobject.PurchaseOrderType;
import org.kuali.ole.select.bo.*;
import org.kuali.ole.select.businessobject.OleCopy;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.select.lookup.DocData;
import org.kuali.ole.select.service.OleTransmissionService;
import org.kuali.ole.service.impl.OLESerialReceivingService;
import org.kuali.ole.service.impl.OLESerialReceivingServiceImpl;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.vnd.businessobject.VendorDetail;
import org.kuali.ole.vnd.businessobject.VendorTransmissionFormatDetail;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.mail.EmailBody;
import org.kuali.rice.core.api.mail.EmailFrom;
import org.kuali.rice.core.api.mail.EmailSubject;
import org.kuali.rice.core.api.mail.EmailTo;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.coreservice.impl.parameter.ParameterBo;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.util.GlobalVariables;


import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 11/7/12
 * Time: 3:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEClaimNoticeService {

    private static final Logger LOG = Logger.getLogger(OLEClaimNoticeService.class);

    private BusinessObjectService businessObjectService;

    private int totalCount = 0;

    private int successRecord = 0;

    private DocstoreClientLocator docstoreClientLocator;

    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            return  SpringContext.getBean(BusinessObjectService.class);
        }
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (null == docstoreClientLocator) {
            return  SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    public void generateClaimReports(OLEBatchProcessJobDetailsBo job) {
        LOG.info("Start of scheduled job to execute generateClaimReports.");
      //  generateClaimReportFromSRR();
        List<OlePurchaseOrderItem> olePurchaseOrderItems = ( List<OlePurchaseOrderItem> )businessObjectService.findAll(OlePurchaseOrderItem.class);
        generateClaimReportFromPO(olePurchaseOrderItems,true);
        job.setTotalNoOfRecords(totalCount+"");
        job.setNoOfRecordsProcessed(totalCount+"");
        job.setNoOfSuccessRecords(successRecord + "");
        job.setNoOfFailureRecords((totalCount - successRecord) + "");
        job.setStatus(OLEConstants.OLEBatchProcess.JOB_STATUS_COMPLETED);
        job.setStatusDesc(OLEConstants.OLEBatchProcess.JOB_STATUS_COMPLETED);
    }

    public void generateClaimReportFromSRR(){
        try {
            LOG.info("Start of scheduled job to execute generateClaimReportFromSRR.");
            OLESerialReceivingService oleSerialReceivingService = new OLESerialReceivingServiceImpl();
            List<OLESerialReceivingDocument> oleSerialReceivingDocuments = ( List<OLESerialReceivingDocument> )businessObjectService.findAll(OLESerialReceivingDocument.class);
            for(OLESerialReceivingDocument oleSerialReceivingDocument : oleSerialReceivingDocuments){
                List<OLESerialReceivingHistory> oleSerialReceivingHistoryList = oleSerialReceivingDocument.getOleSerialReceivingHistoryList();
                totalCount = oleSerialReceivingHistoryList.size();
                for(OLESerialReceivingHistory oleSerialReceivingHistory : oleSerialReceivingHistoryList){
                    Map parentCriterial = new HashMap();
                    parentCriterial.put("serialReceivingIdentifier",oleSerialReceivingDocument.getSerialReceivingRecordId());
                    List<OleCopy> copyList = (List<OleCopy>) businessObjectService.findMatching(OleCopy.class,parentCriterial);
                    Boolean doNotClaim = false;
                    if(copyList.size()>0){
                        Integer poItemId = copyList.get(0).getPoItemId();
                        Map map = new HashMap();
                        map.put("itemIdentifier",poItemId);
                        OlePurchaseOrderItem olePurchaseOrderItem = businessObjectService.findByPrimaryKey(OlePurchaseOrderItem.class,map);
                        doNotClaim = olePurchaseOrderItem.isDoNotClaim();
                    }
                    if(oleSerialReceivingHistory.getReceiptStatus().equalsIgnoreCase(OLEConstants.CLAIMED) && !doNotClaim){
                        String emailAddress ="";
                        if (oleSerialReceivingDocument.getVendorId() != null && oleSerialReceivingDocument.getVendorId().length() > 0) {
                            String[] vendorDetails = oleSerialReceivingDocument.getVendorId().split("-");
                            String vendorHeaderGeneratedIdentifier = vendorDetails[0];
                            String vendorDetailAssignedIdentifier = vendorDetails[1];
                            Map vendorNameMap = new HashMap();
                            vendorNameMap.put(org.kuali.ole.sys.OLEConstants.VENDOR_HEADER_IDENTIFIER,vendorHeaderGeneratedIdentifier);
                            vendorNameMap.put(org.kuali.ole.sys.OLEConstants.VENDOR_DETAIL_IDENTIFIER,vendorDetailAssignedIdentifier);
                            List<VendorDetail> vendorDetailList = (List) businessObjectService.findMatching(VendorDetail.class, vendorNameMap);
                            if(vendorDetailList != null && vendorDetailList.size() >0) {
                                oleSerialReceivingDocument.setVendorName(vendorDetailList.get(0).getVendorName());
                                if( vendorDetailList.get(0).getVendorContacts()!=null &&  vendorDetailList.get(0).getVendorContacts().size()>0){
                                    emailAddress = vendorDetailList.get(0).getVendorContacts().get(0).getVendorContactEmailAddress();
                                }
                                if((emailAddress==null || emailAddress.isEmpty() )&& vendorDetailList.get(0).getVendorAddresses()!=null &&  vendorDetailList.get(0).getVendorAddresses().size()>0){
                                    emailAddress = vendorDetailList.get(0).getVendorAddresses().get(0).getVendorAddressEmailAddress();
                                }
                            }
                        }
                        LinkedHashMap<String, String> bibIdList = new LinkedHashMap<String, String>();
                        bibIdList.put(DocType.BIB.getDescription(), oleSerialReceivingDocument.getBibId());
                        List<WorkBibDocument> workBibDocuments = getWorkBibDocuments(bibIdList);
                        WorkBibDocument workBibDocument = workBibDocuments != null && workBibDocuments.size() > 0 ? workBibDocuments.get(0) : null;
                        oleSerialReceivingService.setEnumerationAndChronologyValues(oleSerialReceivingHistory);
                        DocumentService documentService= GlobalResourceLoader.getService(OLEConstants.DOCUMENT_HEADER_SERVICE);
                        MaintenanceDocument maintenanceDocument = (MaintenanceDocument) documentService.getNewDocument("OLE_CLM_NTC");
                        OLEClaimNoticeBo oleClaimNoticeBo = (OLEClaimNoticeBo) maintenanceDocument.getDocumentDataObject();
                        oleClaimNoticeBo.setNameOfTheSender(oleSerialReceivingDocument.getBoundLocation());
                        oleClaimNoticeBo.setNameOfTheVendor(oleSerialReceivingDocument.getVendorName());
                        oleClaimNoticeBo.setClaimDate(oleSerialReceivingHistory.getClaimDate() != null ? oleSerialReceivingHistory.getClaimDate().toString() : "");
                        oleClaimNoticeBo.setClaimCount(oleSerialReceivingHistory.getClaimCount());
                        oleClaimNoticeBo.setClaimType(oleSerialReceivingHistory.getClaimType());
                        oleClaimNoticeBo.setTitle(oleSerialReceivingDocument.getTitle());
                        if(workBibDocument!=null){
                            oleClaimNoticeBo.setPlaceOfPublication(workBibDocument.getPublicationPlaces()!=null && workBibDocument.getPublicationPlaces().size()>0?
                            workBibDocument.getPublicationPlaces().get(0).toString():"");
                            oleClaimNoticeBo.setPublicationDate(workBibDocument.getPublicationDate());
                        }

                        oleClaimNoticeBo.setPublication(oleSerialReceivingDocument.getPublisher());
                        oleClaimNoticeBo.setEnumeration(oleSerialReceivingHistory.getEnumerationCaption());
                        oleClaimNoticeBo.setChronology(oleSerialReceivingHistory.getChronologyCaption());
                        oleClaimNoticeBo.setVendorsLibraryAcctNum("");
                        oleClaimNoticeBo.setVendorOrderNumber("");
                        oleClaimNoticeBo.setVendorTitleNumber("");
                        oleClaimNoticeBo.setLibraryPurchaseOrderNumber(oleSerialReceivingDocument.getPoId());
                        oleClaimNoticeBo.setUnboundLocation(oleSerialReceivingDocument.getUnboundLocation());
                        oleClaimNoticeBo.setMailAddress(emailAddress);
                        oleClaimNoticeBo.setActive(true);
                        Date now = CoreApiServiceLocator.getDateTimeService().getCurrentSqlDate();
                        maintenanceDocument.getDocumentHeader().setDocumentDescription("Claim Report"+"[date:"+now+"]");
                        maintenanceDocument.getNewMaintainableObject().setDataObject(oleClaimNoticeBo);
                        documentService.routeDocument(maintenanceDocument,null,null);
                        successRecord++;
                    }
                }
            }
        } catch (Exception ex) {
            LOG.error("Exception occurred while performing generateClaimReportFromSRR", ex);
        }
    }

    public void generateClaimReportFromPO(List<OlePurchaseOrderItem> olePurchaseOrderItems,boolean isBatchMode){
        try {
            LOG.info("Start of scheduled job to execute generateClaimReportFromPO.");
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            totalCount += olePurchaseOrderItems.size();
            HashMap<String,List<OLEClaimingByTitle>> hashMap = new HashMap<>();
            List<OLEClaimingByTitle> claimTitles = null;
            for(OlePurchaseOrderItem olePurchaseOrderItem : olePurchaseOrderItems){
                    if(!olePurchaseOrderItem.isDoNotClaim()){
                        String emailAddress ="";
                        String claimInterval="";
                        OlePurchaseOrderDocument olePurchaseOrderDocument = olePurchaseOrderItem.getPurapDocument();
                        boolean validPO = olePurchaseOrderDocument!=null ? olePurchaseOrderDocument.getPurchaseOrderCurrentIndicatorForSearching() : false;
                        boolean serialPOLink = olePurchaseOrderItem.getCopyList()!=null && olePurchaseOrderItem.getCopyList().size()>0 ? olePurchaseOrderItem.getCopyList().get(0).getSerialReceivingIdentifier()!=null : false ;
                        Map purchaseOrderTypeIdMap = new HashMap();
                        purchaseOrderTypeIdMap.put("purchaseOrderTypeId", olePurchaseOrderDocument.getPurchaseOrderTypeId());
                        org.kuali.rice.krad.service.BusinessObjectService businessObject = SpringContext.getBean(org.kuali.rice.krad.service.BusinessObjectService.class);
                        List<PurchaseOrderType> purchaseOrderTypeDocumentList = (List) businessObject.findMatching(PurchaseOrderType.class, purchaseOrderTypeIdMap);
                        boolean  continuing = purchaseOrderTypeDocumentList!=null && purchaseOrderTypeDocumentList.size()>0?
                                purchaseOrderTypeDocumentList.get(0).getPurchaseOrderType().equalsIgnoreCase("Continuing"):false;
                        if(!serialPOLink && !continuing && validPO){
                            VendorDetail vendorDetail =olePurchaseOrderDocument.getVendorDetail();
                            Date date = new Date();
                            String dateString = dateFormat.format(date);
                            Date actionDate=olePurchaseOrderItem.getClaimDate();
                            String actionDateString = actionDate!=null ? dateFormat.format(actionDate) : "";
                            if( vendorDetail.getVendorContacts()!=null &&  vendorDetail.getVendorContacts().size()>0){
                                emailAddress = vendorDetail.getVendorContacts().get(0).getVendorContactEmailAddress();
                                claimInterval = vendorDetail.getClaimInterval();
                            }
                            if((emailAddress==null || emailAddress.isEmpty() ) && vendorDetail.getVendorAddresses()!=null &&  vendorDetail.getVendorAddresses().size()>0){
                                emailAddress = vendorDetail.getVendorAddresses().get(0).getVendorAddressEmailAddress();
                            }
                            if(actionDate!=null && (actionDateString.equals(dateString) || actionDate.before(date))){
                                if(!isBatchMode){
                                    for(OlePurchaseOrderItem purchaseOrderItem : (List<OlePurchaseOrderItem>)olePurchaseOrderDocument.getItems()){
                                           if(purchaseOrderItem.getItemIdentifier().equals(olePurchaseOrderItem.getItemIdentifier())){
                                               if(purchaseOrderItem.getClaimCount()!=null){
                                                   Integer count = purchaseOrderItem.getClaimCount().intValue();
                                                   count++;
                                                   purchaseOrderItem.setClaimCount(new KualiInteger(count));
                                               }else {
                                                   purchaseOrderItem.setClaimCount(new KualiInteger(1));
                                               }
                                               break;
                                           }
                                    }
                                }
                                if (hashMap.containsKey(olePurchaseOrderDocument.getVendorName())) {
                                    claimTitles = hashMap.get(olePurchaseOrderDocument.getVendorName());
                                    OLEClaimingByTitle oleClaimingByTitle = new OLEClaimingByTitle();
                                    updateOleClaimingByTitle(oleClaimingByTitle,olePurchaseOrderItem,olePurchaseOrderDocument);
                                    oleClaimingByTitle.setEmailAddress(emailAddress);
                                    claimTitles.add(oleClaimingByTitle);
                                    hashMap.put(olePurchaseOrderDocument.getVendorName(),claimTitles);
                                }
                                else {
                                    claimTitles = new ArrayList<>();
                                    OLEClaimingByTitle oleClaimingByTitle = new OLEClaimingByTitle();
                                    updateOleClaimingByTitle(oleClaimingByTitle,olePurchaseOrderItem,olePurchaseOrderDocument);
                                    oleClaimingByTitle.setEmailAddress(emailAddress);
                                    claimTitles.add(oleClaimingByTitle);
                                    hashMap.put(olePurchaseOrderDocument.getVendorName(),claimTitles);
                                }
                                OLEPOClaimHistory claimHistory = new OLEPOClaimHistory();
                                claimHistory.setClaimCount(olePurchaseOrderItem.getClaimCount() != null ? olePurchaseOrderItem.getClaimCount().intValue() : 0);
                                claimHistory.setClaimDate(new java.sql.Date(System.currentTimeMillis()));
                                claimHistory.setReqItemId(olePurchaseOrderItem.getCopyList().get(0).getReqItemId());
                                claimHistory.setOperator(GlobalVariables.getUserSession().getPrincipalName());
                                olePurchaseOrderItem.getClaimHistories().add(claimHistory);

                                if (StringUtils.isNotBlank(claimInterval)) {
                                    boolean actIntvlFlag = isNumber(claimInterval);
                                    if (actIntvlFlag) {
                                        Integer actIntvl = Integer.parseInt(claimInterval);
                                        actionDate = DateUtils.addDays(new java.util.Date(), actIntvl);
                                        olePurchaseOrderItem.setClaimDate(new java.sql.Date(actionDate.getTime()));
                                    }
                                } else{
                                    olePurchaseOrderItem.setClaimDate(null);
                                }

                                businessObjectService.save(olePurchaseOrderItem);
                            }
                        }
                    }
                }
            Iterator<Map.Entry<String,List<OLEClaimingByTitle>>> entries = hashMap.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String,List<OLEClaimingByTitle>> entry = entries.next();
                List<OLEClaimingByTitle> oleClaimingByTitles = entry.getValue();
                OLEClaimingByVendor oleClaimingByVendor = new OLEClaimingByVendor();
                oleClaimingByVendor.setVendorName(entry.getKey());
                if(oleClaimingByTitles!=null && oleClaimingByTitles.size()>0){
                    oleClaimingByVendor.setFromAddress(oleClaimingByTitles.get(0).getFromAddress());
                    oleClaimingByVendor.setToAddress(oleClaimingByTitles.get(0).getToAddress());
                    oleClaimingByVendor.setVendorTransmissionFormatDetail(oleClaimingByTitles.get(0).getVendorTransmissionFormatDetail());
                    oleClaimingByVendor.setEmailAddress(oleClaimingByTitles.get(0).getEmailAddress());
                }
                oleClaimingByVendor.setOleClaimingByTitles(oleClaimingByTitles);
                generatePdf(oleClaimingByVendor);
                successRecord++;
            }
        } catch (Exception ex) {
            LOG.error("Exception occurred while performing generateClaimReportFromPO", ex);
        }
    }

    public static boolean isNumber(String actionInterval) {
        String actStr = actionInterval;
        for (int i = 0; i < actStr.length(); i++) {
            if (!Character.isDigit(actStr.charAt(i)))
                return false;
        }
        return true;
    }

    private List<WorkBibDocument> getWorkBibDocuments(LinkedHashMap<String, String> bibIdList) {
        List<LinkedHashMap<String, String>> bibIdMapList = new ArrayList<LinkedHashMap<String, String>>();
        bibIdMapList.add(bibIdList);
        QueryService queryService = QueryServiceImpl.getInstance();
        List<WorkBibDocument> workBibDocuments = new ArrayList<WorkBibDocument>();
        try {
            workBibDocuments = queryService.getWorkBibRecords(bibIdMapList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return workBibDocuments;
    }

    public void generatePdf(OLEClaimingByVendor oleClaimingByVendor) throws Exception {
        Document document = new Document(PageSize.A4);
        String pdfLocationSystemParam = getParameter(OLEParameterConstants.PDF_LOCATION);
        if (LOG.isInfoEnabled()) {
            LOG.info("System Parameter for PDF_Location --> " + pdfLocationSystemParam);
        }
        if (pdfLocationSystemParam == null || pdfLocationSystemParam.trim().isEmpty()) {
            pdfLocationSystemParam = ConfigContext.getCurrentContextConfig().getProperty("staging.directory") + "/";
            if (LOG.isInfoEnabled()) {
                LOG.info("System Parameter for PDF_Location staging dir--> " + pdfLocationSystemParam);
            }
        } else {
            pdfLocationSystemParam = ConfigContext.getCurrentContextConfig().getProperty("homeDirectory") + "/" + pdfLocationSystemParam + "/";
        }
        boolean locationExist = new File(pdfLocationSystemParam).exists();
        boolean fileCreated = false;
        if (LOG.isInfoEnabled()) {
            LOG.info("Is directory Exist ::" + locationExist);
        }
        try {
            if (!locationExist) {
                fileCreated = new File(pdfLocationSystemParam).mkdirs();
                if (!fileCreated) {
                    throw new RuntimeException("Not Able to create directory :" + pdfLocationSystemParam);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception occured while creating the directory : " + e.getMessage(), e);
            throw e;
        }

        String title=oleClaimingByVendor.getVendorName();
        if (title == null || title .trim().isEmpty()) {
            title = OLEConstants.ITEM_TITLE;
        }
        String fileName = pdfLocationSystemParam + title + "_" + OLEConstants.CLAIM_NOTICE+ "_" +new Date(System.currentTimeMillis()) + ".pdf";
        fileName = fileName.replace(" ","_");
        if (LOG.isInfoEnabled()) {
            LOG.info("File Created :" + title + "file name ::" + fileName + "::");
        }
        OutputStream outputStream = new FileOutputStream(fileName);
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        Font boldFont = new Font(Font.TIMES_ROMAN, 15, Font.BOLD);
        Font ver_15_normal = FontFactory.getFont("VERDANA", 15, 0);
        document.open();
        document.newPage();
        Paragraph paraGraph = new Paragraph();
        String institutionName = oleClaimingByVendor.getFromAddress().getInstitutionName();
        paraGraph.setAlignment(Element.ALIGN_CENTER);
        paraGraph.add(new Chunk("Purchasing Institution Name : ", boldFont));
        paraGraph.add(new Chunk(institutionName, boldFont));
        paraGraph.add(Chunk.NEWLINE);
        paraGraph.add(Chunk.NEWLINE);
        document.add(paraGraph);
        float[] infoWidths = {0.50f, 0.50f};
        PdfPTable pdfTable = new PdfPTable(infoWidths);
        pdfTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfTable.setWidthPercentage(100);

        Paragraph toPara = new Paragraph();
        toPara.add(new Chunk("To : "));
        toPara.add(Chunk.NEWLINE);
        if(oleClaimingByVendor.getToAddress() != null){
            OLEClaimingAddress toAddress = oleClaimingByVendor.getToAddress();
            updateAddress(toAddress,toPara);
        }
        PdfPCell pdfPCell = new PdfPCell(toPara);
        pdfPCell.setBorder(pdfPCell.BOX);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
        pdfTable.addCell(pdfPCell);


        Paragraph fromPara = new Paragraph();
        fromPara.add(new Chunk("From : "));
        fromPara.add(Chunk.NEWLINE);
        if(oleClaimingByVendor.getFromAddress() != null){
            OLEClaimingAddress fromAddress = oleClaimingByVendor.getFromAddress();
            updateAddress(fromAddress,fromPara);
        }
        pdfPCell = new PdfPCell(fromPara);
        pdfPCell.setBorder(pdfPCell.BOX);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
        pdfTable.addCell(pdfPCell);
        pdfTable.addCell(pdfPCell);

        document.add(pdfTable);

        paraGraph = new Paragraph();
        paraGraph.add(Chunk.NEWLINE);
        document.add(paraGraph);

        paraGraph = new Paragraph();
        paraGraph.add(new Chunk("The following ordered titles have not yet been received by our library.  Please supply or report. thank you."));
        document.add(paraGraph);

        paraGraph = new Paragraph();
        paraGraph.add(Chunk.NEWLINE);
        document.add(paraGraph);

        int count = 1;
        for (OLEClaimingByTitle claimingByTitle : oleClaimingByVendor.getOleClaimingByTitles()) {
            paraGraph = new Paragraph();
            paraGraph.setAlignment(Element.ALIGN_LEFT);
            paraGraph.add(new Chunk("Title" + count++, boldFont));
            paraGraph.add(Chunk.NEWLINE);
            document.add(paraGraph);

            pdfTable = new PdfPTable(3);

            pdfPCell = new PdfPCell(new Paragraph("Author"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(claimingByTitle.getAuthor() !=null ? claimingByTitle.getAuthor() : "")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);

            pdfPCell = new PdfPCell(new Paragraph("Title"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(claimingByTitle.getTitle()!=null ? claimingByTitle.getTitle() : "")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);

            pdfPCell = new PdfPCell(new Paragraph("Place Of Publication"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(claimingByTitle.getPlaceOfPublication() != null ? claimingByTitle.getPlaceOfPublication() : "")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);

            pdfPCell = new PdfPCell(new Paragraph("Publisher"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(claimingByTitle.getPublisherName() != null ? claimingByTitle.getPublisherName() : "")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);

            pdfPCell = new PdfPCell(new Paragraph("Publication Date"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(claimingByTitle.getPublicationDate() != null ? claimingByTitle.getPublicationDate()  : "")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);

            pdfPCell = new PdfPCell(new Paragraph("ISXN"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(claimingByTitle.getIsxn() != null ? claimingByTitle.getIsxn() : "")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);

            pdfPCell = new PdfPCell(new Paragraph("Vendor Item Identifier"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(claimingByTitle.getVendorItemIdentifier() != null ? claimingByTitle.getVendorItemIdentifier() : "")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);

            pdfPCell = new PdfPCell(new Paragraph("PO #"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(claimingByTitle.getPoOrderedNum() != null ? claimingByTitle.getPoOrderedNum() : "")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);

            pdfPCell = new PdfPCell(new Paragraph("PO Date"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(claimingByTitle.getPoOrderedDate() != null ? claimingByTitle.getPoOrderedDate() : "")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);

            pdfPCell = new PdfPCell(new Paragraph("Claim #"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(claimingByTitle.getClaimNumber() != null ? claimingByTitle.getClaimNumber() : "")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);

            pdfPCell = new PdfPCell(new Paragraph("Claim Note"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(claimingByTitle.getClaimNote() != null ? claimingByTitle.getClaimNote() : "")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);

            document.add(pdfTable);
            paraGraph = new Paragraph();
            paraGraph.add(Chunk.NEWLINE);
            document.add(paraGraph);
        }

        outputStream.flush();
        document.close();
        outputStream.close();

        OleTransmissionService transmissionService = SpringContext.getBean(OleTransmissionService.class);
        VendorTransmissionFormatDetail vendorTransmissionFormatDetail = oleClaimingByVendor.getVendorTransmissionFormatDetail();
        if(vendorTransmissionFormatDetail!=null){
            String fileNam = "Claim Notice "+vendorTransmissionFormatDetail.getVendorTransmissionTypes().getVendorTransmissionType() + new Date(System.currentTimeMillis()) + ".pdf";
            if (vendorTransmissionFormatDetail.getVendorTransmissionTypes().getVendorTransmissionType().equalsIgnoreCase("SFTP")) {
                transmissionService.doSFTPUpload(vendorTransmissionFormatDetail, fileName, fileNam);
            } else if (vendorTransmissionFormatDetail.getVendorTransmissionTypes().getVendorTransmissionType().equalsIgnoreCase("FTP")) {
                transmissionService.doFTPUpload(vendorTransmissionFormatDetail, fileName, fileNam);
            } else if (vendorTransmissionFormatDetail.getVendorTransmissionTypes().getVendorTransmissionType().equalsIgnoreCase("Email") && oleClaimingByVendor.getEmailAddress()!=null && !oleClaimingByVendor.getEmailAddress().isEmpty()) {
                OleMailer oleMail = GlobalResourceLoader.getService("oleMailer");
                //oleMail.sendEmail(new EmailFrom(getParameter(OLEParameterConstants.NOTICE_FROM_ADDRESS)), new EmailTo(oleClaimingByVendor.getEmailAddress()), new EmailSubject("Claim Notice"), new EmailBody(mailContent(oleClaimingByVendor)), true);
                List fileNameList = new ArrayList();
                if(fileName != null){
                    fileNameList.add(fileName);
                }
                oleMail.SendEMail(oleClaimingByVendor.getEmailAddress(),getParameter(OLEParameterConstants.NOTICE_FROM_ADDRESS),fileNameList,OLEConstants.CLAIM_MAIL_SUBJECT,OLEConstants.CLAIM_MAIL_MSSG_BODY);
                LOG.info("Mail send successfully to " + oleClaimingByVendor.getEmailAddress());
            }
        }
    }

    private Paragraph updateAddress(OLEClaimingAddress address,Paragraph para){
        if(address.getVendorName() != null){
            para.add(new Chunk(address.getVendorName()));
            para.add(Chunk.NEWLINE);
        }
        if(address.getAttention() != null){
            para.add(new Chunk("ATTN: "));
            para.add(new Chunk(address.getAttention()));
            para.add(Chunk.NEWLINE);
        }
        if(address.getAddress1() != null){
            para.add(new Chunk(address.getAddress1()));
            para.add(Chunk.NEWLINE);
        }
        if(address.getAddress2() != null){
            para.add(new Chunk(address.getAddress2()));
            para.add(Chunk.NEWLINE);
        }
        if(address.getCity() != null){
            para.add(new Chunk(address.getCity()));
            if (address.getState() != null || address.getPostalCode() != null || address.getProvince() != null) {
                para.add(new Chunk(", "));
            } else {
                para.add(Chunk.NEWLINE);
            }
        }
        if(address.getState() != null){
            para.add(new Chunk(address.getState()));
            if (address.getPostalCode() != null) {
                para.add(new Chunk(" "));
            } else {
                para.add(Chunk.NEWLINE);
            }
        }
        if(address.getPostalCode() != null){
            para.add(new Chunk(address.getPostalCode()));
            if (address.getProvince() != null) {
                para.add(new Chunk(", "));
            } else {
                para.add(Chunk.NEWLINE);
            }
        }
        if(address.getProvince() != null){
            para.add(new Chunk(address.getProvince()));
            para.add(Chunk.NEWLINE);
        }
        if(address.getCountry() != null){
            para.add(new Chunk(address.getCountry()));
            para.add(Chunk.NEWLINE);
        }
        return para;
    }
    public void updateOleClaimingByTitle(OLEClaimingByTitle oleClaimingByTitle,OlePurchaseOrderItem olePurchaseOrderItem,OlePurchaseOrderDocument olePurchaseOrderDocument){
        Bib bib =  new BibMarc();
        if (olePurchaseOrderItem!=null && olePurchaseOrderItem.getItemTitleId() != null && olePurchaseOrderItem.getItemTitleId() != "") {
            try {
                oleClaimingByTitle.setClaimNumber(olePurchaseOrderItem.getClaimCount()!=null ? olePurchaseOrderItem.getClaimCount().toString() : "");
                oleClaimingByTitle.setVendorItemIdentifier(olePurchaseOrderItem.getVendorItemPoNumber()!=null? olePurchaseOrderItem.getVendorItemPoNumber() : "");
                bib = getDocstoreClientLocator().getDocstoreClient().retrieveBib(olePurchaseOrderItem.getItemTitleId());
                if(bib!=null){
                    oleClaimingByTitle.setAuthor(bib.getAuthor());
                    oleClaimingByTitle.setTitle(bib.getTitle());
                    oleClaimingByTitle.setPlaceOfPublication(bib.getPublisher());
                    oleClaimingByTitle.setPublicationDate(bib.getPublicationDate());
                    oleClaimingByTitle.setPublisherName(bib.getPublisher());
                    String isbn = bib.getIsbn()!=null ? bib.getIsbn() : "" ;
                    String issn  =bib.getIssn()!=null ? bib.getIssn() : "";
                    oleClaimingByTitle.setIsxn(isbn + issn);
                }
            }catch (Exception e){
                e.printStackTrace();
                LOG.error(e.getMessage());
            }
        }
        if(olePurchaseOrderDocument!=null){
            oleClaimingByTitle.setPoOrderedNum(olePurchaseOrderDocument.getPurapDocumentIdentifier()!=null ?
                    olePurchaseOrderDocument.getPurapDocumentIdentifier().toString() : "");
            oleClaimingByTitle.setPoOrderedDate(olePurchaseOrderDocument.getPurchaseOrderInitialOpenTimestamp()!=null ?
                    olePurchaseOrderDocument.getPurchaseOrderInitialOpenTimestamp().toString() : "");
            OLEClaimingAddress toAddress = updateClaimingToAddress(olePurchaseOrderDocument);
            OLEClaimingAddress fromAddress = updateClaimingFromAddress(olePurchaseOrderDocument);
            oleClaimingByTitle.setToAddress(toAddress);
            oleClaimingByTitle.setFromAddress(fromAddress);
            if (olePurchaseOrderDocument.getVendorDetail() != null) {
                List<VendorTransmissionFormatDetail> vendorTxFormat = olePurchaseOrderDocument.getVendorDetail().getVendorTransmissionFormat();
                if (vendorTxFormat.size() > 0) {
                    for (int i = 0; i < vendorTxFormat.size(); i++) {
                        VendorTransmissionFormatDetail vendorTransmissionFormatDetail = vendorTxFormat.get(i);
                        boolean isPrefferedTransmissionFormat = vendorTransmissionFormatDetail.isVendorPreferredTransmissionFormat();
                        if (isPrefferedTransmissionFormat ) {
                            oleClaimingByTitle.setVendorTransmissionFormatDetail(vendorTransmissionFormatDetail);
                            break;
                        }
                    }
                }

            }
        }
    }

    public String mailContent(OLEClaimingByVendor oleClaimingByVendor){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<HTML>");
        stringBuffer.append("<VENDOR><h1>" + oleClaimingByVendor.getVendorName() + "</h1></VENDOR>");
        stringBuffer.append("<HEAD></HEAD>");
        stringBuffer.append("<BODY>");

        int i=1;
        for(OLEClaimingByTitle claimingByTitle : oleClaimingByVendor.getOleClaimingByTitles()){
            stringBuffer.append("<table>");
            stringBuffer.append("<h3>Title"+i+++"</h3>");
            stringBuffer.append("<TR><TD>Author :</TD><TD>" + (claimingByTitle.getAuthor() !=null ? claimingByTitle.getAuthor() : "") + "</TD></TR>");
            stringBuffer.append("<TR><TD>Title :</TD><TD>" + (claimingByTitle.getTitle()!=null ? claimingByTitle.getTitle() : "") + "</TD></TR>");
            stringBuffer.append("<TR><TD>Place Of Publication :</TD><TD>" + (claimingByTitle.getPlaceOfPublication() != null ? claimingByTitle.getPlaceOfPublication() : "") + "</TD></TR>");
            stringBuffer.append("<TR><TD>Publisher :</TD><TD>" + (claimingByTitle.getPublisherName() != null ? claimingByTitle.getPublisherName() : "") + "</TD></TR>");
            stringBuffer.append("<TR><TD>Publication Date :</TD><TD>" + ( claimingByTitle.getPublicationDate() != null ? claimingByTitle.getPublicationDate()  : "" )+ "</TD></TR>");
            stringBuffer.append("<TR><TD>ISXN :</TD><TD>" + ( claimingByTitle.getIsxn() != null ? claimingByTitle.getIsxn() : "" )+ "</TD></TR>");
            stringBuffer.append("<TR><TD>Vendor Item Identifier :</TD><TD>" + ( claimingByTitle.getVendorItemIdentifier() != null ? claimingByTitle.getVendorItemIdentifier() : "" )+ "</TD></TR>");
            stringBuffer.append("<TR><TD>PO # :</TD><TD>" + ( claimingByTitle.getPoOrderedNum() != null ? claimingByTitle.getPoOrderedNum() : "" )+ "</TD></TR>");
            stringBuffer.append("<TR><TD>PO Date :</TD><TD>" + ( claimingByTitle.getPublicationDate() != null ? claimingByTitle.getPublicationDate()  : "" )+ "</TD></TR>");
            stringBuffer.append("<TR><TD>Claim # :</TD><TD>" + ( claimingByTitle.getClaimNumber() != null ? claimingByTitle.getClaimNumber() : "" )+ "</TD></TR>");
            stringBuffer.append("<TR><TD>Claim Note :</TD><TD>" + ( claimingByTitle.getClaimNote() != null ? claimingByTitle.getClaimNote() : "" )+ "</TD></TR>");

            stringBuffer.append("<TR><TD>&nbsp;</TD><TD>&nbsp;</TD></TR>");
            stringBuffer.append("</table>");
        }
        return stringBuffer.toString();
    }


    public OLEClaimNoticeForm populateOLEClaimNoticeForm(OLEClaimNoticeForm oleClaimNoticeForm){
        List<File> claimNoticeList = new ArrayList<File>();
        String pdfLocationSystemParam = getParameter(OLEParameterConstants.PDF_LOCATION);
        if (pdfLocationSystemParam == null || pdfLocationSystemParam.trim().isEmpty()) {
            pdfLocationSystemParam = ConfigContext.getCurrentContextConfig().getProperty("staging.directory") + "/";
        } else{
            pdfLocationSystemParam = ConfigContext.getCurrentContextConfig().getProperty("homeDirectory")+ "/" + pdfLocationSystemParam +"/";
        }
        LOG.info("PDF LOCATION : " + pdfLocationSystemParam);
        File directory = new File(pdfLocationSystemParam);
        File[] fList = directory.listFiles();
        if (fList != null && fList.length > 0) {
            for (File file : fList) {
                if (file.isFile()) {

                   if (file.getName().contains(OLEConstants.CLAIM_NOTICE)) {
                        claimNoticeList.add(file);
                   }
                }
            }
        }
        oleClaimNoticeForm.setOleClaimNoticeList(generateClaimNoticeList(claimNoticeList));
        return oleClaimNoticeForm;
    }

    public List<OLEClaimNotice> generateClaimNoticeList(List<File> claimNoticeList) {
        List<OLEClaimNotice>  oleClaimNotices = new ArrayList<OLEClaimNotice>();
        OLEClaimNotice oleClaimNotice ;
        for(File file :claimNoticeList){
            oleClaimNotice= new OLEClaimNotice();
            oleClaimNotice.setFileName(file.getName());
            oleClaimNotice.setFileLocation(file.getAbsolutePath());
            oleClaimNotices.add(oleClaimNotice);
        }
        LOG.info("No of OLEClaimNotice : " +oleClaimNotices.size());
        return oleClaimNotices;
    }

   public String getParameter(String name){
       ParameterKey parameterKey = ParameterKey.create(OLEConstants.APPL_ID, OLEConstants.SELECT_NMSPC, OLEConstants.SELECT_CMPNT,name);
       Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
       return parameter!=null?parameter.getValue():null;
   }

    public OLEClaimingAddress updateClaimingToAddress(OlePurchaseOrderDocument olePurchaseOrderDocument){
        OLEClaimingAddress oleClaimingAddress = new OLEClaimingAddress();
        oleClaimingAddress.setVendorName(olePurchaseOrderDocument.getVendorName());
        oleClaimingAddress.setAttention(olePurchaseOrderDocument.getVendorAttentionName());
        oleClaimingAddress.setAddress1(olePurchaseOrderDocument.getVendorLine1Address());
        oleClaimingAddress.setAddress2(olePurchaseOrderDocument.getVendorLine2Address());
        oleClaimingAddress.setCity(olePurchaseOrderDocument.getVendorCityName());
        oleClaimingAddress.setPostalCode(olePurchaseOrderDocument.getVendorPostalCode());
        String vendorHeaderGeneratedIdentifier = olePurchaseOrderDocument.getVendorHeaderGeneratedIdentifier().toString();
        String vendorDetailAssignedIdentifier = olePurchaseOrderDocument.getVendorDetailAssignedIdentifier().toString();
        Map vendorNameMap = new HashMap();
        vendorNameMap.put(org.kuali.ole.sys.OLEConstants.VENDOR_HEADER_IDENTIFIER,vendorHeaderGeneratedIdentifier);
        vendorNameMap.put(org.kuali.ole.sys.OLEConstants.VENDOR_DETAIL_IDENTIFIER,vendorDetailAssignedIdentifier);
        VendorDetail vendorDetail = getBusinessObjectService().findByPrimaryKey(VendorDetail.class, vendorNameMap);
        if(vendorDetail != null) {
            if(vendorDetail.getVendorHeader().getVendorForeignIndicator()){
                if (olePurchaseOrderDocument.getVendorCountry()!=null) {
                    if (StringUtils.isNotBlank(olePurchaseOrderDocument.getVendorCountry().getName())) {
                        oleClaimingAddress.setCountry(olePurchaseOrderDocument.getVendorCountry().getName());
                    }
                }
                for (int vendorAddress=0; vendorAddress<vendorDetail.getVendorAddresses().size(); vendorAddress++) {
                    if (vendorDetail.getVendorAddresses().get(vendorAddress).getVendorAddressType().getVendorDefaultIndicator()
                            && StringUtils.isNotBlank(vendorDetail.getVendorAddresses().get(vendorAddress).getVendorAddressInternationalProvinceName())) {
                        oleClaimingAddress.setProvince(vendorDetail.getVendorAddresses().get(vendorAddress).getVendorAddressInternationalProvinceName());
                    }
                }
            } else {
                if (StringUtils.isNotBlank(olePurchaseOrderDocument.getVendorStateCode())) {
                    oleClaimingAddress.setState(olePurchaseOrderDocument.getVendorStateCode());
                }
            }
        }
        return oleClaimingAddress;
    }

    public OLEClaimingAddress updateClaimingFromAddress(OlePurchaseOrderDocument olePurchaseOrderDocument){
        OLEClaimingAddress oleClaimingAddress = new OLEClaimingAddress();
        oleClaimingAddress.setAddress1(olePurchaseOrderDocument.getDeliveryBuildingLine1Address());
        oleClaimingAddress.setAddress2(olePurchaseOrderDocument.getDeliveryBuildingLine2Address());
        oleClaimingAddress.setCity(olePurchaseOrderDocument.getDeliveryCityName());
        oleClaimingAddress.setState(olePurchaseOrderDocument.getDeliveryStateCode());
        oleClaimingAddress.setPostalCode(olePurchaseOrderDocument.getDeliveryPostalCode());
        oleClaimingAddress.setInstitutionName(olePurchaseOrderDocument.getDeliveryCampus() != null ? olePurchaseOrderDocument.getDeliveryCampus().getPurchasingInstitutionName() : null);
        return oleClaimingAddress;
    }
}