package org.kuali.ole.docstore.document.jcr;

import org.junit.*;
import org.kuali.ole.BaseTestCase;
import org.kuali.ole.RepositoryBrowser;
import org.kuali.ole.RepositoryManager;
import org.kuali.ole.docstore.OleDocStoreException;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.InstanceOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.document.DocumentManager;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Content;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.ingest.ResponseDocument;
import org.kuali.ole.docstore.service.BeanLocator;
import org.kuali.ole.docstore.transaction.TransactionManager;
import org.kuali.ole.pojo.OleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Pranitha J
 * Date: 21/2/13
 * Time: 12:49 PM
 * To change this template use File | Settings | File Templates.
 */
@Ignore
@Deprecated
public class JcrWorkInstanceDocumentManager_UT extends BaseTestCase {
    private static final Logger LOG = LoggerFactory
            .getLogger(JcrWorkInstanceDocumentManager_UT.class);

    Session session = null;
    InstanceOlemlRecordProcessor instProcessor = new InstanceOlemlRecordProcessor();


    @Override
    @Before
    public void setUp() throws Exception {
        if (null == session) {
            session = RepositoryManager.getRepositoryManager().getSession("chuntley", "ingestDoc");
        }
    }

    @After
    public void tearDown() throws Exception {
        if (session != null) {
            session.logout();
            session = null;
        }


    }

    @Test
    public void testHoldingsCallNumberValidations() throws Exception {
        testHoldingsCreateCallNumberValidations();
        testHoldingsUpdateCallNumberValidations();
    }

    @Test
    public void testItemCallNumberValidations() throws Exception {
        testItemCreateCallNumberValidations();
        testItemUpdateCallNumberValidations();
    }

    @Test
    public void testHoldingsShelvingOrderComputation() throws Exception {
        holdingsCreateShelvingOrderComputation();
    }

    @Test
    public void testHoldingsUpdateShelvingOrderComputation() throws Exception {
        holdingsUpdateShelvingOrderComputation();
    }

    @Test
    public void testItemShelvingOrderComputationWithHoldingsCallNumInfo() throws Exception {
        // only holdings has call number and shelving order information, but shelving order is computed for both holdings and item
        ingestNUpdateItemWithHolCallNumberInfo("E461 .C58", "LCC");
        ingestNUpdateItemWithHolCallNumberInfo("E461 .C59", "LCC");
        ingestNUpdateItemWithHolCallNumberInfo("E461 .C60", "LCC");
        ingestNUpdateItemWithHolCallNumberInfo("E461 .C61", "LCC");
        // including enumeration and chronology for item while ingesting Instance with holdings call number info.
        ingestItemWithHoldingsCallNumberInfo("E461 .C62", "LCC", "v.1", "1996");
        ingestItemWithHoldingsCallNumberInfo("E461 .C63", "LCC", "v.1", "1996");
        ingestItemWithHoldingsCallNumberInfo("E461 .C64", "LCC", "v.1", "1996");
        ingestItemWithHoldingsCallNumberInfo("E461 .C65", "LCC", "v.1", "1996");
    }

    private void ingestItemWithHoldingsCallNumberInfo(String callNum, String shelvingScheme, String eNum, String chronology) throws Exception {
        RequestDocument requestDocument = buildInstanceRequestDocumentWithCallNumber(callNum, shelvingScheme, null, eNum, chronology);
        ResponseDocument responseDocument = ingestTestData(requestDocument);

        String instanceId = responseDocument.getUuid();
        Instance instance = retriveInstanceData(instanceId);
        OleHoldings holdings = instance.getOleHoldings();
        org.kuali.ole.docstore.common.document.content.instance.Item item = instance.getItems().getItem().get(0);

        verifyHoldingsNItemShelvingScheme(holdings, item);
        Assert.assertNotSame("shelving order full value is same for both holdings and item ",
                holdings.getCallNumber().getShelvingOrder().getFullValue(), item.getCallNumber().getShelvingOrder().getFullValue());

    }

    private void verifyHoldingsNItemShelvingScheme(OleHoldings holdings, org.kuali.ole.docstore.common.document.content.instance.Item item) throws OleDocStoreException {
        Assert.assertNotNull(holdings.getCallNumber().getNumber());
        Assert.assertNotNull(holdings.getCallNumber().getShelvingScheme().getCodeValue());
        Assert.assertNotNull(holdings.getCallNumber().getShelvingOrder().getFullValue());
        String holShelOrder = holdings.getCallNumber().getShelvingOrder().getFullValue();
        LOG.debug("holShelOrder " + holShelOrder);

        Assert.assertNull(item.getCallNumber().getNumber());
        Assert.assertNull(item.getCallNumber().getShelvingScheme());
        Assert.assertNotNull(item.getCallNumber().getShelvingOrder().getFullValue());
        String itemShelOrder = item.getCallNumber().getShelvingOrder().getFullValue();
        LOG.debug("itemShelOrder " + itemShelOrder);
    }


    private void ingestNUpdateItemWithHolCallNumberInfo(String callNum, String shelvingScheme) throws Exception {
        cleanRepository("work", DocType.BIB.getDescription());
        cleanRepository("work", DocType.INSTANCE.getDescription());
        RequestDocument requestDocument = buildInstanceRequestDocumentWithCallNumber(callNum, shelvingScheme, null, null, null);
        ResponseDocument responseDocument = ingestTestData(requestDocument);

        String instanceId = responseDocument.getUuid();
        Instance instance = retriveInstanceData(instanceId);
        OleHoldings holdings = instance.getOleHoldings();
        org.kuali.ole.docstore.common.document.content.instance.Item item = instance.getItems().getItem().get(0);
        verifyHoldingsNItemShelvingScheme(holdings, item);
        Assert.assertEquals("shelving order full value is same for both holdings and item ",
                holdings.getCallNumber().getShelvingOrder().getFullValue(), item.getCallNumber().getShelvingOrder().getFullValue());

        // updating item with enumeration and chronology values
        RequestDocument itemReq = buildItemCheckinDocumentWithCallNumber(item, null, null, null, "v.1", "1996", null);
        org.kuali.ole.docstore.common.document.content.instance.Item updatedItem = updateItem(itemReq);
        String updatedShelOrder = updatedItem.getCallNumber().getShelvingOrder().getFullValue();
        Assert.assertNotNull(updatedShelOrder);
        LOG.debug("updatedShelOrder" + updatedShelOrder);
    }


    @Test
    public void testItemShelvingOrderComputation() throws Exception {

        RequestDocument requestDocument = buildInstanceItemIngestDocumentWithCallNumber(null, null, null, null, null, null, null, null, null);
        Instance instance = createInstance(requestDocument);

        itemCreateWithInstanceShelvingOrderComputation();
        itemCreateWithoutInstanceShelvingOrderComputation(instance.getInstanceIdentifier());
        itemUpdateShelvingOrderComputation();


    }

