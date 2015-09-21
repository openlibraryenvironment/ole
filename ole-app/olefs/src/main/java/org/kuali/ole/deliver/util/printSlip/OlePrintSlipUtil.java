package org.kuali.ole.deliver.util.printSlip;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleItemSearch;
import org.kuali.ole.deliver.util.OleItemRecordForCirc;
import org.kuali.ole.deliver.util.PdfFormatUtil;
import org.kuali.ole.util.DocstoreUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

/**
 * Created by pvsubrah on 9/3/15.
 */
public abstract class OlePrintSlipUtil {
    private static final Logger LOG = Logger.getLogger(OlePrintSlipUtil.class);


    private OleItemSearch oleItemSearch;
    private OleItemRecordForCirc oleItemRecordForCirc;
    SimpleDateFormat simpleDateFormat;
    SimpleDateFormat dateFormat;
    private DocstoreUtil docstoreUtil;
    private OutputStream outputStream;

    public SimpleDateFormat getSimpleDateFormat() {
        if (null == simpleDateFormat) {
            simpleDateFormat = new SimpleDateFormat(OLEConstants.TIMESTAMP);
        }
        return simpleDateFormat;
    }

    public SimpleDateFormat getDateFormat() {
        if (null == dateFormat) {
            dateFormat = new SimpleDateFormat(OLEConstants.DATEFORMAT);
        }
        return dateFormat;
    }

    public OleItemRecordForCirc getOleItemRecordForCirc() {
        return oleItemRecordForCirc;
    }

    public OleItemSearch getOleItemSearch() {
        return oleItemSearch;
    }

    public  void createPdfForPrintingSlip(OleItemRecordForCirc oleItemRecordForCirc,HttpServletResponse response) {
        LOG.debug("Initialize Normal pdf Template");
        try {
            this.oleItemRecordForCirc = oleItemRecordForCirc;
            this.oleItemSearch = getDocstoreUtil().getOleItemSearchList(oleItemRecordForCirc.getItemUUID());
            boolean missingPieceCheck = oleItemRecordForCirc.getItemRecord().isMissingPieceFlag();

            getPdfFormatUtil().populateColorMap();
            getPdfFormatUtil().populateFontMap();
            response.setContentType("application/pdf");
            Document document= getPdfFormatUtil().getDocument(0, 0, 5, 5);
            document.open();
            PdfPTable pdfTable = getPdfPTable();
            Paragraph paraGraph = new Paragraph();
            paraGraph.setAlignment(Element.ALIGN_CENTER);

            populateHeader(paraGraph);

            paraGraph.add(Chunk.NEWLINE);
            paraGraph.add(Chunk.NEWLINE);
            paraGraph.add(Chunk.NEWLINE);

            populateBody(pdfTable);


            if (!missingPieceCheck) {
                populateContentForSlip(pdfTable);
            }
            if(null == outputStream){
                outputStream = response.getOutputStream();
            }
            if (!missingPieceCheck) {
                PdfWriter.getInstance(document, outputStream);
                document.open();
                document.add(paraGraph);
                document.add(pdfTable);
                document.close();
            } else {
                document.close();
            }
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            LOG.error("Exception while creating pdf for printing slip", e);
        }

    }

    private DocstoreUtil getDocstoreUtil() {
        if (null == docstoreUtil) {
            docstoreUtil = new DocstoreUtil();
        }
        return docstoreUtil;
    }

    public void setDocstoreUtil(DocstoreUtil docstoreUtil) {
        this.docstoreUtil = docstoreUtil;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public abstract PdfFormatUtil getPdfFormatUtil();


    protected abstract void populateHeader(Paragraph paraGraph);

    protected abstract void populateBody(PdfPTable pdfTable);

    protected abstract void populateContentForSlip(PdfPTable pdfTable);

    protected abstract PdfPTable getPdfPTable();



}
