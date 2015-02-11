package org.kuali.ole.deliver.printSlip;



import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.batch.OleDeliverBatchServiceImpl;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.ItemOleml;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.common.search.SearchResult;
import org.kuali.ole.docstore.common.search.SearchResultField;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.kim.impl.identity.address.EntityAddressBo;
import org.kuali.rice.kim.impl.identity.email.EntityEmailBo;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameBo;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * The OlePrintSlip  class used to create pdf document .
 */
public class OlePrintSlip extends PdfPageEventHelper {

    private static final Logger LOG = Logger.getLogger(OlePrintSlip.class);
    Font boldFont = FontFactory.getFont("Times-Roman", 12, Font.BOLD);

    private Map<String, Font> printFontMap = new HashMap<String, Font>();

    private Map<String, Font> fontMap = new HashMap<String, Font>();
    private Map<String, BaseColor> colorMap = new HashMap<String, BaseColor>();
    private Map<String, BaseColor> printColorMap = new HashMap<String, BaseColor>();
    private DocstoreClientLocator docstoreClientLocator;
    private OleDeliverBatchServiceImpl oleDeliverBatchService;

    public OleDeliverBatchServiceImpl getOleDeliverBatchService() {
        if(oleDeliverBatchService==null){
            oleDeliverBatchService=SpringContext.getBean(OleDeliverBatchServiceImpl.class);
        }
        return oleDeliverBatchService;
    }

    public void setOleDeliverBatchService(OleDeliverBatchServiceImpl oleDeliverBatchService) {
        this.oleDeliverBatchService = oleDeliverBatchService;
    }

