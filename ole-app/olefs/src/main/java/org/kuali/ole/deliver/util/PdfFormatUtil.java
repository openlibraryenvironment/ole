package org.kuali.ole.deliver.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.*;
import java.util.List;

/**
 * Created by sheiksalahudeenm on 7/29/15.
 */
public abstract class PdfFormatUtil {

    private static final Logger LOG = Logger.getLogger(PdfFormatUtil.class);

    private Map<String, Font> fontMap = new HashMap<String, Font>();
    private Map<String, BaseColor> colorMap = new HashMap<String, BaseColor>();


    public void populateColorMap() {
        colorMap.put("WHITE", BaseColor.WHITE);
        colorMap.put("YELLOW", BaseColor.YELLOW);
        colorMap.put("BLACK", BaseColor.BLACK);
        colorMap.put("BLUE", BaseColor.BLUE);
        colorMap.put("CYAN", BaseColor.CYAN);
        colorMap.put("DARK_GRAY", BaseColor.DARK_GRAY);
        colorMap.put("GRAY", BaseColor.GRAY);
        colorMap.put("GREEN", BaseColor.GREEN);
        colorMap.put("LIGHT_GRAY", BaseColor.LIGHT_GRAY);
        colorMap.put("MAGENTA", BaseColor.MAGENTA);
        colorMap.put("ORANGE", BaseColor.ORANGE);
        colorMap.put("PINK", BaseColor.PINK);
        colorMap.put("RED", BaseColor.RED);
        colorMap.put("PINK", BaseColor.PINK);
    }

    public void populateFontMap() {
        fontMap.put("COURIER", new Font(FontFactory.getFont("Times-Roman", Font.BOLD).getBaseFont()));
        fontMap.put("COURIER", new Font(FontFactory.getFont("Times-Roman", Font.BOLDITALIC).getBaseFont()));
        fontMap.put("COURIER", new Font(FontFactory.getFont("Times-Roman", Font.DEFAULTSIZE).getBaseFont()));
        fontMap.put("COURIER", new Font(FontFactory.getFont("Times-Roman", Font.STRIKETHRU).getBaseFont()));
        fontMap.put("COURIER", new Font(FontFactory.getFont("Times-Roman", Font.NORMAL).getBaseFont()));
        fontMap.put("COURIER", new Font(FontFactory.getFont("Times-Roman", Font.UNDEFINED).getBaseFont()));
        fontMap.put("COURIER", new Font(FontFactory.getFont("Times-Roman", Font.UNDERLINE).getBaseFont()));
    }


    public Font getBoldFont(){
        Font boldFont = FontFactory.getFont("Times-Roman", 15, Font.BOLD);
        return boldFont;
    }

    public PdfPCell getPdfPCellInJustified(String chunk) {
        PdfPCell pdfPCell=null;
        if(chunk!=null){
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(chunk, getFont(chunk))));
        }
        else{
            pdfPCell = new PdfPCell(new Paragraph());
        }
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
        return pdfPCell;
    }

    public PdfPCell getPdfPCellAligned(String chunk, int font, int val) {
        PdfPCell pdfPCell = new PdfPCell(new Phrase(chunk,FontFactory.getFont(FontFactory.TIMES_ROMAN, 16, font)));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setPaddingLeft(val);
        return pdfPCell;
    }

    public PdfPCell getPdfPCellInLeft(String chunk) {
        PdfPCell pdfPCell = new PdfPCell(new Paragraph(new Chunk(chunk, getDefaultFont())));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        return pdfPCell;
    }

    public Font getFont(String data) {
        String myData = new String(data);
        List<Character.UnicodeBlock> unicodeBlocks = new ArrayList<>();
        if (StringUtils.isNotBlank(myData)) {
            unicodeBlocks = getListOfLanguage(myData);
        }
        if (unicodeBlocks.contains(Character.UnicodeBlock.ARABIC)) {
            Font font = FontFactory.getFont(getFontFilePath("org/kuali/ole/deliver/batch/fonts/arial.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            return font;
        } else {
            return getDefaultFont();
        }
    }

    public Font getDefaultFont(){
        Font font = FontFactory.getFont(getFontFilePath("org/kuali/ole/deliver/batch/fonts/ARIALUNI.TTF"), BaseFont.IDENTITY_H,10);
        return font;
    }


    public List<Character.UnicodeBlock> getListOfLanguage(String data){
        Set<Character.UnicodeBlock> languageSet=new HashSet<Character.UnicodeBlock>();
        char[] valueArray = data.toCharArray();
        for(int counter=0;counter<valueArray.length;counter++){
            languageSet.add(Character.UnicodeBlock.of(valueArray[counter]));
        }
        return (new ArrayList<Character.UnicodeBlock>(languageSet));
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



    public abstract Document getDocument(float f1, float f2, float f3, float f4);

    public PdfPCell getEmptyCell() {
        PdfPCell pdfPCell = new PdfPCell(new Paragraph(Chunk.NEWLINE));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
        return pdfPCell;
    }


    public String getParameter(String parameterName) {
        String parameter = ParameterValueResolver.getInstance().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants
                .DLVR_CMPNT, parameterName);
        return parameter;
    }
}
