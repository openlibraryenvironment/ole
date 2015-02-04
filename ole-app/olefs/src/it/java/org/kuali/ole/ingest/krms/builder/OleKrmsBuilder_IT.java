package org.kuali.ole.ingest.krms.builder;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.ingest.FileUtil;
import org.kuali.ole.ingest.pojo.MatchBo;
import org.kuali.ole.service.OleCirculationPolicyService;
import org.kuali.ole.service.OleCirculationPolicyServiceImpl;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krms.api.KrmsApiServiceLocator;
import org.kuali.rice.krms.api.engine.*;
import org.kuali.rice.krms.framework.engine.BasicRule;
import org.kuali.rice.krms.impl.repository.AgendaBo;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static junit.framework.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 6/27/12
 * Time: 9:16 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class OleKrmsBuilder_IT extends SpringBaseTestCase{

    private static final String NAMESPACE_CODE_SELECTOR = "namespaceCode";
    private static final String NAME_SELECTOR = "name";

    private OleKrmsBuilder krmsBuilder = new OleKrmsBuilder();

    @Ignore
    @Test
    @Transactional
    public void testPersistLoan() throws Exception{
        OleCirculationPolicyService oleCirculationPolicyService  = new OleCirculationPolicyServiceImpl();
        URL resource = getClass().getResource("deliver.xml");
        DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
        dataCarrierService.addData("listOfOverDueDays",new ArrayList<Integer>());
        dataCarrierService.addData("listOfRecalledOverdueDays",new ArrayList<Integer>());
        File file = new File(resource.toURI());
        String krmsXML = new FileUtil().readFile(file);
       // List<String> agendas = krmsBuilder.persistKrmsFromFileContent(krmsXML);
      //  assertEquals(3,agendas.size());
        String ruleName = null;
        Date d = new Date(0000,03,03);
        dataCarrierService.addData("hoursDiff",oleCirculationPolicyService.getHoursDiff(new Date(2012,11,10),new Date(2012,10,10)));
        String agendaName =  "General Checks";
        HashMap<String,Object> termValues = new HashMap<String, Object>() ;
        termValues.put("generalBlock","true");
        termValues.put("expirationDate",d) ;
        testEngineResultsLoan(agendaName,termValues,null);

        agendaName = "CheckOut Validation";
        termValues = new HashMap<String, Object>() ;
        termValues.put("borrowerType","Graduate");
        termValues.put("itemType","book");
        termValues.put("location","Stacks");
        termValues.put("numberOfItemsCheckedOut","11");
        termValues.put("numberOfOverDueItemsCheckedOut","8");
        termValues.put("numberOfOverDueRecalledItemsCheckedOut","45") ;
        termValues.put("numberOfRecallOverdueDays","20") ;
        termValues.put("numberOfClaimsReturned","6") ;
        termValues.put("overdueFineAmt","200") ;
        termValues.put("replacementFeeAmt","160") ;
        termValues.put("isRenewal","true");
        termValues.put("numberOfRenewals","2");
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String dateToString = formatter.format(d);
        termValues.put("itemDueDate",dateToString);
        termValues.put("itemStatus","inTransit") ;
        termValues.put("digitRoutine","Yes") ;
        termValues.put("itemBarcode","Nil") ;
        termValues.put("numberOfPiecesReturned","5");
        termValues.put("numberOfPiecesFromRecord","5");
        termValues.put("numberOfPiecesFromRecord","5");
        termValues.put("circulationLocation","CDE");
        termValues.put("itemLocation","ABCDEFGHI");
        testEngineResultsLoan(agendaName, termValues, ruleName + agendaName);

        agendaName = "Check-in Validation";
        termValues = new HashMap<String, Object>() ;
        termValues.put("borrowerType","Graduate");
        termValues.put("itemType","book");
        termValues.put("location","Stacks");
        termValues.put("itemLocation","Stacks");
        termValues.put("operatorsCirculationLocation","Stacks");
        termValues.put("itemDueDate",new Date());
        termValues.put("numberOfPiecesOfItem","5");
        termValues.put("itemStatus","lost");
        termValues.put("requestType","recall/delivery");
        testEngineResultsLoan(agendaName,termValues,ruleName+agendaName);

        agendaName = "Request Validation";
        termValues.put("borrowerType","Graduate");
        termValues.put("itemType","book");
        termValues.put("location","ILXSTACKS");
        termValues.put("maxNumberOfRecallRequest","");
        termValues.put("maxNumberOfHoldRequest","");
        termValues.put("maxNumberOfPageRequest","");
        termValues.put("maxNumberOfRequestByBorrower","");
        termValues.put("requestTypeId","5");
        testEngineResultsLoan(agendaName,termValues,ruleName+agendaName);
    }

    @Test
    @Transactional
    public void testDeleteLoan() throws Exception{

        URL resource = getClass().getResource("deliver.xml");
        File file = new File(resource.toURI());
        String krmsXML = new FileUtil().readFile(file);
        //assertNotNull(krmsBuilder.persistKrmsFromFileContent(krmsXML).get(0));
        //assertNotNull(krmsBuilder.persistKrmsFromFileContent(krmsXML).get(0));

    }

    public boolean testEngineResultsLoan(String agendaName,HashMap<String,Object> termValues,String ruleName){
        Engine engine = KrmsApiServiceLocator.getEngine();

        HashMap<String, Object> agendaValue = new HashMap<String, Object>();
        agendaValue.put("nm", agendaName);
        List<AgendaBo> agendaBos = (List<AgendaBo>) KRADServiceLocator.getBusinessObjectService().findMatching(AgendaBo.class, agendaValue);
        AgendaBo agendaBo = agendaBos.get(0);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("AGENDA_NAME", agendaBo.getName());
        List<MatchBo> matchBos = (List<MatchBo>) KRADServiceLocator.getBusinessObjectService().findMatching(MatchBo.class, map);

        SelectionCriteria selectionCriteria =
                SelectionCriteria.createCriteria(null, getSelectionContext(agendaBo.getContext().getName()), getAgendaContext(agendaName));

        ExecutionOptions executionOptions = new ExecutionOptions();
        executionOptions.setFlag(ExecutionFlag.LOG_EXECUTION, true);

        Facts.Builder factBuilder = Facts.Builder.create();

        for (Iterator<MatchBo> matchBoIterator = matchBos.iterator(); matchBoIterator.hasNext(); ) {
            MatchBo matchBo = matchBoIterator.next();
            factBuilder.addFact(matchBo.getTermName(), termValues.get((matchBo.getTermName())));
        }


        EngineResults engineResult = engine.execute(selectionCriteria, factBuilder.build(), executionOptions);
        List<ResultEvent> allResults = engineResult.getAllResults();
        boolean results = true;
        boolean flag = false;
        int i=0;
        StringBuffer failures = new StringBuffer();
        for (Iterator<ResultEvent> resultEventIterator = allResults.iterator(); resultEventIterator.hasNext(); ) {
            ResultEvent resultEvent = resultEventIterator.next();
            if (resultEvent.getType().equals("Rule Evaluated")) {
                BasicRule source = (BasicRule)resultEvent.getSource() ;
                if(ruleName!=null && ruleName.equals(source.getName())){
                    return resultEvent.getResult();
                }
                results &= resultEvent.getResult();
            }
        }
        return results;
    }
    protected Map<String, String> getSelectionContext(String contextName) {
        Map<String, String> selector = new HashMap<String, String>();
        selector.put(NAMESPACE_CODE_SELECTOR, "OLE");
        selector.put(NAME_SELECTOR, contextName);
        return selector;
    }
    protected Map<String, String> getAgendaContext(String agendaName) {
        Map<String, String> selector = new HashMap<String, String>();
        selector.put(NAME_SELECTOR, agendaName);
        return selector;
    }
}
