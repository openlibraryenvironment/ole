package org.kuali.ole.deliver.util.printSlip;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.util.OleRegularPrintSlipUtil;
import org.kuali.ole.deliver.util.RegularPdfFormatUtil;

/**
 * Created by sheiksalahudeenm on 7/31/15.
 */
public class InTransitForLoanRegularPrintSlipUtil extends OleRegularPrintSlipUtil {
    @Override
    public RegularPdfFormatUtil getPdfFormatUtil() {
        return new RegularPdfFormatUtil();
    }

    @Override
    protected void populateHeader(Paragraph paraGraph) {
        paraGraph.add(new Chunk("Intransit for Loan", getPdfFormatUtil().getBoldFont()));
    }

    @Override
    protected void populateBody(PdfPTable pdfTable) {
        String routeTo = getOleItemRecordForCirc().getRouteToLocation();
        OleDeliverRequestBo oleDeliverRequestBo = getOleItemRecordForCirc().getOleDeliverRequestBo();
        String requestedBy = oleDeliverRequestBo != null ? (oleDeliverRequestBo.getOlePatron().getPatronName()) : null;
        String requestNote = oleDeliverRequestBo != null ? (oleDeliverRequestBo.getRequestNote()) : "";
        pdfTable.addCell(getPdfFormatUtil().getPdfPCellInJustified("Route To"));
        pdfTable.addCell(getPdfFormatUtil().getPdfPCellInLeft(":"));
        pdfTable.addCell(getPdfFormatUtil().getPdfPCellInJustified(routeTo));

        pdfTable.addCell(getPdfFormatUtil().getPdfPCellInJustified("Place on hold for"));
        pdfTable.addCell(getPdfFormatUtil().getPdfPCellInLeft(":"));
        pdfTable.addCell(getPdfFormatUtil().getPdfPCellInJustified(requestedBy));


        pdfTable.addCell(getPdfFormatUtil().getPdfPCellInJustified("Date/Time "));
        pdfTable.addCell(getPdfFormatUtil().getPdfPCellInLeft(":"));
        pdfTable.addCell(getPdfFormatUtil().getPdfPCellInJustified(getSimpleDateFormat().format(getOleItemRecordForCirc().getItemRecord().getEffectiveDate()).toString()));

        pdfTable.addCell(getPdfFormatUtil().getPdfPCellInJustified("Request Note"));
        pdfTable.addCell(getPdfFormatUtil().getPdfPCellInLeft(":"));
        pdfTable.addCell(getPdfFormatUtil().getPdfPCellInJustified(requestNote));
    }
}