    @Test
    public void addMultipleItemsToInstanceNUpdateHoldings() throws Exception {

        String callNumber = "DK602.3 .P44 1996";
        String shelvingScheme = "LCC";

        RequestDocument requestDocument = buildInstanceItemIngestDocumentWithCallNumber(callNumber, shelvingScheme, null, null, null, null, null, null, null);
        //Instance created with one item.
        Instance instance = createInstance(requestDocument);
        String item1Uuid = instance.getItems().getItem().get(0).getItemIdentifier();
        String so1 = instance.getItems().getItem().get(0).getCallNumber().getShelvingOrder().getFullValue();

        // add another item to instance
        RequestDocument itemReqDoc1 = buildAddItemToInstance(instance.getInstanceIdentifier(), "QS 11 c815a 1930", "NML", null, "v.5", "2013", "cp.5");
        org.kuali.ole.docstore.common.document.content.instance.Item item = addItemToInstance(itemReqDoc1);
        String item2Uuid = item.getItemIdentifier();
        Assert.assertNotNull(item.getCallNumber().getShelvingOrder().getFullValue());
        String so2 = item.getCallNumber().getShelvingOrder().getFullValue();

        // add another item to instance
        RequestDocument itemReqDoc2 = buildAddItemToInstance(instance.getInstanceIdentifier(), "P145.6.T5 S321 1981", "LCC", null, "v.6", "2013", "cp.6");
        item = addItemToInstance(itemReqDoc2);
        String item3Uuid = item.getItemIdentifier();
        Assert.assertNotNull(item.getCallNumber().getShelvingOrder().getFullValue());
        String so3 = item.getCallNumber().getShelvingOrder().getFullValue();

        callNumber = "PQ145.6.T5 S4 1976";

        // update holdings call number info
        RequestDocument holdingsReqDoc1 = buildHoldingsCheckinDocumentWithCallNumber(instance.getOleHoldings(), callNumber, shelvingScheme, null);
        OleHoldings oleHoldings1 = updateHoldings(holdingsReqDoc1);

        // verify the holdings corresponding items call number and shelving scheme to empty and modify shelving order according to holdings.
        JcrWorkInstanceDocumentManager jcrWorkInstanceDocumentManager = (JcrWorkInstanceDocumentManager) BeanLocator.getDocumentManagerFactory().getDocumentManager(requestDocument);
        org.kuali.ole.docstore.common.document.content.instance.Item item1 = checkOutItem(item1Uuid, jcrWorkInstanceDocumentManager);
        Assert.assertNotNull(item1.getItemIdentifier());
        Assert.assertNotNull(item1.getCallNumber().getShelvingOrder().getFullValue());
        Assert.assertEquals("", item1.getCallNumber().getNumber());
        Assert.assertEquals("", item1.getCallNumber().getShelvingScheme().getCodeValue());
        Assert.assertNotSame(so1, item1.getCallNumber().getShelvingOrder().getFullValue());

        org.kuali.ole.docstore.common.document.content.instance.Item item2 = checkOutItem(item2Uuid, jcrWorkInstanceDocumentManager);
        Assert.assertNotNull(item2.getItemIdentifier());
        Assert.assertNotNull(item2.getCallNumber().getShelvingOrder().getFullValue());
        Assert.assertEquals("", item2.getCallNumber().getNumber());
        Assert.assertEquals("", item2.getCallNumber().getShelvingScheme().getCodeValue());
        Assert.assertNotSame(so2, item2.getCallNumber().getShelvingOrder().getFullValue());

        org.kuali.ole.docstore.common.document.content.instance.Item item3 = checkOutItem(item3Uuid, jcrWorkInstanceDocumentManager);
        Assert.assertNotNull(item3.getItemIdentifier());
        Assert.assertNotNull(item3.getCallNumber().getShelvingOrder().getFullValue());
        Assert.assertEquals("", item3.getCallNumber().getNumber());
        Assert.assertEquals("", item3.getCallNumber().getShelvingScheme().getCodeValue());
        Assert.assertNotSame(so3, item3.getCallNumber().getShelvingOrder().getFullValue());

    }


    private void testHoldingsCreateCallNumberValidations() throws Exception {

        // case 1 :     with call number, shelving scheme and shelving order  , validation will pass
        RequestDocument requestDocument = buildInstanceRequestDocumentWithCallNumber("DK602 .P44 1901", "LCC", "DK  0602.000000 P0.440000 ", null, null);
        ResponseDocument responseDocument = new ResponseDocument();
        responseDocument = ingestDocument(requestDocument);


        Assert.assertNotNull(responseDocument.getStatusMessage());
        //testHoldingsUpdateCallNumberValidations();


        // case 2 :     with out call number  , validation should pass.

        RequestDocument requestDocument1 = buildInstanceRequestDocumentWithCallNumber(null, "LCC", "DK  0602.000000 P0.440000 001901", null, null);


        ResponseDocument responseDocument1 = ingestDocument(requestDocument1);
        Assert.assertNotNull(responseDocument1.getStatusMessage());


        // case 3 :      with out call number and shelving scheme , validation should pass.

        RequestDocument requestDocument2 = buildInstanceRequestDocumentWithCallNumber(null, null, "LCC", null, null);

        ResponseDocument responseDocument2 = ingestDocument(requestDocument2);
        Assert.assertNotNull(responseDocument2.getStatusMessage());


        // case 4 :       with out call number, shelving scheme and shelving order , validation should pass.

        RequestDocument requestDocument3 = buildInstanceRequestDocumentWithCallNumber(null, null, null, null, null);
        ResponseDocument responseDocument3 = ingestDocument(requestDocument3);
        Assert.assertNotNull(responseDocument3.getStatusMessage());


        // case 5 :     with out call number and shelving order and with shelving scheme, validation should pass.

        RequestDocument requestDocument4 = buildInstanceRequestDocumentWithCallNumber(null, "LCC", null, null, null);

        ResponseDocument responseDocument4 = ingestDocument(requestDocument4);
        Assert.assertNotNull(responseDocument4.getStatusMessage());


        // case 6 :     with call number and with out shelving scheme, validation fails.

        RequestDocument requestDocument5 = buildInstanceRequestDocumentWithCallNumber("DK602 .P44 1901", null, null, null, null);

        ResponseDocument responseDocument5 = ingestDocument(requestDocument5);
        Assert.assertNotNull(responseDocument5.getStatusMessage());
        Assert.assertEquals(responseDocument5.getStatusMessage(), "Please enter valid shelving scheme value in call number information ");


        // case 7 :      with call number , shelving order and with out shelving scheme, validation fails.

        RequestDocument requestDocument6 = buildInstanceRequestDocumentWithCallNumber("DK602 .P44 1901", null, "shelvingOrderCode", null, null);

        ResponseDocument responseDocument6 = ingestDocument(requestDocument6);
        Assert.assertNotNull(responseDocument6.getStatusMessage());
        Assert.assertEquals(responseDocument6.getStatusMessage(), "Please enter valid shelving scheme value in call number information ");


        // case 8 :     with call number and shelving order , shelving scheme gets computed and validation should pass.

        RequestDocument requestDocument7 = buildInstanceRequestDocumentWithCallNumber("DK602 .P44 1901", "LCC", null, null, null);

        ResponseDocument responseDocument7 = ingestDocument(requestDocument7);
        Assert.assertNotNull(responseDocument7.getStatusMessage());


    }

    private void testHoldingsUpdateCallNumberValidations() throws Exception {
        RequestDocument instanceReqDoc = buildInstanceItemIngestDocumentWithCallNumber(null, null, null);
        Instance instance = createInstance(instanceReqDoc);
        org.kuali.ole.docstore.common.document.content.instance.Item item = instance.getItems().getItem().get(0);
        // Case 1 : No values

        RequestDocument holdingsReqDoc1 = buildHoldingsCheckinDocumentWithCallNumber(instance.getOleHoldings(), null, null, null);

        OleHoldings oleHoldings1 = updateHoldings(holdingsReqDoc1);

        Assert.assertNotNull(oleHoldings1.getCallNumber().getShelvingOrder());

        //Case 2 :  update holdings with different callNumber and/or shelvingScheme

        RequestDocument holdingsReqDoc2 = buildHoldingsCheckinDocumentWithCallNumber(oleHoldings1, "P145.6.T5 S321 1981", "LCC", null);

        OleHoldings oleHoldings2 = updateHoldings(holdingsReqDoc2);

        Assert.assertNotNull(oleHoldings2.getCallNumber().getShelvingOrder());
//        Need to verify item’s shelving order has changed

        // Case 3 : update holdings with same callNumber and shelvingScheme but different shelvingOrder

        RequestDocument holdingsReqDoc3 = buildHoldingsCheckinDocumentWithCallNumber(oleHoldings2, "P145.6.T5 S321 1981", "LCC", "so1");

        OleHoldings oleHoldings3 = updateHoldings(holdingsReqDoc3);

        Assert.assertNotSame(oleHoldings2.getCallNumber().getShelvingOrder(), oleHoldings3.getCallNumber().getShelvingOrder());
//          Need to verify item’s shelving order has changed

    }


