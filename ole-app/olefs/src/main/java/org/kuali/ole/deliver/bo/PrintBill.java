package org.kuali.ole.deliver.bo;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 11/2/12
 * Time: 2:27 PM
 * To change this template use File | Settings | File Templates.
 */

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.rice.coreservice.impl.parameter.ParameterBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * The PrintBill  class used to create pdf document .
 */
public class PrintBill extends PdfPageEventHelper {

    private static final Logger LOG = Logger.getLogger(PrintBill.class);

    private Map<String, Font> printFontMap = new HashMap<String, Font>();

    private Map<String, Font> fontMap = new HashMap<String, Font>();
    private Map<String, Color> colorMap = new HashMap<String, Color>();
    private Map<String, Color> printColorMap = new HashMap<String, Color>();


    public void populateFontMap() {
        fontMap.put("COURIER", new Font(Font.COURIER));
        fontMap.put("BOLD", new Font(Font.BOLD));
        fontMap.put("BOLDITALIC", new Font(Font.BOLDITALIC));
        fontMap.put("DEFAULTSIZE", new Font(Font.DEFAULTSIZE));
        fontMap.put("HELVETICA", new Font(Font.HELVETICA));
        fontMap.put("ITALIC", new Font(Font.ITALIC));
        fontMap.put("NORMAL", new Font(Font.NORMAL));
        fontMap.put("STRIKETHRU", new Font(Font.STRIKETHRU));
        fontMap.put("SYMBOL", new Font(Font.SYMBOL));
        fontMap.put("TIMES_ROMAN", new Font(Font.TIMES_ROMAN));
        fontMap.put("UNDEFINED", new Font(Font.UNDEFINED));
        fontMap.put("UNDERLINE", new Font(Font.UNDERLINE));
        fontMap.put("ZAPFDINGBATS", new Font(Font.ZAPFDINGBATS));

    }

    public void populateColorMap() {
        colorMap.put("WHITE", Color.WHITE);
        colorMap.put("YELLOW", Color.YELLOW);
        colorMap.put("BLACK", Color.BLACK);
        colorMap.put("BLUE", Color.BLUE);
        colorMap.put("CYAN", Color.CYAN);
        colorMap.put("DARK_GRAY", Color.DARK_GRAY);
        colorMap.put("GRAY", Color.GRAY);
        colorMap.put("GREEN", Color.GREEN);
        colorMap.put("LIGHT_GRAY", Color.LIGHT_GRAY);
        colorMap.put("MAGENTA", Color.MAGENTA);
        colorMap.put("ORANGE", Color.ORANGE);
        colorMap.put("PINK", Color.PINK);
        colorMap.put("RED", Color.RED);

        colorMap.put("PINK", Color.PINK);
    }

    public void populatePrintFontMap() {
        BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
        Map<String, String> criteriaMap = new HashMap<String, String>();
        criteriaMap.put("namespaceCode", "OLE-PRNT");
        criteriaMap.put("componentCode", "Patron Bill Font");
        List<ParameterBo> parametersList = (List<ParameterBo>) businessObjectService.findMatching(ParameterBo.class, criteriaMap);
        for (int i = 0; i < parametersList.size(); i++) {
            printFontMap.put(parametersList.get(i).getName(), fontMap.get(parametersList.get(i).getValue()));
        }
    }

    public void populatePrintColorMap() {
        BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
        Map<String, String> criteriaMap = new HashMap<String, String>();
        criteriaMap.put("namespaceCode", "OLE-PRNT");
        criteriaMap.put("componentCode", "Patron Bill Color");
        List<ParameterBo> parametersList = (List<ParameterBo>) businessObjectService.findMatching(ParameterBo.class, criteriaMap);
        for (int i = 0; i < parametersList.size(); i++) {
            printColorMap.put(parametersList.get(i).getName(), colorMap.get(parametersList.get(i).getValue()));
        }
    }

    public String getTemplate() {

        BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
        Map<String, String> criteriaMap = new HashMap<String, String>();
        criteriaMap.put("namespaceCode", "OLE-PRNT");
        criteriaMap.put("componentCode", "Print Template");
        List<ParameterBo> parametersList = (List<ParameterBo>) businessObjectService.findMatching(ParameterBo.class, criteriaMap);
        return parametersList.get(0).getValue();

    }

