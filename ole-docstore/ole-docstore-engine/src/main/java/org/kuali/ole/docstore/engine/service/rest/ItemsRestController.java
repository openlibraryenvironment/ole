package org.kuali.ole.docstore.engine.service.rest;

import com.thoughtworks.xstream.converters.ConversionException;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.common.document.Items;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecords;
import org.kuali.ole.docstore.common.document.content.bib.marc.OrderBibMarcRecord;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.ids.BibId;
import org.kuali.ole.docstore.common.document.ids.BibIds;
import org.kuali.ole.docstore.common.document.ids.HoldingsId;
import org.kuali.ole.docstore.common.document.ids.ItemIds;
import org.kuali.ole.docstore.common.exception.*;
import org.kuali.ole.docstore.common.find.FindParams;
import org.kuali.ole.docstore.common.service.DocstoreService;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.docstore.service.BeanLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jayabharathreddy
 * Date: 12/18/13
 * Time: 4:07 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/item")
public class ItemsRestController extends AbstractRestService {

    private static final Logger LOG = LoggerFactory.getLogger(ItemsRestController.class);
    private static String responseUrl = "documentrest/item/doc/";
    private Logger logger = LoggerFactory.getLogger(ItemsRestController.class);
    public static BibMarcRecordProcessor bibMarcRecordProcessor = new BibMarcRecordProcessor();