    private void testItemCreateCallNumberValidations() throws Exception {


        // case 1 :     buildInstanceRequestDocumentWithCallNumber(callNumber, shelvingScheme, shelvingOrder)
        RequestDocument requestDocument = buildRequestDocumentWithCallNumberForItem("DK602 .P44 1901", "LCC", "DK  0602.300000 P0.440000 001996");


        ResponseDocument responseDocument = ingestDocument(requestDocument);
        Assert.assertNotNull(responseDocument.getStatusMessage());


        // case 2 :     buildInstanceRequestDocumentWithCallNumber(null, shelvingScheme, shelvingOrder)

        RequestDocument requestDocument1 = buildRequestDocumentWithCallNumberForItem(null, "LCC", "DK  0602.300000 P0.440000 001996");


        ResponseDocument responseDocument1 = ingestDocument(requestDocument1);
        Assert.assertNotNull(responseDocument1.getStatusMessage());

        // case 3 :     buildInstanceRequestDocumentWithCallNumber(null, null, shelvingOrder)

        RequestDocument requestDocument2 = buildRequestDocumentWithCallNumberForItem(null, null, "DK  0602.300000 P0.440000 001996");

        ResponseDocument responseDocument2 = ingestDocument(requestDocument2);
        Assert.assertNotNull(responseDocument2.getStatusMessage());


        // case 4 :     buildInstanceRequestDocumentWithCallNumber(null, null, null)

        RequestDocument requestDocument3 = buildRequestDocumentWithCallNumberForItem(null, null, null);
        ResponseDocument responseDocument3 = ingestDocument(requestDocument3);
        Assert.assertNotNull(responseDocument3.getStatusMessage());


        // case 5 :     buildInstanceRequestDocumentWithCallNumber(null, shelvingScheme, null)

        RequestDocument requestDocument4 = buildRequestDocumentWithCallNumberForItem(null, "LCC", null);

        ResponseDocument responseDocument4 = ingestDocument(requestDocument4);
        Assert.assertNotNull(responseDocument4.getStatusMessage());

        // case 6 :     buildInstanceRequestDocumentWithCallNumber(callNumber, null, null)

        RequestDocument requestDocument5 = buildRequestDocumentWithCallNumberForItem("DK602 .P44 1901", null, null);

        ResponseDocument responseDocument5 = ingestDocument(requestDocument5);
        Assert.assertNotNull(responseDocument5.getStatusMessage());
        Assert.assertEquals(responseDocument5.getStatusMessage(), "Please enter valid shelving scheme value in call number information ");

        // case 7 :     buildInstanceRequestDocumentWithCallNumber(callNumber, null, shelvingOrder)

        RequestDocument requestDocument6 = buildRequestDocumentWithCallNumberForItem("DK602 .P44 1901", null, "DK  0602.300000 P0.440000 001996");

        ResponseDocument responseDocument6 = ingestDocument(requestDocument6);
        Assert.assertNotNull(responseDocument6.getStatusMessage());
        Assert.assertEquals(responseDocument6.getStatusMessage(), "Please enter valid shelving scheme value in call number information ");


        // case 8 :     buildInstanceRequestDocumentWithCallNumber(callNumber, shelvingScheme, null)

        RequestDocument requestDocument7 = buildRequestDocumentWithCallNumberForItem("DK602 .P44 1901", "LCC", null);

        ResponseDocument responseDocument7 = ingestDocument(requestDocument7);
        Assert.assertNotNull(responseDocument7.getStatusMessage());


    }


    private void testItemUpdateCallNumberValidations() throws Exception {
        RequestDocument instanceReqDoc = buildInstanceItemIngestDocumentWithCallNumber(null, null, null);
        Instance instance = createInstance(instanceReqDoc);
        org.kuali.ole.docstore.common.document.content.instance.Item item = instance.getItems().getItem().get(0);
        // Case 1 : No values

        RequestDocument itemReqDoc1 = buildItemCheckinDocumentWithCallNumber(item, null, null, null, null, null, null);

        org.kuali.ole.docstore.common.document.content.instance.Item item1 = updateItem(itemReqDoc1);

        Assert.assertNotNull(item1.getCallNumber().getShelvingOrder());

        //Case 2 :  update holdings with different callNumber and/or shelvingScheme

        RequestDocument itemReqDoc2 = buildItemCheckinDocumentWithCallNumber(item1, "PQ145.T51 S4", "LCC", null, null, null, null);

        org.kuali.ole.docstore.common.document.content.instance.Item item2 = updateItem(itemReqDoc2);

        Assert.assertNotNull(item2.getCallNumber().getShelvingOrder().getFullValue());
//        Need to verify item’s shelving order has changed

        // Case 3 : update holdings with same callNumber and shelvingScheme but different shelvingOrder

        RequestDocument itemReqDoc3 = buildItemCheckinDocumentWithCallNumber(item2, "PQ145.T51 S4", "LCC", "DK  0602.300000 P0.440000 001996", null, null, null);

        org.kuali.ole.docstore.common.document.content.instance.Item item3 = updateItem(itemReqDoc3);

        Assert.assertNotSame(item2.getCallNumber().getShelvingOrder(), item3.getCallNumber().getShelvingOrder());
//          Need to verify item’s shelving order has changed
    }


    /**
     * Builds a requestDocument for WorkInstance with only holdings call number information.
     *
     * @param callNumber
     * @param shelvingScheme
     * @param shelvingOrder
     * @param eNum
     * @param chronology
     * @return
     * @throws RepositoryException
     * @throws OleException
     * @throws FileNotFoundException
     * @throws OleDocStoreException
     */
    private RequestDocument buildInstanceRequestDocumentWithCallNumber(String callNumber, String shelvingScheme, String shelvingOrder, String eNum, String chronology) throws Exception {

        List<Instance> instanceList = new ArrayList<Instance>();
        OleHoldings oleHoldings = setHoldingCallNumberInformation(callNumber, shelvingScheme, shelvingOrder);

        Instance instance = new Instance();
        instance.setOleHoldings(oleHoldings);

        org.kuali.ole.docstore.common.document.content.instance.Item item = new org.kuali.ole.docstore.common.document.content.instance.Item();
        item.setEnumeration(eNum);
        item.setChronology(chronology);
        Items items = new Items();
        items.setItem(Arrays.asList(item));
        instance.setItems(items);
        instanceList.add(instance);

        InstanceCollection instanceCollection = new InstanceCollection();
        instanceCollection.setInstance(instanceList);
        InstanceOlemlRecordProcessor instProcessor = new InstanceOlemlRecordProcessor();
        String instanceContent = instProcessor.toXML(instanceCollection);
        Content content = new Content();
        content.setContent(instanceContent);
        RequestDocument requestDocument = new RequestDocument();
        requestDocument.setCategory(DocCategory.WORK.getCode());
        requestDocument.setType(DocType.INSTANCE.getCode());
        requestDocument.setFormat(DocFormat.OLEML.getCode());
        requestDocument.setContent(content);
        requestDocument.setUuid(UUID.randomUUID().toString());
        return requestDocument;

    }

