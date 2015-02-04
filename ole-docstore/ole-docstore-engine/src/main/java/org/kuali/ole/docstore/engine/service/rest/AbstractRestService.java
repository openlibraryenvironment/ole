package org.kuali.ole.docstore.engine.service.rest;

import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created with IntelliJ IDEA.
 * User: jayabharathreddy
 * Date: 12/17/13
 * Time: 1:12 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractRestService implements RestService {

    @Override
    public String createBib(String requestBody) {
        return null;
    }

    @Override
    public String updateBib(String requestBody) {
        return null;
    }

    @Override
    public String updateBibs(String requestBody) {
        return null;
    }

    @Override
    public String retrieveBib(String bibId) {
        return null;
    }

    @Override
    public String deleteBib(String bibId) {
        return null;
    }

    @Override
    public String createHoldings(String requestBody) {
        return null;
    }

    @Override
    public String retrieveHoldings(String holdingsId) {
        return null;
    }

    @Override
    public String updateHoldings(String requestBody) {
        return null;
    }

    @Override
    public String deleteHoldings(String holdingsId) {
        return null;
    }

    @Override
    public String createItem(String requestBody) {
        return null;
    }

    @Override
    public String createItems(String requestBody) {
        return null;
    }

    @Override
    public String retrieveItems(String itemsId) {
        return null;
    }

    @Override
    public String updateItem(String requestBody) {
        return null;
    }

    @Override
    public String updateItems(String requestBody) {
        return null;
    }

    @Override
    public String deleteItem(String itemsId) {
        return null;
    }

    @Override
    public String deleteItems(String itemIds) {
        return null;
    }

    @Override
    public String createBibTree(String requestBody) {
        return null;
    }

    @Override
    public String createBibTrees(String requestBody) {
        return null;
    }

    @Override
    public String retrieveBibTree(String bibId) {
        return null;
    }

    @Override
    public String createHoldingsTree(String requestBody) {
        return null;
    }

    @Override
    public String createHoldingsTrees(String requestBody) {
        return null;
    }

    @Override
    public String retrieveHoldingsTree(String holdingsId) {
        return null;
    }

    @Override
    public String search(String requestBody) {
        return null;
    }

    @Override
    public String findBibs(String requestBody) {
        return null;
    }

    @Override
    public String findBibTree(String requestBody) {
        return null;
    }

    @Override
    public String findHoldings(String requestBody) {
        return null;
    }

    @Override
    public String findHoldingsTree(String requestBody) {
        return null;
    }

    @Override
    public String findItems(String requestBody) {
        return null;
    }

    @Override
    public String browseItems(String requestBody) {
        return null;
    }

    @Override
    public String browseHoldings(String requestBody) {
        return null;
    }

    @Override
    public String boundWithBibs(String holdingsId, String requestBody) {
        return null;
    }

    @Override
    public String transferHoldings(String bibId, String requestBody) {
        return null;
    }

    @Override
    public String transferItems(String bibId, String requestBody) {
        return null;
    }

    @Override
    public String retrieveHoldingsByBarcode(String[] barcode) {
        return null;
    }

    @Override
    public String retrieveItemByBarcode(String barcode) {
        return null;
    }

    @Override
    public String retrieveHoldingsTrees(String[] bibIds) {
        return null;
    }

    @Override
    public String updateItemByBarcode(String[] barcodes, String requestBody) {
        return null;
    }

    @Override
    public String patchItem(String requestBody) {
        return null;
    }

    @Override
    public String updateItemByBarcodeWithContent(String[] barcode, String requestBody) {
        return null;
    }

    @Override
    public String patchItemById(String[] itemsIds, String requestBody) {
        return null;
    }

    @Override
    public String patchItemByIdWithContent(String[] itemsIds, String requestBody) {
        return null;
    }

    @Override
    public String patchItemWithContent(String requestBody) {
        return null;
    }
}
