package org.kuali.ole.oleng.batch.utils;

import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.BibTrees;
import org.kuali.ole.sys.context.SpringContext;

/**
 * Created by SheikS on 11/25/2015.
 */
public class DocstoreAPIClient {

    private DocstoreClientLocator docstoreClientLocator;

    public BibTrees createOrUpdateBibTrees(BibTrees bibTrees) throws Exception {
        BibTrees savedBibTrees = getDocstoreClientLocator().getDocstoreClient().processBibTrees(bibTrees);
        return savedBibTrees;
    }

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }
}
