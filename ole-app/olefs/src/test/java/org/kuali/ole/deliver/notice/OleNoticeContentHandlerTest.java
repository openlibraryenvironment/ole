package org.kuali.ole.deliver.notice;

import org.junit.Test;
import org.kuali.ole.deliver.batch.OleNoticeBo;

import java.util.Date;

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
        oleNoticeBo.setDueDate(new Date());
        oleNoticeBo.setItemShelvingLocation("UC/JRL/GEN");
        oleNoticeBo.setItemCallNumber("X-123");
        oleNoticeBo.setItemId("1234");
        oleNoticeBo.setNoticeSpecificContent("This is a test notice. Please ignore!!");
        String html = oleNoticeContentHandler.generateHTML("Overdue Notice", oleNoticeBo);
        assertNotNull(html);
        System.out.println(html);
    }

}