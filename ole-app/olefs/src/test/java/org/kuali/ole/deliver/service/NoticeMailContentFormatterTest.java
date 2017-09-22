package org.kuali.ole.deliver.service;

import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.batch.OleNoticeBo;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.notice.bo.OleNoticeContentConfigurationBo;
import org.kuali.ole.deliver.notice.bo.OleNoticeFieldLabelMapping;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameBo;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import static junit.framework.TestCase.assertNotNull;

/**
 * Created by pvsubrah on 4/6/15.
 */
public class NoticeMailContentFormatterTest {

    @Mock
    private ParameterValueResolver parameterValueResolver;

    @Mock
    private OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService;

    @Mock
    private BusinessObjectService businessObjectService;

    @Mock
    private OlePatronDocument mockOlePatronDocument;

    @Mock
    private OleLoanDocument mockOleLoanDocument;

    @Mock
    private OleLoanDocument mockOleLoanDocument1;

    @Mock
    private OleLocation mockOleLocation;

    @Mock
    private CircDeskLocationResolver mockCircDeskLocationResolver;

    @Mock
    private OleCirculationDesk mockCirculationDesk;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testGenerateMailContent() throws Exception {
        NoticeMailContentFormatter noticeMailContentFormatter = new MockNoticeMailContentFormatter();

        Mockito.when(parameterValueResolver.getParameter(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()
        )).thenReturn("TITLE");
        Mockito.when(parameterValueResolver.getParameter(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn
                        ("CONTENT");
        noticeMailContentFormatter.setParameterValueResolver(parameterValueResolver);
        noticeMailContentFormatter.setOleDeliverRequestDocumentHelperService(oleDeliverRequestDocumentHelperService);
        noticeMailContentFormatter.setBusinessObjectService(businessObjectService);
        OleLocation location = new OleLocation();
        location.setLocationId("1");
        location.setLocationCode("B-EDU");
        location.setLocationName("B-EDU");
        location.setOleLocation(location);
        Mockito.when(mockCircDeskLocationResolver.getLocationByLocationCode("B-EDU")).thenReturn(location);
        noticeMailContentFormatter.setCircDeskLocationResolver(mockCircDeskLocationResolver);


        OleLoanDocument oleLoanDocument = new OleLoanDocument();

        OlePatronDocument olePatron = new OlePatronDocument();
        olePatron.setBarcode("123125");
        EntityBo entity = new EntityBo();
        ArrayList<EntityNameBo> entityNameBos = new ArrayList<EntityNameBo>();
        EntityNameBo entityNameBo = new EntityNameBo();
        entityNameBo.setFirstName("FirtName");
        entityNameBos.add(entityNameBo);
        entity.setNames(entityNameBos);
        ArrayList<EntityTypeContactInfoBo> entityTypeContactInfos = new ArrayList<EntityTypeContactInfoBo>();
        entityTypeContactInfos.add(new EntityTypeContactInfoBo());
        entity.setEntityTypeContactInfos(entityTypeContactInfos);
        olePatron.setEntity(entity);

        oleLoanDocument.setOlePatron(olePatron);

        HashMap<String, String> fieldLabelMap = new HashMap<>();
        OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo = new OleNoticeContentConfigurationBo();
        oleNoticeContentConfigurationBo.setActive(true);
        oleNoticeContentConfigurationBo.setNoticeName("Notice Name");
        oleNoticeContentConfigurationBo.setNoticeTitle("Notice Title");
        oleNoticeContentConfigurationBo.setNoticeType("Notice Type");
        oleNoticeContentConfigurationBo.setNoticeBody("This is a test notice. Please ingore!");
        String mailContent = noticeMailContentFormatter.generateMailContentForPatron(Collections.singletonList(oleLoanDocument), oleNoticeContentConfigurationBo);
        System.out.println(mailContent);

    }


    @Test
    public void generateNoticeHTML() throws Exception {
        NoticeMailContentFormatter noticeMailContentFormatter =  new OverdueNoticeEmailContentFormatter();
        OleNoticeBo oleNoticeBo = new OleNoticeBo();
        oleNoticeBo.setPatronName("John Doe");
        oleNoticeBo.setPatronAddress("123, High Street, MA - 201231");
        oleNoticeBo.setPatronEmailAddress("j.doe@hotmail.com");
        oleNoticeBo.setPatronPhoneNumber("712-123-2145");

        oleNoticeBo.setTitle("History of Mars");
        oleNoticeBo.setAuthor("Mary Jane");
        oleNoticeBo.setVolumeNumber("v1.0");
        oleNoticeBo.setDueDateString(new Date().toString());
        oleNoticeBo.setItemShelvingLocation("UC/JRL/GEN");
        oleNoticeBo.setItemCallNumber("X-123");
        oleNoticeBo.setItemId("1234");
        oleNoticeBo.setNoticeSpecificContent("This is a test notice. Please ignore!!");
        oleNoticeBo.setNoticeTitle("Overdue Notice");

        OleNoticeBo oleNoticeBo1 = (OleNoticeBo) oleNoticeBo.clone();
        List<OleNoticeBo> oleNoticeBos = new ArrayList<>();
        oleNoticeBos.add(oleNoticeBo);
        oleNoticeBos.add(oleNoticeBo1);

        OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo = new OleNoticeContentConfigurationBo();
        oleNoticeContentConfigurationBo.setActive(true);
        oleNoticeContentConfigurationBo.setNoticeName("Overdue Notice");
        oleNoticeContentConfigurationBo.setNoticeTitle("Overdue Notice");
        oleNoticeContentConfigurationBo.setNoticeType("Overdue Notice");

        ArrayList<OleNoticeFieldLabelMapping> oleNoticeFieldLabelMappings = new ArrayList<>();

        OleNoticeFieldLabelMapping patronName = new OleNoticeFieldLabelMapping();
        patronName.setFieldLabel("Patron Full Name");
        patronName.setFieldName(OLEConstants.PATRON_NAME);


        OleNoticeFieldLabelMapping address = new OleNoticeFieldLabelMapping();
        address.setFieldLabel("Patron Address");
        address.setFieldName(OLEConstants.NOTICE_ADDRESS);


        OleNoticeFieldLabelMapping phoneNumber = new OleNoticeFieldLabelMapping();
        phoneNumber.setFieldLabel("Patron Phone Number");
        phoneNumber.setFieldName(OLEConstants.NOTICE_PHONE_NUMBER);



        OleNoticeFieldLabelMapping patronEmail = new OleNoticeFieldLabelMapping();
        patronEmail.setFieldLabel("Valid Email Id");
        patronEmail.setFieldName(OLEConstants.NOTICE_EMAIL);


        OleNoticeFieldLabelMapping itemCallNum = new OleNoticeFieldLabelMapping();
        itemCallNum.setFieldLabel("Item Call Number");
        itemCallNum.setFieldName(OLEConstants.NOTICE_CALL_NUMBER);

        OleNoticeFieldLabelMapping title = new OleNoticeFieldLabelMapping();
        title.setFieldLabel("Item Title");
        title.setFieldName(OLEConstants.NOTICE_TITLE);

        OleNoticeFieldLabelMapping author = new OleNoticeFieldLabelMapping();
        author.setFieldLabel("Item Author");
        author.setFieldName(OLEConstants.NOTICE_AUTHOR);

        OleNoticeFieldLabelMapping itemBarcode = new OleNoticeFieldLabelMapping();
        itemBarcode.setFieldLabel("Item Barcode");
        itemBarcode.setFieldName(OLEConstants.NOTICE_ITEM_BARCODE);

        OleNoticeFieldLabelMapping itemDue = new OleNoticeFieldLabelMapping();
        itemDue.setFieldLabel("Item Due Date");
        itemDue.setFieldName(OLEConstants.ITEM_WAS_DUE);


        OleNoticeFieldLabelMapping shelvingLocation = new OleNoticeFieldLabelMapping();
        shelvingLocation.setFieldLabel("Shelving Location");
        shelvingLocation.setFieldName(OLEConstants.LIBRARY_SHELVING_LOCATION);

        oleNoticeFieldLabelMappings.add(patronName);
        oleNoticeFieldLabelMappings.add(address);
        oleNoticeFieldLabelMappings.add(phoneNumber);
        oleNoticeFieldLabelMappings.add(patronEmail);
        oleNoticeFieldLabelMappings.add(itemCallNum);
        oleNoticeFieldLabelMappings.add(title);
        oleNoticeFieldLabelMappings.add(author);
        oleNoticeFieldLabelMappings.add(itemBarcode);
        oleNoticeFieldLabelMappings.add(itemDue);
        oleNoticeFieldLabelMappings.add(shelvingLocation);

        oleNoticeContentConfigurationBo.setOleNoticeFieldLabelMappings(oleNoticeFieldLabelMappings);

        String html = noticeMailContentFormatter.generateHTML(oleNoticeBos, oleNoticeContentConfigurationBo);
        assertNotNull(html);
        System.out.println(html);
    }


    @Test
    public void generateOverdueNoticeMailContentForPatron() throws Exception {
        NoticeMailContentFormatter noticeMailContentFormatter = new MockOverdueNoticeEmailContentFormatter();
        OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo = new OleNoticeContentConfigurationBo();
        oleNoticeContentConfigurationBo.setActive(true);
        oleNoticeContentConfigurationBo.setNoticeName("Overdue Notice");
        oleNoticeContentConfigurationBo.setNoticeTitle("Overdue Notice");
        oleNoticeContentConfigurationBo.setNoticeType("Overdue Notice");
        oleNoticeContentConfigurationBo.setNoticeBody("This is a test notice. Please ingore!");
        OleLocation location = new OleLocation();
        location.setLocationId("1");
        location.setLocationCode("B-EDU");
        location.setLocationName("B-EDU");
        location.setOleLocation(location);
        Mockito.when(mockCircDeskLocationResolver.getLocationByLocationCode("B-EDU")).thenReturn(location);
        noticeMailContentFormatter.setCircDeskLocationResolver(mockCircDeskLocationResolver);

        ArrayList<OleNoticeFieldLabelMapping> oleNoticeFieldLabelMappings = new ArrayList<>();

        OleNoticeFieldLabelMapping patronName = new OleNoticeFieldLabelMapping();
        patronName.setFieldLabel("Patron Full Name");
        patronName.setFieldName(OLEConstants.PATRON_NAME);


        OleNoticeFieldLabelMapping address = new OleNoticeFieldLabelMapping();
        address.setFieldLabel("Patron Address");
        address.setFieldName(OLEConstants.NOTICE_ADDRESS);


        OleNoticeFieldLabelMapping phoneNumber = new OleNoticeFieldLabelMapping();
        phoneNumber.setFieldLabel("Patron Phone Number");
        phoneNumber.setFieldName(OLEConstants.NOTICE_PHONE_NUMBER);



        OleNoticeFieldLabelMapping patronEmail = new OleNoticeFieldLabelMapping();
        patronEmail.setFieldLabel("Valid Email Id");
        patronEmail.setFieldName(OLEConstants.NOTICE_EMAIL);


        OleNoticeFieldLabelMapping itemCallNum = new OleNoticeFieldLabelMapping();
        itemCallNum.setFieldLabel("Item Call Number");
        itemCallNum.setFieldName(OLEConstants.NOTICE_CALL_NUMBER);

        OleNoticeFieldLabelMapping title = new OleNoticeFieldLabelMapping();
        title.setFieldLabel("Item Title");
        title.setFieldName(OLEConstants.NOTICE_TITLE);

        OleNoticeFieldLabelMapping author = new OleNoticeFieldLabelMapping();
        author.setFieldLabel("Item Author");
        author.setFieldName(OLEConstants.NOTICE_AUTHOR);

        OleNoticeFieldLabelMapping itemBarcode = new OleNoticeFieldLabelMapping();
        itemBarcode.setFieldLabel("Item Barcode");
        itemBarcode.setFieldName(OLEConstants.NOTICE_ITEM_BARCODE);

        OleNoticeFieldLabelMapping itemDue = new OleNoticeFieldLabelMapping();
        itemDue.setFieldLabel("Item Due Date");
        itemDue.setFieldName(OLEConstants.ITEM_WAS_DUE);


        OleNoticeFieldLabelMapping shelvingLocation = new OleNoticeFieldLabelMapping();
        shelvingLocation.setFieldLabel("Shelving Location");
        shelvingLocation.setFieldName(OLEConstants.LIBRARY_SHELVING_LOCATION);

        oleNoticeFieldLabelMappings.add(patronName);
        oleNoticeFieldLabelMappings.add(address);
        oleNoticeFieldLabelMappings.add(phoneNumber);
        oleNoticeFieldLabelMappings.add(patronEmail);
        oleNoticeFieldLabelMappings.add(itemCallNum);
        oleNoticeFieldLabelMappings.add(title);
        oleNoticeFieldLabelMappings.add(author);
        oleNoticeFieldLabelMappings.add(itemBarcode);
        oleNoticeFieldLabelMappings.add(itemDue);
        oleNoticeFieldLabelMappings.add(shelvingLocation);

        oleNoticeContentConfigurationBo.setOleNoticeFieldLabelMappings(oleNoticeFieldLabelMappings);

        Mockito.when(mockOlePatronDocument.getPatronName()).thenReturn("John Doe");
        Mockito.when(mockOlePatronDocument.getPreferredAddress()).thenReturn("123 High Street");
        Mockito.when(mockOlePatronDocument.getEmailAddress()).thenReturn("jdoe@gmail.com");
        Mockito.when(mockOlePatronDocument.getPhoneNumber()).thenReturn("123-233-2132");

        ArrayList locations = new ArrayList();
        Mockito.when(mockOleLocation.getLocationName()).thenReturn("Regular Stacks");
        locations.add(mockOleLocation);

        ArrayList<OleLoanDocument> oleLoanDocuments = new ArrayList<>();
        Mockito.when(mockOleLoanDocument.getOlePatron()).thenReturn(mockOlePatronDocument);
        Mockito.when(mockOleLoanDocument.getTitle()).thenReturn("History of Sceience");
        Mockito.when(mockOleLoanDocument.getAuthor()).thenReturn("Mock Author");
        Mockito.when(mockOleLoanDocument.getEnumeration()).thenReturn("v1ase.123");
        Mockito.when(mockOleLoanDocument.getChronology()).thenReturn("chro123.12");
        Mockito.when(mockOleLoanDocument.getItemVolumeNumber()).thenReturn("v.12");
        Mockito.when(mockOleLoanDocument.getItemCallNumber()).thenReturn("123123");
        Mockito.when(mockOleLoanDocument.getItemCopyNumber()).thenReturn("C0123.12");
        Mockito.when(mockOleLoanDocument.getLoanDueDate()).thenReturn(new Timestamp(System.currentTimeMillis()));
        oleLoanDocuments.add(mockOleLoanDocument);


        Mockito.when(mockOleLoanDocument1.getOlePatron()).thenReturn(mockOlePatronDocument);
        Mockito.when(mockOleLoanDocument1.getTitle()).thenReturn("History of War");
        Mockito.when(mockOleLoanDocument1.getAuthor()).thenReturn("Mock Author1");
        Mockito.when(mockOleLoanDocument1.getEnumeration()).thenReturn("v1ase.1231");
        Mockito.when(mockOleLoanDocument1.getChronology()).thenReturn("chro123.12123");
        Mockito.when(mockOleLoanDocument1.getItemVolumeNumber()).thenReturn("v.12123");
        Mockito.when(mockOleLoanDocument1.getItemCallNumber()).thenReturn("12");
        Mockito.when(mockOleLoanDocument1.getItemCopyNumber()).thenReturn("C0123.12");
        Mockito.when(mockOleLoanDocument1.getLoanDueDate()).thenReturn(new Timestamp(System.currentTimeMillis()));
        oleLoanDocuments.add(mockOleLoanDocument1);


        String html = noticeMailContentFormatter.generateMailContentForPatron(oleLoanDocuments, oleNoticeContentConfigurationBo);
        assertNotNull(html);
        System.out.println(html);
    }

    @Test
    public void generateLostNoticeMailContentForPatron() throws Exception {
        NoticeMailContentFormatter noticeMailContentFormatter = new MockLostNoticeEmailContentFormatter();
        OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo = new OleNoticeContentConfigurationBo();
        oleNoticeContentConfigurationBo.setActive(true);
        oleNoticeContentConfigurationBo.setNoticeName("Overdue Notice");
        oleNoticeContentConfigurationBo.setNoticeTitle("Overdue Notice");
        oleNoticeContentConfigurationBo.setNoticeType("Overdue Notice");
        oleNoticeContentConfigurationBo.setNoticeBody("This is a test notice. Please ingore!");
        OleLocation location = new OleLocation();
        location.setLocationId("1");
        location.setLocationCode("B-EDU");
        location.setLocationName("B-EDU");
        location.setOleLocation(location);
        Mockito.when(mockCircDeskLocationResolver.getLocationByLocationCode("B-EDU")).thenReturn(location);
        noticeMailContentFormatter.setCircDeskLocationResolver(mockCircDeskLocationResolver);

        ArrayList<OleNoticeFieldLabelMapping> oleNoticeFieldLabelMappings = new ArrayList<>();

        OleNoticeFieldLabelMapping circulationDeskLocation = new OleNoticeFieldLabelMapping();
        circulationDeskLocation.setFieldLabel("Circulation Location/Library Name");
        circulationDeskLocation.setFieldName(OLEConstants.CIRCULATION_LOCATION_LIBRARY_NAME);

        OleNoticeFieldLabelMapping patronName = new OleNoticeFieldLabelMapping();
        patronName.setFieldLabel("Patron Full Name");
        patronName.setFieldName(OLEConstants.PATRON_NAME);


        OleNoticeFieldLabelMapping address = new OleNoticeFieldLabelMapping();
        address.setFieldLabel("Patron Address");
        address.setFieldName(OLEConstants.NOTICE_ADDRESS);


        OleNoticeFieldLabelMapping phoneNumber = new OleNoticeFieldLabelMapping();
        phoneNumber.setFieldLabel("Patron Phone Number");
        phoneNumber.setFieldName(OLEConstants.NOTICE_PHONE_NUMBER);



        OleNoticeFieldLabelMapping patronEmail = new OleNoticeFieldLabelMapping();
        patronEmail.setFieldLabel("Valid Email Id");
        patronEmail.setFieldName(OLEConstants.NOTICE_EMAIL);


        OleNoticeFieldLabelMapping itemCallNum = new OleNoticeFieldLabelMapping();
        itemCallNum.setFieldLabel("Item Call Number");
        itemCallNum.setFieldName(OLEConstants.NOTICE_CALL_NUMBER);

        OleNoticeFieldLabelMapping title = new OleNoticeFieldLabelMapping();
        title.setFieldLabel("Item Title");
        title.setFieldName(OLEConstants.NOTICE_TITLE);

        OleNoticeFieldLabelMapping author = new OleNoticeFieldLabelMapping();
        author.setFieldLabel("Item Author");
        author.setFieldName(OLEConstants.NOTICE_AUTHOR);

        OleNoticeFieldLabelMapping itemBarcode = new OleNoticeFieldLabelMapping();
        itemBarcode.setFieldLabel("Item Barcode");
        itemBarcode.setFieldName(OLEConstants.NOTICE_ITEM_BARCODE);

        OleNoticeFieldLabelMapping itemDue = new OleNoticeFieldLabelMapping();
        itemDue.setFieldLabel("Item Due Date");
        itemDue.setFieldName(OLEConstants.ITEM_WAS_DUE);

        OleNoticeFieldLabelMapping billNumber = new OleNoticeFieldLabelMapping();
        billNumber.setFieldLabel("Bill Number");
        billNumber.setFieldName("Bill Number");

        OleNoticeFieldLabelMapping feeType = new OleNoticeFieldLabelMapping();
        feeType.setFieldLabel("Fee Type");
        feeType.setFieldName("Fee Type");

        OleNoticeFieldLabelMapping feeAmount = new OleNoticeFieldLabelMapping();
        feeAmount.setFieldLabel("Fee Amount");
        feeAmount.setFieldName("Fee Amount");


        OleNoticeFieldLabelMapping shelvingLocation = new OleNoticeFieldLabelMapping();
        shelvingLocation.setFieldLabel("Shelving Location");
        shelvingLocation.setFieldName(OLEConstants.LIBRARY_SHELVING_LOCATION);

        oleNoticeFieldLabelMappings.add(circulationDeskLocation);
        oleNoticeFieldLabelMappings.add(patronName);
        oleNoticeFieldLabelMappings.add(address);
        oleNoticeFieldLabelMappings.add(phoneNumber);
        oleNoticeFieldLabelMappings.add(patronEmail);
        oleNoticeFieldLabelMappings.add(itemCallNum);
        oleNoticeFieldLabelMappings.add(title);
        oleNoticeFieldLabelMappings.add(author);
        oleNoticeFieldLabelMappings.add(itemBarcode);
        oleNoticeFieldLabelMappings.add(itemDue);
        oleNoticeFieldLabelMappings.add(shelvingLocation);
        oleNoticeFieldLabelMappings.add(billNumber);
        oleNoticeFieldLabelMappings.add(feeType);
        oleNoticeFieldLabelMappings.add(feeAmount);

        oleNoticeContentConfigurationBo.setOleNoticeFieldLabelMappings(oleNoticeFieldLabelMappings);

        Mockito.when(mockOlePatronDocument.getPatronName()).thenReturn("John Doe");
        Mockito.when(mockOlePatronDocument.getPreferredAddress()).thenReturn("123 High Street");
        Mockito.when(mockOlePatronDocument.getEmailAddress()).thenReturn("jdoe@gmail.com");
        Mockito.when(mockOlePatronDocument.getPhoneNumber()).thenReturn("123-233-2132");

        ArrayList locations = new ArrayList();
        Mockito.when(mockOleLocation.getLocationName()).thenReturn("Regular Stacks");
        locations.add(mockOleLocation);

        ArrayList<OleLoanDocument> oleLoanDocuments = new ArrayList<>();
        Mockito.when(mockOleLoanDocument.getOlePatron()).thenReturn(mockOlePatronDocument);
        Mockito.when(mockOleLoanDocument.getTitle()).thenReturn("History of Sceience");
        Mockito.when(mockOleLoanDocument.getAuthor()).thenReturn("Mock Author");
        Mockito.when(mockOleLoanDocument.getEnumeration()).thenReturn("v1ase.123");
        Mockito.when(mockOleLoanDocument.getChronology()).thenReturn("chro123.12");
        Mockito.when(mockOleLoanDocument.getItemVolumeNumber()).thenReturn("v.12");
        Mockito.when(mockOleLoanDocument.getItemCallNumber()).thenReturn("123123");
        Mockito.when(mockOleLoanDocument.getItemCopyNumber()).thenReturn("C0123.12");
        Mockito.when(mockOleLoanDocument.getLoanDueDate()).thenReturn(new Timestamp(System.currentTimeMillis()));
        Mockito.when(mockOleLoanDocument.getReplacementBill()).thenReturn(new BigDecimal(21.00));
        Mockito.when(mockOleLoanDocument.getRepaymentFeePatronBillId()).thenReturn("123");
        oleLoanDocuments.add(mockOleLoanDocument);


        Mockito.when(mockOleLoanDocument1.getOlePatron()).thenReturn(mockOlePatronDocument);
        Mockito.when(mockOleLoanDocument1.getTitle()).thenReturn("History of War");
        Mockito.when(mockOleLoanDocument1.getAuthor()).thenReturn("Mock Author1");
        Mockito.when(mockOleLoanDocument1.getEnumeration()).thenReturn("v1ase.1231");
        Mockito.when(mockOleLoanDocument1.getChronology()).thenReturn("chro123.12123");
        Mockito.when(mockOleLoanDocument1.getItemVolumeNumber()).thenReturn("v.12123");
        Mockito.when(mockOleLoanDocument1.getItemCallNumber()).thenReturn("12");
        Mockito.when(mockOleLoanDocument1.getItemCopyNumber()).thenReturn("C0123.12");
        Mockito.when(mockOleLoanDocument1.getLoanDueDate()).thenReturn(new Timestamp(System.currentTimeMillis()));
        Mockito.when(mockOleLoanDocument1.getLoanDueDate()).thenReturn(new Timestamp(System.currentTimeMillis()));
        Mockito.when(mockOleLoanDocument1.getReplacementBill()).thenReturn(new BigDecimal(21.00));
        Mockito.when(mockOleLoanDocument1.getRepaymentFeePatronBillId()).thenReturn("123");
        oleLoanDocuments.add(mockOleLoanDocument1);


        String html = noticeMailContentFormatter.generateMailContentForPatron(oleLoanDocuments, oleNoticeContentConfigurationBo);
        assertNotNull(html);
        System.out.println(html);
    }


    @Test
    public void generateMissingPieceNoticeMailContentForPatron() throws Exception {
        NoticeMailContentFormatter noticeMailContentFormatter = new MockMissingPieceNoticeEmailContentFormatter();
        OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo = new OleNoticeContentConfigurationBo();
        oleNoticeContentConfigurationBo.setActive(true);
        oleNoticeContentConfigurationBo.setNoticeName(OLEConstants.MISSING_PIECE_NOTICE_TITLE);
        oleNoticeContentConfigurationBo.setNoticeTitle(OLEConstants.MISSING_PIECE_NOTICE_TITLE);
        oleNoticeContentConfigurationBo.setNoticeType(OLEConstants.MISSING_PIECE_NOTICE);
        oleNoticeContentConfigurationBo.setNoticeBody("This is a test notice. Please ignore!");

        ArrayList<OleNoticeFieldLabelMapping> oleNoticeFieldLabelMappings = new ArrayList<>();

        OleNoticeFieldLabelMapping patronName = new OleNoticeFieldLabelMapping();
        patronName.setFieldLabel("Patron Full Name");
        patronName.setFieldName(OLEConstants.PATRON_NAME);


        OleNoticeFieldLabelMapping address = new OleNoticeFieldLabelMapping();
        address.setFieldLabel("Patron Address");
        address.setFieldName(OLEConstants.NOTICE_ADDRESS);


        OleNoticeFieldLabelMapping phoneNumber = new OleNoticeFieldLabelMapping();
        phoneNumber.setFieldLabel("Patron Phone Number");
        phoneNumber.setFieldName(OLEConstants.NOTICE_PHONE_NUMBER);



        OleNoticeFieldLabelMapping patronEmail = new OleNoticeFieldLabelMapping();
        patronEmail.setFieldLabel("Valid Email Id");
        patronEmail.setFieldName(OLEConstants.NOTICE_EMAIL);


        OleNoticeFieldLabelMapping itemCallNum = new OleNoticeFieldLabelMapping();
        itemCallNum.setFieldLabel("Item Call Number");
        itemCallNum.setFieldName(OLEConstants.NOTICE_CALL_NUMBER);

        OleNoticeFieldLabelMapping title = new OleNoticeFieldLabelMapping();
        title.setFieldLabel("Item Title");
        title.setFieldName(OLEConstants.NOTICE_TITLE);

        OleNoticeFieldLabelMapping author = new OleNoticeFieldLabelMapping();
        author.setFieldLabel("Item Author");
        author.setFieldName(OLEConstants.NOTICE_AUTHOR);

        OleNoticeFieldLabelMapping itemBarcode = new OleNoticeFieldLabelMapping();
        itemBarcode.setFieldLabel("Item Barcode");
        itemBarcode.setFieldName(OLEConstants.NOTICE_ITEM_BARCODE);

        OleNoticeFieldLabelMapping itemDue = new OleNoticeFieldLabelMapping();
        itemDue.setFieldLabel("Item Due Date");
        itemDue.setFieldName(OLEConstants.ITEM_WAS_DUE);


        OleNoticeFieldLabelMapping shelvingLocation = new OleNoticeFieldLabelMapping();
        shelvingLocation.setFieldLabel("Shelving Location");
        shelvingLocation.setFieldName(OLEConstants.LIBRARY_SHELVING_LOCATION);

        OleNoticeFieldLabelMapping checkInDate = new OleNoticeFieldLabelMapping();
        checkInDate.setFieldLabel("Check In Date");
        checkInDate.setFieldName(OLEConstants.MISSING_ITEM_CHECK_IN_DATE);

        OleNoticeFieldLabelMapping missingPieceNote = new OleNoticeFieldLabelMapping();
        missingPieceNote.setFieldLabel("Missing Piece Note");
        missingPieceNote.setFieldName(OLEConstants.MISSING_ITEM_NOTE);

        oleNoticeFieldLabelMappings.add(patronName);
        oleNoticeFieldLabelMappings.add(address);
        oleNoticeFieldLabelMappings.add(phoneNumber);
        oleNoticeFieldLabelMappings.add(patronEmail);
        oleNoticeFieldLabelMappings.add(itemCallNum);
        oleNoticeFieldLabelMappings.add(title);
        oleNoticeFieldLabelMappings.add(author);
        oleNoticeFieldLabelMappings.add(itemBarcode);
        oleNoticeFieldLabelMappings.add(itemDue);
        oleNoticeFieldLabelMappings.add(shelvingLocation);
        oleNoticeFieldLabelMappings.add(checkInDate);
        oleNoticeFieldLabelMappings.add(missingPieceNote);

        OleLocation location = new OleLocation();
        location.setLocationId("1");
        location.setLocationCode("B-EDU");
        location.setLocationName("B-EDU");
        location.setOleLocation(location);

        OleNoticeBo oleNoticeBo = new OleNoticeBo();
        oleNoticeBo.setItemInstitution("Chicago");
        oleNoticeBo.setItemCampus("");
        oleNoticeBo.setItemCollection("");
        oleNoticeBo.setItemLibrary("");
        oleNoticeBo.setItemLocation("BEDUC");
        oleNoticeBo.setItemShelvingLocation("BEDSTACK");
        Mockito.when(mockCircDeskLocationResolver.getLocationByLocationCode("B-EDU")).thenReturn(location);

        noticeMailContentFormatter.setCircDeskLocationResolver(mockCircDeskLocationResolver);

        oleNoticeContentConfigurationBo.setOleNoticeFieldLabelMappings(oleNoticeFieldLabelMappings);

        Mockito.when(mockOlePatronDocument.getPatronName()).thenReturn("John Doe");
        Mockito.when(mockOlePatronDocument.getPreferredAddress()).thenReturn("123 High Street");
        Mockito.when(mockOlePatronDocument.getEmailAddress()).thenReturn("jdoe@gmail.com");
        Mockito.when(mockOlePatronDocument.getPhoneNumber()).thenReturn("123-233-2132");

        ArrayList locations = new ArrayList();
        Mockito.when(mockOleLocation.getLocationName()).thenReturn("Regular Stacks");
        locations.add(mockOleLocation);

        ArrayList<OleLoanDocument> oleLoanDocuments = new ArrayList<>();
        Mockito.when(mockOleLoanDocument.getOlePatron()).thenReturn(mockOlePatronDocument);
        Mockito.when(mockOleLoanDocument.getTitle()).thenReturn("History of Sceience");
        Mockito.when(mockOleLoanDocument.getAuthor()).thenReturn("Mock Author");
        Mockito.when(mockOleLoanDocument.getEnumeration()).thenReturn("v1ase.123");
        Mockito.when(mockOleLoanDocument.getChronology()).thenReturn("chro123.12");
        Mockito.when(mockOleLoanDocument.getItemVolumeNumber()).thenReturn("v.12");
        Mockito.when(mockOleLoanDocument.getItemCallNumber()).thenReturn("123123");
        Mockito.when(mockOleLoanDocument.getItemCopyNumber()).thenReturn("C0123.12");
        Mockito.when(mockOleLoanDocument.getLoanDueDate()).thenReturn(new Timestamp(System.currentTimeMillis()));
        Mockito.when(mockOleLoanDocument.getItemFullLocation()).thenReturn("B-EDU/BEDSTACKS");
        Mockito.when(mockOleLoanDocument.getCheckInDate()).thenReturn(new Timestamp(new Date("05/11/2015").getTime()));
        Mockito.when(mockOleLoanDocument.getMissingPieceNote()).thenReturn("Missing Piece Note Description");
        Mockito.when(mockOleLoanDocument.getOleCirculationDesk()).thenReturn(mockCirculationDesk);
        oleLoanDocuments.add(mockOleLoanDocument);

        Mockito.when(mockOleLoanDocument1.getOlePatron()).thenReturn(mockOlePatronDocument);
        Mockito.when(mockOleLoanDocument1.getTitle()).thenReturn("History of War");
        Mockito.when(mockOleLoanDocument1.getAuthor()).thenReturn("Mock Author1");
        Mockito.when(mockOleLoanDocument1.getEnumeration()).thenReturn("v1ase.1231");
        Mockito.when(mockOleLoanDocument1.getChronology()).thenReturn("chro123.12123");
        Mockito.when(mockOleLoanDocument1.getItemVolumeNumber()).thenReturn("v.12123");
        Mockito.when(mockOleLoanDocument1.getItemCallNumber()).thenReturn("12");
        Mockito.when(mockOleLoanDocument1.getItemCopyNumber()).thenReturn("C0123.12");
        Mockito.when(mockOleLoanDocument1.getLoanDueDate()).thenReturn(new Timestamp(System.currentTimeMillis()));
        Mockito.when(mockOleLoanDocument1.getItemFullLocation()).thenReturn("B-EDU/BEDSTACKS");
        Mockito.when(mockOleLoanDocument1.getCheckInDate()).thenReturn(new Timestamp(new Date("05/11/2015").getTime()));
        Mockito.when(mockOleLoanDocument1.getMissingPieceNote()).thenReturn("Missing Piece Note Description");
        Mockito.when(mockOleLoanDocument1.getOleCirculationDesk()).thenReturn(mockCirculationDesk);
        Mockito.when(mockCirculationDesk.getCirculationDeskPublicName()).thenReturn("UC Regenstein Library,1st Floor");
        oleLoanDocuments.add(mockOleLoanDocument1);

        String html = noticeMailContentFormatter.generateMailContentForPatron(oleLoanDocuments, oleNoticeContentConfigurationBo);
        assertNotNull(html);
        System.out.println(html);
    }



    public class MockNoticeMailContentFormatter extends NoticeMailContentFormatter {
        @Override
        protected SimpleDateFormat getSimpleDateFormat() {
            return  new SimpleDateFormat();
        }

        @Override
        protected void processCustomNoticeInfo(OleLoanDocument oleLoanDocument, OleNoticeBo oleNoticeBo){
        }
    }


    class MockOverdueNoticeEmailContentFormatter extends NoticeMailContentFormatter {
        @Override
        protected void processCustomNoticeInfo(OleLoanDocument oleLoanDocument, OleNoticeBo oleNoticeBo) {

        }

        @Override
        protected String getLocationName(String code) {
            return "Stacks Regular";
        }
    }

    class MockLostNoticeEmailContentFormatter extends NoticeMailContentFormatter {
        @Override
        protected void processCustomNoticeInfo(OleLoanDocument oleLoanDocument, OleNoticeBo oleNoticeBo) {
            oleNoticeBo.setNoticeTitle("Lost");
            oleNoticeBo.setBillNumber(oleLoanDocument.getRepaymentFeePatronBillId());
            oleNoticeBo.setFeeType(OLEConstants.REPLACEMENT_FEE);
            if(oleLoanDocument.getReplacementBill() != null) {
                BigDecimal feeamount = oleLoanDocument.getReplacementBill();
                feeamount = feeamount.setScale(2, BigDecimal.ROUND_HALF_UP);
                oleNoticeBo.setFeeAmount("$" + feeamount.toString());
            }
        }

        @Override
        protected String getLocationName(String code) {
            return "Stacks Regular";
        }
    }

    class MockMissingPieceNoticeEmailContentFormatter extends MissingPieceEmailContentFormatter {

        @Override
        protected String getLocationName(String code) {
            return "Stacks Regular";
        }


    }




}