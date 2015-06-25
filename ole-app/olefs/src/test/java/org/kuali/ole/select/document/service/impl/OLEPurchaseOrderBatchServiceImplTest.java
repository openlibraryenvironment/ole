package org.kuali.ole.select.document.service.impl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.module.purap.businessobject.InvoiceAccount;
import org.kuali.ole.module.purap.businessobject.PurApAccountingLine;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.businessobject.OleRequestSourceType;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.bo.DocumentHeader;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by pvsubrah on 6/13/15.
 */
public class OLEPurchaseOrderBatchServiceImplTest {

    @Mock
    private FileWriter mockFileWriter;
    @Mock
    private OlePurchaseOrderDocument mockPurchaseOrderDocument;
    @Mock
    private DocumentHeader mockDocumentHeader;
    @Mock
    private OlePurchaseOrderItem mockItem1;
    @Mock
    private OlePurchaseOrderItem mockItem2;
    @Mock
    private OlePurchaseOrderItem mockItem3;


    private String tempDir;
    private String fileName;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        tempDir = System.getProperty("java.io.tmpdir");
        fileName = tempDir + "poba.csv";
    }

    @Test
    public void testDownloadCSV() throws Exception {
        OLEPurchaseOrderBatchServiceImpl olePurchaseOrderBatchService = new OLEPurchaseOrderBatchServiceImpl();

        olePurchaseOrderBatchService.setFileWriter(new FileWriter(fileName));
        olePurchaseOrderBatchService.setBusinessObjectService(new CustomBusinessObjectService());

        ArrayList items = new ArrayList();
        Mockito.when(mockItem1.getItemLineNumber()).thenReturn(1);
        ArrayList<PurApAccountingLine> purApAccountingLines = new ArrayList<>();
        PurApAccountingLine purApAccountingLine = (PurApAccountingLine) getAccountingLineClass().newInstance();
        purApAccountingLines.add(purApAccountingLine);
        Mockito.when(mockItem1.getSourceAccountingLines()).thenReturn(purApAccountingLines);
        Mockito.when(mockItem1.getFormatTypeName()).thenReturn(null);
        Mockito.when(mockItem1.getCategory()).thenReturn(null);
        Mockito.when(mockItem1.getItemPriceSource()).thenReturn(null);
        Mockito.when(mockItem1.getOleRequestSourceType()).thenReturn(new OleRequestSourceType());
        Mockito.when(mockItem1.isItemRouteToRequestorIndicator()).thenReturn(true);
        Mockito.when(mockItem1.isItemPublicViewIndicator()).thenReturn(true);
        Mockito.when(mockItem1.getItemQuantity()).thenReturn(new KualiDecimal(2.0));
        Mockito.when(mockItem1.getItemListPrice()).thenReturn(new KualiDecimal(2.0));
        Mockito.when(mockItem1.getItemNoOfParts()).thenReturn(new KualiInteger(12));

        items.add(mockItem1);

        Mockito.when(mockItem2.getItemLineNumber()).thenReturn(2);
        Mockito.when(mockItem2.getSourceAccountingLines()).thenReturn(purApAccountingLines);
        Mockito.when(mockItem2.getFormatTypeName()).thenReturn(null);
        Mockito.when(mockItem2.getCategory()).thenReturn(null);
        Mockito.when(mockItem2.getItemPriceSource()).thenReturn(null);
        Mockito.when(mockItem2.getOleRequestSourceType()).thenReturn(new OleRequestSourceType());
        Mockito.when(mockItem2.isItemRouteToRequestorIndicator()).thenReturn(true);
        Mockito.when(mockItem2.isItemRouteToRequestorIndicator()).thenReturn(true);
        Mockito.when(mockItem2.isItemPublicViewIndicator()).thenReturn(true);
        Mockito.when(mockItem2.getItemQuantity()).thenReturn(new KualiDecimal(2.0));
        Mockito.when(mockItem2.getItemListPrice()).thenReturn(new KualiDecimal(2.0));
        Mockito.when(mockItem2.getItemNoOfParts()).thenReturn(new KualiInteger(12));
        items.add(mockItem2);

        Mockito.when(mockItem3.getItemLineNumber()).thenReturn(3);
        Mockito.when(mockItem3.getSourceAccountingLines()).thenReturn(purApAccountingLines);
        Mockito.when(mockItem3.getFormatTypeName()).thenReturn(null);
        Mockito.when(mockItem3.getCategory()).thenReturn(null);
        Mockito.when(mockItem3.getItemPriceSource()).thenReturn(null);
        Mockito.when(mockItem3.getOleRequestSourceType()).thenReturn(new OleRequestSourceType());
        Mockito.when(mockItem3.isItemRouteToRequestorIndicator()).thenReturn(true);
        Mockito.when(mockItem3.isItemRouteToRequestorIndicator()).thenReturn(true);
        Mockito.when(mockItem3.isItemPublicViewIndicator()).thenReturn(true);
        Mockito.when(mockItem3.getItemQuantity()).thenReturn(new KualiDecimal(2.0));
        Mockito.when(mockItem3.getItemListPrice()).thenReturn(new KualiDecimal(2.0));
        Mockito.when(mockItem3.getItemNoOfParts()).thenReturn(new KualiInteger(12));
        items.add(mockItem3);

        Mockito.when(mockPurchaseOrderDocument.getItems()).thenReturn(items);
        Mockito.when(mockPurchaseOrderDocument.getDocumentNumber()).thenReturn("121");
        Mockito.when(mockPurchaseOrderDocument.getDocumentHeader()).thenReturn(mockDocumentHeader);
        Mockito.when(mockDocumentHeader.getDocumentDescription()).thenReturn("Document Description");
        Mockito.when(mockPurchaseOrderDocument.getPurapDocumentIdentifier()).thenReturn(12);
        Mockito.when(mockPurchaseOrderDocument.getDeliveryBuildingCode()).thenReturn("Building Code");

        ArrayList<String> poIds = new ArrayList<>();
        poIds.add("1");
        poIds.add("2");
        poIds.add("3");
        poIds.add("4");
        poIds.add("5");

        olePurchaseOrderBatchService.downloadCSV(poIds);


        //TODO: This particular piece of code is not testing the actual logic in the OLEPurchaseOrderBatchServiceImpl;
        //TODO: This code is just to ensure that the file written by the class is readable and correct.
        //TODO: Same logic has been copied into the actual class.

        List<Map> list = new ArrayList<>();
        File csvData = new File(fileName);
        CSVParser parser = CSVParser.parse(csvData, Charset.defaultCharset(), CSVFormat.EXCEL);
        Iterator<CSVRecord> iterator = parser.getRecords().iterator();
        CSVRecord headerData = iterator.next();
        while (iterator.hasNext()) {
            CSVRecord csvRecord = iterator.next();
            Map map = new HashMap();
            for(int x = 0; x < headerData.size(); x++){
                map.put(headerData.get(x), csvRecord.get(x));
            }
            list.add(map);
        }

        assertTrue(!list.isEmpty());
        for (Iterator<Map> mapIterator = list.iterator(); mapIterator.hasNext(); ) {
            Map map = mapIterator.next();
            for (Iterator iterator1 = map.keySet().iterator(); iterator1.hasNext(); ) {
                String key = (String) iterator1.next();
                System.out.println(key + " : " + map.get(key));
            }
        }

    }

    public Class getAccountingLineClass() {
        return InvoiceAccount.class;
    }

    class MockFileWriter extends FileWriter {
        @Override
        public Writer append(CharSequence csq) throws IOException {
            return super.append(csq);
        }

        public MockFileWriter(String fileName) throws IOException {
            super(fileName);
        }
    }

    class CustomBusinessObjectService implements BusinessObjectService {

        @Override
        public <T extends PersistableBusinessObject> T save(T t) {
            return null;
        }

        @Override
        public List<? extends PersistableBusinessObject> save(List<? extends PersistableBusinessObject> list) {
            return null;
        }

        @Override
        public PersistableBusinessObject linkAndSave(PersistableBusinessObject persistableBusinessObject) {
            return null;
        }

        @Override
        public List<? extends PersistableBusinessObject> linkAndSave(List<? extends PersistableBusinessObject> list) {
            return null;
        }

        @Override
        public <T extends BusinessObject> T findBySinglePrimaryKey(Class<T> aClass, Object o) {
            return null;
        }

        @Override
        public <T extends BusinessObject> T findByPrimaryKey(Class<T> aClass, Map<String, ?> map) {
            return (T) mockPurchaseOrderDocument;
        }

        @Override
        public PersistableBusinessObject retrieve(PersistableBusinessObject persistableBusinessObject) {
            return null;
        }

        @Override
        public <T extends BusinessObject> Collection<T> findAll(Class<T> aClass) {
            return null;
        }

        @Override
        public <T extends BusinessObject> Collection<T> findAllOrderBy(Class<T> aClass, String s, boolean b) {
            return null;
        }

        @Override
        public <T extends BusinessObject> Collection<T> findMatching(Class<T> aClass, Map<String, ?> map) {
            return null;
        }

        @Override
        public int countMatching(Class aClass, Map<String, ?> map) {
            return 0;
        }

        @Override
        public int countMatching(Class aClass, Map<String, ?> map, Map<String, ?> map1) {
            return 0;
        }

        @Override
        public <T extends BusinessObject> Collection<T> findMatchingOrderBy(Class<T> aClass, Map<String, ?> map, String s, boolean b) {
            return null;
        }

        @Override
        public void delete(PersistableBusinessObject persistableBusinessObject) {

        }

        @Override
        public void delete(List<? extends PersistableBusinessObject> list) {

        }

        @Override
        public void deleteMatching(Class aClass, Map<String, ?> map) {

        }

        @Override
        public BusinessObject getReferenceIfExists(BusinessObject businessObject, String s) {
            return null;
        }

        @Override
        public void linkUserFields(PersistableBusinessObject persistableBusinessObject) {

        }

        @Override
        public void linkUserFields(List<PersistableBusinessObject> list) {

        }

        @Override
        public PersistableBusinessObject manageReadOnly(PersistableBusinessObject persistableBusinessObject) {
            return null;
        }
    }

}