    /**
     * @param callNumber
     * @param shelvingScheme
     * @param shelvingOrder
     * @return
     * @throws RepositoryException
     * @throws OleException
     * @throws FileNotFoundException
     * @throws OleDocStoreException
     */
    private RequestDocument buildRequestDocumentWithCallNumberForItem(String callNumber, String shelvingScheme, String shelvingOrder) throws Exception {

        List<Instance> instanceList = new ArrayList<Instance>();
        org.kuali.ole.docstore.common.document.content.instance.Item item = new org.kuali.ole.docstore.common.document.content.instance.Item();
        Items items = new Items();
        List<org.kuali.ole.docstore.common.document.content.instance.Item> itemList = new ArrayList<org.kuali.ole.docstore.common.document.content.instance.Item>();

        CallNumber callNumber1 = new CallNumber();
        callNumber1.setNumber(callNumber);

        ShelvingScheme shelvingScheme1 = new ShelvingScheme();
        shelvingScheme1.setCodeValue(shelvingScheme);

        ShelvingOrder shelvingOrder1 = new ShelvingOrder();
        shelvingOrder1.setFullValue(shelvingOrder);

        callNumber1.setShelvingScheme(shelvingScheme1);
        callNumber1.setShelvingOrder(shelvingOrder1);
        item.setCallNumber(callNumber1);

        Instance instance = new Instance();
        instance.setOleHoldings(setHoldingCallNumberInformation(null, null, null));
        itemList.add(item);
        items.setItem(itemList);
        instance.setItems(items);
        instanceList.add(instance);

        InstanceCollection instanceCollection = new InstanceCollection();
        instanceCollection.setInstance(instanceList);
        InstanceOlemlRecordProcessor instProcessor = new InstanceOlemlRecordProcessor();
        String instanceContent = instProcessor.toXML(instanceCollection);
        Content content = new Content();
        content.setContent(instanceContent);
        RequestDocument requestDocument = new RequestDocument();
        requestDocument.setCategory(DocCategory.WORK.getCode());
        requestDocument.setType(DocType.INSTANCE.getCode());
        requestDocument.setFormat(DocFormat.OLEML.getCode());
        requestDocument.setContent(content);
//        requestDocument.setUuid(UUID.randomUUID().toString());

        return requestDocument;

    }

    /**
     * @param requestDocument
     * @return
     * @throws OleDocStoreException
     * @throws RepositoryException
     * @throws ReferentialIntegrityException
     * @throws ItemExistsException
     */
    private ResponseDocument ingestDocument(RequestDocument requestDocument) throws Exception {

        TransactionManager transactionManager = BeanLocator.getDocstoreFactory().getTransactionManager(requestDocument.getCategory(), requestDocument.getType(), requestDocument.getFormat());
        List<RequestDocument> requestDocumentList = new ArrayList<RequestDocument>();
        requestDocumentList.add(requestDocument);
        transactionManager.startTransaction("testUser", "ingest");

        ResponseDocument respDoc = new ResponseDocument();
        try {
            List<ResponseDocument> responseDocumentList = transactionManager.ingest(requestDocumentList);
            transactionManager.commit();
            respDoc = responseDocumentList.get(0);
        } catch (Exception e) {
            respDoc.setStatusMessage(e.getMessage());
        }

        return respDoc;
    }

    /**
     * This methods updates the holdings information and returns Holdings Object
     *
     * @param holdingsReqDoc
     * @return
     * @throws OleDocStoreException
     */
    private OleHoldings updateHoldings(RequestDocument holdingsReqDoc) throws Exception {

        JcrWorkInstanceDocumentManager jcrWorkInstanceDocumentManager = (JcrWorkInstanceDocumentManager) BeanLocator.getDocumentManagerFactory().getDocumentManager(holdingsReqDoc);

        ResponseDocument checkInResponseDocument = checkInRequestDocument(holdingsReqDoc);

        RequestDocument requestDocument = new RequestDocument();
        requestDocument.setCategory(DocCategory.WORK.getCode());
        requestDocument.setType(DocType.HOLDINGS.getCode());
        requestDocument.setFormat(DocFormat.OLEML.getCode());
        requestDocument.setUuid(checkInResponseDocument.getUuid());
        ResponseDocument responseDocument = jcrWorkInstanceDocumentManager.checkout(requestDocument, session);
        HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
        OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(responseDocument.getContent().getContent());
        return oleHoldings;
    }

    private ResponseDocument checkInRequestDocument(RequestDocument holdingsReqDoc) throws Exception {
        TransactionManager transactionManager = BeanLocator.getDocstoreFactory().getTransactionManager(holdingsReqDoc.getCategory(), holdingsReqDoc.getType(), holdingsReqDoc.getFormat());
        List<RequestDocument> requestDocumentList = new ArrayList<RequestDocument>();
        requestDocumentList.add(holdingsReqDoc);
        transactionManager.startTransaction("testUser", "checkIn");
        List<ResponseDocument> responseDocumentList = transactionManager.checkIn(requestDocumentList);
        transactionManager.commit();
        return responseDocumentList.get(0);
    }

    /**
     * @param instanceReqDoc
     * @return
     * @throws OleDocStoreException
     * @throws RepositoryException
     * @throws OleException
     */
    private Instance createInstance(RequestDocument instanceReqDoc) throws Exception {
        RepositoryBrowser repoBro = new RepositoryBrowser();
        ResponseDocument respDoc = ingestDocument(instanceReqDoc);
        Instance instance = retriveInstanceData(respDoc.getUuid());
        return instance;
    }

    public void cleanRepository(String category, String type) throws OleException, RepositoryException {
        RepositoryManager repositoryManager = RepositoryManager.getRepositoryManager();
        Session session = repositoryManager.getSession("mockUser", "test");
        Node rootNode = session.getRootNode();
        Node catNode = rootNode.getNode(category);
        Node typeNode = catNode.getNode(type);
        for (Iterator<Node> typeiterator = typeNode.getNodes(); typeiterator.hasNext(); ) {
            Node formatNode = typeiterator.next();
            if (!formatNode.getName().equals("jcr:system")) {
                formatNode.remove();
            }
        }
        session.save();
        repositoryManager.logout(session);
    }

    /**
     * Builds a requestDocument for WorkInstance with only item call number information.
     *
     * @param callNumber
     * @param shelvingScheme
     * @param shelvingOrder
     * @return
     * @throws OleDocStoreException
     */
    private RequestDocument buildInstanceItemIngestDocumentWithCallNumber(String callNumber, String shelvingScheme, String shelvingOrder) {
        Items items = new Items();
        List<org.kuali.ole.docstore.common.document.content.instance.Item> itemList = new ArrayList<org.kuali.ole.docstore.common.document.content.instance.Item>();

        Instance instance = new Instance();
        List<Instance> instanceList = new ArrayList<Instance>();
        org.kuali.ole.docstore.common.document.content.instance.Item item = new org.kuali.ole.docstore.common.document.content.instance.Item();
        CallNumber callNumber1 = new CallNumber();
//        if (callNumber != null && callNumber.length() > 0) {
        callNumber1.setNumber(callNumber);

        ShelvingScheme shelvingScheme1 = new ShelvingScheme();
        shelvingScheme1.setCodeValue(shelvingScheme);

        ShelvingOrder shelvingOrder1 = new ShelvingOrder();
        shelvingOrder1.setFullValue(shelvingOrder);

        callNumber1.setShelvingScheme(shelvingScheme1);
        callNumber1.setShelvingOrder(shelvingOrder1);

//        }

        item.setCallNumber(callNumber1);
        itemList.add(item);
        items.setItem(itemList);
        instance.setItems(items);
        instance.setOleHoldings(new OleHoldings());
        instanceList.add(instance);

        InstanceCollection instanceCollection = new InstanceCollection();
        instanceCollection.setInstance(instanceList);
        InstanceOlemlRecordProcessor instProcessor = new InstanceOlemlRecordProcessor();
        String instanceContent = instProcessor.toXML(instanceCollection);
        Content content = new Content();
        content.setContent(instanceContent);
        RequestDocument requestDocument = new RequestDocument();
        requestDocument.setCategory(DocCategory.WORK.getCode());
        requestDocument.setType(DocType.INSTANCE.getCode());
        requestDocument.setFormat(DocFormat.OLEML.getCode());
        requestDocument.setContent(content);
        return requestDocument;
    }


