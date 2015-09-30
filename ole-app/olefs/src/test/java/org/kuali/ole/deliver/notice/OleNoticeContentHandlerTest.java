package org.kuali.ole.deliver.notice;

import org.junit.Test;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.batch.OleNoticeBo;
import org.kuali.ole.deliver.notice.bo.OleNoticeContentConfigurationBo;
import org.kuali.ole.deliver.notice.bo.OleNoticeFieldLabelMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;

/**
 * Created by pvsubrah on 9/28/15.
 */
public class OleNoticeContentHandlerTest {


    @Test
    public void generateNoticeHTML() throws Exception {
        OleNoticeContentHandler oleNoticeContentHandler = new OleNoticeContentHandler();
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
        oleNoticeContentConfigurationBo.setNoticeName("OverdueNotice");
        oleNoticeContentConfigurationBo.setNoticeTitle("OverdueNotice");
        oleNoticeContentConfigurationBo.setNoticeType("OverdueNotice");

        ArrayList<OleNoticeFieldLabelMapping> oleNoticeFieldLabelMappings = new ArrayList<>();
        OleNoticeFieldLabelMapping patronName = new OleNoticeFieldLabelMapping();
        patronName.setFieldLabel("Patron Full Name:");
        patronName.setFieldName(OLEConstants.PATRON_NAME);


        OleNoticeFieldLabelMapping patronEmail = new OleNoticeFieldLabelMapping();
        patronEmail.setFieldLabel("Valid Email Id:");
        patronEmail.setFieldName(OLEConstants.NOTICE_EMAIL);


        OleNoticeFieldLabelMapping itemCallNum = new OleNoticeFieldLabelMapping();
        itemCallNum.setFieldLabel("Item Call Number:");
        itemCallNum.setFieldName(OLEConstants.NOTICE_CALL_NUMBER);

        oleNoticeFieldLabelMappings.add(patronName);

        oleNoticeContentConfigurationBo.setOleNoticeFieldLabelMappings(oleNoticeFieldLabelMappings);

        String html = oleNoticeContentHandler.generateHTML(oleNoticeBos, oleNoticeContentConfigurationBo);
        assertNotNull(html);
        System.out.println(html);
    }

}