    /**
     * Used to select pdf template
     *
     * @param patronBillPayments,feeTypeList,response
     *
     * @return Void
     */
    public void generatePdf(String firstName, String lastName, List<PatronBillPayment> patronBillPayments, List<FeeType> feeTypeList,boolean isDefaultPrint,List<String> transactionIds, HttpServletResponse response) {
        String template = getTemplate();
        if (template.equalsIgnoreCase(OLEConstants.BILL_TEMP_NORMAL)) {
            createPdf(firstName, lastName, patronBillPayments, feeTypeList,isDefaultPrint,transactionIds, response);
        } else if (template.equalsIgnoreCase(OLEConstants.BILL_TEMP_TABLE)) {
            createPdfWithTable(firstName, lastName, patronBillPayments, feeTypeList,isDefaultPrint,transactionIds, response);
        }
    }

    /**
     * Used to create pdf document for patron bill
     *
     * @param patronBillPayments,feeTypeList,response
     *
     * @return Void
     */
    public void createPdf(String firstName, String lastName, List<PatronBillPayment> patronBillPayments, List<FeeType> feeTypeList,boolean isDefaultPrint,List<String> transactionIds, HttpServletResponse response) {
        LOG.debug("Initialize Normal pdf Template");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BigDecimal feeAmount = BigDecimal.valueOf(0);
        BigDecimal paidAmount = BigDecimal.valueOf(0);
        String billNumber = "";
        try {
            populateColorMap();
            populateFontMap();
            populatePrintColorMap();
            populatePrintFontMap();
            response.setContentType("application/pdf");
            Document document = this.getDocument(0, 0, 5, 5);
            document.open();
            document.newPage();
            Paragraph paraGraph = new Paragraph();
            paraGraph.setAlignment(Element.ALIGN_CENTER);
            paraGraph.add(new Chunk(OLEConstants.OlePatronBill.HEADER_PATRON_RECEIPT));
            paraGraph.add(Chunk.NEWLINE);
            paraGraph.add(Chunk.NEWLINE);
            paraGraph.add(Chunk.NEWLINE);
            SimpleDateFormat df = new SimpleDateFormat(OLEConstants.DAT_FORMAT_EFFECTIVE_PRINT);
            paraGraph.add(new Chunk(OLEConstants.BILL_DT + " : " + df.format(System.currentTimeMillis()) + ""));
            paraGraph.add(Chunk.NEWLINE);
            paraGraph.add(new Chunk(OLEConstants.FIRST_NAME + " : " + firstName, printFontMap.get("Patron_Name_Font")));
            paraGraph.add(Chunk.NEWLINE);
            paraGraph.add(new Chunk(OLEConstants.LAST_NAME + " : " + lastName, printFontMap.get("Item_Title_Font")));
            paraGraph.add(Chunk.NEWLINE);
            for (int j = 0; j < feeTypeList.size(); j++) {
                List<OleItemLevelBillPayment> oleItemLevelBillPayments = new ArrayList<>();
                if (feeTypeList.get(j).getItemLevelBillPaymentList() != null) {
                    oleItemLevelBillPayments.addAll(feeTypeList.get(j).getItemLevelBillPaymentList());
                } else {
                    Map<String,String> map=new HashMap<String,String>();
                    map.put("lineItemId",feeTypeList.get(j).getId());
                    List<OleItemLevelBillPayment> itemLevelBillPayments=(List<OleItemLevelBillPayment>)KRADServiceLocator.getBusinessObjectService().findMatching(OleItemLevelBillPayment.class,map);
                    if(itemLevelBillPayments!=null){
                        oleItemLevelBillPayments.addAll(itemLevelBillPayments);
                    }
                }
                String feeTypeName="";
                if(feeTypeList.get(j).getOleFeeType()!=null && feeTypeList.get(j).getOleFeeType().getFeeTypeName()!=null){
                    feeTypeName=feeTypeList.get(j).getOleFeeType().getFeeTypeName();
                }
                if (!feeTypeList.get(j).getBillNumber().equals(billNumber)) {
                    paraGraph.add(new Chunk(OLEConstants.BILL_NO + " : " + feeTypeList.get(j).getBillNumber()));
                    paraGraph.add(Chunk.NEWLINE);
                }
                for (OleItemLevelBillPayment oleItemLevelBillPayment : oleItemLevelBillPayments) {
                    boolean isAddContent = false;
                    if(isDefaultPrint){
                        if(transactionIds.contains(oleItemLevelBillPayment.getPaymentId())){
                            isAddContent=true;
                        }
                    }  else {
                        isAddContent=true;
                    }
                    if (isAddContent) {
                        paraGraph.add(populateParagraphCell(OLEConstants.OlePatronBill.LABEL_PATRON_RECEIPT_NUMBER,oleItemLevelBillPayment.getPaymentId() != null ? oleItemLevelBillPayment.getPaymentId() : " "));
                        paraGraph.add(Chunk.NEWLINE);
                        paraGraph.add(populateParagraphCell(OLEConstants.OlePatronBill.LABEL_BILL_NUMBER,feeTypeList.get(j).getBillNumber() != null ? feeTypeList.get(j).getBillNumber() : " "));
                        paraGraph.add(Chunk.NEWLINE);
                        paraGraph.add(populateParagraphCell(OLEConstants.OlePatronBill.LABEL_FEE_TYPE,feeTypeName));
                        paraGraph.add(Chunk.NEWLINE);
                        paraGraph.add(populateParagraphCell(OLEConstants.OlePatronBill.LABEL_TRANSACTION_DATE,(oleItemLevelBillPayment.getPaymentDate() != null ? df.format(oleItemLevelBillPayment.getPaymentDate()) : " ")));
                        paraGraph.add(Chunk.NEWLINE);
                        paraGraph.add(populateParagraphCell(OLEConstants.OlePatronBill.LABEL_OPERATOR_ID,oleItemLevelBillPayment.getCreatedUser() != null ? oleItemLevelBillPayment.getCreatedUser() : " "));
                        paraGraph.add(Chunk.NEWLINE);
                        paraGraph.add(populateParagraphCell(OLEConstants.OlePatronBill.LABEL_ITEM_BARCODE,feeTypeList.get(j).getItemBarcode() != null ? feeTypeList.get(j).getItemBarcode() : " "));
                        paraGraph.add(Chunk.NEWLINE);
                        paraGraph.add(populateParagraphCell(OLEConstants.OlePatronBill.LABEL_ITEM_TITLE,feeTypeList.get(j).getItemTitle() != null ? feeTypeList.get(j).getItemTitle() : " "));
                        paraGraph.add(Chunk.NEWLINE);
                        paraGraph.add(populateParagraphCell(OLEConstants.OlePatronBill.LABEL_ITEM_AUTHOR,feeTypeList.get(j).getItemAuthor() != null ? feeTypeList.get(j).getItemAuthor() : " "));
                        paraGraph.add(Chunk.NEWLINE);
                        paraGraph.add(populateParagraphCell(OLEConstants.OlePatronBill.LABEL_ITEM_CALL_NUMBER,feeTypeList.get(j).getItemCallNumber() != null ? feeTypeList.get(j).getItemCallNumber() : " "));
                        paraGraph.add(Chunk.NEWLINE);
                        paraGraph.add(populateParagraphCell(OLEConstants.OlePatronBill.LABEL_TOTAL_AMOUNT,feeTypeList.get(j).getFeeAmount() != null ? "$"+feeTypeList.get(j).getFeeAmount().toString() : "$0"));
                        paraGraph.add(Chunk.NEWLINE);
                        paraGraph.add(populateParagraphCell(OLEConstants.OlePatronBill.LABEL_PAID_AMOUNT,oleItemLevelBillPayment.getAmount() != null ? "$"+oleItemLevelBillPayment.getAmount().toString() : "$0"));
                        paraGraph.add(Chunk.NEWLINE);
                        paraGraph.add(populateParagraphCell(OLEConstants.OlePatronBill.LABEL_TRANSACTION_NUMBER,oleItemLevelBillPayment.getTransactionNumber() != null ? oleItemLevelBillPayment.getAmount().toString() : " "));
                        paraGraph.add(Chunk.NEWLINE);
                        paraGraph.add(populateParagraphCell(OLEConstants.OlePatronBill.LABEL_TRANSACTION_NOTE,oleItemLevelBillPayment.getTransactionNote() != null ? oleItemLevelBillPayment.getTransactionNumber() : " "));
                        paraGraph.add(Chunk.NEWLINE);
                        paraGraph.add(populateParagraphCell(OLEConstants.OlePatronBill.LABEL_PAYMENT_MODE,oleItemLevelBillPayment.getPaymentMode() != null ? oleItemLevelBillPayment.getTransactionNote() : " "));
                        paraGraph.add(Chunk.NEWLINE);
                        paraGraph.add(populateParagraphCell(OLEConstants.OlePatronBill.LABEL_NOTE,feeTypeList.get(j).getGeneralNote() != null ? feeTypeList.get(j).getGeneralNote() : " "));
                        paraGraph.add(Chunk.NEWLINE);
                        feeAmount = feeAmount.add(feeTypeList.get(j).getFeeAmount().bigDecimalValue());
                        billNumber = feeTypeList.get(j).getBillNumber();
                        paidAmount=paidAmount.add(oleItemLevelBillPayment.getAmount().bigDecimalValue());
                    }

                }
            }


            paraGraph.add(Chunk.NEWLINE);
            paraGraph.add(new Chunk(OLEConstants.TOT_AMT + " : " +"$"+ feeAmount.subtract(paidAmount)!=null?feeAmount.subtract(paidAmount).toString():"0" + "", printFontMap.get("Total_Font")).setBackground(printColorMap.get("Total_BGColor")));
            response.setContentType("application/pdf");
            ServletOutputStream sos = response.getOutputStream();
            PdfWriter.getInstance(document, sos);
            document.open();
            document.add(paraGraph);
            document.close();
            byteArrayOutputStream.flush();
            byteArrayOutputStream.close();
            sos.flush();
            sos.close();
        } catch (Exception e) {
            LOG.error("Exception while creating pdf", e);
        }
    }


