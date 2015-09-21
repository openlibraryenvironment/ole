package org.kuali.ole.deliver.util;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OleItemSearch;
import org.kuali.ole.deliver.util.printSlip.OlePrintSlipUtil;
import org.kuali.ole.util.DocstoreUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

/**
 * Created by sheiksalahudeenm on 7/29/15.
 */
public class OleRegularPrintSlipUtil extends OlePrintSlipUtil {
    private static final Logger LOG = Logger.getLogger(OleRegularPrintSlipUtil.class);


    @Override
    public RegularPdfFormatUtil getPdfFormatUtil() {
        return null;
    }

    @Override
    protected void populateHeader(Paragraph paraGraph) {

    }

    @Override
    protected void populateBody(PdfPTable pdfTable) {

    }

    @Override
    protected void populateContentForSlip(PdfPTable pdfTable) {
        OleCirculationDesk oleCirculationDesk = null != getOleItemRecordForCirc() ? getOleItemRecordForCirc().getCheckinLocation() : null;
        try {

            pdfTable.addCell(getPdfFormatUtil().getPdfPCellInJustified("Route From"));
            pdfTable.addCell(getPdfFormatUtil().getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfFormatUtil().getPdfPCellInJustified(oleCirculationDesk != null ? oleCirculationDesk.getCirculationDeskPublicName() : ""));

            pdfTable.addCell(getPdfFormatUtil().getPdfPCellInJustified("Item Barcode"));
            pdfTable.addCell(getPdfFormatUtil().getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfFormatUtil().getPdfPCellInJustified(getOleItemRecordForCirc().getItemRecord().getBarCode()));

            pdfTable.addCell(getPdfFormatUtil().getPdfPCellInJustified("Title"));
            pdfTable.addCell(getPdfFormatUtil().getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfFormatUtil().getPdfPCellInJustified(getOleItemSearch().getTitle()));

            pdfTable.addCell(getPdfFormatUtil().getPdfPCellInJustified("Call Number"));
            pdfTable.addCell(getPdfFormatUtil().getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfFormatUtil().getPdfPCellInJustified(getOleItemSearch().getCallNumber()));

            pdfTable.addCell(getPdfFormatUtil().getPdfPCellInJustified("Copy Number"));
            pdfTable.addCell(getPdfFormatUtil().getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfFormatUtil().getPdfPCellInJustified(getOleItemSearch().getCopyNumber()));


        } catch (Exception e) {
            LOG.error("Exception while creating pdf for printing slip", e);
        }

    }

    @Override
    protected PdfPTable getPdfPTable() {
        return new PdfPTable(3);
    }
}

