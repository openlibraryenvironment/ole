package org.kuali.ole.deliver.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.notice.executors.HoldExpirationNoticesExecutor;
import org.kuali.ole.deliver.notice.executors.RecallNoticesExecutor;
import org.kuali.ole.deliver.notice.executors.RequestExpirationNoticesExecutor;
import org.kuali.ole.deliver.notice.noticeFormatters.RecallRequestEmailContentFormatter;
import org.kuali.ole.deliver.notice.noticeFormatters.RequestExpirationEmailContentFormatter;
import org.kuali.ole.service.OlePatronHelperService;
import org.kuali.ole.service.OlePatronHelperServiceImpl;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maheswarang on 6/23/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class NoticesExecutor_IT {



    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(NoticesExecutor_IT.class);
    }

    @Test
    public void testGenerateRequestExpirationNoticesMailContent(){
        OleDeliverRequestBo oleDeliverRequestBo = Mockito.mock(OleDeliverRequestBo.class);
        OlePatronDocument olePatronDocument = Mockito.mock(OlePatronDocument.class);
        oleDeliverRequestBo.setOlePatron(olePatronDocument);
        OleCirculationDesk oleCirculationDesk = Mockito.mock(OleCirculationDesk.class);
        oleDeliverRequestBo.setOlePickUpLocation(oleCirculationDesk);
        List<OleDeliverRequestBo> oleDeliverRequestBoList = new ArrayList<OleDeliverRequestBo>();
        oleDeliverRequestBoList.add(oleDeliverRequestBo);
        RequestExpirationNoticesExecutor requestExpirationNoticesExecutor = new RequestExpirationNoticesExecutor(oleDeliverRequestBoList);
        ParameterValueResolver parameterValueResolver = Mockito.mock(ParameterValueResolver.class);
        Mockito.when(parameterValueResolver.getParameter(Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.eq(" EXP_REQ_TITLE"))).thenReturn("Request Expiration Notice");
        Mockito.when(parameterValueResolver.getParameter(Mockito.anyString(),Mockito.anyString(),Mockito.anyString(), Mockito.eq("EXP_REQ_BODY"))).thenReturn("Your Request Place on the item 1 is expired");
        requestExpirationNoticesExecutor.setParameterResolverInstance(parameterValueResolver);
        RequestExpirationEmailContentFormatter noticeRequestExpirationEmailContentFormatter = Mockito.spy(new RequestExpirationEmailContentFormatter());
        Mockito.when(noticeRequestExpirationEmailContentFormatter.getEntityTypeContactInfo(Mockito.any(OlePatronDocument.class))).thenReturn(new EntityTypeContactInfoBo());
        OlePatronHelperService olePatronHelperService = Mockito.mock(OlePatronHelperService.class);
        try{
        Mockito.when(olePatronHelperService.getPatronHomeEmailId(Mockito.any(EntityTypeContactInfoBo.class))).thenReturn("Mahesh@htcindia.com");
        Mockito.when(olePatronHelperService.getPatronHomePhoneNumber(Mockito.any(EntityTypeContactInfoBo.class))).thenReturn("1234-1234-1234");
        Mockito.when(olePatronHelperService.getPatronPreferredAddress(Mockito.any(EntityTypeContactInfoBo.class))).thenReturn("chennai");
        }catch(Exception e){

        }
        noticeRequestExpirationEmailContentFormatter.setOlePatronHelperService(olePatronHelperService);
        requestExpirationNoticesExecutor.generateMailContent(oleDeliverRequestBoList);

    }




    @Test
    public void testGenerateRequestHoldExpirationNoticesMailContent(){
        OleDeliverRequestBo oleDeliverRequestBo = Mockito.mock(OleDeliverRequestBo.class);
        OlePatronDocument olePatronDocument = Mockito.mock(OlePatronDocument.class);
        oleDeliverRequestBo.setOlePatron(olePatronDocument);
        OleCirculationDesk oleCirculationDesk = Mockito.mock(OleCirculationDesk.class);
        oleDeliverRequestBo.setOlePickUpLocation(oleCirculationDesk);
        List<OleDeliverRequestBo> oleDeliverRequestBoList = new ArrayList<OleDeliverRequestBo>();
        oleDeliverRequestBoList.add(oleDeliverRequestBo);
        HoldExpirationNoticesExecutor holdExpirationNoticesExecutor = new HoldExpirationNoticesExecutor(oleDeliverRequestBoList);
        ParameterValueResolver parameterValueResolver = Mockito.mock(ParameterValueResolver.class);
        Mockito.when(parameterValueResolver.getParameter(Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.eq("EXPIRED_TITLE"))).thenReturn("Hold Courtesy Notice");
        Mockito.when(parameterValueResolver.getParameter(Mockito.anyString(),Mockito.anyString(),Mockito.anyString(), Mockito.eq("EXP_HOLD_NOTICE_CONTENT"))).thenReturn("The item is kept hold in the circulation desk for you to pick up .since you haven't picked up the book the item is moved to shelf.Place a new Request for that item");
        holdExpirationNoticesExecutor.setParameterResolverInstance(parameterValueResolver);
        RequestExpirationEmailContentFormatter noticeRequestExpirationEmailContentFormatter = Mockito.spy(new RequestExpirationEmailContentFormatter());
        Mockito.when(noticeRequestExpirationEmailContentFormatter.getEntityTypeContactInfo(Mockito.any(OlePatronDocument.class))).thenReturn(new EntityTypeContactInfoBo());
        OlePatronHelperService olePatronHelperService = Mockito.mock(OlePatronHelperService.class);
        try{
            Mockito.when(olePatronHelperService.getPatronHomeEmailId(Mockito.any(EntityTypeContactInfoBo.class))).thenReturn("Mahesh@htcindia.com");
            Mockito.when(olePatronHelperService.getPatronHomePhoneNumber(Mockito.any(EntityTypeContactInfoBo.class))).thenReturn("1234-1234-1234");
            Mockito.when(olePatronHelperService.getPatronPreferredAddress(Mockito.any(EntityTypeContactInfoBo.class))).thenReturn("chennai");
        }catch(Exception e){

        }
        noticeRequestExpirationEmailContentFormatter.setOlePatronHelperService(olePatronHelperService);
        holdExpirationNoticesExecutor.generateMailContent(oleDeliverRequestBoList);
    }

    @Test
    public void testGenerateHoldNoticesMailContent(){
        OleDeliverRequestBo oleDeliverRequestBo = Mockito.mock(OleDeliverRequestBo.class);
        OlePatronDocument olePatronDocument = Mockito.mock(OlePatronDocument.class);
        oleDeliverRequestBo.setOlePatron(olePatronDocument);
        OleCirculationDesk oleCirculationDesk = Mockito.mock(OleCirculationDesk.class);
        oleDeliverRequestBo.setOlePickUpLocation(oleCirculationDesk);
        List<OleDeliverRequestBo> oleDeliverRequestBoList = new ArrayList<OleDeliverRequestBo>();
        oleDeliverRequestBoList.add(oleDeliverRequestBo);
        HoldExpirationNoticesExecutor holdExpirationNoticesExecutor = new HoldExpirationNoticesExecutor(oleDeliverRequestBoList);
        ParameterValueResolver parameterValueResolver = Mockito.mock(ParameterValueResolver.class);
        Mockito.when(parameterValueResolver.getParameter(Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.eq("ONHOLD_TITLE"))).thenReturn("On Hold Notice");
        Mockito.when(parameterValueResolver.getParameter(Mockito.anyString(),Mockito.anyString(),Mockito.anyString(), Mockito.eq("ONHOLD_BODY"))).thenReturn("The request you have on the item is available to pick up and the item will be on hold for you till 22nd of june ");
        holdExpirationNoticesExecutor.setParameterResolverInstance(parameterValueResolver);
        RecallRequestEmailContentFormatter noticeRequestExpirationEmailContentFormatter = Mockito.spy(new RecallRequestEmailContentFormatter());
        Mockito.when(noticeRequestExpirationEmailContentFormatter.getEntityTypeContactInfo(Mockito.any(OlePatronDocument.class))).thenReturn(new EntityTypeContactInfoBo());
        OlePatronHelperService olePatronHelperService = Mockito.mock(OlePatronHelperService.class);
        try{
            Mockito.when(olePatronHelperService.getPatronHomeEmailId(Mockito.any(EntityTypeContactInfoBo.class))).thenReturn("Mahesh@htcindia.com");
            Mockito.when(olePatronHelperService.getPatronHomePhoneNumber(Mockito.any(EntityTypeContactInfoBo.class))).thenReturn("1234-1234-1234");
            Mockito.when(olePatronHelperService.getPatronPreferredAddress(Mockito.any(EntityTypeContactInfoBo.class))).thenReturn("chennai");
        }catch(Exception e){

        }
        noticeRequestExpirationEmailContentFormatter.setOlePatronHelperService(olePatronHelperService);
        holdExpirationNoticesExecutor.generateMailContent(oleDeliverRequestBoList);
    }


    @Test
    public void testGenerateRecallNoticesMailContent(){
        OleDeliverRequestBo oleDeliverRequestBo = Mockito.mock(OleDeliverRequestBo.class);
        OlePatronDocument olePatronDocument = Mockito.mock(OlePatronDocument.class);
        oleDeliverRequestBo.setOlePatron(olePatronDocument);
        OleCirculationDesk oleCirculationDesk = Mockito.mock(OleCirculationDesk.class);
        oleDeliverRequestBo.setOlePickUpLocation(oleCirculationDesk);
        Mockito.when(oleDeliverRequestBo.getOriginalDueDate()).thenReturn(new Date(7, 7, 2015));
        Mockito.when(oleDeliverRequestBo.getRecallDueDate()).thenReturn(new Timestamp(2015,7,1,0,0,0,0));
        List<OleDeliverRequestBo> oleDeliverRequestBoList = new ArrayList<OleDeliverRequestBo>();
        oleDeliverRequestBoList.add(oleDeliverRequestBo);
        RecallNoticesExecutor holdExpirationNoticesExecutor = new RecallNoticesExecutor(oleDeliverRequestBoList);
        ParameterValueResolver parameterValueResolver = Mockito.mock(ParameterValueResolver.class);
        Mockito.when(parameterValueResolver.getParameter(Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.eq("RECALL_TITLE"))).thenReturn("Recall Notice");
        Mockito.when(parameterValueResolver.getParameter(Mockito.anyString(),Mockito.anyString(),Mockito.anyString(), Mockito.eq("RECALL_BODY"))).thenReturn("Item is needed by another patron .so kindly return the item before the date specified below");
        holdExpirationNoticesExecutor.setParameterResolverInstance(parameterValueResolver);
        RecallRequestEmailContentFormatter noticeRequestExpirationEmailContentFormatter = Mockito.spy(new RecallRequestEmailContentFormatter());
        Mockito.when(noticeRequestExpirationEmailContentFormatter.getEntityTypeContactInfo(Mockito.any(OlePatronDocument.class))).thenReturn(new EntityTypeContactInfoBo());
        OlePatronHelperService olePatronHelperService = Mockito.mock(OlePatronHelperServiceImpl.class);
        noticeRequestExpirationEmailContentFormatter.setOlePatronHelperService(olePatronHelperService);
        try{
            Mockito.when(olePatronHelperService.getPatronHomeEmailId(Mockito.any(EntityTypeContactInfoBo.class))).thenReturn("Mahesh@htcindia.com");
            Mockito.when(olePatronHelperService.getPatronHomePhoneNumber(Mockito.any(EntityTypeContactInfoBo.class))).thenReturn("1234-1234-1234");
            Mockito.when(olePatronHelperService.getPatronPreferredAddress(Mockito.any(EntityTypeContactInfoBo.class))).thenReturn("chennai");
        }catch(Exception e){
        }
        holdExpirationNoticesExecutor.setRequestEmailContentFormatter(noticeRequestExpirationEmailContentFormatter);
        holdExpirationNoticesExecutor.generateMailContent(oleDeliverRequestBoList);
    }

}