    public DocstoreClientLocator getDocstoreClientLocator() {

        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);

        }
        return docstoreClientLocator;
    }

    public com.itextpdf.text.Font getBoldFont(){
        Font boldFont = FontFactory.getFont("Times-Roman", 15, Font.BOLD);
        return boldFont;
    }



    public com.itextpdf.text.Font getDefaultFont(){
        return getOleDeliverBatchService().getDefaultFont();
    }

    public com.itextpdf.text.Font getFont(String data){
        return getOleDeliverBatchService().getFont(data);
    }

    public void populateFontMap() {

        fontMap.put("COURIER", new Font(FontFactory.getFont("Times-Roman", Font.BOLD).getBaseFont()));
        fontMap.put("COURIER", new Font(FontFactory.getFont("Times-Roman", Font.BOLDITALIC).getBaseFont()));
        fontMap.put("COURIER", new Font(FontFactory.getFont("Times-Roman", Font.DEFAULTSIZE).getBaseFont()));
        fontMap.put("COURIER", new Font(FontFactory.getFont("Times-Roman", Font.STRIKETHRU).getBaseFont()));
        fontMap.put("COURIER", new Font(FontFactory.getFont("Times-Roman", Font.NORMAL).getBaseFont()));
        fontMap.put("COURIER", new Font(FontFactory.getFont("Times-Roman", Font.UNDEFINED).getBaseFont()));
        fontMap.put("COURIER", new Font(FontFactory.getFont("Times-Roman", Font.UNDERLINE).getBaseFont()));

       /* fontMap.put("COURIER", new Font(Font.COURIER));
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
        fontMap.put("ZAPFDINGBATS", new Font(Font.ZAPFDINGBATS));*/

    }

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


    /**
     * Used to create pdf document for hold slip
     *
     * @param oleLoanDocument
     * @return Void
     */
    public void createPdfForPrintingSlip(OleLoanDocument oleLoanDocument, HttpServletResponse response) {
        LOG.debug("Initialize Normal pdf Template");
        LoanProcessor loanProcessor = new LoanProcessor();
        OleDeliverRequestBo oleDeliverRequestBo = oleLoanDocument.getOleDeliverRequestBo();
        OlePatronDocument oleRequestPatronDocument = oleDeliverRequestBo != null ? loanProcessor.getOlePatronDocument(oleDeliverRequestBo.getBorrowerId()) : null;
        EntityNameBo nameBo = oleRequestPatronDocument != null ? oleRequestPatronDocument.getEntity().getNames().get(0) : null;
        String patronName = nameBo != null ? nameBo.getLastName() + "," + nameBo.getFirstName() : null;
        Date expirationDate = oleRequestPatronDocument != null ? oleRequestPatronDocument.getExpirationDate() : null;
        OlePatronDocument olePatronDocument = oleLoanDocument != null ? loanProcessor.getOlePatronDocument(oleLoanDocument.getPatronId()) : null;
        OleLocation oleLocation = null;
        OleCirculationDesk oleCirculationDesk = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(OLEConstants.DATEFORMAT);
        SimpleDateFormat sdf = new SimpleDateFormat(OLEConstants.TIMESTAMP);
        try {
            if (oleLoanDocument.getCirculationLocationId() != null) {
                oleCirculationDesk = loanProcessor.getOleCirculationDesk(oleLoanDocument.getCirculationLocationId());
                oleLocation = oleCirculationDesk != null ? oleCirculationDesk.getOleCirculationDeskLocations().get(0).getLocation() : null;
            }
            String locationName = oleLocation != null ? oleLocation.getLocationName() : null;
            String itemBarcode = oleLoanDocument.getItemId();
            String itemCallNumber = null;
            String copyNumber = null;
            String volumeNumber = null;
            Item oleItem = oleLoanDocument.getOleItem();
            if (oleItem != null) {
                itemCallNumber = loanProcessor.getItemCallNumber(oleItem, oleLoanDocument.getInstanceUuid());
                copyNumber = oleItem.getCopyNumber();
                volumeNumber = oleItem.getEnumeration();
            } else {
                getItemDetails(loanProcessor, oleLoanDocument, oleItem, itemBarcode);
                itemCallNumber = loanProcessor.getItemCallNumber(oleItem, oleLoanDocument.getInstanceUuid());
                copyNumber = oleItem.getCopyNumber();
                volumeNumber = oleItem.getEnumeration();
            }
            if (copyNumber == null) {
                copyNumber = "";
            }
            if (volumeNumber == null) {
                volumeNumber = "";
            }
            String itemTitle = oleLoanDocument.getTitle();
            String requestedBy = null;
            if (oleDeliverRequestBo != null) {
                requestedBy = oleDeliverRequestBo.getFirstName() + " " + oleDeliverRequestBo.getLastName();
            }
            String routeTo = oleLoanDocument.getRouteToLocationName() != null ? oleLoanDocument.getRouteToLocationName() : oleCirculationDesk.getCirculationDeskPublicName();
            String checkInNote = oleItem != null ? oleItem.getCheckinNote() : null;
            String itemStatusCode = oleLoanDocument.getItemStatusCode() != null ? oleLoanDocument.getItemStatusCode() : "";
            boolean checkInNoteDisplay = checkInNote != null && !checkInNote.trim().isEmpty() && !oleLoanDocument.getItemStatusCode().contains(OLEConstants.ITEM_STATUS_IN_TRANSIT) && oleLoanDocument.getRouteToLocation() != null && !oleLoanDocument.getRouteToLocation().trim().isEmpty();
            boolean holdSlip = itemStatusCode.equalsIgnoreCase(OLEConstants.ITEM_STATUS_ON_HOLD);
            boolean inTransitSlip = itemStatusCode.equalsIgnoreCase(OLEConstants.ITEM_STATUS_IN_TRANSIT);
            boolean inTransitForHold = itemStatusCode.equalsIgnoreCase(OLEConstants.ITEM_STATUS_IN_TRANSIT_HOLD);
            boolean inTransitPerStaff = itemStatusCode.equalsIgnoreCase(OLEConstants.ITEM_STATUS_IN_TRANSIT_STAFF);
            boolean missingPieceCheck = oleLoanDocument.isMissingPieceFlag();
            boolean returnedDamaged = oleLoanDocument.isItemDamagedStatus();
            boolean claimsReturned = oleLoanDocument.isClaimsReturnedIndicator();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            populateColorMap();
            populateFontMap();
            response.setContentType("application/pdf");
            OutputStream os = null;

            if (holdSlip && oleCirculationDesk != null && oleCirculationDesk.getHoldFormat() != null && oleCirculationDesk.getHoldFormat().equals(OLEConstants.RECEIPT_PRINTER)) {
                getHoldSlipForReceiptPrinter(patronName, expirationDate, missingPieceCheck, oleCirculationDesk, itemBarcode, itemTitle, itemCallNumber, copyNumber, volumeNumber, response);
            } else {
                Document document = this.getDocument(0, 0, 5, 5);
                if (missingPieceCheck) {
                    os = response.getOutputStream();
                    PdfWriter.getInstance(document, os);
                }
                document.open();
                document.newPage();
                //Font getBoldFont() = new Font(Font.TIMES_ROMAN, 15, Font.BOLD);
                PdfPTable pdfTable = new PdfPTable(3);
                Paragraph paraGraph = new Paragraph();
                paraGraph.setAlignment(Element.ALIGN_CENTER);
                if (holdSlip) {
                    paraGraph.add(new Chunk("Hold Slip", getBoldFont()));
                } else if (inTransitSlip) {
                    paraGraph.add(new Chunk("Routing Slip In-Transit", getBoldFont()));
                } else if (inTransitForHold) {
                    paraGraph.add(new Chunk("Routing Slip In-Transit For Hold", getBoldFont()));
                } else if (inTransitPerStaff) {
                    paraGraph.add(new Chunk("Routing Slip In-Transit Per Staff Request", getBoldFont()));
                } else if (missingPieceCheck) {
                    paraGraph.add(new Chunk("Missing Pieces Notice", getBoldFont()));
                } else if (returnedDamaged) {
                    paraGraph.add(new Chunk("Returned Damaged", getBoldFont()));
                } else if (claimsReturned) {
                    paraGraph.add(new Chunk("Claims Returned Notice", getBoldFont()));
                } else if (checkInNoteDisplay) {
                    paraGraph.add(new Chunk("Routing Slip", getBoldFont()));
                } else {
                    paraGraph.add(new Chunk("Receipt(CheckIn) Slip", getBoldFont()));
                }
                paraGraph.add(Chunk.NEWLINE);
                paraGraph.add(Chunk.NEWLINE);
                paraGraph.add(Chunk.NEWLINE);
                if (holdSlip) {
                    pdfTable.addCell(getPdfPCellInJustified("Patron Name"));
                    pdfTable.addCell(getPdfPCellInLeft(":"));
                    pdfTable.addCell(getPdfPCellInJustified(patronName));

                    pdfTable.addCell(getPdfPCellInJustified("Expiration Date"));
                    pdfTable.addCell(getPdfPCellInLeft(":"));
                    pdfTable.addCell(getPdfPCellInJustified(expirationDate != null ? dateFormat.format(expirationDate).toString() : null));
                } else if (inTransitSlip || checkInNoteDisplay) {

                    pdfTable.addCell(getPdfPCellInJustified("Route To"));
                    pdfTable.addCell(getPdfPCellInLeft(":"));
                    pdfTable.addCell(getPdfPCellInJustified(routeTo));

                    pdfTable.addCell(getPdfPCellInJustified("Date/Time "));
                    pdfTable.addCell(getPdfPCellInLeft(":"));
                    pdfTable.addCell(getPdfPCellInJustified(sdf.format(oleLoanDocument.getCheckInDate()).toString()));

                } else if (inTransitForHold) {

                    pdfTable.addCell(getPdfPCellInJustified("Route To"));
                    pdfTable.addCell(getPdfPCellInLeft(":"));
                    pdfTable.addCell(getPdfPCellInJustified(routeTo));

                    pdfTable.addCell(getPdfPCellInJustified("Place on hold for"));
                    pdfTable.addCell(getPdfPCellInLeft(":"));
                    pdfTable.addCell(getPdfPCellInJustified(requestedBy));


                    pdfTable.addCell(getPdfPCellInJustified("Date/Time "));
                    pdfTable.addCell(getPdfPCellInLeft(":"));
                    pdfTable.addCell(getPdfPCellInJustified(sdf.format(oleLoanDocument.getCheckInDate()).toString()));

                } else if (inTransitPerStaff) {

                    pdfTable.addCell(getPdfPCellInJustified("Route To"));
                    pdfTable.addCell(getPdfPCellInLeft(":"));
                    pdfTable.addCell(getPdfPCellInJustified(routeTo));

                    pdfTable.addCell(getPdfPCellInJustified("Requested By"));
                    pdfTable.addCell(getPdfPCellInLeft(":"));
                    pdfTable.addCell(getPdfPCellInJustified(requestedBy));


                    pdfTable.addCell(getPdfPCellInJustified("Date/Time "));
                    pdfTable.addCell(getPdfPCellInLeft(":"));
                    pdfTable.addCell(getPdfPCellInJustified(sdf.format(oleLoanDocument.getCheckInDate()).toString()));

                } else if (returnedDamaged) {
                    pdfTable.addCell(getPdfPCellInJustified("Route To"));
                    pdfTable.addCell(getPdfPCellInLeft(":"));
                    pdfTable.addCell(getPdfPCellInJustified(routeTo));
                } else if (missingPieceCheck) {
                    document.add(paraGraph);
                    //Circulation Desk
                    pdfTable.addCell(getPdfPCellInJustified("Circulation Location / Library Name"));
                    pdfTable.addCell(getPdfPCellInLeft(":"));
                    pdfTable.addCell(getPdfPCellInJustified(oleCirculationDesk != null ? oleCirculationDesk.getCirculationDeskPublicName() : ""));

                    pdfTable.addCell(getPdfPCellInJustified("Address"));
                    pdfTable.addCell(getPdfPCellInLeft(":"));
                    pdfTable.addCell(getPdfPCellInJustified(""));

                    pdfTable.addCell(getPdfPCellInJustified("Email"));
                    pdfTable.addCell(getPdfPCellInLeft(":"));
                    pdfTable.addCell(getPdfPCellInJustified("kuali.ole@org.com"));

                    pdfTable.addCell(getPdfPCellInJustified("Phone #"));
                    pdfTable.addCell(getPdfPCellInLeft(":"));
                    pdfTable.addCell(getPdfPCellInJustified("111-5555"));

                    document.add(pdfTable);
                    paraGraph = new Paragraph();
                    paraGraph.add(Chunk.NEWLINE);
                    document.add(paraGraph);

                    //Patron
                    paraGraph = new Paragraph();
                    paraGraph.add(new Chunk("Addressee", getBoldFont()));
                    paraGraph.add(Chunk.NEWLINE);
                    document.add(paraGraph);

                    pdfTable = new PdfPTable(3);
                    pdfTable.addCell(getPdfPCellInJustified("Borrower Name"));
                    pdfTable.addCell(getPdfPCellInLeft(":"));
                    pdfTable.addCell(getPdfPCellInJustified(patronName));

                    pdfTable.addCell(getPdfPCellInJustified("Address"));
                    pdfTable.addCell(getPdfPCellInLeft(":"));
                    pdfTable.addCell(getPdfPCellInJustified(""));

                    pdfTable.addCell(getPdfPCellInJustified("Email"));
                    pdfTable.addCell(getPdfPCellInLeft(":"));
                    pdfTable.addCell(getPdfPCellInJustified(olePatronDocument != null && olePatronDocument.getEmailAddress() != null ? olePatronDocument.getEmailAddress() : ""));

                    pdfTable.addCell(getPdfPCellInJustified("Phone #"));
                    pdfTable.addCell(getPdfPCellInLeft(":"));
                    pdfTable.addCell(getPdfPCellInJustified(olePatronDocument != null && olePatronDocument.getPhoneNumber() != null ? olePatronDocument.getPhoneNumber() : ""));

                    document.add(pdfTable);
                    //Notice Type
                    paraGraph = new Paragraph();
                    paraGraph.add(new Chunk("Return with Missing Piece(s) Notice", getBoldFont()));
                    paraGraph.setAlignment(Element.ALIGN_CENTER);
                    paraGraph.add(Chunk.NEWLINE);
                    document.add(paraGraph);


                    //Notice-specific text
                    paraGraph = new Paragraph();
                    paraGraph.add(new Chunk("The following item(s) returned by you is missing one or more of its pieces.Please return the missing piece(s) to the library shown above or contact the library about this matter to avoid incurring any penalties.", getBoldFont()));
                    paraGraph.setAlignment(Element.ALIGN_CENTER);
                    paraGraph.add(Chunk.NEWLINE);
                    document.add(paraGraph);

                    //Title/item information
                    paraGraph = new Paragraph();
                    paraGraph.add(new Chunk("Title/item information", getBoldFont()));
                    paraGraph.add(Chunk.NEWLINE);
                    document.add(paraGraph);

                    pdfTable = new PdfPTable(3);
                    pdfTable.addCell(getPdfPCellInJustified("Title"));
                    pdfTable.addCell(getPdfPCellInLeft(":"));
                    pdfTable.addCell(getPdfPCellInJustified(itemTitle));

                    pdfTable.addCell(getPdfPCellInJustified("Author"));
                    pdfTable.addCell(getPdfPCellInLeft(":"));
                    pdfTable.addCell(getPdfPCellInJustified(oleLoanDocument.getAuthor()));

                    pdfTable.addCell(getPdfPCellInJustified("Volume/Issue/Copy #"));
                    pdfTable.addCell(getPdfPCellInLeft(":"));
                    pdfTable.addCell(getPdfPCellInJustified(copyNumber));

                    pdfTable.addCell(getPdfPCellInJustified("Library shelving location "));
                    pdfTable.addCell(getPdfPCellInLeft(":"));
                    pdfTable.addCell(getPdfPCellInJustified(locationName));

                    pdfTable.addCell(getPdfPCellInJustified("Call #"));
                    pdfTable.addCell(getPdfPCellInLeft(":"));
                    pdfTable.addCell(getPdfPCellInJustified(itemCallNumber));

                    pdfTable.addCell(getPdfPCellInJustified("Item barcode"));
                    pdfTable.addCell(getPdfPCellInLeft(":"));
                    pdfTable.addCell(getPdfPCellInJustified(itemBarcode));
                    document.add(pdfTable);

                }
                if (!missingPieceCheck) {
                    pdfTable.addCell(getPdfPCellInJustified("Route From"));
                    pdfTable.addCell(getPdfPCellInLeft(":"));
                    pdfTable.addCell(getPdfPCellInJustified(oleCirculationDesk != null ? oleCirculationDesk.getCirculationDeskPublicName() : ""));

                    pdfTable.addCell(getPdfPCellInJustified("Item Barcode"));
                    pdfTable.addCell(getPdfPCellInLeft(":"));
                    pdfTable.addCell(getPdfPCellInJustified(itemBarcode));

                    pdfTable.addCell(getPdfPCellInJustified("Title"));
                    pdfTable.addCell(getPdfPCellInLeft(":"));
                    pdfTable.addCell(getPdfPCellInJustified(itemTitle));

                    pdfTable.addCell(getPdfPCellInJustified("Call Number"));
                    pdfTable.addCell(getPdfPCellInLeft(":"));
                    pdfTable.addCell(getPdfPCellInJustified(itemCallNumber));

                    pdfTable.addCell(getPdfPCellInJustified("Copy Number"));
                    pdfTable.addCell(getPdfPCellInLeft(":"));
                    pdfTable.addCell(getPdfPCellInJustified(copyNumber));
                    if (!(holdSlip | inTransitSlip | inTransitForHold | inTransitPerStaff | checkInNoteDisplay)) {
                        pdfTable.addCell(getPdfPCellInJustified("Check-in Date"));
                        pdfTable.addCell(getPdfPCellInLeft(":"));
                        pdfTable.addCell(getPdfPCellInJustified(sdf.format(oleLoanDocument.getCheckInDate()).toString()));

                        pdfTable.addCell(getPdfPCellInJustified("Patron Barcode"));
                        pdfTable.addCell(getPdfPCellInLeft(":"));
                        pdfTable.addCell(getPdfPCellInJustified(olePatronDocument != null && olePatronDocument.getBarcode() != null ? olePatronDocument.getBarcode() : ""));
                    }
                }
                if (holdSlip) {
                    if (oleCirculationDesk != null) {
                        int noDays = Integer.parseInt(oleCirculationDesk.getOnHoldDays());
                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.DATE, noDays);
                        Date date = calendar.getTime();
                        if (date != null) {
                            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                            pdfTable.addCell(getPdfPCellInJustified("Expiration onHoldDate"));
                            pdfTable.addCell(getPdfPCellInLeft(":"));
                            String dateString = date == null ? "" : df.format(date);
                            pdfTable.addCell(getPdfPCellInJustified(dateString));
                        }
                    }
                }
                response.setContentType("application/pdf");
                if (!missingPieceCheck) {
                    os = response.getOutputStream();
                    PdfWriter.getInstance(document, os);
                    document.open();
                    document.add(paraGraph);
                    document.add(pdfTable);
                    document.close();
                } else {
                    document.close();
                }
                byteArrayOutputStream.flush();
                byteArrayOutputStream.close();
                os.flush();
                os.close();
            }
        }catch (Exception e) {
            LOG.error("Exception while creating pdf for printing slip", e);
        }
    }

    /**
     * Used to create pdf document for checkout slip
     *
     * @param oleLoanDocument
     * @return Void
     */
    public void createPdfForBackGroundCheckOut(OleLoanDocument oleLoanDocument, HttpServletResponse response) {
        LOG.debug("Initialize Normal pdf Template");
        LoanProcessor loanProcessor = new LoanProcessor();
        OlePatronDocument olePatronDocument = oleLoanDocument != null ? loanProcessor.getOlePatronDocument(oleLoanDocument.getPatronId()) : null;
        if (olePatronDocument != null) {
            EntityBo entityBo = olePatronDocument.getEntity();
            EntityNameBo nameBo = entityBo != null ? entityBo.getNames().get(0) : null;
            String patronName = nameBo != null ? nameBo.getFirstName() + " " + nameBo.getLastName() : null;
            List<EntityEmailBo> entityEmailBos = entityBo != null && entityBo.getEntityTypeContactInfos().size() > 0 ? entityBo.getEntityTypeContactInfos().get(0).getEmailAddresses() : new ArrayList<EntityEmailBo>();
            List<EntityAddressBo> entityAddressBos = entityBo != null && entityBo.getEntityTypeContactInfos().size() > 0 ? entityBo.getEntityTypeContactInfos().get(0).getAddresses() : new ArrayList<EntityAddressBo>();
            String defaultEmailAddress = null;
            String defaultAddress = "";
            for (EntityEmailBo entityEmailBo : entityEmailBos) {
                if (entityEmailBo.isDefaultValue()) {
                    defaultEmailAddress = entityEmailBo.getEmailAddress();
                    break;
                }
            }
            String line1 = "";
            String line2 = "";
            String line3 = "";
            String city = "";
            String stateProvisionalCode = "";
            String countryCode = "";
            String postalCode = "";
            for (EntityAddressBo entityAddressBo : entityAddressBos) {
                if (entityAddressBo.isDefaultValue()) {
                    line1 = entityAddressBo.getLine1() != null && !entityAddressBo.getLine1().isEmpty() ? entityAddressBo.getLine1() + OLEConstants.COMMA : "";
                    line2 = entityAddressBo.getLine2() != null && !entityAddressBo.getLine2().isEmpty() ? entityAddressBo.getLine2() + OLEConstants.COMMA : "";
                    line3 = entityAddressBo.getLine3() != null && !entityAddressBo.getLine3().isEmpty() ? entityAddressBo.getLine3() + OLEConstants.COMMA : "";
                    city = entityAddressBo.getCity() != null && !entityAddressBo.getCity().isEmpty() ? entityAddressBo.getCity() + OLEConstants.COMMA : "";
                    stateProvisionalCode = entityAddressBo.getStateProvinceCode() != null && !entityAddressBo.getStateProvinceCode().isEmpty() ? entityAddressBo.getStateProvinceCode() + OLEConstants.COMMA : "";
                    countryCode = entityAddressBo.getCountryCode() != null && !entityAddressBo.getCountryCode().isEmpty() ? entityAddressBo.getCountryCode() + OLEConstants.COMMA : "";
                    postalCode = entityAddressBo.getPostalCode() != null && !entityAddressBo.getPostalCode().isEmpty() ? entityAddressBo.getPostalCode() : "";
                }
            }
            defaultAddress = line1 + line2 + line3 + city + stateProvisionalCode + countryCode + postalCode;
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                populateColorMap();
                populateFontMap();
                response.setContentType("application/pdf");
                OutputStream os = null;
                Document document = this.getDocument(0, 0, 5, 5);
                document.open();
                document.newPage();
                //Font getBoldFont() = new Font(Font.TIMES_ROMAN, 15, Font.BOLD);
                PdfPTable pdfTable = new PdfPTable(1);
                Paragraph paraGraph = new Paragraph();
                paraGraph.setAlignment(Element.ALIGN_CENTER);
                paraGraph.add(new Chunk("Mailing Label", getBoldFont()));
                paraGraph.add(Chunk.NEWLINE);
                paraGraph.add(Chunk.NEWLINE);
                paraGraph.add(Chunk.NEWLINE);
                pdfTable.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfTable.addCell(getPdfPCellInJustified(patronName));
                if (defaultAddress != null && !defaultAddress.trim().isEmpty()) {
                    if (!line1.isEmpty()) pdfTable.addCell(getPdfPCellInJustified(line1));
                    if (!line2.isEmpty()) pdfTable.addCell(getPdfPCellInJustified(line2));
                    if (!line3.isEmpty()) pdfTable.addCell(getPdfPCellInJustified(line3));
                    if (!city.isEmpty()) pdfTable.addCell(getPdfPCellInJustified(city));
                    if (!stateProvisionalCode.isEmpty()) pdfTable.addCell(getPdfPCellInJustified(stateProvisionalCode));
                    if (!countryCode.isEmpty()) pdfTable.addCell(getPdfPCellInJustified(countryCode));
                    if (!postalCode.isEmpty()) pdfTable.addCell(getPdfPCellInJustified(postalCode));
                } else {
                    if (defaultEmailAddress != null && !defaultEmailAddress.trim().isEmpty()) {
                        pdfTable.addCell(getPdfPCellInJustified(defaultEmailAddress));
                    }
                }
                document.add(pdfTable);
                response.setContentType("application/pdf");
                os = response.getOutputStream();
                PdfWriter.getInstance(document, os);
                document.open();
                document.add(paraGraph);
                document.add(pdfTable);
                document.close();
                byteArrayOutputStream.flush();
                byteArrayOutputStream.close();
                os.flush();
                os.close();
            } catch (Exception e) {
                LOG.error("Exception while creating pdf for backgroung check out", e);
            }
        }
    }


    public void createDueDateSlipPdf(List<OleLoanDocument> oleLoanDocument, HttpServletResponse response) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            populateColorMap();
            populateFontMap();
            SimpleDateFormat dateFormat = new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE);
            SimpleDateFormat sdf = new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE+" "+RiceConstants.SIMPLE_DATE_FORMAT_FOR_TIME);
            String date = dateFormat.format(System.currentTimeMillis());
            response.setContentType("application/pdf");
            Document document = this.getDocument(0, 0, 5, 5);
            PdfWriter writer = PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();
            document.newPage();
            //Font getBoldFont() = new Font(Font.TIMES_ROMAN, 15, Font.BOLD);
            PdfPTable pdfTable = new PdfPTable(3);
            Paragraph paraGraph = new Paragraph();
            paraGraph.setAlignment(Element.ALIGN_CENTER);
            paraGraph.add(new Chunk("Due Date Slip", getBoldFont()));
            paraGraph.add(Chunk.NEWLINE);
            paraGraph.add(Chunk.NEWLINE);
            paraGraph.add(Chunk.NEWLINE);
            LoanProcessor loanProcessor = new LoanProcessor();
            OleLocation oleLocation = null;
            OleCirculationDesk oleCirculationDesk = null;
            try {
                if (oleLoanDocument.get(0).getCirculationLocationId() != null) {
                    oleCirculationDesk = loanProcessor.getOleCirculationDesk(oleLoanDocument.get(0).getCirculationLocationId());
                }
                oleLocation = oleCirculationDesk.getOleCirculationDeskLocations().get(0).getLocation();
                // oleLocation = loanProcessor.getLocationByLocationId(oleLoanDocument.get(0).getCirculationLocationId());
            } catch (Exception e) {
                LOG.error("Exception", e);
            }
            String locationName = oleLocation != null ? oleLocation.getLocationName() : null;


            for (int dueDateSlip = 0; dueDateSlip < oleLoanDocument.size(); dueDateSlip++) {

                pdfTable.addCell(getPdfPCellInJustified("Circulation Location/Library Name"));
                pdfTable.addCell(getPdfPCellInLeft(":"));
                pdfTable.addCell(getPdfPCellInJustified(oleCirculationDesk != null ? oleCirculationDesk.getCirculationDeskPublicName() : ""));

                pdfTable.addCell(getPdfPCellInJustified("Item Barcode"));
                pdfTable.addCell(getPdfPCellInLeft(":"));
                pdfTable.addCell(getPdfPCellInJustified(oleLoanDocument.get(dueDateSlip).getItemId()));

                pdfTable.addCell(getPdfPCellInJustified("Title"));
                pdfTable.addCell(getPdfPCellInLeft(":"));
                pdfTable.addCell(getPdfPCellInJustified(oleLoanDocument.get(dueDateSlip).getTitle()));

                pdfTable.addCell(getPdfPCellInJustified("Call Number"));
                pdfTable.addCell(getPdfPCellInLeft(":"));
                pdfTable.addCell(getPdfPCellInJustified(oleLoanDocument.get(dueDateSlip).getItemCallNumber()));

                pdfTable.addCell(getPdfPCellInJustified("Copy Number"));
                pdfTable.addCell(getPdfPCellInLeft(":"));
                pdfTable.addCell(getPdfPCellInJustified(oleLoanDocument.get(dueDateSlip).getItemCopyNumber()));

                pdfTable.addCell(getPdfPCellInJustified("Due Date"));
                pdfTable.addCell(getPdfPCellInLeft(":"));
                pdfTable.addCell(getPdfPCellInJustified(oleLoanDocument.get(dueDateSlip).getLoanDueDate()!=null?
                        sdf.format(oleLoanDocument.get(dueDateSlip).getLoanDueDate()).toString():""));

                pdfTable.addCell(getPdfPCellInJustified("Patron Barcode"));
                pdfTable.addCell(getPdfPCellInLeft(":"));
                pdfTable.addCell(getPdfPCellInJustified(oleLoanDocument.get(dueDateSlip).getPatronBarcode()));



                /*paraGraph.add(Chunk.NEWLINE);
                paraGraph.add(Chunk.NEWLINE);
*/
                pdfTable.addCell(getEmptyCell());
                pdfTable.addCell(getEmptyCell());
                pdfTable.addCell(getEmptyCell());

                pdfTable.addCell(getEmptyCell());
                pdfTable.addCell(getEmptyCell());
                pdfTable.addCell(getEmptyCell());
            }
            response.setContentType("application/pdf");
            ServletOutputStream sos = response.getOutputStream();
            PdfWriter.getInstance(document, sos);
            document.open();
            document.add(paraGraph);
            document.add(pdfTable);
            document.close();
            String fileName = "Due date slip_" + date;
            ///OutputStream outputStream = new FileOutputStream(""+fileName+".pdf");
            // byteArrayOutputStream.writeTo(outputStream);
            byteArrayOutputStream.flush();
            byteArrayOutputStream.close();
            sos.flush();
            sos.close();
            // outputStream.close();
        } catch (Exception e) {
            LOG.error("Exception while creating due date pdf slip", e);
        }
    }

    private PdfPCell getEmptyCell() {
        PdfPCell pdfPCell = new PdfPCell(new Paragraph(Chunk.NEWLINE));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
        return pdfPCell;
    }

    private PdfPCell getPdfPCellInJustified(String chunk) {
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

    private PdfPCell getPdfPCellAligned(String chunk, int font, int val) {
        PdfPCell pdfPCell = new PdfPCell(new Phrase(chunk,FontFactory.getFont(FontFactory.TIMES_ROMAN, 16, font)));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setPaddingLeft(val);
        return pdfPCell;
    }

    private PdfPCell getPdfPCellInLeft(String chunk) {
        PdfPCell pdfPCell = new PdfPCell(new Paragraph(new Chunk(chunk, getDefaultFont())));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        return pdfPCell;
    }

    public Document getDocument(float f1, float f2, float f3, float f4) {
        Document document = new Document(PageSize.A4);
        document.setMargins(f1, f2, f3, f4);
        return document;
    }

    private void getHoldSlipForReceiptPrinter(String patronName, Date expirationDate, boolean missingPieceCheck, OleCirculationDesk oleCirculationDesk, String itemBarcode, String itemTitle, String itemCallNumber, String copyNumber, String volumeNumber, HttpServletResponse response) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat(OLEConstants.DATEFORMAT);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        OutputStream os = null;
        //Document document = new Document(new Rectangle(253, 430));
        Document document = new Document(PageSize.A4);
        document.setMargins(0, 0, 5, 5);
        document.open();
        document.newPage();
        PdfPTable pdfTable = new PdfPTable(1);
        pdfTable.addCell(getEmptyCell());
        pdfTable.addCell(getEmptyCell());
        pdfTable.addCell(getPdfPCellAligned("Hold Slip", Font.BOLD, 11));
        pdfTable.addCell(getEmptyCell());
        pdfTable.addCell(getPdfPCellAligned(patronName, Font.NORMAL, 11));
        if (oleCirculationDesk != null) {
            int noDays = Integer.parseInt(oleCirculationDesk.getOnHoldDays());
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, noDays);
            Date date = calendar.getTime();
            if (date != null) {
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                String dateString = date == null ? "" : df.format(date);
                pdfTable.addCell(getPdfPCellAligned(dateString, Font.NORMAL, 11));
            }
        }
        pdfTable.addCell(getEmptyCell());
        if (!missingPieceCheck) {
            pdfTable.addCell(getPdfPCellAligned(oleCirculationDesk != null ? oleCirculationDesk.getCirculationDeskPublicName() : "", Font.NORMAL, 11));
            pdfTable.addCell(getPdfPCellAligned(itemBarcode, Font.NORMAL, 11));
            pdfTable.addCell(getPdfPCellAligned(itemTitle, Font.NORMAL, 11));
            pdfTable.addCell(getPdfPCellAligned(itemCallNumber, Font.NORMAL, 11));
            pdfTable.addCell(getPdfPCellAligned(copyNumber, Font.NORMAL, 11));
            pdfTable.addCell(getPdfPCellAligned(volumeNumber, Font.NORMAL, 11));
        }
        pdfTable.addCell(getEmptyCell());
        pdfTable.addCell(getEmptyCell());
        pdfTable.addCell(getEmptyCell());
        pdfTable.addCell(getEmptyCell());
        pdfTable.addCell(getEmptyCell());
        pdfTable.addCell(getEmptyCell());
        pdfTable.addCell(getEmptyCell());
        pdfTable.addCell(getEmptyCell());
        response.setContentType("application/pdf");
        if (!missingPieceCheck) {
            os = response.getOutputStream();
            PdfWriter.getInstance(document, os);
            document.open();
            document.add(pdfTable);
            document.close();
        } else {
            document.close();
        }
        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();
        os.flush();
        os.close();
    }

    public void createHoldSlipPdf(List<OleLoanDocument> oleLoanDocumentList, HttpServletResponse response, OleCirculationDesk oleCirculationDesk) throws Exception {
        LoanProcessor loanProcessor = new LoanProcessor();
        SimpleDateFormat dateFormat = new SimpleDateFormat(OLEConstants.DATEFORMAT);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        OutputStream os = null;
        Document document;
        PdfPTable pdfTable;
        if (oleCirculationDesk != null && oleCirculationDesk.getHoldFormat().equals(OLEConstants.RECEIPT_PRINTER)) {
            document = new Document(new Rectangle(253, 430));
            document.open();
            document.newPage();
        } else {
            document = this.getDocument(0, 0, 5, 5);
            document.open();
            document.newPage();
        }
        response.setContentType("application/pdf");
        os = response.getOutputStream();
        PdfWriter.getInstance(document, os);
        document.open();
        for (OleLoanDocument oleLoanDocument : oleLoanDocumentList) {
            OleDeliverRequestBo oleDeliverRequestBo = oleLoanDocument.getOleDeliverRequestBo();
            OlePatronDocument oleRequestPatronDocument = oleDeliverRequestBo != null ? loanProcessor.getOlePatronDocument(oleDeliverRequestBo.getBorrowerId()) : null;
            EntityNameBo nameBo = oleRequestPatronDocument != null ? oleRequestPatronDocument.getEntity().getNames().get(0) : null;
            String patronName = nameBo != null ? nameBo.getLastName() + "," + nameBo.getFirstName() : null;
            Date expirationDate = oleRequestPatronDocument != null ? oleRequestPatronDocument.getExpirationDate() : null;
            String itemCallNumber = null;
            String copyNumber = null;
            String volumeNumber = null;
            String itemBarcode = oleLoanDocument.getItemId();
            Item oleItem = oleLoanDocument.getOleItem();
            if (oleItem != null) {
                itemCallNumber = loanProcessor.getItemCallNumber(oleItem, oleLoanDocument.getInstanceUuid());
                copyNumber = oleItem.getCopyNumber();
                volumeNumber = oleItem.getEnumeration();
            } else {
                getItemDetails(loanProcessor, oleLoanDocument, oleItem, itemBarcode);
                itemCallNumber = loanProcessor.getItemCallNumber(oleItem, oleLoanDocument.getInstanceUuid());
                copyNumber = oleItem.getCopyNumber();
                volumeNumber = oleItem.getEnumeration();
            }
            if (copyNumber == null) {
                copyNumber = "";
            }
            if (volumeNumber == null) {
                volumeNumber = "";
            }
            if (oleCirculationDesk != null && oleCirculationDesk.getHoldFormat().equals(OLEConstants.RECEIPT_PRINTER)) {
                pdfTable = new PdfPTable(1);
                pdfTable.addCell(getPdfPCellAligned("Hold Slip", Font.BOLD, -18));
                pdfTable.addCell(getEmptyCell());
                pdfTable.addCell(getPdfPCellAligned(patronName, Font.NORMAL, -18));
                if (oleCirculationDesk != null) {
                    int noDays = Integer.parseInt(oleCirculationDesk.getOnHoldDays());
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DATE, noDays);
                    Date date = calendar.getTime();
                    if (date != null) {
                        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                        String dateString = date == null ? "" : df.format(date);
                        pdfTable.addCell(getPdfPCellAligned(dateString, Font.NORMAL, -18));
                    }
                }
                pdfTable.addCell(getEmptyCell());
                if (!oleLoanDocument.isMissingPieceFlag()) {
                    pdfTable.addCell(getPdfPCellAligned(oleCirculationDesk != null ? oleCirculationDesk.getCirculationDeskPublicName() : "", Font.NORMAL, -18));
                    pdfTable.addCell(getPdfPCellAligned(itemBarcode, Font.NORMAL, -18));
                    pdfTable.addCell(getPdfPCellAligned(oleLoanDocument.getTitle(), Font.NORMAL, -18));
                    pdfTable.addCell(getPdfPCellAligned(itemCallNumber, Font.NORMAL, -18));
                    pdfTable.addCell(getPdfPCellAligned(copyNumber, Font.NORMAL, -18));
                    pdfTable.addCell(getPdfPCellAligned(volumeNumber, Font.NORMAL, -18));
                }
                pdfTable.addCell(getEmptyCell());
                pdfTable.addCell(getEmptyCell());
                pdfTable.addCell(getEmptyCell());
                pdfTable.addCell(getEmptyCell());
                document.add(pdfTable);
                document.newPage();

            } else {
                pdfTable = new PdfPTable(3);
                pdfTable.addCell(getEmptyCell());
                PdfPCell pdfPCell = new PdfPCell(new Phrase("Hold Slip", FontFactory.getFont(FontFactory.TIMES_ROMAN, 15, Font.BOLD)));
                pdfPCell.setBorder(pdfPCell.NO_BORDER);
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfTable.addCell(pdfPCell);
                pdfTable.addCell(getEmptyCell());

                pdfTable.addCell(getEmptyCell());
                pdfTable.addCell(getEmptyCell());
                pdfTable.addCell(getEmptyCell());

                pdfTable.addCell(getEmptyCell());
                pdfTable.addCell(getEmptyCell());
                pdfTable.addCell(getEmptyCell());

                pdfTable.addCell(getPdfPCellInJustified("Patron Name"));
                pdfTable.addCell(getPdfPCellInLeft(":"));
                pdfTable.addCell(getPdfPCellInJustified(patronName));

                pdfTable.addCell(getPdfPCellInJustified("Expiration Date"));
                pdfTable.addCell(getPdfPCellInLeft(":"));
                pdfTable.addCell(getPdfPCellInJustified(expirationDate != null ? dateFormat.format(expirationDate).toString() : null));
                if (!oleLoanDocument.isMissingPieceFlag()) {
                    pdfTable.addCell(getPdfPCellInJustified("Route From"));
                    pdfTable.addCell(getPdfPCellInLeft(":"));
                    pdfTable.addCell(getPdfPCellInJustified(oleCirculationDesk != null ? oleCirculationDesk.getCirculationDeskPublicName() : ""));

                    pdfTable.addCell(getPdfPCellInJustified("Item Barcode"));
                    pdfTable.addCell(getPdfPCellInLeft(":"));
                    pdfTable.addCell(getPdfPCellInJustified(itemBarcode));

                    pdfTable.addCell(getPdfPCellInJustified("Title"));
                    pdfTable.addCell(getPdfPCellInLeft(":"));
                    pdfTable.addCell(getPdfPCellInJustified(oleLoanDocument.getTitle()));

                    pdfTable.addCell(getPdfPCellInJustified("Call Number"));
                    pdfTable.addCell(getPdfPCellInLeft(":"));
                    pdfTable.addCell(getPdfPCellInJustified(itemCallNumber));

                    pdfTable.addCell(getPdfPCellInJustified("Copy Number"));
                    pdfTable.addCell(getPdfPCellInLeft(":"));
                    pdfTable.addCell(getPdfPCellInJustified(copyNumber));

                    if (oleCirculationDesk != null) {
                        int noDays = Integer.parseInt(oleCirculationDesk.getOnHoldDays());
                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.DATE, noDays);
                        Date date = calendar.getTime();
                        if (date != null) {
                            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                            pdfTable.addCell(getPdfPCellInJustified("Expiration onHoldDate"));
                            pdfTable.addCell(getPdfPCellInLeft(":"));
                            String dateString = date == null ? "" : df.format(date);
                            pdfTable.addCell(getPdfPCellInJustified(dateString));
                        }
                    }
                }
                pdfTable.addCell(getEmptyCell());
                pdfTable.addCell(getEmptyCell());
                pdfTable.addCell(getEmptyCell());
                document.add(pdfTable);
                document.newPage();
            }
        }
        document.close();
        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();
        os.flush();
        os.close();
    }

    private void getItemDetails(LoanProcessor loanProcessor, OleLoanDocument oleLoanDocument, Item oleItem, String itemBarcode) throws Exception {
        org.kuali.ole.docstore.common.document.Item item = new ItemOleml();
        org.kuali.ole.docstore.common.search.SearchParams search_Params = new org.kuali.ole.docstore.common.search.SearchParams();
        SearchResponse searchResponse = null;
        search_Params.getSearchConditions().add(search_Params.buildSearchCondition("phrase", search_Params.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), item.ITEM_BARCODE, itemBarcode), ""));
        search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(DocType.ITEM.getCode(), "id"));
        search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.HOLDINGS.getCode(), "id"));
        search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), "id"));
        search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), Bib.TITLE));
        search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), Bib.AUTHOR));
        searchResponse = getDocstoreClientLocator().getDocstoreClient().search(search_Params);
        for (SearchResult searchResult : searchResponse.getSearchResults()) {
            for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                String fieldName = searchResultField.getFieldName();
                String fieldValue = searchResultField.getFieldValue() != null ? searchResultField.getFieldValue() : "";
                if (fieldName.equalsIgnoreCase("id") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("holdings")) {
                    oleLoanDocument.setInstanceUuid(fieldValue);
                } else if (fieldName.equalsIgnoreCase("id") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("item")) {
                    oleLoanDocument.setItemUuid(fieldValue);
                } else if (fieldName.equalsIgnoreCase(Bib.TITLE) && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("bibliographic")) {
                    oleLoanDocument.setTitle(fieldValue);
                } else if (fieldName.equalsIgnoreCase(Bib.AUTHOR) && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("bibliographic")) {
                    oleLoanDocument.setAuthor(fieldValue);
                }
            }
        }
        String itemXml = null;
        itemXml = loanProcessor.getItemXML(oleLoanDocument.getItemUuid());
        oleItem = loanProcessor.getItemPojo(itemXml);
    }
}


