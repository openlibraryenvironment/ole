package org.kuali.ole.deliver.batch;


import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleCirculationDeskDetail;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 11/23/12
 * Time: 5:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleDeliverNoticeService {
    private LoanProcessor loanProcessor = new LoanProcessor();
    private static final Logger LOG = Logger.getLogger(OleShelvingLagTime.class);

    public LoanProcessor getLoanProcessor() {
        return loanProcessor;
    }

    public void setLoanProcessor(LoanProcessor loanProcessor) {
        this.loanProcessor = loanProcessor;
    }


    public Font getArialFont(){
        com.itextpdf.text.Font font = FontFactory.getFont(getFontFilePath("org/kuali/ole/deliver/batch/fonts/arial.ttf"), BaseFont.IDENTITY_H,10);
        return font;
    }

    public String getFontFilePath(String classpathRelativePath)  {
        try {
            Resource rsrc = new ClassPathResource(classpathRelativePath);
            return rsrc.getFile().getAbsolutePath();
        } catch(Exception e){
            LOG.error("Error : while accessing font file "+e);
        }
        return null;
    }

    public Font getBoldFont(){
        return FontFactory.getFont(FontFactory.TIMES_ROMAN, 15, Font.BOLD);
    }


    private PdfPCell getPdfPCellInCenter(String chunk) {
        PdfPCell pdfPCell = new PdfPCell(new Paragraph(new Chunk(chunk,getArialFont())));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        return pdfPCell;
    }

    private PdfPCell getPdfPCellInCenter(String chunk,Font font) {
        PdfPCell pdfPCell = new PdfPCell(new Paragraph(new Chunk(chunk,font)));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        return pdfPCell;
    }

    private PdfPCell getPdfPCellNewLine() {
        PdfPCell pdfPCell = new PdfPCell(new Paragraph(Chunk.NEWLINE));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        return pdfPCell;
    }

    private Paragraph getPdfParagraphNewLine() {
        Paragraph paragraph = new Paragraph(new Paragraph(Chunk.NEWLINE));
        return paragraph;
    }

    public boolean createPdf(OleNoticeBo noticeBo) {
        boolean result = false;
        String directory = getLoanProcessor().getParameter("PDF_LOCATION");
        OutputStream outputStream = null;
        String fileName = noticeBo.getNoticeName() + "/" + noticeBo.getItemId();
        try {
            Document document = new Document(PageSize.A4);
            outputStream = new FileOutputStream(directory + "/" + fileName + ".pdf");
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            Font boldFont = FontFactory.getFont("Times-Roman", 15, Font.BOLD);
            document.open();
            document.newPage();

            //document.setHeader(new HeaderFooter(new Phrase(fileName), new Phrase("Footer")));

            //Circulation Desk
            Paragraph paraGraph = new Paragraph();
            paraGraph.add(new Chunk(noticeBo.getNoticeName(), boldFont));
            paraGraph.add(Chunk.NEWLINE);
            document.add(paraGraph);
            PdfPTable pdfTable = new PdfPTable(3);
            PdfPCell pdfPCell = new PdfPCell(new Paragraph("Circulation Location / Library Name"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getCirculationDeskName())));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk("Address")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getCirculationDeskAddress())));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk("Email")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getCirculationDeskEmailAddress())));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk("Phone #")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getCirculationDeskPhoneNumber())));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            document.add(pdfTable);
            paraGraph = new Paragraph();
            paraGraph.add(Chunk.NEWLINE);
            document.add(paraGraph);

            //Patron
            paraGraph = new Paragraph();
            paraGraph.add(new Chunk("Addressee", boldFont));
            paraGraph.add(Chunk.NEWLINE);
            document.add(paraGraph);
            pdfTable = new PdfPTable(3);

            pdfTable.addCell(getPdfPCellInJustified("Borrower Name"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified(noticeBo.getPatronName() != null ? noticeBo.getPatronName() : ""));

            pdfTable.addCell(getPdfPCellInJustified("Address"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((noticeBo.getPatronAddress() != null ? noticeBo.getPatronAddress() : "")));

            pdfTable.addCell(getPdfPCellInJustified("Email"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((noticeBo.getPatronEmailAddress() != null ? noticeBo.getPatronEmailAddress() : "")));

            pdfTable.addCell(getPdfPCellInJustified("Phone #"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((noticeBo.getPatronPhoneNumber() != null ? noticeBo.getPatronPhoneNumber() : "")));

            document.add(pdfTable);
            paraGraph = new Paragraph();
            paraGraph.add(Chunk.NEWLINE);
            document.add(paraGraph);

            //Notice Type
            paraGraph = new Paragraph();
            paraGraph.add(new Chunk(noticeBo.getNoticeName(), boldFont));
            paraGraph.setAlignment(Element.ALIGN_CENTER);
            paraGraph.add(Chunk.NEWLINE);
            document.add(paraGraph);


            //Notice-specific text
            paraGraph = new Paragraph();
            paraGraph.add(new Chunk(noticeBo.getNoticeSpecificContent(), boldFont));
            paraGraph.setAlignment(Element.ALIGN_CENTER);
            paraGraph.add(Chunk.NEWLINE);
            document.add(paraGraph);
            //Title/item information
            paraGraph = new Paragraph();
            paraGraph.add(new Chunk("Title/item information", boldFont));
            paraGraph.add(Chunk.NEWLINE);
            document.add(paraGraph);
            pdfTable = new PdfPTable(3);


            pdfTable.addCell(getPdfPCellInJustified("Title"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((noticeBo.getTitle() == null ? "" : noticeBo.getTitle())));

            pdfTable.addCell(getPdfPCellInJustified("Author"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((noticeBo.getAuthor() == null ? "" : noticeBo.getAuthor())));


            pdfTable.addCell(getPdfPCellInJustified("Volume/Issue/Copy # "));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            if(!noticeBo.getNoticeName().equalsIgnoreCase(OLEConstants.NOTICE_RECALL)){
                pdfTable.addCell(getPdfPCellInJustified(noticeBo.getVolumeNumber() == null ? "" : noticeBo.getVolumeNumber()));
            }else{
                pdfTable.addCell(getPdfPCellInJustified((noticeBo.getVolumeIssueCopyNumber() == null ? "" : noticeBo.getVolumeIssueCopyNumber())));
            }


            pdfTable.addCell(getPdfPCellInJustified("Library shelving location "));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((noticeBo.getItemShelvingLocation() == null ? "" : noticeBo.getItemShelvingLocation())));


            pdfTable.addCell(getPdfPCellInJustified("Call #"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((noticeBo.getItemCallNumber() == null ? "" : noticeBo.getItemCallNumber())));


            pdfTable.addCell(getPdfPCellInJustified("Item barcode"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((noticeBo.getItemId() == null ? "" : noticeBo.getItemId())));

            document.add(pdfTable);
            paraGraph = new Paragraph();
            paraGraph.add(Chunk.NEWLINE);
            document.add(paraGraph);

            //Information specific text
  /*          paraGraph = new Paragraph();
            paraGraph.add(new Chunk("Information specific text",boldFont));
            paraGraph.add(Chunk.NEWLINE);
            document.add(paraGraph);*/
            if (noticeBo.getNoticeName().equals("Recall")) {
                pdfTable = new PdfPTable(3);
                pdfPCell = new PdfPCell(new Paragraph("Original Due Date"));
                pdfPCell.setBorder(pdfPCell.NO_BORDER);
                pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
                pdfTable.addCell(pdfPCell);
                pdfPCell = new PdfPCell(new Paragraph(":"));
                pdfPCell.setBorder(pdfPCell.NO_BORDER);
                pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
                pdfTable.addCell(pdfPCell);
                pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getOriginalDueDate() == null ? "" : noticeBo.getOriginalDueDate().toString())));
                pdfPCell.setBorder(pdfPCell.NO_BORDER);
                pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
                pdfTable.addCell(pdfPCell);
                pdfPCell = new PdfPCell(new Paragraph(new Chunk("New Due Date")));
                pdfPCell.setBorder(pdfPCell.NO_BORDER);
                pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
                pdfTable.addCell(pdfPCell);
                pdfPCell = new PdfPCell(new Paragraph(":"));
                pdfPCell.setBorder(pdfPCell.NO_BORDER);
                pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
                pdfTable.addCell(pdfPCell);
                pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getNewDueDate() == null ? "" : noticeBo.getNewDueDate().toString())));
                pdfPCell.setBorder(pdfPCell.NO_BORDER);
                pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
                pdfTable.addCell(pdfPCell);
                document.add(pdfTable);
                paraGraph = new Paragraph();
                paraGraph.add(Chunk.NEWLINE);
                document.add(paraGraph);
            } else if (noticeBo.getNoticeName().equals("OnHold")) {
                pdfTable = new PdfPTable(3);

                pdfTable.addCell(getPdfPCellInJustified("Pick Up Location"));
                pdfTable.addCell(getPdfPCellInLeft(":"));
                pdfTable.addCell(getPdfPCellInJustified((noticeBo.getPickUpLocation() != null ? noticeBo.getPickUpLocation() : "")));

                pdfTable.addCell(getPdfPCellInJustified("Circulation Location / Library Name"));
                pdfTable.addCell(getPdfPCellInLeft(":"));
                pdfTable.addCell(getPdfPCellInJustified((noticeBo.getCirculationDeskName() != null ? noticeBo.getCirculationDeskName() : "")));


                pdfTable.addCell(getPdfPCellInJustified("Address"));
                pdfTable.addCell(getPdfPCellInLeft(":"));
                pdfTable.addCell(getPdfPCellInJustified((noticeBo.getCirculationDeskAddress() != null ? noticeBo.getCirculationDeskAddress() : "")));

                pdfTable.addCell(getPdfPCellInJustified("Email"));
                pdfTable.addCell(getPdfPCellInLeft(":"));
                pdfTable.addCell(getPdfPCellInJustified((noticeBo.getCirculationDeskEmailAddress() != null ? noticeBo.getCirculationDeskEmailAddress() : "")));

                pdfTable.addCell(getPdfPCellInJustified("Phone #"));
                pdfTable.addCell(getPdfPCellInLeft(":"));
                pdfTable.addCell(getPdfPCellInJustified((noticeBo.getCirculationDeskPhoneNumber() != null ? noticeBo.getCirculationDeskPhoneNumber() : "")));

                document.add(pdfTable);
                paraGraph = new Paragraph();
                paraGraph.add(Chunk.NEWLINE);
                document.add(paraGraph);

                pdfTable.addCell(getPdfPCellInJustified("Item Will Be Held until"));
                pdfTable.addCell(getPdfPCellInLeft(":"));
                pdfTable.addCell(getPdfPCellInJustified((noticeBo.getOnHoldDueDate() != null ? noticeBo.getOnHoldDueDate().toString() : "")));

                document.add(pdfTable);
                paraGraph = new Paragraph();
                paraGraph.add(Chunk.NEWLINE);
                document.add(paraGraph);
            } else if (noticeBo.getNoticeName().equals("Overdue Notice")) {
                pdfTable = new PdfPTable(3);

                pdfTable.addCell(getPdfPCellInJustified("Item was due"));
                pdfTable.addCell(getPdfPCellInLeft(":"));
                pdfTable.addCell(getPdfPCellInJustified((noticeBo.getDueDate() != null ? noticeBo.getDueDate().toString() : "")));

                document.add(pdfTable);
                document.add(getPdfParagraphNewLine());
            }

            //My Account
            /*    PdfContentByte cb = writer.getDirectContent();
         cb.setLineWidth(1);
         cb.moveTo(0, 245);
         cb.lineTo(0 + document.getPageSize().width(), 245);
         cb.stroke();
         cb = writer.getDirectContent();
         cb.setLineWidth(1);
         cb.moveTo(0, 222);
         cb.lineTo(0 + document.getPageSize().width(), 222);
         cb.stroke();
         paraGraph = new Paragraph();*/
            /*paraGraph.add(new Chunk("My Account",boldFont));
            paraGraph.add(Chunk.NEWLINE);*/
            String url = ConfigContext.getCurrentContextConfig().getProperty("ole.fs.url.base");
            //String myAccountURL = loanProcessor.getParameter(OLEConstants.MY_ACCOUNT_URL);
            String myAccountURL = url+OLEConstants.OLE_MY_ACCOUNT_URL_CHANNEL+url+OLEConstants.OLE_MY_ACCOUNT_URL;
            if (myAccountURL != null && !myAccountURL.trim().isEmpty()) {
                Font ver_15_normal = FontFactory.getFont("Times-Roman", 15, Font.BOLD,BaseColor.BLUE);
                ver_15_normal.setColor(BaseColor.BLUE);
                ver_15_normal.setStyle(Font.UNDERLINE);
                Anchor anchor = new Anchor("MyAccount", ver_15_normal);
                anchor.setName("My Account");
                anchor.setReference(myAccountURL);
                Paragraph newParaGraph = new Paragraph();
                newParaGraph.add(anchor);
                newParaGraph.setFont(ver_15_normal);
                newParaGraph.setAlignment(Element.ALIGN_CENTER);
                document.add(newParaGraph);
            }
            outputStream.flush();
            document.close();
            outputStream.close();
            result = true;
        } catch (Exception ex) {
            LOG.error("Exception while creating pdf", ex);
        }
        return result;
    }

    public void createSlip(OleDeliverRequestBo oleDeliverRequestBo, HttpServletResponse response) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        OutputStream outputStream = null;
        String directory = getLoanProcessor().getParameter("PDF_LOCATION");

        String fileName = "In Transit Routing slip" + oleDeliverRequestBo.getItemId();

        Document document = new Document(PageSize.A4);
        try {
            response.setContentType("application/pdf");
            outputStream = new FileOutputStream(directory + "/" + fileName + ".pdf");
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            Font boldFont = FontFactory.getFont("Times-Roman", 15, Font.BOLD);
            Font ver_15_normal = FontFactory.getFont("VERDANA", 15, 0);
            document.open();
            document.newPage();
            PdfPTable pdfTable = new PdfPTable(3);
            Paragraph paraGraph = new Paragraph();
            paraGraph.setAlignment(Element.ALIGN_CENTER);
            paraGraph.add(new Chunk("Routing Slip In-Transit Per Staff Request", boldFont));
            paraGraph.add(Chunk.NEWLINE);
            paraGraph.add(Chunk.NEWLINE);
            paraGraph.add(Chunk.NEWLINE);
            pdfTable.addCell(getPdfPCellInJustified("Route To"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified(oleDeliverRequestBo.getCirculationLocationId()));

            pdfTable.addCell(getPdfPCellInJustified("Requested By"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified(oleDeliverRequestBo.getBorrowerId()));


            pdfTable.addCell(getPdfPCellInJustified("Date/Time "));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified(oleDeliverRequestBo.getCreateDate().toString()));

            Map<String, String> operatorMap = new HashMap<String, String>();
            operatorMap.put("operatorId", oleDeliverRequestBo.getOperatorCreateId());
            operatorMap.put("defaultLocation", "true");
            java.util.List<OleCirculationDeskDetail> circulationDeskDetailList = (java.util.List<OleCirculationDeskDetail>) KRADServiceLocator.getBusinessObjectService().findMatching(OleCirculationDeskDetail.class, operatorMap);
            String locationName = "";
            if (circulationDeskDetailList.size() > 0) {
                locationName = circulationDeskDetailList.get(0).getOleCirculationDesk().getOleCirculationDeskLocations().get(0).getCirculationDeskLocation();
            }


            pdfTable.addCell(getPdfPCellInJustified("Circulation Location"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified(locationName));


            pdfTable.addCell(getPdfPCellInJustified("Item Barcode"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified(oleDeliverRequestBo.getItemId()));

            pdfTable.addCell(getPdfPCellInJustified("Title"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified(oleDeliverRequestBo.getTitle()));

            pdfTable.addCell(getPdfPCellInJustified("Call Number"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified(oleDeliverRequestBo.getCallNumber()));

            pdfTable.addCell(getPdfPCellInJustified("Copy Number"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified(oleDeliverRequestBo.getCopyNumber()));


            response.setContentType("application/pdf");
            OutputStream os = response.getOutputStream();
            PdfWriter.getInstance(document, os);
            document.open();
            document.add(paraGraph);
            document.add(pdfTable);
            document.close();
            byteArrayOutputStream.flush();
            byteArrayOutputStream.close();
            os.flush();
            os.close();
        } catch (Exception e) {
            LOG.error("Exception while creating slip", e);
        }
    }

    private PdfPCell getPdfPCellInJustified(String chunk) {
        PdfPCell pdfPCell = new PdfPCell(new Paragraph(new Chunk(chunk)));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
        return pdfPCell;
    }

    private PdfPCell getPdfPCellInLeft(String chunk) {
        PdfPCell pdfPCell = new PdfPCell(new Paragraph(new Chunk(chunk)));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        return pdfPCell;
    }


}
