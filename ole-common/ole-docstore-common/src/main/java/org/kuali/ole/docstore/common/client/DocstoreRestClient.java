package org.kuali.ole.docstore.common.client;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.Header;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.util.EntityUtils;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecords;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;

import org.kuali.ole.docstore.common.exception.DocstoreExceptionProcessor;
import org.kuali.ole.docstore.common.find.FindParams;
import org.kuali.ole.docstore.common.search.BrowseParams;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * Created with IntelliJ IDEA.
 * User: jayabharathreddy
 * Date: 12/17/13
 * Time: 5:04 PM
 * To change this template use File | Settings | File Templates.
 */


public class DocstoreRestClient implements DocstoreClient {

    //    static String DOCSTORE_URL = "http://localhost:8080/oledocstore/documentrest/";
    private static String DOCSTORE_URL = ConfigContext.getCurrentContextConfig().getProperty("ole.docstore.Documentrest.url");
    private static String BIB_URL = "bib/doc/";
    private static String RELOAD_URL = "config/reload";
    private static String BIB_CONTENT_URL = "bib/";
    private static String SEARCH_URL = "search/";
    private static String HOLDINGS_URL = "holdings/doc/";
    private static String HOLDINGS_DOCS_URL = "holdings/docs/";
    private static String ITEMS_URL = "item/doc/";
    private static String ITEMS_DOCS_URL = "item/docs/";
    private static String ITEM_MAP_URL = "item/doc/map";
    private static String ITEM_CONTENT_URL = "item/";
    private static String HOLDINGS_TREE_URL = "holdings/doc/tree/";
    private static String HOLDINGS_TREES_URL = "holdings/doc/trees/";
    private static String HOLDINGS_TREES_CONTENT_URL = "holdings/tree/";
    private static String BIB_TREE_URL = "bib/doc/tree/";
    private static String BIB_TREES_URL = "bib/doc/trees/";
    private static String FIND_URL = "/doc/find";
    private static String BROWSE_URL = "browse/";
    private static String BOUND_URL = "/bound";
    private static String ANALYTIC_URL = "/analytic";
    private static String BREAK_ANALYTIC_URL = "/breakAnalytic";
    private static String TRANSFER_URL = "/transfer/";
    private static String LICENSES_URL = "license/";
    private static String LICENSES_TREES_URL = "license/trees/";
    private static String BULK_UPDATE = "/bulkUpdate";
    private static String PROCESS_BIB_TREES = "bib/process";

    private Logger logger = LoggerFactory.getLogger(DocstoreRestClient.class);

