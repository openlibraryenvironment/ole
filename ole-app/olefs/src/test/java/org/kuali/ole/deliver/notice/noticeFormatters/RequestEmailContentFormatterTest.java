package org.kuali.ole.deliver.notice.noticeFormatters;

import org.junit.Test;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.batch.OleNoticeBo;
import org.kuali.ole.deliver.notice.bo.OleNoticeContentConfigurationBo;
import org.kuali.ole.deliver.notice.bo.OleNoticeFieldLabelMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.*;

/**
 * Created by sheiksalahudeenm on 10/14/15.
 */
public class RequestEmailContentFormatterTest {

    @Test
    public void testGenerateMailContentForOnHoldNotice() throws Exception {
        RequestEmailContentFormatter requestEmailContentFormatter =  new OnHoldRequestEmailContentFormatter();
        OleNoticeBo oleNoticeBo = new OleNoticeBo();
        oleNoticeBo.setPatronName("John Doe");
        oleNoticeBo.setPatronAddress("123, High Street, MA - 201231");
        oleNoticeBo.setPatronEmailAddress("j.doe@hotmail.com");
        oleNoticeBo.setPatronPhoneNumber("712-123-2145");

        oleNoticeBo.setTitle("History of Mars");
        oleNoticeBo.setAuthor("Mary Jane");
        oleNoticeBo.setVolumeNumber("v1.0");
        oleNoticeBo.setCopyNumber("C1.0");
        oleNoticeBo.setEnumeration("E1");
        oleNoticeBo.setChronology("CH1");
        oleNoticeBo.setNoticeSpecificFooterContent("This is the test footer content for notice");
        oleNoticeBo.setDueDateString(new Date().toString());
        oleNoticeBo.setItemShelvingLocation("UC/JRL/GEN");
        oleNoticeBo.setItemCallNumber("X-123");
        oleNoticeBo.setItemId("1234");
        oleNoticeBo.setNoticeSpecificContent("This is a test notice. Please ignore!!");
        oleNoticeBo.setNoticeTitle("OnHold Notice");

        oleNoticeBo.setCirculationDeskName("B_EDUC");
        oleNoticeBo.setCirculationDeskReplyToEmail("dev.ole@kuali.org");
        oleNoticeBo.setExpiredOnHoldDate(new Date());
        oleNoticeBo.setOriginalDueDate(new Date());
        oleNoticeBo.setNewDueDate(new Date());
        oleNoticeBo.setNoticeType(OLEConstants.ONHOLD_NOTICE);

        OleNoticeBo oleNoticeBo1 = (OleNoticeBo) oleNoticeBo.clone();
        List<OleNoticeBo> oleNoticeBos = new ArrayList<>();
        oleNoticeBos.add(oleNoticeBo);
        oleNoticeBos.add(oleNoticeBo1);

        OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo = new OleNoticeContentConfigurationBo();
        oleNoticeContentConfigurationBo.setActive(true);
        oleNoticeContentConfigurationBo.setNoticeName(OLEConstants.ONHOLD_NOTICE);
        oleNoticeContentConfigurationBo.setNoticeTitle(OLEConstants.ONHOLD_NOTICE);
        oleNoticeContentConfigurationBo.setNoticeType(OLEConstants.ONHOLD_NOTICE);

        ArrayList<OleNoticeFieldLabelMapping> oleNoticeFieldLabelMappings = new ArrayList<>();

        OleNoticeFieldLabelMapping patronName = new OleNoticeFieldLabelMapping();
        patronName.setFieldLabel("Patron Full Name");
        patronName.setFieldName(OLEConstants.PATRON_NAME);


        OleNoticeFieldLabelMapping address = new OleNoticeFieldLabelMapping();
        address.setFieldLabel("Patron Address");
        address.setFieldName(OLEConstants.NOTICE_ADDRESS);


        OleNoticeFieldLabelMapping phoneNumber = new OleNoticeFieldLabelMapping();
        phoneNumber.setFieldLabel("Patron Phone Number:");
        phoneNumber.setFieldName(OLEConstants.NOTICE_PHONE_NUMBER);



        OleNoticeFieldLabelMapping patronEmail = new OleNoticeFieldLabelMapping();
        patronEmail.setFieldLabel("Valid Email Id:");
        patronEmail.setFieldName(OLEConstants.NOTICE_EMAIL);


        OleNoticeFieldLabelMapping itemCallNum = new OleNoticeFieldLabelMapping();
        itemCallNum.setFieldLabel("Item Call Number:");
        itemCallNum.setFieldName(OLEConstants.NOTICE_CALL_NUMBER);

        OleNoticeFieldLabelMapping title = new OleNoticeFieldLabelMapping();
        title.setFieldLabel("Item Title:");
        title.setFieldName(OLEConstants.NOTICE_TITLE);

        OleNoticeFieldLabelMapping author = new OleNoticeFieldLabelMapping();
        author.setFieldLabel("Item Author:");
        author.setFieldName(OLEConstants.NOTICE_AUTHOR);

        OleNoticeFieldLabelMapping itemBarcode = new OleNoticeFieldLabelMapping();
        itemBarcode.setFieldLabel("Item Barcode:");
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

        String html = requestEmailContentFormatter.generateHTML(oleNoticeBos, oleNoticeContentConfigurationBo);
        assertNotNull(html);
        System.out.println(html);
    }