    /**
     * @param instanceId
     * @return
     * @throws OleDocStoreException
     */
    private Instance retriveInstanceData(String instanceId) throws OleDocStoreException {

        RequestDocument requestDocument = new RequestDocument();
        requestDocument.setCategory(DocCategory.WORK.getCode());
        requestDocument.setType(DocType.INSTANCE.getCode());
        requestDocument.setFormat(DocFormat.OLEML.getCode());
        requestDocument.setUuid(instanceId);

        JcrWorkInstanceDocumentManager jcrWorkInstanceDocumentManager = (JcrWorkInstanceDocumentManager) BeanLocator.getDocumentManagerFactory().getDocumentManager(requestDocument);
        ResponseDocument checkOutResponseDocument = jcrWorkInstanceDocumentManager.checkout(requestDocument, session);

        InstanceOlemlRecordProcessor instProcessor = new InstanceOlemlRecordProcessor();
        InstanceCollection instanceCollection = instProcessor.fromXML(checkOutResponseDocument.getContent().getContent());
        return instanceCollection.getInstance().get(0);
    }


    /**
     * Builds the requestDocument for updating the callNumber information in Holdings.
     *
     * @param oleHoldings
     * @param callNumber
     * @param shelvingScheme
     * @param shelvingOrderCode
     * @return
     * @throws OleDocStoreException
     */
    private RequestDocument buildHoldingsCheckinDocumentWithCallNumber(OleHoldings oleHoldings, String callNumber, String shelvingScheme, String shelvingOrderCode) throws OleDocStoreException {

        RequestDocument requestDocument = new RequestDocument();
        requestDocument.setId(oleHoldings.getHoldingsIdentifier());
        requestDocument.setCategory(DocCategory.WORK.getCode());
        requestDocument.setType(DocType.HOLDINGS.getCode());
        requestDocument.setFormat(DocFormat.OLEML.getCode());

        CallNumber cn = oleHoldings.getCallNumber();
        if (cn == null) {
            cn = new CallNumber();
            cn.setNumber(callNumber);

            ShelvingScheme shelvingScheme1 = new ShelvingScheme();
            shelvingScheme1.setCodeValue(shelvingScheme);

            ShelvingOrder shelvingOrder = new ShelvingOrder();
            shelvingOrder.setFullValue(shelvingOrderCode);

            cn.setShelvingScheme(shelvingScheme1);
            cn.setShelvingOrder(shelvingOrder);

            oleHoldings.setCallNumber(cn);
        } else {
            oleHoldings.getCallNumber().setNumber(callNumber);
            oleHoldings.getCallNumber().getShelvingScheme().setCodeValue(shelvingScheme);
            oleHoldings.getCallNumber().getShelvingOrder().setFullValue(shelvingOrderCode);
        }

        Content content = new Content();
        content.setContent(instProcessor.toXML(oleHoldings));
        requestDocument.setContent(content);

        return requestDocument;
    }


    /**
     * @param item
     * @param callNumber
     * @param shelvingScheme
     * @param shelvingOrderCode
     * @param enumeration
     * @param chronology
     * @param copyNumber
     * @return
     */
    private RequestDocument buildItemCheckinDocumentWithCallNumber(org.kuali.ole.docstore.common.document.content.instance.Item item, String callNumber, String shelvingScheme, String shelvingOrderCode, String enumeration, String chronology, String copyNumber) {
        RequestDocument requestDocument = new RequestDocument();
        requestDocument.setId(item.getItemIdentifier());
        requestDocument.setUuid(item.getItemIdentifier());
        requestDocument.setCategory(DocCategory.WORK.getCode());
        requestDocument.setType(DocType.ITEM.getCode());
        requestDocument.setFormat(DocFormat.OLEML.getCode());

        CallNumber cn = item.getCallNumber();
        if (cn == null) {
            item.setCallNumber(new CallNumber());
        }
        if (item.getCallNumber().getShelvingScheme() == null) {
            item.getCallNumber().setShelvingScheme(new ShelvingScheme());
        }
        if (item.getCallNumber().getShelvingOrder() == null) {
            item.getCallNumber().setShelvingOrder(new ShelvingOrder());
        }

        item.getCallNumber().setNumber(callNumber);
        item.getCallNumber().getShelvingScheme().setCodeValue(shelvingScheme);
        item.getCallNumber().getShelvingOrder().setFullValue(shelvingOrderCode);

        item.setChronology(chronology);
        item.setCopyNumber(copyNumber);
        item.setEnumeration(enumeration);

        Content content = new Content();
        content.setContent(instProcessor.toXML(item));
        requestDocument.setContent(content);
        requestDocument.setOperation(Request.Operation.checkIn.toString());
        return requestDocument;
    }

    /**
     * @param requestDocument
     * @return
     * @throws OleDocStoreException
     */
    private org.kuali.ole.docstore.common.document.content.instance.Item updateItem(RequestDocument requestDocument) throws Exception {
        JcrWorkInstanceDocumentManager jcrWorkInstanceDocumentManager = (JcrWorkInstanceDocumentManager) BeanLocator.getDocumentManagerFactory().getDocumentManager(requestDocument);
//        ResponseDocument checkInResponseDocument =  new ResponseDocument();
        ResponseDocument checkInResponseDocument = checkInRequestDocument(requestDocument);
//        jcrWorkInstanceDocumentManager.checkin(requestDocument, session,checkInResponseDocument);
        org.kuali.ole.docstore.common.document.content.instance.Item item = checkOutItem(checkInResponseDocument.getUuid(), jcrWorkInstanceDocumentManager);

        return item;
    }

    private org.kuali.ole.docstore.common.document.content.instance.Item checkOutItem(String uuid, JcrWorkInstanceDocumentManager jcrWorkInstanceDocumentManager) throws OleDocStoreException {
        RequestDocument requestDocument1 = new RequestDocument();
        requestDocument1.setCategory(DocCategory.WORK.getCode());
        requestDocument1.setType(DocType.ITEM.getCode());
        requestDocument1.setFormat(DocFormat.OLEML.getCode());
        requestDocument1.setUuid(uuid);
        ResponseDocument responseDocument = jcrWorkInstanceDocumentManager.checkout(requestDocument1, session);
        ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
        org.kuali.ole.docstore.common.document.content.instance.Item item = itemOlemlRecordProcessor.fromXML(responseDocument.getContent().getContent());
        return item;
    }


