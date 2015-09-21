package org.kuali.ole.deliver.util;

import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.deliver.api.OlePatronDefinition;
import org.kuali.ole.deliver.api.OlePatronDefintionHelper;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.service.CircDeskLocationResolver;
import org.kuali.ole.service.OlePatronService;
import org.kuali.ole.service.OlePatronServiceImpl;
import org.kuali.rice.kim.impl.identity.address.EntityAddressBo;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameBo;
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneBo;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.*;

/**
 * Created by sheiksalahudeenm on 7/29/15.
 */
public class OnHoldCourtesyNoticeUtilTest {

    @Mock
    private OlePatronDocument mockOlePatronDocument;
    @Mock
    private CircDeskLocationResolver mockCircDeskLocationResolver;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testGenerateRequestMailContentForPatron() throws Exception {
        EntityBo entityBo = new EntityBo();

        EntityTypeContactInfoBo entityTypeContactInfoBo = new EntityTypeContactInfoBo();

        EntityAddressBo entityAddressBo = new EntityAddressBo();
        entityAddressBo.setCity("Chennai");
        entityAddressBo.setCountryCode("India");
        entityAddressBo.setLine1("MEPZ");
        entityAddressBo.setLine2("Sanatoriam");
        entityAddressBo.setLine3("Tambaram");
        entityAddressBo.setActive(true);
        entityAddressBo.setDefaultValue(true);
        List<EntityAddressBo> addresses = new ArrayList<>();
        addresses.add(entityAddressBo);
        entityTypeContactInfoBo.setAddresses(addresses);

        EntityPhoneBo entityPhoneBo = new EntityPhoneBo();
        entityPhoneBo.setExtensionNumber("123456");
        entityPhoneBo.setPhoneNumber("654321");
        entityPhoneBo.setPhoneTypeCode("HM");
        entityPhoneBo.setActive(true);
        entityPhoneBo.setDefaultValue(true);
        List<EntityPhoneBo> entityPhoneBos = new ArrayList<>();
        entityPhoneBos.add(entityPhoneBo);
        entityTypeContactInfoBo.setPhoneNumbers(entityPhoneBos);

        EntityNameBo entityNameBo = new EntityNameBo();
        entityNameBo.setFirstName("First Name");
        entityNameBo.setMiddleName("Middle Name");
        entityNameBo.setLastName("Last Name");
        entityNameBo.setActive(true);
        entityNameBo.setDefaultValue(true);
        List<EntityNameBo> names = new ArrayList<>();
        names.add(entityNameBo);

        entityBo.setNames(names);

        entityBo.setEntityTypeContactInfos(Collections.singletonList(entityTypeContactInfoBo));

        Mockito.when(mockOlePatronDocument.getEntity()).thenReturn(entityBo);
        Mockito.when(mockOlePatronDocument.getOlePatronId()).thenReturn("10001");
        Mockito.when(mockOlePatronDocument.getBarcode()).thenReturn("60000123");
        Mockito.when(mockOlePatronDocument.getPatronName()).thenReturn("lastName,FirstName");
        OlePatronService olePatronService = new OlePatronServiceImpl();
        OnHoldCourtesyNoticeUtil onHoldCourtesyNoticeUtil = new OnHoldCourtesyNoticeUtil();
        OleDeliverRequestBo oleDeliverRequestBo = new OleDeliverRequestBo();
        oleDeliverRequestBo.setRequestId("1");
        oleDeliverRequestBo.setItemId("101");

        oleDeliverRequestBo.setBorrowerId("100001");
        oleDeliverRequestBo.setBorrowerBarcode("60000123");
        oleDeliverRequestBo.setOlePatron(mockOlePatronDocument);

        Map<String,String> fieldLabelMap  = new HashMap<>();
        fieldLabelMap.put("noticeTitle","OnHoldNotice");
        fieldLabelMap.put("noticeBody"," The following requested item(s) is ready for pick-up and will be held until the expiration date at the location shown below.");

        String mailContent = onHoldCourtesyNoticeUtil.generateRequestMailContentForPatron(oleDeliverRequestBo, fieldLabelMap);
        assertNotNull(mailContent);
        System.out.println("Mail Content : \n" + mailContent);


    }
}