package org.kuali.ole.deliver.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.service.OlePatronHelperService;
import org.kuali.ole.service.OlePatronHelperServiceImpl;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Created by premkb on 4/8/15.
 */
public class NoticePDFContentFormatter {

    private static final Logger LOG = Logger.getLogger(NoticePDFContentFormatter.class);
    private CircDeskLocationResolver circDeskLocationResolver;
    private SimpleDateFormat simpleDateFormat;
    private String urlProperty;
    private ParameterValueResolver parameterResolverInstance;
    private OlePatronHelperServiceImpl olePatronHelperService;

    public OlePatronHelperService getOlePatronHelperService(){
        if(olePatronHelperService==null)
            olePatronHelperService=new OlePatronHelperServiceImpl();
        return olePatronHelperService;
    }

    public void setOlePatronHelperService(OlePatronHelperServiceImpl olePatronHelperService) {
        this.olePatronHelperService = olePatronHelperService;
    }

    public void setUrlProperty(String urlProperty) {
        this.urlProperty = urlProperty;
    }

    public String generateOverDueNoticeContent(OleLoanDocument oleLoanDocument)
            throws Exception {
        Document document = new Document(PageSize.A4);
        String fileName = createPDFFile(getParameterResolverInstance().getParameter(OLEConstants
                .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT,OLEParameterConstants.OVERDUE_TITLE), oleLoanDocument.getItemId());
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        PdfWriter.getInstance(document, fileOutputStream);
        document.open();
        String title = getParameterResolverInstance().getParameter(OLEConstants
                .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT,OLEParameterConstants.OVERDUE_TITLE);
        String body = getParameterResolverInstance().getParameter(OLEConstants
                .APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT,OLEConstants.OleDeliverRequest.OVERDUE_NOTICE_CONTENT);
        addPdfHeader(document, title);
        addPatronInfoToDocument(document, oleLoanDocument.getOlePatron(), title, body);
        addOverdueNoticeInfoToDocument(oleLoanDocument, document);
        addPdfFooter(document);
        document.close();
        return fileName;
    }

    private void addPatronInfoToDocument(Document document, OlePatronDocument olePatronDocument, String title, String body)
            throws
            Exception {

        EntityTypeContactInfoBo entityTypeContactInfoBo = olePatronDocument.getEntity().getEntityTypeContactInfos().get(0);
        Paragraph paraGraph = new Paragraph();
        paraGraph.add(Chunk.NEWLINE);
        document.add(paraGraph);
        paraGraph = new Paragraph();
        paraGraph.add(new Chunk("Patron information", getBoldFont()));
        paraGraph.add(Chunk.NEWLINE);
        paraGraph.add(Chunk.NEWLINE);
        document.add(paraGraph);

        PdfPTable pdfTable = new PdfPTable(3);
        pdfTable.setWidths(new int[]{20, 2, 30});

        pdfTable.addCell(getPdfPCellInJustified("Patron Name"));
        pdfTable.addCell(getPdfPCellInLeft(":"));
        pdfTable.addCell(getPdfPCellInJustified((olePatronDocument.getEntity().getNames().get(0).getFirstName() + " " + olePatronDocument.getEntity().getNames().get(0).getLastName())));


        pdfTable.addCell(getPdfPCellInJustified("Address"));
        pdfTable.addCell(getPdfPCellInLeft(":"));
        pdfTable.addCell(getPdfPCellInJustified((getOlePatronHelperService().getPatronPreferredAddress(entityTypeContactInfoBo) != null ? getOlePatronHelperService().getPatronPreferredAddress(entityTypeContactInfoBo) : "")));


        pdfTable.addCell(getPdfPCellInJustified("Email"));
        pdfTable.addCell(getPdfPCellInLeft(":"));
        pdfTable.addCell(getPdfPCellInJustified((getOlePatronHelperService().getPatronHomeEmailId(entityTypeContactInfoBo) != null ? getOlePatronHelperService().getPatronHomeEmailId(entityTypeContactInfoBo) : "")));

        pdfTable.addCell(getPdfPCellInJustified("Phone #"));
        pdfTable.addCell(getPdfPCellInLeft(":"));
        pdfTable.addCell(getPdfPCellInJustified((getOlePatronHelperService().getPatronHomePhoneNumber(entityTypeContactInfoBo) != null ? getOlePatronHelperService().getPatronHomePhoneNumber(entityTypeContactInfoBo) : "")));

        document.add(pdfTable);
        paraGraph = new Paragraph();
        paraGraph.add(Chunk.NEWLINE);
        document.add(paraGraph);


        getPdfTemplateBody(document, title, body);
        paraGraph = new Paragraph();
        paraGraph.add(new Chunk("Title/item information", getBoldFont()));
        paraGraph.add(Chunk.NEWLINE);
        paraGraph.add(Chunk.NEWLINE);
        document.add(paraGraph);
    }