    private void holdingsCreateShelvingOrderComputation() throws Exception {

        // Case 1:  No values for callNumber, shelvingScheme and shelvingOrder

        RequestDocument requestDocument1 = buildInstanceRequestDocumentWithCallNumber(null, null, null, null, null);
        ResponseDocument responseDocument1 = ingestTestData(requestDocument1);

        String instanceId1 = responseDocument1.getUuid();

        Instance instance = retriveInstanceData(instanceId1);
        OleHoldings oleHoldings1 = instance.getOleHoldings();
        String holdingsId1 = oleHoldings1.getHoldingsIdentifier();

        CallNumber oleHoldings1CallNumber = oleHoldings1.getCallNumber();
        ShelvingOrder shelvingOrder = null;
        if (oleHoldings1CallNumber != null) {
            shelvingOrder = oleHoldings1.getCallNumber().getShelvingOrder();
            Assert.assertNull(shelvingOrder.getFullValue());
        }


        //Case 2: callNumber and shelvingScheme present, no shelvingOrder
        String callNumber = "DK602 .P44 1901";
        String shelvingScheme = "LCC";
        String shelvingOrderCode3 = "DK  0602.300000 P0.440000 001996";

        RequestDocument requestDocument2 = buildInstanceRequestDocumentWithCallNumber(callNumber, shelvingScheme, null, null, null);
        ResponseDocument responseDocument2 = ingestTestData(requestDocument2);
        String instanceId2 = responseDocument2.getUuid();

        instance = retriveInstanceData(instanceId2);

        OleHoldings oleHoldings2 = instance.getOleHoldings();
        String holdingsId2 = oleHoldings2.getHoldingsIdentifier();

        CallNumber oleHoldings2CallNumber = oleHoldings2.getCallNumber();

        Assert.assertNotNull(oleHoldings2CallNumber.getShelvingOrder());

        Items items = instance.getItems();
        Assert.assertNotNull(items);
        Assert.assertEquals(items.getItem().size(), 1);
        org.kuali.ole.docstore.common.document.content.instance.Item item = items.getItem().get(0);
        Assert.assertNotNull(item.getItemIdentifier());
        Assert.assertNull(item.getCallNumber().getNumber());
        Assert.assertNull(item.getCallNumber().getShelvingScheme());
        Assert.assertNotNull(item.getCallNumber().getShelvingOrder());

        //Case 3 : all three values are present.

        RequestDocument requestDocument3 = buildInstanceRequestDocumentWithCallNumber(callNumber, shelvingScheme, shelvingOrderCode3, null, null);
        ResponseDocument responseDocument3 = ingestTestData(requestDocument3);
        String instanceId3 = requestDocument3.getUuid();

        instance = retriveInstanceData(instanceId3);

        OleHoldings oleHoldings3 = instance.getOleHoldings();

        String holdingsId3 = oleHoldings3.getHoldingsIdentifier();
        CallNumber oleHoldings3CallNumber = oleHoldings3.getCallNumber();

        ShelvingOrder shelvingOrder3 = oleHoldings3CallNumber.getShelvingOrder();

        Assert.assertTrue(!shelvingOrderCode3.equalsIgnoreCase(shelvingOrder3.getFullValue()));

    }

    private void holdingsUpdateShelvingOrderComputation() throws Exception {

        String callNumber = "PQ145.T51 S4";
        String shelvingScheme = "LCC";
        String shelvingOrder = "PQ  0000.000000";
        RequestDocument instanceReqDoc = buildInstanceItemIngestDocumentWithCallNumber(null, null, null);
        Instance instance = createInstance(instanceReqDoc);
        org.kuali.ole.docstore.common.document.content.instance.Item item = instance.getItems().getItem().get(0);
        String itemId = item.getItemIdentifier();
        String itemShelvingOrder = item.getCallNumber().getShelvingOrder().getFullValue();
        // Case 1 : No values

        RequestDocument holdingsReqDoc1 = buildHoldingsCheckinDocumentWithCallNumber(instance.getOleHoldings(), null, null, null);

        OleHoldings oleHoldings1 = updateHoldings(holdingsReqDoc1);

        Assert.assertNotNull(oleHoldings1.getCallNumber().getShelvingOrder());

        //Case 2 :  update holdings with different callNumber and/or shelvingScheme

        RequestDocument holdingsReqDoc2 = buildHoldingsCheckinDocumentWithCallNumber(oleHoldings1, callNumber, shelvingScheme, null);

        OleHoldings oleHoldings2 = updateHoldings(holdingsReqDoc2);

        Assert.assertNotNull(oleHoldings2.getCallNumber().getShelvingOrder());
        Assert.assertNotSame(oleHoldings1.getCallNumber().getShelvingOrder(), oleHoldings2.getCallNumber().getShelvingOrder());
//        Need to verify item’s shelving order has changed
        item = retriveItemData(itemId);
        Assert.assertNotSame(itemShelvingOrder, item.getCallNumber().getShelvingOrder().getFullValue());

        // Case 3 : update holdings with same callNumber and shelvingScheme but different shelvingOrder

        RequestDocument holdingsReqDoc3 = buildHoldingsCheckinDocumentWithCallNumber(oleHoldings2, callNumber, shelvingScheme, shelvingOrder);

        OleHoldings oleHoldings3 = updateHoldings(holdingsReqDoc3);

        Assert.assertNotSame(oleHoldings2.getCallNumber().getShelvingOrder(), oleHoldings3.getCallNumber().getShelvingOrder());
//          Need to verify item’s shelving order has changed

    }

    private org.kuali.ole.docstore.common.document.content.instance.Item retriveItemData(String itemId) throws OleDocStoreException {
        RequestDocument requestDocument = new RequestDocument();
        requestDocument.setCategory(DocCategory.WORK.getCode());
        requestDocument.setType(DocType.ITEM.getCode());
        requestDocument.setFormat(DocFormat.OLEML.getCode());
        requestDocument.setUuid(itemId);

        DocumentManager documentManager = BeanLocator.getDocumentManagerFactory().getDocumentManager(requestDocument);
        ResponseDocument checkOutResponseDocument = documentManager.checkout(requestDocument, session);
        ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
        org.kuali.ole.docstore.common.document.content.instance.Item item = itemOlemlRecordProcessor.fromXML(checkOutResponseDocument.getContent().getContent());
        return item;
    }

    private ResponseDocument ingestTestData(RequestDocument requestDocument) throws Exception {

       /* DocumentManager documentManager =  BeanLocator.getDocumentManagerFactory().getDocumentManager(requestDocument);

        ResponseDocument responseDocument = new ResponseDocument();
                documentManager.ingest(requestDocument, session, responseDocument);*/
        ResponseDocument responseDocument = ingestDocument(requestDocument);

        return responseDocument;
    }


