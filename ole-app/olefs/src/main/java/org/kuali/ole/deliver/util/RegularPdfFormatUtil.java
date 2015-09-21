package org.kuali.ole.deliver.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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
        Document document = new Document(PageSize.A4);
        document.setMargins(f1, f2, f3, f4);
        return document;
    }
}
