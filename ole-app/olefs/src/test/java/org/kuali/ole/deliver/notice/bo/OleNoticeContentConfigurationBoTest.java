package org.kuali.ole.deliver.notice.bo;

import org.junit.Test;
import org.kuali.ole.OLEConstants;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by pvsubrah on 9/30/15.
 */
public class OleNoticeContentConfigurationBoTest {

    @Test
    public void getLabel() throws Exception {
        OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo = new OleNoticeContentConfigurationBo();
        oleNoticeContentConfigurationBo.setActive(true);
        oleNoticeContentConfigurationBo.setNoticeName("Overdue Notice");
        oleNoticeContentConfigurationBo.setNoticeTitle("Overdue Notice");
        oleNoticeContentConfigurationBo.setNoticeType("Overdue Notice");

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

        String fieldLabel = oleNoticeContentConfigurationBo.getFieldLabel(OLEConstants.PATRON_NAME);
        assertEquals("Patron Full Name:", fieldLabel);

        String volumeLabel = oleNoticeContentConfigurationBo.getFieldLabel(OLEConstants.VOLUME_ISSUE_COPY);
        assertEquals(OLEConstants.VOLUME_ISSUE_COPY, volumeLabel);
    }

}