    private void getPdfTemplateBody(Document document, String title, String body) throws Exception {
        //Notice Type
        Paragraph paraGraph = new Paragraph();
        paraGraph.add(new Chunk(title, getBoldFont()));
        paraGraph.setAlignment(Element.ALIGN_CENTER);
        paraGraph.add(Chunk.NEWLINE);
        paraGraph.add(Chunk.NEWLINE);
        document.add(paraGraph);


        //Notice-specific text
        paraGraph = new Paragraph();
        paraGraph.add(new Chunk(body, getBoldFont()));
        paraGraph.setAlignment(Element.ALIGN_CENTER);
        paraGraph.add(Chunk.NEWLINE);
        paraGraph.add(Chunk.NEWLINE);
        document.add(paraGraph);
    }

    public void addPdfFooter(Document document) throws DocumentException {
        String url = getURLProperty();
        // String myAccountURL = loanProcessor.getParameter(OLEConstants.MY_ACCOUNT_URL);
        String myAccountURL = url + OLEConstants.OLE_MY_ACCOUNT_URL_CHANNEL + url + OLEConstants.OLE_MY_ACCOUNT_URL;
        if (myAccountURL != null && !myAccountURL.trim().isEmpty()) {
            Font ver_15_normal = FontFactory.getFont("Times-Roman", 15, Font.BOLD, BaseColor.BLUE);
            ver_15_normal.setColor(BaseColor.BLUE);
            ver_15_normal.setStyle(Font.UNDERLINE);
            Anchor anchor = new Anchor("MyAccount", ver_15_normal);
            anchor.setReference(myAccountURL);
            Paragraph paraGraph = new Paragraph();
            paraGraph.add(anchor);
            paraGraph.setFont(ver_15_normal);
            paraGraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paraGraph);
        }

    }

    private String getURLProperty() {
        if (null == urlProperty) {
            urlProperty = ConfigContext.getCurrentContextConfig().getProperty("ole.fs.url.base");
        }
        return urlProperty;
    }

    public void addOverdueNoticeInfoToDocument(OleLoanDocument oleLoanDocument, Document document) throws DocumentException {
        Paragraph paraGraph;
        SimpleDateFormat sdf = getSimpleDateFormat();
        PdfPTable pdfTable = new PdfPTable(3);
        pdfTable.setWidths(new int[]{20, 2, 30});

        String circulationLocation = null;
        String circulationReplyToEmail = null;
        OleCirculationDesk oleCirculationDesk = getCircDeskLocationResolver().getCirculationDesk(oleLoanDocument.getItemLocation());
        if (oleCirculationDesk != null) {
            circulationLocation = oleCirculationDesk.getCirculationDeskPublicName();
            if (StringUtils.isNotBlank(oleCirculationDesk.getReplyToEmail())) {
                circulationReplyToEmail = oleCirculationDesk.getReplyToEmail();
            } else {
                circulationReplyToEmail = getParameterResolverInstance().getParameter(OLEConstants
                        .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT,OLEParameterConstants.NOTICE_FROM_MAIL);
            }
        } else {
            circulationLocation = getParameterResolverInstance().getParameter(OLEConstants
                    .APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT,OLEParameterConstants.DEFAULT_CIRCULATION_DESK);
            circulationReplyToEmail = getParameterResolverInstance().getParameter(OLEConstants
                    .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT,OLEParameterConstants.NOTICE_FROM_MAIL);
        }

        pdfTable.addCell(getPdfPCellInJustified("Circulation Location / Library Name"));
        pdfTable.addCell(getPdfPCellInLeft(":"));
        pdfTable.addCell(getPdfPCellInJustified(circulationLocation));

        pdfTable.addCell(getPdfPCellInJustified("Circulation Reply-To Email"));
        pdfTable.addCell(getPdfPCellInLeft(":"));
        pdfTable.addCell(getPdfPCellInJustified(circulationReplyToEmail));

        pdfTable.addCell(getPdfPCellInJustified("Title"));
        pdfTable.addCell(getPdfPCellInLeft(":"));
        pdfTable.addCell(getPdfPCellInJustified((oleLoanDocument.getTitle() == null ? "" : oleLoanDocument.getTitle())));

        pdfTable.addCell(getPdfPCellInJustified("Author"));
        pdfTable.addCell(getPdfPCellInLeft(":"));
        pdfTable.addCell(getPdfPCellInJustified((oleLoanDocument.getAuthor() == null ? "" : oleLoanDocument.getAuthor())));

        String volume = (String) oleLoanDocument.getEnumeration() != null && !oleLoanDocument.getEnumeration().equals("") ? oleLoanDocument.getEnumeration() : "";
        String issue = new String(" ");
        String copyNumber = (String) oleLoanDocument.getItemCopyNumber() != null && !oleLoanDocument.getItemCopyNumber().equals("") ? oleLoanDocument.getItemCopyNumber() : "";
        String volumeNumber = volume + "/" + issue + "/" + copyNumber;

        pdfTable.addCell(getPdfPCellInJustified("Volume/Issue/Copy #"));
        pdfTable.addCell(getPdfPCellInLeft(":"));
        pdfTable.addCell(getPdfPCellInJustified((volumeNumber == null ? "" : volumeNumber)));

        pdfTable.addCell(getPdfPCellInJustified("Library shelving location "));
        pdfTable.addCell(getPdfPCellInLeft(":"));
        pdfTable.addCell(getPdfPCellInJustified((oleLoanDocument.getItemLocation() == null ? "" : oleLoanDocument.getItemLocation())));

        String callNumber = "";
        if (oleLoanDocument.getItemCallNumber() != null && !oleLoanDocument.getItemCallNumber().equals("")) {
            callNumber = (String) (oleLoanDocument.getItemCallNumber() != null && !oleLoanDocument.getItemCallNumber().equals("") ? oleLoanDocument.getItemCallNumber() : "");
        }
        pdfTable.addCell(getPdfPCellInJustified("Call # "));
        pdfTable.addCell(getPdfPCellInLeft(":"));
        pdfTable.addCell(getPdfPCellInJustified((callNumber == null ? "" : callNumber)));

        pdfTable.addCell(getPdfPCellInJustified("Item barcode"));
        pdfTable.addCell(getPdfPCellInLeft(":"));
        pdfTable.addCell(getPdfPCellInJustified((oleLoanDocument.getItemId() == null ? "" : oleLoanDocument.getItemId())));

        document.add(pdfTable);
        paraGraph = new Paragraph();
        paraGraph.add(Chunk.NEWLINE);
        document.add(paraGraph);

        pdfTable = new PdfPTable(3);
        pdfTable.setWidths(new int[]{20, 2, 30});

        pdfTable.addCell(getPdfPCellInJustified("Item was due"));
        pdfTable.addCell(getPdfPCellInLeft(":"));
        pdfTable.addCell(getPdfPCellInJustified((oleLoanDocument.getLoanDueDate() == null ? "" : (sdf.format(oleLoanDocument.getLoanDueDate()).toString()))));

        document.add(pdfTable);
        paraGraph = new Paragraph();
        paraGraph.add(Chunk.NEWLINE);
        document.add(paraGraph);

    }

    private void addPdfHeader(Document document, String title) throws
            Exception {
        Paragraph paraGraph = new Paragraph();
        paraGraph.setAlignment(Element.ALIGN_CENTER);
        paraGraph.add(new Chunk(title, getBoldFont()));
        paraGraph.add(Chunk.NEWLINE);
        document.add(paraGraph);
    }

    public Font getBoldFont() {
        return FontFactory.getFont(FontFactory.TIMES_ROMAN, 15, Font.BOLD);
    }

    private PdfPCell getPdfPCellInJustified(String chunk) {
        if (null == chunk) {
            chunk = "";
        }
        PdfPCell pdfPCell = new PdfPCell(new Paragraph(new Chunk(chunk, getFont(chunk))));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
        return pdfPCell;
    }

    private PdfPCell getPdfPCellInLeft(String chunk) {
        PdfPCell pdfPCell = new PdfPCell(new Paragraph(new Chunk(chunk, getDefaultFont())));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        return pdfPCell;
    }

    private PdfPCell getPdfPCellInCenter(String chunk) {
        PdfPCell pdfPCell = new PdfPCell(new Paragraph(new Chunk(chunk, getDefaultFont())));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        return pdfPCell;
    }

    private PdfPCell getPdfPCellInCenter(String chunk, Font font) {
        PdfPCell pdfPCell = new PdfPCell(new Paragraph(new Chunk(chunk, font)));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        return pdfPCell;
    }

    private PdfPCell getPdfPCellNewLine() {
        PdfPCell pdfPCell = new PdfPCell(new Paragraph(Chunk.NEWLINE));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        return pdfPCell;
    }

    public Font getDefaultFont() {
        com.itextpdf.text.Font font = FontFactory.getFont(getFontFilePath("org/kuali/ole/deliver/batch/fonts/ARIALUNI.TTF"), BaseFont.IDENTITY_H, 10);
        return font;
    }

    public String getFontFilePath(String classpathRelativePath) {
        try {
            Resource rsrc = new ClassPathResource(classpathRelativePath);
            return rsrc.getFile().getAbsolutePath();
        } catch (Exception e) {
            LOG.error("Error : while accessing font file " + e);
        }
        return null;
    }

    public Font getFont(String data) {
        if (null != data) {
            String myData = new String(data);
            java.util.List<Character.UnicodeBlock> unicodeBlocks = new ArrayList<>();
            if (StringUtils.isNotBlank(myData)) {
                unicodeBlocks = getListOfLanguage(myData);
            }
            if (unicodeBlocks.contains(Character.UnicodeBlock.ARABIC)) {
                Font font = FontFactory.getFont(getFontFilePath("org/kuali/ole/deliver/batch/fonts/arial.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                return font;
            }
        }
        return getDefaultFont();
    }

    private List<Character.UnicodeBlock> getListOfLanguage(String data) {
        Set<Character.UnicodeBlock> languageSet = new HashSet<Character.UnicodeBlock>();
        char[] valueArray = data.toCharArray();
        for (int counter = 0; counter < valueArray.length; counter++) {
            languageSet.add(Character.UnicodeBlock.of(valueArray[counter]));
        }
        return (new ArrayList<Character.UnicodeBlock>(languageSet));
    }

    public String createPDFFile(String title, String itemId) throws Exception {
        String pdfLocationSystemParam = getParameterResolverInstance().getParameter(OLEConstants
                .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEParameterConstants.PDF_LOCATION);
        if (LOG.isDebugEnabled()) {
            LOG.debug("System Parameter for PDF_Location --> " + pdfLocationSystemParam);
        }
        if (pdfLocationSystemParam == null || pdfLocationSystemParam.trim().isEmpty()) {
            pdfLocationSystemParam = ConfigContext.getCurrentContextConfig().getProperty("staging.directory") + "/";
            if (LOG.isDebugEnabled()) {
                LOG.debug("System Parameter for PDF_Location staging dir--> " + pdfLocationSystemParam);
            }
        } else {
            pdfLocationSystemParam = ConfigContext.getCurrentContextConfig().getProperty("homeDirectory") + "/" + pdfLocationSystemParam + "/";

        }
        boolean locationExist = new File(pdfLocationSystemParam).exists();
        boolean fileCreated = false;
        if (LOG.isDebugEnabled()) {
            LOG.debug("Is directory Exist ::" + locationExist);
        }
        try {
            if (!locationExist) {
                fileCreated = new File(pdfLocationSystemParam).mkdirs();
                if (!fileCreated) {
                    throw new RuntimeException("Unable to create directory :" + pdfLocationSystemParam);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception occurred while creating the directory : " + e.getMessage(), e);
            throw e;
        }

        if (title == null || title.trim().isEmpty()) {
            title = OLEConstants.ITEM_TITLE;
        }
        title = title.replaceAll(" ", "_");

        if (itemId == null || itemId.trim().isEmpty()) {
            itemId = OLEConstants.ITEM_ID;
        }
        String fileName = pdfLocationSystemParam + title + "_" + itemId + "_" + (new Date(System.currentTimeMillis())).toString().replaceAll(":", "_") + ".pdf";
        String unModifiedFileName = fileName;
        fileName = fileName.replace(" ", "_");
        if (LOG.isDebugEnabled()) {
            LOG.debug("File Created :" + title + "file name ::" + fileName + "::");
        }
        return unModifiedFileName;
    }

    private SimpleDateFormat getSimpleDateFormat() {
        if (null == simpleDateFormat) {
            simpleDateFormat = new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE + " " + RiceConstants.SIMPLE_DATE_FORMAT_FOR_TIME);
        }
        return simpleDateFormat;
    }

    public void setSimpleDateFormat(SimpleDateFormat simpleDateFormat) {
        this.simpleDateFormat = simpleDateFormat;
    }

    private CircDeskLocationResolver getCircDeskLocationResolver() {
        if (circDeskLocationResolver == null) {
            circDeskLocationResolver = new CircDeskLocationResolver();
        }
        return circDeskLocationResolver;
    }

    public void setCircDeskLocationResolver(CircDeskLocationResolver circDeskLocationResolver) {
        this.circDeskLocationResolver = circDeskLocationResolver;
    }

    private ParameterValueResolver getParameterResolverInstance() {
        if (null == parameterResolverInstance) {
            parameterResolverInstance = ParameterValueResolver.getInstance();
        }
        return parameterResolverInstance;
    }

    public void setParameterResolverInstance(ParameterValueResolver parameterResolverInstance) {
        this.parameterResolverInstance = parameterResolverInstance;
    }
}
