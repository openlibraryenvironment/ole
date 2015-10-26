package org.kuali.ole.deliver;

import org.junit.Test;
import org.kuali.ole.KFSTestCaseBase;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.deliver.batch.OleDeliverBatchServiceImpl;
import org.kuali.ole.deliver.batch.OleNoticeBo;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 1/2/13
 * Time: 8:20 PM
 * To change this template use File | Settings | File Templates.
 */

public class OleDeliverBatchService_IT extends KFSTestCaseBase {

    @Test
    @Transactional
   public void TestOverDueNoticeTemplate() {
    List<OleNoticeBo> oleNoticeBoList = new ArrayList<OleNoticeBo>();
    try{
    OleNoticeBo oleNoticeBo = new OleNoticeBo();
    oleNoticeBo.setAuthor("MockOverDueAuthor");
    oleNoticeBo.setCirculationDeskAddress("MockOverDueCirAdd");
    oleNoticeBo.setCirculationDeskEmailAddress("MockOverDueEmailAdd");
    oleNoticeBo.setCirculationDeskName("MockCirDeskName");
    oleNoticeBo.setCirculationDeskPhoneNumber("MockCirPhoneNum");
    oleNoticeBo.setDueDate(new Date());
    oleNoticeBo.setNoticeName("Overdue Notice");
    oleNoticeBo.setPatronAddress("MockPatronAdd");
    oleNoticeBo.setPatronEmailAddress("MockPatronEmailAdd");
    oleNoticeBo.setPatronName("MockPatronName");
    oleNoticeBo.setPatronPhoneNumber("MockPatronPhone");
    oleNoticeBoList.add(oleNoticeBo);

    OleNoticeBo oleNoticeBo1 = new OleNoticeBo();
    oleNoticeBo1.setAuthor("MockOverDueAuthor1");
    oleNoticeBo1.setCirculationDeskAddress("MockOverDueCirAdd1");
    oleNoticeBo1.setCirculationDeskEmailAddress("MockOverDueEmailAdd1");
    oleNoticeBo1.setCirculationDeskName("MockCirDeskName1");
    oleNoticeBo1.setCirculationDeskPhoneNumber("MockCirPhoneNum1");
    oleNoticeBo1.setDueDate(new Date());
    oleNoticeBo1.setNoticeName("Overdue Notice");
    oleNoticeBo1.setPatronAddress("MockPatronAdd1");
    oleNoticeBo1.setPatronEmailAddress("MockPatronEmailAdd1");
    oleNoticeBo1.setPatronName("MockPatronName1");
    oleNoticeBo1.setPatronPhoneNumber("MockPatronPhone1");
    oleNoticeBoList.add(oleNoticeBo1);

    OleNoticeBo oleNoticeBo3 = new OleNoticeBo();
    oleNoticeBo3.setAuthor("MockOverDueAuthor3");
    oleNoticeBo3.setCirculationDeskAddress("MockOverDueCirAdd3");
    oleNoticeBo3.setCirculationDeskEmailAddress("MockOverDueEmailAdd3");
    oleNoticeBo3.setCirculationDeskName("MockCirDeskName");
    oleNoticeBo3.setCirculationDeskPhoneNumber("MockCirPhoneNum3");
    oleNoticeBo3.setDueDate(new Date());
    oleNoticeBo3.setNoticeName("Overdue Notice");
    oleNoticeBo3.setPatronAddress("MockPatronAdd3");
    oleNoticeBo3.setPatronEmailAddress("MockPatronEmailAdd3");
    oleNoticeBo3.setPatronName("MockPatronName3");
    oleNoticeBo3.setPatronPhoneNumber("MockPatronPhone3");
    oleNoticeBoList.add(oleNoticeBo3);

    OleNoticeBo oleNoticeBo2 = new OleNoticeBo();
    oleNoticeBo2.setAuthor("MockOverDueAuthor2");
    oleNoticeBo2.setCirculationDeskAddress("MockOverDueCirAdd2");
    oleNoticeBo2.setCirculationDeskEmailAddress("MockOverDueEmailAdd2");
    oleNoticeBo2.setCirculationDeskName("MockCirDeskName2");
    oleNoticeBo2.setCirculationDeskPhoneNumber("MockCirPhoneNum2");
    oleNoticeBo2.setDueDate(new Date());
    oleNoticeBo2.setNoticeName("OnHold");
    oleNoticeBo2.setPatronAddress("MockPatronAdd2");
    oleNoticeBo2.setPatronEmailAddress("MockPatronEmailAdd2");
    oleNoticeBo2.setPatronName("MockPatronName2");
    oleNoticeBo2.setPatronPhoneNumber("MockPatronPhone2");
    oleNoticeBoList.add(oleNoticeBo2);

    OleDeliverBatchServiceImpl oleDeliverBatchService = new OleDeliverBatchServiceImpl();
    List noticeFromPatron = oleDeliverBatchService.getNoticeForPatron(oleNoticeBoList);
    System.out.println(noticeFromPatron.get(0).toString());
    //assertNotNull(noticeFromPatron.get(0));
   // assertNotNull(noticeFromPatron);
    }catch (Exception e){
        e.getStackTrace();
    }

   }

