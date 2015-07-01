package org.kuali.ole.docstore.common.client.impl;

import org.apache.log4j.Logger;
import org.kuali.ole.docstore.common.client.DocstoreClient;
import org.kuali.ole.docstore.common.client.DocstoreRestClient;

/**
 * Created by pvsubrah on 6/10/15.
 */
public class NewDocstoreClientLocator {
    private static final Logger LOG = Logger.getLogger(NewDocstoreClientLocator.class);
    static NewDocstoreClientLocator docstoreClientLocator;

    private NewDocstoreClientLocator() {
    }

    public static NewDocstoreClientLocator getInstance() {
        if (null == docstoreClientLocator) {
            docstoreClientLocator = new NewDocstoreClientLocator();
        }

        return docstoreClientLocator;
    }

    public DocstoreClient getDocstoreClient(Boolean isLocal) {
        DocstoreClient docstoreClient = null;
        try {
            if (isLocal) {
                docstoreClient = (DocstoreClient) Class.forName("org.kuali.ole.docstore.engine.client.DocstoreLocalClient").newInstance();
                LOG.info("DocstoreLocalClient successfully instantiated");
            } else {
                docstoreClient = new DocstoreRestClient();
                LOG.info("DocstoreRestClient successfully instantiated");
            }
        } catch (ClassNotFoundException cnfe) {
            LOG.error(cnfe.getStackTrace());
        } catch (InstantiationException e) {
            LOG.error(e.getStackTrace());
        } catch (IllegalAccessException e) {
            LOG.error(e.getStackTrace());
        }
        return docstoreClient;
    }
}
