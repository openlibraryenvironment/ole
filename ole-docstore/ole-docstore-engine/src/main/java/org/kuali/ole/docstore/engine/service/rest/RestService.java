package org.kuali.ole.docstore.engine.service.rest;


import org.kuali.ole.docstore.common.search.BrowseParams;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Created with IntelliJ IDEA.
 * User: jayabharathreddy
 * Date: 12/17/13
 * Time: 12:46 PM
 * To change this template use File | Settings | File Templates.
 */
public interface RestService {

    String createBib(String requestBody);

    String updateBib(String requestBody);

    String updateBibs(String requestBody);

    String retrieveBib(String bibId);

    String deleteBib(String bibId);

    String createHoldings(String requestBody);

    String retrieveHoldings(String holdingsId);

    String updateHoldings(String requestBody);

    String deleteHoldings(String holdingsId);

    String createItem(String requestBody);

    String createItems(String requestBody);

    String retrieveItems(String itemsId);

    String updateItem(String requestBody);

    String updateItems(String requestBody);

    String deleteItem(String itemsId);

    String deleteItems(String itemIds);

    String createBibTree(String requestBody);

    String createBibTrees(String requestBody);

    String retrieveBibTree(String bibId);

    String createHoldingsTree(String requestBody);

    String createHoldingsTrees(String requestBody);

    String retrieveHoldingsTree(String holdingsId);

    String search(String requestBody);

    String findBibs(String requestBody);

    String findHoldings(String requestBody);

    String findItems(String requestBody);

    String findBibTree(String requestBody);

    String findHoldingsTree(String requestBody);

    String browseItems(String requestBody);

    String browseHoldings(String requestBody);

    String boundWithBibs(String holdingsId, String requestBody);

    String transferHoldings(String bibId, String requestBody);

    String transferItems(String bibId, String requestBody);

    String retrieveHoldingsByBarcode(String[] barcode);

    String retrieveItemByBarcode(String barcode);

    String retrieveHoldingsTrees(String[] bibIds);

    String updateItemByBarcode(String[] barcodes, String requestBody);

    String patchItem(String requestBody);

    String updateItemByBarcodeWithContent(String[] barcode, String requestBody);

    String patchItemById(String[] itemsIds, String requestBody);

    String patchItemByIdWithContent(String[] itemsIds, String requestBody);

    String patchItemWithContent(String requestBody);

}