    public Document getDocument(float f1, float f2, float f3, float f4) {
        Document document = new Document(PageSize.A4);
        document.setMargins(f1, f2, f3, f4);
        return document;
    }

    /**
     * Used to create pdf document for patron bill
     *
     * @param patronBillPayments,feeTypeList,response
     *
     * @return Void
     */
    public void createPdfWithTable(String firstName, String lastName, List<PatronBillPayment> patronBillPayments, List<FeeType> feeTypeList,boolean isDefaultPrint,List<String> transactionIds, HttpServletResponse response) {
        LOG.debug("Initialize Table pdf Template");
        OutputStream out = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BigDecimal feeAmount = BigDecimal.valueOf(0);
        BigDecimal paidAmount = BigDecimal.valueOf(0);
        try {
            populateColorMap();
            populateFontMap();
            populatePrintColorMap();
            populatePrintFontMap();
            response.setContentType("application/pdf");
            Document document = this.getDocument(0, 0, 0, 0);
            document.open();
            document.newPage();
            PdfPTable pdfTable = new PdfPTable(9);
            pdfTable.getDefaultCell().setBorder(0);
            Table table = new Table(15);
            int headerwidths[] = {5,5,8,9,9,9,20,10,15,7,7,14,15,7,15};
            table.setWidths(headerwidths);
            table.setWidth(97);
            //table.setWidth(100);
            table.setDefaultVerticalAlignment(Element.ALIGN_TOP);
            table.setCellsFitPage(true);
            table.setPadding(1);
            table.setSpacing(0);
            table.getMarkupAttributeNames();
            Paragraph paraGraph = new Paragraph();
            paraGraph.setAlignment(Element.ALIGN_CENTER);
            paraGraph.add(new Chunk(OLEConstants.OlePatronBill.HEADER_PATRON_RECEIPT));
            paraGraph.add(Chunk.NEWLINE);
            paraGraph.add(Chunk.NEWLINE);
            paraGraph.add(Chunk.NEWLINE);
            SimpleDateFormat df=new SimpleDateFormat(OLEConstants.DAT_FORMAT_EFFECTIVE_PRINT);
            paraGraph.add(new Chunk(OLEConstants.BILL_DT + " : " + df.format(System.currentTimeMillis()) + " "));
            paraGraph.add(Chunk.NEWLINE);
            paraGraph.add(new Chunk(OLEConstants.FIRST_NAME + " : " + firstName, printFontMap.get("Patron_Name_Font")));
            paraGraph.add(Chunk.NEWLINE);
            paraGraph.add(new Chunk(OLEConstants.LAST_NAME + "  : " + lastName, printFontMap.get("Patron_Name_Font")));
            paraGraph.add(Chunk.NEWLINE);
            table.addCell(populateCellHeader(OLEConstants.OlePatronBill.LABEL_PATRON_RECEIPT_NUMBER, Color.gray));
            table.addCell(populateCellHeader(OLEConstants.OlePatronBill.LABEL_BILL_NUMBER, Color.gray));
            table.addCell(populateCellHeader(OLEConstants.OlePatronBill.LABEL_FEE_TYPE, Color.gray));
            table.addCell(populateCellHeader(OLEConstants.OlePatronBill.LABEL_TRANSACTION_DATE, Color.gray));
            table.addCell(populateCellHeader(OLEConstants.OlePatronBill.LABEL_OPERATOR_ID,Color.gray));
            table.addCell(populateCellHeader(OLEConstants.OlePatronBill.LABEL_ITEM_BARCODE, Color.gray));
            table.addCell(populateCellHeader(OLEConstants.OlePatronBill.LABEL_ITEM_TITLE,Color.gray));
            table.addCell(populateCellHeader(OLEConstants.OlePatronBill.LABEL_ITEM_AUTHOR,Color.gray));
            table.addCell(populateCellHeader(OLEConstants.OlePatronBill.LABEL_ITEM_CALL_NUMBER,Color.gray));
            table.addCell(populateCellHeader(OLEConstants.OlePatronBill.LABEL_TOTAL_AMOUNT,Color.gray));
            table.addCell(populateCellHeader(OLEConstants.OlePatronBill.LABEL_PAID_AMOUNT,Color.gray));
            table.addCell(populateCellHeader(OLEConstants.OlePatronBill.LABEL_TRANSACTION_NUMBER,Color.gray));
            table.addCell(populateCellHeader(OLEConstants.OlePatronBill.LABEL_TRANSACTION_NOTE,Color.gray));
            table.addCell(populateCellHeader(OLEConstants.OlePatronBill.LABEL_PAYMENT_MODE,Color.gray));
            table.addCell(populateCellHeader(OLEConstants.OlePatronBill.LABEL_NOTE,Color.gray));
            table.endHeaders();

            for (FeeType feeType : feeTypeList) {

                List<OleItemLevelBillPayment> oleItemLevelBillPayments = new ArrayList<>();
                if (feeType.getItemLevelBillPaymentList() != null) {
                    oleItemLevelBillPayments.addAll(feeType.getItemLevelBillPaymentList());
                } else {
                    Map<String,String> map=new HashMap<String,String>();
                    map.put("lineItemId",feeType.getId());
                    List<OleItemLevelBillPayment> itemLevelBillPayments=(List<OleItemLevelBillPayment>)KRADServiceLocator.getBusinessObjectService().findMatching(OleItemLevelBillPayment.class,map);
                    if(itemLevelBillPayments!=null){
                        oleItemLevelBillPayments.addAll(itemLevelBillPayments);
                    }
                }
                String feeTypeName="";
                if(feeType.getOleFeeType()!=null && feeType.getOleFeeType().getFeeTypeName()!=null){
                    feeTypeName=feeType.getOleFeeType().getFeeTypeName();
                }

                for (OleItemLevelBillPayment oleItemLevelBillPayment : oleItemLevelBillPayments) {
                    boolean isAddContent = false;
                    if(isDefaultPrint){
                        if(transactionIds.contains(oleItemLevelBillPayment.getPaymentId())){
                            isAddContent=true;
                        }
                    }  else {
                        isAddContent=true;
                    }
                    if (isAddContent) {
                        table.addCell(populateCell(oleItemLevelBillPayment.getPaymentId() != null ? oleItemLevelBillPayment.getPaymentId() : " "));
                        table.addCell(populateCell(feeType.getBillNumber() != null ? feeType.getBillNumber() : " "));
                        table.addCell(populateCell(feeTypeName));
                        table.addCell(populateCell((oleItemLevelBillPayment.getPaymentDate() != null ? df.format(oleItemLevelBillPayment.getPaymentDate()) : " ")));
                        table.addCell(populateCell(oleItemLevelBillPayment.getCreatedUser() != null ? oleItemLevelBillPayment.getCreatedUser() : " "));
                        table.addCell(populateCell(feeType.getItemBarcode() != null ? feeType.getItemBarcode() : " "));
                        table.addCell(populateCell(feeType.getItemTitle() != null ? feeType.getItemTitle() : " "));
                        table.addCell(populateCell(feeType.getItemAuthor() != null ? feeType.getItemAuthor() : " "));
                        table.addCell(populateCell(feeType.getItemCallNumber() != null ? feeType.getItemCallNumber() : " "));
                        table.addCell(populateCell(feeType.getFeeAmount() != null ? "$"+feeType.getFeeAmount().bigDecimalValue().setScale(2, BigDecimal.ROUND_HALF_UP) : "$0"));
                        table.addCell(populateCell(oleItemLevelBillPayment.getAmount() != null ? "$"+oleItemLevelBillPayment.getAmount().bigDecimalValue().setScale(2, BigDecimal.ROUND_HALF_UP) : "$0"));
                        table.addCell(populateCell(oleItemLevelBillPayment.getTransactionNumber() != null ? oleItemLevelBillPayment.getTransactionNumber() : " "));
                        table.addCell(populateCell(oleItemLevelBillPayment.getTransactionNote() != null ? oleItemLevelBillPayment.getTransactionNote() : " "));
                        table.addCell(populateCell(oleItemLevelBillPayment.getPaymentMode() != null ? oleItemLevelBillPayment.getPaymentMode() : " "));
                        table.addCell(populateCell(feeType.getGeneralNote() != null ? feeType.getGeneralNote() : " "));
                        feeAmount = feeAmount.add(feeType.getFeeAmount().bigDecimalValue());
                        paidAmount=paidAmount.add(oleItemLevelBillPayment.getAmount().bigDecimalValue());
                    }

                }
            }
            String totaldueAmount=feeAmount.subtract(paidAmount)!=null?feeAmount.subtract(paidAmount).toString():"0";
         /*   paraGraph.add(new Chunk(OLEConstants.TOT_AMT + " : " + "$" + totaldueAmount + "",printFontMap.get("Patron_Name_Font")));
            paraGraph.add(Chunk.NEWLINE);*/
            paraGraph.add(new Chunk(OLEConstants.TOT_AMT_PAID + " : " + "$" + (paidAmount!=null?paidAmount.toString():"0") + "",printFontMap.get("Patron_Name_Font")));
            paraGraph.add(Chunk.NEWLINE);
            response.setContentType("application/pdf");
            ServletOutputStream sos = response.getOutputStream();
            PdfWriter.getInstance(document, sos);
            document.open();
            document.add(paraGraph);
            document.add(table);
            document.close();
            byteArrayOutputStream.flush();
            byteArrayOutputStream.close();
        } catch (Exception e) {
            LOG.error("Exception while creating pdf with table", e);
        }
    }

    private Cell  populateCellHeader(String header,Color color){
        BaseFont bf=null;
        Cell cell = new Cell();
        try {
            if (header != null) {
                bf = BaseFont.createFont(BaseFont.COURIER, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
                float glyphWidth = bf.getWidth(header);
                float width = glyphWidth * 0.001f * 16f;
                float fontSize = 400 * width / glyphWidth;
                Font font=new Font();
                font.setSize(fontSize);
                cell.addElement(new Paragraph(header,font));

            }
        } catch (DocumentException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        cell.setBackgroundColor(color);
        return cell;
    }

    private Cell  populateCell(String header){
        BaseFont bf=null;
        Cell cell = new Cell();
        try {
            if (header != null) {
                bf = BaseFont.createFont(BaseFont.COURIER, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
                float glyphWidth = bf.getWidth(header);
                float width = glyphWidth * 0.001f * 16f;
                float fontSize = 400 * width / glyphWidth;
                Font font=new Font();
                font.setSize(fontSize);
                cell.addElement(new Paragraph(header, font));
            }
        } catch (DocumentException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return cell;
    }

    private Chunk  populateParagraphCell(String header,String value){
        return (new Chunk(header+" : " + value ,printFontMap.get("Patron_Name_Font")));
    }
}


