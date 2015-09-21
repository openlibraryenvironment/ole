package org.kuali.ole.deliver.util;

import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import org.apache.log4j.Logger;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.util.printSlip.OlePrintSlipUtil;

/**
 * Created by sheiksalahudeenm on 7/29/15.
 */
public class OleRecieptPrintSlipUtil extends OlePrintSlipUtil {
    private static final Logger LOG = Logger.getLogger(OleRecieptPrintSlipUtil.class);


    @Override
    public PdfFormatUtil getPdfFormatUtil() {
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

    }

    @Override
    protected PdfPTable getPdfPTable() {
        return new PdfPTable(1);
    }
}