    @Override
    @RequestMapping(value = "/doc/", method = RequestMethod.POST, consumes = "application/xml", produces = "application/text")
    @ResponseBody
    public String createItem(@RequestBody String requestBody) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        ItemOleml item = new ItemOleml();
        item = (ItemOleml) item.deserialize(requestBody);
        try {
            ds.createItem(item);
        } catch (DocstoreException e) {
            LOG.info("Exception :", e);
            return DocstoreExceptionProcessor.toXml(e);
        }
        return responseUrl + item.getId();
    }


    @RequestMapping(value = "/doc", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public String retrieveItems(@RequestParam("itemId") String[] itemsIds) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        if(itemsIds.length == 1) {
            Item item = null;
            try {
                item = ds.retrieveItem(itemsIds[0]);
            } catch (DocstoreException e) {
                LOG.info("Exception :", e);
                return DocstoreExceptionProcessor.toXml(e);
            }
            ItemOleml items = new ItemOleml();
            return items.serialize(item);

        }
        else {
            List<String> itemsIdList = new ArrayList<>();
            for (String id : itemsIds) {
                itemsIdList.add(id);
            }
            List<Item> items = null;
            try {
                items = ds.retrieveItems(itemsIdList);
            } catch (DocstoreException e) {
                LOG.info("Exception :", e);
                return DocstoreExceptionProcessor.toXml(e);
            }
            Items itemsObj = new Items();
            itemsObj.getItems().addAll(items);
            return Items.serialize(itemsObj);
        }

    }

    @RequestMapping(value = "/doc/map", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public String retrieveItemMap(@RequestParam("itemId") String[] itemsIds) {
        DocstoreService ds = BeanLocator.getDocstoreService();
            List<String> itemsIdList = new ArrayList<>();
            for (String id : itemsIds) {
                itemsIdList.add(id);
            }
            HashMap<String,Item> items = null;
            try {
                items = ds.retrieveItemMap(itemsIdList);
            } catch (DocstoreException e) {
                LOG.info("Exception :", e);
                return DocstoreExceptionProcessor.toXml(e);
            }
            ItemMap itemsObj = new ItemMap();
            itemsObj.getItemMap().putAll(items);
            return ItemMap.serialize(itemsObj);
    }


    @Override
    @RequestMapping(value = "/holdings", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public String retrieveHoldingsByBarcode(@RequestParam("barcode") String[] barcodes) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        StringBuilder holdingsBuilder = new StringBuilder();
        holdingsBuilder.append("<holdings>").append("\n");
        for (String barcode2 : barcodes) {
            Item item = null;
            try {
                item = ds.retrieveItemByBarcode(barcode2);
            } catch (DocstoreException e) {
                LOG.info("Exception :", e);
                return DocstoreExceptionProcessor.toXml(e);
            }
            if (item != null && null != item.getHolding()) {
                String holdingsId = item.getHolding().getId();
                if (holdingsId != null) {
                    Holdings holdings = ds.retrieveHoldings(holdingsId);
                    holdingsBuilder.append(holdings.getContent()).append("\n");
                }
            }
        }
        holdingsBuilder.append("</holdings>");
        return holdingsBuilder.toString();
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public String retrieveItemByBarcode(@RequestParam("barcode") String barcode) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        Item item = new Item();
        if (StringUtils.isNotBlank(barcode)) {
            try {
                item = ds.retrieveItemByBarcode(barcode);
            } catch (DocstoreException e) {
                LOG.info("Exception :", e);
                return DocstoreExceptionProcessor.toXml(e);
            }
        }
        return item.getContent();
    }

    @Override
    @RequestMapping(method = RequestMethod.PATCH, consumes = "application/xml", produces = "application/xml")
    @ResponseBody
    public String updateItemByBarcodeWithContent(@RequestParam("barcode") String[] barcodes, @RequestBody String requestBody) {
        Item existingItemDoc = null;
        try {
            DocstoreService ds = BeanLocator.getDocstoreService();
            Item incomingItemDoc = new Item();
            incomingItemDoc.setContent(requestBody);
            for (String barcode : barcodes) {
                existingItemDoc = ds.retrieveItemByBarcode(barcode);
                existingItemDoc.setItem(incomingItemDoc);
                ds.updateItem(existingItemDoc);
            }
        } catch (DocstoreException e) {
            LOG.info("Exception :", e);
            return DocstoreExceptionProcessor.toXml(e);
        } catch (ConversionException e) {
            LOG.info("Exception :", e);
            return DocstoreExceptionProcessor.toXml(new DocstoreValidationException(e.getMessage()));
        }
        return existingItemDoc.serialize(existingItemDoc);
    }


    @Override
    @RequestMapping(value = "/doc", method = RequestMethod.PATCH, consumes = "application/xml", produces = "application/xml")
    @ResponseBody
    public String updateItemByBarcode(@RequestParam("barcode") String[] barcodes, @RequestBody String requestBody) {
        Item existingItemDoc = null;
        Item incomingItemDoc = new Item();
        try {
            DocstoreService ds = BeanLocator.getDocstoreService();
            incomingItemDoc = (Item) incomingItemDoc.deserialize(requestBody);
            for (String barcode : barcodes) {
                existingItemDoc = ds.retrieveItemByBarcode(barcode);
                existingItemDoc.setItem(incomingItemDoc);
                ds.updateItem(existingItemDoc);
            }
        } catch (DocstoreException e) {
            LOG.info("Exception :", e);
            return DocstoreExceptionProcessor.toXml(e);
        } catch (ConversionException e) {
            LOG.info("Exception :", e);
            return DocstoreExceptionProcessor.toXml(new DocstoreValidationException(e.getMessage()));
        }
        return existingItemDoc.serialize(existingItemDoc);
    }

    @Override
    @RequestMapping(value = "/doc/", method = RequestMethod.PATCH, consumes = "application/xml", produces = "application/xml")
    @ResponseBody
    public String patchItem(@RequestBody String requestBody) {
        Item existingItemDoc = null;
        Item incomingItemDoc = new Item();
        try {
            DocstoreService ds = BeanLocator.getDocstoreService();
            incomingItemDoc = (Item) incomingItemDoc.deserialize(requestBody);
            existingItemDoc = ds.retrieveItem(incomingItemDoc.getId());
            existingItemDoc.setItem(incomingItemDoc);
            ds.updateItem(existingItemDoc);
        } catch (DocstoreException e) {
            LOG.info("Exception :", e);
            return DocstoreExceptionProcessor.toXml(e);
        } catch (ConversionException e) {
            LOG.info("Exception :", e);
            return DocstoreExceptionProcessor.toXml(new DocstoreValidationException(e.getMessage()));
        }
        return incomingItemDoc.serialize(incomingItemDoc);
    }

    @Override
    @RequestMapping(value = "/", method = RequestMethod.PATCH, consumes = "application/xml", produces = "application/xml")
    @ResponseBody
    public String patchItemWithContent(@RequestBody String requestBody) {
        Item existingItemDoc = null;
        Item incomingItemDoc = new Item();
        try {
            DocstoreService ds = BeanLocator.getDocstoreService();
            incomingItemDoc.setContent(requestBody);
            ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
            org.kuali.ole.docstore.common.document.content.instance.Item incomingItemContent = itemOlemlRecordProcessor.fromXML(incomingItemDoc.getContent());
            existingItemDoc = ds.retrieveItem(incomingItemContent.getItemIdentifier());
            existingItemDoc.setItem(incomingItemDoc);
            ds.updateItem(existingItemDoc);
        } catch (DocstoreException e) {
            LOG.info("Exception :", e);
            return DocstoreExceptionProcessor.toXml(e);
        } catch (ConversionException e) {
            LOG.info("Exception :", e);
            return DocstoreExceptionProcessor.toXml(new DocstoreValidationException(e.getMessage()));
        }
        return incomingItemDoc.serialize(incomingItemDoc);
    }

    @Override
    @RequestMapping(value = "/doc/id", method = RequestMethod.PATCH, consumes = "application/xml", produces = "application/xml")
    @ResponseBody
    public String patchItemById(@RequestParam("id") String[] itemsIds, @RequestBody String requestBody) {

        Item incomingItemDoc = null;
        try {
            DocstoreService ds = BeanLocator.getDocstoreService();
            incomingItemDoc = new Item();
            incomingItemDoc = (Item) incomingItemDoc.deserialize(requestBody);
            for (String itemId : itemsIds) {
                Item existingItemDoc = ds.retrieveItem(itemId);
                existingItemDoc.setItem(incomingItemDoc);
                ds.updateItem(existingItemDoc);
            }
        } catch (DocstoreException e) {
            LOG.info("Exception :", e);
            return DocstoreExceptionProcessor.toXml(e);
        } catch (ConversionException e) {
            LOG.info("Exception :", e);
            return DocstoreExceptionProcessor.toXml(new DocstoreValidationException(e.getMessage()));
        }
        return incomingItemDoc.serialize(incomingItemDoc);
    }

    @Override
    @RequestMapping(value = "/id", method = RequestMethod.PATCH, consumes = "application/xml", produces = "application/xml")
    @ResponseBody
    public String patchItemByIdWithContent(@RequestParam("id") String[] itemsIds, @RequestBody String requestBody) {
        Item existingItemDoc = null;
        Item incomingItemDoc = null;
        try {
            DocstoreService ds = BeanLocator.getDocstoreService();
            incomingItemDoc = new Item();
            incomingItemDoc.setContent(requestBody);
            for (String itemId : itemsIds) {
                existingItemDoc = ds.retrieveItem(itemId);
                existingItemDoc.setItem(incomingItemDoc);
                ds.updateItem(existingItemDoc);
            }
        } catch (DocstoreException e) {
            LOG.info("Exception :", e);
            return DocstoreExceptionProcessor.toXml(e);
        } catch (ConversionException e) {
            LOG.info("Exception :", e);
            return DocstoreExceptionProcessor.toXml(new DocstoreValidationException(e.getMessage()));
        }
        return incomingItemDoc.serialize(incomingItemDoc);
    }

    @Override
    @RequestMapping(value = "/doc/", method = RequestMethod.PUT, consumes = "application/xml", produces = "application/xml")
    @ResponseBody
    public String updateItem(@RequestBody String requestBody) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        ItemOleml item = new ItemOleml();
        item = (ItemOleml) item.deserialize(requestBody);
        try {
            ds.updateItem(item);
        } catch (DocstoreException e) {
            LOG.info("Exception :", e);
            return DocstoreExceptionProcessor.toXml(e);
        }
        return responseUrl + item.getId();
    }



    @Override
    @RequestMapping(value = "/doc", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteItem(@RequestParam("itemId") String itemId) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        try {
            ds.deleteItem(itemId);
        } catch (DocstoreException e) {
            LOG.info("Exception :", e);
            return DocstoreExceptionProcessor.toXml(e);
        }
        return "Success";
    }

    @Override
    @RequestMapping(value = "/docs", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteItems(@RequestParam("itemId") String itemIds) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        String[] itemsIds = itemIds.split(",");
        try {
            for (String itemId : itemsIds) {
                ds.deleteItem(itemId);
            }
        } catch (DocstoreException e) {
            LOG.info("Exception :", e);
            return DocstoreExceptionProcessor.toXml(e);
        }
        return "Success";
    }

    @Override
    @RequestMapping(value = "/doc/find", method = RequestMethod.POST, consumes = "application/xml", produces = "application/text")
    @ResponseBody
    public String findItems(@RequestBody String requestBody) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        FindParams findParams = new FindParams();
        findParams = (FindParams) findParams.deserialize(requestBody);
        HashMap<String, String> hashMap = new HashMap();
        List<FindParams.Map.Entry> entries = findParams.getMap().getEntry();
        for (FindParams.Map.Entry entry : entries) {
            hashMap.put(entry.getKey(), entry.getValue());
        }
        Item item = null;
        try {
            item = ds.findItem(hashMap);
        } catch (DocstoreException e) {
            LOG.info("Exception :", e);
            return DocstoreExceptionProcessor.toXml(e);
        }

        return item.serialize(item);
    }

    @RequestMapping(value = "/doc/bulkUpdate", method = RequestMethod.PUT, consumes = "application/xml", produces = "application/xml")
    @ResponseBody
    public String bulkUpdateItems(@RequestBody String requestBody) {
        DocstoreService ds = BeanLocator.getDocstoreService();

        String[] bulkUpdateRequest = requestBody.split("\n", 3);

        String itemId = bulkUpdateRequest[0];
        String canUpdateStaffOnlyFlag = bulkUpdateRequest[1];
        requestBody = bulkUpdateRequest[2];


        String[] itemSplitIds = itemId.split(",");
        List<String> itemIds = new ArrayList<>();
        for (String id : itemSplitIds) {
            itemIds.add(id);
        }
        ItemOleml items = new ItemOleml();
        items = (ItemOleml) items.deserialize(requestBody);
        try {
            ds.bulkUpdateItem(items, itemIds, canUpdateStaffOnlyFlag);
        } catch (DocstoreException e) {
            LOG.info("Exception :", e);
            return DocstoreExceptionProcessor.toXml(e);
        }
        return items.serialize(items);
    }



}
