package org.kuali.ole.docstore.engine.service.storage;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.content.enums.DocCategory;
import org.kuali.ole.docstore.common.document.content.enums.DocFormat;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.exception.DocstoreException;
import org.kuali.ole.docstore.engine.factory.DocumentManagerFactory;
import org.kuali.ole.docstore.engine.service.BibTreeProcessor;
import org.kuali.ole.docstore.engine.service.storage.rdbms.*;
import org.kuali.ole.utility.OleStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 12/13/13
 * Time: 6:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocstoreRDBMSStorageService implements DocstoreStorageService {

    private static final Logger LOG = LoggerFactory.getLogger(DocstoreRDBMSStorageService.class);
    private PlatformTransactionManager transactionManager;

    @Override
    public void createBib(Bib bib) {
        DocumentManager documentManager = DocumentManagerFactory.getInstance().getDocumentManager(bib.getCategory(), bib.getType(), bib.getFormat());
        documentManager.validate(bib);
        documentManager.create(bib);
    }

    @Override
    public void createHoldings(Holdings holdings) {
        DocumentManager documentManager = DocumentManagerFactory.getInstance().getDocumentManager(DocCategory.WORK.getCode(), DocType.HOLDINGS.getCode(), DocFormat.OLEML.getCode());
        documentManager.validate(holdings);
        documentManager.create(holdings);
    }

    @Override
    public void createItem(Item item) {
        DocumentManager documentManager = DocumentManagerFactory.getInstance().getDocumentManager(DocCategory.WORK.getCode(), DocType.ITEM.getCode(), DocFormat.OLEML.getCode());
        documentManager.validate(item);
        documentManager.create(item);
    }

    @Override
    public void createHoldingsTree(HoldingsTree holdingsTree) {
        DocumentManager holdingsDocumentManager = RdbmsHoldingsDocumentManager.getInstance();
        DocumentManager itemDocumentManager = RdbmsItemDocumentManager.getInstance();
        holdingsDocumentManager.validate(holdingsTree.getHoldings());
        for (Item item : holdingsTree.getItems()) {
            itemDocumentManager.validate(item);
        }
        createHoldings(holdingsTree.getHoldings());
        for (Item item : holdingsTree.getItems()) {
            Holdings holdings = new PHoldings();
            holdings.setId(holdingsTree.getHoldings().getId());
            holdings.setCreatedBy(holdingsTree.getHoldings().getCreatedBy());
            if(holdingsTree.getHoldings().isStaffOnly()){
                item.setStaffOnly(holdingsTree.getHoldings().isStaffOnly());
            }
            item.setHolding(holdings);
            createItem(item);
        }
    }

    @Override
    public void createBibTree(BibTree bibTree) {
        DocumentManager holdingsDocumentManager = RdbmsHoldingsDocumentManager.getInstance();
        DocumentManager itemDocumentManager = RdbmsItemDocumentManager.getInstance();
        for (HoldingsTree holdingsTree : bibTree.getHoldingsTrees()) {
            if(holdingsTree.getHoldings() != null && holdingsTree.getHoldings().getContent() != null) {
                holdingsDocumentManager.validate(holdingsTree.getHoldings());
                for (Item item : holdingsTree.getItems()) {
                    itemDocumentManager.validate(item);
                }
            }
        }
        createBib(bibTree.getBib());
        for (HoldingsTree holdingsTree : bibTree.getHoldingsTrees()) {
            Bib bib = new BibMarc();
            bib.setId(bibTree.getBib().getId());
            if (holdingsTree.getHoldings() != null && holdingsTree.getHoldings().getContent() != null) {
                holdingsTree.getHoldings().setBib(bib);
                createHoldingsTree(holdingsTree);
            }
        }
    }

    @Override
    public Bib retrieveBib(String bibId) {
        DocumentManager documentManager =null;
        documentManager = RdbmsBibDocumentManager.getInstance();
        Bib bib = (Bib) documentManager.retrieve(bibId);
        return bib;
    }

    @Override
    public List<Bib> retrieveBibs(List<String> bibIds) {
        DocumentManager documentManager =null;
        List<Bib> bibs = new ArrayList<>();
        if(bibIds!=null && bibIds.size() > 0) {
            documentManager = RdbmsBibDocumentManager.getInstance();
            List<Object> objs = documentManager.retrieve(bibIds);

            for (Object obj : objs) {
                bibs.add((Bib) obj);
            }
        }
        return bibs;
    }

    @Override
    public List<Item> retrieveItems(List<String> itemIds) {
        DocumentManager documentManager = RdbmsItemDocumentManager.getInstance();
        List<Object> objs = documentManager.retrieve(itemIds);
        List<Item> items = new ArrayList<>();
        for (Object obj : objs) {
            items.add((Item) obj);
        }
        return items;
    }

    @Override
    public HashMap<String, Item> retrieveItemMap(List<String> itemIds) {
        DocumentManager documentManager = RdbmsItemDocumentManager.getInstance();
        List<Object> objs = documentManager.retrieve(itemIds);
        HashMap<String,Item> items = new HashMap<>();
        for (Object obj : objs) {
            Item item = (Item) obj;
            items.put(item.getId(), item);
        }
        return items;
    }

    @Override
    public Holdings retrieveHoldings(String holdingsId) {
        RdbmsHoldingsDocumentManager rdbmsHoldingsDocumentManager = RdbmsHoldingsDocumentManager.getInstance();
        Holdings holdings = (Holdings) rdbmsHoldingsDocumentManager.retrieve(holdingsId);
        return holdings;
    }

    @Override
    public Item retrieveItem(String itemId) {
        RdbmsItemDocumentManager rdbmsItemDocumentManager = RdbmsItemDocumentManager.getInstance();
        Item item = (Item) rdbmsItemDocumentManager.retrieve(itemId);
        return item;
    }

    @Override
    public HoldingsTree retrieveHoldingsTree(String holdingsId) {
        DocumentManager documentManager = RdbmsHoldingsDocumentManager.getInstance();
        HoldingsTree holdingsTree = (HoldingsTree) documentManager.retrieveTree(holdingsId);
        return holdingsTree;
    }

    @Override
    public BibTree retrieveBibTree(String bibId) {
        DocumentManager documentManager =null;
        documentManager = RdbmsBibDocumentManager.getInstance();
        BibTree bibTree = (BibTree) documentManager.retrieveTree(bibId);
        return bibTree;
    }

    @Override
    public List<BibTree> retrieveBibTrees(List<String> bibIds) {
        List<BibTree> bibTrees = new ArrayList<>();
        for (String bibId : bibIds) {
            try{
                bibTrees.add(retrieveBibTree(bibId));
            }catch(Exception e){
            }
        }

        return bibTrees;
    }

    @Override
    public void updateBib(Bib bib) {
        DocumentManager documentManager = DocumentManagerFactory.getInstance().getDocumentManager(bib.getCategory(), bib.getType(), bib.getFormat());
        documentManager.update(bib);
    }

    @Override
    public void updateBibs(List<Bib> bibs) {
        for (Bib bib : bibs) {
            DocumentManager documentManager = DocumentManagerFactory.getInstance().getDocumentManager(bib.getCategory(), bib.getType(), bib.getFormat());
            documentManager.update(bib);
        }
    }

    @Override
    public void updateHoldings(Holdings holdings) {
        DocumentManager documentManager = DocumentManagerFactory.getInstance().getDocumentManager(DocCategory.WORK.getCode(), DocType.HOLDINGS.getCode(), DocFormat.OLEML.getCode());
        documentManager.validate(holdings);
        documentManager.update(holdings);
    }

    @Override
    public void updateItem(Item item) {
        DocumentManager documentManager = DocumentManagerFactory.getInstance().getDocumentManager(item.getCategory(), item.getType(), item.getFormat());
        documentManager.validate(item);
        documentManager.update(item);

    }

    @Override
    public void deleteBib(String bibId) {
        DocumentManager documentManager = RdbmsBibMarcDocumentManager.getInstance();
        documentManager.deleteVerify(bibId);
        documentManager.delete(bibId);
    }

    @Override
    public void deleteHoldings(String holdingsId) {
        DocumentManager documentManager = RdbmsHoldingsDocumentManager.getInstance();
        documentManager.deleteVerify(holdingsId);
        documentManager.delete(holdingsId);

    }

    @Override
    public void deleteItem(String itemId) {
        DocumentManager documentManager = RdbmsItemDocumentManager.getInstance();
        documentManager.deleteVerify(itemId);
        documentManager.delete(itemId);

    }

    @Override
    public void rollback() {
        //TODO: implementation is pending for rollback.
    }

    public void boundHoldingsWithBibs(String holdingsId, List<String> bibIds) {
        RdbmsHoldingsDocumentManager rdbmsHoldingsDocumentManager = RdbmsHoldingsDocumentManager.getInstance();
        rdbmsHoldingsDocumentManager.boundHoldingsWithBibs(holdingsId, bibIds);
    }

    public void transferHoldings(List<String> holdingsIds, String bibId) {
        RdbmsHoldingsDocumentManager rdbmsHoldingsDocumentManager = RdbmsHoldingsDocumentManager.getInstance();
        rdbmsHoldingsDocumentManager.transferInstances(holdingsIds, bibId);

    }

    public void transferItems(List<String> itemIds, String bibId) {
        RdbmsItemDocumentManager rdbmsItemDocumentManager = RdbmsItemDocumentManager.getInstance();
        rdbmsItemDocumentManager.transferItems(itemIds, bibId);
    }

    @Override
    public void createBibTrees(BibTrees bibTrees) {
        for (BibTree bibTree : bibTrees.getBibTrees()) {
            createBibTree(bibTree);
        }
    }

    @Override
    public void deleteBibs(List<String> bibIds) {
        for (String bibId : bibIds) {
            deleteBib(bibId);
        }
    }

    @Override
    public void validateInput(Object object) {
        DocumentManager documentManager = null;
        DocstoreDocument docstoreDocument = (DocstoreDocument) object;
        documentManager = DocumentManagerFactory.getInstance().getDocumentManager(docstoreDocument.getCategory(), docstoreDocument.getType(), docstoreDocument.getFormat());
        documentManager.validate(object);
    }

    @Override
    public void createLicense(License license) {
        DocumentManager documentManager = null;
        documentManager = DocumentManagerFactory.getInstance().getDocumentManager(license.getCategory(), license.getType(), license.getFormat());
        documentManager.create(license);
    }

    @Override
    public void createLicenses(Licenses licenses) {
        DocumentManager documentManager = null;
        for (License license : licenses.getLicenses()) {
            documentManager = DocumentManagerFactory.getInstance().getDocumentManager(license.getCategory(), license.getType(), license.getFormat());
            documentManager.create(license);
        }
    }

    @Override
    public License retrieveLicense(String licenseId) {
        DocumentManager documentManager = null;
        if(licenseId.startsWith(DocumentUniqueIDPrefix.PREFIX_WORK_LICENSE_ONIXPL)) {
            documentManager = DocumentManagerFactory.getInstance().getDocumentManager(DocCategory.WORK.getCode(), DocType.LICENSE.getCode(), DocFormat.ONIXPL.getCode());
        } else {
            documentManager = RdbmsLicenseAttachmentDocumentManager.getInstance();
        }

        License license = (License) documentManager.retrieve(licenseId);
        return license;
    }

    @Override
    public Licenses retrieveLicenses(List<String> licenseIds) {
        Licenses licenses = new Licenses();
        for(String id : licenseIds) {
            licenses.getLicenses().add(this.retrieveLicense(id));
        }
        return licenses;
    }

    @Override
    public void updateLicense(License license) {
        DocumentManager documentManager = DocumentManagerFactory.getInstance().getDocumentManager(license.getCategory(), license.getType(), license.getFormat());
        documentManager.update(license);
    }

    @Override
    public void updateLicenses(Licenses licenses) {
        DocumentManager documentManager = null;
        for(License license : licenses.getLicenses()) {
            documentManager = DocumentManagerFactory.getInstance().getDocumentManager(license.getCategory(), license.getType(), license.getFormat());
            documentManager.update(license);

        }
    }

    @Override
    public void deleteLicense(String licenseId) {
        DocumentManager documentManager = RdbmsLicenseOnixplDocumentManager.getInstance();
        documentManager.delete(licenseId);
    }

    @Override
    public void createAnalyticsRelation(String seriesHoldingsId, List<String> itemIds) {
        RdbmsHoldingsDocumentManager rdbmsHoldingsDocumentManager = RdbmsHoldingsDocumentManager.getInstance();
        rdbmsHoldingsDocumentManager.createAnalyticsRelation(seriesHoldingsId, itemIds);
    }

    @Override
    public void breakAnalyticsRelation(String seriesHoldingsId, List<String> itemIds) {
        RdbmsHoldingsDocumentManager rdbmsHoldingsDocumentManager = RdbmsHoldingsDocumentManager.getInstance();
        rdbmsHoldingsDocumentManager.breakAnalyticsRelation(seriesHoldingsId, itemIds);
    }

    @Override
    public Item retrieveItemByBarcode(String barcode) {
        RdbmsItemDocumentManager rdbmsItemDocumentManager = RdbmsItemDocumentManager.getInstance();
        Item item = rdbmsItemDocumentManager.retrieveItemByBarcode(barcode);
        return item;
    }

    /**
     * This method is for batch updates to bibs, holdings and items.
     *
     * @param bibTrees
     */
    @Override
    public void processBibTrees(BibTrees bibTrees) {
        List<Future> futures = new ArrayList<>();
        String parameter = ParameterValueResolver.getInstance().getParameter("OLE", "OLE-DESC",
                "Describe", "NUM_THREADS_FOR_PROCESSING_BIB");
        OleStopWatch oleStopWatch = new OleStopWatch();
        LOG.info("NUM_THREADS_FOR_PROCESSING_BIB: " + parameter);
        ExecutorService executorService = Executors.newFixedThreadPool(StringUtils.isNotEmpty(parameter) ? Integer
                        .parseInt(parameter) : 10
        );

        LOG.info("Num tree to process:" + bibTrees.getBibTrees().size());
        oleStopWatch.start();
        for (BibTree bibTree : bibTrees.getBibTrees()) {
            futures.add(executorService.submit(new BibTreeProcessor(bibTree)));
        }

        for (Iterator<Future> iterator = futures.iterator(); iterator.hasNext(); ) {
            Future future = iterator.next();
            try {
                BibTree response = (BibTree) future.get();
            } catch (InterruptedException e) {
                LOG.info(e.getMessage());
            } catch (ExecutionException e) {
                LOG.info(e.getMessage());
            }
        }
        executorService.shutdown();
        oleStopWatch.end();
        LOG.info("Time taken to process bibTrees:" + oleStopWatch.getTotalTime() + " ms");
    }


    @Override
    public void processBibTreesForBatch(BibTrees bibTrees) {
      for(BibTree bibTree:bibTrees.getBibTrees()){
          processBibTree(bibTree);
      }
    }


    /**
     * This method is for batch updates to bib tree.
     *
     * @param bibTree
     */
    public void processBibTree(BibTree bibTree) {
        Bib bib = bibTree.getBib();
        if (null != bib) {
            processBib(bib);
            if (Bib.ResultType.SUCCESS.equals(bib.getResult()) || bib.getId() != null) {
                if (!Bib.OperationType.DELETE.equals(bib.getOperation())) {
                    processHoldingsTrees(bibTree.getHoldingsTrees(), bib);
                }
            }
        }
    }

    /**
     * This method is for batch updates to bib.
     *
     * @param bib
     */
    private void processBib(Bib bib) {
        if (Bib.OperationType.CREATE.equals(bib.getOperation())) {
            try {
                createBib(bib);
                bib.setResult(Bib.ResultType.SUCCESS);
            } catch (DocstoreException e) {
                LOG.error("Exception while processing bib to create", e);
                bib.setResult(Bib.ResultType.FAILURE);
                bib.setMessage(e.getErrorMessage());
            } catch (Exception e) {
                LOG.error("Exception while processing bib to create", e);
                bib.setResult(Bib.ResultType.FAILURE);
                bib.setMessage(e.getMessage());
            }
        } else if (Bib.OperationType.UPDATE.equals(bib.getOperation())) {
            try {
                updateBib(bib);
                bib.setResult(Bib.ResultType.SUCCESS);
            } catch (DocstoreException e) {
                LOG.error("Exception while processing bib to update", e);
                bib.setResult(Bib.ResultType.FAILURE);
                bib.setMessage(e.getErrorMessage());
            } catch (Exception e) {
                LOG.error("Exception while processing bib to update", e);
                bib.setResult(Bib.ResultType.FAILURE);
                bib.setMessage(e.getMessage());
            }
        } else if (Bib.OperationType.DELETE.equals(bib.getOperation())) {
            try {
                deleteBib(bib.getId());
                bib.setResult(Bib.ResultType.SUCCESS);
            } catch (DocstoreException e) {
                LOG.error("Exception while processing bib to delete", e);
                bib.setResult(Bib.ResultType.FAILURE);
                bib.setMessage(e.getErrorMessage());
            } catch (Exception e) {
                LOG.error("Exception while processing bib to delete", e);
                bib.setResult(Bib.ResultType.FAILURE);
                bib.setMessage(e.getMessage());
            }
        }
    }

    /**
     * This method is for batch updates to holdings trees.
     * @param holdingsTrees
     * @param bib
     */
    private void processHoldingsTrees(List<HoldingsTree> holdingsTrees, Bib bib) {
        if (CollectionUtils.isNotEmpty(holdingsTrees)) {
            for (HoldingsTree holdingsTree : holdingsTrees) {
                if (null != holdingsTree.getHoldings()) {
                    holdingsTree.getHoldings().setBib(bib);
                    processHoldingsTree(holdingsTree);
                }
            }
        }
    }

    /**
     * This method is for batch updates to holdings tree.
     * @param holdingsTree
     */
    private void processHoldingsTree(HoldingsTree holdingsTree) {
        processHoldings(holdingsTree.getHoldings());
        if (Holdings.ResultType.SUCCESS.equals(holdingsTree.getHoldings().getResult()) || holdingsTree.getHoldings().getId() != null) {
            if (!Holdings.OperationType.DELETE.equals(holdingsTree.getHoldings().getOperation())) {
                processItems(holdingsTree.getItems(), holdingsTree.getHoldings());
            }

        }
    }

    /**
     * This method is for batch updates to holdings.
     *
     * @param holdings
     */
    private void processHoldings(Holdings holdings) {
        if (Holdings.OperationType.CREATE.equals(holdings.getOperation())) {
            try {
                createHoldings(holdings);
                holdings.setResult(Holdings.ResultType.SUCCESS);
            } catch (DocstoreException e) {
                LOG.error("Exception while processing holdings to create", e);
                holdings.setResult(Holdings.ResultType.FAILURE);
                holdings.setMessage(e.getErrorMessage());
            } catch (Exception e) {
                LOG.error("Exception while processing holdings to create", e);
                holdings.setResult(Holdings.ResultType.FAILURE);
                holdings.setMessage(e.getMessage());
            }
        } else if (Holdings.OperationType.UPDATE.equals(holdings.getOperation())) {
            try {
                updateHoldings(holdings);
                holdings.setResult(Holdings.ResultType.SUCCESS);
            } catch (DocstoreException e) {
                LOG.error("Exception while processing holdings to update", e);
                holdings.setResult(Holdings.ResultType.FAILURE);
                holdings.setMessage(e.getErrorMessage());
            } catch (Exception e) {
                LOG.error("Exception while processing holdings to update", e);
                holdings.setResult(Holdings.ResultType.FAILURE);
                holdings.setMessage(e.getMessage());
            }
        } else if (Holdings.OperationType.DELETE.equals(holdings.getOperation())) {
            try {
                deleteHoldings(holdings.getId());
                holdings.setResult(Holdings.ResultType.SUCCESS);
            } catch (DocstoreException e) {
                LOG.error("Exception while processing holdings to delete", e);
                holdings.setResult(Holdings.ResultType.FAILURE);
                holdings.setMessage(e.getErrorMessage());
            } catch (Exception e) {
                LOG.error("Exception while processing holdings to delete", e);
                holdings.setResult(Holdings.ResultType.FAILURE);
                holdings.setMessage(e.getMessage());
            }
        }
    }

    /**
     * This method is for batch updates to items.
     *
     * @param items
     * @param holdings
     */
    private void processItems(List<Item> items, Holdings holdings) {
        if (CollectionUtils.isNotEmpty(items)) {
            for (Item item : items) {
                item.setHolding(holdings);
                processItem(item);
            }
        }
    }

    /**
     * This method is for batch updates to item.
     *
     * @param item
     */
    private void processItem(Item item) {
        if (item != null) {
            if (Item.OperationType.CREATE.equals(item.getOperation())) {
                try {
                    createItem(item);
                    item.setResult(Item.ResultType.SUCCESS);
                } catch (DocstoreException e) {
                    LOG.error("Exception while processing item to create", e);
                    item.setResult(Item.ResultType.FAILURE);
                    item.setMessage(e.getErrorMessage());
                } catch (Exception e) {
                    LOG.error("Exception while processing item to create", e);
                    item.setResult(Item.ResultType.FAILURE);
                    item.setMessage(e.getMessage());
                }
            } else if (Item.OperationType.UPDATE.equals(item.getOperation())) {
                try {
                    updateItem(item);
                    item.setResult(Item.ResultType.SUCCESS);
                } catch (DocstoreException e) {
                    LOG.error("Exception while processing item to update", e);
                    item.setResult(Item.ResultType.FAILURE);
                    item.setMessage(e.getErrorMessage());
                } catch (Exception e) {
                    LOG.error("Exception while processing item to update", e);
                    item.setResult(Item.ResultType.FAILURE);
                    item.setMessage(e.getMessage());
                }
            } else if (Item.OperationType.DELETE.equals(item.getOperation())) {
                try {
                    deleteItem(item.getId());
                    item.setResult(Item.ResultType.SUCCESS);
                } catch (DocstoreException e) {
                    LOG.error("Exception while processing item to delete", e);
                    item.setResult(Item.ResultType.FAILURE);
                    item.setMessage(e.getErrorMessage());
                } catch (Exception e) {
                    LOG.error("Exception while processing item to delete", e);
                    item.setResult(Item.ResultType.FAILURE);
                    item.setMessage(e.getMessage());
                }
            }
        }
    }

    public void unbindWithOneBib(List<String> holdingsIds, String bibId) {
        RdbmsHoldingsDocumentManager rdbmsHoldingsDocumentManager = RdbmsHoldingsDocumentManager.getInstance();
        rdbmsHoldingsDocumentManager.unbindWithOneBib(holdingsIds, bibId);
    }

    public void unbindWithAllBibs(List<String> holdingsIds, String bibId) {
        RdbmsHoldingsDocumentManager rdbmsHoldingsDocumentManager = RdbmsHoldingsDocumentManager.getInstance();
        rdbmsHoldingsDocumentManager.unbindWithAllBibs(holdingsIds, bibId);
    }

    @Override
    public void saveDeletedBibs(List<Bib> bibs) throws Exception {
        try{
            RdbmsBibDocumentManager documentManager = RdbmsBibDocumentManager.getInstance();
            LOG.info("Started to save deleted Bibs");
            documentManager.saveDeletedBibs(bibs);
            LOG.info("Finished Saving deleted Bibs");
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void saveDeletedHolding(Holdings holding){
        RdbmsBibDocumentManager documentManager = RdbmsBibDocumentManager.getInstance();
        LOG.info("Started to save deleted Bibs");
        documentManager.saveDeletedHolding(holding);
        LOG.info("Finished Saving deleted Bibs");
    }

    @Override
    public void saveDeletedItem(Item item) {
        RdbmsBibDocumentManager documentManager = RdbmsBibDocumentManager.getInstance();
        LOG.info("Started to save deleted Bibs");
        documentManager.saveDeletedItem(item);
        LOG.info("Finished Saving deleted Bibs");
    }


}