    private void itemUpdateShelvingOrderComputation() throws Exception {
        RequestDocument instanceReqDoc1 = buildInstanceItemIngestDocumentWithCallNumber(null, null, null, null, null, null, null, null, null);

        Instance instance = createInstance(instanceReqDoc1);
        org.kuali.ole.docstore.common.document.content.instance.Item item = instance.getItems().getItem().get(0);
        String itemShelvingOrder = item.getCallNumber().getShelvingOrder().getFullValue();

        // Case 1 :  Update item with callNumber and shelvingScheme
        String callNumber = "PQ145.T51 S4";
        String shelvingScheme = "LCC";
        String shelvingOrder = "PQ  0000.000000";

        String enumeration = "v.1";
        String chronology = "1984";
        String copyNumber = "cp.2";


        RequestDocument requestDocument = buildItemCheckinDocumentWithCallNumber(item, callNumber, shelvingScheme, null, null, null, null);
        org.kuali.ole.docstore.common.document.content.instance.Item updateditem = updateItem(requestDocument);

        Assert.assertNotNull(updateditem.getCallNumber().getShelvingOrder().getFullValue());

        // Case 2 : Update item with shelvingOrder
        RequestDocument itemReq2 = buildItemCheckinDocumentWithCallNumber(updateditem, callNumber, shelvingScheme, shelvingOrder, null, null, null);
        updateditem = updateItem(itemReq2);
        Assert.assertNotNull(updateditem.getCallNumber().getShelvingOrder().getFullValue());
        Assert.assertEquals(shelvingOrder, updateditem.getCallNumber().getShelvingOrder().getFullValue());

        //Case 3 : Update item with enum, chrono, copy


        RequestDocument itemReq3 = buildItemCheckinDocumentWithCallNumber(updateditem, null, null, null, enumeration, chronology, copyNumber);
        updateditem = updateItem(itemReq3);
        Assert.assertNull(updateditem.getCallNumber().getShelvingOrder().getFullValue());
        Assert.assertSame(itemShelvingOrder, updateditem.getCallNumber().getShelvingOrder().getFullValue());


        //Case 4 : Update item with enum, chrono, copy and callnumber , shelving scheme
        RequestDocument itemReq4 = buildItemCheckinDocumentWithCallNumber(updateditem, callNumber, shelvingScheme, null, enumeration, chronology, copyNumber);
        updateditem = updateItem(itemReq4);
        Assert.assertNotNull(updateditem.getCallNumber().getShelvingOrder().getFullValue());
    }


    private void itemCreateWithoutInstanceShelvingOrderComputation(String instanceId) throws OleDocStoreException {

        String callNumber = "PQ145.T51 S4";
        String shelvingScheme = "LCC";
        String shelvingOrder3 = "PQ  0000.000000";

        String enumeration = "v.1";
        String chronology = "1984";
        String copyNumber = "cp.2";

        //Case 1:  no call number info for holdings and item
        RequestDocument itemReqDoc1 = buildAddItemToInstance(instanceId, null, null, null, null, null, null);
        org.kuali.ole.docstore.common.document.content.instance.Item item = addItemToInstance(itemReqDoc1);
        Assert.assertNull(item.getCallNumber().getShelvingOrder().getFullValue());

        //Case 2: call number info present and shelving order empty for item only

        RequestDocument itemReqDoc2 = buildAddItemToInstance(instanceId, callNumber, shelvingScheme, null, null, null, null);
        item = addItemToInstance(itemReqDoc2);
        Assert.assertNotNull(item.getCallNumber().getShelvingOrder().getFullValue());

        //Case 3 : call number info present and shelving order present for item only

        RequestDocument itemReqDoc3 = buildAddItemToInstance(instanceId, callNumber, shelvingScheme, shelvingOrder3, null, null, null);
        item = addItemToInstance(itemReqDoc3);
        Assert.assertNotNull(item.getCallNumber().getShelvingOrder().getFullValue());
        Assert.assertNotSame(shelvingOrder3, item.getCallNumber().getShelvingOrder().getFullValue());

        // Case 4 : call number info present and Enumeration present  for item only

        RequestDocument itemReqDoc4 = buildAddItemToInstance(instanceId, callNumber, shelvingScheme, null, enumeration, null, null);
        item = addItemToInstance(itemReqDoc4);
        Assert.assertNotNull(item.getCallNumber().getShelvingOrder().getFullValue());
        Assert.assertNotSame(shelvingOrder3, item.getCallNumber().getShelvingOrder().getFullValue());

        // Case 5 : call number info present and Enumeration and chronology present  for item only

        RequestDocument itemReqDoc5 = buildAddItemToInstance(instanceId, callNumber, shelvingScheme, shelvingOrder3, enumeration, chronology, null);
        item = addItemToInstance(itemReqDoc5);
        Assert.assertNotNull(item.getCallNumber().getShelvingOrder().getFullValue());
        Assert.assertNotSame(shelvingOrder3, item.getCallNumber().getShelvingOrder().getFullValue());

        // Case 6 : call number info present and Enumeration, chronology and copy number present for item .

        RequestDocument itemReqDoc6 = buildAddItemToInstance(instanceId, callNumber, shelvingScheme, null, enumeration, chronology, copyNumber);
        item = addItemToInstance(itemReqDoc6);
        Assert.assertNotNull(item.getCallNumber().getShelvingOrder().getFullValue());
        Assert.assertNotSame(shelvingOrder3, item.getCallNumber().getShelvingOrder().getFullValue());

    }

    private void itemCreateWithInstanceShelvingOrderComputation() throws Exception {

        String callNumber = "PQ145.T51 S4";
        String shelvingScheme = "LCC";
        String shelvingOrder3 = "PQ  0000.000000";

        String enumeration = "v.1";
        String chronology = "1984";
        String copyNumber = "cp.2";

      /*  //Case 1 : no call number info for holdings and item
        RequestDocument instanceReqDoc1 = buildInstanceItemIngestDocumentWithCallNumber(null, null, null, null, null, null,null, null, null);

        Instance instance1 = createInstance(instanceReqDoc1);
        Item item1 = instance1.getItems().getItem().get(0);

        Assert.assertNull(item1.getCallNumber().getShelvingOrder().getFullValue());

        // Case 2 : call number info present and shelving order empty for item only

        RequestDocument instanceReqDoc2 = buildInstanceItemIngestDocumentWithCallNumber(null, null, null, callNumber, shelvingScheme, null, null, null, null);

        Instance instance2 = createInstance(instanceReqDoc2);
        Item item2 = instance2.getItems().getItem().get(0);
        Assert.assertNotNull(item2.getCallNumber().getShelvingOrder().getFullValue());
        Assert.assertNotSame(shelvingOrder3, item2.getCallNumber().getShelvingOrder().getFullValue());

        // Case 3 : call number info present and shelving order present for item only

        RequestDocument instanceReqDoc3 = buildInstanceItemIngestDocumentWithCallNumber(null, null, null, callNumber, shelvingScheme, shelvingOrder3, null, null, null);

        Instance instance3 = createInstance(instanceReqDoc3);
        Item item3 = instance3.getItems().getItem().get(0);
        Assert.assertNotSame(shelvingOrder3, item3.getCallNumber().getShelvingOrder().getFullValue());
        Assert.assertNotSame(shelvingOrder3, item3.getCallNumber().getShelvingOrder().getFullValue());*/

        // Case 4 : call number info present and Enumeration present  for item only

        RequestDocument instanceReqDoc4 = buildInstanceItemIngestDocumentWithCallNumber(null, null, null, callNumber, shelvingScheme, shelvingOrder3, enumeration, null, null);

        Instance instance4 = createInstance(instanceReqDoc4);
        org.kuali.ole.docstore.common.document.content.instance.Item item4 = instance4.getItems().getItem().get(0);
        Assert.assertNotNull(item4.getCallNumber().getShelvingOrder().getFullValue());
        Assert.assertNotSame(shelvingOrder3, item4.getCallNumber().getShelvingOrder().getFullValue());


        // Case 5 : call number info present and Enumeration and chronology present  for item only

        RequestDocument instanceReqDoc5 = buildInstanceItemIngestDocumentWithCallNumber(null, null, null, callNumber, shelvingScheme, shelvingOrder3, enumeration, chronology, null);

        Instance instance5 = createInstance(instanceReqDoc5);
        org.kuali.ole.docstore.common.document.content.instance.Item item5 = instance5.getItems().getItem().get(0);
        Assert.assertNotNull(item5.getCallNumber().getShelvingOrder().getFullValue());
        Assert.assertNotSame(shelvingOrder3, item5.getCallNumber().getShelvingOrder().getFullValue());

        // Case 6 : call number info present and Enumeration, chronology and copy number present  for item .

        RequestDocument instanceReqDoc6 = buildInstanceItemIngestDocumentWithCallNumber(null, null, null, callNumber, shelvingScheme, null, enumeration, chronology, copyNumber);

        Instance instance6 = createInstance(instanceReqDoc6);
        org.kuali.ole.docstore.common.document.content.instance.Item item6 = instance6.getItems().getItem().get(0);
        Assert.assertNotNull(item6.getCallNumber().getShelvingOrder().getFullValue());
        Assert.assertNotSame(shelvingOrder3, item6.getCallNumber().getShelvingOrder().getFullValue());

    }


