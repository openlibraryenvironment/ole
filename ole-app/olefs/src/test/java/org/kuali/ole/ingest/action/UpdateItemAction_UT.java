package org.kuali.ole.ingest.action;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.BibliographicRecordHandler;
import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.service.DiscoveryHelperService;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.DataField;
import org.kuali.ole.ingest.FileUtil;
import org.kuali.ole.ingest.pojo.ProfileAttributeBo;
import org.kuali.ole.pojo.bib.BibliographicRecord;
import org.kuali.ole.pojo.bib.Collection;
import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.api.engine.ExecutionOptions;
import org.kuali.rice.krms.api.engine.SelectionCriteria;
import org.kuali.rice.krms.api.engine.Term;
import org.kuali.rice.krms.api.repository.agenda.AgendaDefinition;
import org.kuali.rice.krms.framework.engine.BasicExecutionEnvironment;
import org.kuali.rice.krms.framework.engine.TermResolutionEngineImpl;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.util.Assert;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: vivekb
 * Date: 5/11/12
 * Time: 6:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class UpdateItemAction_UT {

    @Mock
    private ExecutionEnvironment mockExecutionEnvironment;

    @Mock
    private DataCarrierService mockDataCarrierService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }
    // To-do, Need to be modified this UT based on current UpdateItem  functionality

    @Test
    public void testRouteToPrincipal() throws Exception {
       /* System.setProperty("app.environment", "local");
        UpdateItemAction updateItemAction = new MockUpdateItemAction();
        BibliographicRecordHandler bibliographicRecordHandler = new BibliographicRecordHandler();
        URL resource = getClass().getResource("update-item-marc.xml");
        File file = new File(resource.toURI());
        String xmlContent = new FileUtil().readFile(file);
        DiscoveryHelperService discoveryHelperService = new DiscoveryHelperService();

        Collection bibliographicRecords = bibliographicRecordHandler.fromXML(xmlContent);
        List<BibliographicRecord> records = bibliographicRecords.getRecords();
        List<ProfileAttributeBo> profileAttributeBos = getProfileAttributeBos();
        for (Iterator<BibliographicRecord> iterator = records.iterator(); iterator.hasNext(); ) {
            BibliographicRecord bibliographicRecord = iterator.next();
            List list = discoveryHelperService.getResponseFromSOLR("020a", getIsbn(bibliographicRecord));
            Mockito.when(mockDataCarrierService.getData(OLEConstants.REQUEST_BIB_RECORD)).thenReturn(bibliographicRecord);
            Mockito.when(mockDataCarrierService.getData(OLEConstants.PROFILE_ATTRIBUTE_LIST)).thenReturn(profileAttributeBos);
            Mockito.when(mockDataCarrierService.getData(OLEConstants.BIB_INFO_LIST_FROM_SOLR_RESPONSE)).thenReturn(list);
        }
        updateItemAction.execute(getMockExecutionEnvironment());
        Assert.isTrue((Boolean)mockExecutionEnvironment.getEngineResults().getAttribute(OLEConstants.UPDATE_ITEM_FLAG));*/
    }



    private List<ProfileAttributeBo> getProfileAttributeBos() {
        ProfileAttributeBo profileAttributeBo1 = new ProfileAttributeBo();
        profileAttributeBo1.setAgendaName(OLEConstants.PROFILE_AGENDA_NM);
        profileAttributeBo1.setId(1);
        profileAttributeBo1.setAttributeName(OLEConstants.PROFILE_ATTRIBUTE_NM);
        profileAttributeBo1.setAttributeValue(OLEConstants.PROFILE_ATTRIBUTE_VALUE);
        List<ProfileAttributeBo> mockProfileAttributes = Arrays.asList(profileAttributeBo1);
        return mockProfileAttributes;
    }

    private String getIsbn(BibliographicRecord bibliographicRecord){
        String isbn=null;
        List<DataField> marcDataFields = bibliographicRecord.getDatafields() ;
        for(DataField marcDataField :marcDataFields){
               if(marcDataField.getTag().equals("020")){
                  isbn = marcDataField.getSubFields().get(0).getValue();
                  break;
            }
        }
       return isbn;
    }

    public ExecutionEnvironment getMockExecutionEnvironment() {
       SelectionCriteria sc1 =
               SelectionCriteria.createCriteria(new DateTime(), Collections.<String, String>emptyMap(), Collections.singletonMap(
                        AgendaDefinition.Constants.EVENT, "foo"));
       mockExecutionEnvironment = new BasicExecutionEnvironment(sc1, Collections.<Term, Object>emptyMap(), new ExecutionOptions(), new TermResolutionEngineImpl());
       return mockExecutionEnvironment;
    }
    private class MockUpdateItemAction extends UpdateItemAction {
        @Override
        protected DataCarrierService getDataCarrierService() {
            return mockDataCarrierService;    //To change body of overridden methods use File | Settings | File Templates.
        }
    }
}