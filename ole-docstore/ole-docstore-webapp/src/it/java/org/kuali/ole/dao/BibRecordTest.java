package org.kuali.ole.dao;

import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.DocstoreTestCaseBase;
import org.kuali.ole.docstore.common.document.Holdings;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibInfoRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;

/**
 * Created by pvsubrah on 5/7/15.
 */
public class BibRecordTest extends DocstoreTestCaseBase {

    protected BusinessObjectService businessObjectService;

    @Before
    public void setUp() throws Exception {
        businessObjectService = KRADServiceLocator.getBusinessObjectService();
    }

    @Test
    public void saveBib() throws Exception {
        BibRecord bibRecord = new BibRecord();
        bibRecord.setContent("mockContent");
        ArrayList<HoldingsRecord> holdingsRecords = new ArrayList<HoldingsRecord>();
        HoldingsRecord holdingsRecord = new HoldingsRecord();
        holdingsRecord.setAccessPassword("mockPassword");
        holdingsRecord.setPlatform("mockPlatform");
        holdingsRecords.add(holdingsRecord);
        bibRecord.setHoldingsRecords(holdingsRecords);

        BibRecord savedBibRecord = businessObjectService.save(bibRecord);
        assertNotNull(savedBibRecord);
        assertNotNull(savedBibRecord.getBibId());
        assertNotNull(holdingsRecord.getHoldingsId());
    }

    @Test
    public void saveBibInfo() throws Exception {
        BibRecord bibRecord = new BibRecord();
        bibRecord.setContent("mockContent");
        ArrayList<HoldingsRecord> holdingsRecords = new ArrayList<HoldingsRecord>();
        HoldingsRecord holdingsRecord = new HoldingsRecord();
        holdingsRecord.setAccessPassword("mockPassword");
        holdingsRecord.setPlatform("mockPlatform");
        holdingsRecords.add(holdingsRecord);
        bibRecord.setHoldingsRecords(holdingsRecords);

        BibInfoRecord bibInfoRecord = new BibInfoRecord();
        bibInfoRecord.setTitle("MockTitle");
        bibInfoRecord.setPublisher("mockPublisher");

        bibRecord.setBibInfoRecord(bibInfoRecord);

        BibRecord savedBibRecord = businessObjectService.save(bibRecord);
        assertNotNull(savedBibRecord);
        assertNotNull(savedBibRecord.getBibId());
        assertNotNull(holdingsRecord.getHoldingsId());
        assertNotNull(bibInfoRecord.getBibId());
    }

    @Test
    public void saveItems() throws Exception {
        BibRecord bibRecord = new BibRecord();
        bibRecord.setContent("mockContent");
        ArrayList<HoldingsRecord> holdingsRecords = new ArrayList<HoldingsRecord>();
        HoldingsRecord holdingsRecord = new HoldingsRecord();
        holdingsRecord.setAccessPassword("mockPassword");
        holdingsRecord.setPlatform("mockPlatform");
        holdingsRecords.add(holdingsRecord);
        bibRecord.setHoldingsRecords(holdingsRecords);

        BibInfoRecord bibInfoRecord = new BibInfoRecord();
        bibInfoRecord.setTitle("MockTitle");
        bibInfoRecord.setPublisher("mockPublisher");

        bibRecord.setBibInfoRecord(bibInfoRecord);

        ItemRecord itemRecord = new ItemRecord();
        itemRecord.setBarCode("12313213");
        ArrayList<ItemRecord> itemRecords = new ArrayList<ItemRecord>();
        itemRecords.add(itemRecord);
        holdingsRecord.setItemRecords(itemRecords);

        BibRecord savedBibRecord = businessObjectService.save(bibRecord);
        assertNotNull(savedBibRecord);
        assertNotNull(savedBibRecord.getBibId());
        assertNotNull(holdingsRecord.getHoldingsId());
        assertNotNull(bibInfoRecord.getBibId());
        assertNotNull(itemRecord.getItemId());
    }
}