    @Override
    public void createBib(Bib bib) {
        String requestBody = bib.serialize(bib);
        RestResponse restResponse = postRequest(requestBody, BIB_URL);
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            }
            String[] responseString = restResponse.getResponseBody().split("/");
            if (responseString.length == 4) {
                bib.setId(responseString[3]);
            }
        }
    }

    @Override
    public void createHoldings(Holdings holdings) {
        String requestBody = holdings.serialize(holdings);
        RestResponse restResponse = postRequest(requestBody, HOLDINGS_URL);
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            }
            String[] responseString = restResponse.getResponseBody().split("/");
            if (responseString.length == 4) {
                holdings.setId(responseString[3]);
            }
        }
    }

    @Override
    public void createItem(Item item) {
        String requestBody = item.serialize(item);
        RestResponse restResponse = postRequest(requestBody, ITEMS_URL);
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            }
            String[] responseString = restResponse.getResponseBody().split("/");
            if (responseString.length == 4) {
                item.setId(responseString[3]);
            }
        }
    }

    @Override
    public void createHoldingsTree(HoldingsTree holdingsTree) {
        String requestBody = holdingsTree.serialize(holdingsTree);
        RestResponse restResponse = postRequest(requestBody, HOLDINGS_TREE_URL);
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            }
            String[] responseString = restResponse.getResponseBody().split("/");
            holdingsTree.getHoldings().setId(responseString[4]);
            if (responseString.length > 4) {
                int j = 0;
                for (int i = 5; i < responseString.length; i++) {
                    if (responseString[i] != null) {
                        holdingsTree.getItems().get(j).setId(responseString[i]);
                    }
                    j++;
                }
            }
        }
    }



    @Override
    public void createBibTree(BibTree bibTree) {
        String requestBody = bibTree.serialize(bibTree);
        RestResponse restResponse = postRequest(requestBody, BIB_TREE_URL);
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            }
            String[] responseString = restResponse.getResponseBody().split("/");
            if (responseString.length == 5) {
                bibTree.getBib().setId(responseString[4]);
            }
        }
    }



    @Override
    public BibTrees processBibTrees(BibTrees bibTrees) {
        String requestBody = bibTrees.serialize(bibTrees);
        RestResponse restResponse = postRequest(requestBody, PROCESS_BIB_TREES);
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            } else {
                bibTrees = (BibTrees) BibTrees.deserialize(restResponse.getResponseBody());
            }
        }
        return bibTrees;
    }

    @Override
    public List<Bib> retrieveBibs(List<String> bibIds) {
//        String bibIdSb = buildIds(bibIds, "bibIds=");
        RestResponse restResponse = getBibResponse(buildQueryString(bibIds, "bibId"), BIB_URL);
        Bibs bibsObj = new Bibs();
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            } else {
                if (bibIds.size() == 1) {
                    Bib bib = new Bib();
                    bibsObj.getBibs().add((Bib) bib.deserialize(restResponse.getResponseBody()));
                    return bibsObj.getBibs();
                }
                bibsObj = (Bibs) Bibs.deserialize(restResponse.getResponseBody());

            }
        }
        return bibsObj.getBibs();

    }

    @Override
    public List<Item> retrieveItems(List<String> itemIds) {
        RestResponse restResponse = getRequest(buildQueryString(itemIds, "itemId"), ITEMS_URL);
        Items itemsObj = new Items();
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            } else {
                itemsObj = (Items) Items.deserialize(restResponse.getResponseBody());
            }
        }
        return itemsObj.getItems();
    }

    @Override
    public HashMap<String, Item> retrieveItemMap(List<String> itemIds) {
        RestResponse restResponse = getRequest(buildQueryString(itemIds, "itemId"), ITEM_MAP_URL);
        ItemMap itemsObj = new ItemMap();
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            } else {
                itemsObj = (ItemMap) ItemMap.deserialize(restResponse.getResponseBody());
            }
        }
        return itemsObj.getItemMap();
    }

    @Override
    public void createLicenses(Licenses licenses) {

        File bagitFile = null;
        try {
            bagitFile = createBagItfile(licenses);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        RestResponse restResponse = postMultiPartRequest(bagitFile);
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            }
            String[] responseString = restResponse.getResponseBody().split("/");
            int idPos = 2;
            for (License license : licenses.getLicenses()) {
                license.setId(responseString[idPos]);
                idPos += 1;
            }
        }
        bagitFile.delete();
    }

    @Override
    public License retrieveLicense(String id) {
        RestResponse restResponse = getRequest(id, LICENSES_URL);
        License license = new License();
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                logger.info(" rest response " + restResponse.getResponseBody());
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            } else {
                license = (License) license.deserialize(restResponse.getResponseBody());
            }
        }
        return license;

    }

    private RestResponse postMultiPartRequest(File bagitFile) {
        HttpClient httpclient = new DefaultHttpClient();
        FileBody uploadFilePart = new FileBody(bagitFile);
        MultipartEntity reqEntity = new MultipartEntity();
        reqEntity.addPart("upload-file", uploadFilePart);
        HttpPost httpPost = new HttpPost(DOCSTORE_URL + LICENSES_URL);
        httpPost.setEntity(reqEntity);
        httpPost.addHeader("multipart/form-data", "text/xml");
        RestResponse restResponse = new RestResponse();
        try {
            EntityUtils.consume(reqEntity);
            HttpResponse response = httpclient.execute(httpPost);
            restResponse.setResponse(response);
            restResponse.setResponseBody(getEncodeEntityValue(response.getEntity()));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return restResponse;
    }

    private File createBagItfile(Licenses licenses) throws IOException {
        File bagitFolder = new File(FileUtils.getTempDirectoryPath() + File.separator + "bagit");
        String licensesXml = licenses.serialize(licenses);
        File licensesXmlFile = new File(bagitFolder + File.separator + "licenses.xml");
        FileUtils.writeStringToFile(licensesXmlFile, licensesXml);
        for (License license : licenses.getLicenses()) {
            if (license instanceof LicenseAttachment) {
                LicenseAttachment licenseAttachment = (LicenseAttachment) license;
                File contentFile = new File(licenseAttachment.getFilePath() + File.separator + licenseAttachment.getFileName());
                FileUtils.copyFileToDirectory(contentFile, bagitFolder);
            }
        }

        File bagitFile = createZipFile(bagitFolder);
        deleteFiles(bagitFile.listFiles());
        bagitFolder.delete();
        return bagitFile;
    }

    @Override
    public Licenses retrieveLicenses(List<String> ids) {
        String licenseIds = buildIds(ids, "licenseIds=");
        RestResponse restResponse = getRequest(licenseIds, LICENSES_URL);
        Licenses licenses = new Licenses();
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            } else {
                licenses = (Licenses) Licenses.deserialize(restResponse.getResponseBody());
            }
        }
        return licenses;
    }

    @Override
    public void updateLicense(License license) {
        String requestBody = license.serialize(license);
        RestResponse restResponse = putRequest(requestBody, LICENSES_URL);
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            }
        }
    }

    @Override
    public void updateLicenses(Licenses licenses) {
        String requestBody = Licenses.serialize(licenses);
        RestResponse restResponse = putRequest(requestBody, LICENSES_TREES_URL);
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            }
        }

    }

    @Override
    public void deleteLicense(String id) {
        RestResponse restResponse = deleteRequest(id, LICENSES_URL);
        if (restResponse.getResponse().getStatusLine().getStatusCode() != 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            }
        }

    }

    @Override
    public void createAnalyticsRelation(String seriesHoldingsId, List<String> itemIds) {
        //To change body of implemented methods use File | Settings | File Templates.
        String requestBody = buildIds(itemIds, "");
        RestResponse restResponse = postRequest(requestBody, HOLDINGS_URL + seriesHoldingsId + ANALYTIC_URL);
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            }
        }
    }

    @Override
    public void bulkUpdateHoldings(Holdings holdings, List<String> holdingIds, String canUpdateStaffOnlyFlag) {
        String requestBody = holdings.serialize(holdings);
        String holdingUpdateIds = buildIds(holdingIds, "");
        RestResponse restResponse = putRequest(holdingUpdateIds + "\n" + canUpdateStaffOnlyFlag + "\n" + requestBody, HOLDINGS_URL + "/" + BULK_UPDATE);
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            }
        }
    }

    @Override
    public void bulkUpdateItem(Item item, List<String> itemIds, String canUpdateStaffOnlyFlag) {
        String requestBody = item.serialize(item);
        String itemUpdateIds = buildIds(itemIds, "");
        RestResponse restResponse = putRequest(itemUpdateIds + "\n" + canUpdateStaffOnlyFlag + "\n" + requestBody, ITEMS_URL + "/" + BULK_UPDATE);
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            }
        }
    }

    @Override
    public void breakAnalyticsRelation(String seriesHoldingsId, List<String> itemIds) {
        String requestBody = buildIds(itemIds, "");
        RestResponse restResponse = postRequest(requestBody, HOLDINGS_URL + seriesHoldingsId + BREAK_ANALYTIC_URL);
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            }
        }
    }

    @Override
    public Bib retrieveBib(String bibId) {
        String reqParam = "?bibId=" + bibId;
        RestResponse restResponse = getBibResponse(reqParam, BIB_URL);
        Bib bib = new Bib();
        bib = (Bib) bib.deserialize(restResponse.getResponseBody());
        return bib;
    }

    @Override
    public BibMarcRecords retrieveBibContent(List<String> bibIds) {
        RestResponse restResponse = getBibResponse(buildQueryString(bibIds, "bibId"), BIB_CONTENT_URL);
        BibMarcRecords bibMarcRecords = new BibMarcRecords();
        BibMarcRecordProcessor bibMarcRecordProcessor = new BibMarcRecordProcessor();
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            } else {
                bibMarcRecords = bibMarcRecordProcessor.fromXML(restResponse.getResponseBody());
            }
        }
        return bibMarcRecords;
    }

    @Override
    public HoldingsTrees retrieveHoldingsTrees(List<String> bibIds) {
        RestResponse restResponse = getRequest(buildQueryString(bibIds, "bibId"), HOLDINGS_TREES_CONTENT_URL);
        HoldingsTrees holdingsTrees = new HoldingsTrees();
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            } else {
                holdingsTrees = (HoldingsTrees) holdingsTrees.deserialize(restResponse.getResponseBody());
            }
        }
        return holdingsTrees;
    }

    @Override
    public HoldingsTrees retrieveHoldingsDocTrees(List<String> bibIds) {
        RestResponse restResponse = getRequest(buildQueryString(bibIds, "bibId"), HOLDINGS_TREES_URL);
        HoldingsTrees holdingsTrees = new HoldingsTrees();
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            } else {
                holdingsTrees = (HoldingsTrees) holdingsTrees.deserialize(restResponse.getResponseBody());
            }
        }
        return holdingsTrees;
    }

    @Override
    public Item retrieveItemByBarcode(String barcode) {
        RestResponse restResponse = getRequest("?barcode=" + barcode, ITEM_CONTENT_URL);
        Item item = new Item();
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            } else {
                item = (Item) item.deserialize(restResponse.getResponseBody());
            }
        }
        return item;
    }

    @Override
    public void reloadConfiguration() {
        RestResponse restResponse = getRequest("", RELOAD_URL);
    }

    @Override
    public Holdings retrieveHoldings(String holdingsId) {
        String reqParam = "?holdingsId=" + holdingsId;
        RestResponse restResponse = getRequest(reqParam, HOLDINGS_URL);
        Holdings holdings = new PHoldings();
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            holdings = (Holdings) holdings.deserialize(restResponse.getResponseBody());
            if (!holdings.getHoldingsType().equalsIgnoreCase("print")) {
                holdings = new EHoldings();
                if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                    throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
                } else {
                    holdings = (EHoldings) holdings.deserialize(restResponse.getResponseBody());
                }
            }
        }
        return holdings;
    }

    @Override
    public Item retrieveItem(String itemId) {
        String queryParam = "?itemId=" + itemId;
        RestResponse restResponse = getRequest(queryParam, ITEMS_URL);
        Item item = new Item();
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            } else {
                item = (Item) item.deserialize(restResponse.getResponseBody());
            }
        }
        return item;
    }

    @Override
    public HoldingsTree retrieveHoldingsTree(String holdingsId) {
        HoldingsTree holdingsTree = new HoldingsTree();
        String reqParam = "?holdingsId=" + holdingsId;
        RestResponse restResponse = getRequest(reqParam, HOLDINGS_TREE_URL);
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            } else {
                holdingsTree = (HoldingsTree) holdingsTree.deserialize(restResponse.getResponseBody());
            }
        }
        return holdingsTree;
    }

    @Override
    public BibTree retrieveBibTree(String bibId) {
        BibTree bibTree = new BibTree();
        String reqParam = "?bibId=" + bibId;
        RestResponse restResponse = getBibResponse(reqParam, BIB_TREE_URL);
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            } else {
                bibTree = (BibTree) bibTree.deserialize(restResponse.getResponseBody());
            }
        }
        return bibTree;
    }

    @Override
    public BibTrees retrieveBibTrees(List<String> bibIds) {
        BibTrees bibTrees = new BibTrees();

        RestResponse restResponse = getBibResponse(buildQueryString(bibIds, "bibId"), BIB_TREES_URL);
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            } else {
                bibTrees = (BibTrees) BibTrees.deserialize(restResponse.getResponseBody());
            }
        }

        return bibTrees;
    }

    @Override
    public Bib updateBib(Bib bib) {
        String requestBody = bib.serialize(bib);
        RestResponse restResponse = putRequest(requestBody, BIB_URL);
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            }
        }
        return bib;
    }

    @Override
    public Holdings updateHoldings(Holdings holdings) {
        String requestBody = holdings.serialize(holdings);
        RestResponse restResponse = putRequest(requestBody, HOLDINGS_URL);
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            }
        }
        return holdings;
    }

    @Override
    public Item updateItem(Item item) {
        String requestBody = item.serialize(item);
        RestResponse restResponse = putRequest(requestBody, ITEMS_URL);
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            }
        }
        return item;
    }

    @Override
    public String updateItemByBarcode(String barcode, String requestBody) {
        RestResponse restResponse = patchRequest(requestBody, ITEM_CONTENT_URL + "?barcode=" + barcode);
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            }
        }
        return restResponse.getResponseBody();
    }

    @Override
    public String patchItem(String requestBody) {
        RestResponse restResponse = patchRequest(requestBody, ITEM_CONTENT_URL);
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            }
        }
        return restResponse.getResponseBody();
    }

    @Override
    public void deleteBib(String bibId) {
        String reqParam = "?bibId=" + bibId;
        RestResponse restResponse = deleteRequest(reqParam, BIB_URL);
        if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
            throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
        }
    }

    @Override
    public void deleteHoldings(String holdingsId) {
        String reqParam = "?holdingsId=" + holdingsId;
        RestResponse restResponse = deleteRequest(reqParam, HOLDINGS_URL);
        if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
            throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
        }
    }

    @Override
    public void deleteItem(String itemId) {
        String queryString = "?itemId=" + itemId;
        RestResponse restResponse = deleteRequest(queryString, ITEMS_URL);
        if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
            throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
        }
    }

    @Override
    public void deleteItems(List<String> itemIds) {
        String queryString = buildIds(itemIds, "?itemId=");
        RestResponse restResponse = deleteRequest(queryString, ITEMS_DOCS_URL);
        if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
            throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
        }
    }

    @Override
    public SearchResponse search(SearchParams searchParams) {
        String requestBody = searchParams.serialize(searchParams);
        RestResponse restResponse = postRequest(requestBody, SEARCH_URL);
        SearchResponse searchResponse = new SearchResponse();
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                logger.info("DocstoreRestClient search : " + restResponse.getResponseBody());
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            } else {
                searchResponse = (SearchResponse) searchResponse.deserialize(restResponse.getResponseBody());
            }
        }
        return searchResponse;
    }

    @Override
    public Bib findBib(Map<String, String> map) {
        Bib bib = new Bib();
        FindParams findParams = buildFindParams(map);
        String requestBody = findParams.serialize(findParams);
        RestResponse restResponse = postRequest(requestBody, BIB_URL + FIND_URL);
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            } else {
                bib = (Bib) bib.deserialize(restResponse.getResponseBody());
            }
        }
        return bib;
    }

    @Override
    public BibTree findBibTree(Map<String, String> map) {
        BibTree bibTree = new BibTree();
        FindParams findParams = buildFindParams(map);
        String requestBody = findParams.serialize(findParams);
        RestResponse restResponse = postRequest(requestBody, BIB_TREE_URL + FIND_URL);
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            } else {
                bibTree = (BibTree) bibTree.deserialize(restResponse.getResponseBody());
            }
        }
        return bibTree;
    }

    @Override
    public Holdings findHoldings(Map<String, String> map) {
        Holdings holdings = new PHoldings();
        FindParams findParams = buildFindParams(map);
        String requestBody = findParams.serialize(findParams);
        RestResponse restResponse = postRequest(requestBody, HOLDINGS_URL + FIND_URL);
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            } else {
                holdings = (Holdings) holdings.deserialize(restResponse.getResponseBody());
            }
        }
        return holdings;
    }

    @Override
    public HoldingsTree findHoldingsTree(Map<String, String> map) {
        HoldingsTree holdingsTree = new HoldingsTree();
        FindParams findParams = buildFindParams(map);
        String requestBody = findParams.serialize(findParams);
        RestResponse restResponse = postRequest(requestBody, HOLDINGS_TREE_URL + FIND_URL);
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            } else {
                holdingsTree = (HoldingsTree) holdingsTree.deserialize(restResponse.getResponseBody());
            }
        }
        return holdingsTree;
    }

    @Override
    public Item findItem(Map<String, String> map) {
        Item item = new Item();
        FindParams findParams = buildFindParams(map);
        String requestBody = findParams.serialize(findParams);
        RestResponse restResponse = postRequest(requestBody, ITEMS_URL + FIND_URL);
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            } else {
                item = (Item) item.deserialize(restResponse.getResponseBody());
            }
        }
        return item;
    }

    @Override
    public SearchResponse browseItems(BrowseParams browseParams) {
        String requestBody = browseParams.serialize(browseParams);
        RestResponse restResponse = postRequest(requestBody, BROWSE_URL + ITEMS_URL);
        SearchResponse searchResponse = new SearchResponse();
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            } else {
                searchResponse = (SearchResponse) searchResponse.deserialize(restResponse.getResponseBody());
            }
        }
        return searchResponse;
    }

    @Override
    public SearchResponse browseHoldings(BrowseParams browseParams) {
        String requestBody = browseParams.serialize(browseParams);
        RestResponse restResponse = postRequest(requestBody, BROWSE_URL + HOLDINGS_URL);
        SearchResponse searchResponse = new SearchResponse();
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            } else {
                searchResponse = (SearchResponse) searchResponse.deserialize(restResponse.getResponseBody());
            }
        }
        return searchResponse;
    }

    @Override
    public void boundWithBibs(String holdingsId, List<String> bibIds) {
        String requestBody = buildIds(bibIds, "");
        RestResponse restResponse = postRequest(requestBody, HOLDINGS_URL + holdingsId + BOUND_URL);
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            }
        }
    }

    @Override
    public void transferHoldings(List<String> holdingsIds, String bibId) {

        RestResponse restResponse = postRequest(" ", BIB_URL + bibId + TRANSFER_URL + buildQueryString(holdingsIds, "holdingsId"));
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            }
        }
    }

    @Override
    public void transferItems(List<String> itemIds, String holdingsId) {
        String reqParam = buildQueryString(itemIds, "itemId");
        RestResponse restResponse = postRequest(" ", HOLDINGS_URL + holdingsId + TRANSFER_URL + reqParam);
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            }
        }
    }

    @Override
    public void deleteBibs(List<String> bibIds) {
        RestResponse restResponse = deleteRequest(buildQueryString(bibIds, "bibId"), BIB_URL);
        //if (restResponse.getResponse().getStatusLine().getStatusCode() != 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            }
        //}
    }


    /////////////////////////////////// Utility Methods  /////////////////////


    private String buildQueryString(List<String> ids, String queryParam) {
        StringBuilder reqParam = new StringBuilder("?");
        int size = ids.size();
        for (int i = 0; i < size; i++) {
            reqParam.append(queryParam);
            reqParam.append("=");
            reqParam.append(ids.get(i));
            if (i != (size - 1)) {
                reqParam.append("&");
            }
        }
        return reqParam.toString();
    }

    private String buildQueryPostString(List<String> ids, String queryParam) {
        StringBuilder reqParam = new StringBuilder("");
        int size = ids.size();
        for (int i = 0; i < size; i++) {
            reqParam.append(queryParam);
            reqParam.append("=");
            reqParam.append(ids.get(i));
            if (i != (size - 1)) {
                reqParam.append("&");
            }
        }
        return reqParam.toString();
    }

    public String buildIds(List<String> ids, String value) {
        StringBuilder idSb = new StringBuilder(value);
        for (String id : ids) {
            if (ids.get(ids.size() - 1).equals(id)) {
                idSb.append(id);
            } else {
                idSb.append(id + ",");
            }
        }
        return idSb.toString();
    }

    public FindParams buildFindParams(Map<String, String> map) {
        FindParams findParams = new FindParams();
        FindParams.Map findMap = new FindParams.Map();
        for (Map.Entry mapEntry : map.entrySet()) {
            FindParams.Map.Entry entry = new FindParams.Map.Entry();
            entry.setKey((String) mapEntry.getKey());
            entry.setValue((String) mapEntry.getValue());
            findMap.getEntry().add(entry);
        }
        findParams.setMap(findMap);
        return findParams;
    }

    public RestResponse patchRequest(String requestBody, String param) {
        HttpClient client = new DefaultHttpClient();
        String result = new String();
        RestResponse response = new RestResponse();
        HttpPatch patch = new HttpPatch(DOCSTORE_URL + param);
        try {
            StringEntity stringEntity = new StringEntity(requestBody, "UTF-8");
            patch.setEntity(stringEntity);
            response.setResponse(client.execute(patch));
            result = getEncodeEntityValue(response.getResponse().getEntity());
        } catch (Exception e) {
            logger.error("PATCH response error is ::", e);
        }
        response.setResponseBody(result);
        logger.debug(" PATCH Response Body :: ", response.getResponseBody());
        return response;
    }

    public RestResponse postRequest(String requestBody, String param) {
        HttpClient client = new DefaultHttpClient();
        String result = new String();
        RestResponse response = new RestResponse();
        HttpPost post = new HttpPost(DOCSTORE_URL + param);
        try {
            StringEntity stringEntity = new StringEntity(requestBody, "UTF-8");
            post.setEntity(stringEntity);
            response.setResponse(client.execute(post));
            result = getEncodeEntityValue(response.getResponse().getEntity());
        } catch (Exception e) {
            logger.error("POST response error is ::", e);
        }
        response.setResponseBody(result);
        logger.debug(" POST Response Body :: ", response.getResponseBody());
        return response;
    }

    public RestResponse putRequest(String requestBody, String param) {
        HttpClient client = new DefaultHttpClient();
        String result = new String();
        HttpPut put = new HttpPut(DOCSTORE_URL + param);
        RestResponse response = new RestResponse();
        try {
            StringEntity stringEntity = new StringEntity(requestBody, "UTF-8");
            put.setEntity(stringEntity);
            response.setResponse(client.execute(put));
            result = getEncodeEntityValue(response.getResponse().getEntity());
        } catch (Exception e) {
            logger.error("PUT response error is :: ", e);
        }
        response.setResponseBody(result);
        logger.debug(" PUT Response Body :: ", response.getResponseBody());
        return response;
    }

    private RestResponse getRequest(String id, String param) {
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(DOCSTORE_URL + param + id);
        String result = new String();
        RestResponse response = new RestResponse();
        try {
            response.setResponse(client.execute(get));
            // HttpEntity entity = response.getResponse().getEntity();
            result = getEncodeEntityValue(response.getResponse().getEntity());
        } catch (Exception e) {
            logger.error("GET response error is ::", e);
        }
        response.setResponseBody(result);
        logger.debug(" GET Response Body :: ", response.getResponseBody());
        return response;
    }

    private RestResponse getBibResponse(String id, String param) {
        RestResponse response = new RestResponse();
        response.setContentType("text/html; charset=utf-8");
        try {
            URL aURL = new URL(DOCSTORE_URL + param + id);
            StatusLine statusLine = new BasicStatusLine(new ProtocolVersion("http", 1, 1), 200, "OK");
            HttpResponse httpResponse = new BasicHttpResponse(statusLine);
            String result = getHttpResponse(new InputStreamReader(aURL.openStream()), response.getContentType());
            response.setResponseBody(result);
            response.setResponse(httpResponse);
            logger.debug(" GET Response Body :: ", response.getResponseBody());
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("Exception :", ex);
        }
        return response;
    }

    public RestResponse deleteRequest(String id, String param) {
        String result = new String();
        RestResponse response = new RestResponse();
        HttpClient client = new DefaultHttpClient();
        HttpDelete delete = new HttpDelete(DOCSTORE_URL + param + id);
        try {
            response.setResponse(client.execute(delete));
            result = getEncodeEntityValue(response.getResponse().getEntity());
        } catch (Exception e) {
            logger.error("DELETE response error is ::", e);
        }
        response.setResponseBody(result);
        logger.debug("DELETE Response Body :: ", response.getResponseBody());
        return response;
    }

    private String getEncodeEntityValue(HttpEntity entity) throws Exception {
        String result = EntityUtils.toString(entity, "UTF-8");
        return result;
    }

    private String getHttpResponse(InputStreamReader inputStreamReader, String contentType) throws Exception {
        StringWriter writer = new StringWriter();
        //   writer.setContentType("text/html; charset=utf-8");
        IOUtils.copy(inputStreamReader, writer);
        return writer.toString();
    }

    public File createZipFile(File sourceDir) throws IOException {
        File zipFile = File.createTempFile("tmp", ".zip");
        String path = sourceDir.getAbsolutePath();
        ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(zipFile));
        ArrayList<File> fileList = getAllFilesList(sourceDir);
        for (File file : fileList) {
            ZipEntry ze = new ZipEntry(file.getAbsolutePath().substring(path.length() + 1));
            zip.putNextEntry(ze);
            FileInputStream fis = new FileInputStream(file);
            org.apache.commons.compress.utils.IOUtils.copy(fis, zip);
            fis.close();
            zip.closeEntry();
        }
        zip.close();
        return zipFile;
    }

    public ArrayList<File> getAllFilesList(File directory) {
        ArrayList<File> fileList = new ArrayList<File>();
        if (directory.isFile())
            fileList.add(directory);
        else if (directory.isDirectory())
            for (File innerFile : directory.listFiles())
                fileList.addAll(getAllFilesList(innerFile));
        return fileList;
    }


    public void deleteFiles(File[] files) {
        try {
            for (File file : files) {
                try {
                    file.delete();
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
    }

    public List<Bib> acquisitionSearchRetrieveBibs(List<String> bibIds) {
//        String bibIdSb = buildIds(bibIds, "bibIds=");
        RestResponse restResponse = sendPostForAcquisitionSearch(DOCSTORE_URL+BIB_URL+"searchAcquistion",buildQueryPostString(bibIds, "bibId"));
        Bibs bibsObj = new Bibs();
        if (restResponse.getResponse().getStatusLine().getStatusCode() == 200) {
            if (restResponse.getResponseBody().startsWith("<org.kuali.ole.docstore.common.exception")) {
                throw DocstoreExceptionProcessor.fromXML(restResponse.getResponseBody());
            } else {
                if (bibIds.size() == 1) {
                    Bib bib = new Bib();
                    bibsObj.getBibs().add((Bib) bib.deserialize(restResponse.getResponseBody()));
                    return bibsObj.getBibs();
                }
                bibsObj = (Bibs) Bibs.deserialize(restResponse.getResponseBody());

            }
        }
        return bibsObj.getBibs();

    }

    private RestResponse sendPostForAcquisitionSearch(String url, String urlParameters) {

        StatusLine statusLine = new BasicStatusLine(new ProtocolVersion("http", 1, 1), 200, "OK");
        HttpResponse httpResponse = new BasicHttpResponse(statusLine);
        String postResponse = null;
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            postResponse = response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        RestResponse response = new RestResponse();
        response.setContentType("text/html; charset=utf-8");
        response.setResponseBody(postResponse);
        response.setResponse(httpResponse);
        return response;
    }

}