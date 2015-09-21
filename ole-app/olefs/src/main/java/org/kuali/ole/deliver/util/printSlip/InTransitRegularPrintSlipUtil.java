package org.kuali.ole.deliver.util.printSlip;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import org.kuali.ole.deliver.util.OleRegularPrintSlipUtil;
import org.kuali.ole.deliver.util.RegularPdfFormatUtil;

import java.sql.Timestamp;

/**
 * Created by sheiksalahudeenm on 7/30/15.
 */
public class InTransitRegularPrintSlipUtil extends OleRegularPrintSlipUtil {

    @Override
    public RegularPdfFormatUtil getPdfFormatUtil() {
        return new RegularPdfFormatUtil();
    }

    @Override
    protected void populateHeader(Paragraph paraGraph) {
        paraGraph.add(new Chunk("Routing Slip In-Transit", getPdfFormatUtil().getBoldFont()));
    }

    @Override
    protected void populateBody(PdfPTable pdfTable) {
        String routeTo = getOleItemRecordForCirc().getRouteToLocation();
        pdfTable.addCell(getPdfFormatUtil().getPdfPCellInJustified("Route To"));
        pdfTable.addCell(getPdfFormatUtil().getPdfPCellInLeft(":"));
        pdfTable.addCell(getPdfFormatUtil().getPdfPCellInJustified(routeTo));

        pdfTable.addCell(getPdfFormatUtil().getPdfPCellInJustified("Date/Time "));
        pdfTable.addCell(getPdfFormatUtil().getPdfPCellInLeft(":"));
        Timestamp effectiveDate = getOleItemRecordForCirc().getItemRecord().getEffectiveDate();
        pdfTable.addCell(getPdfFormatUtil().getPdfPCellInJustified(effectiveDate != null ?
                getSimpleDateFormat().format(effectiveDate).toString() : ""));
    }
}