    @Test
    @Transactional
    public void TestSMSTemplate() throws Exception{
        List<OleNoticeBo> oleNoticeBoList = new ArrayList<OleNoticeBo>();
        OleNoticeBo oleNoticeBo = new OleNoticeBo();
        oleNoticeBo.setAuthor("MockOverDueAuthor");
        oleNoticeBo.setCirculationDeskAddress("MockOverDueCirAdd");
        oleNoticeBo.setCirculationDeskEmailAddress("MockOverDueEmailAdd");
        oleNoticeBo.setCirculationDeskName("MockCirDeskName");
        oleNoticeBo.setCirculationDeskPhoneNumber("MockCirPhoneNum");
        oleNoticeBo.setItemCallNumber("MockCall");
        oleNoticeBo.setDueDate(new Date());
        oleNoticeBo.setNoticeName("Overdue Notice");
        oleNoticeBo.setPatronAddress("MockPatronAdd");
        oleNoticeBo.setPatronEmailAddress("MockPatronEmailAdd");
        oleNoticeBo.setPatronName("MockPatronName");
        oleNoticeBo.setPatronPhoneNumber("MockPatronPhone");
        oleNoticeBoList.add(oleNoticeBo);

        OleNoticeBo oleNoticeBo1 = new OleNoticeBo();
        oleNoticeBo1.setAuthor("MockOverDueAuthor1");
        oleNoticeBo1.setCirculationDeskAddress("MockOverDueCirAdd1");
        oleNoticeBo1.setCirculationDeskEmailAddress("MockOverDueEmailAdd1");
        oleNoticeBo1.setCirculationDeskName("MockCirDeskName1");
        oleNoticeBo1.setCirculationDeskPhoneNumber("MockCirPhoneNum1");
        oleNoticeBo1.setItemCallNumber("MockCall1");
        oleNoticeBo1.setDueDate(new Date());
        oleNoticeBo1.setNoticeName("Overdue Notice");
        oleNoticeBo1.setPatronAddress("MockPatronAdd1");
        oleNoticeBo1.setPatronEmailAddress("MockPatronEmailAdd1");
        oleNoticeBo1.setPatronName("MockPatronName1");
        oleNoticeBo1.setPatronPhoneNumber("MockPatronPhone1");
        oleNoticeBoList.add(oleNoticeBo1);

        OleNoticeBo oleNoticeBo3 = new OleNoticeBo();
        oleNoticeBo3.setAuthor("MockOverDueAuthor3");
        oleNoticeBo3.setCirculationDeskAddress("MockOverDueCirAdd3");
        oleNoticeBo3.setCirculationDeskEmailAddress("MockOverDueEmailAdd3");
        oleNoticeBo3.setCirculationDeskName("MockCirDeskName");
        oleNoticeBo3.setCirculationDeskPhoneNumber("MockCirPhoneNum3");
        oleNoticeBo3.setDueDate(new Date());
        oleNoticeBo3.setItemCallNumber("MockCall3");
        oleNoticeBo3.setNoticeName("Overdue Notice");
        oleNoticeBo3.setPatronAddress("MockPatronAdd3");
        oleNoticeBo3.setPatronEmailAddress("MockPatronEmailAdd3");
        oleNoticeBo3.setPatronName("MockPatronName3");
        oleNoticeBo3.setPatronPhoneNumber("MockPatronPhone3");
        oleNoticeBoList.add(oleNoticeBo3);

        OleNoticeBo oleNoticeBo2 = new OleNoticeBo();
        oleNoticeBo2.setAuthor("MockOverDueAuthor2");
        oleNoticeBo2.setCirculationDeskAddress("MockOverDueCirAdd2");
        oleNoticeBo2.setCirculationDeskEmailAddress("MockOverDueEmailAdd2");
        oleNoticeBo2.setCirculationDeskName("MockCirDeskName2");
        oleNoticeBo2.setCirculationDeskPhoneNumber("MockCirPhoneNum2");
        oleNoticeBo2.setDueDate(new Date());
        oleNoticeBo2.setNoticeName("OnHold");
        oleNoticeBo2.setPatronAddress("MockPatronAdd2");
        oleNoticeBo2.setPatronEmailAddress("MockPatronEmailAdd2");
        oleNoticeBo2.setPatronName("MockPatronName2");
        oleNoticeBo2.setPatronPhoneNumber("MockPatronPhone2");
        oleNoticeBoList.add(oleNoticeBo2);

        OleDeliverBatchServiceImpl oleDeliverBatchService = new OleDeliverBatchServiceImpl();
        Map map = oleDeliverBatchService.getSMSForPatron(oleNoticeBoList);
        HashMap sms = (HashMap)map.get("OVERDUE");
        Iterator it = sms.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            System.out.println(pairs.getKey() + " = " + pairs.getValue());
        }
        HashMap sms1 = (HashMap)map.get("HOLD");
        Iterator it1 = sms1.entrySet().iterator();
        while (it1.hasNext()) {
            Map.Entry pairs = (Map.Entry)it1.next();
            System.out.println(pairs.getKey() + " = " + pairs.getValue());
        }
       // System.out.println(sms);
    }

}