    @Test
    public void testGenerateMailContentForRecallNotice() throws Exception {
        RequestEmailContentFormatter requestEmailContentFormatter =  new RecallRequestEmailContentFormatter();
        OleNoticeBo oleNoticeBo = new OleNoticeBo();
        oleNoticeBo.setPatronName("John Doe");
        oleNoticeBo.setPatronAddress("123, High Street, MA - 201231");
        oleNoticeBo.setPatronEmailAddress("j.doe@hotmail.com");
        oleNoticeBo.setPatronPhoneNumber("712-123-2145");

        oleNoticeBo.setTitle("History of Mars");
        oleNoticeBo.setAuthor("Mary Jane");
        oleNoticeBo.setVolumeNumber("v1.0");
        oleNoticeBo.setCopyNumber("C1.0");
        oleNoticeBo.setEnumeration("E1");
        oleNoticeBo.setChronology("CH1");
        oleNoticeBo.setNoticeSpecificFooterContent("This is the test footer content for notice");
        oleNoticeBo.setDueDateString(new Date().toString());
        oleNoticeBo.setItemShelvingLocation("UC/JRL/GEN");
        oleNoticeBo.setItemCallNumber("X-123");
        oleNoticeBo.setItemId("1234");
        oleNoticeBo.setNoticeSpecificContent("This is a test notice. Please ignore!!");
        oleNoticeBo.setNoticeTitle("Recall Notice");

        oleNoticeBo.setCirculationDeskName("B_EDUC");
        oleNoticeBo.setCirculationDeskReplyToEmail("dev.ole@kuali.org");
        oleNoticeBo.setExpiredOnHoldDate(new Date());
        oleNoticeBo.setOriginalDueDate(new Date());
        oleNoticeBo.setNewDueDate(new Date());
        oleNoticeBo.setNoticeType(OLEConstants.RECALL_NOTICE);

        OleNoticeBo oleNoticeBo1 = (OleNoticeBo) oleNoticeBo.clone();
        List<OleNoticeBo> oleNoticeBos = new ArrayList<>();
        oleNoticeBos.add(oleNoticeBo);
        oleNoticeBos.add(oleNoticeBo1);

        OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo = new OleNoticeContentConfigurationBo();
        oleNoticeContentConfigurationBo.setActive(true);
        oleNoticeContentConfigurationBo.setNoticeName(OLEConstants.RECALL_NOTICE);
        oleNoticeContentConfigurationBo.setNoticeTitle(OLEConstants.RECALL_NOTICE);
        oleNoticeContentConfigurationBo.setNoticeType(OLEConstants.RECALL_NOTICE);

        ArrayList<OleNoticeFieldLabelMapping> oleNoticeFieldLabelMappings = new ArrayList<>();

        OleNoticeFieldLabelMapping patronName = new OleNoticeFieldLabelMapping();
        patronName.setFieldLabel("Patron Full Name");
        patronName.setFieldName(OLEConstants.PATRON_NAME);


        OleNoticeFieldLabelMapping address = new OleNoticeFieldLabelMapping();
        address.setFieldLabel("Patron Address");
        address.setFieldName(OLEConstants.NOTICE_ADDRESS);


        OleNoticeFieldLabelMapping phoneNumber = new OleNoticeFieldLabelMapping();
        phoneNumber.setFieldLabel("Patron Phone Number:");
        phoneNumber.setFieldName(OLEConstants.NOTICE_PHONE_NUMBER);



        OleNoticeFieldLabelMapping patronEmail = new OleNoticeFieldLabelMapping();
        patronEmail.setFieldLabel("Valid Email Id:");
        patronEmail.setFieldName(OLEConstants.NOTICE_EMAIL);


        OleNoticeFieldLabelMapping itemCallNum = new OleNoticeFieldLabelMapping();
        itemCallNum.setFieldLabel("Item Call Number:");
        itemCallNum.setFieldName(OLEConstants.NOTICE_CALL_NUMBER);

        OleNoticeFieldLabelMapping title = new OleNoticeFieldLabelMapping();
        title.setFieldLabel("Item Title:");
        title.setFieldName(OLEConstants.NOTICE_TITLE);

        OleNoticeFieldLabelMapping author = new OleNoticeFieldLabelMapping();
        author.setFieldLabel("Item Author:");
        author.setFieldName(OLEConstants.NOTICE_AUTHOR);

        OleNoticeFieldLabelMapping itemBarcode = new OleNoticeFieldLabelMapping();
        itemBarcode.setFieldLabel("Item Barcode:");
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

        String html = requestEmailContentFormatter.generateHTML(oleNoticeBos, oleNoticeContentConfigurationBo);
        assertNotNull(html);
        System.out.println(html);
    }

