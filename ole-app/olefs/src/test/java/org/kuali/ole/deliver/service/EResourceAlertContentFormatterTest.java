package org.kuali.ole.deliver.service;

import org.junit.Assert;
import org.junit.Test;
import org.kuali.ole.deliver.batch.OleNoticeBo;
import org.kuali.ole.template.EresourceAlertContentFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maheswarang on 10/20/15.
 */
public class EResourceAlertContentFormatterTest {

    @Test
    public void testGenerateHtmlContent(){
        EresourceAlertContentFormatter eResourceAlertContentFormatter = new EresourceAlertContentFormatter();
        List<OleNoticeBo> oleNoticeBoList = new ArrayList<OleNoticeBo>();
        OleNoticeBo oleNoticeBo = new OleNoticeBo();
        oleNoticeBo.setNoticeTitle("Workflow Completion Alert");
        oleNoticeBo.setNoticeName("Test E-Resource Record");
        oleNoticeBo.setNoticeSpecificContent("Access Activation workflow has been completed");
       oleNoticeBoList.add(oleNoticeBo);
        String mailContent = eResourceAlertContentFormatter.generateHTML(oleNoticeBoList,null);
        Assert.assertNotNull(mailContent);
        System.out.println(mailContent);
    }
}
