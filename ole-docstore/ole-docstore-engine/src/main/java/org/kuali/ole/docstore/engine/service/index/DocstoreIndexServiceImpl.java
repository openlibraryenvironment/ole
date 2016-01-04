package org.kuali.ole.docstore.engine.service.index;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.content.enums.DocCategory;
import org.kuali.ole.docstore.common.document.content.enums.DocFormat;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.discovery.service.SolrServerManager;
import org.kuali.ole.docstore.engine.factory.DocumentIndexerManagerFactory;
import org.kuali.ole.docstore.engine.service.index.solr.BibMarcIndexer;
import org.kuali.ole.docstore.engine.service.index.solr.DocumentIndexer;
import org.kuali.ole.docstore.engine.service.index.solr.HoldingsOlemlIndexer;
import org.kuali.ole.docstore.engine.service.index.solr.ItemOlemlIndexer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 12/13/13
 * Time: 6:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocstoreIndexServiceImpl implements DocstoreIndexService, DocstoreConstants {

    private static final Logger LOG = LoggerFactory.getLogger(DocstoreIndexServiceImpl.class);

    @Override
    public void createBib(Bib bib) {
        DocumentIndexer documentIndexer = DocumentIndexerManagerFactory.getInstance().getDocumentIndexManager(bib.getCategory(), bib.getType(), bib.getFormat());
        documentIndexer.create(bib);
    }

    @Override
    public void createHoldings(Holdings holdings) {
        DocumentIndexer documentIndexer = DocumentIndexerManagerFactory.getInstance().getDocumentIndexManager(holdings.getCategory(), holdings.getType(), holdings.getFormat());
        documentIndexer.create(holdings);
    }

    @Override
    public void createItem(Item item) {
        DocumentIndexer documentIndexer = DocumentIndexerManagerFactory.getInstance().getDocumentIndexManager(item.getCategory(), item.getType(), item.getFormat());
        documentIndexer.create(item);
    }

    @Override
    public void createHoldingsTree(HoldingsTree holdingsTree) {

        DocumentIndexer documentIndexer = HoldingsOlemlIndexer.getInstance();
        documentIndexer.createTree(holdingsTree);


//        createHoldings(holdingsTree.getHoldings(), bibId);
//        for(Item item: holdingsTree.getItems()) {
//            createItem(item, holdingsTree.getHoldings().getId());
//        }
    }

    @Override
    public void createBibTree(BibTree bibTree) {
        DocumentIndexer documentIndexer = BibMarcIndexer.getInstance();
        documentIndexer.createTree(bibTree);


//        createBib(bibTree.getBib());
//
//        for(HoldingsTree holdingsTree : bibTree.getHoldingsTrees()) {
//            createHoldingsTree(holdingsTree, bibTree.getBib().getId());
//        }
    }

    @Override
    public void updateBib(Bib bib) {
        DocumentIndexer documentIndexer = DocumentIndexerManagerFactory.getInstance().getDocumentIndexManager(bib.getCategory(), bib.getType(), bib.getFormat());
        documentIndexer.update(bib);
    }

    @Override
    public void updateBibs(List<Bib> bibs) {
        for (Bib bib : bibs) {
            DocumentIndexer documentIndexer = DocumentIndexerManagerFactory.getInstance().getDocumentIndexManager(bib.getCategory(), bib.getType(), bib.getFormat());
            documentIndexer.update(bib);
        }
    }

    @Override
    public void updateHoldings(Holdings holdings) {
        DocumentIndexer documentIndexer = DocumentIndexerManagerFactory.getInstance().getDocumentIndexManager(DocCategory.WORK.getCode(), DocType.HOLDINGS.getCode(), DocFormat.OLEML.getCode());
        documentIndexer.update(holdings);
    }

    @Override
    public void updateItem(Item item) {
        DocumentIndexer documentIndexer = DocumentIndexerManagerFactory.getInstance().getDocumentIndexManager(DocCategory.WORK.getCode(), DocType.ITEM.getCode(), DocFormat.OLEML.getCode());
        documentIndexer.update(item);

    }

    @Override
    public void deleteBib(String bibId) {
        DocumentIndexer documentIndexer = BibMarcIndexer.getInstance();
        documentIndexer.delete(bibId);

    }


    public void deleteBatchBib(String bibId) {
        DocumentIndexer documentIndexer = BibMarcIndexer.getInstance();
        documentIndexer.deleteBatch(bibId);

    }

    @Override
    public void deleteHoldings(String holdingsId) {
        DocumentIndexer documentIndexer = HoldingsOlemlIndexer.getInstance();
        documentIndexer.delete(holdingsId);

    }

    @Override
    public void deleteItem(String itemId) {
        DocumentIndexer documentIndexer = ItemOlemlIndexer.getInstance();
        documentIndexer.delete(itemId);

    }

    @Override
    public void boundHoldingsWithBibs(String holdingsId, List<String> bibIds) {
        DocumentIndexer documentIndexer = BibMarcIndexer.getInstance();
        try {
            documentIndexer.bind(holdingsId, bibIds);
        } catch (SolrServerException e) {
            LOG.info("Exception :", e);
        } catch (IOException e) {
            LOG.info("Exception :", e);
        }
    }

    @Override
    public void transferHoldings(List<String> holdingsIds, String bibId) {
        DocumentIndexer documentIndexer = HoldingsOlemlIndexer.getInstance();
        documentIndexer.transfer(holdingsIds, bibId);
    }

    @Override
    public void transferItems(List<String> itemIds, String holdingsId) {
        DocumentIndexer documentIndexer = ItemOlemlIndexer.getInstance();
        documentIndexer.transfer(itemIds, holdingsId);
    }

    @Override
    public void createBibTrees(BibTrees bibTrees) {

        DocumentIndexer documentIndexer = BibMarcIndexer.getInstance();
        documentIndexer.createTrees(bibTrees);
    }

    @Override
    public void deleteBibs(List<String> bibIds) {
        for (String id : bibIds) {
            deleteBib(id);
        }
        DocumentIndexer documentIndexer = BibMarcIndexer.getInstance();
        try {
            SolrServer server = SolrServerManager.getInstance().getSolrServer();
            documentIndexer.commitRecordsToSolr(server);
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void createLicense(License license) {
        DocumentIndexer documentIndexer = DocumentIndexerManagerFactory.getInstance().getDocumentIndexManager(license.getCategory(), license.getType(), license.getFormat());
        documentIndexer.create(license);
    }

    @Override
    public void createLicenses(Licenses licenses) {
        DocumentIndexer documentIndexer = DocumentIndexerManagerFactory.getInstance().getDocumentIndexManager(licenses.getLicenses().get(0).getCategory(), licenses.getLicenses().get(0).getType(), licenses.getLicenses().get(0).getFormat());
        documentIndexer.createTrees(licenses);
    }

    @Override
    public void updateLicense(License license) {
        DocumentIndexer documentIndexer = DocumentIndexerManagerFactory.getInstance().getDocumentIndexManager(license.getCategory(), license.getType(), license.getFormat());
        documentIndexer.update(license);
    }

    @Override
    public void updateLicenses(Licenses licenses) {
        for(License license : licenses.getLicenses()) {
            DocumentIndexer documentIndexer = DocumentIndexerManagerFactory.getInstance().getDocumentIndexManager(license.getCategory(), license.getType(), license.getFormat());
            documentIndexer.update(license);
        }
    }

    @Override
    public void deleteLicense(String id) {
        DocumentIndexer documentIndexer = DocumentIndexerManagerFactory.getInstance().getDocumentIndexManager(DocCategory.WORK.getCode(), DocType.LICENSE.getCode(), DocFormat.ONIXPL.getCode());
        documentIndexer.delete(id);
    }

    @Override
    public void createAnalyticsRelation(String seriesHoldingsId, List<String> itemIds) {
        DocumentIndexer documentIndexer = BibMarcIndexer.getInstance();
        try {
            documentIndexer.bindAnalytics(seriesHoldingsId, itemIds, CREATE_RELATION);
        } catch (SolrServerException e) {
            LOG.info("Exception occurred while creating analytic relation :", e);
        } catch (IOException e) {
            LOG.info("Exception occurred while creating analytic relation :", e);
        }
    }

    @Override
    public void breakAnalyticsRelation(String seriesHoldingsId, List<String> itemIds) {
        DocumentIndexer documentIndexer = BibMarcIndexer.getInstance();
        try {
            documentIndexer.bindAnalytics(seriesHoldingsId, itemIds, BREAK_RELATION);
        } catch (SolrServerException e) {
            LOG.info("Exception occurred while breaking analytic relation :", e);
        } catch (IOException e) {
            LOG.info("Exception occurred while breaking analytic relation :", e);
        }
    }

    @Override
    public void  processBibTrees(BibTrees bibTrees) {
        DocumentIndexer documentIndexer = BibMarcIndexer.getInstance();
        documentIndexer.processBibTrees(bibTrees);
    }

    @Override
    public void unbindWithOneBib(List<String> holdingsIds, String bibId) {
        DocumentIndexer documentIndexer = BibMarcIndexer.getInstance();
        try {
            documentIndexer.unbindOne(holdingsIds, bibId);
        } catch (SolrServerException e) {
            LOG.info("Exception :", e);
        } catch (IOException e) {
            LOG.info("Exception :", e);
        }
    }

    @Override
    public void unbindWithAllBibs(List<String> holdingsIds, String bibId) {
        DocumentIndexer documentIndexer = BibMarcIndexer.getInstance();
        try {
            documentIndexer.unbindAll(holdingsIds, bibId);
        } catch (SolrServerException e) {
            LOG.info("Exception :", e);
        } catch (IOException e) {
            LOG.info("Exception :", e);
        }
    }

}