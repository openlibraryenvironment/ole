package org.kuali.ole.deliver.util.printSlip;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.util.OleRegularPrintSlipUtil;
import org.kuali.ole.deliver.util.RegularPdfFormatUtil;

/**
 * Created by chenchulakshmig on 9/3/15.
 */
public class OnHoldRegularPrintSlipUtil extends OleRegularPrintSlipUtil {

    @Override
    public RegularPdfFormatUtil getPdfFormatUtil() {
        return new RegularPdfFormatUtil();
    }

    @Override
    protected void populateHeader(Paragraph paraGraph) {
        paraGraph.add(new Chunk("Hold Slip", getPdfFormatUtil().getBoldFont()));
    }

    @Override
    protected void populateBody(PdfPTable pdfTable) {
        OleDeliverRequestBo oleDeliverRequestBo = getOleItemRecordForCirc().getOleDeliverRequestBo();
        String patronName = oleDeliverRequestBo != null ? oleDeliverRequestBo.getOlePatron().getPatronName() : null;
        Object expirationDate = oleDeliverRequestBo != null ? oleDeliverRequestBo.getHoldExpirationDate() : null;
        pdfTable.addCell(getPdfFormatUtil().getPdfPCellInJustified("Patron Name"));
        pdfTable.addCell(getPdfFormatUtil().getPdfPCellInLeft(":"));
        pdfTable.addCell(getPdfFormatUtil().getPdfPCellInJustified(patronName));

        pdfTable.addCell(getPdfFormatUtil().getPdfPCellInJustified("Expiration Date"));
        pdfTable.addCell(getPdfFormatUtil().getPdfPCellInLeft(":"));
        pdfTable.addCell(getPdfFormatUtil().getPdfPCellInJustified(expirationDate != null ? getDateFormat().format(expirationDate).toString() : null));
    }

}
