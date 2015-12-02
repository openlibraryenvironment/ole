package org.kuali.ole.dsng.rest.processor;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.kuali.ole.docstore.common.exception.DocstoreException;
import org.kuali.ole.docstore.common.exception.DocstoreResources;
import org.kuali.ole.docstore.common.exception.DocstoreValidationException;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.dsng.indexer.BibIndexer;
import org.kuali.ole.dsng.indexer.HoldingIndexer;
import org.kuali.ole.dsng.indexer.ItemIndexer;
import org.kuali.ole.dsng.indexer.OleDsNgIndexer;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.io.IOException;

/**
 * Created by SheikS on 11/25/2015.
 */
public class OleDsNgRestAPIProcessor {

    private BusinessObjectService businessObjectService;

    public String createBib(String jsonBody) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        BibRecord bibRecord = objectMapper.readValue(jsonBody, BibRecord.class);
        getBusinessObjectService().save(bibRecord);
        OleDsNgIndexer bibIndexer = new BibIndexer();
        bibIndexer.indexDocument(bibRecord);
        return objectMapper.writeValueAsString(bibRecord);
    }


    public String updateBib(String jsonBody) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        BibRecord bibRecord = objectMapper.readValue(jsonBody, BibRecord.class);

        if(StringUtils.isNotBlank(bibRecord.getBibId())){
            getBusinessObjectService().save(bibRecord);
            BibIndexer bibIndexer = new BibIndexer();
            bibIndexer.updateDocument(bibRecord);
            return objectMapper.writeValueAsString(bibRecord);
        } else {
            DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.BIB_ID_NOT_FOUND, DocstoreResources.BIB_ID_NOT_FOUND);
            docstoreException.addErrorParams("bibId", bibRecord.getBibId());
            throw docstoreException;
        }

    }

    public String createHolding(String jsonBody) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        HoldingsRecord holdingsRecord = objectMapper.readValue(jsonBody, HoldingsRecord.class);
        getBusinessObjectService().save(holdingsRecord);
        OleDsNgIndexer bibIndexer = new HoldingIndexer();
        bibIndexer.indexDocument(holdingsRecord);
        return objectMapper.writeValueAsString(holdingsRecord);
    }


    public String updateHolding(String jsonBody) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        HoldingsRecord holdingsRecord = objectMapper.readValue(jsonBody, HoldingsRecord.class);

        if(StringUtils.isNotBlank(holdingsRecord.getHoldingsId())){
            getBusinessObjectService().save(holdingsRecord);
            OleDsNgIndexer holdingIndexer = new HoldingIndexer();
            holdingIndexer.updateDocument(holdingsRecord);
            return objectMapper.writeValueAsString(holdingsRecord);
        } else {
            DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.HOLDING_ID_NOT_FOUND, DocstoreResources.HOLDING_ID_NOT_FOUND);
            docstoreException.addErrorParams("holdingId", holdingsRecord.getHoldingsId());
            throw docstoreException;
        }

    }

    public String createItem(String jsonBody) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ItemRecord itemRecord = objectMapper.readValue(jsonBody, ItemRecord.class);
        getBusinessObjectService().save(itemRecord);
        OleDsNgIndexer itemIndexer = new ItemIndexer();
        itemIndexer.indexDocument(itemRecord);
        return objectMapper.writeValueAsString(itemRecord);
    }


    public String updateItem(String jsonBody) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ItemRecord itemRecord = objectMapper.readValue(jsonBody, ItemRecord.class);

        if(StringUtils.isNotBlank(itemRecord.getItemId())){
            getBusinessObjectService().save(itemRecord);
            OleDsNgIndexer itemIndexer = new ItemIndexer();
            itemIndexer.updateDocument(itemRecord);
            return objectMapper.writeValueAsString(itemRecord);
        } else {
            DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.ITEM_ID_NOT_FOUND, DocstoreResources.ITEM_ID_NOT_FOUND);
            docstoreException.addErrorParams("itemId", itemRecord.getItemId());
            throw docstoreException;
        }

    }

    private BusinessObjectService getBusinessObjectService() {
        if(null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }
}

