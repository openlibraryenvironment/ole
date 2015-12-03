package org.kuali.ole.dsng.rest.processor;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.kuali.ole.DocstoreTestCaseBase;
import org.kuali.ole.bo.OLERenewItem;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by SheikS on 11/25/2015.
 */
public class OleDsNgRestAPIProcessor_IT extends DocstoreTestCaseBase{

    @Test
    public void testCreateBib() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        BibRecord bibRecord = new BibRecord();
        bibRecord.setContent(BIB_CONTENT);
        bibRecord.setCreatedBy("ole-quickstart");
        bibRecord.setFassAddFlag(false);
        OleDsNgRestAPIProcessor oleDsNgRestAPIProcessor = new OleDsNgRestAPIProcessor();
        String savedJsonObject = oleDsNgRestAPIProcessor.createBib(objectMapper.defaultPrettyPrintingWriter().writeValueAsString(bibRecord));
        assertNotNull(savedJsonObject);
        System.out.println(savedJsonObject);
        BibRecord savedBibRecord = objectMapper.readValue(savedJsonObject, BibRecord.class);
        System.out.println("Bib id : " + savedBibRecord.getBibId());
    }

    @Test
    public void testCreateHolding() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        HoldingsRecord holdingsRecord = new HoldingsRecord();
        holdingsRecord.setCreatedBy("ole-quickstart");
        holdingsRecord.setCopyNumber("CopyNumber");
        holdingsRecord.setCallNumber("CallNumber");
        holdingsRecord.setBibId("10000037");

        OleDsNgRestAPIProcessor oleDsNgRestAPIProcessor = new OleDsNgRestAPIProcessor();
        String savedJsonObject = oleDsNgRestAPIProcessor.createHolding(objectMapper.defaultPrettyPrintingWriter().writeValueAsString(holdingsRecord));
        assertNotNull(savedJsonObject);
        System.out.println(savedJsonObject);
        HoldingsRecord savedHoldingRecord = objectMapper.readValue(savedJsonObject, HoldingsRecord.class);
        System.out.println("Holding id : " + savedHoldingRecord.getHoldingsId());
    }

    @Test
    public void testRetrieveBibFromDb() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> parameterMap = new HashMap<String,Object>();
        parameterMap.put("bibId","10000044");
        List<BibRecord> matching = (List<BibRecord>) KRADServiceLocator.getBusinessObjectService().findMatching(BibRecord.class, parameterMap);
        if(CollectionUtils.isNotEmpty(matching)){
            BibRecord bibRecord = matching.get(0);
            String jsonString = objectMapper.defaultPrettyPrintingWriter().writeValueAsString(bibRecord);
            assertNotNull(jsonString);
            System.out.println(jsonString);
        }

    }

    @Test
    public void testRetrieveHoldingFromDb() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> parameterMap = new HashMap<String,Object>();
        parameterMap.put("holdingsId","25");
        List<HoldingsRecord> matching = (List<HoldingsRecord>) KRADServiceLocator.getBusinessObjectService().findMatching(HoldingsRecord.class, parameterMap);
        if(CollectionUtils.isNotEmpty(matching)){
            HoldingsRecord holdingsRecord = matching.get(0);
            String jsonString = objectMapper.defaultPrettyPrintingWriter().writeValueAsString(holdingsRecord);
            assertNotNull(jsonString);
            System.out.println(jsonString);
        }

    }

    @Test
    public void testRetrieveItemFromDb() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> parameterMap = new HashMap<String,Object>();
        parameterMap.put("itemId","38");
        List<ItemRecord> matching = (List<ItemRecord>) KRADServiceLocator.getBusinessObjectService().findMatching(ItemRecord.class, parameterMap);
        if(CollectionUtils.isNotEmpty(matching)){
            ItemRecord itemRecord = matching.get(0);
            String jsonString = objectMapper.defaultPrettyPrintingWriter().writeValueAsString(itemRecord);
            assertNotNull(jsonString);
            System.out.println(jsonString);
        }

    }

    private String BIB_CONTENT = "<collection xmlns=\"http://www.loc.gov/MARC21/slim\">\n" +
            "    <record>\n" +
            "    <leader>#####nam#a22######a#4500</leader>\n" +
            "    <controlfield tag=\"001\"></controlfield>\n" +
            "    <controlfield tag=\"003\">OCoLC</controlfield>\n" +
            "    <controlfield tag=\"005\">20090213152530.7</controlfield>\n" +
            "    <controlfield tag=\"008\">131031s########xxu###########000#0#eng#d</controlfield>\n" +
            "    <datafield tag=\"035\" ind1=\" \" ind2=\" \">\n" +
            "    <subfield code=\"a\">(OCoLC)ocm62378465</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"040\" ind1=\" \" ind2=\" \">\n" +
            "    <subfield code=\"a\">DLC</subfield>\n" +
            "    <subfield code=\"c\">DLC</subfield>\n" +
            "    <subfield code=\"d\">DLC</subfield>\n" +
            "    <subfield code=\"d\">HLS</subfield>\n" +
            "    <subfield code=\"d\">IUL</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"022\" ind1=\" \" ind2=\" \">\n" +
            "    <subfield code=\"a\">1729-1070|20</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"029\" ind1=\"1\" ind2=\" \">\n" +
            "    <subfield code=\"a\">AU@|b000040176476</subfield>\n" +
            "    <subfield code=\"b\">000040176476</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"037\" ind1=\" \" ind2=\" \">\n" +
            "    <subfield code=\"b\">The Managing Editor, BIAC Journal, P.O. Box 10026, Gaborone, Botswana</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"042\" ind1=\" \" ind2=\" \">\n" +
            "    <subfield code=\"a\">lc</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"043\" ind1=\"1\" ind2=\"0\">\n" +
            "    <subfield code=\"a\">f-bs---</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"050\" ind1=\"0\" ind2=\"0\">\n" +
            "    <subfield code=\"a\">HD70.B55|bB53</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"049\" ind1=\" \" ind2=\" \">\n" +
            "    <subfield code=\"a\">IULA</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"210\" ind1=\"1\" ind2=\" \">\n" +
            "    <subfield code=\"a\">BIAC j.</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"222\" ind1=\" \" ind2=\"0\">\n" +
            "    <subfield code=\"a\">BIAC journal</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"245\" ind1=\"0\" ind2=\"0\">\n" +
            "    <subfield code=\"a\">BIAC journal</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"246\" ind1=\"1\" ind2=\"3\">\n" +
            "    <subfield code=\"a\">Botswana Institute of Administration and Commerce journal</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"260\" ind1=\" \" ind2=\" \">\n" +
            "    <subfield code=\"a\">Gaborone, Botswana :|bBotswana Institute of Administration and Commerce</subfield>\n" +
            "    <subfield code=\"b\">Botswana Institute of Administration and Commerce</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"300\" ind1=\" \" ind2=\" \">\n" +
            "    <subfield code=\"a\">v. ;</subfield>\n" +
            "    <subfield code=\"c\">24 cm.</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"300\" ind1=\" \" ind2=\" \">\n" +
            "    <subfield code=\"a\">v. ;</subfield>\n" +
            "    <subfield code=\"c\">24 cm.</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"300\" ind1=\" \" ind2=\" \">\n" +
            "    <subfield code=\"a\">v. ;</subfield>\n" +
            "    <subfield code=\"c\">24 cm.</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"310\" ind1=\" \" ind2=\" \">\n" +
            "    <subfield code=\"a\">Semiannual</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"362\" ind1=\"1\" ind2=\" \">\n" +
            "    <subfield code=\"a\">Began in 2004.</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"500\" ind1=\" \" ind2=\" \">\n" +
            "    <subfield code=\"a\">Description based on: Vol. 1, no. 1 (May. 2004); title from cover.</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"500\" ind1=\" \" ind2=\" \">\n" +
            "    <subfield code=\"a\">Latest issue consulted: Vol. 3, no. 1 (May 2006).</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"650\" ind1=\"0\" ind2=\" \">\n" +
            "    <subfield code=\"a\">Industrial management</subfield>\n" +
            "    <subfield code=\"z\">Botswana</subfield>\n" +
            "    <subfield code=\"v\">Periodicals.</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"650\" ind1=\"0\" ind2=\" \">\n" +
            "    <subfield code=\"a\">Occupational training</subfield>\n" +
            "    <subfield code=\"z\">Botswana</subfield>\n" +
            "    <subfield code=\"v\">Periodicals.</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"710\" ind1=\"2\" ind2=\" \">\n" +
            "    <subfield code=\"a\">Botswana Institute of Administration and Commerce.</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"850\" ind1=\" \" ind2=\" \">\n" +
            "    <subfield code=\"a\">DLC</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"891\" ind1=\"2\" ind2=\"0\">\n" +
            "    <subfield code=\"a\">9853|81.1</subfield>\n" +
            "    <subfield code=\"a\">v.</subfield>\n" +
            "    <subfield code=\"b\">no</subfield>\n" +
            "    <subfield code=\"u\">2</subfield>\n" +
            "    <subfield code=\"v\">r</subfield>\n" +
            "    <subfield code=\"i\">(year)</subfield>\n" +
            "    <subfield code=\"j\">(month)</subfield>\n" +
            "    <subfield code=\"w\">f</subfield>\n" +
            "    <subfield code=\"x\">05</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"891\" ind1=\"4\" ind2=\"1\">\n" +
            "    <subfield code=\"a\">9863|81.1</subfield>\n" +
            "    <subfield code=\"a\">1</subfield>\n" +
            "    <subfield code=\"b\">1</subfield>\n" +
            "    <subfield code=\"i\">2004</subfield>\n" +
            "    <subfield code=\"j\">05</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"596\" ind1=\" \" ind2=\" \">\n" +
            "    <subfield code=\"a\">1</subfield>\n" +
            "    </datafield>\n" +
            "    </record>\n" +
            "    </collection>";
}