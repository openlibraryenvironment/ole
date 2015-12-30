package org.kuali.ole.deliver.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.*;
import java.util.List;

/**
 * Created by sheiksalahudeenm on 7/29/15.
 */
public class RegularPdfFormatUtil extends PdfFormatUtil{

    private static final Logger LOG = Logger.getLogger(RegularPdfFormatUtil.class);

    @Override
    public Document getDocument(float f1, float f2, float f3, float f4) {
        String regularPrinterPageSize = getParameter("REGULAR_PRINTER_PAGE_SIZE");
        Document document = new Document(PageSize.getRectangle(StringUtils.isNotBlank(regularPrinterPageSize)?regularPrinterPageSize: OLEConstants.REGULAR_PRINTER_PAGE_SIZE));
        document.setMargins(f1, f2, f3, f4);
        return document;
    }

    @Override
    public Font getDefaultFont() {
        String regularPrinterFontSize = getParameter("REGULAR_PRINTER_FONT_SIZE");
        Font font = FontFactory.getFont(getFontFilePath("org/kuali/ole/deliver/batch/fonts/ARIALUNI.TTF"), BaseFont.IDENTITY_H,Integer.parseInt(StringUtils.isNotBlank(regularPrinterFontSize)?regularPrinterFontSize:OLEConstants.REGULAR_PRINTER_FONT_SIZE));
        return font;
    }
}