    /**
     * Builds the instance request document with holdings and item information.
     *
     * @param holdingsCallNumber
     * @param holdingsShelvingScheme
     * @param holdingsShelvingOrder
     * @param itemCallNumber
     * @param itemShelvingScheme
     * @param itemShelvingOrder
     * @return
     */
    private RequestDocument buildInstanceItemIngestDocumentWithCallNumber(String holdingsCallNumber, String holdingsShelvingScheme, String holdingsShelvingOrder,
                                                                          String itemCallNumber, String itemShelvingScheme, String itemShelvingOrder, String itemEnumeration,
                                                                          String itemChronology, String itemCopyNumber) {

        InstanceCollection instanceCollection = new InstanceCollection();
        List<Instance> instanceList = new ArrayList<Instance>();
        Instance instance = new Instance();

        OleHoldings oleHoldings = setHoldingCallNumberInformation(holdingsCallNumber, holdingsShelvingScheme, holdingsShelvingOrder);
        instance.setOleHoldings(oleHoldings);

        org.kuali.ole.docstore.common.document.content.instance.Item item = setItemCallNumberInformation(itemCallNumber, itemShelvingScheme, itemShelvingOrder, itemEnumeration, itemChronology, itemCopyNumber);

        List<org.kuali.ole.docstore.common.document.content.instance.Item> itemList = new ArrayList<org.kuali.ole.docstore.common.document.content.instance.Item>();
        itemList.add(item);
        Items items = new Items();
        items.setItem(itemList);

        instance.setItems(items);
        instanceList.add(instance);
        instanceCollection.setInstance(instanceList);

        InstanceOlemlRecordProcessor instProcessor = new InstanceOlemlRecordProcessor();
        String instanceContent = instProcessor.toXML(instanceCollection);

        Content content = new Content();
        content.setContent(instanceContent);
        RequestDocument requestDocument = new RequestDocument();
        requestDocument.setCategory(DocCategory.WORK.getCode());
        requestDocument.setType(DocType.INSTANCE.getCode());
        requestDocument.setFormat(DocFormat.OLEML.getCode());
        requestDocument.setContent(content);

        return requestDocument;
    }

    /**
     * Creates the holdings pojo with the CallNumber information.
     *
     * @param callNumber
     * @param shelvingScheme
     * @param shelvingOrder
     * @return
     */
    private OleHoldings setHoldingCallNumberInformation(String callNumber, String shelvingScheme, String shelvingOrder) {

        OleHoldings oleHoldings = new OleHoldings();

        CallNumber callNumber1 = new CallNumber();
        callNumber1.setNumber(callNumber);

        ShelvingScheme shelvingScheme1 = new ShelvingScheme();
        shelvingScheme1.setCodeValue(shelvingScheme);

        ShelvingOrder shelvingOrder1 = new ShelvingOrder();
        shelvingOrder1.setFullValue(shelvingOrder);

        callNumber1.setShelvingScheme(shelvingScheme1);
        callNumber1.setShelvingOrder(shelvingOrder1);

        oleHoldings.setCallNumber(callNumber1);

        return oleHoldings;
    }

    /**
     * Creates the item pojo with CallNumber Information.
     *
     * @param callNumber
     * @param shelvingScheme
     * @param shelvingOrder
     * @return
     */
    private org.kuali.ole.docstore.common.document.content.instance.Item setItemCallNumberInformation(String callNumber, String shelvingScheme, String shelvingOrder,
                                              String enumeration, String chronology, String copyNumber) {

        org.kuali.ole.docstore.common.document.content.instance.Item item = new org.kuali.ole.docstore.common.document.content.instance.Item();

        CallNumber callNumber1 = new CallNumber();
        callNumber1.setNumber(callNumber);

        ShelvingScheme shelvingScheme1 = new ShelvingScheme();
        shelvingScheme1.setCodeValue(shelvingScheme);

        ShelvingOrder shelvingOrder1 = new ShelvingOrder();
        shelvingOrder1.setFullValue(shelvingOrder);

        callNumber1.setShelvingScheme(shelvingScheme1);
        callNumber1.setShelvingOrder(shelvingOrder1);

        item.setCallNumber(callNumber1);
        if (enumeration != null) {
            item.setEnumeration(enumeration);
        }
        if (chronology != null) {
            item.setChronology(chronology);
        }
        if (copyNumber != null) {
            item.setCopyNumber(copyNumber);
        }

        return item;
    }

    /**
     * Builds requestDocument to add Item with callNumber Information to existing Instance.
     *
     * @param instanceId
     * @param callNumber
     * @param shelvingScheme
     * @param shelvingOrder
     * @return
     */
    private RequestDocument buildAddItemToInstance(String instanceId, String callNumber, String shelvingScheme, String shelvingOrder, String enumeration, String chronology, String copyNumber) {

        RequestDocument requestDocument = new RequestDocument();
        requestDocument.setCategory(DocCategory.WORK.getCode());
        requestDocument.setType(DocType.INSTANCE.getCode());
        requestDocument.setFormat(DocFormat.OLEML.getCode());
        requestDocument.setId(instanceId);

        List<RequestDocument> requestDocumentList = new ArrayList<RequestDocument>();

        RequestDocument linkedRequestDocument = new RequestDocument();
        linkedRequestDocument.setCategory(DocCategory.WORK.getCode());
        linkedRequestDocument.setType(DocType.ITEM.getCode());
        linkedRequestDocument.setFormat(DocFormat.OLEML.getCode());

        org.kuali.ole.docstore.common.document.content.instance.Item item = setItemCallNumberInformation(callNumber, shelvingScheme, shelvingOrder, enumeration, chronology, copyNumber);
        Content content = new Content();
        content.setContent(instProcessor.toXML(item));
        linkedRequestDocument.setContent(content);

        requestDocumentList.add(linkedRequestDocument);
        requestDocument.setLinkedRequestDocuments(requestDocumentList);
        requestDocument.setOperation(Request.Operation.checkIn.toString());

        Request request = new Request();
        List<RequestDocument> requestDocuments = new ArrayList<RequestDocument>();
        requestDocuments.add(requestDocument);
        request.setRequestDocuments(requestDocuments);

        return requestDocument;

    }

    /**
     * Adds the Item to the Instance.
     *
     * @param requestDocument
     * @return
     * @throws OleDocStoreException
     */
    private org.kuali.ole.docstore.common.document.content.instance.Item addItemToInstance(RequestDocument requestDocument) throws OleDocStoreException {

        DocumentManager documentManager = JcrDocumentManagerFactory.getInstance().getDocumentManager(requestDocument);
        ResponseDocument responseDocument = new ResponseDocument();
        documentManager.checkin(requestDocument, session, responseDocument);

        String itemId = responseDocument.getLinkedDocuments().get(0).getUuid();

        org.kuali.ole.docstore.common.document.content.instance.Item item = retriveItemData(itemId);

        return item;
    }


}