    @Test
    public void testGenerateMailContentForRequestExpirationNotice() throws Exception {
        RequestEmailContentFormatter requestEmailContentFormatter =  new RequestExpirationEmailContentFormatter();
        OleNoticeBo oleNoticeBo = new OleNoticeBo();
        oleNoticeBo.setPatronName("John Doe");
        oleNoticeBo.setPatronAddress("123, High Street, MA - 201231");
        oleNoticeBo.setPatronEmailAddress("j.doe@hotmail.com");
        oleNoticeBo.setPatronPhoneNumber("712-123-2145");

        oleNoticeBo.setTitle("History of Mars");
        oleNoticeBo.setAuthor("Mary Jane");
        oleNoticeBo.setVolumeNumber("v1.0");
        oleNoticeBo.setCopyNumber("C1.0");
        oleNoticeBo.setEnumeration("E1");
        oleNoticeBo.setChronology("CH1");
        oleNoticeBo.setNoticeSpecificFooterContent("This is the test footer content for notice");
        oleNoticeBo.setDueDateString(new Date().toString());
        oleNoticeBo.setItemShelvingLocation("UC/JRL/GEN");
        oleNoticeBo.setItemCallNumber("X-123");
        oleNoticeBo.setItemId("1234");
        oleNoticeBo.setNoticeSpecificContent("This is a test notice. Please ignore!!");
        oleNoticeBo.setNoticeTitle("Request Expiration Notice");

        oleNoticeBo.setCirculationDeskName("B_EDUC");
        oleNoticeBo.setCirculationDeskReplyToEmail("dev.ole@kuali.org");
        oleNoticeBo.setExpiredOnHoldDate(new Date());
        oleNoticeBo.setOriginalDueDate(new Date());
        oleNoticeBo.setNewDueDate(new Date());
        oleNoticeBo.setNoticeType(OLEConstants.REQUEST_EXPIRATION_NOTICE);

        OleNoticeBo oleNoticeBo1 = (OleNoticeBo) oleNoticeBo.clone();
        List<OleNoticeBo> oleNoticeBos = new ArrayList<>();
        oleNoticeBos.add(oleNoticeBo);
        oleNoticeBos.add(oleNoticeBo1);

        OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo = new OleNoticeContentConfigurationBo();
        oleNoticeContentConfigurationBo.setActive(true);
        oleNoticeContentConfigurationBo.setNoticeName(OLEConstants.REQUEST_EXPIRATION_NOTICE);
        oleNoticeContentConfigurationBo.setNoticeTitle(OLEConstants.REQUEST_EXPIRATION_NOTICE);
        oleNoticeContentConfigurationBo.setNoticeType(OLEConstants.REQUEST_EXPIRATION_NOTICE);

        ArrayList<OleNoticeFieldLabelMapping> oleNoticeFieldLabelMappings = new ArrayList<>();

        OleNoticeFieldLabelMapping patronName = new OleNoticeFieldLabelMapping();
        patronName.setFieldLabel("Patron Full Name");
        patronName.setFieldName(OLEConstants.PATRON_NAME);


        OleNoticeFieldLabelMapping address = new OleNoticeFieldLabelMapping();
        address.setFieldLabel("Patron Address");
        address.setFieldName(OLEConstants.NOTICE_ADDRESS);


        OleNoticeFieldLabelMapping phoneNumber = new OleNoticeFieldLabelMapping();
        phoneNumber.setFieldLabel("Patron Phone Number:");
        phoneNumber.setFieldName(OLEConstants.NOTICE_PHONE_NUMBER);



        OleNoticeFieldLabelMapping patronEmail = new OleNoticeFieldLabelMapping();
        patronEmail.setFieldLabel("Valid Email Id:");
        patronEmail.setFieldName(OLEConstants.NOTICE_EMAIL);


        OleNoticeFieldLabelMapping itemCallNum = new OleNoticeFieldLabelMapping();
        itemCallNum.setFieldLabel("Item Call Number:");
        itemCallNum.setFieldName(OLEConstants.NOTICE_CALL_NUMBER);

        OleNoticeFieldLabelMapping title = new OleNoticeFieldLabelMapping();
        title.setFieldLabel("Item Title:");
        title.setFieldName(OLEConstants.NOTICE_TITLE);

        OleNoticeFieldLabelMapping author = new OleNoticeFieldLabelMapping();
        author.setFieldLabel("Item Author:");
        author.setFieldName(OLEConstants.NOTICE_AUTHOR);

        OleNoticeFieldLabelMapping itemBarcode = new OleNoticeFieldLabelMapping();
        itemBarcode.setFieldLabel("Item Barcode:");
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

        String html = requestEmailContentFormatter.generateHTML(oleNoticeBos, oleNoticeContentConfigurationBo);
        assertNotNull(html);
        System.out.println(html);
    }
}