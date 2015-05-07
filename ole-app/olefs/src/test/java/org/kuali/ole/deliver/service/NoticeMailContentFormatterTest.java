package org.kuali.ole.deliver.service;

import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameBo;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

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

//        String mailContent = noticeMailContentFormatter.generateMailContentForPatron(oleLoanDocument);
        String mailContent = null;
        System.out.println(mailContent);

    }

    public class MockNoticeMailContentFormatter extends NoticeMailContentFormatter {
        @Override
        protected SimpleDateFormat getSimpleDateFormat() {
            return  new SimpleDateFormat();
        }

        @Override
        protected String generateCustomHTML(OleLoanDocument oleLoanDocument) {
            return "";
        }
    }
}