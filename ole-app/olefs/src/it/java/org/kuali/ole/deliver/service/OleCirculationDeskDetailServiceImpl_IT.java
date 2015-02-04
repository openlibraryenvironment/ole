package org.kuali.ole.deliver.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OleCirculationDeskDetail;
import org.kuali.ole.deliver.form.OleCirculationDeskDetailForm;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 11/8/12
 * Time: 7:35 PM
 * To change this template use File | Settings | File Templates.
 */

public class OleCirculationDeskDetailServiceImpl_IT extends SpringBaseTestCase {

    private BusinessObjectService boService;
    private OleCirculationDeskDetailServiceImpl service;
    private OleCirculationDesk oleCirculationDesk;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        boService = KRADServiceLocator.getBusinessObjectService();
        service = new OleCirculationDeskDetailServiceImpl();

    }


    @Test
    @Transactional
    public void populateCreateListTest() {
        OleCirculationDesk oleCirculationDesk = new OleCirculationDesk();
        oleCirculationDesk.setCirculationDeskCode("code");
        oleCirculationDesk.setCirculationDeskPublicName("publicName");
        oleCirculationDesk.setCirculationDeskStaffName("staffName");
        oleCirculationDesk.setActive(true);
        oleCirculationDesk.setLocationId("1");

        boService = KRADServiceLocator.getBusinessObjectService();
        boolean exists = false;
        Collection<OleCirculationDesk> oleCirculationDeskCollection = boService.findAll(OleCirculationDesk.class);
        for (OleCirculationDesk circulationDesk : oleCirculationDeskCollection) {
            if (circulationDesk.getCirculationDeskCode()
                               .equalsIgnoreCase(oleCirculationDesk.getCirculationDeskCode())) {
                exists = true;
            }
        }

        if (!exists) {
            boService.save(oleCirculationDesk);
            List<OleCirculationDeskDetail> oleCirculationDeskDetailList = service.populateCreateList();
            for (int i = 0; i < oleCirculationDeskDetailList.size(); i++) {
                if (oleCirculationDeskDetailList.get(i).getOleCirculationDesk().getCirculationDeskCode()
                                                .equalsIgnoreCase("code")) {
                    assertEquals(
                            oleCirculationDeskDetailList.get(i).getOleCirculationDesk().getCirculationDeskPublicName(),
                            "publicName");
                }
            }
        }
    }

    @Test
    @Transactional
    public void performSaveTest() {

        OleCirculationDesk oleCirculationDesk = new OleCirculationDesk();
        oleCirculationDesk.setCirculationDeskCode("testCode");
        oleCirculationDesk.setCirculationDeskPublicName("publicName");
        oleCirculationDesk.setCirculationDeskStaffName("staffName");
        oleCirculationDesk.setActive(true);
        oleCirculationDesk.setLocationId("1");
        oleCirculationDesk.setOnHoldDays("2");

        boService = KRADServiceLocator.getBusinessObjectService();
        boolean exists = false;
        Collection<OleCirculationDesk> oleCirculationDeskCollection = boService.findAll(OleCirculationDesk.class);
        for (OleCirculationDesk circulationDesk : oleCirculationDeskCollection) {
            if (circulationDesk.getCirculationDeskCode()
                               .equalsIgnoreCase(oleCirculationDesk.getCirculationDeskCode())) {
                exists = true;
            }
        }

        if (!exists && oleCirculationDesk.getOnHoldDays() != null) {
            boService.save(oleCirculationDesk);
            Map<String, String> deskMap = new HashMap<String, String>();
            deskMap.put("circulationDeskCode", oleCirculationDesk.getCirculationDeskCode());
            List<OleCirculationDesk> oleCirculationDeskSave = (List<OleCirculationDesk>) boService
                    .findMatching(OleCirculationDesk.class, deskMap);
            if (oleCirculationDeskSave != null && oleCirculationDeskSave.size() > 0) {
                OleCirculationDeskDetail oleCirculationDeskDetail = new OleCirculationDeskDetail();
                oleCirculationDeskDetail.setCirculationDeskId(oleCirculationDeskSave.get(0).getCirculationDeskId());
                oleCirculationDeskDetail.setDefaultLocation(true);
                oleCirculationDeskDetail.setAllowedLocation(false);
                oleCirculationDeskDetail.setOperatorId("test1");
                oleCirculationDeskDetail.setOleCirculationDesk(oleCirculationDeskSave.get(0));
                List<OleCirculationDeskDetail> oleCirculationDeskDetailList = new ArrayList<OleCirculationDeskDetail>();
                oleCirculationDeskDetailList.add(oleCirculationDeskDetail);
                OleCirculationDeskDetailForm oleCirculationDeskDetailTestForm = new OleCirculationDeskDetailForm();
                oleCirculationDeskDetailTestForm.setOperatorId("test1");
                oleCirculationDeskDetailTestForm.setOleCirculationDetailsCreateList(oleCirculationDeskDetailList);
                OleCirculationDeskDetailForm oleCirculationDeskDetailForm = service
                        .performSave(oleCirculationDeskDetailTestForm);
                Map<String, String> optId = new HashMap<String, String>();
                optId.put("operatorId", "test1");
                List<OleCirculationDeskDetail> matchedList = (List<OleCirculationDeskDetail>) boService
                        .findMatching(OleCirculationDeskDetail.class, optId);
                for (int j = 0; j < matchedList.size(); j++) {
                    if (matchedList.get(j).getOleCirculationDesk().getCirculationDeskCode()
                                   .equalsIgnoreCase("testCode")) {
                        assertEquals(matchedList.get(j).getOleCirculationDesk().getCirculationDeskPublicName(),
                                     "publicName");
                    }
                }
            }
        }


    }

    @Test
    @Transactional
    public void performSearchTest() {
        OleCirculationDesk oleCirculationDesk = new OleCirculationDesk();
        oleCirculationDesk.setCirculationDeskCode("testCode");
        oleCirculationDesk.setCirculationDeskPublicName("publicName");
        oleCirculationDesk.setCirculationDeskStaffName("staffName");
        oleCirculationDesk.setActive(true);
        oleCirculationDesk.setLocationId("1");
        oleCirculationDesk.setOnHoldDays("2");

        boService = KRADServiceLocator.getBusinessObjectService();
        boolean exists = false;
        Collection<OleCirculationDesk> oleCirculationDeskCollection = boService.findAll(OleCirculationDesk.class);
        for (OleCirculationDesk circulationDesk : oleCirculationDeskCollection) {
            if (circulationDesk.getCirculationDeskCode()
                               .equalsIgnoreCase(oleCirculationDesk.getCirculationDeskCode())) {
                exists = true;
            }
        }

        if (!exists && oleCirculationDesk != null) {
            boService.save(oleCirculationDesk);
            Map<String, String> deskMap = new HashMap<String, String>();
            deskMap.put("circulationDeskCode", oleCirculationDesk.getCirculationDeskCode());
            List<OleCirculationDesk> oleCirculationDeskSave = (List<OleCirculationDesk>) boService
                    .findMatching(OleCirculationDesk.class, deskMap);
            if (oleCirculationDeskSave != null && oleCirculationDeskSave.size() > 0) {
                OleCirculationDeskDetail oleCirculationDeskDetail = new OleCirculationDeskDetail();
                oleCirculationDeskDetail.setCirculationDeskId(oleCirculationDeskSave.get(0).getCirculationDeskId());
                oleCirculationDeskDetail.setDefaultLocation(true);
                oleCirculationDeskDetail.setAllowedLocation(false);
                oleCirculationDeskDetail.setOperatorId("test1");
                oleCirculationDeskDetail.setOleCirculationDesk(oleCirculationDeskSave.get(0));
                List<OleCirculationDeskDetail> oleCirculationDeskDetailList = new ArrayList<OleCirculationDeskDetail>();
                oleCirculationDeskDetailList.add(oleCirculationDeskDetail);
                OleCirculationDeskDetailForm oleCirculationDeskDetailTestForm = new OleCirculationDeskDetailForm();
                oleCirculationDeskDetailTestForm.setOperatorId("test1");
                oleCirculationDeskDetailTestForm.setOleCirculationDetailsCreateList(oleCirculationDeskDetailList);
                OleCirculationDeskDetailForm oleCirculationDeskDetailForm = service
                        .performSave(oleCirculationDeskDetailTestForm);
                OleCirculationDeskDetailForm oleCirculationDeskDetailSearchForm = new OleCirculationDeskDetailForm();
                oleCirculationDeskDetailSearchForm.setOperatorId("test1");
                OleCirculationDeskDetailForm oleCirculationDeskDetailForm1 = service
                        .performSearch(oleCirculationDeskDetailSearchForm);
                List<OleCirculationDeskDetail> oleCirculationDeskDetail1 = oleCirculationDeskDetailForm1
                        .getOleCirculationDetailsCreateList();
                for (int k = 0; k < oleCirculationDeskDetail1.size(); k++) {
                    if (oleCirculationDeskDetail1.get(k).getOleCirculationDesk().getCirculationDeskCode()
                                                 .equalsIgnoreCase("testCode")) {
                        assertEquals(
                                oleCirculationDeskDetail1.get(k).getOleCirculationDesk().getCirculationDeskPublicName(),
                                "publicName");
                    }
                }
            }
        }
    }
}