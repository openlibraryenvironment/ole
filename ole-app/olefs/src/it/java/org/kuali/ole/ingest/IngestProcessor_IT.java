package org.kuali.ole.ingest;

import org.junit.Test;
import org.kuali.ole.KFSTestCaseBase;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.ingest.krms.builder.OleKrmsBuilder;
import org.kuali.ole.ingest.pojo.IngestRecord;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.rice.krms.api.engine.EngineResults;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 3/30/12
 * Time: 9:21 AM
 * To change this template use File | Settings | File Templates.
 */

public class IngestProcessor_IT extends KFSTestCaseBase {

    @Test
    @Transactional
    public void testIngestProcessor() throws Exception {
        System.setProperty("app.environment", "local");
        OleKrmsBuilder profileBuilder = new OleKrmsBuilder();
        URL resource2 = getClass().getResource("profile_new.xml");
        File file2 = new File(resource2.toURI());
        String krmsXML = new FileUtil().readFile(file2);
        List<String> agendas = profileBuilder.persistKrmsFromFileContent(krmsXML);
        MockIngestProcessor ingestProcessor = new MockIngestProcessor();

//        URL resource = getClass().getResource("iu.mrc");
        URL resource = getClass().getResource("1MarcError.xml");
        File file = new File(resource.toURI());
        String rawMarcContent = new FileUtil().readFile(file);

        //URL resource1 = getClass().getResource("iu.edi");
        URL resource1 = getClass().getResource("1EdiError.xml");
        File file1 = new File(resource1.toURI());
        String rawEdiContent = new FileUtil().readFile(file1);

        IngestRecord ingestRecord = new IngestRecord();
        ingestRecord.setByPassPreProcessing(false);
        ingestRecord.setMarcFileContent(rawMarcContent);
        ingestRecord.setEdiFileContent(rawEdiContent);
        ingestRecord.setAgendaName(agendas.get(0));
        ingestRecord.setOriginalMarcFileName(file.getName());
        ingestRecord.setOriginalEdiFileName(file1.getName());
        boolean failure_flag = true;
        ingestProcessor.start(ingestRecord , failure_flag, null, null);

        List<EngineResults> engineResults = ingestProcessor.getEngineResults();
        System.out.println("Num results: " + engineResults.size());
        for (Iterator<EngineResults> iterator = engineResults.iterator(); iterator.hasNext(); ) {
            EngineResults results = iterator.next();
            System.out.println(OLEConstants.EXCEPTION_CREATION_FLAG + " : " + results.getAttribute(OLEConstants.EXCEPTION_CREATION_FLAG));
            System.out.println(OLEConstants.UPDATE_ITEM_FLAG + " : " + results.getAttribute(OLEConstants.UPDATE_ITEM_FLAG));
            System.out.println(OLEConstants.BIB_CREATION_FLAG + " : " + results.getAttribute(OLEConstants.BIB_CREATION_FLAG));
            OleOrderRecord oleOrderRecord = (OleOrderRecord) results.getAttribute(OLEConstants.OLE_ORDER_RECORD);
            Map<String,Object> messageMap = oleOrderRecord.getMessageMap();
            List rulesEvaulvated = (List) messageMap.get("rulesEvaluated");
            for (Iterator iterator1 = rulesEvaulvated.iterator(); iterator1.hasNext(); ) {
                String ruleEvauated = (String) iterator1.next();
                System.out.println(ruleEvauated);
            }
        }
    }
}
